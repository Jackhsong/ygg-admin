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

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'商品评论管理',split:true" style="height: 150px;">
			<br>
			<form id="searchForm" action="${rc.contextPath}/comment/exportProductBaseComment" method="post">
				<table class="search">
					<tr>
						<td class="searchName">基本商品ID：</td>
						<td class="searchText"><input id="searchProductBaseId" name="searchProductBaseId" value="<#if productBaseId?exists>${productBaseId}</#if>" /></td>
						<td class="searchName">商品名称：</td>
						<td class="searchText"><input id="searchProductName" name="searchProductName" value="" /></td>
                        <td class="searchName">商家：</td>
                        <td class="searchText"><input id="sellerId" type="text" name="sellerId" /></td>
					</tr>
                    <tr>
                        <td class="searchName">品牌：</td>
                        <td class="searchText"><input id="brandId" type="text" name="brandId" ></td>
                        <td class="searchName">好评率：</td>
                        <td class="searchText">
                        	<select id="goodCommentPercent" name="goodCommentPercent">
                        		<option value="-1">全部</option>
                        		<option value="1">99%以上</option>
                        		<option value="2">95%-99%</option>
                        		<option value="3">95%</option>
                        	</select>
                        </td>
                        <td class="searchName"></td>
                        <td class="searchText">
                        	<a id="searchBtn" onclick="searchComment()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a>&nbsp;
                        	<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" >重置</a>&nbsp;
                        	<a id="searchbtn" onclick="exportData()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出结果</a>
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
    function exportData(){
        $('#searchForm').submit();
    }

function searchComment(){
    
	$('#s_data').datagrid('load', {
		productBaseId : $("#searchForm input[name='searchProductBaseId']").val(),
		productBaseName : $("#searchForm input[name='searchProductName']").val(),
        sellerId:$("input[name='sellerId']").val(),
        brandId:$("input[name='brandId']").val(),
        goodCommentPercent : $("#goodCommentPercent").val()
	});
}

function clearSearch(){
	$("#searchForm input[name='searchProductBaseId']").val('');
	$("#searchForm input[name='searchProductName']").val('');
    $("#sellerId").combobox('clear');
    $("#brandId").combobox('clear');
    $("#goodCommentPercent").find("option").eq(0).attr("selected","selected");
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
        url:'${rc.contextPath}/comment/jsonProductBaseCommentInfo',
        loadMsg:'正在装载数据...',
        queryParams:{
            productBaseId:${productBaseId!"0"}
        },
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        columns:[[
            {field:'productBaseId',    title:'基本商品Id', width:20, align:'center'},
            {field:'productBaseName',     title:'商品名称',  width:80,   align:'center' },
            {field:'sellerName',    title:'商家', width:30, align:'center'},
            {field:'goodComment',    title:'好评', width:15, align:'center'},
            {field:'middleComment',    title:'中评', width:15, align:'center'},
            {field:'badComment',    title:'差评', width:15, align:'center'},
            {field:'totalComment',    title:'总评论', width:15, align:'center'},
            {field:'goodPercent',    title:'好评率', width:15, align:'center'}
            /* {field:'hidden',  title:'操作', width:20,align:'center',
                formatter:function(value,row,index){
                    return '<a target="_blank" href="${rc.contextPath}/comment/productCommentDetailList/'+row.productBaseId+'">查看</a>';
                }
            } */
        ]],
        pagination:true
    });
});
</script>

</body>
</html>