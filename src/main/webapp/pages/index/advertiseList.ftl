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
<style type="text/css">
span{
	font-size: 14px;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'首页广告管理',split:true" style="height: 90px; padding-left: 20px;">
			<br/>
			<span>当前状态：<font color="red"><#if advMap.value?exists && (advMap.value=="1") >已开启<#elseif advMap.value?exists && (advMap.value=="0") >已关闭</#if></font></span>&nbsp;&nbsp;
			<#if advMap.value?exists && (advMap.value=="1") >
			<a onclick="updateConfigStatus('${advMap.id}','0')" href="javascript:;" class="easyui-linkbutton">马上关闭</a>
			<#elseif advMap.value?exists && (advMap.value=="0") >
			<a onclick="updateConfigStatus('${advMap.id}','1')" href="javascript:;" class="easyui-linkbutton">马上开启</a>
			</#if>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		</div>
        
	</div>
</div>

<script>
	function updateConfigStatus(id, status){
		var tips = '';
		if(status == 1){
			tips = '确定开启吗？';
		}else{
			tips = '确定关闭吗？';
		}
		 $.messager.confirm("提示信息",tips,function(ra){
	        if(ra){
	           $.messager.progress();
	           $.post("${rc.contextPath}/index/updateConfigStatus",{id:id,status:status},function(data){
	               $.messager.progress('close');
	               if(data.status == 1){
	                   $.messager.alert('响应信息',"操作成功",'info',function(){
	                      window.location.reload();
	                   });
	               } else{
	                   $.messager.alert('响应信息',data.msg,'error');
	               }
	           });
	        }
	    });
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
			url: '${rc.contextPath}/index/updateAdvertiseSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					window.location.reload();
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
            	$.post("${rc.contextPath}/index/updateAdvertiseDisplayStatus",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                		window.location.reload();
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	});
	}
	
	function deleteIt(id){
		$.messager.confirm("提示信息","确定删除吗？",function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/index/deleteAdvertise",{id:id},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                		window.location.reload();
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	});
	}


	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/index/jsonAdvertiseInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center'},
                {field:'remark',    title:'备注', width:80, align:'center'},
                {field:'displayDesc',    title:'可见状态', width:30, align:'center'},
                {field:'image',    title:'图片URL', width:80, align:'center'},
                {field:'sequence',     title:'排序值',  width:30,   align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">修改排序值</a> | ';
                    		var b = '<a href="${rc.contextPath}/index/editAdv/' + row.id + '">编辑</a> | ';
                            var c = '';
                            if(row.isDisplay == 1){
	                        	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">设为不展现</a> | ';
	                        }else if(row.isDisplay == 0) {
	                        	c = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">设为展现</a> | ';
	                        }
                            var d = '<a href="javascript:;" onclick="deleteIt(' + row.id + ')">删除</a>'
	                        
	                        return a + b + c + d;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增广告',
                iconCls:'icon-add',
                handler:function(){
                	var canAdd = ${canAdd};
                	if(canAdd == 1){
	                	window.location.href="${rc.contextPath}/index/addAdv";
                	}else{
                		$.messager.alert('响应信息','已经存在3条广告，若要继续添加，请删除之前的数据再添加','error');
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