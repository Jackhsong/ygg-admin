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
				<input type="hidden" id="modelId" value="${layout.modelId?string('#######')}">
					<div style="display:inline;!important">
						版块ID：<#if layout.id?exists>${layout.id?string('#######')}</#if>&nbsp;&nbsp;&nbsp;
						版块名称：<#if layout.title?exists>${layout.title}</#if>&nbsp;&nbsp;&nbsp;
					</div>
					<span>展示状态：</span>
					<input id="searchIsDisplay" name="isDisplay" class="easyui-combobox" style="width:100px;"/>
					<a id="searchBtn" onclick="searchLayoutProduct()" href="javascript:;" class="easyui-linkbutton"
					   data-options="iconCls:'icon-search'">查询</a>
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editActivityLayoutPorductDiv" class="easyui-dialog" style="width:640px;height:300px;padding:15px 20px;">
		        <form id="editActivityLayoutPorductForm" method="post">
					<input id="editActivityLayoutPorductForm_layoutId" type="hidden" name="layoutId" value="${layout.id?string('#######')}" >
					<input id="editActivityLayoutPorductForm_id" type="hidden" name="id" value="" >
					<p>
						<span>商品ID：</span>
						<span><input type="text" name="productId" id="editActivityLayoutPorductForm_productId" value="" maxlength="8" style="width: 300px;"/></span>
						<font color="red">*</font>
					</p>
					<p>
						<span>描&nbsp;&nbsp;述：</span>
						<span><textarea name="desc" id="editActivityLayoutPorductForm_desc"
										rows="4" cols="30" maxlength="100"></textarea></span>
						<span id="nowDescSize"></span>字,
						<font color="red"> 最多100字</font>
					</p>
					<p>
						<span>展现状态：</span>
						<span><input type="radio" name="isDisplay" id="editActivityLayoutPorductForm_isDisplay1" checked="checked" value="1"/>展现&nbsp;&nbsp;</span>
						<span><input type="radio" name="isDisplay" id="editActivityLayoutPorductForm_isDisplay0" value="0"/>不展现</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->

            <div id="quickAddFloorProductDialog" class="easyui-dialog" style="width:400px;height:300px;padding:15px 20px;" >
                <form id="channelProManageForm" method="post">
                    <input id="layoutId" type="hidden" name="layoutId" value="${layoutId}" >
					<p>默认排序值为取现有最大排序值依次递增;默认描述取格格说,格格说大于100字则设为""</p>
                    <p>
                        <span>填写商品Id (以英文逗号分割 )：</span>
                    </p>
                    <p><textarea name="ids" id="ids" cols="20" rows="5"></textarea></p>
                </form>
            </div>
		</div>
	</div>
</div>

