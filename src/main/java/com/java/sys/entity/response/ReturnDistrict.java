package com.java.sys.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("区县")
public class ReturnDistrict {
	@ApiModelProperty("区县id")
	public String id;
	@ApiModelProperty("区县名称")
	public String name;
	@ApiModelProperty("所属城市id")
	public String cityId;
	@ApiModelProperty("邮编")
	public String postCode;
	
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
