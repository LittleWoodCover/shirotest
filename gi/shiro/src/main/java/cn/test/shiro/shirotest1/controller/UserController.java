package cn.test.shiro.shirotest1.controller;

import cn.test.shiro.shirotest1.entity.SysPermission;
import cn.test.shiro.shirotest1.utils.MyResult;
import cn.test.shiro.shirotest1.entity.SysUser;
import cn.test.shiro.shirotest1.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private SysUserService sysUserService;

    @PostMapping(value = "/user")
    public SysUser usercheck(@RequestParam  String usercode){
        System.out.print(usercode);
         SysUser user=sysUserService.getUserByName(usercode);
         System.out.print(user.getPassword());
         return  user;
    }

    @GetMapping("/hello")
    public String check(){
        return "hello";
    }

    @PostMapping(value = "/login")
    public MyResult logins(@RequestParam(value = "username") String username,@RequestParam("password") String password){
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
            // 捕获密码错误异常
            ModelAndView mv = new ModelAndView("error");
            mv.addObject("message", "password error!");
            return MyResult.error();
        } catch (UnknownAccountException uae) {
            // 捕获未知用户名异常
            ModelAndView mv = new ModelAndView("error");
            mv.addObject("message", "username error!");
            return MyResult.error();
        } catch (ExcessiveAttemptsException eae) {
            // 捕获错误登录过多的异常
            ModelAndView mv = new ModelAndView("error");
            mv.addObject("message", "times error");
            return MyResult.error();
        }
        SysUser user = sysUserService.getUserByName(username);
        subject.getSession().setAttribute("user", user);

        return MyResult.ok();
    }

    @GetMapping(value = "/list")
    @RequiresPermissions("item:query")
    public List<SysUser> listUsers(){
        return sysUserService.getAllUsers();
    }

    @PostMapping(value = "/getPermission")
    public List<SysPermission> getPerMission(@RequestParam String username){
       return  sysUserService.getPermissions(username);
    }
}
