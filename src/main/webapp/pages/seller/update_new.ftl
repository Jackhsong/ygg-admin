<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>换吧网络-后台管理</title>
	<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/default/easyui.css" rel="stylesheet" type="text/css" />
	<link href="${rc.contextPath}/pages/js/jquery-easyui-1.4/themes/icon.css" rel="stylesheet" type="text/css" />
	<link href="${rc.contextPath}/pages/js/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.min.js"></script>
	<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/jquery.easyui.min.js"></script>
	<script src="${rc.contextPath}/pages/js/jquery-easyui-1.4/locale/easyui-lang-zh_CN.js"></script>
	<!-- <script src="${rc.contextPath}/pages/js/jquery.min.js"></script> -->
	<script src="${rc.contextPath}/pages/js/bootstrap/js/bootstrap.min.js"></script>
	<script src="${rc.contextPath}/pages/js/jquery.bootstrap.min.js"></script>
	<script src="${rc.contextPath}/pages/js/My97DatePicker/WdatePicker.js"></script>
	<style type="text/css">
		.main{
			margin: 20px 30px;
		}
	</style>
</head>
<body>
  <div class="main">
	  <form class="form-horizontal" id="sellerForm" action="" autocomplete="on" method="post">
	    <fieldset>
	    	<input type="hidden" value="${seller.id?c}" id="sellerId" name="sellerId" />
	    	<input type="hidden" value="${sellerExpand.id?c}" id="expandId" name="expandId" />
	    	<div id="legend" class="">
	        	<legend class="">商家信息</legend>
	      	</div>
	      	
		  	<div class="control-group">
	          <label class="control-label">是否使用商家后台</label>
	          <div class="controls">
	          	<label class="radio inline"><input type="radio" value="1" name="isOwner" id="isOwner_yes"  <#if seller.isOwner == 1>checked</#if>>是</label>
	      		<#if (seller.id == 0) || ((seller.id>0) && (seller.isOwner == 0))><label class="radio inline"><input type="radio" value="0" name="isOwner" id="isOwner_no"  <#if seller.isOwner == 0 && (seller.id>0)>checked</#if>>否</label></#if>
	  		 </div>
	        </div>
	       
	       <div id="div_part_1" <#if (seller.id>0) && (seller.isOwner == 0)>style="display:none"</#if>>
		       <div class="control-group">
		          <label class="control-label" for="username">商家后台帐号</label>
		          <div class="controls">
		            <input type="text" id="username" name="username" placeholder="必填" class="input-xxlarge"   maxlength="16" value="<#if sellerExpand.username?exists>${sellerExpand.username}</#if>">
		          	<span class="text-error">6~16个字符，可使用字母、数字、下划线，需以字母开头</span>
		          </div>
		    	</div>
		
				<#if seller.id==0>
				<div class="control-group">
		          <label class="control-label" for="password">商家后台密码</label>
		          <div class="controls">
		            <input type="password" id="password" name="password" placeholder="必填" class="input-xxlarge" maxlength="16" value="<#if sellerExpand.password?exists>${sellerExpand.password}</#if>">
		          	<span class="text-error">6~16个字符，区分大小写</span>
		          </div>
		        </div>
		        </#if>
		
		    	<div class="control-group">
		          <label class="control-label">商品审核负责人</label>
		          <div class="controls">
		            <select class="input-xxlarge" id="auditUserId" name="auditUserId">
		            	<option value="0">--请选择审核人--</option>
		            	<#list auditUserList as audit>
		            	<option value="${audit.id?c}" <#if sellerExpand.auditUserId?exists && (audit.id == sellerExpand.auditUserId)>selected</#if>>${audit.realname}</option>
		            	</#list>
		      		</select>
		          </div>
		        </div>
	
			    <div class="control-group">
		          <label class="control-label">商家商品全部品牌名</label>
		          <div class="controls">
		          	<input class="input-xxlarge" name="brandId" id="brandId"/>
		          	<input class="input-xlarge" name="helpBrand" id="helpBrand"/>
		          </div>
		        </div>
		        	
		    	<div class="control-group">
		          <label class="control-label">商定结算方式</label>
		          <div class="controls">
		      		<label class="radio inline"><input type="radio" value="1" id="proposalSubmitType1" name="proposalSubmitType" <#if sellerExpand.proposalSubmitType?exists && (sellerExpand.proposalSubmitType==1)>checked</#if>>供货价</label>
		      		<label class="radio inline">
		      			<input type="radio" value="2" id="proposalSubmitType2" name="proposalSubmitType" <#if sellerExpand.proposalSubmitType?exists && (sellerExpand.proposalSubmitType==2)>checked</#if> >
			      		<div class="input-prepend input-append">
			      			<span class="add-on">扣点</span>
			      			<input class="input-mini" name="proposalDeduction" id="proposalDeduction" type="text" maxlength="5" value="<#if sellerExpand.proposalDeduction?exists && (sellerExpand.proposalDeduction>0)>${sellerExpand.proposalDeduction?c}</#if>"/>
			      			<span class="add-on">%</span>
			      		</div>
		      		</label>
		  			</div>
		        </div>
	        </div>
	        
	        <span>基本信息</span>
       	 	<hr/>

            <div class="control-group">
                <label class="control-label">商家类目</label>
                <div class="controls">
                    <input class="input-xxlarge" name="categoryId" id="categoryId"/>
                    <input class="input-xlarge" name="helpCategory" id="helpCategory"/>
                </div>
            </div>

	    	<div class="control-group">
	          <label class="control-label" for="realSellerName">真实商家名称</label>
	          <div class="controls">
	            <input type="text" id="realSellerName" name="realSellerName" placeholder="必填" class="input-xxlarge" maxlength="20" value="<#if seller.realSellerName?exists>${seller.realSellerName}</#if>" <#if seller.id?exists && (seller.id != 0)>disabled="disabled"</#if>>
	            <span class="text-error">真实商家名称保存后不可修改</span>
	          </div>
	        </div>
	
	    	<div class="control-group">
	          <label class="control-label" for="sellerName">前台展示名称</label>
	          <div class="controls">
	            <input type="text" id="sellerName" name="sellerName" placeholder="必填" class="input-xxlarge" maxlength="20" value="<#if seller.sellerName?exists>${seller.sellerName}</#if>">
	          </div>
	        </div>
	        
	        <div id="div_part_2">
		    	<div class="control-group">
		          <label class="control-label" for="companyName">公司名称</label>
		          <div class="controls">
		            <input type="text" id="companyName" name="companyName" placeholder="必填" class="input-xxlarge" maxlength="30" value="<#if seller.companyName?exists>${seller.companyName}</#if>">
		          </div>
		        </div>
		        
		    	<div class="control-group">
		          <label class="control-label" for="shopURL">店铺网址</label>
		          <div class="controls">
		          <#if (storeList?size>0)>
					<#list storeList as shop>
		          	<p>
		            	<input type="text" name="${shop.url}" placeholder="选填" class="input-xxlarge" value="">
			            <span class="help-inline">
					     	<a href="#" class="btn btn-primary" onclick="addShop(this)">添加</a>
					     	<a href="#" class="btn btn-primary" onclick="delShop(this)">删除</a>
					     </span>
					     <br/>
		          	</p>
		          	</#list>
					<#else>
		          	<p>
		            	<input type="text" name="shopURL" placeholder="选填" class="input-xxlarge" value="">
			            <span class="help-inline">
					     	<a href="#" class="btn btn-primary" onclick="addShop(this)">添加</a>
					     	<a href="#" class="btn btn-primary" onclick="delShop(this)">删除</a>
					     </span>
					     <br/>
		          	</p>
		          	</#if>
		          </div>
		        </div>
	        </div>
	
	        
	        <div class="control-group">
	          <label class="control-label">结算周期</label>
	          <div class="controls">
	          	<input type="hidden" name="settlementPeriod" value="2"/>
	            <div class="input-prepend input-append">
	              <span class="add-on">活动结束后</span>
	              <input class="input-mini" placeholder="必填" id="settlementDay" name="settlementDay" type="text"   maxlength="5" value="<#if seller.settlementDay?exists && (seller.settlementDay >0)>${seller.settlementDay?c}</#if>">
	              <span class="add-on">天结算</span>
	            </div>
	          </div>
	        </div>
	        
	        <div class="control-group">
	          <label class="control-label">发货依据</label>
	          <div class="controls">
		          <label class="radio inline"><input type="radio" value="1" name="sendCodeType" <#if seller.sendCodeType?exists && (seller.sendCodeType<=1)>checked</#if>>商品编码</label>
		          <label class="radio inline"><input type="radio" value="2" name="sendCodeType" <#if seller.sendCodeType?exists && (seller.sendCodeType==2)>checked</#if>>商品条码</label>
		          <label class="radio inline"><input type="radio" value="3" name="sendCodeType" <#if seller.sendCodeType?exists && (seller.sendCodeType==3)>checked</#if>>商品名称</label>
	  		  </div>
	        </div>
	
	    	<div class="control-group">
	          <label class="control-label">发货类型</label>
	          <div class="controls">
		          <select class="input-xlarge" id="sellerType" name="sellerType">
	      	  			<option value="1" <#if seller.sellerType?exists && (seller.sellerType==1)>selected</#if>>国内发货</option>
	      	  			<option value="2" <#if seller.sellerType?exists && (seller.sellerType==2)>selected</#if>>保税区发货</option>
	      	  			<option value="3" <#if seller.sellerType?exists && (seller.sellerType==3)>selected</#if>>香港发货(仅身份证号)</option>
	      	  			<option value="4" <#if seller.sellerType?exists && (seller.sellerType==4)>selected</#if>>美国</option>
						<option value="5" <#if seller.sellerType?exists && (seller.sellerType==5)>selected</#if>>日本</option>
						<option value="6" <#if seller.sellerType?exists && (seller.sellerType==6)>selected</#if>>澳大利亚</option>
						<option value="7" <#if seller.sellerType?exists && (seller.sellerType==7)>selected</#if>>德国</option>
						<option value="8" <#if seller.sellerType?exists && (seller.sellerType==8)>selected</#if>>荷兰</option>
						<option value="9" <#if seller.sellerType?exists && (seller.sellerType==9)>selected</#if>>台湾</option>
						<option value="10" <#if seller.sellerType?exists && (seller.sellerType==10)>selected</#if>>加拿大</option>
		      	  		<option value="11" <#if seller.sellerType?exists && (seller.sellerType == 11) >selected</#if>>新西兰</option>
		      	  		<option value="12" <#if seller.sellerType?exists && (seller.sellerType == 12) >selected</#if>>韩国</option>
		      	  </select>
	  		  </div>
	        </div>
	
			<div id="div_part_4" <#if seller.sellerType?exists && (seller.sellerType > 1) >style="display: ''"<#else>style="display: none"</#if>>
		    	<div class="control-group">
		          <label class="control-label">报关顺序</label>
		          <div class="controls">
			          <label class="radio inline"><input type="radio" value="1"  id="bondedNumberType1" name="bondedNumberType" <#if seller.bondedNumberType?exists && (seller.bondedNumberType <=1) >checked</#if>>先有物流单号后报关</label>
			          <label class="radio inline"><input type="radio" value="2"  id="bondedNumberType2" name="bondedNumberType" <#if seller.bondedNumberType?exists && (seller.bondedNumberType ==2) >checked</#if>>先报关后有物流单号</label>
		          </div>
		        </div>
	        </div>
	
	    	<div class="control-group">
	          <label class="control-label" for="sendAddress">发货地</label>
	          <div class="controls">
	            <input type="text" id="sendAddress" name="sendAddress" placeholder="必填" maxlength="10"  class="input-xlarge" value="<#if seller.sendAddress?exists>${seller.sendAddress}</#if>"" >
	          </div>
	        </div>
	        
	    	<div class="control-group">
	          <label class="control-label" for="warehouse">分仓</label>
	          <div class="controls">
	            <input type="text" id="warehouse" name="warehouse" placeholder="选填" maxlength="20" class="input-xlarge" value="<#if seller.warehouse?exists>${seller.warehouse}</#if>">
	          </div>
	        </div>
	
	    	<div class="control-group">
	          <label class="control-label">运费结算方式</label>
	          <div class="controls">
	          	<label class="radio inline"><input type="radio" value="1" id="freightType1" name="freightType" <#if seller.freightType?exists && (seller.freightType<=1)>checked</#if>>全部包邮</label>
	      		<label class="radio inline">
	      			<input type="radio" value="2" id="freightType2" name="freightType" <#if seller.freightType?exists && (seller.freightType==2)>checked</#if>>
	      			<div class="input-prepend input-append">
	      				<span class="add-on">单笔订单满</span>
	      				<input class="input-mini" id="freightMoney" name="freightMoney" type="text" value="<#if seller.freightMoney?exists && (seller.freightMoney !=0)>${seller.freightMoney}</#if>"/>
	      				<span class="add-on">包邮</span>
	      			</div>
	      		</label>
	      		<label class="radio inline"><input type="radio" value="3" id="freightType3" name="freightType" <#if seller.freightType?exists && (seller.freightType==3)>checked</#if>>全部不包邮</label>
	  		  </div>
	        </div>

            <div class="control-group">
                <label class="control-label">保证金状态</label>
                <div class="controls">
                <label class="radio inline"><input type="radio" value="0" id="depositUnSign" name="depositStatus" <#if sellerExpand.depositStatus?exists && (sellerExpand.depositStatus==0)>checked</#if>>未签协议</label>
                <label class="radio inline"><input type="radio" value="1" id="depositSignNoPay" name="depositStatus" <#if sellerExpand.depositStatus?exists && (sellerExpand.depositStatus==1)>checked</#if>>已签未缴纳</label>
                <label class="radio inline">
                    <input type="radio" value="2" id="depositSignPay" name="depositStatus" <#if sellerExpand.depositStatus?exists && (sellerExpand.depositStatus==2)>checked</#if>>
                    <div class="input-prepend input-append">
                        <span class="add-on">已缴纳</span>
                        <input class="input-mini" id="depositCount" name="depositCount" style="width:80px" type="number" value="<#if sellerExpand.depositCount?exists && (sellerExpand.depositCount !=0)>${sellerExpand.depositCount}</#if>"/>
                        <span class="add-on">金额(元)</span>
                    </div>
                </label>
				</div>
			</div>
	
			<div id="div_part_3">
				<span>发货信息</span>
	       	 	<hr/>
	       	 	
		    	<div class="control-group">
		          <label class="control-label">默认快递</label>
		          <div class="controls">
		      		<label class="checkbox inline"><input type="checkbox" name="kuaidi" value="顺丰" <#if seller.kuaidi?exists && (seller.kuaidi?contains("顺丰"))>checked</#if>/>顺丰</label>
		      		<label class="checkbox inline"><input type="checkbox" name="kuaidi" value="申通" <#if seller.kuaidi?exists && (seller.kuaidi?contains("申通"))>checked</#if>/>申通</label>
		      		<label class="checkbox inline"><input type="checkbox" name="kuaidi" value="圆通" <#if seller.kuaidi?exists && (seller.kuaidi?contains("圆通"))>checked</#if>/>圆通</label>
		      		<label class="checkbox inline"><input type="checkbox" name="kuaidi" value="中通" <#if seller.kuaidi?exists && (seller.kuaidi?contains("中通"))>checked</#if>/>中通</label>
		      		<label class="checkbox inline"><input type="checkbox" name="kuaidi" value="韵达" <#if seller.kuaidi?exists && (seller.kuaidi?contains("韵达"))>checked</#if>/>韵达</label>
		      		<label class="checkbox inline"><input type="checkbox" name="kuaidi" value="EMS" <#if seller.kuaidi?exists && (seller.kuaidi?contains("EMS"))>checked</#if>/>EMS</label>
		      		<label class="checkbox inline"><input type="checkbox" name="kuaidi" value="天天快递" <#if seller.kuaidi?exists && (seller.kuaidi?contains("天天快递"))>checked</#if>/>天天快递</label>
		      		<label class="checkbox inline">
		      			<input type="checkbox" name="kuaidi" value="other" <#if seller.otherKuaidi?? && (seller.otherKuaidi !="")>checked</#if>>
		      			<div class="input-prepend">
		      				<span class="add-on">其他</span>
		      				<input class="input-xlarge" id="otherKuaidi" name="otherKuaidi" type="text" placeholder="多个用,号隔开" value="<#if seller.otherKuaidi?exists>${seller.otherKuaidi}</#if>" maxlength="30"/>
		      			</div>
		      		</label>
		  		 </div>
		        </div>
		
		    	<!-- <div class="control-group">
		          <label class="control-label">运费结算</label>
		          <div class="controls">
		          	<a href="#" class="btn btn-primary">管理运费结算模板</a>
		          </div>
		        </div> -->
		
		    	<div class="control-group">
		          <label class="control-label">发货时效说明</label>
		          <div class="controls">
		          	<label class="radio"><input type="radio" value="1" name="sendTimeType" <#if seller.sendTimeType?exists && (seller.sendTimeType<=1)>checked</#if>>当天15点前订单当天24点前打包并提供物流单号，24小时内有物流信息</label>
		      		<label class="radio"><input type="radio" value="2" name="sendTimeType" <#if seller.sendTimeType?exists && (seller.sendTimeType==2)>checked</#if>>单笔订单24小时发货</label>
		      		<label class="radio"><input type="radio" value="3" name="sendTimeType" <#if seller.sendTimeType?exists && (seller.sendTimeType==3)>checked</#if>>单笔订单48小时发货</label>
		      		<label class="radio"><input type="radio" value="4" name="sendTimeType" <#if seller.sendTimeType?exists && (seller.sendTimeType==4)>checked</#if>>单笔订单72小时发货</label>
				 </div>
		        </div>
	        	
		    	<div class="control-group">
		          <label class="control-label">周末是否发货</label>
		          <div class="controls">
		      		<label class="radio inline"><input type="radio" value="1" name="isSendWeekend" <#if seller.isSendWeekend?exists && (seller.isSendWeekend<=1)>checked</#if>>周六、日均不发货</label>
		      		<label class="radio inline"><input type="radio" value="2" name="isSendWeekend" <#if seller.isSendWeekend?exists && (seller.isSendWeekend==2)>checked</#if>>仅周六发货</label>
		      		<label class="radio inline"><input type="radio" value="3" name="isSendWeekend" <#if seller.isSendWeekend?exists && (seller.isSendWeekend==3)>checked</#if>>仅周日发货</label>
		          	<label class="radio inline"><input type="radio" value="4" name="isSendWeekend" <#if seller.isSendWeekend?exists && (seller.isSendWeekend==4)>checked</#if>>周六、日均发货</label>
		  		 </div>
		        </div>
				
				<#if (seller.id>0)>
		    	<div class="control-group">
		          <label class="control-label">发货地区限制</label>
		          <div class="controls">
		      		<a href="${rc.contextPath}/sellerDeliverArea/sellerDeliverAreaTemplateList?sellerId=${seller.id?c}" target="_blank" class="btn btn-primary">管理配送地区模板</a>
		          </div>
		        </div>
		        </#if>
		
		    	<div class="control-group">
		          <label class="control-label" for="holidayTips">假期发货提示</label>
		          <div class="controls">
		            <input type="text" id="holidayTips" name="holidayTips" placeholder="若无假期临近可不填" class="input-xxlarge" maxlength="100" value="<#if seller.holidayTips?exists>${seller.holidayTips}</#if>">
		            <span class="text-error">假期发货可填写：国庆节期间正常发货；假期不发货可填写：国庆节期间不发货，统一在节后发出</span>
		          </div>
		        </div>
		
		    	<div class="control-group">
		          <label class="control-label" for="holidayStartTime">假期开始时间</label>
		          <div class="controls">
		            <input type="datetime" id="holidayStartTime" name="holidayStartTime" placeholder="若无假期临近可不填" class="input-xxlarge" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'holidayEndTime\')}'})" value="<#if seller.holidayStartTime?exists>${seller.holidayStartTime}</#if>">
		            <span class="text-error">可填写：2015-10-01 00:00:00</span>
		          </div>
		        </div>
		
		    	<div class="control-group">
		          <label class="control-label" for="holidayEndTime">假期结束时间</label>
		          <div class="controls">
		            <input type="datetime" id="holidayEndTime" name="holidayEndTime" placeholder="若无假期临近可不填" class="input-xxlarge" onClick="WdatePicker({dateFmt: 'yyyy-MM-dd 23:59:59',minDate:'#F{$dp.$D(\'holidayStartTime\')}'})" value="<#if seller.holidayEndTime?exists>${seller.holidayEndTime}</#if>">
		            <span class="text-error">可填写：2015-10-01 00:00:00</span>
		          </div>
		        </div>
		        
		        <span>联系人信息</span>
		        <hr/>
		        <div class="control-group">
		        	<label class="control-label">发货联系人</label>
	          		<div class="controls">
	          			<div class="input-prepend">
	              			<span class="add-on">姓名</span>
	              			<input class="input-medium" id="fhContactPerson"  name="fhContactPerson" placeholder="发货联系人姓名，必填" type="text" value="<#if seller.fhContactPerson?exists>${seller.fhContactPerson}</#if>">
	            		</div>
	
	          			<div class="input-prepend">
	              			<span class="add-on">手机</span>
	              			<input class="input-medium" id="fhContactMobile" name="fhContactMobile" placeholder="发货联系人手机，必填" type="tel" value="<#if seller.fhContactMobile?exists>${seller.fhContactMobile}</#if>" >
	            		</div>
	            		
	          			<div class="input-prepend">
	              			<span class="add-on">QQ</span>
	              			<input class="input-medium" id="fhqq" name="fhqq" placeholder="发货联系人QQ，必填" type="number" value="<#if seller.fhqq?exists>${seller.fhqq}</#if>">
	            		</div>
	
	          			<div class="input-prepend">
	              			<span class="add-on">邮箱</span>
	              			<input class="input-medium" id="fhEmail" name="fhEmail" placeholder="发货联系人邮箱，必填" type="email" value="<#if seller.fhEmail?exists>${seller.fhEmail}</#if>">
	            		</div>
	          		</div>
	       	 	</div>
	       	 	
		        <div class="control-group">
		        	<label class="control-label">售后联系人</label>
	          		<div class="controls">
	          			<div class="input-prepend">
	              			<span class="add-on">姓名</span>
	              			<input class="input-medium" id="shContactPerson" name="shContactPerson" placeholder="售后联系人姓名，必填" type="text" value="<#if seller.shContactPerson?exists>${seller.shContactPerson}</#if>">
	            		</div>
	
	          			<div class="input-prepend">
	              			<span class="add-on">手机</span>
	              			<input class="input-medium" id="shContactMobile" name="shContactMobile" placeholder="售后联系人手机，必填" type="tel" value="<#if seller.shContactMobile?exists>${seller.shContactMobile}</#if>">
	            		</div>
	            		
	            		<div class="input-prepend">
	              			<span class="add-on">QQ</span>
	              			<input class="input-medium" id="shqq" name="shqq" placeholder="售后联系人QQ，必填" type="number" value="<#if seller.shqq?exists>${seller.shqq}</#if>">
	            		</div>
	
	          			<div class="input-prepend">
	              			<span class="add-on">邮箱</span>
	              			<input class="input-medium" id="shEmail" name="shEmail" placeholder="售后联系人邮箱，必填" type="email" value="<#if seller.shEmail?exists>${seller.shEmail}</#if>">
	            		</div>
	            		
	          			<div class="input-prepend">
	              			<span class="add-on">旺旺</span>
	              			<input class="input-medium" id="shAliwang" name="shAliwang" placeholder="售后联系人旺旺号，选填" type="text" value="<#if seller.shAliwang?exists>${seller.shAliwang}</#if>">
	            		</div>
	            		
	            		<span class="btn btn-primary" onclick="copyInfo(1)">与发货联系人一致</span>
	          		</div>
	       	 	</div>
	       	 	
		        <div class="control-group">
		        	<label class="control-label">运营对接人</label>
	          		<div class="controls">
	          			<div class="input-prepend">
	              			<span class="add-on">姓名</span>
	              			<input class="input-medium" id="rcContactPerson" name="rcContactPerson" placeholder="运营对接人姓名，必填" type="text" value="<#if sellerExpand.rcContactPerson?exists>${sellerExpand.rcContactPerson}</#if>">
	            		</div>
	
	          			<div class="input-prepend">
	              			<span class="add-on">手机</span>
	              			<input class="input-medium" id="rcContactMobile" name="rcContactMobile" placeholder="运营对接人手机，必填" type="text" value="<#if sellerExpand.rcContactMobile?exists>${sellerExpand.rcContactMobile}</#if>">
	            		</div>
	            		
	            		<div class="input-prepend">
	              			<span class="add-on">QQ</span>
	              			<input class="input-medium" id="rcqq" name="rcqq" placeholder="运营对接人QQ，必填" type="number" value="<#if sellerExpand.rcqq?exists>${sellerExpand.rcqq}</#if>">
	            		</div>
	
	          			<div class="input-prepend">
	              			<span class="add-on">邮箱</span>
	              			<input class="input-medium" id="rcEmail" name="rcEmail" placeholder="运营对接人邮箱，必填" type="email" value="<#if sellerExpand.rcEmail?exists>${sellerExpand.rcEmail}</#if>">
	            		</div>
	            		
	          			<div class="input-prepend">
	              			<span class="add-on">旺旺</span>
	              			<input class="input-medium" id="rcAliwang" name="rcAliwang" placeholder="运营对接人旺旺号，选填" type="text" value="<#if sellerExpand.rcAliwang?exists>${sellerExpand.rcAliwang}</#if>">
	            		</div>
	            		
	            		<span class="btn btn-primary" onclick="copyInfo(2)">与发货联系人一致</span>
	            		<span class="btn btn-primary" onclick="copyInfo(3)">与售后联系人一致</span>
	          		</div>
	       	 	</div>
	       	 	
		        <div class="control-group">
		        	<label class="control-label">结算联系人</label>
	          		<div class="controls">
	          			<div class="input-prepend">
	              			<span class="add-on">姓名</span>
	              			<input class="input-medium" id="jsContactPerson" name="jsContactPerson" placeholder="结算联系人姓名，必填" type="text" value="<#if seller.jsContactPerson?exists>${seller.jsContactPerson}</#if>">
	            		</div>
	
	          			<div class="input-prepend">
	              			<span class="add-on">手机</span>
	              			<input class="input-medium" id="jsContactMobile" name="jsContactMobile" placeholder="结算联系人手机，必填" type="tel" value="<#if seller.jsContactMobile?exists>${seller.jsContactMobile}</#if>">
	            		</div>
	            		
	            		<div class="input-prepend">
	              			<span class="add-on">QQ</span>
	              			<input class="input-medium" id="jsqq" name="jsqq" placeholder="结算联系人QQ，必填" type="number" value="<#if seller.jsqq?exists>${seller.jsqq}</#if>">
	            		</div>
	
	          			<div class="input-prepend">
	              			<span class="add-on">邮箱</span>
	              			<input class="input-medium" id="jsEmail" name="jsEmail" placeholder="结算联系人邮箱，必填" type="email" value="<#if seller.jsEmail?exists>${seller.jsEmail}</#if>">
	            		</div>
	            		
	          			<div class="input-prepend">
	              			<span class="add-on">旺旺</span>
	              			<input class="input-medium" id="jsAliwang" name="jsAliwang" placeholder="结算联系人旺旺号，选填" type="text" value="<#if seller.jsAliwang?exists>${seller.jsAliwang}</#if>">
	            		</div>
	            		
	            		<span class="btn btn-primary" onclick="copyInfo(4)">与发货联系人一致</span>
	            		<span class="btn btn-primary" onclick="copyInfo(5)">与售后联系人一致</span>
	            		<span class="btn btn-primary" onclick="copyInfo(6)">与运营对接人一致</span>
	          		</div>
	       	 	</div>
	       	 	
	       	 	<span>退货收件信息</span>
	       	 	<hr/>
	
		        <div class="control-group">
		        	<label class="control-label">省市区</label>
	          		<div class="controls">
	          			<select class="input-xlarge" id="receiveProvinceCode" name="receiveProvinceCode" >
	          				<option value="0">--请选择省份--</option>
							<#list provinceList as bl >
							<option value="${bl.provinceId?c}" <#if sellerExpand.receiveProvinceCode?exists && (sellerExpand.receiveProvinceCode == bl.provinceId)>selected</#if> >${bl.name}</option>
							</#list>
	          			</select>
	          			<select class="input-xlarge" id="receiveCityCode" name="receiveCityCode">
	          				<option value="0">--请选择市--</option>
	          				<#list cityList as bl >
			 				<option value="${bl.cityId?c}" <#if sellerExpand.receiveCityCode?exists && (sellerExpand.receiveCityCode == bl.cityId)>selected</#if> >${bl.name}</option>
			 				</#list>
	          			</select>
	          			<select class="input-xlarge" id="receiveDistrictCode" name="receiveDistrictCode">
	          				<option value="0">--请选择区--</option>
	          				<#list districtList as bl >
			 				<option value="${bl.districtId?c}" <#if sellerExpand.receiveDistrictCode?exists && (sellerExpand.receiveDistrictCode == bl.districtId)>selected</#if> >${bl.name}</option>
			 				</#list>
	          			</select>
	          		</div>
	       	 	</div>
	       	 	
	       	 	<div class="control-group">
	       	 		<label class="control-label">详细地址</label>
	       	 		<div class="controls">
	       	 			<input class="input-xxlarge" id="receiveDetailAddress" name="receiveDetailAddress" placeholder="必填" type="text"  maxlength="100" value="<#if sellerExpand.receiveDetailAddress?exists>${sellerExpand.receiveDetailAddress}</#if>"/>
	       	 		</div>
	       	 	</div>
	       	 	    	 	
	       	 	<div class="control-group">
	       	 		<label class="control-label">收件人</label>
	       	 		<div class="controls">
	       	 			<input class="input-xxlarge" id="receivePerson" name="receivePerson" placeholder="必填" type="text"  maxlength="15" value="<#if sellerExpand.receivePerson?exists>${sellerExpand.receivePerson}</#if>"/>
	       	 		</div>
	       	 	</div>
	       	 	 	 	
	       	 	<div class="control-group">
	       	 		<label class="control-label">联系电话</label>
	       	 		<div class="controls">
	       	 			<input class="input-xxlarge" id="receiveTelephone" name="receiveTelephone" placeholder="必填" type="tel"  maxlength="15" value="<#if sellerExpand.receiveTelephone?exists>${sellerExpand.receiveTelephone}</#if>"/>
	       	 		</div>
	       	 	</div>
			</div>
			
       	 	<hr/>
       	 	<div class="control-group">
       	 		<label class="control-label">招商负责人</label>
       	 		<div class="controls">
       	 			<input class="input-xlarge" id="responsibilityPerson" name="responsibilityPerson" placeholder="必填" type="text"  maxlength="15" value="<#if seller.responsibilityPerson?exists>${seller.responsibilityPerson}</#if>"/>
       	 		</div>
       	 	</div>
       	 	
       	 	<div class="control-group">
	          <label class="control-label">商家状态</label>
	          <div class="controls">
	          	<label class="radio inline"><input type="radio" value="1" name="isAvailable" <#if seller.isAvailable?exists && (seller.isAvailable == 1) >checked</#if>>是</label>
	      		<label class="radio inline"><input type="radio" value="0" name="isAvailable" <#if seller.isAvailable?exists && (seller.isAvailable == 0) >checked</#if>>否</label>
	  		  </div>
	        </div>
			
			<div class="control-group">
				<div class="controls">
					<a href="#" id="saveButton" class="btn btn-large btn-primary">保存</a>
	  		  	</div>
			</div>
			
	    </fieldset>
	  </form>
  </div> 
