<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"  />
<title>换吧网络-后台管理</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<script src="${rc.contextPath}/js/qqbs/qqbsUserIndex.js"></script>
<link href="${rc.contextPath}/css/qqbs/qqbsUserIndex.css" rel="stylesheet" type="text/css" />
</head>
<body class="easyui-layout">
<input type="hidden" value="${rc.contextPath}" id="rcContextPath"/>
<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" id="center" >

	<div id="searchQqbsUserDiv" class="datagrid-toolbar">
		<form id="searchForm">
			<table>
				<tr>
					<td>用户ID</td>
					<td><input id="accountId" name="accountId" value="" /></td>
					<td>昵称</td>
					<td><input id="nickName" name="nickName" value="" /></td>
					<td><a id="searchBtn"  href="javascript:void(0)" class="easyui-linkbutton">查询</a></td>
				</tr>
			</table>
	</div>

	<div title="用户管理" class="easyui-panel" id="qqbsUserManageDiv" >
		<div class="content_body">
	        <table id="qqbsUserResult" >
	        </table>
		</div>
	</div>
	
	
</div>


</body>
</html>