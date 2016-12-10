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

	<div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
        <form id="searchForm" method="post" >
            <table>
                <tr>
                	<td>页面状态：</td>
                    <td>
                    	<select name="isAvailable" id="isAvailable">
                    		<option value="-1">全部</option>
                    		<option value="0">停用</option>
                    		<option value="1">可用</option>
                    	</select>
                    </td>
                    <td>页面标题</td>
                    <td>
                    	<input id="searchName" name="searchName" value="" />
                	</td>
                    <td>url</td>
                    <td>
                    	<input id="searchUrl" name="searchUrl" value="" />
                	</td>
                </tr>
				<tr>
					<td>
						<a id="searchBtn" onclick="searchPage()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                	</td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
            </table>
        </form>
    </div>
    <!--数据表格-->
    <table id="s_data" style=""></table>

</div>

<script>

	function searchPage(){
    	$('#s_data').datagrid('load',{
        	fileName:$("#searchUrl").val(),
        	name:$("#searchName").val(),
        	isAvailable:$("#isAvailable").val()
    	});
	}

	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr = "${rc.contextPath}/pageCustom/edit/"+arr.rows[index].id;
		window.open(urlStr,"_blank");
	}
	
	function preview(index){
		var arr=$("#s_data").datagrid("getData");
		window.open (arr.rows[index].pcUrl,"_blank")
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/pageCustom/jsonInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center'},
                {field:'isAvailable',    title:'使用状态', width:70, align:'center'},
                {field:'name',    title:'页面标题', width:70, align:'center'},
                {field:'fileName',     title:'URL',  width:50,   align:'center' },
                {field:'desc',     title:'备注',  width:50,   align:'center' },
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                    	var a = '';
                    	<!--为了兼容以前的数据，id<15的不允许修改-->
                    	if(row.id>15){
                    		a = '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a> | ';
                    	}
                        var b = '<a href="javaScript:;" onclick="preview(' + index + ')">预览</a>';
                        return a+b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增自定义页面',
                iconCls:'icon-add',
                handler:function(){
                    window.location.href = "${rc.contextPath}/pageCustom/addPageCustom"
                }
            }],
            pagination:true
        });
	})
</script>

</body>
</html>