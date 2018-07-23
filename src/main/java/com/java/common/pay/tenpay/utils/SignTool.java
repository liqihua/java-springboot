package com.java.common.pay.tenpay.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import org.apache.shiro.crypto.hash.Sha1Hash;

import com.java.common.pay.tenpay.conf.TenpayConfig;



public class SignTool {
	
	/**
	 * 方法名：createSign
	 * 详述：扫码支付签名
	 * @param map
	 */
	public static String signScan(SortedMap<String,Object> map) throws Exception{
		StringBuffer sb = new StringBuffer();
		Set<Entry<String,Object>> mapEntrySet = map.entrySet();
		for(Entry<String,Object> entry : mapEntrySet){
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			if(value != null && !value.equals("") && !key.equals("sign") && !key.equals("key")){
				sb.append(key + "="+value+"&");
			}
		}
		sb.append("key=" + TenpayConfig.weixin_key_scan);
		return MD5Tool.encryptString(sb.toString()).toLowerCase();
	}
	
	
	/**
	 * 方法名：signPub
	 * 详述：公众号支付签名
	 * @param map
	 */
	public static String signPub(SortedMap<String,Object> map) throws Exception{
		StringBuffer sb = new StringBuffer();
		Set<Entry<String,Object>> mapEntrySet = map.entrySet();
		for(Entry<String,Object> entry : mapEntrySet){
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			if(value != null && !value.equals("") && !key.equals("sign") && !key.equals("key")){
				sb.append(key + "="+value+"&");
			}
		}
		sb.append("key=" + TenpayConfig.weixin_key_pub);
		return MD5Tool.encryptString(sb.toString()).toLowerCase();
	}
	
	
	
	/**
	 * 方法名：signApp
	 * 详述：APP支付签名
	 * @param map
	 */
	public static String signApp(SortedMap<String,Object> map) throws Exception{
		StringBuffer sb = new StringBuffer();
		Set<Entry<String,Object>> mapEntrySet = map.entrySet();
		for(Entry<String,Object> entry : mapEntrySet){
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			if(value != null && !value.equals("") && !key.equals("sign") && !key.equals("key")){
				sb.append(key + "="+value+"&");
			}
		}
		sb.append("key=" + TenpayConfig.weixin_key_app);
		return MD5Tool.encryptString(sb.toString()).toLowerCase();
	}
	
	
	
	
	
	/**
	 * 方法名：signSHA1
	 * 详述：签名（sha1算法）
	 * @param map
	 * @return
	 */
	public static String signSHA1(SortedMap<String,Object> map){
		StringBuffer sb = new StringBuffer();
		Set<Entry<String,Object>> mapEntrySet = map.entrySet();
		for(Entry<String,Object> entry : mapEntrySet){
			String key = entry.getKey();
			String value = String.valueOf(entry.getValue());
			if(value != null && !value.equals("") && !key.equals("sign") && !key.equals("key")){
				sb.append(key + "="+value+"&");
			}
		}
		String originStr = sb.toString();
		String str = originStr.substring(0, originStr.length()-1);
		String signStr = new Sha1Hash(str).toString();
		return signStr;
	}
	
	
	
}
