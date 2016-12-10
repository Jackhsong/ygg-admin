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
		<div data-options="region:'north',title:'今日热卖商品管理',split:true" style="height: 120px;">
			<div style="padding: 15px">
				<table>
					<tr>
						<td class="searchName">展现状态：</td>
						<td class="searchText">
							<select id="searchIsDisplay" name="searchIsDisplay" style="width: 173px;">
								<option value="-1">全部</option>
								<option value="1">展现</option>
								<option value="0">不展现</option>
								<option value="2">手动不展现</option>
								<option value="3">自动不展现</option>
							</select>
							&nbsp;&nbsp;&nbsp;
						</td>
						<td class="searchName">商品类型：</td>
						<td class="searchText">
							<select id="searchType" name="searchType" style="width: 173px;">
								<option value="-1">全部</option>
								<option value="1">特卖商品</option>
								<option value="2">商城商品</option>
							</select>
							&nbsp;&nbsp;&nbsp;
						</td>
						<td class="searchName">是否过期：</td>
						<td class="searchText">
							<select id="searchIsEnd" name="searchIsEnd">
								<option value="-1">全部</option>
								<option value="1">是</option>
								<option value="2">否</option>
							</select>
							&nbsp;&nbsp;&nbsp;
						</td>
						<td><a id="searchBtn" onclick="searchHotProduct()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
					</tr>
					<tr>
						<td class="searchName">商品Id：</td>
						<td class="searchText"><input type="text" id="searchProductId" name="searchProductId"/>&nbsp;&nbsp;&nbsp;</td>
						<td class="searchName">商品名称：</td>
						<td class="searchText"><input type="text" id="searchProductName" name="searchProductName"/>&nbsp;&nbsp;&nbsp;</td>
						<td class="searchName">有无库存：</td>
						<td class="searchText">
							<select id="searchStock" name="searchStock">
								<option value="-1">全部</option>
								<option value="1">有</option>
								<option value="2">无</option>
							</select>
							&nbsp;&nbsp;&nbsp;
						</td>
						<td><a id="clearSearch" onclick="clearSerach()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >重置</a></td>
					</tr>
				</table>
