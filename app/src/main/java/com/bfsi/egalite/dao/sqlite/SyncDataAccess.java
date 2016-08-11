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

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.SyncDao;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.mfi.rest.model.CashTransactionResponse;
import com.bfsi.mfi.rest.model.TransactionRequest;

public class SyncDataAccess implements SyncDao {
	private Logger LOG = LoggerFactory.getLogger(getClass());


	@SuppressLint("NewApi")
	public List<String> readCustomerIds() throws SQLiteException,
			DataAccessException {


		List<String> notes = null;
		notes = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT DISTINCT " + DbHandlerL.COL_CUST_CUSTOMER_ID
					+ " FROM " + DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + ";" ;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				notes.add(cursor.getString(0));
			}
			cursor.close();

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READCUSTID
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READCUSTID
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {

			db.close();
		}
		return notes;
	}

	public List<String> readLoanAcNos() {
		List<String> notes = null;
		notes = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT DISTINCT " + DbHandlerL.COL_LOAN_LOAN_AC_NO
					+ " FROM " + DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				notes.add(cursor.getString(0));
			}
			cursor.close();
		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	public List<String> readUnSyncTxnId() {
		List<String> notes = null;
		notes = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT DISTINCT " + " AT."
					+ DbHandlerL.COL_TXN_TXN_ID + " FROM "
					+ DbHandlerL.TABLE_MBS_TXN_MASTER + " AT " + " WHERE "
					+ DbHandlerL.COL_TXN_IS_SYNCED + " ='0'  AND "+DbHandlerL.COL_TXN_TXN_STATUS+" IN('0','1') ";

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				notes.add(cursor.getString(0));
			}
			cursor.close();
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}
	public List<String> readUnSyncEndId() {
		List<String> notes = null;
		notes = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT DISTINCT " + " AT."
					+ DbHandlerL.COL_MBS_ENRL_ENROLMENTID + " FROM "
					+ DbHandlerL.TABLE_MBS_ENROLMENT + " AT " + " WHERE "
					+ DbHandlerL.COL_MBS_ENRL_ISSYNCED + " ='0' ";

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				notes.add(cursor.getString(0));
			}
			cursor.close();
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}
	public List<String> readUnSyncBios() {
		List<String> notes = null;
		notes = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT DISTINCT " + " AT."
					+ DbHandlerL.COL_MBS_BIO_ENROLID + " FROM "
					+ DbHandlerL.TABLE_MBS_BIOMETRIC_DATA + " AT " ;

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				notes.add(cursor.getString(0));
			}
			cursor.close();
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}
	public TxnMaster readUnSyncTxns(String id) {
	TxnMaster txnMaster = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT * FROM "
					+ DbHandlerL.TABLE_MBS_TXN_MASTER + " AT " + " WHERE "
					+ DbHandlerL.COL_TXN_IS_SYNCED + " ='0' AND "+
					DbHandlerL.COL_TXN_TXN_ID+" = '"+id+"'";

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				 txnMaster = new TxnMaster();
				
				txnMaster.setTxnId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_ID)));
				txnMaster.setModuleCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_MODULE_CODE)));
				txnMaster.setTxnCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_CODE)));
				txnMaster.setAgendaId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_AGENDA_ID)));
				txnMaster.setSeqNo(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SEQ_NO)));
				txnMaster.setCbsAcRefNo(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_CBS_AC_REF_NO)));
				txnMaster.setBrnCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_BRN_CODE)));
				txnMaster.setCustomerId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_CUSTOMER_ID)));
				txnMaster.setInitTime(cursor.getLong(cursor.getColumnIndex(DbHandlerL.COL_TXN_INIT_TIME)));
				txnMaster.setAgentId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_AGENT_ID)));
				txnMaster.setDeviceId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_DEVICE_ID)));
				txnMaster.setLocationCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_LOCATION_CODE)));
				txnMaster.setLnIsFutureSch(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_LN_IS_FUTURE_SCH)));
				txnMaster.setTxnAmt(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_AMT)));
				txnMaster.setAmtLcy(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_AMT_LCY)));
				txnMaster.setSettledAmt(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SETTLED_AMT)));
				txnMaster.setSettledAmtLcyNum(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SETTLED_AMT_LCY_NUM)));
				
				txnMaster.setReqDepNoInst(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DEP_NO_INST)));
				txnMaster.setReqRedReqDt(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_RED_REQ_DT)));
				txnMaster.setReqRedReqFullPartInd(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_RED_FULL_PART_IND)));
				txnMaster.setReqMaturityDate(cursor.getLong(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_MATURITY_DATE)));
				txnMaster.setReqIntRate(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_INT_RATE)));
				txnMaster.setReqDpTenureType(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DP_TENURE_TYPE)));
				txnMaster.setReqDpFrequencyType(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DP_FREQUENCY_TYPE)));
				txnMaster.setReqDpFrequency(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DP_FREQUENCY)));
				txnMaster.setReqDpTenure(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DP_TENURE)));
				txnMaster.setCcyCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_CCY_CODE)));
				txnMaster.setLcyCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_LCY_CODE)));
				txnMaster.setFullPartInd(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_FULL_PART_IND)));
				
				txnMaster.setTxnStatus(cursor.getInt(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_STATUS)));
				txnMaster.setTxnErrorCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_ERR_CODE)));
				txnMaster.setTxnErrorMsg(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_ERR_MSG)));
				txnMaster.setGeneratedSms(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_GENERATED_MSG)));
				txnMaster.setSmsMobileNo(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SMS_MOBILE_NO)));
				txnMaster.setSmsContent(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SMS_CONTENT)));
				txnMaster.setGenerateRevr(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_GENERATE_REVR)));
				txnMaster.setMbsTxnSeqNo(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_MBS_TXN_SEQ_NO)));
			}
			cursor.close();
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return txnMaster;
	}
	
	public List<TxnMaster> readUnSyncTxns() {
		List<TxnMaster> notes = null;
		notes = new ArrayList<TxnMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT * FROM "
					+ DbHandlerL.TABLE_MBS_TXN_MASTER + " AT " + " WHERE "
					+ DbHandlerL.COL_TXN_IS_SYNCED + " ='0'";

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				TxnMaster txnMaster = new TxnMaster();
				
				txnMaster.setTxnId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_ID)));
				txnMaster.setModuleCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_MODULE_CODE)));
				txnMaster.setTxnCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_CODE)));
				txnMaster.setAgendaId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_AGENDA_ID)));
				txnMaster.setSeqNo(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SEQ_NO)));
				txnMaster.setCbsAcRefNo(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_CBS_AC_REF_NO)));
				txnMaster.setBrnCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_BRN_CODE)));
				txnMaster.setCustomerId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_CUSTOMER_ID)));
				txnMaster.setInitTime(cursor.getLong(cursor.getColumnIndex(DbHandlerL.COL_TXN_INIT_TIME)));
				txnMaster.setAgentId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_AGENT_ID)));
				txnMaster.setDeviceId(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_DEVICE_ID)));
				txnMaster.setLocationCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_LOCATION_CODE)));
				txnMaster.setLnIsFutureSch(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_LN_IS_FUTURE_SCH)));
				txnMaster.setTxnAmt(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_AMT)));
				txnMaster.setAmtLcy(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_AMT_LCY)));
				txnMaster.setSettledAmt(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SETTLED_AMT)));
				txnMaster.setSettledAmtLcyNum(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SETTLED_AMT_LCY_NUM)));
				txnMaster.setReqDepNoInst(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DEP_NO_INST)));
				txnMaster.setReqRedReqDt(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_RED_REQ_DT)));
				txnMaster.setReqRedReqFullPartInd(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_RED_FULL_PART_IND)));
				txnMaster.setReqMaturityDate(cursor.getLong(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_MATURITY_DATE)));
				txnMaster.setReqIntRate(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_INT_RATE)));
				txnMaster.setReqDpTenureType(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DP_TENURE_TYPE)));
				txnMaster.setReqDpFrequencyType(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DP_FREQUENCY_TYPE)));
				txnMaster.setReqDpFrequency(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DP_FREQUENCY)));
				txnMaster.setReqDpTenure(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_REQ_DP_TENURE)));
				txnMaster.setCcyCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_CCY_CODE)));
				txnMaster.setLcyCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_LCY_CODE)));
				txnMaster.setFullPartInd(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_FULL_PART_IND)));
				
				txnMaster.setTxnStatus(cursor.getInt(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_STATUS)));
				txnMaster.setTxnErrorCode(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_ERR_CODE)));
				txnMaster.setTxnErrorMsg(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_TXN_ERR_MSG)));
				txnMaster.setGeneratedSms(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_GENERATED_MSG)));
				txnMaster.setSmsMobileNo(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SMS_MOBILE_NO)));
				txnMaster.setSmsContent(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_SMS_CONTENT)));
				txnMaster.setGenerateRevr(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_GENERATE_REVR)));
				txnMaster.setMbsTxnSeqNo(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_TXN_MBS_TXN_SEQ_NO)));
				notes.add(txnMaster);
				
			}
			cursor.close();
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	public List<String> readUnSyncTxnId(String from, String to) {
		List<String> notes = null;
		notes = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT * FROM (SELECT st.*, Row_Number() OVER (ORDER BY "
					+ DbHandlerL.COL_TXN_TXN_ID
					+ ") rn FROM "
					+ DbHandlerL.TABLE_MBS_TXN_MASTER
					+ "  st where st."
					+ DbHandlerL.COL_TXN_IS_SYNCED
					+ "=A )WHERE rn BETWEEN '"
					+ from + "' AND '" + to + "'";
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				notes.add(cursor.getString(0));
			}
			cursor.close();
		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}
	
	

	public int updateGroupLoans()throws DataAccessException {
		int txnStatus = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		List<String> cbsrefList = new ArrayList<String>();
		try {

			String query = "SELECT "+DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO+" FROM "+DbHandlerL.TABLE_MBS_AGENDA_MASTER+" WHERE "+
			DbHandlerL.COL_AGENDA_IS_GROUP_LOAN+"='Y' AND "+DbHandlerL.COL_AGENDA_IS_PARENT_LOAN+"='Y' AND "+DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER+"='Y' AND "+
					DbHandlerL.COL_AGENDA_TXN_CODE+" IN ('L01','L02','L21') AND "+DbHandlerL.COL_AGENDA_AGENDA_STATUS+"='2'"; 
			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				cbsrefList.add(cursor.getString(cursor.getColumnIndex(DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO)));
			}
			cursor.close();
			
			if(cbsrefList.size() >0)
			{
				for(String cbsrefno : cbsrefList)
				{
					ContentValues updateAgendas = new ContentValues();
					updateAgendas.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "2");
					txnStatus =  db.update(DbHandlerL.TABLE_MBS_AGENDA_MASTER,
							updateAgendas,DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO
					+ " = '" + cbsrefno+ "' AND " + DbHandlerL.COL_AGENDA_IS_GROUP_LOAN
					+ " = 'Y' AND "+DbHandlerL.COL_AGENDA_IS_PARENT_LOAN+"='Y' AND "+DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER+"='N' AND "+DbHandlerL.COL_AGENDA_TXN_CODE+" IN ('L01','L02','L21') ",null);
				}
			}
			
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return txnStatus;
	}
	
	
	public int updateSyncStatus(List<TransactionRequest> txnIdList) {

		long synctime = DateUtil.getCurrentDataTime();
		int txnStatus = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			for (TransactionRequest transactionobject : txnIdList) {
				String txnId = transactionobject.getTxnId();
				ContentValues updateValues = new ContentValues();
				updateValues.put(DbHandlerL.COL_TXN_IS_SYNCED, "1");
				updateValues.put(DbHandlerL.COL_TXN_INIT_TIME, synctime);
				updateValues.put(DbHandlerL.COL_TXN_TXN_STATUS, 1);
				updateValues.put(DbHandlerL.COL_TXN_TXN_ERR_MSG, "null");
				updateValues.put(DbHandlerL.COL_TXN_TXN_ERR_CODE, "null");
				updateValues.put(DbHandlerL.COL_TXN_GENERATE_REVR, "X");
				
				txnStatus = db.update(DbHandlerL.TABLE_MBS_TXN_MASTER,
						updateValues, DbHandlerL.COL_TXN_TXN_ID + " = ?",
						new String[] { String.valueOf(txnId) });
				
				//update agenda status
				ContentValues updateAgendas = new ContentValues();
				updateAgendas.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "2");
				txnStatus =  db.update(DbHandlerL.TABLE_MBS_AGENDA_MASTER,
						updateAgendas,DbHandlerL.COL_AGENDA_AGENDA_ID
				+ " = '" + String.valueOf(transactionobject.getAgendaId())
				+ "' AND " + DbHandlerL.COL_AGENDA_SEQ_NO
				+ " = '" + transactionobject.getSeqNo() + "'",null);
			}
		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return txnStatus;
	}
	
	public int updateCashSyncStatus(CashTransactionResponse serverResponse) {

		long synctime = DateUtil.getCurrentDataTime();
		int txnStatus = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
//				String txnId = transactionobject.getTxnId();
				ContentValues updateValues = new ContentValues();
				updateValues.put(DbHandlerL.COL_TXN_IS_SYNCED, "1");
				updateValues.put(DbHandlerL.COL_TXN_INIT_TIME, synctime);
				updateValues.put(DbHandlerL.COL_TXN_TXN_STATUS, 1);
				updateValues.put(DbHandlerL.COL_TXN_TXN_ERR_MSG, "null");//get error code from response
				updateValues.put(DbHandlerL.COL_TXN_TXN_ERR_CODE, "null");//get error msg from response
				updateValues.put(DbHandlerL.COL_TXN_GENERATE_REVR, "N");
				
				//need to check the server response
				txnStatus = db.update(DbHandlerL.TABLE_MBS_TXN_MASTER,
						updateValues, DbHandlerL.COL_TXN_TXN_ID + " = ?",
						new String[] { String.valueOf(CommonContexts.TXNID) });
				
				
				
		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return txnStatus;
	}
	
	public int updateCashTxnUnSyncStatus(CashTransactionResponse serverResponse) {

		long synctime = DateUtil.getCurrentDataTime();
		int txnStatus = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
//				String txnId = transactionobject.getTxnId();
				ContentValues updateValues = new ContentValues();
				updateValues.put(DbHandlerL.COL_TXN_IS_SYNCED, "0");
				updateValues.put(DbHandlerL.COL_TXN_INIT_TIME, synctime);
				updateValues.put(DbHandlerL.COL_TXN_TXN_STATUS, 0);
				updateValues.put(DbHandlerL.COL_TXN_TXN_ERR_MSG, serverResponse.getStatusText());//get error code from response
				updateValues.put(DbHandlerL.COL_TXN_TXN_ERR_CODE, serverResponse.getMessageCode());//get error msg from response
				updateValues.put(DbHandlerL.COL_TXN_GENERATE_REVR, "Y");
				
				//need to check the server response
				txnStatus = db.update(DbHandlerL.TABLE_MBS_TXN_MASTER,
						updateValues, DbHandlerL.COL_TXN_TXN_ID + " = ?",
						new String[] { String.valueOf(CommonContexts.TXNID) });
				
				
				
		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return txnStatus;
	}

	/**
	 * Reads all transactionId (file names)
	 */
	public void readTxnFileNames() {
		SyncDao syncDao = DaoFactory.getSyncDao();
		try {
			@SuppressWarnings("unused")
			List<String> txnId = syncDao.readUnSyncTxnId();
		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}

	}

	/*public TxnMaster readDataFromFile(String id) throws DataAccessException {
		TxnMaster txnMaster = null;
		try {
			txnMaster = TransactionFileAccess.getInstance().read(id);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);

		}
		return txnMaster;

	}*/
	@Override
	public void deleteMaintainceData()throws SQLiteException
	{
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try{
			
			db.delete(DbHandlerL.TABLE_MBS_AGENDA_MASTER, null, null);
			db.delete(DbHandlerL.TABLE_MBS_TXN_MASTER, null, null);
			db.delete(DbHandlerL.TABLE_MBS_ENROLMENT, null, null);
			db.delete(DbHandlerL.TABLE_MBS_BIOMETRIC_DATA, null, null);
			db.delete(DbHandlerL.TABLE_MBS_ENROLMENT_DOCUMENT, null, null);
			db.delete(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, null);
			
		}catch(SQLiteException e)
		{
			throw new SQLiteException(e.getMessage());
		}finally{
			db.close();
		}
		
	}
	public void deleteDDMaintainceData()throws SQLiteException
	{
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try{
			db.delete(DbHandlerL.TABLE_MBS_BRANCH_DETAILS,null,null);
			db.delete(DbHandlerL.TABLE_MBS_CCY_CODES, null, null);
			db.delete(DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL, null, null);
			db.delete(DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL, null, null);
			db.delete(DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL, null, null);
			db.delete(DbHandlerL.TABLE_MBS_LOV, null, null);
			db.delete(DbHandlerL.TABLE_MBS_SMS_TEMPLATE, null, null);
			db.delete(DbHandlerL.TABLE_MBS_MESSAGE_CODE, null, null);
			db.delete(DbHandlerL.TABLE_MBS_MODULE_MASTER, null, null);
			db.delete(DbHandlerL.TABLE_MBS_AGN_LOAN_PAID_SCH,null,null);
			db.delete(DbHandlerL.TABLE_MBS_CUSTACC_DETAIL,null,null);
			db.delete(DbHandlerL.TABLE_MBS_CUSTACC_DETAIL,null,null);
			
			
		}catch(SQLiteException e)
		{
			throw new SQLiteException(e.getMessage());
		}finally{
			db.close();
		}
		
	}

} 