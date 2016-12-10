<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>换吧网络后台-N元任选管理</title>
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

<div data-options="region:'center'" style="padding: 5px;">
    <div id="cc" class="easyui-layout" data-options="fit:true" >
        <div data-options="region:'north',title:'N元任选管理',split:true" style="height: 100px;">
            <div style="height: 40px;padding: 10px">
                <input type="checkbox" name="timeStatus" id="timeStatus1" value="1" <#if status?exists && (status?contains("1"))>checked</#if>/>即将开始
                <input type="checkbox" name="timeStatus" id="timeStatus2" value="2" <#if status?exists && (status?contains("2"))>checked</#if>/>进行中
                <input type="checkbox" name="timeStatus" id="timeStatus3" value="3" <#if status?exists && (status?contains("3"))>checked</#if>/>已结束
            </div>
        </div>

        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" ></table>

            <!-- 编辑 -->
            <div id="editAmDiv" class="easyui-dialog" style="width:500px;height:320px;padding:20px 20px;">
                <form id="editAmForm" method="post">
                    <input id="editAmForm_id" type="hidden" name="id" value="0" >
                    <p>
                        <span>活动名称：</span>
                        <span><input type="text" id="editAmForm_name" name="name" style="width:330px"  maxlength="20"></span>
                    </p>
                    <p>
                        <span>活动时间：</span>
						<span>
							<input type="text" id="editAmForm_startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'editAmForm_endTime\')}'})">
							-
							<input type="text" id="editAmForm_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'editAmForm_startTime\')}'})">
						</span>
                    </p>
                    <p>
                        <span>N元任选规则：</span>
                        <span>
                            <input id="editAmForm_price" name="price" style="width:80px" type="number">元
                            任选
                            <input id="editAmForm_count" name="count" style="width:80px" type="number">件
                        </span>
                    </p>
                    <p>
                        <span>关联类型：</span>
						<span>
							<input type="radio" name="type" id="editAmForm_type1" value="2"/>组合特卖&nbsp;&nbsp;
							<input type="radio" name="type" id="editAmForm_type2" value="3"/>自定义活动
						</span>
                    </p>
                    <p id="groupSaleSpan">
                        <span>组合ID：</span>
                        <span><input type="number" id="editAmForm_groupSale" name="groupSaleId" style="width:330px;" value=""/></span>
                        <span id="editAmForm_groupSaleName" style="color:red"></span>
                    </p>
                    <p id="customSaleSpan" style="display:none">
                        <span>自定义活动：</span>
                        <span><input type="text" id="editAmForm_customSale" name="customSaleId" style="width:300px;"/></span>
                    </p>

                </form>
            </div>
        </div>
    </div>
</div>


