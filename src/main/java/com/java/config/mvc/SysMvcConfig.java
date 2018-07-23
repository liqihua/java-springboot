package com.java.config.mvc;

import com.java.common.interceptor.DDOSInterceptor;
import com.java.common.interceptor.LoginInterceptor;
import com.java.config.mvc.interceptor.CorsInterceptor;
import com.java.config.mvc.interceptor.XSSInterceptor;
import com.java.sys.common.utils.SysConfig;
import com.java.sys.common.utils.Tool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * mvc配置
 */
@Configuration
public class SysMvcConfig extends WebMvcConfigurerAdapter {

    @Bean
    XSSInterceptor xssInterceptor() {
        return new XSSInterceptor();
    }

    @Bean
    LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    DDOSInterceptor ddosInterceptor() {
        return new DDOSInterceptor();
    }

    @Bean
    CorsInterceptor corsInterceptor() {
        return new CorsInterceptor();
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(corsInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(xssInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(ddosInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
        //super.addInterceptors(registry);
    }

    /**
     * 添加静态目录映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/","classpath:/resources/");

        String dirs = SysConfig.getConfig("mvc.static.dir");
        if(Tool.isNotBlank(dirs)){
            String[] arr = dirs.split(",");
            if(arr != null && arr.length > 0){
                String projectPath = Tool.getProjectPath();
                for(String dir : arr){
                    registry.addResourceHandler("/" + dir + "**").addResourceLocations("file:" + projectPath + dir);
                }
            }
        }
        //super.addResourceHandlers(registry);
    }
}
