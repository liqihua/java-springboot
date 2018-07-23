package com.java.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.dao.SysCityDao;
import com.java.sys.entity.SysCity;
import com.java.sys.entity.response.ReturnCity;

@Service
public class SysCityService extends BaseService<SysCityDao, SysCity> {

	
	public ReturnCity getReturnCity(SysCity sysCity){
		ReturnCity city = new ReturnCity();
		try{
			if(sysCity != null){
				BeanUtils.copyProperties(city, sysCity);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return city;
	}
	
	
	public List<ReturnCity> getReturnCityList(List<SysCity> cityList){
		List<ReturnCity> list = new ArrayList<ReturnCity>();
		if(cityList != null && cityList.size()>0){
			for(SysCity city : cityList){
				list.add(getReturnCity(city));
			}
		}
		return list;
	}
	
}
