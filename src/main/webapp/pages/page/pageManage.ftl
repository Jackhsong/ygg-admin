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

<div data-options="region:'center'" style="padding:5px;">

    <div id="cc" class="easyui-layout" data-options="fit:true" >
        <div data-options="region:'north',title:'页面板块管理',split:true" style="height: 120px;">
            <br/>
            <div style="height: 60px;padding: 10px">
                <span>自定义页面ID: ${pageId} </span>&nbsp;&nbsp;&nbsp;
                <span>自定义页面名称: ${pageName} </span>&nbsp;&nbsp;&nbsp;
                <span>自定义页面备注: ${pageRemark} </span>
            </div>
        </div>

        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" style=""></table>

            <!-- 新增 begin -->
            <div id="editDiv" class="easyui-dialog" style="width:600px;height:300px;padding:15px 20px;">
                <form id="editDiv_Form" method="post">
                    <input id="editDiv_Form_page_id" type="hidden" name="pageId" value="${pageId}" >
                    <input id="editDiv_Form_id" type="hidden" name="editId" value="" >
                    <p>
                        <span>板块名称：</span>
                        <span><input type="text" name="name" id="editDiv_Form_name" value="" maxlength="32" style="width: 300px;"/></span>
                    </p>
                    <p>
                        <span>板块备注：</span>
                        <span><input type="text" name="remark" id="editDiv_Form_remark" value="" maxlength="32" style="width: 300px;"/></span>
                        <font color="red">*</font>
                    </p>
                    <p>
                        <span>类型：</span>
                        <input type="radio" id="editDiv_Form_type1" name="type" checked value="1">通栏Banner图片&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="editDiv_Form_type2" name="type" value="2">滚动商品&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="editDiv_Form_type3" name="type" value="3">自定义板块&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="editDiv_Form_type4" name="type" value="4">两栏商品&nbsp;&nbsp;&nbsp;
                    </p>
                    <p>
                        <span>展现状态：</span>
                        <input type="radio" id="editDiv_Form_isDisplay1" name="isDisplay" value="1">展现&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="editDiv_Form_isDisplay0" name="isDisplay" value="0"> 不展现<br/><br/>
                    </p>
                </form>
            </div>
            <!-- 新增 end -->

        </div>

    </div>
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <!-- <input type="hidden" name="needWidth" id="needWidth" value="750">
        <input type="hidden" name="needHeight" id="needHeight" value="0"> -->
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script>

    function cleanEditDiv(){
        $('input[name="type"]').each(function(){
            $(this).attr("disabled",false);
        });
        $("#editDiv_Form_id").val('');
        $("#editDiv_Form input[type='text']").each(function(){
            $(this).val('');
        });

        $("#editDiv_Form_type1").prop("checked", true);
        $("#editDiv_Form_type2").prop("checked", false);
        $("#editDiv_Form_type3").prop("checked", false);
        $("#editDiv_Form_type4").prop("checked", false);

        $("#editDiv_Form_isDisplay1").prop("checked", true);
        $("#editDiv_Form_isDisplay0").prop("checked", false);
    }

    function editIt(index){
        cleanEditDiv();
        var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var name = arr.rows[index].name;
        var remark = arr.rows[index].remark;
        var isDisplay = arr.rows[index].isDisplay;
        var type = arr.rows[index].type;
        $('#editDiv_Form_id').val(id);
        $('#editDiv_Form_name').val(name);
        $('#editDiv_Form_remark').val(remark);
        if(isDisplay == 1)
        {
            $("#editDiv_Form_isDisplay1").prop("checked", true);
            $("#editDiv_Form_isDisplay0").prop("checked", false);
        }
        else
        {
            $("#editDiv_Form_isDisplay1").prop("checked", false);
            $("#editDiv_Form_isDisplay0").prop("checked", true);
        }

        // type
        $('input[name="type"]').each(function(){
            $(this).prop("checked", false);
        });

        var id = 'editDiv_Form_type' + type;
        $("#" + id).prop("checked", true);

        $('input[name="type"]').each(function(){
            $(this).attr("disabled",true);
        });

        $('#editDiv').dialog('open');
    }


    function displayIt(index,isDisplay){
    	var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var name = arr.rows[index].name;
        var pageId = $("#editDiv_Form_page_id").val();
        var tips = '';
        if(isDisplay == 1){
            tips = '确定展示吗？';
        }else{
            tips = '确定隐藏吗？'
        }
        $.messager.confirm("提示信息",tips,function(re){
            if(re)
            {
                $.messager.progress();
                $.ajax({
                    url: '${rc.contextPath}/page/updatePageModel',
                    type: 'post',
                    dataType: 'json',
                    data: {'id':id, 'pageId':pageId,'isDisplay':isDisplay,'name':name},
                    success: function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
                            $('#editDiv').dialog('close');
                            $('#s_data').datagrid('reload');
                        }else{
                            $.messager.alert("提示",data.msg,"error");
                        }
                    },
                    error: function(xhr){
                        $.messager.progress('close');
                        $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                    }
                });
            }
        });
    }

    <!--编辑排序相关 begin-->
    function editrow(index){
        $('#s_data').datagrid('beginEdit', index);
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

    function saverow(index){
        $('#s_data').datagrid('endEdit', index);
    };

    function cancelrow(index){
        $('#s_data').datagrid('cancelEdit', index);
    };

    function updateActivity(row){
        var pageId = $("#editDiv_Form_page_id").val();
        $.ajax({
            url: '${rc.contextPath}/page/updatePageModel',
            type:"POST",
            data: {id:row.id,pageId:pageId,sequence:row.sequence,name:row.name},
            success: function(data) {
                if(data.status == 1){
                    $('#s_data').datagrid('load',{
                        pageId:${pageId}
                    });
                }
                else
                {
                    $.messager.alert("提示",data.msg,"error");
                }
            }
        });
    };
    <!--编辑排序相关 end-->

    $(function(){

        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/page/ajaxPageModel',
            queryParams: {
                pageId: ${pageId}
            },
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center'},
                {field:'displayStatus',    title:'展现状态', width:30, align:'center'},
                {field:'name',    title:'板块名称', width:50, align:'center'},
                {field:'remark',    title:'备注', width:60, align:'center'},
                {field:'typeStr',    title:'类型', width:60, align:'center'},
                {field:'sequence',    title:'排序值', width:30, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){

                        if (row.editing){
                            var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                            var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                            return a + b;
                        }else{
                            var a = '<a href="javascript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                            var b = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                            var c = '';
                            var d = '';
                            if(row.isDisplay == 1){
                                d = '<a href="javaScript:;" onclick="displayIt(' + index + ',' + 0 + ')">不展现</a> | ';
                            }else if(row.isDisplay == 0) {
                                d = '<a href="javaScript:;" onclick="displayIt(' + index + ',' + 1 + ')">展现 </a> | ';
                            }
                            var e = '<a target="_blank" href="${rc.contextPath}/page/modelManage/' + row.id + '">管理板块</a> | ';
                            return a + b + c + d + e;
                        }
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增板块',
                iconCls:'icon-add',
                handler:function(){
                    cleanEditDiv();
                    $('#editDiv').dialog('open');
                }
            }],
            onBeforeEdit:function(index,row){
                row.editing = true;
                updateActions();
            },
            onAfterEdit:function(index,row){
                row.editing = false;
                updateActions();
                updateActivity(row);
            },
            onCancelEdit:function(index,row){
                row.editing = false;
                updateActions();
            },
            pagination:true
        });


        $('#editDiv').dialog({
            title:'自定义页面',
            collapsible: true,
            closed: true,
            modal: true,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function(){
                        var params = {};
                        params.pageId = $("#editDiv_Form_page_id").val();
                        params.id = $("#editDiv_Form_id").val();
                        params.name = $.trim($("#editDiv_Form_name").val());
                        params.remark = $.trim($("#editDiv_Form_remark").val());
                        params.isDisplay = $("input[name='isDisplay']:checked").val();
                        params.type = $("input[name='type']:checked").val();
                        /* if(params.name == ''){
                        	$.messager.alert("提示","请输入板块名称","error");
                        	return false;
                        } */
                        $.messager.progress();
                        $.ajax({
                            url: '${rc.contextPath}/page/updatePageModel',
                            type: 'post',
                            dataType: 'json',
                            data: params,
                            success: function(data){
                                $.messager.progress('close');
                                if(data.status == 1){
                                    $('#editDiv').dialog('close');
                                    $('#s_data').datagrid('reload');
                                }else{
                                    $.messager.alert("提示",data.msg,"error");
                                }
                            },
                            error: function(xhr){
                                $.messager.progress('close');
                                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                            }
                        });
                    }
                },
                {
                    text:'取消',
                    align:'left',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#editDiv').dialog('close');
                    }
                }]
        });
    });

    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        });
    });

    var inputId;
    function picDialogOpen($inputId) {
        inputId = $inputId;
        $("#picDia").dialog("open");
        $("#yun_div").css('display','none');
    }
    function picDialogClose() {
        $("#picDia").dialog("close");
    }
    function picUpload() {
        $('#picForm').form('submit',{
            url:"${rc.contextPath}/pic/fileUpLoad",
            success:function(data){
                var res = eval("("+data+")")
                if(res.status == 1){
                    $.messager.alert('响应信息',"上传成功...",'info',function(){
                        $("#picDia").dialog("close");
                        if(inputId) {
                            $("#"+inputId).val(res.url);
                            $("#picFile").val("");
                        }
                        return
                    });
                } else{
                    $.messager.alert('响应信息',res.msg,'error',function(){
                        return ;
                    });
                }
            }
        })
    }
</script>

</body>
</html>