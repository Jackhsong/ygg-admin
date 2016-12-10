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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'center',title:'用户分销订单明细'" >
			<br/>
			<div style="padding-left:20px">
				<span>用户ID：${account.id}</span>&nbsp;&nbsp;
				<span>用户名：${account.name}</span>&nbsp;&nbsp;
				<span>用户类型：${account.typeStr}</span>&nbsp;&nbsp;
				<span>积分余额：${account.integral}</span>
			</div>
			<br/>
			<!--数据表格-->
    		<table id="s_data"></table>
		</div>
        
	</div>
</div>

<script>

$(function(){
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/partner/jsonOrderDetail?id='+${id},
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,100],
        columns:[[
            {field:'isInvitedStr',    title:'小伙伴类型', width:30, align:'center'},
            {field:'orderStatusStr',    title:'订单状态', width:30, align:'center'},
            {field:'orderNumber',    title:'订单号', width:50, align:'center',
            	formatter : function(value, row, index) {
					var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.orderId+'">'+row.orderNumber+'</a>';
					return a;
				}
            },
            {field:'accountId',    title:'用户Id', width:50, align:'center'},
            {field:'accountName',    title:'用户名', width:50, align:'center'},
            {field:'payTime',    title:'付款时间', width:40, align:'center'},
            {field:'sendTime',    title:'发货时间', width:40, align:'center'},
            {field:'realPrice',    title:'实付金额', width:40, align:'center'},
            {field:'point',    title:'分销积分', width:40, align:'center'}
        ]],
        pagination:true
    });
});
</script>

</body>
</html>