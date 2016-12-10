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
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
textarea{
	resize:none;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'客服常见问题解答管理',split:true">
			<!-- <div style="height: 40px;padding: 10px">
	            <table class="search">
	                <tr>
	                    <td class="searchName">功能入口备注：</td>
	                    <td class="searchText"><input id="searchRemark" name="searchRemark" value="" /></td>
	                    <td class="searchName">展现状态：</td>
	                	<td class="searchText">
	                		<select id="searchIsDisplay" name="searchIsDisplay">
	                			<option value="-1">全部</option>
	                			<option value="1">展现</option>
	                			<option value="0">不展现</option>
	                		</select>
	                	<td class="searchText">
							<a id="searchBtn" onclick="searchCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;
							<a id="clearBtn" onclick="clearCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
							&nbsp;
	                	</td>
	                </tr>
	            </table>
			</div> -->
		</div> 
		
		<div data-options="region:'center'" >
			<!--数据表格-->
			<table id="s_data" ></table>
			
			<!-- 编辑 -->
		    <div id="editProblemDiv" class="easyui-dialog" style="width:550px;height:400px;padding:20px 20px;">
		        <form id="editProblemForm" method="post">
					<input id="editProblemForm_id" type="hidden" name="id" value="0" >
					<p>
						<span>&nbsp;&nbsp;问题：</span>
						<span><textarea rows="5" cols="60" id="editProblemForm_question" name="question"></textarea></span>
					</p>
					<p>
						<span>&nbsp;&nbsp;解答：</span>
						<span><textarea rows="5" cols="60" id="editProblemForm_answer" name="answer"></textarea></span>
					</p>
					<p>
						<span>&nbsp;排序值：</span>
						<span>
							<input type="number" id="editProblemForm_sequence"  name="sequence" maxlength="5" style="width: 400px;">
						</span>
					</p>				
					<p>
						<span>是否展现：</span>
						<span>
							<input type="radio" id="editProblemForm_isShowInApp1" name="isDisplay" value="1" checked="checked"/>展现&nbsp;&nbsp;&nbsp;
							<input type="radio" id="editProblemForm_isShowInApp0" name="isDisplay" value="0"/>不展现
						</span>
					</p>
		    	</form>
		    </div>			 	    		
		</div>
	</div>
</div>

<script>

	function displayId(id,isAvailable){
		var tip = "";
		if(isAvailable == 0){
			tip = "确定不展现吗？";
		}else{
			tip = "确定展现吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
   	            $.messager.progress();
   	            $.ajax({
   	            	url:'${rc.contextPath}/customerProblem/updateCustomerProblemStatus',
   	            	type:'post',
   	            	dataType:'json',
   	            	data:{'id':id,"isDisplay":isAvailable},
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

	function editIt(index){
		clearEditCustomFunctionForm();
	    var arr=$("#s_data").datagrid("getData");
	    $("#editProblemForm_id").val(arr.rows[index].id);
	    $("#editProblemForm_question").val(arr.rows[index].question);
		$("#editProblemForm_answer").val(arr.rows[index].answer);
		$("#editProblemForm_sequence").val(arr.rows[index].sequence);
		$("input[name='isDisplay']").each(function(){
			if($(this).val() == arr.rows[index].isDisplay){
				$(this).prop('checked',true);
			}
		});
	    $("#editProblemDiv").dialog('open');		
	}
	
	function clearEditCustomFunctionForm(){
		$("#editProblemForm_id").val('');
		$("editProblemForm input[type='textarea']").each(function(){
			$(this).val('');
		});
		$("#editProblemForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editProblemForm input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
	}
	$(function(){
		
		<!--加载楼层商品列表 begin-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customerProblem/jsonCustomerProblemInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            columns:[[
            	{field:'isDisplay',    title:'展现状态', width:30, align:'center',
            		formatter:function(value,row,index){
            			if(parseInt(row.isDisplay)==1){
            				return '展现';
            			}else{
            				return '不展现';
            			}
            		}	
            	},
                {field:'question',    title:'问题', width:80, align:'center'},
                {field:'answer',    title:'解答', width:80, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center'},
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                   		var a = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a>';
                       	var b = '';
                       	if(row.isDisplay == 0){
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 1 + ')">展现</a>'
                       	}else{
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 0 + ')">不展现</a>'
                       	}
                       	return a+b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                	$('#editProblemDiv').dialog('open');
                }
            }],
            pagination:true
        });

        $('#editProblemDiv').dialog({
            title:'编辑',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#editProblemForm').form('submit',{
                        url:"${rc.contextPath}/customerProblem/saveOrUpdate",
                        onSubmit:function(){
                        	var question = $("#editProblemForm_question").val();
                        	var answer = $("#editProblemForm_answer").val();
                        	if($.trim(question) == ''){
                        		$.messager.alert("提示","请输入问题","error");
        						return false;
                        	}else if(question.length>50){
                        		$.messager.alert("提示","问题字数请控制在50字以内","error");
        						return false;
                        	}else if($.trim(answer) == ''){
                        		$.messager.alert("提示","请输入解答","error");
        						return false;
                        	}else if(answer.length>500){
                        		$.messager.alert("提示","解答请控制在500字以内","error");
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
                                $('#editProblemDiv').dialog('close');
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
                    $('#editProblemDiv').dialog('close');
                }
            }]
        });
});


</script>

</body>
</html>