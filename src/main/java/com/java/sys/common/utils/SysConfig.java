package com.java.sys.common.utils;

import java.io.InputStream;
import java.util.Properties;

public class SysConfig {
	public static String fileName = null;
	
	/**
	 * 获取配置文件config.properties的值
	 * @param key
	 * @return
	 */
	public static String getConfig(String key) {
		String file = "config.properties";
		if(Tool.isNotBlank(fileName)){
			file = fileName;
		}
		Properties p = new Properties();
		InputStream is = SysConfig.class.getClassLoader().getResourceAsStream(file);
		try{
			p.load(is);
			is.close();
			if(p.getProperty(key)==null){
				return "";
			}
			return p.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
