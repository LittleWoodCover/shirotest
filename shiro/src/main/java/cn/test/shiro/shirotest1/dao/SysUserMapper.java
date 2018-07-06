package cn.test.shiro.shirotest1.dao;

import cn.test.shiro.shirotest1.entity.SysPermission;
import cn.test.shiro.shirotest1.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SysUserMapper {
     SysUser getUserByName(String name);
     SysUser getUserByCode(String usercode);
     SysUser doLogin(@Param("name") String name, @Param("password") String password);
     List<SysPermission> getPermissions(String usercode);
     List<SysUser> getAllUsers();
}
