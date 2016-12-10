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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div id="searchDiv" class="datagrid-toolbar" style="height: 110px;padding: 15px">
        <form id="searchForm" method="post" >
			<table>
				<tr>
					<td>类别名称：</td>
					<td><input id="categoryName" name="categoryName" value="" /></td>
					<td><a id="searchBtn" onclick="searchCategory()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
					<td><a id="searchBtn" onclick="clearSearchForm()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a></td>
				</tr>
			</table>
        </form>
    </div>
    <!--数据表格-->
    <table id="s_data" style=""></table>
        
</div>


<script>

	function deleteImage(index){
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		$.messager.confirm('删除','确定删除吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/image/delete',
		            type: 'post',
		            dataType: 'json',
		            data: {'id':id, 'isAvailable':0, type:'sale'},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
		                	$('#s_data').datagrid('load');
		                	$.messager.alert("提示",'删除成功',"info");
		                }else{
		                	$.messager.alert("提示",data.msg,"info");
		                }
		            },
		            error: function(xhr){
		            	$.messager.progress('close');
		            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		            }
		        });
			}
		});
	}
	
	
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
	

	function searchCategory(){
    	$('#s_data').datagrid('load',{
        	name:$("#categoryName").val(),
        	type:"sale"
    	});
	}

	function clearSearchForm(){
		$("#categoryName").val('');
	}

	function modifyImage(index){
    	var arr=$("#s_data").datagrid("getData");
    	window.location.href = "${rc.contextPath}/image/edit/"+arr.rows[index].id+"?type=sale";
	}
	


	$(function(){
	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/image/jsonImageInfo',
            loadMsg:'正在装载数据...',
            queryParams:{type:"sale"},
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'index',    title:'ID', width:10, align:'center'},
                {field:'name',     title:'分类',  width:50,  align:'center'},
                {field:'image',    title:'格格头像',  width:50,  align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="modifyImage(' + index + ')">修改</a>';
                        var b = ' | <a href="javaScript:;" onclick="deleteImage(' + index + ')">删除</a>';
                        return a + b;
                    }
                }
            ]],
            toolbar:[
            {
                id:'_add',
                text:'新增分类',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href = "${rc.contextPath}/image/add?type=sale"
                }
            },'-',
            {
                iconCls: 'icon-remove',
                text:'批量删除',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('删除','确定删除吗',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/image/batchDelete", //批量删除
									{ids: ids.join(","),isAvailable:0, type:"sale"},
									function(data){
										if(data.status == 1){
											$('#s_data').datagrid('reload');
											$.messager.alert('提示',data.msg, 'info');
										}else{
											$.messager.alert('提示',data.msg,"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的类别',"error")
                    }
                }
            }
            ],
         	pagination:true
        });
       
	});
</script>

</body>
</html>