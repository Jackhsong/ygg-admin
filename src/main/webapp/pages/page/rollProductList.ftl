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
        <div data-options="region:'north',title:'滚动式商品列表',split:true" style="height: 160px;">
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
                    <td class="searchName">商品类型：</td>
                    <td class="searchText">
                        <select id="searchType" name="searchType" style="width: 173px;">
                            <option value="-1">全部</option>
                            <option value="1">特卖商品</option>
                            <option value="2">商城商品</option>
                        </select>
                        &nbsp;&nbsp;&nbsp;
                    </td>
                    <td><a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
                </tr>
                <tr>
                    <td class="searchName">商品Id：</td>
                    <td class="searchText"><input type="text" id="searchProductId" name="searchProductId"/>&nbsp;&nbsp;&nbsp;</td>
                    <td class="searchName">商品名称：</td>
                    <td class="searchText"><input type="text" id="searchProductName" name="searchProductName"/>&nbsp;&nbsp;&nbsp;</td>
                    <td></td>
                </tr>
            </table>
        </div>
        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" ></table>

            <!-- 快速增加begin -->
            <div id="addProductDiv" class="easyui-dialog" style="width:400px;height:250px;padding:20px 20px;">
                <table cellpadding="5">
                    <tr>
                        <td>商品ID：<font color="red">(如果要增加多个商品，用英文逗号分开)</font></td>
                    </tr>
                    <tr>
                        <td>
                            <textarea rows="3" cols="40" id="addProductDiv_id" onkeydown="checkEnter(event)"></textarea>
                        </td>
                    </tr>
                </table>
            </div>
            <!-- 快速增加end -->

            <!-- 增加商品 begin -->
            <div id="addSearchDiv" style="width:950px;height:750px;padding:20px 20px;">
                <div id="addSearchDiv_Search" class="datagrid-toolbar" style="height: 50px;padding: 15px">
                    <form id="addSearchDiv_Form" method="post" >
                        <table>
                            <tr>
                                <td>商品ID</td>
                                <td><input id="searchId" name="id" value=""/></td>
                                <td>商品编码</td>
                                <td><input id="searchCode" name="code" value="" /></td>
                                <td>名称</td>
                                <td><input id="searchName" name="name" value="" /></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>商家</td>
                                <td><input id="sellerId" type="text" name="sellerId" ></input></td>
                                <td>品牌</td>
                                <td><input id="brandId" type="text" name="brandId" ></input></td>
                                <td>备注</td>
                                <td><input id="remark" type="text" name="remark" ></input></td>
                                <td>
                                    &nbsp;&nbsp;<a id="searchBtn" onclick="searchAddProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <!-- 增加商品数据表格 -->
                <table id="product_data" ></table>
            </div>
            <!-- 增加商品 end -->

        </div>
    </div>

</div>

