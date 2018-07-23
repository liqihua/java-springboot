package com.java.common.sms;

import com.java.common.pay.alipay.sign.Base64;
import com.java.sys.common.utils.Tool;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * 云之讯短信下发工具类
 */
public class SMSTool {
    
    public static final String accountsid = "bd3e713ff8b41636e2e56e44c210d7c1";
    public static final String authtoken = "85eea8403986e53581ccc613ec7eb8a1";
    public static final String url = "https://open.ucpaas.com/ol/sms/sendsms";
    public static final String appId = "b98f81280d844479b9c4b1c329ddcf88";
    public static final String version = "2014-06-30";
    
    /**
     * 尊敬的用户，您本次的验证码为：{1}，如非本人操作请忽略本信息。
     */
    public static final String TEMPLATE_VCODE = "265518";




    /**
     * 短信下发接口
     * @param templeteId
     * @param phone
     * @param param
     * @return
     */
    public static String sendSMS(String templeteId,String phone,String ... param) {
        String content = "";
        if(param != null && param.length > 0){
        	for(String str : param){
        		content += (str + ",");
        	}
        	content = content.substring(0,content.length()-1);
        }
        JSONObject jsonParam = new JSONObject();
        jsonParam.element("sid",accountsid);
        jsonParam.element("token",authtoken);
        jsonParam.element("appid",appId);
        jsonParam.element("templateid",TEMPLATE_VCODE);
        jsonParam.element("mobile",phone);
        jsonParam.element("param",content);
        String result = post(url,jsonParam.toString());
        Tool.info("--- sendSms() \nphone : "+phone + " content : "+content+" \nresult :\n"+result,SMSTool.class);
        return result;
    }
    
    
    public static String post(String postUrl,String data) {
        try { 
            //发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Authorization", getAuthorization());
            conn.setRequestProperty("Content-Length", data.length()+"");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(data);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Tool.info("---------------- post() : connect failed!",SMSTool.class);
                return "";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    /**
     * 方法名：getAuthorization
     * 详述：Authorization是包头验证信息，使用Base64编码（账户Id + 冒号 + 时间戳）
     * @return
     */
    private static String getAuthorization() {
    	String src = accountsid + ":" + Tool.formatDate(new Date(), "yyyyMMddHHmmss");
        String authorization = Base64.encode(src.getBytes());
        return authorization;
    }
    
    
    
}
