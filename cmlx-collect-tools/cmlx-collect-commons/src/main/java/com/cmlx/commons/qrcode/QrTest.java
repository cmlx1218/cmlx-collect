package com.cmlx.commons.qrcode;

import com.alibaba.fastjson.JSON;
import com.cmlx.commons.constant.Constant;
import com.cmlx.commons.constant.ErrorCode;
import com.cmlx.commons.encrypt.EncryptorUtility;
import com.cmlx.commons.exception.EXPF;
import com.cmlx.commons.jsoup.UrlDto;
import com.cmlx.commons.support.StringUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:38
 * @Desc ->
 **/
public class QrTest {

    @Autowired
    private Environment environment;
    @Autowired
    private EncryptorUtility encryptorUtility;

    private static final String REDIRECT_URL_PREFIX = "cmlx://";
    private static final StringBuilder baseUrl = new StringBuilder(REDIRECT_URL_PREFIX);
    private static final String QR_SALT = "cmlx2021";
    private final Long GroupUrlExpire = 60L * 60L * 24L * 3L;
    //private String getJoinGroupInfoKey(String joinCode) {
    //    return getCacheKey(CacheConstant.AIMY_FITNESS_SHORT_URL, "join_group") + CacheConstant.COLON + joinCode;
    //}

    public Map<String, Object> getShortUrl(Integer type, Long targetId, Long uid) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        String redirectUrl = null;
        String shareUrl = null;
        Long expireTime = 1000L;
        String shortUrlPrefix = environment.getProperty("cmlx.shortUrlPrefix");
        Constant.ShortUrlType enumConstant = Constant.ShortUrlType.class.getEnumConstants()[type];
        // 1、获取对应短链接app跳转地址和第三方扫码地址
        switch (enumConstant) {
            case LoginTerminal:
                params.put("mid", targetId);
                redirectUrl = getRedirectUrl(Constant.ShortUrlType.LoginTerminal, params);
                shareUrl = "https://www.baidu.com";
                break;
            case ActiveTerminal:
                params.put("mid", targetId);
                redirectUrl = getRedirectUrl(Constant.ShortUrlType.ActiveTerminal, params);
                shareUrl = "https://www.baidu.com";
                break;
            case JoinGroup:
                if (uid == null || uid == -1) {
                    throw EXPF.exception(ErrorCode.NotLogin.getCode(), "用户未登录", false);
                }
                JoinGroupDto joinGroupDto = new JoinGroupDto().setCreatorId(uid).setGroupId(targetId).setJoinType(Constant.GroupNotificationType.EntryByQRCode);
                String joinCode = encryptorUtility.encryptWithSalt(JSON.toJSONString(joinGroupDto), QR_SALT, EncryptorUtility.OPERATION.MD5);
                // 逻辑代码
                //iUrlCacheService.addJoinGroupCodeInfo(joinCode,joinGroupDto);
                params.put("code", joinCode);
                redirectUrl = getRedirectUrl(Constant.ShortUrlType.JoinGroup, params);
                // 没有h5页面展示都转向百度
                //shareUrl = proxy.getGroupShareUrl(targetId);
                shareUrl = "https://www.baidu.com";
                expireTime = GroupUrlExpire;
                break;
            case UserProfile:
                params.put("uid", targetId);
                redirectUrl = getRedirectUrl(Constant.ShortUrlType.UserProfile, params);
                shareUrl = "https://www.baidu.com";
                expireTime = -1L;
                break;
            case LoginCoach:
                //TODO 后续再做入参不同code区分
                redirectUrl = getRedirectUrl(Constant.ShortUrlType.LoginCoach, params);
                shareUrl = "https://www.baidu.com";
                break;
            case LoginPC:
                //TODO
                redirectUrl = getRedirectUrl(Constant.ShortUrlType.LoginPC, params);
                shareUrl = "https://www.baidu.com";
                break;
        }
        if (redirectUrl == null || shareUrl == null) {
            throw EXPF.exception(ErrorCode.ResourceNotFound.getCode(), "不支持的短连接生成类型", true);
        }
        // 获取指定分享链接md5
        String md5 = encryptorUtility.encryptWithSalt(redirectUrl, QR_SALT, EncryptorUtility.OPERATION.MD5);
        // 通过md5获取短连接code
        String shortCode = getShortCodeByMd5(md5);
        // 如果有短连接code，就根据短连接code获取相应的urlInfo
        if (shortCode != null) {
            UrlDto urlDto = getUrlByShortCode(shortCode, targetId, redirectUrl, shareUrl, expireTime, uid);
            if (urlDto != null) {
                map.put("type", type);
                map.put("url", shortUrlPrefix + shortCode);
                map.put("expireTime", expireTime);
                return map;
            }
        }
        // 如果没有短连接code，需要生成新的code和对应的urlInfo
        Long next = next();
        String newShortCode = StringUtility.long2String(next);
        saveMd5ShortCode(md5, newShortCode);
        saveShortCodeUrl(newShortCode, targetId, redirectUrl, shareUrl, expireTime, uid);
        map.put("type", type);
        map.put("url", shortUrlPrefix + newShortCode);
        map.put("expireTime", expireTime);
        return map;
        // 登录终端
        //if (type == Constant.ShortUrlType.LoginTerminal.ordinal()) {
        //    command = Constant.ShortUrlType.LoginTerminal.getVal();
        //    filedName = "?mid=";
        //}
        //// 激活终端
        //else if (type == Constant.ShortUrlType.ActiveTerminal.ordinal()) {
        //    command = Constant.ShortUrlType.ActiveTerminal.getVal();
        //    filedName = "?mid=";
        //}
        //// 扫码加入群聊
        //else if (type == Constant.ShortUrlType.JoinGroup.ordinal()) {
        //    command = Constant.ShortUrlType.JoinGroup.getVal();
        //    filedName = "?groupId=";
        //}
        //if (command == null) {
        //    throw EXPF.exception(ErrorCode.ResourceNotFound.getCode(), "没有该类型短链接生成", false);
        //}
        //String url = Prefix + command + filedName + targetId;
        //String md5 = encryptorUtility.encryptWithSalt(url, Salt, EncryptorUtility.OPERATION.MD5);
        //String shortCode = iUrlCacheService.getShortCodeByMd5(md5);
        //if (shortCode != null) {
        //    UrlDto urlDto = iUrlCacheService.getUrlByShortCode(shortCode, targetId);
        //    if (urlDto != null && urlDto.getUrl().equals(url)) {
        //        map.put("type", type);
        //        map.put("url", shortUrlPrefix + shortCode);
        //        return map;
        //    }
        //}
        //Long next = iUrlCacheService.next();
        //String newShortCode = StringUtility.long2String(next);
        //UrlDto urlDto = new UrlDto();
        //urlDto.setShortCode(newShortCode);
        //urlDto.setUrl(url);
        //iUrlCacheService.saveMd5ShortCode(md5, newShortCode);
        //iUrlCacheService.saveShortCodeUrl(urlDto, targetId);
        //map.put("type", type);
        //map.put("url", shortUrlPrefix + newShortCode);
        //return map;
    }

    public String getRedirectUrl(Constant.ShortUrlType shortUrlType, Map<String, Object> params) {
        StringBuilder builder = new StringBuilder(baseUrl).append(shortUrlType.getVal());
        if (CollectionUtils.isEmpty(params)) {
            return builder.toString();
        }

        StringBuilder paramBuilder = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            paramBuilder.append("&").append(param.getKey()).append("=").append(param.getValue());
        }
        builder.append("?");
        builder.append(paramBuilder.substring(1, paramBuilder.length()));
        return builder.toString();
    }

    public void addJoinGroupCodeInfo(String joinCode, JoinGroupDto joinGroupDto) {
        //String joinGroupInfoKey = getJoinGroupInfoKey(joinCode);
        //if (!redisTemplate.hasKey(joinGroupInfoKey)) {
        //    redisTemplate.opsForValue().set(joinGroupInfoKey, JSON.toJSONString(joinGroupDto));
        //    expire(joinGroupInfoKey, GroupUrlExpire);
        //}
    }

    public String getShortCodeByMd5(String md5) {
        //String shortCodeKey = getShortCodeKey();
        //HashOperations hashOperations = redisTemplate.opsForHash();
        //Object shortCode = hashOperations.get(shortCodeKey, md5);
        //return shortCode == null ? null : (String) shortCode;
        return null;
    }

    public UrlDto getUrlByShortCode(String shortCode, Long targetId, String redirectUrl, String shareUrl, Long expireTime, Long uid) {
        //String urlKey = getUrlKey();
        //HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        //String urlInfo = hashOperations.get(urlKey, shortCode);
        //if (urlInfo == null) {
        //    return null;
        //}
        //UrlDto urlDto = new UrlDto();
        //urlDto.setShortCode(shortCode);
        //urlDto.setUrlInfo(urlInfo);
        //String urlExpireKey = getUrlExpireKey(urlDto.getShortCode());
        //UrlInfoDto urlInfoDto = new UrlInfoDto();
        //urlInfoDto.setTargetId(targetId).setRedirectUrl(redirectUrl).setShareUrl(shareUrl).setCreatorId(uid);
        //redisTemplate.opsForHash().put(urlExpireKey, urlDto.getShortCode(), JSON.toJSONString(urlInfoDto));
        //if (expireTime != -1L) {
        //    expire(urlExpireKey, expireTime);
        //}
        //return urlDto;
        return null;
    }

    public Long next() {
        //String idGenerator = getCacheKey(CacheConstant.AIMY_FITNESS_SHORT_URL, "idGenerator");
        //BoundHashOperations boundHashOperations = template.boundHashOps(idGenerator);
        //return boundHashOperations.increment(IdKey, STEP_SIZE);
        return null;
    }

    public void saveMd5ShortCode(String md5, String newShortCode) {
        //String shortCodeKey = getShortCodeKey();
        //redisTemplate.opsForHash().put(shortCodeKey, md5, newShortCode);
    }

    public void saveShortCodeUrl(String newShortCode, Long targetId, String redirectUrl, String shareUrl, Long expireTime, Long uid) {
        //String urlKey = getUrlKey();
        //UrlInfoDto urlInfoDto = new UrlInfoDto();
        //urlInfoDto.setTargetId(targetId).setRedirectUrl(redirectUrl).setShareUrl(shareUrl).setCreatorId(uid);
        //redisTemplate.opsForHash().put(urlKey, newShortCode, JSON.toJSONString(urlInfoDto));
        //String urlExpireKey = getUrlExpireKey(newShortCode);
        //redisTemplate.opsForHash().put(urlExpireKey, newShortCode, JSON.toJSONString(urlInfoDto));
        //if (expireTime != -1L) {
        //    expire(urlExpireKey, expireTime);
    }
}

