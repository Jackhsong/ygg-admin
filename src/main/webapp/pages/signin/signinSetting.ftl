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
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center',title:'签到管理'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'',split:true" style="height: 100px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td>每月配置：</td>
						<td>
							<select id="search_yearMonth" name="search_yearMonth">
    						<#list yearMonthList as glist>
    		 			  	<option value="${glist.value}"<#if currentMonth?exists && (currentMonth == glist.value)>selected</#if>>${glist.name}</option>
    		 				</#list>
    						</select>
						</td>
						<td>&nbsp;<a id="searchBtn" onclick="searchCouponDetail()" href="javascript:;" class="easyui-linkbutton" style="width:80px" >查询</a></td>
					</tr>
				</table>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
		  	<table id="s_data" style=""></table>
	
		    <!-- 新增 begin -->
		    <div id="editSignInDiv" class="easyui-dialog" style="width:400px;height:300px;padding:20px 20px;">
		        <form id="editSignInForm" method="post">
					<input id="editSignInForm_id" type="hidden" name="id" value="" >
					<input id="editSignInForm_yearMonth" type="hidden" name="yearMonth" value="" >
					<p>
						<span>&nbsp;&nbsp;&nbsp;类型：</span>
						<span>
							<input type="radio" name="signType" id="editSignInForm_signType1" value="1"/>积分&nbsp;&nbsp;
		                	<input type="radio" name="signType" id="editSignInForm_signType2" value="2"/>优惠券
						</span>
						<font color="red">*</font>
					</p>
					<p id="displayScore">
						<span>&nbsp;积分数量：</span>
						<span>
							<input type="text" name="score" id="editSignInForm_score" value=""/>
						</span>
						<font color="red">*</font>
					</p>
					<p id="displayCouponId">
						<span>选择优惠券：</span>
						<span>
							<select name="couponId" id="editSignInForm_couponId" style="width:173px">
		            			<option value="">--请选择--</option>
		            			<option value="690">5元现金券</option>
		            			<option value="691">8元现金券</option>
		            			<option value="142">10元现金券</option>
		            			<option value="196">12元现金券</option>
		            			<option value="141">15元现金券</option>
		            			<option value="140">20元现金券</option>
		            		</select>
						</span>
						<font color="red">*</font>
					</p>
					<p>
						<span>&nbsp;&nbsp;&nbsp;样式：</span>
						<span>
							<input type="radio" name="signStyle" id="editSignInForm_signStyle1" value="1"/>普通&nbsp;&nbsp;
		                	<input type="radio" name="signStyle" id="editSignInForm_signStyle2" value="2"/>高亮&nbsp;&nbsp;
		                	<input type="radio" name="signStyle" id="editSignInForm_signStyle3" value="3"/>月底大奖
						</span>
						<font color="red">*</font>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
		    
		</div>
	</div>
</div>
<script>

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


function searchCouponDetail(){
	$('#s_data').datagrid('load',{
		month:$('#search_yearMonth').val()
	});
}

function cleanEditSignDiv(){
	$('#editSignInForm_id').val('');
	$('#editSignInForm_yearMonth').val('');
	$("input[name='signType']").each(function(){
		$(this).prop("checked", false);
	});
	$('#editSignInForm_score').val('');
	$('#editSignInForm_couponId').find('option').eq(0).attr('selected','selected');
	$("input[name='signStyle']").each(function(){
		$(this).prop("checked", false);
	});
	$("p[id^='display']").each(function(){
		$(this).css('display','');
	});
}

function editIt(index){
	cleanEditSignDiv();
    var arr=$("#s_data").datagrid("getData");
    var id = arr.rows[index].id;
    var signType = arr.rows[index].type;
    var score = arr.rows[index].point;
    var couponId = arr.rows[index].couponId;
    var signStyle = arr.rows[index].style;
	$('#editSignInForm_id').val(id);
	if(signType == 1){
		$('#editSignInForm_signType1').prop('checked',true);
		$('#editSignInForm_score').val(score);
		$('#displayScore').css('display','');
		$('#displayCouponId').css('display','none');
	}else if(signType == 2){
		$('#editSignInForm_signType2').prop('checked',true);
		$('#editSignInForm_couponId').find("option[value='"+couponId+"']").attr('selected','selected');
		$('#displayScore').css('display','none');
		$('#displayCouponId').css('display','');
	}
	$("input[name='signStyle']").each(function(){
		if($(this).val()==signStyle){
			$(this).prop('checked',true);
		}
	});
	$('#editSignInDiv').dialog('open');
}

