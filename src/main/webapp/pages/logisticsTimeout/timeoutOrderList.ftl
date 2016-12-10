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
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1340px;
	align:center;
}

textarea{
	resize:none
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
			<div data-options="region:'north',title:'条件筛选-订单管理',split:true" style="height: 190px;">
				<form action="${rc.contextPath}/logisticsTimeout/exportLogisticsOrder" id="searchForm" method="post" >
						<input type="hidden" id="startTime" name="startTime" value="${startTime!''}"/>
						<input type="hidden" id="endTime" name="endTime" value="${endTime!''}"/>
						<input type="hidden" id="sellerId" name="sellerId" value="${sellerId!'0'}"/>
						<input type="hidden" id="orderIdList" name="orderIdList" value="${orderIdList!''}"/>
						<table class="search">
							<tr>
								<td class="searchName">订单编号：</td>
								<td class="searchText"><input id="orderNumber" name="orderNumber" value="" /></td>
								<td class="searchName">订单状态：</td>
								<td class="searchText">
									<select id="orderStatus" name="orderStatus" ">
                                        <option value="0">全部</option>
                                        <option value="1">未付款</option>
                                        <option value="2">待发货</option>
                                        <option value="3">已发货</option>
                                        <option value="4">交易成功</option>
                                        <option value="5">用户取消</option>
                                        <option value="6">超时取消</option>
                                    </select>
								</td>
								<td class="searchName">收货人姓名：</td>
								<td class="searchText"><input id="receiveName" name="receiveName" value="" /></td>
							</tr>
							<tr>
                                <td class="searchName">商品编码：</td>
                                <td class="searchText"><input id="code" name="code" value="" " /></td>
                                <td class="searchName">收货手机号</td>
                                <td class="searchText"><input id="reveivePhone" name="reveivePhone" value="" " /></td>
                                <td class="searchName">商品名称：</td>
                                <td class="searchText"><input id="productName" name="productName" value="" /></td>
							</tr>
							<tr>
								<input type=hidden id="exportType" name="exportType" value="" />
								<td class="searchName">是否超时：</td>
                                <td class="searchText">
									<select id="isTimeout" name="isTimeout">
										<option value="-1" selected="selected">--全部--</option>
										<option value="1">--是--</option>
										<option value="0">--否--</option>
									</select>
                                </td>
                                <td class="searchName"></td>
                                <td class="searchText">
								<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
								<a id="exportResult" onclick="exportResult()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出结果</a>&nbsp;
								<#--<a id="exportSellerAllStatus" onclick="exportSellerAllStatus()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出订单明细</a>&nbsp;						-->
                                </td>
                                <td class="searchName"></td>
                                <td class="searchText"></td>
							</tr>
						</table>
					</form>
			</div>
			<div data-options="region:'center'" >
				<table id="s_data">

				</table>

				<!-- 申诉明细 begin -->
				<div id="complainDetail_div" style="width:600px;height:700px;padding:20px 20px;">

				</div>
				<!-- 申诉明细 end -->

	        	<!-- QC处理 begin -->
				<div id="qcDealDiv" class="easyui-dialog" style="width:600px;height:400px;padding:20px 20px;">
	            	<form id="qcDealForm" method="post">
	            		<input id="qcDealForm_orderId" type="hidden" name="orderId" />
	            		<p>
	            			订单编号：<span id="qcDealForm_orderNumber"></span>
	            		</p>
	            		<p>
	            			商家名称：<span id="qcDealForm_sellerName"></span>
	            		</p>
	            		<p>
	            			超时原因：<input type="text" id="qcDealForm_reason" name="reasonId" style="width: 400px;"/>
	            		</p>
	            		<p>
	            			&nbsp;&nbsp;说明：<textarea id="qcDealForm_remark" name="remark" rows="5" cols="60"></textarea>
	            		</p>
	            		<p>
	            			处理结果：<input type="text" id="qcDealForm_result" name="result" maxlength="100" style="width: 400px;"/>
	            		</p>
	        		</form>
	        	</div>
	        	<!-- QC处理 end -->

	        	<!-- QC批量处理 begin -->
				<div id="qcBatchDealDiv" class="easyui-dialog" style="width:600px;height:350px;padding:20px 20px;">
	            	<form id="qcBatchDealForm" method="post">
	            		<input id="qcBatchDealForm_orderIds" type="hidden" name="orderIds" />
	            		<p>
	            			超时原因：<input type="text" id="qcBatchDealForm_reason" name="reasonId" style="width: 400px;"/>
	            		</p>
	            		<p>
	            			&nbsp;&nbsp;说明：<textarea id="qcBatchDealForm_remark" name="remark" rows="5" cols="60"></textarea>
	            		</p>
	            		<p>
	            			处理结果：<input type="text" id="qcBatchDealForm_result" name="result" maxlength="100" style="width: 400px;"/>
	            		</p>
	        		</form>
	        	</div>
	        	<!-- QC批量处理 end -->
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

		function searchOrder() {
			$('#s_data').datagrid('load', {
				orderNumber : $("#orderNumber").val(),
				orderStatus : $("#orderStatus").val(),
				productId : $("#productId").val(),
				productName : $("#productName").val(),
				sellerId : $("#sellerId").val(),
				receiveName : $("#receiveName").val(),
				reveivePhone : $("#reveivePhone").val(),
				startTime : $("#startTime").val(),
				endTime : $("#endTime").val(),
				code:$("#code").val(),
				orderIdList:$("#orderIdList").val(),
				isTimeout:$("#isTimeout").val()
			});
		}

		function exportResult() {
			$("#searchForm").submit();
		}

		function complainDetail(orderId){

			var url = "${rc.contextPath}/logisticsTimeout/complainDetail/" + parseInt(orderId);
	        $('#complainDetail_div').dialog('refresh', url);
	        $('#complainDetail_div').dialog('open');

		}

		function qcDeal(index){
			$('#qcDealForm_orderId').val('');
			$('#qcDealForm_reason').combobox('clear');
			$('#qcDealForm_remark').val('');
			$('#qcDealForm_result').val('');
			var arr=$("#s_data").datagrid("getData");
			$('#qcDealForm_orderId').val(arr.rows[index].orderId);
			$('#qcDealForm_orderNumber').text(arr.rows[index].number);
			$('#qcDealForm_sellerName').text(arr.rows[index].sSellerName);
			$('#qcDealDiv').dialog('open');
		}

        function cancelTimeout(id){
            $.messager.confirm('修改超时状态','确定设置为未超时吗？',function(b){
                if(b){
                    $.messager.progress();
                    $.ajax({
                        url: '${rc.contextPath}/logisticsTimeout/updateTimeOutStatus',
                        type: 'post',
                        dataType: 'json',
                        data: {'orderId':id,'isTimeout':0},
                        success: function(data){
                            $.messager.progress('close');
                            if(data.status == 1){
                                $('#s_data').datagrid('reload');
                                $.messager.alert("提示",data.message,"info");
                            }else{
                                $.messager.alert("提示",data.message,"info");
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

		$(function() {

			$("#qcDealForm_reason").combobox({
				url:'${rc.contextPath}/logisticsTimeout/jsonTimeoutReasonCode?isAvailable=1',
                valueField:'code',
                textField:'text'
            });

			$("#qcBatchDealForm_reason").combobox({
				url:'${rc.contextPath}/logisticsTimeout/jsonTimeoutReasonCode?isAvailable=1',
                valueField:'code',
                textField:'text'
            });

			$('#complainDetail_div').dialog({
	            title: '申诉明细',
	            closed: true,
	            href: '${rc.contextPath}/logisticsTimeout/complainDetail',
	            buttons:[{
	                text:'取消 ',
	                align:'left',
	                iconCls:'icon-cancel',
	                handler:function(){
	                	//$('#s_data').datagrid('reload');
	                    $('#complainDetail_div').dialog("close");
	                }
	            }]
	        });

			$('#qcDealDiv').dialog({
	            title:'QC处理',
	            collapsible:true,
	            closed:true,
	            modal:true,
	            buttons:[{
	                text:'处理',
	                iconCls:'icon-ok',
	                handler:function(){
	                    $('#qcDealForm').form('submit',{
	                        url:"${rc.contextPath}/logisticsTimeout/timeoutOrderQcDeal",
	                        onSubmit:function(){
	                        	var reasonId = $("#qcDealForm_reason").combobox('getValue');
	                        	var remark = $.trim($("#qcDealForm_remark").val());
	                        	var result = $.trim($("#qcDealForm_result").val());
	                        	if(reasonId == '' || reasonId == null || reasonId == undefined){
	                        		$.messager.alert('提示',"请选择超时原因",'error');
	                        		return false;
	                        	}else if(remark == ''){
	                        		$.messager.alert('提示',"请输入处理说明",'error');
	                        		return false;
	                        	}else if(remark.length>300){
	                        		$.messager.alert('提示',"处理说明字数请控制在300以内",'error');
	                        		return false;
	                        	}else if(result == ''){
	                        		$.messager.alert('提示',"请输入处理结果",'error');
	                        		return false;
	                        	}
	                        	$.messager.progress();
	                        },
	                        success:function(data){
	                        	$.messager.progress('close');
	                            var res = eval("("+data+")")
	                            if(res.status == 1){
	                                $('#qcDealDiv').dialog('close');
	                                $.messager.alert('响应信息',"处理成功",'info',function(){
	                                    $('#s_data').datagrid('reload');
	                                });
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
	                    $('#qcDealDiv').dialog('close');
	                }
	            }]
	        });

			$('#qcBatchDealDiv').dialog({
	            title:'QC批量处理',
	            collapsible:true,
	            closed:true,
	            modal:true,
	            buttons:[{
	                text:'处理',
	                iconCls:'icon-ok',
	                handler:function(){
	                    $('#qcBatchDealForm').form('submit',{
	                        url:"${rc.contextPath}/logisticsTimeout/timeoutOrderQcBatchDeal",
	                        onSubmit:function(){
	                        	var reasonId = $("#qcBatchDealForm_reason").combobox('getValue');
	                        	var remark = $.trim($("#qcBatchDealForm_remark").val());
	                        	var result = $.trim($("#qcBatchDealForm_result").val());
	                        	if(reasonId == '' || reasonId == null || reasonId == undefined){
	                        		$.messager.alert('提示',"请选择超时原因",'error');
	                        		return;
	                        	}else if(remark == ''){
	                        		$.messager.alert('提示',"请输入处理说明",'error');
	                        		return;
	                        	}else if(remark.length>300){
	                        		$.messager.alert('提示',"处理说明字数请控制在300以内",'error');
	                        		return;
	                        	}else if(result == ''){
	                        		$.messager.alert('提示',"请输入处理结果",'error');
	                        		return;
	                        	}
	                        	$.messager.progress();
	                        },
	                        success:function(data){
	                        	$.messager.progress('close');
	                            var res = eval("("+data+")")
	                            if(res.status == 1){
	                                $('#qcBatchDealDiv').dialog('close');
	                                $.messager.alert('响应信息',"处理成功",'info',function(){
	                                	$('#s_data').datagrid("clearSelections")
	                                    $('#s_data').datagrid('reload');
	                                });
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
	                    $('#qcBatchDealDiv').dialog('close');
	                }
	            }]
	        });

			$('#s_data') .datagrid(
				{
					nowrap : false,
					striped : true,
					collapsible : true,
					idField : 'orderId',
					url : '${rc.contextPath}/logisticsTimeout/jsonLogisticsOrderInfo',
					queryParams: {
	                    sellerId: '${sellerId!"0"}',
	                    orderIdList:'${orderIdList!""}',
	                    startTime:'${startTime!""}',
	                    endTime:'${endTime!""}'
					},
					loadMsg : '正在装载数据...',
					fitColumns : true,
					remoteSort : true,
					singleSelect : false,
					pageSize : 50,
					pageList : [ 50, 60 ],
					columns : [ [
							{field : 'orderId',    title:'序号', align:'center',checkbox:true},
							{field : 'createTime', title : '下单时间', width : 70, align : 'center'},
							{field : 'payTime', title : '付款时间', width : 70, align : 'center'},
							{field : 'sendTime', title : '发货时间', width : 70, align : 'center'},
							{field : 'expireTime', title : '超时日期', width : 70, align : 'center'},
							{field : 'sSellerName', title : '商家', width : 60, align : 'center'},
							{field : 'isTimeout', title : '是否<br/>超时', width : 15, align : 'center',
								formatter:function(value,row,index){
									if(parseInt(row.isTimeout)==1){
										return '是';
									}else{
										return '否';
									}
								}
							},
							{field : 'number', title : '订单编号', width : 50, align : 'center',
								formatter : function(value, row, index) {
									return '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.orderId+'">'+row.number+'</a>';
								}
							},
							{field : 'oStatusDescripton', title : '订单状态', width : 40, align : 'center'},
							{field : 'raFullName', title : '收货人', width : 30, align : 'center'},
							{field : 'raMobileNumber', title : '收货手机', width : 40, align : 'center'},
							{field : 'ologChannel', title : '物流公司', width : 40, align : 'center'},
							{field : 'ologNumber', title : '运单号', width : 40, align : 'center'},
							{field : 'timeOutreason', title : '超时原因', width : 70, align : 'center'},
							/* {field : 'timeOutResult', title : '申诉结果', width : 30, align : 'center'}, */
							{field : 'hidden', title : '操作', width : 45, align : 'center',
								formatter : function(value, row, index) {
									if(parseInt(row.isTimeout)==1){
										var a = '';
										var b = '';
                                    	var timeout_op = '| <a href="javaScript:;" onclick="cancelTimeout(' + row.orderId + ')">变更超时状态</a>';
										if(parseInt(row.complainStatus) == 1){
											a = '处理中 | ';
										}else if(parseInt(row.complainStatus) == 2 || parseInt(row.complainStatus) == 3){
											a = '<a href="javaScript:;" onclick="complainDetail(' + row.orderId + ')">申诉明细</a> | ';
										}else{
											a = '未申诉 | ';
										}
										if(row.isLogisticsTimeoutDeal == 1){
											b = '<a href="javascript:;" onclick="qcDeal('+index+')">QC更新</a>';
										}else{
											b = '<a href="javascript:;" onclick="qcDeal('+index+')">QC处理</a>';
										}
										return a + b + timeout_op;
									}else{
										return '未申诉';
									}
								}
							} ] ],
							toolbar:[{
				                id:'_add',
				                text:'QC批量处理',
				                iconCls:'icon-edit',
				                handler:function(){
				                	$('#qcBatchDealForm_orderIds').val('');
				        			$('#qcBatchDealForm_reason').combobox('clear');
				        			$('#qcBatchDealForm_remark').val('');
				        			$('#qcBatchDealForm_result').val('');

				                	var rows = $('#s_data').datagrid("getSelections");
				                	if(rows.length > 0){
					                	var ids = [];
			                            for(var i=0;i<rows.length;i++){
			                                ids.push(rows[i].orderId)
			                            }
			                            $("#qcBatchDealForm_orderIds").val(ids.join(","));
			                            $("#qcBatchDealDiv").dialog("open");
				                	}else{
				                		$.messager.alert('提示','请选择要操作的订单',"error")
				                	}
				                }
				            }],
					pagination : true
				});
		});
	</script>

</body>
</html>