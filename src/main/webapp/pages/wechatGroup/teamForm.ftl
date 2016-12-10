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

<div data-options="region:'center',title:'新增修改团购信息'" style="padding:5px;">
	<form id="savebannerWindow" action="${rc.contextPath}/wechatGroup/updateProductForTeam"  method="post">
		<fieldset>
			<input type="hidden" value="1" id="editId" name="editId" />
			<legend>商品信息</legend>
			<#if product.productId?exists>
			商品ID: <input type="text" id="productId" name="productId" style="width:300px;" readonly="readonly" value="${product.productId?c}"/><span style="color:red">必须</span><br/><br/>
			<#else>
			商品ID: <input type="text" id="productId" name="productId" style="width:300px;"/><span style="color:red">必须</span><br/><br/>
			</#if>
			长名称: <span id="name"><#if product.name?exists>${product.name}</#if></span><br/><br/>
			短名称: <span id="shortName"><#if product.shortName?exists>${product.shortName}</#if></span><br/><br/>
			核心亮点: <span id="sellingPoint"><#if product.sellingPoint?exists>${product.sellingPoint}</#if></span><br/><br/>
			排序值：<span><input type="text" id="order" name="order"  style="width:70px;" value="<#if product.order?exists>${product.order?c}</#if>" /></span><br/><br/>
			开始时间:<input value="<#if product.startTime?exists >${product.startTime}</#if>" id="startTime" name="startTime" onClick="doWPStart()"/>&nbsp;&nbsp;&nbsp;
			结束时间:<input value="<#if product.endTime?exists >${product.endTime}</#if>" id="endTime" name="endTime" onClick="doWPEnd()"/>
			<span style="color:red">必须</span><br/><br/>
			
			 展现状态:
			<#if product.isAvailable?exists && (product.isAvailable == 1) >
				<input type="radio" name="isAvailable" value="1" checked>展现&nbsp;&nbsp;&nbsp;
				<input type="radio" name="isAvailable" value="0">不展现<br/><br/>
			<#else>
				<input type="radio" name="isAvailable" value="1">展现&nbsp;&nbsp;&nbsp;
				<input type="radio" name="isAvailable" value="0" checked>不展现<br/><br/>
			</#if>
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
</div>

<script type="text/javascript">
	
function doWPStart(){
	WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'});
}

function doWPEnd(){
	WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\',{d:1})}'})
}


	$(function(){
		$("#saveButton").click(function(){
			var productId = $("#productId").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
		    var order = $("#order").val();
			
			if(productId == '') {
				$.messager.alert('提示','请填写商品id');
				return false;
			}
			if(order == '') {
				$.messager.alert('提示','请填写排序值');
				return false;
			}
			if(startTime == '' || endTime == '') {
				$.messager.alert('提示','请选择开始结束时间');
				return false;
			}
			$('#savebannerWindow').submit();
    	});
		
		$('#productId').change(function(){
			var productId = $('#productId').val();
			if(productId == ""){
				$('#name').text("");
				$('#shortName').text("");
				$('#sellingPoint').text("");
			}else{
				$.ajax({
	                url: '${rc.contextPath}/wechatGroup/jsonProductInfoForTeamById',
	                type: 'post',
	                dataType: 'json',
	                data: {'productId':productId},
	                success: function(data) {
	                    if(data.status == 1) {
	                    	$('#name').text(data.data.name);
	        				$('#shortName').text(data.data.shortName);
	        				$('#sellingPoint').text(data.data.sellingPoint);
	                    }else{
	                    	$.messager.alert("提示",data.msg,"info");
	                    	$("#productId").val('')
	                    	$('#name').text("");
	        				$('#shortName').text("");
	        				$('#sellingPoint').text("");
	                    }
	                },
	                error: function(xhr){
	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                }
	            });
			}
		});
	});
</script>

</body>
</html>