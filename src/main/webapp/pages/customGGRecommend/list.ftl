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

<div data-options="region:'center'" style="padding:5px;">
	<div id="cc" class="easyui-layout" data-options="fit:true" >
		<div data-options="region:'north',title:'格格推荐-旧版本',split:true" style="height: 90px;">
			<form id="searchForm" method="post">
				<table cellpadding="15">
					<tr>
	                	<td>进行状态：</td>
						<td><select id="status" name="status" class="easyui-combobox" style="width:150px;"><option value="-1" selected="selected">-全部-</option><option value="1">进行中</option><option value="2">即将开始</option><option value="3">已结束</option></select></td>
	                    <td>展现状态：</td>
		                <td><select id="isDisplay" name="isDisplay" class="easyui-combobox" style="width:150px;"><option value="-1" selected="selected">-全部-</option><option value="1">展示</option><option value="0">不展示</option></select></td>
		                <td>
							<a id="searchBtn" onclick="searchList()" href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search',width:'80px'">查 询</a>
	                	</td>
	                </tr>
				</table>
			</form>
		</div>
		
		<div data-options="region:'center'" >
			<!--数据表格-->
    		<table id="s_data" style=""></table>
    			
		    <!-- 新增 begin -->
		    <div id="editCustomGGRecommendDiv" class="easyui-dialog" style="width:800px;height:700px;padding:15px 20px;">
		        <form id="editCustomGGRecommendForm" method="post">
					<input id="id" type="hidden" name="id" value="" >
					<p>
						<span>选择布局方式：</span>
						<span><input type="radio" name="displayType" value="1"/>一行1个&nbsp;&nbsp;</span>
						<span><input type="radio" name="displayType" value="2"/>一行2个&nbsp;&nbsp;</span>
						<span style="color: red">必填</span>
					</p>
					<p>
						<span>开始时间：</span>
						<span><input type="text" name="startTime" id="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 10:00:00',maxDate:'#F{$dp.$D(\'endTime\')}'})"/></span>
						<span>结束时间：</span>
						<span><input type="text" name="endTime" id="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 10:00:00',minDate:'#F{$dp.$D(\'startTime\')}'})"/></span>
						<span style="color: red">必填</span>
					</p>
					<div id="one">
						<p>
							<span>第一个备注：</span>
							<span><input type="text" name="oneRemark" id="oneRemark" value="" maxlength="100" style="width: 300px;"/></span>
						</p>
						<p>
							<span>图片：</span>
							<span><input type="text" name="oneImage" id="oneImage" readonly="readonly" style="width: 300px;"/></span>
							<span><a onclick="picDialogOpen('oneImage','oneImg')" class="easyui-linkbutton">上传图片</a></span>
							<span style="color: red">必填&nbsp;&nbsp;</span><span style="color: red" id="oneTip"></span><br>
							<span><img id="oneImg" src=""></span>
						</p>
						<p>
							<span>关联类型：</span>
							<span><input type="radio" name="oneType" value="2"/>组合&nbsp;&nbsp;</span>
							<span><input type="radio" name="oneType" value="3"/>网页自定义活动&nbsp;&nbsp;</span>
							<span><input type="radio" name="oneType" value="7"/>原生自定义活动&nbsp;&nbsp;</span>
							<span style="color: red">必填</span>
						</p>
						<p id="oneActivitiesCommon">
							<span>组合ID：</span>
							<span><input type="text" name="oneActivitiesCommonId" id="oneActivitiesCommonId" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="10" style="width: 200px;"/></span><span style="color: red">必填</span>
							<span style="color: red" id="oneActivitiesCommonDesc"></span>
						</p>
						<p id="oneActivitiesCustom">
							<span>自定义活动：</span>
							<span><input type="text" name="oneActivitiesCustomId" id="oneActivitiesCustomId" style="width: 200px;"/></span><span style="color: red">必填</span>
						</p>
						<p id="onePage">
							<span>原生自定义活动：</span>
							<span><input type="text" name="onePageId" id="onePageId" style="width: 200px;"/></span><span style="color: red">必填</span>
						</p>
					</div>
					<div id="two">
						<hr>
						<p>
							<span>第二个备注：</span>
							<span><input type="text" name="twoRemark" id="twoRemark" value="" maxlength="100" style="width: 300px;"/></span>
						</p>
						<p>
							<span>图片：</span>
							<span><input type="text" name="twoImage" id="twoImage" readonly="readonly" style="width: 300px;"/></span>
							<span><a onclick="picDialogOpen('twoImage','twoImg')" class="easyui-linkbutton">上传图片</a></span>
							<span style="color: red">必填&nbsp;&nbsp;</span><span style="color: red" id="twoTip"></span><br>
							<span><img id="twoImg" src=""></span>
						</p>
						<p>
							<span>关联类型：</span>
							<span><input type="radio" name="twoType" value="2"/>组合&nbsp;&nbsp;</span>
							<span><input type="radio" name="twoType" value="3"/>网页自定义活动&nbsp;&nbsp;</span>
							<span><input type="radio" name="twoType" value="7"/>原生自定义活动&nbsp;&nbsp;</span>
							<span style="color: red">必填</span>
						</p>
						<p id="twoActivitiesCommon">
							<span>组合ID：</span>
							<span><input type="text" name="twoActivitiesCommonId" id="twoActivitiesCommonId" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9-]+/,'');}).call(this)" maxlength="10" style="width: 200px;"/></span><span style="color: red">必填</span>
							<span style="color: red" id="twoActivitiesCommonDesc"></span>
						</p>
						<p id="twoActivitiesCustom">
							<span>自定义活动：</span>
							<span><input type="text" name="twoActivitiesCustomId" id="twoActivitiesCustomId" style="width: 200px;"/></span><span style="color: red">必填</span>
						</p>
						<p id="twoPage">
							<span>原生自定义活动：</span>
							<span><input type="text" name="twoPageId" id="twoPageId" style="width: 200px;"/></span><span style="color: red">必填</span>
						</p>
					</div>
					<hr>
					<p id="display">
						<span>可见状态：</span>
						<span><input type="radio" name="isDisplay" value="1"/>可见&nbsp;&nbsp;</span>
						<span><input type="radio" name="isDisplay" value="0"/>不可见</span>
						<span style="color: red">必填</span>
					</p>
		    	</form>
		    </div>
		    <!-- 新增 end -->
		</div>
	</div>
