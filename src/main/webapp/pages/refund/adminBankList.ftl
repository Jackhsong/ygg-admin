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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div title="财务打款账户管理 class="easyui-panel" style="padding:10px">
        <table id="s_data" >
			
        </table>
	</div>
	
	<!-- 新增 -->
	<div id="newDiv">
		<form id="saveBank" method="post">
		<table>
			<tr>
				<td><span id="bankOrAlipay">银行</span></td>
				<td><input type="hidden" id="bankOrAlipayCode" name="bankOrAlipayCode" value="1"/></td>
			</tr>
			<tr>
				<td>银行类型：</td>
				<td>
					<div id="divBankType">
					<select id="bankType" name="bankType">
						<option value="1">中国工商银行</option>
						<option value="2">中国农业银行</option>
						<option value="3">中国银行</option>
						<option value="4">中国建设银行</option>
						<option value="5">中国邮政储蓄银行</option>
						<option value="6">交通银行</option>
						<option value="7">招商银行</option>
						<option value="8">中国光大银行</option>
						<option value="9">中信银行</option>
					</select>
					</div>
				</td>
			</tr>
			<tr>
				<td>开户姓名：</td>
				<td><input name="cardName" id="cardName" style="width:100"></td>
			</tr>
			<tr>
				<td>账号：</td>
				<td><input name="cardNumber" id="cardNumber" style="width:100"></td>
			</tr>
		</table>
		</form>
	</div>
</div>

<script>
	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/refund/adminBankInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,100],
            columns:[[
                {field:'id',    title:'ID', width:50, align:'center'},
                {field:'typeStr',    title:'账号类型', width:50, align:'center'},
                {field:'cardName',    title:'真实名称', width:70, align:'center'},
                {field:'bankTypeStr',     title:'银行',  width:50,   align:'center' },
                {field:'bankCardNumber',    title:'银行卡号', width:70, align:'center'},
                {field:'alipayCardNumber',    title:'支付宝账号', width:70, align:'center'}
            ]],
            toolbar:[{
                id:'_add',
                text:'新增银行',
                iconCls:'icon-add',
                handler:function(){
                	$('#bankOrAlipay').text("银行");
                	$('#bankOrAlipayCode').val(1);
                	$('#cardName').val("");
                	$('#cardNumber').val("");
                	$('#divBankType').show();
                	$('#newDiv').dialog("open");
                }
            },'-',{
                id:'_add',
                text:'新增支付宝',
                iconCls:'icon-add',
                handler:function(){
                	$('#bankOrAlipay').text("支付宝");
                	$('#bankOrAlipayCode').val(2);
                	$('#cardName').val("");
                	$('#cardNumber').val("");
                	$('#divBankType').hide();
                	$('#newDiv').dialog("open");
                }
            },'-',{
                iconCls: 'icon-remove1',
                text:'',
                handler: function(){
                    var rows = $('#s_data').datagrid("getSelections");
                    if(rows.length > 0){
                        $.messager.confirm('★~★','目前禁用！！！',function(b){
                            if(false){
                                var ids = [];
                                for(var i=0;i<rows.length;i++){
                                    ids.push(rows[i].id)
                                }
                                $.post("${rc.contextPath}/refund/deleteBank", //全部删除
									{ids: ids.join(",")},
									function(data){
										if(data.status > 1){
											$('#s_data').datagrid('reload');
										}else{
											$.messager.alert('提示','删除出错',"error")
										}
									},
								"json");
                            }
                 		})
                    }else{
                        $.messager.alert('提示','请选择要操作的商品',"error")
                    }
                }
            }],
            pagination:true
        });
		
		$('#newDiv').dialog({
            title:'新增打款账户',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                	$.messager.progress();
                    $('#saveBank').form('submit',{
                        url:"${rc.contextPath}/refund/saveBank",
                        success:function(data){
                        	$.messager.progress('close');
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#newDiv').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                    $('#s_data').datagrid('load');
                                });
                            }else{
                                $.messager.alert('响应信息',res.msg,'error',function(){
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
                	$('#newDiv').dialog("close");
                }
            }]
        });
		
	})
</script>

</body>
</html>