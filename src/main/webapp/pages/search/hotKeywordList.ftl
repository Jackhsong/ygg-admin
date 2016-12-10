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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">

    <div id="cc" class="easyui-layout" data-options="fit:true" >
        <div data-options="region:'north',title:'搜索词管理',split:true" style="height: 50px;padding-top:10px">

        </div>
        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" style=""></table>
        </div>

        <!-- dialog begin -->
        <div id="addKeyword" class="easyui-dialog" style="width:300px;height:170px;padding:5px 5px;">
            <form id="addKeywordForm" method="post">
                <table cellpadding="5">
                    <tr>
                        <td class="searchName">关键词：</td>
                        <td class="searchText">
                            <input id="keyword" type="text" name="keyword" />
                        </td>
                    </tr>
                    <tr>
                        <td class="searchName">排序值：</td>
                        <td class="searchText">
                            <input id="sequence" type="text" name="sequence" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <!-- dialog end -->

        <!-- dialog begin -->
        <div id="updateSequence" class="easyui-dialog" style="width:300px;height:170px;padding:5px 5px;">
            <form id="updateSequenceForm" method="post">
                <input type="hidden" id="editId" />
                <table cellpadding="5">
                    <tr>
                        <td class="searchName">排序值：</td>
                        <td class="searchText">
                            <input id="newSequence" type="text" name="newSequence" />
                        </td>
                    </tr>
                </table>
            </form>
        </div>
        <!-- dialog end -->

    </div>

</div>

<script>

    function deleteItem(index){
        var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        $.messager.confirm('删除','确定删除吗？',function(b){
            if(b){
                $.messager.progress();
                $.ajax({
                    url: '${rc.contextPath}/search/deleteHotKeyword',
                    type: 'post',
                    dataType: 'json',
                    data: {'id':id},
                    success: function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
                            $('#s_data').datagrid('load');
//                            $.messager.alert("提示",'删除成功',"info");
                        }else{
                            $.messager.alert("提示",data.msg,"info");
                        }
                    },
                    error: function(xhr){
                        $.messager.progress('close');
                        $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                    }
                });
            }
        });
    };

    function editSequence(id){
        $('#editId').val(id);
        $("#newSequence").val("");
        $('#updateSequence').dialog('open');
    }

	$(function(){
        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/search/jsonHotKeyword',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'keyword',    title:'关键词', width:80, align:'center'},
                {field:'sequence',     title:'排序值',  width:40,   align:'center' },
                {field:'hidden',  title:'操作', width:100,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javascript:;" onclick="editSequence('+row.id+')">改排序</a>';
                        var b = ' | <a href="javaScript:;" onclick="deleteItem(' + index + ')">删除</a>';
                        return a + b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增搜索词',
                iconCls:'icon-add',
                handler:function(){
                    $("#keyword").val("");
                    $("#sequence").val("");
                    $('#addKeyword').dialog('open');
                }
            }],
            pagination:true
        });

        //integral dialog  begin
        $('#addKeyword').dialog({
            title:'新增搜索词',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    var params = {};
                    params.keyword = $("#keyword").val();
                    params.sequence = $("#sequence").val();
                    if(!/^\d+$/.test(params.sequence)){
                        $.messager.alert("提示","排序值设置错误","error");
                    }else{
                        $.messager.progress();
                        $.ajax({
                            url: '${rc.contextPath}/search/saveHotKeyword',
                            type: 'post',
                            dataType: 'json',
                            data: params,
                            success: function(data){
                                $.messager.progress('close');
                                if(data.status == 1){
                                    $.messager.alert("提示","保存成功","info",function(){
                                        $('#s_data').datagrid("load");
                                        $('#addKeyword').dialog('close');
                                    });
                                }else{
                                    $.messager.alert("提示","保存失败","error");
                                }
                            },
                            error: function(xhr){
                                $.messager.progress('close');
                                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                            }
                        });
                    }
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addKeyword').dialog('close');
                }
            }]
        });
        //integral dialog end

        //integral dialog  begin
        $('#updateSequence').dialog({
            title:'修改排序值',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    var params = {};
                    params.sequence = $("#newSequence").val();
                    params.id = $("#editId").val();
                    if(!/^\d+$/.test(params.sequence)){
                        $.messager.alert("提示","排序值设置错误","error");
                    }else{
                        $.messager.progress();
                        $.ajax({
                            url: '${rc.contextPath}/search/saveHotKeyword',
                            type: 'post',
                            dataType: 'json',
                            data: params,
                            success: function(data){
                                $.messager.progress('close');
                                if(data.status == 1){
                                    $('#s_data').datagrid("load");
                                    $('#updateSequence').dialog('close');
                                }else{
                                    $.messager.alert("提示","保存失败","error");
                                }
                            },
                            error: function(xhr){
                                $.messager.progress('close');
                                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                            }
                        });
                    }
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#updateSequence').dialog('close');
                }
            }]
        });
        //integral dialog end
	});
</script>

</body>
</html>