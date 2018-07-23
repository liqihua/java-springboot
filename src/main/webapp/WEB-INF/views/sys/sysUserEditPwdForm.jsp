<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
  	<head>
    	<title>修改密码</title>
    	<%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
  	<body>
  		<ul class="nav nav-tabs sys-font">
			<li class="active"><a href="javascript:;">修改密码</a></li>
		</ul>
		<sys:message content="${message }"/>
		<form id="inputForm" action="/sys/userWebController/editPassword" method="post" class="form-horizontal sys-form-loading">
			<div class="control-group">
				<label class="control-label">新密码：</label>
				<div class="controls">
					<input id="password" type="password" name="password" class="input-xlarge required"/>
				</div>
			</div>
			<div class="form-actions">
				<input id="btnSubmit" class="btn btn-info btn-form-submit" type="submit" value="保 存"/>&nbsp;
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form>
		<script type="text/javascript">
		$(document).ready(function(){
			$("#inputForm").submit(function(){
				var password = $("#password").val();
				// 密码sha1加密
				if(notBlank(password)){
					var shaObj = new jsSHA("SHA-1", "TEXT");
					shaObj.update(password);
					password = shaObj.getHash("HEX");
	  				$("#password").val(password);
	  				return true;
				}
			});
		});
		</script>
  	</body>
</html>
