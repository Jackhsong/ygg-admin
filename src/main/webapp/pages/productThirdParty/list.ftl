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
<div data-options="region:'center',title:'第三方商品管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 110px;">
			<div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
				<form action="" method="post">
					<p>
						<span>选择渠道：</span>
						<span><input type="text" id="searchChannelId" name="channelId"/></span>
						<span>商品编码：</span>
						<span><input type="text" id="searchProductCode" name="productCode"/></span>
						<span>采购商品ID：</span>
						<span><input type="text" id="searchProviderProductId" name="providerProductId"/></span>
						<span>第三方商品ID：</span>
						<span><input type="text" id="searchProductId" name="productId"/></span>
						<span>商品名称：</span>
						<span><input type="text" id="searchProviderProductName" name="providerProductName"/></span>
						<span>
							<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</span>
					</p>
				</form>
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增商品 begin -->
		    <div id="editThirdPartyProductDiv" class="easyui-dialog" style="width:600px;height:300px;padding:15px 20px;">
		        <form id="editThirdPartyProductForm" method="post">
					<input id="editThirdPartyProductForm_id" type="hidden" name="id" value="" >
					<p>
						<span>选择渠道：</span>
						<span><input type="text" name="channelId" id="editThirdPartyProductForm_channelId" value="" maxlength="8" style="width: 300px;"/></span>
						<font color="red">*</font>
					</p>
					<p>
						<span>商品编码：</span>
						<span><input type="text" name="productCode" id="editThirdPartyProductForm_productCode" value="" maxlength="15" style="width: 300px;"/></span>
						<font color="red">*</font>
					</p>
					<p>
						<span>入库仓库：</span>
						<span><select name="storageId" id="editThirdPartyProductForm_storageId"><option value="0">--请选择--</option></select></span>
					</p>
					<p>
						<span>采购商品ID：</span>
						<span id="providerProductId"></span>
					</p>
					<p>
						<span>采购商品名称：</span>
						<span id="providerProductName"></span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增商品 end -->
		    
		    <!-- 新增渠道 begin -->
		    <div id="editChannelDiv" class="easyui-dialog" style="width:500px;height:150px;padding:15px 20px;">
		    	<form id="editChannelForm" method="post">
		    		渠道名称：<input type="text" id="editChannelForm_name" name="name" value="" maxlength="20" size="50"/>
		    	</form>
		    </div>
		    <!-- 新增渠道 end -->
		    
    		<!-- 批量新增begin -->
    		<div id="batchAdd_div" style="width:600px;height:200px;padding:20px 20px;">
    			<form method="post" id="batchAddForm" enctype="multipart/form-data">
	    			<table cellpadding="5">
	    				<tr>
		    				<td colspan="2">
		    					<a onclick="downloadTemplate()" href="javascript:;" class="easyui-linkbutton">下载模板</a>
		    				</td>
		    			</tr>
		    			<tr>
		    				<td>文件：</td>
		    				<td><input type="text" id="batchAddForm_userId" name="userFile" style="width:300px"></td>
		    			</tr>
	    			</table>
    			</form>
   	 		</div>
   	 		<!-- 批量新增end -->
   	 	
   	 		<!-- 调库存 begin -->
   	 		<div id="editStock_div" style="width:1100px;height:700px;padding:15px 15px;">
   	 		
   	 		</div> 
   	 		<!-- 调库存 end -->
   	 		  
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
	
	function searchProduct(){
    	$('#s_data').datagrid('load',{
    		id:$("#searchProductId").val(),
    		channelId:$("#searchChannelId").combobox('getValue'),
    		productCode:$("#searchProductCode").val(),
    		providerProductId:$("#searchProviderProductId").val(),
    		providerProductName:$("#searchProviderProductName").val()
    	});
	}

	<!--编辑排序相关 begin-->
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
	
	function saverow(index){
		$('#s_data').datagrid('endEdit', index);
	};
	
	function cancelrow(index){
		$('#s_data').datagrid('cancelEdit', index);
	};
	
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/thirdProduct/updateThirdPartyProductSales',
			type:"POST",
			data: {id:row.id,sales:row.totalSales},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('reload');
		            return;
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	<!--编辑排序相关 end-->
	
	function clealThirdPartyProductDiv(){
		$("#editThirdPartyProductForm_channelId").combobox('clear');
		$("#editThirdPartyProductForm_id").val('');
		$("#editThirdPartyProductForm_productCode").val('');
		$("#editThirdPartyProductForm_storageId").empty();
		$("#providerProductId").text('');
		$("#providerProductName").text('');
	}
	function editIt(index){
		clealThirdPartyProductDiv();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var channelId = arr.rows[index].channelId;
		var storageId = arr.rows[index].storageId;
		var storageName = arr.rows[index].storageName;
		var productCode = arr.rows[index].productCode;
		var providerProductId = arr.rows[index].providerProductId;
		var productName = arr.rows[index].productName;
		var url = '${rc.contextPath}/channel/jsonChannelCode?id='+channelId;
    	$('#editThirdPartyProductForm_id').val(id);
    	$('#editThirdPartyProductForm_channelId').combobox('reload',url);
    	$('#editThirdPartyProductForm_productCode').val(productCode);
    	$('#providerProductId').text(providerProductId);
    	$('#providerProductName').text(productName);
    	
    	var options = '<option value="'+storageId+'" selected="selected">' + storageName + '</option>';
		$('#editThirdPartyProductForm_storageId').empty().append(options);
    	$('#editThirdPartyProductDiv').dialog('open');
	}
	
	function downloadTemplate(){
		window.location.href="${rc.contextPath}/thirdProduct/downloadTemplate";
	}
	
	function editStock(id){
		var url = "${rc.contextPath}/purchase/editStock?id=" + id;
        $('#editStock_div').dialog('refresh', url);
        $('#editStock_div').dialog('open');
	}
	$(function(){
		
		$('#editStock_div').dialog({
            title: '调库存',
            closed: true,
            href: '${rc.contextPath}/purchase/editStock',
            buttons:[{
                text:'取消 ',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                	$('#s_data').datagrid('reload');
                    $('#editStock_div').dialog("close");
                }
            }]
        });
		
		$("input[name='channelId']").each(function(){
			$(this).combobox({
				url:'${rc.contextPath}/channel/jsonChannelCode',   
			    valueField:'code',   
			    textField:'text'
			});
		});
		
		$("#editThirdPartyProductForm_productCode").change(function(){
			var productCode = $.trim($(this).val());
			if(productCode != ''){
   	            $.ajax({
   	            	url:'${rc.contextPath}/purchase/findProviderProductByBarcode',
   	            	type:'post',
   	            	dataType:'json',
   	            	data:{"barCode":productCode},
   	            	success:function(data){
   	            		$.messager.progress('close');
						if(data.status == 1){
							$('#providerProductId').text(data.id);
							$('#providerProductName').text(data.name);
							var options = '<option value="0">--请选择--</option>';
							$.each(data.storageList,function(i){
								options += '<option value="'+this.storageId+'" >' + this.storageName + '</option>';
							});
							$('#editThirdPartyProductForm_storageId').empty().append(options);
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
		<!--列表数据加载-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/thirdProduct/jsonThirdProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            columns:[[
                {field:'channelName',    title:'渠道名', width:30, align:'center'},
                {field:'id',    title:'第三方商品ID', width:20, align:'center'},
                {field:'providerProductId',    title:'采购商品ID', width:20, align:'center'},
                {field:'storageName',    title:'仓库名', width:30, align:'center'},
                {field:'productCode',    title:'商品编码', width:40, align:'center'},
                {field:'productName',    title:'商品名称', width:60, align:'center'},
                {field:'groupCount',    title:'组合件数', width:20, align:'center'},
                {field:'totalStock',    title:'总库存', width:20, align:'center'},
                {field:'availableStock',    title:'可用库存', width:20, align:'center'},
                {field:'totalSales',    title:'累计销量', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a> | ';
                            var b = '<a href="javascript:;" onclick="editrow(' + index + ')">改销量</a> | ';
                            var c = '<a href="javascript:;" onclick="editStock(' + row.providerProductId + ')">调库存</a>';
                    		return a + b + c;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'back',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                	clealThirdPartyProductDiv();
                	$('#editThirdPartyProductDiv').dialog('open');
                }
            },'-',{
                id:'_add',
                text:'批量新增',
                iconCls:'icon-add',
                handler:function(){
                	$('#batchAdd_div').dialog('open');
                }
            },'-',{
                id:'_edit',
                text:'新增渠道',
                iconCls:'icon-add',
                handler:function(){
                	$("#editChannelForm_name").val('');
                	$('#editChannelDiv').dialog('open');
                }
            }],
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
		
		
	    $('#editThirdPartyProductDiv').dialog({
	    	title:'编辑',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editThirdPartyProductForm').form('submit',{
	    				url: "${rc.contextPath}/thirdProduct/saveOrUpdate",
	    				onSubmit:function(){
	    					var channelId = $("#editThirdPartyProductForm_channelId").combobox('getValue');
	    					var productCode = $("#editThirdPartyProductForm_productCode").val();
	    					var storageId = $("#editThirdPartyProductForm_storageId").val();
	    					var providerProductId =$("#providerProductId").text();
	    					var providerProductName =$("#providerProductName").text();
	    					if(channelId == null || channelId == '' || channelId == undefined){
	    						$.messager.alert('提示','请选择渠道','error');
	    						return false;
	    					}else if($.trim(productCode) == ''){
	    						$.messager.alert('提示','请填写商品编码','error');
	    						return false;
	    					}else if(storageId == null || storageId == undefined || parseInt(storageId)==0){
	    						$.messager.alert('提示','请填选择入库仓库','error');
	    						return false;
	    					}else if($.trim(providerProductId) == ''){
	    						$.messager.alert('提示','请填写正确的编码','error');
	    						return false;
	    					}else if($.trim(providerProductName) == ''){
	    						$.messager.alert('提示','请填写正确的编码','error');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#s_data').datagrid('reload');
	                                $('#editThirdPartyProductDiv').dialog('close');
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
	                $('#editThirdPartyProductDiv').dialog('close');
	            }
	    	}]
	    });
	    
	    
	    $('#editChannelDiv').dialog({
	    	title:'编辑',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editChannelForm').form('submit',{
	    				url: "${rc.contextPath}/channel/saveOrUpdate",
	    				onSubmit:function(){
	    					var name = $("#editChannelForm_name").val();
	    					if($.trim(name) == ''){
	    						$.messager.alert('提示','请填写渠道名称','error');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#s_data').datagrid('reload');
	                                $('#editChannelDiv').dialog('close');
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
	                $('#editChannelDiv').dialog('close');
	            }
	    	}]
	    });
	    
	    $('#batchAdd_div').dialog({
	    	title: '向批量用户ID发放优惠券',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '发送',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#batchAddForm').form('submit',{
	    				url: "${rc.contextPath}/thirdProduct/importProduct",
	    				onSubmit:function(){
	    					var userId = $("#batchAddForm_userId").filebox("getValue");
	    					if(userId == ""){
	    						$.messager.alert("info","请选择文件","warn");
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',res.msg,'info',function(){
	                            	$("#batchAddForm_userId").filebox('clear');
	                                $('#s_data').datagrid('reload');
	                                $('#batchAdd_div').dialog('close');
	                            });
	                        } else{
	                            $.messager.alert('响应信息',res.msg,'info');
	                        } 
	    				}
	    			});
	    		}
	    	},
	    	{
	    		text:'取消',
	            align:'left',
	            iconCls:'icon-cancel',
	            handler:function(){
	            	$("#batchAddForm_userId").filebox('clear');
                    $('#batchAdd_div').dialog('close');
	            }
	    	}]
	    });
	    
	    $("input[name='userFile']").each(function(){
			$(this).filebox({
				buttonText: '打开文件',
				buttonAlign: 'right'
			});
		});
				
	});
</script>

</body>
</html>