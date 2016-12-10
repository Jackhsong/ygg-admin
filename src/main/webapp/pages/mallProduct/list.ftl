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

textarea{ 
	resize:none;
}

</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:220px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'商城商品管理----${listType}',split:true" style="height: 220px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/product/exportResult" method="post" >
		        	<input type="hidden" name="isAvailable" value="${isAvailable}" />
		        	<input type="hidden" name="productType" value="${productType}" />
		        	<input type="hidden" name="type" value="2" />
		            <table class="search">
		                <tr>
		                	<td class="searchName">商品ID：</td>
							<td class="searchText"><input id="searchId" name="searchId" value="" /></td>
		                    <td class="searchName">商品编码：</td>
		                    <td class="searchText"><input id="searchCode" name="code" value="" /></td>
		                    <td class="searchName">商品名称：</td>
		                    <td class="searchText"><input id="searchTitle" name="title" value="" /></td>
                            <td class="searchName">基本商品Id：</td>
                            <td class="searchText"><input id="searchBaseId" name="searchBaseId" value="" /></td>
		                </tr>
		                <tr>
		                    <td class="searchName">商家：</td>
							<td class="searchText"><input id="sellerId" type="text" name="sellerId" /></td>
							<td class="searchName">发货类型：</td>
							<td class="searchText"><input id="sendType" name="sendType" value=""/></td>
                            <td class="searchName">品牌：</td>
                            <td class="searchText"><input id="brandId" type="text" name="brandId" ></input></td>
                            <td class="searchName">商品状态：</td>
                            <td class="searchText">
                                <select name="isOffShelves" id="isOffShelves" style="width: 170px;">
                                    <option value="-1">全部</option>
                                    <option value="1">已下架</option>
                                    <option value="0">出售中</option>
                                </select>
                            </td>
		                </tr>
		                <tr>
		                    <td class="searchName">格格说：</td>
							<td class="searchText"><input id="searchDesc" name="desc" value="" /></td>
                            <td class="searchName">一级分类：</td>
                            <td class="searchText"><input id="searchCategoryFirstId" type="text" name="searchCategoryFirstId" /></td>
                            <td class="searchName">二级分类：</td>
                            <td class="searchText"><input id="searchCategorySecondId" type="text" name="searchCategorySecondId" /></td>
                            <td class="searchName">价格区间：</td>
                            <td class="searchText">
                                <input id="lowerPrice" name="lowerPrice" value="" />
                                <input id="higherPrice" name="higherPrice" value="" />
                            </td>

		                </tr>
		                <tr>
                            <td class="searchName">使用渠道：</td>
                            <td class="searchText"><input id="productUseScope" name="productUseScopeId" value="" /></td>
                            <td class="searchName">是否放入商城：</td>
                            <td class="searchText">
                                <select id="searchIsShowInMall" type="text" name="searchIsShowInMall" style="width: 173px;">
                                    <option value="-1">全部</option>
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </td>
                            <td class="searchName">三级分类：</td>
                            <td class="searchText"><input id="searchCategoryThirdId" type="text" name="searchCategoryThirdId" /></td>
                            <td class="searchName"></td>
                            <td class="searchText">
                                <a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                                &nbsp;
                                <a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
                                &nbsp;
                                <a id="exportBtn" onclick="exportProduct()" href="javascript:;" class="easyui-linkbutton" >导出查询结果</a>
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
    		
    		<!-- 调价格 begin-->
			<div id="updatePrice" class="easyui-dialog" style="width:470px;height:200px;padding:20px 20px;">
	            <form id="updatePrice_form" method="post">
				<input id="updatePrice_form_id" type="hidden" name="id" value="" >
	            <table cellpadding="5">
	                <tr>
	                	<td>市场价:</td>
	                	<td>
	                		<input id="updatePrice_form_marketPrice" name="marketPrice"/>
	                	</td>
	                </tr>
	                <tr>
	                    <td>售卖价格:</td>
	                    <td>
	                    	<input id="updatePrice_form_price" name="salesPrice" /><font color="red">(注：售价必须小于市场价)</font>
	                    </td>
	                </tr>
	            </table>
	        	</form>
	        </div>
	        <!-- 调价格 end-->
        	
        	<!-- 该名称begin -->
	        <div id="updateProductName" class="easyui-dialog" style="width:500px;height:190px;padding:20px 20px;">
	            <form id="updateProductName_form" method="post">
				<input id="updateProductName_form_id" type="hidden" name="id" value="" >
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
	        <!-- 该名称end -->
        	
        	<!-- 添加温馨提示begin -->
	        <div id="addTip" class="easyui-dialog" style="width:650px;height:300px;padding:20px 20px;">
	            <form id="af" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>备注(<span style="color:red">默认加到已有温馨提示的后面。</span>):</td>
		                    <td>
		                    	<select>
		                    		<option value="2">节假日不发货</option>
		                    	</select>
		                    </td>
		                </tr>
		            </table>
	        	</form>
	        </div>
	        <!-- 添加温馨提示end -->
	        
	        <!-- 从特卖商品复制begin -->
	        <div id="copySaleProductDiv" class="easyui-dialog" style="width:400px;height:250px;padding:20px 20px;">
	            <table cellpadding="5">
	                <tr>
	                    <td>特卖商品ID：<font color="red">(如果要复制多个特卖商品，用英文逗号分开,<br/>每次最多50个)</font></td>
	                </tr>
	                <tr>
	                	<td>
	                    	<textarea rows="3" cols="40" id="copySaleProductDiv_id" onkeydown="checkEnter(event)"></textarea>
	                    </td>
	                </tr>
	            </table>
	        </div>
	        <!-- 从特卖商品复制end -->

			<!-- 编辑分类排序值begin -->
   	 		<div id="editCategorySequenceDiv" style="width:600px;height:400px;padding:20px 20px;">
   	 			<p>
   	 				<input id="categoryBaseId" type="hidden" value=""/>
   	 				<input id="categoryProductId" type="hidden" value=""/>
   	 				<span id="baseName"></span>
   	 			</p>
   	 			<table id="s_categroyData" ></table>
   	 		</div>
   	 		<!-- 编辑分类排序值end -->
   	 		 
   	 		<!-- 批量分类begin -->
   	 		<div id="editProductCategoryDiv" style="width:600px;height:400px;padding:20px 20px;">
   	 			<input type="hidden" id="baseIds" value=""/>
				<table id="categoryTab">
					<tr>
						<td>
							<select name="categoryFirstId" class="selectStyle">
								<option value="0">-请选择一级分类-</option>
								<#list catetgoryFirstList as first>
 		 			  			<option value="${first.id?c}">${first.name}</option>
 		 			  			</#list>
							</select>
						</td>
						<td>
							<select name="categorySecondId" class="selectStyle">
	 							<option value="0">-请选择二级分类-</option>
	 			  				<#list categorySecondList as second>
	 			  				<option value="${second.id?c}">${second.name}</option>
	 			  				</#list>	
 		 					</select>
						</td>
						<td>
							<select name="categoryThirdId" class="selectStyle">
	 							<option value="0">-请选择三级分类-</option>
	 			  				<#list categoryThirdList as third>
	 			  				<option value="${third.id?c}">${third.name}</option>
	 			  				</#list>	
 		 					</select>
						</td>
						<td>
							<span onclick="addCategoryRow(this)" style="cursor: pointer;color:red">&nbsp;添加&nbsp;|</span>
                  			<span onclick="delCategoryRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
						</td>
					</tr>
				</table>
   	 		</div>
   	 		<!-- 批量分类end -->
   	 		
   	 		
   	 		<!-- 批量调库存 begin-->
			<div id="batchAdjustStock" class="easyui-dialog" style="width:400px;height:150px;padding:20px 20px;">
	            <form id="batchAdjustStock_form" method="post">
				<input id="batchAdjustStock_form_id" type="hidden" name="ids" value="" >
				<input id="batchAdjustStock_form_type" type="hidden" name="type" value="2" >
	            <table cellpadding="5">
	                <tr>
	                	<td>调整:</td>
	                	<td>
	                		<input id="batchAdjustStock_form_stock" name="stock" value=""/>
	                		<font color="red">减少可填负数</font>
	                	</td>
	                </tr>
	            </table>
	        	</form>
	        </div>
	        <!-- 批量调库存 end-->
	        
	        <!-- 改备注begin -->
	        <div id="editProductRemarkDiv" class="easyui-dialog" style="width:500px;height:150px;padding:20px 20px;">
	               <input type="hidden" id="editProductRemark_productId" value=""/>
	                         备注：<input type="text" id="editProductRemark_remark" maxlength="80" style="width: 400px;"/>
	        </div>
	        <!-- 改备注end -->
	        	        
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

