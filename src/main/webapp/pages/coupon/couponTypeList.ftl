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
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center',title:'优惠券类型管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'',split:true" style="height: 100px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td>&nbsp;类型：</td>
						<td>
							<select name="searchCouponType" id="searchCouponType">
								<option value="">全部</option>
								<option value="1">满x减y优惠券</option>
								<option value="2">x元现金券</option>
							</select>
						</td>
						<td>&nbsp;使用范围：</td>
						<td><input name="searchCouponScope" id="searchCouponScope"/></td>
						<td>&nbsp;使用状态：</td>
						<td>
							<select name="searchIsAvailable" id="searchIsAvailable">
								<option value="-1">全部</option>
								<option value="1">可用</option>
								<option value="0">停用</option>
							</select>
						</td>
						<td>&nbsp;<a id="searchBtn" onclick="searchCouponDetail()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
						<td>&nbsp;&nbsp;<a id="clearBtn" onclick="clearCouponDetail()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >重置</a></td>
					</tr>
				</table>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
		  	<table id="s_data" style=""></table>
	
		    <!-- 新增 -->
		    <div id="addCouponTypeDiv" class="easyui-dialog" style="width:700px;height:400px;padding:20px 20px;">
		        <form id="addCouponTypeForm" method="post">
					<input id="addCouponTypeForm_id" type="hidden" name="couponTypeId" value="" >
			        <table cellpadding="5">
			            <tr>
			                <td>&nbsp;&nbsp;类型：</td>
			                <td>
			                	<input type="radio" name="type" id="addCouponTypeForm_type1" value="1"/>优惠券&nbsp;
			                	满<input type="text" name="threshold" id="addCouponTypeForm_threshold" value="" style="width: 50px;"/>
			                	减<input type="text" name="reduce1" id="addCouponTypeForm_reduce1" value="" style="width: 50px;"/><br/><br/>
			                	
			                	<input type="radio" name="type" id="addCouponTypeForm_type4" value="4"/>随机减免优惠券&nbsp;
			                	满<input type="text" name="threshold4" id="addCouponTypeForm_threshold4" value="" style="width: 50px;"/>
			                	减&nbsp;&nbsp;金额从<input type="text" name="lowestReduce4" id="addCouponTypeForm_lowestReduce4" value="" style="width: 50px;"/>
			                	到<input type="text" name="maximalReduce4" id="addCouponTypeForm_maximalReduce4" value="" style="width: 50px;"/><br/><br/>
			                	
			                	<input type="radio" name="type" id="addCouponTypeForm_type2" value="2"/>现金券&nbsp;
			                	<input type="text" name="reduce2" id="addCouponTypeForm_reduce2" value="" style="width: 50px;"/>元<br/><br/>
			                	
			                	<input type="radio" name="type" id="addCouponTypeForm_type3" value="3"/>随机现金券&nbsp;
			                	金额从<input type="text" name="lowestReduce3" id="addCouponTypeForm_lowestReduce3" value="" style="width: 50px;"/>
			                	到<input type="text" name="maximalReduce3" id="addCouponTypeForm_maximalReduce3" value="" style="width: 50px;"/>
			                </td>
			             </tr>
			             <tr>
			                <td><br/><br/>使用范围：</td>
			                <td>
			                	<br/><br/>
			                	<select id="addCouponTypeForm_scopeType" name="scopeType">
			                		<option value="-1">-请选择-</option>
			                		<option value="1">全场商品</option>
			                		<!-- <option value="2">通用专场商品</option>-->
			                		<option value="3">指定商品</option>
			                		<!--<option value="4">二级类目商品</option>
			                		<option value="5">卖家商品</option>
			                		<option value="6">卖家发货类型商品</option> -->
			                	</select>
			                </td>
			            </tr>
			            <tr id="displayScopeId1" style="display:none">
			            	<td>通用专场商品：</td>
			            	<td>
			            		<input id="commonId" style="width:300px" name="scopeId" value=""/><br/><br/>
			            	</td>
			            </tr>
			            <tr id="displayScopeId2" style="display:none">
			            	<td>指定商品：</td>
			            	<td>
			            		<input id="productId" style="width:300px" name="scopeId" value=""/><br/><br/>
			            	</td>
			            </tr>
			            <tr id="displayProductName" style="display:none">
			            	<td>商品名称:</td>
			            	<td><font color="red" id="productName"></font></td>
			            </tr>
			            <tr id="displayScopeId3" style="display:none">
			            	<td>二级类目商品：</td>
			            	<td>
			            		<input id="secondCategoryId" style="width:300px" name="scopeId" value=""/><br/><br/>
			            	</td>
			            </tr>
			            <tr id="displayScopeId4" style="display:none">
			            	<td>卖家商品：</td>
			            	<td>
			            		<input id="sellerId" style="width:300px" name="scopeId" value=""/><br/><br/>
			            	</td>
			            </tr>
			            <tr id="displayScopeId5" style="display:none">
			            	<td>卖家发货类型商品：</td>
			            	<td>
			            		<input id="sellerSendType" style="width:300px" name="scopeId" value=""/><br/><br/>
			            	</td>
			            </tr>
			            <tr>
			            	<td>APP显示使用范围：</td>
			            	<td>
			            		<input id="userScope" name="desc" style="width: 300px;" maxlength="40"/>&nbsp;<font>注：前台展示用</font>
			            	</td>
			            </tr>
			        </table>
		    	</form>
		    </div>
		</div>
	</div>
