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
<style type="text/css">
textarea{
	resize:none;
}
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}	
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'商品评论管理',split:true" style="height: 150px;">
			<br>
			<form id="searchForm" action="${rc.contextPath}/comment/export" method="post">
				<table class="search">
					<tr>
						<td class="searchName">用户ID：</td>
						<td class="searchText"><input id="searchAccountId" name="searchAccountId" value="" /></td>
						<td class="searchName">用户名：</td>
						<td class="searchText"><input id="searchUsername" name="searchUsername" value="" /></td>
						<td class="searchName">订单号：</td>
						<td class="searchText"><input id="searchOrderNo" name="searchOrderNo" value="" /></td>
						<td class="searchName">展现状态：</td>
						<td class="searchText">
							<select id="searchIsDisplay" name="searchIsDisplay" style="width:173px;">
								<option value="-1">全部</option>
								<option value="1">展现</option>
								<option value="0">不展现</option>
							</select>
						</td>
					</tr>
                    <tr>
                    	<td class="searchName">基本商品ID：</td>
						<td class="searchText"><input id="searchBaseId" name="searchBaseId" value="" /></td>
                        <td class="searchName">总体印象：</td>
                        <td class="searchText">
                            <select id="searchLevel" name="searchLevel" style="width:173px;">
                                <option value="0">全部</option>
                                <option value="1">差评</option>
                                <option value="2">中评</option>
                                <option value="3">好评</option>
                            </select>
                        </td>
                        <td class="searchName">商品评价：</td>
                        <td class="searchText"><input id="searchComment" name="searchComment" value="" /></td>
                        <td class="searchName">上传图片：</td>
                        <td class="searchText">
                        	<select id="searchIsHasImage" name="searchIsHasImage" style="width:173px;">
                        		<option value="-1">全部</option>
                                <option value="1">是</option>
                                <option value="0">否</option>
                        	</select>
                        </td>
					</tr>
					<tr>
						<td class="searchName">商家：</td>
                        <td class="searchText"><input id="searchSellerId" type="text" name="searchSellerId" /></td>
						<td class="searchName">品牌：</td>
                        <td class="searchText"><input id="searchBrandId" type="text" name="searchBrandId" /></td>
                        <td class="searchName">商品名称：</td>
                        <td class="searchText"><input id="searchProductName" name="searchProductName"/></td>
                        <td class="searchName"></td>
                        <td class="searchText">
                        	<a id="searchBtn" onclick="searchComment()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a>&nbsp;
                        	<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" >重置</a>&nbsp;
                        	<!-- <a id="searchBtn" onclick="exportData()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >导出结果</a> -->
                        </td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    		
    		<!-- 换吧网络回复begin -->
			<div id="replayCommentDiv" class="easyui-dialog" style="width:500px;height:230px;padding:20px 20px;">
	            <form id="replayCommentForm" method="post">
				<input id="replayCommentForm_id" type="hidden" name="id" value="" >
	            <table cellpadding="5">
	                <tr>
	                    <td>&nbsp;&nbsp;回复内容：</td>
	                    <td><textarea id="replayCommentForm_reply" name="reply" onkeydown="checkEnter(event)" style="height: 60px;width: 300px"></textarea></td>
	                </tr>
	                <tr>
	                    <td>评价展现状态：</td>
	                    <td>
	                    	<input type="radio" name="isDisplay" id="replayCommentForm_isDisplay1" value="1"/>展现&nbsp;&nbsp;
	                    	<input type="radio" name="isDisplay" id="replayCommentForm_isDisplay0" value="0"/>不展现&nbsp;&nbsp;
	                    	<input type="radio" name="isDisplay" id="replayCommentForm_isDisplay2" value="2"/>只展现文本
	                    </td>
	                </tr>
	            </table>
	        	</form>
	        </div>	
    		<!-- 换吧网络回复end -->

            <!-- 处理begin -->
            <div id="updateDealContentDiv" class="easyui-dialog" style="width:400px;height:250px;padding:10px 10px;">
                <form action="" id="dealContentForm" method="post">
                    <input id="dealContentForm_id" type="hidden" name="id" value=""/>
                    <p>
                        <span>处理说明：</span>
                        <span><textarea id="dealContentForm_content" name="dealContent" rows="5" cols="30" onkeydown="checkEnter(event)"></textarea></span>
                    </p>
                </form>
            </div>
            <!-- 处理end -->
    		
		</div>
        
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

