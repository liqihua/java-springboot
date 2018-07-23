package com.java.sys.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.java.sys.common.springholder.SpringContextHolder;
import com.java.sys.entity.SysCity;
import com.java.sys.entity.SysDistrict;
import com.java.sys.entity.SysProvince;
import com.java.sys.service.SysCityService;
import com.java.sys.service.SysDistrictService;
import com.java.sys.service.SysProvinceService;

public class SysAreaUtil {
	private static SysProvinceService provinceService = SpringContextHolder.getBean(SysProvinceService.class);
	private static SysCityService cityService = SpringContextHolder.getBean(SysCityService.class);
	private static SysDistrictService districtService = SpringContextHolder.getBean(SysDistrictService.class);
	
	/**
	 * 获取所有省份
	 * @return Map
	 */
	public static Map<String,String> getAllProvince(){
		Map<String,String> map = new HashMap<String, String>();
		List<SysProvince> list = provinceService.findList(null);
		if(list != null && list.size()>0){
			for(SysProvince p : list){
				map.put(p.getId(), p.getName());
			}
		}
		return map;
	}
	
	/**
	 * 方法名：getProvinceName
	 * 详述：根据id获取省份名称
	 * @param id
	 * @return
	 */
	public static String getProvinceName(String id){
		if(Tool.isNotBlank(id)){
			SysProvince province = provinceService.get(id);
			if(province != null){
				return province.getName();
			}
			return id;
		}
		return null;
	}
	
	/**
	 * 方法名：getCityName
	 * 详述：根据id获取城市名称
	 * @param id
	 * @return
	 */
	public static String getCityName(String id){
		if(Tool.isNotBlank(id)){
			SysCity city = cityService.get(id);
			if(city != null){
				return city.getName();
			}
			return id;
		}
		return null;
	}
	
	/**
	 * 方法名：getDistrictName
	 * 详述：根据id获取地区名称
	 * @param id
	 * @return
	 */
	public static String getDistrictName(String id){
		if(Tool.isNotBlank(id)){
			SysDistrict district = districtService.get(id);
			if(district != null){
				return district.getName();
			}
			return id;
		}
		return null;
	}
	
	/**
	 * 方法名：getCityMap
	 * 详述：根据id获取城市名称
	 * @param cityId
	 * @return
	 */
	public static Map<String,Object> getCityMap(String cityId){
		if(Tool.isNotBlank(cityId)){
			SysCity city = cityService.get(cityId);
			if(city != null){
				SysCity _c = new SysCity();
				_c.setProvinceId(city.getProvinceId());
				List<SysCity> cList = cityService.findList(_c);
				if(cList != null && cList.size()>0){
					Map<String,Object> map = new HashMap<String,Object>();
					for(SysCity c : cList){
						map.put(c.getId(), c.getName());
					}
					return map;
				}
			}
		}
		return null;
	}
	
	/**
	 * 方法名：getDistrictMap
	 * 详述：根据id获取地区名称
	 * @param districtId
	 * @return
	 */
	public static Map<String,Object> getDistrictMap(String districtId){
		if(Tool.isNotBlank(districtId)){
			SysDistrict district = districtService.get(districtId);
			if(district != null){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put(districtId, district.getName());
				return map;
			}
		}
		return null;
	}
}
