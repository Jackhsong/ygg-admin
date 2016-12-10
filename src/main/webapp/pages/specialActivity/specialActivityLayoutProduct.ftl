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
<div data-options="region:'center',title:'情景特卖活动版块布局管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 50px;">
			<div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
            	版块Id：<#if layout.id?exists>${layout.id?c}</#if>&nbsp;&nbsp;&nbsp;
            	版块短名称：<#if layout.shortTitle?exists>${layout.shortTitle}</#if>&nbsp;&nbsp;&nbsp;
            	版块长名称：<#if layout.longTitle?exists>${layout.longTitle}</#if>&nbsp;&nbsp;&nbsp;
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>

			<div id="quickAddFloorProductDialog" class="easyui-dialog" style="width:400px;height:300px;padding:15px 20px;" >
				<form id="channelProManageForm" method="post">
					<input id="layoutId" type="hidden" name="layoutId" value="${layoutId}" >
					<p>快速添加左右布局的商品 关联类型为商品 </p>
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
			url: '${rc.contextPath}/special/updateSpecialActivityLayoutProductSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load',{
						layoutId:${layoutId}
					});
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	<!--编辑排序相关 end-->
	
	
	function displayIt(id,code){
		var tips = '';
		if(code == 1){
			tips = '确定设为展现吗？';
		}else{
			tips = '确定设为不展现吗？'
		}
    	$.messager.confirm("提示信息",tips,function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/special/updateSpecialActivityLayoutProductDisplayStatus",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('load',{
                        	layoutId:${layoutId}
                        });
                        return
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	})
	}	
	
	$(function(){
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
                            url: "${rc.contextPath}/special/quickAddLayoutProduct",
                            onSubmit:function(){
								var ids = $.trim($("#ids").val());
                                if(ids == ''){
                                    $.messager.alert('提示','请填写商品id','info');
                                    return false;
                                }
								$.messager.confirm("确定要创建吗",ids,function(r){
									if(!r) return false;
								});
                                $.messager.progress();
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
                        $('#quickAddFloorProductDialog').dialog('close');
                    }
                }]
        });

        <!--列表数据加载-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/special/jsonSpecialActivityLayoutProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{layoutId:${layoutId}},
            pageSize:50,
            columns:[[
                {field:'id',    title:'id', width:20, align:'center',checkbox:true},
                {field:'displayDesc',    title:'展现状态', width:20, align:'center'},
                {field:'layout',    title:'布局方式', width:30, align:'center'},
                {field:'leftDesc',    title:'左侧描述', width:80, align:'center'},
                {field:'rightDesc',    title:'右侧描述', width:80, align:'center'},
                {field:'singleDesc',    title:'单张描述', width:80, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:60,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var b = '<a href="${rc.contextPath}/special/toEdit/${layoutId}/' + row.id + '")>编辑</a> | ';
                    		var c = '';
                    		if(row.isDisplay == 1){
                            	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">设为不展现</a>';
                            }else{
                            	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">设为展现</a>';
                            }
                    		return a + b + c;
                    	}
                    }
                },
            ]],
            toolbar:[{
                id:'back',
                text:'返回情景特卖活动板块管理',
                iconCls:'icon-back',
                handler:function(){
                	window.location.href = "${rc.contextPath}/special/manageLayout/${saId}"
                }
            },'-',{
                id:'_add',
                text:'新增布局',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href = "${rc.contextPath}/special/toAdd/${layoutId}"
                }
            }
//				,'-',{
//                id:'_quick_add',
//                text:'快速添加商品',
//                iconCls:'icon-add',
//                handler:function(){
//					$("#ids").val('');
//					$("#quickAddFloorProductDialog").dialog('open');
//                }
//            }
			],
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