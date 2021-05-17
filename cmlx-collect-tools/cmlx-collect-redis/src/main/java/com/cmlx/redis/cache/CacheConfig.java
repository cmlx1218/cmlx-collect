package com.cmlx.redis.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author CMLX
 * @Date -> 2021/5/17 16:57
 * @Desc ->
 **/
@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class CacheConfig {
    private String nameSpace;
    private long expires;

    public static Builder custom() {
        return new Builder();
    }

    public static Builder copy(final CacheConfig config) {
        if(null == config) return null;
        return new Builder().nameSpace(config.nameSpace()).expires(config.expires());
    }

    @Setter
    @Accessors(fluent = true)
    public static class Builder {
        private String nameSpace;
        private long expires;

        Builder() {
            expires = 1800L;
        }

        public CacheConfig build() {
            return new CacheConfig(nameSpace, expires);
        }
    }
}

