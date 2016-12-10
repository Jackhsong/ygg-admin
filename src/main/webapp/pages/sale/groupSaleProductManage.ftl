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

	<div title="商品组合管理" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>
	
	<!-- 批量修改商品时间 begin -->
	<div id="editProductTime" style="width:750px;height:150px;padding:20px 20px;">
		<input type="hidden" id="productIds" value=""/>
		批量修改开始时间:<input value="" id="editStartTime" name="editStartTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'editEndTime\')}'})"/>&nbsp;&nbsp;&nbsp;
		批量修改结束时间:<input value="" id="editEndTime" name="editEndTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'editStartTime\')}'})"/><br/><br/>
	</div>
	<!-- 批量修改商品时间 end -->
	
	<!-- 增加商品 begin -->
	<div id="add_div" style="width:950px;height:750px;padding:20px 20px;">
		<div id="searchDiv" class="datagrid-toolbar" style="height: 50px;padding: 15px">
        	<form id="searchForm" method="post" >
            	<table>
                <tr>
                	<td>商品ID</td>
                	<td><input id="searchId" name="id" value=""/></td>
                    <td>商品编码</td>
                    <td><input id="searchCode" name="code" value="" /></td>
                    <td>名称</td>
                    <td><input id="searchName" name="name" value="" /></td>
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="searchProduct_clear()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">清除条件</a></td>
                </tr>
                <tr>
                	<td>商家</td>
					<td><input id="sellerId" type="text" name="sellerId" ></input></td>
					<td>品牌</td>
					<td><input id="brandId" type="text" name="brandId" ></input></td>
					<td>备注</td>
					<td><input id="remark" type="text" name="remark" ></input></td>
					<td>
						&nbsp;&nbsp;<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                	</td>
                </tr>
            	</table>
        	</form>
    	</div>
		<!-- 增加商品数据表格 -->
		<table id="product_data" ></table>
	</div>
	<!-- 增加商品 end -->
	
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
    
 	<!-- 调整销量begin -->
   <div id="editSell_div" style="width:450px;height:250px;padding:20px 20px;">
   	<form id="editSellMyForm" method="post">
   		<input type="hidden" id="editSellCurrRefreshId" value="" />
   		<table cellpadding="5">
   			<tr>
    			<td>商品短名称：</td>
    			<td><span id="editSellShortName"></span></td>
   			</tr>
   			<tr>
    			<td>&nbsp;&nbsp;现销量：</td>
    			<td><span id="productSellNum"></span>&nbsp;<input id="refreshSell" type="button" value="刷新" /></td>
   			</tr>
   			<tr>
                   <td>&nbsp;增加销量：</td>
                   <td>
                   	<input class="" id="sellNum" type="text" name="sellNum" />&nbsp;
                   	<span>注：减少可填负数</span>
                   </td>
               </tr>
   		</table>
       </form>
   </div>
 	<!-- 调整销量end -->
	
	<!-- 改价格begin -->
	<div id="updatePrice" class="easyui-dialog" style="width:470px;height:200px;padding:20px 20px;">
	    <form id="updatePrice_form" method="post">
			<input id="updatePrice_form_id" type="hidden" name="id" value="" />
	         <table cellpadding="5">
        		<tr>
                	<td>市场价:</td>
                	<td>
                		<input id="updatePrice_form_marketPrice" name="marketPrice"/>
                	</td>
                </tr>
	             <tr>
	                 <td>售卖价格:</td>
	                 <td><input id="updatePrice_form_price" name="salesPrice" /><font color="red">(注：售价必须小于市场价)</font></td>
	             </tr>
	         </table>
	     </form>
	 </div>
	 <!-- 改价格end -->

	<!-- 改商品名称begin -->
	<div id="updateProductName" class="easyui-dialog" style="width:500px;height:190px;padding:20px 20px;">
        <form id="updateProductName_form" method="post">
		<input id="updateProductName_form_id" type="hidden" name="id" value="" />
         <table cellpadding="5">
             <tr>
                 <td>长名称:</td>
                 <td><input id="updateProductName_name" name="name" style="width:300px"  maxlength="44"></input></td>
             </tr>
             <tr>
                 <td>短名称:</td>
                 <td><input id="updateProductName_shortName" name="shortName"  style="width:300px" maxlength="20" ></input></td>
             </tr>
         </table>
       	</form>
    </div>
	<!-- 改商品名称end -->

    <!-- 从快速添加商品begin -->
    <div id="quickAddProductDiv" class="easyui-dialog" style="width:400px;height:200px;padding:10px 10px;">
    	<input type="hidden" id="quickAddProductDiv_type" value="${type!'1'}"/>
    	<input type="hidden" id="quickAddProductDiv_cid" value="${id?c}"/>
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

