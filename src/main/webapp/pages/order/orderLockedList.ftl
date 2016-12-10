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
			<div data-options="region:'north',title:'条件筛选-锁定订单',split:true" style="height: 150px;">
				<form action="" id="searchForm" method="post" >
						<table class="search">
							<tr>
								<td class="searchName">订单状态：</td>
								<td class="searchText">
									<select id="orderStatus" name="orderStatus" style="width: 170px;">
										<option value="2">待发货</option>
										<option value="0">全部</option>
										<option value="3">已发货</option>
										<option value="4">交易成功</option>
										<option value="5">用户取消</option>
										<option value="6">超时取消</option>
									</select>
								</td>
								<td class="searchName">审核状态：</td>
								<td class="searchText">
                                    <select id="checkStatus" name="checkStatus" style="width: 170px;">
                                        <#--<option value="0">全部</option>-->
                                        <option value="1">待审核</option>
                                        <option value="2">审核通过</option>
                                        <option value="3">审核不通过</option>
                                    </select>
								</td>
							</tr>
							<tr>
								<td class="searchName">订单编号：</td>
								<td class="searchText">
									<input id="orderNumber" name="orderNumber" value="" />
								</td>
                                <td class="searchName">用户ID：</td>
                                <td class="searchText"><input id="accountId" name="accountId" value="${accountId!''}" /></td>
							</tr>
							<tr>
								<td class="searchName">收货人姓名：</td>
								<td class="searchText"><input id="receiveName" name="receiveName" value="" /></td>
                                <td class="searchName">收货手机号：</td>
                                <td class="searchText"><input id="reveivePhone" name="reveivePhone" value="" /></td>
							</tr>
							<tr>
								<td class="searchName"></td>
								<td class="searchText" style="padding-top: 5px">
									<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
								</td>
								<td class="searchName"></td>
								<td class="searchText"></td>
							</tr>
						</table>
					</form>
			</div>
			<div data-options="region:'center'" >
				<table id="s_data"> </table>
				
				<!-- 审单div -->
				<div id="checkOrderDiv" class="easyui-dialog" style="width:280px;height:150px;padding:20px 20px;">
	            	<form id="checkOrderForm" method="post">
	            		<input id="orderId" type="hidden" name="orderId" />
	            		<input type="radio" id="sendType1" name="sendType" value="2" checked /> 审单通过
	            		<input type="radio" id="sendType0" name="sendType" value="3"/> 审单不通过
	        		</form>
	        	</div>

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
				orderNumber : $("#orderNumber").val(),
				orderStatus : $("#orderStatus").val(),
                checkStatus : $("#checkStatus").val(),
				accountName : $("#accountName").val(),
				accountId : $("#accountId").val(),
				receiveName : $("#receiveName").val(),
				reveivePhone : $("#reveivePhone").val()
			});
		}
		
		function checkOrder(index) {
			$("#sendType1").prop("checked", true);
			var arr=$("#s_data").datagrid("getData");
			$('#orderId').val(arr.rows[index].id);
			$('#checkOrderDiv').dialog('open');
		}
		

		function toIntegralDetail(id){
    		window.open("${rc.contextPath}/account/integralRecordList/"+id,"_blank");
		}

		$(function() {
			
			$('#checkOrderDiv').dialog({
            title:'订单审核',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $.messager.progress();
					var id = $('#orderId').val();
                    var checkStatus= $("input[name='sendType']:checked").val();
                    $.ajax({
                        url: '${rc.contextPath}/order/checkOrder',
                        type: 'post',
                        dataType: 'json',
                        data: {'id':id,'checkStatus':checkStatus},
                        success: function(data){
                            $.messager.progress('close');
                            if(data.status == 1){
                                $('#s_data').datagrid('reload');
                                $('#checkOrderDiv').dialog('close');
                            }else{
                                $.messager.alert("提示",data.msg,"info");
                            }
                        },
                        error: function(xhr){
                            $.messager.progress('close');
                            $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                        }
                    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#checkOrderDiv').dialog('close');
                }
            }]
        })
		
		
	$('#s_data') .datagrid(
	{
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'id',
		url : '${rc.contextPath}/order/jsonOrderLockedInfo',
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : true,
		pageSize : 50,
		pageList : [ 50, 100 ],
		columns : [ [
				{field : 'payTime', title : '付款时间', width : 90, align : 'center'},
				{field : 'checkTime', title : '审单时间', width : 90, align : 'center'},
				{field : 'checkStatusDesc', title : '审单状态', width : 50, align : 'center'},
				{field : 'number', title : '订单编号', width : 70, align : 'center',
					formatter : function(value, row, index) {
						var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
						return a;
					}
				},
				{field : 'oStatusDescripton', title : '订单状态', width : 50, align : 'center'},
				{field : 'totalPrice', title : '订单总价', width : 40, align : 'center'},
				{field : 'realPrice', title : '实付金额', width : 40, align : 'center'},
				{field : 'raFullName', title : '收货人', width : 40, align : 'center'},
				{field : 'raMobileNumber', title : '收货手机', width : 60, align : 'center'},
				{field : 'sSellerName', title : '商家', width : 70, align : 'center'},
				{field : 'hidden', title : '操作', width : 80, align : 'center',
					formatter : function(value, row, index) {
						var a = '<a href="javaScript:;" onclick="toIntegralDetail(' + row.accountId + ')">查看积分明细</a>';
						var b = '';
						if(row.checkStatus == 1){
							b = '<a href="javaScript:;" onclick="checkOrder(' + index + ')"> | 审单</a>';
						}
						return a + b;
					}
				} ] ],
		pagination : true
	});

})
	</script>

</body>
</html>