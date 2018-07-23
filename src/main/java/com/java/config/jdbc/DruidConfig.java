package com.java.config.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.java.sys.common.utils.SysConfig;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
//@PropertySource("classpath:config.properties")
public class DruidConfig {
	
	/*@Value("${jdbc.driver}")
	private String driver;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;*/
	
	
	//注册druid的统计Servlet
	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean reg = new ServletRegistrationBean();
		reg.setServlet(new StatViewServlet());
		reg.addUrlMappings("/druid/*");
		return reg;
	}

	//注册druid过滤器
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new WebStatFilter());
		bean.addUrlPatterns("/*");
		bean.addInitParameter("exclusions", "*.html,*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		bean.addInitParameter("profileEnable", "true");
		bean.addInitParameter("principalCookieName", "USER_COOKIE");
		bean.addInitParameter("principalSessionName", "USER_SESSION");
		return bean;
	}

	//注册druid为项目的数据源
	@Bean(destroyMethod = "close", initMethod = "init")
	public DataSource druidDataSource(){ 
		DruidDataSource datasource = new DruidDataSource(); 
		datasource.setUrl(SysConfig.getConfig("jdbc.url"));
		datasource.setUsername(SysConfig.getConfig("jdbc.username"));
		datasource.setPassword(SysConfig.getConfig("jdbc.password"));
		datasource.setDriverClassName(SysConfig.getConfig("jdbc.driver"));
		try { 
			datasource.setFilters("stat"); 
		} catch (SQLException e) { 
			e.printStackTrace(); 
		} 
		return datasource; 
	}
	
	
	
	
}
