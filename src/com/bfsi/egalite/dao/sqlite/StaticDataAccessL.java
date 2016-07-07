package com.bfsi.egalite.dao.sqlite;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.dao.StaticDaoL;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;

@SuppressLint("NewApi")
public class StaticDataAccessL implements StaticDaoL {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	public static SQLiteDatabase db;
	public SimpleDateFormat formatter = CommonContexts.endateFormat;
	String j[] = { "ADAM MILLER ", "JOHN DICE  ", "VICTOR D", "NANCY K ",
			"ALEX MULLER", "GRAHAM MIKE  ", "MICHAEL K", "NOAH MILTON",
			"DANNY T" };

	@SuppressLint("NewApi")
	public void insertDsbData() throws SQLiteException, DataAccessException {
		// inserting dsb data
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L01000062698");
			values.put(DbHandlerL.COL_AGENDA_SEQ_NO, "11");
			values.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LBGRPDISBR100");
			values.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L01");
			values.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000001");
			values.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Alastair Brown");
			values.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "25000");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "M");
			values.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "Y");
			values.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "N");
			values.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values.put(DbHandlerL.COL_AGENDA_CMP_NAME, "PRINCIPAL");
			values.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LBGRPDISBR001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L01000062714");
			values1.put(DbHandlerL.COL_AGENDA_SEQ_NO, "12");
			values1.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LBINDDISBR100");
			values1.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values1.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L01");
			values1.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values1.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values1.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values1.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000002");
			values1.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Babak Fahami");
			values1.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values1.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values1.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "5000");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values1.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "M");
			values1.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values1.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values1.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values1.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "N");
			values1.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "Y");
			values1.put(DbHandlerL.COL_AGENDA_CMP_NAME, "PRINCIPAL");
			values1.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values1.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values1.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LBGRPDISBR001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L01000062699");
			values2.put(DbHandlerL.COL_AGENDA_SEQ_NO, "13");
			values2.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LBGRPDISBR101");
			values2.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values2.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L01");
			values2.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values2.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values2.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values2.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000003");
			values2.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Bailey George");
			values2.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values2.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values2.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "5000");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values2.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "A");
			values2.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values2.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values2.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values2.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "N");
			values2.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values2.put(DbHandlerL.COL_AGENDA_CMP_NAME, "PRINCIPAL");
			values2.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values2.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values2.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LBGRPDISBR001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L01000062701");
			values3.put(DbHandlerL.COL_AGENDA_SEQ_NO, "14");
			values3.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LBGRPDISBR103");
			values3.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values3.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L01");
			values3.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values3.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values3.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values3.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000004");
			values3.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Brian Corner");
			values3.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values3.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values3.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values3.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "2300");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values3.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "A");
			values3.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values3.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values3.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values3.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "Y");
			values3.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "Y");
			values3.put(DbHandlerL.COL_AGENDA_CMP_NAME, "PRINCIPAL");
			values3.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values3.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values3.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LBGRPDISBR001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L01000062700");
			values4.put(DbHandlerL.COL_AGENDA_SEQ_NO, "15");
			values4.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LBGRPDISBR102");
			values4.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values4.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L01");
			values4.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values4.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values4.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values4.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000005");
			values4.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Albert John");
			values4.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values4.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values4.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "25000");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values4.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "M");
			values4.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values4.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values4.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "Y");
			values4.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "Y");
			values4.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "Y");
			values4.put(DbHandlerL.COL_AGENDA_CMP_NAME, "PRINCIPAL");
			values4.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values4.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values4.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LBGRPDISBR001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values4);

			db.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRDSB_DATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRDSB_DATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}

	}

	public void insertAgendaCash() throws SQLiteException, DataAccessException {
		try {
			long value=0L;
			db = DbHandlerL.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO, "AGN28314L02000062730");
			values.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY001");
			values.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			//2014-11-12
			values.put(DbHandlerL.COL_CASH_TXN_CODE, "T01");
			values.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "11100001");
			values.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "INR");
			values.put(DbHandlerL.COL_CASH_DR_CR_IND, "");
			values.put(DbHandlerL.COL_CASH_CASH_AMT, "100000");
			values.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");

			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L02000062731");
			values1.put(DbHandlerL.COL_CASH_TXN_ID, "T02");
			values1.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY001");
			values1.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values1.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values1.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values1.put(DbHandlerL.COL_CASH_TXN_CODE, "T02");
			values1.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values1.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values1.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "11100002");
			values1.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "INR");
			values1.put(DbHandlerL.COL_CASH_DR_CR_IND, "");
			values1.put(DbHandlerL.COL_CASH_CASH_AMT, "10000");
			values1.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values1.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values1.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L02000062732");
			values2.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values2.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY001");
			values2.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values2.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values2.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values2.put(DbHandlerL.COL_CASH_TXN_CODE, "T05");
			values2.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values2.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values2.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "11100003");
			values2.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "INR");
			values2.put(DbHandlerL.COL_CASH_DR_CR_IND, "");
			values2.put(DbHandlerL.COL_CASH_CASH_AMT, "10000");
			values2.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values2.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values2.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L02000062733");
			values3.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values3.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY001");
			values3.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values3.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values3.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values3.put(DbHandlerL.COL_CASH_TXN_CODE, "T04");
			values3.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values3.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values3.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "11100004");
			values3.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "INR");
			values3.put(DbHandlerL.COL_CASH_DR_CR_IND, "D");
			values3.put(DbHandlerL.COL_CASH_CASH_AMT, "900");
			values3.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values3.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values3.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L0202062734");
			values4.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values4.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPRE21001");
			values4.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values4.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values4.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values4.put(DbHandlerL.COL_CASH_TXN_CODE, "T10");
			values4.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values4.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values4.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "11100005");
			values4.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "INR");
			values4.put(DbHandlerL.COL_CASH_DR_CR_IND, "C");
			values4.put(DbHandlerL.COL_CASH_CASH_AMT, "10");
			values4.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values4.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values4.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values4);

			ContentValues values5 = new ContentValues();
			values5.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L02000062735");
			values5.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values5.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY001");
			values5.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values5.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values5.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values5.put(DbHandlerL.COL_CASH_TXN_CODE, "T07");
			values5.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values5.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values5.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "11100006");
			values5.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "INR");
			values5.put(DbHandlerL.COL_CASH_DR_CR_IND, "D");
			values5.put(DbHandlerL.COL_CASH_CASH_AMT, "10");
			values5.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values5.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values5.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values5);
			

			ContentValues values6 = new ContentValues();
			values6.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO, "AGN28314L020062730");
			values6.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values6.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY01");
			values6.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values6.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values6.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values6.put(DbHandlerL.COL_CASH_TXN_CODE, "T01");
			values6.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values6.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values6.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "110010004");
			values6.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "USD");
			values6.put(DbHandlerL.COL_CASH_DR_CR_IND, "");
			values6.put(DbHandlerL.COL_CASH_CASH_AMT, "99000");
			values6.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values6.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values6.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");

			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values6);

			ContentValues values7 = new ContentValues();
			values7.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L020062731");
			values7.put(DbHandlerL.COL_CASH_TXN_ID, "T02");
			values7.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY02");
			values7.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values7.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values7.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values7.put(DbHandlerL.COL_CASH_TXN_CODE, "T02");
			values7.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values7.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values7.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "110010003");
			values7.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "USD");
			values7.put(DbHandlerL.COL_CASH_DR_CR_IND, "");
			values7.put(DbHandlerL.COL_CASH_CASH_AMT, "9000");
			values7.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values7.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values7.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values7);

			ContentValues values8 = new ContentValues();
			values8.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L020062732");
			values8.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values8.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY03");
			values8.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values8.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values8.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values8.put(DbHandlerL.COL_CASH_TXN_CODE, "T05");
			values8.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values8.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values8.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "110010002");
			values8.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "USD");
			values8.put(DbHandlerL.COL_CASH_DR_CR_IND, "");
			values8.put(DbHandlerL.COL_CASH_CASH_AMT, "1000");
			values8.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values8.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values8.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values8);

			ContentValues values9 = new ContentValues();
			values9.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L020062733");
			values9.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values9.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY04");
			values9.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values9.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values9.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values9.put(DbHandlerL.COL_CASH_TXN_CODE, "T04");
			values9.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values9.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values9.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "110010001");
			values9.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "USD");
			values9.put(DbHandlerL.COL_CASH_DR_CR_IND, "D");
			values9.put(DbHandlerL.COL_CASH_CASH_AMT, "9000");
			values9.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values9.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values9.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values9);

			ContentValues values10 = new ContentValues();
			values10.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L020062734");
			values10.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values10.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY05");
			values10.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values10.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values10.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values10.put(DbHandlerL.COL_CASH_TXN_CODE, "T10");
			values10.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values10.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values10.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "110010000");
			values10.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "USD");
			values10.put(DbHandlerL.COL_CASH_DR_CR_IND, "C");
			values10.put(DbHandlerL.COL_CASH_CASH_AMT, "100");
			values10.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values10.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values10.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values10);

			ContentValues values11 = new ContentValues();
			values11.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
					"AGN28314L020062735");
			values11.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
			values11.put(DbHandlerL.COL_CASH_AGENDA_ID, "LNGRPREPAY06");
			values11.put(DbHandlerL.COL_CASH_TXN_SOURCE, "LN");
			values11.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "L02");
			values11.put(DbHandlerL.COL_CASH_TXN_DATETIME, "1412879400000");
			values11.put(DbHandlerL.COL_CASH_TXN_CODE, "T07");
			values11.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
			values11.put(DbHandlerL.COL_CASH_DEVICE_ID, CommonContexts.DEVICEID);
			values11.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "110010010");
			values11.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "USD");
			values11.put(DbHandlerL.COL_CASH_DR_CR_IND, "D");
			values11.put(DbHandlerL.COL_CASH_CASH_AMT, "120");
			values11.put(DbHandlerL.COL_CASH_IS_REVERSAL, "N");
			values11.put(DbHandlerL.COL_CASH_IS_DELETED, "N");
			values11.put(DbHandlerL.COL_CASH_AUTH_STAT, "A");
			value=db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values11);


		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRAGENDACASH
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRAGENDACASH
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertRepayData() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L02000062730");
			values.put(DbHandlerL.COL_AGENDA_SEQ_NO, "16");
			values.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LNGRPREPAY001");
			values.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L02");
			values.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000001");
			values.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Alastair");
			values.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values.put(DbHandlerL.COL_AGENDA_LCY_CODE, "N");
			values.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "1334.2875");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "N");
			values.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "Y");
			values.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "Y");
			values.put(DbHandlerL.COL_AGENDA_CMP_NAME, "EMI");
			values.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LNGRPREPAY001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L02000062738");
			values1.put(DbHandlerL.COL_AGENDA_SEQ_NO, "17");
			values1.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LNGRPREPAY101");
			values1.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values1.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L02");
			values1.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values1.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, 0);
			values1.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values1.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000001");
			values1.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Alastair");
			values1.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values1.put(DbHandlerL.COL_AGENDA_LCY_CODE, "N");
			values1.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "1334.2875");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "N");
			values1.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values1.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values1.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values1.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "Y");
			values1.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "Y");
			values1.put(DbHandlerL.COL_AGENDA_CMP_NAME, "EMI");
			values1.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values1.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values1.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values1.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LNGRPREPAY001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L02000062739");
			values2.put(DbHandlerL.COL_AGENDA_SEQ_NO, "18");
			values2.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LNGRPREPAY102");
			values2.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values2.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L02");
			values2.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values2.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, 0);
			values2.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values2.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000002");
			values2.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Babak Fahami");
			values2.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values2.put(DbHandlerL.COL_AGENDA_LCY_CODE, "N");
			values2.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "1334.2875");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "N");
			values2.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values2.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values2.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values2.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "Y");
			values2.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "Y");
			values2.put(DbHandlerL.COL_AGENDA_CMP_NAME, "EMI");
			values2.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values2.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values2.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values2.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LNGRPREPAY001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L02000062740");
			values3.put(DbHandlerL.COL_AGENDA_SEQ_NO, "19");
			values3.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LNGRPREPAY103");
			values3.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values3.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L21");
			values3.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values3.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, 0);
			values3.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values3.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000003");
			values3.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Bailey");
			values3.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values3.put(DbHandlerL.COL_AGENDA_LCY_CODE, "N");
			values3.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values3.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "1334.2875");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "N");
			values3.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values3.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values3.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values3.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "Y");
			values3.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "Y");
			values3.put(DbHandlerL.COL_AGENDA_CMP_NAME, "EMI");
			values3.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values3.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values3.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values3.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LNGRPREPAY001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314L02000062741");
			values4.put(DbHandlerL.COL_AGENDA_SEQ_NO, "20");
			values4.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "LNGRPREPAY104");
			values4.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "LN");
			values4.put(DbHandlerL.COL_AGENDA_TXN_CODE, "L21");
			values4.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values4.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, 0);
			values4.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values4.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000004");
			values4.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Brian Corner");
			values4.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values4.put(DbHandlerL.COL_AGENDA_LCY_CODE, "N");
			values4.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "1334.2875");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "N");
			values4.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values4.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values4.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values4.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "Y");
			values4.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "Y");
			values4.put(DbHandlerL.COL_AGENDA_CMP_NAME, "EMI");
			values4.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values4.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values4.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values4.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"LNGRPREPAY001");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values4);
			
			
			

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRAGENDACASH
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRAGENDACASH
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertCollectionData() throws SQLiteException,
			DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();

			values.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D01000063018");
			values.put(DbHandlerL.COL_AGENDA_SEQ_NO, "21");
			values.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0001");
			values.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D01");
			values.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000001");
			values.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Alastair");
			values.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "500");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Collection");
			values.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "28182192821");
			values.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28182192821");
			values.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"28182192821");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D01000063019");
			values1.put(DbHandlerL.COL_AGENDA_SEQ_NO, "2");
			values1.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0002");
			values1.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values1.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D01");
			values1.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values1.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values1.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values1.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000002");
			values1.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Babak Fahami");
			values1.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values1.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values1.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "500");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values1.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values1.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Collection");
			values1.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values1.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values1.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values1.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "28182192821");
			values1.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values1.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values1.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28182192821");
			values1.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"28182192821");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D01000063020");
			values2.put(DbHandlerL.COL_AGENDA_SEQ_NO, "3");
			values2.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0003");
			values2.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values2.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D01");
			values2.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values2.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values2.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values2.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000003");
			values2.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Bailey");
			values2.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values2.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values2.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "500");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values2.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values2.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Collection");
			values2.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values2.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values2.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values2.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "28182192821");
			values2.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values2.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values2.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28182192821");
			values2.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"28182192821");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D01000063021");
			values3.put(DbHandlerL.COL_AGENDA_SEQ_NO, "4");
			values3.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0004");
			values3.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values3.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D01");
			values3.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values3.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values3.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values3.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000004");
			values3.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Brian Corner");
			values3.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values3.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values3.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values3.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "500");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values3.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values3.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Collection");
			values3.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values3.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values3.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values3.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "28182192821");
			values3.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values3.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values3.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28182192821");
			values3.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"28182192821");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D01000063022");
			values4.put(DbHandlerL.COL_AGENDA_SEQ_NO, "5");
			values4.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0005");
			values4.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values4.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D01");
			values4.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values4.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values4.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values4.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000005");
			values4.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Albert");
			values4.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values4.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values4.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "5000");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values4.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values4.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Collection");
			values4.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values4.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values4.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "N");
			values4.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "28182192821");
			values4.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values4.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values4.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28182192821");
			values4.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO,
					"28182192821");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values4);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRCOLL_DARA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRCOLL_DARA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertPaymentsData() throws SQLiteException,
			DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();

			values.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D02000063030");
			values.put(DbHandlerL.COL_AGENDA_SEQ_NO, "6");
			values.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0013");
			values.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D02");
			values.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000001");
			values.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Alastair Brown");
			values.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "60000");
			values.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Maturity Payout");
			values.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "N");
			values.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28548745");
			values.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO, "N");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values);

			ContentValues values1 = new ContentValues();

			values1.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D02000063031");
			values1.put(DbHandlerL.COL_AGENDA_SEQ_NO, "7");
			values1.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0014");
			values1.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values1.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D02");
			values1.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values1.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values1.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values1.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000002");
			values1.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Babak Fahami");
			values1.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values1.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values1.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "60000");
			values1.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values1.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values1.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values1.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values1.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Maturity Payout");
			values1.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values1.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values1.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values1.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "N");
			values1.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values1.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28548745");
			values1.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO, "N");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D03000063032");
			values2.put(DbHandlerL.COL_AGENDA_SEQ_NO, "8");
			values2.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0015");
			values2.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values2.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D03");
			values2.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values2.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values2.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values2.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000003");
			values2.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Bailey");
			values2.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values2.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values2.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "60000");
			values2.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values2.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values2.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values2.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "Y");
			values2.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Redemption Payout");
			values2.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values2.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values2.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values2.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "N");
			values2.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values2.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28548745");
			values2.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO, "N");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D03000063033");
			values3.put(DbHandlerL.COL_AGENDA_SEQ_NO, "9");
			values3.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0016");
			values3.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values3.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D03");
			values3.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values3.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values3.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values3.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000004");
			values3.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Brian Corner");
			values3.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values3.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values3.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values3.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "60000");
			values3.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values3.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values3.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values3.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "Y");
			values3.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Redemption Payout");
			values3.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values3.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values3.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values3.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "N");
			values3.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values3.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28548745");
			values3.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO, "N");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_ID, "AGN28314D02000063029");
			values4.put(DbHandlerL.COL_AGENDA_SEQ_NO, "10");
			values4.put(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO, "DEP0012");
			values4.put(DbHandlerL.COL_AGENDA_MODULE_CODE, "DP");
			values4.put(DbHandlerL.COL_AGENDA_TXN_CODE, "D02");
			values4.put(DbHandlerL.COL_AGENDA_AGENT_ID, "AGT0001");
			values4.put(DbHandlerL.COL_AGENDA_LOCATION_CODE, "LCN00001");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
			values4.put(DbHandlerL.COL_AGENDA_BRANCH_CODE, "000");
			values4.put(DbHandlerL.COL_AGENDA_CUSTOMER_ID, "110000005");
			values4.put(DbHandlerL.COL_AGENDA_CUSTOMER_NAME, "Albert John");
			values4.put(DbHandlerL.COL_AGENDA_CCY_CODE, "INR");
			values4.put(DbHandlerL.COL_AGENDA_LCY_CODE, "INR");
			values4.put(DbHandlerL.COL_AGENDA_CMP_ST_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_AGENDA_CMP_END_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_AMT, "60000");
			values4.put(DbHandlerL.COL_AGENDA_AGENDA_AMT_LCY, "INR");
			values4.put(DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE, "X");
			values4.put(DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH, "N");
			values4.put(DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION, "N");
			values4.put(DbHandlerL.COL_AGENDA_CMP_NAME, "Maturity Payout");
			values4.put(DbHandlerL.COL_AGENDA_DEVICE_ID, "DEV00001");
			values4.put(DbHandlerL.COL_AGENDA_SMS_M_MOBILE, "9900112233");
			values4.put(DbHandlerL.COL_AGENDA_IS_GROUP_LOAN, "Y");
			values4.put(DbHandlerL.COL_AGENDA_IS_PARENT_LOAN, "N");
			values4.put(DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER, "N");
			values4.put(DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID, "28548745");
			values4.put(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO, "N");
			db.insert(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, values4);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRPAYMENT_DATA
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRPAYMENT_DATA
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertloanPaidSchData() throws SQLiteException,
			DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();
			for (int i = 0; i < 9; i++) {
				ContentValues values = new ContentValues();

				values.put(DbHandlerL.COL_LOANPAID_CBS_AC_REF_NO,
						"LBGRPDISBR10" + i);
				values.put(DbHandlerL.COL_LOANPAID_BRANCH_CODE, "LPB000" + i);
				values.put(DbHandlerL.COL_LOANPAID_SCH_DUE_DATE, "1412879400000 "
						);
				values.put(DbHandlerL.COL_LOANPAID_SCH_PAID_DATE, "1412879400000"
						);
				values.put(DbHandlerL.COL_LOANPAID_SETTLEMENT_CCY_CODE, "INR");
				values.put(DbHandlerL.COL_LOANPAID_AMT_SETTLED, "600" + i);
				values.put(DbHandlerL.COL_LOANPAID_FULL_PARTIAL_IND, "FULORPAR");

				db.insert(DbHandlerL.TABLE_MBS_AGN_LOAN_PAID_SCH, null, values);
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSTLOANPAIDSCH
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSTLOANPAIDSCH
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertCustDetail() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_CUST_CUSTOMER_ID, "110000001");
			values.put(DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME, "Alastair Brown");
			values.put(DbHandlerL.COL_CUST_CUSTOMER_SHORT_NAME, "Alastair");
			values.put(DbHandlerL.COL_CUST_CUSTOMER_CATEGORY, "I");
			values.put(DbHandlerL.COL_CUST_GENDER, "M");
			values.put(DbHandlerL.COL_CUST_DOB, "1412879400000");
			values.put(DbHandlerL.COL_CUST_CUSTOMER_SINCE, "647634600000");
			values.put(DbHandlerL.COL_CUST_LOCATION_CODE, "LCN00001");
			values.put(DbHandlerL.COL_CUST_ADDRESS_LINE1, "ADDR1 ");
			values.put(DbHandlerL.COL_CUST_ADDRESS_LINE2, "ADDR2");
			values.put(DbHandlerL.COL_CUST_ADDRESS_LINE3, "ADDR3");
			values.put(DbHandlerL.COL_CUST_ADDRESS_LINE4, "ADDR4");
			values.put(DbHandlerL.COL_CUST_ZIP_CODE, "560050");
			values.put(DbHandlerL.COL_CUST_CITY, "IN");
			values.put(DbHandlerL.COL_CUST_STATE, "Karnataka");
			values.put(DbHandlerL.COL_CUST_COUNTRY, "IN");
			values.put(DbHandlerL.COL_CUST_NATIONALITY, " IN");
			values.put(DbHandlerL.COL_CUST_LOC_BRANCH_CODE, " 000");
			values.put(DbHandlerL.COL_CUST_PREFERRED_LANGUAGE, "EN");
			values.put(DbHandlerL.COL_CUST_EMAIL_ADDRESS, "");
			values.put(DbHandlerL.COL_CUST_SMS_REQUIRED, "");
			values.put(DbHandlerL.COL_CUST_MOBILE_NUMBER, "9900112233");
			values.put(DbHandlerL.COL_CUST_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_CUST_CREDIT_OFFICER, "EGAUSER1");
			values.put(DbHandlerL.COL_CUST_COLLECT_KYC, "Y");
			values.put(DbHandlerL.COL_CUST_PARENT_CUST_ID, "110000001");
			values.put(DbHandlerL.COL_CUST_IS_PARENT_CUST, "Y");
			db.insert(DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_CUST_CUSTOMER_ID, "110000002");
			values1.put(DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME, "Babak Fahami");
			values1.put(DbHandlerL.COL_CUST_CUSTOMER_SHORT_NAME, "Babak");
			values1.put(DbHandlerL.COL_CUST_CUSTOMER_CATEGORY, "I");
			values1.put(DbHandlerL.COL_CUST_GENDER, "M");
			values1.put(DbHandlerL.COL_CUST_DOB, "1412879400000");
			values1.put(DbHandlerL.COL_CUST_CUSTOMER_SINCE, "647634600000");
			values1.put(DbHandlerL.COL_CUST_LOCATION_CODE, "LCN00001");
			values1.put(DbHandlerL.COL_CUST_ADDRESS_LINE1, "ADDR1 ");
			values1.put(DbHandlerL.COL_CUST_ADDRESS_LINE2, "ADDR2");
			values1.put(DbHandlerL.COL_CUST_ADDRESS_LINE3, "ADDR3");
			values1.put(DbHandlerL.COL_CUST_ADDRESS_LINE4, "ADDR4");
			values1.put(DbHandlerL.COL_CUST_ZIP_CODE, "560050");
			values1.put(DbHandlerL.COL_CUST_CITY, "IN");
			values1.put(DbHandlerL.COL_CUST_STATE, "Karnataka");
			values1.put(DbHandlerL.COL_CUST_COUNTRY, "IN");
			values1.put(DbHandlerL.COL_CUST_NATIONALITY, " IN");
			values1.put(DbHandlerL.COL_CUST_LOC_BRANCH_CODE, " 000");
			values1.put(DbHandlerL.COL_CUST_PREFERRED_LANGUAGE, "EN");
			values1.put(DbHandlerL.COL_CUST_EMAIL_ADDRESS, "");
			values1.put(DbHandlerL.COL_CUST_SMS_REQUIRED, "");
			values1.put(DbHandlerL.COL_CUST_MOBILE_NUMBER, "9900112233");
			values1.put(DbHandlerL.COL_CUST_AGENT_ID, "AGT0001");
			values1.put(DbHandlerL.COL_CUST_CREDIT_OFFICER, "EGAUSER1");
			values1.put(DbHandlerL.COL_CUST_COLLECT_KYC, "Y");
			values1.put(DbHandlerL.COL_CUST_PARENT_CUST_ID, "110000001");
			values1.put(DbHandlerL.COL_CUST_IS_PARENT_CUST, "N");
			db.insert(DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_CUST_CUSTOMER_ID, "110000003");
			values2.put(DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME, "Bailey George");
			values2.put(DbHandlerL.COL_CUST_CUSTOMER_SHORT_NAME, "Bailey");
			values2.put(DbHandlerL.COL_CUST_CUSTOMER_CATEGORY, "I");
			values2.put(DbHandlerL.COL_CUST_GENDER, "M");
			values2.put(DbHandlerL.COL_CUST_DOB, "1412879400000");
			values2.put(DbHandlerL.COL_CUST_CUSTOMER_SINCE, "647634600000");
			values2.put(DbHandlerL.COL_CUST_LOCATION_CODE, "LCN00001");
			values2.put(DbHandlerL.COL_CUST_ADDRESS_LINE1, "ADDR1 ");
			values2.put(DbHandlerL.COL_CUST_ADDRESS_LINE2, "ADDR2");
			values2.put(DbHandlerL.COL_CUST_ADDRESS_LINE3, "ADDR3");
			values2.put(DbHandlerL.COL_CUST_ADDRESS_LINE4, "ADDR4");
			values2.put(DbHandlerL.COL_CUST_ZIP_CODE, "560050");
			values2.put(DbHandlerL.COL_CUST_CITY, "IN");
			values2.put(DbHandlerL.COL_CUST_STATE, "Karnataka");
			values2.put(DbHandlerL.COL_CUST_COUNTRY, "IN");
			values2.put(DbHandlerL.COL_CUST_NATIONALITY, " IN");
			values2.put(DbHandlerL.COL_CUST_LOC_BRANCH_CODE, " 000");
			values2.put(DbHandlerL.COL_CUST_PREFERRED_LANGUAGE, "EN");
			values2.put(DbHandlerL.COL_CUST_EMAIL_ADDRESS, "");
			values2.put(DbHandlerL.COL_CUST_SMS_REQUIRED, "");
			values2.put(DbHandlerL.COL_CUST_MOBILE_NUMBER, "9900112233");
			values2.put(DbHandlerL.COL_CUST_AGENT_ID, "AGT0001");
			values2.put(DbHandlerL.COL_CUST_CREDIT_OFFICER, "EGAUSER1");
			values2.put(DbHandlerL.COL_CUST_COLLECT_KYC, "Y");
			values2.put(DbHandlerL.COL_CUST_PARENT_CUST_ID, "110000001");
			values2.put(DbHandlerL.COL_CUST_IS_PARENT_CUST, "N");
			db.insert(DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_CUST_CUSTOMER_ID, "110000004");
			values3.put(DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME, "Brian Corner");
			values3.put(DbHandlerL.COL_CUST_CUSTOMER_SHORT_NAME, "Brian");
			values3.put(DbHandlerL.COL_CUST_CUSTOMER_CATEGORY, "I");
			values3.put(DbHandlerL.COL_CUST_GENDER, "M");
			values3.put(DbHandlerL.COL_CUST_DOB, "1412879400000");
			values3.put(DbHandlerL.COL_CUST_CUSTOMER_SINCE, "647634600000");
			values3.put(DbHandlerL.COL_CUST_LOCATION_CODE, "LCN00001");
			values3.put(DbHandlerL.COL_CUST_ADDRESS_LINE1, "ADDR1 ");
			values3.put(DbHandlerL.COL_CUST_ADDRESS_LINE2, "ADDR2");
			values3.put(DbHandlerL.COL_CUST_ADDRESS_LINE3, "ADDR3");
			values3.put(DbHandlerL.COL_CUST_ADDRESS_LINE4, "ADDR4");
			values3.put(DbHandlerL.COL_CUST_ZIP_CODE, "560050");
			values3.put(DbHandlerL.COL_CUST_CITY, "IN");
			values3.put(DbHandlerL.COL_CUST_STATE, "Karnataka");
			values3.put(DbHandlerL.COL_CUST_COUNTRY, "IN");
			values3.put(DbHandlerL.COL_CUST_NATIONALITY, " IN");
			values3.put(DbHandlerL.COL_CUST_LOC_BRANCH_CODE, " 000");
			values3.put(DbHandlerL.COL_CUST_PREFERRED_LANGUAGE, "EN");
			values3.put(DbHandlerL.COL_CUST_EMAIL_ADDRESS, "");
			values3.put(DbHandlerL.COL_CUST_SMS_REQUIRED, "");
			values3.put(DbHandlerL.COL_CUST_MOBILE_NUMBER, "9900112233");
			values3.put(DbHandlerL.COL_CUST_AGENT_ID, "AGT0001");
			values3.put(DbHandlerL.COL_CUST_CREDIT_OFFICER, "EGAUSER1");
			values3.put(DbHandlerL.COL_CUST_COLLECT_KYC, "Y");
			values3.put(DbHandlerL.COL_CUST_PARENT_CUST_ID, "110000001");
			values3.put(DbHandlerL.COL_CUST_IS_PARENT_CUST, "N");
			db.insert(DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_CUST_CUSTOMER_ID, "110000005");
			values4.put(DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME, "Albert John");
			values4.put(DbHandlerL.COL_CUST_CUSTOMER_SHORT_NAME, "Albert");
			values4.put(DbHandlerL.COL_CUST_CUSTOMER_CATEGORY, "I");
			values4.put(DbHandlerL.COL_CUST_GENDER, "M");
			values4.put(DbHandlerL.COL_CUST_DOB, "1412879400000");
			values4.put(DbHandlerL.COL_CUST_CUSTOMER_SINCE, "647634600000");
			values4.put(DbHandlerL.COL_CUST_LOCATION_CODE, "LCN00001");
			values4.put(DbHandlerL.COL_CUST_ADDRESS_LINE1, "ADDR1 ");
			values4.put(DbHandlerL.COL_CUST_ADDRESS_LINE2, "ADDR2");
			values4.put(DbHandlerL.COL_CUST_ADDRESS_LINE3, "ADDR3");
			values4.put(DbHandlerL.COL_CUST_ADDRESS_LINE4, "ADDR4");
			values4.put(DbHandlerL.COL_CUST_ZIP_CODE, "560050");
			values4.put(DbHandlerL.COL_CUST_CITY, "IN");
			values4.put(DbHandlerL.COL_CUST_STATE, "Karnataka");
			values4.put(DbHandlerL.COL_CUST_COUNTRY, "IN");
			values4.put(DbHandlerL.COL_CUST_NATIONALITY, " IN");
			values4.put(DbHandlerL.COL_CUST_LOC_BRANCH_CODE, " 000");
			values4.put(DbHandlerL.COL_CUST_PREFERRED_LANGUAGE, "EN");
			values4.put(DbHandlerL.COL_CUST_EMAIL_ADDRESS, "");
			values4.put(DbHandlerL.COL_CUST_SMS_REQUIRED, "");
			values4.put(DbHandlerL.COL_CUST_MOBILE_NUMBER, "9900112233");
			values4.put(DbHandlerL.COL_CUST_AGENT_ID, "AGT0001");
			values4.put(DbHandlerL.COL_CUST_CREDIT_OFFICER, "EGAUSER1");
			values4.put(DbHandlerL.COL_CUST_COLLECT_KYC, "Y");
			values4.put(DbHandlerL.COL_CUST_PARENT_CUST_ID, "110000001");
			values4.put(DbHandlerL.COL_CUST_IS_PARENT_CUST, "Y");
			db.insert(DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL, null, values4);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSR_CUSTDELT
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSR_CUSTDELT
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertAgnLonDet() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_LOAN_LOAN_AC_NO, "LBGRPDISBR100");
			values.put(DbHandlerL.COL_LOAN_BRANCH_CODE, "001");
			values.put(DbHandlerL.COL_LOAN_CUSTOMER_ID, "110000001");
			values.put(DbHandlerL.COL_LOAN_CUSTOMER_NAME, "Alastair Brown");
			values.put(DbHandlerL.COL_LOAN_CREDIT_OFFICER_CODE, "EGAUSER1");
			values.put(DbHandlerL.COL_LOAN_LOCATION_CODE, "LCN00001 ");
			values.put(DbHandlerL.COL_LOAN_GROUP_ID, "G01");
			values.put(DbHandlerL.COL_LOAN_SANCTIONED_DATE, "1378751400000");
			values.put(DbHandlerL.COL_LOAN_LAST_DISBURSED_DATE, "1381343400000");
			values.put(DbHandlerL.COL_LOAN_DISBR_TYPE, "M");
			values.put(DbHandlerL.COL_LOAN_LOAN_AC_CCY, "INR");
			values.put(DbHandlerL.COL_LOAN_IS_FULLY_DISBURSED, "N");
			values.put(DbHandlerL.COL_LOAN_SANCTIONED_PRINCIPAL_AMT, "500000");
			values.put(DbHandlerL.COL_LOAN_DISBURSED_PRINCIPAL_AMT, "100000");
			values.put(DbHandlerL.COL_LOAN_INTEREST_RATE, "30");
			values.put(DbHandlerL.COL_LOAN_INTEREST_ACCURED, "122247.2");
			values.put(DbHandlerL.COL_LOAN_PRINCIPAL_AMT_REPAID, "213229.73");
			values.put(DbHandlerL.COL_LOAN_PRINCIPAL_OUTSTANDING, "286770.27");
			values.put(DbHandlerL.COL_LOAN_LAST_REPAYMENT_DATE, " ");
			values.put(DbHandlerL.COL_LOAN_IS_GROUP_LOAN, "Y");
			values.put(DbHandlerL.COL_LOAN_IS_PARENT_LOAN, "Y");
			values.put(DbHandlerL.COL_LOAN_PARENT_CUST_ID, "110000001");
			values.put(DbHandlerL.COL_LOAN_PARENT_LOAN_AC_NO, "LNGRPREPAY1001");
			values.put(DbHandlerL.COL_LOAN_AGENT_ID, " AGT0001");
			db.insert(DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_LOAN_LOAN_AC_NO, "LBGRPDISBR101");
			values1.put(DbHandlerL.COL_LOAN_BRANCH_CODE, "002");
			values1.put(DbHandlerL.COL_LOAN_CUSTOMER_ID, "110000002");
			values1.put(DbHandlerL.COL_LOAN_CUSTOMER_NAME, "Babak Fahami");
			values1.put(DbHandlerL.COL_LOAN_CREDIT_OFFICER_CODE, "EGAUSER1");
			values1.put(DbHandlerL.COL_LOAN_LOCATION_CODE, "LCN00001 ");
			values1.put(DbHandlerL.COL_LOAN_GROUP_ID, "G01");
			values1.put(DbHandlerL.COL_LOAN_SANCTIONED_DATE, "1412879400000");
			values1.put(DbHandlerL.COL_LOAN_LAST_DISBURSED_DATE,
					"1381343400000");
			values1.put(DbHandlerL.COL_LOAN_DISBR_TYPE, "M");
			values1.put(DbHandlerL.COL_LOAN_LOAN_AC_CCY, "INR");
			values1.put(DbHandlerL.COL_LOAN_IS_FULLY_DISBURSED, "N");
			values1.put(DbHandlerL.COL_LOAN_SANCTIONED_PRINCIPAL_AMT, "100000");
			values1.put(DbHandlerL.COL_LOAN_DISBURSED_PRINCIPAL_AMT, "150000");
			values1.put(DbHandlerL.COL_LOAN_INTEREST_RATE, "25");
			values1.put(DbHandlerL.COL_LOAN_INTEREST_ACCURED, "0");
			values1.put(DbHandlerL.COL_LOAN_PRINCIPAL_AMT_REPAID, "0");
			values1.put(DbHandlerL.COL_LOAN_PRINCIPAL_OUTSTANDING, "100000");
			values1.put(DbHandlerL.COL_LOAN_LAST_REPAYMENT_DATE, " ");
			values1.put(DbHandlerL.COL_LOAN_IS_GROUP_LOAN, "N");
			values1.put(DbHandlerL.COL_LOAN_IS_PARENT_LOAN, "N");
			values1.put(DbHandlerL.COL_LOAN_PARENT_CUST_ID, "110000001");
			values1.put(DbHandlerL.COL_LOAN_PARENT_LOAN_AC_NO, "LNGRPREPAY001");
			values1.put(DbHandlerL.COL_LOAN_AGENT_ID, " AGT0001");
			db.insert(DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_LOAN_LOAN_AC_NO, "LBGRPDISBR102");
			values2.put(DbHandlerL.COL_LOAN_BRANCH_CODE, "000");
			values2.put(DbHandlerL.COL_LOAN_CUSTOMER_ID, "110000003");
			values2.put(DbHandlerL.COL_LOAN_CUSTOMER_NAME, "Bailey George");
			values2.put(DbHandlerL.COL_LOAN_CREDIT_OFFICER_CODE, "EGAUSER1");
			values2.put(DbHandlerL.COL_LOAN_LOCATION_CODE, "LCN00001 ");
			values2.put(DbHandlerL.COL_LOAN_GROUP_ID, "G01");
			values2.put(DbHandlerL.COL_LOAN_SANCTIONED_DATE, "1412879400000");
			values2.put(DbHandlerL.COL_LOAN_LAST_DISBURSED_DATE,
					"1381343400000");
			values2.put(DbHandlerL.COL_LOAN_DISBR_TYPE, "M");
			values2.put(DbHandlerL.COL_LOAN_LOAN_AC_CCY, "INR");
			values2.put(DbHandlerL.COL_LOAN_IS_FULLY_DISBURSED, "N");
			values2.put(DbHandlerL.COL_LOAN_SANCTIONED_PRINCIPAL_AMT, "25000");
			values2.put(DbHandlerL.COL_LOAN_DISBURSED_PRINCIPAL_AMT, "20000");
			values2.put(DbHandlerL.COL_LOAN_INTEREST_RATE, "25");
			values2.put(DbHandlerL.COL_LOAN_INTEREST_ACCURED, "0");
			values2.put(DbHandlerL.COL_LOAN_PRINCIPAL_AMT_REPAID, "0");
			values2.put(DbHandlerL.COL_LOAN_PRINCIPAL_OUTSTANDING, "25000");
			values2.put(DbHandlerL.COL_LOAN_LAST_REPAYMENT_DATE, " ");
			values2.put(DbHandlerL.COL_LOAN_IS_GROUP_LOAN, "Y");
			values2.put(DbHandlerL.COL_LOAN_IS_PARENT_LOAN, "N");
			values2.put(DbHandlerL.COL_LOAN_PARENT_CUST_ID, "110000001");
			values2.put(DbHandlerL.COL_LOAN_PARENT_LOAN_AC_NO, "LBGRPDISBR001");
			values2.put(DbHandlerL.COL_LOAN_AGENT_ID, " AGT0001");
			db.insert(DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_LOAN_LOAN_AC_NO, "LBGRPDISBR103");
			values3.put(DbHandlerL.COL_LOAN_BRANCH_CODE, "004");
			values3.put(DbHandlerL.COL_LOAN_CUSTOMER_ID, "110000004");
			values3.put(DbHandlerL.COL_LOAN_CUSTOMER_NAME, "Brian Corner");
			values3.put(DbHandlerL.COL_LOAN_CREDIT_OFFICER_CODE, "EGAUSER1");
			values3.put(DbHandlerL.COL_LOAN_LOCATION_CODE, "LCN00001 ");
			values3.put(DbHandlerL.COL_LOAN_GROUP_ID, "G01");
			values3.put(DbHandlerL.COL_LOAN_SANCTIONED_DATE, "1378751400000");
			values3.put(DbHandlerL.COL_LOAN_LAST_DISBURSED_DATE,
					"1381343400000");
			values3.put(DbHandlerL.COL_LOAN_DISBR_TYPE, "M");
			values3.put(DbHandlerL.COL_LOAN_LOAN_AC_CCY, "INR");
			values3.put(DbHandlerL.COL_LOAN_IS_FULLY_DISBURSED, "N");
			values3.put(DbHandlerL.COL_LOAN_SANCTIONED_PRINCIPAL_AMT, "500000");
			values3.put(DbHandlerL.COL_LOAN_DISBURSED_PRINCIPAL_AMT, "300000");
			values3.put(DbHandlerL.COL_LOAN_INTEREST_RATE, "30");
			values3.put(DbHandlerL.COL_LOAN_INTEREST_ACCURED, "122247.2");
			values3.put(DbHandlerL.COL_LOAN_PRINCIPAL_AMT_REPAID, "213229.73");
			values3.put(DbHandlerL.COL_LOAN_PRINCIPAL_OUTSTANDING, "286770.27");
			values3.put(DbHandlerL.COL_LOAN_LAST_REPAYMENT_DATE, " ");
			values3.put(DbHandlerL.COL_LOAN_IS_GROUP_LOAN, "N");
			values3.put(DbHandlerL.COL_LOAN_IS_PARENT_LOAN, "N");
			values3.put(DbHandlerL.COL_LOAN_PARENT_CUST_ID, "110000001");
			values3.put(DbHandlerL.COL_LOAN_PARENT_LOAN_AC_NO, "LNINDREPAY9101");
			values3.put(DbHandlerL.COL_LOAN_AGENT_ID, " AGT0001");
			db.insert(DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_LOAN_LOAN_AC_NO, "LBGRPDISBR104");
			values4.put(DbHandlerL.COL_LOAN_BRANCH_CODE, "005");
			values4.put(DbHandlerL.COL_LOAN_CUSTOMER_ID, "110000005");
			values4.put(DbHandlerL.COL_LOAN_CUSTOMER_NAME, "Albert John");
			values4.put(DbHandlerL.COL_LOAN_CREDIT_OFFICER_CODE, "EGAUSER1");
			values4.put(DbHandlerL.COL_LOAN_LOCATION_CODE, "LCN00001 ");
			values4.put(DbHandlerL.COL_LOAN_GROUP_ID, "G01");
			values4.put(DbHandlerL.COL_LOAN_SANCTIONED_DATE, "1412879400000");
			values4.put(DbHandlerL.COL_LOAN_LAST_DISBURSED_DATE,
					"1381343400000");
			values4.put(DbHandlerL.COL_LOAN_DISBR_TYPE, "M");
			values4.put(DbHandlerL.COL_LOAN_LOAN_AC_CCY, "INR");
			values4.put(DbHandlerL.COL_LOAN_IS_FULLY_DISBURSED, "N");
			values4.put(DbHandlerL.COL_LOAN_SANCTIONED_PRINCIPAL_AMT, "100000");
			values4.put(DbHandlerL.COL_LOAN_DISBURSED_PRINCIPAL_AMT, "5000");
			values4.put(DbHandlerL.COL_LOAN_INTEREST_RATE, "25");
			values4.put(DbHandlerL.COL_LOAN_INTEREST_ACCURED, "0");
			values4.put(DbHandlerL.COL_LOAN_PRINCIPAL_AMT_REPAID, "0");
			values4.put(DbHandlerL.COL_LOAN_PRINCIPAL_OUTSTANDING, "100000");
			values4.put(DbHandlerL.COL_LOAN_LAST_REPAYMENT_DATE, " ");
			values4.put(DbHandlerL.COL_LOAN_IS_GROUP_LOAN, "Y");
			values4.put(DbHandlerL.COL_LOAN_IS_PARENT_LOAN, "Y");
			values4.put(DbHandlerL.COL_LOAN_PARENT_CUST_ID, "110000001");
			values4.put(DbHandlerL.COL_LOAN_PARENT_LOAN_AC_NO, "LBGRPDISBR001");
			values4.put(DbHandlerL.COL_LOAN_AGENT_ID, " AGT0001");

			db.insert(DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL, null, values4);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRAGNLOANDET
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRAGNLOANDET
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertDeptDet() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_DEPOSIT_DEP_AC_NO, "DEP0001");
			values.put(DbHandlerL.COL_DEPOSIT_BRANCH_CODE, "000");
			values.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_ID, "110000001");
			values.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_NAME, "Alastair Brown");
			values.put(DbHandlerL.COL_DEPOSIT_AC_CCY, "INR");
			values.put(DbHandlerL.COL_DEPOSIT_OPEN_DATE, "1381343400000");
			values.put(DbHandlerL.COL_DEPOSIT_MATURITY_DATE, "1444415400000");
			values.put(DbHandlerL.COL_DEPOSIT_SCH_INSTALLMENT_AMT, "5000");
			values.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ_TYPE, "M");
			values.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ, "1");
			values.put(DbHandlerL.COL_DEPOSIT_TENURE_TYPE, "M");
			values.put(DbHandlerL.COL_DEPOSIT_TENURE, "24");
			values.put(DbHandlerL.COL_DEPOSIT_INT_RATE, "5");
			values.put(DbHandlerL.COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT, "10000");
			values.put(DbHandlerL.COL_DEPOSIT_INTEREST_ACCURED_TILL_DATE, " ");
			values.put(DbHandlerL.COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE, "100");
			values.put(DbHandlerL.COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE, "9000");
			values.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_FLAG, "N");
			values.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_PAYOUT_DATE, " ");
			values.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_AMOUNT, "0");
			values.put(DbHandlerL.COL_DEPOSIT_LOCATION_CODE, "LCN00001");
			values.put(DbHandlerL.COL_DEPOSIT_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_DEPOSIT_CREDIT_OFFICER_CODE, "EGAUSER1");
			db.insert(DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_DEPOSIT_DEP_AC_NO, "DEP0002");
			values1.put(DbHandlerL.COL_DEPOSIT_BRANCH_CODE, "000");
			values1.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_ID, "110000002");
			values1.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_NAME, "Babak Fahami");
			values1.put(DbHandlerL.COL_DEPOSIT_AC_CCY, "INR");
			values1.put(DbHandlerL.COL_DEPOSIT_OPEN_DATE, "1381343400000");
			values1.put(DbHandlerL.COL_DEPOSIT_MATURITY_DATE, "1444415400000");
			values1.put(DbHandlerL.COL_DEPOSIT_SCH_INSTALLMENT_AMT, "5000");
			values1.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ_TYPE, "M");
			values1.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ, "1");
			values1.put(DbHandlerL.COL_DEPOSIT_TENURE_TYPE, "M");
			values1.put(DbHandlerL.COL_DEPOSIT_TENURE, "24");
			values1.put(DbHandlerL.COL_DEPOSIT_INT_RATE, "5");
			values1.put(DbHandlerL.COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT,
					"120000");
			values1.put(DbHandlerL.COL_DEPOSIT_INTEREST_ACCURED_TILL_DATE,
					"1643.15");
			values1.put(DbHandlerL.COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE,
					"60000");
			values1.put(DbHandlerL.COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE,
					"60000");
			values1.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_FLAG, "N");
			values1.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_PAYOUT_DATE, " ");
			values1.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_AMOUNT, "0");
			values1.put(DbHandlerL.COL_DEPOSIT_LOCATION_CODE, "LCN00001");
			values1.put(DbHandlerL.COL_DEPOSIT_AGENT_ID, "AGT0001");
			values1.put(DbHandlerL.COL_DEPOSIT_CREDIT_OFFICER_CODE, "EGAUSER1");
			db.insert(DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_DEPOSIT_DEP_AC_NO, "DEP0003");
			values2.put(DbHandlerL.COL_DEPOSIT_BRANCH_CODE, "000");
			values2.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_ID, "110000003");
			values2.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_NAME, "Bailey George");
			values2.put(DbHandlerL.COL_DEPOSIT_AC_CCY, "INR");
			values2.put(DbHandlerL.COL_DEPOSIT_OPEN_DATE, "1381343400000");
			values2.put(DbHandlerL.COL_DEPOSIT_MATURITY_DATE, "1444415400000");
			values2.put(DbHandlerL.COL_DEPOSIT_SCH_INSTALLMENT_AMT, "5000");
			values2.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ_TYPE, "M");
			values2.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ, "1");
			values2.put(DbHandlerL.COL_DEPOSIT_TENURE_TYPE, "M");
			values2.put(DbHandlerL.COL_DEPOSIT_TENURE, "24");
			values2.put(DbHandlerL.COL_DEPOSIT_INT_RATE, "5");
			values2.put(DbHandlerL.COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT,
					"60000");
			values2.put(DbHandlerL.COL_DEPOSIT_INTEREST_ACCURED_TILL_DATE,
					"1643.15");
			values2.put(DbHandlerL.COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE,
					"60000");
			values2.put(DbHandlerL.COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE,
					"60000");
			values2.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_FLAG, "N");
			values2.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_PAYOUT_DATE, " ");
			values2.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_AMOUNT, "0");
			values2.put(DbHandlerL.COL_DEPOSIT_LOCATION_CODE, "LCN00001");
			values2.put(DbHandlerL.COL_DEPOSIT_AGENT_ID, "AGT0001");
			values2.put(DbHandlerL.COL_DEPOSIT_CREDIT_OFFICER_CODE, "EGAUSER1");
			db.insert(DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_DEPOSIT_DEP_AC_NO, "DEP0004");
			values3.put(DbHandlerL.COL_DEPOSIT_BRANCH_CODE, "000");
			values3.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_ID, "110000004");
			values3.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_NAME, "Brian Corner");
			values3.put(DbHandlerL.COL_DEPOSIT_AC_CCY, "INR");
			values3.put(DbHandlerL.COL_DEPOSIT_OPEN_DATE, "1381343400000");
			values3.put(DbHandlerL.COL_DEPOSIT_MATURITY_DATE, "1444415400000");
			values3.put(DbHandlerL.COL_DEPOSIT_SCH_INSTALLMENT_AMT, "5000");
			values3.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ_TYPE, "M");
			values3.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ, "1");
			values3.put(DbHandlerL.COL_DEPOSIT_TENURE_TYPE, "M");
			values3.put(DbHandlerL.COL_DEPOSIT_TENURE, "24");
			values3.put(DbHandlerL.COL_DEPOSIT_INT_RATE, "5");
			values3.put(DbHandlerL.COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT,
					"120000");
			values3.put(DbHandlerL.COL_DEPOSIT_INTEREST_ACCURED_TILL_DATE,
					"1643.15");
			values3.put(DbHandlerL.COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE,
					"60000");
			values3.put(DbHandlerL.COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE,
					"60000");
			values3.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_FLAG, "N");
			values3.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_PAYOUT_DATE, " ");
			values3.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_AMOUNT, "0");
			values3.put(DbHandlerL.COL_DEPOSIT_LOCATION_CODE, "LCN00001");
			values3.put(DbHandlerL.COL_DEPOSIT_AGENT_ID, "AGT0001");
			values3.put(DbHandlerL.COL_DEPOSIT_CREDIT_OFFICER_CODE, "EGAUSER1");
			db.insert(DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_DEPOSIT_DEP_AC_NO, "DEP0005");
			values4.put(DbHandlerL.COL_DEPOSIT_BRANCH_CODE, "000");
			values4.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_ID, "120000001");
			values4.put(DbHandlerL.COL_DEPOSIT_CUSTOMER_NAME, "Albert John");
			values4.put(DbHandlerL.COL_DEPOSIT_AC_CCY, "INR");
			values4.put(DbHandlerL.COL_DEPOSIT_OPEN_DATE, "1381343400000");
			values4.put(DbHandlerL.COL_DEPOSIT_MATURITY_DATE, "1444415400000");
			values4.put(DbHandlerL.COL_DEPOSIT_SCH_INSTALLMENT_AMT, "5000");
			values4.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ_TYPE, "M");
			values4.put(DbHandlerL.COL_DEPOSIT_PAY_FREQ, "1");
			values4.put(DbHandlerL.COL_DEPOSIT_TENURE_TYPE, "M");
			values4.put(DbHandlerL.COL_DEPOSIT_TENURE, "24");
			values4.put(DbHandlerL.COL_DEPOSIT_INT_RATE, "5");
			values4.put(DbHandlerL.COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT,
					"120000");
			values4.put(DbHandlerL.COL_DEPOSIT_INTEREST_ACCURED_TILL_DATE,
					"1643.15");
			values4.put(DbHandlerL.COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE,
					"60000");
			values4.put(DbHandlerL.COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE,
					"60000");
			values4.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_FLAG, "N");
			values4.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_PAYOUT_DATE, " ");
			values4.put(DbHandlerL.COL_DEPOSIT_REDEMPTION_AMOUNT, "0");
			values4.put(DbHandlerL.COL_DEPOSIT_LOCATION_CODE, "LCN00001");
			values4.put(DbHandlerL.COL_DEPOSIT_AGENT_ID, "AGT0001");
			values4.put(DbHandlerL.COL_DEPOSIT_CREDIT_OFFICER_CODE, "EGAUSER1");

			db.insert(DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL, null, values4);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSR_DEPTDET
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSR_DEPTDET
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	// This is for static data insert
	public void insertAppParm(String name, String value)
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

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRAPPPARM
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRAPPPARM
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}


	public void insertMsgCode() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();
			for (int i = 0; i < 9; i++) {

				ContentValues values = new ContentValues();

				values.put(DbHandlerL.COL_MESSAGE_CODE, "MSG1" + i);
				values.put(DbHandlerL.COL_MESSAGE_LANG, "ENGLISH");
				values.put(DbHandlerL.COL_MESSAGE_DESC, "");
				values.put(DbHandlerL.COL_MESSAGE_ERR_TYPE, " ");
				values.put(DbHandlerL.COL_MESSAGE_SUB_SYS, " ");
				values.put(DbHandlerL.COL_MESSAGE_MSG_TYPE, "");
				db.insert(DbHandlerL.TABLE_MBS_MESSAGE_CODE, null, values);
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRMSG_CODE 
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRMSG_CODE 
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertBrchDts() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();
			for (int i = 0; i < 9; i++) {

				ContentValues values = new ContentValues();

				values.put(DbHandlerL.COL_BRANCH_CODE, "000" + i);
				values.put(DbHandlerL.COL_BRANCH_DATE, "1412879400000");
				db.insert(DbHandlerL.TABLE_MBS_BRANCH_DETAILS, null, values);
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRBRCHDTS
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRBRCHDTS
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertAgtCashRec() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();
			for (int i = 0; i < 9; i++) {

				ContentValues values = new ContentValues();

				values.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO, "CSEQ"+i);
				values.put(DbHandlerL.COL_CASH_TXN_ID, "T01");
				values.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, "");
				values.put(DbHandlerL.COL_CASH_TXN_SOURCE, "");
				values.put(DbHandlerL.COL_CASH_TXN_DATETIME, "");
				values.put(DbHandlerL.COL_CASH_TXN_CODE, "L01");
				values.put(DbHandlerL.COL_CASH_AGENT_ID, "AGT0001");
				values.put(DbHandlerL.COL_CASH_DEVICE_ID, "");
				values.put(DbHandlerL.COL_CASH_AGENDA_ID, "");
				values.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, "");

				values.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, "INR");
				values.put(DbHandlerL.COL_CASH_DR_CR_IND, "");
				values.put(DbHandlerL.COL_CASH_CASH_AMT, "6700");
				values.put(DbHandlerL.COL_CASH_AUTH_STAT, "");
				values.put(DbHandlerL.COL_CASH_IS_REVERSAL, "");
				values.put(DbHandlerL.COL_CASH_IS_DELETED, "");

				db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, values);
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRAGT_CASHREC
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRAGT_CASHREC
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}

	}

	public void insertDevDetl() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_DEVICE_DEVICE_ID, "DEV00001");
			values.put(DbHandlerL.COL_DEVICE_DEVICE_TYPE, "");
			values.put(DbHandlerL.COL_DEVICE_UNIQUE_ID, "000000000000000");
			values.put(DbHandlerL.COL_DEVICE_ISSUED_DATE, "1413829800000");
			values.put(DbHandlerL.COL_DEVICE_LAST_SYNC, "1414912582505");
			values.put(DbHandlerL.COL_DEVICE_ISSUED_BY_CO, "");
			values.put(DbHandlerL.COL_DEVICE_BRAND_MODEL, "Nexus 5");
			values.put(DbHandlerL.COL_DEVICE_OS, "Android");
			values.put(DbHandlerL.COL_DEVICE_APP_VERSION, "1.0");
			values.put(DbHandlerL.COL_DEVICE_SIM_NUMBER, "9980166696");
			db.insert(DbHandlerL.TABLE_MBS_DEVICE_DETAIL, null, values);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRDEV_DETL 
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRDEV_DETL 
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertAgents() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();

			values.put(DbHandlerL.COL_AGENT_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_AGENT_PASSWORD, "bfsi1234");
			values.put(DbHandlerL.COL_AGENT_PASSWORD_SALT, "");
			values.put(DbHandlerL.COL_AGENT_AGENT_TYPE, "1 ");
			values.put(DbHandlerL.COL_AGENT_CASH_LIMIT, "500000");
			values.put(DbHandlerL.COL_AGENT_DEVICE_ID, "DEV00001");
			values.put(DbHandlerL.COL_AGENT_STATUS, "");
			values.put(DbHandlerL.COL_AGENT_FIRST_NAME, "Max");
			values.put(DbHandlerL.COL_AGENT_LAST_NAME, "Muller");
			values.put(DbHandlerL.COL_AGENT_LANG, "1");
			values.put(DbHandlerL.COL_AGENT_AGENCY_CODE, "AGY00001");
			values.put(DbHandlerL.COL_AGENT_CHANGEPASSWORD_DATE, "");
			values.put(DbHandlerL.COL_AGENT_CREDIT_OFFICER, "CR01");
			values.put(DbHandlerL.COL_AGENT_DOB, "496693800000");
			values.put(DbHandlerL.COL_AGENT_GENDER, "1");
			values.put(DbHandlerL.COL_AGENT_AUTH_STATUS, "A");
			values.put(
					DbHandlerL.COL_AGENT_DATA_KEY,
					"YOMQaQec58grMXxcj8CaJXbSty+BHE+npQDYRs08JwFZu4U3D3hkmwWMDnnFS2+XupRSJK5xW4BiORUNFQeNdjF7QWcvlfI782RaMQ==");
			values.put(DbHandlerL.COL_AGENT_REG_KEY, "0");
			values.put(DbHandlerL.COL_AGENT_REGKEY_EXP_DATE, "");
			values.put(DbHandlerL.COL_AGENT_REG_STATUS, " ");
			values.put(DbHandlerL.COL_AGENT_PROMPT_CHANGEPASSWORD, "");

			values.put(DbHandlerL.COL_AGENT_LOCATION_CODE, "LCN00001");
			values.put(
					DbHandlerL.COL_AGENT_ROLES_LIST,
					"FUN00040,FUN00002,FUN00003,FUN00004,FUN00005,FUN00001,FUN00039,FUN00058,FUN00006,FUN00007,FUN00008,FUN00010,FUN00053,FUN00048,FUN00036,FUN00049,FUN00011,FUN00012,FUN00013,FUN00014,FUN00015,FUN00032,FUN00016,FUN00017,FUN00018,FUN00019,FUN00020,FUN00021,FUN00022,FUN00024,FUN00059,FUN00023,FUN00054,FUN00056,FUN00057,FUN00052,FUN00055,FUN00050,FUN00033,FUN00034,FUN00035,FUN00037,FUN00038,FUN00025,FUN00026,FUN00027,FUN00028,FUN00029,FUN00042,FUN00041,FUN00065,FUN00091,FUN00043,FUN00044,FUN00045,FUN00046,FUN00047,FUN00060,FUN00061,FUN00062,FUN00063,FUN00064,FUN00066,FUN00067,FUN00071,FUN00072,FUN00073,FUN00074,FUN00075,FUN00093,FUN00094,FUN00076,FUN00077,FUN00078,FUN00079,FUN00080,FUN00086,FUN00087,FUN00088,FUN00089,FUN00090,FUN00092,FUN00097,FUN00098,FUN00099,FUN00100,FUN00081,FUN00082,FUN00083,FUN00084,FUN00085");
			values.put(DbHandlerL.COL_AGENT_USERNAME, "AGT0001");
			values.put(DbHandlerL.COL_AGENT_LAST_LOGINTIME, "1414912255793");
			values.put(DbHandlerL.COL_AGENT_NO_INVALID_LOGIN, "0");
			values.put(DbHandlerL.COL_AGENT_LAST_SYNC_TIME, "1414912582404");
			values.put(DbHandlerL.COL_AGENT_EVOLUTE_MAC_ID, "00:06:66:61:3A:FE");
			db.insert(DbHandlerL.TABLE_MBS_AGENTS, null, values);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRAGENT
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRAGENT
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertCcyCodes() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_CCY_CCY_CODE, "INR");
			values.put(DbHandlerL.COL_CCY_CCY_NAME, "Indian Rupee");
			values.put(DbHandlerL.COL_CCY_ISO_CCY_CODE, "356");
			values.put(DbHandlerL.COL_CCY_ISO_ALT_CODE, " ");
			values.put(DbHandlerL.COL_CCY_COUNTRY, "IN");
			values.put(DbHandlerL.COL_CCY_ROUND_RULE, "R");
			values.put(DbHandlerL.COL_CCY_DECIMAL, "2");
			db.insert(DbHandlerL.TABLE_MBS_CCY_CODES, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_CCY_CCY_CODE, "USD");
			values1.put(DbHandlerL.COL_CCY_CCY_NAME, "US Dollar");
			values1.put(DbHandlerL.COL_CCY_ISO_CCY_CODE, "292");
			values1.put(DbHandlerL.COL_CCY_ISO_ALT_CODE, " ");
			values1.put(DbHandlerL.COL_CCY_COUNTRY, "US");
			values1.put(DbHandlerL.COL_CCY_ROUND_RULE, "R");
			values1.put(DbHandlerL.COL_CCY_DECIMAL, "2");
			db.insert(DbHandlerL.TABLE_MBS_CCY_CODES, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_CCY_CCY_CODE, "EUR");
			values2.put(DbHandlerL.COL_CCY_CCY_NAME, "Euro");
			values2.put(DbHandlerL.COL_CCY_ISO_CCY_CODE, "978");
			values2.put(DbHandlerL.COL_CCY_ISO_ALT_CODE, " ");
			values2.put(DbHandlerL.COL_CCY_COUNTRY, "DE");
			values2.put(DbHandlerL.COL_CCY_ROUND_RULE, "R");
			values2.put(DbHandlerL.COL_CCY_DECIMAL, "2");
			db.insert(DbHandlerL.TABLE_MBS_CCY_CODES, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_CCY_CCY_CODE, "AUD");
			values3.put(DbHandlerL.COL_CCY_CCY_NAME, "Khmer Riel");
			values3.put(DbHandlerL.COL_CCY_ISO_CCY_CODE, "855");
			values3.put(DbHandlerL.COL_CCY_ISO_ALT_CODE, " ");
			values3.put(DbHandlerL.COL_CCY_COUNTRY, "KH");
			values3.put(DbHandlerL.COL_CCY_ROUND_RULE, "R");
			values3.put(DbHandlerL.COL_CCY_DECIMAL, "2");
			db.insert(DbHandlerL.TABLE_MBS_CCY_CODES, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_CCY_CCY_CODE, "KHR");
			values4.put(DbHandlerL.COL_CCY_CCY_NAME, "Austrialian Dollar");
			values4.put(DbHandlerL.COL_CCY_ISO_CCY_CODE, "295");
			values4.put(DbHandlerL.COL_CCY_ISO_ALT_CODE, " ");
			values4.put(DbHandlerL.COL_CCY_COUNTRY, "AU");
			values4.put(DbHandlerL.COL_CCY_ROUND_RULE, "R");
			values4.put(DbHandlerL.COL_CCY_DECIMAL, "2");
			db.insert(DbHandlerL.TABLE_MBS_CCY_CODES, null, values4);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRCCYCODE
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRCCYCODE
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertFixRates() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();
			for (int i = 0; i < 9; i++) {

				ContentValues values = new ContentValues();
				values.put(DbHandlerL.COL_FX_BRAN_CODE, "FBC0" + i);
				values.put(DbHandlerL.COL_FX_CCY1, "");
				values.put(DbHandlerL.COL_FX_CCY2, "");
				values.put(DbHandlerL.COL_FX_RECORD_STAT, " ");
				values.put(DbHandlerL.COL_FX_MID_RATE, "");
				values.put(DbHandlerL.COL_FX_BUY_RATE, "");
				values.put(DbHandlerL.COL_FX_SALE_RATE, "");
				values.put(DbHandlerL.COL_FX_CBS_UPLD_HOB_ID, "");
				values.put(DbHandlerL.COL_FX_SYNC_STATUS, "");
				values.put(DbHandlerL.COL_FX_SYNC_TIME, "");
				db.insert(DbHandlerL.TABLE_MBS_FX_RATES, null, values);
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRFIX_RATE
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRFIX_RATE
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void insertCashData() throws SQLiteException, DataAccessException {
		try {
			db = DbHandlerL.getInstance().getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(DbHandlerL.COL_CUSTACC_CUSTOMER_ID, "110000001");
			values.put(DbHandlerL.COL_CUSTACC_CUSTOMER_FULL_NAME,
					"Alastair Brown");
			values.put(DbHandlerL.COL_CUSTACC_CUST_AC_NO, "AC0110000001");
			values.put(DbHandlerL.COL_CUSTACC_BRANCH_CODE, "000");
			values.put(DbHandlerL.COL_CUSTACC_ACCOUNT_TYPE, "S");
			values.put(DbHandlerL.COL_CUSTACC_AC_DESC, "Saving Account");
			values.put(DbHandlerL.COL_CUSTACC_AC_CCY, "INR");
			values.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_DR, "N");
			values.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_CR, "N");
			values.put(DbHandlerL.COL_CUSTACC_AC_STAT_BLOCK, "N");
			values.put(DbHandlerL.COL_CUSTACC_AC_STAT_STOP_PAY, "N");
			values.put(DbHandlerL.COL_CUSTACC_AC_STAT_DORMANT, "N");
			values.put(DbHandlerL.COL_CUSTACC_AGENT_ID, "AGT0001");
			values.put(DbHandlerL.COL_CUSTACC_LOCATION_CODE, "LCN00001");
			values.put(DbHandlerL.COL_CUSTACC_CREDIT_OFFICER_ID, "EGAUSER1");

			db.insert(DbHandlerL.TABLE_MBS_CUSTACC_DETAIL, null, values);

			ContentValues values1 = new ContentValues();
			values1.put(DbHandlerL.COL_CUSTACC_CUSTOMER_ID, "110000002");
			values1.put(DbHandlerL.COL_CUSTACC_CUSTOMER_FULL_NAME,
					"Babak Fahami");
			values1.put(DbHandlerL.COL_CUSTACC_CUST_AC_NO, "AC0110000002");
			values1.put(DbHandlerL.COL_CUSTACC_BRANCH_CODE, "000");
			values1.put(DbHandlerL.COL_CUSTACC_ACCOUNT_TYPE, "S");
			values1.put(DbHandlerL.COL_CUSTACC_AC_DESC, "Saving Account");
			values1.put(DbHandlerL.COL_CUSTACC_AC_CCY, "INR");
			values1.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_DR, "N");
			values1.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_CR, "N");
			values1.put(DbHandlerL.COL_CUSTACC_AC_STAT_BLOCK, "N");
			values1.put(DbHandlerL.COL_CUSTACC_AC_STAT_STOP_PAY, "N");
			values1.put(DbHandlerL.COL_CUSTACC_AC_STAT_DORMANT, "N");
			values1.put(DbHandlerL.COL_CUSTACC_AGENT_ID, "AGT0001");
			values1.put(DbHandlerL.COL_CUSTACC_LOCATION_CODE, "LCN00001");
			values1.put(DbHandlerL.COL_CUSTACC_CREDIT_OFFICER_ID, "EGAUSER1");

			db.insert(DbHandlerL.TABLE_MBS_CUSTACC_DETAIL, null, values1);

			ContentValues values2 = new ContentValues();
			values2.put(DbHandlerL.COL_CUSTACC_CUSTOMER_ID, "110000003");
			values2.put(DbHandlerL.COL_CUSTACC_CUSTOMER_FULL_NAME,
					"Bailey George");
			values2.put(DbHandlerL.COL_CUSTACC_CUST_AC_NO, "AC0110000003");
			values2.put(DbHandlerL.COL_CUSTACC_BRANCH_CODE, "000");
			values2.put(DbHandlerL.COL_CUSTACC_ACCOUNT_TYPE, "S");
			values2.put(DbHandlerL.COL_CUSTACC_AC_DESC, "Saving Account");
			values2.put(DbHandlerL.COL_CUSTACC_AC_CCY, "INR");
			values2.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_DR, "N");
			values2.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_CR, "N");
			values2.put(DbHandlerL.COL_CUSTACC_AC_STAT_BLOCK, "N");
			values2.put(DbHandlerL.COL_CUSTACC_AC_STAT_STOP_PAY, "N");
			values2.put(DbHandlerL.COL_CUSTACC_AC_STAT_DORMANT, "N");
			values2.put(DbHandlerL.COL_CUSTACC_AGENT_ID, "AGT0001");
			values2.put(DbHandlerL.COL_CUSTACC_LOCATION_CODE, "LCN00001");
			values2.put(DbHandlerL.COL_CUSTACC_CREDIT_OFFICER_ID, "EGAUSER1");

			db.insert(DbHandlerL.TABLE_MBS_CUSTACC_DETAIL, null, values2);

			ContentValues values3 = new ContentValues();
			values3.put(DbHandlerL.COL_CUSTACC_CUSTOMER_ID, "110000004");
			values3.put(DbHandlerL.COL_CUSTACC_CUSTOMER_FULL_NAME,
					"Brian Corner");
			values3.put(DbHandlerL.COL_CUSTACC_CUST_AC_NO, "AC0110000004");
			values3.put(DbHandlerL.COL_CUSTACC_BRANCH_CODE, "000");
			values3.put(DbHandlerL.COL_CUSTACC_ACCOUNT_TYPE, "S");
			values3.put(DbHandlerL.COL_CUSTACC_AC_DESC, "Saving Account");
			values3.put(DbHandlerL.COL_CUSTACC_AC_CCY, "INR");
			values3.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_DR, "N");
			values3.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_CR, "N");
			values3.put(DbHandlerL.COL_CUSTACC_AC_STAT_BLOCK, "N");
			values3.put(DbHandlerL.COL_CUSTACC_AC_STAT_STOP_PAY, "N");
			values3.put(DbHandlerL.COL_CUSTACC_AC_STAT_DORMANT, "N");
			values3.put(DbHandlerL.COL_CUSTACC_AGENT_ID, "AGT0001");
			values3.put(DbHandlerL.COL_CUSTACC_LOCATION_CODE, "LCN00001");
			values3.put(DbHandlerL.COL_CUSTACC_CREDIT_OFFICER_ID, "EGAUSER1");

			db.insert(DbHandlerL.TABLE_MBS_CUSTACC_DETAIL, null, values3);

			ContentValues values4 = new ContentValues();
			values4.put(DbHandlerL.COL_CUSTACC_CUSTOMER_ID, "120000001");
			values4.put(DbHandlerL.COL_CUSTACC_CUSTOMER_FULL_NAME,
					"Albert John");
			values4.put(DbHandlerL.COL_CUSTACC_CUST_AC_NO, "AC0120000001");
			values4.put(DbHandlerL.COL_CUSTACC_BRANCH_CODE, "000");
			values4.put(DbHandlerL.COL_CUSTACC_ACCOUNT_TYPE, "S");
			values4.put(DbHandlerL.COL_CUSTACC_AC_DESC, "Saving Account");
			values4.put(DbHandlerL.COL_CUSTACC_AC_CCY, "INR");
			values4.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_DR, "N");
			values4.put(DbHandlerL.COL_CUSTACC_AC_STAT_NO_CR, "N");
			values4.put(DbHandlerL.COL_CUSTACC_AC_STAT_BLOCK, "N");
			values4.put(DbHandlerL.COL_CUSTACC_AC_STAT_STOP_PAY, "N");
			values4.put(DbHandlerL.COL_CUSTACC_AC_STAT_DORMANT, "N");
			values4.put(DbHandlerL.COL_CUSTACC_AGENT_ID, "AGT0001");
			values4.put(DbHandlerL.COL_CUSTACC_LOCATION_CODE, "LCN00001");
			values4.put(DbHandlerL.COL_CUSTACC_CREDIT_OFFICER_ID, "EGAUSER1");

			db.insert(DbHandlerL.TABLE_MBS_CUSTACC_DETAIL, null, values4);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_CASHCODE_DATA 
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_CASHCODE_DATA 
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public String getErrorMessage(String errorCode) throws SQLiteException,
			DataAccessException {
		String erroDesc = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String query = "SELECT " + DbHandlerL.COL_MESSAGE_DESC + " FROM "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE + " WHERE "
					+ DbHandlerL.COL_MESSAGE_CODE + "='" + errorCode + "' AND "
					+ DbHandlerL.COL_MESSAGE_LANG + "='" + CommonContexts.LANG
					+ "'";
			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				erroDesc = cursor.getString(0);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_GETERRMSG
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_GETERRMSG
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return erroDesc;
	}

	public void moduleCodes() throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();

		try {
			db.delete(DbHandlerL.TABLE_MBS_MODULE_MASTER, null, null);
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('DA01','Dashboard ','Y','Y');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('SY01','Sync ','Y','Y');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('SE01','Settings ','Y','Y');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('HI01','History ','Y','Y');");

			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('CU01','Customer ','Y','Y');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('MS01','Message of the Day ','Y','Y');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('LD01','Loan Disbursement Module ','N','N');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('LR01','Loan Repayment Module ','N','N');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('DC01','Deposit Collection Module ','N','N');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('LP01','LoanPrepayment Module ','N','N');");

			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('DP01','Deposit Payment Module ','N','N');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('CE01','Customer Enrolment ','N','N');");
			db.execSQL("Insert into "
					+ DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " ("
					+ DbHandlerL.COL_MODULE_MODULE_CODE
					+ ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC
					+ ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT
					+ ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('CA01','Cash Deposit Withdrawal Module ','N','N');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('AG01','Agenda Module ','N','N');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('CR01','Customer Requests Module ','N','N');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('KC01','Customer KYC information ','N','N');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('OT01','Others ','N','N');");

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_MODULECODES
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_MODULECODES
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void moduleCodesToUpdate() throws SQLiteException,
			DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();

		try {
			db.delete(DbHandlerL.TABLE_MBS_MODULE_MASTER, null, null);
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('DA01','Dashboard ','Y','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('SY01','Sync ','Y','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('SE01','Settings ','Y','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('HI01','History ','Y','Y');");
			

			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('CU01','Customer ','Y','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('MS01','Message of the Day ','Y','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('LD01','Loan Disbursement Module ','N','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('LR01','Loan Repayment Module ','N','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('DC01','Deposit Collection Module ','N','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('LP01','LoanPrepayment Module ','N','Y');");

			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('DP01','Deposit Payment Module ','N','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('CE01','Customer Enrolment ','N','Y');");
			
			db.execSQL("Insert into "
					+ DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " ("
					+ DbHandlerL.COL_MODULE_MODULE_CODE
					+ ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC
					+ ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT
					+ ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('CA01','Cash Deposit Withdrawal Module ','N','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('AG01','Agenda Module ','N','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('CR01','Customer Requests Module ','N','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('KC01','Customer KYC information ','N','Y');");
			
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_MODULE_MASTER
					+ " (" + DbHandlerL.COL_MODULE_MODULE_CODE + ","
					+ DbHandlerL.COL_MODULE_MODULE_DESC + ","
					+ DbHandlerL.COL_MODULE_IS_PERMANENT + ","
					+ DbHandlerL.COL_MODULE_IS_LICENSED
					+ ") values ('OT01','Others ','N','Y');");

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_MODULECODES
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_MODULECODES
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
	}

	public void txnCodes() throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();

		try {
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('LN','L01','Loan Disbursement');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('LN','L02','Loan Repayment');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('LN','L21','Loan Prepayment Request');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('DP','D01','Deposit Collection');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('DP','D02','Deposit Maturity Payout');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('DP','D03','Deposit Redemption Payout');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('DP','D21','Deposit New Account Request');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('DP','D22','Deposit Redemption Request');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('DP','D23','Deposit Prepayment Request');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('CE','111','Customer Enrolment Info');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('CW','C21','Cash Deposit Request');");
			db.execSQL("Insert into " + DbHandlerL.TABLE_MBS_TXN_CODES + " ("
					+ DbHandlerL.COL_TXN_MODULE_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_CODES + ","
					+ DbHandlerL.COL_TXN_TXN_DESCS
					+ ") values ('CW','C22','Cash Withdrawal Request');");

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_TXNCODES
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_TXNCODES
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();

		}
	}

	public void insertErrorCodes() throws SQLiteException {
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00501','EN',' Request has been failed at remote system','E','A','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00259','EN','Lov List size is null','E','A','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI10131','EN','Enrolment List is Empty','E','A','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI10132','EN','Invalid Enrolment Id','E','A','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI10133','EN','Invalid Document Details','E','A','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00005','EN','AGENT CREDENTIALS FAILED','E','M','E');");

			db.execSQL("insert into " + DbHandlerL.TABLE_MBS_MESSAGE_CODE + "("
					+ DbHandlerL.COL_MESSAGE_CODE + ","
					+ DbHandlerL.COL_MESSAGE_LANG + ", "
					+ DbHandlerL.COL_MESSAGE_DESC + ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE + ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS + ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00001','EN','LOGIN ERROR','E','M','E');");

			db.execSQL("insert into " + DbHandlerL.TABLE_MBS_MESSAGE_CODE + "("
					+ DbHandlerL.COL_MESSAGE_CODE + ","
					+ DbHandlerL.COL_MESSAGE_LANG + ", "
					+ DbHandlerL.COL_MESSAGE_DESC + ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE + ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS + ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00002','EN','SYNC INFO','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00003','EN','PLEASE CHECK INTERNET CONNECTION','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00004','EN','DEVICE CREDENTIALS FAILED','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00006','EN','PLEASE ENTER YOUR CREDENTIALS','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00007','EN','PLEASE ENTER YOUR USERID','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00008','EN','PLEASE ENTER YOUR PASSWORD','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00009','EN','INVALID MOBILE NUMBER','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00010','EN','INVALID USERID OR PASSWORD','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00011','EN','PLEASE SYNC THE DATA TO MAKE TRANSACTION','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00012','EN','YOUR ACCOUNT IS  LOCKED, PLEASE CHECK USERID OR PASSWORD','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00013','EN','ENTRY FORM ERROR','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00014','EN','FIELDS SHOULD NOT BE EMPTY','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00015','EN','AMOUNT TO BE DISBURSE AND ENTRY AMOUNT SHOULD BE EQUAL','E','M','E');");

			db.execSQL("insert into " + DbHandlerL.TABLE_MBS_MESSAGE_CODE + "("
					+ DbHandlerL.COL_MESSAGE_CODE + ","
					+ DbHandlerL.COL_MESSAGE_LANG + ", "
					+ DbHandlerL.COL_MESSAGE_DESC + ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE + ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS + ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00016','EN','Payment Done','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00017','EN','Transaction Success','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00018','EN','Transaction Failed','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00019','EN','Transaction Failed. Existingdatetime != CurrentDatetime','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00020','EN','PLEASE ENTER NARRATIVE','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00021','EN','PLEASE ENTER REPAYMENT AMOUNT','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00023','EN','PLEASE ENTER FREQUENCY','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00024','EN','REPAYMENT AMOUNT SHOULD NOT BE ZERO','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00025','EN','REPAYMENT AMOUNT SHOULD NOT BE GREATER THEN AMOUNT TO BE PAID.','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00026','EN','NO DETAILS FOUND.','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00027','EN','SYSTEM DATE IS  NOT EQUALS TO CURRENT DATE','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00028','EN','PLEASE FILL THE MANDATORY FIELDS','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00029','EN','DISBURSEMENT COMPLETED','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFB00030','EN','NOT ENOUGH FUNDS','E','M','E');");

			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00502','EN','agentId is Null or UniqueId is Null or RegistKey is Null ','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00503','EN','Agent Registration Success','E','A','I');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00504','EN', 'Agent Id does not exist','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00505','EN', 'Agent is not active','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00506','EN', 'Registration Key is missing','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00507','EN', 'Invalid Device Id','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00508','EN', 'Invalid Current Date','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00509','EN', 'Invalid Registration Key','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00510','EN', 'Registration key has been timed out','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00511','EN', 'Invalid Session','E','A','E');");
			db.execSQL("insert into "
					+ DbHandlerL.TABLE_MBS_MESSAGE_CODE
					+ "("
					+ DbHandlerL.COL_MESSAGE_CODE
					+ ","
					+ DbHandlerL.COL_MESSAGE_LANG
					+ ", "
					+ DbHandlerL.COL_MESSAGE_DESC
					+ ","
					+ DbHandlerL.COL_MESSAGE_ERR_TYPE
					+ ","
					+ DbHandlerL.COL_MESSAGE_SUB_SYS
					+ ", "
					+ DbHandlerL.COL_MESSAGE_MSG_TYPE
					+ ") values ('MFI00512','EN', 'Agent Login Success','E','A','I');");

		} catch (SQLiteException e) {
			throw new SQLiteException(e.getMessage());
		} finally {
			db.close();
		}
	}

	@Override
	public void insertLOV() throws SQLiteException, DataAccessException {

	}
}
