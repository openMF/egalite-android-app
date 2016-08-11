package com.bfsi.egalite.entity;

public class MessageCode {
	private String lang;
	private String desc;
	private String errType;
	private String subSys;
	private String msgType;
	private String code;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getErrType() {
		return errType;
	}
	public void setErrType(String errType) {
		this.errType = errType;
	}
	public String getSubSys() {
		return subSys;
	}
	public void setSubSys(String subSys) {
		this.subSys = subSys;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	

}
