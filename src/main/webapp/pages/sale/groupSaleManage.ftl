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

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'商品组合管理',split:true" style="height: 120px;">
			<div style="height: 120px;padding: 15px">
	            <table>
	                <tr>
	                    <td>组合Id：</td>
	                    <td><input id="searchId" name="id" value="" /></td>
	                    <td>组合标题：</td>
	                    <td><input id="searchName" name="name" value="" /></td>
	                    <td>商品Id：</td>
	                    <td><input id="searchProductId" name="productId" value="" /></td>
	                    <td>商品编码</td>
	                    <td><input id="searchProductCode" name="productCode" value="" /></td>
	                </tr>
	                <tr>
	                	<td>商品名称：</td>
	                    <td><input id="searchProductName" name="productName" value="" /></td>
	                	<td>组合状态：</td>
	                    <td>
		                    <select name="isAvailable" id="isAvailable" style="width: 173px;">
		                    	<option value="1">可用</option>
		                    </select>
	                    </td>
	                    <td>组合类型：</td>
	                    <td>
		                    <select name="type" id="type" style="width: 173px;">
		                    	<option value="-1">全部</option>
		                    	<option value="1">特卖商品组合</option>
		                    	<option value="2">商城商品组合</option>
		                    </select>
	                    </td>
						<td>
							&nbsp;&nbsp;<a id="searchBtn" onclick="searchAcCommon()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;&nbsp;<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
	                	</td>
	                </tr>
	            </table>
    		</div>
    	</div>

		<div data-options="region:'center'" >
		      <table id="s_data" ></table>
		      
		    <!-- 修改组合商品时间begin -->
		    <div id="quickAddProductDiv" class="easyui-dialog" style="width:400px;height:250px;padding:10px 10px;">
		        <table cellpadding="5">
		            <tr>
		                <td>商品ID：<font color="red">(多个商品用英文逗号[,]分开)</font></td>
		            </tr>
		            <tr>
		            	<td><textarea rows="3" cols="45" id="quickAddProductDiv_id" onkeydown="checkEnter(event)"></textarea></td>
		            </tr>
		            <tr>
		            	<td>商品时间：</td>
		            </tr>
		            <tr>
		            	<td>
		            	<input value="" id="quickAddProductDiv_startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'quickAddProductDiv_endTime\')}'})"/>
		            	-
		            	<input value="" id="quickAddProductDiv_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'quickAddProductDiv_startTime\')}'})"/>
		            	</td>
		            </tr>
		        </table>
		    </div>
		    <!-- 修改组合商品时间end -->		      
		</div>
	</div>
</div>

<script>
	//禁止按回车
	function checkEnter(e){
		var et=e||window.event;
		var keycode=et.charCode||et.keyCode;
		if(keycode==13){
			if(window.event)
				window.event.returnValue = false;
			else
				e.preventDefault();//for firefox
		}
	}

	function searchAcCommon(){
    	$('#s_data').datagrid('load',{
    		id:$("#searchId").val(),
        	name:$("#searchName").val(),
        	isAvailable:$("#isAvailable").val(),
        	productId:$("#searchProductId").val(),
        	productName:$("#searchProductName").val(),
        	productCode:$("#searchProductCode").val(),
        	type:$("#type").val()
    	});
	}
	
	function clearSearch(){
		$("#searchId").val('');
		$("#searchName").val('');
    	$("#searchProductId").val('');
    	$("#searchProductName").val('');
    	$("#searchProductCode").val('');
    	$("#type").find("option").eq(0).attr("selected","selected");
    	$('#s_data').datagrid('load',{});
	}

	function editIt(index){
    	var arr=$("#s_data").datagrid("getData");
    	var urlStr = "${rc.contextPath}/sale/editGroupSale/"+arr.rows[index].id;
    	window.open(urlStr,"_blank");
	}

	function productManage(index){
    	var arr=$("#s_data").datagrid("getData");
    	var urlStr = "${rc.contextPath}/sale/groupSaleProductManage/"+arr.rows[index].id;
    	window.open(urlStr,"_blank");
	}

	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sale/jsonInfo',
            queryParams:{
            	isAvailable:1
            },
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'组合ID', align:'center'},
				{field:'isAvailable',    title:'状态', width:50, align:'center'},
                {field:'name',    title:'组合标题', width:70, align:'center'},
                {field:'typeName',    title:'组合类型', width:50, align:'center'},
                {field:'desc',    title:'描述', width:70, align:'center'},
                {field:'gegesay',    title:'格格说', width:70, align:'center'},
                {field:'weixin',    title:'微信分享标题', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a>';
                        var b = ' | <a href="javaScript:;" onclick="productManage(' + index + ')">管理商品</a>';
                        return a + b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商品组合',
                iconCls:'icon-add',
                handler:function(){
                	window.location.href = "${rc.contextPath}/sale/addGroupSale"
                }
            },'-',{
                iconCls: 'icon-edit',
                text:'修改组合时间',
                handler: function(){
                	$("#quickAddProductDiv_id").val('');
                	$("#quickAddProductDiv_startTime").val('');
                	$("#quickAddProductDiv_endTime").val('');
					$("#quickAddProductDiv").dialog('open');
                }
            }],
            pagination:true
        });
		
		$('#quickAddProductDiv').dialog({
            title:'修改组合时间',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var groupId = $("#quickAddProductDiv_id").val();
                	var startTime = $("#quickAddProductDiv_startTime").val();
                	var endTime = $("#quickAddProductDiv_endTime").val();
                	if($.trim(groupId) == ''){
                		$.messager.alert("提示",'请输入特卖组合ID',"error");
                		return false;
                	}else if($.trim(startTime) == ''){
                		$.messager.alert("提示",'请输入开始时间',"error");
                		return false;
                	}else if($.trim(endTime) == ''){
                		$.messager.alert("提示",'请输入结束时间',"error");
                		return false;
                	}else{
               			$.messager.progress();
           				$.ajax({
           		            url: '${rc.contextPath}/sale/batchUpdateGroupProductTime',
           		            type: 'post',
           		            dataType: 'json',
           		            data: {'groupIds':groupId,'startTime':startTime, 'endTime':endTime},
           		            success: function(data){
           		            	$.messager.progress('close');
           		                if(data.status == 1){
           		                	$('#quickAddProductDiv').dialog('close');
           		                	$('#s_data').datagrid('reload');
           		                	$.messager.alert("响应消息",'修改成功',"info");
           		                }else{
           		                	$.messager.alert("响应消息",data.msg,"error");
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
                    $('#quickAddProductDiv').dialog('close');
                }
            }]
        });
	})
</script>

</body>
</html>