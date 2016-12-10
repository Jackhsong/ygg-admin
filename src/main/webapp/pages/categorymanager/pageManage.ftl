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
        <div data-options="region:'north',title:'品类馆页面管理',split:true" style="height: 120px;">
            <br/>
            <div style="height: 60px;padding: 10px">
                <span>品类馆ID: ${pageId} </span>&nbsp;&nbsp;&nbsp;
                <span>品类馆名称: ${pageName} </span>&nbsp;&nbsp;&nbsp;
            </div>
        </div>

        <div data-options="region:'center'" >
            <!--数据表格-->
            <table id="s_data" style=""></table>
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

    function cleanEditDiv(){
        $('input[name="type"]').each(function(){
            $(this).attr("disabled",false);
        });
        $("#editDiv_Form_id").val('');
        $("#editDiv_Form input[type='text']").each(function(){
            $(this).val('');
        });

        $("#editDiv_Form_type1").prop("checked", true);
        $("#editDiv_Form_type2").prop("checked", false);
        $("#editDiv_Form_type3").prop("checked", false);
        $("#editDiv_Form_type4").prop("checked", false);

        $("#editDiv_Form_isDisplay1").prop("checked", true);
        $("#editDiv_Form_isDisplay0").prop("checked", false);
    }

    function editIt(index){
        cleanEditDiv();
        var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        var name = arr.rows[index].name;
        var remark = arr.rows[index].remark;
        var isDisplay = arr.rows[index].isDisplay;
        var type = arr.rows[index].type;
        $('#editDiv_Form_id').val(id);
        $('#editDiv_Form_name').val(name);
        $('#editDiv_Form_remark').val(remark);
        if(isDisplay == 1)
        {
            $("#editDiv_Form_isDisplay1").prop("checked", true);
            $("#editDiv_Form_isDisplay0").prop("checked", false);
        }
        else
        {
            $("#editDiv_Form_isDisplay1").prop("checked", false);
            $("#editDiv_Form_isDisplay0").prop("checked", true);
        }

        // type
        $('input[name="type"]').each(function(){
            $(this).prop("checked", false);
        });

        var id = 'editDiv_Form_type' + type;
        $("#" + id).prop("checked", true);

        $('input[name="type"]').each(function(){
            $(this).attr("disabled",true);
        });

        $('#editDiv').dialog('open');
    }


    function displayIt(index,isDisplay){
    	var arr=$("#s_data").datagrid("getData");
        var id = arr.rows[index].id;
        //var name = arr.rows[index].name;
        var pageId = $("#editDiv_Form_page_id").val();
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
                    url: '${rc.contextPath}/cateGoryManager/updatePageModel',
                    type: 'post',
                    dataType: 'json',
                    data: {'id':id,'isDisplay':isDisplay},
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

    <!--编辑排序相关 begin-->
    function editrow(index){
        $('#s_data').datagrid('beginEdit', index);
    };

    function updateActions(){
        var rowcount = $('#s_data').datagrid('getRows').length;
        for(var i=0; i<rowcount; i++){
            $('#s_data').datagrid('updateRow',{
                index:i,
                row:{}
            });
        }
    }

    function saverow(index){
        $('#s_data').datagrid('endEdit', index);
    };

    function cancelrow(index){
        $('#s_data').datagrid('cancelEdit', index);
    };

    function updateActivity(row){
        var pageId = $("#editDiv_Form_page_id").val();
        $.ajax({
            url: '${rc.contextPath}/page/updatePageModel',
            type:"POST",
            data: {id:row.id,pageId:pageId,sequence:row.sequence,name:row.name},
            success: function(data) {
                if(data.status == 1){
                    $('#s_data').datagrid('load',{
                        pageId:${pageId}
                    });
                }
                else
                {
                    $.messager.alert("提示",data.msg,"error");
                }
            }
        });
    };
    <!--编辑排序相关 end-->

    $(function(){

        $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/cateGoryManager/ajaxPageModel',
            queryParams: {
                pageId: ${pageId}
            },
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:20, align:'center'},
                {field:'typeStr',    title:'板块类型', width:50, align:'center'},
                {field:'displayStatus',    title:'展现状态', width:30, align:'center'},
                {field:'hidden',  title:'操作', width:50,align:'center',
                    formatter:function(value,row,index){

                        if (row.editing){
                            var a = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> | ';
                            var b = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                            return a + b;
                        }else{
                        	if(row.type==1){
                        		var e = '<a target="_blank" href="${rc.contextPath}/categoryBanner/listBanner/' + row.id + '">管理板块</a> | ';
                        	}else if(row.type==2){
                        		var e = '<a target="_blank" href="${rc.contextPath}/categoryClassification/listClassification/' + row.id + '/'+${pageId}+'">管理板块</a> | ';
                        	}else if(row.type==3){
                        		var e = '<a target="_blank" href="${rc.contextPath}/categoryRegion/listCategoryRegion/' + row.id + '/'+${pageId}+'">管理板块</a> | ';
                        	}else if(row.type==4){
                        		var e = '<a target="_blank" href="${rc.contextPath}/categoryGGrecommend/listCategoryGGrecommend/' + row.id + '/'+${pageId}+'">管理板块</a> | ';
                        	}else if(row.type==5){
                        		var e = '<a target="_blank" href="${rc.contextPath}/categoryRecommend/listCategoryRecommend/' + row.id + '/'+${pageId}+'">管理板块</a> | ';
                        	}else if(row.type==6){
                        		var e = '<a target="_blank" href="${rc.contextPath}/categorySale/listCategorySale/' + row.id + '/'+${pageId}+'">管理板块</a> | ';
                        	}
                            if(row.isDisplay == 1){
                                d = '<a href="javaScript:;" onclick="displayIt(' + index + ',' + 0 + ')">不展现</a>';
                            }else if(row.isDisplay == 0) {
                                d = '<a href="javaScript:;" onclick="displayIt(' + index + ',' + 1 + ')">展现 </a>';
                            }
                            return e + d;
                        }
                        
                        
                        
                    }
                }
            ]],
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
            pagination:true
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