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
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/plugins/jquery.easyui.patch.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>

</head>
<body class="easyui-layout">
<div data-options="region:'center'" style="padding:10px 60px 20px 60px">
	<div data-options="region:'north',title:'采购单明细',split:true,height:'auto'">
	<input type="hidden" id="purchaseCode" value="<#if purchaseCode??>${purchaseCode}</#if>">
	<input type="hidden" id="orderId" value="<#if purchase?? && purchase.id??>${purchase.id}</#if>">
	<input type="hidden" id="status" value="<#if purchase?? && purchase.status??>${purchase.status}</#if>">
		<table>
			<tr><td>
				<span style="font-size:18px; font-weight:bolder; display:inline-block; width: 100px;">基本信息</span>
				<#if purchase?? && purchase.status?? && purchase.status!=4><a onclick="finished()" class="easyui-linkbutton" data-options="width:'80px'">采购完结</a></#if>
			</td></tr>
			<tr><td><span>采购单编号：</span><#if purchaseCode??>${purchaseCode}</#if></td></tr>
			<tr style="height: 15px;"><td></td></tr>
			<tr><td><span style="font-size:12px; font-weight:bolder; display:inline-block; width: 100px;">采购方信息：</span></td></tr>
			<tr>
				<td style="display:inline-block; width: 300px;"><span>收货仓库：</span><#if storage?? && storage.name??>${storage.name}</#if></td>
				<td><span>详细地址：</span><#if storage?? && storage.detailAddress??>${storage.detailAddress}</#if></td>
			</tr>
			<tr>
				<td><span>交货联系人：</span><#if storage?? && storage.contactPerson??>${storage.contactPerson}</#if></td>
				<td><span>联系方式：</span><#if storage?? && storage.contactPhone??>${storage.contactPhone}</#if></td>
			</tr>
			
			<tr style="height: 15px;"><td></td></tr>
			<tr><td style="font-size:12px; font-weight:bolder; display:inline-block; width: 100px;">供应商信息：</td></tr>
			<tr>
				<td><span>供应商名称：</span><#if provider?? && provider.name??>${provider.name}</#if></td>
				<td><span>公司名称：</span><#if provider?? && provider.companyName??>${provider.companyName}</#if></td>
				<td>
				<span>报价币种：</span>
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
				</td>
			</tr>
			<tr>
				<td><span>联系人：</span><#if provider?? && provider.contactPerson??>${provider.contactPerson}</#if></td>
				<td style="display:inline-block; width: 300px;"><span>联系电话：</span><#if provider?? && provider.contactPhone??>${provider.contactPhone}</#if></td>
				<td>
				<span>采购款结算：</span>
				<#if provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 1>款到发货
				<#elseif provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 2 && provider.percent??>预付定金${provider.percent}%，出货前付清
				<#elseif provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 3 && provider.percent?? && provider.day??>预付定金${provider.percent}%，出货${provider.day}天付清
				<#elseif provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 4 && provider.day??>到货后${provider.day}天付清
				<#elseif provider?? && provider.purchaseSubmitType?? && provider.purchaseSubmitType == 5 && provider.other??>${provider.other}
				</#if>
				</td>
			</tr>
			<tr>
				<td><span>收款账号：</span><#if provider?? && provider.receiveBankAccount??>${provider.receiveBankAccount}</#if></td>
				<td><span>开户行：</span><#if provider?? && provider.receiveBank??>${provider.receiveBank}</#if></td>
				<td><span>收款人：</span><#if provider?? && provider.receiveName??>${provider.receiveName}</#if></td>
			</tr>
			<tr>
				<td><span>Swift code：</span><#if provider?? && provider.swiftCode??>${provider.swiftCode}</#if></td>
				<td><span>银行地址：</span><#if provider?? && provider.bankAddress??>${provider.bankAddress}</#if></td>
			</tr>
		</table>
		<br>
		<hr/>
		<div>
			<span style="font-size:18px; font-weight:bolder; display:inline-block; width: 100px;">商品明细</span>
			<table id="s_purchaseProduct" style=""></table>
		</div>
		<br>
		<table>
			<tr>
				<td style="display:inline-block; width: 300px;"><span>采购总数：</span><#if purchase_purchaseTotalCount??>${purchase_purchaseTotalCount}</#if></td>
				<td><span>采购总金额：</span><#if purchase_purchaseTotalMoney??>${purchase_purchaseTotalMoney}</#if></td>
				<td><span>运费：</span><#if purchase_freightMoney??>${purchase_freightMoney}</#if></td>
			</tr>
			<tr>
				<td><span>合计金额：</span><#if purchase_totalMoney??>${purchase_totalMoney}</#if></td>
				<td>
					<span>是否含税：</span>
					<#if purchase?? && purchase.isTax?? && purchase.isTax == 1>含税
					<#else>不含税
					</#if>
				</td>
			</tr>
			<tr>
				<td style="font-size:12px; font-weight:bolder;"><span>应付金额：</span><span id="payableMoney"><#if purchase_payableMoney??>${purchase_payableMoney}</#if></span></td>
				<td style="display:inline-block; width: 300px;"><span>应付金额调整备注：</span><#if purchase?? && purchase.payableMoneyRemark??>${purchase.payableMoneyRemark}</#if></td>
			</tr>
			<tr>
				<td><span>采购备注：</span><#if purchase?? && purchase.remark??>${purchase.remark}</#if></td>
			</tr>
			
			<tr style="height: 15px;"><td></td></tr>
			<tr>
				<td><span>入库总数：</span><span id="totalStoringCount"></span></td>
				<td><span>入库备注：</span><span id="totalStoringRemark"><#if purchase?? && purchase.storingRemark??>${purchase.storingRemark}</#if></span></td>
			</tr>
			<tr>
				<td><span style="font-size:12px; font-weight:bolder;">金额调整：</span><span id="adjustMoney"><#if purchase_adjustMoney??>${purchase_adjustMoney}</#if></span></td>
				<td><span>是否计入本次结款：</span><span id="isAdjustAppendOrder"><#if purchase?? && purchase.isAdjustAppendOrder?? && purchase.isAdjustAppendOrder == 1>计入<#else>不计入</#if></span></td>
			</tr>
			<tr>
				<td><span>金额调整备注：</span><span id="adjustRemark"><#if purchase?? && purchase.adjustRemark??>${purchase.adjustRemark}</#if></span></td>
			</tr>
			<tr style="height: 15px;"><td></td></tr>
			<tr><td><span style="font-size:14px; font-weight:bolder;">实付金额：</span>
			<span id="realMoney">
				<#if purchase_realMoney??>${purchase_realMoney}</#if>
			</span></td></tr>
		</table>
		<br>
		<hr/>
		<div>
			<span style="font-size:18px; font-weight:bolder; display:inline-block; width: 100px;">结款明细</span>
			<div><table id="s_purchasePayDetail" style=""></table></div>
		</div>
		<br>
		<table>
			<tr>
				<td><span style="display:inline-block; width: 300px;">已付金额：<#if payDetail_sumPaidMoney??>${payDetail_sumPaidMoney}<#else>0</#if></span></td>
				<td><span>已付金额/RMB：</span><#if payDetail_sumPaidMoneyRMB??>${payDetail_sumPaidMoneyRMB}<#else>0</#if></td>
			</tr>
			<tr>
				<td><span>未付金额：</span>
				<#if payDetail_unPaidMoney??>${payDetail_unPaidMoney}
				<#else>${purchase_realMoney}
				</#if></td>
			</tr>
		</table>
		<br>
		<hr/>
		<div>
			<span style="font-size:18px; font-weight:bolder; display:inline-block; width: 100px;">商品入库</span>
			<div><table id="s_productStoring" style=""></table></div>
		</div>
		<br>
		<hr/>
		<div>
			<span style="font-size:18px; font-weight:bolder; display:inline-block; width: 100px;">采购凭证</span>
			<#if purchase?? && purchase.status?? && purchase.status!=4><a onclick="picDialogOpen()" class="easyui-linkbutton">上传采购凭证</a></#if>
			<#if purchase?? && purchase.purchaseEvidenceUrl?? && purchase.purchaseEvidenceUrl == ''>
			<br><img height="200" width="200" onclick="openWindow()" style="display: none;" id="purchaseEvidenceUrl" src="<#if purchase?? && purchase.purchaseEvidenceUrl??>${purchase.purchaseEvidenceUrl}</#if>">
			<#else><br><img height="200" width="200" onclick="openWindow()" id="purchaseEvidenceUrl" src="<#if purchase?? && purchase.purchaseEvidenceUrl??>${purchase.purchaseEvidenceUrl}</#if>">
			</#if>
		</div>
		<br>
		<br>
		<br>
		<br>
		<br>
	</div>
