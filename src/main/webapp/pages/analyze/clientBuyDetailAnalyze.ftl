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
	
	<div id="searchDiv" class="datagrid-toolbar" style="height: 30px; padding: 15px">
		查看日期：${selectDate}
	</div>
	<br/>
	
	<!-- 数据表格 -->
	<table class="table table-bordered table-striped" width="100%">
		<tr>
			<th >版本</th>
			<th >成交金额</th>
			<th >金额占比</th>
			<th >成交人数</th>
			<th >人数占比</th>
			<th >客单价</th>
			<th >成交订单数</th>
			<th >笔单价</th>
		</tr>
		<#list  rows as r >
			<tr>
				<td >${r.typeVersion}</td>
				<td >${r.totalPrice}</td>
				<td >${r.totalPricePercent}</td>
				<td >${r.totalPersonCount}</td>
				<td >${r.totalPersonCountPercent}</td>
				<td >${r.divPersonPrice}</td>
				<td >${r.totalOrderCount}</td>
				<td >${r.divOrderPrice}</td>
			</tr>
		</#list>
	</table>
	
</div>
</body>
</html>