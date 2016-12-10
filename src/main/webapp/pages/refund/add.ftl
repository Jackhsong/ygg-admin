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
	tr,td{font-size:16px;}
	.tdName{
		padding-right:5px;
		text-align:left;
	}
	.tdText{
		padding-left:10px;
		text-align:justify;
	}
	.yggTable{
		margin-left:20px;
		margin-top:20px;
	}
}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center',title:'新建退款退货订单'" style="padding: 5px;padding-left: 15px;">
		<input type="hidden" type="accountId" id="accountId" />
		<input type="hidden" type="orderId" id="orderId" />
		<table class="yggTable" cellpadding="10">
			<tr>
				<td class="tdName">退款类型：</td>
				<td class="tdText">
					<input type="radio" name="type" id="sendType1" value="1">仅退款&nbsp;			
					<input type="radio" name="type" id="sendType2" value="2">退款并退货
				</td>
			</tr>
			<tr>
				<td class="tdName">订单号：</td>
				<td class="tdText">
					<input type="text" name="number" value="" style="width:320px"></input>
				</td>
			</tr>
			<tr>
				<td class="tdName">收货人：</td>
				<td class="tdText">
					<span id="receiveName_span"></span>&nbsp;&nbsp;收货人手机号：<span id="receiveMobile_span"></span>
				</td>
			</tr>
			<tr>
				<td class="tdName">选择商品：</td>
				<td class="tdText">
					<select id="orderProductId" style="width:320px">
						
					</select>
				</td>
			</tr>
			<tr>
				<td class="tdName">选择数量：</td>
				<td class="tdText">
					<select id="orderProductCount" style="width:320px">
						
					</select>
				</td>
			</tr>
			<tr>
				<td class="tdName">选择退款账号：</td>
				<td class="tdText">
					<select id="accountCardId" style="width:320px">
					</select>
					<a onclick="addUserAccount()" href="javascript:;" class="easyui-linkbutton">管理账户</a>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="tdText">
					<span style="color:red">
						申请退款商品数量：<span id="selectProductCount"></span>&nbsp;&nbsp;&nbsp;
						商品总价：<span id="selectPrice"></span>&nbsp;&nbsp;&nbsp;
						分摊邮费：<span id="freightMoneyProportion"></span>&nbsp;&nbsp;&nbsp;
						分摊优惠券：<span id="couponProportion"></span>&nbsp;&nbsp;&nbsp;
						分摊积分抵扣：<span id="pointProportion"></span>&nbsp;&nbsp;&nbsp;
						分摊满减金额：<span id="activitiesProportion"></span>&nbsp;&nbsp;&nbsp;
						分摊N元任选优惠金额：<span id="activitiesOptionalPartProportion"></span>&nbsp;&nbsp;&nbsp;
						分摊客服改价抵扣：<span id="adjustProportion"></span><br/><br/>
						理论申请退款金额 ：<span id="logicApplyPrice"></span>&nbsp;&nbsp;&nbsp;
						理论应返还积分：<span id="logicGiveAccountPoint"></span>&nbsp;&nbsp;&nbsp;
						理论应扣除积分：<span id="logicRemoveAccountPoint"></span>
					</span>
				</td>
			</tr>
			<tr>
				<td class="tdName">退款金额：</td>
				<td class="tdText">
					<input type="text" name="money" value="" style="width:320px"></input>
				</td>
			</tr>
			<tr>
				<td class="tdName">图片1: </td>
				<td class="tdText">
					<input type="text" id="image1" name="image1" style="width:320px;" value="" />
					<a onclick="picDialogOpen('image1')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
				</td>
			</tr>
			<tr>
				<td class="tdName">图片2: </td>
				<td class="tdText">
					<input type="text" id="image2" name="image2" style="width:320px;" value="" />
					<a onclick="picDialogOpen('image2')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
				</td>
			</tr>
			<tr>
				<td class="tdName">图片3: </td>
				<td class="tdText">
					<input type="text" id="image3" name="image3" style="width:320px;" value="" />
					<a onclick="picDialogOpen('image3')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="tdText">
					<a id="saveRefund" href="javascript:;" class="easyui-linkbutton" style="width:100px">保存</a>
				</td>
			</tr>
		</table>
	</div>

	<!-- 图片上传dialog -->
	<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
	    <form id="picForm" method="post" enctype="multipart/form-data">
	        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
	        <input type="hidden" name="limitSize" value="0"/>
	        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
	    </form>
	</div>

	<!-- 新增 -->
	<div id="addAccountCard_Div" style="padding: 5px; width: 300px; height: 210px;">
		<table>
			<tbody>
				<tr>
					<td>
						<input type="radio" name="bankOrAlipay" id="bank" value="1">银行
					</td>
					<td>
						<input type="radio" name="bankOrAlipay" id="alipay" value="2">支付宝	
					</td>
				</tr>
			</tbody>
			<tbody id="bankTBody" style="display:none">
				<tr id="bankTypeTR">
					<td>银行类型：</td>
					<td>
						<select id="bankType" >
							<option value="1">中国工商银行</option>
							<option value="2">中国农业银行</option>
							<option value="3">中国银行</option>
							<option value="4">中国建设银行</option>
							<option value="5">中国邮政储蓄银行</option>
							<option value="6">交通银行</option>
							<option value="7">招商银行</option>
							<option value="8">中国光大银行</option>
							<option value="9">中信银行</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>账号名：</td>
					<td><input name="cardName" style="width:100"></td>
				</tr>
				<tr>
					<td>账号：</td>
					<td><input name="cardNumber" style="width:100"></td>
				</tr>
			</tbody>
		</table>
	</div>

