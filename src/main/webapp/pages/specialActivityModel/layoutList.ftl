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
<div data-options="region:'center',title:'情景模版板块管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 50px;">
			<div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
						情景模版ID：<#if activity.id?exists>${activity.id?string('#######')}</#if>&nbsp;&nbsp;&nbsp;
						情景模版标题：<#if activity.title?exists>${activity.title}</#if>&nbsp;&nbsp;&nbsp;
							<span>展示状态：</span>
							<input id="searchIsDisplay" name="isDisplay" class="easyui-combobox" style="width:100px;"/>
								<a id="searchBtn" onclick="searchLayout()" href="javascript:;" class="easyui-linkbutton"
								   data-options="iconCls:'icon-search'">查询</a>
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editActivityLayoutDiv" class="easyui-dialog" style="width:600px;height:300px;padding:15px 20px;">
		        <form id="editActivityLayoutForm" method="post">
					<input id="editActivityLayoutForm_saId" type="hidden" name="activityId" value="${activity.id?string('#######')}" >
					<input id="editActivityLayoutForm_id" type="hidden" name="id" value="" >
					<p>
						<span>标题：</span>
						<span><input type="text" name="title" id="editActivityLayoutForm_title" value="" maxlength="8" style="width: 300px;"/></span>
						<font color="red">*</font>
					</p>
					<p>
						<span>图片：</span>
						<span><input type="text" name="image" id="editActivityLayoutForm_image" value="" maxlength="100" style="width: 300px;"/></span>
						<a onclick="picDialogOpen('editActivityLayoutForm_image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">*</font>
					</p>
					<p>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;展现状态：</span>
						<span><input type="radio" name="isDisplay" id="editActivityLayoutForm_isDisplay1" checked="checked" value="1"/>展现&nbsp;&nbsp;</span>
						<span><input type="radio" name="isDisplay" id="editActivityLayoutForm_isDisplay0" value="0"/>不展现</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
		</div>
	</div>
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script>
    function searchLayout() {
        $("#s_data").datagrid("reload", {
            "isDisplay": $("#searchIsDisplay").combobox("getValue"),
            'activityId':$('#editActivityLayoutForm_saId').val()
        });
    }
	// <!-- 编辑排序相关 begin -->
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
			url: '${rc.contextPath}/specialActivityModel/saveOrUpdateLayout',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					var activityId = $('#editActivityLayoutForm_saId').val();
					$('#s_data').datagrid('load',{'activityId':activityId});
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	// <!-- 编辑排序相关 -->
	
	function clealActivityLayoutDiv(){
		$("#editActivityLayoutForm_id").val('');
		$("#editActivityLayoutForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editActivityLayoutForm_isDisplay1").prop('checked',true);
	}
	function editIt(index){
		$("input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		clealActivityLayoutDiv();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var title = arr.rows[index].title;
		var isDisplay = arr.rows[index].isDisplay;
		var image = arr.rows[index].image;
    	$('#editActivityLayoutForm_id').val(id);
    	$('#editActivityLayoutForm_title').val(title);
    	$('#editActivityLayoutForm_image').val(image);
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==isDisplay){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#editActivityLayoutDiv').dialog('open');
	}
	
	function displayIt(id,code){
		var tips = '';
		if(code == 1){
			tips = '确定设为展现吗？';
		}else{
			tips = '确定设为不展现吗？'
		}
		
    	$.messager.confirm("提示信息",tips,function(re) {
        	if(re) {
            	$.messager.progress();
            	var activityId = $('#editActivityLayoutForm_saId').val();
            	$.post("${rc.contextPath}/specialActivityModel/saveOrUpdateLayout",{id:id,isDisplay:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('load',{'activityId':activityId});
                        return
                	} else {
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	});
        	}
    	});
	}
	
	$(function() {
        // 搜索
        $('#searchIsDisplay').combobox({
            panelWidth: 50,
            panelHeight: 80,
            data: [
                {"code": -1, "text": "全部"},
                {"code": 1, "text": "展示", "selected": true},
                {"code": 0, "text": "不展示"},
            ],
            valueField: 'code',
            textField: 'text'
        });
		<!--列表数据加载-->
		var activityId = $('#editActivityLayoutForm_saId').val();
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/specialActivityModel/layoutList',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{'activityId':activityId, "isDisplay":1},
            pageSize:50,
            columns:[[
                {field:'id',    title:'id', width:20, align:'center'},
                {field:'title',    title:'标题', width:30, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'isDisplay',    title:'展现状态', width:30, align:'center',
                	formatter:function(value,row,index){
                		if (row.isDisplay == 1)
                			return '展示';
                		if (row.isDisplay == 0)
                			return '不展示';
                		
                	}
                },
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                    		var a = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a> | ';
                    		var b = '<a target="_blank" href="${rc.contextPath}/specialActivityModel/toLayoutProductList/' + row.id + '">管理布局</a> | '
                            var c = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var d = '';
                    		if(row.isDisplay == 1){
                            	d = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">不展现</a>';
                            }else{
                            	d = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">展现</a>';
                            }
                    		return a + b + c + d;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'back',
                text:'返回情景模版管理',
                iconCls:'icon-back',
                handler:function(){
                	window.location.href = "${rc.contextPath}/specialActivityModel/toList";
                }
            },'-',{
                id:'_add',
                text:'新增板块',
                iconCls:'icon-add',
                handler:function(){
                	clealActivityLayoutDiv();
                	$('#editActivityLayoutDiv').dialog('open');
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
		
		
	    $('#editActivityLayoutDiv').dialog({
	    	title:'情景模版板块',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editActivityLayoutForm').form('submit',{
	    				url: "${rc.contextPath}/specialActivityModel/saveOrUpdateLayout",
	    				onSubmit:function(){
	    					var title = $("#editActivityLayoutForm_title").val();
	    					var image = $("#editActivityLayoutForm_image").val();
	    					if($.trim(title) == ''){
	    						$.messager.alert('提示','请填写标题','error');
	    						return false;
	    					}
	    					if($.trim(image) == ''){
	    						$.messager.alert('提示','请上传图片','error');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#s_data').datagrid('load');
	                                $('#editActivityLayoutDiv').dialog('close');
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
	                $('#editActivityLayoutDiv').dialog('close');
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