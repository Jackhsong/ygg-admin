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

</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'合并商家发货地管理',split:true" style="height: 20px;"></div>
		<div data-options="region:'center'" >
			
			<!-- 数据表格 -->
			<table id="s_data" ></table>
			
    		<!-- 合并商家begin-->
			<div id="mergeSeller" class="easyui-dialog" style="width:600px;height:330px;padding:10px 10px;">
	            <form id="mergeSeller_form" method="post">
					<p>
						<span>&nbsp;&nbsp;关联商家ID:</span>
						<input type="number" name="masterId" maxlength="10" id="masterId" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
					</p>
					<p>
						<span>被关联商家ID 1:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
						<span>被关联商家ID 2:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
					</p>
					<p>
						<span>被关联商家ID 3:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
						<span>被关联商家ID 4:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
					</p>
					<p>
						<span>被关联商家ID 5:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
						<span>被关联商家ID 6:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
					</p>
					<p>
						<span>被关联商家ID 7:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
						<span>被关联商家ID 8:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
					</p>
					<p>
						<span>被关联商家ID 9:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
						<span>被关联商家ID10:</span>
						<input type="number" name="slaveId" maxlength="10" onchange="getSellerInfo(this)"/>
						<span style="color:red"></span>
					</p>
	        	</form>
	        </div>
	        <!-- 合并商家 end-->
	        
	        <!-- 编辑合并商家 -->
	        <div id="editMerge_div"></div>		
		</div>	
	</div>
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
	
	function clearMergeSellerForm(){
		$("#masterId").val('');
		$("input[name='slaveId']").each(function(){
			$(this).val('');
			$(this).next('span').text('');
		});
		
	}
	
	function editIt(id){
		clearMergeSellerForm();
        $.ajax({
        	url:'${rc.contextPath}/seller/editMergeSeller',
        	type:'post',
        	data:{'masterId':id},
        	dataType:'json',
        	success:function(data){
        		$("#masterId").val(data.masterId);
        		$.each(data.slaveList,function(index,element){
        			$("input[name='slaveId']").eq(index).val(element.sellerSlaveId);
        			console.log(index+','+element.sellerSlaveId);
        		})
        		$('#mergeSeller').dialog('open');
        	},
        	error: function(xhr){
            	$.messager.alert("提示",'服务器忙，请稍后再试。(errorCode='+xhr.status+')',"info");
            }
        });
	}
	
	function viewIt(ids){
		var url = '${rc.contextPath}/seller/list/1?ids='+ids;
		window.open(url,'_blank');
	}

	$(function(){
		$('#mergeSeller').dialog({
            title:'合并商家发货地',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存',
                iconCls:'icon-ok',
                handler:function(){
                 	
                    $('#mergeSeller_form').form('submit',{
                        url:"${rc.contextPath}/seller/mergeSeller",
                        onSubmit:function(){
                        	var masterId = $.trim($("#masterId").val());
                        	var slaveId = [];
                        	if(!/^\d*$/.test(masterId)){
                        		$.messager.alert('提示',"请输入关联商家Id并且只能输入数字",'error');
                    			return false;
                        	}
                        	$("input[name='slaveId']").each(function(){
                        		if(/^\d*$/.test($(this).val())){
                        			slaveId.push($(this).val());
                        		}
                        	});
                        	if(slaveId.length==0){
                        		$.messager.alert('提示',"请输入被关联商家Id且只能输入数字",'error');
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
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#mergeSeller').dialog('close');
                }
            }]
        });
		
		$('#editMerge_div').dialog({
            title: '编辑合并商家',
            width: 600,
            height: 330,
            closed: true,
            href: '${rc.contextPath}/seller/editMergeSeller',
            buttons:[{
                text:'关闭    ',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editMerge_div').dialog("close");
                }
            }]
        });
		
		
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/seller/mergeSellerJsonInfo',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:false,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'关联商家ID', width:20, align:'center'},
                {field:'sellerName',    title:'关联商家名称', width:30, align:'center'},
                {field:'sendAddress',    title:'主商家发货地', width:30, align:'center'},
                {field:'slave',    title:'被关联商家', width:90, align:'center'},
                {field:'hidden',  title:'操作', width:20,align:'center',
                    formatter:function(value,row,index){
                    	/* var a = '<a href="javaScript:;" onclick="viewIt('+row.ids+')">查看</a> | '; */
                    	var b = '<a href="javaScript:;" onclick="editIt('+row.id+')">编辑</a>';
                        return b;
                    }
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增商家发货地关联',
                iconCls:'icon-add',
                handler:function(){
                	clearMergeSellerForm();
                	$('#mergeSeller').dialog('open');
                }
            }],
            pagination:true,
            rownumbers:true
        });
	})
</script>

</body>
</html>