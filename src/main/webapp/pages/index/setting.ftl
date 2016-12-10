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

<style>
	span{font-size:14px;}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center'" style="padding: 5px;padding-left: 15px;">
		<div style="padding:10px">
			<span>10元新人礼包弹窗与注册送券功能</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前状态：<font color="red"><#if map1.value?exists && (map1.value=="1") >已开启<#elseif map1.value?exists && (map1.value=="0") >已关闭</#if></font></span>&nbsp;&nbsp;
			<#if map1.value?exists && (map1.value=="1") >
			<a onclick="updateConfigStatus('${map1.id}','0')" href="javascript:;" class="easyui-linkbutton">马上关闭</a>
			<#elseif map1.value?exists && (map1.value=="0") >
			<a onclick="updateConfigStatus('${map1.id}','1')" href="javascript:;" class="easyui-linkbutton">马上开启</a>
			</#if>
			<br/><br/>
			<hr/><br/><br/>
			<span>弹出推广广告版本号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前版本：<font color="red"><span id="currentVersion"><#if map2.value?exists >${map2.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateAdvertiseVersion('${map2.id}')" href="javascript:;" class="easyui-linkbutton">更新版本号</a>
			<br/><br/>
			<!-- <hr/><br/><br/>
			<span>平台支持的最低版本号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前版本：<font color="red"><#if map3.value?exists >${map3.value}</#if></font></span>&nbsp;&nbsp;
			<a onclick="updatePlatformVersion('${map3.id}')" href="javascript:;" class="easyui-linkbutton">更新版本号</a>
			<br/><br/> -->
			<hr/><br/><br/>
			<span>格格微信号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前微信号：<font color="red"><span id="currentWX"><#if map4.value?exists >${map4.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateWeiXin('${map4.id}')" href="javascript:;" class="easyui-linkbutton">更新微信号</a>
			<br/><br/>
			<hr/><br/><br/>
			<span>心动慈露微信号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前微信号：<font color="red"><span id="currentGroupWX"><#if map12.value?exists >${map12.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateGroupWeiXin('${map12.id}')" href="javascript:;" class="easyui-linkbutton">更新微信号</a>
			<br/><br/>
			<hr/><br/><br/>
			<span>购物返积分倍数</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前倍数：<font color="red"><span id="currentFactor"><#if map5.value?exists >${map5.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateShoppingReturnPoint('${map5.id}')" href="javascript:;" class="easyui-linkbutton">更新倍数</a>
			<br/><br/>
			<hr/><br/><br/>
			<span>首页top榜左侧原生自定义页面id</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前Id：<font color="red"><span id="currentLeftPageId"><#if map6.value?exists >${map6.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateLeftPageId('${map6.id}','left_page_id','${map6.value}')" href="javascript:;" class="easyui-linkbutton">更新ID</a>
			<br/><br/>
			<hr/><br/><br/>
			<span>首页top榜右侧原生自定义页面id</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前Id：<font color="red"><span id="currentRightPageId"><#if map7.value?exists >${map7.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateRightPageId('${map7.id}','right_page_id','${map7.value}')" href="javascript:;" class="easyui-linkbutton">更新ID</a>
			<br/><br/>
			<hr/><br/><br/>
			<span>商城品牌原生自定义页面id</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前Id：<font color="red"><span id="currentBrandId"><#if map8.value?exists >${map8.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateBrandId('${map8.id}','brand_page_id','${map8.value}')" href="javascript:;" class="easyui-linkbutton">更新ID</a>
			<br/><br/>
			<hr/><br/><br/>
			<span>不支持银联支付的商家id</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前Id：<font color="red"><span id="currentSellerId"><#if map9.value?exists >${map9.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateUnionpaySellerId('${map9.id}','not_display_unionpay_sellerid','${map9.value}')" href="javascript:;" class="easyui-linkbutton">更新ID</a>
			<br/><br/>
			<hr/><br/><br/>
			<span>省市区版本号</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前版本号：<font color="red"><span id="currentNationwideVersion"><#if map10.value?exists >${map10.value}</#if></span></font></span>&nbsp;&nbsp;
			<a onclick="updateNationwideVersion('${map10.id}','nationwide_version','${map10.value}')" href="javascript:;" class="easyui-linkbutton">更新省市区版本号</a>
			<br/><br/>
			<hr/><br/><br/>
			<span>首页格格推荐标题栏</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span>当前状态：<font color="red"><#if map11.value?exists && (map11.value=="1") >已展现<#elseif map11.value?exists && (map11.value=="0") >不展现</#if></font></span>&nbsp;&nbsp;
			<#if map11.value?exists && (map11.value=="1") >
			<a onclick="updateConfigStatus('${map11.id}','0')" href="javascript:;" class="easyui-linkbutton">不展现</a>
			<#elseif map11.value?exists && (map11.value=="0") >
			<a onclick="updateConfigStatus('${map11.id}','1')" href="javascript:;" class="easyui-linkbutton">展现</a>
			</#if>
			<hr/><br/><br/>
			<span>热卖榜单品图片：</span>
			<span><input type="text" name="oneImage" id="oneImage" value="${map13.value}" readonly="readonly" style="width: 300px;"/></span>
			<span><a onclick="picDialogOpen('oneImage','oneImg')" class="easyui-linkbutton">上传图片</a></span>
			<span style="color: red">必填&nbsp;&nbsp;</span><span style="color: red" id="oneTip"></span><br>
			<span><img id="oneImg" src=""></span>
			<a onclick="saveImage('${map13.id}',$('#oneImage').val())" href="javascript:;" class="easyui-linkbutton">保存</a>
			<hr/><br/><br/>
			<a href="${rc.contextPath}/index/vestList" target="_blank" class="easyui-linkbutton">马甲APP弹窗管理</a>
			<br/><br/>
		</div>
		
	    <!-- 修改广告版本号 begin-->
		<div id="updateAdvertiseVersionDiv" class="easyui-dialog" style="width:300px;height:150px;padding:10px 10px;">
            <form id="updateAdvertiseVersion_form" method="post">
			<input id="updateAdvertiseVersion_form_id" type="hidden" name="id" value="" >
            <table cellpadding="5">
                <tr>
                	<td>版本号:</td>
                	<td>
                		<input id="updateAdvertiseVersion_form_version" name="version"/>
                	</td>
                </tr>
            </table>
        	</form>
        </div>
        <!-- 修改广告版本号  end-->
        
       	<!-- 修改平台支持版本号 begin-->
		<div id="updatePlatformVersionDiv" class="easyui-dialog" style="width:300px;height:150px;padding:10px 10px;">
            <form id="updatePlatformVersion_form" method="post">
			<input id="updatePlatformVersion_form_id" type="hidden" name="id" value="" >
            <table cellpadding="5">
                <tr>
                	<td>版本号:</td>
                	<td>
                		<input id="updatePlatformVersion_form_version" name="version"/>
                	</td>
                </tr>
            </table>
        	</form>
        </div>
        <!-- 修改平台支持版本号  end-->
        
       	<!-- 修改微信号 begin-->
		<div id="updateWeiXinDiv" class="easyui-dialog" style="width:300px;height:150px;padding:10px 10px;">
            <form id="updateWeixin_form" method="post">
			<input id="updateWeixin_form_id" type="hidden" name="id" value="" >
            <table cellpadding="5">
                <tr>
                	<td>微信号:</td>
                	<td>
                		<input id="updateWeixin_form_weixin" name="weixin" maxlength="30"/>
                	</td>
                </tr>
            </table>
        	</form>
        </div>
        <!-- 修改微信号  end-->
        
        
        	<!-- 修改心动慈露微信号 begin-->
		<div id="updateGroupWeiXinDiv" class="easyui-dialog" style="width:300px;height:150px;padding:10px 10px;">
            <form id="updateGroupWeiXin_form" method="post">
			<input id="updateGroupWeiXin_form_id" type="hidden" name="id" value="" >
            <table cellpadding="5">
                <tr>
                	<td>心动慈露微信号:</td>
                	<td>
                		<input id="updateGroupWeiXin_form_weixin" name="groupWeiXin" maxlength="30"/>
                	</td>
                </tr>
            </table>
        	</form>
        </div>
        <!-- 修改心动慈露微信号  end-->
        
        
        
       	<!-- 修改购物返积分倍数 begin-->
		<div id="updateShoppingReturnPointDiv" class="easyui-dialog" style="width:300px;height:150px;padding:10px 10px;">
            <form id="updateShoppingReturnPoint_form" method="post">
			<input id="updateShoppingReturnPoint_form_id" type="hidden" name="id" value="" >
            <table cellpadding="5">
                <tr>
                	<td>购物返积分倍数:</td>
                	<td>
                		<input id="updateShoppingReturnPoint_form_factor" name="factor" maxlength="30"/>
                	</td>
                </tr>
            </table>
        	</form>
        </div>
        <!-- 修改购物返积分倍数  end-->
        
		<div id="updateConfigDiv" class="easyui-dialog" style="width:400px;height:180px;padding:10px 10px;">
			<form id="updateConfig_form" method="post">
			<input id="updateConfig_id" type="hidden" name="id" value="" >
			<input id="updateConfig_key" type="hidden" name="key" value="" >
            <table cellpadding="5">
                <tr>
                	<td id="title"></td>
                </tr>
                <tr>
                	<td>
                		<input id="updateConfig_value" name="value" maxlength="30" style="width: 300px;"/>
                	</td>
                </tr>
            </table>
            </form>
        </div>
        
	</div>
