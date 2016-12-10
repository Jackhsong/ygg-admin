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
	<div>
		<font color="red">文件格式必须为.xls、大小不得超过1M</font>
		<div style="padding-left:500px;padding-bottom:15px;">
			<form action="${rc.contextPath}/order/exportSendTemplate" method="post">
				<input type=hidden name="type" value="1" />
				<input type="submit" value="下载发货模板" style="width:100px"/>
			</form>
		</div>
		<form id="uploadOrderForm" method="post" enctype="multipart/form-data">
			<input type=hidden id="importType" name="importType" value="" />
			<input type=hidden id="isRight" name="isRight" value="0" />
			<input id="orderFileBox" type="text" name="orderFile" style="width:300px" /><br/><br/><br/>
			<input type="button" value="模拟导入结果" onClick="return uploadOrder(1);" style="width:100px"/>
			&nbsp;&nbsp;&nbsp;
			<input type="button" value="确认导入系统" onClick="return uploadOrder(3);" style="width:100px"/>
		</form><br/>
		<form action="${rc.contextPath}/order/exportSendResult" method="post">
			<input type=hidden name="type" value="1" />
			<input type="submit" value="导出模拟结果" style="width:100px"/>
		</form>
	</div>
	<br/><br/>
	<div>
		<span style="color:red">成功：<span id="okNum"> 0</span></span>&nbsp;&nbsp;
		<span style="color:red">失败：<span id="failNum"> 0</span></span>
		<table id="s_data">
		</table>		
	</div>
</div>

<script>

	function uploadOrder(type) {
		$('#importType').val(type);
        $('#uploadOrderForm').submit();
	}
	
	$(function(){
	
		$('#orderFileBox').filebox({
			buttonText: '打开发货文件',
			buttonAlign: 'right'
		})
		
		$('#uploadOrderForm').form({
			url:'${rc.contextPath}/order/batchSendOrder',   
			onSubmit: function(){
				// do some check   
				// return false to prevent submit;
				var type = $('#importType').val();
				var orderFile = $('#orderFileBox').filebox("getValue");
				if(orderFile == "" && type != 2){
					$.messager.alert("info","请选择文件","warn");
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
				$.messager.progress();
			},   
			success:function(data){
				$.messager.progress('close');
				var data = eval('(' + data + ')');  // change the JSON string to javascript object   
				if (data.status == 1){
					$('#okNum').html(data.okNum);
					$('#failNum').html(data.failNum);
					if(data.failNum != 0 && data.canChangeIsRightFlag == true){
						$.messager.confirm('确认','这批订单全部都已经发货，是否继续修改？',function(r){
						    if (r){
						    	$('#isRight').val(true);
						    }else{
						    	$('#isRight').val(data.isRight);
						    }
						});
					}else{
						$('#isRight').val(data.isRight);						
					}
					$('#s_data').datagrid('load',{delete:0});
				}else{
					$.messager.alert("提示",data.msg,"info");
				}
			}   
		});
			
		$('#s_data').datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'pid',
			url : '${rc.contextPath}/order/receiveResult',
			queryParams : {
				delete:1
			},
			loadMsg : '正在装载数据...',
			fitColumns : true,
			remoteSort : true,
			singleSelect : true,
			pageSize : 50,
			pageList : [ 50, 100 ],
			columns : [ [
					{field : 'status', title : '导入状态', width : 70, align : 'center'},
					{field : 'msg', title : '说明', width : 70, align : 'center'},
					{field : 'orderNumber', title : '订单号', width : 70, align : 'center'},
					{field : 'createTime', title : '创建时间', width : 70, align : 'center'},
					{field : 'payTime', title : '付款时间', width : 70, align : 'center'},
					{field : 'sendTime', title : '发货时间', width : 70, align : 'center'},
					{field : 'channel', title : '物流公司', width : 70, align : 'center'},
					{field : 'number', title : '物流单号', width : 70, align : 'center'},
					] ],
			pagination : true,
			rownumbers : true
		});
	})
</script>

</body>
</html>