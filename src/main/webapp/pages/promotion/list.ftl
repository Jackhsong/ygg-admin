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
            <div data-options="region:'north',title:'新精品特卖',split:true" style="height: 100px;">
                <br/>
                <div style="height: 60px;padding: 10px">
                    <span>活动名称：</span>
                    <span><input id="searchTitle" name="searchTitle"/></span>
                    <span>是否可用：</span>
				<span>
					<select id="searchIsAvailable" name="searchIsAvailable">
                        <option value="-1">全部</option>
                        <option value="1">可用</option>
                        <option value="0">停用</option>
                    </select>
				</span>
				<span>
					<a id="searchBtn" onclick="searchActivity()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
				</span>
                </div>
            </div>
            <div data-options="region:'center'" >
                <!--数据表格-->
                <table id="s_data" style=""></table>

                <!-- 新增 begin -->
                <div id="editDiv" class="easyui-dialog" style="width:550px;height:230px;padding:15px 20px;">
                    <form id="editDiv_form" method="post">
                        <input id="editId" type="hidden" name="editId" value="0" >
                        <p>
                            <span>标题：</span>
                            <span><input type="text" name="title" id="title" value="" maxlength="32" style="width: 300px;"/></span>
                            <font color="red">*</font>
                        </p>
                        <p>
                            <span>图片：</span>
                            <span><input type="text" name="image" id="image" value="" maxlength="100" style="width: 300px;"/></span>
                            <a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><font color="red">*&nbsp;750*830</font>
                        </p>
                        <p>
                            <span>可用状态：</span>
                            <span><input type="radio" name="isAvailable" id="isAvailable1" checked="checked" value="1"/>可用&nbsp;&nbsp;</span>
                            <span><input type="radio" name="isAvailable" id="isAvailable0" value="0"/>停用</span>
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
            <!-- <input type="hidden" name="needWidth" id="needWidth" value="640">
            <input type="hidden" name="needHeight" id="needHeight" value="640"> -->
            <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
            <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
        </form>
        <br><br>
    </div>
    <script type="application/javascript">
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

<script type="application/javascript">

    function searchActivity()
    {
        $('#s_data').datagrid('load', {
            title : $("#searchTitle").val(),
            isAvailable : $("#searchIsAvailable").val()
        });
    }

    function editIt(index){
        var arr=$("#s_data").datagrid("getData");
        $("input[name='editId']").val(arr.rows[index].id);
        if(arr.rows[index].isAvailable == 1)
        {
            $("#isAvailable1").prop("checked", "checked");//prop
        }
        else
        {
            $("#isAvailable0").prop("checked", "checked");//prop
        }
        $("input[name='image']").val(arr.rows[index].image);
        $("input[name='title']").val(arr.rows[index].title);

        $('#editDiv').dialog('open');
    }

    $(function(){

        $('#editDiv').dialog({
            title:'新情景专场',
            collapsible: true,
            closed: true,
            modal: true,
            buttons: [
                {
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function(){
                        var params = {};
                        params.editId = $("input[name='editId']").val();
                        params.isAvailable = $("input[name='isAvailable']:checked").val();
                        params.image = $("input[name='image']").val();
                        params.title = $("input[name='title']").val();
                        if(params.image == '' || params.title == '')
                        {
                            $.messager.alert("error","请填写完整信息","error");
                        }
                        else
                        {
                            $.messager.progress();
                            $.ajax({
                                url: '${rc.contextPath}/promotion/saveOrUpdatePromotion',
                                type: 'post',
                                dataType: 'json',
                                data: params,
                                success: function(data){
                                    $.messager.progress('close');
                                    if(data.status == 1){
                                        $('#s_data').datagrid('load');
                                        $('#editDiv').dialog('close');
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

        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/promotion/jsonPromotionInfo',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center'},
                {field:'title',    title:'名称', width:50, align:'center'},
                {field:'imageURL',    title:'图片', width:40, align:'center'},
                {field:'availableDesc',    title:'可用状态', width:30, align:'center'},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javascript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                        var b = '<a target="_blank" href="${rc.contextPath}/promotion/floorProduct/' + row.id + '">管理楼层商品</a> | ';
                        var f = '<a target="_blank" href="${rc.contextPath}/promotion/moreProduct/' + row.id + '">管理更多商品</a> | ';
                        var e = '<a target="_blank" href="'+row.url+'">预览</a>';
                        return a + b + f+ e;

                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增活动',
                iconCls:'icon-add',
                handler:function(){
                    $("input[name='editId']").val("0");
                    $("#isAvailable1").prop("checked", "checked");//prop
                    $("input[name='image']").val("");
                    $("input[name='title']").val("");
                    $('#editDiv').dialog('open');
                }
            }],
            pagination:true
        });
    });
</script>

</body>
</html>