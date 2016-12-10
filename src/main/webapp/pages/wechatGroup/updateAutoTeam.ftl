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
<style type="text/css">
textarea{ 
	resize:none;
}
</style>
</head>
<body class="easyui-layout">

<div data-options="region:'center',title:'商品信息'" style="padding:5px;">
	<form id="saveProduct"  method="post">
		<fieldset>
			
		    <input type="hidden" value="${(mwebGroupProductId?c)!'0'}" id="mwebGroupProductId" name="mwebGroupProductId" />
			<legend>商品信息</legend>
			基本商品Id：<input id="productBaseId" name="productBaseId" maxlength="10" value="<#if mwebGroupProductEntity.productBaseId?exists && mwebGroupProductEntity.productBaseId != 0>${mwebGroupProductEntity.productBaseId?c}</#if>" readonly="readonly"/>
			<font color="red" style="italic">必填&nbsp;<span id="productBaseTips"></span></font>&nbsp;&nbsp;<a onclick="editProductBase();" href="javascript:;" class="easyui-linkbutton">编辑基本商品</a><br/><br/>
			商品条码：<span id="span_barcode">${mwebGroupProductEntity.barCode!''}</span><input type="hidden" id="barcode" name="barcode" value="${mwebGroupProductEntity.barCode!}" /><br/><br/>
    		商品名称：<span id="productBaseName">${mwebGroupProductEntity.name!}</span><br/><br/>
    		商家发货名称：<input maxlength="44" type="text" id="sellerProductName" name="sellerProductName" style="width:300px;"  value="${mwebGroupProductEntity.sellerProductName!}" />
    		 <span style="color:red">提供给商家的发货表中，商家将只能看到该名称</span> <br/><br/>
    		商品类别：&nbsp;&nbsp;<br/>
			<table id="categoryTab">
    			<thead>
	    			<tr><td>一级分类</td><td>二级分类</td><td>三级分类</td></tr>
    			</thead>
    			<tbody>
    				<#if (categoryList?size>0)>
    				<#list categoryList as category>
    				<tr><td>${category.categoryFirstName!""}</td><td>${category.categorySecondName!""}</td><td>${category.categoryThirdName!""}</td>
    				</tr>
    				</#list>
    				</#if>
    			</tbody>
    		</table>
    		<br/><br/>
    		<hr/>
    		商品编码：<span id="span_code">${mwebGroupProductEntity.code!''}</span><input type="hidden" id="code" name="code" value="${mwebGroupProductEntity.code!''}" readonly="readonly" style="width: 300px;"/><br/><br/>
    		商品商家备注：<span id="productBaseRemark">${mwebGroupProductEntity.remark!""}</span><br/><br/>
    		前台展示商家名称：
    		<input type="hidden" name="sellerId" id="sellerId" value="<#if productRelationSeller.id?exists>${productRelationSeller.id?c}</#if>"/>
    		<span id="sellerName"><#if productRelationSeller.sellerName?exists>${productRelationSeller.sellerName}</#if></span>&nbsp;&nbsp;
    		真实商家名称：<span id="realSellerName"><#if productRelationSeller.realSellerName?exists>${productRelationSeller.realSellerName}</#if></span>
    		<br/><br/>
    		发货类型：<span id="sellerType"><#if sellerType?exists>${(sellerType)}</#if></span><br/><br/> 
    		发货地：<span id="sendAddress"><#if productRelationSeller.sendAddress?exists>${productRelationSeller.sendAddress}</#if></span><br/><br/> 
    		商家发货时效：<span id="sendTimeRemark"><#if productRelationSeller.sendTimeRemark?exists>${productRelationSeller.sendTimeRemark}</#if></span><br/><br/> 
			<hr/>
			
			拼团信息:<br/><br/>
    		拼团商品备注：<input maxlength="100" type="text" id="remark" name="remark" value="${mwebGroupProductEntity.remark!}" style="width:400px;"/><br/><br/>
    		核心亮点：     <input maxlength="25" type="text" id="sellingPoint" name="sellingPoint" value="${mwebGroupProductEntity.sellingPoint!}" style="width:400px;"/><font color="red" style="italic">必填(注：在长名称前用红色字展现，不超过6字)</font><br/><br/>
    		拼团商品长名称：<input maxlength="44" type="text" id="name" name="name" value="${mwebGroupProductEntity.name!}" style="width:400px;"/><font color="red" style="italic">必填</font><br/><br/>
    		拼团商品短名称：<input maxlength="25" type="text" id="shortName" name="shortName" style="width:400px;"  value="${mwebGroupProductEntity.shortName!}" /><font color="red" style="italic">必填</font><br/><br/>
    		
    		详情页格格说：
				<textarea id="desc" name="desc" onkeydown="checkEnter(event)" style="height: 60px;width: 400px">${mwebGroupProductEntity.desc!}</textarea>
				&nbsp;&nbsp;长度（&lt; 200）：<span style="color:red" id="counter">0 字</span><font>(注：从基本商品带过来，但是可以修改后独立保存)</font><br/><br/>
				
				
				
			拼团成功人数：<input type="text" id="teamNumberLimit" name="teamNumberLimit" value="${(mwebGroupProductEntity.teamNumberLimit?c)!'0'}" style="width:70px;"/>&nbsp;<font color="red" style="italic">必填</font>&nbsp;&nbsp;
			开团数：<input readonly="readonly" type='text' id='teamCount' name='teamCount' style='width:70px;' value="${(mwebGroupProductEntity.teamCount?c)!'0'}"/>&nbsp;<font color='red' style='italic'>1~9 必填</font>&nbsp;&nbsp;
			团购开始时间：<input  type='text' id='startTeamTime' name='startTeamTime' onClick='WdatePicker({dateFmt: &quot;yyyy-MM-dd HH:mm:ss&quot;})' value="${mwebGroupProductEntity.startTeamTime!}"/></br></br>
			开团有效时间：<input  type="text" id="teamValidHour" name="teamValidHour" value="${mwebGroupProductEntity.teamValidHour?c!'0'}"  style="width:70px;"/>&nbsp;<font color="red" style="italic">（单位：分钟）必填</font></br></br>
			
			团购价：<input  type="text" id="teamPrice" name="teamPrice" value="${mwebGroupProductEntity.teamPrice?string('#.##')!'0'}" style="width:70px;"/>&nbsp;<font color="red" style="italic">必填</font>&nbsp;&nbsp;
    		市场价：<input  type="text" id="marketPrice" name="marketPrice" value="${mwebGroupProductEntity.marketPrice?string('#.##')!'0'}" style="width:70px;"/>&nbsp;<font color="red" style="italic">必填</font>&nbsp;&nbsp;
			团购供货价价：<input  type="text" id="teamSupplyPrice" name="teamSupplyPrice" value="${mwebGroupProductEntity.teamSupplyPrice?string('#.##')!'0'}" style="width:70px;"/>&nbsp;<font color="red" style="italic">必填</font>&nbsp;&nbsp;
            
 

