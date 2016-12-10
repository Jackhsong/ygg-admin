<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/bootstrap/js/bootstrap.min.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>

<style type="text/css">
	#myCss span {
		font-size:12px;
		padding-right:20px;
	}
	body {
		font-size:14px;
		line-height:20px;
	}
</style>

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
<div style="margin-left:25px">
	<form id="orderForm"  method="get">
		<table>
			<tr>
				<td><label>建单原因：</label></td>
				<td><input type="text" class="input-xxlarge" name="remark" /><span style="color:red">*</span></td>
			</tr>
			<tr>
	             <td><label>补发类型：</label></td>
				<td>
					<input type="radio" name="sendType" id="sendType1" value="1">&nbsp;售后补发货  						
					<input type="radio" name="sendType" id="sendType2" value="2">&nbsp;顾客打款发货
				</td>
	        </tr>
            <tr id="transferAmountTR" style="display: none">
            	<td><label>打款金额：</label></td>
                <td>
                	<input type="text" class="input-xlarge" id="transferAmount" name="totalPrice" />
                	&nbsp;<font color="red">必填</font>
                </td>
            </tr>
            <tr id="transferAccountTR" style="display: none">
            	<td><label>打入账户：</label></td>
                <td>
                	<input type="text" class="input-xlarge" id="transferAccount" style="height:27px" name="transferAccount" />
                	&nbsp;<font color="red">必填</font>
                </td>
            </tr>
            <tr id="transferTimeTR" style="display: none">
            	<td><br/><label>打款时间：</label></td>
                <td>
                	<br/><input type="text" class="input-xlarge"id="transferTime" name="transferTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                	&nbsp;<font color="red">必填</font>
                </td>
            </tr>
			<tr>
				<td><label>订单类型：</label></td>
				<td>
					<input type="radio" name="type" id="type" value="1" checked>&nbsp;普通订单  						
 					<input type="radio" name="type" id="type" value="2">&nbsp;海外购订单
					<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：海外购订单身份证号为必填项</span>
				</td>
			</tr>
			<tr>
				<td><label>姓名：</label></td>
				<td><input type="text" class="input-xlarge" name="fullName" /><span>&nbsp;&nbsp;注：海外购订单请填写真实姓名</span></td>
			</tr>
			<tr>
				<td><label>身份证号：</label></td>
				<td><input type="text" class="input-xlarge" name="idCard" /><span></span></td>
			</tr>
			<tr>
				<td><label>手机号：</label></td>
				<td><input type="number" class="input-xlarge" name="mobileNumber" /><span></span></td>
			</tr>
			<tr>
                <td><label>省：</label></td>
                <td>
                	<select class="input-xlarge" name="province" id="province">
                		<#list provinceList as bl >
			 					<option value="${bl.provinceId?c}">${bl.name}</option>
			 				</#list>
		   			</select>
                </td>
                </tr>
                <tr>
                    <td><label>市：</label></td>
                    <td>
                    	<select class="input-xlarge" name="city" id="city">
			 		   </select>
                    </td>
                </tr>
                <tr>
                    <td><label>区：</label></td>
                    <td>
                    	<select class="input-xlarge" name="district" id="district">
			 		   </select>
                    </td>
                </tr>
                <tr>
                    <td><label>详细地址：</label></td>
                    <td><input type="text" class="input-xxlarge" name="detailAddress" /><span style="color:red"></span></td>
                </tr>
                <tr>
                    <td><label>商家：</label></td>
                    <td>
                    	<input class="input-xlarge" style="height:27px" id="sellerId" name="sellerId" />
                    </td>
                </tr>
                <tr>
                	<td><br><label>备注：</label></td>
                	<td>
                		<br><input type="text" class="input-xxlarge" id="desc" name="desc" maxlength="100"/>
                	</td>
                </tr>
		</table>
		<div id="productDiv" style="padding-top:20px;">
			<table>
				<tr>
                    <td></td>
                    <td><label>商品：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                </tr>
                <tr>
                    <td><label>ID：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td>
                    	<input onblur="showProductName(this)" type="number" class="input-large" name="productId" />
                    	<span style="" class="showPName"></span>
                    </td>
                </tr>
                <tr>
                    <td><label>数量：&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                    <td>
                    	<input type="number" class="input-large" name="productNum" />
                    	&nbsp;&nbsp;&nbsp;
                    	<span onclick="addOne()" style="cursor: pointer;color:red">添加</span>
                    	&nbsp;&nbsp;&nbsp;&nbsp;
                    	<span onclick="deleteIt(this)" style="cursor: pointer;color:red">删除</span>
                    </td>
                </tr>
			</table>
		</div>
		<div style="padding-top:20px;padding-left:20px">
			<button id="saveOrder" type="button" class="btn">创建</button>
		</div>
	</form>
