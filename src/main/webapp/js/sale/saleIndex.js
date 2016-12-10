$(function() {
    var rcContextPath=$('#rcContextPath').val();
	$('#s_data')
			.datagrid(
					{
						nowrap : false,
						striped : true,
						collapsible : true,
						idField : 'id',
						url : rcContextPath + '/indexSale/jsonSaleWindowInfo',
						loadMsg : '正在装载数据...',
						singleSelect : true,
						fitColumns : true,
						remoteSort : true,
						pageSize : 50,
						pageList : [ 50, 60 ],
						columns : [ [
								{
									field : 'id',
									title : '特卖ID',
									width : 20,
									align : 'center'
								},
								{
									field : 'displayId',
									title : '组特或单品ID',
									width : 30,
									align : 'center'
								},
								{
									field : 'nowOrder',
									title : '今排序值',
									width : 30,
									align : 'center'
								},
								{
									field : 'laterOrder',
									title : '明排序值',
									width : 30,
									align : 'center'
								},
								{
									field : 'isDisplay',
									title : '展现状态',
									width : 30,
									align : 'center',
									formatter : function(value, row, index) {
										if (row.isDisplay == 1) {
											return '展现';
										} else {
											return '不展现';
										}
									}
								},
								{
									field : 'saleStatus',
									title : '特卖状态',
									width : 30,
									align : 'center'
								},
								{
									field : 'saleTimeType',
									title : '场次',
									width : 30,
									align : 'center'
								},
								{
									field : 'saleType',
									title : '特卖类型',
									width : 30,
									align : 'center'
								},
								{
									field : 'relaRealSellerName',
									title : '关联商家',
									width : 60,
									align : 'center'
								},
								{
									field : 'name',
									title : '名称',
									width : 50,
									align : 'center'
								},
								{
									field : 'desc',
									title : '特卖描述',
									width : 50,
									align : 'center'
								},
								{
									field : 'categoryFirstName',
									title : '所属分类',
									width : 50,
									align : 'center'
								},
								{
									field : 'stock',
									title : '库存数量',
									width : 25,
									align : 'center'
								},
								{
									field : 'startTime',
									title : '开始时间',
									width : 50,
									align : 'center'
								},
								{
									field : 'endTime',
									title : '结束时间',
									width : 50,
									align : 'center'
								},
								{
									field : 'url',
									title : '查看',
									width : 20,
									align : 'center',
									formatter : function(value, row, index) {
										return '<a href="' + row.url
												+ '" target="_blank">查看</a>'
									}
								},
								{
									field : 'hidden',
									title : '操作',
									width : 80,
									align : 'center',
									formatter : function(value, row, index) {
										var a = '<a href="javaScript:;" onclick="editIt('
												+ index + ')">修改</a> | ';
										var b = '';
										if (row.isDisplay == 1) {
											b = '<a href="javaScript:;" onclick="displayIt('
													+ '\''
													+ row.id
													+ '\','
													+ 0
													+ ')">不展现</a>';
										} else {
											b = '<a href="javaScript:;" onclick="displayIt('
													+ '\''
													+ row.id
													+ '\','
													+ 1
													+ ')">展现</a>';
										}
										var c = '';
										if (row.type == 1) {
											c = ' | <a href="javaScript:;" onclick="editStock('
													+ '\''
													+ index
													+ '\')">改库存</a>';
										} else {
											c = ' | ______';
										}
										return a + b + c;
									}
								} ] ],
						toolbar : [ {
							id : '_add',
							text : '新增特卖',
							iconCls : 'icon-add',
							handler : function() {
								window.location.href = rcContextPath
										+ "/indexSale/addSale"
							}
						} ],
						pagination : true
					});

	$('#brandId').combobox({
		panelWidth : 350,
		panelHeight : 350,
		url : rcContextPath + '/brand/jsonBrandCode',
		valueField : 'code',
		textField : 'text'
	});

	$('#sellerId').combobox({
		panelWidth : 350,
		panelHeight : 350,
		mode : 'remote',
		url : rcContextPath + '/seller/jsonSellerCode',
		valueField : 'code',
		textField : 'text'
	});

	$('#s_saleData')
			.datagrid(
					{
						nowrap : false,
						striped : true,
						collapsible : true,
						idField : 'id',
						url : rcContextPath + '/productBase/jsonProductInfo',
						loadMsg : '正在装载数据...',
						fitColumns : true,
						remoteSort : true,
						pageSize : 30,
						pageList : [ 30, 40 ],
						columns : [ [
								{
									field : 'id',
									title : '商品Id',
									width : 20,
									align : 'center'
								},
								{
									field : 'name',
									title : '商品名称',
									width : 90,
									align : 'center'
								},
								{
									field : 'type',
									title : '商品类型',
									width : 30,
									align : 'center'
								},
								{
									field : 'time',
									title : '特卖时间',
									width : 50,
									align : 'center'
								},
								{
									field : 'remark',
									title : '商品备注',
									width : 60,
									align : 'center'
								},
								{
									field : 'addStock',
									title : '增加库存',
									width : 20,
									align : 'center',
									editor : {
										type : 'validatebox',
										options : {
											required : true
										}
									}
								},
								{
									field : 'stock',
									title : '库存',
									width : 20,
									align : 'center'
								},
								{
									field : 'available',
									title : '剩余可用',
									width : 20,
									align : 'center'
								},
								{
									field : 'lock',
									title : '已锁定',
									width : 20,
									align : 'center'
								},
								{
									field : 'isAvailable',
									title : '是否可用',
									width : 20,
									align : 'center'
								},
								{
									field : 'hidden',
									title : '操作',
									width : 40,
									align : 'center',
									formatter : function(value, row, index) {
										if (row.editing) {
											var s = '<a href="javascript:void(0);" onclick="saveProductStock('
													+ index + ')">保存</a> ';
											var c = '<a href="javascript:void(0);" onclick="cancelProductStock('
													+ index + ')">取消</a>';
											return s + c;
										} else {
											var a = '<a href="javascript:;" onclick="editProductStock('
													+ index + ')">调库存</a> | ';
											var b = '<a href="' + rcContextPath
													+ '/product/edit/'
													+ row.typeCode + '/'
													+ row.id
													+ '" targe="_blank">查看</a>'
											return a + b;
										}
									}
								} ] ],
						onBeforeEdit : function(index, row) {
							row.editing = true;
							updateProductStockAction();
						},
						onAfterEdit : function(index, row) {
							row.editing = false;
							updateProductStockAction();
							updateProductStockActivity(row);
						},
						onCancelEdit : function(index, row) {
							row.editing = false;
							updateProductStockAction();
						},
						pagination : true
					});

	$('#editStock_div').dialog({
		title : '调库存',
		collapsible : true,
		closed : true,
		modal : true,
		buttons : [ {
			text : '取消',
			align : 'left',
			iconCls : 'icon-cancel',
			handler : function() {
				var domId = $('#dataId').val();
				$('#editStock_div').dialog('close');
				$('#' + domId).datagrid('reload');
			}
		} ]
	});
});