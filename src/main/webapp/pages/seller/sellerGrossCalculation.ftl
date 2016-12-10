<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>商家毛利实时统计</title>
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
		<div data-options="region:'north',title:'商家毛利实时统计',split:true" style="height: 100px;padding-top:10px">
			<form id="searchDivForm" method="post">
			<table>
				<tr>
					<td>付款时间 ： </td>
					<td>
						<input id="startTime" name="startTime" value="${startTime}" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00'})"/>
						~
                        <input id="endTime" name="endTime" value="${endTime}" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',minDate:'#F{$dp.$D(\'startTime\')}'})"/>
					</td>
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="searchData()" href="javascript:;" class="easyui-linkbutton" style="width:100px" >查询</a></td>
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="exportData()" href="javascript:;" class="easyui-linkbutton" style="width:100px" >导出详细结果</a></td>
				</tr>
			</table>
		<form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style="">
            </table>
		</div>
	</div>
    <input type="hidden" value="1" id="sempN" />
</div>
<script>

function searchData() {
    if($("#startTime").val() == '' || $("#endTime").val() == ''){
        $.messager.alert("警告","请选择时间","error");
        return;
    }else{
        $("#sempN").val("1");
        var st = new Date($("#startTime").val()).getTime();
        var et = new Date($("#endTime").val()).getTime();
        if((et-st) > (86400000*32)){
            $.messager.alert("警告","最多允许查询31天的数据","error");
        }else{
            $('#s_data').datagrid('load', {
                startTime : $("#startTime").val(),
                endTime : $("#endTime").val()
            });
        }
    }
}

function exportData() {
    var sempN = $("#sempN").val();
    if(sempN == 1)
    {
        $("#sempN").val("0");
        var href = "${rc.contextPath}/seller/exportAllSellerGrossCalculation?";
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        href = href + "startTime=" + startTime;
        href = href + "&endTime=" + endTime;
        console.log(href);
        window.location.href=href;
    }
    else
    {
        $.messager.alert("警告","结果已经导出 或 正在进行导出...","error");
    }

}

function export1(i) {
    var href = "${rc.contextPath}/seller/exportSellerGrossCalculation?sellerId=" + i;
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    href = href + "&startTime=" + startTime;
    href = href + "&endTime=" + endTime;
    console.log(href);
    window.location.href=href;
}

$(function(){
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/seller/jsonSellerGrossCalculation',
        queryParams:{
        	search:0
        },
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        columns:[[
            {field:'sellerId',    title:'商家ID', width:40, align:'center'},
            {field:'realSellerName',    title:'真实商家名称', width:40, align:'center'},
            {field:'totalPayPrice',    title:'订单实付', width:40, align:'center'},
            {field:'totalSellerPrice',    title:'应付商家', width:40, align:'center'},
            {field:'totalFreight',    title:'模拟运费金额', width:40, align:'center'},
            {field:'totalpointProportion',    title:'积分优惠', width:40, align:'center'},
            {field:'totalcouponProportion',    title:'优惠券优惠', width:40, align:'center'},
            {field:'totaladjustProportion',    title:'客服调价', width:40, align:'center'},
            {field:'totalGross',    title:'净毛利', width:40, align:'center'},
            {field:'grossRate',    title:'净毛利率', width:40, align:'center'},
            {field:'hidden',  title:'操作', width:80,align:'center',
                formatter:function(value,row,index){
                    var a = '<a href="javaScript:;" onclick="export1(' + row.sellerId + ')">导出明细</a>';
                    return a;
                }
            }
        ]],
        onLoadError: function(){
            $.messager.alert("error","您的操作过于频繁，请稍后再试！","error");
        }
    });
});

</script>

</body>
</html>