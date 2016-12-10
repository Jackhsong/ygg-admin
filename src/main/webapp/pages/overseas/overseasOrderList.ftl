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
		<#include "../common/menu.ftl" >
	</div>

	<div data-options="region:'center',title:'content'" style="padding: 5px;">
	
<!--  begin  订单筛选div -->

<div id="searchDiv" class="datagrid-toolbar" style="height: 140px; padding: 15px">
		<form action="${rc.contextPath}/overseasOrder/exportResult" id="searchForm" method="post">
			<table>
				<tr>
					<td>付款时间：</td>
					<td>
						<input value="" id="startTime1" name="startTime1" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" />
 						~
 						<input value="" id="endTime1" name="endTime1" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" />
 						&nbsp;&nbsp;&nbsp;确定导出成功时间：
 						<input value="" id="startTime2" name="startTime2" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00'})" />
 						~
 						<input value="" id="endTime2" name="endTime2" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00'})" />
					</td>
				</tr>
				<tr>
					<td>订单编号：</td>
					<td>
						<input id="orderNumber" name="orderNumber" value="" />
						&nbsp;&nbsp;订单状态:
						<select id="orderStatus" name="orderStatus">
							<option value="0">全部</option>
							<option value="2">待发货</option>
							<option value="3">已发货</option>
							<option value="4">交易成功</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;订单导出状态：
						<select id="operaStatus" name="operaStatus">
							<option value="0">未导出</option>
							<option value="1">已导出</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>用户名：</td>
					<td>
						<input id="accountName" name="accountName" value="" />	
						&nbsp;&nbsp;收货人姓名：
						<input id="receiveName" name="receiveName" value="" />	
						&nbsp;&nbsp;收货手机号：
						<input id="reveivePhone" name="reveivePhone" value="" />
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type=hidden id="exportType" name="exportType" value="" />
					    <#--<td>订单总价下限</td>-->
						<input type=hidden id="minimumTotalPrice" name="minimumTotalPrice" value="" />
						<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						&nbsp;&nbsp;&nbsp;
						<input type="button" onclick="return exportSearch()" value="导出结果" style="width:100px"/>
					</td>
				</tr>
			</table>
		</form>
		<form style="" id="exportAll" method="post" >
			<input id="exportAllId" name="lastTime" type="hidden" value="0" />
			<input id="isBigPrice" name="isBigPrice" type="hidden" value="0" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a id="searchBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出所有可导订单</a>
			&nbsp;&nbsp;<a id="exportAll2" onclick="exportAll2()" href="javascript:;" class="easyui-linkbutton">导出所有可导订单(2000元以上)</a>
		</form>
</div>

<!--  end  订单筛选div -->

	<div class="selloff_mod">
		<table id="s_data">
			
		</table>
	</div>

</div>

<script>

function searchOrder() {
	$('#s_data').datagrid("clearSelections");
	$('#s_data').datagrid('load', {
		orderNumber : $("#orderNumber").val(),
		orderStatus : $("#orderStatus").val(),
		accountName : $("#accountName").val(),
		receiveName : $("#receiveName").val(),
		reveivePhone : $("#reveivePhone").val(),
		startTime1 : $("#startTime1").val(),
		endTime1 : $("#endTime1").val(),
		startTime2 : $("#startTime2").val(),
		endTime2 : $("#endTime2").val(),
		operaStatus : $("#operaStatus").val()
	});
}

//function exportByLimitMoney() {
//	$("#minimumTotalPrice").val(2000);
//    $('#searchForm').submit();
//}
function exportSearch(){
    $("#minimumTotalPrice").val(-1);
    $('#searchForm').submit();
}

function exportAll(){
	$("#isBigPrice").val(0);
	$('#exportAll').submit();
}

