package com.java.common.im.rongyun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import org.apache.shiro.crypto.hash.Sha1Hash;

import com.java.common.constance.MyConstance;
import com.java.sys.common.utils.Tool;


public class RYTool {
	public static final String APP_KEY = "cpj2xarlc1ykn";
	public static final String APP_SERECT = "Zy1Srbo8o45gOZ";
	
	/**
	 * 获取token的url
	 */
	public static final String URL_GET_TOKEN = "http://api.cn.ronghub.com/user/getToken.json";
	/**
	 * 刷新用户信息的url
	 */
	public static final String URL_REFRESH = "http://api.cn.ronghub.com/user/refresh.json";
	/**
	 * 创建群组方法url
	 */
	public static final String URL_GROUP_CREATE = "http://api.cn.ronghub.com/group/create.json";
	/**
	 * 加入群组方法url
	 */
	public static final String URL_GROUP_JOIN = "http://api.cn.ronghub.com/group/join.json";
	/**
	 * 退出群组方法url
	 */
	public static final String URL_GROUP_QUIT = "http://api.cn.ronghub.com/group/quit.json";
	/**
	 * 解散群组方法url
	 */
	public static final String URL_GROUP_DISMISS = "http://api.cn.ronghub.com/group/dismiss.json";
	/**
	 * 刷新群组信息方法url
	 */
	public static final String URL_GROUP_REFRESH = "http://api.cn.ronghub.com/group/refresh.json";
	
	
	
	
	
	
	/**
	 * 方法名：makeToken
	 * 详述：获取融云token
	 * @param userId
	 * @param name
	 * @param avatar
	 * @return
	 */
	public static String makeToken(String userId,String name,String avatar){
		if(Tool.isNotBlank(userId,name,avatar)) {
			String result = post("userId="+userId+"&name="+name+"&portraitUri="+avatar, URL_GET_TOKEN);
			Tool.info("----- getToken() : "+result, RYTool.class);
			return result;
		}
		return null;
	}
	
	/**
	 * 方法名：refreshInfo
	 * 详述：刷新用户信息
	 * @param userId
	 * @param name
	 * @param avatar
	 * @return
	 */
	public static String refreshInfo(String userId,String name,String avatar){
		if(Tool.isNotBlank(userId,name,avatar)) {
			String result = post("userId="+userId+"&name="+name+"&portraitUri="+avatar, URL_REFRESH);
			Tool.info("----- refreshInfo() : "+result, RYTool.class);
			return result;
		}
		return null;
	}
	
	
	/**
	 * 方法名：createGroup
	 * 详述：创建群组方法
	 * @param groupId
	 * @param groupName
	 */
	public static String groupCreate(String groupId,String groupName) {
		if(Tool.isNotBlank(groupId,groupName)) {
			String result = post("userId=mainId&groupId="+groupId+"&groupName="+groupName, URL_GROUP_CREATE);
			Tool.info("----- groupCreate() : "+result, RYTool.class);
			return result;
		}
		return null;
	}
	
	
	/**
	 * 方法名：groupUpdate
	 * 详述：刷新群组信息方法
	 * @param groupId
	 * @param groupName
	 * @return
	 */
	public static String groupUpdate(String groupId,String groupName) {
		if(Tool.isNotBlank(groupId,groupName)) {
			String result = post("groupId="+groupId+"&groupName="+groupName, URL_GROUP_REFRESH);
			Tool.info("----- groupUpdate() : "+result, RYTool.class);
			return result;
		}
		return null;
	}
	
	/**
	 * 方法名：groupDismiss
	 * 详述：解散群组方法
	 * @param groupId
	 * @return
	 */
	public static String groupDismiss(String groupId) {
		if(Tool.isNotBlank(groupId)) {
			String result = post("userId=mainId&groupId="+groupId, URL_GROUP_DISMISS);
			Tool.info("----- groupDismiss() : "+result, RYTool.class);
			return result;
		}
		return null;
	}
	
	
	
	/**
	 * 方法名：groupJoin
	 * 详述：加入群组方法
	 * @param userId
	 * @param groupId
	 * @param groupName
	 * @return
	 */
	public static String groupJoin(String userId,String groupId,String groupName) {
		if(Tool.isNotBlank(userId,groupId,groupName)) {
			String result = post("userId="+userId+"&groupId="+groupId+"&groupName="+groupName, URL_GROUP_JOIN);
			Tool.info("----- groupJoin() : "+result, RYTool.class);
			return result;
		}
		return null;
	}
	
	
	/**
	 * 方法名：groupQuit
	 * 详述：退出群组方法
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public static String groupQuit(String userId,String groupId) {
		if(Tool.isNotBlank(userId,groupId)) {
			String result = post("userId="+userId+"&groupId="+groupId, URL_GROUP_QUIT);
			Tool.info("----- groupQuit() : "+result, RYTool.class);
			return result;
		}
		return null;
	}
	
	
	
	
	
	
	/**
	 * 方法名：post
	 * 详述：发起post请求，根据融云规则在header添加参数
	 * @param data
	 * @param postUrl
	 * @return
	 */
	public static String post(String data, String postUrl) {
        try {
        	String nonce = Tool.MD5(new Date().getTime()+"",true);
        	String timestamp = System.currentTimeMillis()/1000+"";
        	String signature = new Sha1Hash(APP_SERECT+nonce+timestamp).toString();
            //发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("App-Key", APP_KEY);
            conn.setRequestProperty("Nonce", nonce);
            conn.setRequestProperty("Timestamp", timestamp);
            conn.setRequestProperty("Signature", signature);
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Length", "" + data.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(data);
            out.flush();
            out.close();

            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("---------------- post() : connect failed!");
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
        return "";
    }


	
	
}
