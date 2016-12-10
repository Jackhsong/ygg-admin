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

<div title="包邮模板管理" class="easyui-panel" style="padding:10px">
    <div id="setMoneyDiv" style="height: 40px;padding: 15px">
        <form id="moneyForm" method="post" >
            <table>
                <tr>
                    <td>设置包邮金额：</td>
                    <td><input id="money" name="money" value="" /></td>
                    <td><a id="moneyBtn" onclick="updateMoney()" href="javascript:;" class="easyui-linkbutton">保存</a>
                    </td>
                </tr>
            </table>
        </form>
	</div>
        <div class="content_body">
            <div class="selloff_mod">
                <table id="s_data" >

                </table>
            </div>
        </div>
        <div id="addTemplate" class="easyui-dialog" style="width:650px;height:300px;padding:20px 20px;">
            <form id="af" method="post">
            <table cellpadding="5">
                <tr>
                    <td>模板名称:</td>
                    <td><input id="edit_name" type="text" name="name" data-options="required:true"></input></td>
                </tr>
                <tr>
                    <td>备注:</td>
                    <td><textarea name="desc" id="edit_desc" style="height: 60px;width: 300px"></textarea></td>
                </tr>
                <tr>
                    <td>使用状态:</td>
                    <td>
                        <input type="radio" name="isAvailable" value="1" /> 可用&nbsp;&nbsp;
						<input type="radio" name="isAvailable" value="0" /> 停用
                    </td>
                </tr>
            </table>
        </form>
        </div>
    </div>

</div>

<script>

	function editIt(index){
    	var arr=$("#s_data").datagrid("getData");
    	window.location.href = "${rc.contextPath}/postage/edit/"+arr.rows[index].id
	}
	
	function setFreight(index){
    	var arr=$("#s_data").datagrid("getData");
    	window.location.href = "${rc.contextPath}/postage/province/"+arr.rows[index].id
	}

	$(function(){
	
		$('#addTemplate').dialog({
            title:'邮费模板信息',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                 	$.messager.progress();
                    $('#af').form('submit',{
                        url:"${rc.contextPath}/postage/saveTemplate",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                             	$.messager.progress('close');
                                $('#addTemplate').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                    $('#s_data').datagrid('reload');
                                });
                            } else if(res.status == 0){
                            	$.messager.progress('close');
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                });
                            } else{
                            	$.messager.progress('close');
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
                    $('#addTemplate').dialog('close');
                }
            }]
        })
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/postage/jsonInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'id',    title:'序号', width:50, align:'center'},
                {field:'name',    title:'模板名称', width:70, align:'center'},
                {field:'desc',    title:'备注', width:70, align:'center'},
                {field:'available',     title:'状态',  width:50,   align:'center' },
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a>'
                                +"&nbsp;|&nbsp;"+'<a href="javaScript:;" onclick="setFreight(' + index + ')">设置邮费</a>';
                        return lableStr;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增运费模板',
                iconCls:'icon-add',
                handler:function(){
                	$("#edit_desc").val("");
                    $("#addTemplate").dialog('open').find('input[type=text]').val("");
                }
            }],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>