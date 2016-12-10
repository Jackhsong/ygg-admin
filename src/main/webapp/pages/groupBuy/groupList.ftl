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
		<div data-options="region:'north',title:'团购商品统计',split:true" style="height:100px;padding-top:10px">
			<form id="searchForm" action="" method="post" >
	        	<tr>
	        		<td class="searchName">商品ID：</td>
					<td class="searchText"><input id="searchId" name="searchId" value="" /></td>
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
    	id:$("#searchId").val(),
    	name:$("#searchTitle").val()
	});
};
$(function(){
	$('#s_data').datagrid({
	    nowrap: false,
	    striped: true,
	    collapsible:true,
	    idField:'id',
	    url:'${rc.contextPath}/group/jsonGroup',
	    loadMsg:'正在装载数据...',
	    fitColumns:true,
	    remoteSort: true,
	    singleSelect:true,
	    pageSize:20,
	    pageList:[20,40],
	    columns:[[
	        {field:'productId',    title:'商品ID', width:30, align:'center'},
	        {field:'productType',    title:'商品类型', width:20, align:'center'},
	        {field:'productName',    title:'长名称', width:40, align:'center'},
	        {field:'salesPrice',    title:'售价', width:40, align:'center'},
	        {field:'groupPirce',    title:'团购价', width:30, align:'center'},
	        {field:'total',    title:'团购数', width:30, align:'center'}, 
	        {field:'groupSuccessNums',     title:'发起成功的团购',  width:40,   align:'center' },
	        {field:'groupTotalPeople',     title:'参与人数',  width:40,   align:'center' },
	        {field:'groupSuccessPeople',     title:'成功团购的人数',  width:40,   align:'center' },
	        {field:'groupOrderNums',     title:'成功下单人数',  width:40,   align:'center' },
	        {field:'groupBuyNums',     title:'实际购买人数',  width:40,   align:'center' }
	    ]],
	    pagination:true
	});
});
</script>

</body>
</html>