package com.java.sys.controller.web;

import com.java.common.constance.ApiConstance;
import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.common.utils.Tool;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("/publicWebController")
public class PublicWebController extends BaseController{

	@RequestMapping("/apiConstanceList")
	public String apiConstanceList(Model model){
		ApiConstance api = new ApiConstance();
		model.addAttribute("constanceMap", api.map);
		return "/WEB-INF/views/sys/apiConstanceList.jsp";
	}
	

	@RequestMapping("/apiSignForm")
	public String apiSignForm(Model model,HttpServletRequest request){
		model.addAttribute("ajaxUrl", "/publicWebController/doSign");
		return "/WEB-INF/views/sys/apiSignForm.jsp";
	}
	
	
	@RequestMapping("/doSign")
	public ResponseEntity<BaseResult> doSign(HttpServletRequest request,
			@RequestParam(value="param",required=false) String param){
		String signStr = "";
		if(Tool.isNotBlank(param)){
			param = param.replace(" ", "+");
			param = param.replace("e0d509dcab1b4835a795899e48c63684", "=");
			param = param.replace("b90a128f6ae54256bddb84dc998c29e0", "&");
			param = param.substring(0, param.length()-1);
			if(!param.contains("timeStamp")){
				param = param + "&timeStamp="+System.currentTimeMillis()/1000;
			}
			signStr = Tool.sysEncode(param);
		}else{
			param = "";
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("param", param);
		map.put("signStr", signStr);
		return buildSuccessInfo(map);
	}
	
	
}
