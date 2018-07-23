package com.java.sys.entity;

import com.java.sys.common.basic.entity.BaseEntity;


public class SysRolePerm extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String roleId;
	private String permId;
	
	public SysRolePerm() {
		super();
	}
	public SysRolePerm(String roleId, String permId) {
		super();
		this.roleId = roleId;
		this.permId = permId;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getPermId() {
		return permId;
	}
	public void setPermId(String permId) {
		this.permId = permId;
	}
	
}
