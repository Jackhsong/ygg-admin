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

<div data-options="region:'center',title:'板块布局信息'" style="padding:5px;">
	<form id="saveCustomLayout"  method="post">
		<fieldset>
			<input type="hidden" id="id" name="id" value="<#if layoutProduct.id?exists>${layoutProduct.id?c}<#else>0</#if>"/>
			<input type="hidden" id="layoutId" name="layoutId" value="${layoutId}"/>
			<legend>自定义布局</legend>
			 选择布局方式：<input type="radio" name="displayType"  id="displayType1" value="1" <#if layoutProduct.displayType?exists &&(layoutProduct.displayType==1)>checked="checked"</#if>> 单张&nbsp;&nbsp;&nbsp;
					<input type="radio" name="displayType"  id="displayType2" value="2" <#if layoutProduct.displayType?exists &&(layoutProduct.displayType==2)>checked="checked"</#if>> 左右&nbsp;&nbsp;&nbsp;
					<font color="red">必填</font><br/><br/>
    		<span id="oneImageSpan">左侧图片上传</span>：<input maxlength="100" id="oneImage" name="oneImage" style="width:300px;"  value="<#if layoutProduct.oneImage?exists>${layoutProduct.oneImage}</#if>" />
    		<a onclick="picDialogOpen('oneImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">（左右布局且为单品时可不传）</font><br/>
    		<span id="oneImageShow">
    		<#if layoutProduct.oneImage?exists>
    			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="${layoutProduct.oneImage}" style="min-width: 100px;">
    		</#if>
    		</span><br/>
    		&nbsp;&nbsp;关联类型：<input type="radio" name="oneType"  id="oneType1" value="1" <#if layoutProduct.oneType?exists && (layoutProduct.oneType == 1 || layoutProduct.oneType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
    				<input type="radio" name="oneType"  id="oneType2" value="2" <#if layoutProduct.oneType?exists && (layoutProduct.oneType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
    				<font color="red">必填</font><br/><br/>
			<!-- 特卖类型为单品时 -->
			<div id="oneSingleDiv">
			 &nbsp;&nbsp;&nbsp;商品ID：<input type="text" id="oneProductId" name="oneProductId" style="width:300px;"  onblur="stringTrim(this);" value="<#if layoutProduct.oneDisplayId?exists &&(layoutProduct.oneDisplayId !=0) >${layoutProduct.oneDisplayId?c}</#if>" />
			(<font color="red">必填</font>)&nbsp;<span id="oneProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为组合特卖时 -->
			<div id="oneGroupDiv" style="display:none">
			 &nbsp;&nbsp;&nbsp;组合ID：<input id="oneGroupSale" type="text" name="oneGroupSale" style="width:300px" onblur="stringTrim(this);" value="<#if layoutProduct.oneDisplayId?exists &&(layoutProduct.oneDisplayId !=0) >${layoutProduct.oneDisplayId?c}</#if>"/>
			 (<font color="red">必填</font>)&nbsp;<span id="oneGroupName" style="color:red"></span><br/><br/>
			</div>
			&nbsp;&nbsp;&nbsp;&nbsp;描述：<span><textarea id="oneDesc" name="oneDesc" cols="40" rows="3" onkeydown="checkEnter(event)"><#if layoutProduct.oneDesc?exists>${layoutProduct.oneDesc}</#if></textarea></span>&nbsp;(字数&lt; 33：<span style="color:red" id="oneCounter">0 字</span>)<font color="red">*</font>
			<div id="two_div" style="width: 100%">
				<hr/><br/><br/>
	    		右侧图片上传：<input maxlength="100" id="twoImage" name="twoImage" style="width:300px;"  value="<#if layoutProduct.twoImage?exists>${layoutProduct.twoImage}</#if>" />
	    		<a onclick="picDialogOpen('twoImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">（左右布局且为单品时可不传）</font><br/>
	    		<span id="twoImageShow">
	    		<#if layoutProduct.twoImage?exists>
    				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="${layoutProduct.twoImage}" style="min-width: 100px;">
    			</#if>
	    		</span><br/>
	    		&nbsp;&nbsp;关联类型：<input type="radio" name="twoType"  id="twoType1" value="1" <#if layoutProduct.twoType?exists && (layoutProduct.twoType == 1 || layoutProduct.twoType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
	    				<input type="radio" name="twoType"  id="twoType2" value="2" <#if layoutProduct.twoType?exists && (layoutProduct.twoType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
	    				<font color="red">必填</font><br/><br/>
				<!-- 特卖类型为单品时 -->
				<div id="twoSingleDiv">
				 &nbsp;&nbsp;&nbsp;商品ID：<input type="text" id="twoProductId" name="twoProductId" style="width:300px;" onblur="stringTrim(this);" value="<#if layoutProduct.twoDisplayId?exists &&(layoutProduct.twoDisplayId !=0) >${layoutProduct.twoDisplayId?c}</#if>"/>
				(<font color="red">必填</font>)&nbsp;<span id="twoProductName" style="color:red"></span><br/><br/>
				</div>
				
				<!-- 特卖类型为组合特卖时 -->
				<div id="twoGroupDiv" style="display:none">
				&nbsp;&nbsp;&nbsp;组合ID：<input id="twoGroupSale" type="text" name="twoGroupSale" style="width:300px" onblur="stringTrim(this);" value="<#if layoutProduct.twoDisplayId?exists &&(layoutProduct.twoDisplayId !=0) >${layoutProduct.twoDisplayId?c}</#if>"/>
				(<font color="red">必填</font>)&nbsp;<span id="twoGroupName" style="color:red"></span><br/><br/>
				</div>
			&nbsp;&nbsp;&nbsp;&nbsp;描述：<span><textarea id="twoDesc" name="twoDesc" cols="40" rows="3" onkeydown="checkEnter(event)"><#if layoutProduct.twoDesc?exists>${layoutProduct.twoDesc}</#if></textarea></span>&nbsp;(字数&lt; 33：<span style="color:red" id="twoCounter">0 字</span>)<font color="red">*</font>
			</div>
			<hr/><br/><br/>
			 &nbsp;&nbsp;是否展现：<input type="radio"  name="isDisplay" value="1" <#if layoutProduct.isDisplay?exists &&(layoutProduct.isDisplay ==1) >checked="checked"</#if>>展现&nbsp;&nbsp;&nbsp;
			<input type="radio"  name="isDisplay" value="0" <#if layoutProduct.isDisplay?exists &&(layoutProduct.isDisplay ==0) >checked="checked"</#if>>不展现<br/><br/>

			
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

	function stringTrim(obj){
		$(obj).val($.trim($(obj).val()));	
	}
	
	$(document).keyup(function() { 
		var text=$("#oneDesc").val(); 
		var counter=strlen(text);
		$("#oneCounter").text(counter+" 字");
	});
	
	
	$(document).keyup(function() { 
		var text=$("#twoDesc").val(); 
		var counter=strlen(text);
		$("#twoCounter").text(counter+" 字");
	});
	
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
	
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        });
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
                            if(inputId == 'oneImage'){
                            	$("#oneImageShow").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
                            }else if(inputId == 'twoImage'){
                            	$("#twoImageShow").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
                            }
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
		$("#displayType1").change(function(){
			if($(this).is(':checked')){
				$('#two_div').hide();
				$('#oneImageSpan').text('单张图片上传');
			}
		});
		
		$("#displayType2").change(function(){
			if($(this).is(':checked')){
				$('#two_div').show();
				$('#oneImageSpan').text('左侧图片上传');
			}
		});
		
		
		$('#oneType1').change(function(){
			if($(this).is(':checked')){
				$('#oneSingleDiv').show();
				$('#oneGroupDiv').hide();
			}
		});
		
		$('#oneType2').change(function(){
			if($(this).is(':checked')){
				$('#oneSingleDiv').hide();
				$('#oneGroupDiv').show();
			}
		});
		
		
		$('#twoType1').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').show();
				$('#twoGroupDiv').hide();
			}
		});
		
		$('#twoType2').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').hide();
				$('#twoGroupDiv').show();
			}
		});
		
		$('#twoType3').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').hide();
				$('#twoGroupDiv').hide();
			}
		});
		
		
		
		$("#displayType1").change();
		$("#displayType2").change();
		$('#oneType1').change();
		$('#oneType2').change();
		$('#twoType1').change();
		$('#twoType2').change();

		
		$('#saveCustomLayout').form({   
		    url:'${rc.contextPath}/special/saveOrUpdateSpecialActivityLayoutProduct',   
		    onSubmit: function(){   
		          var displayType = $("input[name='displayType']:checked").val();
		          var oneType = $("input[name='oneType']:checked").val();
		          var oneProductId = $("#oneProductId").val();
		          var oneGroupSale = $("#oneGroupSale").val();
		          var oneDesc = $("#oneDesc").val();
		          var oneImage = $("#oneImage").val();
		          var twoType = $("input[name='twoType']:checked").val();
		          var twoProductId = $("#twoProductId").val();
		          var twoGroupSale = $("#twoGroupSale").val();
		          var twoDesc = $("#twoDesc").val();
		          var twoImage = $("#twoImage").val();
		          if(displayType == '' || displayType == null || displayType==undefined){
		        	  $.messager.alert('提示','请选择布局方式','warning');
		        	  return false;
		          }else if(oneImage == '' && (oneType != 1 || displayType !=2 )){
		        	  $.messager.alert('提示','请上传图片','warning');
		        	  return false;
		          }else if(oneType == '' || oneType == null || oneType == undefined){
		        	  $.messager.alert('提示','请选择关联类型','warning'); 
		        	  return false;
		          }else if(oneType == 1 && oneProductId == ''){
		        	  $.messager.alert('提示','请填写关联商品Id','warning'); 
		        	  return false;
		          }else if(oneType == 2 && (oneGroupSale == '' || oneGroupSale == null || oneGroupSale == undefined)){
		        	  $.messager.alert('提示','请选择关联组合','warning');
		        	  return false;
		          }else if($.trim(oneDesc)=='' || strlen(oneDesc)>33){
		        	  $.messager.alert('提示','描述必填并且字数小于33','warning');
		        	  return false;
		          }/* else if(displayType == 2){
		        	  if(twoImage == '' && twoType !=1){
		        		  $.messager.alert('提示','请上传右侧图片','warning');
			        	  return false;
		        	  }else if(twoType == '' || twoType == null || twoType == undefined){
		        		  $.messager.alert('提示','请选择右侧关联类型','warning'); 
		        		  return false;
		        	  }else if(twoType == 1 && twoProductId == ''){
			        	  $.messager.alert('提示','请填写关联商品Id','warning'); 
			        	  return false;
			          }else if(twoType == 2 && (twoGroupSale == '' || twoGroupSale == null || twoGroupSale == undefined)){
			        	  $.messager.alert('提示','请选择关联组合','warning');
			        	  return false;
			          }else if($.trim(twoDesc)=='' || strlen(twoDesc)>33){
			        	  $.messager.alert('提示','描述必填并且字数小于33','warning');
			        	  return false;
			          }
		          } */
		          $.messager.progress();
		    },   
		    success:function(data){   
		    	$.messager.progress('close');
                var res = eval("("+data+")");
                if(res.status == 1){
                    $.messager.alert('响应信息',"保存成功",'info',function(){
                        window.location.href = '${rc.contextPath}/special/manageProduct/${layoutId}';
                    });
                } else if(res.status == 0){
                    $.messager.alert('响应信息',res.msg,'info');
                } 
		    }   
		});
	
		
		$("#saveButton").click(function(){
			$('#saveCustomLayout').submit();  
    	});
    	
		$("input[id$='ProductId']").each(function(){
			$(this).change(function(){
				var id = $.trim($(this).val());
				var elementId = $(this).attr('id');
				if(id == ""){
					$("input[id$='ProductName']").each(function(){
						$(this).text("");
					});
				}else{
					$("input[id$='ProductName']").each(function(){
						$(this).text("");
					});
					$.ajax({
		                url: '${rc.contextPath}/product/findProductInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	if(elementId == 'oneProductId'){
		                    		$('#oneProductName').text(data.msg);
		                    	}else if(elementId == 'twoProductId'){
		                    		$('#twoProductName').text(data.msg);
		                    	}
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
		
		
		$("input[id$='GroupSale']").each(function(){
			$(this).change(function(){
				var id = $.trim($(this).val());
				var elementId = $(this).attr('id');
				if(id == ""){
					$("input[id$='GroupName']").each(function(){
						$(this).text("");
					});
				}else{
					$("input[id$='GroupName']").each(function(){
						$(this).text("");
					});
					$.ajax({
		                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	if(elementId == 'oneGroupSale'){
		                    		$('#oneGroupName').text(data.name + "-" + data.remark);
		                    	}else if(elementId == 'twoGroupSale'){
		                    		$('#twoGroupName').text(data.name + "-" + data.remark);
		                    	}
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
	});
</script>

</body>
</html>