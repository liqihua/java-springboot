package com.java.controller.weixin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.common.constance.ApiConstance;
import com.java.common.pay.tenpay.utils.SignTool;
import com.java.common.weixin.WeixinConfig;
import com.java.common.weixin.WeixinTool;
import com.java.entity.weixin.TextMessage;
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
@Api(value="weixin-index-controller",description="微信服务器基础响应接口")
@SpringBootApplication
@RequestMapping("/weixin/indexWeixinController")
public class IndexWeixinController extends BaseController{
	
	
	@ApiIgnore
	@ApiOperation(value = "index")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> index(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("signature") @RequestParam(value="signature",required=false) String signature,
			@ApiParam("timestamp") @RequestParam(value="timestamp",required=false) String timestamp,
			@ApiParam("nonce") @RequestParam(value="nonce",required=false) String nonce,
			@ApiParam("echostr") @RequestParam(value="echostr",required=false) String echostr){
		try {
			if(Tool.isNotBlank(echostr)){
				Tool.info("--- index() -> signature:"+signature+",timestamp:"+timestamp+",nonce:"+nonce+",echostr:"+echostr);
				response.getWriter().print(echostr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@ApiIgnore
	@ApiOperation(value = "index")
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> index(HttpServletRequest request,HttpServletResponse response){
		try {
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
				Map<String,String> map = Tool.XMLToMap(request);
				Tool.printMap(map);
				
				String msgType = map.get("MsgType");
				String toUserName = map.get("ToUserName");
				String fromUserName = map.get("FromUserName");
				
				if("text".equals(msgType)){
					String content = map.get("Content");
					TextMessage tm = new TextMessage(fromUserName, toUserName, new Date().getTime()+"","text", "你发送的内容是:"+content,null);
					print(tm,response);
				}
				
				if("image".equals(msgType)){
					String PicUrl = map.get("PicUrl");
					TextMessage tm = new TextMessage(fromUserName, toUserName, new Date().getTime()+"","text", "图片链接是:"+PicUrl,null);
					print(tm,response);
				}
				
				if("location".equals(msgType)){
					String Location_X = map.get("Location_X");
					String Location_Y = map.get("Location_Y");
					String Label = map.get("Label");
					
					TextMessage tm = new TextMessage(fromUserName, toUserName, new Date().getTime()+"","text", "经纬度是:"+Location_X+","+Location_Y+"。"+Label,null);
					print(tm,response);
				}
				
				if("event".equals(msgType)){
					String event = map.get("Event");
					if(event.equals("LOCATION")){
						String latitude = map.get("Latitude");
						String longitude = map.get("Longitude");
						TextMessage tm = new TextMessage(fromUserName, toUserName, new Date().getTime()+"","text", "收到的经纬度是："+latitude+","+longitude,null);
						print(tm,response);
					}
					if(event.equals("subscribe")){
						TextMessage tm = new TextMessage(fromUserName, toUserName, new Date().getTime()+"","text", "收到关注事件",null);
						print(tm,response);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@ApiOperation(value = "获取ACCESS_TOKEN")
	@RequestMapping(value = "/getAccessToken", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> getAccessToken(HttpServletRequest request,HttpServletResponse response){
		String accessToken = WeixinTool.getAccessToken();
		return buildSuccessInfo(accessToken);
	}
	
	
	
	@ApiOperation(value = "刷新菜单")
	@RequestMapping(value = "/refreshMenu", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> refreshMenu(HttpServletRequest request,HttpServletResponse response){
		try{
			InputStream in = request.getServletContext().getResourceAsStream("/WEB-INF/classes/weixin_menu.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line=br.readLine()) != null){
				sb.append(line);
			}
			Tool.info("-------- CreateMenu weixin_menu.txt :\n "+sb.toString());
			String result = Tool.post(sb.toString(), "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+WeixinTool.getAccessToken());
			return buildSuccessInfo(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		return buildFailedInfo(ApiConstance.BASE_FAIL_CODE);
	}
	
	
	
	@ApiOperation(value = "根据openId获取用户信息")
	@RequestMapping(value = "/getUserInfoByOpenId", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> getUserInfoByOpenId(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("openId") @RequestParam(value="openId",required=true) String openId){
		String str = WeixinTool.getUserInfoByOpenId(openId);
		Tool.info("--- getUserInfoByOpenId() : \n"+str);
		return buildSuccessInfo(str);
	}
	
	
	@ApiOperation(value = "根据授权code获取用户信息")
	@RequestMapping(value = "/getUserInfoByCode", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> getUserInfoByCode(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("code") @RequestParam(value="code",required=true) String code){
		String str = WeixinTool.getUserInfoByCode(code);
		Tool.info("--- getUserInfoByCode() : \n"+str);
		return buildSuccessInfo(str);
	}
	
	
	
	@ApiOperation(value = "获取js-sdk初始化配置")
	@RequestMapping(value = "/getJsConfig", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> getJsConfig(HttpServletRequest request,HttpServletResponse response,
			@ApiParam("url") @RequestParam(value="url",required=true) String url){
		String jsapiTicket = WeixinTool.getJsapiTicket();
		Long timestamp = new Date().getTime()/1000;
		String noncestr = UUID.randomUUID().toString().replace("-", "").substring(10);
		SortedMap<String, Object> map = new TreeMap<String, Object>();
		map.put("noncestr", noncestr);
		map.put("jsapi_ticket", jsapiTicket);
		map.put("timestamp", timestamp);
		map.put("url",url);
		map.put("signature", SignTool.signSHA1(map));
		map.put("appId",WeixinConfig.APP_ID);
		Tool.info("--- getJSApiConfig()  map : ");
		Tool.info(" noncestr : "+map.get("noncestr")+"\n jsapi_ticket : "+map.get("jsapi_ticket")+"\n timestamp : "+map.get("timestamp")+"\n url : "+map.get("url")+"\n signature : "+map.get("signature")+"\n");
		return buildSuccessInfo(map);
	}
	
	
	
	
	@ApiOperation(value = "获取jsapi_ticket")
	@RequestMapping(value = "/getJsapiTicket", method = RequestMethod.POST)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> getJsapiTicket(HttpServletRequest request,HttpServletResponse response){
		return buildSuccessInfo(WeixinTool.getJsapiTicket());
	}
	
	
	
	
	public static void print(TextMessage tm,HttpServletResponse response){
		try{
			String xmlStr = Tool.objToXML(tm);
			Tool.info(xmlStr);
			response.getWriter().print(xmlStr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
