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

<div data-options="region:'center',title:'content'" style="padding:5px;">

	<div id="searchDiv" class="datagrid-toolbar" style="height: 50px;padding: 15px">
        <form id="searchForm" method="post" >
            <table>
            	<tr>
            		<td>Id：</td>
                    <td><input id="mallPageId" name="mallPageId" value=""/></td>
                    <td>主题馆页面标题：</td>
                    <td><input id="mallPageName" name="mallPageName" value="" /></td>
                    <td>展现状态：</td>
                    <td>
                    	<select id="isAvailable" name="isAvailable">
	                    	<option value="-1">全部</option>
	                    	<option value="1">可用</option>
	                    	<option value="0">不可用</option>
                    	</select>
                    </td>
					<td><a id="searchBtn" onclick="searchSaleWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a></td>
                	<td><a id="clearBtn" onclick="clearSaleWindow()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a></td>
                </tr>
            </table>
        </form>
    </div>
    <!--数据表格-->
    <table id="s_data"></table>
    
    <!-- 新增/编辑Dialog框 begin -->
    <div id="editMallPageDiv" class="easyui-dialog" style="width:600px;height:250px;padding:10px 10px;">
        <form id="editMallPageForm" method="post">
        	<input type="hidden" name="id" id="editMallPageForm_id"/>
	        <table cellpadding="5">
	            <tr>
	                <td>页面标题：</td>
	                <td>
	                	<input type="text" name="name" id="editMallPageForm_name" value="" maxlength="11"/><font color="red">*&nbsp;注：APP上看到的页面标题，11个汉字以内</font>
	                </td>
	            </tr>
	            <tr>
	                <td>&nbsp;&nbsp;备注：</td>
	                <td>
	                	<input type="text" name="remark" id="editMallPageForm_remark" value="" maxlength="50" style="width:400px;"/>
	                </td>
	            </tr>
	            <tr>
	                <td>可用状态：</td>
	                <td>
	                	<input type="radio"  name="isAvailable"  id="isAvailable_1" value="1"> 可用&nbsp;&nbsp;&nbsp;
						<input type="radio"  name="isAvailable"  id="isAvailable_0" value="0"> 不可用
	                </td>
	            </tr>
	        </table>
    	</form>
    </div>    
    <!-- 新增/编辑Dialog框 end -->

</div>

<script>

	<!--搜索-->
	function searchSaleWindow(){
    	$('#s_data').datagrid('load',{
        	id:$("#mallPageId").val(),
        	name:$("#mallPageName").val(),
        	isAvailable:$("#isAvailable").val()
    	});
	}
	
	<!--清除搜索框-->
	function clearSaleWindow(){
		$("#mallPageId").val(''),
    	$("#mallPageName").val(''),
    	$("#isAvailable").find("option").eq(0).attr('selected','selected');
		$('#s_data').datagrid('load',{});
	}
	
	<!--清楚编辑框-->
	function cleanEditMallPageForm(){
		$("#editMallPageForm_id").val('');
		$("#editMallPageForm input[type='text']").each(function(){
			$(this).val('');
		});
		$("#editMallPageForm input[type='radio']").each(function(){
			$(this).prop('checked', false);
		});
	}
	
	<!--编辑-->
	function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		cleanEditMallPageForm();
		$("#editMallPageForm_id").val(arr.rows[index].id);
		$("#editMallPageForm_name").val(arr.rows[index].name);
		$("#editMallPageForm_remark").val(arr.rows[index].remark);
		if(arr.rows[index].isAvailableCode == 1){
			$("#isAvailable_1").prop('checked',true);
		}else if(arr.rows[index].isAvailableCode == 0){
			$("#isAvailable_0").prop('checked',true);
		}
    	$('#editMallPageDiv').dialog('open');
	}
	
	function manageFloor(id){
		var url = '${rc.contextPath}/mallWindow/manageFloor/'+id;
		window.open(url,'_blank');
	}
	
	function manageProduct(id){
		var url = '${rc.contextPath}/mallWindow/manageProduct/'+id;
		window.open(url,'_blank');
	}
	

	$(function(){
		
		<!--加载数据表格-->
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/mallWindow/jsonMallPageInfo',
            loadMsg:'正在装载数据...',
            singleSelect:true,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'ID', width:20, align:'center'},
                {field:'isAvailable',    title:'状态', width:50, align:'center'},
                {field:'name',    title:'主题馆页面标题', width:70, align:'center'},
                {field:'remark',    title:'备注', width:70, align:'center'},
                {field:'hidden',  title:'操作', width:80,align:'center',
                    formatter:function(value,row,index){
                    		var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">编辑</a> | ';
                            var b = '<a href="javascript:;" onclick="manageFloor(' + row.id + ')">楼层管理</a> | ';
                            var c = '<a href="javascript:;" onclick="manageProduct(' + row.id + ')">商品管理</a>';
	                        return a + b + c;
                    	}
                }
            ]],
            toolbar:[{
                id:'_add',
                text:'新增主题馆页面',
                iconCls:'icon-add',
                handler:function(){
                	cleanEditMallPageForm();
                	$('#editMallPageForm_id').val('0');
                	$('#isAvailable_0').prop('checked',true);
                	$('#editMallPageDiv').dialog('open');
                }
            }],
            pagination:true
        });
		
		<!--新增/编辑弹出框-->
	    $('#editMallPageDiv').dialog({
	    	title:'主题馆页面',
	    	collapsible: true,
	    	closed: true,
	    	modal: true,
	    	buttons: [
	    	{
	    		text: '保存',
	    		iconCls: 'icon-ok',
	    		handler: function(){
	    			$('#editMallPageForm').form('submit',{
	    				url: "${rc.contextPath}/mallWindow/saveOrUpdateMallPage",
	    				onSubmit:function(){
	    					var name = $("#editMallPageForm input[name='name']").val();
	    					if($.trim(name) == ''){
	    						$.messager.alert("提示","请输入页面标题","warn");
	    						return false;
	    					}else if(name.length>11){
	    						$.messager.alert("提示","页面标题请控制在11字数以内","warn");
	    						return false;
	    					}
	    					$.messager.progress();
	    				},
	    				success: function(data){
	    					$.messager.progress('close');
	                        var res = eval("("+data+")");
	                        if(res.status == 1){
	                            $.messager.alert('响应信息',"保存成功",'info',function(){
	                                $('#s_data').datagrid('reload');
	                                $('#editMallPageDiv').dialog('close');
	                            });
	                        } else if(res.status == 0){
	                            $.messager.alert('响应信息',res.msg,'info');
	                        } 
	    				}
	    			});
	    		}
	    	},
	    	{
	    		text:'取消',
	            align:'left',
	            iconCls:'icon-cancel',
	            handler:function(){
	                $('#editMallPageDiv').dialog('close');
	            }
	    	}]
	    });
	})
</script>

</body>
</html>