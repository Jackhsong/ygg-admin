<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>

</head>
<body class="easyui-layout">

<div data-options="region:'center',title:'添加商品信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="saveBrand" action="${rc.contextPath}/brand/save"  method="post">
		<fieldset>
			<input type="hidden" value="<#if brand.id?exists && (brand.id != 0)>${brand.id?c}</#if>" id="editId" name="editId" />
			<legend>品牌信息</legend>
    		 品牌名称: <input type="text" maxlength="20" id="name" name="name" style="width:300px;"  value="<#if brand.name?exists>${brand.name}</#if>" /><br/><br/>
    		 品牌英文名称: <input type="text" maxlength="30" id="enName" name="enName" style="width:300px;"  value="<#if brand.enName?exists>${brand.enName}</#if>" /><br/><br/>
			 品牌logo: <input type="text" id="image" name="image" style="width:300px;" value="<#if brand.image?exists>${brand.image}</#if>" />
            <span style="color:red">固定大小：122x122</span>
				<a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
                <p></p>
				<span id="imageShow">
		    		<#if brand.image?exists>
		    			<img alt="" src="${brand.image}" style="min-width: 100px;">
		    		</#if>
    			</span>
				<br/><br/>
            品牌广告图:  <input type="text" id="adImage" name="adImage" style="width:300px;" value="<#if brand.adImage?exists>${brand.adImage}</#if>" />
                <span style="color:red">固定大小：750x358 或 640x306</span>
                <a onclick="picDialogOpen('adImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
                <p></p>
                <span id="adImageShow">
                    <#if brand.adImage?exists>
                        <img alt="" src="${brand.adImage}" style="max-width: 750px;">
                    </#if>
                </span>
                <br/><br/>
			品牌卖点: <input type="text" maxlength="250" id="hotSpots" name="hotSpots" class="easyui-textbox" data-options="multiline:true" style="width:250px;height:80px" value="<#if brand.hotSpots?exists>${brand.hotSpots}</#if>" /><br/><br/>
           	品牌介绍: <input type="text" maxlength="300" id="introduce" name="introduce" class="easyui-textbox" data-options="multiline:true" style="width:300px;height:100px"  value="<#if brand.introduce?exists>${brand.introduce}</#if>" /><br/><br/>
           	品牌国家信息：<input id="stateId" name="stateId" value=""/><span style="color:red">&nbsp;必须</span><br/><br/>
           	
           	<div id="categoryFirstId_div">
				<span>品牌所属类目:</span>
                    <#list brandCategoryList as brandCategory>
                        <select name="categoryFirstIdList" class="selectStyle">
                            <option value="0">-请选择一级分类-</option>
                            <#list brandCategory.catetgoryFirstList as categoryFrist>
                                <option value="${categoryFrist.id?c}" <#if brandCategory.cateId == categoryFrist.id>selected</#if>>${categoryFrist.name}</option>
                            </#list>
                        </select>
                    </#list>
				 <span style="color:red">必须</span>
                <button id="copyId" class="btn btn-primary btn-small" onclick="return appendCategoryFirst(this)">添加分类</button>
                <button id="deleteId" class="btn btn-danger btn-small" onclick="return removeCategoryFirst(this)">删除分类</button>
			 </div>
			 <br/>
           	合伙人返分销毛利:
            <input type="radio" name="returnDistributionProportionType" value="1" <#if brand.returnDistributionProportionType==1 >checked</#if>>返25%&nbsp;&nbsp;&nbsp;
            <input type="radio" name="returnDistributionProportionType" value="2" <#if brand.returnDistributionProportionType==2 >checked</#if>>返100%<br/><br/>
            <p>
                <span>关联类型<span style="color: red">(可选)</span>：</span>
                <input type="radio" name="activityType" value="1" <#if brand.activityType?exists && (brand.activityType == 1) >checked</#if> />组合&nbsp;&nbsp;
                <input type="radio" name="activityType" value="2" <#if brand.activityType?exists && (brand.activityType == 2) >checked</#if> />网页自定义活动&nbsp;&nbsp;
                <input type="radio" name="activityType" value="3" <#if brand.activityType?exists && (brand.activityType == 3) >checked</#if> />原生自定义活动&nbsp;&nbsp;
            </p>
            <p id="activitiesCommon">
                <span>组合ID：</span>
                <span><input type="text" name="activitiesCommonId" id="activitiesCommonId" value="<#if brand.activityDisplayId?exists &&(brand.activityDisplayId !=0) >${brand.activityDisplayId?c}</#if>" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="10" style="width: 200px;"/></span>
                <span style="color: red" id="activitiesCommonDesc"></span>
            </p>
            <p id="activitiesCustom">
                <span>自定义活动：</span>
                <span><input type="text" name="activitiesCustomId" id="activitiesCustomId" style="width: 200px;"/></span>
            </p>
            <p id="page">
                <span>原生自定义活动：</span>
                <span><input type="text" name="pageId" id="pageId" style="width: 200px;"/></span>
            </p>
            活动展现状态:
            <input type="radio" name="isShowActivity" value="1" <#if brand.isShowActivity?exists && (brand.isShowActivity == 1) >checked</#if> /> 展现&nbsp;&nbsp;&nbsp;
            <input type="radio" name="isShowActivity" value="0" <#if brand.isShowActivity?exists && (brand.isShowActivity == 0) >checked</#if> /> 不展现<br/><br/>
			 品牌状态:
			 <input type="radio" name="isAvailable" value="1" <#if brand.isAvailable?exists && (brand.isAvailable == 1) >checked</#if> /> 可用&nbsp;&nbsp;&nbsp;
			 <input type="radio" name="isAvailable" value="0" <#if brand.isAvailable?exists && (brand.isAvailable == 0) >checked</#if> /> 停用<br/><br/>
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>


	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="heightList" id="heightList" value="">
        <input type="hidden" name="widthList" id="widthList" value="">
        <input type="hidden" name="needWidth" id="needWidth" value="">
        <input type="hidden" name="needHeight" id="needHeight" value="">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script type="text/javascript">
    function appendCategoryFirst(obj) {
        var row = $(obj).parent().find("select").first().clone(true);
        $(row).find("option").first().attr("selected",true);
        $(obj).parent().find("select").last().after(row);
        return false;
    }

    function removeCategoryFirst(obj){
        if($(obj).parent().find("select").size() <=1 ) return false;
        $(obj).parent().find("select").last().remove();
        return false;
    }

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
        if($inputId == 'image'){
            $("#needWidth").val('122'); //122
            $("#needHeight").val('122'); //122
            $("#widthList").val(''); //122
            $("#heightList").val(''); //122
        }else if($inputId == 'adImage') {
            $("#needWidth").val(''); //
            $("#needHeight").val(''); //
            $("#heightList").val('358,306');
            $("#widthList").val('750,640'); //
        }
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
                            $("#picFile").val("");
                            if(inputId == "adImage"){
                                $("#adImageShow").html("<img alt='' src='"+res.url+"' style='max-width: 250px;'/>");
                            }else{
                                $("#imageShow").html("<img alt='' src='"+res.url+"' style='max-width: 350px;'/>");
                            }
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

