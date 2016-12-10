<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<link
	href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>

</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center',title:'content'" style="padding: 5px;">
	
		<div class="selloff_mod">
			<table id="s_data">

			</table>
		</div>
	</div>

	<script>
		$(function (){
			$('#s_data') .datagrid({
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/order/jsonUpper24OrderInfo',
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 50,
				pageList : [ 50, 60 ],
				columns : [ [
						{field : 'oCreateTime', title : '下单时间', width : 90, align : 'center'},
						{field : 'oPayTime', title : '付款时间', width : 90, align : 'center'},
						{field : 'oSendTime', title : '发货时间', width : 90, align : 'center'},
						{field : 'orderChannel', title : '订单来源', width : 80, align : 'center'},
						{field : 'number', title : '订单编号', width : 70, align : 'center',
							formatter : function(value, row, index) {
								var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
								return a;
							}
						},
						{field : 'oStatusDescripton', title : '订单状态', width : 50, align : 'center'},
						{field : 'oTotalPrice', title : '订单总价', width : 40, align : 'center'},
						{field : 'raFullName', title : '收货人', width : 50, align : 'center'},
						{field : 'raMobileNumber', title : '收货手机', width : 60, align : 'center'},
						{field : 'sSellerName', title : '商家', width : 70, align : 'center'},
						{field : 'sSendAddress', title : '发货地', width : 70, align : 'center'},
						{field : 'operaStatus', title : '是否导出', width : 40, align : 'center'},
						{field : 'ologChannel', title : '物流公司', width : 60, align : 'center'},
						{field : 'ologNumber', title : '运单号', width : 70, align : 'center'},
						{field : 'hidden', title : '操作', width : 80, align : 'center',
							formatter : function(value, row, index) {
								var a = '';
								if(row.oStatus == 2){
									a = '<a href="javaScript:;" onclick="sendOrder(' + index + ')">发货</a>';
								}else if(row.oStatus == 3){
									a = '<a href="javaScript:;" onclick="editSendOrder(' + index + ')">修改发货信息</a>';
								}
								return a;
							}
						} ] ],
				pagination : true,
				rownumbers : true
			}); 
		});
	</script>

</body>
</html>