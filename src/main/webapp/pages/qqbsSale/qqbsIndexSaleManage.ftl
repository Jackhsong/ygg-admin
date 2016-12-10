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
<script src="${rc.contextPath}/js/qqbs/sale.js"></script>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
<input type="hidden" value="${rc.contextPath}" id="rcContextPath"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;" id="main_panel">

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
		 <!-- 展示佣金 -->
	<div title="展示佣金" id="showCommision_div"  style="width:900px;height:400px;padding:20px 20px; >
		<div class="content_body">
	        <table id="commision_Data" >
	        </table>
		</div>
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
				window.open('${rc.contextPath}/qqbsSale/jsonSaleWindowInfo?isExport=1&type=' + $("#type").val() 
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
		var urlStr = "${rc.contextPath}/qqbsSale/edit/"+arr.rows[index].id;
		window.open(urlStr,"_blank");
	}
	
	function displayIt(id,code){
    	$.messager.confirm("提示信息","确定修改展现状态吗？",function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/qqbsSale/updateDisplayCode",{id:id,code:code},function(data){
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
			url: '${rc.contextPath}/qqbsSale/updateOrder',
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