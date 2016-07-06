package com.bfsi.egalite.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.dao.EnrolementDao;
import com.bfsi.egalite.entity.CustomerDocuments;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.service.impl.BaseTransactionService;
import com.bfsi.mfi.rest.model.CustomerBiometricDetails;
import com.bfsi.mfi.rest.model.CustomerDocument;
import com.bfsi.mfi.rest.model.CustomerEnrolmentDetails;

@SuppressLint("NewApi")
public class EnrolmentDataAccess extends BaseTransactionService implements
		EnrolementDao {

	private Logger LOG = LoggerFactory.getLogger(getClass());

	public static String MTS01 = "MTS01";
	public static String GDR01 = "GDR01";
	public static String KYC01 = "KYC01";
	public static String CN001 = "CN001";
	public static String PRO01 = "PRO01";
	public static String NAT01 = "NAT01";
	public static String CCY01 = "CCY01";

	@Override
	public long insertEnrolement(CustomerEnrolmentDetails enrolement)
			throws SQLiteException, DataAccessException {
		long status = -1;
		SQLiteDatabase db = null;
		try {

			enrolement.setIsActive("P");
			enrolement.setIsActive(enrolement.getIsActive());
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_MBS_ENRL_ENROLMENTID,
					enrolement.getEnrolmentId());
			values.put(DbHandlerL.COL_MBS_ENRL_FIRSTNAME,
					enrolement.getFirstName());
			values.put(DbHandlerL.COL_MBS_ENRL_LASTNAME,
					enrolement.getLastName());
			values.put(DbHandlerL.COL_MBS_ENRL_MIDDLENAME,
					enrolement.getMiddleName());
			values.put(DbHandlerL.COL_MBS_ENRL_DOB, enrolement.getDob());
			values.put(DbHandlerL.COL_MBS_ENRL_GENDER, enrolement.getGender());
			values.put(DbHandlerL.COL_MBS_ENRL_ADDRESS1,
					enrolement.getAddress1());
			values.put(DbHandlerL.COL_MBS_ENRL_ADDESS2,
					enrolement.getAddress2());
			values.put(DbHandlerL.COL_MBS_ENRL_ADDESS3,
					enrolement.getAddress3());
			values.put(DbHandlerL.COL_MBS_ENRL_ADDESS4,
					enrolement.getAddress4());
			values.put(DbHandlerL.COL_MBS_ENRL_CONTACTNO,
					enrolement.getContactNo());
			values.put(DbHandlerL.COL_MBS_ENRL_EMAILID, enrolement.getEmail());
			values.put(DbHandlerL.COL_MBS_ENRL_COUNTRY, enrolement.getCountry());
			values.put(DbHandlerL.COL_MBS_ENRL_STATE, enrolement.getState());
			values.put(DbHandlerL.COL_MBS_ENRL_ZIPCODE, enrolement.getZipCode());
			values.put(DbHandlerL.COL_MBS_ENRL_CURRENCY,
					enrolement.getAccCurrency());
			values.put(DbHandlerL.COL_MBS_ENRL_LOCATIONCODE,
					enrolement.getLocCode());
			values.put(DbHandlerL.COL_MBS_ENRL_RESIDENCETYPE,
					enrolement.getResidenceType());
			values.put(DbHandlerL.COL_MBS_ENRL_NATIONALID,
					enrolement.getNationality());
			values.put(DbHandlerL.COL_MBS_ENRL_MARTIALSTATUS,
					enrolement.getMaritalStatus());
			values.put(DbHandlerL.COL_MBS_ENRL_PROFESSION,
					enrolement.getProfession());
			values.put(DbHandlerL.COL_MBS_ENRL_PRFREMARK,
					enrolement.getProfessionRemark());
			values.put(DbHandlerL.COL_MBS_ENRL_ACCOUNTTYPE,
					enrolement.getAccountCategory());
			values.put(DbHandlerL.COL_MBS_ENRL_TXNINITIME,
					enrolement.getTxnInitTime());
			values.put(DbHandlerL.COL_MBS_ENRL_TXNSTATUS,
					0);
			values.put(DbHandlerL.COL_MBS_ENRL_MODULECODE,
					enrolement.getModuleCode());
			values.put(DbHandlerL.COL_MBS_ENRL_TXNCODE,
					enrolement.getTxnCode());
			values.put(DbHandlerL.COL_MBS_ENRL_ISKYCONLY,
					enrolement.getIsKycOnly());
			values.put(DbHandlerL.COL_MBS_ENRL_CUSTOMERID,
					enrolement.getCustomerId());
			values.put(DbHandlerL.COL_MBS_ENRL_AGENTID,
					enrolement.getAgentId());
			values.put(DbHandlerL.COL_MBS_ENRL_DEVICEID,
					enrolement.getDeviceId());
			values.put(DbHandlerL.COL_MBS_ENRL_TEMPGROUPID, enrolement.getTmpGrpId());
			values.put(DbHandlerL.COL_MBS_ENRL_GROUPINDIVIDUALTYPE,enrolement.getGroupIndlType());
			values.put(DbHandlerL.COL_MBS_ENRL_ISSYNCED,0);
			values.put(DbHandlerL.COL_MBS_ENRL_POC,enrolement.getPoc());
			values.put(DbHandlerL.COL_MBS_ENRL_THUMBIMAGE, "");//need to add this to interface object	
			db = DbHandlerL.getInstance().getWritableDatabase();

			status = db.insert(DbHandlerL.TABLE_MBS_ENROLMENT,
					DbHandlerL.COL_MBS_ENRL_ENROLMENTID, values);

			// insertEnrolDocument(db, enrolement);

		} catch (SQLiteException e) {
			LOG.error("Exception while insertEnrolement : EnrolmentDataAccess"
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception while insertEnrolement : EnrolmentDataAccess "
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			if (db != null) {

				db.close();
			}
		}
		return status;

	}

	public long insertEnrolDocument(CustomerEnrolmentDetails enrolement,
			long txnStatus) throws SQLiteException, DataAccessException {
		SQLiteDatabase db = null;
		long docStatus = -1;
		try {
			CustomerDocument listDocuemnts = enrolement.getDocument();
			ContentValues values = new ContentValues();

			values.put(DbHandlerL.COL_MBS_ENRL_ENROLID,
					enrolement.getEnrolmentId());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_IMG_CUSTOMER,
					listDocuemnts.getKycCustImage());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_1_IMG,
					listDocuemnts.getKycIdImage1());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_1_TYPE,
					listDocuemnts.getKycIdType1());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_1_NUMBER,
					listDocuemnts.getKycIdNo1());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_1_PROOF_TYPE,
					listDocuemnts.getKycIdProofType1());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_2_IMG,
					listDocuemnts.getKycIdImage2());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_2_TYPE,
					listDocuemnts.getKycIdType2());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_2_NUMBER,
					listDocuemnts.getKycIdNo2());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_2_PROOF_TYPE,
					listDocuemnts.getKycIdProofType2());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_3_IMG,
					listDocuemnts.getKycIdImage3());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_3_TYPE,
					listDocuemnts.getKycIdType3());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_3_NUMBER,
					listDocuemnts.getKycIdNo3());
			values.put(DbHandlerL.COL_MBS_ENRL_KYC_ID_3_PROOF_TYPE,
					listDocuemnts.getKycIdProofType3());
