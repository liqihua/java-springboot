<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
  <head>
  	<script type="text/javascript" src="${ctxStatic }/jquery/jquery-1.8.3.min.js"></script>
  	<script type="text/javascript">
  		function getQueryString(name) { 
	  		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	  		var r = window.location.search.substr(1).match(reg); 
	  		if (r != null) return unescape(r[2]); return null; 
		}
  		
  		$(document).ready(function(){
  			// 网页js开发：获取微信url中的code（获取用户信息需要）
  			var code = getQueryString("code");
  			var state = getQueryString("state");
  			if(code != '' && code != null && code != 'null' && code != 'undefined'){
  				// 根据code获取用户信息
  				$.ajax( {
					url : "/项目名/weixin/indexWeixinController/getUserInfoByCode",
					data : {code:code},
					type : "POST",
					dataType : "json",
					success : function(data) {
						alert(data);
						JSON.stringify(data);
					},error: function(XMLHttpRequest, textStatus, errorThrown) {
						alert("error : getUserInfoByCode() ");
		            }
				});
  			}
  		});
  	</script>
  </head>
  <body>
    getCode.jsp
  </body>
</html>
