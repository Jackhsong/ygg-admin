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
            <form id="af" method="post">
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
<div id="addManager" class="easyui-dialog" style="width:330px;height:200px;padding:20px 20px;">
            <form id="af" method="post">
            <table cellpadding="5">
                <tr>
                    <td>用户名:</td>
                    <td><input id="name" name="name" data-options="required:true"></input></td>
                </tr>
                <tr>
                    <td>密码:</td>
                    <td><input id="new_pwd" name="new_pwd" type="password" data-options="required:true"></textarea></td>
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
	$(function(){
	
		$('#addManager').dialog({
            title:'add',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var name = $('#name').val();
                	var pwd = $('#new_pwd').val();
                	if(name == '' || pwd == ''){
                		$.messager.alert('提示',"信息为空","error")
                	}else{
                		$.post("${rc.contextPath}/account/tre",
         						{name:name,pwd:pwd},
                    			function(data){
                            		if(data.status == 1){
                            			$("#addManager").dialog('close');
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
                    $('#addManager').dialog('close');
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
                		$.post("${rc.contextPath}/account/managerUpdatePWD",
   						{editId:editId,pwd:pwd,pwd1:pwd1},
              			function(data){
                      		if(data.status == 1){
                      			$("#editPwd_div").dialog('close');
                            	$.messager.alert('提示',"修改成功","info")
                       		}else{{
                            	$.messager.alert('提示',"修改失败","error")
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
            url:'${rc.contextPath}/account/managerJsonInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'createTime',    title:'创建时间', width:70, align:'center'},
                {field:'name',    title:'用户名', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="editPwd(' + index + ')">修改密码</a>';
                        return lableStr;
                    }
                }
            ]],toolbar:[{
                id:'_add',
                text:'新增管理员',
                iconCls:'icon-add',
                handler:function(){
                	$("#name").val("");
                	$("#new_pwd").val("");
                    $("#addManager").dialog('open').val("");
                }
            }],
            pagination:true
        });
	})
</script>

</body>
</html>