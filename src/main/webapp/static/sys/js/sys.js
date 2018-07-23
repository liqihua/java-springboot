$(document).ready(function(){
	// 表单字段校验
	$("#inputForm").validate({
		errorPlacement:function(error,element){
			if(element.parent().children(".sys-error-info").length < 1){
				element.parent().append("&nbsp;&nbsp;&nbsp;<span class='label label-important sys-error-info'>必填信息</span>");
			}
		}
	});
	// 把表单的select标签变漂亮，同时具备检索功能（jquery-select2）
	$("select").each(function(){
		$(this).select2();
	});
	// 系统图片选择器的叉叉
	$(".a-sys-img-select-append").live("click",function(){
		var inputId = $(this).attr("title");
		var imgSrc = $(this).siblings(".img-sys-img-select-append").attr("src");
		var newValue = $("#"+inputId).val().replace(imgSrc+",",'').replace(imgSrc,'');
		$("#"+inputId).val(newValue);
		$(this).parent(".div-sys-img-select-append").remove();
	});
	
	//系统zTree回显
	$(".sys-hidden-input").each(function(){
		//如果模态框不存在，就在当前页面添加一个模态框
		if($(".sys-tree-model").length < 1){
			var htmlModel = "";
			htmlModel += "<div id='sys-tree-model' class='modal hide fade sys-tree-model' tabindex='-1' style='width:350px;'>";
			htmlModel += "<div class='modal-header'>";
			htmlModel += "<button type='button' class='close' data-dismiss='modal'>×</button>";
			htmlModel += "<h4>选择列表</h4>";
			htmlModel += "</div>";
			htmlModel += "<div class='modal-body'>";
			htmlModel += "<ul id='ul-sys-tree' class='ztree'></ul>";
			htmlModel += "</div>";
			htmlModel += "<input id='hiddenInputId' type='hidden' value='' />";
			htmlModel += "</div>";
			$("body").append(htmlModel);
		};
		//回显zTree
		$hiddenInput = $(this);
		var id = $(this).attr("id");
		var url = $(this).attr("url");
		if(notBlank(url)){
			var oldValue = $(this).val();
			if(notBlank(oldValue)){
				$.ajax( {
					url : url,
					type : "post",
					dataType : "json",
					success : function(data) {
						var oldName = getTreeName(oldValue,data.resultData);
						$("input[title='"+id+"']").val(oldName);
					},error: function(XMLHttpRequest, textStatus, errorThrown) {
						console.log("error : 回显zTree -> ajax");
			        }
				});
			}
		}
	});
	
	//表单提交Loading效果
	$(".sys-form-loading").submit(function(){
		var valid = $(this).valid();
		if(valid){
			inLoading("btn-form-submit");
		}
	});
	
	//页码跳转
	$(".pageGo").click(function(){
		var pageSize = $(this).attr("pageSize");
		var page = $(this).parent().prev().children("input").val();
		if(notBlank(page) && !isNaN(page)){
			var first = (parseInt(page) - 1)*pageSize;
			var firstInput = "<input type='hidden' name='first' value='"+first+"'/>";
			$("#searchForm").prepend(firstInput);
			$("#searchForm").submit();
		}
	});
});

//根据类名替换成Loading图片
function inLoading(className){
	$("."+className).before("<img src='/static/sys/img/loading-1.gif' class='img-form-loading'/>");
	$("."+className).remove();
}

//回显zTree，根据id找出list中的name
function getTreeName(id,list){
	if(notBlank(id) && list != null && list.length > 0){
		for(var i=0;i<list.length;i++){
			if(id == list[i].id){
				return list[i].name;
			}
			var childName = getTreeName(id,list[i].children);
			if(childName != null && childName != ''){
				return childName;
			}
		}
	}
	return "";
}

//系统zTree按钮被点击
function sysTreeClick(url,hiddenInputId){
	$("#hiddenInputId").val(hiddenInputId);
	var treeList = [];
	var treeSetting = {callback:{beforeDblClick: sysTreeCallBack}};
	$.ajax( {
		url : url,
		type : "post",
		dataType : "json",
		success : function(data) {
			if(data != null && data.resultData != null && data.resultData.length > 0){
				for(var i=0;i<data.resultData.length;i++){
					treeList.push(data.resultData[i]);
				}
			}
			$.fn.zTree.init($("#ul-sys-tree"), treeSetting, treeList);
		},error: function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("error : sysTreeClick -> ajax");
        }
	});
}
	
