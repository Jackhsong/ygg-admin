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
		<form id="searchDivForm" action="${rc.contextPath}/analyze/appVersionBuyAnalyze" method="post">
			<table>
				<tr>
					<td>查询日期</td>
					<td><input value="${selectDate}" id="selectDate" name="selectDate" onClick="WdatePicker({dateFmt: 'yyyy-MM',minDate:'2015-03'})"/></td>
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="searchAll()" href="javascript:;" class="easyui-linkbutton" >查询</a></td>
				</tr>
			</table>
		<form>
	</div>
	<br/>
	
	<!-- 数据表格 -->
	<table class="table table-bordered table-striped" width="100%">
		<tr>
			<th >日期</th>
			<th >IOS1.8</th>
			<th >IOS1.7</th>
			<th >IOS1.6</th>
			<th >IOS马甲1.7</th>
			<th >IOS马甲1.5</th>
			<th >Android1.8</th>
			<th >Android1.7</th>
			<th >Android1.6</th>
			<th >web</th>
		</tr>
		<#list  rows as r >
			<tr>
				<td >${r.dateStr}</td>
				<td >${r.IOS18}</td>
				<td >${r.IOS17}</td>
				<td >${r.IOS16}</td>
				<td >${r.IOS马甲17}</td>
				<td >${r.IOS马甲15}</td>
				<td >${r.Android18}</td>
				<td >${r.Android17}</td>
				<td >${r.Android16}</td>
				<td >${r.web}</td>
			</tr>
		</#list>
		<tr>
			<td >总计</td>
			<td >${lastRow.tIOS18}</td>
			<td >${lastRow.tIOS17}</td>
			<td >${lastRow.tIOS16}</td>
			<td >${lastRow.tIOS马甲17}</td>
			<td >${lastRow.tIOS马甲15}</td>
			<td >${lastRow.tAndroid18}</td>
			<td >${lastRow.tAndroid17}</td>
			<td >${lastRow.tAndroid16}</td>
			<td >${lastRow.tweb}</td>
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
	$('#searchDivForm').attr("action", "appVersionBuyAnalyze").submit();
}

function exportAll() {
	$('#searchDivForm').attr("action", "exportNewAccountBuyingAnalyze").submit();
}
</script>

</body>
</html>