package com.yaohan.bbs.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public CustomRealm customRealm(){
        return new CustomRealm();
    }

    @Bean
    public RedisSessionDao redisSessionDao(){
        return new RedisSessionDao();
    }

    @Bean
    public SessionManager sessionManager(){
        CustomSessionManager sessionManager = new CustomSessionManager();
        sessionManager.setSessionDAO(redisSessionDao());
        return sessionManager;
    }

    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        CustomRealm customRealm = customRealm();
        // 2.加密配置
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(1);
        customRealm.setCredentialsMatcher(matcher);

        // 设置Realm
        securityManager.setRealm(customRealm);
        //设置session管理器
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是会报错的，因为在初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     *
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/user/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/user/index");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/login");

        // 权限控制map.过滤器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        filterChainDefinitionMap.put("/user/login", "anon");
//        filterChainDefinitionMap.put("/user/*", "authc");
        filterChainDefinitionMap.put("/*", "anon");

        //静态资源
//        filterChainDefinitionMap.put("/css/**", "anon");
//        filterChainDefinitionMap.put("/images/**", "anon");
//        filterChainDefinitionMap.put("/json/**", "anon");
//        filterChainDefinitionMap.put("/layui/**", "anon");
//        filterChainDefinitionMap.put("/mods/**", "anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

}
