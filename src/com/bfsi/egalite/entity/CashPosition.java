package com.bfsi.egalite.entity;


public class CashPosition {
	private String agentId;
	private String ccy;
	private Double openingBal;
	private Double closingBal;
	private Double creditAmt;
	private Double debitAmt;
	private Double topUp;
	private Double topDown;
	private String lastUpdateTime;
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getCcy() {
		return ccy;
	}
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	public Double getOpeningBal() {
		return openingBal;
	}
	public void setOpeningBal(Double openingBal) {
		this.openingBal = openingBal;
	}
	public Double getClosingBal() {
		return closingBal;
	}
	public void setClosingBal(Double closingBal) {
		this.closingBal = closingBal;
	}
	public Double getCreditAmt() {
		return creditAmt;
	}
	public void setCreditAmt(Double creditAmt) {
		this.creditAmt = creditAmt;
	}
	public Double getDebitAmt() {
		return debitAmt;
	}
	public void setDebitAmt(Double debitAmt) {
		this.debitAmt = debitAmt;
	}
	public Double getTopUp() {
		return topUp;
	}
	public void setTopUp(Double topUp) {
		this.topUp = topUp;
	}
	public Double getTopDown() {
		return topDown;
	}
	public void setTopDown(Double topDown) {
		this.topDown = topDown;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	
		
	
	
}
