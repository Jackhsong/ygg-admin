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
		<div data-options="region:'north',title:'新增修改供应商信息',split:true,height:'auto'">
			<form id="editProviderForm" method="post">
				<p><input id="id" type="hidden" name="id" value="<#if provider?? && provider.id??>${provider.id}<#else>0</#if>"></p>
				<p style="padding:'15px,15px,15px,15px'">
					<span style="font-weight:bold;color: red">*</span><span>供应商名称：</span>
					<span><input type="text" name="name" id="name" value="<#if provider?? && provider.name??>${provider.name}</#if>" maxlength="30" style="width: 200px;"/></span>
				</p>
				<p>
					<span>公司名称：</span>
					<span><input type="text" name="companyName" id="companyName" value="<#if provider?? && provider.companyName??>${provider.companyName}</#if>" maxlength="30" style="width: 200px;"/></span>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>供应商类型：</span>
					<#if provider?? && provider.type?? && provider.type == 1><span><input type="radio" name="type" value="1" checked="checked"/>食品</span>
					<#else><span><input type="radio" name="type" value="1"/>食品</span>
					</#if>
					<#if provider?? && provider.type?? && provider.type == 2><span><input type="radio" name="type" value="2" checked="checked"/>化妆品</span>
					<#else><span><input type="radio" name="type" value="2"/>化妆品</span>
					</#if>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>经营品牌：</span>
					<span><input type="text" name="providerBrand" id="providerBrand" value="<#if provider?? && provider.providerBrand??>${provider.providerBrand}</#if>" maxlength="30" style="width: 200px;"/></span>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>公司所在地：</span>
					<span><input type="text" name="offices" id="offices" value="<#if provider?? && provider.offices??>${provider.offices}</#if>" maxlength="20" style="width: 200px;"/></span>
				</p>
				<p>
					<span>公司详细地址：</span>
					<span><input type="text" name="companyDetailAddress" id="companyDetailAddress" value="<#if provider?? && provider.companyDetailAddress??>${provider.companyDetailAddress}</#if>" maxlength="200" style="width: 200px;"/></span>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>报价币种：</span>
					<#if provider?? && provider.currency?? && provider.currency == 1><span><input type="radio" name="currency" value="1" checked="checked"/>人民币</span>
					<#else><span><input type="radio" name="currency" value="1"/>人民币</span>
					</#if>
					<#if provider?? && provider.currency?? && provider.currency == 2><span><input type="radio" name="currency" value="2" checked="checked"/>美元</span>
					<#else><span><input type="radio" name="currency" value="2"/>美元</span>
					</#if>
					<#if provider?? && provider.currency?? && provider.currency == 3><span><input type="radio" name="currency" value="3" checked="checked"/>澳元</span>
					<#else><span><input type="radio" name="currency" value="3"/>澳元</span>
					</#if>
					<#if provider?? && provider.currency?? && provider.currency == 4><span><input type="radio" name="currency" value="4" checked="checked"/>日元</span>
					<#else><span><input type="radio" name="currency" value="4"/>日元</span>
					</#if>
					<#if provider?? && provider.currency?? && provider.currency == 5><span><input type="radio" name="currency" value="5" checked="checked"/>港币</span>
					<#else><span><input type="radio" name="currency" value="5"/>港币</span>
					</#if>
					<#if provider?? && provider.currency?? && provider.currency == 6><span><input type="radio" name="currency" value="6" checked="checked"/>欧元</span>
					<#else><span><input type="radio" name="currency" value="6"/>欧元</span>
					</#if>
					<#if provider?? && provider.currency?? && provider.currency == 7><span><input type="radio" name="currency" value="6" checked="checked"/>新元</span>
					<#else><span><input type="radio" name="currency" value="7"/>新元</span>
					</#if>
					<#if provider?? && provider.currency?? && provider.currency == 8><span><input type="radio" name="currency" value="6" checked="checked"/>韩元</span>
					<#else><span><input type="radio" name="currency" value="8"/>韩元</span>
					</#if>
					<#if provider?? && provider.currency?? && provider.currency == 9><span><input type="radio" name="currency" value="6" checked="checked"/>英镑</span>
					<#else><span><input type="radio" name="currency" value="9"/>英镑</span>
					</#if>
				</p>
				<p>
					<span>是否开票：</span>
					<#if provider?? && provider.isInvoice?? && provider.isInvoice == 1><span><input type="radio" name="isInvoice" checked="checked" value="1"/>是</span>
					&nbsp;&nbsp;税点<input type="text" name="tax" id="tax" value="<#if provider?? && provider.tax??>${provider.tax}</#if>" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="2" style="width: 30px;" />%&nbsp;&nbsp;&nbsp;&nbsp;
					<#else><span><input type="radio" name="isInvoice" value="1"/>是</span>
					&nbsp;&nbsp;税点<input type="text" name="tax" id="tax" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="2" disabled="disabled" style="width: 30px;" />%&nbsp;&nbsp;&nbsp;&nbsp;
					</#if>
					<#if provider?? && provider.isInvoice?? && provider.isInvoice == 0><span><input type="radio" name="isInvoice" checked="checked" value="0"/>否</span>
					<#else><span><input type="radio" name="isInvoice" value="0"/>否</span>
					</#if>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>联系人：</span>
					<span><input type="text" name="contactPerson" id="contactPerson" value="<#if provider?? && provider.contactPerson??>${provider.contactPerson}</#if>" maxlength="30" style="width: 200px;"/></span>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>联系电话：</span>
					<span><input type="text" name="contactPhone" id="contactPhone" value="<#if provider?? && provider.contactPhone??>${provider.contactPhone}</#if>" maxlength="20" style="width: 200px;"/></span>
				</p>
				<p>
					<span>QQ、微信：</span>
					<span><input type="text" name="qq" id="qq" value="<#if provider?? && provider.qq??>${provider.qq}</#if>" maxlength="30" style="width: 200px;"/></span>
				</p>
				<p>
					<span>邮箱：</span>
					<span><input type="text" name="email" id="email" value="<#if provider?? && provider.email??>${provider.email}</#if>" maxlength="30" style="width: 200px;"/></span>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>采购款结算：</span>
					<#if provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 1><span><input type="radio" name="purchaseSubmitType" checked="checked" value="1"/>款到发货</span>
					<#else><span><input type="radio" name="purchaseSubmitType" value="1"/>款到发货</span>
					</#if>
					<#if provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 2><span><input type="radio" name="purchaseSubmitType" checked="checked" value="2"/>预付定金</span>
					<input onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" class="percent2" type="text" value="<#if provider?? && provider.percent??>${provider.percent}</#if>" name="percent" maxlength="2" style="width: 20px;" />%，出货前付清
					<#else><span><input type="radio" name="purchaseSubmitType" value="2"/>预付定金</span>
					<input onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" class="percent2" disabled="disabled" type="text" name="percent" maxlength="2" style="width: 20px;" />%，出货前付清
					</#if>
					<#if provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 3><span><input type="radio" name="purchaseSubmitType" checked="checked" value="3"/>预付定金</span>
					<input class="percent3" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" type="text" value="<#if provider?? && provider.percent??>${provider.percent}</#if>" name="percent" maxlength="2" style="width: 20px;" />%，出货<input class="day3" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" type="text" value="<#if provider?? && provider.day??>${provider.day}</#if>" name="day" maxlength="2" style="width: 20px;" />天付清
					<#else><span><input type="radio" name="purchaseSubmitType" value="3"/>预付定金</span>
					<input class="percent3" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" disabled="disabled" type="text" name="percent" maxlength="2" style="width: 20px;" />%，出货<input class="day3" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" disabled="disabled" type="text" name="day" maxlength="2" style="width: 20px;" />天付清
					</#if>
					<#if provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 4><span><input type="radio" name="purchaseSubmitType" checked="checked" value="4"/>到货后</span>
					<input onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" class="day4" type="text" value="<#if provider?? && provider.day??>${provider.day}</#if>" name="day" maxlength="2" style="width: 20px;" />天付清
					<#else><span><input type="radio" name="purchaseSubmitType" value="4"/>到货后</span>
					<input onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" disabled="disabled" class="day4" type="text" name="day" maxlength="2" style="width: 20px;" />天付清
					</#if>
					<#if provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 5><span><input type="radio" name="purchaseSubmitType" checked="checked" value="5"/>其他</span><input type="text" id="other" name="other" value="<#if provider?? && provider.other??>${provider.other}</#if>" maxlength="100" style="width: 200px;" />
					<#else><span><input type="radio" name="purchaseSubmitType" value="5"/>其他</span><input type="text" disabled="disabled" id="other" name="other" maxlength="100" style="width: 200px;" />
					</#if>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>收款账号：</span>
					<span><input type="text" name="receiveBankAccount" id="receiveBankAccount" value="<#if provider?? && provider.receiveBankAccount??>${provider.receiveBankAccount}</#if>" maxlength="200" style="width: 200px;"/></span>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>开户行：</span>
					<span><input type="text" name="receiveBank" id="receiveBank" value="<#if provider?? && provider.receiveBank??>${provider.receiveBank}</#if>" maxlength="200" style="width: 200px;"/></span>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>收款方名称：</span>
					<span><input type="text" name="receiveName" id="receiveName" value="<#if provider?? && provider.receiveName??>${provider.receiveName}</#if>" maxlength="200" style="width: 200px;"/></span>
				</p>
				<p>
					<span>Swift code：</span>
					<span><input type="text" name="swiftCode" id="swiftCode" value="<#if provider?? && provider.swiftCode??>${provider.swiftCode}</#if>" maxlength="200" style="width: 200px;"/></span>海外账号需填写银行国际代码
				</p>
				<p>
					<span>银行地址：</span>
					<span><input type="text" name="bankAddress" id="bankAddress" value="<#if provider?? && provider.bankAddress??>${provider.bankAddress}</#if>" maxlength="200" style="width: 200px;"/></span>海外账号需填写
				</p>
				<p>
					<span>备注：</span>
					<span><input type="text" name="remark" id="remark" value="<#if provider?? && provider.remark??>${provider.remark}</#if>" maxlength="100" style="width: 200px;"/></span>
				</p>
				<p>
					<input type="hidden" name="contractImgUrl" id="contractImgUrl" value="<#if provider?? && provider.contractImgUrl??>${provider.contractImgUrl}</#if>" />
					<a onclick="picDialogOpen()" href="javascript:;" class="easyui-linkbutton">上传图片</a><br>
					<#if provider?? && provider.contractImgUrl?? && provider.contractImgUrl == ''>
					<img height="200" width="200" onclick="openWindow()" style="display: none;" id="contractImg" name="contractImg" src="<#if provider?? && provider.contractImgUrl??>${provider.contractImgUrl}</#if>">
					<#else><img height="200" width="200" onclick="openWindow()" id="contractImg" name="contractImg" src="<#if provider?? && provider.contractImgUrl??>${provider.contractImgUrl}</#if>">
					</#if>
				</p>
				<p>
					<span style="font-weight:bold;color: red">*</span><span>采购负责人：</span>
					<span><input type="text" name="purchasingLeader" id="purchasingLeader" value="<#if provider?? && provider.purchasingLeader??>${provider.purchasingLeader}</#if>" maxlength="30" style="width: 200px;"/></span>
				</p>
				<p>
					<span>状态：</span>
					<#if provider?? && provider.isAvailable?? && provider.isAvailable == 1><span><input type="radio" name="isAvailable" checked="checked" value="1"/>可用</span>
					<span><input type="radio" name="isAvailable" value="0"/>停用</span>
					<#else><span><input type="radio" name="isAvailable" value="1"/>可用</span>
					<span><input type="radio" name="isAvailable" checked="checked" value="0"/>停用</span>
					</#if>
				</p>
			</form>
			<a class="easyui-linkbutton" style="width: 80px;" onclick="saveProvider()">保  存</a>
			<br>
			<br>
			<br>
			<br>
			<br>
		</div>
