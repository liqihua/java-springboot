package com.java.config.jdbc;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/*
 * 扫描dao
 * 这个配置一定要注意@AutoConfigureAfter(MyBatisConfig.class)，
 * 必须有这个配置，否则会有异常。原因就是这个类执行的比较早，由于sqlSessionFactory还不存在，后续执行出错。
 */
@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MyBatisMapperScannerConfig {
	
	@Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.java.dao,com.java.sys.dao");
        return mapperScannerConfigurer;
    }
}