</div>

<div id="picDia" class="easyui-dialog" closed="true" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
    	<input type="hidden" name="needWidth" id="needWidth" value="0">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/><br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>

<script>
$(function(){
	$(document).keydown(function(e){
		if (!e){
		   e = window.event;  
		}  
		if ((e.keyCode || e.which) == 13) {  
		      $("#searchBtn").click();  
		}
	});
});

var imageId = '';
var imgSrc = '';
function picDialogOpen(id, img) {
	imageId = id;
	imgSrc = img;
	if(typeof($('input[name=displayType]:checked').val()) == 'undefined') {
		$.messager.alert('警告','先选择布局方式，然后上次对应尺寸的图片', 'warning');
		return false;
	}
	if($('input[name=displayType]:checked').val() == 1) {
		$('#needWidth').val(730);
	}
	if($('input[name=displayType]:checked').val() == 2) {
		$('#needWidth').val(360);
	}
	$('picForm').form('reset');
	$('#picDia').dialog('open');
}
//图片上传
function picUpload() {
    $('#picForm').form('submit',{
        url:"${rc.contextPath}/pic/fileUpLoad",
        async:false,
        success:function(data) {
            var res = eval("("+data+")")
            if(res.status == 1) {
            	$.messager.alert('响应信息',"上传成功...",'info', function() {
        			$("#picDia").dialog("close");
                    $('#' + imageId).val(res.url);
                    $('#' + imgSrc).attr('src', res.url);
                    $('#' + imgSrc).show();
        		});
            } else{
                $.messager.alert('响应信息',res.msg,'error',function(){
                    return ;
                });
            }
        }
    })
}

