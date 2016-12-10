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
		<form id="searchDivForm" action="${rc.contextPath}/analyze/sellerSendTimeAnalyze" method="post">
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
			<th >商家Id</th>
			<th >商家真实名称</th>
			<th >发货地</th>
			<th >分仓</th>
			<th >报关顺序</th>
			<th >付款订单总量</th>
			<th >0.5天</th>
			<th >1天</th>
			<th >1.5天</th>
			<th >2天</th>
			<th >3天</th>
			<th >4天</th>
			<th >5天</th>
			<th >6天</th>
			<th >7天</th>
			<th >8天及更多</th>
			<th >未发货</th>
		</tr>
		<#list  rows as r >
			<tr>
				<td >${r.sellerId}</td>
				<td >${r.sellerName}</td>
				<td >${r.sendAddress}</td>
				<td >${r.warehouse}</td>
				<td >${r.bondedNumberType}</td>
				<td ><#if r.totalAmount?exists && (r.totalAmount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','0','0')">${r.totalAmount}</a><#else>${r.totalAmount}</#if></td>
				<td ><#if r.dayOfHalfAmount?exists && (r.dayOfHalfAmount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','0','12')">${r.dayOfHalfAmount}</a><#else>${r.dayOfHalfAmount}</#if></td>
				<td ><#if r.dayOf1Amount?exists && (r.dayOf1Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','12','24')">${r.dayOf1Amount}</a><#else>${r.dayOf1Amount}</#if></td>
				<td ><#if r.dayOf15Amount?exists && (r.dayOf15Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','24','36')">${r.dayOf15Amount}</a><#else>${r.dayOf15Amount}</#if></td>
				<td ><#if r.dayOf2Amount?exists && (r.dayOf2Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','36','48')">${r.dayOf2Amount}</a><#else>${r.dayOf2Amount}</#if></td>
				<td ><#if r.dayOf3Amount?exists && (r.dayOf3Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','48','72')">${r.dayOf3Amount}</a><#else>${r.dayOf3Amount}</#if></td>
				<td ><#if r.dayOf4Amount?exists && (r.dayOf4Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','72','96')">${r.dayOf4Amount}</a><#else>${r.dayOf4Amount}</#if></td>
				<td ><#if r.dayOf5Amount?exists && (r.dayOf5Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','96','120')">${r.dayOf5Amount}</a><#else>${r.dayOf5Amount}</#if></td>
				<td ><#if r.dayOf6Amount?exists && (r.dayOf6Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','120','144')">${r.dayOf6Amount}</a><#else>${r.dayOf6Amount}</#if></td>
				<td ><#if r.dayOf7Amount?exists && (r.dayOf7Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','144','168')">${r.dayOf7Amount}</a><#else>${r.dayOf7Amount}</#if></td>
				<td ><#if r.dayOf8Amount?exists && (r.dayOf8Amount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','168','')">${r.dayOf8Amount}</a><#else>${r.dayOf8Amount}</#if></td>
				<td ><#if r.notSendAmount?exists && (r.notSendAmount !='0')><a href="javascript:void(0);" onclick="viewOrderDetail('${r.sellerId}','-1','-1')">${r.notSendAmount}</a><#else>${r.notSendAmount}</#if></td>
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
	$('#searchDivForm').attr("action", "sellerSendTimeAnalyze").submit();
}

function exportAll() {
	$('#searchDivForm').attr("action", "exportSellerSendTimeAnalyze").submit();
}

function viewOrderDetail(sellerId,beginHour,endHour){
	var url = '${rc.contextPath}/order/sellerSendTimeAnalyzeDetail?payTime=${selectDate}&sellerId='+sellerId+'&beginHour='+beginHour+"&endHour="+endHour;
	window.open(url,'_blank');
}
</script>

</body>
</html>