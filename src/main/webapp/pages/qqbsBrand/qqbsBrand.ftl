<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"  />
<title>换吧网络-后台管理</title>
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
.searchName{
	padding-left:12px;
	text-align:left;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		 <div data-options="region:'north',title:'品牌管理',split:true" style="height: 120px;">
            <div style="height: 40px;padding: 10px">
            <p>
                <span>品牌馆ID:<input id="brandCategoryId" name="brandCategoryId" value="${brandCategoryId}" readOnly="true" width="50"></span>&nbsp;&nbsp;&nbsp;
                <span>品牌馆名称:<input id="brandCategoryName" name="brandCategoryName" value="${brandCategoryName}" readOnly="true" ></span>&nbsp;&nbsp;&nbsp;
            </p>
            </div>
        </div>
		 
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    	
    	  <!-- 新增品牌begin -->
		    <div id="brandDiv" class="easyui-dialog" style="width:600px;height:600px;padding:15px 20px;">
		        <form id="brandForm" method="post">
					<input id="id" type="hidden" name="id" value="" >
					<input id="brand_form_category_id" type="hidden" name="qqbsBrandCategoryId" value="" >
					<input id="brand_form_brand_id" type="hidden" name="brandId" value="" >
					<input id="brand_form_brand_name" type="hidden" name="name" value="" >
					<p>
						<span>品牌名称：</span>
						<span><input type="text" name="brand_name" id="brand_form_name" maxlength="100" style="width: 300px;"/></span>
						<span style="color: red">*</span>
					</p>
					<p>
						<span>品牌图片：</span>
						<span><input type="text" name="image" id="brand_form_image" readonly="readonly" style="width: 300px;"/></span>
						<span><a onclick="picDialogOpen('brand_form_image','oneImg')" class="easyui-linkbutton">上传图片</a></span>
						<span><img id="oneImg" src=""></span>
					</p>
					<p>
						<span>品牌排序：</span>
						<span><input type="text" name="order" id="brand_form_order" value="" maxlength="100" style="width: 300px;"/></span>
						<span style="color: red">*</span>
					</p>
					<p>
						<span class="searchName">组合ID：</span>
						<span><input type="text" name="activitiesCommonId" id="brand_form_activitiesCommonId" value="" maxlength="100" style="width: 300px;"/></span>
						<span style="color: red">*</span>
					</p>
					<p>
					 <span id="groupSaleName" style="color:red"></span>&nbsp;&nbsp;&nbsp;<span id="groupSaleRemark" style="color:red"></span><br/>
					</p>
					<p id="display">
						<span>展现状态：</span>
						<span><input type="radio" name="isDisplay"  value="1" checked="true"/>展现&nbsp;&nbsp;</span>
						<span><input type="radio" name="isDisplay"  value="0"/>不展现</span>
						<span style="color: red">*</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增品牌end -->
    	
		</div>
	</div>
</div>

<div id="picDia" class="easyui-dialog" closed="true" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
    	<input type="hidden" name="needWidth" id="needWidth" value="0">
    	<input type="hidden" name="needHeight" id="needHeight" value="0">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script>

	<!--加载品牌-->
	$("#brand_form_name").each(function(){
			$(this).combobox({
			url: '${rc.contextPath}/brand/jsonBrandCode?isAvailable=1&brandId=0',
			    valueField:'code',   
			    textField:'text',
			    onSelect: function(brand) {
				if (brand != '' && brand != null || brand != undefined) {
					$.post("${rc.contextPath}/product/ajaxBrandInfo", {
							id: brand.code
						},
						function(data) {
							if (data.status != 1) {
								$.messager.alert("提示", data.msg, "info");
								$("#brand_form_name").val("-1");
							}
						}, "json");
					}
				}
			});
	});
	
	var imageId = '';
	var imgSrc = '';
	function picDialogOpen(id, img) {
		imageId = id;
		imgSrc = img;
		$('picForm').form('reset');
		$('#picDia').dialog('open');
	}
	
	<!--图片上传-->
	function picUpload() {
	    $('#picForm').form('submit',{
	        url:"${rc.contextPath}/pic/fileUpLoad",
	        async:false,
	        success:function(data) {
	            var res = eval("("+data+")")
	            if(res.status == 1) {
	            	$.messager.alert('响应信息',"上传成功...",'info', function() {
	        			$("#picDia").dialog("close");
	                    $('#' + imageId).val(res.url);
	                    $('#' + imgSrc).attr('src', res.url);
	                    $('#' + imgSrc).show();
	        		});
	            } else{
	                $.messager.alert('响应信息',res.msg,'error',function(){
	                    return ;
	                });
	            }
	        }
	    })
	}
	
	<!--组合ID请求后台-->
	$('#brand_form_activitiesCommonId').change(function(){
		var id = $.trim($('#brand_form_activitiesCommonId').val());
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

	//编辑
	function editForm(index) {
		cleanbrandForm();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var brandId = arr.rows[index].brandId;
		var name = arr.rows[index].name;
		var image = arr.rows[index].imageUrl;
		var order = arr.rows[index].order;
		var activitiesCommonId = arr.rows[index].activitiesCommonId;
		var qqbsBrandCategoryId = arr.rows[index].qqbsBrandCategoryId;
		var isDisplay =  arr.rows[index].isDisplay;
		
		$("#oneImg").attr('src',image);
	    $("#oneImg").show();
		$('#id').val(id);
    	$('#brand_form_brand_id').val(brandId);
    	$('#brand_form_brand_name').val(name);
    	$('#brand_form_name').combobox('setValue',name);
    	$('#brand_form_image').val(image);
    	$('#brand_form_order').val(order);
    	$('#brand_form_activitiesCommonId').val(activitiesCommonId);
    	$('#brand_form_category_id').val(qqbsBrandCategoryId);
    	$("input[name='isDisplay']").removeAttr("checked");
    	
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==isDisplay){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#brandDiv').dialog('open');
	}

	<!--清除ChannelForm数据-->
	function cleanbrandForm(){
		$("#oneImg").removeAttr("src");
		$('#id').val('');
    	$('#brand_form_brand_id').val('');
    	$('#brand_form_brand_name').val('');
    	$('#brand_form_name').combobox('setValue','');
    	$('#brand_form_image').val('');
    	$('#brand_form_order').val('');
    	$('#brand_form_activitiesCommonId').val('');
    	$('#brand_form_category_id').val('');
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==1){
    			$(this).prop('checked',true);
    		}
    	});
	    $('#groupSaleName').text('');
        $('#groupSaleRemark').text('');
	}
	
	function display(id,isDisplay,order){
		var tip = "";
		if(isDisplay == 1){
			tip = "确定展现吗？";
		}else{
			tip = "确定不展现吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
		            $.messager.progress();
		            $.post(
		            		"${rc.contextPath}/qqbsBrand/updateBrandDisplay",
		            		{id:id,isDisplay:isDisplay},
		            		function(data){
		   	                $.messager.progress('close');
		   	                if(data.status == 1){
		   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
		   	                        $('#s_data').datagrid('reload');
		   	                        return;
		   	                    });
		   	                } else{
		   	                    $.messager.alert('响应信息',data.msg,'error');
		   	                }
		            });
	       		}
	        });
	}

	<!--加载Grid-->
	$(function(){
		var categoryId = $("#brandCategoryId").val();
		var param = {categoryId:categoryId};
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/qqbsBrand/jsonBrandInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            params:param,
            pageSize:50,
            pageList:[50],
            rowStyler:function(index,row){
    					return 'height:50px';
  			},
            columns:[[
            	{field:'id',title:'ID', width:50, align:'center'},
            	{field:'image',title:'图片预览', width:50, align:'center'},
                {field:'name',title:'品牌名称', width:50, align:'center'},
                {field:'order',title:'排序', width:50, align:'center'},
                {field:'activitiesCommonId',title:'对应组合', width:30, align:'center'},
                {field:'urlacId',title:'对应组合链接', width:50, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
	                 	var a = '<a href="javaScript:;" onclick="editForm(' + index + ')">编辑</a> | ';
	                 	var b = '<a target="_blank" href="${rc.contextPath}/sale/groupSaleProductManage/'+row.activitiesCommonId+'">管理品牌</a> | ';
	                	var c = '';
	                	if(row.isDisplay == 1){
		                	c = '<a href="javaScript:;" onclick="display(' + row.id + ',' + 0 +','+ row.order +')">设为不展现</a>';
	                	}else{
	                		c = '<a href="javaScript:;" onclick="display(' + row.id + ',' + 1 +','+ row.order +')">设为展现</a>';
	                	}
	                		return a + b + c;
               		}
               	}
            ]],
            toolbar:[{
                id:'_add',
                text:'新增栏目',
                iconCls:'icon-add',
                handler:function(){
                		cleanbrandForm();
                    	$('#brandDiv').dialog('open');
                	}
            	}],
            pagination:true
        });
        
        <!--新增品牌-->
	    $('#brandDiv').dialog({
	    	title:'新增品牌',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#brandForm').form('submit',{
	    				url: "${rc.contextPath}/qqbsBrand/saveOrUpdate",
	    				onSubmit:function(){
	    				   	var name = $("#brand_form_name").combobox('getText');
	    				   	$("#brand_form_brand_name").val(name);
	    				   	var brandId = $("#brand_form_name").combobox('getValue');
	    				   	//判断是否为数字
	    				   	if(!isNaN(brandId)){
	    				   		$("#brand_form_brand_id").val(brandId);
	    				   	}
	    					var image = $('#brand_form_image').val();
	    					var order = $('#brand_form_order').val();
	    					var activitiesCommonId = $("#brand_form_activitiesCommonId").val();
	    					var isDisplay = $("input[name='isDisplay']:checked").val();
	    					var qqbsBrandCategoryId = $("#brandCategoryId").val();
	    					$("#brand_form_category_id").val(qqbsBrandCategoryId);
	    					if($.trim(name)==''){
	    						$.messager.alert('提示','请填写品牌名称','info');
	    						return false;
	    					}else if($.trim(image)==''){
	    						$.messager.alert('提示','请上传图片','info');
	    						return false;
	    					}else if($.trim(order)==''){
								$.messager.alert('提示','请填写排序值','info');
	    						return false;					
	    					}else if($.trim(activitiesCommonId)==''){
	    						$.messager.alert('提示','请填写组合ID','info');
	    						return false;
	    					}else if($.trim(isDisplay)==''){
	    						$.messager.alert('提示','请选择展现状态','info');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#brandDiv').dialog('close');
	                                $('#s_data').datagrid('load');
	                            });
	                        } else if(res.status == 0){
	                            $.messager.alert('响应信息',res.msg,'error');
	                        } 
	    				},
	    				error: function(xhr){
				         	$.messager.progress('close');
				        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
				       }
	    			});
	    		}
	    	},
	    	{
	    		text:'取消',
	            align:'left',
	            iconCls:'icon-cancel',
	            handler:function(){
	                $('#brandDiv').dialog('close');
	            }
	    	}]
	    });	
	});
</script>

</body>
</html>