package com.java.config.mvc.interceptor;

import com.java.common.constance.ApiConstance;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.common.utils.Tool;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * 防止XSS攻击
 */
public class XSSInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				String newStr = Tool.replaceXSS(valueStr);
				if(!valueStr.equals(newStr)) {
					Tool.info("--- XSS : ");
					Tool.info(valueStr);
					Tool.info(newStr);
					BaseResult result = new BaseResult(ApiConstance.BASE_FAIL_CODE, "含有XSS字符", null);
					JSONObject json = JSONObject.fromObject(result);
					response.setCharacterEncoding("UTF-8");
					response.setContentType("application/json");
					response.getWriter().print(json.toString());
					
					return false;
				}
			}
		}
		return true;
	}
	
}
