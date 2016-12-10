<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>商城国家品牌</title>
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
        <div data-options="region:'north',title:'商城国家下品牌管理',split:true" style="height: 110px;">
            <div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
                <form id="searchForm" method="post">
                    <table class="search">
                        <tr>
                            <td>展示状态：</td>
                            <td><input id="searchIsDisplay" name="isDisplay" class="easyui-combobox"
                                       style="width:100px;"/></td>
                            <td>
                                <a id="searchBtn" onclick="searchMallCountryBrand()" href="javascript:;" class="easyui-linkbutton"
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

            <!-- 新增编辑商城国家下品牌 对话框 Begin -->
            <div id="addMallCountryBrandDiv" class="easyui-dialog" style="width:600px;height:300px;padding:15px 20px;">
                <form id="addHotBrandForm" method="post">
                    <input id="form_id" type="hidden" name="id">
                    <p>
                        <span>品牌(可直接填写品牌ID)：</span>
                        <span><input type="text" id="brandId" name="brandId" style="width: 200px;"/></span>
                    </p>
                    <p>
                        <span>是否展示：</span>
                        <span style="padding-left:80px"><input type="text" id="addIsDisplay" name="idDisplay" style="width: 200px;"/></span>
                    </p>
                    <p>
                        <span>排序值：</span>
                        <span style="padding-left:90px"><input type="number" id="sequence" name="sequence" style="width: 200px;"/></span>
                    </p>
                </form>
            </div>
            <!-- 新增编辑 End -->

        </div>
    </div>
</div>

