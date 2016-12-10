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

</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'手动订单创建商品管理',split:true" style="height:100px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<tr>
                    <td class="searchName">商品编码：</td>
                    <td class="searchText"><input id="searchCode" name="code" value="" /></td>
                    <td class="searchName">商品名称：</td>
		            <td class="searchText"><input id="searchTitle" name="title" value="" /></td>
		            <a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                </tr>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
    		<!-- dialog begin -->
			<div id="addProductLinkDiv" class="easyui-dialog" style="width:700px;height:300px;padding:5px 5px;">
				<form id="addProductLinkDivForm" method="post">
					<table cellpadding="5">
						<tr>
							<td class="searchName">商家：</td>
							<td class="searchText"><input class="inputStyle" id="sellerId" style="width:300px;" /></td>
						</tr>
						<tr id="customCodeTr">
							<td class="searchName">商品ID：</td>
							<td class="searchText">
								<input onblur="showProductName(this)" type="text" class="input-large" id="productId" />
                    			<span style="" class="showPName"></span>
							</td>
						</tr>
						<tr>
							<td class="searchName">生成数量：</td>
							<td class="searchText"><input class="inputStyle" id="nums"/></td>
						</tr>
						<tr>
							<td class="searchName">备注：</td>
							<td class="searchText"><input class="inputStyle" id="remark" maxlength="80"/></td>
						</tr>
					</table>
				</form>
			</div>
			<!-- dialog end -->
   	 		
			<!-- 调库存begin -->
   	 		<div id="editStock_div" style="width:1000px;height:600px;padding:20px 20px;">
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
   	 		<!-- 调库存end -->   	 		
		</div>
	</div>
</div>

<script>

function searchProduct(){
	$('#s_data').datagrid('load',{
    	code:$("#searchCode").val(),
    	name:$("#searchTitle").val()
	});
}

function showProductName(obj){
	var pid = $(obj).val();
	if(pid != ''){
		$.ajax({
	        url: '${rc.contextPath}/product/findProductInfoById',
	        type: 'post',
	        dataType: 'json',
	        data: {'id':pid},
	        success: function(data){
	            if(data.status == 1){
	            	$(obj).next().text(data.msg);
	            }else{
	            	$(obj).next().text("");
	            	$.messager.alert("提示",data.msg,"info");
	            }
	        },
	        error: function(xhr){
	        	$(obj).next().text("");
	        	$.messager.alert("提示",'商品ID填写错误',"info");
	        }
	    });
	}
}

function editrow(index){
	$('#s_saleData').datagrid('beginEdit', index);
};

function updateActions(){
	var rowcount = $('#s_saleData').datagrid('getRows').length;
	for(var i=0; i<rowcount; i++){
		$('#s_saleData').datagrid('updateRow',{
	    	index:i,
	    	row:{}
		});
	}
}

function saverow(index){
	$('#s_saleData').datagrid('endEdit', index);
};

function cancelrow(index){
	$('#s_saleData').datagrid('cancelEdit', index);
};

function updateActivity(row){
	$.ajax({
		url: '${rc.contextPath}/productBase/adjustStock',
		type:"POST",
		data: {productId:row.id,baseId:row.baseId,stock:row.addStock,type:row.typeCode},
		success: function(data) {
			if(data.status == 1){
				$('#s_saleData').datagrid('reload',{
					baseId:row.baseId
				});
				refreshStock();
				refreshAllottedStock();
	            return;
	        } else{
	            $.messager.alert('响应信息',data.message,'error',function(){
	                return
	            });
	        }
		}
	});
};

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

/**调库存*/
function editStock(index){
	var arr=$("#s_data").datagrid("getData");
	var proudctBaseId = arr.rows[index].baseId;
	var type = arr.rows[index].type;
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
				$('#s_saleData').datagrid('reload',{baseId:proudctBaseId});
				$('#editStock_div').dialog('open');
            } else{
                $.messager.alert('响应信息',data.msg,'error');
            }
		}
	});
}

