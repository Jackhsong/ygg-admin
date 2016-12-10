package com.ygg.admin.entity;

import java.io.Serializable;

public class ProviderEntity implements Serializable {

	private static final long serialVersionUID = -4720836938942633035L;

	private int id;
	private String name;
	private String companyName = "";
	private String providerBrand;
	private String offices;
	private String companyDetailAddress;
	private short currency = 0;
	private short isInvoice = 0;
	private String tax = "";
	private String contactPerson;
	private String contactPhone;
	private String qq = "";
	private String email = "";
	private short purchaseSubmitType = 0;
	private String percent = "";
	private String day = "";
	private String other = "";
	private String receiveBankAccount;
	private String receiveBank;;
	private String receiveName;
	private String swiftCode = "";
	private String bankAddress = "";
	private String remark = "";
	private String contractImgUrl = "";
	private String purchasingLeader;
	private short isAvailable;
	private String createTime;
	private short type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProviderBrand() {
		return providerBrand;
	}

	public void setProviderBrand(String providerBrand) {
		this.providerBrand = providerBrand;
	}

	public String getOffices() {
		return offices;
	}

	public void setOffices(String offices) {
		this.offices = offices;
	}

	public String getCompanyDetailAddress() {
		return companyDetailAddress;
	}

	public void setCompanyDetailAddress(String companyDetailAddress) {
		this.companyDetailAddress = companyDetailAddress;
	}

	public short getCurrency() {
		return currency;
	}

	public void setCurrency(short currency) {
		this.currency = currency;
	}

	public short getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(short isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public short getPurchaseSubmitType() {
		return purchaseSubmitType;
	}

	public void setPurchaseSubmitType(short purchaseSubmitType) {
		this.purchaseSubmitType = purchaseSubmitType;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getReceiveBankAccount() {
		return receiveBankAccount;
	}

	public void setReceiveBankAccount(String receiveBankAccount) {
		this.receiveBankAccount = receiveBankAccount;
	}

	public String getReceiveBank() {
		return receiveBank;
	}

	public void setReceiveBank(String receiveBank) {
		this.receiveBank = receiveBank;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContractImgUrl() {
		return contractImgUrl;
	}

	public void setContractImgUrl(String contractImgUrl) {
		this.contractImgUrl = contractImgUrl;
	}

	public String getPurchasingLeader() {
		return purchasingLeader;
	}

	public void setPurchasingLeader(String purchasingLeader) {
		this.purchasingLeader = purchasingLeader;
	}

	public short getIsAvailable() {
		return isAvailable;
	}

	public void setIsAvailable(short isAvailable) {
		this.isAvailable = isAvailable;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

    public short getType()
    {
        return type;
    }

    public void setType(short type)
    {
        this.type = type;
    }

}
