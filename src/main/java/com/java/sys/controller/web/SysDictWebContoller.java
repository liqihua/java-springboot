package com.java.sys.controller.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import springfox.documentation.annotations.ApiIgnore;

import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.page.SysPage;
import com.java.sys.common.utils.Tool;
import com.java.sys.entity.SysDict;
import com.java.sys.service.SysDictService;

@ApiIgnore
@Controller
@RequestMapping("/sys/dictWebController")
public class SysDictWebContoller extends BaseController{
	@Resource
	private SysDictService dictService;
	
	@ModelAttribute
	public SysDict get(@RequestParam(required=false) String id) {
		SysDict entity = null;
		if (Tool.isNotBlank(id)){
			entity = dictService.get(id);
		}
		if (entity == null){
			entity = new SysDict();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:dict:view")
	@RequestMapping("/list")
	public String list(SysDict sysDict,Model model,HttpServletRequest request,HttpServletResponse response){
		sysDict.setOrderBy(" a.type ASC,a.rank*1 ASC ");
		SysPage<SysDict> page = dictService.findPage(sysDict,request);
		model.addAttribute("page", page);
		return "/WEB-INF/views/sys/sysDictList.jsp";
	}
	
	@RequiresPermissions("sys:dict:dict:edit")
	@RequestMapping("/form")
	public String form(SysDict sysDict, Model model) {
		model.addAttribute("sysDict", sysDict);
		return "/WEB-INF/views/sys/sysDictForm.jsp";
	}
	
	@RequiresPermissions("sys:dict:dict:edit")
	@RequestMapping("/save")
	public String save(SysDict sysDict, Model model, RedirectAttributes redirectAttributes) {
		if(Tool.isBlank(sysDict.getId())){
			SysDict _d = new SysDict(sysDict.getType(), null, sysDict.getValue(), null, null);
			List<SysDict> list = dictService.findList(_d);
			if(list != null && list.size()>0){
				addMessage("字典已存在", ERROR, redirectAttributes);
			}else{
				dictService.save(sysDict);
				addMessage("保存字典成功", SUCCESS, redirectAttributes);
			}
		}else{
			dictService.save(sysDict);
			addMessage("保存字典成功", SUCCESS, redirectAttributes);
		}
		return "redirect:/sys/dictWebController/list";
	}
	
	@RequiresPermissions("sys:dict:dict:delete")
	@RequestMapping("/delete")
	public String delete(SysDict sysDict, RedirectAttributes redirectAttributes) {
		dictService.delete(sysDict);
		addMessage("删除字典成功", SUCCESS, redirectAttributes);
		return "redirect:/sys/dictWebController/list";
	}
	
}
