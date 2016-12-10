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
	
		
		<div data-options="region:'center'" >
	    	<!--数据表格-->
	    	<table id="s_data" style=""></table>
    	</div>
    	
		<!-- dialog begin -->
		<div id="addTeamHeadFreeOrderDiv" class="easyui-dialog" style="width:600px;height:420px;padding:10px 10px;">
			<form action="" id="teamHeadFreeOrderForm" method="post">
				<p>
					<span>团购商品Id：</span>
					<span><input type="text" id="teamHeadFreeOrderForm_mwebGroupProductId" name="mwebGroupProductId" value="" style="width:300px;" onblur="stringTrim(this)"/></span>&nbsp;<br/><br/>
					<span>商品名称：</span></span>
					<span id="mwebGroupProductInfo" style="color:red"></span><br/><br/>
					<span>团购时间：</span></span>
					<span id="mwebGroupProductData" style="color:red"></span>
				</p>
				
				<div id="canAddDiv">
				<p>
					<span>优惠券Id：</span>
					<span><input type="text" id="teamHeadFreeOrderForm_couponId" name="couponId" value="" style="width:300px;" onblur="stringTrim(this)"/></span>&nbsp;<br/><br/>
					<span>优惠券类型：</span></span>
					<span id="couponInfo" style="color:red"></span><br/><br/>
					<span>使用范围：</span></span>
					<span id="teamTypeInfo" style="color:red"></span>
					
				</p>
				<p>
					<span>优惠券有效期：</span>
					<span>
						<input type="radio" name="dateType" id="teamHeadFreeOrderForm_dateType1" value="1"/>从原有优惠券中读取信息&nbsp;&nbsp;
		                <input type="radio" name="dateType" id="teamHeadFreeOrderForm_dateType2" value="2"/>从领券之日起
		                <input type="text"  name="days" id="teamHeadFreeOrderForm_days" onblur="stringTrim(this)" style="width:30px;"/>天内
					</span>
				</p>
				</div>
				<p>
					<span>派发概率：</span>
					<span>
						<input type="text" name="giveChance" id="teamHeadFreeOrderForm_giveChance" value="1" onblur="stringTrim(this)" style="width:30px;"/>%&nbsp;&nbsp;
					</span>
				</p>		
			</form>
		</div>
		<!-- dialog end -->


		<!-- dialog begin -->
		  <div id="editTeamHeadFreeOrderDiv" class="easyui-dialog" style="width:620px;height:390px;padding:10px 10px;">
			<form action="" id="editTeamHeadFreeOrderForm" method="post">
				<input id="editTeamHeadFreeOrderForm_Id" type="hidden" name="id" value=""/>
				<p>
					<span>团购商品Id：</span>
					<span><input type="text" id="editTeamHeadFreeOrderForm_mwebGroupProductId" name="mwebGroupProductId" value="" style="width:300px;" onblur="stringTrim(this)"/></span>&nbsp;<br/><br/>
					<span>商品名称：</span></span>
					<span id="mwebGroupProductInfo2" style="color:red"></span><br/><br/>
					<span>团购时间：</span></span>
					<span id="mwebGroupProductData2" style="color:red"></span>
				</p>
				
				<div id="canNotEditDiv">
				<p>
					<span>优惠券Id：</span>
					<span><input type="text" id="editTeamHeadFreeOrderForm_couponId" name="couponId" value="" style="width:300px;" onblur="stringTrim(this)"/></span>&nbsp;<br/><br/>
					<span>优惠券类型：</span></span>
					<span id="couponInfo2" style="color:red"></span><br/><br/>
					<span>使用范围：</span></span>
					<span id="teamTypeInfo2" style="color:red"></span>
				</p>
				<p>
					<span>优惠券有效期：</span>
					<span>
						<input type="radio" name="dateType" id="editTeamHeadFreeOrderForm_dateType1" value="1"/>从原有优惠券中读取信息&nbsp;&nbsp;
		                <input type="radio" name="dateType" id="editTeamHeadFreeOrderForm_dateType2" value="2"/>从领券之日起
		                <input type="text"  name="days" id="editTeamHeadFreeOrderForm_days" onblur="stringTrim(this)" style="width:30px;"/>天内
					</span>
				</p>
				</div>
				<p>
					<span>派发概率：</span>
					<span>
						<input type="text" name="giveChance" id="editTeamHeadFreeOrderForm_giveChance" value="1" onblur="stringTrim(this)" style="width:30px;"/>%&nbsp;&nbsp;
					</span>
				</p>		
			</form>
		</div>
		<!-- dialog end -->
	</div>
	
