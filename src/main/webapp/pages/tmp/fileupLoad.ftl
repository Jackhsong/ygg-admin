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
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'center'" >
   			<form method="post" id="fileUploadForm" enctype="multipart/form-data">
    			<table cellpadding="5">
	    			<tr>
	    				<td>上传文件：</td>
	    				<td><input type="text" id="userFile" name="userFile" style="width:300px"></td>
	    			</tr>
	    			<tr>
	    				<td><a onclick="fileUpload()" href="javascript:;" class="easyui-linkbutton">上传</a></td>
	    				<td></td>
	    			</tr>
    			</table>
   			</form>
		</div>
	</div>
</div>
<script>
	$(function(){
	    
		$('#fileUploadForm').form({   
		    url:"${rc.contextPath}/temporary/readFile", 
		    /* url:"${rc.contextPath}/temporary/importBaseProductStock",  */  
		    onSubmit: function(){   
		    	var userFile = $("#userFile").filebox("getValue");
		    	if(userFile==''){
		    		$.messager.alert("info","请选择文件","warn");
					return false;
		    	}
		    	$.messager.progress();
		    },   
		    success:function(data){   
		    	$.messager.progress('close');
		    	var res = eval("("+data+")");
		    	$.messager.alert('响应信息',res.msg,'info');
		    }   
		});   
		  
	    
		$("input[name='userFile']").each(function(){
			$(this).filebox({
				buttonText: '打开文件',
				buttonAlign: 'right'
			});
		});
	});
	
	function fileUpload(){
		$('#fileUploadForm').submit();
	}
</script>

</body>
</html>