$(function(){
	
	$("#editSignInForm_signType1").change(function(){
		if($(this).is(":checked")){
			$("#displayScore").css('display','');
			$("#displayCouponId").css('display','none');
		}
	});
	
	$("#editSignInForm_signType2").change(function(){
		if($(this).is(":checked")){
			$("#displayScore").css('display','none');
			$("#displayCouponId").css('display','');
		}
	});

	$('#s_data').datagrid({
        nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/signin/jsonSettingInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns:[[
            {field:'day',     title:'累计签到次数',  width:50,  align:'center'},
            {field:'typeStr',     title:'奖励类型',  width:50,  align:'center'},
            {field:'score',    title:'积分数量', width:80, align:'center'},
            {field:'coupon',    title:'选择优惠券', width:80, align:'center'},
            {field:'styleStr',   title:'样式', width:60, align:'center'},
            {field:'signCount',   title:'累计签到人数', width:30, align:'center'},
            {field:'hidden',  title:'操作', width:20,align:'center',
                formatter:function(value,row,index){
                    if(row.canEdit==1){
                    	return '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a>';
                    }else{
                    	return '-';
                    }
                }
            }
        ]],
        toolbar:[
        {
            id:'_add',
            text:'新增',
            iconCls:'icon-add',
            handler:function(){
            	cleanEditSignDiv();
            	$('#editSignInForm_yearMonth').val($('#search_yearMonth').val());
            	$('#editSignInDiv').dialog('open');
            }
        },'-',{
            id:'_edit',
            text:'从上个月复制',
            iconCls:'icon-edit',
            handler:function(){
            	var yearMonth = $('#search_yearMonth').val();
       	    	$.messager.progress();
       	    	$.ajax({ 
                       url: '${rc.contextPath}/signin/checkCurrentMonthDays',
                       type: 'post',
                       dataType: 'json',
                       data: {'yearMonth':yearMonth},
                       success: function(data1){
	                       	if(data1.status > 0){
	                       		var tips = "确定复制吗？";
	                       		if(data1.status>1){
	                       			tisp = data1.msg;
	                       		}
	                       		$.messager.progress('close');
	                       		$.messager.confirm('提示',tips,function(b){
	                       			if(b){
	                       				$.ajax({ 
	                                           url: '${rc.contextPath}/signin/copyFromLastMonth',
	                                           type: 'post',
	                                           dataType: 'json',
	                                           data: {'yearMonth':yearMonth},
	                                           success: function(data2){
	                                             	$.messager.progress('close');
	                                               if(data2.status == 1){
	                                                 	$.messager.alert("提示",data2.msg,"info");
	                                                 	searchCouponDetail();
	                                                   $('#s_data') .datagrid("load");
	                                               }else{
	                                                  $.messager.alert("提示",data2.msg,"error");
	                                               }
	                                           },
	                                           error: function(xhr){
	                                              $.messager.progress('close');
	                                              $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                                           }
	                                     });
	                       			}
	                       		});
	                       	}else{
	                       		$.messager.progress('close');
	                       		$.messager.alert("提示",data1.msg,"error");
	                       	}
                       },
                       error: function(xhr){
                           $.messager.progress('close');
                           $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                        }
       	    	});
            }
        }],
     	pagination:true
    });
    
    $('#editSignInDiv').dialog({
    	title:'每日签到配置',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#editSignInForm').form('submit',{
    				url: "${rc.contextPath}/signin/saveOrUpdate",
    				onSubmit:function(){
    					var signType = $("input[name='signType']:checked").val();
    					var score = $('#editSignInForm_score').val();
    					var couponId = $('#editSignInForm_couponId').val();
    					var signStyle = $("input[name='signStyle']:checked").val();
    					if(signType == '' || signType==null || signType==undefined){
    						$.messager.alert("info","请选择类型","warn");
    						return false;
    					}else if(signType==1){
    						if($.trim(score)==''){
								$.messager.alert("提示","请输入积分数量","warn");
								return false;
							}else{
								if(!/^\d+$/.test(score)){
									$.messager.alert("提示","积分数量只能为正整数","warn");
    								return false;
								}
							}
    					}else if(signType==2){
							if(couponId == '' || couponId == null || couponId == undefined){
								$.messager.alert("提示","请选择优惠券信息","warn");
								return false;
							}
						}else if(signStyle == '' || signStyle == null || signStyle == undefined){
							$.messager.alert("提示","请选择样式","warn");
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
                                $('#editSignInDiv').dialog('close');
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
                $('#editSignInDiv').dialog('close');
            }
    	}]
    });     
});
</script>

</body>
</html>