<!--     		排序值：<input  type="text" id="order" name="order"  style="width:70px;" value="${mwebGroupProductEntity.order?c}" /></br></br>	 -->
    		
<!--     		开始时间：<input value="${mwebGroupProductEntity.startTime!}" id="startTime" name="startTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/>&nbsp;&nbsp; -->
<!--    		结束时间：<input value="${mwebGroupProductEntity.endTime!}" id="endTime" name="endTime" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss'})"/><br/> -->
    		<hr/>
    		商品结算：<span id="submitType"><#if submitType?exists>${(submitType)}</#if></span><br/><br/>
    		运费结算：<span id="freightType"><#if freightType?exists>${(freightType)}</#if></span><br/><br/>
    		
			<hr/>		
			品牌&nbsp;&nbsp;：<span id="brandName">${(brandName)!}</span><input type="hidden" id="brandId" name="brandId" value="${mwebGroupProductEntity.brandId?c!}" /><br/><br/>
			净含量&nbsp;：<span id="span_netVolume">${mwebGroupProductEntity.netVolume!}</span><input type="hidden"  id="netVolume" name="netVolume" value="${mwebGroupProductEntity.netVolume!}"/><br/><br/>
			产地&nbsp;&nbsp;：<span id="span_placeOfOrigin">${mwebGroupProductEntity.placeOfOrigin!}</span><input type="hidden"  id="placeOfOrigin" name="placeOfOrigin" value="${mwebGroupProductEntity.placeOfOrigin!}"/><br/><br/>
			储存方法：<span id="span_storageMethod">${mwebGroupProductEntity.storageMethod!}</span><input type="hidden" id="storageMethod" name="storageMethod" value="${mwebGroupProductEntity.storageMethod!}" /><br/><br/>
			生产日期：<span id="span_manufacturerDate">${mwebGroupProductEntity.manufacturerDate!}</span><input type="hidden" id="manufacturerDate" name="manufacturerDate" value="${mwebGroupProductEntity.manufacturerDate!}" /><br/><br/>
			保质期&nbsp;：<span id="span_durabilityPeriod">${mwebGroupProductEntity.durabilityPeriod!}</span><input type="hidden" maxlength="15" id="durabilityPeriod" name="durabilityPeriod" value="${mwebGroupProductEntity.durabilityPeriod!}" /><br/><br/>
			适用人群：<span id="span_peopleFor">${mwebGroupProductEntity.peopleFor!}</span><input type="hidden" id="peopleFor" name="peopleFor" value="${mwebGroupProductEntity.peopleFor!}" /><br/><br/>
			食用方法：<span id="span_foodMethod">${mwebGroupProductEntity.foodMethod!}</span><input type="hidden" id="foodMethod" name="foodMethod" value="${mwebGroupProductEntity.foodMethod!}" /><br/><br/>
			使用方法：<span id="span_useMethod">${mwebGroupProductEntity.useMethod!}</span><input type="hidden" id="useMethod" name="useMethod" value="${mwebGroupProductEntity.useMethod!}" /><br/><br/>
			温馨提示: 
			<textarea name="tip" id="tip" onkeydown="checkEnter(event)" style="height: 60px;width: 400px">${(mwebGroupProductEntity.tip)!}</textarea>
			&nbsp;&nbsp;<span>注：温馨提示内容从基本商品复制过来，可以修改后独立保存</span>（长度&lt; 100；当前字数：<font id="tipCounter" color="red">0</font>）<br/><br/>
			
			<hr/> 	 		
				<!--首页图片&nbsp;<span style="color:red">尺寸要求 640*395</span><br/><br/>
		        首页图片 <input type="text" id="productImage_1" name="productImage" style="width:450px;" value="${mwebGroupProductEntity.productImage!}"  />
			<a onclick="picDialogOpen('productImage_1')" href="javascript:;" class="easyui-linkbutton" name="uploadPicture" >上传图片</a>
			<a onclick="viewPicture('productImage_1')" href="javascript:;" class="easyui-linkbutton" name="viewPicture">打开图片</a>-->
			&nbsp;<span style="color:red">必须，图片大小不得超过400KB</span><br/><br/>
			
			商品图片：&nbsp;<a onclick="copyInfo(1)" href="javascript:;" class="easyui-linkbutton">从基本商品表复制</a>&nbsp;&nbsp;
			<!--<a onclick="uploadAllImage()" href="javascript:;" class="easyui-linkbutton">上传图片压缩包</a><br/><br/>-->
				
			<span style="color:red">上传商品详情主图，尺寸要求 640*395</span><br/><br/>
			
			商品详情主图1：<input type="text" id="pic_1" name="image1" style="width:450px;" value="${mwebGroupProductEntity.image1!}"  />
			<a onclick="picDialogOpen('pic_1')" href="javascript:;" class="easyui-linkbutton" name="uploadPicture" >上传图片</a>
			<a onclick="viewPicture('pic_1')" href="javascript:;" class="easyui-linkbutton" name="viewPicture">打开图片</a>
			&nbsp;<span style="color:red">必须，图片大小不得超过400KB</span><br/><br/>
			
			商品详情主图2：<input type="text" id="pic_2" name="image2" style="width:450px;" value="${mwebGroupProductEntity.image2!}"  />
			<a onclick="picDialogOpen('pic_2')" href="javascript:;" class="easyui-linkbutton" name="uploadPicture" >上传图片</a>
			<a onclick="viewPicture('pic_2')" href="javascript:;" class="easyui-linkbutton" name="viewPicture">打开图片</a><br/><br/>	
			
			商品详情主图3：<input type="text" id="pic_3" name="image3" style="width:450px;" value="${mwebGroupProductEntity.image3!}" />
			<a onclick="picDialogOpen('pic_3')" href="javascript:;" class="easyui-linkbutton" name="uploadPicture" >上传图片</a>
			<a onclick="viewPicture('pic_3')" href="javascript:;" class="easyui-linkbutton" name="viewPicture">打开图片</a>
			<br/><br/>
			
			商品详情主图4：<input type="text" id="pic_4" name="image4" style="width:450px;" value="${mwebGroupProductEntity.image4!}"  />
			<a onclick="picDialogOpen('pic_4')" href="javascript:;" class="easyui-linkbutton" name="uploadPicture" >上传图片</a>
			<a onclick="viewPicture('pic_4')" href="javascript:;" class="easyui-linkbutton" name="viewPicture">打开图片</a>
			<br/><br/>
			
			商品详情主图5：<input type="text" id="pic_5" name="image5" style="width:450px;" value="${mwebGroupProductEntity.image5!}" />
			<a onclick="picDialogOpen('pic_5')" href="javascript:;" class="easyui-linkbutton" name="uploadPicture" >上传图片</a>
			<a onclick="viewPicture('pic_5')" href="javascript:;" class="easyui-linkbutton" name="viewPicture">打开图片</a>
			<br/><br/>
			<hr/>		
			详情页图片：&nbsp;<a onclick="copyInfo(2)" href="javascript:;" class="easyui-linkbutton">从基本商品表复制</a><br/><br/>
			
            <input type="hidden" value="${(detail_pic_id_1?c)!'0'}" name="detail_id_1" />
			图片1&nbsp;：<input type="text" value="${(detail_pic_1)!}"  id="detail_pic_1" name="detail_pic_1" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_1')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_1')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
            <input type="hidden" value="${(detail_pic_id_2?c)!'0'}" name="detail_id_2" />
			图片2&nbsp;：<input type="text" value="${(detail_pic_2)!}"  id="detail_pic_2" name="detail_pic_2" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_2')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_2')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
           <input type="hidden" value="${(detail_pic_id_3?c)!'0'}" name="detail_id_3" />
			图片3&nbsp;：<input type="text" value="${(detail_pic_3)!}"  id="detail_pic_3" name="detail_pic_3" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_3')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_3')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>

            <input type="hidden" value="${(detail_pic_id_4?c)!'0'}" name="detail_id_4" />
			图片4&nbsp;：<input type="text" value="${(detail_pic_4)!}"  id="detail_pic_4" name="detail_pic_4" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_4')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_4')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
            <input type="hidden" value="${(detail_pic_id_5?c)!'0'}" name="detail_id_5" />
			图片5&nbsp;：<input type="text" value="${(detail_pic_5)!}"  id="detail_pic_5" name="detail_pic_5" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_5')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_5')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
           <input type="hidden" value="${(detail_pic_id_6?c)!'0'}" name="detail_id_6" />
			图片6&nbsp;：<input type="text" value="${(detail_pic_6)!}"  id="detail_pic_6" name="detail_pic_6" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_6')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_6')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
            <input type="hidden" value="${(detail_pic_id_7?c)!'0'}" name="detail_id_7" />
			图片7&nbsp;：<input type="text" value="${(detail_pic_7)!}"  id="detail_pic_7" name="detail_pic_7" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_7')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_7')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_8?c)!'0'}" name="detail_id_8" />
			图片8&nbsp;：<input type="text" value="${(detail_pic_8)!}"  id="detail_pic_8" name="detail_pic_8" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_8')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_8')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_9?c)!'0'}" name="detail_id_9" />
			图片9&nbsp;：<input type="text" value="${(detail_pic_9)!}"  id="detail_pic_9" name="detail_pic_9" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_9')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_9')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_10?c)!'0'}" name="detail_id_10" />
			图片10&nbsp;：<input type="text" value="${(detail_pic_10)!}"  id="detail_pic_10" name="detail_pic_10" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_10')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_10')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_11?c)!'0'}" name="detail_id_11" />
			图片11&nbsp;：<input type="text" value="${(detail_pic_11)!}"  id="detail_pic_11" name="detail_pic_11" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_11')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_11')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_12?c)!'0'}" name="detail_id_12" />
			图片12&nbsp;：<input type="text" value="${(detail_pic_12)!}"  id="detail_pic_12" name="detail_pic_12" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_12')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_12')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_13?c)!'0'}" name="detail_id_13" />
			图片13&nbsp;：<input type="text" value="${(detail_pic_13)!}"  id="detail_pic_13" name="detail_pic_13" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_13')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_13')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_14?c)!'0'}" name="detail_id_14" />
			图片14&nbsp;：<input type="text" value="${(detail_pic_14)!}"  id="detail_pic_14" name="detail_pic_14" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_14')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_14')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_15?c)!'0'}" name="detail_id_15" />
			图片15&nbsp;：<input type="text" value="${(detail_pic_15)!}"  id="detail_pic_15" name="detail_pic_15" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_15')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_15')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_16?c)!'0'}" name="detail_id_16" />
			图片16&nbsp;：<input type="text" value="${(detail_pic_16)!}"  id="detail_pic_16" name="detail_pic_16" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_16')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_16')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_17?c)!'0'}" name="detail_id_17" />
			图片17&nbsp;：<input type="text" value="${(detail_pic_17)!}"  id="detail_pic_17" name="detail_pic_17" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_17')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_17')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_18?c)!'0'}" name="detail_id_18" />
			图片18&nbsp;：<input type="text" value="${(detail_pic_18)!}"  id="detail_pic_18" name="detail_pic_18" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_18')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_18')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_19?c)!'0'}" name="detail_id_19" />
			图片19&nbsp;：<input type="text" value="${(detail_pic_19)!}"  id="detail_pic_19" name="detail_pic_19" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_19')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_19')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_20?c)!'0'}" name="detail_id_20" />
			图片20&nbsp;：<input type="text" value="${(detail_pic_20)!}"  id="detail_pic_20" name="detail_pic_20" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_20')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_20')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_21?c)!'0'}" name="detail_id_21" />
			图片21&nbsp;：<input type="text" value="${(detail_pic_21)!}"  id="detail_pic_21" name="detail_pic_21" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_21')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_21')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			
			<input type="hidden" value="${(detail_pic_id_22?c)!'0'}" name="detail_id_22" />
			图片22&nbsp;：<input type="text" value="${(detail_pic_22)!}"  id="detail_pic_22" name="detail_pic_22" style="width:450px;" />
			<a onclick="picDialogOpen('detail_pic_22')" href="javascript:;" class="easyui-linkbutton" name="uploadDetail" >上传图片</a>
			<a onclick="viewPicture('detail_pic_22')" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
			<br/><br/>
			<hr/>		
			商品使用状态: 
			<input type="radio" name="isAvailable" value="1"  <#if mwebGroupProductEntity.isAvailable?exists && mwebGroupProductEntity.isAvailable == 1> checked</#if> > 可用&nbsp;&nbsp;&nbsp;
			<input type="radio" name="isAvailable" value="0" <#if mwebGroupProductEntity.isAvailable?exists && mwebGroupProductEntity.isAvailable == 0> checked</#if> > 停用<br/><br/>
			
 			<input type="radio" name="isOffShelves" value="0" <#if mwebGroupProductEntity.isOffShelves?exists && mwebGroupProductEntity.isOffShelves == 0> checked</#if> > 上架&nbsp;&nbsp;&nbsp;
 			<input type="radio" name="isOffShelves" value="1" <#if mwebGroupProductEntity.isOffShelves?exists && mwebGroupProductEntity.isOffShelves == 1> checked</#if>> 下架<br/><br/>
			<input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
	<div id="uploadAllImage_div" class="easyui-dialog" style="width:500px;height:400px;padding:20px 20px;">
           <form id="uploadAllImage_form" method="post" enctype="multipart/form-data">
	           <input type="file" name="imageZipFile" />
       	</form>
       	<p>
       		1:压缩格式必须为.zip，可用“360压缩”或者“好压”等软件压缩。<br><br>
       		2:压缩文件中不能包含文件夹。<br><br>
       		3:压缩文件中“文件名（不包括后缀）”必须是数字且不可重复命名。<br><br>
       		4:文件名1-5代表“商品详情主图”，可选，若有，则命名必须连续。<br><br>
       		5:文件名6-7代表“组合特卖图 和 购物车小图”，可选。<br><br>
       		6:文件名8-29代表“详情页图片”，可选，若有，则命名必须连续。<br><br>
       		7:不支持除以上外的其他文件名。<br><br>
       		8:图片后缀只允许(*.jpg)|(*.png)|(*.gif)|(*.bmp)|(*.jpeg)<br><br>
       		9:图片大小不得超过400KB。
       	</p>
	</div>
	
