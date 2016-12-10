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

<div data-options="region:'center',title:''" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<!-- 搜索原型上没有暂时不做
		<div data-options="region:'north',title:'一级分类管理',split:true" style="height:100px;padding-top:10px">
			<table>
				<tr>
					<td>一级分类：</td>
					<td><input id="searchCategoryFirstName" name="searchCategoryFirstName"  value=""/></td>
					<td>二级分类：</td>
					<td><input id="searchCategorySecondName" name="searchCategorySecondName" value=""/></td>
					<td>状态：</td>
					<td>
						<select id="searchIsAvailable" name="searchIsAvailable">
							<option value="-1">全部</option>
							<option value="1">可用</option>
							<option value="0">停用</option>
						</select>
					</td>
					<td><a id="searchBtn" onclick="searchCategory()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
					<td><a id="searchBtn" onclick="clearSearchForm()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a></td>
				</tr>
			</table>
		</div> -->
		<div data-options="region:'center'" >
		    <!--数据表格-->
		    <table id="s_data" style=""></table>
		    
			<!-- 编辑分类begin -->
		    <div id="editCategoryDiv" class="easyui-dialog" style="width:550px;height:350px;padding:20px 20px;">
		        <form id="editCategoryForm" method="post">
					<input id="editCategoryForm_id" type="hidden" name="id" value="" >
					<p>
						<span>选择一级分类：</span>
						<span><input type="text" id="editCategoryForm_fid" name="categoryFirstId" style="width:300px"></span>
					</p>
					<p>
						<span>选择二级分类：</span>
						<span><input type="text" id="editCategoryForm_sid" name="categorySecondId" style="width:300px"></span>
					</p>
					<p>
						<span>三级分类名称：</span>
						<span><input type="text" id="editCategoryForm_name" name="name" style="width:300px"  maxlength="10"><font color="red">不超过5个汉字</font></span>
					</p>
					<p>
						<span>&nbsp;&nbsp;可用状态：</span>
						<span>
							<input type="radio" id="editCategoryForm_isAvailable1" name="isAvailable" value="1" checked="checked"/>可用&nbsp;&nbsp;&nbsp;
							<input type="radio" id="editCategoryForm_isAvailable0" name="isAvailable" value="0" />不可用
						</span>
					</p>
					<p>
						<span>&nbsp;&nbsp;展现状态：</span>
						<span>
							<input type="radio" id="editCategoryForm_isDisplay1" name="isDisplay" value="1" checked="checked"/>展现&nbsp;&nbsp;&nbsp;
							<input type="radio" id="editCategoryForm_isDisplay0" name="isDisplay" value="0" />不展现
						</span>
					</p>
					<p>
						<span>三级分类商品排序规则：</span>
						<span>
							<input type="radio" id="editCategoryForm_orderType1" name="orderType" value="1" checked="checked"/>按销量排序&nbsp;&nbsp;
							<input type="radio" id="editCategoryForm_orderType2" name="orderType" value="2" />按类目的顺序排序						
						</span>

					</p>
					<p>
						<span>&nbsp;&nbsp;是否HOT ：</span>
						<span>
							<input type="checkbox" id="editCategoryForm_isHot" name="isHot" value="1"/>HOT
						</span>
					</p>
					<p>
						<span>&nbsp;&nbsp;是否高亮：</span>
						<span>
							<input type="checkbox" id="editCategoryForm_isHighlight" name="isHighlight" value="1"/>高亮
						</span>
					</p>
		    	</form>
		    </div>
		    <!-- 编辑分类end -->
		    
		</div>
	</div>
</div>


