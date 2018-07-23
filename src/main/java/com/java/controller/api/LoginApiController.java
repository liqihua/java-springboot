package com.java.controller.api;

import com.java.common.constance.ApiConstance;
import com.java.common.utils.TokenUtil;
import com.java.sys.common.basic.controller.BaseController;
import com.java.sys.common.basic.result.BaseResult;
import com.java.sys.common.utils.Tool;
import io.swagger.annotations.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(value="api-login-controller",description="登录接口")
@SpringBootApplication
@RequestMapping("/api/redApiController")
public class LoginApiController extends BaseController {


    @ApiOperation(value = "APP登录")
    @RequestMapping(value = "/loginAPP", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})
    public ResponseEntity<BaseResult> loginAPP(HttpServletRequest request, HttpServletResponse response,
        @ApiParam("用户名") @RequestParam(value="username",required=false) String username,
        @ApiParam("密码") @RequestParam(value="password",required=false) String password){
        //业务获取用户id
        String userId = "userId";
        //设置token过期时间为30天后
        Date expireTime = Tool.datePlu(new Date(),30);
        //生成token
        String token = TokenUtil.makeTokenAPP(userId,expireTime);
        //更新缓存里面该用户的token
        TokenUtil.updateTokenAPP(userId,token);
        Map<String,Object> map = new HashMap<String,Object>();
        //map.put("user",user);//用户信息
        map.put("token",token);//服务器生成的token
        return buildSuccessInfo(map);
    }


    @ApiOperation(value = "WEB登录")
    @RequestMapping(value = "/loginWEB", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = ApiConstance.BASE_SUCCESS_CODE, message = "成功", response = String.class)})
    public ResponseEntity<BaseResult> loginWEB(HttpServletRequest request, HttpServletResponse response,
        @ApiParam("用户名") @RequestParam(value="username",required=false) String username,
        @ApiParam("密码") @RequestParam(value="password",required=false) String password){
        //业务获取用户id
        String userId = "userId";
        //设置token过期时间为30天后
        Date expireTime = Tool.datePlu(new Date(),30);
        //生成token
        String token = TokenUtil.makeTokenAPP(userId,expireTime);
        //更新缓存里面该用户的token
        TokenUtil.updateTokenWEB(userId,token);
        Map<String,Object> map = new HashMap<String,Object>();
        //map.put("user",user);//用户信息
        map.put("token",token);//服务器生成的token
        return buildSuccessInfo(map);
    }
}
