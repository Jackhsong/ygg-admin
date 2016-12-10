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
		<div data-options="region:'north',title:'用户分组',split:true" style="height: 100px;">
			<form id="searchForm" method="post">
				<table cellpadding="15">
					<tr>
	                	<td>总用户数：</td>
						<td><input id="startCount" name="startCount"/> ~ <input id="endCount" name="endCount"/></td>
	                    <td>分组ID：</td>
		                <td><input id="groupId" name="groupId" /></td>
		                <td>产品线：</td>
		                <td><select class="easyui-combobox" id="type" name="type">
		                		<option value="-1">全部</option>
		                		<option value="0">换吧网络</option>
		                		<option value="1">心动慈露</option>
		                		<option value="2">心动慈露
</option>
		                	</select>
		                </td>
		                <td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',width:'80px'">查 询</a>
	                	</td>
	                </tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
		</div>
	</div>
</div>

<script>
	$(function(){
		$('#searchBtn').click(function() {
			$('#s_data').datagrid('load', {
				startCount:$('#startCount').val(),
				endCount:$('#endCount').val(),
				groupId:$('#groupId').val(),
				type:$('#type').combobox('getValue')
			});
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/crm/filterList',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'分组ID', width:30, align:'center'},
                {field:'title',    title:'分组标题', width:70, align:'center'},
                {field:'accountCount',    title:'分组总用户数', width:30, align:'center'},
                {field:'phoneCount',    title:'手机号码用户数', width:70, align:'center'},
                {field:'type',    title:'产品线', width:70, align:'center',
                	formatter : function(value, row, index) {
                		if(row.type == 0)
                			return '换吧网络';
                		else if(row.type == 1)
                			return '心动慈露';
                		else if(row.type == 2)
                			return '心动慈露
';
                		else
                			return type;
                	}
                },
                {field:'hidden',  title:'操作', width:20, align:'center',
					formatter : function(value, row, index) {
						var a = '<a href="javascript:void(0);" onclick="detail(' + row.id + ')">查看</a>';
						a += '&nbsp;&nbsp;<a href="javascript:void(0);" onclick="del(' + row.id + ')">删除</a>';
						return a;
					}
				}]],
			pagination : true
		});
	});
	
	function detail(id) {
		window.location.href="${rc.contextPath}/crm/toFilterDetailList?groupId=" + id;
	}
	
	function del(id) {
		$.messager.confirm('确认对话框', '您想要删除该分组信息吗？', function(r){
			if (r){
				$.ajax({
					type : "POST",
					url : '${rc.contextPath}/crm/delete/' + id,
					success : function(data) {
						$('#s_data').datagrid('reload');
					}
				});
			}
		});
	}
</script>

</body>
</html>