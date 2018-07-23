<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
	<head>
	    <title>字典列表</title>
	    <%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
	<body>
    	<ul class="nav nav-tabs sys-font">
			<li class="active"><a href="/sys/dictWebController/list">字典列表</a></li>
			<li><a href="/sys/dictWebController/form">字典添加</a></li>
		</ul>
		<form:form id="searchForm" modelAttribute="sysDict" action="/sys/dictWebController/list" method="post" class="breadcrumb form-search">
			<ul class="ul-form">
				<li><label>类型：</label>
					<form:input path="keysType" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-info" type="submit" value="查询"/></li>
				<li class="clear"></li>
			</ul>
		</form:form>
		<sys:message content="${message }"/>
    	<table id="contentTable" class="table table-hover table-striped table-bordered table-condensed">
		<thead class="sys-font">
			<tr>
				<th>键值</th>
				<th>标签</th>
				<th>类型</th>
				<th>描述</th>
				<th>排序</th>
				<th>创建时间</th>
				<th>更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="entity">
			<tr>
				<td>${entity.value }</td>
				<td><a href="/sys/dictWebController/form?id=${entity.id }">${entity.label }</a></td>
				<td><a href="/sys/dictWebController/form?id=${entity.id }">${entity.type }</a></td>
				
				<td>${entity.description }</td>
				<td>${entity.rank }</td>
				<td><fmt:formatDate value="${entity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td><fmt:formatDate value="${entity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td>
    				<a href="/sys/dictWebController/form?id=${entity.id }">修改</a>
					<a href="/sys/dictWebController/delete?id=${entity.id }" onclick="return confirm('您确定要删除该项吗？')">删除</a>
					<a href="/sys/dictWebController/form?type=${entity.type }&description=${entity.description }">添加键值</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<sys:page page="${page }"/>
  	</body>
</html>