function strToDate(str) {
 var tempStrs = str.split(" ");
 var dateStrs = tempStrs[0].split("-");
 var year = parseInt(dateStrs[0], 10);
 var month = parseInt(dateStrs[1], 10) - 1;
 var day = parseInt(dateStrs[2], 10);
 var timeStrs = tempStrs[1].split(":");
 var hour = parseInt(timeStrs [0], 10);
 var minute = parseInt(timeStrs[1], 10) - 1;
 var second = parseInt(timeStrs[2], 10);
 var date = new Date(year, month, day, hour, minute, second);
 return date;
}
function checkOne() {
	if($('#oneImage').val() == '') {
		$.messager.alert('警告','图片不能为空', 'warning');
		return false;
	}
	if(typeof($('input[name=oneType]:checked').val()) == 'undefined') {
		$.messager.alert('警告','关联类型不能为空', 'warning');
		return false;
	}
	if($('input[name=oneType]:checked').val() == 2) {
		if($('#oneActivitiesCommonId').val() == '') {
			$.messager.alert('警告','组合ID不能为空', 'warning');
			return false;
		}
	} else if($('input[name=oneType]:checked').val() == 3) {
		if($('#oneActivitiesCustomId').combobox('getValue') == '') {
			$.messager.alert('警告','自定义活动不能为空', 'warning');
			return false;
		}
	} else if($('input[name=oneType]:checked').val() == 7) {
		if($('#onePageId').combobox('getValue') == '') {
			$.messager.alert('警告','原生自定义活动不能为空', 'warning');
			return false;
		}
	}
	return true;
}
function checkTwo() {
	if($('#twoImage').val() == '') {
		$.messager.alert('警告','第二张图片不能为空', 'warning');
		return false;
	}
	if(typeof($('input[name=twoType]:checked').val()) == 'undefined') {
		$.messager.alert('警告','关联类型不能为空', 'warning');
		return false;
	}
	if($('input[name=twoType]:checked').val() == 2) {
		if($('#twoActivitiesCommonId').val() == '') {
			$.messager.alert('警告','组合ID不能为空', 'warning');
			return false;
		}
	} else if($('input[name=twoType]:checked').val() == 3) {
		if($('#twoActivitiesCustomId').combobox('getValue') == '') {
			$.messager.alert('警告','自定义活动不能为空', 'warning');
			return false;
		}
	} else if($('input[name=twoType]:checked').val() == 7) {
		if($('#twoPageId').combobox('getValue') == '') {
			$.messager.alert('警告','原生自定义活动不能为空', 'warning');
			return false;
		}
	}
	return true;
}
	// 验证表单是否是否有效
	function valid() {
		if(typeof($('input[name=displayType]:checked').val()) == 'undefined') {
			$.messager.alert('警告','布局方式不能为空', 'warning');
			return false;
		}
		if($('#startTime').val() == '') {
			$.messager.alert('警告','开始时间不能为空', 'warning');
			return false;
		}
		if($('#endTime').val() == '') {
			$.messager.alert('警告','结束时间不能为空', 'warning');
			return false;
		}
		if($('input[name=displayType]:checked').val() == 1) {
			if(!checkOne()) {
				return false;
			}
		}
		if($('input[name=displayType]:checked').val() == 2) {
			if(!checkOne()) {
				return false;
			}
			if(!checkTwo()) {
				return false;
			}
		}
		if($('#id').val() == '') {
			if(typeof($('input[name=isDisplay]:checked').val()) == 'undefined') {
				$.messager.alert('警告','可见状态不能为空');
				return false;
			}
		}
		return true;
	}
	function initForm() {
		$('#editCustomGGRecommendForm').form('clear');
		$('#oneActivitiesCommonDesc').text('');
		$('#oneActivitiesCommon').hide();
		$('#oneActivitiesCustom').hide();
		$('#onePage').hide();
		$('#twoActivitiesCommonDesc').text('');
		$('#twoActivitiesCommon').hide();
		$('#twoActivitiesCustom').hide();
		$('#twoPage').hide();
		$('#two').hide();
		$('#oneTip').text('');
		$('#twoTip').text('');
		if($('#oneImage').val() == '') {
			$('#oneImg').hide();
		}
		if($('#twoImage').val() == '') {
			$('#twoImg').hide();
		}
		$.ajax({
			url:'${rc.contextPath}/customActivities/jsonCustomActivitiesCode',
			type:'POST',
			async:false,
			success:function(data) {
				$("#oneActivitiesCustomId").combobox({
					data:data,
					editable : false,
				    valueField:'code',
				    textField:'text' 
				});
				$("#twoActivitiesCustomId").combobox({
					data:data,
					editable : false,
				    valueField:'code',
				    textField:'text' 
				});
			}
		});
		$.ajax({
			url:'${rc.contextPath}/page/ajaxAppCustomPage',
			type:'POST',
			async:false,
			success:function(data) {
				$("#onePageId").combobox({
					data:data,
		            editable : false,
		            valueField:'code',
		            textField:'text'
		        });
				$("#twoPageId").combobox({
					data:data,
		            editable : false,
		            valueField:'code',
		            textField:'text'
		        });
			}
		});
	}
	//编辑
	function edit(id) {
		$.messager.progress();
		initForm();
		$('#display').hide();
		$.ajax({
			url: '${rc.contextPath}/customGGRecommend/findById?id=' + id,
			type:"POST",
			async:false,
			success: function(data) {
				$.messager.progress('close');
				if(data.status == 1) {
					$('#editCustomGGRecommendForm').form('load',data.data);
					if(data.data.displayType == 2) {
						$('#two').show();
						$('#twoTip').text('宽度：360px');
						$('#oneTip').text('宽度：360px');
						if(data.data.twoType == 2) {
							$('#twoActivitiesCommon').show();
							$('#twoActivitiesCommonId').val(data.data.twoDisplayId);
						} else if(data.data.twoType == 3) {
							$('#twoActivitiesCustom').show();
							$('#twoActivitiesCustomId').combobox('setValue', data.data.twoDisplayId);
						} else if(data.data.twoType == 7) {
							$('#twoPage').show();
							$('#twoPageId').combobox('setValue', data.data.twoDisplayId);
						}
						if(data.data.twoImage != '') {
							$('#twoImg').attr('src', data.data.twoImage);
							$('#twoImg').show();
						}
					} else {
						$('#oneTip').text('宽度：730px');
					}
					if(data.data.oneType == 2) {
						$('#oneActivitiesCommon').show();
						$('#oneActivitiesCommonId').val(data.data.oneDisplayId);
					} else if(data.data.oneType == 3) {
						$('#oneActivitiesCustom').show();
						$('#oneActivitiesCustomId').combobox('setValue', data.data.oneDisplayId);
					} else if(data.data.oneType == 7) {
						$('#onePage').show();
						$('#onePageId').combobox('setValue', data.data.oneDisplayId);
					}
					if(data.data.oneImage != '') {
						$('#oneImg').attr('src', data.data.oneImage);
						$('#oneImg').show();
					}
					$('#editCustomGGRecommendDiv').dialog('open');
		        } else{
		        	$.messager.progress('close');
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			},
			error : function() {
				$.messager.progress('close');
			}
		});
	}
	// 搜索按钮
	function searchList() {
		$('#s_data').datagrid('load', {
			status:$('#status').combobox('getValue'),
			isDisplay:$('#isDisplay').combobox('getValue')
		});
	}
	// 编辑排序
	function editSequence(index){
		$('#s_data').datagrid('beginEdit', index);
	};
	// 保存保存
	function saverow(index){
		$('#s_data').datagrid('endEdit', index);
	};
	// 取消编辑
	function cancelrow(index){
		$('#s_data').datagrid('cancelEdit', index);
	};
	// 跟新gird行数据
	function updateActions(){
		var rowcount = $('#s_data').datagrid('getRows').length;
		for(var i=0; i<rowcount; i++){
			$('#s_data').datagrid('updateRow',{
		    	index:i,
		    	row:{}
			});
		}
	}

	// 跟新导航排序值
	function updateActivity(row){
		$.ajax({
			url: '${rc.contextPath}/customGGRecommend/saveOrUpdate',
			type:"POST",
			data: {id:row.id,sequence:row.sequence},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	};
	// 更新导航展示
	function editIsDisplay(id,isDisplay) {
		$.ajax({
			url: '${rc.contextPath}/customGGRecommend/saveOrUpdate',
			type:"POST",
			data: {id:id,isDisplay:isDisplay},
			success: function(data) {
				if(data.status == 1){
					$('#s_data').datagrid('load');
		            return
		        } else{
		            $.messager.alert('响应信息',data.msg,'error');
		        }
			}
		});
	} 
	
	$(function() {
		$('input[name=displayType]').change(function() {
			$('#oneImage').val('');
			$('#oneImg').attr('src','');
			$('#twoImg').attr('src','');
			$('#twoImage').val('');
			$('#oneImg').hide();
			$('#twoImg').hide();
			if($('input[name=displayType]:checked').val() == 2) {
				$('#two').show();
				$('#oneTip').text('宽度：360px');
				$('#twoTip').text('宽度：360px');
			}
			if($('input[name=displayType]:checked').val() == 1) {
				$('#two').hide();
				$('#oneTip').text('宽度：730px');
			}
		});
		// 填写组合ID后
		$('#oneActivitiesCommonId').blur(function() {
			if($('#oneActivitiesCommonId').val().length < 1)
				return;
			$.ajax({
                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
                type: 'POST',
                data: {id:$('#oneActivitiesCommonId').val()},
                success: function(data) {
                    if(data.status == 1) {
                    	$('#oneActivitiesCommonDesc').text(data.name + "-" + data.remark);
                    }else{
                    	$.messager.alert("提示",data.msg,"info");
                    }
                }
            });
		});
		// 填写组合ID后
		$('#twoActivitiesCommonId').blur(function() {
			if($('#twoActivitiesCommonId').val().length < 1)
				return;
			$.ajax({
                url: '${rc.contextPath}/sale/findActivitiesCommonInfoById',
                type: 'POST',
                data: {id:$('#twoActivitiesCommonId').val()},
                success: function(data) {
                    if(data.status == 1) {
                    	$('#twoActivitiesCommonDesc').text(data.name + "-" + data.remark);
                    }else{
                    	$.messager.alert("提示",data.msg,"info");
                    }
                }
            });
		});
		// 选择类型展示不同的信息
		$('input[name=oneType]').change(function() {
			var oneType = $('input[name=oneType]:checked').val();
			if(oneType == 2) {
				$('#oneActivitiesCommon').show();
				$('#oneActivitiesCustom').hide();
				$('#onePage').hide();
			} else if(oneType == 3) {
				$('#oneActivitiesCommon').hide();
				$('#oneActivitiesCustom').show();
				$('#onePage').hide();
			} else if(oneType == 7) {
				$('#oneActivitiesCommon').hide();
				$('#oneActivitiesCustom').hide();
				$('#onePage').show();
			}
		});
		// 选择类型展示不同的信息
		$('input[name=twoType]').change(function() {
			var twoType = $('input[name=twoType]:checked').val();
			if(twoType == 2) {
				$('#twoActivitiesCommon').show();
				$('#twoActivitiesCustom').hide();
				$('#twoPage').hide();
			} else if(twoType == 3) {
				$('#twoActivitiesCommon').hide();
				$('#twoActivitiesCustom').show();
				$('#twoPage').hide();
			} else if(twoType == 7) {
				$('#twoActivitiesCommon').hide();
				$('#twoActivitiesCustom').hide();
				$('#twoPage').show();
			}
		});
		$('#s_data').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/customGGRecommend/listInfo',
            loadMsg:'正在装载数据...',
            singleSelect:false,
            fitColumns:true,
            remoteSort: true,
            pageSize:50,
            pageList:[50,60],
            columns:[[
                {field:'id',    title:'ID', width:15, align:'center'},
                {field:'isDisplay',    title:'展现状态', width:15, align:'center',
            		formatter:function(value,row,index){
 						if(row.isDisplay == 1)
 							return '展示';
 						else if(row.isDisplay == 0)
 							return '不展示';
 						else 
 							return row.isDisplay;
                }},
                {field:'status',    title:'进行状态', width:30, align:'center',
               		formatter:function(value,row,index) {
               			var startTime = strToDate(row.startTime);
               			var endTime = strToDate(row.endTime);
               			var now = new Date();
               			var tstartTime = startTime.valueOf();
               			var tendTime = endTime.valueOf();
               			var tnow = now.valueOf();
               			
               			
               			var day = 86400000;// 一天的毫秒数
   						if(tendTime < tnow)
   							return '已结束';
   						else if(tstartTime <= tnow && tnow <= tendTime)
   							return '进行中';
   						else if(tstartTime > tnow) {
   							var t = (tstartTime - tnow) / day;
   							t = t.toFixed();
   							if(t < 2)
   								return '即将开始';
   							else
   								return '还有' + (t - 1) + '天即将开始';
   						}
   						else 
   							return '';
                   	}
                },
                {field:'oneRemark',    title:'第一个备注', width:80, align:'center'},
                {field:'twoRemark',    title:'第二个备注', width:80, align:'center'},
                {field:'startTime',    title:'开始时间', width:30, align:'center'},
                {field:'endTime',    title:'结束时间', width:30, align:'center'},
                {field:'sequence',     title:'排序值',  width:15, align:'center',editor:{type:'validatebox',options:{required:true}}},
                {field:'hidden',  title:'操作', width:30, align:'center',
					formatter : function(value, row, index) {
						if (row.editing) {
							var a = '<a href="javascript:void(0);" onclick="saverow(' + index + ')">保存</a> | ';
							var b = '<a href="javascript:void(0);" onclick="cancelrow(' + index + ')">取消</a>';
							return a + b;
						} else {
							var a = '<a href="javascript:void(0);" onclick="edit(' + row.id + ')">编辑</a> | ';
							var b = '<a href="javascript:void(0);" onclick="editSequence(' + index + ')">改排序</a> | ';
							var c = '';
							if (row.isDisplay == 1)
								c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 0 + ')">不展示</a>';
							if (row.isDisplay == 0)
								c = '<a href="javascript:void(0);" onclick="editIsDisplay(' + row.id + ',' + 1 + ')">展示</a>';
							return b + a + c;
						}
					}
				} ] ],
			toolbar : [ {
				id : '_add',
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					$('#editCustomGGRecommendForm').form('clear');
					$('#editCustomGGRecommendDiv').dialog('open');
					initForm();
					$('#display').show();
				}
			} ],
			onBeforeEdit:function(index,row){
            	row.editing = true;
            	updateActions();
        	},
        	onAfterEdit:function(index,row){
            	row.editing = false;
            	updateActions();
            	updateActivity(row);
        	},
        	onCancelEdit:function(index,row){
            	row.editing = false;
            	updateActions();
        	},
			pagination : true
		});

		$('#editCustomGGRecommendDiv').dialog({
			title : '新增修改格格推荐',
			collapsible : true,
			closed : true,
			modal : true,
			buttons : [ {
				text : '保存',
				iconCls : 'icon-ok',
				handler : function() {
					$('#editCustomGGRecommendForm').form('submit', {
						url : "${rc.contextPath}/customGGRecommend/saveOrUpdate",
						onSubmit : function() {
							return valid();
						},
						success : function(data) {
							var res = eval("("+data+")");
							if (res.status == 1) {
								$.messager.alert('响应信息', "保存成功", 'info', function() {
									$('#s_data').datagrid('load');
									$('#editCustomGGRecommendDiv').dialog('close');
								});
							} else if (res.status == 0) {
								$.messager.alert('响应信息', res.msg, 'error');
							}
						}
					});
				}
			}, {
				text : '取消',
				align : 'left',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#editCustomGGRecommendDiv').dialog('close');
				}
			} ]
		});
	});
</script>

</body>
</html>