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

<style type="text/css">
	#myCss span {
		font-size:13px;
		padding-right:20px;
	}
	
	textarea{
		resize:none;
	}

    ul a:link{
        text-decoration:none;
    }
    ul a:hover {
        color：black；
    }
    ul a:visited {
        color:black;
    }
    ul a:link {
        color:black;
    }
    ul a {
        color:black;
    }
</style>

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'菜单列表',split:true" style="width:180px;">
	<#include "../sellerAdmin/menu.ftl" >
</div>

<div data-options="region:'center',title:'订单详情'" style="padding:5px;">
	<div id="myCss">
		<div style="padding:10px;">
			<span>订单退款状态：
				<#if detail.freezeCode == 0>
					未退款
				<#elseif detail.freezeCode == 1 >
					退款中
				<#else>
					退款成功
				</#if>
			</span><br><br>
			<span style="padding-right:30px;">订单信息</span> 
			<span>订单编号：<#if detail.number?exists>${detail.number}</#if></span> 
			<span>订单状态：<#if detail.status?exists>${detail.status}</#if></span>
			<br/>
			<hr/>
			<span>下单时间：<#if detail.createTime?exists>${detail.createTime}</#if></span>
			<span>付款时间：<#if detail.payTime?exists>${detail.payTime}</#if></span>
			<span>发货时间：<#if detail.sendTime?exists>${detail.sendTime}</#if></span>
			<span>收货时间：<#if detail.receiveTime?exists>${detail.receiveTime}</#if></span>
			<br/><br/>
			<span>收货人姓名：<#if detail.receiveName?exists>${detail.receiveName}</#if></span>
			<span>收货人手机号：<#if detail.receiveMobile?exists>${detail.receiveMobile}</#if></span>
			<span>收货人身份证号：<#if detail.receiveIdCart?exists>${detail.receiveIdCart}</#if></span>
			<br/><br/>
			<span>详细地址：<#if detail.address?exists>${detail.address}</#if></span>
			<#if detail.statusCode == 2>
				<a id="searchBtn" onclick="sendOrder()" href="javascript:;" class="easyui-linkbutton"> &nbsp;发货</a>
			</#if>
			<br/><br/>
			<span>商家：<#if detail.sellerName?exists>${detail.sellerName}</#if></span>
			<span>发货方式：<#if detail.sellerType?exists>${detail.sellerType}</#if></span>
			<span>发货地：<#if detail.sendAddress?exists>${detail.sendAddress}</#if></span>
            <br/><br/>
            <span><#if detail.modifyReceiveName?exists>${detail.modifyReceiveName}</#if></span>
		</div>
		<br/><br/>
		<#--<div style="padding:10px;">-->
			<#--<span style="padding-right:30px;">付款信息</span> -->
			<#--<span style="color:red">订单总价：<#if detail.totalPrice?exists>${detail.totalPrice}</#if></span>-->
			<#--<span style="color:red">实付金额：<#if detail.realPrice?exists>${detail.realPrice}</#if></span>-->
			<#--<br/>-->
			<#--<hr/>-->
			<#--<span>商品总价：<#if detail.totalProductPrice?exists>${detail.totalProductPrice}</#if></span>-->
			<#--<span>邮费：<#if detail.freightMoney?exists>${detail.freightMoney}</#if></span>-->
			<#--<span>优惠券金额：<#if detail.couponPrice?exists>${detail.couponPrice}</#if></span>-->
			<#--<span>积分抵用：<#if detail.accountPoint?exists>${detail.accountPoint}</#if></span>-->
			<#--<span>客服改价：<#if detail.adjustPrice?exists>${detail.adjustPrice}</#if></span>-->
			<#--<span>满减活动：<#if detail.activitiesPrice?exists>${detail.activitiesPrice}</#if></span><br/><br/>-->
			<#--<span>其他活动：<#if detail.otherActivity?exists>${detail.otherActivity}</#if></span><br/><br/>-->
			<#--<span>付款方式：<#if detail.payChannel?exists>${detail.payChannel}</#if></span>-->
            <#--&nbsp;&nbsp;&nbsp;&nbsp;-->
            <#--<span >交易号：<#if detail.payTid?exists>${detail.payTid}</#if></span><br/><br/>-->
            <#--<span >平台微信交易号：<#if detail.weixinMark?exists>${detail.weixinMark}</#if></span><br/><br/>-->
            <#--<span >平台支付宝交易号：<#if detail.zhifubaoMark?exists>${detail.zhifubaoMark}</#if></span><br/><br/>-->
            <#--<span >平台银联交易号：<#if detail.yinlianMark?exists>${detail.yinlianMark}</#if></span><br/><br/>-->
			<#--<span>商家备注：<span id="sellerRemark">${detail.remark!""}</span></span><br/><br/>-->
			<#--<#if detail.remark2?exists && (detail.remark2 !="")>-->
			<#--<span>客服备注：${detail.remark2!""}</span><br/><br/>-->
			<#--</#if>-->
		<#--</div>-->
		<#--<br/><br/>-->
		<div style="padding:10px;">
			<span style="padding-right:30px;">物流信息</span>
			<#if detail.logisticsChannel?exists>
				<span>${detail.logisticsChannel}：${detail.logisticsNumber}</span>
			</#if>
			<br/><br/>
			<hr/>
			<#list  detail.logisticsList as bl >
				<span>${bl.operateTime}</span><span>${bl.content}</span><br/><br/>
			</#list>
			<#if detail.hasOrderLogistics?exists>
				<a href="${detail.logisticsUrl}" target="_blank" class="easyui-linkbutton" style="width:200px">查询物流详细信息</a>
			</#if>
		</div>
	</div>
	<!-- 商品列表 -->
	<div>
		<br/><br/>
		<span style="padding-right:30px;">商品明细</span> 
		<#--<span style="color:red">商品总价：<#if detail.totalProductPrice?exists>${detail.totalProductPrice}</#if></span> -->
		<br/>
		<hr/>
		<table id="s_data">
		</table>		
	</div>

    <!-- 填写发货所需信息的div begin -->
    <div id="sendOrderDiv" class="easyui-dialog" style="width:350px;height:240px;padding:20px 20px;">
        <form id="sendOrderForm" method="post">
            <input id="orderId" type="hidden" name="orderId" value="${id?c!'0'}" />
            <input id="sellerId" name="sellerId" type="hidden" value="${sellerId?c!'0'}" />
            <input type="radio" id="sendType1" name="sendType" value="1" checked /> 有物流
		<#--<input type="radio" id="sendType0" name="sendType" value="0"/> 无物流-->
            <div id="haveChannel">
                <table cellpadding="5">
                    <tr>
                        <td>物流渠道:</td>
                        <td><input id="channel" type="text" name="channel" ></input></td>
                    </tr>
                    <tr>
                        <td>物流单号:</td>
                        <td><input id="number" type="text" name="number" ></input></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input id="money" type="hidden" name="money" value="0" ></input></td>
                    </tr>
                </table>
            </div>
        </form>
    </div>

