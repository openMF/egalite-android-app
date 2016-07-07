package com.bfsi.egalite.entity;

public class TxnCount {
	private String txnName;
	private String pending;
	private String executed;
	private String synced;
	private String total;
	private String reversed;
	
	public String getReversed() {
		return reversed;
	}
	public void setReversed(String reversed) {
		this.reversed = reversed;
	}
	public String getPending() {
		return pending;
	}
	public String getSynced() {
		return synced;
	}
	public void setSynced(String synced) {
		this.synced = synced;
	}
	public void setPending(String pending) {
		this.pending = pending;
	}
	public String getExecuted() {
		return executed;
	}
	public void setExecuted(String executed) {
		this.executed = executed;
	}
	public String getTxnName() {
		return txnName;
	}
	public void setTxnName(String txnName) {
		this.txnName = txnName;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}


}
