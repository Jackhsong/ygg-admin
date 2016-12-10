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
	/* text-align:right; */
	text-align:justify;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
.inputStyle{
	width:250px;
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
		<div data-options="region:'north',title:'格格福利商品管理',split:true" style="height: 120px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td>&nbsp;商品ID：</td>
						<td><input name="productId" /></td> 
						<td>&nbsp;&nbsp;商品名称：</td>
						<td><input name="productName" /></td>
						<td>&nbsp;<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
						<td></td>
					</tr>
				</table>
	        </form>
			<br>
			<span style="color: red">&nbsp;每个ID链接只能使用一次活动，不能编辑重复使用！！！</span>
            <br>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
    		<!-- dialog begin -->
			<div id="addGegeProduct" class="easyui-dialog" style="width:700px;height:350px;padding:5px 5px;">
				<form id="addGegeProductForm" method="post">
					<input type="hidden" id="editId" name="editId" />
					<table cellpadding="5">
						<tr>
							<td class="searchName">商品ID：</td>
							<td class="searchText">
								<input id="productId" type="text" style="width:80px" name="productId" />
								<span id="addProductForm_productName" style="width:300px"></span>
							</td>
						</tr>
						<tr>
							<td class="searchName">品牌ID：</td>
							<td class="searchText">
								<input id="brandId" name="brandId" type="text" style="width:400px;"/>
		          				<input id="helpBrand" name="helpBrand" type="text"/>
							</td>
						</tr>
						<tr>
							<td class="searchName">福利价：</td>
							<td class="searchText">
								<input id="salesPrice" type="text" style="width:80px" name="salesPrice" />
								<span>（会同步修改特卖或商城商品价格）</span>
							</td>
						</tr>
						<tr>
							<td class="searchName">购买要求：</td>
							<td class="searchText">
								站内消费额满<input id="limitPrice" type="number" style="width:80px" name="limitPrice" />元
							</td>
						</tr>
                        <tr>
                            <td class="searchName">限购件数：</td>
                            <td class="searchText">
                                <input id="limitNum" type="number" style="width:80px" name="limitNum" />
                            </td>
                        </tr>
                        <tr>
                            <td class="searchName">付款时间：</td>
                            <td class="searchText">
                                <input id="payTimeBegin" type="text" name="payTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'payTimeEnd\')}'})"/>
                                -
                                <input id="payTimeEnd" type="text"  name="payTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'payTimeBegin\')}'})"/>
                            </td>
                        </tr>
						<tr>
							<td class="searchName">备注：</td>
							<td class="searchText">
								<input id="remark" type="text" style="width:300px" name="remark" />
							</td>
						</tr>
					</table>
				</form>
                <br>
                <span style="color: red">&nbsp;每个ID链接只能使用一次活动，不能编辑重复使用！！！</span>
			</div>
			<!-- dialog end -->
		</div>
	</div>
</div>

<script>
	function searchProduct() {
		$('#s_data').datagrid('load', {
			productName : $("#searchForm input[name='productName']").val(),
			productId : $("#searchForm input[name='productId']").val()
		});
	}
	
	function editIt(index){
	    var arr=$("#s_data").datagrid("getData");
	    $("#editId").val(arr.rows[index].id);
	    $("#addGegeProductForm input[name=productId]").val(arr.rows[index].productId);
	    $("#addGegeProductForm input[name=productId]").attr("readonly","readonly");
    	$("#addGegeProductForm input[name=salesPrice]").val(arr.rows[index].gegePrice);
    	$("#addGegeProductForm input[name=limitPrice]").val(arr.rows[index].limitPrice);
    	$("#addGegeProductForm input[name=limitNum]").val(arr.rows[index].limitNum);
    	$("#addGegeProductForm input[name=remark]").val(arr.rows[index].remark);
    	$("#addGegeProductForm input[name=payTimeBegin]").val(arr.rows[index].payTimeBegin);
    	$("#addGegeProductForm input[name=payTimeEnd]").val(arr.rows[index].payTimeEnd);
    	
    	var brandIds = arr.rows[index].brandIds;
    	var url='${rc.contextPath}/brand/jsonBrandCode?isAvailable=1';
    	$("#brandId").combobox('reload',url);
    	$("#brandId").combobox('setValues',brandIds.split(","));
    	$('#addGegeProduct').dialog('open');
	}
	
	$(function(){
		
		$("#brandId").combobox({
			url: '${rc.contextPath}/brand/jsonBrandCode?isAvailable=1',
			valueField: 'code',
			textField: 'text',
			multiple:true,
			editable:false
		});
		
		$("#helpBrand").combobox({
			url: '${rc.contextPath}/brand/jsonBrandCode?isAvailable=1',
			valueField: 'code',
			textField: 'text',
			onSelect:function(brand){
				if(brand != '' && brand != null && brand != undefined){
					var selectValue = $("#brandId").combobox('getValues');
					var id = brand.code;
					if($.inArray(id,selectValue)==-1){
						selectValue.push(id);
					}
					$("#brandId").combobox('setValues',selectValue);
					$("#helpBrand").combobox('clear');
				}
			}
		});
		
		
		$("#productId").change(function(){
			var productId = $(this).val();
			if(productId != ''){
				$.ajax({
					type: 'post',
					url: '${rc.contextPath}/productBlacklist/getProductInfo',
					data: {'productId':productId},
					datatype:'json',
					success: function(data){
						if(data.status == 1){
	                		$("#addProductForm_productName").text(data.msg);							
						}else{
							$("#addProductForm_productName").text('');
						}
		            },
		            error: function(xhr){
		            	$("#addProductForm_productName").text('');
		            }
				});	
			}
		});
		//integral dialog  begin
		$('#addGegeProduct').dialog({
			title:'新增福利商品',
			collapsible:true,
			closed:true,
			modal:true,
			buttons:[{
			    text:'保存',
			    iconCls:'icon-ok',
			    handler:function(){
			    	var params = {};
			    	params.id = $("#editId").val();
			    	params.productId = $.trim($("#productId").val());
			    	params.salesPrice = $.trim($("#salesPrice").val());
			    	params.limitPrice = $.trim($("#limitPrice").val());
			    	params.limitNum = $.trim($("#limitNum").val());
			    	params.remark = $.trim($("#remark").val());
			    	params.payTimeBegin = $.trim($("#payTimeBegin").val());
			    	params.payTimeEnd = $.trim($("#payTimeEnd").val());
			    	params.brandIds = $("#brandId").combobox('getValues').join(",");
			    	if(!/^(-?\d+)(\.\d+)?$/.test(params.salesPrice) || !/^(-?\d+)(\.\d+)?$/.test(params.limitPrice)){
			    		$.messager.alert("提示","价格设置错误","error");
			    	}else if((params.payTimeBegin== '' && params.payTimeEnd !='') || (params.payTimeBegin != '' && params.payTimeEnd =='')){
			    		$.messager.alert("提示","付款时间设置不完整","error");
			    	}else{
			    		$.messager.progress();
		    			$.ajax({
							url: '${rc.contextPath}/product/saveGegeWelfare',
							type: 'post',
							dataType: 'json',
							data: params,
							success: function(data){
								$.messager.progress('close');
								if(data.status == 1){
									$.messager.alert("提示","保存成功","info",function(){
										$('#s_data').datagrid("load");
										$('#addGegeProduct').dialog('close');
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
			        $('#addGegeProduct').dialog('close');
			    }
			}]
	     });
		//integral dialog end
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        idField:'id',
	        url:'${rc.contextPath}/product/jsonGegeWelfare',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        pageSize:50,
	        pageList:[50,100],
	        columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
	            {field:'productId',    title:'商品ID', width:20, align:'center'},
	            {field:'productName',    title:'商品名', width:50, align:'center'},
	            {field:'brand',    title:'品牌', width:50, align:'center'},
	            {field:'productLink',    title:'链接', width:50, align:'center'},
	            {field:'gegePrice',    title:'福利价', width:10, align:'center'},
	            {field:'stock',    title:'总数', width:10, align:'center'},
	            {field:'limitNum',    title:'限购数', width:10, align:'center'},
	            {field:'limitPrice',    title:'购买最低平台消费', width:20, align:'center'},
	            {field:'payTime',    title:'付款时间', width:30, align:'center',
	            	formatter:function(value,row,index){
	            		return row.payTimeBegin+"-"+row.payTimeEnd;
	            	}	
	            },
	            {field:'remark',     title:'备注',  width:30,   align:'center' },
	            {field:'hidden',  title:'操作', width:20,align:'center',
	                formatter:function(value,row,index){
	                    var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a>';	                    	
	                    return a;
	                }
	            }
	        ]],
	        toolbar:[{
                id:'_add',
                text:'新增福利商品',
                iconCls:'icon-add',
                handler:function(){
                	$("#editId").val("");
                	$("#addProductForm_productName").text('');
			    	$("#addGegeProductForm input[name=productId]").val("");
			    	$("#addGegeProductForm input[name=productId]").removeAttr("readonly");
			    	$("#addGegeProductForm input[name=salesPrice]").val("");
			    	$("#addGegeProductForm input[name=limitPrice]").val("");
			    	$("#addGegeProductForm input[name=limitNum]").val("");
			    	$("#addGegeProductForm input[name=remark]").val("");
			    	$("#addGegeProductForm input[name=payTimeBegin]").val("");
			    	$("#addGegeProductForm input[name=payTimeEnd]").val("");
			    	$("#brandId").combobox('clear');
					$("#helpBrand").combobox('clear');
                	$('#addGegeProduct').dialog('open');
                }
            },'-',{
                iconCls: 'icon-add',
                text:'批量删除',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('删除','确定删除吗',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].productId)
                                }
                                $.post("${rc.contextPath}/product/deleteGegeWelfare",
									{productIds: ids.join(",")},
									function(data){
										if(data.status == 1){
											$.messager.alert('提示',data.msg,"int")
											$('#s_data').datagrid('load');
                                            $('#s_data').datagrid('clearSelections');
										}else{
											$.messager.alert('提示','删除出错',"error")
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
	        onLoadSuccess:function(){
            	$("#s_data").datagrid('clearSelections');
            }
	    });
	});
</script>
</body>
</html>