</div>


<script type="text/javascript">
$(function(){
	$("#teamHeadFreeOrderForm_mwebGroupProductId").change(function(){
		var mwebGroupProductId = $.trim($(this).val());
		if(mwebGroupProductId != ''){
            $.post(
  	           	"${rc.contextPath}/wechatGroup/jsonProductInfoForTeamById",
  	            {id:mwebGroupProductId},
  	            function(data){
  	                  if(data.status==0){
  	                   $("#mwebGroupProductInfo").text(data.msg);
  	                  }
  	                  else{
  	                  var startTime=data.data.startTime;
  	                   endTime=data.data.endTime;
  	                      $("#mwebGroupProductInfo").text(data.data.shortName);
  	                      $("#mwebGroupProductData").text(startTime+'~'+endTime);
  	                      
  	                  }
	   	          	
  	            }
  	        );
		}
	});
	
	
	$("#teamHeadFreeOrderForm_couponId").change(function(){
		var couponId = $.trim($(this).val());
		if(couponId != ''){
            $.post(
  	           	"${rc.contextPath}/mwebGroupCoupon/getCouponInfoById",
  	            {id:couponId},
  	            function(data){
	   	          	$("#couponInfo").text(data.couponInfo);
	   	          	$("#teamTypeInfo").text(data.teamTypeInfo);
	   	          	
  	            }
  	        );
		}
	});
	
	
	$("#editTeamHeadFreeOrderForm_mwebGroupProductId").change(function(){
		var mwebGroupProductId = $.trim($(this).val());
		if(mwebGroupProductId != ''){
            $.post(
  	           	"${rc.contextPath}/wechatGroup/jsonProductInfoForTeamById",
  	            {id:mwebGroupProductId},
  	            function(data){
  	                  if(data.status==0){
  	                   $("#mwebGroupProductInfo2").text(data.msg);
  	                  }
  	                  else{
  	                  var startTime=data.data.startTime;
  	                   endTime=data.data.endTime;
  	                      $("#mwebGroupProductInfo2").text(data.data.shortName);
  	                      $("#mwebGroupProductData2").text(startTime+'~'+endTime);
  	                      
  	                  }
	   	          	
  	            }
  	        );
		}
	});
	
	
	$("#editTeamHeadFreeOrderForm_couponId").change(function(){
		var couponId = $.trim($(this).val());
		if(couponId != ''){
            $.post(
  	           	"${rc.contextPath}/mwebGroupCoupon/getCouponInfoById",
  	            {id:couponId},
  	            function(data){
	   	          	$("#couponInfo2").text(data.couponInfo);
	   	          	$("#teamTypeInfo2").text(data.teamTypeInfo);
	   	          	
  	            }
  	        );
		}
	});
	
	$(document).keydown(function(e){
		if (!e){
		   e = window.event;  
		}  
		if ((e.keyCode || e.which) == 13) {  
		      $("#searchBtn").click();  
		}
	});
});

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

function stringTrim(obj){
	var value = $(obj).val();
	$(obj).val($.trim(value));
}

function cleanAddTeamHeadFreeOrderDiv(){
	$("#teamHeadFreeOrderForm_mwebGroupProductId").val('');
	$("#teamHeadFreeOrderForm_giveChance").val('');
	$("#mwebGroupProductInfo").text('');
	$("#mwebGroupProductData").text('');
	$("#teamHeadFreeOrderForm_couponId").val('');
	$("#couponInfo").text('');
	$("#teamTypeInfo").text('');
	$("#teamHeadFreeOrderForm input[type='text']").each(function(){
		$(this).val('');
	});

}


