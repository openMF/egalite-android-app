package com.bfsi.egalite.entity;

import java.util.List;

public class Agent {
	private String password;
	private byte[] passwordSalt;
	private String agentType;
	private long cashLimit;
	private String deviceId;
	private String status;
	private String fName;
	private String lName;
	private String lang;
	private String agencyCode;
	private String changePasswordDate;
	private String creditOfficer;
	private long dob;
	private String gender;
	private String authStatus;
	private String dataKey;
	private String regKey;
	private long regKeyExpDate;
	private String regStatus;	
	private String promptChangePassword;
	private String agentId;
	private String locationCode;
	private List<String> agentRoles;
	private String lastLoginTime;
	private String noInvalidLogin;
	private String lastSyncTime;
	private boolean allowTransactions;
	private boolean isLastSyncValid;
	private String city;
	private String address;
	private String contactNumber;
	private String country;
	private String dataValue;
	private String emailAddress;
	private long endDate;
	private String isActive;
	private long startDate;
	private String state;
	private String userName;
	private String zipCode;
	private String IMEI;
	private String macId;
	private String simNo;
	
	public String getSimNo() {
		return simNo;
	}
	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}
	public String getMacId() {
		return macId;
	}
	public void setMacId(String macId) {
		this.macId = macId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public byte[] getPasswordSalt() {
		return passwordSalt;
	}
	public void setPasswordSalt(byte[] passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	public long getCashLimit() {
		return cashLimit;
	}
	public void setCashLimit(long cashLimit) {
		this.cashLimit = cashLimit;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getlName() {
		return lName;
	}
	public void setlName(String lName) {
		this.lName = lName;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getAgencyCode() {
		return agencyCode;
	}
	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}
	public String getChangePasswordDate() {
		return changePasswordDate;
	}
	public void setChangePasswordDate(String changePasswordDate) {
		this.changePasswordDate = changePasswordDate;
	}
	public String getCreditOfficer() {
		return creditOfficer;
	}
	public void setCreditOfficer(String creditOfficer) {
		this.creditOfficer = creditOfficer;
	}
	public long getDob() {
		return dob;
	}
	public void setDob(long dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}
	public String getDataKey() {
		return dataKey;
	}
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}
	public String getRegKey() {
		return regKey;
	}
	public void setRegKey(String regKey) {
		this.regKey = regKey;
	}
	public long getRegKeyExpDate() {
		return regKeyExpDate;
	}
	public void setRegKeyExpDate(long regKeyExpDate) {
		this.regKeyExpDate = regKeyExpDate;
	}
	public String getRegStatus() {
		return regStatus;
	}
	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}
	public String getPromptChangePassword() {
		return promptChangePassword;
	}
	public void setPromptChangePassword(String promptChangePassword) {
		this.promptChangePassword = promptChangePassword;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public List<String> getAgentRoles() {
		return agentRoles;
	}
	public void setAgentRoles(List<String> agentRoles) {
		this.agentRoles = agentRoles;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getNoInvalidLogin() {
		return noInvalidLogin;
	}
	public void setNoInvalidLogin(String noInvalidLogin) {
		this.noInvalidLogin = noInvalidLogin;
	}
	public String getLastSyncTime() {
		return lastSyncTime;
	}
	public void setLastSyncTime(String lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}
	public boolean isAllowTransactions() {
		return allowTransactions;
	}
	public void setAllowTransactions(boolean allowTransactions) {
		this.allowTransactions = allowTransactions;
	}
	public boolean isLastSyncValid() {
		return isLastSyncValid;
	}
	public void setLastSyncValid(boolean isLastSyncValid) {
		this.isLastSyncValid = isLastSyncValid;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getIMEI() {
		return IMEI;
	}
	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}
	
	
	
	
}
