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
            ------------------------------------------------------------------------------------------<br/>
        <#if hotCityList?exists>
            <table border="0">
                提示: <span style="color: red;">若某省份下地区全无勾选，则表示支持该省全市。否则只支持勾选地区。</span><br/><br/>
                <#list  hotCityList as bl >
                    <tr class="ajax_province_city" id="hot_${bl.provinceId}" data-provinceId="${bl.provinceId}" style="display: none">
                        <td>
                            <span>${bl.provinceName}</span>
                            <input type="checkbox" name="ajax_province_city_all" <#if (bl.containsAll == 1) >checked</#if> onclick="ajaxCheckAllCity(this)" ><span style="color: red">全部</span>
                            <#list  bl.provinceHotCityList as b2 >
                                <input type="checkbox" <#if b2.selected?exists && (b2.selected == 1) >checked</#if> name="ajax_re_province1_city" value="${b2.cityId}">
                                <span>${b2.cityName}</span>
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

    function ajaxCheckAllCity(obj){
        if($(obj).is(':checked')){
            $(obj).parent().find("input[name='ajax_re_province1_city']").prop("checked",true);
        }else{
            $(obj).parent().find("input[name='ajax_re_province1_city']").prop("checked",false);
        }
    }

    function ajaxCheckAll(obj){
        if($(obj).is(':checked')){
            $(obj).parent().find("input[name='ajax_re_province1']").prop("checked",true);
        }else{
            $(obj).parent().find("input[name='ajax_re_province1']").prop("checked",false);
        }
        showHotCity();
    }

    function showHotCity(){
        var provinceStr="";
        $(".ajax_province").each(function(){
            var resources="";
            $(this).find("td").eq(0).find("input[name='ajax_re_province1']:checked").each(function(){
                resources+=("hot_" + $(this).val()+",");
            });
            provinceStr+=resources;
        });
//        console.log(provinceStr);
        $(".ajax_province_city").each(function(){
            var currId = $(this).attr("id");
            if (provinceStr.indexOf(currId)>=0){
                $(this).show();
            }
            else{
                $(this).hide();
            }
        });
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

        $("input[name='ajax_re_province1']").change(function(){
            showHotCity();
        });

        $("#supportAreaType1").change();
        $("#supportAreaType2").change();
        showHotCity();

        $("#save").click(function(){
            var id = $("#ajax_id").val();
            var supportAreaType= $("input[name='supportAreaType']:checked").val();
            // 省
            var provinceStr="";
            $(".ajax_province").each(function(){
                var resources="";
                $(this).find("td").eq(0).find("input[name='ajax_re_province1']:checked").each(function(){
                    resources+=$(this).val()+",";
                });
                provinceStr+=resources;
            });
            // 市
            var provinceCityStr="";
            $(".ajax_province_city").each(function(){
                var resources="";
                var pId = $(this).attr("data-provinceId")
                $(this).find("td").eq(0).find("input[name='ajax_re_province1_city']:checked").each(function(){
                    resources+=$(this).val()+",";
                });
                if(resources != ""){
                    provinceCityStr+=(pId + ":" + resources + ";");
                }
            });
            $.messager.progress();
            $.ajax({
                url: '${rc.contextPath}/indexNavigation/editArea',
                type: 'post',
                dataType: 'json',
                data: {'id':id,'supportAreaType':supportAreaType,'provinceStr':provinceStr,'provinceCityStr':provinceCityStr},
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