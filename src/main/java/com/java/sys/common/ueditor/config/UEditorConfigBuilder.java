package com.java.sys.common.ueditor.config;

import com.java.common.constance.MyConstance;
import com.java.sys.common.utils.SysConfig;
import com.java.sys.common.utils.Tool;

/**
 * ueditor配置类生产工厂
 * @author liqihua
 */
public class UEditorConfigBuilder {
	
	public static UEditorConfig build() {
		String uploadType = Tool.getUploadType();
		Tool.info("---uploadType:"+uploadType,UEditorConfigBuilder.class);
		if(uploadType.equals(MyConstance.UPLOAD_TYPE_LOCAL)){
			String urlPrefix = Tool.getServerUrl();
			return new UEditorConfig(urlPrefix);
		}else{
			return new UEditorConfig("");
		}
	}
	
	
	
	
}
