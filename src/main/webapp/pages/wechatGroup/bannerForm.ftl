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

<div data-options="region:'center',title:'添加Banner信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="savebannerWindow" action="${rc.contextPath}/mwebGroupBanner/save"  method="post">
		<fieldset>
			<input type="hidden" value="${bannerWindow.id?c}" id="editId" name="editId" />
			<legend>Banner信息</legend>
    		 Banner排序值: <input type="text" id="order" name="order" style="width:300px;"  value="<#if bannerWindow.order?exists>${bannerWindow.order?c}</#if>" /><br/><br/>
    		 Banner描述: <input type="text" maxlength="20" id="desc" name="desc" style="width:300px;"  value="<#if bannerWindow.desc?exists>${bannerWindow.desc}</#if>" /><br/><br/>
			 Banner图片: <input type="text" id="image" name="image" style="width:300px;" value="<#if bannerWindow.image?exists>${bannerWindow.image}</#if>" />
            <a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><span style="color:red">必须（640x260）</span><br/>
			<span id="imageShow">
			<#if bannerWindow.image?exists>
                <img alt="" src="${bannerWindow.image}" style="min-width: 100px;">
			</#if>
            </span><br/><br/>

			开始时间:<input value="<#if startTime?exists >${startTime}</#if>" id="startTime" name="startTime" onClick="doWPStart()"/>&nbsp;&nbsp;&nbsp;
			 结束时间:<input value="<#if endTime?exists >${endTime}</#if>" id="endTime" name="endTime" onClick="doWPEnd()"/>
			 <span style="color:red">必须</span><br/><br/>
			
			 商品ID: <input type="number" id="product" name="product" style="width:300px;" onblur="stringTrim(this);" value="<#if bannerWindow.displayId?exists && (bannerWindow.displayId != 0)>${bannerWindow.displayId?c}</#if>" />
			 <span style="color:red">必须</span>&nbsp;&nbsp;&nbsp;<span id="productName" style="color:red"></span><br/><br/>
			
			 展现状态:
			 <#if bannerWindow.isDisplay?exists && (bannerWindow.isDisplay == 1) >
				 <input type="radio" name="isDisplay" value="1" checked>展现&nbsp;&nbsp;&nbsp;
				 <input type="radio" name="isDisplay" value="0">不展现<br/><br/>
			<#else>
				<input type="radio" name="isDisplay" value="1">展现&nbsp;&nbsp;&nbsp;
				<input type="radio" name="isDisplay" value="0" checked>不展现<br/><br/>
			</#if>
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <input type="hidden" name="needWidth" id="needWidth" value="640">
        <input type="hidden" name="needHeight" id="needHeight" value="260">
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script type="text/javascript">

	function stringTrim(obj){
		$(obj).val($.trim($(obj).val()));	
	}
	
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
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
                            $("#imageShow").html("<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
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
	
function doWPStart(){
	WdatePicker({dateFmt: 'yyyy-MM-dd 10:00:00',maxDate:'#F{$dp.$D(\'endTime\')}'});
}

function doWPEnd(){
	WdatePicker({dateFmt: 'yyyy-MM-dd 09:59:59',minDate:'#F{$dp.$D(\'startTime\',{d:1})}'})
}


	$(function(){
		$("#saveButton").click(function(){
			var product = $("#product").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			var image = $("#image").val();
			if(image == ''){
				$.messager.alert('提示','请上传Banner图片');
				return false;
			}
			if(startTime == '' || endTime == ''){
				$.messager.alert('提示','请选择开始结束时间');
				return false;
			}
			if(product == ''){
				$.messager.alert('提示','请填写商品ID');
				return false;
			}
			$('#savebannerWindow').submit();
    	});
		
		$('#product').change(function(){
			var id = $('#product').val();
			if(id == ""){
				$('#productName').text("");
			}else{
				$('#productName').text("");
				$.ajax({
	                url: '${rc.contextPath}/wechatGroup/jsonProductInfoForTeamById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#productName').text(data.data.name);
	                    }else{
	                    	$.messager.alert("提示",data.msg,"info");
	                    }
	                },
	                error: function(xhr){
	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                }
	            });
			}
		});
	});
</script>

</body>
</html>