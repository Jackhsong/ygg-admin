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

<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
	
<div data-options="region:'center',title:'content'" style="padding: 5px;">
	<div title="海外购合并订单记录" class="easyui-panel" style="padding: 10px">
		<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
			<form action="${rc.contextPath}/overseasOrder/exportHBOrderRecord" id="searchForm" method="post">
				<table>
					<tr>
						<td>
							&nbsp;子订单号：
							<input id="sonNumber" name="sonNumber" value="" />	
						</td>
						<td>
							&nbsp;合并订单号：
							<input id="hbNumber" name="hbNumber" value="" />	
						</td>
						<td>
							&nbsp;起始时间：
							<input id="createTimeBegin" name="createTimeBegin" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
						</td>
                        <td>
                            &nbsp;截止时间：
                            <input id="createTimeEnd" name="createTimeEnd" value="" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                        </td>
					</tr>
					<tr>
					<td>
						&nbsp;&nbsp;
						<a id="searchBtn" onclick="searchInfo()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						&nbsp;&nbsp;
						<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
						&nbsp;&nbsp;<a id="exportBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a>
					</td>
					<td>
					</td>
					</tr>
				</table>
			</form>
		</div>
			
		<div class="selloff_mod">
			<table id="s_data">

			</table>
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

function exportAll(){
	var sonNumber=$.trim($("#sonNumber").val());
	var hbNumber=$.trim($("#hbNumber").val());
	var createTimeBegin=$.trim($("#createTimeBegin").val());
	if(sonNumber == '' && hbNumber=='' && createTimeBegin==''){
		$.messager.confirm('导出','确定全部导出吗？全部导出由于数据量大，将会等待较长时间，建议根据创建时间筛选导出',function(b){
			if(b){
				$('#searchForm').submit();
			}
		});
	}else{
		$('#searchForm').submit();
	}
}

function searchInfo(){
	$('#s_data').datagrid('load',{
		sonNumber:$("#sonNumber").val(),
		hbNumber:$("#hbNumber").val(),
        createTimeBegin:$("#createTimeBegin").val(),
        createTimeEnd:$("#createTimeEnd").val()
	});
}

function clearSearch(){
	$("#sonNumber").val('');
	$("#hbNumber").val('');
	$("#creatTime").val('');
	$('#s_data').datagrid('load',{});
}

function deleteIt(id){
    $.messager.confirm("提示信息","确定删除么？",function(re){
        if(re){
            $.messager.progress();
            $.post("${rc.contextPath}/overseasOrder/deleteHBOrderRecord",{id:id},function(data){
                $.messager.progress('close');
                if(data.status == 1){
                    $.messager.alert('响应信息',"删除成功...",'info',function(){
                        $('#s_data').datagrid('reload');
                        return
                    });
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
	
	$('#s_data') .datagrid({
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'id',
		url : '${rc.contextPath}/overseasOrder/jsonhbOrderList',
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : true,
		pageSize : 50,
		pageList : [ 50, 60 ],
		columns : [ [
			{field : 'createTimeStr', title : '添加时间', width : 20, align : 'center'},
			{field : 'hbNumber', title : '合并订单号', width : 40, align : 'center'},
			{field : 'sonNumber', title : '子订单号', width : 100, align : 'center'}
		] ],
		toolbar:[{
	        id:'_add',
	        text:'新增',
	        iconCls:'icon-add',
	        handler:function(){
	        	$("#updateIdCard_form_id").val("");
            	$("#updateIdCard_form_idCard").val("");
            	$("#updateIdCard_form_oldName").val("");
            	$("#updateIdCard_form_realName").val("");
	        	$('#updateIdCard').dialog('open');
	        }
        }],
		pagination : true,
        rownumbers:true
	});  
});
</script>

</body>
</html>