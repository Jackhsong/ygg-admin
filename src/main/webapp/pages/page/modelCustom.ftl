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
<div data-options="region:'center',title:'版块布局管理'" style="padding: 5px;">
    <div id="cc" class="easyui-layout" data-options="fit:true" >
        <div data-options="region:'north',split:true" style="height: 50px;">
            <div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
                版块Id：${modelId}&nbsp;&nbsp;&nbsp;
                版块名称：${modelName}&nbsp;&nbsp;&nbsp;
                版块类型：${modelType}&nbsp;&nbsp;&nbsp;
                备注：${modelRemark}&nbsp;&nbsp;&nbsp;
            </div>
        </div>
        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" style=""></table>

        </div>
    </div>
</div>
<script>

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
        $.ajax({
            url: '${rc.contextPath}/page/updatePageModelCustomLayoutSimpleData',
            type:"POST",
            data: {id:row.id,sequence:row.sequence},
            success: function(data) {
                if(data.status == 1){
                    $('#s_data').datagrid('load',{
                        modelId:${modelId}
                    });
                } else{
                    $.messager.alert('响应信息',data.msg,'error');
                }
            }
        });
    };


    function displayIt(id,code){
        var tips = '';
        if(code == 1){
            tips = '确定设为展现吗？';
        }else{
            tips = '确定设为不展现吗？'
        }
        $.messager.confirm("提示信息",tips,function(re){
            if(re){
                $.messager.progress();
                $.post("${rc.contextPath}/page/updatePageModelCustomLayoutSimpleData",{id:id,isDisplay:code},function(data){
                    $.messager.progress('close');
                    if(data.status == 1){
                        $('#s_data').datagrid('load',{
                            modelId:${modelId}
                        });
                    } else{
                        $.messager.alert('响应信息',data.msg,'error');
                    }
                })
            }
        })
    }

    $(function(){

        <!--列表数据加载-->
        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/page/ajaxPageModelData',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{modelId:${modelId}},
            pageSize:50,
            columns:[[
                {field:'id',    title:'id', width:20, align:'center',checkbox:true},
                {field:'displayDesc',    title:'展现状态', width:70, align:'center'},
                {field:'layout',    title:'布局方式', width:50, align:'center'},
                {field:'oneRemark',    title:'第一张备注', width:80, align:'center'},
                {field:'twoRemark',    title:'第二张备注', width:80, align:'center'},
                {field:'threeRemark',    title:'第三张备注', width:80, align:'center'},
                {field:'fourRemark',    title:'第四张备注', width:80, align:'center'},
                {field:'sequence',    title:'排序值', width:50, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden1',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                        if (row.editing){
                            var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                            var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                            return a + b;
                        }else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                            var b = '<a href="${rc.contextPath}/page/toEdit/${modelId}/' + row.id + '")>编辑</a>';
                            return a + b;
                        }
                    }
                },
                {field:'hidden2',  title:'展现设置', width:30,align:'center',
                    formatter:function(value,row,index){
                        if(row.isDisplay == 1){
                            return '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">设为不展现</a>';
                        }else{
                            return '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">设为展现</a>';
                        }
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增布局',
                iconCls:'icon-add',
                handler:function(){
                    window.location.href = "${rc.contextPath}/page/toAdd/${modelId}"
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

    });
</script>

</body>
</html>