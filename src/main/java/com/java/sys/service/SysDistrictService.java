package com.java.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.java.sys.common.basic.service.BaseService;
import com.java.sys.dao.SysDistrictDao;
import com.java.sys.entity.SysDistrict;
import com.java.sys.entity.response.ReturnDistrict;

@Service
public class SysDistrictService extends BaseService<SysDistrictDao,SysDistrict>{

	
	public ReturnDistrict getReturnDistrict(SysDistrict sysDistrict){
		ReturnDistrict district = new ReturnDistrict();
		try{
			if(district != null){
				BeanUtils.copyProperties(district, sysDistrict);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return district;
	}
	
	
	public List<ReturnDistrict> getReturnDistrictList(List<SysDistrict> districtList){
		List<ReturnDistrict> list = new ArrayList<ReturnDistrict>();
		if(districtList != null && districtList.size()>0){
			for(SysDistrict district : districtList){
				list.add(getReturnDistrict(district));
			}
		}
		return list;
	}
	
}
