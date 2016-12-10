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
<div data-options="region:'center',title:'情景模板管理-更多商品'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 50px;">
			<div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
            	情景模板Id：<#if (activity.id)?exists>${activity.id?c}</#if>&nbsp;&nbsp;&nbsp;
               	情景模板名称：<#if (activity.title)?exists>${activity.title}</#if>&nbsp;&nbsp;&nbsp;
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editActivityLayoutDiv" class="easyui-dialog" style="width:600px;height:350px;padding:15px 20px;">
		        <form id="editActivityLayoutForm" method="post">
					<input id="editActivityLayoutForm_saId" type="hidden" name="activityId" value="${activityId}" >
					<input id="editActivityLayoutForm_id" type="hidden" name="editId" value="0" >
					<p>
						<span>&nbsp;商品：</span>
						<span><input type="text" name="productId" id="editActivityLayoutForm_productId" onblur="stringTrim(this);" value="" maxlength="8" style="width: 300px;"/></span>
						<font color="red">*</font><br/>
						&nbsp;&nbsp;&nbsp;<span id="productName" style="color:red"></span>
					</p>
					<p>
						<span>展现状态：</span>
						<span><input type="radio" name="isDisplay" id="editActivityLayoutForm_isDisplay1" checked="checked" value="1"/>展现&nbsp;&nbsp;</span>
						<span><input type="radio" name="isDisplay" id="editActivityLayoutForm_isDisplay0" value="0"/>不展现</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
		    
		    <!-- 从快速添加商品begin -->
		    <div id="quickAddProductDiv" class="easyui-dialog" style="width:400px;height:200px;padding:10px 10px;">
		    	<input type="hidden" id="quickAddProductDiv_type" value="${type!'1'}"/>
		    	<input type="hidden" id="quickAddProductDiv_cid" value="${activityId}"/>
		        <table cellpadding="5">
		            <tr>
		                <td>商品ID：<font color="red">(多个商品用英文逗号[,]分开)</font></td>
		            </tr>
		            <tr>
		            	<td>
		                	<textarea rows="3" cols="45" id="quickAddProductDiv_id" onkeydown="checkEnter(event)"></textarea>
		                </td>
		            </tr>
		        </table>
		    </div>
		    <!-- 从快速添加商品end -->
		    
		    	    
		</div>
	</div>
</div>

