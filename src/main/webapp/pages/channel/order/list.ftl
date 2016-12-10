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
    </style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
    <input type="hidden" value="${nodes!0}" id="nowNode"/>
<#include "../../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
    <div id="cc" class="easyui-layout" data-options="fit:true" >
        <div data-options="region:'north',title:'第三方订单列表',split:true" style="height: 150px;">
            <div id="searchDiv" style="height: 120px;padding: 15px">
                <form id="searchForm" action="${rc.contextPath}/channelOrder/export" method="post" >
                    <table class="search">
                        <tr>
                            <td class="searchName">渠道：</td>
                            <td class="searchText"><input id="search_channel_id" name="channelId"  /></td>
                            <td class="searchName">仓库：</td>
                            <td class="searchText"><input id="search_warehouse_type" name="warehouseType"  /></td>
                            <td class="searchName">付款时间：</td>
                            <td class="searchText">
                                <input value="" id="startTimeBegin" name="paytimeStart" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                                	至
                                <input value="" id="startTimeEnd" name="paytimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                            </td>
                            <td class="searchName">收货人姓名：</td>
                            <td class="searchText"><input id="search_receiver" name="receiver"  /></td>
                        </tr>
                        <tr>
                            <td class="searchName">订单编号：</td>
                            <td class="searchText"><input id="search_number" name="number" /></td>
                            <td class="searchName">收货人手机号：</td>
                            <td class="searchText"><input id="search_phone" name="phone" /></td>
                            <td class="searchName">商品编码：</td>
                            <td class="searchText"><input id="search_code" name="code" /></td>
                            <td class="searchName">商品名称：</td>
                            <td class="searchText"><input id="search_name" name="name"/></td>
                        </tr>
                        <tr>
                            <td class="searchName"></td>
                            <td class="searchText">
                                <a id="searchBtn" onclick="searchChannelOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                                &nbsp;
                                <a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
                            </td>
                            <td class="searchText">
                                &nbsp;
                                <a id="exportBtn" onclick="exportProduct()" href="javascript:;" class="easyui-linkbutton" >导出查询结果</a>
                            </td>
                        </tr>
                    </table>
                </form>
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
    function exportProduct(){
        $.messager.confirm("确认要导出吗?", "确认要导出吗?", function (r) {
            if (r) {
                $("#searchForm").submit();
            }
        });
    }
    function checkEnter(e){
        var et=e||window.event;
        var keycode=et.charCode||et.keyCode;
        if(keycode==13){
            if(window.event)
                window.event.returnValue = false;
            else
                e.preventDefault();//for firefox
        }
    }
    function searchChannelOrder(){
        $('#s_data').datagrid('load',{
            channelId:$("#search_channel_id").combobox('getValue'),
            warehouseType:$("#search_warehouse_type").combobox('getValue'),
            paytimeStart:$("#startTimeBegin").val(),
            paytimeEnd:$("#startTimeEnd").val(),
            receiver:$("#search_receiver").val(),
            number:$("#search_number").val(),
            phone:$("#search_phone").val(),
            code:$("#search_code").val(),
            name:$("#search_name").val(),
        });
    }

    function clearSearch(){
        $("#search_channel_id").combobox('clear');
        $("#search_warehouse_type").combobox('clear');
        $("#startTimeBegin").val('');
        $("#startTimeEnd").val('');
        $("#search_receiver").val('');
        $("#search_number").val('');
        $("#search_phone").val('');
        $("#search_code").val('');
        $('#search_name').val('');
        $('#s_data').datagrid('load',{
        });
    }

    $(function() {
        $('#search_channel_id').combobox({
            url: '${rc.contextPath}/channel/jsonChannelCode',
            valueField: 'code',
            textField: 'text'
        });

        $('#search_warehouse_type').combobox({
            url:'${rc.contextPath}/channelProManage/jsonWareHouseCode',
            valueField: 'code',
            textField: 'text'
        });

        $('#s_data').datagrid({
                    nowrap: false,
                    striped: true,
                    collapsible:true,
                    idField:'id',
                    url:'${rc.contextPath}/channelOrder/jsonChannelInfo',
                    loadMsg:'正在装载数据...',
                    queryParams:{
                    },
                    fitColumns:true,
                    remoteSort: true,
                    pageSize:50,
                    pageList:[50,60],
                    columns:[[
                        {field:'channelName',    title:'渠道名', width:15, align:'center'},
                        {field:'number',    title:'订单编号', width:18, align:'center'},
                        {field:'orderPayTime',    title:'付款时间', width:30, align:'center'},
                        {field:'receiver',    title:'收货人姓名', width:30, align:'center'},
                        {field:'phone',     title:'收货人手机号',  width:40,  align:'center'},
                        {field:'productCode',     title:'商品编码',  width:40,   align:'center'},
                        {field:'productName',     title:'商品名称',  width:60,   align:'center'},
                        {field:'price',     title:'商品单价',  width:30,   align:'center'},
                        {field:'count',     title:'件数',  width:30,   align:'center' },
                        {field:'productTotalPrice',     title:'商品总价',  width:25,   align:'center' },
                        {field:'orderRealPrice',     title:'订单金额',  width:30,   align:'center' },
                        {field:'warehouseName',     title:'发货仓库',  width:20,   align:'center' },
                    ]],
                    pagination:true,
                    onLoadSuccess:function(){
                        $("#s_data").datagrid('clearSelections');
                    }
        });
    })

</script>

</body>
</html>