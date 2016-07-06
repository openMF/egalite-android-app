package com.bfsi.egalite.entity;

public class TxnMaster {
	private String moduleCode;
	private String txnCode;
	private String agendaId;
	private String seqNo;
	private String cbsAcRefNo;
	private String brnCode;
	private String customerId;
	private long initTime;
	private String isSynched;
	private String agentId;
	private String deviceId;
	private String locationCode;
	private String lnIsFutureSch;
	private String txnAmt;
	private String amtLcy;
	private String settledAmt;
	private String settledAmtLcyNum;
	private String reqDepNoInst;
	private String reqRedReqDt;
	private String reqRedReqFullPartInd;
	private long reqMaturityDate;
	private String reqIntRate;
	private String reqDpTenureType;
	private String reqDpFrequencyType;
	private String reqDpFrequency;
	private String reqDpTenure;
	private String fullPartInd;
	private String txnDt;
	private String customerName;
	private String ccyCode;
	private String lcyCode;
	private String txnType;
	private String agendaStatus;
	private String accountType;
	private String componentName;
	private String loanDisbursementType;
	private String dob;
	private String isGroupLoan;
	private String mbsSeqNo;
	private String parentCbsRefAcNo;
	private String agendaCmpStartDate;
	
	public String getAgendaCmpStartDate() {
		return agendaCmpStartDate;
	}
	public void setAgendaCmpStartDate(String agendaCmpStartDate) {
		this.agendaCmpStartDate = agendaCmpStartDate;
	}
	public String getParentCbsRefAcNo() {
		return parentCbsRefAcNo;
	}
	public void setParentCbsRefAcNo(String parentCbsRefAcNo) {
		this.parentCbsRefAcNo = parentCbsRefAcNo;
	}
	
