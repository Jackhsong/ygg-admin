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
	width:1340px;
	align:center;
}
td,input,select{
	font-size:14px;
}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',title:'条件筛选-订单管理',split:true" style="height: 120px;">
				<form action="${rc.contextPath}/order/exportOrderFreezeList" id="searchForm" method="post" >
					<table class="search" cellpadding="10px" cellspacing="0px">
						<tr>
							<td >
								冻结状态：
								<select id="status" name="status" style="width:173px;">
									<option value="-1">全部</option>
									<option value="1">冻结未处理</option>
									<option value="2">已解冻</option>
									<option value="3">永久冻结</option>
								</select>
							</td>	
						</tr>
						<tr>
							<td >
								<a id="searchBtn" onclick="searchData()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a>	&nbsp;&nbsp;						
								<a id="exportBtn" onclick="exportData()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出结果</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div data-options="region:'center'" >
				<table id="s_data">

				</table>
				
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

	function searchData() {
		$('#s_data').datagrid('load', {
			status : $("#status").val()
		});
	}
	
	function exportData() {
		$("#searchForm").submit();
	}
		$(function() {
			$('#s_data') .datagrid(
			{
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/order/jsonOrderFreezeInfo',
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 50,
				pageList : [ 50, 100 ],
				columns : [ [
						{field : 'freezeStatusStr', title : '冻结状态', width : 30, align : 'center'},
						{field : 'payTime', title : '付款时间', width : 60, align : 'center'},
						{field : 'freezeTime', title : '冻结时间', width : 60, align : 'center'},
						{field : 'unfreezeTime', title : '解冻时间', width : 60, align : 'center'},
						{field : 'number', title : '订单编号', width : 45, align : 'center',
							formatter : function(value, row, index) {
								var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.orderId+'">'+row.number+'</a>';
								return a;
							}
						},
						{field : 'orderStatusStr', title : '订单状态', width : 30, align : 'center'},
						{field : 'totalPrice', title : '订单总价', width : 30, align : 'center'},
						{field : 'realPrice', title : '实付金额', width : 30, align : 'center'},
						{field : 'sellerName', title : '商家', width : 40, align : 'center'},
						{field : 'sendAddress', title : '发货地', width : 40, align : 'center'}] ],
				pagination : true
			});
		})
	</script>

</body>
</html>