<div id="picDia" class="easyui-dialog" closed="true" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
    	<input type="hidden" name="needWidth" id="needWidth" value="0">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>
<script>
	$(function(){
	    $('#picDia').dialog({
	        title:'又拍图片上传窗口',
	        collapsible:true,
	        closed:true,
	        modal:true
	    });
	});
	var imageId = '';
	var imgSrc = '';
	function picDialogOpen(id, img) {
		imageId = id;
		imgSrc = img;
		$('picForm').form('reset');
		$('#picDia').dialog('open');
	}
	//图片上传
	function picUpload() {
	    $('#picForm').form('submit',{
	        url:"${rc.contextPath}/pic/fileUpLoad",
	        async:false,
	        success:function(data) {
	            var res = eval("("+data+")")
	            if(res.status == 1) {
	            	$.messager.alert('响应信息',"上传成功...",'info', function() {
	        			$("#picDia").dialog("close");
	                    $('#' + imageId).val(res.url);
	                    $('#' + imgSrc).attr('src', res.url);
	                    $('#' + imgSrc).show();
	        		});
	            } else{
	                $.messager.alert('响应信息',res.msg,'error',function(){
	                    return ;
	                });
	            }
	        }
	    })
	}

function saveImage(id, image){
	if(image == '') {
		$.messager.alert('警告','热卖榜单品图片不能为空', 'warning');
		return;
	}
	var tips = '确定保存吗?';
	 $.messager.confirm("提示信息",tips,function(ra){
        if(ra){
           $.messager.progress();
           $.post("${rc.contextPath}/index/saveImage",{id:id,image:image},function(data){
               $.messager.progress('close');
               if(data.status == 1){
                   $.messager.alert('响应信息',"操作成功",'info',function(){
                      window.location.reload();
                   });
               } else{
                   $.messager.alert('响应信息',data.msg,'error');
               }
           });
        }
    });
}

