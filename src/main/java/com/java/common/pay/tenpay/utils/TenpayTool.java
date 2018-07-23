package com.java.common.pay.tenpay.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;

import com.java.common.pay.tenpay.conf.TenpayConfig;
import com.java.sys.common.utils.Tool;
import com.java.sys.common.utils.Tool;

public class TenpayTool {
	
	/**
	 * 方法名：refund
	 * 详述：微信支付退款接口
	 * @param appid 邮件中的APPID
	 * @param mch_id 微信支付商户号
	 * @param out_trade_no 订单号
	 * @param out_refund_no 退款单号
	 * @param total_fee 订单总额
	 * @param refund_fee 退款金额
	 * @return
	 */
	public static String refund(String appid,String mch_id,String out_trade_no,String out_refund_no,int total_fee,int refund_fee){
		try{
			StringBuilder sb = new StringBuilder();
			SortedMap<String, Object> sp = new TreeMap<String, Object>();
			sp.put("appid", appid);
			sp.put("mch_id", mch_id);
			sp.put("nonce_str", Tool.MD5(System.currentTimeMillis()/1000 + "", true));
			sp.put("out_trade_no", out_trade_no);
			sp.put("out_refund_no", out_refund_no);
			sp.put("total_fee",total_fee);
			sp.put("refund_fee",refund_fee);
			sp.put("sign", SignTool.signApp(sp));
			String reqXML = Tool.mapToXML(sp);
			Tool.info("--- refund() reqXML:\n"+reqXML,TenpayTool.class);
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File(TenpayConfig.path_cert));
	        try {
	            keyStore.load(instream, mch_id.toCharArray());
	        } finally {
	            instream.close();
	        }
	        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
	        try {
	            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
	            //HttpEntity reqEntity = MultipartEntityBuilder.create().addTextBody("xml", reqXML).build();
	            HttpEntity reqEntity = new StringEntity(reqXML,"UTF-8");
	            httpPost.setEntity(reqEntity);
	            Tool.info("executing request" + httpPost.getRequestLine(),TenpayTool.class);
	            CloseableHttpResponse response = httpclient.execute(httpPost);
	            try {
	                HttpEntity entity = response.getEntity();
	                Tool.info("----------------------------------------",TenpayTool.class);
	                Tool.info(response.getStatusLine().toString(),TenpayTool.class);
	                if (entity != null) {
	                    Tool.info("Response content length: " + entity.getContentLength(),TenpayTool.class);
	                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	                    String text;
	                    while ((text = bufferedReader.readLine()) != null) {
	                    	sb.append(text);
	                        Tool.info(text,TenpayTool.class);
	                    }
	                }
	                EntityUtils.consume(entity);
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
	        return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 方法名：transfer
	 * 详述：企业付款到用户微信余额
	 * @param mch_appid 商户appid
	 * @param mchid 商户号
	 * @param partner_trade_no 订单号
	 * @param openid 用户微信openid
	 * @param re_user_name 实名姓名
	 * @param amount 金额（分）
	 * @param desc 企业付款描述信息
	 * @return
	 */
	public static String transfer(String mch_appid,String mchid,String partner_trade_no,String openid,String re_user_name,int amount,String desc){
		try{
			StringBuilder sb = new StringBuilder();
			SortedMap<String, Object> sp = new TreeMap<String, Object>();
			sp.put("mch_appid", mch_appid);
			sp.put("mchid", mchid);
			sp.put("nonce_str", Tool.MD5(System.currentTimeMillis()/1000 + "", true));
			sp.put("partner_trade_no", partner_trade_no);
			sp.put("openid", openid);
			sp.put("check_name", "NO_CHECK");
			if(Tool.isNotBlank(re_user_name)) {
				sp.put("check_name", "FORCE_CHECK");
				sp.put("re_user_name", re_user_name);
			}
			sp.put("amount",amount);//企业付款金额，单位为分
			sp.put("desc",desc);
			sp.put("spbill_create_ip",Tool.getIp().replace("http://", ""));
			sp.put("sign", SignTool.signApp(sp));
			String reqXML = Tool.mapToXML(sp);
			Tool.info("--- transfer() reqXML:\n"+reqXML,TenpayTool.class);
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File(TenpayConfig.path_cert));
	        try {
	            keyStore.load(instream, mchid.toCharArray());
	        } finally {
	            instream.close();
	        }
	        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mchid.toCharArray()).build();
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
	        try {
	        	HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
	            HttpEntity reqEntity = new StringEntity(reqXML,"UTF-8");
	            httpPost.setEntity(reqEntity);
	            Tool.info("executing request" + httpPost.getRequestLine(),TenpayTool.class);
	            CloseableHttpResponse response = httpclient.execute(httpPost);
	            try {
	                HttpEntity entity = response.getEntity();
	                Tool.info("----------------------------------------",TenpayTool.class);
	                Tool.info(response.getStatusLine().toString(),TenpayTool.class);
	                if (entity != null) {
	                    Tool.info("Response content length: " + entity.getContentLength(),TenpayTool.class);
	                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	                    String text;
	                    while ((text = bufferedReader.readLine()) != null) {
	                    	sb.append(text);
	                        Tool.info(text,TenpayTool.class);
	                    }
	                }
	                EntityUtils.consume(entity);
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
	        return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 方法名：getTransferInfo
	 * 详述：企业付款结果查询
	 * @param appid 商户appid
	 * @param mch_id 商户号
	 * @param partner_trade_no 订单号
	 * @return
	 */
	public static String getTransferInfo(String appid,String mch_id,String partner_trade_no){
		try{
			StringBuilder sb = new StringBuilder();
			SortedMap<String, Object> sp = new TreeMap<String, Object>();
			sp.put("appid", appid);
			sp.put("mch_id", mch_id);
			sp.put("nonce_str", Tool.MD5(System.currentTimeMillis()/1000 + "", true));
			sp.put("partner_trade_no", partner_trade_no);
			sp.put("sign", SignTool.signApp(sp));
			String reqXML = Tool.mapToXML(sp);
			Tool.info("--- refund() reqXML:\n"+reqXML,TenpayTool.class);
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File(TenpayConfig.path_cert));
	        try {
	            keyStore.load(instream, mch_id.toCharArray());
	        } finally {
	            instream.close();
	        }
	        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, mch_id.toCharArray()).build();
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
	        try {
	            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo");
	            HttpEntity reqEntity = new StringEntity(reqXML, "UTF-8");
	            httpPost.setEntity(reqEntity);
	            Tool.info("executing request" + httpPost.getRequestLine(),TenpayTool.class);
	            CloseableHttpResponse response = httpclient.execute(httpPost);
	            try {
	                HttpEntity entity = response.getEntity();
	                Tool.info("----------------------------------------",TenpayTool.class);
	                Tool.info(response.getStatusLine().toString(),TenpayTool.class);
	                if (entity != null) {
	                    Tool.info("Response content length: " + entity.getContentLength(),TenpayTool.class);
	                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	                    String text;
	                    while ((text = bufferedReader.readLine()) != null) {
	                    	sb.append(text);
	                        Tool.info(text,TenpayTool.class);
	                    }
	                }
	                EntityUtils.consume(entity);
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
	        return sb.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
