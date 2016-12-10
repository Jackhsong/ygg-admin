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
	<div id="providerProduct" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'采购商品管理',split:true" style="height: 120px;">
			<input type="hidden" id="type" value="${type}">
			<form id="searchForm" method="post">
				<table>
					<tr>
						<td>采购商品ID：</td>
						<td><input name="id"/></td>
	                    <td>商品条码：</td>
	                    <td><input name="barcode"/></td>
	                    <td>商品名称：</td>
	                    <td><input name="name"/></td>
	                    <td>品牌：</td>
		                <td><input id="brandId" name="brandId" /></td>
	                </tr>
	                <tr>
		                <td>供应商名称：</td>
		                <td><input id="providerId" name="providerId" /></td>
		                <td>入库仓库：</td>
		                <td><input id="storageId" name="storageId" /></td>
		                <td>备注：</td>
	                    <td><input name="remark"/></td>
	                    <td><a id="searchBtn" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',width:'80px'">查 询</a></td>
	                </tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
		</div>
	</div>
	<div id="editProviderProduct" class="easyui-dailog" style="width:900px;height:700px;padding:15px 20px;">
	 	<form id="editProviderProductForm" method="post">
	 		<input type="hidden" name="id" id="id" />
			<p>
				<span style="font-weight:bold;color: red">*</span><span>商品条码：</span>
				<span><input type="text" id="barcode" name="barcode" maxlength="20" style="width: 200px;"/></span>
			</p>
			<p>
				<span style="font-weight:bold;color: red">*</span><span>商品名称：</span>
				<span><input type="text" name="name" maxlength="40" style="width: 200px;"/></span>
			</p>
			<p>
				<span style="font-weight:bold;color: red">*</span><span>品牌：</span>
				<span><input type="text" id="formBrandId" name="formBrandId" style="width: 200px;"/></span>
			</p>
			<p>
				<span style="font-weight:bold;color: red">*</span><span>规格：</span>
				<span><input type="text" name="specification" maxlength="20" style="width: 100px;"/></span>
			</p>
			<p>
				<span style="font-weight:bold;color: red">*</span><span>采购单位：</span>
				<span><input type="text" name="purchaseUnit" maxlength="20" style="width: 100px;"/></span>
			</p>
			<p>
				<span>售卖单位：</span>
				<span><input type="text" name="sellingUnit" maxlength="20" style="width: 100px;"/></span>与"采购单位"相同可不填写
			</p>
			<p>
				<span>换算倍数：</span>
				<span><input type="text" name="ratio" maxlength="20" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" style="width: 100px;"/></span>换算倍数>=1，指采购单位换算为售卖单位的倍数，只填数字
			</p>
			<p>
				<span>箱规：</span>
				<span><input type="text" name="boxSpecification" maxlength="20" style="width: 100px;"/></span>
			</p>
			<br>
			<p>
				<span style="font-weight:bold;color: red">*</span><span>供应商名称：</span>
				<span><input type="text" name="formProviderId" id="formProviderId" style="width: 400px;"/></span>
			</p>
			<p>
				<span style="font-weight:bold;color: red">*</span><span>收货仓库：</span>
				<span><input type="text" id="formStorageId" name="formStorageId" style="width: 200px;"/></span>
			</p>
			<p>
				<span style="font-weight:bold;color: red">*</span><span>采购商品状态：</span>
				<span><input type="radio" name="isAvailable" value="1"/>可用</span>
				<span><input type="radio" name="isAvailable" checked="checked" value="0"/>停用</span>
			</p>
			<p>
				<span>备注：</span>
				<span><input type="text" name="remark" maxlength="100" style="width: 200px;"/></span>
			</p>
		</form>
	</div>
</div>
<div align="left" id="productDiv" class="easyui-dialog" align="center" data-options="iconCls:'icon-save'" style="padding: 5px; width: 320px; height: 250px;">
    <div>
    	<br>
    	<a onclick="window.open('${rc.contextPath}/purchase/downloadTemplate')" href="javascript:void(0)" class="easyui-linkbutton" data-options="width:'150px'">下载模板</a>
    </div>
    <div>
    	<br>
    	<form id="importProductForm" method="post" enctype="multipart/form-data">
       		<input id="productFile" type="file" name="productFile" />&nbsp;&nbsp;<br/><br/>
    	</form>
    </div>
</div>

