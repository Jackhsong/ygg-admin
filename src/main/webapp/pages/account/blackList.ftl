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
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<div data-options="region:'center',title:'用户黑名单'" style="padding:5px;">

    <!--数据表格-->
    <table id="s_data" style=""></table>
    
	<div id="addAccountDiv" class="easyui-dialog" style="width:500px;height:190px;padding:20px 20px;">
       <form id="addAccountForm" method="post" enctype="multipart/form-data">
	        <table cellpadding="5">
	            <tr>
	                <td>用户ID:</td>
	                <td>
						<input id="accountId" style="width:300px"  type="number"></input>
					</td>
	            </tr>
	            <tr>
	                <td>备注:</td>
	                <td>
                        <input id="remark" style="width:300px" ></input>
	                </td>
	            </tr>
	        </table>
   		</form>
   	</div> 
</div>

<script>
	function deleteAccount(accountId){
		$.messager.confirm('移除','确定移除吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/account/deleteBlacklist',
		            type: 'post',
		            dataType: 'json',
		            data: {'accountId':accountId},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
		                	$('#s_data').datagrid('reload');
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
            url:'${rc.contextPath}/account/ajaxAccountBlacklist',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect : true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'accountId',    title:'用户ID', width:30, align:'center'},
                {field:'accountName',    title:'用户名', width:40, align:'center'},
                {field:'partnerStatus',    title:'身份', width:40, align:'center'},
                {field:'recommendedCode',    title:'邀请码', width:40, align:'center'},
                {field:'freezeTime',    title:'冻结时间', width:40, align:'center'},
                {field:'freezeRemark',    title:'冻结备注', width:40, align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                    	return '<a href="javaScript:;" onclick="deleteAccount(' + row.accountId + ')">移除黑名单</a>';
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                    var accountId = $("#accountId").val("");
                    var remark = $("#remark").val("");
                    $("#addAccountDiv").dialog("open");
                }
            }],
            pagination:true
        });
        
        
        $('#addAccountDiv').dialog({
            title:'新增',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    var accountId = $("#accountId").val();
                    var remark = $("#remark").val();
                    if(accountId == '' || remark == '')
                    {
                        $.messager.alert("提示","请填写完整信息","error");
                        return;
                    }
                    $.messager.progress();
                    $.ajax({
                        url: '${rc.contextPath}/account/addBlacklist',
                        type: 'post',
                        dataType: 'json',
                        data: {'accountId':accountId,'remark':remark},
                        success: function(data){
                            $.messager.progress('close');
                            if(data.status == 1){
                                $('#s_data').datagrid('load');
                                $("#addAccountDiv").dialog("close");
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
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $("#addAccountDiv").dialog("close");
                }
            }]
        });       
	});
</script>

</body>
</html>