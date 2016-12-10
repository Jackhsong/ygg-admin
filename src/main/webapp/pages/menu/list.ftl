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

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div title="菜单管理" class="easyui-panel" style="padding:10px">
		<table id="s_data" ></table>
	</div>
	
	<!-- dialog -->
	<div id="add_div" class="easyui-dialog" style="width:400px;height:250px;padding:20px 20px;">
		<form id="addForm" method="post" enctype="multipart/form-data">
			<input id="id" type="hidden" name="id" value="" >
			<input id="pid" type="hidden" name="pid" value="${pid}" >
            <table cellpadding="5">
                <tr>
                    <td class="searchName">描述：</td>
                    <td class="searchText"><input maxlength="30" id="text" name="text"  style="width:250px"></input></td>
                </tr>
                <tr>
                    <td class="searchName">访问路径：</td>
                    <td class="searchText"><input maxlength="50" id="url" name="url" style="width:250px"></input></td>
                </tr>
                <tr>
                    <td class="searchName">排序值：</td>
                    <td class="searchText">
                    	<input id="sequence" name="sequence"  style="width:250px"></input>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>

<script>
	function show(pid){
		window.location.href = '${rc.contextPath}/menu/menuList?pid='+pid;
	}
	function edit(index){
		var arr=$("#s_data").datagrid("getData");
		$('#id').val(arr.rows[index].id);
    	$('#url').val(arr.rows[index].url);
    	$('#text').val(arr.rows[index].text);
		$('#sequence').val(arr.rows[index].sequence);
		$('#add_div').dialog('open');
	}
	function deleteIt(id){
	    $.messager.confirm("提示信息","确定删除么？",function(re){
	        if(re){
	            $.messager.progress();
	            $.post("${rc.contextPath}/menu/delete",{id:id},function(data){
	                $.messager.progress('close');
	                if(data.status == 1){
	                    $.messager.alert('响应信息',"删除成功...",'info',function(){
	                        $('#s_data').datagrid('reload');
	                        return
	                    });
	                } else{
	                    $.messager.alert('响应信息',data.msg,'error',function(){
	                        return
	                    });
	                }
	            })
	        }
	    })
	}
	$(function(){
		
		$('#add_div').dialog({
            title:'add-menu',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
					$('#addForm').form('submit',{
						url:"${rc.contextPath}/menu/update",
					    onSubmit: function(){
					    	$.messager.progress();
					    },
					    success:function(data){
					    	$.messager.progress('close');
					    	var data = eval('(' + data + ')');  
							if(data.status == 1){
								$.messager.alert("提示","成功","info");
								$('#s_data').datagrid("load");
								$('#add_div').dialog('close');
							}else{
								$.messager.alert("提示","失败","info");
							}
					    }
					});
					
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#add_div').dialog('close');
                }
            }]
        });
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/menu/jsonMenuList',
            queryParams:{
            	pid:${pid}
            },
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:70,
            pageList:[70,80],
            columns:[[
                {field:'id',    title:'ID', width:30, align:'center'},
                {field:'pid',    title:'PID', width:30, align:'center'},
                {field:'text',     title:'描述',  width:50,   align:'center' },
                {field:'url',    title:'访问路径', width:70, align:'center'},
                {field:'sequence',    title:'排序值', width:30, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var a = '';
                        var b = '';
                        var c = '';
                        if(row.url == ''){
                        	a += '<a href="javaScript:;" onclick="show(' + row.id + ')">查看子目录</a>';                        	
                        }
                        b += ' | <a href="javaScript:;" onclick="edit(' + index + ')">编辑</a>';
                        c += ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
                        return a+b+c;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增Menu',
                iconCls:'icon-add',
                handler:function(){
                	$('#id').val("");
                	$('#url').val("");
                	$('#text').val("");
            		$('#sequence').val("");
                	$('#add_div').dialog('open');
                }
            }],
            pagination:true
        });
	})
</script>

</body>
</html>