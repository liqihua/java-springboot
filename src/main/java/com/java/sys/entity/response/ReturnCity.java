package com.java.sys.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("城市")
public class ReturnCity {
	@ApiModelProperty("城市id")
	public String id;
	@ApiModelProperty("城市名称")
	public String name;
	@ApiModelProperty("所属省id")
	public String provinceId;
	@ApiModelProperty("城市区号")
	public String cityNo;
	@ApiModelProperty("拼音-全拼")
	public String fullSpell;
	@ApiModelProperty("拼音-首字母")
	public String firstSpell;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