</div>

<div id="picDia" class="easyui-dialog" icon="icon-save" align="center"
     style="padding: 5px; width: 300px; height: 150px;">
    <form id="picForm" method="post" enctype="multipart/form-data">
        <input id="picFile" type="file" name="picFile" />&nbsp;&nbsp;<br/>    <br/>
        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
    </form>
    <br><br>
</div>
<script type="text/javascript">

function uploadAllImage(index){
    $('#uploadAllImage_div').dialog('open');
}
	function copyInfo(type){
		var id = $("#productBaseId").val();
		if($.trim(id) != ''){
			$.ajax({
	    		url: '${rc.contextPath}/productBase/copyImageFromProductBase',
		        type: 'post',
		        dataType: 'json',
		        data: {'id':id},
		        success: function(data){
		            	if(data.status == 1){
		            		if(type===1){
			                    $("#pic_1").val(data.image1);
			                    $("#pic_2").val(data.image2);
			            		$("#pic_3").val(data.image3);
			            		$("#pic_4").val(data.image4);
			            		$("#pic_5").val(data.image5);
			            		$("#pic_6").val(data.mediumImage);
			            		$("#pic_7").val(data.smallImage);
		            		}else if(type===2){
			            		$.each(data.mobileDetails,function(i,item){
			            			$("#detail_pic_"+(i+1)).val(item.pic);
			            		});	
		            		}
		            	}
		        }
	    	});
		}
	}
	
	//清空所有图片
	function clearImage(){
		for (var i=1;i<8;i++)
		{
			$("#pic_"+i).val("");
		}
		for (var i=1;i<23;i++)
		{
			$("#detail_pic_"+i).val("");
		}
	}
	
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        });
        
        $("#gegeImageId").change(function(){
        	var imageId = $(this).val();
        	var $image = $(this).next('img');
        	$.ajax({
        		url: '${rc.contextPath}/image/getGegeImageById',
		            type: 'post',
		            dataType: 'json',
		            data: {'imageId':imageId, 'isAvailable':1,type:'product'},
		            success: function(data){
		            	if(data.status == 1){
		            		$image.attr('src',data.url);	
		            	}
		            }
        	});
        });
    })
    
    $('#uploadAllImage_div').dialog({
            title:'批量上传图片',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'确认上传',
                iconCls:'icon-ok',
                handler:function(){
					$('#uploadAllImage_form').form('submit',{
						url:"${rc.contextPath}/pic/upZip",
					    onSubmit: function(){
					    	$.messager.progress();
					    },
					    success:function(data){
					    	clearImage();//清空图片
					    	$.messager.progress('close');
					    	var data = eval('(' + data + ')');  
							if(data.status == 1){
								$.messager.alert("提示",data.msg,"info",function(){
									$('#uploadAllImage_div').dialog('close');
								});	
								for(var p in data.fileUrlMap){
									$("#"+p).val(data.fileUrlMap[p]);
								}
							}else{
								$.messager.alert("提示",data.msg,"warning");
							}
					    }
					});
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#uploadAllImage_div').dialog('close');
                }
            }]
        });

    var inputId;
    function picDialogOpen($inputId) {
        inputId = $inputId;
        $("#picDia").dialog("open");
        $("#yun_div").css('display','none');
    }
    function picDialogClose() {
       $("#picDia").dialog("close");
    }
    function picUpload() {
        $('#picForm').form('submit',{
            url:"${rc.contextPath}/pic/fileUpLoad",
            success:function(data){
                var res = eval("("+data+")")
                if(res.status == 1){
                    $.messager.alert('响应信息',"上传成功...",'info',function(){
                        $("#picDia").dialog("close");
                        if(inputId) {
                            $("#"+inputId).val(res.url);
                            $("#picFile").val("");
                        }
                        return
                    });
                } else{
                    $.messager.alert('响应信息',res.msg,'error',function(){
                        return ;
                    });
                }
            }
        })
    }
    
    function viewPicture(id){
    	var url = $("#"+id).val();
    	if($.trim(url)==''){
    		$.messager.alert('提示','请上传图片后再查看','info');
    	}else{
    		window.open(url,"_blank");
    	}
    }
