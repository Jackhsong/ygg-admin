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
		<div data-options="region:'center'" >
			<!--数据表格-->
			<table id="s_data" ></table>
			
			<!-- 销售天数打折配置 -->
			<div id="editSaleTimeDiv" style="width:500px;height:300px;padding:10px 10px;">
				<table id="sale_time" ></table>
			</div>
					
		</div>
	</div>
</div>

<script>
	
	/**	调整人工干预因子  begin*/
	function editCustomFactor(index){
		$('#s_data').datagrid('beginEdit', index);
	};

	function updateCustomFactorAction(){
		var rowcount = $('#s_data').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_data').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}

	function saveCustomFactor(index){
		$('#s_data').datagrid('endEdit', index);
	};

	function cancelCustomFactor(index){
		$('#s_data').datagrid('cancelEdit', index);
	};

	function updateCustomFactorActivity(row){
		$.ajax({
			url: '${rc.contextPath}/mallWindow/updateHotProductCustomFactor',
			type:"POST",
			data: {id:row.id,customFactor:row.customFactor},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error',function(){
		                return
		            });
		        }
			}
		});
	};
	/**	调整人工干预因子  end*/
	

	/**	调整折扣  begin*/
	function editSaleTimeFactor(index){
		$('#sale_time').datagrid('beginEdit', index);
	};

	function updateSaleTimeFactorAction(){
		var rowcount = $('#sale_time').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#sale_time').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}

	function saveSaleTimeFactor(index){
		$('#sale_time').datagrid('endEdit', index);
	};

	function cancelSaleTimeFactor(index){
		$('#sale_time').datagrid('cancelEdit', index);
	};

	function updateSaleTimeFactorActivity(row){
		$.ajax({
			url: '${rc.contextPath}/mallWindow/updateSaleTimeFactor',
			type:"POST",
			data: {index:row.index,factor:row.factor},
			success: function(data) {
				if(data.status == 1){
					$('#sale_time').datagrid('load');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error',function(){
		                return
		            });
		        }
			}
		});
	};
	/**	调整折扣  end*/
	
	
	$(function(){

		<!--商品列表begin-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/mallWindow/jsonTodayHotDisplayProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:30,
            pageList:[30,40],
            columns:[[
                {field:'type',    title:'商品类型', width:30, align:'center'},
            	{field:'productId',    title:'商品Id', width:20, align:'center'},
            	{field:'productName',    title:'商品名称', width:70, align:'center'},
                {field:'saleTime',    title:'销售小时数', width:30, align:'center'},
                {field:'salesPrice',    title:'单价', width:20,align:'center'},
                {field:'saleCount',    title:'销量', width:20, align:'center'},
                {field:'saleMoney',    title:'销售额', width:20,align:'center'},
                {field:'saleCountDiscount',    title:'打折销量', width:20,align:'center'},
                {field:'saleMoneyDiscount',    title:'打折销售额', width:20,align:'center'},
                {field:'saleAmountFactor',    title:'销量权重', width:20,align:'center'},
                {field:'saleMoneyFactor',    title:'销售额权重', width:20,align:'center'},
                {field:'totalFactor',    title:'合计权重', width:20,align:'center'},
                {field:'customFactor',    title:'人工干预', width:20,align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'sequence',    title:'最终权重', width:20,align:'center'},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saveCustomFactor('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelCustomFactor('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		return '<a href="javascript:;" onclick="editCustomFactor(' + index + ')">修改人工干预值</a>';
                    	}
                    }
                }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateCustomFactorAction();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateCustomFactorAction();
            	updateCustomFactorActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateCustomFactorAction();
        	},
        	toolbar:[{
                id:'_add',
                text:'销售天数打折配置',
                iconCls:'icon-edit',
                handler:function(){
                	$('#editSaleTimeDiv').dialog('open');
                }
            }],
            pagination:true,
            rownumbers:true
        });
		<!--商品列表end-->
		
		<!--销售天数打折列表Begin-->
        $('#sale_time').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'index',
            url:'${rc.contextPath}/mallWindow/jsonSaleTimeDiscount',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            columns:[[
            	{field:'type',    title:'类型', width:50,},
            	{field:'time',    title:'上架小时数', width:50, align:'center'},
            	{field:'factor',    title:'折扣', width:50, align:'center',editor:{type:'validatebox',options:{required:true}}},
            	{field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saveSaleTimeFactor('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelSaleTimeFactor('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		return '<a href="javascript:;" onclick="editSaleTimeFactor(' + index + ')">修改折扣</a>';
                    	}
                    }
                }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateSaleTimeFactorAction();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateSaleTimeFactorAction();
            	updateSaleTimeFactorActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateSaleTimeFactorAction();
        	},
            rownumbers:true
        });
		<!--销售天数打折列表End-->
		
		$('#editSaleTimeDiv').dialog({
    		title:'销售天数打折配置',
    		collapsible:true,
    		closed:true,
    		modal:true,
    		buttons:[{
                text:'关闭',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editSaleTimeDiv').dialog('close');
                }
            }]
		});
		
});
</script>

</body>
</html>