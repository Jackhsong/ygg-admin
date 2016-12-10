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
			<div data-options="region:'north',title:'条件筛选-假单管理',split:true" style="height: 150px;">
				<form id="searchForm" method="post" >
						<table class="search">
							<tr>
								<td class="searchName">订单编号：</td>
								<td class="searchText">
									<input type="text" name="searchOrderNo" id="searchOrderNo" value=""/>
								</td>
			 					<td class="searchName">物流渠道：</td>
			 					<td class="searchText">
									<input type="text" name="searchchannel" id="searchchannel" value=""/></td>
								</td>
			 					<td class="searchName">物流编号：</td>
			 					<td class="searchText">
									<input type="text" name="searchNumber" id="searchNumber" value=""/></td>
								</td>
							</tr>
							<tr>
								<td class="searchName">商家名称：</td>
								<td class="searchText">
									<input type="text" name="searchSellerId" id="searchSellerId" value=""/>
								</td>
			 					<td class="searchName">发货类型：</td>
			 					<td class="searchText">
									<input type="text" name="searchSellerType" id="searchSellerType" value=""/></td>
								</td>
			 					<td class="searchName">订阅次数：</td>
			 					<td class="searchText">
									<input type="text" name="searchOrderCount" id="searchOrderCount" value=""/></td>
								</td>
							<tr>
								<td class="searchName">发货天数：</td>
								<td class="searchText">
									<input type="text" name="searchDays" id="searchDays" value=""/>
								</td>
								<td class="searchText" style="padding-top: 5px">
									<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>	&nbsp;&nbsp;						
									<a id="clearSearch" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">重置</a>&nbsp;&nbsp;						
								<td></td>
								<td></td>
								<td></td>
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
	
		function searchOrder() {
			$('#s_data').datagrid('load', {
				orderNumber : $("#searchOrderNo").val(),
				channelType : $("input[name='searchchannel']").val(),
				channelNumber : $("#searchNumber").val(),
				sellerId : $("input[name='searchSellerId']").val(),
				sellerType : $("input[name='searchSellerType']").val(),
				orderCount : $("#searchOrderCount").val(),
				days:$("#searchDays").val()
			});
		}
		
		function clearSearch(){
			$("#searchOrderNo").val('');
			$("#searchchannel").combobox('clear');
			$("#searchNumber").val('');
			$("#searchSellerId").combobox('clear');
			$("#searchSellerType").combobox('clear');
			$("#searchOrderCount").val('');
			$("#searchDays").val('');
			$('#s_data').datagrid('load', {});
		}

		$(function() {
			$('#searchSellerId').combobox({   
			    url:'${rc.contextPath}/seller/jsonSellerCode?isAvailable=1',
			    valueField:'code',   
			    textField:'text'
			});
			
			
			$('#searchchannel').combobox({   
			    url:'${rc.contextPath}/order/jsonCompanyCode',   
			    valueField:'code',   
			    textField:'text'  
			});
			
			$('#searchSellerType').combobox({
			    url:'${rc.contextPath}/seller/jsonSellerType',   
			    valueField:'code',   
			    textField:'text'  
			});
		
		
			$('#s_data') .datagrid(
			{
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/order/fakeOrderJsonInfo',
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : false,
				pageSize : 50,
				pageList : [ 50, 60 ],
				columns : [ [
						{field:'id',    title:'序号', align:'center',checkbox:true},
						{field : 'orderNumber', title : '订单编号', width : 90, align : 'center',
							formatter : function(value, row, index) {
								var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.orderId+'">'+row.orderNumber+'</a>';
								return a;
							}
						},
						{field : 'sellerName', title : '商家名称', width : 90, align : 'center'},
						{field : 'sellerType', title : '发货类型', width : 90, align : 'center'},
						{field : 'sendAddress', title : '发货地', width : 90, align : 'center'},
						{field : 'channelType', title : '物流渠道', width : 90, align : 'center'},
						{field : 'channelNumber', title : '物流编号', width : 80, align : 'center'},
						{field : 'orderCount', title : '订阅次数', width : 80, align : 'center'},
						{field : 'isFakeOrder', title : '是否假单', width : 70, align : 'center'},
						{field : 'days', title : '发货天数', width : 40, align : 'center'},
						{field : 'hidden', title : '操作', width : 80, align : 'center',
							formatter : function(value, row, index) {
								if(row.isFakeOrder=='是'){
									return '<a href="javaScript:;" onclick="orderAgain(' + row.id + ')">重新订阅</a>';
								}else{
									return '-';
								}
							}
						} ] ],
						toolbar:[{
					                iconCls: 'icon-edit',
					                text:'批量重新订阅',
					                handler: function(){
					                    var rows = $('#s_data').datagrid("getSelections");
					                    if(rows.length > 0){
					                        $.messager.confirm('确认','确定重新订阅吗？',function(b){
					                            if(b){
					                                var ids = [];
					                                for(var i=0;i<rows.length;i++){
					                                    ids.push(rows[i].id)
					                                }
					                                $.post("${rc.contextPath}/order/orderLogisticsAgain",
													{ids: ids.join(",")},
													function(data){
														if(data.status==1){
															$.messager.alert('提示','订阅成功','info');
															$('#s_data').datagrid('reload');
														}else{
															$.messager.alert('提示',data.msg,'info');
														}
													},
													"json");
					                            }
					                 		})
					                    }else{
					                        $.messager.alert('提示','请选择要操作的类别',"error")
					                    }
					                }
						       }],
				pagination : true,
				rownumbers : true
			});
			
		});
		
		function orderAgain(id){
			$.messager.confirm('确认', '确定重新订阅吗？', function(r){
				if (r){
					$.post(
						"${rc.contextPath}/order/orderLogisticsAgain",
						{id:id},
						function(data){
							if(data.status==1){
								$.messager.alert('提示','订阅成功','info');
								$('#s_data').datagrid('reload');
							}else{
								$.messager.alert('提示',data.msg,'info');
							}
						},
						'json');
				}
			});
		}
	</script>

</body>
</html>