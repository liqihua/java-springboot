package com.java.sys.common.utils;

import com.java.common.constance.MyConstance;
import com.java.common.qiniu.QiniuTool;
import com.java.common.utils.JVMUtils;
import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.cache.CacheUtil;
import com.java.sys.common.security.SysSecurity;
import com.thoughtworks.xstream.XStream;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Tool {
	public static final String PATH_IMG = "/uploads/images/";		//上传图片默认保存路径
	public static final String PATH_FILE = "/uploads/files/";		//上传图片默认保存路径
	
	
	/**
	 * 方法名：ceil
	 * 详述：向上取整
	 * @param num
	 * @param size
	 * @return int
	 */
	public static int ceil(int num, int size) {
		if (size == 0) {
			return 0;
		}
		int n1 = num/size;
		double n2 = num%size;
		if (n2 > 0) {
			n1 += 1;
		}
		return n1;
	}


	/**
	 * 获取项目服务url，含http，到端口，没有/，如：http://127.0.0.1:8080
	 * @return
	 */
	public static String getServerUrl(){
		return Tool.getIp()+":"+CacheUtil.get(MyConstance.KEY_CONFIG_SERVER_PORT);
	}


	/**
	 * 对字符串以split为分割符，转换成List
	 * @param str
	 * @param split
	 * @return
	 */
	public static List<String> strToList(String str,String split){
		if(Tool.isBlank(str,split)){
			return null;
		}
		List<String> list = null;
		String[] strArr = str.split(split);
		if(strArr != null && strArr.length>0){
			list = new ArrayList<String>();
			for(String s : strArr){
				if(!isBlank(s)){
					list.add(s);
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 方法名：toUrl
	 * 详述：转换绝对地址
	 * @param path
	 * @return
	 */
	public static String toUrl(String path){
		if(isBlank(path)){
			return null;
		}
		if(path.contains("http")){
			return path;
		}
        String uploadType = getUploadType();
        if(uploadType.equals(MyConstance.UPLOAD_TYPE_REMOTE)){
		    return SysConfig.getConfig("upload.prefix")+path;
        }else if(uploadType.equals(MyConstance.UPLOAD_TYPE_QINIU)){
		    return path;
        }else{
            return getServerUrl()+path;
        }
	}

    /**
     * 获取项目文件上传方式
     * @return
     */
	public static String getUploadType(){
        String uploadType = SysConfig.getConfig("upload.type");
        if(isBlank(uploadType)){
            uploadType = MyConstance.UPLOAD_TYPE_LOCAL;
        }
        return uploadType.toLowerCase();
    }
	
	
	/**
	 * 方法名：isDouble
	 * 详述：判断一个字符串是否为小数
	 * 创建时间：2016年3月21日
	 * @param str
	 * @return boolean
	 */
	public static boolean isDouble(String str){
		if(isBlank(str)){
			return false;
		}
		Pattern pattern = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");
		Matcher m = pattern.matcher(str);
		return m.matches();
	}

	/**
	 * 判断多个个字符串是否为小数
	 * @param strs
	 * @return
	 */
	public static boolean isDouble(String ... strs){
		for(String s : strs){
			if(!isDouble(s)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断一个字符串是否是一个正确的手机号码
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if(isBlank(phone) || phone.length()!=11){
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}

	/**
	 * 判断一个字符是否为数字字符
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		if(isBlank(str)){
			return false;
		}
		if(StringUtils.isNumeric(str)){
			return true;
		}
		return false;
	}

	/**
	 * 判断多个字符是否为数字字符
	 * @param strs
	 * @return
	 */
	public static boolean isNumber(String ... strs){
		for(String s : strs){
			if(!isNumber(s)){
				return false;
			}
		}
		return true;
	}


	/**
	 * 判断String参数是否为空的方法，参数数量可变，如果其中有一个参数是空，就返回true
	 * @param strs
	 * @return
	 */
	public static boolean isBlank(String ... strs){
		for(String s : strs){
			if(StringUtils.isBlank(s)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断多个参数是否为空
	 * @param strs
	 * @return
	 */
	public static boolean isNotBlank(String ... strs){
		return !isBlank(strs);
	}


	/**
	 * 判断一个String的值在不在可变参数里面,如果不在里面，就返回true
	 * @param str
	 * @param strs
	 * @return
	 */
	public static boolean notIn(String str,String ... strs){
		for(String s : strs){
			if(s.equals(str)){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 判断str在不在strs里面，如果在，返回true
	 * @param str
	 * @param strs
	 * @return
	 */
	public static boolean in(String str,String ... strs){
		for(String s : strs){
			if(s.equals(str)){
				return true;
			}
		}
		return false;
	}
	

	/**
	 * 方法名：random
	 * 详述：随机生成6位数的字符串
	 * 创建时间：2016年3月21日
	 * @return String
	 */
	public static String random(){
		int num = (int)((Math.random()*9+1)*100000);
		return String.valueOf(num);
	}
	
	/**
	 * 方法名：toLength
	 * 详述：返回将number补0，长度为length位后的字符
	 * 创建时间：2016年4月14日
	 * @param number 要补0的数字
	 * @param length 补0后的长度
	 * @return String
	 */
	public static String toLength(int number,int length){
		return String.format("%0"+length+"d", number);
	}
	
	
	/**
	 * 把Date对象格式化成String对象
	 * @param date
	 * @param format 传null默认为 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatDate(Date date,String format){
		SimpleDateFormat sf = null;
		if(Tool.isBlank(format)){
			sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else{
			sf = new SimpleDateFormat(format);
		}
		return sf.format(date);
	}
	
	
	/**
	 * 方法名：toDate
	 * 详述：把String转成Date
	 * 创建时间：2016年4月22日
	 * @param str
	 * @param format
	 * @return
	 * @throws ParseException Date
	 */
	public static Date toDate(String str,String format) throws ParseException {
		if(isBlank(str)){
			return null;
		}
		if(Tool.isBlank(format)){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat(format);
		date = sf.parse(str);
		return date;
	}
	
	/**
	 * 获取Date对象当前月份的第一天
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getMonthFirst(Date date) {
		if(date == null){
			return null;
		}
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		return calendar.getTime();
	}
	
	/**
	 * 获取Date对象当前月份的最后一天
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getMonthLast(Date date) {
		if(date == null){
			return null;
		}
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}
	
	/**
	 * 返回Date年份加value后的Date对象
	 * @param date
	 * @param value
	 * @return
	 */
	public static Date yearPlu(Date date,int value){
		if(date == null || value < 1){
			return null;
		}
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)+value);
		return calendar.getTime();
	}
	
	/**
	 * 返回Date年份减value后的Date对象
	 * @param date
	 * @param value
	 * @return
	 */
	public static Date yearSub(Date date,int value){
		if(date == null || value < 1){
			return null;
		}
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)-value);
		return calendar.getTime();
	}
	
	/**
	 * 方法名：yearSub
	 * 详述：两个日期相减，返回 date1 - date2 的年份差
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int yearSub(Date date1,Date date2){
		if(date1 != null && date2 != null){
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance(); 
			cal1.setTime(date1);
			cal2.setTime(date2);
			return (cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR));
		}
		return 0;
	}
	
	/**
	 * 方法名：datePlu
	 * 详述：返回日期参数 加 value天后的Date
	 * 创建时间：2016年4月22日
	 * @param date
	 * @param value
	 * @return Date
	 */
	public static Date datePlu(Date date,int value){
		if(date == null || value < 1){
			return null;
		}
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, value);
		return calendar.getTime();
	}
	
	/**
	 * 
	 * 方法名：datePluWork
	 * 详述：返回日期参数 加 value天后的Date
	 * 开发人员：李启华
	 * @param date
	 * @param value
	 * @return
	 */
	public static Date datePluWork(Date date,int value){
		if(date == null || value < 1){
			return null;
		}
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTime(date);
		for(int i = value; i > 0; i--){
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			//不要星期六7和星期天1
			while(calendar.get(Calendar.DAY_OF_WEEK) == 7 || calendar.get(Calendar.DAY_OF_WEEK) == 1){
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return calendar.getTime();
	}
	
	/**
	 * 方法名：dateSub
	 * 详述：返回日期参数 减 value天后的Date
	 * 创建时间：2016年4月22日
	 * @param date
	 * @param value
	 * @return Date
	 */
	public static Date dateSub(Date date,int value){
		if(date == null || value < 1){
			return null;
		}
		Calendar calendar=Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 0-value);
		return calendar.getTime();
	}
	
	/**
	 * 方法名：day2Day
	 * 详述：计算两个Date对象之间相差多少天
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int day2Day(Date min,Date max){
		if(min == null || max == null || min.getTime() > max.getTime()){
			return 0;
		}
		int result = (int)((max.getTime() - min.getTime())/1000/3600/24);
		return result;
	}
	
	/**
	 * 方法名：isSameDay
	 * 详述：判断两个Date是否是同一天
	 * 创建时间：2016年4月23日
	 * @param d1
	 * @param d2
	 * @return boolean
	 */
	public static boolean isSameDay(Date d1,Date d2){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(d1);
		cal2.setTime(d2);
		if(cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR)){
			return false;
		}
		if(cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)){
			return false;
		}
		if(cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * 方法名：getWeekOne
	 * 详述：获取星期一
	 * @param date
	 * @return
	 */
	public static Date getWeekOne(Date date){
		if(date == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if(day == 1){//星期天
			return dateSub(date, 6);
		}
		if(day == 2){//星期一
			return date;
		}
		if(day == 3){
			return dateSub(date, 1);
		}
		if(day == 4){
			return dateSub(date, 2);
		}
		if(day == 5){
			return dateSub(date, 3);
		}
		if(day == 6){
			return dateSub(date, 4);
		}
		if(day == 7){
			return dateSub(date, 5);
		}
		return null;
	}
	
	
	
	
	/**
	 * 方法名：MD5
	 * 详述：MD5加密字符串返回小写，如果upper是true，返回大写
	 * @param str 加密字符串
	 * @param upper 如果upper是true，返回大写
	 * @return
	 */
	public static String MD5(String str,boolean upper){
		try{
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] md5 = digest.digest(str.getBytes("UTF-8"));
			StringBuffer sb = new StringBuffer();
			String part = null;
			for (int i=0;i<md5.length;i++) {
				part = Integer.toHexString(md5[i] & 0xFF);
				if (part.length()==1) {
					part = "0"+part;
				}
				sb.append(part);
			}
			String result = sb.toString().toLowerCase();
			if(upper == true){
				result = result.toUpperCase();
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 方法名：subImg
	 * 详述：处理CKFinder上传的图片
	 * 创建时间：2016年4月21日
	 * @param img
	 * @return String
	 */
	public static String subImg(String img){
		if(isBlank(img)){
			return null;
		}
		String imgStr = img.replace("|", ",");
		if(imgStr.subSequence(0, 1).equals(",")){
			imgStr = imgStr.substring(1);
		}
		String[] iconArr = imgStr.split(",");
		if(iconArr != null && iconArr.length>1){
			imgStr = iconArr[0];
		}
		return imgStr;
	}
	
	/**
	 * 方法名：subImgs
	 * 详述：处理CKFinder上传的图片
	 * 创建时间：2016年4月21日
	 * @param imgs
	 * @return String
	 */
	public static String subImgs(String imgs){
		if(isBlank(imgs)){
			return null;
		}
		String imgsStr = imgs.replace("|", ",");
		if(imgsStr.subSequence(0, 1).equals(",")){
			imgsStr = imgsStr.substring(1);
		}
		return imgsStr;
	}
	
	/**
	 * 方法名：dateToString
	 * 详述：Date转String
	 * 创建时间：2016年5月4日
	 * @param date
	 * @param type
	 * @return String
	 */
	public static String dateToString(Date date, String type) {
		SimpleDateFormat sdf=new SimpleDateFormat(type); 
		String str=sdf.format(date); 
		return str;
	}


	/**
	 * 发送post请求
	 * @param data
	 * @param url
	 * @return
	 */
	public static String post(String data, String url) {
        try {
            URL postUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) postUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);

			if(Tool.isNotBlank(data)) {
            	conn.setRequestProperty("Content-Length", data.length()+"");
				OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
				out.write(data);
				out.flush();
				out.close();
			}
			InputStream is = null;
			Tool.info("--- post() code : "+conn.getResponseCode());
			if(conn.getResponseCode() == 200){
				is = conn.getInputStream();
			}else{
				is = conn.getErrorStream();
			}
			if(is != null){
				//获取响应内容体
				String line, result = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
				while ((line = in.readLine()) != null) {
					result += line + "\n";
				}
				in.close();
				return result;
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


	/**
	 * 发送get请求
	 * @param url
	 * @return
	 */
	public static String get(String url) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
			InputStream is = null;
			Tool.info("--- get() code : "+conn.getResponseCode());
			if(conn.getResponseCode() == 200){
				is = conn.getInputStream();
			}else{
				is = conn.getErrorStream();
			}
			if(is != null){
				//获取响应内容体
				String line, result = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
				while ((line = in.readLine()) != null) {
					result += line + "\n";
				}
				in.close();
				return result;
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 发送put请求
	 * @param url
	 * @return
	 */
	public static String put(String data,String url) {
		try {
			URL getUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if(Tool.isNotBlank(data)) {
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(data);
				dos.flush();
				dos.close();
			}
			InputStream is = null;
			Tool.info("--- put() code : "+conn.getResponseCode());
			if(conn.getResponseCode() == 200){
				is = conn.getInputStream();
			}else{
				is = conn.getErrorStream();
			}
			if(is != null){
				//获取响应内容体
				String line, result = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
				while ((line = in.readLine()) != null) {
					result += line + "\n";
				}
				in.close();
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


    /**
     * 发送delete请求
     * @param url
     * @return
     */
    public static String delete(String url) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
			InputStream is = null;
			Tool.info("--- delete() code : "+conn.getResponseCode());
			if(conn.getResponseCode() == 200){
				is = conn.getInputStream();
			}else{
				is = conn.getErrorStream();
			}
			if(is != null){
				//获取响应内容体
				String line, result = "";
				BufferedReader in = new BufferedReader(new InputStreamReader(is, "utf-8"));
				while ((line = in.readLine()) != null) {
					result += line + "\n";
				}
				in.close();
				return result;
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


	
	
	
	
	/**
	 * 方法名：printRequest
	 * 详述：打印HttpServletRequest
	 * @param request
	 */
	public static void printRequest(HttpServletRequest request){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			Tool.info("------- printRequest() : \n "+sb.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * 读取InputStream，返回String
	 * @param is
	 * @return
	 */
	public static String InputStream2String(InputStream is) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 读取InputStream，返回byte[]
	 * @param is
	 * @return
	 */
	public static byte[] InputStream2byte(InputStream is) {
		try {
			byte[] arr = new byte[is.available()];
			is.read(arr);
			return arr;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	/**
	 * 方法名：trimList
	 * 详述：去掉List<String>的重复值
	 * @param oldList
	 * @return
	 */
	public static List<String> trimList(List<String> oldList){
		List<String> list = new ArrayList<String>();
		if(oldList != null && oldList.size()>0){
			for(String str : oldList){
				if(!list.contains(str)){
					list.add(str);
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 方法名：XMLToMap
	 * 详述：把xml字符串封装到Map中
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> XMLToMap(HttpServletRequest request){
		try{
			Map<String,String> map = new HashMap<String, String>();
			SAXReader reader = new SAXReader();
			InputStream ins = request.getInputStream();
			Document doc = reader.read(ins);
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			for(Element e : list){
				map.put(e.getName(), e.getText());
			}
			ins.close();
			return map;
		}catch(DocumentException e){
			Tool.info("------------ XMLToMap() DocumentException : "+e.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 方法名：XMLToMap
	 * 详述：把xml字符串封装到Map中
	 * @param xml
	 * @return
	 */
	public static Map<String,String> XMLToMap(String xml){
		try{
			Map<String,String> map = new HashMap<String, String>();
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			for(Element e : list){
				map.put(e.getName(), e.getText());
			}
			return map;
		}catch(DocumentException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 方法名：printMap
	 * 详述：打印map
	 * @param map
	 */
	public static void printMap(Map<String,String> map){
		if(map != null){
			Tool.info("******************* printMap start *******************");
			Set<String> set = map.keySet();
			for(String key : set){
				Tool.info(key+":"+map.get(key));
			}
			Tool.info("******************* printMap end *******************");
		}
	}
	
	/**
	 * 方法名：mapToXML
	 * 详述：把map转成xml字符串
	 * @param map
	 * @return
	 */
	public static String mapToXML(Map map){
		if(map != null){
			Element root = DocumentHelper.createElement("xml");  
		    Document document = DocumentHelper.createDocument(root);
			Set<String> set = map.keySet();
			for(String key : set){
				if(map.get(key) != null){
					root.addElement(key).addText(String.valueOf(map.get(key)));
				}
			}
			return document.asXML();
		}
		return "";
	}
	
	/**
	 * 方法名：objToXML
	 * 详述：把对象变成xml格式字符串
	 * @param obj
	 * @return
	 */
	public static String objToXML(Object obj){
		XStream xstream = new XStream();
		xstream.alias("xml", obj.getClass());
		return xstream.toXML(obj).replace("__", "_");
	}
	
	/** 
	 * @Description 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式（表情占4个字节，需要utf8mb4字符集） 
	 * @param str 待转换字符串 
	 * @return 转换后字符串 
	 */ 
	public static String emojiConvert(String str){
		if(Tool.isNotBlank(str)){
			try{
			    String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";  
			    Pattern pattern = Pattern.compile(patternString);  
			    Matcher matcher = pattern.matcher(str);  
			    StringBuffer sb = new StringBuffer();  
			    while(matcher.find()) {  
			    	matcher.appendReplacement(sb,"[[" + URLEncoder.encode(matcher.group(1),"UTF-8") + "]]");  
			    }  
			    matcher.appendTail(sb);
			    return sb.toString();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/** 
	 * @Description 还原utf8数据库中保存的含转换后emoji表情的字符串 
	 * @param str 转换后的字符串 
	 * @return 转换前的字符串 
	 */
	public static String emojiRecovery(String str) {
		if(Tool.isNotBlank(str)){
			try{
				if(str==null){
					str ="";
				}
			    String patternString = "\\[\\[(.*?)\\]\\]";  
			    Pattern pattern = Pattern.compile(patternString);  
			    Matcher matcher = pattern.matcher(str);  
			  
			    StringBuffer sb = new StringBuffer();  
			    while(matcher.find()) {  
			    	matcher.appendReplacement(sb,URLDecoder.decode(matcher.group(1), "UTF-8"));  
			    }  
			    matcher.appendTail(sb);  
			    return sb.toString();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * 方法名：addImgPrefixInHTML
	 * 详述：把富文本中的img标签的src的值添加前缀，如 http://xxx
	 * @param str 要处理的富文本内容
	 * @param prefix img的src需要添加的前缀
	 * @return
	 */
	public static String addImgPrefixInHTML(String str,String prefix){
		if(isNotBlank(str)){
			str = str.replace("alt=\"\"", "");
			Pattern patternForTag = Pattern.compile("<\\s*img\\s+([^>]*)\\s*>");   
	        Pattern patternForAttrib = Pattern.compile("src=\"([^\"]+)\"");
	        Matcher matcherForTag = patternForTag.matcher(str); 
	        StringBuffer sb = new StringBuffer();
	        boolean result = matcherForTag.find();
	        while (result) {
	            StringBuffer sbreplace = new StringBuffer();   
	            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));   
	            if (matcherForAttrib.find()) {
	            	String newTag = "";
	            	if(matcherForAttrib.group(1).contains("http")){
	            		newTag = "<img src='" + matcherForAttrib.group(1) + "'/>";
	            	}else{
	            		newTag = "<img src='" + prefix + matcherForAttrib.group(1) + "'/>";
	            	}
	            	matcherForAttrib.appendReplacement(sbreplace,newTag);
	            }   
	            matcherForTag.appendReplacement(sb, sbreplace.toString());   
	            result = matcherForTag.find();   
	        }   
	        matcherForTag.appendTail(sb);   
	        return sb.toString();
		}
		return null;
	}
	
	
	/**
	 * 方法名：makeUniqueFileName
	 * 详述：返回不重复的文件名
	 * @param arr 文件名称数组，通常调用File的list()方法获得
	 * @param name 原文件名
	 * @return
	 */
	public static String makeUniqueFileName(String[] arr,String name){
		Pattern p = Pattern.compile("\\([0-9]*\\)");
		if(arr != null && isNotBlank(name)){
			if(name.lastIndexOf(".") != -1){
				String suffix = name.substring(name.lastIndexOf("."));
				if(isNotBlank(suffix)){
					name = name.replace(suffix, "");
				}
			}
			Integer num = 0;
			if(arr.length>0){
				for(String str : arr){
					if(str.lastIndexOf(".") != -1){
						String suffix = str.substring(str.lastIndexOf("."));
						if(isNotBlank(suffix)){
							str = str.replace(suffix, "");
						}
					}
					Matcher mStr = p.matcher(str);
					if(mStr.find()){
						String s = mStr.group(0);
						str = str.replace(s, "");
						num = Integer.parseInt(s.replace("(", "").replace(")", ""))+1;
					}
					if(str.equals(name)){
						name += "("+num+")";
						return makeUniqueFileName(arr,name);
					}
				}
				Matcher mName = p.matcher(name);
				if(!mName.find()){
					for(String str : arr){
						if(str.lastIndexOf(".") != -1){
							String suffix = str.substring(str.lastIndexOf("."));
							if(isNotBlank(suffix)){
								str = str.replace(suffix, "");
							}
						}
						if(str.equals(name)){
							name = name+"(1)";
						}
					}
				}else{
					Integer newNum = 1;
					name = name.replace(mName.group(0), "");
					for(String str : arr){
						if(str.lastIndexOf(".") != -1){
							String suffix = str.substring(str.lastIndexOf("."));
							if(isNotBlank(suffix)){
								str = str.replace(suffix, "");
							}
						}
						Matcher mStr = p.matcher(str);
						if(mStr.find()){
							str = str.replace(mStr.group(0), "");
							if(str.equals(name)){
								Integer eNum = Integer.parseInt(mStr.group(0).replace("(", "").replace(")", ""));
								if(newNum <= eNum){
									newNum = eNum+1;
								}
							}
						}
					}
					name += "("+newNum+")";
				}
			}
			return name;
		}
		return null;
	}
	
	/**
	 * 方法名：formatDecimal
	 * 详述：格式化小数
	 * @param d double小数对象
	 * @param pattern 格式，null默认为#.00
	 * @return
	 */
	public Double formatDecimal(Double d,String pattern){
		if(d != null){
			if(isBlank(pattern)){
				pattern = "#.00";
			}
			DecimalFormat df = new DecimalFormat(pattern);
			return Double.parseDouble(df.format(d));
		}
		return 0.0;
	}
	
	/**
	 * 方法名：divide
	 * 详述：精确计算两个数相除，v1除以v2
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Double divide(Integer v1, Integer v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal bd = b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
	
	/**
	 * 方法名：divide
	 * 详述：精确计算两个数相除，v1除以v2
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Double divide(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        BigDecimal bd = b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
	
	/**
	 * 方法名：divide
	 * 详述：精确计算两个数相除，v1除以v2
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Double divide(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        BigDecimal bd = b1.divide(b2, 10, BigDecimal.ROUND_HALF_UP);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }
	
	/**
	 * 方法名：getIp
	 * 详述：获取外网IP
	 * @return
	 */
	public static String getIp(){
		String protocol = "http://";
		String netip = null;
		try{
			boolean finded = false;// 是否找到外网IP
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			while(netInterfaces.hasMoreElements() && !finded){
				NetworkInterface ni = netInterfaces.nextElement();
	            Enumeration<InetAddress> address = ni.getInetAddresses();
	            while (address.hasMoreElements()) {
	            	 InetAddress ip = address.nextElement();
	            	 if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
	                     netip = protocol+ip.getHostAddress();
	                     finded = true;
	                     break;
	                 } 
	            }
			}
			if(Tool.isBlank(netip)){
				netip = protocol + "127.0.0.1";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return netip;
	}
	
	/**
	 * 方法名：escapeHtml
	 * 详述：去掉字符中的html
	 * @param str
	 * @return
	 */
	public static String escapeHtml(String str){
		if(isNotBlank(str)){
			return StringEscapeUtils.escapeHtml4(str.trim());
		}
		return null;
	}
	
	
	
	/**
	 * 方法名：isChinese
	 * 详述：判断字符串是不是中文，是返回true
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		if(isNotBlank(str)){
			char[] arr = str.toCharArray();
			if(arr != null && arr.length > 0){
				for(char c : arr){
					Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
			        if (ub != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
			                && ub != Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
			                && ub != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
			                && ub != Character.UnicodeBlock.GENERAL_PUNCTUATION
			                && ub != Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
			                && ub != Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			            return false;
			        }
				}
				return true;
			}
		}
        return false;
    }
	
	/**
	 * 方法名：suffixIs
	 * 详述：判断文件后缀
	 * @param file 文件
	 * @param suffix 后缀名
	 * @return
	 */
	public static boolean suffixIs(MultipartFile file,String suffix){
		if(file != null && isNotBlank(suffix)){
			String fileName = file.getOriginalFilename();
			if(fileName.contains(".")){
				String suffixFile = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
				if(suffix.equals(suffixFile)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 方法名：getWeekOne
	 * 详述：获取星期一
	 * @param date
	 * @return
	 */
	public static Date getWeekFirst(Date date){
		if(date == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if(day == 1){//星期天
			return dateSub(date, 6);
		}
		if(day == 2){//星期一
			return date;
		}
		if(day == 3){
			return dateSub(date, 1);
		}
		if(day == 4){
			return dateSub(date, 2);
		}
		if(day == 5){
			return dateSub(date, 3);
		}
		if(day == 6){
			return dateSub(date, 4);
		}
		if(day == 7){
			return dateSub(date, 5);
		}
		return null;
	}
	
	/**
	 * 方法名：getWeekLast
	 * 详述：获取星期天
	 * @param date
	 * @return
	 */
	public static Date getWeekLast(Date date){
		if(date == null){
			return null;
		}
		return datePlu(getWeekFirst(date), 6);
	}
	
	
	
	
	/**
	 * 方法名：getWeekList
	 * 详述：获取date当周星期一~星期天的日期
	 * @param date
	 * @return
	 */
	public static List<Date> getWeekList(Date date){
		if(date == null){
			return null;
		}
		List<Date> list = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if(day == 1){//星期天
			Date start = dateSub(date, 6);
			list.add(start);
			for(int i=0; i<6; i++){
				start = datePlu(start, 1);
				list.add(start);
			}
		}
		if(day == 2){//星期一
			Date start = date;
			list.add(start);
			for(int i=0; i<6; i++){
				start = datePlu(start, 1);
				list.add(start);
			}
		}
		if(day == 3){
			Date start = dateSub(date, 1);
			list.add(start);
			for(int i=0; i<6; i++){
				start = datePlu(start, 1);
				list.add(start);
			}	
		}
		if(day == 4){
			Date start = dateSub(date, 2);
			list.add(start);
			for(int i=0; i<6; i++){
				start = datePlu(start, 1);
				list.add(start);
			}
		}
		if(day == 5){
			Date start = dateSub(date, 3);
			list.add(start);
			for(int i=0; i<6; i++){
				start = datePlu(start, 1);
				list.add(start);
			}
		}
		if(day == 6){
			Date start = dateSub(date, 4);
			list.add(start);
			for(int i=0; i<6; i++){
				start = datePlu(start, 1);
				list.add(start);
			}
		}
		if(day == 7){
			Date start = dateSub(date, 5);
			list.add(start);
			for(int i=0; i<6; i++){
				start = datePlu(start, 1);
				list.add(start);
			}
		}
		return list;
	}
	
	/**
	 * 方法名：makeColor
	 * 详述：随机生成一个16进制颜色
	 * @return
	 */
	public static String makeColor(){
		String r,g,b;    
        Random random = new Random();    
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();    
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();    
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();    
        r = r.length()==1 ? "0" + r : r ;    
        g = g.length()==1 ? "0" + g : g ;    
        b = b.length()==1 ? "0" + b : b ; 
        String hexColor = r+g+b;
        return hexColor;
	}
	
	
	/**
	 * 方法名：getSundayNum
	 * 详述：获取两个时间之间有多少个星期天
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getSundayNum(Date start,Date end){
		if(start != null && end != null){
			int num = 0;
			Calendar calStart = Calendar.getInstance();
			Calendar calEnd = Calendar.getInstance();
			calStart.setTime(start);
			calEnd.setTime(end);
			while(calStart.get(Calendar.DAY_OF_YEAR) <= calEnd.get(Calendar.DAY_OF_YEAR)){
				if(calStart.get(Calendar.DAY_OF_WEEK) == 1){
					num += 1;
				}
				calStart.add(Calendar.DAY_OF_YEAR, 1);
			}
			return num;
		}
		return 0;
	}
	
	
	/**
	 * 方法名：double2String
	 * 详述：使转化得到的str不是科学计数法，如：4.3062319920812602E17->43062319920812602.00
	 * @param d
	 * @param pattern
	 * @return
	 */
	public static String double2String(Double d,String pattern){
		if(d != null){
			if(isBlank(pattern)){
				pattern = "0.00";
			}
			DecimalFormat df = new DecimalFormat(pattern);
			return df.format(d);
		}
		return null;
	}
	
	/**
	 * 方法名：double2String
	 * 详述：double的金额转String(超过万，转成，如：3.00万)
	 * 创建时间：2016年5月24日
	 * @param num
	 * @return String
	 */
	public static String double2String(Double num) {
		String str = "";
		if (num >= 10000 && num < 1000000) {
			num = num * 0.0001;
			DecimalFormat df = new DecimalFormat("######0.0");  
			str = df.format(num);
			str += "万";
		} else if (num >= 1000000 && num < 10000000) {
			num = num * 0.000001;
			DecimalFormat df = new DecimalFormat("######0.0");  
			str = df.format(num);
			str += "百万";
		} else if (num >= 10000000) {
			num = num * 0.0000001;
			DecimalFormat df = new DecimalFormat("######0.0");  
			str = df.format(num);
			str += "千万";
		} else {
			str = Double.toString(num);
		}
		return str;
	}
	
	
	
	/**
	 * 方法名：sysEncode
	 * 详述：框架加密（先RSA后BASE64），参数最大长度3275
	 * @param str
	 * @return
	 */
	public static String sysEncode(String str) {
		return SysSecurity.BASE64Encode(str);
	}
	
	/**
	 * 方法名：sysDecode
	 * 详述：框架解密（先BASE64后RSA），参数最大长度3275
	 * @param str
	 * @return
	 */
	public static String sysDecode(String str) {
		return SysSecurity.BASE64Decode(str);
	}
	
	
	/**
	 * 方法名：replaceXSS
	 * 详述：去除待带script、src的语句，转义替换后的value值
	 * @param value
	 * @return
	 */
	public static String replaceXSS(String value) {  
        if (value != null) {  
            try{  
                value = value.replace("+","%2B");   //'+' replace to '%2B'  
                value = URLDecoder.decode(value, "utf-8");  
            }catch(UnsupportedEncodingException e){
            	e.printStackTrace();
            }catch(IllegalArgumentException e){
            	e.printStackTrace();
            }  
              
            // Avoid null characters  
            value = value.replaceAll("\0", "");  
  
            // Avoid anything between script tags  
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            // Avoid anything in a src='...' type of e­xpression  
            /*scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");*/
  
  
            // Remove any lonesome </script> tag  
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            // Remove any lonesome <script ...> tag  
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            // Avoid eval(...) e­xpressions  
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            // Avoid e­xpression(...) e­xpressions  
            scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
  
            // Avoid javascript:... e­xpressions  
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid alert:... e­xpressions  
            scriptPattern = Pattern.compile("alert", Pattern.CASE_INSENSITIVE);  
            value = scriptPattern.matcher(value).replaceAll("");  
            // Avoid onload= e­xpressions  
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);  
            value = scriptPattern.matcher(value).replaceAll("");  
            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);    
            value = scriptPattern.matcher(value).replaceAll("");  
        }             
        return XSSFilter(value);  
    }  
          
	
	/**
	 * 方法名：XSSFilter
	 * 详述：过滤特殊字符 
	 * @param value
	 * @return
	 */
    public static String XSSFilter(String value) {  
        if (value == null) {  
            return null;  
        }          
        StringBuffer result = new StringBuffer(value.length());  
        for (int i=0; i<value.length(); ++i) {  
            switch (value.charAt(i)) {  
                case '<':  
                    result.append("<");  
                    break;  
                case '>':   
                    result.append(">");  
                    break;  
                case '"':   
                    result.append("\"");  
                    break;  
                case '\'':   
                    result.append("'");  
                    break;  
                case '%':   
                    result.append("%");  
                    break;  
                case ';':   
                    result.append(";");  
                    break;  
                case '(':   
                    result.append("(");  
                    break;  
                case ')':   
                    result.append(")");  
                    break;  
                case '&':   
                    result.append("&");  
                    break;  
                case '+':  
                    result.append("+");  
                    break;  
                default:  
                    result.append(value.charAt(i));  
                    break;  
            }    
        }  
        return result.toString();  
    }
	
	/**
	 * 获取项目路径
	 */
	public static String getProjectPath() {
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		if(path.contains("!")) {
			path = path.substring(0, path.indexOf("!"));
			path = path.replace("file:", "");
			path = path.substring(0,path.lastIndexOf("/")+1);
		}else {
			path = path.substring(1);
			path = path.replace("classes/", "");
		}
		if(JVMUtils.isWindows()){
			String firstChar = path.substring(0,1);
			if(firstChar.equals("/")){
				path = path.substring(1,path.length());
			}
		}
		return path;
	}
	
	
	/**
	 * 方法名：getUploadToQiniu
	 * 详述：获取配置文件中是否使用七牛云存储
	 * @return
	 */
	/*public static boolean getUploadToQiniu() {
		String value = (String) CacheUtil.get(CacheUtil.KEY_UPLOAD_TO_QINIU);
		if(Tool.isNotBlank(value) && "true".equals(value)) {
			return true;
		}
		return false;
	}
	
	
	
	*//**
	 * 方法名：getUploadKey
	 * 详述：获取文件上传密钥
	 *//*
	public static String getUploadKey() {
		return (String) CacheUtil.get(CacheUtil.KEY_UPLOAD_KEY);
	}
	
	*//**
	 * 方法名：getUploadPrefix
	 * 详述：获取文件上传服务器地址
	 *//*
	public static String getUploadPrefix() {
		return (String) CacheUtil.get(CacheUtil.KEY_UPLOAD_PREFIX);
	}
	
	*//**
	 * 方法名：getUploadUEditor
	 * 详述：获取ueditor文件上传地址
	 *//*
	public static String getUploadUEditor() {
		return getUploadPrefix() + (String) CacheUtil.get(CacheUtil.KEY_UPLOAD_UEDITOR);
	}
	
	*//**
	 * 方法名：getUploadSingle
	 * 详述：获取api单文件上传地址
	 *//*
	public static String getUploadSingle() {
		return getUploadPrefix() + (String) CacheUtil.get(CacheUtil.KEY_UPLOAD_SINGLE);
	}
	
	*//**
	 * 方法名：getUploadMulti
	 * 详述：获取api多文件上传地址
	 *//*
	public static String getUploadMulti() {
		return getUploadPrefix() + (String) CacheUtil.get(CacheUtil.KEY_UPLOAD_MULTI);
	}
	*/
	
	/**
	 * 方法名：uploadFileRemote
	 * 详述：okhttp文件上传
	 * @param url 上传地址
	 * @param name 参数名
	 * @param file 文件
	 * @param paramMap 其他参数
	 * @return
	 */
	public static String uploadFileRemote(String url,String name,File file,Map<String,String> paramMap) {
		if(Tool.isNotBlank(name) && file != null && file.length()>0) {
			MultipartBody body;
			MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
			bodyBuilder.setType(MultipartBody.FORM);
			if(paramMap != null) {
				for(Iterator<String> iter = paramMap.keySet().iterator();iter.hasNext();) {
					String paramName = iter.next();
					String paramValue = paramMap.get(paramName);
					if(Tool.isNotBlank(paramName,paramValue)) {
						bodyBuilder.addFormDataPart(paramName,paramValue);
					}
				}
			}
			MediaType mt = MediaType.parse("application/octet-stream");
			bodyBuilder.addFormDataPart(name, file.getName(), RequestBody.create(mt, file));
			body = bodyBuilder.build();
			Request okRequest = new Request.Builder().url(url).post(body).build();
			OkHttpClient client = new OkHttpClient();
			try {
				Response response = client.newCall(okRequest).execute();
				String result = response.body().string();
				Tool.info("--- fileUpload() result : "+result,Tool.class);
				JSONObject json = JSONObject.fromObject(result);
				String path = json.getString("resultData");
				Tool.info("--- fileUpload() path : "+path,Tool.class);
				return path;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}




    /**
     * 把file保存到项目本地路径
     * @param file
     * @param dir 上传目录，null默认为upload/
     * @return
     */
	public static SysFile uploadFile(MultipartFile file,String dir) {
	    if(file == null || file.getSize() < 1){
	        return null;
        }
	    if(Tool.isBlank(dir)){
            dir = "upload/";
        }
        String uploadType = getUploadType();
        //如果上传类型为 remote
        if(uploadType.equals(MyConstance.UPLOAD_TYPE_REMOTE)){
            String url = SysConfig.getConfig("upload.prefix") + SysConfig.getConfig("upload.single");
            String key = SysConfig.getConfig("upload.key");
            if(isBlank(url,key)){
                Tool.error("--- url:"+url+",key:"+key,Tool.class);
                return null;
            }
            File upFile = toFile(file);
            if(upFile == null || upFile.length() < 1){
                Tool.error("--- upFile is NULL ",Tool.class);
                return null;
            }
            Map<String,String> paramMap = new HashMap<String,String>();
            paramMap.put("key", key);
            String path = uploadFileRemote(url, "file", upFile, paramMap);
            if(isBlank(path)){
                Tool.error("--- path is NULL ",Tool.class);
                return null;
            }
            SysFile sysFile = new SysFile();
            sysFile.path = path;
            sysFile.file = upFile;
            return sysFile;
        }else if(uploadType.equals(MyConstance.UPLOAD_TYPE_QINIU)){
			SysFile sysFile = new SysFile();
			sysFile.path = QiniuTool.uploadFile(file);
			return sysFile;
		}else{
			try {
				if(file != null && file.getSize()>0){
					String fileName = file.getOriginalFilename();
					if(!fileName.contains(".")){
						return null;
					}
					String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
					String newFileName =  UUID.randomUUID().toString().replace("-", "")+System.currentTimeMillis()+suffix;
					String saveDirStr = getProjectPath()+dir;
					File saveDir = new File(saveDirStr);
					saveDir.mkdirs();
					File newFile = new File(saveDirStr,newFileName);
					newFile.createNewFile();
					file.transferTo(newFile);
					if(newFile != null && newFile.length()>0) {
						SysFile sysFile = new SysFile( "/" + dir + newFileName, newFile);
						return sysFile;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}


    /**
     * MultipartFile转File
     * @param file
     * @return
     */
	public static File toFile(MultipartFile file){
        if(file != null && file.getSize()>0){
            try {
                String fileName = file.getOriginalFilename();
                if (!fileName.contains(".")) {
                    return null;
                }
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String newFileName = UUID.randomUUID().toString().replace("-", "") + System.currentTimeMillis() + suffix;
                String saveDirStr = getProjectPath() + "tmpFile/";
                File saveDir = new File(saveDirStr);
                saveDir.mkdirs();
                File newFile = new File(saveDirStr, newFileName);
                newFile.createNewFile();
                file.transferTo(newFile);
                return newFile;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
	}


	/**
	 * 方法名：getFileSuffix
	 * 详述：获取文件后缀，包含.
	 * @param file
	 * @return
	 */
	public static String getFileSuffix(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		if(!fileName.contains(".")){
			return null;
		}
		String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		return suffix;
	}
	
	
	/**
	 * 方法名：genId
	 * 详述：生成id
	 * @return
	 */
	public static String genId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 方法名：info
	 * 详述：打印日志
	 * @param info
	 */
	public static void info(Object info) {
		LogManager.getLogger(Tool.class).info(info);
	}
	
	/**
	 * 方法名：info
	 * 详述：打印日志
	 * @param info
	 * @param clazz
	 */
	public static void info(Object info,Class<?> clazz) {
		LogManager.getLogger(clazz).info(info);
	}
	
	/**
	 * 方法名：warn
	 * 详述：打印日志
	 * @param info
	 * @param clazz
	 */
	public static void warn(Object info,Class<?> clazz) {
		LogManager.getLogger(clazz).warn(info);
	}
	
	/**
	 * 方法名：error
	 * 详述：打印日志
	 * @param error
	 * @param clazz
	 */
	public static void error(Object error,Class<?> clazz) {
		LogManager.getLogger(clazz).error(error);
	}
	
	
	
	/**
	 * 方法名：isIMG
	 * 详述：判断文件是不是图片
	 * @param suffix
	 * @return
	 */
	public static boolean isIMG(String suffix) {
		if(isNotBlank(suffix)) {
			String[] arr = {".png", ".jpg", ".jpeg", ".gif", ".bmp"};
			for(String s : arr) {
				if(suffix.toLowerCase().equals(s)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 方法名：isIMG
	 * 详述：判断文件是不是图片
	 * @param file
	 * @return
	 */
	public static boolean isIMG(MultipartFile file) {
		String suffix = getFileSuffix(file);
		if(isNotBlank(suffix)) {
			String[] arr = {".png", ".jpg", ".jpeg", ".gif", ".bmp"};
			for(String s : arr) {
				if(suffix.toLowerCase().equals(s)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 方法名：isVIDEO
	 * 详述：判断文件是不是视频
	 * @param suffix
	 * @return
	 */
	public static boolean isVIDEO(String suffix) {
		if(isNotBlank(suffix)) {
			String[] arr = {".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid"}; 
			for(String s : arr) {
				if(suffix.toLowerCase().equals(s)) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 方法名：isVIDEO
	 * 详述：判断文件是不是视频
	 * @param file
	 * @return
	 */
	public static boolean isVIDEO(MultipartFile file) {
		String suffix = getFileSuffix(file);
		if(isNotBlank(suffix)) {
			String[] arr = {".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid"}; 
			for(String s : arr) {
				if(suffix.toLowerCase().equals(s)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 方法名：makeOrderNo
	 * 详述：生成不重复的订单号
	 * @return
	 */
	public synchronized static String makeOrderNo() {
		try {
			Thread.sleep(1);//休眠1毫秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Tool.formatDate(new Date(), "yyyyMMddHHmmssSSS")+Tool.random();
	}
	
	
	/**
	 * 方法名：serialize
	 * 详述：序列化对象，Object->byte[]
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			byte[] bytes = baos.toByteArray();
			return bytes;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 方法名：unSerialize
	 * 详述：反序列化对象，byte[]->Object
	 * @param bytes
	 * @return
	 */
	public static Object unSerialize(byte[] bytes) {
		if(bytes == null || bytes.length < 1) {
			return null;
		}
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 从一个List随机抽取一个元素
	 * @param list
	 * @return
	 */
	public static String randomList(List<String> list){
		int percent = (int)(1000 / list.size());
		List<Integer> sectionList = new ArrayList<Integer>();
		sectionList.add(0);
		int totalPro = 0;
		for (String element : list) {
			totalPro += percent;
			sectionList.add(totalPro);
		}
		Random random = new Random();
		int result = random.nextInt(totalPro);
		for(int i=0; i<sectionList.size()-1; i++){
			if(result >= sectionList.get(i) && result < sectionList.get(i+1)){
				return list.get(i);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(new Sha1Hash("QiHua19930910").toString());
	}
	
}
