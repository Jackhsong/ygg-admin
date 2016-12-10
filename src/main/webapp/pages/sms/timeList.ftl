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

	<div id="searchDiv" class="datagrid-toolbar" style="height: 100px; padding: 15px">
		<table>
			<tr>
				<td>
					<form id="methodForm" method="post" action="${rc.contextPath}/delayDate/importDelayDate" enctype="multipart/form-data">
						<input id="orderFileBox" type="text" name="orderFile" style="width:300px" /><br/><br/><br/>
						设置导单日期：
						<input value="" id="importYear" name="importYear" onClick="WdatePicker({dateFmt: 'yyyy'})"/>
						<a id="searchBtn" onclick="doFormAction()" href="javascript:;" class="easyui-linkbutton" >确认导入</a>
					</form>
				</td>
				<td valign=top>
					<form id="downFileForm" action="${rc.contextPath}/delayDate/downloadTemplate" method="post">
						<input type=hidden name="type" value="1" />
						<a id="searchBtn" onclick="downFile()" href="javascript:;" class="easyui-linkbutton" >下载模板</a>
					</form>
				</td>
			</tr>
		</table>
	</div>

	<div title="短信内容管理" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>
	
    <div id="addTimeDiv" class="easyui-dialog" style="width:400px;height:220px;padding:20px 20px;">
       <form id="addTimeForm" method="post">
       	<input type="hidden" id="addTimeForm_id" name="id" value=""/>
        <table cellpadding="5">
            <tr>
                <td>日期:</td>
                <td><input id="addTimeForm_sendDate" name="sendDate" onClick="WdatePicker({dateFmt: 'yyyyMMdd'})" ></input></td>
            </tr>
            <tr>
                <td>起始时间:</td>
                <td><input id="addTimeForm_beginTime" name="beginTime" onClick="WdatePicker({dateFmt: 'HH'})"></input></td>
            </tr>
            <tr>
                <td>结束时间:</td>
                <td><input id="addTimeForm_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'HH'})"></input></td>
            </tr>
        </table>
 	   </form>
    </div>
    
    <div id="editTimeDiv" class="easyui-dialog" style="width:400px;height:220px;padding:20px 20px;">
       <form id="editTimeForm" method="post">
       	<input type="hidden" id="editTimeForm_id" name="id" value=""/>
        <table cellpadding="5">
            <tr>
                <td>日期:</td>
                <td><input id="editTimeForm_sendDate" name="sendDate" onClick="WdatePicker({dateFmt: 'yyyyMMdd'})" ></input></td>
            </tr>
            <tr>
                <td>起始时间:</td>
                <td><input id="editTimeForm_beginTime" name="beginTime" onClick="WdatePicker({dateFmt: 'HH'})"></input></td>
            </tr>
            <tr>
                <td>结束时间:</td>
                <td><input id="editTimeForm_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'HH'})"></input></td>
            </tr>
        </table>
 	   </form>
    </div>

</div>

