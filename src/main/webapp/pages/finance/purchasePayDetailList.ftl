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
		<div data-options="region:'north',title:'采购结款管理',split:true" style="height: 100px;">
			<form id="searchForm" method="post">
				<table style="margin-top: 15px">
					<tr>
	                	<td>采购单编号：</td>
						<td><input id="searchPurchaseCode" name="purchaseCode"/></td>
	                    <td>采购单状态：</td>
	                    <td><select id="searchStatus" class="easyui-combobox" style="width:150px;"><option value="0" selected="selected">-全部-</option>
			                <option value="1">新建</option><option value="2">已导出</option>
			                <option value="3">已入库</option><option value="4">已完结</option>
		               		</select>
		               	</td>
	                    <td>供应商名称：</td>
		                <td><input id="searchProviderId" class="easyui-combobox" style="width:150px;"></td>
		                <td>采购时间：</td>
		                <td><input id="searchStartTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'searchEndTime\')}'})" value="" />
							 ~ <input id="searchEndTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'searchStartTime\')}'})" value="" /></td>
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

	$(function(){
		$('#searchBtn').click(function() {
			$('#s_data').datagrid('load', {
				purchaseCode:$('#searchPurchaseCode').val(),
				status:$('#searchStatus').combobox('getValue'),
				providerId:$('#searchProviderId').combobox('getValue'),
				startTime:$('#searchStartTime').val(),
				endTime:$('#searchEndTime').val(),
			});
		});
		$('#searchProviderId').combobox({
			url:'${rc.contextPath}/purchase/providerList',
			valueField:'id',
			textField:'name',
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/purchase/findPurchasePayDetailByParam',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:15, align:'center'},
            	{field:'createTime',    title:'创建时间', width:25, align:'center'},
                {field:'purchaseCode',    title:'采购单编号', width:25, align:'center'},
                {field:'status',    title:'采购单状态', width:25, align:'center',
                	formatter:function(value,row,index){
 						if(row.status == 1)
 							return '新建';
 						else if(row.status == 2)
							return '已导出';
 						else if(row.status == 3)
							return '已入库';
 						else if(row.status == 4)
							return '已完结';
 						else if(row.status == 5)
							return '已采购';
 						return row.status;
                	}
                },
                {field:'name',    title:'供应商名称', width:50, align:'center'},
                {field:'currency',    title:'报价币种', width:15, align:'center',
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
                	{
        				field : 'type',
        				title : '付款类型',
        				width : 15,
        				align : 'center',
        				formatter : function(value, row, index) {
        					var a = '';
        					if(row.type.indexOf(1)!=-1)
        						a=' 定金 ';
        					if(row.type.indexOf(2)!=-1)
        						a+=' 货款 ';
        					if(row.type.indexOf(3)!=-1)
        						a+=' 运费 ';
        					return a;
        				}
        			}, {
        				field : 'payMoney',
        				title : '付款金额',
        				width : 15,
        				align : 'center'
        			}, {
        				field : 'currentRate',
        				title : '当日汇率',
        				width : 15,
        				align : 'center'
        			}, {
        				field : 'payRMB',
        				title : '付款金额/RMB',
        				width : 15,
        				align : 'center'
        			}, {
        				field : 'remark',
        				title : '付款备注',
        				width : 60,
        				align : 'center'
        			},
                {field:'hidden',  title:'操作', width:20, align:'center',
					formatter : function(value, row, index) {
						var a = '<a href="javascript:void(0);" onclick="detail(' + row.purchaseCode + ',' + row.storageId + ',' + row.providerId + ')">明细</a>';
						if(row.isPaid == 0) {
							a += '&nbsp;&nbsp;<a href="javascript:void(0);" onclick="paid(' + row.id + ')">确认</a>';
						}
						return a;
					}
				} ] ],
			pagination : true
		});
	});
	function detail(purchaseCode, storageId, providerId) {
		window.open('${rc.contextPath}/purchase/toOrderDetail?purchaseCode='+purchaseCode+'&storageId='+storageId+'&providerId='+providerId);
	}
	function paid(id) {
		$.messager.confirm('提示', '是否确定结款', function(r) {
			if(r) {
				$.ajax({
					url: '${rc.contextPath}/purchase/saveOrUpdatePurchasePayDetail',
					type:"POST",
					data:{id:id,isPaid:1},
					success: function(data) {
						if(data.status == 1) {
							$('#s_data').datagrid('load');
				            return;
				        } else{
				            $.messager.alert('响应信息',data.msg,'error');
				        }
					}
				});
			}
		});
	}
</script>

</body>
</html>