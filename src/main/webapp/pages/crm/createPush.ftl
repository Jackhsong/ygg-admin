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

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'center',title:'添加推送'" >
			<form id="paramForm">
				<fieldset>
					<legend>添加推送</legend>
					<table style="line-height:30px;">
						<tr>
							<td><label style="color: red;">*</label>推送对象：</td>
							<td>
								<input type="radio" value="1" name="pushType" checked="checked"/>换吧网络所有用户
								<input type="radio" value="2" name="pushType"/>心动慈露所有用户
								<input type="radio" value="3" name="pushType"/>分组用户
								<input id="groupId" name="groupId" class="easyui-numberbox" data-options="min:1"/><label style="color: red;">请输入分组ID</label>
							</td>
						</tr>
						<tr>
							<td><label style="color: red;">*</label><label>推送内容类型：</label></td>
							<td>
								<input type="radio" value="1" name="contentType" checked="checked"/>首页
								<input type="radio" value="2" name="contentType"/>自定义活动
								<input type="radio" value="3" name="contentType"/>特卖单品
								<input type="radio" value="4" name="contentType"/>特卖组合
							</td>
						</tr>
						<tr id="pid">
							<td><label style="color: red;">*</label><label>链接ID：</label></td>
							<td>
								<input id="productId" name="productId" class="easyui-numberbox" data-options="min:1"/><label style="color: red;">请输入链接ID</label>
							</td>
						</tr>
						<tr>
							<td><label style="color: red;">*</label><label>推送时间：</label></td>
							<td>
								<input id="sendTime" name="sendTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm'})" value=""/><label style="color: red;">需要准确到分钟，例如：2016-05-10 10:00</label>
							</td>
						</tr>
						<tr>
							<td><label style="color: red;">*</label><label>是否进离线消息：</label></td>
							<td>
								<input type="radio" value="1" name="offline" checked="checked"/>是
								<input type="radio" value="2" name="offline"/>否
							</td>
						</tr>
						<tr>
							<td><label style="color: red;">*</label><label>有效时长：</label></td>
							<td>
								<input id="expireTime" name="expireTime" class="easyui-numberbox" data-options="min:1"/><label style="color: red;">单位：小时</label>
							</td>
						</tr>
						<tr>
							<td><label style="color: red;">*</label>目标平台：</td>
							<td>
								<input type="checkbox" name="platform" value="1" />Android
								<input type="checkbox" name="platform" value="2" />IOS
							</td>
						</tr>
						<tr name="android">
							<td style="font-size: 30px;">Android：</td>
							<td></td>
						</tr>
						<tr name="android">
							<td><label style="color: red;">*</label>推送标题：</td>
							<td>
								<input id="androidTitle" name="androidTitle" size="83px;"/>
							</td>
						</tr>
						<tr name="android">
							<td><label style="color: red;">*</label>推送内容：</td>
							<td>
								<textarea id="androidContent" name="androidContent" rows="3" cols="61"></textarea>
							</td>
						</tr>
						<tr name="ios">
							<td style="font-size: 30px;">IOS：</td>
							<td></td>
						</tr>
						<tr name="ios">
							<td><label style="color: red;">*</label>推送内容：</td>
							<td>
								<textarea id="iosContent" name="iosContent" rows="3" cols="61"></textarea>
							</td>
						</tr>
						<tr>
							<td></td>
							<td><a class="easyui-linkbutton" onclick="submitMessage()">确定</a></td>
						</tr>
					</table>
				</fieldset>
			</form>
		</div>
	</div>
</div>
<script>
$(function() {
	$('tr[name=android]').each(function(i) {
		$(this).hide();
	});
	$('tr[name=ios]').each(function(i) {
		$(this).hide();
	});
	
	if($('input[name=contentType]:checked').val() == 1)
		$('#pid').hide();
	$('input[name=contentType]').change(function() {
		if($(this).val() == 1)
			$('#pid').hide();
		else
			$('#pid').show();
	});
	
	
	$('input[name=platform]').change(function() {
		$('input[name=platform]').each(function(i) {
			
		});
	});
});
function submitMessage() {
	if($('input[name=pushType]:checked').val() == 3 && $('#groupId').numberbox('getValue') == '') {
		$.messager.alert("提示信息", "分组Id不能为空", 'info');
		return false;
	}
	if($('input[name=contentType]:checked').val() != 1) {
		if($('#productId').numberbox('getValue') == '') {
			$.messager.alert("提示信息", "链接ID不能为空", 'info');
			return false;
		}
	}
	if($('#sendTime').val() == '') {
		$.messager.alert("提示信息", "推送时间不能为空", 'info');
		return false;
	}
	if($('#expireTime').val() == '') {
		$.messager.alert("提示信息", "有效时长不能为空", 'info');
		return false;
	}
	
	if(typeof($('input[name=platform]:checked').val()) == 'undefined') {
		$.messager.alert("提示信息", "目标平台不能为空", 'info');
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

</script>
</body>
</html>