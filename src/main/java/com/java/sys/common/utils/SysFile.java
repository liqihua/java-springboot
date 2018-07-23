package com.java.sys.common.utils;

import java.io.File;

/**
 * 文件上传结果类
 */
public class SysFile {
	public String path;//保存路径
	public File file;//目标文件对象
	
	
	public SysFile() {
		super();
	}
	
	
	public SysFile(String path, File file) {
		super();
		this.path = path;
		this.file = file;
	}


	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
