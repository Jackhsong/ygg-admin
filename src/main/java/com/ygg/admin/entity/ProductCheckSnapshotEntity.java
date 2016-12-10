package com.ygg.admin.entity;

import java.io.Serializable;

public class ProductCheckSnapshotEntity implements Serializable {

	private static final long serialVersionUID = 2469609978853144243L;

	public int id;
	private int sellerProductId;
	private int sellerId;
	private int categoryFirstId;
	private int categorySecondId;
	private int categoryThirdId;
	private String checker = "";
	private String submitTime = "";
	private String name = "";
	private String sellerName = "";
	private String sellingPoint = "";
	private String flagName = "";
	private String brandName = "";
	private String netVolume = "";
	private String placeOfOrigin = "";
	private String manufacturerDate = "";
	private String storageMethod = "";
	private String durabilityPeriod = "";
	private String peopleFor = "";
	private String foodMethod = "";
	private String useMethod = "";
	private String tip = "";
	private String code = "";
	private String barcode = "";
	private String submit = "";
	private String proposalMarketPrice = "";
	private String proposalPrice = "";
	private int productCheckDeliverAreaTemplateSnapshotId;
	private int sellerDeliverAreaTemplateId;
	private int sellerFreightTemplateId;
	private String despatchType = "";
	private String despatchAddress = "";
	private String freightType = "";
	private String freightName = "";
	private String kuaidi = "";
	private String sendTimeRemark = "";
	private String sendWeekendRemark = "";
	private String image1 = "";
	private String image2 = "";
	private String image3 = "";
	private String image4 = "";
	private String image5 = "";
	private String checkContent = "";
	private short status;
	private String detailImage = "";
	private String createTime = "";
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public int getSellerProductId() {
		return sellerProductId;
	}

	public void setSellerProductId(int sellerProductId) {
		this.sellerProductId = sellerProductId;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public int getCategoryFirstId() {
		return categoryFirstId;
	}

	public void setCategoryFirstId(int categoryFirstId) {
		this.categoryFirstId = categoryFirstId;
	}

	public int getCategorySecondId() {
		return categorySecondId;
	}

	public void setCategorySecondId(int categorySecondId) {
		this.categorySecondId = categorySecondId;
	}

	public int getCategoryThirdId() {
		return categoryThirdId;
	}

	public void setCategoryThirdId(int categoryThirdId) {
		this.categoryThirdId = categoryThirdId;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getSellingPoint() {
		return sellingPoint;
	}

	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
	}

	public String getFlagName() {
		return flagName;
	}

	public void setFlagName(String flagName) {
		this.flagName = flagName;
	}

	public String getFreightType() {
		return freightType;
	}

	public void setFreightType(String freightType) {
		this.freightType = freightType;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getNetVolume() {
		return netVolume;
	}

	public void setNetVolume(String netVolume) {
		this.netVolume = netVolume;
	}

	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	public String getManufacturerDate() {
		return manufacturerDate;
	}

	public void setManufacturerDate(String manufacturerDate) {
		this.manufacturerDate = manufacturerDate;
	}

	public String getStorageMethod() {
		return storageMethod;
	}

	public void setStorageMethod(String storageMethod) {
		this.storageMethod = storageMethod;
	}

	public String getDurabilityPeriod() {
		return durabilityPeriod;
	}

	public void setDurabilityPeriod(String durabilityPeriod) {
		this.durabilityPeriod = durabilityPeriod;
	}

	public String getPeopleFor() {
		return peopleFor;
	}

	public void setPeopleFor(String peopleFor) {
		this.peopleFor = peopleFor;
	}

	public String getFoodMethod() {
		return foodMethod;
	}

	public void setFoodMethod(String foodMethod) {
		this.foodMethod = foodMethod;
	}

	public String getUseMethod() {
		return useMethod;
	}

	public void setUseMethod(String useMethod) {
		this.useMethod = useMethod;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public String getProposalMarketPrice() {
		return proposalMarketPrice;
	}

	public void setProposalMarketPrice(String proposalMarketPrice) {
		this.proposalMarketPrice = proposalMarketPrice;
	}

	public String getProposalPrice() {
		return proposalPrice;
	}

	public String getFreightName() {
		return freightName;
	}

	public void setFreightName(String freightName) {
		this.freightName = freightName;
	}

	public void setProposalPrice(String proposalPrice) {
		this.proposalPrice = proposalPrice;
	}

	public int getProductCheckDeliverAreaTemplateSnapshotId() {
		return productCheckDeliverAreaTemplateSnapshotId;
	}

	public void setProductCheckDeliverAreaTemplateSnapshotId(int productCheckDeliverAreaTemplateSnapshotId) {
		this.productCheckDeliverAreaTemplateSnapshotId = productCheckDeliverAreaTemplateSnapshotId;
	}

	public int getSellerDeliverAreaTemplateId() {
		return sellerDeliverAreaTemplateId;
	}

	public void setSellerDeliverAreaTemplateId(int sellerDeliverAreaTemplateId) {
		this.sellerDeliverAreaTemplateId = sellerDeliverAreaTemplateId;
	}

	public int getSellerFreightTemplateId() {
		return sellerFreightTemplateId;
	}

	public void setSellerFreightTemplateId(int sellerFreightTemplateId) {
		this.sellerFreightTemplateId = sellerFreightTemplateId;
	}

	public String getDespatchType() {
		return despatchType;
	}

	public void setDespatchType(String despatchType) {
		this.despatchType = despatchType;
	}

	public String getDespatchAddress() {
		return despatchAddress;
	}

	public void setDespatchAddress(String despatchAddress) {
		this.despatchAddress = despatchAddress;
	}

	public String getKuaidi() {
		return kuaidi;
	}

	public void setKuaidi(String kuaidi) {
		this.kuaidi = kuaidi;
	}

	public String getSendTimeRemark() {
		return sendTimeRemark;
	}

	public void setSendTimeRemark(String sendTimeRemark) {
		this.sendTimeRemark = sendTimeRemark;
	}

	public String getSendWeekendRemark() {
		return sendWeekendRemark;
	}

	public void setSendWeekendRemark(String sendWeekendRemark) {
		this.sendWeekendRemark = sendWeekendRemark;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getImage5() {
		return image5;
	}

	public void setImage5(String image5) {
		this.image5 = image5;
	}

	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getDetailImage() {
		return detailImage;
	}

	public void setDetailImage(String detailImage) {
		this.detailImage = detailImage;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
