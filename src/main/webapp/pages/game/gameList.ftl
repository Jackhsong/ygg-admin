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
textarea{
	resize:none;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:''" style="padding:5px;">

	<div id="cc" class="easyui-layout" data-options="fit:true" >
	
		<div data-options="region:'north',title:'条件筛选-游戏管理',split:true" style="height: 120px;">
			<div style="height: 60px;padding: 10px">
				<span>游戏名称：</span>
				<span><input id="searchGameName" name="searchGameName"/></span>
				<span>游戏状态：</span>
				<span>
					<select id="searchIsAvailable" name="searchIsAvailable">
	          			<option value="-1">全部</option>
	          			<option value="1">可用</option>
	          			<option value="0">停用</option>
	           		</select>
				</span>
				<span>
					<a id="searchBtn" onclick="searchSpread()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
					<a id="clearBtn" onclick="clearSpread()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>&nbsp;
				</span>			
			</div>			
		</div>
		
		<div data-options="region:'center'" >
	    	<!--数据表格-->
	    	<table id="s_data" style=""></table>
    	</div>
    	
		<!-- dialog begin -->
		<div id="addGameDiv" class="easyui-dialog" style="width:600px;height:330px;padding:10px 10px;">
			<form action="" id="gameForm" method="post">
				<input id="gameForm_gameId" type="hidden" name="gameId" value=""/>
				<input id="gameForm_prizeId" type="hidden" name="prizeId" value=""/>
				<p>
					<span>&nbsp;&nbsp;游戏名称：</span>
					<span><input type="text" id="gameForm_gameName" name="gameName" value="" maxlength="20" style="width:300px;"/></span>
					<span>注：用于分享标题(<font color="red">20字以内</font>)</span>
				</p>
				<p>
					<span>&nbsp;&nbsp;游戏Logo：</span>
					<span>
						<input type="text" id="gameForm_gameLogo" name="gameLogo" value="" style="width:300px;"/>
						<a onclick="picDialogOpen('gameForm_gameLogo')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
					</span>
					<span>注：用于分享图标</span>
				</p>
				<p>
					<span>&nbsp;&nbsp;游戏简介：</span>
					<span><textarea id="gameForm_introduce" name="introduce" cols="40" rows="3"></textarea></span>
					<span>注：用于分享内容</span>
				</p>
				<div id="canNotEditDiv">
				<p>
					<span>&nbsp;&nbsp;优惠券Id：</span>
					<span><input type="text" id="gameForm_couponId" name="couponId" value="" style="width:300px;" onblur="stringTrim(this)"/></span>&nbsp;
					<span id="couponInfo" style="color:red"></span>
				</p>
				<p>
					<span>优惠券有效期：</span>
					<span>
						<input type="radio" name="dateType" id="gameForm_dateType1" value="1"/>从原有优惠券中读取信息&nbsp;&nbsp;
		                <input type="radio" name="dateType" id="gameForm_dateType2" value="2"/>从领券之日起
		                <input type="text"  name="days" id="gameForm_days" onblur="stringTrim(this)" style="width:30px;"/>天内
					</span>
				</p>
				</div>
			</form>
		</div>
		<!-- dialog end -->


		<!-- dialog begin -->
		<div id="editGameDiv" class="easyui-dialog" style="width:600px;height:300px;padding:10px 10px;">
			<form action="" id="editGameForm" method="post">
				<input id="editGameForm_gameId" type="hidden" name="gameId" value=""/>
				<p>
					<span>&nbsp;&nbsp;游戏名称：</span>
					<span><input type="text" id="editGameForm_gameName" name="gameName" value="" maxlength="20" style="width:300px;"/></span>
					<span>注：用于分享标题(<font color="red">20字以内</font>)</span>
				</p>
				<p>
					<span>&nbsp;&nbsp;游戏Logo：</span>
					<span>
						<input type="text" id="editGameForm_gameLogo" name="gameLogo" value="" style="width:300px;"/>
						<a onclick="picDialogOpen('editGameForm_gameLogo')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
					</span>
					<span>注：用于分享图标</span>
				</p>
				<p>
					<span>&nbsp;&nbsp;游戏简介：</span>
					<span><textarea id="editGameForm_introduce" name="introduce" cols="40" rows="3"></textarea></span>
					<span>注：用于分享内容</span>
				</p>
			</form>
		</div>
		<!-- dialog end -->
		
				
		<!-- 编辑短信内容begin -->
        <div id="editMsgContentDiv" class="easyui-dialog" style="width:400px;height:250px;padding:20px 20px;">
        	<input type="hidden" id="gameId" value=""/>
            <table cellpadding="5">
                <tr>
                    <td>短信内容：<font color="red">(字数 &lt; 200)</font></td>
                </tr>
                <tr>
                	<td>
                    	<textarea rows="5" cols="45" id="msgContent" onkeydown="checkEnter(event)"></textarea>
                    </td>
                </tr>
            </table>
        </div>
        <!-- 编辑短信内容end -->
		
		
	</div>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
</div>

<script type="text/javascript">
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

function stringTrim(obj){
	var value = $(obj).val();
	$(obj).val($.trim(value));
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

function searchSpread(){
	$("#s_data").datagrid('load',{
		gameName:$("#searchGameName").val(),
		isAvailable:$("#searchIsAvailable").val()
	});
}

function clearSpread(){
	$("#searchGameName").val('');
	$("#searchIsAvailable").find('option').eq(0).attr('selected','selected');
	$("#s_data").datagrid('load',{});
}


function cleanAddGameDiv(){
	$("#gameForm_gameId").val('');
	$("#gameForm_prizeId").val('');
	$("#gameForm input[type='text']").each(function(){
		$(this).val('');
	});
	$("#gameForm_introduce").val('');
	$("#gameForm radio[name='dateType']").each(function(){
		$(this).prop('checked',true);
	});
	$("#couponInfo").text('');
	$("#gameForm_canUpdateDateType").val('');
	$("#canNotEditDiv").show();
}


function cleanEditGameDiv(){
	$("#editGameForm_gameId").val('');
	$("#editGameForm input[type='text']").each(function(){
		$(this).val('');
	});
	$("#editGameForm_introduce").val('');
}

function editIt(index){
	cleanEditGameDiv();
    var arr=$("#s_data").datagrid("getData");
    $("#editGameForm_gameId").val(arr.rows[index].id);
    $("#editGameForm_gameName").val(arr.rows[index].gameName);
    $("#editGameForm_gameLogo").val(arr.rows[index].gameLogo); 
    $("#editGameForm_introduce").val(arr.rows[index].introduce);
    $("#editGameDiv").dialog('open');
}

function updateAvailable(id, isAvailable){
	var tip = "";
	if(isAvailable == 0){
		tip = "确定停用吗？";
	}else{
		tip = "确定启用吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/game/updateGameAvailable",
	            		{id:id,isAvailable:isAvailable},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('load');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	           });
        }
    });
}

