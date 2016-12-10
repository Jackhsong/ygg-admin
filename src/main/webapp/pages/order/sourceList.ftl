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
					<td>参数</td>
					<td><input id="mark" type="text" name="mark" /></td>
					<td>渠道</td>
					<td><input id="name" type="text" name="name" /></td>
					<td>负责人</td>
					<td><input id="responsibilityPerson" type="text" name="responsibilityPerson" /></td>
					<td><a id="searchBtn" onclick="searchChannel()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
				</tr>
			</table>
		</form>
	</div>

	<div title="订单来源管理" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>
	
	<div id="addChannel" class="easyui-dialog" style="width:330px;height:200px;padding:20px 20px;">
		<form id="af" method="post">
			<input name="id" type="hidden" value="" />
            <table cellpadding="5">
                <tr>
                    <td>渠道:</td>
                    <td><input name="addChannelName" /></td>
                </tr>
                <tr>
                    <td>负责人:</td>
                    <td><input name="addChannelResponsibilityPerson" /></td>
                </tr>
            </table>
		</form>
	</div>

</div>

<script>

	function searchChannel() {
		$('#s_data').datagrid('load', {
			name : $("input[name='name']").val(),
			mark : $("input[name='mark']").val(),
			responsibilityPerson : $("input[name='responsibilityPerson']").val()
		});
	}

	function editIt(index){
    	var arr=$("#s_data").datagrid("getData");
    	$("input[name='id']").val(arr.rows[index].id);
    	$("input[name='addChannelName']").val(arr.rows[index].name);
    	$("input[name='addChannelResponsibilityPerson']").val(arr.rows[index].responsibilityPerson);
    	$('#addChannel').dialog('open');
	}
	
	function deleteIt(id){
	    $.messager.confirm("tip","确定删除么？",function(re){
	        if(re){
	            $.messager.progress();
	            $.ajax({ 
     		       url: '${rc.contextPath}/order/deleteChannel',
     		       type: 'post',
     		       dataType: 'json',
     		       data: {'id':id},
     		       success: function(data){
     		         $.messager.progress('close');
     		           if(data.status == 1){
     		            	$('#s_data') .datagrid("load");
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
	    })
	}
	
	$('#addChannel').dialog({
        title:'tip',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存',
            iconCls:'icon-ok',
            handler:function(){
            	var id = $("input[name='id']").val();
            	var name = $("input[name='addChannelName']").val();
            	var responsibilityPerson = $("input[name='addChannelResponsibilityPerson']").val();
            	if(name == '' || responsibilityPerson == ''){
            		$.messager.alert('提示',"信息为空","error")
            	}else{
            		$.messager.progress();
            		$.ajax({ 
            		       url: '${rc.contextPath}/order/saveChannel',
            		       type: 'post',
            		       dataType: 'json',
            		       data: {'name':name,'id':id,'responsibilityPerson':responsibilityPerson},
            		       success: function(data){
            		         $.messager.progress('close');
            		           if(data.status == 1){
            		            	$('#s_data') .datagrid("load");
            		            	$('#addChannel').dialog('close');
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
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#addChannel').dialog('close');
            }
        }]
    });

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/order/otherSourceInfo',
            queryParams:{
            	isAvailable:0
            },
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:70,
            pageList:[70,80],
            columns:[[
                {field:'osc',    title:'参数(举例，红色部分为参数)', width:70, align:'center'},
                {field:'name',     title:'渠道',  width:50,   align:'center' },
                {field:'responsibilityPerson',  title:'负责人', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a>';
                        var b = ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                        return a + b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增渠道信息',
                iconCls:'icon-add',
                handler:function(){
                	$("input[name='id']").val("0");
                	$("input[name='addChannelName']").val("");
                	$("input[name='addChannelResponsibilityPerson']").val("");
                	$('#addChannel').dialog('open');
                }
            }],
            pagination:true
        });
		
		$('#mark').combobox({   
		    url:'${rc.contextPath}/order/searchInfo?type=1',   
		    valueField:'code',   
		    textField:'text'  
		});
		
		$('#name').combobox({
		    url:'${rc.contextPath}/order/searchInfo?type=2',   
		    valueField:'code',   
		    textField:'text'  
		});
		
		$('#responsibilityPerson').combobox({   
		    url:'${rc.contextPath}/order/searchInfo?type=3',   
		    valueField:'code',   
		    textField:'text'  
		});
	})
</script>

</body>
</html>