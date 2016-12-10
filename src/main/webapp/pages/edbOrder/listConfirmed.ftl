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
		<div data-options="region:'north',title:'已确认E店宝订单列表',split:true" style="height: 120px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td class="searchName">订单编号：</td>
						<td class="searchText">
							<input id="orderNumber" name="orderNumber" value="" />
						</td>
                        <td class="searchName">&nbsp;商家：</td>
                        <td class="searchText"><input id="sellerId" name="sellerId" value=""/></td>
                        <td class="searchName">&nbsp;订单状态：</td>
                        <td class="searchText">
                            <select id="orderStatus" name="orderStatus" >
                                <option value="0">全部</option>
                                <option value="1">未付款</option>
                                <option value="2">待发货</option>
                                <option value="3">已发货</option>
                                <option value="4">交易成功</option>
                                <option value="5">用户取消</option>
                                <option value="6">超时取消</option>
                            </select>
                        </td>
                        <td class="searchName">&nbsp;推送状态：</td>
                        <td class="searchText">
							<select id="pushStatus">
								<option value="0">全部</option>
								<option value="1">推送成功</option>
								<option value="2">推送失败</option>
								<option value="3">等待推送</option>
								<option value="4">不推送</option>
							</select>
						</td>
                        <td class="searchName">&nbsp;是否成功获取发货信息：</td>
                        <td class="searchText">
                            <select id="receiveStatus">
                                <option value="-1">全部</option>
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                        </td>
						<td class="searchText" style="padding-top: 5px">
							<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                            &nbsp;&nbsp;总计推送失败数：<span style="color: red">${errorNums!'0'}</span>
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
            pushStatus : $("#pushStatus").val(),
            receiveStatus : $("#receiveStatus").val(),
            orderStatus : $("#orderStatus").val(),
            sellerId : $("input[name='sellerId']").val()
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
	        url:'${rc.contextPath}/edbOrder/jsonConfirmedEdbOrderInfo',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
            singleSelect:true,
	        pageSize:50,
	        pageList:[50,100],
	        columns:[[
	            {field :'id',    title:'ID', width:20, align:'center'},
	            {field :'number', title : '订单编号', width : 40, align : 'center',
					formatter : function(value, row, index) {
						var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
						return a;
					}
				},
                {field :'payTime', title : '付款时间', width : 40, align : 'center'},
                {field :'orderStatus', title : '订单状态', width : 20, align : 'center'},
				{field :'totalPrice', title : '总价', width : 20, align : 'center'},
				{field :'realPrice', title : '实付', width : 20, align : 'center'},
				{field :'sellerId', title : '商家ID', width : 20, align : 'center'},
				{field :'realSellerName', title : '商家名称', width : 30, align : 'center'},
	            {field:'remark',    title:'商家备注', width:60, align:'center'},
	            {field:'remark2',    title:'客服备注', width:80, align:'center'},
	            {field:'pushTime',    title:'推送时间', width:40, align:'center'},
	            {field:'pushStatus',    title:'推送状态', width:20, align:'center'},
	            {field:'pushError',    title:'推送失败原因', width:60, align:'center'},
	            {field:'sendStatus',    title:'确认状态', width:30, align:'center'},
	            {field:'hidden',  title:'操作', width:30,align:'center',
	                formatter:function(value,row,index){
	                    var a = '';
	                    if(row.canUnSend == 1){
							a += '<a href="javaScript:;" onclick="unSend(' + row.id + ')">取消推送</a>';
	                    }
	                    return a;
	                }
	            }
	        ]],
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