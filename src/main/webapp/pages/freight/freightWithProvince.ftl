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
<input id="subjectId" type="hidden" name="id" value="${id?c}" >
<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">

<div title="包邮模板-具体省份邮费管理" class="easyui-panel" style="padding:10px">
        <div class="content_body">
            <div class="selloff_mod">
                <table id="s_data" >

                </table>
            </div>
        </div>
        <div id="updateFreight" class="easyui-dialog" style="width:350px;height:170px;padding:20px 20px;">
            <form id="af" method="post">
			<input id="editId" type="hidden" name="id" value="" >
            <table cellpadding="5">
                <tr>
                    <td>设置邮费:</td>
                    <td><input class="" id="edit_name" type="text" name="money" data-options="required:true" onblur="stringTrim(this);"></input></td>
                </tr>
            </table>
        </form>
        </div>
    </div>

</div>

<script>

function stringTrim(obj){
	$(obj).val($.trim($(obj).val()));	
}

function editIt(index){
	$("#edit_name").val('');
    var arr=$("#s_data").datagrid("getData");
    $("#editId").val(arr.rows[index].id);
    var tt=arr.rows[index].money;
	$("#edit_name").val(tt);
    $("#updateFreight").dialog('open');
}

	$(function(){
		$('#updateFreight').dialog({
            title:'修改邮费',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#af').form('submit',{
                        url:"${rc.contextPath}/postage/saveProvinceFreight",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#addTemplate').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                    $('#s_data').datagrid('reload');
                                    $('#s_data').datagrid('clearSelections');
                                    $('#updateFreight').dialog('close');
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
                    $('#updateFreight').dialog('close');
                }
            }]
        });
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/postage/jsonProvinceFreightInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            queryParams: {
                id: $("#subjectId").val()
            },
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'province',    title:'省份', width:70, align:'center'},
                {field:'money',    title:'运费', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a>';
                        return lableStr;
                    }
                }
            ]],
            toolbar:[{
                   iconCls: 'icon-edit',
                   text:'批量修改邮费',
                   handler: function(){
                       var rows = $('#s_data').datagrid("getSelections");
                       if(rows.length > 0){
                           var ids = [];
                           for(var i=0;i<rows.length;i++){
                               ids.push(rows[i].id)
                           }
                           var id = ids.join(",");
                           $("#editId").val(id);
                           $("#edit_name").val('');
                           $("#updateFreight").dialog('open');
                       }else{
                           $.messager.alert('提示','请选择要操作的商品',"error")
                       }
                   }
                }],           
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>