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

<div data-options="region:'center',title:'content'" style="padding:5px;">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
		<form id="searchForm" method="post">
			<table>
				<tr>
					<td>用户名</td>
					<td><input id="searchName" name="searchName" value="" /></td>
					<td><a id="searchBtn" onclick="searchManager()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
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
            <form id="editPwd_form" method="post">
			<input id="editId" type="hidden" name="id" value="" >
            <table cellpadding="5">
                <tr>
                    <td>新密码:</td>
                    <td><input type="password" id="pwd" name="pwd"></input></td>
                </tr>    
                <tr>
                    <td>确认新密码:</td>
                    <td><input type="password" id="pwd1" name="pwd1"></input></td>
                </tr>
            </table>
        </form>
     </div>
	
	<div id="addManager_div" class="easyui-dialog" style="width:330px;height:400px;padding:20px 20px;">
		<form id="addManager_form" method="post">
            <table cellpadding="5">
                <tr>
                    <td>用户名:</td>
                    <td><input id="name" name="name" /></td>
                </tr>
                <tr>
                    <td>真实姓名:</td>
                    <td><input id="realname" name="realname" /></td>
                </tr>
                <tr>
                    <td>手机号:</td>
                    <td><input id="mobileNumber" name="mobileNumber" /></td>
                </tr>
                <tr>
                    <td>密码:</td>
                    <td><input id="new_pwd" name="new_pwd" type="password" /></td>
                </tr>
                <tr>
                    <td>角色:</td>
                    <td>
                    	<#list  roles as bl >
                    	<input type="checkbox" name="roles" value="${bl.id}">：${bl.role}<br/>
						</#list>
                    </td>
                </tr>
                
            </table>
		</form>
	</div>
	
	<div id="editManager" class="easyui-dialog" style="width:330px;height:400px;padding:20px 20px;">
		<form id="editManager_form" method="post">
			<input type="hidden" name="id" id="id" value=""/>
            <table cellpadding="5">
                <tr>
                    <td>用户名:</td>
                    <td><input id="editManager_name" name="editManager_name"/></td>
                </tr>
                <tr>
                    <td>真实姓名:</td>
                    <td><input id="editManager_realname" name="editManager_realname"/></td>
                </tr>
                <tr>
                    <td>手机号:</td>
                    <td><input id="editManager_mobileNumber" name="editManager_mobileNumber" /></td>
                </tr>
                <tr>
                    <td>角色:</td>
                    <td>
                    	<#list  roles as bl >
                    	<input type="checkbox" name="editManager_roles" value="${bl.id}">：${bl.role}<br/>
						</#list>
                    </td>
                </tr>
                
            </table>
		</form>
	</div>
	
	
</div>

<script>
function searchManager() {
	$('#s_data').datagrid('load', {
		name : $("#searchName").val()
	});
}

function editPwd(index){
	var arr=$("#s_data").datagrid("getData");
	$("#editId").val(arr.rows[index].id);
	$("#pwd").val("");
	$("#pwd1").val("");
	$("#editPwd_div").dialog('open');
}

function updateLocked(id,locked){
	var tip = '';
	if(locked==1){
		tip = '确定锁定该账户吗？';
	}else{
		tip = '确定解除锁定吗？'
	}
	$.messager.confirm("提示信息",tip,function(re){
        if(re){
            $.messager.progress();
            $.post("${rc.contextPath}/user/updateLocked",{id:id,locked:locked},function(data){
                $.messager.progress('close');
                if(data.status == 1){
                    $.messager.alert('响应信息',"操作成功...",'info',function(){
                        $('#s_data').datagrid('load');
                        return
                    });
                } else{
                    $.messager.alert('响应信息',data.msg,'error');
                }
            })
        }
    })
}