<!-- 			<font color="red" size="3"></font>
           	<input name="searchIsDisplay" id="searchIsDisplay1" type="radio" value="1" />展现&nbsp;
           	<input name="searchIsDisplay" id="searchIsDisplay2" type="radio" value="0" />不展现&nbsp;
           	<input name="searchIsDisplay" id="searchIsDisplay3" type="radio" value="2" />手动不展现&nbsp;
           	<input name="searchIsDisplay" id="searchIsDisplay4" type="radio" value="3" />自动不展现&nbsp;
           	<input name="searchIsDisplay" id="searchIsDisplay5" type="radio" value="-1" checked="checked"/>全部
			&nbsp;&nbsp;&nbsp;&nbsp;<font color="red" size="3">商品类型：</font>
       		<input name="searchType" id="searchType1" type="radio" value="1" />特卖商品&nbsp;
           	<input name="searchType" id="searchType2" type="radio" value="2" />商城商品&nbsp;
           	<input name="searchType" id="searchType3" type="radio" value="-1" checked="checked"/>全部
			&nbsp;&nbsp;&nbsp;&nbsp;<font color="red" size="3">是否过期：</font>
       		<input name="searchIsEnd" id="searchIsEnd1" type="radio" value="1" />是&nbsp;
           	<input name="searchIsEnd" id="searchIsEnd2" type="radio" value="2" />否&nbsp;
           	<input name="searchIsEnd" id="searchIsEnd3" type="radio" value="-1" checked="checked"/>全部
			&nbsp;&nbsp;&nbsp;&nbsp;<font color="red" size="3">有无库存：</font>
       		<input name="searchStock" id="searchStock1" type="radio" value="1" />有&nbsp;
           	<input name="searchStock" id="searchStock2" type="radio" value="2" />无&nbsp;
           	<input name="searchStock" id="searchStock3" type="radio" value="-1" checked="checked"/>全部 -->
			</div>
		</div> 
		
		<div data-options="region:'center'" >
			<!--数据表格-->
			<table id="s_data" ></table>
			
			<!-- 增加商品 begin -->
			<div id="add_div" style="width:1000px;height:750px;padding:20px 20px;">
				<div id="searchDiv" class="datagrid-toolbar" style="height: 50px;padding: 15px">
		        	<form id="searchForm" method="post" >
		            	<table>
			                <tr>
			                	<td>商品ID</td>
			                	<td><input id="searchId" name="id" value=""/></td>
			                    <td>&nbsp;&nbsp;商品编码</td>
			                    <td><input id="searchCode" name="code" value="" /></td>
			                    <td>&nbsp;&nbsp;名称</td>
			                    <td><input id="searchName" name="name" value="" /></td>
								<td>&nbsp;&nbsp;商品类型</td>
								<td>
									<select id="searchType" name="searchType" style="width: 100px;">
										<option value="-1">全部</option>
										<option value="1">特卖商品</option>
										<option value="2">商城商品</option>
									</select>
								</td>
			                </tr>
			                <tr>
			                	<td>商家</td>
								<td><input id="sellerId" type="text" name="sellerId" ></input></td>
								<td>&nbsp;&nbsp;品牌</td>
								<td><input id="brandId" type="text" name="brandId" ></input></td>
								<td>&nbsp;&nbsp;备注</td>
								<td><input id="remark" type="text" name="remark" ></input></td>
								<td>
									&nbsp;&nbsp;<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			                	</td>
			                	<td>
			                		&nbsp;&nbsp;<a id="searchBtn" onclick="searchProduct_clear()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除条件</a>
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
			
		    <!-- 修改销量begin -->
		    <div id="editSell_div" style="width:350px;height:330px;padding:20px 20px;">
		    	<div>
		    		<input type="hidden" id="editSellCurrRefreshId" value="" />
		    		商品短名称：<span id="editSellShortName"></span><br/><br/>
		    		现销量：<span id="productSellNum"></span><br/><br/>
		    		<input id="refreshSell" type="button" value="刷新" />
		    	</div>
		    	<br/><br/>
		    	<div>
		    		<form id="editSellMyForm" method="post">
						<input id="editSellEditId" type="hidden" name="id" value="" >
		            	<table cellpadding="5">
		                	<tr>
		                    	<td>增加销量：</td><br/>
		                    	<td><input class="" id="sellNum" type="text" name="sellNum" /></td>
		                	</tr>
		                	<tr>
		                		<td></td>
		                		<td>减少可填负数</td>
		                	</tr>
		            	</table>
		        	</form>
		    	</div>
		    </div>
		    <!-- 修改销量end -->
		    
			<!-- 改价格begin -->
			<div id="updatePrice" class="easyui-dialog" style="width:470px;height:200px;padding:20px 20px;">
			    <form id="updatePrice_form" method="post">
					<input id="updatePrice_form_id" type="hidden" name="id" value="" />
		            <table cellpadding="5">
		                <tr>
		                	<td>市场价:</td>
		                	<td>
		                		<input id="updatePrice_form_marketPrice" name="marketPrice" disabled="disabled"/>
		                	</td>
		                </tr>
		                <tr>
		                    <td>售卖价格:</td>
		                    <td>
		                    	<input id="updatePrice_form_price" name="money" /><font color="red">(注：售价必须小于市场价)</font>
		                    </td>
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
		
	        <!-- 快速增加begin -->
	        <div id="copySaleProductDiv" class="easyui-dialog" style="width:400px;height:250px;padding:20px 20px;">
	            <table cellpadding="5">
	                <tr>
	                    <td>商品ID：<font color="red">(如果要增加多个商品，用英文逗号分开)</font></td>
	                </tr>
	                <tr>
	                	<td>
	                    	<textarea rows="3" cols="40" id="copySaleProductDiv_id" onkeydown="checkEnter(event)"></textarea>
	                    </td>
	                </tr>
	            </table>
	        </div>
	        <!-- 快速增加end -->    
	        		
	        <!-- 一键添加明日即将开抢商品begin -->
	        <div id="addTomorrowSaleProductDiv" class="easyui-dialog" style="width:400px;height:120px;padding:10px 10px;">
	            <form id="addTomorrowSaleProduct_form" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>选择添加日期：</td>
		                    <td><input value="" id="selectedDate" name="selectedDate" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})"/></td>
		                </tr>
		            </table>
	            </form>
	        </div>
	        <!-- 一键添加明日即将开抢商品end -->    		
		</div>
	</div>
