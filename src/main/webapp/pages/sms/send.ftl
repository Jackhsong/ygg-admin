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
	<form id="sendContent"  method="post" enctype="multipart/form-data">
		<fieldset>
			<legend style="color: red;font-size: 20px;">亿美短信</legend>
			<table>
				<tr>
					<td>余额：</td>
					<td style="color: red">${balance!""}元</td>
				</tr>
				<tr>
					<td>手机号：</td>
					<td>
						<input id="phone" name="phone" value=""  />
						<span style="color:red;font-size:20px"> 或 </span>
						<input type="text" name="phoneFile" style="width:300px" />(excel第一行为标题，第二行开始为手机号。)<br/><br/><br/>
					</td>
				</tr>
				<tr>
					<td colspan="2"><span style="padding-left:50px">短信内容前面会自动加上“【换吧网络】”</span></td>
				</tr>
				<tr>
					<td>内容：</td>
					<td><textarea  id="content" name="content" onkeydown="checkEnter(event)" style="height: 80px;width: 600px" ></textarea></td>
				</tr>
				<tr>
					<td colspan="2">
						<input style="width: 100px" type="button" id="saveButton" value="发送"/>
						&nbsp;&nbsp;文字长度：<span id="counter">0 字</span>
						短信条数：<span id="tiaoshu">0</span>
					</td>
				</tr>
			</table>
		</fieldset>
	</form>
 	<br/><br/>
	<hr/>
	<br/><br/>
	<form id="sendContent2"  method="post" enctype="multipart/form-data">
		<fieldset>
			<legend style="color: red;font-size: 20px;">梦网短信（换吧网络）</legend>
			<table>
				<tr>
					<td>余额：</td>
					<td style="color: red">${montnets!""}条</td>
				</tr>
				<tr>
					<td>手机号：</td>
					<td>
						<input id="phone2" name="phone" value=""  />
						<span style="color:red;font-size:20px"> 或 </span>
						<input type="text" name="phoneFile" style="width:300px" />(excel第一行为标题，第二行开始为手机号。)<br/><br/><br/>
					</td>
				</tr>
				<tr>
					<td colspan="2"><span style="padding-left:50px">短信内容前面会自动加上“【换吧网络】”</span></td>
				</tr>
				<tr>
					<td>内容：</td>
					<td><textarea  id="content2" name="content" onkeydown="checkEnter(event)" style="height: 80px;width: 600px" ></textarea></td>
				</tr>
				<tr>
					<td colspan="2">
						<input style="width: 100px" type="button" id="saveButton2" value="发送"/>
						&nbsp;&nbsp;文字长度：<span id="counter2">0 字</span>
						短信条数：<span id="tiaoshu2">0</span>
					</td>
				</tr>
			</table>		
		</fieldset>
	</form>
 	<br/><br/>
	<hr/>
	<br/><br/>
	<form id="sendContent3"  method="post" enctype="multipart/form-data">
		<fieldset>
			<legend style="color: red;font-size: 20px;">梦网短信（心动慈露）</legend>
			<table>
				<tr>
					<td>余额：</td>
					<td style="color: red">${montnetsTuan!""}条</td>
				</tr>
				<tr>
					<td>手机号：</td>
					<td>
						<input id="phone3" name="phone" value=""  />
						<span style="color:red;font-size:20px"> 或 </span>
						<input type="text" name="phoneFile" style="width:300px" />(excel第一行为标题，第二行开始为手机号。)<br/><br/><br/>
					</td>
				</tr>
				<tr>
					<td colspan="2"><span style="padding-left:50px">短信内容前面会自动加上“【心动慈露】”</span></td>
				</tr>
				<tr>
					<td>内容：</td>
					<td><textarea  id="content3" name="content" onkeydown="checkEnter(event)" style="height: 80px;width: 600px" ></textarea></td>
				</tr>
				<tr>
					<td colspan="2">
						<input style="width: 100px" type="button" id="saveButton3" value="发送"/>
						&nbsp;&nbsp;文字长度：<span id="counter3">0 字</span>
						短信条数：<span id="tiaoshu3">0</span>
					</td>
				</tr>
			</table>		
		</fieldset>
	</form>
    <br/><br/>
    <hr/>
    <br/><br/>
    <form id="sendContent4"  method="post" enctype="multipart/form-data">
        <fieldset>
            <legend style="color: red;font-size: 20px;">梦网短信（心动慈露
）</legend>
            <table>
                <tr>
                    <td>余额：</td>
                    <td style="color: red">${montnetsGlobal!""}条</td>
                </tr>
                <tr>
                    <td>手机号：</td>
                    <td>
                        <input id="phone4" name="phone" value=""  />
                        <span style="color:red;font-size:20px"> 或 </span>
                        <input type="text" name="phoneFile" style="width:300px" />(excel第一行为标题，第二行开始为手机号。)<br/><br/><br/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2"><span style="padding-left:50px">短信内容前面会自动加上“【心动慈露
