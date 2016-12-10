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
   			<form method="post" id="movePartnerForm">
    			<table cellpadding="5">
	    			<tr>
	    				<td>输入手机号：</td>
	    				<td><input type="text" id="phoneNumber" name="phoneNumber" style="width:300px"></td>
	    				<td><a onclick="fileUpload()" href="javascript:;" class="easyui-linkbutton">转移</a></td>
	    			</tr>
    			</table>
   			</form>
		</div>
	</div>
</div>
<script>
	$(function(){
	    
		$('#movePartnerForm').form({   
		    url:"${rc.contextPath}/temporary/movePartner",   
		    onSubmit: function(){   
		    	var phoneNumber = $("#phoneNumber").val();
		    	if($.trim(phoneNumber)==''){
		    		$.messager.alert("info","请填写手机号","warn");
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
		
	});
	
	function fileUpload(){
		$('#movePartnerForm').submit();
	}
</script>

</body>
</html>