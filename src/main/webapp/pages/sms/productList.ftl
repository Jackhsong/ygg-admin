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
<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div id="searchDiv" class="datagrid-toolbar" style="height: 50px;padding: 15px">
		<span><font size="3" color="red">功能说明：凡购买本页面内商品的订单，不发送以下短信：发货短信、海外购节假日提醒短信。</font></span>
    </div>
    <!--数据表格-->
    <table id="s_data" style=""></table>
    
	<div id="addProductDiv" class="easyui-dialog" style="width:500px;height:190px;padding:20px 20px;">
       <form id="addProductForm" method="post">
	        <table cellpadding="5">
	            <tr>
	                <td>商品ID:</td>
	                <td><input id="addProductForm_Id" name="productId" style="width:300px"  maxlength="44" autocomplete="off"></input></td>
	            </tr>
	            <tr>
	                <td>商品名称:</td>
	                <td>
	                	<span id="addProductForm_productName" style="width:300px"></span>
	                	<input type="hidden" id="productName" value=""/>
	                </td>
	            </tr>
	        </table>
   		</form>
   	</div> 
</div>

<script>
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

	function deleteItem(index){
		var arr=$("#s_data").datagrid("getData");
		var id = arr.rows[index].id;
		$.messager.confirm('删除','确定删除吗？',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
		            url: '${rc.contextPath}/notSendMsgProduct/delete',
		            type: 'post',
		            dataType: 'json',
		            data: {'id':id},
		            success: function(data){
		            	$.messager.progress('close');
		                if(data.status == 1){
		                	$('#s_data').datagrid('load');
		                	$.messager.alert("提示",'删除成功',"info");
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

	$(function(){
		$("#addProductForm_Id").keyup(function(){
			$("#productName").val('');
			var productId = $(this).val();
			if(productId != ''){
				$.ajax({
					type: 'post',
					url: '${rc.contextPath}/notSendMsgProduct/getProductInfo',
					data: {'productId':productId},
					datatype:'json',
					success: function(data){
	                	$("#addProductForm_productName").html(data.msg);
		                if(data.status == 1){
                        	$("#productName").val(data.msg);
		                }
		            },
		            error: function(xhr){
		            	$("#addProductForm_productName").html('');
                    	$("#productName").val('');
		            }
				});	
			}
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/notSendMsgProduct/jsonProductInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'序号', align:'center',checkbox:true},
                {field:'editId',    title:'商品ID', width:20, align:'center'},
                {field:'isAvailable',    title:'使用状态', width:20, align:'center'},
                {field:'isOffShelves',    title:'商品状态', width:20, align:'center'},
                {field:'code',    title:'编码', width:20, align:'center'},
                {field:'pName',     title:'名称',  width:50,  align:'center'},
                {field:'shortName',     title:'短名称',  width:40,   align:'center'},
                {field:'remark',     title:'备注',  width:40,   align:'center'},
                {field:'sell',     title:'累计销量',  width:15,   align:'center'},
                {field:'stock',     title:'剩余库存',  width:15,   align:'center'},
                {field:'lockNum',     title:'锁定库存',  width:15,   align:'center'},
                {field:'salesPrice',     title:'售价',  width:15,   align:'center'},
                {field:'ftName',     title:'运费模板',  width:20,   align:'center'},
                {field:'sellerName',     title:'商家',  width:20,   align:'center' },
                {field:'brandName',     title:'品牌',  width:20,   align:'center' },
                {field:'sendAddress',     title:'发货地',  width:20,   align:'center' },
                {field:'warehouse',     title:'分仓',  width:20,   align:'center' },
                {field:'marketPrice',     title:'市场价',  hidden:true},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                    	return '<a href="javaScript:;" onclick="deleteItem(' + index + ')">删除</a>';
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商品',
                iconCls:'icon-add',
                handler:function(){
                    $("#addProductDiv").dialog("open");
                }
            },'-',{
                id:'_add',
                iconCls: 'icon-remove',
                text:'导出所有商品ID',
                handler:function(){
                	window.location.href="${rc.contextPath}/notSendMsgProduct/exportAllProductIds";
                }
            },'-',{
                    iconCls: 'icon-remove',
                    text:'批量删除',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('删除','确定删除吗',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/notSendMsgProduct/delete",
										{ids: ids.join(",")},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('reload');
											}else{
												$.messager.alert('提示','保存出错',"error");
											}
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error");
                        }
                    }
                }],
            pagination:true
        });
        
        
        $('#addProductDiv').dialog({
            title:'新增',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#addProductForm').form('submit',{
                        url:"${rc.contextPath}/notSendMsgProduct/addProduct",
                        onSubmit:function(){
                        	var productId = $.trim($("#addProductForm_Id").val());
                        	var productName = $.trim($("#productName").val());
                        	if(productId != '' && productName !=''){
                        		return true;
                        	}else{
                        		$.messager.alert('提示','请输入正确的商品Id','info');
                        		return false;
                        	}
                        },
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $('#addProductDiv').dialog('close');
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	$("#addProductForm_Id").val("");
                                	$("#addProductForm_productName").html("");
                                	$("#productName").val("");
                                    $('#s_data').datagrid('reload');
                                    $('#addProductDiv').dialog('close');
                                });
                            } else if(res.status == 0){
                                $.messager.alert('响应信息',res.msg,'info',function(){
                                });
                            } else{
                                $.messager.alert('响应信息',res.msg,'error',function(){
                                });
                            }
                        }
                    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                	$("#addProductForm_Id").val("");
                	$("#addProductForm_productName").html("");
                	$("#productName").val();
                    $('#s_data').datagrid('reload');
                    $('#addProductDiv').dialog('close');
                }
            }]
        });       
	});
</script>

</body>
</html>