package com.cmlx.redis.cache;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.cmlx.commons.support.ReflectionUtility;
import com.cmlx.redis.cache.annotation.CacheConfigList;
import com.cmlx.redis.cache.annotation.CacheConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 17:18
 * @Desc ->
 **/
@Configuration
@EnableConfigurationProperties({RedisProperties.class, RedisCacheProperties.class})
@EnableCaching
@Slf4j
public class RedisConfiguration extends CachingConfigurerSupport {


    //private JedisConnectionFactory connectionFactory;
    private RedisProperties properties;
    private RedisCacheProperties redisCacheProperties;
    private ConcurrentHashMap<String, RedisCacheConfiguration> expires;
    private Set<String> nameSpaces;
    private RedisCacheConfiguration defaultCacheConfig;

    public RedisConfiguration(RedisProperties properties, RedisCacheProperties redisCacheProperties) {
        //this.connectionFactory = factory;
        this.properties = properties;
        this.redisCacheProperties = redisCacheProperties;
        init();
    }

    public void init() {
        try {
            Set<Class<?>> classes = ReflectionUtility.loadClassesByAnnotationClass(
                    new Class[]{CacheConfiguration.class, CacheConfigList.class}, redisCacheProperties.getPackagesToScan().split(","));
            if (!CollectionUtils.isEmpty(classes)) {
                expires = new ConcurrentHashMap<>();
                nameSpaces = new HashSet<>();
                defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                        .computePrefixWith(name -> name + ":")
                        .entryTtl(Duration.ofSeconds(3600 * 24 * 60))
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()))
                        .disableCachingNullValues();
            }
            classes.forEach(aClass -> {
                CacheConfiguration configuration = AnnotationUtils.findAnnotation(aClass, CacheConfiguration.class);
                CacheConfigList configurationList = AnnotationUtils.findAnnotation(aClass, CacheConfigList.class);
                if (null != configuration) {
                    long expTime = configuration.expTime() == 0 ? redisCacheProperties.getExpiredTime() : configuration.expTime();
                    expires.put(configuration.nameSpace(), defaultCacheConfig.entryTtl(Duration.ofSeconds(expTime)));
                    nameSpaces.add(configuration.nameSpace());
                }
                if (null != configurationList) {
                    CacheConfiguration[] value = configurationList.value();
                    for (CacheConfiguration config : value) {
                        long expTime = config.expTime() == 0 ? redisCacheProperties.getExpiredTime() : config.expTime();
                        expires.put(config.nameSpace(), defaultCacheConfig.entryTtl(Duration.ofSeconds(expTime)));
                        nameSpaces.add(config.nameSpace());
                    }
                }
            });
        } catch (IOException e) {
            log.error("RedisCache init error", e);
        } catch (ClassNotFoundException e) {
            log.error("RedisCache init error", e);
        }
    }

    //cacheManager???????????????????????????
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        //RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
        //cacheManager.setUsePrefix(true);
        //if (!CollectionUtils.isEmpty(nameSpaces))
        //    cacheManager.setCacheNames(nameSpaces);
        //if (!CollectionUtils.isEmpty(expires))
        //    cacheManager.setExpires(expires);
        //return cacheManager;
        //RedisCacheManager cacheManager = new RedisCacheManager();
        //cacheManager.setUsePrefix(true);
        //if (!CollectionUtils.isEmpty(nameSpaces))
        //    cacheManager.setCacheNames(nameSpaces);
        //if (!CollectionUtils.isEmpty(expires))
        //    cacheManager.setExpires(expires);
        //return cacheManager;
        //RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        //RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        //        .entryTtl(Duration.ofMinutes(10)).disableCachingNullValues();
        //RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
        //return RedisCacheManager.builder(redisCacheWriter)
        //        .cacheDefaults(defaultCacheConfig)
        //        .initialCacheNames(nameSpaces)
        //        .withInitialCacheConfigurations(expires)
        //        .build();
        RedisSerializer<Object> redisSerializer = getRedisSerializer();
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                // ?????????????????????
                .entryTtl(Duration.ofSeconds(3600 * 24 * 60))
                .computePrefixWith(name -> name + ":")
                // ??????cache key????????????????????????????????????key?????????
                //.computePrefixWith(cacheName -> "yourAppName".concat(":").concat(cacheName).concat(":"))
                // springboot2.0 ????????????????????????
                // ??????key???value??????????????????????????????hash key???hash value????????????
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                // ????????????null??????
                .disableCachingNullValues();
        if (nameSpaces != null) {
            return RedisCacheManager.builder(redisConnectionFactory)
                    // ????????????
                    .cacheDefaults(redisCacheConfiguration)
                    // ??????????????????
                    .initialCacheNames(nameSpaces)
                    // ?????????????????????
                    .withInitialCacheConfigurations(expires)
                    .build();
        }
        return RedisCacheManager.builder(redisConnectionFactory)
                // ????????????
                .cacheDefaults(redisCacheConfiguration)
                //// ??????????????????
                //.initialCacheNames(nameSpaces)
                //// ?????????????????????
                //.withInitialCacheConfigurations(expires)
                .build();
    }

    //@Bean
    ////@Primary
    //public RedisTemplate<String, String> redisTemplate2() {
    //    StringRedisTemplate template = new StringRedisTemplate(connectionFactory);
    //
    //    Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
    //    ObjectMapper om = new ObjectMapper();
    //    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    //    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    //    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    //    jackson2JsonRedisSerializer.setObjectMapper(om);
    //    template.setValueSerializer(jackson2JsonRedisSerializer);
    //    GenericToStringSerializer toStringSerializer = new GenericToStringSerializer(Object.class);
    //    template.setKeySerializer(toStringSerializer);
    //    template.afterPropertiesSet();
    //    return template;
    //}

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        //RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //redisTemplate.setConnectionFactory(redisConnectionFactory);
        //
        //FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
        //// ????????????AutoType??????????????????
        //// ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        //// ???????????????????????????????????????????????????
        //ParserConfig.getGlobalInstance().addAccept("com.xiaolyuh.");
        //
        //// ????????????value?????????????????????FastJsonRedisSerializer???
        //redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        //redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        //// ????????????key?????????????????????StringRedisSerializer???
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //
        //redisTemplate.afterPropertiesSet();
        //return redisTemplate;

        RedisSerializer<Object> redisSerializer = getRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // ??????????????????
        template.setConnectionFactory(factory);

        ////??????Jackson2JsonRedisSerializer???????????????????????????redis???value??????????????????JDK?????????????????????
        //Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        //
        //ObjectMapper om = new ObjectMapper();
        //// ???????????????????????????field,get???set,????????????????????????ANY???????????????private???public
        //om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //// ????????????????????????????????????????????????final????????????final?????????????????????String,Integer??????????????????
        //om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //jacksonSeial.setObjectMapper(om);

        //??????StringRedisSerializer???????????????????????????redis???key???
        template.setKeySerializer(new StringRedisSerializer());
        // ?????????json?????????
        template.setValueSerializer(redisSerializer);

        // ??????hash key ???value???????????????
        template.setHashKeySerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    public RedisSerializer<Object> getRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

    @Override
    public KeyGenerator keyGenerator() {
        return super.keyGenerator();
    }

    @Override
    public CacheResolver cacheResolver() {
        return super.cacheResolver();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return super.errorHandler();
    }
}

