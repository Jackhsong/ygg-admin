<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>商品使用渠道</title>
    <link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet"
          type="text/css"/>
    <link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css"/>
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

<div data-options="region:'center'" style="padding:5px;">
    <div id="cc" class="easyui-layout" data-options="fit:true">
        <div data-options="region:'center'">
            <!--数据表格-->
            <table id="s_data" style=""></table>

            <!-- 新增编辑使用渠道 Begin -->
            <div id="useScopeDiv" class="easyui-dialog" style="width:600px;height:300px;padding:15px 20px;">
                <form id="adduseScopeForm" method="post">
                    <input id="form_id" type="hidden" name="id">

                    <p>
                        <span>分类名称：</span>
                        <span><input type="text" id="scopeName" name="name" style="width: 200px;"/></span>
                    </p>
                    <p>
                        <span>状态：</span>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="text" id="isAvailable" name="isAvailable" style="width: 200px;"/></span>
                    </p>
                </form>
            </div>
            <!-- 新增编辑商城国家 End -->

        </div>
    </div>
</div>

<script>
    function editForm(index) {
        cleanAddFrom();
        var arr = $("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var name = arr.rows[index].name;
        var isAvailable = arr.rows[index].isAvailable;

        $('#form_id').val(id);
        $('#scopeName').val(name);
        $('#isAvailable').combobox('setValue', isAvailable);
        $('#useScopeDiv').panel({title:"编辑"});

        $("#useScopeDiv").dialog("open");
    }

    function cleanAddFrom() {
        $("#form_id").val('');
        $("#scopeName").val('');
        $("#isAvailable").combobox('clear');
    }


    //单个显示或者不显示
    function display(id, isAvailable) {
        var a ='不可用';
        if(isAvailable == 1){
            a = "可用";
        }
        $.messager.confirm("确认", "确定"+a+"吗?", function (r) {
            if(r){
                $.post("${rc.contextPath}/productUseScope/saveOrUpdate", {id: id, isAvailable: isAvailable}, function (data) {
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


    // 单个删除
    function deleteIt(id) {
        $.messager.confirm("确认", "确定要<span style='color:red'>删除</span>吗?", function (r) {
            if (r) {
                $.post("${rc.contextPath}/productUseScope/delete", {id: id}, function (data) {
                    $.messager.progress('close');
                    if (data.status == 1) {
                        $.messager.alert('响应信息', "删除成功...", 'info', function () {
                            $('#s_data').datagrid('reload');
                            return
                        });
                    } else {
                        $.messager.alert('响应信息', data.message, 'error', function () {
                            return
                        });
                    }
                })
            }
        });
    }


    <!--加载Grid-->
    $(function () {
        $('#isAvailable').combobox({
            panelWidth: 50,
            panelHeight: 80,
            data: [
                {"code": 1, "text": "可用", "selected": true},
                {"code": 0, "text": "不可用"},
            ],
            valueField: 'code',
            textField: 'text'
        });



        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible: true,
            idField: 'id',
            url: '${rc.contextPath}/productUseScope/jsonListInfo',
            queryParams : {
            },
            loadMsg: '正在装载数据...',
            singleSelect: true,
            fitColumns: true,
            remoteSort: true,
            pageSize: 50,
            pageList: [50],
            columns: [[
                {field: 'id', title: '分类Id', width: 50, align: 'center'},
                {field: 'name', title: '分类名称)', width: 50, align: 'center'},
                {field: 'status', title: '状态', width: 50, align: 'center',
                },
                {field: 'hidden', title: '操作', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                            var v1 = '<a href="javascript:;" onclick="editForm(' + index + ')">编辑</a>';
                            if (row.isAvailable == 1) {
                                var v4 = ' | <a href="javascript:;" onclick="display(' + row.id + ', 0)">不可用</a>';
                            } else {
                                var v4 = '| <a href="javascript:;" onclick="display(' + row.id + ', 1)">可用</a>';
                            }
                            var v5 = ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                            return v1   + v4 + v5;
                        }
                    }
            ]],
            toolbar: [
                {
                    id: '_add',
                    text: '新增',
                    iconCls: 'icon-add',
                    handler: function () {
                        cleanAddFrom();
                        $('#useScopeDiv').panel({title:"新增"});
                        $('#isAvailable').combobox('setValue', 1);
                        $('#useScopeDiv').dialog('open');
                    }
                },
            ],
            pagination: true
        });

        <!--新增编辑大牌-->
        $('#useScopeDiv').dialog({
            title: "",
            height: 300 ,
            width: 400 ,
            collapsible: true,
            closed: true,
            modal: true,
            resizable:true,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var  isAvailable = $('#isAvailable').combobox("getValue");
                        var name = $.trim($("#scopeName").val());
                        if (name == '') {
                            $.messager.alert('提示', '请填写描述', 'info');
                            return false;
                        }
                        else if ($.trim(isAvailable) == '') {
                            $.messager.alert('提示', '请选择状态', 'info');
                            return false;
                        }
                        $.ajax({
                            type: "POST",
                            url: "${rc.contextPath}/productUseScope/saveOrUpdate",
                            data: {
                                id: $('#form_id').val(),
                                name: name,
                                isAvailable: isAvailable
                            },
                            success: function (res) {
                                if (res.status == 1) {
                                    $.messager.alert('响应信息', "保存成功", 'info', function () {
                                        $('#s_data').datagrid('reload');
                                        $('#useScopeDiv').dialog('close');
                                    });
                                } else if (res.status == 0) {
                                    $.messager.alert('响应信息', res.message, 'error');
                                }
                            },
                        });
                    }
                },
                {
                    text: '取消',
                    align: 'left',
                    iconCls: 'icon-cancel',
                    handler: function () {
                        cleanAddFrom();
                        $('#useScopeDiv').dialog('close');
                    }
                }]
        });

    });
</script>

</body>
</html>