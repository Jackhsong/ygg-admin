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
<style type="text/css">
.searchName{
	padding-right:10px;
	/* text-align:right; */
	text-align:justify;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
.inputStyle{
	width:250px;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">

	<div id="cc" class="easyui-layout" data-options="fit:true" >
	
		<div data-options="region:'north',title:'条件筛选-抽奖管理',split:true" style="height: 20px;">
			
		</div>
		
		<div data-options="region:'center'" >
	    	<!--数据表格-->
	    	<table id="s_data" style=""></table>
    	</div>
    	
		<!-- dialog begin -->
			<div id="add_div" class="easyui-dialog" style="width:700px;height:400px;padding:5px 5px;">
				<input id="editId" type="hidden" name="id" value="">
					<table cellpadding="5">
					<tr>
		                <td class="searchName">活动名称：</td>
		                <td><input id="name" name="name" value=""></td>
		            </tr>
		            <tr>
		                <td class="searchName">开始时间：</td>
		                <td>
		                    <input value="" id="startTime" name="startTime"
		                           onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
		                </td>
		            </tr>
		            <tr>
		                <td class="searchName">结束时间：</td>
		                <td>
		                    <input value="" id="endTime" name="endTime"
		                           onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
		                </td>
		            </tr>
		            <tr>
		                <td  class="searchName">活动类型：</td>
		                <td>
		                	<select id="type">
		                		<option value="1">普通活动</option>
		                	</select>
		                </td>
		            </tr>
		            <tr>
		                <td  class="searchName">会员每日限制次数：</td>
		                <td><input id="limitNum" name="limitNum" value=""></td>
		            </tr>
		            <tr>
		                <td class="searchName">分享增加次数：</td>
		                <td><input id="shareNum" name="shareNum" value=""></td>
		            </tr>
		            <tr>
		                <td class="searchName">备注信息：</td>
		                <td><textarea name="remark" id="remark" style="height: 60px;width: 300px"></textarea></td>
		            </tr>
					</table>
				</form>
			</div>
			<!-- dialog end -->
		
		
	</div>
	
</div>

<script type="text/javascript">

function deleteIt(id){
    $.messager.confirm("提示信息","确定删除么？",function(re){
        if(re){
            $.messager.progress();
            $.post("${rc.contextPath}/lotteryActivity/delete",{id:id},function(data){
                $.messager.progress('close');
                if(data.status == 1){
                    $.messager.alert('响应信息',"删除成功...",'info',function(){
                        $('#dg').datagrid('reload');
                        return
                    });
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return
                    });
                }
            })
        }
    })
}

function editIt(index){
    var arr=$("#dg").datagrid("getData");
    $("#editId").val(arr.rows[index].id);
    $("#lotteryName").val(arr.rows[index].name);
    $("#startTime").val(arr.rows[index].startTime);
    $("#endTime").val(arr.rows[index].startTime);
    $("#limitNum").val(arr.rows[index].limitNum);
    $("#shareNum").val(arr.rows[index].shareNum);
    $("#remark").val(arr.rows[index].remark);
    $("#add_div").dialog('open');
}

$(function(){

    $('#s_data').datagrid({
        nowrap: false,
        height: 700,
        striped: true,
        collapsible: true,
        idField: 'id',
        url: '${rc.contextPath}/lotteryActivity/jsonDataForIndex',
        fitColumns: true,
        singleSelect: true,
        pageSize: 20,
        pageList: [20, 30, 40],
        pagination: true,
        columns: [
            [
                {field: 'id', title: 'ID', width: 10, align: 'center'},
                {field: 'name', title: '活动名称', width: 80, align: 'center'},
                {field: 'startTime', title: '开始时间', width: 80, align: 'center'},
                {field: 'endTime', title: '结束时间', width: 80, align: 'center'},
                {field: 'expend', title: '抽奖消耗', width: 80, align: 'center'},
                {field: 'limitNum', title: '次数限制', width: 80, align: 'center'},
                {field: 'manager', title: '最后操作者', width: 80, align: 'center'},
                {field: 'marks', title: '备注信息', width: 80, align: 'center'},
                {field: 'status', title: '状态', width: 80, align: 'center'},
                {field: 'hidden', title: '操作', width: 80, align: 'center',
                    formatter: function (value, row, index) {
                        var lableStr = '';
                        lableStr += '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a>'
                            + "&nbsp;|&nbsp;" + '<a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>'
                            + "&nbsp;|&nbsp;" + '<a href="/lotteryActivity/manLotAty/'+ row.id +'/">管理奖品</a>';
                        return lableStr;
                    }
                }
            ]
        ],
        toolbar: [
            {
                id: '_add',
                iconCls:'icon-add',
                text: '新增抽奖活动',
                handler: function () {
                    $("#editId").val('');
                    $("#add_div").find("textarea").val('');
                    $("#add_div").find("input:text").val('').end().dialog('open');

                }
            }
        ]
    });

    $('#add_div').dialog({
        title:'抽奖活动信息',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存信息',
            iconCls:'icon-ok',
            handler:function(){
            	var params = {
            			editId:$("#editId").val();
            			name:$("#name").val();
            			startTime:$("#startTime").val();
            			endTime:$("#endTime").val();
            			type:$("#type").val();
		            	limitNum:$("#limitNum").val();
		            	shareNum:$("#shareNum").val();
            			remark:$("#remark").val();
            	};
            	if(params.name == '' || params.startTime == '' || params.endTime == ''){
            		$.messager.alert("提示","名称、开始时间和结束时间必填","warning");
            	}
            	else if(!/^[1-9]+\d*$/.test(params.limitNum) || !/^[1-9]+\d*$/.test(params.shareNum)){
            		$.messager.alert("提示","次数请填写正整数","warning");
            	}
            	else{
            		$.ajax({
    					url: '${rc.contextPath}/lotteryActivity/saveOrUpdate',
    					type: 'post',
    					dataType: 'json',
    					data: params,
    					success: function(data){
    						$.messager.progress('close');
    						if(data.status == 1){
    							$.messager.alert("提示","创建成功","info",function(){
    								$('#s_data').datagrid("load");
    								$('#add_div').dialog('close');
    							});
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
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#add_div').dialog('close');
            }
        }]
    })
})
</script>

</body>
</html>