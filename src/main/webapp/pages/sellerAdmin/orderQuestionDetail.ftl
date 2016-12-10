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
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1340px;
	align:center;
}
ul a:link{
    text-decoration:none;
}
ul a:hover {
    color：black；
}
ul a:visited {
    color:black;
}
ul a:link {
    color:black;
}
ul a {
    color:black;
}
textarea{
	resize:none
}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'菜单列表',split:true" style="width: 180px;">
	 <#include "../sellerAdmin/menu.ftl" >
	</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div id="myCss">
		<a href="${rc.contextPath}/sellerAdminOrder/detail/${detail.orderId}" target="_blank" class="easyui-linkbutton" style="width:200px">在新窗口打开订单详情</a>
		该订单共有${detail.count}个问题正在进行中...<br/><br/><br/><br/>
		<div style="padding:10px;">
			<span style="padding-right:30px;">顾客问题处理：${detail.customerStatus}</span>
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
			<a id="sellerDeal" onclick="sellerDeal()" href="javascript:;" class="easyui-linkbutton" style="width:130px" >反馈</a>
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
        
        <!-- 反馈 begin-->
        <div id="updateOrderQuestion" class="easyui-dialog" style="width:600px;height:260px;padding:20px 20px;">
            <form id="updateOrderQuestion_form" method="post">
				<input id="updateOrderQuestion_form_qid" type="hidden" name="questionId" value="${detail.id}" >
				<span id="tips">商家反馈：</span>
				<span><textarea rows="4" cols="40" id="updateOrderQuestion_form_content" name="content" onkeydown="checkEnter(event)"></textarea></span><br/>
        	</form>
        </div>
		<!-- 反馈 end-->
	
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


function sellerDeal(){
	$('#updateOrderQuestion_form_content').val('');
	$('#updateOrderQuestion').dialog('open');
}

	
$(function(){
	
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
                       url:"${rc.contextPath}/sellerAdminOrder/sellerOrderQuestionFeedback",
                       onSubmit:function(){
                       	var content = $('#updateOrderQuestion_form_content').val();
                       	if($.trim(content)==''){
                       		$.messager.alert('提示',"请输入处理内容",'error');
                       		return false;
                       	}
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

});
</script>

</body>
</html>