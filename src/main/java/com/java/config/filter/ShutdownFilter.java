package com.java.config.filter;

import com.java.common.constance.ApiConstance;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.common.utils.SysConfig;
import com.java.sys.common.utils.Tool;
import net.sf.json.JSONObject;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
//@ServletComponentScan
//@WebFilter(urlPatterns = "/shutdown",filterName = "shutdownFilter")
public class ShutdownFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		String uri = request.getRequestURI().toString();
		boolean ok = true;
		if(uri.contains("/shutdown")) {
			Tool.info("--- /shutdown ...");
			ok = false;
			String sysName = SysConfig.getConfig("endpoints.shutdown.name");
			String sysPassword = SysConfig.getConfig("endpoints.shutdown.password");
			if(Tool.isNotBlank(sysName,sysPassword)) {
				String name = request.getParameter("name");
				String password = request.getParameter("password");
				if(Tool.isNotBlank(name,password)) {
					if(name.equals(sysName) && password.equals(sysPassword)) {
						ok = true;
					}
				}
			}
		}
		if(ok) {
			chain.doFilter(request, response);
		}else {
			BaseResult result = new BaseResult(ApiConstance.BASE_FAIL_CODE, "account error", null);
			JSONObject json = JSONObject.fromObject(result);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json");
			response.getWriter().print(json.toString());
		}
	}

	@Override
	public void destroy() {
	}
	
}
