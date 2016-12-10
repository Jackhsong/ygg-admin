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

</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center',title:'content'" style="padding: 5px;">

		<div title="订单管理" class="easyui-panel" style="padding: 10px">
			<div class="content_body">
				<div id="cc">
				<div id="searchDiv" class="datagrid-toolbar" style="height: 170px; padding: 15px">
					<form action="${rc.contextPath}/order/export" id="searchForm" method="post">
						<table>
							<tr>
								<td>付款时间：</td>
								<td>
									<input type="radio" name="timeType" value="1" />最近一个阶段
									<input value="${startTimeBegin}" id="startTime1" name="startTime1"/>
			 						~
			 						<input value="${startTimeEnd}" id="endTime1" name="endTime1" />
								</td>
			 					<td>
			 						&nbsp;&nbsp;
			 						<input type="radio" name="timeType" value="2" checked />自定义时间
									<input value="" id="startTime2" name="startTime2" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
									~
			 						<input value="" id="endTime2" name="endTime2" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								</td>
							</tr>
							<tr>
								<td>订单编号：</td>
								<td>
									<input id="orderNumber" name="orderNumber" value="" />
									&nbsp;&nbsp;订单状态:
									<select id="orderStatus" name="orderStatus">
										<option value="0">全部</option>
										<option value="1">未付款</option>
										<option value="2">待发货</option>
										<option value="3">已发货</option>
										<option value="4">交易成功</option>
										<option value="5">用户取消</option>
										<option value="6">超时取消</option>
									</select>
								</td>
								<td>
									&nbsp;&nbsp;订单导出状态：
									<select id="operaStatus" name="operaStatus">
										<option value="-1">全部</option>
										<option value="1">已导出</option>
										<option value="0">未导出</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>商品id：</td>
								<td>
									<input id="productId" name="productId" value="" />	
									&nbsp;&nbsp;商品名称：
									<input id="productName" name="productName" value="" />	
								</td>
								<td>
									&nbsp;&nbsp;发货类型：
									<select id="sellerType" name="sellerType">
										<option value="0">全部</option>
										<option value="1">国内</option>
										<option value="2">保税区</option>
										<option value="3">香港</option>
									</select>
									商家：
									<select id="sellerId" name="sellerId" >
										<option value="0">全部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										</option>
									</select>
									&nbsp;&nbsp;发货地：<span id="sendAddress" style="color:red"></span>
								</td>
							</tr>
							<tr>
								<td>用户名：</td>
								<td>
									<input id="accountName" name="accountName" value="" />	
									&nbsp;&nbsp;收货人姓名：
									<input id="receiveName" name="receiveName" value="" />	
								</td>
								<td>
									&nbsp;&nbsp;收货手机号：
									<input id="reveivePhone" name="reveivePhone" value="" />	
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
									<input type=hidden id="exportType" name="exportType" value="" />
									<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
									&nbsp;&nbsp;&nbsp;									
									<input type="submit" value="导出结果" onClick="return exportResult();" style="width:100px"/>
									&nbsp;&nbsp;&nbsp;
									<input type="submit" value="导出商家发货表" onClick="return exportSeller();" style="width:100px"/>
									&nbsp;&nbsp;&nbsp;
									<input type="submit" value="导出订单明细" onClick="return exportSellerAllStatus();" style="width:100px"/>
								</td>
								<td></td>
							</tr>
						</table>
					</form>
				</div>
				</div>
				
				<div class="selloff_mod">
					<table id="s_data">

					</table>
				</div>
				
				<!-- 填写发货所需信息的div begin -->
				<div id="sendOrderDiv" class="easyui-dialog" style="width:350px;height:220px;padding:20px 20px;">
	            	<form id="sendOrderForm" method="post">
	            		<input id="orderId" type="hidden" name="orderId" />
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
	        		</form>
	        	</div>
				<!-- 填写发货所需信息的div end -->
				
			</div>
		</div>

	</div>

	<script>
		
		function searchOrder() {
			$('#s_data').datagrid('load', {
				orderNumber : $("#orderNumber").val(),
				orderStatus : $("#orderStatus").val(),
				productId : $("#productId").val(),
				productName : $("#productName").val(),
				sellerType : $("#sellerType").val(),
				sellerId : $("input[name='sellerId']").val(),
				accountName : $("#accountName").val(),
				receiveName : $("#receiveName").val(),
				reveivePhone : $("#reveivePhone").val(),
				timeType : $("input[name='timeType']:checked").val(),
				startTime1 : $("#startTime1").val(),
				endTime1 : $("#endTime1").val(),
				startTime2 : $("#startTime2").val(),
				endTime2 : $("#endTime2").val(),
				operaStatus : $("#operaStatus").val()
			});
		}
		
		function exportSeller(){
			$('#exportType').val("seller");
			return true;
		}
		
		function exportSellerAllStatus(){
			$('#exportType').val("sellerAllStatus");
			return true;
		}
		
		function exportResult(){
			$('#exportType').val("result");
			return true;
		}
		
		function sendOrder(index) {
        	$('#channel').combobox('clear');
        	$('#money').val("");
        	$('#number').val("");
			var arr=$("#s_data").datagrid("getData");
			$('#orderId').val(arr.rows[index].id);
			$('#sendOrderDiv').dialog('open');
		}
		
		function editSendOrder(index) {
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
		
		function uploadOrder() {
        	$('#uploadOrderForm').submit();
		}
		
		function toDetail(id){
    		window.open("${rc.contextPath}/order/detail/"+id,"_blank");
		}

		$(function() {
		
			$('#sellerId').combobox({   
				    url:'${rc.contextPath}/seller/jsonSellerCode?isAvailable=1',
				    valueField:'code',   
				    textField:'text',
				    onSelect:function(){
				    	var sellerId=$("input[name='sellerId']").val();
				    	$.post("${rc.contextPath}/product/ajaxSellerInfo", 
							{id: sellerId},
							function(data){
								if(data.status == 1){
									$("#sendAddress").html("");
									$("#sendAddress").html(data.sendAddress);
								}
								else{
									$("#sendAddress").val("");
								}
						}, "json");
				    }
			});
			
			$('#orderFileBox').filebox({
				buttonText: '选择文件',
				buttonAlign: 'right'
			})
			
			$('#channel').combobox({   
			    url:'${rc.contextPath}/order/jsonCompanyCode',   
			    valueField:'code',   
			    textField:'text'  
			});
			
			$('#uploadOrderForm').form({
		    	url:'${rc.contextPath}/order/batchSendOrder',   
				onSubmit: function(){   
					// do some check   
					// return false to prevent submit;
					$.messager.progress();
		    	},   
				success:function(data){
		    		$.messager.progress('close');
		    		var data = eval('(' + data + ')');  // change the JSON string to javascript object   
		        	if (data.status == 1){
		        		$.messager.alert("提示","only test","info");
		            	window.location.href = "${rc.contextPath}/order/list";
		        	}else{
		        		$.messager.alert("提示",data.msg,"error");
		        	}
		    	}   
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
        })
		
		
			$('#s_data') .datagrid(
			{
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/order/jsonOrderInfo',
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 50,
				pageList : [ 50, 60 ],
				columns : [ [
						{field : 'oCreateTime', title : '下单时间', width : 90, align : 'center'},
						{field : 'oPayTime', title : '付款时间', width : 90, align : 'center'},
						{field : 'oSendTime', title : '发货时间', width : 90, align : 'center'},
						{field : 'orderChannel', title : '客户端类型', width : 80, align : 'center'},
						{field : 'orderSource', title : '订单来源', width : 80, align : 'center'},
						{field : 'number', title : '订单编号', width : 70, align : 'center',
							formatter : function(value, row, index) {
								var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
								return a;
							}
						},
						{field : 'oStatusDescripton', title : '订单状态', width : 50, align : 'center'},
						{field : 'oTotalPrice', title : '订单总价', width : 40, align : 'center'},
						{field : 'raFullName', title : '收货人', width : 50, align : 'center'},
						{field : 'raMobileNumber', title : '收货手机', width : 60, align : 'center'},
						{field : 'sSellerName', title : '商家', width : 70, align : 'center'},
						{field : 'sSendAddress', title : '发货地', width : 70, align : 'center'},
						{field : 'operaStatus', title : '是否导出', width : 40, align : 'center'},
						{field : 'ologChannel', title : '物流公司', width : 60, align : 'center'},
						{field : 'ologNumber', title : '运单号', width : 70, align : 'center'},
						{field : 'hidden', title : '操作', width : 80, align : 'center',
							formatter : function(value, row, index) {
								var a = '';
								if(row.oStatus == 2){
									a = '<a href="javaScript:;" onclick="sendOrder(' + index + ')">发货</a>';
								}else if(row.oStatus == 3){
									a = '<a href="javaScript:;" onclick="editSendOrder(' + index + ')">修改发货信息</a>';
								}
								return a;
							}
						} ] ],
				pagination : true,
				rownumbers : true
			});   			
		})
	</script>

</body>
</html>