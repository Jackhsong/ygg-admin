<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理-商家邮费黑名单</title>
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
<div data-options="region:'center',title:'商家优惠券及活动黑名单'" style="padding:5px;">
    <!--数据表格-->
    <table id="s_data" style=""></table>
    
	<div id="addDataDiv" class="easyui-dialog" style="width:500px;height:190px;padding:20px 20px;">
       <form id="addDataForm" method="post">
       		<input type="hidden" value="${type}"  id="type" />
       		<input type="hidden" value=""  id="id" />
	        <table cellpadding="5">
	            <tr>
	                <td>商家ID:</td>
	                <td><input id="addDataForm_sellerId"  style="width:300px"  maxlength="8" autocomplete="off"></input></td>
	            </tr>
	        </table>
   		</form>
   	</div> 
</div>

<script>

	function deleteItem(index){
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		$.messager.confirm('删除','确定删除吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/sellerBlacklist/delete',
		            type: 'post',
		            dataType: 'json',
		            data: {'id':id},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
                            $('#s_data').datagrid('load');
//		                	$.messager.alert("提示",'删除成功',"info");
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
            url:'${rc.contextPath}/sellerBlacklist/ajaxData',
            queryParams:{
                type:'2'
            },
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'sellerId',    title:'商家ID', width:30, align:'center'},
                {field:'sellerName',    title:'商家名称', width:50, align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                    	var b = '<a href="javaScript:;" onclick="deleteItem(' + index + ')">删除</a>';
                        return b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                    $("#id").val("");
                	$("#addDataForm_sellerId").val("");
                    $("#addDataDiv").dialog("open");
                }
            }],
            pagination:true
        });
        
        
        $('#addDataDiv').dialog({
            title:'新增',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    var params = {};
                    params.id = $("#id").val();
                    params.type = $("#type").val();
                    params.sellerId = $.trim($("#addDataForm_sellerId").val());
                    if(params.sellerId == ''){
                        $.messager.alert('警告','商家不能为空', 'warning');
                        return false;
                    }
                    $.ajax({
                        url: '${rc.contextPath}/sellerBlacklist/save',
                        type: 'post',
                        dataType: 'json',
                        data: params,
                        success: function(data){
                            $.messager.progress('close');
                            if(data.status == 1){
                                $('#s_data').datagrid('load');
//                                $.messager.alert("提示",'保存成功',"info");
                                $('#addDataDiv').dialog('close');
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
                    $('#addDataDiv').dialog('close');
                }
            }]
        });       
	});
</script>

</body>
</html>