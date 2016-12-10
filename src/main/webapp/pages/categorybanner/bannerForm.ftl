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

<div data-options="region:'center',title:'添加Banner信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="savebannerWindow" action="${rc.contextPath}/categoryBanner/save"  method="post">
		<fieldset>
			<input type="hidden" value="${bannerWindow.id?c}" id="editId" name="editId" />
			<input type="hidden" value="${(modelId)!}" id="page2ModelId" name="page2ModelId" />
			<legend>Banner信息</legend>
			 着陆页类型:<input type="radio" name="type" id="type1" value="1" <#if bannerWindow.type?exists && (bannerWindow.type == 1 || bannerWindow.type == 4) >checked<#else>checked</#if>> 单品&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type"  id="type2" value="2" <#if bannerWindow.type?exists && (bannerWindow.type == 2) >checked</#if>> 组合&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type"  id="type3" value="3" <#if bannerWindow.type?exists && (bannerWindow.type == 3) >checked</#if>> 自定义活动&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type"  id="type5" value="5" <#if bannerWindow.type?exists && (bannerWindow.type == 5) >checked</#if>> 原生自定义页面&nbsp;&nbsp;&nbsp;
					<input type="radio" name="type"  id="type6" value="6" <#if bannerWindow.type?exists && (bannerWindow.type == 6) >checked</#if>> 图片点击不跳转<br/><br/>
    		 Banner排序值: <input type="text" id="sequence" name="sequence" style="width:300px;"  value="<#if bannerWindow.sequence?exists>${bannerWindow.sequence?c}</#if>" /><br/><br/>
    		 Banner描述: <input type="text" maxlength="20" id="desc" name="desc" style="width:300px;"  value="<#if bannerWindow.desc?exists>${bannerWindow.desc}</#if>" /><br/><br/>
			 Banner图片: <input type="text" id="image" name="image" style="width:300px;" value="<#if bannerWindow.image?exists>${bannerWindow.image}</#if>" />
            <a onclick="picDialogOpen('image')" href="javascript:;" class="easyui-linkbutton">上传图片</a><span style="color:red">必须（640x260）</span><br/>
			<span id="imageShow">
			<#if bannerWindow.image?exists>
                <img alt="" src="${bannerWindow.image}" style="min-width: 100px;">
			</#if>
            </span><br/><br/>

            <#--<input type="radio" <#if bannerWindow.saleTimeType?exists && (bannerWindow.saleTimeType == 1 || bannerWindow.saleTimeType == 0) >checked</#if> name="saleTimeType" id="saleTimeType_10" value="1">早10点场-->
            <#--<input type="radio" <#if bannerWindow.saleTimeType?exists && (bannerWindow.saleTimeType == 2) >checked</#if> name="saleTimeType" id="saleTimeType_20" value="2">晚8点场-->
            <#--<br><br>-->
            开始时间:<input value="<#if startTime?exists >${startTime}</#if>" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>&nbsp;&nbsp;&nbsp;
			 结束时间:<input value="<#if endTime?exists >${endTime}</#if>" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
			 <span style="color:red">必须</span><br/><br/>
			 
			 <!-- 特卖类型为单品时 -->
			<div id="product_div">
			 商品ID: <input type="number" id="product" name="product" style="width:300px;"  onblur="stringTrim(this);" value="<#if bannerWindow.displayId?exists && (bannerWindow.displayId != 0) && (bannerWindow.type == 1)>${bannerWindow.displayId?c}</#if>" />
			 <input style="width: 150px" type="button" onclick="startEditProduct()" value="修改单品销售时间"/>
			 <span id="productName" style="color:red"></span><br/><br/>
			 </div>
			
			 <!-- 特卖类型为组合特卖时 -->
			<div id="groupSale_div">
			 组合ID:
			 <input id="groupSale" type="number" name="groupSale" style="width:200px" onblur="stringTrim(this);" value="<#if bannerWindow.displayId?exists && (bannerWindow.displayId != 0) && (bannerWindow.type == 2)>${bannerWindow.displayId?c}</#if>" />
			 <input style="width: 150px" type="button" onclick="startEditProduct()" value="修改商品销售时间"/>
			 <span id="groupSaleName" style="color:red"></span>&nbsp;&nbsp;&nbsp;<span id="groupSaleRemark" style="color:red"></span><br/><br/>
			 </div>
			
			 <!-- 特卖类型为自定义活动 -->
			<div id="customSale_div">
			 自定义活动:
			 <input id="customSale" type="number" name="customSale" style="width:200px" onblur="stringTrim(this);" value="<#if bannerWindow.displayId?exists && (bannerWindow.displayId != 0) && (bannerWindow.type == 3)>${bannerWindow.displayId?c}</#if>" />
			 <span id="customSaleName" style="color:red"></span>&nbsp;&nbsp;&nbsp;<span id="customSaleRemark" style="color:red"></span><br/><br/>
			 </div>
			 
			  <!-- 特卖类型为自定义页面时 -->
			<div id="customPage_div">
			 原生自定义页面:<input type="text" id="customPage" name="customPage" style="width:300px"/>
			 </div> 
			
			 展现状态:
			 <input type="radio" <#if bannerWindow.isDisplay?exists && (bannerWindow.isDisplay == 1) >checked</#if> name="isDisplay" value="1">展现&nbsp;&nbsp;&nbsp;
			<input type="radio" <#if bannerWindow.isDisplay?exists && (bannerWindow.isDisplay == 0) >checked</#if> name="isDisplay" value="0"> 不展现<br/><br/>

			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
	<div id="editProduct" style="width:800px;height:650px;padding:20px 20px;">
		批量修改开始时间:<input value="" id="editStartTime" name="editStartTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})"/>&nbsp;&nbsp;&nbsp;
		批量修改结束时间:<input value="" id="editEndTime" name="editEndTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"/><br/><br/>
		<table id="s_data" >
		</table>
	</div>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <input type="hidden" name="needWidth" id="needWidth" value="640">
        <input type="hidden" name="needHeight" id="needHeight" value="260">
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
                            $("#imageShow").html("<img alt='' src='"+res.url+"' style='min-width: 100px;'/>");
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
            WdatePicker({dateFmt: 'yyyy-MM-dd 09:59:59',minDate:'#F{$dp.$D(\'startTime\',{d:1})}'})
        }else{
            WdatePicker({dateFmt: 'yyyy-MM-dd 19:59:59',minDate:'#F{$dp.$D(\'startTime\',{d:1})}'})
        }
    }

	function startEditProduct(){
		var type = $("input[name='type']:checked").val();
		var subjectId = "";
		if(type==1){
			subjectId = $('#product').val();
		}else if(type == 2){
			subjectId = $.trim($('#groupSale').val());
			//subjectId = $('#groupSale').combobox('getValue');
		}else if(type == 3){
			subjectId = $('#customSale').val();
		}/* else if(type == 4){
			subjectId = $('#customPage').val();
		} */
		if(subjectId != "" && subjectId != 0){
			$('#editStartTime').val($('#startTime').val());
            $('#editEndTime').val($('#endTime').val());
			$('#s_data').datagrid('load',{type:type,subjectId:subjectId});
			$('#editProduct').dialog('open');
		}else{
			if(type==1){
				$.messager.alert('提示','请先填写商品编码',"error")
			}else if(type == 2){
				$.messager.alert('提示','请填写组合特卖ID',"error")
			}else if(type == 3){
				$.messager.alert('提示','请先选择自定义活动~',"error")
			}/* else if(type == 4){
				$.messager.alert('提示','请先选择自定义页面',"error")
			} */else{
				$.messager.alert('提示','选择出错',"error")
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
			data: {productIds:row.id,startTime:row.startTime,endTime:row.endTime,type:1},
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
	
	function saverow(index){
    	$('#s_data').datagrid('endEdit', index);
	};

	function cancelrow(index){
    	$('#s_data').datagrid('cancelEdit', index);
	};
	
	$(function(){
		$('#type1').change(function(){
			if($('#type1').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').hide();
				$('#customPage_div').hide();
				$('#product_div').show();
			}
		});
		$('#type2').change(function(){
			if($('#type2').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').show();
				$('#customPage_div').hide();
				$('#product_div').hide();
			}
		});
		$('#type3').change(function(){
			if($('#type3').is(':checked')){
				$('#customSale_div').show();
				$('#groupSale_div').hide();
				$('#customPage_div').hide();
				$('#product_div').hide();
			}
		});
		$('#type5').change(function(){
			if($('#type5').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').hide();
				$('#customPage_div').show();
				$('#product_div').hide();
			}
		});
		$('#type6').change(function(){
			if($('#type6').is(':checked')){
				$('#customSale_div').hide();
				$('#groupSale_div').hide();
				$('#customPage_div').hide();
				$('#product_div').hide();
			}
		});
		
		$('#type1').change();
		$('#type2').change();
		$('#type3').change();
		$('#type5').change();
		$('#type6').change();

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
			var product = $("#product").val();
			var groupSale = $.trim($("#groupSale").val());
			var customSale = $("#customSale").val();
			var customPage = $("#customPage").combobox('getValue');
			var startTime = $("#startTime").val();
			var endTime = $("#endTime").val();
			if(type == 1 && $.trim(product) == ""){
				$.messager.alert('提示','请填写商品ID'); 
				return false;
			}
			else if(type == 2 && groupSale == ""){
				$.messager.alert('提示','请填写组合特卖ID');
				return false;
			}
			else if(type == 3 && $.trim(customSale) == ''){
				$.messager.alert('提示','请选择自定义活动-');
				return false;
			}
			else if(type == 5 && (customPage == '' || customPage == null || customPage == undefined)){
				$.messager.alert('提示','请选择自定义页面');
				return false;
			} 
			else if(startTime == '' || endTime == ''){
				$.messager.alert('提示','请选择开始结束时间');
				return false;
			}
			else{
                $('#savebannerWindow').submit();
				<#--var subjectId = "";-->
				<#--if(type==1){-->
					<#--subjectId = product;-->
				<#--}else if(type == 2){-->
					<#--subjectId = groupSale;-->
				<#--}else if(type == 3){-->
					<#--subjectId = customSale;-->
				<#--}else if(type == 5){-->
					<#--subjectId = customPage;-->
				<#--}else if(type == 6){-->
					<#--subjectId = '0';-->
				<#--}else{-->
					<#--subjectId = '';-->
				<#--}-->
				<#--$.messager.progress();-->
				<#--$.ajax({-->
				       <#--url: '${rc.contextPath}/banner/checkProductTime',-->
				       <#--type: 'post',-->
				       <#--dataType: 'json',-->
				       <#--data: {type: type,subjectId:subjectId},-->
				       <#--success: function(data){-->
				    	   <#--$.messager.progress('close');-->
				    	   <#--if(data.status == 1){-->
								<#--$('#savebannerWindow').submit();-->
							<#--}else{{-->
								<#--$.messager.alert('提示',data.msg,"error")-->
							<#--}}-->
				       <#--},-->
				       <#--error: function(xhr){-->
				         	<#--$.messager.progress('close');-->
				        	<#--$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");-->
				       <#--}-->
				<#--});-->
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
            pageSize:50,
            pageList:[50,60],
            columns:[[
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
                                	$.messager.progress();
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.ajax({ 
                 				       url: '${rc.contextPath}/indexSale/updateProductTime',
                 				       type: 'post',
                 				       dataType: 'json',
                 				       data: {productIds: ids.join(","),startTime:time_start,endTime:time_edit,type:1},
                 				       success: function(data){
                 				    	   $.messager.progress('close');
                 				    	  	if(data.status == 1){
												$('#s_data').datagrid('reload');
											}else{{
												$.messager.alert('提示','保存出错',"error")
											}}
                 				       },
                 				       error: function(xhr){
                 				         	$.messager.progress('close');
                 				        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                 				       }
                 					});
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
		})
		
		$('#product').change(function(){
			var id = $('#product').val();
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
    	
    	//$('#groupSale').combobox({
		//    url:'${rc.contextPath}/banner/jsonAcCommonCode?id=<#if id??>${id?c}<#else>${0}</#if>',   
		//    valueField:'product',   
		//    textField:'text'  
		//});
    	
    	$('#groupSale').change(function(){
			var id = $.trim($('#groupSale').val());
			$('#groupSaleName').text("");
			$('#groupSaleRemark').text("");
			if(id != ""){
				$.ajax({
	                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
	                type: 'post',
	                dataType: 'json',
	                data: {'id':id},
	                success: function(data){
	                    if(data.status == 1){
	                    	$('#groupSaleName').text("组合名称："+data.name);
	            			$('#groupSaleRemark').text("组合备注："+data.remark);
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
    	$('#groupSale').change();
    	
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
            url:'${rc.contextPath}/page/ajaxAppCustomPage?id=<#if id??>${id?c}<#else>${0}</#if>',
            valueField:'code',
            textField:'text'
        });
	});
</script>

</body>
</html>