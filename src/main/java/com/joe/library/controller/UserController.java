package com.joe.library.controller;

import com.joe.library.Util.RestMsg;
import com.joe.library.pojo.User;
import com.joe.library.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

/**
 * @authr 乔
 * @Date 2019/9/7
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    //发送邮件
    @Autowired
    JavaMailSender mailSender;

    /**
     * 跳转到登录页面
     * @return
     */
    @GetMapping("/login")
    public String toLogin(){
        return "login";
    }

    /**
     * 跳转到注册页面
     * @return
     */
    @GetMapping("/regist")
    public String toRegist(){
        return "regist";
    }

    @GetMapping("/index")
    public String toIndex(){
        return "index";
    }

    /**
     * 用户注册
     * @param user
     * @param verificationCode
     * @return
     */
    @GetMapping("/user/regist")
    @ResponseBody
    public RestMsg<Object> regist(User user, String verificationCode){
        RestMsg<Object> restMsg = new RestMsg<>();
        //判断验证码是否正确
        boolean verification1 = userService.isVerification(user.getUserName(), verificationCode);
        if(verification1){
            //给密码进行盐值加密
            String hashAlgorithnName="MD5";
            Object credentials = user.getPassword();
            Object salt = ByteSource.Util.bytes(user.getUserName());
            int hashIterations = 2;
            String result = new SimpleHash(hashAlgorithnName,credentials,salt,hashIterations).toString();
            user.setPassword(result);
            //默认nickName和username一样
            user.setNickName(user.getUserName());
            userService.registUser(user);
            return restMsg.successMsg("注册成功，前往登录页面");
        }else{
            return restMsg.errorMsg("验证码不正确");
        }
    }

    @PostMapping("user/login")
    public String login(User user, Model model){
        /**
         * 使用shiro编写认证操作
         */
        //获取Subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户名密码
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword());
        //执行登录方法
        try{
        subject.login(token);
        return "redirect:/index";
        }catch (UnknownAccountException e){
            //登录失败，用户不存在
            model.addAttribute("msg","用户不存在");
            return "login";
        }catch (IncorrectCredentialsException e){
            //登录失败，密码错误
            model.addAttribute("msg","密码错误");
            return "login";
        }
    }

    /**
     * 注册时验证邮箱是否注册
     * @param username
     * @return
     */
    @PostMapping("/user/username")
    @ResponseBody
    public Map byUserName(@RequestParam("userName") String username ){
        Map<String,Boolean> map = new HashMap<>();
        User user = userService.selectByUsername(username);
        if (StringUtils.isEmpty(user)){
            map.put("valid",true);
        }else{
            map.put("valid",false);
        }
        return map;
    }

    /**
     * 发送邮件
     */
    @GetMapping("/verification")
    @ResponseBody
    public void getVerification(String email) throws MessagingException {

        //创建一个复杂的邮件消息
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

        //邮件设置
        helper.setSubject("验证码");
        helper.setText("<p style='color:red'>你的验证码为："+ getVerificationCode(email)+"</p>",true);
        //接收人邮箱
        helper.setTo(email);
        //发送人邮箱
        helper.setFrom("1652744678@qq.com");
        mailSender.send(mimeMessage);
    }

    /**
     * 生成验证码
     * @param email
     * @return
     */
    public String getVerificationCode(String email){
        return userService.getVerification(email);
    }



}
