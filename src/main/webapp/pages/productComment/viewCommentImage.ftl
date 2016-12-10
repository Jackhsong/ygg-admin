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
</head>
<body class="easyui-layout">

<div data-options="region:'center'" style="padding:5px;">
		<#if comment.image1?exists>
			<img alt="" src="${comment.image1}">
		</#if>
		<#if comment.image2?exists>
			<img alt="" src="${comment.image2}">
		</#if>
		<#if comment.image3?exists>
			<img alt="" src="${comment.image3}">
		</#if>
</div>
</body>
</html>