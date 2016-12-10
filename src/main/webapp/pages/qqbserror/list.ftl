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
		<div data-options="region:'north',title:'用户关系修改',split:true" style="height: 90px;">
			<form id="searchForm" method="post">
				<table cellpadding="15">
					<tr>
	                	<td>用户ID：</td>
	                	<td class="searchText">
							<input id="searchAccountId" name="searchAccountId" value="" />
						</td>
	                    <td>推荐人ID：</td>
	                    <td class="searchText">
							<input id="searchTuiAccountId" name="searchTuiAccountId" value="" />
						</td>
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
		    <div id="editCustomGGRecommendDiv" class="easyui-dialog" style="width:500px;height:400px;padding:15px 20px;">
		        <form id="editCustomGGRecommendForm" method="post">
					<p><span style="color: red">数据无法还原,请谨慎操作!</span></p>
					<p>
						<span>用户&nbsp;ID：</span>
						<span><input type="text" name="accountId" id="accountId" maxlength="100" style="width: 120px;"/></span>
						<span style="color: red">*</span>
					</p>
					<p>
						<span>推荐人ID：</span>
						<span><input type="text" name="tuiAccountId" id="tuiAccountId" value="" maxlength="100" style="width: 120px;"/></span>
						<span style="color: red">*</span>
					</p>
					<p>
						<span>操作原因：</span>
						<span><input type="text" name="remark" id="remark" value="" maxlength="100" style="width: 120px;"/></span>
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
		$("#searchTuiAccountId").val('');
		$("#searchAccountId").val('');
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
		var title = $("#accountId").val();
		if($.trim(title) == ''){
			$.messager.alert('警告','用户ID不能为空', 'warning');
			$.messager.progress('close');
			return false;
		}
		var title = $("#tuiAccountId").val();
		if($.trim(title) == ''){
			$.messager.alert('警告','推荐人ID不能为空', 'warning');
			$.messager.progress('close');
			return false;
		}
		var title = $("#remark").val();
		if($.trim(title) == ''){
			$.messager.alert('警告','操作原因不能为空', 'warning');
			$.messager.progress('close');
			return false;
		}
		return true;
	}
	//编辑
	function edit(id) {
		$.messager.progress();
		initForm();
		$('#display').hide();
		$.ajax({
			url: '${rc.contextPath}/qqbsError/findById?id=' + id,
			type:"POST",
			async:false,
			success: function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					$('#editCustomGGRecommendForm').form('load',data.data);
					$('#oneTip').text('宽度：750px');
					if(data.data.type == 2) {
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
	function refuse(id,accountId,withdrawals,type){
	    $.messager.confirm("提示信息","确定拒绝么？",function(re){
	        if(re){
	            $.messager.progress();
	            $.post("${rc.contextPath}/qqbsError/refuse",{id:id,accountId:accountId,withdrawals:withdrawals,type:type},function(data){
	                $.messager.progress('close');
	                if(data.status == 1){
	                    $.messager.alert('响应信息',"拒绝成功...",'info',function(){
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
	function accept(id,accountId,withdrawals,type){
    	$.messager.confirm("提示信息","确定打款么？",function(re){
        	if(re){
	            $.messager.progress();
	            $.post("${rc.contextPath}/qqbsError/accept",{id:id,accountId:accountId,withdrawals:withdrawals,type:type},function(data){
	                $.messager.progress('close');
	                if(data.status == 1){
	                    $.messager.alert('响应信息',"打款成功...",'info',function(){
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
			tuiAccountId : $("#searchTuiAccountId").val(),
			accountId:$('#searchAccountId').val()
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
			url: '${rc.contextPath}/qqbsError/saveOrUpdate',
			type:"POST",
			data: {id:row.id,artificialIncrement:row.artificialIncrement},
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
			url: '${rc.contextPath}/qqbsError/saveOrUpdate',
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
		// 填写组合ID后
		$('#productBaseId').blur(function() {
			if($('#productBaseId').val().length < 1)
				return;
			$.ajax({
                url: '${rc.contextPath}/qqbsError/getProductInfo',
                type: 'POST',
                data: {id:$('#productBaseId').val()},
                success: function(data) {
                    if(data.status == 1) {
                    	$('#productId').val(data.productId);
                    	$('#name').val(data.name);
                    	$('#type').val(data.type);
                    	$('#actualSales').val(data.actualSales);
                    	$('#artificialIncrement').val(data.artificialIncrement);
                    }else{
                    	$.messager.alert("提示",data.msg,"info");
                    	$('#productBaseId').val('');
                    	$('#productId').val('');
                    	$('#name').val('');
                    	$('#type').val('');
                    	$('#actualSales').val('');
                    	$('#artificialIncrement').val('');
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
            url:'${rc.contextPath}/qqbsError/listInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'createTime',    title:'操作时间', width:20, align:'center'},
                {field:'accountId',    title:'用户ID', width:20, align:'center'},
                {field:'tuiAccountId',    title:'推荐人ID', width:20, align:'center'},
                {field:'operator',    title:'操作人', width:20, align:'center'},
                {field:'remark',    title:'备注', width:20, align:'center'},
                ] ],
            toolbar : [ {
				id : '_add',
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					$('#editCustomGGRecommendForm').form('clear');
					$('#editCustomGGRecommendDiv').dialog('open');
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
			title : '新增用户关系',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '新增',
				iconCls : 'icon-ok',
				handler : function() {
					$.messager.confirm("提示信息","确定新增关系么？",function(re){
        				if(re){
        					$.messager.progress();
							$('#editCustomGGRecommendForm').form('submit', {
								url : "${rc.contextPath}/qqbsError/save",
								onSubmit : function() {
										return valid();
								},
								success : function(data) {
									$.messager.progress('close');
									var res = eval("("+data+")");
									if (res.status == 1) {
										$.messager.alert('响应信息', "新增成功", 'info', function() {
											$('#s_data').datagrid('load');
											$('#editCustomGGRecommendDiv').dialog('close');
										});
									} else if (res.status == 0) {
										$.messager.alert('响应信息', res.msg, 'error');
									}
							   }
						  });
					  }
				})
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