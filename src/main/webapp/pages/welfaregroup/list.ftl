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
		<div data-options="region:'north',title:'首页格格福利团管理',split:true" style="height: 90px;">
			<form id="searchForm" method="post">
				<table cellpadding="15">
					<tr>
	                	<td>福利团名称：</td>
	                	<td class="searchText">
							<input id="searchTitle" name="searchTitle" value="" />
						</td>
	                    <td>展现状态：</td>
		                <td><select id="isDisplay" name="isDisplay" class="easyui-combobox" style="width:150px;"><option value="-1" selected="selected">-全部-</option><option value="1">展示</option><option value="0">不展示</option></select></td>
		                <td>
							<a id="searchBtn" onclick="searchList()" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',width:'80px'">查 询</a>
	                	</td>
	                </tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    			
		    <!-- 新增 begin -->
		    <div id="editCustomGGRecommendDiv" class="easyui-dialog" style="width:800px;height:700px;padding:15px 20px;">
		        <form id="editCustomGGRecommendForm" method="post">
					<input id="id" type="hidden" name="id" value="" >
					<p>
						<span>福利团名称：</span>
						<span><input type="text" name="title" id="editCustomGGRecommendForm_title" maxlength="10" style="width: 300px;"/></span>
						<span style="color: red">*</span>
					</p>
					<p>
						<span>福利团图片：</span>
						<span><input type="text" name="image" id="image" readonly="readonly" style="width: 300px;"/></span>
						<span><a onclick="picDialogOpen('image','oneImg')" class="easyui-linkbutton">上传图片</a></span>
						<span style="color: red">宽度：752,高度不定&nbsp;</span><span style="color: red" id="oneTip"></span><br>
						<span><img id="oneImg" src=""></span>
					</p>
					<p>
						<span>关联类型：</span>
						<span><input type="radio" name="type" value="1"/>单品&nbsp;&nbsp;</span>
						<span><input type="radio" name="type" value="2"/>组合&nbsp;&nbsp;</span>
						<span><input type="radio" name="type" value="3"/>网页自定义活动&nbsp;&nbsp;</span>
						<span><input type="radio" name="type" value="7"/>原生自定义活动&nbsp;&nbsp;</span>
						<span style="color: red">*</span>
					</p>
                    <p id="oneProduct">
                        <span>单品ID：</span>
                        <span><input type="text" name="oneProductId" id="oneProductId" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="10" style="width: 200px;"/></span><span style="color: red">*</span>
                        <span style="color: red" id="oneProductDesc"></span>
                    </p>
					<p id="oneActivitiesCommon">
						<span>组合ID：</span>
						<span><input type="text" name="oneActivitiesCommonId" id="oneActivitiesCommonId" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="10" style="width: 200px;"/></span><span style="color: red">*</span>
						<span style="color: red" id="oneActivitiesCommonDesc"></span>
					</p>
					<p id="oneActivitiesCustom">
						<span>自定义活动：</span>
						<span><input type="text" name="oneActivitiesCustomId" id="oneActivitiesCustomId" style="width: 200px;"/></span><span style="color: red">*</span>
					</p>
					<p id="onePage">
						<span>原生自定义活动：</span>
						<span><input type="text" name="onePageId" id="onePageId" style="width: 200px;"/></span><span style="color: red">*</span>
					</p>
					<p>
						<span>排序值：</span>
						<span><input type="number" name="sequence" id="sequence" value="" maxlength="100" style="width: 300px;"/></span>
						<span style="color: red">*</span>
					</p>
					<p id="display">
						<span>展现状态：</span>
						<span><input type="radio" name="isDisplay" value="1"/>展现&nbsp;&nbsp;</span>
						<span><input type="radio" name="isDisplay" value="0"/>不展现</span>
						<span style="color: red">*</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
		</div>
	</div>
</div>

<div id="picDia" class="easyui-dialog" closed="true" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
    	<input type="hidden" name="needWidth" id="needWidth" value="752">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
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
 $(function(){
    $('#picDia').dialog({
        title:'图片上传窗口',
        collapsible:true,
        closed:true,
        modal:true
    });
});
var imageId = '';
var imgSrc = '';
function picDialogOpen(id, img) {
	imageId = id;
	imgSrc = img;
	$('#needWidth').val(752);
	$('picForm').form('reset');
	$('#picDia').dialog('open');
}
//图片上传
function picUpload() {
    $('#picForm').form('submit',{
        url:"${rc.contextPath}/pic/fileUpLoad",
        async:false,
        success:function(data) {
            var res = eval("("+data+")")
            if(res.status == 1) {
            	$.messager.alert('响应信息',"上传成功...",'info', function() {
        			$("#picDia").dialog("close");
                    $('#' + imageId).val(res.url);
                    $('#' + imgSrc).attr('src', res.url);
                    $('#' + imgSrc).show();
        		});
            } else{
                $.messager.alert('响应信息',res.msg,'error',function(){
                    return ;
                });
            }
        }
    })
}

