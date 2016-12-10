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
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center',title:'返积分记录'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'',split:true" style="height: 100px;padding-top:10px">
			<form id="searchForm" action="${rc.contextPath}/partner/exportReturnIntegral" method="post">
	        	<table>
					<tr>
						<td>用户Id：</td>
						<td><input name="searchAccountId" id="searchAccountId"/></td>
						<td>用户名：</td>
						<td><input name="searchAccountName" id="searchAccountName"/></td>
						<td>被邀请人用户名：</td>
						<td><input name="searchInvitedName" id="searchInvitedName"/></td>
						<td>返积分类型：</td>
						<td>
							<select name="searchReturnType" id="searchReturnType">
								<option value="-1">全部</option>
								<option value="1">首次下单</option>
								<option value="2">重复下单</option>
							</select>
						</td>
						<td>&nbsp;<a id="searchBtn" onclick="searchPartner();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
						<td>&nbsp;&nbsp;<a id="clearBtn" onclick="clearSearchForm();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >重置</a></td>
						<td>&nbsp;&nbsp;<a id="searchBtn" onclick="exportIntegral()" href="javascript:;" class="easyui-linkbutton" >导出查询结果</a></td>
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
function searchPartner(){
	$('#s_data').datagrid('load',{
		userId:$("#searchAccountId").val(),
		username:$("#searchAccountName").val(),
		invitedName:$("#searchInvitedName").val(),
		returnType:$("#searchReturnType").val()
	});
}

function clearSearchForm(){
	$("#searchAccountId").val('');
	$("#searchAccountName").val('');
	$("#searchInvitedName").val('');
	$("#searchReturnType").find("option").eq(0).attr("selected","selected");
	$('#s_data').datagrid('load',{});
}

function exportIntegral(){
	$("#searchForm").submit();
}

$(function(){

	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/partner/jsonReturnIntegralInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
            {field:'userId',    title:'用户Id', width:20, align:'center'},
            {field:'username',  title:'用户名',  width:50,  align:'center'},
            {field:'identity',    title:'身份', width:50, align:'center'},
            {field:'inviteCode',     title:'邀请码',  width:50,  align:'center'},
            {field:'invitedName',     title:'被邀请人用户名',  width:50,  align:'center'},
            {field : 'hidden', title : '订单编号', width : 50, align : 'center',
				formatter : function(value, row, index) {
					var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.orderId+'">'+row.orderNumber+'</a>';
					return a;
				}
			},
			{field:'statusDesc',    title:'订单状态', width:50, align:'center'},
			{field:'totalPrice',    title:'订单总价', width:50, align:'center'},
			{field:'realPrice',    title:'实付金额', width:50, align:'center'},
            {field:'returnType',    title:'返积分类型', width:50, align:'center'},
            {field:'point', title:'积分数量', width:30, align:'center'},
            {field:'createTime', title:'积分变动时间', width:50, align:'center'}
        ]],
     	pagination:true
    });   
});
</script>

</body>
</html>