$(function(){
	
	$('#refreshStock').click(function(){
    	var currRefreshId =$('#currRefreshId').val();
		$.post("${rc.contextPath}/product/refreshStock",{
			id:currRefreshId
		},function(data){
			if(data.status == 1){
				$('#shortName').text(data.shortName);
				$('#productStock').text(data.stock);
				$('#productLockNum').text(data.lockNum);
			}
		},"json");
	});
	//integral dialog  begin
	$('#addProductLinkDiv').dialog({
		title:'生成商品购买链接',
		collapsible:true,
		closed:true,
		modal:true,
		buttons:[{
		    text:'保存',
		    iconCls:'icon-ok',
		    handler:function(){
		    	var params = {};
		    	params.sellerId = $('#sellerId').combobox('getValue');
				params.productId = $('#productId').val();
				params.nums = $('#nums').val();
				params.remark = $('#remark').val();
		    	if(params.sellerId == '' || typeof(params.sellerId) == "undefined"){
		    		$.messager.alert("提示","不支持的商家，请在下拉列表中选择商家","warning");
		    	}else if(params.remark == '' || params.nums == '' || params.productId == ''){
		    		$.messager.alert("提示","请填写备注","warning");
		    	}
		    	else{
	    			$.messager.progress();
	    			$.ajax({
						url: '${rc.contextPath}/orderManual/saveOverseasManualProduct',
						type: 'post',
						dataType: 'json',
						data: params,
						success: function(data){
							$.messager.progress('close');
							if(data.status == 1){
								$.messager.alert("提示","创建成功","info",function(){
									$('#s_data').datagrid("load");
									$('#addProductLinkDiv').dialog('close');
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
		        $('#addProductLinkDiv').dialog('close');
		    }
		}]
	});
	
	$('#s_data').datagrid({
	    nowrap: false,
	    striped: true,
	    collapsible:true,
	    idField:'id',
	    url:'${rc.contextPath}/orderManual/jsonOverseasManualProduct',
	    loadMsg:'正在装载数据...',
	    fitColumns:true,
	    remoteSort: true,
	    singleSelect:true,
	    pageSize:20,
	    pageList:[20,40],
	    columns:[[
	        {field:'productId',    title:'商品ID', width:30, align:'center'},
	        {field:'productCount',    title:'生成数量', width:20, align:'center'},
	        {field:'remark',    title:'备注', width:40, align:'center'},
	        {field:'realSellerName',    title:'商家', width:40, align:'center'},
	        {field:'name',    title:'商品名称', width:30, align:'center'},
	        {field:'code',     title:'商品编码',  width:40,   align:'center' },
	        {field:'hidden',  title:'操作', width:100,align:'center',
	            formatter:function(value,row,index){
	                var a = '<a target="_blank" href="'+row.url+'" >去生成订单</a>(必须用13018993661登陆，密码：gegejia123)';
	                var d = ' | <a href="javaScript:;" onclick="editStock(' + index + ')">改库存</a>';
	                return a +d;
	            }
	        }
	    ]],
	    toolbar:[{
	        id:'_add',
	        text:'新增链接',
	        iconCls:'icon-add',
	        handler:function(){
	        	$('#sellerId').combobox("clear");
		    	$("#productId").val("");
		    	$("#nums").val("");
	        	$('#addProductLinkDiv').dialog('open');
	        }
	    }],
	    pagination:true
	});
	
	$('#sellerId').combobox({
		panelWidth:300,
		panelHeight:300,
	    url:'${rc.contextPath}/seller/jsonSellerCode?isAvailable=1&isBirdex=1',
	    valueField:'code',   
	    textField:'text'
	});
	
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
                		var s = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> ';
                    	var c = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                    	return s+c;
                	}else{
                		var a = '<a href="javascript:;" onclick="editrow(' + index + ')">调库存</a> | ';
                		var b = '<a href="${rc.contextPath}/product/edit/' + row.typeCode + '/' + row.id + '" targe="_blank">查看</a>';
                    	return a+b;
                	}
                }
            }
        ]],
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
	
	
	/**调库存弹出框*/
	$('#editStock_div').dialog({
		title:'商品调库存',
		collapsible:true,
		closed:true,
		modal:true,
		buttons:[{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#editStock_div').dialog('close');
                $('#s_data').datagrid('reload');
                $("#s_data").datagrid('clearSelections');
            }
        }]
	});
	
	$('#refreshStock').click(function(){
    	var currRefreshId =$('#currRefreshId').val();
		$.post("${rc.contextPath}/product/refreshStock",{
			id:currRefreshId
		},function(data){
			if(data.status == 1){
				$('#shortName').text(data.shortName);
				$('#productStock').text(data.stock);
				$('#productLockNum').text(data.lockNum);
			}
		},"json");
	});
	
});

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
						$("#totalStock").val('');
						refreshStock();
						refreshAllottedStock();
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
//integral dialog end
</script>
</body>
</html>