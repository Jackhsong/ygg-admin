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
		<div data-options="region:'north',title:'未确认笨鸟订单列表',split:true" style="height: 120px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td class="searchName">订单编号：</td>
						<td class="searchText">
							<input id="orderNumber" name="orderNumber" value="" />
						</td>
						<!-- <td class="searchName">推送状态：</td>
						<td class="searchText">
							<select id="sendStatus" name="sendStatus" style="width: 100px;">
								<option value="0">未推送</option>
								<option value="1">已推送</option>
								<option value="-1">全部</option>
							</select>
						</td> -->
						<td class="searchText" style="padding-top: 5px">
							<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
	        </form>
	        <span style="color:red;margin-left:30px">推送成功3分钟内，订单会在此列表消失</span>
            <span style="color:red;margin-left:100px;font-size: x-large">价格超过500元的订单，系统不会推送。请联系相关人员确定推送价格。</span>
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
			sendStatus : $("#orderStatus").val()
		});
	}
	
	function send(id){
		$.messager.confirm('确认推送','确认推送？',function(r){
		    if (r){
		    	$.messager.progress();
    			$.ajax({
					url: '${rc.contextPath}/birdex/sendBirdex',
					type: 'post',
					dataType: 'json',
				    data: {'ids':id},
					success: function(data){
						$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid("load");
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
	
	function cancelSend(id){
		$.messager.confirm('提示','确认取消推送？',function(r){
		    if (r){
		    	$.messager.progress();
    			$.ajax({
					url: '${rc.contextPath}/birdex/cancelPushBirdexOrder',
					type: 'post',
					dataType: 'json',
				    data: {'orderId':id,'pushStatus':0},
					success: function(data){
						$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid("load");
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
	        url:'${rc.contextPath}/birdex/jsonBirdexInfo',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        pageSize:50,
	        pageList:[50,100],
	        columns:[[
				{field:'checkId',    title:'序号', align:'center',checkbox:true},
	            {field :'id',    title:'ID', width:20, align:'center'},
	            {field :'payTime', title : '付款时间', width : 50, align : 'center'},
	            {field :'number', title : '订单编号', width : 50, align : 'center',
					formatter : function(value, row, index) {
						var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.id+'">'+row.number+'</a>';
						return a;
					}
				},
				{field :'freezeStatus', title : '退款冻结', width : 30, align : 'center'},
				{field :'totalPrice', title : '总价', width : 20, align : 'center'},
				{field :'realPrice', title : '实付', width : 20, align : 'center'},
	            {field:'receiveAddress',    title:'收货地址', width:60, align:'center'},
	            {field:'fullName',     title:'收货人',  width:35,   align:'center' },
	            {field:'idCard',     title:'身份证号',  width:50,   align:'center' },
	            {field:'remark',    title:'商家备注', width:60, align:'center'},
	            {field:'remark2',    title:'客服备注', width:80, align:'center'},
	            {field:'wrongMsg',    title:'异常(不一定准确)', width:70, align:'center'},
	            {field:'dealMsg',    title:'系统处理', width:70, align:'center'},
	            {field:'sendStatus',    title:'确认状态', width:30, align:'center'},
	            {field:'hidden',  title:'操作', width:70,align:'center',
	                formatter:function(value,row,index){
	                    var a = '';
	                    if(row.canSend == 1){
	                    	a += '<a href="javaScript:;" onclick="send(' + row.id + ')">推送</a> | ';
	                    }
	                    
	                    if(row.errorFullName == 1){
	                    	a += '<a target="_blank" href="${rc.contextPath}/overseasOrder/idCardList">去设置真实姓名</a> | ';
	                    }
	                    if(row.errorSettlement == 1){
	                    	a += row.errorSettlementEditSetUrl;
	                    }
	                    
	                    var b = '<a href="javaScript:;" onclick="cancelSend(' + row.id + ')">取消推送</a>'
	                    return a + b;
	                }
	            }
	        ]],toolbar:[{
                    iconCls: 'icon-add',
                    text:'批量推送',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                        	for(var i=0;i<rows.length;i++){
                                if(rows[i].canSend != 1){
                                	$.messager.alert("info","您选择的订单中存在有问题的订单，无法推送","error");
                                	return;
                                }
                            }
                            $.messager.confirm('批量推送','确定批量推送吗',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/birdex/sendBirdex", //上架
										{ids: ids.join(",")},
										function(data){
											if(data.status == 1){
												$.messager.alert('提示',data.msg,"info",function(){
													$('#s_data').datagrid("load");												
												})
											}else{
												$.messager.alert('提示','保存出错',"error")
											}
											$('#s_data').datagrid('clearSelections');
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的订单',"error")
                        }
                    }
                }],
	        pagination:true
	    });
	});
</script>
</body>
</html>