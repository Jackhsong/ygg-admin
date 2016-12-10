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
<script src="${rc.contextPath}/js/qqbs/qrCodeIndex.js"></script>
<script src="${rc.contextPath}/js/lib/clipboard.min.js"></script>
<link href="${rc.contextPath}/css/qqbs/qrCodeIndex.css" rel="stylesheet" type="text/css" />
</head>
<body class="easyui-layout">
<input type="hidden" value="${rc.contextPath}" id="rcContextPath"/>
<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" id="center" >
	<div id="searchQRCodeDiv" class="datagrid-toolbar">
		<form id="searchForm">
			<table>
				<tr>
					<td>用户ID</td>
					<td><input id="queryAccountId" name="queryAccountId" value="" /></td>
					<td><a id="searchBtn"  href="javascript:void(0)" class="easyui-linkbutton">查询</a></td>
				</tr>
			</table>
	</div>

	<div title="永久二维码管理" class="easyui-panel" id="qrCodeManageDiv" >
		<div class="content_body">
	        <table id="qrCodeResult" >
	        </table>
		</div>
	</div>
	<!-- 二维码生成 -->
    <div id="createQRCodePanel">
        <form id="createQRCodeForm" method="post">
           <div>
				<label for="accountId">用户ID：</label>
				<input class="easyui-validatebox" type="text" name="accountId" id="accountId" data-options="required:true" />
		    </div>
		     <div class="qrCodeUrlDiv">
				<label for="qrCodeUrl">二维码地址：</label>
				<input  type="text" name="qrCodeUrl" id="qrCodeUrl" readOnly="true" />
				<a  href="javascript:void(0)" data-clipboard-target="#qrCodeUrl" class="easyui-linkbutton copy-link" data-options="iconCls:'icon-cut'">复制</a>
		    </div>
		    <div class="createButtonDiv">
				<button id="createButton"  class="easyui-linkbutton">生成</button>	
			</div>	
    	</form>
    </div>
    <!-- 二维码生成end -->
	
</div>


</body>
</html>