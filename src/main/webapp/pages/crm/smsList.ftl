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
		<div data-options="region:'north',title:'短信营销',split:true" style="height: 100px;">
			<form id="searchForm" method="post">
				<table style="margin-top: 15px">
					<tr>
	                	<td>发送时间：</td>
						<td>
							<input id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'searchEndTime\')}'})" value="" />
							 ~ <input id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'searchStartTime\')}'})" value="" /></td>
		                <td>
	                    <td>短信ID：</td>
	                    <td><input name="id" id="id"></td>
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

<!-- 统计对应短信的数据 -->
<div id="smsDataDiv" class="easyui-dialog" style="width:500px;height:300px;padding:15px 20px;">
	<div>发送短信数据：<label id="totalCount"></label></div>
	<div>页面点击数据：<label id="accessCount"></label></div>
	<div>页面点击比例：<label id="accessRatio"></label></div>
</div>

<script>
	$(function() {
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/crm/smsList',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'短信ID', width:15, align:'center'},
                {field:'content',    title:'短信内容', width:80, align:'center'},
                {field:'createTime',    title:'发送时间', width:30, align:'center'},
                {field:'totalCount',    title:'发送数量', width:20, align:'center'},
                {field:'hidden',  title:'操作', width:20, align:'center',
					formatter : function(value, row, index) {
						return '<a href="javascript:void(0);" onclick="showDetail(' + row.id + ')">查看数据</a>';
					}
				} ] ],
			toolbar : [ {
				id : '_add',
				text : '新增短信',
				iconCls : 'icon-add',
				handler : function() {
					window.open('${rc.contextPath}/crm/toSms');
				}
			}],
			pagination : true
		});
		
		$('#smsDataDiv').dialog({
			title : '查看数据',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '关闭',
				align : 'left',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#smsDataDiv').dialog('close');
				}
			} ]
		});
	});
	
	function showDetail(id) {
		$.messager.progress();
		$.ajax({
			type : "POST",
			url : '${rc.contextPath}/crm/statisticsResult/' + id,
			success : function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					$('#totalCount').text(data.data.totalCount);
					$('#accessCount').text(data.data.accessCount);
					$('#accessRatio').text(((Number(data.data.accessCount / data.data.totalCount)).toFixed(4)) * 100 + '%');
					$('#smsDataDiv').dialog('open');
				} else {
					$.messager.alert('提示信息', data.msg, 'error');
				}
			}
		});
	}
</script>

</body>
</html>