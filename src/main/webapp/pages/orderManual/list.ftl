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
			<div data-options="region:'north',title:'条件筛选-手动订单',split:true" style="height: 140px;">
				<form action="${rc.contextPath}/orderManual/export" id="searchForm" method="post">
						<table style="margin-left:10px">
							<tr>
								<td>
									订单编号：<input id="orderNumber" name="orderNumber" value="" />
								</td>
								<td>&nbsp;&nbsp;创建时间：
									<input value="" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
									~
			 						<input value="" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
			 					</td>
							</tr>
							<tr>
								<td>
									发货状态：<select id="status" name="status">
										<option value="0">全部</option>
										<option value="1">待发货</option>
										<option value="2">已发货</option>
										<option value="3">客服取消</option>
									</select>
								</td>
								<td>
									补发类型：<select id="sendType" name="sendType">
										<option value="-1">全部</option>
										<option value="1">售后补发</option>
										<option value="2">顾客打款请求发货</option>
									</select>
			 					</td>
							</tr>
							<tr>
								<td>
									收货人姓名：<input id="fullName" name="fullName" value="" />
									<input type=hidden id="exportType" name="exportType" value="" />	
								</td>
								<td>
									&nbsp;&nbsp;收货手机号：<input id="mobileNumber" name="mobileNumber" value="" />&nbsp;&nbsp;
									<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;&nbsp;
									<a id="exportSeller" onclick="exportSeller()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出商家发货表</a>&nbsp;&nbsp;
									<a id="exportSellerAllStatus" onclick="exportSellerAllStatus()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出订单明细</a>
									<!-- &nbsp;&nbsp;<input type="submit" onClick="return exportSeller();" class="easyui-linkbutton" value="导出商家发货表" style="height: 30px"/> -->
									<!-- &nbsp;&nbsp;<input type="submit" onClick="return exportSellerAllStatus();" class="easyui-linkbutton" value="导出订单明细" style="height: 30px"/> -->
								</td>
							</tr>
						</table>
					</form>
			</div>
			<div data-options="region:'center'" >
				<table id="s_data">

				</table>
			</div>
			
			<!-- 填写发货所需信息的div begin -->
				<div id="sendOrderDiv" class="easyui-dialog" style="width:350px;height:220px;padding:20px 20px;">
            		<input id="orderId" type="hidden" name="orderId" />
            		<table cellpadding="5">
                		<tr>
                    		<td>物流渠道:</td>
                    		<td><select id="channel" name="channel" style="width:200px;"></select></td>
                		</tr>
                		<tr>
                    		<td>物流单号:</td>
                    		<td><input id="number" type="text" name="number" style="width:190px;" ></input></td>
                		</tr>
            		</table>
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
	
		function cancelOrder(id){
			$.messager.confirm('(^_^)','确定取消吗',function(b){
                if(b){
                	$.messager.progress();
                	$.ajax({ 
                	       url: '${rc.contextPath}/orderManual/cancelOrder',
                	       type: 'post',
                	       dataType: 'json',
                	       data: {'id':id},
                	       success: function(data){
                	         $.messager.progress('close');
                	           if(data.status == 1){
                	            $('#s_data') .datagrid("load");
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
     		})
		}
		
		function exportSeller(){
			$('#exportType').val("seller");
			$('#searchForm').submit();
		}
		
		function exportSellerAllStatus(){
			$('#exportType').val("sellerAllStatus");
			$('#searchForm').submit();
		}
	
		function sendOrder(index) {
	    	$('#channel').combobox('clear');
	    	$('#number').val("");
			var arr=$("#s_data").datagrid("getData");
			$('#orderId').val(arr.rows[index].id);
			$('#sendOrderDiv').dialog('open');
		}
	
		function searchOrder() {
			$('#s_data').datagrid('load', {
				startTime : $("#startTime").val(),
				endTime : $("#endTime").val(),
				orderNumber : $("#orderNumber").val(),
				status : $("#status").val(),
				fullName : $("#fullName").val(),
				mobileNumber : $("#mobileNumber").val(),
				sendType : $("#sendType").val()
			});
		}
		
		$('#sendOrderDiv').dialog({
            title:'物流信息',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'发货',
                iconCls:'icon-ok',
                handler:function(){
                	var id=$('#orderId').val();
                	var channel=$('#channel').combobox('getValue');
                	var number=$('#number').val();
                	if(channel=='' || number==''){
                		$.messager.alert("次奥",'请填写完整物流信息',"info");
                	}else{
                		$.messager.progress();
                    	$.ajax({ 
                    	       url: '${rc.contextPath}/orderManual/sendOrder',
                    	       type: 'post',
                    	       dataType: 'json',
                    	       data: {'id':id,channel:channel,number:number},
                    	       success: function(data){
                    	         $.messager.progress('close');
                    	           if(data.status == 1){
                    	            $('#s_data') .datagrid("reload");
                    	            $('#sendOrderDiv').dialog('close');
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
		
		$(function(){
			$('#s_data') .datagrid({
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/orderManual/manualJson',
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 50,
				pageList : [ 50, 60 ],
				columns : [ [
						{field : 'createTime', title : '下单时间', width : 70, align : 'center'},
						{field : 'sendTime', title : '发货时间', width : 70, align : 'center'},
						{field : 'number', title : '订单编号', width : 50, align : 'center',
							formatter : function(value, row, index) {
								var a = '<a target="_blank" href="${rc.contextPath}/orderManual/detail/'+row.id+'">'+row.number+'</a>';
								return a;
							}
						},
						{field : 'statusDescripton', title : '订单状态', width : 30, align : 'center'},
						{field : 'totalPrice', title : '订单总价', width : 30, align : 'center'},
						{field : 'fullName', title : '收货人', width : 40, align : 'center'},
						{field : 'mobileNumber', title : '收货手机', width : 40, align : 'center'},
						{field : 'sellerName', title : '商家', width : 50, align : 'center'},
						{field : 'sendAddress', title : '发货地', width : 70, align : 'center'},
						{field : 'desc', title : '备注', width : 70, align : 'center'},
						{field : 'logisticsChannel', title : '物流公司', width : 60, align : 'center'},
						{field : 'logisticsNumber', title : '运单号', width : 50, align : 'center'},
						{field : 'sendType', title : '补发类型', width : 50, align : 'center',formatter:function(value, row, index) {
							if(row.sendType==1){
								return '售后补发货';
							}else if(row.sendType==2){
								return '顾客打款请求发货';
							}else{
								return '';
							}
						}},
						{field : 'hidden', title : '操作', width : 80, align : 'center',
							formatter : function(value, row, index) {
								var a = '';
								var b = '';
								if(row.status==1){
									a = '<a href="javaScript:;" onclick="sendOrder(' + index + ')">发货</a>';
									b = ' | <a href="javaScript:;" onclick="cancelOrder(' + row.id + ')">取消订单</a>';
								}else if(row.status==2){
									a = '<a href="javaScript:;" onclick="sendOrder(' + index + ')">修改物流信息</a>';
								}
								return a + b;
							}
						} ] ],
				pagination : true
			});
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
			
			$('#channel').combobox({   
			    url:'${rc.contextPath}/order/jsonCompanyCode',   
			    valueField:'code',   
			    textField:'text'  
			});
		});
	</script>

</body>
</html>