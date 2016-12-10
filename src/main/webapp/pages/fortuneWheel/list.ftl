<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>换吧网络-大转盘管理</title>
    <link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
    <link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
    <script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
    <style type="text/css">
        .searchName{
            padding-right:3px;
            text-align:right;
        }
        .searchText{
            padding-left:3px;
            text-align:justify;
        }
        .search{
            /*width:1340px;*/
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
        <div data-options="region:'north',title:'大转盘',split:true" style="height: 150px;">
            <div id="searchDiv" style="height: 120px;padding: 15px">
                <form id="searchForm" action="${rc.contextPath}/channelOrder/export" method="post" >
                    <table class="search">
                        <tr>
                            <td class="searchName">活动Id：</td>
                            <td class="searchText"><input id="wheelId" name="id" type="number"  /></td>
                            <td class="searchName">活动名称：</td>
                            <td class="searchText"><input id="name" name="name"  /></td>
                            <td class="searchName">状态：</td>
                            <td class="searchText"><input id="isAvailable" name="isAvailable" class="easyui-combobox"  /></td>

                        </tr>
                        <tr>
                            <td class="searchName">创建时间：</td>
                            <td class="searchText">
                                <input value="" id="createTimeBegin" name="createTimeStart" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                                至
                                <input value="" id="createTimeEnd" name="createTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                            </td>
                            <td class="searchName">开始时间：</td>
                            <td class="searchText">
                                <input value="" id="startTimeBegin" name="paytimeStart" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                                至
                                <input value="" id="startTimeEnd" name="paytimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                            </td>
                            <td class="searchName"></td>
                            <td class="searchText">
                                <a id="searchBtn" onclick="toSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                                &nbsp;
                                <a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
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
    function toSearch(){
        $('#s_data').datagrid('reload',{
            id:$("#wheelId").val(),
            name:$("#name").val(),
            isAvailable:$("#isAvailable").combobox('getValue'),
            createTimeBegin:$("#createTimeBegin").val(),
            createTimeEnd:$("#createTimeEnd").val(),
            startTimeBegin:$("#startTimeBegin").val(),
            startTimeEnd:$("#startTimeEnd").val(),
        });
    }

    function editForm(id) {
        window.open('${rc.contextPath}/fortuneWheel/addUpdate?id=' + id);
    }

    function clearSearch(){
        $("#wheelId").val('');
        $("#name").val('');
        $("#isAvailable").combobox('clear');
        $("#createTimeBegin").val('');
        $("#createTimeEnd").val('');
        $("#startTimeBegin").val('');
        $("#startTimeEnd").val('');
        $('#s_data').datagrid('reload',{
        });
    }

    function preview(id){
        alert(id);
        window.location.href = "http://www.baidu.com";
    }

    function display(id, isAvailable) {
        var a ='不可用';
        if(isAvailable == 1){
            a = "可用";
        }
        $.messager.confirm("确认", "确定"+a+"吗?", function (r) {
            if(r){
                $.post("${rc.contextPath}/fortuneWheel/update", {id: id, isAvailable: isAvailable}, function (data) {
                    $.messager.progress('close');
                    if (data.status == 1) {
                        $('#s_data').datagrid('reload');
                    } else {
                        $.messager.alert('响应信息', data.message, 'error', function () {
                            return
                        });
                    }
                });
            }
        });
    }



    $(function() {
        $('#isAvailable').combobox({
            panelWidth: 50,
            panelHeight: 80,
            data: [
                {"code": -1, "text": "全部"},
                {"code": 1, "text": "可用", "selected": true},
                {"code": 0, "text": "不可用"},
            ],
            valueField: 'code',
            textField: 'text'
        });

        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/fortuneWheel/jsonInfo',
            loadMsg:'正在装载数据...',
            queryParams:{
                isAvailable:1
            },
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'活动ID', width:15, align:'center'},
                {field:'isAvailable',    title:'状态', width:18, align:'center',
                    formatter: function (value, row, index) {
                        if (row.isAvailable == 1) {
                            return "可用";
                        } else {
                            return "不可用";
                        }
                    }
                },
                {field:'name',    title:'活动名称', width:30, align:'center'},
                {field:'createTime',    title:'创建时间', width:30, align:'center'},
                {field:'startTime',     title:'开始时间',  width:40,  align:'center'},
                {field:'endTime',     title:'结束时间',  width:40,   align:'center'},
                {field: 'hidden', title: '操作', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                        var v1 = '<a href="javascript:;" onclick="editForm(' + row.id + ')">编辑</a>';
                        var v2 = ' | <a href="javascript:;" onclick="preview(' + row.id + ')">预览</a>';
                        var v3 = '';
                        if (row.isAvailable == 1) {
                            v3 = ' | <a href="javascript:;" onclick="changeAvailable(' + row.id + ', 0)">停用</a>';
                        } else {
                            v3 = '| <a href="javascript:;" onclick="changeAvailable(' + row.id + ', 1)">启用</a>';
                        }
                        return v1 + v2 + v3 ;
                        }
                    }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                    window.open('${rc.contextPath}/fortuneWheel/addUpdate');
                }
            }
            ],
            pagination:true,
            onLoadSuccess:function(){
                $("#s_data").datagrid('clearSelections');
            }
        });
    })

</script>

</body>
</html>