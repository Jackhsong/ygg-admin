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
.input-xxlarge{
	width: 400px;
	height: 20px;
}
.input-xlarge{
	width:300px;
	height: 20px;
}
.input-mini{
	width:50px;
	height: 15px;
}
.selectStyle{
	width:200px;
	height:20px;
}
textarea{ 
 	resize:none;
}                                                               
</style>
</head>
<body class="easyui-layout">
	<div data-options="region:'center',title:'添加商品信息'" style="padding:5px;">
		<form id="productBaseForm" action="post">
			<fieldset>
				<legend>商品基本信息</legend>
				<input type="hidden" value="1" name="saveType" id="saveType"/>
				<input type="hidden" value="<#if product.id?exists && (product.id != 0)>${product.id?c}</#if>" id="editId" name="id" />
				<table id="baseInfo">
					<tr>
						<td><label for="barcode">商品条码：</label><br/><br/></td>
						<td>
							<input type="text" class="input-xxlarge" id="barcode" name="barcode" maxlength="20" value="<#if product.barcode?exists>${product.barcode}</#if>"/><br/><br/>
						</td>
					</tr>
					<tr>
						<td><label for="productName">商品名称：</label><br/><br/></td>
						<td>
							<input type="text" class="input-xxlarge" id="productName" name="name" maxlength="44" value="<#if product.name?exists>${product.name}</#if>"/>
							<font color="red" size="2px">必填</font>
						<br/><br/></td>
					</tr>
