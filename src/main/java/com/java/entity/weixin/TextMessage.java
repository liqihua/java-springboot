package com.java.entity.weixin;

public class TextMessage {
	public String ToUserName;		//开发者微信号
	public String FromUserName;		//发送方帐号（一个OpenID）
	public String CreateTime;		//消息创建时间 （整型）
	public String MsgType;			//消息类型
	public String MsgId;			//消息id，64位整型
	public String Content;			//文本消息内容
	
	public TextMessage(String toUserName, String fromUserName,
			String createTime, String msgType, String content,String msgId) {
		super();
		ToUserName = toUserName;
		FromUserName = fromUserName;
		CreateTime = createTime;
		MsgType = msgType;
		MsgId = msgId;
		Content = content;
	}
	
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}
