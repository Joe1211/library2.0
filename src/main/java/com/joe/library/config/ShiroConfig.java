package com.joe.library.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @authr 乔
 * @Date 2019/9/7
 * shiro配置类
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /**
         * Shiro内置过滤器，可以实现权限相关的拦截器
         *  anon:无需认证（登录）可以实现访问
         *  authc:必须认证才可以访问
         *  user:如果使用rememberMe的功能可以直接访问
         *  perms:改资源必须得到资源权限才可以访问
         *  role:改资源必须得到角色权限才可以访问
         */
        Map<String,String> filterMap = new LinkedHashMap<>();

        //无需认证页面
        filterMap.put("/index","user");
        filterMap.put("/login","anon");
        filterMap.put("/regist","anon");
        filterMap.put("/verification","anon");
        filterMap.put("/user/regist","anon");
        filterMap.put("/admin/index","user");

        //拦截页面
        filterMap.put("/*","authc");

        //成功跳转页面
        shiroFilterFactoryBean.setSuccessUrl("/index");

        //跳转页面
        shiroFilterFactoryBean.setLoginUrl("/login");

        //无权限页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm ){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //关联realm
        defaultWebSecurityManager.setRealm(userRealm);
        defaultWebSecurityManager.setRememberMeManager(rememberMeManager());
        return defaultWebSecurityManager;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5"); // 散列算法
        hashedCredentialsMatcher.setHashIterations(2); // 散列次数
        return hashedCredentialsMatcher;
    }

    /**
     * 创建Realm
     */
    @Bean(name = "userRealm")
    public UserRealm shiroRealm() {
        UserRealm shiroRealm = new UserRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }



    /**
     * 配置Shirodialect,用于thymeleaf和shiro标签的配合使用
     */
    @Bean
    public ShiroDialect getShiro(){
        return new ShiroDialect();
    }

    /**
     * cookie对象
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        // 设置cookie名称，对应login.html页面的<input type="checkbox" name="rememberme"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置cookie的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(86400);
        return cookie;
    }

    /**
     * cookie管理对象
     * @return
     */
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie加密的密钥
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }

}
