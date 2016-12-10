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
<style type="text/css">
.searchName{
	padding-right:10px;
	/* text-align:right; */
	text-align:justify;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
.inputStyle{
	width:250px;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'新人专享商品管理',split:true" style="height: 120px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td>商品名称：</td>
						<td><input name="productName" /></td>
						<td>&nbsp;<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
						<td></td>
					</tr>
				</table>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
    		<!-- dialog begin -->
			<div id="addGegeProduct" class="easyui-dialog" style="width:700px;height:250px;padding:5px 5px;">
				<input type="hidden" id="editId" name="id" />
				<table cellpadding="5">
					<tr>
						<td class="searchName">商品ID：</td>
						<td class="searchText">
							<input id="productId" type="number" style="width:80px" name="productId" />
							<span id="addProductForm_productName" style="width:300px"></span>
						</td>
					</tr>
					<tr>
						<td class="searchName">新人专享价：</td>
						<td class="searchText">
							<input id="salesPrice" type="number" style="width:80px" name="salesPrice" />
							<span>（会同步修改特卖或商城商品价格）</span>
						</td>
					</tr>
					<tr>
						<td class="searchName">展现状态：</td>
						<td class="searchText">
							<input type="radio" name="isDisplay" id="isDisplay1" value="1"/>展现&nbsp;&nbsp;
							<input type="radio" name="isDisplay" id="isDisplay0" value="0" checked="checked" />不展现
						</td>
					</tr>
				</table>
			</div>
			<!-- dialog end -->
		</div>
	</div>
</div>

<script>
	function searchProduct() {
		$('#s_data').datagrid('load', {
			productName : $("#searchForm input[name='productName']").val()
		});
	}
	
	function editIt(index){
	    var arr=$("#s_data").datagrid("getData");
	    $("#editId").val(arr.rows[index].id);
	    $("#productId").val(arr.rows[index].productId);
	    $("#productId").attr("readonly","readonly");
    	$("#salesPrice").val(arr.rows[index].salesPrice);
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==arr.rows[index].isDisplay){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#addGegeProduct').dialog('open');
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
			url: '${rc.contextPath}/product/updateProductNewbieSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('reload');
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	
	
	function displayIt(id,isDisplay){
		var tips = '';
		if(isDisplay == 1){
			tips = '确定启用吗？';
		}else{
			tips = '确定停用吗？'
		}
    	$.messager.confirm("提示信息",tips,function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/product/updateProductNewbieDisplayStatus",{id:id,isDisplay:isDisplay},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('reload');
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	});
	}
	
	function deleteIt(ids){
		$.messager.confirm('提示', '确定删除吗?', function(r){
			if (r){
				$.ajax({
					url: '${rc.contextPath}/product/deleteProductNewbie',
					type:"POST",
					data: {ids:ids},
					success: function(data) {
						if(data.status == 1){
							$('#s_data').datagrid('reload');
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
	
	$(function(){
		
		$("#productId").change(function(){
			var productId = $(this).val();
			if(productId != ''){
				$.ajax({
					type: 'post',
					url: '${rc.contextPath}/productBlacklist/getProductInfo',
					data: {'productId':productId},
					datatype:'json',
					success: function(data){
						if(data.status == 1){
	                		$("#addProductForm_productName").text(data.msg);							
						}else{
							$("#addProductForm_productName").text('');
						}
		            },
		            error: function(xhr){
		            	$("#addProductForm_productName").text('');
		            }
				});	
			}
		});
		//integral dialog  begin
		$('#addGegeProduct').dialog({
			title:'新增商品',
			collapsible:true,
			closed:true,
			modal:true,
			buttons:[{
			    text:'保存',
			    iconCls:'icon-ok',
			    handler:function(){
			    	var params = {};
			    	params.id = $("#editId").val();
			    	params.productId = $.trim($("#productId").val());
			    	params.salesPrice = $.trim($("#salesPrice").val());
			    	params.isDisplay = $("input[name='isDisplay']:checked").val();
			    	if(params.productId == ''){
			    		$.messager.alert("提示","请输入商品Id","error");
			    	}else if(params.salesPrice == '' || parseFloat(params.salesPrice)<=0){
			    		$.messager.alert("提示","新人专享价必填且必须大于0","error");
			    	}else{
			    		$.messager.progress();
		    			$.ajax({
							url: '${rc.contextPath}/product/saveOrUpdateProductNewbie',
							type: 'post',
							dataType: 'json',
							data: params,
							success: function(data){
								$.messager.progress('close');
								if(data.status == 1){
									$.messager.alert("提示","保存成功","info",function(){
										$('#s_data').datagrid("load");
										$('#addGegeProduct').dialog('close');
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
			},{
			    text:'取消',
			    align:'left',
			    iconCls:'icon-cancel',
			    handler:function(){
			        $('#addGegeProduct').dialog('close');
			    }
			}]
	     });
		//integral dialog end
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        idField:'id',
	        url:'${rc.contextPath}/product/jsonProductNewbie',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        pageSize:50,
	        columns:[[
                {field:'id',             title:'序号', align:'center',checkbox:true},
	            {field:'productId',      title:'商品ID', width:15, align:'center'},
	            {field:'productName',    title:'商品名称', width:60, align:'center'},
	            {field:'productType',    title:'商品类型', width:20, align:'center',
	            	formatter:function(value,row,index){
	            		if(row.productType==1){
	            			return '特卖商品';
	            		}else if(row.productType==2){
	            			return '商城商品';
	            		}else{
	            			return '其他';
	            		}
	            	}	
	            },
	            {field:'marketPrice',    title:'原价', width:20, align:'center'},
	            {field:'salesPrice',     title:'新人专享价', width:20, align:'center'},
	            {field:'sequence',       title:'排序', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
	            {field:'isDisplay',    	 title:'展现状态', width:20, align:'center',
	            	formatter:function(value,row,index){
	            		if(row.isDisplay==1){
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
                        	return a+b;
                    	}else{
		                    var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a> | ';
		                    var b = '<a href="javascript:;" onclick="editrow(' + index + ')">改排序</a> | ';
		                    var c = '';
		                    if(row.isDisplay == 1){
		                     	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">不展现</a> | ';
		                    }else if(row.isDisplay == 0) {
		                     	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">展现</a> | ';
		                    }
		                    var d = '<a href="${rc.contextPath}/product/edit/' + row.productType + '/' + row.productId + '" targe="_blank">编辑商品</a> | ';
		                    var e = '<a href="javaScript:;" onclick="deleteIt(' + row.productId + ')">删除</a>'
		                    return a+b+c+d+e;
                    	}
	                }
	            }
	        ]],
	        toolbar:[{
                id:'_add',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                	$("#editId").val("");
                	$("#addProductForm_productName").text('');
			    	$("#productId").val("");
			    	$("#productId").removeAttr("readonly");
			    	$("#salesPrice").val("");
			    	$("#isDisplay0").prop('checked',true);
                	$('#addGegeProduct').dialog('open');
                }
            },'-',{
                iconCls: 'icon-remove',
                text:'批量删除',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        var ids = [];
                        for(var i=0;i<rows.length;i++){
                            ids.push(rows[i].productId)
                        }
                        deleteIt(ids.join(","));
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
	        pagination:true,
	        onLoadSuccess:function(){
            	$("#s_data").datagrid('clearSelections');
            }
	    });
	});
</script>
</body>
</html>