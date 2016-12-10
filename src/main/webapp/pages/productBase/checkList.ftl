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
		<div data-options="region:'north',title:'商品审核',split:true" style="height: 190px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" method="get" >
		            <table class="search">
		                <tr>
		                	<td class="searchName">提交审核时间：</td>
							<td class="searchText"><input id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endTime\')}'})" value="" />
							 ~ <input id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})" value="" /></td>
		                    <td class="searchName">审核时间：</td>
		                    <td class="searchText"><input id="checkStartTime" name="checkStartTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'checkEndTime\')}'})" value="" />
							 ~ <input id="checkEndTime" name="checkEndTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'checkStartTime\')}'})" value="" /></td>
		                </tr>
		                <tr>
		                	<td class="searchName">审核状态：</td>
		                	<td class="searchText"><select id="status"><option value="0" selected="selected">-请选择-</option><option value="2">待审核</option><option value="3">未通过</option><option value="4">通过</option></select></td>
		                	<td class="searchName">审核负责人：</td>
		                	<td class="searchText"><input id="checker" name="checker" value=""/></td>
		                </tr>
		                <tr>
		                	<td class="searchName">商品ID：</td>
		                    <td class="searchText"><input id="id" name="id" value="" /></td>
							<td class="searchName">商品类目：</td>
							<td class="searchText"><input name="firstCategory" id="searchFirstCategory"/> ~ <input name="secondCategory" id="searchSecondCategory"/></td>
		                </tr>
		                <tr>
		                    <td class="searchName">商品名称：</td>
							<td class="searchText"><input id="name" name="name" value=""/></td>
							<td class="searchName">商家名称：</td>
		                    <td class="searchText"><input id="sellerName" name="sellerName" /></td>
		                </tr>
						<tr>
		                	<td class="searchName">排期状态：</td>
		                	<td class="searchText"><select id="waitingStatus"><option value="0" selected="selected">-请选择-</option><option value="1">未排期</option><option value="2">排期未上架</option><option value="3">排期已上架</option></select></td>
		                	<td class="searchName"></td>
		                	<td class="searchText">
								<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
		                	</td>
						</tr>
		            </table>
		        </form>
    		</div>
		</div>
		<div data-options="region:'center'" >
    		<table id="s_data" style=""></table>   	 		
		</div>
	</div>