</div>

<script>

    function sendOrder() {
        $("#sendType1").prop("checked", true);
        $("#sendType1").change();
        $('#channel').combobox('clear');
        $('#money').val("");
        $('#number').val("");
        $('#sendOrderDiv').dialog('open');
    }

	$(function(){
		$('#s_data').datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'pid',
			url : '${rc.contextPath}/order/orderProductInfo',
			loadMsg : '正在装载数据...',
			fitColumns : true,
			queryParams : {
				id : ${detail.id?c}
			},
			remoteSort : true,
			singleSelect : true,
			columns : [ [
					{field : 'pid', title : '商品ID', width : 30, align : 'center'},
					{field : 'pCode', title : '商品编码', width : 50, align : 'center'},
					{field : 'pName', title : '商品名称', width : 60, align : 'center'},
                	{field : 'refundStatus', title : '退款情况', width : 30, align : 'center'},
                	{field : 'refundDetailStatus', title : '退款状态', width : 40, align : 'center'},
					{field : 'count', title : '购买数量', width : 40, align : 'center'}
					] ],
			rownumbers : true
			});

        $('#sendOrderDiv').dialog({
            title:'物流信息',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'发货',
                iconCls:'icon-ok',
                handler:function(){
                    $('#sendOrderForm').form('submit',{
                        url:"${rc.contextPath}/sellerAdminOrder/sendOrder",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#sendOrderDiv').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                    window.location.href = window.location.href;
                                });
                            } else if(res.status == 2){
                                $.messager.alert('响应信息',"该订单已经发货",'info');
                            }else if(res.status == 3){
                                $.messager.alert('响应信息',res.msg,'info');
                            }
                            else{
                                $.messager.alert('响应信息',"发货失败",'error');
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#sendOrderDiv').dialog('close');
                }
            }]
        });

        $('#channel').combobox({
			width:173,
            url:'${rc.contextPath}/sellerAdminOrder/jsonCompanyCode',
            valueField:'code',
            textField:'text'
        });

    })
</script>

</body>
</html>