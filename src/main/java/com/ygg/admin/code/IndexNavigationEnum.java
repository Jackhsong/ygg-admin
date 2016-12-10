
 /**************************************************************************
 * Copyright (c) 2014-2016 浙江格家网络技术有限公司.
 * All rights reserved.
 * 
 * 项目名称：左岸城堡
APP
 * 版权说明：本软件属浙江格家网络技术有限公司所有，在未获得浙江格家网络技术有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.ygg.admin.code;


 /**
  * TODO 请在此处添加注释
  * @author <a href="mailto:zhangld@yangege.com">zhangld</a>
  * @version $Id: IndexNavigationEnum.java 8599 2016-03-10 05:46:24Z zhangyibo $   
  * @since 2.0
  */
public final class IndexNavigationEnum {
	
	
	public enum Type{
		 /**    */
		UNDEFINED("0", ""),
		 /**组合  */
		GROUP("1", "组合"),
		/**组合  */
		WEB_PAGE("2", "网页自定义活动"),
		/**组合  */
		NATIVE("3", "原生自定义页面"),
		/**组合  */
		CATEGORY("4", "品类馆"),
		;
		/**类型*/
		private String type;
		/**名称*/
		private String title;
	
		/**
		 * 构造函数
		  *@param type 类
		  *@param title 名称
		 */
		Type(String type, String title) {
			this.type = type;
			this.title = title;
		}
		
		/**
		 * 根据编码获取对象
		 * @param type 类型
		 * @return RiskType
		 */
		public static Type parse(String type) {
			for (Type v : Type.values()) {
				if (v.getType().equals(type)){
					return v;
				}	
			}
			return null;
		}

		/**  
		 *@return  the type
		 */
		public String getType() {
			return type;
		}

		/** 
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**  
		 *@return  the title
		 */
		public String getTitle() {
			return title;
		}

		/** 
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}
	
	public enum PageModelType{
		 /**    */
		UNDEFINED(0, ""),
		 /**通栏Banner*/
		FULL_BANNER(1, "通栏Banner"),
		/**组合  */
		CALSSIFY(2, "分类"),
		/**组合  */
		CUSTOM(3, "自定义板块"),
		/**组合  */
		GEGE_RE(4, "格格推荐"),
		/**组合  */
		BRAND_RE(5, "品牌推荐"),
		/**组合  */
		SALE(6, "今日特卖");
		/**类型*/
		private int type;
		/**名称*/
		private String title;
	
		/**
		 * 构造函数
		  *@param type 类
		  *@param title 名称
		 */
		PageModelType(int type, String title) {
			this.type = type;
			this.title = title;
		}
		
		/**
		 * 根据编码获取对象
		 * @param type 类型
		 * @return RiskType
		 */
		public static PageModelType parse(int type) {
			for (PageModelType v : PageModelType.values()) {
				if (v.getType() == type){
					return v;
				}	
			}
			return null;
		}

		/**  
		 *@return  the type
		 */
		public int getType() {
			return type;
		}

		/** 
		 * @param type the type to set
		 */
		public void setType(int type) {
			this.type = type;
		}

		/**  
		 *@return  the title
		 */
		public String getTitle() {
			return title;
		}

		/** 
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}
	public enum CategoryClassificationType{
        /**    */
        UNDEFINED("0", ""),

        /** 组合 */
		GROUP("1", "组合特卖"),

		/** 网页自定义活动 */
        WEB_PAGE("2", "网页自定义活动"),

		/** 原生自定义活动 */
        NATIVE("3", "原生自定义页面"),

		/** 商城分类 */
        MALL_CLASSIFICATION("4", "商城分类");

        /** 类型 */
        private String type;
        
        /** 名称 */
        private String title;
	
		/**
		 * 构造函数
		  *@param type 类
		  *@param title 名称
		 */
		CategoryClassificationType(String type, String title) {
			this.type = type;
			this.title = title;
		}
		
		/**
		 * 根据编码获取对象
		 * @param type 类型
		 * @return RiskType
		 */
		public static CategoryClassificationType parse(String type) {
			for (CategoryClassificationType v : CategoryClassificationType.values()) {
				if (v.getType().equals(type)){
					return v;
				}	
			}
			return null;
		}

		/**  
		 *@return  the type
		 */
		public String getType() {
			return type;
		}

		/** 
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**  
		 *@return  the title
		 */
		public String getTitle() {
			return title;
		}

		/** 
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}
	public enum CategoryRecommendType{
		 /**    */
		UNDEFINED("0", ""),
		 /**组合  */
		GROUP("2", "组合特卖"),
		/**组合  */
		WEB_PAGE("3", "网页自定义活动"),
		/**组合  */
		NATIVE("7", "原生自定义页面");
		/**类型*/
		private String type;
		/**名称*/
		private String title;
	
		/**
		 * 构造函数
		  *@param type 类
		  *@param title 名称
		 */
		CategoryRecommendType(String type, String title) {
			this.type = type;
			this.title = title;
		}
		
		/**
		 * 根据编码获取对象
		 * @param type 类型
		 * @return RiskType
		 */
		public static CategoryRecommendType parse(String type) {
			for (CategoryRecommendType v : CategoryRecommendType.values()) {
				if (v.getType().equals(type)){
					return v;
				}	
			}
			return null;
		}

		/**  
		 *@return  the type
		 */
		public String getType() {
			return type;
		}

		/** 
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**  
		 *@return  the title
		 */
		public String getTitle() {
			return title;
		}

		/** 
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}
}
