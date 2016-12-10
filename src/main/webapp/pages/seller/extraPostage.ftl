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
<div data-options="region:'center',title:'偏远地区加运费管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 50px;">
			<div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
            	商家名称：${sellerName!""}&nbsp;&nbsp;&nbsp;
            	模版名称：${templateName!""}&nbsp;&nbsp;&nbsp;
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editActivityLayoutDiv" class="easyui-dialog" style="width:600px;height:350px;padding:15px 20px;">
				<input id="editId" type="hidden" value=""/>
				<p>
					<span>发货省份：</span>
					<span id="provinceName"></span>
				</p>
				<p>
					<span>是否加运费：</span>
					<input type="radio" id="isExtra" name="isExtra" value="1"/>是&nbsp;&nbsp;&nbsp;
					<input type="radio" id="isExtra" name="isExtra" value="0"/>否&nbsp;&nbsp;&nbsp;
				</p>
				<p>
					<span>加运费金额：</span>
					<input type="number" id="freightMoney" name="freightMoney"/>
				</p>
		    </div>
		    <!-- 新增 end -->
		</div>
	</div>
</div>

<script>
	
	function editIt(index){
		$("input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		$("#editId").val('');
		$("#provinceName").text('');
		$("#freightMoney").val('');
		
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var provinceCode = arr.rows[index].provinceCode;
		var provinceName = arr.rows[index].provinceName;
		var isExtra = arr.rows[index].isExtra;
		var freightMoney = arr.rows[index].freightMoney;
    	
		$('#editId').val(id);
    	$('#provinceName').text(provinceName);
    	
    	if(isExtra == 0){
    		$.ajax({
                url: '${rc.contextPath}/sellerDeliverArea/findCommonExtraPostage',
                type: 'post',
                dataType: 'json',
                data: {'provinceCode':provinceCode},
                success: function(data){
                	$("#freightMoney").val(data);
                },
                error: function(xhr){
                	$("#freightMoney").val('0');
                }
            });
    	}else{
    		$("#freightMoney").val(freightMoney);
    	}
    	
    	$("input[name='isExtra']").each(function(){
    		if($(this).val()==isExtra){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#editActivityLayoutDiv').dialog('open');
	}
	
	
	$(function(){
		
		<!--列表数据加载-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sellerDeliverArea/jsonExtraPostage',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{templateId:${templateId!"0"}},
            pageSize:50,
            columns:[[
                {field:'provinceName',    title:'发货省', width:80, align:'center'},
                {field:'isExtra',    title:'是否加运费', width:30, align:'center',
                	formatter:function(value,row,index){
                		if(row.isExtra==1){
                			return '是';
                		}else{
                			return '否';
                		}
                	}	
                },
                {field:'freightMoney',    title:'加运费金额', width:30, align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                   		return '<a href="javascript:;" onclick="editIt('+ index + ')">修改</a>';
                    }
                }
            ]],
            pagination:true
        });
		
		
	    $('#editActivityLayoutDiv').dialog({
	    	title:'修改',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			var id = $("#editId").val();
	    			var isExtra = $("input[name='isExtra']:checked").val();
	    			var freightMoney = $("#freightMoney").val();
	    			if(isExtra == null || isExtra == '' || isExtra == undefined){
	    				$.messager.alert("提示","请选择是否加运费",'error');
                		return false;
	    			}else if(freightMoney<0){
	    				$.messager.alert("提示","加运费必须大于0",'error');
                		return false;
	    			}else{
	    				$.messager.progress();
            			var param = {
            					'id':id,
            					'isExtra':isExtra,
            					'freightMoney':freightMoney
            			};
           				$.ajax({
           		            url: '${rc.contextPath}/sellerDeliverArea/updateExtraPostage',
           		            type: 'post',
           		            dataType: 'json',
           		            data: param,
           		            success: function(data){
           		            	$.messager.progress('close');
           		            	if(data.status == 1){
	           		            	$.messager.alert("提示",data.msg,"info",function(){
		           		            	$("#s_data").datagrid('reload');
	           		            		$('#editActivityLayoutDiv').dialog('close');
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
</script>

</body>
</html>