</div>
<div id="picDia" class="easyui-dialog" closed="true" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>
<script>
$(function() {
	$('input[name=isInvoice]').change(function() {
		var invoice = $('input[name=isInvoice]:checked').val();
		if(invoice == 1) {
			$('#tax').val('');
			$('#tax').removeAttr('disabled');
		}
		if(invoice == 0) {
			$('#tax').val('');
			$('#tax').attr('disabled','disabled');
		}
	});
	$('input[name=purchaseSubmitType]').change(function() {
		var submitType = $('input[name=purchaseSubmitType]:checked').val();
		if(submitType == 1) {
			$('.percent2').val('');
			$('.percent3').val('');
			$('.day3').val('');
			$('.day4').val('');
			$('.percent2').attr('disabled','disabled');
			$('.percent3').attr('disabled','disabled');
			$('.day3').attr('disabled','disabled');
			$('.day4').attr('disabled','disabled');
			$('#other').val('');
			$('#other').attr('disabled','disabled');
		}
		if(submitType == 2) {
			$('.percent2').removeAttr('disabled');
			$('.percent3').val('');
			$('.day3').val('');
			$('.day4').val('');
			$('.percent3').attr('disabled','disabled');
			$('.day3').attr('disabled','disabled');
			$('.day4').attr('disabled','disabled');
			$('#other').val('');
			$('#other').attr('disabled','disabled');
		}
		if(submitType == 3) {
			$('.percent3').removeAttr('disabled');
			$('.percent3').val('');
			$('.day3').val('');
			$('.day3').removeAttr('disabled');
			$('.percent2').val('');
			$('.percent2').attr('disabled','disabled');
			$('.day4').val('');
			$('.day4').attr('disabled','disabled');
			$('#other').val('');
			$('#other').attr('disabled','disabled');
		}
		if(submitType == 4) {
			$('.day4').val('');
			$('.day4').removeAttr('disabled');
			$('.percent3').val('');
			$('.percent3').attr('disabled','disabled');
			$('.day3').val('');
			$('.day3').attr('disabled','disabled');
			$('.percent2').val('');
			$('.percent2').attr('disabled','disabled');
			$('#other').val('');
			$('#other').attr('disabled','disabled');
		}
		if(submitType == 5) {
			$('#other').val('');
			$('#other').removeAttr('disabled');
			$('.percent2').val('');
			$('.percent3').val('');
			$('.day3').val('');
			$('.day4').val('');
			$('.percent2').attr('disabled','disabled');
			$('.percent3').attr('disabled','disabled');
			$('.day3').attr('disabled','disabled');
			$('.day4').attr('disabled','disabled');
		}
	});
});
	function picDialogOpen() {
		$('picForm').form('clear');
		$('#picDia').dialog('open');
	}
	// 图片上传
	function picUpload() {
        $('#picForm').form('submit',{
            url:"${rc.contextPath}/pic/fileUpLoad",
            success:function(data){
                var res = eval("("+data+")")
                if(res.status == 1){
                    $.messager.alert('响应信息',"上传成功...",'info',function(){
                        $("#picDia").dialog("close");
                        $('#contractImg').attr("src", res.url);
                        $('#contractImg').show();
                        $('#contractImgUrl').val(res.url);
                        return
                    });
                } else{
                    $.messager.alert('响应信息',res.msg,'error',function(){
                        return ;
                    });
                }
            }
        })
    }
	// 验证表单是否是否有效
	function valid() {
		if($('#name').val() == '') {
			$.messager.alert('警告','供应商名称不能为空');
			return false;
		}
		if(typeof($('input[name=type]:checked').val()) == 'undefined') {
			$.messager.alert('警告','供应商类型不能为空');
			return false;
		}
		if($('#providerBrand').val() == '') {
			$.messager.alert('警告','经营品牌不能为空');
			return false;
		}
		if($('#offices').val() == '') {
			$.messager.alert('警告','公司所在地不能为空');
			return false;
		}
		if(typeof($('input[name=currency]:checked').val()) == 'undefined') {
			$.messager.alert('警告','币种不能为空');
			return false;
		}
		if($('input[name=isInvoice]:checked').val() == 1) {
			if($('#tax').val() == '') {
				$.messager.alert('警告','税点不能为空');
				return false;
			}
		}
		if($('#contactPerson').val() == '') {
			$.messager.alert('警告','联系人不能为空');
			return false;
		}
		if($('#contactPhone').val() == '') {
			$.messager.alert('警告','联系电话不能为空');
			return false;
		}
		if(typeof($('input[name=purchaseSubmitType]:checked').val()) == 'undefined') {
			$.messager.alert('警告','采购款结算不能为空');
			return false;
		}
		if($('input[name=purchaseSubmitType]:checked').val() == 2) {
			if($('.percent2').val() == '') {
				$.messager.alert('警告','预付定金不能为空');
				return false;
			}
		}
		if($('input[name=purchaseSubmitType]:checked').val() == 3) {
			if($('.percent3').val() == '') {
				$.messager.alert('警告','预付定金不能为空');
				return false;
			}
			if($('.day3').val() == '') {
				$.messager.alert('警告','到货???天付清不能为空');
				return false;
			}
		}
		if($('input[name=purchaseSubmitType]:checked').val() == 4) {
			if($('.day4').val() == '') {
				$.messager.alert('警告','到货???天付清不能为空');
				return false;
			}
		}
		if($('input[name=purchaseSubmitType]:checked').val() == 5) {
			if($('#other').val() == '') {
				$.messager.alert('警告','其他采购款结算方式不能为空');
				return false;
			}
		}
		if($('#receiveBankAccount').val() == '') {
			$.messager.alert('警告','收款账号不能为空');
			return false;
		}
		if($('#receiveBank').val() == '') {
			$.messager.alert('警告','开户行不能为空');
			return false;
		}
		if($('#receiveBank').val() == '') {
			$.messager.alert('警告','开户行不能为空');
			return false;
		}
		if($('#receiveName').val() == '') {
			$.messager.alert('警告','收款方名称不能为空');
			return false;
		}
		if($('#purchasingLeader').val() == '') {
			$.messager.alert('警告','采购负责人不能为空');
			return false;
		}
		return true;
	}
	function openWindow() {
		var url = $('#contractImg').attr('src');
		if(url == '')
			return;
		window.open(url);
	}
	function saveProvider() {
		$('#editProviderForm').form('submit', {
			url : "${rc.contextPath}/purchase/saveOrUpdateProvider",
			onSubmit : function() {
				return valid();
			},
			success : function(data) {
				var res = eval("("+data+")");
				if (res.status == 1) {
					$.messager.alert('响应信息', "保存成功", 'info', function() {
						window.location.href="${rc.contextPath}/purchase/toProviderList";
					});
				} else if (res.status == 0) {
					$.messager.alert('响应信息', res.msg, 'error');
				}
			}
		});
	}
</script>
</body>
</html>