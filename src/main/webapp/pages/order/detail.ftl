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
		font-size:12px;
		padding-right:20px;
	}
	
	textarea{
		resize:none;
	}
</style>

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div id="myCss">
		<a href="${rc.contextPath}/order/list" class="easyui-linkbutton" style="width:200px">返回订单列表</a>
		<div style="padding:10px;">
			<span>订单退款冻结状态：
				<#if detail.freezeCode == 0>
					未冻结
				<#elseif detail.freezeCode == 1 >
					该订单有商品正在申请退款未被处理，<span style="color:red">所以已冻结，</span>冻结状态的订单无法被导出发货表
				<#else>
					该订单已经被永久冻结
				</#if>
			</span><br><br>
			<#if detail.operationStatus?exists>
            	<span style="color:red">
            		${detail.operationStatus}
				</span><br><br>
			</#if>
			<span style="padding-right:30px;">订单信息</span> 
			<span>订单编号：<#if detail.number?exists>${detail.number}</#if></span> 
			<span>订单状态：<#if detail.status?exists>${detail.status}</#if></span>
			<span>订单类型：<#if detail.orderType?exists>${detail.orderType}</#if></span>
			<br/>
			<span>同一批次订单编号：<#if detail.orderNumberJsonArray?exists> <#list detail.orderNumberJsonArray as item><a href="${item.id}" target="_blank">${item.number}</a>&nbsp;&nbsp;&nbsp;</#list></#if></span> 
			<hr/>
			<span>下单时间：<#if detail.createTime?exists>${detail.createTime}</#if></span>
			<span>付款时间：<#if detail.payTime?exists>${detail.payTime}</#if></span>
			<span>发货时间：<#if detail.sendTime?exists>${detail.sendTime}</#if></span>
			<span>收货时间：<#if detail.receiveTime?exists>${detail.receiveTime}</#if></span>
			<br/><br/>
			<span>登陆方式：<#if detail.accountType?exists>${detail.accountType}</#if></span>
			<span>用户Id：<#if detail.accountId?exists>${detail.accountId?c}</#if></span>
			<span>用户名：<#if detail.accountName?exists>${detail.accountName}</#if></span>
			<span>收货人姓名：<#if detail.receiveName?exists>${detail.receiveName}</#if></span>
			<span>收货人手机号：<#if detail.receiveMobile?exists>${detail.receiveMobile}</#if></span>
			<span>收货人身份证号：<#if detail.receiveIdCart?exists>${detail.receiveIdCart}</#if></span>
			<br/><br/>
			<span>详细地址：<#if detail.address?exists>${detail.address}</#if></span>
			<a id="setReceiveAddress" onclick="editReceiveAddress()" href="javascript:;" class="easyui-linkbutton" style="width:130px">修改收货地址</a>
			<br/><br/>
			<span>商家：<#if detail.sellerName?exists>${detail.sellerName}</#if></span>
			<span>发货方式：<#if detail.sellerType?exists>${detail.sellerType}</#if></span>
			<span>发货地：<#if detail.sendAddress?exists>${detail.sendAddress}</#if></span>
            <br/><br/>
            <span><#if detail.modifyReceiveName?exists>${detail.modifyReceiveName}</#if></span>
		</div>
		<br/><br/>
		<div style="padding:10px;">
			<span style="padding-right:30px;">付款信息</span> 
			<span style="color:red">订单总价：<#if detail.totalPrice?exists>${detail.totalPrice}</#if></span>
			<span style="color:red">实付金额：<#if detail.realPrice?exists>${detail.realPrice}</#if></span>
			<br/>
			<hr/>
			<span>商品总价：<#if detail.totalProductPrice?exists>${detail.totalProductPrice}</#if></span>
			<span>邮费：<#if detail.freightMoney?exists>${detail.freightMoney}</#if></span>
			<span>优惠券金额：<#if detail.couponPrice?exists>${detail.couponPrice}</#if></span>
			<span>积分抵用：<#if detail.accountPoint?exists>${detail.accountPoint}</#if></span>
			<span>客服改价：<#if detail.adjustPrice?exists>${detail.adjustPrice}</#if></span>
			<span>满减活动：<#if detail.activitiesPrice?exists>${detail.activitiesPrice}</#if></span>
			<span>N元任选活动：<#if detail.activitiesOptionalPartPrice?exists>${detail.activitiesOptionalPartPrice}</#if></span>
			<span>其他活动：<#if detail.otherActivity?exists>${detail.otherActivity}</#if></span><br/><br/>
			<span>付款方式：<#if detail.payChannel?exists>${detail.payChannel}</#if></span>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <span >交易号：<#if detail.payTid?exists>${detail.payTid}</#if></span><br/><br/>
            <span >平台微信交易号：<#if detail.weixinMark?exists>${detail.weixinMark}</#if></span><br/><br/>
            <span >平台支付宝交易号：<#if detail.zhifubaoMark?exists>${detail.zhifubaoMark}</#if></span><br/><br/>
            <span >平台银联交易号：<#if detail.yinlianMark?exists>${detail.yinlianMark}</#if></span><br/><br/>
			<span>商家备注：<span id="sellerRemark">${detail.remark!""}</span></span><br/><br/>
            <span>客服备注：<span id="sellerRemark">${detail.remark2!""}</span></span><br/><br/>
			<a id="changePrice" onclick="editPrice()" href="javascript:;" class="easyui-linkbutton" style="width:100px" >改价</a>
			<a id="setMarks" onclick="editMarks()" href="javascript:;" class="easyui-linkbutton" style="width:100px">商家备注</a>
			<a id="setKefuMarks" onclick="editKefuMarks()" href="javascript:;" class="easyui-linkbutton" style="width:100px">客服备注</a>
		</div>
		<br/><br/>
		<div style="padding:10px;">
			<span style="padding-right:30px;">客服问题登记</span>
			<a id="addOrderQuestion" onclick="addOrderQuestion()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >新增</a>
			<a id="viewSellerInfo" onclick="viewSellerInfo(${detail.sellerId!'0'})" href="javascript:;" class="easyui-linkbutton" style="width:140px">查看商家信息</a>
			<br/>
			<hr/>
			<table id="s_question"></table>
		</div>
		<br/><br/>
		<div style="padding:10px;">
			<span style="padding-right:30px;">质量监督处理情况</span>
			<hr/>
			<p><span>商家发货时效：${detail.sellerSendTime}</span></p>
			<p><span>该订单发货情况：${detail.sendTimeoutStatus}</span>
			<span>申诉情况：${detail.orderSendTimeoutComplain}</span><br/><br/></p>
			<p><span>QC处理记录：</span></p>
			<#if detail.sendTimeoutQcList?exists>
				<#list detail.sendTimeoutQcList as sqc>
				<p><span>${sqc.dealTime}</span><span>${sqc.dealUser}</span></p>
				<p style="padding-left: 30px;"><span>原因：${sqc.reason}</span></p>
				<p style="padding-left: 30px;"><span>说明：${sqc.dealRemark}</span></p>
				<p style="padding-left: 30px;"><span>处理结果：${sqc.dealResult}</span></p>
				</#list>
			</#if>
			<hr/>
			<p><span>物流更新时效：${detail.logisticsTime}</span></p>
			<p><span>该订单物流更新：${detail.logisticsTimeoutStatus}</span>
			<span>申诉情况：${detail.orderLogisticsTimeoutComplain}</span><br/><br/></p>
			<p><span>QC处理记录：</span></p>
			<#if detail.logisticsTimeoutQcList?exists>
				<#list detail.logisticsTimeoutQcList as lqc>
				<p><span>${lqc.dealTime}</span><span>${lqc.dealUser}</span></p>
				<p style="padding-left: 30px;"><span>原因：${lqc.reason}</span></p>
				<p style="padding-left: 30px;"><span>说明：${lqc.dealRemark}</span></p>
				<p style="padding-left: 30px;"><span>处理结果：${lqc.dealResult}</span></p>
				</#list>
			</#if>
		</div>
		<br/><br/>
		<div style="padding:10px;">
			<span style="padding-right:30px;">物流信息</span>
			<#if detail.logisticsChannel?exists>
				<span>${detail.logisticsChannel}：${detail.logisticsNumber}</span>
				<span><a onclick="editSendOrder()" href="javascript:;" class="easyui-linkbutton" style="width:140px">修改发货信息</a></span>
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
		<span style="color:red">商品总价：<#if detail.totalProductPrice?exists>${detail.totalProductPrice}</#if></span> 
		<br/>
		<hr/>
		<table id="s_data">
		</table>		
	</div>

		<div id="updateMarks" class="easyui-dialog" style="width:500px;height:200px;padding:20px 20px;">
            <form id="updateMarks_form" method="post">
            <input id="updateMarks_form_type" type="hidden" name="type" value=""/>
			<input id="updateMarks_form_id" type="hidden" name="id" value="${detail.id?c}" >
            <table cellpadding="5">
                <tr>
                    <td>备注:</td>
                    <td>
                    <textarea name="remark" onkeydown="checkEnter(event)" style="height: 60px;width: 300px">${detail.remark!""}</textarea>
						</td>
                </tr>
            </table>
        	</form>
        </div>
        
        <div id="updatePrice" class="easyui-dialog" style="width:500px;height:200px;padding:20px 20px;">
            <form id="updatePrice_form" method="post">
			<input id="updatePrice_form_id" type="hidden" name="id" value="${detail.id?c}" >
            <table cellpadding="5">
                <tr>
                    <td>新价格:</td>
                    <td>
                    	<input name="price" id="price" />
					</td>
                </tr>
                <#--<tr>-->
					<#--<td>是否可用优惠券:</td>-->
					<#--<td>-->
						<#--<input type="radio" name="isAvailableCoupon" value="0" />不可用-->
						<#--<input type="radio" name="isAvailableCoupon" value="1" checked />可用-->
					<#--</td>-->
                <#--</tr>-->
            </table>
        	</form>
        </div>
        
        <!-- 修改订单收货地址 -->
        <div id="updateAddress" class="easyui-dialog" style="width:500px;height:400px;padding:20px 20px;">
            <form id="updateAddress_form" method="post">
			<input id="updateAddress_form_id" type="hidden" name="tradeId" value="${detail.id?c}" >
            <table cellpadding="5">
            	<tr>
                    <td>姓名:</td>
                    <td><input name="fullName" value="${detail.receiveName!''}" /></td>
                </tr>
                <tr>
                    <td>身份证号:</td>
                    <td><input name="idCard" value="${detail.receiveIdCart!''}" /></td>
                </tr>
                <tr>
                    <td>手机:</td>
                    <td><input name="mobileNumber" value="${detail.receiveMobile!''}" /></td>
                </tr>
                <tr>
                    <td>省:</td>
                    <td>
                    	<select name="province" id="province">
                    		<#list detail.provinceList as bl >
			 					<option value="${bl.provinceId?c}" <#if detail.provinceId == bl.provinceId>selected</#if> >${bl.name}</option>
			 				</#list>
			 		   </select>
                    </td>
                </tr>
                <tr>
                    <td>市:</td>
                    <td>
                    	<select name="city" id="city">
                    		<#list detail.cityList as bl >
			 					<option value="${bl.cityId?c}" <#if detail.cityId == bl.cityId>selected</#if> >${bl.name}</option>
			 				</#list>
			 		   </select>
                    </td>
                </tr>
                <tr>
                    <td>区:</td>
                    <td>
                    	<select name="district" id="district">
                    		<#list detail.districtList as bl >
			 					<option value="${bl.districtId?c}" <#if detail.districtId == bl.districtId>selected</#if> >${bl.name}</option>
			 				</#list>
			 		   </select>
                    </td>
                </tr>
                <tr>
                    <td>详细地址:</td>
                    <td><textarea name="detailAddress" onkeydown="checkEnter(event)" style="height: 60px;width: 300px">${detail.detailAddress!""}</textarea></td>
                </tr>
            </table>
        	</form>
        </div>
        
        <!-- 订单问题 begin-->
        <div id="updateOrderQuestion" class="easyui-dialog" style="width:600px;height:300px;padding:20px 20px;">
            <form id="updateOrderQuestion_form" method="post">
				<input id="updateOrderQuestion_form_orderId" type="hidden" name="orderId" value="${detail.id?c}" >
				<span>问题类型：</span>
				<span><input type="text" name="templateId" id="updateOrderQuestion_form_templateId" style="width: 300px;"/></span><br/><br/>
				<span>问题描述：</span>
				<span><textarea rows="4" cols="40" id="questionDesc" name="questionDesc"></textarea></span><br/>
				<table id="imageDetails">
					<tr>
						<td>上传图片：<br/></td>
						<td>
							<input type="text" value=""  name="image" style="width: 250px;"/>
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
							<br/>
						</td>
						<td>
							<span onclick="addImageDetailsRow(this)" style="cursor: pointer;color:red">添加&nbsp;|</span>
                    		<span onclick="removeImageDetailsRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
							<br/>
						</td>
					</tr>
			  </table>
			  <!-- <span>回复顾客时限：</span>
			  <span>
			  	<input type="radio" name="timeLimitType" value="1"/>1小时后
			  	<input type="radio" name="timeLimitType" value="2"/>2小时后
			  	<input type="radio" name="timeLimitType" value="3"/>自定义
			  	<input type="text" name="customTime"  id="customTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00'})"/>
			  </span> -->
        	</form>
        </div>
		<!-- 订单问题 end-->
		
		<!-- 修改发货信息begin -->
		<div id="sendOrderDiv" class="easyui-dialog" style="width:350px;height:240px;padding:20px 20px;">
           	<form id="sendOrderForm" method="post">
           		<input id="orderId" type="hidden" name="orderId" />
           		<input type="radio" id="sendType1" name="sendType" value="1" checked /> 有物流
           		<input type="radio" id="sendType0" name="sendType" value="0"/> 无物流
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
                    		<td>物流运费:</td>
                    		<td><input id="money" type="text" name="money" ></input></td>
                		</tr>
            		</table>
           		</div>
       		</form>
       	</div>
       	<!-- 修改发货信息end -->	
		
	<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
	    <form id="picForm" method="post" enctype="multipart/form-data">
	        <input id="picFile" type="file" name="picFile" /><br/><br/>
	        <input id="limitSize" type="hidden" name="limitSize" value="0"/><br/><br/>
	        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
	    </form>
	</div>		
