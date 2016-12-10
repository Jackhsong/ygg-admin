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

<div data-options="region:'center',title:'个人中心页面管理'" style="padding:5px;">
	<form id="saveCustomCenter"  method="post">
		<fieldset>
			<input type="hidden" id="id" name="id" value="<#if center.id?exists>${center.id?c}</#if>" />
			模块备注：<input type="text" id="remark" name="remark" class="input" maxlength="50" value="<#if center.remark?exists>${center.remark}</#if>"/>&nbsp;<font color="red">必填</font><br/><br/>
			布局方式：<input type="radio" name="displayType" id="displayType1" value="1" <#if center.displayType?exists && (center.displayType == 1)>checked="checked"</#if>/>一行1张&nbsp;&nbsp;
			<input type="radio" name="displayType" id="displayType2" value="2" <#if center.displayType?exists && (center.displayType == 2)>checked="checked"</#if>/>一行2张&nbsp;&nbsp;
			<input type="radio" name="displayType" id="displayType3" value="3" <#if center.displayType?exists && (center.displayType == 3)>checked="checked"</#if>/>一行3张&nbsp;&nbsp;
			<input type="radio" name="displayType" id="displayType4" value="4" <#if center.displayType?exists && (center.displayType == 4)>checked="checked"</#if>/>一行4张&nbsp;&nbsp;
			<font color="red">必填</font><br/><br/>
			
			<hr/>
			
			<!-- -------------------------------------------第一张begin----------------------------------- -->
			<div id="oneDiv">
			<div id="oneTitleDiv">
			第一张标题：<input type="text" id="oneTitle" name="oneTitle" class="input" maxlength="10" value="<#if center.oneTitle?exists>${center.oneTitle}</#if>"/>&nbsp;<span id="oneTitleTip" style="color:red">选填</span><br/><br/>
			&nbsp;标题颜色：<input type="text" id="oneTitleColor" name="oneTitleColor" class="input" maxlength="10" value="<#if center.oneTitleColor?exists>${center.oneTitleColor}</#if>"/><br/><br/>
			</div>
			第一张图片：<input type="text" id="oneImage" name="oneImage" class="input" maxlength="100" value="<#if center.oneImage?exists>${center.oneImage}</#if>"/>
			<a onclick="picDialogOpen('oneImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">必填</font>&nbsp;<span id="oneImageTip" style="color:red">750x188</span><br/><br/>
			&nbsp;关联类型：<input type="radio" name="oneType"  id="oneType1" value="1" <#if center.oneType?exists && (center.oneType == 1 || center.oneType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType2" value="2" <#if center.oneType?exists && (center.oneType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType3" value="3" <#if center.oneType?exists && (center.oneType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType5" value="5" <#if center.oneType?exists && (center.oneType == 5)>checked="checked"</#if>> 原生自定义页面&nbsp;&nbsp;
    			<input type="radio" name="oneType"  id="oneType6" value="6" <#if center.oneType?exists && (center.oneType == 6)>checked="checked"</#if>> 积分商城&nbsp;&nbsp;
    			<font color="red">必填</font><br/><br/>
			
			<!-- 特卖类型为单品时 -->
			<div id="oneSingleDiv">
			&nbsp;&nbsp;商品ID：<input type="number" id="oneProductId" name="oneProductId" class="input"   value="<#if center.oneDisplayId?exists &&(center.oneDisplayId !=0) >${center.oneDisplayId?c}</#if>" />
			<font color="red">必填</font>&nbsp;<span id="oneProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为组合特卖时 -->
			<div id="oneGroupDiv" style="display:none">
			&nbsp;&nbsp;组合ID：<input id="oneGroupSale" type="number" name="oneGroupSale" class="input"  value="<#if center.oneDisplayId?exists &&(center.oneDisplayId !=0) >${center.oneDisplayId?c}</#if>"/>
			 <font color="red">必填</font>&nbsp;<span id="oneGroupName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为自定义活动时 -->
			<div id="oneCustomSaleDiv" style="display:none">
			自定义活动：<input type="text" id="oneCustomSale" name="oneCustomSale" class="input"/><font color="red">必填</font>
			 <br/><br/>
			</div>

            <!-- 特卖类型为原生自定义页面时 -->
            <div id="oneAppCustomPageDiv" style="display:none">
           	原生自定义页面：<input type="text" id="oneAppCustomPage" name="oneAppCustomPage" class="input"/><font color="red">必填</font>
                <br/><br/>
            </div>
            </div>
            <!-- -----------------------------------------第一张end-------------------------------------------- -->
            
            
            
			<!-- -----------------------------------------第二张begin---------------------------------------------- -->
			<div id="twoDiv">
			<hr/>
			<div id="twoTitleDiv">
			第二张标题：<input type="text" id="twoTitle" name="twoTitle" class="input" maxlength="10" value="<#if center.twoTitle?exists>${center.twoTitle}</#if>"/>&nbsp;<span id="twoTitleTip" style="color:red">选填</span><br/><br/>
			&nbsp;标题颜色：<input type="text" id="twoTitleColor" name="twoTitleColor" class="input" maxlength="10" value="<#if center.twoTitleColor?exists>${center.twoTitleColor}</#if>"/><br/><br/>
			</div>
			第二张图片：<input type="text" id="twoImage" name="twoImage" class="input" maxlength="100" value="<#if center.twoImage?exists>${center.twoImage}</#if>"/>
			<a onclick="picDialogOpen('twoImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">必填</font>&nbsp;<span id="twoImageTip" style="color:red">宽度：710px</span><br/><br/>
			&nbsp;关联类型：<input type="radio" name="twoType"  id="twoType1" value="1" <#if center.twoType?exists && (center.twoType == 1 || center.twoType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
    			<input type="radio" name="twoType"  id="twoType2" value="2" <#if center.twoType?exists && (center.twoType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
    			<input type="radio" name="twoType"  id="twoType3" value="3" <#if center.twoType?exists && (center.twoType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
    			<input type="radio" name="twoType"  id="twoType5" value="5" <#if center.twoType?exists && (center.twoType == 5)>checked="checked"</#if>> 原生自定义页面&nbsp;&nbsp;
                <input type="radio" name="twoType"  id="twoType6" value="6" <#if center.twoType?exists && (center.twoType == 6)>checked="checked"</#if>> 积分商城&nbsp;&nbsp;
    			<font color="red">必填</font><br/><br/>
			
			<!-- 特卖类型为单品时 -->
			<div id="twoSingleDiv">
			&nbsp;&nbsp;商品ID：<input type="number" id="twoProductId" name="twoProductId" class="input"   value="<#if center.twoDisplayId?exists &&(center.twoDisplayId !=0) >${center.twoDisplayId?c}</#if>" />
			<font color="red">必填</font>&nbsp;<span id="twoProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为组合特卖时 -->
			<div id="twoGroupDiv" style="display:none">
			&nbsp;&nbsp;组合ID：<input id="twoGroupSale" type="number" name="twoGroupSale" class="input"  value="<#if center.twoDisplayId?exists &&(center.twoDisplayId !=0) >${center.twoDisplayId?c}</#if>"/>
			<font color="red">必填</font>&nbsp;<span id="twoGroupName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为自定义活动时 -->
			<div id="twoCustomSaleDiv" style="display:none">
			自定义活动：<input type="text" id="twoCustomSale" name="twoCustomSale" class="input"/><font color="red">必填</font>
			 <br/><br/>
			</div>

            <!-- 特卖类型为原生自定义页面时 -->
            <div id="twoAppCustomPageDiv" style="display:none">
           	原生自定义页面：<input type="text" id="twoAppCustomPage" name="twoAppCustomPage" class="input"/><font color="red">必填</font>
                <br/><br/>
            </div>
            </div>
            <!-- ------------------------------------------第二张end--------------------------------------------- -->
            
            
			<!-- -------------------------------------------第三张begin------------------------------------------ -->
			<div id="threeDiv">
			<hr/>
			<div id="threeTitleDiv">
			第三张标题：<input type="text" id="threeTitle" name="threeTitle" class="input" maxlength="10" value="<#if center.threeTitle?exists>${center.threeTitle}</#if>"/>&nbsp;<span id="threeTitleTip" style="color:red">选填</span><br/><br/>
			&nbsp;标题颜色：<input type="text" id="threeTitleColor" name="threeTitleColor" class="input" maxlength="10" value="<#if center.threeTitleColor?exists>${center.threeTitleColor}</#if>"/><br/><br/>
			</div>
			第三张图片：<input type="text" id="threeImage" name="threeImage" class="input" maxlength="100" value="<#if center.threeImage?exists>${center.threeImage}</#if>"/>
			<a onclick="picDialogOpen('threeImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">必填</font>&nbsp;<span id="threeImageTip" style="color:red">宽度：710px</span><br/><br/>
			&nbsp;关联类型：<input type="radio" name="threeType"  id="threeType1" value="1" <#if center.threeType?exists && (center.threeType == 1 || center.threeType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
    			<input type="radio" name="threeType"  id="threeType2" value="2" <#if center.threeType?exists && (center.threeType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
    			<input type="radio" name="threeType"  id="threeType3" value="3" <#if center.threeType?exists && (center.threeType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
    			<input type="radio" name="threeType"  id="threeType5" value="5" <#if center.threeType?exists && (center.threeType == 5)>checked="checked"</#if>> 原生自定义页面&nbsp;&nbsp;
                <input type="radio" name="threeType"  id="threeType6" value="6" <#if center.threeType?exists && (center.threeType == 6)>checked="checked"</#if>> 积分商城&nbsp;&nbsp;
    			<font color="red">必填</font><br/><br/>
			
			<!-- 特卖类型为单品时 -->
			<div id="threeSingleDiv">
			&nbsp;&nbsp;商品ID：<input type="number" id="threeProductId" name="threeProductId" class="input"   value="<#if center.threeDisplayId?exists &&(center.threeDisplayId !=0) >${center.threeDisplayId?c}</#if>" />
			<font color="red">必填</font>&nbsp;<span id="threeProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为组合特卖时 -->
			<div id="threeGroupDiv" style="display:none">
			&nbsp;&nbsp;组合ID：<input id="threeGroupSale" type="number" name="threeGroupSale" class="input"  value="<#if center.threeDisplayId?exists &&(center.threeDisplayId !=0) >${center.threeDisplayId?c}</#if>"/>
			<font color="red">必填</font>&nbsp;<span id="threeGroupName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为自定义活动时 -->
			<div id="threeCustomSaleDiv" style="display:none">
			自定义活动：<input type="text" id="threeCustomSale" name="threeCustomSale" class="input"/><font color="red">必填</font>
			 <br/><br/>
			</div>

            <!-- 特卖类型为原生自定义页面时 -->
            <div id="threeAppCustomPageDiv" style="display:none">
           	原生自定义页面：<input type="text" id="threeAppCustomPage" name="threeAppCustomPage" class="input"/><font color="red">必填</font>
                <br/><br/>
            </div>
            </div>
            <!-- ------------------------------------------------第三张end--------------------------------------- -->
            
            
            
			<!-- ------------------------------------------------第四张begin---------------------------------------- -->
			<div id="fourDiv">
			<hr/>
			<div id="fourTitleDiv">
			第四张标题：<input type="text" id="fourTitle" name="fourTitle" class="input" maxlength="10" value="<#if center.fourTitle?exists>${center.fourTitle}</#if>"/>&nbsp;<span id="fourTitleTip" style="color:red">选填</span><br/><br/>
			&nbsp;标题颜色：<input type="text" id="fourTitleColor" name="fourTitleColor" class="input" maxlength="10" value="<#if center.fourTitleColor?exists>${center.fourTitleColor}</#if>"/><br/><br/>
			</div>
			第四张图片：<input type="text" id="fourImage" name="fourImage" class="input" maxlength="100" value="<#if center.fourImage?exists>${center.fourImage}</#if>"/>
			<a onclick="picDialogOpen('fourImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">必填</font>&nbsp;<span id="fourImageTip" style="color:red">宽度：710px</span><br/><br/>
			&nbsp;关联类型：<input type="radio" name="fourType"  id="fourType1" value="1" <#if center.fourType?exists && (center.fourType == 1 || center.fourType == 4)>checked="checked"</#if>> 单品&nbsp;&nbsp;
    			<input type="radio" name="fourType"  id="fourType2" value="2" <#if center.fourType?exists && (center.fourType == 2)>checked="checked"</#if>> 组合&nbsp;&nbsp;
    			<input type="radio" name="fourType"  id="fourType3" value="3" <#if center.fourType?exists && (center.fourType == 3)>checked="checked"</#if>> 自定义活动&nbsp;&nbsp;
    			<input type="radio" name="fourType"  id="fourType5" value="5" <#if center.fourType?exists && (center.fourType == 5)>checked="checked"</#if>> 原生自定义页面&nbsp;&nbsp;
                <input type="radio" name="fourType"  id="fourType6" value="6" <#if center.fourType?exists && (center.fourType == 6)>checked="checked"</#if>> 积分商城&nbsp;&nbsp;
    			<font color="red">必填</font><br/><br/>
			
			<!-- 特卖类型为单品时 -->
			<div id="fourSingleDiv">
			&nbsp;&nbsp;商品ID：<input type="number" id="fourProductId" name="fourProductId" class="input"   value="<#if center.fourDisplayId?exists &&(center.fourDisplayId !=0) >${center.fourDisplayId?c}</#if>" />
			<font color="red">必填</font>&nbsp;<span id="fourProductName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为组合特卖时 -->
			<div id="fourGroupDiv" style="display:none">
			&nbsp;&nbsp;组合ID：<input id="fourGroupSale" type="number" name="fourGroupSale" class="input"  value="<#if center.fourDisplayId?exists &&(center.fourDisplayId !=0) >${center.fourDisplayId?c}</#if>"/>
			<font color="red">必填</font>&nbsp;<span id="fourGroupName" style="color:red"></span><br/><br/>
			</div>
			
			<!-- 特卖类型为自定义活动时 -->
			<div id="fourCustomSaleDiv" style="display:none">
			自定义活动：<input type="text" id="fourCustomSale" name="fourCustomSale" class="input"/><font color="red">必填</font>
			 <br/><br/>
			</div>

            <!-- 特卖类型为原生自定义页面时 -->
            <div id="fourAppCustomPageDiv" style="display:none">
           	原生自定义页面：<input type="text" id="fourAppCustomPage" name="fourAppCustomPage" class="input"/><font color="red">必填</font>
                <br/><br/>
            </div>
            </div>
            <!-- -----------------------------------------------第四张end----------------------------------------- -->
			
			<hr/>
			展现状态：<input type="radio" name="isDisplay" id="isDisplay_1" value="1" <#if center.isDisplay?exists && (center.isDisplay ==1)>checked="checked"</#if>>展现&nbsp;&nbsp;&nbsp;
			<input type="radio" name="isAvailable"  id="isDisplay_0" value="0" <#if center.isDisplay?exists && (center.isDisplay ==0)>checked="checked"</#if>>不展现<br/><br/>
			<a id="saveButton" href="javascript:;" class="easyui-linkbutton">&nbsp;&nbsp;保存&nbsp;&nbsp;</a>
		</fieldset>	
	</form>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <input type="hidden" name="needWidth" id="needWidth" value="0">
        <input type="hidden" name="needHeight" id="needHeight" value="0">
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
</div>

