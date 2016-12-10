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
textarea{
	resize:none;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">

	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'条件筛选-用户反馈管理',split:true" style="height: 80px;">
        <form id="searchForm" action="${rc.contextPath}/account/exportProposeInfo" method="post">
			<div style="height: 60px;padding: 10px">
				<span>用户Id：</span>
				<span><input id="searchAccountId" name="accountId"/></span>
				<span>用户名：</span>
				<span><input id="searchAccountName" name="accountName"/></span>
				<span>反馈类型：</span>
				<span>
					<input id="searchProposeType" name="proposeType"/>
				</span>
				<span>是否处理：</span>
				<span>
					<select id="searchIsDeal" name="isDeal">
						<option value="-1">全部</option>
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</span>
				<span>是否返积分：</span>
				<span>
					<select id="searchIsBackPoint" name="isBackPoint">
						<option value="-1">全部</option>
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
				</span>
				<span>
					<a id="searchBtn" onclick="searchSpread()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
					<a id="clearBtn" onclick="clearSpread()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>&nbsp;
					<a id="exportBtn" onclick="exportIntegral()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出结果</a>
				</span>			
			</div>			
        </form>
		</div>
		<div data-options="region:'center'" >
	    	<!--数据表格-->
	    	<table id="s_data" style=""></table>
    	</div>
    	
    	<!-- 返积分begin -->
 		<div id="returnScoreDiv" class="easyui-dialog" style="width:350px;height:150px;padding:10px 10px;">
			<form action="" id="scoreForm" method="post">
				<input id="scoreForm_id" type="hidden" name="id" value=""/>
				<input id="scoreForm_accountId" type="hidden" name="accountId" value=""/>
				<p>
					<span>回馈积分：</span>
					<span><input type="text" id="scoreForm_backPoint" name="backPoint" value="" maxlength="5" onblur="stringTrim(this);"/></span>
					<font color="red">&lt;200</font>
				</p>
			</form>
		</div>
		<!-- 返积分end -->
		  	
		<!-- 处理begin -->
 		<div id="updateDealContentDiv" class="easyui-dialog" style="width:400px;height:250px;padding:10px 10px;">
			<form action="" id="dealContentForm" method="post">
				<input id="dealContentForm_id" type="hidden" name="id" value=""/>
				<p>
					<span>处理说明：</span>
					<span><textarea id="dealContentForm_content" name="dealContent" rows="5" cols="30" onkeydown="checkEnter(event)"></textarea></span>
				</p>
			</form>
		</div>   	
    	<!-- 处理end -->
    	
	</div>
	
</div>

<script type="text/javascript">

    function exportIntegral(){
        $("#searchForm").submit();
    }

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

function stringTrim(obj){
	var value = $(obj).val();
	$(obj).val($.trim(value));
}

function searchSpread(){
	$("#s_data").datagrid('load',{
		accountId:$("#searchAccountId").val(),
		accountName:$("#searchAccountName").val(),
        proposeType:$("#searchProposeType").combobox('getValue'),
		isDeal:$("#searchIsDeal").val(),
		isBackPoint:$("#searchIsBackPoint").val()
	});
}

function clearSpread(){
	$("#searchAccountId").val('');
	$("#searchAccountName").val('');
	$("#searchProposeType").combobox('clear');
	$("#searchIsDeal").find("option").eq(0).attr("selected","selected");
	$("#searchIsBackPoint").find("option").eq(0).attr("selected","selected");
	$("#s_data").datagrid('load',{});
}

function cleanReturnScoreDiv(){
	$("#scoreForm_id").val('');
	$("#scoreForm_accountId").val('');
	$("#scoreForm_backPoint").val('');
}
function editIt(index){
	cleanReturnScoreDiv();
    var arr=$("#s_data").datagrid("getData");
    $("#scoreForm_id").val(arr.rows[index].id);
    $("#scoreForm_accountId").val(arr.rows[index].accountId);
    $("#returnScoreDiv").dialog('open');
}

function updateDealContent(index){
	$("#dealContentForm_id").val('');
	$("#dealContentForm_content").val('');
    var arr=$("#s_data").datagrid("getData");
    $("#dealContentForm_id").val(arr.rows[index].id);
    $("#dealContentForm_content").val(arr.rows[index].dealContent);
    $("#updateDealContentDiv").dialog('open');
}


$(function(){
	
	$("#searchProposeType").combobox({
		url:'${rc.contextPath}/account/jsonProposeTypeCode',   
	    valueField:'code',   
	    textField:'text' 
	});
	
	
    $('#s_data').datagrid({
    	nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/account/jsonProposeInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        columns: [
            [
                {field: 'accountId', title: '用户Id', width: 15, align: 'center'},
                {field: 'accountName', title: '用户名', width: 30, align: 'center'},
                {field: 'contact', title: '联系方式', width: 25, align: 'center'},
                {field: 'proposeType', title: '反馈类型', width: 20, align: 'center'},
                {field: 'os', title: '操作系统', width: 20, align: 'center'},
                {field: 'version', title: '应用版本', width: 20, align: 'center'},
                {field: 'createTime', title: '反馈时间', width: 30, align: 'center'},
                {field: 'content', title: '反馈内容', width: 70, align: 'center'},
                {field: 'isDealDesc', title: '是否处理', width: 20, align: 'center'},
                {field: 'dealUser', title: '处理人', width: 20, align: 'center'},
                {field: 'dealContent', title: '处理说明', width: 60, align: 'center'},
                {field: 'isBackPointDesc', title: '是否已返积分', width: 20, align: 'center'},
                {field: 'hidden', title: '操作', width: 25, align: 'center',
                    formatter: function (value, row, index) {
                    	var a = '';
                    	var b = '';
                    	if(row.isDeal == 1){
                    		a = '<a href="javaScript:;" onclick="updateDealContent(' + index + ')">更新处理</a> | '
                    	}else{
                    		a = '<a href="javaScript:;" onclick="updateDealContent(' + index + ')">处理反馈</a> | '
                    	}
                    	
                    	if(row.isBackPoint == 1){
                    		b = row.backPoint + '积分';
                    	}else{
                    		b = '<a href="javaScript:;" onclick="editIt(' + index + ')">返积分</a>'
                    	}
                    	return a + b;
                    }
                }
            ]
        ],
        pagination:true
    });

    $('#updateDealContentDiv').dialog({
    	title:"反馈处理",
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#dealContentForm').form('submit',{
    				url: "${rc.contextPath}/account/updateDealContent",
    				onSubmit:function(){
    					var content = $('#dealContentForm_content').val();
    					if($.trim(content)=='' || content.length>200){
    						$.messager.alert("提示","处理说明不能为空且字数必须小于200","warning");
    						return false;
    					}
    					$.messager.progress();
    				},
    				success: function(data){
    					$.messager.progress('close');
                        var res = eval("("+data+")");
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                                $('#s_data').datagrid('reload');
                                $('#updateDealContentDiv').dialog('close');
                            });
                        } else if(res.status == 0){
                            $.messager.alert('响应信息',res.msg,'error');
                        } 
    				},
    				error: function(xhr){
			         	$.messager.progress('close');
			        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
			       }
    			});
    		}
    	},
    	{
    		text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#updateDealContentDiv').dialog('close');
            }
    	}]
    }); 
    
    $('#returnScoreDiv').dialog({
    	title:"积分回馈",
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#scoreForm').form('submit',{
    				url: "${rc.contextPath}/account/returnScoreForPropose",
    				onSubmit:function(){
    					var score = $('#scoreForm_backPoint').val();
    					if($.trim(score)==''){
    						$.messager.alert("提示","请输入积分","warning");
    						return false;
    					}else if(!/\d+$/.test(score)){
    						$.messager.alert("提示","积分只能为正整数","warning");
    						return false;
    					}else if(parseInt(score) > 200){
    						$.messager.alert("提示","积分必须小于200","warning");
    						return false;
    					}
    					$.messager.progress();
    				},
    				success: function(data){
    					$.messager.progress('close');
                        var res = eval("("+data+")");
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                                $('#s_data').datagrid('reload');
                                $('#returnScoreDiv').dialog('close');
                            });
                        } else if(res.status == 0){
                            $.messager.alert('响应信息',res.msg,'error');
                        } 
    				},
    				error: function(xhr){
			         	$.messager.progress('close');
			        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
			       }
    			});
    		}
    	},
    	{
    		text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#returnScoreDiv').dialog('close');
            }
    	}]
    });   

});

</script>

</body>
</html>