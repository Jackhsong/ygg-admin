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
.input{
	width: 360px;
	height: 20px;
}
textarea{ 
	resize:none;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'center',title:'自定义特卖信息'" style="padding:5px;">
	<form id="saveCustomActivities"  method="post">
		<fieldset>
			<input type="hidden" id="id" name="id" value="<#if customActivities.id?exists>${customActivities.id?c}<#else>0</#if>" />
			&nbsp;&nbsp;&nbsp;&nbsp;名称：<input type="text" id="remark" name="remark" class="input" maxlength="50" value="<#if customActivities.remark?exists>${customActivities.remark}</#if>"/>&nbsp;<font color="red">必填</font><br/><br/>
			&nbsp;&nbsp;&nbsp;版本号：<input type="text" id="version" name="version" class="input" maxlength="50" value="<#if customActivities.version?exists>${customActivities.version}<#else>1.0</#if>"/>&nbsp;<font color="red">必填</font><br/><br/>
			&nbsp;&nbsp;&nbsp;&nbsp;类型：<input type="radio" name="type" id="type1" value="1" <#if customActivities.typeCode?exists && (customActivities.typeCode == 1)>checked="checked"</#if>/>抽奖活动&nbsp;&nbsp;
			<input type="radio" name="type" id="type2" value="2" <#if customActivities.typeCode?exists && (customActivities.typeCode == 2)>checked="checked"</#if>/>情景特卖活动&nbsp;&nbsp;
			<input type="radio" name="type" id="type6" value="6" <#if customActivities.typeCode?exists && (customActivities.typeCode == 6)>checked="checked"</#if>/>精品特卖活动&nbsp;&nbsp;
			<input type="radio" name="type" id="type3" value="3" <#if customActivities.typeCode?exists && (customActivities.typeCode == 3)>checked="checked"</#if>/>礼包活动&nbsp;&nbsp;
			<input type="radio" name="type" id="type5" value="5" <#if customActivities.typeCode?exists && (customActivities.typeCode == 5)>checked="checked"</#if>/>任意门&nbsp;&nbsp;
            <input type="radio" name="type" id="type12" value="12" <#if customActivities.typeCode?exists && (customActivities.typeCode == 12)>checked="checked"</#if>/>新情景特卖活动&nbsp;&nbsp;
            <input type="radio" name="type" id="type20" value="20" <#if customActivities.typeCode?exists && (customActivities.typeCode == 20)>checked="checked"</#if>/>组合搭配情景活动&nbsp;&nbsp;
            <input type="radio" name="type" id="type22" value="22" <#if customActivities.typeCode?exists && (customActivities.typeCode == 22)>checked="checked"</#if>/>情景模版&nbsp;&nbsp;
			<input type="radio" name="type" id="type4" value="4" <#if customActivities.typeCode?exists && (customActivities.typeCode == 4)>checked="checked"</#if>/>其他活动&nbsp;&nbsp;
			<font color="red">必填</font><br/><br/>
			<!-- 关联抽奖活动 -->
			<div id="lotteryActivityDiv">
				&nbsp;&nbsp;抽奖活动：<input type="text" id="lotteryActivity" name="lotteryActivity" class="input"/>&nbsp;<font color="red">必填</font><br/><br/>
			</div>
			<!-- 关联情景特卖活动 -->
			<div id="saleActivityDiv">
				&nbsp;&nbsp;情景特卖活动：<input type="text" id="saleActivity" name="saleActivity" class="input"/>&nbsp;<font color="red">必填</font><br/><br/>
			</div>
			<!-- 关联新情景特卖活动 -->
            <div id="newSaleActivityDiv">
                &nbsp;&nbsp;新情景特卖活动：<input type="text" id="newSaleActivity" name="newSaleActivity" class="input"/>&nbsp;<font color="red">必填</font><br/><br/>
            </div>
			<!-- 关联精品特卖活动 -->
			<div id="simplifyActivityDiv">
				&nbsp;&nbsp;精品特卖活动：<input type="text" id="simplifyActivity" name="simplifyActivity" class="input"/>&nbsp;<font color="red">必填</font><br/><br/>
			</div>
			<!-- 关联礼包活动 -->
			<div id="giftActivityDiv">
				&nbsp;&nbsp;礼包活动：<input id="giftActivity" name="giftActivity" class="input"/>&nbsp;<font color="red">必填</font><br/><br/>
			</div>
			<!-- 关联礼包活动 -->
			<div id="otherActivityDiv">
				&nbsp;&nbsp;其他活动：<input id="otherActivity" name="otherActivity" class="input"/>&nbsp;<font color="red">必填</font><br/><br/>
			</div>
			<div id="specialGroupActivityDiv">
				&nbsp;&nbsp;组合搭配情景活动：<input id="specialGroupActivity" name="specialGroupActivity" class="input"/>&nbsp;<font color="red">必填</font><br/><br/>
			</div>
			<div id="specialActivityModelDiv">
				&nbsp;&nbsp;情景模板：<input id="specialActivityModel" name="specialActivityModel" class="input"/>&nbsp;<font color="red">必填</font><br/><br/>
			</div>
			&nbsp;&nbsp;分享标题：<input type="text" id="shareTitle" name="shareTitle" class="input" maxlength="200" value="<#if customActivities.shareTitle?exists >${customActivities.shareTitle}</#if>"/>&nbsp;<font color="red">必填</font><br/>
			<br/>
            朋友圈分享标题：<textarea id="sharePengYouQuanTitle" name="sharePengYouQuanTitle" rows="2" cols="49"><#if customActivities.sharePengYouQuanTitle?exists >${customActivities.sharePengYouQuanTitle}</#if></textarea>&nbsp;<font color="red">必填（长度&lt; 100；当前字数：<span id="sharePengYouQuanTitleCounter" color="red">0</span>）</font><br/><br/>
			腾讯分享内容：<textarea id="shareContentTencent" name="shareContentTencent" rows="3" cols="49"><#if customActivities.shareContentTencent?exists >${customActivities.shareContentTencent}</#if></textarea>&nbsp;<font color="red">必填（长度&lt; 200；当前字数：<span id="shareContentTencentCounter" color="red">0</span>）</font><br/><br/>
			新浪分享内容：<textarea id="shareContentSina" name="shareContentSina" rows="3" cols="49"><#if customActivities.shareContentSina?exists >${customActivities.shareContentSina}</#if></textarea>&nbsp;<font color="red">必填（长度&lt; 140；当前字数：<span id="shareContentSinaCounter" color="red">0</span>）</font><br/><br/>
			&nbsp;&nbsp;分享图标：<input type="text" id="shareImage" name="shareImage" class="input"  value="<#if customActivities.shareImage?exists >${customActivities.shareImage}</#if>"/>&nbsp;
			<a onclick="picDialogOpen('shareImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>&nbsp;
			<font color="red">必填</font><br/>
			<span id="showShareImage">
				<#if customActivities.shareImage?exists >
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="${customActivities.shareImage}" style="max-height: 100px;max-width: 100px;">
				</#if>
			</span>
			<br/><br/>
			是否隐藏分享按钮：
			<input type="radio" name="isHideShareButton" id="isHideShareButton1" value="1" <#if customActivities.isHideShareButton?exists && (customActivities.isHideShareButton ==1)>checked="checked"</#if>>是&nbsp;&nbsp;&nbsp;
			<input type="radio" name="isHideShareButton" id="isHideShareButton0" value="0" <#if customActivities.isHideShareButton?exists && (customActivities.isHideShareButton ==0)>checked="checked"</#if>>否<br/><br/>
			APP分享支持类型：
			<input type="radio" name="shareType" id="shareType1" value="1" <#if customActivities.shareType?exists && (customActivities.shareType ==1)>checked="checked"</#if>>全部&nbsp;&nbsp;&nbsp;
			<input type="radio" name="shareType" id="shareType2" value="2" <#if customActivities.shareType?exists && (customActivities.shareType ==2)>checked="checked"</#if>>微信好友及微信朋友圈<br/><br/>
			APP顶部头区域显示类型：
			<input type="radio" name="headType" id="headType1" value="1" <#if customActivities.headType?exists && (customActivities.headType ==1)>checked="checked"</#if>>默认底色&nbsp;&nbsp;&nbsp;
			<input type="radio" name="headType" id="headType2" value="2" <#if customActivities.headType?exists && (customActivities.headType ==2)>checked="checked"</#if>>透明底色<br/><br/>
			&nbsp;&nbsp;是否可用：<input type="radio" name="isAvailable" id="isAvailable_1" value="1" <#if customActivities.isAvailableCode?exists && (customActivities.isAvailableCode ==1)>checked="checked"</#if>>可用&nbsp;&nbsp;&nbsp;
			<input type="radio" name="isAvailable"  id="isAvailable_1" value="0" <#if customActivities.isAvailableCode?exists && (customActivities.isAvailableCode ==0)>checked="checked"</#if>>停用<br/><br/>
			<input style="width: 100px;height: 30px;cursor: pointer;" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
</div>

<script type="text/javascript">
	
    $(function(){


		$("#sharePengYouQuanTitle").keyup(function(){
			var text = $('#sharePengYouQuanTitle').val();
			$('#sharePengYouQuanTitleCounter').text(text.length);
		});
    	

   		$("#shareContentTencent").keyup(function(){
   			var text = $("#shareContentTencent").val();
   			$("#shareContentTencentCounter").text(text.length);
   		});
   		
   		$("#shareContentSina").keyup(function(){
   			var text = $("#shareContentSina").val();
   			$("#shareContentSinaCounter").text(text.length);
   		});

    	
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
        });
        
        $("#type1").change(function(){
        	if($(this).is(':checked')){
        		$("#lotteryActivityDiv").show();
        		$("#saleActivityDiv").hide();
        		$("#giftActivityDiv").hide();
        		$("#otherActivityDiv").hide();
        		$("#simplifyActivityDiv").hide();
                $("#newSaleActivityDiv").hide();
                $("#specialGroupActivityDiv").hide();
                $("#specialActivityModelDiv").hide();
        	}
        });
        $("#type2").change(function(){
        	if($(this).is(':checked')){
        		$("#lotteryActivityDiv").hide();
        		$("#saleActivityDiv").show();
        		$("#giftActivityDiv").hide();
        		$("#otherActivityDiv").hide();
        		$("#simplifyActivityDiv").hide();
                $("#newSaleActivityDiv").hide();
                $("#specialGroupActivityDiv").hide();
                $("#specialActivityModelDiv").hide();
        	}
        });
        $("#type3").change(function(){
        	if($(this).is(':checked')){
        		$("#lotteryActivityDiv").hide();
        		$("#saleActivityDiv").hide();
        		$("#giftActivityDiv").show();
        		$("#otherActivityDiv").hide();
        		$("#simplifyActivityDiv").hide();
                $("#newSaleActivityDiv").hide();
                $("#specialGroupActivityDiv").hide();
                $("#specialActivityModelDiv").hide();
        	}
        });
        $("#type4").change(function(){
        	if($(this).is(':checked')){
        		$("#lotteryActivityDiv").hide();
        		$("#saleActivityDiv").hide();
        		$("#giftActivityDiv").hide();
        		$("#otherActivityDiv").show();
        		$("#simplifyActivityDiv").hide();
                $("#newSaleActivityDiv").hide();
                $("#specialGroupActivityDiv").hide();
                $("#specialActivityModelDiv").hide();
        	}
        });
        $("#type5").change(function(){
        	if($(this).is(':checked')){
        		$("#lotteryActivityDiv").hide();
        		$("#saleActivityDiv").hide();
        		$("#giftActivityDiv").hide();
        		$("#otherActivityDiv").hide();
        		$("#simplifyActivityDiv").hide();
                $("#newSaleActivityDiv").hide();
                $("#specialGroupActivityDiv").hide();
                $("#specialActivityModelDiv").hide();
        	}
        });
        $("#type6").change(function(){
        	if($(this).is(':checked')){
        		$("#lotteryActivityDiv").hide();
        		$("#saleActivityDiv").hide();
        		$("#giftActivityDiv").hide();
        		$("#otherActivityDiv").hide();
        		$("#simplifyActivityDiv").show();
                $("#newSaleActivityDiv").hide();
                $("#specialGroupActivityDiv").hide();
                $("#specialActivityModelDiv").hide();
        	}
        });
        $("#type12").change(function(){
            if($(this).is(':checked')){
                $("#lotteryActivityDiv").hide();
                $("#saleActivityDiv").hide();
                $("#newSaleActivityDiv").show();
                $("#giftActivityDiv").hide();
                $("#otherActivityDiv").hide();
                $("#simplifyActivityDiv").hide();
                $("#specialGroupActivityDiv").hide();
                $("#specialActivityModelDiv").hide();
            }
        });
        $("#type20").change(function(){
            if($(this).is(':checked')){
                $("#lotteryActivityDiv").hide();
                $("#saleActivityDiv").hide();
                $("#newSaleActivityDiv").hide();
                $("#giftActivityDiv").hide();
                $("#otherActivityDiv").hide();
                $("#simplifyActivityDiv").hide();
                $("#specialGroupActivityDiv").show();
                $("#specialActivityModelDiv").hide();
            }
        });
        $("#type22").change(function(){
            if($(this).is(':checked')){
                $("#lotteryActivityDiv").hide();
                $("#saleActivityDiv").hide();
                $("#newSaleActivityDiv").hide();
                $("#giftActivityDiv").hide();
                $("#otherActivityDiv").hide();
                $("#simplifyActivityDiv").hide();
                $("#specialGroupActivityDiv").hide();
                $("#specialActivityModelDiv").show();
            }
        });
        
        $("#type1").change();
        $("#type2").change();
        $("#type3").change();
        $("#type4").change();
        $("#type5").change();
        $("#type6").change();
        $("#type12").change();
        $("#type20").change();
        $("#type22").change();

        $("#lotteryActivity").combobox({
        	url:'${rc.contextPath}/lotteryActivity/jsonLotteryCode?id=<#if typeId??>${typeId}<#else>0</#if>',
        	valueField:'code',
        	textField:'text'
        });
        
        $("#saleActivity").combobox({
        	url:'${rc.contextPath}/special/jsonSpecialActivityCode?id=<#if typeId??>${typeId}<#else>0</#if>',
        	valueField:'code',
        	textField:'text'
        });

        $("#newSaleActivity").combobox({
            url:'${rc.contextPath}/promotion/jsonPromotionActivityCode?id=<#if typeId??>${typeId}<#else>0</#if>',
            valueField:'code',
            textField:'text'
        });
        
        $("#simplifyActivity").combobox({
        	url:'${rc.contextPath}/simplify/jsonActivitySimplifyCode?id=<#if typeId??>${typeId}<#else>0</#if>',
        	valueField:'code',
        	textField:'text'
        });
        
        $("#giftActivity").combobox({
        	url:'${rc.contextPath}/lotteryActivity/jsonGiftActivityCode?id=<#if typeId??>${typeId}<#else>0</#if>',
        	valueField:'code',
        	textField:'text'
        });
        
        $("#otherActivity").combobox({
        	url:'${rc.contextPath}/pageCustom/jsonPageCustomCode?id=<#if typeId??>${typeId}<#else>0</#if>',
        	valueField:'code',
        	textField:'text'
        });
        
        $("#specialGroupActivity").combobox({
        	url:'${rc.contextPath}/specialGroup/jsonSpecialGroupCode?id=<#if typeId??>${typeId}<#else>0</#if>',
        	valueField:'code',
        	textField:'text'
        });
        $("#specialActivityModel").combobox({
        	url:'${rc.contextPath}/specialActivityModel/jsonSpecialActivityModelCode?id=<#if typeId??>${typeId}<#else>0</#if>',
        	valueField:'code',
        	textField:'text'
        });
        
        $("#saveButton").click(function(){
        	$('#saveCustomActivities').form('submit', {   
        	    url:'${rc.contextPath}/customActivities/saveOrUpdate',   
        	    onSubmit: function(){
        	    	  var remark = $("#remark").val();
        	    	  var version = $("#version").val();
        	          var type = $("input[name='type']:checked").val();
        	          var lotteryActivity = $("#lotteryActivity").combobox('getValue');
        	          var saleActivity = $("#saleActivity").combobox('getValue');
        	          var newSaleActivity = $("#newSaleActivity").combobox('getValue');
        	          var giftActivity = $("#giftActivity").combobox('getValue');
        	          var otherActivity = $("#otherActivity").combobox('getValue');
        	          var specialGroupActivity = $("#specialGroupActivity").combobox('getValue');
        	          var specialActivityModel = $("#specialActivityModel").combobox('getValue');
        	          var shareTitle = $("#shareTitle").val();
        	          var shareContentTencent = $("#shareContentTencent").val();
        	          var shareContentSina = $("#shareContentSina").val();
					  var sharePengYouQuanTitle = $("#sharePengYouQuanTitle").val();
        	          var shareImage = $("#shareImage").val();
        	          var reg = /^[1-9]+.\d{1}$/;
        	          if($.trim(remark)==''){
        	        	  $.messager.alert('提示','请输入自定义活动名称','warning');
        	        	  return false;
        	          }else if($.trim(version)==''){
        	        	  $.messager.alert('提示','请输入版本号','warning');
        	        	  return false;
        	          }else if(!reg.test(version)){
        	        	  $.messager.alert('提示','版本号只能为小数，并且只有一位小数','warning');
        	        	  return false;
        	          }else if(type == '' || type == null || type == undefined){
        	        	  $.messager.alert('提示','请选择自定义活动类型','warning');
        	        	  return false;
        	          }else if(type == 1 && (lotteryActivity == '' || lotteryActivity == null || lotteryActivity == undefined)){
        	        	  $.messager.alert('提示','请选择抽奖活动','warning');
        	        	  return false;
        	          }else if(type == 2 && (saleActivity == '' || saleActivity == null || saleActivity == undefined)){
        	        	  $.messager.alert('提示','请选择特卖活动','warning');
        	        	  return false;
        	          }else if(type == 12 && (newSaleActivity == '' || newSaleActivity == null || newSaleActivity == undefined)){
                          $.messager.alert('提示','请选择特卖活动','warning');
                          return false;
                      }else if(type == 3 && (giftActivity == '' || giftActivity == null || giftActivity == undefined)){
        	        	  $.messager.alert('提示','请选择礼包活动','warning');
        	        	  return false;
        	          }else if(type == 4 && (otherActivity == '' || otherActivity == null || otherActivity == undefined)){
        	        	  $.messager.alert('提示','请选择其他活动','warning');
        	        	  return false;
        	          }else if(type == 20 && (specialGroupActivity == '' || specialGroupActivity == null || specialGroupActivity == undefined)){
        	        	  $.messager.alert('提示','请选择组合搭配情景活动','warning');
        	        	  return false;
        	          }else if(type == 22 && (specialActivityModel == '' || specialActivityModel == null || specialActivityModel == undefined)){
        	        	  $.messager.alert('提示','请选择情景模版','warning');
        	        	  return false;
        	          }else if($.trim(shareTitle) == ''){
        	        	  $.messager.alert('提示','请填写分享标题','warning');
        	        	  return false;
        	          }else if($.trim(sharePengYouQuanTitle) == ''){
        	        	  $.messager.alert('提示','请填写朋友圈分享标题','warning');
						  return false;
					  }else if(sharePengYouQuanTitle.length > 100) {
        	        	  $.messager.alert('提示','朋友圈分享标题请控制在100字以内','warning');
						  return false;
					  } else if($.trim(shareContentTencent) == ''){
        	        	  $.messager.alert('提示','请填写腾讯分享内容','warning');
        	        	  return false;
        	          }else if(shareContentTencent.length>200){
        	        	  $.messager.alert('提示','腾讯分享内容请控制在200字以内','warning');
        	        	  return false;
        	          }else if($.trim(shareContentSina) == ''){
        	        	  $.messager.alert('提示','请填写新浪分享内容','warning');
        	        	  return false;
        	          }else if(shareContentSina.length>140){
        	        	  $.messager.alert('提示','新浪分享内容请控制在140个字以内','warning');
        	        	  return false;
        	          }else if($.trim(shareImage) == ''){
        	        	  $.messager.alert('提示','请上传分享图标','warning');
        	        	  return false;
        	          }
        	          $.messager.progress();
        	    },   
        	    success:function(data){
        	    	$.messager.progress('close');
        	    	var res = eval("("+data+")");
        	        if(res.status == 1){
        	        	$.messager.alert('响应消息','保存成功','info',function(){
        	        		window.location.href='${rc.contextPath}/customActivities/list';
        	        	});
        	        }else{
        	        	$.messager.alert('响应消息',res.msg,'error');
        	        }
        	    }   
        	});
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
                            $("#showShareImage").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt='' src='"+res.url+"' style='max-height: 100px;max-width: 100px;'>");
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

<script>
	
</script>

</body>
</html>