<script>

	//禁止按回车
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
			data: {baseId:proudctBaseId, type:${type}},
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
	
	function editSell(index){
		var arr=$("#s_data").datagrid("getData");
		$('#editSellCurrRefreshId').val(arr.rows[index].productId);
		$('#sellNum').val("");
		$('#refreshSell').click();
		$('#editSell_div').dialog('open');
	}

function editPrice(index){
    var arr=$("#s_data").datagrid("getData");
    $("#updatePrice_form_id").val(arr.rows[index].productId);
    var tt=arr.rows[index].salesPrice;
    var marketPrice = arr.rows[index].marketPrice
	$("#updatePrice_form_price").val(tt);
	$("#updatePrice_form_marketPrice").val(marketPrice);
    $("#updatePrice").dialog('open');
}

function editProductName(index){
    var arr=$("#s_data").datagrid("getData");
    $("#updateProductName_form_id").val(arr.rows[index].productId);
    var t1=arr.rows[index].name;
    var t2=arr.rows[index].shortName;
	$("#updateProductName_name").val(t1);
	$("#updateProductName_shortName").val(t2);
    $("#updateProductName").dialog('open');
}

function searchProduct(){
		$('#product_data').datagrid('clearSelections');
    	$('#product_data').datagrid('load',{
        	name:$("#searchName").val(),
        	code:$("#searchCode").val(),
        	brandId:$("input[name='brandId']").val(),
        	sellerId:$("input[name='sellerId']").val(),
        	status:1,
    		productId:$("#searchId").val(),
    		remark:$("#remark").val(),
        	id:${id?c},
        	type:${type}
    	});
	}
	
	function searchProduct_clear(){
		$("#searchName").val("");
		$("#searchCode").val("");
		$('#sellerId').combobox('clear');
		$('#brandId').combobox('clear');
		$("#searchId").val('');
		remark:$("#remark").val();
	}