</div>
<#if purchase?? && purchase.status?? && purchase.status!=4>
<div id="adjustMoneyDiv" class="easyui-dialog" style="width: 800px; height: 250px; padding: 15px 20px;">
	<form id="adjustMoneyDivForm" method="post">
		<p>
			<input type="hidden" name="id" value="<#if purchase?? && purchase.id??>${purchase.id}</#if>"/>
			<span>是否计入本次采购结款：</span>
			<span>
				<input type="radio" name="isAdjustAppendOrder" value="1"/>计入
				<input type="radio" name="isAdjustAppendOrder" value="0"/>不计入
			</span>
		</p>
		<p>
			<span>入库金额调整：</span><span><input type="text" class="easyui-numberbox" data-options="precision:2" name="adjustMoney" id="formAdjustMoney"/></span>
		</p>
		<p>
			<span>金额调整备注：</span><span><input type="text" style="width: 600px;" name="adjustRemark"/></span>
		</p>
	</form>
</div>

<div id="payDetailDiv" class="easyui-dialog" style="width: 800px; height: 300px; padding: 15px 20px;">
	<form id="payDetailDivForm" method="post">
		<p>
			<input type="hidden" name="purchaseCode" value="<#if purchaseCode??>${purchaseCode}</#if>">
			<input type="hidden" name="storageId" value="<#if storage?? && storage.id??>${storage.id}</#if>">
			<input type="hidden" name="providerId" value="<#if purchaseCode?? && provider.id??>${provider.id}</#if>">
			<input type="hidden" name="id" value=""/>
			<span>付款类型：</span>
			<span>
				<input type="checkbox" name="type" value="1"/>定金
				<input type="checkbox" name="type" value="2"/>货款
				<input type="checkbox" name="type" value="3"/>运费
			</span>
		</p>
		<p>
			<span>付款金额：</span><span><input type="text" onkeyup="amount(this)" onBlur="processRMB()" name="payMoney"/></span>
		</p>
		<p>
			<span>当日汇率：</span><span><input type="text" onkeyup="amount1(this)" onBlur="processRMB()" name="currentRate"/></span>
		</p>
		<p>
			<span>付款金额/RMB：</span><span id="payRMB"></span>
		</p>
		<p>
			<span>付款备注：</span><span><input type="text" style="width: 600px;" name="remark"/></span>
		</p>
	</form>
