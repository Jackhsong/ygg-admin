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

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true,title:'心动慈露管理'" >
		<div data-options="region:'north',title:'心动慈露管理',split:true" style="height: 100px;">
			<form id="searchForm" method="post">
				<table style="margin-top: 15px">
					<tr>
	                	<td>商品名称：</td>
						<td><input id="name" name="name"/></td>
		                <td>
							<a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',width:'80px'">查 询</a>
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
	$(function() {
		$('#searchBtn').click(function() {
			$('#s_data').datagrid('load', {
				name:$('#name').val(),
			});
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/gegetuan/listInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'团购ID', width:15, align:'center'},
                {field:'shortName',    title:' 团购商品名称', width:70, align:'center'},
                {field:'teamNumberLimit',    title:'需要成团人数', width:20, align:'center'},
                {field:'currentNumber',    title:'当前人数', width:15, align:'center'},
                {field:'status',    title:'团购状态', width:15, align:'center',
                	formatter : function(value, row, index) {
						return '进行中';
					}	
                },
                {field:'nickname',  title:'团成员',  width:70, align:'center',
                	formatter : function(value, row, index) {
                		var link = '';
                		var nicknameArr = row.nickname.split(',');
                		var orderNumberArr = row.orderNumber.split(',');
                		var orderIdArr = row.orderId.split(',');
                		for (var i = 0; i < nicknameArr.length; i++) {
                			link += nicknameArr[i] + '(<a href="javascript:void(0);" onclick="openWindow(\'${rc.contextPath}/order/detail/' + orderIdArr[i] + '\')">' + orderNumberArr[i] + '</a>)&nbsp;&nbsp;';
						}
						return link;
					}	
                },
                {field:'createTime',  title:'团创建时间',  width:30, align:'center'},
                {field:'teamEndTime',  title:'团结束时间',  width:30, align:'center'},
                {field:'link',  title:'链接',  width:70, align:'center',
                	formatter : function(value, row, index) {
						return '<a href="javascript:void(0);" onclick="openWindow(\'http://tuan.gegejia.com/yggGroupPurchase/product/teamInfo?mwebGroupProductId=' + row.mwebGroupProductInfoId + 
								'&mwebGroupProductInfoId=' + row.id + '\')">http://tuan.gegejia.com/yggGroupPurchase/product/teamInfo?mwebGroupProductId=' + row.mwebGroupProductInfoId + "&mwebGroupProductInfoId=" + row.id + '</a>';
						//return 'http://tuan.gegejia.com/yggGroupPurchase/product/teamInfo?mwebGroupProductId=' + row.mwebGroupProductInfoId + '&mwebGroupProductInfoId=' + row.id;
					}
                }] ],
			pagination : true
		});
	});
	function openWindow(url) {
		window.open(url);
	}
</script>

</body>
</html>