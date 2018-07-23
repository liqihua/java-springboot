<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
  	<head>
    	<title>系统权限表添加</title>
    	<%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
  	<body>
  		<ul class="nav nav-tabs sys-font">
			<li><a href="/sys/sysPermWebController/list">权限列表</a></li>
			<li class="active"><a href="/sys/sysPermWebController/form">权限添加</a></li>
		</ul>
  		<form:form id="inputForm" modelAttribute="sysPerm" action="/sys/sysPermWebController/save" method="post" class="form-horizontal sys-form-loading">
			<form:hidden path="id"/>
			<div class="control-group">
				<label class="control-label">所属菜单：</label>
				<div class="controls">
					<sys:tree url="/ajax/sysMenuAjaxController/findListTree" name="menuId" value="${sysPerm.menuId }" required="true"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">权限名称：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" class="input-xlarge required"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">权限符号：</label>
				<div class="controls">
					<form:input path="permission" htmlEscape="false" class="input-xlarge required"/>
				</div>
			</div>
			<div class="form-actions">
				<input id="btnSubmit" class="btn btn-info btn-form-submit" type="submit" value="保 存"/>&nbsp;
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
  	</body>
</html>
