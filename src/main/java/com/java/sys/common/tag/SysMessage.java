package com.java.sys.common.tag;

public class SysMessage {
	private String message;
	private String type;
	
	public SysMessage() {
		super();
	}
	public SysMessage(String message, String type) {
		super();
		this.message = message;
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