</script>

<script>
	$(document).keyup(function() { 
		var text=$("#desc").val(); 
		var counter=text.length;
		$("#counter").text(counter+" 字");
	});
	
	$(function(){
		$("#tip").keyup(function(){
			var text = $("#tip").val();
			$("#tipCounter").text(text.length);
		});
	});

	function checkEnter(e){
		var et=e||window.event;
		var keycode=et.charCode||et.keyCode;
		if(keycode==13){
			if(window.event)
				window.event.returnValue = false;
			else
				e.preventDefault();//for firefox
		}
	}

	
	$(function(){
        $("#productBaseId").change(function(){
        	var id = $.trim($(this).val());
        	if(id!=''){
		    	$.messager.progress();		    	
		    	$.ajax({
		    		url:'${rc.contextPath}/productBase/getProductBaseInfo?id='+id,
		    		type:'post',
		    		dataType: 'json',
		            success: function(data){
		            	$.messager.progress('close');
		            	if(data.status == 1){
		            		$("#productBaseTips").html('');
		            		$("#code").val(data.code);
		            		$("#span_code").html(data.code);
		            		$("#barcode").val(data.barcode);
		            		$("#span_barcode").html(data.barcode);
		            		$("#sellerId").val(data.sellerId);
		            		$("#sellerName").html(data.sellerName);
		            		$("#realSellerName").html(data.realSellerName);
		            		$("#sendAddress").html(data.sendAddress);
		            		$("#sellerType").html(data.sellerType);
		            		$("#sendTimeRemark").html(data.sendTimeRemark);
		            		$("#productBaseRemark").html(data.remark);
                            $('#returnDistributionProportionType').val(data.returnDistributionProportionType);
							if(data.returnDistributionProportionType == 1){
                            	$('#returnDistributionProportionType_1').prop("checked",true);
                            	$('#returnDistributionProportionType_2').prop("checked",false);
                            }else{
                                $('#returnDistributionProportionType_2').prop("checked",true);
                                $('#returnDistributionProportionType_1').prop("checked",false);
							}
		            		$("#categoryTab tbody").empty();
		            		var row = "<tr><td></td><td></td><td></td></tr>";
		            		$.each(data.categoryList,function(index,category){
		            			$("#categoryTab tbody").append(row);
		            			$("#categoryTab tbody").find("tr").last().find("td").eq(0).text(category.categoryFirstName);
		            			$("#categoryTab tbody").find("tr").last().find("td").eq(1).text(category.categorySecondName);
		            			$("#categoryTab tbody").find("tr").last().find("td").eq(2).text(category.categoryThirdName);
		            		});
		            		
		            		$("#brandId").val(data.brandId); 
		            		$("#brandName").html(data.brandName); 
		            		$("#gegeImageId").val(data.gegeImageId);
		            		$("#desc").val(data.gegeSay);
		            		$("#productBaseName").html(data.name);
		            		$("#submitType").html(data.submitType);
		            		$("#freightType").html(data.freightType);
		            		$("#netVolume").val(data.netVolume);
		            		$("#span_netVolume").html(data.netVolume);
		            		$("#placeOfOrigin").val(data.placeOfOrigin);
		            		$("#span_placeOfOrigin").html(data.placeOfOrigin);
		            		$("#manufacturerDate").val(data.manufacturerDate);
		            		$("#span_manufacturerDate").html(data.manufacturerDate);
		            		$("#storageMethod").val(data.storageMethod);
		            		$("#span_storageMethod").html(data.storageMethod);
		            		$("#durabilityPeriod").val(data.durabilityPeriod);
		            		$("#span_durabilityPeriod").html(data.durabilityPeriod);
		            		$("#peopleFor").val(data.peopleFor);
		            		$("#span_peopleFor").html(data.peopleFor);
		            		$("#foodMethod").val(data.foodMethod);
		            		$("#span_foodMethod").html(data.foodMethod);
		            		$("#useMethod").val(data.useMethod);
		            		$("#span_useMethod").html(data.useMethod);
		            		$("#tip").val(data.tip);
		            		$("#gegeImageId").find("option[value='"+data.gegeImageId+"']").attr('selected',true);
		            		$("#gegeImageView").attr('src',data.gegeImageUrl);
		            	}else{
		            		$("#productBaseTips").html('不存在 id='+id+' 的基本商品');
		            		$("#code").val('');
		            		$("#span_code").html('');
		            		$("#barcode").val('');
		            		$("#span_barcode").html('');
		            		$("#sellerId").val('');
		            		$("#sellerName").html('');
		            		$("#realSellerName").html('');
		            		$("#sendAddress").html('');
		            		$("#sellerType").html('');
		            		$("#sendTimeRemark").html('');
		            		$("#productBaseRemark").html('');
		            		$("#categoryTab tr").empty();
		            		$("#brandId").val(''); 
		            		$("#brandName").html(''); 
		            		$("#gegeImageId").val('');
		            		$("#desc").val('');
		            		$("#productBaseName").html('');
		            		$("#submitType").html('');
		            		$("#freightType").html('');
		            		$("#netVolume").val('');
		            		$("#span_netVolume").html('');
		            		$("#placeOfOrigin").val('');
		            		$("#span_placeOfOrigin").html('');
		            		$("#manufacturerDate").val('');
		            		$("#span_manufacturerDate").html('');
		            		$("#storageMethod").val('');
		            		$("#span_storageMethod").html('');
		            		$("#durabilityPeriod").val('');
		            		$("#span_durabilityPeriod").html('');
		            		$("#peopleFor").val('');
		            		$("#span_peopleFor").html('');
		            		$("#foodMethod").val('');
		            		$("#span_foodMethod").html('');
		            		$("#useMethod").val('');
		            		$("#span_useMethod").html('');
		            		$("#tip").val('');
		            		$("#gegeImageId").val('');
		            		$("#gegeImageView").attr('src','http://m.gegejia.com:80/ygg/pages/images/userimg/gege.png');
		            	}
		            },
		            error: function(xhr){
		            	$.messager.progress('close');
		            	$("#code").val('');
	            		$("#span_code").html('');
	            		$("#barcode").val('');
	            		$("#span_barcode").html('');
	            		$("#sellerId").val('');
	            		$("#sellerName").html('');
	            		$("#realSellerName").html('');
	            		$("#sendAddress").html('');
	            		$("#sellerType").html('');
	            		$("#sendTimeRemark").html('');
	            		$("#productBaseRemark").html('');
	            		$("#categoryTab tr").empty();
	            		$("#brandId").val(''); 
	            		$("#brandName").html(''); 
	            		$("#gegeImageId").val('');
	            		$("#desc").val('');
	            		$("#productBaseName").html('');
	            		$("#submitType").html('');
	            		$("#freightType").html('');
	            		$("#netVolume").val('');
	            		$("#span_netVolume").html('');
	            		$("#placeOfOrigin").val('');
	            		$("#span_placeOfOrigin").html('');
	            		$("#manufacturerDate").val('');
	            		$("#span_manufacturerDate").html('');
	            		$("#storageMethod").val('');
	            		$("#span_storageMethod").html('');
	            		$("#durabilityPeriod").val('');
	            		$("#span_durabilityPeriod").html('');
	            		$("#peopleFor").val('');
	            		$("#span_peopleFor").html('');
	            		$("#foodMethod").val('');
	            		$("#span_foodMethod").html('');
	            		$("#useMethod").val('');
	            		$("#span_useMethod").html('');
	            		$("#tip").val('');
	            		$("#gegeImageId").val('');
	            		$("#gegeImageView").attr('src','http://m.gegejia.com:80/ygg/pages/images/userimg/gege.png');
		            }
		    	});
        	}
        });
		
		$('#saveProduct').form({   
		    url:'${rc.contextPath}/wechatGroup/save/update',   
		    onSubmit: function(){   
		        $.messager.progress();
		    },   
		    success:function(data){
		    	$.messager.progress('close');
		    	var data = eval('(' + data + ')');  // change the JSON string to javascript object   
		        if (data.status == 1){
		            window.location.href = "${rc.contextPath}/wechatGroup/list?isAvailable=${isAvailable}";
		        }else{
		        	$.messager.alert("提示",data.msg,"error");	
		        }
		    }
		}); 
	
	    $("#saveButton").click(function(){
	    	var pic_1 = $("#pic_1").val();
	    	var productBaseId = $("#productBaseId").val();
	    	var name = $("#name").val();
	    	var sellerProductName = $("#sellerProductName").val();
	    	var shortName = $("#shortName").val();
	    	var sellingPoint=$("#sellingPoint").val();
	    	var marketPrice = $("#marketPrice").val();
	    	var teamSupplyPrice = $('#teamSupplyPrice').val();
	    	var singlePrice = $("#singlePrice").val();
	    	var teamPrice=$("#teamPrice").val();
	    	var teamNumberLimit= $("#teamNumberLimit").val();
	    	var stock = $('#stock').val();
	    	var smallImage = $("#pic_7").val();
	    	var mediumImage = $("#pic_6").val();
            var teamValidHour=$("#teamValidHour").val();
	    	var text=$("#desc").val(); 
	    	var tip = $("#tip").val();
	    	var startTeamTime=$("#startTeamTime").val(); 
	    	var teamCount = $("#teamCount").val();
			var counter=text.length; 
			//var productImage_1=$("#productImage_1").val();
			if(productBaseId == ''){
				$.messager.alert("提示","请输入基本商品Id","info");
			}else if(!/^[0-9.]+$/.test(marketPrice) || !/^[0-9.]+$/.test(teamPrice) || !/^[0-9.]+$/.test(teamSupplyPrice)){
				$.messager.alert("提示","价格不能除数字和.外的其他符号。","info");
			}else if(counter > 200){
	    		$.messager.alert("提示","格格说字数不得超过200","info");
	    	}else if(name == "" || shortName == "" || sellerProductName == ""){
	    		$.messager.alert("提示","商品名称或者短名称或者商家发货名称都必填","info");
	    	}
	    	else if(shortName.length>20){
	    		$.messager.alert("提示","短名称不能超过20个字","info");
	    	}
	    	else if(sellingPoint == ""){
	    		$.messager.alert("提示","核心亮点必填","info");
	    	}
	    	else if(sellingPoint.length>6){
	    		$.messager.alert("提示","核心亮点不能超过6个字","info");
	    	}
	    	else if(teamNumberLimit == ""){
	    		$.messager.alert("提示","拼团成功人数必填","info");
	    	}
	    	else if(marketPrice == ""||teamPrice==""){
	    		$.messager.alert("提示","商品售价必填","info");
	    	}else if(tip.length > 99){
	    		$.messager.alert("提示","温馨提示字数不得超过100","info");
	    	}
	    	/**else if(productImage_1 == ""){
	    		$.messager.alert("提示","商品主图必填","info");
	    	}**/else if(pic_1 == ""){
	    		$.messager.alert("提示","商品详情主图1必填","info");
	    	}else if(teamValidHour==""){
	    	  $.messager.alert("提示","开团有效时间必填","info");
	    	}
	    	else if(!/^[0-9]+$/.test(teamValidHour)){
	    	  $.messager.alert("提示","开团有效时间不能除数字外的其他符号。","info");
	    	}
	    	else if(teamCount>9){
	    	  $.messager.alert("提示","开团数不能大于9","info");
	    	}
	    	else if(startTeamTime==""){
	    	  $.messager.alert("提示","开团时间不能为空","info");
	    	}
	    	else{
	    		$('#saveProduct').submit();
	    	}
    	});
    	

	});
	
function editProductBase(){
		var productBaseId = $("#productBaseId").val();
		var url="${rc.contextPath}/productBase/toAddOrUpdate?id="+productBaseId;
		window.open(url,"_blank");
	}
</script>

</body>
</html>