<!-- 					<tr>
						<td><label for="categoryFirstId">商品类目：</label><br/><br/></td>
						<td>
							<table id="categoryTab">
								<#if (categoryList?size>0)>
								<#list categoryList as category>
								<tr>
									<td style="display:none">${category.id}</td>
									<td>
										<select name="categoryFirstId" class="selectStyle">
											<option value="0">-请选择一级分类-</option>
											<#list category.catetgoryFirstList as first>
    		 			  					<option value="${first.id?c}"<#if category.categoryFirstId?exists && (category.categoryFirstId == first.id)>selected</#if>>${first.name}</option>
    		 			  					</#list>
										</select>
									</td>
									<td>
										<select name="categorySecondId" class="selectStyle">
	    		 							<option value="0">-请选择二级分类-</option>
	    		 			  				<#list category.categorySecondList as second>
	    		 			  				<option value="${second.id?c}"<#if category.categorySecondId?exists && (category.categorySecondId == second.id)>selected</#if>>${second.name}</option>
	    		 			  				</#list>	
    		 							</select>
									</td>
									<td>
										<select name="categoryThirdId" class="selectStyle">
	    		 							<option value="0">-请选择三级分类-</option>
	    		 			  				<#list category.categoryThirdList as third>
	    		 			  				<option value="${third.id?c}"<#if category.categoryThirdId?exists && (category.categoryThirdId == third.id)>selected</#if>>${third.name}</option>
	    		 			  				</#list>	
    		 							</select>
									</td>
									<td>
										<span onclick="addCategoryRow(this)" style="cursor: pointer;color:red">&nbsp;添加&nbsp;|</span>
	                    				<span onclick="delCategoryRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
									</td>
								</tr>
								</#list>
								<#else>
								<tr>
									<td style="display:none;">0</td>
									<td>
										<select name="categoryFirstId" class="selectStyle">
											<option value="0">-请选择一级分类-</option>
											<#list catetgoryFirstList as first>
    		 			  					<option value="${first.id?c}">${first.name}</option>
    		 			  					</#list>
										</select>
									</td>
									<td>
										<select name="categorySecondId" class="selectStyle">
	    		 							<option value="0">-请选择二级分类-</option>
	    		 			  				<#list categorySecondList as second>
	    		 			  				<option value="${second.id?c}">${second.name}</option>
	    		 			  				</#list>	
    		 							</select>
									</td>
									<td>
										<select name="categoryThirdId" class="selectStyle">
	    		 							<option value="0">-请选择三级分类-</option>
	    		 			  				<#list categoryThirdList as third>
	    		 			  				<option value="${third.id?c}">${third.name}</option>
	    		 			  				</#list>	
    		 							</select>
									</td>
									<td>
										<font color="red" size="2px">必填</font>
										<span onclick="addCategoryRow(this)" style="cursor: pointer;color:red">&nbsp;添加&nbsp;|</span>
	                    				<span onclick="delCategoryRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
									</td>
								</tr>									
								</#if>
							</table>
						<br/><br/></td>
					</tr> -->
					<tr>
						<td><label for="remark">商家商品备注：</label><br/><br/></td>
						<td>
							<input type="text" class="input-xxlarge" id="remark" name="remark" maxlength="100" value="<#if product.remark?exists>${product.remark}</#if>"/>
						<br/><br/></td>
					</tr>
					<tr><td colspan="2"><hr/></td></tr>
					<tr>
						<td><label for="sellerId">商家名称：</label><br/><br/></td>
						<td>
							<input type="text" class="input-xxlarge" id="sellerId" name="sellerId" <#if canEdit?exists && (canEdit>0)>readonly="readonly"</#if>/>
							<font color="red" size="2px">必填</font>
						<br/><br/></td>
					</tr>
					<tr>
						<td><label for="sendType">发货类型：</label><br/><br/></td>
						<td>
							<input type="radio" name="sellerType"  id="sendType_1" disabled="disabled" value="1" <#if seller.sellerType?exists && (seller.sellerType <= 1) >checked</#if>>国内
						<br/><br/>
						</td>
					</tr>
		

					<tr>
						<td><label for="freightType">运费结算方式：</label><br/><br/></td>
						<td>
							<input type="radio" name="freightType" id="freightType1" disabled="disabled" value="1" <#if seller.freightType?exists && (seller.freightType==1)>checked</#if>> 包邮
							<input type="radio" name="freightType" id="freightType2" disabled="disabled" value="2" <#if seller.freightType?exists && (seller.freightType==2)>checked</#if>> 满
							<span name="freightMoney" id="freightMoney"><#if freightMoney?exists>${freightMoney}</#if></span>包邮
							<input type="radio" name="freightType" id="freightType3" disabled="disabled" value="3" <#if seller.freightType?exists && (seller.freightType==3)>checked</#if>> 全部不包邮
							<input type="radio" name="freightType" id="freightType4" disabled="disabled" value="4" <#if seller.freightType?exists && (seller.freightType==4)>checked</#if>> 其他
							(<span name="freightOther" id="freightOther"><#if product.freightOther?exists>${product.freightOther}</#if></span>)
						<br/><br/></td>
					</tr>
					<tr>
						<td><label>发货时效说明：</label><br/><br/></td>
						<td><span id="sendTimeRemark" name="sendTimeRemark" ><#if seller.sendTimeRemark?exists>${seller.sendTimeRemark}</#if></span><br/><br/></td>
					</tr>
					<tr>
						<td><label>发货依据：</label><br/><br/></td>
						<td><span id="sendCodeRemark" name="sendCodeRemark"><#if seller.sendCodeRemark?exists>${seller.sendCodeRemark}</#if></span><br/><br/></td>
					</tr>
					<tr>
						<td><label>周末发货说明：</label><br/><br/></td>
						<td><span id="sendWeekendRemark" name="sendWeekendRemark"><#if seller.sendWeekendRemark?exists>${seller.sendWeekendRemark}</#if></span><br/><br/></td>
					</tr>
					<!-- <tr>
						<td><label>预计发货后到货时间：</label><br/><br/></td>
						<td><span id="expectArriveTime" name="expectArriveTime"><#if seller.expectArriveTime?exists>${seller.expectArriveTime}</#if></span>天左右<br/><br/></td>
					</tr> -->
					<tr>
						<td><label>配送地区描述：</label><br/><br/></td>
						<td><span id="sellerDeliverAreaDesc" name="sellerDeliverAreaDesc"><#if seller.deliverAreaDesc?exists>${seller.deliverAreaDesc}</#if></span><br/><br/></td>
					</tr>
					<tr>
						<td><label>发送签收提醒短信商品类型：</label></td>
						<td>
							<!-- <input type="radio" name="type" id="type1" value="1" <#if product.type?exists && (product.type==1)>checked</#if> />普通商品
							<input type="radio" name="type" id="type2" value="2" <#if product.type?exists && (product.type==2)>checked</#if> />生鲜等需冷藏或冷冻的商品类商品 -->
						</td>
					</tr>
					<tr><td colspan="2"><hr/></td></tr>
					<tr>
						<td><label for="code">商品编码：</label><br/><br/></td>
						<td>
							<input maxlength="22" type="text" id="code" name="code" class="input-xlarge" value="<#if product.code?exists>${product.code}</#if>" <#if (canEdit?exists && (canEdit>0))>readonly="readonly"</#if>/><font color="red">必填</font>
							<#if (canEdit?exists && (canEdit == 0)) && (seller.sendCodeType?exists && (seller.sendCodeType !=2))><a id="autoCreateCode" href="javascript:;" class="easyui-linkbutton">生成编码</a></#if>
							<span id="autoCreateCodeTips" style="color: red"></span>&nbsp;注：不同的商家可能用不同的商品编码，可使用%x实现单件编码组合成多件销售
						<br/><br/></td>
					</tr>
					<tr>
						<td><label>商品结算：</label><br/><br/></td>
						<td>
							<input type="radio" name="submitType" id="submitType1" value="1" <#if product.submitType?exists && (product.submitType==1)>checked</#if>> 供货价 
							<input type="number" class="input-mini" name="wholesalePrice" id="wholesalePrice" maxlength="10" value="<#if wholesalePrice?exists>${wholesalePrice}</#if>"/>&nbsp;&nbsp;
							<input type="radio" name="submitType" id="submitType2" value="2" <#if product.submitType?exists && (product.submitType==2)>checked</#if>> 扣点
							<input type="number" class="input-mini" name="deduction" id="deduction" maxlength="10" value="<#if deduction?exists>${deduction}</#if>"/> %&nbsp;
							建议价：<input type="number" class="input-mini" name="proposalPrice" id="proposalPrice" maxlength="10" value="<#if proposalPrice?exists>${proposalPrice}</#if>" />
							<input type="radio" name="submitType" id="submitType3" value="3" <#if product.submitType?exists && (product.submitType==3)>checked</#if>> 自营采购价
							<input type="number" class="input-mini" name="selfPurchasePrice" id="selfPurchasePrice" maxlength="10" value="<#if selfPurchasePrice?exists>${selfPurchasePrice}</#if>"/>
							&nbsp;&nbsp;<a href="javascript:void(0);" onclick="showLogList();">供货价历史记录</a>
						<br/><br/></td>
					</tr>
					<#if product.id?exists && (product.id == 0)>
					<tr>
						<td><label for="totalStock">商品库存：</label><br/><br/></td>
						<td>
							<input type="number" class="input-xxlarge" name="totalStock" id="totalStock" maxlength="11" onblur="intValidator(this)" value="<#if totalStock?exists>${totalStock}</#if>"/>
							<font color="red" size="2px">必填</font>
							<br/><br/></td>
					</tr>
					</#if>
					<!-- <tr>
						<td><label >未分配总库存：</label><br/><br/></td>
						<td><input type="text" class="input-mini" name="availableStock" id="availableStock" readonly="readonly" value="<#if availableStock?exists>${availableStock}</#if>"/><br/><br/></td>
					</tr> -->
					<tr>
						<td><label>是否自动调库存：</label><br/><br/></td>
						<td>
							<input type="radio" name="isAutomaticAdjustStock" id="isAutomaticAdjustStock1" value="1" <#if product.isAutomaticAdjustStock?exists && (product.isAutomaticAdjustStock==1)>checked</#if>/>是&nbsp;&nbsp;&nbsp;
							<input type="radio" name="isAutomaticAdjustStock" id="isAutomaticAdjustStock0" value="0" <#if product.isAutomaticAdjustStock?exists && (product.isAutomaticAdjustStock==0)>checked</#if>/>否
						</td>
					</tr>
					<tr>
						<td><label>建议市场价：</label><br/><br/></td>
						<td>
							<input type="number" class="input-xxlarge" name="proposalMarketPrice" id="proposalMarketPrice" value="<#if proposalMarketPrice?exists>${proposalMarketPrice}</#if>"/><br/><br/>
						</td>
					</tr>
					<tr>
						<td><label>建议特卖价：</label><br/><br/></td>
						<td>
							<input type="number" class="input-xxlarge" name="proposalSalesPrice" id="proposalSalesPrice" value="<#if proposalSalesPrice?exists>${proposalSalesPrice}</#if>"/><br/><br/>
						</td>
					</tr>
                    <tr>
                        <td><label>心动慈露
供货价：</label><br/><br/></td>
                        <td>
                            <input type="number" class="input-xxlarge" name="hqbsWholesalePrice" id="hqbsWholesalePrice" value="<#if product.hqbsWholesalePrice?exists>${product.hqbsWholesalePrice}</#if>"/><br/><br/>
                        </td>
                    </tr>

					<tr><td colspan="2"><hr/></td></tr>

					<tr>
						<td><label for="netVolume">净含量：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="netVolume" id="netVolume" maxlength="15" value="<#if product.netVolume?exists>${product.netVolume}</#if>"/><br/><br/></td>
					</tr>
					<tr>
						<td><label for="placeOfOrigin">产地：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="placeOfOrigin" id="placeOfOrigin" maxlength="20" value="<#if product.placeOfOrigin?exists>${product.placeOfOrigin}</#if>"/><br/><br/></td>
					</tr>
					<tr>
						<td><label for="storageMethod">储藏方法：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="storageMethod" id="storageMethod" maxlength="50" value="<#if product.storageMethod?exists>${product.storageMethod}</#if>"/><br/><br/></td>
					</tr>
					<tr>
						<td><label for="manufacturerDate">生产日期：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="manufacturerDate" id="manufacturerDate" maxlength="20" value="<#if product.manufacturerDate?exists>${product.manufacturerDate}</#if>"/>
						<!-- <font color="red" size="2px">必填</font> -->
						<br/><br/></td>
					</tr>
					<tr>
						<td><label for="durabilityPeriod">保质期：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="durabilityPeriod" id="durabilityPeriod" maxlength="15" value="<#if product.durabilityPeriod?exists>${product.durabilityPeriod}</#if>"/>
						<!-- <font color="red" size="2px">必填</font> -->
						<br/><br/></td>
					</tr>
					<tr>
						<td><label for="expireDate">过期提醒时间：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="expireDate" id="expireDate" maxlength="15" value="<#if product.expireDate?exists>${product.expireDate}</#if>" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd'})"/>
						<!-- <font color="red" size="2px">必填</font> -->
						<br/><br/></td>
					</tr>
					<tr>
						<td><label for="peopleFor">适用人群：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="peopleFor" id="peopleFor" maxlength="50" value="<#if product.peopleFor?exists>${product.peopleFor}</#if>"/><br/><br/></td>
					</tr>
					<tr>
						<td><label for="foodMethod">食用方法：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="foodMethod" id="foodMethod" maxlength="50" value="<#if product.foodMethod?exists>${product.foodMethod}</#if>"/><br/><br/></td>
					</tr>
					<tr>
						<td><label for="useMethod">使用方法：</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="useMethod" id="useMethod" maxlength="50" value="<#if product.useMethod?exists>${product.useMethod}</#if>"/><br/><br/></td>
					</tr>
					<tr>
						<td><label for="sellingPoint">卖点</label><br/><br/></td>
						<td><input type="text" class="input-xxlarge" name="sellingPoint" id="sellingPoint" maxlength="50" value="<#if product.sellingPoint?exists>${product.sellingPoint}</#if>"/><br/><br/></td>
					</tr>
					<tr>
						<td><label for="tip">温馨提示：</label><br/><br/></td>
						<td>
							<textarea name="tip" id="tip" onkeydown="checkEnter(event)" style="height: 60px;width: 400px"><#if product.tip?exists>${product.tip}</#if></textarea>
							&nbsp;&nbsp;（长度&lt; 100；当前字数：<font id="tipCounter" color="red">0</font>）<br/><br/>
						</td>
					</tr>
					<!-- <tr>
						<td><label>商品正品保证展示：</label><br/><br/></td>
						<td>
							<input type="radio" name="qualityPromiseType" id="qualityPromiseType1" value="1" <#if product.qualityPromiseType?exists && (product.qualityPromiseType==1)>checked</#if> />进口商品
							<input type="radio" name="qualityPromiseType" id="qualityPromiseType2" value="2" <#if product.qualityPromiseType?exists && (product.qualityPromiseType==2)>checked</#if> />国产商品
						</td>
					</tr> -->
					<tr>
						<td><label for="deliverAreaDesc">配送地区描述：</label><br/><br/></td>
						<td>
							<input type="text" class="input-xxlarge" name="deliverAreaDesc" id="deliverAreaDesc" maxlength="50" value="<#if product.deliverAreaDesc?exists>${product.deliverAreaDesc}</#if>"/>
							<input type="hidden" name="deliverAreaType" id="deliverAreaType" value="<#if product.deliverAreaType?exists>${product.deliverAreaType?c}</#if>"/>
							<br/><br/>
						</td>
					</tr>
					<tr>
						<td><label>配送地区设置：</label><br/><br/></td>
						<td>
							<input type="text" class="input-xxlarge" name="sellerDeliverAreaTemplateId" id="sellerDeliverAreaTemplateId"/>
							<a href="javascript:;" onclick="addDeliverAreaTemplate()">已选商家还没有配送地区模版？点此设置</a>&nbsp;&nbsp;
							<a href="javascript:;" onclick="viewDeliverAreaTemplate()">查看已选模版</a><br/><br/>
						</td>
					</tr>
					<tr><td colspan="2"><hr/></td></tr>
					<tr>
						<td><label for="gegeSay">介绍描述：</label><br/><br/></td>
						<td>
							<textarea id="gegeSay" name="gegeSay" onkeydown="checkEnter(event)" style="height: 60px;width: 400px"><#if product.gegeSay?exists>${product.gegeSay}</#if></textarea>
							&nbsp;&nbsp;长度（&lt; 140）：<span style="color:red" id="counter">0 字</span><br/><br/></td>
					</tr>
					<tr>
						<td><label for="gegeImageId">logo：</label><br/><br/></td>
						<td>
							<select id="gegeImageId" name="gegeImageId" class="selectStyle">
		    					<#list gegeImageList as glist>
		    		 			  	<option value="${glist.id?c}"<#if product.gegeImageId?exists && (product.gegeImageId == glist.id)>selected</#if>>${glist.categoryName}</option>
		    		 			</#list>
    						</select> 
							<img src="http://www.cilu.com.cn/climg/LOGO/logo_01.jpg" alt="logo"  height="150" width="100">
						<br/><br/></td>
					</tr>
					<tr><td colspan="2"><hr/></td></tr>
					<tr>
						<td style="font-size: 20px;">商品图片</td>
						<td>
							上传商品详情主图，尺寸要求 640x640 &nbsp;&nbsp;&nbsp;
							<a onclick="uploadAllImage()" href="javascript:;" class="easyui-linkbutton">上传图片压缩包</a><br/><br/>
						</td>
					</tr>
					<tr>
						<td><label for="image1">商品详情主图1</label><br/><br/></td>
						<td>
							<input type="text" id="pic_1" name="image1" style="width: 450px;" class="input-xlarge" value="<#if product.image1?exists>${product.image1}</#if>" />
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
							<font color="red" size="2px" style="italtic">图片大小不得超过400KB</font>
						<br/><br/></td>
					</tr>
					<tr>
						<td><label for="image2">商品详情主图2</label><br/><br/></td>
						<td>
							<input type="text" id="pic_2" name="image2" style="width: 450px;" class="input-xlarge" value="<#if product.image2?exists>${product.image2}</#if>" />
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						<br/><br/></td>
					</tr>
					<tr>
						<td><label for="image3">商品详情主图3</label><br/><br/></td>
						<td>
							<input type="text" id="pic_3" name="image3" style="width: 450px;" class="input-xlarge" value="<#if product.image3?exists>${product.image3}</#if>" />
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a><br/><br/>
					</tr>
					<tr>
						<td><label for="image4">商品详情主图4</label><br/><br/></td>
						<td>
							<input type="text" id="pic_4" name="image4" style="width: 450px;" class="input-xlarge" value="<#if product.image4?exists>${product.image4}</#if>" />
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a><br/><br/>
					</tr>
					<tr>
						<td><label for="image5">商品详情主图5</label><br/><br/></td>
						<td>
							<input type="text" id="pic_5" name="image5" style="width: 450px;" class="input-xlarge" value="<#if product.image5?exists>${product.image5}</#if>" />
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
							<br/><br/>
						</td>
					</tr>
					<tr style="display:none">
						<td><label for="mediumImage">组合特卖图6</label><br/><br/></td>
						<td>
							<input type="text" id="pic_6" name="mediumImage" style="width: 450px;" class="input-xlarge" value="<#if product.mediumImage?exists>${product.mediumImage}</#if>" />
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
							<font color="red" size="2px" style="italtic">图片大小不得超过400KB，尺寸260x200</font>
						<br/><br/></td>
					</tr>
					<tr style="display: none">
						<td><label for="smallImage">购物车小图7</label><br/><br/></td>
						<td>
							<input type="text" id="pic_7" style="width: 450px;" name="smallImage" class="input-xlarge" value="<#if product.smallImage?exists>${product.smallImage}</#if>" />
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
							<font color="red" size="2px" style="italtic">图片大小不得超过400KB，尺寸100x100</font>
						<br/><br/></td>
					</tr>
					<tr><td colspan="2"><hr/></td></tr>
					<tr>
						<td style="font-size: 20px;">详情页图片</td>
						<td>
							图片大小不得超过400KB
						</td>
					</tr>
				</table>
				<table id="imageDetails">
					<#if (mobileDetails?size>0)>
					<#list mobileDetails as detail>
					<tr>
						<td><label>图片</label><br/><br/></td>
						<td>
							<input type="hidden" value="${detail.id?c}" name="detailId" />
							<input type="text" value="${detail.content}"  name="content" style="width: 450px;"/>
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
							<br/><br/>
						</td>
						<td>
							<span onclick="addImageDetailsRow(this)" style="cursor: pointer;color:red">&nbsp;添加&nbsp;|</span>
                    		<span onclick="removeImageDetailsRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
							<br/><br/>
						</td>
					</tr>
					</#list>
					<#else>
						<tr>
							<td><label>图片</label><br/><br/></td>
							<td>
								<input type="hidden" value="0" name="detailId" />
								<input type="text" value=""  name="content" style="width: 450px;"/>
								<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
								<br/><br/>
							</td>
							<td>
								<span onclick="addImageDetailsRow(this)" style="cursor: pointer;color:red">&nbsp;添加&nbsp;|</span>
	                    		<span onclick="removeImageDetailsRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
								<br/><br/>
							</td>
						</tr>
					</#if>
				</table>
				<table>
					<tr>
						<td><label for="isAvailable">商品使用状态</label></td>
						<td>
							<input type="radio" name="isAvailable" id="isAvailable1" value="1" <#if product.isAvailable?exists && (product.isAvailable==1)>checked</#if>> 可用
							<input type="radio" name="isAvailable" id="isAvailable0" value="0" <#if product.isAvailable?exists && (product.isAvailable==0)>checked</#if>> 停用
						</td>
					</tr>
					<tr>
						<td><input style="width: 150px;cursor:pointer" type="button" id="saveButton1" value="保存"/></td>
						<td><input style="width: 200px;cursor:pointer" type="button" id="saveButton2" value="保存并同步至特卖/商城商品"/></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>

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

	<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
	    <form id="picForm" method="post" enctype="multipart/form-data">
	        <input id="picFile" type="file" name="picFile" /><br/><br/>
	        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
	    </form>
	</div>
	
    <!-- 显示配送地区模版begin -->
    <div id="showTemplateDiv" class="easyui-dialog" style="width:600px;height:420px;padding:10px 10px;">
    		<p>
    			<span>商家名称：</span>
    			<span id="show_sellerName"></span>
    		</p>
    		<p>
    			<span>模版名称：</span>
    			<span id="show_templateName"></span>
    		</p>
    		<p>
    			<span>限制类型：</span>
    			<span id="show_templateType"></span>
    		</p>
    		<p>
    			<span>选择地区：</span>
    			<span id="show_area"></span>
    		</p>
    		<p>
    			<span>例外地区：</span>
    			<span id="show_exceptArea"></span>
    		</p>
    		<p>
    			<span>地区描述：</span>
    			<span id="show_desc"></span>
    		</p>
    </div>
	<!-- 显示配送地区模版end -->
	
	<!-- 基本商品供货价修改列表begin -->
	<div id="showHistoryListDiv" style="width:500px;height:600px;padding:20px 20px;">
		<table id="s_historyData"></table>
	</div>
	<!-- 基本商品供货价修改列表end -->
	
<script type="text/javascript">

	function intValidator(obj){
		var reg = /^[1-9]\d+$/;
		var value = $.trim($(obj).val());
		if(!reg.test(value)){
			$.messager.alert('提示',"请输入正整数",'info');
			return false;
		}
		$(obj).val(value);
		
	}

	function uploadAllImage(index){
        $('#uploadAllImage_div').dialog('open');
	}
	
    $(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
        });
        
        $("select[name='categoryFirstId']").each(function(){
        	$(this).change(function(){
            	var firstId = $(this).val();
            	var secondId = $(this).parent().next().find("select[name='categorySecondId']");
            	secondId.empty();
            	var option = $("<option>").text("-请选择二级分类-").val("0");
            	secondId.append(option);
            	$.ajax({
            		url: '${rc.contextPath}/category/jsonCategorySecondCode',
    		            type: 'post',
    		            dataType: 'json',
    		            data: {'firstId':firstId},
    		            success: function(data){
    		            	$.each(data,function(key,value){    
                      			var opt = $("<option>").text(value.text).val(value.code);    
                      			secondId.append(opt);  
                  			});
    		            }
            	});
            });
        });
        
        $("select[name='categorySecondId']").each(function(){
        	$(this).change(function(){
            	var secondId = $(this).val();
            	var thirdId = $(this).parent().next().find("select[name='categoryThirdId']");
            	thirdId.empty();
            	var option = $("<option>").text("-请选择三级分类-").val("0");
            	thirdId.append(option);
            	$.ajax({
            		url: '${rc.contextPath}/category/jsonCategoryThirdCode',
    		            type: 'post',
    		            dataType: 'json',
    		            data: {'secondId':secondId},
    		            success: function(data){
    		            	$.each(data,function(key,value){    
                      			var opt = $("<option>").text(value.text).val(value.code);    
                      			thirdId.append(opt);  
                  			});
    		            }
            	});
            });
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
        
        $("#code").focus(function(){
        	$("#autoCreateCodeTips").html('');
        });
        
        //自动生成商品编号
        $("#autoCreateCode").click(function(){
        	$("#autoCreateCodeTips").html();
        	$.ajax({
        		url: '${rc.contextPath}/productBase/autoCreateCode',
	            type: 'post',
	            dataType: 'json',
	            success: function(data){
	            	if(data.status == 1){
	            		$("#code").val(data.code);
	            	}else{
	            		$("#autoCreateCodeTips").html('自动生成失败，请手动输入或重试');
	            	}
	            },
	            error: function(xhr){
	            	$("#autoCreateCodeTips").html('自动生成失败，请手动输入或重试');
	            }
        	});
        });
        
        $("#saleFlagId").combobox({
			url:'${rc.contextPath}/flag/jsonSaleFlagCode?id=${product.saleFlagId?c!"0"}',   
		    valueField:'code',   
		    textField:'text'  
		});
        
        $("#sellerDeliverAreaTemplateId").combobox({
        	url:'${rc.contextPath}/sellerDeliverArea/jsonSellerDeliverAreaTemplateCode?sellerId=${product.sellerId?c!"0"}&templateId=${product.sellerDeliverAreaTemplateId?c!"0"}',
        	valueField:'code',
        	textField:'text'
        });
        
        $("select[name='provinceCode']").each(function(){
        	$(this).change(function(){
            	var provinceCode = $(this).val();
            	var cityCode = $(this).parent().next().find("select[name='cityCode']");
            	cityCode.empty();
            	var option = $("<option>").text("-全部市-").val("1");
            	cityCode.append(option);
            	$.ajax({
            		url: '${rc.contextPath}/area/jsonCityCode',
    		            type: 'post',
    		            dataType: 'json',
    		            data: {'provinceCode':provinceCode},
    		            success: function(data){
    		            	$.each(data,function(key,value){    
                      			var opt = $("<option>").text(value.text).val(value.code);    
                      			cityCode.append(opt);  
                  			});
    		            }
            	});
            });
        });
        
        $("select[name='cityCode']").each(function(){
        	$(this).change(function(){
            	var cityCode = $(this).val();
            	var districtCode = $(this).parent().next().find("select[name='districtCode']");
            	districtCode.empty();
            	var option = $("<option>").text("-全部区-").val("1");
            	districtCode.append(option);
            	$.ajax({
            		url: '${rc.contextPath}/area/jsonDistrictCode',
    		            type: 'post',
    		            dataType: 'json',
    		            data: {'cityCode':cityCode},
    		            success: function(data){
    		            	$.each(data,function(key,value){    
                      			var opt = $("<option>").text(value.text).val(value.code);    
                      			districtCode.append(opt);  
                  			});
    		            }
            	});
            });
        }); 
		
		$('#deliverAreaType1').change(function(){
			$("select[name='provinceCode']").each(function(){
				$(this).find("option").eq(0).attr('selected','selected');
			});
			$("select[name='cityCode']").each(function(){
				$(this).find("option").eq(0).attr('selected','selected');
			});
			$("select[name='districtCode']").each(function(){
				$(this).find("option").eq(0).attr('selected','selected');
			});
		});
		
		$('#deliverAreaType2').change(function(){
			$("select[name='provinceCode']").each(function(){
				$(this).find("option").eq(0).attr('selected','selected');
			});
			$("select[name='cityCode']").each(function(){
				$(this).find("option").eq(0).attr('selected','selected');
			});
			$("select[name='districtCode']").each(function(){
				$(this).find("option").eq(0).attr('selected','selected');
			});
		});
		
		$('#s_historyData').datagrid({
            nowrap: false,
            striped: true,
            collapsible:true,
            idField:'id',
            url:'${rc.contextPath}/productBase/jsonWholeSalePriceHistory',
            loadMsg:'正在装载数据...',
            fitColumns:true,
            remoteSort: true,
            pageSize:30,
            columns:[[
            	{field:'createTime',  title:'修改时间', width:80, align:'center'},
                {field:'username',    title:'修改人', width:30, align:'center'},
                {field:'oldPrice',    title:'修改前', width:30, align:'center'},
                {field:'newPrice',    title:'修改后', width:30, align:'center'},
            ]]
            //pagination:true
        });
		
		$('#showHistoryListDiv').dialog({
    		title:'供货价历史记录',
    		collapsible:true,
    		closed:true,
    		modal:true,
    		buttons:[{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#showHistoryListDiv').dialog('close');
                    $('#s_historyData').datagrid('reload');
                }
            }]
		});
    });

    function showLogList(){
    	var id = $.trim($("#editId").val());
    	$('#s_historyData').datagrid('reload',{proudctBaseId:id});
		$('#showHistoryListDiv').dialog('open');
    }
    var $obj;
    function picDialogOpen(obj) {
    	$obj = $(obj).prev();
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
                        if($obj != null && $obj!='' && $obj !=undefined) {
                        	$obj.val(res.url);
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
    
    function addImageDetailsRow(obj){
    	var row= $("#imageDetails").find("tr").first().clone();
    	$(row).find("input[name='detailId']").val('');
    	$(row).find("input[name='content']").val('');
    	$(obj).parent().parent().after(row);
    }
    
    //批量上传图片后回调
    function addImageDetailsRowAuto(content){
    	var row= $("#imageDetails").find("tr").first().clone();
    	$(row).find("input[name='detailId']").val('');
    	$(row).find("input[name='content']").val(content);
    	$("#imageDetails").find("tr").last().after(row);
    }
    
    function removeImageDetailsRow(obj){
    	$(obj).parent().parent().remove();
    }
    
    function addCategoryRow(obj){
    	var row = $(obj).parent().parent().clone(true);
    	var length = $(obj).parent().parent().parent().find("tr").size();
    	if(length > 5){
    		$.messager.alert("提示","最多允许添加5个分类","error");
    		return false;
    	}
    	$(row).find("td").eq(0).text('0');
    	$(row).find("td").eq(1).find("option").eq(0).attr("selected","selected");
    	$(row).find("td").eq(2).find("option").eq(0).attr("selected","selected");
    	$(row).find("td").eq(3).find("option").eq(0).attr("selected","selected");
    	$(obj).parent().parent().parent().find("tr").last().after(row);
    }
    
    function delCategoryRow(obj){
    	var length = $(obj).parent().parent().parent().find("tr").size();
    	if(length <= 1){
    		$.messager.alert("提示","必须有1个分类","error");
    		return false;
    	}
    	$(obj).parent().parent().remove();
    }
    
    function addAreasRow(obj){
    	$.messager.progress();
    	var row = $(obj).parent().parent().clone(true);
    	$(row).find("td").eq(0).text('0');
    	$(row).find("td").eq(1).find("option").eq(0).attr("selected","selected");
    	$(row).find("td").eq(2).find("option").eq(0).attr("selected","selected");
    	$(row).find("td").eq(3).find("option").eq(0).attr("selected","selected");
    	$(obj).parent().parent().parent().find("tr").last().after(row);
    	$.messager.progress('close');
    }
    
    function delAreasRow(obj){
    	$(obj).parent().parent().remove();
    }    
</script>

<script>

	$(document).keyup(function() { 
		var text=$("#gegeSay").val(); 
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
	
	//清空所有图片
	function clearImage(){
		for (var i=1;i<8;i++)
		{
			$("#pic_"+i).val("");
		}
		
		var row= $("#imageDetails").find("tr").first().clone();
    	$(row).find("input[name='detailId']").val('');
    	$(row).find("input[name='content']").val('');
    	
    	$("#imageDetails").find("tr").remove();
    	
    	$("#imageDetails").append(row);
		
		
	}
	
	function filterNullParameter(form){
		var params = "";
		$.each(form.split("&"),function(i, item){
			var arryStr = item.split("=");
			if($.trim(arryStr[1])!=''){
				params +=item +"&";
			}
		});
		var mIds = $("input[name='detailId']");
		var mContents = $("input[name='content']");
		var mIdAndContent = "";
		$.each(mIds,function(i,item){
			var id = $(mIds[i]).val();
			var content = $(mContents[i]).val();
			mIdAndContent +=(id+","+content+";");
		});
		mIdAndContent = mIdAndContent.substring(0,mIdAndContent.length-1);
		params += ("mIdAndContent="+mIdAndContent);
		
		var categoryIds = "";
		$("#categoryTab").find("tr").each(function(){
			 var id = $(this).find("td").eq(0).text();
			 var firstId = $(this).find("td").eq(1).find("select[name='categoryFirstId']").val();
			 var secondId = $(this).find("td").eq(2).find("select[name='categorySecondId']").val();
			 var thirdId = $(this).find("td").eq(3).find("select[name='categoryThirdId']").val();
			 categoryIds+=id+","+firstId+","+secondId+","+thirdId+";";
		});
		params = params+"&categoryIds="+categoryIds;
		
		/* var areaCodes = "";
		$("#areasTab").find("tr").each(function(){
			 var id = $(this).find("td").eq(0).text();
			 var provinceCode = $(this).find("td").eq(1).find("select[name='provinceCode']").val();
			 var cityCode = $(this).find("td").eq(2).find("select[name='cityCode']").val();
			 var districtCode = $(this).find("td").eq(3).find("select[name='districtCode']").val();
			 areaCodes+=id+","+provinceCode+","+cityCode+","+districtCode+";";
		});
		params = params+"&areaCodes="+areaCodes; */
		return params;
	}

	function sortNumber(a, b)
	{
		return a - b;
	}
	
	function submitForm(saveType){
		$("#saveType").val(saveType);
		var submitType= $("input[name='submitType']:checked").val();
		var deduction = $("#deduction").val();
		var proposalPrice = $("#proposalPrice").val();
		var selfPurchasePrice =$("#selfPurchasePrice").val();
    	var wholesalePrice = $("#wholesalePrice").val();
    	var editId = $("#editId").val();
    	var name = $("#productName").val();
    	
    	var sellerId = $("#sellerId").combobox('getValue');
    	var code = $("#code").val();
    	var totalStock = $("#totalStock").val();
    	var manufacturerDate = $("#manufacturerDate").val();
    	var durabilityPeriod = $("#durabilityPeriod").val();
    	// var saleFlagId = $("#saleFlagId").combobox('getValue');
    	// var brandId = $("#brandId").combobox('getValue');
    	var tip = $("#tip").val();
    	var gegeSay = $("#gegeSay").val();
    	var image1 = $("#pic_1").val();
    	var mediumImage = $("#pic_6").val();
    	var smallImage = $("#pic_7").val();
		var counter=gegeSay.length; 
		
		var categoryFirstId = true;
		var categorySecondId = true;
		$("select[name='categoryFirstId']").each(function(){
			if($(this).val()==0){
				categoryFirstId = false;
				return false;
			}
		});
		$("select[name='categorySecondId']").each(function(){
			if($(this).val()==0){
				categorySecondId = false;
				return false;
			}
		});

		if(submitType == undefined ||(submitType == 1 && wholesalePrice == '') || (submitType == 2 && (deduction == '' || proposalPrice == '')) || (submitType == 3 && selfPurchasePrice == '')){
			$.messager.alert("提示","请填写商品结算信息","info");
		}else if(Number(deduction) >= 100){
            $.messager.alert("提示","扣点需要小于100%","info");
        }
		else if($.trim(name) == ""){
			$.messager.alert("提示","请输入商品名称","info");
		}/*else if(!categoryFirstId || !categorySecondId){
			$.messager.alert("提示","商品一级分类和二级分类为必填项","info");
		}*/else if(sellerId == -1 || sellerId=='' || sellerId==null || sellerId==undefined){
    		$.messager.alert("提示","请选择商家信息","info");
    	}else if($.trim(code) == ""){
    		$.messager.alert("提示","请输入商品编码","info");
		}else if(totalStock == ''){
			$.messager.alert("提示","请输入库存","info");
		}/*else if(saleFlagId == '' || saleFlagId == null || saleFlagId == undefined){
			$.messager.alert("提示","请选择国家信息","info");
		}*//*else if(brandId == -1 || brandId=='' || brandId == null || brandId == undefined){
    		$.messager.alert("提示","请选择品牌信息","info");
    	}*/
		/* else if($.trim(manufacturerDate)==''){
    		$.messager.alert("提示","请输入生产日期","info");
    	}else if($.trim(durabilityPeriod)==''){
    		$.messager.alert("提示","请输入保质期","info");
    	} */
    	else if(tip.length > 99){
    		$.messager.alert("提示","温馨提示字数不得超过100","info");
    	}else if(counter > 140){
    		$.messager.alert("提示","格格说字数不得超过140","info");
    	}/* else if(image1 == ""){
    		$.messager.alert("提示","请上传商品详情主图1","info");
    	}else if(mediumImage==''){
    		$.messager.alert("提示","请上传组合特卖图","info");
    	}else if(smallImage==''){
    		$.messager.alert("提示","请上传购物车小图","info");
    	} */else{
    		$.messager.progress();
    		$.post(
    			"${rc.contextPath}/productBase/checkCodeAndBarCode",
    			{sellerId:sellerId,code:code,productBaseId:editId},
    			function(data){
    				if(data.status==1){
    					$.ajax({
    		    			url: '${rc.contextPath}/productBase/saveOrUpdate',
    		    			data:filterNullParameter($("#productBaseForm").serialize()),
    		    			type:'post',
    		    			dataType:'JSON',
    		    			success: function(data) {
    		    				$.messager.progress('close');
    		    				if (data.status == 1) {
    		    					window.location.href = "${rc.contextPath}/productBase/list";
    		    				} else {
    		    					$.messager.alert("提示", data.msg, "error");
    		    				}
    		    			},
    		    			error:function(data, textStatus){
    		    				$.messager.progress('close');
    		    			}
    		    		});
    				}else{
    					$.messager.alert("提示","商品编码重复，请重新生成","info");
    					$.messager.progress('close');
    				}
    			},
    			'json'
    		);
    		
    	} 
	}
	
	$(function(){
		
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
					    	var imgs = [];
					    	$.messager.progress('close');
					    	var data = eval('(' + data + ')');  
							if(data.status == 1){
								$.messager.alert("提示",data.msg,"info",function(){
									$('#uploadAllImage_div').dialog('close');
								});	
								for(var p in data.fileUrlMap){
									if(p.indexOf("detail") > -1){
										if(p == 'detail_pic_1'){
											$("#imageDetails").find("tr").first().find("input[name='content']").val(data.fileUrlMap[p]);
										}else{
											//addImageDetailsRowAuto(data.fileUrlMap[p])
											imgs[imgs.length] = parseInt(p.split("_pic_")[1]);
										}
									}else{
										$("#"+p).val(data.fileUrlMap[p]);
									}
								}
								console.log(imgs);
								//对imgs进行排序
								imgs.sort(sortNumber)
								console.log(imgs);
								for(var i=0;i<imgs.length;i++) { 
									addImageDetailsRowAuto(data.fileUrlMap["detail_pic_"+imgs[i]]);
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
		
	    $("#saveButton1").click(function(){
	    	submitForm(1);
    	});
	    
	    $("#saveButton2").click(function(){
	    	var image1 = $.trim($("#pic_1").val());
	    	var image2 = $.trim($("#pic_2").val());
	    	var image3 = $.trim($("#pic_3").val());
	    	var image4 = $.trim($("#pic_4").val());
	    	var image5 = $.trim($("#pic_5").val());
	    	var contents = $("input[name='content']");
	    	var imageDetails = '';
	    	$.each(contents,function(i,item){
				imageDetails +=$.trim($(contents[i]).val());
			});
	    	var isEmpty = true;
	    	if((image1 == '' && image2 == '' && image3 == '' && image4 == '' && image5 == '') || imageDetails == '' ){
	    		$.messager.alert('提示','图片不能全部为空',"error");
	    	}else{
		    	$.messager.confirm('提示','确定把信息同步至特卖/商城商品吗？',function(b){
		    		if(b){
				    	submitForm(2);
		    		}
		    	});
	    	}
    	});
	    
		$("#freightTemplate").change(function(){
			$("#freight_muban").prop("checked", true);
			$("#freight_baoyou").prop("checked", false);
		});
				
		$('#brandId').combobox({
			url: '${rc.contextPath}/brand/jsonBrandCode?isAvailable=1&brandId='+${product.brandId?c},
			valueField: 'code',
			textField: 'text',
			onSelect: function(brand) {
				if (brand != '' && brand != null || brand != undefined) {
					$.post("${rc.contextPath}/product/ajaxBrandInfo", {
							id: brand.code
						},
						function(data) {
							if (data.status != 1) {
								$.messager.alert("提示", data.msg, "info");
								$("#brandId").val("-1");
							}
						}, "json");
				}
			}
		});
		
		$('#sellerId').combobox({
			url: '${rc.contextPath}/seller/jsonSellerCode?isAvailable=1&id='+${seller.id?c},
			valueField: 'code',
			textField: 'text',
			onSelect: function(seller) {
				if (seller == '' || seller == null || seller == undefined) {
					$("#sendAddress").html("");
					$("#warehouse").html("");
					$("#sendTimeRemark").html("");
					$("#sendCodeRemark").html("");
					$("#sendType_1").prop("checked", false);
					$("#sendType_2").prop("checked", false);
					$("#sendType_3").prop("checked", false);
					$("#freightType1").prop("checked", false);
					$("#freightType2").prop("checked", false);
					$("#freightMoney").html('');
					$("#freightType3").prop("checked", false);
					$("#freightType4").prop("checked", false);
					$("#freightOther").html('');
					$("#kuaidi").html('');
					$("#sendWeekendRemark").html('');
					$("#expectArriveTime").html('');
					$("#sellerDeliverAreaDesc").html('');
				} else {
					$.post("${rc.contextPath}/product/ajaxSellerInfo", {
							id: seller.code
						},
						function(data) {
							if (data.status == 1) {
								$("#sendAddress").html(data.sendAddress);
								$("#warehouse").html(data.warehouse);
								$("#sendTimeRemark").html(data.sendTimeRemark);
								$("#sendCodeRemark").html(data.sendCodeRemark);
								$("#kuaidi").html(data.kuaidi);
								$("#sendWeekendRemark").html(data.sendWeekendRemark);
								$("#expectArriveTime").html(data.expectArriveTime);
								$("#sellerDeliverAreaDesc").html(data.deliverAreaDesc);
								$("#deliverAreaDesc").val(data.deliverAreaDesc);
								if (data.sellerType == 1) {
									$("#sendType_1").prop("checked", true);
									$("#sendType_2").prop("checked", false);
									$("#sendType_3").prop("checked", false);
									$("#sendType_4").prop("checked", false);
									$("#deliverAreaType1").prop("checked",true);
									$("#deliverAreaType2").prop("checked",false);
								} else if (data.sellerType == 2) {
									$("#sendType_1").prop("checked", false);
									$("#sendType_2").prop("checked", true);
									$("#sendType_3").prop("checked", false);
									$("#sendType_4").prop("checked", false);
									$("#deliverAreaType1").prop("checked",false);
									$("#deliverAreaType2").prop("checked",true);
								} else if (data.sellerType == 3) {
									$("#sendType_1").prop("checked", false);
									$("#sendType_2").prop("checked", false);
									$("#sendType_3").prop("checked", true);
									$("#sendType_4").prop("checked", false);
									$("#deliverAreaType1").prop("checked",false);
									$("#deliverAreaType2").prop("checked",true);
								} else if(data.sellerType >3){
									$("#sellerTypeOther").find("option[value='"+data.sellerType+"']").attr("selected",true);
									$("#sendType_1").prop("checked", false);
									$("#sendType_2").prop("checked", false);
									$("#sendType_3").prop("checked", false);
									$("#sendType_4").prop("checked", true);
									$("#deliverAreaType1").prop("checked",false);
									$("#deliverAreaType2").prop("checked",true);
								}else {
									$("#sendAddress").html('');
									$("#warehouse").html('');
									$("#sendTimeRemark").html('');
									$("#kuaidi").html('');
									$("#sendWeekendRemark").html('');
									$("#expectArriveTime").html('');
									$("#sellerDeliverAreaDesc").html('');
									$("#sendType_1").prop("checked", false);
									$("#sendType_2").prop("checked", false);
									$("#sendType_3").prop("checked", false);
									$("#sendType_4").prop("checked", false);
								}
								if(data.freightType==1){
									$("#freightType1").prop("checked", true);
									$("#freightType2").prop("checked", false);
									$("#freightMoney").html('');
									$("#freightType3").prop("checked", false);
									$("#freightType4").prop("checked", false);
									$("#freightOther").html('');
								}else if(data.freightType==2){
									$("#freightType1").prop("checked", false);
									$("#freightType2").prop("checked", true);
									$("#freightMoney").html(data.freightMoney);
									$("#freightType3").prop("checked", false);
									$("#freightType4").prop("checked", false);
									$("#freightOther").html('');
								}else if(data.freightType==3){
									$("#freightType1").prop("checked", false);
									$("#freightType2").prop("checked", false);
									$("#freightMoney").html('');
									$("#freightType3").prop("checked", true);
									$("#freightType4").prop("checked", false);
									$("#freightOther").html('');
								}else if(data.freightType==4){
									$("#freightType1").prop("checked", false);
									$("#freightType2").prop("checked", false);
									$("#freightMoney").html('');
									$("#freightType3").prop("checked", false);
									$("#freightType4").prop("checked", true);
									$("#freightOther").html(data.freightOther);
								}else{
									$("#freightType1").prop("checked", false);
									$("#freightType2").prop("checked", false);
									$("#freightMoney").html('');
									$("#freightType3").prop("checked", false);
									$("#freightType4").prop("checked", false);
									$("#freightOther").html('');
								}
								if(data.sendCodeType == 2){
									$("#code").val($("#barcode").val());
									$("#autoCreateCode").hide();
								}else{
									$("#code").val('');
									$("#code").removeAttr("readonly");
									$("#autoCreateCode").show();
								}
							} else if (data.status == 2) {
								$.messager.alert("提示", "所选商家已经停用，请重新选择", "info");
								$("#sendAddress").html('');
								$("#warehouse").html('');
								$("#sendTimeRemark").html('');
								$("#sendCodeRemark").html('');
								$("#freightMoney").html('');
								$("#freightOther").html('');
								$("#kuaidi").html('');
								$("#sendWeekendRemark").html('');
								$("#expectArriveTime").html('');
								$("#sellerDeliverAreaDesc").html('');
								$("#deliverAreaDesc").val('');
								$("#sendType_1").prop("checked", false);
								$("#sendType_2").prop("checked", false);
								$("#sendType_3").prop("checked", false);
								$("#freightType1").prop("checked", false);
								$("#freightType2").prop("checked", false);
								$("#freightType3").prop("checked", false);
								$("#freightType4").prop("checked", false);
								$("#code").val('');
								$("#code").removeAttr("readonly");
								$("#autoCreateCode").show();
							}
						}, "json");
					
					$("#sellerDeliverAreaTemplateId").combobox('clear');
					var url = '${rc.contextPath}/sellerDeliverArea/jsonSellerDeliverAreaTemplateCode?sellerId='+seller.code;
					$("#sellerDeliverAreaTemplateId").combobox('reload',url);
				}
			}
		});


		$('#code').change(function (){
			var code = $('#code').val();
            var index = code.lastIndexOf("%");
            if(index != -1){
            	var num = code.substring(index+1);
                var reg = new RegExp("^[0-9]*$");
    			if(index == (code.length-1)){
                    $.messager.alert("确认信息","您输入了一个以%结尾的商品编码哦。"+num,"info");
    			}else{
                    if(reg.test (num)){
                        $.messager.alert("确认信息","该商品发货时数量会默认乘以"+num+"哦，请确认。","warn");
                    }
    			}
            }
		});
		
		var submitType = $("input[name='submitType']:checked").val();
		var wholesalePrice = $("#wholesalePrice").val();
		var deduction = $("#deduction").val();
		var proposalPrice = $("#proposalPrice").val();
		var selfPurchasePrice =$("#selfPurchasePrice").val();
		$("#submitType1").change(function(){
			if($(this).is(":checked")){
				$("#deduction").val('');
				$("#proposalPrice").val('');
				$("#selfPurchasePrice").val('');
				if(submitType == 1){
					$("#wholesalePrice").val(wholesalePrice);
				}
			}
		});
		
		$("#submitType2").change(function(){
			if($(this).is(":checked")){
				$("#wholesalePrice").val('');
				$("#selfPurchasePrice").val('');
				if(submitType == 2){
					$("#deduction").val(deduction);
					$("#proposalPrice").val(proposalPrice);
				}
			}
		});
		
		$("#submitType3").change(function(){
			if($(this).is(":checked")){
				$("#wholesalePrice").val('');
				$("#deduction").val('');
				$("#proposalPrice").val('');
				if(submitType == 3){
					$("#selfPurchasePrice").val(selfPurchasePrice);
				}
			}
		});
		
		$("#showTemplateDiv").dialog({
            title:'配送地区查看',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'确认',
                iconCls:'icon-ok',
                handler:function(){
                	 $('#showTemplateDiv').dialog('close');
                }
            }]
        });
	});
	
	function viewSeller(){
		var sellerId = $("#sellerId").combobox('getValue');
		if(sellerId == '' || sellerId == null || sellerId == undefined){
			$.messager.alert("提示","请选择商家","warn");
			return false;
		}
		var url = '${rc.contextPath}/seller/edit/'+sellerId;
		window.open(url);
	}
	
	function copyDeliverAreaFromSeller(){
		var sellerId = $("#sellerId").combobox('getValue');
		if(sellerId == '' || sellerId == null || sellerId == undefined){
			$.messager.alert("提示","请选择商家","warn");
			return false;
		}else{
			$.messager.progress();
			$.post("${rc.contextPath}/product/ajaxSellerInfo", 
			{id: sellerId},
			function(data) {
				if (data.status == 1) {
					if(data.deliverAreaType <= 1){
						$("#deliverAreaType1").prop("checked",true);
						$("#deliverAreaType2").prop("checked",false);
					}else if(data.deliverAreaType == 2){
						$("#deliverAreaType1").prop("checked",false);
						$("#deliverAreaType2").prop("checked",true);
					}
					$("#deliverAreaDesc").val(data.deliverAreaDesc);
					var length = $("#areasTab").find("tr").length;
					if(data.areaCodes.length > 0){
						$.each(data.areaCodes,function(index,area){
							var rr = $("#areasTab").find("tr").first().clone(true);;
							$(rr).find("td").eq(0).text('0');
					    	$(rr).find("td").eq(1).find("select[name='provinceCode']").val(area.provinceCode);
					    	
				    		var cityCode = $(rr).find("td").eq(2).find("select[name='cityCode']");
				    		cityCode.empty();
			            	var option = $("<option>").text("-全部市-").val("1");
			            	cityCode.append(option);
			            	$.ajax({
			            		url: '${rc.contextPath}/area/jsonCityCode',
			    		            type: 'post',
			    		            dataType: 'json',
			    		            data: {'provinceCode':area.provinceCode},
			    		            success: function(data1){
			    		            	$.each(data1,function(key,value){    
			                      			var opt = $("<option>").text(value.text).val(value.code); 
			                      			if($(opt).val()==area.cityCode){
			                      				$(opt).attr("selected",true);
			                      			}
			                      			cityCode.append(opt);  
			                  			});
			    		            }
			            	});
			            	
				    		var districtCode = $(rr).find("td").eq(3).find("select[name='districtCode']");
				    		districtCode.empty();
			            	var option = $("<option>").text("-全部区-").val("1");
			            	districtCode.append(option);
			            	$.ajax({
			            		url: '${rc.contextPath}/area/jsonDistrictCode',
			    		            type: 'post',
			    		            dataType: 'json',
			    		            data: {'cityCode':area.cityCode},
			    		            success: function(data1){
			    		            	$.each(data1,function(key,value){    
			                      			var opt = $("<option>").text(value.text).val(value.code);
			                      			if($(opt).val() == area.districtCode){
			                      				$(opt).attr("selected",true);
			                      			}
			                      			districtCode.append(opt);  
			                  			});
			    		            }
			            	});
					    	
					    	$("#areasTab").append(rr);
						});
						$("#areasTab").find("tr:lt("+length+")").remove();
					} 
					$.messager.progress('close');
				} else if (data.status == 2) {
					$.messager.alert("提示", "所选商家已经停用，请重新选择", "info");
					$("#deliverAreaType1").prop("checked",true);
					$("#deliverAreaType2").prop("checked",false);
					$("#deliverAreaDesc").val('');
				}
			}, 
			"json");
		}
	}
	
	//设置所选商家配送地区模版
	function addDeliverAreaTemplate(){
		var sellerId = $("#sellerId").combobox('getValue');
		if(sellerId == null || sellerId == '' || sellerId == undefined){
			$.messager.alert('提示','请选择商家名称','error');
			return false;
		}
		var url = '${rc.contextPath}/sellerDeliverArea/sellerDeliverAreaTemplateList?sellerId='+sellerId;
		window.open(url,'_blank');
	}
	
	function clearShowTemplateDiv(){
		$("#show_sellerName").text('');
		$("#show_templateName").text('');
		$("#show_templateType").text('');
		$("#show_area").text('');
		$("#show_exceptArea").text('');
		$("#show_desc").text('');
	}
	//查看所选商家配送地区模版
	function viewDeliverAreaTemplate(){
		clearShowTemplateDiv();
		var templateId = $("#sellerDeliverAreaTemplateId").combobox('getValue');
		if(templateId == null || templateId == '' || templateId == undefined){
			$.messager.alert('提示','请选配送地区模版','error');
			return false;
		}else{
			$.ajax({
				url:'${rc.contextPath}/sellerDeliverArea/getSellerDeliverAreaTemplate/'+templateId,
				type:'post',
				data:{'id':templateId},
				success:function(data){
					var areas = [];
					var exceptAreas = [];
					var areaCodes = data.areaCodes;
					var exceptAreaCodes = data.exceptAreaCodes;
					
					$.each(areaCodes,function(index,area){
						areas.push(area.provinceName);
					});
					
					$.each(exceptAreaCodes,function(index,area){
						var address = '';
						if(area.provinceCode != 1){
							address += area.provinceName;
						}
						if(area.cityCode !=1){
							address += area.cityName;
						}
						if(area.districtCode !=1){
							address += area.districtName;
						}
						exceptAreas.push(address);
					});
					
					// 设置showTemplateDiv
					$("#show_sellerName").text(data.sellerName);
					$("#show_templateName").text(data.name);
					$("#show_templateType").text(data.type == 1?"仅以下地区发货":"以下地区不发货");
					$("#show_area").text(areas.join(','));
					$("#show_exceptArea").text(exceptAreas.join(','));
					$("#show_desc").text(data.desc);
					$('#showTemplateDiv').dialog('open');
				},
				error:function(xhr){
					$.messager.progress('close');
		            $.messager.alert("提示",'服务器忙，请稍后再试，errorCode='+xhr.status,"info");
				}
			});
		}
	}
	
</script>

</body>
</html>