function updateSendMsg(index, status){
	var arr=$("#s_data").datagrid("getData");
	var msgContent = arr.rows[index].msgContent;
	var id = arr.rows[index].id;
	var tip = "";
	if(status == 0){
		tip = "确定关闭短信提醒吗？";
	}else{
		tip = "确定开启短信提醒吗？";
		if($.trim(msgContent)==''){
			$.messager.alert('提示',"短信内容未设置，不能开启短息提醒",'error');
			return false;
		}
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/game/updateGameSendMsg",
	            		{id:id,status:status},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('load');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	           });
        }
    });
}


function deleteId(id){
	$.messager.confirm("提示信息","确定删除吗？",function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/game/deleteGame",
	            		{id:id},
	            		function(data){
	   	                $.messager.progress('close');
	   	                if(data.status == 1){
	   	                    $.messager.alert('响应信息',"操作成功",'info',function(){
	   	                        $('#s_data').datagrid('load');
	   	                        return
	   	                    });
	   	                } else{
	   	                    $.messager.alert('响应信息',data.msg,'error');
	   	                }
	           });
        }
    });
}

function editMsgContent(index){
	$("#gameId").val('');
	$("#msgContent").val('');
	var arr=$("#s_data").datagrid("getData");
	var msgContent = arr.rows[index].msgContent;
	var id = arr.rows[index].id;
	$("#gameId").val(id);
	$("#msgContent").val(msgContent);
	$("#editMsgContentDiv").dialog('open');
}

