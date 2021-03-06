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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private SysUserService sysUserService;
    @GetMapping("/index1")
    public String index(){
        return "index";
    }

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
    public MyResult logins(@RequestParam(value = "usercode") String usercode,@RequestParam("password") String password){
        UsernamePasswordToken token = new UsernamePasswordToken(usercode, password);
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
        SysUser user = sysUserService.getUserByName(usercode);
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

    @PostMapping(value = "/login1")
    public String login1(HttpServletRequest request, Map<String,Object> map){
        System.out.println("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");
        System.out.println("exception=" + exception);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                System.out.println("UnknownAccountException -- > 账号不存在：");
                msg = "UnknownAccountException -- > 账号不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "IncorrectCredentialsException -- > 密码不正确：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                System.out.println("kaptchaValidateFailed -- > 验证码错误");
                msg = "kaptchaValidateFailed -- > 验证码错误";
            } else {
                msg = "else >> "+exception;
                System.out.println("else -- >" + exception);
            }
        }
        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理
        return "ok";

    }
}
