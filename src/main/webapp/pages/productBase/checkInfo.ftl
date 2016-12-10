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
	<div data-options="region:'center',title:'商品信息'" style="padding:5px;">
		<form id="checkProductForm" style="font-size: 14px">
			<fieldset>
				<legend>基本信息</legend>
				<input type="hidden" value="<#if productBaseInfo?? && productBaseInfo.id??>${productBaseInfo.id?string('#######')}</#if>" id="productBaseId"/>
				<input type="hidden" value="<#if productInfo.seller_product_id??>${productInfo.seller_product_id?string('#######')}</#if>" id="sellerProductId" name="sellerProductId"/>
				<input type="hidden" value="<#if productInfo.category_first_id??>${productInfo.category_first_id?string('#######')}</#if>" id="categoryFirstId" name="categoryFirstId"/>
				<input type="hidden" value="<#if productInfo.category_second_id??>${productInfo.category_second_id?string('#######')}</#if>" id="categorySecondId" name="categorySecondId"/>
				<input type="hidden" value="<#if productInfo.category_third_id??>${productInfo.category_third_id?string('#######')}</#if>" id="categoryThirdId" name="categoryThirdId"/>
				<input type="hidden" value="<#if productInfo.seller_name??>${productInfo.seller_name}</#if>" id="sellerName" name="sellerName"/>
				<input type="hidden" value="<#if productInfo.seller_id??>${productInfo.seller_id?string('#######')}</#if>" id="sellerId" name="sellerId"/>
				<input type="hidden" value="<#if productInfo.checker??>${productInfo.checker}</#if>" id="checker" name="checker"/>
				<input type="hidden" value="<#if productInfo.id??>${productInfo.id?string('#######')}</#if>" id="id"/>
				<input type="hidden" value="<#if productInfo.submitTime??>${productInfo.submitTime}</#if>" id="submitTime" name="submitTime"/>
				<input type="hidden" value="<#if productInfo.seller_deliver_area_template_id??>${productInfo.seller_deliver_area_template_id?string('#######')}</#if>" id="sellerDeliverAreaTemplateId" name="sellerDeliverAreaTemplateId"/>
				<input type="hidden" name="type"/>
				<input type="hidden" name="checkContent"/>
				<input type="hidden" name="detailImage"/>
				
				<table>
					<tr>
						<td><label>商品类目：</label>
												<#if (type?? && type==1)>
												<label><#if (categoryInfos?? && categoryInfos?size > 0)>
															<#list categoryInfos as category>
																<label><#if category.categoryFirstName??>${category.categoryFirstName}</#if></label>
																<label>><#if category.categorySecondName??>${category.categorySecondName}</#if></label>
																<label>><#if category.categoryThirdName??>${category.categoryThirdName}</#if></label>
															</#list>
														</#if>
												</label>
												<label style="color: red">
													<#if (productBaseCategoryInfos?? && productBaseCategoryInfos?size > 0)>
															(
															<#list productBaseCategoryInfos as baseCategory>
																<label><#if baseCategory.categoryFirstName??>${baseCategory.categoryFirstName}</#if></label>
																<label>><#if baseCategory.categorySecondName??>${baseCategory.categorySecondName}</#if></label>
																<label>><#if baseCategory.categoryThirdName??>${baseCategory.categoryThirdName}</#if></label>
															</#list>
															)
														</#if>
														
												</label>
												<#else>
													<label><#if productInfo.firstName??>${productInfo.firstName}</#if></label>
													<label>><#if productInfo.secondName??>${productInfo.secondName}</#if></label>
													<label>><#if productInfo.thirdName??>${productInfo.thirdName}</#if></label>
												</#if>
						</td>
					</tr>
					<tr>
						<td><label>商品名称：</label><label id="name"><#if productInfo.name??>${productInfo.name}</#if></label>
							<input type="hidden" name="name" value="<#if productInfo.name??>${productInfo.name}</#if>">
						</td>
					</tr>
					<tr>
						<td><label>商品卖点：</label><label id="sellingPoint"><#if productInfo.selling_point??>${productInfo.selling_point}</#if></label>
							<input name="sellingPoint" type="hidden" value="<#if productInfo.selling_point??>${productInfo.selling_point}</#if>">
						</td>
					</tr>
					<tr>
						<td><label>国家信息：</label><label id="flagName"><#if productInfo.flag_name??>${productInfo.flag_name}</#if></label>
							<input type="hidden" name="flagName" value="<#if productInfo.flag_name??>${productInfo.flag_name}</#if>">
						</td>
					</tr>
					<tr>
						<td><label>品牌：</label><label id="brandName"><#if productInfo.brand_name??>${productInfo.brand_name}</#if></label>
							<input type="hidden" name="brandName" value="<#if productInfo.brand_name??>${productInfo.brand_name}</#if>">
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset>
				<legend>详细信息</legend>
				<table>
					<tr>
						<td>
							<label>规格：</label><label id="netVolume"><#if productInfo.net_volume??>${productInfo.net_volume}</#if></label>
												<input type="hidden" name="netVolume" value="<#if productInfo.net_volume??>${productInfo.net_volume}</#if>">
												<label style="color: red;"><#if productBaseInfo?? && productBaseInfo.netVolume??>(${productBaseInfo.netVolume})</#if></label>
						</td>
					</tr>
					<tr>
						<td><label>产地：</label><label id="placeOfOrigin"><#if productInfo.place_of_origin??>${productInfo.place_of_origin}</#if></label>
							<input type="hidden" name="placeOfOrigin" value="<#if productInfo.place_of_origin??>${productInfo.place_of_origin}</#if>">
						</td>
					</tr>
					<tr>
						<td><label>储存方法：</label><label id="storageMethod"><#if productInfo.storage_method??>${productInfo.storage_method}</#if></label>
							<input type="hidden" name="storageMethod" value="<#if productInfo.storage_method??>${productInfo.storage_method}</#if>">
						</td>
					</tr>
					<tr>
						<td>
							<label>生产日期：</label><label id="manufacturerDate"><#if productInfo.manufacturer_date??>${productInfo.manufacturer_date}</#if></label>
													<input type="hidden" name="manufacturerDate" value="<#if productInfo.manufacturer_date??>${productInfo.manufacturer_date}</#if>">
													<label style="color: red;"><#if productBaseInfo?? && productBaseInfo.manufacturerDate??>(${productBaseInfo.manufacturerDate})</#if></label>
						</td>
					</tr>
					<tr>
						<td>
							<label>保质期：</label><label id="durabilityPeriod"><#if productInfo.durability_period??>${productInfo.durability_period}</#if></label>
													<input type="hidden" name="durabilityPeriod" value="<#if productInfo.durability_period??>${productInfo.durability_period}</#if>">
													<label style="color: red;"><#if productBaseInfo?? && productBaseInfo.durabilityPeriod??>(${productBaseInfo.durabilityPeriod})</#if></label>
						</td>
					</tr>
					<tr>
						<td><label>适用人群：</label><label id="peopleFor"><#if productInfo.people_for??>${productInfo.people_for}</#if></label>
							<input type="hidden" name="peopleFor" value="<#if productInfo.people_for??>${productInfo.people_for}</#if>">
						</td>
					</tr>
					<tr>
						<td><label>食用方法：</label><label id="foodMethod"><#if productInfo.food_method??>${productInfo.food_method}</#if></label>
							<input type="hidden" name="foodMethod" value="<#if productInfo.food_method??>${productInfo.food_method}</#if>">
						</td>
					</tr>
					<tr>
						<td><label>使用方法：</label><label id="useMethod"><#if productInfo.use_method??>${productInfo.use_method}</#if></label>
							<input type="hidden" name="useMethod" value="<#if productInfo.use_method??>${productInfo.use_method}</#if>">
						</td>
					</tr>
					<tr>
						<td><label>温馨提示：</label><label id="tip"><#if productInfo.tip??>${productInfo.tip}</#if></label>
							<input type="hidden" name="tip" value="<#if productInfo.tip??>${productInfo.tip}</#if>">
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset>
				<legend>SKU信息</legend>
				<table>
					<tr>
						<td>
							<label>商品条码：</label><label id="barcode"><#if productInfo.barcode??>${productInfo.barcode}</#if></label>
													<input type="hidden" name="barcode" value="<#if productInfo.barcode??>${productInfo.barcode}</#if>">
													<label style="color: red;"><#if productBaseInfo?? && productBaseInfo.barcode??>(${productBaseInfo.barcode})</#if></label>
						</td>
					</tr>
					<tr>
						<td><label>商品结算：</label><label id="submit">
											<#if (productInfo.submit??)>${productInfo.submit}
											<#elseif (productInfo.submit_type?? && productInfo.submit_type == 1)>${productInfo.wholesale_price}元
											<#elseif (productInfo.submit_type?? && productInfo.submit_type == 2)>${productInfo.deduction}%
											</#if></label>
											<label style="color: red;">
											<#if (productBaseInfo?? && productBaseInfo.submitType?? && productBaseInfo.submitType == 1)>(${productBaseInfo.wholesalePrice}元)
											<#elseif (productBaseInfo?? && productBaseInfo.submitType?? && productBaseInfo.submitType == 2)>(${productBaseInfo.deduction}%)
											</#if>
											</label>
											<input type="hidden" name="submit">
						</td>
					</tr>
					<tr>
						<td><label>参考价格：</label><label>&nbsp;&nbsp;市场价：</label><label id="proposalMarketPrice"><#if (productInfo.proposal_market_price??)>${productInfo.proposal_market_price}</#if></label><label>元</label>
												<input type="hidden" name="proposalMarketPrice" value="<#if (productInfo.proposal_market_price??)>${productInfo.proposal_market_price}</#if>">
												<label>&nbsp;&nbsp;&nbsp;&nbsp;建议特卖价：</label><label id="proposalPrice"><#if (productInfo.proposal_price??)>${productInfo.proposal_price}</#if></label><label>元</label>
												<input type="hidden" name="proposalPrice" value="<#if (productInfo.proposal_price??)>${productInfo.proposal_price}</#if>">
												</td>
					</tr>
				</table>
			</fieldset>
			<fieldset>
				<legend>发货信息</legend>
				<table>
					<tr>
						<td><label>商品发货编码：</label><label id="code"><#if productInfo.code??>${productInfo.code}</#if></label>
												<input type="hidden" name="code" value="<#if productInfo.code??>${productInfo.code}</#if>">
														<label style="color: red;"><#if productBaseInfo?? && productBaseInfo.code??>(${productBaseInfo.code})</#if></label>
						</td>
					</tr>
					<tr>
						<td><label>发货类型：</label><label id="despatchType"><#if productInfo.id?? && productInfo.despatch_type??>${productInfo.despatch_type}
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 1>国内
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 2>保税区
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 3>香港
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 4>美国
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 5>日本
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 6>澳大利亚
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 7>德国
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 8>荷兰
																			<#elseif productInfo.despatch_type?? && productInfo.despatch_type == 9>台湾
																			</#if>
																			</label>
																			<input type="hidden" name="despatchType">
																			</td>
					</tr>
					<tr>
						<td><label>发货地：</label><label id="despatchAddress"><#if productInfo.despatch_address??>${productInfo.despatch_address}</#if></label>
												<input type="hidden" name="despatchAddress" value="<#if productInfo.despatch_address??>${productInfo.despatch_address}</#if>">
						</td>
					</tr>
					<tr>
						<td><label class="dddddd" >发货快递：</label><label id="kuaidi"><#if productInfo.kuaidi??>${productInfo.kuaidi}</#if></label>
							<input type="hidden" name="kuaidi" value="<#if productInfo.kuaidi??>${productInfo.kuaidi}</#if>">
						</td>
					</tr>
					<tr>
						<td><label>运费结算方式：</label><label id="freightType">
															<#if productInfo.id?? && productInfo.freight_type??>${productInfo.freight_type}
															<#elseif (productInfo.freight_type?? && productInfo.freight_type == 1)>包邮
															<#elseif (productInfo.freight_type?? && productInfo.freight_money?? && productInfo.freight_type == 2)>满${productInfo.freight_money}包邮
															<#elseif (productInfo.freight_type?? && productInfo.freight_type == 3)>不包邮
															<#elseif (productInfo.freight_type?? && productInfo.freight_other?? && productInfo.freight_type == 4)>${productInfo.freight_other}
															</#if></label>
															<input type="hidden" name="freightType">
															</td>
					</tr>
					<!-- <tr>
						<td><label>运费结算：</label><label id="freightName"><a href="javascript:void(0)" onclick="showFreightTemplate()"><#if productInfo.freightName??>${productInfo.freightName}</#if></a></label></td>
					</tr> -->
					<tr>
						<td><label>发货时效说明：</label><label id="sendTimeRemark">
															<#if (productInfo.send_time_remark?? && productInfo.id??)>${productInfo.send_time_remark}
															<#elseif (productInfo.send_time_type?? && productInfo.send_time_type == 1)>当天15点前订单当天24点前打包并提供物流单号，24小时内有物流信息
															<#elseif (productInfo.send_time_type?? && productInfo.send_time_type == 2)>单笔订单24小时发货
															<#elseif (productInfo.send_time_type?? && productInfo.send_time_type == 3)>单笔订单48小时发货
															<#elseif (productInfo.send_time_type?? && productInfo.send_time_type == 4)>单笔订单72小时发货
															</#if></label>
															<input type="hidden" name="sendTimeRemark">
															</td>
					</tr>
					<tr>
						<td><label>周末是否发货：</label><label id="sendWeekendRemark">
															<#if productInfo.send_weekend_remark??>${productInfo.send_weekend_remark}
															<#elseif (productInfo.is_send_weekend?? && productInfo.is_send_weekend == 1)>周末不发货
															<#elseif (productInfo.is_send_weekend?? && productInfo.is_send_weekend == 2)>周六发货
															<#elseif (productInfo.is_send_weekend?? && productInfo.is_send_weekend == 3)>周日发货
															<#elseif (productInfo.is_send_weekend?? && productInfo.is_send_weekend == 4)>周末发货
															</#if></label>
															<input type="hidden" name="sendWeekendRemark">
															</td>
					</tr>
					<tr>
						<td><label>发货地区限制：</label><label id="areaTemplateName"><a href="javascript:void(0)" onclick="showAreaTemplate()"><#if productInfo.areaTemplateName??>${productInfo.areaTemplateName}</#if></a></label>
								<input type="hidden" name="areaTemplateName" value="<#if productInfo.areaTemplateName??>${productInfo.areaTemplateName}</#if>">
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset>
				<legend>商品图片</legend>
				<table>
					<tr><td><a href="javascript:void(0)" class="easyui-linkbutton" onclick="openImage()">基本商品主图</a></td></tr>
					<tr>
						<td><label>商品主图：</label><br><#if productInfo.image1?? && productInfo.image1 != ''>
															<img id="image1" height="150" width="150" onclick="openWindow('${productInfo.image1}')" src="${productInfo.image1}">
															<input type="hidden" name="image1" value="${productInfo.image1}">
														</#if>
												<#if productInfo.image1_width??>&nbsp;宽：${productInfo.image1_width}&nbsp;高：${productInfo.image1_hight}</#if>
												<#if productInfo.image2?? && productInfo.image2 != ''>
													<img id="image2" height="150" width="150" onclick="openWindow('${productInfo.image2}')" src="${productInfo.image2}">
													<input type="hidden" name="image2" value="${productInfo.image2}">
												</#if>
												<#if productInfo.image2_width??>&nbsp;宽：${productInfo.image2_width}&nbsp;高：${productInfo.image2_hight}</#if>
												<#if productInfo.image3?? && productInfo.image3 != ''>
													<img id="image3" height="150" width="150" onclick="openWindow('${productInfo.image3}')" src="${productInfo.image3}">
													<input type="hidden" name="image3" value="${productInfo.image3}">
												</#if>
												<#if productInfo.image3_width??>&nbsp;宽：${productInfo.image3_width}&nbsp;高：${productInfo.image3_hight}</#if>
												<#if productInfo.image4?? && productInfo.image4 != ''>
													<img id="image4" height="150" width="150" onclick="openWindow('${productInfo.image4}')" src="${productInfo.image4}">
													<input type="hidden" name="image4" value="${productInfo.image4}">
												</#if>
												<#if productInfo.image4_width??>&nbsp;宽：${productInfo.image4_width}&nbsp;高：${productInfo.image4_hight}</#if>
												<#if productInfo.image5?? && productInfo.image5 != ''>
													<img id="image5" height="150" width="150" onclick="openWindow('${productInfo.image5}')" src="${productInfo.image5}">
													<input type="hidden" name="image5" value="${productInfo.image5}">
												</#if>
												<#if productInfo.image5_width??>&nbsp;宽：${productInfo.image5_width}&nbsp;高：${productInfo.image5_hight}</#if>
												</td>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr><td><a href="javascript:void(0)" class="easyui-linkbutton" onclick="openDetailImage()">基本商品详情图</a></td></tr>
					<tr>
						<td><label>商品详情图：</label>
							<#if (productInfo.detailImage?? && productInfo.detailImage?size>0)>
								<#list productInfo.detailImage as detail>
									<#if type?? && type == 2>
										<#if detail?? && detail!=''><br><img class="detailImage" width="250" onclick="openWindow('${detail}')" src="${detail}"></#if>
									<#else>
										<#if detail?? && detail.content?? && detail.content!=''><br><img class="detailImage" width="500" onclick="openWindow('${detail.content}')" src="${detail.content}"></#if>
                      					<#if detail?? && detail.width??>&nbsp;&nbsp;宽：${detail.width}</#if>
									</#if>
                      			</#list>
							</#if>
							</td>
					</tr>
				</table>
			</fieldset>
			<#if productInfo.check_content??>
				<fieldset>
					<legend>审核记录</legend>
					<table>
						<tr>
							<td><label><#if productInfo.status?? && productInfo.status == 3>审核未通过：</#if><#if productInfo.status?? && productInfo.status == 4>审核通过：</#if></label><label>${productInfo.check_content}</label></td>
						</tr>
					</table>
				</fieldset>
			<#else>
				<fieldset>
					<legend>审核</legend>
					<table>
						<tr>
							<td><input onclick="checkProduct(1)" type="button" value="审核通过：" /><textarea maxlength="100" id="content1" rows="2" cols="40"></textarea></td>
						</tr>
						<tr>
							<td><input onclick="checkProduct(2)" type="button" value="审核不通过：" /><textarea maxlength="100" id="content2" rows="2" cols="40"></textarea></td>
						</tr>
					</table>
				</fieldset>
			</#if>
		</form>
	</div>
	<div class="easyui-dialog" title="运费模板信息" style="height:300px;width:600px;font-size:14px;" modal="true" closed="true" id="showFreightTemplate"></div>
	<div class="easyui-dialog" title="配送地区模板信息" style="height:300px;width:600px;font-size:14px;" modal="true" closed="true" id="showAreaTemplate"></div>
	
	<div class="easyui-dialog" title="商品主图" style="height:300px;width:900px;font-size:14px;" modal="true" closed="true" id="productBaseImage">
		<img height="150" width="150" alt="" src="<#if productBaseInfo?? && productBaseInfo.image1??>${productBaseInfo.image1}</#if>">
		<img height="150" width="150" alt="" src="<#if productBaseInfo?? && productBaseInfo.image2??>${productBaseInfo.image2}</#if>">
		<img height="150" width="150" alt="" src="<#if productBaseInfo?? && productBaseInfo.image3??>${productBaseInfo.image3}</#if>">
		<img height="150" width="150" alt="" src="<#if productBaseInfo?? && productBaseInfo.image4??>${productBaseInfo.image4}</#if>">
		<img height="150" width="150" alt="" src="<#if productBaseInfo?? && productBaseInfo.image5??>${productBaseInfo.image5}</#if>">
	</div>
	<div class="easyui-dialog" title="商品明细图片" style="height:600px;width:600px;font-size:14px;" closed="true" id="productBaseDetailImage">
		<#if (productBaseDetailImage?? && productBaseDetailImage?size > 0)>
			<#list productBaseDetailImage as detailImage>
				<img height="150" width="150" alt="" src="<#if detailImage?? && detailImage.content??>${detailImage.content}</#if>">
			</#list>
		</#if>
	</div>
	
