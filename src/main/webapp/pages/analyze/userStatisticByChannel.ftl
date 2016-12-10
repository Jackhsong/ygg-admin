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

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'商品管理',split:true" style="height: 110px;">
			<div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/channelProManage/exportChannelPro" method="post">
		            <table class="search">
		            	<tr>
                            <td>注册时间：</td>
                            <td>
                                <input value="" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								~
		 						<input value="" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"/>
                            </td>
                            <td>
                                <a id="searchBtn" onclick="getUserStatistic()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                            </td>
		                </tr>
		            </table>
		        </form>
		    </div>
		 </div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
		</div>
	</div>
</div>

<script>

	<!--编辑数据-->
	function viewDetail(index){
		var arr  =$("#s_data").datagrid("getData");
		
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		var type = arr.rows[index].type;
		var platform = arr.rows[index].platform;
		var appChannel = arr.rows[index].appChannel;
		var registUserCount = arr.rows[index].registUserCount;
		var orderUserCount = arr.rows[index].orderUserCount;
		var totalPriceDisplay = arr.rows[index].totalPriceDisplay;
		
    	var url = '${rc.contextPath}/analyze/viewStatisticDetail?startTime='+startTime+'&endTime='+endTime+'&type='+type+'&platform='+platform+'&appChannel='+appChannel+'&registUserCount='+registUserCount+'&orderUserCount='+orderUserCount+'&totalPriceDisplay='+totalPriceDisplay;
    	window.open(url,'target=_blank');
	}
	
	
		<!--查询商品-->
	function getUserStatistic(){
		$('#s_data').datagrid('load', {
			startTime:$('#startTime').val(),
			endTime:$('#endTime').val()
		});
	}
	
	<!--加载Grid-->
	$(function(){
		var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    
	    var year = date.getFullYear();
	    var month = date.getMonth() + 1;
	    var strDate1 = date.getDate();
	    var strDate2 = date.getDate();
	    var seconds = date.getSeconds();
	    var minutes =  date.getMinutes();
	    var hours = date.getHours();
	    
    	strDate1 =  strDate1<10?"0" + strDate1:strDate1;
    	strDate2 =  strDate2<10?"0" + strDate2:strDate2;
    	month = month<10?"0" + month:month;
    	
		var currentdate1 = year + seperator1 + month + seperator1 + strDate1
            + " " + '00' + seperator2 + '00'
            + seperator2 + '00';
        var currentdate2 = year + seperator1 + month + seperator1 + strDate2
            + " " + '23' + seperator2 + '59'
            + seperator2 + '59';
        $('#startTime').val(currentdate1);
        $('#endTime').val(currentdate2);
        
        var params = {startTime:currentdate1,endTime:currentdate2};
        
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/analyze/jsonUserStatisticByChannelInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            queryParams:params,
            columns:[[
            	{field:'typeName',    title:'业务线', width:50, align:'center'},
                {field:'platform',    title:'平台', width:50, align:'center'},
                {field:'appChannelName',    title:'渠道', width:50, align:'center'},
                {field:'registUserCount',    title:'注册用户数', width:50, align:'center'},
                {field:'orderUserCount',    title:'订单用户数', width:50, align:'center'},
                {field:'totalPriceDisplay',    title:'累计成交金额', width:50, align:'center'},
                {field:'hidden',  title:'查看每日成交', width:80,align:'center',
                    formatter:function(value,row,index){
                 		var view = '<a href="javascript:;" onclick="viewDetail(' + index + ')">查看</a>';
                      	return view;
                    }
                }
            ]]
        });
	});
</script>

</body>
</html>