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

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'自定义版块管理-新版本',split:true" style="height: 90px;">
			<br/>查看以下版块：
			<input type="checkbox" name="searchIsAvailable" value="1" checked/>可用
		    <input type="checkbox" name="searchIsAvailable" value="0" />停用
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editCustomRegionDiv" class="easyui-dialog" style="width:500px;height:280px;padding:15px 20px;">
		        <form id="editCustomRegionForm" method="post">
					<input id="editCustomRegionForm_id" type="hidden" name="id" value="" >
					<p>
						<span>版块标题：</span>
						<span><input type="text" name="title" id="editCustomRegionForm_title" value="" maxlength="32" style="width: 300px;"/></span>
						<br/>
						<span style="color:red">&nbsp;&nbsp;提示：</span>
						<span style="color:red">标题会在页面上展示，如不需要展示请勿填写</span>
					</p>
					<p>
						<span>版块备注：</span>
						<span><input type="text" name="remark" id="editCustomRegionForm_remark" value="" maxlength="100" style="width: 300px;"/></span>
					</p>
					<p>
						<span>可见状态：</span>
						<span id="isDisplaySpan"><input type="radio" name="isDisplay" id="editCustomRegionForm_isDisplay1" value="1"/>可见&nbsp;&nbsp;</span>
						<span><input type="radio" name="isDisplay" id="editCustomRegionForm_isDisplay0" value="0" checked="checked"/>不可见</span>
					</p>
					<p>
						<span>可用状态：</span>
						<span><input type="radio" name="isAvailable" id="editCustomRegionForm_isAvailable1" checked="checked" value="1"/>可用&nbsp;&nbsp;</span>
						<span id="isAvailableSpan"><input type="radio" name="isAvailable" id="editCustomRegionForm_isAvailable0" value="0"/>停用</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
    		
		</div>
        
	</div>
</div>

