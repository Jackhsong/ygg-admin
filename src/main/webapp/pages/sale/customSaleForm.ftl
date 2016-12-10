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

<div data-options="region:'center',title:'添加自定义特卖信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="saveAcCustom" action="${rc.contextPath}/customSale/saveCustomSale"  method="post">
		<fieldset>
			<input type="hidden" value="<#if acCustom.id?exists && (acCustom.id != 0)>${acCustom.id?c}</#if>" id="editId" name="editId" />
			<legend>组合特卖信息</legend>
    		 标题: <input type="text" maxlength="10" id="name" name="name" style="width:300px;"  value="<#if acCustom.name?exists>${acCustom.name}</#if>" /><br/><br/>
    		 URL: <input type="text" maxlength="100" id="name" name="name" style="width:300px;"  value="<#if acCustom.name?exists>${acCustom.name}</#if>" /><br/><br/>
			 备注: <textarea name="desc" style="height: 60px;width: 300px"><#if acCustom.desc?exists>${acCustom.desc}</#if></textarea><br/><br/>
			 状态:
			 <input type="radio" name="isAvailable" value="1" <#if acCustom.isAvailable?exists && (acCustom.isAvailable == 1) >checked</#if> > 可用&nbsp;&nbsp;&nbsp;
			 <input type="radio" name="isAvailable" value="0" <#if acCustom.isAvailable?exists && (acCustom.isAvailable == 0) >checked</#if> > 停用<br/><br/>
			 
			 <span>页面内容编辑</span><br/><br/>
			 <input style="width: 100px;background-color:grey" type="button" id="pcButton" value="PC"/>
			 <input style="width: 100px" type="button" id="mobileButton" value="移动"/>
			 <div id="pcDetail_div">
			 	<textarea name="pcDetail" style="height: 400px;width: 700px"><#if acCustom.pcDetail?exists>${acCustom.pcDetail}</#if>1</textarea>
			 </div>
			 <div id="mobileDetail_div" style="display:none;">
			 	<textarea name="mobileDetail" style="height: 400px;width: 700px"><#if acCustom.mobileDetail?exists>${acCustom.mobileDetail}</#if>2</textarea>
			 </div>
			 <br/><br/>
			 
			 <div style="width:700px;">
			 	<table id="s_data" >
					
		        </table>
			 </div>
			 <br/><br/>
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
	<div id="add_div" style="width:700px;height:650px;padding:20px 20px;">
		
		<div id="searchDiv" class="datagrid-toolbar" style="height: 40px;padding: 15px">
        	<form id="searchForm" method="post" >
            	<table>
                <tr>
                    <td>商品编码</td>
                    <td><input id="searchCode" name="code" value="" /></td>
                    <td>名称</td>
                    <td><input id="searchName" name="name" value="" /></td>
					<td>
						&nbsp;&nbsp;
						<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                	</td>
                </tr>
            	</table>
        	</form>
    	</div>
	
		<table id="product_data" >

		</table>
	</div>
	
</div>

<script type="text/javascript">

	function searchProduct(){
    	$('#product_data').datagrid('load',{
        	name:$("#searchName").val(),
        	code:$("#searchCode").val(),
        	status:1,
        	id:${acCustom.id?c}
    	});
	}
	
	$(function(){
		
		$('#pcButton').click(function(){
			$('#pcButton').css("background","gray");
			$('#mobileButton').css("background","");
			$('#pcDetail_div').show();
			$('#mobileDetail_div').hide();
		});
		
		$('#mobileButton').click(function(){
			$('#mobileButton').css("background","gray");
			$('#pcButton').css("background","");
			$('#pcDetail_div').hide();
			$('#mobileDetail_div').show();
		});
		
		$("#saveButton").click(function(){
    		$('#saveAcCustom').submit();
    	});
    	
    	$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customSale/jsonRelationProductInfo',
			queryParams: {
				id:${acCustom.id?c}
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            singleSelect:true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'code',    title:'商品编码', width:50, align:'center'},
                {field:'name',    title:'商品名称', width:70, align:'center'},
                {field:'stock',     title:'库存',  width:50,   align:'center' },
                {field:'marketPrice',    title:'原价', width:70, align:'center'},
                {field:'salesPrice',    title:'现价', width:70, align:'center'}
            ]],
            toolbar:[{
                id:'_add',
                text:'增加商品',
                iconCls:'icon-add',
                handler:function(){
                	$('#product_data').datagrid('reload');
                	$('#add_div').dialog('open');
                }
            }],
            pagination:true,
            rownumbers:true
        });
        
        $('#product_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customSale/jsonProductList',
            queryParams: {
				id: ${acCustom.id?c},
				status:0
			},
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
            	{field:'id',    title:'序号', width:50, align:'center',checkbox:true},
                {field:'code',    title:'商品商家编码', width:70, align:'center'},
                {field:'name',    title:'商品名称', width:70, align:'center'},
                {field:'stock',    title:'库存', width:70, align:'center'},
                {field:'marketPrice',    title:'原价', width:70, align:'center'},
                {field:'salesPrice',    title:'现价', width:70, align:'center'}
            ]],
            toolbar:[{
                    iconCls: 'icon-add',
                    text:'增加选中商品',
                    handler: function(){
                        var rows = $('#product_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('添加商品','请确认',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/customSale/addCustomSaleProduct", //添加选中商品
										{ids: ids.join(","),activitiesCustomId:${acCustom.id?c}},
										function(data){
											if(data.status == 1){
												$('#add_div').dialog('close');
												$('#s_data').datagrid('reload');
											}else{{
												$.messager.alert('提示','保存出错',"error")
											}}
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                }],
            pagination:true,
            rownumbers:true
        });
        
        $('#add_div').dialog({
    		title:'商品信息',
    		collapsible:true,
    		closed:true,
    		modal:true,
    		buttons:[]
		})
	})
</script>

</body>
</html>