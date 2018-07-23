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
import com.java.sys.entity.SysRole;
import com.java.sys.entity.SysRolePerm;
import com.java.sys.entity.SysRoleUser;
import com.java.sys.service.SysMenuService;
import com.java.sys.service.SysPermService;
import com.java.sys.service.SysRolePermService;
import com.java.sys.service.SysRoleService;
import com.java.sys.service.SysRoleUserService;

@ApiIgnore
@Controller
@RequestMapping("/sys/sysRoleWebController")
public class SysRoleWebController extends BaseController{
	@Resource
	private SysRoleService sysRoleService;
	@Resource
	private SysPermService sysPermService;
	@Resource
	private SysMenuService sysMenuService;
	@Resource
	private SysRolePermService sysRolePermService;
	@Resource
	private SysRoleUserService sysRoleUserService;
	
	@ModelAttribute
	public SysRole get(@RequestParam(required=false) String id) {
		SysRole entity = null;
		if (Tool.isNotBlank(id)){
			entity = sysRoleService.get(id);
		}
		if (entity == null){
			entity = new SysRole();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:role:role:view")
	@RequestMapping("/list")
	public String list(SysRole sysRole,Model model,HttpServletRequest request,HttpServletResponse response){
		SysPage<SysRole> page = sysRoleService.findPage(sysRole,request);
		model.addAttribute("page", page);
		return "/WEB-INF/views/sys/sysRoleList.jsp";
	}
	
	@RequiresPermissions("sys:role:role:edit")
	@RequestMapping("/form")
	public String form(SysRole sysRole, Model model) {
		model.addAttribute("sysRole", sysRole);
		return "/WEB-INF/views/sys/sysRoleForm.jsp";
	}
	
	@RequiresPermissions("sys:role:role:edit")
	@RequestMapping("/save")
	public String save(SysRole sysRole, Model model, RedirectAttributes redirectAttributes) {
		sysRoleService.save(sysRole);
		CacheUtil.refreshRealm();
		addMessage("保存成功", SUCCESS, redirectAttributes);
		return "redirect:/sys/sysRoleWebController/list";
	}
	
	@RequiresPermissions("sys:role:role:delete")
	@RequestMapping("/delete")
	public String delete(SysRole sysRole, RedirectAttributes redirectAttributes) {
		// 删除关联
		if(Tool.isNotBlank(sysRole.getId())){
			SysRoleUser _ru = new SysRoleUser(sysRole.getId(), null);
			List<SysRoleUser> ruList = sysRoleUserService.findList(_ru);
			if(ruList != null && ruList.size()>0){
				addMessage("删除失败，某些用户属于该角色，请先删除用户或重置用户的角色", ERROR, redirectAttributes);
				return "redirect:/sys/sysRoleWebController/list";
			}
			sysRoleService.delete(sysRole);
			CacheUtil.refreshRealm();
			addMessage("删除成功", SUCCESS, redirectAttributes);
		}
		return "redirect:/sys/sysRoleWebController/list";
	}
	
	@RequiresPermissions("sys:role:role:edit")
	@RequestMapping("/distributeForm")
	public String distributeForm(SysRole sysRole, Model model) {
		//Subject subject = SecurityUtils.getSubject();
		//SysUser user = (SysUser)subject.getPrincipal();
		
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
		
		
		String rolePermStr = sysRolePermService.roleToPermStr(sysRole.getId());
		for(SysPerm perm : list){
			if(rolePermStr.contains(perm.getPermission())){
				perm.setChecked("checked");
			}
		}
		SysPage<SysPerm> page = new SysPage<SysPerm>();
		page.setList(list);
		model.addAttribute("page", page);
		model.addAttribute("sysRole", sysRole);
		
		return "/WEB-INF/views/sys/sysRoleDistributeForm.jsp";
	}
	
	@RequiresPermissions("sys:role:role:edit")
	@RequestMapping("/distributeSave")
	public String distributeSave(Model model, RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
		String permIds = request.getParameter("permIds");
		String roleId = request.getParameter("roleId");
		if(Tool.isNotBlank(permIds,roleId)){
			SysRole role = sysRoleService.get(roleId);
			if(role != null){
				SysRolePerm _rp = new SysRolePerm(role.getId(), null);
				List<SysRolePerm> rpList = sysRolePermService.findList(_rp);
				if(rpList != null && rpList.size()>0){
					for(SysRolePerm rp : rpList){
						sysRolePermService.delete(rp);
					}
				}
			}
			String[] permArr = permIds.split(",");
			if(permArr != null && permArr.length>0){
				for(String permId : permArr){
					if(Tool.isNotBlank(permId)){
						SysPerm perm = sysPermService.get(permId);
						if(perm != null){
							SysRolePerm rp = new SysRolePerm(roleId, permId);
							sysRolePermService.save(rp);
						}
					}
				}
			}
		}
		CacheUtil.refreshRealm();
		addMessage("保存成功", SUCCESS, redirectAttributes);
		return "redirect:/sys/sysRoleWebController/list";
	}
	
}
