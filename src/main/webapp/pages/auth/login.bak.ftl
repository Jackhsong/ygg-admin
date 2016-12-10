<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台登陆</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

</head>
<body>
<div style="padding-left:30%;padding-top:100px;" >
	<h2>换吧网络-后台登陆</h2>
    <div style="margin:20px 0;"></div>
    <div class="easyui-panel" title="gegejia" style="width:550px">
        <div style="padding:10px 60px 20px 60px">
        <form id="ff" method="post">
        	<input type="hidden" name="rememberMe" value="true" />
            <table cellpadding="5">
                <tr>
                    <td>用户名:</td>
                    <td><input class="easyui-textbox"  ype="text" name="name"  data-options="required:true" ></input></td>
                    <td></td>
                </tr>
                <tr>
                    <td>密码:</td>
                    <td><input  class="easyui-textbox" type="password"  name="pwd" data-options="required:true" ></input></td>
                    <td></td>
                </tr>
                <tr>
                    <td>验证码:</td>
                    <td><input  class="easyui-textbox" type="text"  name="code" ></td>
                    <td><img id="imgObj" alt="验证码"  src="${rc.contextPath}/verify/getCode" /><a href="javascript:;" onclick="reImg();">换一张</a></td>
                </tr>
                <tr><td>
                	<input style="width: 80px" type="button" id="saveButton" value="登陆" />
                	<td></td>
                </td><td></td></tr>
            </table>
            
        </form>
        </div>
    </div>
    <script>

		function reImg(){ 
			$("#imgObj").attr('src',"${rc.contextPath}/verify/getCode?rnd=" + Math.random());
        }

    $(function(){
    	
    		$("#saveButton").click(function(){
				$('#ff').submit();
			});
			
			$('#ff').form({   
		    	url:'${rc.contextPath}/auth/shiroLogin',   
		    onSubmit: function(){
		        // do some check
		        // return false to prevent submit;
		        $.messager.progress();
		    },   
		    success:function(data){
		    	$.messager.progress('close');
		    	var data = eval('(' + data + ')');  // change the JSON string to javascript object   
		        if (data.status == 1){
		        	//window.close();
		            window.location.href = "${rc.contextPath}/common/index";
		        }else{
		        	$.messager.alert("提示",data.msg,"error");
		        }
		    }   
			}); 
    });
		
    </script>
</div>
</body>
</html>