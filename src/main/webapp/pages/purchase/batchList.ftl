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
		<input type="hidden" value="${nodes!0}" id="nowNode" /> <#include "../common/menu.ftl" >
	</div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true,title:'批次列表'">
			<input type="hidden" id="providerProductId" value="<#if providerProductId??>${providerProductId}</#if>">
			<div data-options="region:'center'">
				<!--数据表格-->
				<table id="s_data" style=""></table>
			</div>
		</div>
	</div>
	<script>
		$(function() {
			$('#s_data').datagrid({
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/purchase/batchListInfo?providerProductId=' + $('#providerProductId').val(),
				loadMsg : '正在装载数据...',
				singleSelect : false,
				fitColumns : true,
				remoteSort : true,
				pageSize : 50,
				pageList : [ 50, 60 ],
				columns : [ [ {
					field : 'purchaseCode',
					title : '采购单编号',
					width : 50,
					align : 'center'
				}, {
					field : 'barcode',
					title : '商品条码',
					width : 35,
					align : 'center'
				}, {
					field : 'name',
					title : '商品名称',
					width : 70,
					align : 'center'
				}, {
					field : 'purchaseQuantity',
					title : '采购数',
					width : 20,
					align : 'center'
				}, {
					field : 'storingCount',
					title : '入库数',
					width : 20,
					align : 'center'
				}, {
					field : 'providerPrice',
					title : '供货单价',
					width : 25,
					align : 'center',
					formatter : function(value, row, index) {
						return Number(row.providerPrice).toFixed(2);
					}
				}, {
					field : 'totalPrice',
					title : '合计金额',
					width : 25,
					align : 'center',
					formatter : function(value, row, index) {
						return Number(row.totalPrice).toFixed(2);
					}
				}, {
					field : 'shareFreightMoney',
					title : '分摊运费',
					width : 25,
					align : 'center',
					formatter : function(value, row, index) {
						return Number(row.shareFreightMoney).toFixed(2);
					}
				}, {
					field : 'providerPriceWithFreight',
					title : '含运费单价',
					width : 30,
					align : 'center',
					formatter : function(value, row, index) {
						return Number(row.providerPriceWithFreight).toFixed(2);
					}
				}, {
					field : 'providerPriceRMBWithFreight',
					title : '含运费单价/RMB',
					width : 35,
					align : 'center',
					formatter : function(value, row, index) {
						return Number(row.providerPriceRMBWithFreight).toFixed(2);
					}
				}, {
					field : 'manufacturerDate',
					title : '生产日期',
					width : 30,
					align : 'center'
				}, {
					field : 'durabilityPeriod',
					title : '保质期',
					width : 30,
					align : 'center'
				}, {
					field : 'remark',
					title : '采购备注',
					width : 30,
					align : 'center'
				}, {
					field : 'storingTime',
					title : '入库时间',
					width : 45,
					align : 'center'
				}, {
					field : 'storingRemark',
					title : '入库备注',
					width : 30,
					align : 'center'
				}, {
					field : 'hidden',
					title : '操作',
					width : 15,
					align : 'center',
					formatter : function(value, row, index) {
						return '<a href="javascript:void(0);" onclick="detail(' + row.purchaseCode + ',' + row.storageId + ',' + row.providerId + ')">明细</a>';
					}
				} ] ],
				pagination : true
			});
		});
		function detail(purchaseCode, storageId, providerId) {
			window.open('${rc.contextPath}/purchase/toOrderDetail?storageId=' + storageId + '&providerId=' + providerId + '&purchaseCode=' + purchaseCode);
		}
	</script>

</body>
</html>