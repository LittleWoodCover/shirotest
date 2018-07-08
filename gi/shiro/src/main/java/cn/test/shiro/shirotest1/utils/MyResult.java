package cn.test.shiro.shirotest1.utils;

public class MyResult {

    private Integer status;
    private String msg;
    private Object object;

    public MyResult(Integer status, String msg, Object object) {
        this.status = status;
        this.msg = msg;
        this.object = object;
    }

    public static MyResult ok(){
        return new MyResult(1,"ok",null);
    }

    public static MyResult error(){
        return new MyResult(0,"error",null);
    }
}
