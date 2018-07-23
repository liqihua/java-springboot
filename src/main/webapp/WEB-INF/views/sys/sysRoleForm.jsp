<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
  	<head>
    	<title>系统角色表添加</title>
    	<%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
  	<body>
  		<ul class="nav nav-tabs sys-font">
			<li><a href="/sys/sysRoleWebController/list">角色列表</a></li>
			<li class="active"><a href="/sys/sysRoleWebController/form">角色添加</a></li>
		</ul>
  		<form:form id="inputForm" modelAttribute="sysRole" action="/sys/sysRoleWebController/save" method="post" class="form-horizontal sys-form-loading">
			<form:hidden path="id"/>
			<div class="control-group">
				<label class="control-label">name：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" class="input-xlarge required"/>
				</div>
			</div>
			<div class="form-actions">
				<input id="btnSubmit" class="btn btn-info btn-form-submit" type="submit" value="保 存"/>&nbsp;
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
  	</body>
</html>
