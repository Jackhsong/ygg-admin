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

	<div title="组合特卖管理" class="easyui-panel" style="padding:10px">
		<div class="content_body">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 40px;padding: 15px">
        <form id="searchForm" method="post" >
            <table>
                <tr>
                    <td>组合特卖标题</td>
                    <td><input id="searchName" name="name" value="" /></td>
                    <td>组合状态：</td>
                    <td>
                    <select name="isAvailable" id="isAvailable">
                    	<option value="0">停用</option>
                    </select>
                    </td>
					<td>
						&nbsp;&nbsp;<a id="searchBtn" onclick="searchAcCommon()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                	</td>
                </tr>
            </table>
        </form>
    </div>

		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>

</div>

<script>

	function searchAcCommon(){
    	$('#s_data').datagrid('load',{
        	name:$("#searchName").val(),
        	isAvailable:$("#isAvailable").val(),
    	});
	}

	function editIt(index){
    	var arr=$("#s_data").datagrid("getData");
    	var urlStr = "${rc.contextPath}/sale/editGroupSale/"+arr.rows[index].id;
    	window.open(urlStr,"_blank");
	}

	function productManage(index){
    	var arr=$("#s_data").datagrid("getData");
    	var urlStr = "${rc.contextPath}/sale/groupSaleProductManage/"+arr.rows[index].id;
    	window.open(urlStr,"_blank");
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sale/jsonInfo',
            queryParams:{
            	isAvailable:0
            },
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'特卖ID', align:'center'},
				{field:'isAvailable',    title:'状态', width:70, align:'center'},
                {field:'name',    title:'组合特卖标题', width:70, align:'center'},
                {field:'desc',    title:'描述', width:70, align:'center'},
                {field:'gegesay',    title:'格格说', width:70, align:'center'},
                {field:'weixin',    title:'微信分享标题', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a>';
                        var b = ' | <a href="javaScript:;" onclick="productManage(' + index + ')">管理商品</a>';
                        return a + b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增组合特卖信息',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href = "${rc.contextPath}/sale/addGroupSale"
                }
            }],
            pagination:true
        });
	})
</script>

</body>
</html>