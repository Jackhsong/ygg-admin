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
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script src="${rc.contextPath}/pages/js/commonUtil.js"></script>
<style>
	
</style>
</head>
<body class="easyui-layout">
<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
	
	<div id="searchDiv" class="datagrid-toolbar" style="height: 30px; padding: 15px">
		<form id="searchDivForm"   method="post">
		<input type="hidden"  id="nodeId" name="nodeId"/>
			<table>
				<tr>
					<td>查询日期</td>
			<td>
					<input value="${startTime}" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})"/>
						~
					<input value="${endTime}" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})"/></td>
				    <td>&nbsp;&nbsp;<a  onclick="searchAll()" href="javascript:;" class="easyui-linkbutton" >查询</a></td>
					<td>&nbsp;&nbsp;<a  onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a></td>
				</tr>
			</table>
		<form>
		
	</div>
	<br/>

	<table id="s_data" style=""></table>	        
</div>
<script>
var nodeId=$.getUrlParam('nodes');
$('#nodeId').val(nodeId);

$(function(){
	  loadDatagrid();
 });
 
function loadDatagrid(){
  
    $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/refund/jsonMwebAutomaticRefundList',
            loadMsg:'正在装载数据...',
            queryParams:{
            	
            	
            },
            fitColumns:true,
            remoteSort: true,
            pagination: true, //显示最下端的分页工具栏
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'id', align:'center',checkbox:true},
                {field:'rderId',    title:'订单ID', align:'center'},
                {field:'number',    title:'订单编号', align:'center'},
                {field:'orderChannel',    title:'订单渠道', width:25, align:'center'},
                {field:'orderTime',    title:'下单时间', width:25, align:'center'},
                {field:'payTime',     title:'付款时间',  width:25,  align:'center'},
                {field:'payTid',     title:'交易号',  width:50,  align:'center'},
                {field:'payMark',     title:'平台交易号',  width:50,   align:'center'},
                {field:'payChannel',     title:'付款渠道',  width:25,   align:'center'},
                {field:'realPrice',     title:'实付金额',  width:25,   align:'center'},
                {field:'totalPrice',     title:'总价',  width:25,   align:'center'},
                {field:'refundPrice',     title:'退款金额',  width:25,   align:'center'},
                {field:'paymentAccount',     title:'支付账户号',  width:25,   align:'center'},
                {field:'createTime',     title:'退款时间',  width:25,   align:'center'} 
            ]],
          toolbar: [
            
            ]
       });
}

 function searchAll(){

     var pjson=sy.serializeObject($("#searchDivForm").form());

      $("#s_data").datagrid("load",pjson );//将searchForm表单内的元素序列为对象传递到后台
    }
    
function exportAll() {
	$('#searchDivForm').attr("action", "${rc.contextPath}/refund/exportMwebAutomaticRefundList").submit();
}
</script>

</body>
</html>