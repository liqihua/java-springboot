<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
	<head>
	    <title>用户列表</title>
	    <%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
	<body>
    	<ul class="nav nav-tabs sys-font">
			<li class="active"><a href="/sys/userWebController/list">用户列表</a></li>
			<li><a href="/sys/userWebController/form">用户添加</a></li>
		</ul>
		<sys:message content="${message }"/>
    	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead class="sys-font">
			<tr>
				<th>用户名</th>
				<th>昵称</th>
				<th>性别</th>
				<th>锁定</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="entity">
			<tr>
				<td><a href="/sys/userWebController/form?id=${entity.id }">${entity.username }</a></td>
				<td>${entity.nickname }</td>
				<td>${fnc:getDictLabel(entity.gender,'gender','') }</td>
				<td>${fnc:getDictLabel(entity.locked,'locked','') }</td>
				<td><fmt:formatDate value="${entity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${entity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<a href="/sys/userWebController/form?id=${entity.id }">修改</a>
					<c:if test="${entity.id ne '1' }">
						<a href="/sys/userWebController/delete?id=${entity.id }" onclick="return confirm('您确定要删除该项吗？')">删除</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<sys:page page="${page }"/>
  	</body>
</html>