</div>
<script>
$(function(){
	$(document).keydown(function(e){
		if (!e){
		   e = window.event;  
		}  
		if ((e.keyCode || e.which) == 13) {  
		      $("#searchBtn").click();  
		}
	});
});

function searchCouponDetail(){
	$('#s_data').datagrid('load',{
    	type:$("#searchCouponType").val(),
    	scope:$("input[name='searchCouponScope']").val(),
    	isAvailable:$("#searchIsAvailable").val()
	});
}

function clearCouponDetail(){
	$("#searchCouponType").find('option').eq(0).attr('selected','selected');
	$("#searchIsAvailable").find('option').eq(0).attr('selected','selected');
	$("#searchCouponScope").combobox('clear');
	$('#s_data').datagrid('load',{});
}


function updateStatus(id, isAvailable){
	var confirm = '';
	if(isAvailable == 0){
		confirm = '确定要启用吗？';
	}else{
		confirm = '确定要停用吗！';
	}
	$.messager.confirm('提示',confirm,function(r){
	    if (r){
	    	$.messager.progress();
			$.ajax({
		           url: '${rc.contextPath}/coupon/updateStatus',
		           type: 'post',
		           dataType: 'json',
		           data: {'id':id,'isAvailable':isAvailable},
		           success: function(data){
		           	$.messager.progress('close');
		               if(data.status == 1){
		               	$('#s_data').datagrid('load');
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
}

function cleanForm(){
	$("#addCouponTypeForm_id").val("");
	$("#addCouponTypeForm_threshold").val("");
	$("#addCouponTypeForm_reduce1").val("");
	$("#addCouponTypeForm_reduce2").val("");
	$("#addCouponTypeForm_lowestReduce3").val('');
	$('#addCouponTypeForm_maximalReduce3').val('');
	$("#addCouponTypeForm_threshold4").val("");
	$("#addCouponTypeForm_lowestReduce4").val('');
	$('#addCouponTypeForm_maximalReduce4').val('');
	$("#addCouponTypeForm_type1").prop("checked", false);
	$("#addCouponTypeForm_type2").prop("checked", false);
	$("#addCouponTypeForm_type3").prop("checked", false);
	$("#addCouponTypeForm_type4").prop("checked", false);
	$("#addCouponTypeForm_scopeType").find('option').eq(0).attr('selected','selected');
	$("#productName").html('');
	$("#displayProductName").css('display','none');
	$("#userScope").val('');
	$("tr[id^='displayScopeId']").each(function(){
		$(this).find('td').eq(1).find('input').val('');
	});
	$("tr[id^='displayScopeId']").each(function(){
		$(this).css('display','none');
	});
}

function modifyDetail(index){
    var arr=$("#s_data").datagrid("getData");
    var id = arr.rows[index].id;
    var type = arr.rows[index].type;
    var scopeType = arr.rows[index].scopeType;
    var scopeId = arr.rows[index].scopeId;
    var threshold = arr.rows[index].threshold;
    var reduce = arr.rows[index].reduce;
    var desc = arr.rows[index].desc;
    var lowestReduce = arr.rows[index].lowestReduce;
    var maximalReduce = arr.rows[index].maximalReduce;
    $.ajax({
		type:'post',
		url:'${rc.contextPath}/coupon/checkIsInUse',
		data:{'id':id},
		dataType:'json',
		success:function(data){
			if(data.status==1){
				$("#addCouponTypeForm_id").val(id);
				if(type==1){
					$("#addCouponTypeForm_type1").prop("checked", true);
					$("#addCouponTypeForm_threshold").val(threshold);
					$("#addCouponTypeForm_reduce1").val(reduce);
				}else if(type==2){
					$("#addCouponTypeForm_type2").prop("checked", true);
					$("#addCouponTypeForm_reduce2").val(reduce);
				}else if(type == 3){
					$("#addCouponTypeForm_type3").prop("checked", true);
					$("#addCouponTypeForm_lowestReduce3").val(lowestReduce);
					$('#addCouponTypeForm_maximalReduce3').val(maximalReduce);
				}else if(type == 4){
					$("#addCouponTypeForm_type4").prop("checked", true);
					$("#addCouponTypeForm_threshold4").val(threshold);
					$("#addCouponTypeForm_lowestReduce4").val(lowestReduce);
					$('#addCouponTypeForm_maximalReduce4').val(maximalReduce);
				}
				if(scopeType==2){
					$("#displayScopeId1").css('display','');
					$("#commonId").combobox('reload','${rc.contextPath}/banner/jsonAcCommonCode?id='+scopeId);
				}
				if(scopeType==3){
					$("#displayScopeId2").css('display','');
					$("#productId").val(scopeId);
					$("#displayProductName").css('display','');
					$("#productName").html(data.productName);
				}
				if(scopeType==4){
					$("#displayScopeId3").css('display','');
					$("#secondCategoryId").combobox('reload','${rc.contextPath}/category/jsonCategorySecondCode?id='+scopeId);
				}
				if(scopeType==5){
					$("#displayScopeId4").css('display','');
					$("#sellerId").combobox('reload','${rc.contextPath}/seller/jsonSellerCode?isAvailable=1&id='+scopeId);
				}
				if(scopeType==6){
					$("#displayScopeId5").css('display','');
					$("#sellerSendType").combobox('reload','${rc.contextPath}/seller/jsonSellerType?code='+scopeId);
				}
				$("#addCouponTypeForm_scopeType").find("option[value='"+scopeType+"']").attr("selected",true);
				$("#userScope").val(desc);
			    $("#addCouponTypeDiv").dialog('open');
			}else{
				$.messager.alert("提示",data.msg,"info");
			}
		},
		error:function(data){
			$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		}
	});
}

$(function(){
	
	$("#addCouponTypeForm_type1").change(function(){
		if($(this).is(":checked")){
			$("#addCouponTypeForm_reduce2").val('');
			$("#addCouponTypeForm_lowestReduce3").val('');
			$('#addCouponTypeForm_maximalReduce3').val('');
			$("#addCouponTypeForm_threshold4").val('');
			$("#addCouponTypeForm_lowestReduce4").val('');
			$('#addCouponTypeForm_maximalReduce4').val('');
		}
	});
	
	$("#addCouponTypeForm_type2").change(function(){
		if($(this).is(":checked")){
			$("#addCouponTypeForm_threshold").val('');
			$("#addCouponTypeForm_reduce1").val('');
			$("#addCouponTypeForm_lowestReduce3").val('');
			$('#addCouponTypeForm_maximalReduce3').val('');
			$("#addCouponTypeForm_threshold4").val('');
			$("#addCouponTypeForm_lowestReduce4").val('');
			$('#addCouponTypeForm_maximalReduce4').val('');
		}
	});
	
	$("#addCouponTypeForm_type3").change(function(){
		if($(this).is(":checked")){
			$("#addCouponTypeForm_threshold").val('');
			$("#addCouponTypeForm_reduce1").val('');
			$("#addCouponTypeForm_reduce2").val('');
			$("#addCouponTypeForm_threshold4").val('');
			$("#addCouponTypeForm_lowestReduce4").val('');
			$('#addCouponTypeForm_maximalReduce4').val('');
		}
	});
	
	$("#addCouponTypeForm_type4").change(function(){
		if($(this).is(":checked")){
			$("#addCouponTypeForm_threshold").val('');
			$("#addCouponTypeForm_reduce1").val('');
			$("#addCouponTypeForm_reduce2").val('');
			$("#addCouponTypeForm_lowestReduce3").val('');
			$('#addCouponTypeForm_maximalReduce3').val('');
		}
	});
	
	$("#searchCouponScope").combobox({
		url:'${rc.contextPath}/coupon/jsonScopeTypeCode', 
	    valueField:'code',   
	    textField:'text'
	});
	
	$("#commonId").combobox({
		url:'${rc.contextPath}/banner/jsonAcCommonCode',   
	    valueField:'code',   
	    textField:'text' 
	});
	
	$("#secondCategoryId").combobox({
		url:'${rc.contextPath}/category/jsonCategorySecondCode',   
	    valueField:'code',   
	    textField:'text' 
	});
	
	$("#sellerId").combobox({
		url:'${rc.contextPath}/seller/jsonSellerCode?isAvailable=1',   
	    valueField:'code',   
	    textField:'text'
	});
	
	$("#sellerSendType").combobox({
		url:'${rc.contextPath}/seller/jsonSellerType',   
	    valueField:'code',   
	    textField:'text'
	});
	
	$('#addCouponTypeForm_scopeType').change(function(){
		var scopeType = $(this).val();
		if(scopeType ==1 || scopeType==-1){
			$("tr[id^='displayScopeId']").each(function(){
				$(this).css('display','none');
			});
			$("input[name='scopeId']").each(function(){
				$(this).prop("disabled", true);
			});
			$("#displayProductName").css('display','none');
		}else if(scopeType ==2){
			$("tr[id^='displayScopeId']").each(function(){
				$(this).css('display','none');
			});
			$("input[name='scopeId']").each(function(){
				$(this).prop("disabled", true);
			});
			$("#commonId").parent().parent().css('display','');
			$("#commonId").prop("disabled", false);
			$("#displayProductName").css('display','none');
		}else if(scopeType ==3){
			$("tr[id^='displayScopeId']").each(function(){
				$(this).css('display','none');
			});
			$("input[name='scopeId']").each(function(){
				$(this).prop("disabled", true);
			});
			$("#productId").parent().parent().css('display','');
			$("#productId").prop("disabled", false);
		}else if(scopeType ==4){
			$("tr[id^='displayScopeId']").each(function(){
				$(this).css('display','none');
			});
			$("input[name='scopeId']").each(function(){
				$(this).prop("disabled", true);
			});
			$("#secondCategoryId").parent().parent().css('display','');
			$("#secondCategoryId").prop("disabled", false);
			$("#displayProductName").css('display','none');
		}else if(scopeType==5){
			$("tr[id^='displayScopeId']").each(function(){
				$(this).css('display','none');
			});
			$("input[name='scopeId']").each(function(){
				$(this).prop("disabled", true);
			});
			$("#sellerId").parent().parent().css('display','');
			$("#sellerId").prop("disabled", false);
			$("#displayProductName").css('display','none');
		}else if(scopeType==6){
			$("tr[id^='displayScopeId']").each(function(){
				$(this).css('display','none');
			});
			$("input[name='scopeId']").each(function(){
				$(this).prop("disabled", true);
			});
			$("#sellerSendType").parent().parent().css('display','');
			$("#sellerSendType").prop("disabled", false);
			$("#displayProductName").css('display','none');
		}
	});
	
	$("#productId").keyup(function(){
		var productId = $.trim($(this).val());
		if(productId != ''){
			$.ajax({
				type:'post',
				url:'${rc.contextPath}/coupon/showScopeTypeName',
				data:{'scopeId':productId},
				dataType:'json',
				success:function(data){
					$("#displayProductName").css('display','');
					$("#productName").html(data.msg);
					if(data.status !=1){
						$(this).val('');
					}
				},
				error: function(xhr){
		        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		       }
			});
		}
	});

	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/coupon/jsonCouponDetailInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
            {field:'index',    title:'ID', width:10, align:'center'},
            {field:'typeStr',     title:'类型',  width:50,  align:'center'},
            {field:'status',     title:'使用状态',  width:30,  align:'center'},
            {field:'scope',    title:'优惠券商品使用范围', width:80, align:'center'},
            {field:'desc',    title:'APP显示使用范围', width:80, align:'center'},
            {field:'type', hidden:true},
            {field:'scopeType', hidden:true},
            {field:'scopeId', hidden:true},
            {field:'threshold', hidden:true},
            {field:'reduce',   hidden:true},
            {field:'hidden',  title:'操作', width:20,align:'center',
                formatter:function(value,row,index){
                    var a = '<a href="javaScript:;" onclick="modifyDetail(' + index + ')">编辑</a>';
                    var b = '';
                    if(row.isAvailable==1){
                    	b = ' | <a href="javaScript:;" onclick="updateStatus(' + row.id + ',' + row.isAvailable + ')">停用</a>';
                    }
                    if(row.isAvailable==0){
                    	b = ' | <a href="javaScript:;" onclick="updateStatus(' + row.id + ',' + row.isAvailable + ')">启用</a>';
                    }
                    return a + b;
                }
            }
        ]],
        toolbar:[
        {
            id:'_add',
            text:'新增分类',
            iconCls:'icon-add',
            handler:function(){
            	cleanForm();
            	$('#addCouponTypeDiv').dialog('open');
            }
        }],
     	pagination:true
    });
    
    $('#addCouponTypeDiv').dialog({
    	title:'优惠券类型',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#addCouponTypeForm').form('submit',{
    				url: "${rc.contextPath}/coupon/saveOrUpdate",
    				onSubmit:function(){
    					var type = $("input[name='type']:checked").val();
    					var threshold;
    					var reduce;
    					var lowestReduce;
    					var maximalReduce;
    					var scopeType = $("#addCouponTypeForm_scopeType").val();
    					var desc = $("#userScope").val();
    					if(type == '' || type==null || type==undefined){
    						$.messager.alert("info","请选择类型","warn");
    						return false;
    					}else{
    						if(type==1){
    							threshold = $("input[name='threshold']").val();
    							reduce = $("input[name='reduce1']").val();
    							if($.trim(threshold)=='' || $.trim(reduce)==''){
    								$.messager.alert("info","请输入优惠信息","warn");
    								return false;
    							}else{
    								if(!/^[1-9]+\d*$/.test(threshold) || !/^[1-9]+\d*$/.test(reduce)){
    									$.messager.alert("info","优惠信息只能为正整数","warn");
        								return false;
    								}
    							}
    						}else if(type==2){
    							reduce = $("input[name='reduce2']").val();
    							if($.trim(reduce)==''){
    								$.messager.alert("info","请输入优惠信息","warn");
    								return false;
    							}else{
    								if(!/^[1-9]+\d*$/.test(reduce)){
    									$.messager.alert("info","优惠信息只能为正整数","warn");
        								return false;
    								}
    							}
    						}else if(type == 3){
    							maximalReduce = $.trim($("input[name='maximalReduce3']").val());
    							lowestReduce = $.trim($("input[name='lowestReduce3']").val());
    							if(maximalReduce=='' || lowestReduce==''){
    								$.messager.alert("info","请输入优惠信息","warn");
    								return false;
    							}else{
    								if(!/^[1-9]+\d*$/.test(maximalReduce) || !/^[1-9]+\d*$/.test(lowestReduce)){
    									$.messager.alert("info","优惠信息只能为正整数","warn");
        								return false;
    								}else if(parseInt(lowestReduce) >= parseInt(maximalReduce)){
    									$.messager.alert("info","随机现金券最低金额不得高于最高金额","warn");
        								return false;
    								}
    							}
    						}else if(type == 4){
    							maximalReduce = $.trim($("input[name='maximalReduce4']").val());
    							lowestReduce = $.trim($("input[name='lowestReduce4']").val());
    							threshold = $.trim($("input[name='threshold4']").val());
    							if(maximalReduce=='' || lowestReduce=='' || threshold==''){
    								$.messager.alert("info","请输入优惠信息","warn");
    								return false;
    							}else{
    								if(!/^[1-9]+\d*$/.test(threshold) || !/^[1-9]+\d*$/.test(maximalReduce) || !/^[1-9]+\d*$/.test(lowestReduce)){
    									$.messager.alert("info","优惠信息只能为正整数","warn");
        								return false;
    								}else if(parseInt(lowestReduce) >= parseInt(maximalReduce)){
    									$.messager.alert("info","随机优惠券最低减免金额不得高于最高减免金额","warn");
        								return false;
    								}else if(parseInt(maximalReduce) >= parseInt(threshold)){
    									$.messager.alert("info","随机优惠券最高减免金额不得高于满减金额","warn");
        								return false;
    								}
    							}
    						}
    					}
    					if(scopeType=="-1"){
    						$.messager.alert("info","请选择使用范围","warn");
							return false;
    					}else if(scopeType=="2"){
   							var commonId = $.trim($("#commonId").combobox('getValue'));
   							if(commonId =='' || commonId == '0'){
   								$.messager.alert("info","请选择专场","warn");
   								return false;
   							}
   						}else if(scopeType=="3"){
   							var productId = $.trim($("#productId").val());
   							if(productId==''){
   								$.messager.alert("info","请输入商品ID","warn");
   								return false;
   							}else{
   								if(!/^[1-9]+\d*$/.test(productId)){
   									$.messager.alert("info","商品ID必须是不以零开头的整数","warn");
       								return false;
   								}
   							}
   						}else if(scopeType=="4"){
   							var secondCategoryId = $.trim($("#secondCategoryId").combobox('getValue'));
   							if(secondCategoryId==''){
   								$.messager.alert("info","请选择二级类目","warn");
   								return false;
   							}
   						}else if(scopeType=="5"){
   							var sellerId = $.trim($("#sellerId").combobox('getValue'));
   							if(sellerId == ''){
   								$.messager.alert("info","请选择卖家","warn");
   								return false;
   							}
   						}else if(scopeType=='6'){
   							var sellerSendType = $.trim($("#sellerSendType").combobox('getValue'));
   							if(sellerSendType == ''){
   								$.messager.alert("info","请选择卖家","warn");
   								return false;
   							}
   						}
    					if($.trim(desc)==''){
    						$.messager.alert("info","请输入用户使用范围","warn");
							return false;
    					}
    					$.messager.progress();
    				},
    				success: function(data){
    					$.messager.progress('close');
                        var res = eval("("+data+")");
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                                $('#s_data').datagrid('reload');
                                $('#addCouponTypeDiv').dialog('close');
                            });
                        } else if(res.status == 0){
                            $.messager.alert('响应信息',res.msg,'info');
                        } 
    				}
    			});
    		}
    	},
    	{
    		text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#addCouponTypeDiv').dialog('close');
            }
    	}]
    });     
});
</script>

</body>
</html>