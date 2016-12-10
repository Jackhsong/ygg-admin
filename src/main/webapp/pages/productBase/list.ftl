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
a{
	text-decoration: none;
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
		<div data-options="region:'north',title:'商品管理',split:true" style="height: 190px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/productBase/exportResult" method="get" >
		            <table class="search">
		                <tr>
		                	<td class="searchName">商品ID：</td>
							<td class="searchText"><input id="searchId" name="productId" value="" /></td>
		                    <td class="searchName">商品编码：</td>
		                    <td class="searchText"><input id="searchCode" name="code" value="" /></td>
		                    <td class="searchName">商品条码：</td>
		                	<td class="searchText"><input id="searchBarCode" name="barCode" value=""/></td>
		                </tr>
		                <tr>
		                	<td class="searchName">商品名称：</td>
		                    <td class="searchText"><input id="searchName" name="productName" value="" /></td>
							<td class="searchName">一级类目：</td>
							<td class="searchText"><input name="firstCategory" id="searchFirstCategory"/></td>
		                    <td class="searchName">商家备注：</td>
							<td class="searchText"><input id="searchRemark" name="remark" value=""/></td>
		                </tr>
		                <tr>
							<td class="searchName">商家：</td>
		                    <td class="searchText"><input id="sellerId" type="text" name="sellerId" /></td>
		                    <td class="searchName">二级类目：</td>
							<td class="searchText"><input name="secondCategory" id="searchSecondCategory"/></td>
		                    <td class="searchName">品牌：</td>
							<td class="searchText"><input id="brandId" type="text" name="brandId" /></td>
		                </tr>
						<tr>
							<td class="searchName"></td>
							<td class="searchText"></td>
		                	<td class="searchName"></td>
							<td class="searchText">
								<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
								&nbsp;
								<a id="exportBtn" onclick="exportProduct()" href="javascript:;" class="easyui-linkbutton" >导出查询结果</a>
		                	</td>
		                	<td class="searchName"></td>
							<td class="searchText"></td>
						</tr>
		            </table>
		        </form>
    		</div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
    		<!-- 调库存begin -->
   	 		<div id="editStock_div" style="width:1100px;height:600px;padding:20px 20px;">
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

   	 		<!-- 复制 begin-->
			<div id="copyProduct" class="easyui-dialog" style="width:450px;height:150px;padding:20px 20px;">
	            <form id="copyProduct_form" method="post">
				<input id="copyProduct_form_id" type="hidden" name="id" value="" >
	            <table cellpadding="5">
	                <tr>
	                	<td>复制后的商品编码:</td>
	                	<td>
	                		<input id="copyProduct_form_code" name="code" value="" maxlength="20" size="30"/>
	                	</td>
	                </tr>
	            </table>
	        	</form>
	        </div>
	        <!-- 复制 end-->

			<!-- 预览商品 begin-->
			<div id="previewPicture_div" class="easyui-dialog" style="width:1000px;height:150px;padding:20px 20px;">
				<fieldset>
					<legend>主图</legend>
					<table id="mainImages_tab" style="width: 100%;"></table>
				</fieldset>
	            <fieldset>
					<legend>详情图</legend>
					<table id="detailImages_tab" style="width: 100%;" cellpadding="10"></table>
				</fieldset>
	        </div>
	        <!-- 预览商品 end-->

            <div id="historySalesVolume_div"></div>

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
		            return
		        } else{
		            $.messager.alert('响应信息',data.message,'error',function(){
		                return
		            });
		        }
			}
		});
	};
	
	function copyProduct(id){
		$("#copyProduct_form_id").val('');
    	$("#copyProduct_form_code").val('');
    	
    	$("#copyProduct_form_id").val(id);
    	$('#copyProduct').dialog('open');

	}

	function searchProduct(){
    	$('#s_data').datagrid('load',{
    		productId:$("#searchId").val(),
            isAvailable:${isAvailable!"1"},
        	code:$("#searchCode").val(),
        	barCode:$("#searchBarCode").val(),
        	productName:$("#searchName").val(),
        	firstCategory:$("input[name='firstCategory']").val(),
        	secondCategory:$("input[name='secondCategory']").val(),
        	brandId:$("input[name='brandId']").val(),
        	sellerId:$("input[name='sellerId']").val(),
        	remark:$("#searchRemark").val()
    	});
	}
	
	function clearSearch(){
		$("#searchId").val('');
    	$("#searchCode").val('');
    	$("#searchBarCode").val('');
    	$("#searchName").val('');
    	$("#searchFirstCategory").combobox('clear');
    	$("#searchSecondCategory").combobox('clear');
    	$("#brandId").combobox('clear');
    	$("#sellerId").combobox('clear');
    	$("#searchRemark").val('');
    	$('#s_data').datagrid('load',{
            isAvailable:${isAvailable!"1"}
		});
	}

	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr="${rc.contextPath}/productBase/toAddOrUpdate?id="+arr.rows[index].productId;
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
		var proudctBaseId = arr.rows[index].productId;
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


	function previewPicture(productId){
        $.ajax({
            url: '${rc.contextPath}/productBase/previewPicture',
            type: 'post',
            dataType: 'json',
            data: {'id':productId},
            beforeSend: function(XHR){
                $.messager.progress();
			},
            success: function(data){
                if(data.status == 1){
                    var mainImages = '<tr>';
					var detailImages = '';
					var size = 0;
					$.each(data.mainImages, function(value, image){
                        mainImages += '<td align="center"><a href="'+image.url+'" target="_blank"><img alt="" src="'+image.url+'" style="max-height:200px"/></a><br/>'+image.width+'x'+image.height+'</td>';
					});
                    $.each(data.detailImages, function(value, image){
						detailImages += '<tr align="center"><td align="center"><a href="'+image.url+'" target="_blank"><img alt="" src="'+image.url+'"/></a><br/>'+image.width+'x'+image.height+'</td></tr>';
                        size++;
					});
					$('#mainImages_tab').html(mainImages+"</tr>");
					$('#detailImages_tab').html(detailImages);
					$('#previewPicture_div').dialog('resize',{
                        width: 1000,
                        height: size*250
					});
                }else{
                    $.messager.alert("提示",data.msg,"info");
                }
            },
            complete: function(XHR, TS){
                $.messager.progress('close');
			},
            error: function(xhr){
                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
            }
        });
		$("#previewPicture_div").dialog('open');
	}

	function historySalesVolume(id){
        var url = "${rc.contextPath}/productBase/historySalesVolume/" + id;
        $('#historySalesVolume_div').dialog('refresh', url);
        $('#historySalesVolume_div').dialog('open');
	}

	$(function(){
		$('#sellerId').combobox({
				panelWidth:350,
            	panelHeight:350,
				mode:'remote',
			    url:'${rc.contextPath}/seller/jsonSellerCode',   
			    valueField:'code',   
			    textField:'text'  
		});
		
		$('#brandId').combobox({   
			    url:'${rc.contextPath}/brand/jsonBrandCode',   
			    valueField:'code',   
			    textField:'text'  
		});
		
		$('#searchFirstCategory').combobox({
			valueField: 'code',
		    textField: 'text',
		    url: '${rc.contextPath}/category/jsonCategoryFirstCode',
		    editable: false, 
		    onChange: function(firstCategoryId) {
		        $('#searchSecondCategory').combobox({
		            valueField: 'code',
		            textField: 'text',
		            url: '${rc.contextPath}/category/jsonCategorySecondCode?firstId='+firstCategoryId,
		            editable: false, //不可编辑，只能选择
		        });
		    }
		});
		
		$('#searchSecondCategory').combobox({
			valueField: 'code',
		    textField: 'text',
		    url: '${rc.contextPath}/category/jsonCategorySecondCode',
		    editable: false
		});
	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'productId',
            url:'${rc.contextPath}/productBase/jsonProductBaseInfo',
            loadMsg:'正在装载数据...',
            queryParams:{
                isAvailable:${isAvailable!"1"}
            },
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'productId',    title:'序号', align:'center',checkbox:true},
                {field:'image',    title:'商品主图', width:30,align:'center',
					formatter:function(value, row, index){
						if(row.image1 == null || row.image1 == '' || row.image1 == undefined){
							return "<a href='http://img.gegejia.com/platform/appImage.png' target='_blank'><img alt='' src='http://img.gegejia.com/platform/appImage.png' style='max-height:80px'/></a>";
						}else{
                            return "<a href='"+row.image1+"' target='_blank'><img alt='' src='"+row.image1+"' style='max-height:80px'/></a>";
						}
					}
				},
                {field:'baseInfo',    title:'基本信息', width:90, align:'left',
					formatter:function(value, row, index){
						var productId 	= "<br/>商品ID："+row.productId+"<br/>";
						var productName = "商品名称："+row.productName+"<br/>";
						var netVolume 	= "商品规格："+row.netVolume+"<br/>";
						var productCode = "商品编码："+row.productCode+"<br/><br/>";
						return productId + productName + netVolume + productCode;
					}
				},
                {field:'detailInfo1',    title:'详细信息', width:50, align:'left',
					formatter:function(value, row, index){
						var categorys = '';
						$.each(row.categoryList,function(value, category){
							categorys += category.categoryFirstName+" > "+category.categorySecondName+" > "+category.categoryThirdName+"<br/>";
						});
						var category = "<br/>分类："+categorys;
						var brandName = "品牌："+row.brandName+"<br/>";
						var sellerName = "商家："+row.sellerName+"<br/>";
						var sendAddress= "发货地："+row.sendAddress+"<br/><br/>";
						return category + brandName + sellerName + sendAddress;
					}
				},
                {field:'detailInfo2',    title:'详细信息', width:40, align:'left',
                    formatter:function(value, row, index){
                        var manufacturerDate = "<br/>生产日期："+row.manufacturerDate+"<br/>";
                        var durabilityPeriod = "保质期："+row.durabilityPeriod+"<br/>";
                        var expireDate = "有效期至："+row.expireDate+"<br/>";
						var goodComment = "好评率：<a href='${rc.contextPath}/comment/pcList?productBaseId="+row.productId+"' target='_blank'>"+row.goodCommentRate+"</a><br/><br/>";
						return manufacturerDate + expireDate + durabilityPeriod  + goodComment;
                    }
                },
                {field:'submitInfo',     title:'结算信息',  width:30,   align:'left' ,
                    formatter:function(value, row, index){
						var submitType = "结算方式："+row.submitType+"<br/>";
						var submitContent = "结算内容："+row.submitContent+"<br/>";
						var freightType = "运费结算："+row.freightType+"<br/>";
						return submitType + submitContent + freightType;
					}
				},
                {field:'salesInfo',     title:'销售信息',  width:30,   align:'left',
                    formatter:function(value, row, index){
						var saleStatus = '';
						if(row.saleStatus == 1){
							saleStatus = '<label style="color: red;font-size:x-large">New!</label><br/>'
						}
                        var availableStock = "未分配库存："+row.availableStock+"<br/>";
                        var salesVolume7 = "7日销量："+row.salesVolumeIn7+"<br/>";
                        var salesVolume30 = "30日销量："+row.salesVolumeIn30+"<br/>";
                        return saleStatus + availableStock + salesVolume7 + salesVolume30;
                    }
				},
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑商品</a><br/>';
                        var b = '<a href="javaScript:;" onclick="editStock(' + index + ')">分配库存</a><br/>';
						var c =	'<a href="javaScript:;" onclick="previewPicture(' + row.productId + ')">预览商品</a><br/>';
						/*var d =	'<a href="javaScript:;" onclick="historySalesVolume(' + row.productId + ')">历史销量</a><br/>';*/
						var d = '<a href="${rc.contextPath}/productBase/historySalesVolume/'+row.productId+'" target="_blank">历史销量</a><br/>';
                        var e = '<a href="javaScript:;" onclick="copyProduct(' + row.productId + ')">复制商品</a><br/>';
                        return a + b + c + d + e;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                    window.open('${rc.contextPath}/productBase/toAddOrUpdate',"_blank");
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
                                        ids.push(rows[i].productId)
                                    }
                                    $.post("${rc.contextPath}/productBase/forAvailable", //全部可用
										{ids: ids.join(","),code:1},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('reload');
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
                    iconCls: 'icon-remove',
                    text:'全部停用',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('停用','全部停用',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].productId)
                                    }
                                    $.post("${rc.contextPath}/productBase/forAvailable", //全部停用
										{ids: ids.join(","),code:0},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('reload');
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
                               ids.push(rows[i].productId)
                           }
                           $("#baseIds").val(ids);
                           $('#editProductCategoryDiv').dialog('open');
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                }],
            pagination:true,
            onLoadSuccess:function(){
            	$("#s_data").datagrid('clearSelections');
            }
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
        		            url: '${rc.contextPath}/productBase/classifyProduct',
        		            type: 'post',
        		            dataType: 'json',
        		            data: {'ids':baseIds,'categoryIds':categoryIds},
        		            success: function(data){
        		            	$.messager.progress('close');
        		                if(data.status == 1){
        		                	$.messager.alert("提示","操作成功","info");
        		                	$('#editProductCategoryDiv').dialog('close');
        		                	$('#s_data').datagrid('clearSelections');
        		                	$('#s_data').datagrid('reload');
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
        
        $('#copyProduct').dialog({
            title:'复制商品',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var params = {};
			    	params.id = $.trim($("#copyProduct_form_id").val());
			    	params.code = $.trim($("#copyProduct_form_code").val());
			    	if(params.id==''){
			    		$.messager.alert('提示',"请选择要操作的商品",'error');
                		return false;
			    	}else if(params.code == ''){
			    		$.messager.alert("提示","请输入商品编码","error");
			    		return false;
			    	}else{
			    		$.messager.progress();
		    			$.ajax({
							url: '${rc.contextPath}/productBase/copyProduct',
							type: 'post',
							dataType: 'json',
							data: params,
							success: function(res){
								$.messager.progress('close');
								if(res.status == 1){
									$.messager.alert("提示","保存成功","info",function(){
										$("#s_data").datagrid('clearSelections');
	                                	$('#s_data').datagrid('reload');
	                                    $('#copyProduct').dialog('close');
	                                    var urlStr="${rc.contextPath}/productBase/toAddOrUpdate?id="+res.data;
	    		                		window.open(urlStr,"_blank");
									});
								}else{
									$.messager.alert("提示",res.message,"error");
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
                	$("#s_data").datagrid('clearSelections');
                    $('#copyProduct').dialog('close');
                }
            }]
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
                    $('#s_data').datagrid('reload');
                }
            }]
		});

		/**商品图片预览弹出框*/
		$("#previewPicture_div").dialog({
            title:'商品图片预览',
            collapsible:true,
            minimizable:true,
            maximizable:true,
            closed:true,
            modal:true,
            resizable:true,
            buttons:[{
                text:'关闭',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){

                    $('#previewPicture_div').dialog('close');
                }
            }]
		});

        $('#historySalesVolume_div').dialog({
            title: '历史销量',
            width: 1100,
            height: 700,
            closed: true,
            href: '${rc.contextPath}/productBase/historySalesVolume/0',
            buttons:[{
                text:'关闭    ',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#historySalesVolume_div').dialog("close");
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