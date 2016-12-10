<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"  />
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


<div data-options="region:'center',title:'content'" id="center" >
	<div id="searchQqbsAllRewardDiv" class="datagrid-toolbar" style="height: 40px;"> 
		<form id="searchForm">
			<table>
				<tr>
					<td>用户ID</td>
					<td><input id="accountId" name="accountId" value="" /></td>
					<td><a id="searchBtn" onclick="searchReward()" href="javascript:void(0)" class="easyui-linkbutton"  data-options="iconCls:'icon-search'" >查询</a></td>
				</tr>
			</table>
	</div>

    <table id="s_data" style=""></table>
    
</div>

<script>

	function searchReward() {
		var param = {accountId: $("#accountId").val()};
		$('#s_data').datagrid('load', {
			accountId:$("#accountId").val()
		});
	}

<!--加载Grid-->
	$(function(){
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            url:'${rc.contextPath}/qqbsNewGuyRewThr/getNewGuyRewardThr',
            fitColumns:true,
            remoteSort: true,
            rowStyler:function(index,row){
    					return 'height:30px';
  			},
            columns:[[
            	{field:'accountId',title:'账户ID', width:50, align:'center'},
            	{field:'nickName',title:'昵称', width:50, align:'center'},
                {field:'levelAllNum',title:'粉丝数量', width:50, align:'center'},
                {field:'levelOneNum',title:'一级粉丝数量', width:50, align:'center'},
                {field:'totalPrice',title:'销售额', width:50, align:'center'}
            ]],
        });
     });
</script>

</body>
</html>