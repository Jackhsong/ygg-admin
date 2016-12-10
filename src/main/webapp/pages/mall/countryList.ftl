<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>商城国家</title>
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
        <div data-options="region:'north',title:'商城国家管理',split:true" style="height: 90px;">
            <div id="searchDiv" class="datagrid-toolbar" style="height: 90px;padding: 15px">
                <form id="searchForm" method="post">
                    <table class="search">
                        <tr>
                            <td>是否展示：</td>
                            <td><input id="searchIsDisplay" name="isDisplay" class="easyui-combobox"
                                       style="width:100px;"/></td>
                            <td>
                                <a id="searchBtn" onclick="searchMallCountry()" href="javascript:;" class="easyui-linkbutton"
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

            <!-- 新增编辑商城国家 Begin -->
            <div id="addMallCountryDiv" class="easyui-dialog" style="width:600px;height:300px;padding:15px 20px;">
                <form id="addHotBrandForm" method="post">
                    <input id="form_id" type="hidden" name="id">

                    <p>
                        <span>国家：</span>
                        <span><input type="text" id="saleFlagId" name="saleFlagId" style="width: 200px;"/></span>
                    </p>
                    <p>
                        <span>Banner：</span>
						<span>
							<input type="text" id="bannerImage" name="bannerImage" style="width:300px"><span style="color:red">建议大小:750*296</span>
							<a onclick="picDialogOpen()" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						</span>
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
            <!-- 新增编辑商城国家 End -->

        </div>
    </div>
</div>

<!-- 图片上传 -->
<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <#--<input type="hidden" name="needWidth" id="needWidth" value="750">-->
        <#--<input type="hidden" name="needHeight" id="needHeight" value="296">-->
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script>
    function searchMallCountry() {
        $("#s_data").datagrid("reload", {
            "isDisplay": $("#searchIsDisplay").combobox("getValue")
        });
    }

    function picDialogOpen() {
        $("#picDia").dialog("open");
    }

    function picUpload() {
        $('#picForm').form('submit',{
            url:"${rc.contextPath}/pic/fileUpLoad",
            success:function(data){
                var res = eval("("+data+")")
                if(res.status == 1){
                    $.messager.alert('响应信息',"上传成功...",'info',function(){
                        $("#picDia").dialog("close");
                            $("#bannerImage").val(res.url);
                            $("#picFile").val("");
                        });
                } else{
                    $.messager.alert('响应信息',res.msg,'error',function(){
                        return ;
                    });
                }
            }
        });
    }

    <!--编辑国家-->
    function editForm(index) {
        cleanAddFrom();
        var arr = $("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var flagId = arr.rows[index].flagId;
        var isDisplay = arr.rows[index].isDisplay;
        var sequence = arr.rows[index].sequence;
        var banner = arr.rows[index].bannerImage;

        $('#form_id').val(id);
        $('#sequence').val(sequence);
        $("#bannerImage").val(banner);
        $('#saleFlagId').combobox('setValue', flagId);
        $('#addIsDisplay').combobox('setValue', isDisplay);
        $('#addMallCountryDiv').panel({title:"编辑商城国家"});

        $("#addMallCountryDiv").dialog("open");
    }

    function manageBrand(id){
        window.open("${rc.contextPath}/mallCountryBrand/list/"+id);
    }

    function cleanAddFrom() {
        $("#form_id").val('');
        $("#sequence").val('');
        $("#bannerImage").val('');
        $("#saleFlagId").combobox("clear");
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
            url: '${rc.contextPath}/mallCountry/update',
            type:"POST",
            data: {id:row.id,sequence:row.sequence},
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
                $.post("${rc.contextPath}/mallCountry/update", {id: id, isDisplay: displayStatus}, function (data) {
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
                $.post("${rc.contextPath}/mallCountry/delete", {id: id}, function (data) {
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

        $('#saleFlagId').combobox({
            url: '${rc.contextPath}/flag/jsonSaleFlagCode?isAvailable=1',
            valueField: 'code',
            textField: 'text',
            multiple: false,
            editable: true,
        });

        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
        });


        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible: true,
            idField: 'id',
            url: '${rc.contextPath}/mallCountry/jsonInfo',
            queryParams : {
                isDisplay : 1
            },
            loadMsg: '正在装载数据...',
            singleSelect: true,
            fitColumns: true,
            remoteSort: true,
            pageSize: 50,
            pageList: [50],
            columns: [[
                {field: 'name', title: '国家名称(中文)', width: 50, align: 'center'},
                {field: 'enName', title: '国家名称(英文)', width: 50, align: 'center'},
                {field: 'flagImage', title: '国旗', width: 50, align: 'center'},
                {field: 'bannerDisplayImg', title: 'Banner', width: 50, align: 'center'},
                {field: 'brandCount', title: '下属品牌数', width: 50, align: 'center'},
                {field: 'sequence', title: '排序', width: 50, align: 'center', editor: {type: 'validatebox', options: {required: true}}},
                {field: 'isDisplay', title: '显示状态', width: 50, align: 'center',
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
                            var v3 = ' | <a href="javascript:;" onclick="manageBrand(' + row.id + ')">管理品牌</a>';
                            if (row.isDisplay == 1) {
                                var v4 = ' | <a href="javascript:;" onclick="display(' + row.id + ', 0)">不显示</a>';
                            } else {
                                var v4 = '| <a href="javascript:;" onclick="display(' + row.id + ', 1)">显示</a>';
                            }
                            var v5 = ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                            return v1 + v2 + v3 + v4 + v5;
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
                    text: '新增国家',
                    iconCls: 'icon-add',
                    handler: function () {
                        cleanAddFrom();
                        $('#addIsDisplay').combobox('setValue', 0);
                        $('#addMallCountryDiv').panel({title:"新增商城国家"});
                        $('#addMallCountryDiv').dialog('open');
                    }
                },
            ],
            pagination: true
        });

        <!--新增编辑大牌-->
        $('#addMallCountryDiv').dialog({
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
                        var flagId = $('#saleFlagId').combobox("getValue");
                        var sequence = $("#sequence").val();
                        var bannerImage = $.trim($("#bannerImage").val());
                        var isDisplay = $('#addIsDisplay').combobox("getValue");
                        if ($.trim(flagId) == '') {
                            $.messager.alert('提示', '请选择国家', 'info');
                            return false;
                        }
                        else if ($.trim(sequence) == '') {
                            $.messager.alert('提示', '请输入排序值', 'info');
                            return false;
                        } else if (Number($.trim(sequence)) < 0){
                            $.messager.alert('提示', '排序值大于等于0', 'info');
                            return false;
                        }else if ($.trim(isDisplay) == '') {
                            $.messager.alert('提示', '请选择是否展示', 'info');
                            return false;
                        }else  if(bannerImage == ''){
                            $.messager.alert('提示', '请填写banner Url', 'info');
                            return false;
                        }
                        $.ajax({
                            type: "POST",
                            url: "${rc.contextPath}/mallCountry/saveOrUpdate",
                            data: {
                                id: $('#form_id').val(),
                                saleFlagId: flagId,
                                image: bannerImage,
                                isDisplay: isDisplay,
                                sequence: sequence
                            },
                            success: function (res) {
                                if (res.status == 1) {
                                    $.messager.alert('响应信息', "保存成功", 'info', function () {
                                        $('#s_data').datagrid('reload');
                                        $('#addMallCountryDiv').dialog('close');
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
                        $('#addMallCountryDiv').dialog('close');
                    }
                }]
        });

    });
</script>

</body>
</html>