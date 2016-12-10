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

<div data-options="region:'center',title:'content'" style="padding:5px;">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 50px;padding: 15px">
        <form id="searchForm" method="post" >
            <table>
            	<tr>
            		<td>Id：</td>
                    <td><input id="mallWindowId" name="mallWindowId" value=""/></td>
                    <td>名称：</td>
                    <td><input id="name" name="name" value="" /></td>
                    <td>展现状态：</td>
                    <td>
                    	<select id="isDisplay" name="isDisplay">
	                    	<option value="1">展现</option>
	                    	<option value="0">不展现</option>
                    	</select>
                    </td>
					<td><a id="searchBtn" onclick="searchSaleWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
                	<td><a id="clearBtn" onclick="clearSaleWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a></td>
                </tr>
            </table>
        </form>
    </div>
    <!--数据表格-->
    <table id="s_data"></table>

</div>

<script>

	function searchSaleWindow(){
    	$('#s_data').datagrid('load',{
        	id:$("#mallWindowId").val(),
        	name:$("#name").val(),
        	isDisplay:$("#isDisplay").val()
    	});
	}
	
	function clearSaleWindow(){
		$("#mallWindowId").val(''),
    	$("#name").val(''),
    	$("#isDisplay").find("option").eq(0).attr('selected','selected');
		$('#s_data').datagrid('load',{});
	}
	
	function editrow(index){
		$('#s_data').datagrid('beginEdit', index);
	};

	function updateActions(){
		var rowcount = $('#s_data').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_data').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}

	function saverow(index){
		$('#s_data').datagrid('endEdit', index);
	};

	function cancelrow(index){
		$('#s_data').datagrid('cancelEdit', index);
	};

	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/mallWindow/updateSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('reload');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};

	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr = "${rc.contextPath}/mallWindow/edit/"+arr.rows[index].id;
		window.open(urlStr,"_blank");
	}
	
	function displayIt(id,code){
		var tips = '';
		if(code == 1){
			tips = '确定展现吗？';
		}else{
			tips = '确定不展现吗？'
		}
    	$.messager.confirm("提示信息",tips,function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/mallWindow/updateDisplayCode",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('reload');
                        return
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	})
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/mallWindow/jsonMallWindowInfo',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:20, align:'center'},
                {field:'isDiaplay',    title:'展现状态', width:70, align:'center'},
                {field:'mallWindowName',    title:'名称', width:40, align:'center'},
                {field:'remark',    title:'备注', width:60, align:'center'},
                {field:'image',     title:'图标',  width:50,   align:'center' },
                {field:'mallPageName',     title:'关联主题馆页面',  width:50,   align:'center' },
                {field:'sequence',     title:'排序值',  width:30,   align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                    		var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                            var b = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                            var c = '';
	                        if(row.isDisplayCode == 1){
	                        	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">不展现</a>';
	                        }else{
	                        	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">展现</a>';
	                        }
	                        return a + b + c;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增布局',
                iconCls:'icon-add',
                handler:function(){
                    window.location.href = "${rc.contextPath}/mallWindow/addMallWindow"
                }
            }],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions();
            	updateActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions();
        	},
            pagination:true
        });
	})
</script>

</body>
</html>