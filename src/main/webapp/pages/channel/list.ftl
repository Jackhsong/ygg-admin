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
<div data-options="region:'center'" style="padding: 5px;">
    <div id="cc" class="easyui-layout" data-options="fit:true" >
        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" style=""></table>

            <!-- dialog begin -->
            <div id="addChannelDIV" class="easyui-dialog" style="width:300px;height:200px;padding:10px 10px;">
                <form id="addChannelForm" method="post">
                    <input type="text" id="channelId" name="id" hidden="true" />
                  渠道名：  <input type="text" id="channelName" name="name" />
                </form>
            </div>
            <!-- dialog end -->
        </div>
    </div>
</div>

<script>

    function editIt(index){
        var arr=$("#s_data").datagrid("getData");
        $("#channelId").val(arr.rows[index].id);
        $("#channelName").val(arr.rows[index].channelName);
        $('#addChannelDIV').dialog('open');
    }


    function deleteIt(id){
        $.messager.confirm("操作提示", "确定要删除？", function (data) {
            if (data) {
                $.ajax({
                    url :'${rc.contextPath}/channel/deleteChannel',
                    type: 'post',
                    dataType: 'json',
                    data: {
                            "id":id
                            },
                    success: function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
                            $.messager.alert("提示","删除成功","info",function(){
                                $('#s_data').datagrid("load");
                                $('#addChannelDIV').dialog('close');
                            });
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

    $(function(){
        //integral dialog  begin
        $('#addChannelDIV').dialog({
            title:'新增渠道',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                        $.messager.progress();
                        var params = {};
                        params.name = $("#channelName").val();
                        params.id = $("#channelId").val();
                        $.ajax({
                            url: '${rc.contextPath}/channel/saveOrUpdate',
                            type: 'post',
                            dataType: 'json',
                            data: params,
                            success: function(data){
                                $.messager.progress('close');
                                if(data.status == 1){
                                    $.messager.alert("提示","保存成功","info",function(){
                                        $("#channelName").val('');
                                        $("#channelId").val('');
                                        $('#s_data').datagrid("load");
                                        $('#addChannelDIV').dialog('close');
                                    });
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
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addChannelDIV').dialog('close');
                }
            }]
        });
        //integral dialog end
        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/channel/jsonChannelInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'channelName',    title:'渠道名', width:30, align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a>';
//                        var b= ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                        return a ;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增渠道',
                iconCls:'icon-add',
                handler:function(){
                    $("#addChannelForm input[name=channelId]").val("");
                    $("#addChannelForm input[name=channelName]").val("");
                    $('#addChannelDIV').dialog('open');
                }
            }],
            pagination:true,
            onLoadSuccess:function(){
                $("#s_data").datagrid('clearSelections');
            }
        });
    });
</script>
</body>
</html>