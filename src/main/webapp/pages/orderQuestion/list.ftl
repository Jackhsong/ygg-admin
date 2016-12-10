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
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1340px;
	align:center;
}
</style>
</head>
<body class="easyui-layout">

	<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
	<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" ></div>

	<div data-options="region:'center'" style="padding: 5px;">
		<div id="cc" class="easyui-layout" data-options="fit:true" >
			<div data-options="region:'north',title:'客服问题登记管理',split:true" style="height: 150px;">
				<form action="${rc.contextPath}/orderQuestion/export" id="searchForm" method="post" >
						<input type="hidden" id="status" name="status" value="${status}"/>
						<table class="search">
							<tr>
								<td class="searchName">订单编号：</td>
								<td class="searchText"><input id="orderNo" name="orderNo" value="" /></td>
								<td class="searchName">用户ID：</td>
								<td class="searchText"><input id="accountId" name="accountId" value=""/></td>
								<td class="searchName">用户名：</td>
								<td class="searchText"><input id="accountName" name="accountName" value="" /></td>
								<td class="searchName">商家：</td>
								<td class="searchText"><input id="sellerId" name="sellerId" value=""/></td>
							</tr>
							<tr>
								<td class="searchName">顾客问题进度：</td>
								<td class="searchText">
									<select id="customerStatus" name="customerStatus" style="width:150px;">
										<option value="-1">全部</option>
										<option value="1">进行中</option>
										<option value="2">已完结</option>
									</select>
								<td class="searchName">商家对接进度：</td>
								<td class="searchText">
									<select id="sellerStatus"  name="sellerStatus" style="width:150px;">
										<option value="-1">全部</option>
										<option value="1">进行中</option>
										<option value="2">已完结</option>
									</select>
								</td>
                                <td class="searchName">订单类型：</td>
                                <td class="searchText">
                                    <select id="orderType"  name="orderType" style="width:150px;">
                                        <option value="-1">全部</option>
                                        <option value="1">换吧网络</option>
                                        <option value="2">心动慈露</option>
                                        <option value="3">心动慈露
