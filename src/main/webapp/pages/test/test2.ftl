<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/bootstrap/js/bootstrap.min.js"></script>

<style type="text/css">
	#myCss span {
		font-size:12px;
		padding-right:20px;
	}
</style>

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<form action="" id="testForm">
		appKey:<input type="text" name="appKey" value="Birdex0000001"/>
		eventTime:<input type="text" name="eventTime" value="2015-01-01 00:00:00"/>
		occurTime:<input type="text" name="occurTime" value="2015-01-01 00:00:00"/>
		logisticsId:<input type="text" name="logisticsId" value="LP0001-3-1"/>
		<input type="button" id="submit" value="测试"/>
	</form>
</div>

<script>
	$(function(){
		$("#submit").click(function(){
			var data1={"eventTime":"2015-01-01 00:00:00","appKey":"Birdex0000001"};
			var saveDataAry=[]; 
	        saveDataAry.push(data1);  
			$.ajax({
				url:'${rc.contextPath}/test/testjson',
				type:'post',
				contentType : "application/json;charset=UTF-8",
				data:JSON.stringify(data1),
				dataType:'json',
				success:function(data){
						$.messager.alert('响应消息',data.msg,'info');
				},
				error:function(xhr){
					$.messager.progress('close');
		            $.messager.alert("提示",'服务器忙，请稍后再试，errorCode='+xhr.status,"info");
				}
			});
		});
	});
</script>

</body>
</html>