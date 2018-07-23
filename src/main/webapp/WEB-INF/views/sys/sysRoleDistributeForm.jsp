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
			<li class="active"><a href="javascript:;">分配权限</a></li>
		</ul>
		<sys:message content="${message }"/>
    	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead class="sys-font">
			<tr>
				<th>所属菜单</th>
				<th>权限名称</th>
				<th>权限符号</th>
				<th>创建时间</th>
				<th>更新时间</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="perm">
			<tr>
				<td>
				<c:choose>
					<c:when test="${perm.sysMenu.level eq '2'}">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:when>
					<c:when test="${perm.sysMenu.level eq '3'}">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${perm.checked eq 'checked' }">
						<input type="checkbox" name="permId" value="${perm.id }" checked="checked"/>
					</c:when>
					<c:otherwise>
						<input type="checkbox" name="permId" value="${perm.id }"/>
					</c:otherwise>
				</c:choose>
				&nbsp;${perm.sysMenu.name }
				</td>
				<td>${perm.name }</td>
				<td>${perm.permission }</td>
				<td><fmt:formatDate value="${perm.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${perm.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="form-actions">
		<input id="btnSubmit" class="btn btn-info btn-do-save" type="button" value="保 存"/>&nbsp;
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
	<script type="text/javascript">
	$("#btnSubmit").click(function(){
		inLoading("btn-do-save");
		var permIds = "";
		$("input[name='permId']:checked").each(function(){
			permIds += $(this).val()+",";
		});
		window.location.href="/sys/sysRoleWebController/distributeSave?permIds="+permIds+"&roleId=${sysRole.id}";
	});
	</script>
  	</body>
</html>
