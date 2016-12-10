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
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',title:'条件筛选-订单管理',split:true" style="height: 80px;">
				<form action="${rc.contextPath}/order/export" id="searchForm" method="post" >
						<table class="search">
							<tr>
								<td class="searchName">订单状态：</td>
								<td class="searchText">
									<select id="orderStatus" name="orderStatus" style="width: 170px;">
										<option value="0">全部</option>
										<option value="1">未付款</option>
										<option value="2">待发货</option>
										<option value="3">已发货</option>
										<option value="4">交易成功</option>
										<option value="5">用户取消</option>
										<option value="6">超时取消</option>
									</select>
								</td>
								<td class="searchName">订单编号：</td>
								<td class="searchText">
									<input id="orderNumber" name="orderNumber" value="" />
								</td>
								<td class="searchName">用户ID：</td>
								<td class="searchText"><input id="accountId" name="accountId" value="${accountId!''}" /></td>
								<td class="searchText" style="padding-top: 5px">
									<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;						
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
		function searchOrder() {
			$('#s_data').datagrid('load', {
				orderNumber : $("#orderNumber").val(),
				orderStatus : $("#orderStatus").val(),
				accountId : $("#accountId").val()
			});
		}
		$(function() {
			$(document).keydown(function(e){
				if (!e){
				   e = window.event;  
				}  
				if ((e.keyCode || e.which) == 13) {  
				      $("#searchBtn").click();  
				}
			});
			
			$('#s_data').datagrid({
				nowrap: false,
	            striped: true,
	            collapsible:true,
	            idField:'id',
	            url:'${rc.contextPath}/qqbsOrder/orderList',
	            loadMsg:'正在装载数据...',
	            singleSelect:true,
	            fitColumns:true,
	            remoteSort: true,
	            pageSize:50,
	            pageList:[50,60],
	            columns:[[
{field : 'createTime', title : '下单时间', width : 35, align : 'center'},
{field : 'payTime', title : '付款时间', width : 35, align : 'center'},
{field : 'sendTime', title : '发货时间', width : 35, align : 'center'},
{field : 'number', title : '订单编号', width : 30, align : 'center'},
{field : 'status', title : '订单状态', width : 20, align : 'center',
	formatter:function(value,row,index){
		if(row.status == 1)
			return '未付款';
		if(row.status == 2)
			return '待发货';
		if(row.status == 3)
			return '已发货';
		if(row.status == 4)
			return '交易成功';
		if(row.status == 5)
			return '用户取消';
		if(row.status == 6)
			return '超时取消';
	}
},
{field : 'totalPrice', title : '订单总价', width : 20, align : 'center'},
{field : 'realPrice', title : '实付金额', width : 20, align : 'center'},
{field : 'accountId', title : '用户ID', width : 20, align : 'center'},
{field : 'name', title : '用户名', width : 40, align : 'center'},
{field : 'nickname', title : '昵称', width : 30, align : 'center'},
{field : 'fullName', title : '收货人', width : 30, align : 'center'},
{field : 'mobileNumber', title : '收货手机', width : 30, align : 'center'},
	            	] ],
				pagination : true
			});
		})
	</script>

</body>
</html>