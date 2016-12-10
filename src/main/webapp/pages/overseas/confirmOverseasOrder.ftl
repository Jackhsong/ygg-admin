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
	<table>
		<tr>
			<td>
				<form id="methodForm" method="post">
					<input type=hidden id="type" name="type" value="" />
					<input type=hidden id="isRight" name="isRight" value="0" />
					<input type=hidden id="canDelete" name="canDelete" value="0" />
					<input id="orderFileBox" type="text" name="orderFile" style="width:300px" /><br/><br/><br/>
					设置导单日期：
					<input value="" id="sendDate" name="sendDate" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00'})"/>
					<a id="searchBtn" onclick="doFormAction(1)" href="javascript:;" class="easyui-linkbutton" >模拟导入结果</a>
					<br/><br/>
					<a id="searchBtn" onclick="doFormAction(3)" href="javascript:;" class="easyui-linkbutton" >确认将订单导入系统</a>
					<a id="searchBtn" onclick="doFormAction(4)" href="javascript:;" class="easyui-linkbutton" >将订单导入记录从系统删除</a>
				</form>
			</td>
			<td valign=top>
				<form id="downFileForm" action="${rc.contextPath}/overseasOrder/downloadTemplate" method="post">
					<input type=hidden name="type" value="1" />
					<a id="searchBtn" onclick="downFile()" href="javascript:;" class="easyui-linkbutton" >下载导单模板</a>
				</form><br/><br/>
				<form id="downResult" action="${rc.contextPath}/overseasOrder/downloadReceiveResult" method="post">
					<input type=hidden name="type" value="1" />
					<a id="searchBtn" onclick="downResult()" href="javascript:;" class="easyui-linkbutton" >导出模拟结果</a>
				</form>
			</td>
		</tr>
	</table>
	
	<br/><br/>
	<div>
		<span style="color:red">成功：<span id="okNum"> 0</span></span>&nbsp;&nbsp;
		<span style="color:red">失败：<span id="failNum"> 0</span></span>
		<table id="s_data">
		</table>		
	</div>
</div>

<script>

function doFormAction(type) {
	$('#type').val(type);
	if(type == 1){
		$('#methodForm').attr("action", "importOverseasOrder").attr("enctype", "multipart/form-data").submit();
	}else if(type == 2){
	}else if(type == 3){
		$('#methodForm').attr("action", "importOverseasOrder").submit();
	}else if(type == 4){
		$('#methodForm').attr("action", "importOverseasOrder").submit();
	}
}

function downFile() {
	$('#downFileForm').submit();
}

function downResult() {
	$('#downResult').submit();
}

$(function (){
	
	$('#s_data').datagrid({
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'pid',
		url : '${rc.contextPath}/overseasOrder/receiveResult',
		queryParams : {
			delete:1
		},
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : true,
		columns : [ [
				{field : 'status', title : '导入状态', width : 70, align : 'center'},
				{field : 'msg', title : '说明', width : 70, align : 'center'},
				{field : 'orderNumber', title : '订单号', width : 70, align : 'center'},
				{field : 'cellSendDate', title : '表格内导单日期', width : 70, align : 'center'}
				] ]
		});
	
	$('#methodForm').form({
		url:'',   
		onSubmit: function(){   
			// do some check   
			// return false to prevent submit;
			var type = $('#type').val();
			var orderFile = $('#orderFileBox').filebox("getValue");
			var sendDate = $('#sendDate').val();
			if(orderFile == "" && type != 2){
				$.messager.alert("info","请选择文件","warn");
				return false;
			}
			if(sendDate == ""){
				$.messager.alert("info","请选择日期","warn");
				return false;
			}
			if(type == 3){
				//确定导入
				var isRight = $('#isRight').val();
				if(isRight == 0){
					$.messager.alert("提示","当前状态不允许确定导入","error");
					return false;
				}
			}
			if(type == 4){
				//删除
				var canDelete = $('#canDelete').val();
				if(canDelete == 0){
					$.messager.alert("提示","当前状态不允许删除","error");
					return false;
				}
			}
			$.messager.progress();
		},   
		success:function(data){
			$.messager.progress('close');
			var data = eval('(' + data + ')');  // change the JSON string to javascript object   
			if (data.status == 1){
				$('#isRight').val(data.isRight);
				$('#canDelete').val(data.canDelete);
				$('#okNum').html(data.okNum);
				$('#failNum').html(data.failNum);
				$('#s_data').datagrid('load',{delete:0});
			}else{
				$.messager.alert("提示",data.msg,"info");
			}
		}   
	});
	
	$('#orderFileBox').filebox({
		buttonText: '打开导单文件',
		buttonAlign: 'right'
	});
});

</script>

</body>
</html>