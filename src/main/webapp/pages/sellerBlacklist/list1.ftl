<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理-商家邮费黑名单</title>
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
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<div data-options="region:'center',title:'商家邮费黑名单'" style="padding:5px;">
    <!--数据表格-->
    <table id="s_data" style=""></table>
    
	<div id="addDataDiv" class="easyui-dialog" style="width:650px;height:350px;padding:20px 20px;">
       <form id="addDataForm" method="post">
       		<input type="hidden" value="${type}"  id="type" />
       		<input type="hidden" value="0"  id="id" />
	        <table cellpadding="5">
	            <tr>
	                <td>商家ID:</td>
	                <td><input id="addDataForm_sellerId"  style="width:300px"  maxlength="8" autocomplete="off"></input></td>
	            </tr>
                <tr>
                    <td>邮费（邮费）:</td>
                    <td><input id="addDataForm_freightMoney"  style="width:300px" type="number"  maxlength="8" autocomplete="off"></input></td>
                </tr>
                <tr>
                    <td>包邮门槛（可选）:</td>
                    <td><input id="addDataForm_thresholdPrice"  style="width:300px"  ></input></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <p>
                            <span>凑单跳转类型：</span>
                            <span><input type="radio" style="display: none" name="displayType" id="displayType1" value="1"/><#-- 单品&nbsp;&nbsp;--></span>
                            <span><input type="radio" name="displayType" id="displayType2" value="2"/>组合&nbsp;&nbsp;</span>
                            <span><input type="radio" name="displayType" id="displayType3" value="3"/>网页自定义活动&nbsp;&nbsp;</span>
                            <span><input type="radio" name="displayType" id="displayType4" value="4"/>原生自定义活动&nbsp;&nbsp;</span>
                        </p>
                        <p id="oneProduct" style="display: none">
                            <span>单品ID：</span>
                            <span><input type="text" name="oneProductId" id="oneProductId" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="10" style="width: 200px;"/></span>
                            <span style="color: red" id="oneProductDesc"></span>
                        </p>
                        <p id="oneActivitiesCommon" style="display: none">
                            <span>组合ID：</span>
                            <span><input type="text" name="oneActivitiesCommonId" id="oneActivitiesCommonId" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="10" style="width: 200px;"/></span>
                            <span style="color: red" id="oneActivitiesCommonDesc"></span>
                        </p>
                        <p id="oneActivitiesCustom"  style="display: none">
                            <span>自定义活动：</span>
                            <span><input type="text" name="oneActivitiesCustomId" id="oneActivitiesCustomId" style="width: 200px;"/>
                        </p>
                        <p id="onePage"  style="display: none">
                            <span>原生自定义活动：</span>
                            <span><input type="text" name="onePageId" id="onePageId" style="width: 200px;"/></span>
                        </p>
                    </td>
                </tr>
	        </table>
   		</form>
   	</div> 
</div>

