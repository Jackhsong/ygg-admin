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
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'积分换购商品管理',split:true" style="height: 100px;">
			<div style="height: 40px;padding: 10px">
	            <table>
	                <tr>
	                    <td >商品ID：</td>
	                    <td ><input id="searchProductId" name="productId" value="" /></td>
	                    <td >商品编码：</td>
	                    <td ><input id="searchProductCode" name="productCode" value="" /></td>
	                    <td >商品长名称：</td>
	                    <td ><input id="searchProductName" name="productName" value="" /></td>
	                    <td >商品短名称：</td>
	                    <td ><input id="searchShortName" name="shortName" value="" /></td>
	                 </tr>
	                 <tr>
	                 	<td >商品备注：</td>
	                    <td ><input id="searchRemark" name="remark" value="" /></td>
	                    <td >品牌：</td>
	                	<td ><input id="searchBrandId" name="brandId" value=""/></td >
	                    <td >商家：</td>
	                	<td ><input id="searchSellerId" name="sellerId" value=""/></td >
	                	<td ></td>
						<td ><a id="searchBtn" onclick="searchCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
	                </tr>
	            </table>
			</div>
		</div>
			
		<div data-options="region:'center'" >
	        <table id="s_data" ></table>

		    <!-- 新增 begin -->
		    <div id="editMemberProductDiv" class="easyui-dialog" style="width:500px;height:300px;padding:15px 20px;">
		        <form id="editMemberProductForm" method="post">
					<input id="editMemberProductForm_id" type="hidden" name="id" value="0" >
					<input id="editMemberProductForm_levelId" type="hidden" name="levelId" value="${levelId}" >
					<input id="editMemberProductForm_level" type="hidden" name="level" value="${level}" >
					<p>
						<span>&nbsp;商品ID：</span>
						<span><input type="number" name="productId" id="editMemberProductForm_productId" value="" maxlength="32" style="width: 300px;"/></span>
					</p>
					<p>
						<span>商品名称：</span>
						<span id="editMemberProductForm_name"></span>
					</p>
					<p>
						<span>&nbsp;积分价：</span>
						<span><input type="number" name="point" id="editMemberProductForm_point" value="" maxlength="32" style="width: 300px;"/></span>
					</p>
					<p>
						<span>限购数量：</span>
						<span><input type="number" name="limitNum" id="editMemberProductForm_limitNum" value="" maxlength="32" style="width: 300px;"/></span>
					</p>
					<p>
						<span>现价购买：</span>
						<span>
							<input type="radio" name="isSupportCashBuy" id="editMemberProductForm_isSupportCashBuy1" value="1"/>支持&nbsp;&nbsp;&nbsp;
							<input type="radio" name="isSupportCashBuy" id="editMemberProductForm_isSupportCashBuy0" value="0"/>不支持
						</span>
					</p>
					<p>
						<span>&nbsp;排序值：</span>
						<span><input type="number" name="sequence" id="editMemberProductForm_sequence" value="" maxlength="32" style="width: 300px;"/></span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
			
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
			shortName:$("#searchShortName").val(),
			remark:$("#searchRemark").val(),
			brandId:$("#searchBrandId").combobox('getValue'),
			sellerId:$("#searchSellerId").combobox('getValue'),
			levelId:${levelId}
		});
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
		var proudctBaseId = arr.rows[index].productBaseId;
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
	    var t1=arr.rows[index].productName;
	    var t2=arr.rows[index].productShortName;
		$("#updateProductName_name").val(t1);
		$("#updateProductName_shortName").val(t2);
	    $("#updateProductName").dialog('open');
	}

	function deleteIt(id){
	    $.messager.confirm("提示信息","确定删除吗？",function(re){
	        if(re){
	            $.messager.progress();
	    		$.ajax({
	    			url: '${rc.contextPath}/member/deleteMemberProduct',
	    			type:"POST",
	    			data: {ids:id},
	    			success: function(data) {
	    				if(data.status == 1){
	    					$.messager.progress('close');
	                        $.messager.alert('响应信息',"删除成功...",'info');
	                        $('#s_data').datagrid('clearSelections');
	                        $('#s_data').datagrid('reload');
	                    } else{
	                        $.messager.alert('响应信息',data.msg,'error');
	                    }
	    			},
	    			error: function(xhr){
		            	$.messager.progress('close');
		            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		            }
	    		});
	        }
	    })
	}

	function forSale(ids,code){
		var tip = '';
		if(code == 1){
			tip = '确定下架吗？';
		}else{
			tip = '确定上架吗？';
		}
		$.messager.confirm("提示信息",tip,function(re){
	        if(re){
	            $.messager.progress();
	    		$.ajax({
	    			url: '${rc.contextPath}/product/forSale',
	    			type:"POST",
	    			data: {ids:ids,code:code},
	    			success: function(data) {
	    				if(data.status == 1){
	    					$.messager.progress('close');
	                        $.messager.alert('响应信息',"操作成功...",'info');
	                        $('#s_data').datagrid('clearSelections');
	                        $('#s_data').datagrid('reload');
	                    } else{
	                        $.messager.alert('响应信息',data.msg,'error');
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
	
	function displayIt(ids,isDisplay){
		var tip = '';
		if(isDisplay == 1){
			tip = '确定展现吗？';
		}else{
			tip = '确定不展现吗？';
		}
		$.messager.confirm("提示信息",tip,function(re){
	        if(re){
	            $.messager.progress();
	    		$.ajax({
	    			url: '${rc.contextPath}/member/updateMemberProductDisplayStatus',
	    			type:"POST",
	    			data: {ids:ids,isDisplay:isDisplay},
	    			success: function(data) {
	    				if(data.status == 1){
	    					$.messager.progress('close');
	                        $.messager.alert('响应信息',"操作成功...",'info');
	                        $('#s_data').datagrid('clearSelections');
	                        $('#s_data').datagrid('reload');
	                    } else{
	                        $.messager.alert('响应信息',data.msg,'error');
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
	function editProduct(index){
    	var arr=$("#s_data").datagrid("getData");
    	var productId = arr.rows[index].productId;
    	var type = arr.rows[index].productType;
    	var urlStr = "${rc.contextPath}/product/edit/"+type+"/"+productId;
    	window.open(urlStr,"_blank");
	}


	function clearEditMemberProductForm(){
		$("#editMemberProductForm_id").val('0');
		$("#editMemberProductForm input[type='number']").each(function(){
			$(this).val('');
		});
		$('#editMemberProductForm_name').text('');
		$("input[name='isSupportCashBuy']").each(function(){
			$(this).prop('checked',false);
		});
	}
	function editIt(index){
		clearEditMemberProductForm();
    	var arr=$("#s_data").datagrid("getData");
    	$("#editMemberProductForm_id").val(arr.rows[index].id);
    	$("#editMemberProductForm_productId").val(arr.rows[index].productId);
    	$("#editMemberProductForm_point").val(arr.rows[index].point);
    	$("#editMemberProductForm_limitNum").val(arr.rows[index].limitNum);
    	$("input[name='isSupportCashBuy']").each(function(){
    		if(arr.rows[index].isSupportCashBuy==$(this).val()){
    			$(this).prop('checked',true);
    		}
    	});
    	$("#editMemberProductForm_sequence").val(arr.rows[index].sequence);
    	$("#editMemberProductDiv").dialog('open');
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
			url: '${rc.contextPath}/member/updateMemberProductSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('clearSelections');
					$('#s_data').datagrid('reload');
                    return;
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return;
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
	
		$('#searchSellerId').combobox({   
			    url:'${rc.contextPath}/seller/jsonSellerCode',   
			    valueField:'code',   
			    textField:'text'  
		});
		
		$('#searchBrandId').combobox({   
			    url:'${rc.contextPath}/brand/jsonBrandCode',   
			    valueField:'code',   
			    textField:'text'  
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/member/jsonMemberProductInfo',
            queryParams: {
            	levelId:${levelId}
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'x', width:15, align:'center',checkbox:true},
            	{field:'isDisplay',    title:'展现状态', width:15, align:'center',
            		formatter:function(value,row,index){
            			if(row.isDisplay == 1){
            				return "展现";
            			}else{
            				return "不展现";
            			}
            		}	
            	},
            	{field:'productId',    title:'商品ID', width:15, align:'center'},
                {field:'isOffShelves',title:'商品状态', width:15, align:'center',
            		formatter:function(value,row,index){
            			if(row.isOffShelves == 1){
            				return '下架';
            			}else{
            				return '出售中';
            			}
            		}	
                },
                {field:'productName',    title:'长名称', width:50, align:'center',
                	formatter:function(value,row,index){
                		if(row.productType==1){
                			return "<a target='_blank' href='http://m.gegejia.com/item-"+row.productId+"'>"+row.productName+"</a>";
                		}else if(row.productType==2){
                			return "<a target='_blank' href='http://m.gegejia.com/mitem-"+row.productId+"'>"+row.productName+"</a>";
                		}else{
                			return row.productName;
                		}
                	}	
                },
                {field:'productShortName',    title:'短名称', width:40, align:'center'},
                {field:'remark',    title:'商品备注', width:30, align:'center'},
                {field:'sell',    title:'销量', width:10, align:'center'},
                {field:'stock',    title:'库存', width:10, align:'center'},
                {field:'marketPrice',    title:'原价', width:10, align:'center'},
                {field:'salesPrice',    title:'现价', width:10, align:'center'},
                {field:'point',    title:'积分价', width:15, align:'center',
                	formatter:function(value,row,index){
                		return row.point+"积分";
                	}	
                },
                {field:'limitNum',    title:'限购数量', width:15, align:'center'},
                {field:'isSupportCashBuy',    title:'现价购买', width:15, align:'center',
                	formatter:function(value,row,index){
                		if(row.isSupportCashBuy == 1){
                			return '支持';
                		}else{
                			return '不支持';
                		}
                	}	
                },
                {field:'sellerName',    title:'商家',width:40, align:'center'},
                {field:'sendAddress',    title:'发货地',width:20, align:'center'},
                {field:'sequence',     title:'排序值',  width:15,   align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editrow(' + index + ')">改排序</a>';
	                   		var b = ' | <a href="javaScript:;" onclick="editProduct(' + index + ')">改商品</a>';
							var c = ' | <a href="javaScript:;" onclick="editPrice(' + index + ')">改价格</a>';
                    		var d = ' | <a href="javascript:;" onclick="editIt(' + index + ')">编辑</a><br/>';
	                       	var e = '<a href="javaScript:;" onclick="editProductName(' + index + ')">改名称</a>';
	                       	var f = ' | <a href="javaScript:;" onclick="editStock(' + index + ')">改库存</a>';
	                       	var g = ' | <a href="javaScript:;" onclick="editSell(' + index + ')">改销量</a>';
	                   		var h = ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
	                       	return a + b + c + d + e + f + g + h;
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
                id:'_add',
                text:'增加商品',
                iconCls:'icon-add',
                handler:function(){
                	clearEditMemberProductForm();
                	$('#editMemberProductDiv').dialog('open');
                }
            },'-',{
            	 id:'_delete',
                 iconCls: 'icon-remove',
                 text:'批量删除',
                 handler: function(){
                 var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                        	var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].id)
                            }
                            deleteIt(ids.join(","));
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
                            var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].productId)
                            }
                            forSale(ids.join(","),0);
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
                            var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].productId)
                            }
                            forSale(ids.join(","),1);
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                },'-',{
                    iconCls: 'icon-edit',
                    text:'批量展现',
                    handler: function(){
                    	var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].id)
                            }
                            displayIt(ids.join(","),1);
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                },'-',{
                    iconCls: 'icon-edit',
                    text:'批量不展现',
                    handler: function(){
                    	var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].id)
                            }
                            displayIt(ids.join(","),0);
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                }],
            pagination:true
        });
		
	    $('#editMemberProductDiv').dialog({
	    	title:'编辑商品',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editMemberProductForm').form('submit',{
	    				url: "${rc.contextPath}/member/saveOrUpdateMemberProduct",
	    				onSubmit:function(){
	    					var productId = $("#editMemberProductForm_productId").val();
	    					var point = $("#editMemberProductForm_point").val();
	    					var limitNum = $("#editMemberProductForm_limitNum").val();
	    					var isSupportCashBuy = $("input[name='isSupportCashBuy']:checked").val();
	    					var sequence = $("#editMemberProductForm_sequence").val();
	    					if($.trim(productId) == ''){
	    						$.messager.alert('提示',"请输入商品ID",'error');
	    						return false;
	    					}else if($.trim(point) == ''){
	    						$.messager.alert('提示',"请输入积分价",'error');
	    						return false;
	    					}else if($.trim(limitNum) == ''){
	    						$.messager.alert('提示',"请输入限购数量",'error');
	    						return false;
	    					}else if($.trim(isSupportCashBuy) == '' || isSupportCashBuy == undefined || isSupportCashBuy == null){
	    						$.messager.alert('提示',"请选择是否支持现价购买",'error');
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
	                                $('#editMemberProductDiv').dialog('close');
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
	                $('#editMemberProductDiv').dialog('close');
	            }
	    	}]
	    });
	    
       	$("#editMemberProductForm_productId").change(function(){
			var id = $.trim($(this).val());
			if(id == ""){
				$("#editMemberProductForm_name").text('');
			}else{
				$("#editMemberProductForm_name").text('');
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#editMemberProductForm_name').text(data.msg);
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