</div>

<script>

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
function clearUpdateMarksForm(){
	$("#updateMarks_form_type").val('');
	$("#updateMarks_form textarea[name='remark']").val('');
}

function editMarks(){
	clearUpdateMarksForm();
	$("#updateMarks_form textarea[name='remark']").val($("#sellerRemark").text());
	$('#updateMarks_form_type').val('1');
	$('#updateMarks').dialog('open');
}

function editKefuMarks(){
	clearUpdateMarksForm();
	$('#updateMarks_form_type').val('2');
	$('#updateMarks').dialog('open');
}

function editPrice(){
	$('#updatePrice').dialog('open');
}

function editReceiveAddress(){
	$('#updateAddress').dialog('open');
}

	$(function(){
		
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
        });
        
        
		$('#province').change(function(){
			$child = $('#city');
			var pid = $('#province').val();
			var selected_id = 0;
			$('#district').empty();
			$.ajax({
	            url:"${rc.contextPath}/order/getAllCity",
	            type:'post',
	            data: {id : pid},
	            dataType: 'json',
	            success:function(data){
	                var options = '<option value="0">--请选择--</option>';
	                $.each(data,function(i){
	                    if(data[i].id == selected_id){
	                        options += '<option value="'+this.cityId+'" selected="selected">'+this.name+'</option>';  
	                    }else{
	                        options += '<option value="'+this.cityId+'" >'+this.name+'</option>'; 
	                    }
	                });
	                $child.empty().append(options);
	            }
	        });
		});
		
		$('#city').change(function(){
			$child = $('#district');
			var pid = $('#city').val();
			var selected_id = 0;
			$.ajax({
	            url:"${rc.contextPath}/order/getAllDistrict",
	            type:'post',
	            data: {id : pid},
	            dataType: 'json',
	            success:function(data){
	                var options = '<option value="0">--请选择--</option>';
	                $.each(data,function(i){
	                    if(data[i].id == selected_id){
	                        options += '<option value="'+this.districtId+'" selected="selected">'+this.name+'</option>';  
	                    }else{
	                        options += '<option value="'+this.districtId+'" >'+this.name+'</option>'; 
	                    }
	                });
	                $child.empty().append(options);
	            }
	        });
		});
		
		$('#updateMarks').dialog({
            title:'修改备注',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#updateMarks_form').form('submit',{
                        url:"${rc.contextPath}/order/updateMarks",
                        onSubmit: function(){ 
                        	var type = $("#updateMarks_form_type").val();
                            var remark = $("#updateMarks_form textarea[name='remark']").val();
                            if($.trim(remark) == ''){
                            	$.messager.alert('提示',"请输入备注",'error');
                            	return false;
                            }else if(type == 1 && (remark.length > 100)){
                            	$.messager.alert('提示',"商家备注字数不得超过100",'error');
                            	return false;
                            }else if(type == 2 && (remark.length > 200)){
                            	$.messager.alert('提示',"客服备注字数不得超过200",'error');
                            	return false;
                            }
                        }, 
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	window.location.href = window.location.href
                                    $('#updateMarks').dialog('close');
                                });
                            } else if(res.status == 0){
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                });
                            } else{
                                $.messager.alert('响应信息',res.msg,'error',function(){
                                });
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#updateMarks').dialog('close');
                }
            }]
        });
		
		$('#updatePrice').dialog({
            title:'修改价格',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                	$.messager.progress();
                    $('#updatePrice_form').form('submit',{
                        url:"${rc.contextPath}/order/updatePrice",
                        success:function(data){
                        	$.messager.progress('close');
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	window.location.href = window.location.href
                                    $('#updatePrice').dialog('close');
                                });
                            } else if(res.status == 0){
                                $.messager.alert('响应信息',res.msg,'info');
                            } else{
                               	$.messager.alert('响应信息',res.msg,'error');
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#updatePrice').dialog('close');
                }
            }]
        });
		
		$('#updateAddress').dialog({
            title:'修改订单收货地址',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#updateAddress_form').form('submit',{
                        url:"${rc.contextPath}/order/updateAddress",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功,"+res.msg,'info',function(){
                                	window.location.href = window.location.href
                                    $('#updateAddress').dialog('close');
                                });
                            } else if(res.status == 0){
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                });
                            } else{
                                $.messager.alert('响应信息',res.msg,'error',function(){
                                });
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#updateAddress').dialog('close');
                }
            }]
        });
	
	
		$('#updateOrderQuestion').dialog({
            title:'新增订单问题',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#updateOrderQuestion_form').form('submit',{
                        url:"${rc.contextPath}/orderQuestion/addOrderQuestion",
                        onSubmit:function(){
                        	var templateId = $('#updateOrderQuestion_form_templateId').combobox('getValue');
                        	var questionDesc = $('#questionDesc').val();
                        	/* var timeLimitType = $("input[name='timeLimitType']:checked").val();
                        	var customTime = $("#customTime").val(); */
                        	if(templateId == '' || templateId == null || templateId == undefined){
                        		$.messager.alert('提示',"请选择问题类型",'error');
                        		return false;
                        	}else if($.trim(questionDesc) == '' || questionDesc.length>200){
                        		$.messager.alert('提示',"问题描述不能为空且字数必须小于200",'error');
                        		return false;
                        	}/* else if(timeLimitType == '' || timeLimitType == null || timeLimitType == undefined){
                        		$.messager.alert('提示',"请选择顾客回复时限",'error');
                        		return false;
                        	}else if(timeLimitType == 3 && $.trim(customTime) == ''){
                        		$.messager.alert('提示',"请填写自定义顾客回复时限",'error');
                        		return false;
                        	} */
                        },
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	window.location.href = window.location.href
                                    $('#updateAddress').dialog('close');
                                });
                            }else{
                                $.messager.alert('响应信息',res.msg,'error');
                            }
                        }
                    })
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#updateOrderQuestion').dialog('close');
                }
            }]
        });
		
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
					{field : 'sendGoodsStatus', title : '是否仍然发货', width : 30, align : 'center'},
					{field : 'sendGoodsCount', title : '发货数量', width : 30, align : 'center'},
					{field : 'sellerName', title : '商家', width : 40, align : 'center'},
					{field : 'bName', title : '品牌', width : 40, align : 'center'},
					{field : 'sellerType', title : '发货类型', width : 40, align : 'center'},
					{field : 'sendAddress', title : '发货地', width : 40, align : 'center'},
					{field : 'price', title : '售价', width : 40, align : 'center'},
					{field : 'count', title : '购买数量', width : 40, align : 'center'},
					{field : 'totalPrice', title : '总价', width : 40, align : 'center'}
					] ],
			rownumbers : true
			});
		
		$('#s_question').datagrid({
			nowrap : false,
			striped : true,
			collapsible : true,
			idField : 'id',
			url : '${rc.contextPath}/orderQuestion/orderQuestionInfo',
			loadMsg : '正在装载数据...',
			fitColumns : true,
			queryParams : {
				orderId : ${detail.id?c}
			},
			remoteSort : true,
			singleSelect : true,
			columns : [ [
					{field : 'customerStatusStr', title : '顾客问题状态', width : 15, align : 'center',
						formatter:function(value,row,index){
							if(row.customerStatus == 1){
								return '<span style="color:red">'+row.customerStatusStr+'</span>'
							}else{
								return row.customerStatusStr;
							}
						}	
					},
					{field : 'leftTime', title : '回复顾客时限', width : 15, align : 'center'},
					{field : 'sellerStatusStr', title : '商家对接状态', width : 15, align : 'center',
						formatter:function(value,row,index){
							if(row.sellerStatus == 1){
								return '<span style="color:red">'+row.sellerStatusStr+'</span>'
							}else{
								return row.sellerStatusStr;
							}
						}	
					},
					{field : 'questionType', title : '问题类型', width : 20, align : 'center'},
					{field : 'customerDealDetail', title : '顾客问题描述', width : 70, align : 'left'},
					{field : 'sellerDealDetail', title : '商家对接描述', width : 70, align : 'left'},
					{field : 'isPush', title : '是否推送商家', width : 20, align : 'center',
						formatter:function(value,row,index){
							if(parseInt(row.isPush)==1){
								return '是';
							}else{
								return '否';
							}
						}	
					},
					{field : 'sellerFeedbackDetail', title : '商家反馈', width : 70, align : 'left'},
					{field : 'createUser', title : '创建人', width : 10, align : 'center'},
					{field:'hidden',  title:'操作', width:10,align:'center',
						formatter:function(value,row,index){
							return '<a href="javascript:void(0);" onclick="viewOrderQuestion('+row.id+')">处理问题</a>';
						}	
					}
					] ],
			rownumbers : true
			});
		
		$("#updateOrderQuestion_form_templateId").combobox({
			url:'${rc.contextPath}/orderQuestion/jsonOrderQuestionTemplate',   
		    valueField:'code',   
		    textField:'text'  
		});
		
		$('#sendType1').change(function(){
			if($('#sendType1').is(':checked')){
				$('#haveChannel').show();
			}
		});
		$('#sendType0').change(function(){
			if($('#sendType0').is(':checked')){
				$('#haveChannel').hide();
			}
		});
		
		$('#channel').combobox({   
		    url:'${rc.contextPath}/order/jsonCompanyCode',   
		    valueField:'code',   
		    textField:'text'  
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
                        url:"${rc.contextPath}/order/sendOrder",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#sendOrderDiv').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                    $('#s_data').datagrid('reload');
                                });
                            } else if(res.status == 2){
                                $.messager.alert('响应信息',"该订单已经发货",'info',function(){
                                });
                            }else if(res.status == 3){
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                });
                            } 
                            else{
                                $.messager.alert('响应信息',"发货失败",'error',function(){
                                });
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
	});
	
