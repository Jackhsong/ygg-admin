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
.searchName{
	padding-right:10px;
	/* text-align:right; */
	text-align:justify;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
.inputStyle{
	width:250px;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'优惠码管理',split:true" style="height: 100px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td>&nbsp;优惠码：</td>
						<td><input name="searchCode" /></td> 
						<td>&nbsp;&nbsp;备注：</td>
						<td><input name="searchRemark" /></td>
						<td>&nbsp;&nbsp;状态：</td>
						<td>
							<select name="searchIsAvailable" id="searchIsAvailable">
								<option value="-1">全部</option>
								<option value="1">可用</option>
								<option value="0">停用</option>
							</select>
						<td>&nbsp;类型：</td>
						<td>
							<select name="searchType" id="searchType">
								<option value="0">全部</option>
								<option value="1">单次</option>
								<option value="2">多次</option>
							</select>
						</td>
						<td>&nbsp;<a id="searchBtn" onclick="searchCouponCode()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
						<td></td>
					</tr>
				</table>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
    		<!-- dialog begin -->
			<div id="addCodeDiv" class="easyui-dialog" style="width:700px;height:300px;padding:5px 5px;">
				<form id="addCodeDivForm" method="post">
					<table cellpadding="5">
						<tr>
							<td class="searchName">优惠券类型：</td>
							<td class="searchText">
								<input id="couponDetail" type="text" style="width:450px" name="couponDetail" />
								<span>多选组成礼包</span>
							</td>
						</tr>
						<tr id="setCouponDetailCount" style="display:none">
							<td colspan="2" class="searchText">
								<table id= "setCouponDetailCountTable" width="90%" align="center" border="1" cellpadding="10px" cellspacing="0px">
									<tr>
										<td width="90%"><span></span></td>
										<td width="10%">
											<input type="hidden" name="couponDetailId">
											<input name="couponDetailCount" onchange="checkCount(this)">
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td class="searchName">有效期：</td>
							<td class="searchText">
								开始：<input id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>&nbsp;&nbsp;&nbsp;&nbsp;
								结束：<input id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"/>
							</td>
						</tr>
						<tr>
							<td class="searchName">备注：</td>
							<td class="searchText"><input class="inputStyle" name="remark" /></td>
						</tr>
						<tr>
							<td class="searchName">限用人数：</td>
							<td class="searchText">
								<input type="radio" name="type" id="type_one" value="1" checked>单次
								<input type="radio" name="type" id="type_many" value="2">不限制
							</td>
						</tr>
						<tr>
							<td class="searchName">生成数量：</td>
							<td class="searchText"><input class="inputStyle" name="nums"/></td>
						</tr>
						<tr id="customCodeTr">
							<td class="searchName">自定义优惠码：</td>
							<td class="searchText"><input class="inputStyle" name="customCode" /><span>&nbsp;注：选填，最多15个字</span></td>
						</tr>
					</table>
				</form>
			</div>
			<!-- dialog end -->
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

	function searchCouponCode() {
		$('#s_data').datagrid('load', {
			code : $("#searchForm input[name='searchCode']").val(),
			remark : $("#searchForm input[name='searchRemark']").val(),
			isAvailable : $("#searchIsAvailable").val(),
			type : $("#searchType").val()
		});
	}
	//修改优惠码可用不可用状态
	function available(id,isAvailable){
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
				       url: '${rc.contextPath}/couponCode/available',
				       type: 'post',
				       dataType: 'json',
				       data: {'id':id,'isAvailable':isAvailable},
				       success: function(data){
				         $.messager.progress('close');
				           if(data.status == 1){
				            	$('#s_data') .datagrid("load");
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
	}
	//查看详情
	function showDetail(id,type){
		var url = "${rc.contextPath}/couponCode/couponCodeDetail/"+type+"/"+id;
		window.open(url,'_blank');
	}
	
	//查看礼包详情
	function showLiBaoDetail(id,type){
		var url = "${rc.contextPath}/couponCode/couponCodeLiBaoDetail/"+type+"/"+id;
		window.open(url,'_blank');
	}
	
	//检查优惠券数量
	function checkCount(obj){
		var currCount = $(obj).val();
		if(!/^[1-9]+\d*$/.test(currCount)){
			$.messager.alert("提示",'请输入正整数',"info");
			$(obj).val("1");
		}
	}
	
	$(function(){
		//integral dialog  begin
		$('#addCodeDiv').dialog({
			title:'生成优惠码',
			collapsible:true,
			closed:true,
			modal:true,
			buttons:[{
			    text:'保存',
			    iconCls:'icon-ok',
			    handler:function(){
			    	var params = {};
			    	var countValidate = true;
			    	params.couponDetail = $('#couponDetail').combobox('getValues').toString();
					params.mIdAndCount = "";
			    	params.startTime = $("#addCodeDivForm input[name=startTime]").val();
			    	params.endTime = $("#addCodeDivForm input[name=endTime]").val();
			    	params.remark = $.trim($("#addCodeDivForm input[name=remark]").val());
			    	params.type = $("input[name='type']:checked").val();
			    	params.nums = $("#addCodeDivForm input[name=nums]").val();
			    	params.customCode = $.trim($("#addCodeDivForm input[name=customCode]").val());
			    	//统计优惠券表格数据
			    	var mIds = $("input[name='couponDetailId']");
					var mCounts = $("input[name='couponDetailCount']");
					$.each(mIds,function(i,item){
						var id = $(mIds[i]).val();
						var count = $(mCounts[i]).val();
						params.mIdAndCount += (id+":"+count+",");
					});
					$.each(mCounts,function(i,item){
						var count = $(mCounts[i]).val();
						if(count > 5){
							countValidate = false;
							return false;
						}
					});
					console.log(params.couponDetail);
			    	if(params.couponDetail == '' || typeof(params.couponDetail) == "undefined"){
			    		$.messager.alert("提示","不支持的优惠券类型，请在下拉列表中选择支持的优惠券类型","warning");
			    	}
			    	else if(params.mIdAndCount == '' || typeof(params.mIdAndCount) == "undefined"){
			    		$.messager.alert("提示","不支持的优惠券类型，请在下拉列表中选择支持的优惠券类型","warning");
			    	}
			    	else if(params.startTime == '' || params.endTime == ''){
			    		$.messager.alert("提示","开始时间和结束时间必填","warning");
			    	}else if(!/^[1-9]+\d*$/.test(params.nums)){
			    		$.messager.alert("提示","生成数量必须是不以零开头的整数","warning");
			    	}else if(params.type == '2' && params.customCode.length > 15){
			    		$.messager.alert("提示","自定义优惠码长度不得超过15个字符","warning");
			    	}else if(!countValidate){
			    		$.messager.alert("提示","每个优惠券类型数量最多不得超过5个","warning");
			    	}
			    	else{
			    		var sec = parseInt(params.nums) / 100;
			    		$.messager.alert("提示","生成"+params.nums+"个优惠码耗时约"+sec+"秒","info",function(){
			    			$.messager.progress();
			    			$.ajax({
								url: '${rc.contextPath}/couponCode/addCode',
								type: 'post',
								dataType: 'json',
								data: params,
								success: function(data){
									$.messager.progress('close');
									if(data.status == 1){
										$.messager.alert("提示","创建成功","info",function(){
											$('#s_data').datagrid("load");
											$('#addCodeDiv').dialog('close');
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
			    		});
			    	}
			    }
			},{
			    text:'取消',
			    align:'left',
			    iconCls:'icon-cancel',
			    handler:function(){
			        $('#addCodeDiv').dialog('close');
			    }
			}]
	     });
		//integral dialog end
		
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        idField:'id',
	        url:'${rc.contextPath}/couponCode/jsonCouponCode',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        singleSelect:true,
	        pageSize:50,
	        pageList:[50,100],
	        columns:[[
	            {field:'id',    title:'ID', width:20, align:'center'},
	            {field:'isAvailableStr',    title:'状态', width:20, align:'center'},
	            {field:'createTime',    title:'生成时间', width:40, align:'center'},
	            {field:'typeStr',    title:'类型', width:40, align:'center'},
	            {field:'code',    title:'优惠码', width:30, align:'center'},
	            {field:'couponDetailTypeStr',     title:'优惠券类型',  width:40,   align:'center' },
	            {field:'couponDetailDesc',     title:'使用范围（用户可见）',  width:40,   align:'center' },
	            {field:'total',    title:'优惠码总数', width:20, align:'center'},
	            {field:'desc',     title:'备注',  width:40,   align:'center' },
	            {field:'startTime',    title:'有效期起', width:40, align:'center'},
	            {field:'endTime',    title:'有效期止', width:40, align:'center'},
	            {field:'convertNums',    title:'兑换人数', width:20, align:'center'},
	            {field:'hidden0',    title:'使用人数', width:30, align:'center',
	            	formatter:function(value,row,index){
	                    var a = '--';
	                    if(row.changeType == 1){
	                    	a = row.usedNums;
	                    }
	                    return a;
	                }
	            },
	            {field:'hidden',  title:'操作', width:50,align:'center',
	                formatter:function(value,row,index){
	                    var a = '';
	                    if(row.changeType == 1){
	                    	a = '<a href="javaScript:;" onclick="showDetail(' + row.id + ',' + row.type + ')">查看</a>';	                    	
	                    }else{
	                    	a = '<a href="javaScript:;" onclick="showLiBaoDetail(' + row.id + ',' + row.type + ')">查看</a>';	
	                    }
	                    var b = '';
	                    if(row.isAvailable == 0){
	                    	b = ' | <a href="javaScript:;" onclick="available(' + row.id + ',' + row.isAvailable + ')">设为可用</a>';
	                    }else{
	                    	b = ' | <a href="javaScript:;" onclick="available(' + row.id + ',' + row.isAvailable + ')">设为停用</a>';
	                    }
	                    return a + b;
	                }
	            }
	        ]],
	        toolbar:[{
                id:'_add',
                text:'新增优惠码',
                iconCls:'icon-add',
                handler:function(){
                	$("#setCouponDetailCount").hide();
                	$('#couponDetail').combobox("clear");
			    	$("#addCodeDivForm input[name=startTime]").val("");
			    	$("#addCodeDivForm input[name=endTime]").val("");
			    	$("#addCodeDivForm input[name=remark]").val("");
			    	$("#addCodeDivForm input[name=nums]").val("");
			    	$("#addCodeDivForm input[name=customCode]").val("");
			    	$('#type_one').prop("checked",true);
			    	$('#type_one').change();
			    	$('#addCodeDiv').dialog('resize',{
			    		width: 700,
			    		height: 300
			    	});
                	$('#addCodeDiv').dialog('open');
                }
            }],
	        pagination:true
	    });
		
		$('#couponDetail').combobox({
			panelWidth:500,
			panelHeight:500,
			multiple:true,
		    url:'${rc.contextPath}/coupon/jsonCouponType?isRandomReduce=1',   
		    valueField:'code',   
		    textField:'text',
		    onHidePanel:function(){
		    	var tableObj = $("#setCouponDetailCountTable");
		    	var obj = $(tableObj).find("tr:first");
		    	$(tableObj).find("tr").remove();
		    	$(tableObj).append(obj);
		    	var vs = $('#couponDetail').combobox('getValues').toString();
		    	if(vs == ''){
		    		$("#setCouponDetailCount").hide();
		    		$('#addCodeDiv').dialog('resize',{
			    		width: 700,
			    		height: 300
			    	});
		    		return;
		    	}
		    	var dataList =  $('#couponDetail').combobox('getData');
		    	if(vs.indexOf(',') == -1){
		    		cdStr = $('#couponDetail').combobox('getText');
		    		//$.messager.alert('info',"您选择了一张优惠券："+cdStr,'info');
		    		//$("#setCouponDetailCount").
		    		$(obj).find("span").text(cdStr);
		    		$(obj).find("input[name='couponDetailId']").val(vs);
		    		$(obj).find("input[name='couponDetailCount']").val("1");
		    		$('#addCodeDiv').dialog('resize',{
			    		width: 700,
			    		height: 350
			    	});
		    	}else{
		    		var arr = vs.split(",");
		    		var currObj;
		    		//$.messager.alert('info',"您共选择了"+arr.length+"张优惠券",'info');
		    		//console.log(arr);
		    		for (var i=0; i<arr.length; i++)
					{
		    			var currCode = arr[i];
		    			var cdStr = '';
		    			for (var j=0; j<dataList.length; j++)
						{
		    				if(dataList[j].code == currCode){
		    					cdStr = dataList[j].text;
		    				}
						}
		    			var options = $('#couponDetail').combobox('options');
		    			if(i==0){
		    				currObj = obj;
		    			}else{
		    				currObj = $(obj).clone();
			    			$(tableObj).append(currObj);
		    			}
		    			$(currObj).find("span").text(cdStr);
			    		$(currObj).find("input[name='couponDetailId']").val(currCode);
			    		$(currObj).find("input[name='couponDetailCount']").val("1");
					}
		    		$('#addCodeDiv').dialog('resize',{
			    		width: 700,
			    		height: 300 + 50 * arr.length
			    	});
		    	}
		    	$("#setCouponDetailCount").show();
		    }
		});
		
		$('#type_one').change(function(){
			if($('#type_one').is(':checked')){
				$("#addCodeDivForm input[name=nums]").removeAttr("readonly");
				var num = $("#addCodeDivForm input[name=nums]").val();
				if(num == '1'){
					$("#addCodeDivForm input[name=nums]").val("");
				}
				$('#customCodeTr').hide();
			}
		});
		$('#type_many').change(function(){
			if($('#type_many').is(':checked')){
				$("#addCodeDivForm input[name=nums]").attr("readonly","readonly");
				$("#addCodeDivForm input[name=nums]").val(1);
				$('#customCodeTr').show();
			}
		});
		$('#type_one').change();
	});
</script>
</body>
</html>