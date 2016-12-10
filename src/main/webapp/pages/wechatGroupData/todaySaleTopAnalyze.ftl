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
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script src="${rc.contextPath}/pages/js/Chart.min.js"></script>
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
		<form id="searchDivForm" action="${rc.contextPath}/wechatGroupData/todaySaleTop" method="post">
			<table>
				<tr>
					<input type="hidden" name="selectDate" id="selectDate" >
					<td>查询日期：<span id="now">${selectDate}</span></td>
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="searchAll('${prev}')" href="javascript:;" class="easyui-linkbutton" >前一日</a></td>
					<td>&nbsp;&nbsp;<a id="searchBtn" onclick="searchAll('${next}')" href="javascript:;" class="easyui-linkbutton" >后一日</a></td>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;<a id="searchBtn" onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a>
					<span style="margin-left:50px;"><span style="color:red"> 红色 线条代表当前选择日期</span>，黑色代表前一天</span>
				</td>
				</tr>
			</table>
		<form>
	</div>
	<br/>
	
	<!-- 分析图表 -->
	<div style="width:100%;margin-bottom:35px">
		<span style="font-size:20px;color:red">今日累计：${nowTotal!"0"}</span>&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="font-size:20px;color:#1E90FF">昨日累计：${yesterdayTotal!"0"}</span><br><br>
		<div>
			<canvas id="myChart" height="450" width="1600"></canvas>
		</div>
	</div>
	
<!-- 数据表格 -->
	<table class="table table-bordered table-striped" width="100%">
		<tr>
			<th >拼团商品ID</th>
			<th >商品名称</th>
			<th >创建拼团数</th>
			<th >创建订单数</th>
			<th >创建金额</th>
	
		</tr>

	<#list  list as r >
			<tr>
				<td >${r.mwebGroupProductId}</td>
				<td >${r.name}</td>
				<td >${r.createGroupCount}</td>
				<td >${r.createOrderCount}</td>
				<td >${r.createRealPrice}</td>
	
			</tr>
  </#list>
		<tr>
				<td >总计</td>
			    <td >${totalProductCount}</td>
				<td >${totalCreateGroupCount}</td>
				<td >${totalCreateOrderCount}</td>
				<td >${totalCreateRealPrice}</td>
		</tr>
	</table>
	
</div>
<script>
function searchAll(day) {
	$("#selectDate").val(day);
	$('#searchDivForm').attr("action", "todaySaleTop").submit();
}

function exportAll(day) {
	$('#searchDivForm').attr("action", "exportTodaySaleTop").submit();
}
</script>
<script>
		var data1 = ${data1};
		var data2 = ${data2};
		var lineChartData = {
			labels : ["0:00","1:00","2:00","3:00","4:00","5:00","6:00","7:00","8:00","9:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00","24:00"],
			datasets : [
				{
					label: "昨日销售数据",
					fillColor : "rgba(220,220,220,0.2)",
					strokeColor : "#4F4F4F",
					pointColor : "#4F4F4F",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(220,220,220,1)",
					data : data1
				},
				{
					label: "今日销售数据",
					fillColor : "rgba(220,220,220,0.2)",
					strokeColor : "#EE2C2C",
					pointColor : "#EE2C2C",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(220,220,220,1)",
					data : data2
				}
			]

		}

	window.onload = function(){
		var ctx = document.getElementById("myChart").getContext("2d");
		window.myLine = new Chart(ctx).Line(lineChartData, {
			//如果是移动端则为true
			responsive: true,
			
			//Boolean - If we show the scale above the chart data			
		    scaleOverlay : true,
		    
		    //Boolean - If we want to override with a hard coded scale
		    scaleOverride : false,
		    
		    //** Required if scaleOverride is true **
		    //Number - The number of steps in a hard coded scale
		    scaleSteps : 100,
		    
		    //Number - The value jump in the hard coded scale
		    scaleStepWidth : 20,
		    
		    // Y 轴的起始值
		    scaleStartValue : null,

		    // Y/X轴的颜色
		    scaleLineColor : "rgba(0,0,0,.1)",
		    
		    // X,Y轴的宽度
		    scaleLineWidth : 1,

		    // 刻度是否显示标签, 即Y轴上是否显示文字
		    scaleShowLabels : true,
		    
		    // Y轴上的刻度,即文字
		    scaleLabel : "<%=value%>",
		    
		    // 字体
		    scaleFontFamily : "'Arial'",
		    
		    // 文字大小
		    scaleFontSize : 12,
		    
		    // 文字样式
		    scaleFontStyle : "normal",
		    
		    // 文字颜色
		    scaleFontColor : "#666",	
		    
		    // 是否显示网格
		    scaleShowGridLines : true,
		    
		    // 网格颜色
		    scaleGridLineColor : "rgba(0,0,0,.05)",
		    
		    // 网格宽度
		    scaleGridLineWidth : 2,	
		    
		    // 是否使用贝塞尔曲线? 即:线条是否弯曲
		    bezierCurve : false,
		    
		    // 是否显示点数
		    pointDot : true,
		    
		    // 圆点的大小
		    pointDotRadius : 4,
		    
		    // 圆点的笔触宽度, 即:圆点外层白色大小
		    pointDotStrokeWidth : 1,
		    
		    // 数据集行程
		    datasetStroke : false,
		    
		    // 线条的宽度, 即:数据集
		    datasetStrokeWidth : 2,
		    
		    // 是否填充数据集
		    datasetFill : true,
		    
		    // 是否执行动画
		    animation : true,

		    // 动画的时间
		    animationSteps : 40,
		    
		    // 动画的特效
		    animationEasing : "easeOutQuart",

		    // 动画完成时的执行函数
		    onAnimationComplete : null
		});
	}

</script>

</body>
</html>