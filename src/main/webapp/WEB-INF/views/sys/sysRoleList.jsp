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
			<li class="active"><a href="/sys/sysRoleWebController/list">角色列表</a></li>
			<li><a href="/sys/sysRoleWebController/form">角色添加</a></li>
		</ul>
		<sys:message content="${message }"/>
    	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead class="sys-font">
			<tr>
				<th>角色名称</th>
				<th>创建日期</th>
				<th>更新日期</th>
				<td>操作</td>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="entity">
			<tr>
				<td>${entity.name }</td>
				<td><fmt:formatDate value="${entity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${entity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<a href="/sys/sysRoleWebController/distributeForm?id=${entity.id }">分配权限</a>
					<c:if test="${entity.id ne '1' }">
						<a href="/sys/sysRoleWebController/form?id=${entity.id }">修改</a>
						<a href="/sys/sysRoleWebController/delete?id=${entity.id }" onclick="return confirm('您确定要删除该项吗？')">删除</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<sys:page page="${page }"/>
  	</body>
</html>
