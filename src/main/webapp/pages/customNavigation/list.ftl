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
		<div data-options="region:'north',title:'自定义首页导航',split:true" style="height: 90px;">
			<form id="searchForm" method="post">
				<table cellpadding="15">
					<tr>
	                	<td>ID：</td>
						<td><input id="id" name="id" value=""/></td>
	                    <td>导航名称：</td>
	                    <td><input id="name" name="name" value="" /></td>
	                    <td>展现状态：</td>
		                <td><select id="isDisplay" name="isDisplay" class="easyui-combobox" style="width:150px;"><option value="-1">-全部-</option><option value="1" selected="selected">展示</option><option value="0">不展示</option></select></td>
		                <td>
							<a id="searchBtn" onclick="searchNavigationList()" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',width:'80px'">查 询</a>
	                	</td>
	                </tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    			
		    <!-- 新增 begin -->
		    <div id="editCustomNavigationDiv" class="easyui-dialog" style="width:500px;height:300px;padding:15px 20px;">
		        <form id="editCustomNavigationForm" method="post">
					<input id="customNavigationId" type="hidden" name="customNavigationId" value="" >
					<p>
						<span>导航名称：</span>
						<span><input type="text" name="name" id="customNavigationName" value="" maxlength="32" style="width: 200px;"/></span>
					</p>
					<p>
						<span>备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</span>
						<span><input type="text" name="remark" id="customNavigationRemark" value="" maxlength="100" style="width: 200px;"/></span>
					</p>
					<p>
						<span>关联类型：</span>
						<span><input type="radio" name="customNavigationType" value="1"/>组合&nbsp;&nbsp;</span>
						<span><input type="radio" name="customNavigationType" value="2"/>网页自定义活动&nbsp;&nbsp;</span>
						<span><input type="radio" name="customNavigationType" value="3"/>原生自定义活动&nbsp;&nbsp;</span>
					</p>
					<p id="activitiesCommon">
						<span>组合ID：</span>
						<span><input type="text" name="activitiesCommonId" id="activitiesCommonId" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="10" style="width: 200px;"/></span>
						<span style="color: red" id="activitiesCommonDesc"></span>
					</p>
					<p id="activitiesCustom">
						<span>自定义活动：</span>
						<span><input type="text" name="activitiesCustomId" id="activitiesCustomId" style="width: 200px;"/></span>
					</p>
					<p id="page">
						<span>原生自定义活动：</span>
						<span><input type="text" name="pageId" id="pageId" style="width: 200px;"/></span>
					</p>
					<p id="display">
						<span>可见状态：</span>
						<span><input type="radio" name="isDisplay" value="1"/>可见&nbsp;&nbsp;</span>
						<span><input type="radio" name="isDisplay" value="0"/>不可见</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->

            <div id="editArea_div">

            </div>
            <input type="hidden" value="${rc.contextPath}" id="contextPath">
		</div>
	</div>
</div>

