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

<div data-options="region:'center',title:'添加商家信息'" style="padding:5px;">
	<div>
		<#if wrongMessage?exists>
			<span style="color:red">${wrongMessage}</span>
		</#if>
	</div>
	
	<form id="saveSeller"  method="post">
		<fieldset>
			<input type="hidden" value="<#if seller.id?exists && (seller.id != 0)>${seller.id?c}</#if>" id="editId" name="editId" />
			<legend>商家信息</legend>
    		 前台展示商家名称： <input type="text" maxlength="20" id="sellerName" name="sellerName" style="width:300px;"  value="<#if seller.sellerName?exists>${seller.sellerName}</#if>" />
    		 <font style="color:red">*</font><br/><br/>
    		 真实商家名称：<input type="text" maxlength="20" id="realSellerName" name="realSellerName" style="width:300px;"  value="<#if seller.realSellerName?exists>${seller.realSellerName}</#if>" <#if seller.id?exists && (seller.id != 0)>disabled="disabled"</#if>/>
    		 <font color="red" size="2px">*(商家真实姓名保存后不可更改)</font>
    		 <br/><br/>
			 公司名称：<input type="text" maxlength="30" id="companyName" name="companyName" style="width:300px;" value="<#if seller.companyName?exists>${seller.companyName}</#if>" />
			 <font style="color:red">*</font><br/><br/>
			<table id="eshopAddress">
				<#if (storeList?size>0)>
				<#list storeList as shop>
				<tr>
					<td>店铺网址：<br/><br/></td>
					<td>
						<input type="hidden" value="${shop.id?c}" name="shopId" />
						<input type="text" value="${shop.url}"  name="shopURL" style="width: 450px;"/>
						<br/><br/>
					</td>
					<td>
						<span onclick="addEshopAddressRow(this)" style="cursor: pointer;color:red">&nbsp;添加&nbsp;|</span>
                   		<span onclick="removeEshopAddressRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
						<br/><br/>
					</td>
				</tr>
				</#list>
				<#else>
					<tr>
						<td>店铺网址：<br/><br/></td>
						<td>
							<input type="hidden" value="0" name="shopId" />
							<input type="text" value=""  name="shopURL" style="width: 450px;"/>
							<br/><br/>
						</td>
						<td>
							<span onclick="addEshopAddressRow(this)" style="cursor: pointer;color:red">&nbsp;添加&nbsp;|</span>
                    		<span onclick="removeEshopAddressRow(this)" style="cursor: pointer;color:red">&nbsp;删除</span>
							<br/><br/>
						</td>
					</tr>
				</#if>
			</table><br/><br/>
			 发货类型：
			 		<input type="radio" name="sellerType"  id="sellerType_1" value="1" <#if seller.sellerType?exists && (seller.sellerType <= 1) >checked</#if>> 国内发货&nbsp;&nbsp;&nbsp;
					<input type="radio" name="sellerType"  id="sellerType_2" value="2" <#if seller.sellerType?exists && (seller.sellerType == 2) >checked</#if>> 保税区发货&nbsp;&nbsp;&nbsp;
					<input type="radio" name="sellerType"  id="sellerType_3" value="3" <#if seller.sellerType?exists && (seller.sellerType == 3) >checked</#if>> 香港发货(仅身份证号)&nbsp;&nbsp;&nbsp;
					<input type="radio" name="sellerType"  id="sellerType_4" value="-1" <#if seller.sellerType?exists && (seller.sellerType >3) >checked</#if>> 其他国家(仅身份证号)
					<select name="sellerTypeOther" id="sellerTypeOther" >
						<option value="4" <#if seller.sellerType?exists && (seller.sellerType == 4) >selected</#if>>美国</option>
						<option value="5" <#if seller.sellerType?exists && (seller.sellerType == 5) >selected</#if>>日本</option>
						<option value="6" <#if seller.sellerType?exists && (seller.sellerType == 6) >selected</#if>>澳大利亚</option>
						<option value="7" <#if seller.sellerType?exists && (seller.sellerType == 7) >selected</#if>>德国</option>
						<option value="8" <#if seller.sellerType?exists && (seller.sellerType == 8) >selected</#if>>荷兰</option>
						<option value="9" <#if seller.sellerType?exists && (seller.sellerType == 9) >selected</#if>>台湾</option>
						<option value="10" <#if seller.sellerType?exists && (seller.sellerType == 10) >selected</#if>>加拿大</option>
						<option value="11" <#if seller.sellerType?exists && (seller.sellerType == 11) >selected</#if>>新西兰</option>
						<!-- <option value="10" <#if seller.sellerType?exists && (seller.sellerType == 10) >selected</#if>>香港(身份证照片)</option> -->
					</select>
					<br/><br/>
			<div <#if seller.sellerType?exists && (seller.sellerType == 2 || seller.sellerType == 3) >style="display: ''"<#else>style="display: none"</#if> id="bondedNumberTypeDiv">
			报关顺序：<input type="radio" name="bondedNumberType" id="bondedNumberType_1" value="1" <#if seller.bondedNumberType?exists && (seller.bondedNumberType ==1) >checked</#if>/> 先有物流单号后报关&nbsp;&nbsp;
				   <input type="radio" name="bondedNumberType" id="bondedNumberType_2" value="2" <#if seller.bondedNumberType?exists && (seller.bondedNumberType == 2) >checked</#if>/> 先报关后有物流单号&nbsp;&nbsp;&nbsp;
				   <br/><br/>
			</div>
			发货地&nbsp;：<input maxlength="10" type="text" id="sendAddress" name="sendAddress" style="width:300px;" value="<#if seller.sendAddress?exists>${seller.sendAddress}</#if>" /><font style="color:red">*</font><br/><br/>
			分仓&nbsp;&nbsp;：<input maxlength="20" type="text" id="warehouse" name="warehouse" style="width:300px;" value="<#if seller.warehouse?exists>${seller.warehouse}</#if>" /><br/><br/>
			默认快递：<input type="checkbox" name="kuaidi" id="kuaidi1" value="顺丰" <#if seller.kuaidi?exists && (seller.kuaidi?contains("顺丰"))>checked</#if>/>顺丰&nbsp;
					<input type="checkbox" name="kuaidi" id="kuaidi2" value="申通" <#if seller.kuaidi?exists && (seller.kuaidi?contains("申通"))>checked</#if>/>申通&nbsp;
					<input type="checkbox" name="kuaidi" id="kuaidi3" value="圆通" <#if seller.kuaidi?exists && (seller.kuaidi?contains("圆通"))>checked</#if>/>圆通&nbsp;
					<input type="checkbox" name="kuaidi" id="kuaidi4" value="中通" <#if seller.kuaidi?exists && (seller.kuaidi?contains("中通"))>checked</#if>/>中通&nbsp;
					<input type="checkbox" name="kuaidi" id="kuaidi5" value="韵达" <#if seller.kuaidi?exists && (seller.kuaidi?contains("韵达"))>checked</#if>/>韵达&nbsp;
					<input type="checkbox" name="kuaidi" id="kuaidi5" value="EMS" <#if seller.kuaidi?exists && (seller.kuaidi?contains("EMS"))>checked</#if>/>EMS&nbsp;
					<input type="checkbox" name="kuaidi" id="kuaidi5" value="天天快递" <#if seller.kuaidi?exists && (seller.kuaidi?contains("天天快递"))>checked</#if>/>天天快递&nbsp;
					<input type="checkbox" name="kuaidi" id="kuaidi6" value="other" <#if seller.otherKuaidi?? && (seller.otherKuaidi !="")>checked</#if>/>其他&nbsp;
					<input type="text" name="otherKuaidi" id="otherKuaidi" value="<#if seller.otherKuaidi?exists>${seller.otherKuaidi}</#if>" maxlength="30" style="width:300px;"/>
					<span style="color:red">注：多个用,号隔开</span>
					<br/><br/>
			运费结算方式：
					<input type="radio" name="freightType" id="freightType1" value="1" <#if seller.freightType?exists && (seller.freightType<=1)>checked</#if>> 包邮&nbsp;&nbsp;
					<input type="radio" name="freightType" id="freightType2" value="2" <#if seller.freightType?exists && (seller.freightType==2)>checked</#if>> 满
					<input type="text" name="freightMoney" id="freightMoney" value="<#if seller.freightMoney?exists && (seller.freightMoney !=0)>${seller.freightMoney}</#if>" maxlength="30" style="width:50px;"/>包邮&nbsp;&nbsp;
					<input type="radio" name="freightType" id="freightType3" value="3" <#if seller.freightType?exists && (seller.freightType==3)>checked</#if>> 全部不包邮&nbsp;&nbsp;
					<input type="radio" name="freightType" id="freightType4" value="4" <#if seller.freightType?exists && (seller.freightType==4)>checked</#if>> 其他
					<input type="text" name="freightOther" id="freightOther" value="<#if seller.freightOther?exists>${seller.freightOther}</#if>" maxlength="30" style="width:300px;"/>
					<br/><br/>
			发货时效说明：<br/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="sendTimeType_1" name="sendTimeType" value="1" <#if seller.sendTimeType?exists && (seller.sendTimeType<=1)>checked</#if>>当天15点前订单当天打包并提供物流单号，24小时内有物流信息<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="sendTimeType_2" name="sendTimeType" value="2" <#if seller.sendTimeType?exists && (seller.sendTimeType==2)>checked</#if>>24小时内打包并提供物流单号，48小时内有物流信息<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="sendTimeType_3" name="sendTimeType" value="3" <#if seller.sendTimeType?exists && (seller.sendTimeType==3)>checked</#if>>48小时内打包并提供物流单号，72小时内有物流信息<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="sendTimeType_4" name="sendTimeType" value="4" <#if seller.sendTimeType?exists && (seller.sendTimeType==4)>checked</#if>>其他
					<input type="text" id="sendTimeRemark" name="sendTimeRemark" maxlength="30" style="width:300px;" value="<#if seller.sendTimeRemark?exists>${seller.sendTimeRemark}</#if>"/><br/><br/>
			周末是否发货：<input type="checkbox" name="sendWeekend" id="sendWeekend1" value="2" <#if seller.isSendWeekend?exists && (seller.isSendWeekend == 2 || seller.isSendWeekend == 4)>checked</#if>/>周六可发货&nbsp;&nbsp;
					  <input type="checkbox" name="sendWeekend" id="sendWeekend2" value="3" <#if seller.isSendWeekend?exists && (seller.isSendWeekend == 3 || seller.isSendWeekend == 4)>checked</#if>/>周日可发货&nbsp;&nbsp;
					  <font color="red">注：哪天能发勾哪个，两个都不勾表示两天都不能发</font><br/><br/>
			假期发货提示：<input maxlength="20" type="text" id="holidayTips" name="holidayTips" style="width:300px;" value="<#if seller.holidayTips?exists>${seller.holidayTips}</#if>" />
					<font color="red">假期发货可填写：国庆节期间正常发货；假期不发货可填写：国庆节期间不发货，统一在节后发出（若无假期临近，可不填）</font><br/><br/>
			假期开始时间：<input maxlength="20" type="text" id="holidayStartTime" name="holidayStartTime"  style="width:300px;" value="<#if seller.holidayStartTime?exists>${seller.holidayStartTime}</#if>" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'holidayEndTime\')}'})"/>
					<font color="red">可填写：2015-10-01 00:00:00（若无假期临近可不填）</font><br/><br/>
			假期结束时间：<input maxlength="20" type="text" id="holidayEndTime" name="holidayEndTime"  style="width:300px;" value="<#if seller.holidayEndTime?exists>${seller.holidayEndTime}</#if>" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'holidayStartTime\')}'})"/>
					<font color="red">可填写：2015-11-07 23:59:59（若无假期临近可不填）</font><br/><br/>			
			<!-- 预计发货后到货时间：<input type="text" id="expectArriveTime" name="expectArriveTime" onblur="stringTrim(this);" value="<#if seller.expectArriveTime?exists>${seller.expectArriveTime}</#if>" maxlength="5"/>天左右
					  <font color="red">注：该时间从物流单号生成之日起算，如该商家先有单号再报关，该时间需包含报关时间。</font><br/><br/>	 -->  
			<a onclick="copyDeliverAreaFromSeller()" href="javascript:;" class="easyui-linkbutton">从其他商家信息复制配送地区描述和设置</a><br/><br/>
			配送地区描述：<input type="text" name="deliverAreaDesc" id="deliverAreaDesc" value="<#if seller.deliverAreaDesc?exists>${seller.deliverAreaDesc}</#if>" maxlength="50" style="width:300px;"/><br/><br/>
			配送地区设置：<input type="radio" name="deliverAreaType" id="deliverAreaType1" value="1" <#if seller.deliverAreaType?exists && (seller.deliverAreaType<=1)>checked</#if>/>设置支持地区&nbsp;&nbsp;
					  <input type="radio" name="deliverAreaType" id="deliverAreaType2" value="2" <#if seller.deliverAreaType?exists && (seller.deliverAreaType==2)>checked</#if>/>设置不支持地区&nbsp;&nbsp;
					  <table id="categoryTab" style="margin-left: 80px;">
						<#if (areasList?size>0)>
						<#list areasList as areas>
						<tr>
							<td style="display:none">${areas.id?c}</td>
							<td>
								<select name="provinceCode" style="width: 160px;">
									<option value="1">-全部省份-</option>
									<#list areas.provinceList as province>
  		 			  					<option value="${province.provinceId?c}"<#if areas.provinceCode?exists && (areas.provinceCode == province.provinceId)>selected</#if>>${province.name}</option>
  		 			  					</#list>
								</select>
							</td>
							<td>
								<select name="cityCode" style="width: 160px;">
   		 							<option value="1">-全部市-</option>
   		 			  				<#list areas.cityList as city>
   		 			  				<option value="${city.cityId?c}"<#if areas.cityCode?exists && (areas.cityCode == city.cityId)>selected</#if>>${city.name}</option>
   		 			  				</#list>	
  		 							</select>
							</td>
							<td>
								<select name="districtCode" style="width: 160px;">
   		 							<option value="1">-全部区-</option>
   		 			  				<#list areas.districtList as district>
   		 			  				<option value="${district.districtId?c}"<#if areas.districtCode?exists && (areas.districtCode == district.districtId)>selected</#if>>${district.name}</option>
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
								<select name="provinceCode" style="width: 160px;">
									<option value="1">-全部省份-</option>
									<#list provinceList as province>
  		 			  					<option value="${province.provinceId?c}">${province.name}</option>
  		 			  					</#list>
								</select>
							</td>
							<td>
								<select name="cityCode" style="width: 160px;">
   		 							<option value="1">-全部市-</option>
   		 			  				<#list cityList as city>
   		 			  				<option value="${city.cityId?c}">${city.name}</option>
   		 			  				</#list>	
  		 							</select>
							</td>
							<td>
								<select name="districtCode" style="width: 160px;">
   		 							<option value="1">-全部区-</option>
   		 			  				<#list districtList as district>
   		 			  				<option value="${district.districtId?c}">${district.name}</option>
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
					</table><br/>
			发货依据：<input type="radio" id="sendCodeType_1" name="sendCodeType" value="1" <#if seller.sendCodeType?exists && (seller.sendCodeType<=1)>checked</#if>>商品编码&nbsp;&nbsp;
					<input type="radio" id="sendCodeType_2" name="sendCodeType" value="2" <#if seller.sendCodeType?exists && (seller.sendCodeType==2)>checked</#if>>商品条码&nbsp;&nbsp;
					<input type="radio" id="sendCodeType_3" name="sendCodeType" value="3" <#if seller.sendCodeType?exists && (seller.sendCodeType==3)>checked</#if>>商品名称&nbsp;&nbsp;
					<input type="radio" id="sendCodeType_4" name="sendCodeType" value="4" <#if seller.sendCodeType?exists && (seller.sendCodeType==4)>checked</#if>>其他
					<input type="text" id="sendCodeRemark" name="sendCodeRemark" maxlength="30" style="width:300px;" value="<#if seller.sendCodeRemark?exists>${seller.sendCodeRemark}</#if>"/><br/><br/>
			结算周期：<input type="radio" name="settlementPeriod" id="settlementPeriod_1" value="1" <#if seller.settlementPeriod?exists && (seller.settlementPeriod<=1)>checked</#if>>日结&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="radio" name="settlementPeriod" id="settlementPeriod_2" value="2" <#if seller.settlementPeriod?exists && (seller.settlementPeriod==2)>checked</#if>>活动结束后
					<input type="text" name="settlementDay" id="settlementDay" value="<#if seller.settlementDay?exists && (seller.settlementDay !=0)>${seller.settlementDay}</#if>" maxlength="30" style="width:50px;"/>天结&nbsp;&nbsp;
					<!-- <input type="radio" name="settlementPeriod" id="settlementPeriod_3" value="3" <#if seller.settlementPeriod?exists && (seller.settlementPeriod==3)>checked</#if>>其他
					<input type="text" name="settlementOther" id="settlementOther" value="<#if seller.settlementOther?exists>${seller.settlementOther}</#if>" maxlength="30" style="width:300px;"/><br/> -->
			<br/><hr/><br/>
			发货联系人<br/><br/>
			姓名：<input maxlength="10" type="text" id="fhContactPerson" name="fhContactPerson" style="width:200px;"  value="<#if seller.fhContactPerson?exists>${seller.fhContactPerson}</#if>" /> <font style="color:red">*</font>&nbsp;&nbsp;&nbsp;
			手机：<input maxlength="30" type="text" id="fhContactMobile" name="fhContactMobile" style="width:200px;"  value="<#if seller.fhContactMobile?exists>${seller.fhContactMobile}</#if>" /> <font style="color:red">*</font>&nbsp;&nbsp;&nbsp;
			 QQ&nbsp;：<input maxlength="15" type="text" id="fhqq" name="fhqq" style="width:200px;"  value="<#if seller.fhqq?exists>${seller.fhqq}</#if>" /> <font style="color:red">*</font>&nbsp;&nbsp;&nbsp;
			邮箱：<input maxlength="30" type="text" id="fhEmail" name="fhEmail" style="width:200px;"  value="<#if seller.fhEmail?exists>${seller.fhEmail}</#if>" /> <font style="color:red">*</font>
			<br/><br/><br/><br/>
			
			售后联系人 &nbsp;<a onclick="copyInfo(1)" href="javascript:;" class="easyui-linkbutton">从发货联系人复制</a><br/><br/>
			姓名：<input maxlength="10" type="text" id="shContactPerson" name="shContactPerson" style="width:300px;"  value="<#if seller.shContactPerson?exists>${seller.shContactPerson}</#if>" /> <font style="color:red">*</font>&nbsp;&nbsp;&nbsp;
			手机：<input maxlength="30" type="text" id="shContactMobile" name="shContactMobile" style="width:300px;"  value="<#if seller.shContactMobile?exists>${seller.shContactMobile}</#if>" /> <font style="color:red">*</font><br/><br/>
			QQ&nbsp;：<input maxlength="15" type="text" id="shqq" name="shqq" style="width:300px;"  value="<#if seller.shqq?exists>${seller.shqq}</#if>" /> <font style="color:red">*</font>&nbsp;&nbsp;&nbsp;
			邮箱：<input maxlength="30" type="text" id="shEmail" name="shEmail" style="width:300px;"  value="<#if seller.shEmail?exists>${seller.shEmail}</#if>" /> <font style="color:red">*</font><br/><br/>
			旺旺：<input maxlength="50" type="text" id="shAliwang" name="shAliwang" style="width:300px;"  value="<#if seller.shAliwang?exists>${seller.shAliwang}</#if>" />&nbsp;&nbsp;&nbsp;&nbsp;
			备注：<input maxlength="100" type="text" id="shRemark" name="shRemark" style="width:300px;"  value="<#if seller.shRemark?exists>${seller.shRemark}</#if>" /><br/><br/><br/><br/>

			结算联系人 &nbsp;<a onclick="copyInfo(2)" href="javascript:;" class="easyui-linkbutton">从发货联系人复制</a>&nbsp;<a onclick="copyInfo(3)" href="javascript:;" class="easyui-linkbutton">从售后联系人复制</a><br/><br/>
			姓名：<input maxlength="10" type="text" id="jsContactPerson" name="jsContactPerson" style="width:300px;"  value="<#if seller.jsContactPerson?exists>${seller.jsContactPerson}</#if>" /> <font style="color:red">*</font>&nbsp;&nbsp;&nbsp;
			手机：<input maxlength="30" type="text" id="jsContactMobile" name="jsContactMobile" style="width:300px;"  value="<#if seller.jsContactMobile?exists>${seller.jsContactMobile}</#if>" /> <font style="color:red">*</font><br/><br/>
			QQ&nbsp;：<input maxlength="15" type="text" id="jsqq" name="jsqq" style="width:300px;"  value="<#if seller.jsqq?exists>${seller.jsqq}</#if>" /> <font style="color:red">*</font>&nbsp;&nbsp;&nbsp;
			邮箱：<input maxlength="30" type="text" id="jsEmail" name="jsEmail" style="width:300px;"  value="<#if seller.jsEmail?exists>${seller.jsEmail}</#if>" /> <font style="color:red">*</font><br/><br/>
			旺旺：<input maxlength="50" type="text" id="jsAliwang" name="jsAliwang" style="width:300px;"  value="<#if seller.jsAliwang?exists>${seller.jsAliwang}</#if>" />&nbsp;&nbsp;&nbsp;&nbsp;
			备注：<input maxlength="100" type="text" id="jsRemark" name="jsRemark" style="width:300px;"  value="<#if seller.jsRemark?exists>${seller.jsRemark}</#if>" /><br/><br/><br/>	
			<br/><hr/><br/>
			商家退货收件信息<br/><br/>
			<input type="hidden" name="id" id="id" value="<#if sellerExpand.id?exists>${sellerExpand.id?c}</#if>"/>
			<input type="hidden" name="sellerId" id="sellerId" value="<#if seller.id?exists>${seller.id?c}</#if>"/>
			&nbsp;&nbsp;省：<select name="receiveProvinceCode" id="receiveProvinceCode">
				<option value="0">-请选择-</option>
               	<#list provinceList as bl >
				<option value="${bl.provinceId?c}" <#if sellerExpand.receiveProvinceCode?exists && (sellerExpand.receiveProvinceCode == bl.provinceId)>selected</#if> >${bl.name}</option>
				</#list>
			 </select><font style="color:red">*</font>&nbsp;&nbsp;
			 市：<select name="receiveCityCode" id="receiveCityCode">
                <option value="0">-请选择-</option>
                <#list cityList as bl >
			 	<option value="${bl.cityId?c}" <#if sellerExpand.receiveCityCode?exists && (sellerExpand.receiveCityCode == bl.cityId)>selected</#if> >${bl.name}</option>
			 	</#list>
			 </select><font style="color:red">*</font>&nbsp;&nbsp;
			 区：<select name="receiveDistrictCode" id="receiveDistrictCode">
                <option value="0">-请选择-</option>
                <#list districtList as bl >
			 	<option value="${bl.districtId?c}" <#if sellerExpand.receiveDistrictCode?exists && (sellerExpand.receiveDistrictCode == bl.districtId)>selected</#if> >${bl.name}</option>
			 	</#list>
			 </select><font style="color:red">*</font>&nbsp;&nbsp;
			 详细地址：<input type="text" name="receiveDetailAddress" id="receiveDetailAddress" style="width:400px;" maxlength="200" value="<#if sellerExpand.receiveDetailAddress?exists>${sellerExpand.receiveDetailAddress}</#if>"/><font style="color:red">*</font><br/><br/>
			 收件人：<input type="text" name="receivePerson" id="receivePerson" value="<#if sellerExpand.receivePerson?exists>${sellerExpand.receivePerson}</#if>"/><font style="color:red">*</font>&nbsp;&nbsp;&nbsp;
			 联系人电话：<input type="text" name="receiveTelephone" id="receiveTelephone" value="<#if sellerExpand.receiveTelephone?exists>${sellerExpand.receiveTelephone}</#if>"/><font style="color:red">*</font><br/><br/>
			<table id="imageDetails">
				<#if (mobileDetails?size>0)>
				<#list mobileDetails as images>
				<tr>
					<td><label>付款信息图片</label><br/><br/></td>
					<td>
						<input type="hidden" value="${images.id?c}" name="imageId" />
						<input type="text" value="${images.image}"  name="image" style="width: 450px;"/>
						<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
						<a onclick="viewPicture(this)" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
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
						<td><label>付款信息图片</label><br/><br/></td>
						<td>
							<input type="hidden" value="0" name="imageId" />
							<input type="text" value=""  name="image" style="width: 450px;"/>
							<a onclick="picDialogOpen(this)" href="javascript:;" class="easyui-linkbutton">上传图片</a>
							<a onclick="viewPicture(this)" href="javascript:;" class="easyui-linkbutton" name="viewDetails">打开图片</a>
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
			<hr/><br/>
			 招商负责人&nbsp;：<input type="text" id="responsibilityPerson" name="responsibilityPerson" style="width:300px;"  value="<#if seller.responsibilityPerson?exists>${seller.responsibilityPerson}</#if>" />
			 <font style="color:red">*</font><br/><br/>
			 商家状态：
			 <input type="radio" name="isAvailable" value="1" <#if seller.isAvailable?exists && (seller.isAvailable == 1) >checked</#if>> 可用&nbsp;&nbsp;&nbsp;
			 <input type="radio" name="isAvailable" value="0" <#if seller.isAvailable?exists && (seller.isAvailable == 0) >checked</#if>> 停用<br/><br/>
			 <hr/><br/>
			 <!-- 周末发货备注：<input maxlength="100" type="text" id="weekendRemark" name="weekendRemark" style="width:500px;"  value="" /><br/><br/> --><br/>
			 <input style="width: 150px" type="button" id="saveButton" value="保存"/>
		</fieldset>	
	</form>
	
	<div id="copyFromOtherSellerDiv" class="easyui-dialog" style="width:400px;height:120px;padding:10px 10px;">
       <form id="copyFromOtherSeller_Form" method="post">
       		商家ID：<input type="text" id="copyFromOtherSellerForm_sellerId"/>
       </form>
    </div>
    
	<div id="picDia" class="easyui-dialog" icon="icon-save" align="center" style="padding: 5px; width: 300px; height: 150px;">
	    <form id="picForm" method="post" enctype="multipart/form-data">
	        <input id="picFile" type="file" name="picFile" /><br/><br/>
	        <a href="javascript:;" onclick="picUpload()" class="easyui-linkbutton" iconCls='icon-reload'>提交图片</a>
	    </form>
	</div>	
	
	
