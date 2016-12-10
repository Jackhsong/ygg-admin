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

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'个人中心管理',split:true" style="height: 100px;">
			<div style="height: 40px;padding: 10px">
	            <table>
	                <tr>
	                    <td >模块备注：</td>
	                    <td ><input id="searchRemark" name="searchRemark" value="" /></td>
	                    <td >展现状态：</td>
	                	<td >
	                		<select id="searchIsDisplay" name="searchIsDisplay">
	                			<option value="-1">全部</option>
	                			<option value="1">展现</option>
	                			<option value="0">不展现</option>
	                		</select>
	                	<td >
							<a id="searchBtn" onclick="searchCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;
							<a id="clearBtn" onclick="clearCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
							&nbsp;
	                	</td>
	                </tr>
	            </table>
			</div>
		</div> 
		
		<div data-options="region:'center'" >
			<!--数据表格-->
			<table id="s_data" ></table>
		</div>
	</div>
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
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

	function searchCustom(){
		$('#s_data').datagrid('load',{
			remark:$("#searchRemark").val(),
			isDisplay:$("#searchIsDisplay").val()
		});
	}
	
	function clearCustom(){
		$("#searchRemark").val('');
		$("#searchIsDisplay").find('option').eq(0).attr('selected','selected');
		$('#s_data').datagrid('load',{});
	}

	function displayId(id,isDisplay){
		var tip = "";
		if(isDisplay == 0){
			tip = "确定不展现吗？";
		}else{
			tip = "确定展现吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
   	            $.messager.progress();
   	            $.ajax({
   	            	url:'${rc.contextPath}/customCenter/updateDisplayStatus',
   	            	type:'post',
   	            	dataType:'json',
   	            	data:{'id':id,"code":isDisplay},
   	            	success:function(data){
   	            		$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid('clearSelections');
							$('#s_data').datagrid("reload");
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
	    });
	}
	
	function editIt(id){
		var url = '${rc.contextPath}/customCenter/edit/'+id;
    	window.open(url,'_blank');
	}

	$(function(){
		
		<!--加载楼层商品列表 begin-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customCenter/jsoncustomCenterInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            columns:[[
            	{field:'id',    title:'序号', align:'center',checkbox:true},
            	{field:'index',    title:'ID', width:20, align:'center'},
            	{field:'displayDesc',    title:'展现状态', width:20, align:'center'},
                {field:'remark',    title:'功能入口备注', width:50, align:'center'},
                {field:'displayType',    title:'布局方式', width:20, align:'center'},
                {field:'oneTitle',    title:'第一张标题', width:40, align:'center'},
                {field:'twoTitle',    title:'第二张标题', width:40, align:'center'},
                {field:'threeTitle',    title:'第三张标题', width:40, align:'center'},
                {field:'fourTitle',    title:'第四张标题', width:40, align:'center'},
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                   		var a = '<a href="javascript:;" onclick="editIt('+ row.id + ')">编辑</a>';
                       	var b = '';
                       	if(row.isDisplay == 0){
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 1 + ')">展现</a>'
                       	}else{
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 0 + ')">不展现</a>'
                       	}
                       	return a+b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                	var url = '${rc.contextPath}/customCenter/edit/0';
                	window.open(url,'_blank');
                }
            },'-',{
            	text:'批量展现',
                iconCls:'icon-edit',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                	if(rows.length>0){
                		var ids = [];
                        for(var i=0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                        displayId(ids.join(","),1);
                	}else{
                		$.messager.alert('提示','请选择要操作的行',"error");
                	}
                }
            },'-',{
            	text:'批量不展现',
                iconCls:'icon-edit',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                	if(rows.length>0){
                		var ids = [];
                        for(var i=0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                        displayId(ids.join(","),0);
                	}else{
                		$.messager.alert('提示','请选择要操作的行',"error");
                	}
                }
            }],
            pagination:true
        });
	
	});
	
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
        });
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

</body>
</html>