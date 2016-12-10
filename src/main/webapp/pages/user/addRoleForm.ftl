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

	<div title="角色管理" class="easyui-panel" style="padding:10px">
        <table id="s_data" >

        </table>
	</div>
	
	<div id="addRole_div" class="easyui-dialog" style="width:400px;height:500px;padding:20px 20px;">
		<form id="af" method="post">
			<input id="id" type="hidden" value="" />
            <table cellpadding="5">
                <tr>
                    <td>新角色:</td>
                    <td><input id="role" name="role"></input></td>
                </tr>
                <tr>
                    <td>描述:</td>
                    <td><input id="desc" name="desc"></input></td>
                </tr>   
                <tr>
                    <td>资源分配:</td>
                    <td>
                    <#list  permissions as bl >
                    	<input type="checkbox" name="resources" value="${bl.id}">：${bl.description}<br/>
					</#list>
                    </td>
                </tr>
            </table>
        </form>
	</div>

</div>

<script>

	function editRole(index){
    	var arr=$("#s_data").datagrid("getData");
    	var pIds = arr.rows[index].pIds;
    	$("#id").val(arr.rows[index].id);
    	$("#role").val(arr.rows[index].role);
    	$("#desc").val(arr.rows[index].description);
    	$("input[name='resources']:checked").each(function(){ 
    		$(this).prop("checked",false);
    	});
    	$.each(pIds,function(i){
            var nl = pIds[i];
            $("input[name='resources']").each(function(j){ 
				if($(this).val() == nl){
					$(this).prop("checked",true);
				}
        	});
        });
    	$("#addRole_div").dialog('open');
	}

	$(function(){
	
		$('#addRole_div').dialog({
            title:'新增角色',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	$.messager.progress();
                	var id=$("#id").val();
                	var role=$("input[name='role']").val();
                	var desc=$("input[name='desc']").val();
                	var resources="";
                	$("input[name='resources']:checked").each(function(){ 
                		resources+=$(this).val()+",";
                	});
                	$.ajax({
    	                url: '${rc.contextPath}/user/addRole',
    	                type: 'post',
    	                dataType: 'json',
    	                data: {'role':role,'resource':resources,'desc':desc,'id':id},
    	                success: function(data){
    	                	$.messager.progress('close');
    	                    if(data.status == 1){
    	                    	$('#s_data').datagrid('load');
    	                    	$('#addRole_div').dialog('close');
    	                    }else{
    	                    	$.messager.alert("提示",data.msg,"error");
    	                    }
    	                },
    	                error: function(xhr){
    	                	$.messager.progress('close');
    	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
    	                }
    	            });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addRole_div').dialog('close');
                }
            }]
        });
	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/user/roleInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            columns:[[
                {field:'id',    title:'ID', width:50, align:'center'},
                {field:'role',    title:'名称', width:30, align:'center'},
                {field:'description',     title:'角色描述',  width:100,   align:'center' },
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="editRole(' + index + ')">修改</a>';
                        return lableStr;
                    }
                }
            ]],
            toolbar:[{
                    iconCls: 'icon-add',
                    text:'新增角色',
                    handler: function(){
                    	$("input[name='role']").val("");
                    	$("input[name='desc']").val("");
                    	$("#id").val("");
                    	$("input[name='resources']:checked").each(function(){ 
                    		//$(this).removeAttr("checked");
                    		$(this).prop("checked",false);
                    		//$(this).attr('checked', false);
                    	});
                    	$('#addRole_div').dialog('open');
                    }
             }]
        });
	})
</script>

</body>
</html>