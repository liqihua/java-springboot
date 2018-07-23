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
			<li><a href="/sys/dictWebController/list">字典列表</a></li>
			<li class="active"><a href="/sys/dictWebController/form">字典添加</a></li>
		</ul>
  		<form:form id="inputForm" modelAttribute="sysDict" action="/sys/dictWebController/save" method="post" class="form-horizontal sys-form-loading">
			<form:hidden path="id"/>
			<div class="control-group">
				<label class="control-label">键值：</label>
				<div class="controls">
					<form:input path="value" htmlEscape="false" class="input-xlarge required"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">标签：</label>
				<div class="controls">
					<form:input path="label" htmlEscape="false" class="input-xlarge required"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">类型：</label>
				<div class="controls">
					<form:input path="type" htmlEscape="false" class="input-xlarge required"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">描述：</label>
				<div class="controls">
					<form:input path="description" htmlEscape="false" class="input-xlarge required"/>
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
		<script type="text/javascript">
		$(document).ready(function(){
			$("#inputForm").submit(function(){
				var rank = $("#rank").val();
				if(notBlank(rank)){
					if(isNaN(rank)){
						alert("排序值必须是数字");
						return false;
					}
				}
			});
		});
		</script>
  	</body>
</html>
