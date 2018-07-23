package com.java.sys.entity.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("省份")
public class ReturnProvince {
	@ApiModelProperty("省份id")
	public String id;
	@ApiModelProperty("省份名称")
	public String name;
	
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
}
