package cn.test.shiro.shirotest1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.test.shiro.shirotest1.controller","cn.test.shiro.shirotest1.service"})
@MapperScan(basePackages = "cn.test.shiro.shirotest1.dao")
public class ShiroTest1Application {

	public static void main(String[] args) {
		SpringApplication.run(ShiroTest1Application.class, args);
	}
}


