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

<div data-options="region:'center',title:'开机广告信息'" style="padding:5px;">
	<form id="saveAdvertise"  method="post">
		<fieldset>
			<input type="hidden" id="id" name="id" value="<#if advMap.id?exists>${advMap.id?c}<#else>0</#if>"/>
			<legend>开机广告</legend>
			 图片备注：<input name="remark" id="remark"  maxlength="100" value="<#if advMap.remark?exists>${advMap.remark}</#if>" style="width:300px;"/><br/><br/>
    		&nbsp;&nbsp;图片：<input maxlength="100" id="image" name="image" style="width:300px;"  value="<#if advMap.image?exists>${advMap.image}</#if>" />
    		<a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a>&nbsp;
    		<font color="red">尺寸长宽比  750:1234</font><br/><br/>
    		关联类型：<input type="radio" name="type"  id="type1" value="1" <#if advMap.type?exists && (advMap.type == 1 || advMap.type == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
    				<input type="radio" name="type"  id="type2" value="2" <#if advMap.type?exists && (advMap.type == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
    				<input type="radio" name="type"  id="type3" value="3" <#if advMap.type?exists && (advMap.type == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
    				<font color="red">必填</font><br/><br/>
			<!-- 特卖类型为单品时 -->
			<div id="singleDiv">
			 &nbsp;商品ID：<input type="text" id="productId" name="productId" style="width:300px;"  value="<#if advMap.displayId?exists &&(advMap.displayId !=0) >${advMap.displayId}</#if>" />
			<span id="productName" style="color:red"></span><font color="red">必填</font><br/><br/>
			</div>
			
			<!-- 特卖类型为组合特卖时 -->
			<div id="groupDiv" style="display:none">
			选择组合：<input id="groupSale" type="text" name="groupSale" style="width:300px" /><font color="red">必填</font><br/><br/>
			</div>
			
			<!-- 特卖类型为自定义活动时 -->
			<div id="customSaleDiv" style="display:none">
			自定义活动：<input type="text" id="customSale" name="customSale" style="width:300px;"/><font color="red">必填</font><br/><br/>
			</div>
			
			是否展现：<input type="radio"  name="isDisplay" value="1" <#if advMap.isDisplay?exists &&(advMap.isDisplay ==1) >checked="checked"</#if>>展现&nbsp;&nbsp;&nbsp;
			<input type="radio"  name="isDisplay" value="0" <#if advMap.isDisplay?exists &&(advMap.isDisplay ==0) >checked="checked"</#if>>不展现<br/><br/>

			
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

	
	$(function(){
		$('#type1').change(function(){
			if($(this).is(':checked')){
				$('#singleDiv').show();
				$('#groupDiv').hide();
				$('#customSaleDiv').hide();
			}
		});
		
		$('#type2').change(function(){
			if($(this).is(':checked')){
				$('#singleDiv').hide();
				$('#groupDiv').show();
				$('#customSaleDiv').hide();
			}
		});
		
		$('#type3').change(function(){
			if($(this).is(':checked')){
				$('#singleDiv').hide();
				$('#groupDiv').hide();
				$('#customSaleDiv').show();
			}
		});
		
	
		$('#type1').change();
		$('#type2').change();
		$('#type3').change();
		
		$('#saveAdvertise').form({   
		    url:'${rc.contextPath}/index/saveOrUpdateAdvertise',   
		    onSubmit: function(){   
		          var type = $("input[name='type']:checked").val();
		          var productId = $("#productId").val();
		          var groupSale = $("#groupSale").combobox('getValue');
		          var customSale = $("#customSale").combobox('getValue');
		          var image = $("#image").val();
		          if(image == ''){
		        	  $.messager.alert('提示','请上传图片','warning');
		        	  return false;
		          }else if(type == '' || type == null || type == undefined){
		        	  $.messager.alert('提示','请选择关联类型','warning'); 
		        	  return false;
		          }else if(type == 1 && productId == ''){
		        	  $.messager.alert('提示','请填写关联商品Id','warning'); 
		        	  return false;
		          }else if(type == 2 && (groupSale == '' || groupSale == null || groupSale == undefined)){
		        	  $.messager.alert('提示','请选择组合','warning');
		        	  return false;
		          }else if(type == 3 && (customSale == '' || customSale == null || customSale == undefined)){
		        	  $.messager.alert('提示','请选择自定义活动','warning');
		        	  return false;
		          }
		          $.messager.progress();
		    },   
		    success:function(data){   
		    	$.messager.progress('close');
                var res = eval("("+data+")");
                if(res.status == 1){
                    $.messager.alert('响应信息',"保存成功",'info',function(){
                        window.location.href = '${rc.contextPath}/index/advList';
                    });
                } else if(res.status == 0){
                    $.messager.alert('响应信息',res.msg,'info');
                } 
		    }   
		});
	
		
		$("#saveButton").click(function(){
			$('#saveAdvertise').submit();  
    	});
    	
		$("#productId").change(function(){
			var id = $.trim($(this).val());
			var elementId = $(this).attr('id');
			if(id == ""){
				$("#productName").text("");
			}else{
				$("#productName").text("");
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
                    		$('#productName').text(data.msg);
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
		
		$("#groupSale").combobox({
			url:'${rc.contextPath}/banner/jsonAcCommonCode?id=<#if displayId??>${displayId?c}<#else>${0}</#if>',   
		    valueField:'code',   
		    textField:'text'  
		});
		
		$("#customSale").combobox({
			url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id=<#if displayId??>${displayId?c}<#else>${0}</#if>',   
		    valueField:'code',   
		    textField:'text' 
		});
	});
</script>

</body>
</html>