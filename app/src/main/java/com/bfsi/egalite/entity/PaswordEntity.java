package com.bfsi.egalite.entity;

import com.bfsi.mfi.rest.model.AgentDetail;
import com.bfsi.mfi.rest.model.DeviceDetail;



public class PaswordEntity {

	private AgentDetail agentdetails;
	private DeviceDetail devicedetails;
	private String datakey;
	private String statusText;
	private ResultMessage resultMessage;
	
	public ResultMessage getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(ResultMessage resultMessage) {
		this.resultMessage = resultMessage;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
	public String getDatakey() {
		return datakey;
	}
	public void setDatakey(String datakey) {
		this.datakey = datakey;
	}
	public AgentDetail getAgentdetails() {
		return agentdetails;
	}
	public void setAgentdetails(AgentDetail agentdetails) {
		this.agentdetails = agentdetails;
	}
	public DeviceDetail getDevicedetails() {
		return devicedetails;
	}
	public void setDevicedetails(DeviceDetail devicedetails) {
		this.devicedetails = devicedetails;
	}

}
