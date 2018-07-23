package com.java.sys.entity;


import com.java.sys.common.basic.entity.BaseEntity;

public class SysPerm extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String menuId;	//
	private String name;	//权限名称
	private String permission;	//权限标识符
	
	private SysMenu sysMenu;
	private String checked;
	
	public SysPerm() {
		super();
	}
	
    public SysPerm(String menuId, String name, String permission,
			SysMenu sysMenu) {
		super();
		this.menuId = menuId;
		this.name = name;
		this.permission = permission;
		this.sysMenu = sysMenu;
	}

	public String getMenuId() {
    	return menuId;
    }
    public void setMenuId(String menuId) {
    	this.menuId = menuId;
    }
    public String getName() {
    	return name;
    }
    public void setName(String name) {
    	this.name = name;
    }
    public String getPermission() {
    	return permission;
    }
    public void setPermission(String permission) {
    	this.permission = permission;
    }

	public SysMenu getSysMenu() {
		return sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	
}