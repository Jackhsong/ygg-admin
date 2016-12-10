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
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>

</head>
<body class="easyui-layout">

<div data-options="region:'center',title:'添加Banner信息'" style="padding:5px;">
	
	<form id="savebanner"  method="post">
		<fieldset>
			<input type="hidden" value="${banner.id?c}" id="id" name="id" />
			<legend>Banner信息</legend>
			 着陆页类型:<input type="radio" name="type" id="type1" value="1" <#if banner.type?exists && (banner.type == 1 || banner.type == 4 || banner.type == 7 || banner.type == 8) >checked<#else>checked</#if>> 单品&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type"  id="type2" value="2" <#if banner.type?exists && (banner.type == 2) >checked</#if>> 组合&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type"  id="type3" value="3" <#if banner.type?exists && (banner.type == 3) >checked</#if>> 自定义活动&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type"  id="type5" value="5" <#if banner.type?exists && (banner.type == 5) >checked</#if>> 原生自定义页面&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type"  id="type6" value="6" <#if banner.type?exists && (banner.type == 6) >checked</#if>> 图片点击不跳转<br/><br/>
    		 Banner排序值: <input type="text" id="sequence" name="sequence" style="width:300px;"  value="<#if banner.sequence?exists>${banner.sequence?c}</#if>" /><br/><br/>
    		 Banner描述: <input type="text" maxlength="20" id="desc" name="desc" style="width:300px;"  value="<#if banner.desc?exists>${banner.desc}</#if>" /><br/><br/>
			 Banner图片: <input type="text" id="image" name="image" style="width:300px;" value="<#if banner.image?exists>${banner.image}</#if>" />
            <a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><span style="color:red">必须（640x260）</span><br/>
			<span id="imageShow">
			<#if banner.image?exists>
                <img alt="" src="${banner.image}" style="min-width: 100px;">
			</#if>
            </span><br/><br/>
			
			<!-- 特卖类型为单品时 -->
			<div id="product_div">
			 商品ID: <input type="number" id="productId" name="productId" style="width:300px;"  onblur="stringTrim(this);" value="<#if banner.displayId?exists && (banner.displayId != 0) && (banner.type == 1 || banner.type == 4 || banner.type == 7 || banner.type == 8)>${banner.displayId?c}</#if>" />
			 <span id="productName" style="color:red"></span><br/><br/>
			 </div>
			
			 <!-- 特卖类型为组合特卖时 -->
			<div id="groupSale_div">
			 组合ID:
			 <input id="groupSaleId" type="number" name="groupSaleId" style="width:200px" onblur="stringTrim(this);" value="<#if banner.displayId?exists && (banner.displayId != 0) && (banner.type == 2)>${banner.displayId?c}</#if>" />
			 <span id="groupSaleName" style="color:red"></span>&nbsp;&nbsp;&nbsp;<span id="groupSaleRemark" style="color:red"></span><br/><br/>
			 </div>
			
			 <!-- 特卖类型为自定义活动 -->
			<div id="customSale_div">
			 自定义活动:
			 <input id="customSaleId" type="number" name="customSaleId" style="width:200px" onblur="stringTrim(this);" value="<#if banner.displayId?exists && (banner.displayId != 0) && (banner.type == 3)>${banner.displayId?c}</#if>" />
			 <span id="customSaleName" style="color:red"></span>&nbsp;&nbsp;&nbsp;<span id="customSaleRemark" style="color:red"></span><br/><br/>
			 </div>
			 
			  <!-- 特卖类型为自定义页面时 -->
			<div id="customPage_div">
			 原生自定义页面:<input type="text" id="customPageId" name="customPageId" style="width:300px"/>
			 </div> 
			
			 展现状态:
			 <input type="radio" <#if banner.isDisplay?exists && (banner.isDisplay == 1) >checked</#if> name="isDisplay" value="1">展现&nbsp;&nbsp;&nbsp;
			 <input type="radio" <#if banner.isDisplay?exists && (banner.isDisplay == 0) >checked</#if> name="isDisplay" value="0"> 不展现<br/><br/>

			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <input type="hidden" name="needWidth" id="needWidth" value="640">
        <input type="hidden" name="needHeight" id="needHeight" value="260">
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script type="text/javascript">

	function stringTrim(obj){
		$(obj).val($.trim($(obj).val()));	
	}
	
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
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
                            $("#imageShow").html("<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
                            $("#picFile").val("");
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
	
	$(function(){
		$('#type1').change(function(){
			if($('#type1').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').hide();
				$('#customPage_div').hide();
				$('#product_div').show();
			}
		});
		$('#type2').change(function(){
			if($('#type2').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').show();
				$('#customPage_div').hide();
				$('#product_div').hide();
			}
		});
		$('#type3').change(function(){
			if($('#type3').is(':checked')){
				$('#customSale_div').show();
				$('#groupSale_div').hide();
				$('#customPage_div').hide();
				$('#product_div').hide();
			}
		});
		$('#type5').change(function(){
			if($('#type5').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').hide();
				$('#customPage_div').show();
				$('#product_div').hide();
			}
		});
		$('#type6').change(function(){
			if($('#type6').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').hide();
				$('#customPage_div').hide();
				$('#product_div').hide();
			}
		});
		
		$('#type1').change();
		$('#type2').change();
		$('#type3').change();
		$('#type5').change();
		$('#type6').change();

        $('#saleTimeType_10').change(function(){
            if($('#saleTimeType_10').is(':checked')){
                $("#startTime").val("");
                $("#endTime").val("");
            }
        });

        $('#saleTimeType_20').change(function(){
            if($('#saleTimeType_20').is(':checked')){
                $("#startTime").val("");
                $("#endTime").val("");
            }
        });

		$("#saveButton").click(function(){
			$('#savebanner').form('submit', {   
			    url:'${rc.contextPath}/member/saveOrUpdateMemberBanner',   
			    onSubmit: function(){   
			    	var type = $("input[name='type']:checked").val();
					var productId = $("#productId").val();
					var groupSaleId = $.trim($("#groupSaleId").val());
					var customSaleId = $("#customSaleId").val();
					var customPageId = $("#customPageId").combobox('getValue');
					if(type == null || type == '' || type == undefined){
						$.messager.alert('提示','请选择着陆页类型','info'); 
						return false;
					}else if(type == 1 && $.trim(productId) == ''){
						$.messager.alert('提示','请填写商品ID','info'); 
						return false;
					}else if(type == 2 && $.trim(groupSaleId) == ""){
						$.messager.alert('提示','请填写组合特卖ID','info');
						return false;
					}else if(type == 3 && $.trim(customSaleId == '')){
						$.messager.alert('提示','请选择自定义活动','info');
						return false;
					}else if(type == 5 && (customPageId == '' || customPageId == null || customPageId == undefined)){
						$.messager.alert('提示','请选择自定义页面','info');
						return false;
					}
					$.messager.progress();
			    },   
			    success:function(data){   
			    	var res = eval("("+data+")")
                    if(res.status == 1){
                        $.messager.alert('响应信息',"保存成功",'info',function(){
                        	window.location.href = '${rc.contextPath}/member/bannerList';
                        });
                    }else{
                        $.messager.alert('响应信息',res.msg,'error',function(){
                        	$.messager.progress('close');
                        });
                    }   
			    }   
			});
    	});
		
		$('#productId').change(function(){
			var id = $('#productId').val();
			if(id == ""){
				$('#productName').text("");
			}else{
				$('#productName').text("");
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#productName').text(data.msg);
	                    }else{
	                    	$.messager.alert("提示",data.msg,"info");
	                    }
	                },
	                error: function(xhr){
	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                }
	            });
			}
		});
    	
    	//$('#groupSale').combobox({
		//    url:'${rc.contextPath}/banner/jsonAcCommonCode?id=<#if id??>${id?c}<#else>${0}</#if>',   
		//    valueField:'product',   
		//    textField:'text'  
		//});
    	
    	$('#groupSaleId').change(function(){
			var id = $.trim($('#groupSaleId').val());
			$('#groupSaleName').text("");
			$('#groupSaleRemark').text("");
			if(id != ""){
				$.ajax({
	                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#groupSaleName').text("组合名称："+data.name);
	            			$('#groupSaleRemark').text("组合备注："+data.remark);
	                    }else{
	                    	$.messager.alert("提示",data.msg,"info");
	                    }
	                },
	                error: function(xhr){
	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                }
	            });
			}
		});
    	$('#groupSaleId').change();
    	
    	$('#customSaleId').change(function(){
			var id = $.trim($('#customSaleId').val());
			$('#customSaleName').text("");
			$('#customSaleRemark').text("");
			if(id != ""){
				$.ajax({
	                url: '${rc.contextPath}/customActivities/findCustomActivitiesInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#customSaleName').text("活动名称："+data.name);
	            			$('#customSaleRemark').text("活动备注："+data.remark);
	                    }else{
	                    	$.messager.alert("提示",data.msg,"info");
	                    }
	                },
	                error: function(xhr){
	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                }
	            });
			}
		});
    	
        
        $("#customPageId").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage',
            valueField:'code',
            textField:'text'
        });
	});
</script>

</body>
</html>