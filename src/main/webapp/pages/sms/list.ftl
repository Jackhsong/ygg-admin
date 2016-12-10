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

<div data-options="region:'center',title:'content'" style="padding:5px;">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
		<form id="searchForm" method="post">
			<table>
				<tr>
					<td>发送时间</td>
					<td>
						<input value="" id="sendTimeBegin" name="sendTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
			 			~
			 			<input value="" id="sendTimeEnd" name="sendTimeEnd"  onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
					</td>
					<td>状态</td>
					<td>
						<select id="type" name="type">
							<option value="0">全部</option>
							<option value="1">注册验证码</option>
							<option value="2">忘记密码</option>
							<option value="3">自定义短信</option>
							<option value="4">发货短信</option>
							<option value="5">海外购节假日提醒短信</option>
							<option value="6">短信余额提醒</option>
							<option value="7">下单短信提醒</option>
							<option value="8">签收提醒短信</option>
						</select>
					</td>
					<td>手机号</td>
					<td>
						<input id="mobileNumber" />
					</td>
					<td><a id="searchBtn" onclick="searchSms()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
				</tr>
			</table>
	</div>

	<div title="短信内容管理" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>

</div>

<script>

	function searchSms() {
		$('#s_data').datagrid('load', {
			sendTimeBegin : $("#sendTimeBegin").val(),
			sendTimeEnd : $("#sendTimeEnd").val(),
			mobileNumber : $("#mobileNumber").val(),
			type : $("#type").val()
		});
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sms/jsonInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:70,
            pageList:[70,80],
            columns:[[
                {field:'typeStr',    title:'短信类型', width:50, align:'center'},
                {field:'serviceType',    title:'服务商类型', width:50, align:'center'},
                {field:'sendTimeStr',    title:'发送时间', width:50, align:'center'},
                {field:'mobileNumber',     title:'手机号',  width:50,   align:'center' },
                {field:'content',    title:'短信内容', width:200, align:'center'}
            ]],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>