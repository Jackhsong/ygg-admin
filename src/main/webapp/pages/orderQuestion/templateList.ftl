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

<div title="订单售后问题类型管理" class="easyui-panel" style="padding:10px">
        <div class="content_body">
            <div class="selloff_mod">
                <table id="s_data" ></table>
            </div>
        </div>
        <div id="addTemplate" class="easyui-dialog" style="width:400px;height:180px;padding:10px 10px;">
            <form id="af" method="post">
            <input type="hidden" name="id" id="edit_id" value="0"/>
            <table cellpadding="5">
                <tr>
                    <td>问题类型:</td>
                    <td><input id="edit_name" type="text" name="name" data-options="required:true"></input></td>
                </tr>
                <tr>
                    <td>问题时效:</td>
                    <td><input id="edit_limitHour" type="text" name="limitHour" data-options="required:true">（小时）</input></td>
                </tr>
            </table>
        </form>
        </div>
    </div>

</div>

<script>

function updateIsAvailable(id,code){
	var tip = "";
	if(code == 0){
		tip = "确定不展现吗？";
	}else{
		tip = "确定展现吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/orderQuestion/updateOrderQuestionTemplateStatus",
	            		{id:id,isAvailable:code},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('load');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	            		});
        }
    });
}

function updateIt(index){
	$("#edit_id").val("");
	$("#edit_name").val("");
	$("#edit_limitHour").val("");
	var arr=$("#s_data").datagrid("getData");
	$('#edit_id').val(arr.rows[index].id);
	$('#edit_name').val(arr.rows[index].name);
	$('#edit_limitHour').val(arr.rows[index].limitHour);
    $("#addTemplate").dialog('open');
}
	$(function(){
	
		$('#addTemplate').dialog({
            title:'问题类型',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#af').form('submit',{
                        url:"${rc.contextPath}/orderQuestion/saveTemplate",
                        onSubmit: function(){ 
                        	var name = $("#edit_name").val();
                        	var limitHour = $.trim($("#edit_limitHour").val());
                        	var reg = /^\d*.?\d*$/;
                            if($.trim(name) == ''){
                            	$.messager.alert('提示',"请输入问题类型",'error');
                            	return false;
                            }else if(!reg.test(limitHour)){
                            	$.messager.alert('提示',"问题时限只能为整数或小数",'error');
                            	return false;
                            }
                            $.messager.progress();
                        }, 
                        success:function(data){
                            var res = eval("("+data+")")
                            $.messager.progress('close');
                            if(res.status == 1){
                                $('#addTemplate').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                    $('#s_data').datagrid('reload');
                                });
                            } else{
                            	$.messager.progress('close');
                                $.messager.alert('响应信息',res.msg,'error',function(){
                                });
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addTemplate').dialog('close');
                }
            }]
        })
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/orderQuestion/jsonQuestionTemplateInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            columns:[[
                {field:'status',    title:'可用状态', width:30, align:'center'},
                {field:'name',    title:'问题类型', width:70, align:'center'},
                {field:'limitHourStr',    title:'时效', width:50, align:'center'},
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                    	var a = '<a href="javaScript:;" onclick="updateIt(' + index + ')">编辑</a> | ';
                    	var b = '';
                        if(row.isAvailable == 1){
                        	b = '<a href="javaScript:;" onclick="updateIsAvailable(' + row.id + ',' + 0 + ')">停用</a>';
                        }else{
                        	b =  '<a href="javaScript:;" onclick="updateIsAvailable(' + row.id + ',' + 1 + ')">可用</a>';
                        }
                        return b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增问题类型',
                iconCls:'icon-add',
                handler:function(){
                	$("#edit_id").val("");
                	$("#edit_name").val("");
                	$("#edit_limitHour").val("");
                    $("#addTemplate").dialog('open');
                }
            }],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>