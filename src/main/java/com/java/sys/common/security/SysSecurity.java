package com.java.sys.common.security;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.java.sys.common.utils.Tool;

public class SysSecurity {

    /**
     * 加密算法RSA
     */
    public static final String  KEY_ALGORITHM       = "RSA";
    /**
     * RSA最大加密明文大小
     */
    private static final int    MAX_ENCRYPT_BLOCK   = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int    MAX_DECRYPT_BLOCK   = 128;

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiPu8VpggQt3zlQV0P4clOuI34C6FhEuZAiC8b3Y4Oq3Umx+m0JoemJ0s/bd4nxVMUru02XWYEEkCumo0OBX6kHj0aA3G3NvKN19nZDJj+AUpu8sRAiJh/EOlrYC1qiBq7TfbUpBxnb24cOq4fwdAvng+O+lccICK712FAYCnILQIDAQAB";
    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKI+7xWmCBC3fOVBXQ/hyU64jfgLoWES5kCILxvdjg6rdSbH6bQmh6YnSz9t3ifFUxSu7TZdZgQSQK6ajQ4FfqQePRoDcbc28o3X2dkMmP4BSm7yxECImH8Q6WtgLWqIGrtN9tSkHGdvbhw6rh/B0C+eD476VxwgIrvXYUBgKcgtAgMBAAECgYEAhNe6sqMLH4TdQmjOZd6dEVOTCnGbZPwwgs3+pHY3hlNkzKgK9nJFxwmNVxYCO9YZPGot18GJcfPv+gaZT8237iLhhpmz73XJRjipraukfxCe0C9edJFrsswRMuFonpr972Wwby+bnhteHmO4be33NNECb9AgTTW+ZoBVkNXZ5YECQQDR6vzpYu3FuZl6f3j29AIkiTcN1IeAbxahN38aRI8azyO0sQquvh7D+JZmOPCcgOLq7BQd3MyG5wE7f2Ue7KwpAkEAxdzcJYO944be8bSle8k6lUxx5YPxD30HdQAI//QKLrM4n7RVTZ1E4Ic0VhNuvQPerz/Lm9e8SUbkMnKdivZ8ZQJBAKRvR+hmR01wgspTWmVQZPFLzyV1SyOPYZGuEFF6TQM6KYUcffq+EQmADAhNKCXgTGCrx2JRGKHACNYbiBG+YWkCQF8Kj9SVS4kXJ9ACbdrf9fvxFGSi1BAUhph0w93uY1W7UN+lx7PYb1VKnKEcBH1WdojHpA3futmX69UvqO0vKOECQBwMJf9QPF7UdvPnlah5yaxWJbWNM8tR2OarQ64KfTnV7vwfOaTMzsINeoWhInB9AtCwyELzXAFym7ir0LKEOMo=";

    
    
    /**
     * 方法名：signToMap
     * 详述：把签名字符串解析到map中
     * @param sign
     * @return
     * @throws Exception
     */
    public static Map<String,String> signToMap(String sign) {
    	try {
	    	Map<String,String> map = new HashMap<String,String>();
	    	byte[] decryptByPrivateKey = RSADecrypt(Base64.decodeBase64(sign));
	        String string = new String(decryptByPrivateKey, "utf-8");
	        String[] str = string.split("&");
	    	for (int i = 0; i < str.length; i++) {
	    		String key = str[i].substring(0, str[i].indexOf("="));
	    		String value = str[i].substring(str[i].indexOf("=")+1);
				map.put(key,value);
			}
	        return map;
    	}catch(Exception e) {
    		Tool.error(e.getMessage(), SysSecurity.class);
    		return null;
    	}
    }
    
    /**
	 * 方法名：getParam
	 * 详述：从加密字符串中获取参数
	 * @param key 参数名
	 * @param sign 加密字符串
	 * @return String
	 */
	public static String getParam(String key,String sign){
		if(Tool.isNotBlank(key,sign)) {
			try {
				Map<String,String> map = SysSecurity.signToMap(sign);
				return map.get(key);
			} catch (Exception e) {
				Tool.error(e.getMessage(), SysSecurity.class);
				return null;
			}
		}
		return null;
	}

	
	/**
	 * 方法名：RSAEncrypt
	 * 详述：RSA加密-publicKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] RSAEncrypt(byte[] data) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(PUBLIC_KEY);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;

    }
	
    
	/**
	 * 方法名：RSADecrypt
	 * 详述：RSA解密-privates
	 * @param encryptedData
	 * @return
	 * @throws Exception
	 */
    public static byte[] RSADecrypt(byte[] encryptedData) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(PRIVATE_KEY);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;

    }
    
    
    /**
     * 方法名：BASE64Encode
     * 详述：把RSA加密后的字符串再进行BASE64加密
     * @param str
     * @return
     */
	public static String BASE64Encode(String str){
		String encryptStr = null;
		try {
			encryptStr = Base64.encodeBase64String((SysSecurity.RSAEncrypt(str.getBytes("utf-8"))));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return encryptStr;
	}
	
	/**
	 * 方法名：BASE64Decode
	 * 详述：把BASE64解密后的字符串再进行RSA解密
	 * @param str
	 * @return
	 */
	public static String BASE64Decode(String str){
		String decodeStr = null;
		try {
			byte[] decryptByPrivateKey = SysSecurity.RSADecrypt(Base64.decodeBase64(str));
			decodeStr = new String(decryptByPrivateKey, "utf-8");
		} catch (Exception e) {
			Tool.error(e.getMessage(), SysSecurity.class);
			return null;
		}
        return decodeStr;
	}
	
	
	



}
