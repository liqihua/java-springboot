package com.java.sys.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.dao.SysUserDao;
import com.java.sys.entity.SysUser;
@Service
public class SysUserService extends BaseService<SysUserDao, SysUser>{

	/**
	 * 获取当前管理后台登录用户
	 * 方法名：getCurrentUser
	 * 详述：
	 * @return
	 */
	public SysUser getCurrentUser(){
		Subject subject = SecurityUtils.getSubject();
		SysUser sysUser = (SysUser)subject.getPrincipal();
		return sysUser;
	}
	
}