<script>

	function strlen(str){  
	    var len = 0;  
	    for (var i=0; i<str.length; i++) {   
		     var c = str.charCodeAt(i);   
		     if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {   
		       len+=0.5;   
		     }else {   
		      len+=1;   
		     }   
	    }   
	    return len;  
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
			url: '${rc.contextPath}/category/updateCategoryThirdSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load',{});
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	<!--编辑排序相关 end-->		

	function clearEditCategoryForm(){
		$("#editCategoryForm_id").val('');
		$("#editCategoryForm input[type='text']").each(function(){
			$(this).val("");
		});
		$("#editCategoryForm_fid").combobox('clear');
		$("#editCategoryForm_sid").combobox('clear');
		$("#editCategoryForm_isAvailable1").prop("checked",true);
		$("#editCategoryForm_isDisplay1").prop("checked",true);
		$("input[type='checkbox']").each(function(){
			$(this).attr('checked',false);
		});
	}
	
	function editIt(index){
		clearEditCategoryForm();
	    var arr=$("#s_data").datagrid("getData");
	    $("#editCategoryForm_id").val(arr.rows[index].id);
		$("#editCategoryForm_name").val(arr.rows[index].name);
		$('#editCategoryForm_fid').combobox('clear');
		$('#editCategoryForm_sid').combobox('clear');
		var fid = arr.rows[index].fid;
		var sid = arr.rows[index].sid;
		var url1 = '${rc.contextPath}/category/jsonCategoryFirstCode?id='+fid;
		var url2 = '${rc.contextPath}/category/jsonCategorySecondCode?id='+sid+'&firstId='+fid;
		$('#editCategoryForm_fid').combobox('reload',url1);
		$('#editCategoryForm_sid').combobox('reload',url2);
		var isAvailable = arr.rows[index].isAvailable;
		var isDisplay = arr.rows[index].isDisplay;
		var isHot = arr.rows[index].isHot;
		var isHighlight = arr.rows[index].isHighlight;
	
		$("#editCategoryForm input[name='isAvailable']").each(function(){
			if($(this).val() == isAvailable){
				$(this).prop("checked",true);
			}
		});
		$("#editCategoryForm input[name='isDisplay']").each(function(){
			if($(this).val() == isDisplay){
				$(this).prop("checked",true);
			}
		});
		if(isHot == 1){
			$("#editCategoryForm_isHot").prop('checked',true);
		}
		if(isHighlight == 1){
			$("#editCategoryForm_isHighlight").prop('checked',true);
		}
	    $("#editCategoryDiv").dialog('open');
	}


	$(function(){
		
		$('#editCategoryForm_fid').combobox({   
		    url:'${rc.contextPath}/category/jsonCategoryFirstCode',   
		    valueField:'code',   
		    textField:'text',
		    editable:false,
		    onSelect:function(rac){
		    	$('#editCategoryForm_sid').combobox('clear');
		    	var url = '${rc.contextPath}/category/jsonCategorySecondCode?firstId='+rac.code;
		    	$('#editCategoryForm_sid').combobox('reload',url);
		    }
		    
		});
		
		$('#editCategoryForm_sid').combobox({
			url:'${rc.contextPath}/category/jsonCategorySecondCode',   
		    valueField:'code',   
		    textField:'text',
		    editable:false
		});
		
	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/category/jsonCategoryThirdInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect: false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'fname',	title:'一级分类', width:50, align:'center'},
                {field:'sname',	title:'二级分类', width:50, align:'center'},
                {field:'name',  title:'三级分类', width:50, align:'center'},
                {field:'isAvailableDesc',    title:'使用状态', width:20, align:'center'},
                {field:'isDisplayDesc',    title:'展现状态', width:20, align:'center'},
                {field:'isHotDesc',    title:'是否HOT', width:20, align:'center'},
                {field:'isHightLightDesc',    title:'是否高亮', width:20, align:'center'},
                {field:'orderType',    title:'商品排序规则', width:20, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var b = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a>';
                    		return a + b;
                    	}
                    }
                }
            ]],
            toolbar:[
            {
            	id:'_add',
                text:'新增分类',
                iconCls:'icon-add',
                handler:function(){
                	clearEditCategoryForm();
                	$('#editCategoryDiv').dialog('open');
                }
            },'-',{
                iconCls: 'icon-add',
                text:'启用',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定启用吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryThirdStatus",
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
                iconCls: 'icon-remove',
                text:'停用',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定停用吗？停用后，所有已选择该三级类目的商品，相应的三级类目会被清空。',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryThirdStatus",
									{id: ids.join(","),isAvailable:0},
									function(data){
										if(data.status == 1){
											$('#s_data').datagrid('reload');
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
                iconCls: 'icon-add',
                text:'展现',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定展现吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryThirdDisplay",
									{id: ids.join(","),isDisplay:1},
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
                iconCls: 'icon-remove',
                text:'不展现',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定不展现吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryThirdDisplay",
									{id: ids.join(","),isDisplay:0},
									function(data){
										if(data.status == 1){
											$('#s_data').datagrid('reload');
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
        
        $('#editCategoryDiv').dialog({
            title:'三级分类',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#editCategoryForm').form('submit',{
                        url:"${rc.contextPath}/category/saveOrUpdateCategoryThird",
                        onSubmit:function(){
                        	var fid = $("#editCategoryForm_fid").combobox('getValue');
                        	var sid = $("#editCategoryForm_sid").combobox('getValue');
                        	var name = $("#editCategoryForm_name").val();
                        	if(fid == '' || fid == undefined || fid == null){
                        		$.messager.alert("提示","请选择一级分类","error");
        						return false;
                        	}else if(sid == '' || sid == undefined || sid == null){
                        		$.messager.alert("提示","请选择二级分类","error");
        						return false;
                        	}else if($.trim(name) == ''){
                        		$.messager.alert("提示","请输入三级分类名称","error");
        						return false;
                        	}else if(strlen(name)>5){
                        		$.messager.alert("提示","三级级分类名称字符长度超过限制","error");
        						return false;
                        	}
                        	$.messager.progress();
                        },
                        success:function(data){
                        	$.messager.progress('close');
                            var res = eval("("+data+")");
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info');
                                $('#s_data').datagrid('load');
                                $('#s_data').datagrid('clearSelections');
                                $('#editCategoryDiv').dialog('close');
                            }else{
                                $.messager.alert('响应信息',res.msg,'error');
                            }
                        }
                    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editCategoryDiv').dialog('close');
                }
            }]
        });       
	});
</script>

</body>
</html>