</div>


<script type="text/javascript">

	var isSubmitSuccess = true;
	
	function stringTrim(obj){
		$(obj).val($.trim($(obj).val()));	
	}

	function filterNullParameter(form){
		var params = "";
		$.each(form.split("&"),function(i, item){
			var arryStr = item.split("=");
			if($.trim(arryStr[1])!='' && arryStr[1]!=null && arryStr[1]!='null' && arryStr[1]!=undefined){
				params +=item +"&";
			}
		});
		
		var areaCodes = "";
		$("#categoryTab").find("tr").each(function(){
			 var id = $(this).find("td").eq(0).text();
			 var provinceCode = $(this).find("td").eq(1).find("select[name='provinceCode']").val();
			 var cityCode = $(this).find("td").eq(2).find("select[name='cityCode']").val();
			 var districtCode = $(this).find("td").eq(3).find("select[name='districtCode']").val();
			 areaCodes+=id+","+provinceCode+","+cityCode+","+districtCode+";";
		});
		params = params+"areaCodes="+areaCodes;
		
		var imageIds = $("input[name='imageId']");
		var images = $("input[name='image']");
		var imageIdAndImage = "";
		$.each(imageIds,function(i,item){
			var id = $(imageIds[i]).val();
			var content = $(images[i]).val();
			imageIdAndImage +=(id+","+content+";");
		});
		params = params + "&imageIdAndImages="+imageIdAndImage;
		
		var shopIds = $("input[name='shopId']");
		var shopURLs = $("input[name='shopURL']");
		var shopIdAndUrls = "";
		$.each(shopIds,function(i,item){
			var id = $(shopIds[i]).val();
			var content = $(shopURLs[i]).val();
			shopIdAndUrls +=(id+","+content+";");
		});
		shopIdAndUrls = shopIdAndUrls.substring(0,shopIdAndUrls.length-1);
		params = params + "&shopIdAndUrls="+shopIdAndUrls;
		return params;
	}
	
	function copyInfo(type){
		//从发货联系人复制到售后联系人
		if(type===1){
			$("#shContactPerson").val($("#fhContactPerson").val());
			$("#shContactMobile").val($("#fhContactMobile").val());
			$("#shqq").val($("#fhqq").val());
			$("#shEmail").val($("#fhEmail").val());
			/* $("#shAliwang").val($("#fhAliwang").val());
			$("#shRemark").val($("#fhRemark").val()); */
		}
		//从发货联系人复制到结算联系人
		if(type===2){
			$("#jsContactPerson").val($("#fhContactPerson").val());
			$("#jsContactMobile").val($("#fhContactMobile").val());
			$("#jsqq").val($("#fhqq").val());
			$("#jsEmail").val($("#fhEmail").val());
			/* $("#jsAliwang").val($("#fhAliwang").val());
			$("#jsRemark").val($("#fhRemark").val()); */
		}
		//从售后联系人复制到结算联系人
		if(type===3){
			$("#jsContactPerson").val($("#shContactPerson").val());
			$("#jsContactMobile").val($("#shContactMobile").val());
			$("#jsqq").val($("#shqq").val());
			$("#jsEmail").val($("#shEmail").val());
			$("#jsAliwang").val($("#shAliwang").val());
			$("#jsRemark").val($("#shRemark").val());
		}
	}
	
    function addCategoryRow(obj){
    	var row = $(obj).parent().parent().clone(true);
    	/* var length = $(obj).parent().parent().parent().find("tr").size();
    	if(length > 5){
    		$.messager.alert("提示","最多允许添加5个分类","error");
    		return false;
    	} */
    	$(row).find("td").eq(0).text('0');
    	$(row).find("td").eq(1).find("option").eq(0).attr("selected","selected");
    	$(row).find("td").eq(2).find("option").eq(0).attr("selected","selected");
    	$(row).find("td").eq(3).find("option").eq(0).attr("selected","selected");
    	$(obj).parent().parent().parent().find("tr").last().after(row);
    }
    
    function delCategoryRow(obj){
    	var length = $(obj).parent().parent().parent().find("tr").size();
    	/* if(length <= 1){
    		$.messager.alert("提示","必须有1个分类","error");
    		return false;
    	} */
    	$(obj).parent().parent().remove();
    }
	
	$(function(){
        $('#picDia').dialog({
            title:'又拍图片上传窗口',
            collapsible:true,
            closed:true,
            modal:true
//            draggable:true
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
		
		$('#sellerTypeOther').change(function(){
			$("#bondedNumberTypeDiv").css('display','none');
			$("#sellerType_1").prop('checked',false);
			$("#sellerType_2").prop('checked',false);
			$("#sellerType_3").prop('checked',false);
			$("#sellerType_4").prop('checked',true);
			$("#bondedNumberType_1").prop('checked',true);
			$("#bondedNumberType_2").prop('checked',false);
		});
		
		$("#saveButton").click(function(){
			var sellerName = $("#sellerName").val();
			var realSellerName = $("#realSellerName").val();
			var companyName = $("#companyName").val();
			var sendAddress = $("#sendAddress").val();
			var sellerType = $("input[name='sellerType']:checked").val();
			var bondedNumberType = $("input[name='bondedNumberType']:checked").val();
			var expectArriveTime = $("#expectArriveTime").val();
			var fhContactPerson = $("#fhContactPerson").val();
			var fhContactMobile = $("#fhContactMobile").val();
			var fhqq = $("#fhqq").val();
			var fhEmail = $("#fhEmail").val();
			var shContactPerson = $("#shContactPerson").val();
			var shContactMobile = $("#shContactMobile").val();
			var shqq = $("#shqq").val();
			var shEmail = $("#shEmail").val();
			var jsContactPerson = $("#jsContactPerson").val();
			var jsContactMobile = $("#jsContactMobile").val();
			var jsqq = $("#jsqq").val();
			var jsEmail = $("#jsEmail").val();
			var mobileReg = /^[1][3,4,5,7,8][0-9]{9}$/;
			var qqReg = /^[1-9][0-9]{3,10}$/;
			var emailReg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
			var intReg = /^[1-9][0-9]*$/ ;
			var floatReg = /^\d+(\.\d+)?$/ ;
			var settlementPeriod = $("input[name='settlementPeriod']:checked").val();
			var settlementDay = $("#settlementDay").val();
			
			var responsibilityPerson = $("#responsibilityPerson").val();
			var editId = $("#editId").val();
			var holidayTips = $("#holidayTips").val();
			var holidayStartTime = $("#holidayStartTime").val();
			var holidayEndTime = $("#holidayEndTime").val();
			
			var receiveProvinceCode = $("#receiveProvinceCode").val();
			var receiveCityCode = $("#receiveCityCode").val();
			var receiveDistrictCode = $("#receiveDistrictCode").val();
			var receiveDetailAddress = $("#receiveDetailAddress").val();
			var receivePerson = $("#receivePerson").val();
			var receiveTelephone = $("#receiveTelephone").val();
			
			if(sellerName == ""){
				$.messager.alert("提示","商家名称必填","info");
			}else if(realSellerName == ""){
				$.messager.alert("提示","真实商家名称必填","info");
			}else if(companyName == ""){
				$.messager.alert("提示","公司名称必填","info");
			}else if((sellerType==2 || sellerType==3) && (bondedNumberType == '' || bondedNumberType == null || bondedNumberType == undefined)){
				$.messager.alert("提示","请选择报关顺序","info");
			}else if(sendAddress == ""){
				$.messager.alert("提示","发货地必填","info");
			}/* else if(!/^[1-9][0-9]*$/.test(expectArriveTime)){
				$.messager.alert("提示","预计发货后到货时间必填且只能为大于0的正整数","info");
			} */else if(settlementPeriod == '' || settlementPeriod == null || settlementPeriod == undefined ){
				$.messager.alert("提示","请选择结算周期","info");
			}else if(settlementPeriod == 2 && !/^[1-9][0-9]*$/.test(settlementDay)){
				$.messager.alert("提示","结算周期为'活动结束后N天结算'，天数必填且只能为数字","info");
			}else if($.trim(fhContactPerson)==''){
				$.messager.alert("提示","发货联系人姓名必填","info");
			}else if($.trim(fhContactMobile)==''){
				$.messager.alert("提示","发货联系人手机必填","info");
			}else if($.trim(fhqq)==''){
				$.messager.alert("提示","发货联系人QQ必填","info");
			}else if($.trim(fhEmail)==''){
				$.messager.alert("提示","发货联系人邮箱必填","info");
			}else if($.trim(shContactPerson)==''){
				$.messager.alert("提示","售后联系人姓名必填","info");
			}else if($.trim(shContactMobile)==''){
				$.messager.alert("提示","售后联系人手机必填","info");
			}else if($.trim(shqq)==''){
				$.messager.alert("提示","售后联系人QQ必填","info");
			}else if($.trim(shEmail)==''){
				$.messager.alert("提示","售后联系人邮箱必填","info");
			}else if($.trim(jsContactPerson)==''){
				$.messager.alert("提示","结算联系人姓名必填","info");
			}else if($.trim(jsContactMobile)==''){
				$.messager.alert("提示","结算联系人手机必填","info");
			}else if($.trim(jsqq)==''){
				$.messager.alert("提示","结算联系人QQ必填","info");
			}else if($.trim(jsEmail)==''){
				$.messager.alert("提示","结算联系人邮箱必填","info");
			}else if(responsibilityPerson == ""){
				$.messager.alert("提示","招商负责人必填","info");
			}else if(!(($.trim(holidayTips)=='' && $.trim(holidayStartTime)=='' && $.trim(holidayEndTime)=='')||
			($.trim(holidayTips)!='' && $.trim(holidayStartTime)!='' && $.trim(holidayEndTime)!=''))){
				$.messager.alert("提示","填写了假期发货信息但是信息不完整","info");
			}else if(receiveProvinceCode == '0' || receiveProvinceCode == '' || receiveProvinceCode == undefined || receiveProvinceCode == null){
				$.messager.alert("提示","请选择商家退货收件地址省份","info");
			}else if(receiveCityCode == '0' || receiveCityCode == '' || receiveCityCode == undefined || receiveCityCode == null){
				$.messager.alert("提示","请选择商家退货收件地址城市","info");
			}else if(receiveDistrictCode == '0' || receiveDistrictCode == '' || receiveDistrictCode == undefined || receiveDistrictCode == null){
				$.messager.alert("提示","请选择商家退货收件地址地区","info");
			}else if($.trim(receiveDetailAddress) == ''){
				$.messager.alert("提示","请填写商家退货收件详细地址","info");
			}else if($.trim(receivePerson) == ''){
				$.messager.alert("提示","请填写收件人","info");
			}else if(!/^\d+/.test(receiveTelephone)){
				$.messager.alert("提示","请填写正确的收件人联系电话","info");
			}else{
				/* if(!mobileReg.test(fhContactMobile)){
					$.messager.alert("提示","发货联系人手机号格式不正确","info");
					return false;
				} */
				if(!qqReg.test(fhqq)){
					$.messager.alert("提示","发货联系人QQ号格式不正确","info");
					return false;
				}
				if(!emailReg.test(fhEmail)){
					$.messager.alert("提示","发货联系人邮箱格式不正确","info");
					return false;
				}
				/* if(!mobileReg.test(shContactMobile)){
					$.messager.alert("提示","售后联系人手机号格式不正确","info");
					return false;
				} */
				if(!qqReg.test(shqq)){
					$.messager.alert("提示","售后联系人QQ号格式不正确","info");
					return false;
				}
				if(!emailReg.test(shEmail)){
					$.messager.alert("提示","售后联系人邮箱格式不正确","info");
					return false;
				}
				/* if(!mobileReg.test(jsContactMobile)){
					$.messager.alert("提示","结算联系人手机号格式不正确","info");
					return false;
				} */
				if(!qqReg.test(jsqq)){
					$.messager.alert("提示","结算联系人QQ号格式不正确","info");
					return false;
				}
				if(!emailReg.test(jsEmail)){
					$.messager.alert("提示","结算联系人邮箱格式不正确","info");
					return false;
				}
				if(!isSubmitSuccess){
					console.log("表单未提交成功又再次被点击");
					return false;
				}
				isSubmitSuccess = false;
				$.post("${rc.contextPath}/seller/checkName",
   						{name:realSellerName,editId:editId},
              			function(data){
                      		if(data.status != 1){
                      			$.messager.confirm("确认信息",data.msg,function(re){
                      				if(re){
                      					$.messager.progress(); 
                      					$.ajax({
                      						url:'${rc.contextPath}/seller/save',
                      						type:'post',
                      						data:filterNullParameter($("#saveSeller").serialize()),
                      						dataType:'json',
                      						success:function(data){
                      							if(data.status == 1){
                      								$.messager.alert("提示","保存成功","info",function(){
                      									window.location.href = '${rc.contextPath}/seller/list/1';
                      								});
                      							}else{
                      								$.messager.progress('close');
                      								$.messager.alert("提示",data.msg,"info");
                      							}
                      						},
                      						error:function(){
                      							$.messager.progress('close');
                      							$.messager.alert("提示","保存失败","info");
                      						}
                      					});
                      				}
                      			});
                       		}else{
                       			$.messager.progress(); 
                       			$.ajax({
              						url:'${rc.contextPath}/seller/save',
              						type:'post',
              						data:filterNullParameter($("#saveSeller").serialize()),
              						dataType:'json',
              						success:function(data){
              							if(data.status == 1){
              								$.messager.alert("提示","保存成功","info",function(){
              									window.location.href = '${rc.contextPath}/seller/list/1';
              								});
              							}else{
              								$.messager.progress('close');
              								$.messager.alert("提示",data.msg,"info");
              							}
              						},
              						error:function(){
              							$.messager.progress('close');
              							$.messager.alert("提示","保存失败","info");
              						}
              					});
                       		}
                		},
				"json");
			}
			isSubmitSuccess = true;
    	});
		
		
		var freightType = $("input[name='freightType']:checked").val();
		var freightMoney = $("#freightMoney").val();
		var freightOther = $("#freightOther").val();
		$("#freightType1").change(function(){
			if($(this).is(":checked")){
				$("#freightMoney").val('');
				$("#freightOther").val('');
			}
		});
		$("#freightType2").change(function(){
			if($(this).is(":checked")){
				$("#freightMoney").val(freightMoney);
				$("#freightOther").val('');
				if(freightType==2){
					$("#freightMoney").val(freightMoney);
				}
			}
		});
		$("#freightType3").change(function(){
			if($(this).is(":checked")){
				$("#freightMoney").val('');
				$("#freightOther").val('');
			}
		});
		$("#freightType4").change(function(){
			if($(this).is(":checked")){
				$("#freightMoney").val('');
				if(freightType==4){
					$("#freightOther").val(freightOther);
				}
			}
		});
		
		
		var sendTimeType = $("input[name='sendTimeType']:checked").val();
		var sendTimeRemark = $("#sendTimeRemark").val();
		$("#sendTimeType_1").change(function(){
			if($(this).is(":checked")){
				$("#sendTimeRemark").val('');
			}
		});
		$("#sendTimeType_2").change(function(){
			if($(this).is(":checked")){
				$("#sendTimeRemark").val('');
			}
		});
		$("#sendTimeType_3").change(function(){
			if($(this).is(":checked")){
				$("#sendTimeRemark").val('');
			}
		});
		$("#sendTimeType_4").change(function(){
			if($(this).is(":checked")){
				if(sendTimeType == 4){
					$("#sendTimeRemark").val(sendTimeRemark);
				}
			}
		});
		
		
		var sendCodeType = $("input[name='sendCodeType']:checked").val();
		var sendCodeRemark = $("#sendCodeRemark").val();
		$("#sendCodeType_1").change(function(){
			if($(this).is(":checked")){
				$("#sendCodeRemark").val('');
			}
		});
		$("#sendCodeType_2").change(function(){
			if($(this).is(":checked")){
				$("#sendCodeRemark").val('');
			}
		});
		$("#sendCodeType_3").change(function(){
			if($(this).is(":checked")){
				$("#sendCodeRemark").val('');
			}
		});
		$("#sendCodeType_4").change(function(){
			if($(this).is(":checked")){
				if(sendCodeType == 4){
					$("#sendCodeRemark").val(sendCodeRemark);
				}
			}
		});
		
		var sellerType = $("input[name='sellerType']:checked").val();
		var bondedNumberType = $("input[name='bondedNumberType']:checked").val();
		$("#sellerType_1").change(function(){
			if($(this).is(':checked')){
				$("#bondedNumberTypeDiv").css('display','none');
				$("#bondedNumberType_1").prop('checked',true);
				$("#bondedNumberType_2").prop('checked',false);
			}
		});
		$("#sellerType_2").change(function(){
			if($(this).val()!=sellerType){
				$("#bondedNumberType_1").prop('checked',false);
				$("#bondedNumberType_2").prop('checked',false);
			}
			if($(this).is(':checked')){
				$("#bondedNumberTypeDiv").css('display','');
			}
		});
		$("#sellerType_3").change(function(){
			if($(this).val()!=sellerType){
				$("#bondedNumberType_1").prop('checked',false);
				$("#bondedNumberType_2").prop('checked',false);
			}
			if($(this).is(':checked')){
				$("#bondedNumberTypeDiv").css('display','');
			}
		});
		$("#sellerType_4").change(function(){
			if($(this).is(':checked')){
				$("#bondedNumberTypeDiv").css('display','none');
				$("#bondedNumberType_1").prop('checked',true);
				$("#bondedNumberType_2").prop('checked',false);
			}
		});
		
		var settlementPeriod = $("input[name='settlementPeriod']:checked").val();
		var settlementDay = $("#settlementDay").val();
		var settlementOther = $("#settlementOther").val();
		$("#settlementPeriod_1").change(function(){
			if($(this).is(':checked')){
				$("#settlementDay").val('');
				$("#settlementOther").val('');
			}
		});
		$("#settlementPeriod_2").change(function(){
			if($(this).is(':checked')){
				if(settlementPeriod==2){
					$("#settlementDay").val(settlementDay);
				}
				$("#settlementOther").val('');
			}
		});
		$("#settlementPeriod_3").change(function(){
			if($(this).is(':checked')){
				if(settlementPeriod==3){
					$("#settlementOther").val(settlementOther);
				}
				$("#settlementDay").val('');
			}
		});
		
		
		
		$('#copyFromOtherSellerDiv').dialog({
            title:'从其他商家复制',
            collapsible:true,
            closed:true,
            modal:true,
            buttons:[{
                text:'保存信息',
                iconCls:'icon-ok',
                handler:function(){
                 	$.messager.progress();
                 	var sellerId = $('#copyFromOtherSellerForm_sellerId').val();
                 	if(/^[1-9][0-9]*$/.test(sellerId)){
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
            							var length = $("#categoryTab").find("tr").length;
            							if(data.areaCodes.length > 0){
            								$.each(data.areaCodes,function(index,area){
            									var rr = $("#categoryTab").find("tr").first().clone(true);;
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
            							    	
            							    	$("#categoryTab").append(rr);
            								});
            								$("#categoryTab").find("tr:lt("+length+")").remove();
            							}
            							$('#copyFromOtherSellerDiv').dialog('close');
            							$.messager.progress('close');
            						} else if (data.status == 2) {
            							$.messager.alert("提示", "所选商家已经停用，请重新选择", "info");
            							$("#deliverAreaType1").prop("checked",true);
            							$("#deliverAreaType2").prop("checked",false);
            							$("#deliverAreaDesc").val('');
            						}
            					}, 
            					"json");
                 	}else{
                 		$.messager.alert('提示','请输入商家Id','info');
                 	}
                }
            },{
                text:'取消',
                align:'left',
                iconCls:'icon-cancel',
                handler:function(){
                    $('#copyFromOtherSellerDiv').dialog('close');
                }
            }]
        });
		
		$('#receiveProvinceCode').change(function(){
			$child = $('#receiveCityCode');
			var pid = $('#receiveProvinceCode').val();
			var selected_id = 0;
			$('#receiveDistrictCode').empty();
			$.ajax({
	            url:"${rc.contextPath}/order/getAllCity",
	            type:'post',
	            data: {id : pid},
	            dataType: 'json',
	            success:function(data){
	                var options = '<option value="0">--请选择--</option>';
	                $.each(data,function(i){
	                    if(data[i].id == selected_id){
	                        options += '<option value="'+this.cityId+'" selected="selected">'+this.name+'</option>';  
	                    }else{
	                        options += '<option value="'+this.cityId+'" >'+this.name+'</option>'; 
	                    }
	                });
	                $child.empty().append(options);
	            }
	        });
		});
		
		$('#receiveCityCode').change(function(){
			$child = $('#receiveDistrictCode');
			var pid = $('#receiveCityCode').val();
			var selected_id = 0;
			$.ajax({
	            url:"${rc.contextPath}/order/getAllDistrict",
	            type:'post',
	            data: {id : pid},
	            dataType: 'json',
	            success:function(data){
	                var options = '<option value="0">--请选择--</option>';
	                $.each(data,function(i){
	                    if(data[i].id == selected_id){
	                        options += '<option value="'+this.districtId+'" selected="selected">'+this.name+'</option>';  
	                    }else{
	                        options += '<option value="'+this.districtId+'" >'+this.name+'</option>'; 
	                    }
	                });
	                $child.empty().append(options);
	            }
	        });
		});		
		
	});
	
	
	function copyDeliverAreaFromSeller(){
		$('#copyFromOtherSellerForm_sellerId').val('');
		$('#copyFromOtherSellerDiv').dialog('open');
	}
	
    function addImageDetailsRow(obj){
    	var row= $("#imageDetails").find("tr").first().clone();
    	$(row).find("input[name='imageId']").val('0');
    	$(row).find("input[name='image']").val('');
    	$(obj).parent().parent().after(row);
    }
    
    function removeImageDetailsRow(obj){
    	$(obj).parent().parent().remove();
    }
    
    function addEshopAddressRow(obj){
    	var row= $("#eshopAddress").find("tr").first().clone();
    	$(row).find("input[name='shopId']").val('0');
    	$(row).find("input[name='shopURL']").val('');
    	$(obj).parent().parent().after(row);
    }
    
    function removeEshopAddressRow(obj){
    	$(obj).parent().parent().remove();
    }
    
    var $obj;
    function picDialogOpen(obj) {
    	$obj = $(obj).prev();
        $("#picDia").dialog("open");
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
    
    function viewPicture(e){
    	var url = $(e).prev().prev().val();
    	if($.trim(url)==''){
    		$.messager.alert('提示','请上传图片后再查看','info');
    	}else{
    		window.open(url,"_blank");
    	}
    }
</script>

</body>
</html>