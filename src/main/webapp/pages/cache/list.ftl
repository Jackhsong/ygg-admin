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

<style>
	span{font-size:14px;}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center'" style="padding: 5px;padding-left: 15px;">
		<div style="padding:10px">
			<span>搜索缓存</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a onclick="clearSearchCache()" href="javascript:;" class="easyui-linkbutton">刷新</a>
			<br/><br/>
			<hr/><br/><br/>
		</div>
	</div>

<script>
function clearSearchCache(){
    $.messager.progress();
    $.ajax({
        url: '${rc.contextPath}/cache/clearSearchCache',
        type: 'post',
        dataType: 'json',
        data: {'id':1},
        success: function(data){
            $.messager.progress('close');
            if(data.status == 1){
                $.messager.alert("提示",data.msg,"info");
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
</script>

</body>
</html>