function updateDealContent(index){
    $("#dealContentForm_id").val('');
    $("#dealContentForm_content").val('');
    var arr=$("#s_data").datagrid("getData");
    $("#dealContentForm_id").val(arr.rows[index].id);
    $("#dealContentForm_content").val(arr.rows[index].dealContent);
    $("#updateDealContentDiv").dialog('open');
}

function exportData(){
    $('#searchForm').submit();
}

function clearReplayCommentForm(){
	$('#replayCommentForm_id').val('');
	$('#replayCommentForm_reply').val('');
	$("#replayCommentForm input[name='isDisplay']").each(function(){
			$(this).removeAttr("disabled");
	});
	$('#replayCommentForm_isDisplay1').prop('checked',true);
}

function replyIt(index){

	clearReplayCommentForm();

	var arr=$("#s_data").datagrid("getData");
	var id = arr.rows[index].id;
	
	var content = arr.rows[index].content;
	var image = arr.rows[index].image;
	//判断只展现文本 radio是否可用
	if(($.trim(content) == '')||(image =='无')){
    	$("#replayCommentForm input[name='isDisplay']").eq(2).attr("disabled","disabled");
	}
	
	//判断只展现文本 radio是否选中
	var isDisplay = arr.rows[index].isDisplay;
	var isDisplayImage = arr.rows[index].isDisplayImage;
	if(($.trim(content) != '')||(image !='无')){
		if(isDisplay==1&&isDisplayImage==0){
			isDisplay = 2;
		}
	}
	
	var reply = arr.rows[index].reply;
	$('#replayCommentForm_id').val(id);
	$('#replayCommentForm_reply').val(reply);
	$("#replayCommentForm input[name='isDisplay']").each(function(){
		if($(this).val()==isDisplay){
			$(this).prop('checked',true);
		}
	});
	$('#replayCommentDiv').dialog('open');
}

function searchComment(){
    
	$('#s_data').datagrid('load', {
		productBaseId:$("#searchBaseId").val(),
		accountId : $("#searchForm input[name='searchAccountId']").val(),
		username : $("#searchForm input[name='searchUsername']").val(),
		orderNo : $("#searchForm input[name='searchOrderNo']").val(),
		comment : $("#searchForm input[name='searchComment']").val(),
		isDisplay : $("#searchIsDisplay").val(),
		level : $("#searchLevel").val(),
        isHasImage:$("#searchIsHasImage").val(),
        sellerId:$("#searchSellerId").combobox('getValue'),
        brandId:$("#searchBrandId").combobox('getValue'),
        productName :$("#searchProductName").val()
	});
}

function clearSearch(){
	$("#searchForm input[name='searchBaseId']").val('');
	$("#searchForm input[name='searchAccountId']").val('');
	$("#searchForm input[name='searchUsername']").val('');
	$("#searchForm input[name='searchComment']").val('');
	$("#searchForm input[name='searchOrderNo']").val('');
	$("#searchLevel").find('option').eq(0).attr('selected','selected');
	$("#searchIsDisplay").find('option').eq(0).attr('selected','selected');
	$("#searchIsHasImage").find('option').eq(0).attr('selected','selected');
	$("#searchSellerId").combobox('clear');
    $("#searchBrandId").combobox('clear');
	$('#s_data').datagrid('reload',{});
}

