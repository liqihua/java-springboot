package com.java.common.push.alipush;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.java.common.constance.MyConstance;
import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.utils.Tool;
import net.sf.json.JSONObject;

public class Alipush {
	protected static final String ACCESS_KEY_ID = "LTAIsv9AyrCz9sMY";
	protected static final String ACCESS_KEY_SERECT = "e4fUeM0QZ6tp813kEJ8G8DH32NMUIH";
	protected static final Long APP_KEY = 24719578L;
	
	

	/**
	 * 方法名：push
	 * 详述：推送通知
	 * @param content
	 * @param token
	 * @param jsonParam
	 */
	public static void push(String content,String token,String jsonParam) {
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SERECT);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        PushRequest pushRequest = new PushRequest();
        /*
         * 推送目标
         */
        pushRequest.setAppKey(APP_KEY);
        pushRequest.setTarget("DEVICE"); //推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
        pushRequest.setTargetValue(token); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
        pushRequest.setPushType("NOTICE"); // 消息类型 MESSAGE NOTICE
        pushRequest.setDeviceType("ALL"); // 设备类型 ANDROID iOS ALL.
        // 推送配置
        pushRequest.setTitle(content); // 消息的标题
        pushRequest.setBody(content); // 消息的内容
        // 推送配置: iOS
        pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
        if(Tool.isNotBlank(jsonParam)) {
        	 pushRequest.setIOSExtParameters(jsonParam);
             pushRequest.setAndroidExtParameters(jsonParam);
        }
        try {
			PushResponse pushResponse = client.getAcsResponse(pushRequest);
			Tool.info("--- RequestId : "+pushResponse.getRequestId()+", MessageID : "+pushResponse.getMessageId(), Alipush.class);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 方法名：pushSilent
	 * 详述：静默推送
	 * @param token
	 * @param jsonParam
	 */
	public static void pushSilent(String token,String jsonParam) {
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SERECT);
        DefaultAcsClient client = new DefaultAcsClient(profile);
		PushRequest pushRequest = new PushRequest();
		// 推送目标
		pushRequest.setAppKey(APP_KEY);
		pushRequest.setTarget("DEVICE"); //推送目标: DEVICE:推送给设备; ACCOUNT:推送给指定帐号,TAG:推送给自定义标签; ALIAS: 按别名推送; ALL: 全推
		pushRequest.setTargetValue(token); //根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
		pushRequest.setDeviceType("ALL"); // 设备类型deviceType, iOS设备: "iOS"; Android设备: "ANDROID"; 全部: "ALL", 这是默认值.
		// 推送配置
		pushRequest.setPushType("NOTICE"); // MESSAGE:表示消息(默认), NOTICE:表示通知
		pushRequest.setTitle("silent title"); // 消息的标题
		pushRequest.setBody(""); // 消息的内容
		// 推送配置: iOS
		pushRequest.setIOSApnsEnv("DEV");//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
		pushRequest.setIOSSilentNotification(true);
		// 推送配置: Android
		
		if(Tool.isNotBlank(jsonParam)) {
       	 	pushRequest.setIOSExtParameters(jsonParam);
            pushRequest.setAndroidExtParameters(jsonParam);
		}
		try {
			PushResponse pushResponse = client.getAcsResponse(pushRequest);
			System.out.printf("RequestId: %s, message: %s\n",pushResponse.getRequestId(), pushResponse.getMessageId());
		} catch (ClientException e) {
			e.printStackTrace();
		}
	}
	
	

	
	
}
