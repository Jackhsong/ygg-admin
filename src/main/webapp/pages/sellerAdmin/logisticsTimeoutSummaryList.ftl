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

.spanStyle{
	margin-left: 15px;
	margin-right: 10px;
	margin-top: 20px;
}
a{
	text-decoration: none;
}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'菜单列表',split:true" style="width: 180px;">
	 <#include "../sellerAdmin/menu.ftl" >
	</div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',title:'物流更新时效',split:true" style="height: 100px;padding-top: 20px;">
				<input type="hidden" name="sellerId" id="sellerId" value="${sellerId}"/>
				<span class="spanStyle">商家名称：${sellerName!""}</span>
				<span class="spanStyle">发货类型：${sellerType!""}</span>
				<span class="spanStyle">报关顺序：${bonedType!""}</span>
				<span class="spanStyle">超时罚款：每单50元</span>
				<span class="spanStyle">查询时期：<input value="${selectDate!''}" id="selectDate" name="selectDate" onClick="WdatePicker({dateFmt: 'yyyy-MM',minDate:'2015-03'})"/></span>
				<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
			</div>
			<div data-options="region:'center'" >
			
				<form action="${rc.contextPath}/sellerAdminOrder/logisticsTimeoutOrderList" method="post" id="postForm" target="_blank">
					<input type="hidden" name="startTime" id="post_startTime" value="${startTime!''}"/>
					<input type="hidden" name="endTime" id="post_endTime" value="${endTime!''}"/>
					<input type="hidden" name="sellerId" id="post_sellerId" value=""/>
					<input type="hidden" name="orderIdList" id="post_orderIdList" value=""/>
				</form>
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
				sellerId : ${sellerId!"0"},
				selectDate : $("#selectDate").val()
			});
		}

		function viewDetail(sellerId,orderIdList){
			$("#post_sellerId").val(sellerId);
			$("#post_orderIdList").val(orderIdList);
			$("#postForm").submit();
		}

		$(function() {
		
			$('#s_data') .datagrid(
			{
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/sellerAdminOrder/jsonLogisticsTimeoutSummaryInfo',
				queryParams: {
                    sellerId: '${sellerId!"0"}',
                    selectDate:'${selectDate!""}'
				},
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 50,
				pagination : false,
				columns : 
				[
				    [
						{field : 'date', title : '日期', width : 90, align : 'center'},
						{field : 'totalAmount', title : '当日订单个数', width : 90, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.totalAmount) == 0){
									return row.totalAmount;							
								}else{
									return '<a href="javascript:;" onclick="viewDetail(' + '\'' + row.sellerId + '\',' + '\'' + row.totalIdList +'\'' + ')">' + row.totalAmount + '</a>';								
								}
							}	
						},
						{field : 'ontimeAmount', title : '时效内物流更新', width : 90, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.ontimeAmount) == 0){
									return row.ontimeAmount;
								}else{
									return '<a href="javascript:;" onclick="viewDetail(' + '\'' + row.sellerId + '\',' + '\'' + row.ontimeIdList + '\'' +')">' + row.ontimeAmount + '</a>';
								}
							}	
						},
						{field : 'timeoutUpateAmount', title : '超时物流更新', width : 70, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.timeoutUpateAmount) == 0){
									return row.timeoutUpateAmount;
								}else{
									return '<a href="javascript:;" onclick="viewDetail(' + '\'' + row.sellerId + '\',' + '\'' + row.timeoutUpateIdList +'\'' + ')">' + row.timeoutUpateAmount + '</a>';
								}
							}	
						},
						{field : 'timeoutNoUpdateAmount', title : '超时未更新', width : 50, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.timeoutNoUpdateAmount) == 0){
									return row.timeoutNoUpdateAmount;	
								}else{
									return '<a href="javascript:;" onclick="viewDetail(' + '\'' + row.sellerId + '\',' + '\'' + row.timeoutNoUpdateIdList +'\'' + ')">' + row.timeoutNoUpdateAmount + '</a>';
								}
							}	
						},
						{field : 'notUpdateAmount', title : '总体未更新', width : 50, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.notUpdateAmount) == 0){
									return row.notUpdateAmount;
								}else{
									return '<a href="javascript:;" onclick="viewDetail(' + '\'' + row.sellerId + '\',' + '\'' + row.notUpdateIdList +'\'' + ')">' + row.notUpdateAmount + '</a>';
								}
							}	
						},
						{field : 'amerceAmount', title : '超时罚款/元', width : 40, align : 'center'}
					] 
				]
			});
		})
	</script>

</body>
</html>