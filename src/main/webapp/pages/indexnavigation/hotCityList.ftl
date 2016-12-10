<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理-首页分站点城市</title>
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
		<div data-options="region:'north',title:'首页分站点城市',split:true" style="height: 90px;">
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
		</div>
	</div>

    <div id="addHotCity" class="easyui-dialog" style="width:300px;height:200px;padding:20px 20px;">
        <form id="addHotCity_form" method="post">
            <table cellpadding="5">
                <tr>
                    <td>省:</td>
                    <td>
                        <select name="province" id="province">
                        <#list provinceList as bl >
                            <option value="${bl.provinceId}" >${bl.name}</option>
                        </#list>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>市:</td>
                    <td>
                        <select name="city" id="city">
                        </select>
                    </td>
                </tr>
            </table>
        </form>
    </div>

</div>

<script>

    function deleteIt(id){
        $.messager.confirm("提示信息","确认删除？",function(re){
            if(re){
                $.messager.progress();
                $.ajax({
                    url: '${rc.contextPath}/indexNavigation/deleteHotCityList',
                    type: 'post',
                    dataType: 'json',
                    data: {'id': id},
                    success: function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
                            $('#s_data').datagrid('load');
                        }else{
                            $('#s_data').datagrid('load');
                            $.messager.alert("提示",data.msg,"info");
                        }
                    },
                    error: function(xhr){
                        $.messager.progress('close');
                        $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                    }
                });
            }
        });
    }

    // 编辑排序
    function editSequence(index){
        $('#s_data').datagrid('beginEdit', index);
    };
    // 保存保存
    function saverow(index){
        $('#s_data').datagrid('endEdit', index);
    };
    // 取消编辑
    function cancelrow(index){
        $('#s_data').datagrid('cancelEdit', index);
    };
    // 跟新gird行数据
    function updateActions(){
        var rowcount = $('#s_data').datagrid('getRows').length;
        for(var i=0; i<rowcount; i++){
            $('#s_data').datagrid('updateRow',{
                index:i,
                row:{}
            });
        }
    }

    // 跟新导航排序值
    function updateActivity(row){
        $.ajax({
            url: '${rc.contextPath}/indexNavigation/updateHotCityByParam',
            type:"POST",
            data: {id:row.id,sequence:row.sequence},
            success: function(data) {
                if(data.status == 1){
                    $('#s_data').datagrid('load');
                    return
                } else{
                    $.messager.alert('响应信息',data.msg,'error');
                }
            }
        });
    };

	$(function(){

        $('#addHotCity').dialog({
            title:'新增热门城市',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    var params = {};
                    params.cityId = $("#city").val();
                    params.provinceId = $("#province").val();
                    if(params.cityId == '' || params.provinceId == '')
                    {
                        $.messager.alert("提示",'请填写完整信息',"error");
                        return;
                    }
                    $.messager.progress();
                    $.ajax({
                        url: '${rc.contextPath}/indexNavigation/saveHotCityList',
                        type: 'post',
                        dataType: 'json',
                        data: params,
                        success: function(data){
                            $.messager.progress('close');
                            if(data.status == 1){
                                $('#s_data').datagrid('load');
                                $('#addHotCity').dialog('close');
                            }else{
                                $('#s_data').datagrid('load');
                                $.messager.alert("提示",data.msg,"info");
                            }
                        },
                        error: function(xhr){
                            $.messager.progress('close');
                            $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                        }
                    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#addHotCity').dialog('close');
                }
            }]
        });

        $('#province').change(function(){
            $child = $('#city');
            var pid = $('#province').val();
            var selected_id = 0;
//            $('#district').empty();
            $.ajax({
                url:"${rc.contextPath}/order/getAllCity",
                type:'post',
                data: {id : pid},
                dataType: 'json',
                success:function(data){
                    var options = '';
                    $.each(data,function(i){
                        if(data[i].id == selected_id){
                            options += '<option value="'+this.cityId+'" selected="selected">'+this.name+'</option>';
                        }else{
                            options += '<option value="'+this.cityId+'" >'+this.name+'</option>';
                        }
                    });
                    $child.empty().append(options);
                }
            });
        });

        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/indexNavigation/ajaxHotCityList',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center'},
                {field:'province',    title:'省份', width:70, align:'center'},
                {field:'city',    title:'城市', width:70, align:'center'},
                {field:'sequence',     title:'排序值',  width:30, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:80, align:'center',
                    formatter : function(value, row, index) {
                        if (row.editing) {
                            var a = '<a href="javascript:void(0);" onclick="saverow(' + index + ')">保存</a> | ';
                            var b = '<a href="javascript:void(0);" onclick="cancelrow(' + index + ')">取消</a>';
                            return a + b;
                        }else{
                            var a = '<a href="javascript:void(0);" onclick="deleteIt(' + row.id + ')">删除</a>';
                            var b = ' | <a href="javascript:void(0);" onclick="editSequence(' + index + ')">改排序</a>';
                            return a + b;
                        }
                    }
                } ] ],
            toolbar : [ {
                id : '_add',
                text : '新增',
                iconCls : 'icon-add',
                handler : function() {
                    $('#addHotCity').dialog('open');
                }
            } ],
            onBeforeEdit:function(index,row){
                row.editing = true;
                updateActions();
            },
            onAfterEdit:function(index,row){
                row.editing = false;
                updateActions();
                updateActivity(row);
            },
            onCancelEdit:function(index,row){
                row.editing = false;
                updateActions();
            },
            pagination : true
        });
	});
</script>

</body>
</html>