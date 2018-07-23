package com.java.sys.controller.ajax;

import io.swagger.annotations.ApiParam;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import springfox.documentation.annotations.ApiIgnore;

import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.entity.SysCity;
import com.java.sys.entity.SysDistrict;
import com.java.sys.entity.SysProvince;
import com.java.sys.service.SysCityService;
import com.java.sys.service.SysDistrictService;
import com.java.sys.service.SysProvinceService;

@ApiIgnore
@Controller
@RequestMapping("/ajax/sysAreaAjaxController")
public class AreaAjaxController extends BaseController{
	@Resource
	private SysProvinceService provinceService;
	@Resource
	private SysCityService cityService;
	@Resource
	private SysDistrictService districtService;
	
	
	/*
	 * 查询所有省份
	 */
	@RequestMapping(value = "/findProvinceList", method = RequestMethod.POST)
	public ResponseEntity<BaseResult> findProvinceList(HttpServletRequest request){
		List<SysProvince> list = provinceService.findList(null);
		return buildSuccessInfo(list);
	}
	
	
	/*
	 * 查询所有城市或者根据省id查询城市
	 */
	@RequestMapping(value = "/findCityList", method = RequestMethod.POST)
	public ResponseEntity<BaseResult> findCityList(HttpServletRequest request,
			@ApiParam("省份id，可选") @RequestParam(value="provinceId",required=false) String provinceId){
		List<SysCity> list = cityService.findList(new SysCity(null, provinceId, null, null, null));
		return buildSuccessInfo(list);
	}
	
	
	
	/*
	 * 查询所有地区或者根据城市id查询地区
	 */
	@RequestMapping(value = "/findDistrictList", method = RequestMethod.POST)
	public ResponseEntity<BaseResult> findDistrictList(HttpServletRequest request,
			@ApiParam("城市id，可选") @RequestParam(value="cityId",required=false) String cityId){
		List<SysDistrict> list = districtService.findList(new SysDistrict(null, cityId, null));
		return buildSuccessInfo(list);
	}
	
	
	
	
	
	
	
}
