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

<style>
	span,p{font-size:14px;}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center'" style="padding: 5px;padding-left: 15px;">
		<div style="padding:10px">
			<input type="hidden" id="refundId" value="${refund.id}"/>
			<input type="hidden" id="accountId" value="${refund.accountId}"/>
			<input type="hidden" id="appChannel" value="${refund.appChannel}"/>
			<p>
				<span style="color:red">【退款状态】</span><span>&nbsp;<#if refund.statusStr?exists>${refund.statusStr}</#if></span>
                <span style="color:red">&nbsp;&nbsp;&nbsp;&nbsp;<#if refund.operationStatus?exists>${refund.operationStatus}</#if></span>
			</p>
			<p>
			<span style="color:red">订单状态：<#if refund.orderStatus?exists>${refund.orderStatus}</#if></span>&nbsp;&nbsp;
				用户名：<#if refund.username?exists>${refund.username}</#if>&nbsp;&nbsp;
				订单编号：<#if refund.number?exists>${refund.number}</#if>&nbsp;&nbsp;
				订单总价：<#if refund.totalPrice?exists>${refund.totalPrice}</#if>&nbsp;&nbsp;
				<span style="color:red">订单实付金额：<#if refund.realPrice?exists>${refund.realPrice}</#if></span>&nbsp;&nbsp;
                订单类型：<#if refund.orderType?exists>${refund.orderType}</#if>
				<br>
				<span>收货人：<#if refund.receiveName?exists>${refund.receiveName}</#if>&nbsp;&nbsp;
				收货手机：<#if refund.receiveMobile?exists>${refund.receiveMobile}</#if>&nbsp;&nbsp;
				收货地址：<#if refund.address?exists>${refund.address}</#if></span>
				<p>
				<span>快递单号：${refund.logisticsChannel!""}&nbsp;${refund.logisticsNumber!""}</span>&nbsp;&nbsp;
				<span>最新物流：${refund.logisticsDetail!""}</span>
				</p>
			</p>
			
			<a href="${rc.contextPath}/order/detail/${refund.orderId}" target="_blank" class="easyui-linkbutton" style="width:100px">查看订单明细</a>
			<p>
				<span style="color:red">申请类型：<#if refund.typeStr?exists>${refund.typeStr}</#if></span>
				<span>&nbsp;&nbsp;退款商品id：<#if refund.productId?exists>${refund.productId}</#if>&nbsp;&nbsp;
				商品名称：<#if refund.productName?exists>${refund.productName}</#if>&nbsp;&nbsp;
				商品价格：<#if refund.salesPrice?exists>${refund.salesPrice}</#if>&nbsp;&nbsp;
				商品数量：<#if refund.productCount?exists>${refund.productCount}</#if></span><br>
				<span style="color:red">
						申请退款商品数量：<#if (refund.orderPrductRefundInfo.selectProductCount)??>${refund.orderPrductRefundInfo.selectProductCount}</#if>&nbsp;&nbsp;&nbsp;
						商品总价：<#if (refund.orderPrductRefundInfo.selectPrice)??>${refund.orderPrductRefundInfo.selectPrice}</#if>&nbsp;&nbsp;&nbsp;
						分摊邮费：<#if (refund.orderPrductRefundInfo.freightMoneyProportion)??>${refund.orderPrductRefundInfo.freightMoneyProportion}</#if>&nbsp;&nbsp;&nbsp;
						分摊优惠券：<#if (refund.orderPrductRefundInfo.couponProportion)??>${refund.orderPrductRefundInfo.couponProportion}</#if>&nbsp;&nbsp;&nbsp;
						分摊积分抵扣：<#if (refund.orderPrductRefundInfo.pointProportion)??>${refund.orderPrductRefundInfo.pointProportion}</#if>&nbsp;&nbsp;&nbsp;
						分摊满减金额：<#if (refund.orderPrductRefundInfo.activitiesProportion)??>${refund.orderPrductRefundInfo.activitiesProportion}</#if>&nbsp;&nbsp;&nbsp;
						分摊N元立减优惠金额：<#if (refund.orderPrductRefundInfo.activitiesOptionalPartProportion)??>${refund.orderPrductRefundInfo.activitiesOptionalPartProportion}</#if>&nbsp;&nbsp;&nbsp;
						分摊客服改价抵扣：<#if (refund.orderPrductRefundInfo.adjustProportion)??>${refund.orderPrductRefundInfo.adjustProportion}</#if><br/>
						理论申请退款金额 ：<#if (refund.orderPrductRefundInfo.logicApplyPrice)??>${refund.orderPrductRefundInfo.logicApplyPrice}</#if>&nbsp;&nbsp;&nbsp;
						理论应返还积分：<#if (refund.orderPrductRefundInfo.logicGiveAccountPoint)??>${refund.orderPrductRefundInfo.logicGiveAccountPoint}</#if>&nbsp;&nbsp;&nbsp;
						理论应扣除积分：<#if (refund.orderPrductRefundInfo.logicRemoveAccountPoint)??>${refund.orderPrductRefundInfo.logicRemoveAccountPoint}</#if><br/><br/>
						实际申请退款金额：<#if refund.refundMoney?exists>${refund.refundMoney}</#if></span>
					</span>
			</p>
			<p>
				<span>退款到：<#if refund.bankTypeDesc?exists>${refund.bankTypeDesc}</#if></span><br>
				<#if refund.refundPayType?exists && refund.refundPayType == "1">
					<span>姓名：<#if refund.cardName?exists>${refund.cardName}</#if>&nbsp;&nbsp;
					开户行：<#if refund.bankName?exists>${refund.bankName}</#if>&nbsp;&nbsp;
					卡号：<#if refund.cardNumber?exists>${refund.cardNumber}</#if></span>
				</#if>
			</p>
			<p><span style="color:red">【退款说明】</span><span>&nbsp;<#if refund.explain?exists>${refund.explain}</#if></span></p>
			<p><span style="color:red">【附加图片】</span></p>
			<div>
				<#if refund.image1?exists>
					<a href="${refund.normalImage1}" target="_blank"><img src="${refund.image1}" style="width:100px;height:100px;" ></a>
				</#if>
				<#if refund.image2?exists>
					<a href="${refund.normalImage2}" target="_blank"><img src="${refund.image2}" style="width:100px;height:100px;" ></a>
				</#if>
				<#if refund.image3?exists>
					<a href="${refund.normalImage3}" target="_blank"><img src="${refund.image3}" style="width:100px;height:100px;" ></a>
				</#if>
			</div>
		</div>
		
	<!-- 处理一    begin -->
		<#if teackMap.step1?exists>
			<div style="padding:10px">
				<p><span style="color:red">【处理1】客服处理退款申请</span><br>
				--------------------------------------------------------------------------------------------------------------------------------------</p>
				<p>
					<span style="color:red">申请退款金额：<#if refund.refundMoney?exists>${refund.refundMoney}</#if>&nbsp;&nbsp;&nbsp;</span>
					<span>实际处理结果为：</span><font size="7" ><#if teackMap.step1.content?exists>${teackMap.step1.content}</#if></font>
					&nbsp;&nbsp;&nbsp;<a onclick="updateRefundPrice()" href="javascript:;" class="easyui-linkbutton" style="width:80px">修改金额</a>
				</p>
				<p>
					理论返还抵现积分：<span style="color:red"><#if (teackMap.step1.logicGiveAccountPoint)??>${teackMap.step1.logicGiveAccountPoint}</#if></span>&nbsp;&nbsp;&nbsp;
					实际返还抵现积分：<span style="color:red"><#if (teackMap.step1.returnAccountPoint)??>${teackMap.step1.returnAccountPoint}</#if></span><br>
					理论扣除购物返积分：<span style="color:red"><#if (teackMap.step1.logicRemoveAccountPoint)??>${teackMap.step1.logicRemoveAccountPoint}</#if></span>&nbsp;&nbsp;&nbsp;
					实际扣除购物返积分：<span style="color:red"><#if (teackMap.step1.removeAccountPoint)??>${teackMap.step1.removeAccountPoint}</#if></span>
				</p>
				<p><span>选择售后问题原因：<#if refund.reason?exists>${refund.reason}</#if></span>
				</p>
				<p>
					<span>客服处理备注（${teackMap.step1.dealManager}）：<#if teackMap.step1.remark?exists>${teackMap.step1.remark}</#if></span><br>
				</p>
			</div>
		<#else>
			<div style="padding:10px">
				<p><span style="color:red">【处理1】客服处理退款申请</span><br>
				--------------------------------------------------------------------------------------------------------------------------------------</p>
				<p>
					申请退款金额：<span style="color:red"><#if refund.refundMoney?exists>${refund.refundMoney}</#if>&nbsp;&nbsp;&nbsp;</span>
					<span>修改退款金额为：</span><input class="easyui-textbox" name="money" style="width:100" value="<#if (refund.orderPrductRefundInfo.logicApplyPrice)??>${refund.orderPrductRefundInfo.logicApplyPrice}</#if>">
					<span>&nbsp;注：与买家协商一致后方可修改！</span>
				</p>
				<p>
					理论返还抵现积分：<span style="color:red"><#if (refund.orderPrductRefundInfo.logicGiveAccountPoint)??>${refund.orderPrductRefundInfo.logicGiveAccountPoint}</#if>&nbsp;&nbsp;&nbsp;</span>
					<span>返还积分：</span><input class="easyui-textbox" id="logicGiveAccountPoint" style="width:100" value="<#if (refund.orderPrductRefundInfo.logicGiveAccountPoint)??>${refund.orderPrductRefundInfo.logicGiveAccountPoint}</#if>">
					<input type="hidden" id="logicGiveAccountPoint_status" value="1" />
					<span>&nbsp;<a id="logicGiveAccountPoint_a" href="javascript:;" class="easyui-linkbutton" style="width:80px">立即返还</a></span>
				</p>
				<p>
					理论应扣除积分&nbsp;：<span style="color:red"><#if (refund.orderPrductRefundInfo.logicRemoveAccountPoint)??>${refund.orderPrductRefundInfo.logicRemoveAccountPoint}</#if>&nbsp;&nbsp;&nbsp;</span>
					<span>扣除积分：</span><input class="easyui-textbox" id="logicRemoveAccountPoint" style="width:100" value="<#if (refund.orderPrductRefundInfo.logicRemoveAccountPoint)??>${refund.orderPrductRefundInfo.logicRemoveAccountPoint}</#if>">
					<input type="hidden" id="logicRemoveAccountPoint_status" value="1" />
					<span>&nbsp;<a id="logicRemoveAccountPoint_a" href="javascript:;" class="easyui-linkbutton" style="width:80px">立即扣除</a></span>
				</p>
				
				<#if refund.orderStatusCode == 2>
					<p><input type="checkbox" id="sendGoods" checked value="1" /> 该订单需要继续发货</p>
					<div id="sendGoodsDiv" style="display:block">
						<table width="60%" border="1" cellpadding="10px" cellspacing="0px">
							<tr>
								<th width="45%">商品名称</th>
								<th width="10%">购买数量</th>
								<th width="15%">退货状态</th>
								<th width="10%">退货数量</th>
								<th width="10%">发货数量</th>
								<th width="10%">勾选发货</th>
							</tr>
							<#list refund.orderProductList as r>
								<tr>
									<td width="45%">${r.name}</td>
									<td width="10%">${r.productCount}</td>
									<th width="15%"><sapn >${r.refundStatus}</sapn></th>
									<td width="10%">${r.refundCount}</td>
									<td width="10%">
										<input type="hidden" name="realSendGoodsCountHide_${r.id?c}" value="${r.productCount - r.refundCount}" />
										<input type="hidden" name="maxCount_${r.id?c}" value="${r.productCount}" />
										<input id="realSendGoodsCount_${r.id?c}" onchange="checkSendGoodsCount(this)" value="${r.productCount - r.refundCount}" style="width:30px" />
										<input type="hidden" name="countCid" value="${r.id?c}" />
									</td>
									<td width="10%">
										<input type="hidden" name="cid" value="${r.id?c}" />
										<input type="checkbox" onchange="sendGoodsCurrOrderProduct(this)" id="sendGoodsCurrOrderProduct_${r.id?c}" value="1" <#if (r.productCount - r.refundCount) == 0><#else>checked</#if> />
									</td>
								</tr>
							</#list>
						</table>
					</div>
				</#if>
				
				<p><span>选择售后问题原因：</span><input type="text" id="refundReasonId" name="refundReasonId" style="width: 200px;"/>
				</p>
				
				<p>
					<span>客服处理备注</span><br/><br/>
					<textarea id="remark1" onkeydown="checkEnter(event)" style="height: 60px;width: 350px"></textarea><br/>
				</p>
				<p><input type="checkbox" id="modifyRefundType" value="1" /> 修改退款类型为：退款并退货</p>
				<p><input type="checkbox" id="cancelOrder" value="1" /> 同时取消订单</p>
				<a href="javascript:;" onclick="agreeRefund(1)" class="easyui-linkbutton" style="width:100px">同意退款申请</a>&nbsp;
				<a href="javascript:;" onclick="closeRefund(1)" class="easyui-linkbutton" style="width:120px">经协商，关闭退款</a>&nbsp;
				<a href="javascript:;" onclick="cancelRefund(1)" class="easyui-linkbutton" style="width:100px">重置，取消退款</a>
			</div>
		</#if>
	<!-- 处理一    end -->
	
	<!-- 处理二    begin -->
		<#if teackMap.step2?exists>
			<div style="padding:10px">
				<p><span style="color:red">【处理2】财务打款处理</span>&nbsp;&nbsp;&nbsp;
				<a onclick="financeShare()" href="javascript:;" class="easyui-linkbutton" style="width:130px">财务分摊</a><br>
				--------------------------------------------------------------------------------------------------------------------------------------</p>
				<p>
					<span style="color:red">【退款账号】</span><sapn><#if refund.bankTypeDesc?exists>${refund.bankTypeDesc}</#if>&nbsp;&nbsp;&nbsp;</sapn>
					
				<#if refund.refundPayType?exists && refund.refundPayType == "1">
					<span>姓名：<#if refund.cardName?exists>${refund.cardName}</#if>&nbsp;&nbsp;&nbsp;</span>
					<span>开户行：<#if refund.bankName?exists>${refund.bankName}</#if>&nbsp;&nbsp;&nbsp;</span>
					<span>卡号：<#if refund.cardNumber?exists>${refund.cardNumber}</#if></span>
				</#if>
				</p>
				<p>
					<span>打款账户选择：<#if teackMap.step2.content?exists>${teackMap.step2.content}</#if></span><br>
				</p>
				<p>
					<span>财务打款备注（${teackMap.step2.dealManager}）：<#if teackMap.step2.remark?exists>${teackMap.step2.remark}</#if></span><br>
				</p>
			</div>
		<#elseif refund.status==3>
			<div style="padding:10px">
				<p><span style="color:red">【处理2】财务打款处理</span>&nbsp;&nbsp;&nbsp;
				<a onclick="financeShare()" href="javascript:;" class="easyui-linkbutton" style="width:130px">财务分摊</a><br>
				--------------------------------------------------------------------------------------------------------------------------------------</p>
				<p>
					<span style="color:red">【退款账号】</span><sapn><#if refund.bankTypeDesc?exists>${refund.bankTypeDesc}</#if>&nbsp;&nbsp;&nbsp;</sapn>
					
				<#if refund.refundPayType?exists && refund.refundPayType == "1">
					<span>姓名：<#if refund.cardName?exists>${refund.cardName}</#if>&nbsp;&nbsp;&nbsp;</span>
					<span>开户行：<#if refund.bankName?exists>${refund.bankName}</#if>&nbsp;&nbsp;&nbsp;</span>
					<span>卡号：<#if refund.cardNumber?exists>${refund.cardNumber}</#if></span>
				</#if>
				<#if refund.returnPayUrl?exists && refund.returnPayUrl!="">
					<a href="${refund.returnPayUrl}">前往原支付账户退款(${(refund.returnPayAccount)!''})</a>
				<#elseif refund.returnPayMark?exists && refund.returnPayMark!="">
					${refund.returnPayMark}
				</#if>
				</p>
				<p>
					<span>打款账户选择：</span>
					<select name="cardId" id="cardId">
		 				<#list  financialAffairsCards as bl >
		 					<option value="${bl.id?c}" <#if refund.targetFinancialAffairsCardId == bl.id >selected</#if> >${bl.name}</option>
		 				</#list>
			 		</select>
					<span id="qqhsRefund" style="display:none;">
                        <a href="javascript:;" onclick="refundImmediately();">直接打款</a>
					</span>
				</p>
				<p>
					<span>财务处理备注</span><br/><br/>
					<textarea id="remark2" onkeydown="checkEnter(event)" style="height: 60px;width: 350px"></textarea><br/>
				</p>
				<a href="javascript:;" onclick="agreeRefund(2)" class="easyui-linkbutton" style="width:100px">已打款</a>&nbsp;
				<a href="javascript:;" onclick="closeRefund(2)" class="easyui-linkbutton" style="width:120px">经协商，关闭退款</a>&nbsp;
				<a href="javascript:;" onclick="cancelRefund(2)" class="easyui-linkbutton" style="width:100px">重置，取消退款</a>
			</div>
		<#else>
		</#if>
	<!-- 处理二    end -->
	
	<!-- 修改金额dialog -->
	<div id="updateRefundPrice_div" class="easyui-dialog"  style="padding: 5px; width: 360px; height: 150px;">
	    <span>退款金额为</span>：<input id="updateRefundPrice_input"/><br/><br/>
	    <span style="color:red" >&nbsp;注：与买家协商一致后方可修改！</span>
	</div>
	
	<!-- dialog begin -->
		<div id="showErrorDialog" class="easyui-dialog" style="width:400px;height:280px;padding:20px 20px;">
            <table cellpadding="5">
                <tr>
                    <td>用户积分余额:</td>
                    <td><span id="spanUserIntegral"></span></td>
                </tr>
                <tr>
                    <td>本次合计需扣除:</td>
                    <td><span id="totalRemoveIntegral"></span></td>
                </tr>
                <tr>
                    <td>已从积分余额中扣除:</td>
                    <td><span id="alreadyRemoveIntegral" ></span></td>
                </tr>
                <tr>
                    <td><span style="color:red">剩余未扣除:</span></td>
                    <td><span style="color:red" id="tipInfo" ></span></td>
                </tr>
                <tr>
                    <td colspan="2"><span>剩余部分请自行调整退款金额</span></td>
                </tr>
            </table>
        </div>
        <!-- dialog end -->
        
        <!-- 填写财务分摊信息的div begin -->
			<div id="financeShareDiv" class="easyui-dialog" style="width:600px;height:350px;padding:20px 20px;">
				<input type="hidden" id="refundProportionId" value="${(refund.refundProportionId)!'0'}"/>
				<input type="hidden" id="realMoney" value="${(refund.realMoney)!'0'}"/>
				<input type="hidden" id="dealMoney" value=""/>
				选择类型：<select id="type">
					<option value="0" <#if refund.refundProportionType?exists && (refund.refundProportionType == 0) >selected</#if> >未发货订单取消</option>
					<option value="1" <#if refund.refundProportionType?exists && (refund.refundProportionType == 1) >selected</#if> >已发货订单退款</option>
					<option value="2" <#if refund.refundProportionType?exists && (refund.refundProportionType == 2) >selected</#if> >订单退差价</option>
					<option value="3" <#if refund.refundProportionType?exists && (refund.refundProportionType == 3) >selected</#if> >订单退运费</option>
				</select><br /><br />
            	<table width="100%" border="1" cellpadding="10px" cellspacing="0px">
					<tr>
						<th width="20%">分摊至</th>
						<th width="20%">货款</th>
						<th width="20%">运费</th>
						<th width="20%">差价</th>
						<th width="20%">合计</th>
					</tr>
					<tr>
						<td width="20%">商家</td>
						<td width="20%"><input class="proportion sellerMoney" style="width:50px" value="${(refund.sellerMoney)!''}"/></td>
						<td width="20%"><input class="proportion sellerPostageMoney" style="width:50px" value="${(refund.sellerPostageMoney)!''}"/></td>
						<td width="20%"><input class="proportion sellerDifferenceMoney" style="width:50px" value="${(refund.sellerDifferenceMoney)!''}"/></td>
						<td width="20%"><span id="ta" ></span></td>
					</tr>
					<tr>
						<td width="20%">换吧网络</td>
						<td width="20%"><input class="proportion gegejiaMoney" style="width:50px" value="${(refund.gegejiaMoney)!''}"/></td>
						<td width="20%"><input class="proportion gegejiaPostageMoney" style="width:50px" value="${(refund.gegejiaPostageMoney)!''}"/></td>
						<td width="20%"><input class="proportion gegejiaDifferenceMoney" style="width:50px" value="${(refund.gegejiaDifferenceMoney)!''}"/></td>
						<td width="20%"><span id="tb" ></span></td>
					</tr>
					<tr>
						<td width="20%">合计</td>
						<td width="20%"><span id="t1" ></span></td>
						<td width="20%"><span id="t2" ></span></td>
						<td width="20%"><span id="t3" ></span></td>
						<td width="20%"><span id="tt" ></span></td>
					</tr>
				</table>
				<#if teackMap.step1?exists>
                    <span>实际退款金额：<font size="6" color="red" ><#if teackMap.step1.content?exists>${teackMap.step1.content}</#if></font></span>
				</#if>
        	</div>
        <!-- 填写财务分摊信息的div end -->
	
	</div>