<script>
	var inputId;
	var orderProduct;
	function picDialogOpen($inputId) {
	    inputId = $inputId;
	    $("#picDia").dialog("open");
	    $("#yun_div").css('display','none');
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
	                    if(inputId) {
	                        $("#"+inputId).val(res.url);
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

	function addUserAccount(){
		$('#addAccountCard_Div').dialog("open");
	}
	
	$(function(){
		$('#picDia').dialog({
            title:'图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
        });
		
		$('#addAccountCard_Div').dialog({
            title:'新增用户账户',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                	var params = {};
                	params.accountId = $("#accountId").val();
                	if(params.accountId == ''){
                		$.messager.alert("info","请先填写订单编号","warning");
                		return ;
                	}
                	params.bankOrAlipay = $("input[name='bankOrAlipay']:checked").val();
                	params.bankType = $("#bankType").val();
                	params.cardName = $("input[name='cardName']").val();
                	params.cardNumber = $("input[name='cardNumber']").val();
                	if(params.bankOrAlipay == undefined){
                		$.messager.alert("info","请选择账户类型","warning");
                	}else if(params.bankOrAlipay == 1 && params.cardNumber == ''){
                		$.messager.alert("info","请填写支付宝账号","warning");
                	}else if(params.bankOrAlipay == 2 && (params.cardNumber == '' || params.cardName == '')){
                		$.messager.alert("info","请填写银行信息","warning");
                	}else{
                		$.messager.progress();
                    	$.ajax({
        					url: '${rc.contextPath}/account/saveAccountCard',
                 	       	type: 'post',
                 	       	dataType: 'json',
                 	       	data: params,
                 	       	success: function(data){
                 	       		$.messager.progress('close');
                 	       		if(data.status == 1){
                 	       			$.messager.alert("warn","保存成功","info",function(){
                 	       				$('#addAccountCard_Div').dialog("close");
                 	       			});
                 	       			var options = '<option value="'+data.id+'" >'+data.name+'</option>'; 
                 	       			$("#accountCardId").append(options);
                 	       		}else{
                 	       			$.messager.alert("warn",data.msg,"error");
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
                	$("#bankTBody").hide();
                	$("#bank").prop("checked",false);
                	$("#alipay").prop("checked",false);
                	$("input[name='cardName']").val("");
                	$("input[name='cardNumber']").val("");
                	$('#addAccountCard_Div').dialog("close");
                }
            }]
        });
		
		//新增银行信息js
		$("#alipay").change(function(){
			$("#bankTBody").show();
			$("#bankTypeTR").hide();
		});
		$("#bank").change(function(){
			$("#bankTBody").show();
			$("#bankTypeTR").show();
		});
		
		//订单号改变带出收货人信息
		$(".yggTable input[name='number']").change(function(){
			$("#accountId").val("");
			$("#orderId").val("");
			$("#orderProductId").empty();
   	  		$("#accountCardId").empty();
   	  		$("#receiveName_span").text("");
	  		$("#receiveMobile_span").text("");
			var number = $.trim($(this).val());
			if(/^[1-9]+\d*$/.test(number)){
				$.ajax({
					url: '${rc.contextPath}/order/getOrderInfo',
         	       	type: 'post',
         	       	dataType: 'json',
         	       	data: {'number':number},
         	       	success: function(data){
         	         $.messager.progress('close');
         	           if(data.status == 1){
         	        	  $("#orderId").val(data.orderId);
         	        	   $("#accountId").val(data.accountId);
         	        	   //收货人信息	
         	        	   	$("#receiveName_span").text(data.receiveInfo.fullName);
         	        	  	$("#receiveMobile_span").text(data.receiveInfo.mobileNumber);
         	        	  	//订单商品信息
         	        	  	orderProduct = data.orderProduct;
         	        	  	var options1 = '<option value="0">--请选择--</option>';
         	        	  	$.each(orderProduct,function(i){
        	                    options1 += '<option value="'+this.id+'" >'+this.name+'</option>'; 
        	                });
         	        	  	$("#orderProductId").empty().append(options1);
         	        	  	//银行账户信息
                           	var options2 = '<option value="0">原路返回</option>';
         	        	  	$.each(data.accountCardList,function(i){
        	                    options2 += '<option value="'+this.id+'" >'+this.name+'</option>'; 
        	                });
         	        	  	$("#accountCardId").empty().append(options2);
         	           }else if(data.status == 2){
         	        	  $.messager.alert("提示",'此订单状态不符合退款退货处理',"error");
         	           }
         	       },
         	       error: function(xhr){
         	    	  $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
         	       }
				});
			}
		});
		
		$('#orderProductId').change(function(){
			var orderProductId = $(this).val();
			var productCount = 0;
			$.each(orderProduct,function(i){
                if(orderProduct[i].id == orderProductId){
                	productCount = orderProduct[i].productCount;
                }
            });
			var options = '<option value="0">--请选择--</option>';
			for (var i = 1 ; i <= productCount ; i++)
			{
				options += '<option value="'+i+'" >'+i+'</option>'; 
			}
     	  	$("#orderProductCount").empty().append(options);
		});
		
		//数量改变时计算相应的应该退款的金额
		$("#orderProductCount").change(function(){
			$.messager.progress({text:"正在计算金额"});
			$("#selectProductCount").text("");
   			$("#selectPrice").text("");
   			$("#couponProportion").text("");
   			$("#freightMoneyProportion").text("");
   			$("#pointProportion").text("");
   			$("#activitiesProportion").text("");
   			$("#activitiesOptionalPartProportion").text("");
   			$("#adjustProportion").text("");
  			$("#logicApplyPrice").text("");
 			$("#logicGiveAccountPoint").text("");
			$("#logicRemoveAccountPoint").text("");
			var params = {};
			params.number = $(".yggTable input[name='number']").val();
			params.selectProductCount = $("#orderProductCount").val();
			params.orderProductId = $("#orderProductId").val();
			if(params.selectProductCount == 0 || params.orderProductId == 0){
				//$.messager.alert("提示",data.msg,"info");
				$.messager.progress('close');
				return;
			}else{
				$.ajax({
					url: '${rc.contextPath}/order/calOrderRefundInfo',
	     	       	type: 'post',
	     	       	dataType: 'json',
	     	       	data:params,
	     	       	success: function(data){
	     	       		$.messager.progress('close');
	     	       		if(data.status == 1){
	     	       			$("#selectProductCount").text(data.selectProductCount);
	     	       			$("#selectPrice").text(data.selectPrice);
	     	       			$("#couponProportion").text(data.couponProportion);
	     	       			$("#freightMoneyProportion").text(data.freightMoneyProportion);
	     	       			$("#pointProportion").text(data.pointProportion);
	     	       			$("#adjustProportion").text(data.adjustProportion);
	     	       			$("#activitiesProportion").text(data.activitiesProportion);
	     	       			$("#activitiesOptionalPartProportion").text(data.activitiesOptionalPartProportion);
	     	      			$("#logicApplyPrice").text(data.logicApplyPrice);
	     	     			$("#logicGiveAccountPoint").text(data.logicGiveAccountPoint);
	     	    			$("#logicRemoveAccountPoint").text(data.logicRemoveAccountPoint);
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
		});
		
		//保存退款订单信息
		$("#saveRefund").click(function(){
			var params = {
					orderId : $("#orderId").val(),
					accountId : $("#accountId").val(),
					type : $("input[name='type']:checked").val(),
					number : $.trim($(".yggTable input[name='number']").val()),
					orderProductId : $("#orderProductId").val(),
					selectProductCount : $("#orderProductCount").val(),
					accountCardId : $("#accountCardId").val(),
					money : $.trim($(".yggTable input[name='money']").val()),
					image1 : $.trim($("#image1").val()),
					image2 : $.trim($("#image2").val()),
					image3 : $.trim($("#image3").val())
			};
			//alert(params.selectProductCount);
			if(params.type == undefined){
				$.messager.alert("提示","请选择退款类型","warning");
			}else if(params.number == '' || params.accountId == '' || params.orderId == ''){
				$.messager.alert("提示","请填写正确的订单编号","warning");
			}else if(params.orderProductId == null || params.orderProductId == 0){
				$.messager.alert("提示","请选择正确的商品","warning");
			}else if(params.selectProductCount == null || params.selectProductCount == 0){
				$.messager.alert("提示","请选择正确的商品数量","warning");
			}else if(params.accountCardId == null){
				$.messager.alert("提示","请选择正确的退款账户","warning");
			}else if(params.money == '' || !/^\d+\.?\d*$/.test(params.money)){
				$.messager.alert("提示","请填写正确退款金额","warning");
			}else{
				$.messager.progress({text:"保存中"});
				$.ajax({
					url: '${rc.contextPath}/refund/saveRefund',
	     	       	type: 'post',
	     	       	dataType: 'json',
	     	       	data:params,
	     	       	success: function(data){
	     	       		$.messager.progress('close');
	     	       		if(data.status == 1){
	     	       			$.messager.alert("提示","保存成功","info",function(){
	     	       				window.location.href = window.location.href; 
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
		});
		
	});
</script>

</body>
</html>