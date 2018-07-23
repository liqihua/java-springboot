package com.java.sys.entity;

import com.java.sys.common.basic.entity.BaseEntity;

public class SysRole extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String name;	//角色名称
	
	public SysRole() {
		super();
	}
	
    public String getName() {
    	return name;
    }
    public void setName(String name) {
    	this.name = name;
    }
	
}