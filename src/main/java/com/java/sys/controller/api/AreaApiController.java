package com.java.sys.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.common.constance.ApiConstance;
import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.entity.SysCity;
import com.java.sys.entity.SysDistrict;
import com.java.sys.entity.SysProvince;
import com.java.sys.entity.response.ReturnCity;
import com.java.sys.entity.response.ReturnDistrict;
import com.java.sys.entity.response.ReturnProvince;
import com.java.sys.service.SysCityService;
import com.java.sys.service.SysDistrictService;
import com.java.sys.service.SysProvinceService;

@Api(value="apiAreaController",description = "省、市、区相关")
@Controller
@RequestMapping("/api/sysAreaApiController")
public class AreaApiController extends BaseController{
	@Resource
	private SysProvinceService provinceService;
	@Resource
	private SysCityService cityService;
	@Resource
	private SysDistrictService districtService;
	
	/*
	 * 查询所有省份
	 */
	@ApiOperation(value = "查询所有省份", notes = "查询所有省份")
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = ReturnProvince.class)})
	@RequestMapping(value = "/findProvinceList", method = RequestMethod.POST)
	public ResponseEntity<BaseResult> findProvinceList(HttpServletRequest request){
		List<SysProvince> provinceList = provinceService.findList(null);
		List<ReturnProvince> list = provinceService.getReturnProvinceList(provinceList);
		return buildSuccessInfo(list);
	}
	
	
	/*
	 * 查询所有城市或者根据省id查询城市
	 */
	@ApiOperation(value = "查询所有城市或者根据省id查询城市", notes = "查询所有城市或者根据省id查询城市")
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = ReturnCity.class)})
	@RequestMapping(value = "/findCityList", method = RequestMethod.POST)
	public ResponseEntity<BaseResult> findCityList(HttpServletRequest request,
			@ApiParam("省份id，可选") @RequestParam(value="provinceId",required=false) String provinceId){
		List<SysCity> cityList = cityService.findList(new SysCity(null, provinceId, null, null, null));
		List<ReturnCity> list = cityService.getReturnCityList(cityList);
		return buildSuccessInfo(list);
	}
	
	/*
	 * 查询所有地区或者根据城市id查询地区
	 */
	@ApiOperation(value = "查询所有地区或者根据城市id查询地区", notes = "查询所有地区或者根据城市id查询地区")
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = ReturnDistrict.class)})
	@RequestMapping(value = "/findDistrictList", method = RequestMethod.POST)
	public ResponseEntity<BaseResult> findDistrictList(HttpServletRequest request,
			@ApiParam("城市id，可选") @RequestParam(value="cityId",required=false) String cityId){
		List<SysDistrict> districtList = districtService.findList(new SysDistrict(null, cityId, null));
		List<ReturnDistrict> list = districtService.getReturnDistrictList(districtList);
		return buildSuccessInfo(list);
	}
	
}
