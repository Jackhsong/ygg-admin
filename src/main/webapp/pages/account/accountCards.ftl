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

<div data-options="region:'center',title:'content'" style="padding:5px;">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
		<form id="searchForm" method="post">
			<table>
				<tr>
					<td>用户名</td>
					<td><input id="accountName" name="accountName" value="" /></td>
					<td>真实姓名</td>
					<td><input id="cardName" name="cardName" value="" /></td>
					<td><a id="searchBtn" onclick="searchAccount()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
				</tr>
			</table>
	</div>

	<div title="用户管理" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>

</div>

<script>

	function searchAccount() {
		$('#s_data').datagrid('load', {
			accountName : $("#accountName").val(),
			cardName : $("#cardName").val()
		});
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/account/accountCardsJsonInfo',
            queryParams:{data:0},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'accountId',    title:'用户ID', width:30, align:'center'},
                {field:'name',    title:'用户名', width:40, align:'center'},
                {field:'typeStr',    title:'账号类型', width:40, align:'center'},
                {field:'cardName',     title:'真实姓名',  width:40,   align:'center' },
                {field:'bankTypeStr',    title:'银行', width:40, align:'center'},
                {field:'bankCardNumber',    title:'银行卡号', width:60, align:'center'},
                {field:'alipayCardNumber',    title:'支付宝账号', width:60, align:'center'}
            ]],
            pagination:true
        });
	})
</script>

</body>
</html>