</div>

<script>
	function searchHotProduct(){
		$('#s_data').datagrid('load',{
			isDisplay:$("#searchIsDisplay").val(),
			type:$("#searchType").val(),
			isEnd:$("#searchIsEnd").val(),
			stock:$("#searchStock").val(),
			productId:$("#searchProductId").val(),
			productName:$("#searchProductName").val()
		});
	}
	
	function clearSerach(){
		$("#searchIsDisplay").find("option").eq(0).attr("selected","selected");
		$("#searchType").find("option").eq(0).attr("selected","selected");
		$("#searchIsEnd").find("option").eq(0).attr("selected","selected");
		$("#searchStock").find("option").eq(0).attr("selected","selected");
		$("#searchProductId").val('');
		$("#searchProductName").val('');
		$('#s_data').datagrid('load',{});
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
	
	/**调库存 begin*/
	function editStock(index){
		var arr=$("#s_data").datagrid("getData");
		var proudctBaseId = arr.rows[index].baseId;
		$.ajax({
			url: '${rc.contextPath}/productBase/findProductInfoByBaseId',
			type:"POST",
			data: {baseId:proudctBaseId, type:1},
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
	/**调库存 end*/
	
	<!--改销量 -->
	function editSell(index){
		var arr=$("#s_data").datagrid("getData");
		$('#editSellCurrRefreshId').val(arr.rows[index].productId);
		$('#sellNum').val("");
		$('#refreshSell').click();
		$('#editSell_div').dialog('open');
	}

	<!--改价格 -->
	function editPrice(index){
	    var arr=$("#s_data").datagrid("getData");
	    $("#updatePrice_form_id").val(arr.rows[index].productId);
	    var tt=arr.rows[index].salesPrice;
	    var marketPrice = arr.rows[index].marketPrice;
		$("#updatePrice_form_price").val(tt);
		$("#updatePrice_form_marketPrice").val(marketPrice);
	    $("#updatePrice").dialog('open');
	}

	<!--改名称 -->
	function editProductName(index){
	    var arr=$("#s_data").datagrid("getData");
	    $("#updateProductName_form_id").val(arr.rows[index].productId);
	    var t1=arr.rows[index].productName;
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
	   		type:$("#searchType").val()
	   	});
	}
	
	function searchProduct_clear(){
		$("#searchName").val("");
		$("#searchCode").val("");
		$('#sellerId').combobox('clear');
		$('#brandId').combobox('clear');
		$("#searchId").val('');
		$("#remark").val();
		$("#searchType").find('option').eq(0).attr('selected','selected');
	}

	<!--删除商品-->
	function deleteIt(id){
	    $.messager.confirm("提示信息","确定删除吗？",function(ra){
	        if(ra){
	        	$.messager.confirm("提示信息","删除后用户将看不到该商品信息，再想想吧？",function(re){
	    	        if(re){
	    	            $.messager.progress();
	    	            $.post("${rc.contextPath}/mallWindow/deleteHotProduct",{id:id},function(data){
	    	                $.messager.progress('close');
	    	                if(data.status == 1){
	    	                    $.messager.alert('响应信息',"删除成功...",'info',function(){
	    	                        $('#s_data').datagrid('load');
	    	                        return
	    	                    });
	    	                } else{
	    	                    $.messager.alert('响应信息',data.msg,'error',function(){
	    	                        return
	    	                    });
	    	                }
	    	            });
	    	        }
	    	    });
	        }
	    });
	}

	function editProduct(index){
    	var arr=$("#s_data").datagrid("getData");
    	var type = arr.rows[index].typeCode;
    	var productId = arr.rows[index].productId;
    	var urlStr = "${rc.contextPath}/product/edit/"+type+"/"+productId;
    	window.open(urlStr,"_blank");
	}

	function displayId(id,productId,type,isDisplay){
		var tip = "";
		if(isDisplay == 0){
			tip = "确定不展现吗？";
		}else{
			tip = "确定展现吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
   	            $.messager.progress();
   	            $.post(
   	            		"${rc.contextPath}/mallWindow/updateHotProductDisplayStatus",
   	            		{id:id,productId:productId,isDisplay:isDisplay,type:type},
   	            		function(data){
		   	                $.messager.progress('close');
		   	                if(data.status == 1){
		   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
		   	                        $('#s_data').datagrid('load');
		   	                        return
		   	                    });
		   	                } else{
		   	                    $.messager.alert('响应信息',data.msg,'error');
		   	                }
   	            		});
	        }
	    });
	}
	
	
	/**	调整商品库存相关  begin*/
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
					refreshStock();
					refreshAllottedStock();
					$('#s_saleData').datagrid('load',{baseId:row.baseId});
					$('#s_data').datagrid('load');
		            return
		        } else{
		            $.messager.alert('响应信息',data.message,'error',function(){
		                return
		            });
		        }
			}
		});
	};
	/**	调整商品库存相关  end*/
	
	
	$(function(){

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
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saveProductStock('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelProductStock('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editProductStock(' + index + ')">调库存</a> | ';
                    		var b = '<a href="${rc.contextPath}/product/edit/' + row.id + '" targe="_blank">查看</a>'
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
                                	$("#updatePrice_form_id").val("");
                                	$("#updatePrice_form_price").val("");
                                	$("#updatePrice_form_marketPrice").val("");
                                    $('#s_data').datagrid('load');
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
                                    $('#s_data').datagrid('load');
                                    $('#updateProductName').dialog('close');
                                });
                            } else{
                                $.messager.alert('响应信息',res.msg,'error');
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
	
		
		$('#copySaleProductDiv').dialog({
            title:'快速增加商品',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                	var productId = $("#copySaleProductDiv_id").val();
                	if($.trim(productId) == ''){
                		$.messager.alert("提示",'请输入商品ID',"warning");
                	}else{
               			$.messager.progress();
           				$.ajax({
           		            url: '${rc.contextPath}/mallWindow/quickAddProduct',
           		            type: 'post',
           		            dataType: 'json',
           		            data: {'ids':productId},
           		            success: function(data){
           		            	$.messager.progress('close');
           		                if(data.status == 1){
           		                	$('#copySaleProductDiv').dialog('close');
           		                	$('#s_data').datagrid('load');
           		                	$.messager.alert("提示",data.msg,"info");
           		                }else{
           		                	$('#s_data').datagrid('load');
           		                	$.messager.alert("提示",data.msg,"info");
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
                    $('#copySaleProductDiv').dialog('close');
                }
            }]
        });
		
		
		$('#addTomorrowSaleProductDiv').dialog({
            title:'一键添加所有明日即将开抢商品',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'添加',
                iconCls:'icon-ok',
                handler:function(){
               	    $.messager.confirm("提示信息","确认一键添加所选日期早10点和晚20点开抢的所有商品吗？有部分不适合在今日最热TOP商品中展示的商品，请手工删除。",function(ra){
               	    	if(ra){
                            $('#addTomorrowSaleProduct_form').form('submit',{
                                url:"${rc.contextPath}/mallWindow/addSaleWindowToHotProduct",
                                onSumbit:function(){
                                	var selectedDate = $('#selectedDate').val();
                                	if($.trim(selectedDate) == ''){
                                		$.messager.alert('提示','选择添加日期','error');
                                		return false;
                                	}
                                	$.messager.progress();
                                },
                                success:function(data){
                                	$.messager.progress('close');
                                    var res = eval("("+data+")")
                                    if(res.status == 1){
        	    	                    $.messager.alert('响应信息',res.msg,'info',function(){
        	    	                        $('#s_data').datagrid('reload');
        	    	                        $("#addTomorrowSaleProductDiv").dialog('close');
        	    	                        return;
        	    	                    });
        	    	                }else{
        	    	                    $.messager.alert('响应信息',res.msg,'error',function(){
        	    	                        return
        	    	                    });
        	    	                }
                                }
                            });
               	    	}
               	    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                	$("#addTomorrowSaleProductDiv").dialog('close');
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
		
		<!--加载楼层商品列表 begin-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/mallWindow/jsonTodayHotProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'-', width:15, align:'center',checkbox:true},
            	{field:'isDisplay',    title:'展现状态', width:15, align:'center'},
            	{field:'productId',    title:'商品ID', width:15, align:'center'},
            	{field:'type',    title:'商品类型', width:15, align:'center'},
            	{field:'productTime',    title:'特卖起止时间', width:30, align:'center'},
            	{field:'isEnd',    title:'是否过期', width:15, align:'center'},
            	{field:'isZeroStock',    title:'有无库存', width:15, align:'center'},
                {field:'nameUrl',    title:'长名称', width:70, align:'center'},
                {field:'shortName',    title:'短名称', width:50, align:'center'},
                {field:'salesPrice',    title:'售价', width:15, align:'center'},
                {field:'saleCount',    title:'销量', width:15, align:'center'},
                {field:'stock',    title:'剩余库存', width:15, align:'center'},
                {field:'lockAmount',    title:'锁定库存', width:15, align:'center'},
                {field:'hidden1',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                   		var b = '<a href="javaScript:;" onclick="editProduct(' + index + ')">编辑商品</a>';
                       	var c = ' | <a href="javaScript:;" onclick="editStock(' + index + ')">改库存</a>';
                       	var d = ' | <a href="javaScript:;" onclick="editProductName(' + index + ')">改名称</a>';
                       	var e = '';
                       	if(row.isDisplayCode == 1){
                       		e = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + row.productId + ',' + row.typeCode + ',' + 0 + ')">不展现</a>'
                       	}else{
                       		e = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + row.productId + ',' + row.typeCode + ',' + 1 + ')">展现</a>'
                       	}
                       	return b+c+d+e;
                    }
                },
                {field:'hidden2',  title:'操作', width:15,align:'center',
                	formatter:function(value,row,index){
                		return '<a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                	}
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'增加商品',
                iconCls:'icon-add',
                handler:function(){
                	$('#product_data').datagrid('load',{
                		status:0
                	});
                	$('#add_div').dialog('open');
                }
            },'-',{
                iconCls: 'icon-edit',
                text:'快速添加商品',
                handler: function(){
                	$("#copySaleProductDiv_id").val('');
					$("#copySaleProductDiv").dialog('open');
                }
            },'-',{
            	text:'一键添加所有明日即将开抢商品',
                iconCls:'icon-add',
                handler:function(){
                	$("#selectedDate").val('');
					$("#addTomorrowSaleProductDiv").dialog('open');
                }
            },'-',{
            	text:'全部展现',
                iconCls:'icon-edit',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('展现','确认全部展现吗？',function(b){
                            if(b){
                            	$.messager.progress();
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id+":"+rows[i].productId+":"+rows[i].typeCode)
                                }
                                $.post("${rc.contextPath}/mallWindow/batchUpdateHotProductDisplayStatus",
									{ids: ids.join(","),code:1},
									function(data){
										$.messager.progress('close');
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$.messager.alert('提示',data.msg,"");
											$('#s_data').datagrid('clearSelections');
										}else{
											$.messager.alert('提示','保存出错',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的商品',"error")
                    }
                }
            },'-',{
            	text:'全部不展现',
                iconCls:'icon-remove',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('不展现','确认全部不展现吗？',function(b){
                            if(b){
                            	$.messager.progress();
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id+":"+rows[i].productId+":"+rows[i].typeCode)
                                }
                                $.post("${rc.contextPath}/mallWindow/batchUpdateHotProductDisplayStatus",
									{ids: ids.join(","),code:0},
									function(data){
										$.messager.progress('close');
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$('#s_data').datagrid('clearSelections');
										}else{
											$.messager.alert('提示','保存出错',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的商品',"error")
                    }
                }
            },'-',{
            	text:'批量删除',
                iconCls:'icon-remove',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                	    $.messager.confirm("提示信息","确定删除吗？",function(ra){
                	        if(ra){
                	        	$.messager.confirm("提示信息","删除后用户将看不到该商品信息，再想想吧？",function(re){
                	    	        if(re){
                	    	            $.messager.progress();
                	    	            var ids = [];
                                        for(var i=0;i<rows.length;i++){
                                            ids.push(rows[i].id)
                                        }
                	    	            $.post("${rc.contextPath}/mallWindow/deleteHotProduct",{id: ids.join(",")},function(data){
                	    	                $.messager.progress('close');
                	    	                if(data.status == 1){
                	    	                    $.messager.alert('响应信息',"删除成功...",'info',function(){
                	    	                        $('#s_data').datagrid('load');
                	    	                        return
                	    	                    });
                	    	                } else{
                	    	                    $.messager.alert('响应信息',data.msg,'error',function(){
                	    	                        return
                	    	                    });
                	    	                }
                	    	            });
                	    	        }
                	    	    });
                	        }
                	    });
                    }else{
                        $.messager.alert('提示','请选择要操作的商品',"error")
                    }
                }
            }],
            pagination:true,
            rownumbers:true
        });
		<!--加载楼层商品列表 end-->
        
        <!--添加商品加载列表 begin-->
        $('#product_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'productId',
            url:'${rc.contextPath}/mallWindow/jsonProductForHotProductList',
            queryParams: {
				status:0
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            columns:[[
            	{field:'productId',    title:'序号', width:50, align:'center',checkbox:true},
            	{field:'showId',    title:'商品ID', width:30, align:'center'},
            	{field:'typeStr',    title:'商品类型', width:50, align:'center'},
                {field:'code',    title:'商品编码', width:60, align:'center'},
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
                        $.messager.confirm('添加商品','确认添加吗',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].productId+":"+rows[i].type)
                                }
                                $.post("${rc.contextPath}/mallWindow/addProductForHotProduct", //添加选中商品
									{ids: ids.join(",")},
									function(data){
										if(data.status == 1){
											$.messager.alert('提示','添加成功',"info");
											$('#product_data').datagrid('load',{
												status:0
											});
											$('#add_div').dialog('close');
											$('#s_data').datagrid('load');
										}else{
											$.messager.alert('提示',data.msg,"error")
										}
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
        <!--添加商品加载列表 end-->
    
       <!--添加商品弹出框-->
	   $('#add_div').dialog({
	    	title:'商品信息',
	    	collapsible:true,
    		minimizable:true,
    		maximizable:true,
    		closed:true,
    		modal:true,
    		resizable:true,
	    	buttons:[{
	    		text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#add_div').dialog('close');
                }
	    	}]
		});
});
	
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
</script>

</body>
</html>