<script>
    function searchCustom(){
        var status = [];
        $("input[name='timeStatus']:checked").each(function(){
            status.push($(this).val());
        });
        $('#s_data').datagrid('load',{
            status:status.join(','),
            type:'${type}'
        });
    }


    function displayId(id,isAvailable){
        var tip = "";
        if(isAvailable == 0){
            tip = "确定停用吗？";
        }else{
            tip = "确定启用吗？";
        }
        $.messager.confirm("提示信息",tip,function(ra){
            if(ra){
                $.messager.progress();
                $.ajax({
                    url:'${rc.contextPath}/activityOptionalPart/updateDisplayStatus',
                    type:'post',
                    dataType:'json',
                    data:{'id':id,"code":isAvailable},
                    success:function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
                            $('#s_data').datagrid('clearSelections');
                            $('#s_data').datagrid("reload");
                            $('#s_data').datagrid('clearSelections');
                        }else{
                            $.messager.alert("提示",data.msg,"error");
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

    function editIt(index){
        clearEditAmForm();
        var arr=$("#s_data").datagrid("getData");
        var type = arr.rows[index].type;
        var typeId = arr.rows[index].typeId;
        $("#editAmForm_id").val(arr.rows[index].id);
        $("#editAmForm_count").val(arr.rows[index].count);
        $("#editAmForm_price").val(arr.rows[index].price);
        $("#editAmForm_name").val(arr.rows[index].name);
        $("#editAmForm_startTime").val(arr.rows[index].startTime);
        $("#editAmForm_endTime").val(arr.rows[index].endTime);
        if(type == 2){
            $("#editAmForm_type1").prop('checked',true);
            $("#editAmForm_groupSale").val(typeId);
            $("#groupSaleSpan").show();
            $("#customSaleSpan").hide();
        }else if(type == 3){
            $("#editAmForm_type2").prop('checked',true);
            var url = '${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id='+typeId;
            $("#editAmForm_customSale").combobox('reload',url);
            $("#groupSaleSpan").hide();
            $("#customSaleSpan").show();
        }
        $("#editAmDiv").dialog('open');
    }

    function clearEditAmForm(){
        $("#editAmForm_id").val('0');
        $("#editAmForm_count").val('');
        $("#editAmForm_price").val('');
        $("#editAmForm_name").val('');
        $("#editAmForm_startTime").val('');
        $("#editAmForm_endTime").val('');
        $("#editAmForm input[type='input']").each(function(){
            $(this).val('');
        });
        $("#editAmForm_customSale").combobox('clear');
    }
    $(function(){

        $('#editAmForm_type1').change(function(){
            if($(this).is(':checked')){
                $('#groupSaleSpan').show();
                $('#customSaleSpan').hide();
            }
        });

        $('#editAmForm_type2').change(function(){
            if($(this).is(':checked')){
                $('#groupSaleSpan').hide();
                $('#customSaleSpan').show();
            }
        });


        $("input[name='timeStatus']").each(function(){
            $(this).change(function(){
                searchCustom();
            });
        });
        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/activityOptionalPart/jsonActivityOptionalPartInfo',
            loadMsg:'正在装载数据...',
            queryParams:{
                type:'${type}',
                status:'${status}'
            },
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            columns:[[
                {field:'id',    title:'活动id', width:20,align:'center'},
                {field:'availableDesc',    title:'停用状态', width:20, align:'center'},
                {field:'timeStatus',    title:'活动状态', width:30, align:'center'},
                {field:'startTime',    title:'开始时间', width:40, align:'center'},
                {field:'endTime',    title:'结束时间', width:40, align:'center'},
                {field:'name',    title:'活动名称', width:60, align:'center'},
                {field:'role',    title:'N元任选规则', width:50, align:'center'},
                {field:'typeDesc',    title:'关联类型', width:50, align:'center'},
                {field:'typeId',    title:'关联ID', width:20, align:'center'},
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a>';
                        var b = '';
                        if(row.isAvailable == 0){
                            b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 1 + ')">启用</a>'
                        }else{
                            b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 0 + ')">停用</a>'
                        }
                        return a+b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                    clearEditAmForm();
                    $('#editAmDiv').dialog('open');
                }
            }],
            pagination:true
        });

        $('#editAmDiv').dialog({
            title:'编辑',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#editAmForm').form('submit',{
                        url:"${rc.contextPath}/activityOptionalPart/saveOrUpdate",
                        onSubmit:function(){
                            var name = $("#editAmForm_name").val();
                            var startTime = $("#editAmForm_startTime").val();
                            var endTime = $("#editAmForm_endTime").val();
                            var type = $("input[name='type']:checked").val();
                            var groupSaleId = $("#editAmForm_groupSale").val();
                            var customSaleId = $("#editAmForm_customSale").combobox('getValue');
                            var count = $("#editAmForm_count").val();
                            var price = $("#editAmForm_price").val();
                            if($.trim(name) == ''){
                                $.messager.alert("提示","请输入活动名称","error");
                                return false;
                            }else if($.trim(count) == ''){
                                $.messager.alert("提示","请输入数量","error");
                                return false;
                            }else if($.trim(price) == ''){
                                $.messager.alert("提示","请输入价格","error");
                                return false;
                            }else if($.trim(startTime) == ''){
                                $.messager.alert("提示","请输入活动开始时间","error");
                                return false;
                            }else if($.trim(endTime) == ''){
                                $.messager.alert("提示","请输入活动结束时间","error");
                                return false;
                            }else if(type == null || type == '' || type == undefined){
                                $.messager.alert("提示","请选择关联类型","error");
                                return false;
                            }else if(type == 2 && $.trim(groupSaleId) == ''){
                                $.messager.alert("提示","请输入组合ID","error");
                                return false;
                            }else if(type == 3 && (customSaleId == '' || customSaleId == null || customSaleId == undefined)){
                                $.messager.alert("提示","请选择自定义活动","error");
                                return false;
                            }
                            $.messager.progress();
                        },
                        success:function(data){
                            $.messager.progress('close');
                            var res = eval("("+data+")");
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info');
                                $('#s_data').datagrid('reload');
                                $('#s_data').datagrid('clearSelections');
                                $('#editAmDiv').dialog('close');
                            }else{
                                $.messager.alert('响应信息',res.msg,'error');
                            }
                        }
                    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editAmDiv').dialog('close');
                }
            }]
        });

        $("#editAmForm_customSale").combobox({
            url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode?type=2,4,6,12',
            valueField:'code',
            textField:'text'
        });

        $("#editAmForm_groupSale").change(function(){
            var id = $.trim($(this).val());
            if(id == ''){
                $("#editAmForm_groupSaleName").text('');
            }else{
                $.ajax({
                    url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
                    type: 'post',
                    dataType: 'json',
                    data: {'id':id},
                    success: function(data){
                        if(data.status == 1){
                            $("#editAmForm_groupSaleName").text(data.name + "-" + data.remark);
                        }else{
                            $.messager.alert("提示",data.msg,"info");
                        }
                    },
                    error: function(xhr){
                        $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                    }
                });
            }

        });
    });

</script>

</body>
</html>