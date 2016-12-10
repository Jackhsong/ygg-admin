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
textarea{
	resize:none;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">

	<div id="cc" class="easyui-layout" data-options="fit:true" >
	
		<div data-options="region:'north',title:'条件筛选',split:true" style="height: 120px;">
			<div style="height: 60px;padding: 10px">
				<span>马甲描述：</span>
				<span><input id="searchDesc" name="searchDesc"/></span>
				<span>马甲状态：</span>
				<span>
					<select id="searchValue" name="searchValue">
	          			<option value="-1">全部</option>
	          			<option value="1">开启</option>
	          			<option value="0">关闭</option>
	           		</select>
				</span>
				<span>自定义板块展示状态：</span>
				<span>
					<select id="searchValue1" name="searchValue1">
	          			<option value="-1">全部</option>
	          			<option value="1">是</option>
	          			<option value="0">否</option>
	           		</select>
				</span>
				<span>
					<a id="searchBtn" onclick="searchVest()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
					<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>&nbsp;
				</span>			
			</div>			
		</div>
		
		<div data-options="region:'center'" >
	    	<!--数据表格-->
	    	<table id="s_data" style=""></table>
    	</div>
    	
		<!-- dialog begin -->
		<div id="editVestAppDiv" class="easyui-dialog" style="width:500px;height:200px;padding:20px 20px;">
			<form action="" id="vestAppForm" method="post">
				<input id="vestAppForm_id" type="hidden" name="id" value=""/>
				<input id="vestAppForm_value" type="hidden" name="value" value=""/>
				<p>
					<span>马甲描述：</span>
					<span><input type="text" id="vestAppForm_desc" name="desc" value="" maxlength="100" style="width:330px;"/></span>
					<font color="red">必填</font>
				</p>
				<p>
					<span>马甲key：</span>
					<span><input type="text" id="vestAppForm_key" name="key" value="" maxlength="30" style="width:330px;"/></span>
				</p>
			</form>
		</div>
		<!-- dialog end -->
		
	</div>
	
</div>

<script type="text/javascript">

$(function(){
	$(document).keydown(function(e){
		if (!e){
		   e = window.event;  
		}  
		if ((e.keyCode || e.which) == 13) {  
		      $("#searchBtn").click();  
		}
	});
});

function checkEnter(e){
	var et=e||window.event;
	var keycode=et.charCode||et.keyCode;
	if(keycode==13){
		if(window.event)
			window.event.returnValue = false;
		else
			e.preventDefault();//for firefox
	}
}

function searchVest(){
	$("#s_data").datagrid('load',{
		desc:$("#searchDesc").val(),
		value:$("#searchValue").val(),
		value1:$("#searchValue1").val()
	});
}

function clearSearch(){
	$("#searchDesc").val('');
	$("#searchValue").find('option').eq(0).attr('selected','selected');
	$("#searchValue1").find('option').eq(0).attr('selected','selected');
	$("#s_data").datagrid('load',{});
}


function cleanVestAppDiv(){
	$("#vestAppForm_id").val('');
	$("#vestAppForm_desc").val('');
	$("#vestAppForm_key").val('');
	$("#vestAppForm_value").val('');
}

function editIt(index){
	cleanVestAppDiv();
    var arr=$("#s_data").datagrid("getData");
    $("#vestAppForm_id").val(arr.rows[index].id);
    $("#vestAppForm_desc").val(arr.rows[index].desc);
    $("#vestAppForm_key").val(arr.rows[index].key);
    $("#vestAppForm_value").val(arr.rows[index].value);
    $("#editVestAppDiv").dialog('open');
}

function updateVestAppStatus(id, value){
	var tip = "";
	if(value == 0){
		tip = "确定关闭吗？";
	}else{
		tip = "确定开启吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/index/updateVestAppStatus",
	            		{id:id,value:value},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                    	$('#s_data').datagrid('clearSelections');
	   	                        $('#s_data').datagrid('reload');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	           });
        }
    });
}
function updateVestAppCustomLayoutDisplayStatus(id, value){
	var tip = "";
	if(value == 0){
		tip = "确定不展现自定义板块吗？";
	}else{
		tip = "确定展现自定义板块吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/index/updateVestAppCustomLayoutDisplayStatus",
	            		{id:id,value:value},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                    	$('#s_data').datagrid('clearSelections');
	   	                        $('#s_data').datagrid('reload');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	           });
        }
    });
}


function deleteId(id){
	$.messager.confirm("提示信息","确定删除吗？",function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/index/deleteVestApp",
	            		{id:id},
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
        url:'${rc.contextPath}/index/jsonVestAppInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        columns: [
            [
                {field: 'id', title: 'ID', width: 20, align: 'center'},
                {field: 'desc', title: '马甲App名称', width: 80, align: 'center'},
                {field: 'valueStr', title: '当前状态', width: 50, align: 'center'},
                {field: 'value1Str', title: '是否展示自定义版块', width: 50, align: 'center'},
                {field: 'hidden', title: '操作', width: 60, align: 'center',
                    formatter: function (value, row, index) {
                    	/* var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';*/
                    	var b = '';
                    	var c = '';
                    	if(row.value == 1){
                    		b = '<a href="javaScript:;" onclick="updateVestAppStatus(' + row.id + ',' + 0 + ')">马上关闭</a> | ';
                    	}else{
                    		b = '<a href="javaScript:;" onclick="updateVestAppStatus(' + row.id + ',' + 1 + ')">马上开启</a> | ';
                    	}
                    	if(row.value1 == 1){
                    		c = '<a href="javaScript:;" onclick="updateVestAppCustomLayoutDisplayStatus(' + row.id + ',' + 0 + ')">不展示自定义板块</a>'
                    	}else{
                    		c = '<a href="javaScript:;" onclick="updateVestAppCustomLayoutDisplayStatus(' + row.id + ',' + 1 + ')">展示自定义板块</a>';
                    	}
                    	
                    	/* var c = '<a href="javaScript:;" onclick="deleteId(' + row.id + ')">删除</a>'; */
                    	/* return a + b + c; */
                    	return b + c;
                    }
                }
            ]
        ],
        /* toolbar: [
            {
                id: '_add',
                iconCls:'icon-add',
                text: '新增',
                handler: function () {
                	cleanVestAppDiv();
                	$('#editVestAppDiv').dialog('open');
                }
            }
        ], */
        pagination:true
    });

    $('#editVestAppDiv').dialog({
    	title:'马甲App信息',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#vestAppForm').form('submit',{
    				url: "${rc.contextPath}/index/saveOrUpdateVestApp",
    				onSubmit:function(){
    					var desc = $('#vestAppForm_desc').val();
    					if($.trim(desc)==''){
    						$.messager.alert("info","请输入马甲App名称","warn");
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
                                $('#editVestAppDiv').dialog('close');
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
                $('#editVestAppDiv').dialog('close');
            }
    	}]
    });
    
});
</script>

</body>
</html>