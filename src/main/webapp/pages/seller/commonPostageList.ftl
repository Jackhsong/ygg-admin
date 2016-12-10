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
<style type="text/css">
	.alignLeft{
		margin-left: 65px;
	}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'通用偏远地区运费'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >

		<div data-options="region:'center'" >
		      <table id="s_data" ></table>
		      
		      <!-- 新增配送地区模版begin -->
		      <div id="addTemplateDiv" class="easyui-dialog" style="width:500px;height:200px;padding:10px 10px;">
		      		<p>
		      			<span>省份：</span>
		      			<input type="text" name="provinceCode" id="provinceCode" style="width: 400px;"/>
		      		</p>
		      		<p>
		      			<span>邮费：</span>
		      			<input type="number" id="freightMoney" name="freightMoney" style="width: 400px;" maxlength="30" />
		      		</p>
		      </div>
		      <!-- 新增配送地区模版end -->
  			  
		</div>
	</div>
</div>

<script>

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
			url: '${rc.contextPath}/sellerDeliverArea/updateCommonPostage',
			type:"POST",
			data: {id:row.id,freightMoney:row.freightMoney},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('reload');
		            return
		        } else{
		        	$.messager.alert('响应信息',data.msg,'error',function(){
		                return
		            });
		        }
			}
		});
	};
	
	$(function(){
		
		$('#provinceCode').combobox({
			url: '${rc.contextPath}/area/jsonProvinceCode',
			valueField: 'code',
			textField: 'text'
		});
		
		$('#addTemplateDiv').dialog({
            title:'新增',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var provinceCode = $("#provinceCode").combobox('getValue');
                	var freightMoney = $.trim($("#freightMoney").val());
                	if(provinceCode == null || provinceCode == '' || provinceCode == undefined){
                		$.messager.alert("提示","请选择省份",'info');
                		return false;
                	}else if(freightMoney == ''){
                		$.messager.alert("提示","请输入邮费",'info');
                		return false;
                	}else{
            			$.messager.progress();
            			var param = {
            					'provinceCode':provinceCode,
            					'freightMoney':freightMoney
            			};
           				$.ajax({
           		            url: '${rc.contextPath}/sellerDeliverArea/addCommonPostage',
           		            type: 'post',
           		            dataType: 'json',
           		            data: param,
           		            success: function(data){
           		            	$.messager.progress('close');
           		            	if(data.status == 1){
	           		            	$.messager.alert("提示",data.msg,"info",function(){
		           		            	$("#s_data").datagrid('reload');
	           		            		$('#addTemplateDiv').dialog('close');
	           		            	});
           		            	}else{
           		            		$.messager.alert("提示",data.msg,"info");
           		            	}
           		            },
           		            error: function(xhr){
           		            	$.messager.progress('close');
           		            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
           		            }
           		        });
            		}
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addTemplateDiv').dialog('close');
                }
            }]
        });
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sellerDeliverArea/jsonCommonPostage',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            columns:[[
            	{field:'provinceName',    title:'省份', width:80, align:'center'},
				{field:'freightMoney',    title:'需加运费', width:80, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                    		return '<a href="javascript:;" onclick="editrow(' + index + ')">修改</a>';
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                	$('#addTemplateDiv').dialog('open');
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