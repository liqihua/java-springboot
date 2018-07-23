package com.java.common.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.java.sys.common.utils.Tool;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AliSMS {
	/*
	 * 模板id
	 */
	//尊敬的用户，您本次的验证码为：${code}。如非本人操作请忽略本信息。
	public static final String TEMPLATE_ID_VCODE = "SMS_117517382";
	
	
	/*
	 * 短信签名-可在短信控制台中找到
	 */
	static final String SIGN_NAME = "选大学";
	/*
	 * 产品名称:云通信短信API产品,开发者无需替换
	 */
    static final String product = "Dysmsapi";
    /*
     * 产品域名,开发者无需替换
     */
    static final String domain = "dysmsapi.aliyuncs.com";
    /*
     * 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
     */
    static final String accessKeyId = "LTAIsv9AyrCz9sMY";
    static final String accessKeySecret = "e4fUeM0QZ6tp813kEJ8G8DH32NMUIH";

    /**
     * 方法名：sendVCode
     * 详述：发送短信验证码
     * @param phone
     * @param vcode
     */
    public static void sendVCode(String phone,String vcode) {
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("code", vcode);
    	sendSms(phone, TEMPLATE_ID_VCODE, map);
    	Tool.info("--- code : "+vcode+", phone : "+phone, AliSMS.class);
    }
    
    /**
     * 方法名：sendSms
     * 详述：发送短信
     * @param phone
     * @param templateId
     * @param param
     */
    public static void sendSms(String phone,String templateId,Map<String,String> param) {
    	try {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(SIGN_NAME);//必填:短信签名-可在短信控制台中找到
        request.setTemplateCode(templateId);//必填:短信模板-可在短信控制台中找到
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        if(param != null) {
        	JSONObject paramJson = JSONObject.fromObject(param);
        	if(paramJson != null) {
        		request.setTemplateParam(paramJson.toString());
        	}
        }
        SendSmsResponse response = acsClient.getAcsResponse(request);
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());
    	}catch(ClientException e) {
    		e.printStackTrace();
    	}
    }


    public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

}
