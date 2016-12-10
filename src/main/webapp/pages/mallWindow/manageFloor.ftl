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
<div data-options="region:'center',title:'楼层管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 50px;">
			<div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
            	主题馆ID：<#if mallPage.id?exists>${mallPage.id?c}</#if>&nbsp;&nbsp;&nbsp;
            	主题馆标题：<#if mallPage.name?exists>${mallPage.name}</#if>&nbsp;&nbsp;&nbsp;
            	备注：<#if mallPage.remark?exists>${mallPage.remark}</#if>&nbsp;&nbsp;&nbsp;
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
    		<!-- 新增/编辑弹出框 begin -->
		    <div id="editPageFloorDiv" class="easyui-dialog" style="width:450px;height:200px;padding:10px 10px;">
		        <form id="editPageFloorForm" method="post">
		        	<input type="hidden" name="mallPageId" id="editPageFloorForm_mallPageId" value="${mallPageId}"/>
		        	<input type="hidden" name="id" id="editPageFloorForm_id" value=""/>
			        <table cellpadding="5">
			            <tr>
			                <td>楼层名称：</td>
			                <td>
			                	<input type="text" name="name" id="editPageFloorForm_name" value="" maxlength="6"/><font color="red">*&nbsp;注：6个汉字以内</font>
			                </td>
			             </tr>
			             <tr>
			                <td>是否展现：</td>
			                <td>
			                	<input type="radio"  name="isDisplay"  id="isDisplay_1" value="1"> 展现&nbsp;&nbsp;&nbsp;
								<input type="radio"  name="isDisplay"  id="isDisplay_0" value="0"> 不展现
			                </td>
			            </tr>
			        </table>
		    	</form>
		    </div>
		    <!-- 新增/编辑弹出框 begin -->
		      		
		</div>
	</div>
</div>
<script>
	<!--编辑排序相关 begin-->
	function editrow(index){
		$('#s_data').datagrid('beginEdit', index);
	};
	
	function updateActions(){
		var rowcount = $('#s_data').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_data').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}
	
	function saverow(index){
		$('#s_data').datagrid('endEdit', index);
	};
	
	function cancelrow(index){
		$('#s_data').datagrid('cancelEdit', index);
	};
	
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/mallWindow/updateFloorSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence,mallPageId:row.mallPageId},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('reload');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	<!--编辑排序相关 end-->
	
	<!--编辑-->
	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		$("#editPageFloorForm input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		$("#editPageFloorForm_id").val(arr.rows[index].id);
		$("#editPageFloorForm_name").val(arr.rows[index].name);
		if(arr.rows[index].isDisplayCode==1){
			$('#isDisplay_1').prop('checked',true);
		}else{
			$('#isDisplay_0').prop('checked',true);
		}
    	$('#editPageFloorDiv').dialog('open');
	}
	
	function displayIt(id,code){
		var tips = '';
		if(code == 1){
			tips = '确定展现吗？';
		}else{
			tips = '确定不展现吗？'
		}
    	$.messager.confirm("提示信息",tips,function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/mallWindow/updatePageFloorDisplayCode",{id:id,code:code,mallPageId:${mallPageId}},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('reload');
                        return
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	})
	}	
	
	$(function(){
		
		<!--列表数据加载-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/mallWindow/jsonPageFloorInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{mallPageId:${mallPageId}},
            pageSize:50,
            columns:[[
                {field:'name',    title:'楼层名称', width:70, align:'center'},
                {field:'sequence',    title:'排序值', width:50, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'isDisplay',    title:'是否展现', width:50, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var b = '<a href="javaScript:;" onclick="editIt(' + index + ')">修改楼层</a> | ';
                    		var c = '';
                    		if(row.isDisplayCode == 1){
	                        	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">不展现</a>';
	                        }else{
	                        	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">展现</a>';
	                        }
                    		return a + b + c;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'back',
                text:'返回主题馆页面管理',
                iconCls:'icon-back',
                handler:function(){
                	window.location.href = "${rc.contextPath}/mallWindow/pageList"
                }
            },'-',{
                id:'_add',
                text:'新增楼层',
                iconCls:'icon-add',
                handler:function(){
                	$('#editPageFloorForm_name').val('');
                	$('#editPageFloorForm_id').val('0');
                	$('#isDisplay_0').prop('checked',true);
                	$('#editPageFloorDiv').dialog('open');
                }
            }],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions();
            	updateActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions();
        	},
            pagination:true
        });
		
		<!--新增/编辑 弹出框-->
	    $('#editPageFloorDiv').dialog({
	    	title:'楼层',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editPageFloorForm').form('submit',{
	    				url: "${rc.contextPath}/mallWindow/saveOrUpdatePageFloor",
	    				onSubmit:function(){
	    					var name = $("#editPageFloorForm input[name='name']").val();
	    					if($.trim(name) == ''){
	    						$.messager.alert("提示","请输入页面标题","warn");
	    						return false;
	    					}else if(name.length>6){
	    						$.messager.alert("提示","页面标题请控制在6字数以内","warn");
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
	                                $('#editPageFloorDiv').dialog('close');
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
	                $('#editPageFloorDiv').dialog('close');
	            }
	    	}]
	    });		
	});
</script>

</body>
</html>