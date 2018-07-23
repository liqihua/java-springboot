package com.java.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.java.common.constance.MyConstance;
import com.java.sys.common.utils.Tool;

@Service
@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
public class CommonService {
	
	
	/**
	 * 方法名：vcodeImg
	 * 详述：检验图形验证码，如果正确返回true
	 * @param request
	 * @param vcodeImg
	 * @return
	 */
	public boolean vcodeImg(HttpServletRequest request,String vcodeImg){
		if(Tool.isNotBlank(vcodeImg)){
			String vcodeImgSession = (String) request.getSession().getAttribute(MyConstance.KEY_VCODE_IMG);
			if(Tool.isNotBlank(vcodeImgSession)){
				vcodeImgSession = vcodeImgSession.toUpperCase();
				vcodeImg = vcodeImg.toUpperCase();
				if(vcodeImg.equals(vcodeImgSession)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	
}