<script>

	function stringTrim(obj){
		$(obj).val($.trim($(obj).val()));	
	}
	
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
			url: '${rc.contextPath}/specialGroup/updateSpecialGroupActivityProductSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load',{
						activityId:${activityId},
						type:${type}
					});
		        }
				else
				{
                    $.messager.alert("提示",data.msg,"error");
		        }
			},
            error: function(xhr){
                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
            }
		});
	};
	<!--编辑排序相关 end-->
	
	<!-- 删除 -->
	function deleteIt(id){
		$.messager.confirm('提示', '确定删除吗?', function(r){
			if (r){
				$.ajax({
					url: '${rc.contextPath}/specialGroup/deleteSpecialGroupActivityProduct',
					type:"POST",
					data: {id:id},
					success: function(data) {
						if(data.status == 1){
							$('#s_data').datagrid('load',{
								activityId:${activityId},
								type:${type}
							});
				        }
						else
						{
		                    $.messager.alert("提示",data.msg,"error");
				        }
					},
		            error: function(xhr){
		                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		            }
				});	
			}
		});
	}
	
	function displayIt(id,isDisplay){
		var tip = '';
		if(isDisplay == 1){
			tip = "确定展现吗？";
		}else{
			tip = "确定不展现吗？";
		}
		$.messager.confirm('提示',tip,function(r){
			$.ajax({
				url: '${rc.contextPath}/specialGroup/updateSpecialGroupActivityProductDisplay',
				type:"POST",
				data: {id:id,isDisplay:isDisplay},
				success: function(data) {
					if(data.status == 1){
						$('#s_data').datagrid('load',{
							activityId:${activityId},
							type:${type}
						});
			        }
					else
					{
	                    $.messager.alert("提示",data.msg,"error");
			        }
				},
	            error: function(xhr){
	                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	            }
			});			
		});
	}
	
	function clealActivityLayoutDiv(){
		$("#editActivityLayoutForm_id").val('');
		$("#editActivityLayoutForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editActivityLayoutForm_isDisplay1").prop('checked',true);
		$('#productName').text('');
	}
	function editIt(index){
		$("input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		clealActivityLayoutDiv();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var productId = arr.rows[index].productId;
		var isDisplay = arr.rows[index].isDisplay;
    	$('#editActivityLayoutForm_id').val(id);
    	$('#editActivityLayoutForm_productId').val(productId);
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==isDisplay){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#editActivityLayoutDiv').dialog('open');
	}
	
	$(function(){
		

		$("#editActivityLayoutForm_productId").change(function(){
			var id = $.trim($(this).val());
			var elementId = $(this).attr('id');
			if(id == ""){
				$("#productName").text("");
			}else{
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#productName').text(data.msg);
	                    }else{
                            $.messager.alert("提示",data.msg,"error");
	                    }
	                },
	                error: function(xhr){
                        $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
	                }
	            });
			}
		});

		
		<!--列表数据加载-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/specialGroup/jsonFloorProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{activityId:${activityId},type: ${type}},
            pageSize:50,
            columns:[[
                {field:'productId',    title:'商品Id', width:20, align:'center'},
                {field:'productName',    title:'商品名称', width:80, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'isDisplay',    title:'展现状态', width:20, align:'center',
                	formatter:function(value,row,index){
                		if(row.isDisplay == 1){
                			return '展现';
                		}else{
                			return '不展现';
                		}
                	}	
                },
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">改排序</a> | ';
                    		var b = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a> | ';
                    		var c = '<a href="javascript:;" onclick="deleteIt('+ row.id + ')">删除</a> | ';
                    		var d = '';
                    		if(row.isDisplay == 1){
                    			d = '<a href="javascript:;" onclick="displayIt('+ row.id + ',' + 0 +')">不展现</a>';
                    		}else{
                    			d = '<a href="javascript:;" onclick="displayIt('+ row.id + ',' + 1 +')">展现</a>';
                    		}
                    		return a  + b + c + d;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'back',
                text:'返回新情景专场',
                iconCls:'icon-back',
                handler:function(){
                	window.location.href = "${rc.contextPath}/specialGroup/list";
                }
            },'-',{
                id:'_add',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                	clealActivityLayoutDiv();
                	$('#editActivityLayoutDiv').dialog('open');
                }
            },'-',{
                iconCls: 'icon-edit',
                text:'快速添加商品',
                handler: function(){
                	$("#quickAddProductDiv_id").val('');
					$("#quickAddProductDiv").dialog('open');
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
		
		
	    $('#editActivityLayoutDiv').dialog({
	    	title:'添加商品',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
                    var params = {};
					params.type = 2;
                    params.activityId = $("input[name='activityId']").val();
                    params.id = $("input[name='editId']").val();
                    params.isDisplay = $("input[name='isDisplay']:checked").val();
                    params.productId = $("input[name='productId']").val();
                    if(params.productId == '')
                    {
                        $.messager.alert("error","请填写完整信息","error");
                    }
                    else
                    {
                        $.messager.progress();
                        $.ajax({
                            url: '${rc.contextPath}/specialGroup/editSpecialGroupActivityProduct',
                            type: 'post',
                            dataType: 'json',
                            data: params,
                            success: function(data){
                                $.messager.progress('close');
                                if(data.status == 1){
                                	$.messager.alert("提示",data.msg,"info",function(){
	                                    $('#s_data').datagrid('load',{activityId:${activityId},type: ${type}});
	                                    $('#editActivityLayoutDiv').dialog('close');
                                	});
                                }else{
                                    $.messager.alert("提示",data.msg,"error");
                                }
                            },
                            error: function(xhr){
                                $.messager.progress('close');
                                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                            }
                        });
                    }
	    		}
	    	},
	    	{
	    		text:'取消',
	            align:'left',
	            iconCls:'icon-cancel',
	            handler:function(){
	                $('#editActivityLayoutDiv').dialog('close');
	            }
	    	}]
	    });
	    
		$('#quickAddProductDiv').dialog({
            title:'快速添加商品',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                	var productId = $("#quickAddProductDiv_id").val();
                	var activityId = $("#quickAddProductDiv_cid").val();
                	if($.trim(productId) == ''){
                		$.messager.alert("提示",'请输入商品ID',"warning");
                	}else{
               			$.messager.progress();
           				$.ajax({
           		            url: '${rc.contextPath}/specialGroup/batchAddSpecialGroupActivityProduct',
           		            type: 'post',
           		            dataType: 'json',
           		            data: {'productIds':productId, 'activityId':activityId},
           		            success: function(data){
           		            	$.messager.progress('close');
           		                if(data.status == 1){
           		                	$('#quickAddProductDiv').dialog('close');
           		                	$('#s_data').datagrid('reload');
           		                	$.messager.alert("提示",data.msg,"info");
           		                }else{
           		                	$.messager.alert("提示",data.msg,"error");
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
                    $('#quickAddProductDiv').dialog('close');
                }
            }]
        });
				
	});
	
</script>

</body>
</html>