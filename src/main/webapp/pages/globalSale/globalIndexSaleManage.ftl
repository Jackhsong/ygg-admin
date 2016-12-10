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
            <table>
            	<tr>
            		<td>特卖状态：</td>
                    <td>
                    <select id="saleStatus" name="saleStatus" style="width:173px;">
                    	<option value="-1">所有状态</option>
                    	<option value="1">即将开始</option>
                    	<option value="2">进行中（早场和晚场）</option>
                    	<option value="3">已结束</option>
                    </select>
                    </td>
                    
                    <td>&nbsp;特卖类型：</td>
                    <td>
                    <select id="type" name="type" style="width:173px;">
                    	<option value="-1">全部</option>
                    	<option value="1">单品</option>
                    	<option value="2">组合</option>
                    </select>
                    </td>
                    <td>&nbsp;特卖名称：</td>
                    <td><input id="name" name="name" value="" /></td>
                    <td>&nbsp;开售档期：</td>
                    <td><input id="startTime" name="startTime" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})" /></td>
                    <td>&nbsp;商家：</td>
		            <td><input id="sellerId" type="text" name="sellerId" /></td>
                </tr>
                <tr>
                    <td>展现状态：</td>
                    <td>
                    	<select id="isDisplay" name="isDisplay" style="width:173px;">
	                    	<option value="-1">全部</option>
	                    	<option value="0">不展现</option>
	                    	<option value="1">展现</option>
                    	</select>
                    </td>
                    <td>&nbsp;商品Id：</td>
                    <td><input id="productId" name="productId" value="" /></td>
                    <td>&nbsp;商品名称：</td>
                    <td><input id="productName" name="productName" value="" /></td>
                    <td>&nbsp;品牌：</td>
                    <td><input id="brandId" type="text" name="brandId" ></input></td>
					<td>
						&nbsp;<a id="searchBtn" onclick="searchSaleWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                	</td>
                	<td></td>
                </tr>
            </table>
        </form>
    </div>
    <!--数据表格-->
    <table id="s_data" style=""></table>

</div>

<script>

	function searchSaleWindow(){
    	$('#s_data').datagrid('load',{
        	type:$("#type").val(),
        	name:$("#name").val(),
        	saleStatus:$("#saleStatus").val(),
        	isDisplay:$("#isDisplay").val(),
        	startTime:$("#startTime").val(),
        	productId:$("#productId").val(),
        	productName:$("#productName").val(),
        	brandId:$("input[name='brandId']").val(),
        	sellerId:$("#sellerId").combobox('getValue')
    	});
	}

	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr = "${rc.contextPath}/globalSale/edit/"+arr.rows[index].id;
		window.open(urlStr,"_blank");
	}
	
	function displayIt(id,code){
    	$.messager.confirm("提示信息","确定修改展现状态吗？",function(re){
        	if(re){
            	$.messager.progress();
            	$.post("${rc.contextPath}/globalSale/updateDisplayCode",{id:id,code:code},function(data){
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
            url:'${rc.contextPath}/globalSale/jsonSaleWindowInfo',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'特卖ID', width:20, align:'center'},
            	{field:'displayId',    title:'组特或单品ID', width:30, align:'center'},
                {field:'isDisplay',    title:'展现状态', width:30, align:'center'},
                {field:'saleStatus',    title:'特卖状态', width:30, align:'center'},
                {field:'order',    title:'排序值', width:30, align:'center'},
                {field:'type',    title:'特卖类型', width:30, align:'center'},
                {field:'relaRealSellerName',    title:'关联商家', width:60, align:'center'},
                {field:'name',     title:'名称',  width:50,   align:'center' },
                {field:'desc',     title:'特卖描述',  width:50,   align:'center' },
                {field:'stock',     title:'库存数量',  width:25,   align:'center' },
                {field:'startTime',     title:'开始时间',  width:50,   align:'center' },
                {field:'endTime',     title:'结束时间',  width:50,   align:'center' },
                {field:'descToWeb',     title:'查看',  width:20,   align:'center' },
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a> | ';
                        var b = '';
                        if(row.isDisplayCode == 1){
                        	b = '<a href="javaScript:;" onclick="displayIt(' + '\'' + row.id + '\',' + row.isDisplayCode + ')">不展现</a>';
                        }else{
                        	b = '<a href="javaScript:;" onclick="displayIt(' + '\'' + row.id + '\',' + row.isDisplayCode + ')">展现</a>';
                        }
                        return a+b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增特卖',
                iconCls:'icon-add',
                handler:function(){
                    window.location.href = "${rc.contextPath}/globalSale/addSale"
                }
            }],
            pagination:true
        });
		
		$('#brandId').combobox({
            panelWidth:350,
            panelHeight:350,
            url:'${rc.contextPath}/brand/jsonBrandCode',
            valueField:'code',
            textField:'text'
        });
		
		$('#sellerId').combobox({
			panelWidth:350,
        	panelHeight:350,
			mode:'remote',
		    url:'${rc.contextPath}/seller/jsonSellerCode',   
		    valueField:'code',   
		    textField:'text'  
		});
	});
		
</script>

</body>
</html>