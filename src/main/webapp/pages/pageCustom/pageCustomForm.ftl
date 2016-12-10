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

<div data-options="region:'center',title:'添加自定义特卖信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="savePageCustom" action="${rc.contextPath}/pageCustom/save"  method="post">
		<fieldset>
			<input type="hidden" value="<#if pageCustom.id?exists && (pageCustom.id != 0)>${pageCustom.id?c}</#if>" id="editId" name="editId" />
			<legend>组合特卖信息</legend>
    		 标题: <input type="text" maxlength="10" id="name" name="name" style="width:300px;"  value="<#if pageCustom.name?exists>${pageCustom.name}</#if>" /><br/><br/>
    		 <span style="color:red">URL</span>&nbsp;: <input type="text" maxlength="100" id="fileName" name="fileName" style="width:300px;"  value="<#if pageCustom.fileName?exists>${pageCustom.fileName}</#if>" /><br/><br/>
			 <span>APP端访问URL：http://www.gegejia.com/custom/<span style="color:red">URL</span>.html</span><br/>
			 <span>MOBILE端访问URL：http://static.gegejia.com/custom/<span style="color:red">URL</span>.html</span><br/><br/>
			 备注: <textarea name="desc" style="height: 60px;width: 300px"><#if pageCustom.desc?exists>${pageCustom.desc}</#if></textarea><br/><br/>
			 状态:
			 <input type="radio" name="isAvailable" value="1" <#if pageCustom.isAvailable?exists && (pageCustom.isAvailable == 1) >checked</#if> > 可用&nbsp;&nbsp;&nbsp;
			 <input type="radio" name="isAvailable" value="0" <#if pageCustom.isAvailable?exists && (pageCustom.isAvailable == 0) >checked</#if> > 停用<br/><br/>
			 
			 <span>页面内容编辑</span><br/><br/>
			 <input style="width: 100px;background-color:grey" type="button" id="pcButton" value="APP页面编辑"/>
			 <input style="width: 100px" type="button" id="mobileButton" value="移动页面编辑"/>
			 <div id="pcDetail_div">
			 	<textarea name="pcDetail" style="height: 400px;width: 700px"><#if pageCustom.pcDetail?exists>${pageCustom.pcDetail}</#if></textarea>
			 </div>
			 <div id="mobileDetail_div" style="display:none;">
			 	<textarea name="mobileDetail" style="height: 400px;width: 700px"><#if pageCustom.mobileDetail?exists>${pageCustom.mobileDetail}</#if></textarea>
			 </div>
			 <br/><br/>
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
</div>

<script type="text/javascript">
	
	$("#saveButton").click(function(){
    	$('#savePageCustom').submit();
    });
	
	$('#pcButton').click(function(){
		$('#pcButton').css("background","gray");
		$('#mobileButton').css("background","");
		$('#pcDetail_div').show();
		$('#mobileDetail_div').hide();
	});
		
	$('#mobileButton').click(function(){
		$('#mobileButton').css("background","gray");
		$('#pcButton').css("background","");
		$('#pcDetail_div').hide();
		$('#mobileDetail_div').show();
	});
		
</script>

</body>
</html>