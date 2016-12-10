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
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
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
		<div data-options="region:'north',title:'库存告急列表',split:true" style="height: 80px;">
			<div id="searchDiv" style="height: 60px;padding: 10px">
				<form action="${rc.contextPath}/product/exportStockList" method="post" id="searchForm">
		            <table class="search">
		                <tr>
		                	<td class="searchName">商品ID：</td>
							<td class="searchText"><input id="searchId" name="searchId" value="" /></td>
							<td class="searchName">商品名称：</td>
		                    <td class="searchText"><input id="searchTitle" name="title" value="" /></td>
		                    <td class="searchText">
								<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
			                	&nbsp;
			                	<a id="submit"  onClick="exportResult();" href="javascript:;"  class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出</a>
			                </td>
		                </tr>
		            </table>
	            </form>
    		</div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>

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

function exportResult(){
	$("#searchForm").submit();
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
				$('#s_saleData').datagrid('load',{
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

	function checkEnter(e){
		var et=e||window.event;
		var keycode=et.charCode||et.keyCode;
		if(keycode==13){
			if(window.event)
				window.event.returnValue = false;
			else
				e.preventDefault();//for firefox
		}
	}
	

	function searchProduct(){
    	$('#s_data').datagrid('load',{
        	productId:$("#searchId").val(),
        	productName:$("#searchTitle").val()
    	});
	}
	
	function clearSearch(){
    	$("#searchTitle").val('');
    	$("#searchId").val('');
	}
	
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
	


	$(function(){
	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/product/jsonProductStockInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'typeStr',    title:'商品类型', width:20,align:'center'},
                /* {field:'isOffShelves',    title:'上下架状态', width:20, align:'center'}, */
                {field:'id',    title:'商品Id', width:20, align:'center'},
                {field:'productName',     title:'长名称',  width:70,  align:'center'},
                {field:'remark',     title:'备注',  width:40,   align:'center'},
                {field:'startTime',     title:'特卖开始时间',  width:35,   align:'center'},
                {field:'endTime',     title:'特卖结束时间',  width:35,   align:'center'},
                {field:'stock',      title:'剩余库存',  width:15,   align:'center'},
                {field:'lock',     title:'锁定库存',  width:15,   align:'center'},
                {field:'availableStock',     title:'可用库存',  width:15,   align:'center'},
                {field:'salesPrice',     title:'售价',  width:15,   align:'center'},
                {field:'sellerName',     title:'商家',  width:30,   align:'center' },
                {field:'sendAddress',     title:'发货地',  width:30,   align:'center' },
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editStock(' + index + ')">改库存</a>';
                        return a;
                    }
                }
            ]],
            pagination:true
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
    		title:'特卖商品调库存',
    		collapsible:true,
    		closed:true,
    		modal:true,
    		buttons:[{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editStock_div').dialog('close');
                    $("#s_data").datagrid('clearSelections');
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
</script>

</body>
</html>