<script type="text/javascript">
	
    $(function(){
    	

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
        
		$("#displayType1").change(function(){
			if($(this).is(':checked')){
				$('#oneTitleDiv').hide();
				$('#twoDiv').hide();
				$('#threeDiv').hide();
				$('#fourDiv').hide();
				$('#oneImageTip').text('750x188');
				$('#twoImageTip').text('');
				$('#threeImageTip').text('');
                $('#fourImageTip').text('');
			}
		});
		
		$("#displayType2").change(function(){
			if($(this).is(':checked')){
				$('#oneTitleDiv').hide();
				$('#twoTitleDiv').hide();
				$('#twoDiv').show();
                $('#threeDiv').hide();
                $('#fourDiv').hide();
                $('#oneImageTip').text('374*188');
                $('#twoImageTip').text('374*188');
                $('#threeImageTip').text('');
                $('#fourImageTip').text('');
			}
		});

        $("#displayType3").change(function(){
            if($(this).is(':checked')){
            	$('#oneTitleTip').text('选填');
            	$('#threeTitleTip').text('选填');
            	$('#oneTitleDiv').show();
				$('#twoTitleDiv').hide();
				$('#threeTitleDiv').show();
                $('#twoDiv').show();
                $('#threeDiv').show();
                $('#fourDiv').hide();
                $('#oneImageTip').text('有标题60x60/无标题187x188');
                $('#twoImageTip').text('374*188');
                $('#threeImageTip').text('有标题60x60/无标题187x188');
                $('#fourImageTip').text('');
            }
        });
        
        $("#displayType4").change(function(){
            if($(this).is(':checked')){
            	$('#oneTitleTip').text('必填');
            	$('#twoTitleTip').text('必填');
            	$('#threeTitleTip').text('必填');
            	$('#fourTitleTip').text('必填');
            	$('#oneTitleDiv').show();
				$('#twoTitleDiv').show();
				$('#threeTitleDiv').show();
				$('#fourTitleDiv').show();
                $('#twoDiv').show();
                $('#threeDiv').show();
                $('#fourDiv').show();
                $('#oneImageTip').text('60*60');
                $('#twoImageTip').text('60*60');
                $('#threeImageTip').text('60*60');
                $('#fourImageTip').text('60*60');
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
                $('#oneAppCustomPageDiv').show();
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
                $('#twoAppCustomPageDiv').show();
			}
		});

        $('#twoType6').change(function(){
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
                $('#threeAppCustomPageDiv').show();
            }
        });

        $('#threeType6').change(function(){
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
                $('#fourAppCustomPageDiv').show();
            }
        });

        $('#fourType6').change(function(){
            if($(this).is(':checked')){
                $('#oneSingleDiv').hide();
                $('#oneGroupDiv').hide();
                $('#oneCustomSaleDiv').hide();
                $('#oneAppCustomPageDiv').hide();
            }
        });

		$("#displayType1").change();
		$("#displayType2").change();
		$("#displayType3").change();
		$("#displayType4").change();
		$('#oneType1').change();
		$('#oneType2').change();
		$('#oneType3').change();
		$('#oneType5').change();
		$('#oneType6').change();
		$('#twoType1').change();
		$('#twoType2').change();
		$('#twoType3').change();
		$('#twoType5').change();
		$('#twoType6').change();
        $('#threeType1').change();
        $('#threeType2').change();
        $('#threeType3').change();
        $('#threeType5').change();
        $('#threeType6').change();
        $('#fourType1').change();
        $('#fourType2').change();
        $('#fourType3').change();
        $('#fourType5').change();
        $('#fourType6').change();

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
        
        $("#saveButton").click(function(){
        	$('#saveCustomCenter').form('submit', {   
        	    url:'${rc.contextPath}/customCenter/saveOrUpdate',   
        	    onSubmit: function(){
        	    	  var remark = $("#remark").val();
        	    	  var displayType = $("input[name='displayType']:checked").val();
        	    	  
        	    	  if($.trim(remark) == ''){
        	    		  $.messager.alert('提示','请输入模块备注','warning');
        	        	  return false;
        	    	  }else if(displayType == '' || displayType == null || displayType == undefined){
        	    		  $.messager.alert('提示','请选择布局方式','warning');
        	        	  return false;
        	    	  }else if(displayType == 1){
        	    		  if(validateTypeOne()){
        	    			  return true;
        	    		  }else{
        	    			  return false;
        	    		  }
        	    	  }else if(displayType == 2){
        	    		  if(validateTypeTwo()){
        	    			  return true;
        	    		  }else{
        	    			  return false;
        	    		  }
        	    	  }else if(displayType == 3){
        	    		  if(validateTypeThree()){
        	    			  return true;
        	    		  }else{
        	    			  return false;
        	    		  }
        	    	  }else if(displayType == 4){
        	    		  if(validateTypeFour()){
        	    			  return true;
        	    		  }else{
        	    			  return false;
        	    		  }
        	    	  }
        	          $.messager.progress();
        	    },   
        	    success:function(data){
        	    	$.messager.progress('close');
        	    	var res = eval("("+data+")");
        	        if(res.status == 1){
        	        	$.messager.alert('响应消息','保存成功','info',function(){
        	        		window.location.href='${rc.contextPath}/customCenter/list';
        	        	});
        	        }else{
        	        	$.messager.alert('响应消息1111',res.msg,'error');
        	        }
        	    }   
        	});
        });
    });

    function validateTypeOne(){
  	  var oneTitle = $("#oneTitle").val();
	  var oneTitleColor = $("#oneTitleColor").val();
	  var oneImage = $("#oneImage").val();
	  var oneType = $("input[name='oneType']:checked").val();
	  var oneProductId = $("#oneProductId").val();
	  var oneGroupSale = $("#oneGroupSale").val();
	  var oneCustomSale = $("#oneCustomSale").combobox('getValue');
	  var oneAppCustomPage = $("#oneAppCustomPage").combobox('getValue');
		alert(oneType);
	  if($.trim(oneImage) == ''){
		  $.messager.alert('提示','请上传第一张图片','warning');
    	  return false;
	  }else if(oneType == null || oneType == undefined || oneType == ''){
		  $.messager.alert('提示','请选择第一张关联类型','warning');
    	  return false;
	  }else if(oneType == 1 && $.trim(oneProductId) == ''){
		  $.messager.alert('提示','请输入第一张商品ID','warning');
    	  return false;
	  }else if(oneType == 2 && $.trim(oneGroupSale) == ''){
		  $.messager.alert('提示','请输入第一张组合ID','warning');
    	  return false;
	  }else if(oneType == 3 && (oneCustomSale == '' || oneCustomSale == null || oneCustomSale == undefined)){
		  $.messager.alert('提示','请选择第一张自定义活动','warning');
    	  return false;
	  }else if(oneType == 4 && (oneAppCustomPage == '' || oneAppCustomPage == null || oneAppCustomPage == undefined)){
		  $.messager.alert('提示','请选择第一张原生自定义页面','warning');
    	  return false;
	  }else{
		  return true;
	  }
    }
    
	function validateTypeTwo(){
	  	var twoTitle = $("#twoTitle").val();
		var twoTitleColor = $("#twoTitleColor").val();
		var twoImage = $("#twoImage").val();
		var twoType = $("input[name='twoType']:checked").val();
		var twoProductId = $("#twoProductId").val();
		var twoGroupSale = $("#twoGroupSale").val();
		var twoCustomSale = $("#twoCustomSale").combobox('getValue');
		var twoAppCustomPage = $("#twoAppCustomPage").combobox('getValue');		
    	if(validateTypeOne()){
    		  if($.trim(twoImage) == ''){
    			  $.messager.alert('提示','请上传第二张图片','warning');
    	    	  return false;
    		  }else if(twoType == null || twoType == undefined || twoType == ''){
    			  $.messager.alert('提示','请选择第二张关联类型','warning');
    	    	  return false;
    		  }else if(twoType == 1 && $.trim(twoProductId) == ''){
    			  $.messager.alert('提示','请输入第二张商品ID','warning');
    	    	  return false;
    		  }else if(twoType == 2 && $.trim(twoGroupSale) == ''){
    			  $.messager.alert('提示','请输入第二张组合ID','warning');
    	    	  return false;
    		  }else if(twoType == 3 && (twoCustomSale == '' || twoCustomSale == null || twoCustomSale == undefined)){
    			  $.messager.alert('提示','请选择第二张自定义活动','warning');
    	    	  return false;
    		  }else if(twoType == 4 && (twoAppCustomPage == '' || twoAppCustomPage == null || twoAppCustomPage == undefined)){
    			  $.messager.alert('提示','请选择第二张原生自定义页面','warning');
    	    	  return false;
    		  }else{
    			  return true;
    		  }    		
    	}
    }
	
	function validateTypeThree(){
	   	  var threeTitle = $("#threeTitle").val();
	   	  var threeTitleColor = $("#threeTitleColor").val();
	   	  var threeImage = $("#threeImage").val();
	   	  var threeType = $("input[name='threeType']:checked").val();
	   	  var threeProductId = $("#threeProductId").val();
	   	  var threeGroupSale = $("#threeGroupSale").val();
	   	  var threeCustomSale = $("#threeCustomSale").combobox('getValue');
	   	  var threeAppCustomPage = $("#threeAppCustomPage").combobox('getValue');			
		  if(validateTypeTwo()){
	  		  if($.trim(threeImage) == ''){
				  $.messager.alert('提示','请上传第三张图片','warning');
		    	  return false;
			  }else if(threeType == null || threeType == undefined || threeType == ''){
				  $.messager.alert('提示','请选择第三张关联类型','warning');
		    	  return false;
			  }else if(threeType == 1 && $.trim(threeProductId) == ''){
				  $.messager.alert('提示','请输入第三张商品ID','warning');
		    	  return false;
			  }else if(threeType == 2 && $.trim(threeGroupSale) == ''){
				  $.messager.alert('提示','请输入第三张组合ID','warning');
		    	  return false;
			  }else if(threeType == 3 && (threeCustomSale == '' || threeCustomSale == null || threeCustomSale == undefined)){
				  $.messager.alert('提示','请选择第三张自定义活动','warning');
		    	  return false;
			  }else if(threeType == 4 && (threeAppCustomPage == '' || threeAppCustomPage == null || threeAppCustomPage == undefined)){
				  $.messager.alert('提示','请选择第三张原生自定义页面','warning');
		    	  return false;
			  }else{
				  return true;
			  } 			
		  }
	}
	
	function validateTypeFour(){
	   	  var oneTitle = $("#oneTitle").val();
	   	  var twoTitle = $("#twoTitle").val();
	   	  var threeTitle = $("#threeTitle").val();
	   	  var fourTitle = $("#fourTitle").val();
	   	  var fourTitleColor = $("#fourTitleColor").val();
	   	  var fourImage = $("#fourImage").val();
	   	  var fourType = $("input[name='fourType']:checked").val();
	   	  var fourProductId = $("#fourProductId").val();
	   	  var fourGroupSale = $("#fourGroupSale").val();
	   	  var fourCustomSale = $("#fourCustomSale").combobox('getValue');
	   	  var fourAppCustomPage = $("#fourAppCustomPage").combobox('getValue');			
		  if(validateTypeThree()){
			  if($.trim(oneTitle) == ''){
				  $.messager.alert('提示','请输入第一张标题','warning');
		    	  return false;
			  }else if($.trim(twoTitle) == ''){
				  $.messager.alert('提示','请输入第二张标题','warning');
		    	  return false;
			  }else if($.trim(threeTitle) == ''){
				  $.messager.alert('提示','请输入第三张标题','warning');
		    	  return false;
			  }else if($.trim(fourTitle) == ''){
				  $.messager.alert('提示','请输入第四张标题','warning');
		    	  return false;
			  }else if($.trim(fourImage) == ''){
				  $.messager.alert('提示','请上传第四张图片','warning');
		    	  return false;
			  }else if(fourType == null || fourType == undefined || fourType == ''){
				  $.messager.alert('提示','请选择第四张关联类型','warning');
		    	  return false;
			  }else if(fourType == 1 && $.trim(fourProductId) == ''){
				  $.messager.alert('提示','请输入第四张商品ID','warning');
		    	  return false;
			  }else if(fourType == 2 && $.trim(fourGroupSale) == ''){
				  $.messager.alert('提示','请输入第四张组合ID','warning');
		    	  return false;
			  }else if(fourType == 3 && (fourCustomSale == '' || fourCustomSale == null || fourCustomSale == undefined)){
				  $.messager.alert('提示','请选择第四张自定义活动','warning');
		    	  return false;
			  }else if(fourType == 4 && (fourAppCustomPage == '' || fourAppCustomPage == null || fourAppCustomPage == undefined)){
				  $.messager.alert('提示','请选择第四张原生自定义页面','warning');
		    	  return false;
			  }else{
				  return true;
			  } 			
		  }		
	}
    
    var inputId;
    function picDialogOpen($inputId) {
        inputId = $inputId;
        var selectType = $("input[name='displayType']:checked").val();
        var oneTitle = $("#oneTitle").val();
        var threeTitle = $("#threeTitle").val();
        if(selectType == null || selectType == undefined || selectType == ''){
        	$.messager.alert('提示','请选择布局方式','error',function(){
                return ;
            });
        }
        /* if(inputId == 'oneImage'){
            if(selectType == 1){
            	$("#needWidth").val("750");
            	$("#needHeight").val("188");
        	}else if(selectType == 2){
        		$("#needWidth").val("374");
            	$("#needHeight").val("188");
        	}else if(selectType == 3){
        		if($.trim(oneTitle) == ''){
        			$("#needWidth").val("187");
                	$("#needHeight").val("188");
        		}else{
        			$("#needWidth").val("60");
                	$("#needHeight").val("60");
        		}
        	}else if(selectType == 4){
        		$("#needWidth").val("60");
            	$("#needHeight").val("60");
        	}
        }else if(inputId == 'twoImage'){
        	if(selectType == 2){
        		$("#needWidth").val("374");
            	$("#needHeight").val("188");
        	}else if(selectType == 3){
        		$("#needWidth").val("374");
            	$("#needHeight").val("188");
        	}else if(selectType == 4){
        		$("#needWidth").val("60");
            	$("#needHeight").val("60");
        	}
        }else if(inputId == 'threeImage'){
        	if(selectType == 3){
        		if($.trim(threeTitle) == ''){
        			$("#needWidth").val("187");
                	$("#needHeight").val("188");
        		}else{
        			$("#needWidth").val("60");
                	$("#needHeight").val("60");
        		}
        	}else if(selectType == 4){
        		$("#needWidth").val("60");
            	$("#needHeight").val("60");
        	}
        }else if(inputId == 'fourImage'){
        	$("#needWidth").val("60");
        	$("#needHeight").val("60");
        } */
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

</body>
</html>