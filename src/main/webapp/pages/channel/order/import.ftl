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
    <style type="text/css">
        .searchName{
            padding-right:10px;
            text-align:right;
        }
        .searchText{
            padding-left:10px;
            text-align:justify;
        }
        .search{
            width:1100px;
            align:center;
        }

    </style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
    <input type="hidden" value="${nodes!0}" id="nowNode"/>
<#include "../../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
    <div id="cc" class="easyui-layout" data-options="fit:true" >
        <div data-options="region:'north',title:'第三方订单导入',split:true" style="height: 100%;">
            <div id="searchDiv" style="height: 120px;padding: 15px">
                <form id="searchForm" action="${rc.contextPath}/channelOrder/importChannelOrder" method="post"   enctype="multipart/form-data">
                    <table class="search">
                        <tr>
                            <td class="searchName">选择文件：</td>
                            <td class="searchText"><input id="channel_order_file" name="channel_order_file" style="width:300px" type="text"  /></td>
                        </tr>
                        <tr>
                            <td class="searchName"></td>
                            <td class="searchText">
                                <a id="searchBtn" onclick="importOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'">上传并下载</a>
                                &nbsp;
                                <a id="clearBtn" onclick="downloadTemplate()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">下载模板</a>
                            </td>
                        </tr>
                    </table>
                    <br/>
                    <pre id="importMsg">

                    </pre>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    function importOrder(){
        var orderFile = $('#channel_order_file').filebox("getValue");
        if(orderFile == ""){
            $.messager.alert("提示","请选择文件","warning");
            return false;
        }
        $.messager.confirm("确认要导入吗?", "确认要导入吗?", function (r) {
            $('#importMsg').text('');
            if (r) {
                $('#searchForm').form('submit', {
                    url:"${rc.contextPath}/channelOrder/importChannelOrderCheck",
                    success:function(data){
                        var res = eval("("+data+")");
                        if(res.status == 1){
                            $('#searchForm').form('submit', {
                                url:"${rc.contextPath}/channelOrder/importChannelOrder",
                            });
                        }else{
                            $('#importMsg').text(res.message);
                            alert(res.message);
                        }
                    }
                });
        }
        });
    }
    function downloadTemplate(){
        window.location.href = '${rc.contextPath}/channelOrder/downloadTemplate' ;
    }

    $(function() {
        $('#channel_order_file').filebox({
            buttonText: '打开导单文件',
            buttonAlign: 'right'
        });

    });

</script>

</body>
</html>