<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
  	<head>
    	<title>菜单添加</title>
    	<%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
  	<body>
  		<ul class="nav nav-tabs sys-font">
			<li><a href="/sys/menuWebController/list">菜单列表</a></li>
			<li class="active"><a href="/sys/menuWebController/form">菜单添加</a></li>
		</ul>
  		<form:form id="inputForm" modelAttribute="sysMenu" action="/sys/menuWebController/save" method="post" class="form-horizontal sys-form-loading">
			<form:hidden path="id"/>
			<div class="control-group">
				<label class="control-label">上级菜单：</label>
				<div class="controls">
					<sys:tree url="/ajax/sysMenuAjaxController/findListTree" name="parentId" value="${sysMenu.parentId }" required="false"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">名称：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" class="input-xlarge required"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">链接：</label>
				<div class="controls">
					<form:input path="href" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">隐藏：</label>
				<div class="controls">
					<form:select path="hide" class="input-large required">
						<form:option value="" label=""/>
						<form:options items="${fnc:getDictList('hide')}"   htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">排序：</label>
				<div class="controls">
					<form:input path="rank" htmlEscape="false" class="input-xlarge "/>
				</div>
			</div>
			<div class="form-actions">
				<input id="btnSubmit" class="btn btn-info btn-form-submit" type="submit" value="保 存"/>&nbsp;
				<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			</div>
		</form:form>
  	</body>
</html>