function addCategoryRow(obj){
	var row = $(obj).parent().parent().clone(true);
	var length = $(obj).parent().parent().parent().find("tr").size();
	if(length > 4){
		$.messager.alert("提示","最多允许添加5个分类","error");
		return false;
	}
	$(row).find("td").eq(0).find("option").eq(0).attr("selected","selected");
	$(row).find("td").eq(1).find("option").eq(0).attr("selected","selected");
	$(row).find("td").eq(2).find("option").eq(0).attr("selected","selected");
	$(obj).parent().parent().parent().find("tr").last().after(row);
}

function delCategoryRow(obj){
	var length = $(obj).parent().parent().parent().find("tr").size();
	if(length <= 1){
		$.messager.alert("提示","必须有1个分类","error");
		return false;
	}
	$(obj).parent().parent().remove();
}

function exportProduct(){
	$('#searchForm').submit();
}

<!--调库存相关begin-->
function editrow(index){
	$('#s_saleData').datagrid('beginEdit', index);
}

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
}

function cancelrow(index){
	$('#s_saleData').datagrid('cancelEdit', index);
}

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
	            return
	        } else{
	            $.messager.alert('响应信息',data.message,'error',function(){
	                return
	            });
	        }
		}
	});
};
<!--调库存相关end -->

<!--修改排序值begin-->
function editSequenceRow(index){
	$('#s_categroyData').datagrid('beginEdit', index);
}

function updateSequenceActions(){
	var rowcount = $('#s_categroyData').datagrid('getRows').length;
	for(var i=0; i<rowcount; i++){
		$('#s_categroyData').datagrid('updateRow',{
	    	index:i,
	    	row:{}
		});
	}
}

function saveSequenceRow(index){
	$('#s_categroyData').datagrid('endEdit', index);
}

function cancelSequenceRow(index){
	$('#s_categroyData').datagrid('cancelEdit', index);
}

