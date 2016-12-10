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
				<span><input id="searchGateName" name="searchGateName"/></span>
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
		<div id="addGateDiv" class="easyui-dialog" style="width:650px;height:380px;padding:10px 10px;">
			<form action="" id="gateForm" method="post">
				<input id="gateForm_gateId" type="hidden" name="id" value=""/>
				<p>
					<span>&nbsp;&nbsp;今日主题：</span>
					<span><input type="text" id="gateForm_theme" name="theme" value="" maxlength="20" style="width:300px;"/></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>&nbsp;&nbsp;口令答案：</span>
					<span>
						<input type="text" id="gateForm_answer" name="answer" value="" maxlength="50" style="width:300px;"/>
					</span>
					<font color="red">*</font>
				</p>
				<p>
					<span>&nbsp;&nbsp;口令描述：</span>
					<span><textarea id="gateForm_desc" name="desc" cols="40" rows="3"></textarea></span>
					<font color="red">*</font>
				</p>
				<p>
					<span>&nbsp;&nbsp;有效时间：</span>
					<span><input type="text" id="gateForm_validTimeStart" name="validTimeStart" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'gateForm_validTimeEnd\')}'})"/></span>
					到
					<span><input type="text" id="gateForm_validTimeEnd" name="validTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'gateForm_validTimeStart\')}'})"/></span>
					<font color="red">*</font>
				</p>
				<div id="canNotEditDiv">
				<p>
					<span>&nbsp;&nbsp;优惠券Id：</span>
					<span><input type="text" id="gateForm_couponId" name="couponId" value="" style="width:300px;" onblur="stringTrim(this)"/></span>&nbsp;
					<font color="red">*</font>
					<span id="couponInfo" style="color:red"></span>
				</p>
				<p>
					<span>优惠券有效期：</span>
					<span>
						<input type="radio" name="validTimeType" id="gateForm_dateType1" value="1"/>从原有优惠券中读取信息&nbsp;&nbsp;
		                <input type="radio" name="validTimeType" id="gateForm_dateType2" value="2"/>从领券之日起
		                <input type="text"  name="days" id="gateForm_days" onblur="stringTrim(this)" style="width:30px;"/>天内
					</span>
				</p>
				</div>
				<p>
					<span>&nbsp;&nbsp;是否展现：</span>
					<span>
						<input type="radio" name="isDisplay" id="gateForm_isDisplay1" value="1"/>展现&nbsp;&nbsp;
		                <input type="radio" name="isDisplay" id="gateForm_isDisplay0" value="0"/>不展现
					</span>
				</p>
			</form>
		</div>
		<!-- dialog end -->
		
	</div>
	
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
		gateName:$("#searchGateName").val(),
		isAvailable:$("#searchIsAvailable").val()
	});
}

function clearSpread(){
	$("#searchGateName").val('');
	$("#searchIsAvailable").find('option').eq(0).attr('selected','selected');
	$("#s_data").datagrid('load',{});
}


function cleanAddGateDiv(){
	$("#gateForm_gateId").val('');
	$("#gateForm input[type='text']").each(function(){
		$(this).val('');
	});
	$("#gateForm_desc").val('');
	$("input[name='validTimeType']").each(function(){
		if($(this).val()==1){
			$(this).prop('checked',true);
		}else{
			$(this).prop('checked',false);
		}
	});
	$("input[name='isDisplay']").each(function(){
		if($(this).val()==1){
			$(this).prop('checked',true);
		}else{
			$(this).prop('checked',false);
		}
	});
	$("#couponInfo").text('');
	$("#canNotEditDiv").show();
	$("#gateForm_answer").attr("readonly",false);
}

function editIt(index){
	cleanAddGateDiv();
    var arr=$("#s_data").datagrid("getData");
    $("#gateForm_gateId").val(arr.rows[index].id);
    $("#gateForm_theme").val(arr.rows[index].theme);
    $("#gateForm_answer").val(arr.rows[index].answer); 
    // 编辑时不允许修改答案
    $("#gateForm_answer").attr("readonly",true);
    $('#gateForm_desc').val(arr.rows[index].desc.replace(/<br\/>/g,"\n"));
    $("#gateForm_validTimeStart").val(arr.rows[index].validTimeStart);
    $("#gateForm_validTimeEnd").val(arr.rows[index].validTimeEnd);
    var isDisplayCode = arr.rows[index].isDisplayCode;
    $("input[name='isDisplay']").each(function(){
		if($(this).val()==isDisplayCode){
			$(this).prop('checked',true);
		}
	});
    $("#canNotEditDiv").hide();
    $("#addGateDiv").dialog('open');
}