</option>
                                    </select>
                                </td>
								<td class="searchName">顾客问题类型：</td>
								<td class="searchText"><input id="templateId" name="templateId" value=""/></td>
                                <td class="searchName">问题描述：</td>
                                <td class="searchText"><input id="questionDesc" name="questionDesc" value=""/></td>
							</tr>
							<tr>
                                <td class="searchName">问题Id：</td>
                                <td class="searchText"><input id="questionId" name="questionId" value="" /></td>
								<td class="searchName">创建人：</td>
								<td class="searchText">
									<select id="createUser" name="createUser" style="width:150px;">
										<option value="-1">--请选择--</option>
										<#list userList as user>
											<option value="${user.id?c}">${user.realname}</option>
										</#list>
									</select>
								</td>
								<td class="searchName">创建时间：</td>
								<td class="searchText">
									<input id="searchCreateTimeBegin" name="createTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" value=""/> - 
									<input id="searchCreateTimeEnd" name="createTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})" value=""/>
									
								</td>
								<td class="searchName"></td>
								<td class="searchText" style="padding-top: 5px">
									<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>					
									<a id="submit"  onClick="exportResult();" href="javascript:;"  class="easyui-linkbutton" data-options="iconCls:'icon-print'">导出</a>
								</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</table>
					</form>
			</div>
			<div data-options="region:'center'" >
				<table id="s_data"></table>
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
	
		function searchOrder() {
			$('#s_data').datagrid('load', {
				status:$("#status").val(),
				orderNo : $("#orderNo").val(),
				accountId : $("#accountId").val(),
				accountName : $("#accountName").val(),
				sellerId : $("#sellerId").combobox('getValue'),
				createUser : $("#createUser").val(),
				customerStatus : $("#customerStatus").val(),
				sellerStatus : $("#sellerStatus").val(),
				templateId : $("#templateId").combobox('getValue'),
				questionId : $("#questionId").val(),
				questionDesc : $("#questionDesc").val(),
				createTimeBegin : $("#searchCreateTimeBegin").val(),
				createTimeEnd : $("#searchCreateTimeEnd").val(),
				orderType : $("#orderType").val()
			});
		}
		
		
		function exportResult(){
			$('#searchForm').attr("action", "export").submit();
		}

		function updateIsMark(id,isMark){
			var tip = '';
			if(isMark == 1){
				tip = '确定标记吗？';
			}else if(isMark == 0){
				tip = '确定取消标记吗？';
			}
			$.messager.confirm('提示',tip,function(b){
				if(b){
					$.messager.progress();
					$.ajax({
			            url: '${rc.contextPath}/orderQuestion/updateOrderQuestionMark',
			            type: 'post',
			            dataType: 'json',
			            data: {'id':id,'isMark':isMark},
			            success: function(data){
			            	$.messager.progress('close');
			                if(data.status == 1){
			                	$('#s_data').datagrid('reload');
			                	$.messager.alert("提示",data.msg,"info");
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
		$(function() {

			$('#sellerId').combobox({
                	panelWidth:350,
                	panelHeight:350,
                	mode:'remote',
				    url:'${rc.contextPath}/seller/jsonSellerCode',
				    valueField:'code',   
				    textField:'text'
			});
		
			$("#templateId").combobox({
				url:'${rc.contextPath}/orderQuestion/jsonOrderQuestionTemplate',
			    valueField:'code',   
			    textField:'text'
			});
			
			$('#s_data') .datagrid(
			{
				nowrap : false,
				striped : true,
				collapsible : true,
				idField : 'id',
				url : '${rc.contextPath}/orderQuestion/jsonQuestionListInfo',
				queryParams: {
					status: '${status!"1"}'
				},
				loadMsg : '正在装载数据...',
				fitColumns : true,
				remoteSort : true,
				singleSelect : true,
				pageSize : 30,
				columns : [ [
						{field : 'questionId', title : '问题Id', width : 15, align : 'center'},
						{field : 'customerStatusStr', title : '顾客问<br/>题进度', width : 25, align : 'center'},
						{field : 'leftTime', title : '回复顾<br/>客时限', width : 20, align : 'center'},
						{field : 'customerDealDetail', title : '顾客问题描述', width : 70, align : 'left'},
						{field : 'sellerStatusStr', title : '商家对<br/>接进度', width : 20, align : 'center'},
						{field : 'sellerDealDetail', title : '商家对接描述', width : 70, align : 'left'},
						{field : 'isPush', title : '是否推<br>给商家', width : 20, align : 'center',
							formatter:function(value,row,index){
								if(parseInt(row.isPush)==1){
									return '是';
								}else{
									return '否';
								}
							}	
						},
						{field : 'sellerFeedbackDetail', title : '商家反馈', width : 70, align : 'left'},
						{field : 'templateName', title : '问题类型', width : 35, align : 'center',
							formatter:function(value, row, index){
								var a = '<a target="_blank" href="${rc.contextPath}/orderQuestion/orderQuestionDetail/'+row.questionId+'">'+row.templateName+'</a>';
								return a;
							}	
						},
						{field : 'createTime', title : '创建时间', width : 30, align : 'center'},
						{field : 'createUser', title : '创建者', width : 20, align : 'center'},
						{field : 'orderNo', title : '订单编号', width : 35, align : 'center',
							formatter : function(value, row, index) {
								var a = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.orderId+'">'+row.orderNo+'</a>';
								return a;
							}
						},
						{field : 'orderType', title : '订单类型', width : 24, align : 'center'},
						{field : 'oStatusDescripton', title : '订单状态', width : 20, align : 'center'},
						{field : 'payTime', title : '付款时间', width : 30, align : 'center'},
						{field : 'sendTime', title : '发货时间', width : 30, align : 'center'},
						{field : 'totalPrice', title : '订单总价', width : 20, align : 'center'},
						{field : 'realPrice', title : '实付金额', width : 20, align : 'center'},
						{field : 'raFullName', title : '收货人', width : 20, align : 'center'},
						{field : 'raMobileNumber', title : '收货手机', width : 30, align : 'center'},
						{field : 'sellerName', title : '商家', width : 40, align : 'center'},
						{field : 'sendAddress', title : '发货地', width : 30, align : 'center'},
						{field:'hidden',  title:'操作', width:20,align:'center',
		                    formatter:function(value,row,index){
		                        if(row.isMark == 1){
		                        	return '<a href="javaScript:;" onclick="updateIsMark(' + row.questionId + ',' + 0 + ')">取消标记</a>';
		                        }else{
		                        	return  '<a href="javaScript:;" onclick="updateIsMark(' + row.questionId + ',' + 1 + ')">标记</a>';
		                        }
		                    }
		                }
				] ],
				pagination : true,
				rownumbers : true
			});

		});
	</script>

</body>
</html>