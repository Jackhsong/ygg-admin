<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-订单工具</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

<style type="text/css">
	#myCss span {
		font-size:12px;
		padding-right:20px;
	}
</style>

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'订单工具'" style="padding:5px;">

    <div style="padding:5px;">
		<p>贝贝网转笨鸟订单：</p>
        <form id="orderHBFromBeiBeiForBirdex" action="" method="post" enctype="multipart/form-data">
            <input id="orderHBFromBeiBeiForBirdex_file" type="text" name="orderFile" style="width:300px" />&nbsp;&nbsp;&nbsp;
            <a id="searchBtn" onclick="orderHBFromBeiBeiForBirdex_click()" href="javascript:;" class="easyui-linkbutton" style="width: 80px;" >立即转换</a><br/><br/>
        </form>
        <p>贝贝网订单发货导出：</p>
        <form id="beiBeiOrderSendGoods" action="" method="post" enctype="multipart/form-data">
            <input id="beiBeiOrderSendGoods_file" type="text" name="logisticsFile" style="width:300px" />&nbsp;&nbsp;&nbsp;
            <a id="searchBtn" onclick="beiBeiOrderSendGoods_click()" href="javascript:;" class="easyui-linkbutton" style="width: 80px;" >立即转换</a><br/><br/>
        </form>
    </div>

	<hr>

    <div style="padding:5px;">
        <p>导出海外购订单发货文件：</p>
        <form id="exportSendGoodsInfoByNumbers" action="" method="post" enctype="multipart/form-data">
            <input id="exportSendGoodsInfoByNumbers_file" type="text" name="orderNumberFile" style="width:300px" />&nbsp;&nbsp;&nbsp;
            <a id="searchBtn" onclick="exportSendGoodsInfoByNumbers_click()" href="javascript:;" class="easyui-linkbutton" style="width: 80px;" >导出</a><br/><br/>
        </form>
    </div>

</div>

<script type="application/javascript">
	function orderHBFromBeiBeiForBirdex_click(){
        $('#orderHBFromBeiBeiForBirdex').submit();
	}
    function beiBeiOrderSendGoods_click(){
        $('#beiBeiOrderSendGoods').submit();
    }
    function exportSendGoodsInfoByNumbers_click(){
        $('#exportSendGoodsInfoByNumbers').submit();
    }
    $(function (){

        $('#orderHBFromBeiBeiForBirdex').form({
            url:'${rc.contextPath}/tools/orderHBFromBeiBeiForBirdex',
            onSubmit: function(){
                var orderFile = $('#orderHBFromBeiBeiForBirdex_file').filebox("getValue");
                if(orderFile == ""){
                    $.messager.alert("去","请选择文件","error");
                    return false;
                }
                $.messager.progress({text:"转换中，导出成功后请刷新页面"});
            },
            success:function(data){
//                $.messager.progress('close');
//                var data = eval('(' + data + ')');  // change the JSON string to javascript object
            }
        });

        $('#beiBeiOrderSendGoods').form({
            url:'${rc.contextPath}/tools/orderSendGoodsFromBirdexForBeiBei',
            onSubmit: function(){
                var orderFile = $('#beiBeiOrderSendGoods_file').filebox("getValue");
                if(orderFile == ""){
                    $.messager.alert("去","请选择文件","error");
                    return false;
                }
                $.messager.progress({text:"转换中，导出成功后请刷新页面"});
            },
            success:function(data){
//                $.messager.progress('close');
//                var data = eval('(' + data + ')');  // change the JSON string to javascript object
            }
        });

        $('#exportSendGoodsInfoByNumbers').form({
            url:'${rc.contextPath}/tools/exportSendGoodsInfoByNumbers',
            onSubmit: function(){
                var orderFile = $('#exportSendGoodsInfoByNumbers_file').filebox("getValue");
                if(orderFile == ""){
                    $.messager.alert("去","请选择文件","error");
                    return false;
                }
                $.messager.progress({text:"计算中，导出成功后请刷新页面"});
            },
            success:function(data){
//                $.messager.progress('close');
//                var data = eval('(' + data + ')');  // change the JSON string to javascript object
            }
        });

        $('#orderHBFromBeiBeiForBirdex_file').filebox({
            buttonText: '打开贝贝订单文件',
            buttonAlign: 'left'
        });
        $('#beiBeiOrderSendGoods_file').filebox({
            buttonText: '打开笨鸟物流文件',
            buttonAlign: 'left'
        });
        $('#exportSendGoodsInfoByNumbers_file').filebox({
            buttonText: '打开订单编号文件',
            buttonAlign: 'left'
        });
	});
</script>

</body>
</html>