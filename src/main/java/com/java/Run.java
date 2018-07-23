package com.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages={"com.*"})//如果不加这个注解，只会扫描本类同级和下级往下的目录里面的controller
public class Run {
	
	public static void main(String[] args) {
		Properties properties = new Properties();
		InputStream in = Run.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpringApplication app = new SpringApplication(Run.class);
		app.setDefaultProperties(properties);
		app.run(args);
		
	}
	
	
}
