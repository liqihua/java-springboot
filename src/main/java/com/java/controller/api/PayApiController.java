package com.java.controller.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.java.common.constance.ApiConstance;
import com.java.common.pay.alipay.conf.AlipayConfig;
import com.java.common.pay.alipay.sign.RSA;
import com.java.common.pay.alipay.utils.AlipayNotify;
import com.java.common.pay.alipay.utils.AlipayTool;
import com.java.common.pay.tenpay.conf.TenpayConfig;
import com.java.common.pay.tenpay.utils.HTTPTool;
import com.java.common.pay.tenpay.utils.MD5Tool;
import com.java.common.pay.tenpay.utils.SignTool;
import com.java.common.pay.tenpay.utils.TenpayTool;
import com.java.common.pay.tenpay.utils.XMLTool;
import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.common.utils.Tool;
import com.java.sys.common.utils.Tool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.sf.json.JSONObject;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Api(value="api-pay-controller",description = "支付相关")
@SpringBootApplication
@RequestMapping("/api/payApiController")
public class PayApiController extends BaseController{
	
	
	@ApiOperation(value = "APP发起微信支付订单")
	@RequestMapping(value = "/tenpayAddOrderAPP", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> tenpayAddOrderAPP(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("订单ID，必传") @RequestParam(value="orderId",required=true) String orderId){
		String prepay_id =null;
		try{
			String ip = request.getRemoteAddr();
			String nonceStr = MD5Tool.encryptString(String.valueOf(new Date().getTime()/1000).toString());
			SortedMap<String, Object> sp = new TreeMap<String, Object>();
			sp.put("appid", TenpayConfig.weixin_appid_app);
			sp.put("attach", "附带参数");
			sp.put("body", "产品说明");
			sp.put("mch_id", TenpayConfig.weixin_mch_id_app);
			sp.put("nonce_str", nonceStr);
			sp.put("notify_url", TenpayConfig.notify_url_app);
			sp.put("out_trade_no", "订单号");	
			sp.put("spbill_create_ip", ip);
			sp.put("total_fee",1);	//1分钱
			sp.put("trade_type", "APP");
			sp.put("sign", SignTool.signApp(sp));
			/*
			 * 准备请求统一下单接口的参数
			 */
			String reqXML = Tool.mapToXML(sp);
			Tool.info("reqXML : \n"+reqXML);
			/*
			 * 请求微信统一下单url
			 */
			String postUrl="https://api.mch.weixin.qq.com/pay/unifiedorder";
			String responseStr = HTTPTool.post(reqXML, postUrl);
			Tool.info("--------------- getTenpayPrepayId : 统一下单返回结果："+responseStr);
			Document d = XMLTool.parseXMLDocument(responseStr);
			// 如果订单已支付
			if(d.getElementsByTagName("err_code") != null && d.getElementsByTagName("err_code").item(0) != null && d.getElementsByTagName("err_code").item(0).getFirstChild() != null){
				String err_code = d.getElementsByTagName("err_code").item(0).getFirstChild().getNodeValue();
				if(Tool.isNotBlank(err_code) && "ORDERPAID".equals(err_code)){
					
				}
			}
			// 否则
			String return_code = d.getElementsByTagName("return_code").item(0).getFirstChild().getNodeValue();
			if(Tool.isNotBlank(return_code) && return_code.equals("SUCCESS")){
				String result_code = d.getElementsByTagName("result_code").item(0).getFirstChild().getNodeValue();
				if(Tool.isNotBlank(result_code) && result_code.equals("SUCCESS")){
					prepay_id = d.getElementsByTagName("prepay_id").item(0).getFirstChild().getNodeValue();
					Tool.info("-------- tenpayAddOrder() : 获取到的预支付id prepay_id（本次交易的流水号） 是："+prepay_id);
					
					// 返回结果给APP前端
					SortedMap<String,Object> map = new TreeMap<String,Object>();
					map.put("appid", TenpayConfig.weixin_appid_app);
					map.put("noncestr", nonceStr);
					map.put("package", "Sign=WXPay");
					map.put("partnerid", TenpayConfig.weixin_mch_id_app);
					map.put("prepayid", prepay_id);
					map.put("timestamp", System.currentTimeMillis()/1000);
					map.put("sign", SignTool.signApp(map));
					map.put("outTradeNo", sp.get("out_trade_no"));
					map.put("total", sp.get("total_fee"));
					return buildSuccessInfo(map);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return buildFailedInfo(ApiConstance.BASE_FAIL_CODE);
	}
	
	
	
	/**
	 * 方法名：tenpayNotifyAPP
	 * 详述：微信APP支付结果异步通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value = "/tenpayNotifyAPP", method = RequestMethod.POST)
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public synchronized String tenpayNotifyAPP(HttpServletRequest request,HttpServletResponse response){
		Tool.info("--------------- tenpayNotifyAPP() : come !");
		try{
			
			Map<String,String> map = Tool.XMLToMap(request);
			Tool.printMap(map);
			String return_code = map.get("return_code");
			Tool.info("--------------- return_code : "+return_code);
			if(Tool.isNotBlank(return_code)&&return_code.equals("SUCCESS")){
				String result_code = map.get("result_code");
				Tool.info("--------------- result_code : "+result_code);
				if(Tool.isNotBlank(result_code)&&result_code.equals("SUCCESS")){
					String out_trade_no = map.get("out_trade_no");
					String transaction_id = map.get("transaction_id");
					Tool.info("---------------- out_trade_no : "+out_trade_no+" , transaction_id : "+transaction_id);
					
				}
			}
			PrintWriter out = response.getWriter();
			out.print(return_code);
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	/*@ApiOperation(value = "APP获取发起支付参数（旧版-RSA）")
	@RequestMapping(value = "/alipayAddOrderAPP1", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> alipayAddOrderAPP1(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("订单ID，必传") @RequestParam(value="orderId",required=true) String orderId){
		String orderInfo = AlipayTool.makeOrderInfo(AlipayConfig.partner, AlipayConfig.seller_email, "订单号", "商品名称", "商品详情", 0.01, AlipayConfig.notify_url_app);
		String sign = RSA.sign(orderInfo, AlipayConfig.private_key);
		sign = URLEncoder.encode(sign);
		String payInfo = orderInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
		return buildSuccessInfo(payInfo);
	}*/
	
	
	
	@ApiOperation(value = "APP获取发起支付参数（新版-RSA2）")
	@RequestMapping(value = "/alipayAddOrderAPP", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> alipayAddOrderAPP(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("aa") @RequestParam(value="aa",required=false) String aa){
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", AlipayConfig.app_id, AlipayConfig.private_key, "json", "UTF-8", AlipayConfig.alipay_public_key, "RSA2");
		AlipayTradeAppPayRequest req = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("商品详情");
		model.setSubject("商品名称");
		model.setOutTradeNo("订单号");
		model.setTimeoutExpress("30m");//超时时限
		model.setTotalAmount("0.01");//金额（元）
		req.setBizModel(model);
		req.setNotifyUrl(AlipayConfig.notify_url_app);
		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        AlipayTradeAppPayResponse response1 = alipayClient.sdkExecute(req);
		        Tool.info(response1.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
		        return buildSuccessInfo(response1.getBody());
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		}
		
		
		return buildFailedInfo(null);
	}
	
	
	/**
	 * 
	 * 方法名：alipayNotifyApp
	 * 详述：支付宝APP支付结果异步通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	/*@ApiIgnore
	@RequestMapping(value = "/alipayNotifyApp1")
	public String alipayNotifyApp1(HttpServletRequest request,HttpServletResponse response){
		Tool.info("------- alipayNotifyApp1() : come !");
		try{
			PrintWriter out = response.getWriter();
			//获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号	
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//支付宝交易号	
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			//异步通知ID
			String notify_id=request.getParameter("notify_id");
			//sign
			String sign=request.getParameter("sign");
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
			
			Tool.info("out_trade_no : "+out_trade_no+"\ntrade_no : "+trade_no+"\ntrade_status : "+trade_status+"\nnotify_id : "+notify_id+"\nsign : "+sign);
			
			//判断接受的post通知中有无notify_id，如果有则是异步通知。
			if(notify_id!=""&&notify_id!=null){
				Tool.info("notify_id != null");
				//判断成功之后使用getResponse方法判断是否是支付宝发来的异步通知。
				if(AlipayNotify.verifyResponse(notify_id).equals("true")){
					Tool.info("验证来自支付宝的通知 成功");
					//使用支付宝公钥验签
					if(AlipayNotify.getSignVeryfyApp(params, sign)){
						Tool.info("支付宝公钥验签 成功 ! trade_status : "+trade_status);
						
						 * 判断该笔订单是否在商户网站中已经做过处理
						 * 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						 * 如果有做过处理，不执行商户的业务程序
						 
						if(trade_status.equals("TRADE_FINISHED")){
							Tool.info("TRADE_FINISHED");
							
						} else if (trade_status.equals("TRADE_SUCCESS")){
							
						}
						//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
						out.print("success");//请不要修改或删除
					}else{//验证签名失败
						Tool.info("支付宝公钥验签 失败");
						out.print("sign fail");
					}
				}else{//验证是否来自支付宝的通知失败
					Tool.info("验证来自支付宝的通知 失败");
					out.print("验证是否来自支付宝的通知失败");
				}
			}else{
				Tool.info("no notify message");
				out.print("no notify message");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}*/
	
	
	
	@ApiIgnore
	@RequestMapping(value = "/alipayNotifyApp", method = RequestMethod.POST)
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public synchronized String alipayNotifyApp(HttpServletRequest request,HttpServletResponse response){
		Tool.info("--- alipayNotifyApp2() come !");
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  	}
			params.put(name, valueStr);
		}
		Tool.info("---"+JSONObject.fromObject(params).toString());
		try {
			boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, "UTF-8","RSA2");
			if(flag) {
				Tool.info("--- 支付成功");
				String out_trade_no = params.get("out_trade_no");//APP订单号
				String trade_no = params.get("trade_no");//支付宝交易号
				Tool.info("--- out_trade_no:"+out_trade_no+",trade_no"+trade_no);
			}else {
				Tool.info("--- 支付失败");
			}
			response.getWriter().print(flag);
		} catch (AlipayApiException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	@ApiOperation(value = "支付宝退款")
	@RequestMapping(value = "/alipayRefund", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> alipayRefund(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("用户id") @RequestParam(value="userId",required=true) String userId){
			JSONObject json = new JSONObject();
			json.element("out_trade_no", "订单号");//订单号
			json.element("refund_amount", "0.01");//退款金额（不能大于订单总金额）
			AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConfig.app_id,AlipayConfig.private_key,"json","UTF-8",AlipayConfig.alipay_public_key,"RSA2");
			AlipayTradeRefundRequest AlipayRequest = new AlipayTradeRefundRequest();
			AlipayRequest.setBizContent(json.toString());
			try {
				AlipayTradeRefundResponse AlipayResponse = alipayClient.execute(AlipayRequest);
				if(AlipayResponse.isSuccess()){
					Tool.info("--- refundDeposit() AlipayTradeRefundResponse : 调用成功");
					
					return buildSuccessInfo(null);
				} else {
					Tool.info("--- refundDeposit() AlipayTradeRefundResponse : 调用失败");
					return buildFailedInfo("--- refundDeposit() AlipayTradeRefundResponse : 调用失败");
				}
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
		return buildFailedInfo("退款失败");
	}
	
	
	
	@ApiOperation(value = "微信支付退款")
	@RequestMapping(value = "/tenpayRefund", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> tenpayRefund(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("用户id") @RequestParam(value="userId",required=true) String userId){
			String result = TenpayTool.refund(TenpayConfig.weixin_appid_app, TenpayConfig.weixin_mch_id_app, "订单号", "退款单号", 1/*订单总额*/, 1/*退款金额*/);
			Document d = XMLTool.parseXMLDocument(result);
			String return_code = d.getElementsByTagName("return_code").item(0).getFirstChild().getNodeValue();
			if(Tool.isNotBlank(return_code) && return_code.equals("SUCCESS")){
				String result_code = d.getElementsByTagName("result_code").item(0).getFirstChild().getNodeValue();
				if(Tool.isNotBlank(result_code) && result_code.equals("SUCCESS")){
					Tool.info("--- 退款成功");
				}
			}
		return buildFailedInfo("退款失败");
	}
	
	
	@ApiOperation(value = "微信支付企业付款")
	@RequestMapping(value = "/tenpayTransfer", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> tenpayTransfer(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("openid") @RequestParam(value="openid",required=true) String openid,
			@ApiParam("re_user_name") @RequestParam(value="re_user_name",required=true) String re_user_name){
		//发起请求
		String partner_trade_no = Tool.makeOrderNo();
		String result = TenpayTool.transfer(TenpayConfig.weixin_appid_mini, TenpayConfig.weixin_mch_id_mini, partner_trade_no, openid, re_user_name, 100, "企业付款测试");
		Tool.info("--- testTransfer 1 : "+result);
		Map<String,String> map = Tool.XMLToMap(result);
		String return_code = map.get("return_code");
		//如果成功
		if("SUCCESS".equals(return_code)) {
			return buildSuccessInfo(null);
		}else {
			//如果失败并且错误码是SYSTEMERROR（系统繁忙，请稍后再试。），先查询，不行再原订单重试
			String err_code = map.get("err_code");
			if("SYSTEMERROR".equals(err_code)) {
				//查询
				String queryResult = TenpayTool.getTransferInfo(TenpayConfig.weixin_appid_mini, TenpayConfig.weixin_mch_id_mini, partner_trade_no);
				Map<String,String> queryMap = Tool.XMLToMap(queryResult);
				if("SUCCESS".equals(queryMap.get("return_code")) && "SUCCESS".equals(queryMap.get("status"))) {
					return buildSuccessInfo(null);
				}else {
					//重试
					result = TenpayTool.transfer(TenpayConfig.weixin_appid_mini, TenpayConfig.weixin_mch_id_mini, partner_trade_no, openid, re_user_name, 100, "企业付款测试");
					Tool.info("--- testTransfer 2 : "+result);
					map = Tool.XMLToMap(result);
					return_code = map.get("return_code");
					if("SUCCESS".equals(return_code)) {
						return buildSuccessInfo(null);
					}else {
						String err_code_des = map.get("err_code_des");
						return buildFailedInfo(err_code_des);
					}
				}
			}else {
				////如果失败并且错误码不是SYSTEMERROR，返回错误信息
				String err_code_des = map.get("err_code_des");
				return buildFailedInfo(err_code_des);
			}
		}
	}
	
	
	
	@ApiOperation(value = "支付宝企业转账给个人账户")
	@RequestMapping(value = "/alipayTranster", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> alipayTranster(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("用户id") @RequestParam(value="userId",required=true) String userId){
		String out_biz_no = System.currentTimeMillis()+"";
		String payee_account = "13580389054";
		String payee_real_name = "李启华";
		String amount = "0.1";
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConfig.app_id,AlipayConfig.private_key,"json","UTF-8",AlipayConfig.alipay_public_key,"RSA2");
		AlipayFundTransToaccountTransferRequest aliRequest = new AlipayFundTransToaccountTransferRequest();
		aliRequest.setBizContent("{" +
		"    \"out_biz_no\":\""+out_biz_no+"\"," +
		"    \"payee_type\":\"ALIPAY_LOGONID\"," +
		"    \"payee_account\":\""+payee_account+"\"," +
		"    \"amount\":\""+amount+"\"," +
		"    \"payer_show_name\":\"选大学官方\"," +
		"    \"payee_real_name\":\""+payee_real_name+"\"," +
		"    \"remark\":\"转账备注test\"," +
		"  }");
		
		try {
			AlipayFundTransToaccountTransferResponse aliResponse = alipayClient.execute(aliRequest);
			System.out.println(aliResponse.getBody());
			if(aliResponse.isSuccess()){
				System.out.println("调用成功");
			} else {
				System.out.println("调用失败");
				JSONObject json = JSONObject.fromObject(aliResponse.getBody()).getJSONObject("alipay_fund_trans_toaccount_transfer_response");
				String code = json.getString("code");//最外层状态码，10000表示成功
				String sub_code = json.getString("sub_code");//子层错误码
				String sub_msg = json.getString("sub_msg");//子层错误信息
				if(sub_code.equals("PAYER_BALANCE_NOT_ENOUGH")) {
					System.out.println("--- 企业余额不足");
				}else if(sub_code.equals("PAYEE_USER_INFO_ERROR")) {
					System.out.println("--- 支付宝账号和姓名不匹配");
				}else if(sub_code.equals("PAYEE_NOT_EXIST")) {
					System.out.println("--- 收款账号不存在");
				}else if(sub_code.equals("EXCEED_LIMIT_UNRN_DM_AMOUNT")) {
					System.out.println("--- 收款账户未实名认证");
				}else if(sub_code.equals("EXCEED_LIMIT_SM_MIN_AMOUNT")) {
					System.out.println("--- 单笔最低转账金额0.1元");
				}else if(sub_code.equals("EXCEED_LIMIT_PERSONAL_SM_AMOUNT")) {
					System.out.println("--- 转账给个人支付宝账户单笔最多5万元");
				}else if(sub_code.equals("EXCEED_LIMIT_ENT_SM_AMOUNT")) {
					System.out.println("--- 转账给企业支付宝账户单笔最多10万元");
				}else if(sub_code.equals("PAYEE_ACC_OCUPIED")) {
					System.out.println("--- 该手机号对应多个支付宝账户，请传入收款方姓名确定正确的收款账号");
				}
				if(code.equals("20000") || sub_code.equals("SYSTEM_ERROR")) {
					System.out.println("--- 系统繁忙");
				}
				//状态码信息官网地址：https://docs.open.alipay.com/api_28/alipay.fund.trans.toaccount.transfer
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return buildSuccessInfo(null);
	}
	
	
	@ApiOperation(value = "支付宝企业转账给个人账户结果查询")
	@RequestMapping(value = "/alipayTransterQuery", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> alipayTransterQuery(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("用户id") @RequestParam(value="userId",required=true) String userId){
		//参数二选一
		String out_biz_no = "1513218073639";//订单号
		String order_id = "";//支付宝订单id
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConfig.app_id,AlipayConfig.private_key,"json","UTF-8",AlipayConfig.alipay_public_key,"RSA2");
		AlipayFundTransOrderQueryRequest aliRequest = new AlipayFundTransOrderQueryRequest();
		aliRequest.setBizContent("{" +
		"    \"out_biz_no\":\""+out_biz_no+"\"," +
		"    \"order_id\":\""+order_id+"\"" +
		"  }");
		try {
			AlipayFundTransOrderQueryResponse aliResponse = alipayClient.execute(aliRequest);
			System.out.println(aliResponse.getBody());
			if(aliResponse.isSuccess()){
				System.out.println("付款成功");
			} else {
				System.out.println("付款失败");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		//官网文档地址：https://docs.open.alipay.com/api_28/alipay.fund.trans.order.query/
		return buildSuccessInfo(null);
	}


	/**
	 * 方法名：alipayTransterSuccess
	 * 详述：根据订单号判断企业转账是否成功
	 * @param out_biz_no
	 * @return
	 */
	protected boolean alipayTransterSuccess(String out_biz_no) {
		String order_id = "";//支付宝订单id
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",AlipayConfig.app_id,AlipayConfig.private_key,"json","UTF-8",AlipayConfig.alipay_public_key,"RSA2");
		AlipayFundTransOrderQueryRequest aliRequest = new AlipayFundTransOrderQueryRequest();
		aliRequest.setBizContent("{" +
				"    \"out_biz_no\":\""+out_biz_no+"\"," +
				"    \"order_id\":\""+order_id+"\"" +
				"  }");
		try {
			AlipayFundTransOrderQueryResponse aliResponse = alipayClient.execute(aliRequest);
			System.out.println(aliResponse.getBody());
			if(aliResponse.isSuccess()){
				return true;
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
