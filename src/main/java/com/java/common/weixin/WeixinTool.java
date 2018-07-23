package com.java.common.weixin;

import com.java.common.constance.MyConstance;
import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.utils.Tool;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


public class WeixinTool {
	
	/**
	 * 方法名：init
	 * 详述：初始化微信参数
	 */
	public static void keyRefresh(){
		refreshAccessToken();
		refreshJsapiTicket();
	}
	
	/**
	 * 方法名：getAccessToken
	 * 详述：返回微信的ACCESS_TOKEN
	 * @return
	 */
	public static String getAccessToken(){
		String accessToken = (String) CacheUtil.get(MyConstance.KEY_WEIXIN_ACCESS_TOKEN);
		if(Tool.isBlank(accessToken)){
			WeixinTool.refreshAccessToken();
			accessToken = (String) CacheUtil.get(MyConstance.KEY_WEIXIN_ACCESS_TOKEN);
		}
		return accessToken;
	}
	
	/**
	 * 
	 * 获取ACCESS_TOKEN
	 * 正常情况下，微信会返回下述JSON数据包给公众号：
	 * {"access_token":"ACCESS_TOKEN","expires_in":7200}
	 * 错误时微信会返回错误码等信息，JSON数据包示例如下（该示例为AppID无效错误）:
	 * {"errcode":40013,"errmsg":"invalid appid"}
	 */
	public static String refreshAccessToken(){
		String appid = WeixinConfig.APP_ID;
		String appsecrect = WeixinConfig.APP_SERECT;
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+appsecrect;
		String result = Tool.get(url);
		JSONObject json = JSONObject.fromObject(result);
		String accessToken = json.getString("access_token");
		CacheUtil.set(MyConstance.KEY_WEIXIN_ACCESS_TOKEN,accessToken);
		return accessToken;
	}
	
	
	/**
	 * 方法名：getUserInfo
	 * 详述：根据openId获取用户资料
	 * @param openId
	 * @return
	 */
	public static String getUserInfoByOpenId(String openId){
		String accessToken = getAccessToken();
		String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN";
		String result = Tool.get(url);
		return result;
	}
	
	/**
	 * 
	 * 方法名：getUserInfoByCode
	 * 详述：根据网页授权code获取用户资料
	 * @param code
	 * @return
	 */
	public static String getUserInfoByCode(String code){
		String appid = WeixinConfig.APP_ID;
		String appsecrect = WeixinConfig.APP_SERECT;
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecrect+"&code="+code+"&grant_type=authorization_code";
		String result = Tool.get(url);
		try{
			JSONObject json = JSONObject.fromObject(result);
			String openId = json.getString("openid");
			return getUserInfoByOpenId(openId);
		}catch(JSONException e){
			Tool.info("--- WeixinTool getUserInfoByCode() result : "+result);
			return result;
		}
	}
	
	
	/**
	 * 方法名：refreshJsapiTicket
	 * 详述：刷新jsapi_ticket
	 * @return
	 */
	public static String refreshJsapiTicket(){
		String accessToken = getAccessToken();
		String result = Tool.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+accessToken+"&type=jsapi");
		JSONObject obj = JSONObject.fromObject(result);
		String ticket = obj.getString("ticket");
		CacheUtil.set(MyConstance.KEY_WEIXIN_JSAPI_TICKET,ticket);
		return ticket;
	}
	
	
	
	/**
	 * 方法名：getJsapiTicket
	 * 详述：从缓存中获取jsapi_ticket
	 * @return
	 */
	public static String getJsapiTicket(){
		String ticket = (String) CacheUtil.get(MyConstance.KEY_WEIXIN_JSAPI_TICKET);
		if(Tool.isBlank(ticket)){
			ticket = refreshJsapiTicket();
		}
		return ticket;
	}
	
	/**
	 * 方法名：sendCustomMessage
	 * 详述：发送客服消息
	 * @param json
	 * @return
	 */
	public static void sendCustomMessage(JSONObject json){
		if(json != null){
			Tool.post(json.toString(), "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+getAccessToken());
		}
	}
	
	
	/**
	 * 方法名：addMaterial
	 * 详述：新增其他类型永久素材
	 * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @param mFile MultipartFile文件
	 * @param req HttpServletRequest
	 * @return
	 */
	public static String addMaterial(String type,MultipartFile mFile,HttpServletRequest req){
		try {
			String suffix = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("."));
			String saveDirStr = req.getSession().getServletContext().getRealPath("/uploads/files/temp/");
			String newFileName = System.currentTimeMillis()+""+suffix;
			File saveDir = new File(saveDirStr);
			saveDir.mkdirs();
			File file = new File(saveDirStr,newFileName);
			file.createNewFile();
			mFile.transferTo(file);
			MultipartBody body;
			MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
			bodyBuilder.setType(MultipartBody.FORM);
			bodyBuilder.addFormDataPart("type",type);
			bodyBuilder.addFormDataPart("access_token",getAccessToken());
			MediaType mt = MediaType.parse("application/octet-stream");
			bodyBuilder.addFormDataPart("media", file.getName(), RequestBody.create(mt, file));
			body = bodyBuilder.build();
			Request request = new Request.Builder().url("https://api.weixin.qq.com/cgi-bin/material/add_material").post(body).build();
			OkHttpClient client = new OkHttpClient();
			Response response = client.newCall(request).execute();
			String result = response.body().string();
			Tool.info("--- addMaterial() result : "+result);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
