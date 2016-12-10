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
        <div data-options="region:'north',title:'品类馆管理',split:true" style="height: 120px;">
            <br/>
            <div style="height: 60px;padding: 10px">
                <span>品类馆名称：</span>
                <span><input id="searchName" name="searchName"/>&nbsp;&nbsp;&nbsp;</span>
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
                        <span>品类馆名称:</span>
                        <span><input type="text" name="title" id="editDiv_Form_title" value="" maxlength="32" style="width: 300px;"/></span>
                        <font color="red">*</font>
                    </p>
                    <p>
						<span>所属 分类 :</span>
						<span>
							<span><input type="text" id="editCategoryForm_fid" name="categoryFirstId" style="width:300px"></span>
						</span>
						<span style="color:red">*</span>
					</p>
					<p>
                        <span>格格推荐标题栏展现状态：</span>
                        <input type="radio" id="editDiv_Form_isDisplay1" name="isDisplay" checked value="1">展现&nbsp;&nbsp;&nbsp;
                        <input type="radio" id="editDiv_Form_isDisplay0" name="isDisplay" value="0"> 不展现<br/><br/>
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
        });
    }

    function cleanEditDiv(){
        $("#editDiv_Form_id").val('');
        $("#editDiv_Form input[type='text']").each(function(){
            $(this).val('');
        });
        $("#editDiv_Form_isAvailable1").prop("checked", true);
        $("#editDiv_Form_isAvailable0").prop("checked", false);
        $("#editCategoryForm_fid").combobox('clear');
    }

    function editIt(index){
        cleanEditDiv();
        var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var name = arr.rows[index].name;
        var remark = arr.rows[index].remark;
        var isDisplay = arr.rows[index].isDisplay;
        $('#editDiv_Form_id').val(id);
        $('#editDiv_Form_title').val(name);
        $('#editDiv_Form_remark').val(remark);
        var fid = arr.rows[index].fId;
		var url = '${rc.contextPath}/category/jsonCategoryFirstCode?id='+fid;
		$('#editCategoryForm_fid').combobox('reload',url);
        if(isDisplay == 1)
        {
            $("#editDiv_Form_isDisplay1").prop("checked", true);
            $("#editDiv_Form_isDisplay0").prop("checked", false);
        }
        else
        {
            $("#editDiv_Form_isDisplay0").prop("checked", true);
            $("#editDiv_Form_isDisplay1").prop("checked", false);
        }
        $('#editDiv').dialog('open');
    }


    function displayIt(index,isDisplay){
    	var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var tips = '';
        if(isDisplay == 1){
            tips = '确定展示吗？';
        }else{
            tips = '确定隐藏吗？'
        }
        $.messager.confirm("提示信息",tips,function(re){
            if(re)
            {
                $.messager.progress();
                $.ajax({
                    url: '${rc.contextPath}/cateGoryManager/updatePage',
                    type: 'post',
                    dataType: 'json',
                    data: {'id':id,'isDisplay':isDisplay},
                    success: function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
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
    
    	$('#editCategoryForm_fid').combobox({   
		    url:'${rc.contextPath}/category/jsonCategoryFirstCode',   
		    valueField:'code',   
		    textField:'text',
		    editable:false
		});
		
        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/cateGoryManager/ajaxPageData',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center'},
                {field:'name',    title:'品类馆名称', width:50, align:'center'},
                {field:'fname', title:'所属分类', width:60, align:'center'},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javascript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                        var b = '<a target="_blank" href="${rc.contextPath}/cateGoryManager/pageManage/' + row.id + '">管理板块</a> | ';
                        if(row.isDisplay == 1){
                            d = '<a href="javaScript:;" onclick="displayIt(' + index + ',' + 0 + ')">不展现格格推荐标题栏</a>';
                        }else if(row.isDisplay == 0) {
                            d = '<a href="javaScript:;" onclick="displayIt(' + index + ',' + 1 + ')">展现格格推荐标题栏 </a>';
                        }
                        return a + b + d;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
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
                        params.isDisplay = $("input[name='isDisplay']:checked").val();
                        var fid = $("#editCategoryForm_fid").combobox('getValue');
                    	if(fid == '' || fid == undefined || fid == null){
                    		$.messager.alert("提示","请选择一级分类","error");
    						return false;
                    	}
						params.categoryFirstId = fid;                    
                        $.messager.progress();
                        $.ajax({
                            url: '${rc.contextPath}/cateGoryManager/updatePage',
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