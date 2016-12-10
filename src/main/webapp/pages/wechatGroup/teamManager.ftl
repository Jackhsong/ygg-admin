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
		<div data-options="region:'north',title:'拼团管理',split:true" style="height: 100px;">
			<div id="searchDiv" style="height: 120px;padding: 15px">
		        <form id="searchForm" method="post">
		            <table class="search">
		                <tr>
		                	<td class="searchName">商品ID：</td>
							<td class="searchText"><input id="productId" name="productId" value="" /></td>
		                    <td class="searchName">商品名称：</td>
		                    <td class="searchText"><input id="name" name="name" value="" /></td>
		                    <td class="searchName">商品状态：</td>
		                    <td class="searchText">
			                    <select name="status" id="status" style="width: 170px;">
			                    	<option value="0">全部</option>
			                    	<option value="1">即将开始</option>
			                    	<option value="2">进行中</option>
			                    </select>
		                    </td>
		                    <td class="searchText">
								<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
								&nbsp;
								<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a>
		                	</td>
		                </tr>
		                <tr>
<!-- 		                   <td class="searchName">时间：</td> -->
<!-- 		                   <td class="searchText"> -->
<!-- 								<input value="" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/> -->
<!-- 								- -->
<!-- 								<input value="" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/> -->
<!-- 							</td> -->
<!-- 		                    <td class="searchName"></td> -->
<!-- 							<td class="searchText"></td> -->
<!-- 							<td class="searchName"></td> -->
<!-- 							<td class="searchText"> -->
<!-- 								<a id="searchBtn" onclick="searchProduct()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> -->
<!-- 								&nbsp; -->
<!-- 								<a id="searchBtn" onclick="clearSearch()" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">清除</a> -->
<!-- 		                	</td> -->
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
  
	$(function(){
	  loadDatagrid();
 	});
 
 
  function loadDatagrid(){
  
    $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/wechatGroup/jsonProductInfoForTeam',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            singleSelect:true,
            remoteSort: true,
            pagination: true, //显示最下端的分页工具栏
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'productId',    title:'商品ID', align:'center'},
                {field:'isAvailable',    title:'展示状态', width:20, align:'center',
                	formatter:function(value,row,index){  
                        if(row.isAvailable == 1)
                        	return '展示';
                        else if(row.isAvailable == 0)
                        	return '不展示';
                        else 
                        	return '';
               		}
                },
                {field:'status',    title:'商品状态', width:20, align:'center',
                	formatter:function(value,row,index){  
                        var unixTimestamp = new Date();  
                        var time = unixTimestamp.getTime()
                        if(row.startTime > time)
                        	return '即将开始';
                        else if((row.startTime < time) && (row.endTime > time))
                        	return '进行中';
                        else if(row.endTime < time)
                        	return '已下架'
                        else 
                        	return '';
               		}
                },
                {field:'order',     title:'排序值',  width:20,   align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'name',     title:'长名称',  width:100,  align:'center'},
                {field:'shortName',     title:'短名称',  width:80,   align:'center'},
                {field:'sellingPoint',     title:'核心亮点',  width:30,   align:'center'},
                {field:'stock',     title:'剩余库存',  width:20,   align:'center'},
                {field:'startTime',     title:'开始时间',  width:45,   align:'center',
                	formatter:function(value,row,index){  
                         var unixTimestamp = new Date(value);  
                         return unixTimestamp.toLocaleString();  
                	}
                },
                {field:'endTime',     title:'结束时间',  width:45,   align:'center',
                	formatter:function(value,row,index){  
                         var unixTimestamp = new Date(value);  
                         return unixTimestamp.toLocaleString();  
                	}
                },
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                       if (row.editing){
                    		var s = '<a href="javascript:void(0);" onclick="saverow_order('+index+')">保存</a> ';
                        	var c = '<a href="javascript:void(0);" onclick="cancelrow_order('+index+')">取消</a>';
                        	return s+c;
                    	}
                    	else{
                              var a = '<a href="javaScript:;" onclick="editIt(' + row.productId + ')">修改</a>';
                              var b = '';
                              if(row.isAvailable == 1) {
                            	  b = ' | <a href="javaScript:;" onclick="editAvailable(' + row.productId + ',' + 0 + ')">不展现</a>';
                              } else {
                            	  b = ' | <a href="javaScript:;" onclick="editAvailable(' + row.productId + ',' + 1 + ')">展现</a>';
                              }
                              var c=' | <a href="javaScript:;" onclick="editrow_order(' + index + ')">修改排序值</a>';
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
                text: "新增团购",
                iconCls: "icon-add",
                handler: function () {
                	window.location.href = "${rc.contextPath}/wechatGroup/editForTeam/-1"
                }
            }]
       });
       
  }
        
 //点击清空按钮出发事件
    function clearSearch() {
        $("#searchForm").find("input").val("");//找到form表单下的所有input标签并清空
        $("#statis").val('');
    }	
    
    function searchProduct(){
     var pjson=sy.serializeObject($("#searchForm").form());
      $("#s_data").datagrid("load",pjson );//将searchForm表单内的元素序列为对象传递到后台
    }
    
    function editIt(productId){
    	window.location.href = "${rc.contextPath}/wechatGroup/editForTeam/"+productId;
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
	$.ajax({
		url: '${rc.contextPath}/wechatGroup/updateOrder',
		type:"POST",
		data: {mwebGroupProductId:row.id,order:row.order},
		dataType:"json",
		success: function(data) {
			if(data.status == 1){
			$('#s_data').datagrid('reload');
	            return
	        } else{
	            $.messager.alert('响应信息',data.msg,'error',function(){
	                return
	            });
	        }
		}
	});
}

function editAvailable(productId, available) {
	$.messager.confirm('确认对话框', '确定修改展现状态吗？', function(r){
		if (r){
			$.messager.progress();
			$.ajax({
				url: '${rc.contextPath}/wechatGroup/updateProductForTeam',
				type:"POST",
				data: {'productId':productId,'isAvailable':available},
				dataType:"json",
				success: function(data) {
					$.messager.progress('close');
					if(data.status != 1){
			            $.messager.alert('响应信息',data.msg,'error',function(){
			                return
			            });
			        } else {
			        	$('#s_data').datagrid('reload');
			        }
				}
			});
		}
	});
}

</script>

</body>
</html>