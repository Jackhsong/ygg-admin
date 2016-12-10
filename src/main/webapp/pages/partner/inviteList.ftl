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
<div data-options="region:'center',title:'邀请关系查询'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'',split:true" style="height: 100px;padding-top:10px">
        	<table>
				<tr>
					<td>邀请人Id：</td>
					<td><input name="userId" id="userId"/></td>
					<td>邀请人用户名：</td>
					<td><input name="username" id="username"/></td>
					<td>被邀请人Id：</td>
					<td><input name="invitedId" id="invitedId"/></td>
					<td>被邀请人用户名：</td>
					<td><input name="invitedName" id="invitedName"/></td>
					<td>邀请码：</td>
					<td><input name="invitedCode" id="invitedCode"/></td>
					<td>&nbsp;<a id="searchBtn" onclick="searchPartner();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
					<td>&nbsp;&nbsp;<a id="clearBtn" onclick="clearSearchForm();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >重置</a></td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
		  	<table id="s_data" style=""></table>
		  	
		    <!-- 新增 begin -->
		    <div id="editInviteRaltionDiv" class="easyui-dialog" style="width:400px;height:200px;padding:20px 20px;">
		        <form id="inviteRaltionForm" method="post">
		        	<input type="hidden" name="type" id="inviteRaltionForm_type" value=""/>
					<p>
						<span id="parentTip">&nbsp;邀请人Id：</span>
						<span>
							<input type="text" name="parentAccount" id="inviteRaltionForm_parent" onblur="stringTrim(this)"/>
						</span>
						<font color="red">*</font>
					</p>
					<p>
						<span id="childTip">被邀请人Id：</span>
						<span>
							<input type="text" name="invitedAccount" id="inviteRaltionForm_current" onblur="stringTrim(this)"/>
						</span>
						<font color="red">*</font>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->		  	
		  	
		</div>
	</div>
</div>
<script>

function stringTrim(obj){
	var value = $(obj).val();
	$(obj).val($.trim(value));
}

function searchPartner(){
	$('#s_data').datagrid('load',{
		userId:$("#userId").val(),
		username:$("#username").val(),
		inviteId:$("#invitedId").val(),
		inviteName:$("#invitedName").val(),
		inviteCode:$("#invitedCode").val()
	});
}

function clearSearchForm(){
	$("table input").each(function(){
		$(this).val('');
	});
	$('#s_data').datagrid('load',{});
}

function cleanEditInviteRaltionDiv(){
	$("#inviteRaltionForm_type").val("");
	$("#inviteRaltionForm input[type='text']").each(function(){
		$(this).val('');
	});
}

$(function(){

	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/partner/jsonInviteRelationInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
            {field:'userId',    title:'邀请人用户Id', width:40, align:'center'},
            {field:'username',  title:'用户名',  width:50,  align:'center'},
            {field:'identity',  title:'身份',  width:50,  align:'center'},
            {field:'realname',  title:'真实姓名',  width:50,  align:'center'},
            {field:'inviteCode',    title:'邀请码', width:50, align:'center'},
            {field:'inviteId', title:'被邀请人用户Id', width:40, align:'center'},
            {field:'invitedName', title:'被邀请人用户名', width:50, align:'center'},
            {field:'createTime', title:'被邀请用户注册时间', width:60, align:'center'},
        ]],
        toolbar:[
           {
               id:'_add',
               text:'按用户Id创建邀请关系',
               iconCls:'icon-add',
               handler:function(){
               	cleanEditInviteRaltionDiv();
               	$("#inviteRaltionForm_type").val("1");
               	$("#parentTip").html("&nbsp;邀请人Id：");
               	$("#childTip").html("被邀请人Id：");
               	$('#editInviteRaltionDiv').dialog('open');
               }
           },'-',{
               id:'_add',
               text:'按用户名创建邀请关系',
               iconCls:'icon-add',
               handler:function(){
               	cleanEditInviteRaltionDiv();
               	$("#inviteRaltionForm_type").val("2");
               	$("#parentTip").html("&nbsp;邀请人用户名：");
               	$("#childTip").html("被邀请人用户名：");
               	$('#editInviteRaltionDiv').dialog('open');
               }
           }
        ],
     	pagination:true
    });
	
	
    $('#editInviteRaltionDiv').dialog({
    	title:'创建邀请关系',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#inviteRaltionForm').form('submit',{
    				url: "${rc.contextPath}/partner/createInviteRelation",
    				onSubmit:function(){
    					var parentAccountId = $("#inviteRaltionForm_parent").val();
    					var invitedAccountId = $('#inviteRaltionForm_current').val();
    					if($.trim(parentAccountId) == ''){
    						$.messager.alert("提示","请填写邀请人","warn");
    						return false;
    					}else if($.trim(invitedAccountId) == ''){
    						$.messager.alert("提示","请填写被邀请人","warn");
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
                                $('#editInviteRaltionDiv').dialog('close');
                            });
                        }else if(res.status == 2){
                        	$.messager.alert('响应信息',"邀请人不存在或不是手机号注册用户",'info');
                        }else if(res.status == 3){
                        	$.messager.alert('响应信息',"被邀请人不存在或不是手机号注册用户",'info');
                        }else if(res.status == 4){
                        	$.messager.alert('响应信息',"被邀请人是合伙人不能再次被邀请",'info');
                        }else if(res.status == 5){
                        	$.messager.alert('响应信息',"被邀请人已经填写过邀请码不能再次被邀请",'info');
                        }else if(res.status == 6){
                        	$.messager.alert('响应信息',"邀请关系已经存在不能再次被邀请",'info');
                        }else if(res.status == 7){
                        	$.messager.alert('响应信息',"被邀请人已经邀请过邀请人，不能相互邀请",'info');
                        }else if(res.status == 0){
                            $.messager.alert('响应信息',res.msg,'error');
                        } 
    				},
    				error: function(xhr){
			         	$.messager.progress('close');
			        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
			       }
    			});
    		}
    	},
    	{
    		text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#editInviteRaltionDiv').dialog('close');
            }
    	}]
    }); 
});
</script>

</body>
</html>