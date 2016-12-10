<ul id="menu">  
</ul>
<div>
<form id="lgout" action="${rc.contextPath}/auth/lgout" type="post">
	<input type="hidden" value="0"/>
	<a href="javascript:;" onclick="lgout()" >退出&nbsp;&nbsp;</a>
</form>
</div>
<!-- 右键菜单定义如下： -->
<input type="hidden" id="tempUrl" value="" />
<div id="mm" class="easyui-menu" style="width:120px;">
	<div onclick="oopen()">新标签页打开</div>
</div>
<script>
function oopen(){
	var url = '${rc.contextPath}'+$('#tempUrl').val();
	window.open(url,'_blank');
}
$(function(){
	$('#menu').tree({
		url:'${rc.contextPath}/common/menu?nodes='+$("#nowNode").val(),
		lines:true,
		animate:true,
		onClick:function(node){
			var url = node.attributes.url;
			var menus = $('#menu').tree('getRoots');
			var menusStr = "";
			
			$.each(menus,function(i){
				var cm = menus[i];
				if(cm.state == 'open'){
					menusStr+=cm.id+"-";
				}
			});
			if(url != ""){
				if(url.indexOf("?") > 0 )
				{
					window.location.href = "${rc.contextPath}"+url+"&nodes="+menusStr;
				}else{
					window.location.href = "${rc.contextPath}"+url+"?nodes="+menusStr;
				}
			}else{
				if(node.state == 'closed'){
					$(this).tree('expand',node.target);
				}else{
					$(this).tree('collapse',node.target);
				}
			}
		},
		onContextMenu: function(e, node){
			e.preventDefault();
			var url = node.attributes.url;
			if(url != ""){//是链接
				// 查找节点
				$('#menu').tree('select', node.target);
				$('#tempUrl').val(node.attributes.url);
				// 显示快捷菜单
				$('#mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
			}
		}
	});
})
function lgout(){
	$("#lgout").submit();
}
</script>