function deleteIt(id){
    $.messager.confirm("提示信息","确定删除么？",function(re){
        if(re){
            $.messager.progress();
            $.post("${rc.contextPath}/sale/deleteGroupSaleProduct",{id:id},function(data){
                $.messager.progress('close');
                if(data.status == 1){
                    $.messager.alert('响应信息',"删除成功...",'info',function(){
                        $('#s_data').datagrid('reload');
                        return
                    });
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return
                    });
                }
            })
        }
    })
}

	function editProduct(index){
    	var arr=$("#s_data").datagrid("getData");
    	var productId = arr.rows[index].productId;
    	var type = arr.rows[index].type;
    	var urlStr = "${rc.contextPath}/product/edit/"+type+"/"+productId;
    	window.open(urlStr,"_blank");
	}


	function editIt(index){
    	var arr=$("#s_data").datagrid("getData");
    	window.location.href = "${rc.contextPath}/sale/editGroupSale/"+arr.rows[index].id
	}

	function productManage(index){
    	var arr=$("#s_data").datagrid("getData");
    	window.location.href = "${rc.contextPath}/sale/groupSaleProductManage/"+arr.rows[index].id
	}
	
	/**	编辑特卖商品排序值相关   begin*/
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
	
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/sale/updateOrder',
			type:"POST",
			data: {id:row.id,order:row.order},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('reload');
                    return
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return
                    });
                }
			}
		});
	};
	
	function saverow(index){
    	$('#s_data').datagrid('endEdit', index);
	};

	function cancelrow(index){
    	$('#s_data').datagrid('cancelEdit', index);
	};
	/**	编辑特卖商品排序值相关   end*/
	
	
	/**	调整特卖商品库存相关  begin*/
	function editrow2(index){
		$('#s_saleData').datagrid('beginEdit', index);
	};

	function updateActions2(){
		var rowcount = $('#s_saleData').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_saleData').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}

	function saverow2(index){
		$('#s_saleData').datagrid('endEdit', index);
	};

	function cancelrow2(index){
		$('#s_saleData').datagrid('cancelEdit', index);
	};

	function updateActivity2(row){
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
		        } else{
		            $.messager.alert('响应信息',data.message,'error',function(){
		                return
		            });
		        }
			}
		});
	};
	/**	调整特卖商品库存相关  end*/
	
	
	$(function(){

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
                    		var s = '<a href="javascript:void(0);" onclick="saverow2('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow2('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editrow2(' + index + ')">调库存</a> | ';
                    		var b = '<a href="${rc.contextPath}/product/edit/' + row.typeCode + '/' + row.id + '" targe="_blank">查看</a>'
                        	return a+b;
                    	}
                    }
                }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions2();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions2();
            	updateActivity2(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions2();
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
                    $('#s_data').datagrid('reload');
                }
            }]
		});
        
		
		$('#editSell_div').dialog({
            title:'销量调整',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var currRefreshId =$('#editSellCurrRefreshId').val();
                	var sellNum=$('#sellNum').val();
                	$.post("${rc.contextPath}/product/addSellNum",
   						{id:currRefreshId,sellNum:sellNum},
              			function(data){
                      		if(data.status == 1){
                            	$.messager.alert('提示',"保存成功","info")
                            	$('#s_data').datagrid('load');
                            	$('#sellNum').val("")
                            	$('#refreshSell').click();
                       		}else{
                            	$.messager.alert('提示',data.msg,"error")
                          	}
                		},
					"json");
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editSell_div').dialog('close');
                }
            }]
        });
        
        $('#refreshSell').click(function(){
        	var currRefreshId =$('#editSellCurrRefreshId').val();
			$.post("${rc.contextPath}/product/refreshStock",{
				id:currRefreshId
			},function(data){
				if(data.status == 1){
					$('#editSellShortName').text(data.shortName);
					$('#productSellNum').text(data.sell);
				}
			},"json");
		});	

	$('#updatePrice').dialog({
            title:'修改售卖价格',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#updatePrice_form').form('submit',{
                        url:"${rc.contextPath}/product/updateProduct",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#addTemplate').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	$("#updatePrice_form_price").val("");
                                	$("#updatePrice_form_marketPrice").val("");
                                    $('#s_data').datagrid('reload');
                                    $('#updatePrice').dialog('close');
                                });
                            } else if(res.status == 0){
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                });
                            } else{
                                $.messager.alert('响应信息',res.msg,'error',function(){
                                });
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                	$("#updatePrice_form_id").val("");
                	$("#updatePrice_form_price").val("");
                	$("#updatePrice_form_marketPrice").val("");
                    $('#updatePrice').dialog('close');
                }
            }]
        });

		$('#updateProductName').dialog({
            title:'修改商品名字',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#updateProductName_form').form('submit',{
                        url:"${rc.contextPath}/product/updateProduct",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#addTemplate').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	$("#updateProductName_form_id").val("");
                                    $('#s_data').datagrid('reload');
                                    $('#updateProductName').dialog('close');
                                });
                            } else if(res.status == 0){
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                });
                            } else{
                                $.messager.alert('响应信息',res.msg,'error',function(){
                                });
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                	$("#updateProductName_form_id").val("");
                	$("#updateProductName_shortName").val("");
                	$("#updateProductName_name").val("");
                    $('#updateProductName').dialog('close');
                }
            }]
        });
	
		$('#sellerId').combobox({   
			    url:'${rc.contextPath}/seller/jsonSellerCode',   
			    valueField:'code',   
			    textField:'text'  
		});
		
		$('#brandId').combobox({   
			    url:'${rc.contextPath}/brand/jsonBrandCode',   
			    valueField:'code',   
			    textField:'text'  
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sale/jsonGroupSaleProductInfo',
            queryParams: {
				id: ${id?c},
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'x', width:15, align:'center',checkbox:true},
            	{field:'productId',    title:'商品ID', width:15, align:'center'},
                {field:'code',    title:'商品商家编码', width:25, align:'center'},
                {field:'isOffShelves',title:'商品状态', width:15, align:'center'},
                {field:'nameUrl',    title:'长名称', width:50, align:'center'},
                {field:'shortName',    title:'短名称', width:40, align:'center'},
                {field:'remark',    title:'商品备注', width:30, align:'center'},
                {field:'sell',    title:'销量', width:10, align:'center'},
                {field:'stock',    title:'库存', width:10, align:'center'},
                {field:'marketPrice',    title:'原价', width:10, align:'center'},
                {field:'salesPrice',    title:'现价', width:10, align:'center'},
                {field:'sellerName',    title:'商家', width:20, align:'center'},
                {field:'sendAddress',    title:'发货地',width:20, align:'center'},
                {field:'order',    title:'排序值', width:15, align:'center', editor:{type:'validatebox',options:{required:true}}},
                {field:'display',    title:'是否展现', width:15, align:'center'},
                {field:'hidden',  title:'操作', width:100,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var f1 = '<a href="javascript:;" onclick="editrow(' + index + ')">编辑排序</a>';
                    		var f2 = ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                    		var f3 = ' | <a href="javaScript:;" onclick="editProduct(' + index + ')">改商品</a>';
							var b = ' | <a href="javaScript:;" onclick="editPrice(' + index + ')">改价格</a>';
                        	var c = ' | <a href="javaScript:;" onclick="editProductName(' + index + ')">改名称</a>';
                        	var d = ' | <a href="javaScript:;" onclick="editStock(' + index + ')">改库存</a>';
                        	var f = ' | <a href="javaScript:;" onclick="editSell(' + index + ')">改销量</a>';
                        	return f1+f2+f3+b+c+d+f;
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
            toolbar:[{
                id:'back',
                text:'返回商品组合管理',
                iconCls:'icon-back',
                handler:function(){
                	window.location.href = "${rc.contextPath}/sale/groupSaleManage"
                }
            },'-',{
                id:'_add',
                text:'增加商品',
                iconCls:'icon-add',
                handler:function(){
                	$('#product_data').datagrid('reload');
                	$('#add_div').dialog('open');
                }
            },'-',{
            	 id:'_delete',
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
                                    $.post("${rc.contextPath}/sale/deleteGroupSaleProductList", //删除
										{ids: ids.join(",")},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('reload');
											}else{{
												$.messager.alert('提示','删除出错',"error")
											}}
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                 }
            },'-',{
                    iconCls: 'icon-add',
                    text:'全部上架',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('上架','确定上架吗',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].productId)
                                    }
                                    $.post("${rc.contextPath}/product/forSale", //上架
										{ids: ids.join(","),code:0},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('reload');
											}else{{
												$.messager.alert('提示','保存出错',"error")
											}}
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                },'-',{
                    iconCls: 'icon-remove',
                    text:'全部下架',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('下架','确定下架吗',function(b){
                                if(b){
                                    var ids = [];
                                    var relationIds = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].productId)
                                        relationIds.push(rows[i].id);
                                    }
                                    $.post("${rc.contextPath}/product/forSale", //下架
										{ids: ids.join(","),code:1,relationIds: relationIds.join(",")},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('reload');
											}else{{
												$.messager.alert('提示','保存出错',"error")
											}}
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                }
                <#if type==1>
                ,'-',{
                    iconCls: 'icon-edit',
                    text:'批量修改商品时间',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        var time_start = $('#editStartTime').val();
                        var time_edit = $('#editEndTime').val();
                        if(rows.length < 1){
                        	$.messager.alert('提示','请选择要操作的商品',"error");
                        }else{
	                        var ids = [];
	                        for(var i=0;i<rows.length;i++){
	                            ids.push(rows[i].productId);
	                        }
	                        $("#productIds").val(ids.join(","));
	                        $('#editProductTime').dialog('open');
                        }
                    }
                }
               </#if>
               ,'-',{
                   iconCls: 'icon-edit',
                   text:'快速添加商品',
                   handler: function(){
                   	$("#quickAddProductDiv_id").val('');
   					$("#quickAddProductDiv").dialog('open');
                   }
               },'-',{
            	   iconCls: 'icon-print',
                   text:'导出组合商品',
                   handler: function(){
                   		var url = '${rc.contextPath}/sale/exportGroupProduct?id=${id?c}';
                   		window.open(url,"_blank");
                   }
               },'-',{
            	   iconCls: 'icon-add',
                   text:'批量展现',
                   handler: function(){
                	   var rows = $('#s_data').datagrid("getSelections");
                       if(rows.length < 1){
                       	$.messager.alert('提示','请选择要操作的商品',"error");
                       }else{
	                        var ids = [];
	                        for(var i=0;i<rows.length;i++){
	                            ids.push(rows[i].id);
	                        }
	                        displayId(ids.join(","),1);
                       }
                   }
               },'-',{
            	   iconCls: 'icon-remove',
                   text:'批量不展现',
                   handler: function(){
                	   var rows = $('#s_data').datagrid("getSelections");
                       if(rows.length < 1){
                       	$.messager.alert('提示','请选择要操作的商品',"error");
                       }else{
	                        var ids = [];
	                        for(var i=0;i<rows.length;i++){
	                            ids.push(rows[i].id);
	                        }
	                        displayId(ids.join(","),0);
                       }
                   }
               }
                ],
            pagination:true
        });
        
        $('#editProductTime').dialog({
        	title: '修改商品时间',
        	collapsible: true,
        	closed: true,
        	modal: true,
        	buttons: [
        	{
        		text: '保存',
        		iconCls: 'icon-ok',
        		handler: function(){
        			var time_start = $('#editStartTime').val();
                    var time_edit = $('#editEndTime').val();
                    var productIds = $("#productIds").val();
                    if(time_start != "" && time_edit != ""){
	        			$.post(
	        					"${rc.contextPath}/indexSale/updateProductTime",
								{productIds: productIds,startTime:time_start,endTime:time_edit,type:2,saleId:$("#editId").val(),saleId:${id?c}},
								function(data){
									if(data.status == 1){
										$('#editStartTime').val('');
										$('#editEndTime').val('');
										$("#productIds").val('');
										$('#s_data').datagrid('reload');
										$.messager.alert("提示",'保存成功',"info");
										$('#editProductTime').dialog('close');
									}else{{
										$.messager.alert('提示','保存出错',"error")
									}}
								},
							"json");
                    }
                    else{
                    	$.messager.alert('提示','请先填写完开始和结束时间',"error");
                    }
        		}
        	},
        	{
        		text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                	$('#editStartTime').val('');
					$('#editEndTime').val('');
					$("#productIds").val('');
                    $('#editProductTime').dialog('close');
                }
        	}]
        });
        
		$('#quickAddProductDiv').dialog({
            title:'复制商品',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                	var productId = $("#quickAddProductDiv_id").val();
                	var type = $("#quickAddProductDiv_type").val();
                	var activitiesCommonId = $("#quickAddProductDiv_cid").val();
                	if($.trim(productId) == ''){
                		$.messager.alert("提示",'请输入特卖商品ID',"warning");
                	}else{
               			$.messager.progress();
           				$.ajax({
           		            url: '${rc.contextPath}/sale/batchAddGroupSaleProduct',
           		            type: 'post',
           		            dataType: 'json',
           		            data: {'productIds':productId,'type':type, 'activitiesCommonId':activitiesCommonId},
           		            success: function(data){
           		            	$.messager.progress('close');
           		                if(data.status == 1){
           		                	$('#quickAddProductDiv').dialog('close');
           		                	$('#s_data').datagrid('reload');
           		                	$.messager.alert("提示",'添加成功',"error");
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
		
		
        $('#product_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sale/jsonProductList',
            queryParams: {
				id: ${id?c},
				status:0,
				type:${type}
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'序号', width:50, align:'center',checkbox:true},
            	{field:'showId',    title:'商品ID', width:50, align:'center'},
                {field:'code',    title:'商品商家编码', width:60, align:'center'},
                {field:'name',    title:'商品名称', width:60, align:'center'},
                {field:'remark',    title:'商品备注', width:50, align:'center'},
                {field:'brandName',    title:'品牌', width:50, align:'center'},
                {field:'sellerName',    title:'商家', width:50, align:'center'},
                {field:'sendAddress',    title:'发货地', width:50, align:'center'},
                {field:'warehouse',    title:'分仓', width:50, align:'center'},
                {field:'stock',    title:'库存', width:30, align:'center'},
                {field:'marketPrice',    title:'原价', width:30, align:'center'},
                {field:'salesPrice',    title:'现价', width:30, align:'center'}
            ]],
            toolbar:[{
                    iconCls: 'icon-add',
                    text:'增加选中商品',
                    handler: function(){
                        var rows = $('#product_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('添加商品','请确认',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/sale/addGroupSaleProduct", //添加选中商品
										{ids: ids.join(","),activitiesCommonId:${id?c}},
										function(data){
											if(data.status == 1){
												$('#add_div').dialog('close');
												$('#s_data').datagrid('reload');
											}else{{
												$.messager.alert('提示',data.msg,"error")
											}}
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                }],
            pagination:true,
            rownumbers:true
        });
        
   $('#add_div').dialog({
    	title:'商品信息',
    	collapsible:true,
    	closed:true,
    	modal:true,
    	buttons:[]
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
	
	function displayId(id,isDisplay){
		var tip = "";
		if(isDisplay == 0){
			tip = "确定不展现吗？";
		}else{
			tip = "确定展现吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
   	            $.messager.progress();
   	            $.ajax({
   	            	url:'${rc.contextPath}/sale/updateProductDisplayStatus',
   	            	type:'post',
   	            	dataType:'json',
   	            	data:{'id':id,"code":isDisplay},
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
</script>

</body>
</html>