function updateConfigStatus(id, status){
	var tips = '';
	if(status == 1){
		tips = '确定开启吗？';
	}else{
		tips = '确定关闭吗？';
	}
	 $.messager.confirm("提示信息",tips,function(ra){
        if(ra){
           $.messager.progress();
           $.post("${rc.contextPath}/index/updateConfigStatus",{id:id,status:status},function(data){
               $.messager.progress('close');
               if(data.status == 1){
                   $.messager.alert('响应信息',"操作成功",'info',function(){
                      window.location.reload();
                   });
               } else{
                   $.messager.alert('响应信息',data.msg,'error');
               }
           });
        }
    });
}

function clearUpdateAdvertiseVersionForm(){
	$("input[id^='updateAdvertiseVersion_form_']").each(function(){
		$(this).val('');
	});
}

function clearUpdatePlatformVersionForm(){
	$("input[id^='updatePlatformVersion_form_']").each(function(){
		$(this).val('');
	});
}

function clearUpdateWeiXinForm(){
	$("input[id^='updateWeixin_form_']").each(function(){
		$(this).val('');
	});
}

function clearUpdateGroupWeiXinForm(){
	$("input[id^='updateGroupWeiXin_form_']").each(function(){
		$(this).val('');
	});
}


function clearUpdateShoppingReturnPointForm(){
	$("input[id^='updateShoppingReturnPoint_form_']").each(function(){
		$(this).val('');
	});
}

function updateAdvertiseVersion(id){
	clearUpdateAdvertiseVersionForm();
    $("#updateAdvertiseVersion_form_id").val(id);
    $("#updateAdvertiseVersionDiv").dialog('open');
}

function updatePlatformVersion(id){
	clearUpdatePlatformVersionForm();
    $("#updatePlatformVersion_form_id").val(id);
    $("#updatePlatformVersionDiv").dialog('open');
}

