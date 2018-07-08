package cn.test.shiro.shirotest1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserRequestController {

    @RequestMapping("/index2")
    public String index() {
        System.out.println("here=============================");
       return "index";
    }
}