<script>

//textarea 不允许按enter键
function checkEnter(e){
	var et=e||window.event;
	var keycode=et.charCode||et.keyCode;
	if(keycode==13){
		if(window.event)
			window.event.returnValue = false;
		else
			e.preventDefault();//for firefox
	}
}

//检查客服修改的商品发货数量
function checkSendGoodsCount(obj){
	var defaultCount = $(obj).prev().prev().val();
	var maxCount = $(obj).prev().val();
	var realSendGoodsCount = $(obj).val();
	var countCid = $(obj).next().val();
	if(!/^\d*$/.test(realSendGoodsCount)){
		$.messager.alert("提示",'请输入正整数',"info");
		$(obj).val(defaultCount);
		$("#sendGoodsCurrOrderProduct_"+countCid).prop("checked",false);
	}else if(parseInt(realSendGoodsCount) > maxCount){
		$.messager.alert("提示",'发货数量不得大于购买数量',"info");
		$(obj).val(defaultCount);
		if(defaultCount == '0'){
			$("#sendGoodsCurrOrderProduct_"+countCid).prop("checked",false);
		}else{
			$("#sendGoodsCurrOrderProduct_"+countCid).prop("checked",true);
		}
	}else if(realSendGoodsCount == '0'){
		$("#sendGoodsCurrOrderProduct_"+countCid).prop("checked",false);
	}else if(realSendGoodsCount != '0'){
		$("#sendGoodsCurrOrderProduct_"+countCid).prop("checked",true);
	}else{
		alert("ok");
	}
}

