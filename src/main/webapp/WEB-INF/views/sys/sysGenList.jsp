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
			<li class="active"><a href="/sys/generatorWebController/list">数据表</a></li>
		</ul>
		<sys:message content="${message }"/>
    	<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
		<thead class="sys-font">
			<tr>
				<th>表名</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="tableName">
			<tr>
				<td>${tableName }</td>
				<td>
    				<a href="/sys/generatorWebController/make?tableName=${tableName }" onclick="return confirm('确定生成该表吗？原有文件会被覆盖！')">生成全部</a>&nbsp;&nbsp;
    				<a href="/sys/generatorWebController/make?tableName=${tableName }&type=mapper" onclick="return confirm('确定生成该表吗？原有文件会被覆盖！')">生成mapper</a>&nbsp;&nbsp;
    				<a href="/sys/generatorWebController/make?tableName=${tableName }&type=entity" onclick="return confirm('确定生成该表吗？原有文件会被覆盖！')">生成entity</a>&nbsp;&nbsp;
    				<a href="/sys/generatorWebController/make?tableName=${tableName }&type=dao" onclick="return confirm('确定生成该表吗？原有文件会被覆盖！')">生成dao</a>&nbsp;&nbsp;
    				<a href="/sys/generatorWebController/make?tableName=${tableName }&type=return" onclick="return confirm('确定生成该表吗？原有文件会被覆盖！')">生成return</a>&nbsp;&nbsp;
    				<a href="/sys/generatorWebController/make?tableName=${tableName }&type=service" onclick="return confirm('确定生成该表吗？原有文件会被覆盖！')">生成service</a>&nbsp;&nbsp;
    				<a href="/sys/generatorWebController/make?tableName=${tableName }&type=controller" onclick="return confirm('确定生成该表吗？原有文件会被覆盖！')">生成controller</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
  	</body>
</html>
