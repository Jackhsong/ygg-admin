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
	
<div data-options="region:'center',title:''" style="padding: 5px;">
	<div title="笨鸟商品采购价管理" class="easyui-panel" style="padding: 10px">
		<div id="searchDiv" class="datagrid-toolbar" style="height: 50px; padding: 15px">
			<form action="${rc.contextPath}/birdex/exportOverseasProductInfo" id="searchForm" method="post">
				<table>
					<tr>
						<td>
							商品编码：
							<input id="code" name="code" value="" />	
						</td>
						<td>
							<!-- 导单名称：
							<input id="exportName" name="exportName" value="" />	 -->
						</td>
						<td>
			&nbsp;&nbsp;<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
						<td>
			<!-- &nbsp;&nbsp;<a id="searchBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a> -->
						</td>
					</tr>
				</table>
			</form>
			<br/>
			<div style="display:none">
				<div>
					<form action="${rc.contextPath}/birdex/exportImportOverseasProTemplate" id="searchForm" method="post">
						<input type="submit" value="下载导入模板" style="width:100px"/>
					</form>
				</div>
				<br/>
				<div>
				
				<form id="importForm" method="post" enctype="multipart/form-data">
					<input id="orderFileBox" type="text" name="orderFile" style="width:300px" />&nbsp;
					<input type="button" value="批量导入" onClick="return uploadPro();" style="width:100px"/>
				</form>
		
				</div>
			</div>
		</div>
			
		<div class="selloff_mod">
			<table id="s_data">

			</table>
		</div>
	</div>
	
	<div id="updateProduct" class="easyui-dialog" style="width:450px;height:370px;padding:20px 20px;">
        <form id="updateProduct_form" method="post">
			<input id="updateProduct_form_id" type="hidden" name="id" value="" >
            <table>
                <tr>
                    <td>商品编码:</td>
                    <td><input id="updateProduct_form_code" name="code" style="width:280px" ></input></td>
                </tr>
                <tr>
                    <td>原名称:</td>
                    <td><input id="updateProduct_form_name" name="name"  style="width:280px" ></input></td>
                </tr>
                <tr>
                    <td>导单名称:</td>
                    <td><input id="updateProduct_form_exportName" name="exportName" style="width:280px" ></input></td>
                </tr>
                <tr>
                    <td>导单售价:</td>
                    <td><input id="updateProduct_form_exportPrice" name="exportPrice" style="width:280px" ></input></td>
                </tr>
                <tr>
                    <td>备注:</td>
                    <td><textarea name="remark" onkeydown="checkEnter(event)" id="remark" style="height: 60px;width: 280px"></textarea></td>
                </tr>
            </table>
        </form>
   </div>
   
</div>
	

<script>

function uploadPro(type) {
    $('#importForm').submit();
}

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

function exportAll(){
	$('#searchForm').submit();
}

function searchProduct(){
	$('#s_data').datagrid('load',{
    	code:$("#code").val(),
    	exportName:$("#exportName").val()
	});
}

function deleteIt(id){
    $.messager.confirm("提示信息","确定删除么？",function(re){
        if(re){
            $.messager.progress();
            $.post("${rc.contextPath}/birdex/deleteProduct",{id:id},function(data){
                $.messager.progress('close');
                if(data.status == 1){
                    $.messager.alert('响应信息',"删除成功...",'info',function(){
                        $('#s_data').datagrid('reload');
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

function editProduct(index){
	$("#sellerIdTr").hide();
	$("#sellerNameTr").show();
    var arr=$("#s_data").datagrid("getData");
    $("#updateProduct_form_id").val(arr.rows[index].id);
	$("#updateProduct_form_code").val(arr.rows[index].code);
	$("#updateProduct_form_name").val(arr.rows[index].name);
	$("#updateProduct_form_exportName").val(arr.rows[index].exportName);
	$("#updateProduct_form_exportPrice").val(arr.rows[index].exportPrice);
	$("#remark").val(arr.rows[index].remark);
    $("#updateProduct").dialog('open');
}

$(function(){
	$('#orderFileBox').filebox({
		buttonText: '打开发货文件',
		buttonAlign: 'right'
	})
	
	$('#s_data') .datagrid({
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'id',
		url : '${rc.contextPath}/birdex/jsonBirdexProductInfo',
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : true,
		pageSize : 50,
		pageList : [ 50, 60 ],
		columns : [ [
			{field : 'statusMsg', title : '状态', width : 20, align : 'center'},
			{field : 'code', title : '商品编码', width : 40, align : 'center'},
			{field : 'name', title : '原名称', width : 90, align : 'center'},
			{field : 'exportName', title : '导单名称', width : 70, align : 'center'},
			{field : 'exportPrice', title : '采购价', width : 40, align : 'center'},
			{field : 'remark', title : '备注', width : 70, align : 'center'},
			{field : 'hidden', title : '操作', width : 80, align : 'center',
				formatter : function(value, row, index) {
					var f1 = '<a href="javaScript:;" onclick="editProduct(' + index + ')">修改</a>';
					var f2 = ' | <a href="javaScript:;" onclick="deleteIt(' + row.id + ')">删除</a>';
            		return f1 + f2;
				}
			} 
		] ],
		toolbar:[{
	        id:'_add',
	        text:'新增',
	        iconCls:'icon-add',
	        handler:function(){
	        	$("#updateProduct_form_id").val("");
            	$("#updateProduct_form_code").val("");
            	$("#updateProduct_form_name").val("");
            	$("#updateProduct_form_exportName").val("");
            	$("#updateProduct_form_exportPrice").val("");
            	$("#remark").val("");
	        	$('#updateProduct').dialog('open');
	        }
        }],
		pagination : true
	});  

	$('#updateProduct').dialog({
        title:'信息',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存信息',
            iconCls:'icon-ok',
            handler:function(){
            	var params = {
            			id:$("#updateProduct_form input[name='id']").val(),
            			code:$("#updateProduct_form input[name='code']").val(),
            			remark:$("#updateProduct_form input[name='remark']").val(),
            			exportName:$("#updateProduct_form input[name='exportName']").val(),
            			exportPrice:$("#updateProduct_form input[name='exportPrice']").val(),
            			name:$("#updateProduct_form input[name='name']").val()
            	};
            	if(params.code == '' || params.remark == '' || params.exportName == '' || params.exportPrice == '' || params.name == ''){
            		$.messager.alert("提示","请填写完整信息","error");
            	}else{
            		$.messager.progress();
                	$.ajax({
                		url:"${rc.contextPath}/birdex/saveProduct",
    	    			type: 'post',
    	    			dataType: 'json',
    	    			data: params,
    	    			success: function(data){
    	    				$.messager.progress('close');
    	    				if(data.status == 1){
    	    					$.messager.alert('响应信息',"保存成功",'info',function(){
                                	$("#updateProduct_form_id").val("");
                                	$("#updateProduct_form_code").val("");
                                	$("#updateProduct_form_name").val("");
                                	$("#updateProduct_form_exportName").val("");
                                	$("#updateProduct_form_exportPrice").val("");
                                	$("#remark").val("");
                                    $('#s_data').datagrid('reload');
                                    $('#updateProduct').dialog('close');
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
            	$('#updateProduct').dialog('close');
            	$("#updateProduct_form_id").val("");
            	$("#updateProduct_form_code").val("");
            	$("#updateProduct_form_name").val("");
            	$("#updateProduct_form_exportName").val("");
            	$("#updateProduct_form_exportPrice").val("");
            	$("#remark").val("");
            }
        }]
    });
	
});
</script>

</body>
</html>