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
		<div data-options="region:'north',title:'升级审核管理',split:true" style="height: 90px;">
			<form id="searchForm" method="post">
				<table cellpadding="15">
					<tr>
	                	<td>用户昵称：</td>
	                	<td class="searchText">
							<input id="searchTitle" name="searchTitle" value="" />
						</td>
	                    <td>用户ID：</td>
	                    <td class="searchText">
							<input id="searchAccountId" name="searchAccountId" value="" />
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
		var title = $("#productBaseId").val();
		if($.trim(title) == ''){
			$.messager.alert('警告','基本商品ID不能为空', 'warning');
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
		$.ajax({
			url: '${rc.contextPath}/qqbsUpgradeCheck/findById?id=' + id,
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
	function refuse(id,type){
	    $.messager.confirm("提示信息","确定拒绝么？",function(re){
	        if(re){
	            $.messager.progress();
	            $.post("${rc.contextPath}/qqbsUpgradeCheck/refuse",{id:id,type:type},function(data){
	                $.messager.progress('close');
	                if(data.status == 1){
	                    $.messager.alert('响应信息',"拒绝成功...",'info',function(){
	                        $('#s_data').datagrid('reload');
	                    });
	                } else{
	                    $.messager.alert('响应信息',data.msg,'error',function(){
	                    });
	                }
	            })
	        }
	    })
	}
	function accept(id,type){
    	$.messager.confirm("提示信息","确定通过么？",function(re){
        	if(re){
	            $.messager.progress();
	            $.post("${rc.contextPath}/qqbsUpgradeCheck/accept",{id:id,type:type},function(data){
	                $.messager.progress('close');
	                if(data.status == 1){
	                    $.messager.alert('响应信息',"审核成功...",'info',function(){
	                        $('#s_data').datagrid('reload');
	                    });
	                } else{
	                    $.messager.alert('响应信息',data.msg,'error',function(){
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
			url: '${rc.contextPath}/qqbsUpgradeCheck/saveOrUpdate',
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
			url: '${rc.contextPath}/qqbsUpgradeCheck/saveOrUpdate',
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
                url: '${rc.contextPath}/qqbsUpgradeCheck/getProductInfo',
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
            url:'${rc.contextPath}/qqbsUpgradeCheck/listInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:15, align:'center',checkbox:true},
            	{field:'index',    title:'申请编号', width:10, align:'center'},
            	{field:'accountId',    title:'用户Id', width:20, align:'center'},
            	{field:'name',    title:'姓名', width:20, align:'center'},
            	{field:'flag',    title:'申请身份', width:15, align:'center',
            		formatter:function(value,row,index){
 						if(row.flag == 0)
 							return '申请经理';
 						else if(row.flag == 1)
 							return '申请总监';
 						else 
 							return row.flag;
                }},
            	{field:'cardId',    title:'身份证号', width:20, align:'center'},
            	{field:'nickname',    title:'昵称', width:20, align:'center'},
            	{field:'number',    title:'手机号', width:20, align:'center'},
                {field:'forceTime',    title:'生效时间', width:20, align:'center'},
                {field:'hidden',  title:'操作', width:40, align:'center',
					formatter : function(value, row, index) {
							var a = '';
							var b = '';
							var c = '';
							var d = '';
							if (row.status == 1){
								a = '审核通过';
							}else if(row.status == 2){
								b = '审核不通过';
							}else{
								c = '<a href="javascript:void(0);" onclick="accept(' + row.id + ',' +1+ ')">审核通过</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;';
								d = '<a href="javascript:void(0);" onclick="refuse(' + row.id + ','+2+')">审核不通过</a>';
							}
							return a+b+c + d;
					}
				} ] ],
				toolbar : [ 
				'-',{
                    iconCls: 'icon-remove',
                    text:'批量审核',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('审核通过','确定批量通过吗',function(b){
                                if(b){
                                	$.messager.progress();
                                	var type = "delete";
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.ajax({
                    					type: 'post',
                    					url: '${rc.contextPath}/qqbsUpgradeCheck/deleteBat',
                    					data: {'ids': ids.join(",")},
                    					datatype:'json',
                    					success: function(data){
                    						$.messager.progress('close');
                    						if(data.status == 1){
                    							$.messager.alert('响应信息', data.msg, 'info', function() {
													$('#s_data').datagrid('reload');
												});
                    						}else{
                    							$.messager.alert('提示','通过出错',"error");
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
                            $.messager.alert('提示','请选择中要审核的申请编号',"error");
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
			pagination : true,
			onLoadSuccess:function(data){
    	  		if (data.rows.length > 0) { 
            		//循环判断操作为新增的不能选择 
                 	for (var i = 0; i < data.rows.length; i++) { 
	                     //根据operate让某些行不可选 
	                     if (data.rows[i].status == 2 || data.rows[i].status == 3) { 
	                         $("input[type='checkbox']")[i + 1].disabled = true;
                     		 $("#s_data").datagrid("unselectRow", i);
	                     } 
                 	} 
             	} 
            },
            onClickRow:function(rowIndex, rowData){
	             //加载完毕后获取所有的checkbox遍历 
	             $("input[type='checkbox']").each(function(index, el){ 
	                 //如果当前的复选框不可选，则不让其选中 
	                 if (el.disabled == true) { 
	                     $("#s_data").datagrid("unselectRow", index-1);
	                 } 
	             }) 
         	}, 
	         onCheckAll:function(rows){ 
	            //加载完毕后获取所有的checkbox遍历 
	             $("input[type='checkbox']").each(function(index, el){
	                 //如果当前的复选框不可选，则不让其选中 
	                 if (el.disabled == true) { 
	                 	$("#s_data").datagrid("unselectRow", index-1);
	                 } 
	             }) 
	         }
		});

		$('#editCustomGGRecommendDiv').dialog({
			title : '新增热卖商品',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					$('#editCustomGGRecommendForm').form('submit', {
						url : "${rc.contextPath}/qqbsUpgradeCheck/save",
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