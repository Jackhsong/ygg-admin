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

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',title:'条件筛选-订单管理',split:true" style="height: 80px;">
				<form action="${rc.contextPath}/order/exportSellerLogisticAnalyzeDetail" id="searchForm" method="post" >
						<input type="hidden" id="sendTime" name="sendTime" value="${sendTime}"/>
						<input type="hidden" id="hour" name="hour" value="${hour}"/>
						<input type="hidden" id="sellerId" name="sellerId" value="${sellerId}"/>
						<table>
							<tr>
								<td>${sendTime}商家发货后物流明细&nbsp;&nbsp;&nbsp;</td>
								<td>
									<input type="submit" value="导出结果" onClick="return exportResult();" style="width:100px;height: 25px;"/>&nbsp;
								</td>
							</tr>
						</table>
					</form>
			</div>		
			<div data-options="region:'center'" >
				<table id="s_data"></table>
				
				<!-- 填写发货所需信息的div begin -->
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
			</div>
		</div>
	</div>

	<script>
		function exportResult(){
			$('#exportType').val("result");
			return true;
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
		
		function uploadOrder() {
        	$('#uploadOrderForm').submit();
		}
		
		function toDetail(id){
    		window.open("${rc.contextPath}/order/detail/"+id,"_blank");
		}

		$(function() {
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
			$('#sellerId').combobox({
                	panelWidth:350,
                	panelHeight:350,
                	mode:'remote',
				    url:'${rc.contextPath}/seller/jsonSellerCode',
				    valueField:'code',   
				    textField:'text'
				    //onSelect:function(){
				    	//var sellerId=$("input[name='sellerId']").val();
				    	///$.post("${rc.contextPath}/product/ajaxSellerInfo", 
							//{id: sellerId},
							//function(data){
								//if(data.status == 1){
									//$("#sendAddress").html("");
									//$("#sendAddress").html(data.sendAddress);
								//}
								//else{
									//$("#sendAddress").val("");
								//}
						//}, "json");
				    //}
			});
			
			$('#orderFileBox').filebox({
				buttonText: '选择文件',
				buttonAlign: 'right'
			})
			
			$("#sellerType").combobox({
				url:'${rc.contextPath}/seller/jsonSellerType',   
			    valueField:'code',   
			    textField:'text' 
			});
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
				queryParams: {
					sendTime: '${sendTime}',
					hour:'${hour}',
					sellerId:'${sellerId}'
				},
				url : '${rc.contextPath}/order/jsonSellerLogisticAnalyzeDetail',
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
						{field : 'orderChannel', title : '客户端类型', width : 50, align : 'center'},
						{field : 'orderSource', title : '订单来源', width : 60, align : 'center'},
						{field : 'number', title : '订单编号', width : 70, align : 'center',
							formatter : function(value, row, index) {
								var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
								return a;
							}
						},
						{field : 'oStatusDescripton', title : '订单状态', width : 50, align : 'center'},
						{field : 'oTotalPrice', title : '订单总价', width : 40, align : 'center'},
						{field : 'oRealPrice', title : '实付金额', width : 40, align : 'center'},
						{field : 'raFullName', title : '收货人', width : 40, align : 'center'},
						{field : 'raMobileNumber', title : '收货手机', width : 60, align : 'center'},
						{field : 'sSellerName', title : '商家', width : 70, align : 'center'},
						{field : 'sSendAddress', title : '发货地', width : 70, align : 'center'},
						{field : 'operaStatus', title : '是否导出', width : 40, align : 'center'},
						{field : 'ologChannel', title : '物流公司', width : 60, align : 'center'},
						{field : 'ologNumber', title : '运单号', width : 70, align : 'center'},
						{field : 'isSettlement', title : '是否结算', width : 40, align : 'center'},
				] ],
				pagination : true,
				rownumbers : true
			});
			$('#sourceType').combobox({
			    url:'${rc.contextPath}/order/searchInfo?type=2',   
			    valueField:'code',   
			    textField:'text'  
			});
		})
	</script>

</body>
</html>