<script type="text/javascript">
var providerListData = '';
var storageListData = '';
var brandListData = '';
// 清空div
function cleanProviderProductDiv() {
	$('#editProviderProductForm').form('clear');
	$('#productBaseName').text('');
	$('#brandName').text('');
	$('#sellerName').text('');
}
function fillCommbox() {
	$('#formBrandId').combobox({
        data:brandListData,
        valueField:'code',
        textField:'text',
	});
	$('#formProviderId').combobox({
	        data:providerListData,
	        multiple:true,
	        valueField:'id',
	        textField:'name',
	});
	$('#formStorageId').combobox({
        data:storageListData,
        valueField:'id',
        textField:'name',
	});
}
// 修改
function edit(id) {
	$.messager.progress();
	fillCommbox();
	$.ajax({
		url: '${rc.contextPath}/purchase/providerProductInfoById/' + id,
		type:"POST",
		async:false,
		success: function(data) {
			$.messager.progress('close');
			if(data.status == 1) {
				$('#editProviderProductForm').form('load',data.data);
				$('#editProviderProduct').dialog('open');
				
				$('#formBrandId').combobox('setValue', data.data.brandId);
				$('#formProviderId').combobox('setValues', data.data.providerId.split(","));
				$('#formStorageId').combobox('setValue', data.data.storageId);
	        } else{
	        	$.messager.progress('close');
	            $.messager.alert('响应信息',data.msg,'error');
	        }
		},
		error : function() {
			$.messager.progress('close');
		}
	});
}
function search() {
	$('#s_data').datagrid('load', {
		productBaseId:$('#searchForm').find('input[name=productBaseId]').val(),
		id:$('#searchForm').find('input[name=id]').val(),
		barcode:$('#searchForm').find('input[name=barcode]').val(),
		name:$('#searchForm').find('input[name=name]').val(),
		brandId:$('#brandId').combobox('getValue'),
		providerId:$('#providerId').combobox('getValue'),
		storageId:$('#storageId').combobox('getValue'),
		remark:$('#searchForm').find('input[name=remark]').val()
	});
}
// 验证必要参数
function valid() {
	if($('#barcode').val == '') {
		$.messager.alert('警告', '商品条码不能为空');
		return false;
	}
	if($('#editProviderProductForm').find('input[name=name]').val() == '') {
		$.messager.alert('警告', '商品名称不能为空');
		return false;
	}
	if(typeof($('#formBrandId').combobox('getValue')) == 'undefined' || $('#formBrandId').combobox('getValue') == 0) {
		$.messager.alert('警告', '品牌不能为空');
		return false;
	}
	if($('#editProviderProductForm').find('input[name=specification]').val() == '') {
		$.messager.alert('警告', '规格不能为空');
		return false;
	}
	if($('#editProviderProductForm').find('input[name=purchaseUnit]').val() == '') {
		$.messager.alert('警告', '采购单位不能为空');
		return false;
	}
	if(typeof($('#formProviderId').combobox('getValue')) == 'undefined') {
		$.messager.alert('警告', '供应商名称不能为空');
		return false;
	}
	if($('#formStorageId').combobox('getValue') == '') {
		$.messager.alert('警告', '收货仓库名称不能为空');
		return false;
	}
	if(typeof($('input[name=isAvailable]:checked').val()) == 'undefined') {
		$.messager.alert('警告', '采购商品状态不能为空');
		return false;
	}
	return true;
}
$(function() {
	$.ajax({
		url: '${rc.contextPath}/purchase/providerList?type='+$("#type").val(),
		type:"POST",
		async:false,
		success: function(data) {
			providerListData = data;
		}
	});
	$.ajax({
		url: '${rc.contextPath}/purchase/storageList?type='+$("#type").val(),
		type:"POST",
		async:false,
		success: function(data) {
			storageListData = data;
		}
	});
	$.ajax({
		url: '${rc.contextPath}/brand/jsonBrandCode',
		type:"POST",
		async:false,
		success: function(data) {
			brandListData = data;
		}
	});
	$('#storageId').combobox({
        data:storageListData,
        valueField:'id',
        textField:'name',
	});
	$('#brandId').combobox({
        data:brandListData,
        valueField:'code',
        textField:'text',
	});
	$('#providerId').combobox({
        data:providerListData,
        valueField:'id',
        textField:'name',
	});
	$('#searchBtn').click(function() {
		search();
	});
	$('#productDiv').dialog({
		title : '批量导入',
		collapsible : true,
		closed : true,
		modal : true,
		buttons : [ {
			text : '保存',
			iconCls : 'icon-ok',
			handler : function() {
				$.messager.progress();
				$('#importProductForm').form('submit', {
					url : "${rc.contextPath}/purchase/importProviderProduct",
					success : function(data) {
						$.messager.progress('close');
						var res = eval("("+data+")");
						if (res.status == 1) {
							$.messager.alert('响应信息', "保存成功", 'info', function() {
								$('#s_data').datagrid('load');
								$('#productDiv').dialog('close');
							});
						} else if (res.status == 0) {
							$.messager.alert('响应信息', res.msg, 'error');
						}
					}
				});
			}
		}]
	});
	$('#s_data').datagrid({
           nowrap: false,
           striped: true,
           collapsible:true,
           idField:'id',
           url:'${rc.contextPath}/purchase/providerProductListInfo?type='+$('#type').val(),
           loadMsg:'正在装载数据...',
           singleSelect:false,
           fitColumns:true,
           remoteSort: true,
           pageSize:50,
           pageList:[50,60],
           columns:[[
           	{field:'id',    title:'采购商品ID', width:18, align:'center'},
               {field:'barcode',    title:'商品条码', width:35, align:'center'},
               {field:'name',    title:'商品名称', width:70, align:'center'},
               {field:'brandName',  title:'品牌',  width:35, align:'center'},
               /* {field:'currency',  title:'报价币种', width:18, align:'center',
            	   formatter:function(value,row,index) {
            		   var a = '';
            		   var currencyArr = row.currency.split(',');
            		   for(var i = 0; i < currencyArr.length; i++) {
            			   if(currencyArr[i] == 1) {
            				   if(a.indexOf('人民币,') == -1)
            					a +=  '人民币,';
            			   }
            				else if(currencyArr[i] == 2) {
            					if(a.indexOf('美元,') == -1)
            					a +=  '美元,';
            				}
            				else if(currencyArr[i] == 3) {
            					if(a.indexOf('澳元,') == -1)
            					a +=  '澳元,';
            				}
            				else if(currencyArr[i] == 4) {
            					if(a.indexOf('日元,') == -1)
            					a +=  '日元,';
            				}
            				else if(currencyArr[i] == 5) {
            					if(a.indexOf('港币,') == -1)
            					a +=  '港币,';
            				}
            				else if(currencyArr[i] == 6) {
            					if(a.indexOf('欧元,') == -1)
            					a +=  '欧元,';
            				}
            				else if(currencyArr[i] == 7) {
            					if(a.indexOf('新元,') == -1)
            					a +=  '新元,';
            				}
            				else if(currencyArr[i] == 8) {
            					if(a.indexOf('韩元,') == -1)
            					a +=  '韩元,';
            				}
            				else if(currencyArr[i] == 9) {
            					if(a.indexOf('英镑,') == -1)
            					a +=  '英镑,';
            				}
            		   }
            		   if(a.length > 0)
            			   a = a.substring(0, a.length - 1);
            		   return a;
               	}},
                // {field:'ddd',  title:'供货单价',  width:30, align:'center'},
                {field:'specification',  title:'规格',  width:18, align:'center'},
                {field:'sellingUnit',  title:'售卖单位',  width:20, align:'center'},
                {field:'ratio',  title:'换算倍数',  width:20, align:'center'},
                {field:'boxSpecification',  title:'箱规',  width:20, align:'center'}, */
                
                {field:'1',  title:'待入库数',  width:20, align:'center'},
                {field:'2',  title:'实物库存',  width:20, align:'center'},
                {field:'3',  title:'可用库存',  width:20, align:'center'},
                {field:'4',  title:'实际可用库存',  width:20, align:'center'},
                {field:'5',  title:'未分配库存',  width:20, align:'center'},
                {field:'6',  title:'渠道剩余库存总和',  width:25, align:'center'},
                {field:'7',  title:'差额',  width:20, align:'center'},
                
                {field:'providerName',  title:'供应商名称',  width:30, align:'center'},
                {field:'storageName',  title:'入库仓库',  width:30, align:'center'},
                {field:'remark',  title:'备注',  width:30, align:'center'},
                {field:'isAvailable',  title:'状态',  width:10, align:'center',
                	formatter:function(value,row,index){
						if(row.isAvailable == 1)
							return '可用';
						else if(row.isAvailable == 0)
							return '停用';
                	}
                },
                {field:'hidden',  title:'操作', width:20, align:'center',
					formatter : function(value, row, index) {
						var a = '<a href="javascript:void(0);" onclick="edit(' + row.id + ')">编辑</a>';
						a += '&nbsp;&nbsp;<a href="javascript:void(0);" onclick="window.open(\'${rc.contextPath}/purchase/toBatch/' + row.id + '\')">批次</a>'
						return a;
					}
				}
			 ]],
		toolbar : [ {
			id : '_add',
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				cleanProviderProductDiv();
				fillCommbox();
				$('#editProviderProduct').dialog('open');
			}
		},{
			id : '_import',
			text : '上传商品',
			iconCls : 'icon-add',
			handler : function() {
				$('#importProductForm').form('clear');
				$('#productDiv').dialog('open');
			}
		} ],
		pagination : true
	});
	
	$('#editProviderProduct').dialog({
		title : '新增修改供应商商品',
		collapsible : true,
		closed : true,
		modal : true,
		buttons : [ {
			text : '保存',
			iconCls : 'icon-ok',
			handler : function() {
				$('#editProviderProductForm').form('submit', {
					url : "${rc.contextPath}/purchase/saveOrUpateProviderProduct",
					onSubmit : function() {
						return valid();
					},
					success : function(data) {
						var res = eval("("+data+")");
						if (res.status == 1) {
							$.messager.alert('响应信息', "保存成功", 'info', function() {
								$('#s_data').datagrid('load');
								$('#editProviderProduct').dialog('close');
							});
						} else if (res.status == 0) {
							$.messager.alert('响应信息', res.msg, 'error');
						}
					}
				});
			}
		}, {
			text : '取消',
			align : 'left',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#editProviderProduct').dialog('close');
			}
		} ]
	});
});
</script>
</body>
</html>