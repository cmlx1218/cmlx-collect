package com.cmlx.commons.support;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

import java.util.List;
import java.util.Map;

/**
 * @Author CMLX
 * @Date -> 2021/5/7 18:33
 * @Desc ->
 **/
public class JwtUtility {
    public static Map getMap(String token) throws Exception {
        //解码token，转换成map格式
        Jwt decode = JwtHelper.decode(token);
        return JsonUtility.toMap(decode.getClaims());
    }

    public static String getProperty(String token,String key) throws Exception {
        //解码token，
        Map map = getMap(token);
        Object value = map.get(key);
        if (null != value) {
            return String.valueOf(value);
        }
        return null;
    }

    public static Long  getUid(String token) throws Exception {
        Map map = getMap(token);
        String uid = String.valueOf(map.get("uid"));
        return Long.valueOf(uid);
    }
    public static String  getClientId(String token) throws Exception {
        Map map = getMap(token);
        return String.valueOf(map.get("client_id"));
    }
    public static List<String> getScopes(String token) throws Exception {
        Map map = getMap(token);
        return (List<String>) map.get("scope");
    }

    /**
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static String getSub(String token) throws Exception {
        Map map = getMap(token);
        String sub = String.valueOf(map.get("sub"));
        return sub;
    }
}

