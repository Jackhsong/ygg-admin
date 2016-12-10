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
		<div data-options="region:'center',title:'条件筛选-用户积分变动明细'" >
			<div style="padding-left:20px">
				<p>用户ID：${account.id}</p>
				<p>用户名：${account.name}</p>
				<p>用户类型：${account.typeStr}</p>
				<p>积分余额：${account.integral}</p>
			</div>
			<!--数据表格-->
    		<table id="s_data" style=""></table>
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
        url:'${rc.contextPath}/account/jsonIntegralRecord?id='+${id},
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,100],
        columns:[[
            {field:'typeStr',    title:'变动类型', width:50, align:'center'},
            {field:'operatePoint',     title:'积分',  width:35,   align:'center' },
            {field:'totalPoint',    title:'积分余额', width:30, align:'center'},
            {field:'createTime',    title:'更新时间', width:50, align:'center'}
        ]],
        pagination:true
    });
});
</script>

</body>
</html>