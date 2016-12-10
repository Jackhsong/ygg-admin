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
<link href="${rc.contextPath}/pages/css/table.css" rel="stylesheet" type="text/css" />
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<style>
	
</style>
</head>
<body class="easyui-layout">
<!-- 
<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
	<#include "../common/menu.ftl" >
</div>
 -->
<div data-options="region:'center',title:'content'" style="padding:5px;">
	
	<div id="searchDiv" class="datagrid-toolbar" style="height: 30px; padding: 15px">
		<form id="searchDivForm" action="${rc.contextPath}/analyze/product" method="post">
			<input type="hidden" name="isSearch" value="0" />
            <input type="hidden" name="type" value="0" />
			<table>
				<tr>
					<td>月份</td>
					<td><input value="${selectDate}" id="selectDate" name="selectDate" onClick="WdatePicker({dateFmt: 'yyyy-MM',minDate:'2015-03'})"/></td>
					<td>日期</td>
					<td>
						<select name="start" style="width:120px">
							<#list dateList as item>
								<option value="${item}" <#if item==start>selected</#if>>${item}</option>
							</#list>
						</select>
						日
						~
						<select name="stop"  style="width:120px">
							<#list dateList as item>
								<option value="${item}" <#if item==stop>selected</#if>>${item}</option>
							</#list>
						</select>
						日
					</td>
					
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="searchAll()" href="javascript:;" class="easyui-linkbutton" >查询</a></td>
					<td>&nbsp;&nbsp;<a id="exportBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a></td>
				</tr>
			</table>
		<form>
	</div>
	<br/>
	<span style="padding:10px">心动慈露
