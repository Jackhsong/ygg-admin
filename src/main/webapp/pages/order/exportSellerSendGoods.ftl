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

</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center'" style="padding: 5px;">
			<div data-options="region:'north',title:'订单发货',split:true" style="height: 120px;">
				<form action="${rc.contextPath}/order/exportSellerSendGoods" id="searchForm" method="post" >
					<table class="search" cellpadding="10px" cellspacing="0px">
						<tr>
							<td >
								<input type="checkbox" value="1" checked disabled /> 订单付款时间
								<input type="checkbox"  value="1" checked disabled /> 订单解冻时间
							</td>
							<td ></td>
						</tr>
						<tr>
							<td class="searchText">
								<input type="radio" name="timeType" value="1" />最近一个阶段
								<input value="${startTimeBegin}" id="startTime1" name="startTime1" readonly/>
		 						~
		 						<input value="${startTimeEnd}" id="endTime1" name="endTime1" readonly />
							</td>
							<td class="searchText">
								<input type="radio" name="timeType" value="2" checked />自定义时间：
								<input value="" id="startTime2" name="startTime2" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								~
		 						<input value="" id="endTime2" name="endTime2" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime2\')}'})"/>
							</td>
						</tr>
						<tr>
							<td >
								<a id="searchBtn" onclick="exportData()" href="javascript:;" class="easyui-linkbutton" style="width:140px" >导出商家发货表</a>
							</td>
							<td ></td>
						</tr>
					</table>
				</form>
			</div>
	</div>

<script>
	function exportData(){
		var timeType = $("input[name='timeType']:checked").val();
		if(timeType == 2){
			var startTime2 = $("#startTime2").val();
			if(startTime2 == ''){
				$.messager.alert("info","您选择了自定义时间时，请填写完整时间信息。");
				return;
			}
		}
		$("#searchForm").submit();	
	}
</script>

</body>
</html>