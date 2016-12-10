package com.ygg.admin.entity;

import java.io.Serializable;

public class ProductCheckLogEntity implements Serializable {

	private static final long serialVersionUID = 2842728611716272689L;

	private int id;
	private int sellerProductId;
	private int productCheckSnapshotId;
	private String checker;
	private String name;
	private String sellerName;
	private int categoryFirstId;
	private int categorySecondId;
	private int categoryThirdId;
	private short status;
	private String checkContent;
	private String submitTime;
	private String checkTime;
	private String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSellerProductId() {
		return sellerProductId;
	}

	public void setSellerProductId(int sellerProductId) {
		this.sellerProductId = sellerProductId;
	}

	public int getProductCheckSnapshotId() {
		return productCheckSnapshotId;
	}

	public void setProductCheckSnapshotId(int productCheckSnapshotId) {
		this.productCheckSnapshotId = productCheckSnapshotId;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
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

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
