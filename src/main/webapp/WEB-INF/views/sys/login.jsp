<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!doctype html>
<html>
	<head>
	    <title>管理后台登陆</title>
	    <%@ include file="/WEB-INF/views/include/style.jsp"%>
	    <style>
	    	.form-wrap{width:500px;margin:150px auto;margin-top:150px;border:2px solid #f5f5f5;border-radius:10px;}
	    	.div-error{width:480px;margin:0 auto;margin-top:30px;display:none;}
	    </style>
  	</head>
  	<body>
    	<div class="alert alert-error div-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
		  	<span id="error-message">${message }</span>
		</div>
    	<div class="form-wrap">
    		<form id="inputForm" method="post" action="/sys/indexWebController/login" class="form-horizontal">
				<div class="control-group"></div>
				<div class="control-group">
	    			<label class="control-label">用户名：</label>
		    		<div class="controls">
		    			<input id="username" type="text" name="username"/>
		    		</div>
	 			</div>
	 			<div class="control-group">
		    		<label class="control-label">密码：</label>
		    		<div class="controls">
		    			<input id="password" type="password" name="password"/>
		    		</div>
	 			</div>
	 			<div class="form-actions">
	 				<input type="submit" value="登录" class="btn btn-large btn-info"/>
	 			</div>
			</form>
    	</div>
  	</body>
  	<script type="text/javascript">
  		$(document).ready(function(){
  			// 让登陆页面始终在顶级窗口打开
  			if (self != top) {
  				window.top.location.href="/sys/loginWebController/login";
  			}
  			// 错误消息不为空时显示div
  			var msg = $("#error-message").text();
  			if(msg != null && msg != '' && msg != 'null'){
  				$(".div-error").css("display","block");
  			}
  			// 表单提交
  			$("#inputForm").submit(function(){
  				var username = $("#username").val();
  				var password = $("#password").val();
  				// 去除空格
  				var reg=new RegExp(' ',"gm"); 
  				username = username.replace(reg,'');
  				password = password.replace(reg,'');
  				// 非空校验
  				if(typeof(username) != 'undefined' && username != null && username != '' && typeof(password) != 'undefined' && password != null && password != ''){
  					// 密码sha1加密
  					var shaObj = new jsSHA("SHA-1", "TEXT");
  					shaObj.update(password);
  					password = shaObj.getHash("HEX");
  					$("#username").val(username);
  	  				$("#password").val(password);
  	  				return true;
  				}
  	  			return false;
  			});
  		});
  	</script>
</html>