<script>
    function searchMallCountryBrand() {
        $("#s_data").datagrid("reload", {
            "isDisplay": $("#searchIsDisplay").combobox("getValue"),
            "mallCountryId": ${mallCountryId}
        });
    }

    <!--编辑国家下品牌-->
    function editForm(index) {
        cleanAddFrom();
        var arr = $("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var brandId = arr.rows[index].brandId;
        var isDisplay = arr.rows[index].isDisplay;
        var sequence = arr.rows[index].countryBrandSequence;

        $('#form_id').val(id);
        $('#sequence').val(sequence);
        $('#brandId').combobox('setValue', brandId);
        $('#addIsDisplay').combobox('setValue', isDisplay);
        $('#addMallCountryBrandDiv').panel({title:"编辑商城国家品牌"});

        $("#addMallCountryBrandDiv").dialog("open");
    }

    function cleanAddFrom() {
        $("#form_id").val('');
        $("#sequence").val('');
        $("#brandId").combobox("clear");
        $("#addIsDisplay").combobox("clear");
    }

    //编辑排序 start
    function editrow(index) {
        $('#s_data').datagrid('beginEdit', index);
    }

    function saverow(index){
        $('#s_data').datagrid('endEdit', index);
    };

    function cancelrow(index){
        $('#s_data').datagrid('cancelEdit', index);
    };
    function updateActions(){
        var rowcount = $('#s_data').datagrid('getRows').length;
        for(var i=0; i<rowcount; i++){
            $('#s_data').datagrid('updateRow',{
                index:i,
                row:{}
            });
        }
    }

    function updateSequence(row){
        if(row.sequence<0){
            alert("排序值不能小于0"); return;
        }
        $.ajax({
            url: '${rc.contextPath}/mallCountryBrand/update',
            type:"POST",
            data: {id:row.id,sequence:row.countryBrandSequence},
            success: function(data) {
                if(data.status == 1){
                    $('#s_data').datagrid('reload');
                    return;
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return
                    });
                }
            }
        });
    };
    //编辑排序 end

    //单个显示或者不显示
    function display(id, displayStatus) {
        var a ='不显示';
        if(displayStatus == 1){
            a = "显示";
        }
        $.messager.confirm("确认", "确定"+a+"吗?", function (r) {
            if(r){
                $.post("${rc.contextPath}/mallCountryBrand/update", {id: id, isDisplay: displayStatus}, function (data) {
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
                $.post("${rc.contextPath}/mallCountryBrand/delete", {id: id}, function (data) {
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

    <!-- 批量修改显示状态 -->
    function batchUpdateDisplay(isDisplay) {
        var hotBrands = $("#s_data").datagrid("getSelections");
        if (hotBrands.length == 0) {
            $.messager.alert('warn', '请选择至少一条记录!', 'error');
            return;
        }
        var ids = [];
        $.each(hotBrands, function () {
            ids.push(this.id);
        })
        var id = ids.join(",");
        var tip = "显示";
        if(isDisplay == 0){
            tip = "不显示";
        }
        $.messager.confirm("确定?", "确定要"+tip+"?" ,function(r){
            if(r){
                $.ajax({
                    type: "POST",
                    url: "${rc.contextPath}/mallCountryBrand/updateDisplay",
                    data: {
                        id: id,
                        isDisplay: isDisplay
                    },
                    success: function (res) {
                        if (res.status == 1) {
                            $.messager.alert('响应信息', "保存成功", 'info', function () {
                                $('#s_data').datagrid('reload');
                                $('#s_data').datagrid('uncheckAll');
                            });
                        } else if (res.status == 0) {
                            $.messager.alert('响应信息s', res.message, 'error');
                        }
                    },
                });
            }
        });
    }


    <!--加载Grid-->
    $(function () {
        // 搜索
        $('#searchIsDisplay').combobox({
            panelWidth: 50,
            panelHeight: 80,
            data: [
                {"code": -1, "text": "全部"},
                {"code": 1, "text": "展示", "selected": true},
                {"code": 0, "text": "不展示"},
            ],
            valueField: 'code',
            textField: 'text'
        });

        // 添加 编辑
        $('#addIsDisplay').combobox({
            panelWidth: 50,
            panelHeight: 80,
            data: [
                {"code": 0, "text": "不展示", "selected": true},
                {"code": 1, "text": "展示"}
            ],
            valueField: 'code',
            textField: 'text'
        });

        $('#brandId').combobox({
            url: '${rc.contextPath}/brand/jsonBrandCode?isAvailable=1&mallCountryId=${mallCountryId}',
            valueField: 'code',
            textField: 'text',
            multiple: false,
            editable: true,
        });



        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible: true,
            idField: 'id',
            url: '${rc.contextPath}/mallCountryBrand/jsonInfo',
            queryParams : {
                isDisplay : 1,
                mallCountryId: ${mallCountryId}
            },
            loadMsg: '正在装载数据...',
            singleSelect: false,
            fitColumns: true,
            remoteSort: true,
            pageSize: 50,
            pageList: [50],
            columns: [[
                {field: 'id', title: '品牌', width: 30, align: 'center', checkbox:true},
                {field: 'brandId', title: '品牌id', width: 35, align: 'center',
                    formatter: function(value, row, index){
                        var url = "${rc.contextPath}/brand/edit/"+value;
                        return '<a href='+url+' target="_blank" style="max-height:30px" >'+value+' </href>'
                    }
                },
                {field: 'brandName', title: '品牌名称', width: 50, align: 'center'},
                {field: 'hotSpots', title: '品牌卖点', width: 50, align: 'center'},
                {field: 'introduce', title: '品牌介绍', width: 80, align: 'center'},
                {field: 'imageUrl', title: '品牌logo', width: 50, align: 'center'},
                {field: 'countryName', title: '品牌国家', width: 40, align: 'center'},
                {field: 'mallCountrySequence', title: '国家排序', width: 40, align: 'center'},
                {field: 'countryBrandSequence', title: '品牌排序', width: 40, align: 'center', editor: {type: 'validatebox', options: {required: true}}},
                {field: 'isDisplay', title: '显示状态', width: 40, align: 'center',
                    formatter: function (value, row, index) {
                        if(value == 1 ) {
                            return '<span style = "color:red" >显示中<span>';
                        }else {
                            return "未显示";
                        }
                    }
                },
                {field: 'hidden', title: '操作', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                        if (row.editing) {
                            var s = '<a href="javascript:void(0);" onclick="saverow(' + index + ')">保存</a> ';
                            var c = ' | <a href="javascript:void(0);" onclick="cancelrow(' + index + ')">取消</a>';
                            return s + c;
                        } else {
                            var v1 = '<a href="javascript:;" onclick="editForm(' + index + ')">编辑</a>';
                            var v2 = ' | <a href="javascript:;" onclick="editrow(' + index + ')">编辑排序</a>';
                            if (row.isDisplay == 1) {
                                var v4 = ' | <a href="javascript:;" onclick="display(' + row.id + ', 0)">不显示</a>';
                            } else {
                                var v4 = '| <a href="javascript:;" onclick="display(' + row.id + ', 1)">显示</a>';
                            }
                            var v5 = ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                            return v1 + v2 + v4 + v5;
                        }
                    }
                }
            ]],
            onBeforeEdit: function (index, row) {
                row.editing = true;
                updateActions();
            },
            onAfterEdit: function (index, row) {
                row.editing = false;
                updateActions();
                updateSequence(row);
            },
            onCancelEdit: function (index, row) {
                row.editing = false;
                updateActions();
            },
            toolbar: [
                {
                    id: '_add',
                    text: '添加新品牌',
                    iconCls: 'icon-add',
                    handler: function () {
                        cleanAddFrom();
                        $('#addIsDisplay').combobox('setValue', 0);
                        $('#addMallCountryBrandDiv').panel({title:"添加国家新品牌"});
                        $('#addMallCountryBrandDiv').dialog('open');
                    }
                },
                {
                    id: '_batch_show',
                    iconCls: 'icon-edit',
                    text: '批量显示',
                    handler: function () {
                        batchUpdateDisplay(1);
                    }
                },
                {
                    id: '_batch_unshow',
                    iconCls: 'icon-edit',
                    text: '批量不显示',
                    handler: function () {
                        batchUpdateDisplay(0);
                    }
                },
            ],
            pagination: true
        });

        <!--新增或编辑-->
        $('#addMallCountryBrandDiv').dialog({
            title: "",
            height: 400 ,
            width: 500 ,
            collapsible: true,
            closed: true,
            modal: true,
            resizable:true,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function () {
                        var brandId = $('#brandId').combobox("getValue");
                        if($.trim(brandId) == ''){
                            brandId = $('#brandId').combobox("getText");
                        }
                        var sequence = $("#sequence").val();
                        var isDisplay = $('#addIsDisplay').combobox("getValue");
                        if ($.trim(brandId) == '') {
                            $.messager.alert('提示', '请选择品牌', 'info');
                            return false;
                        } else if(isNaN(brandId)){
                            $.messager.alert('提示', '品牌必须为数字ID', 'info');
                            return false;
                        } else if ($.trim(sequence) == '') {
                            $.messager.alert('提示', '请输入排序值', 'info');
                            return false;
                        } else if (Number($.trim(sequence)) < 0){
                            $.messager.alert('提示', '排序值大于等于0', 'info');
                            return false;
                        }else if ($.trim(isDisplay) == '') {
                            $.messager.alert('提示', '请选择是否展示', 'info');
                            return false;
                        }
                        $.ajax({
                            type: "POST",
                            url: "${rc.contextPath}/mallCountryBrand/saveOrUpdate",
                            data: {
                                id: $('#form_id').val(),
                                brandId: brandId,
                                isDisplay: isDisplay,
                                sequence: sequence,
                                mallCountryId: ${mallCountryId}
                            },
                            success: function (res) {
                                if (res.status == 1) {
                                    $.messager.alert('响应信息', "保存成功", 'info', function () {
                                        $('#s_data').datagrid('reload');
                                        $('#addMallCountryBrandDiv').dialog('close');
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
                        $('#addMallCountryBrandDiv').dialog('close');
                    }
                }]
        });

    });
</script>

</body>
</html>