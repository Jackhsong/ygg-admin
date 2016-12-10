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
<div data-options="region:'center'" style="padding:10px 60px 20px 60px">
	<div data-options="region:'north',title:'修改采购单信息',split:true,height:'auto'">
		<form id="editOrderForm" method="post">
			<p><input type="hidden" name="id" value="<#if purchase?? && purchase.id??>${purchase.id}<#else>0</#if>"></p>
			<p><input type="hidden" name="storageId" id="storageId" value="<#if storage?? && storage.id??>${storage.id}</#if>"></p>
			<p><input type="hidden" name="providerId" id="providerId" value="<#if provider?? && provider.id??>${provider.id}</#if>"></p>
			<p><input type="hidden" name="purchaseCode" value="<#if purchaseCode??>${purchaseCode}</#if>"></p>
			<p><span>采购单编号：</span><#if purchaseCode??><span id="purchaseCode">${purchaseCode}</span>
				<#else><span style="font-size: 100px;color: red">采购单已经被删除了。。。</span>
				</#if>
			</p>
			<p><span>采购单说明：</span><input maxlength="100" style="width:700px;" name="desc" value="<#if purchase?? && purchase.desc??>${purchase.desc}</#if>" /></p>
			<br>
			<p><span style="font-weight:bold;" >采购方信息</span></p>
			<p><span>收货仓库：</span><#if storage?? && storage.name??>${storage.name}</#if></p>
			<p><span>详细地址：</span><#if storage?? && storage.detailAddress??>${storage.detailAddress}</#if></p>
			<p><span>交货联系人：</span><#if storage?? && storage.contactPerson??>${storage.contactPerson}</#if></p>
			<p><span>联系方式：</span><#if storage?? && storage.contactPhone??>${storage.contactPhone}</#if></p>
			<br>
			<p><span style="font-weight:bold;" >供应商信息</span></p>
			<p><span>供应商名称：</span><#if provider?? && provider.name??>${provider.name}</#if></p>
			<p><span>公司名称：</span><#if provider?? && provider.companyName??>${provider.companyName}</#if></p>
			<p><span>报价币种：</span>
				<#if provider?? && provider.currency?? && provider.currency == 1>人民币
				<#elseif provider?? && provider.currency?? && provider.currency == 2>美元
				<#elseif provider?? && provider.currency?? && provider.currency == 3>澳元
				<#elseif provider?? && provider.currency?? && provider.currency == 4>日元
				<#elseif provider?? && provider.currency?? && provider.currency == 5>港币
				<#elseif provider?? && provider.currency?? && provider.currency == 6>欧元
				<#elseif provider?? && provider.currency?? && provider.currency == 7>新元
				<#elseif provider?? && provider.currency?? && provider.currency == 8>韩元
				<#elseif provider?? && provider.currency?? && provider.currency == 9>英镑
				</#if>
			</p>
			<p><span>联系人：</span><#if provider?? && provider.contactPerson??>${provider.contactPerson}</#if></p>
			<p><span>联系电话：</span><#if provider?? && provider.contactPhone??>${provider.contactPhone}</#if></p>
			<p><span>采购款结算：</span>
				<#if provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 1>款到发货
				<#elseif provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 2 && provider.percent??>预付定金${provider.percent}%，出货前付清
				<#elseif provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 3 && provider.percent?? && provider.day??>预付定金${provider.percent}%，出货${provider.day}天付清
				<#elseif provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 4 && provider.day??>到货后${provider.day}天付清
				<#elseif provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 5 && provider.other??>${provider.other}
				</#if>
			</p>
			<p><span>收款账号：</span><#if provider?? && provider.receiveBankAccount??>${provider.receiveBankAccount}</#if></p>
			<p><span>开户行：</span><#if provider?? && provider.receiveBank??>${provider.receiveBank}</#if></p>
			<p><span>收款人：</span><#if provider?? && provider.receiveName??>${provider.receiveName}</#if></p>
			<p><span>Swift code：</span><#if provider?? && provider.swiftCode??>${provider.swiftCode}</#if></p>
			<p><span>银行地址：</span><#if provider?? && provider.bankAddress??>${provider.bankAddress}</#if></p>
			<br>
			<a class="easyui-linkbutton" onclick="purchaseOrderManager()">管理采购单商品</a>
			<br>
			<p><input type="hidden" name="purchaseTotalCount" value="<#if purchase?? && purchase.purchaseTotalCount??>${purchase.purchaseTotalCount?string('#######.##')}</#if>"></p>
			<p><span>采购总数：</span><span id="purchaseTotalCount" ><#if purchase?? && purchase.purchaseTotalCount??>${purchase.purchaseTotalCount?string('#######.##')}</#if></span></p>
			<p><input type="hidden" name="purchaseTotalMoney" value="<#if purchase_purchaseTotalMoney??>${purchase_purchaseTotalMoney}</#if>"></p>
			<p><span>采购总金额：</span><span id="purchaseTotalMoney" ><#if purchase_purchaseTotalMoney??>${purchase_purchaseTotalMoney}</#if></span></p>
			<p><input type="hidden" name="freightMoney" value="<#if purchase?? && purchase.freightMoney??>${purchase.freightMoney?string('#######.##')}</#if>"></p>
			<p><span>运费：</span><span style="display:inline-block; width: 100px;" id="freightMoney"><#if purchase?? && purchase.freightMoney??>${purchase.freightMoney?string('#######.##')}</#if></span>
				<a class="easyui-linkbutton" onclick="editFreightMoney()">修改运费</a>
			</p>
			<p><input type="hidden" name="totalMoney" value="<#if purchase_totalMoney??>${purchase_totalMoney}</#if>"></p>
			<p><span>合计金额：</span><span id="totalMoney" ><#if purchase_totalMoney??>${purchase_totalMoney}</#if></span></p>
			<p><span>是否含税：</span>
			<#if purchase?? && purchase.isTax?? && purchase.isTax == 1><span><input type="radio" name="isTax" checked="checked" value="1"/>含税</span>
			<span><input type="radio" name="isTax" value="0"/>不含税</span>
			<#else><span><input type="radio" name="isTax" value="1"/>含税</span>
			<span><input type="radio" name="isTax" checked="checked" value="0"/>不含税</span>
			</#if>
			</p>
			<p><input type="hidden" name="payableMoney" value="<#if purchase_payableMoney??>${purchase_payableMoney}</#if>"></p>
			<p><span style="font-weight:bold;" >应付金额：</span><span style="display:inline-block; width: 100px;" id="payableMoney"><#if purchase_payableMoney??>${purchase_payableMoney}</#if></span>
				<a class="easyui-linkbutton" onclick="adjustPayableMoney()">修改应付金额</a>
			</p>
			<p><input type="hidden" name="realMoney" id="realMoney" value="<#if purchase_realMoney??>${purchase_realMoney}</#if>"></p>
			<p><input type="hidden" name="payableMoneyRemark" value="<#if purchase?? && purchase.payableMoneyRemark??>${purchase.payableMoneyRemark}</#if>"></p>
			<p><span>金额调整备注：</span><span id="payableMoneyRemark"><#if purchase?? && purchase.payableMoneyRemark??>${purchase.payableMoneyRemark}</#if></span></p>
			<p><span>采购备注：</span><input style="width:700px;" type="text" name="remark" value="<#if purchase?? && purchase.remark??>${purchase.remark}</#if>" maxlength="100"></p>
		</form>
		<a class="easyui-linkbutton" style="height: 40px; width: 100px;" onclick="createOrder()">保存采购单</a>
		<br>
		<br>
		<br>
		<br>
	</div>