销售数量</span>
	<div>
	<br/>
	<table class="table table-bordered table-striped"  width="100%">
		<tr>
			<th >商品ID</th>
			<th >基本商品ID</th>
			<th >商品类型</th>
			<th >品牌</th>
			<th >一级分类</th>
			<th >编码</th>
			<th >短名</th>
			<th >商家</th>
			<th >发货地</th>
			<#if num1??><th >1日</th></#if>
			<#if num2??><th >2日</th></#if>
			<#if num3??><th >3日</th></#if>
			<#if num4??><th >4日</th></#if>
			<#if num5??><th >5日</th></#if>
			<#if num6??><th >6日</th></#if>
			<#if num7??><th >7日</th></#if>
			<#if num8??><th >8日</th></#if>
			<#if num9??><th >9日</th></#if>
			<#if num10??><th >10日</th></#if>
			<#if num11??><th >11日</th></#if>
			<#if num12??><th >12日</th></#if>
			<#if num13??><th >13日</th></#if>
			<#if num14??><th >14日</th></#if>
			<#if num15??><th >15日</th></#if>
			<#if num16??><th >16日</th></#if>
			<#if num17??><th >17日</th></#if>
			<#if num18??><th >18日</th></#if>
			<#if num19??><th >19日</th></#if>
			<#if num20??><th >20日</th></#if>
			<#if num21??><th >21日</th></#if>
			<#if num22??><th >22日</th></#if>
			<#if num23??><th >23日</th></#if>
			<#if num24??><th >24日</th></#if>
			<#if num25??><th >25日</th></#if>
			<#if num26??><th >26日</th></#if>
			<#if num27??><th >27日</th></#if>
			<#if num28??><th >28日</th></#if>
			<#if num29??><th >29日</th></#if>
			<#if num30??><th >30日</th></#if>
			<#if num31??><th >31日</th></#if>
			<th >总计</th>
		</tr>
		
		<#list  rows as r >
			<tr>
				<td >${r.productId}</td>
				<td >${r.productBaseId}</td>
				<td >${r.productType}</td>
				<td >${r.brandName}</td>
				<td >${r.categoryName}</td>
				<td >${r.code}</td>
				<td >${r.shortName}</td>
				<td >${r.sellerName}</td>
				<td >${r.sendAddress}</td>
				<#if num1??><td ><#if r.num1??>${r.num1?c}</#if></td></#if>
				<#if num2??><td ><#if r.num2??>${r.num2?c}</#if></td></#if>
				<#if num3??><td ><#if r.num3??>${r.num3?c}</#if></td></#if>
				<#if num4??><td ><#if r.num4??>${r.num4?c}</#if></td></#if>
				<#if num5??><td ><#if r.num5??>${r.num5?c}</#if></td></#if>
				<#if num6??><td ><#if r.num6??>${r.num6?c}</#if></td></#if>
				<#if num7??><td ><#if r.num7??>${r.num7?c}</#if></td></#if>
				<#if num8??><td ><#if r.num8??>${r.num8?c}</#if></td></#if>
				<#if num9??><td ><#if r.num9??>${r.num9?c}</#if></td></#if>
				<#if num10??><td ><#if r.num10??>${r.num10?c}</#if></td></#if>
				<#if num11??><td ><#if r.num11??>${r.num11?c}</#if></td></#if>
				<#if num12??><td ><#if r.num12??>${r.num12?c}</#if></td></#if>
				<#if num13??><td ><#if r.num13??>${r.num13?c}</#if></td></#if>
				<#if num14??><td ><#if r.num14??>${r.num14?c}</#if></td></#if>
				<#if num15??><td ><#if r.num15??>${r.num15?c}</#if></td></#if>
				<#if num16??><td ><#if r.num16??>${r.num16?c}</#if></td></#if>
				<#if num17??><td ><#if r.num17??>${r.num17?c}</#if></td></#if>
				<#if num18??><td ><#if r.num18??>${r.num18?c}</#if></td></#if>
				<#if num19??><td ><#if r.num19??>${r.num19?c}</#if></td></#if>
				<#if num20??><td ><#if r.num20??>${r.num20?c}</#if></td></#if>
				<#if num21??><td ><#if r.num21??>${r.num21?c}</#if></td></#if>
				<#if num22??><td ><#if r.num22??>${r.num22?c}</#if></td></#if>
				<#if num23??><td ><#if r.num23??>${r.num23?c}</#if></td></#if>
				<#if num24??><td ><#if r.num24??>${r.num24?c}</#if></td></#if>
				<#if num25??><td ><#if r.num25??>${r.num25?c}</#if></td></#if>
				<#if num26??><td ><#if r.num26??>${r.num26?c}</#if></td></#if>
				<#if num27??><td ><#if r.num27??>${r.num27?c}</#if></td></#if>
				<#if num28??><td ><#if r.num28??>${r.num28?c}</#if></td></#if>
				<#if num29??><td ><#if r.num39??>${r.num29?c}</#if></td></#if>
				<#if num30??><td ><#if r.num30??>${r.num30?c}</#if></td></#if>
				<#if num31??><td ><#if r.num31??>${r.num31?c}</#if></td></#if>
				<td ><#if r.totalNum??>${r.totalNum?c}</#if></td>
			</tr>
		</#list>
		<tr>
				<td >合计</td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
				<td ></td>
				<#if num1??><td ><#if lastRow.num1??>${lastRow.num1?c}</#if></td></#if>
				<#if num2??><td ><#if lastRow.num2??>${lastRow.num2?c}</#if></td></#if>
				<#if num3??><td ><#if lastRow.num3??>${lastRow.num3?c}</#if></td></#if>
				<#if num4??><td ><#if lastRow.num4??>${lastRow.num4?c}</#if></td></#if>
				<#if num5??><td ><#if lastRow.num5??>${lastRow.num5?c}</#if></td></#if>
				<#if num6??><td ><#if lastRow.num6??>${lastRow.num6?c}</#if></td></#if>
				<#if num7??><td ><#if lastRow.num7??>${lastRow.num7?c}</#if></td></#if>
				<#if num8??><td ><#if lastRow.num8??>${lastRow.num8?c}</#if></td></#if>
				<#if num9??><td ><#if lastRow.num9??>${lastRow.num9?c}</#if></td></#if>
				<#if num10??><td ><#if lastRow.num10??>${lastRow.num10?c}</#if></td></#if>
				<#if num11??><td ><#if lastRow.num11??>${lastRow.num11?c}</#if></td></#if>
				<#if num12??><td ><#if lastRow.num12??>${lastRow.num12?c}</#if></td></#if>
				<#if num13??><td ><#if lastRow.num13??>${lastRow.num13?c}</#if></td></#if>
				<#if num14??><td ><#if lastRow.num14??>${lastRow.num14?c}</#if></td></#if>
				<#if num15??><td ><#if lastRow.num15??>${lastRow.num15?c}</#if></td></#if>
				<#if num16??><td ><#if lastRow.num16??>${lastRow.num16?c}</#if></td></#if>
				<#if num17??><td ><#if lastRow.num17??>${lastRow.num17?c}</#if></td></#if>
				<#if num18??><td ><#if lastRow.num18??>${lastRow.num18?c}</#if></td></#if>
				<#if num19??><td ><#if lastRow.num19??>${lastRow.num19?c}</#if></td></#if>
				<#if num20??><td ><#if lastRow.num20??>${lastRow.num20?c}</#if></td></#if>
				<#if num21??><td ><#if lastRow.num21??>${lastRow.num21?c}</#if></td></#if>
				<#if num22??><td ><#if lastRow.num22??>${lastRow.num22?c}</#if></td></#if>
				<#if num23??><td ><#if lastRow.num23??>${lastRow.num23?c}</#if></td></#if>
				<#if num24??><td ><#if lastRow.num24??>${lastRow.num24?c}</#if></td></#if>
				<#if num25??><td ><#if lastRow.num25??>${lastRow.num25?c}</#if></td></#if>
				<#if num26??><td ><#if lastRow.num26??>${lastRow.num26?c}</#if></td></#if>
				<#if num27??><td ><#if lastRow.num27??>${lastRow.num27?c}</#if></td></#if>
				<#if num28??><td ><#if lastRow.num28??>${lastRow.num28?c}</#if></td></#if>
				<#if num29??><td ><#if lastRow.num29??>${lastRow.num29?c}</#if></td></#if>
				<#if num30??><td ><#if lastRow.num30??>${lastRow.num30?c}</#if></td></#if>
				<#if num31??><td ><#if lastRow.num31??>${lastRow.num31?c}</#if></td></#if>
				<td ></td>
			</tr>
		
	</table>

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

function searchAll() {
	$('input[name="isSearch"]').val("1");
	$('#searchDivForm').attr("action", "product").submit();
}

function exportAll() {
	$('input[name="isSearch"]').val("1");
	$('#searchDivForm').attr("action", "exportProductResult").submit();
}
</script>

</body>
</html>