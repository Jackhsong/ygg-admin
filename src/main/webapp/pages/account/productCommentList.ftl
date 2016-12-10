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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'商品评论管理',split:true" style="height: 150px;">
			<br>
			<form id="searchForm" action="${rc.contextPath}/comment/export" method="post">
				<table>
					<tr>
						<td>&nbsp;用户ID：</td>
						<td><input id="searchAccountId" name="searchAccountId" value="" /></td>
						<td>&nbsp;用户名：</td>
						<td><input id="searchUsername" name="searchUsername" value="" /></td>
						<td>&nbsp;商品ID：</td>
						<td><input id="searchProductId" name="searchProductId" value="" /></td>
						<td>&nbsp;商品名称：</td>
						<td><input id="searchProductName" name="searchProductName" value="" /></td>
                        <td>&nbsp;商家：</td>
                        <td><input id="sellerId" type="text" name="sellerId" /></td>
					</tr>
                    <tr>
                        <td>&nbsp;总体印象：</td>
                        <td>
                            <select id="searchLevel" name="searchLevel" value="" >
                                <option value="0">全部</option>
                                <option value="1">差评</option>
                                <option value="2">中评</option>
                                <option value="3">好评</option>
                            </select>
                        </td>
                        <td>&nbsp;评论内容：</td>
                        <td><input id="searchComment" name="searchComment" value="" /></td>
                        <td>&nbsp;品牌：</td>
                        <td><input id="brandId" type="text" name="brandId" ></input></td>
                        <td>&nbsp;<a id="searchBtn" onclick="searchComment()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a></td>
                        <td>&nbsp;&nbsp;<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" >重置</a></td>
                        <td><a id="exportBtn" onclick="exportData()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出结果</a></td>
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

    function exportData(){
        $('#searchForm').submit();
    }

function searchComment(){
    
	$('#s_data').datagrid('load', {
		accountId : $("#searchForm input[name='searchAccountId']").val(),
		username : $("#searchForm input[name='searchUsername']").val(),
		comment : $("#searchForm input[name='searchComment']").val(),
		productId : $("#searchForm input[name='searchProductId']").val(),
		productName : $("#searchForm input[name='searchProductName']").val(),
        sellerId:$("input[name='sellerId']").val(),
        brandId:$("input[name='brandId']").val(),
		level : $("#searchLevel").val()
	});
}

function clearSearch(){
	$("#searchForm input[name='searchAccountId']").val('');
	$("#searchForm input[name='searchUsername']").val('');
	$("#searchForm input[name='searchComment']").val('');
	$("#searchForm input[name='searchProductId']").val('');
	$("#searchForm input[name='searchProductName']").val('');
    $("#sellerId").combobox('clear');
    $("#brandId").combobox('clear');
	$("#searchLevel").find('option').eq(0).attr('selected','selected');
	$('#s_data').datagrid('load',{});
}

$(function(){

    $('#brandId').combobox({
        panelWidth:350,
        panelHeight:350,
        url:'${rc.contextPath}/brand/jsonBrandCode',
        valueField:'code',
        textField:'text'
    });

    $('#sellerId').combobox({
        panelWidth:350,
        panelHeight:350,
        mode:'remote',
        url:'${rc.contextPath}/seller/jsonSellerCode',
        valueField:'code',
        textField:'text'
    });
	
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/comment/jsonProductCommentInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,100],
        columns:[[
            {field:'orderNo',    title:'订单号', width:30, align:'center',
            	formatter : function(value, row, index) {
					return '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.orderId+'">'+row.orderNo+'</a>';
				}	
           	},
            {field:'accountId',    title:'用户Id', width:20, align:'center'},
            {field:'username',     title:'用户名',  width:20,   align:'center' },
            {field:'typeDesc',    title:'商品类型', width:20, align:'center'},
            {field:'productId',    title:'商品Id', width:20, align:'center'},
            {field:'productNameUrl',    title:'商品名称', width:80, align:'center'},
            {field:'realSellerName',    title:'商家', width:50, align:'center'},
            {field:'productAmount',    title:'购买数量', width:20, align:'center'},
            {field:'levelDesc',    title:'总体印象', width:20, align:'center'},
            {field:'content',    title:'商品评论', width:100, align:'center'},
            {field:'imageAmount',    title:'上传图片', width:20, align:'center'},
            {field:'hidden',  title:'操作', width:30,align:'center',
                formatter:function(value,row,index){
                    return '<a target="_blank" href="${rc.contextPath}/comment/productCommentDetail/'+row.id+'">查看</a>';
                }
            }
        ]],
        pagination:true
    });
});
</script>

</body>
</html>