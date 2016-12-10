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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<div data-options="region:'center',title:'积分商品黑名单'" style="padding:5px;">
	
	<div style="margin:5px">
		<font size="5" color="red">本列表中的商品，不返商品购买积分，也不返合伙人首次成交积分、后续任何一次成交积分</font>
	</div>
	
    <!--数据表格-->
    <table id="s_data" style=""></table>
    
	<div id="addProductDiv" class="easyui-dialog" style="width:500px;height:190px;padding:20px 20px;">
       <form id="addProductForm" method="post" enctype="multipart/form-data">
       		<input type="hidden" value="${type}"  name="type" />
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
function downloadTemplate(){
	window.location.href="${rc.contextPath}/productBlacklist/downloadImportFileTemplate";
}
	function deleteItem(index){
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var type = $("#addProductDiv input[name='type']").val();
		$.messager.confirm('删除','确定删除吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/productBlacklist/delete',
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
            url:'${rc.contextPath}/productBlacklist/jsonProduct?type=${type}',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'editId',    title:'商品ID', width:20, align:'center'},
                {field:'isAvailable',    title:'使用状态', width:20, align:'center'},
                {field:'isOffShelves',    title:'商品状态', width:20, align:'center'},
                {field:'code',    title:'编码', width:20, align:'center'},
                {field:'pName',     title:'名称',  width:50,  align:'center'},
                {field:'shortName',     title:'短名称',  width:40,   align:'center'},
                {field:'remark',     title:'备注',  width:40,   align:'center'},
                {field:'sell',     title:'累计销量',  width:15,   align:'center'},
                {field:'stock',     title:'剩余库存',  width:15,   align:'center'},
                {field:'lockNum',     title:'锁定库存',  width:15,   align:'center'},
                {field:'salesPrice',     title:'售价',  width:15,   align:'center'},
                {field:'ftName',     title:'运费模板',  width:20,   align:'center'},
                {field:'sellerName',     title:'商家',  width:20,   align:'center' },
                {field:'brandName',     title:'品牌',  width:20,   align:'center' },
                {field:'sendAddress',     title:'发货地',  width:20,   align:'center' },
                {field:'warehouse',     title:'分仓',  width:20,   align:'center' },
                {field:'marketPrice',     title:'市场价',  hidden:true},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                    	return '<a href="javaScript:;" onclick="deleteItem(' + index + ')">删除</a>';
                    }
                }
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
                                	var type = $("#addProductDiv input[name='type']").val();
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.ajax({
                    					type: 'post',
                    					url: '${rc.contextPath}/productBlacklist/delete',
                    					data: {'ids': ids.join(","),'type':type},
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
                        url:"${rc.contextPath}/productBlacklist/addProduct",
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