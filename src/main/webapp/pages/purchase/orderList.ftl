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
		<div data-options="region:'north',title:'采购单管理',split:true" style="height: 100px;">
			<input type="hidden" id="type" value="${type}">
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
		                <td>收货分仓：</td>
		                <td><input id="searchStorageId" class="easyui-combobox" style="width:150px;"></td>
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

<!-- 新增 begin -->
	   <div id="toCreatePurchaseDiv" class="easyui-dialog" style="width:500px;height:300px;padding:15px 20px;">
	       <form id="toCreatePurchaseForm" method="post">
			<p>
				<span>选择供应商：</span>
				<span><input type="text" id="providerId" name="providerId" class="easyui-combobox" style="width: 200px;"/></span>
			</p>
			<p>
				<span>选择收货仓库：</span>
				<span><input type="text" id="storageId" name="storageId" class="easyui-combobox" style="width: 200px;"/></span>
			</p>
	   	</form>
	   </div>
	   <!-- 新增 end -->
	    
<script>
	$(function() {
		$('#searchBtn').click(function() {
			$('#s_data').datagrid('load', {
				purchaseCode:$('#searchPurchaseCode').val(),
				status:$('#searchStatus').combobox('getValue'),
				providerId:$('#searchProviderId').combobox('getValue'),
				storageId:$('#searchStorageId').combobox('getValue'),
				startTime:$('#searchStartTime').val(),
				endTime:$('#searchEndTime').val(),
			});
		});
		$('#searchProviderId').combobox({
			url:'${rc.contextPath}/purchase/providerList?type='+$('#type').val(),
			valueField:'id',
			textField:'name',
		});
		$('#providerId').combobox({
			url:'${rc.contextPath}/purchase/providerList?type='+$('#type').val(),
			valueField:'id',
			textField:'name',
		});
		$('#searchStorageId').combobox({
			url:'${rc.contextPath}/purchase/storageList?type='+$('#type').val(),
			valueField:'id',
			textField:'name',
		});
		$('#storageId').combobox({
			url:'${rc.contextPath}/purchase/storageList?type='+$('#type').val(),
			valueField:'id',
			textField:'name',
		});
		
		$('#toCreatePurchaseDiv').dialog({
			title : '新建采购单',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
					if(valid()) {
						$('#toCreatePurchaseDiv').dialog('close');
						var providerId = $('#providerId').combobox('getValue');
						var storageId = $('#storageId').combobox('getValue');
						window.location.href='${rc.contextPath}/purchase/toSaveOrUpdateOrder?providerId='+providerId+'&storageId='+storageId;
					}
				}
			}, {
				text : '取消',
				align : 'left',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#toCreatePurchaseDiv').dialog('close');
				}
			} ]
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/purchase/orderListInfo?type='+$('#type').val(),
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'', width:5, align:'left', checkbox:true},
                {field:'purchaseCode',    title:'采购单编号', width:25, align:'center'},
                {field:'status',    title:'采购单状态', width:13, align:'center',
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
                	}
                },
                {field:'providerName',    title:'供应商名称', width:40, align:'center'},
                {field:'storageName',    title:'入库仓库', width:30, align:'center'},
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
                {field:'purchaseTotalCount',    title:'采购总数', width:12, align:'center'},
                {field:'totalMoney',  title:'合计金额',  width:15, align:'center',
                	formatter:function(value,row,index){
						return Number(row.totalMoney).toFixed(2);
        			}	
                },
                {field:'payableMoney',  title:'应付金额',  width:15, align:'center',
                	formatter:function(value,row,index){
							return Number(row.payableMoney).toFixed(2);
            		}	
                },
                {field:'adjustMoney',  title:'金额调整',  width:15, align:'center'},
                {field:'isAdjustAppendOrder',  title:'是否计入',  width:15, align:'center', 
                	formatter:function(value,row,index){
 						if(row.isAdjustAppendOrder == 1)
 							return '计入';
 						else if(row.isAdjustAppendOrder == 0)
 							return '不计入';
 							return row.isAdjustAppendOrder;
                	}
                },
                {field:'realMoney',  title:'实付金额',  width:15, align:'center',
                		formatter:function(value,row,index){
     							return Number(row.realMoney).toFixed(2);
                    	}
                	},
                {field:'paidMoney',  title:'已付金额',  width:15, align:'center',
                		formatter:function(value,row,index){
                			return Number(row.paidMoney).toFixed(2);
                    	}	
                },
                {field:'unpaidMoney',  title:'未付金额',  width:15, align:'center',
                	formatter:function(value,row,index){
 						if(row.status == 4)
 							return 0;
 						else
 							return Number(row.unpaidMoney).toFixed(2);;
                	}
                },
                {field:'paidRMB',  title:'已付金额/RMB',  width:18, align:'center'},
                /* {field:'remark',  title:'采购备注',  width:40, align:'center'},
                {field:'storingRemark',  title:'入库备注',  width:30, align:'center'}, */
                {field:'hidden',  title:'操作', width:30, align:'center',
					formatter : function(value, row, index) {
						var a = '';
						if(row.status > 2) {
							a = '<a href="javascript:void(0);" onclick="detail(' + row.purchaseCode + ',' + row.storageId + ',' + row.providerId + ')">明细</a>';
						}
						else if(row.status > 1) {
							a = '<a href="javascript:void(0);" onclick="detail(' + row.purchaseCode + ',' + row.storageId + ',' + row.providerId + ')">明细</a>';
							a += '&nbsp;&nbsp;<a href="javascript:void(0);" onclick="edit(' + row.purchaseCode + ',' + row.storageId + ',' + row.providerId + ')">修改</a>';
							a += '&nbsp;&nbsp;<a href="javascript:void(0);" onclick="update(' + row.id + ')">采购</a>';
						}
						else if(row.status > 0) {
							a = '<a href="javascript:void(0);" onclick="detail(' + row.purchaseCode + ',' + row.storageId + ',' + row.providerId + ')">明细</a>';
							a += '&nbsp;&nbsp;<a href="javascript:void(0);" onclick="edit(' + row.purchaseCode + ',' + row.storageId + ',' + row.providerId + ')">修改</a>';
							a += '&nbsp;&nbsp;<a href="javascript:void(0);" onclick="removeOrder(' + row.id + ')">删除</a>';
						}
						return a;
					}
				} ] ],
			toolbar : [ {
				id : '_add',
				text : '新增采购单',
				iconCls : 'icon-add',
				handler : function() {
					$('#toCreatePurchaseDiv').dialog('open');
				}
			},{
				id : '_export',
				text : '导出采购单',
				iconCls : 'icon-redo',
				handler : function() {
					var ids = new Array();
					var checkedRows = $('#s_data').datagrid('getChecked');
					if(checkedRows.length == 0) {
						$.messager.alert('提示', '未选中采购单', 'info');
						return ;
					}
					$.each(checkedRows, function(i, item) {
						ids.push(item.id);
					});
					window.open('${rc.contextPath}/purchase/exportOrder?param='+ids.join());
				}
			} ],
			pagination : true
		});
	});
	function valid() {
		if($('#providerId').combobox('getValue') == '') {
			$.messager.alert('警告', '供应商不能为空');
			return false;
		}
		if($('#storageId').combobox('getValue') == '') {
			$.messager.alert('警告', '收货仓库不能为空');
			return false;
		}
		return true;
	}
	function detail(purchaseCode, storageId, providerId) {
		window.open('${rc.contextPath}/purchase/toOrderDetail?storageId='+storageId+'&providerId='+providerId+'&purchaseCode='+purchaseCode);
	}
	function removeOrder(orderId) {
		$.messager.confirm('提示', '是否确定删除采购单', function(r) {
			if(r) {
				$.ajax({
					url: '${rc.contextPath}/purchase/deleteOrderById/' + orderId,
					type:"POST",
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
	function update(id) {
		$.messager.confirm('提示', '是否确定采购', function(r) {
			if(r) {
				$.ajax({
					url: '${rc.contextPath}/purchase/saveOrUpdateOrder/',
					type:"POST",
					data:{id:id,status:5},
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
	function edit(purchaseCode,storageId,providerId) {
		window.open('${rc.contextPath}/purchase/toSaveOrUpdateOrder?purchaseCode='+purchaseCode+'&storageId='+storageId+'&providerId='+providerId);
	}
</script>

</body>
</html>