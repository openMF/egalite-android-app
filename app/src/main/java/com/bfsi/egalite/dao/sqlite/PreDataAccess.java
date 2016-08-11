/**
This source is part of the MFI and is copyrighted by BFSI Consulting Pvt Ltd.

All rights reserved.  No part of this work may be reproduced, stored in a
retrieval system, adopted or transmitted in any form or by any means,
electronic, mechanical, photographic, graphic, optic recording or otherwise,
translated in any language or computer language, without the prior written
permission of BFSI Consulting Pvt Ltd.

BFSI Consulting Pvt Ltd,
No-309, Venkatesh Complex,
2nd & Ground floor, 100 ft road, 
1st stage, Indiranagar,
(Opp to Lebanese Restaurant)
BANGALORE - 560038,
India

Copyright 2013 BFSI Consulting Pvt Ltd.
 */

package com.bfsi.egalite.dao.sqlite;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.dao.StaticDaoL;
import com.bfsi.egalite.entity.Agent;
import com.bfsi.egalite.entity.Parameters;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.StringUtil;
import com.bfsi.mfi.rest.model.AgentAgenda;
import com.bfsi.mfi.rest.model.AgentDetail;
import com.bfsi.mfi.rest.model.BranchDetail;
import com.bfsi.mfi.rest.model.CashRecord;
import com.bfsi.mfi.rest.model.CurrencyDetail;
import com.bfsi.mfi.rest.model.CustomerAccount;
import com.bfsi.mfi.rest.model.CustomerDetail;
import com.bfsi.mfi.rest.model.Deposit;
import com.bfsi.mfi.rest.model.DeviceDetail;
import com.bfsi.mfi.rest.model.Loan;
import com.bfsi.mfi.rest.model.LoanPaidSch;
import com.bfsi.mfi.rest.model.Lov;
import com.bfsi.mfi.rest.model.Message;
import com.bfsi.mfi.rest.model.SmsTemplate;

/**
 * @author Administrator
 * 
 */
