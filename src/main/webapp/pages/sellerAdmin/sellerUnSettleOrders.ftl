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
    ul a:link{
        text-decoration:none;
    }
    ul a:hover {
        color：black；
    }
    ul a:visited {
        color:black;
    }
    ul a:link {
        color:black;
    }
    ul a {
        color:black;
    }
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<#include "../sellerAdmin/menu.ftl" >
	</div>

	<div data-options="region:'center'" style="padding: 5px;">
			<div data-options="region:'north',title:'结算订单',split:true" style="height: 120px;">
				<form   action="${rc.contextPath}/sellerAdminOrder/exportSellerUnSettleOrders" id="searchForm" method="post" >
                    <input type=hidden id="sellerId" name="sellerId" value="${sellerId}" />
					<table class="search" cellpadding="10px" cellspacing="0px">
						<tr>
							<td class="searchText">
								结算订单时间：
								<input value="" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00'})"/>
								~
		 						<input value="" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'startTime\')}'})"/>
							</td>
							<td >
								<a id="searchBtn" onclick="exportData()" href="javascript:;" class="easyui-linkbutton" style="width:140px" >导出结算表</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
        <span style="color: red">换吧网络提醒您：导出结算表格后，请您务必保存好每次结算的表格记录，供后续财务对账、结款使用。</span>
	</div>

<script>
	function exportData(){
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if(startTime == '' || endTime == ''){
				$.messager.alert("友情提示","请填写完整时间信息。");
				return;
			}
			if(startTime > endTime){
				$.messager.alert("友情提示","起始时间不能大于结束时间。");
				return;
			}
		$("#searchForm").submit();	
	}
</script>

</body>
</html>