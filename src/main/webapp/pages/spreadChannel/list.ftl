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
<style type="text/css">
textarea{
	resize:none;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'渠道管理',split:true" style="height: 120px;">
			<div style="height: 60px;padding: 10px">
				<span>渠道名称：</span>
				<span><input id="searchChannelName" name="searchChannelName"/></span>
				<span>渠道状态：</span>
				<span>
					<select id="searchIsAvailable" name="searchIsAvailable">
	          			<option value="-1">全部</option>
	          			<option value="1">可用</option>
	          			<option value="0">停用</option>
	           		</select>
				</span>
				<span>
					<a id="searchBtn" onclick="searchSpread()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
					<a id="clearBtn" onclick="clearSpread()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>&nbsp;
				</span>			
			</div>
		</div>
		
		<div data-options="region:'center'" >
	    	<!--数据表格-->
	    	<table id="s_data" style=""></table>
    	</div>
    	
		<!-- 新增渠道dialog begin -->
		<div id="addChannelDiv" class="easyui-dialog" style="width:650px;height:400px;padding:10px 10px;">
			<form action="" id="channelForm" method="post">
				<input id="channelForm_channelId" type="hidden" name="channelId" value=""/>
				<p>
					<span>渠道名称：</span>
					<span><input type="text" id="channelForm_channelName" name="channelName" value="" maxlength="20" style="width:400px;"/></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>分享标题：</span>
					<span><input type="text" id="channelForm_shareTitle" name="shareTitle" value="" maxlength="50" style="width:400px;"/></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>分享图标：</span>
					<span>
						<input type="text" id="channelForm_shareImage" name="shareImage" value="" style="width:330px;"/><font color="red">*</font>
						<a onclick="picDialogOpen('channelForm_shareImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
					</span>
				</p>
				<p>
					<span>分享内容：</span>
					<span><textarea id="channelForm_shareContent" name="shareContent" cols="45" rows="3"></textarea></span>
					<span style="color:red">* &nbsp;注：字数 &lt; 200</span>
				</p>
				<div id="canNotEditDiv">
				<p>
					<div>
						<p>
						<span>优惠券Id：</span>
						<span><input type="text" name="couponId" value="" style="width:180px;" onchange="getCouponInfo(this)" onblur="stringTrim(this)"/><font color="red">*</font></span>
						<span>&nbsp;优惠券数量：</span><span><input type="text" name="couponAmount" value="" style="width:40px;" onblur="stringTrim(this)"/><font color="red">*</font></span>
						<span onclick="addCouponInfo(this)" style="cursor: pointer;color:red">&nbsp;添加&nbsp;|</span>
                    	<span onclick="removeCouponInfo(this)" style="cursor: pointer;color:red">&nbsp;删除</span>&nbsp;
                    	<span name="couponInfo" style="color:red"></span>
                    	</p>
					</div>
				</p>
				<p>
					<span>优惠券有效期：</span>
					<span>
						<input type="radio" name="dateType" id="channelForm_dateType1" value="1"/>从原有优惠券中读取信息&nbsp;
		                <input type="radio" name="dateType" id="channelForm_dateType2" value="2"/>领券之日起
		                <input type="text"  name="days" id="channelForm_days" onblur="stringTrim(this)" style="width:30px;"/>天内
					</span>
				</p>
				</div>
			</form>
		</div>
		<!-- 新增渠道dialog end -->
		

		<!-- 编辑渠道dialog begin -->
		<div id="editChannelDiv" class="easyui-dialog" style="width:650px;height:300px;padding:10px 10px;">
			<form action="" id="editChannelForm" method="post">
				<input id="editChannelForm_channelId" type="hidden" name="channelId" value=""/>
				<p>
					<span>渠道名称：</span>
					<span><input type="text" id="editChannelForm_channelName" name="channelName" value="" maxlength="20" style="width:400px;"/></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>分享标题：</span>
					<span><input type="text" id="editChannelForm_shareTitle" name="shareTitle" value="" maxlength="50" style="width:400px;"/></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>分享图标：</span>
					<span>
						<input type="text" id="editChannelForm_shareImage" name="shareImage" value="" style="width:330px;"/><font color="red">*</font>
						<a onclick="picDialogOpen('editChannelForm_shareImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
					</span>
				</p>
				<p>
					<span>分享内容：</span>
					<span><textarea id="editChannelForm_shareContent" name="shareContent" cols="45" rows="3"></textarea></span>
					<span style="color:red">* &nbsp;注：字数 &lt; 200</span>
				</p>
			</form>
		</div>
		<!-- 编辑渠道dialog end -->
				
		<!-- 编辑短信内容begin -->
        <div id="editMsgContentDiv" class="easyui-dialog" style="width:400px;height:250px;padding:20px 20px;">
        	<input type="hidden" id="channelId" value=""/>
            <table cellpadding="5">
                <tr>
                    <td>短信内容：<font color="red">(字数 &lt; 200)</font></td>
                </tr>
                <tr>
                	<td>
                    	<textarea rows="5" cols="45" id="msgContent" onkeydown="checkEnter(event)"></textarea>
                    </td>
                </tr>
            </table>
        </div>
        <!-- 编辑短信内容end -->
		
	</div>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
</div>

<script type="text/javascript">
$(function(){
	$(document).keydown(function(e){
		if (!e){
		   e = window.event;  
		}  
		if ((e.keyCode || e.which) == 13) {  
		      $("#searchBtn").click();  
		}
	});
});

function stringTrim(obj){
	var value = $(obj).val();
	$(obj).val($.trim(value));
}

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

function searchSpread(){
	$("#s_data").datagrid('load',{
		channelName:$("#searchChannelName").val(),
		isAvailable:$("#searchIsAvailable").val()
	});
}

function clearSpread(){
	$("#searchChannelName").val('');
	$("#searchIsAvailable").find('option').eq(0).attr('selected','selected');
	$("#s_data").datagrid('load',{});
}

function cleanAddChannelDiv(){
	$("#channelForm_channelId").val('');
	$("#channelForm input[type='text']").each(function(){
		$(this).val('');
	});
	$("input[name='couponId']").each(function(){
		$(this).val('');
	});
	$("input[name='couponAmount']").each(function(){
		$(this).val('');
	});
	$("#channelForm_shareContent").val('');
}

function cleanEditChannelDiv(){
	$("#editChannelForm_channelId").val('');
	$("#editChannelForm input[type='text']").each(function(){
		$(this).val('');
	});
	$("#editChannelForm_shareContent").val('');
}

function editIt(index){
	cleanEditChannelDiv();
    var arr=$("#s_data").datagrid("getData");
    $("#editChannelForm_channelId").val(arr.rows[index].id);
    $("#editChannelForm_channelName").val(arr.rows[index].channelName);
    $("#editChannelForm_shareTitle").val(arr.rows[index].shareTitle); 
    $("#editChannelForm_shareImage").val(arr.rows[index].shareImage);
    $("#editChannelForm_shareContent").val(arr.rows[index].shareContent);
    $("#editChannelDiv").dialog('open');
}

function addCouponInfo(obj){
	var row= $(obj).parent().parent().clone();
	$(row).find("input[name='couponId']").val('');
	$(row).find("input[name='couponAmount']").val('');
	$(row).find("span[name='couponInfo']").text('');
	$(obj).parent().parent().after(row);
	var n = $(obj).parent().parent().parent().find("div").length - 1;
	$('#addChannelDiv').dialog('resize',{
		width: 650,
		height: 330 + 50 * n.length
	});
}

function removeCouponInfo(obj){
	$(obj).parent().parent().remove();
	var n = $(obj).parent().parent().parent().find("div").length - 1;
	$('#addChannelDiv').dialog('resize',{
		width: 650,
		height: 330 + 50 * n.length
	});
}

function getCouponInfo(obj){
	var couponId = $.trim($(obj).val());
	if(couponId != ''){
         $.post(
           	"${rc.contextPath}/coupon/getCouponInfoById",
            {id:couponId},
            function(data){
            	$(obj).parent().parent().children().last().text(data.couponInfo);
            }
        );
	}
}

function deleteId(id){
	$.messager.confirm("提示信息","确定删除吗？",function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/spreaChannel/deleteSpreadChannel",
	            		{id:id},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('load');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	           });
        }
    });
}

