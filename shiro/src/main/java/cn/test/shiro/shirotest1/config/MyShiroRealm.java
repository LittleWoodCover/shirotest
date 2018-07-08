package cn.test.shiro.shirotest1.config;

import cn.test.shiro.shirotest1.entity.SysPermission;
import cn.test.shiro.shirotest1.entity.SysUser;
import cn.test.shiro.shirotest1.service.SysUserService;
import org.apache.coyote.http2.ByteUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义realm授权认证
 */
@Component
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void setName(String name) {
        super.setName("myrealm");
    }

    /**
     * 重写认证方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     * 认证方法返回一个认证之后的信息 SimpleAuthenticationInfo
     */
    public AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("++++++++++++++++++++++++进入认证");
        //获取身份信息（页面输入的账号）
        String usercode= (String) authenticationToken.getPrincipal();
        //调用服务根据用户名获取用户信息
        System.out.println("=============================获取到的用户信息"+usercode);
        SysUser user=sysUserService.getUserByCode(usercode);
//        SysUser user=new SysUser();
//        user.setUsername("zhangsan");
//        user.setPassword("8914684fb7da95dc7b8f6afa3f88fe67");
//        user.setSalt("eteokues");
        user.setLocked(0);

        System.out.println(user.getUsername());
        if(user==null){
            throw new UnknownAccountException("用户名密码错误");
        }
        if(user.getLocked()==1){
            throw new LockedAccountException("账号被锁定请联系管理员");
        }
        System.out.println(user.getPassword()+user.getSalt()+user.getUsername());
        //新建一个认证信息用户存储从数据库中取到的信息用户和subject中的信息对比
        //c参数说明：1可以传入user对象，也可以传入用户名

        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,user.getPassword(),ByteSource.Util.bytes(user.getSalt()),getName());
        return info;
    }

    /**
     * 重写授权方法：
     *
     * @param principalCollection
     * @return
     */
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

         /*
         * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
         * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
         * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
         * 缓存过期之后会再次执行。
         */
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");


        SysUser activeUser = (SysUser) principalCollection.getPrimaryPrincipal();

        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        List<SysPermission> permissions = null;
        try {
            permissions = sysUserService.getPermissions(activeUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ///在认证成功之后返回.
        //设置角色信息.
        //支持 Set集合,
        //用户的角色对应的所权限，如果只使用角色定义访问权限，下面的四行可以不要
        Set<String> permissionsList = new HashSet<>();
        for (SysPermission permission : permissions) {
            System.out.print("查到的permission信息"+permission.getPercode());
            permissionsList.add(permission.getPercode());
        }
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //设置权限信息.
        authorizationInfo.setStringPermissions(permissionsList);
        return authorizationInfo;
    }

    public HashedCredentialsMatcher setMyShiroRealmSalt(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;
    }
}
