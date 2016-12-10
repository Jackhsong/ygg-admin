<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
</head>
<body>

<div id="addRole_div">
    <br>
    <span style="color: red">&nbsp;角色修改请联系技术开发</span><br>
    <br>
    <form>
        <input type="hidden" id="ajax_id" value="${id!'0'}" />
        <fieldset>
            <legend>显示信息</legend>
            角色名称: <input type="text" id="ajax_addRole_form_role" value="${role!''}" readonly />
            角色描述: <input type="text" id="ajax_addRole_form_desc" value="${description!''}" />
        </fieldset>
        <br>
        <fieldset>
            <legend>权限信息</legend>
                <#if permissionList?exists>
                    <table border="1">
                        <#list  permissionList as bl >
                            <tr class="ajax_resources">
                                <td>
                                    ${bl.category}
                                </td>
                                <td>
                                    <input type="checkbox" name="ajax_re_permission_all" onclick="ajaxCheckAll(this)" ><span style="color: red">全选</span>
                                    <#list  bl.pes as b2 >
                                        <input type="checkbox" <#if b2.selected?exists && (b2.selected == 1) >checked</#if> name="ajax_re_permission" value="${b2.id}">
                                        <span title="${b2.permission}">${b2.description}</span>
                                    </#list>
                                    <br>
                                </td>
                            </tr>
                        </#list>
                    </table>
                </#if>
        </fieldset>
        <br>
        <input style="width: 150px; height: 30px" type="button" id="save" value="保存">
    </form>

</div>
<script>
    function ajaxCheckAll(obj){
        if($(obj).is(':checked')){
            $(obj).parent().find("input[name='ajax_re_permission']").prop("checked",true);
        }else{
            $(obj).parent().find("input[name='ajax_re_permission']").prop("checked",false);
        }
    }
    $(function(){
        $("#save").click(function(){
            var id = $("#ajax_id").val();
            var addRole_form_role = $("#ajax_addRole_form_role").val();
            var addRole_form_desc = $("#ajax_addRole_form_desc").val();
            var permissionStr="";
            $(".ajax_resources").each(function(){
//                		var des = $(this).find("td").eq(0).find("input[name='addRole_form_description']").val();
                var resources="";
                $(this).find("td").eq(1).find("input[name='ajax_re_permission']:checked").each(function(){
                    resources+=$(this).val()+",";
                });
                permissionStr+=resources;
            });
            $.ajax({
                url: '${rc.contextPath}/admin/updateRole',
                type: 'post',
                dataType: 'json',
                data: {'id':id,'role':addRole_form_role,'resource':permissionStr,'desc':addRole_form_desc},
                success: function(data){
                    $.messager.progress('close');
                    if(data.status == 1){
                        $.messager.alert("提示","保存成功","info",function(){
                            window.location.href = window.location.href;
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
        })
    })
</script>
</body>
</html>