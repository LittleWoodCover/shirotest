package cn.test.shiro.shirotest1.service.impl;

import cn.test.shiro.shirotest1.dao.SysUserMapper;
import cn.test.shiro.shirotest1.entity.SysPermission;
import cn.test.shiro.shirotest1.entity.SysUser;
import cn.test.shiro.shirotest1.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    public SysUser getUserByName(String name) {
        return sysUserMapper.getUserByName(name);
    }

    @Override
    public SysUser getUserByCode(String usercode) {
        return sysUserMapper.getUserByCode(usercode);
    }

    @Override
    public SysUser doLogin(String name, String password) {
        return sysUserMapper.doLogin(name,password);
    }

    @Override
    public List<SysPermission> getPermissions(String username) {

        return sysUserMapper.getPermissions(username);
    }

    @Override
    public List<SysUser> getAllUsers() {
        return sysUserMapper.getAllUsers();
    }
}