】”</span></td>
                </tr>
                <tr>
                    <td>内容：</td>
                    <td><textarea  id="content4" name="content" onkeydown="checkEnter(event)" style="height: 80px;width: 600px" ></textarea></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input style="width: 100px" type="button" id="saveButton4" value="发送"/>
                        &nbsp;&nbsp;文字长度：<span id="counter4">0 字</span>
                        短信条数：<span id="tiaoshu4">0</span>
                    </td>
                </tr>
            </table>
        </fieldset>
    </form>
</div>

<script>
	
	$(document).keyup(function() { 
		var text=$("#content").val(); 
		var counter=text.length; 
		$("#counter").text(counter+" 字"); 
		var tiaoshu = Math.ceil(counter/60);
		$("#tiaoshu").text(tiaoshu); 
		
		var text2 = $("#content2").val();
		var counter2=text2.length; 
		$("#counter2").text(counter2+" 字"); 
		var tiaoshu2 = Math.ceil(counter2/67);
		$("#tiaoshu2").text(tiaoshu2);
		
		var text3 = $("#content3").val();
		var counter3=text3.length; 
		$("#counter3").text(counter3+" 字"); 
		var tiaoshu3 = Math.ceil(counter3/67);
		$("#tiaoshu3").text(tiaoshu3);

        var text4 = $("#content4").val();
        var counter4=text4.length;
        $("#counter4").text(counter4+" 字");
        var tiaoshu4 = Math.ceil(counter4/67);
        $("#tiaoshu4").text(tiaoshu4);
	});  

	$("#saveButton").click(function(){
	    	var phone = $("#phone").val();
	    	var content = $("#content").val();
			var counter = content.length; 
	    	if(content == ""){
	    		$.messager.alert("提示","请输入发送内容","info");
	    	}else if(counter >= 500){
				$.messager.alert("提示","输入字数不得大于500","error");
			}else{
	    		$('#sendContent').submit();
	    	}
    	});
	
	$("#saveButton2").click(function(){
    	var phone = $("#phone2").val();
    	var content = $("#content2").val();
		var counter = content.length; 
    	if(content == ""){
    		$.messager.alert("提示","请输入发送内容","info");
    	}else if(counter >= 350){
			$.messager.alert("提示","输入字数不得大于350","error");
		}else{
    		$('#sendContent2').submit();
    	}
    });
	
	$("#saveButton3").click(function(){
    	var phone = $("#phone3").val();
    	var content = $("#content3").val();
		var counter = content.length; 
    	if(content == ""){
    		$.messager.alert("提示","请输入发送内容","info");
    	}else if(counter >= 350){
			$.messager.alert("提示","输入字数不得大于350","error");
		}else{
    		$('#sendContent3').submit();
    	}
    });

    $("#saveButton4").click(function(){
        var phone = $("#phone4").val();
        var content = $("#content4").val();
        var counter = content.length;
        if(content == ""){
            $.messager.alert("提示","请输入发送内容","info");
        }else if(counter >= 350){
            $.messager.alert("提示","输入字数不得大于350","error");
        }else{
            $('#sendContent4').submit();
        }
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
		$("input[name='phoneFile']").each(function(){
			$(this).filebox({
				buttonText: '打开发货文件',
				buttonAlign: 'right'
			});
		});

	
		$('#sendContent').form({   
		    url:'${rc.contextPath}/sms/send',   
		    onSubmit: function(){
		        $.messager.progress();
		    },   
		    success:function(data){
		    	$.messager.progress('close');
		    	var data = eval('(' + data + ')');
		        if (data.status == 1){
		            $.messager.alert("提示",data.msg,"info");	
		        }else{
		        	$.messager.alert("提示",data.msg,"error");	
		        }
		    }   
		});
		
		$('#sendContent2').form({   
		    url:'${rc.contextPath}/sms/sendMontnets',   
		    onSubmit: function(){   
		        $.messager.progress();
		    },   
		    success:function(data){
		    	$.messager.progress('close');
		    	var data = eval('(' + data + ')');   
		        if (data.status == 1){
		            $.messager.alert("提示",data.msg,"info");	
		        }else{
		        	$.messager.alert("提示",data.msg,"error");	
		        }
		    }   
		});
		
		$('#sendContent3').form({   
		    url:'${rc.contextPath}/sms/sendMontnetsTuan',   
		    onSubmit: function(){   
		        $.messager.progress();
		    },   
		    success:function(data){
		    	$.messager.progress('close');
		    	var data = eval('(' + data + ')');   
		        if (data.status == 1){
		            $.messager.alert("提示",data.msg,"info");	
		        }else{
		        	$.messager.alert("提示",data.msg,"error");	
		        }
		    }   
		});

        $('#sendContent4').form({
            url:'${rc.contextPath}/sms/sendMontnetsGlobal',
            onSubmit: function(){
                $.messager.progress();
            },
            success:function(data){
                $.messager.progress('close');
                var data = eval('(' + data + ')');
                if (data.status == 1){
                    $.messager.alert("提示",data.msg,"info");
                }else{
                    $.messager.alert("提示",data.msg,"error");
                }
            }
        });

	});
	
</script>

</body>
</html>