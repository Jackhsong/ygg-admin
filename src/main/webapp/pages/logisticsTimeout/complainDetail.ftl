<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
</head>
<body>

<div id="complain_div">
	<p>订单编号：${orderNumber!""}</p>
	<p>商家名称：${sellerName!""}</p>
	<hr/>
	<#if complainList?exists>
		<#list complainList as complain>
		<p>申诉时间：${complain.complainTime!""}</p>
		<p>申诉理由：${complain.reason!""}</p>
		<p>处理结果：${complain.result!""}</p>
		<p>处理人：${complain.dealUser!""}</p>
		<p>处理时间：${complain.dealTime!""}</p>
		<p>处理备注：${complain.remark!""}</p>
		<hr/>
		</#list>
	</#if>
	<p>超时原因：${timeoutReason!""}</p>
</div>
</body>
</html>