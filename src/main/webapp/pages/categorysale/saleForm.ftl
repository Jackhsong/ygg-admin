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

<div data-options="region:'center',title:'添加特卖信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="saveSaleWindow" action="${rc.contextPath}/categorySale/save"  method="post">
		<fieldset>
			<input type="hidden" value="<#if saleWindow.id?exists && (saleWindow.id != 0)>${saleWindow.id?c}</#if>" id="editId" name="editId" />
			
			<input  value="${modelId}" type="hidden" id="modelId" name="modelId" />
			<input  value="${pageId}" type="hidden" id="pageId" name="pageId" />
			<input  value="${categoryFirstId}" type="hidden" id="categoryFirstId" name="categoryFirstId" />
			<legend>特卖信息</legend>
			 特卖类型:<input type="radio" name="type" <#if saleWindow.type?exists && (saleWindow.type == 1) >checked<#else>checked</#if> id="type1" value="1"> 单品&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type" <#if saleWindow.type?exists && (saleWindow.type == 2) >checked</#if> id="type2" value="2"> 组合&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type" <#if saleWindow.type?exists && (saleWindow.type == 3) >checked</#if> id="type3" value="3"> 自定义活动&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type" <#if saleWindow.type?exists && (saleWindow.type == 4) >checked</#if> id="type4" value="4"> 原生自定义页面<br/><br/>
    		 特卖名称: <input maxlength="20" id="name" name="name" style="width:300px;"  value="<#if saleWindow.name?exists>${saleWindow.name}</#if>" />
    		 <span style="color:red">必须</span><br/><br/>
    		 特卖描述: <input maxlength="20" id="desc" name="desc" style="width:300px;"  value="<#if saleWindow.desc?exists>${saleWindow.desc}</#if>" /><br/><br/>
			 <!-- 特卖图片(600X260):  -->
			 <input type="hidden" id="image" name="image" style="width:450px;" value="<#if saleWindow.image?exists>${saleWindow.image}</#if>" />
				<!-- <a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><span style="color:red">必须</span><br/><br/> -->
			<!--特卖图片(640X260): <input type="text" readonly id="newImage" name="newImage" style="width:450px;" value="<#if saleWindow.newImage?exists>${saleWindow.newImage}</#if>" />
			<a onclick="picDialogOpen('newImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a><span style="color:red">必须</span><br/>
            <span id="newImageShow">
    		<#if saleWindow.newImage?exists>
    			<img alt="" src="${saleWindow.newImage}" style="min-width: 100px;">
    		</#if>-->
    		</span><br/><br/>
            1.7 特卖图片(640X306): <input type="text" readonly id="newImage17" name="newImage17" style="width:450px;" value="<#if saleWindow.newImage17?exists>${saleWindow.newImage17}</#if>" />
            <a onclick="picDialogOpen('newImage17')" href="javascript:;" class="easyui-linkbutton">上传图片</a><span style="color:red">必须</span><br/>
			<span id="newImage17Show">
    		<#if saleWindow.newImage17?exists>
    			<img alt="" src="${saleWindow.newImage17}" style="min-width: 100px;">
    		</#if>
    		</span><br/><br/> 

			<input type="radio" <#if saleWindow.saleTimeType?exists && (saleWindow.saleTimeType == 1 || saleWindow.saleTimeType == 0) >checked</#if> name="saleTimeType" id="saleTimeType_10" value="1">早10点场
            <input type="radio" <#if saleWindow.saleTimeType?exists && (saleWindow.saleTimeType == 2) >checked</#if> name="saleTimeType" id="saleTimeType_20" value="2">晚8点场
            <br><br>
			 开始时间:<input value="<#if startTime?exists >${startTime}</#if>" id="startTime" name="startTime" onClick="doWPStart()"/>&nbsp;&nbsp;&nbsp;
			 结束时间:<input value="<#if endTime?exists >${endTime}</#if>" id="endTime" name="endTime" onClick="doWPEnd()"/>
			 <span style="color:red">必须</span><br/><br/>

			 <!-- 特卖类型为单品时 -->
			<div id="code_div">
			 商品ID: <input type="number" id="code" name="code" style="width:300px;"  onblur="stringTrim(this);" value="<#if saleWindow.displayId?exists && (saleWindow.displayId != 0)>${saleWindow.displayId?c}</#if>" />
			 <input id="editProductTime" style="width: 150px" type="button" onclick="startEditProduct()" value="修改单品销售时间"/>&nbsp;&nbsp;&nbsp;&nbsp;
			 <#if saleWindow.displayId?exists && (saleWindow.displayId != 0)>
				<a href="${rc.contextPath}/product/edit/1/${saleWindow.displayId?c}" target="_blank">编辑单品</a>
			</#if>
			<span id="productName" style="color:red"></span>
			 <br/><br/>
			 </div>
			
			 <!-- 特卖类型为组合特卖时 -->
			<div id="groupSale_div">
			 组合ID:
			 <input id="groupSale" type="number" name="groupSale" style="width:200px" onblur="stringTrim(this);" value="<#if saleWindow.displayId?exists && (saleWindow.displayId != 0)>${saleWindow.displayId?c}</#if>" />
			 <input style="width: 150px" type="button" onclick="startEditProduct()" value="修改商品销售时间"/>&nbsp;&nbsp;&nbsp;&nbsp;
			 <#if saleWindow.displayId?exists && (saleWindow.displayId != 0) >
				<a href="${rc.contextPath}/sale/groupSaleProductManage/${saleWindow.displayId?c}" target="_blank">管理组合特卖商品</a>
			 </#if>
			 <span id="groupSaleName" style="color:red"></span>
			 <br/><br/>
			 </div>
			
			 <!-- 特卖类型为自定义活动时 -->
			<div id="customSale_div">
			 自定义活动:
			 <input id="customSale" type="number" name="customSale" style="width:200px" onblur="stringTrim(this);" value="<#if saleWindow.displayId?exists && (saleWindow.displayId != 0)>${saleWindow.displayId?c}</#if>" />
			 <span id="customSaleName" style="color:red"></span>&nbsp;&nbsp;&nbsp;<span id="customSaleRemark" style="color:red"></span><br/><br/>
			 <!-- <input style="width: 150px" type="button" onclick="startEditProduct()" value="修改商品销售时间"/> -->
			 </div>
			 
			 <!-- 特卖类型为自定义页面时 -->
			<div id="customPage_div">
			 原生自定义页面:<input type="text" id="customPage" name="customPage" style="width:300px"/><br/><br/>
			 </div>
			 特卖展现状态:
			 <input type="radio" <#if saleWindow.isDisplay?exists && (saleWindow.isDisplay == 1) >checked</#if> name="isDisplay" value="1">展现&nbsp;&nbsp;&nbsp;
			<input type="radio" <#if saleWindow.isDisplay?exists && (saleWindow.isDisplay == 0) >checked</#if> name="isDisplay" value="0"> 不展现<br/><br/>

			特卖国家信息：<input id="saleFlagId" name="saleFlagId" value=""/><span style="color:red">&nbsp;必须</span><br/><br/>
			
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
	<div id="editProduct" style="width:800px;height:650px;padding:20px 20px;">
		批量修改开始时间:<input value="" id="editStartTime" name="editStartTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 10:00:00',maxDate:'#F{$dp.$D(\'endTime\')}'})"/>&nbsp;&nbsp;&nbsp;
		批量修改结束时间:<input value="" id="editEndTime" name="editEndTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 10:00:00',minDate:'#F{$dp.$D(\'startTime\')}'})"/><br/><br/>
		<table id="s_data" >
		</table>
	</div>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
		<input type="hidden" name="needWidth" id="needWidth" value="0">
        <input type="hidden" name="needHeight" id="needHeight" value="0">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script type="text/javascript">

	function stringTrim(obj){
		$(obj).val($.trim($(obj).val()));	
	}
	
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        })
    })

    var inputId;
    function picDialogOpen($inputId) {
        inputId = $inputId;
		if(inputId == 'newImage17'){
			$("#needWidth").val("640");
            $("#needHeight").val("306");
		}else{
            $("#needWidth").val("0");
            $("#needHeight").val("0");
		}
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
                            if(inputId == 'newImage'){
                            	$("#newImageShow").html("<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
                            }else if(inputId == 'newImage17'){
                            	$("#newImage17Show").html("<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
                            }
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

<script type="text/javascript">

	function doWPStart(){
        if($('#saleTimeType_10').is(':checked')){
        	WdatePicker({dateFmt: 'yyyy-MM-dd 10:00:00',maxDate:'#F{$dp.$D(\'endTime\')}'});
        }else{
            WdatePicker({dateFmt: 'yyyy-MM-dd 20:00:00',maxDate:'#F{$dp.$D(\'endTime\')}'});
		}
	}

	function doWPEnd(){
        if($('#saleTimeType_10').is(':checked')){
            WdatePicker({dateFmt: 'yyyy-MM-dd 10:00:00',minDate:'#F{$dp.$D(\'startTime\',{d:1})}'})
        }else{
            WdatePicker({dateFmt: 'yyyy-MM-dd 20:00:00',minDate:'#F{$dp.$D(\'startTime\',{d:1})}'})
        }
	}

	function startEditProduct(){
		var type = $("input[name='type']:checked").val();
		var subjectId = "";
		if(type==1){
			subjectId = $('#code').val();
		}else if(type == 2){
			subjectId = $('#groupSale').val();
		}else if(type == 3){
			subjectId = $('#customSale').val();
		}else if(type == 4){
			subjectId = $('#customPage').combobox('getValue');
		}else{
			subjectId = '0';
		}
		if(subjectId != ""){
			$('#editStartTime').val($('#startTime').val());
            $('#editEndTime').val($('#endTime').val());
			$('#s_data').datagrid('load',{type:type,subjectId:subjectId});
			$('#editProduct').dialog('open');
		}else{
			if(type==1){
				$.messager.alert('提示','请先填写商品编码',"error")
			}else if(type == 2){
				$.messager.alert('提示','请先选择组合特卖',"error")
			}else if(type == 3){
				$.messager.alert('提示','请先选择自定义活动',"error")
			}else if(type == 4){
				$.messager.alert('提示','请先选择原生自定义页面',"error")
			}
		}
	};
	
	function editrow(index){
    		$('#s_data').datagrid('beginEdit', index);
		};

	function updateActions(){
    	var rowcount = $('#s_data').datagrid('getRows').length;
    	for(var i=0; i<rowcount; i++){
        	$('#s_data').datagrid('updateRow',{
            	index:i,
            	row:{}
        	});
   		}
	}
	
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/indexSale/updateProductTime',
			type:"POST",
			data: {productIds:row.id,startTime:row.startTime,endTime:row.endTime,type:2,saleId:row.id},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('reload');
                    return
                } else{
                    $.messager.alert('响应信息',data.msg,'error',function(){
                        return
                    });
                }
			}
		});
	};
	
	function checkFormDate(type, subjectId){
		$.messager.progress();
		var name = $("#name").val();
		var desc = $("#desc").val();
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		$.post("${rc.contextPath}/indexSale/checkProductTime",
				{type: type,subjectId:subjectId,startTime:startTime,endTime:endTime},
				function(data){
					if(data.status == 1){
						if(!($.trim(name) == '' && $.trim(desc) == '')){
							$.messager.progress();
							$.post("${rc.contextPath}/indexSale/checkNameAndDescLength",
									{name: name,desc:desc},
									function(data1){
										if(data1.status != 1){
											$.messager.progress('close');
											$.messager.alert('提示',data1.msg,"error");
										}else{
											$('#saveSaleWindow').submit();
										}
									},
								"json");
						}
					}else{
						$.messager.progress('close');
						$.messager.alert('提示',data.msg,"error");
					}
				},
		"json");
	}
	
	function saverow(index){
    	$('#s_data').datagrid('endEdit', index);
	};

	function cancelrow(index){
    	$('#s_data').datagrid('cancelEdit', index);
	};
	
	$(function(){
		
		$("#saleFlagId").combobox({
			url:'${rc.contextPath}/flag/jsonSaleFlagCode?id=${flagId}',   
		    valueField:'code',   
		    textField:'text'  
		});
		
		$('#type1').change(function(){
			if($('#type1').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').hide();
				$('#customPage_div').hide();
				$('#code_div').show();
			}
		});
		$('#type2').change(function(){
			if($('#type2').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').show();
				$('#code_div').hide();
				$('#customPage_div').hide();
			}
		});
		$('#type3').change(function(){
			if($('#type3').is(':checked')){
				$('#customSale_div').show();
				$('#groupSale_div').hide();
				$('#code_div').hide();
				$('#customPage_div').hide();
			}
		});
		$('#type4').change(function(){
			if($('#type4').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').hide();
				$('#code_div').hide();
				$('#customPage_div').show();
			}
		});
		
		
		$("#name").blur(function(){
			var name = $(this).val();
			$(this).val($.trim(name));
		});
		
		$("#desc").blur(function(){
			var desc = $(this).val();
			$(this).val($.trim(desc));
		});
		
		$('#type1').change();
		$('#type2').change();
		$('#type3').change();
		$('#type4').change();

        $('#saleTimeType_10').change(function(){
            if($('#saleTimeType_10').is(':checked')){
				$("#startTime").val("");
                $("#endTime").val("");
            }
        });

        $('#saleTimeType_20').change(function(){
            if($('#saleTimeType_20').is(':checked')){
                $("#startTime").val("");
                $("#endTime").val("");
            }
        });

		$("#saveButton").click(function(){
			var type = $("input[name='type']:checked").val();
			/* var image = $("#image").val(); */
			var newImage = $("#newImage").val();
            var newImage17 = $("#newImage17").val();
			var code = $("#code").val();
			var groupSale = $('#groupSale').val();
			var customSale = $("#customSale").val();
			var customPage = $("#customPage").combobox('getValue');
			var name = $("#name").val();
			var desc = $("#desc").val();
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			var saleFlagId = $("#saleFlagId").combobox('getValue');
			var nameAndDescLength = name.length + desc.length;
			var categoryFirstId = true;
			$("select[name='categoryFirstId']").each(function(){
				if($(this).val()==0){
					categoryFirstId = false;
				}
			});
			if(type == 1 && $.trim(code) == ""){
				$.messager.alert('提示','请填写商品ID','warning'); 
			}else if(newImage == '' || newImage17 == ''){
				$.messager.alert('提示','请先上传特卖图片','warning');
			}else if(type == 2 && $.trim(groupSale) == ''){
				$.messager.alert('提示','请选择组合特卖','warning');
			}else if(type == 3 && $.trim(customSale) == ''){
				$.messager.alert('提示','请选择自定义活动','warning');
			}else if(type == 4 && (customPage == '' || customPage == null || customPage == undefined)){
				$.messager.alert('提示','请选择原生自定义页面','warning');
			}else if(startTime == '' || endTime == ''){
				$.messager.alert('提示','请选择开始结束时间','warning');
			}else if(nameAndDescLength > 40){
				$.messager.alert('提示','名称+描述不得超过40个字');
			}else if(!categoryFirstId){
				$.messager.alert('提示','请填写所属分类','warning');
			}else if( saleFlagId == '' || saleFlagId == null || saleFlagId==undefined){
				$.messager.alert('提示','请选择特卖国家信息','warning');
			}else{
				var subjectId = "";
				if(type==1){
					subjectId = code;
				}else if(type == 2){
					subjectId = groupSale;
				}else if(type == 3){
					subjectId = customSale;
				}else if(type == 4){
					subjectId = customPage;
				}else{
					subjectId = '0';
				}
				checkFormDate(type, subjectId); 
			}
			
    	});
    	
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/indexSale/jsonProductInfo',
            queryParams: {
				type: 0
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:30,
            pageList:[30,40],
            columns:[[
            	{field:'id',    title:'序号', width:50, align:'center',checkbox:true},
            	{field:'showId',    title:'商品ID', width:50, align:'center'},
                {field:'code',    title:'商品编码', width:70, align:'center'},
                {field:'name',    title:'商品名称', width:70, align:'center'},
                {field:'startTime',    title:'开始时间', width:70, align:'center', editor:{type:'datetimebox',options:{required:true}}},
                {field:'endTime',    title:'结束时间', width:70, align:'center', editor:{type:'datetimebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    		var f1 = '<a href="javascript:;" onclick="editrow(' + index + ')">编辑日期</a>';
                        	return f1;
                    	}
                    }
                }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions();
            	updateActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions();
        	},
        	toolbar:[{
                    iconCls: 'icon-add',
                    text:'批量修改商品时间',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        var time_start = $('#editStartTime').val();
                        var time_edit = $('#editEndTime').val();
                        if(rows.length < 1){
                        	$.messager.alert('提示','请选择要操作的商品',"error");
                        }
                        else if(time_start != "" && time_edit != ""){
                            $.messager.confirm('添加商品','请确认',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/indexSale/updateProductTime",
										{productIds: ids.join(","),startTime:time_start,endTime:time_edit,type:2,saleId:$("#editId").val()},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('reload');
											}else{{
												$.messager.alert('提示','保存出错',"error")
											}}
										},
									"json");
                                }
                     		})
                        }
                        else{
                        	$.messager.alert('提示','请先填写完开始和结束时间',"error");
                        }
                    }
                }],
            pagination:true,
            rownumbers:true
        });
    	
    	$('#editProduct').dialog({
    		title:'商品信息',
    		collapsible:true,
    		minimizable:true,
    		maximizable:true,
    		closed:true,
    		modal:true,
    		resizable:true,
    		buttons:[{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editProduct').dialog('close');
                }
            }]
		});
    	
		$('#code').change(function(){
			var id = $("#editId").val();
			var productId = $.trim($(this).val());
			if(productId !=''){
				$.ajax({
					url:'${rc.contextPath}/indexSale/checkSaleWindowRelation',
					type:'POST',
					data:{id:id,productId:productId},
					dataType:'json',
					beforeSend:function(xhr){
						$.messager.progress();
					},
					success: function(data) {
						if(data.status == 0){
				            $.messager.alert('响应信息',data.message,'error',function(){
				            	$(this).val('');
				            	$('#editProductTime').attr("disabled","disabled");
				            });
				        }else{
				        	$('#editProductTime').removeAttr("disabled");
				        }
					},
					error:function(xhr){
						$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
					},
					complete:function(xhr,ts){
						$.messager.progress('close');
					}
				});
			}
		});
		
		$('#code').change(function(){
			var id = $('#code').val();
			if(id == ""){
				$('#productName').text("");
			}else{
				$('#productName').text("");
				$.ajax({
	                url: '${rc.contextPath}/product/findProductInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#productName').text(data.msg);
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
		/* $('#groupSale').combobox({
		    url:'${rc.contextPath}/banner/jsonAcCommonCode?id=<#if id??>${id?c}<#else>${0}</#if>',   
		    valueField:'code',   
		    textField:'text'  
		}); */
		
    	$('#groupSale').change(function(){
			var id = $.trim($('#groupSale').val());
			$('#groupSaleName').text("");
			if(id != ""){
				$.ajax({
	                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#groupSaleName').text("组合名称："+data.name);
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
		
    	$('#customSale').change(function(){
			var id = $.trim($('#customSale').val());
			$('#customSaleName').text("");
			$('#customSaleRemark').text("");
			if(id != ""){
				$.ajax({
	                url: '${rc.contextPath}/customActivities/findCustomActivitiesInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#customSaleName').text("活动名称："+data.name);
	            			$('#customSaleRemark').text("活动备注："+data.remark);
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
    	
        $("#customPage").combobox({
            url:'${rc.contextPath}/page/ajaxAppCustomPage',
            valueField:'code',
            textField:'text'
        });
	});
</script>

</body>
</html>