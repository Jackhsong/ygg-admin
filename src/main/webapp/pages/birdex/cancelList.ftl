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
		<div data-options="region:'north',title:'笨鸟取消推送订单列表',split:true" style="height: 120px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td class="searchName">订单编号：</td>
						<td class="searchText">
							<input id="orderNumber" name="orderNumber" value="" />
						</td>
						<!-- <td class="searchName">推送状态：</td>
						<td class="searchText">
							<select id="sendStatus" name="sendStatus" style="width: 100px;">
								<option value="0">未推送</option>
								<option value="1">已推送</option>
								<option value="-1">全部</option>
							</select>
						</td> -->
						<td class="searchText" style="padding-top: 5px">
							<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
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
	
	function searchOrder(){
		$('#s_data').datagrid('load', {
			number : $("#orderNumber").val(),
			sendStatus : $("#orderStatus").val()
		});
	}
	
	function pushAgain(id){
		$.messager.confirm('提示','确定恢复推送？',function(r){
		    if (r){
		    	$.messager.progress();
    			$.ajax({
					url: '${rc.contextPath}/birdex/updateBirdexOrderConfirmPushStatus',
					type: 'post',
					dataType: 'json',
				    data: {'orderId':id},
					success: function(data){
						$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid("load");
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

	$(function(){
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        idField:'id',
	        url:'${rc.contextPath}/birdex/jsonBirdexCancelOrderInfo',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        pageSize:50,
	        pageList:[50,100],
	        columns:[[
				{field:'checkId',    title:'序号', align:'center',checkbox:true},
	            {field :'id',    title:'ID', width:20, align:'center'},
	            {field :'payTime', title : '付款时间', width : 50, align : 'center'},
	            {field :'number', title : '订单编号', width : 50, align : 'center',
					formatter : function(value, row, index) {
						var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
						return a;
					}
				},
				{field :'totalPrice', title : '总价', width : 20, align : 'center'},
				{field :'realPrice', title : '实付', width : 20, align : 'center'},
	            {field:'receiveAddress',    title:'收货地址', width:60, align:'center'},
	            {field:'fullName',     title:'收货人',  width:35,   align:'center' },
	            {field:'idCard',     title:'身份证号',  width:50,   align:'center' },
	            {field:'status',     title:'推送状态',  width:50,   align:'center' },
	            {field:'hidden',  title:'操作', width:70,align:'center',
	                formatter:function(value,row,index){
	                    return '<a href="javaScript:;" onclick="pushAgain(' + row.id + ')">恢复推送</a>'
	                }
	            }
	        ]],
	        pagination:true
	    });
	});
</script>
</body>
</html>