//			values.put(DbHandlerL.COL_MBS_ENRL_TXNSTATUS,
//					enrolement.getTxnStatus());
			db = DbHandlerL.getInstance().getWritableDatabase();
			docStatus = db.insert(DbHandlerL.TABLE_MBS_ENROLMENT_DOCUMENT,
					DbHandlerL.COL_MBS_ENRL_ENROLID, values);
			
			//insert in Biometric Data table
			ContentValues valuesBio = new ContentValues();
			valuesBio.put(DbHandlerL.COL_MBS_BIO_ENROLID,
					listDocuemnts.getEnrolmentId());
			db.insert(DbHandlerL.TABLE_MBS_BIOMETRIC_DATA,
					DbHandlerL.COL_MBS_ENRL_ENROLID, valuesBio);
			
			if (txnStatus == -1) {

				db.delete(DbHandlerL.TABLE_MBS_ENROLMENT,
						DbHandlerL.COL_MBS_ENRL_ENROLMENTID + " = '"
								+ enrolement.getEnrolmentId() + "'", null);
				db.delete(DbHandlerL.TABLE_MBS_BIOMETRIC_DATA,
						DbHandlerL.COL_MBS_BIO_ENROLID + " = '"
								+ enrolement.getEnrolmentId() + "'", null);
			}

			else if (docStatus == -1) {
				db.delete(DbHandlerL.TABLE_MBS_ENROLMENT,
						DbHandlerL.COL_MBS_ENRL_ENROLMENTID + " = '"
								+ enrolement.getEnrolmentId() + "'", null);
				db.delete(DbHandlerL.TABLE_MBS_BIOMETRIC_DATA,
						DbHandlerL.COL_MBS_BIO_ENROLID + " = '"
								+ enrolement.getEnrolmentId() + "'", null);

			}

		} catch (SQLiteException e) {
			LOG.error("Exception while insertEnrolDocument : EnrolmentDataAccess"
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception while insertEnrolDocument : EnrolmentDataAccess "
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return docStatus;
	}

	public List<Enrolement> readStatusList() throws SQLiteException,
			DataAccessException {
		Enrolement enrolement = null;
		List<Enrolement> enrollist = new ArrayList<Enrolement>();
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {

			String sql = "select * from " + DbHandlerL.TABLE_MBS_ENROLMENT;
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {
					enrolement = new Enrolement();
					enrolement
							.setEnrollmentId(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ENROLMENTID)));
					enrolement
							.setFirstName(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_FIRSTNAME)));
					enrolement.setIsActive(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ISACTIVE)));

					enrollist.add(enrolement);
				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			LOG.error("Exception while readStatusList : EnrolmentDataAccess "
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception while readStatusList : EnrolmentDataAccess "
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return enrollist;
	}

	public static List<String> readUnSyncList() throws SQLiteException,
			DataAccessException {
		List<String> unsyncList = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "select * from " + DbHandlerL.TABLE_MBS_ENROLMENT
					+ " where " + DbHandlerL.COL_MBS_ENRL_ISACTIVE + "='P'";
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {
					unsyncList.add(cursor2.getString(0));
				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {

			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return unsyncList;
	}

	public static int updateEnrolStatus(List<String> enrolIdList)
			throws SQLiteException, DataAccessException {
		int txnStatus = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			for (String enId : enrolIdList) {
				ContentValues updateValues = new ContentValues();
				updateValues.put(DbHandlerL.COL_MBS_ENRL_ISACTIVE, "S");
				updateValues.put(DbHandlerL.COL_MBS_ENRL_ISSYNCED, 1);

				txnStatus = db
						.update(DbHandlerL.TABLE_MBS_ENROLMENT, updateValues,
								DbHandlerL.COL_MBS_ENRL_ENROLMENTID + " = ?",
								new String[] { String.valueOf(enId) });
			}
		} catch (SQLiteException e) {
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return txnStatus;
	}

	public static Enrolement readCustomerEnrolmentData(String enrolId)
			throws SQLiteException, DataAccessException {
		Enrolement enrolement = null;

		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			String sql = "select * from " + DbHandlerL.TABLE_MBS_ENROLMENT
					+ " where " + DbHandlerL.COL_MBS_ENRL_ENROLMENTID + "='"
					+ enrolId + "'";
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {

					enrolement = new Enrolement();
					enrolement.setDob((cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_DOB))==null?null:Long.parseLong(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_DOB)))));
					enrolement.setGender(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_GENDER)));
					enrolement.setAddress1(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ADDRESS1)));
					enrolement.setAddress2(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ADDESS2)));
					enrolement.setAddress3(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ADDESS3)));
					enrolement.setAddress4(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ADDESS4)));
					enrolement
							.setContactNo(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_CONTACTNO)));
					enrolement
					.setLocationCode(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_LOCATIONCODE)));
					enrolement.setEmailId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_EMAILID)));
					enrolement.setCountry(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_COUNTRY)));
					enrolement.setState(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_STATE)));

					enrolement.setZipCode(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ZIPCODE)));
					enrolement.setCurrency(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_CURRENCY)));
					enrolement
							.setResidenceType(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_RESIDENCETYPE)));
					enrolement
							.setNationality(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_NATIONALID)));
					enrolement
							.setMartialStatus(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_MARTIALSTATUS)));
					enrolement
							.setProfession(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_PROFESSION)));
					enrolement
							.setProfessionRemark(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_PRFREMARK)));
					enrolement
							.setTxnInitTime(cursor2.getLong(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_TXNINITIME)));

					enrolement
							.setEnrollmentId(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ENROLMENTID)));
					enrolement
							.setFirstName(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_FIRSTNAME)));
					enrolement.setLastName(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_LASTNAME)));
					enrolement
							.setMiddleName(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_MIDDLENAME)));
					enrolement
					.setCustomerId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_CUSTOMERID)));
					enrolement
					.setDeviceId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_DEVICEID)));
					enrolement
					.setAgentId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_AGENTID)));
					enrolement
					.setIsKycOnly(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ISKYCONLY)));
					enrolement
					.setModuleCode(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_MODULECODE)));
					enrolement
					.setTxnCode(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_TXNCODE)));
					enrolement
							.setAccountType(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ACCOUNTTYPE)));
					enrolement
							.setGroupIndiviType(cursor2.getString(cursor2
									.getColumnIndex(DbHandlerL.COL_MBS_ENRL_GROUPINDIVIDUALTYPE)));
					enrolement
					.setPoc(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_POC)));
					enrolement
					.setTempGroupId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_TEMPGROUPID)));
					enrolement
					.setThumbImage(cursor2.getBlob(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_ENRL_THUMBIMAGE)));

					String sqldocument = "select * from "
							+ DbHandlerL.TABLE_MBS_ENROLMENT_DOCUMENT
							+ " where " + DbHandlerL.COL_MBS_ENRL_ENROLID
							+ "='" + enrolement.getEnrollmentId() + "'";
					Cursor cursordoc = db.rawQuery(sqldocument, null);
					if (cursordoc.moveToFirst()) {
						do {
							CustomerDocuments enrolDocuments = new CustomerDocuments();
							enrolDocuments
									.setEnrolmentId(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_ENROLID)));

							enrolDocuments
									.setKycImgCustomer(cursordoc.getBlob(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_IMG_CUSTOMER)));
							enrolDocuments
									.setKycId1Img(cursordoc.getBlob(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_1_IMG)));
							enrolDocuments
									.setKycId1Number(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_1_NUMBER)));
							enrolDocuments
									.setKycId1Type(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_1_TYPE)));
							enrolDocuments
									.setKycId1Proof(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_1_PROOF_TYPE)));
							enrolDocuments
									.setKycId2Img(cursordoc.getBlob(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_2_IMG)));
							enrolDocuments
									.setKycId2Number(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_2_NUMBER)));
							enrolDocuments
									.setKycId2Type(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_2_TYPE)));
							enrolDocuments
									.setKycId2Proof(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_2_PROOF_TYPE)));
							enrolDocuments
									.setKycId3Img(cursordoc.getBlob(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_3_IMG)));
							enrolDocuments
									.setKycId3Number(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_3_NUMBER)));
							enrolDocuments
									.setKycId3Type(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_3_TYPE)));
							enrolDocuments
									.setKycId3Proof(cursordoc.getString(cursordoc
											.getColumnIndex(DbHandlerL.COL_MBS_ENRL_KYC_ID_3_PROOF_TYPE)));
							

							enrolement.setCustomerDocs(enrolDocuments);
						} while (cursordoc.moveToNext());
					}

				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return enrolement;
	}
	public static CustomerBiometricDetails readEnrolmentBioData(String enrolId)
			throws SQLiteException, DataAccessException {
		CustomerBiometricDetails bio = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			String sql = "select * from " + DbHandlerL.TABLE_MBS_BIOMETRIC_DATA
					+ " where " + DbHandlerL.COL_MBS_BIO_ENROLID + "='"
					+ enrolId + "'";
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {
					bio = new CustomerBiometricDetails();
					bio.setEnrolmentId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_ENROLID)));
					
					bio.setRhlfTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHLF_TMPLTDATA)));
					bio.setRhrfTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHRF_TMPLTDATA)));
					bio.setRhmfTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHMF_TMPLTDATA)));
					bio.setRhifTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHIF_TMPLTDATA)));
					bio.setRhtfTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHTF_TMPLTDATA)));
					bio.setLhlfTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHLF_TMPLTDATA)));
					bio.setLhrfTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHRF_TMPLTDATA)));
					bio.setLhmfTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHMF_TMPLTDATA)));
					bio.setLhifTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHIF_TMPLTDATA)));
					bio.setLhtfTmpltData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHTF_TMPLTDATA)));
					
					bio.setRhlfScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHLF_SCANDATA)));
					bio.setRhrfScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHRF_SCANDATA)));
					bio.setRhmfScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHMF_SCANDATA)));
					bio.setRhifScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHIF_SCANDATA)));
					bio.setRhtfScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_RHTF_SCANDATA)));
					bio.setLhlfScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHLF_SCANDATA)));
					bio.setLhrfScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHRF_SCANDATA)));
					bio.setLhmfScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHMF_SCANDATA)));
					bio.setLhifScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHIF_SCANDATA)));
					bio.setLhtfScanData(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_BIO_LHTF_SCANDATA)));

				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return bio;
	}
	public static List<String> readlov(String employType)
			throws SQLiteException, DataAccessException {
		List<String> lovValues = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			String sql = "select " 
					+ DbHandlerL.COL_LOV_VALUE + " from "
					+ DbHandlerL.TABLE_MBS_LOV + " where "
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + "='" + employType
					+ "'";
			Cursor cursor2 = db.rawQuery(sql, null);
			
			if (cursor2.moveToFirst()) {
				do {
					String lov;
					lov = cursor2.getString(0);
					lovValues.add(lov);
				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return lovValues;
	}
	
	public static String readIntLovValue(String LovValue,String employType)
			throws SQLiteException, DataAccessException {
		String lovValues = "";
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			String sql = "select " + DbHandlerL.COL_LOV_INTVALUE +" from "
					+ DbHandlerL.TABLE_MBS_LOV + " where "
					+ DbHandlerL.COL_LOV_VALUE + "='" + LovValue + "' and "
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + "='" + employType
					+ "'";
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {
					
					lovValues = cursor2.getString(0);
				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return lovValues;
	}


	@Override
	public String getType() {
		return "EN";
	}
	public long insertBiometricData(CustomerBiometricDetails biometric) throws SQLiteException, DataAccessException {
		SQLiteDatabase db = null;
		long docStatus = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_MBS_BIO_ENROLID, biometric.getEnrolmentId());			
			values.put(DbHandlerL.COL_MBS_BIO_RHLF_TMPLTDATA, biometric.getRhlfTmpltData());
			values.put(DbHandlerL.COL_MBS_BIO_RHRF_TMPLTDATA, biometric.getRhrfTmpltData());
			values.put(DbHandlerL.COL_MBS_BIO_RHMF_TMPLTDATA, biometric.getRhmfTmpltData());
			values.put(DbHandlerL.COL_MBS_BIO_RHIF_TMPLTDATA, biometric.getRhifTmpltData());
			values.put(DbHandlerL.COL_MBS_BIO_RHTF_TMPLTDATA, biometric.getRhtfTmpltData());			
			values.put(DbHandlerL.COL_MBS_BIO_LHLF_TMPLTDATA, biometric.getLhlfTmpltData());
			values.put(DbHandlerL.COL_MBS_BIO_LHRF_TMPLTDATA, biometric.getLhrfTmpltData());
			values.put(DbHandlerL.COL_MBS_BIO_LHMF_TMPLTDATA, biometric.getLhmfTmpltData());
			values.put(DbHandlerL.COL_MBS_BIO_LHIF_TMPLTDATA, biometric.getLhifTmpltData());
			values.put(DbHandlerL.COL_MBS_BIO_LHTF_TMPLTDATA, biometric.getLhtfTmpltData());
			
			values.put(DbHandlerL.COL_MBS_BIO_RHLF_SCANDATA, biometric.getRhlfScanData());
			values.put(DbHandlerL.COL_MBS_BIO_RHRF_SCANDATA, biometric.getRhrfScanData());
			values.put(DbHandlerL.COL_MBS_BIO_RHMF_SCANDATA, biometric.getRhmfScanData());
			values.put(DbHandlerL.COL_MBS_BIO_RHIF_SCANDATA, biometric.getRhifScanData());
			values.put(DbHandlerL.COL_MBS_BIO_RHTF_SCANDATA, biometric.getRhtfScanData());			
			values.put(DbHandlerL.COL_MBS_BIO_LHRF_SCANDATA, biometric.getLhrfScanData());
			values.put(DbHandlerL.COL_MBS_BIO_LHMF_SCANDATA, biometric.getLhmfScanData());
			values.put(DbHandlerL.COL_MBS_BIO_LHIF_SCANDATA, biometric.getLhifScanData());
			values.put(DbHandlerL.COL_MBS_BIO_LHTF_SCANDATA, biometric.getLhtfScanData());
			
			
			
			db = DbHandlerL.getInstance().getWritableDatabase();
			docStatus = db.insert(DbHandlerL.TABLE_MBS_BIOMETRIC_DATA, DbHandlerL.COL_MBS_BIO_ENROLID, values);
			
		} catch (SQLiteException e) {
			LOG.error("Exception while insertBiometricData : EnrolmentDataAccess"
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception while insertBiometricData : EnrolmentDataAccess "
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return docStatus;
	}
	public static void insertLov() throws SQLiteException, DataAccessException {

		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();

		try {

			// Marital Status
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('MTS01','1','Martial Status','1')");

			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('MTS01','2','Married','2')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('MTS01','3','Single','3')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('MTS01','4','Widowed','4')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('MTS01','5','Divorced','5')");

			// Gender
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('GDR01','1','Male','1')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('GDR01','2','Female','2')");

			// List OF Documents
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','1','Select','1')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','2','Passport','2')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','3','National ID','3')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','4','Govt ID','4')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','5','Driving License','5')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','6','Birth Certificate','6')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','7','Aadhar Card','7')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','8','Resident Book','8')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('KYC01','9','Family Book','9')");

			// List oF Country

			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CN001','1','Country','1')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CN001','2','India','2')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CN001','3','Cambodia','3')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CN001','4','China','4')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CN001','5','Hong Kong','5')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CN001','6','Monaco','6')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CN001','7','Malaysia','7')");

			// list of Currency

			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CCY01','1','Currency','1')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CCY01','2','INR','2')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CCY01','3','KHR','3')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CCY01','4','EUR','4')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CCY01','5','USD','5')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CCY01','6','HKD','6')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CCY01','7','CNY','7')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('CCY01','8','MYR','8')");

			// list of Profession employmentType

			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','1','Profession','1')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','2','Engineer','2')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','3','Social Worker','3')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','4','Lawyer','4')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','5','Farmer','5')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','6','Accountant','6')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','7','Artist','7')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','8','Dentist','8')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('PRO01','9','Others','9')");

			// nationality
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('NAT01','1','Nationality','1')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('NAT01','2','Indian','2')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('NAT01','3','Cambodian','3')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('NAT01','4','Chinese','4')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('NAT01','5','Malaysian','5')");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_LOV + " ("
					+ DbHandlerL.COL_LOV_KEYIDENTIFIER + ","
					+ DbHandlerL.COL_LOV_INTVALUE + ","
					+ DbHandlerL.COL_LOV_VALUE + ","
					+ DbHandlerL.COL_LOV_ORDERS
					+ ") values ('NAT01','6','British','6')");

			db.close();
		} catch (SQLiteException e) {
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}

	}
	public int cntBiometric(String bioEnrollId) throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		int customerCount = 0;
		try {
			String sql = (" SELECT  COUNT(*)  FROM "
					+ DbHandlerL.TABLE_MBS_BIOMETRIC_DATA + " AG WHERE AG."
					+ DbHandlerL.COL_MBS_BIO_ENROLID + " ='"
					+ bioEnrollId
					+ "' ;");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				customerCount = (cursor.getString(0) == null ? 0 : Integer
						.parseInt(cursor.getString(0)));
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error("Exception while cntBiometric : EnrollDataAccess"
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception while cntBiometric : EnrollDataAccess "
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return customerCount;
	}
	
	public long updateBiometric(CustomerBiometricDetails biometric)
			throws DataAccessException {

		SQLiteDatabase db = null;
		db = DbHandlerL.getInstance().getWritableDatabase();
		long txnStatus = 0;

		String url =  "";
		ContentValues values = new ContentValues();
		values.put(DbHandlerL.COL_MBS_BIO_RHLF_TMPLTDATA, biometric.getRhlfTmpltData());
		values.put(DbHandlerL.COL_MBS_BIO_RHRF_TMPLTDATA, biometric.getRhrfTmpltData());
		values.put(DbHandlerL.COL_MBS_BIO_RHMF_TMPLTDATA, biometric.getRhmfTmpltData());
		values.put(DbHandlerL.COL_MBS_BIO_RHIF_TMPLTDATA, biometric.getRhifTmpltData());
		values.put(DbHandlerL.COL_MBS_BIO_RHTF_TMPLTDATA, biometric.getRhtfTmpltData());			
		values.put(DbHandlerL.COL_MBS_BIO_LHLF_TMPLTDATA, biometric.getLhlfTmpltData());
		values.put(DbHandlerL.COL_MBS_BIO_LHRF_TMPLTDATA, biometric.getLhrfTmpltData());
		values.put(DbHandlerL.COL_MBS_BIO_LHMF_TMPLTDATA, biometric.getLhmfTmpltData());
		values.put(DbHandlerL.COL_MBS_BIO_LHIF_TMPLTDATA, biometric.getLhifTmpltData());
		values.put(DbHandlerL.COL_MBS_BIO_LHTF_TMPLTDATA, biometric.getLhtfTmpltData());
		
		values.put(DbHandlerL.COL_MBS_BIO_RHLF_SCANDATA, biometric.getRhlfScanData());
		values.put(DbHandlerL.COL_MBS_BIO_RHRF_SCANDATA, biometric.getRhrfScanData());
		values.put(DbHandlerL.COL_MBS_BIO_RHMF_SCANDATA, biometric.getRhmfScanData());
		values.put(DbHandlerL.COL_MBS_BIO_RHIF_SCANDATA, biometric.getRhifScanData());
		values.put(DbHandlerL.COL_MBS_BIO_RHTF_SCANDATA, biometric.getRhtfScanData());			
		values.put(DbHandlerL.COL_MBS_BIO_LHRF_SCANDATA, biometric.getLhrfScanData());
		values.put(DbHandlerL.COL_MBS_BIO_LHMF_SCANDATA, biometric.getLhmfScanData());
		values.put(DbHandlerL.COL_MBS_BIO_LHIF_SCANDATA, biometric.getLhifScanData());
		values.put(DbHandlerL.COL_MBS_BIO_LHTF_SCANDATA, biometric.getLhtfScanData());
		
		url =  DbHandlerL.COL_MBS_BIO_ENROLID+ " = '"
				+ biometric.getEnrolmentId() + "'";
		
		txnStatus = db.update(DbHandlerL.TABLE_MBS_BIOMETRIC_DATA,values,url, null);
		
		return txnStatus;
	}
}
