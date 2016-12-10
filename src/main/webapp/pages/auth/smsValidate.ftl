<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>换吧网络-后台登陆</title>
    <!-- Bootstrap -->
    <link href="${rc.contextPath}/pages/js/bootstrap-3.3.4-dist/css/bootstrap.min.css" rel="stylesheet">
	<link href="${rc.contextPath}/pages/js/bootstrap-3.3.4-dist/css/signin.css" rel="stylesheet">
	<script src="${rc.contextPath}/pages/js/jquery.min.js"></script>
	<script src="${rc.contextPath}/pages/js/bootstrap-3.3.4-dist/js/bootstrap.min.js"></script>
  </head>
  <body>
    <div class="container">
      <form id="ff" action="${rc.contextPath}/auth/shiroLogin" method="post" class="form-signin">
        <h5 class="form-signin-heading"><#if isSend?exists && (isSend==1)>您的验证码已发送，10分钟内请勿重复发送。<#else>系统检测到您正在常用办公地点之外登陆，为了确保您本次是安全登陆，系统将通短信的形式验证本次操作实属您本人。验证码已经发送到您的手机，请输入验证码。</#if></h5>
        <input type="text" id="inputMobileNumber" class="form-control" name="mobileNumber" value="${mobileNumber}" readonly="readonly">
        <br/>
        <input type="text" id="inputSmsCode" class="form-control" placeholder="验证码" name="smsCode" required="required">
        <div id="sendAgainDiv">
        <br/>
        <button class="btn btn-lg btn-primary btn-block" type="button" id="sendAgainButton">重新发送</button>
        </div>
		<br/>
        <button class="btn btn-lg btn-primary btn-block" type="button" id="confirmButton">确认</button>
      </form>
		<div id="messageDiv" class="alert alert-danger alert-dismissible" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
		  <span aria-hidden="true">&times;</span></button>
		  <strong>失败：&nbsp;&nbsp;&nbsp;</strong><span id="wrongMessage"></span>
		</div>
    </div>
    <script>

    $(function(){
    	$(document).keydown(function(e){
    		if (!e){
    		   e = window.event;  
    		}  
    		if ((e.keyCode || e.which) == 13) {  
    		      $("#confirmButton").click();  
    		}
    	});
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
    	
    	$("#inputSmsCode").focus(function(){
    		$("#messageDiv").hide();
    	});
    	
    	var params = {};
    	$("#sendAgainDiv").hide();
    	$("#messageDiv").hide();
   		$("#confirmButton").click(function(){
   			$("#messageDiv").hide();
   			params.mobileNumber = $("input[name='mobileNumber']").val();
   			params.smsCode = $("input[name='smsCode']").val();
   			if(params.mobileNumber == '' || params.smsCode == ''){
   				$("#wrongMessage").html("请填写完整信息");
		       	$("#messageDiv").show();
   			}else{
   				$.ajax({ 
   	   		       url: '${rc.contextPath}/auth/confirmSmsValidate',
   	   		       type: 'post',
   	   		       dataType: 'json',
   	   		       data: params,
   	   		       success: function(data){
   	   		           if(data.status == 1){
   	   		        		window.location.href = "${rc.contextPath}/common/index";
   	   		           }else if(data.status == 2){
   	   		      			// 验证码已过期
  	   		        		$("#wrongMessage").html(data.msg);
	   		        		$("#messageDiv").show();
	   		        		$("#sendAgainDiv").show();
   	   		           }else{
   	   		        		$("#wrongMessage").html(data.msg);
   	   		        		$("#messageDiv").show();
   	   		           }
   	   		       },
   	   		       error: function(xhr){
   	   		    		$("#wrongMessage").html('服务器忙，请稍后再试。('+xhr.status+')');
   	   		    		$("#messageDiv").show();
   	   		       }
   	   		   });
   			}
		});
   		
   		$("#sendAgainButton").click(function(){
   			var mobileNumber = $("input[name='mobileNumber']").val();
   			$.ajax({ 
	   		       url: '${rc.contextPath}/auth/sendSmsCode',
	   		       type: 'post',
	   		       dataType: 'json',
	   		       data: {mobileNumber:mobileNumber},
	   		       success: function(data){
	   		           if(data.status == 1){
	   		        		$("#sendAgainDiv").hide();
	   		           }else{
	   		        		$("#wrongMessage").html(data.msg);
	   		        		$("#messageDiv").show();
	   		           }
	   		       },
	   		       error: function(xhr){
	   		    		$("#wrongMessage").html('服务器忙，请稍后再试。('+xhr.status+')');
	   		    		$("#messageDiv").show();
	   		       }
	   		   });
   		});
    });
		
    </script>
  </body>
</html>