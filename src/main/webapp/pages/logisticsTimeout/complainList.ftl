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
			<div data-options="region:'north',title:'商家物流时效更新申诉管理',split:true">
			</div>
			<div data-options="region:'center'" >
				<table id="s_data">

				</table>
				
	        	
	        	<!-- 超时申诉 begin -->
				<div id="dealDiv" class="easyui-dialog" style="width:500px;height:400px;padding:20px 20px;">
	            	<form id="dealForm" method="post">
	            		<input id="dealForm_orderId" type="hidden" name="orderId" />
	            		<input id="dealForm_complainId" type="hidden" name="complainId"/>
	            		<p>
	            			订单编号：<span id="dealForm_orderNumber"></span>
	            		</p>
	            		<p>
	            			商家名称：<span id="dealForm_sellerName"></span>
	            		</p>
	            		<p>
	            			申诉理由：<span id="dealForm_complain_reason"></span>
	            		</p>
	            		<p>
	            			超时原因：<input id="dealForm_timeout_reason" name="timeoutReasonId"/>
	            		</p>
	            		<p>
	            			处理结果：<input type="radio" name="dealResult" id="dealForm_dealResult2" value="2">申诉成功&nbsp;&nbsp;
	            					<input type="radio" name="dealResult" id="dealForm_dealResult3" value="3">申诉失败
	            		</p>
	            		<p>
	            			处理备注：<input type="text" name="remark" id="dealForm_remark" size="30" maxlength="50"/>
	            		</p>
	        		</form>
	        	</div>	        		
	        	<!-- 超时申诉 end -->
			</div>
		</div>
	</div>

	<script>

		function deal(index){
			$("#dealForm_orderId").val('');
			$("#dealForm_orderNumber").text('');
			$("#dealForm_sellerName").text('');
			$("#dealForm_complain_reason").text('');
			$("#dealForm input[name='dealResult']").each(function(){
				$(this).prop('checked',false);
			});
			$("#dealForm_timeout_reason").combobox('clear');
			$("#dealForm_remark").val('');
			var arr=$("#s_data").datagrid("getData");
			$('#dealForm_orderId').val(arr.rows[index].orderId);
			$('#dealForm_complainId').val(arr.rows[index].complainId);
			$('#dealForm_orderNumber').text(arr.rows[index].number);
			$('#dealForm_sellerName').text(arr.rows[index].sSellerName);
			$('#dealForm_complain_reason').text(arr.rows[index].complainReason);
			var url = '${rc.contextPath}/logisticsTimeout/jsonTimeoutReasonCode?isAvailable=1&id='+arr.rows[index].reasonId;
			$("#dealForm_timeout_reason").combobox('reload',url);
			$('#dealDiv').dialog('open');
		}
		$(function() {
            
            $("#dealForm_timeout_reason").combobox({
            	url:'${rc.contextPath}/logisticsTimeout/jsonTimeoutReasonCode?isAvailable=1',
                valueField:'code',
                textField:'text'
            });
			
			$('#dealDiv').dialog({
	            title:'订单申诉处理',
	            collapsible:true,
	            closed:true,
	            modal:true,
	            buttons:[{
	                text:'处理',
	                iconCls:'icon-ok',
	                handler:function(){
	                    $('#dealForm').form('submit',{
	                        url:"${rc.contextPath}/logisticsTimeout/dealComplain",
	                        onSubmit:function(){
	                        	var reason = $("#dealForm_timeout_reason").combobox('getValue');
	                        	var dealResult = $("input[name='dealResult']:checked").val();
	                        	if(reason=='' || reason == null || reason == undefined){
	                        		$.messager.alert('提示',"请选择超时原因",'error');
	                        		return false;
	                        	}else if(dealResult=='' || dealResult == null || dealResult == undefined){
	                        		$.messager.alert('提示',"请选择处理结果",'error');
	                        		return false;
	                        	}
	                        	$.messager.progress();
	                        },
	                        success:function(data){
	                        	$.messager.progress('close');
	                            var res = eval("("+data+")")
	                            if(res.status == 1){
	                                $('#dealDiv').dialog('close');
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
	                    $('#dealDiv').dialog('close');
	                }
	            }]
	        });
		
		
			$('#s_data') .datagrid(
			{
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/logisticsTimeout/jsonComplainOrderInfo',
				loadMsg : '正在装载数据...',
                queryParams:{
                    orderType:${orderType}
                },
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 50,
				pageList : [ 50, 60 ],
				columns : [ [
						/* {field : 'createTime', title : '下单时间', width : 70, align : 'center'}, */
						{field : 'payTime', title : '付款时间', width : 70, align : 'center'},
						{field : 'sendTime', title : '发货时间', width : 70, align : 'center'},
						{field : 'isTimeout', title : '是否超时', width : 30, align : 'center',
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
						{field : 'raFullName', title : '收货人', width : 40, align : 'center'},
						{field : 'raMobileNumber', title : '收货手机', width : 60, align : 'center'},
						{field : 'ologChannel', title : '物流公司', width : 60, align : 'center'},
						{field : 'ologNumber', title : '运单号', width : 70, align : 'center'},
						{field : 'sSellerName', title : '商家', width : 70, align : 'center'},
						{field : 'timeoutReason', title : '超时原因', width : 70, align : 'center'},
						{field : 'hidden', title : '操作', width : 30, align : 'center',
							formatter : function(value, row, index) {
								return '<a href="javaScript:;" onclick="deal(' + index + ')">处理</a>'
							}
						} ] ],
				pagination : true
			});
		})
	</script>

</body>
</html>