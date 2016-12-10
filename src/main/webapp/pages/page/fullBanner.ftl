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
        <div data-options="region:'north',title:'通栏图片Banner',split:true" style="height: 160px;">
            <div id="searchDiv" style="padding: 15px;font-size: 15px;">
                版块Id：${modelId}&nbsp;&nbsp;&nbsp;
                版块名称：${modelName}&nbsp;&nbsp;&nbsp;
                版块类型：${modelType}&nbsp;&nbsp;&nbsp;
                备注：${modelRemark}&nbsp;&nbsp;&nbsp;
            </div>
            <input type="hidden" id="modelId" value="${modelId}" >
            <table style="padding-left: 15px;font-size: 15px;">
                <tr>
                    <td class="searchName">展现状态：</td>
                    <td class="searchText">
                        <select id="searchIsDisplay" name="searchIsDisplay" style="width: 173px;">
                            <option value="-1">全部</option>
                            <option value="1">展现</option>
                            <option value="0">不展现</option>
                        </select>
                        &nbsp;&nbsp;&nbsp;
                    </td>
                    <td class="searchName">进行状态：</td>
                    <td class="searchText">
                        <select id="searchStatus" name="searchStatus" style="width: 173px;">
                            <option value="-1">全部</option>
                            <option value="1">即将开始</option>
                            <option value="2">进行中</option>
                            <option value="3">已结束</option>
                        </select>
                        &nbsp;&nbsp;&nbsp;
                    </td>
                    <td class="searchRemark">banner描述：</td>
                    <td><input type="text" id="searchRemark" /></td>
                    <td><a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
                </tr>
            </table>
        </div>
        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" ></table>
        </div>
    </div>

</div>

<script>

    function searchProduct(){
        var modelId = $("#modelId").val();
        $('#s_data').datagrid('load',{
            modelId:modelId,
            bannerIsDisplay:$("#searchIsDisplay").val(),
            bannerStatus:$("#searchStatus").val(),
            bannerRemark:$("#searchRemark").val()
        });
    }

    function editIt(index){
        var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var urlStr = "${rc.contextPath}/page/toEditBanner/${modelId}/"+id;
        window.open(urlStr,"_blank");
    }

    function displayId(id,isDisplay){
        var tip = "";
        if(isDisplay == 0){
            tip = "确定不展现吗？";
        }else{
            tip = "确定展现吗？";
        }
        $.messager.confirm("提示信息",tip,function(ra){
            if(ra){
                $.messager.progress();
                $.ajax({
                    url: '${rc.contextPath}/page/updateBanner',
                    type: 'post',
                    dataType: 'json',
                    data: {'id':id,'isDisplay':isDisplay},
                    success: function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
                            $('#s_data').datagrid('load',{
                                modelId:${modelId}
                            });
                        }else{
                            $('#s_data').datagrid('load');
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
    }

    $(function(){

        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/page/ajaxPageModelData',
            queryParams:{modelId:${modelId}},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'displayDesc',    title:'展现状态', width:15, align:'center'},
                {field:'activityStatus',    title:'进行状态', width:15, align:'center'},
                {field:'typeStr',    title:'类型', width:15, align:'center'},
                {field:'displayId',    title:'对应ID', width:20, align:'center'},
                {field:'displayName',    title:'对应名称', width:35, align:'center'},
//                {field:'displayRemark',    title:'备注', width:50, align:'center'},
                {field:'imageURL',    title:'banner图', width:50, align:'center'},
                {field:'desc',    title:'banner描述', width:50, align:'center'},
                {field:'startTime',    title:'开始时间', width:50, align:'center'},
                {field:'endTime',    title:'结束时间', width:50, align:'center'},
                {field:'hidden1',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                        var b = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a>';
                        var e = '';
                        if(row.isDisplay == 1){
                            e = ' | <a href="javaScript:;" onclick="displayId(' + row.id  + ',' + 0 + ')">不展现</a>'
                        }else{
                            e = ' | <a href="javaScript:;" onclick="displayId(' + row.id  + ',' + 1 + ')">展现</a>'
                        }
                        return  b+e;
                    }
                }
            ]],
            toolbar:[{
                iconCls: 'icon-add',
                text:'添加',
                handler: function(){
                    window.location.href = "${rc.contextPath}/page/toAddBanner/${modelId}"
                }
            }],
            pagination:true,
            rownumbers:true
        });
    });

</script>

</body>
</html>