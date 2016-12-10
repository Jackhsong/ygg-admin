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
		<form id="searchForm" action="${rc.contextPath}/account/exportAccount" method="post">
			<table>
				<tr>
					<td>用户ID</td>
					<td><input id="searchId" name="id" value="" /></td>
					<td>用户名</td>
					<td><input id="searchName" name="name" value="" /></td>
					<td>昵称</td>
					<td><input id="searchNickname" name="nickname" value="" /></td>
					<td>手机号</td>
					<td><input id="searchPhone" name="mobileNumber" value="" /></td>
				</tr>
				<tr>
					<td class="searchName">创建时间：</td>
		            <td class="searchText">
						<input value="" id="startTimeBegin" name="startTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
						-
						<input value="" id="startTimeEnd" name="startTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
					</td>
					<td></td>
					<td><a id="searchBtn" onclick="searchAccount()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
					<td><a id="exportBtn" onclick="exportAccount()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出</a></td>
					<td></td>
				</tr>
			</table>
	</div>

	<div title="用户管理" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>
	
	<div id="editPwd_div" class="easyui-dialog" style="width:350px;height:200px;padding:20px 20px;">
            <form id="af" method="post">
			<input id="editId" type="hidden" name="id" value="" >
            <table cellpadding="5">
                <tr>
                    <td>新密码:</td>
                    <td><input class="" id="pwd" type="text" name="pwd"></input></td>
                </tr>    
                <tr>
                    <td>确认新密码:</td>
                    <td><input class="" id="pwd1" type="text" name="pwd1"></input></td>
                </tr>
            </table>
        </form>
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

	function searchAccount() {
		$('#s_data').datagrid('load', {
			id : $("#searchId").val(),
			name : $("#searchName").val(),
			nickname : $("#searchNickname").val(),
			mobileNumber : $("#searchPhone").val(),
			startTimeBegin : $("#startTimeBegin").val(),
			startTimeEnd : $("#startTimeEnd").val()
		});
	}

	function editPwd(index){
    	var arr=$("#s_data").datagrid("getData");
    	$("#editId").val(arr.rows[index].id);
    	$("#pwd").val("");
    	$("#pwd1").val("");
    	$("#editPwd_div").dialog('open');
	}
	
	function exportAccount(){
		$("#searchForm").submit();
	}

	$(function(){
	
		$('#editPwd_div').dialog({
            title:'修改密码',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var editId=$('#editId').val();
                	var pwd=$('#pwd').val();
                	var pwd1=$('#pwd1').val();
                	if(pwd != pwd1){
                		$.messager.alert('提示',"两次输入的密码不一致","error")
                	}else{
                        $.ajax({
                            url: '${rc.contextPath}/account/updatePWD',
                            type: 'post',
                            dataType: 'json',
                            data: {editId:editId,pwd:pwd,pwd1:pwd1},
                            success: function(data){
                                if(data.status == 1){
                                    $("#editPwd_div").dialog('close');
                                    $.messager.alert('提示',"修改成功","info")
                                }else{{
                                    $.messager.alert('提示',data.msg,"error")
                                }}
                            },
                            error: function(xhr){
								if(xhr.status == 200)
								{
                                    $.messager.alert("提示",'无操作权限',"info");
								}
								else
								{
                                    $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
								}
                            }
                        });
                	}
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editPwd_div').dialog('close');
                }
            }]
        });
	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/account/jsonInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:50, align:'center'},
                {field:'createTime',    title:'创建时间', width:70, align:'center'},
                {field:'name',    title:'用户名', width:70, align:'center'},
                {field:'type',     title:'用户类型',  width:50,   align:'center' },
                {field:'nickname',    title:'昵称', width:70, align:'center'},
                {field:'mobileNumber',    title:'手机号码', width:70, align:'center'},
                {field:'totalAmount',    title:'累计成交金额', width:40, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="editPwd(' + index + ')">修改密码</a>';
                        return lableStr;
                    }
                }
            ]],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>