//退款商品是否可以发货处理
function sendGoodsCurrOrderProduct(obj){
	var cid = $(obj).prev().val();
	var realSendGoodsCount = $("#realSendGoodsCount_"+cid).val();
	if(parseInt(realSendGoodsCount) <= 0){
		$.messager.alert("info","发货数量大于0时才可以勾选发货操作。","warning");
		$(obj).attr('checked',false);
	}
}

//同意退款申请  &  打款操作
function agreeRefund(step){
	var id = $('#refundId').val();
	var remark = $('#remark'+step).val();
	var money = $("input[name='money']").val();
	var cardInfo = "";
	
	var sendGoods = $("#sendGoods").is(':checked');
	var realSendGoodsCountStr = '';
	if(step == 1){
		//step=1  是否继续发货
		if(sendGoods){
			//找到所有cid
			$("input[name='cid']").each(function(i,obj){
			    var currCid = $(obj).val();
			    var currRealSendGoodsCount = "0";
			    if($(obj).next().is(':checked')){
			    	var currRealSendGoodsCount = $("#realSendGoodsCount_"+currCid).val();
			    }
			    var cstr = currCid + "," + currRealSendGoodsCount + ";";
			    realSendGoodsCountStr += cstr;
			});
		}
	}
	//step=1  修改退款退货类型	
	var modifyRefundType = $("#modifyRefundType").is(':checked');
	var cancelOrder = $("#cancelOrder").is(':checked');
	
	var reg = /^[1234567890.]*$/;
	if(reg.test(money) || (step == 2)){
		$.messager.confirm('tip','请确认？',function(b){
			if(b){
				var tempData;
				if(step == 1) {
					tempData = {'id':id,'remark':remark,'step':step,'money':money,'cardInfo':cardInfo,'type':1,'modifyRefundType':modifyRefundType,'cancelOrder':cancelOrder,'sendGoods':sendGoods,'realSendGoodsCount':realSendGoodsCountStr,'refundReasonId':$('#refundReasonId').combobox('getValue')};
				} else {
					tempData = {'id':id,'remark':remark,'step':step,'money':money,'cardInfo':cardInfo,'type':1,'modifyRefundType':modifyRefundType,'cancelOrder':cancelOrder,'sendGoods':sendGoods,'realSendGoodsCount':realSendGoodsCountStr};
				}
				$.ajax({
				    url: '${rc.contextPath}/refund/agreeRefund',
				    type: 'post',
				    dataType: 'json',
				    data: tempData,
				    success: function(data){
				        if(data.status == 1){
				        	//刷新当前页面
				        	if(step == 2){
				        		window.location.href = window.location.href;
				        	}else{
				        		window.location.href = '${rc.contextPath}/refund/listOnlyReturnMoney';
				        	}
				        }else{
				        	$.messager.alert("提示",data.msg,"info");
				        }
				    },
				    error: function(xhr){
				    	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
				    }
				});
			}
		});
	}else{
		$.messager.alert("确认信息","非法金额","wrong");
	}
}

