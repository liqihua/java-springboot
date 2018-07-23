package com.java.sys.entity;


import java.util.Date;

import com.java.sys.common.basic.entity.BaseEntity;

public class SysDict extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String type;	//类型名称
	private String label;	//标签
	private String value;	//数据值
	private String description;	//描述
	private String rank;	//排序
	
	//查询条件
	public String keysType;
	
	public SysDict() {
		super();
	}
	
    public SysDict(String type, String label, String value, String description,
			String rank) {
		super();
		this.type = type;
		this.label = label;
		this.value = value;
		this.description = description;
		this.rank = rank;
	}

	public String getType() {
    	return type;
    }
    public void setType(String type) {
    	this.type = type;
    }
    public String getLabel() {
    	return label;
    }
    public void setLabel(String label) {
    	this.label = label;
    }
    public String getValue() {
    	return value;
    }
    public void setValue(String value) {
    	this.value = value;
    }
    public String getDescription() {
    	return description;
    }
    public void setDescription(String description) {
    	this.description = description;
    }
    public String getRank() {
    	return rank;
    }
    public void setRank(String rank) {
    	this.rank = rank;
    }

	public String getKeysType() {
		return keysType;
	}

	public void setKeysType(String keysType) {
		this.keysType = keysType;
	}
	
}