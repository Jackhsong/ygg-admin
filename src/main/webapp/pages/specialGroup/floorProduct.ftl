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
<div data-options="region:'center',title:'情景模板管理-楼层商品'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 50px;">
			<div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
            	情景模板Id：<#if (activity.id)?exists>${activity.id?c}</#if>&nbsp;&nbsp;&nbsp;
                                     情景模板名称：<#if (activity.title)?exists>${activity.title}</#if>&nbsp;&nbsp;&nbsp;
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editActivityLayoutDiv" class="easyui-dialog" style="width:700px;height:600px;padding:15px 20px;">
		        <form id="editActivityLayoutForm" method="post">
					<input id="editActivityLayoutForm_saId" type="hidden" name="activityId" value="${activityId}" >
					<input id="editActivityLayoutForm_id" type="hidden" name="id" value="0" >
					<p>
						<span>&nbsp;布局方式：</span>
						<input type="radio" name="layoutType"  id="layoutType1" value="1" /> 一行1张&nbsp;&nbsp;&nbsp;
						<input type="radio" name="layoutType"  id="layoutType2" value="2" /> 一行2张
						<font color="red">必填</font>
					</p>
					<hr/>
                    <p>
                        <span>第一张备注：</span>
                        <span><input type="text" name="oneRemark" id="oneRemark" value="" maxlength="50" style="width: 300px;"/></span>
                        <font color="red">必填</font>
                    </p>
					<p>
						<span>第一张图片：</span>
						<span><input type="text" name="oneImageUrl" id="oneImageUrl" value="" maxlength="45" style="width: 300px;"/></span>
						<a onclick="picDialogOpen('oneImageUrl')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">*</font>
					</p>
					<p>
						<span>&nbsp;关联类型：</span>
						<input type="radio" name="oneType"  id="oneType1" value="1"/> 单品&nbsp;&nbsp;
    					<input type="radio" name="oneType"  id="oneType2" value="2"/> 组合&nbsp;&nbsp;
    					<input type="radio" name="oneType"  id="oneType3" value="3"/> 自定义活动&nbsp;&nbsp;
    					<input type="radio" name="oneType"  id="oneType4" value="4"/> 点击不跳转
    					<font color="red">必填</font>
					</p>
					<p id="oneDisplaySpan">
						<span>&nbsp;&nbsp;关联ID：</span>
						<span><input type="number" name="oneDisplayId" id="oneDisplayId" value="" maxlength="8" style="width: 300px;"/></span>
						<font color="red">必填</font><br/><br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="oneDisplayName" style="color:red"></span>
					</p>
					<div id="two_div">
					<hr/>
                    <p>
                        <span>第二张备注：</span>
                        <span><input type="text" name="twoRemark" id="twoRemark" value="" maxlength="50" style="width: 300px;"/></span>
                        <font color="red">必填</font>
                    </p>
					<p>
						<span>第二张图片：</span>
						<span><input type="text" name="twoImageUrl" id="twoImageUrl" value="" maxlength="45" style="width: 300px;"/></span>
						<a onclick="picDialogOpen('twoImageUrl')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">*</font>
					</p>
					<p>
						<span>&nbsp;关联类型：</span>
						<input type="radio" name="twoType"  id="twoType1" value="1"/> 单品&nbsp;&nbsp;
    					<input type="radio" name="twoType"  id="twoType2" value="2"/> 组合&nbsp;&nbsp;
    					<input type="radio" name="twoType"  id="twoType3" value="3"/> 自定义活动&nbsp;&nbsp;
    					<input type="radio" name="twoType"  id="twoType4" value="4"/> 点击不跳转
    					<font color="red">必填</font>
					</p>
					<p id="twoDisplaySpan">
						<span>&nbsp;&nbsp;关联ID：</span>
						<span><input type="number" name="twoDisplayId" id="twoDisplayId" value="" maxlength="8" style="width: 300px;"/></span>
						<font color="red">必填</font><br/><br/>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="twoDisplayName" style="color:red"></span>
					</p>
					</div>
					<hr/>
					<p>
						<span>展现状态：</span>
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
    	<input type="hidden" name="needWidth" id="needWidth" value="750">
        <input type="hidden" name="needHeight" id="needHeight" value="0">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script>

	function stringTrim(obj){
		$(obj).val($.trim($(obj).val()));	
	}
	
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
			url: '${rc.contextPath}/specialGroup/updateSpecialGroupActivityProductSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load',{
						activityId:${activityId},
						type:${type}
					});
		        }
				else
				{
                    $.messager.alert("提示",data.msg,"error");
		        }
			},
            error: function(xhr){
                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
            }
		});
	};
	<!--编辑排序相关 end-->
	
	<!-- 删除 -->
	function deleteIt(id){
		$.messager.confirm('提示', '确定删除吗?', function(r){
			if (r){
				$.ajax({
					url: '${rc.contextPath}/specialGroup/deleteSpecialGroupActivityProduct',
					type:"POST",
					data: {id:id},
					success: function(data) {
						if(data.status == 1){
							$('#s_data').datagrid('load',{
								activityId:${activityId},
								type:${type}
							});
				        }
						else
						{
		                    $.messager.alert("提示",data.msg,"error");
				        }
					},
		            error: function(xhr){
		                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		            }
				});	
			}
		});
	}
	
	function displayIt(id,isDisplay){
		var tip = '';
		if(isDisplay == 1){
			tip = "确定展现吗？";
		}else{
			tip = "确定不展现吗？";
		}
		$.messager.confirm('提示',tip,function(r){
			$.ajax({
				url: '${rc.contextPath}/specialGroup/updateSpecialGroupActivityProductDisplay',
				type:"POST",
				data: {id:id,isDisplay:isDisplay},
				success: function(data) {
					if(data.status == 1){
						$('#s_data').datagrid('load',{
							activityId:${activityId},
							type:${type}
						});
			        }
					else
					{
	                    $.messager.alert("提示",data.msg,"error");
			        }
				},
	            error: function(xhr){
	                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	            }
			});			
		});
	}
	
	function clealActivityLayoutDiv(){
		$("#editActivityLayoutForm_id").val('0');
		$("input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		$("#editActivityLayoutForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editActivityLayoutForm input[type='number']").each(function(){
			$(this).val('');
		});
		$("#editActivityLayoutForm_isDisplay1").prop('checked',true);
		$('#oneDisplayName').text('');
		$('#twoDisplayName').text('');
	}
	function editIt(index){
		clealActivityLayoutDiv();
		var arr=$("#s_data").datagrid("getData");
		
		var id = arr.rows[index].id;
		var layoutType = arr.rows[index].layoutType;
		var oneRemark = arr.rows[index].oneRemark;
		var oneImageUrl = arr.rows[index].oneImageUrl;
		var oneType = arr.rows[index].oneType;
		var oneDisplayId = arr.rows[index].oneDisplayId;
		var twoRemark = arr.rows[index].twoRemark;
		var twoImageUrl = arr.rows[index].twoImageUrl;
		var twoType = arr.rows[index].twoType;
		var twoDisplayId = arr.rows[index].twoDisplayId;
		var isDisplay = arr.rows[index].isDisplay;
		
    	$('#editActivityLayoutForm_id').val(id);
    	$("input[name='layoutType']").each(function(){
    		if($(this).val() == layoutType){
    			$(this).prop('checked',true);
    		}
    	});
   		if(layoutType == 1){
   			$("#two_div").hide();
   		}else if(layoutType == 2){
   			$("#two_div").show();
   		}
    	
    	$('#oneRemark').val(oneRemark);
    	$('#oneImageUrl').val(oneImageUrl);
    	$("input[name='oneType']").each(function(){
    		if($(this).val() == oneType){
    			$(this).prop('checked',true);
    		}
    	});
    	
   		if(oneType == 4){
   			$("#oneDisplaySpan").hide();
   		}else{
   			$("#oneDisplaySpan").show();
   		}
    	$("#oneDisplayId").val(oneDisplayId);
    	
    	$('#twoRemark').val(twoRemark);
    	$('#twoImageUrl').val(twoImageUrl);
    	$("input[name='twoType']").each(function(){
    		if($(this).val() == twoType){
    			$(this).prop('checked',true);
    		}
    	});
   		if(twoType == 4){
   			$("#twoDisplaySpan").hide();
   		}else{
   			$("#twoDisplaySpan").show();
   		}
    	$("#twoDisplayId").val(twoDisplayId);
    	
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==isDisplay){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#editActivityLayoutDiv').dialog('open');
	}
	
	$(function(){
		
		$("#layoutType1").change(function(){
			if($(this).is(':checked')){
				$("#two_div").hide();
				$("#twoRemark").val("");
				$("#twoImageUrl").val("");
				$("#twoType1").prop('checked',true);
				$("#twoDisplayId").val('0');
			}
		});
		$("#layoutType2").change(function(){
			if($(this).is(':checked')){
				$("#two_div").show();
				$("#twoRemark").val("");
				$("#twoImageUrl").val("");
				$("#twoType1").prop('checked',false);
				$("#twoDisplayId").val('');
			}
		});
		
		$("#oneType1").change(function(){
			if($(this).is(':checked')){
				$("#oneDisplayId").val('');
				$("#oneDisplaySpan").show();
			}
		});
		
		$("#oneType2").change(function(){
			if($(this).is(':checked')){
				$("#oneDisplayId").val('');
				$("#oneDisplaySpan").show();
			}
		});
		
		$("#oneType3").change(function(){
			if($(this).is(':checked')){
				$("#oneDisplayId").val('');
				$("#oneDisplaySpan").show();
			}
		});
		
		$("#oneType4").change(function(){
			if($(this).is(':checked')){
				$("#oneDisplayId").val('0');
				$("#oneDisplaySpan").hide();
			}
		});
		
		$("#twoType1").change(function(){
			if($(this).is(':checked')){
				$("#twoDisplayId").val('');
				$("#twoDisplaySpan").show();
			}
		});
		
		$("#twoType2").change(function(){
			if($(this).is(':checked')){
				$("#twoDisplayId").val('');
				$("#twoDisplaySpan").show();
			}
		});
		
		$("#twoType3").change(function(){
			if($(this).is(':checked')){
				$("#twoDisplayId").val('');
				$("#twoDisplaySpan").show();
			}
		});
		
		$("#twoType4").change(function(){
			if($(this).is(':checked')){
				$("#twoDisplayId").val('0');
				$("#twoDisplaySpan").hide();
			}
		});
		
		$("#layoutType1").change();
		$("#layoutType2").change();
		$("#oneType1").change();
		$("#oneType2").change();
		$("#oneType3").change();
		$("#oneType4").change();
		$("#twoType1").change();
		$("#twoType2").change();
		$("#twoType3").change();
		$("#twoType4").change();
		
		$("#oneDisplayId").change(function(){
			var type = $("input[name='oneType']:checked").val();
			var id = $.trim($(this).val());
			if(id == ""){
				$('#oneDisplayName').text('');
			}else{
				if(type == null || type == undefined){
					$.messager.alert("提示",'请选择第一张关联类型',"error");
					return;
				}else if(type == 1){
					$.ajax({
		                url: '${rc.contextPath}/product/findProductInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	$('#oneDisplayName').text(data.msg);
		                    }else{
	                            $.messager.alert("提示",data.msg,"error");
		                    }
		                },
		                error: function(xhr){
	                        $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
		                }
		            });
				}else if(type == 2){
					$.ajax({
		                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	$('#oneDisplayName').text(data.name + "-" + data.remark);
		                    }else{
		                    	$.messager.alert("提示",data.msg,"info");
		                    }
		                },
		                error: function(xhr){
		                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		                }
		            });
				}else if(type == 3){
					$.ajax({
		                url: '${rc.contextPath}/customActivities/findCustomActivitiesInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	$('#oneDisplayName').text(data.name + "-" + data.remark);
		                    }else{
		                    	$.messager.alert("提示",data.msg,"info");
		                    }
		                },
		                error: function(xhr){
		                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		                }
		            });					
				}else{
					$('#oneDisplayName').text('');
				}
			}
		});
		
		$("#twoDisplayId").change(function(){
			var type = $("input[name='twoType']:checked").val();
			var id = $.trim($(this).val());
			if(id == ""){
				$('#twoDisplayName').text('');
			}else{
				if(type == null || type == undefined){
					$.messager.alert("提示",'请选择第二张关联类型',"error");
					return;
				}else if(type == 1){
					$.ajax({
		                url: '${rc.contextPath}/product/findProductInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	$('#twoDisplayName').text(data.msg);
		                    }else{
	                            $.messager.alert("提示",data.msg,"error");
		                    }
		                },
		                error: function(xhr){
	                        $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
		                }
		            });
				}else if(type == 2){
					$.ajax({
		                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	$('#twoDisplayName').text(data.name + "-" + data.remark);
		                    }else{
		                    	$.messager.alert("提示",data.msg,"info");
		                    }
		                },
		                error: function(xhr){
		                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		                }
		            });
				}else if(type == 3){
					$.ajax({
		                url: '${rc.contextPath}/customActivities/findCustomActivitiesInfoById',
		                type: 'post',
		                dataType: 'json',
		                data: {'id':id},
		                success: function(data){
		                    if(data.status == 1){
		                    	$('#twoDisplayName').text(data.name + "-" + data.remark);
		                    }else{
		                    	$.messager.alert("提示",data.msg,"info");
		                    }
		                },
		                error: function(xhr){
		                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		                }
		            });					
				}else{
					$('#twoDisplayName').text('');
				}
			}
		});

		
		<!--列表数据加载-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/specialGroup/jsonFloorProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{activityId:${activityId},type: ${type}},
            pageSize:50,
            columns:[[
                {field:'isDisplay',    title:'展现状态', width:20, align:'center',
                	formatter:function(value,row,index){
                		if(row.isDisplay == 1){
                			return '展现';
                		}else{
                			return '不展现';
                		}
                	}	
                },
                {field:'layoutType',    title:'布局方式', width:50, align:'center',
                	formatter:function(value,row,index){
                		if(row.layoutType == 1){
                			return '一行1张';
                		}else if(row.layoutType == 2){
                			return '一行2张';
                		}
                	}               		
                },
                {field:'oneRemark',    title:'第一张备注', width:50, align:'center'},
                {field:'twoRemark',    title:'第二张备注', width:50, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                        	var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return a + b;
                    	}else{
                            var a = '<a href="javascript:;" onclick="editrow(' + index + ')">改排序</a> | ';
                    		var b = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a> | ';
                    		var c = '<a href="javascript:;" onclick="deleteIt('+ row.id + ')">删除</a> | ';
                    		var d = '';
                    		if(row.isDisplay == 1){
                    			d = '<a href="javascript:;" onclick="displayIt('+ row.id + ',' + 0 +')">不展现</a>';
                    		}else{
                    			d = '<a href="javascript:;" onclick="displayIt('+ row.id + ',' + 1 +')">展现</a>';
                    		}
                    		return a  + b + c + d;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'back',
                text:'返回新情景专场',
                iconCls:'icon-back',
                handler:function(){
                	window.location.href = "${rc.contextPath}/specialGroup/list";
                }
            },'-',{
                id:'_add',
                text:'新增商品',
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
	    	title:'添加商品',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
                    var params = {};
                    params.id = $("#editActivityLayoutForm_id").val();
					params.type = 1;
                    params.activityId = $("#editActivityLayoutForm_saId").val();
                    params.layoutType = $("input[name='layoutType']:checked").val();
                    params.oneRemark = $("#oneRemark").val();
                    params.oneImageUrl = $("#oneImageUrl").val();
                    params.oneType = $("input[name='oneType']:checked").val();
                    params.oneDisplayId = $("#oneDisplayId").val();
                    params.twoRemark = $("#twoRemark").val();
                    params.twoImageUrl = $("#twoImageUrl").val();
                    params.twoType = $("input[name='twoType']:checked").val();
                    params.twoDisplayId = $("#twoDisplayId").val();
                    params.isDisplay = $("input[name='isDisplay']:checked").val();
                    if(params.layoutType == '' || params.layoutType == null || params.layoutType == undefined){
                        $.messager.alert("提示","请填选择布局方式","error");
                    }else if(params.oneRemark == ''){
                    	$.messager.alert("提示","请输入第一张备注","error");
                    }else if(params.oneImageUrl == ''){
                    	$.messager.alert("提示","请输上传第一张图片","error");
                    }else if(params.oneType == null || params.oneType == '' || params.oneType == undefined){
                    	$.messager.alert("提示","请选择第一张关联类型","error");
                    }else if(params.oneType<4 && params.oneDisplayId==''){
                    	$.messager.alert("提示","请输入第一张关联类型ID","error");
                    }else{
                    	var submit = true;
                    	if(params.layoutType == 1){
                    		params.twoRemark = '';
                            params.twoImageUrl = '';
                            params.twoType = 1;
                            params.twoDisplayId = 0;
                            submit = true;
                    	}else if(params.layoutType == 2){
                    		if(params.twoRemark == ''){
                            	$.messager.alert("提示","请输入第一张备注","error");
                            	submit = false;
                            	return false;
                            }else if(params.twoImageUrl == ''){
                            	$.messager.alert("提示","请输上传第一张图片","error");
                            	submit = false;
                            	return false;
                            }else if(params.twoType == null || params.twoType == '' || params.twoType == undefined){
                            	$.messager.alert("提示","请选择第一张关联类型","error");
                            	submit = false;
                            	return false;
                            }else if(params.twoType<4 && params.twoDisplayId==''){
                            	$.messager.alert("提示","请输入第一张关联类型ID","error");
                            	submit = false;
                            	return false;
                            }else{
                            	submit = true;
                            }
                    	}
                    	if(submit){
	                        $.messager.progress();
	                        $.ajax({
	                            url: '${rc.contextPath}/specialGroup/editSpecialGroupActivityProduct',
	                            type: 'post',
	                            dataType: 'json',
	                            data: params,
	                            success: function(data){
	                                $.messager.progress('close');
	                                if(data.status == 1){
	                                    $('#s_data').datagrid('load',{activityId:${activityId},type: ${type}});
	                                    $('#editActivityLayoutDiv').dialog('close');
	                                }else{
	                                    $.messager.alert("提示",data.msg,"error");
	                                }
	                            },
	                            error: function(xhr){
	                                $.messager.progress('close');
	                                $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                            }
	                        });
                    	}else{
                    		$.messager.alert("提示",'信息不完整',"info");
                    	}
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
	
	
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        });
    });

    var inputId;
    var selectType;
    function picDialogOpen($inputId) {
    	selectType = $("input[name='layoutType']:checked").val();
        inputId = $inputId;
        if(selectType == 1){
	    	$("#needWidth").val("750");
        }else if(selectType == 2){
        	$("#needWidth").val("375");
        }
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