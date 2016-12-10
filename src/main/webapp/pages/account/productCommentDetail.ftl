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
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">

<style type="text/css">
	#myCss span {
		font-size:12px;
		padding-right:40px;
	}
</style>

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div id="myCss">
		<a href="${rc.contextPath}/comment/list" class="easyui-linkbutton" style="width:200px">返回商品评价列表</a>
		<div style="padding:20px;">
			<span>订单编号：<#if commentMap.orderNo?exists>${commentMap.orderNo?c}</#if></span>
			<span>用户Id：<#if commentMap.accountId?exists>${commentMap.accountId?c}</#if></span>
			<span>用户名：<#if commentMap.username?exists>${commentMap.username}</#if></span>
			<span>用户类型：<#if commentMap.userType?exists>${commentMap.userType}</#if></span><br/><br/>
			<span>收货人姓名：<#if commentMap.receiveName?exists>${commentMap.receiveName}</#if></span>
			<span>收货人手机号：<#if commentMap.receiveMobile?exists>${commentMap.receiveMobile}</#if></span>
			<#if commentMap.orderId?exists>
			<a target="_blank" href="${rc.contextPath}/order/detail/${commentMap.orderId?c}" class="easyui-linkbutton">查看订单明细</a>
			</#if>
			<br/><br/>
			<span>商品Id：<#if commentMap.productId?exists>${commentMap.productId?c}</#if></span>
			<span>商品名称：<#if commentMap.productName?exists>${commentMap.productName}</#if></span>
			<span>商品数量：<#if commentMap.productAmount?exists>${commentMap.productAmount?c}</#if></span>
			<span>售价：<#if commentMap.salePrice?exists>${commentMap.salePrice}</#if></span>
			<br/><br/>
			<span><font size="3" color="red">总体印象：<#if commentMap.levelDesc?exists>${commentMap.levelDesc}</#if></font></span><br/><br/>
			<span>商品评论：<#if commentMap.content?exists>${commentMap.content}</#if></span><br/><br/>
			<span>上传图片：</span><br/><br/>
			<#if commentMap.image1?exists>
				<span><img alt="" src="${commentMap.image1}"></span>
			</#if>
			<#if commentMap.image2?exists>
				<span><img alt="" src="${commentMap.image2}"></span>
			</#if>
			<#if commentMap.image3?exists>
				<span><img alt="" src="${commentMap.image3}"></span>
			</#if>
		</div>
	</div>

</div>
</body>
</html>