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
<div data-options="region:'center',title:'精品特卖活动版块管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',split:true" style="height: 50px;">
			<div id="searchDiv" style="height: 50px;padding: 15px;font-size: 15px;">
            	特卖Id：<#if activity.id?exists>${activity.id?c}</#if>&nbsp;&nbsp;&nbsp;
            	特卖名称：<#if activity.title?exists>${activity.title}</#if>&nbsp;&nbsp;&nbsp;
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editActivityLayoutDiv" class="easyui-dialog" style="width:600px;height:350px;padding:15px 20px;">
		        <form id="editActivityLayoutForm" method="post">
					<input id="editActivityLayoutForm_saId" type="hidden" name="activityId" value="${activityId}" >
					<input id="editActivityLayoutForm_id" type="hidden" name="id" value="" >
					<p>
						<span>&nbsp;标题：</span>
						<span><input type="text" name="title" id="editActivityLayoutForm_title" value="" maxlength="45" style="width: 300px;"/></span>
						<font color="red">*</font>
					</p>
					<p>
						<span>&nbsp;图片：</span>
						<span><input type="text" name="image" id="editActivityLayoutForm_image" value="" maxlength="15" style="width: 300px;"/></span>
						<a onclick="picDialogOpen('editActivityLayoutForm_image')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						<font color="red">不传则取商品头图</font>
					</p>
					<p>
						<span>&nbsp;描述：</span>
						<span><textarea id="editActivityLayoutForm_desc" name="desc" cols="40" rows="3"></textarea></span>&nbsp;(字数 &lt; 100)
						<font color="red">*</font>
					</p>
					<p>
						<span>&nbsp;商品：</span>
						<span><input type="text" name="productId" id="editActivityLayoutForm_productId" onblur="stringTrim(this);" value="" maxlength="8" style="width: 300px;"/></span>
						<font color="red">*</font><br/>
						&nbsp;&nbsp;&nbsp;<span id="productName" style="color:red"></span>
					</p>
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
    	<!-- <input type="hidden" name="needWidth" id="needWidth" value="640">
        <input type="hidden" name="needHeight" id="needHeight" value="640"> -->
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
			url: '${rc.contextPath}/simplify/updateActivitySimplifyProductSequence',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load',{
						activityId:${activityId}
					});
		            return
		        } else{
                    if(data.msg == undefined){
                        $.messager.alert("提示","无权限","error");
                    }else{
                        $.messager.alert("提示",data.msg,"error");
                    }
		        }
			}
		});
	};
	<!--编辑排序相关 end-->
	
	function clealActivityLayoutDiv(){
		$("#editActivityLayoutForm_id").val('');
		$("#editActivityLayoutForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editActivityLayoutForm_desc").val('');
		$("#editActivityLayoutForm_isDisplay1").prop('checked',true);
		$('#productName').text('');
	}
	function editIt(index){
		$("input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		clealActivityLayoutDiv();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var title = arr.rows[index].title;
		var image = arr.rows[index].image;
		var desc = arr.rows[index].desc;
		var productId = arr.rows[index].productId;
		var isDisplay = arr.rows[index].isDisplay;
    	$('#editActivityLayoutForm_id').val(id);
    	$('#editActivityLayoutForm_title').val(title);
    	$('#editActivityLayoutForm_image').val(image);
    	$('#editActivityLayoutForm_desc').val(desc);
    	$('#editActivityLayoutForm_productId').val(productId);
    	$("input[name='isDisplay']").each(function(){
    		if($(this).val()==isDisplay){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#editActivityLayoutDiv').dialog('open');
	}
	
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
            	$.post("${rc.contextPath}/simplify/updateActivitySimplifyProductDisplayStatus",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('load',{
                        	activityId:${activityId}
                        });
                        return
                	} else{
                        if(data.msg == undefined){
                            $.messager.alert("提示","无权限","error");
                        }else{
                            $.messager.alert("提示",data.msg,"error");
                        }
                	}
            	})
        	}
    	})
	}	
	
	$(function(){
		

		$("#editActivityLayoutForm_productId").change(function(){
			var id = $.trim($(this).val());
			var elementId = $(this).attr('id');
			if(id == ""){
				$("#productName").text("");
			}else{
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#productName').text(data.msg);
	                    }else{
							if(data.msg == undefined){
                                $.messager.alert("提示","无权限","error");
							}else{
                                $.messager.alert("提示",data.msg,"error");
							}

	                    }
	                },
	                error: function(xhr){
						if(xhr.status == 200)
						{
                            $.messager.alert("提示",'无权限',"error");
						}else{
                            $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
						}
	                }
	            });
			}
		});

		
		<!--列表数据加载-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/simplify/jsonActivitySimplifyProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            queryParams:{activityId:${activityId}},
            pageSize:50,
            columns:[[
                {field:'id',    title:'id', width:20, align:'center',checkbox:true},
                {field:'title',    title:'标题', width:50, align:'center'},
                {field:'productId',    title:'商品Id', width:20, align:'center'},
                {field:'imageURL',    title:'图片', width:20, align:'center'},
                {field:'desc',    title:'描述', width:80, align:'center'},
                {field:'sequence',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'displayDesc',    title:'展现状态', width:20, align:'center'},
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
                    		return a  + c + d;
                    	}
                    }
                }
            ]],
            toolbar:[{
                id:'back',
                text:'返回精品特卖活动管理',
                iconCls:'icon-back',
                handler:function(){
                	window.location.href = "${rc.contextPath}/simplify/list";
                }
            },'-',{
                id:'_add',
                text:'新增板块',
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
	    	title:'精品特卖活动板块',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editActivityLayoutForm').form('submit',{
	    				url: "${rc.contextPath}/simplify/saveOrUpdateActivitySimplifyProduct",
	    				onSubmit:function(){
	    					var title = $("#editActivityLayoutForm_title").val();
	    					var image = $("#editActivityLayoutForm_image").val();
	    					var desc =$("#editActivityLayoutForm_desc").val();
	    					var productId =$("#editActivityLayoutForm_productId").val();
	    					if($.trim(title) == ''){
	    						$.messager.alert('提示','请填写长标题','error');
	    						return false;
	    					}/* else if($.trim(image) == ''){
	    						$.messager.alert('提示','请填写短标题','error');
	    						return false;
	    					} */else if($.trim(desc) == ''){
	    						$.messager.alert('提示','请填写描述','error');
	    						return false;
	    					}else if(desc.length>100){
	    						$.messager.alert('提示','描述字数请控制在100字以内','error');
	    						return false;
	    					}else if(!/^[1-9][0-9]*$/.test(productId)){
	    						$.messager.alert('提示','商品Id必填且只能为数字','error');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息1',"保存成功",'info',function(){
	                                $('#s_data').datagrid('load');
	                                $('#editActivityLayoutDiv').dialog('close');
	                            });
	                        } else if(res.status == 0){
	                            $.messager.alert('响应信息2',res.msg,'error');
	                        } else{
                                $.messager.alert('响应信息2',"无权限",'error');
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
    function picDialogOpen($inputId) {
        inputId = $inputId;
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