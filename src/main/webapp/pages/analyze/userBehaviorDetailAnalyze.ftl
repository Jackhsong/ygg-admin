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
	
	<div id="searchDiv" class="datagrid-toolbar" style="height: 40px; padding: 15px">
		<p>${prevStartTime}~${prevEndTime}，购买次数 <font color='red'>${times}</font> 次，人数 <font color='red'>${userCount}</font> </p>
		<p>${nextStartTime}~${nextEndTime}购买行为分析</p>
	</div>
	<br/>
	
	<!-- 数据表格 -->
	<table class="table table-bordered table-striped" width="100%">
		<tr>
			<th align="center">购物次数</th>
			<th align="center">购买人数</th>
			<th align="center">成交金额</th>
			<th align="center">平均每次购买金额</th>
		</tr>	
		<#list  rows as r >
			<tr>
				<td >${r.times}</td>
				<td >${r.userCount}</td>
				<td >${r.totalAmount}</td>
				<td >${r.averagePrice}</td>
			</tr>
		</#list>

	</table>
	
</div>
</body>
</html>