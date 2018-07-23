package com.java.sys.controller.web;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.page.SysPage;
import com.java.sys.common.utils.Tool;
import com.java.sys.entity.SysMenu;
import com.java.sys.service.SysMenuService;
import com.java.sys.service.SysPermService;

@ApiIgnore
@Controller
@RequestMapping("/sys/menuWebController")
public class SysMenuWebController extends BaseController{
	@Resource
	private SysMenuService menuService;
	@Resource
	private SysPermService permService;
	
	
	@ModelAttribute
	public SysMenu get(@RequestParam(required=false) String id) {
		SysMenu entity = null;
		if (Tool.isNotBlank(id)){
			entity = menuService.get(id);
		}
		if (entity == null){
			entity = new SysMenu();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:menu:menu:view")
	@RequestMapping("/list")
	public String list(SysMenu sysMenu,Model model,HttpServletRequest request,HttpServletResponse response){
		List<SysMenu> list = menuService.findListWeb();
		SysPage<SysMenu> page = new SysPage<SysMenu>();
		page.setList(list);
		model.addAttribute("page", page);
		return "/WEB-INF/views/sys/sysMenuList.jsp";
	}
	
	@RequiresPermissions("sys:menu:menu:edit")
	@RequestMapping("/form")
	public String form(SysMenu sysMenu, Model model) {
		Map<String,String> showMap = new HashMap<String,String>();
		showMap.put("0", "显示");
		showMap.put("1", "隐藏");
		model.addAttribute("showMap", showMap);
		model.addAttribute("sysMenu", sysMenu);
		return "/WEB-INF/views/sys/sysMenuForm.jsp";
	}
	
	@RequiresPermissions("sys:menu:menu:edit")
	@RequestMapping("/save")
	public String save(SysMenu sysMenu, Model model, RedirectAttributes redirectAttributes) {
		if(Tool.isBlank(sysMenu.getParentId())){
			sysMenu.setLevel("1");
		}else{
			SysMenu pMenu = menuService.get(sysMenu.getParentId());
			if(pMenu == null){
				sysMenu.setLevel("1");
			}else{
				if("3".equals(pMenu.getLevel())){
					pMenu = menuService.get(pMenu.getParentId());
					sysMenu.setLevel("3");
					sysMenu.setParentId(pMenu.getId());
				}
				if("2".equals(pMenu.getLevel())){
					sysMenu.setLevel("3");
				}
				if("1".equals(pMenu.getLevel())){
					sysMenu.setLevel("2");
				}
				sysMenu.setParentName(pMenu.getName());
			}
		}
		menuService.save(sysMenu);
		CacheUtil.refreshMenu();
		addMessage("保存菜单成功", SUCCESS, redirectAttributes);
		return "redirect:/sys/menuWebController/list";
	}
	
	@RequiresPermissions("sys:menu:menu:delete")
	@RequestMapping("/delete")
	public String delete(SysMenu sysMenu, RedirectAttributes redirectAttributes) {
		if(sysMenu != null && Tool.isNotBlank(sysMenu.getId())){
			if(!menuService.noPerm(sysMenu)){
				addMessage("删除菜单失败，某些权限指向该菜单，请先删除相应权限", ERROR, redirectAttributes);
				return "redirect:/sys/menuWebController/list";
			}
			if(!menuService.noChild(sysMenu)){
				addMessage("删除菜单失败，该菜单含有子菜单，请先删除子菜单", ERROR, redirectAttributes);
				return "redirect:/sys/menuWebController/list";
			}
			menuService.delete(sysMenu);
			CacheUtil.refreshMenu();
			addMessage("删除菜单成功", SUCCESS, redirectAttributes);
		}
		return "redirect:/sys/menuWebController/list";
	}
	
}
