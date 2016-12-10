package com.ygg.admin.entity;

import java.io.Serializable;

public class PurchaseOrderProductEntity implements Serializable {

	private static final long serialVersionUID = -7438398724399067717L;

	private String barcode;
	private String name;
	private String brandId;
	private String specification;
	private String purchaseQuantity;
	private String purchaseUnit;
	private String providerPrice;
	private String totalMoney;
	private String manufacturerDate;
	private String durabilityPeriod;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getPurchaseQuantity() {
		return purchaseQuantity;
	}

	public void setPurchaseQuantity(String purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}

	public String getProviderPrice() {
		return providerPrice;
	}

	public void setProviderPrice(String providerPrice) {
		this.providerPrice = providerPrice;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getManufacturerDate() {
		return manufacturerDate;
	}

	public void setManufacturerDate(String manufacturerDate) {
		this.manufacturerDate = manufacturerDate;
	}

	public String getDurabilityPeriod() {
		return durabilityPeriod;
	}

	public void setDurabilityPeriod(String durabilityPeriod) {
		this.durabilityPeriod = durabilityPeriod;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

}
