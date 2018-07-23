package com.java.sys.common.constance;

import java.util.Map;
import java.util.TreeMap;


public class SysApiConstance {
	public static final Map<Integer,String> map = new TreeMap<Integer,String>();
	
	/**
	 * 签名过期时间
	 */
	public final static int TIMESTAMP_PASS_TIME = 20;
	/**
	 * 成功
	 */
	public final static int BASE_SUCCESS_CODE = 0;
	/**
	 * 失败
	 */
	public final static int BASE_FAIL_CODE = -1;
	/**
	 * 权限不足
	 */
	public final static int NO_AUTHORIZATION = -2;
	
	
	
	public final static int PARAM_ERROR = 1001;
	public final static int USER_EXIST = 1002;
	public final static int USER_NOT_EXIST = 1003;
	public final static int PASSWORD_ERROR = 1004;
	public final static int NO_LOGIN = 1005;
	public final static int CITY_NOT_EXIST = 1006;
	public final static int PROVINCE_NOT_EXIST = 1007;
	public final static int DISTRICT_NOT_EXIST = 1008;
	public final static int SIGN_OVERDUE = 1009;
	public final static int VCODE_ERROR = 1010;
	public final static int PHONE_BINDED = 1011;
	public final static int ACCOUNT_ERROR = 1012;
	public final static int USER_LOCKED = 1013;
	public final static int ADDRESS_NOT_EXIST = 1014;
	public final static int ORDER_NOT_EXIST = 1015;
	public final static int COLLECT_NOT_EXIST = 1016;
	public final static int COLLECT_EXIST = 1017;
	public final static int COMMENT_EXIST = 1018;
	public final static int COMMENT_NOT_EXIST = 1019;
	public final static int PARAM_IS_NULL = 1020;
	public final static int PARAM_DATE_ERROR = 1021;
	public final static int PARAM_PHONE_ERROR = 1022;
	public final static int PARAM_NUMBER_ERROR = 1023;
	public final static int PARAM_DOUBLE_ERROR = 1024;
	public final static int PARAM_TYPE_ERROR = 1025;
	public final static int NO_AUTHENTICATION = 1026;
	public final static int PRAISE_EXIST = 1027;
	public final static int PRAISE_NOT_EXIST = 1028;
	public final static int LOG_NOT_EXIST = 1029;
	public final static int REQUEST_BUSY = 1030;

	static{
		map.put(BASE_SUCCESS_CODE, "success");
		map.put(BASE_FAIL_CODE, "fail");
		map.put(NO_AUTHORIZATION, "权限不足");
		map.put(PARAM_ERROR, "参数有误");
		map.put(USER_EXIST, "用户已存在");
		map.put(USER_NOT_EXIST, "用户不存在");
		map.put(PASSWORD_ERROR, "密码有误");
		map.put(NO_LOGIN, "未登录");
		map.put(CITY_NOT_EXIST, "省份不存在");
		map.put(PROVINCE_NOT_EXIST, "城市不存在");
		map.put(DISTRICT_NOT_EXIST, "区不存在");
		map.put(SIGN_OVERDUE, "签名参数已过期");
		map.put(VCODE_ERROR, "验证码有误");
		map.put(PHONE_BINDED, "该手机号码已被绑定");
		map.put(ACCOUNT_ERROR, "用户名或密码错误");
		map.put(USER_LOCKED, "用户已被冻结");
		map.put(ADDRESS_NOT_EXIST, "地址不存在");
		map.put(ORDER_NOT_EXIST, "订单不存在");
		map.put(COLLECT_NOT_EXIST, "收藏记录不存在");
		map.put(COLLECT_EXIST, "你已收藏过");
		map.put(COMMENT_EXIST, "你已评价过");
		map.put(COMMENT_NOT_EXIST, "评价不存在");
		map.put(PARAM_IS_NULL, "参数不能为空");
		map.put(PARAM_DATE_ERROR, "日期参数错误");
		map.put(PARAM_PHONE_ERROR, "手机号码参数有误");
		map.put(PARAM_NUMBER_ERROR, "数字类参数有误");
		map.put(PARAM_DOUBLE_ERROR, "小数类参数有误");
		map.put(PARAM_TYPE_ERROR, "参数类型不正确");
		map.put(NO_AUTHENTICATION, "没有通过审核");
		map.put(PRAISE_EXIST, "你已点赞过");
		map.put(PRAISE_NOT_EXIST, "点赞记录不存在");
		map.put(LOG_NOT_EXIST, "记录不存在");
		map.put(REQUEST_BUSY, "请求过于频繁，请稍后再试");
	}
	
	
	
}
