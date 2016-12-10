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

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'条件筛选-用户积分管理',split:true" style="height: 130px;">
			<br>
			<form id="searchForm" action="${rc.contextPath}/account/export" method="post">
				<table>
					<tr>
						<td>&nbsp;用户ID：</td>
						<td><input name="accountId" value="" /></td>
						<td>&nbsp;用户名：</td>
						<td><input name="searchName" value="" /></td>
						<td>&nbsp;手机号：</td>
						<td><input name="searchMobileNumber" value="" /></td>
					</tr>
					<tr>
						<td class="searchName">创建时间：</td>
			            <td class="searchText">
							<input value="" id="startTimeBegin" name="startTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
							-
							<input value="" id="startTimeEnd" name="startTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
						</td>
						<td></td>
						<td>&nbsp;<a id="searchBtn" onclick="searchIntegral()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
							<td>&nbsp;&nbsp;<a id="exportBtn" onclick="exportIntegral()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出结果</a></td>
						<td></td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
		</div>
		
		<!-- dialog begin -->
		<div id="editIntegralDiv" class="easyui-dialog" style="width:550px;height:250px;padding:10px 10px;">
            <form id="editIntegralDivForm" method="post">
				<input type="hidden" name="editId" value="" >
	            <table cellpadding="5">
	                <tr>
	                    <td>&nbsp;用户ID：</td>
	                    <td><span id="spanUserId"></span></td>
	                    <td>&nbsp;用户名：</td>
	                    <td><span id="spanUsername"></span></td>
	                </tr>    
	                <tr>
	                    <td>用户类型：</td>
	                    <td><span id="spanUserType"></span></td>
	                    <td>积分余额：</td>
	                    <td><span id="spanUserIntegral"></span></td>
	                </tr>
	                <tr>
	                    <td>调增积分：</td>
	                    <td colspan="3"><input name="integral" style="width:300px;"/><span>&nbsp;注：调减请使用负数</span></td>
	                </tr>
	                <tr>
	                    
	                </tr>
	                <tr>
	                    <td>调整原因：</td>
	                    <td colspan="3"><input name="reason" type="text" maxlength="30" style="width:300px;"/></td>
	                </tr>
	            </table>
        	</form>
        </div>
        <!-- dialog end -->
        
   		<!-- 批量操作积分 -->
   		<div id="batchEditIntegral_div" style="width:600px;height:180px;padding:20px 20px;">
   			<form method="post" id="batchEditIntegralForm" enctype="multipart/form-data">
    			<table cellpadding="5">
	    			<tr>
	    				<td><a onclick="downloadTemplate()" href="javascript:;" class="easyui-linkbutton">下载模板</a></td>
	    				<td><input type="text" id="batchEditIntegralForm_userId" name="userFile" style="width:300px"></td>
	    			</tr>
    			</table>
   			</form>
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

function searchIntegral(){
	$('#s_data').datagrid('load', {
		accountId : $("#searchForm input[name='accountId']").val(),
		name : $("#searchForm input[name='searchName']").val(),
		mobileNumber : $("#searchForm input[name='searchMobileNumber']").val(),
		startTimeBegin : $("#startTimeBegin").val(),
		startTimeEnd : $("#startTimeEnd").val()
	});
}

function exportIntegral(){
	$("#searchForm").submit();
}

function editIntegral(index){
	var arr = $("#s_data").datagrid("getData");
	$("#editIntegralDivForm input[name=editId]").val(arr.rows[index].id);
	$("#spanUserId").text(arr.rows[index].id);
	$("#spanUsername").text(arr.rows[index].name);
	$("#spanUserType").text(arr.rows[index].typeStr);
	$("#spanUserIntegral").text(arr.rows[index].integral);
	$("#editIntegralDivForm input[name=integral]").val("");
	$("#editIntegralDiv").dialog('open');
}

function showDetail(id){
	var urlStr="${rc.contextPath}/account/integralRecordList/"+id;
	window.open(urlStr,"_blank");
}

function downloadTemplate(){
	window.location.href="${rc.contextPath}/account/downloadIntegralTemplate";
}

