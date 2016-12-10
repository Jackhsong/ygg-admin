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
					<td><a id="clearBtn" onclick="clearSearchForm()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a></td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" >
		    <!--数据表格-->
		    <table id="s_data" style=""></table>
		    
			<!-- 编辑分类begin -->
		    <div id="editCategoryDiv" class="easyui-dialog" style="width:550px;height:300px;padding:20px 20px;">
		        <form id="editCategoryForm" method="post">
					<input id="editCategoryForm_id" type="hidden" name="id" value="" >
					<p>
						<span>选择一级分类：</span>
						<span><input type="text" id="editCategoryForm_fid" name="categoryFirstId" style="width:300px"></span>
					</p>
					<p>
						<span>二级分类名称：</span>
						<span><input type="text" id="editCategoryForm_name" name="name" style="width:300px"  maxlength="20"><font color="red">不超过10个汉字</font></span>
					</p>
					<p>
						<span>可用状态：</span>
						<span>
							<input type="radio" id="editCategoryForm_isAvailable1" name="isAvailable" value="1" checked="checked"/>可用&nbsp;&nbsp;&nbsp;
							<input type="radio" id="editCategoryForm_isAvailable0" name="isAvailable" value="0" />不可用
						</span>
					</p>
					<p>
                        <span>分类图：</span>
						<span>
							<span style="color:red">固定大小：582x268</span>
							<input type="text" id="categoryImage" name="categoryImage" style="width:300px">
							<a onclick="picDialogOpen('categoryImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						</span>
					</p>
					<p>
						<span>二级分类下全部商品排序规则：</span>
						<span>
							<input type="radio" id="editCategoryForm_orderType1" name="orderType" value="1" checked="checked"/>按销量排序&nbsp;&nbsp;
							<input type="radio" id="editCategoryForm_orderType2" name="orderType" value="2" />按所包含三级类目的顺序排序						
						</span>

					</p>
					<p>
						<span>在APP中展现状态：</span>
						<span>
							<input type="radio" id="editCategoryForm_isShowInApp1" name="isDisplay" value="1"/>展现&nbsp;&nbsp;&nbsp;
							<input type="radio" id="editCategoryForm_isShowInApp0" name="isDisplay" value="0" checked="checked"/>不展现
						</span>
					</p>
		    	</form>
		    </div>
		    <!-- 编辑分类end -->
		    
		</div>
	</div>
</div>


<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input type="hidden" name="needWidth" id="needWidth" value="582">
        <input type="hidden" name="needHeight" id="needHeight" value="268">
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
	
	function searchCategory(){
    	$('#s_data').datagrid('load',{
        	fname:$("#searchCategoryFirstName").val(),
        	name:$("#searchCategorySecondName").val(),
        	isAvailable:$("#searchIsAvailable").val()
    	});
	}

	function clearSearchForm(){
       	$("#searchCategoryFirstName").val('');
       	$("#searchCategorySecondName").val('');
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
			url: '${rc.contextPath}/category/updateCategorySecondSequence',
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
		$("#categoryImage").val('');
        $("#picFile").val("");
		$("#editCategoryForm_fid").combobox('clear');
		$("#editCategoryForm_isAvailable1").prop("checked",true);
		$("#editCategoryForm_orderType1").prop("checked",true);
		$("#editCategoryForm_isShowInApp0").prop("checked",true);
	}
	
	function editIt(index){
		clearEditCategoryForm();
	    var arr=$("#s_data").datagrid("getData");
	    $("#editCategoryForm_id").val(arr.rows[index].id);
		$("#editCategoryForm_name").val(arr.rows[index].name);
		$("#categoryImage").val(arr.rows[index].image);
		var fid = arr.rows[index].fId;
		var url = '${rc.contextPath}/category/jsonCategoryFirstCode?id='+fid;
		$('#editCategoryForm_fid').combobox('reload',url);
		var orderType = arr.rows[index].orderType;
		var isAvailable = arr.rows[index].isAvailable;
		var isDisplay = arr.rows[index].isDisplay;
		$("#editCategoryForm input[name='orderType']").each(function(){
			if($(this).val() == orderType){
				$(this).prop("checked",true);
			}
		});
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
	    $("#editCategoryDiv").dialog('open');
	}

	$(function(){
		
		$('#editCategoryForm_fid').combobox({   
			    url:'${rc.contextPath}/category/jsonCategoryFirstCode',   
			    valueField:'code',   
			    textField:'text',
			    editable:false
		});
		
	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/category/jsonCategorySecondInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect: false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'index',    title:'ID', width:10, align:'center'},
                {field:'fname',	title:'一级分类', width:50, align:'center'},
                {field:'name',     title:'二级分类',  width:50,  align:'center'},
                {field:'imageUrl',    title:'分类图', width:20, align:'center'},
                {field:'isAvailableDesc',    title:'可用状态', width:20, align:'center'},
                {field:'isDisplayDesc',    title:'展现状态', width:20, align:'center'},
                {field:'amount',    title:'包含三级分类数量', width:30, align:'center'},
                {field:'orderTypeDesc',    title:'二级下全部商品排序规则', width:30, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:20,align:'center',
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
                                $.post("${rc.contextPath}/category/updateCategorySecondStatus",
									{id: ids.join(","),isDisplay:1},
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
                                $.post("${rc.contextPath}/category/updateCategorySecondStatus",
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
                                $.post("${rc.contextPath}/category/updateCategorySecondStatus",
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
                        $.messager.confirm('提示','确定停用吗？停用后，该二级类目下所有的三级类目以及所有已选择该二级类目的商品都会被清空',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategorySecondStatus",
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
        
        $('#editCategoryDiv').dialog({
            title:'二级分类',
            width: 600, // 对话框宽度参数
            height: 350, // 对话框高度参数
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#editCategoryForm').form('submit',{
                        url:"${rc.contextPath}/category/saveOrUpdateCategorySecond",
                        onSubmit:function(){
                        	var fid = $("#editCategoryForm_fid").combobox('getValue');
                        	var name = $("#editCategoryForm_name").val();
                        	var image = $("#categoryImage").val();
                        	if(fid == '' || fid == undefined || fid == null){
                        		$.messager.alert("提示","请选择一级分类","error");
        						return false;
                        	}else if($.trim(name) == ''){
                        		$.messager.alert("提示","请输入二级分类名称","error");
        						return false;
                        	}else if(strlen(name)>10){
                        		$.messager.alert("提示","二级分类名称字符长度超过限制","error");
        						return false;
                        	}
//							else if($.trim(image) == ''){
//                                $.messager.alert("提示","二级分类图片URL未填写","error");
//                                return false;
//							}
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

	$('#picDia').dialog({
		title:'又拍图片上传窗口',
		collapsible:true,
		closed:true,
		modal:true
	});

	var inputId;
	function picDialogOpen($inputId) {
		inputId = $inputId;
		$("#picDia").dialog("open");
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