//系统zTree双击回调函数
function sysTreeCallBack(treeId, treeNode){
	if(treeNode != null && treeNode.id != null && typeof(treeNode.id) != "undefined"){
		var hiddenInputId = $("#hiddenInputId").val();
		$("#"+hiddenInputId).val(treeNode.id);
		$("#sys-tree-model").modal("hide");
		$("#"+hiddenInputId).next().val(treeNode.name);
	}
}

// 菜单zTree双击回调函数
function sysMenuTreeCallBack(treeId, treeNode){
	if(typeof(treeNode.id) != "undefined"){
		$(".select-sys-menu-display").val(treeNode.name);
		var inputId = $(".select-sys-menu-display").attr("title");
		$("#"+inputId).val(treeNode.id);
		$('#sysMenuModel').modal('hide');
	}
}

// 去除空格
function trim(str){
	if(typeof(str) != 'undefined'){
		var reg=new RegExp(' ',"gm");
		return str.replace(reg,'');
	}
	return '';
}

// 判断非空
function notBlank(str){
	if(typeof(str) != 'undefined' && str != null){
		str = trim(str);
		if(str != '' && str != 'null'){
			return true;
		}
	}
	return false;
}

// 获取浏览器url参数
function getUrlParam(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
}

// 打开系统图片选择器
function sysImgSelect(inputName,multiSelect){
	if(notBlank(inputName)){
		window.open("/sys/sysCommonWebController/sysImgList?inputName="+inputName+"&multiSelect="+multiSelect,"win-sys-uploadImg","width=965,height=550,top=200,left=200,location=no,toolbar=no,menubar=no,status=no");
	}
}

// 根据id清空表单值
function clearInput(inputId){
	var obj = document.getElementById(inputId);
	if(obj != null ){
		obj.value = "";
	}
}

//打开系统文件选择器
function sysFileSelect(inputName){
	if(notBlank(inputName)){
		window.open("/sys/sysCommonWebController/sysFileList?inputName="+inputName,"win-sys-uploadFile","width=965,height=550,top=200,left=200,location=no,toolbar=no,menubar=no,status=no");
	}
}

// 省份选择器ajax
function provinceChange(){
	var pid = $("#provinceId").val();
	$.ajax( {
		url : "/ajax/sysAreaAjaxController/findCityList",
		data : {provinceId:pid},
		type : "post",
		dataType : "json",
		success : function(data) {
			var option = "<option value='' selected='selected'></option>";
			for(var i=0;i<data.resultData.length;i++){
				option += "<option value='"+data.resultData[i].id+"'>"+data.resultData[i].name+"</option>";
			}
			$("#cityId").html(option);
			$("#cityId").select2();
		},error: function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("error : provinceChange");
			console.log(XMLHttpRequest+","+textStatus+","+errorThrown);
        }
	});
}

// 城市选择器ajax
function cityChange(){
	var cid = $("#cityId").val();
	$.ajax( {
		url : "/ajax/sysAreaAjaxController/findDistrictList",
		data : {cityId:cid},
		type : "post",
		dataType : "json",
		success : function(data) {
			var option = "<option value='' selected='selected'></option>";
			for(var i=0;i<data.resultData.length;i++){
				option += "<option value='"+data.resultData[i].id+"'>"+data.resultData[i].name+"</option>";
			}
			$("#districtId").html(option);
			$("#districtId").select2();
		},error: function(XMLHttpRequest, textStatus, errorThrown) {
			console.log("error : cityChange");
			console.log(XMLHttpRequest+","+textStatus+","+errorThrown);
        }
	});
}

// 系统分页组件点击事件
function pageClick(first){
	var firstInput = "<input type='hidden' name='first' value='"+first+"'/>";
	$("#searchForm").prepend(firstInput);
	$("#searchForm").submit();
}

//判断一个字符串是否为小数
function isDouble(s) {
	if(s.split(".").length != 2){
		return false;
	}
	var reg = new RegExp("\\d+\\.\\d+$|-\\d+\\.\\d+$");
	return reg.test(s);
}

