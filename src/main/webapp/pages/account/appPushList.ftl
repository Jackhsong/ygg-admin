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
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
		<form id="searchForm" action="${rc.contextPath}/account/exportPushAccount" method="post">
			<table>
				<tr>
					<td>个推CID</td>
					<td><input name="cid" /></td>
					<td>用户ID</td>
					<td><input name="id" /></td>
					<td>用户名</td>
					<td><input name="name" /></td>
					<td>用户类型</td>
					<td>
						<select class="searchText" id="accountType" name="type">
	                    	<#list accountTypeList as type>
	                    		<option value="${type.code}">${type.name}</option>
	                    	</#list>
                    	</select>
					</td>
					<td>手机号</td>
					<td><input name="mobileNumber" /></td>
				</tr>
				<tr>
					<td class="searchName">创建时间：</td>
		            <td class="searchText">
						<input value="" id="startTimeBegin" name="startTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
						-
						<input value="" id="startTimeEnd" name="startTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
					</td>
					<td></td>
					<td><a id="searchBtn" onclick="searchAccount()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
					<td><a id="exportBtn" onclick="exportAccount()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出</a></td>
					<td></td>
					<td></td>
					<td></td>
				</tr>
			</table>
		</form>
	</div>

	<div title="推送用户列表" class="easyui-panel" style="padding:10px">
		<div class="content_body">
		    <div class="selloff_mod">
		        <table id="s_data" >
					
		        </table>
		    </div>
		</div>
	</div>
	
	<div id="push_div" class="easyui-dialog" style="width:600px;height:400px;padding:20px 20px;">
		<form id="pushForm" method="post" enctype="multipart/form-data">
			<input id="ids" type="hidden" name="ids" value="" >
			<input id="pids" type="hidden" name="pids" value="" >
			<input id="operationType" type="hidden" name="operationType" value="1" >
            <table cellpadding="5">
            	<tr id="customUserInfo" style="display:none">
                    <td class="searchName">用户ID：</td>
                    <td class="searchText">
                    	<input type="file" name="userIdFile" />
                    	<a onclick="downloadTemplate()" href="javascript:;" class="easyui-linkbutton">下载模板</a>
                    </td>
                </tr>
                <tr>
                    <td class="searchName">推送内容类型：</td>
                    <td class="searchText">
                    	<select class="searchText" id="pushType" name="pushType">
	                    	<#list pushTypeList as type>
	                    		<option value="${type.code}">${type.name}</option>
	                    	</#list>
                    	</select>
                    </td>
                </tr>    
                <tr id="urlTr" style="display:none">
                    <td class="searchName">URL：</td>
                    <td class="searchText"><input class="" id="pushUrl" name="pushUrl"  style="width:300px"></input></td>
                </tr>
                <tr  id="numberTr" style="display:none">
                    <td class="searchName">订单编号：</td>
                    <td class="searchText"><input class="" id="pushNumber" name="pushNumber" style="width:300px"></input></td>
                </tr>
                <tr  id="productIdTr" style="display:none">
                    <td class="searchName">商品Id：</td>
                    <td class="searchText"><input class="" id="pushProductId" name="pushProductId" style="width:300px"></input></td>
                </tr>
                <tr  id="windowIdTr" style="display:none">
                    <td class="searchName">专场Id：</td>
                    <td class="searchText"><input class="" id="pushWindowId" name="pushWindowId" style="width:300px"></input></td>
                </tr>
                <tr>
                    <td class="searchName">推送标题（仅安卓显示）：</td>
                    <td class="searchText">
                    	<input class=""   id="pushContent" name="pushContent"  style="width:300px"></input>
                    </td>
                </tr>
                <tr>
                	<td class="searchName">推送内容：</td>
                	<td class="searchText">
                		<textarea id="pushTitle" name="pushTitle" onkeydown="checkEnter(event)" style="height: 60px;width: 300px"></textarea>
                	</td>
                </tr>
                <tr id="showPeopleNums">
                	<td class="searchName">本次推送人数：</td>
                	<td class="searchText"><span id="pushPeople"></span></td>
                </tr>
            </table>
        </form>
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

	function downloadTemplate(){
		window.location.href="${rc.contextPath}/account/downloadAppPushTemplate";
	}

	//查询
	function searchAccount() {
		$('#s_data').datagrid('load', {
			id : $("#searchForm input[name='id']").val(),
			cid : $("#searchForm input[name='cid']").val(),
			name : $("#searchForm input[name='name']").val(),
			type : $("#accountType").val(),
			startTimeBegin : $("#startTimeBegin").val(),
			startTimeEnd : $("#startTimeEnd").val(),
			mobileNumber : $("#searchForm input[name='mobileNumber']").val()
		});
	}
	
	//禁止按回车
	function checkEnter(e){
		var et=e||window.event;
		var keycode=et.charCode||et.keyCode;
		if(keycode==13){
			if(window.event)
				window.event.returnValue = false;
			else
				e.preventDefault();//for firefox
		}
	}
	
	//导出
	function exportAccount() {
		$("#searchForm").submit();
	}

	//单个推送
	function pushIt(index){
		//设置操作为单个推送
		$("#operationType").val(1);
		var arr=$("#s_data").datagrid("getData");
		$("#ids").val(arr.rows[index].id);
		$("#pids").val(arr.rows[index].pushId);
        $("#pushPeople").text(1);
        //input复原
        $("#pushUrl").val('');
		$("#pushNumber").val('');
		$("#pushTitle").val('');
		$("#pushContent").val('');
		$("#customUserInfo").hide();
		$("#showPeopleNums").show();
        $('#push_div').dialog('open');
	}

	$(function(){
		
		$('#push_div').dialog({
            title:'推送',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'推送',
                iconCls:'icon-ok',
                handler:function(){
					
					$('#pushForm').form('submit',{
						url:"${rc.contextPath}/account/push",
					    onSubmit: function(){
					    	$.messager.progress();
					    },
					    success:function(data){
					    	$.messager.progress('close');
					    	var data = eval('(' + data + ')');  
							if(data.status == 1){
								$.messager.alert("提示","推送成功","info");
							}else{
								$.messager.alert("提示",data.msg,"info");
							}
					    }
					});
					
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#push_div').dialog('close');
                }
            }]
        });
	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/account/jsonAppPushList',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
				{field:'checkId',    title:'序号', align:'center',checkbox:true},
				{field:'pushId',    title:'个推CID', width:50, align:'center'},
                {field:'id',    title:'用户ID', width:20, align:'center'},
                {field:'createTime',    title:'创建时间', width:50, align:'center'},
                {field:'name',    title:'用户名', width:70, align:'center'},
                {field:'typeStr',     title:'用户类型',  width:50,   align:'center' },
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="pushIt(' + index + ')">推送</a>';
                        return lableStr;
                    }
                }
            ]],
            toolbar:[{
            	iconCls: 'icon-ok',
                text:'根据所选ID推送',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                    	var ids = [];
                        for(var i=0;i<rows.length;i++){
                            ids.push(rows[i].id)
                        }
                        var pids = [];
                        for(var i=0;i<rows.length;i++){
                        	pids.push(rows[i].pushId)
                        }
                        $("#ids").val(ids.join(","));
                        $("#pids").val(pids.join(","));
                        $("#pushPeople").text(ids.length);
                      	//设置操作为所选ID推送
                		$("#operationType").val(1);
                      	//input复原
                        $("#pushUrl").val('');
                		$("#pushNumber").val('');
                		$("#pushProductId").val('');
                		$("#pushWindowId").val('');
                		$("#pushTitle").val('');
                		$("#pushContent").val('');
                		$("#customUserInfo").hide();
                		$("#showPeopleNums").show();
                        $('#push_div').dialog('open');
                    }else{
                        $.messager.alert('提示','请选择要操作的用户',"error")
                    }
                }
            },'-',{
            	id:'_add',
                text:'自定义推送',
                iconCls:'icon-add',
                handler:function(){
                	//设置操作为所选ID推送
            		$("#operationType").val(2);
                	//input复原
                	$("#ids").val('');
                    $("#pids").val('');
                    $("#pushUrl").val('');
            		$("#pushNumber").val('');
            		$("#pushProductId").val('');
            		$("#pushWindowId").val('');
            		$("#pushTitle").val('');
            		$("#pushContent").val('');
                	$("#customUserInfo").show();
                	$("#showPeopleNums").hide();
                	$('#push_div').dialog('open');
                }
            }],
            pagination:true
        });
		
		//推送类型选择
		$("#pushType").change(function(){
			var type = $("#pushType").val();
			$("#urlTr").hide();
			$("#numberTr").hide();
			$("#productIdTr").hide();
			$("#windowIdTr").hide();
			
			if(type == 5){//自定义页面
				$("#urlTr").show();
				$("#pushUrl").val('');
			}else if(type == 4){ //订单详情页
				$("#numberTr").show();
				$("#pushNumber").val('');
			}else if(type == 6 || type == 7){
				$("#productIdTr").show();
				$("#pushProductId").val('');
			}else if(type == 8 || type == 9){
				$("#windowIdTr").show();
				$("#pushWindowId").val('');
			}else{
				$("#pushUrl").val('');
				$("#pushNumber").val('');
			}
		});
		
	})
</script>

</body>
</html>