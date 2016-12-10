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
		padding-right:20px;
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
		<a href="${rc.contextPath}/orderManual/list" class="easyui-linkbutton" style="width:200px">返回手动订单列表</a>
		<div style="padding:10px;">
			<span style="padding-right:30px;">订单信息</span><br/>
			<span>订单编号：<#if detail.number?exists>${detail.number?c}</#if></span> 
			<span>订单状态：<#if detail.status?exists>${detail.status}</#if></span>
			<span>建单原因：<#if detail.remark?exists>${detail.remark}</#if></span>
			<br/>
			<hr/>
			<span>下单时间：<#if detail.createTime?exists>${detail.createTime}</#if></span>
			<span>发货时间：<#if detail.sendTime?exists>${detail.sendTime}</#if></span>
			<br/><br/>
			<span>收货人姓名：<#if detail.receiveName?exists>${detail.receiveName}</#if></span>
			<span>收货人手机号：<#if detail.receiveMobile?exists>${detail.receiveMobile}</#if></span>
			<span>收货人身份证号：<#if detail.receiveIdCart?exists>${detail.receiveIdCart}</#if></span>
			<br/><br/>
			<span>详细地址：<#if detail.address?exists>${detail.address}</#if></span>
			<br/><br/>
			<span>商家：<#if detail.sellerName?exists>${detail.sellerName}</#if></span>
			<span>发货方式：<#if detail.sellerType?exists>${detail.sellerType}</#if></span>
			<span>发货地：<#if detail.sendAddress?exists>${detail.sendAddress}</#if></span>
			<br/><br/>
			<span>备注：<#if detail.desc?exists>${detail.desc}</#if></span>
		</div>
	</div>
	<!-- 商品列表 -->
	<div>
		<br/><br/>
		<span style="padding-right:30px;">商品明细</span> 
		<span style="color:red">商品总价：<#if detail.totalProductPrice?exists>${detail.totalProductPrice}</#if></span> 
		<br/>
		<hr/>
		<table  class="table table-bordered table-striped" width="100%">
			<tr>
				<th>商品ID</th>
				<th>商品编码</th>
				<th>商品名称</th>
				<th>商家</th>
				<th>发货类型</th>
				<th>发货地</th>
				<th>售价</th>
				<th>数量</th>
				<th>总价</th>
			</tr>
			<#list detail.products as p>
				<tr>
					<td>${p.id}</td>
					<td>${p.code}</td>
					<td>${p.name}</td>
					<td>${p.sellerName}</td>
					<td>${p.sellerType}</td>
					<td>${p.sendAddress}</td>
					<td>${p.salePrice}</td>
					<td>${p.productCount}</td>
					<td>${p.productTotalPrice}</td>
				</tr>
			</#list>
		</table>		
	</div>

</div>
</body>
</html>