@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class PreDataAccess implements PreDataDao {

	private Logger LOG = LoggerFactory.getLogger(getClass());

	// public SimpleDateFormat formatter = CommonContexts.endateFormat;

	// Read agentvalues
	public Agent readAgentValues() throws SQLiteException, DataAccessException {
		Agent agent = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "select * from " + DbHandlerL.TABLE_MBS_AGENTS;
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {
					agent = new Agent();
					agent.setAgentId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_AGENT_ID)));
					agent.setPassword(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_PASSWORD)));
					agent.setAgentType(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_AGENT_TYPE)));
					agent.setCashLimit(cursor2.getLong(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_CASH_LIMIT)));
					agent.setDeviceId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_DEVICE_ID)));
					agent.setStatus(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_STATUS)));
					agent.setfName(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_FIRST_NAME)));
					agent.setlName(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_LAST_NAME)));
					agent.setLang(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_LANG)));
					agent.setAgencyCode(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_AGENCY_CODE)));
					agent.setChangePasswordDate(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_CHANGEPASSWORD_DATE)));
					agent.setCreditOfficer(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_CREDIT_OFFICER)));
					agent.setDob(cursor2.getLong(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_DOB)));
					agent.setGender(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_GENDER)));
					agent.setLocationCode(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_LOCATION_CODE)));
					agent.setAuthStatus(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_AUTH_STATUS)));
					agent.setDataKey(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_DATA_KEY)));
					agent.setRegKey(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_REG_KEY)));
					agent.setRegKeyExpDate(cursor2.getLong(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_REGKEY_EXP_DATE)));
					agent.setRegStatus(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_REG_STATUS)));
					agent.setPromptChangePassword(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_PROMPT_CHANGEPASSWORD)));
					agent.setUserName(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_USERNAME)));
					agent.setMacId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_AGENT_EVOLUTE_MAC_ID)));

				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READAGENTVALUES
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READAGENTVALUES
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return agent;
	}

	public void updateDeviceLastSynctime(String deviceId, long lastSyncTime)
			throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			ContentValues updateValues = new ContentValues();
			updateValues.put(DbHandlerL.COL_DEVICE_LAST_SYNC, lastSyncTime);

			db.update(DbHandlerL.TABLE_MBS_DEVICE_DETAIL, updateValues,
					DbHandlerL.COL_DEVICE_DEVICE_ID + " = ?",
					new String[] { deviceId });

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_UPDATEDEVICELASTSYNCTIME
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_UPDATEDEVICELASTSYNCTIME
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public int agendaCount() throws SQLiteException, DataAccessException {
		int count = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "select Count(*) from "
					+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " WHERE "
					+ DbHandlerL.COL_AGENDA_AGENDA_STATUS + "='0'";
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {
					count = Integer.parseInt((cursor2.getString(0)));

				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_AGENDACOUNT
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_AGENDACOUNT
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return count;
	}

	// Read Device data
	public DeviceDetail readDeviceValues() throws SQLiteException,
			DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		DeviceDetail device = null;
		try {

			String sql = "select * from " + DbHandlerL.TABLE_MBS_DEVICE_DETAIL;
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {
					device = new DeviceDetail();
					device.setId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_DEVICE_ID)));
					device.setDevicetype(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_DEVICE_TYPE)));
					device.setUniqueId(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_UNIQUE_ID)));
					device.setIssuedDate(Long.valueOf(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_ISSUED_DATE))));
					device.setLastSync(Long.valueOf(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_LAST_SYNC))));
					device.setIssuedBy(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_ISSUED_BY_CO)));
					device.setOperatingSystem(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_OS)));
					device.setAppVersion(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_APP_VERSION)));
					device.setSimNumber(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_DEVICE_SIM_NUMBER)));

				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READDEVICEVALUES
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READDEVICEVALUES
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return device;
	}

	// Parameter Data
	public void insertParameterData(String name, String value)
			throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			String query = "DELETE FROM " + DbHandlerL.TABLE_MBS_APP_PARAMETERS
					+ " WHERE " + DbHandlerL.COL_PARAM_NAME + "='" + name + "'";
			db.execSQL(query);
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_PARAM_NAME, name);
			values.put(DbHandlerL.COL_PARAM_VALUE, value);
			db.insert(DbHandlerL.TABLE_MBS_APP_PARAMETERS, null, values);

			db.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSERTPARAMDATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSERTPARAMDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertUserLoginData(String agentid, long lostlogin,
			int invalid, String synctime) throws SQLiteException,
			DataAccessException {
		try {
			SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_AGENT_AGENT_ID, agentid);
			values.put(DbHandlerL.COL_AGENT_LAST_LOGINTIME, lostlogin);
			values.put(DbHandlerL.COL_AGENT_NO_INVALID_LOGIN, invalid);
			values.put(DbHandlerL.COL_AGENT_LAST_SYNC_TIME, synctime);
			db.insert(DbHandlerL.TABLE_MBS_AGENTS, null, values);

			db.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_USERLOGINDATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_USERLOGINDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	// Update Userlogin data
	public void updateUserLogin(String agt_useriddb, long lastlogintime,
			int invalidcount) throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			ContentValues updateValues = new ContentValues();
			updateValues
					.put(DbHandlerL.COL_AGENT_LAST_LOGINTIME, lastlogintime);
			updateValues.put(DbHandlerL.COL_AGENT_NO_INVALID_LOGIN,
					invalidcount);
			db.update(DbHandlerL.TABLE_MBS_AGENTS, updateValues,
					DbHandlerL.COL_AGENT_AGENT_ID + " = ?",
					new String[] { agt_useriddb });

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_UPDT_USRLOGIN
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_UPDT_USRLOGIN
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}

	}

	public void updateLastSyncTime(String agentId, long syncTime)
			throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {

			ContentValues updateValues = new ContentValues();
			updateValues.put(DbHandlerL.COL_AGENT_LAST_SYNC_TIME, syncTime);
			db.update(DbHandlerL.TABLE_MBS_AGENTS, updateValues,
					DbHandlerL.COL_AGENT_AGENT_ID + " = ?",
					new String[] { agentId });

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_UPDT_LASTSYNC
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_UPDT_LASTSYNC
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	// Read Userlogin values
	public Agent readUserLoginValues() throws SQLiteException,
			DataAccessException {
		Agent login = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "select " + DbHandlerL.COL_AGENT_AGENT_ID + ","
					+ DbHandlerL.COL_AGENT_LAST_LOGINTIME + ","
					+ DbHandlerL.COL_AGENT_NO_INVALID_LOGIN + ","
					+ DbHandlerL.COL_AGENT_LAST_SYNC_TIME + " from "
					+ DbHandlerL.TABLE_MBS_AGENTS;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				login = new Agent();
				login.setAgentId(cursor.getString(0));
				login.setLastLoginTime(cursor.getString(1));
				login.setNoInvalidLogin(cursor.getString(2));
				login.setLastSyncTime(cursor.getString(3));

			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_USRLOGINVALUES
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_USRLOGINVALUES
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return login;
	}

	public String readLastSyncTime() throws SQLiteException,
			DataAccessException {
		String lastSyncTime = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "select " + DbHandlerL.COL_AGENT_LAST_SYNC_TIME
					+ " from " + DbHandlerL.TABLE_MBS_AGENTS;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				lastSyncTime = cursor.getString(0);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READLASTSYNC 
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READLASTSYNC 
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return lastSyncTime;
	}

	// Dynamic insert scripts
	/**
	 * Insert AgentData
	 * 
	 * @param agentData
	 */
	public void insertAgentsData(Agent agentData) throws SQLiteException,
			DataAccessException {
		StaticDaoL dao = DaoFactory.getStaticDao();
		dao.moduleCodes();
		
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_AGENT_AGENT_ID, agentData.getAgentId());
			values.put(DbHandlerL.COL_AGENT_PASSWORD, agentData.getPassword());
			values.put(DbHandlerL.COL_AGENT_PASSWORD_SALT,
					agentData.getPasswordSalt());
			values.put(DbHandlerL.COL_AGENT_AGENT_TYPE,
					agentData.getAgentType());
			values.put(DbHandlerL.COL_AGENT_CASH_LIMIT,
					agentData.getCashLimit());
			values.put(DbHandlerL.COL_AGENT_DEVICE_ID, agentData.getDeviceId());
			values.put(DbHandlerL.COL_AGENT_STATUS, agentData.getStatus());
			values.put(DbHandlerL.COL_AGENT_FIRST_NAME, agentData.getfName());
			values.put(DbHandlerL.COL_AGENT_LAST_NAME, agentData.getlName());
			values.put(DbHandlerL.COL_AGENT_LANG, agentData.getLang());
			values.put(DbHandlerL.COL_AGENT_AGENCY_CODE,
					agentData.getAgencyCode());
			values.put(DbHandlerL.COL_AGENT_CHANGEPASSWORD_DATE,
					agentData.getChangePasswordDate());
			values.put(DbHandlerL.COL_AGENT_CREDIT_OFFICER,
					agentData.getCreditOfficer());
			values.put(DbHandlerL.COL_AGENT_DOB, agentData.getDob());
			values.put(DbHandlerL.COL_AGENT_GENDER, agentData.getGender());
			values.put(DbHandlerL.COL_AGENT_LOCATION_CODE,
					agentData.getLocationCode());

			values.put(DbHandlerL.COL_AGENT_AUTH_STATUS,
					agentData.getAuthStatus());

			values.put(DbHandlerL.COL_AGENT_DATA_KEY, agentData.getDataKey());
			values.put(DbHandlerL.COL_AGENT_REG_KEY, agentData.getRegKey());
			values.put(DbHandlerL.COL_AGENT_REGKEY_EXP_DATE,
					agentData.getRegKeyExpDate());
			values.put(DbHandlerL.COL_AGENT_REG_STATUS,
					agentData.getRegStatus());
			values.put(DbHandlerL.COL_AGENT_PROMPT_CHANGEPASSWORD,
					agentData.getPromptChangePassword());
			values.put(DbHandlerL.COL_AGENT_ROLES_LIST,
					StringUtil.join(agentData.getAgentRoles(), ","));
			values.put(DbHandlerL.COL_AGENT_USERNAME, agentData.getUserName());
			values.put(DbHandlerL.COL_AGENT_EVOLUTE_MAC_ID, agentData.getMacId());

			db.insert(DbHandlerL.TABLE_MBS_AGENTS, null, values);

			if (agentData.getAgentRoles() != null
					&& agentData.getAgentRoles().size() > 0) {
				for (String id : agentData.getAgentRoles()) {
					ContentValues update = new ContentValues();
					update.put(DbHandlerL.COL_MODULE_IS_LICENSED, "'Y'");
					String url =  DbHandlerL.COL_MODULE_MODULE_CODE+ " = '"+ id + "'";
					int value = db.update(DbHandlerL.TABLE_MBS_MODULE_MASTER,update,url, null);
					System.out.println(value);
					System.out.println(value);
				}
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRT_AGENT
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRT_AGENT
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	/**
	 * Insert Device data
	 * 
	 * @param deviceDetails
	 */
	public void insertDeviceData(DeviceDetail deviceDetails)
			throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		
			
		try {
			if(deviceDetails != null){
				ContentValues values = new ContentValues();
				values.put(DbHandlerL.COL_DEVICE_DEVICE_ID, deviceDetails.getId());
				values.put(DbHandlerL.COL_DEVICE_DEVICE_TYPE,
						deviceDetails.getDevicetype());
				values.put(DbHandlerL.COL_DEVICE_ISSUED_DATE,
						deviceDetails.getIssuedDate());
				values.put(DbHandlerL.COL_DEVICE_LAST_SYNC,
						deviceDetails.getLastSync());
				values.put(DbHandlerL.COL_DEVICE_ISSUED_BY_CO,
						deviceDetails.getIssuedBy());
				values.put(DbHandlerL.COL_DEVICE_BRAND_MODEL,
						deviceDetails.getBrandModel());
				values.put(DbHandlerL.COL_DEVICE_OS,
						deviceDetails.getOperatingSystem());
				values.put(DbHandlerL.COL_DEVICE_APP_VERSION,
						deviceDetails.getAppVersion());
				values.put(DbHandlerL.COL_DEVICE_SIM_NUMBER,
						deviceDetails.getSimNumber());
				values.put(DbHandlerL.COL_DEVICE_UNIQUE_ID,
						deviceDetails.getUniqueId());
				String query = "DELETE FROM " + DbHandlerL.TABLE_MBS_DEVICE_DETAIL
						+ " WHERE " + DbHandlerL.COL_DEVICE_DEVICE_ID + "='"
						+ deviceDetails.getId() + "'";
				db.execSQL(query);
	
				long i = db
						.insert(DbHandlerL.TABLE_MBS_DEVICE_DETAIL, null, values);
				System.out.println(i);
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRT_DEV
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRT_DEV
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	/**
	 * Insert Paramets Data
	 * 
	 * @param parmeterData
	 *            public static String COL_PARAM_NAME= "NAME"; public static
	 *            String COL_PARAM_VALUE= "VALUE";
	 */
	public void insertParameterData(Parameters parmeterData)
			throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_PARAM_NAME, parmeterData.getName());
			values.put(DbHandlerL.COL_PARAM_VALUE, parmeterData.getValue());
			db.insert(DbHandlerL.TABLE_MBS_APP_PARAMETERS, null, values);
			db.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSERTPARAMDATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSERTPARAMDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	/**
	 * InsertCustomers Data
	 * 
	 * @param customerData
	 */
	public void insertCustomersData(List<CustomerDetail> CustomerDetails)
			throws SQLiteException, DataAccessException {

		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			if (CustomerDetails != null) {
				for (CustomerDetail customerData : CustomerDetails) {

					ContentValues values = new ContentValues();
					values.put(DbHandlerL.COL_CUST_CUSTOMER_ID,
							customerData.getCustomerId());
					values.put(DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME,
							customerData.getCustomerFullName());
					values.put(DbHandlerL.COL_CUST_CUSTOMER_SHORT_NAME,
							customerData.getCustomerShortName());
					values.put(DbHandlerL.COL_CUST_CUSTOMER_CATEGORY,
							customerData.getCustomerCategory());
					values.put(DbHandlerL.COL_CUST_GENDER,
							customerData.getGender());
					values.put(DbHandlerL.COL_CUST_DOB, customerData.getDob());

					values.put(DbHandlerL.COL_CUST_CUSTOMER_SINCE,
							customerData.getCustomerSince());
					values.put(DbHandlerL.COL_CUST_LOCATION_CODE,
							customerData.getLocationCode());
					values.put(DbHandlerL.COL_CUST_ADDRESS_LINE1,
							customerData.getAddressLine1());
					values.put(DbHandlerL.COL_CUST_ADDRESS_LINE2,
							customerData.getAddressLine2());
					values.put(DbHandlerL.COL_CUST_ADDRESS_LINE3,
							customerData.getAddressLine3());
					values.put(DbHandlerL.COL_CUST_ADDRESS_LINE4,
							customerData.getAddressLine4());
					values.put(DbHandlerL.COL_CUST_ZIP_CODE,
							customerData.getZipcode());
					values.put(DbHandlerL.COL_CUST_CITY,
							customerData.getCity());
					values.put(DbHandlerL.COL_CUST_COUNTRY,
							customerData.getCountry());
					values.put(DbHandlerL.COL_CUST_NATIONALITY,
							customerData.getNationality());
					values.put(DbHandlerL.COL_CUST_LOC_BRANCH_CODE,
							customerData.getLocalBranchCode());
					values.put(DbHandlerL.COL_CUST_PREFERRED_LANGUAGE,
							customerData.getPreferredLanguage());
					values.put(DbHandlerL.COL_CUST_EMAIL_ADDRESS,
							customerData.getEmailAddress());
					values.put(DbHandlerL.COL_CUST_SMS_REQUIRED,
							customerData.getSmsRequired());
					values.put(DbHandlerL.COL_CUST_MOBILE_NUMBER,
							customerData.getMobileNumber());
					values.put(DbHandlerL.COL_CUST_STATE,
							customerData.getState());
					values.put(DbHandlerL.COL_CUST_AGENT_ID,
							customerData.getAgentId());
					values.put(DbHandlerL.COL_CUST_CREDIT_OFFICER,
							customerData.getCreditOfficer());
					values.put(DbHandlerL.COL_CUST_COLLECT_KYC,
							customerData.getCollectKyc());
					values.put(DbHandlerL.COL_CUST_PARENT_CUST_ID,
							customerData.getParentCustId());
					values.put(DbHandlerL.COL_CUST_IS_PARENT_CUST,
							customerData.getIsParentCust());

					db.insert(DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL, null,
							values);
				}
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRCUST_DATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRCUST_DATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertCustomersAcc(List<CustomerAccount> CustomerAcc)
			throws SQLiteException, DataAccessException {

		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			if (CustomerAcc != null) {
				for (CustomerAccount customerAcc : CustomerAcc) {

					ContentValues values = new ContentValues();
					values.put(DbHandlerL.COL_CUSTACC_CUSTOMER_ID,
							customerAcc.getCustomerId());
					values.put(DbHandlerL.COL_CUSTACC_CUSTOMER_FULL_NAME,
							customerAcc.getCustFullName());
					values.put(DbHandlerL.COL_CUSTACC_CUST_AC_NO,
							customerAcc.getCustAcNo());
					values.put(DbHandlerL.COL_CUSTACC_BRANCH_CODE,
							customerAcc.getBranchCode());
					values.put(DbHandlerL.COL_CUSTACC_ACCOUNT_TYPE,
							customerAcc.getAccountType());
					values.put(DbHandlerL.COL_CUSTACC_AC_DESC,
							customerAcc.getAcDesc());
					values.put(DbHandlerL.COL_CUSTACC_AC_CCY,
							customerAcc.getAcCcy());
					values.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_DR,
							customerAcc.getAcStatNoDr());
					values.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_CR,
							customerAcc.getAcStatNoCr());
					values.put(DbHandlerL.COL_CUSTACC_AC_STAT_BLOCK,
							customerAcc.getAcStatBlock());
					values.put(DbHandlerL.COL_CUSTACC_AC_STAT_STOP_PAY,
							customerAcc.getAcStatStopPay());
					values.put(DbHandlerL.COL_CUSTACC_AC_STAT_DORMANT,
							customerAcc.getAcStatDorMant());
					values.put(DbHandlerL.COL_CUSTACC_AGENT_ID,
							customerAcc.getAgentId());
					values.put(DbHandlerL.COL_CUSTACC_LOCATION_CODE,
							customerAcc.getLocCode());
					values.put(DbHandlerL.COL_CUSTACC_CREDIT_OFFICER_ID,
							customerAcc.getCreditOficerId());

					db.insert(DbHandlerL.TABLE_MBS_CUSTACC_DETAIL, null, values);
				}
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRCUST_DATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRCUST_DATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertBranchData(List<BranchDetail> branchDetails)
			throws SQLiteException, DataAccessException {

		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			if (branchDetails != null) {
				for (BranchDetail branchData : branchDetails) {

					ContentValues values = new ContentValues();
					values.put(DbHandlerL.COL_BRANCH_CODE,
							branchData.getBranchCode());
					values.put(DbHandlerL.COL_BRANCH_DATE,
							branchData.getBranchDate());

					String query = "DELETE FROM "
							+ DbHandlerL.TABLE_MBS_BRANCH_DETAILS + " WHERE "
							+ DbHandlerL.COL_BRANCH_CODE + "='"
							+ branchData.getBranchCode() + "'";
					db.execSQL(query);

					db.insert(DbHandlerL.TABLE_MBS_BRANCH_DETAILS, null, values);
				}
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSR_BRCHDATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSR_BRCHDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertAgnDepositDetails(List<Deposit> depositDetails)
			throws SQLiteException, DataAccessException {

		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			if (depositDetails != null) {
				for (Deposit depositData : depositDetails) {

					ContentValues values = new ContentValues();

					values.put(DbHandlerL.COL_DEPOSIT_DEP_AC_NO,
							depositData.getDepositAccNo());
					values.put(DbHandlerL.COL_DEPOSIT_BRANCH_CODE,
							depositData.getBranchCode());
					values.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_ID,
							depositData.getCustomerId());
					values.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_NAME,
							depositData.getCustomerName());
					values.put(DbHandlerL.COL_DEPOSIT_AC_CCY,
							depositData.getAccCcy());
					values.put(DbHandlerL.COL_DEPOSIT_OPEN_DATE,
							depositData.getOpenDate());
					values.put(DbHandlerL.COL_DEPOSIT_MATURITY_DATE,
							depositData.getMaturityDate());
					values.put(DbHandlerL.COL_DEPOSIT_SCH_INSTALLMENT_AMT,
							depositData.getSchInstAmt());
					values.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ_TYPE,
							depositData.getPayFreqType());
					values.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ,
							depositData.getPayFreq());
					values.put(DbHandlerL.COL_DEPOSIT_TENURE_TYPE,
							depositData.getTenureType());
					values.put(DbHandlerL.COL_DEPOSIT_TENURE,
							depositData.getTenure());
					values.put(DbHandlerL.COL_DEPOSIT_INT_RATE,
							depositData.getIntRate());
					values.put(
							DbHandlerL.COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT,
							(depositData.getPrincMaturityAmt()));
					values.put(
							DbHandlerL.COL_DEPOSIT_INTEREST_ACCURED_TILL_DATE,
							depositData.getIntAccuredTillDate());
					values.put(
							DbHandlerL.COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE,
							depositData.getInstPaidTillDate());

					values.put(
							DbHandlerL.COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE,
							depositData.getTotAmtDue());
					values.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_PAYOUT_DATE,
							depositData.getRedemptionPayoutDate());
					values.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_AMOUNT,
							depositData.getRedemptionAmt());

					values.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_FLAG,
							depositData.getRedemptionFlag());

					values.put(DbHandlerL.COL_DEPOSIT_LOCATION_CODE,
							depositData.getLocCode());
					values.put(DbHandlerL.COL_DEPOSIT_AGENT_ID,
							depositData.getAgentId());
					values.put(DbHandlerL.COL_DEPOSIT_CREDIT_OFFICER_CODE,
							depositData.getCreditOfficeCode());
					db.insert(DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL, null,
							values);

				}
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRAGN_DEPOSDELT
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRAGN_DEPOSDELT
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	/**
	 * InsertLoans data
	 * 
	 * @param loanData
	 */
	public void insertLoansData(List<Loan> loanDetailsList)
			throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			if (loanDetailsList != null) {
				for (Loan loandetails : loanDetailsList) {

					ContentValues values = new ContentValues();
					
					values.put(DbHandlerL.COL_LOAN_LOAN_AC_NO, loandetails
							.getLoanAccNo().trim());
					values.put(DbHandlerL.COL_LOAN_BRANCH_CODE, loandetails
							.getBranchCode().trim());
					values.put(DbHandlerL.COL_LOAN_CUSTOMER_ID,
							loandetails.getCustomerId());
					values.put(DbHandlerL.COL_LOAN_CUSTOMER_NAME,
							loandetails.getCustomerName());
					values.put(DbHandlerL.COL_LOAN_CREDIT_OFFICER_CODE,
							loandetails.getCreditOfficerCode());

					values.put(DbHandlerL.COL_LOAN_LOCATION_CODE,
							loandetails.getLocCode());

					values.put(DbHandlerL.COL_LOAN_GROUP_ID,
							loandetails.getGroupId());
					values.put(DbHandlerL.COL_LOAN_SANCTIONED_DATE,
							loandetails.getSanctionedDate());
					values.put(DbHandlerL.COL_LOAN_LAST_DISBURSED_DATE,
							loandetails.getLastDisbursedDate());
					values.put(DbHandlerL.COL_LOAN_DISBR_TYPE,
							loandetails.getDisbrType());
					values.put(DbHandlerL.COL_LOAN_LOAN_AC_CCY,
							loandetails.getLoanAccCcy());
					values.put(DbHandlerL.COL_LOAN_IS_FULLY_DISBURSED,
							loandetails.getIsFullyDisbursed());
					values.put(DbHandlerL.COL_LOAN_SANCTIONED_PRINCIPAL_AMT,
							loandetails.getSanctPrincAmt());
					
					values.put(DbHandlerL.COL_LOAN_INTEREST_RATE,
							loandetails.getIntrRate());
					values.put(DbHandlerL.COL_LOAN_INTEREST_ACCURED,
							loandetails.getIntrAccured());
					values.put(DbHandlerL.COL_LOAN_PRINCIPAL_AMT_REPAID,
							loandetails.getPrincRepaidAmt());
					values.put(DbHandlerL.COL_LOAN_PRINCIPAL_OUTSTANDING,
							loandetails.getPrincOutstanding());
					values.put(DbHandlerL.COL_LOAN_LAST_REPAYMENT_DATE,
							loandetails.getLastRepayDate());

					values.put(DbHandlerL.COL_LOAN_IS_GROUP_LOAN,
							loandetails.getIsGrpLoan());
					values.put(DbHandlerL.COL_LOAN_IS_PARENT_LOAN,
							loandetails.getIsParentLoan());
					values.put(DbHandlerL.COL_LOAN_PARENT_CUST_ID,
							loandetails.getParentCustId());
					values.put(DbHandlerL.COL_LOAN_PARENT_LOAN_AC_NO,
							loandetails.getParentLoanAcNo());
					values.put(DbHandlerL.COL_LOAN_AGENT_ID,
							loandetails.getAgentId());
					
					values.put(DbHandlerL.COL_LOAN_DISBURSED_PRINCIPAL_AMT,
							loandetails.getDisbPrincAmt());
					
					for (LoanPaidSch loanPaidSchedule : loandetails
							.getLoanPaidSch()) {
						ContentValues values1 = new ContentValues();

						values1.put(DbHandlerL.COL_LOANPAID_CBS_AC_REF_NO,
								loanPaidSchedule.getCbsAcRefNo().trim());
						values1.put(DbHandlerL.COL_LOANPAID_BRANCH_CODE,
								loanPaidSchedule.getBranchCode());
						
						values1.put(DbHandlerL.COL_LOANPAID_SCH_DUE_DATE,
								loanPaidSchedule.getSchDueDate());
						values1.put(DbHandlerL.COL_LOANPAID_SCH_PAID_DATE,
								loanPaidSchedule.getSchPaidDate());
						values1.put(
								DbHandlerL.COL_LOANPAID_SETTLEMENT_CCY_CODE,
								loanPaidSchedule.getStlmtCcyCode());
						values1.put(DbHandlerL.COL_LOANPAID_AMT_SETTLED,
								loanPaidSchedule.getAmtSettled());
						values1.put(DbHandlerL.COL_LOANPAID_FULL_PARTIAL_IND,
								loanPaidSchedule.getFullPartialInd());
						db.insert(DbHandlerL.TABLE_MBS_AGN_LOAN_PAID_SCH, null,
								values1);
					}

					db.insert(DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL, null,
							values);
				}
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRLOANDATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRLOANDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	/**
	 * Insert Running cash
	 * 
	 * @param cashData
	 */

	public void insertCashRecordDetails(List<CashRecord> cashDataList)
			throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			for (CashRecord cashData : cashDataList) {

				ContentValues values = new ContentValues();
				values.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO, cashData
						.getEntrySeqNo().trim());
				values.put(DbHandlerL.COL_CASH_TXN_ID, cashData.getCashTxnId());
				values.put(DbHandlerL.COL_CASH_AGENDA_ID,
						cashData.getAgendaId());
				values.put(DbHandlerL.COL_CASH_TXN_SEQ_NO,
						cashData.getCashTxnSeqNo());
				values.put(DbHandlerL.COL_CASH_TXN_SOURCE,
						cashData.getTxnSource());
				values.put(DbHandlerL.COL_CASH_TXN_DATETIME,
						cashData.getTxnDateTime());
				values.put(DbHandlerL.COL_CASH_TXN_CODE, cashData.getTxnCode());
				values.put(DbHandlerL.COL_CASH_AGENT_ID, cashData.getAgentId());
				values.put(DbHandlerL.COL_CASH_DEVICE_ID, cashData
						.getDeviceId().trim());
				values.put(DbHandlerL.COL_CASH_AGN_SEQ_NO,
						cashData.getAgendaSeqNo());
				values.put(DbHandlerL.COL_CASH_TXN_CCY_CODE,
						cashData.getTxnCcy());
				values.put(DbHandlerL.COL_CASH_DR_CR_IND,
						cashData.getDrCrIndicator());
				values.put(DbHandlerL.COL_CASH_CASH_AMT, cashData.getAmount());
				values.put(DbHandlerL.COL_CASH_IS_REVERSAL,
						cashData.getIsReversal());
				values.put(DbHandlerL.COL_CASH_IS_DELETED,
						cashData.getIsDeleted());
				values.put(DbHandlerL.COL_CASH_AUTH_STAT,
						cashData.getAuthStatus());

				db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values);
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRRUNNGCASH
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRRUNNGCASH
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public int updatePassword(Agent agent) throws SQLiteException,
			DataAccessException {
		int status = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			ContentValues updateValues = new ContentValues();
			updateValues
					.put(DbHandlerL.COL_AGENT_PASSWORD, agent.getPassword());
			status = db.update(DbHandlerL.TABLE_MBS_AGENTS, updateValues,
					DbHandlerL.COL_AGENT_AGENT_ID + " = ?",
					new String[] { String.valueOf(agent.getAgentId()) });
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_UPDTPWD
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_UPDTPWD
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return status;// returns 0 on failure, returns postive values on success
	}

	@Override
	public List<Parameters> readAllParameterValues() throws SQLiteException,
			DataAccessException {
		List<Parameters> listParams = new ArrayList<Parameters>();
		Parameters parameter = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			String sql = "SELECT * FROM " + DbHandlerL.TABLE_MBS_APP_PARAMETERS;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				parameter = new Parameters();
				parameter.setName(cursor.getString(0));
				parameter.setValue(cursor.getString(1));
				listParams.add(parameter);
			}
			cursor.close();

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READALL_PARM
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READALL_PARM
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();

		}
		return listParams;
	}

	public Parameters readParameterValues(String name) throws SQLiteException,
			DataAccessException {
		Parameters parameter = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			String sql = "select " + DbHandlerL.COL_PARAM_NAME + ","
					+ DbHandlerL.COL_PARAM_VALUE + " from "
					+ DbHandlerL.TABLE_MBS_APP_PARAMETERS + " AP WHERE AP."
					+ DbHandlerL.COL_PARAM_NAME + " = '" + name + "'";
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				parameter = new Parameters();
				parameter.setName(cursor.getString(0));
				parameter.setValue(cursor.getString(1));

			}
			cursor.close();

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_PARAMVAL
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_PARAMVAL
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return parameter;
	}

	@SuppressWarnings("unused")
	private long getBranchDate(SQLiteDatabase db, String branchCode)
			throws ParseException {
		long dates = 0;
		String branchdate = "select " + DbHandlerL.COL_BRANCH_DATE + " from "
				+ DbHandlerL.TABLE_MBS_BRANCH_DETAILS + " where "
				+ DbHandlerL.COL_BRANCH_CODE + "='" + branchCode + "'";
		Cursor brCursor = db.rawQuery(branchdate, null);
		if (brCursor.moveToFirst()) {
			dates = Long.valueOf(brCursor.getString(0));
		}

		return dates;
	}

	public void updateAgentsData(AgentDetail agentDetails)
			throws SQLiteException, DataAccessException {
		StaticDaoL dao = DaoFactory.getStaticDao();
		dao.moduleCodes();
		
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {

			ContentValues updateValues = new ContentValues();
			updateValues.put(DbHandlerL.COL_AGENT_AGENT_ID,
					agentDetails.getId());
			updateValues.put(DbHandlerL.COL_AGENT_AGENT_TYPE,
					agentDetails.getAgentType());
			updateValues.put(DbHandlerL.COL_AGENT_CASH_LIMIT,
					agentDetails.getCashLimit());
			updateValues.put(DbHandlerL.COL_AGENT_FIRST_NAME,
					agentDetails.getFname());
			updateValues.put(DbHandlerL.COL_AGENT_LAST_NAME,
					agentDetails.getLname());
			updateValues.put(DbHandlerL.COL_AGENT_AGENCY_CODE,
					agentDetails.getAgencyCode());
			updateValues.put(DbHandlerL.COL_AGENT_LOCATION_CODE,
					agentDetails.getLocationCode());
			updateValues.put(DbHandlerL.COL_AGENT_ROLES_LIST,
					StringUtil.join(agentDetails.getAgentRoles(), ","));
			updateValues.put(DbHandlerL.COL_AGENT_EVOLUTE_MAC_ID,
					agentDetails.getMacId());

			int i = db.update(DbHandlerL.TABLE_MBS_AGENTS, updateValues,
					DbHandlerL.COL_AGENT_AGENT_ID + " = ?",
					new String[] { String.valueOf(agentDetails.getId()) });

			if (agentDetails.getAgentRoles() != null
					&& agentDetails.getAgentRoles().size() > 0) {
				for (String id : agentDetails.getAgentRoles()) {
					ContentValues update = new ContentValues();
					update.put(DbHandlerL.COL_MODULE_IS_LICENSED, "'Y'");
					String url =  DbHandlerL.COL_MODULE_MODULE_CODE+ " = '"+ id + "'";
					int value = db.update(DbHandlerL.TABLE_MBS_MODULE_MASTER,update,url, null);
					System.out.println(value);
					
				}
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_UPDTAGENTDATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_UPDTAGENTDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void updateAgentsPassword(byte[] salt, String password,
			String datakey, String changePasswordDate) throws SQLiteException,
			DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			ContentValues updateValues = new ContentValues();
			updateValues.put(DbHandlerL.COL_AGENT_PASSWORD, password);
			updateValues.put(DbHandlerL.COL_AGENT_DATA_KEY, datakey);
			updateValues.put(DbHandlerL.COL_AGENT_CHANGEPASSWORD_DATE,
					changePasswordDate);
			updateValues.put(DbHandlerL.COL_AGENT_PASSWORD_SALT, salt);
			db.update(DbHandlerL.TABLE_MBS_AGENTS, updateValues,
					DbHandlerL.COL_AGENT_AGENT_ID + " = ?",
					new String[] { String
							.valueOf(CommonContexts.mLOGINVALIDATION
									.getAgentId()) });
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_UPDTAGENTPWD
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_UPDTAGENTPWD
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void deleteDeviceData() throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		String query = "DELETE FROM " + DbHandlerL.TABLE_MBS_DEVICE_DETAIL;
		try {
			db.execSQL(query);
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_DELETEDEV_DATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_DELETEDEV_DATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void deleteParamsData() throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		String query = "DELETE FROM " + DbHandlerL.TABLE_MBS_APP_PARAMETERS;
		try {
			db.execSQL(query);
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_DELETEPARAMDATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_DELETEPARAMDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void deleteAllBranchCodes() throws SQLiteException,
			DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		String query = "DELETE FROM " + DbHandlerL.TABLE_MBS_BRANCH_DETAILS;
		try {
			db.execSQL(query);
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_DELBRCHCODE
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_DELBRCHCODE
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	@SuppressLint("NewApi")
	public String getDeviceId() throws SQLiteException, DataAccessException {

		String deviceId = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		String query = "Select " + DbHandlerL.COL_DEVICE_DEVICE_ID + " from "
				+ DbHandlerL.TABLE_MBS_DEVICE_DETAIL;
		try {
			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				deviceId = cursor.getString(0);
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_GETDEVID
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);

		} catch (Exception e) {
			LOG.error(Constants.EXCP_GETDEVID
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return deviceId;
	}

	@Override
	public void insertCurrency(List<CurrencyDetail> currencyList)
			throws DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			if(currencyList != null)
			{
				for (CurrencyDetail ccyList : currencyList) {
					ContentValues values = new ContentValues();
					values.put(DbHandlerL.COL_CCY_CCY_CODE,
							ccyList.getCurrencyCode());
					values.put(DbHandlerL.COL_CCY_CCY_NAME,
							ccyList.getCurrencyName());
					values.put(DbHandlerL.COL_CCY_ISO_CCY_CODE,
							ccyList.getIsoCcyCode());
					values.put(DbHandlerL.COL_CCY_ISO_ALT_CODE,
							ccyList.getIsoAltCcode());
					values.put(DbHandlerL.COL_CCY_COUNTRY, ccyList.getCountry());
					values.put(DbHandlerL.COL_CCY_ROUND_RULE,
							ccyList.getCcyRoundRule());
	
					values.put(DbHandlerL.COL_CCY_DECIMAL,
							ccyList.getCurrencyDecimals());
	
					String query = "DELETE FROM " + DbHandlerL.TABLE_MBS_CCY_CODES
							+ " WHERE " + DbHandlerL.COL_CCY_CCY_CODE + "='"
							+ ccyList.getCurrencyCode() + "'";
					db.execSQL(query);
					db.insert(DbHandlerL.TABLE_MBS_CCY_CODES, null, values);
				}
			}
		} catch (Exception e) {
			LOG.debug(Constants.ERR_INSR_CURRENCY
					+ e.getMessage());
			throw new DataAccessException(Constants.ERR_INSR_CURRENCY
					+ e.getMessage(), e);
		} finally {
			db.close();
		}

	}

	@Override
	public void insertMessageCodes(List<Message> message)
			throws DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			for (Message msg : message) {
				ContentValues values = new ContentValues();
				values.put(DbHandlerL.COL_MESSAGE_CODE, msg.getMsgCode());
				values.put(DbHandlerL.COL_MESSAGE_DESC, msg.getDesc());
				values.put(DbHandlerL.COL_MESSAGE_ERR_TYPE, msg.getErrorType());
				values.put(DbHandlerL.COL_MESSAGE_LANG, msg.getLang());
				values.put(DbHandlerL.COL_MESSAGE_MSG_TYPE, msg.getMsgType());
				values.put(DbHandlerL.COL_MESSAGE_SUB_SYS, msg.getSubSystem());
				db.insert(DbHandlerL.TABLE_MBS_MESSAGE_CODE, null, values);
			}
		} catch (Exception e) {
			LOG.debug(Constants.ERR_INSRMSG_CODE
					+ e.getMessage());
			throw new DataAccessException(Constants.ERR_INSRMSG_CODE
					+ e.getMessage(), e);
		} finally {
			db.close();
		}

	}

	@Override
	public void insertAgentAgenda(List<AgentAgenda> agenda)
			throws DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			for (AgentAgenda agna : agenda) {
				ContentValues values = new ContentValues();
				values.put(DbHandlerL.COL_AGENDA_AGENDA_ID, agna.getAgendaId());
				values.put(DbHandlerL.COL_AGENDA_SEQ_NO, agna.getSeqNo());
				values.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO,
						agna.getCbdAccRefNo());
				values.put(DbHandlerL.COL_AGENDA_MODULE_CODE,
						agna.getModuleCode());
				values.put(DbHandlerL.COL_AGENDA_TXN_CODE, agna.getTxnCode());
				values.put(DbHandlerL.COL_AGENDA_AGENT_ID, agna.getAgentId());
				values.put(DbHandlerL.COL_AGENDA_LOCATION_CODE,
						agna.getLocCode());
				values.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS,
						agna.getAgendaStatus());
				// values.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "2");
				values.put(DbHandlerL.COL_AGENDA_BRANCH_CODE,
						agna.getBranchCode());
				values.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID,
						agna.getCustomerId());
				values.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME,
						agna.getCustomerName());
				values.put(DbHandlerL.COL_AGENDA_CCY_CODE,
						agna.getAgendaCcyCode());
				values.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE,
						agna.getAgendaCmpStartDate());
				values.put(DbHandlerL.COL_AGENDA_CMP_END_DATE,
						agna.getAgendaCmpEndDate());
				values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT,
						agna.getAgendaAmount());
				values.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE,
						agna.getLoanDisbType());
				values.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH,
						agna.getIsLoanFutureSch());
				values.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION,
						agna.getIsLoanRedemption());
				values.put(DbHandlerL.COL_AGENDA_CMP_NAME,
						agna.getAgendaCmpName());
				values.put(DbHandlerL.COL_AGENDA_DEVICE_ID, agna.getDeviceId());
				values.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE,
						agna.getSmsMMobile());
				values.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN,
						agna.getIsGroupLoan());
				values.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN,
						agna.getIsParentLoan());
				values.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER,
						agna.getIsParentCust());
				values.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID,
						agna.getParentCustId());
				values.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
						agna.getParentCbsAcRefNo());

				String query = "DELETE FROM "
						+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " WHERE "
						+ DbHandlerL.COL_AGENDA_AGENDA_ID + "='"
						+ agna.getAgendaId() + "'";
				db.execSQL(query);
				db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values);
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRDSB_DATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRDSB_DATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}

	}

	@Override
	public void insertLov(List<Lov> lovs) throws DataAccessException {

//		StaticDaoL dao = DaoFactory.getStaticDao();
//		dao.moduleCodes();
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			if(lovs != null)
			{
				for (Lov lov : lovs) {
					ContentValues values = new ContentValues();
					values.put(DbHandlerL.COL_LOV_KEYIDENTIFIER, lov.getLovId());
					values.put(DbHandlerL.COL_LOV_INTVALUE, lov.getIntrValue());
					values.put(DbHandlerL.COL_LOV_VALUE, lov.getDisplayValue());
					values.put(DbHandlerL.COL_LOV_ORDERS, lov.getOrderBy());
	
					db.insert(DbHandlerL.TABLE_MBS_LOV, null, values);
				}
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSERT_AGENTDATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSERT_AGENTDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}

	}
	public void insertTemplates(List<SmsTemplate> listTemplt) throws DataAccessException {

		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			if(listTemplt != null)
			{
				for(SmsTemplate smstempl:listTemplt)
				{
					ContentValues values = new ContentValues();
					values.put(DbHandlerL.COL_MBS_SMS_TXN_CODE, smstempl.getTxnCode());
					values.put(DbHandlerL.COL_MBS_SMS_TXN_SMS, smstempl.getTxnSms());
					values.put(DbHandlerL.COL_MBS_SMS_DYN_FIELDS, smstempl.getDynFields());
					
					db.insert(DbHandlerL.TABLE_MBS_SMS_TEMPLATE, null, values);
				}
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSERT_AGENTDATA +e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSERT_AGENTDATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}finally {
			db.close();
		}
	}
	
	public SmsTemplate readSmsTemplate(String txnCode)throws SQLiteException ,DataAccessException {
		SmsTemplate template = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "select "+DbHandlerL.COL_MBS_SMS_TXN_SMS +" from " + DbHandlerL.TABLE_MBS_SMS_TEMPLATE+ " WHERE "
		+DbHandlerL.COL_MBS_SMS_TXN_CODE+"='"+txnCode+"'"/*"' AND "+DbHandlerL.COL_MBS_SMS_DYN_FIELDS+"='Y'"*/;
			Cursor cursor2 = db.rawQuery(sql, null);
			if (cursor2.moveToFirst()) {
				do {
					template = new SmsTemplate();
					template.setTxnSms(cursor2.getString(cursor2
							.getColumnIndex(DbHandlerL.COL_MBS_SMS_TXN_SMS)));
					
				} while (cursor2.moveToNext());
			}
			cursor2.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READAGENTVALUES  +e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READAGENTVALUES
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}finally {
			db.close();
		}
		return template;

	}
	
}