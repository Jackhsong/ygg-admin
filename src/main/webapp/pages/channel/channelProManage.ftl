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
		<div data-options="region:'north',title:'商品管理',split:true" style="height: 110px;">
			<div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/channelProManage/exportChannelPro" method="post">
		            <table class="search">
		            	<tr>
		            		<td>渠道：</td>
		                    <td><input id="search_channelId" name="channelId" class="easyui-combobox" style="width:150px;" /></td>
		                    <td>仓库：</td>
		                    <td><input id="search_wareHouseType" name="wareHouseId" class="easyui-combobox" style="width:150px;" /></td>
		                </tr>
		                <tr>
		                    <td>商品编码：</td>
		                    <td><input id="search_productCode"  name="productCode" type="text" value="" /></td>
		                    <td>第三方商品ID：</td>
		                    <td><input id="search_productId" name="id" type="text"  value="" /></td>
		                    <td>商品名称：</td>
		                    <td><input id="search_productName" name="productName" type="text" value="" /></td>
							<td>
								&nbsp;<a id="searchBtn" onclick="searchChannelPro()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		                	</td>
		                	<td>
								&nbsp;<a id="exportBtn" onclick="exportChannelPro()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">导出数据</a>
		                	</td>
		                	<td></td>
		                </tr>
		            </table>
		        </form>
		    </div>
		 </div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
		  <!-- 新增商品 Begin -->
	    <div id="channelProManageDiv" class="easyui-dialog" style="width:600px;height:300px;padding:15px 20px;">
	        <form id="channelProManageForm" method="post">
				<input id="form_id" type="hidden" name="id" >
				<p>
					<span>选择渠道：</span>
					<span><input type="text" id="form_channelId" name="channelId" maxlength="8" style="width: 300px;"/></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>商品编码：</span>
					<span><input type="text" id="form_productCode" name="productCode" maxlength="50" style="width: 300px;"/></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>发货仓库：</span>
					<span><input type="radio" id="form_hzwh"  name="wareHouseId"  value="1"/>杭州仓&nbsp;&nbsp;</span>
					<span><input type="radio" id="form_birwh" name="wareHouseId"  value="2"/>香港笨鸟仓</span>
					<span><input type="radio" id="form_japwh" name="wareHouseId"  value="3"/>日本埼玉仓</span>
				</p>
				<p>
					<span>商品名称：</span>
					<span><input type="text" id="form_productName" name="productName" maxlength="100" style="width: 300px;"/></span>
				</p>
	    	</form>
	      </div>
	    <!-- 新增商品 End -->
	    
    	<!-- 批量新增Begin -->
		<div id="batchChannelProDiv" style="width:600px;height:200px;padding:20px 20px;">
			<form method="post" id="batchChannelProForm" enctype="multipart/form-data">
    			<table cellpadding="5">
    				<tr>
	    				<td>上传文件：</td>
	    				<td><input id="orderFileBox" type="text" name="uploadChannelProFile" style="width:300px" /></td>
	    			</tr>
	    			<tr>
	    				<td colspan="2">
	    					<a onclick="downloadTemplate()" href="javascript:;" class="easyui-linkbutton">下载模板</a>
	    				</td>
	    			</tr>
    			</table>
			</form>
 		</div>
    	<!--批量新增End-->	
    		
		</div>
	</div>
</div>

