package com.ygg.admin.entity;

import java.io.Serializable;
import java.util.List;

public class PurchaseOrderInfoEntity implements Serializable {

	private static final long serialVersionUID = -2620932213040423655L;
	
	private String id;
	private String storageDetailAddress;
	private String storageContactPerson;
	private String storageContactPhone;

	private String providerName;
	private String providerCompanyName;
	private String providerContactPerson;
	private String providerContactPhone;
	private String providerReceiveBankAccount;
	private String providerReceiveBank;
	private String providerReceiveName;
	private String providerBankAddress;
	private String swiftCode;
	private String purchasingLeader;

	private String createTime;
	private float freightMoney;
	private float totalMoney;
	private String remark;
	private int status;

	private List<PurchaseOrderProductEntity> orderProductList;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStorageDetailAddress() {
		return storageDetailAddress;
	}

	public void setStorageDetailAddress(String storageDetailAddress) {
		this.storageDetailAddress = storageDetailAddress;
	}

	public String getStorageContactPerson() {
		return storageContactPerson;
	}

	public void setStorageContactPerson(String storageContactPerson) {
		this.storageContactPerson = storageContactPerson;
	}

	public String getStorageContactPhone() {
		return storageContactPhone;
	}

	public void setStorageContactPhone(String storageContactPhone) {
		this.storageContactPhone = storageContactPhone;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderCompanyName() {
		return providerCompanyName;
	}

	public void setProviderCompanyName(String providerCompanyName) {
		this.providerCompanyName = providerCompanyName;
	}

	public String getProviderContactPerson() {
		return providerContactPerson;
	}

	public void setProviderContactPerson(String providerContactPerson) {
		this.providerContactPerson = providerContactPerson;
	}

	public String getProviderContactPhone() {
		return providerContactPhone;
	}

	public void setProviderContactPhone(String providerContactPhone) {
		this.providerContactPhone = providerContactPhone;
	}

	public String getProviderReceiveBankAccount() {
		return providerReceiveBankAccount;
	}

	public void setProviderReceiveBankAccount(String providerReceiveBankAccount) {
		this.providerReceiveBankAccount = providerReceiveBankAccount;
	}

	public String getProviderReceiveBank() {
		return providerReceiveBank;
	}

	public void setProviderReceiveBank(String providerReceiveBank) {
		this.providerReceiveBank = providerReceiveBank;
	}

	public String getProviderReceiveName() {
		return providerReceiveName;
	}

	public void setProviderReceiveName(String providerReceiveName) {
		this.providerReceiveName = providerReceiveName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public float getFreightMoney() {
		return freightMoney;
	}

	public void setFreightMoney(float freightMoney) {
		this.freightMoney = freightMoney;
	}

	public float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public List<PurchaseOrderProductEntity> getOrderProductList() {
		return orderProductList;
	}

	public void setOrderProductList(List<PurchaseOrderProductEntity> orderProductList) {
		this.orderProductList = orderProductList;
	}

	public String getProviderBankAddress() {
		return providerBankAddress;
	}

	public void setProviderBankAddress(String providerBankAddress) {
		this.providerBankAddress = providerBankAddress;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPurchasingLeader() {
		return purchasingLeader;
	}

	public void setPurchasingLeader(String purchasingLeader) {
		this.purchasingLeader = purchasingLeader;
	}

}
