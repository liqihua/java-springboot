package com.java.sys.controller.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.sys.common.cache.CacheUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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
import com.java.sys.entity.SysRole;
import com.java.sys.entity.SysRoleUser;
import com.java.sys.entity.SysUser;
import com.java.sys.service.SysRoleService;
import com.java.sys.service.SysRoleUserService;
import com.java.sys.service.SysUserService;

@ApiIgnore
@Controller
@RequestMapping("/sys/userWebController")
public class SysUserWebController extends BaseController{
	@Resource
	private SysUserService userService;
	@Resource
	private SysRoleUserService sysRoleUserService;
	@Resource
	private SysRoleService sysRoleService;
	
	@ModelAttribute
	public SysUser get(@RequestParam(required=false) String id) {
		SysUser entity = null;
		if (Tool.isNotBlank(id)){
			entity = userService.get(id);
		}
		if (entity == null){
			entity = new SysUser();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:user:user:view")
	@RequestMapping("/list")
	public String list(SysUser sysUser,Model model,HttpServletRequest request,HttpServletResponse response){
		SysPage<SysUser> page = userService.findPage(sysUser,request);
		model.addAttribute("page", page);
		return "/WEB-INF/views/sys/sysUserList.jsp";
	}
	
	@RequiresPermissions("sys:user:user:edit")
	@RequestMapping("/form")
	public String form(SysUser sysUser, Model model) {
		Map<String,String> roleMap = new HashMap<String,String>();
		List<SysRole> roleList = sysRoleService.findList(null);
		if(roleList != null && roleList.size()>0){
			for(SysRole role : roleList){
				roleMap.put(role.getId(), role.getName());
			}
		}
		if(sysUser != null && Tool.isNotBlank(sysUser.getId())){
			SysRoleUser _ru = new SysRoleUser(null, sysUser.getId());
			List<SysRoleUser> ruList = sysRoleUserService.findList(_ru);
			if(ruList != null && ruList.size()>0){
				sysUser.setRoleId(ruList.get(0).getRoleId());
			}
		}
		model.addAttribute("roleMap", roleMap);
		model.addAttribute("sysUser", sysUser);
		return "/WEB-INF/views/sys/sysUserForm.jsp";
	}
	
	@RequiresPermissions("sys:user:user:edit")
	@RequestMapping("/save")
	public String save(SysUser sysUser, Model model, RedirectAttributes redirectAttributes) {
		if(Tool.isNotBlank(sysUser.getId())){
			SysRoleUser _ru = new SysRoleUser(null, sysUser.getId());
			List<SysRoleUser> ruList = sysRoleUserService.findList(_ru);
			sysRoleUserService.delete(ruList);
		}
		userService.save(sysUser);
		SysRoleUser ru = new SysRoleUser(sysUser.getRoleId(), sysUser.getId());
		sysRoleUserService.save(ru);
		CacheUtil.refreshRealm();
		addMessage("保存用户成功", SUCCESS, redirectAttributes);
		return "redirect:/sys/userWebController/list";
	}
	
	@RequiresPermissions("sys:user:user:delete")
	@RequestMapping("/delete")
	public String delete(SysUser sysUser, RedirectAttributes redirectAttributes) {
		if("1".equals(sysUser.getId())){
			addMessage("不允许删除超级管理员", ERROR, redirectAttributes);
			return "redirect:/sys/userWebController/list";
		}
		// 删除关联
		SysRoleUser _ru = new SysRoleUser(null, sysUser.getId());
		List<SysRoleUser> ruList = sysRoleUserService.findList(_ru);
		if(ruList != null && ruList.size()>0){
			for(SysRoleUser ru : ruList){
				sysRoleUserService.delete(ru);
			}
		}
		userService.delete(sysUser);
		CacheUtil.refreshRealm();
		addMessage("删除用户成功", SUCCESS, redirectAttributes);
		return "redirect:/sys/userWebController/list";
	}
	
	@RequestMapping("/goEditPassword")
	public String goEditPassword(HttpServletRequest request) {
		return "/WEB-INF/views/sys/sysUserEditPwdForm.jsp";
	}
	
	@RequestMapping("/editPassword")
	public String editPassword(HttpServletRequest request,RedirectAttributes redirectAttributes) {
		String password = request.getParameter("password");
		if(Tool.isNotBlank(password)){
			Subject subject = SecurityUtils.getSubject();
			SysUser user = (SysUser)subject.getPrincipal();
			if(user != null){
				user.setPassword(password);
				userService.save(user);
				addMessage("密码修改成功", SUCCESS, redirectAttributes);
			}
		}
		return "redirect:/sys/userWebController/goEditPassword";
	}
}
