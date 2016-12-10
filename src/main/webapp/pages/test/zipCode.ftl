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

<div data-options="region:'center',title:'添加商品信息'" style="padding:5px;">

	
	<form id="saveBrand" action="${rc.contextPath}/test/setZipCode" method="post" enctype="multipart/form-data">
		<fieldset>
			<legend>品牌信息</legend>
    		 品牌名称: <input type="file" name="zipFile"/><br/><br/>
			 <input style="width: 150px" type="submit" value="保存"/>
		</fieldset>	
	</form>
	
</div>


</body>
</html>