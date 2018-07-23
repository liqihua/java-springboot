package com.java.common.pay.tenpay.conf;

import com.java.common.weixin.WeixinConfig;

public class TenpayConfig {
	/*
	 * 微信的扫码支付和公众号jsapi支付，都是拥有微信公众号就可以申请
	 * 微信的APP支付需要拥有微信开放平台才可以申请
	 */
	
	
	/*
	 * 扫描支付配置
	 */
	public static final String weixin_appid_scan = "wxeda13e1afd39ec06";//微信支付申请成功后在邮件中的APPID，相当于公众号appid
	public static final String weixin_mch_id_scan = "1489328382";//微信支付商户号
	public static final String weixin_key_scan = "e9f2b6f55be24c9c804855043d1a09f7";//在商户平台设置，登录后——账户中心——账户设置——API安全中设置
	public static final String notify_url_scan = "http://xxx/ajax/payAjaxController/tenpayNotifyScan";//异步通知支付结果url
	
	/*
	 * app支付配置
	 */
	public static final String weixin_appid_app = "wx0c673f36974ae5c7";//微信支付申请成功后在邮件中的APPID，相当于开放平台appid
	public static final String weixin_mch_id_app = "1400044502";
	public static final String weixin_key_app = "e9f2b6f55be24c9c804855043d1a09f7";
	public static final String notify_url_app = "http://xxx/api/payApiController/tenpayNotifyAPP";
	
	/*
	 * 公众号jsapi支付配置
	 */
	public static final String weixin_appid_pub = "wx0c673f36974ae5c7";//微信支付申请成功后在邮件中的APPID，相当于公众号appid
	public static final String weixin_mch_id_pub = "1400044502";
	public static final String weixin_key_pub = "baa0a92192c051a5faf5b94fe7a28fb1";
	public static final String notify_url_pub = "http://xxx/api/payApiController/tenpayNotifyPub";
	
	/*
	 * 小程序支付配置
	 */
	public static final String weixin_appid_mini = WeixinConfig.MINI_APP_ID;
	public static final String weixin_mch_id_mini = "1494502722";
	public static final String weixin_key_mini = "e9f2b6f55be24c9c804855043d1a09f7";
	public static final String notify_url_mini = "http://quxiangba.cn:8080/api/redApiController/tenpayNotifyMini";
	
	
	
	//API双向证书的路径：登录微信商户平台——账户中心——账户设置——API安全——下载证书，java用apiclient_cert.p12这个文件（企业付款接口、退款接口等）
	public static final String path_cert = "/usr/piano/apiclient_cert.p12";//linux
	//public static final String path_cert = "F:/apiclient_cert.p12";//window
	
	
	
}
