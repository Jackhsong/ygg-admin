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
		<div data-options="region:'north',title:'笨鸟库存列表',split:true">
	        <form id="searchForm" action="${rc.contextPath}/birdex/exportResult" method="post" ></form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
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
			number : $("#orderNumber").val(),
			sendStatus : $("#orderStatus").val()
		});
	}
	
	function refreshStock(){
        $.ajax({
          	url:'${rc.contextPath}/birdex/refreshBirdexStock',
          	type:'post',
          	dataType:'json',
          	success:function(data){
          		$.messager.progress('close');
				if(data.status == 1){
					$.messager.alert("提示","刷新成功","error",function(){
						$('#s_data').datagrid('reload');
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
	
	function exportStock(){
		$("#searchForm").submit();
	}

	$(function(){
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        idField:'id',
	        url:'${rc.contextPath}/birdex/jsonBirdexStockInfo',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        singleSelect:true,
	        pageSize:50,
	        columns:[[
	            {field :'warehouse', title : '仓库', width : 20, align : 'center'},
	            {field :'code', title : '商品编码', width : 30, align : 'center'},
				{field :'actualStock', title : '实际库存', width : 20, align : 'center'},
				{field :'availableStock', title : '可用库存', width : 20, align : 'center'},
				{field :'providerProductId', title : '采购商品ID', width : 20, align : 'center'},
	            {field :'providerProductName',    title:'采购商品名称', width:60, align:'center'},
	        ]],
	        toolbar:[{
                id:'_refresh',
                text:'刷新',
                iconCls: 'icon-reload',
                handler:function(){
                	refreshStock();
                }
            },'-',{
            	 id:'_export',
                 text:'导出列表',
                 iconCls: 'icon-print',
                 handler: function(){
                	 exportStock();
                 }
            }],
	        pagination:true
	    });
		
	});
</script>
</body>
</html>