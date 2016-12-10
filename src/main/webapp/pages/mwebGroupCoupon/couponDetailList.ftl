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
<div data-options="region:'center',title:'心动慈露优惠券管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 150px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/product/exportResult" method="post" >
		            <table class="search">
		                <tr>
		                	<td class="searchName">用户ID：</td>
							<td class="searchText"><input id="searchAccountId" name="searchAccountId" value="" /></td>
		                    <td class="searchName">手机号：</td>
		                    <td class="searchText"><input id="searchPhoneNumber" name="searchPhoneNumber" value="" /></td>
		                    <td class="searchName">有效期起：</td>
		                    <td class="searchText">
								<input value="" id="startTimeBegin" name="startTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								-
								<input value="" id="startTimeEnd" name="startTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
							</td>
		                </tr>
		                <tr>
		                	<td class="searchName">用户名：</td>
		                    <td class="searchText"><input id="searchUserName" name="searchUserName" value="" /></td>
		                    <td class="searchName">是否使用：</td>
							<td class="searchText">
								<select id="searchIsUsed" name="searchIsUsed" style="width:173px;">
									<option value="">全部</option>
									<option value="1">已使用</option>
									<option value="0">未使用</option>
								</select>
							</td>
							<td class="searchName">有效期止：</td>
							<td class="searchText">
								<input value="" id="endTimeBegin" name="endTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								-
								<input value="" id="endTimeEnd" name="endTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
							</td>
		                </tr>
						<tr>
							
		                	<td class="searchName"></td>
							<td class="searchText">
								<a id="searchBtn" onclick="searchDetail()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
		                	</td>
		                	<td class="searchName"></td>
							<td class="searchText"></td>
							<td class="searchName"></td>
		                    <td class="searchText"></td>
						</tr>
						<tr>
							<td colspan="6">
								<br/>
								<span style="padding: 5px;color:red;font-size: 15px;">本次优惠券共有${useAmount}人使用、产生订单交易额${totalAmount!"0"}元</span>
							</td>
						</tr>
		            </table>
		        </form>
		        
		        <p style="color:red"></p>
		    </div>
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

	function searchDetail(){
    	$('#s_data').datagrid('load',{
    		couponId:${couponId},
    		accountId:$("#searchAccountId").val(),
    		username:$("#searchUserName").val(),
        	phoneNumber:$("#searchPhoneNumber").val(),
        	startTimeBegin:$("#startTimeBegin").val(),
        	startTimeEnd:$("#startTimeEnd").val(),
        	endTimeBegin:$("#endTimeBegin").val(),
        	endTimeEnd:$("#endTimeEnd").val(),
        	isUsed:$("#searchIsUsed").val()
    	});
	}
	
	function clearSearch(){
		$("#searchAccountId").val('');
		$("#searchUserName").val('');
    	$("#searchPhoneNumber").val('');
    	$("#startTimeBegin").val('');
    	$("#startTimeEnd").val('');
    	$("#endTimeBegin").val('');
    	$("#endTimeEnd").val('');
    	isUsed:$("#searchIsUsed").find("option").eq(0).attr('selected','selected');
    	$('#s_data').datagrid('load',{
    		couponId:${couponId}
    	});
	}
	
	function viewDetail(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr="${rc.contextPath}/mwebGroupCoupon/couponOrderDetailList/"+arr.rows[index].id;
		window.open(urlStr,"_blank");
	}
	
	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            rownumbers:true,
            idField:'id',
            url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponAccountInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            queryParams:{couponId:${couponId}},
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'accountId',    title:'用户ID', width:20, align:'center'},
                {field:'teamAccountId',    title:'心动慈露用户ID', width:20, align:'center'},
                {field:'nickName',    title:'用户名', width:50, align:'center'},
                {field:'phoneNumber',    title:'手机号', width:50, align:'center'},
                {field:'remark',    title:'备注', width:70, align:'center'},
                {field:'startTime',     title:'有效期起',  width:50,  align:'center'},
                {field:'endTime',     title:'有效期止',  width:50,   align:'center'},
                {field:'isUsed',     title:'是否使用',  width:50,   align:'center'},
                {field:'id',     hidden:true},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                    	if(row.usedCode=='0'){
                    		return '-';
                    	}else if(row.usedCode=='1'){
		                    return '<a href="javaScript:;" onclick="viewDetail(' + index + ')">查看</a>';
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