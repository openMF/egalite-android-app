package com.bfsi.egalite.entity;

public class LoanPaidSch {
	private String branchCode;
	private String customerId;
	private String customerName;
	private String schDueDate;
	private String schPaidDate;
	private String settelementCurrencyCode;
	private String amtSettled;
	private String fullPartialInd;
	private String cbsAcRefNo;
	public String getCbsAcRefNo() {
		return cbsAcRefNo;
	}
	public void setCbsAcRefNo(String cbsAcRefNo) {
		this.cbsAcRefNo = cbsAcRefNo;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSchDueDate() {
		return schDueDate;
	}
	public void setSchDueDate(String schDueDate) {
		this.schDueDate = schDueDate;
	}
	public String getSchPaidDate() {
		return schPaidDate;
	}
	public void setSchPaidDate(String schPaidDate) {
		this.schPaidDate = schPaidDate;
	}
	public String getSettelementCurrencyCode() {
		return settelementCurrencyCode;
	}
	public void setSettelementCurrencyCode(String settelementCurrencyCode) {
		this.settelementCurrencyCode = settelementCurrencyCode;
	}
	public String getAmtSettled() {
		return amtSettled;
	}
	public void setAmtSettled(String amtSettled) {
		this.amtSettled = amtSettled;
	}
	public String getFullPartialInd() {
		return fullPartialInd;
	}
	public void setFullPartialInd(String fullPartialInd) {
		this.fullPartialInd = fullPartialInd;
	}
	
}
