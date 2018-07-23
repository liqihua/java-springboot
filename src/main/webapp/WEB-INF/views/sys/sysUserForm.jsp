<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
  	<head>
    	<title>用户添加</title>
    	<%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
  	<body>
  		<ul class="nav nav-tabs sys-font">
			<li><a href="/sys/userWebController/list">用户列表</a></li>
			<li class="active"><a href="/sys/userWebController/form">用户添加</a></li>
		</ul>
  		<form:form id="inputForm" modelAttribute="sysUser" action="/sys/userWebController/save" method="post" class="form-horizontal sys-form-loading">
			<form:hidden path="id"/>
			<div class="control-group">
				<label class="control-label">所属角色：</label>
				<div class="controls">
					<c:choose>
						<c:when test="${sysUser.id eq '1' }">admin</c:when>
						<c:otherwise>
							<form:select path="roleId" class="input-large required">
								<form:option value="" label=""/>
								<form:options items="${roleMap}"   htmlEscape="false"/>
							</form:select>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">用户名：</label>
				<div class="controls">
					<c:choose>
						<c:when test="${sysUser.id eq '1' }">${sysUser.username }</c:when>
						<c:otherwise><form:input path="username" htmlEscape="false" class="input-xlarge required"/></c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">密码：</label>
				<div class="controls">
					<c:choose>
						<c:when test="${empty sysUser.id }">
							<form:input path="password" htmlEscape="false" class="input-xlarge required" type="password"/>
						</c:when>
						<c:otherwise>
							<a id="resetPwd" href="javascript:;">重设密码</a>
							<input id="password" type="password" name="password" class="input-resetPwd hide input-large required" disabled="true"/>
							&nbsp;&nbsp;<a id="cancelResetPwd" href="javascript:;" class="hide">取消</a>
						</c:otherwise>
					</c:choose>
					
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">昵称：</label>
				<div class="controls">
					<form:input path="nickname" htmlEscape="false" class="input-xlarge required"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">性别：</label>
				<div class="controls">
					<form:select path="gender" class="input-large required">
						<form:option value="" label=""/>
						<form:options items="${fnc:getDictList('gender')}"   htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">锁定：</label>
				<div class="controls">
					<c:choose>
						<c:when test="${sysUser.id eq '1' }">否</c:when>
						<c:otherwise>
							<form:select path="locked" class="input-large required">
								<form:option value="" label=""/>
								<form:options items="${fnc:getDictList('locked')}"   htmlEscape="false"/>
							</form:select>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="form-actions">
				<input id="btnSubmit" class="btn btn-info btn-form-submit" type="submit" value="保 存"/>&nbsp;
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
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
			$("#resetPwd").click(function(){
				$(this).hide();
				$(".input-resetPwd").show();
				$(".input-resetPwd").removeAttr("disabled");
				$("#cancelResetPwd").show();
			});
			$("#cancelResetPwd").click(function(){
				$(this).hide();
				$(".input-resetPwd").hide();
				$(".input-resetPwd").attr("disabled","true");
				$(this).siblings(".sys-error-info").remove();
				$("#resetPwd").show();
				
			});
		});
		</script>
  	</body>
</html>
