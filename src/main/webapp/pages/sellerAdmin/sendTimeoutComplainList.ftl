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
textarea{
	resize:none
}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'菜单列表',split:true" style="width: 180px;">
	 <#include "../sellerAdmin/menu.ftl" >
	</div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',title:'发货时效管理',split:true" style="height: 100px;">
				<form action="${rc.contextPath}/sellerAdminOrder/exportUnSettlementOrder" id="searchForm" method="post" style="padding-top: 20px;">
                    	<input type="hidden" name="sellerId"    id="sellerId" value="${sellerId!'0'}" />
                    	订单编号：<input id="orderNumber" name="orderNumber" value="" />&nbsp;&nbsp;
						申诉状态：<select id="complainStatus" name="complainStatus">
									<option value="-1" selected="selected">--全部--</option>
									<option value="1">--处理中--</option>
									<option value="2">--申诉成功--</option>
									<option value="3">--申诉失败--</option>
								</select>&nbsp;&nbsp;
                    	发货时效是否超时：<select id="isTimeout" name="isTimeout" style="width: 173px;">
										<option value="-1" selected="selected">--全部--</option>
										<option value="1">--是--</option>
										<option value="0">--否--</option>
									</select>&nbsp;&nbsp;
                      <a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
				</form>
			</div>
			<div data-options="region:'center'" >
				<table id="s_data">

				</table>
				
				<!-- 填写发货所需信息的div begin -->
				<div id="sendOrderDiv" class="easyui-dialog" style="width:350px;height:240px;padding:20px 20px;">
	            	<form id="sendOrderForm" method="post">
	            		<input id="orderId" type="hidden" name="orderId" />
	            		<input id="sellerId" name="sellerId" type="hidden" value="${sellerId!'0'}" />
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
	        	
	        	<!-- 超时申诉 begin -->
				<div id="complainDiv" class="easyui-dialog" style="width:500px;height:250px;padding:20px 20px;">
	            	<form id="complainForm" method="post">
	            		<input id="complainForm_orderId" type="hidden" name="orderId" />
	            		<p>
	            			订单编号：<span id="complainForm_orderNumber"></span>
	            		</p>
	            		<p>
	            			申诉理由：<textarea id="complainForm_reason" name="reason" rows="5" cols="50"></textarea>
	            		</p>
	        		</form>
	        	</div>	        		
	        	<!-- 超时申诉 end -->
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
				sellerId : ${sellerId!"0"},
				isTimeout:$("#isTimeout").val(),
				complainStatus:$("#complainStatus").val()
			});
		}

        function exportUnSettlementOrder(){
			var timeTypeCode = $("input[name='timeType']:checked").val();
			var startTime2 = $("#startTime2").val();
			var endTime2 = $("#endTime2").val();
			if(timeTypeCode == 2 && (startTime2 == '' || endTime2 == ''))
			{
                $.messager.alert('响应信息',"请筛选时间",'error');
			}
            $('#searchForm').submit();
        }
		
		function sendOrder(index) {
			$("#sendType1").prop("checked", true);
			$("#sendType1").change();
        	$('#channel').combobox('clear');
        	$('#money').val("");
        	$('#number').val("");
			var arr=$("#s_data").datagrid("getData");
			$('#orderId').val(arr.rows[index].id);
			$('#sendOrderDiv').dialog('open');
		}
		
		function editSendOrder(index) {
			$("#sendType1").prop("checked", true);
			$("#sendType1").change();
        	$('#channel').combobox('clear');
        	$('#money').val("");
        	$('#number').val("");
        	$('#orderId').val("");
			var arr=$("#s_data").datagrid("getData");
			var channel = arr.rows[index].ologChannel;
			$('#channel').combobox('setValue', channel);
        	$('#money').val(arr.rows[index].ologMoney);
        	$('#number').val(arr.rows[index].ologNumber);
			$('#orderId').val(arr.rows[index].id);
			$('#sendOrderDiv').dialog('open');
		}
		
		function toDetail(id){
    		window.open("${rc.contextPath}/sellerAdminOrder/detail/"+id,"_blank");
		}

		function complain(index){
			$("#complainForm_orderId").val('');
			$("#complainForm_orderNumber").text('');
			$("#complainForm_reason").val('');
			var arr=$("#s_data").datagrid("getData");
			$('#complainForm_orderId').val(arr.rows[index].id);
			$('#complainForm_orderNumber').text(arr.rows[index].number);
			$('#complainDiv').dialog('open');
		}
		$(function() {
            $('#channel').combobox({
                url:'${rc.contextPath}/sellerAdminOrder/jsonCompanyCode',
                valueField:'code',
                textField:'text'
            });

			$('#sendType1').change(function(){
				if($('#sendType1').is(':checked')){
					$('#haveChannel').show();
				}
			});
//			$('#sendType0').change(function(){
//				if($('#sendType0').is(':checked')){
//					$('#haveChannel').hide();
//				}
//			});
			
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
                                    $('#s_data').datagrid('reload');
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
			
			$('#complainDiv').dialog({
            title:'订单申诉',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'提交申诉',
                iconCls:'icon-ok',
                handler:function(){
                    $('#complainForm').form('submit',{
                        url:"${rc.contextPath}/sellerAdminOrder/sendTimeOutComplain",
                        onSubmit:function(){
                        	var reason = $("#complainForm_reason").val();
                        	if($.trim(reason)==''){
                        		$.messager.alert('提示',"请填写申诉理由",'error');
                        		return;
                        	}else if(reason.length>200){
                        		$.messager.alert('提示',"申诉理由请限制在200字以内",'error');
                        		return;
                        	}
                        	$.messager.progress();
                        },
                        success:function(data){
                        	$.messager.progress('close');
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#complainDiv').dialog('close');
                                $.messager.alert('响应信息',"申诉成功",'info',function(){
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
                    $('#complainDiv').dialog('close');
                }
            }]
        });
		
		
			$('#s_data') .datagrid(
			{
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/sellerAdminOrder/jsonSendTimeoutComplainOrderInfo',
				queryParams: {
                    sellerId: '${sellerId!"0"}'
				},
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 50,
				pageList : [ 50, 60 ],
				columns : [ [
						{field : 'createTime', title : '下单时间', width : 70, align : 'center'},
						{field : 'payTime', title : '付款时间', width : 70, align : 'center'},
						{field : 'sendTime', title : '发货时间', width : 70, align : 'center'},
						{field : 'isTimeout', title : '发货时效<br/>是否超时', width : 30, align : 'center',
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
								var a = '<a target="_blank" href="${rc.contextPath}/sellerAdminOrder/detail/'+row.id+'">'+row.number+'</a>';
								return a;
							}
						},
						{field : 'oStatusDescripton', title : '订单状态', width : 50, align : 'center'},
						{field : 'refundStatus', title : '退款状态', width : 50, align : 'center'},
						{field : 'raFullName', title : '收货人', width : 40, align : 'center'},
						{field : 'raMobileNumber', title : '收货手机', width : 60, align : 'center'},
						{field : 'ologChannel', title : '物流公司', width : 60, align : 'center'},
						{field : 'ologNumber', title : '运单号', width : 70, align : 'center'},
						{field : 'timeOutReason', title : '超时原因', width : 70, align : 'center'},
						{field : 'timeOutResult', title : '申诉结果', width : 30, align : 'center'},
						{field : 'hidden', title : '操作', width : 40, align : 'center',
							formatter : function(value, row, index) {
								/* var a = '';
								if(row.status == 2){
									a = '<a href="javaScript:;" onclick="sendOrder(' + index + ')">发货</a>';
								}else if(row.status == 3 && row.ologNumber != ''){
									a = '<a href="javaScript:;" onclick="editSendOrder(' + index + ')">修改发货信息</a>';
								}
								var b = ''; */
								if(parseInt(row.isTimeout)==1){
									if(parseInt(row.complainStatus) == 0){
										return '<a href="javaScript:;" onclick="complain(' + index + ')">超时申诉</a>';
									}else if(parseInt(row.complainStatus) == 3){
										return '<a href="javaScript:;" onclick="complain(' + index + ')">重新申诉</a>';
									}
								}else{
									return '-';
								}
								/* if(a !='' && b!=''){
									return a + ' | ' + b;
								}else{
									return a + b;
								} */
							}
						} ] ],
				pagination : true
			});
		})
	</script>

</body>
</html>