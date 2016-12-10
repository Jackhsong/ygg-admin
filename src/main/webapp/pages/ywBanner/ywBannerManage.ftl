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

<div data-options="region:'center',title:'content'" style="padding:5px;">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
        <form id="searchForm" method="post" >
            <table cellspacing="5" cellpadding="5">
            	<tr>
            		<td>Banner展示状态：</td>
                    <td>
	                    <input type="checkbox" name="isDisplay" value="1" checked />展示
	                    <input type="checkbox" name="isDisplay" value="0" checked/>不展示
                    </td>
                    
                    <td>&nbsp;&nbsp;&nbsp;Banner进行状态：</td>
                    <td>
	                    <input type="checkbox" name="bannerStatus" value="1" checked />等待开始
	                    <input type="checkbox" name="bannerStatus" value="2" checked />进行中
	                    <input type="checkbox" name="bannerStatus" value="3" />已结束
                    </td>
                    <td>&nbsp;&nbsp;&nbsp;Banner描述：</td>
                    <td>
	                    <input id="bannerDesc" name="bannerDesc" value="" />
                    </td>
                    <td>
						<a id="searchBtn" onclick="searchBannerWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                	</td>
            	</tr>
            </table>
        </form>
    </div>
    <!--数据表格-->
    <table id="s_data" style=""></table>

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

	function searchBannerWindow(){
		var isDisplay =[]; 
		$('input[name="isDisplay"]:checked').each(function(){ 
			isDisplay.push($(this).val()); 
		});
		var isDisplayStr = isDisplay.join(",")
		if(isDisplayStr == ''){
			isDisplayStr = '-1';
		}
		
		var bannerStatus =[]; 
		$('input[name="bannerStatus"]:checked').each(function(){ 
			bannerStatus.push($(this).val()); 
		});
		var bannerStatusStr = bannerStatus.join(",")
		if(bannerStatusStr == ''){
			bannerStatusStr = '-1';
		}
		
    	$('#s_data').datagrid('load',{
        	isDisplay:isDisplayStr,
        	bannerStatus:bannerStatusStr,
        	bannerDesc:$("#bannerDesc").val()
    	});
	}

	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		window.location.href = "${rc.contextPath}/ywBanner/edit/"+arr.rows[index].id
	}
	
	function editrow(index){
    	$('#s_data').datagrid('beginEdit', index);
	};

	function updateActions(){
    	var rowcount = $('#s_data').datagrid('getRows').length;
    	for(var i=0; i<rowcount; i++){
        	$('#s_data').datagrid('updateRow',{
            	index:i,
            	row:{}
        	});
   		}
	}
	
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/ywBanner/updateOrder',
			type:"POST",
			data: {id:row.id,order:row.order},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load');
                    return
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return
                    });
                }
			}
		});
	};
	
	function saverow(index){
    	$('#s_data').datagrid('endEdit', index);
	};

	function cancelrow(index){
    	$('#s_data').datagrid('cancelEdit', index);
	};
	
	function displayIt(id,code){
    	$.messager.confirm("提示信息","确定修改展现状态吗？",function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/ywBanner/updateDisplayCode",{id:id,code:code},function(data){
                	$.messager.progress('close');
                	if(data.status == 1){
                        $('#s_data').datagrid('reload');
                        return
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
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/ywBanner/jsonBannerWindowInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'isDisplay',    title:'展现状态', width:20, align:'center'},
                {field:'bannerStatus',    title:'进行状态', width:30, align:'center'},
                {field:'type',    title:'类型', width:20, align:'center'},
                {field:'displayId',    title:'对应ID', width:30, align:'center'},
                {field:'displayName',    title:'对应名称', width:60, align:'center'},
                {field:'displayRemark',    title:'对应备注', width:60, align:'center'},
                {field:'bannerImage',    title:'BANNER图', width:40,align:'center',
                	formatter:function(value,row,index){
                		return '<a href="'+row.image+'" target="_blank">'+row.bannerImage+'</a>';
                	}	
                },
                {field:'desc',     title:'banner描述',  width:50,   align:'center' },
                {field:'startTime',     title:'开始时间',  width:40,   align:'center' },
                {field:'endTime',     title:'结束时间',  width:40,   align:'center' },
                {field:'order',    title:'排序值', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                    
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a> | ';
                    		var b = '<a href="javascript:;" onclick="editrow(' + index + ')">编辑排序值</a> | ';
                        	var c = '';
                        	if(row.isDisplayCode == 1){
                        		c = '<a href="javaScript:;" onclick="displayIt(' + '\'' + row.id + '\',' + row.isDisplayCode + ')">不展现</a>';
                        	}else{
                        		c = '<a href="javaScript:;" onclick="displayIt(' + '\'' + row.id + '\',' + row.isDisplayCode + ')">展现</a>';
                        	}
                        	return b+a+c;
                    	}
                    }
                }
            ]],
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
            toolbar:[{
                id:'_add',
                text:'新增Banner',
                iconCls:'icon-add',
                handler:function(){
                    window.location.href = "${rc.contextPath}/ywBanner/addBanner"
                }
            }],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>