<script>
	function searchCustomRegion(){
		var isAvailable =[]; 
		$("input[name='searchIsAvailable']:checked").each(function(){ 
			isAvailable.push($(this).val()); 
		});
		var isAvailableStr = isAvailable.join(",");
		if(isAvailableStr == ''){
			isAvailableStr = '-1';
		}
		
    	$('#s_data').datagrid('load',{
    		isAvailable:isAvailableStr
    	});
	}
	
	function cleanEditCustomRegionDiv(){
		$("#editCustomRegionForm_id").val('');
		$("#editCustomRegionForm input[type='text']").each(function(){
			$(this).val('');
		});
	}
	
	function editIt(index){
		$("input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		cleanEditCustomRegionDiv();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var title = arr.rows[index].title;
		var remark = arr.rows[index].remark;
		var isAvailable = arr.rows[index].isAvailable;
		var isDisplay = arr.rows[index].isDisplay;
		$('#isDisplaySpan').css('display','');
    	$('#isAvailableSpan').css('display','');
    	$('#editCustomRegionForm_id').val(id);
    	$('#editCustomRegionForm_title').val(title);
    	$('#editCustomRegionForm_remark').val(remark);
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==isDisplay){
    			$(this).prop('checked',true);
    		}
    	});
    	$("input[name='isAvailable']").each(function(){
    		if($(this).val()==isAvailable){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#editCustomRegionDiv').dialog('open');
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
			url: '${rc.contextPath}/customRegion24/updateCustomRegionSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	
	function displayIt(id,code){
		var tips = '';
		if(code == 1){
			tips = '确定设为可见吗？';
		}else{
			tips = '确定设为不可见吗？'
		}
    	$.messager.confirm("提示信息",tips,function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/customRegion24/updateCustomRegionDisplayStatus",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('reload');
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	});
	}
	
	function availableIt(id,code){
		var tips = '';
		if(code == 1){
			tips = '确定启用吗？';
		}else{
			tips = '确定停用吗？'
		}
    	$.messager.confirm("提示信息",tips,function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/customRegion24/updateCustomRegionAvailableStatus",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('reload');
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	});
	}

	$(function(){
		$("input[name='searchIsAvailable']").each(function(){
			$(this).change(function(){
				searchCustomRegion();
			});
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customRegion24/jsonCustomRegionInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:20, align:'center',checkbox:true},
            	{field:'index',    title:'ID', width:20, align:'center'},
                {field:'availableDesc',    title:'可用状态', width:70, align:'center'},
                {field:'displayDesc',    title:'可见状态', width:70, align:'center'},
                {field:'title',    title:'名称', width:40, align:'center'},
                {field:'remark',    title:'备注', width:60, align:'center'},
                {field:'sequence',     title:'排序值',  width:30,   align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var b = '<a href="javascript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                            var c = '<a target="_blank" href="${rc.contextPath}/customRegion24/manageLayout/' + row.id + '">管理布局</a> | ';
                            var d = '';
                            var e = '';
                            if(row.isDisplay == 1){
	                        	d = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">设为不可见</a> | ';
	                        }else if(row.isDisplay == 0) {
	                        	d = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">设为可见</a> | ';
	                        }
	                        if(row.isAvailable == 1){
	                        	e = '<a href="javaScript:;" onclick="availableIt(' + row.id + ',' + 0 + ')">停用</a>';
	                        }else if(row.isAvailable == 0) {
	                        	e = '<a href="javaScript:;" onclick="availableIt(' + row.id + ',' + 1 + ')">启用 </a>';
	                        }
	                        return a + b + c + d + e;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增板块',
                iconCls:'icon-add',
                handler:function(){
                	cleanEditCustomRegionDiv();
                	$('#isDisplaySpan').css('display','none');
                	$('#isAvailableSpan').css('display','none');
                	$('#editCustomRegionDiv').dialog('open');
                }
            },'-',{
                id:'_edit1',
                text:'批量可用',
                iconCls:'icon-add',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('展现','确认全部设为可用吗？',function(b){
                            if(b){
                            	$.messager.progress();
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id);
                                }
                                $.post("${rc.contextPath}/customRegion24/updateCustomRegionAvailableStatus",
									{id: ids.join(","),code:1},
									function(data){
										$.messager.progress('close');
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$.messager.alert('提示','操作成功',"info");
											$('#s_data').datagrid('clearSelections');
										}else{
											$.messager.alert('提示','保存出错',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的版块',"error")
                    }
                }
            },'-',{
                id:'_edit2',
                text:'批量停用',
                iconCls:'icon-remove',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('展现','确认全部停用吗？',function(b){
                            if(b){
                            	$.messager.progress();
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id);
                                }
                                $.post("${rc.contextPath}/customRegion24/updateCustomRegionAvailableStatus",
									{id: ids.join(","),code:0},
									function(data){
										$.messager.progress('close');
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$.messager.alert('提示','操作成功',"info");
											$('#s_data').datagrid('clearSelections');
										}else{
											$.messager.alert('提示','保存出错',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的版块',"error")
                    }
                }
            },'-',{
                id:'_edit3',
                text:'批量可见',
                iconCls:'icon-edit',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('展现','确认全部设为可见吗？',function(b){
                            if(b){
                            	$.messager.progress();
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id);
                                }
                                $.post("${rc.contextPath}/customRegion24/updateCustomRegionDisplayStatus",
									{id: ids.join(","),code:1},
									function(data){
										$.messager.progress('close');
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$.messager.alert('提示','操作成功',"info");
											$('#s_data').datagrid('clearSelections');
										}else{
											$.messager.alert('提示','保存出错',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的版块',"error")
                    }
                }
            },'-',{
                id:'_edit4',
                text:'批量不可见',
                iconCls:'icon-remove',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('展现','确认全部设为不可见吗？',function(b){
                            if(b){
                            	$.messager.progress();
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id);
                                }
                                $.post("${rc.contextPath}/customRegion24/updateCustomRegionDisplayStatus",
									{id: ids.join(","),code:0},
									function(data){
										$.messager.progress('close');
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$.messager.alert('提示','操作成功',"info");
											$('#s_data').datagrid('clearSelections');
										}else{
											$.messager.alert('提示','保存出错',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的版块',"error")
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
		
		
	    $('#editCustomRegionDiv').dialog({
	    	title:'自定义版块',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editCustomRegionForm').form('submit',{
	    				url: "${rc.contextPath}/customRegion24/saveOrUpdateCustonRegion",
	    				onSubmit:function(){
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#s_data').datagrid('load');
	                                $('#editCustomRegionDiv').dialog('close');
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
	                $('#editCustomRegionDiv').dialog('close');
	            }
	    	}]
	    });	
	});
</script>

</body>
</html>