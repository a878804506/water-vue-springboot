package cn.enilu.flash.api.config;

import cn.enilu.flash.api.interceptor.JwtFilter;
import cn.enilu.flash.api.interceptor.NoHeaderTokenFilter;
import cn.enilu.flash.security.ApiRealm;
import cn.enilu.flash.security.SystemLogoutFilter;
import cn.enilu.flash.utils.Maps;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ：enilu
 * @date ：Created in 2019/7/30 23:08
 */
@Configuration
public class ShiroConfig {
    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(ApiRealm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自己的realm
        manager.setRealm(realm);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);

        // 添加自己的过滤器并且取名为jwt
        // 重要：设置自定义拦截器，当访问某些自定义url时，使用这个filter进行验证
        Map<String, Filter> filterMap =  Maps.newHashMap();
        filterMap.put("jwt", new JwtFilter());
        filterMap.put("noHeaderToken", new NoHeaderTokenFilter());
        filterMap.put("logout", new SystemLogoutFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setUnauthorizedUrl("/401");

        /*
         * 自定义url规则
         * http://shiro.apache.org/web.html#urls-
         */
        Map<String, String> filterRuleMap =  new LinkedHashMap<>();
        // anon：所有url都可以匿名访问
        // authc：需要认证才能进行访问
        // user：配置记住我或认证通过可以访问

        // 放开静态资源的过滤
        filterRuleMap.put("/css/**","anon");
        filterRuleMap.put("/js/**","anon");
        filterRuleMap.put("/img/**","anon");
        filterRuleMap.put("*.html","anon");
        filterRuleMap.put("*.css","anon");
        filterRuleMap.put("*.js","anon");
        filterRuleMap.put("*.map","anon");
        filterRuleMap.put("*.png","anon");
        filterRuleMap.put("*.gif","anon");
        filterRuleMap.put("*.woff2","anon");
        filterRuleMap.put("/swagger-*","anon");
        filterRuleMap.put("/v2/api-docs","anon");
        filterRuleMap.put("/configuration/security","anon");
        filterRuleMap.put("/configuration/ui","anon");

        // 放开登陆url的过滤
        filterRuleMap.put("/account/login","anon");
        filterRuleMap.put("/logout", "logout");
        filterRuleMap.put("/offcialsite","anon");
        // 访问401和404页面不通过我们的Filter
        filterRuleMap.put("/401", "anon");
        // 钉钉登录回调
        filterRuleMap.put("/account/dingdingCallback","anon");

        // 水务系统 excel下载---不带header token的url特殊处理
        filterRuleMap.put("/water/info/downloadExcel","noHeaderToken");
        // pdf解析后下载---不带header token的url特殊处理
        filterRuleMap.put("/pdf/management/download","noHeaderToken");

        filterRuleMap.put("/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
