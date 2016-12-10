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
		<div data-options="region:'north',title:'情景模版管理',split:true" style="height: 90px;">
			<br/>
			<div style="height: 60px;padding: 10px">
				<span>活动标题：</span>
				<span><input id="title" name="title"/></span>
				<span>是否可用：</span>
				<span>
					<select id="isAvailable" name="isAvailable">
	          			<option value="-1">全部</option>
	          			<option value="1">可用</option>
	          			<option value="0">停用</option>
	           		</select>
				</span>
				<span>
					<a id="searchBtn" onclick="searchActivity()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
					<a id="clearBtn" onclick="cleanActivity()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>&nbsp;
				</span>			
			</div>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editSpecialActivityDiv" class="easyui-dialog" style="width:550px;height:300px;padding:15px 20px;">
		        <form id="editSpecialActivityForm" method="post">
					<input id="editSpecialActivityForm_id" type="hidden" name="id" value="" >
					<p>
						<span>标题：</span>
						<span><input type="text" name="title" id="editSpecialActivityForm_title" value="" maxlength="32" style="width: 300px;"/></span>
						<font color="red">*</font>
					</p>
					<p>
						<span>图片：</span>
						<span><input type="text" name="image" id="editSpecialActivityForm_image" value="" maxlength="100" style="width: 300px;"/></span>
						<a onclick="picDialogOpen('editSpecialActivityForm_image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">*</font>
					</p>
					<p>
						<span>可用状态：</span>
						<span><input type="radio" name="isAvailable" id="editSpecialActivityForm_isAvailable1" checked="checked" value="1"/>可用&nbsp;&nbsp;</span>
						<span><input type="radio" name="isAvailable" id="editSpecialActivityForm_isAvailable0" value="0"/>停用</span>
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
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
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

	function searchActivity(){
    	$('#s_data').datagrid('load',{
    		title:$("#title").val(),
    		isAvailable:$("#isAvailable").val()
    	});
	}
	
	function cleanActivity(){
		$("#title").val('');
		$("#isAvailable").find("option").eq(0).attr("selected","selected");
	}
	
	function cleanEditSpecialActivityDiv(){
		$("#editSpecialActivityForm_id").val('');
		$("#editSpecialActivityForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editSpecialActivityForm_isAvailable1").prop('checked',true);
	}
	
	function editIt(index){
		$("input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		cleanEditSpecialActivityDiv();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var title = arr.rows[index].title;
		var image = arr.rows[index].image;
		var isAvailable = arr.rows[index].isAvailable;
    	$('#editSpecialActivityForm_id').val(id);
    	$('#editSpecialActivityForm_title').val(title);
    	$('#editSpecialActivityForm_image').val(image);
    	$("input[name='isAvailable']").each(function(){
    		if($(this).val()==isAvailable){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#editSpecialActivityDiv').dialog('open');
	}
	
	
	function availableIt(id,code){
		var tips = '';
		if(code == 1){
			tips = '确定启用吗？';
		}else{
			tips = '确定停用吗？'
		}
    	$.messager.confirm("提示信息",tips,function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/specialActivityModel/saveOrUpdate",{'id':id,'isAvailable':code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('reload');
                	} else{
                    	$.messager.alert('响应信息',data.msg,'error');
                	}
            	})
        	}
    	});
	}

	$(function() {
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/specialActivityModel/list',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:20, align:'center',checkbox:true},
                {field:'title',    title:'标题', width:50, align:'center'},
                {field:'image',    title:'图片', width:40, align:'center'},
                {field:'isAvailable',    title:'可用状态', width:30, align:'center',
                	formatter:function(value,row,index){
                		if(row.isAvailable == 1)
                			return '可用';
                		if(row.isAvailable == 0)
                			return '停用';
                		return row.isAvailable;
                	}
                },
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                		var a = '<a href="javascript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                        var b = '<a target="_blank" href="${rc.contextPath}/specialActivityModel/toLayoutList/' + row.id + '">管理板块</a> | ';
                        var c = '';
                        var d = '';
	                    if(row.isAvailable == 1){
	                     	d = '<a href="javaScript:;" onclick="availableIt(' + row.id + ',' + 0 + ')">停用</a> | ';
	                    }else if(row.isAvailable == 0) {
	                     	d = '<a href="javaScript:;" onclick="availableIt(' + row.id + ',' + 1 + ')">启用 </a> | ';
	                    }
	                    var e = '<a target="_blank" href="http://m.gegejia.com/ygg/special/newSceneWeb/'+ row.id +'">预览</a>';
	                   return a + b + c + d + e;
	                    	
	              }
              }
            ]],
            toolbar:[{
                id:'_add',
                text:'情景模版',
                iconCls:'icon-add',
                handler:function(){
                	cleanEditSpecialActivityDiv();
                	$('#editSpecialActivityDiv').dialog('open');
                }
            }],
            pagination:true
        });
		
		
	    $('#editSpecialActivityDiv').dialog({
	    	title:'情景模版',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editSpecialActivityForm').form('submit',{
	    				url: "${rc.contextPath}/specialActivityModel/saveOrUpdate",
	    				onSubmit:function(){
	    					var title = $("#editSpecialActivityForm_title").val();
	    					var image = $("#editSpecialActivityForm_image").val();
	    					if($.trim(title) == ''){
	    						$.messager.alert('提示','请填写标题','error');
	    						return false;
	    					}else if($.trim(image) == ''){
	    						$.messager.alert('提示','请上传图片','error');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#s_data').datagrid('load');
	                                $('#editSpecialActivityDiv').dialog('close');
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
	                $('#editSpecialActivityDiv').dialog('close');
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