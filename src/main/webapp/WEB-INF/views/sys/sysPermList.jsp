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
			<li class="active"><a href="/sys/sysPermWebController/list">权限列表</a></li>
			<li><a href="/sys/sysPermWebController/form">权限添加</a></li>
		</ul>
		<sys:message content="${message }"/>
    	<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
		<thead class="sys-font">
			<tr>
				<th>所属菜单</th>
				<th>权限名称</th>
				<th>权限符号</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<td>操作</td>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="entity">
			<tr>
				<td>
				<c:choose>
					<c:when test="${entity.sysMenu.level eq '2'}">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:when>
					<c:when test="${entity.sysMenu.level eq '3'}">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
				${entity.sysMenu.name }
				</td>
				<td><a href="/sys/sysPermWebController/form?id=${entity.id }">${entity.name }</a></td>
				<td>${entity.permission }</td>
				<td><fmt:formatDate value="${entity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${entity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
    				<a href="/sys/sysPermWebController/form?id=${entity.id }">修改</a>
					<a href="/sys/sysPermWebController/delete?id=${entity.id }" onclick="return confirm('您确定要删除该项吗？')">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
  	</body>
</html>
