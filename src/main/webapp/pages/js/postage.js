/**
 * 新增邮费模板相关js
 */
$('#edit_name').validatebox({
    required: true
});

$('#addItem').dialog({
    title:'信息添加',
    collapsible:true,
    closed:true,
    modal:true,
    buttons:[{
        text:'保存信息',
        iconCls:'icon-ok',
        handler:function(){
            $('#af').form('submit',{
//                url:"/liziSpecial/addOrEditSingle",
                success:function(data){
                    var res = eval("("+data+")")
                    if(res.status == 1){
                        $('#addItem').dialog('close');
                        $.messager.alert('响应信息',"保存成功",'info',function(){
                            $('#s_data').datagrid('reload');
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
            $('#addItem').dialog('close');
        }
    }]
})

