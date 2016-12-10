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

<div data-options="region:'center',title:'添加标签信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="saveSaleTag" action="${rc.contextPath}/saleTag/save"  method="post">
		<fieldset>
			<input type="hidden" value="<#if saleTag.id?exists && (saleTag.id != 0)>${saleTag.id?c}</#if>" id="editId" name="editId" />
			<legend>标签信息</legend>
    		 标签名称: <input type="text" maxlength="10" id="name" name="name" style="width:300px;"  value="<#if saleTag.name?exists>${saleTag.name}</#if>" /><br/><br/>
			 上传图片: <input type="text" id="image" name="image" style="width:300px;" value="<#if saleTag.image?exists>${saleTag.image}</#if>" />
				<a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><br/><br/>
			 标签状态:
			 <input type="radio" name="isAvailable" value="1" <#if saleTag.isAvailable?exists && (saleTag.isAvailable == 1) >checked</#if>> 可用&nbsp;&nbsp;&nbsp;
			 <input type="radio" name="isAvailable" value="0" <#if saleTag.isAvailable?exists && (saleTag.isAvailable == 0) >checked</#if>> 停用<br/><br/>
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script type="text/javascript">
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        })
    })

    var inputId;
    function picDialogOpen($inputId) {
        inputId = $inputId;
        $("#picDia").dialog("open");
        $("#yun_div").css('display','none');
    }
    function picDialogClose() {
       $("#picDia").dialog("close");
    }
    function picUpload() {
        $('#picForm').form('submit',{
            url:"${rc.contextPath}/pic/fileUpLoad",
            success:function(data){
                var res = eval("("+data+")")
                if(res.status == 1){
                    $.messager.alert('响应信息',"上传成功...",'info',function(){
                        $("#picDia").dialog("close");
                        if(inputId) {
                            $("#"+inputId).val(res.url);
                            $("#picFile").val("");
                        }
                        return
                    });
                } else{
                    $.messager.alert('响应信息',res.msg,'error',function(){
                        return ;
                    });
                }
            }
        })
    }
</script>

<script type="text/javascript">
	$(function(){
		$("#saveButton").click(function(){
			var name = $('#name').val();
			var image = $('#image').val();
			if(name == ""){
				$.messager.alert("提示","名称必填","info");
			}else if(image == ""){
				$.messager.alert("提示","图片必填","info");
			}else{
				$('#saveSaleTag').submit();			
			}
    	});
	})
</script>

</body>
</html>