	public String getMbsSeqNo() {
		return mbsSeqNo;
	}
	public void setMbsSeqNo(String mbsSeqNo) {
		this.mbsSeqNo = mbsSeqNo;
	}
	public String getIsGroupLoan() {
		return isGroupLoan;
	}
	public void setIsGroupLoan(String isGroupLoan) {
		this.isGroupLoan = isGroupLoan;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public String getLoanDisbursementType() {
		return loanDisbursementType;
	}
	public void setLoanDisbursementType(String loanDisbursementType) {
		this.loanDisbursementType = loanDisbursementType;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	private int txnStatus;
	private String txnErrorCode;
	private String txnErrorMsg;
	private String generatedSms;
	private String smsMobileNo;
	private String smsContent;
	private String generateRevr;
	private String mbsTxnSeqNo;
	
	
	
	public int getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(int txnStatus) {
		this.txnStatus = txnStatus;
	}
	public String getTxnErrorCode() {
		return txnErrorCode;
	}
	public void setTxnErrorCode(String txnErrorCode) {
		this.txnErrorCode = txnErrorCode;
	}
	public String getTxnErrorMsg() {
		return txnErrorMsg;
	}
	public void setTxnErrorMsg(String txnErrorMsg) {
		this.txnErrorMsg = txnErrorMsg;
	}
	public String getGeneratedSms() {
		return generatedSms;
	}
	public void setGeneratedSms(String generatedSms) {
		this.generatedSms = generatedSms;
	}
	public String getSmsMobileNo() {
		return smsMobileNo;
	}
	public void setSmsMobileNo(String smsMobileNo) {
		this.smsMobileNo = smsMobileNo;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	public String getGenerateRevr() {
		return generateRevr;
	}
	public void setGenerateRevr(String generateRevr) {
		this.generateRevr = generateRevr;
	}
	public String getMbsTxnSeqNo() {
		return mbsTxnSeqNo;
	}
	public void setMbsTxnSeqNo(String mbsTxnSeqNo) {
		this.mbsTxnSeqNo = mbsTxnSeqNo;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getCcyCode() {
		return ccyCode;
	}
	public void setCcyCode(String ccyCode) {
		this.ccyCode = ccyCode;
	}
	public String getLcyCode() {
		return lcyCode;
	}
	public void setLcyCode(String lcyCode) {
		this.lcyCode = lcyCode;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String txnId;
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public String getTxnCode() {
		return txnCode;
	}
	public void setTxnCode(String txnCode) {
		this.txnCode = txnCode;
	}
	public String getAgendaId() {
		return agendaId;
	}
	public void setAgendaId(String agendaId) {
		this.agendaId = agendaId;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public String getCbsAcRefNo() {
		return cbsAcRefNo;
	}
	public void setCbsAcRefNo(String cbsAcRefNo) {
		this.cbsAcRefNo = cbsAcRefNo;
	}
	public String getBrnCode() {
		return brnCode;
	}
	public void setBrnCode(String brnCode) {
		this.brnCode = brnCode;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public long getInitTime() {
		return initTime;
	}
	public void setInitTime(long initTime) {
		this.initTime = initTime;
	}
	public String getIsSynched() {
		return isSynched;
	}
	public void setIsSynched(String isSynched) {
		this.isSynched = isSynched;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getLnIsFutureSch() {
		return lnIsFutureSch;
	}
	public void setLnIsFutureSch(String lnIsFutureSch) {
		this.lnIsFutureSch = lnIsFutureSch;
	}
	public String getTxnAmt() {
		return txnAmt;
	}
	public void setTxnAmt(String txnAmt) {
		this.txnAmt = txnAmt;
	}
	public String getAmtLcy() {
		return amtLcy;
	}
	public void setAmtLcy(String amtLcy) {
		this.amtLcy = amtLcy;
	}
	public String getSettledAmt() {
		return settledAmt;
	}
	public void setSettledAmt(String settledAmt) {
		this.settledAmt = settledAmt;
	}
	public String getSettledAmtLcyNum() {
		return settledAmtLcyNum;
	}
	public void setSettledAmtLcyNum(String settledAmtLcyNum) {
		this.settledAmtLcyNum = settledAmtLcyNum;
	}
	
	public String getReqDepNoInst() {
		return reqDepNoInst;
	}
	public void setReqDepNoInst(String reqDepNoInst) {
		this.reqDepNoInst = reqDepNoInst;
	}
	public String getReqRedReqDt() {
		return reqRedReqDt;
	}
	public void setReqRedReqDt(String reqRedReqDt) {
		this.reqRedReqDt = reqRedReqDt;
	}
	public String getReqRedReqFullPartInd() {
		return reqRedReqFullPartInd;
	}
	public void setReqRedReqFullPartInd(String reqRedReqFullPartInd) {
		this.reqRedReqFullPartInd = reqRedReqFullPartInd;
	}
	public long getReqMaturityDate() {
		return reqMaturityDate;
	}
	public void setReqMaturityDate(long mtDateTime) {
		this.reqMaturityDate = mtDateTime;
	}
	public String getReqIntRate() {
		return reqIntRate;
	}
	public void setReqIntRate(String reqIntRate) {
		this.reqIntRate = reqIntRate;
	}
	public String getReqDpTenureType() {
		return reqDpTenureType;
	}
	public void setReqDpTenureType(String reqDpTenureType) {
		this.reqDpTenureType = reqDpTenureType;
	}
	public String getReqDpFrequencyType() {
		return reqDpFrequencyType;
	}
	public void setReqDpFrequencyType(String reqDpFrequencyType) {
		this.reqDpFrequencyType = reqDpFrequencyType;
	}
	public String getReqDpFrequency() {
		return reqDpFrequency;
	}
	public void setReqDpFrequency(String reqDpFrequency) {
		this.reqDpFrequency = reqDpFrequency;
	}
	public String getReqDpTenure() {
		return reqDpTenure;
	}
	public void setReqDpTenure(String reqDpTenure) {
		this.reqDpTenure = reqDpTenure;
	}
	public String getFullPartInd() {
		return fullPartInd;
	}
	public void setFullPartInd(String fullPartInd) {
		this.fullPartInd = fullPartInd;
	}
	public String getTxnDt() {
		return txnDt;
	}
	public void setTxnDt(String txnDt) {
		this.txnDt = txnDt;
	}
	public String getAgendaStatus() {
		return agendaStatus;
	}
	public void setAgendaStatus(String agendaStatus) {
		this.agendaStatus = agendaStatus;
	}
	
	

}
