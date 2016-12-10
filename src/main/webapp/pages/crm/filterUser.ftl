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
		<div data-options="region:'center',title:'用户筛选'" >
			<form id="paramForm">
				<input name="data" type="hidden">
				<input name="accountCount" type="hidden">
				<input name="phoneCount" type="hidden">
				<input name="title" type="hidden">
				<fieldset>
					<legend>用户特征</legend>
					<table style="line-height:30px;">
						<tr>
							<td width="40px;"><input name="useType" value="on" type="hidden"/></td>
							<td width="110px;"><label>产品线：</label></td>
							<td>
								<input type="radio" name="type" value="0" checked="checked">换吧网络
								<input type="radio" name="type" value="1">心动慈露
								<input type="radio" name="type" value="2">心动慈露

							</td>
						</tr>
						<tr>
							<td width="40px;"><input type="checkbox" name="useCreateTime" id="useCreateTime"/></td>
							<td width="110px;"><label>注册时间：</label></td>
							<td>
								<input class="myCombobox" id="includeCreateTime" name="includeCreateTime"/>
								<input id="startCreateTime" name="startCreateTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />
								&nbsp;到&nbsp;<input id="endCreateTime" name="endCreateTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useLevel" id="useLevel"/></td>
							<td><label>用户等级：</label></td>
							<td>
								<input type="checkbox" value="0" name="level"/>V0
								<input type="checkbox" value="1" name="level"/>V1
								<input type="checkbox" value="2" name="level"/>V2
								<input type="checkbox" value="3" name="level"/>V3
								<input type="checkbox" value="4" name="level"/>V4
								<input type="checkbox" value="5" name="level"/>V5
								<input type="checkbox" value="6" name="level"/>V6
								<input type="checkbox" value="7" name="level"/>V7
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useAge" id="useAge"/></td>
							<td><label>用户年龄：</label></td>
							<td>
								<input class="myCombobox" id="includeAge" name="includeAge"/>
								<input id="startAge" name="startAge" class="easyui-numberbox check" data-options="min:10,max:100"/>
								&nbsp;到&nbsp;<input id="endAge" name="endAge" class="easyui-numberbox check" data-options="min:10,max:100"/>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useBirthday" id="useBirthday"/></td>
							<td><label>生日时间：</label></td>
							<td>
								<input class="myCombobox" id="includeBirthday" name="includeBirthday"/>
								<input id="startBirthday" name="startBirthday" onClick="WdatePicker({dateFmt: 'MM月dd日'})" value="" />
								&nbsp;到&nbsp;<input id="endBirthday" name="endBirthday" onClick="WdatePicker({dateFmt: 'MM月dd日'})" value="" />
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useProvince" id="useProvince"/></td>
							<td><label>省份地域：</label></td>
							<td>
								<input class="myCombobox" id="includeProvince" name="includeProvince"/>
								<input id="province" name="province"/>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useCoupon" id="useCoupon"/></td>
							<td><label>优惠券：</label></td>
							<td>
								<input class="couponType" id="couponType" name="couponType">
								<label class="coupon" style="display: none;"><input id="coupon" name="coupon" style="width: 400px;"/>&nbsp;&nbsp;多个以逗号","分开</label>
								<label class="startCoupon" style="display: none;">到期时间：<input id="startCoupon" name="startCoupon" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />&nbsp;到&nbsp;<input id="endCoupon" name="endCoupon" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" /></label>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useOrderComment" id="useOrderComment"/></td>
							<td><label>评论：</label></td>
							<td>
								<input id="startOrderComment" name="startOrderComment" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />&nbsp;到&nbsp;<input id="endOrderComment" name="endOrderComment" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />
								<input type="checkbox" value="3" name="commentLevel"/>好评
								<input type="checkbox" value="2" name="commentLevel"/>中评
								<input type="checkbox" value="1" name="commentLevel"/>差评
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="usePoint" id="usePoint"/></td>
							<td><label>用户积分：</label></td>
							<td>
								<input type="text" id="startPoint" name="startPoint" class="easyui-numberbox" data-options="min:0"/>&nbsp;到&nbsp;<input type="text" id="endPoint" name="endPoint" class="easyui-numberbox" data-options="min:0"/>
							</td>
						</tr>
					</table>
				</fieldset>
				
				<fieldset>
					<legend>交易信息</legend>
					<table style="line-height:30px;">
						<tr>
							<td width="40px;"><input type="checkbox" name="useSaleFlag" id="useSaleFlag"/></td>
							<td width="110px;"><label>商品国家：</label></td>
							<td>
								<input class="myCombobox" id="includeSaleFlag" name="includeSaleFlag"/>
								<input id="saleFlag" name="saleFlag"/>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useCategory" id="useCategory"/></td>
							<td><label>商品类目：</label></td>
							<td>
								<input class="myCombobox" id="includeCategory" name="includeCategory"/>
								<a class="easyui-linkbutton" onclick="openCategory()" >选择商品类目</a>
								<input type="hidden" name="categoryFirstId">
								<input type="hidden" name="categorySecondId">
								<input type="hidden" name="categoryThirdId">
								<label id="categoryContent"></label>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useBrand" id="useBrand"/></td>
							<td><label>商品品牌：</label></td>
							<td>
								<input class="myCombobox" id="includeBrand" name="includeBrand"/>
								<input id="brand" name="brand" />
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useProductId" id="useProductId"/></td>
							<td><label>商品ID：</label></td>
							<td>
								<input class="myCombobox" id="includeProductId" name="includeProductId"/>
								<input id="productId" name="productId" style="width: 400px;"/>&nbsp;&nbsp;多个以逗号","分开
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useCustomTransaction" id="useCustomTransaction"/></td>
							<td><label>客单价：</label></td>
							<td>
								<input class="myCombobox" id="includeCustomTransaction" name="includeCustomTransaction"/>
								<input type="text" id="startCustomTransaction" name="startCustomTransaction" class="easyui-numberbox" data-options="min:0"/>&nbsp;到&nbsp;<input type="text" id="endCustomTransaction" name="endCustomTransaction" class="easyui-numberbox" data-options="min:0"/>
							</td>
						</tr>
					</table>
				</fieldset>
				
				<fieldset>
					<legend>RFM</legend>
					<table style="line-height:30px;">
						<tr>
							<td width="40px;"><input type="checkbox" name="useLastConsumer" id="useLastConsumer"/></td>
							<td width="110px;"><label>最近一次消费：</label></td>
							<td>
								<input class="myCombobox" id="includeLastConsumer" name="includeLastConsumer"/>
								<input id="startLastConsumer" name="startLastConsumer" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />&nbsp;到&nbsp;<input id="endLastConsumer" name="endLastConsumer" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useConsumer" id="useConsumer"/></td>
							<td><label>消费时间：</label></td>
							<td>
								<input class="myCombobox" id="includeConsumer" name="includeConsumer"/>
								<input id="startConsumer" name="startConsumer" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />&nbsp;到&nbsp;<input id="endConsumer" name="endConsumer" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useTotalOrder" id="useTotalOrder"/></td>
							<td><label>总付款订单数：</label></td>
							<td>
								<input class="myCombobox" id="includeBrand" name="includeBrand"/>
								<input type="text" id="startTotalOrder" name="startTotalOrder" class="easyui-numberbox" data-options="min:0"/>&nbsp;到&nbsp;<input type="text" id="endTotalOrder" name="endTotalOrder" class="easyui-numberbox" data-options="min:0"/>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useTotalOrderInTime" id="useTotalOrderInTime"/></td>
							<td><label>时段内付款订单数：</label></td>
							<td>
								<input class="myCombobox1" id="includeTotalOrderInTime" name="includeTotalOrderInTime"/>
								<input id="startTotalOrderInTime" name="startTotalOrderInTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />&nbsp;到&nbsp;<input id="endTotalOrderInTime" name="endTotalOrderInTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />
								<input type="text" id="startTotalCountOrderInTime" name="startTotalCountOrderInTime" class="easyui-numberbox" data-options="min:0"/>&nbsp;到&nbsp;<input type="text" id="endTotalCountOrderInTime" name="endTotalCountOrderInTime" class="easyui-numberbox" data-options="min:0"/>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useTotalCustomMoney" id="useTotalCustomMoney"/></td>
							<td><label>总消费金额：</label></td>
							<td>
								<input class="myCombobox" id="includeTotalCustomMoney" name="includeTotalCustomMoney"/>
								<input type="text" id="startTotalCustomMoney" name="startTotalCustomMoney" class="easyui-numberbox" data-options="min:0"/>&nbsp;到&nbsp;<input type="text" id="endTotalCustomMoney" name="endTotalCustomMoney" class="easyui-numberbox" data-options="min:0"/>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="useTotalCustomMoneyInTime" id="useTotalCustomMoneyInTime"/></td>
							<td><label>时段内消费金额：</label></td>
							<td>
								<input class="myCombobox1" id="includeTotalCustomMoneyInTime" name="includeTotalCustomMoneyInTime"/>
								<input id="startTotalCustomMoneyInTime" name="startTotalCustomMoneyInTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />&nbsp;到&nbsp;<input id="endTotalCustomMoneyInTime" name="endTotalCustomMoneyInTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" value="" />
								<input type="text" id="startTotalCountCustomMoneyInTime" name="startTotalCountCustomMoneyInTime" class="easyui-numberbox" data-options="min:0"/>&nbsp;到&nbsp;<input type="text" id="endTotalCountCustomMoneyInTime" name="endTotalCountCustomMoneyInTime" class="easyui-numberbox" data-options="min:0"/>
							</td>
						</tr>
					</table>
				</fieldset>
				<table>
					<tr><td width="100px;"></td><td width="110px;"></td></tr>
					<tr>
						<td></td>
						<td><a id="searchBtn" onclick="filterUser()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-filter'">筛&nbsp;&nbsp;&nbsp;&nbsp;选</a></td>
						<td style="color: red;" id="resultDesc"></td>
					</tr>
					<tr></tr>
				</table>
			</form>
			<div>
				<fieldset>
					<legend>筛选结果</legend>
					<form id="resultForm">
						<table style="line-height:30px;">
							<tr>
								<td width="110px;"><label>筛选结果标题：</label></td>
								<td>
									<input id="title" style="width: 400px;"/>
								</td>
							</tr>
							<tr><td width="100px;"></td><td width="110px;"></td></tr>
							<tr>
								<td></td>
								<td><a id="searchBtn" onclick="saveFilterResult()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存筛选结果</a></td>
								<td><a id="searchBtn" onclick="showFilterResult()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查看筛选结果</a></td>
							</tr>
						</table>
					</form>
				</fieldset>
			</div>
		</div>
	</div>
