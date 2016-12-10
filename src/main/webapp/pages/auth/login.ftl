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
      	<input type="hidden" name="rememberMe" value="true" />
        <h2 class="form-signin-heading">换吧网络-后台登陆</h2>
        <label for="inputName" class="sr-only">用户名</label>
        <input type="text" id="inputName" class="form-control" placeholder="用户名" name="name" required autofocus>
        <br/>
        <label for="inputPassword" class="sr-only">密码</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="密码" name="pwd" required>
		<label for="inputCode" class="sr-only">验证码</label>
		<input type="text" id="inputCode" class="form-control-code" placeholder="验证码" name="code" required>
		<img id="imgObj" alt="验证码"  src="${rc.contextPath}/verify/getCode" /><a href="javascript:;" onclick="reImg();">换一张</a>
		<br/>
		<br/>
        <button class="btn btn-lg btn-primary btn-block" type="button" id="saveButton">登陆</button>
      </form>
		<div id="messageDiv" class="alert alert-danger alert-dismissible" role="alert">
		  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
		  <span aria-hidden="true">&times;</span></button>
		  <strong>登陆失败：&nbsp;&nbsp;&nbsp;</strong><span id="wrongMessage"></span>
		</div>
    </div>
    <script>

    $(function(){
    	$(document).keydown(function(e){
    		if (!e){
    		   e = window.event;  
    		}  
    		if ((e.keyCode || e.which) == 13) {  
    		      $("#saveButton").click();  
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
    
	function reImg(){ 
		$("#imgObj").attr('src',"${rc.contextPath}/verify/getCode?rnd=" + Math.random());
    }

    $(function(){
    	var params = {
    			rememberMe:true,
    	};
    	$("#messageDiv").hide();
   		$("#saveButton").click(function(){
   			$("#messageDiv").hide();
   			params.name = $("input[name='name']").val();
   			params.pwd = $("input[name='pwd']").val();
   			params.code = $("input[name='code']").val();
   			if(params.name == '' || params.pwd == '' || params.code == ''){
   				$("#wrongMessage").html("请填写完整信息");
		       	$("#messageDiv").show();
   			}else{
   				$.ajax({ 
   	   		       url: '${rc.contextPath}/auth/shiroLogin',
   	   		       type: 'post',
   	   		       dataType: 'json',
   	   		       data: params,
   	   		       success: function(data){
   	   		           if(data.status == 1){
   	   		        		window.location.href = "${rc.contextPath}/common/index";
   	   		           }else{
   	   		        		$("#wrongMessage").html(data.msg);
   	   		        		$("#messageDiv").show();
   	   		           }
   	   		       },
   	   		       error: function(xhr){
   	   		    		//$("#wrongMessage").html('服务器忙，请稍后再试。('+xhr.status+')');
   	   		    		$("#messageDiv").show();
   	   		    		   	   		        		window.location.href = "${rc.contextPath}/common/index";
   	   		    		
   	   		       }
   	   		   });
   			}
   			
		});
    });
		
    </script>
  </body>
</html>