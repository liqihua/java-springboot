package com.java.sys.controller.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@SpringBootApplication
@RequestMapping("/sys")
public class SysWebController {
	
	@RequestMapping("/")
	public String goLogin(){
		return "redirect:/sys/indexWebController/login";
	}
	
	
}