function cleanEditTeamHeadFreeOrderDiv(){
	$("#editTeamHeadFreeOrderForm_mwebGroupProductId").val('');
	$("#editTeamHeadFreeOrderForm_giveChance").val('');
	$("#mwebGroupProductInfo2").text('');
	$("#mwebGroupProductData2").text('');
	$("#teamHeadFreeOrderForm_couponId").val('');
	$("#couponInfo2").text('');
	$("#teamTypeInfo2").text('');
	$("#editTeamHeadFreeOrderForm input[type='text']").each(function(){
		$(this).val('');
	});
}


    $('#s_data').datagrid({
    	nowrap: false,
        striped: true,
        collapsible:true,
        idField:'id',
        url:'${rc.contextPath}/mwebGroupTeamHeadFreeOrder/jsonTeamHeadFreeOrderInfo',
        loadMsg:'正在装载数据...',
        fitColumns:true,
        remoteSort: true,
        singleSelect:true,
        pageSize:50,
        pageList:[50,60],
        columns: [
            [
                {field: 'id', title: 'ID',  align: 'center'},
                {field: 'mwebGroupProductId', title: '团购商品ID',  align: 'center'},
                {field: 'shortName', title: '商品名称',  align: 'center'},
                {field: 'isOffShelves', title: '商品状态',  align: 'center',
                formatter: function (value, row, index) {
                       var isOffShelves=value;
                       var isAvailable=row.isAvailable;
                       if(isOffShelves==1||isAvailable==0){
                       return '停用';
                       }
                       else{
                          return '进行中';
                       }
                }},
                {field: 'isOpenGive', title: '派发状态', align: 'center',formatter: function (value, row, index) {
                   if(value==1){
                    return '进行中';
                   }
                   else{
                    return ' 停止中';
                   }
                }},
                {field: 'couponId', title: '优惠券id', align: 'center'},
                {field: 'type', title: '优惠券类型',  align: 'center',formatter: function (value, row, index) {
                   if(value==1){
                            return '满减';
                   }else if(value==2){
                       return '现金';
                   }
                }},
                {field: 'desc', title: '显示使用范围', align: 'center'},
                {field: 'teamType', title: '拼团可用范围',  align: 'center',formatter: function (value, row, index) {
                   if(value==1){
                     return '仅用于开团';
                   }
                   else{
                     return '开团参团均可使用';
                   }
                }},
                {field: 'giveChance', title: '派发概率', align: 'center',formatter: function (value, row, index) {
                    return value+'%';
                }},
                {field: 'totalMoney', title: '成功拼团数', align: 'center'},
                {field: 'giveNumber', title: '已派发数量',  align: 'center'},
                {field: 'hidden', title: '操作', width: 40, align: 'center',
                    formatter: function (value, row, index) {
                    	var c = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                    	var d = ''
                    	if(row.isOpenGive == 1){
                    		d = '<a href="javaScript:;" onclick="updateIsOpenGive(' + row.id + ',' + 0 + ')">停止派发</a>';
                    	}else{
                    		d = '<a href="javaScript:;" onclick="updateIsOpenGive(' + row.id + ',' + 1 + ')">开始派发</a>';
                    	}
                    	return  c + d;
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
                	cleanAddTeamHeadFreeOrderDiv();
                	$('#addTeamHeadFreeOrderDiv').dialog('open');
                }
            }
        ],
        pagination:true
    });

    $('#addTeamHeadFreeOrderDiv').dialog({
    	title:'免单信息',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#teamHeadFreeOrderForm').form('submit',{
    				url: "${rc.contextPath}/mwebGroupTeamHeadFreeOrder/saveTeamHeadFreeOrder",
    				onSubmit:function(){	
    					var giveChance = $('#teamHeadFreeOrderForm_giveChance').val();
    					var couponId = $('#teamHeadFreeOrderForm_couponId').val();
    					var dateType = $("input[name='dateType']:checked").val();
    					var days = $("#teamHeadFreeOrderForm_days").val();
    					var mwebGroupProductId= $("#teamHeadFreeOrderForm_mwebGroupProductId").val();
    					if($.trim(giveChance)==''){
    						$.messager.alert("info","请输入派发概率","warn");
    						return false;
    					}else if(couponId == '' || couponId==null || couponId==undefined){
      						$.messager.alert("info","请选择优惠券","warn");
      						return false;
      					}
      					else if(mwebGroupProductId == '' || mwebGroupProductId==null || mwebGroupProductId==undefined){
      						$.messager.alert("info","请选择团购商品","warn");
      						return false;
      					}
      					else if(dateType == '' || dateType==null || dateType==undefined){
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
                                $('#addTeamHeadFreeOrderDiv').dialog('close');
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
                $('#addTeamHeadFreeOrderDiv').dialog('close');
            }
    	}]
    });