function updateAvailable(id, isAvailable){
	var tip = "";
	if(isAvailable == 0){
		tip = "确定停用吗？";
	}else{
		tip = "确定启用吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/spreaChannel/updateChannelAvailable",
	            		{id:id,isAvailable:isAvailable},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('load');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	           });
        }
    });
}


function updateSendMsg(index, status){
	var arr=$("#s_data").datagrid("getData");
	var msgContent = arr.rows[index].msgContent;
	var id = arr.rows[index].id;
	var tip = "";
	if(status == 0){
		tip = "确定关闭短信提醒吗？";
	}else{
		tip = "确定开启短信提醒吗？";
		if($.trim(msgContent)==''){
			$.messager.alert('提示',"短信内容未设置，不能开启短息提醒",'error');
			return false;
		}
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/spreaChannel/updateChannelSendMsg",
	            		{id:id,status:status},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('load');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	           });
        }
    });
}

function editMsgContent(index){
	$("#channelId").val('');
	$("#msgContent").val('');
	var arr=$("#s_data").datagrid("getData");
	var msgContent = arr.rows[index].msgContent;
	var id = arr.rows[index].id;
	$("#channelId").val(id);
	$("#msgContent").val(msgContent);
	$("#editMsgContentDiv").dialog('open');
}

$(function(){
	
    $('#s_data').datagrid({
    	nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/spreaChannel/jsonSpreadChannelInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns: [
            [
                {field: 'id', title: 'ID', width: 10, align: 'center'},
                {field: 'channelName', title: '渠道名称', width: 30, align: 'center'},
                {field: 'shareTitle', title: '分享标题', width: 30, align: 'center'},
                {field: 'shareImageURL', title: '分享图标', width: 35, align: 'center'},
                {field: 'shareContent', title: '分享内容', width: 40, align: 'center'},
                {field: 'url', title: '分享链接', width: 50, align: 'center'},
                {field: 'coupon', title: '优惠券', width: 90, align: 'center'},
                {field: 'validityDate', title: '优惠券有效期', width: 30, align: 'center'},
                {field: 'receiveAmount', title: '领取人数', width: 18, align: 'center'},
                {field: 'totalMoney', title: '订单交易额', width: 20, align: 'center'},
                {field: 'registerAmount', title: '注册新用户', width: 20, align: 'center'},
                {field: 'newBuyerMoney', title: '新用户交易额', width: 25, align: 'center'},
                {field: 'msgContent', title: '短信提醒内容', width: 35, align: 'center'},
                {field: 'msgStatus', title: '短信提醒状态', width: 25, align: 'center'},
                {field: 'hidden', title: '操作', width: 65, align: 'center',
                    formatter: function (value, row, index) {
                    	var a = '<a href="javaScript:;" onclick="editMsgContent(' + index + ')">编辑短信内容</a> | ';
                    	var b = ''
                       	if(row.isSendMsg == 1){
                       		b = '<a href="javaScript:;" onclick="updateSendMsg(' + index + ',' + 0 + ')">关闭短信提醒</a> | ';
                       	}else{
                       		b = '<a href="javaScript:;" onclick="updateSendMsg(' + index + ',' + 1 + ')">开启短信提醒</a> | ';
                       	}
                    	var c = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                    	var d = ''
                    	if(row.isAvailableCode == 1){
                    		d = '<a href="javaScript:;" onclick="updateAvailable(' + row.id + ',' + 0 + ')">停用</a>';
                    	}else{
                    		d = '<a href="javaScript:;" onclick="updateAvailable(' + row.id + ',' + 1 + ')">启用</a>';
                    	}
                    	return a + b + c + d;
                    }
                }
            ]
        ],
        toolbar: [
            {
                id: '_add',
                iconCls:'icon-add',
                text: '新增渠道',
                handler: function () {
                	cleanAddChannelDiv();
                	$('#addChannelDiv').dialog('open');
                }
            }
        ],
        pagination:true
    });

    $('#addChannelDiv').dialog({
    	title:'渠道信息(带<font color="red">*</font>为必填项)',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			var params = {};
    			params.couponIdAndCount='';
    			params.channelId=$('#channelForm_channelId').val();
    			params.channelName = $('#channelForm_channelName').val();
    			params.shareTitle = $('#channelForm_shareTitle').val();
    			params.shareImage = $('#channelForm_shareImage').val();
    			params.shareContent = $('#channelForm_shareContent').val();
    			params.dateType = $("input[name='dateType']:checked").val();
    			params.days = $("#channelForm_days").val();
    			
				var mIds = $("input[name='couponId']");
				var isCouponIdEmpty = true;
				$.each(mIds,function(i,item){
					var id = $(mIds[i]).val();
					if(!/^[1-9]\d*$/.test(id)){
						isCouponIdEmpty = false;
						return false;
					}
				});
				var isCouponAmountEmpty = true;
				var mCounts = $("input[name='couponAmount']");
				$.each(mCounts,function(i,item){
					var count = $(mCounts[i]).val();
					if(!/^[1-9]\d*$/.test(count)){
						isCouponAmountEmpty = false;
						return false;
					}else if(count>5){
						isCouponIdEmpty = false;
						return false;
					}
				});
				if($.trim(params.channelName)==''){
					$.messager.alert("info","请输入渠道名称","warn");
					return false;
				}else if($.trim(params.shareTitle)==''){
					$.messager.alert("info","请输入分享标题","warn");
					return false;
				}else if($.trim(params.shareImage)==''){
					$.messager.alert("info","请上传分享图标","warn");
					return false;
				}else if($.trim(params.shareContent)==''){
					$.messager.alert("info","请输入分享内容","warn");
					return false;
				}else if(params.shareContent.length>200){
					$.messager.alert("info","请限制分享内容字数在200以内","warn");
					return false;
				}else if(!isCouponIdEmpty || !isCouponAmountEmpty){
					$.messager.alert("提示","优惠券Id和数量只能为正整数,并且数量不能大于5","warn");
					return false;
				}else if(params.dateType == '' || params.dateType==null || params.dateType==undefined){
					$.messager.alert("提示","请选择优惠券有效期","warn");
					return false;
				}else if(params.dateType == 2 && (!/^[1-9]\d*$/.test(params.days))){
					$.messager.alert("提示","请输入有效期","warn");
					return false;
				}else if(params.couponIdAndCount=''){
					$.messager.alert("提示","请输入优惠券Id和数量","warn");
				}else{
					$.each(mIds,function(i,item){
						var id = $(mIds[i]).val();
						var count = $(mCounts[i]).val();
						params.couponIdAndCount += (id+":"+count+",");
					});
					$.messager.progress();
	    			$.ajax({
						url: '${rc.contextPath}/spreaChannel/save',
						type: 'post',
						dataType: 'json',
						data: params,
						success: function(data){
							$.messager.progress('close');
							if(data.status == 1){
								$.messager.alert("提示","保存成功","info",function(){
									$('#s_data').datagrid("load");
									$('#addChannelDiv').dialog('close');
								});
							}else{
								$.messager.alert("提示",data.msg,"error");
							}
						},
						error: function(xhr){
							$.messager.progress('close');
							$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
						}
					});
				}
    		}
    	},
    	{
    		text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#addChannelDiv').dialog('close');
            }
    	}]
    });
    
    
    $('#editChannelDiv').dialog({
    	title:'渠道信息(带<font color="red">*</font>为必填项)',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			var params = {};
    			params.channelId=$('#editChannelForm_channelId').val();
    			params.channelName = $('#editChannelForm_channelName').val();
    			params.shareTitle = $('#editChannelForm_shareTitle').val();
    			params.shareImage = $('#editChannelForm_shareImage').val();
    			params.shareContent = $('#editChannelForm_shareContent').val();
				if($.trim(params.channelName)==''){
					$.messager.alert("info","请输入渠道名称","warn");
					return false;
				}else if($.trim(params.shareTitle)==''){
					$.messager.alert("info","请输入分享标题","warn");
					return false;
				}else if($.trim(params.shareImage)==''){
					$.messager.alert("info","请上传分享图标","warn");
					return false;
				}else if($.trim(params.shareContent)==''){
					$.messager.alert("info","请输入分享内容","warn");
					return false;
				}else if(params.shareContent.length>200){
					$.messager.alert("info","请限制分享内容字数在200以内","warn");
					return false;
				}else{
					$.messager.progress();
	    			$.ajax({
						url: '${rc.contextPath}/spreaChannel/update',
						type: 'post',
						dataType: 'json',
						data: params,
						success: function(data){
							$.messager.progress('close');
							if(data.status == 1){
								$.messager.alert("提示","保存成功","info",function(){
									$('#s_data').datagrid("load");
									$('#editChannelDiv').dialog('close');
								});
							}else{
								$.messager.alert("提示",data.msg,"error");
							}
						},
						error: function(xhr){
							$.messager.progress('close');
							$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
						}
					});
				}
    		}
    	},
    	{
    		text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#editChannelDiv').dialog('close');
            }
    	}]
    });
    
    
	$('#editMsgContentDiv').dialog({
        title:'编辑短信内容',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存信息',
            iconCls:'icon-ok',
            handler:function(){
            	var msgContent = $("#msgContent").val();
            	var channelId = $("#channelId").val();
            	if($.trim(msgContent) == ''){
            		$.messager.alert("提示",'请输短信内容',"warning");
            	}else if(msgContent.length >= 200){
            		$.messager.alert("提示",'短信内容字数超过限制',"warning");
            	}else{
           			$.messager.progress();
       				$.ajax({
       		            url: '${rc.contextPath}/spreaChannel/editMsgContent',
       		            type: 'post',
       		            dataType: 'json',
       		            data: {'msgContent':msgContent,'channelId':channelId},
       		            success: function(data){
       		            	$.messager.progress('close');
       		                if(data.status == 1){
       		                	$('#editMsgContentDiv').dialog('close');
       		                	$('#s_data').datagrid('load');
       		                	$.messager.alert("提示","保存成功","");
       		                }else{
       		                	$.messager.alert("提示",data.msg,"error");
       		                }
       		            },
       		            error: function(xhr){
       		            	$.messager.progress('close');
       		            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
       		            }
       		        });
            	}
				
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#editMsgContentDiv').dialog('close');
            }
        }]
    });
    
    $('#picDia').dialog({
        title:'又拍图片上传窗口',
        collapsible:true,
        closed:true,
        modal:true
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
</script>

</body>
</html>