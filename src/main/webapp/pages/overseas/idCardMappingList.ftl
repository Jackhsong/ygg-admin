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
	<script type="text/javascript" src="http://www.w3cschool.cc/try/jeasyui/datagrid-detailview.js"></script>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
	
<div data-options="region:'center',title:'content'" style="padding: 5px;">
	<div title="海外购商品导单名称和价格管理" class="easyui-panel" style="padding: 10px">
		<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
			<form action="${rc.contextPath}/overseasOrder/exportIdCardMapping" id="searchForm" method="post">
				<table>
					<tr>
						<td>
							&nbsp;身份证号：
							<input id="idCard" name="idCard" value="" />	
						</td>
						<td>
							&nbsp;原姓名：
							<input id="oldName" name="oldName" value="" />	
						</td>
						<td>
							&nbsp;修改后姓名：
							<input id="realName" name="realName" value="" />	
						</td>
						<td>
							&nbsp;订单导出状态：
							<select id="status" name="status">
								<option value="-1">全部</option>
								<option value="1">OK</option>
								<option value="0">待添加真名</option>
							</select>
						</td>
						<td>
							&nbsp;添加来源：
							<select id="source" name="source">
								<option value="-1">全部</option>
								<option value="0">系统</option>
								<option value="1">app</option>
							</select>
						</td>
					</tr>
					<tr>
                        <td class="searchText">
							更新时间：
                            <input value="" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                            ~
                            <input value="" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                        </td>
						<td>
							&nbsp;&nbsp;
							<a id="searchBtn" onclick="searchInfo()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;&nbsp;<a id="searchBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a>
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
	
	<div id="updateIdCard" class="easyui-dialog" style="width:450px;height:370px;padding:20px 20px;">
        <form id="updateIdCard_form" method="post">
			<input id="updateIdCard_form_id" type="hidden" name="id" value="" >
            <table>
                <tr>
                    <td>身份证号:</td>
                    <td><input id="updateIdCard_form_idCard" name="idCard" style="width:280px" ></input></td>
                </tr>
                <tr>
                    <td>原姓名:</td>
                    <td><input id="updateIdCard_form_oldName" name="oldName"  style="width:280px" ></input></td>
                </tr>
                <tr>
                    <td>修改后姓名:</td>
                    <td><input id="updateIdCard_form_realName" name="realName" style="width:280px" ></input></td>
                </tr>
            </table>
        </form>
   </div>
   
</div>
	

<script>

function exportAll(){
	$('#searchForm').submit();
}

function searchInfo(){
	$('#s_data').datagrid('load',{
    	idCard:$("#idCard").val(),
    	oldName:$("#oldName").val(),
    	realName:$("#realName").val(),
        source:$("#source").val(),
        startTime:$("#startTime").val(),
        endTime:$("#endTime").val(),
    	status:$("#status").val()
	});
}

function deleteIt(id){
    $.messager.confirm("提示信息","确定删除么？",function(re){
        if(re){
            $.messager.progress();
            $.post("${rc.contextPath}/overseasOrder/deleteIdcard",{id:id},function(data){
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

function editProduct(index){
    var arr=$("#s_data").datagrid("getData");
    $("#updateIdCard_form_id").val(arr.rows[index].id);
	$("#updateIdCard_form_idCard").val(arr.rows[index].idCard);
	$("#updateIdCard_form_oldName").val(arr.rows[index].oldName);
	$("#updateIdCard_form_realName").val(arr.rows[index].realName);
    $("#updateIdCard").dialog('open');
}

$(function(){
	
	$('#s_data') .datagrid({
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'id',
		url : '${rc.contextPath}/overseasOrder/jsonIdCardMapping',
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : true,
		pageSize : 50,
		pageList : [ 50, 60 ],
		columns : [ [
			{field : 'statusMsg', title : '状态', width : 20, align : 'center'},
			{field : 'idCard', title : '身份证号', width : 40, align : 'center'},
			{field : 'oldName', title : '原姓名', width : 90, align : 'center'},
			{field : 'realName', title : '修改后姓名', width : 70, align : 'center'},
			{field : 'updateTime', title : '最近更新时间', width : 70, align : 'center'},
			{field : 'orderNumber', title : '来源订单号', width : 50, align : 'center'},
			{field : 'sellerName', title : '商家', width : 30, align : 'center'},
			{field : 'sendAddress', title : '发货地', width : 30, align : 'center'},
			{field : 'sourceDesc', title : '添加来源', width : 30, align : 'center'},
			{field : 'hidden', title : '操作', width : 80, align : 'center',
				formatter : function(value, row, index) {
					var f1 = '<a href="javaScript:;" onclick="editProduct(' + index + ')">修改</a>';
					var f2 = ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
            		return f1 + f2;
				}
			} 
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
		pagination : true
	});  

	$('#updateIdCard').dialog({
        title:'信息',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存信息',
            iconCls:'icon-ok',
            handler:function(){
                $('#updateIdCard_form').form('submit',{
                    url:"${rc.contextPath}/overseasOrder/saveIdCard",
                    success:function(data){
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                            	$("#updateIdCard_form_id").val("");
                            	$("#updateIdCard_form_idCard").val("");
                            	$("#updateIdCard_form_oldName").val("");
                            	$("#updateIdCard_form_realName").val("");
                                $('#updateIdCard').dialog('close');
                                $('#s_data') .datagrid('reload');
                            });
                        } else if(res.status == 0){
                            $.messager.alert('响应信息',res.msg,'info',function(){
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'error',function(){
                            });
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
            	$('#updateIdCard').dialog('close');
            	$("#updateIdCard_form_id").val("");
            	$("#updateIdCard_form_idCard").val("");
            	$("#updateIdCard_form_oldName").val("");
            	$("#updateIdCard_form_realName").val("");
            }
        }]
    });
	
});
</script>

</body>
</html>