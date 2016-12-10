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
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >  
	    	<div data-options="region:'north',title:'条件筛选-仅退款订单处理',split:true" style="height: 160px;">
	    	<form action="${rc.contextPath}/refund/exportResult" method="post">
	    		<input type="hidden" id="type" name="type" value="1"/>
	    		<table>
	    			<tr>
	    				<td>
	    				订单编号 <input class="easyui-numberbox" name="number" data-options="prompt:'只能输入数字'" style="width:160px">&nbsp;&nbsp;&nbsp;&nbsp;
	    				</td>
	    				<td>
	    				退款状态 <select name="status" id="status" style="width:80px">
	    					<option value="0">全部</option>
        					<option value="1">申请中</option>
        					<option value="3">待退款</option>
        					<option value="4">退款成功</option>
        					<option value="5">退款关闭</option>
        					<option value="6">退款取消</option>
	    				</select>
	    				</td>
	    				<td>
	    				退款申请时间 <input name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
									~
			 						<input name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
	    				</td>
	    			</tr>
	    			<tr>
	    				<td>
	    				打款状态 <select name="moneyStatus" id="moneyStatus" style="width:80px;">
	    					<option value="0">全部</option>
        					<option value="1">已打款</option>
        					<option value="2">未打款</option>
	    				</select>&nbsp;&nbsp;&nbsp;&nbsp;
	    				财务金额分摊状态 <select name="refundProportionStatus" id="refundProportionStatus" style="width:80px;">
	    					<option value="-1">全部</option>
        					<option value="1">已分摊</option>
        					<option value="0">未分摊</option>
	    				</select>&nbsp;&nbsp;&nbsp;&nbsp;
	    				</td>
	    				<td>用户名 <input class="easyui-textbox" name="name" style="width:160">&nbsp;&nbsp;</td>
	    				<td>
	    				收货人姓名 <input class="easyui-textbox" name="receiveName" style="width:160">&nbsp;
	    				收货手机 <input class="easyui-numberbox" data-options="prompt:'只能输入数字'" name="mobileNumber" style="width:160">
	    				</td>
	    			</tr>
					<tr>
                        <td >订单状态：
                            <select id="orderStatus" name="orderStatus" style="width: 170px;">
                                <option value="0">全部</option>
                                <option value="1">未付款</option>
                                <option value="2">待发货</option>
                                <option value="3">已发货</option>
                                <option value="4">交易成功</option>
                                <option value="5">用户取消</option>
                                <option value="6">超时取消</option>
                            </select>&nbsp;&nbsp;&nbsp;&nbsp;
                            	是否有物流信息：
                            	<select id="logisticsStatus" name="logisticsStatus" style="width:170px;">
                            		<option value="-1">全部</option>
                            		<option value="1">有</option>
                            		<option value="0">无</option>
                            	</select>
						</td>
                        <td>
                            商家订单导出状态：
                            <select id="operationStatus" name="operationStatus" style="width: 170px;">
                                <option value="-1">全部</option>
                                <option value="1">已导出</option>
                                <option value="0">未导出</option>
                            </select>
                        </td>
						<td>
                            订单来源：
                            <select id="searchOrderChannel" name="searchOrderChannel" style="width: 170px;">
                                <option value="0">全部</option>
                                <option value="1">换吧网络</option>
                                <option value="2">心动慈露</option>
                                <option value="3">心动慈露
</option>
                            </select>
						</td>
					</tr>
	    			<tr>
	    				<td>
	    					最后操作时间 
	    					<input name="startCheckTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
							~
			 				<input name="endCheckTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                            &nbsp;商家&nbsp;<input id="sellerId" type="text" name="sellerId" />
			 			</td>
                        <td>
                            &nbsp;打款账户选择
							<select name="financialAffairsCardId" id="financialAffairsCardId" >
                                <option value="0"  >全部</option>
                                <option value="4"  >支付宝 黄丽慧  huanglh@yangege.com</option>
                                <option value="5"  >支付宝 换吧网络  yangege@yangege.com</option>
                                <option value="6"  >支付宝 微信网页  1231417002</option>
                                <option value="7"  >支付宝 微信APP  1230273901</option>
                                <option value="8"  >支付宝 银联  ywae0937</option>
                                <option value="9"  >支付宝 格家-微信APP  1249412201</option>
                                <option value="10"  >支付宝 格家-微信网页版  1249303601</option>
                                <option value="11"  >支付宝 换吧网络微信  1266647901</option>
                                <option value="12"  >支付宝 格家-微信app  1270667101</option>
                                <option value="13"  >支付宝 格家-mohh  mohh@gegejia.com</option>
                                <option value="14" >支付宝 格家-国际版hk  gegejia_hk@yangege.com</option>
                                <option value="15"  >支付宝 格家-微信APP  1289865701</option>
                                <option value="16"  >支付宝 格家-微信app  1297858301</option>
                                <option value="17"  >支付宝 莫红辉  3067537@qq.com</option>
                                <option value="18"  >支付宝 格家微信-app 1304400701</option>
                                <option value="19"  >燕网-微信 1316385501</option>
							</select>&nbsp;&nbsp;
						</td>
	    				<td>
	    					<a onclick="searchInfo()" href="javascript:;" class="easyui-linkbutton" style="width:100px" >查询</a>&nbsp;&nbsp;
	    					<input type="submit" value="导出查询结果" class="easyui-linkbutton" style="width:100px;height: 30px"/>
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

