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
		<div data-options="region:'north',title:'商品统计',split:true" style="height: 110px;">
			<div id="searchDiv" class="datagrid-toolbar" style="height: 70px;padding: 15px">
		        <form id="searchForm" action="${rc.contextPath}/channelProStatistic/exportChannelProStatistic" method="post">
		            <table>
		            	<tr>
		                 	<td class="searchText">
								时间：
								<input value="" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})"/>
								至
		 						<input value="" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTime\')}'})"/>
							</td>
		                    <td>
								&nbsp;<a id="searchBtn" onclick="searchBySelectDate()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
		                	</td>
		                	<td>
								&nbsp;<a id="exportBtn" onclick="exportChannelStatistic()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">导出结果</a>
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

	<!--按照日期获取渠道统计-->
	function searchBySelectDate(){
		$('#s_data').datagrid('load', {
			startTime:$('#startTime').val(),
			endTime:$('#endTime').val()
		});
	}
	
	<!--导出文件-->
	function exportChannelStatistic(){
		$('#searchForm').submit();
	}
	
	$(function() {
	    var columns = new Array();
	    var cols = new Array();
	    var colData = new Object();
	    $.post('${rc.contextPath}/channelProStatistic/getAllChannelName',"params",function(data){
	        if(data != null){
	        	var i = 0;
	        	console.debug(data.length);
	            $.each(data,function(){
	            	i++;
	                colData = new Object();
	                if(i==1){
	                  colData.title = this.productCode;
	               	  colData.field = 'productCode';
	                }else if(i==2){
	                  colData.title = this.productName;
	               	  colData.field = 'productName';
	                }else if(i==data.length){
	                  colData.title = this.totalPrice;
	               	  colData.field = 'totalPrice';
	                }else{
	                  var channelNameAndId = this.channelNameAndId.split("%");
	                  colData.title = channelNameAndId[0];
	               	  colData.field = 'channelId' + channelNameAndId[1];
	                }
	                colData.width = 50;
	                colData.align = 'center';
	                cols.push(colData);	
	                console.debug(colData,i);
	            });
	        };
	      	columns.push(cols);
	        <!--加载Grid-->
			$('#s_data').datagrid({
	            nowrap: false,
	            striped: true,
	            collapsible:true,
	            idField:'id',
	        	url:'${rc.contextPath}/channelProStatistic/jsonChannelProStatisticInfo',
	            loadMsg:'正在装载数据...',
	            singleSelect:false,
	            fitColumns:true,
	            remoteSort: true,
	            pageSize:50,
	            pageList:[50],
	            columns:columns,
	            pagination:true
	        });
		});
		
	});

</script>

</body>
</html>