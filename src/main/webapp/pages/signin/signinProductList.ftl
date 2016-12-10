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

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'签到商品管理',split:true" style="height: 90px;">
			<div style="height: 40px;padding: 10px">
	            <table>
	                <tr>
	                    <td >商品ID：</td>
	                    <td ><input id="searchProductId" name="productId" value="" /></td>
	                    <td >商品编码：</td>
	                    <td ><input id="searchProductCode" name="productCode" value="" /></td>
	                    <td >商品名称：</td>
	                    <td ><input id="searchProductName" name="productName" value="" /></td>
	                    <td >展现状态：</td>
	                	<td >
	                		<select id="searchIsDisplay" name="searchIsDisplay">
	                			<option value="-1">全部</option>
	                			<option value="1">展现</option>
	                			<option value="0">不展现</option>
	                		</select>
	                	<td >
							<a id="searchBtn" onclick="searchCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;
							<a id="clearBtn" onclick="clearCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
							&nbsp;
	                	</td>
	                </tr>
	            </table>
			</div>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editSigninProductDiv" class="easyui-dialog" style="width:500px;height:280px;padding:15px 20px;">
		        <form id="editSigninProductForm" method="post">
					<input id="editSigninProductForm_id" type="hidden" name="id" value="" >
					<p>
						<span>商品ID：</span>
						<span><input type="number" name="productId" id="editSigninProductForm_productId" value="" maxlength="32" style="width: 300px;"/></span>
					</p>
					<p>
						<span>商品名称：</span>
						<span id="editSigninProductForm_name"></span>
					</p>
					<p>
						<span>排序值：</span>
						<span><input type="number" name="sequence" id="editSigninProductForm_sequence" value="" maxlength="32" style="width: 300px;"/></span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
    		
		</div>
        
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

	function searchCustom(){
		$('#s_data').datagrid('load',{
			productId:$("#searchProductId").val(),
			productCode:$("#searchProductCode").val(),
			productName:$("#searchProductName").val(),
			isDisplay:$("#searchIsDisplay").val()
		});
	}
	
	function clearCustom(){
		$("#searchProductId").val('');
		$("#searchProductCode").val('');
		$("#searchProductName").val('');
		$("#searchIsDisplay").find('option').eq(0).attr('selected','selected');
		$('#s_data').datagrid('load',{});
	}
	
	
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
			url: '${rc.contextPath}/signin/updateSigninProductSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('clearSelections');
					$('#s_data').datagrid('reload');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	
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
   	            $.ajax({
   	            	url:'${rc.contextPath}/signin/updateSigninProductDisplayStatus',
   	            	type:'post',
   	            	dataType:'json',
   	            	data:{'ids':id,"isDisplay":code},
   	            	success:function(data){
   	            		$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid('clearSelections');
							$('#s_data').datagrid("reload");
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
    	});
	}
	
	
	function deleteIt(id){
		var tips = '';
    	$.messager.confirm("提示信息","确定删除吗？",function(re){
        	if(re){
   	            $.messager.progress();
   	            $.ajax({
   	            	url:'${rc.contextPath}/signin/deleteSigninProduct',
   	            	type:'post',
   	            	dataType:'json',
   	            	data:{'ids':id},
   	            	success:function(data){
   	            		$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid('clearSelections');
							$('#s_data').datagrid("reload");
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
    	});
	}
	

	$(function(){
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/signin/jsonSigninProductInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', align:'center',checkbox:true},
            	{field:'isDisplay',    title:'展现状态', width:20, align:'center',
            		formatter:function(value,row,index){
            			if(row.isDisplay ==1){
            				return '展现';
            			}else{
            				return '不展现';
            			}
            		}
            	},
                {field:'productId',    title:'商品ID', width:20, align:'center'},
                {field:'isOffShelves',    title:'商品状态', width:20, align:'center',
                	formatter:function(value,row,index){
                		if(row.isOffShelves == 1){
                			return '下架';
                		}else{
                			return '出售中';
                		}
                	}
                },
                {field:'typeStr',    title:'商品类型', width:30, align:'center'},
                {field:'saleStartTime',    title:'起售时间', width:30, align:'center'},
                {field:'saleEndTime',    title:'截止时间', width:30, align:'center'},
                {field:'stock',    title:'库存', width:30, align:'center'},
                {field:'productCode',    title:'商品编码', width:30, align:'center'},
                {field:'productName',    title:'长名称', width:60, align:'center'},
                {field:'remark',    title:'备注', width:40, align:'center'},
                {field:'salesPrice',    title:'售价', width:20, align:'center'},
                {field:'sellerName',    title:'商家', width:40, align:'center'},
                {field:'sendAddress',    title:'发货地', width:30, align:'center'},
                {field:'sequence',     title:'排序值',  width:20,   align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var b = '<a href="javascript:;" onclick="deleteIt(' + row.id + ')">删除</a> | ';
                            var c = '';
                            if(row.isDisplay == 1){
	                        	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">不展现</a>';
	                        }else if(row.isDisplay == 0) {
	                        	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">展现</a>';
	                        }
	                        return a + b + c;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                	$('#editSigninProductForm_productId').val('');
                	$('#editSigninProductForm_sequence').val('');
                	$("#editSigninProductForm_name").text('');
                	$('#editSigninProductDiv').dialog('open');
                }
            },'-',{
                id:'_edit1',
                text:'批量删除',
                iconCls:'icon-remove',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        var ids = [];
                        for(var i=0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                        deleteIt(ids.join(","))
                    }else{
                        $.messager.alert('提示','请选择要操作的商品',"error")
                    }
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
		
		
	    $('#editSigninProductDiv').dialog({
	    	title:'签到商品',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editSigninProductForm').form('submit',{
	    				url: "${rc.contextPath}/signin/saveOrUpdateProduct",
	    				onSubmit:function(){
	    					var productId = $("#editSigninProductForm_productId").val();
	    					var sequence = $("#editSigninProductForm_sequence").val();
	    					if($.trim(productId) == ''){
	    						$.messager.alert('提示',"请输入商品ID",'error');
	    						return false;
	    					}else if($.trim(sequence) == ''){
	    						$.messager.alert('提示',"请输入排序值",'error');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                            	$('#s_data').datagrid('clearSelections');
	                                $('#s_data').datagrid('reload');
	                                $('#editSigninProductDiv').dialog('close');
	                            });
	                        } else if(res.status == 0){
	                            $.messager.alert('响应信息',res.msg,'error');
	                        } 
	    				},
	    				error: function(xhr){
				         	$.messager.progress('close');
				        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
				       }
	    			});
	    		}
	    	},
	    	{
	    		text:'取消',
	            align:'left',
	            iconCls:'icon-cancel',
	            handler:function(){
	                $('#editSigninProductDiv').dialog('close');
	            }
	    	}]
	    });
	    
       	$("#editSigninProductForm_productId").change(function(){
			var id = $.trim($(this).val());
			if(id == ""){
				$("#editSigninProductForm_name").text('');
			}else{
				$("#editSigninProductForm_name").text('');
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#editSigninProductForm_name').text(data.msg);
	                    }else{
	                    	$.messager.alert("提示",data.msg,"info");
	                    }
	                },
	                error: function(xhr){
	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                }
	            });
			}
		});
	});
</script>

</body>
</html>