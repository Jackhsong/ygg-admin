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
<style type="text/css">
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	text-align:justify;
}
.search{
	width: 1200px;
	align:center;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'日志管理',split:true" style="height: 120px;">
			<form id="searchForm" method="post">
				<table class="search">
					<tr>
						<td class="searchName">用户名：</td>
						<td class="searchText"><input id="username" name="username" value="" /></td>
						<td class="searchName">操作内容：</td>
						<td class="searchText"><input id="content" name="content"/></td>
						<td class="searchName">业务模块：</td>
						<td class="searchText"><input id="businessType" name="businessType" value="" /></td>
						<td class="searchName">操作类型：</td>
						<td class="searchText"><input id="operationType" name="operationType" value=""/></td>
					</tr>
					<tr>
					</tr>
					<tr>
						<td class="searchName">操作时间：</td>
						<td class="searchText">
							<input value="" id="createTimeBegin" name="createTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
							-
							<input value="" id="createTimeEnd" name="createTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
						</td>
						<td class="searchName">日志级别：</td>
						<td class="searchText"><input id="level" name="level" value=""/></td>
						<td class="searchName"></td>
						<td class="searchText">
							<a id="searchBtn" onclick="searchSeller()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
						</td>
						<td class="searchName"></td>
						<td class="searchText"></td>
						
					</tr>
				</table>
			<form>
		</div>
		<div data-options="region:'center'" >
			<table id="s_data">

			</table>
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

	function searchSeller() {
		$('#s_data').datagrid('load', {
			username : $("#username").val(),
			businessType : $("input[name='businessType']").val(),
			operationType : $("input[name='operationType']").val(),
			content : $("#content").val(),
			createTimeBegin : $("#createTimeBegin").val(),
			createTimeEnd : $("#createTimeEnd").val(),
			level:$("input[name='level']").val()
		});
	}
	
	function clearSearch(){
		$("#username").val('');
		$("#businessType").combobox('clear');
		$("#operationType").combobox('clear');
		$("#content").val('');
		$("#createTimeBegin").val('');
		$("#createTimeEnd").val('');
		$("#level").combobox('clear');
		$('#s_data').datagrid('load', {});
	}

	$(function(){
		$("#businessType").combobox({   
		    url:'${rc.contextPath}/log/jsonBusinessTypeCode',   
		    valueField:'code',   
		    textField:'text'  
		});
		
		$("#operationType").combobox({   
		    url:'${rc.contextPath}/log/jsonOperationTypeCode',   
		    valueField:'code',   
		    textField:'text'  
		});
		
		$("#level").combobox({
			url:'${rc.contextPath}/log/jsonLevelCode',   
		    valueField:'code',   
		    textField:'text'
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/log/jsonSystemLogInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', width:20, align:'center'},
                {field:'username',    title:'用户名', width:30, align:'center'},
                {field:'level',    title:'日志级别', width:40, align:'center'},
                {field:'businessType',    title:'业务模块', width:40, align:'center'},
                {field:'operationType',    title:'操作类型', width:40, align:'center'},
                {field:'content',    title:'操作内容', width:100, align:'center'},
                {field:'createTime',     title:'操作时间',  width:40,   align:'center' },
            ]],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>