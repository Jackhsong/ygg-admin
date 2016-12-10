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
.large-font{
	font-size: 20px;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center',title:'积分提现处理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'',split:true" style="height: 80px;padding-top:10px">
        	<table>
				<tr>
					<td>用户Id：</td>
					<td><input name="searchAccountId" id="searchAccountId"/></td>
					<td>用户名：</td>
					<td><input name="searchAccountName" id="searchAccountName"/></td>
					<td>状态：</td>
					<td>
						<select name="searchStatus" id="searchStatus">
							<option value="1">待提现</option>
							<option value="2">已打款</option>
						</select>
					</td>
					<td>&nbsp;<a id="searchBtn" onclick="searchPartner();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
					<td>&nbsp;&nbsp;<a id="clearBtn" onclick="clearSearchForm();" href="javascript:;" class="easyui-linkbutton" style="width:80px" >重置</a></td>
				</tr>
			</table>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
		  	<table id="s_data" style=""></table>
		</div>
	    <!-- 打款 -->
	    <div id="dealWithDiv" class="easyui-dialog" style="width:500px;height:400px;padding:20px 20px;">
	        <form id="dealWithForm" method="post">
	        	<input type="hidden" id="dealWithId" name="dealWithId" value=""/>
	        	<input type="hidden" id="accountId" name="accountId" value=""/>
		        <table cellpadding="5">
		            <tr class="large-font">
		                <td>姓名：</td>
		                <td>
		                	<span name="realName" id="dealWithForm_realName"></span>
		                </td>
		            </tr>
		            <tr class="large-font">
		                <td>银行：</td>
		                <td>
		                	<span name="bank" id="dealWithForm_bank"></span>
		                </td>
		            </tr>
		            <tr class="large-font">
		                <td>账号：</td>
		                <td>
		                	<span name="bankAccount" id="dealWithForm_bankAccount"></span>
		                </td>
		            </tr>
		            <tr class="large-font">
		                <td>打款金额：</td>
		                <td style="color: red">
		                	￥<span name="money" id="dealWithForm_money"></span>
		                </td>
		            </tr>
		            <tr>
		            	<td><a onclick="showDetail();" href="javascript:;" class="easyui-linkbutton">积分明细</a></td>
		            	<td><a onclick="showOrder()" href="javascript:;" class="easyui-linkbutton">查看分销订单</a></td>
		            </tr>
		            <tr>
		            	<td>选择打款账号：</td>
		            	<td><input type="text" name="financialAffairsCardId" id="financialAffairsCardId" style="width:300px;"/></td>
		            </tr>
		        </table>
	    	</form>
	    </div>		
	</div>
</div>
<script>
function searchPartner(){
	$('#s_data').datagrid('load',{
		userId:$("#searchAccountId").val(),
		username:$("#searchAccountName").val(),
		status:$("#searchStatus").val()
	});
}

function clearSearchForm(){
	$("#searchAccountId").val('');
	$("#searchAccountName").val('');
	$("#searchStatus").find("option").eq(0).attr("selected","selected");
	$('#s_data').datagrid('load',{});
}

function cleanAddForm(){
	$("#dealWithId").val('');
	$("#accountId").val('');
	$("#dealWithForm_realName").html('');
	$("#dealWithForm_bank").html('');
	$("#dealWithForm_bankAccount").html('');
	$("#dealWithForm_money").html('');
	$("#financialAffairsCardId").combobox('clear');
}

function showDetail(){
	var id = $("#accountId").val();
	var urlStr="${rc.contextPath}/account/integralRecordList/"+id;
	window.open(urlStr,"_blank");
}

function showOrder(){
	var id = $("#accountId").val();
	var urlStr="${rc.contextPath}/partner/orderDetailList/"+id;
	window.open(urlStr,"_blank");
}
function dealWith(index){
	cleanAddForm();
	var arr=$("#s_data").datagrid("getData");
	$("#dealWithId").val(arr.rows[index].id);
	$("#accountId").val(arr.rows[index].accountId);
	$("#dealWithForm_realName").html(arr.rows[index].realName);
	$("#dealWithForm_bank").html(arr.rows[index].bank);
	$("#dealWithForm_bankAccount").html(arr.rows[index].bankAccount);
	$("#dealWithForm_money").html(arr.rows[index].money);
	$('#dealWithDiv').dialog('open');
}


$(function(){
	
	$("#financialAffairsCardId").combobox({
		url:'${rc.contextPath}/refund/adminBankInfoCode',
		valueField:'code',   
	    textField:'text'
	});
	
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/partner/jsonExchangeIntegralInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
            {field:'id', hidden:true},
            {field:'statusStr',    title:'状态', width:20, align:'center'},
            {field:'createTime',  title:'提交时间',  width:40,  align:'center'},
            {field:'accountId',     title:'用户Id',  width:40,  align:'center'},
            {field:'accountName',    title:'用户名', width:40, align:'center'},
            {field:'point',    title:'提现积分', width:50, align:'center'},
            {field:'money', title:'提现金额', width:40, align:'center'},
            {field:'realName', title:'真实姓名', width:40, align:'center'},
            {field:'bank', title:'银行', width:50, align:'center'},
            {field:'bankAccount', title:'账号', width:40, align:'center'},
            {field:'hidden',  title:'操作', width:40,align:'center',
                formatter:function(value,row,index){
                    if(row.status==1){
                    	return '<a href="javaScript:;" onclick="dealWith(' + index + ')">打款</a>';
                    }
                    else if(row.status==2){
                    	return '已打款';
                    }
                }
            }
        ]],
     	pagination:true
    });
    
    $('#dealWithDiv').dialog({
    	title:'打款',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '已打款',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#dealWithForm').form('submit',{
    				url: "${rc.contextPath}/partner/dealWithExchange",
    				onSubmit:function(){
    					var financialAffairsCardId = $("#financialAffairsCardId").combobox('getValue');
    					var wxAccount = $("form input[name='wxAccount']").val();
    					var realName = $("form input[name='realName']").val();
    					if($.trim(financialAffairsCardId) == ''){
    						$.messager.alert("提示","请选择打款账户","warn");
    						return false;
    					}
    					$.messager.progress();
    				},
    				success: function(data){
    					$.messager.progress('close');
                        var res = eval("("+data+")");
                        if(res.status == 1){
                            $.messager.alert('响应信息',"处理成功",'info',function(){
                                $('#s_data').datagrid('reload');
                                $('#dealWithDiv').dialog('close');
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'info');
                        } 
    				}
    			});
    		}
    	},
    	{
    		text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#dealWithDiv').dialog('close');
            }
    	}]
    });     
});
</script>

</body>
</html>