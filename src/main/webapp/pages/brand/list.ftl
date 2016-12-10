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

<div data-options="region:'center',title:'content'" style="padding:5px;">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
		<form id="searchForm" method="post" action="${rc.contextPath}/brand/exportBrand">
			<input style="display: none;" type="text" /> 
			<table>
				<tr>
					<td>品牌名称</td>
					<td><input id="searchName" name="brandName" value="" /></td>
                    <td>品牌英文名称</td>
                    <td><input id="enName" name="enName" value="" /></td>
					<td>状态</td>
					<td>
						<select id="isAvailable" name="isAvailable">
							<#if isAvailable?exists && isAvailable == 1>
								<option value="1">可用</option>
							<#else>
								<option value="0">停用</option>
							</#if>
						</select>
					</td>
					<td>品牌国家</td>
					<td><input name="stateId" id="searchStateId"  value=""/></td>
					<td>品牌类目</td>
					<td><input id="searchCategoryFirstId" name="categoryFirstId" value="" /></td>
					<td>
						<a id="searchBtn" onclick="searchBrand()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;&nbsp;
						<a id="exportBtn" onclick="exportBrand()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出</a>
					</td>
				</tr>
			</table>
	</div>

	<div title="品牌管理 --- ${listType}" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>

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

	function searchBrand() {
		$('#s_data').datagrid('load', {
			name : $("#searchName").val(),
			enName : $("#enName").val(),
			isAvailable : $("#isAvailable").val(),
			stateId : $('#searchStateId').combobox('getValue'),
			categoryFirstId : $('#searchCategoryFirstId').combobox('getValue')
		});
	}

	function exportBrand(){
		$('#searchForm').submit();
	}
	
	function editIt(index){
    	var arr=$("#s_data").datagrid("getData");
    	var urlStr= "${rc.contextPath}/brand/edit/"+arr.rows[index].id;
    	window.open(urlStr,"_blank");
	}
	function editIsDisplay(id,isAvailable) {
		$.ajax({
			url: '${rc.contextPath}/brand/updateInfo',
			type:"POST",
			data: {id:id,isAvailable:isAvailable},
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
	function deleteIt(id){
	    $.messager.confirm("提示信息","确定删除么？",function(re){
	        if(re){
	            $.messager.progress();
	            $.post("${rc.contextPath}/brand/delete",{id:id},function(data){
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
	$(function(){
		$("#searchStateId").combobox({
			url:'${rc.contextPath}/flag/jsonSaleFlagCode?id=${stateId}&needAll=1',
		    valueField:'code',   
		    textField:'text'  
		});
		$('#searchCategoryFirstId').combobox({
		    url:'${rc.contextPath}/category/jsonCategoryFirstCode?id=${categoryFirstId}&zeroNeed=1',
		    valueField:'code',   
		    textField:'text',
            multiple: false,
		    editable:false
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/brand/jsonInfo',
            queryParams:{
            	isAvailable:${isAvailable}
            },
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:70,
            pageList:[70,80],
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center',checkbox:true},
            	{field:'index',    title:'ID', width:20, align:'center'},
                {field:'name',    title:'品牌名称', width:30, align:'center'},
                {field:'enName',    title:'英文名', width:30, align:'center'},
                {field:'adImage',    title:'广告图', width:30, align:'center'},
                {field:'hotSpots',    title:'品牌卖点', width:45, align:'center'},
                {field:'introduce',    title:'品牌介绍', width:45, align:'center'},
                {field:'image',     title:'logo',  width:35,   align:'center' },
                {field:'stateName',    title:'品牌国家', width:20, align:'center'},
                {field:'categoryFirstName', title:'品牌类目', width:20, align:'center'},
                {field:'isAvailable',    title:'状态', width:15, align:'center'},
                {field:'hidden',  title:'操作', width:25,align:'center',
                    formatter:function(value,row,index){
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                        var c = '';
                        if (row.isAvailable == '可用')
							c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 0 + ')">停用</a> | ';
						if (row.isAvailable == '停用')
							c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 1 + ')">可用</a> | ';
						var d = '<a href="javascript:void(0);" onclick="deleteIt(' + row.id + ')">删除</a>';	
                        
                        return c+lableStr+d;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增品牌信息',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href = "${rc.contextPath}/brand/add"
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
                    					url: '${rc.contextPath}/brand/deleteBat',
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
            }],
            pagination:true,
        });
	})
</script>

</body>
</html>