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

<div data-options="region:'center',title:'一级分类资源位'" style="padding:5px;">
	<form id="saveFirstWindow"  method="post">
		<fieldset>
			<input type="hidden" id="id" name="id" value="<#if fwindow.id?exists>${fwindow.id?c}<#else>0</#if>"/>
			<legend>一级分类资源位</legend>
    		&nbsp;&nbsp;&nbsp;&nbsp;备注：<input maxlength="100" id="remark" name="remark" style="width:300px;"  value="<#if fwindow.remark?exists>${fwindow.remark}</#if>" /><br/><br/>
    		&nbsp;&nbsp;一级分类：<input id="categoryFirstId" name="categoryFirstId" style="width:300px;"/><br/><br/>
    		<span style="color:red;">左侧BANNER管理：</span><br/><br/>
    		&nbsp;&nbsp;BANNER图：<input maxlength="100" id="leftImage" name="leftImage" style="width:300px;"  value="<#if fwindow.leftImage?exists>${fwindow.leftImage}</#if>" />
    		<a onclick="picDialogOpen('leftImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">尺寸：260x160</font><br/><br/>
			&nbsp;&nbsp;左侧类型：<input type="radio" name="leftRelationType"  id="leftRelationType1" value="1" <#if fwindow.leftRelationType?exists && (fwindow.leftRelationType == 1)>checked="checked"</#if>> 特卖单品&nbsp;&nbsp;
   				<input type="radio" name="leftRelationType"  id="leftRelationType2" value="2" <#if fwindow.leftRelationType?exists && (fwindow.leftRelationType == 2)>checked="checked"</#if>> 组合特卖&nbsp;&nbsp;
   				<input type="radio" name="leftRelationType"  id="leftRelationType3" value="3" <#if fwindow.leftRelationType?exists && (fwindow.leftRelationType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
   				<input type="radio" name="leftRelationType"  id="leftRelationType4" value="4" <#if fwindow.leftRelationType?exists && (fwindow.leftRelationType == 4)>checked="checked"</#if>> 商城单品&nbsp;&nbsp;
			<br/><br/>
			
			<!-- 特卖单品 -->
			<div id="leftSaleProductDiv">
			 &nbsp;特卖单品ID：<input type="text" id="leftSaleProductId" name="leftSaleProductId" style="width:300px;"  onblur="stringTrim(this);" value="<#if fwindow.leftRelationObjectId?exists &&(fwindow.leftRelationObjectId !=0) >${fwindow.leftRelationObjectId?c}</#if>" />
			 <font color="red">必填</font>&nbsp;<span id="leftSaleProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖组合 -->
			<div id="leftGroupSaleDiv" style="display:none">
			 &nbsp;特卖组合ID：<input id="leftGroupSaleId" type="text" name="leftGroupSaleId" style="width:300px" onblur="stringTrim(this);" value="<#if fwindow.leftRelationObjectId?exists &&(fwindow.leftRelationObjectId !=0) >${fwindow.leftRelationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="leftGroupSaleName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 商城单品 -->
			<div id="leftMallProductDiv" style="display:none">
			&nbsp;商城单品ID：<input id="leftMallProductId" type="text" name="leftMallProductId" style="width:300px" onblur="stringTrim(this);" value="<#if fwindow.leftRelationObjectId?exists &&(fwindow.leftRelationObjectId !=0) >${fwindow.leftRelationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="leftMallProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 自定义活动 -->
			<div id="leftCustomActivityDiv" style="display:none">
			 &nbsp;自定义活动ID：<input type="text" id="leftCustomActivityId" name="leftCustomActivityId" style="width:300px;" onblur="stringTrim(this);" value="<#if fwindow.leftRelationObjectId?exists &&(fwindow.leftRelationObjectId !=0) >${fwindow.leftRelationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="leftCustomActivityName" style="color:red"></span><br/><br/>
			</div>
			
			<span style="color:red;">右侧BANNER管理：</span><br/><br/>
    		&nbsp;&nbsp;BANNER图：<input maxlength="100" id="rightImage" name="rightImage" style="width:300px;"  value="<#if fwindow.rightImage?exists>${fwindow.rightImage}</#if>" />
    		<a onclick="picDialogOpen('rightImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">尺寸：260x160</font><br/><br/>
			&nbsp;&nbsp;右侧类型：<input type="radio" name="rightRelationType"  id="rightRelationType1" value="1" <#if fwindow.rightRelationType?exists && (fwindow.rightRelationType == 1)>checked="checked"</#if>> 特卖单品&nbsp;&nbsp;
   				<input type="radio" name="rightRelationType"  id="rightRelationType2" value="2" <#if fwindow.rightRelationType?exists && (fwindow.rightRelationType == 2)>checked="checked"</#if>> 组合特卖&nbsp;&nbsp;
   				<input type="radio" name="rightRelationType"  id="rightRelationType3" value="3" <#if fwindow.rightRelationType?exists && (fwindow.rightRelationType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
   				<input type="radio" name="rightRelationType"  id="rightRelationType4" value="4" <#if fwindow.rightRelationType?exists && (fwindow.rightRelationType == 4)>checked="checked"</#if>> 商城单品&nbsp;&nbsp;
			<br/><br/>
			
			<!-- 特卖单品 -->
			<div id="rightSaleProductDiv">
			 &nbsp;特卖单品ID：<input type="text" id="rightSaleProductId" name="rightSaleProductId" style="width:300px;"  onblur="stringTrim(this);" value="<#if fwindow.rightRelationObjectId?exists &&(fwindow.rightRelationObjectId !=0) >${fwindow.rightRelationObjectId?c}</#if>" />
			 <font color="red">必填</font>&nbsp;<span id="rightSaleProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖组合 -->
			<div id="rightGroupSaleDiv" style="display:none">
			&nbsp;特卖组合ID：<input id="rightGroupSaleId" type="text" name="rightGroupSaleId" style="width:300px" onblur="stringTrim(this);" value="<#if fwindow.rightRelationObjectId?exists &&(fwindow.rightRelationObjectId !=0) >${fwindow.rightRelationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="rightGroupSaleName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 商城单品 -->
			<div id="rightMallProductDiv" style="display:none">
			 &nbsp;商城单品ID：<input id="rightMallProductId" type="text" name="rightMallProductId" style="width:300px" onblur="stringTrim(this);" value="<#if fwindow.rightRelationObjectId?exists &&(fwindow.rightRelationObjectId !=0) >${fwindow.rightRelationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="rightMallProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 自定义活动 -->
			<div id="rightCustomActivityDiv" style="display:none">
			 自定义活动ID：<input type="text" id="rightCustomActivityId" name="rightCustomActivityId" style="width:300px;" onblur="stringTrim(this);" value="<#if fwindow.rightRelationObjectId?exists &&(fwindow.rightRelationObjectId !=0) >${fwindow.rightRelationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="rightCustomActivityName" style="color:red"></span><br/><br/>
			</div>			

			 左右可用状态：
			<#if fwindow.id?exists && (fwindow.id > 0) >
			 <input type="radio"  name="isAvailable" value="1" <#if fwindow.isAvailable?exists &&(fwindow.isAvailable ==1) >checked="checked"</#if>>可用&nbsp;&nbsp;&nbsp;
			</#if> 
			<input type="radio"  name="isAvailable" value="0" <#if fwindow.isAvailable?exists &&(fwindow.isAvailable ==0) >checked="checked"</#if>>不可用<br/><br/>
			
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <input type="hidden" name="needWidth" id="needWidth" value="260">
        <input type="hidden" name="needHeight" id="needHeight" value="160">
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
		
		$('#leftRelationType1').change(function(){
			if($(this).is(':checked')){
				$('#leftSaleProductDiv').show();
				$('#leftGroupSaleDiv').hide();
				$('#leftMallProductDiv').hide();
				$('#leftCustomActivityDiv').hide();
			}
		});
		
		$('#leftRelationType2').change(function(){
			if($(this).is(':checked')){
				$('#leftSaleProductDiv').hide();
				$('#leftGroupSaleDiv').show();
				$('#leftMallProductDiv').hide();
				$('#leftCustomActivityDiv').hide();
			}
		});
		
		$('#leftRelationType3').change(function(){
			if($(this).is(':checked')){
				$('#leftSaleProductDiv').hide();
				$('#leftGroupSaleDiv').hide();
				$('#leftMallProductDiv').hide();
				$('#leftCustomActivityDiv').show();
			}
		});
		
		$('#leftRelationType4').change(function(){
			if($(this).is(':checked')){
				$('#leftSaleProductDiv').hide();
				$('#leftGroupSaleDiv').hide();
				$('#leftMallProductDiv').show();
				$('#leftCustomActivityDiv').hide();
			}
		});
		
		
		$('#rightRelationType1').change(function(){
			if($(this).is(':checked')){
				$('#rightSaleProductDiv').show();
				$('#rightGroupSaleDiv').hide();
				$('#rightMallProductDiv').hide();
				$('#rightCustomActivityDiv').hide();
			}
		});
		
		$('#rightRelationType2').change(function(){
			if($(this).is(':checked')){
				$('#rightSaleProductDiv').hide();
				$('#rightGroupSaleDiv').show();
				$('#rightMallProductDiv').hide();
				$('#rightCustomActivityDiv').hide();
			}
		});
		
		$('#rightRelationType3').change(function(){
			if($(this).is(':checked')){
				$('#rightSaleProductDiv').hide();
				$('#rightGroupSaleDiv').hide();
				$('#rightMallProductDiv').hide();
				$('#rightCustomActivityDiv').show();
			}
		});
		
		$('#rightRelationType4').change(function(){
			if($(this).is(':checked')){
				$('#rightSaleProductDiv').hide();
				$('#rightGroupSaleDiv').hide();
				$('#rightMallProductDiv').show();
				$('#rightCustomActivityDiv').hide();
			}
		});
		
		
		$('#leftRelationType1').change();
		$('#leftRelationType2').change();
		$('#leftRelationType3').change();
		$('#leftRelationType4').change();
		$('#rightRelationType1').change();
		$('#rightRelationType2').change();
		$('#rightRelationType3').change();
		$('#rightRelationType4').change();
		
		$('#categoryFirstId').combobox({   
		    url:'${rc.contextPath}/category/jsonCategoryFirstCode?id=<#if fwindow.firstCategoryId?exists && (fwindow.firstCategoryId !=0)>${fwindow.firstCategoryId?c}<#else>0</#if>',   
		    valueField:'code',   
		    textField:'text',
		    editable:false
		});
		
		$('#saveFirstWindow').form({   
		    url:'${rc.contextPath}/category/saveOrUpdateCategoryFirstWindow',   
		    onSubmit: function(){
		          var leftRelationType = $("input[name='leftRelationType']:checked").val();
		          var remark = $("#remark").val();
		          var categoryFirstId = $("#categoryFirstId").combobox('getValue');
		          var leftSaleProductId = $("#leftSaleProductId").val();
		          var leftGroupSaleId = $("#leftGroupSaleId").val();
		          var leftMallProductId = $("#leftMallProductId").val();
		          var leftCustomActivityId = $("#leftCustomActivityId").val();
		          var leftImage = $("#leftImage").val();
		          var rightRelationType = $("input[name='rightRelationType']:checked").val();
		          var rightSaleProductId = $("#rightSaleProductId").val();
		          var rightGroupSaleId = $("#rightGroupSaleId").val();
		          var rightMallProductId = $("#rightMallProductId").val();
		          var rightCustomActivityId = $("#rightCustomActivityId").val();
		          var rightImage = $("#rightImage").val();
		          if($.trim(remark) == ''){
		        	  $.messager.alert('提示','请填写备注','error'); 
		        	  return false;
		          }else if(categoryFirstId == '' || categoryFirstId == null || categoryFirstId == undefined){
		        	  $.messager.alert('提示','请选择一级分类','error'); 
		        	  return false;
		          }else if(leftRelationType == '' || leftRelationType == null || leftRelationType == undefined){
		        	  $.messager.alert('提示','请选择左侧类型','error'); 
		        	  return false;
		          }else if(leftRelationType == 1 && $.trim(leftSaleProductId) == ''){
		        	  $.messager.alert('提示','请填写左侧写特卖单品Id','error'); 
		        	  return false;
		          }else if(leftRelationType == 2 && $.trim(leftGroupSaleId) == ''){
		        	  $.messager.alert('提示','请填写左侧写特卖组合Id','error');
		        	  return false;
		          }else if(leftRelationType == 4 && $.trim(leftMallProductId) == ''){
		        	  $.messager.alert('提示','请填写左侧商城单品Id','error');
		        	  return false;
		          }else if(leftRelationType == 3 && $.trim(leftCustomActivityId) == ''){
		        	  $.messager.alert('提示','请填写左侧自定义活动Id','error');
		        	  return false;
		          }else if($.trim(leftImage) == ''){
	        		  $.messager.alert('提示','请左侧上传图片','error');
		        	  return false;
		          }else if(rightRelationType == '' || rightRelationType == null || rightRelationType == undefined){
		        	  $.messager.alert('提示','请选择右侧类型','error'); 
		        	  return false;
		          }else if(rightRelationType == 1 && $.trim(rightSaleProductId) == ''){
		        	  $.messager.alert('提示','请填写右侧写特卖单品Id','error'); 
		        	  return false;
		          }else if(rightRelationType == 2 && $.trim(rightGroupSaleId) == ''){
		        	  $.messager.alert('提示','请填写右侧写特卖组合Id','error');
		        	  return false;
		          }else if(rightRelationType == 4 && $.trim(rightMallProductId) == ''){
		        	  $.messager.alert('提示','请填写右侧商城单品Id','error');
		        	  return false;
		          }else if(rightRelationType == 3 && $.trim(rightCustomActivityId) == ''){
		        	  $.messager.alert('提示','请填写右侧自定义活动Id','error');
		        	  return false;
		          }else if($.trim(rightImage) == ''){
	        		  $.messager.alert('提示','请右侧上传图片','error');
		        	  return false;
		          }
		          $.messager.progress();
		    },   
		    success:function(data){   
		    	$.messager.progress('close');
                var res = eval("("+data+")");
                if(res.status == 1){
                    $.messager.alert('响应信息',"保存成功",'info',function(){
                        window.location.href = '${rc.contextPath}/category/fwindow';
                    });
                } else if(res.status == 0){
                    $.messager.alert('响应信息',res.msg,'error');
                } 
		    }   
		});
	
		
		$("#saveButton").click(function(){
			$('#saveFirstWindow').submit();  
    	});
    	
		$("input[id$='SaleProductId']").each(function(){
			$(this).change(function(){
				var id = $.trim($(this).val());
				var elementId = $(this).attr('id');
				if(id == ""){
					$("input[id$='SaleProductName']").each(function(){
						$(this).text("");
					});
				}else{
					$("input[id$='SaleProductName']").each(function(){
						$(this).text("");
					});
					$.ajax({
		                url: '${rc.contextPath}/product/findProductInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id,'type':1},
		                success: function(data){
		                    if(data.status == 1){
		                    	if(elementId == 'leftSaleProductId'){
		                    		$('#leftSaleProductName').text(data.msg);
		                    	}else if(elementId == 'rightSaleProductId'){
		                    		$('#rightSaleProductName').text(data.msg);
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
		
		
		$("input[id$='GroupSaleId']").each(function(){
			$(this).change(function(){
				var id = $.trim($(this).val());
				var elementId = $(this).attr('id');
				if(id == ""){
					$("input[id$='GroupSaleName']").each(function(){
						$(this).text("");
					});
				}else{
					$("input[id$='GroupSaleName']").each(function(){
						$(this).text("");
					});
					$.ajax({
		                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	if(elementId == 'leftGroupSaleId'){
		                    		$('#leftGroupSaleName').text(data.name + "-" + data.remark);
		                    	}else if(elementId == 'rightGroupSaleId'){
		                    		$('#rightGroupSaleName').text(data.name + "-" + data.remark);
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
		
		$("input[id$='MallProductId']").each(function(){
			$(this).change(function(){
				var id = $.trim($(this).val());
				var elementId = $(this).attr('id');
				if(id == ""){
					$("input[id$='MallProductName']").each(function(){
						$(this).text("");
					});
				}else{
					$("input[id$='MallProductName']").each(function(){
						$(this).text("");
					});
					$.ajax({
		                url: '${rc.contextPath}/product/findProductInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id,'type':2},
		                success: function(data){
		                    if(data.status == 1){
		                    	if(elementId == 'leftMallProductId'){
		                    		$('#leftMallProductName').text(data.msg);
		                    	}else if(elementId == 'rightMallProductId'){
		                    		$('#rightMallProductName').text(data.msg);
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
		
		$("input[id$='CustomActivityId']").each(function(){
			$(this).change(function(){
				var id = $.trim($(this).val());
				var elementId = $(this).attr('id');
				if(id == ""){
					$("input[id$='CustomActivityName']").each(function(){
						$(this).text("");
					});
				}else{
					$("input[id$='CustomActivityName']").each(function(){
						$(this).text("");
					});
					$.ajax({
		                url: '${rc.contextPath}/customActivities/findCustomActivitiesInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	if(elementId == 'leftCustomActivityId'){
		                    		$('#leftCustomActivityName').text(data.name+"-"+data.remark);
		                    	}else if(elementId == 'rightCustomActivityId'){
		                    		$('#rightCustomActivityName').text(data.name+"-"+data.remark);
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