$(function(){
	
	$("#gameForm_couponId").change(function(){
		var couponId = $.trim($(this).val());
		if(couponId != ''){
            $.post(
  	           	"${rc.contextPath}/coupon/getCouponInfoById",
  	            {id:couponId},
  	            function(data){
	   	          	$("#couponInfo").text(data.couponInfo);
  	            }
  	        );
		}
	});
	
    $('#s_data').datagrid({
    	nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/game/jsonGameInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns: [
            [
                {field: 'id', title: 'ID', width: 15, align: 'center'},
                {field: 'gameName', title: '游戏名称', width: 30, align: 'center'},
                {field: 'gameLogoImage', title: '游戏logo', width: 40, align: 'center'},
                {field: 'introduce', title: '游戏简介', width: 50, align: 'center'},
                {field: 'gameURL', title: '游戏链接', width: 80, align: 'center'},
                {field: 'coupon', title: '优惠券', width: 30, align: 'center'},
                {field: 'validityDate', title: '优惠券有效期', width: 40, align: 'center'},
                {field: 'receiveAmount', title: '领取人数', width: 20, align: 'center'},
                {field: 'totalMoney', title: '订单交易额', width: 20, align: 'center'},
                {field: 'registerAmount', title: '注册新用户', width: 20, align: 'center'},
                {field: 'newBuyerAmount', title: '新用户交易额', width: 25, align: 'center'},
                {field: 'msgContent', title: '短信提醒内容', width: 40, align: 'center'},
                {field: 'msgStatus', title: '短信提醒状态', width: 25, align: 'center'},
                {field: 'hidden', title: '操作', width: 60, align: 'center',
                    formatter: function (value, row, index) {
                    	var a = '<a href="javaScript:;" onclick="editMsgContent(' + index + ')">编辑短信内容</a> | ';
                    	var b = ''
                       	if(row.isSendMsg == 1){
                       		b = '<a href="javaScript:;" onclick="updateSendMsg(' + index + ',' + 0 + ')">关闭短信提醒</a> | ';
                       	}else{
                       		b = '<a href="javaScript:;" onclick="updateSendMsg(' + index + ',' + 1 + ')">开启短信提醒</a> | ';
                       	}
                    	var c = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                    	var d = ''
                    	if(row.isAvailableCode == 1){
                    		d = '<a href="javaScript:;" onclick="updateAvailable(' + row.id + ',' + 0 + ')">停用</a>';
                    	}else{
                    		d = '<a href="javaScript:;" onclick="updateAvailable(' + row.id + ',' + 1 + ')">启用</a>';
                    	}
                    	return a + b + c + d;
                    }
                }
            ]
        ],
        toolbar: [
            {
                id: '_add',
                iconCls:'icon-add',
                text: '新增游戏',
                handler: function () {
                	cleanAddGameDiv();
                	$('#addGameDiv').dialog('open');
                }
            }
        ],
        pagination:true
    });

    $('#addGameDiv').dialog({
    	title:'游戏信息',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#gameForm').form('submit',{
    				url: "${rc.contextPath}/game/saveGame",
    				onSubmit:function(){
    					var gameName = $('#gameForm_gameName').val();
    					var gameLogo = $('#gameForm_gameLogo').val();
    					var introduce = $('#gameForm_introduce').val();
    					var couponId = $('#gameForm_couponId').val();
    					var dateType = $("input[name='dateType']:checked").val();
    					var days = $("#gameForm_days").val();
    					if($.trim(gameName)==''){
    						$.messager.alert("info","请输入游戏名称","warn");
    						return false;
    					}else if($.trim(gameLogo)==''){
    						$.messager.alert("info","请上传游戏logo","warn");
    						return false;
    					}else if($.trim(introduce)==''){
    						$.messager.alert("info","请填写游戏简介","warn");
    						return false;
    					}else if(introduce.length > 200){
    						$.messager.alert("info","游戏简介字数请控制在200以内","warn");
    						return false;
    					}else if(couponId == '' || couponId==null || couponId==undefined){
      						$.messager.alert("info","请选择优惠券","warn");
      						return false;
      					}else if(dateType == '' || dateType==null || dateType==undefined){
  							$.messager.alert("提示","请选择优惠券有效期","warn");
  							return false;
      					}else if(dateType == 2 && (!/^[1-9]\d*$/.test(days))){
  							$.messager.alert("提示","请输入有效期","warn");
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
                                $('#addGameDiv').dialog('close');
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
                $('#addGameDiv').dialog('close');
            }
    	}]
    });
    
    
    $('#editGameDiv').dialog({
    	title:'游戏信息',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#editGameForm').form('submit',{
    				url: "${rc.contextPath}/game/updateGame",
    				onSubmit:function(){
    					var gameName = $('#editGameForm_gameName').val();
    					var gameLogo = $('#editGameForm_gameLogo').val();
    					var introduce = $('#editGameForm_introduce').val();
    					if($.trim(gameName)==''){
    						$.messager.alert("info","请输入游戏名称","warn");
    						return false;
    					}else if($.trim(gameLogo)==''){
    						$.messager.alert("info","请上传游戏logo","warn");
    						return false;
    					}else if($.trim(introduce)==''){
    						$.messager.alert("info","请填写游戏简介","warn");
    						return false;
    					}else if(introduce.length > 200){
    						$.messager.alert("info","游戏简介字数请控制在200以内","warn");
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
                                $('#editGameDiv').dialog('close');
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
                $('#editGameDiv').dialog('close');
            }
    	}]
    });   
    
    
	$('#editMsgContentDiv').dialog({
        title:'编辑短信内容',
        collapsible:true,
        closed:true,
        modal:true,
        buttons:[{
            text:'保存信息',
            iconCls:'icon-ok',
            handler:function(){
            	var msgContent = $("#msgContent").val();
            	var gameId = $("#gameId").val();
            	if($.trim(msgContent) == ''){
            		$.messager.alert("提示",'请输短信内容',"warning");
            	}else if(msgContent.length >= 200){
            		$.messager.alert("提示",'短信内容字数超过限制',"warning");
            	}else{
           			$.messager.progress();
       				$.ajax({
       		            url: '${rc.contextPath}/game/editMsgContent',
       		            type: 'post',
       		            dataType: 'json',
       		            data: {'msgContent':msgContent,'gameId':gameId},
       		            success: function(data){
       		            	$.messager.progress('close');
       		                if(data.status == 1){
       		                	$('#editMsgContentDiv').dialog('close');
       		                	$('#s_data').datagrid('load');
       		                	$.messager.alert("提示","保存成功","");
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
                $('#editMsgContentDiv').dialog('close');
            }
        }]
    });
    
    
    $('#picDia').dialog({
        title:'又拍图片上传窗口',
        collapsible:true,
        closed:true,
        modal:true
    });
});

var inputId;
function picDialogOpen($inputId) {
    inputId = $inputId;
    $("#picDia").dialog("open");
    $("#yun_div").css('display','none');
}
function picDialogClose() {
   $("#picDia").dialog("close");
}
function picUpload() {
    $('#picForm').form('submit',{
        url:"${rc.contextPath}/pic/fileUpLoad",
        success:function(data){
            var res = eval("("+data+")")
            if(res.status == 1){
                $.messager.alert('响应信息',"上传成功...",'info',function(){
                    $("#picDia").dialog("close");
                    if(inputId) {
                        $("#"+inputId).val(res.url);
                        $("#picFile").val("");
                    }
                    return
                });
            } else{
                $.messager.alert('响应信息',res.msg,'error',function(){
                    return ;
                });
            }
        }
    })
}
</script>

</body>
</html>