package com.java.config.shiro;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
	
	//注册realm
	@Bean
	public SysRealm sysRealm(){
		return new SysRealm();
	}
	
	//注册shiro的DefaultWebSecurityManager为SecurityManager
	@Bean
    public DefaultWebSecurityManager securityManager(SysRealm sysRealm){
       DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
       securityManager.setRealm(sysRealm);
       return securityManager; 
    }
	
	
	//拦截配置
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		bean.setLoginUrl("/sys/indexWebController/login");
		bean.setUnauthorizedUrl("/WEB-INF/views/shiro/unauth.jsp");
		
		Map<String,String> filterMap = new LinkedHashMap<String,String>();
		filterMap.put("/sys/indexWebController/**", "anon");
		filterMap.put("/sys/**", "authc");
		filterMap.put("/druid/**", "authc");
		//filterMap.put("/logout", "logout");
		bean.setFilterChainDefinitionMap(filterMap);
		return bean;
	}
	
	
	//启用shiro注解
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
	
	
	//创建代理
	@Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
	
	//shiro回收机制
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
	
	
	
}
