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
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'心动慈露优惠券管理',split:true" style="height: 190px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/product/exportResult" method="post" >
		            <table class="search">
		                <tr>
		                	<td class="searchName">优惠券ID：</td>
							<td class="searchText"><input id="searchCouponId" name="searchCouponId" value="" /></td>
		                    <td class="searchName">优惠券类型：</td>
		                    <td class="searchText">
		                    	<select name="searchCouponType" id="searchCouponType" style="width:173px;">
									<option value="">全部</option>
									<option value="1">满x减y优惠券</option>
									<option value="2">x元现金券</option>
								</select>
		                    </td>
		                </tr>
		                <tr>
		                	<td class="searchName">有效期起：</td>
		                    <td class="searchText">
		                    	<input value="" id="startTimeBegin" name="startTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								-
								<input value="" id="startTimeEnd" name="startTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
		                    </td>
		                    <td class="searchName">有效期止：</td>
		                    <td class="searchText">
		                    	<input value="" id="endTimeBegin" name="endTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								-
								<input value="" id="endTimeEnd" name="endTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
		                    </td>
		                    
		                </tr>
		                <tr>
							<td class="searchName">备注：</td>
		                    <td class="searchText"><input type="text" id="searchRemark" name="searchRemark"/></td>
		                   	<td class="searchName">使用范围：</td>
		                	<td class="searchText">
								<input name="searchCouponScope" id="searchCouponScope" style="width:364px;"/>
							</td>
		                </tr>
		                <tr>
		                	<td class="searchName"></td>
		                    <td class="searchText">
								<a id="searchBtn" onclick="searchCoupon()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
		                	</td>
		                	<td class="searchName"></td>
							<td class="searchText"></td>
		                </tr>
		            </table>
		        </form>
		    </div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>

    		<!-- 生成单张优惠券 -->
    		<div id="createSingleCoupon_div" style="width:600px;height:250px;padding:20px 20px;">
    			<form method="post" id="createSingleCouponForm">
	    			<table cellpadding="5">
		    			<tr>
		    				<td>优惠券类型：</td>
		    				<td><input type="text" id="createSingleCouponForm_couponType" name="couponType" style="width:365px;"/></td>
		    			</tr>
		    			<tr>
		    				<td>有效期：</td>
		    				<td>
		    					<input type="text" value="" id="createSingleCouponForm_startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'createSingleCouponForm_endTime\')}'})"/>
								-
								<input type="text" value="" id="createSingleCouponForm_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'createSingleCouponForm_startTime\')}'})"/>
		    				</td>
		    			</tr>
		    			<tr>
		    				<td>备注：</td>
		    				<td><input type="text" id="createSingleCouponForm_remark" name="remark" value=""/></td>
		    			</tr>
	    			</table>
    			</form>
   	 		</div>
    		
    		<!-- 向单个用户发送优惠券 -->
    		<div id="singleSendCouponById_div" style="width:600px;height:300px;padding:20px 20px;">
    			<form method="post" id="singleSendCouponByIdForm">
	    			<table cellpadding="5">
		    			<tr>
		    				<td>用户ID：</td>
		    				<td><input type="text" id="singleSendCouponByIdForm_userId" name="userId"></td>
		    			</tr>
		    			<tr>
		    				<td>优惠券类型：</td>
		    				<td><input type="text" id="singleSendCouponByIdForm_couponType" name="couponType" style="width:365px;"/></td>
		    			</tr>
		    			<tr>
		    				<td>有效期：</td>
		    				<td>
		    					<input type="text" value="" id="singleSendCouponByIdForm_startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'singleSendCouponByIdForm_endTime\')}'})"/>
								-
								<input type="text" value="" id="singleSendCouponByIdForm_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'singleSendCouponByIdForm_startTime\')}'})"/>
		    				</td>
		    			</tr>
		    			<tr>
		    				<td>备注：</td>
		    				<td><input type="text" id="singleSendCouponByIdForm_remark" name="remark" value=""/></td>
		    			</tr>
	    			</table>
    			</form>
   	 		</div>
    		
    		<!-- 向用户Id批量发送优惠券 -->
    		<div id="batchSendCouponById_div" style="width:600px;height:300px;padding:20px 20px;">
    			<form method="post" id="batchSendCouponByIdForm" enctype="multipart/form-data">
	    			<table cellpadding="5">
	    				<tr>
		    				<td colspan="2">
		    					<a onclick="downloadTemplate(1)" href="javascript:;" class="easyui-linkbutton">下载模板</a>
		    					<input type="hidden" name="operType" value="1"/>
		    				</td>
		    			</tr>
		    			<tr>
		    				<td>用户ID：</td>
		    				<td><input type="text" id="batchSendCouponByIdForm_userId" name="userFile" style="width:300px"></td>
		    			</tr>
		    			<tr>
		    				<td>优惠券类型：</td>
		    				<td><input type="text" id="batchSendCouponByIdForm_couponType" name="couponType" style="width:365px;"/></td>
		    			</tr>
		    			<tr>
		    				<td>有效期：</td>
		    				<td>
		    					<input type="text" value="" id="batchSendCouponByIdForm_startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'batchSendCouponByIdForm_endTime\')}'})"/>
								-
								<input type="text" value="" id="batchSendCouponByIdForm_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'batchSendCouponByIdForm_startTime\')}'})"/>
		    				</td>
		    			</tr>
		    			<tr>
		    				<td>备注：</td>
		    				<td><input type="text" id="batchSendCouponByIdForm_remark" name="remark" value=""/></td>
		    			</tr>
	    			</table>
    			</form>
   	 		</div>
   	 		
   	 		<!-- 向用户手机号批量发送优惠券 -->
    		<div id="batchSendCouponByPhone_div" style="width:600px;height:300px;padding:20px 20px;">
    			<form method="post" id="batchSendCouponByPhoneForm" enctype="multipart/form-data">
	    			<table cellpadding="5">
	    				<tr>
		    				<td colspan="2">
		    					<a onclick="downloadTemplate(2)" href="javascript:;" class="easyui-linkbutton">下载模板</a>
		    					<input type="hidden" name="operType" value="2"/>
		    				</td>
		    			</tr>
		    			<tr>
		    				<td>用户手机号：</td>
		    				<td><input type="text" id="batchSendCouponByPhoneForm_userId" name="userFile" style="width:300px;"></td>
		    			</tr>
		    			<tr>
		    				<td>优惠券类型：</td>
		    				<td><input type="text" id="batchSendCouponByPhoneForm_couponType" name="couponType" style="width:365px;"/></td>
		    			</tr>
		    			<tr>
		    				<td>有效期：</td>
		    				<td>
		    					<input type="text" value="" id="batchSendCouponByPhoneForm_startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'batchSendCouponByPhoneForm_endTime\')}'})"/>
								-
								<input type="text" value="" id="batchSendCouponByPhoneForm_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'batchSendCouponByPhoneForm_startTime\')}'})"/>
		    				</td>
		    			</tr>
		    			<tr>
		    				<td>备注：</td>
		    				<td><input type="text" id="batchSendCouponByPhoneForm_remark" name="remark" value=""/></td>
		    			</tr>
	    			</table>
    			</form>
   	 		</div>
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

	function downloadTemplate(type){
		window.location.href="${rc.contextPath}/coupon/downloadTemplate?type="+type;
	}
	function searchCoupon(){
    	$('#s_data').datagrid('load',{
    		couponId:$("#searchCouponId").val(),
        	couponDetailType:$("#searchCouponType").val(),
        	couponDetailId:$("#searchCouponScope").combobox('getValue'),
        	startTimeBegin:$("#startTimeBegin").val(),
        	startTimeEnd:$("#startTimeEnd").val(),
        	endTimeBegin:$("#endTimeBegin").val(),
        	endTimeEnd:$("#endTimeEnd").val(),
        	couponRemark:$("#searchRemark").val()
    	});
	}
	
	function clearSearch(){
		$("#searchCouponId").val(''),
		$("#searchCouponType").find('option').eq(0).attr('selected','selected');
		$("#searchCouponScope").combobox('clear');
    	$("#startTimeBegin").val('');
    	$("#startTimeEnd").val('');
    	$("#endTimeBegin").val('');
    	$("#endTimeEnd").val('');
    	$("#searchRemark").val('');
    	$('#s_data').datagrid('load',{});
	}

	function viewDetail(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr="${rc.contextPath}/mwebGroupCoupon/couponAccountList/"+arr.rows[index].id;
		window.open(urlStr,"_blank");
	}
	
	$(function(){
		
		$("#createSingleCouponForm_couponType").combobox({
			url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponType?isRandomReduce=1',   
		    valueField:'code',   
		    textField:'text'
		});
		
		$("#singleSendCouponByIdForm_couponType").combobox({
			url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponType',   
		    valueField:'code',   
		    textField:'text'
		});
		
		$("#batchSendCouponByIdForm_couponType").combobox({
			url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponType',   
		    valueField:'code',   
		    textField:'text'
		});
		
		$("#batchSendCouponByPhoneForm_couponType").combobox({
			url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponType',   
		    valueField:'code',   
		    textField:'text'
		});
		
		/* $("input[name='couponType']").each(function(){
			$(this).combobox({
				url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponType',   
			    valueField:'code',   
			    textField:'text'
			});
		}); */
		
		$("#searchCouponScope").combobox({
			url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponType',   
		    valueField:'code',   
		    textField:'text'
		});
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/mwebGroupCoupon/jsonCouponInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'优惠券ID', width:15, align:'center'},
                {field:'createTime',    title:'生成时间', width:40, align:'center'},
                {field:'couponType',    title:'优惠券类型', width:50, align:'center'},
                {field:'couponScope',    title:'优惠券商品使用范围', width:70, align:'center'},
                {field:'total',    title:'优惠券总数', width:30, align:'center'},
                /* {field:'used',    title:'已使用', width:30, align:'center'}, */
                {field:'startTime',     title:'有效期起',  width:40,  align:'center'},
                {field:'endTime',     title:'有效期止',  width:40,   align:'center'},
                {field:'remark',    title:'app显示使用范围', width:80, align:'center'},
                {field:'type',     hidden:true},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
	                    return '<a href="javaScript:;" onclick="viewDetail(' + index + ')">查看</a>';
                    }
                }
            ]],
            toolbar:[{
	                id:'_add',
	                text:'生成单张优惠券',
	                iconCls:'icon-add',
	                handler:function(){
	                $("#createSingleCoupon_div input[type='text']").each(function(){
		                 $(this).val('');
	                 });
	                	$("#createSingleCoupon_div").dialog('open');
	                }
	        	},'-',{
	                id:'_add',
	                text:'单用户ID发放优惠券',
	                iconCls:'icon-add',
	                handler:function(){
	                $("#singleSendCouponById_div input[type='text']").each(function(){
		                 $(this).val('');
	                 });
	                	$("#singleSendCouponById_div").dialog('open');
	                }
            	},'-',{
                    iconCls: 'icon-add',
                    text:'批量用户ID发放优惠券',
                    handler: function(){
                    $("#batchSendCouponById_div input[type='text']").each(function(){
		                 $(this).val('');
	                 });
                    	$('#batchSendCouponById_div').dialog('open');
                    }
                
                }],
            pagination:true
        });
		
	    $('#createSingleCoupon_div').dialog({
	    	title: '生成单张优惠券',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '发送',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#createSingleCouponForm').form('submit',{
	    				url: "${rc.contextPath}/mwebGroupCoupon/createSingleCoupon",
	    				onSubmit:function(){
	    					var couponType = $("#createSingleCouponForm_couponType").combobox('getValue');
	    					var startTime = $("#createSingleCouponForm_startTime").val();
	    					var endTime = $("#createSingleCouponForm_endTime").val();
	    					if(couponType==""){
	    						$.messager.alert("info","请选择优惠券类型","warn");
	    						return false;
	    					}
	    					if($.trim(startTime)=='' || $.trim(endTime)==''){
	    						$.messager.alert("info","请选择有效期","warn");
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"发送成功,共放了"+res.num+"张优惠券",'info',function(){
	                            	$("input[name='couponType']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='startTime']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='endTime']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='remark']").each(function(){
	                            		$(this).val('');
	                            	});
	                                $('#s_data').datagrid('reload');
	                                $('#createSingleCoupon_div').dialog('close');
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
                	$("input[name='couponType']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='startTime']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='endTime']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='remark']").each(function(){
                		$(this).val('');
                	});
                    $('#createSingleCoupon_div').dialog('close');
	            }
	    	}]
	    });
	    
	    
		$('#singleSendCouponById_div').dialog({
	    	title: '向单个用户发放优惠券',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '发送',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#singleSendCouponByIdForm').form('submit',{
	    				url: "${rc.contextPath}/mwebGroupCoupon/sendCouponSingle",
	    				onSubmit:function(){
	    					var userId = $("#singleSendCouponByIdForm_userId").val();
	    					var couponType = $("#singleSendCouponByIdForm_couponType").combobox('getValue');
	    					var startTime = $("#singleSendCouponByIdForm_startTime").val();
	    					var endTime = $("#singleSendCouponByIdForm_endTime").val();
	    					if(userId == ""){
	    						$.messager.alert("info","请选择文件","warn");
	    						return false;
	    					}
	    					if(couponType==""){
	    						$.messager.alert("info","请选择优惠券类型","warn");
	    						return false;
	    					}
	    					if($.trim(startTime)=='' || $.trim(endTime)==''){
	    						$.messager.alert("info","请选择有效期","warn");
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"发送成功,共放了"+res.num+"张优惠券",'info',function(){
	                            	$("#singleSendCouponByIdForm_userId").val('');
	                            	$("input[name='couponType']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='startTime']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='endTime']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='remark']").each(function(){
	                            		$(this).val('');
	                            	});
	                                $('#s_data').datagrid('reload');
	                                $('#singleSendCouponById_div').dialog('close');
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
	            	$("#singleSendCouponByIdForm_userId").val('');
                	$("input[name='couponType']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='startTime']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='endTime']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='remark']").each(function(){
                		$(this).val('');
                	});
                    $('#singleSendCouponById_div').dialog('close');
	            }
	    	}]
	    });
	    
	    
	    $('#batchSendCouponById_div').dialog({
	    	title: '向批量用户ID发放优惠券',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '发送',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#batchSendCouponByIdForm').form('submit',{
	    				url: "${rc.contextPath}/mwebGroupCoupon/sendCouponBatch",
	    				onSubmit:function(){
	    					var userId = $("#batchSendCouponByIdForm_userId").filebox("getValue");
	    					var couponType = $("#batchSendCouponByIdForm_couponType").combobox('getValue');
	    					var startTime = $("#batchSendCouponByIdForm_startTime").val();
	    					var endTime = $("#batchSendCouponByIdForm_endTime").val();
	    					if(userId == ""){
	    						$.messager.alert("info","请选择文件","warn");
	    						return false;
	    					}
	    					if(couponType==""){
	    						$.messager.alert("info","请选择优惠券类型","warn");
	    						return false;
	    					}
	    					if($.trim(startTime)=='' || $.trim(endTime)==''){
	    						$.messager.alert("info","请选择有效期","warn");
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"发送成功,共放了"+res.num+"张优惠券",'info',function(){
	                            	$("#batchSendCouponByIdForm_userId").filebox('clear');
	                            	$("#batchSendCouponByIdForm_couponType").combobox('clear');
	                            	$("input[name='startTime']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='endTime']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='remark']").each(function(){
	                            		$(this).val('');
	                            	});
	                                $('#s_data').datagrid('reload');
	                                $('#batchSendCouponById_div').dialog('close');
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
	            	$("#batchSendCouponByIdForm_userId").filebox('clear');
	            	$("#batchSendCouponByIdForm_couponType").combobox('clear');
                	$("input[name='startTime']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='endTime']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='remark']").each(function(){
                		$(this).val('');
                	});
                    $('#batchSendCouponById_div').dialog('close');
	            }
	    	}]
	    });
	    
	    
	    $('#batchSendCouponByPhone_div').dialog({
	    	title: '向批量用户名<font color="red">（仅限手机用户）</font>发放优惠券',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '发送',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#batchSendCouponByPhoneForm').form('submit',{
	    				url: "${rc.contextPath}/coupon/sendCouponBatch",
	    				onSubmit:function(){
	    					var userId = $("#batchSendCouponByPhoneForm_userId").filebox("getValue");
	    					var couponType = $("#batchSendCouponByPhoneForm_couponType").combobox('getValue');
	    					var startTime = $("#batchSendCouponByPhoneForm_startTime").val();
	    					var endTime = $("#batchSendCouponByPhoneForm_endTime").val();
	    					if(userId == ""){
	    						$.messager.alert("info","请选择文件","warn");
	    						return false;
	    					}
	    					if(couponType==""){
	    						$.messager.alert("info","请选择优惠券类型","warn");
	    						return false;
	    					}
	    					if($.trim(startTime)=='' || $.trim(endTime)==''){
	    						$.messager.alert("info","请输入有效期","warn");
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"发送成功,共放了"+res.num+"张优惠券",'info',function(){
	                            	$("#batchSendCouponByPhoneForm_userId").filebox('clear');
	                            	$("#batchSendCouponByPhoneForm_couponType").combobox('clear');
	                            	$("input[name='startTime']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='endTime']").each(function(){
	                            		$(this).val('');
	                            	});
	                            	$("input[name='remark']").each(function(){
	                            		$(this).val('');
	                            	});
	                                $('#s_data').datagrid('reload');
	                                $('#batchSendCouponByPhone_div').dialog('close');
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
	            	$("#batchSendCouponByPhoneForm_userId").filebox('clear');
	            	$("#batchSendCouponByPhoneForm_couponType").combobox('clear');
                	$("input[name='startTime']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='endTime']").each(function(){
                		$(this).val('');
                	});
                	$("input[name='remark']").each(function(){
                		$(this).val('');
                	});
                    $('#batchSendCouponByPhone_div').dialog('close');
	            }
	    	}]
	    });
	    
		$("input[name='userFile']").each(function(){
			$(this).filebox({
				buttonText: '打开文件',
				buttonAlign: 'right'
			});
		});
	});
</script>

</body>
</html>