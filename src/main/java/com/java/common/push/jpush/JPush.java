package com.java.common.push.jpush;

import com.java.sys.common.utils.Tool;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 极光推送类
 */
public class JPush {
	protected static final String MASTER_SECRET = "b6c39115cade461da62a1655";
	protected static final String APP_KEY = "be9c7e7aa0aa7c0b609f59f5";
	
	/**
	 * 方法名：push
	 * 详述：推送函数
	 * @param tag 设备tag号
	 * @param content 推送内容
	 */
	public static void push(String alias,String content){
		Tool.info("---------- JPush() alias : "+alias);
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
	    PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                //.setAudience(Audience.tag(tag))//根据tag推
                .setAudience(Audience.alias(alias))//根据别名推
                .setNotification(Notification.alert(content))
                .build();
	    try {
	        PushResult result = jpushClient.sendPush(payload);
	        Tool.info("Got result - " + result);

	    } catch (APIConnectionException e) {
	        // Connection error, should retry later
	    	Tool.info(e.getMessage());
	    } catch (APIRequestException e) {
	        // Should review the error, and fix the request
	        Tool.info("Should review the error, and fix the request");
	        Tool.info("HTTP Status: " + e.getStatus());
	        Tool.info("Error Code: " + e.getErrorCode());
	        Tool.info("Error Message: " + e.getErrorMessage());
	        Tool.info(e.getMessage());
	    }
	}
	
	
	
}
