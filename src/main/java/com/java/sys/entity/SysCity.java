package com.java.sys.entity;

import com.java.sys.common.basic.entity.BaseEntity;


public class SysCity extends BaseEntity{
	private static final long serialVersionUID = 1L;
	private String name;
	private String provinceId;
	private String cityNo;
	private String fullSpell;
	private String firstSpell;
	
	public SysCity() {
		super();
	}

	public SysCity(String name, String provinceId, String cityNo,
			String fullSpell, String firstSpell) {
		super();
		this.name = name;
		this.provinceId = provinceId;
		this.cityNo = cityNo;
		this.fullSpell = fullSpell;
		this.firstSpell = firstSpell;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityNo() {
		return cityNo;
	}

	public void setCityNo(String cityNo) {
		this.cityNo = cityNo;
	}

	public String getFullSpell() {
		return fullSpell;
	}

	public void setFullSpell(String fullSpell) {
		this.fullSpell = fullSpell;
	}

	public String getFirstSpell() {
		return firstSpell;
	}

	public void setFirstSpell(String firstSpell) {
		this.firstSpell = firstSpell;
	}
	
	
	
}
