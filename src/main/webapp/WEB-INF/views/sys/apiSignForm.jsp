<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
  	<head>
    	<title>签名工具</title>
    	<%@ include file="/WEB-INF/views/include/style.jsp"%>
  	</head>
  	<body>
  		<div class="body-wrap">
  			<div class="form-actions">
	  			<input id="doSign" class="btn btn-info" type="submit" value="执行"/>&nbsp;
	  			<input id="add" class="btn" type="button" value="添加" />&nbsp;
			</div>
	  		<div class="control-group">
				<input type="text" class="input-medium param-name" placeholder="参数名">
				<input type="text" class="input-xlarge param-value" placeholder="参数值">
			</div>
  		</div>
  		参数：<br><textarea id="textarea-show-param" rows="3" style="width:800px;" readOnly="true"></textarea><br>
		签名结果：<br><textarea id="textarea-show-value" rows="10" style="width:800px;" readOnly="true"></textarea>
		<script type="text/javascript">
		$(document).ready(function(){
			$("#add").click(function(){
				var str = "";
				str += "<div class='control-group'>";
				str += "<input type='text' class='input-medium param-name' placeholder='参数名'>";
				str += "<input type='text' class='input-xlarge param-value' placeholder='参数值'>";
				str += "</div>";
				$(".body-wrap").append(str);
			});
			$("#doSign").click(function(){
				var param = "";
				$(".control-group").each(function(){
					var name = $(this).children(".param-name").val();
					var value = $(this).children(".param-value").val();
					if(notBlank(name) && notBlank(value)){
						param += (name+"e0d509dcab1b4835a795899e48c63684"+value+"b90a128f6ae54256bddb84dc998c29e0");
					}
				});
				$.ajax({
					url : "${ajaxUrl }",
					type : "post",
					data : {param:param},
					dataType : "json",
					success : function(data) {
						$("#textarea-show-param").val(data.resultData.param);
						$("#textarea-show-value").val(data.resultData.signStr);
					},error: function(XMLHttpRequest, textStatus, errorThrown) {
						console.log("error : /publicWebController/doSign");
		            }
				});
			});
		});
		</script>
  	</body>
</html>