<script type="text/javascript">
function openImage() {
	$('#productBaseImage').dialog('open');
}
function openDetailImage() {
	$('#productBaseDetailImage').dialog('open');
}
	function showFreightTemplate() {
		var id = $('#id').val();
		var templateId = '';
		var type = '';
		if(id == '') {
			type = 2;
			templateId = $('#sellerFreightTemplateId').val();
		} else {
			type = 1;
			templateId = id;
		}
		$.ajax({
			url : "${rc.contextPath}/productCheck/showFreightTemplate",
	        type : 'post',
	        data : {'templateId':templateId,'type':type},
	        success : function(data) {
	        	if(data.status == 1) {
	        		var html = '<tr><td><label>运费名称：</label>' + data.info.name + '</td></tr>';
	        		var areas = data.info.area.split('|');
	        		for(var i = 0; i < areas.length; i++) {
	        			var area = areas[i].split(';');
	        			html += '<tr><td><label>运费：</label>' + (area[0] == 0 ? '包邮' : area[0]) + '</td></tr>';
	        			html += '<tr><td><label>地区：</label>' + area[1] + '</td></tr>';
	        		}
	        		$('#showFreightTemplate').empty().append(html);
	        		$('#showFreightTemplate').dialog('open');
	        	} else {
	        		$.messager.alert("失败提示", data.message);
	        	}
	        }
		});
	}
	function showAreaTemplate() {
		var id = $('#id').val();
		var templateId = '';
		var type = '';
		if(id == '') {
			type = 2;
			templateId = $('#sellerDeliverAreaTemplateId').val();
		} else {
			type = 1;
			templateId = id;
		}
		$.ajax({
			url : "${rc.contextPath}/productCheck/showDeliverAreaTemplate",
	        type : 'post',
	        data : {'templateId':templateId,'type':type},
	        success : function(data) {
	        	if(data.status == 1) {
	        		var html = '<tr><td><label>配送模板名称：</label>' + data.info.name + '</td></tr>';
        			html += '<tr><td><label>配送类型：</label>' + data.info.type + '</td></tr>';
        			html += '<tr><td><label>配送地区：</label>' + data.info.areaName + '</td></tr>';
        			html += '<tr><td><label>例外地区：</label>' + data.info.liwaiArea + '</td></tr>';
        			html += '<tr><td><label>配送描述：</label>' + data.info.areaDesc + '</td></tr>';
	        		$('#showAreaTemplate').empty().append(html);
	        		$('#showAreaTemplate').dialog('open');
	        	} else {
	        		$.messager.alert("失败提示", data.message);
	        	}
	        }
		});
	}
	function checkProduct(type) {
		if (confirm("是否确认审核")) {
			if (type == 1 && $('#content1').val() == '') {
				alert('审核说明不能为空');
				return;
			}
			if (type == 2 && $('#content2').val() == '') {
				alert('审核说明不能为空');
				return;
			}
			
			$('input[name=submit]').val($.trim($('#submit').text()));
			$('input[name=despatchType]').val($.trim($('#despatchType').text()));
			$('input[name=freightType]').val($.trim($('#freightType').text()));
			$('input[name=sendTimeRemark]').val($.trim($('#sendTimeRemark').text()));
			$('input[name=sendWeekendRemark]').val($.trim($('#sendWeekendRemark').text()));
			$('input[name=type]').val(type);
			
			var cont = ((type == 1) ? $('#content1').val() : $('#content2').val());
			$('input[name=checkContent]').val(cont);
			
			var detailImage = "";
			$('.detailImage').each(function(i) {
				if(i == 0) {
					detailImage +=  (typeof($(this).attr('src')) == 'undefined') ? '' : $(this).attr('src');
				} else {
					detailImage += ';' + ((typeof($(this).attr('src')) == 'undefined') ? '' : $(this).attr('src'));
				}
			});
			$('input[name=detailImage]').val(detailImage);
			
			$.ajax({
				url : "${rc.contextPath}/productCheck/saveCheckSnapshot",
		        type : 'post',
		        data : $('#checkProductForm').serialize(),
		        success : function(data) {
		        	if(data.status == 1) {
			        	$.messager.alert("提示", "审核成功");
		        		window.location.href="${rc.contextPath}/productCheck/checkList";
		        	} else {
		        		$.messager.alert("失败提示", data.message);
		        	}
		        }
			});
		}
	}
	function openWindow(src) {
		window.open(src);
	}
</script>
</body>
</html>