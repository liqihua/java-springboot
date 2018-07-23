package com.java.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.dao.SysProvinceDao;
import com.java.sys.entity.SysProvince;
import com.java.sys.entity.response.ReturnProvince;
@Service
public class SysProvinceService extends BaseService<SysProvinceDao, SysProvince> {

	
	public ReturnProvince getReturnProvince(SysProvince sysProvince){
		ReturnProvince province = new ReturnProvince();
		try{
			if(sysProvince != null){
				BeanUtils.copyProperties(province,sysProvince);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return province;
	}
	
	
	public List<ReturnProvince> getReturnProvinceList(List<SysProvince> provinceList){
		List<ReturnProvince> list = new ArrayList<ReturnProvince>();
		if(provinceList != null && provinceList.size()>0){
			for(SysProvince province : provinceList){
				list.add(getReturnProvince(province));
			}
		}
		return list;
	}
	
}
