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
		<div data-options="region:'north',title:'供应商管理',split:true" style="height: 100px;">
			<input type="hidden" id="type" value="${type}">
			<form id="searchForm" method="post">
				<table cellpadding="15">
					<tr>
	                	<td>供应商名称：</td>
						<td><input id="id" name="id"/></td>
	                    <td>采购款结算方式：</td>
		                <td><select id="purchaseSubmitType" class="easyui-combobox"  style="width:150px;"><option value="0" selected="selected">-全部-</option>
			                <option value="1">款到发货</option><option value="2">预付定金，出货前付清</option>
			                <option value="3">预付定金，到货后天付清</option><option value="4">到货后后天付清</option><option value="5">其他</option>
		               		</select>
		                </td>
		                <td>备注：</td>
					    <td><input id="remark" name="remark"/></td>
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
	// 编辑页面
	function edit(id) {
		window.open('${rc.contextPath}/purchase/toSaveOrUpdateProvider?id=' + id);
	}
	$(function(){
		$('#searchBtn').click(function() {
			$('#s_data').datagrid('load', {
				id:$('#id').combobox('getValue'),
				remark:$('#remark').val(),
				purchaseSubmitType:$('#purchaseSubmitType').combobox('getValue')
			});
		});
		$('#id').combobox({
            url:'${rc.contextPath}/purchase/providerList?type='+$("#type").val(),
            valueField:'id',
            textField:'name',
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/purchase/providerListInfo?type='+$("#type").val(),
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'供应商编码', width:30, align:'center'},
                {field:'name',    title:'供应商名称', width:70, align:'center'},
                {field:'purchasingLeader',    title:'采购负责人', width:30, align:'center'},
                {field:'providerBrand',    title:'经营品牌', width:70, align:'center'},
                {field:'offices',    title:'公司所在地', width:70, align:'center'},
                {field:'currency',    title:'报价币种', width:30, align:'center',
                	formatter:function(value,row,index){
 						if(row.currency == 1)
 							return '人民币';
 						else if(row.currency == 2)
 							return '美元';
 						else if(row.currency == 3)
 							return '澳元';
 						else if(row.currency == 4)
 							return '日元';
 						else if(row.currency == 5)
 							return '港币';
 						else if(row.currency == 6)
 							return '欧元';
 						else if(row.currency == 7)
 							return '新元';
 						else if(row.currency == 8)
 							return '韩元';
 						else if(row.currency == 9)
 							return '英镑';
 						else 
 							return row.currency;
                	}},
                {field:'isInvoice',    title:'是否开票', width:30, align:'center',formatter:function(value,row,index){
 						if(row.isInvoice == 1)
 							return '是  ' + row.tax + '%';
 						else if(row.isInvoice == 0)
 							return '否';
 						else 
 							return row.isInvoice;
                	}},
                {field:'purchaseSubmitType',    title:'采购款结算方式', width:70, align:'center',
                	formatter:function(value,row,index){
 						if(row.purchaseSubmitType == 1)
 							return '款到发货';
 						else if(row.purchaseSubmitType == 2)
 							return '预付定金 ' + row.percent + '%，出货前付清';
 						else if(row.purchaseSubmitType == 3)
 							return '预付定金 ' + row.percent + '%，到货' + row.day + '天付清';
 						else if(row.purchaseSubmitType == 4)
 							return '到货后' + row.day + '天付清';
 						else if(row.purchaseSubmitType == 5)
 							return row.other;
 						else 
 							return row.purchaseSubmitType;
                	}},
                {field:'remark',    title:'备注', width:80, align:'center'},
                {field:'isAvailable',    title:'状态', width:20, align:'center',
                	formatter:function(value,row,index){
 						if(row.isAvailable == 1)
 							return '可用';
 						else if(row.isAvailable == 0)
 							return '停用';
 						else 
 							return row.isAvailable;
                	}},
                {field:'hidden',  title:'操作', width:20, align:'center',
					formatter : function(value, row, index) {
						return '<a href="javascript:void(0);" onclick="edit(' + row.id + ')">编辑</a>';
					}
				}]],
                toolbar : [ {
    				id : '_add',
    				text : '新增',
    				iconCls : 'icon-add',
    				handler : function() {
    					window.open('${rc.contextPath}/purchase/toSaveOrUpdateProvider');
    				}
    			} ],
			pagination : true
		});
	});
</script>

</body>
</html>