<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>订单结算</title>
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

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">

	<div id="cc" class="easyui-layout" data-options="fit:true" >
	
		<div data-options="region:'north',title:'条件筛选-订单结算',split:true" style="height: 150px;">
			<form id="searchForm" method="post" action="${rc.contextPath}/finance/exportOrderFinanceData" >
            <table cellspacing="5" cellpadding="5">
            	<tr>
            		<td class="searchName">订单类型：</td>
                    <td class="searchText">
	                    <input type="checkbox" name="orderType" value="1" checked />正常订单
	                    <input type="checkbox" name="orderType" value="2" checked />手工订单
                    </td>
                    <td>&nbsp;&nbsp;付款时间：</td>
                    <td>
	                    <input value="" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
						~
			 			<input value="" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
					</td>
					<td class="searchName">&nbsp;商家：</td>
					<td class="searchText"><input id="sellerId" name="sellerId" style="width:300px" value=""/></td>
            	</tr>
            	<tr>
            		<td class="searchName">订单结算状态：</td>
                    <td class="searchText">
	                    <input type="checkbox" name="settlementStatus" value="1" checked />已结算
	                    <input type="checkbox" name="settlementStatus" value="0" checked />未结算
                    </td>
                    <td>&nbsp;&nbsp;订单结算时间：</td>
                    <td>
                        <input value="" id="settlementStartTime" name="settlementStartTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                        ~
                        <input value="" id="settlementEndTime" name="settlementEndTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>
                    <td>&nbsp;&nbsp;退款结算时间：</td>
                    <td>
                        <input value="" id="refundSettlementStartTime" name="refundSettlementStartTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                        ~
                        <input value="" id="refundSettlementEndTime" name="refundSettlementEndTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                    </td>
            	</tr>
            	<tr>
                    <td class="searchName">&nbsp;订单编号：</td>
                    <td class="searchText"><input id="orderNumber" name="orderNumber" value="" /></td>
                    <td>
                        &nbsp;
						<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                    </td>
					<td>
                    	<a id="exportBtn" onclick="exportOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">导出结算清单</a>
                    </td>
                    <td></td>
                    <td></td>
            	</tr>
            </table>
        </form>
		</div>
		
		<div data-options="region:'center'" >
	    	<!--数据表格-->
	    	<table id="s_data" style=""></table>
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
		var orderTypeList =[]; 
		$('input[name="orderType"]:checked').each(function(){ 
			orderTypeList.push($(this).val()); 
		});
		var orderType = orderTypeList.join(",")
		if(orderType == ''){
			orderType = '0';
		}
		
		var settlementStatusList =[]; 
		$('input[name="settlementStatus"]:checked').each(function(){ 
			settlementStatusList.push($(this).val()); 
		});
		var settlementStatus = settlementStatusList.join(",")
		if(settlementStatus == ''){
			settlementStatus = '-1';
		}
		
		var sellerIds = $('#sellerId').combobox('getValues').toString();
		$('#s_data').datagrid('load', {
			startTime : $("#startTime").val(),
			endTime : $("#endTime").val(),
            settlementStartTime : $("#settlementStartTime").val(),
            settlementEndTime : $("#settlementEndTime").val(),
            refundSettlementStartTime : $("#refundSettlementStartTime").val(),
            refundSettlementEndTime : $("#refundSettlementEndTime").val(),
			orderType : orderType,
			settlementStatus : settlementStatus,
			sellerIds : sellerIds,
            orderNumber : $("#orderNumber").val()
		});
	}

	function exportOrder() {
		/* var startTime = $("#startTime").val();
		if(startTime == ''){
			$.messager.alert("info","请选择起始时间","warning");
		}else{
			$("#searchForm").submit();			
		} */
//        $.messager.confirm('温馨提示','（不可忽略此提示！） 导出的文件，打开时可能会提示格式不对，选择“是”即可，然后再另存为xlsx格式。******' +
//		'PS：由于数据复杂度高，又要求数据实时。so导出的时候稍等会，勿要导了一半，然后关闭页面，再导，这样反而更慢。',function(r){
//            if (r){
//                $("#searchForm").submit();
//            }
//        });
        $("#searchForm").submit();
	}
	
	$(function(){
		
		$('#s_data') .datagrid(
				{
					nowrap : false,
					striped : true,
					collapsible : true,
					idField : 'id',
					url : '${rc.contextPath}/finance/jsonOrderFinanceData',
					loadMsg : '正在装载数据...',
					fitColumns : true,
					remoteSort : true,
					singleSelect : true,
					pageSize : 50,
					pageList : [ 50,100 ],
					columns : [ [
							{field : 'isSettlementStr', title : '订单结算状态', width : 28, align : 'center'},
							{field : 'orderType', title : '订单类型', width : 25, align : 'center'},
							{field : 'statusStr', title : '订单状态', width : 25, align : 'center'},
							{field : 'number', title : '订单号', width : 45, align : 'center',
								formatter : function(value, row, index) {
									var a = '';
									if(row.orderType == '手工订单'){
										a = '<a target="_blank" href="${rc.contextPath}/orderManual/detail/'+row.id+'">'+row.number+'</a>'
									}else{
										a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>'
									}
									return a;
								}
							},
//							{field : 'createTime', title : '创建时间', width : 65, align : 'center'},
							{field : 'payTime', title : '付款时间', width : 65, align : 'center'},
//							{field : 'sendTime', title : '发货时间', width : 65, align : 'center'},
							{field : 'sellerName', title : '商家', width : 40, align : 'center'},
							{field : 'sendAddress', title : '发货地', width : 40, align : 'center'},
							{field : 'totalPrice', title : '订单总价', width : 30, align : 'center'},
							{field : 'realPrice', title : '实付金额', width : 30, align : 'center'},
							{field : 'postageIsSettlementStr', title : '运费结算状态', width : 30, align : 'center'},
							{field : 'settlementFreightMoney', title : '应付运费金额', width : 30, align : 'center'},
							{field : 'containsRefund', title : '是否包含退款商品', width : 20, align : 'center'}
							] ],
					pagination : true,
					rownumbers : true
				});
		
		$('#sellerId').combobox({
		    url:'${rc.contextPath}/seller/jsonSellerCode',
		    valueField:'code',   
		    textField:'text',
		    panelHeight:500,
            mode:'remote',
            multiple:true
        });
	});

</script>

</body>
</html>