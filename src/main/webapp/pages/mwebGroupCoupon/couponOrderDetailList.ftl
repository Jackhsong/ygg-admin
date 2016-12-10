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
	width:1100px;
	align:center;
	font-size:16px;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center',title:'心动慈露优惠券管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 170px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
	            <table class="search">
	            	<#if coupon.couponCode?exists>
	            	<tr>
	                	<td class="searchName">优惠券码：</td>
						<td class="searchText"><#if coupon.couponCode?exists>${coupon.couponCode}</#if></td>
	                    <td class="searchName">限用人数：</td>
	                    <td class="searchText"><#if coupon.useTimes?exists>${coupon.useTimes}</#if></td>
	                    <td class="searchName"></td>
						<td class="searchText"></td>
	                </tr>
	                </#if>
	                <tr>
	                	<td class="searchName">优惠券类型ID：</td>
						<td class="searchText"><#if coupon.couponDetailId?exists>${coupon.couponDetailId?c}</#if></td>
	                    <td class="searchName">具体类型：</td>
	                    <td class="searchText"><#if coupon.couponScope?exists>${coupon.couponScope}</#if></td>
	                    <td class="searchName"></td>
						<td class="searchText"></td>
	                </tr>
	                <tr>
	                	<td class="searchName">备注：</td>
	                    <td class="searchText"><#if coupon.remark?exists>${coupon.remark}</#if></td>
	                    <td class="searchName"></td>
						<td class="searchText"></td>
	                    <td class="searchName"></td>
						<td class="searchText"></td>
	                </tr>
	                <tr>
	                   
						<td class="searchName">有效期开始：</td>
	                    <td class="searchText"><#if coupon.startTime?exists>${coupon.startTime}</#if></td>
	                    <td class="searchName">有效期结束：</td>
						<td class="searchText"><#if coupon.endTime?exists>${coupon.endTime}</#if></td>
						
	                </tr>
					<tr>
						<td class="searchName">关联用户ID：</td>
	                    <td class="searchText"><#if coupon.accountId?exists>${coupon.accountId?c}</#if></td>
	                	<td class="searchName">用户名：</td>
						<td class="searchText"><#if coupon.accountName?exists>${coupon.accountName}</#if></td>
	                	<td class="searchName">用户类型：</td>
						<td class="searchText"><#if coupon.accountType?exists>${coupon.accountType}</#if></td>
					</tr>
					<tr>
					    <td class="searchName">心动慈露用户ID：</td>
	                    <td class="searchText"><#if coupon.teamAccountId?exists>${coupon.teamAccountId?c}</#if></td>
					</tr>
					 
	            </table>
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		</div>
	</div>
</div>
<script>
	function viewDetail(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr="${rc.contextPath}/order/detail/"+arr.rows[index].orderId;
		window.open(urlStr,"_blank");
	}
	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponOrderDetailInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{couponAccountId:${couponAccountId?c}},
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'orderId',    title:'订单ID', width:20, align:'center'},
                {field:'orderNumber',    title:'订单编号', width:50, align:'center'},
                {field:'payTime',    title:'付款时间', width:50, align:'center'},
                {field:'realPrice',    title:'订单实付金额', width:70, align:'center'},
                {field:'couponPrice',     title:'优惠金额',  width:50,   align:'center'},
                {field:'sellerName',     title:'商家',  width:50,   align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
	                    return '<a href="javaScript:;" onclick="viewDetail(' + index + ')">查看</a>';
                    }
                }
            ]],
            pagination:true
        });
	});
</script>

</body>
</html>