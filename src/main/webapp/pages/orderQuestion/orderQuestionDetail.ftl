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
	#myCss span {
		font-size:12px;
		padding-right:20px;
	}
	
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

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div id="myCss">
		<a href="${rc.contextPath}/order/detail/${detail.orderId}" target="_blank" class="easyui-linkbutton" style="width:200px">在新窗口打开订单详情</a>
		该订单共有${detail.count}个问题正在进行中...<br/><br/><br/><br/>
		<div style="padding:10px;">
			<span style="padding-right:30px;">顾客问题处理：${detail.customerStatus}</span>
			<a id="customerDeal" onclick="customerDeal()" href="javascript:;" class="easyui-linkbutton" style="width:120px" >更新问题</a>
			<a id="sendMsg" onclick="sendMsg()" href="javascript:;" class="easyui-linkbutton" style="width:100px">发短信</a>
			<br/>
			<hr/>
			<span>${detail.createTime}</span>
			<span>${detail.createUser}新增问题</span>
			<span>类型：${detail.questionType}</span>
			<span>回复顾客时限：${detail.timeLimit}</span>
			<span>${detail.questionDesc}</span>
			<br/>
			<#if detail.imageList?exists && (detail.imageList?size>0)>
				<#list detail.imageList as image>
					<span><a href="${image}" target="_blank"><img alt="" src="${image}" style="max-height: 50px;max-width: 50px"/></a></span>
				</#list>
				<br/><br/>
			</#if>
			<#if detail.cProgressList?? && (detail.cProgressList?size>0)>
				<#list detail.cProgressList as customer>
					<span>${customer.createTime}</span>
					<span>${customer.operation}</span>
					<span>${customer.content}</span><br/>
					<#if customer.images?exists && (customer.images?size>0)>
						<#list customer.images as image>
						<span><a href="${image}" target="_blank"><img alt="" src="${image}" style="max-height: 50px;max-width: 50px"/></a></span>
						</#list>
					</#if>
					<br/><br/>
				</#list>
			</#if>
		</div>
		<br/><br/><br/><br/>
		<div style="padding:10px;">
			<span style="padding-right:30px;">商家对接：${detail.sellerStatus}</span>
			<a id="sellerDeal" onclick="sellerDeal()" href="javascript:;" class="easyui-linkbutton" style="width:130px" >更新对接情况</a>
			<a id="sendMsg" onclick="sendMsg()" href="javascript:;" class="easyui-linkbutton" style="width:100px">发短信</a>
			<#if detail.isPush?exists && (detail.isPush==0)><a id="pushToSeller" onclick="push('${detail.id}')" href="javascript:;" class="easyui-linkbutton" style="width:130px">推送给商家</a></#if>
			<br/>
			<hr/>
			<span>${detail.createTime}</span>
			<span>${detail.createUser}新增商家对接</span>
			<br/><br/>
			<#if detail.sProgressList?? && (detail.sProgressList?size>0)>
				<#list detail.sProgressList as seller>
					<span>${seller.createTime}</span>
					<span>${seller.operation}</span>
					<span>${seller.content}</span><br/>
					<#if seller.images?exists && (seller.images?size>0)>
						<#list seller.images as image>
						<span><a href="${image}" target="_blank"><img alt="" src="${image}" style="max-height: 50px;max-width: 50px"/></a></span>
						</#list>
					</#if>
					<br/><br/>
				</#list>
			</#if>
		</div>
		<#if detail.isPush?exists && (detail.isPush==1)>
		<br/><br/><br/><br/>
		<div style="padding:10px;">
			<span style="padding-right:30px;">商家反馈</span>
			<br/>
			<hr/>
			<#if detail.feedBackList?? && (detail.feedBackList?size>0)>
				<#list detail.feedBackList as feedBack>
					<span>${feedBack.createTime}</span>
					<span>${feedBack.content}</span>
					<br/><br/>
				</#list>
			</#if>
		</div>
		</#if>
        
        <!-- 订单问题 begin-->
        <div id="updateOrderQuestion" class="easyui-dialog" style="width:600px;height:260px;padding:20px 20px;">
            <form id="updateOrderQuestion_form" method="post">
				<input id="updateOrderQuestion_form_qid" type="hidden" name="questionId" value="${detail.id}" >
				<input id="updateOrderQuestion_form_type" type="hidden" name="dealType" value="1" >
				<span id="tips">&nbsp;&nbsp;最新进展：</span>
				<span><textarea rows="4" cols="40" id="updateOrderQuestion_form_content" name="content" onkeydown="checkEnter(event)"></textarea></span><br/>
				<table id="imageDetails">
					<tr>
						<td>&nbsp;&nbsp;上传图片：<br/></td>
						<td>
							<input type="text" value=""  name="image" style="width: 250px;"/>
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
							<br/>
						</td>
						<td>
							<span onclick="addImageDetailsRow(this)" style="cursor: pointer;color:red">添加&nbsp;|</span>
                    		<span onclick="removeImageDetailsRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
							<br/>
						</td>
					</tr>
			  </table>
			  <span>&nbsp;&nbsp;是否完结：</span>
			  <span>
			  	<input type="radio" name="status" value="1" id="status1"/>进行中
			  	<input type="radio" name="status" value="2" id="status2"/>已完结
			  </span>
			  <div id="isMarkDiv">
			  <span>&nbsp;&nbsp;是否标记：</span>
			  <span>
			  	<input type="radio" name="isMark" value="1" id="isMark1"/>标记
			  	<input type="radio" name="isMark" value="0" id="isMark0" checked="checked"/>不标记
			  </span>
			  </div>
			  <!-- <div id="replyDiv">
				  <span>回复顾客时限：</span>
				  <span>
				  	<input type="radio" name="timeLimitType" value="1"/>1小时后
				  	<input type="radio" name="timeLimitType" value="2"/>2小时后
				  	<input type="radio" name="timeLimitType" value="3"/>自定义
				  	<input type="text" name="customTime"  id="customTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00'})"/>
				  </span>
			  </div> -->
        	</form>
        </div>
		<!-- 订单问题 end-->
		
		<!-- 发送短信begin -->
		<div id="sendMsgDiv" class="easyui-dialog" style="width:600px;height:270px;padding:20px 20px;">
			<form id="sendMsgForm"  method="post" enctype="multipart/form-data">
				<table>
					<tr>
						<td>手机号：</td>
						<td>
							<input id="sendMsgForm_phone" name="phone" value="${detail.receiveMobile}"  />
						</td>
					</tr>
					<tr>
						<td colspan="2"><span style="padding-left:50px">短信内容前面会自动加上“【换吧网络】”</span></td>
					</tr>
					<tr>
						<td>内容：</td>
						<td><textarea  id="sendMsgForm_content" name="content" onkeydown="checkEnter(event)" style="height: 100px;width: 400px" ></textarea></td>
					</tr>
					<tr>
						<td colspan="2">
							<input style="width: 100px" type="button" id="saveButton" value="发送"/>
							&nbsp;&nbsp;文字长度：<span id="counter">0 字</span>
							短信条数：<span id="tiaoshu">0</span>
						</td>
					</tr>
				</table>		
			</form>	
		</div>	
		<!-- 发送短信end -->
	
		<!-- 上传图片begin -->	
		<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
		    <form id="picForm" method="post" enctype="multipart/form-data">
		        <input id="picFile" type="file" name="picFile" /><br/><br/>
		        <input id="limitSize" type="hidden" name="limitSize" value="0"/><br/><br/>
		        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
		    </form>
		</div>
		<!-- 上传图片end -->	
	
