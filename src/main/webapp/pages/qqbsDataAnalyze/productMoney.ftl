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
            <input type="hidden" name="type" value="1" />
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
销售金额</span>
	<br/><br/>
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
				<#if num1??><td ><#if r.tp1??>${r.tp1}</#if></td></#if>
				<#if num2??><td ><#if r.tp2??>${r.tp2}</#if></td></#if>
				<#if num3??><td ><#if r.tp3??>${r.tp3}</#if></td></#if>
				<#if num4??><td ><#if r.tp4??>${r.tp4}</#if></td></#if>
				<#if num5??><td ><#if r.tp5??>${r.tp5}</#if></td></#if>
				<#if num6??><td ><#if r.tp6??>${r.tp6}</#if></td></#if>
				<#if num7??><td ><#if r.tp7??>${r.tp7}</#if></td></#if>
				<#if num8??><td ><#if r.tp8??>${r.tp8}</#if></td></#if>
				<#if num9??><td ><#if r.tp9??>${r.tp9}</#if></td></#if>
				<#if num10??><td ><#if r.tp10??>${r.tp10}</#if></td></#if>
				<#if num11??><td ><#if r.tp11??>${r.tp11}</#if></td></#if>
				<#if num12??><td ><#if r.tp12??>${r.tp12}</#if></td></#if>
				<#if num13??><td ><#if r.tp13??>${r.tp13}</#if></td></#if>
				<#if num14??><td ><#if r.tp14??>${r.tp14}</#if></td></#if>
				<#if num15??><td ><#if r.tp15??>${r.tp15}</#if></td></#if>
				<#if num16??><td ><#if r.tp16??>${r.tp16}</#if></td></#if>
				<#if num17??><td ><#if r.tp17??>${r.tp17}</#if></td></#if>
				<#if num18??><td ><#if r.tp18??>${r.tp18}</#if></td></#if>
				<#if num19??><td ><#if r.tp19??>${r.tp19}</#if></td></#if>
				<#if num20??><td ><#if r.tp20??>${r.tp20}</#if></td></#if>
				<#if num21??><td ><#if r.tp21??>${r.tp21}</#if></td></#if>
				<#if num22??><td ><#if r.tp22??>${r.tp22}</#if></td></#if>
				<#if num23??><td ><#if r.tp23??>${r.tp23}</#if></td></#if>
				<#if num24??><td ><#if r.tp24??>${r.tp24}</#if></td></#if>
				<#if num25??><td ><#if r.tp25??>${r.tp25}</#if></td></#if>
				<#if num26??><td ><#if r.tp26??>${r.tp26}</#if></td></#if>
				<#if num27??><td ><#if r.tp27??>${r.tp27}</#if></td></#if>
				<#if num28??><td ><#if r.tp28??>${r.tp28}</#if></td></#if>
				<#if num29??><td ><#if r.tp29??>${r.tp29}</#if></td></#if>
				<#if num30??><td ><#if r.tp30??>${r.tp30}</#if></td></#if>
				<#if num31??><td ><#if r.tp31??>${r.tp31}</#if></td></#if>
				<td ><#if r.totalPrice??>${r.totalPrice}</#if></td>
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
				<#if num1??><td ><#if lastRow.tp1??>${lastRow.tp1}</#if></td></#if>
				<#if num2??><td ><#if lastRow.tp2??>${lastRow.tp2}</#if></td></#if>
				<#if num3??><td ><#if lastRow.tp3??>${lastRow.tp3}</#if></td></#if>
				<#if num4??><td ><#if lastRow.tp4??>${lastRow.tp4}</#if></td></#if>
				<#if num5??><td ><#if lastRow.tp5??>${lastRow.tp5}</#if></td></#if>
				<#if num6??><td ><#if lastRow.tp6??>${lastRow.tp6}</#if></td></#if>
				<#if num7??><td ><#if lastRow.tp7??>${lastRow.tp7}</#if></td></#if>
				<#if num8??><td ><#if lastRow.tp8??>${lastRow.tp8}</#if></td></#if>
				<#if num9??><td ><#if lastRow.tp9??>${lastRow.tp9}</#if></td></#if>
				<#if num10??><td ><#if lastRow.tp10??>${lastRow.tp10}</#if></td></#if>
				<#if num11??><td ><#if lastRow.tp11??>${lastRow.tp11}</#if></td></#if>
				<#if num12??><td ><#if lastRow.tp12??>${lastRow.tp12}</#if></td></#if>
				<#if num13??><td ><#if lastRow.tp13??>${lastRow.tp13}</#if></td></#if>
				<#if num14??><td ><#if lastRow.tp14??>${lastRow.tp14}</#if></td></#if>
				<#if num15??><td ><#if lastRow.tp15??>${lastRow.tp15}</#if></td></#if>
				<#if num16??><td ><#if lastRow.tp16??>${lastRow.tp16}</#if></td></#if>
				<#if num17??><td ><#if lastRow.tp17??>${lastRow.tp17}</#if></td></#if>
				<#if num18??><td ><#if lastRow.tp18??>${lastRow.tp18}</#if></td></#if>
				<#if num19??><td ><#if lastRow.tp19??>${lastRow.tp19}</#if></td></#if>
				<#if num20??><td ><#if lastRow.tp20??>${lastRow.tp20}</#if></td></#if>
				<#if num21??><td ><#if lastRow.tp21??>${lastRow.tp21}</#if></td></#if>
				<#if num22??><td ><#if lastRow.tp22??>${lastRow.tp22}</#if></td></#if>
				<#if num23??><td ><#if lastRow.tp23??>${lastRow.tp23}</#if></td></#if>
				<#if num24??><td ><#if lastRow.tp24??>${lastRow.tp24}</#if></td></#if>
				<#if num25??><td ><#if lastRow.tp25??>${lastRow.tp25}</#if></td></#if>
				<#if num26??><td ><#if lastRow.tp26??>${lastRow.tp26}</#if></td></#if>
				<#if num27??><td ><#if lastRow.tp27??>${lastRow.tp27}</#if></td></#if>
				<#if num28??><td ><#if lastRow.tp28??>${lastRow.tp28}</#if></td></#if>
				<#if num29??><td ><#if lastRow.tp29??>${lastRow.tp29}</#if></td></#if>
				<#if num30??><td ><#if lastRow.tp30??>${lastRow.tp30}</#if></td></#if>
				<#if num31??><td ><#if lastRow.tp31??>${lastRow.tp31}</#if></td></#if>
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