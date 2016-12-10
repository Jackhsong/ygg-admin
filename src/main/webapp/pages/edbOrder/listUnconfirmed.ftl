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
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'未确认E店宝订单列表',split:true" style="height: 120px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td class="searchName">订单编号：</td>
						<td class="searchText">
							<input id="orderNumber" name="orderNumber" value="" />
						</td>
                        <td class="searchName">&nbsp;付款时间：</td>
                        <td class="searchText">
                            <input value="" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                            ~
                            <input value="" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
                        </td>
                        <td class="searchName">&nbsp;商家：</td>
                        <td class="searchText"><input id="sellerId" name="sellerId" value=""/></td>
                        <td class="searchText">
							冻结状态：
                            <select id="freezeStatus" name="freezeStatus" style="width: 170px;">
                                <option value="-1">全部</option>
                                <option value="1">已冻结</option>
                                <option value="0">未冻结</option>
                            </select>
                        </td>
						<td class="searchText" style="padding-top: 5px">
							<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		</div>
	</div>
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
	function searchOrder(){
		$('#s_data').datagrid('load', {
			number : $("#orderNumber").val(),
            sellerId : $("input[name='sellerId']").val(),
            freezeStatus : $("#freezeStatus").val(),
            startTime : $("input[name='startTime']").val(),
            endTime : $("input[name='endTime']").val()
        });
	}
	
	function send(id){
		$.messager.confirm('确认推送','确认推送至E店宝？',function(r){
		    if (r){
		    	$.messager.progress();
    			$.ajax({
					url: '${rc.contextPath}/edbOrder/confirm',
					type: 'post',
					dataType: 'json',
				    data: {'ids':id,'isPush':1},
					success: function(data){
						$.messager.progress('close');
						if(data.status == 1){
                            $('#s_data').datagrid('clearSelections');<!-- 取消所有的已选择项。 -->
                            if(data.dialog == 1){
                                $.messager.alert("提示",data.msg,"info");
                            }
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

    function unSend(id){
        $.messager.confirm('取消推送','确认取消推送至E店宝？',function(r){
            if (r){
                $.messager.progress();
                $.ajax({
                    url: '${rc.contextPath}/edbOrder/confirm',
                    type: 'post',
                    dataType: 'json',
                    data: {'ids':id,'isPush':0},
                    success: function(data){
                        $.messager.progress('close');
                        if(data.status == 1){
                            $('#s_data').datagrid('clearSelections');<!-- 取消所有的已选择项。 -->
                            if(data.dialog == 1){
                                $.messager.alert("提示",data.msg,"info");
                            }
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

	$(function(){
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        idField:'id',
	        url:'${rc.contextPath}/edbOrder/jsonUnconfirmedEdbOrderInfo',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        pageSize:50,
	        pageList:[50,100],
	        columns:[[
				{field:'checkId',    title:'序号', align:'center',checkbox:true},
	            {field :'id',    title:'ID', width:20, align:'center'},
	            {field :'number', title : '订单编号', width : 50, align : 'center',
					formatter : function(value, row, index) {
						var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
						return a;
					}
				},
                {field :'payTime', title : '付款时间', width : 50, align : 'center'},
                {field :'orderStatus', title : '订单状态', width : 30, align : 'center'},
				{field :'freezeStatus', title : '冻结状态', width : 30, align : 'center'},
				{field :'totalPrice', title : '总价', width : 20, align : 'center'},
				{field :'realPrice', title : '实付', width : 20, align : 'center'},
				{field :'sellerId', title : '商家ID', width : 20, align : 'center'},
				{field :'realSellerName', title : '商家名称', width : 30, align : 'center'},
	            {field:'remark',    title:'商家备注', width:60, align:'center'},
	            {field:'remark2',    title:'客服备注', width:80, align:'center'},
	            {field:'sendStatus',    title:'确认状态', width:30, align:'center'},
	            {field:'hidden',  title:'操作', width:70,align:'center',
	                formatter:function(value,row,index){
	                    var a = '';
	                    if(row.canSend == 1){
	                    	a += '<a href="javaScript:;" onclick="send(' + row.id + ')">确认推送</a>';
							a += ' | <a href="javaScript:;" onclick="unSend(' + row.id + ')">取消推送</a>';
	                    }
						else
						{
                            a += '冻结中';
						}
	                    return a;
	                }
	            }
	        ]],toolbar:[{
                    iconCls: 'icon-add',
                    text:'批量推送',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            for (var i = 0; i < rows.length; i++) {
                                if (rows[i].canSend != 1) {
                                    $.messager.alert("info", "您选择的订单中存在已冻结订单，无法推送", "error");
                                    return;
                                }
                            }
                            $.messager.confirm('批量推送','确定批量推送至E店宝吗',function(b){
                                if(b){
                                    $.messager.progress();
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.ajax({
                                        url: '${rc.contextPath}/edbOrder/confirm',
                                        type: 'post',
                                        dataType: 'json',
                                        data: {ids: ids.join(","),'isPush':1},
                                        success: function(data){
                                            $.messager.progress('close');
                                            if(data.status == 1){
                                                $('#s_data').datagrid('clearSelections');<!-- 取消所有的已选择项。 -->
                                                if(data.dialog == 1){
                                                    $.messager.alert("提示",data.msg,"info");
                                                }
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
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的订单',"error")
                        }
                    }
                }],
	        pagination:true
	    });

        $('#sellerId').combobox({
            panelWidth:350,
            panelHeight:350,
            mode:'remote',
            url:'${rc.contextPath}/seller/jsonSellerCode?isEdbSeller=1',
            valueField:'code',
            textField:'text'
        });

	});
</script>
</body>
</html>