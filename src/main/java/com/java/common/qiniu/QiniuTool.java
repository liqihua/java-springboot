package com.java.common.qiniu;

import com.google.gson.Gson;
import com.java.sys.common.utils.Tool;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class QiniuTool {
	
	public static final String PREFFIX_IMG = "http://ozfs1leii.bkt.clouddn.com/";//图片访问前缀
	public static final String PREFFIX_VIDEO = "http://ozfsrldvq.bkt.clouddn.com/";//视频访问前缀
	public static final String PREFFIX_FILE = "http://ozfs1leii.bkt.clouddn.com/";//文件访问前缀

	protected static final String APP_KEY = "hJi-pUDYofUcsMDoi3Mp4yOtTGiZax24EEmrzqTY";//key
	protected static final String APP_SECRECT = "tIA8k-zn2hyilu4_X3mLDjJcE5VINeKl4gRvLnpc";//serect
	
	public static final String BUCKET_IMG = "coll-img";//图片文件夹名称
	public static final String BUCKET_VIDEO = "coll-video";//视频文件夹名称
	public static final String BUCKET_FILE = "coll-img";//通用文件夹名称

	
	
	/**
	 * 方法名：makeToken
	 * 详述：生成上传token
	 * @param bucket
	 * @return
	 */
	public static String makeToken(String bucket) {
		Auth auth = Auth.create(QiniuTool.APP_KEY, QiniuTool.APP_SECRECT);
		String token = auth.uploadToken(bucket);
		return token;
	}


	/**
	 * 七牛上传方法
	 * @param bucket
	 * @param data
	 * @return
	 */
	public static String upload(String bucket,byte[] data) {
		try {
			Configuration cfg = new Configuration(Zone.zone2());//Zone.zone2()->华南
			UploadManager uploadManager = new UploadManager(cfg);
			String token = makeToken(bucket);
			String key = Tool.genId()+Tool.random();
	        Response response = uploadManager.put(data, key, token);
	        Tool.info("--- upload : "+response.bodyString(), QiniuTool.class);
	        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
	        return putRet.key;
	    } catch (QiniuException ex) {
	    	Response r = ex.response;
	        System.err.println(r.toString());
	        try {
				System.err.println(r.bodyString());
			} catch (QiniuException e) {
				System.err.println("--- QiniuException : r.bodyString()");
			}
	    } catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 七牛上传方法
	 * @param bucket
	 * @param is
	 * @return
	 */
	public static String upload(String bucket,InputStream is) {
		try {
			Configuration cfg = new Configuration(Zone.zone2());//Zone.zone2()->华南
			UploadManager uploadManager = new UploadManager(cfg);
			String token = makeToken(bucket);
			String key = Tool.genId()+Tool.random();
	        Response response = uploadManager.put(is, key, token, null, null);
	        Tool.info("--- upload : "+response.bodyString(), QiniuTool.class);
	        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
	        return putRet.key;
	    } catch (QiniuException ex) {
	    	Response r = ex.response;
	        System.err.println(r.toString());
	        try {
				System.err.println(r.bodyString());
			} catch (QiniuException e) {
				System.err.println("--- QiniuException : r.bodyString()");
			}
	    } catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 上传图片封装-MultipartFile
	 * @param file
	 * @return
	 */
	public static String uploadImg(MultipartFile file) {
		try {
			String fileName = upload(QiniuTool.BUCKET_IMG, file.getBytes());
			if(Tool.isNotBlank(fileName)) {
				return QiniuTool.PREFFIX_IMG + fileName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传图片封装-byte[]
	 * @param data
	 * @return
	 */
	public static String uploadImg(byte[] data) {
		String fileName = upload(QiniuTool.BUCKET_IMG, data);
		if(Tool.isNotBlank(fileName)) {
			return QiniuTool.PREFFIX_IMG + fileName;
		}
		return null;
	}

	/**
	 * 上传图片封装-InputStream
	 * @param is
	 * @return
	 */
	public static String uploadImg(InputStream is) {
		String fileName = upload(QiniuTool.BUCKET_IMG, is);
		if(Tool.isNotBlank(fileName)) {
			return QiniuTool.PREFFIX_IMG + fileName;
		}
		return null;
	}


	/**
	 * 上传视频封装-MultipartFile
	 * @param file
	 * @return
	 */
	public static String uploadVideo(MultipartFile file) {
		try {
			String fileName = upload(QiniuTool.BUCKET_VIDEO, file.getBytes());
			if(Tool.isNotBlank(fileName)) {
				return QiniuTool.PREFFIX_VIDEO + fileName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传视频封装-byte[]
	 * @param data
	 * @return
	 */
	public static String uploadVideo(byte[] data) {
		String fileName = upload(QiniuTool.BUCKET_VIDEO, data);
		if(Tool.isNotBlank(fileName)) {
			return QiniuTool.PREFFIX_VIDEO + fileName;
		}
		return null;
	}

	/**
	 * 上传视频封装-InputStream
	 * @param is
	 * @return
	 */
	public static String uploadVideo(InputStream is) {
		String fileName = upload(QiniuTool.BUCKET_VIDEO, is);
		if(Tool.isNotBlank(fileName)) {
			return QiniuTool.PREFFIX_VIDEO + fileName;
		}
		return null;
	}


	/**
	 * 上传文件封装-MultipartFile
	 * @param file
	 * @return
	 */
	public static String uploadFile(MultipartFile file) {
		try {
			String fileName = upload(QiniuTool.BUCKET_FILE, file.getBytes());
			if(Tool.isNotBlank(fileName)) {
				return QiniuTool.PREFFIX_FILE + fileName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 上传文件封装-byte[]
	 * @param data
	 * @return
	 */
	public static String uploadFile(byte[] data) {
		String fileName = upload(QiniuTool.BUCKET_FILE, data);
		if(Tool.isNotBlank(fileName)) {
			return QiniuTool.PREFFIX_FILE + fileName;
		}
		return null;
	}

	/**
	 * 上传文件封装-InputStream
	 * @param is
	 * @return
	 */
	public static String uploadFile(InputStream is) {
		String fileName = upload(QiniuTool.BUCKET_FILE, is);
		if(Tool.isNotBlank(fileName)) {
			return QiniuTool.PREFFIX_FILE + fileName;
		}
		return null;
	}
	
	
	
	
}