<script>

	<!--查询商品-->
	function searchChannelPro(){
		$('#s_data').datagrid('load', {
			id:$('#search_productId').val(),
			channelId:$('#search_channelId').combobox('getValue'),
			wareHouseId:$('#search_wareHouseType').combobox('getValue'),
			productCode:$('#search_productCode').val(),
			productName:$('#search_productName').val()
		});
	}

	<!--下载模板-->
	function downloadTemplate(){
		window.location.href="${rc.contextPath}/channelProManage/downloadTemplate";
	}
	
	<!--导出文件-->
	function exportChannelPro(){
		$('#searchForm').submit();
	}

	<!--批量新增 打开文件按钮-->
	$('#orderFileBox').filebox({
		buttonText: '打开文件',
		buttonAlign: 'right'
	});
	
	<!--商品编码blur监听-->
	<!--
	$(function() {
		$('#form_productCode').blur(function(){
			searchProductName();
		});
	});
	-->
	
	<!--查找商品名称-->
	function searchProductName(){
		$('#form_productName').val("");
	  	var channel = $('#form_channelId').combobox('getValue');
	  	var code = $('#form_productCode').val();
	    var wh = $("input[name='wareHouseId']:checked").val();
	    if($.trim(channel)==''){
			$.messager.alert('提示','请选择渠道','info');
			return false;
		}else if($.trim(code)==''){
			$.messager.alert('提示','请填写商品编码','info');
			return false;
		}else if($.trim(wh)==''){
			$.messager.alert('提示','请填写仓库','info');
			return false;
		}
		var params = {'productCode':code,'wareHouseId':wh};
		$.ajax({
                type: 'post',
                url: '${rc.contextPath}/channelProManage/getProductName',
                data: params,
                dataType: 'json',
                success: function(data) {
                    if(data.status == 0){
                        $.messager.alert('响应信息',data.msg,'error');
                    }else{
                        $('#form_productName').val(data.productName);
                    }
                },
                error: function(xhr, msg, e) {
                    alert("error");
                }
        });
	}


	<!--radio事件change监听-->
	$(function() {
		$("input[name='wareHouseId']").change(function(){
			searchProductName();
		});
	});

	<!--加载渠道-->
	$("input[name='channelId']").each(function(){
			$(this).combobox({
				url:'${rc.contextPath}/channel/jsonChannelCode',   
			    valueField:'code',   
			    textField:'text'
			});
	});
	
	<!--加载仓库-->
	$('#search_wareHouseType').combobox({
		panelWidth:350,
    	panelHeight:350,
		mode:'remote',
	    url:'${rc.contextPath}/channelProManage/jsonWareHouseCode',   
	    valueField:'code',   
	    textField:'text'  
	});
	
	<!--编辑数据-->
	function editForm(index){
		cleanChannelForm();
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		var channelId = arr.rows[index].channelId;
		var chaennlName = arr.rows[index].channelName;
		var wareHouseId = arr.rows[index].wareHouseId;
		var wareHouseName = arr.rows[index].wareHouseName;
		var productCode = arr.rows[index].productCode;
		var productName = arr.rows[index].productName
		
		$('#form_id').val(id);
		$('#form_channelId').combobox('setValue',channelId);
    	$("input[name='wareHouseId']").each(function(){
    		if($(this).val()==wareHouseId){
    			$(this).prop('checked',true);
    		}
    	});
    	$('#form_productCode').val(productCode);
    	$('#form_productName').val(productName);
    	$('#channelProManageDiv').dialog('open');
	}

	<!--清除ChannelForm数据-->
	function cleanChannelForm(){
		$("#form_channelId").val('');
		$("#form_productCode").val('');
		$("#form_productName").val('');
		$("#input[name='wareHouseId']").each(function(){
			$(this).val('');
		});
	}
	
	<!--清除UploadForm数据-->
	function cleanUploadForm(){
		$("#orderFileBox").filebox('clear');
	}

	<!--加载Grid-->
	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/channelProManage/jsonChannelProInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50],
            columns:[[
            	{field:'id',    title:'第三方商品ID', width:50, align:'center'},
                {field:'channelName',    title:'渠道名', width:50, align:'center'},
                {field:'wareHouseName',    title:'仓库名', width:50, align:'center'},
                {field:'productCode',    title:'商品编码', width:50, align:'center'},
                {field:'productName',    title:'商品名称', width:50, align:'center'},
                {field:'assembleCount',    title:'组合销售件数', width:60, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                 		var edit = '<a href="javascript:;" onclick="editForm(' + index + ')">编辑</a>';
                      	return edit;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                		cleanChannelForm();
                    	$('#channelProManageDiv').dialog('open');
                	}
            	},
            	{
            	id:'_batch_add',
                iconCls: 'icon-add',
                text:'批量新增',
                handler: function(){
                		cleanUploadForm();
                		$('#batchChannelProDiv').dialog('open');
                	}
                }],
            pagination:true
        });
        
		<!--新增商品-->
	    $('#channelProManageDiv').dialog({
	    	title:'新增商品',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#channelProManageForm').form('submit',{
	    				url: "${rc.contextPath}/channelProManage/saveOrUpdate",
	    				onSubmit:function(){
	    					var channel = $('#form_channelId').combobox('getValue');
	    					var code = $('#form_productCode').val();
	    					var wh = $("input[name='wareHouseId']:checked").val();
	    					var name = $('#form_productName').val();
	    					if($.trim(channel)==''){
	    						$.messager.alert('提示','请选择渠道','info');
	    						return false;
	    					}else if($.trim(code)==''){
	    						$.messager.alert('提示','请输入商品编码','info');
	    						return false;
	    					}else if($.trim(wh) == ''){
	    						$.messager.alert('提示','请选择发货仓库','info');
	    						return false;
	    					}else if($.trim(name) == ''){
	    						$.messager.alert('提示','商品名称不能为空','info');
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#s_data').datagrid('load');
	                                $('#channelProManageDiv').dialog('close');
	                            });
	                        } else if(res.status == 0){
	                            $.messager.alert('响应信息',res.msg,'error');
	                        } 
	    				},
	    				error: function(xhr){
				         	$.messager.progress('close');
				        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"error");
				       }
	    			});
	    		}
	    	},
	    	{
	    		text:'取消',
	            align:'left',
	            iconCls:'icon-cancel',
	            handler:function(){
	                $('#channelProManageDiv').dialog('close');
	            }
	    	}]
	    });	
	    
	    	    
	    $('#batchChannelProDiv').dialog({
	    	title: '批量新增',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '确定',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#batchChannelProForm').form('submit',{
	    				url: "${rc.contextPath}/channelProManage/uploadChannelProFile",
	    				onSubmit:function(){
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 0){
	                            $.messager.alert('响应信息','保存成功','info',function(){
	                                $('#s_data').datagrid('reload');
	                                $('#batchChannelProDiv').dialog('close');
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
	            	cleanUploadForm();
                    $('#batchChannelProDiv').dialog('close');
	            }
	    	}]
	    });
	});
</script>

</body>
</html>