</body>

<script type="text/javascript">

	var mobileReg = /^[1][3,4,5,7,8][0-9]{9}$/;
	var qqReg = /^[1-9][0-9]{3,10}$/;
	var emailReg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
	var intReg = /^[1-9][0-9]*$/ ;
	var floatReg = /^\d+(\.\d+)?$/ ;
	var usernameReg = /^[a-zA-Z]{1}([a-zA-Z0-9]|[_]){5,15}$/;
	var passwordReg = /^\w{6,16}$/;

	var sellerId = $("#sellerId").val();
	$(document).ready(function(){
		
		$("#isOwner_yes").change(function(){
		    if($(this).is(':checked')){
		    	$("#div_part_1").show();
		    	if(sellerId == 0){
			    	$("#div_part_2").hide();
			    	$("#div_part_3").hide();
		    	}
		    }
	  	});
		
		$("#isOwner_no").change(function(){
			  if($(this).is(':checked')){
				  $("#div_part_1").hide();
				  $("#div_part_2").show();
				  $("#div_part_3").show();
			  }
		});
		
		<#if seller.sellerType?exists && (seller.sellerType > 1) >
			$("#div_part_4").show();
		<#else>
			$("#div_part_4").hide();
		</#if>
		
		
		$("#sellerType").change(function(){
			var sellerType = $(this).val();
			if(sellerType > 1){
				$("#bondedNumberType1").prop('checked',false);
				$("#bondedNumberType2").prop('checked',false);
				$("#div_part_4").show();
			}else{
				$("#bondedNumberType1").prop('checked',true);
				$("#bondedNumberType2").prop('checked',false);
				$("#div_part_4").hide();
			}
		});
		
		var freightType = $("input[name='freightType']:checked").val();
		var freightMoney = $("#freightMoney").val();
		$("#freightType1").change(function(){
			if($(this).is(":checked")){
				$("#freightMoney").val('');
			}
		});
		$("#freightType2").change(function(){
			if($(this).is(":checked")){
				$("#freightMoney").val(freightMoney);
				if(freightType==2){
					$("#freightMoney").val(freightMoney);
				}
			}
		});
		$("#freightType3").change(function(){
			if($(this).is(":checked")){
				$("#freightMoney").val('');
			}
		});

        var depositStatus = $("input[name='depositStatus']:checked").val();
        var depositCount = $("#depositCount").val();
        $("#depositUnSign").change(function(){
            if($(this).is(":checked")){
                $("#depositCount").val('');
            }
        });
        $("#depositSignNoPay").change(function(){
            if($(this).is(":checked")){
                $("#depositCount").val('');
            }
        });
        $("#depositSignPay").change(function(){
            if($(this).is(":checked")){
                $("#depositCount").val(depositCount);
            }
        });
		
		var proposalSubmitType = $("input[name='proposalSubmitType']:checked").val();
		var proposalDeduction = $("#proposalDeduction").val();
		$("#proposalSubmitType1").change(function(){
			if($(this).is(":checked")){
				$("#proposalDeduction").val('');
			}
		});
		$("#proposalSubmitType2").change(function(){
			if($(this).is(":checked")){
				$("#proposalDeduction").val(proposalDeduction);
				if(proposalSubmitType==2){
					$("#proposalDeduction").val(proposalSubmitType);
				}
			}
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
	                var options = '<option value="0">--请选择省份--</option>';
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
	                var options = '<option value="0">--请选择市--</option>';
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
		
		
		//表单提交
		$("#saveButton").click(function(){
			if(checkFormData()){
				var sellerId = $("#sellerId").val();
				var realSellerName = $("#realSellerName").val();
				//新增商家时，验证商家名称
				if(sellerId == 0){
					$.ajax({
						url:'${rc.contextPath}/seller/checkName',
						type:'post',
						data:{'realSellerName':realSellerName,'sellerId':sellerId},
						dataType:'json',
						success:function(data){
							if(data.status == 1){
								$.messager.confirm("提示", data.msg, function(){ 
									submitForm();  
								});
							}else{
								submitForm();
							}
						},
						error:function(xhr){
							$.messager.progress('close');
				            $.messager.alert("提示",'服务器忙，请稍后再试，errorCode='+xhr.status,"info");
						}
					});
				}else{
					//修改时不需要验证商家名称
					submitForm();
				}
			}else{
				return false;
			}
		});
		
		$("#brandId").combobox({
			url: '${rc.contextPath}/brand/jsonBrandCode?isAvailable=1',
			valueField: 'code',
			textField: 'text',
			multiple:true,
			editable:false,
			onLoadSuccess:function(para){
				var selectBrand = [];
				$.each(${sellerBrandList},function(index,brand){
					selectBrand.push(brand.id);
				});
				$("#brandId").combobox('setValues',selectBrand);
			}
		});
		
		$("#helpBrand").combobox({
			url: '${rc.contextPath}/brand/jsonBrandCode?isAvailable=1',
			valueField: 'code',
			textField: 'text',
			onSelect:function(brand){
				if(brand != '' && brand != null && brand != undefined){
					var selectValue = $("#brandId").combobox('getValues');
					var id = brand.code;
					selectValue.push(id);
					$("#brandId").combobox('setValues',selectValue);
					$("#helpBrand").combobox('clear');
				}
			}
		});

        $("#categoryId").combobox({
            url: '${rc.contextPath}/category/jsonCategoryFirstCode?isAvailable=1&zeroNeed=0',
            valueField: 'code',
            textField: 'text',
            multiple:true,
            editable:false,
            onLoadSuccess:function(para){
				var selectCategories = [];
				$.each(${sellerCategoryList},function(index, category){
					selectCategories.push(category.id);
				});
				$("#categoryId").combobox('setValues',selectCategories);
            }
        });

        $("#helpCategory").combobox({
            url: '${rc.contextPath}/category/jsonCategoryFirstCode?isAvailable=1',
            valueField: 'code',
            textField: 'text',
            onSelect:function(category){
                if(category != '' && category != null && category != undefined){
                    var selectValue = $("#categoryId").combobox('getValues');
                    var id = category.code;
                    selectValue.push(id);
                    $("#categoryId").combobox('setValues',selectValue);
                    $("#helpCategory").combobox('clear');
                }
            }
        });
	});
	
	function submitForm(){
		$.ajax({
			url:'${rc.contextPath}/seller/save',
			type:'post',
			data:$("#sellerForm").serialize(),
			dataType:'json',
			success:function(data){
				if(data.status == 1){
					$.messager.model = { 
				        ok:{ text: "回到商家列表", classed: 'btn-default' },
				        cancel: { text: "留在当前页面", classed: 'btn-error' }
					};
					$.messager.confirm("提示","保存成功!",function(){
						window.location.href = '${rc.contextPath}/seller/list/1';
					});
				}else{
					$.messager.alert("提示",data.msg);
				}
			},
			error:function(xhr){
				$.messager.progress('close');
	            $.messager.alert("提示",'服务器忙，请稍后再试，errorCode='+xhr.status,"info");
			}
		});
	}
	
	
	function addShop(element){
		var parentNode = $(element).parent().parent().parent();
		var childNode = $(parentNode).find("p").first().clone(true);
		$(childNode).find("input").eq(0).val("");
		$(parentNode).find("p").last().after(childNode);
	}
	
	function delShop(element){
		$(element).parent().parent().parent().find("p").last().remove();
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
		//从发货联系人复制到运营对接人
		if(type===2){
			$("#rcContactPerson").val($("#fhContactPerson").val());
			$("#rcContactMobile").val($("#fhContactMobile").val());
			$("#rcqq").val($("#fhqq").val());
			$("#rcEmail").val($("#fhEmail").val());
		}
		//从售后联系人复制到运营对接人
		if(type===3){
			$("#rcContactPerson").val($("#shContactPerson").val());
			$("#rcContactMobile").val($("#shContactMobile").val());
			$("#rcqq").val($("#shqq").val());
			$("#rcEmail").val($("#shEmail").val());
			$("#rcAliwang").val($("#shAliwang").val());
		}
		//从发货联系人复制到结算联系人
		if(type===4){
			$("#jsContactPerson").val($("#fhContactPerson").val());
			$("#jsContactMobile").val($("#fhContactMobile").val());
			$("#jsqq").val($("#fhqq").val());
			$("#jsEmail").val($("#fhEmail").val());
			/* $("#jsAliwang").val($("#fhAliwang").val());
			$("#jsRemark").val($("#fhRemark").val()); */
		}
		//从售后联系人复制到结算联系人
		if(type===5){
			$("#jsContactPerson").val($("#shContactPerson").val());
			$("#jsContactMobile").val($("#shContactMobile").val());
			$("#jsqq").val($("#shqq").val());
			$("#jsEmail").val($("#shEmail").val());
			$("#jsAliwang").val($("#shAliwang").val());
		}
		//从运营对接人复制到结算联系人
		if(type===6){
			$("#jsContactPerson").val($("#rcContactPerson").val());
			$("#jsContactMobile").val($("#rcContactMobile").val());
			$("#jsqq").val($("#rcqq").val());
			$("#jsEmail").val($("#rcEmail").val());
			$("#jsAliwang").val($("#rcAliwang").val());
		}
	}
	
	function checkFormData(){
		var sellerId = $("#sellerId").val();
		var isOwner = $("input[name='isOwner']:checked").val();//是否使用商家后台
		var username = $.trim($("#username").val());//登录名
		var password = $.trim($("#password").val());//密码
		var auditUserId = $("#auditUserId").val();//审核人
		var brandIds = $("#brandId").combobox('getValues');//品牌
		var brandId = [];
		$.each(brandIds,function(index,value){
			if(value != '' && value != null && value != undefined){
				brandId.push(value);
			}
		});
		var categoryIds = $('#categoryId').combobox('getValues'); // 商家类别
		var categoryId = [];
        $.each(categoryIds,function(index,value){
            if(value != '' && value != null && value != undefined){
                categoryId.push(value);
            }
        });
		var proposalSubmitType = $("input[name='proposalSubmitType']:checked").val();//商定结算方式
		var proposalDeduction = $.trim($("#proposalDeduction").val());//扣点
		
		var companyName = $.trim($("#companyName").val());//公司名称
		var isSendWeekend = $("input[name='isSendWeekend']:checked").val();//周末是否发货
		var sendTimeType = $("input[name='sendTimeType']:checked").val();//发货时效说明
		var holidayTips = $.trim($("#holidayTips").val());
		var holidayStartTime = $.trim($("#holidayStartTime").val());
		var holidayEndTime = $.trim($("#holidayEndTime").val());

		
		//发货联系人信息
		var fhContactPerson = $.trim($("#fhContactPerson").val());
		var fhContactMobile = $.trim($("#fhContactMobile").val());
		var fhqq = $.trim($("#fhqq").val());
		var fhEmail = $.trim($("#fhEmail").val());
		
		//售后联系人信息
		var shContactPerson = $.trim($("#shContactPerson").val());
		var shContactMobile = $.trim($("#shContactMobile").val());
		var shqq = $.trim($("#shqq").val());
		var shEmail = $.trim($("#shEmail").val());
		
		//运营对接人信息
		var rcContactPerson = $.trim($("#rcContactPerson").val());
		var rcContactMobile = $.trim($("#rcContactMobile").val());
		var rcqq = $.trim($("#rcqq").val());
		var rcEmail = $.trim($("#rcEmail").val());
		
		//结算联系人信息
		var jsContactPerson = $.trim($("#jsContactPerson").val());
		var jsContactMobile = $.trim($("#jsContactMobile").val());
		var jsqq = $.trim($("#jsqq").val());
		var jsEmail = $.trim($("#jsEmail").val());
		
		var receiveProvinceCode = $("#receiveProvinceCode").val();
		var receiveCityCode = $("#receiveCityCode").val();
		var receiveDistrictCode = $("#receiveDistrictCode").val();
		var receiveDetailAddress = $.trim($("#receiveDetailAddress").val());
		var receivePerson = $.trim($("#receivePerson").val());
		var receiveTelephone = $.trim($("#receiveTelephone").val());
		var responsibilityPerson = $.trim($("#responsibilityPerson").val());
		
		if(isOwner == null || isOwner == '' || isOwner ==undefined){
			$.messager.alert("提醒", "请选择是否使用商家后台");
			return false;
		}else if(isOwner == 1){
			if(username == ''){
				$.messager.alert("提醒", "请输入商家后台帐号");
				return false;
			}else if(!usernameReg.test(username)){
				$.messager.alert("提醒", "商家后台帐号需由字母、数字或下划线组成且长度为6-16个字符");
				return false;
			}else if(sellerId ==0 && password == ''){
				$.messager.alert("提醒", "请输入商家后台密码");
				return false;
			}else if(sellerId ==0 && !passwordReg.test(password)){
				$.messager.alert("提醒", "商家后台密码长度必须为6-16个字符");
				return false;
			}else if(auditUserId == null || auditUserId == '0' || auditUserId == undefined ){
				$.messager.alert("提醒", "请选择商品审核负责人");
				return false;
			}else if(brandId.length == 0){
				$.messager.alert("提醒", "请选择商家商品全部品牌名");
				return false;
			}else if(categoryId.length == 0){
                $.messager.alert("提醒", "请选择商家类别");
                return false;
            }
			else if(proposalSubmitType == null || proposalSubmitType == '' || proposalSubmitType == undefined){
				$.messager.alert("提醒", "请选择商定结算方式");
				return false;
			}else if(proposalSubmitType === 2 && (!intReg.test(proposalDeduction) || !floatReg.test(proposalDeduction))){
				$.messager.alert("提醒", "扣点只能为数字");
				return false;
			}else{
				return checkCommon();
			}
		}else if(isOwner == 0){
			if(checkCommon()){
				if(companyName == ''){
					$.messager.alert("提醒", "请输入公司名称");
					return false;
				}else if(isSendWeekend == null || isSendWeekend == '' || isSendWeekend ==undefined){
					$.messager.alert("提醒", "请选择周末是否发货");
					return false;
				}else if(sendTimeType == null || sendTimeType == '' || sendTimeType == undefined){
					$.messager.alert("提醒", "请选择发货时效说明");
					return false;
				}else if(!(($.trim(holidayTips)=='' && $.trim(holidayStartTime)=='' && $.trim(holidayEndTime)=='')||
						($.trim(holidayTips)!='' && $.trim(holidayStartTime)!='' && $.trim(holidayEndTime)!=''))){
					$.messager.alert("提示","填写了假期发货信息但是信息不完整");
					return false;
				}else if(fhContactPerson == ''){
					$.messager.alert("提醒", "请填写发货联系人姓名");
					return false;
				}else if(!mobileReg.test(fhContactMobile)){
					$.messager.alert("提醒", "请填写正确的发货联系人手机号");
					return false;
				}else if(!qqReg.test(jsqq)){
					$.messager.alert("提醒", "请填写正确的发货联系人QQ");
					return false;
				}else if(!emailReg.test(jsEmail)){
					$.messager.alert("提醒", "请填写正确的发货联系人邮箱");
					return false;
				}else if(shContactPerson == ''){
					$.messager.alert("提醒", "请填写售后联系人姓名");
					return false;
				}else if(!mobileReg.test(shContactMobile)){
					$.messager.alert("提醒", "请填写正确的售后联系人手机号");
					return false;
				}else if(!qqReg.test(shqq)){
					$.messager.alert("提醒", "请填写正确的售后联系人QQ");
					return false;
				}else if(!emailReg.test(shEmail)){
					$.messager.alert("提醒", "请填写正确的售后联系人邮箱");
					return false;
				}else if(rcContactPerson == ''){
					$.messager.alert("提醒", "请填写运营对接人姓名");
					return false;
				}else if(!mobileReg.test(rcContactMobile)){
					$.messager.alert("提醒", "请填写正确的运营对接人手机号");
					return false;
				}else if(!qqReg.test(rcqq)){
					$.messager.alert("提醒", "请填写正确的运营对接人QQ");
					return false;
				}else if(!emailReg.test(rcEmail)){
					$.messager.alert("提醒", "请填写正确的运营对接人邮箱");
					return false;
				}else if(jsContactPerson == ''){
					$.messager.alert("提醒", "请填写结算联系人姓名");
					return false;
				}else if(!mobileReg.test(jsContactMobile)){
					$.messager.alert("提醒", "请填写正确的结算联系人手机号");
					return false;
				}else if(!qqReg.test(jsqq)){
					$.messager.alert("提醒", "请填写正确的结算联系人QQ");
					return false;
				}else if(!emailReg.test(jsEmail)){
					$.messager.alert("提醒", "请填写正确的结算联系人邮箱");
					return false;
				}else if(receiveProvinceCode == '0' || receiveProvinceCode == '' || receiveProvinceCode == undefined || receiveProvinceCode == null){
					$.messager.alert("提示","请选择商家退货收件地址省份");
					return false;
				}else if(receiveCityCode == '0' || receiveCityCode == '' || receiveCityCode == undefined || receiveCityCode == null){
					$.messager.alert("提示","请选择商家退货收件地址城市");
					return false;
				}else if(receiveDistrictCode == '0' || receiveDistrictCode == '' || receiveDistrictCode == undefined || receiveDistrictCode == null){
					$.messager.alert("提示","请选择商家退货收件地址地区");
					return false;
				}else if($.trim(receiveDetailAddress) == ''){
					$.messager.alert("提示","请填写商家退货收件详细地址");
					return false;
				}else if($.trim(receivePerson) == ''){
					$.messager.alert("提示","请填写收件人");
					return false;
				}else if(!/^\d+/.test(receiveTelephone)){
					$.messager.alert("提示","请填写正确的收件人联系电话");
					return false;
				}else if(categoryId.length == 0){
                    $.messager.alert("提醒", "请选择商家类别");
                    return false;
                }
				else{
					return true;
				}
			}
		}
	}
	
	function checkCommon(){
		var realSellerName = $.trim($("#realSellerName").val());//商家真实名称
		var sellerName = $.trim($("#sellerName").val());//商家名称
		var settlementDay = $.trim($("#settlementDay").val());//结算周期
		var sendCodeType = $("input[name='sendCodeType']:checked").val();//发货依据
		var sellerType = $("#sellerType").val();//发货类型
		var bondedNumberType = $("input[name='bondedNumberType']:checked").val();//报关顺序
		var sendAddress = $.trim($("#sendAddress").val());//发货地
		var freightType = $("input[name='freightType']:checked").val();//运费结算方式
		var freightMoney = $.trim($("#freightMoney").val());
        var depositStatus = $("input[name='depositStatus']:checked").val();//保证金状态
        var depositCount = $.trim($("#depositCount").val());
		var responsibilityPerson = $.trim($("#responsibilityPerson").val());//招商负责人
		
		if(realSellerName == ''){
			$.messager.alert("提醒", "请输入商家真实名称");
			return false;
		}else if(sellerName == ''){
			$.messager.alert("提醒", "请输入前台展示名称");
			return false;
		}else if(!intReg.test(settlementDay)){
			$.messager.alert("提醒", "结算周期必填且只能为整数");
			return false;
		}else if(sendCodeType == null || sendCodeType == '' || sendCodeType == undefined){
			$.messager.alert("提醒", "请选择发货依据");
			return false;
		}else if((sellerType>1) && (bondedNumberType == '' || bondedNumberType == null || bondedNumberType == undefined)){
			$.messager.alert("提醒", "请选择报关顺序");
			return false;
		}else if(sendAddress == ''){
			$.messager.alert("提醒", "请输入发货地");
			return false;
		}else if(freightType == null || freightType == '' || freightType == undefined){
			$.messager.alert("提醒", "请选择运费结算方式");
			return false;
		}else if(freightType === 2 && (!intReg.test(freightMoney) || !floatReg.test(freightMoney))){
			$.messager.alert("提醒", "运费只能为数字");
			return false;
		}else if(depositStatus == null || depositStatus == '' || depositStatus == undefined){
            $.messager.alert("提醒", "请选择保证金状态");
            return false;
        }else if(depositStatus == 2 && (!intReg.test(depositCount)) && (depositCount <= 0) ){
            $.messager.alert("提醒", "保证金额只能填整数元");
            return false;
        }
		else if(responsibilityPerson == ''){
			$.messager.alert("提示","请填写招商负责人");
			return false;
		}else {
			return true;
		}
	}
</script>
</html>