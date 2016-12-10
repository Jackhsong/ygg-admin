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
		<div data-options="region:'north',title:'商品过期管理',split:true" style="height: 80px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/product/exportResult" method="post" >
		            <table class="search">
		                <tr>
		                	<td class="searchName">商品ID：</td>
							<td class="searchText"><input id="searchId" name="searchId" value="" /></td>
		                	<td class="searchName">商品名称：</td>
		                	<td class="searchText"><input id="searchName" name="productName" value="" /></td>
		                	<td class="searchText">
								<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
								&nbsp;
								<!-- <a id="exportBtn" onclick="exportProduct()" href="javascript:;" class="easyui-linkbutton" >导出查询结果</a> -->
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

	function searchProduct(){
    	$('#s_data').datagrid('load',{
    		productId:$("#searchId").val(),
        	productName:$("#searchName").val()
    	});
	}
	
	function clearSearch(){
		$("#searchId").val('');
    	$("#searchName").val('');
	}

	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr="${rc.contextPath}/productBase/toAddOrUpdate?id="+arr.rows[index].id;
		window.open(urlStr,"_blank");
	}
	

	$(function(){
		$('#sellerId').combobox({
				panelWidth:350,
            	panelHeight:350,
				mode:'remote',
			    url:'${rc.contextPath}/seller/jsonSellerCode',   
			    valueField:'code',   
			    textField:'text'  
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/productBase/jsonExpireProductBaseInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect: false,
            pageSize:50,
            columns:[[
                {field:'id',    title:'基本商品ID', width:15,align:'center'},
                {field:'name',     title:'名称',  width:60,  align:'center'},
                {field:'manufacturerDate',     title:'生产日期',  width:30,   align:'center'},
                {field:'durabilityPeriod',     title:'保质期',  width:30,   align:'center'},
                {field:'expireDate',     title:'过期时间',  width:30,   align:'center' },
                {field:'remainDay',     title:'剩余天数',  width:20,   align:'center' },
                {field:'sellerName',     title:'商家',  width:40,   align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                       return '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a>';
                    }
                }
            ]],
            pagination:true
        });
      
	});

</script>

</body>
</html>