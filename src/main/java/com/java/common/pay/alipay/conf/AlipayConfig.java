package com.java.common.pay.alipay.conf;

public class AlipayConfig {
	//开放平台——右上角主账号——密钥管理
	public static String app_id = "2017052307324097";
	
	//开放平台——右上角主账号——密钥管理——左上角账户账户管理
	public static String partner = "2088621821546946";
	
	// 收款支付宝账号
	public static String seller_email = "2021892878@qq.com";
	// 商户的安全校验码（合作伙伴密钥管理）
	public static String key = "x50bbtw45k03qc2pxfc5c9p01zn90xhd";
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	
	//服务器异步通知页面路径
	public static String notify_url_web = "http://www.ttocz.com/timerent/ajax/payAjaxController/alipayNotifyWeb";
	public static String notify_url_app = "http://www.ttocz.com/timerent/api/payApiController/alipayNotifyApp";
	
	//即时到帐支付成功后的跳转页面
	public static String return_url_web = "http://www.ttocz.com";
	//即时到帐商品展示页面
	public static String show_url_web = "http://www.ttocz.com";
	
	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "/usr/alipay/";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type_web = "MD5";
	public static String sign_type_app = "RSA";
	
	//https://docs.open.alipay.com/291/105971
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKiGJPttyve+53Lx2rPHFTDOh1GOwmOAsAInNofNVrLC2d0t1FIBVZguD0X5OdZRKoL677XNhu0jWNF58rp1qq67or/rsqyQG/A+nL9USRC58grCF/ER+JCaaZhRPwcK2O0hcnriP1DQTKNCUQtOwGCEcpe/scWa4XLy1tQcJdfxAgMBAAECgYBqx1xlZyLEqVRTU4Ukhg8aNrIs7gyjKWXcZ2Y409WMPMP4TdKLB0dpvj11M73+2rzZ8w+xnQFR/iSHzgILPQUg3DwUM7wMiU2MkR81w+7MrJ1AnTaVNY99WZ1INBGA/OP1zAFW4NBJ/NWFJ8OKldqfzxvLL7EdfEu2DkwuH+uiMQJBANp19NKdKVuWBekG+6w30BPOdhnH8wT7NsWcOzFohBY4YZngqKbNilRvhnRHXDjTnAumMk/s0sdBoiCT5c/NKjUCQQDFe3rvp2krGj7AzrE1xyI1w7hmJ7ISqDgx0LM8AFQANIsDDY198EzPuPk8x+hmGuVSB4juc4VI/ET7QcmoxE5NAkA5wtZomYVF4fTOGDqS3m71zbEMQAHRX1qsBRLhjXDfLykfNndhIxdgG9zBAd0waULpEZhb6ZIRfRkQ5AnvV5/BAkEAxVmJ2HdPlSBsN9gWpexz1pDZ9VSUEysCxV2P0FVvnKjlhs6DaKi8yhVLFZ+dMnSMWPZotArD1tUrxn/PHBXZPQJAWjKyHefENcdmSG4zekVHTRljASIERzj1np0/lFFLMSH8eXcwws9yhrNDP4+ZS1BOKqf+rimJCHPRDhH88eamiA==";
	
	//支付宝的公钥，查看地址：https://openhome.alipay.com/platform/keyManage.htm?keyType=partner
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";		
	
	

}