function viewOrderQuestion(id){
	var urlStr = "${rc.contextPath}/orderQuestion/orderQuestionDetail/"+id;
	window.open(urlStr,"_blank");
}

function addOrderQuestion(){
	$('#updateOrderQuestion').dialog('open');
}

function viewSellerInfo(sellerId){
	var urlStr = "${rc.contextPath}/seller/edit/"+sellerId;
	window.open(urlStr,"_blank");
}

function addImageDetailsRow(obj){
	var length = $("#imageDetails").find("tr").size();
	var row= $("#imageDetails").find("tr").first().clone();
	$(row).find("input[name='image']").val('');
	$(obj).parent().parent().after(row);
	$('#updateOrderQuestion').dialog('resize',{
		width: 600,
		height: 300 + 50 * length
	});
}

function removeImageDetailsRow(obj){
	$(obj).parent().parent().remove();
	var length = $("#imageDetails").find("tr").size();
	$('#updateOrderQuestion').dialog('resize',{
		width: 600,
		height: 300 + 50 * length
	});
}


var $obj;
function picDialogOpen(obj) {
	$obj = $(obj).prev();
    $("#picDia").dialog("open");
}
function picDialogClose() {
   $("#picDia").dialog("close");
}
function picUpload() {
    $('#picForm').form('submit',{
        url:"${rc.contextPath}/pic/fileUpLoad",
        success:function(data){
            var res = eval("("+data+")")
            if(res.status == 1){
                $.messager.alert('响应信息',"上传成功...",'info',function(){
                    $("#picDia").dialog("close");
                    if($obj != null && $obj!='' && $obj !=undefined) {
                    	$obj.val(res.url);
                        $("#picFile").val("");
                    }
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

function editSendOrder() {
	$("#sendType1").prop("checked", true);
	$("#sendType1").change();
	$('#channel').combobox('clear');
	$('#money').val("");
	$('#number').val("");
	$('#orderId').val("");
	$('#channel').combobox('setValue', '${detail.logisticsChannel!""}');
	$('#money').val(${detail.logisticsMoney!'0'});
	$('#number').val(${detail.logisticsNumber!""});
	$('#orderId').val(${detail.id?c});
	$('#sendOrderDiv').dialog('open');
}
</script>

</body>
</html>