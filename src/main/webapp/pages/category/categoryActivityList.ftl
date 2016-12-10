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
<div data-options="region:'center',title:''" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'分类活动管理',split:true" style="height:100px;padding-top:10px">
			<table>
				<tr>
					<td>活动备注：</td>
					<td><input id="searchRemark" name="searchRemark" value="" /></td>
					<td>状态：</td>
					<td>
						<select id="searchIsAvailable" name="searchIsAvailable">
							<option value="-1">全部</option>
							<option value="1">可用</option>
							<option value="0">停用</option>
						</select>
					</td>
					<td><a id="searchBtn" onclick="searchCategory()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
					<td><a id="clearBtn" onclick="clearSearchForm()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a></td>
				</tr>
			</table>
		</div>	
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
		      		
		</div>
	</div>
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

	function searchCategory(){
		$('#s_data').datagrid('load',{
	    	remark:$("#searchRemark").val(),
	    	isAvailable:$("#searchIsAvailable").val()
		});
	}
	
	function clearSearchForm(){
		$("#searchRemark").val('');
		$("#searchIsAvailable").find("option").eq(0).attr("selected","selected");
		$('#s_data').datagrid('load',{});
	}

	<!--编辑排序相关 begin-->
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
			url: '${rc.contextPath}/category/updateCategoryActivitySequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load',{});
		            return;
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	<!--编辑排序相关 end-->
	
		
	
	$(function(){
		
		<!--列表数据加载-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/category/jsonCategoryActivityInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            columns:[[
                {field:'id',    title:'id', width:20, align:'center',checkbox:true},
                {field:'index',    title:'活动ID', width:20, align:'center'},
                {field:'remark',    title:'活动备注', width:70, align:'center'},
                {field:'isAvailable',    title:'可用状态', width:30, align:'center'},
                {field:'relationType',    title:'类型', width:30, align:'center'},
                {field:'sequence',    title:'排序值', width:50, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var b = '<a href="${rc.contextPath}/category/toEditActivity/' + row.id + '")>编辑</a>';
                    		return a + b;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增活动',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href = "${rc.contextPath}/category/toAddActivity"
                }
            },'-',{
                iconCls: 'icon-edit',
                text:'批量设为可用',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定启用吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryActivityStatus",
									{id: ids.join(","),isAvailable:1},
									function(data){
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$('#s_data').datagrid('clearSelections');
											$.messager.alert('提示','操作成功', 'info');
										}else{
											$.messager.alert('提示',data.msg,"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的行',"error")
                    }
                }
            },'-',{
                iconCls: 'icon-edit',
                text:'批量设为不可用',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定停用吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryActivityStatus",
									{id: ids.join(","),isAvailable:0},
									function(data){
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$('#s_data').datagrid('clearSelections');
											$.messager.alert('提示','操作成功', 'info');
										}else{
											$.messager.alert('提示',data.msg,"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的行',"error")
                    }
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
				
	});
</script>

</body>
</html>