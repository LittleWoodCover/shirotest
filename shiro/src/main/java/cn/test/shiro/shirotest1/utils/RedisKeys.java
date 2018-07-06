package cn.test.shiro.shirotest1.utils;

public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:"+key;
    }

    public static String getShiroSessionKey(String key){
        return "sessionId:"+key;
    }
}
