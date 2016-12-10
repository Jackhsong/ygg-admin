<!DOCTYPE html>
<html>
<head>
<meta charset='utf-8' />
<title>换吧网络-后台管理</title>
<link href='${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css' rel='stylesheet' type='text/css' />
<link href='${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css' rel='stylesheet' type='text/css' />
<link href='${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css' rel='stylesheet' media='screen'>
<script src='${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js'></script>
<script src='${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js'></script>
<script src='${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js'></script>
<script src='${rc.contextPath}/pages/js/bootstrap/js/bootstrap.min.js'></script>

<style type='text/css'>
	#myCss span {
		font-size:12px;
		padding-right:20px;
	}
</style>

</head>
<body class='easyui-layout'>

<div data-options='region:'center',title:'content'' style='padding:5px;'>
	<form method="post" action="${rc.contextPath}/outCall/kd8Callback">
		<table>
			<tr>
				<td>
					<input type='text' name='companyname' value='申通快递'/>
				</td>
			</tr>
			<tr>
				<td>
					<input type='text' name='outid' value='229393251290'/>
				</td>
			</tr>
			<tr>
				<td>
					<input type='text' name='status' value='0'/>
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" name="tracklist" value='{"Status":"未签收","TrackList":[{"ShortStatus":"收件","TrackDate":"2015-09-16 19:03:22","TrackStatus":"【浙江金华公司】的收件员【称重扫描02 】已收件"}],"UpdateDate":"\/Date(1442403946449+0800)\/"}'/>
				</td>
			</tr>
			<tr>
				<td>
					<input type="text" name="sign" value="431DD96FD2519FC46268FEDD6639EED2"/>
				</td>
			</tr>
			<tr>
				<td>
					<input type="submit" value="提交"/>
				</td>
			</tr>
		</table>	
	</form>
</div>

<script>

</script>

</body>
</html>