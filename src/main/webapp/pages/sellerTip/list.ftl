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

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true,title:'商家公告管理'" >
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editSellerTipDiv" class="easyui-dialog" style="width:60%;height:80%;padding:15px 20px;">
		        <form id="editSellerTipForm" method="post">
		        	<input id="id" type="hidden" name="id" value="" >
		        	<table>
		        		<tr>
		        			<td align="right">标题：</td>
		        			<td><input type="text" name="title" id="title" maxlength="25" style="width:600px;"/></td>
		        		</tr>
		        		<tr id="imageUrl">
		        			<td align="right">上传图片：</td>
		        			<td>
			        			<input type="text" name="url" id="url" maxlength="255" style="width:600px;" readonly="readonly"/>&nbsp;&nbsp;<a class="easyui-linkbutton" onclick="picDialogOpen()">上传图片</a>
		        			</td>
		        		</tr>
		        		<tr>
		        			<td align="right">内容：</td>
		        			<td><textarea rows="25" cols="110" name="content" id="content"></textarea></td>
		        		</tr>
		        		<tr>
		        			<td align="right">备注：</td>
		        			<td><input type="text" name="remark" id="remark" maxlength="20" style="width:600px;"/></td>
		        		</tr>
		        		<tr>
		        			<td align="right">开启状态：</td>
		        			<td><input type="radio" name="status" value="1"/>启用<input type="radio" name="status" value="0"/>停用</td>
		        		</tr>
		        	</table>
		    	</form>
		    </div>
		    <!-- 新增 end -->
		</div>
	</div>
</div>
<div id="picDia" class="easyui-dialog" closed="true" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>
<script>

function picDialogOpen() {
	$('picForm').form('clear');
	$('#picDia').dialog('open');
}
//图片上传
function picUpload() {
	$.messager.progress();
    $('#picForm').form('submit', {
        url:"${rc.contextPath}/pic/fileUpLoad",
        async:false,
        success:function(data) {
        	$.messager.progress('close');
            var res = eval("("+data+")")
            if(res.status == 1) {
                $("#picDia").dialog("close");
                $('#url').val(res.url);
            } else{
                $.messager.alert('响应信息',res.msg,'error',function(){
                    return ;
                });
            }
        }
    });
}
	// 验证表单是否是否有效
	function valid() {
		if($('#title').val() == '') {
			$.messager.alert('警告','标题不能为空');
			return false;
		}
		if($('#content').val() == '') {
			$.messager.alert('警告','公告内容不能为空');
			return false;
		}
		if(typeof($('input[name=status]:checked').val()) == 'undefined') {
			$.messager.alert('警告', '公告开启状态不能为空');
			return false;
		}
		return true;
	}
	//编辑
	function edit(id) {
		cleanSellerTipDiv();
		$.messager.progress();
		$.ajax({
			url: '${rc.contextPath}/sellerTip/findById/' + id,
			type:"POST",
			success: function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					$('#editSellerTipForm').form('load',data.data);
					$('#editSellerTipDiv').dialog('open');
		        } else{
		        	$.messager.progress('close');
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			},
			error : function() {
				$.messager.progress('close');
			}
		});
	}
	// 清空dialog div
	function cleanSellerTipDiv(){
		$('#editSellerTipForm').form('clear');
	}
	$(function() {
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sellerTip/findListInfo',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:10, align:'center'},
                {field:'status',    title:'启用状态', width:20, align:'center', 
            		formatter : function(value, row, index) {
            			if(row.status == 1)
            				return '启动中';
            		}
                },
                {field:'title',    title:'标题', width:70, align:'center'},
                {field:'remark',    title:'备注', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:20, align:'center',
					formatter : function(value, row, index) {
						return '<a href="javascript:void(0);" onclick="edit(' + row.id + ')">修改</a>';
					}
				} ] ],
			toolbar : [ {
				id : '_add',
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					cleanSellerTipDiv();
					$('#editSellerTipDiv').dialog('open');
				}
			} ],
			pagination : true
		});

		$('#editSellerTipDiv').dialog({
			title : '新增商家公告',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					$('#editSellerTipForm').form('submit', {
						url : "${rc.contextPath}/sellerTip/saveOrUpdate",
						onSubmit : function() {
							return valid();
						},
						success : function(data) {
							var res = eval("("+data+")");
							if (res.status == 1) {
								$.messager.alert('响应信息', "保存成功", 'info', function() {
									$('#s_data').datagrid('load');
									$('#editSellerTipDiv').dialog('close');
								});
							} else if (res.status == 0) {
								$.messager.alert('响应信息', res.msg, 'error');
							}
						}
					});
				}
			}, {
				text : '取消',
				align : 'left',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#editSellerTipDiv').dialog('close');
				}
			} ]
		});
	});
</script>

</body>
</html>