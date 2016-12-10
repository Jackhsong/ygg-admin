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
		<div data-options="region:'north',title:'热卖品牌',split:true" style="height: 90px;">
			<form id="searchForm" method="post">
				<table cellpadding="15">
					<tr>
	                	<td>品牌名称：</td>
	                	<td class="searchText">
							<input id="searchTitle" name="searchTitle" value="" />
						</td>
	                    <td>展现状态：</td>
		                <td><select id="isDisplay" name="isDisplay" class="easyui-combobox" style="width:150px;"><option value="-1" selected="selected">-全部-</option><option value="1">展示</option><option value="0">不展示</option></select></td>
		                <td>
							<a id="searchBtn" onclick="searchList()" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',width:'80px'">查 询</a>
							<a id="searchBtn" onclick="clearSearchForm()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
	                	</td>
	                </tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    			
		    <!-- 新增 begin -->
		    <div id="editCustomGGRecommendDiv" class="easyui-dialog" style="width:700px;height:600px;padding:15px 20px;">
		        <form id="editCustomGGRecommendForm" method="post">
					<input id="id" type="hidden" name="id" value="" >
					<input id="image"  type="hidden" name="image" value="" >
					<p>
						<span>品牌ID：</span>
						<span><input type="text" name="brandId" id="brandId" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="100" style="width: 300px;"/></span>
						<span style="color: red" id="bId"></span>
						<span style="color: red">*</span>
					</p>
					<p>
						<span>品牌名称：</span>
						<span><input type="text" name="name" id="name" maxlength="100" style="width: 300px;"/></span>
					</p>
					<p>
						<span>品牌国家：</span>
						<span><input type="text" name="stateId" id="stateId"  style="width: 300px;"/></span>
					</p>
					<p>
						<span>品牌类目：</span>
						<span><input type="text" name="categoryFirstId" id="categoryFirstId"  style="width: 300px;"/></span>
					</p>
					<p>
						<span>品牌图片：</span>
						<span><input type="text" name="headImage" id="headImage" readonly="readonly" style="width: 300px;"/></span>
						<span><a onclick="picDialogOpen('headImage','oneImg')" class="easyui-linkbutton">上传图片</a></span>
						<span style="color: red"></span><span style="color: red" id="oneTip"></span><br>
						<span><img id="oneImg" src=""></span>
					</p>
					<p>
						<span>热卖排序：</span>
						<span><input type="text" name="sequence" id="sequence" value="" maxlength="100" style="width: 300px;"/></span>
					</p>
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
    	<#--<input type="hidden" name="needWidth" id="needWidth" value="0">-->
    	<#--<input type="hidden" name="needHeight" id="needHeight" value="0">-->
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script>
	function clearSearchForm(){
		$("#searchTitle").val('');
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
	$('#needWidth').val(188);
	$('#needHeight').val(132);
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
		var title = $("#brandId").val();
		if($.trim(title) == ''){
			$.messager.alert('警告','品牌ID不能为空', 'warning');
			return false;
		}
		if($('#id').val() == '') {
			if(typeof($('input[name=isDisplay]:checked').val()) == 'undefined') {
				$.messager.alert('警告','展现状态不能为空');
				return false;
			}
		}
		if($('#stateId').combobox('getValue') == '') {
            $.messager.alert('警告','品牌国家不能为空','warning');
            return false;
        }
        if($('#categoryFirstId').combobox('getValue') == '') {
            $.messager.alert('警告','品牌类目不能为空','warning');
            return false;
        }
		return true;
	}
	function initForm() {
		$('#editCustomGGRecommendForm').form('clear');
		$('#oneActivitiesCommonDesc').text('');
		$('#oneActivitiesCommon').hide();
		$('#oneActivitiesCustom').hide();
		$('#onePage').hide();
		$('#bId').text('');
		if($('#headImage').val() == '') {
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
				$("#twoActivitiesCustomId").combobox({
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
				$("#twoPageId").combobox({
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
		$('#brandId').show();
		$.ajax({
			url: '${rc.contextPath}/saleBrand/findById?id=' + id,
			type:"POST",
			async:false,
			success: function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					$('#editCustomGGRecommendForm').form('load',data.data);
					$('#brandId').hide();
					$('#bId').text(data.data.brandId);
                	$('#name').attr("readonly",true);
					$('#stateId').combobox("readonly",true);
                	$('#categoryFirstId').combobox("readonly",true);
					if(data.data.headImage != '') {
						$('#oneImg').attr('src', data.data.headImage);
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
	            $.post("${rc.contextPath}/saleBrand/delete",{id:id},function(data){
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
			name : $("#searchTitle").val(),
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
			url: '${rc.contextPath}/saleBrand/saveOrUpdate',
			type:"POST",
			data: {id:row.id,sequence:row.sequence,headImage:row.hImage},
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
			url: '${rc.contextPath}/saleBrand/saveOrUpdate',
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
		$("#stateId").combobox({
			url:'${rc.contextPath}/flag/jsonSaleFlagCode?id=${stateId}',   
		    valueField:'code',   
		    textField:'text'  
		});
		$('#categoryFirstId').combobox({   
		    url:'${rc.contextPath}/category/jsonCategoryFirstCode?id=${categoryFirstId}',   
		    valueField:'code',   
		    textField:'text',
		    editable:false
		});
		// 填写组合ID后
		$('#brandId').blur(function() {
			if($('#brandId').val().length < 1)
				return;
			$('#brandId').show();
			$.ajax({
                url: '${rc.contextPath}/saleBrand/getBrandInfo',
                type: 'POST',
                data: {id:$('#brandId').val()},
                success: function(data) {
                    if(data.status == 1) {
                   	 	$('#brandId').hide();
						$('#bId').text(data.brandId);
                    	$('#name').val(data.name);
                    	$('#name').attr("readonly",true);
                    	$('#stateId').val(data.stateId);
                    	$('#image').val(data.image);
                    	$("#stateId").combobox({
							url:'${rc.contextPath}/flag/jsonSaleFlagCode?id='+data.stateId,   
						    valueField:'code',   
						    textField:'text'  
						});
						$('#stateId').combobox("readonly",true);
						$('#categoryFirstId').combobox({ 
						    url:'${rc.contextPath}/category/jsonCategoryFirstCode?id='+data.categoryFirstId, 
						    valueField:'code',
						    textField:'text'
						});
                    	$('#categoryFirstId').val(data.categoryFirstId);
                    	$('#categoryFirstId').combobox("readonly",true);
                    	$('#sequence').val(data.sequence);
                    }else{
                    	$.messager.alert("提示",data.msg,"info");
                    	$('#bId').text('');
                    	$('#brandId').val('');
                    	$('#name').val('');
                    	$('#stateId').val('');
                    	$('#categoryFirstId').val('');
                    	$('#sequence').val('');
                    	$('#image').val('');
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
			if(type == 2) {
				$('#oneActivitiesCommon').show();
				$('#oneActivitiesCustom').hide();
				$('#onePage').hide();
			} else if(type == 3) {
				$('#oneActivitiesCommon').hide();
				$('#oneActivitiesCustom').show();
				$('#onePage').hide();
			} else if(type == 7) {
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
            url:'${rc.contextPath}/saleBrand/listInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:15, align:'center',checkbox:true},
            	{field:'index',    title:'ID', width:10, align:'center'},
            	{field:'brandId',    title:'品牌ID', width:10, align:'center'},
                {field:'isDisplay',    title:'展现状态', width:20, align:'center',
            		formatter:function(value,row,index){
 						if(row.isDisplay == 1)
 							return '展示';
 						else if(row.isDisplay == 0)
 							return '不展示';
 						else 
 							return row.isDisplay;
                }},
               	{field:'name',    title:'品牌名称', width:20, align:'center'},
                {field:'image',    title:'品牌logo', width:20, align:'center'},
                {field:'headImage',    title:'品牌头图', width:20, align:'center'},
                {field:'sequence',     title:'排序值',  width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:40, align:'center',
					formatter : function(value, row, index) {
						if (row.editing) {
							var a = '<a href="javascript:void(0);" onclick="saverow(' + index + ')">保存</a> | ';
							var b = '<a href="javascript:void(0);" onclick="cancelrow(' + index + ')">取消</a>';
							return a + b;
						} else {
							var a = '<a href="javascript:void(0);" onclick="edit(' + row.id + ')">编辑</a> | ';
							var b = '<a href="javascript:void(0);" onclick="editSequence(' + index + ')">修改排序</a> | ';
							var c = '';
							if (row.isDisplay == 1)
								c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 0 + ')">不展示</a> | ';
							if (row.isDisplay == 0)
								c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 1 + ')">展示</a> | ';
							var d = '<a href="javascript:void(0);" onclick="deleteIt(' + row.id + ')">删除</a>';				
							return a + b + c + d;
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
					$('#brandId').show();
				}
				},'-',{
                    iconCls: 'icon-remove',
                    text:'批量删除',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('删除','确定删除吗',function(b){
                                if(b){
                                	$.messager.progress();
                                	var type = "delete";
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.ajax({
                    					type: 'post',
                    					url: '${rc.contextPath}/saleBrand/deleteBat',
                    					data: {'ids': ids.join(",")},
                    					datatype:'json',
                    					success: function(data){
                    						$.messager.progress('close');
                    						if(data.status == 1){
                    							$('#s_data').datagrid('reload');					
                    						}else{
                    							$.messager.alert('提示','删除出错',"error");
                    						}
                    		            },
                    		            error: function(xhr){
                    		            	$.messager.progress('close');
                    		            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                    		            }
                    				});	
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error");
                        }
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
			title : '新增热卖品牌',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					$('#editCustomGGRecommendForm').form('submit', {
						url : "${rc.contextPath}/saleBrand/saveOrUpdate",
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