package com.java.sys.entity;

import com.java.sys.common.basic.entity.BaseEntity;


public class SysRoleUser extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String roleId;
	private String userId;
	
	public SysRoleUser() {
		super();
	}
	
	public SysRoleUser(String roleId, String userId) {
		super();
		this.roleId = roleId;
		this.userId = userId;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
