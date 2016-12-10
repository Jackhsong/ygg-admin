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
	<div style="padding:10px">
		<span style="color:red">${timeStr}</span>&nbsp;&nbsp;
		<#if showButtom?exists>明日<#else>今日</#if>
		在售档期（包括10点及20点档期）&nbsp;&nbsp;&nbsp;&nbsp;
		<#if showButtom?exists><input style="width: 150px" type="button" id="resetButton" value="重置明日排序值"/></#if>
		&nbsp;&nbsp;&nbsp;&nbsp;特卖名称：<input type="text" id="searchName" value=""/>&nbsp;
		<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
	</div>
	
    <!--数据表格1-->
    
    <table id="s_data1" style=""></table>
    
    <br/>
    <div style="padding:10px">
		&nbsp;&nbsp;
		<#if showButtom?exists>明日<#else>今日</#if>
		将售档期（包括10点及20点档期）
	</div>
    
    <!--数据表格2-->
    <table id="s_data2" style=""></table>

	<!-- 调库存begin -->
	<div id="editStock_div" style="width:1000px;height:600px;padding:20px 20px;">
   		<input type="hidden" id="dataId" value="" />
		<p>
			商品名称：<span id="span_baseName"></span>&nbsp;&nbsp;
			商品条码：<span id="span_barcode"></span>&nbsp;&nbsp;
			商品编码：<span id="span_code"></span>&nbsp;&nbsp;
			商家名称：<span id="span_sellerName"></span>&nbsp;&nbsp;
		</p>
		<p>
			发货类型：<span id="span_sendType"></span>&nbsp;&nbsp;
			发货地：<span id="span_sendAddress"></span>&nbsp;&nbsp;
			分仓：<span id="span_warehouse"></span>&nbsp;&nbsp;
		</p>
		<p>
			剩余未分配总库存：<span id="span_availableStock"></span>&nbsp;<a onclick="refreshStock();" href="javascript:;" class="easyui-linkbutton">刷新</a>&nbsp;&nbsp;
			已分配总库存：<span id="span_allottedStock"></span>&nbsp;<a onclick="refreshAllottedStock();" href="javascript:;" class="easyui-linkbutton">刷新</a>&nbsp;&nbsp;
			增加库存：<input type="text" style="width:40px;" name="totalStock" id="totalStock" value="" maxlength="10"/>
			<input type="hidden" name="baseId" id="baseId" value="" maxlength="10"/>
			<a onclick="addStock();" href="javascript:;" class="easyui-linkbutton">增加</a>
			<font color="red" style="italic">(注：减少可填负数)</font>
		</p>
		<table id="s_saleData" ></table>
	</div>

</div>

