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
		<div data-options="region:'north',title:'日退款率统计管理',split:true" style="height: 100px;">
			<div style="height: 40px;padding: 10px">
	            <table class="search">
	                <tr>
	                    <td class="searchName">订单付款时间：</td>
	                    <td class="searchText"><input value="${date}" id="date" name="date" onClick="WdatePicker({dateFmt: 'yyyy-MM',minDate:'2015-03'})" width="220px;"/></td>
	                	<td class="searchText">
							<a id="searchBtn" onclick="searchList()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;
							<a id="clearBtn" onclick="exportList()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">导出</a>
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

	function searchList(){
		$('#s_data').datagrid('load',{
			date:$("#date").val()
		});
	}
	
	function exportList() {
		$.messager.confirm('提示', '确定导出列表', function(r) {
			if(r) {
				window.open('${rc.contextPath}/customerStatistics/exportListOfDay?date='+$('#date').val());
			}
		});
	}
	
	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customerStatistics/refundListOfDay',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            sortname: 'vcd',
            sortorder:'desc',
            remoteSort: false,
            singleSelect:true,
            pageSize:50,
            columns:[[
				{field:'day',    title:'付款日期', width:20,align:'center'},
            	{field:'count',    title:'订单数', width:20, align:'center'},
                {field:'realMoney',    title:'实付金额', width:30, align:'center'},
                {field:'realMoney1',    title:'仅退款金额', width:30, align:'center',
                	formatter:function(value,row,index) {
                		var res = (isNaN(row.realMoney1) ? 0 : row.realMoney1);
                		return res;
                	}
                },
                {field:'he',    title:'仅退款率', width:20, align:'center',
                	formatter:function(value,row,index){
                		var realMoney = (isNaN(row.realMoney) ? 0 : row.realMoney);
                		var realMoney1 = (isNaN(row.realMoney1) ? 0 : row.realMoney1);
                		var res = ((realMoney1 * 100 / realMoney).toFixed(2));
                		res = isNaN(res) ? 0 : res;
                		return res + '%';
                	}
                },
                {field:'realMoney2',    title:'退款并退货金额', width:20, align:'center',
                	formatter:function(value,row,index){
                		return (isNaN(row.realMoney2) ? 0 : row.realMoney2);
                	}
                },
                {field:'eeg',  title:'退货并退款率', width:30,align:'center',
                	formatter:function(value,row,index){
                		var realMoney = (isNaN(row.realMoney) ? 0 : row.realMoney);
                		var realMoney2 = (isNaN(row.realMoney2) ? 0 : row.realMoney2);
                		var res = ((realMoney2 * 100 / realMoney).toFixed(2));
                		res = isNaN(res) ? 0 : res;
                		return res  + '%';
                	}	
                },
                {field:'vdv',  title:'合计退款金额', width:30,align:'center',
                	formatter:function(value,row,index){
                		var realMoney1 = (isNaN(row.realMoney1) ? 0 : row.realMoney1);
                		var realMoney2 = (isNaN(row.realMoney2) ? 0 : row.realMoney2);
                		var res = (((realMoney1 * 100) + (realMoney2 * 100)) / 100).toFixed(2);
                		res = isNaN(res) ? 0 : res;
                		return res;
                	}
                },
                {field:'vcd',  title:'合计退款率', width:30,align:'center',sortable: true,
                	formatter:function(value,row,index){
                		var realMoney = (isNaN(row.realMoney) ? 0 : row.realMoney);
                		var realMoney1 = (isNaN(row.realMoney1) ? 0 : row.realMoney1);
                		var realMoney2 = (isNaN(row.realMoney2) ? 0 : row.realMoney2);
                		var res = ((realMoney1 + realMoney2) * 100 / realMoney).toFixed(2);
                		res = isNaN(res) ? 0 : res;
                		res = Number(res);
                		row.vcd = res;
                		return res + '%';
                	}	
                }
            ]],
            pagination:true
        });
});
</script>
</body>
</html>