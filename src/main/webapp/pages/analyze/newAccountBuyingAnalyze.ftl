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
		<form id="searchDivForm" action="${rc.contextPath}/analyze/newAccountBuyingAnalyze" method="post">
			<table>
				<tr>
					<td>查询日期</td>
					<td><input value="${selectDate}" id="selectDate" name="selectDate" onClick="WdatePicker({dateFmt: 'yyyy-MM',minDate:'2015-03'})"/></td>
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="searchAll()" href="javascript:;" class="easyui-linkbutton" >查询</a></td>
					<td>&nbsp;&nbsp;<a id="exportBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a></td>
				</tr>
			</table>
		<form>
	</div>
	<br/>
	
	<!-- 数据表格 -->
	<table class="table table-bordered table-striped" width="100%">
		<tr>
			<th >日期</th>
			<th >成交人数</th>
			<th >其中首次成交用户人数</th>
			<th >其中老客成交人数</th>
			<th >首次成交用户人数占比</th>
			<th >成交金额</th>
			<th >其中首次成交用户金额</th>
			<th >其中老客成交金额</th>
			<th >首次成交用户金额占比</th>
			<th >成交订单数</th>
			<th >其中首次成交用户订单数</th>
			<th >成交商品数</th>
			<th >其中首次成交用户商品数</th>
		</tr>
		<#list  rows as r >
			<tr>
				<td >${r.dateStr}</td>
				<td >${r.totalPersonCount}</td>
				<td >${r.newPersonCount}</td>
				<td >${r.oldPersonCount}</td>
				<td >${r.newPersonCountDivtotalPersonCount}</td>
				<td >${r.totalPrice}</td>
				<td >${r.newPersonTotalPrice}</td>
				<td >${r.oldPersonTotalPrice}</td>
				<td >${r.newPersonTotalPriceDivtotalPrice}</td>
				<td >${r.totalOrderCount}</td>
				<td >${r.newOrderCount}</td>
				<td >${r.totalProductCount}</td>
				<td >${r.newProductCount}</td>
			</tr>
		</#list>
	</table>
	
</div>
<script>
$(function(){
	$(document).keydown(function(e){
		if (!e){
		   e = window.event;  
		}  
		if ((e.keyCode || e.which) == 13) {  
		      $("#searchBtn").click();  
		}
	});
});

function searchAll() {
	$('#searchDivForm').attr("action", "newAccountBuyingAnalyze").submit();
}

function exportAll() {
	$('#searchDivForm').attr("action", "exportNewAccountBuyingAnalyze").submit();
}
</script>

</body>
</html>