</div>

<div id="orderProductStoringDiv" class="easyui-dialog" style="width: 1300px; height: 700px;">
	<div><table id="s_orderProductStoring" style=""></table></div>
	<div><p>&nbsp;&nbsp;入库备注：<input style="width: 800px;" id="storingRemark" value="<#if purchase?? && purchase.storingRemark??>${purchase.storingRemark}</#if>" /></p></div>
</div>

<div id="picDia" class="easyui-dialog" closed="true" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>
</#if>
</body>
<script>
function openWindow() {
	var url = $('#purchaseEvidenceUrl').attr('src');
	if(url == '')
		return;
	window.open(url);
}
function picDialogOpen() {
	$('picForm').form('clear');
	$('#picDia').dialog('open');
}
//图片上传
function picUpload() {
	$.messager.progress();
    $('#picForm').form('submit', {
        url:"${rc.contextPath}/pic/fileUpLoad",
        async:false,
        success:function(data) {
        	$.messager.progress('close');
            var res = eval("("+data+")")
            if(res.status == 1) {
            	$.messager.alert('响应信息',"图片上传成功，点击确定将采购凭证保存到采购单。",'info', function() {
            		$.messager.progress();
            		$.ajax({
            			url:'${rc.contextPath}/purchase/saveOrUpdateOrder',
                    	async:false,
                    	type:'POST',
                    	data:{id:$('#orderId').val(),purchaseEvidenceUrl:res.url},
                    	success:function(data) {
                    		$.messager.progress('close');
                    		if(data.status == 1) {
                    			$("#picDia").dialog("close");
                                $('#purchaseEvidenceUrl').attr("src", res.url);
                                $('#purchaseEvidenceUrl').show();
                    		} else {
                    			$.messager.alert('响应信息', data.msg,'error');
                    		}
                    	}
            		});
                });
            } else{
                $.messager.alert('响应信息',res.msg,'error',function(){
                    return ;
                });
            }
        }
    });
}
function finished() {
	$.messager.confirm("提示", "确认完结采购单", function(r) {
		if(r) {
			$.ajax({
				url:'${rc.contextPath}/purchase/saveOrUpdateOrder',
				type:'POST',
				data:{id:$('#orderId').val(),purchaseCode:$('#purchaseCode').val(),status:4},
				success:function(data) {
					if(data.status == 1) {
						$.messager.alert('提示', '完结采购单成功', 'info', function() {
							window.location.href='${rc.contextPath}/purchase/toOrderList';
						});
					} else {
						$.messager.alert('提示', data.msg, 'error');
					}
				}
			});
		}
	})
}
function processRMB() {
	var paymoney = $('#payDetailDivForm').find('input[name=payMoney]').val();
	var currentRate = $('#payDetailDivForm').find('input[name=currentRate]').val();
	if(paymoney == '' || currentRate == '') {
		$('#payDetailDivForm').find('input[name=payRMB]').val(0);
		$('#payRMB').text(0);
		return ;
	}
	//multiple
	var payRMB = Number(paymoney) * Number(currentRate);
	$('#payRMB').text(payRMB.toFixed(2));
}
function amount1(th){
    var regStrs = [
        ['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
        ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
        ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
        ['^(\\d+\\.\\d{10}).+', '$1'] //禁止录入小数点后两位以上
    ];
    for(i=0; i<regStrs.length; i++){
        var reg = new RegExp(regStrs[i][0]);
        th.value = th.value.replace(reg, regStrs[i][1]);
    }
}
function amount(th){
    var regStrs = [
        ['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
        ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
        ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
        ['^(\\d+\\.\\d{2}).+', '$1'] //禁止录入小数点后两位以上
    ];
    for(i=0; i<regStrs.length; i++){
        var reg = new RegExp(regStrs[i][0]);
        th.value = th.value.replace(reg, regStrs[i][1]);
    }
}
function editPayDetail(type, id, payMoney, currentRate, payRMB, remark) {
	$('#payDetailDivForm').find('input[name=type]').each(function(i) {
		if(type.indexOf($(this).val()) != -1) {
			$(this).prop('checked', 'checked');
		}
	});
	$('#payDetailDivForm').find('input[name=id]').val(id);
	$('#payDetailDivForm').find('input[name=payMoney]').val(payMoney);
	$('#payDetailDivForm').find('input[name=currentRate]').val(currentRate);
	$('#payRMB').text(payRMB);
	$('#payDetailDivForm').find('input[name=remark]').val(remark);
	$('#payDetailDiv').dialog('open');
}
function delPayDetail(id) {
	$.messager.confirm("提示", "删除确认", function(r) {
		if(r) {
			$.ajax({
				url:'${rc.contextPath}/purchase/deletePurchasePay/'+id,
				type:'POST',
				success:function(data) {
					if(data.status == 1) {
						$('#s_purchasePayDetail').datagrid('load');
					} else {
						$.messager.alert('提示', data.msg, 'error');
					}
				}
			});
		}
	})
}
	$(function() {
		$('#orderProductStoringDiv').dialog({
			title:'商品入库管理',
			closed:true,
			collapsible : true,
			modal : true,
			buttons : [ {
				text : '确认入库并完结SKU',
				iconCls : 'icon-lock',
				handler : function() {
					$.messager.confirm('提示', '确定入库并完结SKU', function(r) {
						if(r) {
							var arr = new Array();
							var data = $('#s_orderProductStoring').datagrid('getData');
							$.each(data.rows, function() {
								if(typeof(this.currentStoringCount) != 'undefined' && this.currentStoringCount != null && this.currentStoringCount != '' && this.currentStoringCount != '0') {
									arr.push(JSON.stringify(this));
								}
							});
							if(arr.length < 1) {
								$.messager.alert('提示', '没有数据', 'warning');
								return ;
							}
							$.ajax({
								url : '${rc.contextPath}/purchase/savePurchaseProductStoring',
								type : 'POST',
								data : {params:'['+arr.join('')+']', status:3, storingRemark:$('#storingRemark').val()},
								success:function(data) {
									if(data.status == 1) {
										$.messager.alert('提示', '操作成功', 'info', function() {
											$('#orderProductStoringDiv').dialog('close');
											$('#s_purchaseProduct').datagrid('load');
											$('#s_productStoring').datagrid('load');
										});
									} else {
										$.messager.alert('提示',data.msg,'error');
									}
								}
							});
						}
					});
				}
			},{
				text : '仅确认入库',
				iconCls : 'icon-edit',
				handler : function() {
					$.messager.confirm('提示', '确定入库', function(r) {
						if(r) {
							var arr = new Array();
							var data = $('#s_orderProductStoring').datagrid('getData');
							$.each(data.rows, function() {
								if(typeof(this.currentStoringCount) != 'undefined' && this.currentStoringCount != null && this.currentStoringCount != '' && this.currentStoringCount != '0') {
									arr.push(JSON.stringify(this));
								}
							});
							if(arr.length < 1) {
								$.messager.alert('提示', '没有数据', 'warning');
								return ;
							}
							$.ajax({
								url : '${rc.contextPath}/purchase/savePurchaseProductStoring',
								type : 'POST',
								data : {params:'['+arr.join('')+']', status:1, storingRemark:$('#storingRemark').val()},
								success:function(data) {
									if(data.status == 1) {
										$.messager.alert('提示', '操作成功', 'info', function() {
											$('#orderProductStoringDiv').dialog('close');
											$('#s_purchaseProduct').datagrid('load');
											$('#s_productStoring').datagrid('load');
										});
									} else {
										$.messager.alert('提示',data.msg,'error');
									}
								}
							});
						}
					});
				}
			},{
				text : '仅完结SKU',
				iconCls : 'icon-ok',
				handler : function() {
					$.messager.confirm('提示', '确定SKU', function(r) {
						if(r) {
							var flag = false;
							var arr = new Array();
							var data = $('#s_orderProductStoring').datagrid('getChecked');
							$.each(data, function() {
								if(typeof(this.currentStoringCount) == 'undefined' || this.currentStoringCount == null || this.currentStoringCount == '0' || this.currentStoringCount == '') {
									arr.push(JSON.stringify(this));
								} else {
									$.messager.alert('警告', '本次入库数不合法，不可以执行仅完结SKU操作', 'warning');
									flag = true;
									return false;
								}
							});
							if(flag) 
								return;
							if(arr.length < 1) {
								$.messager.alert('提示', '没有数据', 'warning');
								return ;
							}
							$.ajax({
								url : '${rc.contextPath}/purchase/savePurchaseProductStoring',
								type : 'POST',
								data : {params:'['+arr.join('')+']', status:2, storingRemark:$('#storingRemark').val()},
								success:function(data) {
									if(data.status == 1) {
										$.messager.alert('提示', '操作成功', 'info', function() {
											$('#s_orderProductStoring').datagrid('clearChecked');
											$('#orderProductStoringDiv').dialog('close');
											$('#s_purchaseProduct').datagrid('load');
											$('#s_productStoring').datagrid('load');
										});
									} else {
										$.messager.alert('提示',data.msg,'error');
									}
								}
							});
						}
					});
				}
			}]
		});
		$('#payDetailDiv').dialog({
			title:'新增付款',
			closed:true,
			collapsible : true,
			modal : true,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
					$('#payDetailDivForm').form('submit',{
						url:'${rc.contextPath}/purchase/saveOrUpdatePurchasePayDetail',
						onSubmit:function() {
							if(typeof($('#payDetailDivForm').find('input[name=type]:checked').val()) == 'undefined') {
								$.messager.alert('警告', '付款类型不能为空', 'warning');
								return false;
							}
							if($('#payDetailDivForm').find('input[name=payMoney]').val() == '') {
								$.messager.alert('警告', '调整金额不能为空', 'warning');
								return false;
							}
							if($('#payDetailDivForm').find('input[name=currentRate]').val() == '') {
								$.messager.alert('警告', '当日汇率不能为空', 'warning');
								return false;
							}
							if($('#payRMB').text() == '') {
								$.messager.alert('警告', '付款金额/RMB还没有计算', 'warning');
								return false;
							}
							return true;
						},
						success:function(data) {
							var data = eval('(' + data + ')');
							if(data.status == 1) {
								$.messager.alert('提示', '操作成功', 'info', function() {
									$('#payDetailDiv').dialog('close');
									$('#s_purchasePayDetail').datagrid('load');
								});
							} else {
								$.messager.alert('提示',data.msg,'error');
							}
						}
					});
				}
			}, {
				text : '取消',
				align : 'left',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#payDetailDiv').dialog('close');
				}
			}]
		});
		$('#adjustMoneyDiv').dialog({
			title:'金额调整',
			closed:true,
			collapsible : true,
			modal : true,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
					$('#adjustMoneyDivForm').form('submit',{
						url:'${rc.contextPath}/purchase/saveOrUpdateOrder',
						onSubmit:function() {
							if(typeof($('#adjustMoneyDivForm').find('input[name=isAdjustAppendOrder]:checked').val()) == 'undefined') {
								$.messager.alert('警告', '是否计入本次采购结款不能为空', 'warning');
								return false;
							}
							
							if($('#formAdjustMoney').numberbox('getValue') == '') {
								$.messager.alert('警告', '调整金额不能为空', 'warning');
								return false;
							}
							return true;
						},
						success:function(data) {
							var data = eval('(' + data + ')');
							if(data.status == 1) {
								$.messager.alert('提示', '操作成功', 'info', function() {
									var multiple = 100;
									$('#adjustMoneyDiv').dialog('close');
									$('#adjustMoney').text($('#formAdjustMoney').numberbox('getValue'));
									if($('#adjustMoneyDivForm').find('input[name=isAdjustAppendOrder]:checked').val() == 1) {
										$('#isAdjustAppendOrder').text('记录');
										$('#realMoney').text(((Number($('#payableMoney').text()) * multiple + Number($('#formAdjustMoney').numberbox('getValue')) * multiple ) / multiple).toFixed(2));
									} else {
										$('#isAdjustAppendOrder').text('不记录');
										$('#realMoney').text(Number($('#payableMoney').text()));
									}
									$('#adjustRemark').text($('#adjustMoneyDivForm').find('input[name=adjustRemark]').val());
								});
							} else {
								$.messager.alert('提示',data.msg,'error');
							}
						}
					});
				}
			}, {
				text : '取消',
				align : 'left',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#adjustMoneyDiv').dialog('close');
				}
			}]
		});
		// 采购单的商品列表
		$('#s_purchaseProduct').datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'id',
			url : '${rc.contextPath}/purchase/findOrderProductListInfo?purchaseCode=' + $('#purchaseCode').val(),
			loadMsg : '正在装载数据...',
			singleSelect : false,
			fitColumns : true,
			remoteSort : true,
			pageSize : 150,
			pagination : false,
			columns : [ [ 
{				field : 'providerProductId',				title : '采购商品ID',				width : 25,				align : 'center'			}, 
{				field : 'barcode',				title : '商品条码',				width : 30,				align : 'center'			}, 
{				field : 'productName',				title : '商品名称',				width : 70,				align : 'center'			}, 
{				field : 'brandName',				title : '品牌',				width : 40,				align : 'center'			}, 
{				field : 'isFinished',				title : '是否完结',				width : 20,				align : 'center',				
	  formatter : function(value, row, index) {
		  if(row.isFinished == 1)
			return '完结';
		else 
			return '未完结';
	}
}, 
{				field : 'purchaseQuantity',				title : '采购数',				width : 20,				align : 'center'			}, 
{				field : 'storingCount',				title : '入库数',				width : 20,				align : 'center'			}, 
{				field : 'providerPrice',				title : '供货单价',				width : 20,				align : 'center'			}, 
{				field : 'totalPrice',				title : '合计金额',				width : 20,				align : 'center'			}, 
{				field : 'shareFreightMoney',				title : '分摊运费',				width : 20,				align : 'center'			}, 
{				field : 'providerPriceWithFreight',				title : '含运费单价',				width : 20,				align : 'center'			}, 
{				field : 'specification',				title : '规格',				width : 30,				align : 'center'			}, 
{				field : 'purchaseUnit',				title : '采购单位',				width : 25,				align : 'center'			}, 
{				field : 'boxSpecification',				title : '箱规',				width : 30,				align : 'center'			}, 
{				field : 'manufacturerDate',				title : '生产日期',				width : 20,				align : 'center'			}, 
{				field : 'durabilityPeriod',				title : '保质期',				width : 20,				align : 'center'			} 
] ],
toolbar : [ {
	id : '_add0',
	text : '金额调整',
	iconCls : 'icon-add',
	handler : function() {
		if($('#status').val() == '4')
			return;
		$.messager.progress();
		$.ajax({
			url:'${rc.contextPath}/purchase/findAdjustMoneyInfo',
			type:'POST',
			data:{purchaseCode:$('#purchaseCode').val()},
			success: function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					$('#adjustMoneyDivForm').form('load', data.data);
					$('#adjustMoneyDiv').dialog('open');
		        } else{
		        	$.messager.progress('close');	
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	}
} ],
onLoadSuccess : function(data) {
	var totalStoringCount = 0;
	$.each(data.rows, function() {
		totalStoringCount += Number(this.storingCount);
	});
	$('#totalStoringCount').text(totalStoringCount);
	$('#totalStoringRemark').text($('#storingRemark').val());
}
		});
		$('#s_purchasePayDetail').datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'id',
			url : '${rc.contextPath}/purchase/findPurchasePayDetailByParam?purchaseCode='+$('#purchaseCode').val(),
			loadMsg : '正在装载数据...',
			singleSelect : false,
			fitColumns : true,
			remoteSort : true,
			pageSize : 150,
			pagination : false,
			columns : [ [ {
				field : 'id',
				title : '序号',
				width : 20,
				align : 'left'
			}, {
				field : 'createTime',
				title : '创建时间',
				width : 40,
				align : 'center'
			}, {
				field : 'type',
				title : '付款类型',
				width : 70,
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
				width : 40,
				align : 'center'
			}, {
				field : 'currentRate',
				title : '当日汇率',
				width : 30,
				align : 'center'
			}, {
				field : 'payRMB',
				title : '付款金额/RMB',
				width : 30,
				align : 'center'
			}, {
				field : 'remark',
				title : '付款备注',
				width : 40,
				align : 'center'
			}, {
				field : 'op',
				title : '操作',
				width : 30,
				align : 'center',
				formatter : function(value, row, index) {
					if(row.isPaid == 0) {
						var a = '<a href="javascript:void(0);" onclick="editPayDetail(\'' + row.type + '\',' + row.id + ',' + row.payMoney + ',' + row.currentRate + ',' + row.payRMB + ',\'' + row.remark + '\')">修改</a> | ';
						var b = '<a href="javascript:void(0);" onclick="delPayDetail(' + row.id + ')">删除</a> ';
						return a + b;
					}
					return '已付';
				}
			} ] ],
			toolbar : [ {
				id : '_add1',
				text : '新增付款',
				iconCls : 'icon-add',
				handler : function() {
					if($('#status').val() == '4')
						return;
					
					$('#payDetailDivForm').find('input[name=type]').each(function(i) {
						$(this).prop('checked', false);
					});
					$('#payDetailDivForm').find('input[name=id]').val('');
					$('#payDetailDivForm').find('input[name=payMoney]').val('');
					$('#payDetailDivForm').find('input[name=currentRate]').val('');
					$('#payDetailDivForm').find('input[name=remark]').val('');
					$('#payRMB').text('');
					$('#payDetailDiv').dialog('open');
				}
			} ]
		});
		$('#s_productStoring').datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'id',
			url : '${rc.contextPath}/purchase/purchaseProductStoringListInfo?purchaseCode=' + $('#purchaseCode').val(),
			loadMsg : '正在装载数据...',
			singleSelect : false,
			fitColumns : true,
			remoteSort : true,
			pageSize : 150,
			pagination : false,
			columns : [ [ {
				field : 'createTime',
				title : '入库时间',
				width : 40,
				align : 'center'
			},{
				field : 'status',
				title : '入库状态',
				width : 30,
				align : 'center',
				formatter : function(value, row, index) {
					if (row.status == 1) {
						return '仅确认入库';
					} else if (row.status == 2) {
						return '仅完结SKU';
					} else if (row.status == 3) {
						return '确认入库并完结SKU';
					}
					return row.status;
				}
			}, {
				field : 'barcode',
				title : '商品条码',
				width : 40,
				align : 'center'
			}, {
				field : 'name',
				title : '商品名称',
				width : 60,
				align : 'center'
			}, {
				field : 'brandName',
				title : '品牌',
				width : 40,
				align : 'center'
			}, {
				field : 'purchaseQuantity',
				title : '采购数',
				width : 20,
				align : 'center'
			}, {
				field : 'storingCount',
				title : '本次入库数',
				width : 20,
				align : 'center'
			}, {
				field : 'providerPrice',
				title : '供货单价',
				width : 20,
				align : 'center'
			}, {
				field : 'totalPrice',
				title : '合计金额',
				width : 20,
				align : 'center'
			}, {
				field : 'specification',
				title : '规格',
				width : 30,
				align : 'center'
			}, {
				field : 'purchaseUnit',
				title : '采购单位',
				width : 30,
				align : 'center'
			}, {
				field : 'boxSpecification',
				title : '箱规',
				width : 30,
				align : 'center'
			}, {
				field : 'manufacturerDate',
				title : '生产日期',
				width : 30,
				align : 'center'
			}, {
				field : 'durabilityPeriod',
				title : '保质期',
				width : 30,
				align : 'center'
			} ] ],
			toolbar : [ {
				id : '_add2',
				text : '商品入库',
				iconCls : 'icon-add',
				handler : function() {
					if($('#status').val() == '4')
						return;
					$('#storingRemark').val($('#totalStoringRemark').text());
					$('#orderProductStoringDiv').dialog('open');
					$('#s_orderProductStoring').datagrid('load');
					$('#s_orderProductStoring').datagrid('clearChecked');
				}
			} ]
		});
		$('#s_orderProductStoring').datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'id',
			url : '${rc.contextPath}/purchase/findOrderProductListInfo?purchaseCode=' + $('#purchaseCode').val() + '&isDetail=1',
			loadMsg : '正在装载数据...',
			singleSelect : false,
			fitColumns : true,
			maxHeight : 550,
			remoteSort : true,
			pageSize : 150,
			pagination : false,
			columns : [[ 
						{field:'id',  title:'', width:20, align:'left', checkbox:true},
				{	field : 'barcode',			title : '商品条码',			width : 30,			align : 'center'		}, 
				{	field : 'productName',			title : '商品名称',			width : 70,			align : 'center'		}, 
				{	field : 'brandName',			title : '品牌',			width : 40,			align : 'center'		}, 
				{	field : 'purchaseQuantity',			title : '采购数',			width : 20,			align : 'center'		}, 
				{	field : 'storingCount',			title : '已入库',			width : 20,			align : 'center'		}, 
				{	field : 'currentStoringCount',			title : '本次入库数',			width : 20,			align : 'center',		editor:{type:'validatebox'}}, 
				{	field : 'providerPrice',			title : '供货单价',			width : 20,			align : 'center'		}, 
				{	field : 'specification',			title : '规格',			width : 30,			align : 'center'		}, 
				{	field : 'purchaseUnit',			title : '采购单位',			width : 25,			align : 'center'		}, 
				{	field : 'manufacturerDate',			title : '生产日期',			width : 30,			align : 'center',		editor:{type:'datebox'}}, 
				{	field : 'durabilityPeriod',			title : '保质期',			width : 30,			align : 'center',		editor:{type:'validatebox'}},
				{	field : 'op',			title : '操作',			width : 25,			align : 'center',		
					formatter : function(value, row, index) {
						if (row.editing) {
							var a = '<a href="javascript:void(0);" onclick="saverow(' + index + ')">确定</a> | ';
							var b = '<a href="javascript:void(0);" onclick="cancelrow(' + index + ')">取消</a>';
							return a + b;
						} else {
							return '<a href="javascript:void(0);" onclick="edit(' + index + ')">修改</a>';
						}
					}
				}]],
			toolbar : [{
				id : '_storing',
				text : '一键入库',
				iconCls : 'icon-edit',
				handler : function() {
					var checkedRows = $('#s_orderProductStoring').datagrid('getChecked');
					if(checkedRows.length == 0) {
						$.messager.alert('提示', '未选中商品', 'info');
						return ;
					}
					$.each(checkedRows, function(i, item) {
						$('#s_orderProductStoring').datagrid('updateRow',{
					    	index:$('#s_orderProductStoring').datagrid('getRowIndex', item.id),
					    	row:{currentStoringCount:this.purchaseQuantity}
						});
					});
				}
			}],
			onBeforeEdit:function(index, row){
	        	row.editing = true;
	        	updateActions();
	    	},
	    	onAfterEdit:function(index, row){
	        	row.editing = false;
	        	updateActions();
	    	},
	    	onCancelEdit:function(index, row){
	        	row.editing = false;
	        	updateActions();
	    	}
		});
	});
	//编辑排序
	function edit(index){
		$('#s_orderProductStoring').datagrid('beginEdit', index);
	};
	// 保存保存
	function saverow(index){
		$('#s_orderProductStoring').datagrid('endEdit', index);
	};
	// 取消编辑
	function cancelrow(index){
		$('#s_orderProductStoring').datagrid('cancelEdit', index);
	};
	// 跟新gird行数据
	function updateActions(){
		var rowcount = $('#s_orderProductStoring').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_orderProductStoring').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}
</script>
</html>