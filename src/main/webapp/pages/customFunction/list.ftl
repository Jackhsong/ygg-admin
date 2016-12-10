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
.search{
	width:1100px;
	align:center;
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
		<div data-options="region:'north',title:'自定义活动管理',split:true" style="height: 100px;">
			<div style="height: 40px;padding: 10px">
	            <table class="search">
	                <tr>
	                    <td class="searchName">功能入口备注：</td>
	                    <td class="searchText"><input id="searchRemark" name="searchRemark" value="" /></td>
	                    <td class="searchName">展现状态：</td>
	                	<td class="searchText">
	                		<select id="searchIsDisplay" name="searchIsDisplay">
	                			<option value="-1">全部</option>
	                			<option value="1">展现</option>
	                			<option value="0">不展现</option>
	                		</select>
	                	<td class="searchText">
							<a id="searchBtn" onclick="searchCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
							&nbsp;
							<a id="clearBtn" onclick="clearCustom()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
							&nbsp;
	                	</td>
	                </tr>
	            </table>
			</div>
		</div> 
		
		<div data-options="region:'center'" >
			<!--数据表格-->
			<table id="s_data" ></table>
			
			<!-- 编辑 -->
		    <div id="editCustomFunctionDiv" class="easyui-dialog" style="width:750px;height:700px;padding:20px 20px;">
		        <form id="editCustomFunctionForm" method="post">
					<input id="editCustomFunctionForm_id" type="hidden" name="id" value="0" >
					<p>
						<span>功能入口备注：</span>
						<span><input type="text" id="editCustomFunctionForm_remark" name="remark" style="width:300px"  maxlength="20"></span>
					</p>
					<hr/>
					<p>
						<span>第一张备注：</span>
						<span>
							<input type="text" id="editCustomFunctionForm_oneRemark" name="oneRemark" style="width:300px">
						</span>
					</p>
					<p>
						<span>第一张图片：</span>
						<span>
							<input type="text" name="oneImage" id="editCustomFunctionForm_oneImage" style="width:300px" maxlength="100">
							<a onclick="picDialogOpen('editCustomFunctionForm_oneImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						</span>
					</p>
					<p>
						<span>关联类型：</span>
						<span>
							<input type="radio" name="oneType" id="editCustomFunctionForm_oneType1" value="1"/>任意门
							<input type="radio" name="oneType" id="editCustomFunctionForm_oneType2" value="2"/>签到
							<input type="radio" name="oneType" id="editCustomFunctionForm_oneType3" value="3"/>热卖榜
							<input type="radio" name="oneType" id="editCustomFunctionForm_oneType4" value="4"/>原生自定义活动
							<input type="radio" name="oneType" id="editCustomFunctionForm_oneType5" value="5"/>网页自定义活动
							<input type="radio" name="oneType" id="editCustomFunctionForm_oneType6" value="6"/>邀请页面/新人专区
						</span>
					</p>
					<p id="oneType4Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_oneType4Id" name="onePageId" style="width:300px;"/>
						</span>
					</p>
					<p id="oneType5Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_oneType5Id" name="oneCustomActivityId" style="width:300px;"/>
						</span>
					</p>
					<hr/>				
					<p>
						<span>第二张备注：</span>
						<span>
							<input type="text" id="editCustomFunctionForm_twoRemark" name="twoRemark" style="width:300px">
						</span>
					</p>
					<p>
						<span>第二张图片：</span>
						<span>
							<input type="text" name="twoImage" id="editCustomFunctionForm_twoImage" style="width:300px" maxlength="100">
							<a onclick="picDialogOpen('editCustomFunctionForm_twoImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						</span>
					</p>
					<p>
						<span>关联类型：</span>
						<span>
							<input type="radio" name="twoType" id="editCustomFunctionForm_twoType1" value="1"/>任意门
							<input type="radio" name="twoType" id="editCustomFunctionForm_twoType2" value="2"/>签到
							<input type="radio" name="twoType" id="editCustomFunctionForm_twoType3" value="3"/>热卖榜
							<input type="radio" name="twoType" id="editCustomFunctionForm_twoType4" value="4"/>原生自定义活动
							<input type="radio" name="twoType" id="editCustomFunctionForm_twoType5" value="5"/>网页自定义活动
							<input type="radio" name="twoType" id="editCustomFunctionForm_twoType6" value="6"/>邀请页面/新人专区
						</span>
					</p>
					<p id="twoType4Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_twoType4Id" name="twoPageId" style="width:300px;"/>
						</span>
					</p>
					<p id="twoType5Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_twoType5Id" name="twoCustomActivityId" style="width:300px;"/>
						</span>
					</p>
					<hr/>					
					<p>
						<span>第三张备注：</span>
						<span>
							<input type="text" id="editCustomFunctionForm_threeRemark" name="threeRemark" style="width:300px">
						</span>
					</p>
					<p>
						<span>第三张图片：</span>
						<span>
							<input type="text" name="threeImage" id="editCustomFunctionForm_threeImage" style="width:300px" maxlength="100">
							<a onclick="picDialogOpen('editCustomFunctionForm_threeImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						</span>
					</p>
					<p>
						<span>关联类型：</span>
						<span>
							<input type="radio" name="threeType" id="editCustomFunctionForm_threeType1" value="1"/>任意门
							<input type="radio" name="threeType" id="editCustomFunctionForm_threeType2" value="2"/>签到
							<input type="radio" name="threeType" id="editCustomFunctionForm_threeType3" value="3"/>热卖榜
							<input type="radio" name="threeType" id="editCustomFunctionForm_threeType4" value="4"/>原生自定义活动
							<input type="radio" name="threeType" id="editCustomFunctionForm_threeType5" value="5"/>网页自定义活动
							<input type="radio" name="threeType" id="editCustomFunctionForm_threeType6" value="6"/>邀请页面/新人专区
						</span>
					</p>
					<p id="threeType4Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_threeType4Id" name="threePageId" style="width:300px;"/>
						</span>
					</p>
					<p id="threeType5Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_threeType5Id" name="threeCustomActivityId" style="width:300px;"/>
						</span>
					</p>
					<hr/>					
					<p>
						<span>第四张备注：</span>
						<span>
							<input type="text" id="editCustomFunctionForm_fourRemark" name="fourRemark" style="width:300px">
						</span>
					</p>
					<p>
						<span>第四张图片：</span>
						<span>
							<input type="text" name="fourImage" id="editCustomFunctionForm_fourImage" style="width:300px" maxlength="100">
							<a onclick="picDialogOpen('editCustomFunctionForm_fourImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						</span>
					</p>
					<p>
						<span>关联类型：</span>
						<span>
							<input type="radio" name="fourType" id="editCustomFunctionForm_fourType1" value="1"/>任意门
							<input type="radio" name="fourType" id="editCustomFunctionForm_fourType2" value="2"/>签到
							<input type="radio" name="fourType" id="editCustomFunctionForm_fourType3" value="3"/>热卖榜
							<input type="radio" name="fourType" id="editCustomFunctionForm_fourType4" value="4"/>原生自定义活动
							<input type="radio" name="fourType" id="editCustomFunctionForm_fourType5" value="5"/>网页自定义活动
							<input type="radio" name="fourType" id="editCustomFunctionForm_fourType6" value="6"/>邀请页面/新人专区
						</span>
					</p>
					<p id="fourType4Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_fourType4Id" name="fourPageId" style="width:300px;"/>
						</span>
					</p>
					<p id="fourType5Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_fourType5Id" name="fourCustomActivityId" style="width:300px;"/>
						</span>
					</p>
					<hr/>					
					<p>
						<span>第五张备注：</span>
						<span>
							<input type="text" id="editCustomFunctionForm_fiveRemark" name="fiveRemark" style="width:300px">
						</span>
					</p>
					<p>
						<span>第五张图片：</span>
						<span>
							<input type="text" name="fiveImage" id="editCustomFunctionForm_fiveImage" style="width:300px" maxlength="100">
							<a onclick="picDialogOpen('editCustomFunctionForm_fiveImage')" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						</span>
					</p>
					<p>
						<span>关联类型：</span>
						<span>
							<input type="radio" name="fiveType" id="editCustomFunctionForm_fiveType1" value="1"/>任意门
							<input type="radio" name="fiveType" id="editCustomFunctionForm_fiveType2" value="2"/>签到
							<input type="radio" name="fiveType" id="editCustomFunctionForm_fiveType3" value="3"/>热卖榜
							<input type="radio" name="fiveType" id="editCustomFunctionForm_fiveType4" value="4"/>原生自定义活动
							<input type="radio" name="fiveType" id="editCustomFunctionForm_fiveType5" value="5"/>网页自定义活动
							<input type="radio" name="fiveType" id="editCustomFunctionForm_fiveType6" value="6"/>邀请页面/新人专区
						</span>
					</p>
					<p id="fiveType4Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_fiveType4Id" name="fivePageId" style="width:300px;"/>
						</span>
					</p>
					<p id="fiveType5Id" style="display:none">
						<span>关联ID：&nbsp;&nbsp;</span>
						<span>
							<input type="text" id="editCustomFunctionForm_fiveType5Id" name="fiveCustomActivityId" style="width:300px;"/>
						</span>
					</p>
					<hr/>					
					<p>
						<span>是否展现：</span>
						<span>
							<input type="radio" id="editCustomFunctionForm_isShowInApp1" name="isDisplay" value="1"/>展现&nbsp;&nbsp;&nbsp;
							<input type="radio" id="editCustomFunctionForm_isShowInApp0" name="isDisplay" value="0" checked="checked"/>不展现
						</span>
					</p>
		    	</form>
		    </div>			 	    		
		</div>
	</div>
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
    	<input type="hidden" name="needWidth" id="needWidth" value="0">
        <input type="hidden" name="needHeight" id="needHeight" value="0">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
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

	function searchCustom(){
		$('#s_data').datagrid('load',{
			remark:$("#searchRemark").val(),
			isDisplay:$("#searchIsDisplay").val()
		});
	}
	
	function clearCustom(){
		$("#searchRemark").val('');
		$("#searchIsDisplay").find('option').eq(0).attr('selected','selected');
		$('#s_data').datagrid('load',{});
	}

	function displayId(id,isAvailable){
		var tip = "";
		if(isAvailable == 0){
			tip = "确定不展现吗？";
		}else{
			tip = "确定展现吗？";
		}
		$.messager.confirm("提示信息",tip,function(ra){
	        if(ra){
   	            $.messager.progress();
   	            $.ajax({
   	            	url:'${rc.contextPath}/customFunction/updateDisplayStatus',
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
		clearEditCustomFunctionForm();
	    var arr=$("#s_data").datagrid("getData");
	    $("#editCustomFunctionForm_id").val(arr.rows[index].id);
	    $("#editCustomFunctionForm_remark").val(arr.rows[index].remark);
	    
		$("#editCustomFunctionForm_oneRemark").val(arr.rows[index].oneRemark);
		$("#editCustomFunctionForm_oneImage").val(arr.rows[index].oneImage);
		$("input[name='oneType']").each(function(){
			if($(this).val() == arr.rows[index].oneType){
				$(this).prop('checked',true);
			}
		});
		if(arr.rows[index].oneType == 4){
			var url = '${rc.contextPath}/page/ajaxAppCustomPage?id='+arr.rows[index].oneId;
			$("#editCustomFunctionForm_oneType4Id").combobox('reload',url);
			$("#oneType4Id").show();
		}else if(arr.rows[index].oneType ==5){
			var url = '${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id='+arr.rows[index].oneId;
			$("#editCustomFunctionForm_oneType5Id").combobox('reload',url);
			$("#oneType5Id").show();
		}else{
			$("#editCustomFunctionForm_oneType4Id").combobox('clear');
			$("#editCustomFunctionForm_oneType5Id").combobox('clear');
			$("#oneType4Id").hide();
			$("#oneType5Id").hide();
		}
		
		$("#editCustomFunctionForm_twoRemark").val(arr.rows[index].twoRemark);
		$("#editCustomFunctionForm_twoImage").val(arr.rows[index].twoImage);
		$("input[name='twoType']").each(function(){
			if($(this).val() == arr.rows[index].twoType){
				$(this).prop('checked',true);
			}
		});
		if(arr.rows[index].twoType == 4){
			var url = '${rc.contextPath}/page/ajaxAppCustomPage?id='+arr.rows[index].twoId;
			$("#editCustomFunctionForm_twoType4Id").combobox('reload',url);
			$("#twoType4Id").show();
		}else if(arr.rows[index].twoType ==5){
			var url = '${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id='+arr.rows[index].twoId;
			$("#editCustomFunctionForm_twoType5Id").combobox('reload',url);
			$("#twoType5Id").show();
		}else{
			$("#editCustomFunctionForm_twoType4Id").combobox('clear');
			$("#editCustomFunctionForm_twoType5Id").combobox('clear');
			$("#twoType4Id").hide();
			$("#twoType5Id").hide();
		}
		
		$("#editCustomFunctionForm_threeRemark").val(arr.rows[index].threeRemark);
		$("#editCustomFunctionForm_threeImage").val(arr.rows[index].threeImage);
		$("input[name='threeType']").each(function(){
			if($(this).val() == arr.rows[index].threeType){
				$(this).prop('checked',true);
			}
		});
		if(arr.rows[index].threeType == 4){
			var url = '${rc.contextPath}/page/ajaxAppCustomPage?id='+arr.rows[index].threeId;
			$("#editCustomFunctionForm_threeType4Id").combobox('reload',url);
			$("#threeType4Id").show();
		}else if(arr.rows[index].threeType ==5){
			var url = '${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id='+arr.rows[index].threeId;
			$("#editCustomFunctionForm_threeType5Id").combobox('reload',url);
			$("#threeType5Id").show();
		}else{
			$("#editCustomFunctionForm_threeType4Id").combobox('clear');
			$("#editCustomFunctionForm_threeType5Id").combobox('clear');
			$("#threeType4Id").hide();
			$("#threeType5Id").hide();
		}
		
		$("#editCustomFunctionForm_fourRemark").val(arr.rows[index].fourRemark);
		$("#editCustomFunctionForm_fourImage").val(arr.rows[index].fourImage);
		$("input[name='fourType']").each(function(){
			if($(this).val() == arr.rows[index].fourType){
				$(this).prop('checked',true);
			}
		});
		if(arr.rows[index].fourType == 4){
			var url = '${rc.contextPath}/page/ajaxAppCustomPage?id='+arr.rows[index].fourId;
			$("#editCustomFunctionForm_fourType4Id").combobox('reload',url);
			$("#fourType4Id").show();
		}else if(arr.rows[index].fourType ==5){
			var url = '${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id='+arr.rows[index].fourId;
			$("#editCustomFunctionForm_fourType5Id").combobox('reload',url);
			$("#fourType5Id").show();
		}else{
			$("#editCustomFunctionForm_fourType4Id").combobox('clear');
			$("#editCustomFunctionForm_fourType5Id").combobox('clear');
			$("#fourType4Id").hide();
			$("#fourType5Id").hide();
		}
		
		$("#editCustomFunctionForm_fiveRemark").val(arr.rows[index].fiveRemark);
		$("#editCustomFunctionForm_fiveImage").val(arr.rows[index].fiveImage);
		$("input[name='fiveType']").each(function(){
			if($(this).val() == arr.rows[index].fiveType){
				$(this).prop('checked',true);
			}
		});
		if(arr.rows[index].fiveType == 4){
			var url = '${rc.contextPath}/page/ajaxAppCustomPage?id='+arr.rows[index].fiveId;
			$("#editCustomFunctionForm_fiveType4Id").combobox('reload',url);
			$("#fiveType4Id").show();
		}else if(arr.rows[index].fiveType ==5){
			var url = '${rc.contextPath}/customActivities/jsonCustomActivitiesCode?id='+arr.rows[index].fiveId;
			$("#editCustomFunctionForm_fiveType5Id").combobox('reload',url);
			$("#fiveType5Id").show();
		}else{
			$("#editCustomFunctionForm_fiveType4Id").combobox('clear');
			$("#editCustomFunctionForm_fiveType5Id").combobox('clear');
			$("#fiveType4Id").hide();
			$("#fiveType5Id").hide();
		}
		
		$("input[name='isDisplay']").each(function(){
			if($(this).val() == arr.rows[index].isDisplay){
				$(this).prop('checked',true);
			}
		});
	    $("#editCustomFunctionDiv").dialog('open');		
	}
	
	function clearEditCustomFunctionForm(){
		$("#editCustomFunctionForm_id").val('');
		$("#editCustomFunctionForm input[type='input']").each(function(){
			$(this).val('');
		});
		$("#editCustomFunctionForm input[type='radio']").each(function(){
			$(this).prop('checked',false);
		});
		$("input[id$='Type4Id']").each(function(){
			$(this).combobox('clear');
		});
		
		$("input[id$='Type5Id']").each(function(){
			$(this).combobox('clear');
		});
	}
	$(function(){
		
		$('#editCustomFunctionForm_oneType1').change(function(){
			if($(this).is(':checked')){
				$('#oneType4Id').hide();
				$('#oneType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_oneType2').change(function(){
			if($(this).is(':checked')){
				$('#oneType4Id').hide();
				$('#oneType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_oneType3').change(function(){
			if($(this).is(':checked')){
				$('#oneType4Id').hide();
				$('#oneType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_oneType4').change(function(){
			if($(this).is(':checked')){
				$('#oneType4Id').show();
				$('#oneType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_oneType5').change(function(){
			if($(this).is(':checked')){
				$('#oneType4Id').hide();
				$('#oneType5Id').show();
			}
		});
		$('#editCustomFunctionForm_oneType6').change(function(){
			if($(this).is(':checked')){
				$('#oneType4Id').hide();
				$('#oneType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_twoType1').change(function(){
			if($(this).is(':checked')){
				$('#twoType4Id').hide();
				$('#twoType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_twoType2').change(function(){
			if($(this).is(':checked')){
				$('#twoType4Id').hide();
				$('#twoType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_twoType3').change(function(){
			if($(this).is(':checked')){
				$('#twoType4Id').hide();
				$('#twoType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_twoType4').change(function(){
			if($(this).is(':checked')){
				$('#twoType4Id').show();
				$('#twoType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_twoType5').change(function(){
			if($(this).is(':checked')){
				$('#twoType4Id').hide();
				$('#twoType5Id').show();
			}
		});
		$('#editCustomFunctionForm_twoType6').change(function(){
			if($(this).is(':checked')){
				$('#twoType4Id').hide();
				$('#twoType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_threeType1').change(function(){
			if($(this).is(':checked')){
				$('#threeType4Id').hide();
				$('#threeType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_threeType2').change(function(){
			if($(this).is(':checked')){
				$('#threeType4Id').hide();
				$('#threeType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_threeType3').change(function(){
			if($(this).is(':checked')){
				$('#threeType4Id').hide();
				$('#threeType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_threeType4').change(function(){
			if($(this).is(':checked')){
				$('#threeType4Id').show();
				$('#threeType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_threeType5').change(function(){
			if($(this).is(':checked')){
				$('#threeType4Id').hide();
				$('#threeType5Id').show();
			}
		});
		$('#editCustomFunctionForm_threeType6').change(function(){
			if($(this).is(':checked')){
				$('#threeType4Id').hide();
				$('#threeType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fourType1').change(function(){
			if($(this).is(':checked')){
				$('#fourType4Id').hide();
				$('#fourType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fourType2').change(function(){
			if($(this).is(':checked')){
				$('#fourType4Id').hide();
				$('#fourType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fourType3').change(function(){
			if($(this).is(':checked')){
				$('#fourType4Id').hide();
				$('#fourType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fourType4').change(function(){
			if($(this).is(':checked')){
				$('#fourType4Id').show();
				$('#fourType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fourType5').change(function(){
			if($(this).is(':checked')){
				$('#fourType4Id').hide();
				$('#fourType5Id').show();
			}
		});
		$('#editCustomFunctionForm_fourType6').change(function(){
			if($(this).is(':checked')){
				$('#fourType4Id').hide();
				$('#fourType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fiveType1').change(function(){
			if($(this).is(':checked')){
				$('#fiveType4Id').hide();
				$('#fiveType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fiveType2').change(function(){
			if($(this).is(':checked')){
				$('#fiveType4Id').hide();
				$('#fiveType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fiveType3').change(function(){
			if($(this).is(':checked')){
				$('#fiveType4Id').hide();
				$('#fiveType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fiveType4').change(function(){
			if($(this).is(':checked')){
				$('#fiveType4Id').show();
				$('#fiveType5Id').hide();
			}
		});
		$('#editCustomFunctionForm_fiveType5').change(function(){
			if($(this).is(':checked')){
				$('#fiveType4Id').hide();
				$('#fiveType5Id').show();
			}
		});
		$('#editCustomFunctionForm_fiveType6').change(function(){
			if($(this).is(':checked')){
				$('#fiveType4Id').hide();
				$('#fiveType5Id').hide();
			}
		});

		$("input[id$='Type4Id']").each(function(){
			$(this).combobox({
				url:'${rc.contextPath}/page/ajaxAppCustomPage',   
			    valueField:'code',   
			    textField:'text' 
			});
		});
		
		$("input[id$='Type5Id']").each(function(){
			$(this).combobox({
				url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode',   
			    valueField:'code',   
			    textField:'text' 
			});
		});
		
		<!--加载楼层商品列表 begin-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customFunction/jsonCustomFunctionInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            columns:[[
				{field:'id',    title:'序号', align:'center',checkbox:true},
            	{field:'isDisplay',    title:'展现状态', width:30, align:'center',
					formatter:function(value,row,index){
						if(row.isDisplay == 1){
							return '展现';
						}else{
							return '不展现';
						}
					}	
            	},
                {field:'remark',    title:'功能入口备注', width:80, align:'center'},
                {field:'oneRemark',    title:'第一张备注', width:80, align:'center'},
                {field:'twoRemark',    title:'第二张备注', width:80, align:'center'},
                {field:'threeRemark',    title:'第三张备注', width:80, align:'center'},
                {field:'fourRemark',    title:'第四张备注', width:80, align:'center'},
                {field:'fiveRemark',    title:'第五张备注', width:80, align:'center'},
                {field:'image',    title:'图片显示', width:80, align:'center',
                	formatter:function(value,row,index){
                		var a = '<a href="'+row.oneImage+'" target="_blank"><img alt="" src="'+row.oneImage+'" style="max-width:40px"/></a> '
                		var b = '<a href="'+row.twoImage+'" target="_blank"><img alt="" src="'+row.twoImage+'" style="max-width:40px"/></a> '
                		var c = '<a href="'+row.threeImage+'" target="_blank"><img alt="" src="'+row.threeImage+'" style="max-width:40px"/></a> '
                		var d = '<a href="'+row.fourImage+'" target="_blank"><img alt="" src="'+row.fourImage+'" style="max-width:40px"/></a>'
                		var e = '';
                		if($.trim(row.fiveImage) !=''){
                			e = '<a href="'+row.fiveImage+'" target="_blank"><img alt="" src="'+row.fiveImage+'" style="max-width:40px"/></a>'
                		}
                		return a + b + c + d + e;
                	}	
                },
                {field:'hidden',  title:'操作', width:30,align:'center',
                    formatter:function(value,row,index){
                   		var a = '<a href="javascript:;" onclick="editIt('+ index + ')">编辑</a>';
                       	var b = '';
                       	if(row.isDisplay == 0){
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 1 + ')">展现</a>'
                       	}else{
                       		b = ' | <a href="javaScript:;" onclick="displayId(' + row.id + ',' + 0 + ')">不展现</a>'
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
                	clearEditCustomFunctionForm();
                	$('#editCustomFunctionDiv').dialog('open');
                }
            },'-',{
            	text:'批量展现',
                iconCls:'icon-edit',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                	if(rows.length>0){
                		var ids = [];
                        for(var i=0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                        displayId(ids.join(","),1);
                	}else{
                		$.messager.alert('提示','请选择要操作的行',"error");
                	}
                }
            },'-',{
            	text:'批量不展现',
                iconCls:'icon-edit',
                handler:function(){
                	var rows = $('#s_data').datagrid("getSelections");
                	if(rows.length>0){
                		var ids = [];
                        for(var i=0;i<rows.length;i++){
                            ids.push(rows[i].id);
                        }
                        displayId(ids.join(","),0);
                	}else{
                		$.messager.alert('提示','请选择要操作的行',"error");
                	}
                }
            }],
            pagination:true
        });

        $('#editCustomFunctionDiv').dialog({
            title:'编辑',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                	var remark = $.trim($("#editCustomFunctionForm_remark").val());
                	var oneRemark = $.trim($("#editCustomFunctionForm_oneRemark").val());
                	var oneImage = $.trim($("#editCustomFunctionForm_oneImage").val());
                	var oneType = $("input[name='oneType']:checked").val();
					var oneType4Id = $("#editCustomFunctionForm_oneType4Id").combobox('getValue');
					var oneType5Id = $("#editCustomFunctionForm_oneType5Id").combobox('getValue');
                	var twoRemark = $.trim($("#editCustomFunctionForm_twoRemark").val());
                	var twoImage = $.trim($("#editCustomFunctionForm_twoImage").val());
                	var twoType = $("input[name='twoType']:checked").val();
                	var twoType4Id = $("#editCustomFunctionForm_twoType4Id").combobox('getValue');
					var twoType5Id = $("#editCustomFunctionForm_twoType5Id").combobox('getValue');
                	var threeRemark = $.trim($("#editCustomFunctionForm_threeRemark").val());
                	var threeImage = $.trim($("#editCustomFunctionForm_threeImage").val());
                	var threeType = $("input[name='threeType']:checked").val();
                	var threeType4Id = $("#editCustomFunctionForm_threeType4Id").combobox('getValue');
					var threeType5Id = $("#editCustomFunctionForm_threeType5Id").combobox('getValue');
                	var fourRemark = $.trim($("#editCustomFunctionForm_fourRemark").val());
                	var fourImage = $.trim($("#editCustomFunctionForm_fourImage").val());
                	var fourType = $("input[name='fourType']:checked").val();
                	var fourType4Id = $("#editCustomFunctionForm_fourType4Id").combobox('getValue');
					var fourType5Id = $("#editCustomFunctionForm_fourType5Id").combobox('getValue');
                	var fiveRemark = $.trim($("#editCustomFunctionForm_fiveRemark").val());
                	var fiveImage = $.trim($("#editCustomFunctionForm_fiveImage").val());
                	var fiveType = $("input[name='fiveType']:checked").val();
                	var fiveType4Id = $("#editCustomFunctionForm_fiveType4Id").combobox('getValue');
					var fiveType5Id = $("#editCustomFunctionForm_fiveType5Id").combobox('getValue');
                	if($.trim(remark) == ''){
                		$.messager.alert("提示","请输入功能入口备注","error");
						return false;
                	}else if($.trim(oneRemark) == ''){
                		$.messager.alert("提示","请输入第一张备注","error");
						return false;
                	}else if($.trim(oneImage) == ''){
                		$.messager.alert("提示","请上传第一张图片","error");
						return false;
                	}else if(oneType == null || oneType == '' || oneType == undefined){
                		$.messager.alert("提示","请选择第一张关联类型","error");
						return false;
                	}else if(oneType == 4 && (oneType4Id == null || oneType4Id == '' || oneType4Id == undefined)){
                		$.messager.alert("提示","请选择第一张原生自定义活动","error");
						return false;
                	}else if(oneType == 5 && (oneType5Id == null || oneType5Id == '' || oneType5Id == undefined)){
                		$.messager.alert("提示","请选择第一张网页自定义活动","error");
						return false;
                	}else if($.trim(twoRemark) == ''){
                		$.messager.alert("提示","请输入第二张备注","error");
						return false;
                	}else if($.trim(twoImage) == ''){
                		$.messager.alert("提示","请上传第二张图片","error");
						return false;
                	}else if(twoType == null || twoType == '' || twoType == undefined){
                		$.messager.alert("提示","请选择第二张关联类型","error");
						return false;
                	}else if(twoType == 4 && (twoType4Id == null || twoType4Id == '' || twoType4Id == undefined)){
                		$.messager.alert("提示","请选择第二张原生自定义活动","error");
						return false;
                	}else if(twoType == 5 && (twoType5Id == null || twoType5Id == '' || twoType5Id == undefined)){
                		$.messager.alert("提示","请选择第二张网页自定义活动","error");
						return false;
                	}else if($.trim(threeRemark) == ''){
                		$.messager.alert("提示","请输入第三张备注","error");
						return false;
                	}else if($.trim(threeImage) == ''){
                		$.messager.alert("提示","请上传第三张图片","error");
						return false;
                	}else if(threeType == null || threeType == '' || threeType == undefined){
                		$.messager.alert("提示","请选择第三张关联类型","error");
						return false;
                	}else if(threeType == 4 && (threeType4Id == null || threeType4Id == '' || threeType4Id == undefined)){
                		$.messager.alert("提示","请选择第三张原生自定义活动","error");
						return false;
                	}else if(threeType == 5 && (threeType5Id == null || threeType5Id == '' || threeType5Id == undefined)){
                		$.messager.alert("提示","请选择第三张网页自定义活动","error");
						return false;
                	}else if($.trim(fourRemark) == ''){
                		$.messager.alert("提示","请输入第四张备注","error");
						return false;
                	}else if($.trim(fourImage) == ''){
                		$.messager.alert("提示","请上传第四张图片","error");
						return false;
                	}else if(fourType == null || fourType == '' || fourType == undefined){
                		$.messager.alert("提示","请选择第四张关联类型","error");
						return false;
                	}else if(fourType == 4 && (fourType4Id == null || fourType4Id == '' || fourType4Id == undefined)){
                		$.messager.alert("提示","请选择第四张原生自定义活动","error");
						return false;
                	}else if(fourType == 5 && (fourType5Id == null || fourType5Id == '' || fourType5Id == undefined)){
                		$.messager.alert("提示","请选择第四张网页自定义活动","error");
						return false;
                	}else if(fiveImage == '' && fiveType == 4 && (fiveType4Id == null || fiveType4Id == '' || fiveType4Id == undefined)){
                		$.messager.alert("提示","请选择第五张原生自定义活动","error");
						return false;
                	}else if(fiveImage == '' && fiveType == 5 && (fiveType5Id == null || fiveType5Id == '' || fiveType5Id == undefined)){
                		$.messager.alert("提示","请选择第五张网页自定义活动","error");
						return false;
                	}else{
                		var params = {};
                    	params.id=$("#editCustomFunctionForm_id").val();
                    	params.remark=remark;
                    	params.oneRemark=oneRemark;
                    	params.oneImage=oneImage;
                    	params.oneType=oneType;
                    	if(oneType == 4){
                    		params.oneId=oneType4Id;
                    	}else if(oneType==5){
                    		params.oneId=oneType5Id;
                    	}else{
                    		params.oneId='0';
                    	}
                    	params.twoRemark=twoRemark;
                    	params.twoImage=twoImage;
                    	params.twoType=twoType;
                    	if(twoType == 4){
                    		params.twoId=twoType4Id;
                    	}else if(twoType==5){
                    		params.twoId=twoType5Id;
                    	}else{
                    		params.twoId='0';
                    	}
                    	params.threeRemark=threeRemark;
                    	params.threeImage=threeImage;
                    	params.threeType=threeType;
                    	if(threeType == 4){
                    		params.threeId=threeType4Id;
                    	}else if(threeType==5){
                    		params.threeId=threeType5Id;
                    	}else{
                    		params.threeId='0';
                    	}
                    	params.fourRemark=fourRemark;
                    	params.fourImage=fourImage;
                    	params.fourType=fourType;
                    	if(fourType == 4){
                    		params.fourId=fourType4Id;
                    	}else if(fourType==5){
                    		params.fourId=fourType5Id;
                    	}else{
                    		params.fourId='0';
                    	}
                    	params.fiveRemark=fiveRemark;
                    	params.fiveImage=fiveImage;
                    	params.fiveType=fiveType;
                    	if(fiveType == 4){
                    		if(fiveImage == ''){
                    			params.fiveId='0';
                    		}else{
	                    		params.fiveId=fiveType4Id;
                    		}
                    	}else if(fiveType==5){
                    		if(fiveImage == ''){
                    			params.fiveId='0';
                    		}else{
                    			params.fiveId=fiveType5Id;
                    		}
                    	}else{
                    		params.fiveId='0';
                    	}
                    	params.isDisplay=$("input[name='isDisplay']:checked").val();
                		$.messager.progress();
		    			$.ajax({
							url: '${rc.contextPath}/customFunction/saveOrUpdate',
							type: 'post',
							dataType: 'json',
							data: params,
							success: function(data){
								$.messager.progress('close');
								if(data.status == 1){
									$.messager.alert("提示","保存成功","info",function(){
										$('#s_data').datagrid("load");
										$('#editCustomFunctionDiv').dialog('close');
									});
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
                    $('#editCustomFunctionDiv').dialog('close');
                }
            }]
        });
});
	
    $(function(){
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