//经协商，关闭退款
function closeRefund(step){
	var id = $('#refundId').val();
	var remark = $('#remark'+step).val();
	$.messager.confirm('tip','是否确定关闭退款？',function(b){
		if(b){
			$.ajax({
			    url: '${rc.contextPath}/refund/closeRefund',
			    type: 'post',
			    dataType: 'json',
			    data: {'id':id,'remark':remark,'step':step,type:1},
			    success: function(data){
			        if(data.status == 1){
			        	//刷新当前页面
			        	window.location.href = window.location.href;
			        }else{
			        	$.messager.alert("提示",data.msg,"info");
			        }
			    },
			    error: function(xhr){
			    	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
			    }
			});
		}
	});
}

//重置，取消退款
function cancelRefund(step){
	var id = $('#refundId').val();
	var remark = $('#remark'+step).val();
	$.messager.confirm('tip','是否确定取消退款？',function(b){
		if(b){
			$.ajax({
			    url: '${rc.contextPath}/refund/cancelRefund',
			    type: 'post',
			    dataType: 'json',
			    data: {'id':id,'remark':remark,'step':step,type:1},
			    success: function(data){
			        if(data.status == 1){
			        	//刷新当前页面
			        	window.location.href = window.location.href;
			        }else{
			        	$.messager.alert("提示",data.msg,"info");
			        }
			    },
			    error: function(xhr){
			    	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
			    }
			});
		}
	});
}

