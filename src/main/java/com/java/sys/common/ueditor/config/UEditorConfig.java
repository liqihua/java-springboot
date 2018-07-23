package com.java.sys.common.ueditor.config;

/**
 * ueditor配置类
 * @author lqihua
 */
public class UEditorConfig {
	/*
	 * 图片访问路径前缀
	 */
	public static final String DEFAULT_URL_PREFIX = "http://localhost:8080/java";
	/*
	 * 上传保存路径,可以自定义保存路径和文件名格式
	 */
	/* {filename} 会替换成原文件名,配置这项需要注意中文乱码问题 */
    /* {rand:6} 会替换成随机数,后面的数字是随机数的位数 */
    /* {time} 会替换成时间戳 */
    /* {yyyy} 会替换成四位年份 */
    /* {yy} 会替换成两位年份 */
    /* {mm} 会替换成两位月份 */
    /* {dd} 会替换成两位日期 */
    /* {hh} 会替换成两位小时 */
    /* {ii} 会替换成两位分钟 */
    /* {ss} 会替换成两位秒 */
    /* 非法字符 \ : * ? " < > | */
	public static final String DEFAULT_SAVE_PATH = "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}";
	
	





	/* 上传图片配置项 */
	public String imageActionName = "uploadimage";/* 执行上传图片的action名称 */
	public String imageFieldName = "upfile";/* 提交的图片表单名称 */
	public Integer imageMaxSize = 2048000;/* 上传大小限制，单位B */
	public String[] imageAllowFiles = {".png",".jpg",".jpeg",".gif",".bmp"};/* 上传图片格式显示 */
	public boolean imageCompressEnable = true;/* 是否压缩图片,默认是true */
	public Integer imageCompressBorder = 1600;/* 图片压缩最长边限制 */
	public String imageInsertAlign = "none";/* 插入的图片浮动方式 */
	public String imagePathFormat;
	public String imageUrlPrefix;
	
	
	
	/* 涂鸦图片上传配置项 */
    public String scrawlActionName = "uploadscrawl"; /* 执行上传涂鸦的action名称 */
    public String scrawlFieldName = "upfile"; /* 提交的图片表单名称 */
    public Integer scrawlMaxSize = 2048000; /* 上传大小限制，单位B */
    public String scrawlInsertAlign = "none";/* 插入的图片浮动方式 */
    public String scrawlPathFormat;
    public String scrawlUrlPrefix;
    
	
    /* 截图工具上传 */
    public String snapscreenActionName = "uploadimage"; /* 执行上传截图的action名称 */
    public String snapscreenInsertAlign = "none"; /* 插入的图片浮动方式 */
    public String snapscreenPathFormat;
    public String snapscreenUrlPrefix;
    
    /* 抓取远程图片配置 */
    public String[] catcherLocalDomain = new String[] {"127.0.0.1", "localhost", "img.baidu.com"};
    public String catcherActionName = "catchimage"; /* 执行抓取远程图片的action名称 */
    public String catcherFieldName = "source"; /* 提交的图片列表表单名称 */
    public Integer catcherMaxSize = 2048000; /* 上传大小限制，单位B */
    public String[] catcherAllowFiles = {".png", ".jpg", ".jpeg", ".gif", ".bmp"}; /* 抓取图片格式显示 */
    public String catcherPathFormat; 
    public String catcherUrlPrefix;
    
    /* 上传视频配置 */
    public String videoActionName = "uploadvideo"; /* 执行上传视频的action名称 */
    public String videoFieldName = "upfile"; /* 提交的视频表单名称 */
    public Integer videoMaxSize = 102400000; /* 上传大小限制，单位B，默认100MB */
    public String[] videoAllowFiles = {".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid"}; /* 上传视频格式显示 */
    public String videoPathFormat;
    public String videoUrlPrefix;
    
    /* 上传文件配置 */
    public String fileActionName = "uploadfile"; /* controller里,执行上传视频的action名称 */
    public String fileFieldName = "upfile"; /* 提交的文件表单名称 */
    public Integer fileMaxSize = 51200000; /* 上传大小限制，单位B，默认50MB */
    public String[] fileAllowFiles = {
        ".png", ".jpg", ".jpeg", ".gif", ".bmp",
        ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",
        ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid",
        ".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso",
        ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt", ".md", ".xml"
    }; /* 上传文件格式显示 */
    public String filePathFormat;
    public String fileUrlPrefix;
    

    
    public UEditorConfig() {
    	imagePathFormat = DEFAULT_SAVE_PATH;
		imageUrlPrefix = DEFAULT_URL_PREFIX;
		
		scrawlPathFormat = DEFAULT_SAVE_PATH;
		scrawlUrlPrefix = DEFAULT_URL_PREFIX;
		
		snapscreenPathFormat = DEFAULT_SAVE_PATH;
	    snapscreenUrlPrefix = DEFAULT_URL_PREFIX;
	    
	    catcherPathFormat = DEFAULT_SAVE_PATH; 
	    catcherUrlPrefix = DEFAULT_URL_PREFIX;
	    
	    videoPathFormat = DEFAULT_SAVE_PATH;
	    videoUrlPrefix = DEFAULT_URL_PREFIX;
	    
	    filePathFormat = DEFAULT_SAVE_PATH;
	    fileUrlPrefix = DEFAULT_URL_PREFIX;
	    
	}
	
