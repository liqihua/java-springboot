<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
	<head>
	    <title>API状态码列表</title>
	    <%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
	<body>
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead class="sys-font">
			<tr>
				<th>状态码</th>
				<th>结果</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${constanceMap }" var="map">
	    		<tr>
					<td>${map.key }</td>
					<td>${map.value }</td>
				</tr>
	    	</c:forEach>
		</tbody>
		</table>
    	
  	</body>
</html>
