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
		<form id="searchDivForm" action="${rc.contextPath}/analyze/userBehaviorAnalyze" method="post">
			<table>
				<tr>
					<td>前一段时间：</td>
					<td>
						<input value="${prevStartTime}" id="prevStartTime" name="prevStartTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'2015-03',maxDate:'#F{$dp.$D(\'prevEndTime\')}'})"/>
						~
						<input value="${prevEndTime}" id="prevEndTime" name="prevEndTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'prevStartTime\')}',maxDate:'#F{$dp.$D(\'nextStartTime\',{d:-1})}'})"/>
					</td>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>后一段时间：</td>
					<td>
						<input value="${nextStartTime}" id="nextStartTime" name="nextStartTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'prevEndTime\',{d:1})}',maxDate:'#F{$dp.$D(\'nextEndTime\')}'})"/>
						~
						<input value="${nextEndTime}" id="nextEndTime" name="nextEndTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'nextStartTime\')}'})"/>
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
			<th align="center">购物次数</th>
			<th align="center">${prevStartTime}~${prevEndTime}<br/>人数</th>
			<th align="center">人数占比</th>
			<th align="center">总成交额</th>
			<th align="center">金额占比</th>
			<th align="center">人均成交金额</th>
			<th align="center">${nextStartTime}~${nextEndTime}<br/>购买人数</th>
			<th align="center">总成交额</th>
			<th align="center">操作</th>
		</tr>	
		<#list  rows as r >
			<tr>
				<td >${r.times}</td>
				<td >${r.userCount}</td>
				<td >${r.userCountPercent}</td>
				<td >${r.totalAmount}</td>
				<td >${r.totalAmountPercent}</td>
				<td >${r.averagePrice}</td>
				<td >${r.nextUserCount}</td>
				<td >${r.nextTotalAmount}</td>
				<td ><a href="javascript:void(0);" onclick="viewDetail(${r.times},${r.userCount},'${r.prevStartTime}','${r.prevEndTime}','${nextStartTime}','${nextEndTime}')">查看详情</a></td>
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
	$('#searchDivForm').attr("action", "userBehaviorAnalyze").submit();
}

function exportAll() {
	$('#searchDivForm').attr("action", "exportUserBehaviorAnalyze").submit();
}

function viewDetail(times,userCount,prevStartTime,prevEndTime,nextStartTime,nextEndTime){
	var url = '${rc.contextPath}/analyze/userBehaviorDetailAnalyze?times='+times+'&userCount='+userCount+'&prevStartTime='+prevStartTime+'&prevEndTime='+prevEndTime+'&nextStartTime='+nextStartTime+'&nextEndTime='+nextEndTime;
	window.open(url,'target=_blank');
}
</script>

</body>
</html>