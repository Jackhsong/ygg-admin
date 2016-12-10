<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>商家结算毛利统计</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<link href="${rc.contextPath}/pages/css/table.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
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
		<div data-options="region:'north',title:'商家结算毛利统计',split:true" style="height: 100px;padding-top:10px">
			<form id="searchDivForm" method="post">
			<table>
				<tr>
					<td>结算导入时间&nbsp;&nbsp;月份</td>
					<td><input value="${selectDate}" id="selectDate" name="selectDate" onClick="WdatePicker({dateFmt: 'yyyy-MM',minDate:'2015-03'})"/></td>
					<td>日期</td>
					<td>
						<select name="start" id="start" style="width:120px">
							<#list dateList as item>
								<option value="${item}" >${item}</option>
							</#list>
						</select>
						日
						~
						<select name="stop" id="stop" style="width:120px">
							<#list dateList as item>
								<option value="${item}" >${item}</option>
							</#list>
						</select>
						日
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
	$('#s_data').datagrid('load', {
		selectDate : $("#selectDate").val(),
		start : $("#start").val(),
		stop : $("#stop").val()
	});
}

function exportData() {
	$('#searchDivForm').attr("action", "exportSellerGrossSettlement").submit();
}

function export1(i) {
	var href = "${rc.contextPath}/finance/exportOrderFinanceData?orderType=1,2&source=analyze&settlementStatus=1";
	var selectDate = $("#selectDate").val();
    var start = $("#start").val();
    var stop = $("#stop").val();
    href = href + "&selectDate=" + selectDate;
    href = href + "&start=" + start;
    href = href + "&stop=" + stop;
    href = href + "&sellerId=" + i;
	console.log(href);
    window.location.href=href;
}

$(function(){
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/analyze/jsonSellerGrossSettlement',
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
            {field:'sendAddress',    title:'发货地', width:40, align:'center'},
            {field:'totalSalesPrice',    title:'订单商品总价', width:40, align:'center'},
            {field:'totalCost',    title:'订单供货总价', width:40, align:'center'},
            {field:'totalFreight',    title:'订单结算运费', width:40, align:'center'},
            {field:'totalGross',    title:'扣除运费毛利', width:40, align:'center'},
            {field:'totalGrossDivTotalSalesPrice',    title:'毛利率', width:40, align:'center'},
            {field:'hidden',  title:'操作', width:80,align:'center',
                formatter:function(value,row,index){
                    var a = '<a href="javaScript:;" onclick="export1(' + row.sellerId + ')">导出明细</a>';
                    return a;
                }
            }
        ]]
    });
});

</script>

</body>
</html>