<script>
	
	/**搜索*/
	function searchProduct(){
		$('#s_data1').datagrid('load',{
			saleName:$("#searchName").val(),
			type: 2,
			day: ${day},
			orderType:${orderType}
		});
	}
	
	
	/**调库存 begin*/
	function clearEditStock(){
		$("#span_baseName").text('');
		$("#span_barcode").text('');
		$("#span_code").text('');
		$("#span_sellerName").text('');
		$("#span_sendType").text('');
		$("#span_sendAddress").text('');
		$("#span_warehouse").text('');
		$("#span_availableStock").text('');
		$("#span_allottedStock").text('');
		$("#baseId").val('');
	}
	function editStock(domid,index){
		var arr=$('#'+domid).datagrid("getData");
		var proudctBaseId = arr.rows[index].baseId;
		$('#dataId').val(domid);
		$.ajax({
			url: '${rc.contextPath}/productBase/findProductInfoByBaseId',
			type:"POST",
			data: {baseId:proudctBaseId},
			success: function(data) {
				if(data.status == 1){
					clearEditStock();
					$("#span_baseName").text(data.baseName);
					$("#span_barcode").text(data.barcode);
					$("#span_code").text(data.code);
					$("#span_sellerName").text(data.sellerName);
					$("#span_sendType").text(data.sendType);
					$("#span_sendAddress").text(data.sendAddress);
					$("#span_warehouse").text(data.warehouse);
					$("#span_availableStock").text(data.availableStock);
					$("#span_allottedStock").text(data.allottedStock);
					$("#baseId").val(data.baseId);
					$('#s_saleData').datagrid('load',{baseId:proudctBaseId});
					$('#editStock_div').dialog('open');
	            } else{
	                $.messager.alert('响应信息',data.msg,'error');
	            }
			}
		});
	}
	

	function editProductStock(index){
		$('#s_saleData').datagrid('beginEdit', index);
	};

	function updateProductStockAction(){
		var rowcount = $('#s_saleData').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_saleData').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}

	function saveProductStock(index){
		$('#s_saleData').datagrid('endEdit', index);
	};

	function cancelProductStock(index){
		$('#s_saleData').datagrid('cancelEdit', index);
	};

	function updateProductStockActivity(row){
		$.ajax({
			url: '${rc.contextPath}/productBase/adjustStock',
			type:"POST",
			data: {productId:row.id,baseId:row.baseId,stock:row.addStock,type:row.typeCode},
			success: function(data) {
				if(data.status == 1){
					$('#s_saleData').datagrid('load',{baseId:row.baseId})
					refreshStock();
					refreshAllottedStock();
		            return
		        } else{
		            $.messager.alert('响应信息',data.message,'error',function(){
		                return
		            });
		        }
			}
		});
	};
	
	<!--增加基本商品库存-->
	function addStock(){
		var totalStock = $("#totalStock").val();
		if($.trim(totalStock)==''){
			$.messager.alert("提示","请输入库存数量","error");
		}else{
			if(/^-?\d+$/.test(totalStock)){
				$.ajax({
					url:'${rc.contextPath}/productBase/addTotalStock',
					type:'post',
					data:{'stock':totalStock,'baseId':$("#baseId").val()},
					dataType:'json',
					success:function(data){
						if(data.status==1){
							$.messager.alert("提示", '保存成功', "info");
							refreshStock();
							refreshAllottedStock();
							$("#totalStock").val('');
						}else{
							$.messager.alert("提示", data.msg, "error");
						}
					},
					error:function(){
						
					}
				});
			}else{
				$.messager.alert("提示","库存数量只能为数字","error");
			}
		}
	}
	
	//刷新库存
	function refreshStock(){
		$.ajax({
			url:'${rc.contextPath}/productBase/refreshStock',
			type:'post',
			data:{'id':$("#baseId").val()},
			dataType:'json',
			success:function(data){
				if(data.status==1){
					$("#span_availableStock").text(data.stock);
				}
			}
		});
	}
	
	function refreshAllottedStock(){
		$.ajax({
			url:'${rc.contextPath}/productBase/refreshAllottedStock',
			type:'post',
			data:{'id':$("#baseId").val()},
			dataType:'json',
			success:function(data){
				if(data.status==1){
					$("#span_allottedStock").text(data.stock);
				}
			}
		});
	}
	/**	调库存  end*/

	function editSaleWindow(domid,index){
    	var arr=$('#'+domid).datagrid("getData");
    	var urlStr = "${rc.contextPath}/indexSale/edit/"+arr.rows[index].id;
    	window.open(urlStr,"_blank");
	}

	function editrow(domid,index){
    	$('#'+domid).datagrid('beginEdit', index);
	};
	
	function updateActions(domid){
    	var rowcount = $('#'+domid).datagrid('getRows').length;
    	for(var i=0; i<rowcount; i++){
        	$('#'+domid).datagrid('updateRow',{
            	index:i,
            	row:{}
        	});
    	}
	}
	
	function updateActivity(row,type,domid){
		$.ajax({
			url: '${rc.contextPath}/indexSale/updateOrder',
			type:"POST",
			data: {id:row.id,order:row.order,type:type},
			success: function(data) {
				if(data.status == 1){
					$('#'+domid).datagrid('load');
                    return
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return
                    });
                }
			}
		});
	};

	function saverow(domid,index){
   		$('#'+domid).datagrid('endEdit', index);
	};
	
	function cancelrow(domid,index){
    	$('#'+domid).datagrid('cancelEdit', index);
	};

	function displayIt(domId,id,code){
    	$.messager.confirm("提示信息","确定修改展现状态吗？",function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/indexSale/updateDisplayCode",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#'+domId).datagrid('reload');
                        return
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error',function(){
                        	return
                    	});
                	}
            	})
        	}
    	})
	}
	
	$(function(){
		
		$('#resetButton').click(function(){
			$.messager.confirm("提示信息","确定马上更新吗么？",function(re){
				 if(re){
					 $.messager.progress();
				 	$.post("${rc.contextPath}/indexSale/resetTomorrowOrder",{},function(data){
						$.messager.progress('close');
                		if(data.status == 1){
                    	$.messager.alert('响应信息',"更新成功...",'info',function(){
                    		$('#s_data1').datagrid('load');
                        	return
                    	});
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return
                    });
                }
            })
				 }
			});
		});
		
		<!--加载调库存列表begin-->
		$('#s_saleData').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/productBase/jsonProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:30,
            pageList:[30,40],
            columns:[[
            	{field:'id',    title:'商品Id', width:20, align:'center'},
            	{field:'name',    title:'商品名称', width:90, align:'center'},
                {field:'type',    title:'商品类型', width:30, align:'center'},
                {field:'time',    title:'特卖时间', width:50, align:'center'},
                {field:'remark',    title:'商品备注', width:60, align:'center'},
                {field:'addStock',    title:'增加库存', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'stock',    title:'库存', width:20,align:'center'},
                {field:'available',    title:'剩余可用', width:20,align:'center'},
                {field:'lock',    title:'已锁定', width:20,align:'center'},
                {field:'isAvailable',    title:'是否可用', width:20,align:'center'},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saveProductStock('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelProductStock('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editProductStock(' + index + ')">调库存</a> | ';
                    		var b = '<a href="${rc.contextPath}/product/edit/' + row.typeCode + '/' + row.id + '" targe="_blank">查看</a>'
                        	return a+b;
                    	}
                    }
                }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateProductStockAction();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateProductStockAction();
            	updateProductStockActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateProductStockAction();
        	},
            pagination:true
        });
		<!--加载调库存列表end-->
		
		
		/**调库存弹出框*/
		$('#editStock_div').dialog({
    		title:'调库存',
    		collapsible:true,
    		closed:true,
    		modal:true,
    		buttons:[{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    var domId = $('#dataId').val();
                    $('#editStock_div').dialog('close');
                    $('#'+domId).datagrid('reload');
                }
            }]
		});
		
		$('#s_data1').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/indexSale/todayJsonSaleWindowInfo',
            queryParams: {
				type: 2,
				day: ${day},
				orderType:${orderType}
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:2000,
            pageList:[2000,3000,4000],
            columns:[[
                {field:'order',    title:'排序值', width:20, align:'center', editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	var domId = "s_data1";
                    	if (row.editing){
                    		var save = '<a href="javascript:void(0);" onclick="saverow('+'\''+domId+'\','+index+')">保存</a> ';
                        	var cancel = '<a href="javascript:void(0);" onclick="cancelrow('+'\''+domId+'\','+index+')">取消</a>';
                        	return save+cancel;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editrow('+'\''+domId+'\','+index+')">改排序</a>';
                    		var b = '';
                    		var c = '';
                            if(row.isDisplayCode == 1){
                            	b = ' | <a href="javaScript:;" onclick="displayIt(' + '\'' + domId + '\',' + '\'' + row.id + '\',' + 0 + ')">改为不展现</a>';
                            }else{
                            	b = ' | <a href="javaScript:;" onclick="displayIt(' + '\'' + domId + '\',' + '\'' + row.id + '\',' + 1 + ')">改为展现</a>';
                            }
                            if(row.typeCode == 1){
                            	c = ' | <a href="javaScript:;" onclick="editStock(' + '\'' + domId + '\',' + '\'' + index + '\')">改库存</a>';
                            }else{
                            	c = ' | ______';
                            }
                        	return a+b+c;
                    	}
                    }
                },
                {field:'descToWeb',     title:'查看',  width:10,   align:'center' },
                {field:'isDisplay',    title:'展现状态', width:20, align:'center'},
                {field:'saleStatus',    title:'特卖状态', width:20, align:'center'},
                {field:'type',    title:'特卖类型', width:20, align:'center'},
                {field:'name',     title:'名称',  width:50,   align:'center' ,
                	formatter:function(value,row,index){
                		var domId = "s_data1";
                		var f1 = '<a href="javascript:;" onclick="editSaleWindow('+'\''+domId+'\','+index+')">'+row.name+'</a>';
                    	return f1;
                    }
                },
                {field:'desc',     title:'特卖描述',  width:50,   align:'center' },
                {field:'stock',     title:'库存数量',  width:20,   align:'center' },
                {field:'saleTimeTypeStr',     title:'场次',  width:30,   align:'center' },
                {field:'startTime',     title:'开始时间',  width:35,   align:'center' },
                {field:'endTime',     title:'结束时间',  width:35,   align:'center' }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions("s_data1");
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions("s_data1");
            	updateActivity(row,${orderType},"s_data1");
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions("s_data1");
        	},
            pagination:true
        });
        
        $('#s_data2').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/indexSale/todayJsonSaleWindowInfo',
            queryParams: {
				type: 1,
				day: ${day},
				orderType:${orderType}
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:2000,
            pageList:[2000,3000,4000],
            columns:[[
                {field:'order',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	var domId = "s_data2";
                    	if (row.editing){
                    		var save = '<a href="javascript:void(0);" onclick="saverow('+'\''+domId+'\','+index+')">保存</a> ';
                        	var cancel = '<a href="javascript:void(0);" onclick="cancelrow('+'\''+domId+'\','+index+')">取消</a>';
                        	return save+cancel;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editrow('+'\''+domId+'\','+index+')">改排序</a>';
                    		var b = '';
                    		var c = '';
                        	if(row.isDisplayCode == 1){
                            	b = ' | <a href="javaScript:;" onclick="displayIt(' + '\'' + domId + '\',' + '\'' + row.id + '\',' + 0 + ')">改为不展现</a>';
                            }else{
                            	b = ' | <a href="javaScript:;" onclick="displayIt(' + '\'' + domId + '\',' + '\'' + row.id + '\',' + 1 + ')">改为展现</a>';
                            }
                        	if(row.typeCode == 1){
                            	c = ' | <a href="javaScript:;" onclick="editStock(' + '\'' + domId + '\',' + '\'' + index + '\')">改库存</a>';
                            }else{
                            	c = ' | ______';
                            }
                        	return a+b+c;
                    	}
                    }
                },
                {field:'descToWeb',     title:'查看',  width:10,   align:'center' },
                {field:'isDisplay',    title:'展现状态', width:20, align:'center'},
                {field:'saleStatus',    title:'特卖状态', width:20, align:'center'},
                {field:'type',    title:'特卖类型', width:20, align:'center'},
                {field:'name',     title:'名称',  width:50,   align:'center' ,
                	formatter:function(value,row,index){
                		var domId = "s_data2";
                		var f1 = '<a href="javascript:;" onclick="editSaleWindow('+'\''+domId+'\','+index+')">'+row.name+'</a>';
                    	return f1;
                    }
                },
                {field:'desc',     title:'特卖描述',  width:50,   align:'center' },
                {field:'stock',     title:'库存数量',  width:20,   align:'center' },
                {field:'saleTimeTypeStr',     title:'场次',  width:30,   align:'center' },
                {field:'startTime',     title:'开始时间',  width:35,   align:'center' },
                {field:'endTime',     title:'结束时间',  width:35,   align:'center' }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions("s_data2");
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions("s_data2");
            	updateActivity(row,${orderType},"s_data2");
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions("s_data2");
        	},
            pagination:true
        });
	})
</script>

</body>
</html>