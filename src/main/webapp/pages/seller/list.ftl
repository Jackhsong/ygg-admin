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

<div data-options="region:'center',title:'content'" style="padding:5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',title:'商家管理 --- ${listType}',split:true" style="height: 160px;">
				<div id="searchDiv" style="height: 50px;padding: 15px">
					<form id="searchForm" method="post" action="${rc.contextPath}/seller/exportSeller">
						<table>
							<tr>
								<td>商家ID：</td>
								<td><input id="searchSellerId" name="sellerId" value=""/></td>
								<td>真实商家名称：</td>
								<td><input id="searchRealName" name="realSellerName" value="" /></td>
								<td>页面展示名称：</td>
								<td><input id="searchName" name="sellerName" value="" /></td>
								<td>状态：</td>
								<td>
									<select id="isAvailable" name="isAvailable">
										<#if isAvailable?exists && isAvailable == 1>
											<option value="1">可用</option>
										<#else>
											<option value="0">停用</option>
										</#if>
									</select>
								</td>
								<td>是否使用商家后台：</td>
								<td>
									<select id="isOwner" name="isOwner">
										<option value="-1">--全部--</option>
										<option value="1">是</option>
										<option value="0">否</option>
									</select>
								</td>
							</tr>
							<tr>
								<td>类目:</td>
								<td> <input  name="categoryId" id="categoryId"/></td>
								<td>招商负责人:</td>
								<td><input type="text" id="responsibilityPerson" name ="responsibilityPerson"></td>
								<td>创建时间:</td>
								<td>
                                    <input value="" id="createTimeStart" name="createTimeStart" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                                    至
                                    <input value="" id="createTimeEnd" name="createTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								</td>
								<td>保证金状态</td>
                                <td>
                                    <select id="depositStatus" name="depositStatus" style="width:100px">
                                        <option value="-1">--全部--</option>
                                        <option value="0">未签协议</option>
                                        <option value="1">已签未缴纳</option>
                                        <option value="2">已缴纳</option>
                                    </select>
                                </td>
								<td><a id="searchBtn" onclick="searchSeller()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
								<td><a id="exportBtn" onclick="exportSeller()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出</a></td>
							</tr>
						</table>
					<form>				
				</div>
			</div>
		<div data-options="region:'center'" >
			<table id="s_data" ></table>
			
			<div id="addTip" class="easyui-dialog" style="width:300px;height:180px;padding:20px 20px;">
		            <form id="af" method="post">
		            <table cellpadding="5">
		                <tr>
		                    <td>负责人:</td>
		                    <td>
		                    	<input id="person" name=person/>
		                    </td>
		                </tr>
		            </table>
		        </form>
		   </div>
		   
			<div id="editPwd_div" class="easyui-dialog" style="width:350px;height:200px;padding:20px 20px;">
		            <form id="editPwd_form" method="post">
					<input id="editId" type="hidden" name="id" value="" >
		            <table cellpadding="5">
		                <tr>
		                    <td>新密码:</td>
		                    <td><input type="password" id="pwd" name="pwd"></input></td>
		                </tr>    
		                <tr>
		                    <td>确认新密码:</td>
		                    <td><input type="password" id="pwd1" name="pwd1"></input></td>
		                </tr>
		            </table>
		        </form>
		     </div> 		   		
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

    $("#categoryId").combobox({
        url: '${rc.contextPath}/category/jsonCategoryFirstCode?isAvailable=1&zeroNeed=0',
        valueField: 'code',
        textField: 'text',
        multiple:true,
        editable:false,
    });

	function searchSeller() {
		var category = $("#categoryId").combobox('getValues');
		var ids = category.join(",");
		$('#s_data').datagrid('load', {
			id:$("#searchSellerId").val(),
			name : $("#searchName").val(),
			realName : $("#searchRealName").val(),
			isAvailable : $("#isAvailable").val(),
			isOwner:$("#isOwner").val(),
            categoryId : ids,
            responsibilityPerson : $("#responsibilityPerson").val(),
			createTimeStart : $("#createTimeStart").val(),
			createTimeEnd : $("#createTimeEnd").val(),
            depositStatus : $("#depositStatus").val()
		});
	}

	function exportSeller(){
		$("#searchForm").submit();
	}
	function editIt(index){
    	var arr=$("#s_data").datagrid("getData");
    	var urlStr = "${rc.contextPath}/seller/edit/"+arr.rows[index].id;
    	window.open(urlStr,"_blank");
	}
	
	function updatePassword(id){
		$("#editId").val(id);
		$("#pwd").val("");
		$("#pwd1").val("");
		$("#editPwd_div").dialog('open');
	}
	
	function availableIt(id,isAvailable){
		var tip = '';
		if(isAvailable == 0){
			tip = '确定停用吗？';
		}else{
			tip = '确定启用吗？';
		}
		$.messager.confirm("提示",tip,function(r){
			if(r){
				$.ajax({
					url:'${rc.contextPath}/seller/updateAvailableStatus',
					type:'POST',
					data:{id:id,isAvailable:isAvailable},
					dataType:'json',
					beforeSend:function(xhr){
						$.messager.progress();
					},
					success: function(data) {
						if(data.status == 1){
							$('#s_data').datagrid('clearSelections');
							$('#s_data').datagrid('reload');
				        } else{
				            $.messager.alert('响应信息',data.msg,'error');
				        }
					},
					error:function(xhr){
						$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
					},
					complete:function(xhr,ts){
						$.messager.progress('close');
					}
				});
			}
		});
	}
	
	function unlock(id) {
		$.messager.confirm("提示",'确定解锁吗',function(r){
			if(r){
				$.ajax({
					url:'${rc.contextPath}/seller/updateEcount',
					type:'POST',
					data:{id:id},
					dataType:'json',
					beforeSend:function(xhr){
						$.messager.progress();
					},
					success: function(data) {
						if(data.status == 1){
							$.messager.alert('响应信息','解锁成功','info');
				        } else{
				            $.messager.alert('响应信息',data.msg,'error');
				        }
					},
					error:function(xhr){
						$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
					},
					complete:function(xhr,ts){
						$.messager.progress('close');
					}
				});
			}
		});
	}

	$(function(){
		$('#addTip').dialog({
            title:'批量修改商家负责人',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                 	$.messager.progress();
                 	var person = $('#person').val();
                 	var rows = $('#s_data').datagrid("getSelections");
                    var ids = [];
                    for(var i=0;i<rows.length;i++){
                    	ids.push(rows[i].id)
                    }
                    $.post("${rc.contextPath}/seller/batchEditResponsibilityPerson", 
						{ids: ids.join(","),person:person},
						function(data){
							if(data.status == 1){
								$.messager.alert('提示',data.msg,"info")
								$.messager.progress('close');
								$('#addTip').dialog('close');
								$('#s_data').datagrid('load');
							}else{{
								$.messager.alert('提示',data.msg,"error")
								$.messager.progress('close');
							}}
						},
					"json");
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addTip').dialog('close');
                }
            }]
        });
		
		$('#editPwd_div').dialog({
            title:'修改密码',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var editId=$('#editId').val();
                	var pwd=$('#pwd').val();
                	var pwd1=$('#pwd1').val();
                	if(pwd != pwd1){
                		$.messager.alert('提示',"两次输入的密码不一致","error")
                	}else{
                		$.messager.progress();
                		$.ajax({
                			url:'${rc.contextPath}/seller/updatePassword',
                			type:'post',
                			data:{'sellerId':editId,'pwd':pwd,'pwd1':pwd1},
                			dataType:'json',
                			success:function(data){
                				$.messager.progress('close');
                				if(data.status == 1){
                					$.messager.alert('响应消息','修改成功','info');
                					$('#s_data').datagrid('clearSelections');
                					$('#s_data').datagrid('reload');
                					$('#editPwd_div').dialog('close');
                				}else{
                					$.messager.alert('响应消息',data.msg,'info');
                				}
                			},
                			error:function(xhr){
            					$.messager.progress('close');
            		            $.messager.alert("提示",'服务器忙，请稍后再试，errorCode='+xhr.status,"info");
            				}
                		});
                	}
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editPwd_div').dialog('close');
                }
            }]
        });
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/seller/jsonInfo',
            queryParams:{isAvailable:${isAvailable}},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:50, align:'center'},
                {field:'categories',    title:'类目', width:55, align:'center'},
                {field:'createTime',    title:'创建时间', width:55, align:'center'},
                {field:'sellerName',    title:'页面展示名称', width:70, align:'center'},
                {field:'realSellerName',    title:'真实商家名称', width:70, align:'center'},
                {field:'hidden1',    title:'负责人', width:65, align:'left',
                    formatter:function(value,row,index){
                        var a = "招商： " + row.responsibilityPerson + "<br/>";
                        var b = "审核： " + $.trim(row.auditUser) + "<br/>";
                        return  a+ b  ;
                    }
                },
