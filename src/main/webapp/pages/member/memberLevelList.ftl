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
		<div data-options="region:'north',title:'会员等级体系管理',split:true">
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		</div>
        
	</div>
</div>

<script>
	
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
   	            	url:'${rc.contextPath}/member/updateMemberLevelDisplayStatus',
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
            url:'${rc.contextPath}/member/jsonMemberLevelInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
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
                {field:'level',    title:'等级名称(APP展示)', width:70, align:'center',
                	formatter:function(value,row,index){
                		return "V"+row.level;
                	}	
                },
                {field:'betweenPrice',    title:'消费区间', width:50, align:'center',
                	formatter:function(value,row,index){
                		if(row.isLastLevel==1){
                			return row.lowPrice+"以上";
                		}else{
                			return row.lowPrice+"-"+row.highPrice;
                		}
                	}	
                },
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                    	
                    		var a = '<a href="${rc.contextPath}/member/manageProduct/'+row.id+'/'+row.level+'" target="_blank">管理商品</a> | ';
                            var b = '';
                            if(row.isDisplay == 1){
	                        	b = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 + ')">不展现</a>';
	                        }else if(row.isDisplay == 0) {
	                        	b = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 + ')">展现</a>';
	                        }
	                        return a + b;
                    }
                }
            ]],
            pagination:true
        });
		
	});
</script>

</body>
</html>