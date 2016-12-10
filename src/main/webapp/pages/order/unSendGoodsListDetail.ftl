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

<div data-options="region:'west',title:'menu',split:true" style="width: 180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
	
<div data-options="region:'center',title:'content'" style="padding: 5px;">
	<div title="24订单管理" class="easyui-panel" style="padding: 10px">
		<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
			<form action="${rc.contextPath}/order/exportUnSendGoodsListDetail" id="searchForm" method="post">
				<table>
					<tr>
						<td>
							查询时间：<span id="selectTime">${selectTime}&nbsp;</span>
							<input type=hidden id="exportType" name="exportType" value="" />
							<input type=hidden id="operType" name="operType" value="${operType}" />
							<input type=hidden id="sellerName" name="sellerName" value="${id}" />
						</td>
						<td>
						&nbsp;&nbsp;发货类型：
						<select id="sellerType" name="sellerType" value="">
							<option value="0">全部</option>
							<option value="1" <#if seller?exists && seller.sellerType==1>selected</#if>>国内</option>
							<option value="2" <#if seller?exists && seller.sellerType==2>selected</#if>>保税区</option>
							<option value="3" <#if seller?exists && seller.sellerType==3>selected</#if>>香港</option>
						</select>
						&nbsp;&nbsp;商家：
						<select id="sellerId" name="sellerId" >
							<option value="0">全部&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</option>
						</select>
						&nbsp;&nbsp;发货地：<span id="sendAddress" style="color:red"><#if seller?exists>${seller.sendAddress}</#if></span>
						</td>
						<td>&nbsp;&nbsp;
							<a id="searchBtn" onclick="searchInfo()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;&nbsp;<a id="searchBtn" onclick="exportAllResult()" href="javascript:;" class="easyui-linkbutton" >导出结果</a>
							&nbsp;&nbsp;&nbsp;<a id="searchBtn" onclick="exportAllSeller()" href="javascript:;" class="easyui-linkbutton" >导出商家发货表</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
			
		<div class="selloff_mod">
			<table id="s_data">

			</table>
		</div>
	</div>
	<!-- 填写发货所需信息的div begin -->
				<div id="sendOrderDiv" class="easyui-dialog" style="width:350px;height:220px;padding:20px 20px;">
	            	<form id="sendOrderForm" method="post">
	            		<input id="orderId" type="hidden" name="orderId" />
	            		<input type="radio" id="sendType1" name="sendType" value="1" checked /> 有物流
	            		<input type="radio" id="sendType0" name="sendType" value="0"/> 无物流
	            		<div id="haveChannel">
							<table cellpadding="5">
		                		<tr>
		                    		<td>物流渠道:</td>
		                    		<td><input id="channel" type="text" name="channel" ></input></td>
		                		</tr>
		                		<tr>
		                    		<td>物流单号:</td>
		                    		<td><input id="number" type="text" name="number" ></input></td>
		                		</tr>
		                		<tr>
		                    		<td>物流运费:</td>
		                    		<td><input id="money" type="text" name="money" ></input></td>
		                		</tr>
		            		</table>	            		
	            		</div>
	        		</form>
	        	</div>
				<!-- 填写发货所需信息的div end -->
</div>
	

<script>

function sendOrder(index) {
	$("#sendType1").prop("checked", true);
	$("#sendType1").change();
	$('#channel').combobox('clear');
	$('#money').val("");
	$('#number').val("");
	var arr=$("#s_data").datagrid("getData");
	$('#orderId').val(arr.rows[index].id);
	$('#sendOrderDiv').dialog('open');
}

function searchInfo() {
	$('#s_data').datagrid('load',{
		type:1,
		sellerType : $("#sellerType").val(),
		sellerId : $("input[name='sellerId']").val(),
		operType : $("#operType").val()
	});
}


function exportAllSeller(){
	$('#exportType').val("seller");
	$('#searchForm').submit();
}

function exportAllResult(){
	$('#exportType').val("result");
	$('#searchForm').submit();
}

$(function(){
	
	$('#sendType1').change(function(){
		if($('#sendType1').is(':checked')){
			$('#haveChannel').show();
		}
	});
	$('#sendType0').change(function(){
		if($('#sendType0').is(':checked')){
			$('#haveChannel').hide();
		}
	});
	
	$('#channel').combobox({   
	    url:'${rc.contextPath}/order/jsonCompanyCode',   
	    valueField:'code',   
	    textField:'text'  
	});
	
	$('#sendOrderDiv').dialog({
        title:'物流信息',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'发货',
            iconCls:'icon-ok',
            handler:function(){
                $('#sendOrderForm').form('submit',{
                    url:"${rc.contextPath}/order/sendOrder",
                    success:function(data){
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $('#sendOrderDiv').dialog('close');
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                                $('#s_data').datagrid('reload');
                            });
                        } else if(res.status == 2){
                            $.messager.alert('响应信息',"该订单已经发货",'info',function(){
                            });
                        }else if(res.status == 3){
                            $.messager.alert('响应信息',res.msg,'info',function(){
                            });
                        } 
                        else{
                            $.messager.alert('响应信息',"发货失败",'error',function(){
                            });
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#sendOrderDiv').dialog('close');
            }
        }]
    })
	
	$('#sellerId').combobox({
        panelWidth:350,
        mode:'remote',
	    url:'${rc.contextPath}/seller/jsonSellerCode',
	    valueField:'code',   
	    textField:'text',
	    value:$("#sellerName").val(),
	    onSelect:function(){
	    	var sellerId=$("input[name='sellerId']").val();
	    	$.post("${rc.contextPath}/product/ajaxSellerInfo", 
				{id: sellerId},
				function(data){
					if(data.status == 1){
						$("#sendAddress").html("");
						$("#sendAddress").html(data.sendAddress);
					}
					else{
						$("#sendAddress").val("");
					}
			}, "json");
	    }
});
	
	$('#s_data') .datagrid({
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'id',
		url : '${rc.contextPath}/order/jsonUnSendGoodsListDetail',
		queryParams: {
			type: '0',
			operType: ${operType},
			fromId:${id}
		},
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : true,
		pageSize : 50,
		pageList : [ 50, 60 ],
		columns : [ [
			{field : 'createTimeStr', title : '下单时间', width : 40, align : 'center'},
			{field : 'payTimeStr', title : '付款时间', width : 40, align : 'center'},
			{field : 'orderChannel', title : '订单来源', width : 30, align : 'center'},
			{field : 'hrefNumber', title : '订单编号', width : 30, align : 'center'},
			{field : 'statusStr', title : '订单状态', width : 25, align : 'center'},
			{field : 'totalPrice', title : '订单总价', width : 45, align : 'center'},
			{field : 'fullName', title : '收货人', width : 30, align : 'center'},
			{field : 'mobileNumber', title : '收货手机', width : 30, align : 'center'},
			{field : 'sellerName', title : '商家', width : 30, align : 'center'},
			{field : 'sendAddress', title : '发货地', width : 40, align : 'center'},
			{field : 'warehouse', title : '分仓', width : 50, align : 'center'},
			{field : 'hidden', title : '操作', width : 30, align : 'center',
				formatter : function(value, row, index) {
					var f2 = '<a href="javaScript:;" onclick="sendOrder(' + index + ')">发货</a>';
            		return f2;
				}
			} 
		] ],
		pagination : true,
        rownumbers:true
	});  
});
</script>

</body>
</html>