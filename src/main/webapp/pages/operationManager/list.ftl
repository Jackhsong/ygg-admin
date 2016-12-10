<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>运营对接人</title>
    <link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css"/>
    <link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css"/>
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
    <script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
    <script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
    <style type="text/css">
        .searchName{
            display:inline-block;
            width:80px;
        }
    </style>
</head>

<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
    <input type="hidden" value="${nodes!0}" id="nowNode"/>
<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding:5px;">
    <div id="cc" class="easyui-layout" data-options="fit:true">
        <div data-options="region:'north',title:'大牌汇管理',split:true" style="height: 110px;">
            <div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
                <form id="searchForm" method="post">
                    <table class="search">
                        <tr>
                            <td>是否可用：</td>
                            <td><input id="searchisAvailable" name="isAvailable" class="easyui-combobox"
                                       style="width:100px;"/></td>
                            <td>
                                <a id="searchBtn" onclick="startSearch()" href="javascript:;" class="easyui-linkbutton"
                                   data-options="iconCls:'icon-search'">查询</a>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>

        <div data-options="region:'center'">
            <!--数据表格-->
            <table id="s_data" style=""></table>

            <!-- 新增编辑 Begin -->
            <div id="addEditDiv" class="easyui-dialog" style="width:620px;height:340px;padding:15px 20px;">
                <form id="addEditForm" method="post">
                    <input id="form_id" type="hidden" name="id">

                    <p>
                        <span class="searchName">分类：</span>
                        <span><input type="text" id="categoryFirstId" name="categoryFirstId" style="width: 200px;"/></span>
                    </p>

                    <p>
                        <span class="searchName">运营对接人：</span>
                        <span><input type="text" id="name" name="name" style="width: 200px;"/></span>
                    </p>
                    <p>
                        <span class="searchName">qq：</span>
                        <span><input type="text" id="qqNumber" name="qqNumber" style="width: 200px;"/></span>
                    </p>
                    <p>
                        <span class="searchName">手机号：</span>
                        <span><input type="text" id="mobileNumber" name="mobileNumber" style="width: 200px;"/></span>
                    </p>
                    <p>
                        <span class="searchName">是否可用：</span>
                        <span ><input type="text" id="addIsAvailable" name="isAvailable" style="width: 200px;"/></span>
                    </p>
                </form>
            </div>
            <!-- 新增编辑 End -->

        </div>
    </div>
</div>