	public UEditorConfig(String urlPrefix) {
		imagePathFormat = DEFAULT_SAVE_PATH;
		imageUrlPrefix = urlPrefix;
		
		scrawlPathFormat = DEFAULT_SAVE_PATH;
		scrawlUrlPrefix = urlPrefix;
		
		snapscreenPathFormat = DEFAULT_SAVE_PATH;
	    snapscreenUrlPrefix = urlPrefix;
	    
	    catcherPathFormat = DEFAULT_SAVE_PATH; 
	    catcherUrlPrefix = urlPrefix;
	    
	    videoPathFormat = DEFAULT_SAVE_PATH;
	    videoUrlPrefix = urlPrefix;
	    
	    filePathFormat = DEFAULT_SAVE_PATH;
	    fileUrlPrefix = urlPrefix;
	}

	public UEditorConfig(String urlPrefix, String savePath) {
		imagePathFormat = savePath;
		imageUrlPrefix = urlPrefix;
		
		scrawlPathFormat = savePath;
		scrawlUrlPrefix = urlPrefix;
		
		snapscreenPathFormat = savePath;
	    snapscreenUrlPrefix = urlPrefix;
	    
	    catcherPathFormat = savePath; 
	    catcherUrlPrefix = urlPrefix;
	    
	    videoPathFormat = savePath;
	    videoUrlPrefix = urlPrefix;
	    
	    filePathFormat = savePath;
	    fileUrlPrefix = urlPrefix;
	}
    
	
	public String getImageActionName() {
		return imageActionName;
	}
	public void setImageActionName(String imageActionName) {
		this.imageActionName = imageActionName;
	}
	public String getImageFieldName() {
		return imageFieldName;
	}
	public void setImageFieldName(String imageFieldName) {
		this.imageFieldName = imageFieldName;
	}
	public Integer getImageMaxSize() {
		return imageMaxSize;
	}
	public void setImageMaxSize(Integer imageMaxSize) {
		this.imageMaxSize = imageMaxSize;
	}
	
	public boolean isImageCompressEnable() {
		return imageCompressEnable;
	}
	public void setImageCompressEnable(boolean imageCompressEnable) {
		this.imageCompressEnable = imageCompressEnable;
	}
	public Integer getImageCompressBorder() {
		return imageCompressBorder;
	}
	public void setImageCompressBorder(Integer imageCompressBorder) {
		this.imageCompressBorder = imageCompressBorder;
	}
	public String getImageInsertAlign() {
		return imageInsertAlign;
	}
	public void setImageInsertAlign(String imageInsertAlign) {
		this.imageInsertAlign = imageInsertAlign;
	}
	public String getImageUrlPrefix() {
		return imageUrlPrefix;
	}
	public void setImageUrlPrefix(String imageUrlPrefix) {
		this.imageUrlPrefix = imageUrlPrefix;
	}
	public String getImagePathFormat() {
		return imagePathFormat;
	}
	public void setImagePathFormat(String imagePathFormat) {
		this.imagePathFormat = imagePathFormat;
	}
	public String[] getImageAllowFiles() {
		return imageAllowFiles;
	}
	public void setImageAllowFiles(String[] imageAllowFiles) {
		this.imageAllowFiles = imageAllowFiles;
	}
	
	public String getScrawlActionName() {
		return scrawlActionName;
	}
	public void setScrawlActionName(String scrawlActionName) {
		this.scrawlActionName = scrawlActionName;
	}
	public String getScrawlFieldName() {
		return scrawlFieldName;
	}
	public void setScrawlFieldName(String scrawlFieldName) {
		this.scrawlFieldName = scrawlFieldName;
	}
	public Integer getScrawlMaxSize() {
		return scrawlMaxSize;
	}
	public void setScrawlMaxSize(Integer scrawlMaxSize) {
		this.scrawlMaxSize = scrawlMaxSize;
	}
	public String getScrawlInsertAlign() {
		return scrawlInsertAlign;
	}
	public void setScrawlInsertAlign(String scrawlInsertAlign) {
		this.scrawlInsertAlign = scrawlInsertAlign;
	}
	public String getScrawlPathFormat() {
		return scrawlPathFormat;
	}
	public void setScrawlPathFormat(String scrawlPathFormat) {
		this.scrawlPathFormat = scrawlPathFormat;
	}
	public String getScrawlUrlPrefix() {
		return scrawlUrlPrefix;
	}
	public void setScrawlUrlPrefix(String scrawlUrlPrefix) {
		this.scrawlUrlPrefix = scrawlUrlPrefix;
	}
	public String getSnapscreenActionName() {
		return snapscreenActionName;
	}
	public void setSnapscreenActionName(String snapscreenActionName) {
		this.snapscreenActionName = snapscreenActionName;
	}
	public String getSnapscreenInsertAlign() {
		return snapscreenInsertAlign;
	}
	public void setSnapscreenInsertAlign(String snapscreenInsertAlign) {
		this.snapscreenInsertAlign = snapscreenInsertAlign;
	}
	public String getSnapscreenPathFormat() {
		return snapscreenPathFormat;
	}
	public void setSnapscreenPathFormat(String snapscreenPathFormat) {
		this.snapscreenPathFormat = snapscreenPathFormat;
	}
	public String getSnapscreenUrlPrefix() {
		return snapscreenUrlPrefix;
	}
	public void setSnapscreenUrlPrefix(String snapscreenUrlPrefix) {
		this.snapscreenUrlPrefix = snapscreenUrlPrefix;
	}

