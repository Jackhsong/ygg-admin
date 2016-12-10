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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'商品正品保证管理'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<!-- <div data-options="region:'north',title:'商品正品保证管理',split:true" style="height:100px;padding-top:10px">
			<table>
				<tr>
					<td>类别名称：</td>
					<td><input id="searchQualityPromiseName" name="searchQualityPromiseName" value="" /></td>
					<td>状态：</td>
					<td>
						<select id="searchIsAvailable" name="searchIsAvailable">
							<option value="-1">全部</option>
							<option value="1">可用</option>
							<option value="0">停用</option>
						</select>
					</td>
					<td><a id="searchBtn" onclick="searchQualityPromise()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
					<td><a id="searchBtn" onclick="clearSearchForm()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a></td>
				</tr>
			</table>
		</div> -->
		<div data-options="region:'center'" >
		    <!--数据表格-->
		    <table id="s_data" style=""></table>
		    
			<!-- 编辑分类begin -->
		    <div id="editQualityPromiseDiv" class="easyui-dialog" style="width:700px;height:300px;padding:20px 20px;">
		        <form id="editQualityPromiseForm" method="post">
					<input id="editQualityPromiseForm_id" type="hidden" name="id" value="" >
					<p>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;类型：</span>
						<span>
							<input type="radio" id="editQualityPromiseForm_type1" name="type" value="1"/>进口商品&nbsp;&nbsp;
							<input type="radio" id="editQualityPromiseForm_type2" name="type" value="2"/>国产商品
						</span>
					</p>
					<p>
						<span>&nbsp;上传正品保证图片：</span>
						<span>
							<input type="text" id="editQualityPromiseForm_image" name="image" style="width:300px">
							<a onclick="picDialogOpen('editQualityPromiseForm_image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">尺寸：750x234</font>
						</span>
					</p>
					<p>
						<span>链接到自定义页面Id：</span>
						<span>
							<input type="text" name="customPageId" id="editQualityPromiseForm_customPageId" onblur="stringTrim(this);" style="width:300px" maxlength="20">
						</span>
						<span id="pageName"></span>
					</p>
		    	</form>
		    </div>
		    <!-- 编辑分类end -->
		    
		</div>
	</div>
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <input type="hidden" name="needWidth" id="needWidth" value="750">
        <input type="hidden" name="needHeight" id="needHeight" value="234">
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script>
	
	function stringTrim(obj){
		$(obj).val($.trim($(obj).val()));	
	}

	function clearEditQualityPromiseForm(){
		$("#editQualityPromiseForm_id").val('');
		$("#editQualityPromiseForm input[type='text']").each(function(){
			$(this).val("");
		});
		$("#editQualityPromiseForm input[name='type']").each(function(){
			$(this).prop("checked",false);
		});
	}
	
	function editIt(index){
		clearEditQualityPromiseForm();
	    var arr=$("#s_data").datagrid("getData");
	    $("#editQualityPromiseForm_id").val(arr.rows[index].id);
		$("#editQualityPromiseForm_image").val(arr.rows[index].image);
		$("#editQualityPromiseForm_customPageId").val(arr.rows[index].customPageId);
		var type = arr.rows[index].type;
		$("#editQualityPromiseForm input[name='type']").each(function(){
			if($(this).val() == type){
				$(this).prop("checked",true);
			}
		});
	    $("#editQualityPromiseDiv").dialog('open');
	}

	$(function(){
		
		$("#editQualityPromiseForm_customPageId").change(function(){
			var id = $(this).val();
			if(id != 0){
				$.ajax({
	                url: '${rc.contextPath}/pageCustom/findPageCustom',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    	$("#pageName").text(data.msg);
	                },
	                error: function(xhr){
	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                }
	            });
			}
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/productBase/jsonQualityPromiseInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect: false,
            pageSize:50,
            columns:[[
                {field:'typeDesc',     title:'类型',  width:50,  align:'center'},
                {field:'url',    title:'自定义页面链接', width:50, align:'center'},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javascript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                   		var b = '<a href="'+row.url+'" target="_blank">预览</a>';
                   		return a + b;
                    }
                }
            ]],
            toolbar:[
            {
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                	clearEditQualityPromiseForm();
                	$('#editQualityPromiseDiv').dialog('open');
                }
            }],
         	pagination:true
        });
		
        $('#editQualityPromiseDiv').dialog({
            title:'正品保证',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#editQualityPromiseForm').form('submit',{
                        url:"${rc.contextPath}/productBase/saveOrUpdateQualityPromise",
                        onSubmit:function(){
                        	var type = $("#editQualityPromiseForm input[name='type']:checked").val();
                        	var image = $("#editQualityPromiseForm_image").val();
                        	var customPageId = $("#editQualityPromiseForm_customPageId").val();
                        	if(type == '' || type == undefined){
                        		$.messager.alert("提示","请选择类型","error");
        						return false;
                        	}else if($.trim(image) == ''){
                        		$.messager.alert("提示","请上图片","error");
        						return false;
                        	}else if(!/^\\d+$/.test(customPageId) == ''){
                        		$.messager.alert("提示","自定义页面Id不能为空且只能为数字","error");
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
                                $('#editQualityPromiseDiv').dialog('close');
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
                    $('#editQualityPromiseDiv').dialog('close');
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