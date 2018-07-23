package com.java.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean shutdownFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new ShutdownFilter());
    	bean.addUrlPatterns("/shutdown");
        bean.setName("shutdownFilter");
        return bean;
	}
	
	

	
}
