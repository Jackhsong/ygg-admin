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
<script src="${rc.contextPath}/pages/js/commonUtil.js"></script>
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<style>
	
</style>
</head>
<body class="easyui-layout">
<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
	
	<div id="searchDiv" class="datagrid-toolbar" style="height: 30px; padding: 15px">
		<form id="searchDivForm"   method="post">
		<input type="hidden"  id="nodeId" name="nodeId"/>
			<table>
				<tr>
					<td>查询日期</td>
			<td>
					<input value="${startTime}" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})"/>
						~
					<input value="${endTime}" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})"/></td>
						
					<td>&nbsp;&nbsp;<a  onclick="searchAll()" href="javascript:;" class="easyui-linkbutton" >查询</a></td>
					<td>&nbsp;&nbsp;<a  onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a></td>
				</tr>
			</table>
		<form>
	</div>
	<br/>

	<!-- 数据表格 -->
	<table class="table table-bordered table-striped" width="100%">
		<tr>
			<th >拼团商品ID</th>
			<th >商品名称</th>
			<th >创建拼团数</th>
			<th >创建订单数</th>
			<th >创建金额</th>
			<th >成功拼团数</th>
			<th >成功订单数</th>
			<th >成功金额</th>
			<th >拼团成功率</th>
		</tr>

	<#list  list as r >
			<tr>
				<td >${r.mwebGroupProductId}</td>
				<td >${r.name}</td>
				<td >${r.createGroupCount}</td>
				<td >${r.createOrderCount}</td>
				<td >${r.createRealPrice}</td>
				<td >${r.successGroupCount}</td>
				<td >${r.successOrderCount}</td>
				<td >${r.successRealPrice}</td>
		        <td >${r.successRate}</td>
			</tr>
  </#list>
		<tr>
				<td >总计</td>
			    <td >${totalProductCount}</td>
				<td >${totalCreateGroupCount}</td>
				<td >${totalCreateOrderCount}</td>
				<td >${totalCreateRealPrice}</td>
				<td >${totalSuccessGroupCount}</td>
				<td >${totalSuccessOrderCount}</td>
		        <td >${totalSuccessRealPrice}</td>
		        <td >${totalSuccessRate}</td>
		
		</tr>
	</table>
	
</div>
<script>
var nodeId=$.getUrlParam('nodes');
$('#nodeId').val(nodeId);
function searchAll() {
	$('#searchDivForm').attr("action", "${rc.contextPath}/wechatGroupData/productDateList?nodes="+nodeId).submit();
}

function exportAll() {
	$('#searchDivForm').attr("action", "${rc.contextPath}/wechatGroupData/exportProductDateList").submit();
}
</script>

</body>
</html>