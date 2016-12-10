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
		<div data-options="region:'center',title:'创建短信'" >
			<form id="paramForm">
				<input type="hidden" id="linkInfoId" name="linkInfoId"/>
				<input type="hidden" id="longUrl" name="longUrl"/>
				<input type="hidden" id="sendType" name="sendType" />
				<fieldset>
					<legend>添加短信</legend>
					<table style="line-height:30px;">
						<tr>
							<td width="110px;"><label>剩余短信数：</label></td>
							<td style="color: red;">
								<label id="ggj"><#if montnets??>${montnets} 条</#if></label>
								<label id="ggt" hidden="hidden"><#if montnetsTuan??>${montnetsTuan} 条</#if></label>
								<label id="qqbs" hidden="hidden"><#if montnetsGlobal??>${montnetsGlobal} 条</#if></label>
							</td>
						</tr>
						<tr>
							<td width="110px;"><label>发送用户分组：</label></td>
							<td>
								<input id="groupId" name="groupId" class="easyui-numberbox" data-options="min:1"/><label style="color: red;">请输入分组ID</label>
							</td>
						</tr>
						<tr>
							<td><label>过滤已发送会员：</label></td>
							<td>
								<input type="radio" value="0" name="filterType" checked="checked"/>不过滤
								<input type="radio" value="1" name="filterType"/>最近1天
								<input type="radio" value="2" name="filterType"/>最近5天
								<input type="radio" value="3" name="filterType"/>最近7天
								<input type="radio" value="7" name="filterType"/>其他
								<input name="filterDay" class="easyui-numberbox" data-options="min:1"/>天
							</td>
						</tr>
						<tr>
							<td width="110px;"><label>定时发送：</label></td>
							<td>
								<input name="sendTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" value="" readonly="readonly"/><label style="color: red;">立马发送可以不填，有1分钟缓冲时间</label>
							</td>
						</tr>
						<tr>
							<td><label>链接类型：</label></td>
							<td>
								<input type="radio" value="0" name="pType" checked="checked"/>单品
								<input type="radio" value="1" name="pType"/>组合
							</td>
						</tr>
						<tr>
							<td><label>链接ID：</label></td>
							<td>
								<input id="pId" class="easyui-numberbox" data-options="min:1"/><label style="color: red;">请输入商品ID</label>
							</td>
						</tr>
						<tr>
							<td><a id="generateLink" class="easyui-linkbutton">生成链接</a></td>
							<td>
								<input id="shortLink" name="shortUrl" readonly="readonly" style="width: 300px;"/><label style="color: red;">必须生成链接，可以不使用</label>
							</td>
						</tr>
						<tr>
							<td><label>签名：</label></td>
							<td>
								<select id="contentType" name="contentType" class="easyui-combobox" style="width:80px;"></select>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<textarea id="content" name="content" rows="3" cols="50"></textarea>
							</td>
						</tr>
						<tr>
							<td><label>测试短信：</label></td>
							<td>
								<input name="phone" class="easyui-numberbox" data-options="min:1" /><a class="easyui-linkbutton" onclick="sendMessage(0)">发送测试</a>
							</td>
						</tr>
						<tr>
							<td></td>
							<td><a class="easyui-linkbutton" onclick="sendMessage(1)">发送短信</a></td>
						</tr>
					</table>
				</fieldset>
			</form>
			<form id="urlForm">
				<input type="hidden" name="longUrl" id="furl">
				<input type="hidden" name="groupId" id="fgroupId">
			</form>
		</div>
	</div>
</div>
<script>
function sendMessage(sendType) {
	if($('#groupId').val() == '') {
		$.messager.alert("提示信息", "分组Id不能为空", 'info');
		return false;
	}
	if($('#shortLink').val() == '') {
		$.messager.alert("提示信息", "短连接可以不使用，但不能为空", 'info');
		return false;
	}
	if($('#content').val() == '') {
		$.messager.alert("提示信息", "短信内容不能为空", 'info');
		return false;
	}
	$('#sendType').val(sendType);
	$.messager.confirm("确认信息", "确定发送短信？", function(r) {
		if(r) {
			$.ajax({
				type : "POST",
				url : '${rc.contextPath}/crm/sendMessage',
				data: $('#paramForm').serialize(),
				success : function(data) {
					$('#paramForm')[0].reset();
					if(data.status == 1) {
						if(sendType == 0) {
							$.messager.alert('提示信息', '测试短信发送成功', 'info');
						} else {
							window.location.href="${rc.contextPath}/crm/toSmsList";
						}
					} else {
						$.messager.alert('提示信息', data.msg, 'error');
					}
				}
			});
		}
	});
}
$(function() {
	//生成链接
	$('#generateLink').click(function() {
		var pid = $('#pId').numberbox('getValue');
		if(pid == '') {
			$.messager.alert('提示信息', '链接ID为空', 'info');
			return false;
		}
		var pType = $('input[name=pType]:checked').val();

		var longUrl = "";
		if(pType == 0) {
			// 单品
			longUrl = "http://static.gegejia.com/custom/openApp1.html?url=gegejia://resource/saleProduct/" + pid + "/code";
		} else if(pType == 1) {
			// 组合
			longUrl = "http://static.gegejia.com/custom/openApp1.html?url=gegejia://resource/commonActivitiesList/" + pid;
		}
		$('#furl').val(longUrl);
		$('#fgroupId').val($('#groupId').val());
		$.ajax({
			type : "POST",
			url : '${rc.contextPath}/crm/shortUrl',
			data : $('#urlForm').serialize(),
			success : function(data) {
				if(data.status == 1) {
					$('#shortLink').val(data.data.shortUrl);
					$('#longUrl').val(data.data.longUrl);
					$('#linkInfoId').val(data.data.id);
				} else {
					$.messager.alert('提示信息', data.msg, 'error');
				}
			}
		});
	});
	
	//签名相关事件
	$('#contentType').combobox({
		valueField: 'code',
		textField: 'text',
		panelHeight: 80,
		data: [{
			code: '0',
			selected: true,
			text: '换吧网络'
		}
		/**,{
			code: '1',
			text: '心动慈露'
		},{
			code: '2',
			text: '心动慈露
'
		}*/],
		onChange:function(newValue, oldValue){
			if(newValue == 0) {
				$('#ggt').hide();
				$('#qqbs').hide();
				$('#ggj').show();
			} else if(newValue == 1) {
				$('#ggj').hide();
				$('#ggt').show();
				$('#qqbs').hide();
			} else if(newValue == 2) {
				$('#ggj').hide();
				$('#ggt').hide();
				$('#qqbs').show();
			}
		}
	});
});
</script>
</body>
</html>