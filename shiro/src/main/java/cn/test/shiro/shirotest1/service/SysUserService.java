package cn.test.shiro.shirotest1.service;

import cn.test.shiro.shirotest1.entity.SysPermission;
import cn.test.shiro.shirotest1.entity.SysUser;

import java.util.List;

public interface SysUserService {
   SysUser getUserByName(String name);
   SysUser getUserByCode(String usercode);
   SysUser doLogin(String name,String password);
   List<SysPermission> getPermissions(String username);
   List<SysUser> getAllUsers();
}
