$(function() {
	var loadQqbsUserResult = function(accountId) {
		$('#qrCodeResult').datagrid('load', {
			accountId : accountId || $("#queryAccountId").val()
		});
	};

	var showCreateQRCode = function() {
		$('#createQRCodePanel').removeClass('showQRCodeUrl');
		$('#createQRCodePanel').find('#accountId').val('');
		$('#createQRCodePanel').find('.qrCodeUrlDiv #qrCodeUrl').val('');
		$('#createQRCodePanel').dialog('open');
	};

	var createQRCode = function() {
		var accountId = $('#createQRCodePanel').find('#accountId').val();
		if (accountId !== '') {
			$.ajax({
				url : $('#rcContextPath').val() + '/qRcode/create',
				method : 'POST',
				data : {
					accountId : accountId
				}
			}).done(function(doneObj) {
				if (doneObj.status) {
					$('#createQRCodePanel').find('.qrCodeUrlDiv #qrCodeUrl').val(doneObj.qrCodeUrl);
					$('#createQRCodePanel').addClass('showQRCodeUrl');
				} else {
					$.messager.alert('存在错误', doneObj.message);
				}
			}).fail(function(failObj) {
				console.error(failObj);
			});
		}
	};

	$('#qrCodeResult')
			.datagrid(
					{
						nowrap : false,
						striped : true,
						collapsible : true,
						idField : 'id',
						url : $('#rcContextPath').val()
						+ '/qRcode/query',
						loadMsg : '正在装载数据...',
						fitColumns : true,
						remoteSort : true,
						singleSelect : true,
						pageSize : 50,
						pageList : [ 50, 60 ],
						columns : [ [
								{
									field : 'accountId',
									title : 'ID',
									width : 50,
									align : 'center'
								},
								{
									field : 'nickname',
									title : '昵称',
									width : 70,
									align : 'center',
									formatter : function(value, rec) {
										return rec.account?rec.account.nickname:"";
									}
								},
								{
									field : 'hidden',
									title : '操作',
									width : 80,
									align : 'center',
									formatter : function(value, row, index) {
										return "<a class='showQRCodeLink' target='_blank' href='"+row.qrCodeUrl+"' data-index='"
												+ index + "'>查看</a>";
									}
								} ] ],
						toolbar : [ {
							id : '_add',
							text : '永久二维码生成',
							iconCls : 'icon-add',
							handler : showCreateQRCode
						} ],
						pagination : true,
						rownumbers : true
					});

	$('#createQRCodePanel').dialog({
		title : '二维码生成',
		closed : true,
		modal : true,
		width : 500,
		height : 300
	});

	$('#createQRCodePanel').on('click', '#createButton', createQRCode);

	$('#createQRCodePanel').on('keyup', '#accountId', function(e) {
		if (e.keyCode == 13) {
			createQRCode();
		}
	});
	$('#searchQRCodeDiv #searchBtn').on('click', function() {
		loadQqbsUserResult();
	});
	$('#searchQRCodeDiv').on('keyup', '#queryAccountId', function(e) {
		if (e.keyCode == 13) {
			loadQqbsUserResult();
		}
	});
	new Clipboard('.copy-link');

})