function dealWithApply(id) {
	var urlStr = "${rc.contextPath}/refund/dealRefund/"+id;
	window.open(urlStr,"_blank");
}

function searchInfo() {
	$('#s_data').datagrid('load', {
		source:2,
		number : $("input[name='number']").val(),
		status : $("#status").val(),
        operationStatus : $("#operationStatus").val(),
        searchOrderChannel : $("#searchOrderChannel").val(),
        orderStatus : $("#orderStatus").val(),
        financialAffairsCardId : $("#financialAffairsCardId").val(),
		type : 1,
		startTime : $("input[name='startTime']").val(),
		endTime : $("input[name='endTime']").val(),
		moneyStatus : $("#moneyStatus").val(),
		name : $("input[name='name']").val(),
		receiveName : $("input[name='receiveName']").val(),
		mobileNumber : $("input[name='mobileNumber']").val(),
		startCheckTime : $("input[name='startCheckTime']").val(),
		endCheckTime : $("input[name='endCheckTime']").val(),
		refundProportionStatus : $("#refundProportionStatus").val(),
        sellerId:$("#sellerId").combobox('getValue'),
        logisticsStatus:$("#logisticsStatus").val()
	});
}
	
	$(function (){

        $('#sellerId').combobox({
            panelWidth:350,
            panelHeight:350,
            mode:'remote',
            url:'${rc.contextPath}/seller/jsonSellerCode',
            valueField:'code',
            textField:'text'
        });

		$('#s_data') .datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'id',
			url : '${rc.contextPath}/refund/jsonAll',
			queryParams:{
				type : 1,
				source : 2
			},
			loadMsg : '正在装载数据...',
			fitColumns : true,
			remoteSort : true,
			singleSelect : true,
			pageSize : 50,
			pageList : [ 50, 60 ],
			columns : [ [
					{field : 'id', title : 'ID', width : 30, align : 'center'},
					{field : 'orderNumber', title : '订单号', width : 35, align : 'center'},
					{field : 'orderChannel', title : '订单来源', width : 35, align : 'center'},
					{field : 'orderStatus', title : '订单状态', width : 25, align : 'center'},
					{field : 'sellerName', title : '商家', width : 35, align : 'center'},
					{field : 'payTime', title : '付款时间', width : 45, align : 'center'},
					{field : 'createTime', title : '退款发起时间', width : 55, align : 'center'},
					{field : 'updateTime', title : '最新操作时间', width : 55, align : 'center'},
					{field : 'statusStr', title : '退款状态', width : 20, align : 'center'},
					{field : 'moneyStatus', title : '打款状态', width : 25, align : 'center'},
					{field : 'realMoney', title : '退款金额', width : 30, align : 'center'},
					{field : 'productName', title : '商品名称', width : 50, align : 'center'},
					{field : 'productCount', title : '购买数量', width : 20, align : 'center'},
					{field : 'fullName', title : '收货人', width : 30, align : 'center'},
					{field : 'mobileNumber', title : '收货手机', width : 30, align : 'center'},
					{field : 'hidden', title : '操作', width : 80, align : 'center',
						formatter : function(value, row, index) {
							var a = '';
							if(row.status == 1){
								a = '<a href="javaScript:;" onclick="dealWithApply(' + row.id + ')">处理申请</a>';
							}else if(row.status == 3){
								a = '<a href="javaScript:;" onclick="dealWithApply(' + row.id + ')">打款</a>';
							}else{
								a = '<a href="javaScript:;" onclick="dealWithApply(' + row.id + ')">详情</a>';
							}
							return a;
						}
					} ] ],
			pagination : true
		}); 
	});
</script>

</body>
</html>