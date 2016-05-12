package com.bfsi.egalite.entity;

public class Liquidation {
	private String loanAcNo;
	private String branchCode;
	private String valueDt;
	private String executionDt;
	private String paymentStatus;
	private String settleMode;
	private String settleCcy;
	private String settleAmt;
	private String eventSeqNo;
	
	public String getLoanAcNo() {
		return loanAcNo;
	}
	public void setLoanAcNo(String loanAcNo) {
		this.loanAcNo = loanAcNo;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getValueDt() {
		return valueDt;
	}
	public void setValueDt(String valueDt) {
		this.valueDt = valueDt;
	}
	public String getExecutionDt() {
		return executionDt;
	}
	public void setExecutionDt(String executionDt) {
		this.executionDt = executionDt;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getSettleMode() {
		return settleMode;
	}
	public void setSettleMode(String settleMode) {
		this.settleMode = settleMode;
	}
	public String getSettleCcy() {
		return settleCcy;
	}
	public void setSettleCcy(String settleCcy) {
		this.settleCcy = settleCcy;
	}
	public String getSettleAmt() {
		return settleAmt;
	}
	public void setSettleAmt(String settleAmt) {
		this.settleAmt = settleAmt;
	}
	public String getEventSeqNo() {
		return eventSeqNo;
	}
	public void setEventSeqNo(String eventSeqNo) {
		this.eventSeqNo = eventSeqNo;
	}

}
