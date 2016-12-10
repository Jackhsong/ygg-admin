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
<style type="text/css">
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'国旗管理',split:true" style="height: 80px;">
			<div style="height: 120px;padding: 15px">
	            <table class="search">
	                <tr>
	                    <td class="searchName">ID：</td>
	                    <td class="searchText"><input id="searchId" name="searchId" value="" /></td>
	                    <td class="searchName">国家名称：</td>
	                	<td class="searchText"><input id="searchName" name="searchName" value=""/></td>
	                    <td class="searchName">是否可用：</td>
	                	<td class="searchText">
	                		<select id="searchAvailable" name="searchAvailable">
	                			<option value="-1">全部</option>
	                			<option value="1">可用</option>
	                			<option value="0">停用</option>
	                		</select>
	                	</td>
	                	<td class="searchText">
							<a id="searchBtn" onclick="searchFlag()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;
							<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
							&nbsp;
	                	</td>
	                </tr>
	            </table>
			</div>
		</div> 
		
		<div data-options="region:'center'" >
			<!--数据表格-->
			<table id="s_data" ></table>
			
			<!-- 增加商品 begin -->
			<div id="editFlagDiv" style="width:450px;height:300px;padding:10px 10px;">
	        	<form id="editFlagForm" method="post" >
	        		<input type="hidden" id="flagId" name="id"  value=""/>
	            	<table>
		                <tr>
		                	<td>国家名称：</td>
		                	<td><input id="flagName" name="name" value="" maxlength="10"/></td>
		                </tr>
                        <tr>
                            <td>国家英文名：</td>
                            <td><input id="flagEnName" name="flagEnName" value="" maxlength="30"/></td>
                        </tr>
		                <tr>
		                    <td>国旗图标：</td>
		                    <td>
		                    	<input id="flagImage" name="image" value="" />&nbsp;
		                    	<a id="searchBtn" onclick="picDialogOpen('flagImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
		                    	尺寸48x48
		                    </td>
		                </tr>
		                <tr>
		                	<td></td>
		                	<td id="showImage"></td>
		                </tr>
		                <tr>
			                <td>可用状态：</td>
			                <td>
			                	<input type="radio"  name="isAvailable"  id="isAvailable_1" value="1"/> 可用&nbsp;&nbsp;
								<input type="radio"  name="isAvailable"  id="isAvailable_0" value="0"/> 不可用
			                </td>
			            </tr>
	            	</table>
	        	</form>
			</div>
			
			<!-- 上传图片 -->
			<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
			    <form id="picForm" method="post" enctype="multipart/form-data">
			        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
			        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
			    </form>
			    <br/>
			    <br/>
			</div>			
				 	    		
		</div>
	</div>
</div>

<script>

	$(function(){
	    $('#picDia').dialog({
	        title:'又拍图片上传窗口',
	        collapsible:true,
	        closed:true,
	        modal:true
	    })
	});
	
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
	                        $("#showImage").html('<img alt="" src="'+res.url+'" style="max-width:100px"/>');
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
	    });
	}
	
	function searchFlag(){
		$('#s_data').datagrid('load',{
			id:$("#searchId").val(),
			name:$("#searchName").val(),
			isAvailable:$("#searchAvailable").val()
		});
	}

	function clearSearch(){
		$("#searchId").val('');
		$("#searchName").val('');
		$("#searchAvailable").find('option').eq(0).attr('selected','selected');
		$('#s_data').datagrid('load',{});
	}

	function clearEditFlagForm(){
		$("#flagId").val('');
		$("#flagName").val('');
		$("#flagEnName").val('');
		$("#flagImage").val('');
		$("#editFlagForm input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		$("#showImage").html('');
		
	}
	
	function editFlag(index){
		var arr=$("#s_data").datagrid("getData");
     	clearEditFlagForm();
   		$("#flagId").val(arr.rows[index].id);
   		$("#flagName").val(arr.rows[index].name);
   		$("#flagEnName").val(arr.rows[index].flagEnName);
   		$("#flagImage").val(arr.rows[index].imageURL);
   		$("#showImage").html(arr.rows[index].image);
   		if(arr.rows[index].isAvailableCode==1){
   			$("#isAvailable_1").prop('checked',true);
   		}else{
   			$("#isAvailable_0").prop('checked',true);
   		}
   		$('#editFlagDiv').dialog('open');

	}
	function displayId(id,isAvailable){
		var tip = "";
		if(isAvailable == 0){
			tip = "确定停用吗？";
		}else{
			tip = "确定启用吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
   	            $.messager.progress();
   	            $.post(
   	            		"${rc.contextPath}/flag/updateFlagStatus",
   	            		{id:id,isAvailable:isAvailable},
   	            		function(data){
		   	                $.messager.progress('close');
		   	                if(data.status == 1){
		   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
		   	                        $('#s_data').datagrid('load');
		   	                        return
		   	                    });
		   	                } else{
		   	                    $.messager.alert('响应信息',data.msg,'error');
		   	                }
   	            		});
	        }
	    });
	}
	
	
	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/flag/jsonSaleFlagInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:15, align:'center'},
            	{field:'name',    title:'国家', width:80, align:'center'},
                {field:'flagEnName',    title:'英文名', width:80, align:'center'},
                {field:'image',    title:'国旗', width:80, align:'center'},
                {field:'isAvailable',    title:'状态', width:50, align:'center'},
                {field:'hidden',  title:'操作', width:60,align:'center',
                    formatter:function(value,row,index){
                   		var a = '<a href="javaScript:;" onclick="editFlag(' + index + ')">编辑</a>';
                   		var b = '';
                       	if(row.isAvailableCode == 1){
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 0 + ')">停用</a>'
                       	}else{
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 1 + ')">启用</a>'
                       	}
                       	return a + b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                	clearEditFlagForm();
                	$("#isAvailable_1").prop('checked',true);
                	$('#editFlagDiv').dialog('open');
                }
            }],
            pagination:true
        });
		
    
	    $('#editFlagDiv').dialog({
	    	title:'国旗图标',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editFlagForm').form('submit',{
	    				url: "${rc.contextPath}/flag/saveOrUpdateFlag",
	    				onSubmit:function(){
	    					var name = $("#editFlagForm input[name='name']").val();
	    					var enName = $("#flagEnName").val();
	    					var image = $("#editFlagForm input[name='image']").val();
	    					if($.trim(name) == ''){
	    						$.messager.alert("提示","请输入国家名称","warn");
	    						return false;
	    					}else if($.trim(enName) == ''){
	    						$.messager.alert("提示","请输入国家英文名","warn");
								return false;
							}
							else if($.trim(image) == ''){
	    						$.messager.alert("提示","请上传国旗图标","warn");
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#s_data').datagrid('reload');
	                                $('#editFlagDiv').dialog('close');
	                            });
	                        } else if(res.status == 0){
	                            $.messager.alert('响应信息',res.msg,'info');
	                        } 
	    				}
	    			});
	    		}
	    	},
	    	{
	    		text:'取消',
	            align:'left',
	            iconCls:'icon-cancel',
	            handler:function(){
	                $('#editFlagDiv').dialog('close');
	            }
	    	}]
	    });
	});
</script>

</body>
</html>