	public String[] getCatcherLocalDomain() {
		return catcherLocalDomain;
	}

	public void setCatcherLocalDomain(String[] catcherLocalDomain) {
		this.catcherLocalDomain = catcherLocalDomain;
	}

	public String getCatcherActionName() {
		return catcherActionName;
	}

	public void setCatcherActionName(String catcherActionName) {
		this.catcherActionName = catcherActionName;
	}

	public String getCatcherFieldName() {
		return catcherFieldName;
	}

	public void setCatcherFieldName(String catcherFieldName) {
		this.catcherFieldName = catcherFieldName;
	}

	public Integer getCatcherMaxSize() {
		return catcherMaxSize;
	}

	public void setCatcherMaxSize(Integer catcherMaxSize) {
		this.catcherMaxSize = catcherMaxSize;
	}

	public String[] getCatcherAllowFiles() {
		return catcherAllowFiles;
	}

	public void setCatcherAllowFiles(String[] catcherAllowFiles) {
		this.catcherAllowFiles = catcherAllowFiles;
	}

	public String getCatcherPathFormat() {
		return catcherPathFormat;
	}

	public void setCatcherPathFormat(String catcherPathFormat) {
		this.catcherPathFormat = catcherPathFormat;
	}

	public String getCatcherUrlPrefix() {
		return catcherUrlPrefix;
	}

	public void setCatcherUrlPrefix(String catcherUrlPrefix) {
		this.catcherUrlPrefix = catcherUrlPrefix;
	}

	public String getVideoActionName() {
		return videoActionName;
	}

	public void setVideoActionName(String videoActionName) {
		this.videoActionName = videoActionName;
	}

	public String getVideoFieldName() {
		return videoFieldName;
	}

	public void setVideoFieldName(String videoFieldName) {
		this.videoFieldName = videoFieldName;
	}

	public Integer getVideoMaxSize() {
		return videoMaxSize;
	}

	public void setVideoMaxSize(Integer videoMaxSize) {
		this.videoMaxSize = videoMaxSize;
	}

	public String[] getVideoAllowFiles() {
		return videoAllowFiles;
	}

	public void setVideoAllowFiles(String[] videoAllowFiles) {
		this.videoAllowFiles = videoAllowFiles;
	}

	public String getVideoPathFormat() {
		return videoPathFormat;
	}

	public void setVideoPathFormat(String videoPathFormat) {
		this.videoPathFormat = videoPathFormat;
	}

	public String getVideoUrlPrefix() {
		return videoUrlPrefix;
	}

	public void setVideoUrlPrefix(String videoUrlPrefix) {
		this.videoUrlPrefix = videoUrlPrefix;
	}

	public String getFileActionName() {
		return fileActionName;
	}

	public void setFileActionName(String fileActionName) {
		this.fileActionName = fileActionName;
	}

	public String getFileFieldName() {
		return fileFieldName;
	}

	public void setFileFieldName(String fileFieldName) {
		this.fileFieldName = fileFieldName;
	}

	public Integer getFileMaxSize() {
		return fileMaxSize;
	}

	public void setFileMaxSize(Integer fileMaxSize) {
		this.fileMaxSize = fileMaxSize;
	}

	public String[] getFileAllowFiles() {
		return fileAllowFiles;
	}

	public void setFileAllowFiles(String[] fileAllowFiles) {
		this.fileAllowFiles = fileAllowFiles;
	}

	public String getFilePathFormat() {
		return filePathFormat;
	}

	public void setFilePathFormat(String filePathFormat) {
		this.filePathFormat = filePathFormat;
	}

	public String getFileUrlPrefix() {
		return fileUrlPrefix;
	}

	public void setFileUrlPrefix(String fileUrlPrefix) {
		this.fileUrlPrefix = fileUrlPrefix;
	}
	
}