</div>
</div>

<script>
function showProductName(obj){
	var pid = $(obj).val();
	if(pid != ''){
		$.ajax({
	        url: '${rc.contextPath}/product/findProductInfoById',
	        type: 'post',
	        dataType: 'json',
	        data: {'id':pid},
	        success: function(data){
	            if(data.status == 1){
	            	$(obj).next().text(data.msg);
	            }else{
	            	$(obj).next().text("");
	            	$.messager.alert("提示",data.msg,"info");
	            }
	        },
	        error: function(xhr){
	        	$(obj).next().text("");
	        	$.messager.alert("提示",'商品ID填写错误',"info");
	        }
	    });
	}
}

function addOne(){
	$('#productDiv').append($("#productDiv").find("table").first().clone());
	var $last = $("#productDiv").find("table").last();
	$last.find("input[name='productId']").val("");
	$last.find("input[name='productNum']").val("");
	$last.find(".showPName").text("");
}

function deleteIt(obj){
	$(obj).parent().parent().parent().remove();
}

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
$(function(){
	
	$('#saveOrder').click(function(){
		var nonsupportIds = '${nonsupportIds}';
		var nonsupportIdsArr = nonsupportIds.split(',');
		var sellerId = $('#sellerId').combobox('getValue');//商家
		var remark = $("input[name='remark']").val();//建单原因
		remark = $.trim(remark); 
		var type = $("input[name='type']:checked").val();//订单类型
		var fullName = $("input[name='fullName']").val();//姓名
		fullName = $.trim(fullName);
		var idCard = $("input[name='idCard']").val();//身份证号
		var mobileNumber = $("input[name='mobileNumber']").val();//手机号
		mobileNumber = $.trim(mobileNumber);
		var province = $("#province").val();
		var city = $("#city").val();
		if(city == null){
			city == '0';
		}
		var district = $("#district").val();
		if(district == null){
			district = '0';
		}
		var detailAddress = $("input[name='detailAddress']").val();//详细地址
		detailAddress = $.trim(detailAddress);
		// var sellerId = $('#sellerId').combobox('getValue');//商家
		var sendType = $("input[name='sendType']:checked").val();//发货类型
		var transferAccount = $("#transferAccount").combobox('getValue');//商家
		var transferTime = $("#transferTime").val();
		var transferAmount = $.trim($("#transferAmount").val());
		var desc = $('#desc').val();
		var pIds = $("input[name='productId']");
		var pNums = $("input[name='productNum']");
		var pIdAndNums="";
		$.each(pIds,function(i){
            var pid = $(pIds[i]).val();
            var pnum = $(pNums[i]).val();
            if(($.trim(pid)!="") && ($.trim(pnum)!="")){
            	pIdAndNums += ($.trim(pid) + ',' + $.trim(pnum) + ";");
            }
        });
		if(remark == '' || fullName == ''){
			$.messager.alert('info',"建单原因和收货人姓名必填","error");
		}else if(type==2 && idCard==''){//海外购订单身份证号为必填项
			$.messager.alert('info',"海外购订单身份证号为必填项","error");
//	如北京市没有市和区出来..	}else if(mobileNumber==''||province=='0'||city=='0'||district=='0'||detailAddress==''){//收货地址
		}else if(mobileNumber==''||province=='0'||detailAddress==''){//收货地址
			$.messager.alert('info',"请填写完整收货地址信息","error");
		}else if(sellerId=='' || pIdAndNums==''){//商家信息 & 商品信息
			$.messager.alert('info',"请填写完整商家信息和商品信息","error");
		}else if(typeof(sellerId) == "undefined"){//商家信息
			$.messager.alert('info',"不支持的商家，请在下拉列表中选择支持的商家","error");
		}else if(sendType == '' || sendType==null || sendType == undefined){
			$.messager.alert('提示',"请选择补发类型","error");
		}else if(nonsupportIdsArr.length > 0 && nonsupportIdsArr.indexOf(sellerId) > -1){
			$.messager.alert('提示',"该商家需要付款信息，请前往“手动新建wap订单”创建。","error");
		}
		else{
			if(sendType=='2'){
				if(transferAmount==''){
					$.messager.alert('提示',"请输入打款金额","error");
					return;
				}
				if(transferAccount==''){
					$.messager.alert('提示','请选择打款账户。如果没有可供选择的打款账户，请先去"售后管理-退款退货管理-财务打款账户管理"添加账户',"error");
					return;
				}
				if(transferTime==''){
					$.messager.alert('提示',"请填写打款时间","error");
					return;
				}
			}
			$.messager.progress();
			$.ajax({
			       url: '${rc.contextPath}/orderManual/validateProduct',
			       type: 'post',
			       dataType: 'json',
			       data: {'sellerId':sellerId,'pIdAndNums':pIdAndNums},
			       success: function(data){
		       			if(data.status == 1){
		       				if(sendType=='1'){
		       					transferAccount='0';
		       					transferTime="0000-00-00 00:00:00";
		       				}
		       				var params = {
		       						remark:remark, 
		       						type:type, 
		       						fullName:fullName,
		       						idCard:idCard,
		       						mobileNumber:mobileNumber,
		       						province:province,
		       						city:city,
		       						district:district,
		       						detailAddress:detailAddress,
		       						sellerId:sellerId,
		       						pIdAndNums:pIdAndNums,
		       						desc:desc,
		       						sendType:sendType,
		       						transferAccount:transferAccount,
		       						transferTime:transferTime
		       				}
		       				if(sendType=='2'){
		       					params.totalPrice = transferAmount;
		       				}
		       				$.ajax({
		 				       		url: '${rc.contextPath}/orderManual/save',
		 				       		type: 'post',
		 				       		dataType: 'json',
		 				       		data: params,
		 				       		success: function(data){
		 				         		$.messager.progress('close');
		 				           		if(data.status == 1){
		 				           		$.messager.alert("提示","保存成功","info",function(){
		 				           			//跳转
		 				           			window.location.href='${rc.contextPath}/orderManual/list'
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
		           		}else{
		           			$.messager.progress('close');
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
	
	$('#sellerId').combobox({
	    url:'${rc.contextPath}/seller/jsonSellerCode?isAvailable=1',
	    valueField:'code',   
	    textField:'text',
	    onSelect:function(){
	    	
	    }
	});
	
	$("#transferAccount").combobox({
		url:'${rc.contextPath}/refund/adminBankInfoCode',
		valueField:'code',   
	    textField:'text'
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
	
	$("#sendType1").change(function(){
		if($(this).is(':checked')){
			$("#transferAmountTR").css('display','none');
			$("#transferAccountTR").css('display','none');
			$("#transferTimeTR").css('display','none');
		}
	});
	
	$("#sendType2").change(function(){
		if($(this).is(':checked')){
			$("#transferAmountTR").css('display','');
			$("#transferAccountTR").css('display','');
			$("#transferTimeTR").css('display','');
		}
	});
	
});
</script>

</body>
</html>