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
		<form id="searchDivForm" action="${rc.contextPath}/analyze/todayUserRegist" method="post">
			<table>
				<tr>
					<td>查询日期</td>
					<td><input value="${selectDate}" id="selectDate" name="selectDate" onClick="WdatePicker({dateFmt: 'yyyy-MM',minDate:'2015-03'})"/></td>
                    <td>来源渠道</td>
                    <td>
                        <select name="channel">
                            <option value="">全部</option>
						<#list channelList as r>
                            <option <#if r.selected=="1" >selected</#if> value="${r.channel}">${r.name}</option>
						</#list>
                        </select>
                    </td>
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
			<th >新注册用户数</th>
			<th >手机用户</th>
			<th >微信用户</th>
			<th >QQ用户</th>
			<th >微博用户</th>
			<th >支付宝用户</th>
		</tr>
		<#list  rows as r >
			<tr>
				<td >${r.dateStr}</td>
				<td >${r.total}</td>
				<td >${r.mobile}</td>
				<td >${r.weixin}</td>
				<td >${r.qq}</td>
				<td >${r.weibo}</td>
				<td >${r.alipay}</td>
			</tr>
		</#list>
		<tr>
				<td >总计</td>
				<td >${lastRow.total}</td>
				<td >${lastRow.mobile}</td>
				<td >${lastRow.weixin}</td>
				<td >${lastRow.qq}</td>
				<td >${lastRow.weibo}</td>
				<td >${lastRow.alipay}</td>
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
	$('#searchDivForm').attr("action", "registAnalyze").submit();
}

function exportAll() {
	$('#searchDivForm').attr("action", "exportRegistAnalyze").submit();
}
</script>

</body>
</html>