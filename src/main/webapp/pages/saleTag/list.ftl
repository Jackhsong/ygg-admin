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

	<div title="标签管理" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
		
		        </table>
		    </div>
		</div>
	</div>

</div>

<script>

	function editIt(index){
    	var arr=$("#s_data").datagrid("getData");
    	window.location.href = "${rc.contextPath}/saleTag/edit/"+arr.rows[index].id
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/saleTag/jsonInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', width:50, align:'center'},
                {field:'name',    title:'标签名称', width:70, align:'center'},
                {field:'image',     title:'图片',  width:50, height:80,  align:'center' },
                {field:'isAvailable',    title:'状态', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a>';
                        return lableStr;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增标签信息',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href = "${rc.contextPath}/saleTag/add"
                }
            }],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>