//立即打款
function refundImmediately(){
    var id = $("#refundId").val();
    var cardId = $("#cardId").val();
    $.messager.confirm('提示','确定立即打款吗？',function(b){
        if(b){
            $.ajax({
                url:'${rc.contextPath}/refund/dealRefundImmediately',
                type:'post',
                dataType:'json',
                data:{'id':id,'cardId':cardId},
                beforeSend:function(xhr){
                    $.messager.progress();
                },
                success:function(data){
                    if(data.status == 1){
						$.messager.alert('提示','退款成功',"info");
                    }else{
                        $.messager.alert("提示",data.message,"info");
                    }
                },
                complete:function(xhr,ts){
                    $.messager.progress('close');
                },
                error:function(xhr){
                    $.messager.alert('提示','服务器忙，请稍后再试用。（'+xhr.status+')','info');
                }
            });
        }
    });
}

function showImage(src){
	window.open(src,'_blank');
}

function updateRefundPrice(){
	$('#updateRefundPrice_input').val("");
	$('#updateRefundPrice_div').dialog("open");
}

function financeShare(){
	$('#financeShareDiv').dialog("open");
}

function resetData(data){
	if(!/^(-?\d+)(\.\d+)?$/.test(data)){
		return 0;
	}
	return parseFloat(data);
}

