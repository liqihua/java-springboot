package com.java.controller.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.java.common.constance.ApiConstance;
import com.java.common.pay.alipay.conf.AlipayConfig;
import com.java.common.pay.alipay.utils.AlipayNotify;
import com.java.common.pay.alipay.utils.AlipaySubmit;
import com.java.common.pay.tenpay.conf.TenpayConfig;
import com.java.common.pay.tenpay.utils.HTTPTool;
import com.java.common.pay.tenpay.utils.MD5Tool;
import com.java.common.pay.tenpay.utils.SignTool;
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
import springfox.documentation.annotations.ApiIgnore;




@ApiIgnore
@Api(value="ajax-pay-controller",description = "支付相关")
@SpringBootApplication
@RequestMapping("/ajax/payAjaxController")
public class PayAjaxController extends BaseController{
	
	
	
	@ApiOperation(value = "生成微信支付扫一扫的二维码url", notes = "")
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class) })
	@RequestMapping(value = "/makeTenpayScanUrl", method = RequestMethod.POST)
	public ResponseEntity<BaseResult> makeTenpayScanUrl(HttpServletRequest request,
			@ApiParam("订单id，必传") @RequestParam(value="orderId",required=false) String orderId){
		try{
			Long time_stamp = new Date().getTime()/1000;
			SortedMap<String, Object> signMap = new TreeMap<String, Object>();
			signMap.put("appid", TenpayConfig.weixin_appid_scan);
			signMap.put("mch_id", TenpayConfig.weixin_mch_id_scan);
			signMap.put("nonce_str", MD5Tool.encryptString(String.valueOf(time_stamp)));
			signMap.put("time_stamp", new Date().getTime()/1000);
			signMap.put("product_id", "orderid");	// 订单id，扫描回调的时候微信会返回这个product_id
			signMap.put("sign", SignTool.signScan(signMap));
			String wm = "weixin://wxpay/bizpayurl?appid="+signMap.get("appid")+"&mch_id="+signMap.get("mch_id")+"&nonce_str="+signMap.get("nonce_str")+"&product_id="+signMap.get("product_id")+"&time_stamp="+signMap.get("time_stamp")+"&sign="+signMap.get("sign");
			Map<String,String> map = new HashMap<String,String>();
			map.put("wm", wm);
			return buildSuccessInfo(map);
		}catch(Exception e){
			e.printStackTrace();
		}
		return buildFailedInfo(ApiConstance.BASE_FAIL_CODE);
	}
	
	
	/**
	 * 方法名：tenpayScan
	 * 详述：微信用户扫了二维码以后的回调统一下单接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value = "/tenpayScan")
	public String tenpayScan(HttpServletRequest request,HttpServletResponse response){
		try{
			Tool.info("----------- tenpayScan() : come !");
			/*
			 * 获取微信回调参数
			 */
			Map<String,String> reqMap = Tool.XMLToMap(request);
			if(reqMap != null){
				Tool.printMap(reqMap);
				String product_id = reqMap.get("product_id");	// 生成微信扫描二维码时传入的，通常给这个参数放订单id
				if(Tool.isNotBlank(product_id)){
					// 用 product_id 查询订单去查订单，把查出来的订单里面的订单号放进下面的out_trade_no
					/*
					 * 统一下单接口需要的参数
					 */
					String ip = request.getRemoteAddr();
					SortedMap<String, Object> map = new TreeMap<String, Object>();
					map.put("appid", TenpayConfig.weixin_appid_scan);
					map.put("mch_id", TenpayConfig.weixin_mch_id_scan);
					map.put("nonce_str", MD5Tool.encryptString(String.valueOf(new Date().getTime()/1000).toString()));
					map.put("body", "时间出租");
					map.put("out_trade_no", System.currentTimeMillis()+"");		//订单号
					map.put("total_fee",1);
					map.put("spbill_create_ip", ip);
					map.put("notify_url", TenpayConfig.notify_url_scan);
					map.put("trade_type", "NATIVE");
					map.put("product_id", product_id);
					map.put("sign", SignTool.signScan(map));
					/*
					 * 把参数转成xml格式
					 */
					String xml = Tool.mapToXML(map);
					/*
					 * 请求微信统一下单url
					 */
					String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
					String responseStr = HTTPTool.post(xml, url);
					Tool.info("--------------- tenpayScan() : 统一下单返回结果：\n"+responseStr);
					Document d = XMLTool.parseXMLDocument(responseStr);
					String return_code = d.getElementsByTagName("return_code").item(0).getFirstChild().getNodeValue();
					String return_msg = d.getElementsByTagName("return_msg").item(0).getFirstChild().getNodeValue();
					String prepay_id =null;
					if(Tool.isNotBlank(return_code) && return_code.equals("SUCCESS")){
						String result_code = d.getElementsByTagName("result_code").item(0).getFirstChild().getNodeValue();
						if(Tool.isNotBlank(result_code) && result_code.equals("SUCCESS")){
							prepay_id = d.getElementsByTagName("prepay_id").item(0).getFirstChild().getNodeValue();
							Tool.info("-------- tenpayScan() : 获取到的预支付id prepay_id 是："+prepay_id);
							/*
							 * 把相关参数按要求转成xml响应给微信服务器，微信服务器向客户端发起输入支付密码
							 */
							SortedMap<String, Object> signParams = new TreeMap<String, Object>();
							signParams.put("return_code", return_code);
							signParams.put("return_msg", return_msg);
							signParams.put("appid", TenpayConfig.weixin_appid_scan);
							signParams.put("mch_id", TenpayConfig.weixin_mch_id_scan);
							signParams.put("nonce_str", MD5Tool.encryptString(String.valueOf(new Date().getTime()/1000).toString()));
							signParams.put("prepay_id", prepay_id);
							signParams.put("result_code", return_code);
							signParams.put("err_code_des", return_msg);
							signParams.put("sign", SignTool.signScan(signParams));
							
							response.setHeader("ContentType", "text/xml");
							response.setHeader("Pragma", "No-cache");
							response.setHeader("Cache-Control", "no-cache");
							response.setDateHeader("Expires", 0);
							PrintWriter out = response.getWriter();
							out.flush();
							String returnStr = Tool.mapToXML(signParams);
							Tool.info("---------- return : \n"+returnStr);
							out.print(returnStr);
							out.close();
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 方法名：tenpayNotifyScan
	 * 详述：微信扫码支付结果异步通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value = "/tenpayNotifyScan")
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public synchronized String tenpayNotifyScan(HttpServletRequest request,HttpServletResponse response){
		Tool.info("------- tenpayNotifyScan() : come !");
		try{
			Map<String,String> map = Tool.XMLToMap(request);
			Tool.printMap(map);
			String return_code = map.get("return_code");
			Tool.info("------- tenpayNotifyScan() return_code : "+return_code);
			if(Tool.isNotBlank(return_code)&&return_code.equals("SUCCESS")){
				String result_code = map.get("result_code");
				Tool.info("------- tenpayNotifyScan() result_code : "+result_code);
				if(Tool.isNotBlank(result_code)&&result_code.equals("SUCCESS")){
					String out_trade_no = map.get("out_trade_no");		// 订单号
					String transaction_id = map.get("transaction_id");	// 微信交易号（交易id，也叫流水id、微信的订单id）
					Tool.info("------- tenpayNotifyScan() : 支付成功！\n out_trade_no : "+out_trade_no+", transaction_id : "+transaction_id);
					
				}
			}
			response.setHeader("ContentType", "text/xml");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			PrintWriter out = response.getWriter();
			out.flush();
			out.print(return_code);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	@ApiOperation(value = "微信公众号发起支付")
	@RequestMapping(value = "/tenpayPub", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})
	public ResponseEntity<BaseResult> tenpayPub(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("微信openId") @RequestParam(value="openId",required=true) String openId){
		try{
			Tool.info("----------- tenpayJSAPI() : come !");
			/*
			 * 统一下单接口需要的参数
			 */
			String ip = request.getRemoteAddr();
			SortedMap<String, Object> sp = new TreeMap<String, Object>();
			sp.put("appid", TenpayConfig.weixin_appid_pub);
			sp.put("mch_id", TenpayConfig.weixin_mch_id_pub);
			sp.put("nonce_str", MD5Tool.encryptString(String.valueOf(new Date().getTime()/1000).toString()));
			sp.put("body", "产品名称");
			sp.put("out_trade_no", "订单号");
			sp.put("total_fee",1);
			sp.put("spbill_create_ip", ip);
			sp.put("notify_url", TenpayConfig.notify_url_pub);
			sp.put("trade_type", "JSAPI");
			sp.put("openid", openId);
			sp.put("sign", SignTool.signPub(sp));
			/*
			 * 把参数转成xml格式
			 */
			String xml = Tool.mapToXML(sp);
			/*
			 * 请求微信统一下单url
			 */
			String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
			String responseStr = HTTPTool.post(xml, url);
			Tool.info("--------------- tenpayJSAPI() : 统一下单返回结果：\n"+responseStr);
			Document d = XMLTool.parseXMLDocument(responseStr);
			String return_code = d.getElementsByTagName("return_code").item(0).getFirstChild().getNodeValue();
			String return_msg = d.getElementsByTagName("return_msg").item(0).getFirstChild().getNodeValue();
			String prepay_id =null;
			if(Tool.isNotBlank(return_code) && return_code.equals("SUCCESS")){
				String result_code = d.getElementsByTagName("result_code").item(0).getFirstChild().getNodeValue();
				if(Tool.isNotBlank(result_code) && result_code.equals("SUCCESS")){
					prepay_id = d.getElementsByTagName("prepay_id").item(0).getFirstChild().getNodeValue();
					Tool.info("-------- tenpayJSAPI() : 获取到的预支付id prepay_id 是："+prepay_id);
					
					SortedMap<String, Object> map = new TreeMap<String, Object>();
					map.put("appId", TenpayConfig.weixin_appid_pub);
					map.put("timeStamp", System.currentTimeMillis()/1000+"");
					map.put("nonceStr", UUID.randomUUID().toString().replace("-", "").substring(10));
					map.put("package", "prepay_id="+prepay_id);
					map.put("signType", "MD5");
					map.put("paySign", SignTool.signPub(map).toUpperCase());
					map.put("prepay_id", prepay_id);
					return buildSuccessInfo(map);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return buildFailedInfo(null);
	}
	
	
	/**
	 * 方法名：tenpayNotifyPub
	 * 详述：微信公众号支付结果异步通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value = "/tenpayNotifyPub")
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public synchronized String tenpayNotifyPub(HttpServletRequest request,HttpServletResponse response){
		Tool.info("------- tenpayNotifyPub() : come !");
		try{
			Map<String,String> map = Tool.XMLToMap(request);
			Tool.printMap(map);
			String return_code = map.get("return_code");
			Tool.info("------- tenpayNotifyPub() return_code : "+return_code);
			if(Tool.isNotBlank(return_code)&&return_code.equals("SUCCESS")){
				String result_code = map.get("result_code");
				Tool.info("------- tenpayNotifyPub() result_code : "+result_code);
				if(Tool.isNotBlank(result_code)&&result_code.equals("SUCCESS")){
					String out_trade_no = map.get("out_trade_no");		// 订单号
					String transaction_id = map.get("transaction_id");	// 微信交易号（交易id，也叫流水id、微信的订单id）
					Tool.info("------- tenpayNotifyPub() : 支付成功！\n out_trade_no : "+out_trade_no+", transaction_id : "+transaction_id);
					
				}
			}
			response.setHeader("ContentType", "text/xml");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			PrintWriter out = response.getWriter();
			out.flush();
			out.print(return_code);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@ApiOperation(value = "跳到支付宝即时到帐页面（含扫一扫和密码支付）")
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class) })
	@RequestMapping(value = "/alipayPayWeb",method=RequestMethod.GET)
	public String alipayPayWeb(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("订单ID，必传") @RequestParam(value="orderId",required=true) String orderId){
		try{
			
			String payment_type = "1";			//支付类型，必填，不能修改
			String out_trade_no = "订单号";		//订单号
			String subject = "订单名称";		//订单名称
			String total_fee = "0.01";		//付款金额
			String body = "订单描述";			//订单描述
			String show_url = AlipayConfig.show_url_web;	//商品展示地址
			String anti_phishing_key = System.currentTimeMillis()/1000+"";	//防钓鱼时间戳
			String exter_invoke_ip = request.getRemoteAddr();	//客户端的IP地址
			
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "create_direct_pay_by_user");
	        sParaTemp.put("partner", AlipayConfig.partner);
	        sParaTemp.put("seller_email", AlipayConfig.seller_email);
	        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
			sParaTemp.put("payment_type", payment_type);
			sParaTemp.put("notify_url", AlipayConfig.notify_url_web);
			sParaTemp.put("return_url", AlipayConfig.return_url_web);
			sParaTemp.put("out_trade_no", out_trade_no);
			sParaTemp.put("subject", subject);
			sParaTemp.put("total_fee", total_fee);
			sParaTemp.put("body", body);
			sParaTemp.put("show_url", show_url);
			sParaTemp.put("anti_phishing_key", anti_phishing_key);
			sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
			
			//建立请求
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
			request.setAttribute("sHtmlText", sHtmlText);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "/WEB-INF/views/project/alipayScan.jsp";
	}
	
	
	/**
	 * 方法名：alipayNotifyWeb
	 * 详述：支付宝即时到帐支付结果异步通知接口
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiIgnore
	@RequestMapping(value = "/alipayNotifyWeb")
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public synchronized String alipayNotifyWeb(HttpServletRequest request,HttpServletResponse response){
		Tool.info("------- alipayNotifyWeb() : come !");
		try{
			PrintWriter out = response.getWriter();
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
			
			//商户订单号
			String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//支付宝交易号
			String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//交易状态
			String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
			
			Tool.info("out_trade_no:"+out_trade_no+",trade_no:"+trade_no+",trade_status:"+trade_status);
			if(AlipayNotify.verifyWeb(params)){//验证成功
				Tool.info("----------------- 验证成功 ! trade_status : "+trade_status);
				/*
				 * 判断该笔订单是否在商户网站中已经做过处理
				 * 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				 * 如果有做过处理，不执行商户的业务程序
				 */
				if(trade_status.equals("TRADE_FINISHED")){
					Tool.info("----------------- TRADE_FINISHED");
				} else if (trade_status.equals("TRADE_SUCCESS")){
					Tool.info("----------------- TRADE_SUCCESS");
					
					
				}
				out.print("验证成功");
			}else{//验证失败
				Tool.info("----------------- 验证失败");
				out.print("验证失败");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	@ApiOperation(value = "H5Alipay")
	@RequestMapping(value = "/H5Alipay", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public String H5Alipay(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("aa") @RequestParam(value="aa",required=false) String aa) throws IOException{
		String APP_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOAC+ng/Mm3NQXzgVtzIlDT7aH7XRUwyf+ynxtdilggbPrv4MDLqIZfTR0VnsKaBAZU3bLd8B/rk4q6wnglCNoz+pM7LTA5ALyh1oxFuUd0Ocem9sG/shiorzbNxgXdHk17Cre0nVv+dRSiz0vU2yqlWchu/HiNzhIMnYYEkxRf1AgMBAAECgYAB4USE2dLshPp1t9RCyhQkVa/M0LWuLAZS0B2lFRi5PLFMFoshAkIB6i4A8RvUaeS/gdWRvKK8gks8uBK7dSjMBhhJ94ycR8Hm4ffxiCBSRadpbJ2gPlcxvYaUleXcKb8m/tUW0XWLp3vfd6wCNEfeP9CKgTwSoDi0sRj1xTj+gQJBAP4GO9Qjw2u/kuNW8iQO1SKb+pkXNNgabouwOeGZxOVJinnWYsPByjIkc0yBlvB/Y4anJD7VhpiXWOiB9VcsS/ECQQDhwP0qFq8yX3X7GwRC4gfuxompOs8cITj2ilblV2CloeuQe4sQkAkvxQllsyve7e9igjULprMPXk+S0S2yPqBFAkB/3+V4SjSS5hJsjRVB35GdZYXGUS9R5iRl/2Vz0vOSNO4XnTuEreY6Ta0ZA1dXY9lXLIaLdvPVlsKz8M/vfSYRAkEAp2eK1CkaJYpwXJJ+KHKgW0nSTzGS31MrgHa5VTvkKS7XFb7nMpL8nPXJsCabA6cA+tV/LHKjGWRgMYj4D9Ms2QJBAOfOb5F/VTwWpgJoP6ov6a78U84lQa0fGqhexA9BQl90dsVM2Yq6J/MbiPY+abjEDDMOroslUC+IH/DIF/CFWTQ=";
		String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", "2017081808259717", APP_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2"); //获得初始化的AlipayClient
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
		alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
		alipayRequest.setNotifyUrl("http://domain.com/CallBack/notify_url.jsp");//在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{" +
		" \"out_trade_no\":\""+System.currentTimeMillis()+"\"," +
		" \"total_amount\":\"0.01\"," +
		" \"subject\":\"Iphone6 16G\"," +
		" \"product_code\":\"QUICK_WAP_PAY\"" +
		" }");//填充业务参数
		String form="";
		try {
		form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
		} catch (AlipayApiException e) {
		e.printStackTrace();
		}
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(form);//直接将完整的表单html输出到页面
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}
	
	
	
	
}
