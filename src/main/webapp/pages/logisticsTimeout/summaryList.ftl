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
	<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
		<#include "../common/menu.ftl" >
	</div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
            <div data-options="region:'north',title:'<#if orderType?exists && (orderType==1)>换吧网络<#elseif orderType == 2>心动慈露<#elseif orderType==3>心动慈露全球购<#elseif orderType==4>心动慈露
</#if>商家发货时效管理',split:true" style="height: 100px;padding-top: 20px;padding-left: 20px;">
				<form action="${rc.contextPath}/logisticsTimeout/exportResult" method="post" id="searchForm">
					<input value="${startTime!''}" id="searchStartTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
					-
					<input value="${endTime!''}" id="searchEndTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
					<input type="hidden" id="exportType" name="exportType" value="0"/>
					<input type="hidden" name="orderType" value="${orderType!'1'}"/>
					<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;&nbsp;
					<a id="exportResult" onclick="exportResult()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出结果</a>
					<a id="exportTimeoutResult" onclick="exportTimeoutResult()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出该时间段内超时未更新</a>
				</form>
			</div>
			<div data-options="region:'center'" >
			
				<form action="${rc.contextPath}/logisticsTimeout/timeoutOrderList" method="post" id="postForm" target="_blank">
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
			var startTime = $("#searchStartTime").val();
			var endTime = $("#searchEndTime").val();
			if($.trim(startTime) == '' || $.trim(endTime) == ''){
				$.messager.alert('提示','请选择查询日期','info');
				return;
			}else{
				$("input[name='startTime']").each(function(){
					$(this).val(startTime);
				});
				$("input[name='endTime']").each(function(){
					$(this).val(endTime);
				});
				$('#s_data').datagrid('load', {
					startTime : $("#searchStartTime").val(),
					endTime : $("#searchEndTime").val(),
					orderType : ${orderType!'1'}
					
				});
			}
		}


		function exportResult(){
			$('#searchForm').attr("action", "exportResult").submit();
		}
		
		function exportTimeoutResult(){
			$('#searchForm').attr("action", "exportTimeoutResult").submit();
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
				url : '${rc.contextPath}/logisticsTimeout/jsonLogisticsTimeout',
				queryParams: {
					startTime : '${startTime!""}',
					endTime : '${endTime!""}',
					orderType : ${orderType!'1'}
				},
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 50,
				pagination : false,
				rownumbers: true,
				columns : 
				[
				    [
						{field : 'sellerName', title : '商家名称', width : 90, align : 'center'},
						{field : 'totalAmount', title : '发货订单数', width : 90, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.totalAmount)==0){
									return  row.totalAmount;
								}else{
									return '<a href="javascript:;" onclick="viewDetail(' + '\'' + row.sellerId + '\',' + '\'' + row.totalIdList +'\'' + ')">' + row.totalAmount + '</a>';
								}
							}	
						},
						{field : 'ontimeAmount', title : '时效内物流更新', width : 90, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.ontimeAmount) == 0){
									return 	row.ontimeAmount;						
								}else{
									return '<a href="javascript:;" onclick="viewDetail(' + '\'' + row.sellerId + '\',' + '\'' + row.ontimeIdList + '\'' +')">' + row.ontimeAmount + '</a>';
								}
							}	
						},
						{field : 'timeoutUpateAmount', title : '超时物流更新', width : 70, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.timeoutUpateAmount) == 0){
									return 	row.timeoutUpateAmount;								
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