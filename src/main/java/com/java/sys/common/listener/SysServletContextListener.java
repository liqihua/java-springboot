package com.java.sys.common.listener;

import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;

import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.utils.Tool;

public class SysServletContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Tool.info("----- SysServletContextListener->contextInitialized.");
		ConvertUtils.register(new DateConverter(null), Date.class);
		ConvertUtils.register(new BigDecimalConverter(BigDecimal.ZERO), BigDecimal.class);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		Tool.info("----- SysServletContextListener->contextDestroyed.");
	}

}
