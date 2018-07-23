<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
	<head>
	    <title>菜单列表</title>
	    <%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
	<body>
    	<ul class="nav nav-tabs sys-font">
			<li class="active"><a href="/sys/menuWebController/list">菜单列表</a></li>
			<li><a href="/sys/menuWebController/form">菜单添加</a></li>
		</ul>
		<sys:message content="${message }"/>
    	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead class="sys-font">
			<tr>
				<th>菜单名称</th>
				<th>上级菜单</th>
				<th>菜单等级</th>
				<th>链接</th>
				<th>隐藏</th>
				<th>排序</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="entity">
			<tr>
				<td><a href="/sys/menuWebController/form?id=${entity.id }">${entity.name }</a></td>
				<td>
					<c:choose>
						<c:when test="${empty entity.parentName }">空</c:when>
						<c:otherwise>${entity.parentName }</c:otherwise>
					</c:choose>
				</td>
				<td>${fnc:getDictLabel(entity.level, 'menu_level' ,'空') }</td>
				<td>${entity.href }</td>
				<td>${fnc:getDictLabel(entity.hide, 'hide' ,'空') }</td>
				<td>${entity.rank }</td>
				<td><fmt:formatDate value="${entity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${entity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
					<a href="/sys/menuWebController/form?parentId=${entity.id }">添加下级菜单</a>
    				<a href="/sys/menuWebController/form?id=${entity.id }">修改</a>
					<a href="/sys/menuWebController/delete?id=${entity.id }" onclick="return confirm('您确定要删除该项吗？')">删除</a>
				</td>
			</tr>
			<c:forEach items="${entity.childList}" var="two">
				<tr>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/sys/menuWebController/form?id=${two.id }">${two.name }</a></td>
					<td>
						<c:choose>
							<c:when test="${empty two.parentName }">空</c:when>
							<c:otherwise>${two.parentName }</c:otherwise>
						</c:choose>
					</td>
					<td>${fnc:getDictLabel(two.level, 'menu_level' ,'空') }</td>
					<td>${two.href }</td>
					<td>${fnc:getDictLabel(two.hide, 'hide' ,'空') }</td>
					<td>${two.rank }</td>
					<td><fmt:formatDate value="${two.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${two.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<a href="/sys/menuWebController/form?parentId=${two.id }">添加下级菜单</a>
	    				<a href="/sys/menuWebController/form?id=${two.id }">修改</a>
						<a href="/sys/menuWebController/delete?id=${two.id }" onclick="return confirm('您确定要删除该项吗？')">删除</a>
					</td>
				</tr>
				<c:forEach items="${two.childList}" var="Three">
					<tr>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/sys/menuWebController/form?id=${Three.id }">${Three.name }</a></td>
						<td>
							<c:choose>
								<c:when test="${empty Three.parentName }">空</c:when>
								<c:otherwise>${Three.parentName }</c:otherwise>
							</c:choose>
						</td>
						<td>${fnc:getDictLabel(Three.level, 'menu_level' ,'空') }</td>
						<td>${Three.href }</td>
						<td>${fnc:getDictLabel(Three.hide, 'hide' ,'空') }</td>
						<td>${Three.rank }</td>
						<td><fmt:formatDate value="${Three.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td><fmt:formatDate value="${Three.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>
		    				<a href="/sys/menuWebController/form?id=${Three.id }">修改</a>
							<a href="/sys/menuWebController/delete?id=${Three.id }" onclick="return confirm('确定删除该菜单吗？')">删除</a>
						</td>
					</tr>
				</c:forEach>
			</c:forEach>
		</c:forEach>
		</tbody>
	</table>
	<%-- <sys:page page="${page }"/> --%>
  	</body>
</html>
