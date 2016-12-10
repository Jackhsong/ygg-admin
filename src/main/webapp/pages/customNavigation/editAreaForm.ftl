<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
</head>
<body>

<div id="ajax_editArea_div">
    <form>
        <br>
        <input type="hidden" id="ajax_id" value="${id}" />
        展示地区设置 :
        <input type="radio" id="supportAreaType1" name="supportAreaType" value="0" <#if (supportAreaType == 0) >checked</#if>> 全国&nbsp;&nbsp;&nbsp;
        <input type="radio" id="supportAreaType2" name="supportAreaType" value="1" <#if (supportAreaType == 1) >checked</#if>> 部分地区<br/><br/>
        <div id="allArea">
        <#if pList?exists>
            <table border="0">
                <#list  pList as bl >
                    <tr class="ajax_province">
                        <td>
                            <input type="checkbox" name="ajax_province_all" <#if (bl.containsAll == 1) >checked</#if> onclick="ajaxCheckAll(this)" ><span style="color: red">${bl.name}</span>
                            <#list  bl.provinceList as b2 >
                                <input type="checkbox" <#if b2.selected?exists && (b2.selected == 1) >checked</#if> name="ajax_re_province1" value="${b2.provinceId}">
                                <span>${b2.provinceName}</span>
                            </#list>
                            <br>
                        </td>
                    </tr>
                </#list>
            </table>
        </#if>
        </div>
        <br>
        <input style="width: 150px; height: 30px" type="button" id="save" value="保存">
    </form>

</div>
<script>

    function ajaxCheckAll(obj){
        if($(obj).is(':checked')){
            $(obj).parent().find("input[name='ajax_re_province1']").prop("checked",true);
        }else{
            $(obj).parent().find("input[name='ajax_re_province1']").prop("checked",false);
        }
    }
    $(function(){

        $("#supportAreaType1").change(function(){
            if($(this).is(':checked')){
                $("#allArea").hide();
            }
        });

        $("#supportAreaType2").change(function(){
            if($(this).is(':checked')){
                $("#allArea").show();
            }
        });

        $("#supportAreaType1").change();
        $("#supportAreaType2").change();

        $("#save").click(function(){
            var id = $("#ajax_id").val();
            var supportAreaType= $("input[name='supportAreaType']:checked").val();
            var provinceStr="";
            $(".ajax_province").each(function(){
                var resources="";
                $(this).find("td").eq(0).find("input[name='ajax_re_province1']:checked").each(function(){
                    resources+=$(this).val()+",";
                });
                provinceStr+=resources;
            });
            $.messager.progress();
            $.ajax({
                url: '${rc.contextPath}/customNavigation/editArea',
                type: 'post',
                dataType: 'json',
                data: {'id':id,'supportAreaType':supportAreaType,'provinceStr':provinceStr},
                success: function(data){
                    $.messager.progress('close');
                    if(data.status == 1){
                        $.messager.alert("提示","保存成功","info",function(){
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