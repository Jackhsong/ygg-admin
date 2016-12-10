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

<div data-options="region:'center',title:'分类活动管理'" style="padding:5px;">
	<form id="saveCategoryActivity"  method="post">
		<fieldset>
			<input type="hidden" id="id" name="id" value="<#if activity.id?exists>${activity.id?c}<#else>0</#if>"/>
			<legend>分类活动</legend>
    		&nbsp;&nbsp;&nbsp;&nbsp;备注：<input maxlength="100" id="remark" name="remark" style="width:300px;"  value="<#if activity.remark?exists>${activity.remark}</#if>" /><br/><br/>
    		&nbsp;&nbsp;BANNER图：<input maxlength="100" id="image" name="image" style="width:300px;"  value="<#if activity.image?exists>${activity.image}</#if>" />
    		<a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">尺寸：640x306</font><br/><br/>
			&nbsp;&nbsp;&nbsp;&nbsp;类型：<input type="radio" name="relationType"  id="relationType1" value="1" <#if activity.relationType?exists && (activity.relationType == 1)>checked="checked"</#if>> 特卖单品&nbsp;&nbsp;
   				<input type="radio" name="relationType"  id="relationType2" value="2" <#if activity.relationType?exists && (activity.relationType == 2)>checked="checked"</#if>> 组合特卖&nbsp;&nbsp;
   				<input type="radio" name="relationType"  id="relationType3" value="3" <#if activity.relationType?exists && (activity.relationType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
   				<input type="radio" name="relationType"  id="relationType4" value="4" <#if activity.relationType?exists && (activity.relationType == 4)>checked="checked"</#if>> 商城单品&nbsp;&nbsp;
			<br/><br/>
			
			<!-- 特卖单品 -->
			<div id="saleProductDiv">
			 &nbsp;特卖单品ID：<input type="text" id="saleProductId" name="saleProductId" style="width:300px;"  onblur="stringTrim(this);" value="<#if activity.relationObjectId?exists &&(activity.relationObjectId !=0) >${activity.relationObjectId?c}</#if>" />
			 <font color="red">必填</font>&nbsp;<span id="saleProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖组合 -->
			<div id="groupSaleDiv" style="display:none">
			 &nbsp;特卖组合ID：<input id="groupSaleId" type="text" name="groupSaleId" style="width:300px" onblur="stringTrim(this);" value="<#if activity.relationObjectId?exists &&(activity.relationObjectId !=0) >${activity.relationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="groupSaleName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 商城单品 -->
			<div id="mallProductDiv" style="display:none">
			&nbsp;商城单品ID：<input id="mallProductId" type="text" name="mallProductId" style="width:300px" onblur="stringTrim(this);" value="<#if activity.relationObjectId?exists &&(activity.relationObjectId !=0) >${activity.relationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="mallProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 自定义活动 -->
			<div id="customActivityDiv" style="display:none">
			 &nbsp;自定义活动ID：<input type="text" id="customActivityId" name="customActivityId" style="width:300px;" onblur="stringTrim(this);" value="<#if activity.relationObjectId?exists &&(activity.relationObjectId !=0) >${activity.relationObjectId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="customActivityName" style="color:red"></span><br/><br/>
			</div>
			
			 &nbsp;&nbsp;可用状态：
			<#if activity.id?exists && (activity.id > 0) >
			 <input type="radio"  name="isAvailable" value="1" <#if activity.isAvailable?exists &&(activity.isAvailable ==1) >checked="checked"</#if>>可用&nbsp;&nbsp;&nbsp;
			</#if> 
			<input type="radio"  name="isAvailable" value="0" <#if activity.isAvailable?exists &&(activity.isAvailable ==0) >checked="checked"</#if>>不可用<br/><br/>
			
			<input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <input type="hidden" name="needWidth" id="needWidth" value="640">
        <input type="hidden" name="needHeight" id="needHeight" value="306">
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
		
		$('#relationType1').change(function(){
			if($(this).is(':checked')){
				$('#saleProductDiv').show();
				$('#groupSaleDiv').hide();
				$('#mallProductDiv').hide();
				$('#customActivityDiv').hide();
			}
		});
		
		$('#relationType2').change(function(){
			if($(this).is(':checked')){
				$('#saleProductDiv').hide();
				$('#groupSaleDiv').show();
				$('#mallProductDiv').hide();
				$('#customActivityDiv').hide();
			}
		});
		
		$('#relationType3').change(function(){
			if($(this).is(':checked')){
				$('#saleProductDiv').hide();
				$('#groupSaleDiv').hide();
				$('#mallProductDiv').hide();
				$('#customActivityDiv').show();
			}
		});
		
		$('#relationType4').change(function(){
			if($(this).is(':checked')){
				$('#saleProductDiv').hide();
				$('#groupSaleDiv').hide();
				$('#mallProductDiv').show();
				$('#customActivityDiv').hide();
			}
		});
		
		
		$('#relationType1').change();
		$('#relationType2').change();
		$('#relationType3').change();
		$('#relationType4').change();
		
		
		$('#saveCategoryActivity').form({   
		    url:'${rc.contextPath}/category/saveOrUpdateCategoryActivity',   
		    onSubmit: function(){
		          var relationType = $("input[name='relationType']:checked").val();
		          var remark = $("#remark").val();
		          var saleProductId = $("#saleProductId").val();
		          var groupSaleId = $("#groupSaleId").val();
		          var mallProductId = $("#mallProductId").val();
		          var customActivityId = $("#customActivityId").val();
		          var image = $("#image").val();
		          if($.trim(remark) == ''){
		        	  $.messager.alert('提示','请填写备注','error'); 
		        	  return false;
		          }else if(relationType == '' || relationType == null || relationType == undefined){
		        	  $.messager.alert('提示','请选择类型','error'); 
		        	  return false;
		          }else if(relationType == 1 && $.trim(saleProductId) == ''){
		        	  $.messager.alert('提示','请填写特卖单品Id','error'); 
		        	  return false;
		          }else if(relationType == 2 && $.trim(groupSaleId) == ''){
		        	  $.messager.alert('提示','请填写特卖组合Id','error');
		        	  return false;
		          }else if(relationType == 4 && $.trim(mallProductId) == ''){
		        	  $.messager.alert('提示','请填写商城单品Id','error');
		        	  return false;
		          }else if(relationType == 3 && $.trim(customActivityId) == ''){
		        	  $.messager.alert('提示','请填写自定义活动Id','error');
		        	  return false;
		          }else if($.trim(image) == ''){
	        		  $.messager.alert('提示','请左侧上传图片','error');
		        	  return false;
		          }
		          $.messager.progress();
		    },   
		    success:function(data){   
		    	$.messager.progress('close');
                var res = eval("("+data+")");
                if(res.status == 1){
                    $.messager.alert('响应信息',"保存成功",'info',function(){
                        window.location.href = '${rc.contextPath}/category/activity';
                    });
                } else if(res.status == 0){
                    $.messager.alert('响应信息',res.msg,'error');
                } 
		    }   
		});
	
		
		$("#saveButton").click(function(){
			$('#saveCategoryActivity').submit();  
    	});
    	
		$("#saleProductId").change(function(){
			var id = $.trim($(this).val());
			if(id == ""){
				$("#saleProductName").text("");
			}else{
				$("#saleProductName").text("");
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id,'type':1},
	                success: function(data){
	                    if(data.status == 1){
                    		$('#saleProductName').text(data.msg);
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
		
		
		$("#groupSaleId").change(function(){
			var id = $.trim($(this).val());
			if(id == ""){
				$("#groupSaleName").text("");
			}else{
				$("#groupSaleName").text("");
				$.ajax({
	                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
                    		$('#groupSaleName').text(data.name + "-" + data.remark);
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
		
		$("#mallProductId").change(function(){
			var id = $.trim($(this).val());
			if(id == ""){
				$("#mallProductName").text("");
			}else{
				$("#mallProductName").text("");
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id,'type':2},
	                success: function(data){
	                    if(data.status == 1){
                    		$('#mallProductName').text(data.msg);
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
		
		$("#customActivityId").change(function(){
			var id = $.trim($(this).val());
			var elementId = $(this).attr('id');
			if(id == ""){
				$("#customActivityName").text("");
			}else{
				$("#customActivityName").text("");
				$.ajax({
	                url: '${rc.contextPath}/customActivities/findCustomActivitiesInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
                    		$('#customActivityName').text(data.name+"-"+data.remark);
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