function displayIt(id,isDisplay){
	var tip = "";
	if(isDisplay == 0){
		tip = "确定不展现吗？";
	}else{
		tip = "确定展现吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/comment/updateProductCommentDisplayStatus",
	            		{ids:id,isDisplay:isDisplay},
	            		function(data){
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('reload');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	            });
	            $.messager.progress('close');
        }
    });
}

function displayTextPic(id,isDisplayImage){
	var isDisplay = 1;
	var tip = "";
	if(isDisplayImage == 0){
		tip = "确定只展现文本吗？";
	}else{
		tip = "确定展现文本和图片吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/comment/updateProductCommentDisplayTextStatus",
	            		{ids:id,isDisplay:isDisplay,isDisplayImage:isDisplayImage},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('reload');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	            });
       		}
    });
}



$(function(){
    $('#searchBrandId').combobox({
        panelWidth:350,
        panelHeight:350,
        url:'${rc.contextPath}/brand/jsonBrandCode',
        valueField:'code',
        textField:'text'
    });

    $('#searchSellerId').combobox({
        panelWidth:350,
        panelHeight:350,
        mode:'remote',
        url:'${rc.contextPath}/seller/jsonSellerCode',
        valueField:'code',
        textField:'text'
    });
	
	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/comment/jsonProductCommentDetailList',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        pageSize:30,
        columns:[[
            /* {field:'orderNo',    title:'订单号', width:20, align:'center'}, */
            {field:'accountId',    title:'用户Id', width:15, align:'center',checkbox:true},
            {field:'username',     title:'用户名',  width:15,   align:'center' },
            {field:'typeDesc',    title:'商品类型', width:10, align:'center'},
            {field:'productId',    title:'商品Id', width:20, align:'center'},
            {field:'productNameUrl',    title:'商品名称', width:80, align:'center'},
            /* {field:'realSellerName',    title:'商家', width:50, align:'center'},
            {field:'productAmount',    title:'购买数量', width:20, align:'center'}, */
            {field:'levelDesc',    title:'总体印象', width:10, align:'center'},
            {field:'createTime',    title:'评论时间', width:30, align:'center'},
            {field:'content',    title:'商品评论', width:65, align:'center'},
            {field:'image',    title:'上传图片', width:35, align:'center',
            	formatter : function(value, row, index) {
            		if(row.hasImage==1){
						return row.image+'&nbsp;<a target="_blank" href="${rc.contextPath}/comment/viewCommentImage/'+row.id+'">查看</a>';
            		}else{
            			return row.image;
            		}
				}
            },
            {field:'reply',    title:'换吧网络回复', width:60, align:'center'},
            {field: 'isDealDesc', title: '是否处理', width: 15, align: 'center'},
            {field:'handleTime',    title:'处理时间', width:30, align:'center'},
            {field: 'dealUser', title: '处理人', width: 15, align: 'center'},
            {field: 'dealContent', title: '处理说明', width: 60, align: 'center'},
            {field:'hidden',  title:'操作', width:45,align:'center',
                formatter:function(value,row,index){
                    var gg = '';
					if(row.isDeal == 1){
                        gg = '<a href="javaScript:;" onclick="updateDealContent(' + index + ')">更新处理</a> | '
                    }else{
                        gg = '<a href="javaScript:;" onclick="updateDealContent(' + index + ')">处理反馈</a> | '
                    }
                	var a = '';
                	if(row.isDisplay == 1){
                		a = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 0 +')">不展现</a> | ';
                	}else{
                		a = '<a href="javaScript:;" onclick="displayIt(' + row.id + ',' + 1 +')">展现</a> | ';
                	}
                	var b = '<br/><a href="javaScript:;" onclick="replyIt(' + index + ')">换吧网络回复</a> | ';
                	var c = '<a target="_blank" href="${rc.contextPath}/order/detail/'+row.orderId+'">订单明细</a> | ';
                	var d = '';
                	if(($.trim(row.content) != '')&&(row.image!='无')){
	                	d = '<br/><a href="javaScript:;" onclick="displayTextPic(' + row.id + ',' + 0 +')">只展现文本</a>';
	                	if(row.isDisplayImage == 0){
	                		d = '<br/><a href="javaScript:;" onclick="displayTextPic(' + row.id + ',' + 1 +')">展现文本和图片</a>';
                		}
                	}
                	return gg + a + b + c + d;
                }
            }
        ]],
        toolbar:[{
                    iconCls: 'icon-remove',
                    text:'批量不展现',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('停用','全部不展现',function(b){
                                if(b){
                                  $.messager.progress();
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/comment/updateProductCommentDisplayStatus", //全部不展现
										{ids: ids.join(","),isDisplay:0},
										function(data){
											if(data.status == 1){
												$.messager.alert('响应信息',"操作成功",'info',function(){
							   	                        $('#s_data').datagrid('reload');
							   	                        return
							   	                 });
											}else{
												$.messager.alert('提示',data.msg,"error")
											}
										},
									"json");
								$.messager.progress('close');
                                }
                     		})
                        }else{
                        	$.messager.progress('close');
                            $.messager.alert('提示','请选择要操作的评论',"error")
                        }
                    }
                },'-',{
                    iconCls: 'icon-edit',
                    text:'批量只展现文本',
                    handler: function(){
                    	var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('停用','全部不展现文本',function(b){
                                $.messager.progress();
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/comment/updateProductCommentDisplayTextStatus",
											{ids: ids.join(","),isDisplay:1,isDisplayImage:0},
											function(data){
											    $.messager.progress('close');
												if(data.status == 1){
													$.messager.alert('响应信息',"操作成功",'info',function(){
							   	                        $('#s_data').datagrid('reload');
							   	                        return
							   	                    });
												}else{
													$.messager.alert('提示',data.msg,"error")
												}
											},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的评论',"error")
                        }
                    }
                }],
        pagination:true,
        onLoadSuccess:function(){
            	$("#s_data").datagrid('clearSelections');
            }
    });
	
	
	$('#replayCommentDiv').dialog({
        title:'换吧网络回复',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存信息',
            iconCls:'icon-ok',
            handler:function(){
                $('#replayCommentForm').form('submit',{
                    url:"${rc.contextPath}/comment/replayProductComment",
                    onSubmit: function(){ 
                        var reply = $("#replayCommentForm_reply").val();
                        if($.trim(reply) == ''){
                        	$.messager.alert('提示',"请输入回复内容",'error');
                        	return false;
                        }else if(reply.length > 200){
                        	$.messager.alert('提示',"回复内容字数不得超过200",'error');
                        	return false;
                        }
                    }, 
                    success:function(data){
                        var res = eval("("+data+")")
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                            	$('#s_data').datagrid('reload');
                                $('#replayCommentDiv').dialog('close');
                            });
                        } else{
                            $.messager.alert('响应信息',res.msg,'error');
                        }
                    }
                })
            }
        },{
            text:'取消',
            align:'left',
            iconCls:'icon-cancel',
            handler:function(){
                $('#replayCommentDiv').dialog('close');
            }
        }]
    });

    $('#updateDealContentDiv').dialog({
        title:"反馈处理",
        collapsible: true,
        closed: true,
        modal: true,
        buttons: [
            {
                text: '保存',
                iconCls: 'icon-ok',
                handler: function(){
                    $('#dealContentForm').form('submit',{
                        url: "${rc.contextPath}/comment/updateDealContent",
                        onSubmit:function(){
                            var content = $('#dealContentForm_content').val();
                            if($.trim(content)=='' || content.length>200){
                                $.messager.alert("提示","处理说明不能为空且字数必须小于200","warning");
                                return false;
                            }
                            $.messager.progress();
                        },
                        success: function(data){
                            $.messager.progress('close');
                            var res = eval("("+data+")");
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                    $('#s_data').datagrid('reload');
                                    $('#updateDealContentDiv').dialog('close');
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
                    $('#updateDealContentDiv').dialog('close');
                }
            }]
    });

});
</script>

</body>
</html>