</div>
<script>

	function clearSearch() {
		$('#startTime').val('');
		$('#endTime').val('');
		$('#checkStartTime').val('');
		$('#checkEndTime').val('');
		$('#status').val(0);
		if($('#checker').combobox('getText') != '')
			$('#checker').combobox('clear');
		$('#id').val('');
		if($('#searchFirstCategory').combobox('getText') != '')
			$('#searchFirstCategory').combobox('clear');
		if($('#searchSecondCategory').combobox('getText') != '')
			$('#searchSecondCategory').combobox('clear');
		$('#name').val('');
		if($('#sellerName').combobox('getText') != '')
			$('#sellerName').combobox('clear');
		if($('#waitingStatus').combobox('getText') != '')
			$('#waitingStatus').combobox('clear');
	}

	function searchProduct() {
		$('#s_data').datagrid('load', {
			submitStartTime : $("#startTime").val(),
			submitEndTime : $("#endTime").val(),
			checkStartTime : $("#checkStartTime").val(),
			checkEndTime : $("#checkEndTime").val(),
			status : $("#status").val(),
			sellerProductId : $("#id").val() == "" ? 0 : $("#id").val(),
			checker : $("#checker").combobox('getValue'),
			firstCategory : $("input[name='firstCategory']").val() == "" ? 0 : $("input[name='firstCategory']").val(),
			secondCategory : $("input[name='secondCategory']").val() == "" ? 0 : $("input[name='secondCategory']").val(),
			name : $('#name').val(),
			sellerName : $("#sellerName").combobox('getText'),
			waitingStatus : $("#waitingStatus").val(),
		});
	}
	$(function() {
		$("#sellerName").combobox({
			mode:'remote',
			valueField : 'code',
			textField : 'text',
			url : '${rc.contextPath}/seller/jsonSellerCode',
		});
		$("#checker").combobox({
			valueField : 'id',
			textField : 'realname',
			url : '${rc.contextPath}/user/findAllUserCode',
		});
		$('#searchFirstCategory').combobox({
			valueField : 'code',
			textField : 'text',
			url : '${rc.contextPath}/category/jsonCategoryFirstCode',
			onChange : function(firstCategoryId) {
				$('#searchSecondCategory').combobox({
					valueField : 'code',
					textField : 'text',
					url : '${rc.contextPath}/category/jsonCategorySecondCode?firstId=' + firstCategoryId,
				});
			}
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/productCheck/checkListInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            singleSelect:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'submitTime',    title:'提交审核时间', width:20, align:'center'},
                {field:'checkTime',    title:'审核时间', width:20, align:'center'},
                {field:'status',    title:'审核状态', width:10, align:'center',
                	formatter:function(value,row,index){
 						if(row.status == 2)
 							return '待审核';
 						else if(row.status == 3)
 							return '未通过';
 						else if(row.status == 4)
 							return '通过';
 						else 
 							return '';
                	}
                },
                {field:'productBaseId',    title:'基本商品ID', width:13, align:'center'},
                {field:'sellerProductId',    title:'商家商品ID', width:13, align:'center'},
                {field:'categoryName',     title:'商品类目',  width:30,  align:'center'},
                {field:'name',     title:'商品名称',  width:30,   align:'center'},
                {field:'sellerName',     title:'商家名称',  width:20,   align:'center'},
                {field:'checker',     title:'审核负责人',  width:13,   align:'center'},
                {field:'checkContent',     title:'备注或未通过说明',  width:40,   align:'center' },
                {field:'waitingStatus',  title:'排期状态', width:13,align:'center',
                    formatter:function(value,row,index) {
                    	if(row.status == 4 && row.waitingStatus == 1) {
                        	return '未排期';
                    	} else if(row.waitingStatus == 2) {
                        	return '排期未上架';
                        } else if(row.waitingStatus == 3) {
                        	return '排期已上架';
                        }
                    }
                },
                {field:'hidden',  title:'操作', width:13,align:'center',
                    formatter:function(value,row,index) {
                    	if(row.status == 2) {
                        	return '<a href="javaScript:;" onclick="openWindow(' + row.sellerProductId + ',1)">审核</a>';
                    	}
                        else {
                        	var a = '<a href="javaScript:;" onclick="openWindow(' + row.productCheckSnapshotId + ',2)">查看</a>';
                        	if(row.status == 4 && row.waitingStatus == 1) {
                            	return a + '&nbsp;&nbsp;&nbsp;&nbsp;<a href="javaScript:;" onclick="updateWaitingStatus(' + row.sellerProductId + ',2)">排期</a>';
                        	}
                        	return a;
                        }
                    }
                }
            ]],
            pagination:true
        });
	});
	// 更新排期状态
	function updateWaitingStatus(sellerProductId, waitingStatus) {
		$.messager.confirm('提示','是否标记改商品排期？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/productCheck/updateWaitingStatus',
		            type: 'post',
		            dataType: 'json',
		            data: {'sellerProductId':sellerProductId,'waitingStatus':waitingStatus},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
		                	$('#s_data').datagrid('reload');
		                }else{
		                	$.messager.alert("提示",data.msg,"info");
		                }
		            },
		            error: function(xhr){
		            	$.messager.progress('close');
		            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		            }
		        });
			}
		});
	}
	// 审核信息
	function openWindow(id,type){
		window.open("${rc.contextPath}/productCheck/checkProductInfo/"+id+"/"+type);
	}
</script>
</body>
</html>