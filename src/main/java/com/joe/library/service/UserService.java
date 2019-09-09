package com.joe.library.service;

import com.joe.library.mapper.UserMapper;
import com.joe.library.mapper.UserRoleMapper;
import com.joe.library.pojo.User;
import com.joe.library.pojo.UserRole;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @authr 乔
 * @Date 2019/9/8
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;//操作k-v都是字符串的

    /**
     * 生成验证码
     * @param email
     */
    public String getVerification(String email){
        //生成4为验证码
        Integer code = (int)((Math.random()*9+1)*1000);
        //将验证码放入redis中，5分钟后过期
        stringRedisTemplate.opsForValue().set(email,code.toString(),60*5, TimeUnit.SECONDS);
        return code.toString();
    }

    /**
     * 添加用户并赋予角色权限
     * @param user
     * @return
     */
    public int registUser(User user){
        int i = userMapper.insert(user);
        //默认给user角色（1）
        UserRole userRole = new UserRole(user.getId(),1);
        int i1 = userRoleMapper.insert(userRole);
        return i1;
    }

    /**
     * 更具username查询
     * @param username
     * @return
     */
    public User selectByUsername(String username){
        return userMapper.selectByUserName(username);
    }

    /**
     * 判断验证码是否正确
     * @param key
     * @param verification
     * @return
     */
    public boolean isVerification(String key,String verification){
        String code = stringRedisTemplate.opsForValue().get(key);
        if (verification.equals(code)){
            return true;
        }
        return false;
    }
}
