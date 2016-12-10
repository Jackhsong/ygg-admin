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

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">

<div title="更新包邮模板" class="easyui-panel" style="padding:10px">
        <div id="updateTemplate" style="width:650px;height:300px;padding:20px 20px;">
            <form id="af" method="post" action="${rc.contextPath}/postage/addTemplate">
			<input id="editId" type="hidden" name="id" value="${freightTemplate.id?c}" >
            <table cellpadding="5">
                <tr>
                    <td>模板名称:</td>
                    <td><input class="easyui-textbox" id="edit_name" type="text" name="name" data-options="required:true" value="${freightTemplate.name}"></input></td>
                </tr>
                <tr>
                    <td>备注:</td>
                    <!-- <textarea name="desc" id="edit_desc" style="height: 60px;width: 300px"></textarea> -->
                    <td><input class="easyui-textbox" id="edit_desc" type="textarea" name="desc" value="${freightTemplate.desc}" ></input></td>
                </tr>
                <tr>
                    <td>使用状态:</td>
                    <td>
						<#if freightTemplate.getIsAvailable()==1 >
							<input type="radio" name="isAvailable" value="1" checked /> 可用&nbsp;&nbsp;
							<input type="radio" name="isAvailable" value="0" /> 停用
						<#else>
							<input type="radio" name="isAvailable" value="1" /> 可用&nbsp;&nbsp;
							<input type="radio" name="isAvailable" value="0" checked /> 停用
						</#if>
                    </td>
                </tr>
					<tr>
                    <td><input type="submit" value="更新"></td>
					<td></td>
                </tr>
            </table>
        </form>
        </div>
    </div>

</div>

</body>
</html>