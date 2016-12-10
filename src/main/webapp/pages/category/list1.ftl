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
					<td>类别名称：</td>
					<td><input id="searchCategoryName" name="searchCategoryName" value="" /></td>
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
		    <div id="editCategoryDiv" class="easyui-dialog" style="width:700px;height:380px;padding:20px 20px;">
		        <form id="editCategoryForm" method="post">
					<input id="editCategoryForm_id" type="hidden" name="id" value="" >
					<p>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名称：</span>
						<span><input type="text" id="editCategoryForm_name" name="name" style="width:300px"  maxlength="20"></span>
					</p>
					<p>
						<span>&nbsp;&nbsp;商城首页图标：</span>
						<span>
							<input type="text" id="editCategoryForm_image1" name="image1" style="width:300px">
							<a onclick="picDialogOpen('editCategoryForm_image1')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">尺寸：120x100</font>
						</span>
					</p>
					<p>
						<span>提示性子分类文案：</span>
						<span>
							<input type="text" name="tag" id="editCategoryForm_tag" style="width:300px" maxlength="20">
						</span>
						<font color="red">多个用#号分割，最多两个</font>
					</p>
					<p>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;颜色：</span>
						<span>
							<input type="text" name="color" id="editCategoryForm_color" style="width:300px">
						</span>
					</p>
                    <a href=""> 查看</a>
					<p>
						<span>&nbsp;&nbsp;分类页面图标：</span>
						<span>
							<input type="text" id="editCategoryForm_image2" name="image2" style="width:300px">
							<a onclick="picDialogOpen('editCategoryForm_image2')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">尺寸：62x62</font>
						</span>
					</p>					
					<p>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;可用状态：</span>
						<span>
							<input type="radio" id="editCategoryForm_isAvailable1" name="isAvailable" value="1" checked="checked"/>可用&nbsp;&nbsp;&nbsp;
							<input type="radio" id="editCategoryForm_isAvailable0" name="isAvailable" value="0" />不可用
						</span>
					</p>
					<p>
						<span>&nbsp;在app中展现状态：</span>
						<span>
							<input type="radio" id="editCategoryForm_isShowInApp1" name="isShowInApp" value="1"/>展现&nbsp;&nbsp;&nbsp;
							<input type="radio" id="editCategoryForm_isShowInApp0" name="isShowInApp" value="0" checked="checked"/>不展现
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
			url: '${rc.contextPath}/category/updateCategoryFirstSequence',
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

	function searchCategory(){
		$('#s_data').datagrid('load',{
	    	name:$("#searchCategoryName").val(),
	    	isAvailable:$("#searchIsAvailable").val()
		});
	}
	
	function clearSearchForm(){
		$("#searchCategoryName").val('');
		$("#searchIsAvailable").find("option").eq(0).attr("selected","selected");
		$('#s_data').datagrid('load',{});
	}
	
	function clearEditCategoryForm(){
		$("#editCategoryForm_id").val('');
		$("#editCategoryForm input[type='text']").each(function(){
			$(this).val("");
		});
		$("#editCategoryForm_isAvailable1").prop("checked",true);
		$("#editCategoryForm_isShowInApp0").prop("checked",true);
		$("#editCategoryForm_color").combobox('clear');
	}
	
	function editIt(index){
		clearEditCategoryForm();
	    var arr=$("#s_data").datagrid("getData");
	    $("#editCategoryForm_id").val(arr.rows[index].id);
		$("#editCategoryForm_name").val(arr.rows[index].name);
		$("#editCategoryForm_image1").val(arr.rows[index].image1);
		$("#editCategoryForm_image2").val(arr.rows[index].image2);
		$("#editCategoryForm_tag").val(arr.rows[index].tag);
		var color = arr.rows[index].colorValue;
		var url = '${rc.contextPath}/category/jsonCategoryColorCode?color='+color;
		$('#editCategoryForm_color').combobox('reload',url);
		var isAvailable = arr.rows[index].isAvailable;
		var isShowInApp = arr.rows[index].isShowInApp;
		$("#editCategoryForm input[name='isAvailable']").each(function(){
			if($(this).val() == isAvailable){
				$(this).prop("checked",true);
			}
		});
		$("#editCategoryForm input[name='isShowInApp']").each(function(){
			if($(this).val() == isShowInApp){
				$(this).prop("checked",true);
			}
		});
	    $("#editCategoryDiv").dialog('open');
	}
	

	$(function(){
		
		$('#editCategoryForm_color').combobox({   
		    url:'${rc.contextPath}/category/jsonCategoryColorCode',   
		    valueField:'code',   
		    textField:'text',
		    editable:false
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/category/jsonCategoryFirstInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect: false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'index',    title:'ID', width:10, align:'center'},
                {field:'name',     title:'名称',  width:50,  align:'center'},
                {field:'isAvailableDesc',    title:'使用状态', width:20, align:'center'},
                {field:'imageURL1',    title:'商城首页图标', width:20, align:'center'},
                {field:'imageURL2',    title:'分类页面图标', width:20, align:'center'},
                {field:'tags',     title:'提示性子分类',  width:50,  align:'center'},
                {field:'colorName',     title:'颜色',  width:20,  align:'center'},
                {field:'isShowInAppDesc',    title:'是否在App中展现', width:20, align:'center'},
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
                text:'在APP中展现',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定展现吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryFirstStatus",
									{id: ids.join(","),isShowInApp:1},
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
                text:'在APP中不展现',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('提示','确定不展现吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryFirstStatus",
									{id: ids.join(","),isShowInApp:0},
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
                                $.post("${rc.contextPath}/category/updateCategoryFirstStatus",
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
                        $.messager.confirm('提示','确定停用吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/category/updateCategoryFirstStatus",
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
            title:'一级分类',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#editCategoryForm').form('submit',{
                        url:"${rc.contextPath}/category/saveOrUpdateCategoryFirst",
                        onSubmit:function(){
                        	var name = $("#editCategoryForm_name").val();
                        	var image1 = $("#editCategoryForm_image1").val();
                        	var image2 = $("#editCategoryForm_image2").val();
                        	var tag = $("#editCategoryForm_tag").val();
                        	var color = $("#editCategoryForm_color").combobox('getValue');
                        	if($.trim(name) == ''){
                        		$.messager.alert("提示","请输入名称","error");
        						return false;
                        	}else if($.trim(image1) == ''){
                        		$.messager.alert("提示","请上传商城首页图标","error");
        						return false;
                        	}else if($.trim(tag) == ''){
                        		$.messager.alert("提示","请提示提示性子分类文案","error");
        						return false;
                        	}else if($.trim(image2) == ''){
                        		$.messager.alert("提示","请上分类页面图标","error");
        						return false;
                        	}else if(color == '' || color == null || color == undefined){
                        		$.messager.alert("提示","请选择颜色","error");
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
        if($inputId == "editCategoryForm_image1"){
        	$('#needWidth').val(120);
        	$('#needHeight').val(100);
        }
        if($inputId == "editCategoryForm_image2"){
        	$('#needWidth').val(62);
        	$('#needHeight').val(62);
        }
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