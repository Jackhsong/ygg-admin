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
<script src="${rc.contextPath}/pages/js/commonUtil.js"></script>
<style type="text/css">
.searchName{
	padding-right:10px;
	text-align:right;
}
.searchText{
	padding-left:10px;
	text-align:justify;
}
.search{
	width:1100px;
	align:center;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'west',title:'menu',split:true" style="width:180px;">
<input type="hidden" value="${nodes!0}" id="nowNode"/>
	<#include "../common/menu.ftl" >
</div>
<div data-options="region:'center'" style="padding: 5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'拼团商品管理----${listType}',split:true" style="height: 190px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" method="post">
		        	<input type="hidden"  name="isAvailable" value="${isAvailable}" />
		            <table class="search">
		                <tr>
		                	<td class="searchName">商品ID：</td>
							<td class="searchText"><input id="productId" name="productId" value="" /></td>
		                    <td class="searchName">商品名称：</td>
		                    <td class="searchText"><input id="name" name="name" value="" /></td>
		                    <td class="searchName">商品状态：</td>
		                    <td class="searchText">
			                    <select name="isOffShelves" id="isOffShelves" style="width: 170px;">
			                    	<option value="-1">全部</option>
			                    	<option value="1">已下架</option>
			                    	<option value="0">出售中</option>
			                    </select>
		                    </td>
		                   

		                </tr>
		                <tr>
		                	<td class="searchName">基本商品Id：</td>
		                    <td class="searchText"><input id="productBaseId" name="productBaseId" value="" /></td>
		                   
		                    <td class="searchName">编码：</td>
							<td class="searchText"><input id="code" type="text" name="code" /></td>
							
		                    <td class="searchName">商家：</td>
							<td class="searchText"><input id="sellerId" type="text" name="sellerId" /></td>
							
		                  
		                </tr>
		                <tr>
		                   <td class="searchName">开售时间：</td>
		                   <td class="searchText">
								<input value="" id="startTimeBegin" name="startTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								-
								<input value="" id="startTimeEnd" name="startTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
							</td>
							
							<td class="searchName">结束时间：</td>
							<td class="searchText">
								<input value="" id="endTimeBegin" name="endTimeBegin" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
								-
								<input value="" id="endTimeEnd" name="endTimeEnd" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>
							</td>
		                </tr>
		                <tr>
		                    <td class="searchName"></td>
							<td class="searchText"></td>
			
							
							<td class="searchName"></td>
							<td class="searchText">
								<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
								&nbsp;
								&nbsp;<a  onclick="exportAll()" href="javascript:;" class="easyui-linkbutton" >导出结果</a>
		                	</td>
						</tr>
		            </table>
		        </form>
    		</div>
		</div>
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>	        
    		
    		<!-- 调库存begin -->
   	 		<div id="editStock_div" style="width:1000px;height:600px;padding:20px 20px;">
   	 			<p>
   	 				商品名称：<span id="span_baseName"></span>&nbsp;&nbsp;
	   	 			商品条码：<span id="span_barcode"></span>&nbsp;&nbsp;
	   	 			商品编码：<span id="span_code"></span>&nbsp;&nbsp;
   	 				商家名称：<span id="span_sellerName"></span>&nbsp;&nbsp;
   	 			</p>
   	 			<p>
	   	 			发货类型：<span id="span_sendType"></span>&nbsp;&nbsp;
	   	 			发货地：<span id="span_sendAddress"></span>&nbsp;&nbsp;
   	 				分仓：<span id="span_warehouse"></span>&nbsp;&nbsp;
   	 			</p>
   	 			<font color="red" style="italic">(注：减少可填负数)</font>
   	 			<!--<p>
	   	 			剩余未分配总库存：<span id="span_availableStock"></span>&nbsp;<a onclick="refreshStock();" href="javascript:;" class="easyui-linkbutton">刷新</a>&nbsp;&nbsp;
	   	 			增加库存：<input type="text" style="width:40px;" name="totalStock" id="totalStock" value="" maxlength="10"/>
					<input type="hidden" name="baseId" id="baseId" value="" maxlength="10"/>
					<a onclick="addStock();" href="javascript:;" class="easyui-linkbutton">增加</a>
					<font color="red" style="italic">(注：减少可填负数)</font>
   	 			</p>-->
   	 			<table id="s_saleData" ></table>
   	 		</div>
   	 		<!-- 调库存end -->
    		
		</div>
	</div>
</div>
<script>
  
	$(function(){
	  loadDatagrid();
	  
	  $('#sellerId').combobox({
            	panelWidth:350,
            	panelHeight:350,
            	mode:'remote',
			    url:'${rc.contextPath}/seller/jsonSellerCode',   
			    valueField:'code',   
			    textField:'text'  
		});
			/**调库存弹出框*/
		$('#editStock_div').dialog({
    		title:'拼团商品调库存',
    		collapsible:true,
    		closed:true,
    		modal:true,
    		buttons:[{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#editStock_div').dialog('close');
                    $('#s_data').datagrid('reload',{
                       	isAvailable:${isAvailable}
                    });
                }
            }]
		});

 });
 
 
  function loadDatagrid(){
  
    $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/wechatGroup/jsonProductInfo',
            loadMsg:'正在装载数据...',
            queryParams:{
            	isAvailable:${isAvailable}
            	
            },
            fitColumns:true,
            remoteSort: true,
            pagination: true, //显示最下端的分页工具栏
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'number',    title:'序号', align:'center',checkbox:true},
                {field:'mwebGroupProductId',    title:'团购商品ID', align:'center'},
                {field:'productId',    title:'商品ID', align:'center'},
                {field:'isAvailable',    title:'使用状态', width:20, align:'center'},
                {field:'isOffShelves',    title:'商品状态', width:20, align:'center'},
                {field:'code',     title:'编码',  width:50,  align:'center'},
                {field:'name',     title:'长名称',  width:100,  align:'center'},
                {field:'shortName',     title:'短名称',  width:100,   align:'center'},
                {field:'typeView',     title:'团购类型',  width:25,   align:'center'},
                //{field:'teamNumberLimit',     title:'拼团成功人数',  width:40,   align:'center'},
                //{field:'teamValidHour',     title:'开团有效时间',  width:45,   align:'center'},
                {field:'teamPrice',     title:'团购价',  width:25,   align:'center'},
                {field:'sell',     title:'已成团销量',  width:25,   align:'center'},
                {field:'stock',     title:'剩余库存',  width:25,   align:'center'},
                {field:'lock',     title:'锁定库存',  width:25,   align:'center'},
                //{field:'order',     title:'排序值',  width:25,   align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'realSellerName',     title:'商家',  width:40,   align:'center' },
                {field:'sendAddress',     title:'发货地',  width:40,   align:'center' },
                /* {field:'startTime',     title:'开始时间',  width:50,   align:'center', formatter:function(value,row,index){  
                         var unixTimestamp = new Date(value);  
                         return unixTimestamp.toLocaleString();  
                         }  },
                {field:'endTime',     title:'结束时间',  width:50,   align:'center', formatter:function(value,row,index){  
                         var unixTimestamp = new Date(value);  
                         return unixTimestamp.toLocaleString();  
                         }   }, */
                {field:'remark',     title:'备注',  width:30,   align:'center'},     
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                       if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow_order('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow_order('+index+')">取消</a>';
                        	return s+c;
                    	}
                    	else{
                              var a = '<a href="javaScript:;" onclick="editIt(' + index + ')">修改</a>';                 
                              var b = ' | <a href="javaScript:;" onclick="editStock(' + index + ')">改库存</a>';
                              var t='';
                              var isOffShelves=-1;
                              if(row.isOffShelves=='出售中'){
                                     t='下架';
                                     isOffShelves=1;
                                }
                              else{
                                     t='上架';
                                     isOffShelves=0;
                                }
                              var c=' | <a href="javaScript:;" onclick="editIsOffShelves(' + index + ','+isOffShelves+')">'+t+'</a>';
                              //var d=' | <a href="javaScript:;" onclick="editrow_order(' + index + ')">修改排序值</a>';
                              //return a+b+c+d;
                              return a+b+c;
                           }
                    }
                }
                
            ]],
             onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions('s_data');
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions('s_data');
            	updateOrder(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions('s_data');
        	},
          toolbar: [{//在dategrid表单的头部添加按钮
                text: "新增商品",
                iconCls: "icon-add",
                handler: function () {
                	window.location.href = "${rc.contextPath}/wechatGroup/add/0"
                }
            }, '-',
             {
                text: "新增千人团商品",
                iconCls: "icon-add",
                handler: function () {
                	window.location.href = "${rc.contextPath}/wechatGroup/add/1"
                }
            } , '-'
            ,{
                    iconCls: 'icon-add',
                    text:'全部上架',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('上架','确定上架吗',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/wechatGroup/forSale", //上架
										{ids: ids.join(","),code:0},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
							                       	isAvailable:${isAvailable},
							                    });
											}else{
												$.messager.alert('提示','保存出错',"error")
											}
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                },'-',{
                    iconCls: 'icon-remove',
                    text:'全部下架',
                    handler: function(){
                        var rows = $('#s_data').datagrid("getSelections");
                        if(rows.length > 0){
                            $.messager.confirm('下架','确定下架吗',function(b){
                                if(b){
                                    var ids = [];
                                    for(var i=0;i<rows.length;i++){
                                        ids.push(rows[i].id)
                                    }
                                    $.post("${rc.contextPath}/wechatGroup/forSale", //下架
										{ids: ids.join(","),code:1},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
							                       	isAvailable:${isAvailable},
							                    });
											}else{
												$.messager.alert('提示','保存出错',"error")
											}
										},
									"json");
                                }
                     		})
                        }else{
                            $.messager.alert('提示','请选择要操作的商品',"error")
                        }
                    }
                },'-'
            
            ]
       });
       
      
      
      	$('#s_saleData').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/wechatGroup/getGroupProductCount',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:30,
            pageList:[30,40],
            columns:[[
            	{field:'id',    title:'商品Id', width:20, align:'center'},
            	{field:'name',    title:'商品名称', width:90, align:'center'},
                {field:'shortName',    title:'商品短名称', width:30, align:'center'},
                {field:'remark',    title:'商品备注', width:60, align:'center'},
                {field:'sell',    title:'卖出数量', width:20, align:'center'},
                {field:'stock',    title:'库存', width:20,align:'center'},
                {field:'addStock',    title:'增加库存', width:20, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'lock',    title:'已锁定', width:20,align:'center'},
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                    	if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow('+index+')">取消</a>';
                        	return s+c;
                    	}else{
                    	    var isAvailable="${isAvailable}";
                    		var a = '<a href="javascript:;" onclick="editrow(' + index + ')">改库存</a> | ';
                    		var b = '<a href="${rc.contextPath}/product/edit/3/' + row.productId + '" targe="_blank">查看</a>'
                        	return a+b;
                    	}
                    }
                }
            ]],
            onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions('s_saleData');
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions('s_saleData');
            	updateActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions('s_saleData');
        	},
            pagination:true
        });
      
      
      
  }
        
 //点击清空按钮出发事件
    function clearSearch() {
        
        $("#searchForm").find("input").val("");//找到form表单下的所有input标签并清空
        $("#isOffShelves").val('');
    }	
    
    function searchProduct(){
     var pjson=sy.serializeObject($("#searchForm").form());
     pjson.isAvailable=${isAvailable}
      $("#s_data").datagrid("load",pjson );//将searchForm表单内的元素序列为对象传递到后台
    }
    
    function editIt(index){
		var arr=$("#s_data").datagrid("getData");
		var urlStr="${rc.contextPath}/wechatGroup/edit/"+arr.rows[index].id+"/"+${isAvailable}+"/"+arr.rows[index].type;
		window.open(urlStr,"_blank");
	}
	
	function clearEditStock(){
		$("#span_baseName").text('');
		$("#span_barcode").text('');
		$("#span_code").text('');
		$("#span_sellerName").text('');
		$("#span_sendType").text('');
		$("#span_sendAddress").text('');
		$("#span_warehouse").text('');
		$("#span_availableStock").text('');
		$("#baseId").val('');
	}
	/**调库存*/
	function editStock(index){
		var arr=$("#s_data").datagrid("getData");
		var proudctBaseId = arr.rows[index].productBaseId;
		var mwebGroupProductId=arr.rows[index].mwebGroupProductId
		$.ajax({
			url: '${rc.contextPath}/productBase/findProductInfoByBaseId',
			type:"POST",
			data: {baseId:proudctBaseId},
			success: function(data) {
				if(data.status == 1){
					clearEditStock();
					$("#span_baseName").text(data.baseName);
					$("#span_barcode").text(data.barcode);
					$("#span_code").text(data.code);
					$("#span_sellerName").text(data.sellerName);
					$("#span_sendType").text(data.sendType);
					$("#span_sendAddress").text(data.sendAddress);
					$("#span_warehouse").text(data.warehouse);
					$("#span_availableStock").text(data.availableStock);
					$("#baseId").val(data.baseId);
					$('#s_saleData').datagrid('reload',{mwebGroupProductId:mwebGroupProductId});
					$('#editStock_div').dialog('open');
                } else{
                    $.messager.alert('响应信息',data.msg,'error');
                }
			}
		});
	}
	
	
	function editIsOffShelves(index,isOffShelves){
	  var arr=$("#s_data").datagrid("getData");
	  var productId = arr.rows[index].id;
	  var isAvailable=${isAvailable};
	 
	    		  $.post("${rc.contextPath}/wechatGroup/forSale", //下架
										{ids: productId,code:isOffShelves},
										function(data){
											if(data.status == 1){
												$('#s_data').datagrid('clearSelections');
												$('#s_data').datagrid('reload',{
							                       	isAvailable:isAvailable
							                    });
											}else{
												$.messager.alert('提示','保存出错',"error")
											}
										},
									"json");
	    	
	}
	
	
	function editOrder(index){
	}
	
	
	<!--调库存相关begin-->
