<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>商家结算管理</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1340px;
	align:center;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">

	<div id="cc" class="easyui-layout" data-options="fit:true" >
	
		<div data-options="region:'north',title:'条件筛选-商家结算管理',split:true" style="height: 100px;">
			<form id="searchForm" method="post" action="${rc.contextPath}/finance/exportSellerSettlementPeriodData" >
            <table cellspacing="5" cellpadding="5">
            	<tr>
					<td class="searchName">&nbsp;商家：</td>
					<td class="searchText">
						<input type="text" id="sellerId" name="sellerIds" style="width:300px" value=""/>
					</td>
					<td>
						&nbsp;距离结算日天数小于
                        <input id="remainsDays" name="remainsDays" style="width:50px" value=""/>
						天
					</td>
                    <td>
                        &nbsp;
                        <a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" >查询</a>
                    </td>
                    <td>
                        <a id="exportBtn" onclick="exportOrder()" href="javascript:;" class="easyui-linkbutton" >导出结算清单</a>
                    </td>
            	</tr>
            </table>
        </form>
		</div>
		
		<div data-options="region:'center'" >
	    	<!--数据表格-->
	    	<table id="s_data" style=""></table>
    	</div>
	</div>
	
</div>

<script>

$(function(){
	$(document).keydown(function(e){
		if (!e){
		   e = window.event;  
		}  
		if ((e.keyCode || e.which) == 13) {  
		      $("#searchBtn").click();  
		}
	});
});

	function searchOrder() {
        var sellerIds = $('#sellerId').combobox('getValues').toString();
		var remainsDays = $('#remainsDays').val();
        $('#s_data').datagrid('load', {
            sellerIds : sellerIds,
            remainsDays : remainsDays
        });
	}

	function exportOrder() {
		$("#searchForm").submit();
	}
	
	$(function(){
		
		$('#s_data') .datagrid(
				{
					nowrap : false,
					striped : true,
					collapsible : true,
					idField : 'id',
					url : '${rc.contextPath}/finance/jsonSellerSettlementPeriodData',
					loadMsg : '正在装载数据...',
					fitColumns : true,
					remoteSort : true,
					singleSelect : true,
					columns : [ [
							{field : 'sellerId', title : '商家ID', width : 30, align : 'center'},
							{field : 'realSellerName', title : '商家真实名称', width : 40, align : 'center'},
							{field : 'sendAddress', title : '发货地', width : 40, align : 'center'},
							{field : 'earliestTime', title : '最早未结算订单时间', width : 40, align : 'center'},
							{field : 'totalNum', title : '未结算订单总数量', width : 40, align : 'center'},
							{field : 'totalRealPrice', title : '未结算订单总金额', width : 40, align : 'center'},
							{field : 'settlementRule', title : '结算规则', width : 40, align : 'center'},
							{field : 'activityCycleDuration', title : '活动周期时长', width : 35, align : 'center'},
							{field : 'settlementTime', title : '结算日', width : 40, align : 'center'},
							{field : 'remainsDays', title : '距离结算日还有几天', width : 40, align : 'center'}
							] ],
					rownumbers : true
				});
		
		$('#sellerId').combobox({
		    url:'${rc.contextPath}/seller/jsonSellerCode',
		    valueField:'code',   
		    textField:'text',
		    panelHeight:500,
            panelHeight:500,
            mode:'remote',
            multiple:true
        });
	});

</script>

</body>
</html>