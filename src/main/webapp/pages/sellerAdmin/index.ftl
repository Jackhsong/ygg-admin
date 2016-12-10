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
<style type="text/css">
    a:link{
        text-decoration:none;
    }
    a:hover {
        color：black；
    }
    a:visited {
        color:black;
    }
    a:link {
        color:black;
    }
    a {
        color:black;
    }
</style>

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'菜单列表',split:true" style="width:180px;">
    <#include "../sellerAdmin/menu.ftl" >
</div>

<div data-options="region:'center',title:'${sellerName}'" style="padding:5px;">
	<h2 style="padding-left:200px">换吧网络-订单管理系统</h2>
</div>

</body>
</html>