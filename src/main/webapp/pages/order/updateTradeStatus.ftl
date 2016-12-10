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
	<div id="myCss">
		<div style="padding:10px;">
			<form id="searchOrder" method="post">
				订单编号：<input id="number" name="number" value="" />
				<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton">查询</a>
			</form>
		</div>
		<#-- display:none -->
		<div id="showOrderDetail" style="padding-left:30px;display:none">
		
			订单编号：<span id="sNumber" name="number"></span>&nbsp;&nbsp;&nbsp;
			订单状态：<span id="sStatus" name="oldStatus"></span><br/><br/>

			<form id="updateStatus" method="post">
				<input type="hidden" value="" name="orderId" id="orderId" />
				<input type="hidden" value="" name="oldStatus" id="oldStatus_hidden" />
				<input type="hidden" value="" name="number" id="number_hidden" />
				修改状态为:
				<select id="newStatus" name="newStatus" >
					<option value="0">--请选择--</option>
					<option value="2">待发货</option>
					<option value="5">用户取消</option>
				</select><br/><br/>
				<textarea name="mark" style="height: 60px;width: 500px""></textarea>
				<br/><br/>
				<a id="saveBtn" onclick="saveStatus()" href="javascript:;" class="easyui-linkbutton">保存</a>
			</form>
			
		</div>
	</div>
	<!-- 操作日志 -->
	<div>
		<br/><br/>
		<span style="padding-right:30px;">后台订单操作日志<span> 
		<br/>
		<hr/>
		<table id="s_data">
		</table>		
	</div>
</div>

<script>

	function searchOrder(){
		$('#searchOrder').submit();
	}
	
	function saveStatus(){
		$('#updateStatus').submit();
	}
	
	$(function(){

		$('#searchOrder').form({   
		    url:'${rc.contextPath}/order/searchOrder',   
		    onSubmit: function(){   
		        // do some check   
		        // return false to prevent submit;
		        $.messager.progress();
		    },   
		    success:function(data){
		    	$.messager.progress('close');
		    	var data = eval('(' + data + ')');  // change the JSON string to javascript object   
		        if (data.status == 1){
		        	$('#newStatus').find('option').eq(0).attr("selected","selected");
		        	$('#sNumber').html(data.number);
		            $('#sStatus').html(data.msg);
					$('#orderId').val(data.id);
					$('#oldStatus_hidden').val(data.orderStatus);
					$('#number_hidden').val(data.number);
		            $('#showOrderDetail').show();
		        }else{
		        	$.messager.alert("提示",data.msg,"error");	
		        }
		    }   
		}); 
		
		$('#updateStatus').form({   
		    url:'${rc.contextPath}/order/updateStatus',   
		    onSubmit: function(){   
		        var newStatus = $("#newStatus").val();
		        if(newStatus == '' || newStatus == 0){
		        	$.messager.alert("提示","请选择修改状态","error");
		        	return;
		        }
		        $.messager.progress();
		    },   
		    success:function(data){
		    	$.messager.progress('close');
		    	var data = eval('(' + data + ')');  // change the JSON string to javascript object   
		        if (data.status == 1){
					$('#s_data').datagrid('reload');
					$('#showOrderDetail').hide();
		        	$.messager.alert("提示","保存成功","success");
		        }else{
		        	$.messager.alert("提示",data.msg,"error");	
		        }
		    }   
		}); 
	
		$('#s_data').datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'pid',
			url : '${rc.contextPath}/order/findOrderOperation',
			loadMsg : '正在装载数据...',
			fitColumns : true,
			queryParams : {
				id : 1
			},
			remoteSort : true,
			singleSelect : true,
			pageSize : 50,
			pageList : [ 50, 80 ],
			columns : [ [
					{field : 'dateCreated', title : '操作时间', width : 70, align : 'center'},
					{field : 'autoMemo', title : '操作内容', width : 70, align : 'center'},
					{field : 'marks', title : '备注', width : 70, align : 'center'}
					] ],
			rownumbers : true,
			pagination : true
			});
	})
</script>

</body>
</html>