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
	       <div data-options="region:'center',title:'自动拼团商品管理',split:true">
	         <table id="s_data" style=""></table>	  
           </div>               
    </div>
    
    
    
    
      <div id="autoJoinTeamConfigDiv" class="easyui-dialog" style="width:250px;height:200px;padding:20px 20px;" data-options="title:'自动参团设置'" closed="true">
		        <form id="autoJoinTeamConfigForm" method="post">
					<input id="autoJoinTeamConfigId"  type="hidden" name="id"/>
			        <table>
			             <tr>
			                <td>入团时间/秒：</td>
			            	<td>
			            		<input id="randomStartSecond" style="width:35px" name="randomStartSecond" value=""/>~<input id="randomEndSecond" style="width:35px" name="randomEndSecond" />
			            	</td>
			            </tr>
			             <tr>
			                <td>参团人数限制：</td>
			            	<td>
			            		<input id="autoJoinTeamNumberLimit" style="width:85px" name="autoJoinTeamNumberLimit" />
			            	</td>
			            </tr>
			        </table>
		    	</form>
		    </div>
</div>

<script>

 
$(function(){
	  loadDatagrid();
	  
	   $('#autoJoinTeamConfigDiv').dialog({
	      title:'自动参团设置',
    	  collapsible: true,
    	  closed: true,
    	  modal: true,
    	  buttons: [
    	      {
    	        text: '保存',
    		    iconCls: 'icon-ok',
    		    handler: function(){
    		       $('#autoJoinTeamConfigForm').form('submit',{
    		          url: "${rc.contextPath}/wechatGroup/updateAutoTeamConfig",
    		          onSubmit:function(){
    		           
    				    var randomStartSecond = $("#randomStartSecond").val();
    				    var randomEndSecond = $("#randomEndSecond").val();
    				    var autoJoinTeamNumberLimit = $("#autoJoinTeamNumberLimit").val();
    				    var id=$("#autoJoinTeamConfigId").val();
    				    if(randomStartSecond<0||randomStartSecond>randomEndSecond){
    				       $.messager.alert("info","时间无效","warn");
    						return false;
    				    }
    				    if(autoJoinTeamNumberLimit<=0){
    				       $.messager.alert("info","限制人数无效","warn");
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
                                $('#autoJoinTeamConfigDiv').dialog('close');
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
                  $('#autoJoinTeamConfigDiv').dialog('close');
                }
    	      },
    	  ]
	    })
})
 
  function loadDatagrid(){
  
    $('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/wechatGroup/jsonAutoTeamList',
            loadMsg:'正在装载数据...',
            queryParams:{
            	
            	
            },
            fitColumns:true,
            remoteSort: true,
            pagination: true, //显示最下端的分页工具栏
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'number',    title:'序号', align:'center',checkbox:true},
                {field:'id',    title:'id', align:'center'},
                {field:'mwebGroupProductInfoId',    title:'团购ID',width:20, align:'center'},
                {field:'mwebGroupProductId',    title:'商品ID', align:'center',width:20,},
                {field:'shortName',     title:'短名称',  width:30,   align:'center'},
                {field:'teamNumberLimit',     title:'拼团成功人数',  width:20,   align:'center'},
                {field:'teamValidHour',     title:'开团有效时间',  width:20,   align:'center',formatter:function(value,row,index){
                
                  return value+'分钟';
                 }},
                {field:'startTeamTime',     title:'团购开始时间',  width:40,   align:'center', formatter:function(value,row,index){  
                         var unixTimestamp = new Date(value);  
                         return unixTimestamp.toLocaleString();  
                         }},
       
                {field:'currentNumber',     title:'当前参团人数',  width:20,   align:'center'},
                {field:'actualNumber',     title:'实际参团人数',  width:20,   align:'center'},   
                {field:'currentAutoNumber',     title:'自动参团人数',  width:20,   align:'center'},        
                {field:'isOpenAutoJoinTeam',     title:'是否开启自动参团',  width:25,   align:'center',
                formatter:function(value,row,index){
                  if(value==0){
                    return '否';
                  }
                  else{
                    return '是';
                  }
                }},        
                {field:'hidden',  title:'操作', width:40,align:'center',
                    formatter:function(value,row,index){
                          var str;
                          if(row.isOpenAutoJoinTeam==0){
                            str='开启';
                          }
                          else if(row.isOpenAutoJoinTeam==1){
                           str='停止';
                          }
                          var a = '<a href="javascript:void(0);" onclick="autoJoinTeamConfigDiv('+index+')">自动参团设置</a>| ';
                          var b= '<a href="javascript:void(0);" onclick="setupAutoJoinTeam('+index+')">'+str+'</a>';
                          return a+b;
                        	
                    }
                }
                
            ]]
       });
  }
  
  
  function autoJoinTeamConfigDiv(index){
    var arr=$("#s_data").datagrid("getData");
    var id = arr.rows[index].id;
    $("#autoJoinTeamConfigId").val(id);

    		$.ajax({
				type:'get',
				url:'${rc.contextPath}/wechatGroup/getAutoTeamConfig/'+id,
				dataType:'json',
				success:function(data){
					if(data.status !=1){
					   
					    var randomStartSecond=data.randomStartSecond;
					    var randomEndSecond=data.randomEndSecond;
					    var autoJoinTeamNumberLimit=data.autoJoinTeamNumberLimit;
					
					    $("#autoJoinTeamNumberLimit").val(autoJoinTeamNumberLimit);
					    $("#randomStartSecond").val(randomStartSecond);
					    $("#randomEndSecond").val(randomEndSecond);
					    $("#autoJoinTeamConfigDiv").dialog('open');
					    
					}
				},
				error: function(xhr){
		        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		       }
			});
    
  }
  
  
  function setupAutoJoinTeam(index){
    var arr=$("#s_data").datagrid("getData");
    var id = arr.rows[index].id;
    var isOpenAutoJoinTeam=arr.rows[index].isOpenAutoJoinTeam==0?1:0;
     $.ajax({
				url:'${rc.contextPath}/wechatGroup/setupAutoJoinTeam',
				type:"POST",
			    data:{id:id,isOpenAutoJoinTeam:isOpenAutoJoinTeam},
			    dataType:'json',
				success:function(data){
					if(data.status==1){
					     $('#s_data').datagrid('reload');
					}
					else{
					$.messager.alert("提示",data.msg,"info");
					}
				},
				error: function(xhr){
		        	$.messager.alert("提示",'服务器忙，请稍后再试。('+xhr.status+')',"info");
		       }
			});
  }
 
</script>

</body>
</html>