function updateSequenceActivity(row){
	$.ajax({
		url: '${rc.contextPath}/category/updateProductCategoryThirdSequence',
		type:"POST",
		data: {id:row.id,sequence:row.sequence},
		success: function(data) {
			if(data.status == 1){
				$('#s_categroyData').datagrid('reload',{
					baseId:$("#categoryBaseId").val(),
					productId:$("#categoryProductId").val()
				});
				$('#s_data').datagrid('reload');
	            return
	        } else{
	            $.messager.alert('响应信息',data.msg,'error',function(){
	                return
	            });
	        }
		}
	});
}
<!--修改排序值end-->

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
	
	function copyProduct(index){
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var type = arr.rows[index].type;
		$.messager.confirm('复制商品','确定复制吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/product/copyProduct',
		            type: 'post',
		            dataType: 'json',
		            data: {'id':id,'productType':type},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
		                	$('#s_data').datagrid('reload',{
                                code:$("#searchCode").val(),
                                name:$("#searchTitle").val(),
                                desc:$("#searchDesc").val(),
                                isAvailable:$("#isAvailable").val(),
                                isOffShelves:$("#isOffShelves").val(),
                                startTimeBegin:$("#startTimeBegin").val(),
                                startTimeEnd:$("#startTimeEnd").val(),
                                endTimeBegin:$("#endTimeBegin").val(),
                                endTimeEnd:$("#endTimeEnd").val(),
                                brandId:$("input[name='brandId']").val(),
                                sellerId:$("input[name='sellerId']").val(),
                                searchId:$("input[name='searchId']").val(),
                                sendType:$("#sendType").combobox('getValue'),
                                baseId:$("#searchBaseId").val(),
                                isAvailable:${isAvailable},
                                productType:${productType},
                                categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                isShowInMall:$("#searchIsShowInMall").val(),
                                lowerPrice:$('#lowerPrice').val(),
                                higherPrice:$('#higherPrice').val()
		                    });
		                	$.messager.alert("提示",data.msg,"info");
		                }else{
		                	$.messager.alert("提示",data.msg,"info");
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

	function searchProduct(){
    	$('#s_data').datagrid('load',{
        	code:$("#searchCode").val(),
        	name:$("#searchTitle").val(),
        	desc:$("#searchDesc").val(),
        	isAvailable:$("#isAvailable").val(),
        	isOffShelves:$("#isOffShelves").val(),
        	startTimeBegin:$("#startTimeBegin").val(),
        	startTimeEnd:$("#startTimeEnd").val(),
        	endTimeBegin:$("#endTimeBegin").val(),
        	endTimeEnd:$("#endTimeEnd").val(),
        	brandId:$("input[name='brandId']").val(),
        	sellerId:$("input[name='sellerId']").val(),
        	searchId:$("input[name='searchId']").val(),
        	sendType:$("#sendType").combobox('getValue'),
        	baseId:$("#searchBaseId").val(),
           	isAvailable:${isAvailable},
           	productType:${productType},
            productUseScopeId: $("#productUseScope").combobox('getValue'),
           	categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
           	categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
           	categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
           	isShowInMall:$("#searchIsShowInMall").val(),
           	lowerPrice:$('#lowerPrice').val(),
        	higherPrice:$('#higherPrice').val()
    	});
	}
	
	function clearSearch(){
		$("#searchCode").val('');
    	$("#searchTitle").val('');
    	$("#searchDesc").val('');
    	$("#isAvailable").val('');
    	$("#isOffShelves").val('');
    	$("#startTimeBegin").val('');
    	$("#startTimeEnd").val('');
    	$("#endTimeBegin").val('');
    	$("#endTimeEnd").val('');
    	$("#brandId").combobox('clear');
    	$("#sellerId").combobox('clear');
    	$("input[name='searchId']").val('');
    	$("#sendType").combobox('clear');
    	$("#searchBaseId").val('');
        $("#productUseScope").combobox('clear');
    	$("#searchCategoryFirstId").combobox('clear');
       	$("#searchCategorySecondId").combobox('clear');
       	$("#searchCategoryThirdId").combobox('clear');
       	$("#searchIsShowInMall").find("option").eq(0).attr("selected","selected");
       	$('#lowerPrice').val();
    	$('#higherPrice').val();
    	$('#s_data').datagrid('load',{
           	isAvailable:${isAvailable},
           	productType:${productType}
        });
	}

	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr="${rc.contextPath}/product/edit/2/"+arr.rows[index].id;
		window.open(urlStr,"_blank");
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
	
		
	<!--修改排序值-->
	function editSequence(index){
		var arr=$("#s_data").datagrid("getData");
		var baseId = arr.rows[index].baseId;
		var productId = arr.rows[index].id;
		var productName = arr.rows[index].name;
		$.post("${rc.contextPath}/product/checkIsInMall", //检查是否放入了商城
				{'productId': productId},
				function(data){
					if(data.status == 1){
						$("#categoryBaseId").val('');
						$("#categoryProductId").val('');
						$("#baseName").text('');
						$("#categoryBaseId").val(baseId);
						$("#categoryProductId").val(productId);
						$("#baseName").text(productName);
				        $("#s_categroyData").datagrid('clearSelections');
						$('#s_categroyData').datagrid('reload',{productId:productId,baseId:baseId});
				        $("#s_categroyData").datagrid('clearSelections');
						$('#editCategorySequenceDiv').dialog('open');
					}else{
						$.messager.alert('提示',data.msg,"error");
					}
				},
			"json");	
		
               
	}	
	
	function editSell(index){
		var arr=$("#s_data").datagrid("getData");
		$('#editSellCurrRefreshId').val(arr.rows[index].id);
		$('#sellNum').val("");
		$('#refreshSell').click();
		$('#editSell_div').dialog('open');
	}
	
function editPrice(index){
    var arr=$("#s_data").datagrid("getData");
    $("#updatePrice_form_id").val(arr.rows[index].id);
    var tt=arr.rows[index].salesPrice;
    var marketPrice = arr.rows[index].marketPrice;
	$("#updatePrice_form_price").val(tt);
	$("#updatePrice_form_marketPrice").val(marketPrice);
    $("#updatePrice").dialog('open');
}

function editProductName(index){
    var arr=$("#s_data").datagrid("getData");
    $("#updateProductName_form_id").val(arr.rows[index].id);
    var t1=arr.rows[index].name;
    var t2=arr.rows[index].shortName;
	$("#updateProductName_name").val(t1);
	$("#updateProductName_shortName").val(t2);
    $("#updateProductName").dialog('open');
}


	$(function(){
        $('#productUseScope').combobox({
            url:'${rc.contextPath}/productUseScope/jsonproductUseScope?includeAll=1',
            valueField:'code',
            textField:'text'
        });
	
		$('#searchCategoryFirstId').combobox({   
		    url:'${rc.contextPath}/category/jsonCategoryFirstCode',   
		    valueField:'code',   
		    textField:'text',
		    editable:false,
		    onSelect:function(rac){
		    	var url = '${rc.contextPath}/category/jsonCategorySecondCode?firstId='+rac.code;
		    	$('#searchCategorySecondId').combobox('reload',url);
		    }
		    
		});
		
		$('#searchCategorySecondId').combobox({
			url:'${rc.contextPath}/category/jsonCategorySecondCode',   
		    valueField:'code',   
		    textField:'text',
		    editable:false,
		    onSelect:function(rac){
		    	var url = '${rc.contextPath}/category/jsonCategoryThirdCode?secondId='+rac.code;
		    	$('#searchCategoryThirdId').combobox('reload',url);
		    }
		});

		$('#searchCategoryThirdId').combobox({
			url:'${rc.contextPath}/category/jsonCategoryThirdCode',   
		    valueField:'code',   
		    textField:'text',
		    editable:false
		});
		
		$('#addTip').dialog({
            title:'批量修改商品温馨提示信息',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                 	$.messager.progress();
                 	var tip = $('#edit_tip').val();
                 	var rows = $('#s_data').datagrid("getSelections");
                    var ids = [];
                    for(var i=0;i<rows.length;i++){
                    	ids.push(rows[i].id)
                    }
                    $.post("${rc.contextPath}/product/batchEditTip", //批量修改商品温馨提示
						{ids: ids.join(","),tip:tip},
						function(data){
							if(data.status == 1){
								$.messager.alert('提示',data.msg,"info")
								$.messager.progress('close');
								$('#addTip').dialog('close');
								$('#s_data').datagrid('reload',{
                                    code:$("#searchCode").val(),
                                    name:$("#searchTitle").val(),
                                    desc:$("#searchDesc").val(),
                                    isAvailable:$("#isAvailable").val(),
                                    isOffShelves:$("#isOffShelves").val(),
                                    startTimeBegin:$("#startTimeBegin").val(),
                                    startTimeEnd:$("#startTimeEnd").val(),
                                    endTimeBegin:$("#endTimeBegin").val(),
                                    endTimeEnd:$("#endTimeEnd").val(),
                                    brandId:$("input[name='brandId']").val(),
                                    sellerId:$("input[name='sellerId']").val(),
                                    searchId:$("input[name='searchId']").val(),
                                    sendType:$("#sendType").combobox('getValue'),
                                    baseId:$("#searchBaseId").val(),
                                    isAvailable:${isAvailable},
                                    productType:${productType},
                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                    isShowInMall:$("#searchIsShowInMall").val(),
                                    lowerPrice:$('#lowerPrice').val(),
                                    higherPrice:$('#higherPrice').val()
			                    });
							}else{{
								$.messager.alert('提示',data.msg,"error")
								$.messager.progress('close');
							}}
						},
					"json");
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addTip').dialog('close');
                }
            }]
        });
		
		
		
		$('#copySaleProductDiv').dialog({
            title:'复制商品',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                	var productId = $("#copySaleProductDiv_id").val();
                	if($.trim(productId) == ''){
                		$.messager.alert("提示",'请输入特卖商品ID',"warning");
                	}else{
                        var productId = $("#copySaleProductDiv_id").val();
                    	if($.trim(productId) == ''){
                    		$.messager.alert("提示",'请输入特卖商品ID',"warning");
                    	}else{
                    		batchCopyProduct(productId,1,2);
                    	}
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
	
		$('#sellerId').combobox({
            panelWidth:350,
            panelHeight:350,
            mode:'remote',
		    url:'${rc.contextPath}/seller/jsonSellerCode',   
		    valueField:'code',   
		    textField:'text'  
		});
		
		$('#sendType').combobox({
			url:'${rc.contextPath}/seller/jsonSellerType',   
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
            url:'${rc.contextPath}/product/jsonMallProductInfo',
            loadMsg:'正在装载数据...',
            queryParams:{
             	isAvailable:${isAvailable},
             	productType:${productType}
            },
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'mainImage',    title:'主图',width:30, align:'center'},
                {field:'hidden1',    title:'商品详细信息', width:80, align:'left',
                    formatter:function(value,row,index){
                        var a = "商品ID： " + row.id + "<br/>";
                        var b = "名称： " + row.pName + "<br/>";
                        var c = "短名称： " + row.shortName + "<br/>";
                        var d =  "商品编码: " + row.code + "<br/>";
                        return  a+ b +c +d ;
                    }
                },
                {field:'hidden2',    title:'销售信息', width:38, align:'left',
                    formatter:function(value,row,index){
                        var a = "销售状态： " + row.isOffShelves + "<br/>";
                        var b = "品牌： " + row.brandName + "<br/>";
                        var c = "商家： " + row.sellerName + "<br/>";
                        var d =  "发货地: " + row.sendAddress + "<br/>";
                        return  a+ b +c +d ;
                    }
                },
                {field:'hidden3',    title:'发货信息', width:32, align:'left',
                    formatter:function(value,row,index){
                        var a = "是否放入商城： " + row.isShowInMallDesc + "<br/>";
                        var b = "结算内容： " + row.submitContent + "<br/>";
                        var c = "商城价： " + row.salesPrice + "<br/>";
                        var d =  "运费: " + row.ftName + "<br/>";
                        return  a+ b +c +d ;
                    }
                },
                {field:'hidden4',    title:'库存信息', width:28, align:'left',
                    formatter:function(value,row,index){
                        var t = '';
						if(row.isAutomaticAdjustStock == 1){
							t = "是";
						}else if(row.isAutomaticAdjustStock == 0){
							t = "否";
						}
                        var a = "是否自动调库存： " + t + "<br/>";
                        var b = "限购数量： " + row.restriction + "<br/>";
                        var c = "30天销量： " + row.thirtyDaysSaleVolume + "<br/>";
                        var d = "剩余库存: " + row.stock + "<br/>";
                        return  a+ b +c +d ;
                    }
                },
                {field:'hidden5',    title:'商品分类', width:45, align:'left',
                    formatter:function(value,row,index){
                        var a = "使用渠道： " + $.trim(row.scopeName) + "<br/>";
                        var b = "分类： " + row.categoryName + "<br/>";
                        var c = "备注： " + row.remark + "<br/>";
                        var c = "三级分类中排序值： " + row.categorySeq + "<br/>";
                        return  a + b + c  ;
                    }
                },
//                {field:'isAvailable',    title:'使用状态', width:15, align:'center'},
//                {field:'sell',     title:'累计销量',  width:15,   align:'center'},
//                {field:'stock',     title:'剩余库存',  width:15,   align:'center'},
//                {field:'lockNum',     title:'锁定库存',  width:15,   align:'center'},
//                {field:'submitContent',     title:'结算内容',  width:15,   align:'center'},
//                {field:'remindStock',     title:'库存提醒',  width:15,   align:'center' },
//                {field:'marketPrice',     title:'市场价',  hidden:true},
//                {field:'categorySeq',     title:'三级分类<br/>中排序值',  width:15,   align:'center' },
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                    	var a = '<a href="javaScript:;" onclick="editSequence(' + index + ')">修改排序值</a>'
                        var b = ' | <a href="javaScript:;" onclick="editIt(' + index + ')">修改</a>';
                        var c = ' | <a href="javaScript:;" onclick="editPrice(' + index + ')">改价</a>';
                        var d = ' | <a href="javaScript:;" onclick="editProductName(' + index + ')">改名</a>';
                        var e = ' | <a href="javaScript:;" onclick="editStock(' + index + ')">改库存</a>';
                        var f = ' | <a href="javaScript:;" onclick="editSell(' + index + ')">改销量</a>';
                        var g = ' | <a href="javaScript:;" onclick="copyProduct(' + index + ')">复制</a>';
                        var h = ' | <a href="javaScript:;" onclick="editCategory(' + row.baseId + ')">修改分类</a>';
                        return a + b + c + d + e + f + g + h;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                    window.location.href = "${rc.contextPath}/product/add/2"
                }
            },'-',{
                iconCls: 'icon-edit',
                text:'从特卖商品复制',
                handler: function(){
                	$("#copySaleProductDiv_id").val('');
					$("#copySaleProductDiv").dialog('open');
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
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/product/forSale", //上架
										{ids: ids.join(","),code:0},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示','上架成功',"info");
												$('#s_data').datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
                                                    code:$("#searchCode").val(),
                                                    name:$("#searchTitle").val(),
                                                    desc:$("#searchDesc").val(),
                                                    isAvailable:$("#isAvailable").val(),
                                                    isOffShelves:$("#isOffShelves").val(),
                                                    startTimeBegin:$("#startTimeBegin").val(),
                                                    startTimeEnd:$("#startTimeEnd").val(),
                                                    endTimeBegin:$("#endTimeBegin").val(),
                                                    endTimeEnd:$("#endTimeEnd").val(),
                                                    brandId:$("input[name='brandId']").val(),
                                                    sellerId:$("input[name='sellerId']").val(),
                                                    searchId:$("input[name='searchId']").val(),
                                                    sendType:$("#sendType").combobox('getValue'),
                                                    baseId:$("#searchBaseId").val(),
                                                    isAvailable:${isAvailable},
                                                    productType:${productType},
                                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                                    isShowInMall:$("#searchIsShowInMall").val(),
                                                    lowerPrice:$('#lowerPrice').val(),
                                                    higherPrice:$('#higherPrice').val()
							                    });
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
                },'-',{
                    iconCls: 'icon-remove',
                    text:'全部下架',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('下架','确定下架吗',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/product/forSale", //下架
										{ids: ids.join(","),code:1},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示','下架成功',"info");
												$('#s_data').datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
                                                    code:$("#searchCode").val(),
                                                    name:$("#searchTitle").val(),
                                                    desc:$("#searchDesc").val(),
                                                    isAvailable:$("#isAvailable").val(),
                                                    isOffShelves:$("#isOffShelves").val(),
                                                    startTimeBegin:$("#startTimeBegin").val(),
                                                    startTimeEnd:$("#startTimeEnd").val(),
                                                    endTimeBegin:$("#endTimeBegin").val(),
                                                    endTimeEnd:$("#endTimeEnd").val(),
                                                    brandId:$("input[name='brandId']").val(),
                                                    sellerId:$("input[name='sellerId']").val(),
                                                    searchId:$("input[name='searchId']").val(),
                                                    sendType:$("#sendType").combobox('getValue'),
                                                    baseId:$("#searchBaseId").val(),
                                                    isAvailable:${isAvailable},
                                                    productType:${productType},
                                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                                    isShowInMall:$("#searchIsShowInMall").val(),
                                                    lowerPrice:$('#lowerPrice').val(),
                                                    higherPrice:$('#higherPrice').val()
							                    });
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
                },'-',{
                    iconCls: 'icon-add',
                    text:'全部可用',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('可用','全部可用',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/product/forAvailable", //全部可用
										{ids: ids.join(","),code:1},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示','操作成功',"info");
												$('#s_data').datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
                                                    code:$("#searchCode").val(),
                                                    name:$("#searchTitle").val(),
                                                    desc:$("#searchDesc").val(),
                                                    isAvailable:$("#isAvailable").val(),
                                                    isOffShelves:$("#isOffShelves").val(),
                                                    startTimeBegin:$("#startTimeBegin").val(),
                                                    startTimeEnd:$("#startTimeEnd").val(),
                                                    endTimeBegin:$("#endTimeBegin").val(),
                                                    endTimeEnd:$("#endTimeEnd").val(),
                                                    brandId:$("input[name='brandId']").val(),
                                                    sellerId:$("input[name='sellerId']").val(),
                                                    searchId:$("input[name='searchId']").val(),
                                                    sendType:$("#sendType").combobox('getValue'),
                                                    baseId:$("#searchBaseId").val(),
                                                    isAvailable:${isAvailable},
                                                    productType:${productType},
                                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                                    isShowInMall:$("#searchIsShowInMall").val(),
                                                    lowerPrice:$('#lowerPrice').val(),
                                                    higherPrice:$('#higherPrice').val()
							                    });
											}else{
												$.messager.alert('提示',data.msg,"error");
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
                    iconCls: 'icon-remove',
                    text:'全部停用',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('停用','全部停用',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/product/forAvailable", //全部停用
										{ids: ids.join(","),code:0},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示','操作成功',"info");
												$('#s_data').datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
                                                    code:$("#searchCode").val(),
                                                    name:$("#searchTitle").val(),
                                                    desc:$("#searchDesc").val(),
                                                    isAvailable:$("#isAvailable").val(),
                                                    isOffShelves:$("#isOffShelves").val(),
                                                    startTimeBegin:$("#startTimeBegin").val(),
                                                    startTimeEnd:$("#startTimeEnd").val(),
                                                    endTimeBegin:$("#endTimeBegin").val(),
                                                    endTimeEnd:$("#endTimeEnd").val(),
                                                    brandId:$("input[name='brandId']").val(),
                                                    sellerId:$("input[name='sellerId']").val(),
                                                    searchId:$("input[name='searchId']").val(),
                                                    sendType:$("#sendType").combobox('getValue'),
                                                    baseId:$("#searchBaseId").val(),
                                                    isAvailable:${isAvailable},
                                                    productType:${productType},
                                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                                    isShowInMall:$("#searchIsShowInMall").val(),
                                                    lowerPrice:$('#lowerPrice').val(),
                                                    higherPrice:$('#higherPrice').val()
							                    });
											}else{
												$.messager.alert('提示',data.msg,"error");
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
                    iconCls: 'icon-edit',
                    text:'开启库存提醒',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('提示','确定开启库存提醒吗？开启库存提醒后，当该商品库存不足时会发送提醒邮件。',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.messager.progress();
                                    $.post("${rc.contextPath}/product/changeEmailRemind",
										{ids: ids.join(","),code:1},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示','开启成功',"info");
												$.messager.progress();
												$("#s_data").datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
                                                    code:$("#searchCode").val(),
                                                    name:$("#searchTitle").val(),
                                                    desc:$("#searchDesc").val(),
                                                    isAvailable:$("#isAvailable").val(),
                                                    isOffShelves:$("#isOffShelves").val(),
                                                    startTimeBegin:$("#startTimeBegin").val(),
                                                    startTimeEnd:$("#startTimeEnd").val(),
                                                    endTimeBegin:$("#endTimeBegin").val(),
                                                    endTimeEnd:$("#endTimeEnd").val(),
                                                    brandId:$("input[name='brandId']").val(),
                                                    sellerId:$("input[name='sellerId']").val(),
                                                    searchId:$("input[name='searchId']").val(),
                                                    sendType:$("#sendType").combobox('getValue'),
                                                    baseId:$("#searchBaseId").val(),
                                                    isAvailable:${isAvailable},
                                                    productType:${productType},
                                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                                    isShowInMall:$("#searchIsShowInMall").val(),
                                                    lowerPrice:$('#lowerPrice').val(),
                                                    higherPrice:$('#higherPrice').val()
							                    });
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
                    iconCls: 'icon-edit',
                    text:'关闭库存提醒',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('提示','确定关闭库存提醒吗？关闭库存提醒后，当该商品库存不足时将收不到提醒邮件。',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.messager.progress();
                                    $.post("${rc.contextPath}/product/changeEmailRemind",
										{ids: ids.join(","),code:0},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示','关闭成功',"info");
												$.messager.progress('close');
												$("#s_data").datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
                                                    code:$("#searchCode").val(),
                                                    name:$("#searchTitle").val(),
                                                    desc:$("#searchDesc").val(),
                                                    isAvailable:$("#isAvailable").val(),
                                                    isOffShelves:$("#isOffShelves").val(),
                                                    startTimeBegin:$("#startTimeBegin").val(),
                                                    startTimeEnd:$("#startTimeEnd").val(),
                                                    endTimeBegin:$("#endTimeBegin").val(),
                                                    endTimeEnd:$("#endTimeEnd").val(),
                                                    brandId:$("input[name='brandId']").val(),
                                                    sellerId:$("input[name='sellerId']").val(),
                                                    searchId:$("input[name='searchId']").val(),
                                                    sendType:$("#sendType").combobox('getValue'),
                                                    baseId:$("#searchBaseId").val(),
                                                    isAvailable:${isAvailable},
                                                    productType:${productType},
                                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                                    isShowInMall:$("#searchIsShowInMall").val(),
                                                    lowerPrice:$('#lowerPrice').val(),
                                                    higherPrice:$('#higherPrice').val()
							                    });
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
                    iconCls: 'icon-edit',
                    text:'放入商城',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('提示','确定放入商城吗？',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.messager.progress();
                                    $.post("${rc.contextPath}/product/updateShowInMall",
										{ids: ids.join(","),isShow:1},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示','操作成功',"info");
												$.messager.progress('close');
												$("#s_data").datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
                                                    code:$("#searchCode").val(),
                                                    name:$("#searchTitle").val(),
                                                    desc:$("#searchDesc").val(),
                                                    isAvailable:$("#isAvailable").val(),
                                                    isOffShelves:$("#isOffShelves").val(),
                                                    startTimeBegin:$("#startTimeBegin").val(),
                                                    startTimeEnd:$("#startTimeEnd").val(),
                                                    endTimeBegin:$("#endTimeBegin").val(),
                                                    endTimeEnd:$("#endTimeEnd").val(),
                                                    brandId:$("input[name='brandId']").val(),
                                                    sellerId:$("input[name='sellerId']").val(),
                                                    searchId:$("input[name='searchId']").val(),
                                                    sendType:$("#sendType").combobox('getValue'),
                                                    baseId:$("#searchBaseId").val(),
                                                    isAvailable:${isAvailable},
                                                    productType:${productType},
                                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                                    isShowInMall:$("#searchIsShowInMall").val(),
                                                    lowerPrice:$('#lowerPrice').val(),
                                                    higherPrice:$('#higherPrice').val()
							                    });
											}else{
												$.messager.alert('提示','操作失败',"error")
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
                    iconCls: 'icon-remove',
                    text:'从商城移除',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('提示','确定从商城中移除吗？',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.messager.progress();
                                    $.post("${rc.contextPath}/product/updateShowInMall",
										{ids: ids.join(","),isShow:0},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示','操作成功',"info");
												$.messager.progress('close');
												$("#s_data").datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
                                                    code:$("#searchCode").val(),
                                                    name:$("#searchTitle").val(),
                                                    desc:$("#searchDesc").val(),
                                                    isAvailable:$("#isAvailable").val(),
                                                    isOffShelves:$("#isOffShelves").val(),
                                                    startTimeBegin:$("#startTimeBegin").val(),
                                                    startTimeEnd:$("#startTimeEnd").val(),
                                                    endTimeBegin:$("#endTimeBegin").val(),
                                                    endTimeEnd:$("#endTimeEnd").val(),
                                                    brandId:$("input[name='brandId']").val(),
                                                    sellerId:$("input[name='sellerId']").val(),
                                                    searchId:$("input[name='searchId']").val(),
                                                    sendType:$("#sendType").combobox('getValue'),
                                                    baseId:$("#searchBaseId").val(),
                                                    isAvailable:${isAvailable},
                                                    productType:${productType},
                                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                                    isShowInMall:$("#searchIsShowInMall").val(),
                                                    lowerPrice:$('#lowerPrice').val(),
                                                    higherPrice:$('#higherPrice').val()
							                    });
											}else{
												$.messager.alert('提示','操作失败',"error")
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
                    iconCls: 'icon-edit',
                    text:'批量分类',
                    handler: function(){
                    	$("#baseIds").val('');
                    	$("#categoryTab tr:gt("+$("#categoryTab").find("tr").first().index()+")").remove();
                    	$("#categoryTab").find("tr").first().find("td").eq(0).find("option").eq(0).attr("selected","selected");
                    	$("#categoryTab").find("tr").first().find("td").eq(1).find("option").eq(0).attr("selected","selected");
                    	$("#categoryTab").find("tr").first().find("td").eq(2).find("option").eq(0).attr("selected","selected");
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                           var ids = [];
                           for(var i=0;i<rows.length;i++){
                               ids.push(rows[i].baseId)
                           }
                           $("#baseIds").val(ids);
                           $('#editProductCategoryDiv').dialog('open');
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                },'-',{
                    iconCls: 'icon-edit',
                    text:'调库存',
                    handler: function(){
                    	$("#batchAdjustStock_form_id").val("");
                    	$("#batchAdjustStock_form_stock").val("");
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                               var ids = '';
                               for(var i=0;i<rows.length;i++){
                                   ids+=(rows[i].baseId+':'+rows[i].id+";");
                               }
                               $("#batchAdjustStock_form_id").val(ids);
                               $("#batchAdjustStock").dialog("open"); 
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                },'-',{
                    iconCls: 'icon-edit',
                    text:'批量改备注',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                           var ids = [];
                           for(var i=0; i<rows.length; i++){
                        	   ids.push(rows[i].id);
                           }
                           $("#editProductRemark_productId").val('');
                           $("#editProductRemark_remark").val('');
                           $("#editProductRemark_productId").val(ids.join(','));
                           $("#editProductRemarkDiv").dialog('open');
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                },'-',{
                    iconCls: 'icon-edit',
                    text:'批量复制商品',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                        	var ids = [];
                        	for(var i=0; i<rows.length; i++){
                        		ids.push(rows[i].id);
                        	}
                            $.messager.confirm('提示','确定批量复制吗？',function(b){
                            	if(b){
                            		batchCopyProduct(ids.join(","),1,1);
                            	}
                     		});
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                } /* ,'-',{
                    iconCls: 'icon-remove',
                    text:'批量添加温馨提示',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('添加温馨提示','确定批量操作吗',function(b){
                            	if(b){
                            		$("#addTip").dialog('open')
                            	}
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                } */],
            pagination:true,
            onLoadSuccess:function(){
            	//$(".panel.datagrid").find("input[type='checkbox']").prop("checked",false);
            	$("#s_data").datagrid('clearSelections');
            }
        });
        
		
		$('#s_saleData').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/productBase/jsonProductInfo',
            queryParams: {type:2},
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
                    		var b = '<a href="${rc.contextPath}/product/edit/' + row.typeCode + '/' + row.id + '" targe="_blank">查看</a>'
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
                    $('#s_data').datagrid('reload',{
                        code:$("#searchCode").val(),
                        name:$("#searchTitle").val(),
                        desc:$("#searchDesc").val(),
                        isAvailable:$("#isAvailable").val(),
                        isOffShelves:$("#isOffShelves").val(),
                        startTimeBegin:$("#startTimeBegin").val(),
                        startTimeEnd:$("#startTimeEnd").val(),
                        endTimeBegin:$("#endTimeBegin").val(),
                        endTimeEnd:$("#endTimeEnd").val(),
                        brandId:$("input[name='brandId']").val(),
                        sellerId:$("input[name='sellerId']").val(),
                        searchId:$("input[name='searchId']").val(),
                        sendType:$("#sendType").combobox('getValue'),
                        baseId:$("#searchBaseId").val(),
                        isAvailable:${isAvailable},
                        productType:${productType},
                        categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                        categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                        categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                        isShowInMall:$("#searchIsShowInMall").val(),
                        lowerPrice:$('#lowerPrice').val(),
                        higherPrice:$('#higherPrice').val()
                    });
                }
            }]
		});
        
		
		<!--修改排序值弹出框-->
		$('#s_categroyData').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/category/jsonProductCategory',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:20,
            columns:[[
            	{field:'categoryName',    title:'分类', width:90, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saveSequenceRow('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelSequenceRow('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		return '<a href="javascript:;" onclick="editSequenceRow(' + index + ')">修改排序值</a>';
                    	}
                    }
                }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateSequenceActions();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateSequenceActions();
            	updateSequenceActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateSequenceActions();
        	},
            pagination:true
        });


		$('#editCategorySequenceDiv').dialog({
    		title:'修改排序值',
    		collapsible:true,
    		closed:true,
    		modal:true,
    		buttons:[{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editCategorySequenceDiv').dialog('close');
                    $('#s_categroyData').datagrid('reload',{
                       	productId:$("#categoryBaseId").val()
                    });
                }
            }]
		});
		
		$('#editProductCategoryDiv').dialog({
    		title:'批量分类',
    		collapsible:true,
    		closed:true,
    		modal:true,
    		buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var baseIds = $("#baseIds").val();
                	var categoryIds = "";
            		$("#categoryTab").find("tr").each(function(){
            			 var firstId = $(this).find("td").eq(0).find("select[name='categoryFirstId']").val();
            			 var secondId = $(this).find("td").eq(1).find("select[name='categorySecondId']").val();
            			 var thirdId = $(this).find("td").eq(2).find("select[name='categoryThirdId']").val();
            			 categoryIds+=firstId+","+secondId+","+thirdId+";";
            		});
            		var categoryFirstId = true;
        			var categorySecondId = true;
        			$("select[name='categoryFirstId']").each(function(){
        				if($(this).val()==0){
        					categoryFirstId = false;
        					return false;
        				}
        			});
        			$("select[name='categorySecondId']").each(function(){
        				if($(this).val()==0){
        					categorySecondId = false;
        					return false;
        				}
        			});
        			
        			if($.trim(baseIds) != "" && categoryIds != "" && categoryFirstId && categorySecondId){
        				$.messager.progress();
        				$.ajax({
        		            url: '${rc.contextPath}/product/classifyProduct',
        		            type: 'post',
        		            dataType: 'json',
        		            data: {'baseIds':baseIds,'categoryIds':categoryIds},
        		            success: function(data){
        		            	$.messager.progress('close');
        		                if(data.status == 1){
        		                	$.messager.alert("提示","操作成功","info");
        		                	$('#editProductCategoryDiv').dialog('close');
        		                	$('#s_data').datagrid('clearSelections');
        		                	$('#s_data').datagrid('reload',{
                                        code:$("#searchCode").val(),
                                        name:$("#searchTitle").val(),
                                        desc:$("#searchDesc").val(),
                                        isAvailable:$("#isAvailable").val(),
                                        isOffShelves:$("#isOffShelves").val(),
                                        startTimeBegin:$("#startTimeBegin").val(),
                                        startTimeEnd:$("#startTimeEnd").val(),
                                        endTimeBegin:$("#endTimeBegin").val(),
                                        endTimeEnd:$("#endTimeEnd").val(),
                                        brandId:$("input[name='brandId']").val(),
                                        sellerId:$("input[name='sellerId']").val(),
                                        searchId:$("input[name='searchId']").val(),
                                        sendType:$("#sendType").combobox('getValue'),
                                        baseId:$("#searchBaseId").val(),
                                        isAvailable:${isAvailable},
                                        productType:${productType},
                                        categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                        categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                        categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                        isShowInMall:$("#searchIsShowInMall").val(),
                                        lowerPrice:$('#lowerPrice').val(),
                                        higherPrice:$('#higherPrice').val()
        		                    });
        		                }else{
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
                    $('#editProductCategoryDiv').dialog('close');
                }
            }]
		});
		
        $("select[name='categoryFirstId']").each(function(){
        	$(this).change(function(){
            	var firstId = $(this).val();
            	var secondId = $(this).parent().next().find("select[name='categorySecondId']");
            	secondId.empty();
            	var option = $("<option>").text("-请选择二级分类-").val("0");
            	secondId.append(option);
            	$.ajax({
            		url: '${rc.contextPath}/category/jsonCategorySecondCode',
    		            type: 'post',
    		            dataType: 'json',
    		            data: {'firstId':firstId},
    		            success: function(data){
    		            	$.each(data,function(key,value){    
                      			var opt = $("<option>").text(value.text).val(value.code);    
                      			secondId.append(opt);  
                  			});
    		            }
            	});
            });
        });
        
        $("select[name='categorySecondId']").each(function(){
        	$(this).change(function(){
            	var secondId = $(this).val();
            	var thirdId = $(this).parent().next().find("select[name='categoryThirdId']");
            	thirdId.empty();
            	var option = $("<option>").text("-请选择三级分类-").val("0");
            	thirdId.append(option);
            	$.ajax({
            		url: '${rc.contextPath}/category/jsonCategoryThirdCode',
    		            type: 'post',
    		            dataType: 'json',
    		            data: {'secondId':secondId},
    		            success: function(data){
    		            	$.each(data,function(key,value){    
                      			var opt = $("<option>").text(value.text).val(value.code);    
                      			thirdId.append(opt);  
                  			});
    		            }
            	});
            });
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
                                $("#s_data").datagrid('clearSelections');
                            	$('#s_data').datagrid('reload',{
                                    code:$("#searchCode").val(),
                                    name:$("#searchTitle").val(),
                                    desc:$("#searchDesc").val(),
                                    isAvailable:$("#isAvailable").val(),
                                    isOffShelves:$("#isOffShelves").val(),
                                    startTimeBegin:$("#startTimeBegin").val(),
                                    startTimeEnd:$("#startTimeEnd").val(),
                                    endTimeBegin:$("#endTimeBegin").val(),
                                    endTimeEnd:$("#endTimeEnd").val(),
                                    brandId:$("input[name='brandId']").val(),
                                    sellerId:$("input[name='sellerId']").val(),
                                    searchId:$("input[name='searchId']").val(),
                                    sendType:$("#sendType").combobox('getValue'),
                                    baseId:$("#searchBaseId").val(),
                                    isAvailable:${isAvailable},
                                    productType:${productType},
                                    categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                    categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                    categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                    isShowInMall:$("#searchIsShowInMall").val(),
                                    lowerPrice:$('#lowerPrice').val(),
                                    higherPrice:$('#higherPrice').val()
			                    });
                                $("#s_data").datagrid('clearSelections');
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
            title:'修改商品价格',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                	$.messager.progress();
                    $('#updatePrice_form').form('submit',{
                        url:"${rc.contextPath}/product/updateProduct",
                        success:function(data){
                        	$.messager.progress('close');
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#addTemplate').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	$("#updatePrice_form_id").val("");
                                	$("#updatePrice_form_price").val("");
                                	$("#updatePrice_form_marketPrice").val("");
                                    $("#s_data").datagrid('clearSelections');
                                	$('#s_data').datagrid('reload',{
                                        code:$("#searchCode").val(),
                                        name:$("#searchTitle").val(),
                                        desc:$("#searchDesc").val(),
                                        isAvailable:$("#isAvailable").val(),
                                        isOffShelves:$("#isOffShelves").val(),
                                        startTimeBegin:$("#startTimeBegin").val(),
                                        startTimeEnd:$("#startTimeEnd").val(),
                                        endTimeBegin:$("#endTimeBegin").val(),
                                        endTimeEnd:$("#endTimeEnd").val(),
                                        brandId:$("input[name='brandId']").val(),
                                        sellerId:$("input[name='sellerId']").val(),
                                        searchId:$("input[name='searchId']").val(),
                                        sendType:$("#sendType").combobox('getValue'),
                                        baseId:$("#searchBaseId").val(),
                                        isAvailable:${isAvailable},
                                        productType:${productType},
                                        categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                        categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                        categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                        isShowInMall:$("#searchIsShowInMall").val(),
                                        lowerPrice:$('#lowerPrice').val(),
                                        higherPrice:$('#higherPrice').val()
                                    });
                                    $("#s_data").datagrid('clearSelections');
                                    $('#updatePrice').dialog('close');
                                });
                            } else if(res.status == 0){
                                $.messager.alert('响应信息',res.msg,'info');
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
                                    $("#s_data").datagrid('clearSelections');
                                	$('#s_data').datagrid('reload',{
                                        code:$("#searchCode").val(),
                                        name:$("#searchTitle").val(),
                                        desc:$("#searchDesc").val(),
                                        isAvailable:$("#isAvailable").val(),
                                        isOffShelves:$("#isOffShelves").val(),
                                        startTimeBegin:$("#startTimeBegin").val(),
                                        startTimeEnd:$("#startTimeEnd").val(),
                                        endTimeBegin:$("#endTimeBegin").val(),
                                        endTimeEnd:$("#endTimeEnd").val(),
                                        brandId:$("input[name='brandId']").val(),
                                        sellerId:$("input[name='sellerId']").val(),
                                        searchId:$("input[name='searchId']").val(),
                                        sendType:$("#sendType").combobox('getValue'),
                                        baseId:$("#searchBaseId").val(),
                                        isAvailable:${isAvailable},
                                        productType:${productType},
                                        categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                        categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                        categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                        isShowInMall:$("#searchIsShowInMall").val(),
                                        lowerPrice:$('#lowerPrice').val(),
                                        higherPrice:$('#higherPrice').val()
                                    });
                                    $("#s_data").datagrid('clearSelections');
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
        
        $('#batchAdjustStock').dialog({
            title:'调库存',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#batchAdjustStock_form').form('submit',{
                        url:"${rc.contextPath}/productBase/batchAdjustStock",
                        onSubmit:function(){
                        	var ids = $("#batchAdjustStock_form_id").val();
                        	var stock = $("#batchAdjustStock_form_stock").val();
                        	if($.trim(ids)==''){
                        		$.messager.alert('提示',"请选择要操作的商品",'error');
                        		return false;
                        	}else if(!/^-?\d+$/.test(stock)){
                        		$.messager.alert('提示',"库存只能为数字",'error');
                        		return false;
                        	}
                        	
                        },
                        success:function(data){
	                            var res = eval("("+data+")");
	                            $.messager.alert('响应信息',res.msg,'info',function(){
	                                $("#s_data").datagrid('clearSelections');
	                            	$('#s_data').datagrid('reload');
	                                $('#batchAdjustStock').dialog('close');
	                            });
                        }
                    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                	$("#s_data").datagrid('clearSelections');
                    $('#batchAdjustStock').dialog('close');
                }
            }]
        });
        
		$('#editProductRemarkDiv').dialog({
            title:'修改备注',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var ids = $("#editProductRemark_productId").val();
                	var remark = $("#editProductRemark_remark").val();
                	if($.trim(remark) == ''){
                		$.messager.alert("提示",'请输入备注',"warning");
                	}else{
                		$.messager.progress();
                		$.ajax({
                            url: '${rc.contextPath}/product/updateProductRemark',
                            type: 'post',
                            dataType: 'json',
                            data: {'ids':ids,'remark':remark},
                            success: function(data){
                            	$.messager.progress('close');
                                if(data.status == 1){
                                	$('#editProductRemarkDiv').dialog('close');
                                	$('#s_data').datagrid('reload',{
                                        code:$("#searchCode").val(),
                                        name:$("#searchTitle").val(),
                                        desc:$("#searchDesc").val(),
                                        isAvailable:$("#isAvailable").val(),
                                        isOffShelves:$("#isOffShelves").val(),
                                        startTimeBegin:$("#startTimeBegin").val(),
                                        startTimeEnd:$("#startTimeEnd").val(),
                                        endTimeBegin:$("#endTimeBegin").val(),
                                        endTimeEnd:$("#endTimeEnd").val(),
                                        brandId:$("input[name='brandId']").val(),
                                        sellerId:$("input[name='sellerId']").val(),
                                        searchId:$("input[name='searchId']").val(),
                                        sendType:$("#sendType").combobox('getValue'),
                                        baseId:$("#searchBaseId").val(),
                                        isAvailable:${isAvailable},
                                        productType:${productType},
                                        categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                        categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                        categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                        isShowInMall:$("#searchIsShowInMall").val(),
                                        lowerPrice:$('#lowerPrice').val(),
                                        higherPrice:$('#higherPrice').val()
                                    });
                                	$.messager.alert("提示",data.msg,"");
                                }else{
                                	$('#s_data').datagrid('reload',{
                                        code:$("#searchCode").val(),
                                        name:$("#searchTitle").val(),
                                        desc:$("#searchDesc").val(),
                                        isAvailable:$("#isAvailable").val(),
                                        isOffShelves:$("#isOffShelves").val(),
                                        startTimeBegin:$("#startTimeBegin").val(),
                                        startTimeEnd:$("#startTimeEnd").val(),
                                        endTimeBegin:$("#endTimeBegin").val(),
                                        endTimeEnd:$("#endTimeEnd").val(),
                                        brandId:$("input[name='brandId']").val(),
                                        sellerId:$("input[name='sellerId']").val(),
                                        searchId:$("input[name='searchId']").val(),
                                        sendType:$("#sendType").combobox('getValue'),
                                        baseId:$("#searchBaseId").val(),
                                        isAvailable:${isAvailable},
                                        productType:${productType},
                                        categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                                        categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                                        categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                                        isShowInMall:$("#searchIsShowInMall").val(),
                                        lowerPrice:$('#lowerPrice').val(),
                                        higherPrice:$('#higherPrice').val()
                                    });
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
                    $('#editProductRemarkDiv').dialog('close');
                }
            }]
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
	
	function editCategory(baseId){
		var urlStr="${rc.contextPath}/productBase/toAddOrUpdate?id="+baseId;
		window.open(urlStr,"_blank");
	}
	
	function batchCopyProduct(ids,fromType,toType){
		$.messager.progress();
		$.ajax({
            url: '${rc.contextPath}/product/copyProductFromOtherType',
            type: 'post',
            dataType: 'json',
            data: {'ids':ids,'fromType':fromType, 'toType':toType},
            success: function(data){
            	$.messager.progress('close');
                if(data.status == 1){
                	$('#copySaleProductDiv').dialog('close');
                	$('#s_data').datagrid('reload',{
                        code:$("#searchCode").val(),
                        name:$("#searchTitle").val(),
                        desc:$("#searchDesc").val(),
                        isAvailable:$("#isAvailable").val(),
                        isOffShelves:$("#isOffShelves").val(),
                        startTimeBegin:$("#startTimeBegin").val(),
                        startTimeEnd:$("#startTimeEnd").val(),
                        endTimeBegin:$("#endTimeBegin").val(),
                        endTimeEnd:$("#endTimeEnd").val(),
                        brandId:$("input[name='brandId']").val(),
                        sellerId:$("input[name='sellerId']").val(),
                        searchId:$("input[name='searchId']").val(),
                        sendType:$("#sendType").combobox('getValue'),
                        baseId:$("#searchBaseId").val(),
                        isAvailable:${isAvailable},
                        productType:${productType},
                        categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                        categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                        categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                        isShowInMall:$("#searchIsShowInMall").val(),
                        lowerPrice:$('#lowerPrice').val(),
                        higherPrice:$('#higherPrice').val()
                    });
                	$.messager.alert("提示",data.msg,"");
                }else{
                	$('#s_data').datagrid('reload',{
                        code:$("#searchCode").val(),
                        name:$("#searchTitle").val(),
                        desc:$("#searchDesc").val(),
                        isAvailable:$("#isAvailable").val(),
                        isOffShelves:$("#isOffShelves").val(),
                        startTimeBegin:$("#startTimeBegin").val(),
                        startTimeEnd:$("#startTimeEnd").val(),
                        endTimeBegin:$("#endTimeBegin").val(),
                        endTimeEnd:$("#endTimeEnd").val(),
                        brandId:$("input[name='brandId']").val(),
                        sellerId:$("input[name='sellerId']").val(),
                        searchId:$("input[name='searchId']").val(),
                        sendType:$("#sendType").combobox('getValue'),
                        baseId:$("#searchBaseId").val(),
                        isAvailable:${isAvailable},
                        productType:${productType},
                        categoryFirstId:$("#searchCategoryFirstId").combobox('getValue'),
                        categorySecondId:$("#searchCategorySecondId").combobox('getValue'),
                        categoryThirdId:$("#searchCategoryThirdId").combobox('getValue'),
                        isShowInMall:$("#searchIsShowInMall").val(),
                        lowerPrice:$('#lowerPrice').val(),
                        higherPrice:$('#higherPrice').val()
                    });
                	$.messager.alert("提示",data.msg,"error");
                }
            },
            error: function(xhr){
            	$.messager.progress('close');
            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
            }
        });		
	}
</script>

</body>
</html>