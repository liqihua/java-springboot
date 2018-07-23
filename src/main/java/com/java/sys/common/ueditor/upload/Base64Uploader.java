package com.java.sys.common.ueditor.upload;


import com.java.common.qiniu.QiniuTool;
import com.java.sys.common.ueditor.PathFormat;
import com.java.sys.common.ueditor.define.AppInfo;
import com.java.sys.common.ueditor.define.BaseState;
import com.java.sys.common.ueditor.define.FileType;
import com.java.sys.common.ueditor.define.State;
import com.java.sys.common.utils.Tool;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Base64Uploader {

	public static State save(String content, Map<String, Object> conf) {
		
		byte[] data = decode(content);

		long maxSize = ((Long) conf.get("maxSize")).longValue();

		if (!validSize(data, maxSize)) {
			return new BaseState(false, AppInfo.MAX_SIZE);
		}

		String suffix = FileType.getSuffix("JPG");

		String savePath = PathFormat.parse((String) conf.get("savePath"),
				(String) conf.get("filename"));
		
		savePath = savePath + suffix;
		String physicalPath = (String) conf.get("rootPath") + savePath;

		State storageState = StorageManager.saveBinaryFile(data, physicalPath);
		
		/*
		 * 上传到文件服务器 --- start
		 */
		String url = null;
		/*if(Tool.getUploadToQiniu()) {
			//如果使用七牛云存储
			url = QiniuTool.uploadImg(data);
		}else {
			//否则
			url = PathFormat.format(savePath);
			String uploadUrl = Tool.getUploadUEditor();
			String uploadKey = Tool.getUploadKey();
			if(Tool.isNotBlank(uploadUrl,uploadKey)) {
				File file = new File(physicalPath);
				if(file != null){
					Map<String,String> paramMap = new HashMap<String,String>();
					paramMap.put("savePath", savePath);
					paramMap.put("key", uploadKey);
					String result = Tool.fileUpload(uploadUrl, "file", file, paramMap);
					Tool.info("--- Base64Uploader save() result : "+result);
				}
			}
		}*/
		/*
		 * 上传到文件服务器 --- end
		 */
		
		if (storageState.isSuccess()) {
			storageState.putInfo("url", url);
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}

		return storageState;
	}

	private static byte[] decode(String content) {
		return Base64.decodeBase64(content);
	}

	private static boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
	
}