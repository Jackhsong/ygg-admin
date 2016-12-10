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
	<div id="cc" class="easyui-layout" data-options="fit:true,title:'退款原因管理'" >
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="refundReasonDiv" class="easyui-dialog" style="width:500px;height:300px;padding:15px 20px;">
		        <form id="refundReasonForm" method="post">
					<p><input id="id" type="hidden" name="id" value="0" ></p>
					<p>
						<span>退款原因：</span>
						<span><input type="text" name="reason" id="reason" maxlength="30" style="width: 200px;"/></span>
					</p>
					<p>
						<span>状态：</span>
						<span><input type="radio" name="isAvailable" value="1"/>可用</span>
						<span><input type="radio" name="isAvailable" value="0"/>停用</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
		</div>
	</div>
</div>

<script>
	// 验证表单是否是否有效
	function valid() {
		if($('#reason').val() == '') {
			$.messager.alert('警告','退款原因不能为空');
			return false;
		}
		if(typeof($('input[name=isAvailable]:checked').val()) == 'undefined') {
			$.messager.alert('警告','状态不能为空');
			return false;
		}
		return true;
	}
	//编辑
	function edit(id,isAvailable) {
		$.messager.progress();
		$.ajax({
			url: '${rc.contextPath}/refundReason/update/' + id,
			data:{'isAvailable':isAvailable},
			type:"POST",
			success: function(data) {
				$.messager.progress('close');
				if(data.status != 1) {
		            $.messager.alert('响应信息',data.msg,'error');
		        } else {
		        	$('#s_data').datagrid('reload');
		        }
			},
			error : function() {
				$.messager.progress('close');
			}
		});
	}
	// 清空dialog div
	function cleanStorageDiv(){
		$('#reason').val('');
	}
	$(function() {
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/refundReason/findList',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'isAvailable',    title:'可用状态', width:20, align:'center',
            		formatter : function(value, row, index) {
            			if(row.isAvailable == 0)
            				return '停用';
            			if(row.isAvailable == 1)
            				return '可用';
						return '';
					}
            	},
                {field:'reason',    title:'退款原因', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:20, align:'center',
					formatter : function(value, row, index) {
						if(row.isAvailable == 0)
							return '<a href="javascript:void(0);" onclick="edit(' + row.id + ',1' + ')">可用</a>';
						if(row.isAvailable == 1)
							return '<a href="javascript:void(0);" onclick="edit(' + row.id + ',0' + ')">停用</a>';
					}
				}
            ] ],
			toolbar : [ {
				id : '_add',
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					cleanStorageDiv();
					$('#refundReasonDiv').dialog('open');
				}
			} ],
			pagination : true
		});

		$('#refundReasonDiv').dialog({
			title : '新增退款原因',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					$('#refundReasonForm').form('submit', {
						url : "${rc.contextPath}/refundReason/save",
						onSubmit : function() {
							return valid();
						},
						success : function(data) {
							var res = eval("("+data+")");
							if (res.status == 1) {
								$.messager.alert('响应信息', "保存成功", 'info', function() {
									$('#s_data').datagrid('load');
									$('#refundReasonDiv').dialog('close');
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
					$('#refundReasonDiv').dialog('close');
				}
			} ]
		});
	});
</script>

</body>
</html>