$(function(){
	
	$.ajax({ 
	       url: '${rc.contextPath}/refundReason/findList?isAvailable=1',
	       type: 'post',
	       dataType: 'json',
	       success: function(data){
        	   $('#refundReasonId').combobox({
        	        data:data.rows,
        	        valueField:'id',
        	        textField:'reason'
        		});
	       }
	});

    $("#cardId").change(function(){
        var cardId = $(this).val();
        if(cardId == '9' || cardId == '11' || cardId == '18' || cardId == '14'){
            $("#qqhsRefund").show();
        }else{
            $("#qqhsRefund").hide();
        }
    });
    $("#cardId").change();

	$(".proportion").change(function(){
		var dd = $(this).val();
		if(dd == ''){
			return;
		}
		if(!/^(-?\d+)(\.\d+)?$/.test(dd)){
			//$.messager.alert("info","您输入了非法字符！","error");
			 $(this).val("");
			return;
		}
        var realMoney = parseFloat($("#realMoney").val());
		var sellerMoney = resetData($(".sellerMoney").val());
		var sellerPostageMoney = resetData($(".sellerPostageMoney").val());
		var sellerDifferenceMoney = resetData($(".sellerDifferenceMoney").val());
		var gegejiaPostageMoney = resetData($(".gegejiaPostageMoney").val());
		var gegejiaDifferenceMoney = resetData($(".gegejiaDifferenceMoney").val());
		var gegejiaMoney = resetData($(".gegejiaMoney").val());
		if (!$(this).hasClass("gegejiaMoney"))
		{
            gegejiaMoney = realMoney - sellerMoney - sellerPostageMoney - sellerDifferenceMoney - gegejiaPostageMoney - gegejiaDifferenceMoney;
            $(".gegejiaMoney").val(gegejiaMoney)
		}
		var ta = sellerMoney+sellerPostageMoney+sellerDifferenceMoney;
		var tb = gegejiaMoney+gegejiaPostageMoney+gegejiaDifferenceMoney;
		var t1 = sellerMoney+gegejiaMoney;
		var t2 = sellerPostageMoney+gegejiaPostageMoney;
		var t3 = sellerDifferenceMoney+gegejiaDifferenceMoney;
		var tt = ta+tb;
		$("#ta").text(ta.toFixed(3));
		$("#tb").text(tb.toFixed(3));
		$("#t1").text(t1.toFixed(3));
		$("#t2").text(t2.toFixed(3));
		$("#t3").text(t3.toFixed(3));
		$("#tt").text(tt.toFixed(3));
		$("#dealMoney").val(tt.toFixed(3));
	});
	$(".sellerMoney").change();
	
	$('#financeShareDiv').dialog({
        title:'退款分摊',
        collapsible:true,
        closed:true,
        modal:true,
        left:720,
        top:250,
        buttons:[{
            text:'保存',
            iconCls:'icon-ok',
            handler:function(){
            	var dealMoney = parseFloat($("#dealMoney").val());
            	var realMoney = parseFloat($("#realMoney").val());
            	if(dealMoney > realMoney){
            		$.messager.alert("info","计算结果大于实际退款金额","error");
            	}
            	else if(dealMoney < realMoney){
            		$.messager.alert("info","计算结果小于实际退款金额","error");
            	}
            	else{
            		var params = {};
                	params.refundId = $('#refundId').val();
                	params.type=$("#type").val();
                	params.refundProportionId = $('#refundProportionId').val();
                	params.sellerMoney = resetData($(".sellerMoney").val());
                	params.sellerPostageMoney = resetData($(".sellerPostageMoney").val());
                	params.sellerDifferenceMoney = resetData($(".sellerDifferenceMoney").val());
                	params.gegejiaMoney = resetData($(".gegejiaMoney").val());
                	params.gegejiaPostageMoney = resetData($(".gegejiaPostageMoney").val());
                	params.gegejiaDifferenceMoney = resetData($(".gegejiaDifferenceMoney").val());
                	$.messager.progress();
                	$.ajax({ 
                	       url: '${rc.contextPath}/refund/saveFinanceShare',
                	       type: 'post',
                	       dataType: 'json',
                	       data: params,
                	       success: function(data){
                	         $.messager.progress('close');
                	           if(data.status == 1){
                	        	   $.messager.alert("提示","保存成功","info",function(){
                	        		   $('#financeShareDiv').dialog('close');
                	        		   $('#refundProportionId').val(data.id);
                	        	   });
                	           }else{
                	             	$.messager.alert("提示",data.msg,"info");
                	           }
                	       },
                	       error: function(xhr){
                	         $.messager.progress('close');
                	         $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                	       }
                	});
            	}
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#financeShareDiv').dialog('close');
            }
        }]
    });
	
	//订单是否需要继续发货
	$("#sendGoods").change(function(){
		if($("#sendGoods").is(':checked')){
			$("#sendGoodsDiv").show();			
		}else{
			$("#sendGoodsDiv").hide();
		}
	});
	
	$('#updateRefundPrice_div').dialog({
        title:'修改退款金额',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存信息',
            iconCls:'icon-ok',
            handler:function(){
            	$.messager.progress();
            	var id = $('#refundId').val();
            	var newPrice = $('#updateRefundPrice_input').val();
            	$.ajax({ 
            	       url: '${rc.contextPath}/refund/updateRefundPrice',
            	       type: 'post',
            	       dataType: 'json',
            	       data: {'id':id,'newPrice':newPrice},
            	       success: function(data){
            	         $.messager.progress('close');
            	           if(data.status == 1){
            	        	   $.messager.alert("提示",data.msg,"info",function(){
            	        		   window.location.href = window.location.href;
                	        	   $('#updateRefundPrice_div').dialog("close");
            	        	   });
            	           }else{
            	            	$.messager.alert("提示",data.msg,"info");
            	           }
            	       },
            	       error: function(xhr){
            	        	$.messager.progress('close');
            	        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
            	       }
            	   });
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
            	$('#updateRefundPrice_div').dialog("close");
            }
        }]
    });
	
	//立即返还
	$("#logicGiveAccountPoint_a").click(function(){
		var id = $('#refundId').val();
		var logicGiveAccountPoint_status = $("#logicGiveAccountPoint_status").val();
		if(logicGiveAccountPoint_status == 1){
			var accountId = $("#accountId").val();
			var addIntegral = $("#logicGiveAccountPoint").val();
			var maxIntegral = 2100000000;
	    	if(!/^[1-9]+\d*$/.test(addIntegral)){
	    		$.messager.alert("提示","积分必须是不以零开头的正整数","info");
	    	}
	    	else if(Math.abs(parseInt(addIntegral)) > maxIntegral){
	    		$.messager.alert("提示","不允许的积分操作","info");
	    	}else{
	    		$.messager.progress({text:"正在返还..."});
	    		$.ajax({
	    			url: '${rc.contextPath}/account/updateIntegral',
	    			type: 'post',
	    			dataType: 'json',
	    			data: {'id':accountId,'addIntegral':addIntegral,source:'dealrefund_give'+id},
	    			success: function(data){
	    				$.messager.progress('close');
	    				if(data.status == 1){
	    					$("#logicGiveAccountPoint_status").val("0");
	    					$.messager.alert("提示","返还成功","info");
	    					//更新退款退货 实际返还积分
	    					$.ajax({
	    						url: '${rc.contextPath}/refund/updateRefund',
	    						type: 'post',
	    		    			dataType: 'json',
	    		    			data: {'id':id,'returnAccountPoint':addIntegral},
	    		    			success: function(data){
	    		    				//TODO
	    		    			}
	    					});
	    				}else{
	    					$.messager.alert("提示",data.msg,"error");
	    				}
	    			},
	    			error: function(xhr){
	    				$.messager.progress('close');
	    				$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	    			}
	    		});
	    	}
		}else{
			$.messager.alert("提示","您已经返还过了","error");
		}
	});
	
	//立即扣除
	$("#logicRemoveAccountPoint_a").click(function(){
		var id = $('#refundId').val();
		var logicRemoveAccountPoint_status = $("#logicRemoveAccountPoint_status").val();
		if(logicRemoveAccountPoint_status == 1){
			var accountId = $("#accountId").val();
			var addIntegral = $("#logicRemoveAccountPoint").val();
			var maxIntegral = 2100000000;
	    	if(!/^[1-9]+\d*$/.test(addIntegral)){
	    		$.messager.alert("提示","积分必须是不以零开头的正整数","info");
	    	}
	    	else if(Math.abs(parseInt(addIntegral)) > maxIntegral){
	    		$.messager.alert("提示","不允许的积分操作","info");
	    	}else{
	    		$.messager.progress({text:"正在扣除..."});
	    		$.ajax({
	    			url: '${rc.contextPath}/account/updateIntegral',
	    			type: 'post',
	    			dataType: 'json',
	    			data: {'id':accountId,'addIntegral':-addIntegral,'isForcibly':1,source:'dealrefund_remove'+id},
	    			success: function(data){
	    				$.messager.progress('close');
	    				if(data.status == 1){
	    					if(data.forciblyUpdate == 1){
	    						$('#spanUserIntegral').text(data.remainPoint);
	    						$('#totalRemoveIntegral').text(addIntegral);
	    						$('#alreadyRemoveIntegral').text(data.remainPoint);
	    						var stillText = addIntegral - data.remainPoint +"(折合￥"+data.disIntegral+")"
	    						$('#tipInfo').text(stillText);
	    						$('#showErrorDialog').dialog('open');
	    						//更新退款退货 实际返还积分
		    					$.ajax({
		    						url: '${rc.contextPath}/refund/updateRefund',
		    						type: 'post',
		    		    			dataType: 'json',
		    		    			data: {'id':id,'removeAccountPoint':addIntegral},
		    		    			success: function(data){
		    		    				//TODO
		    		    			}
		    					});
	    					}else{
	    						$("#logicRemoveAccountPoint_status").val("0");
	    						//更新退款退货 实际返还积分
		    					$.ajax({
		    						url: '${rc.contextPath}/refund/updateRefund',
		    						type: 'post',
		    		    			dataType: 'json',
		    		    			data: {'id':id,'removeAccountPoint':addIntegral},
		    		    			success: function(data){
		    		    				//TODO
		    		    			}
		    					});
		    					$.messager.alert("提示","扣除成功","info");
	    					}
	    				}else{
	    					$.messager.alert("提示",data.msg,"error");
	    				}
	    			},
	    			error: function(xhr){
	    				$.messager.progress('close');
	    				$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	    			}
	    		});
	    	}
		}else{
			$.messager.alert("提示","您已经扣除过了","error");
		}
	});
	
	//integral dialog 
	$('#showErrorDialog').dialog({
		title:'积分扣除结果',
		collapsible:true,
		closed:true,
		modal:true,
		buttons:[{
		    text:'确定',
		    iconCls:'icon-ok',
		    align:'left',
		    handler:function(){
		        $('#showErrorDialog').dialog('close');
		    }
		}]
     });
	
});

</script>

</body>
</html>