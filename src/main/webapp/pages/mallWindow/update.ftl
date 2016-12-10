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

<div data-options="region:'center',title:'添加布局'" style="padding:5px;">
	<form id="saveMallWindow" action="${rc.contextPath}/mallWindow/save"  method="post">
		<fieldset>
			<input type="hidden" value="<#if mallWindow.id?exists && (mallWindow.id != 0)>${mallWindow.id?c}<#else>0</#if>" id="id" name="id" />
			<legend>主题馆信息</legend>
    		 名称： 
    		 <input maxlength="5" id="name" name="name" style="width:300px;"  value="<#if mallWindow.name?exists>${mallWindow.name}</#if>" /><font color="red">&nbsp;*(注：5个汉字长度以内)</font><br/><br/>
    		 备注： 
    		 <input maxlength="50" id="remark" name="remark" style="width:300px;"  value="<#if mallWindow.remark?exists>${mallWindow.remark}</#if>" /><br/><br/>
			 图片上传： 
			<input type="text" id="image" name="image" style="width:300px;" value="<#if mallWindow.image?exists>${mallWindow.image}</#if>" />
			<a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">*&nbsp;尺寸120x120</font>
			<#if mallWindow.image?exists>
			<br/>
			<img alt="" src="${mallWindow.image}" style="max-width:100px"/>
			</#if>
			<br/><br/>
			 关联主题馆页面：<input id="mallPageId" name="mallPageId" value=""/><br/><br/>
			 展现状态：
			<input type="radio" <#if mallWindow.isDisplay?exists && (mallWindow.isDisplay == 1) >checked</#if> name="isDisplay" value="1"> 展现&nbsp;&nbsp;&nbsp;
			<input type="radio" <#if mallWindow.isDisplay?exists && (mallWindow.isDisplay == 0) >checked</#if> name="isDisplay" value="0"> 不展现<br/><br/>

			<input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br/>
    <br/>
</div>

<script type="text/javascript">
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
        })
    });

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

    
	$(function(){
		
		
		$("#name").blur(function(){
			var name = $(this).val();
			$(this).val($.trim(name));
		});
		
		$("#remark").blur(function(){
			var remark = $(this).val();
			$(this).val($.trim(remark));
		});

		
		$("#saveButton").click(function(){
			var name = $("#name").val();
			var image = $("#image").val();
			var mallPageId = $("#mallPageId").combobox('getValue');
			if(name.length > 5){
				$.messager.alert('提示','名称不得超过5个字','info');
				return false;
			}else if($.trim(image)==''){
				$.messager.alert('提示','请上传图片','info');
				return false;
			}else if($.trim(mallPageId)=='' || mallPageId == null || mallPageId == undefined){
				$.messager.alert('提示','请选择关联主题馆页面','info');
				return false;
			}
			$('#saveMallWindow').submit();
			
    	});
    	

		$('#mallPageId').combobox({
		    url:'${rc.contextPath}/mallWindow/jsonMallPageCode?mallPageId=<#if mallWindow.mallPageId?exists>${mallWindow.mallPageId?c}<#else>${0}</#if>',   
		    valueField:'code',   
		    textField:'text'  
		});
	});
</script>

</body>
</html>