<script>
    function startSearch() {
        $("#s_data").datagrid("reload", {
            "isAvailable": $("#searchisAvailable").combobox("getValue")
        });
    }

    <!--编辑-->
    function editForm(index) {
        cleanAddFrom();
        var arr = $("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var categoryFirstId = arr.rows[index].categoryFirstId;
        var name = arr.rows[index].name;
        var qqNumber = arr.rows[index].qqNumber;
        var mobileNumber = arr.rows[index].mobileNumber;
        var isAvailable = arr.rows[index].isAvailable;

        $('#form_id').val(id);
        $('#name').val(name);
        $('#qqNumber').val(qqNumber);
        $('#mobileNumber').val(mobileNumber);
        $('#categoryFirstId').combobox('setValue', categoryFirstId);
        $('#addIsAvailable').combobox('setValue', isAvailable);

        $('#addEditDiv').panel({title:"编辑"});
        $("#addEditDiv").dialog("open");
    }

    function cleanAddFrom() {
        $("#form_id").val('');
        $("#name").val('');
        $('#qqNumber').val('');
        $("#mobileNumber").val('');
        $("#categoryFirstId").combobox("clear");
        $("#addIsAvailable").combobox("clear");
    }


    <!--加载Grid-->
    $(function () {
        $('#searchisAvailable').combobox({
            panelWidth: 50,
            panelHeight: 80,
            data: [
                {"code": -1, "text": "全部", },
                {"code": 1, "text": "可用" ,"selected": true},
                {"code": 0, "text": "不可用"},
            ],
            valueField: 'code',
            textField: 'text'
        });

        $('#addIsAvailable').combobox({
            panelWidth: 50,
            panelHeight: 80,
            data: [
                {"code": 1, "text": "可用", "selected": true},
                {"code": 0, "text": "不可用"}
            ],
            valueField: 'code',
            textField: 'text'
        });

        $('#categoryFirstId').combobox({
            url: '${rc.contextPath}/category/jsonCategoryFirstCode?isAvailable=1',
            valueField: 'code',
            textField: 'text',
            multiple: false,
            editable: false,
        });


        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible: true,
            idField: 'id',
            url: '${rc.contextPath}/sellerCategoryOperationManager/jsonInfo',
            queryParams : {
                isAvailable : 1
            },
            loadMsg: '正在装载数据...',
            singleSelect: false,
            fitColumns: true,
            remoteSort: true,
            pageSize: 50,
            pageList: [50],
            columns: [[
                {field: 'id', title: 'ID', width: 30, align: 'center'},
                {field: 'name', title: '姓名', width: 50, align: 'center'},
                {field: 'categoryFirstName', title: '一级分类', width: 50, align: 'center'},
                {field: 'qqNumber', title: 'QQ', width: 50, align: 'center'},
                {field: 'mobileNumber', title: '手机号码', width: 50, align: 'center'},
                {field: 'isAvailable', title: '状态', width: 50, align: 'center',
                    formatter: function (value, row, index) {
                        if(value == 1 ) {
                            return '可用';
                        }else {
                            return "不可用";
                        }
                    }
                },
                {field: 'hidden', title: '操作', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                            var v1 = '<a href="javascript:;" onclick="editForm(' + index + ')">更改</a>';
                            return v1 ;
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
                        $('#addIsAvailable').combobox('setValue', 1);
                        $('#addEditDiv').panel({title: "新增"});
                        $('#addEditDiv').dialog('open');
                    }
                }
            ],
            pagination: true
        });

        <!--新增编辑-->
        $('#addEditDiv').dialog({
            title: "",
            height: 400 ,
            width: 450 ,
            collapsible: true,
            closed: true,
            modal: true,
            resizable:true,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var categoryFirstId = $('#categoryFirstId').combobox("getValue");
                        var name = $.trim($("#name").val());
                        var qqNumber = $.trim($("#qqNumber").val());
                        var mobileNumber = Number($("#mobileNumber").val());
                        var isAvailable = $('#addIsAvailable').combobox("getValue");
                        var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
                        if (! reg.test(mobileNumber)) {
                            $.messager.alert('提示', '请填写正确的手机号', 'info');
                            return false;
                        }
                        if ($.trim(categoryFirstId) == '') {
                            $.messager.alert('提示', '请选择分类', 'info');
                            return false;
                        } else if(name == ''){
                            $.messager.alert('提示', '请填写对接人姓名', 'info');
                            return false;
                        }else if (qqNumber == '') {
                            $.messager.alert('提示', '请输入qq', 'info');
                            return false;
                        }else if ($.trim(isAvailable) == '') {
                            $.messager.alert('提示', '请选择是否可用', 'info');
                            return false;
                        }
                        $.ajax({
                            type: "POST",
                            url: "${rc.contextPath}/sellerCategoryOperationManager/saveOrUpdate",
                            data: {
                                id: $('#form_id').val(),
                                categoryFirstId: categoryFirstId,
                                name: name,
                                qqNumber: qqNumber,
                                mobileNumber: mobileNumber,
                                isAvailable: isAvailable
                            },
                            success: function (res) {
                                if (res.status == 1) {
                                    $.messager.alert('响应信息', "成功", 'info', function () {
                                        $('#s_data').datagrid('reload');
                                        $('#addEditDiv').dialog('close');
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
                        $('#addEditDiv').dialog('close');
                    }
                }]
        });

    });
</script>

</body>
</html>