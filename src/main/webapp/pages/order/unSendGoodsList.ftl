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

<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
	
<div data-options="region:'center',title:'content'" style="padding: 5px;">
	<div title="24订单管理" class="easyui-panel" style="padding: 10px">
		<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
			<form action="${rc.contextPath}/order/exportUnSendGoods" id="searchForm" method="post">
				<table>
					<tr>
						<td>
							查询时间：<span id="selectTime">${selectTime}&nbsp;</span>
						</td>
						<td>
							<a id="searchBtn" onclick="searchInfo()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;&nbsp;<a id="searchBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
			
		<div class="selloff_mod">
			<table id="s_data">

			</table>
		</div>
	</div>
	
</div>
	

<script>

function searchInfo() {
	$('#s_data').datagrid('load',{type:1});
}

function exportAll(){
	$('#searchForm').submit();
}

function detail(id, type){
	var urlStr = '${rc.contextPath}/order/unSendGoodsListDetail?id='+id+'&operType='+type;
	window.open(urlStr,'_blank');
}

$(function(){
	
	$('#s_data') .datagrid({
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'id',
		url : '${rc.contextPath}/order/jsonUnSendGoodsList',
		queryParams: {
			type: '0'
		},
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : true,
		pageSize : 50,
		pageList : [ 50, 60 ],
		columns : [ [
			{field : 'sellerName', title : '商家', width : 40, align : 'center'},
			{field : 'responsibilityPerson', title : '招商负责人', width : 20, align : 'center'},
			{field : 'sendAddress', title : '发货地', width : 50, align : 'center'},
			{field : 'warehouse', title : '分仓', width : 30, align : 'center'},
			{field : 'countIds_before_15', title : '今日15点前<br/>付款未发货订单数', width : 30, align : 'center'},
			{field : 'sumTotal_before_15', title : '今日15点前<br/>付款未发货订单金额', width : 40, align : 'center'},
			{field : 'countIds_24_to_48', title : '24小时-48小时<br/>未发货订单数', width : 30, align : 'center'},
			{field : 'sumTotal_24_to_48', title : '24-48小时<br/>未发货订单金额', width : 40, align : 'center'},
			{field : 'countIds_upper_48', title : '超48小时<br/>未发货订单数', width : 30, align : 'center'},
			{field : 'sumTotal_upper_48', title : '超48小时<br/>未发货订单金额', width : 40, align : 'center'},
			{field : 'hidden', title : '操作', width : 50, align : 'center',
				formatter : function(value, row, index) {
					var f1 = '<a href="javaScript:;" onclick="detail(' + row.id + ',1)">今日15点前明细</a> | ';
					var f2 = '<a href="javaScript:;" onclick="detail(' + row.id + ',2)">超24小时明细</a>';
            		return f1+f2;
				}
			} 
		] ],
		pagination : true,
        rownumbers:true
	});  
});
</script>

</body>
</html>