$(function(){
	//integral dialog  begin
	$('#editIntegralDiv').dialog({
		title:'积分调整',
		collapsible:true,
		closed:true,
		modal:true,
		buttons:[{
		    text:'保存',
		    iconCls:'icon-ok',
		    handler:function(){
		    	var id = $("#editIntegralDivForm input[name='editId']").val();
		    	var addIntegral = $("#editIntegralDivForm input[name='integral']").val();
		    	var reason = $("#editIntegralDivForm input[name='reason']").val();
		    	var maxIntegral = 2100000000;
		    	if(!/^-?[1-9]+\d*$/.test(addIntegral)){
		    		$.messager.alert("提示","积分必须是不以零开头的整数","info");
		    	}
		    	else if(Math.abs(parseInt(addIntegral)) > maxIntegral){
		    		$.messager.alert("提示","不允许的积分操作","info");
		    	}else if($.trim(reason) == ''){
		    		$.messager.alert("提示","请填写调整原因","info");
		    	}
		    	else{
		    		$.messager.progress();
					$.ajax({
						url: '${rc.contextPath}/account/updateIntegral',
						type: 'post',
						dataType: 'json',
						data: {'id':id,'addIntegral':addIntegral,'reason':reason},
						success: function(data){
							$.messager.progress('close');
							if(data.status == 1){
								$('#s_data') .datagrid("load");
								$('#editIntegralDiv').dialog('close');
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
		    }
		},{
		    text:'取消',
		    align:'left',
		    iconCls:'icon-cancel',
		    handler:function(){
		        $('#editIntegralDiv').dialog('close');
		    }
		}]
     });
	//integral dialog end
	
	    $('#batchEditIntegral_div').dialog({
	    	title: '向批量用户ID发放优惠券',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '发送',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#batchEditIntegralForm').form('submit',{
	    				url: "${rc.contextPath}/account/importAccountIntegral",
	    				onSubmit:function(){
	    					var userId = $("#batchEditIntegralForm_userId").filebox("getValue");
	    					if(userId == ""){
	    						$.messager.alert("info","请选择文件","warn");
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"导入成功",'info',function(){
	                            	$("#batchEditIntegralForm_userId").filebox('clear');
	                                $('#s_data').datagrid('reload');
	                                $('#batchEditIntegral_div').dialog('close');
	                            });
	                        } else{
	                            $.messager.alert('响应信息',res.msg,'info');
	                        } 
	    				}
	    			});
	    		}
	    	},
	    	{
	    		text:'取消',
	            align:'left',
	            iconCls:'icon-cancel',
	            handler:function(){
	            	$("#batchEditIntegralForm_userId").filebox('clear');
                    $('#batchEditIntegral_div').dialog('close');
	            }
	    	}]
	    });	
	
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/account/jsonIntegral',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,100],
        columns:[[
            {field:'id',    title:'ID', width:30, align:'center'},
            {field:'name',    title:'用户名', width:50, align:'center'},
            {field:'createTime',    title:'创建时间', width:50, align:'center'},
            {field:'mobileNumber',    title:'手机号', width:50, align:'center'},
            {field:'typeStr',     title:'用户类型',  width:35,   align:'center' },
            {field:'integral',    title:'积分余额', width:30, align:'center'},
            /* {field:'lastIntegralUpdateTime',    title:'最后更新时间', width:50, align:'center'}, */
            {field:'hidden',  title:'操作', width:80,align:'center',
                formatter:function(value,row,index){
                    var a = '<a href="javaScript:;" onclick="showDetail(' + row.id + ')">查看明细</a>';
                    var b = ' | <a href="javaScript:;" onclick="editIntegral(' + index + ')">调整积分</a>';
                    return a+b;
                }
            }
        ]],
        toolbar:[{
            iconCls: 'icon-add',
            text:'批量操作积分',
            handler: function(){
            	$('#batchEditIntegral_div').dialog('open');
            }
        }],
        pagination:true
    });
	
	$("input[name='userFile']").each(function(){
		$(this).filebox({
			buttonText: '打开文件',
			buttonAlign: 'right'
		});
	});
});
</script>

</body>
</html>