<script>
    function editArea(id){
        var url = $("#contextPath").val() + "/customNavigation/editAreaForm?id=" + id;
        $('#editArea_div').dialog('refresh', url);
        $('#editArea_div').dialog('open');
    }
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

	// 验证表单是否是否有效
	function valid() {
		if($('#customNavigationName').val() == '') {
			$.messager.alert('警告','导航名称不能为空');
			return false;
		}
		if(typeof($('input[name=customNavigationType]:checked').val()) == 'undefined') {
			$.messager.alert('警告','关联类型不能为空');
			return false;
		}
		if($('input[name=customNavigationType]:checked').val() == 1) {
			if($('#activitiesCommonId').val() == '') {
				$.messager.alert('警告','组合ID不能为空');
				return false;
			}
		} else if($('input[name=customNavigationType]:checked').val() == 2) {
			if($('#activitiesCustomId').combobox('getValue') == '') {
				$.messager.alert('警告','自定义活动不能为空');
				return false;
			}
		} else if($('input[name=customNavigationType]:checked').val() == 3) {
			if($('#pageId').combobox('getValue') == '') {
				$.messager.alert('警告','原生自定义活动不能为空');
				return false;
			}
		}
		if($('#customNavigationId').val() > 0) {
			return true;
		}
		if(typeof($('input[name=isDisplay]:checked').val()) == 'undefined') {
			$.messager.alert('警告','可见状态不能为空');
			return false;
		}
		return true;
	}
	//编辑首页导航
	function edit(id) {
		cleanEditCustomNavigationDiv();
		$('#display').hide();
		$.messager.progress();
		$("#pageId").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage',
            editable : false,
            valueField:'code',
            textField:'text',
        });
		$("#activitiesCustomId").combobox({
			url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode',
			editable : false,
		    valueField:'code',   
		    textField:'text',
		});
		$.ajax({
			url: '${rc.contextPath}/customNavigation/findById?id=' + id,
			type:"POST",
			sync:false,
			success: function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					if(data.data.customNavigationType == 1) {
						$('#activitiesCommon').show();
						$('#activitiesCommonId').val(data.data.displayId);
					} else if(data.data.customNavigationType == 2) {
						$('#activitiesCustom').show();
						$('#activitiesCustomId').combobox('setValue', data.data.displayId);
					} else if(data.data.customNavigationType == 3) {
						$('#page').show();
						$('#pageId').combobox('setValue', data.data.displayId);
					}
					$('#editCustomNavigationForm').form('load',data.data);
					$('#editCustomNavigationDiv').dialog('open');
		            return
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
	// 搜索按钮
	function searchNavigationList() {
		$('#s_data').datagrid('load', {
			id:$('#id').val(),
			name:$('#name').val(),
			isDisplay:$('#isDisplay').combobox('getValue')
		});
	}
	// 编辑排序
	function editSequence(index){
		$('#s_data').datagrid('beginEdit', index);
	};
	// 保存保存
	function saverow(index){
		$('#s_data').datagrid('endEdit', index);
	};
	// 取消编辑
	function cancelrow(index){
		$('#s_data').datagrid('cancelEdit', index);
	};
	// 跟新gird行数据
	function updateActions(){
		var rowcount = $('#s_data').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_data').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}
	// 清空dialog div
	function cleanEditCustomNavigationDiv(){
		$("#customNavigationId").val('0');
		$("#editCustomNavigationForm input[type='text']").each(function(){
			$(this).val('');
		});
		$('input[name=customNavigationType]').each(function(i) {
			$(this).attr('checked', false);
		});
		$('input[name=isDisplay]').each(function(i) {
			$(this).attr('checked', false);
		});
		$('#activitiesCommonDesc').text('');
		$('#activitiesCommon').hide();
		$('#activitiesCustom').hide();
		$('#page').hide();
		$('#display').show();
	}
	// 跟新导航排序值
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/customNavigation/updateByParam',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	// 更新导航展示
	function editIsDisplay(id,isDisplay) {
		$.ajax({
			url: '${rc.contextPath}/customNavigation/updateByParam',
			type:"POST",
			data: {id:id,isDisplay:isDisplay},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	} 
	
	$(function(){
		// 填写组合ID后
		$('#activitiesCommonId').blur(function() {
			if($('#activitiesCommonId').val().length < 1)
				return;
			$.ajax({
                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
                type: 'POST',
                data: {id:$('#activitiesCommonId').val()},
                success: function(data){
                    if(data.status == 1){
                    	$('#activitiesCommonDesc').text(data.name + "-" + data.remark);
                    }else{
                    	$.messager.alert("提示",data.msg,"info");
                    }
                }
            });
		});
		// 选择类型展示不同的信息
		$('input[name=customNavigationType]').change(function() {
			var type = $('input[name=customNavigationType]:checked').val();
			if(type == 1) {
				$('#activitiesCommon').show();
				$('#activitiesCustom').hide();
				$('#page').hide();
			} else if(type == 2) {
				$('#activitiesCommon').hide();
				$('#activitiesCustom').show();
				$('#page').hide();
			} else if(type == 3) {
				$('#activitiesCommon').hide();
				$('#activitiesCustom').hide();
				$('#page').show();
			}
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customNavigation/info',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:20, align:'center'},
                {field:'isDisplay',    title:'展现状态', width:70, align:'center',
            		formatter:function(value,row,index){
 						if(row.isDisplay == 1)
 							return '展示';
 						else if(row.isDisplay == 0)
 							return '不展示';
 						else 
 							return '';
                	}},
                {field:'name',    title:'名称', width:70, align:'center'},
                {field:'remark',    title:'备注', width:40, align:'center'},
                {field:'supportAreaTypeStr',    title:'展示地区', width:70, align:'center'},
                {field:'sequence',     title:'排序值',  width:30, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:80, align:'center',
					formatter : function(value, row, index) {
						if (row.editing) {
							var a = '<a href="javascript:void(0);" onclick="saverow(' + index + ')">保存</a> | ';
							var b = '<a href="javascript:void(0);" onclick="cancelrow(' + index + ')">取消</a>';
							return a + b;
						} else {
							var a = '<a href="javascript:void(0);" onclick="edit(' + row.id + ')">编辑</a> | ';
							var b = '<a href="javascript:void(0);" onclick="editSequence(' + index + ')">改排序</a> | ';
							var c = '';
							if (row.isDisplay == 1)
								c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 0 + ')">不展示</a>';
							if (row.isDisplay == 0)
								c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 1 + ')">展示</a>';
                            var d = '<a href="javaScript:;" onclick="editArea(' + row.id + ')">地区设置</a> | ';
							return b + a + d + c;
						}
					}
				} ] ],
			toolbar : [ {
				id : '_add',
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					cleanEditCustomNavigationDiv();
					$('#editCustomNavigationDiv').dialog('open');
					$("#activitiesCustomId").combobox({
						url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode',
						editable : false,
					    valueField:'code',   
					    textField:'text' 
					});
					$("#pageId").combobox({
			            url:'${rc.contextPath}/page/ajaxAppCustomPage',
			            editable : false,
			            valueField:'code',
			            textField:'text'
			        });
				}
			} ],
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
			pagination : true
		});

        $('#editArea_div').dialog({
            title: '地区设置',
            width: 600,
            height: 350,
            closed: true,
            href: '${rc.contextPath}/customNavigation/editArea',
            buttons:[{
                text:'关闭    ',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#s_data').datagrid("load");
                    $('#editArea_div').dialog("close");
                }
            }]
        });

		$('#editCustomNavigationDiv').dialog({
			title : '新增自定义首页导航',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					$('#editCustomNavigationForm').form('submit', {
						url : "${rc.contextPath}/customNavigation/saveOrUpdate",
						onSubmit : function() {
							return valid(1);
						},
						success : function(data) {
							var res = eval("("+data+")");
							if (res.status == 1) {
								$.messager.alert('响应信息', "保存成功", 'info', function() {
									$('#s_data').datagrid('load');
									$('#editCustomNavigationDiv').dialog('close');
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
					$('#editCustomNavigationDiv').dialog('close');
				}
			} ]
		});
	});
</script>

</body>
</html>