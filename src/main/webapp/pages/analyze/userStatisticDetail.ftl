<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<style>
	
</style>
</head>
<body class="easyui-layout">
<!-- 
<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
	<#include "../common/menu.ftl" >
</div>
 -->
         
<div data-options="region:'center',title:'content'" style="padding:5px;">
	
	<div id="searchDiv" class="datagrid-toolbar" style="height: 100px; padding: 10px">
		 <p>注册时间： ${startTime}&nbsp;~&nbsp;${endTime}</p>
		<p>业务线：${type}&nbsp;&nbsp;&nbsp;&nbsp;
		平台：${platform}&nbsp;&nbsp;&nbsp;&nbsp;
		渠道：${appChannel}</p>
		<p>注册用户数： ${registUserCount}&nbsp;&nbsp;&nbsp;&nbsp;
		订单用户数: ${orderUserCount}</p>
		<p>累计成交金额：${totalPriceDisplay}</p>
	</div>
	
	<!-- 数据表格 -->
	<table class="table table-bordered table-striped" width="100%">
		<tr>
			<th align="center">成交日期</th>
			<th align="center">成交金额</th>
		</tr>	
		<#list  rows as r >
			<tr>
				<td >${r.createTime}</td>
				<td >${r.totalPrice}</td>
			</tr>
		</#list>
	</table>
	
</div>
</body>
</html>