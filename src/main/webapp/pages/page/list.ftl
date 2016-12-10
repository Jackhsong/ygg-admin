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
        <div data-options="region:'north',title:'自定义页面',split:true" style="height: 120px;">
            <br/>
            <div style="height: 60px;padding: 10px">
                <span>自定义活动名称：</span>
                <span><input id="searchName" name="searchName"/>&nbsp;&nbsp;&nbsp;</span>
                <span>是否可用：</span>
                <span><select id="isAvailable">
                    <option value="-1">全部</option>
                    <option value="1">可用</option>
                    <option value="0">停用</option>
                </select> </span>
				<span>
					<a id="searchBtn" onclick="searchActivity()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
				</span>
            </div>
        </div>

        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" style=""></table>

            <!-- 新增 begin -->
            <div id="editDiv" class="easyui-dialog" style="width:550px;height:250px;padding:15px 20px;">
                <form id="editDiv_Form" method="post">
                    <input id="editDiv_Form_id" type="hidden" name="editId" value="" >
                    <p>
                        <span>页面名称：</span>
                        <span><input type="text" name="title" id="editDiv_Form_title" value="" maxlength="32" style="width: 300px;"/></span>
                        <font color="red">*</font>
                    </p>
                    <p>
                        <span>&nbsp;&nbsp;备注：</span>
                        <span><input type="text" name="remark" id="editDiv_Form_remark" value="" maxlength="32" style="width: 300px;"/></span>
                        <font color="red">*</font>
                    </p>
                    <p>
                        <span>是否可用:</span>
                        <input type="radio" id="editDiv_Form_isAvailable1" name="isAvailable" value="1">可用&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="editDiv_Form_isAvailable0" name="isAvailable" value="0"> 不可用<br/><br/>
                    </p>
                </form>
            </div>
            <!-- 新增 end -->

        </div>

    </div>
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <!-- <input type="hidden" name="needWidth" id="needWidth" value="750">
        <input type="hidden" name="needHeight" id="needHeight" value="0"> -->
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
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

    function searchActivity(){
        $('#s_data').datagrid('load',{
            name:$("#searchName").val(),
            isAvailable:$("#isAvailable").val()
        });
    }

    function cleanEditDiv(){
        $("#editDiv_Form_id").val('');
        $("#editDiv_Form input[type='text']").each(function(){
            $(this).val('');
        });
        $("#editDiv_Form_isAvailable1").prop("checked", true);
        $("#editDiv_Form_isAvailable0").prop("checked", false);
    }

    function editIt(index){
        cleanEditDiv();
        var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var name = arr.rows[index].name;
        var remark = arr.rows[index].remark;
        var isAvailable = arr.rows[index].isAvailable;
        $('#editDiv_Form_id').val(id);
        $('#editDiv_Form_title').val(name);
        $('#editDiv_Form_remark').val(remark);
        if(isAvailable == 1)
        {
            $("#editDiv_Form_isAvailable1").prop("checked", true);
            $("#editDiv_Form_isAvailable0").prop("checked", false);
        }
        else
        {
            $("#editDiv_Form_isAvailable0").prop("checked", true);
            $("#editDiv_Form_isAvailable1").prop("checked", false);
        }
        $('#editDiv').dialog('open');
    }


    function availableIt(id,isAvailable){
        var tips = '';
        if(isAvailable == 1){
            tips = '确定启用吗？';
        }else{
            tips = '确定停用吗？'
        }
        $.messager.confirm("提示信息",tips,function(re){
            if(re)
            {
                $.messager.progress();
                $.ajax({
                    url: '${rc.contextPath}/page/updatePage',
                    type: 'post',
                    dataType: 'json',
                    data: {'id':id, 'isAvailable':isAvailable},
                    success: function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
                            $('#editDiv').dialog('close');
                            $('#s_data').datagrid('reload');
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

    $(function(){

        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/page/ajaxPageData',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center',checkbox:true},
                {field:'index',    title:'ID', width:20, align:'center'},
                {field:'name',    title:'名称', width:50, align:'center'},
                {field:'remark',    title:'描述', width:60, align:'center'},
                {field:'availableDesc',    title:'可用状态', width:30, align:'center'},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javascript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                        var b = '<a target="_blank" href="${rc.contextPath}/page/pageManage/' + row.id + '">管理板块</a> | ';
                        var c = '';
                        var d = '';
                        if(row.isAvailable == 1){
                            d = '<a href="javaScript:;" onclick="availableIt(' + row.id + ',' + 0 + ')">停用</a> | ';
                        }else if(row.isAvailable == 0) {
                            d = '<a href="javaScript:;" onclick="availableIt(' + row.id + ',' + 1 + ')">启用 </a> | ';
                        }
                        var e = '<a href="${rc.contextPath}/activityManjian/manageProduct/3/'+row.id+'" target="_blank">管理满减活动商品</a>';
                        return a + b + c + d + e;

                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增自定义活动页面',
                iconCls:'icon-add',
                handler:function(){
                    cleanEditDiv();
                    $('#editDiv').dialog('open');
                }
            }],
            pagination:true
        });


        $('#editDiv').dialog({
            title:'自定义页面活动',
            collapsible: true,
            closed: true,
            modal: true,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function(){
                        var params = {};
                        params.id = $("#editDiv_Form_id").val();
                        params.name = $("#editDiv_Form_title").val();
                        params.remark = $("#editDiv_Form_remark").val();
                        params.isAvailable = $("input[name='isAvailable']:checked").val();
                        $.messager.progress();
                        $.ajax({
                            url: '${rc.contextPath}/page/updatePage',
                            type: 'post',
                            dataType: 'json',
                            data: params,
                            success: function(data){
                                $.messager.progress('close');
                                if(data.status == 1){
                                    $('#editDiv').dialog('close');
                                    $('#s_data').datagrid('reload');
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
                },
                {
                    text:'取消',
                    align:'left',
                    iconCls:'icon-cancel',
                    handler:function(){
                        $('#editDiv').dialog('close');
                    }
                }]
        });
    });

    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        });
    });

    var inputId;
    function picDialogOpen($inputId) {
        inputId = $inputId;
        $("#picDia").dialog("open");
        $("#yun_div").css('display','none');
    }
    function picDialogClose() {
        $("#picDia").dialog("close");
    }
    function picUpload() {
        $('#picForm').form('submit',{
            url:"${rc.contextPath}/pic/fileUpLoad",
            success:function(data){
                var res = eval("("+data+")")
                if(res.status == 1){
                    $.messager.alert('响应信息',"上传成功...",'info',function(){
                        $("#picDia").dialog("close");
                        if(inputId) {
                            $("#"+inputId).val(res.url);
                            $("#picFile").val("");
                        }
                        return
                    });
                } else{
                    $.messager.alert('响应信息',res.msg,'error',function(){
                        return ;
                    });
                }
            }
        })
    }
</script>

</body>
</html>