</div>

<script>

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

function customerDeal(){
	$('#tips').html("&nbsp;&nbsp;最新进展：");
	$('#updateOrderQuestion_form_type').val('1');
	$('#updateOrderQuestion_form_content').val('');
	/* $('input[name="timeLimitType"]').each(function(){
		$(this).prop("checked",false);
	}); */
	$('input[name="status"]').each(function(){
		$(this).prop("checked",false);
	});
	/* $('#customTime').val(''); */
	/* $('#replyDiv').show(); */
	$("#isMarkDiv").hide();
	$('#updateOrderQuestion').dialog('open');
}

function sellerDeal(){
	$('#tips').text("商家对接状态：");
	$('#updateOrderQuestion_form_type').val('2');
	$('#updateOrderQuestion_form_content').val('');
	/* $('input[name="timeLimitType"]').each(function(){
		$(this).prop("checked",false);
	}); */
	$('input[name="status"]').each(function(){
		$(this).prop("checked",false);
	});
	/* $('#customTime').val(''); */
	/* $('#replyDiv').hide(); */
	$("#isMarkDiv").show();
	$('#updateOrderQuestion').dialog('open');
}

function sendMsg(){
	$("#sendMsgForm_content").val('');
	$("#sendMsgDiv").dialog('open');	
}
$(document).keyup(function() { 
	var text=$("#sendMsgForm_content").val(); 
	var counter=text.length; 
	$("#counter").text(counter+" 字"); 
	var tiaoshu = Math.ceil(counter/60);
	$("#tiaoshu").text(tiaoshu); 

});  

