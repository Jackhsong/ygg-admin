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
.searchSpan{
	margin-left: 20px;
	margin-top: 10px;
}
ul a:link{
    text-decoration:none;
}
ul a:hover {
    color：black；
}
ul a:visited {
    color:black;
}
ul a:link {
    color:black;
}
ul a {
    color:black;
}
textarea{
	resize:none
}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'菜单列表',split:true" style="width: 180px;">
	 <#include "../sellerAdmin/menu.ftl" >
	</div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',title:'客服问题登记管理',split:true" style="height: 80px; padding-top: 10px;">
				<form action="" id="searchForm" method="post" >
						<span class="searchSpan">
							订单编号：	<input id="orderNo" name="orderNo" value="" />
						</span>
						<span class="searchSpan">
							顾客问题类型：<input id="templateId" name="templateId" value=""/>
						</span>
						<span class="searchSpan">
							<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</span>
					</form>
			</div>
			<div data-options="region:'center'" >
				<table id="s_data"></table>
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
	
		function searchOrder() {
			$('#s_data').datagrid('load', {
				orderNo : $("#orderNo").val(),
				sellerId : ${sellerId},
				templateId : $("#templateId").combobox('getValue'),
				questionDesc : $("#questionDesc").val()
			});
		}
		$(function() {

			$("#templateId").combobox({
				url:'${rc.contextPath}/orderQuestion/jsonOrderQuestionTemplate',
			    valueField:'code',   
			    textField:'text'
			});
			
			$('#s_data') .datagrid(
			{
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/sellerAdminOrder/jsonQuestionListInfo',
				queryParams: {
					sellerId: '${sellerId}'
				},
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 30,
				columns : [ [
						{field : 'leftTime', title : '反馈时限', width : 20, align : 'left'},
						{field : 'customerDealDetail', title : '顾客问题描述', width : 70, align : 'left'},
						{field : 'sellerDealDetail', title : '商家对接描述', width : 70, align : 'left'},
						{field : 'sellerFeedbackDetail', title : '商家反馈', width : 70, align : 'left'},
						{field : 'templateName', title : '问题类型', width : 35, align : 'center',
							formatter:function(value, row, index){
								var a = '<a target="_blank" href="${rc.contextPath}/sellerAdminOrder/orderQuestionDetail/'+row.questionId+'">'+row.templateName+'</a>';
								return a;
							}	
						},
						{field : 'orderNo', title : '订单编号', width : 35, align : 'center',
							formatter : function(value, row, index) {
								var a = '<a target="_blank" href="${rc.contextPath}/sellerAdminOrder/detail/'+row.orderId+'">'+row.orderNo+'</a>';
								return a;
							}
						},
						{field : 'oStatusDescripton', title : '订单状态', width : 20, align : 'center'},
						{field : 'payTime', title : '付款时间', width : 30, align : 'center'},
						{field : 'sendTime', title : '发货时间', width : 30, align : 'center'},
						{field : 'sellerName', title : '商家', width : 40, align : 'center'},
						{field : 'sendAddress', title : '发货地', width : 30, align : 'center'}
				] ],
				pagination : true,
				rownumbers : true
			});

		});
	</script>

</body>
</html>