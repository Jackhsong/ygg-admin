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
#searchForm span{
	margin-left: 5px;
	margin-right: 5px;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center',title:'用户优惠券管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'',split:true" style="height: 180px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<input type="hidden" id="isSearch" value="0"/>
	        	<font color="red">温馨提示：请选择条件后查询，否则由于数据较多，会等待较长时间</font>
	        	<p>
	        		<span>用户ID：<input type="text" id="search_accountId" name="search_accountId" value=""/></span>
	        		<span>用户名：<input type="text" id="search_accountName" name="search_accountName" value=""/></span>
	        		<span>手机号：<input type="text" id="search_phoneNumber" name="search_phoneNumber" value=""/></span>
	        		<span>备注：<input type="text" id="search_couponRemark" name="search_couponRemark" value=""/></span>
	        	</p>
	        	<p>
	        		<span>获得时间：<input type="text" id="search_acquireTimeBegin" name="search_acquireTimeBegin" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/> - <input type="text" id="search_acquireTimeEnd" name="search_acquireTimeEnd" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/></span>
	        		<span>使用状态：<select id="search_isUsed" name="search_isUsed"><option value="-1">全部</option><option value="1">已使用</option><option value="0">未使用</option></select></span>
	        		<!-- <span>优惠类型：<select id="search_couponDetailType" name="search_couponDetailType"><option value="-1">全部</option><option value="1">满x减y优惠券</option><option value="2">x元现金券</option></select></span>
	        		<span>优惠券面额：<input type="text" id="search_reduceMin" name="search_reduceMin" value="" style="width: 120px;"/> - <input type="text" id="search_reduceMax" name="search_reduceMax" value="" style="width: 120px;"/></span> -->
	        	</p>
	        	<p>
	        		<span>有效期起：<input type="text" id="search_validTimeBeginStart" name="search_validTimeBeginStart" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/> - <input type="text" id="search_validTimeBeginEnd" name="search_validTimeBeginEnd" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/></span>
	        		<span>有效期止：<input type="text" id="search_validTimeEndStart" name="search_validTimeEndStart" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/> - <input type="text" id="search_validTimeEndEnd" name="search_validTimeEndEnd" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/></span>
	        		<span>显示名称：<input type="text" id="search_couponDetailRemark" name="search_couponDetailRemark" value=""/></span>
	        	</p>
	        	<p>
	        		<span><a id="searchBtn" onclick="searchCoupon()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></span>
	        		<span><a id="clearBtn" onclick="clearCoupon()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >重置</a></span>
	        	</p>
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

function searchCoupon(){
	var accountId = $("#search_accountId").val();
	var phoneNumber = $("#search_phoneNumber").val();
	var accountName = $("#search_accountName").val();
	if($.trim(accountId)=='' && $.trim(accountName)=='' && $.trim(phoneNumber)==''){
		$.messager.alert("提示","请输入要查询的用户ID或用户名或手机号","info");
		return false;
	}
	$("#isSearch").val("1");
	$('#s_data').datagrid('load',{
		accountId:$("#search_accountId").val(),
		accountName:$("#search_accountName").val(),
		phoneNumber:$("#search_phoneNumber").val(),
		couponRemark:$("#search_couponRemark").val(),
		acquireTimeBegin:$("#search_acquireTimeBegin").val(),
		acquireTimeEnd:$("#search_acquireTimeEnd").val(),
		isUsed:$("#search_isUsed").val(),
		couponDetailType:$("#search_couponDetailType").val(),
		reduceMin:$("#search_reduceMin").val(),
		reduceMax:$("#search_reduceMax").val(),
		validTimeBeginStart:$("#search_validTimeBeginStart").val(),
		validTimeBeginEnd:$("#search_validTimeBeginEnd").val(),
		validTimeEndStart:$("#search_validTimeEndStart").val(),
		validTimeEndEnd:$("#search_validTimeEndEnd").val(),
		couponDetailRemark:$("#search_couponDetailRemark").val(),
		isSearch:$("#isSearch").val()
	});
}

function clearCoupon(){
	//$("#search_accountId").val("");
	$("#search_accountName").val("");
	$("#search_phoneNumber").val("");
	$("#search_couponRemark").val("");
	$("#search_acquireTimeBegin").val("");
	$("#search_acquireTimeEnd").val("");
	$("#search_isUsed").find("option").eq(0).attr("selected","selected");
	$("#search_couponDetailType").find("option").eq(0).attr("selected","selected");
	$("#search_reduceMin").val("");
	$("#search_reduceMax").val("");
	$("#search_validTimeBeginStart").val("");
	$("#search_validTimeBeginEnd").val("");
	$("#search_validTimeEndStart").val("");
	$("#search_validTimeEndEnd").val("");
	$("#isSearch").val("0");
}


$(function(){
	
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'couponAccountId',
        url:'${rc.contextPath}/account/jsonAccountCouponList',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:30,
        columns:[[
            {field:'accountId',    title:'用户ID', width:15, align:'center'},
            {field:'accountName',    title:'用户名', width:20, align:'center'},
            {field:'phoneNumber',    title:'手机号', width:20, align:'center'},
            {field:'acquireTime',    title:'获得时间', width:35, align:'center'},
            {field:'isUsedStr',    title:'使用状态', width:15, align:'center'},
            {field:'couponDetailtypeStr',    title:'优惠券类型', width:50, align:'center'},
            {field:'reduce',    title:'面额', width:10, align:'center'},
            {field:'couponScope',    title:'使用范围', width:20, align:'center'},
            {field:'validTimeBegin',    title:'有效期起', width:35, align:'center'},
            {field:'validTimeEnd',    title:'有效期止', width:35, align:'center'},
            {field:'couponDetailRemark',    title:'优惠券显示名称', width:40, align:'center'},
            {field:'couponRemark',    title:'备注', width:30, align:'center'},
            {field:'hidden',  title:'操作', width:10,align:'center',
                formatter:function(value,row,index){
                	if(row.isUsed == 1){
                		return '<a href="${rc.contextPath}/coupon/couponOrderDetailList/'+row.couponAccountId+'">查看</a>';
                	}else{
                		return '-';
                	}
                }
            }
        ]],
     	pagination:true
    });
    
});
</script>

</body>
</html>