$("#saveButton").click(function(){
    	var phone = $("#sendMsgForm_phone").val();
    	var content = $("#sendMsgForm_content").val();
		var counter = content.length; 
    	if(content == ""){
    		$.messager.alert("提示","请输入发送内容","info");
    	}else if(counter >= 500){
			$.messager.alert("提示","输入字数不得大于500","error");
		}else{
    		$('#sendMsgForm').submit();
    	}
});

function push(id){
	$.messager.confirm('提示','确认推送给商家吗？',function(r){   
	    if (r){   
    		$.messager.progress();
            $.ajax({
            	url:'${rc.contextPath}/orderQuestion/updateOrderQuestionPushStatus',
            	type:'post',
            	dataType:'json',
            	data:{'id':id},
            	success:function(data){
            		$.messager.progress('close');
					if(data.status == 1){
						window.location.href = window.location.href
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
	}); 
}
	
$(function(){
	
     $('#picDia').dialog({
         title:'又拍图片上传窗口',
         collapsible:true,
         closed:true,
         modal:true
     });

     /* $("#status1").click(function(){
    	 var type = $("#updateOrderQuestion_form_type").val();
    	 if(type == 1){
	    	 if($(this).is(':checked')){
	    		 $("#replyDiv").show();
	    	 }
    	 }
     }); */
     /* $("#status2").click(function(){
    	 if($(this).is(':checked')){
    		 $("#replyDiv").hide();
    		 $('input[name="timeLimitType"]').each(function(){
    				$(this).prop("checked",false);
    		 });
    		 $('#customTime').val('');
    	 }
     }); */

	$('#updateOrderQuestion').dialog({
           title:'处理问题',
           collapsible:true,
           closed:true,
           modal:true,
           buttons:[{
               text:'保存信息',
               iconCls:'icon-ok',
               handler:function(){
                   $('#updateOrderQuestion_form').form('submit',{
                       url:"${rc.contextPath}/orderQuestion/updateOrderQuestionProgress",
                       onSubmit:function(){
                       	var content = $('#updateOrderQuestion_form_content').val();
                       	var status = $("#updateOrderQuestion_form input[name='status']:checked").val();
                       	/* var timeLimitType = $("input[name='timeLimitType']:checked").val();
                       	var customTime = $("#customTime").val(); */
                       	if($.trim(content)==''){
                       		$.messager.alert('提示',"请输入处理内容",'error');
                       		return false;
                       	}else if(status == null || status == '' || status == undefined){
                       		$.messager.alert('提示',"请选择是否完结",'error');
                       		return false;
                       	}/* else if(timeLimitType == 3 && $.trim(customTime) == ''){
                       		$.messager.alert('提示',"请填写自定义顾客回复时限",'error');
                    		return false;
                       	} */
                       	$.messager.progress(); 
                       },
                       success:function(data){
                           var res = eval("("+data+")")
                           if(res.status == 1){
                           	$.messager.progress('close'); 
                               $.messager.alert('响应信息',"保存成功",'info',function(){
                               	window.location.href = window.location.href
                                   $('#updateAddress').dialog('close');
                               });
                           }else{
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
                   $('#updateOrderQuestion').dialog('close');
               }
           }]
       });

	
	$("#sendMsgDiv").dialog({
        title:'发送短息',
        collapsible:true,
        closed:true,
        modal:true
    });
	
	$('#sendMsgForm').form({   
	    url:'${rc.contextPath}/sms/send',   
	    onSubmit: function(){   
	        $.messager.progress();
	    },   
	    success:function(data){
	    	$.messager.progress('close');
	    	var data = eval('(' + data + ')');  // change the JSON string to javascript object   
	        if (data.status == 1){
	            $.messager.alert("提示",data.msg,"info");
	            $("#sendMsgDiv").dialog('close');
	        }else{
	        	$.messager.alert("提示",data.msg,"error");	
	        }
	    }   
	});
});
	
function addImageDetailsRow(obj){
	var length = $("#imageDetails").find("tr").size();
	var row= $("#imageDetails").find("tr").first().clone();
	$(row).find("input[name='image']").val('');
	$(obj).parent().parent().after(row);
	$('#updateOrderQuestion').dialog('resize',{
		width: 600,
		height: 300 + 50 * length
	});
}

function removeImageDetailsRow(obj){
	$(obj).parent().parent().remove();
	var length = $("#imageDetails").find("tr").size();
	$('#updateOrderQuestion').dialog('resize',{
		width: 600,
		height: 260 + 10 * length
	});
}


var $obj;
function picDialogOpen(obj) {
	$obj = $(obj).prev();
    $("#picDia").dialog("open");
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
                    if($obj != null && $obj!='' && $obj !=undefined) {
                    	$obj.val(res.url);
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
    });
}
</script>

</body>
</html>