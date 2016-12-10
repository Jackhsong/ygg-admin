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
<div data-options="region:'center',title:''" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'团购商品列表',split:true" style="height:100px;padding-top:10px">
			<form id="searchForm" action="" method="post" >
	        	<tr>
	        		<td class="searchName">商品ID：</td>
					<td class="searchText"><input id="searchId" name="searchId" value="" /></td>
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
		</div>
	</div>
	<div id="addProductDiv" class="easyui-dialog" style="width:500px;height:190px;padding:20px 20px;">
       <form id="addProductForm" method="post" enctype="multipart/form-data">
       		<input type="hidden" value="add"  name="type" />
       		<input type="hidden" value=""  name="fileType" id="fileType" />
	        <table cellpadding="5">
	        	<tr id="productFileTr" style="display:none">
	        		<td>商品文件:</td>
	                <td>
	                	<input name="productFile" type="file" id="productFile" ></input>
	                	<a onclick="downloadTemplate()" href="javascript:;" class="easyui-linkbutton">下载模板</a><br>
	                	<span style="color:red">第一行为商品标题，如有疑问，点击下载模板</span>
	                </td>
	        	</tr>
	            <tr id="productIdTr" style="display:none">
	                <td>商品ID:</td>
	                <td><input id="addProductForm_Id" name="productId" style="width:300px"  maxlength="44" autocomplete="off"></input></td>
	            </tr>
	            <tr id="productNameTr" style="display:none">
	                <td>商品名称:</td>
	                <td>
	                	<span id="addProductForm_productName" style="width:300px"></span>
	                </td>
	            </tr>
	        </table>
   		</form>
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

function searchProduct(){
	$('#s_data').datagrid('load',{
    	code:$("#searchCode").val(),
    	searchId:$("#searchId").val(),
    	name:$("#searchTitle").val()
	});
}
function downloadTemplate(){
	window.location.href="${rc.contextPath}/productBlacklist/downloadImportFileTemplate";
}
	function deleteItem(index){
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var type = "delete";
		$.messager.confirm('删除','确定删除吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/group/updateProductGroupStatus',
		            type: 'post',
		            dataType: 'json',
		            data: {'ids':id,'type':type},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
		                	$('#s_data').datagrid('load');
		                	$.messager.alert("提示",'删除成功',"info");
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

	$(function(){
		$("#addProductForm_Id").keyup(function(){
			$("#productName").val('');
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
		            	$("#addProductForm_productName").html('');
		            }
				});	
			}
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/group/jsonProduct',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
					{field:'id',    title:'序号', align:'center',checkbox:true},
					{field:'editId',    title:'商品ID', width:15, align:'center'},
					{field:'isAvailable',    title:'使用状态', width:15, align:'center'},
					{field:'isOffShelves',    title:'商品状态', width:15, align:'center'},
					{field:'code',    title:'编码', width:20, align:'center'},
					{field:'pName',     title:'名称',  width:50,  align:'center'},
					{field:'shortName',     title:'短名称',  width:40,   align:'center'},
					{field:'remark',     title:'备注',  width:40,   align:'center'},
					{field:'salesPrice',     title:'售价',  width:15,   align:'center'},
					{field:'sellerName',     title:'商家',  width:20,   align:'center' },
					{field:'brandName',     title:'品牌',  width:20,   align:'center' },
					{field:'sendAddress',     title:'发货地',  width:20,   align:'center' }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                	$("#productFileTr").hide();
                	$("#productIdTr").show();
                	$("#productNameTr").show();
                	$("#fileType").val("1");
                    $("#addProductDiv").dialog("open");
                }
            },'-',{
                id:'_add',
                text:'批量导入商品',
                iconCls:'icon-add',
                handler:function(){
                	$("#productFileTr").show();
                	$("#productIdTr").hide();
                	$("#productNameTr").hide();
                	$("#fileType").val("2");
                	$("#addProductDiv").dialog("open");
                }
            },'-',{
                    iconCls: 'icon-remove',
                    text:'批量删除',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('删除','确定删除吗',function(b){
                                if(b){
                                	$.messager.progress();
                                	var type = "delete";
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.ajax({
                    					type: 'post',
                    					url: '${rc.contextPath}/group/deleteProductGroupStatus',
                    					data: {'ids': ids.join(",")},
                    					datatype:'json',
                    					success: function(data){
                    						$.messager.progress('close');
                    						if(data.status == 1){
                    							$('#s_data').datagrid('reload');					
                    						}else{
                    							$.messager.alert('提示','删除出错',"error");
                    						}
                    		            },
                    		            error: function(xhr){
                    		            	$.messager.progress('close');
                    		            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                    		            }
                    				});	
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error");
                        }
                    }
                }],
            pagination:true
        });
        
        
        $('#addProductDiv').dialog({
            title:'新增',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#addProductForm').form('submit',{
                        url:"${rc.contextPath}/group/updateProductGroupStatus",
                        onSubmit:function(){
                        	var productId = $.trim($("#addProductForm_Id").val());
                        	var productName = $("#addProductForm_productName").text();
                        	var file = $("#productFile").val();
                        	var fileType = parseInt($("#fileType").val());
                        	if(fileType == 1 && productId != '' && productName !=''){
                        		return true;
                        		$.messager.progress();
                        	}else if(fileType == 2 && file != ''){
                        		return true;
                        		$.messager.progress();
                        	}else{
                        		$.messager.alert('提示','请输入正确信息','info');
                        		return false;
                        	}
                        },
                        success:function(data){
                        	$.messager.progress('close');
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                	$("#addProductForm_Id").val("");
                                	$("#addProductForm_productName").text("");
                                    $('#s_data').datagrid('load');
                                    $('#addProductDiv').dialog('close');
                                });
                            }else{
                                $.messager.alert('响应信息',res.msg,'error');
                            }
                        }
                    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                	$("#addProductForm_Id").val("");
                	$("#addProductForm_productName").text("");
                    $('#addProductDiv').dialog('close');
                }
            }]
        });       
	});
</script>

</body>
</html>