function editUser(index){
	var arr=$("#s_data").datagrid("getData");
	var rIds = arr.rows[index].rIds;
	$("#id").val(arr.rows[index].id);
	$("#editManager_name").val(arr.rows[index].username);
	$("#editManager_realname").val(arr.rows[index].realname);
	$("#editManager_mobileNumber").val(arr.rows[index].mobileNumber);
	$("input[name='editManager_roles']:checked").each(function(){ 
		$(this).prop("checked",false);
	});
	$.each(rIds,function(i){
        var nl = rIds[i];
        $("input[name='editManager_roles']").each(function(j){ 
			if($(this).val() == nl){
				$(this).prop("checked",true);
			}
    	});
    });
	$("#editManager").dialog('open');
}

	$(function(){
	
		$('#addManager_div').dialog({
            title:'add',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var name = $.trim($('#name').val());
                	var realname = $.trim($('#realname').val());
                	var mobileNumber = $.trim($('#mobileNumber').val());
                	var pwd = $.trim($('#new_pwd').val());
                	var roles="";
                	$("input[name='roles']:checked").each(function(){ 
                		roles+=$(this).val()+",";
                	});
                	if(name == '' || pwd == '' || realname == ''){
                		$.messager.alert('提示',"信息为空","error")
                	}else{
                		$.messager.progress();
                		$.post("${rc.contextPath}/user/addUser",
         						{name:name,realname:realname,mobileNumber:mobileNumber,pwd:pwd,roles:roles},
                    			function(data){
         							$.messager.progress('close');
                            		if(data.status == 1){
                            			$("#addManager_div").dialog('close');
                                  		$.messager.alert('提示',"保存成功","info")
                                  		$('#s_data').datagrid('load');
                             		}else{
                                  		$.messager.alert('提示',"保存失败","error")
                                	}
                      			},
        			     "json");
                	}
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addManager_div').dialog('close');
                }
            }]
        });
		
		$('#editManager').dialog({
            title:'edit',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'修改',
                iconCls:'icon-ok',
                handler:function(){
                	$.messager.progress();
                	var name = $.trim($('#editManager_name').val());
                	var realname = $.trim($('#editManager_realname').val());
                	var mobileNumber = $.trim($("#editManager_mobileNumber").val());
                	var id = $("#id").val();
                	var roles="";
                	$("input[name='editManager_roles']:checked").each(function(){ 
                		roles+=$(this).val()+",";
                	});
                	if(name == '' || realname == ''){
                		$.messager.alert('提示',"信息为空","error")
                	}else{
                		$.post("${rc.contextPath}/user/editUser",
         						{id:id,name:name,realname:realname,mobileNumber:mobileNumber,roles:roles},
                    			function(data){
         							$.messager.progress('close');
                            		if(data.status == 1){
                            			$("#editManager").dialog('close');
                                  		$.messager.alert('提示',"保存成功","info")
                                  		$('#s_data').datagrid('load');
                             		}else{
                                  		$.messager.alert('提示',"保存失败","error")
                                	}
                      			},
        			     "json");
                	}
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editManager').dialog('close');
                }
            }]
        });
		
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
                		$.post("${rc.contextPath}/user/updatePWD",
   						{editId:editId,pwd:pwd,pwd1:pwd1},
              			function(data){
                      		if(data.status == 1){
                      			$("#editPwd_div").dialog('close');
                            	$.messager.alert('提示',"修改成功","info")
                       		}else{{
                            	$.messager.alert('提示',data.msg,"error")
                          	}}
                		},
					"json");
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
            url:'${rc.contextPath}/user/userInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'id', width:20, align:'center',checkbox:true},
                {field:'username',    title:'用户名', width:60, align:'center'},
                {field:'realname',    title:'真实姓名', width:60, align:'center'},
                {field:'mobileNumber',    title:'手机号', width:60, align:'center'},
                {field:'roles',    title:'拥有角色', width:70, align:'center'},
                {field:'lockStatus',    title:'锁定状态', width:50, align:'center'},
                {field:'hidden',  title:'操作', width:60,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editUser(' + index + ')">修改</a>';
                        var b = ' | <a href="javaScript:;" onclick="editPwd(' + index + ')">修改密码</a>';
                        var c = '';
                        if(row.locked == 1){
                        	c = ' | <a href="javaScript:;" onclick="updateLocked(' + row.id + ',' + 0 +')">解除锁定</a>';
                        }else{
                        	c = ' | <a href="javaScript:;" onclick="updateLocked(' + row.id + ',' + 1 +')">锁定账户</a>';
                        }
                        return a + b + c;
                    }
                }
            ]],toolbar:[{
                id:'_add',
                text:'新增管理员',
                iconCls:'icon-add',
                handler:function(){
                	$("#name").val("");
                	$("#realname").val("");
                	$("#new_pwd").val("");
                	$("#mobileNumber").val("");
                	$("input[name='roles']:checked").each(function(){ 
                		//$(this).removeAttr("checked");
                		$(this).prop("checked",false);
                		//$(this).attr('checked', false);
                	});
                    $("#addManager_div").dialog('open').val("");
                }
            },'-',{
                iconCls: 'icon-remove',
                text:'锁定账户',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定锁定账户吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/user/batchUpdateLockStatus",
									{ids: ids.join(","),locked:1},
									function(data){
										if(data.status == 1){
											$.messager.alert('提示','操作成功',"info")
											$('#s_data').datagrid('load');
										}else{
											$.messager.alert('提示','保存出错',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的商品',"error")
                    }
                }
            },'-',{
                iconCls: 'icon-edit',
                text:'解除锁定',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定解除锁定吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/user/batchUpdateLockStatus",
									{ids: ids.join(","),locked:0},
									function(data){
										if(data.status == 1){
											$.messager.alert('提示','操作成功',"info")
											$('#s_data').datagrid('load');
										}else{
											$.messager.alert('提示','操作失败',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的商品',"error")
                    }
                }
            }],
            pagination:true
        });
	})
</script>

</body>
</html>