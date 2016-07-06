package com.bfsi.egalite.entity;

import java.util.List;

public class EnrolDocuments 
{
	 private String enrollmentId;
	 private String documentId;
	 private String documentName;
	 private String documentProof;
	 private byte[] documentData;
	 private List<String> documentTag;
	 
	public String getEnrollmentId() {
		return enrollmentId;
	}
	public void setEnrollmentId(String enrollmentId) {
		this.enrollmentId = enrollmentId;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public byte[] getDocumentData() {
		return documentData;
	}
	public void setDocumentData(byte[] documentData) {
		this.documentData = documentData;
	}
	public List<String> getDocumentTag() {
		return documentTag;
	}
	public void setDocumentTag(List<String> documentTag) {
		this.documentTag = documentTag;
	}
	public String getDocumentProof() {
		return documentProof;
	}
	public void setDocumentProof(String documentProof) {
		this.documentProof = documentProof;
	}
}