</div>
<div id="editFreightMoneyDiv" class="easyui-dialog" style="width:400px;height:200px;padding:10px 60px 20px 60px">
	<form action="">
		<span>运费：</span><input onkeyup="money('newFreightMoney')" type="text" id="newFreightMoney" />
	</form>
	
</div>
<div id="editPayableMoneyDiv" class="easyui-dialog" style="width:400px;height:200px;padding:10px 60px 20px 60px">
	<form action="">
		<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;应付金额：</span><input onkeyup="money6('newPayableMoney')" type="text" id="newPayableMoney" /><br><br>
		<span>金额调整备注：</span><input type="text" id="newPayableMoneyRemark" maxlength="100"/>
	</form>
</div>
<div id="purchaseProductManagerDiv" title="采购单商品列表" class="easyui-dialog" modal="true" closable="false" closed="true" style="width:95%;height:95%;">
	<table id="s_purchaseProduct" style=""></table>
</div>
<div id="providerProductDiv" title="采购单可以新增的商品列表" class="easyui-dialog" modal="true" closed="true" style="width:90%;height:90%;">
	<div data-options="region:'north',title:'',split:true">
		<form id="searchForm" method="post">
			<table cellpadding="15">
				<tr>
                	<td>供应商名称：</td>
					<td><#if provider?? && provider.name??>${provider.name}</#if></td>
                    <td>品牌：</td>
                    <td><input id="brandId" name="brandId" style="width:150px;" /></td>
                    <td>采购商品ID：</td>
	                <td><input name="productId"/></td>
                </tr>
                <tr>
                	<td>入库仓库：</td>
                	<td><#if storage?? && storage.name??>${storage.name}</#if></td>
                    <td>商品条码：</td>
                    <td><input name="barcode"/></td>
                    <td>商品名称：</td>
	                <td><input name="productName"/></td>
	                <td>
						<a onclick="searchProviderProduct()" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',width:'80px'">查 询</a>
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
<div align="left" id="orderProductDiv" class="easyui-dialog" align="center" data-options="iconCls:'icon-save'" style="padding: 5px; width: 320px; height: 250px;">
    <div>
    	<form id="importOrderProductForm" method="post" enctype="multipart/form-data">
       		<input type="file" name="orderProductFile" />&nbsp;&nbsp;<br/><br/>
    	</form>
    </div>
