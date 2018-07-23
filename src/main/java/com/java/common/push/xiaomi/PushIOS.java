package com.java.common.push.xiaomi;

import com.java.sys.common.utils.Tool;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

public class PushIOS {
	
	
	
	public static void sendMessage(String regId,String title,String content){
		try{
			//Constants.useOfficial();
			Constants.useSandbox();
			Sender sender = new Sender(Config.IOS_APP_SECRET);
			Message message = new Message.Builder()
	                .title("你收到一条消息")
	                .description(title).payload(content)	//设置要发送的消息内容payload，不允许全是空白字符，长度小于4K，中英文均以一个计算。
	                .restrictedPackageName(Config.IOS_PACKAGE_NAME)
	                .notifyType(1)     	// 使用默认提示音提示
	                .passThrough(1)
	                .build();			//根据前面的属性，生成message对象
			Result result =  sender.send(message, regId, 0); //根据regID，发送消息到指定设备上，不重试。
			Tool.info("---------------- PushIOS : result ="+result);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
