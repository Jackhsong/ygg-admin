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
		<div data-options="region:'north',title:'笨鸟商品报关价格列表',split:true" style="height: 120px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td class="searchName">商品ID：</td>
						<td class="searchText"><input id="searchProductId" name="searchProductId" value="" /></td>
						<td class="searchName">商品名称：</td>
						<td class="searchText"><input id="searchProductName" name="searchProductName" value=""/></td>
						<td class="searchName">商品编码：</td>
						<td class="searchText"><input id="searchProductCode" name="searchProductCode" value=""/></td>
						<td class="searchText" style="padding-top: 5px">
							<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>

			<div id="updateProduct" class="easyui-dialog" style="width:450px;height:180px;padding:20px 20px;">
		        <form id="updateProduct_form" method="post">
					<input id="updateProduct_form_id" type="hidden" name="id" value="" >
		            <table>
		                <tr>
		                    <td>商品ID:</td>
		                    <td><input type="number" id="updateProduct_form_productId" name="productId" style="width:280px" /></td>
		                </tr>
		                <tr>
		                    <td>报关价:</td>
		                    <td><input type="number" id="updateProduct_form_price" name="price"  style="width:280px" /></td>
		                </tr>
		            </table>
		        </form>
		   </div>
       		
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
	function searchOrder(){
		$('#s_data').datagrid('load', {
			productId : $("#searchProductId").val(),
			productName : $("#searchProductName").val(),
			productCode : $("#searchProductCode").val()
		});
	}
	
	function editIt(index){
	    var arr=$("#s_data").datagrid("getData");
	    $("#updateProduct_form_id").val(arr.rows[index].id);
		$("#updateProduct_form_productId").val(arr.rows[index].productId);
		$("#updateProduct_form_price").val(arr.rows[index].price);
	    $("#updateProduct").dialog('open');		
	}
	
	function deleteId(id){
		$.messager.confirm('提示','确认删除吗？',function(r){
		    if (r){
		    	$.messager.progress();
    			$.ajax({
					url: '${rc.contextPath}/birdex/deleteBirdexProduct',
					type: 'post',
					dataType: 'json',
				    data: {'id':id},
					success: function(data){
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

	$(function(){
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        idField:'id',
	        url:'${rc.contextPath}/birdex/jsonBirdexProductInfo',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        pageSize:50,
	        pageList:[50,100],
	        columns:[[
				{field:'id',    title:'序号', align:'center',checkbox:true},
	            {field :'productId',    title:'商品ID', width:20, align:'center'},
	            {field :'productCode', title : '商品编码', width : 40, align : 'center'},
	            {field :'productName', title : '商品名称', width : 80, align : 'center'},
				{field :'price', title : '报关价', width : 30, align : 'center'},
	            {field:'hidden',  title:'操作', width:30,align:'center',
	                formatter:function(value,row,index){
	                    var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';
	                    var b = '<a href="javaScript:;" onclick="deleteId(' + row.id + ')">删除</a>';
	                    return a + b;
	                }
	            }
	        ]],
	        toolbar:[{
                iconCls: 'icon-add',
                text:'新增',
                handler: function(){
                	$("#updateProduct_form_id").val("0");
                	$("#updateProduct_form_productId").val("");
                	$("#updateProduct_form_price").val("");
    	        	$('#updateProduct').dialog('open');
                }
            }],
	        pagination:true
	    });
		
		$('#updateProduct').dialog({
	        title:'信息',
	        collapsible:true,
	        closed:true,
	        modal:true,
	        buttons:[{
	            text:'保存',
	            iconCls:'icon-ok',
	            handler:function(){
	            	var params = {
	            			id:$("#updateProduct_form_id").val(),
	            			productId:$.trim($("#updateProduct_form_productId").val()),
	            			price:$.trim($("#updateProduct_form_price").val())
	            	};
	            	if(params.productId == '' || params.price == ''){
	            		$.messager.alert("提示","请填写完整信息","error");
	            	}else{
	            		$.messager.progress();
	                	$.ajax({
	                		url:"${rc.contextPath}/birdex/saveOrUpdateBirdexProduct",
	    	    			type: 'post',
	    	    			dataType: 'json',
	    	    			data: params,
	    	    			success: function(data){
	    	    				$.messager.progress('close');
	    	    				if(data.status == 1){
	    	    					$.messager.alert('响应信息',"保存成功",'info',function(){
	                                	$("#updateProduct_form_id").val("");
	                                	$("#updateProduct_form_productId").val("");
	                                	$("#updateProduct_form_price").val("");
	                                    $('#s_data').datagrid('reload');
	                                    $('#updateProduct').dialog('close');
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
	            	$('#updateProduct').dialog('close');
	            	$("#updateProduct_form_id").val("");
                	$("#updateProduct_form_productId").val("");
                	$("#updateProduct_form_price").val("");
	            }
	        }]
	    });
	});
</script>
</body>
</html>