</div>

<div id="categoryInfo" class="easyui-dialog" style="height:500px;width:900px;font-size:14px;"></div>

<script>
function openCategory() {
	$('#categoryInfo').dialog('open');
	
	$.ajax({
		type : "POST",
		url : '${rc.contextPath}/category/jsonCategoryFirstCode',
		success : function(data) {
			var info = data;
			var content = '';
			for(var i = 0; i < info.length; i++) {
				if(info[i].code == 0) 
					continue;
				content += '<div>';
				content += '<input type="checkbox" name=cFirst value=' + info[i].code + ',' + info[i].text + ' />' + info[i].text
				content += '</div>';
			}
			$('#categoryInfo').append(content);
		}
	});
}

$(function() {
	
	$('#categoryInfo').on('click', 'input[name=cFirst]', function() {
		var $that = $(this);
		var id = $(this).val().split(',')[0];
		if($that.is(':checked')) {
			if($that.parent().children('div').length > 0) 
				return;
			$.ajax({
				type : "POST",
				url : '${rc.contextPath}/category/jsonCategorySecondCode?firstId=' + id,
				success : function(data) {
					var info = data;
					var content = '';
					for(var i = 0; i < info.length; i++) {
						if(info[i].code == 0) 
							continue;
						content += '<div>';
						content += '&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name=cSecond value=' + info[i].code + ',' + info[i].text + ' />' + info[i].text
						content += '</div>';
					}
					$that.parent().append(content);
				}
			});
		}
	});
	
	$('#categoryInfo').on('click', 'input[name=cSecond]', function() {
		var $that = $(this);
		var id = $(this).val().split(',')[0];
		if($that.is(':checked')) {
			if($that.parent().children('div').length > 0) 
				return;
			$.ajax({
				type : "POST",
				url : '${rc.contextPath}/category/jsonCategoryThirdCode?secondId=' + id,
				success : function(data) {
					var info = data;
					var	content = '<div>';
					for(var i = 0; i < info.length; i++) {
						if(info[i].code == 0) 
							continue;
						if(i == 0)
							content += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name=cThird value=' + info[i].code + ',' + info[i].text + ' />' + info[i].text
						else 
							content += '&nbsp;&nbsp;<input type="checkbox" name=cThird value=' + info[i].code + ',' + info[i].text + ' />' + info[i].text
					}
					content += '</div>';
					$that.parent().append(content);
				}
			});
		}
	});
	
	$('#province').combobox({
		url: '${rc.contextPath}/area/jsonProvinceCode',
		multiple: true,
		width: 400,
		valueField: 'code',
		textField: 'text'
	});
	
	$('.myCombobox').combobox({
		data: [{
			code: '1',
			text: '包含',
			selected: true
		},{
			code: '0',
			text: '排除'
		}],
		width:80,
		panelHeight:50,
		valueField: 'code',
		textField: 'text'
	});
	$('.myCombobox1').combobox({
		data: [{
			code: '1',
			text: '包含',
			selected: true
		}],
		width:80,
		panelHeight:50,
		valueField: 'code',
		textField: 'text'
	});
	$('.couponType').combobox({
		data: [{
			code: '1',
			text: '优惠券ID',
			selected: true
		},{
			code: '0',
			text: '全部'
		}],
		width:80,
		panelHeight:50,
		valueField: 'code',
		textField: 'text',
		onChange: function(newValue, oldValue) {
			if(newValue == 1) {
				$('.coupon').show();
				$('.startCoupon').hide();
			}
			if(newValue == 0) {
				$('.coupon').hide();
				$('.startCoupon').show();
			}
		}
	});
	$("#saleFlag").combobox({
		width: 400,
		multiple: true,
		url:'${rc.contextPath}/flag/jsonSaleFlagCode',   
	    valueField:'code',   
	    textField:'text'  
	});
	$('#brand').combobox({
		width: 400,
        multiple: true,
        url:'${rc.contextPath}/brand/jsonBrandCode',
        valueField:'code',
        textField:'text'
    });
	
	$('#categoryInfo').dialog({
		title : '商品类目',
		collapsible : true,
		closed : true,
		resizable : true,
		modal : true,
		buttons : [ {
			text : '确定',
			iconCls : 'icon-ok',
			handler : function() {
				var categoryFirstId = new Array();
				var categorySecondId = new Array();
				var categoryThirdId = new Array();
				var categoryFirstName = new Array();
				var categorySecondName = new Array();
				var categoryThirdName = new Array();
				// 一级分类
				$('#categoryInfo').find('input[name=cFirst]').each(function(i) {
					if($(this).is(':checked')) {
						var con = $(this).val();
						var cont = con.split(',');
						categoryFirstId.push(cont[0]);
						categoryFirstName.push(cont[1]);
					}
				});
				// 二级分类
				$('#categoryInfo').find('input[name=cSecond]').each(function(i) {
					if($(this).is(':checked')) {
						var con = $(this).val();
						var cont = con.split(',');
						categorySecondId.push(cont[0]);
						categorySecondName.push(cont[1]);
					}
				});
				// 三级分类
				$('#categoryInfo').find('input[name=cThird]').each(function(i) {
					if($(this).is(':checked')) {
						var con = $(this).val();
						var cont = con.split(',');
						categoryThirdId.push(cont[0]);
						categoryThirdName.push(cont[1]);
					}
				});
				$('input[name=categoryFirstId]').val(categoryFirstId.join());
				$('input[name=categorySecondId]').val(categorySecondId.join());
				$('input[name=categoryThirdId]').val(categoryThirdId.join());
				var content = '一级分类：' + categoryFirstName.join();
				content += '，    二级分类：' + categorySecondName.join();
				content += '，    三级分类：' + categoryThirdName.join();
				$('#categoryContent').text(content);
				$('#categoryInfo').dialog('close');
			}
		}, {
			text : '取消',
			align : 'left',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#categoryInfo').dialog('close');
			}
		} ]
	});
});

