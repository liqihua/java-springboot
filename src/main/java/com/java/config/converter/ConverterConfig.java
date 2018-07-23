package com.java.config.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liqihua
 * @since 2018/5/18
 */
@Configuration
public class ConverterConfig {


    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        //设置日期格式
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(smt);
        converter.setObjectMapper(objectMapper);

        //设置支持的类型
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        list.add(MediaType.APPLICATION_JSON);
        list.add(MediaType.APPLICATION_ATOM_XML);
        list.add(MediaType.APPLICATION_FORM_URLENCODED);
        list.add(MediaType.APPLICATION_OCTET_STREAM);
        list.add(MediaType.APPLICATION_PDF);
        list.add(MediaType.APPLICATION_RSS_XML);
        list.add(MediaType.APPLICATION_XHTML_XML);
        list.add(MediaType.APPLICATION_XML);
        list.add(MediaType.IMAGE_GIF);
        list.add(MediaType.IMAGE_JPEG);
        list.add(MediaType.IMAGE_PNG);
        list.add(MediaType.TEXT_EVENT_STREAM);
        list.add(MediaType.TEXT_HTML);
        list.add(MediaType.TEXT_MARKDOWN);
        list.add(MediaType.TEXT_PLAIN);
        list.add(MediaType.TEXT_XML);
        converter.setSupportedMediaTypes(list);

        return converter;
    }


}
