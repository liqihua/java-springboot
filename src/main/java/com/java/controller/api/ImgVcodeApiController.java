package com.java.controller.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.java.common.constance.ApiConstance;
import com.java.common.constance.MyConstance;
import com.java.sys.common.basic.result.BaseResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="api-img-vcode-controller",description="验证码图片")
@SpringBootApplication
@RequestMapping("/api/imgVcode")
public class ImgVcodeApiController {
	private static final long serialVersionUID = 1L;
	private int width = 70;
	private int height =30;
	/**
	 * 验证码字符个数
	 */
	private int codeCount = 5;
	private int xx = 0;
	/**
	 * 字体高度
	 */
	private int fontHeight;
	private int codeY;
	String[] codeSequence = { "1" , "2" , "3" , "4" , "5" ,"6","7","8","9","A","a","B","b","c","C","D","d","E","e","F","f","G","g","z","X","Q","v"};
	
	
	
	
	@ApiOperation(value = "获取session验证码图片")
	@RequestMapping(value = "/makeImg", method = RequestMethod.GET)
	@ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})	
	public ResponseEntity<BaseResult> makeImg(HttpServletRequest request,HttpServletResponse response){
	    try{
		    String strWidth =width+"";// 宽度
		    String strHeight = height+"";// 高度
		    String strCodeCount = codeCount+"";// 字符个数
	    	if (strWidth != null && strWidth.length() != 0) {
	    		width = Integer.parseInt(strWidth);
	    	}
	    	if (strHeight != null && strHeight.length() != 0) {
	    		height = Integer.parseInt(strHeight);
	    	}
	    	if (strCodeCount != null && strCodeCount.length() != 0) {
	    		codeCount = Integer.parseInt(strCodeCount);
	    	}
		    xx = width / (codeCount + 2); //生成随机数的水平距离
		    fontHeight = height - 12;   //生成随机数的数字高度
		    codeY = height - 8;      //生成随机数的垂直距离
		    // 定义图像buffer
		    BufferedImage buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
		    Graphics2D gd = buffImg.createGraphics();
		    // 创建一个随机数生成器类
		    Random random = new Random();
		    // 将图像填充为白色
		    gd.setColor(Color.WHITE);
		    gd.fillRect(0, 0, width, height);
		    // 创建字体，字体的大小应该根据图片的高度来定。
		    Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		    // 设置字体。
		    gd.setFont(font);
		    // 画边框。
		    gd.setColor(Color.BLACK);
		    gd.drawRect(0, 0, width - 1, height - 1);
		    // 随机产生4条干扰线，使图象中的认证码不易被其它程序探测到。
		    gd.setColor(Color.BLACK);
		    for (int i = 0; i < 4; i++) {
		    	int x = random.nextInt(width);
		    	int y = random.nextInt(height);
		    	int xl = random.nextInt(12);
		    	int yl = random.nextInt(12);
		    	gd.drawLine(x, y, x + xl, y + yl);
		    }
		    // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		    StringBuffer randomCode = new StringBuffer();
		    int red = 0, green = 0, blue = 0;
		    // 随机产生codeCount数字的验证码。
		    for (int i = 0; i < codeCount; i++) {
		    	// 得到随机产生的验证码数字。
		    	String strRand = String.valueOf(codeSequence[random.nextInt(27)]);
		    	// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
		    	red = random.nextInt(125);
		    	green = random.nextInt(255);
		    	blue = random.nextInt(200);
		    	// 用随机产生的颜色将验证码绘制到图像中。
		    	gd.setColor(new Color(red, green, blue));
		    	gd.drawString(strRand, (i + 1) * xx, codeY);
		    	// 将产生的四个随机数组合在一起。
		    	randomCode.append(strRand);
		    }
		    // 将四位数字的验证码保存到Session中。
		    request.getSession().setAttribute(MyConstance.KEY_VCODE_IMG, randomCode.toString());
		    // 禁止图像缓存。
		    response.setHeader("Pragma", "no-cache");
		    response.setHeader("Cache-Control", "no-cache");
		    response.setDateHeader("Expires", 0);
		    response.setContentType("image/jpeg");
		    // 将图像输出到Servlet输出流中。
		    ServletOutputStream sos = response.getOutputStream();
		    ImageIO.write(buffImg, "jpeg", sos);
		    sos.close();
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return null;
	}
	
	
	
	
	
	
}
