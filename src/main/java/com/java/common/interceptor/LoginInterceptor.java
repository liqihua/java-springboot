package com.java.common.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.common.utils.TokenUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.java.common.constance.ApiConstance;
import com.java.common.interceptor.annotation.LoginValidate;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.common.utils.Tool;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * 登录校验拦截器：
 * 凡是方法头部加了注解@LoginValidate的controller，执行前都会先执行下面的preHandle()方法
 * 该拦截器注册在spring-context.xml中，不需要可以去掉
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			LoginValidate loginValidate = ((HandlerMethod)handler).getMethodAnnotation(LoginValidate.class);
			if(loginValidate != null && loginValidate.validate() == true){
				/*boolean isLogin = false;
				String tokenWEB = request.getParameter("tokenWEB");
				String tokenAPP = request.getParameter("tokenAPP");
				String userId = request.getParameter("userId");
				if(Tool.isNotBlank(userId)){
					if(Tool.isNotBlank(tokenWEB)){
						isLogin = TokenUtil.isLoginWEB(userId,tokenWEB);
					}
					if(Tool.isNotBlank(tokenAPP)){
						isLogin = TokenUtil.isLoginAPP(userId,tokenAPP);
					}
				}*/
				/*
				 * 如果没有登录，返回false拦截请求
				 */
				/*if(!isLogin){
					BaseResult result = new BaseResult(ApiConstance.NO_LOGIN, ApiConstance.getMessage(ApiConstance.NO_LOGIN), null);
					JSONObject json = JSONObject.fromObject(result);
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json");
					response.getWriter().print(json.toString());
					return false;
				}*/
			}
		}
		return true;
	}

	
	
	
	
	
}