function updateWeiXin(id){
	clearUpdateWeiXinForm();
    $("#updateWeixin_form_id").val(id);
    $("#updateWeiXinDiv").dialog('open');
}

function updateGroupWeiXin(id){
	clearUpdateGroupWeiXinForm();
    $("#updateGroupWeiXin_form_id").val(id);
    $("#updateGroupWeiXinDiv").dialog('open');
}


function updateShoppingReturnPoint(id){
	clearUpdateShoppingReturnPointForm();
    $("#updateShoppingReturnPoint_form_id").val(id);
    $("#updateShoppingReturnPointDiv").dialog('open');
}

function updateLeftPageId(id,key,value){
	$("#updateConfig_id").val('');
	$("#updateConfig_value").val('');
	$("#title").text('首页top榜左侧原生自定义页面id:');
	$("#updateConfig_id").val(id);
	$("#updateConfig_key").val(key);
	$("#updateConfig_value").val(value);
	$("#updateConfig_value").attr('type','number');
	$("#updateConfigDiv").dialog('open');
}

function updateRightPageId(id,key,value){
	$("#updateConfig_id").val('');
	$("#updateConfig_value").val('');
	$("#title").text('首页top榜右侧原生自定义页面id:');
	$("#updateConfig_id").val(id);
	$("#updateConfig_key").val(key);
	$("#updateConfig_value").val(value);
	$("#updateConfig_value").attr('type','number');
	$("#updateConfigDiv").dialog('open');
}

function updateBrandId(id,key,value){
	$("#updateConfig_id").val('');
	$("#updateConfig_value").val('');
	$("#title").text('商城品牌原生自定义页面id:');
	$("#updateConfig_id").val(id);
	$("#updateConfig_key").val(key);
	$("#updateConfig_value").val(value);
	$("#updateConfig_value").attr('type','number');
	$("#updateConfigDiv").dialog('open');
}

function updateUnionpaySellerId(id,key,value){
	$("#updateConfig_id").val('');
	$("#updateConfig_value").val('');
	$("#title").html('不支持银联支付商家Id(<span style="color:red">多个商家时用逗号,分割</span>):');
	$("#updateConfig_id").val(id);
	$("#updateConfig_key").val(key);
	$("#updateConfig_value").val(value);
	$("#updateConfig_value").attr('type','text');
	$("#updateConfigDiv").dialog('open');
}

