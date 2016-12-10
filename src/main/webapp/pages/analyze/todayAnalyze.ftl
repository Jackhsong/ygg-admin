<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script type="text/javascript">
$(function(){
	$("input[name='searchType']").each(function(){
		$(this).click(function(){
			$('#searchDivForm').attr("action", "todaySale").submit();
		});
	});
});

function exportAll() {
	$('#searchDivForm').attr("action", "exportTodaySale").submit();
}
</script>
</head>
<body class="easyui-layout">
<!-- 数据表格 -->
	<div style="height: 30px;margin-left: 20px;margin-top: 10px;margin-bottom: 10px;">
		<form id="searchDivForm" action="${rc.contextPath}/analyze/todaySale" method="post">
			<input type="radio" name="searchType" id="searchType1" value="1" <#if searchType?exists && (searchType==1)>checked="checked"</#if>/>只看今天10:00起&nbsp;&nbsp;&nbsp;&nbsp;
			<input type="radio" name="searchType" id="searchType2" value="2" <#if searchType?exists && (searchType==2)>checked="checked"</#if>/>查看在售期间累计&nbsp;&nbsp;
			<a id="searchBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a>
		</form>
	</div>
	<table class="table table-bordered table-striped" width="100%">
		<tr>
			<th >名称</th>
			<th >类型</th>
			<th >销售数量</th>
			<th >总金额</th>
			<th >剩余库存</th>
		</tr>
		<#list  rows as r >
			<tr>
				<td >
				<#if (r.detailUrl)??>
					<a href="${rc.contextPath}${r.detailUrl}">${r.name}</a>
				<#else>
					${r.name}
				</#if>
				</td>
				<td >${r.source}</td>
				<td >${r.saleTotalCount}</td>
				<td >${r.saleToalPrice}</td>
				<td >${r.totalStock}</td>
			</tr>
		</#list>
	</table>

</body>
</html>