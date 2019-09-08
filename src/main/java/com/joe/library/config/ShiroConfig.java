package com.joe.library.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
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
        filterMap.put("/login","anon");
        filterMap.put("/regist","anon");
        filterMap.put("/verification","anon");
        filterMap.put("/user/regist","anon");

        //拦截页面
        filterMap.put("/*","authc");

        //跳转页面
        shiroFilterFactoryBean.setLoginUrl("/login");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm ){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //关联realm
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5"); // 散列算法
        hashedCredentialsMatcher.setHashIterations(1); // 散列次数
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

}