function updateNationwideVersion(id,key,value){
	$("#updateConfig_id").val('');
	$("#updateConfig_value").val('');
	$("#title").text('省市区版本号：');
	$("#updateConfig_id").val(id);
	$("#updateConfig_key").val(key);
	$("#updateConfig_value").val(value);
	$("#updateConfig_value").attr('type','number');
	$("#updateConfigDiv").dialog('open');
}
$(function(){
	$('#updateAdvertiseVersionDiv').dialog({
        title:'更新版本号',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存',
            iconCls:'icon-ok',
            handler:function(){
                $('#updateAdvertiseVersion_form').form('submit',{
                    url:"${rc.contextPath}/index/updateAdvertiseVersion",
                    onSubmit: function(){   
                        var oldVersion = $("span[id='currentVersion']").text();
                        var newVersion = $("#updateAdvertiseVersion_form_version").val();
                        if(!/^\d+$/.test(newVersion)){
                        	$.messager.alert('提示','版本号只能为数字');
                        	return false;	
                        }else if(parseInt(newVersion)<=parseInt(oldVersion)){
                        	$.messager.alert('提示','版本号必须大于当前版本号');
                        	return false;
                        }
                        $.messager.progress();
                    },
                    success:function(data){
                    	$.messager.progress('close');
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                            	clearUpdateAdvertiseVersionForm();
                                $('#updateAdvertiseVersionDiv').dialog('close');
                                window.location.reload();
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'error');
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
            	clearUpdateAdvertiseVersionForm();
                $('#updateAdvertiseVersionDiv').dialog('close');
            }
        }]
    });
	
	$('#updatePlatformVersionDiv').dialog({
        title:'更新版本号',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存',
            iconCls:'icon-ok',
            handler:function(){
                $('#updatePlatformVersion_form').form('submit',{
                    url:"${rc.contextPath}/index/updatePlatformVersion",
                    onSubmit: function(){   
                        var newVersion = $("#updatePlatformVersion_form_version").val();
                        var reg = /^[1-9]+.\d{1}$/;
                        if(!reg.test(newVersion)){
                        	$.messager.alert('提示','版本号只能为小数，并且只有一位小数','warning');
                        	return false;	
                        }
                        $.messager.progress();
                    },
                    success:function(data){
                    	$.messager.progress('close');
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                            	clearUpdatePlatformVersionForm();
                                $('#updatePlatformVersionDiv').dialog('close');
                                window.location.reload();
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'error');
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
            	clearUpdatePlatformVersionForm();
                $('#updatePlatformVersionDiv').dialog('close');
            }
        }]
    });
	
	$('#updateWeiXinDiv').dialog({
        title:'更新微信号',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存',
            iconCls:'icon-ok',
            handler:function(){
                $('#updateWeixin_form').form('submit',{
                    url:"${rc.contextPath}/index/updateWeiXin",
                    onSubmit: function(){   
                        var weixin = $("#updateWeixin_form_weixin").val();
                        if($.trim(weixin) == ''){
                        	$.messager.alert('提示','请输入微信号','warning');
                        	return false;	
                        }
                        $.messager.progress();
                    },
                    success:function(data){
                    	$.messager.progress('close');
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                            	clearUpdatePlatformVersionForm();
                                $('#updateWeiXinDiv').dialog('close');
                                window.location.reload();
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'error');
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
            	clearUpdatePlatformVersionForm();
                $('#updateWeiXinDiv').dialog('close');
            }
        }]
    });
	
	
		$('#updateGroupWeiXinDiv').dialog({
        title:'更新心动慈露微信号',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存',
            iconCls:'icon-ok',
            handler:function(){
                $('#updateGroupWeiXin_form').form('submit',{
                    url:"${rc.contextPath}/index/updateGroupWeiXin",
                    onSubmit: function(){   
                        var groupWeiXin = $("#updateGroupWeiXin_form_weixin").val();
                        if($.trim(groupWeiXin) == ''){
                        	$.messager.alert('提示','请输入微信号','warning');
                        	return false;	
                        }
                        $.messager.progress();
                    },
                    success:function(data){
                    	$.messager.progress('close');
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                            	//clearUpdatePlatformVersionForm();
                                $('#updateGroupWeiXinDiv').dialog('close');
                                window.location.reload();
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'error');
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
            	//clearUpdatePlatformVersionForm();
                $('#updateGroupWeiXinDiv').dialog('close');
            }
        }]
    });
	
	
	$('#updateShoppingReturnPointDiv').dialog({
        title:'更新购物返积分倍数',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存',
            iconCls:'icon-ok',
            handler:function(){
                $('#updateShoppingReturnPoint_form').form('submit',{
                    url:"${rc.contextPath}/index/updateShoppingReturnPointFactor",
                    onSubmit: function(){   
                        var factor = $("#updateShoppingReturnPoint_form_factor").val();
                        var reg = /^[1-9]+.\d{1}$/;
                        if(!reg.test(factor)){
                        	$.messager.alert('提示','倍数只能为小数，并且只有一位小数','warning');
                        	return false;	
                        }
                        $.messager.progress();
                    },
                    success:function(data){
                    	$.messager.progress('close');
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                            	clearUpdateShoppingReturnPointForm();
                                $('#updateShoppingReturnPointDiv').dialog('close');
                                window.location.reload();
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'error');
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
            	clearUpdateShoppingReturnPointForm();
                $('#updateShoppingReturnPointDiv').dialog('close');
            }
        }]
    });
	
	$('#updateConfigDiv').dialog({
        title:'更新',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存',
            iconCls:'icon-ok',
            handler:function(){
                $('#updateConfig_form').form('submit',{
                    url:"${rc.contextPath}/index/updateConfig",
                    onSubmit: function(){   
                        var value = $("#updateConfig_value").val();
                        var key = $("#updateConfig_key").val();
                        if($.trim(value) ==''){
                        	$.messager.alert('提示','请输入要修改的值');
                        	return false;	
                        }
                        if(key == 'nationwide_version'){
                        	var currentNationwideVersion = $("#currentNationwideVersion").text();
                        	if(parseInt(value)<parseInt(currentNationwideVersion)){
                        		$.messager.alert('提示','修改后的版本号必须大于当前版本号');
                            	return false;
                        	}
                        }
                        $.messager.progress();
                    },
                    success:function(data){
                    	$.messager.progress('close');
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                                $('#updateConfigDiv').dialog('close');
                                window.location.reload();
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'error');
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#updateConfigDiv').dialog('close');
            }
        }]
    });
});
</script>

</body>
</html>