function editrow(index){
	$('#s_saleData').datagrid('beginEdit', index);
}
function saverow(index){
	$('#s_saleData').datagrid('endEdit', index);
}
function cancelrow(index){
	$('#s_saleData').datagrid('cancelEdit', index);
}

function updateActions(datagrid){
	var rowcount = $('#'+datagrid).datagrid('getRows').length;
	for(var i=0; i<rowcount; i++){
		$('#'+datagrid).datagrid('updateRow',{
	    	index:i,
	    	row:{}
		});
	}
}


function updateActivity(row){
	$.ajax({
		url: '${rc.contextPath}/wechatGroup/updateGroupProductCount',
		type:"POST",
		data: {mwebGroupProductId:row.id,addStock:row.addStock},
		dataType:"json",
		success: function(data) {
			if(data.status == 1){
			$('#s_saleData').datagrid('reload',{mwebGroupProductId:row.id});
	            return
	        } else{
	            $.messager.alert('响应信息',data.msg,'error',function(){
	                return
	            });
	        }
		}
	});
};





	<!--排序值相关begin-->
function editrow_order(index){
	$('#s_data').datagrid('beginEdit', index);
}
function saverow_order(index){
	$('#s_data').datagrid('endEdit', index);
}
function cancelrow_order(index){
	$('#s_data').datagrid('cancelEdit', index);
}



function updateOrder(row){
      var isAvailable=${isAvailable};
	$.ajax({
		url: '${rc.contextPath}/wechatGroup/updateOrder',
		type:"POST",
		data: {mwebGroupProductId:row.id,order:row.order},
		dataType:"json",
		success: function(data) {
			if(data.status == 1){
			$('#s_data').datagrid('reload',{isAvailable:isAvailable});
	            return
	        } else{
	            $.messager.alert('响应信息',data.msg,'error',function(){
	                return
	            });
	        }
		}
	});
};

function exportAll() {

	$('#searchForm').attr("action", "${rc.contextPath}/wechatGroup/exportProductInfo").submit();
}
</script>

</body>
</html>