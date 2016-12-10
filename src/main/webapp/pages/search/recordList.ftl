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

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">

    <div id="cc" class="easyui-layout" data-options="fit:true" >
        <div data-options="region:'north',title:'搜索记录',split:true" style="height: 100px;padding-top:10px">
            <form id="searchForm" action="${rc.contextPath}/search/exportRecord" method="post" >
                <table class="search">
                    <tr>
                        <td class="searchName">搜索时间：</td>
                        <td class="searchText">
                            <input value="" id="startTimeBegin" name="startTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                            -
                            <input value="" id="startTimeEnd" name="startTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                        </td>
                        <td class="searchName">关键词：</td>
                        <td class="searchText"><input id="keyword" name="keyword" value="" /></td>
                        <td class="searchText">
                            &nbsp;<a id="searchBtn" onclick="searchData()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a>
                        </td>
                        <td class="searchText">
                            &nbsp;&nbsp;<a id="searchBtn" onclick="exportCouponCodeDetail()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出结果</a>
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

    function searchData() {
        $('#s_data').datagrid('load', {
            startTimeBegin : $("#searchForm input[name='startTimeBegin']").val(),
            startTimeEnd : $("#searchForm input[name='startTimeEnd']").val(),
            keyword : $("#searchForm input[name='keyword']").val()
        });
    }

    function exportCouponCodeDetail(){
        $('#searchForm').submit();
    }

	$(function(){
        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/search/jsonSearchRecord',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'accountId',    title:'用户ID', width:50, align:'center'},
                {field:'accountName',    title:'用户名', width:80, align:'center'},
                {field:'keyword',    title:'关键词', width:80, align:'center'},
                {field:'resultNum',    title:'搜索结果数', width:80, align:'center'},
                {field:'createTime',     title:'搜索时间',  width:80,   align:'center' }
            ]],
            pagination:true
        });

	});
</script>

</body>
</html>