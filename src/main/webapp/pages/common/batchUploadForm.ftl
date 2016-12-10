<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<link href="${rc.contextPath}/pages/js/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/uploadify/jquery-1.4.2.min.js"></script>
<script src="${rc.contextPath}/pages/js/uploadify/swfobject.js"></script>
<script src="${rc.contextPath}/pages/js/uploadify/jquery.uploadify.v2.1.4.min.js"></script>

</head>
<body>

<div id="fileQueue"></div>
<br/>

<input type="file" name="uploadify" id="uploadify" />
<br/>
<p>
    <a href="javascript: jQuery('#uploadify').uploadifyUpload()">开始上传</a>&nbsp;
    <a href="javascript:jQuery('#uploadify').uploadifyClearQueue()">取消所有上传</a>
</p>
<br/><br/>

<table align="center" width="70%">
    <thead>
    <th>图片文件名</th>
    <th>又拍图片地址</th>
    <th>图片</th>
    <th>图片上传状态</th>
    </thead>
    <tbody id="info">
    </tbody>
</table>

<script type="text/javascript">
    //官方网址：http://www.uploadify.com/
    $(document).ready(function(){
        //$("#uploadify").uploadifySettings('scriptData',	{'name':'liudong','age':22});
        $("#uploadify").uploadify({
            'uploader'	:	"${rc.contextPath}/pages/js/uploadify/uploadify.swf",
            'script'    :	"${rc.contextPath}/pic/batchFileUpLoad",
            'cancelImg' :	"${rc.contextPath}/pages/js/uploadify/cancel.png",
//            'folder'	:	"uploads",//上传文件存放的路径,请保持与uploadFile.jsp中PATH的值相同
            'queueId'	:	"fileQueue",
            'queueSizeLimit'	:	100,//限制上传文件的数量
            'fileExt'	:	"*.jpg,*.png,*.gif,*.bmp,*.jpeg"  , //"*.RAR,*.rar",
//            'fileDesc'	:	"*.exe ,*.rar",//限制文件类型
            'auto'		:	false,
            'multi'		:	true,//是否允许多文件上传
            'simUploadLimit':	2,//同时运行上传的进程数量
            'fileSizeLimit':'300KB',
            'buttonText':	"files",
//            'scriptData':	{'name':'liudong','age':22},//这个参数用于传递用户自己的参数，此时'method' 必须设置为GET, 后台可以用request.getParameter('name')获取名字的值
            'method'	:	"POST"    ,
            'onComplete': function(_event,queueId,fileObj,response,data) {
                //  alert(response);
                var jsonData = eval("("+response+")");
                var tr;
                if(!jsonData.status){
                     tr='<tr> <td>'+jsonData.originalName+'</td><td>'+jsonData.url+'</td><td><img src="'+jsonData.url+'!v1cartProduct" /></td><td><font color="red">'+jsonData.msg+'</font></td></tr>'
                }  else{
                     tr='<tr> <td>'+jsonData.originalName+'</td><td>'+jsonData.url+'</td><td><img src="'+jsonData.url+'!v1cartProduct" /></td><td><font color="green">'+jsonData.msg+'</font></td></tr>'
                }

                $('#info').append(tr);


            }
        });

    });
</script>

</body>
</html>