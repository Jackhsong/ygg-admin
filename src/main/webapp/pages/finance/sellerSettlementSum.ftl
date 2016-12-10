<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>商家结算统计</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<style>
	
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'商家结算统计',split:true" style="height: 100px;padding-top:10px">
			<form id="searchDivForm" method="post">
			<table>
				<tr>
					<td>结算导入时间 ： </td>
					<td>
						<input id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
						~
                        <input id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"/>
					</td>
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="searchData()" href="javascript:;" class="easyui-linkbutton" style="width:100px" >查询</a></td>
					<td>&nbsp;&nbsp;<a id="exportBtn" onclick="exportData()" href="javascript:;" class="easyui-linkbutton" style="width:100px" >导出结果</a></td>
				</tr>
			</table>
		<form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
		</div>
	</div>
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

function searchData() {
    if($("#startTime").val() == '' || $("#endTime").val() == '')
    {
        $.messager.alert("info","请筛选时间","error");
        return;
    }
	$('#s_data').datagrid('load', {
        startTime : $("#startTime").val(),
        endTime : $("#endTime").val()
	});
}

function exportData() {
	if($("#startTime").val() == '' || $("#endTime").val() == '')
	{
		$.messager.alert("info","请筛选时间","error");
		return;
	}
	$('#searchDivForm').attr("action", "exportSellerSettlementSum").submit();
}

$(function(){
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/finance/jsonSellerSettlementSum',
        queryParams:{
        	search:0
        },
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        columns:[[
            {field:'sellerId',    title:'商家ID', width:40, align:'center'},
            {field:'realSellerName',    title:'商家名称', width:40, align:'center'},
            {field:'totalRealPrice',    title:'订单实付', width:40, align:'center'},
            {field:'totalCost',    title:'订单供货价', width:40, align:'center'},
            {field:'totalFreight',    title:'订单结算运费', width:40, align:'center'},
            {field:'totalSellerResponsibility',    title:'结算退款中商家承担部分', width:40, align:'center'},
            {field:'totalPaySeller',    title:'应付商家', width:40, align:'center'}
        ]]
    });
});

</script>

</body>
</html>