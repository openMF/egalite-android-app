package com.bfsi.egalite.entity;

public class Enrolement {
	private String enrollmentId;
	private String workflowQType;
	private String firstName;
	private String lastName;
	private String middleName;
	private long dob;
	private String gender;
	private String residenceType;
	private String nationality;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String emailId;
	private String contactNo;
	private String martialStatus;
	private String profession;
	private String professionRemark;
	private String accountCategory;
	private String accountType;
	private String currency;
	private String isActive;
	private String moduleCode;
	private String txnCode;
	private long txnInitTime;
	private long txnSyncTime;
	private String agentId;
	private String deviceId;
	private String locationCode;
	private String txnStatus;
	private String txnErrorCode;
	private String txnErrorMsg;
	private String groupIndiviType;
	private String tempGroupId;
	private String isKycOnly;
	private String customerId;
	private String poc;
	private byte[] thumbImage;

	public byte[] getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(byte[] thumbImage) {
		this.thumbImage = thumbImage;
	}

	public String getPoc() {
		return poc;
	}

	public void setPoc(String poc) {
		this.poc = poc;
	}

	private CustomerDocuments customerDocs;

	public CustomerDocuments getCustomerDocs() {
		return customerDocs;
	}

	public void setCustomerDocs(CustomerDocuments customerDocs) {
		this.customerDocs = customerDocs;
	}

	public String getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public String getWorkflowQType() {
		return workflowQType;
	}

	public void setWorkflowQType(String workflowQType) {
		this.workflowQType = workflowQType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Long getDob() {
		return dob;
	}

	public void setDob(Long dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getResidenceType() {
		return residenceType;
	}

	public void setResidenceType(String residenceType) {
		this.residenceType = residenceType;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getMartialStatus() {
		return martialStatus;
	}

	public void setMartialStatus(String martialStatus) {
		this.martialStatus = martialStatus;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getProfessionRemark() {
		return professionRemark;
	}

	public void setProfessionRemark(String professionRemark) {
		this.professionRemark = professionRemark;
	}

	public String getAccountCategory() {
		return accountCategory;
	}

	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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

	

	public long getTxnInitTime() {
		return txnInitTime;
	}

	public void setTxnInitTime(long txnInitTime) {
		this.txnInitTime = txnInitTime;
	}

	public long getTxnSyncTime() {
		return txnSyncTime;
	}

	public void setTxnSyncTime(long txnSyncTime) {
		this.txnSyncTime = txnSyncTime;
	}

	public void setDob(long dob) {
		this.dob = dob;
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

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
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

	public String getGroupIndiviType() {
		return groupIndiviType;
	}

	public void setGroupIndiviType(String groupIndiviType) {
		this.groupIndiviType = groupIndiviType;
	}

	public String getTempGroupId() {
		return tempGroupId;
	}

	public void setTempGroupId(String tempGroupId) {
		this.tempGroupId = tempGroupId;
	}

	public String getIsKycOnly() {
		return isKycOnly;
	}

	public void setIsKycOnly(String isKycOnly) {
		this.isKycOnly = isKycOnly;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
	}

}
