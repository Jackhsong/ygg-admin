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

<div data-options="region:'center',title:'板块布局信息-旧版本'" style="padding:5px;">
	<form id="saveCustomLayout"  method="post">
		<fieldset>
			<input type="hidden" id="id" name="id" value="<#if customLayout.layoutId?exists>${customLayout.layoutId?c}<#else>0</#if>"/>
			<input type="hidden" id="regionId" name="regionId" value="${regionId}"/>
			<legend>自定义布局</legend>
			 选择布局方式：<input type="radio" name="displayType"  id="displayType1" value="1" <#if customLayout.displayType?exists &&(customLayout.displayType==1)>checked="checked"</#if>> 一行1张&nbsp;&nbsp;&nbsp;
					<input type="radio" name="displayType"  id="displayType2" value="2" <#if customLayout.displayType?exists &&(customLayout.displayType==2)>checked="checked"</#if>> 一行2张&nbsp;&nbsp;&nbsp;
					<input type="radio" name="displayType"  id="displayType3" value="3" <#if customLayout.displayType?exists &&(customLayout.displayType==3)>checked="checked"</#if>> 一行4张&nbsp;&nbsp;&nbsp;
					<font color="red">必填</font><br/><br/>
    		&nbsp;&nbsp;

			<!--  ################## 第一张 begin ################## -->
			<span id="oneRemarkSpan">第一张备注</span>：<input maxlength="100" id="oneRemark" name="oneRemark" style="width:300px;"  value="<#if customLayout.oneRemark?exists>${customLayout.oneRemark}</#if>" /><br/><br/>
    		<span id="oneImageSpan">第一张图片上传</span>：<input maxlength="100" id="oneImage" name="oneImage" style="width:300px;"  value="<#if customLayout.oneImage?exists>${customLayout.oneImage}</#if>" />
    		<a onclick="picDialogOpen('oneImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">必填</font>&nbsp;<span id="oneImageTip" style="color:red">宽度：730px</span><br/>
    		<span id="oneImageShow">
    		<#if customLayout.oneImage?exists>
    			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="${customLayout.oneImage}" style="min-width: 100px;">
    		</#if>
    		</span><br/><br/>
    		&nbsp;&nbsp;关联类型：
				<input type="radio" name="oneType"  id="oneType1" value="1" <#if customLayout.oneType?exists && (customLayout.oneType == 1 || customLayout.oneType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType2" value="2" <#if customLayout.oneType?exists && (customLayout.oneType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType3" value="3" <#if customLayout.oneType?exists && (customLayout.oneType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType5" value="5" <#if customLayout.oneType?exists && (customLayout.oneType == 5)>checked="checked"</#if>> 签到&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType6" value="6" <#if customLayout.oneType?exists && (customLayout.oneType == 6)>checked="checked"</#if>> 邀请小伙伴&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType7" value="7" <#if customLayout.oneType?exists && (customLayout.oneType == 7)>checked="checked"</#if>> 原生自定义页面&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType8" value="8" <#if customLayout.oneType?exists && (customLayout.oneType == 8)>checked="checked"</#if>> 图片点击不跳转&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType9" value="9" <#if customLayout.oneType?exists && (customLayout.oneType == 9)>checked="checked"</#if>> 积分商城&nbsp;&nbsp;
    			<font color="red">必填</font><br/><br/>
			<!-- 特卖类型为单品时 -->
			<div id="oneSingleDiv">
			 &nbsp;&nbsp;&nbsp;商品ID：<input type="text" id="oneProductId" name="oneProductId" style="width:300px;"  onblur="stringTrim(this);" value="<#if customLayout.oneDisplayId?exists &&(customLayout.oneDisplayId !=0) >${customLayout.oneDisplayId?c}</#if>" />
			(<font color="red">必填</font>)&nbsp;<span id="oneProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为组合特卖时 -->
			<div id="oneGroupDiv" style="display:none">
			 &nbsp;&nbsp;&nbsp;组合ID：<input id="oneGroupSale" type="text" name="oneGroupSale" style="width:300px" onblur="stringTrim(this);" value="<#if customLayout.oneDisplayId?exists &&(customLayout.oneDisplayId !=0) >${customLayout.oneDisplayId?c}</#if>"/>
			 (<font color="red">必填</font>)&nbsp;<span id="oneGroupName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为自定义活动时 -->
			<div id="oneCustomSaleDiv" style="display:none">
			 &nbsp;自定义活动：<input type="text" id="oneCustomSale" name="oneCustomSale" style="width:300px;"/>
			 <br/><br/>
			</div>

            <!-- 特卖类型为原生自定义页面时 -->
            <div id="oneAppCustomPageDiv" style="display:none">
                &nbsp;原生自定义页面：<input type="text" id="oneAppCustomPage" name="oneAppCustomPage" style="width:300px;"/>
                <br/><br/>
            </div>
            <!--  ################## 第一张 end ################## -->

            <!--  ################## 第二张 begin ################## -->
			<div id="two_div" style="width: 100%">
				<hr/><br/><br/>
				&nbsp;&nbsp;第二张备注：<input maxlength="100" id="twoRemark" name="twoRemark" style="width:300px;"  value="<#if customLayout.twoRemark?exists>${customLayout.twoRemark}</#if>" /><br/><br/>
                第二张图片上传：<input maxlength="100" id="twoImage" name="twoImage" style="width:300px;"  value="<#if customLayout.twoImage?exists>${customLayout.twoImage}</#if>" />
	    		<a onclick="picDialogOpen('twoImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">必填</font>&nbsp;<span id="twoImageTip" style="color:red">宽度：360px</span><br/>
	    		<span id="twoImageShow">
	    		<#if customLayout.twoImage?exists>
    				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="${customLayout.twoImage}" style="min-width: 100px;">
    			</#if>
	    		</span><br/><br/>
	    		&nbsp;&nbsp;关联类型：
					<input type="radio" name="twoType"  id="twoType1" value="1" <#if customLayout.twoType?exists && (customLayout.twoType == 1 || customLayout.twoType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
	    			<input type="radio" name="twoType"  id="twoType2" value="2" <#if customLayout.twoType?exists && (customLayout.twoType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
	    			<input type="radio" name="twoType"  id="twoType3" value="3" <#if customLayout.twoType?exists && (customLayout.twoType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
	    			<input type="radio" name="twoType"  id="twoType5" value="5" <#if customLayout.twoType?exists && (customLayout.twoType == 5)>checked="checked"</#if>> 签到&nbsp;&nbsp;
	    			<input type="radio" name="twoType"  id="twoType6" value="6" <#if customLayout.twoType?exists && (customLayout.twoType == 6)>checked="checked"</#if>> 邀请小伙伴&nbsp;&nbsp;
                	<input type="radio" name="twoType"  id="twoType7" value="7" <#if customLayout.twoType?exists && (customLayout.twoType == 7)>checked="checked"</#if>> 原生自定义页面&nbsp;&nbsp;
                    <input type="radio" name="twoType"  id="twoType8" value="8" <#if customLayout.twoType?exists && (customLayout.twoType == 8)>checked="checked"</#if>> 图片点击不跳转&nbsp;&nbsp;
                    <input type="radio" name="twoType"  id="twoType9" value="9" <#if customLayout.twoType?exists && (customLayout.twoType == 9)>checked="checked"</#if>> 积分商城&nbsp;&nbsp;
                    <font color="red">必填</font><br/><br/>
				<!-- 特卖类型为单品时 -->
				<div id="twoSingleDiv">
				 &nbsp;&nbsp;&nbsp;商品ID：<input type="text" id="twoProductId" name="twoProductId" style="width:300px;" onblur="stringTrim(this);" value="<#if customLayout.twoDisplayId?exists &&(customLayout.twoDisplayId !=0) >${customLayout.twoDisplayId?c}</#if>"/>
				(<font color="red">必填</font>)&nbsp;<span id="twoProductName" style="color:red"></span><br/><br/>
				</div>
				
				<!-- 特卖类型为组合特卖时 -->
				<div id="twoGroupDiv" style="display:none">
				&nbsp;&nbsp;&nbsp;组合ID：<input id="twoGroupSale" type="text" name="twoGroupSale" style="width:300px" onblur="stringTrim(this);" value="<#if customLayout.twoDisplayId?exists &&(customLayout.twoDisplayId !=0) >${customLayout.twoDisplayId?c}</#if>"/>
				(<font color="red">必填</font>)&nbsp;<span id="twoGroupName" style="color:red"></span><br/><br/>
				</div>
				
				<!-- 特卖类型为自定义活动时 -->
				<div id="twoCustomSaleDiv" style="display:none">
				&nbsp;自定义活动：<input type="text" id="twoCustomSale" name="twoCustomSale" style="width:300px;"/>
				 <br/><br/>
				</div>

                <!-- 特卖类型为原生自定义页面时 -->
                <div id="twoAppCustomPageDiv" style="display:none">
                    &nbsp;原生自定义页面：<input type="text" id="twoAppCustomPage" name="twoAppCustomPage" style="width:300px;"/>
                    <br/><br/>
                </div>
			</div>
            <!--  ################## 第二张 end ################## -->

            <!--  ################## 第三张 begin ################## -->
            <div id="three_div" style="width: 100%">
                <hr/><br/><br/>
                &nbsp;&nbsp;第三张备注：<input maxlength="100" id="threeRemark" name="threeRemark" style="width:300px;"  value="<#if customLayout.threeRemark?exists>${customLayout.threeRemark}</#if>" /><br/><br/>
                第三张图片上传：<input maxlength="100" id="threeImage" name="threeImage" style="width:300px;"  value="<#if customLayout.threeImage?exists>${customLayout.threeImage}</#if>" />
                <a onclick="picDialogOpen('threeImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">必填</font>&nbsp;<span id="threeImageTip" style="color:red">宽度：175px</span><br/>
	    		<span id="threeImageShow">
				<#if customLayout.threeImage?exists>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="${customLayout.threeImage}" style="min-width: 100px;">
				</#if>
                </span><br/><br/>
                &nbsp;&nbsp;关联类型：
                <input type="radio" name="threeType"  id="threeType1" value="1" <#if customLayout.threeType?exists && (customLayout.threeType == 1 || customLayout.threeType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
                <input type="radio" name="threeType"  id="threeType2" value="2" <#if customLayout.threeType?exists && (customLayout.threeType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
                <input type="radio" name="threeType"  id="threeType3" value="3" <#if customLayout.threeType?exists && (customLayout.threeType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
                <input type="radio" name="threeType"  id="threeType5" value="5" <#if customLayout.threeType?exists && (customLayout.threeType == 5)>checked="checked"</#if>> 签到&nbsp;&nbsp;
                <input type="radio" name="threeType"  id="threeType6" value="6" <#if customLayout.threeType?exists && (customLayout.threeType == 6)>checked="checked"</#if>> 邀请小伙伴&nbsp;&nbsp;
                <input type="radio" name="threeType"  id="threeType7" value="7" <#if customLayout.threeType?exists && (customLayout.threeType == 7)>checked="checked"</#if>> 原生自定义页面&nbsp;&nbsp;
                <input type="radio" name="threeType"  id="threeType8" value="8" <#if customLayout.threeType?exists && (customLayout.threeType == 8)>checked="checked"</#if>> 图片点击不跳转&nbsp;&nbsp;
                <input type="radio" name="threeType"  id="threeType9" value="9" <#if customLayout.threeType?exists && (customLayout.threeType == 9)>checked="checked"</#if>> 积分商城&nbsp;&nbsp;
                <font color="red">必填</font><br/><br/>
                <!-- 特卖类型为单品时 -->
                <div id="threeSingleDiv">
                    &nbsp;&nbsp;&nbsp;商品ID：<input type="text" id="threeProductId" name="threeProductId" style="width:300px;" onblur="stringTrim(this);" value="<#if customLayout.threeDisplayId?exists &&(customLayout.threeDisplayId !=0) >${customLayout.threeDisplayId?c}</#if>"/>
                    (<font color="red">必填</font>)&nbsp;<span id="threeProductName" style="color:red"></span><br/><br/>
                </div>

                <!-- 特卖类型为组合特卖时 -->
                <div id="threeGroupDiv" style="display:none">
                    &nbsp;&nbsp;&nbsp;组合ID：<input id="threeGroupSale" type="text" name="threeGroupSale" style="width:300px" onblur="stringTrim(this);" value="<#if customLayout.threeDisplayId?exists &&(customLayout.threeDisplayId !=0) >${customLayout.threeDisplayId?c}</#if>"/>
                    (<font color="red">必填</font>)&nbsp;<span id="threeGroupName" style="color:red"></span><br/><br/>
                </div>

                <!-- 特卖类型为自定义活动时 -->
                <div id="threeCustomSaleDiv" style="display:none">
                    &nbsp;自定义活动：<input type="text" id="threeCustomSale" name="threeCustomSale" style="width:300px;"/>
                    <br/><br/>
                </div>

                <!-- 特卖类型为原生自定义页面时 -->
                <div id="threeAppCustomPageDiv" style="display:none">
                    &nbsp;原生自定义页面：<input type="text" id="threeAppCustomPage" name="threeAppCustomPage" style="width:300px;"/>
                    <br/><br/>
                </div>
            </div>
            <!--  ################## 第三张 end ################## -->

            <!--  ################## 第四张 begin ################## -->
            <div id="four_div" style="width: 100%">
                <hr/><br/><br/>
                &nbsp;&nbsp;第四张备注：<input maxlength="100" id="fourRemark" name="fourRemark" style="width:300px;"  value="<#if customLayout.fourRemark?exists>${customLayout.fourRemark}</#if>" /><br/><br/>
                第四张图片上传：<input maxlength="100" id="fourImage" name="fourImage" style="width:300px;"  value="<#if customLayout.fourImage?exists>${customLayout.fourImage}</#if>" />
                <a onclick="picDialogOpen('fourImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">必填</font>&nbsp;<span id="fourImageTip" style="color:red">宽度：175px</span><br/>
	    		<span id="fourImageShow">
				<#if customLayout.fourImage?exists>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt="" src="${customLayout.fourImage}" style="min-width: 100px;">
				</#if>
                </span><br/><br/>
                &nbsp;&nbsp;关联类型：
                <input type="radio" name="fourType"  id="fourType1" value="1" <#if customLayout.fourType?exists && (customLayout.fourType == 1 || customLayout.fourType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
                <input type="radio" name="fourType"  id="fourType2" value="2" <#if customLayout.fourType?exists && (customLayout.fourType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
                <input type="radio" name="fourType"  id="fourType3" value="3" <#if customLayout.fourType?exists && (customLayout.fourType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
                <input type="radio" name="fourType"  id="fourType5" value="5" <#if customLayout.fourType?exists && (customLayout.fourType == 5)>checked="checked"</#if>> 签到&nbsp;&nbsp;
                <input type="radio" name="fourType"  id="fourType6" value="6" <#if customLayout.fourType?exists && (customLayout.fourType == 6)>checked="checked"</#if>> 邀请小伙伴&nbsp;&nbsp;
                <input type="radio" name="fourType"  id="fourType7" value="7" <#if customLayout.fourType?exists && (customLayout.fourType == 7)>checked="checked"</#if>> 原生自定义页面&nbsp;&nbsp;
                <input type="radio" name="fourType"  id="fourType8" value="8" <#if customLayout.fourType?exists && (customLayout.fourType == 8)>checked="checked"</#if>> 图片点击不跳转&nbsp;&nbsp;
                <input type="radio" name="fourType"  id="fourType9" value="9" <#if customLayout.fourType?exists && (customLayout.fourType == 9)>checked="checked"</#if>> 积分商城&nbsp;&nbsp;
                <font color="red">必填</font><br/><br/>
                <!-- 特卖类型为单品时 -->
                <div id="fourSingleDiv">
                    &nbsp;&nbsp;&nbsp;商品ID：<input type="text" id="fourProductId" name="fourProductId" style="width:300px;" onblur="stringTrim(this);" value="<#if customLayout.fourDisplayId?exists &&(customLayout.fourDisplayId !=0) >${customLayout.fourDisplayId?c}</#if>"/>
                    (<font color="red">必填</font>)&nbsp;<span id="fourProductName" style="color:red"></span><br/><br/>
                </div>

                <!-- 特卖类型为组合特卖时 -->
                <div id="fourGroupDiv" style="display:none">
                    &nbsp;&nbsp;&nbsp;组合ID：<input id="fourGroupSale" type="text" name="fourGroupSale" style="width:300px" onblur="stringTrim(this);" value="<#if customLayout.fourDisplayId?exists &&(customLayout.fourDisplayId !=0) >${customLayout.fourDisplayId?c}</#if>"/>
                    (<font color="red">必填</font>)&nbsp;<span id="fourGroupName" style="color:red"></span><br/><br/>
                </div>

                <!-- 特卖类型为自定义活动时 -->
                <div id="fourCustomSaleDiv" style="display:none">
                    &nbsp;自定义活动：<input type="text" id="fourCustomSale" name="fourCustomSale" style="width:300px;"/>
                    <br/><br/>
                </div>

                <!-- 特卖类型为原生自定义页面时 -->
                <div id="fourAppCustomPageDiv" style="display:none">
                    &nbsp;原生自定义页面：<input type="text" id="fourAppCustomPage" name="fourAppCustomPage" style="width:300px;"/>
                    <br/><br/>
                </div>
            </div>
            <!--  ################## 第四张 end ################## -->

			<hr/><br/><br/>
			 &nbsp;&nbsp;是否展现：<input type="radio"  name="isDisplay" value="1" <#if customLayout.isDisplay?exists &&(customLayout.isDisplay ==1) >checked="checked"</#if>>展现&nbsp;&nbsp;&nbsp;
			<input type="radio"  name="isDisplay" value="0" <#if customLayout.isDisplay?exists &&(customLayout.isDisplay ==0) >checked="checked"</#if>>不展现<br/><br/>

			
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
    var selectType;
    function picDialogOpen($inputId) {
        inputId = $inputId;
        selectType = $("input[name='displayType']:checked").val();
        if(inputId == 'oneImage'){
            if(selectType == 1){
            	$("#needWidth").val("730");
        	}else if(selectType == 2){
        		$("#needWidth").val("360");
        	}else if(selectType == 3){
        		$("#needWidth").val("175");
        	}
        }else if(inputId == 'twoImage'){
        	if(selectType == 2){
        		$("#needWidth").val("360");
        	}else if(selectType == 3){
        		$("#needWidth").val("175");
        	}else{
        		$("#needWidth").val("730");
        	}
        }else if(inputId == 'threeImage'){
        	$("#needWidth").val("175");
        }else if(inputId == 'fourImage'){
        	$("#needWidth").val("175");
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
                var res = eval("("+data+")");
                console.log("selectType:"+selectType);
                if(res.status == 1){
                    $.messager.alert('响应信息',"上传成功...",'info',function(){
                        $("#picDia").dialog("close");
                        if(inputId) {
                            $("#"+inputId).val(res.url);
                            if(inputId == 'oneImage'){
                            	$("#oneImageShow").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
                            }else if(inputId == 'twoImage'){
                            	$("#twoImageShow").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
                            }else if(inputId == 'threeImage'){
                                $("#threeImageShow").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
                            }else if(inputId == 'fourImage'){
                                $("#fourImageShow").html("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
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
				$('#three_div').hide();
				$('#four_div').hide();
				$('#oneImageTip').text('宽度：730px');
			}
		});
		
		$("#displayType2").change(function(){
			if($(this).is(':checked')){
				$('#two_div').show();
                $('#three_div').hide();
                $('#four_div').hide();
                $('#oneImageTip').text('宽度：360px');
                $('#twoImageTip').text('宽度：360px');
			}
		});

        $("#displayType3").change(function(){
            if($(this).is(':checked')){
                $('#two_div').show();
                $('#three_div').show();
                $('#four_div').show();
                $('#oneImageTip').text('宽度：175px');
                $('#twoImageTip').text('宽度：175px');
                $('#threeImageTip').text('宽度：175px');
                $('#fourImageTip').text('宽度：175px');
            }
        });

		$('#oneType1').change(function(){
			if($(this).is(':checked')){
				$('#oneSingleDiv').show();
				$('#oneGroupDiv').hide();
				$('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
			}
		});
		
		$('#oneType2').change(function(){
			if($(this).is(':checked')){
				$('#oneSingleDiv').hide();
				$('#oneGroupDiv').show();
				$('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
			}
		});
		
		$('#oneType3').change(function(){
			if($(this).is(':checked')){
				$('#oneSingleDiv').hide();
				$('#oneGroupDiv').hide();
				$('#oneCustomSaleDiv').show();
                $('#oneAppCustomPageDiv').hide();
			}
		});
		
		$('#oneType5').change(function(){
			if($(this).is(':checked')){
				$('#oneSingleDiv').hide();
				$('#oneGroupDiv').hide();
				$('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
			}
		});
		
		
		$('#oneType6').change(function(){
			if($(this).is(':checked')){
				$('#oneSingleDiv').hide();
				$('#oneGroupDiv').hide();
				$('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
			}
		});

        $('#oneType7').change(function(){
            if($(this).is(':checked')){
                $('#oneSingleDiv').hide();
                $('#oneGroupDiv').hide();
                $('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').show();
            }
        });
		
        $('#oneType8').change(function(){
			if($(this).is(':checked')){
				$('#oneSingleDiv').hide();
				$('#oneGroupDiv').hide();
				$('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
			}
		});

        $('#oneType9').change(function(){
            if($(this).is(':checked')){
                $('#oneSingleDiv').hide();
                $('#oneGroupDiv').hide();
                $('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
            }
        });
        
		$('#twoType1').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').show();
				$('#twoGroupDiv').hide();
				$('#twoCustomSaleDiv').hide();
                $('#twoAppCustomPageDiv').hide();
			}
		});
		
		$('#twoType2').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').hide();
				$('#twoGroupDiv').show();
				$('#twoCustomSaleDiv').hide();
                $('#twoAppCustomPageDiv').hide();
			}
		});
		
		$('#twoType3').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').hide();
				$('#twoGroupDiv').hide();
				$('#twoCustomSaleDiv').show();
                $('#twoAppCustomPageDiv').hide();
			}
		});
		
		$('#twoType5').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').hide();
				$('#twoGroupDiv').hide();
				$('#twoCustomSaleDiv').hide();
                $('#twoAppCustomPageDiv').hide();
			}
		});
		
		$('#twoType6').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').hide();
				$('#twoGroupDiv').hide();
				$('#twoCustomSaleDiv').hide();
                $('#twoAppCustomPageDiv').hide();
			}
		});

        $('#twoType7').change(function(){
            if($(this).is(':checked')){
                $('#twoSingleDiv').hide();
                $('#twoGroupDiv').hide();
                $('#twoCustomSaleDiv').hide();
                $('#twoAppCustomPageDiv').show();
            }
        });
        
		$('#twoType8').change(function(){
			if($(this).is(':checked')){
				$('#twoSingleDiv').hide();
				$('#twoGroupDiv').hide();
				$('#twoCustomSaleDiv').hide();
                $('#twoAppCustomPageDiv').hide();
			}
		});

        $('#twoType9').change(function(){
            if($(this).is(':checked')){
                $('#oneSingleDiv').hide();
                $('#oneGroupDiv').hide();
                $('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
            }
        });

        $('#threeType1').change(function(){
            if($(this).is(':checked')){
                $('#threeSingleDiv').show();
                $('#threeGroupDiv').hide();
                $('#threeCustomSaleDiv').hide();
                $('#threeAppCustomPageDiv').hide();
            }
        });

        $('#threeType2').change(function(){
            if($(this).is(':checked')){
                $('#threeSingleDiv').hide();
                $('#threeGroupDiv').show();
                $('#threeCustomSaleDiv').hide();
                $('#threeAppCustomPageDiv').hide();
            }
        });

        $('#threeType3').change(function(){
            if($(this).is(':checked')){
                $('#threeSingleDiv').hide();
                $('#threeGroupDiv').hide();
                $('#threeCustomSaleDiv').show();
                $('#threeAppCustomPageDiv').hide();
            }
        });

        $('#threeType5').change(function(){
            if($(this).is(':checked')){
                $('#threeSingleDiv').hide();
                $('#threeGroupDiv').hide();
                $('#threeCustomSaleDiv').hide();
                $('#threeAppCustomPageDiv').hide();
            }
        });

        $('#threeType6').change(function(){
            if($(this).is(':checked')){
                $('#threeSingleDiv').hide();
                $('#threeGroupDiv').hide();
                $('#threeCustomSaleDiv').hide();
                $('#threeAppCustomPageDiv').hide();
            }
        });

        $('#threeType7').change(function(){
            if($(this).is(':checked')){
                $('#threeSingleDiv').hide();
                $('#threeGroupDiv').hide();
                $('#threeCustomSaleDiv').hide();
                $('#threeAppCustomPageDiv').show();
            }
        });
        
        $('#threeType8').change(function(){
            if($(this).is(':checked')){
                $('#threeSingleDiv').hide();
                $('#threeGroupDiv').hide();
                $('#threeCustomSaleDiv').hide();
                $('#threeAppCustomPageDiv').hide();
            }
        });

        $('#threeType9').change(function(){
            if($(this).is(':checked')){
                $('#oneSingleDiv').hide();
                $('#oneGroupDiv').hide();
                $('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
            }
        });

        $('#fourType1').change(function(){
            if($(this).is(':checked')){
                $('#fourSingleDiv').show();
                $('#fourGroupDiv').hide();
                $('#fourCustomSaleDiv').hide();
                $('#fourAppCustomPageDiv').hide();
            }
        });

        $('#fourType2').change(function(){
            if($(this).is(':checked')){
                $('#fourSingleDiv').hide();
                $('#fourGroupDiv').show();
                $('#fourCustomSaleDiv').hide();
                $('#fourAppCustomPageDiv').hide();
            }
        });

        $('#fourType3').change(function(){
            if($(this).is(':checked')){
                $('#fourSingleDiv').hide();
                $('#fourGroupDiv').hide();
                $('#fourCustomSaleDiv').show();
                $('#fourAppCustomPageDiv').hide();
            }
        });

        $('#fourType5').change(function(){
            if($(this).is(':checked')){
                $('#fourSingleDiv').hide();
                $('#fourGroupDiv').hide();
                $('#fourCustomSaleDiv').hide();
                $('#fourAppCustomPageDiv').hide();
            }
        });

        $('#fourType6').change(function(){
            if($(this).is(':checked')){
                $('#fourSingleDiv').hide();
                $('#fourGroupDiv').hide();
                $('#fourCustomSaleDiv').hide();
                $('#fourAppCustomPageDiv').hide();
            }
        });

        $('#fourType7').change(function(){
            if($(this).is(':checked')){
                $('#fourSingleDiv').hide();
                $('#fourGroupDiv').hide();
                $('#fourCustomSaleDiv').hide();
                $('#fourAppCustomPageDiv').show();
            }
        });
		
        $('#fourType8').change(function(){
            if($(this).is(':checked')){
                $('#fourSingleDiv').hide();
                $('#fourGroupDiv').hide();
                $('#fourCustomSaleDiv').hide();
                $('#fourAppCustomPageDiv').hide();
            }
        });

        $('#fourType9').change(function(){
            if($(this).is(':checked')){
                $('#oneSingleDiv').hide();
                $('#oneGroupDiv').hide();
                $('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
            }
        });
		
		$("#displayType1").change();
		$("#displayType2").change();
		$('#oneType1').change();
		$('#oneType2').change();
		$('#oneType3').change();
		$('#oneType5').change();
		$('#oneType6').change();
        $('#oneType7').change();
        $('#oneType8').change();
        $('#oneType9').change();
		$('#twoType1').change();
		$('#twoType2').change();
		$('#twoType3').change();
		$('#twoType5').change();
		$('#twoType6').change();
        $('#twoType7').change();
        $('#twoType8').change();
        $('#twoType9').change();
        $('#threeType1').change();
        $('#threeType2').change();
        $('#threeType3').change();
        $('#threeType5').change();
        $('#threeType6').change();
        $('#threeType7').change();
        $('#threeType8').change();
        $('#threeType9').change();
        $('#fourType1').change();
        $('#fourType2').change();
        $('#fourType3').change();
        $('#fourType5').change();
        $('#fourType6').change();
        $('#fourType7').change();
        $('#fourType8').change();
        $('#fourType9').change();

        $('#saveCustomLayout').form({
		    url:'${rc.contextPath}/customRegion/saveOrUpdateCustomLayout',   
		    onSubmit: function(){
					var displayType = $("input[name='displayType']:checked").val();
					var oneType = $("input[name='oneType']:checked").val();
					var oneProductId = $("#oneProductId").val();
					var oneGroupSale = $("#oneGroupSale").val();
					var oneCustomSale = $("#oneCustomSale").combobox('getValue');
					var oneAppCustomPage = $("#oneAppCustomPage").combobox('getValue');
					var oneImage = $("#oneImage").val();
					var twoType = $("input[name='twoType']:checked").val();
					var twoProductId = $("#twoProductId").val();
					var twoGroupSale = $("#twoGroupSale").val();
					var twoCustomSale = $("#twoCustomSale").combobox('getValue');
                    var twoAppCustomPage = $("#twoAppCustomPage").combobox('getValue');
					var twoImage = $("#twoImage").val();
					var threeType = $("input[name='threeType']:checked").val();
					var threeProductId = $("#threeProductId").val();
					var threeGroupSale = $("#threeGroupSale").val();
					var threeCustomSale = $("#threeCustomSale").combobox('getValue');
                    var threeAppCustomPage = $("#threeAppCustomPage").combobox('getValue');
					var threeImage = $("#threeImage").val();
                    var fourType = $("input[name='fourType']:checked").val();
                    var fourProductId = $("#fourProductId").val();
                    var fourGroupSale = $("#fourGroupSale").val();
                    var fourCustomSale = $("#fourCustomSale").combobox('getValue');
                    var fourAppCustomPage = $("#fourAppCustomPage").combobox('getValue');
                    var fourImage = $("#fourImage").val();

					if(displayType == '' || displayType == null || displayType==undefined){
						$.messager.alert('提示','请选择布局方式','warning');
						return false;
					}else if(oneImage == ''){
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
					}else if(oneType == 3 && (oneCustomSale == '' || oneCustomSale == null || oneCustomSale == undefined )){
						$.messager.alert('提示','请选择自定义活动','warning');
						return false;
					}

					if(displayType == 2 || displayType == 3){
						if(twoImage == ''){
							$.messager.alert('提示','请上传图片','warning');
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
						}else if(twoType == 3 && (twoCustomSale == '' || twoCustomSale == null || twoCustomSale == undefined )){
							$.messager.alert('提示','请选择自定义活动','warning');
							return false;
						}
					}

					if(displayType == 3){
						if(threeImage == ''){
							$.messager.alert('提示','请上传图片','warning');
							return false;
						}else if(threeType == '' || threeType == null || threeType == undefined){
							$.messager.alert('提示','请选择右侧关联类型','warning');
							return false;
						}else if(threeType == 1 && threeProductId == ''){
							$.messager.alert('提示','请填写关联商品Id','warning');
							return false;
						}else if(threeType == 2 && (threeGroupSale == '' || threeGroupSale == null || threeGroupSale == undefined)){
							$.messager.alert('提示','请选择关联组合','warning');
							return false;
						}else if(threeType == 3 && (threeCustomSale == '' || threeCustomSale == null || threeCustomSale == undefined )){
							$.messager.alert('提示','请选择自定义活动','warning');
							return false;
						}
                        if(fourImage == ''){
                            $.messager.alert('提示','请上传图片','warning');
                            return false;
                        }else if(fourType == '' || fourType == null || fourType == undefined){
                            $.messager.alert('提示','请选择右侧关联类型','warning');
                            return false;
                        }else if(fourType == 1 && fourProductId == ''){
                            $.messager.alert('提示','请填写关联商品Id','warning');
                            return false;
                        }else if(fourType == 2 && (fourGroupSale == '' || fourGroupSale == null || fourGroupSale == undefined)){
                            $.messager.alert('提示','请选择关联组合','warning');
                            return false;
                        }else if(fourType == 3 && (fourCustomSale == '' || fourCustomSale == null || fourCustomSale == undefined )){
                            $.messager.alert('提示','请选择自定义活动','warning');
                            return false;
                        }else if(fourType == 7 && (fourAppCustomPage == '' || fourAppCustomPage == null || fourAppCustomPage == undefined )){
                            $.messager.alert('提示','请选择原生自定义页面','warning');
                            return false;
                        }
					}
		          $.messager.progress();
		    },   
		    success:function(data){   
		    	$.messager.progress('close');
                var res = eval("("+data+")");
                if(res.status == 1){
                    $.messager.alert('响应信息',"保存成功",'info',function(){
                        window.location.href = '${rc.contextPath}/customRegion/manageLayout/${regionId}';
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
		                    	}else if(elementId == 'threeProductId'){
                                    $('#threeProductName').text(data.msg);
                                }else if(elementId == 'fourProductId'){
                                    $('#fourProductName').text(data.msg);
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
		                    	}else if(elementId == 'threeGroupSale'){
                                    $('#threeGroupName').text(data.name + "-" + data.remark);
                                }else if(elementId == 'fourGroupSale'){
                                    $('#fourGroupName').text(data.name + "-" + data.remark);
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

        // 异步获取自定义活动信息  -- begin
		$("#oneCustomSale").combobox({
			url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id=<#if oneDisplayId??>${oneDisplayId?c}<#else>${0}</#if>',   
		    valueField:'code',   
		    textField:'text' 
		});
		
		$("#twoCustomSale").combobox({
			url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id=<#if twoDisplayId??>${twoDisplayId?c}<#else>${0}</#if>',   
		    valueField:'code',   
		    textField:'text' 
		});

        $("#threeCustomSale").combobox({
            url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id=<#if threeDisplayId??>${threeDisplayId?c}<#else>${0}</#if>',
            valueField:'code',
            textField:'text'
        });

        $("#fourCustomSale").combobox({
            url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id=<#if fourDisplayId??>${fourDisplayId?c}<#else>${0}</#if>',
            valueField:'code',
            textField:'text'
        });
        // 异步获取自定义活动信息  -- end

        // 异步获取原生自定义页面信息  -- begin
        $("#oneAppCustomPage").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage?id=<#if oneDisplayId??>${oneDisplayId?c}<#else>${0}</#if>',
            valueField:'code',
            textField:'text'
        });

        $("#twoAppCustomPage").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage?id=<#if twoDisplayId??>${twoDisplayId?c}<#else>${0}</#if>',
            valueField:'code',
            textField:'text'
        });

        $("#threeAppCustomPage").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage?id=<#if threeDisplayId??>${threeDisplayId?c}<#else>${0}</#if>',
            valueField:'code',
            textField:'text'
        });

        $("#fourAppCustomPage").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage?id=<#if fourDisplayId??>${fourDisplayId?c}<#else>${0}</#if>',
            valueField:'code',
            textField:'text'
        });
        // 异步获取原生自定义页面信息  -- end
	});
</script>

</body>
</html>