</div>
<div align="left" id="orderNewProductDiv" class="easyui-dialog" align="center" data-options="iconCls:'icon-save'" style="padding: 5px; width: 320px; height: 250px;">
    <div>
    	<form id="importNewOrderProductForm" method="post" enctype="multipart/form-data">
    		<input type="hidden" name="purchaseCode">
    		<input type="hidden" name="providerId">
       		<input type="file" name="orderProductFile" />&nbsp;&nbsp;<br/><br/>
    	</form>
    </div>
</div>
<script>
var multiple = 1000000;
function valid() {
	return true;
}
function money(id) {
	$("#" + id).keyup(function () {
        var reg = $(this).val().match(/\d+\.?\d{0,2}/);
        var txt = '';
        if (reg != null) {
            txt = reg[0];
        }
        $(this).val(txt);
    }).change(function () {
        $(this).keypress();
        var v = $(this).val();
        if (/\.$/.test(v))
        {
            $(this).val(v.substr(0, v.length - 1));
        }
    });
}
function money6(id) {
	$("#" + id).keyup(function () {
        var reg = $(this).val().match(/\d+\.?\d{0,6}/);
        var txt = '';
        if (reg != null) {
            txt = reg[0];
        }
        $(this).val(txt);
    }).change(function () {
        $(this).keypress();
        var v = $(this).val();
        if (/\.$/.test(v))
        {
            $(this).val(v.substr(0, v.length - 1));
        }
    });
}
function createOrder() {
	$('#editOrderForm').form('submit', {
		url:'${rc.contextPath}/purchase/saveOrUpdateOrder',
		onSubmit: function() {
			return valid();
		},
		success: function(data) {
			var res = eval("("+data+")");
			if (res.status == 1) {
				window.location.href='${rc.contextPath}/purchase/toOrderList';
			} else if (res.status == 0) {
				$.messager.alert('响应信息', res.msg, 'error');
			}
		}
	});
}
function editFreightMoney() {
	$('#newFreightMoney').val($('#freightMoney').text());
	$('#editFreightMoneyDiv').dialog('open');
}
function adjustPayableMoney() {
	$('#newPayableMoney').val($('#payableMoney').text());
	$('#newPayableMoneyRemark').val($('#payableMoneyRemark').text());
	$('#editPayableMoneyDiv').dialog('open');
}
function purchaseOrderManager() {
	var purchaseCode = $('#purchaseCode').text();
	// 采购单的商品列表
	$('#s_purchaseProduct').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/purchase/findOrderProductListInfo?purchaseCode='+purchaseCode,
        loadMsg:'正在装载数据...',
        singleSelect:false,
        fitColumns:true,
        remoteSort: true,
        pageSize:150,
        columns:[[
        	// {field:'id',  title:'', width:20, align:'left', checkbox:true},
            {field:'barcode',   title:'商品条码', width:40, align:'center',editor:{type:'validatebox'}},
            {field:'productName',    title:'商品名称', width:80, align:'center'},
            {field:'brandName',    title:'品牌', width:40, align:'center'},
            {field:'purchaseQuantity',  title:'采购数',  width:30, align:'center',editor:{type:'numberbox'}},
            {field:'providerPrice',  title:'供货单价',  width:30, align:'center',editor:{type:'text'}},
            {field:'totalPrice',  title:'合计金额',  width:30, align:'center'},
            {field:'specification',  title:'规格',  width:30, align:'center'},
            {field:'purchaseUnit',  title:'采购单位',  width:30, align:'center'},
            {field:'boxSpecification',  title:'箱规',  width:30, align:'center'},
            {field:'manufacturerDate',  title:'生产日期',  width:30, align:'center',editor:{type:'datebox'}},
            {field:'durabilityPeriod',  title:'保质期',  width:30, align:'center',editor:{type:'text'}},
            {field:'hidden',  title:'操作', width:30, align:'center',
            	formatter : function(value, row, index) {
					if (row.editing) {
						var a = '<a href="javascript:void(0);" onclick="saverow(' + index + ')">保存</a> | ';
						var b = '<a href="javascript:void(0);" onclick="cancelrow(' + index + ')">取消</a>';
						return a + b;
					} else {
						var a = '<a href="javascript:void(0);" onclick="edit(' + index + ')">编辑</a> | ';
						var b = '<a href="javascript:void(0);" onclick="removeProduct(' + row.id + ', ' + index + ')">删除</a> ';
						return a + b;
					}
				}
			} ] ],
		toolbar : [{
			id : '_back',
			text : '保存并返回修改采购单页面',
			iconCls : 'icon-add',
			handler : function() {
				var data = $('#s_purchaseProduct').datagrid('getData');
				var rows = data.rows;
				var purchaseTotalCount = 0;
				var purchaseTotalMoney = 0;
				var flag = true;
				$.each(rows, function(index,item) {
					if(Number(item.purchaseQuantity) == 0 || Number(item.totalPrice) == 0) {
						flag = false;
					}
					purchaseTotalCount = purchaseTotalCount + Number(item.purchaseQuantity);
					purchaseTotalMoney = (purchaseTotalMoney * multiple + Number(item.totalPrice) * multiple) / multiple;
				});
				var freightMoney = Number($('#freightMoney').text());
				// 合计金额
				var totalMoney = ((purchaseTotalMoney * multiple + freightMoney * multiple) / multiple).toFixed(6);
				// 本次改变的金额
				var updateMoney = ((totalMoney * multiple - Number($('#totalMoney').text()) * multiple) / multiple).toFixed(6);
				// 应付金额
				var payableMoney = ((Number($('#payableMoney').text()) * multiple + Number(updateMoney) * multiple) / multiple).toFixed(6);
				// 实付金额
				var realMoney = ((Number($('#realMoney').val()) * multiple + Number(updateMoney) * multiple) / multiple).toFixed(6);
				if(!flag) {
					$.messager.confirm('提示', '采购数、供货单价等关键信息未填写，是否确认保存。', function(r) {
						if(r) {
							$('#purchaseTotalCount').text(purchaseTotalCount);
							$('input[name=purchaseTotalCount]').val(purchaseTotalCount);
							$('#purchaseTotalMoney').text(purchaseTotalMoney);
							$('input[name=purchaseTotalMoney]').val(purchaseTotalMoney);
							$('#payableMoney').text(payableMoney);
							$('input[name=payableMoney]').val(payableMoney);
							$('#totalMoney').text(totalMoney);
							$('input[name=totalMoney]').val(totalMoney);
							// 更新实付金额
							$('#realMoney').val(realMoney);
							$('#purchaseProductManagerDiv').dialog('close');
						}
					});
				} else {
					$('#purchaseTotalCount').text(purchaseTotalCount);
					$('input[name=purchaseTotalCount]').val(purchaseTotalCount);
					$('#purchaseTotalMoney').text(purchaseTotalMoney);
					$('input[name=purchaseTotalMoney]').val(purchaseTotalMoney);
					$('#payableMoney').text(payableMoney);
					$('input[name=payableMoney]').val(payableMoney);
					$('#totalMoney').text(totalMoney);
					$('input[name=totalMoney]').val(totalMoney);
					// 更新实付金额
					$('#realMoney').val(realMoney);
					$('#purchaseProductManagerDiv').dialog('close');
				}
			}
		},{
			id : '_add',
			text : '增加商品',
			iconCls : 'icon-add',
			handler : function() {
				addProviderProduct4order();
			}
		},{
			id : '_exportOrderProduct',
			text : '导出商品列表',
			iconCls : 'icon-redo',
			handler : function() {
				$.messager.confirm('提示', '确定导出商品列表', function(r) {
					if(r) {
						window.open('${rc.contextPath}/purchase/findOrderProductListInfo?purchaseCode='+$('#purchaseCode').text()+'&isExport=1');
					}
				});
			}
		},{
			id : '_importOrderProduct',
			text : '导入商品列表（更新）',
			iconCls : 'icon-edit',
			handler : function() {
				$('#orderProductDiv').dialog('open');
			}
		},{
			id : '_importNewOrderProduct',
			text : '导入商品列表（新增）',
			iconCls : 'icon-edit',
			handler : function() {
				$('#orderNewProductDiv').dialog('open');
			}
		}],
		onBeforeEdit:function(index,row){
        	row.editing = true;
        	updateActions();
    	},
    	onAfterEdit:function(index,row){
        	row.editing = false;
        	updateActions();
        	updateActivity(row);
    	},
    	onCancelEdit:function(index,row){
        	row.editing = false;
        	updateActions();
    	}
	});
	$('#purchaseProductManagerDiv').dialog('open');
}
// 删除采购单商品
function removeProduct(ids, index) {
	$.messager.confirm('提示', '是否确定删除该商品', function(r) {
		if(r) {
			$.ajax({
				url: '${rc.contextPath}/purchase/removeOrderProduct',
				type:"POST",
				data: {ids:ids},
				success: function(data) {
					if(data.status == 1){
						purchaseOrderManager();
			            return
			        } else{
			            $.messager.alert('响应信息',data.msg,'error');
			        }
				}
			});
		}
	})
}
//跟新导航排序值
function updateActivity(row){
	$.ajax({
		url: '${rc.contextPath}/purchase/updateOrderProduct',
		type:"POST",
		data: {id:row.id,barcode:row.barcode,purchaseQuantity:row.purchaseQuantity,providerPrice:row.providerPrice,manufacturerDate:row.manufacturerDate,durabilityPeriod:row.durabilityPeriod},
		success: function(data) {
			if(data.status == 1){
				purchaseOrderManager();
	            return
	        } else{
	            $.messager.alert('响应信息',data.msg,'error');
	        }
		}
	});
};
//编辑排序
function edit(index){
	$('#s_purchaseProduct').datagrid('beginEdit', index);
};
// 保存保存
function saverow(index){
	$('#s_purchaseProduct').datagrid('endEdit', index);
};
// 取消编辑
function cancelrow(index){
	$('#s_purchaseProduct').datagrid('cancelEdit', index);
};
// 跟新gird行数据
function updateActions(){
	var rowcount = $('#s_purchaseProduct').datagrid('getRows').length;
	for(var i=0; i<rowcount; i++){
		$('#s_purchaseProduct').datagrid('updateRow',{
	    	index:i,
	    	row:{}
		});
	}
}
function addProviderProduct4order() {
	var providerId = $('#providerId').val();
	var purchaseCode = $('#purchaseCode').text();
	var storageId = $('#storageId').val();
	// 采购单可以新增的商品列表
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        resizable: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/purchase/findProviderProduct4OrderListInfo?providerId='+providerId+'&purchaseCode='+purchaseCode+'&storageId='+storageId,
        loadMsg:'正在装载数据...',
        singleSelect:false,
        fitColumns:true,
        remoteSort: true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
			{field:'id', title:'', width:25, align:'center', checkbox:true},
			{field:'xxx',    title:'采购商品ID', width:25, align:'center', 
				formatter:function(value,row,index) {
					return row.id;
				}
			},
			{field:'barcode',    title:'商品条码', width:40, align:'center'},
			{field:'name',    title:'商品名称', width:70, align:'center'},
			{field:'brandName',  title:'品牌',  width:30, align:'center'},
			{field:'currency',  title:'报价币种', width:30, align:'center',
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
			 {field:'ddd',  title:'供货单价',  width:30, align:'center'},
			 {field:'specification',  title:'规格',  width:30, align:'center'},
			 {field:'purchaseUnit',  title:'采购单位',  width:30, align:'center'},
			 {field:'sellingUnit',  title:'售卖单位',  width:30, align:'center'},
			 {field:'ratio',  title:'换算倍数',  width:30, align:'center'},
			 {field:'boxSpecification',  title:'箱规',  width:30, align:'center'},
			 {field:'providerName',  title:'供应商名称',  width:30, align:'center'},
			 {field:'storageName',  title:'入库仓库',  width:30, align:'center'},
			 {field:'remark',  title:'备注',  width:30, align:'center'} 
		] ],
		toolbar : [ {
			id : '_add',
			text : '增加选中商品',
			iconCls : 'icon-add',
			handler : function() {
				var checkedItems = $('#s_data').datagrid('getChecked');
				var ids = [];
				$.each(checkedItems, function(index, item) {
					ids.push(item.id);
				});
				if(ids.length < 1) {
					$.messager.alert('提示', '未选中商品', 'info');
					return ;
				}
				var providerProductIds = ids.join(',');
				$.messager.progress();
				$.ajax({
					url: '${rc.contextPath}/purchase/saveOrderProduct',
					type:"POST",
					data:{'purchaseCode':purchaseCode,'providerProductIds':providerProductIds},
					success: function(data) {
						$.messager.progress('close');
						if(data.status == 1) {
							$('#s_data').datagrid('clearChecked');
							$('#providerProductDiv').dialog('close');
							purchaseOrderManager();
				        } else{
				        	$.messager.progress('close');
				            $.messager.alert('响应信息',data.msg,'error');
				        }
					}
				});
			}
		} ],
		pagination : true
	});
	$('#providerProductDiv').dialog('open');
}
function searchProviderProduct() {
	$('#s_data').datagrid('load', {
		productId:$('#searchForm').find('input[name=productId]').val(),
		barcode:$('#searchForm').find('input[name=barcode]').val(),
		brandId:$('#brandId').combobox('getValue'),
		productName:$('#searchForm').find('input[name=productName]').val()
	});
}
$(function() {
	// 品牌
	$('#brandId').combobox({
            url:'${rc.contextPath}/brand/jsonBrandCode',
            valueField:'code',
            textField:'text',
	});
	// 修改应付金额
	$('#editPayableMoneyDiv').dialog({
		title : '调整应付金额',
		collapsible : true,
		closed : true,
		modal : true,
		buttons : [ {
			text : '确定',
			iconCls : 'icon-ok',
			handler : function() {
				// 本次改变的金额
				var updateMoney = ((Number($('#newPayableMoney').val()) * multiple - Number($('#payableMoney').text()) * multiple) / multiple).toFixed(6);
				// 实付金额
				$('#realMoney').val(((Number($('#realMoney').val()) * multiple + Number(updateMoney) * multiple) / multiple).toFixed(6));
				// 应付金额
				$('input[name=payableMoney]').val($('#newPayableMoney').val());
				$('#payableMoney').text($('#newPayableMoney').val());
				
				$('input[name=payableMoneyRemark]').val($('#newPayableMoneyRemark').val());
				$('#payableMoneyRemark').text($('#newPayableMoneyRemark').val());
				$('#editPayableMoneyDiv').dialog('close');
			}
		}, {
			text : '取消',
			align : 'left',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#editPayableMoneyDiv').dialog('close');
			}
		} ]
	});
	// 调整运费
	$('#editFreightMoneyDiv').dialog({
		title : '调整运费',
		collapsible : true,
		closed : true,
		modal : true,
		buttons : [ {
			text : '确定',
			iconCls : 'icon-ok',
			handler : function() {
				// 本次改变的金额
				var updateMoney = ((Number($('#newFreightMoney').val()) * multiple - Number($('#freightMoney').text()) * multiple) / multiple).toFixed(6);
				$('#realMoney').val(((Number($('#realMoney').val()) * multiple + Number(updateMoney) * multiple) / multiple).toFixed(6));
				// 更新金额
				$('input[name=freightMoney]').val($('#newFreightMoney').val());
				$('#freightMoney').text($('#newFreightMoney').val());
				
				// 合计金额
				var totalMoney = ((Number($('#totalMoney').text()) * multiple + Number(updateMoney) * multiple) / multiple).toFixed(6);
				$('#totalMoney').text(totalMoney);
				$('input[name=totalMoney]').val(totalMoney);
				// 应付金额
				var payableMoney = ((Number($('#payableMoney').text()) * multiple + Number(updateMoney) * multiple) / multiple).toFixed(6);
				$('#payableMoney').text(payableMoney);
				$('input[name=payableMoney]').val(payableMoney);
				$('#editFreightMoneyDiv').dialog('close');
			}
		}, {
			text : '取消',
			align : 'left',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#editFreightMoneyDiv').dialog('close');
			}
		} ]
	});
	
	$('#orderProductDiv').dialog({
		title : '导入商品列表（更新）',
		collapsible : true,
		closed : true,
		modal : true,
		buttons : [ {
			text : '保存',
			iconCls : 'icon-ok',
			handler : function() {
				$.messager.progress();
				$('#importOrderProductForm').form('submit', {
					url : "${rc.contextPath}/purchase/importOrderProduct",
					success : function(data) {
						$.messager.progress('close');
						var res = eval("("+data+")");
						if (res.status == 1) {
							purchaseOrderManager();
							$('#orderProductDiv').dialog('close');
						} else if (res.status == 0) {
							$.messager.alert('响应信息', res.msg, 'error');
						}
					}
				});
			}
		}]
	});
	
	$('#orderNewProductDiv').dialog({
		title : '导入商品列表（新增）',
		collapsible : true,
		closed : true,
		modal : true,
		buttons : [ {
			text : '保存',
			iconCls : 'icon-ok',
			handler : function() {
				$.messager.progress();
				$('#importNewOrderProductForm').find('input[name=purchaseCode]').val($('#purchaseCode').text());
				$('#importNewOrderProductForm').find('input[name=providerId]').val($('#providerId').val());
				$('#importNewOrderProductForm').form('submit', {
					url : "${rc.contextPath}/purchase/importNewOrderProduct",
					success : function(data) {
						$.messager.progress('close');
						var res = eval("("+data+")");
						if (res.status == 1) {
							purchaseOrderManager();
							$('#orderNewProductDiv').dialog('close');
						} else if (res.status == 0) {
							$.messager.alert('响应信息', res.msg, 'error');
						}
					}
				});
			}
		}]
	});
});
</script>
</body>
</html>