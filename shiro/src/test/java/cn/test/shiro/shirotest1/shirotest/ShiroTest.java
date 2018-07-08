package cn.test.shiro.shirotest1.shirotest;

import cn.test.shiro.shirotest1.config.MyShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class ShiroTest {

    @Test
    public void test1(){
        MyShiroRealm myShiroRealm=new MyShiroRealm();
        myShiroRealm.setCredentialsMatcher(myShiroRealm.setMyShiroRealmSalt());
        //1创建一个securityManager对象
        DefaultSecurityManager defaultSecurityManager =new DefaultSecurityManager();

        //将用户信息设置到securityManager对象中
        defaultSecurityManager.setRealm(myShiroRealm);
        //2主体来提交认证请求

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        //3认证
        UsernamePasswordToken token=new UsernamePasswordToken("zhangsan","111111");
        //
        subject.login(token);
        System.out.println("是否认证通过:"+subject.isAuthenticated());

    }


}