<script>

	function deleteItem(index){
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		$.messager.confirm('删除','确定删除吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/sellerBlacklist/delete',
		            type: 'post',
		            dataType: 'json',
		            data: {'id':id},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
                            $('#s_data').datagrid('load');
//		                	$.messager.alert("提示",'删除成功',"info");
		                }else{
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

    function initForm() {
        $("#id").val("");
        $("#addDataForm_sellerId").val("");
        $("#addDataForm_freightMoney").val("");
        $("#addDataForm_thresholdPrice").val("");
        $("#addDataForm_sellerId").removeAttr("readonly");
        $("#oneProduct").hide();
        $("#oneActivitiesCommon").hide();
        $("#onePage").hide();
        $("#oneActivitiesCustom").hide();
        $.ajax({
            url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode',
            type:'POST',
            async:false,
            success:function(data) {
                $("#oneActivitiesCustomId").combobox({
                    data:data,
                    editable : false,
                    valueField:'code',
                    textField:'text'
                });
            }
        });
        $.ajax({
            url:'${rc.contextPath}/page/ajaxAppCustomPage',
            type:'POST',
            async:false,
            success:function(data) {
                $("#onePageId").combobox({
                    data:data,
                    editable : false,
                    valueField:'code',
                    textField:'text'
                });
            }
        });
    }

    function editItem(index){
        initForm();
        var arr=$("#s_data").datagrid("getData");
        $("#id").val(arr.rows[index].id);
        $("#addDataForm_sellerId").val(arr.rows[index].sellerId);
        $("#addDataForm_freightMoney").val(arr.rows[index].freightMoney);
        $("#addDataForm_thresholdPrice").val(arr.rows[index].thresholdPrice);
        var displayType = arr.rows[index].displayType;
        var displayId = arr.rows[index].displayId;
        if (displayType == 1)
        {
            $("#displayType1").prop('checked',true);
            $('#oneProductId').val(displayId);
            $('#oneProduct').show();
            $('#oneActivitiesCommon').hide();
            $('#oneActivitiesCustom').hide();
            $('#onePage').hide();
        }
        else if(displayType == 2)
        {
            $("#displayType2").prop('checked',true);
            $('#oneProduct').hide();
            $('#oneActivitiesCommon').show();
            $('#oneActivitiesCustom').hide();
            $('#onePage').hide();
            $('#oneActivitiesCommonId').val(displayId);
        }
        else if(displayType == 3)
        {
            $("#displayType3").prop('checked',true);
            $('#oneProduct').hide();
            $('#oneActivitiesCommon').hide();
            $('#oneActivitiesCustom').show();
            $('#onePage').hide();
            $('#oneActivitiesCustomId').combobox('setValue', displayId);
        }
        else if(displayType == 4)
        {
            $("#displayType4").prop('checked',true);
            $('#oneProduct').hide();
            $('#oneActivitiesCommon').hide();
            $('#oneActivitiesCustom').hide();
            $('#onePage').show();
            $('#onePageId').combobox('setValue', displayId);
        }

        $("#addDataForm_sellerId").attr("readonly","readonly");
        $("#addDataDiv").dialog("open");
    }

	$(function(){

        // 填写商品ID后
        $('#oneProductId').blur(function() {
            if($('#oneProductId').val().length < 1)
                return;
            $.ajax({
                url: '${rc.contextPath}/product/findProductInfoById',
                type: 'POST',
                data: {id:$('#oneProductId').val()},
                success: function(data) {
                    if(data.status == 1) {
                        $('#oneProductDesc').text(data.msg);
                    }else{
                        $.messager.alert("提示",data.msg,"info");
                    }
                }
            });
        });

        // 填写组合ID后
        $('#oneActivitiesCommonId').blur(function() {
            if($('#oneActivitiesCommonId').val().length < 1)
                return;
            $.ajax({
                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
                type: 'POST',
                data: {id:$('#oneActivitiesCommonId').val()},
                success: function(data) {
                    if(data.status == 1) {
                        $('#oneActivitiesCommonDesc').text(data.name + "-" + data.remark);
                    }else{
                        $.messager.alert("提示",data.msg,"info");
                    }
                }
            });
        });

        // 选择类型展示不同的信息
        $('input[name=displayType]').change(function() {
            var type = $('input[name=displayType]:checked').val();
            if(type == 1) {
                $('#oneProduct').show();
                $('#oneActivitiesCommon').hide();
                $('#oneActivitiesCustom').hide();
                $('#onePage').hide();
            }
            else if(type == 2) {
                $('#oneProduct').hide();
                $('#oneActivitiesCommon').show();
                $('#oneActivitiesCustom').hide();
                $('#onePage').hide();
            }
            else if(type == 3) {
                $('#oneProduct').hide();
                $('#oneActivitiesCommon').hide();
                $('#oneActivitiesCustom').show();
                $('#onePage').hide();
            } else if(type == 4) {
                $('#oneProduct').hide();
                $('#oneActivitiesCommon').hide();
                $('#oneActivitiesCustom').hide();
                $('#onePage').show();
            }
        });
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sellerBlacklist/ajaxData',
            queryParams:{
                type:'1'
            },
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'sellerId',    title:'商家ID', width:30, align:'center'},
                {field:'sellerName',    title:'商家名称', width:50, align:'center'},
                {field:'freightMoney',    title:'收取邮费', width:30, align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javascript:void(0);" onclick="editItem(' + index + ')">编辑</a>';
                    	var b = ' | <a href="javaScript:;" onclick="deleteItem(' + index + ')">删除</a>';
                        return a + b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                    initForm();
                    $("#addDataDiv").dialog("open");
                }
            }],
            pagination:true
        });
        
        
        $('#addDataDiv').dialog({
            title:'新增',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    var params = {};
                    params.id = $("#id").val();
                    params.type = $("#type").val();
                    params.sellerId = $.trim($("#addDataForm_sellerId").val());
                    params.freightMoney = $.trim($("#addDataForm_freightMoney").val());
                    params.thresholdPrice = $.trim($("#addDataForm_thresholdPrice").val());
                    params.displayType = $("input[name='displayType']:checked").val();
                    params.onePageId = $("#onePageId").combobox('getValue');
                    params.oneActivitiesCustomId = $("#oneActivitiesCustomId").combobox('getValue');
                    params.oneActivitiesCommonId = $("#oneActivitiesCommonId").val();
                    params.oneProductId = $.trim($("#oneProductId").val());
                    if(params.sellerId == ''){
                        $.messager.alert('警告','商家不能为空', 'warning');
                        return false;
                    }
                    if(params.freightMoney == ''){
                        $.messager.alert('警告','邮费不能为空', 'warning');
                        return false;
                    }
                    $.ajax({
                        url: '${rc.contextPath}/sellerBlacklist/save',
                        type: 'post',
                        dataType: 'json',
                        data: params,
                        success: function(data){
                            $.messager.progress('close');
                            if(data.status == 1){
                                $('#s_data').datagrid('load');
//                                $.messager.alert("提示",'保存成功',"info");
                                $('#addDataDiv').dialog('close');
                            }else{
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
                    $('#addDataDiv').dialog('close');
                }
            }]
        });       
	});
</script>

</body>
</html>