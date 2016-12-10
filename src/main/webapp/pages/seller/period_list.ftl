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
<style type="text/css">
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
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
		<div data-options="region:'north',title:'商家结算周期管理',split:true" style="height: 150px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/product/exportResult" method="post" >
		            <table class="search">
		                <tr>
		                	<td class="searchName">特卖ID：</td>
							<td class="searchText"><input id="searchSaleWindowId" name="searchSaleWindowId" value="" /></td>
							<td class="searchName">特卖名称：</td>
		                    <td class="searchText"><input type="text" id="searchSaleWindowName" name="searchSaleWindowName"/></td>
		                    <td class="searchName">特卖类型：</td>
		                    <td class="searchText">
		                    	<select name="searchSaleWindowType" id="searchSaleWindowType" style="width:173px;">
									<option value="-1">全部</option>
									<option value="1">单品</option>
									<option value="2">组合</option>
									<option value="3">自定义</option>
								</select>
		                    </td>
		                </tr>
		                <tr>
		                   	<td class="searchName">商家：</td>
		                	<td class="searchText"><input type="text" id="searchSellerName" name="searchSellerName"/></td>
		                	<td class="searchName">是否处理：</td>
		                	<td class="searchText">
		                		<select name="searchSaleWindowIsHide" id="searchSaleWindowIsHide" style="width:173px;">
									<option value="0">未处理</option>
									<option value="1">已处理</option>
								</select>
		                	</td>
		                	<td class="searchName"></td>
		                    <td class="searchText">
								<a id="searchBtn" onclick="searchSaleWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
		                	</td>
		                </tr>
		            </table>
		        </form>
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    	</div>
	</div>
</div>

<script>
		function searchSaleWindow(){
			$('#s_data').datagrid('load',{
				saleWindowId:$("#searchSaleWindowId").val(),
				saleWindowType:$("#searchSaleWindowType").val(),
				saleWindowName:$("#searchSaleWindowName").val(),
				sellerId:$("#searchSellerName").combobox('getValue'),
				isDealWith:$("#searchSaleWindowIsHide").val()

			});
		}
		
		function clearSearch(){
			$("#searchSaleWindowId").val(''),
			$("#searchSaleWindowType").find('option').eq(0).attr('selected','selected');
			$("#searchSaleWindowName").val('');
			$("#searchSellerName").combobox('clear');
			$("#searchSaleWindowIsHide").find('option').eq(0).attr('selected','selected');
			$('#s_data').datagrid('load',{});
		}

	function hide(id,dealWith){
		$.messager.confirm('处理',"确定处理所选特卖吗？",function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/seller/dealWithSaleWindow',
		            type: 'post',
		            dataType: 'json',
		            data: {'id':id,'dealWith':dealWith},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
		                	$('#s_data').datagrid('load');
		                	$.messager.alert("提示",'操作成功',"info");
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

	$(function(){
		
		$('#searchSellerName').combobox({   
		    url:'${rc.contextPath}/seller/jsonSellerCode?isAvailable=1',
		    valueField:'code',   
		    textField:'text'
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/seller/jsonSellerPeriodList',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'saleWindowId',    title:'特卖ID', width:30, align:'center'},
                {field:'displayId',    title:'组特或单品Id', width:30, align:'center'},
                {field:'typeStr',    title:'特卖类型', width:30, align:'center'},
                {field:'saleWindowName',     title:'特卖名称',  width:80,   align:'center' },
                {field:'sellerName',    title:'商家', width:70, align:'center'},
                {field:'settlement',    title:'商家结算周期', width:60, align:'center'},
                {field:'startTime',    title:'开始时间', width:60, align:'center'},
                {field:'endTime',    title:'结束时间', width:60, align:'center'},
                {field:'days',    title:'结束时间已过天数', width:30, align:'center'},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                    	if(row.dealWith==1){
                    		return '<a href="javaScript:;" onclick="hide(' + row.id + ','  + 0 +')">设为未处理</a>';
                    	}
                    	if(row.dealWith==0){
	                        return '<a href="javaScript:;" onclick="hide(' + row.id + ','  + 1 +')">设为已处理</a>';      
                    	}
                    }
                }
            ]],
            toolbar:[{
                text:'批量设为已处理',
                iconCls:'icon-remove',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('操作','确定处理所选特卖吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/seller/dealWithSaleWindow", //批量隐藏
									{ids: ids.join(","),dealWith:1},
									function(data){
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$.messager.alert('提示','操作成功', 'info');
										}else{
											$.messager.alert('提示',data.msg,"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的特卖',"error")
                    }
                }
            },'-',{
                text:'批量设为未处理',
                iconCls:'icon-remove',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('删除','确定处理所选特卖吗？',function(b){
                            if(b){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/seller/dealWithSaleWindow", //批量隐藏
									{ids: ids.join(","),dealWith:0},
									function(data){
										if(data.status == 1){
											$('#s_data').datagrid('load');
											$.messager.alert('提示','操作成功', 'info');
										}else{
											$.messager.alert('提示',data.msg,"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的特卖',"error")
                    }
                }
            }],
            pagination:true
        });
	})
</script>

</body>
</html>