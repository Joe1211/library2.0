package com.joe.library;

import com.joe.library.mapper.UserMapper;
import com.joe.library.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LibraryApplicationTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    UserMapper userMapper;


    @Test
    public void contextLoads() {
        User user = new User();
        user.setUserName("163@qq.com");
        user.setPassword("123");
        userMapper.insert(user);
        System.out.println(user.getId());
    }
    }