function updateIsOpenGive(id,isOpenGive){
	var tip = "";
	if(isOpenGive == 0){
		tip = "确定停用吗？";
	}else{
		tip = "确定启用吗？";
	}
	$.messager.confirm("提示信息",tip,function(ra){
        if(ra){
	            $.messager.progress();
	            $.post(
	            		"${rc.contextPath}/mwebGroupTeamHeadFreeOrder/updateIsOpenGive",
	            		{id:id,isOpenGive:isOpenGive},
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


function editIt(index){
	cleanEditTeamHeadFreeOrderDiv();
    var arr=$("#s_data").datagrid("getData");
    $("#editTeamHeadFreeOrderForm_Id").val(arr.rows[index].id);
    $("#editTeamHeadFreeOrderForm_mwebGroupProductId").val(arr.rows[index].mwebGroupProductId);
	$("#editTeamHeadFreeOrderForm_giveChance").val(arr.rows[index].giveChance);
	$("#editTeamHeadFreeOrderForm_couponId").val(arr.rows[index].couponId);
    var validity_date_type=arr.rows[index].validityDateType;
    var days=arr.rows[index].days;

    if(validity_date_type==1){
     
      $("#editTeamHeadFreeOrderForm_dateType1").prop('checked',true);;
    }
    else{
     $("#editTeamHeadFreeOrderForm_dateType2").prop('checked',true);
     $("#editTeamHeadFreeOrderForm_days").val(days);
    }
      $.post(
  	           	"${rc.contextPath}/wechatGroup/jsonProductInfoForTeamById",
  	            {id:arr.rows[index].mwebGroupProductId},
  	            function(data){
  	                  if(data.status==0){
  	                   $("#mwebGroupProductInfo2").text(data.msg);
  	                  }
  	                  else{
  	                  var startTime=data.data.startTime;
  	                   endTime=data.data.endTime;
  	                      $("#mwebGroupProductInfo2").text(data.data.shortName);
  	                      $("#mwebGroupProductData2").text(startTime+'~'+endTime);
  	                      
  	                  }
	   	          	
  	            }
  	        );
  	        
  	        
  	        $.post(
  	           	"${rc.contextPath}/mwebGroupCoupon/getCouponInfoById",
  	            {id:arr.rows[index].couponId},
  	            function(data){
	   	          	$("#couponInfo2").text(data.couponInfo);
	   	          	$("#teamTypeInfo2").text(data.teamTypeInfo);
	   	          	
  	            }
  	        );
    $("#editTeamHeadFreeOrderDiv").dialog('open');
}




 $('#editTeamHeadFreeOrderDiv').dialog({
    	title:'免单信息',
    	collapsible: true,
    	closed: true,
    	modal: true,
    	buttons: [
    	{
    		text: '保存',
    		iconCls: 'icon-ok',
    		handler: function(){
    			$('#editTeamHeadFreeOrderForm').form('submit',{
    				url: "${rc.contextPath}/mwebGroupTeamHeadFreeOrder/saveTeamHeadFreeOrder",
    				onSubmit:function(){
    					var giveChance = $('#editTeamHeadFreeOrderForm_giveChance').val();
    					var couponId = $('#editTeamHeadFreeOrderForm_couponId').val();
    					var mwebGroupProductId= $("#editTeamHeadFreeOrderForm_mwebGroupProductId").val();
    					var dateType = $("input[name='dateType']:checked").val();
    					var days = $("#editTeamHeadFreeOrderForm_days").val();
    					if($.trim(giveChance)==''){
    						$.messager.alert("info","请输入派发概率","warn");
    						return false;
    					}else if(couponId == '' || couponId==null || couponId==undefined){
      						$.messager.alert("info","请选择优惠券","warn");
      						return false;
      					}
      					else if(mwebGroupProductId == '' || mwebGroupProductId==null || mwebGroupProductId==undefined){
      						$.messager.alert("info","请选择团购商品","warn");
      						return false;
      					}
      					else if(dateType == '' || dateType==null || dateType==undefined){
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
                                $('#editTeamHeadFreeOrderDiv').dialog('close');
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
                $('#editTeamHeadFreeOrderDiv').dialog('close');
            }
    	}]
    }); 
</script>

</body>
</html>