<script>

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

    function searchProduct(){
        var modelId = $("#modelId").val();
        $('#s_data').datagrid('load',{
            modelId:modelId,
            isDisplay:$("#searchIsDisplay").val(),
            productType:$("#searchType").val(),
            productId:$("#searchProductId").val(),
            productName:$("#searchProductName").val()
        });
    }

    function editProduct(index){
        var arr=$("#s_data").datagrid("getData");
        var type = arr.rows[index].type;
        var productId = arr.rows[index].productId;
        var urlStr = "${rc.contextPath}/product/edit/"+type+"/"+productId;
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
                    url: '${rc.contextPath}/page/updatePageModelRelationRollProduct',
                    type: 'post',
                    dataType: 'json',
                    data: {'ids':id,'isDisplay':isDisplay},
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

    function deleteIt(id){
        $.messager.confirm("提示信息","确认删除？",function(re){
            if(re){
                $.messager.progress();
                $.post("${rc.contextPath}/page/deletePageModelRelationRollProduct",{ids: id},function(data){
                    $.messager.progress('close');
                    if(data.status == 1){
                        $('#s_data').datagrid('load');
                    } else{
                        $.messager.alert('响应信息',data.msg,'error');
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
        $.ajax({
            url: '${rc.contextPath}/page/updatePageModelRelationRollProduct',
            type:"POST",
            data: {ids:row.id,sequence:row.sequence},
            success: function(data) {
                if(data.status == 1){
                    $('#s_data').datagrid('load',{
                        modelId:${modelId}
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

    function searchAddProduct(){
        $('#product_data').datagrid('clearSelections');
        $('#product_data').datagrid('load',{
            name:$("#searchName").val(),
            code:$("#searchCode").val(),
            brandId:$("input[name='brandId']").val(),
            sellerId:$("input[name='sellerId']").val(),
            status:1,
            productId:$("#searchId").val(),
            remark:$("#remark").val(),
            id:${modelId}
        });
    }

$(function(){

    $('#addSearchDiv').dialog({
        title:'商品信息',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[]
    });

    $('#product_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/page/ajaxProductList',
        queryParams: {
            id: ${modelId},
            status:0
        },
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
            {field:'id',    title:'序号', width:50, align:'center',checkbox:true},
            {field:'showId',    title:'商品ID', width:50, align:'center'},
            {field:'code',    title:'商品商家编码', width:60, align:'center'},
            {field:'name',    title:'商品名称', width:60, align:'center'},
            {field:'remark',    title:'商品备注', width:50, align:'center'},
            {field:'brandName',    title:'品牌', width:50, align:'center'},
            {field:'sellerName',    title:'商家', width:50, align:'center'},
            {field:'sendAddress',    title:'发货地', width:50, align:'center'},
            {field:'warehouse',    title:'分仓', width:50, align:'center'},
            {field:'stock',    title:'库存', width:30, align:'center'},
            {field:'marketPrice',    title:'原价', width:30, align:'center'},
            {field:'salesPrice',    title:'现价', width:30, align:'center'}
        ]],
        toolbar:[{
            iconCls: 'icon-add',
            text:'增加选中商品',
            handler: function(){
                var rows = $('#product_data').datagrid("getSelections");
                if(rows.length > 0){
                    $.messager.confirm('添加商品','请确认',function(b){
                        if(b){
                            var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].id)
                            }
                            $.post("${rc.contextPath}/page/addPageModelRelationRollProduct", //添加选中商品
                                    {ids: ids.join(","),modelId:${modelId}},
                                    function(data){
                                        if(data.status == 1){
                                            $('#addSearchDiv').dialog('close');
                                            $('#product_data').datagrid('clearSelections');
                                            $('#s_data').datagrid('reload');
                                        }else{{
                                            $.messager.alert('提示',data.msg,"error")
                                        }}
                                    },
                                    "json");
                        }
                    })
                }else{
                    $.messager.alert('提示','请选择要操作的商品',"error")
                }
            }
        }],
        pagination:true,
        rownumbers:true
    });

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
            {field:'id',    title:'-', width:15, align:'center',checkbox:true},
            {field:'displayDesc',    title:'展现状态', width:15, align:'center'},
            {field:'productId',    title:'商品ID', width:15, align:'center'},
            {field:'productTypeDesc',    title:'商品类型', width:15, align:'center'},
            {field:'name',    title:'长名称', width:70, align:'center'},
            {field:'salesPrice',    title:'售价', width:15, align:'center'},
            {field:'sequence',    title:'排序值', width:30, align:'center',editor:{type:'validatebox',options:{required:true}}},
            {field:'hidden1',  title:'操作', width:50,align:'center',
                formatter:function(value,row,index){
                    if (row.editing){
                        var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        return a + b;
                    }else{
                        var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                        var b = '<a href="javaScript:;" onclick="editProduct(' + index + ')">去编辑商品</a>';
                        var e = '';
                        if(row.isDisplay == 1){
                            e = ' | <a href="javaScript:;" onclick="displayId(' + row.id  + ',' + 0 + ')">不展现</a>'
                        }else{
                            e = ' | <a href="javaScript:;" onclick="displayId(' + row.id  + ',' + 1 + ')">展现</a>'
                        }
                        return a+b+e;
                    }
                }
            },
            {field:'hidden2',  title:'操作', width:15,align:'center',
                formatter:function(value,row,index){
                    return '<a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                }
            }
        ]],
        toolbar:[{
            iconCls: 'icon-add',
            text:'快速添加商品',
            handler: function(){
                $("#addProductDiv_id").val('');
                $("#addProductDiv").dialog('open');
            }
        },{
            text:'全部展现',
            iconCls:'icon-add',
            handler:function(){
                var rows = $('#s_data').datagrid("getSelections");
                if(rows.length > 0){
                    $.messager.confirm('展现','确认全部展现吗？',function(b){
                        if(b){
                            $.messager.progress();
                            var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].id)
                            }
                            $.post("${rc.contextPath}/page/updatePageModelRelationRollProduct",
                                    {ids: ids.join(","),isDisplay:1},
                                    function(data){
                                        $.messager.progress('close');
                                        if(data.status == 1){
                                            $('#s_data').datagrid('load');
                                            $('#s_data').datagrid('clearSelections');
                                        }else{
                                            $.messager.alert('提示','保存出错',"error")
                                        }
                                    },
                                "json");
                        }
                    })
                }else{
                    $.messager.alert('提示','请选择要操作的商品',"error")
                }
            }
        },'-',{
            text:'全部不展现',
            iconCls:'icon-remove',
            handler:function(){
                var rows = $('#s_data').datagrid("getSelections");
                if(rows.length > 0){
                    $.messager.confirm('不展现','确认全部不展现吗？',function(b){
                        if(b){
                            $.messager.progress();
                            var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].id)
                            }
                            $.post("${rc.contextPath}/page/updatePageModelRelationRollProduct",
                                    {ids: ids.join(","),isDisplay:0},
                                    function(data){
                                        $.messager.progress('close');
                                        if(data.status == 1){
                                            $('#s_data').datagrid('load');
                                            $('#s_data').datagrid('clearSelections');
                                        }else{
                                            $.messager.alert('提示','保存出错',"error")
                                        }
                                    },
                                "json");
                        }
                    })
                }else{
                    $.messager.alert('提示','请选择要操作的商品',"error")
                }
            }
        },'-',{
            text:'批量删除',
            iconCls:'icon-remove',
            handler:function(){
                var rows = $('#s_data').datagrid("getSelections");
                if(rows.length > 0){
                    $.messager.confirm("提示信息","确认删除？",function(re){
                        if(re){
                            $.messager.progress();
                            var ids = [];
                            for(var i=0;i<rows.length;i++){
                                ids.push(rows[i].id)
                            }
                            $.post("${rc.contextPath}/page/deletePageModelRelationRollProduct",{ids: ids.join(",")},function(data){
                                $.messager.progress('close');
                                if(data.status == 1){
                                    $('#s_data').datagrid('load');
                                } else{
                                    $.messager.alert('响应信息',data.msg,'error');
                                }
                            });
                        }
                    });
                }else{
                    $.messager.alert('提示','请选择要操作的商品',"error")
                }
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
        pagination:true,
        rownumbers:true
    });

    $('#addProductDiv').dialog({
        title:'快速增加商品',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存信息',
            iconCls:'icon-ok',
            handler:function(){
                var productId = $("#addProductDiv_id").val();
                if($.trim(productId) == ''){
                    $.messager.alert("提示",'请输入商品ID',"warning");
                }else{
                    $.messager.progress();
                    var modelId = $("#modelId").val();
                    $.ajax({
                        url: '${rc.contextPath}/page/addPageModelRelationRollProduct',
                        type: 'post',
                        dataType: 'json',
                        data: {'ids':productId,'modelId':modelId},
                        success: function(data){
                            $.messager.progress('close');
                            if(data.status == 1){
                                $('#addProductDiv').dialog('close');
                                $('#s_data').datagrid('load');
                                $.messager.alert("提示",data.msg,"info");
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

            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#addProductDiv').dialog('close');
            }
        }]
    });
});

</script>

</body>
</html>