//保存筛选结果
function saveFilterResult() {
	var count = $('input[name=accountCount]').val();
	if(count == '' || count == '0') {
		$.messager.alert('提示', '没有筛选结果。', 'info');
		return false;
	}
	if($('#title').val() == '') {
		$.messager.alert('提示', '没有标题。', 'info');
		return false;
	}
	
	$.messager.progress();
	$('input[name=title]').val($('#title').val());
	$.ajax({
		type : "POST",
		url : '${rc.contextPath}/crm/save',
		data : $('#paramForm').serialize(),
		error : function(request) {
			$.messager.progress('close');
			$.messager.alert('提示', '出错了', 'info');
		},
		success : function(data) {
			$.messager.progress('close');
			var data = eval('(' + data + ')');  
			if (data.status == 1) {
				window.location.href="${rc.contextPath}/crm/toFilterList";
			} else {
				$.messager.alert('提示', data.msg, 'error ');
			}
		}
	});
}

function check() {
	// 注册时间
	if($('#useCreateTime').is(':checked')) {
		if($('#startCreateTime').val() == '' || $('#endCreateTime').val() == ''){
			$.messager.alert('提示信息', '注册时间不能为空', 'info');
			return false;
		}
	}
	// 用户等级
	if($('#useLevel').is(':checked')) {
		if(typeof($('input[name=level]:checked').val()) == 'undefined') {
			$.messager.alert('提示信息', '用户等级不能为空', 'info');
			return false;
		}
	}
	// 用户年龄
	if($('#useAge').is(':checked')) {
		if($('#startAge').numberbox('getValue') == '' || $('#endAge').numberbox('getValue') == ''){
			$.messager.alert('提示信息', '用户年龄不能为空', 'info');
			return false;
		}
	}
	// 生日时间
	if($('#useBirthday').is(':checked')) {
		if($('#startBirthday').val() == '' || $('#endBirthday').val() == ''){
			$.messager.alert('提示信息', '生日时间不能为空', 'info');
			return false;
		}
	}
	// 省份地域
	if($('#useProvince').is(':checked')) {
		if(typeof($('#province').combobox('getValue')) == 'undefined'){
			$.messager.alert('提示信息', '省份地域不能为空', 'info');
			return false;
		}
	}
	// 优惠劵
	if($('#useCoupon').is(':checked')) {
		if($('#couponType').combobox('getValue') == 1) {
			if($('#coupon').val() == ''){
				$.messager.alert('提示信息', '优惠劵ID不能为空', 'info');
				return false;
			}
		} else {
			if($('#startCoupon').val() == '' || $('#endCoupon').val() == ''){
				$.messager.alert('提示信息', '优惠劵时间不能为空', 'info');
				return false;
			}
		}
	}
	// 评论
	if($('#useOrderComment').is(':checked')) {
		if($('#startOrderComment').val() == '' || $('#endOrderComment').val() == ''){
			$.messager.alert('提示信息', '评论时间不能为空', 'info');
			return false;
		}
		if(typeof($('input[name=commentLevel]:checked').val()) == 'undefined') {
			$.messager.alert('提示信息', '评论等级不能为空', 'info');
			return false;
		}
	}
	// 用户积分
	if($('#usePoint').is(':checked')) {
		if($('#startPoint').numberbox('getValue') == '' || $('#endPoint').numberbox('getValue') == ''){
			$.messager.alert('提示信息', '用户积分不能为空', 'info');
			return false;
		}
	}
	// 商品国家
	if($('#useSaleFlag').is(':checked')) {
		if(typeof($('#saleFlag').combobox('getValue')) == 'undefined'){
			$.messager.alert('提示信息', '商品国家不能为空', 'info');
			return false;
		}
	}
	// 商品类目
	if($('#useCategory').is(':checked')) {
		if($('input[name=categoryFirstId]').val() == '' || $('input[name=categorySecondId]').val() == '' || $('input[name=categoryThirdId]').val() == ''){
			$.messager.alert('提示信息', '商品类目不能为空', 'info');
			return false;
		}
	}
	// 商品品牌
	if($('#useBrand').is(':checked')) {
		if(typeof($('#brand').combobox('getValue')) == 'undefined'){
			$.messager.alert('提示信息', '商品品牌不能为空', 'info');
			return false;
		}
	}
	// 商品ID
	if($('#useProductId').is(':checked')) {
		if($('#productId').val() == ''){
			$.messager.alert('提示信息', '商品ID不能为空', 'info');
			return false;
		}
	}
	// 客单价
	if($('#useCustomTransaction').is(':checked')) {
		if($('#startCustomTransaction').numberbox('getValue') == '' || $('#endCustomTransaction').numberbox('getValue') == ''){
			$.messager.alert('提示信息', '客单价不能为空', 'info');
			return false;
		}
	}
	// 最近一次消费
	if($('#useLastConsumer').is(':checked')) {
		if($('#startLastConsumer').val() == '' || $('#endLastConsumer').val() == ''){
			$.messager.alert('提示信息', '最近一次消费不能为空', 'info');
			return false;
		}
	}
	// 消费时间
	if($('#useConsumer').is(':checked')) {
		if($('#startConsumer').val() == '' || $('#endConsumer').val() == ''){
			$.messager.alert('提示信息', '消费时间不能为空', 'info');
			return false;
		}
	}
	// 总付款订单数
	if($('#useTotalOrder').is(':checked')) {
		if($('#startTotalOrder').numberbox('getValue') == '' || $('#endTotalOrder').numberbox('getValue') == ''){
			$.messager.alert('提示信息', '总付款订单数不能为空', 'info');
			return false;
		}
	}
	// 时段内付款订单数
	if($('#useTotalOrderInTime').is(':checked')) {
		if($('#startTotalOrderInTime').val() == '' || $('#endTotalCountOrderInTime').val() == ''){
			$.messager.alert('提示信息', '时段内付款订单数 -- 时间不能为空', 'info');
			return false;
		}
		if($('#startTotalCountOrderInTime').numberbox('getValue') == '' || $('#endTotalCountOrderInTime').numberbox('getValue') == ''){
			$.messager.alert('提示信息', '时段内付款订单数 -- 订单不能为空', 'info');
			return false;
		}
	}
	// 总消费金额
	if($('#useTotalCustomMoney').is(':checked')) {
		if($('#startTotalCustomMoney').numberbox('getValue') == '' || $('#endTotalCustomMoney').numberbox('getValue') == ''){
			$.messager.alert('提示信息', '总消费金额数不能为空', 'info');
			return false;
		}
	}
	// 时段内消费金额
	if($('#useTotalCustomMoneyInTime').is(':checked')) {
		if($('#startTotalCustomMoneyInTime').val() == '' || $('#endTotalCustomMoneyInTime').val() == ''){
			$.messager.alert('提示信息', '时段内消费金额  -- 时间不能为空', 'info');
			return false;
		}
		if($('#startTotalCountCustomMoneyInTime').numberbox('getValue') == '' || $('#endTotalCountCustomMoneyInTime').numberbox('getValue') == ''){
			$.messager.alert('提示信息', '时段内消费金额 -- 金额不能为空', 'info');
			return false;
		}
	}
	return true;
}
// 筛选用户
function filterUser() {
	if(!check()) return false;
	$.messager.progress();
	$('input[name=accountCount]').val('');
	$('input[name=phoneCount]').val('');
	$('input[name=title]').val('');
	$('input[name=data]').val('');
	
	$.ajax({
		type : "POST",
		url : '${rc.contextPath}/crm/filterByParam',
		data : $('#paramForm').serialize(),
		error : function(request) {
			$.messager.progress('close');
			$.messager.alert('提示', '出错了', 'info');
		},
		success : function(data) {
			$.messager.progress('close');
			data = JSON.parse(data);
			if (data.status == 1) {
				$('#resultDesc').text("共" + data.accountCount + "个用户，有手机号码用户" + data.phoneCount + "个");
				$('input[name=accountCount]').val(data.accountCount);
				$('input[name=phoneCount]').val(data.phoneCount);
				if(data.accountCount > 0) {
					var s = JSON.stringify(data.data);
					$('input[name=data]').val(s);
				} else {
					$('input[name=data]').val("");
				}
				
			} else {
				$.messager.alert('提示', data.msg, 'error ');
			}
		}
	});
}
</script>

</body>
</html>