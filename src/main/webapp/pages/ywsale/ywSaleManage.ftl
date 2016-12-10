<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络
-后台管理</title>
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

	<div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
        <form id="searchForm" method="post" >
            <table>
            	<tr>
            		<td>特卖状态：</td>
                    <td>
                    <select id="saleStatus" name="saleStatus" style="width:173px;">
                    	<option value="-1">所有状态</option>
                    	<option value="1">即将开始</option>
                    	<option value="2">进行中（早场和晚场）</option>
                    	<option value="3">已结束</option>
                    </select>
                    </td>
                    
                    <td>&nbsp;特卖类型：</td>
                    <td>
                    <select id="type" name="type" style="width:173px;">
                    	<option value="-1">全部</option>
                    	<option value="1">单品</option>
                    	<option value="2">组合</option>
                    </select>
                    </td>
                    <td>&nbsp;特卖名称：</td>
                    <td><input id="name" name="name" value="" /></td>
                    <td>&nbsp;开售档期：</td>
                    <td><input id="startTime" name="startTime" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" /></td>
                    <td>&nbsp;商家：</td>
		            <td><input id="sellerId" type="text" name="sellerId" /></td>
                </tr>
                <tr>
                    <td>展现状态：</td>
                    <td>
                    	<select id="isDisplay" name="isDisplay" style="width:173px;">
	                    	<option value="-1">全部</option>
	                    	<option value="0">不展现</option>
	                    	<option value="1">展现</option>
                    	</select>
                    </td>
                    <td>&nbsp;单品/组合Id：</td>
                    <td><input id="productId" name="productId" value="" /></td>
                    <td>&nbsp;商品Id：</td>
                    <td><input id="pId" name="pId" value="" /></td>
                    <td>&nbsp;商品名称：</td>
                    <td><input id="productName" name="productName" value="" /></td>
                    <td>&nbsp;品牌：</td>
                    <td><input id="brandId" type="text" name="brandId" ></input></td>
					<td>
						&nbsp;<a id="searchBtn" onclick="searchSaleWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						&nbsp;<a id="searchBtn" onclick="exportSaleWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">导出</a>
                	</td>
                	<td></td>
                </tr>
            </table>
        </form>
    </div>
    <!--数据表格-->
    <table id="s_data" style=""></table>

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

	function searchSaleWindow(){
    	$('#s_data').datagrid('load',{
        	type:$("#type").val(),
        	name:$("#name").val(),
        	saleStatus:$("#saleStatus").val(),
        	isDisplay:$("#isDisplay").val(),
        	startTime:$("#startTime").val(),
        	productId:$("#productId").val(),
        	pId:$("#pId").val(),
        	productName:$("#productName").val(),
        	brandId:$("input[name='brandId']").val(),
        	sellerId:$("#sellerId").combobox('getValue')
    	});
	}
	
	function exportSaleWindow() {
		$.messager.confirm('提示', '确定导出列表', function(r) {
			if(r) {
				window.open('${rc.contextPath}/ywSale/jsonSaleWindowInfo?isExport=1&type=' + $("#type").val() 
						+ '&name=' + $("#name").val()
						+ '&saleStatus=' + $("#saleStatus").val()
						+ '&isDisplay=' + $("#isDisplay").val()
						+ '&startTime=' + $("#startTime").val()
						+ '&productId=' + $("#productId").val()
						+ '&pId=' + $("#pId").val()
						+ '&productName=' + $("#productName").val()
						+ '&brandId=' + $("input[name='brandId']").val()
						+ '&sellerId=' + $("#sellerId").combobox('getValue')
				);
			}
		});
	}
	

	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr = "${rc.contextPath}/ywSale/edit/"+arr.rows[index].id;
		window.open(urlStr,"_blank");
	}
	
	function displayIt(id,code){
    	$.messager.confirm("提示信息","确定修改展现状态吗？",function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/ywSale/updateDisplayCode",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('reload');
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

	
	//跟新导航排序值
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/ywSale/updateOrder',
			type:"POST",
			data: {id:row.id,order:row.order},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('reload');
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	//编辑排序
	function editOrder(index){
		$('#s_data').datagrid('beginEdit', index);
	};
	// 保存保存
	function saverow(index){
		$('#s_data').datagrid('endEdit', index);
	};
	// 取消编辑
	function cancelrow(index){
		$('#s_data').datagrid('cancelEdit', index);
	};
	// 跟新gird行数据
	function updateActions(){
		var rowcount = $('#s_data').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_data').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}
	
	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/ywSale/jsonSaleWindowInfo',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pagination:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'特卖ID', width:20, align:'center'},
            	{field:'displayId',    title:'组特或单品ID', width:30, align:'center'},
                {field:'isDisplay',    title:'展现状态', width:30, align:'center'},
                {field:'saleStatus',    title:'特卖状态', width:30, align:'center'},
                {field:'order',    title:'排序值', width:30, align:'center',editor:{type:'numberbox'}},
                {field:'saleTimeTypeStr',    title:'场次', width:30, align:'center'},
                {field:'type',    title:'特卖类型', width:30, align:'center'},
                {field:'relaRealSellerName',    title:'关联商家', width:60, align:'center'},
                {field:'name',     title:'名称',  width:50,   align:'center' },
                {field:'desc',     title:'特卖描述',  width:50,   align:'center' },
                {field:'stock',     title:'库存数量',  width:25,   align:'center' },
                {field:'startTime',     title:'开始时间',  width:50,   align:'center' },
                {field:'endTime',     title:'结束时间',  width:50,   align:'center' },
                {field:'descToWeb',     title:'查看',  width:15,   align:'center' },
                {field:'urlForQqbs',     title:'燕网链接',  width:25,   align:'center' },
                {field:'hidden',  title:'操作', width:65,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing) {
    						var a = '<a href="javascript:void(0);" onclick="saverow(' + index + ')">保存</a> | ';
    						var b = '<a href="javascript:void(0);" onclick="cancelrow(' + index + ')">取消</a>';
    						return a + b;
    					} else {
    						var a = '<a href="javaScript:;" onclick="editOrder(' + index + ')">改排序</a> | <a href="javaScript:;" onclick="editIt(' + index + ')">修改</a> | ';
                            var b = '';
                            if(row.isDisplayCode == 1){
                            	b = '<a href="javaScript:;" onclick="displayIt(' + '\'' + row.id + '\',' + row.isDisplayCode + ')">不展现</a>';
                            }else{
                            	b = '<a href="javaScript:;" onclick="displayIt(' + '\'' + row.id + '\',' + row.isDisplayCode + ')">展现</a>';
                            }
                            var c = '';
                            if(row.typeCode == 1){
                            	c = ' | <a href="javaScript:;" onclick="editStock(' + '\'' + index + '\')">改库存</a>';
                            }else{
                            	c = ' | ______';
                            }
                            return a+b+c;
    					}
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增特卖',
                iconCls:'icon-add',
                handler:function(){
                    window.location.href = "${rc.contextPath}/ywSale/addSale"
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
        	}
        });
		
		$('#brandId').combobox({
            panelWidth:350,
            panelHeight:350,
            url:'${rc.contextPath}/brand/jsonBrandCode',
            valueField:'code',
            textField:'text'
        });
		
		$('#sellerId').combobox({
			panelWidth:350,
        	panelHeight:350,
			mode:'remote',
		    url:'${rc.contextPath}/seller/jsonSellerCode',   
		    valueField:'code',   
		    textField:'text'  
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
	});
	
	

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
function editStock(index){
	var arr=$('#s_data').datagrid("getData");
	var proudctBaseId = arr.rows[index].baseId;
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
</script>

</body>
</html>