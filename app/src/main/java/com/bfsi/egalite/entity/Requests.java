package com.bfsi.egalite.entity;

public class Requests {
	
	public String customerName;
	private String agentName;
	private String prePayDate;
	private String requestId;
	public String requestType;
	private String requestDate;
	private String moduleCode;
	private String agentId;
	private String customerId;
	private String ccyCode;
	public String depInstallmentAmt;
	private String deposittenure;
	private String depositPayfreq;
	private String numOfInst;
	private String depOpenDate;
	public String acNo;
	private String requeststatus;
	private String requestRemark;
	private String amountTillDate;
	private String redempReqDate;
	private String redemReqAmt;
	private long maturityDate;
	private String branchCode;
	private String reqAmnt;
	private String deviceId;
	private String authStatus;
	private String prepaymentAmt;
	private String interestAccrued;
	private String rateofInst;
	private String maturityStatus;
	private String maturityAmt;
	private long txnTime;
	private String redemptionType;
	private String firstPaymentDate;
	private String address;
	private String frequency;
	private String frequencyType;
	private String tenureType;
	private String locCode;
	private String narrative;
	private String agentBal;
	private String mobileNumber;
	private String smsRequired;
	private String txnId;
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getSmsRequired() {
		return smsRequired;
	}
	public void setSmsRequired(String smsRequired) {
		this.smsRequired = smsRequired;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getTenure() {
		return tenure;
	}
	public void setTenure(String tenure) {
		this.tenure = tenure;
	}
	private String tenure;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFirstPaymentDate() {
		return firstPaymentDate;
	}
	public void setFirstPaymentDate(String firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}
	public String getRedemptionType() {
		return redemptionType;
	}
	public void setRedemptionType(String redemptionType) {
		this.redemptionType = redemptionType;
	}
	public String getMaturityAmt() {
		return maturityAmt;
	}
	public void setMaturityAmt(String maturityAmt) {
		this.maturityAmt = maturityAmt;
	}
	public long getTxnTime() {
		return txnTime;
	}
	public void setTxnTime(long txnTime) {
		this.txnTime = txnTime;
	}
	public String getPrePayDate() {
			return prePayDate;
		}
		public void setPrePayDate(String prePayDate) {
			this.prePayDate = prePayDate;
		}
		public String getAgentName() {
			return agentName;
		}
		public void setAgentName(String agentName) {
			this.agentName = agentName;
		}
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}  
	public String getRateofInst() {
		return rateofInst;
	}
	public void setRateofInst(String rateofInst) {
		this.rateofInst = rateofInst;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCcyCode() {
		return ccyCode;
	}
	public void setCcyCode(String depositCcy) {
		this.ccyCode = depositCcy;
	}
	
	public String getDeposittenure() {
		return deposittenure;
	}
	public void setDeposittenure(String deposittenure) {
		this.deposittenure = deposittenure;
	}
	public String getDepositPayfreq() {
		return depositPayfreq;
	}
	public void setDepositPayfreq(String depositPayfreq) {
		this.depositPayfreq = depositPayfreq;
	}
	public String getNumOfInst() {
		return numOfInst;
	}
	public void setNumOfInst(String numOfInst) {
		this.numOfInst = numOfInst;
	}
	
	public String getDepOpenDate() {
		return depOpenDate;
	}
	public void setDepOpenDate(String depOpenDate) {
		this.depOpenDate = depOpenDate;
	}
	public String getAcNo() {
		return acNo;
	}
	public void setAcNo(String depositAccNum) {
		this.acNo = depositAccNum;
	}
	public String getRequeststatus() {
		return requeststatus;
	}
	public void setRequeststatus(String requeststatus) {
		this.requeststatus = requeststatus;
	}
	public String getRequestRemark() {
		return requestRemark;
	}
	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
	
	public String getRedempReqDate() {
		return redempReqDate;
	}
	public void setRedempReqDate(String redempReqDate) {
		this.redempReqDate = redempReqDate;
	}
	
	
	public String getBranchCode() {
		return branchCode;
	}
	public long getMaturityDate() {
		return maturityDate;
	}
	public void setMaturityDate(long maturityDate) {
		this.maturityDate = maturityDate;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getReqAmnt() {
		return reqAmnt;
	}
	public void setReqAmnt(String reqAmnt) {
		this.reqAmnt = reqAmnt;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDepInstallmentAmt() {
		return depInstallmentAmt;
	}
	public void setDepInstallmentAmt(String depInstallmentAmt) {
		this.depInstallmentAmt = depInstallmentAmt;
	}
	public String getAmountTillDate() {
		return amountTillDate;
	}
	public void setAmountTillDate(String amountTillDate) {
		this.amountTillDate = amountTillDate;
	}
	public String getRedemReqAmt() {
		return redemReqAmt;
	}
	public void setRedemReqAmt(String redemReqAmt) {
		this.redemReqAmt = redemReqAmt;
	}
	public String getPrepaymentAmt() {
		return prepaymentAmt;
	}
	public void setPrepaymentAmt(String prepaymentAmt) {
		this.prepaymentAmt = prepaymentAmt;
	}
	public String getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	public String getInterestAccrued() {
		return interestAccrued;
	}
	public void setInterestAccrued(String interestAccrued) {
		this.interestAccrued = interestAccrued;
	}
	public String getMaturityStatus() {
		return maturityStatus;
	}
	public void setMaturityStatus(String maturityStatus) {
		this.maturityStatus = maturityStatus;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public String getTenureType() {
		return tenureType;
	}
	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}
	public String getLocCode() {
		return locCode;
	}
	public void setLocCode(String locCode) {
		this.locCode = locCode;
	}
	public String getNarrative() {
		return narrative;
	}
	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}
	public String getAgentBal() {
		return agentBal;
	}
	public void setAgentBal(String agentBal) {
		this.agentBal = agentBal;
	}
}
