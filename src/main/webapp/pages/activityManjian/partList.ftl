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

<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'专场满减活动管理',split:true" style="height: 100px;">
			<div style="height: 40px;padding: 10px">
				<input type="checkbox" name="timeStatus" id="timeStatus1" value="2" <#if status?exists && (status?contains("2"))>checked</#if>/>进行中
				<input type="checkbox" name="timeStatus" id="timeStatus1" value="3" <#if status?exists && (status?contains("3"))>checked</#if>/>即将开始
				<input type="checkbox" name="timeStatus" id="timeStatus1" value="4" <#if status?exists && (status?contains("4"))>checked</#if>/>已结束
			</div>
		</div> 
		
		<div data-options="region:'center'" >
			<!--数据表格-->
			<table id="s_data" ></table>
			
			<!-- 编辑 -->
		    <div id="editAmDiv" class="easyui-dialog" style="width:500px;height:400px;padding:20px 20px;">
		        <form id="editAmForm" method="post">
					<input id="editAmForm_id" type="hidden" name="id" value="0" >
					<p>
						<span>活动名称：</span>
						<span><input type="text" id="editAmForm_name" name="name" style="width:330px"  maxlength="20"></span>
					</p>
					<p>
						<span>活动时间：</span>
						<span>
							<input type="text" id="editAmForm_startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'editAmForm_endTime\')}'})">
							-
							<input type="text" id="editAmForm_endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'editAmForm_startTime\')}'})">
						</span>
					</p>
					<p>
						<span>活动梯度：</span>
						<span>梯度1&nbsp;&nbsp;满<input type="number" name="oneThreshold" id="editAmForm_oneThreshold" style="width:50px"/>减<input type="number" name="oneReduce" id="editAmForm_oneReduce" style="width:50px"/></span></p>
					<p>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<span>梯度2&nbsp;&nbsp;满<input type="number" name="twoThreshold" id="editAmForm_twoThreshold" style="width:50px"/>减<input type="number" name="twoReduce" id="editAmForm_twoReduce" style="width:50px"/><span>注：如不需要该梯度请留空</span></span>
					</p>
					<p>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<span>梯度3&nbsp;&nbsp;满<input type="number" name="threeThreshold" id="editAmForm_threeThreshold" style="width:50px"/>减<input type="number" name="threeReduce" id="editAmForm_threeReduce" style="width:50px"/><span>注：如不需要该梯度请留空</span></span>
					</p>
					<p>
						<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<span>梯度4&nbsp;&nbsp;满<input type="number" name="fourThreshold" id="editAmForm_fourThreshold" style="width:50px"/>减<input type="number" name="fourReduce" id="editAmForm_fourReduce" style="width:50px"/><span>注：如不需要该梯度请留空</span></span>
					</p>
					<p>
						<span>关联类型：</span>
						<span>
							<input type="radio" name="type" id="editAmForm_type1" value="2"/>组合特卖&nbsp;&nbsp;
							<input type="radio" name="type" id="editAmForm_type2" value="3"/>自定义活动&nbsp;&nbsp;
							<input type="radio" name="type" id="editAmForm_type3" value="4"/>原生自定义页面
						</span>
					</p>
					<p id="groupSaleSpan">
						<span>组合ID：</span>
						<span><input type="number" id="editAmForm_groupSale" name="groupSaleId" style="width:330px;" value=""/></span>
						<span id="editAmForm_groupSaleName" style="color:red"></span>
					</p>
					<p id="customSaleSpan" style="display:none">
						<span>自定义活动：</span>
						<span><input type="text" id="editAmForm_customSale" name="customSaleId" style="width:300px;"/></span>
					</p>
					<p id="customPageSpan" style="display:none">
						<span>原生自定义页面：</span>
						<span><input type="text" id="editAmForm_customPage" name="customPageId" style="width:300px;"/></span>
					</p>
		    	</form>
		    </div>			 	    		
		</div>
	</div>
</div>