<script>

	function doFormAction() {
		$('#methodForm').submit();
	}
	
	function downFile() {
		$('#downFileForm').submit();
	}
	
	function deleteTime(index){
		var arr = $("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		$.messager.confirm('删除','确定删除吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/delayDate/delete',
		            type: 'post',
		            dataType: 'json',
		            data: {'idStr':id},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
		                	$('#s_data').datagrid('load');
		                	$.messager.alert("提示",'删除成功',"info");
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
	
	function updateTime(index){
		var arr=$("#s_data").datagrid("getData");
	    $("#editTimeForm_id").val(arr.rows[index].id);
		$("#editTimeForm_sendDate").val(arr.rows[index].sendDate);
		$("#editTimeForm_beginTime").val(arr.rows[index].beginTime);
		$("#editTimeForm_endTime").val(arr.rows[index].endTime);
	    $("#editTimeDiv").dialog('open');
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/delayDate/jsonInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
				{field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'sendDate',    title:'日期', width:80, align:'center'},
                {field:'beginTime',     title:'起始时间',  width:80,   align:'center' },
                {field:'endTime',    title:'结束时间', width:80, align:'center'},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="updateTime(' + index + ')">修改</a>';
                        var b = ' | <a href="javaScript:;" onclick="deleteTime(' + index + ')">删除</a>';                        
                        return a + b;
                    }
                }
            ]],
            toolbar:[
                   {
                       id:'_add',
                       text:'新增',
                       iconCls:'icon-add',
                       handler:function(){
                       	$('#addTimeDiv').dialog('open');
                       }
                   },'-',
                   {
                       iconCls: 'icon-remove',
                       text:'删除',
                       handler: function(){
                           var rows = $('#s_data').datagrid("getSelections");
                           if(rows.length > 0){
                               $.messager.confirm('删除','确定删除吗',function(b){
                                   if(b){
                                       var ids = [];
                                       for(var i=0;i<rows.length;i++){
                                           ids.push(rows[i].id)
                                       }
                                       $.post("${rc.contextPath}/delayDate/delete", //批量删除
       									{idStr: ids.join(",")},
       									function(data){
       										if(data.status == 1){
       											$('#s_data').datagrid('reload');
       											$.messager.alert('提示',data.msg, 'info');
       										}else{
       											$.messager.alert('提示',data.msg,"error")
       										}
       									},
       								"json");
                                   }
                        		});
                           }else{
                               $.messager.alert('提示','请选择要操作的类别',"error");
                           }
                       }
                   }
                ],
            pagination:true,
            rownumbers:true
        });
		
        $('#addTimeDiv').dialog({
        	title: '新增短信发送时间',
        	collapsible: true,
        	closed: true,
        	modal: true,
        	buttons: [
        	{
        		text: '保存',
        		iconCls: 'icon-ok',
        		handler: function(){
        			$('#addTimeForm').form('submit',{
        				url: "${rc.contextPath}/delayDate/saveOrUpdate",
        				success: function(data){
                            var res = eval("("+data+")");
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	$("#addTimeForm_sendDate").val("");
                                	$("#addTimeForm_beginTime").val("");
                                	$("#addTimeForm_endTime").val("");
                                    $('#s_data').datagrid('reload');
                                    $('#addTimeDiv').dialog('close');
                                });
                            } else if(res.status == 0){
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                });
                            } else{
                                $.messager.alert('响应信息',res.msg,'error',function(){
                                });
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
                	$("#addTimeForm_sendDate").val("");
                	$("#addTimeForm_beginTime").val("");
                	$("#addTimeForm_endTime").val("");
                    $('#addTimeDiv').dialog('close');
                }
        	}]
        });
        
        $("#editTimeDiv").dialog({
        	title:'修改发送短信时间',
        	collapsible: true,
        	closed: true,
        	modal: true,
        	buttons:[
       	         {
       	        	text:	'保存',
       	        	iconCls:'icon-ok',
       	        	handler:function(){
       	        		$('#editTimeForm').form('submit',{
            				url: "${rc.contextPath}/delayDate/saveOrUpdate",
            				success: function(data){
                                var res = eval("("+data+")");
                                if(res.status == 1){
                                    $.messager.alert('响应信息',"保存成功",'info',function(){
                                    	$("#editTimeForm_id").val('');
                	       	     		$("#editTimeForm_sendDate").val('');
                	       	     		$("#editTimeForm_beginTime").val('');
                	       	     		$("#editTimeForm_endTime").val('');
                	       	     		$("#editTimeDiv").dialog('close');
                                        $('#s_data').datagrid('reload');
                                    });
                                } else if(res.status == 0){
                                    $.messager.alert('响应信息',res.msg,'info',function(){
                                    });
                                } else{
                                    $.messager.alert('响应信息',res.msg,'error',function(){
                                    });
                                }
            				}
            			});
       	        	}
       	         },
       	         {
       	        	 text:	'取消',
       	        	 iconCls:'icon-cancel',
       	        	 handler:function(){
       	        		$("#editTimeForm_id").val('');
	       	     		$("#editTimeForm_sendDate").val('');
	       	     		$("#editTimeForm_beginTime").val('');
	       	     		$("#editTimeForm_endTime").val('');
	       	     		$("#editTimeDiv").dialog('close');
       	        	 }
       	         }
        	]
        });
        
        
        $('#orderFileBox').filebox({
    		buttonText: '打开导单文件',
    		buttonAlign: 'right'
    	});
        
        
        $('#methodForm').form({
    		url:'',   
    		onSubmit: function(){
    			var orderFile = $('#orderFileBox').filebox("getValue");
    			var sendDate = $('#importYear').val();
    			if(orderFile == ""){
    				$.messager.alert("info","请选择文件","warn");
    				return false;
    			}
    			if(sendDate == ""){
    				$.messager.alert("info","请选择日期","warn");
    				return false;
    			}
    			$.messager.progress();
    		},   
    		success:function(data){
    			$.messager.progress('close');
    			var data = eval('(' + data + ')');  // change the JSON string to javascript object   
    			if (data.status == 1){
    				$.messager.alert('响应信息',"保存成功",'info',function(){
    					$('#importYear').val("");
                        $('#s_data').datagrid('reload');
                    });
    			}else{
    				$.messager.alert("提示",data.msg,"info");
    			}
    		}   
    	});
	});
</script>

</body>
</html>