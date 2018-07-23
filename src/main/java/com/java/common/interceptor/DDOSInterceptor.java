package com.java.common.interceptor;

import com.java.common.constance.ApiConstance;
import com.java.common.interceptor.annotation.DDOSValidate;
import com.java.common.interceptor.annotation.LoginValidate;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.utils.Tool;
import net.sf.json.JSONObject;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DDOS攻击拦截器：
 * 凡是方法头部加了注解@LoginValidate的controller，执行前都会先执行下面的preHandle()方法
 * 该拦截器注册在spring-context.xml中，不需要可以去掉
 */
public class DDOSInterceptor extends HandlerInterceptorAdapter {
	
	
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
			HandlerMethod handlerMethod = (HandlerMethod)handler;
			DDOSValidate ddosValidate = handlerMethod.getMethodAnnotation(DDOSValidate.class);
			if(ddosValidate != null && ddosValidate.validate() == true){
				String methodName = handlerMethod.getMethod().getName();//获取函数名
				int second = ddosValidate.second();
				if(second > 0){
					String ip = request.getRemoteAddr();
					String cacheName = ip+"-"+methodName;
					String cacheValue = (String) CacheUtil.get(cacheName);
					if(Tool.isNotBlank(cacheValue)){
						BaseResult result = new BaseResult(ApiConstance.REQUEST_BUSY, ApiConstance.getMessage(ApiConstance.REQUEST_BUSY), null);
						JSONObject json = JSONObject.fromObject(result);
						response.setCharacterEncoding("UTF-8");
						response.setContentType("application/json");
						response.getWriter().print(json.toString());
						return false;
					}
					CacheUtil.set(cacheName,cacheName,second);
				}
			}
		}
		return true;
	}

	
	
	
	
	
}
