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
<div data-options="region:'center',title:'合伙人管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'',split:true" style="height: 100px;padding-top:10px">
        	<table>
				<tr>
					<td>用户Id：</td>
					<td><input name="searchAccountId" id="searchAccountId"/></td>
					<td>用户名：</td>
					<td><input name="searchAccountName" id="searchAccountName"/></td>
					<td>真实姓名：</td>
					<td><input name="searchRealName" id="searchRealName"/></td>
					<td>审核状态：</td>
					<td>
						<select name="searchAuditStatus" id="searchAuditStatus">
							<option value="-1">全部</option>
							<option value="2">待审核</option>
							<option value="3">未通过</option>
							<option value="4">已通过</option>
						</select>
					</td>
					<td>&nbsp;<a id="searchBtn" onclick="searchPartner();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
					<td>&nbsp;&nbsp;<a id="clearBtn" onclick="clearSearchForm();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >重置</a></td>
				</tr>
			</table>
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
		auditStatus:$("#searchAuditStatus").val(),
		realName:$("#searchRealName").val()
	});
}

function clearSearchForm(){
	$("#searchAccountId").val('');
	$("#searchAccountName").val('');
	$("#searchAuditStatus").find("option").eq(0).attr("selected","selected");
	$("#searchRealName").val('');
	$('#s_data').datagrid('load',{});
}

function updateStatus(id, status){
	var confirm = '';
	if(status == 4){
		confirm = '确定通过吗？';
	}else{
		confirm = '确定拒绝吗？';
	}
	$.messager.confirm('提示',confirm,function(r){
	    if (r){
	    	$.messager.progress();
			$.ajax({
		           url: '${rc.contextPath}/partner/updatePartnerApplyStatus',
		           type: 'post',
		           dataType: 'json',
		           data: {'accountId':id,'status':status},
		           success: function(data){
		           	$.messager.progress('close');
		               if(data.status == 1){
		               	$('#s_data').datagrid('load');
		               }else{
		               	$.messager.alert("提示",data.msg,"info");
		               }
		           },
		           error: function(xhr){
		           	$.messager.progress('close');
		           	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		           }
		       });
	    }
	});
}

$(function(){

	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/partner/jsonPartnerApplyInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
            {field:'createTime',    title:'申请时间', width:40, align:'center'},
            {field:'updateTime',  title:'最新更新时间',  width:40,  align:'center'},
            {field:'userId',    title:'用户Id', width:20, align:'center'},
            {field:'username',  title:'用户名',  width:30,  align:'center'},
            {field:'realname',    title:'真实姓名', width:30, align:'center'},
            {field:'auditStatus',    title:'状态', width:20, align:'center'},
            {field:'age', title:'年龄', width:20, align:'center'},
            {field:'mobileNumber', title:'常用手机号', width:40, align:'center'},
            {field:'wxAccount', title:'微信号', width:50, align:'center'},
            {field:'wxFriendsAmount', title:'微信好友数量', width:30, align:'center'},
            {field:'job',   title:'职业', width:30, align:'center'},
            {field:'monthlyIncome',   title:'月收入', width:30, align:'center'},
            {field:'reason',   title:'自荐理由', width:50, align:'center'},
            {field:'hidden',  title:'操作', width:40,align:'center',
                formatter:function(value,row,index){
                    if(row.applyStatus==2){
                    	var a = '<a href="javaScript:;" onclick="updateStatus(' + row.userId + ',' + 4 + ')">通过</a>';
                    	var b = ' | <a href="javaScript:;" onclick="updateStatus(' + row.userId + ',' + 3 + ')">拒绝</a>';
                    	return a + b;
                    }else{
                    	return '';
                    }
                }
            }
        ]],
     	pagination:true
    });   
});
</script>

</body>
</html>