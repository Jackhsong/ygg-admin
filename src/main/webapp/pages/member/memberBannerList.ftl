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
		<div data-options="region:'north',title:'积分商城Banner管理',split:true"></div>
		
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


	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		window.location.href = "${rc.contextPath}/banner/edit/"+arr.rows[index].id
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
	
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/member/updateMemberBannerSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('clearSelections');
					$('#s_data').datagrid('reload');
                    return;
                } else{
                    $.messager.alert('响应信息',data.msg,'error');
                    return;
                }
			},
           	error: function(xhr){
				$.messager.progress('close');
				$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
			}
		});
	};
	
	function saverow(index){
    	$('#s_data').datagrid('endEdit', index);
	};

	function cancelrow(index){
    	$('#s_data').datagrid('cancelEdit', index);
	};
	
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
   	            $.ajax({
   	            	url:'${rc.contextPath}/member/updateMemberBannerDisplayStatus',
   	            	type:'post',
   	            	dataType:'json',
   	            	data:{'ids':id,"isDisplay":code},
   	            	success:function(data){
   	            		$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid('clearSelections');
							$('#s_data').datagrid("reload");
						}else{
							$.messager.alert("提示",data.msg,"error");
						}
   	            	},
   	            	error: function(xhr){
						$.messager.progress('close');
						$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
					}
   	            });            	
        	}
    	});
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/member/jsonMemberBannerInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'isDisplay',    title:'展现状态', width:20, align:'center',
                	formatter:function(value,row,index){
            			if(row.isDisplay ==1){
            				return '展现';
            			}else{
            				return '不展现';
            			}
            		}	
                },
                {field:'type',    title:'类型', width:30, align:'center',
                	formatter:function(value,row,index){
                		if(row.type == 1){
                			return '特卖单品';
                		}else if(row.type == 2){
                			return '通用专场';
                		}else if(row.type == 3){
                			return '自定义活动'
                		}else if(row.type == 4){
                			return '商城单品';
                		}else if(row.type == 5){
                			return '原生自定义页面';
                		}else if(row.type == 6){
                			return '图片点击不跳转';
                		}else if(row.type == 7){
                			return '积分特卖商品';
                		}else if(row.type == 8){
                			return '积分商城商品';
                		}
                	}	
                },
                {field:'displayId',    title:'对应ID', width:30, align:'center'},
                {field:'displayName',    title:'对应名称', width:60, align:'center'},
                {field:'image',    title:'BANNER图', width:40, align:'center',
                	formatter:function(value,row,index){
                		return '<a href="'+row.image+'" target="_blank"><img src="'+row.image+'" style="max-height:40px" alt=""/></a>';
                	}	
                },
                {field:'desc',     title:'banner描述',  width:50,   align:'center' },
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                    
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var a = '<a href="${rc.contextPath}/member/editMemberBanner/'+row.id+'" target="_blank">修改</a> | ';
                    		var b = '<a href="javascript:;" onclick="editrow(' + index + ')">编辑排序值</a> | ';
                        	var c = '';
                        	if(row.isDisplay == 1){
                        		c = '<a href="javaScript:;" onclick="displayIt(' + '\'' + row.id + '\',' + 0 + ')">不展现</a>';
                        	}else{
                        		c = '<a href="javaScript:;" onclick="displayIt(' + '\'' + row.id + '\',' + 1 + ')">展现</a>';
                        	}
                        	return b+a+c;
                    	}
                    }
                }
            ]],
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
            toolbar:[{
                id:'_add',
                text:'新增Banner',
                iconCls:'icon-add',
                handler:function(){
                	var url = '${rc.contextPath}/member/editMemberBanner/0'
                    window.open(url,'_blank');
                }
            }],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>