<script>
	function searchCustom(){
		var status = [];
		$("input[name='timeStatus']:checked").each(function(){
			status.push($(this).val());
		});
		$('#s_data').datagrid('load',{
			status:status.join(','),
			type:'${type}'
		});
	}
	

	function displayId(id,isAvailable){
		var tip = "";
		if(isAvailable == 0){
			tip = "确定停用吗？";
		}else{
			tip = "确定启用吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
   	            $.messager.progress();
   	            $.ajax({
   	            	url:'${rc.contextPath}/activityManjian/updateDisplayStatus',
   	            	type:'post',
   	            	dataType:'json',
   	            	data:{'id':id,"code":isAvailable},
   	            	success:function(data){
   	            		$.messager.progress('close');
						if(data.status == 1){
							$('#s_data').datagrid('clearSelections');
							$('#s_data').datagrid("reload");
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

	function editIt(index){
		clearEditAmForm();
	    var arr=$("#s_data").datagrid("getData");
	    var gradientType = arr.rows[index].gradientType;
	    var type = arr.rows[index].type;
	    var typeId = arr.rows[index].typeId;
	    $("#editAmForm_id").val(arr.rows[index].id);
	    $("#editAmForm_name").val(arr.rows[index].name);
		$("#editAmForm_startTime").val(arr.rows[index].startTime);
		$("#editAmForm_endTime").val(arr.rows[index].endTime);
		if(gradientType == 1 || gradientType == 2 || gradientType == 3 || gradientType == 4){
			$("#editAmForm_oneThreshold").val(arr.rows[index].oneThreshold);
			$("#editAmForm_oneReduce").val(arr.rows[index].oneReduce);
		}
		if(gradientType == 2 || gradientType == 3 || gradientType == 4){
			$("#editAmForm_twoThreshold").val(arr.rows[index].twoThreshold);
			$("#editAmForm_twoReduce").val(arr.rows[index].twoReduce);
		}
		if(gradientType == 3 || gradientType == 4){
			$("#editAmForm_threeThreshold").val(arr.rows[index].threeThreshold);
			$("#editAmForm_threeReduce").val(arr.rows[index].threeReduce);
		}
		if(gradientType == 4){
			$("#editAmForm_fourThreshold").val(arr.rows[index].fourThreshold);
			$("#editAmForm_fourReduce").val(arr.rows[index].fourReduce);
		}
		if(type == 2){
			$("#editAmForm_type1").prop('checked',true);
			$("#editAmForm_groupSale").val(typeId);
			$("#groupSaleSpan").show();
			$("#customSaleSpan").hide();
			$("#customPageSpan").hide();
		}else if(type == 3){
			$("#editAmForm_type2").prop('checked',true);
			var url = '${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id='+typeId;
			$("#editAmForm_customSale").combobox('reload',url);
			$("#groupSaleSpan").hide();
			$("#customSaleSpan").show();
			$("#customPageSpan").hide();
		}else if(type == 4){
			$("#editAmForm_type3").prop('checked',true);
			var url = '${rc.contextPath}/page/ajaxAppCustomPage?id='+typeId;
			$("#editAmForm_customPage").combobox('reload',url);
			$("#groupSaleSpan").hide();
			$("#customSaleSpan").hide();
			$("#customPageSpan").show();
		}
	    $("#editAmDiv").dialog('open');		
	}
	
	function clearEditAmForm(){
		$("#editAmForm_id").val('0');
		$("#editAmForm input[type='input']").each(function(){
			$(this).val('');
		});
		$("#editAmForm_customSale").combobox('clear');
		$("#editAmForm_customPage").combobox('clear');
	}
	$(function(){
		
		$('#editAmForm_type1').change(function(){
			if($(this).is(':checked')){
				$('#groupSaleSpan').show();
				$('#customSaleSpan').hide();
				$('#customPageSpan').hide();
			}
		});
		
		$('#editAmForm_type2').change(function(){
			if($(this).is(':checked')){
				$('#groupSaleSpan').hide();
				$('#customSaleSpan').show();
				$('#customPageSpan').hide();
			}
		});
		
		$('#editAmForm_type3').change(function(){
			if($(this).is(':checked')){
				$('#groupSaleSpan').hide();
				$('#customSaleSpan').hide();
				$('#customPageSpan').show();
			}
		});
		
		
		$("input[name='timeStatus']").each(function(){
			$(this).change(function(){
				searchCustom();
			});
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/activityManjian/jsonActivityManjianInfo',
            loadMsg:'正在装载数据...',
            queryParams:{
            	type:'${type}',
            	status:'${status}'
            },
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            columns:[[
				{field:'id',    title:'活动id', width:20,align:'center'},
            	{field:'availableDesc',    title:'是否可用', width:20, align:'center'},
                {field:'timeStatus',    title:'活动状态', width:30, align:'center'},
                {field:'startTime',    title:'开始时间', width:40, align:'center'},
                {field:'endTime',    title:'结束时间', width:40, align:'center'},
                {field:'name',    title:'活动名称', width:60, align:'center'},
                {field:'gradientTypeDesc',    title:'满减梯度', width:20, align:'center'},
                {field:'relationTypeDesc',    title:'关联类型', width:50, align:'center'},
                {field:'typeId',    title:'关联ID', width:20, align:'center'},
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                   		var a = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a>';
                       	var b = '';
                       	if(row.isAvailable == 0){
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 1 + ')">启用</a>'
                       	}else{
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 0 + ')">停用</a>'
                       	}
                       	return a+b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增',
                iconCls:'icon-add',
                handler:function(){
                	clearEditAmForm();
                	$('#editAmDiv').dialog('open');
                }
            }],
            pagination:true
        });

        $('#editAmDiv').dialog({
            title:'编辑',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                    $('#editAmForm').form('submit',{
                        url:"${rc.contextPath}/activityManjian/saveOrUpdate",
                        onSubmit:function(){
                        	var name = $("#editAmForm_name").val();
                        	var startTime = $("#editAmForm_startTime").val();
                        	var endTime = $("#editAmForm_endTime").val();
                        	var oneThreshold = $("#editAmForm_oneThreshold").val();
                        	var oneReduce = $("#editAmForm_oneReduce").val();
                        	var twoThreshold = $("#editAmForm_twoThreshold").val();
                        	var twoReduce = $("#editAmForm_twoReduce").val();
                        	var threeThreshold = $("#editAmForm_threeThreshold").val();
                        	var threeReduce = $("#editAmForm_threeReduce").val();
                        	var fourThreshold = $("#editAmForm_fourThreshold").val();
                        	var fourReduce = $("#editAmForm_fourReduce").val();
                        	var type = $("input[name='type']:checked").val();
                        	var groupSaleId = $("#editAmForm_groupSale").val();
                        	var customSaleId = $("#editAmForm_customSale").combobox('getValue');
                        	var customPageId = $("#editAmForm_customPage").combobox('getValue');
                        	var reg = /^\d+$/;
                        	
                        	if($.trim(name) == ''){
                        		$.messager.alert("提示","请输入活动名称","error");
        						return false;
                        	}else if($.trim(startTime) == ''){
                        		$.messager.alert("提示","请输入活动开始时间","error");
        						return false;
                        	}else if($.trim(endTime) == ''){
                        		$.messager.alert("提示","请输入活动结束时间","error");
        						return false;
                        	}else if(!reg.test(oneThreshold)){
                        		$.messager.alert("提示","梯度1满减金额必填且只能为数字","error");
        						return false;
                        	}else if(!reg.test(oneReduce)){
                        		$.messager.alert("提示","梯度1满减金额必填且只能为数字","error");
        						return false;
                        	}else if(parseInt(oneReduce)>parseInt(oneThreshold)){
                        		$.messager.alert("提示","梯度1满额必须大于减免金额","error");
        						return false;
                        	}else if(($.trim(twoThreshold) != '' && !reg.test(twoThreshold)) || ($.trim(twoReduce) != '' && !reg.test(twoReduce))){
                        		$.messager.alert("提示","梯度2满减金额只能为整数","error");
        						return false;
                        	}else if(($.trim(threeThreshold) != '' && !reg.test(threeThreshold)) || ($.trim(threeReduce) != '' && !reg.test(threeReduce))){
                        		$.messager.alert("提示","梯度3满减金额只能为整数","error");
        						return false;
                        	}else if(($.trim(fourThreshold) != '' && !reg.test(fourThreshold)) || ($.trim(fourReduce) != '' && !reg.test(fourReduce))){
                        		$.messager.alert("提示","梯度4满减金额只能为整数","error");
        						return false;
                        	}else if(type == null || type == '' || type == undefined){
                        		$.messager.alert("提示","请选择关联类型","error");
        						return false;
                        	}else if(type == 2 && $.trim(groupSaleId) == ''){
                        		$.messager.alert("提示","请输入组合ID","error");
        						return false;
                        	}else if(type == 3 && (customSaleId == '' || customSaleId == null || customSaleId == undefined)){
                        		$.messager.alert("提示","请选择自定义活动","error");
        						return false;
                        	}else if(type == 4 && (customPageId == '' || customPageId == null || customPageId == undefined)){
                        		$.messager.alert("提示","请选择原生自定义页面","error");
        						return false;
                        	}
                        	$.messager.progress();
                        },
                        success:function(data){
                        	$.messager.progress('close');
                            var res = eval("("+data+")");
                            if(res.status == 1){
                                $.messager.alert('响应信息',"保存成功",'info');
                                $('#s_data').datagrid('reload');
                                $('#s_data').datagrid('clearSelections');
                                $('#editAmDiv').dialog('close');
                            }else{
                                $.messager.alert('响应信息',res.msg,'error');
                            }
                        }
                    });
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editAmDiv').dialog('close');
                }
            }]
        });
        
        $("#editAmForm_customSale").combobox({
			url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode?type=2,4,6,12',
		    valueField:'code',   
		    textField:'text' 
		});
        
        $("#editAmForm_customPage").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage',
            valueField:'code',
            textField:'text'
        });
        
		$("#editAmForm_groupSale").change(function(){
			var id = $.trim($(this).val());
			if(id == ''){
				$("#editAmForm_groupSaleName").text('');
			}else{
				$.ajax({
	                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$("#editAmForm_groupSaleName").text(data.name + "-" + data.remark);
	                    }else{
	                    	$.messager.alert("提示",data.msg,"info");
	                    }
	                },
	                error: function(xhr){
	                	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	                }
	            });				
			}

		});
});
	
</script>

</body>
</html>