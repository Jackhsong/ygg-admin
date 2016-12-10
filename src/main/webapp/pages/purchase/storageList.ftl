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
	<div id="cc" class="easyui-layout" data-options="fit:true,title:'收货分仓管理'" >
		<input type="hidden" id="type" value="${type}" >
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		    <!-- 新增 begin -->
		    <div id="editStorageDiv" class="easyui-dialog" style="width:500px;height:300px;padding:15px 20px;">
		        <form id="editStorageForm" method="post">
					<p><input id="id" type="hidden" name="id" value="0" ></p>
					<p>
						<span>仓库名称：</span>
						<span><input type="text" name="name" id="name" maxlength="20" style="width: 200px;"/></span>
					</p>
					<p>
						<span>仓库类型：</span>
						<span>
							<input type="radio" name="type" value="1">食品
							<input type="radio" name="type" value="2">化妆品
							<input type="radio" name="type" value="3">食品化妆品通用
						</span>
					</p>
					<p>
						<span>仓库关联商家：</span>
						<span><input type="text" name="sellerId" id="sellerId" maxlength="20" style="width: 200px;"/></span>
					</p>
					<p>
						<span>仓库详细地址：</span>
						<span><input type="text" name="detailAddress" id="detailAddress" maxlength="100" style="width: 200px;"/></span>
					</p>
					<p>
						<span>交货入库联系人：</span>
						<span><input type="text" name="contactPerson" id="contactPerson" maxlength="20" style="width: 200px;"/></span>
					</p>
					<p>
						<span>联系方式：</span>
						<span><input type="text" name="contactPhone" id="contactPhone" maxlength="20" style="width: 200px;"/></span>
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
		if($('#name').val() == '') {
			$.messager.alert('警告','仓库名称');
			return false;
		}
		if(typeof($('input[name=type]:checked').val()) == 'undefined') {
			$.messager.alert('警告','仓库类型');
			return false;
		}
		if($('#detailAddress').val() == '') {
			$.messager.alert('警告','仓库详细地址');
			return false;
		}
		if($('#contactPerson').val() == '') {
			$.messager.alert('警告','交货入库联系人');
			return false;
		}
		if($('#contactPhone').val() == '') {
			$.messager.alert('警告','联系方式');
			return false;
		}
		return true;
	}
	//编辑
	function edit(id) {
		cleanStorageDiv();
		$.messager.progress();
		$.ajax({
			url: '${rc.contextPath}/purchase/findStorageById/' + id,
			type:"POST",
			success: function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					$('#editStorageForm').form('load',data.data);
					$('#sellerId').combobox('disable');
					$('#editStorageDiv').dialog('open');
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
	function cleanStorageDiv(){
		$("#editStorageForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editStorageForm input[type='radio']").each(function(){
			$(this).attr('checked', false);
		});
		$('#id').val('');
	}
	$(function() {
		$('#sellerId').combobox({
			url:'${rc.contextPath}/purchase/sellerId',
			valueField:'id',
			textField:'name',
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/purchase/storageListInfo?type='+$('#type').val(),
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'仓库编码', width:20, align:'left'},
                {field:'name',    title:'仓库名称', width:70, align:'center'},
                {field:'sellerName',    title:'关联换吧网络商家', width:70, align:'center'},
                {field:'detailAddress',    title:'仓库详细地址', width:70, align:'center'},
                {field:'contactPerson',    title:'交货入库联系人', width:40, align:'center'},
                {field:'contactPhone',  title:'联系方式',  width:30, align:'center'},
                {field:'hidden',  title:'操作', width:80, align:'center',
					formatter : function(value, row, index) {
						return '<a href="javascript:void(0);" onclick="edit(' + row.id + ')">修改</a>';
					}
				} ] ],
			toolbar : [ {
				id : '_add',
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					cleanStorageDiv();
					$('#sellerId').combobox('enable');
					$('#editStorageDiv').dialog('open');
				}
			} ],
			pagination : true
		});

		$('#editStorageDiv').dialog({
			title : '新增分仓',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					$('#editStorageForm').form('submit', {
						url : "${rc.contextPath}/purchase/saveOrUpdateStorage",
						onSubmit : function() {
							$('#sellerId').combobox('enable');
							var r = valid();
							if(!r)
								$('#sellerId').combobox('disable');
							return r;
						},
						success : function(data) {
							var res = eval("("+data+")");
							if (res.status == 1) {
								$.messager.alert('响应信息', "保存成功", 'info', function() {
									$('#s_data').datagrid('load');
									$('#editStorageDiv').dialog('close');
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
					$('#editStorageDiv').dialog('close');
				}
			} ]
		});
	});
</script>

</body>
</html>