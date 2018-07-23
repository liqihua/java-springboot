package com.java.sys.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.java.sys.common.springholder.SpringContextHolder;
import com.java.sys.entity.SysDict;
import com.java.sys.service.SysDictService;

public class SysDictUtil {
	private static SysDictService dictService = SpringContextHolder.getBean(SysDictService.class);
	
	/*
	 * 根据键值和类型名称查询对应的Label
	 */
	public static String getDictLabel(String value,String type,String defaultValue){
		if(dictService == null){
			return "";
		}
		if(Tool.isBlank(defaultValue)){
			defaultValue = "";
		}
		if(Tool.isBlank(value)){
			return defaultValue;
		}
		SysDict _d = new SysDict(type, null, value, null, null);
		List<SysDict> list = dictService.findList(_d);
		if(list != null && list.size()>0){
			return list.get(0).getLabel();
		}
		return defaultValue;
	}
	
	/*
	 * 根据type查询字典列表，返回Map
	 */
	public static Map<String,String> getDictList(String type){
		Map<String,String> map = new HashMap<String, String>();
		SysDict _d = new SysDict(type, null, null, null, null);
		List<SysDict> list = dictService.findList(_d);
		if(list != null && list.size()>0){
			for(SysDict d : list){
				map.put(d.getValue(), d.getLabel());
			}
		}
		return map;
	}
	
	
}	