function strToDate(str) {
 var tempStrs = str.split(" ");
 var dateStrs = tempStrs[0].split("-");
 var year = parseInt(dateStrs[0], 10);
 var month = parseInt(dateStrs[1], 10) - 1;
 var day = parseInt(dateStrs[2], 10);
 var timeStrs = tempStrs[1].split(":");
 var hour = parseInt(timeStrs [0], 10);
 var minute = parseInt(timeStrs[1], 10) - 1;
 var second = parseInt(timeStrs[2], 10);
 var date = new Date(year, month, day, hour, minute, second);
 return date;
}
	// 验证表单是否是否有效
	function valid() {
		var title = $("#editCustomGGRecommendForm_title").val();
		if($.trim(title) == ''){
			$.messager.alert('警告','福利团名称不能为空', 'warning');
			return false;
		}
		if($('#image').val() == '') {
			$.messager.alert('警告','福利团图片不能为空', 'warning');
			return false;
		}
		if(typeof($('input[name=type]:checked').val()) == 'undefined') {
			$.messager.alert('警告','关联类型不能为空', 'warning');
			return false;
		}
		if($('input[name=type]:checked').val() == 2) {
			if($('#oneActivitiesCommonId').val() == '') {
				$.messager.alert('警告','组合ID不能为空', 'warning');
				return false;
			}
		} else if($('input[name=type]:checked').val() == 3) {
			if($('#oneActivitiesCustomId').combobox('getValue') == '') {
				$.messager.alert('警告','自定义活动不能为空', 'warning');
				return false;
			}
		} else if($('input[name=type]:checked').val() == 7) {
			if($('#onePageId').combobox('getValue') == '') {
				$.messager.alert('警告','原生自定义活动不能为空', 'warning');
				return false;
			}
		}
        else if($('input[name=type]:checked').val() == 1) {
            if($('#oneProductId').val() == '') {
                $.messager.alert('警告','商品ID不能为空', 'warning');
                return false;
            }
        }
		if($('#sequence').val() == '') {
			$.messager.alert('警告','排序值不能为空', 'warning');
			return false;
		}
		if($('#id').val() == '') {
			if(typeof($('input[name=isDisplay]:checked').val()) == 'undefined') {
				$.messager.alert('警告','展现状态不能为空');
				return false;
			}
		}
		return true;
	}
	function initForm() {
		$('#editCustomGGRecommendForm').form('clear');
		$('#oneActivitiesCommonDesc').text('');
		$('#oneProductDesc').text('');
		$('#oneProduct').hide();
		$('#oneActivitiesCommon').hide();
		$('#oneActivitiesCustom').hide();
		$('#onePage').hide();
		$('#oneTip').text('');
		if($('#image').val() == '') {
			$('#oneImg').hide();
		}
		$.ajax({
			url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode',
			type:'POST',
			async:false,
			success:function(data) {
				$("#oneActivitiesCustomId").combobox({
					data:data,
					editable : false,
				    valueField:'code',
				    textField:'text' 
				});
			}
		});
		$.ajax({
			url:'${rc.contextPath}/page/ajaxAppCustomPage',
			type:'POST',
			async:false,
			success:function(data) {
				$("#onePageId").combobox({
					data:data,
		            editable : false,
		            valueField:'code',
		            textField:'text'
		        });
			}
		});
	}
	//编辑
	function edit(id) {
		$.messager.progress();
		initForm();
		$('#display').hide();
		$.ajax({
			url: '${rc.contextPath}/welfareGroup/findById?id=' + id,
			type:"POST",
			async:false,
			success: function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					$('#editCustomGGRecommendForm').form('load',data.data);
//					$('#oneTip').text('宽度：752px');
                    if(data.data.type == 1 || data.data.type == 4) {
                        $('#oneProduct').show();
                        $('#oneProductId').val(data.data.displayId);
                    }
					else if(data.data.type == 2) {
						$('#oneActivitiesCommon').show();
						$('#oneActivitiesCommonId').val(data.data.displayId);
					} else if(data.data.type == 3) {
						$('#oneActivitiesCustom').show();
						$('#oneActivitiesCustomId').combobox('setValue', data.data.displayId);
					} else if(data.data.type == 7) {
						$('#onePage').show();
						$('#onePageId').combobox('setValue', data.data.displayId);
					}
					if(data.data.image != '') {
						$('#oneImg').attr('src', data.data.image);
						$('#oneImg').show();
					}
					$('#editCustomGGRecommendDiv').dialog('open');
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
	function deleteIt(id){
	    $.messager.confirm("提示信息","确定删除么？",function(re){
	        if(re){
	            $.messager.progress();
	            $.post("${rc.contextPath}/welfareGroup/delete",{id:id},function(data){
	                $.messager.progress('close');
	                if(data.status == 1){
	                    $.messager.alert('响应信息',"删除成功...",'info',function(){
	                        $('#s_data').datagrid('reload');
	                        return
	                    });
	                } else{
	                    $.messager.alert('响应信息',data.msg,'error',function(){
	                        return
	                    });
	                }
	            })
	        }
	    })
	}
	
	
	// 搜索按钮
	function searchList() {
		$('#s_data').datagrid('load', {
			title : $("#searchTitle").val(),
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

	// 跟新导航排序值
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/welfareGroup/saveOrUpdate',
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
			url: '${rc.contextPath}/welfareGroup/saveOrUpdate',
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
	$(function() {
        // 填写商品ID后
        $('#oneProductId').blur(function() {
            if($('#oneProductId').val().length < 1)
                return;
            $.ajax({
                url: '${rc.contextPath}/product/findProductInfoById',
                type: 'POST',
                data: {id:$('#oneProductId').val()},
                success: function(data) {
                    if(data.status == 1) {
                        $('#oneProductDesc').text(data.msg);
                    }else{
                        $.messager.alert("提示",data.msg,"info");
                    }
                }
            });
        });
		// 填写组合ID后
		$('#oneActivitiesCommonId').blur(function() {
			if($('#oneActivitiesCommonId').val().length < 1)
				return;
			$.ajax({
                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
                type: 'POST',
                data: {id:$('#oneActivitiesCommonId').val()},
                success: function(data) {
                    if(data.status == 1) {
                    	$('#oneActivitiesCommonDesc').text(data.name + "-" + data.remark);
                    }else{
                    	$.messager.alert("提示",data.msg,"info");
                    }
                }
            });
		});
		// 填写组合ID后
		$('#twoActivitiesCommonId').blur(function() {
			if($('#twoActivitiesCommonId').val().length < 1)
				return;
			$.ajax({
                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
                type: 'POST',
                data: {id:$('#twoActivitiesCommonId').val()},
                success: function(data) {
                    if(data.status == 1) {
                    	$('#twoActivitiesCommonDesc').text(data.name + "-" + data.remark);
                    }else{
                    	$.messager.alert("提示",data.msg,"info");
                    }
                }
            });
		});
		// 选择类型展示不同的信息
		$('input[name=type]').change(function() {
			var type = $('input[name=type]:checked').val();
			if(type == 1 || type == 4) {
				$('#oneProduct').show();
				$('#oneActivitiesCommon').hide();
				$('#oneActivitiesCustom').hide();
				$('#onePage').hide();
			}
			else if(type == 2) {
                $('#oneProduct').hide();
                $('#oneActivitiesCommon').show();
                $('#oneActivitiesCustom').hide();
                $('#onePage').hide();
            }
			else if(type == 3) {
                $('#oneProduct').hide();
				$('#oneActivitiesCommon').hide();
				$('#oneActivitiesCustom').show();
				$('#onePage').hide();
			} else if(type == 7) {
                $('#oneProduct').hide();
				$('#oneActivitiesCommon').hide();
				$('#oneActivitiesCustom').hide();
				$('#onePage').show();
			}
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/welfareGroup/listInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            singleSelect: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:15, align:'center'},
                {field:'isDisplay',    title:'展现状态', width:15, align:'center',
            		formatter:function(value,row,index){
 						if(row.isDisplay == 1)
 							return '展示';
 						else if(row.isDisplay == 0)
 							return '不展示';
 						else 
 							return row.isDisplay;
                }},
               	{field:'title',    title:'福利团名称', width:80, align:'center'},
                {field:'imageURL',    title:'福利团图片', width:80, align:'center'},
                {field:'displayId',    title:'关联类型', width:80, align:'center'},
                {field:'sequence',     title:'排序值',  width:25, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:40, align:'center',
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
								c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 0 + ')">不展示</a> | ';
							if (row.isDisplay == 0)
								c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 1 + ')">展示</a> | ';
							var d = '<a href="javascript:void(0);" onclick="deleteIt(' + row.id + ')">删除</a>';				
							return b + a + c + d;
						}
					}
				} ] ],
			toolbar : [ {
				id : '_add',
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					$('#editCustomGGRecommendForm').form('clear');
					$('#editCustomGGRecommendDiv').dialog('open');
					initForm();
					$('#display').show();
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

		$('#editCustomGGRecommendDiv').dialog({
			title : '新增修改格格福利团',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					$('#editCustomGGRecommendForm').form('submit', {
						url : "${rc.contextPath}/welfareGroup/saveOrUpdate",
						onSubmit : function() {
							return valid();
						},
						success : function(data) {
							var res = eval("("+data+")");
							if (res.status == 1) {
								$.messager.alert('响应信息', "保存成功", 'info', function() {
									$('#s_data').datagrid('load');
									$('#editCustomGGRecommendDiv').dialog('close');
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
					$('#editCustomGGRecommendDiv').dialog('close');
				}
			} ]
		});
	});
</script>

</body>
</html>