function updateDisplay(id, isDisplay){
	var tip = "";
	if(isDisplay == 0){
		tip = "确定展现吗？";
	}else{
		tip = "确定不展现吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/gate/updateGateDisplay",
	            		{id:id,isDisplay:isDisplay},
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
	            		"${rc.contextPath}/gate/deleteGate",
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


$(function(){
	
	$("#gateForm_couponId").change(function(){
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
        url:'${rc.contextPath}/gate/jsonGateInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        columns: [
            [
                {field: 'id', title: 'ID', width: 15, align: 'center'},
                {field: 'couponId', title: '优惠券ID', width: 15, align: 'center'},
                {field: 'theme', title: '今日主题', width: 40, align: 'center'},
                {field: 'answer', title: '口令答案', width: 20, align: 'center'},
                {field: 'desc', title: '口令描述', width: 70, align: 'center'},
                {field: 'validTimeStart', title: '有效期起', width: 30, align: 'center'},
                {field: 'validTimeEnd', title: '有效期止', width: 30, align: 'center'},
                {field: 'coupon', title: '优惠券', width: 30, align: 'center'},
                {field: 'validTimeType', title: '优惠券有效期', width: 40, align: 'center'},
                {field: 'receiveAmount', title: '领取人数', width: 20, align: 'center'},
                {field: 'totalMoney', title: '订单交易额', width: 20, align: 'center'},
                {field: 'registerAmount', title: '注册新用户', width: 20, align: 'center'},
                {field: 'newBuyerAmount', title: '新用户交易额', width: 25, align: 'center'},
                {field: 'isDisplay', title: '是否展现', width: 20, align: 'center'},
                {field: 'hidden', title: '操作', width: 40, align: 'center',
                    formatter: function (value, row, index) {
                    	var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                    	var b = ''
                    	if(row.isDisplayCode == 0){
                    		b = '<a href="javaScript:;" onclick="updateDisplay(' + row.id + ',' + 1 + ')">展现</a>';
                    	}else{
                    		b = '<a href="javaScript:;" onclick="updateDisplay(' + row.id + ',' + 0 + ')">不展现</a>';
                    	}
                    	var c = '';
                    	if(row.receiveAmount == 0){
                    		c = ' | <a href="javaScript:;" onclick="deleteId(' + row.id + ')">删除</a>'
                    	}
                    	return a + b + c;
                    }
                }
            ]
        ],
        toolbar: [
            {
                id: '_add',
                iconCls:'icon-add',
                text: '新增',
                handler: function () {
                	cleanAddGateDiv();
                	$('#addGateDiv').dialog('open');
                }
            }
        ],
        pagination:true
    });

    $('#addGateDiv').dialog({
    	title:"任意门信息(带<font color='red'>*</font>为必填项)",
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#gateForm').form('submit',{
    				url: "${rc.contextPath}/gate/saveOrUpdateGate",
    				onSubmit:function(){
    					var id = $('#gateForm_gateId').val();
    					var theme = $('#gateForm_theme').val();
    					var answer = $('#gateForm_answer').val();
    					var desc = $('#gateForm_desc').val();
    					var validTimeStart = $('#gateForm_validTimeStart').val();
    					var validTimeEnd = $('#gateForm_validTimeEnd').val();
    					var couponId = $('#gateForm_couponId').val();
    					var dateType = $("input[name='validTimeType']:checked").val();
    					var days = $("#gateForm_days").val();
    					if($.trim(theme)==''){
    						$.messager.alert("提示","请输入今日主题","warning");
    						return false;
    					}else if($.trim(answer)==''){
    						$.messager.alert("提示","请输入口令答案","warning");
    						return false;
    					}else if($.trim(desc)==''){
    						$.messager.alert("提示","请输入口令描述","warning");
    						return false;
    					}else if(desc.length > 300){
    						$.messager.alert("提示","口令描述字数请控制在300以内","warning");
    						return false;
    					}else if($.trim(validTimeStart) == '' || $.trim(validTimeEnd) == ''){
    						$.messager.alert("提示","请选择有效时间","warning");
    						return false;
    					}else if(id == ''){
    						if(couponId == '' || couponId==null || couponId==undefined){
          						$.messager.alert("提示","请选择优惠券","warning");
          						return false;
          					}else if(dateType == '' || dateType==null || dateType==undefined){
      							$.messager.alert("提示","请选择优惠券有效期","warning");
      							return false;
          					}else if(dateType == 2 && (!/^[1-9]\d*$/.test(days))){
      							$.messager.alert("提示","请输入有效期","warning");
      							return false;
      						}
    					}
    					$('#gateForm_desc').val(desc.replace(/\n/g,"<br/>"));
    					$.messager.progress();
    				},
    				success: function(data){
    					$.messager.progress('close');
                        var res = eval("("+data+")");
                        if(res.status == 1){
                            $.messager.alert('响应信息',"保存成功",'info',function(){
                                $('#s_data').datagrid('reload');
                                $('#addGateDiv').dialog('close');
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
                $('#addGateDiv').dialog('close');
            }
    	}]
    });   

});

</script>

</body>
</html>