//                {field:'responsibilityPerson',    title:'招商负责人', width:45, align:'center'},
//                {field:'auditUser',    title:'审核负责人', width:45, align:'center'},
                {field:'companyName',    title:'公司名称', width:70, align:'center'},
                {field:'hidden2',    title:'发货信息', width:70, align:'left',
                    formatter:function(value,row,index){
                        var a = "类型： " + row.sellerTypeString + "<br/>";
                        var b = "发货地： " + row.sendAddress + "<br/>";
                        return  a+ b  ;
                    }
                },
//                {field:'sellerTypeString',     title:'发货类型',  width:50,   align:'center' },
//                {field:'sendAddress',     title:'发货地',  width:50,   align:'center' },
//                {field:'warehouse',     title:'分仓',  width:50,   align:'center' },
//                {field:'weekendRemark',     title:'周末发货备注',  width:50,   align:'center' },
                {field:'sendTypeDesc',    title:'发货依据', width:35, align:'center'},
                {field:'isAvailable',    title:'状态', width:30, align:'center',
                	formatter:function(value,row,index){
                		if(row.isAvailable==1){
                			return '可用';
                		}else{
                			return '停用';
                		}
                	}	
                },
                {field:'isOwner',    title:'是否使用<br/>商家后台', width:30, align:'center',
                	formatter:function(value,row,index){
                		if(row.isOwner == 1){
                			return '是';
                		}else{
                			return '否';
                		}
                	}	
                },
                {field:'depositStatus',    title:'保证金状态', width:35, align:'center'},
                {field:'depositCount',    title:'保证金金额', width:35, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a>';
                        var b = ''
                        if(row.isOwner == 1){
                        	b = ' | <a href="javaScript:;" onclick="updatePassword('+row.id+')">修改密码</a>'
                        }
                        var c = '';
                        if(row.isAvailable == 1){
                        	c = ' | <a href="javaScript:;" onclick="availableIt('+row.id+','+0+')">停用</a> | <a href="javaScript:;" onclick="unlock('+row.id+')">解锁</a>'
                        }else{
                        	c = ' | <a href="javaScript:;" onclick="availableIt('+row.id+','+1+')">启用</a>'
                        }
                        return a + b + c;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商家信息',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href = "${rc.contextPath}/seller/add"
                }
            },'-',{
                iconCls: 'icon-edit',
                text:'批量添加招商负责人',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('添加温馨提示','确定批量操作吗',function(b){
                        	if(b){
                        		$("#addTip").dialog('open')
                        	}
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的商家',"error")
                    }
                }
            }],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>