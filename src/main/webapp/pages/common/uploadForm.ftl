<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 400px; height: 200px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
    <div id="yun_div" align="left">
        <label style="color: green;">源文件名称：</label><span id="originalName"></span> <br>  <br>
        <label style="color: green;">又拍云图片地址：</label><span id="yun_url"></span>
    </div>
</div>
<script type="text/javascript">

    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        })
    })

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
                        } else {
                            $("#originalName").html(res.originalName);
                            $("#yun_url").html(res.url);
                            $("#yun_div").css('display','block');
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