<script type="text/javascript">
    // 清空dialog div
    function cleanEditCustomNavigationDiv(){
        $('input[name=activityType]').each(function(i) {
            $(this).attr('checked', false);
        });
        $('#activitiesCommon').hide();
        $('#activitiesCustom').hide();
        $('#page').hide();
    }
    // 验证表单是否是否有效
    function valid() {
        if(typeof($('input[name=activityType]:checked').val()) == 'undefined') {
            $.messager.alert('警告','关联类型不能为空','warning');
            return false;
        }
        if($('input[name=activityType]:checked').val() == 1) {
            if($('#activitiesCommonId').val() == '' || $('#activitiesCommonDesc').text() == 'undefined-undefined') {
                $.messager.alert('警告','组合ID为空或不存在','warning');
                return false;
            }
        } else if($('input[name=activityType]:checked').val() == 2) {
            if($('#activitiesCustomId').combobox('getValue') == '') {
                $.messager.alert('警告','自定义活动不能为空','warning');
                return false;
            }
        } else if($('input[name=activityType]:checked').val() == 3) {
            if($('#pageId').combobox('getValue') == '') {
                $.messager.alert('警告','原生自定义活动不能为空','warning');
                return false;
            }
        }
        return true;
    }
	$(function(){
	
		$("#stateId").combobox({
			url:'${rc.contextPath}/flag/jsonSaleFlagCode?id=${stateId}',   
		    valueField:'code',   
		    textField:'text'  
		});
        $('#activitiesCommonId').blur(function() {
            if($('#activitiesCommonId').val().length < 1)
                return;
            $.ajax({
                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
                type: 'POST',
                data: {id:$('#activitiesCommonId').val()},
                success: function(data){
                    if(data.status == 1){
                        $('#activitiesCommonDesc').text(data.name + "-" + data.remark);
                    }else{
                        $.messager.alert("提示",data.msg,"info");
                    }
                }
            });
        });
        $("#pageId").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage?id=<#if brand.activityDisplayId??>${brand.activityDisplayId?c}<#else>${0}</#if>',
            editable : false,
            valueField:'code',
            textField:'text',
        });
        $("#activitiesCustomId").combobox({
            url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id=<#if brand.activityDisplayId??>${brand.activityDisplayId?c}<#else>${0}</#if>',
            editable : false,
            valueField:'code',
            textField:'text',
        });
		$("#saveButton").click(function(){
			var name = $.trim($("#name").val());
            var enName = $.trim($("#enName").val());
			var editId = $.trim($('#editId').val());
            var type = $("input[name='isShowActivity']:checked").val();
            if(type == 1)
            {
                var reV = valid();
                if(!reV)
                {
                    return;
                }
            }
//          var categoryFirstId = true;
//			$("select[name='categoryFirstId']").each(function(){
//				if($(this).val()==0){
//					categoryFirstId = false;
//				}
//			});

            var categoryFirstIdList = [];
            $.each($(".selectStyle  option:selected"), function(index,data){
                if(data.value > 0) {
                    categoryFirstIdList.push(data.value);
                }
            });
            console.log(categoryFirstIdList);

            if(categoryFirstIdList.length == 0){
                $.messager.alert('提示','请填写品牌属类目','warning');
	        	return;
            }
	    	if($.trim($('#stateId').combobox('getValue')) == '') {
	            $.messager.alert('警告','品牌国家信息不能为空','warning');
	            return;
	        }
//	        if(!categoryFirstId){
//	        	$.messager.alert('提示','请填写品牌属类目','warning');
//	        	return;
//	        }
            if(name == ""){
	    		$.messager.alert("提示","名称必填","info");
                return;
	    	}
            if(enName != "" && enName.length > 30){
	    		$.messager.alert("提示","英文名称长度不超过30","info");
                return ;
            }
	    	else
            {
	    		$.post("${rc.contextPath}/brand/checkName",
   					{name:name,editId:editId},
              		function(data){
                    	if(data.status == 1){
                        	$('#saveBrand').submit();
	    					$.messager.progress();
                       	}else{
                       		$.messager.alert("提示",data.msg,"info");
                       	}
                	},
				"json");
	    	}
    	});
    	// 选择类型展示不同的信息
        $('input[name=activityType]').change(function() {
            var type = $('input[name=activityType]:checked').val();
            if(type == 1) {
                $('#activitiesCommon').show();
                $('#activitiesCustom').hide();
                $('#page').hide();
            } else if(type == 2) {
                $('#activitiesCommon').hide();
                $('#activitiesCustom').show();
                $('#page').hide();
            } else if(type == 3) {
                $('#activitiesCommon').hide();
                $('#activitiesCustom').hide();
                $('#page').show();
            }
        });
        $('#activitiesCommon').hide();
        $('#activitiesCustom').hide();
        $('#page').hide();
        $('input[name=activityType]').change();
	})
</script>

</body>
</html>