<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
<script src="${rc.contextPath}/pages/js/Chart.min.js"></script>
</head>
<body class="easyui-layout">
<div data-options="region:'center',title:'content'" style="padding:5px;">
	<!-- 分析图表 -->
	<div style="width:100%;margin-bottom:35px">
        <label>${productName}</label>
        <label>${begin} - ${end}</label>
        <label style="font-size:20px;color:red">累计销量：${totalSales!"0"}</label>
        <form action="${rc.contextPath}/order/export" id="searchForm" method="post" >
		</form>
		<div>
			<canvas id="myChart" height="450" width="1600"></canvas>
		</div>
	</div>
	
</div>
<script>
		var lineChartData = {
			labels : ${labels},
			datasets : [
				{
					label: "历史销量",
					fillColor : "rgba(220,220,220,0.2)",
					strokeColor : "#EE2C2C",
					pointColor : "#EE2C2C",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(220,220,220,1)",
					data : ${data}
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
		    scaleSteps : 5,
		    
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
		    bezierCurve : true,

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