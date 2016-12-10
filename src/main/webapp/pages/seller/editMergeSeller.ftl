<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>换吧网络-后台管理</title>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>

<div id="addRole_div" style="margin: 20 20">
    <form action="post" id="editForm">
		<p>
			<span>&nbsp;&nbsp;关联商家ID:</span>
			<input type="number" name="masterId" maxlength="10" id="editForm_masterId" onchange="getSellerInfo(this)" value="<#if masterId?exists>${masterId}</#if>"/>
			<span style="color:red"></span>
		</p>
		<#if slaveList?exists>
		<#list slaveList as slave>
		<p>
			<span>被关联商家ID ${slave_index+1}:</span>
			<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)" value="<#if slave.sellerSlaveId?exists>${slave.sellerSlaveId?c}</#if>"/>
			<span style="color:red"></span>
		</p>
		</#list>
		</#if>
        <p>&nbsp;&nbsp;<input style="width: 80px; height: 20px" type="button" id="save" value="保存"/></p>
    </form>
</div>
<script>
	function getSellerInfo(e){
		var tip = $(e).next("span");
		var sellerId = $.trim($(e).val());
		if(sellerId != ""){
			$.ajax({
	            url: '${rc.contextPath}/seller/findSellerById',
	            type: 'post',
	            dataType: 'json',
	            data: {'sellerId':sellerId},
	            success: function(data){
	                if(data.status == 1){
	                	$(tip).text(data.msg);
	                }else{
	                	$.messager.alert("提示",data.msg,"info",function(){
	                		$(e).val('');
	                	});
	                }
	            },
	            error: function(xhr){
	            	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
	            }
	        });
		}
	}
	
    $(function(){
        $("#save").click(function(){
        	/* var masterId = $.trim($("#editForm_masterId").val());
        	var slaveId = [];
        	$("input[name='editForm_slaveId']").each(function(){
        		if($.trim($(this).val()) !=''){
        			slaveId.push($.trim($(this).val()));
        		}
        	}); */
            $('#editForm').form('submit',{
                url:"${rc.contextPath}/seller/mergeSeller",
                onSubmit:function(){
                	var masterId = $.trim($("#editForm_masterId").val());
                	if(!/^\d*$/.test(masterId)){
                		$.messager.alert('提示',"请输入关联商家Id并且只能输入数字",'error');
            			return false;
                	}
                	$.messager.progress();
                },
                success:function(data){
                		$.messager.progress('close');
                        var res = eval("("+data+")");
                        $.messager.alert('响应信息',res.msg,'info',function(){
                            $("#s_data").datagrid('clearSelections');
                        	$('#s_data').datagrid('reload');
                            $('#mergeSeller').dialog('close');
                        });
                }
            });
        	/* $.ajax({
                url: '${rc.contextPath}/seller/mergeSeller',
                type: 'post',
                dataType: 'json',
                data: {'masterId':masterId,'slaveId':slaveId.join(",")},
                success: function(data){
                    $.messager.progress('close');
                    if(data.status == 1){
                        $.messager.alert("提示","保存成功","info",function(){
                            window.location.href = window.location.href;
                        });
                    }else{
                        $.messager.alert("提示",data.msg,"error");
                    }
                },
                error: function(xhr){
                    $.messager.progress('close');
                    $.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
                }
            }); */ 
        });
    });
</script>
</body>
</html>