package com.java.sys.entity;

import com.java.sys.common.basic.entity.BaseEntity;


public class SysDistrict extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String name;
	private String cityId;
	private String postCode;
	
	public SysDistrict() {
		super();
	}
	
	public SysDistrict(String name, String cityId, String postCode) {
		super();
		this.name = name;
		this.cityId = cityId;
		this.postCode = postCode;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
}
