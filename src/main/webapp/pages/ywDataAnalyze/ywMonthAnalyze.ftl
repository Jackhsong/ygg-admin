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
		<form id="searchDivForm" action="${rc.contextPath}/ywanalyze/monthAnalyze" method="post">
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
			<th >创建订单数</th>
			<th >支付订单数</th>
			<th >支付成功率</th>
			<th >订单金额</th>
			<th >笔单价</th>
			<th >成交人数（已去重）</th>
			<th >客单价</th>
		</tr>
		<#list  rows as r >
			<tr>
				<td >${r.dateStr}</td>
				<td >${r.totalOrderCount}</td>
				<td >${r.payOrderCount}</td>
				<td >${r.divPayOrderCount}</td>
				<td >${r.totalPrice}</td>
				<td >${r.divOrderCountPrice}</td>
				<td >${r.totalPersonCount}</td>
				<td >${r.divPersonCountPrice}</td>
			</tr>
		</#list>
		<tr>
				<td >总计</td>
				<td ><#if lastRow.totalOrderCount?exists>${lastRow.totalOrderCount}<#else>0</#if></td>
				<td ><#if lastRow.payOrderCount?exists>${lastRow.payOrderCount}<#else>0</#if></td>
				<td ><#if lastRow.divPayOrderCount?exists>${lastRow.divPayOrderCount}<#else>0</#if></td>
				<td ><#if lastRow.totalPrice?exists>${lastRow.totalPrice}<#else>0</#if></td>
				<td ><#if lastRow.divOrderCountPrice?exists>${lastRow.divOrderCountPrice}<#else>0</#if></td>
				<td ><#if lastRow.totalPersonCount?exists>${lastRow.totalPersonCount}<#else>0</#if></td>
				<td ><#if lastRow.divPersonCountPrice?exists>${lastRow.divPersonCountPrice}<#else>0</#if></td>
		</tr>
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
	$('#searchDivForm').attr("action", "monthAnalyze").submit();
}

function exportAll() {
	$('#searchDivForm').attr("action", "exportMonthAnalyze").submit();
}
</script>

</body>
</html>