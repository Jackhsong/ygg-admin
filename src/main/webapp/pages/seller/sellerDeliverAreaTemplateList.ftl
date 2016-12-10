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
	.alignLeft{
		margin-left: 65px;
	}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>

<div data-options="region:'center',title:'content'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'商家配送地区模版管理',split:true" style="height: 120px;">
			<div style="height: 120px;padding: 15px">
				<table>
					<tr>
						<td>商家名称：</td>
						<td><input id="searchSellerId" name="searchSellerId" value="" /></td>
						<td>模版名称：</td>
						<td><input id="searchTemplateName" name="searchTemplateName" value="" /></td>
						<td>配送地区描述：</td>
						<td><input id="searchDeliverDesc" name="searchDeliverDesc" value="" /></td>
						<td>类型：</td>
						<td>
							<select id="searchType" name="searchType">
								<option value="0">全部</option>
								<option value="1">限制仅发货地区</option>
								<option value="2">限制不发货地区</option>
								<option value="3">限制仅发货省份中的不发货区县</option>
								<option value="4">限制不发货省份中的发货区县</option>
							</select>
						</td>
						<td>
							<a id="searchBtn" onclick="searchSeller()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>&nbsp;
							<a id="clearBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">重置</a>
						</td>
					</tr>
				</table>
    		</div>
    	</div>

		<div data-options="region:'center'" >
		      <table id="s_data" ></table>
		      
		      <!-- 新增配送地区模版begin -->
		      <div id="addTemplateDiv" class="easyui-dialog" style="width:600px;height:420px;padding:10px 10px;">
		      		<p>
		      			<span>商家名称：</span>
		      			<input type="text" name="sellerId" id="sellerId" style="width: 400px;"/>
		      		</p>
		      		<p>
		      			<span>模版名称：</span>
		      			<input type="hidden" name="templateId" id="templateId" value="0" />
		      			<input type="text" maxlength="30" id="templateName" name="templateName" style="width: 400px;"/>
		      		</p>
		      		<p>
		      			<span>限制类型：</span>
		      			<input type="radio" id="templateType1" name="templateType" value="1"/>仅发以下地区
		      			<input type="radio" id="templateType2" name="templateType" value='2'/>以下地区不发货
		      		</p>
		      		<p>
		      			<span>选择地区：</span>
		      			<input type="checkbox" name="huadong" id="huadong_all" value="-1" onclick="checkAll(this)"/><label for="huadong_all">华东</label>
		      		    <input type="checkbox" name="huadong" id="huadong_shanghai" value="310000"/><label for="huadong_shanghai">上海</label>
		      		    <input type="checkbox" name="huadong" id="huadong_jiangsu" value="320000"/><label for="huadong_jiangsu">江苏</label>
						<input type="checkbox" name="huadong" id="huadong_zhejiang" value="330000"/><label for="huadong_zhejiang">浙江</label>
                        <input type="checkbox" name="huadong" id="huadong_anhui" value="340000"/><label for="huadong_anhui">安徽</label>
                        <input type="checkbox" name="huadong" id="huadong_jiangxi" value="360000"/><label for="huadong_jiangxi">江西</label>
                        <br/>
                        <span class="alignLeft">
							<input type="checkbox" name="huabei" id="huabei_all" value="-1" onclick="checkAll(this)"/><label for="huabei_all">华北</label>
							<input type="checkbox" name="huabei" id="huabei_beijing" value="110000"/><label for="huabei_beijing">北京</label>
							<input type="checkbox" name="huabei" id="huabei_tianjin" value="120000"/><label for="huabei_tianjin">天津</label>
							<input type="checkbox" name="huabei" id="huabei_hebei" value="130000"/><label for="huabei_hebei">河北</label>
							<input type="checkbox" name="huabei" id="huabei_shandong" value="370000"/><label for="huabei_shandong">山东</label>
							<input type="checkbox" name="huabei" id="huabei_shanxi" value="140000"/><label for="huabei_shanxi">山西</label>
							<input type="checkbox" name="huabei" id="huabei_neimenggu" value="150000"/><label for="huabei_neimenggu">内蒙古</label>
						</span>
						<br/>
						<span class="alignLeft">
	                        <input type="checkbox" name="huazhong" id="huazhong_all" value="-1" onclick="checkAll(this)"/><label for="huazhong_all">华中</label>
	                        <input type="checkbox" name="huazhong" id="huazhong_henan" value="410000"/><label for="huazhong_henan">河南</label>
	                        <input type="checkbox" name="huazhong" id="huazhong_hubei" value="420000"/><label for="huazhong_hubei">湖北</label>
	                        <input type="checkbox" name="huazhong" id="huazhong_hunan" value="430000"/><label for="huazhong_hunan">湖南</label>
                        </span>
                        <br/>
                        <span class="alignLeft">
	                        <input type="checkbox" name="huanan" id="huanan_all" value="-1" onclick="checkAll(this)"/><label for="huanan_all">华南</label>
	                        <input type="checkbox" name="huanan" id="huanan_guangdong" value="440000"/><label for="huanan_guangdong">广东</label>
	                        <input type="checkbox" name="huanan" id="huanan_fujian" value="350000"/><label for="huanan_fujian">福建</label>
	                        <input type="checkbox" name="huanan" id="huanan_guangxi" value="450000"/><label for="huanan_guangxi">广西</label>
	                        <input type="checkbox" name="huanan" id="huanan_hainan" value="460000"/><label for="huanan_hainan">海南</label>
                        </span>
                        <br/>
                        <span class="alignLeft">
	                        <input type="checkbox" name="dongbei" id="dongbei_all" value="-1" onclick="checkAll(this)"/><label for="dongbei_all">东北</label>
	                        <input type="checkbox" name="dongbei" id="dongbei_liaoning" value="210000"/><label for="dongbei_liaoning">辽宁</label>
	                        <input type="checkbox" name="dongbei" id="dongbei_jilin" value="220000"/><label for="dongbei_jilin">吉林</label>
	                        <input type="checkbox" name="dongbei" id="dongbei_heilongjiang" value="230000"/><label for="dongbei_heilongjiang">黑龙江</label>
                        </span>
                        <br/>
                        <span class="alignLeft">
	                        <input type="checkbox" name="xibei" id="xibei_all" value="-1" onclick="checkAll(this)"/><label for="xibei_all">西北</label>
	                        <input type="checkbox" name="xibei" id="xibei_shanxi" value="610000"/><label for="xibei_shanxi">陕西</label>
	                        <input type="checkbox" name="xibei" id="xibei_gansu" value="620000"/><label for="xibei_gansu">甘肃</label>
	                        <input type="checkbox" name="xibei" id="xibei_qinghai" value="630000"/><label for="xibei_qinghai">青海</label>
	                        <input type="checkbox" name="xibei" id="xibei_ningxia" value="640000"/><label for="xibei_ningxia">宁夏</label>
	                        <input type="checkbox" name="xibei" id="xibei_xinjiang" value="650000"/><label for="xibei_xinjiang">新疆</label>
                        </span>
                        <br/>
                        <span class="alignLeft">
	                        <input type="checkbox" name="xinan" id="xinan_all" value="-1" onclick="checkAll(this)"/><label for="xinan_all">西南</label>
	                        <input type="checkbox" name="xinan" id="xinan_chongqin" value="500000"/><label for="xinan_chongqin">重庆</label>
	                        <input type="checkbox" name="xinan" id="xinan_sichuan" value="510000"/><label for="xinan_sichuan">四川</label>
	                        <input type="checkbox" name="xinan" id="xinan_guizhou" value="520000"/><label for="xinan_guizhou">贵州</label>
	                        <input type="checkbox" name="xinan" id="xinan_yunnan" value="530000"/><label for="xinan_yunnan">云南</label>
	                        <input type="checkbox" name="xinan" id="xinan_xizang" value="540000"/><label for="xinan_xizang">西藏</label>
                        </span>
		      		</p>
		      		<p>
		      			<span>例外地区：</span>
		      			<span>
		      				<a href="javascript:;" class="easyui-linkbutton" id="createArea">添加</a>&nbsp;
		      				<a href="javascript:;" class="easyui-linkbutton" id="removeArea">删除</a>
		      			</span>
		      			<div class="alignLeft" id="liwaiAreaList"></div>
		      		</p>
		      		<p>
		      			<span>地区描述：</span>
		      			<input name="templateDesc" id="templateDesc" value="" maxlength="50" style="width: 400px;"/>
		      		</p>
		      </div>
		      <!-- 新增配送地区模版end -->
		      
		      <!-- 显示配送地区模版begin -->
		      <div id="showTemplateDiv" class="easyui-dialog" style="width:600px;height:420px;padding:10px 10px;">
		      		<p>
		      			<span>商家名称：</span>
		      			<span id="show_sellerName"></span>
		      		</p>
		      		<p>
		      			<span>模版名称：</span>
		      			<span id="show_templateName"></span>
		      		</p>
		      		<p>
		      			<span>限制类型：</span>
		      			<span id="show_templateType"></span>
		      		</p>
		      		<p>
		      			<span>选择地区：</span>
		      			<span id="show_area"></span>
		      		</p>
		      		<p>
		      			<span>例外地区：</span>
		      			<span id="show_exceptArea"></span>
		      		</p>
		      		<p>
		      			<span>地区描述：</span>
		      			<span id="show_desc"></span>
		      		</p>
		      </div>
  			  <!-- 显示配送地区模版end -->
  			  
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
	
	function searchSeller(){
    	$('#s_data').datagrid('load',{
    		sellerId:$("#searchSellerId").combobox('getValue'),
        	name:$("#searchTemplateName").val(),
        	desc:$("#searchDeliverDesc").val(),
        	type:$("#searchType").val()
    	});
	}
	
	function clearSearch(){
		$("#searchSellerId").combobox('clear');
		$("#searchTemplateName").val('');
    	$("#searchDeliverDesc").val('');
    	$("#searchType").find("option").eq(0).attr("selected","selected");
    	$('#s_data').datagrid('load',{});
	}

	function clearEditForm(){
		$("#sellerId").combobox('clear');
		$("#templateId").val('0');
		$("#templateName").val('');
		$("input[name='templateType']").each(function(){
			$(this).prop('checked',false);
		});
		$("input[type='checkbox']").each(function(){
			$(this).prop('checked',false);
		});
		$("#liwaiAreaList").html('');
		$("#templateDesc").val('');
		$("#show_sellerName").text('');
		$("#show_templateName").text('');
		$("#show_templateType").text('');
		$("#show_area").text('');
		$("#show_exceptArea").text('');
		$("#show_desc").text('');
	}
	function editIt(id){
		clearEditForm();
		show(id);
		$('#addTemplateDiv').dialog('open');
	}

	function viewIt(id){
		clearEditForm();
		show(id);
		$('#showTemplateDiv').dialog('open');
	}
	
	function deleteId(id){
		$.messager.confirm('删除','确定删除吗',function(b){
			if(b){
				$.messager.progress();
				$.ajax({
					url:'${rc.contextPath}/sellerDeliverArea/delete/'+id,
					type:'post',
					success:function(data){
						$.messager.progress('close');
						if(data.status == 1){
							$.messager.alert("响应消息",'删除成功',function(){
								$("#s_data").datagrid('clearSelections');
								$("#s_data").datagrid('reload');
							});
						}else{
							$.messager.alert("响应消息",data.msg,'info');
						}
					},
					error:function(xhr){
						$.messager.progress('close');
			            $.messager.alert("响应消息",'服务器忙，请稍后再试，errorCode='+xhr.status,"info");
					}
				});
			}
		});
	}
	
	function show(id){
		$.ajax({
			url:'${rc.contextPath}/sellerDeliverArea/getSellerDeliverAreaTemplate/'+id,
			type:'post',
			data:{'id':id},
			success:function(data){
				
				//设置addTemplateDiv
				var areas = [];
				var exceptAreas = [];
				var url = '${rc.contextPath}/seller/jsonSellerCode?isAvailable=1&id='+ data.sellerId;
				$("#sellerId").combobox('reload',url);
				$("#templateId").val(data.id);
				$("#templateName").val(data.name);
				$("input[name='templateType'").each(function(){
					if($(this).val() == data.type){
						$(this).prop("checked",true);
					}
				});
				$("#templateDesc").val(data.desc);
				var areaCodes = data.areaCodes;
				var exceptAreaCodes = data.exceptAreaCodes;
				
				$("input[type='checkbox']").each(function(){
					var $val = $(this);
					$.each(areaCodes,function(index,area){
						if(area.provinceCode == $val.val()){
							$val.prop('checked',true);
						}
					});
				});
				
				$.each(areaCodes,function(index,area){
					areas.push(area.provinceName);
				});
				
				var options = '';
				$.each(exceptAreaCodes,function(index,area){
					var province = '<span><select class="province"><option value="' + area.provinceCode + '">' + area.provinceName + '</option></select>&nbsp;&nbsp;';
					var city  	 = '<select class="city"><option value="' + area.cityCode + '">' + area.cityName + '</option></select>&nbsp;&nbsp;';
					var district = '<select class="district"><option value="' + area.districtCode + '">' + area.districtName + '</option></select><br/><br/></span>';
					options += province + city + district;
					var address = '';
					if(area.provinceCode != 1){
						address += area.provinceName;
					}
					if(area.cityCode !=1){
						address += area.cityName;
					}
					if(area.districtCode !=1){
						address += area.districtName;
					}
					exceptAreas.push(address);
				});
				$('#liwaiAreaList').append(options);
				var size = $('#liwaiAreaList').find('span').length;
				$('#addTemplateDiv').dialog('resize',{
		    		width: 600,
		    		height: 450 + 40 * size
		    	});
				
				// 设置showTemplateDiv
				$("#show_sellerName").text(data.sellerName);
				$("#show_templateName").text(data.name);
				$("#show_templateType").text(data.type == 1?"仅以下地区发货":"以下地区不发货");
				$("#show_area").text(areas.join(','));
				$("#show_exceptArea").text(exceptAreas.join(','));
				$("#show_desc").text(data.desc);
			},
			error:function(xhr){
				$.messager.progress('close');
				var tip = '服务器忙，请稍后再试，errorCode='+xhr.status;
				if(xhr.status == 400){
					tip = '参数错误，请检查页面填写内容是否正确';
				}
	            $.messager.alert("提示",tip,"info");
			}
		});
	}
	

	function checkAll(e){
		var name = $(e).attr("name");
		if($(e).is(':checked')){
            $("input[name='"+name+"']").each(function(){
            	$(this).prop("checked",true);
            });
        }else{
        	$("input[name='"+name+"']").each(function(){
            	$(this).prop("checked",false);
            });
        }
	}

	$(function(){
		
		$('#searchSellerId').combobox({
			url: '${rc.contextPath}/seller/jsonSellerCode?isAvailable=1',
			valueField: 'code',
			textField: 'text'
		});
		
		$('#sellerId').combobox({
			url: '${rc.contextPath}/seller/jsonSellerCode?isAvailable=1',
			valueField: 'code',
			textField: 'text'
		});
		
		$('#addTemplateDiv').dialog({
            title:'配送地区设置',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var templateId = $("#templateId").val();
                	var sellerId = $("#sellerId").combobox('getValue');
                	var templateName = $.trim($("#templateName").val());
                	var templateType = $("input[name='templateType']:checked").val();
                	var templateDesc = $.trim($("#templateDesc").val());
                	var areaCodes = [];
                	var exceptionAreas = [];
                	var exceptionAreaSize = $('#liwaiAreaList').find('.province').length;
                	$("input[type=checkbox]").each(function(){
                		if($(this).is(":checked")){
                			if($(this).val() !=-1){
	                			areaCodes.push($(this).val());
                			}
                		}
                	});
                	$('#liwaiAreaList').find('.province').each(function(){
                		var provinceCode = $(this).val();
                		var cityCode = $(this).parent().find('.city').val();
                		var districtCode = $(this).parent().find('.district').val();
                		if(provinceCode !=0 && cityCode !=1){
                			exceptionAreas.push(provinceCode + ',' + cityCode + ',' + districtCode);
                		}
                	});
                	
                	if(sellerId == null || sellerId == '' || sellerId == undefined){
                		$.messager.alert("提示","请选择商家",'info');
                		return false;
                	}else if(templateName == ''){
                		$.messager.alert("提示","请输入模版名称",'info');
                		return false;
                	}else if(templateType == null || templateType == '' || templateType == undefined){
                		$.messager.alert("提示","请选择限制类型",'info');
                		return false;
                	}else if(areaCodes.length == 0){
                		$.messager.alert("提示","请选择地区",'info');
                		return false;
                	}else if(exceptionAreaSize >0 && exceptionAreas.length == 0){
                		$.messager.alert("提示","您选择了例外地区，但是没有选择具体的省市");
                		return false;
                	}else if(templateDesc == ''){
                		$.messager.alert("提示","输入配送地区描述");
                		return false;
            		}else{
            			$.messager.progress();
            			var param = {
            					'id':templateId,
            					'sellerId':sellerId,
            					'name':templateName,
            					'type':templateType,
            					'desc':templateDesc,
            					'areaCodes':areaCodes.join(','),
            					'other':exceptionAreas.join(';')
            			};
           				$.ajax({
           		            url: '${rc.contextPath}/sellerDeliverArea/saveOrUpdate',
           		            type: 'post',
           		            dataType: 'json',
           		            data: param,
           		            success: function(data){
           		            	$.messager.progress('close');
           		            	if(data.status == 1){
	           		            	$.messager.alert("提示",data.msg,"info",function(){
		           		            	$("#s_data").datagrid('reload');
	           		            		$('#addTemplateDiv').dialog('close');
	           		            	});
           		            	}else{
           		            		$.messager.alert("提示",data.msg,"info");
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
                    $('#addTemplateDiv').dialog('close');
                }
            }]
        });
		
		$("#showTemplateDiv").dialog({
            title:'配送地区查看',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'确认',
                iconCls:'icon-ok',
                handler:function(){
                	 $('#showTemplateDiv').dialog('close');
                }
            }]
        });
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/sellerDeliverArea/jsonSellerDeliverAreaTemplate',
            queryParams:{
            	sellerId:${sellerId!'0'}
            },
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            columns:[[
            	{field:'sellerId',    title:'商家Id', width:15, align:'center'},
				{field:'sellerName',    title:'商家名称', width:50, align:'center'},
                {field:'templateName',    title:'模版名称', width:50, align:'center'},
                {field:'templateType',    title:'模版类型', width:20, align:'center'},
                {field:'templateDesc',    title:'配送地区描述', width:70, align:'center'},
                {field:'extraPostage',    title:'加运费地区', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                        var a = '<a href="javaScript:;" onclick="viewIt(' + row.id + ')">查看</a> | ';
                        var b = '<a href="javaScript:;" onclick="editIt(' + row.id + ')">编辑</a> | ';
                        var c = '<a href="javaScript:;" onclick="deleteId(' + row.id + ')">删除</a><br/>';
                        var d = '<a href="${rc.contextPath}/sellerDeliverArea/extraPostage/'+row.id+'" target="_blank">偏远地区加运费</a>';
                        return a + b + c + d;
                    }
                }
            ]],
            toolbar:[
            {
                id:'_add',
                text:'新增配送地区模版',
                iconCls:'icon-add',
                handler:function(){
                	clearEditForm();
                	$('#addTemplateDiv').dialog('open');
                }
            },{
                id:'_edit',
                text:'设置通用偏远地区运费',
                iconCls:'icon-edit',
                handler:function(){
                	window.open("${rc.contextPath}/sellerDeliverArea/commonPostageList","_blank");
                }
            }],
            pagination:true
        });
		
		$('#createArea').click(function() {
			var flag = false;
			var options = '<span><select class="province newProvince"><option value="0">--省--</option>';
			$("input[type=checkbox]").each(
					function() {
						if ($(this).is(':checked')) {
							var $this = $(this);
							if ($this.val() != "-1") {
								flag = true;
								options += '<option value="' + $this.val() + '" >'
										+ $this.next("label").text() + '</option>';
							}
						}
			});
			options += '</select>&nbsp;&nbsp;<select class="city newCity"><option value="1">--市--</option></select>&nbsp;&nbsp;';
			options += '<select class="district newDistrict"><option value="1">--区/县--</option></select><br/><br/></span';
			if (flag) {
				$('#liwaiAreaList').append(options);
				var size = $('#liwaiAreaList').find('span').length;
				$('#addTemplateDiv').dialog('resize',{
		    		width: 600,
		    		height: 450 + 40 * size
		    	});
			} else {
				$.messager.alert('提示','请先选择配送地区，然后才能添加例外地区。 ','info');
				return;
			}
		});
		
		$('#removeArea').click(function() {
			$('#liwaiAreaList').find('span:last').remove();
			var size = $('#liwaiAreaList').find('span').length;
			$('#addTemplateDiv').dialog('resize',{
	    		width: 600,
	    		height: 450 + (40 * size) - 10 * size
	    	});
			return;
		});
		
		// 例外地区-----选择省
		$('#liwaiAreaList').on('change', '.newProvince', function() {
			var $that = $(this);
			var provinceCode = $(this).val();
			$.ajax({
				url : "${rc.contextPath}/area/loadAllCity",
				type : 'post',
				data : {
					'provinceId' : provinceCode
				},
				success : function(data) {
					var options = '<option value="1">--市--</option>';
					$.each(data, function(i) {
						options += '<option value="'+this.cityId+'" >' + this.name + '</option>';
					});
					$that.parent().find('.newCity').empty().append(options);
				}
			});
		});
		// 例外地区-----选择市
		$('#liwaiAreaList').on('change', '.newCity', function() {
			var $that = $(this);
			var cityCode = $(this).val();
			$.ajax({
				url : "${rc.contextPath}/area/loadAllDistrict",
				type : 'post',
				data : {
					'cityId' : cityCode
				},
				success : function(data) {
					var options = '<option value="1">--区/县--</option>';
					$.each(data, function(i) {
						options += '<option value="'+this.districtId+'" >' + this.name + '</option>';
					});
					$that.parent().find('.newDistrict').empty().append(options);
				}
			});
		});
		
		$("input[type=checkbox]").each(function(){
			$(this).change(function(){
				var options = '<option value="0">--省--</option>';
				$("input[type=checkbox]:checked").each(function(index,e){
					if ($(e).val() != "-1") {
						options += '<option value="' + $(e).val() + '" >'+$(e).next("label").text() + '</option>';
					}
				});
				$('#liwaiAreaList').find('.newProvince').each(function(index,e){
					$(e).empty();
					$(e).append(options);
				});
			});
		});
	});
</script>

</body>
</html>