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
					<td>邀请码：</td>
					<td><input name="searchInviteCode" id="searchInviteCode"/></td>
					<td>真实姓名：</td>
					<td><input name="searchRealName" id="searchRealName"/></td>
					<td>&nbsp;<a id="searchBtn" onclick="searchPartner();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
					<td>&nbsp;&nbsp;<a id="clearBtn" onclick="clearSearchForm();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >重置</a></td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
		  	<table id="s_data" style=""></table>
	
		    <!-- 新增 -->
		    <div id="addPartnerDiv" class="easyui-dialog" style="width:500px;height:250px;padding:20px 20px;">
		        <form id="addPartnerForm" method="post">
			        <table cellpadding="5">
			            <tr>
			                <td>用户名：</td>
			                <td>
			                	<input type="text" name="username" id="addPartnerForm_username" value="" maxlength="25"/><span>注：仅支持手机用户</span>
			                </td>
			            </tr>
			            <tr>
			                <td>微信号：</td>
			                <td>
			                	<input type="text" name="wxAccount" id="addPartnerForm_wxAccount" value="" maxlength="16"/>
			                </td>
			            </tr>
			            <tr>
			                <td>真实姓名：</td>
			                <td>
			                	<input type="text" name="realName" id="addPartnerForm_realName" value="" maxlength="16"/>
			                </td>
			            </tr>
			            <tr>
			                <td>备注：</td>
			                <td>
			                	<input type="text" name="remark" id="addPartnerForm_remark" value="" maxlength="16"/>
			                </td>
			            </tr>
			        </table>
		    	</form>
		    </div>
		</div>
	</div>
</div>
<script>
function searchPartner(){
	$('#s_data').datagrid('load',{
		userId:$("#searchAccountId").val(),
		username:$("#searchAccountName").val(),
		inviteCode:$("#searchInviteCode").val(),
		realName:$("#searchRealName").val()
	});
}

function clearSearchForm(){
	$("#searchAccountId").val('');
	$("#searchAccountName").val('');
	$("#searchInviteCode").val('');
	$("#searchRealName").val('');
	$('#s_data').datagrid('load',{});
}

function cleanAddForm(){
	$("#addPartnerForm input").each(function(){
		$(this).val('');
	});
}

function updateStatus(id, isAvailable){
	var confirm = '';
	if(isAvailable == 3){
		confirm = '确定要取消该用户的合伙人资格吗？';
	}else{
		confirm = '确定要恢复该用户的合伙人资格吗？';
	}
	$.messager.confirm('提示',confirm,function(r){
	    if (r){
	    	$.messager.progress();
			$.ajax({
		           url: '${rc.contextPath}/partner/updatePartnerStatus',
		           type: 'post',
		           dataType: 'json',
		           data: {'accountId':id,'status':isAvailable},
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
        url:'${rc.contextPath}/partner/jsonPartnerInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
            {field:'userId',    title:'用户Id', width:20, align:'center'},
            {field:'username',  title:'用户名',  width:40,  align:'center'},
            {field:'inviteCode',     title:'邀请码',  width:30,  align:'center'},
            {field:'realname',    title:'真实姓名', width:40, align:'center'},
            {field:'remark',    title:'备注', width:60, align:'center'},
            {field:'age', title:'年龄', width:20, align:'center'},
            {field:'mobileNumber', title:'常用手机号', width:50, align:'center'},
            {field:'wxAccount', title:'微信号', width:50, align:'center'},
            {field:'wxFriendsAmount', title:'微信好友数量', width:40, align:'center'},
            {field:'job',   title:'职业', width:30, align:'center'},
            {field:'monthlyIncome',   title:'月收入', width:30, align:'center'},
            {field:'reason',   title:'自荐理由', width:50, align:'center'},
            {field:'hidden',  title:'操作', width:40,align:'center',
                formatter:function(value,row,index){
                    if(row.partnerStatus==2){
                    	return '<a href="javaScript:;" onclick="updateStatus(' + row.userId + ',' + 3 + ')">取消合伙人资格</a>';
                    }
                    else if(row.partnerStatus==3){
                    	return '<a href="javaScript:;" onclick="updateStatus(' + row.userId + ',' + 2 + ')">恢复合伙人资格</a>';
                    }
                }
            }
        ]],
        toolbar:[
        {
            id:'_add',
            text:'手动增加合伙人',
            iconCls:'icon-add',
            handler:function(){
            	cleanAddForm();
            	$('#addPartnerDiv').dialog('open');
            }
        }],
     	pagination:true
    });
    
    $('#addPartnerDiv').dialog({
    	title:'手动增加合伙人',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '设置该用户为合伙人',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#addPartnerForm').form('submit',{
    				url: "${rc.contextPath}/partner/addPartnerByHandle",
    				onSubmit:function(){
    					var username = $("form input[name='username']").val();
    					var wxAccount = $("form input[name='wxAccount']").val();
    					var realName = $("form input[name='realName']").val();
    					if($.trim(username) == ''){
    						$.messager.alert("提示","请输入用户名","warn");
    						return false;
    					}
    					if($.trim(wxAccount) == ''){
    						$.messager.alert("提示","请输入微信号","warn");
    						return false;
    					}
    					if($.trim(realName) == ''){
    						$.messager.alert("提示","请输入真实姓名","warn");
    						return false;
    					}
    					$.messager.progress();
    				},
    				success: function(data){
    					$.messager.progress('close');
                        var res = eval("("+data+")");
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                                $('#s_data').datagrid('reload');
                                $('#addPartnerDiv').dialog('close');
                            });
                        } else if(res.status == 0){
                            $.messager.alert('响应信息',res.msg,'info');
                        } 
    				}
    			});
    		}
    	},
    	{
    		text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#addPartnerDiv').dialog('close');
            }
    	}]
    });     
});
</script>

</body>
</html>