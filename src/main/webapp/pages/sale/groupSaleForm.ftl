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
textarea{ 
	resize:none;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'center',title:'添加商品组合信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="saveAcCommon" action="${rc.contextPath}/sale/saveGroupSale"  method="post">
		<fieldset>
			<input type="hidden" value="<#if acCommon.id?exists && (acCommon.id != 0)>${acCommon.id?c}</#if>" id="editId" name="editId" />
			<legend>商品组合信息</legend>
			&nbsp;&nbsp;组合类型：<input type="radio" name="type" value="1" <#if acCommon.type?exists && (acCommon.type == 1) >checked</#if> > 特卖商品组合&nbsp;&nbsp;&nbsp;
			<input type="radio" name="type" value="2" <#if acCommon.type?exists && (acCommon.type == 2) >checked</#if> > 商城商品组合<br/><br/>
    		&nbsp;&nbsp;&nbsp;&nbsp;标题：<input type="text"  id="name" name="name" style="width:300px;"  value="<#if acCommon.name?exists>${acCommon.name}</#if>" /><font color="red">将在组合特卖页面标题位置展示，请认真填写（勿使用"专场"字样）。注：最多输入10个汉字或者20个字母/数字</font><br/><br/>
			&nbsp;&nbsp;&nbsp;格格说：<textarea name="gegesay" onkeydown="checkEnter(event)" id="gegesay" style="height: 60px;width: 300px"><#if acCommon.gegesay?exists>${acCommon.gegesay}</#if></textarea>&nbsp;&nbsp;长度（&lt; 140）：<span style="color:red" id="counter">0 字</span><br/><br/>
			&nbsp;&nbsp;&nbsp;&nbsp;备注：<input type="text" id="desc" name="desc" style="width:300px;"  value="<#if acCommon.desc?exists>${acCommon.desc}</#if>" /><br/><br/>
			 &nbsp;换吧网络头像：<select id="gegeImageId" name="gegeImageId">
    					<#list gegeImageList as glist>
    		 			  	<option value="${glist.id?c}"<#if acCommon.gegeImageId?exists && (acCommon.gegeImageId == glist.id)>selected</#if>>${glist.categoryName}</option>
    		 			</#list>
    				</select>
    				<img style='max-width:100px' src='${geGeImageEntity.image}'/><br/><br/>
			 &nbsp;&nbsp;&nbsp;&nbsp;状态：<input type="radio" name="isAvailable" value="1" <#if acCommon.isAvailable?exists && (acCommon.isAvailable == 1) >checked</#if> > 可用&nbsp;&nbsp;&nbsp;
			 <input type="radio" name="isAvailable" value="0" <#if acCommon.isAvailable?exists && (acCommon.isAvailable == 0) >checked</#if> > 停用<br/><br/>
			<!--&nbsp;&nbsp;banner图：<input type="text" readonly id="image" name="image" style="width:300px;" value="<#if acCommon.image?exists>${acCommon.image}</#if>" />
			<a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><span style="color:red">必须(640X260)</span><br/><br/>-->
			1.7banner图：<input type="text" readonly id="newImage17" name="newImage17" style="width:300px;" value="<#if acCommon.newImage17?exists>${acCommon.newImage17}</#if>" />
            <a onclick="picDialogOpen('newImage17')" href="javascript:;" class="easyui-linkbutton">上传图片</a><span style="color:red">必须(640X306)</span><br/><br/>
			<input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <input type="hidden" name="needWidth" id="needWidth" value="0">
        <input type="hidden" name="needHeight" id="needHeight" value="0">
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script type="text/javascript">

	$(document).keyup(function() { 
		var text=$("#gegesay").val(); 
		var counter=text.length;
		$("#counter").text(counter+" 字");
	});
	
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
        if(inputId == 'image'){
			$("#needWidth").val("0");
            $("#needHeight").val("0");
		}else if(inputId == 'newImage17'){
            $("#needWidth").val("0");
            $("#needHeight").val("0");
		}else{
			$("#needWidth").val("0");
            $("#needHeight").val("0");
		}
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

	function strlen(str){  
	    var len = 0;  
	    for (var i=0; i<str.length; i++) {   
		     var c = str.charCodeAt(i);   
		     if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {   
		       len+=0.5;   
		     }else {   
		      len+=1;   
		     }   
	    }   
	    return len;  
	}
	
	$(function(){
		$("#saveButton").click(function(){
			var title = $("#name").val();
	    	var gegesay = $("#gegesay").val();
	    	var weixin = $("#weixin").val();
	    	var image = $("#image").val();
	    	var newImage17 = $("#newImage17").val();
	    	if($.trim(title) == ""){
	    		$.messager.alert("提示","组合标题必填","info");
	    	}else if($.trim(gegesay) == ""){
	    		$.messager.alert("提示","说必填","info");
	    	}else if(gegesay.length>140){
	    		$.messager.alert("提示","堡主说字数不得超过140","info");
	    	}/* else if($.trim(weixin) == ""){
	    		$.messager.alert("提示","微信分享标题必填","info");
	    	} else if($.trim(image)==''){
	    		$.messager.alert("提示","banner图必填","info");
	    	}else if($.trim(newImage17) == ''){
	    		$.messager.alert("提示","1.7banner图必填","info");
	    	}*/else{
	    		$('#saveAcCommon').submit();
	    	}
    	});
		
		$("#gegeImageId").change(function(){
        	var imageId = $(this).val();
        	var $image = $(this).next('img');
        	$.ajax({
        		url: '${rc.contextPath}/image/getGegeImageById',
		            type: 'post',
		            dataType: 'json',
		            data: {'imageId':imageId, 'isAvailable':1,type:'sale'},
		            success: function(data){
		            	if(data.status == 1){
		            		$image.attr('src',data.url);	
		            	}
		            }
        	});
        });
	})
</script>

</body>
</html>