package com.java.sys.controller.web;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springfox.documentation.annotations.ApiIgnore;

import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.utils.Tool;
import com.java.sys.service.SysColumnService;
import com.java.sys.service.SysGeneratorService;

@ApiIgnore
@Controller
@RequestMapping("/sys/generatorWebController")
public class SysGeneratorWebController extends BaseController{
	@Resource
	private SysGeneratorService genService;
	@Resource
	private SysColumnService columnService;
	
	@RequiresPermissions("sys:gen:gen:view")
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request,HttpServletResponse response){
		List<String> list = columnService.findTableList();
		if(list != null && list.size()>0){
			Iterator<String> it = list.iterator();
			// 去掉系统表
			while(it.hasNext()){
				String tableName = it.next();
				if(tableName.contains("sys_")){
					it.remove();
				}
			}
		}
		model.addAttribute("list", list);
		return "/WEB-INF/views/sys/sysGenList.jsp";
	}
	
	@RequiresPermissions("sys:gen:gen:make")
	@RequestMapping("/make")
	public String make(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes) {
		String tableName = request.getParameter("tableName");
		if(Tool.isNotBlank(tableName)){
			String type = request.getParameter("type");
			if(Tool.isBlank(type)){
				genService.makeAll(tableName);
			}else if("mapper".equals(type)){
				genService.makeMapper(tableName);
			}else if("entity".equals(type)){
				genService.makeEntity(tableName);
			}else if("dao".equals(type)){
				genService.makeDao(tableName);
			}else if("return".equals(type)){
				genService.makeReturn(tableName);
			}else if("service".equals(type)){
				genService.makeService(tableName);
			}else if("controller".equals(type)){
				genService.makeWebController(tableName);
			}
			
			addMessage("代码生成成功", SUCCESS, redirectAttributes);
		}
		return "redirect:/sys/generatorWebController/list";
	}
	
	
	
}
