package com.java.sys.controller.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.sys.common.cache.CacheUtil;
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
import com.java.sys.entity.SysMenu;
import com.java.sys.entity.SysPerm;
import com.java.sys.entity.SysRolePerm;
import com.java.sys.service.SysMenuService;
import com.java.sys.service.SysPermService;
import com.java.sys.service.SysRolePermService;

@ApiIgnore
@Controller
@RequestMapping("/sys/sysPermWebController")
public class SysPermWebController extends BaseController{
	@Resource
	private SysPermService sysPermService;
	@Resource
	private SysMenuService sysMenuService;
	@Resource
	private SysRolePermService sysRolePermService;
	
	@ModelAttribute
	public SysPerm get(@RequestParam(required=false) String id) {
		SysPerm entity = null;
		if (Tool.isNotBlank(id)){
			entity = sysPermService.get(id);
		}
		if (entity == null){
			entity = new SysPerm();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:perm:perm:view")
	@RequestMapping("/list")
	public String list(SysPerm sysPerm,Model model,HttpServletRequest request,HttpServletResponse response){
		List<SysPerm> list = new ArrayList<SysPerm>();
		List<SysMenu> menuList = sysMenuService.findListOrderByLevelMerge();
		if(menuList != null && menuList.size()>0){
			for(SysMenu menu :menuList){
				SysPerm _p = new SysPerm(menu.getId(), null, null, null);
				List<SysPerm> permList = sysPermService.findList(_p);
				if(permList != null && permList.size()>0){
					for(SysPerm perm : permList){
						perm.setSysMenu(menu);
					}
					list.addAll(permList);
				}
			}
		}
		SysPage<SysPerm> page = new SysPage<SysPerm>();
		page.setList(list);
		model.addAttribute("page", page);
		return "/WEB-INF/views/sys/sysPermList.jsp";
	}
	
	@RequiresPermissions("sys:perm:perm:edit")
	@RequestMapping("/form")
	public String form(SysPerm sysPerm, Model model) {
		model.addAttribute("sysPerm", sysPerm);
		return "/WEB-INF/views/sys/sysPermForm.jsp";
	}
	
	@RequiresPermissions("sys:perm:perm:edit")
	@RequestMapping("/save")
	public String save(SysPerm sysPerm, Model model, RedirectAttributes redirectAttributes) {
		if(Tool.isBlank(sysPerm.getId()) && Tool.isNotBlank(sysPerm.getPermission())){
			SysPerm _p = new SysPerm(null, null, sysPerm.getPermission(), null);
			List<SysPerm> list = sysPermService.findList(_p);
			if(list != null && list.size()>0){
				addMessage("权限符号"+sysPerm.getPermission()+"已存在", ERROR, redirectAttributes);
				return "redirect:/sys/sysPermWebController/list";
			}
		}
		sysPermService.save(sysPerm);
		CacheUtil.refreshRealm();
		addMessage("保存成功", SUCCESS, redirectAttributes);
		return "redirect:/sys/sysPermWebController/list";
	}
	
	@RequiresPermissions("sys:perm:perm:delete")
	@RequestMapping("/delete")
	public String delete(SysPerm sysPerm, RedirectAttributes redirectAttributes) {
		// 删除关联
		SysRolePerm _rp = new SysRolePerm(null, sysPerm.getId());
		List<SysRolePerm> rpList = sysRolePermService.findList(_rp);
		if(rpList != null && rpList.size()>0){
			addMessage("删除失败，该权限被分配到某些角色中，请先取消角色相应的权限再删除", ERROR, redirectAttributes);
			return "redirect:/sys/sysPermWebController/list";
		}
		sysPermService.delete(sysPerm);
		CacheUtil.refreshRealm();
		addMessage("删除成功", SUCCESS, redirectAttributes);
		return "redirect:/sys/sysPermWebController/list";
	}
}