function exportAll2() {
    $("#isBigPrice").val(1);
    $('#exportAll').submit();
}
$(function(){
	$('#exportAll').form({
	    url:'${rc.contextPath}/overseasOrder/exportAll',   
	    onSubmit: function(){
	    	$.messager.progress();
	    	var exportAllId = $('#exportAllId').val();
	    	if(exportAllId != '0' ){
	    		$.messager.progress('close');
	    		return true;
	    	}
	    	$.ajax({
        		url:"${rc.contextPath}/overseasOrder/checkOverseasExport",
        		type:'post',
        		data: {id:0},
        		dataType: 'json',
        		success:function(data){
        			$.messager.progress('close');
            		if(data.status == 1){
            			$('#exportAllId').val(data.lastTime);
            			$.messager.alert("提示","请再次点击按钮来完成导出","info");
            		}else{
            			$('#exportAllId').val('0');
            			$.messager.alert("提示",data.msg,"info");
            		}
        		}
    		});
	    	return false;
	    },
	    success:function(data){
	    }   
	});
	
	$('#s_data') .datagrid({
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'id',
		url : '${rc.contextPath}/overseasOrder/jsonOverseasOrderInfo',
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : false,
		pageSize : 50,
		pageList : [ 50, 60 ],
		columns : [ [
				{field : 'createTime', title : '下单时间', width : 90, align : 'center'},
				{field : 'payTime', title : '付款时间', width : 90, align : 'center'},
				{field : 'sendTime', title : '发货时间', width : 90, align : 'center'},
				{field : 'exportStatusStr', title : '是否导出', width : 40, align : 'center'},
				{field : 'exportTime', title : '确定导出成功时间', width : 80, align : 'center'},
				{field : 'number', title : '订单编号', width : 70, align : 'center',
					formatter : function(value, row, index) {
						var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
						return a;
					}
				},
				{field : 'statusStr', title : '订单状态', width : 50, align : 'center'},
				{field : 'totalPrice', title : '订单总价', width : 40, align : 'center'},
				{field : 'fullName', title : '收货人', width : 50, align : 'center'},
				{field : 'mobileNumber', title : '收货手机', width : 60, align : 'center'},
				{field : 'sellerName', title : '商家', width : 70, align : 'center'},
				{field : 'sendAddress', title : '发货地', width : 70, align : 'center'},
				{field : 'warehouse', title : '分仓', width : 70, align : 'center'}
				] ],
	toolbar:[{
            iconCls: 'icon-add',
            text:'全部改为已导出',
            handler: function(){
                var rows = $('#s_data').datagrid("getSelections");
                if(rows.length > 0){
                	var exportStatusStr = rows[0].exportStatusStr;
                	if(exportStatusStr == '未导出'){
                		$.messager.confirm('修改状态','确定修改为已导出状态吗',function(b){
                         if(b){
                             var ids = [];
                             for(var i=0;i<rows.length;i++){
                                 ids.push(rows[i].number)
                             }
                             $.post("${rc.contextPath}/overseasOrder/changeExport", //改为已导出
								{ids: ids.join(","),code:1},
								function(data){
									if(data.status == 1){
										$('#s_data').datagrid('reload');
									}else{
										$.messager.alert('提示','保存出错',"error")
									}
								},
								"json");
                         }else{
                        	 $('#s_data').datagrid("clearSelections");
                         }
              		 })
                	}else{
                		$.messager.alert('提示','当前订单已经是已导出状态',"error")
                	}
                }else{
                    $.messager.alert('提示','请选择要操作的商品',"error")
                }
            }
        },'-',{
            iconCls: 'icon-add',
            text:'全部改为未导出',
            handler: function(){
                var rows = $('#s_data').datagrid("getSelections");
                if(rows.length > 0){
                	var exportStatusStr = rows[0].exportStatusStr;
                	if(exportStatusStr == '已导出'){
                		$.messager.confirm('修改状态','确定修改为未导出状态吗',function(b){
                         if(b){
                             var ids = [];
                             for(var i=0;i<rows.length;i++){
                                 ids.push(rows[i].number)
                             }
                             $.post("${rc.contextPath}/overseasOrder/changeExport", //改为未导出
								{ids: ids.join(","),code:0},
								function(data){
									if(data.status == 1){
										$('#s_data').datagrid('reload');
									}else{
										$.messager.alert('提示','保存出错',"error")
									}
								},
								"json");
             				}else{
             					$('#s_data').datagrid("clearSelections");
             				}
              		     })
                	}else{
                		$.messager.alert('提示','当前订单已经是未导出状态',"error")
                	}
                }else{
                    $.messager.alert('提示','请选择要操作的商品',"error")
                }
            }
        }],
		pagination : true
	});   
	
});
</script>

</body>
</html>