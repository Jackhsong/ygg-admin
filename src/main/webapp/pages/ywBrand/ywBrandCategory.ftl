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
	padding-left:10px;
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
		<div data-options="region:'north',title:'品牌馆栏目管理',split:true" style="height: 100px;">
			<div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/channelProManage/exportChannelPro" method="post">
		            <table class="search">
		            	<tr>
		            		<td>品牌馆栏目管理</td>
		                </tr>
		            </table>
		        </form>
		    </div>
		 </div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
	    
	      <!-- 新增栏目 Begin -->
	    <div id="brandCategoryDiv" class="easyui-dialog" style="width:500px;height:200px;padding:15px 20px;">
	        <form id="brandCategoryForm" method="post">
				<input id="form_brandCategory_id" type="hidden" name="id" >
				<input id="form_brandCategory_isDisplay" type="hidden" name="isDisplay" >
				<p>
					<span>栏目名称：</span>
					<span><input type="text" id="form_brandCategory_name" name="name" maxlength="8" style="width: 300px;"/></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>栏目排序：</span>
					<span><input type="text" id="form_brandCategory_order" name="order" maxlength="50" style="width: 300px;"/></span>
					<font color="red">*</font>
				</p>
	    	</form>
	      </div>
    	<!-- 新增栏目 End  -->
		</div>
	</div>
</div>

<script>
	
	<!--编辑数据-->
	function editForm(index){
		cleanbrandCategoryForm();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var name = arr.rows[index].name;
		var order = arr.rows[index].order;
		var isDisplay = arr.rows[index].isDisplay;
		
		$('#form_brandCategory_id').val(id);
		$('#form_brandCategory_isDisplay').val(isDisplay);
    	$('#form_brandCategory_name').val(name);
    	$('#form_brandCategory_order').val(order);
    	$('#brandCategoryDiv').dialog('open');
	}

	<!--清除ChannelForm数据-->
	function cleanbrandCategoryForm(){
		$('#form_brandCategory_id').val('');
		$("#form_brandCategory_name").val('');
		$("#form_brandCategory_order").val('');
		$("#form_brandCategory_isDisplay").val('');
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
		            		"${rc.contextPath}/ywBrandCategory/updateCategoryDisplay",
		            		{id:id,isDisplay:isDisplay,order:order},
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
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/ywBrandCategory/jsonBrandCategoryInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50],
            columns:[[
            	{field:'id',title:'ID', width:50, align:'center'},
                {field:'name',title:'栏目名称', width:50, align:'center'},
                {field:'order',title:'排序', width:50, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
	                 	var a = '<a href="javaScript:;" onclick="editForm(' + index + ')">编辑</a> | ';
	                	var b = '<a target="_blank" href="${rc.contextPath}/ywBrand/brandManage/'+row.id+'">管理品牌</a> | ';
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
                		cleanbrandCategoryForm();
                    	$('#brandCategoryDiv').dialog('open');
                	}
            	}],
            pagination:true
        });
        
        
        <!--新增栏目-->
	    $('#brandCategoryDiv').dialog({
	    	title:'新增栏目',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#brandCategoryForm').form('submit',{
	    				url: "${rc.contextPath}/ywBrandCategory/saveOrUpdate",
	    				onSubmit:function(){
	    					var name = $('#form_brandCategory_name').val();
	    					var order = $('#form_brandCategory_order').val();
	    					var id = $("#form_brandCategory_name").val();
	    					//新增默认为展现
	    					 $("#form_brandCategory_isDisplay").val(1);
	    					if($.trim(name)==''){
	    						$.messager.alert('提示','请填写品牌名称','info');
	    						return false;
	    					}else if($.trim(order)==''){
	    						$.messager.alert('提示','请填写序号','info');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#brandCategoryDiv').dialog('close');
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
	                $('#brandCategoryDiv').dialog('close');
	            }
	    	}]
	    });	
	});
</script>

</body>
</html>