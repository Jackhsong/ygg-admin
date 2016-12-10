$(function() {
	var loadQqbsUserResult = function() {
		$("#qqbsUserResult").datagrid("options").url = $('#rcContextPath').val() + '/qqbsUser/query';
		$('#qqbsUserResult').datagrid('load', {
			accountId : $("#accountId").val(),
			nickName : $("#nickName").val()
		});
	};
	$('#qqbsUserResult').datagrid({
		nowrap : false,
		striped : true,
		collapsible : true,
		idField : 'id',
		url : '',
		loadMsg : '正在装载数据...',
		fitColumns : true,
		remoteSort : true,
		singleSelect : true,
		pageSize : 50,
		pageList : [ 50, 60 ],
		columns : [ [ {
			field : 'accountId',
			title : 'ID',
			width : 50,
			align : 'center'
		}, {
			field : 'createTime',
			title : '创建时间',
			width : 70,
			align : 'center'
		}, {
			field : 'nickName',
			title : '昵称',
			width : 70,
			align : 'center'
		}, {
			field : 'fatherAccountId',
			title : '推荐人ID',
			width : 40,
			align : 'center'
		}, {
			field : 'subscribe',
			title : '加入方式',
			width : 70,
			align : 'center',
			formatter : function(value, row, index) {
				return {
					sourceType1 : '代言人二维码',
					sourceType2 : '推广链接',
					sourceType3 : '填ID',
					sourceType4 : '直接进入'
				}['sourceType' + row.subscribe] || '';
			}
		} ] ],
		pagination : true,
		rownumbers : true
	});
	$('#searchQqbsUserDiv #searchBtn').on('click', function() {
		loadQqbsUserResult();
	});
	$('#searchQqbsUserDiv').on('keyup', 'input:text', function(e) {
		if (e.keyCode == 13) {
			loadQqbsUserResult();
		}
	});
})