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
	/* text-align:right; */
	text-align:justify;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
.inputStyle{
	width:250px;
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
		<div data-options="region:'north',title:'已确认笨鸟订单列表',split:true" style="height: 120px;padding-top:10px">
	        <form id="searchForm" action="" method="post" >
	        	<table>
					<tr>
						<td class="searchName">订单编号：</td>
						<td class="searchText">
							<input id="searchOrderNumber" name="orderNumber" value="" />
						</td>
						<!-- <td class="searchName">推送状态：</td>
						<td class="searchText">
							<select id="sendStatus" name="sendStatus" style="width: 100px;">
								<option value="0">未推送</option>
								<option value="1">已推送</option>
								<option value="-1">全部</option>
							</select>
						</td> -->
						<td class="searchText" style="padding-top: 5px">
							<a id="searchBtn" onclick="searchOrder()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
						</td>
					</tr>
				</table>
	        </form>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>

	        <!-- 修改订单收货地址 -->
	        <div id="updateAddress" class="easyui-dialog" style="width:500px;height:400px;padding:20px 20px;">
	            <form id="addressForm" method="post">
				<input id="addressForm_orderNumber" type="hidden" name="orderNumber" value="" >
	            <table cellpadding="5">
	            	<tr>
	                    <td>姓名:</td>
	                    <td><input name="fullName" value="" id="addressForm_fullName"/></td>
	                </tr>
	                <tr>
	                    <td>身份证号:</td>
	                    <td><input name="idCard" value="" id="addressForm_idCard"/></td>
	                </tr>
	                <tr>
	                    <td>手机:</td>
	                    <td><input name="mobileNumber" value="" id="addressForm_mobileNumber"/></td>
	                </tr>
	                <tr>
	                    <td>省:</td>
	                    <td>
	                    	<select name="province" id="addressForm_province">
	                    		<#list provinceList as p >
				 					<option value="${p.provinceId?c}">${p.name}</option>
				 				</#list>
				 		   </select>
	                    </td>
	                </tr>
	                <tr>
	                    <td>市:</td>
	                    <td>
	                    	<select name="city" id="addressForm_city">
	                    		<#list cityList as c >
				 					<option value="${c.cityId?c}" >${c.name}</option>
				 				</#list>
				 		   </select>
	                    </td>
	                </tr>
	                <tr>
	                    <td>区:</td>
	                    <td>
	                    	<select name="district" id="addressForm_district">
	                    		<#list districtList as d >
				 					<option value="${d.districtId?c}">${d.name}</option>
				 				</#list>
				 		   </select>
	                    </td>
	                </tr>
	                <tr>
	                    <td>详细地址:</td>
	                    <td><textarea id="addressForm_detailAddress" name="detailAddress" onkeydown="checkEnter(event)" style="height: 60px;width: 300px"></textarea></td>
	                </tr>
	            </table>
	        	</form>
	        </div>
	        
	        
	        <!-- 修改订单身份证 -->
	        <div id="updateIdCard" class="easyui-dialog" style="width:500px;height:200px;padding:20px 20px;">
	            <form id="idCardForm" method="post">
				<input id="idCardForm_orderNumber" type="hidden" name="orderNumber" value="" >
	            <table cellpadding="5">
	            	<tr>
	                    <td>姓名:</td>
	                    <td><input name="fullName" value="" id="idCardForm_fullName"/></td>
	                </tr>
	                <tr>
	                    <td>身份证号:</td>
	                    <td><input name="idCard" value="" id="idCardForm_idCard"/></td>
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

	function searchOrder(){
		$('#s_data').datagrid('load', {
			number : $("#searchOrderNumber").val(),
			sendStatus : $("#orderStatus").val()
		});
	}
	
	function modifyAddress(index){
		var arr=$("#s_data").datagrid("getData");
		var number = arr.rows[index].number;
		$("#addressForm_orderNumber").val(number);
		$.ajax({
			url: '${rc.contextPath}/birdex/findReceiveInfo',
			type:'post',
			dataType:'json',
			data:{'orderNumber':number},
			success: function(data){
				if(data.status == 1){
					$("#addressForm_fullName").val(data.fullName);
					$("#addressForm_idCard").val(data.idCard);
					$("#addressForm_mobileNumber").val(data.mobileNumber);
					$("#addressForm_province").val(data.province);
					$("#addressForm_city").val(data.city);
					$("#addressForm_district").val(data.district);
					$("#addressForm_detailAddress").val(data.detailAddress);
				}
				$('#updateAddress').dialog('open');
			},
			error:function(xhr){
				$.messager.progress('close');
				$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
			}
		});
	}
	
	
	function modifyIdCard(index){
		var arr=$("#s_data").datagrid("getData");
		var number = arr.rows[index].number;
		$("#idCardForm_orderNumber").val(number);
		$.ajax({
			url: '${rc.contextPath}/birdex/findReceiveInfo',
			type:'post',
			dataType:'json',
			data:{'orderNumber':number},
			success: function(data){
				if(data.status == 1){
					$("#idCardForm_fullName").val(data.fullName);
					$("#idCardForm_idCard").val(data.idCard);
				}
				$('#updateIdCard').dialog('open');
			},
			error:function(xhr){
				$.messager.progress('close');
				$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
			}
		});
	}
	
	function cancelOrder(index){
		var arr=$("#s_data").datagrid("getData");
		var number = arr.rows[index].number;
		$.messager.confirm('提示','确认取消订单吗？',function(r){
		    if (r){
		    	$.messager.progress();
    			$.ajax({
					url: '${rc.contextPath}/birdex/cancelBirdexOrder',
					type: 'post',
					dataType: 'json',
				    data: {'orderNumber':number},
					success: function(data){
						$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid("load");
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
		});
	}

	$(function(){
		$('#s_data').datagrid({
	        nowrap: false,
	        striped: true,
	        collapsible:true,
	        idField:'id',
	        url:'${rc.contextPath}/birdex/jsonBirdexPushInfo',
	        loadMsg:'正在装载数据...',
	        fitColumns:true,
	        remoteSort: true,
	        pageSize:50,
	        columns:[[
	            {field :'id',    title:'ID', width:20, align:'center'},
	            {field :'number', title : '订单编号', width : 30, align : 'center'},
				{field :'pushDesc', title : '推送状态', width : 20, align : 'center'},
				{field :'pushResultRemark', title : '备注', width : 40, align : 'center'},
				{field :'statusDesc', title : '物流状态', width : 20, align : 'center'},
	            {field:'updateTime',    title:'最后更新时间', width:20, align:'center'},
	            {field:'hidden',  title:'操作', width:40,align:'center',
	                formatter:function(value,row,index){
	                	var a = '';
	                	var b = '';
	                	var c = '';
	                	if(row.status<=2){
	                		a = '<a href="javaScript:;" onclick="modifyIdCard(' + index + ')">修改身份证</a> | ';
	                		b = '<a href="javaScript:;" onclick="modifyAddress(' + index + ')">修改收货地址</a> | ';
	                	}
	                	if(row.status==0){
	                		c = '<a href="javaScript:;" onclick="cancelOrder(' + index + ')">取消订单</a>'
	                	}
	                	if(a=='' && b=='' && c==''){
	                		return '--';
	                	}else{
		                    return a + b + c;
	                	}
	                }
	            }
	        ]],
	        pagination:true
	    });
		
		$('#updateAddress').dialog({
            title:'修改订单收货地址',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#addressForm').form('submit',{
                        url:"${rc.contextPath}/birdex/updateAddress",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	$('#s_data').datagrid("load");
                                    $('#updateAddress').dialog('close');
                                });
                            } else{
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
                    $('#updateAddress').dialog('close');
                }
            }]
        });	
		
		$('#updateIdCard').dialog({
            title:'修改身份证',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                    $('#idCardForm').form('submit',{
                        url:"${rc.contextPath}/birdex/updateIdCard",
                        success:function(data){
                            var res = eval("("+data+")")
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info',function(){
                                	$('#s_data').datagrid("load");
                                    $('#updateIdCard').dialog('close');
                                });
                            } else{
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
                    $('#updateIdCard').dialog('close');
                }
            }]
        });	
		
		$('#addressForm_province').change(function(){
			$child = $('#addressForm_city');
			var pid = $('#addressForm_province').val();
			var selected_id = 0;
			$('#addressForm_district').empty();
			$.ajax({
	            url:"${rc.contextPath}/order/getAllCity",
	            type:'post',
	            data: {id : pid},
	            dataType: 'json',
	            success:function(data){
	                var options = '<option value="0">--请选择--</option>';
	                $.each(data,function(i){
	                    if(data[i].id == selected_id){
	                        options += '<option value="'+this.cityId+'" selected="selected">'+this.name+'</option>';  
	                    }else{
	                        options += '<option value="'+this.cityId+'" >'+this.name+'</option>'; 
	                    }
	                });
	                $child.empty().append(options);
	            }
	        });
		});
		
		$('#addressForm_city').change(function(){
			$child = $('#addressForm_district');
			var pid = $('#addressForm_city').val();
			var selected_id = 0;
			$.ajax({
	            url:"${rc.contextPath}/order/getAllDistrict",
	            type:'post',
	            data: {id : pid},
	            dataType: 'json',
	            success:function(data){
	                var options = '<option value="0">--请选择--</option>';
	                $.each(data,function(i){
	                    if(data[i].id == selected_id){
	                        options += '<option value="'+this.districtId+'" selected="selected">'+this.name+'</option>';  
	                    }else{
	                        options += '<option value="'+this.districtId+'" >'+this.name+'</option>'; 
	                    }
	                });
	                $child.empty().append(options);
	            }
	        });
		});		
	});
</script>
</body>
</html>