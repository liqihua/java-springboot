package com.java.sys.common.ueditor.upload;


import net.sf.json.JSONObject;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.java.sys.common.ueditor.config.UEditorConfig;
import com.java.sys.common.ueditor.define.State;
import com.java.sys.common.utils.Tool;

public class Uploader {
	private HttpServletRequest request = null;
	private Map<String, Object> conf = null;

	public Uploader(HttpServletRequest request, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
		Tool.info("--- Uploader Uploader() ");
	}

	public final State doExec() {
		Tool.info("--- Uploader doExec() come !");
		String filedName = (String) this.conf.get("fieldName");
		State state = null;
		Tool.info("--- Uploader doExec() filedName : "+filedName);
		if ("true".equals(this.conf.get("isBase64"))) {
			Tool.info("--- Uploader doExec() true.equals(this.conf.get(isBase64)) ");
			state = Base64Uploader.save(this.request.getParameter(filedName),this.conf);
		} else {
			Tool.info("--- Uploader doExec() else ");
			state = BinaryUploader.save(this.request, this.conf);
		}

		return state;
	}
	
	
}
