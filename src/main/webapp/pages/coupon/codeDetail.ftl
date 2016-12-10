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
	width:1100px;
	align:center;
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
		<div data-options="region:'north',title:'优惠码详细--<#if te?exists && te==1>单次<#else>多次</#if>',split:true" style="height: 150px;padding-top:10px">
	        <form id="searchForm" action="${rc.contextPath}/couponCode/exportCouponCodeDetail" method="post" >
	        	<input type="hidden" value="${id}" name="couponCodeId"/>
	        	<table>
					<tr>
					<#if te?exists && te==1>
						<td>优惠券码：</td>
						<td><input name="code" /></td>						
					</#if>
						<td>&nbsp;用户名：</td>
						<td><input name="name" /></td>
					<#if te?exists  && te==1>
						<td>兑换状态：</td>	
						<td>
							<select name="convertType" id="searchConvertType">
								<option value="-1">全部</option>
								<option value="1">已兑换</option>
								<option value="0">未兑换</option>
							</select>
						</td>			
					</#if>
						<td>使用状态：</td>
						<td>
							<select name="usedType" id="searchUsedType">
								<option value="-1">全部</option>
								<option value="1">已使用</option>
								<option value="0">未使用</option>
							</select>
						</td>
						<td>&nbsp;<a id="searchBtn" onclick="searchCouponCodeDetail()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
						<td>&nbsp;&nbsp;<a id="clearBtn" onclick="exportCouponCodeDetail()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出结果</a></td>
					</tr>
				</table>
	        </form>
	        
	        <p style="color:red">本次优惠码共产生订单交易额：${totalAmount!"0"}元</p>
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

	function searchCouponCodeDetail() {
		$('#s_data').datagrid('load', {
			couponCodeId : $("#searchForm input[name='couponCodeId']").val(),
			code : $("#searchForm input[name='code']").val(),
			name : $("#searchForm input[name='name']").val(),
			convertType : $("#searchConvertType").val(),
			usedType : $("#searchUsedType").val()
		});
	}
	
	function exportCouponCodeDetail(){
		$('#searchForm').submit();
	}

	function showDetail(type,couponAccountId){
		var url = "${rc.contextPath}/couponCode/couponCodeOrderDetail/"+type+"/"+couponAccountId;
		window.open(url,'_blank');
	}

	$(function(){
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        url:'${rc.contextPath}/couponCode/jsonCouponCodeDetail',
	        queryParams:{couponCodeId:${id}},
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        singleSelect:true,
	        pageSize:50,
	        pageList:[50,100],
	        columns:[[
	            {field:'code',    title:'优惠券码', width:40, align:'center'},
	            {field:'accountName',    title:'用户名', width:40, align:'center'},
	            {field:'accountTypeStr',    title:'用户类型', width:30, align:'center'},
	            {field:'couponTypeStr',    title:'优惠券类型', width:80, align:'center'},
	            {field:'reducePrice',    title:'金额', width:20, align:'center'},
	            {field:'limitPeople',    title:'限用人数', width:20, align:'center'},
	            {field:'convert',     title:'兑换状态',  width:20,   align:'center' },
	            {field:'used',     title:'使用状态',  width:20,   align:'center' },
	            {field:'hidden',  title:'操作', width:40,align:'center',
	                formatter:function(value,row,index){
	                    var a = '';
	                    if(row.usedCode == 1){
	                    	a = '<a href="javaScript:;" onclick="showDetail(' + row.type + ','+ row.couponAccountId + ')">查看使用情况</a>';
	                    }
	                    return a;
	                }
	            }
	        ]],
	        pagination:true
	    });
	});
</script>
</body>
</html>