<script>
    function searchLayoutProduct() {
        $("#s_data").datagrid("reload", {
            "isDisplay": $("#searchIsDisplay").combobox("getValue"),
			"layoutId": $('#editActivityLayoutPorductForm_layoutId').val()
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
			url: '${rc.contextPath}/specialActivityModel/saveOrUpdateLayoutProduct',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					var activityId = $('#editActivityLayoutPorductForm_layoutId').val();
//					$('#s_data').datagrid('load',{'activityId':activityId});
					$('#s_data').datagrid('reload',{'activityId':activityId});
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	// <!-- 编辑排序相关 -->

    $("#editActivityLayoutPorductForm_desc").keyup(function(){
        $("#nowDescSize").html($.trim($("#editActivityLayoutPorductForm_desc").val()).length);
	});

	$("#editActivityLayoutPorductForm_productId").change(function(){
        var productId = $("#editActivityLayoutPorductForm_productId").val();
		if(Number($.trim(productId)) <= 0) {
			return;
		}
        $.ajax({
            url: '${rc.contextPath}/product/findProductInfoById',
            type:"POST",
            data: {id:productId},
            success: function(data) {
                if(data.status == 1){
					$("#editActivityLayoutPorductForm_desc").val(data.name);
                    $("#nowDescSize").html($.trim($("#editActivityLayoutPorductForm_desc").val()).length);
                    return;
                } else{
                    $.messager.alert('响应信息',data.msg,'error');
                }
            }
        });
	});

	function clealActivityLayoutDiv(){
		$("#editActivityLayoutPorductForm_id").val('');
		$("#editActivityLayoutPorductForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editActivityLayoutPorductForm_desc").val('');
		$("#editActivityLayoutPorductForm_isDisplay1").prop('checked',true);
        $("#nowDescSize").html('');
	}
	function editIt(index){
		$("input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		clealActivityLayoutDiv();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var productId = arr.rows[index].productId;
		var layoutId = arr.rows[index].layoutId;
		var isDisplay = arr.rows[index].isDisplay;
		var desc = arr.rows[index].desc;
    	$('#editActivityLayoutPorductForm_id').val(id);
    	$('#editActivityLayoutPorductForm_layoutId').val(layoutId);
    	$('#editActivityLayoutPorductForm_productId').val(productId);
    	$('#editActivityLayoutPorductForm_desc').val(desc);
        $("#nowDescSize").html($.trim($("#editActivityLayoutPorductForm_desc").val()).length);
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==isDisplay){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#editActivityLayoutPorductDiv').dialog('open');
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
            	var activityId = $('#editActivityLayoutPorductForm_layoutId').val();
            	$.post("${rc.contextPath}/specialActivityModel/saveOrUpdateLayoutProduct",{id:id,isDisplay:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
//                        $('#s_data').datagrid('reload',{'activityId':activityId});
                        $('#s_data').datagrid('reload');
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
		var layoutId = $('#editActivityLayoutPorductForm_layoutId').val();

        $('#quickAddFloorProductDialog').dialog({
            title:'快速添加布局商品',
            collapsible: true,
            closed: true,
            modal: true,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function(){
                        $('#channelProManageForm').form('submit',{
                            url: "${rc.contextPath}/specialActivityModel/quickAddLayoutProduct",
                            onSubmit:function(){
                                var ids = $.trim($("#ids").val());
                                if(ids == ''){
                                    $.messager.alert('提示','请填写商品id','info');
                                    return false;
                                }
                            },
                            success: function(data){
                                $.messager.progress('close');
                                var res = eval("("+data+")");
                                if(res.status == 1){
                                    $.messager.alert('响应信息',"保存成功",'info',function(){
                                        $('#s_data').datagrid('load');
                                        $('#quickAddFloorProductDialog').dialog('close');
                                    });
                                } else if(res.status == 0){
                                    $.messager.alert('响应信息',res.message,'error');
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
                        $('#quickAddFloorProductDialog').dialog('close');
                    }
                }]
        });

		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/specialActivityModel/layoutProductList',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{'layoutId':layoutId,'isDisplay':1},
            pageSize:50,
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center'},
                {field:'productId',    title:'商品ID', width:30, align:'center',
                    formatter:function(value,row,index){
						return '<a href="${rc.contextPath}/product/edit/1/'+row.productId+'" target="_blank">'+row.productId+'</href>';
                    }
				},
                {field:'desc',    title:'描述', width:30, align:'center'},
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
                            var c = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var d = '';
                    		if(row.isDisplay == 1){
                            	d = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">不展现</a>';
                            }else{
                            	d = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">展现</a>';
                            }
                    		return a + c + d;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'back',
                text:'返回情景模版板块管理',
                iconCls:'icon-back',
                handler:function(){
                	window.location.href = "${rc.contextPath}/specialActivityModel/toLayoutList/" + $('#modelId').val();
                }
            },'-',{
                id:'_add',
                text:'新增板块商品',
                iconCls:'icon-add',
                handler:function(){
                	clealActivityLayoutDiv();
                	$('#editActivityLayoutPorductDiv').dialog('open');
                }
            },'-',{
                id:'_quick_add',
                text:'快速添加商品',
                iconCls:'icon-add',
                handler:function(){
                    $("#ids").val('');
                    $("#quickAddFloorProductDialog").dialog('open');
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
		
		
	    $('#editActivityLayoutPorductDiv').dialog({
	    	title:'情景模版板块',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editActivityLayoutPorductForm').form('submit',{
	    				url: "${rc.contextPath}/specialActivityModel/saveOrUpdateLayoutProduct",
	    				onSubmit:function(){
	    					var productId = $("#editActivityLayoutPorductForm_productId").val();
	    					var desc = $("#editActivityLayoutPorductForm_desc").val();
	    					if($.trim(productId) == ''){
	    						$.messager.alert('提示','请填写商品ID','error');
	    						return false;
	    					}
	    					if($.trim(desc) == ''){
	    						$.messager.alert('提示','请填写描述','error');
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
	                                $('#editActivityLayoutPorductDiv').dialog('close');
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
	                $('#editActivityLayoutPorductDiv').dialog('close');
	            }
	    	}]
	    });
				
	});
	
</script>

</body>
</html>