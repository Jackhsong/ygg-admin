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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<input id="groupId" type="hidden" value="${groupId }"/>
		<div data-options="region:'center'" >
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
            url:'${rc.contextPath}/crm/filterDetailList/' + $('#groupId').val(),
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'accountId',    title:'用户ID', width:30, align:'center'},
                {field:'createTime',    title:'创建时间', width:70, align:'center'},
                {field:'name',    title:'用户名', width:70, align:'center'},
                {field:'type',    title:'用户类型', width:40, align:'center',
                	formatter : function(value, row, index) {
                		if(row.type == 1)
                			return '手机';
                		else if(row.type == 2)
                			return 'QQ';
                		else if(row.type == 3)
                			return '微信';
                		else if(row.type == 4)
                			return '新浪';
                		else if(row.type == 5)
                			return '支付宝';
                		else if(row.type == 6)
                			return '微信拼团';
                		else if(row.type == 7)
                			return 'APP拼团';
                		else if(row.type == 8)
                			return '心动慈露
';
                		else if(row.type == 9)
                			return '燕网';
                		else
                			return type;
                	}
                },
                {field:'nickname',    title:'昵称', width:70, align:'center'},
                {field:'phone',    title:'手机号码', width:70, align:'center'}
                ]],
                toolbar : [ {
    				id : '_export',
    				text : '导出',
    				iconCls : 'icon-add',
    				handler : function() {
    					window.open('${rc.contextPath}/crm/filterDetailList/' + $('#groupId').val() + '?export=1');
    				}
    			} ],
			pagination : true
		});
	});
</script>

</body>
</html>