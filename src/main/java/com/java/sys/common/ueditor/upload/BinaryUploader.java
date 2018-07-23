package com.java.sys.common.ueditor.upload;

import com.java.common.constance.MyConstance;
import com.java.common.qiniu.QiniuTool;
import com.java.sys.common.ueditor.PathFormat;
import com.java.sys.common.ueditor.define.AppInfo;
import com.java.sys.common.ueditor.define.BaseState;
import com.java.sys.common.ueditor.define.FileType;
import com.java.sys.common.ueditor.define.State;
import com.java.sys.common.utils.SysConfig;
import com.java.sys.common.utils.Tool;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
		Tool.info("--- BinaryUploader save() conf : "+JSONObject.fromObject(conf).toString());
		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;

		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());

        if ( isAjaxUpload ) {
            upload.setHeaderEncoding( "UTF-8" );
        }

		try {
			FileItemIterator iterator = upload.getItemIterator(request);

			while (iterator.hasNext()) {
				fileStream = iterator.next();

				if (!fileStream.isFormField())
					break;
				fileStream = null;
			}

			if (fileStream == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = (String) conf.get("savePath");
			String originFileName = fileStream.getName();
			String suffix = FileType.getSuffixByFilename(originFileName);
			Tool.info("--- BinaryUploader save() savePath : "+savePath);
			Tool.info("--- BinaryUploader save() originFileName : "+originFileName);
			Tool.info("--- BinaryUploader save() suffix : "+suffix);
			originFileName = originFileName.substring(0,originFileName.length() - suffix.length());
			savePath = savePath + suffix;
			Tool.info("--- BinaryUploader save() originFileName : "+originFileName);
			Tool.info("--- BinaryUploader save() savePath : "+savePath);
			long maxSize = ((Long) conf.get("maxSize")).longValue();
			Tool.info("--- BinaryUploader save() maxSize : "+maxSize);
			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);
			Tool.info("--- BinaryUploader save() savePath : "+savePath);
			String physicalPath = (String) conf.get("rootPath") + savePath;
			Tool.info("--- BinaryUploader save() physicalPath : "+physicalPath);
			InputStream is = fileStream.openStream();
			State storageState = new BaseState(true);
			String url = null;
			
			/*
			 * 上传到文件 --- start
			 */
			String uploadType = Tool.getUploadType();
			if(uploadType.equals(MyConstance.UPLOAD_TYPE_QINIU)) {
				url = QiniuTool.uploadImg(is);
			}else{
				storageState = StorageManager.saveFileByInputStream(is,physicalPath, maxSize);
				url = PathFormat.format(savePath);
				if(uploadType.equals(MyConstance.UPLOAD_TYPE_REMOTE)){
					String uploadUrl = SysConfig.getConfig("upload.prefix") + SysConfig.getConfig("upload.single");
					String uploadKey = SysConfig.getConfig("upload.key");
					if(Tool.isBlank(uploadUrl,uploadKey)){
						Tool.error("--- uploadUrl:"+uploadUrl+",uploadKey:"+uploadKey,BinaryUploader.class);
						return null;
					}
					if(Tool.isNotBlank(uploadUrl,uploadKey)) {
						File file = new File(physicalPath);
						if(file == null){
							Tool.error("--- file is NULL ",BinaryUploader.class);
							return null;
						}
						Map<String,String> paramMap = new HashMap<String,String>();
						paramMap.put("key", uploadKey);
						String result = Tool.uploadFileRemote(uploadUrl, "file", file, paramMap);
						url = SysConfig.getConfig("upload.prefix") + result;
						storageState = new BaseState(true);
						Tool.info("--- BinaryUploader save() result : "+result);
					}
				}
			}
			is.close();
			/*
			 * 上传到文件 --- end
			 */
			
			if (storageState.isSuccess()) {
				storageState.putInfo("url", url);
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}
			return storageState;
		} catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
