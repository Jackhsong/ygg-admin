<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
</head>
<body>

<div id="addRole_div">
	 <p>
		商品名称：<span>${name!""}</span>&nbsp;&nbsp;
		采购商品条码：<span>${barcode!""}</span>&nbsp;&nbsp;
		入库仓库：<span>${storageName!""}</span>&nbsp;&nbsp;
		采购商品待入库库存：<span id=""></span>&nbsp;&nbsp;
	</p>
	<p>
		采购商品未分配库存：<span></span>&nbsp;&nbsp;
		各渠道剩余库存总和：<span>${remainStock!""}</span>&nbsp;&nbsp;
		仓库实时实际可用库存：<span></span>&nbsp;&nbsp;
	</p>

   	<table id="s_saleData" ></table>
</div>
<script>

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
		if(parseInt(row.type) == 1){
			updateProductBaseStock(row.addStock,row.productId,row.providerProductId,row.groupCount);
		}else if(parseInt(row.type) == 2){
			updateProductThirdPartyStock(row.addStock,row.productId,row.providerProductId,row.groupCount);
		}
	};

	function updateProductBaseStock(stock,productId,providerProductId,groupCount){
		$.ajax({
			url:'${rc.contextPath}/productBase/updateProductBaseAvailableStock',
			type:'post',
			data:{'stock':stock,'id':productId,'providerProductId':providerProductId,'groupCount':groupCount},
			dataType:'json',
			success:function(data){
				if(data.status==1){
					$.messager.alert("提示", '保存成功', "info",function(){
						$('#s_saleData').datagrid('reload');
					});
				}else{
					$.messager.alert("提示", data.msg, "error");
				}
			},
			error:function(xhr){
				$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
			}
		});
	}
	
	function updateProductThirdPartyStock(stock,productId,providerProductId,groupCount){
		$.ajax({
			url:'${rc.contextPath}/thirdProduct/updateThirdPartyProductStock',
			type:'post',
			data:{'stock':stock,'id':productId,'providerProductId':providerProductId,'groupCount':groupCount},
			dataType:'json',
			success:function(data){
				if(data.status==1){
					$.messager.alert("提示", '保存成功', "info",function(){
						$('#s_saleData').datagrid('reload');
					});
				}else{
					$.messager.alert("提示", data.msg, "error");
				}
			},
			error:function(xhr){
				$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
			}
		});
	}
    $(function(){
		$('#s_saleData').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/purchase/jsonProductInfo',
            loadMsg:'正在装载数据...',
            queryParams:{
            	id:${id!"0"}
            },
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:30,
            columns:[[
            	{field:'channelName',    		 title:'渠道名', width:25, align:'center'},
            	{field:'productId',      title:'基本/第三方<br/>商品ID', width:20, align:'center'},
                {field:'productCode',    title:'商品编码', width:40, align:'center'},
                {field:'productName',    title:'商品名称', width:60, align:'center'},
                {field:'groupCount',     title:'组合销<br/>售件数', width:15, align:'center'},
                {field:'totalSales',     title:'累计销量', width:15, align:'center'},
                {field:'remainStock',    title:'剩余库存', width:15,align:'center'},
                {field:'availableStock', title:'未分配库存', width:20,align:'center'},
                {field:'addStock',    	 title:'调库存', width:15,align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  		 title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editrow(' + index + ')">调库存</a>';
                        	return a;
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
    });
    
    
</script>
</body>
</html>