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
		<form id="searchDivForm" action="${rc.contextPath}/analyze/eachDayOrderLogisticAnalyze" method="post">
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
			<th >发货订单总量</th>
			<th >0.5天</th>
			<th >1天</th>
			<th >2天</th>
			<th >3天</th>
			<th >4天</th>
			<th >5天及更多</th>
			<th >尚无物流信息</th>
		</tr>
		<#list  rows as r >
			<tr>
				<td >${r.date}</td>
				<td ><#if r.totalAmount?exists && (r.totalAmount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.date}','0')">${r.totalAmount}</a><#else>${r.totalAmount}</#if></td>
				<td ><#if r.dayOfHalfAmount?exists && (r.dayOfHalfAmount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.date}','12')">${r.dayOfHalfAmount}</a><#else>${r.dayOfHalfAmount}</#if></td>
				<td ><#if r.dayOf1Amount?exists && (r.dayOf1Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.date}','24')">${r.dayOf1Amount}</a><#else>${r.dayOf1Amount}</#if></td>
				<td ><#if r.dayOf2Amount?exists && (r.dayOf2Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.date}','48')">${r.dayOf2Amount}</a><#else>${r.dayOf2Amount}</#if></td>
				<td ><#if r.dayOf3Amount?exists && (r.dayOf3Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.date}','72')">${r.dayOf3Amount}</a><#else>${r.dayOf3Amount}</#if></td>
				<td ><#if r.dayOf4Amount?exists && (r.dayOf4Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.date}','96')">${r.dayOf4Amount}</a><#else>${r.dayOf4Amount}</#if></td>
				<td ><#if r.dayOf5Amount?exists && (r.dayOf5Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.date}','120')">${r.dayOf5Amount}</a><#else>${r.dayOf5Amount}</#if></td>
				<td ><#if r.noLogisticsAmount?exists && (r.noLogisticsAmount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.date}','-1')">${r.noLogisticsAmount}</a><#else>${r.noLogisticsAmount}</#if></td>
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
	$('#searchDivForm').attr("action", "eachDayOrderLogisticAnalyze").submit();
}

function exportAll() {
	$('#searchDivForm').attr("action", "exportEachDayOrderLogisticAnalyze").submit();
}

function viewOrderDetail(date,beginHour){
	var url = '${rc.contextPath}/order/orderLogisticAnalyzeDetail?sendTime='+date+'&hour='+beginHour;
	window.open(url,'_blank');
}
</script>

</body>
</html>