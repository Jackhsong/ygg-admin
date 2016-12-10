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
<style type="text/css">
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'自定义活动管理',split:true" style="height: 80px;">
			<div style="height: 120px;padding: 15px">
	            <table class="search">
	                <tr>
	                    <td class="searchName">自定义特卖ID：</td>
	                    <td class="searchText"><input id="searchId" name="searchId" value="" /></td>
                        <td class="searchName">备注：</td>
                        <td class="searchText"><input id="searchRemarkId" name="remark" value="" /></td>
	                    <td class="searchName">使用状态：</td>
	                	<td class="searchText">
	                		<select id="searchIsAvailable" name="searchIsAvailable">
	                			<option value="-1">全部</option>
	                			<option value="1">可用</option>
	                			<option value="0">停用</option>
	                		</select>
	                	<td class="searchText">
							<a id="searchBtn" onclick="searchCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;
							<a id="clearBtn" onclick="clearCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
							&nbsp;
	                	</td>
	                </tr>
	            </table>
			</div>
		</div> 
		
		<div data-options="region:'center'" >
			<!--数据表格-->
			<table id="s_data" ></table>
				 	    		
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

	function searchCustom(){
		$('#s_data').datagrid('load',{
			id:$("#searchId").val(),
			remark:$("#searchRemarkId").val(),
			isAvailable:$("#searchIsAvailable").val()
		});
	}
	
	function clearCustom(){
		$("#searchId").val(''),
		$("#searchRemarkId").val(''),
		$("#searchIsAvailable").find('option').eq(0).attr('selected','selected');
		$('#s_data').datagrid('load',{});
	}

	function displayId(id,isAvailable){
		var tip = "";
		if(isAvailable == 0){
			tip = "确定停用吗？";
		}else{
			tip = "确定启用吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
   	            $.messager.progress();
   	            $.post(
   	            		"${rc.contextPath}/customActivities/updateCustomActivitiesStatus",
   	            		{id:id,isAvailable:isAvailable},
   	            		function(data){
		   	                $.messager.progress('close');
		   	                if(data.status == 1){
		   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
		   	                        $('#s_data').datagrid('load');
		   	                        return
		   	                    });
		   	                } else{
		   	                    $.messager.alert('响应信息',data.msg,'error');
		   	                }
   	            		});
	        }
	    });
	}

		
	function preview(index){
		var arr = $("#s_data").datagrid("getData")
		var url = arr.rows[index].shareURL;
		window.open(url);
	}
	
	
	$(function(){
		
		<!--加载楼层商品列表 begin-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customActivities/jsonCustomActivitiesInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:20, align:'center'},
            	{field:'isAvailable',    title:'使用状态', width:30, align:'center'},
                {field:'type',    title:'类型', width:50, align:'center'},
                {field:'remark',    title:'备注', width:70, align:'center'},
                {field:'url',    title:'URL', width:80, align:'center'},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                   		var a = '';
                   		if(row.typeCode <= 6 || row.typeCode == 12 || row.typeCode == 20 || row.typeCode==22){
                   			a = '<a href="${rc.contextPath}/customActivities/edit/' + row.id + '">编辑</a> | '
                   		}
                       	var b = '<a href="javaScript:;" onclick="preview(' + index + ')">预览</a>';
                       	var c = '';
                       	var d = '';
                       	if(row.isAvailableCode == 1){
                       		c = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 0 + ')">停用</a>'
                       	}else{
                       		c = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 1 + ')">启用</a>'
                       	}
                       	if(row.typeCode == 2){
                       		d = ' | <a href="${rc.contextPath}/activityManjian/manageProduct/2/'+row.id+'" target="_blank">管理满减活动商品</a>';
                       	}else if(row.typeCode == 4){
                       		d = ' | <a href="${rc.contextPath}/activityManjian/manageProduct/1/'+row.id+'" target="_blank">管理满减活动商品</a>';
                       	}else{
                       		d = '';
                       	}
                       	return a+b+c+d;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'增加自定义活动',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href='${rc.contextPath}/customActivities/add';
                }
            }],
            pagination:true
        });
		<!--加载楼层商品列表 end-->

});
</script>

</body>
</html>