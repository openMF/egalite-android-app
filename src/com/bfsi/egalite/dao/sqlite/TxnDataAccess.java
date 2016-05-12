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

import com.bfsi.egalite.dao.TransactionDao;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.mfi.rest.model.CashRecord;

@SuppressLint("NewApi")
public class TxnDataAccess implements TransactionDao {

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@SuppressLint("NewApi")
	@Override
	public long insertTrasanction(TxnMaster txn) throws DataAccessException,
			SQLiteException {

		long rowId = -1;
		SQLiteDatabase db = null;
		try {

			ContentValues values = new ContentValues();

			values.put(DbHandlerL.COL_TXN_TXN_ID, txn.getTxnId());
			values.put(DbHandlerL.COL_TXN_MODULE_CODE, txn.getModuleCode());
			values.put(DbHandlerL.COL_TXN_TXN_CODE, txn.getTxnCode());
			values.put(DbHandlerL.COL_TXN_AGENDA_ID, txn.getAgendaId());
			values.put(DbHandlerL.COL_TXN_SEQ_NO, txn.getSeqNo());
			values.put(DbHandlerL.COL_TXN_CBS_AC_REF_NO, txn.getCbsAcRefNo());
			values.put(DbHandlerL.COL_TXN_BRN_CODE, txn.getBrnCode());
			values.put(DbHandlerL.COL_TXN_CUSTOMER_ID, txn.getCustomerId());
			values.put(DbHandlerL.COL_TXN_INIT_TIME, txn.getInitTime());
			values.put(DbHandlerL.COL_TXN_IS_SYNCED, txn.getIsSynched());
			values.put(DbHandlerL.COL_TXN_AGENT_ID, txn.getAgentId());
			values.put(DbHandlerL.COL_TXN_DEVICE_ID, txn.getDeviceId());
			values.put(DbHandlerL.COL_TXN_LOCATION_CODE, txn.getLocationCode());
			values.put(DbHandlerL.COL_TXN_LN_IS_FUTURE_SCH,
					txn.getLnIsFutureSch());
			values.put(DbHandlerL.COL_TXN_TXN_AMT, txn.getTxnAmt());
			values.put(DbHandlerL.COL_TXN_AMT_LCY, txn.getAmtLcy());
			values.put(DbHandlerL.COL_TXN_SETTLED_AMT, txn.getSettledAmt());
			values.put(DbHandlerL.COL_TXN_SETTLED_AMT_LCY_NUM,
					txn.getSettledAmtLcyNum());

			values.put(DbHandlerL.COL_TXN_REQ_DEP_NO_INST,
					txn.getReqDepNoInst());
			values.put(DbHandlerL.COL_TXN_REQ_RED_REQ_DT, txn.getReqRedReqDt());
			values.put(DbHandlerL.COL_TXN_REQ_RED_FULL_PART_IND,
					txn.getReqRedReqFullPartInd());
			values.put(DbHandlerL.COL_TXN_REQ_MATURITY_DATE,
					txn.getReqMaturityDate());
			values.put(DbHandlerL.COL_TXN_REQ_INT_RATE, txn.getReqIntRate());
			values.put(DbHandlerL.COL_TXN_REQ_DP_TENURE_TYPE,
					txn.getReqDpTenureType());
			values.put(DbHandlerL.COL_TXN_REQ_DP_FREQUENCY_TYPE,
					txn.getReqDpFrequencyType());
			values.put(DbHandlerL.COL_TXN_REQ_DP_FREQUENCY,
					txn.getReqDpFrequency());
			values.put(DbHandlerL.COL_TXN_REQ_DP_TENURE, txn.getReqDpTenure());
			values.put(DbHandlerL.COL_TXN_CCY_CODE, txn.getCcyCode());
			values.put(DbHandlerL.COL_TXN_LCY_CODE, txn.getLcyCode());
			values.put(DbHandlerL.COL_TXN_FULL_PART_IND, txn.getFullPartInd());
			values.put(DbHandlerL.COL_TXN_TXN_STATUS, txn.getTxnStatus());
			values.put(DbHandlerL.COL_TXN_TXN_ERR_CODE, txn.getTxnErrorCode());
			values.put(DbHandlerL.COL_TXN_TXN_ERR_MSG, txn.getTxnErrorMsg());
			values.put(DbHandlerL.COL_TXN_GENERATED_MSG, txn.getGeneratedSms());
			values.put(DbHandlerL.COL_TXN_SMS_MOBILE_NO, txn.getSmsMobileNo());
			values.put(DbHandlerL.COL_TXN_SMS_CONTENT, txn.getSmsContent());
			values.put(DbHandlerL.COL_TXN_GENERATE_REVR, txn.getGenerateRevr());
			values.put(DbHandlerL.COL_TXN_MBS_TXN_SEQ_NO, txn.getMbsTxnSeqNo());
			values.put(DbHandlerL.COL_TXN_AC_TYPE, txn.getAccountType());

			db = DbHandlerL.getInstance().getWritableDatabase();
			rowId = db.insert(DbHandlerL.TABLE_MBS_TXN_MASTER,
					DbHandlerL.COL_TXN_TXN_ID, values);
			// write transaction to file
			// TransactionFileAccess.getInstance().write(txn);

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRTXN
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRTXN
					+ e.getMessage());

			throw new DataAccessException(e.getMessage(), e);

		} finally {
			if (db != null) {

				db.close();
			}
		}
		return rowId;
	}

	@Override
	public long insertCashRecord(CashRecord txn,TxnMaster master, long status)
			throws DataAccessException, SQLiteException {
		SQLiteDatabase db = null;
		db = DbHandlerL.getInstance().getWritableDatabase();
		long rowId = -1;
		try {
			
			if (status != 0) {
				String drCrIndicator = indicatorModule(txn.getTxnCode());

				ContentValues values = new ContentValues();
				values.put(DbHandlerL.COL_CASH_ENTRY_SEQ_NO,
						txn.getEntrySeqNo());
				values.put(DbHandlerL.COL_CASH_TXN_ID, txn.getCashTxnId());
				values.put(DbHandlerL.COL_CASH_AGENDA_ID, txn.getAgendaId());
				values.put(DbHandlerL.COL_CASH_TXN_SOURCE, txn.getTxnSource());
				values.put(DbHandlerL.COL_CASH_TXN_SEQ_NO, txn.getEntrySeqNo());
				values.put(DbHandlerL.COL_CASH_TXN_DATETIME,
						txn.getTxnDateTime());
				
				values.put(DbHandlerL.COL_CASH_TXN_CODE, txn.getTxnCode());
				values.put(DbHandlerL.COL_CASH_AGENT_ID, txn.getAgentId());
				values.put(DbHandlerL.COL_CASH_DEVICE_ID, txn.getDeviceId());
				values.put(DbHandlerL.COL_CASH_AGN_SEQ_NO, txn.getAgendaSeqNo());
				values.put(DbHandlerL.COL_CASH_TXN_CCY_CODE, txn.getTxnCcy());
				values.put(DbHandlerL.COL_CASH_DR_CR_IND, drCrIndicator);
				values.put(DbHandlerL.COL_CASH_CASH_AMT, txn.getAmount());
				values.put(DbHandlerL.COL_CASH_IS_REVERSAL, txn.getIsReversal());
				values.put(DbHandlerL.COL_CASH_IS_DELETED, txn.getIsDeleted());
				values.put(DbHandlerL.COL_CASH_AUTH_STAT, txn.getAuthStatus());

				rowId = db.insert(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null,
						values);
				if (rowId == -1) {

					db.delete(
							DbHandlerL.TABLE_MBS_TXN_MASTER,
							DbHandlerL.COL_TXN_TXN_ID + " = '"
									+ txn.getCashTxnId() + "'", null);
					if(!(master.getCbsAcRefNo()==null||master.getTxnCode().equalsIgnoreCase("D21")||
							master.getTxnCode().equalsIgnoreCase("D22")||master.getTxnCode().equalsIgnoreCase("D23")))
					{
						ContentValues updateValues = new ContentValues();
						updateValues.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
						db.update(DbHandlerL.TABLE_MBS_AGENDA_MASTER,
								updateValues, DbHandlerL.COL_AGENDA_CBS_AC_REF_NO
										+ " = '" + master.getCbsAcRefNo()
										+ "' AND " + DbHandlerL.COL_AGENDA_SEQ_NO
										+ " = '" + master.getSeqNo() + "'",
								null);
					}
				}
			} else {
				db.delete(DbHandlerL.TABLE_MBS_TXN_MASTER,
						DbHandlerL.COL_TXN_TXN_ID + " = '" + txn.getCashTxnId()
								+ "'", null);
				if(!(master.getCbsAcRefNo()==null||master.getTxnCode().equalsIgnoreCase("D21")||
						master.getTxnCode().equalsIgnoreCase("D22")||master.getTxnCode().equalsIgnoreCase("D23")))
				{
					ContentValues updateValues = new ContentValues();
					updateValues.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS, "0");
					db.update(DbHandlerL.TABLE_MBS_AGENDA_MASTER,
							updateValues, DbHandlerL.COL_AGENDA_CBS_AC_REF_NO
									+ " = '" + master.getCbsAcRefNo()
									+ "' AND " + DbHandlerL.COL_AGENDA_SEQ_NO
									+ " = '" + master.getSeqNo() + "'",
							null);
				}
			}
			return rowId;

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_INSRCASHREC
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_INSRCASHREC
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);

		} finally {
			if (db != null) {

				db.close();
			}
		}
	}

	private String indicatorModule(String txncode) {
		if (txncode.equals("L01") || txncode.equals("D02")
				|| txncode.equals("D03") || txncode.equals("C22"))
			return "D";
		else if (txncode.equals("L02") || txncode.equals("L21")
				|| txncode.equals("D01") || txncode.equals("D23")
				|| txncode.equals("C21")|| txncode.equals("D21")
				|| txncode.equals("D22") || txncode.equals("D23"))
			return "C";
		else
			return "C";
	}

	@Override
	public long updateAgenda(TxnMaster VerifyData, long rowId)
			throws DataAccessException {

		SQLiteDatabase db = null;
		db = DbHandlerL.getInstance().getWritableDatabase();
		long txnStatus = 0;
		long txnupdateid = 0;
		if (rowId != -1) {

			String url =  "";
			ContentValues updateValues = new ContentValues();
			updateValues.put(DbHandlerL.COL_AGENDA_AGENDA_STATUS,
					VerifyData.getAgendaStatus());
			
			url =  DbHandlerL.COL_AGENDA_CBS_AC_REF_NO + " = '"
					+ VerifyData.getCbsAcRefNo() + "' AND "
					+ DbHandlerL.COL_AGENDA_AGENDA_ID + " = '"
					+ VerifyData.getAgendaId() + "' AND "
					+ DbHandlerL.COL_AGENDA_SEQ_NO + " = '"
					+ VerifyData.getSeqNo() + "'";
			
			txnupdateid  = db.update(DbHandlerL.TABLE_MBS_AGENDA_MASTER,updateValues,url, null);
			
			if(txnupdateid != 0 && VerifyData.getIsGroupLoan() != null && (VerifyData.getTxnCode().equalsIgnoreCase("L01")||VerifyData.getTxnCode().equalsIgnoreCase("L02")||VerifyData.getTxnCode().equalsIgnoreCase("L21")))
			{
				if(VerifyData.getIsGroupLoan().equalsIgnoreCase("Y"))
				{
					url = DbHandlerL.COL_AGENDA_TXN_CODE + "='"+VerifyData.getTxnCode()+"' AND "+DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO
					+"='"+VerifyData.getParentCbsRefAcNo()+"' AND "+DbHandlerL.COL_AGENDA_CMP_ST_DATE+"='"+VerifyData.getAgendaCmpStartDate()
					+"' AND "+DbHandlerL.COL_AGENDA_IS_PARENT_LOAN+"='N' AND "+DbHandlerL.COL_AGENDA_SEQ_NO+"='"+VerifyData.getSeqNo()
					+"' AND "+DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER+"='N' AND "+DbHandlerL.COL_AGENDA_IS_GROUP_LOAN+"='Y'";
				
					txnStatus = db.update(DbHandlerL.TABLE_MBS_AGENDA_MASTER,updateValues,url, null);
					
					if (txnStatus == 0) {
						db.delete(
								DbHandlerL.TABLE_MBS_TXN_MASTER,
								DbHandlerL.COL_TXN_TXN_ID + " = '"
										+ VerifyData.getTxnId() + "'", null);
					}
				}else if(txnupdateid != 0 && VerifyData.getIsGroupLoan().equalsIgnoreCase("N")){
					txnStatus = txnupdateid;
				}
			}else if(txnupdateid != 0 && VerifyData.getIsGroupLoan() == null)
			{
				txnStatus = txnupdateid;
			}else
				txnStatus = txnupdateid;
			if(txnupdateid ==0)
			{
				db.delete(
						DbHandlerL.TABLE_MBS_TXN_MASTER,
						DbHandlerL.COL_TXN_TXN_ID + " = '"
								+ VerifyData.getTxnId() + "'", null);
			}

		} else {
			db.delete(DbHandlerL.TABLE_MBS_TXN_MASTER,
					DbHandlerL.COL_TXN_TXN_ID + " = '" + VerifyData.getTxnId()
							+ "'", null);

		}
		return txnStatus;
	}
	
	
	public long updateCashRecord(String authStatus)
			throws DataAccessException {
		long txnStatus = 0;
		SQLiteDatabase db = null;
		db = DbHandlerL.getInstance().getWritableDatabase();

		ContentValues updateValues = new ContentValues();
		updateValues.put(DbHandlerL.COL_CASH_AUTH_STAT,
				authStatus);
		String url =  DbHandlerL.COL_CASH_TXN_ID + " = '"
						+ CommonContexts.TXNID  + "'";
		txnStatus = db.update(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD,updateValues,url, null);
			

		return txnStatus;
	}

	@Override
	public int updateTxnStatus(List<String> existingId)
			throws DataAccessException, SQLiteException {
		long synctime = DateUtil.getCurrentDataTime();
		int txnStatus = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			List<String> txnIdLists = new ArrayList<String>();
			for (String id : existingId) {
				ContentValues updateValues = new ContentValues();
				updateValues.put(DbHandlerL.COL_TXN_IS_SYNCED, "S");
				updateValues.put(DbHandlerL.COL_TXN_INIT_TIME, synctime);
				txnStatus = db.update(DbHandlerL.TABLE_MBS_TXN_MASTER,
						updateValues, DbHandlerL.COL_TXN_TXN_ID + " = ?",
						new String[] { String.valueOf(id) });
				txnIdLists.add(id);
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_UPDATETXN_STATUS
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_UPDATETXN_STATUS
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return txnStatus;

	}

	public int updateTxnSmsText(String smsText,String txnId)
			throws DataAccessException, SQLiteException {
		int txnStatus = 0;
		SQLiteDatabase db = DbHandlerL.getInstance().getWritableDatabase();
		try {
			
				ContentValues updateValues = new ContentValues();
				updateValues.put(DbHandlerL.COL_TXN_SMS_CONTENT, smsText);
				updateValues.put(DbHandlerL.COL_TXN_GENERATED_MSG, "Y");
				txnStatus = db.update(DbHandlerL.TABLE_MBS_TXN_MASTER,
						updateValues, DbHandlerL.COL_TXN_TXN_ID + " = ?",
						new String[] { String.valueOf(txnId) });
				
			
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_UPDATETXN_STATUS
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_UPDATETXN_STATUS
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return txnStatus;
	}
	
	
	@Override
	public List<String> readUnSyncTxnStatus() throws DataAccessException {
		return null;
	}

	@Override
	public List<TxnMaster> getTxnAmt(String AccountNumber)
			throws DataAccessException {
		ArrayList<TxnMaster> notes = null;
		notes = new ArrayList<TxnMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT AT." + DbHandlerL.COL_TXN_TXN_ID + ", AT.  "
					+ DbHandlerL.COL_TXN_SETTLED_AMT + " FROM "
					+ DbHandlerL.TABLE_MBS_TXN_MASTER + " AT " + "WHERE "
					+ "  AT. " + DbHandlerL.COL_TXN_CBS_AC_REF_NO + "=" + "'"
					+ AccountNumber + "'";

			Cursor cursor = db.rawQuery(sql, null);

			if (cursor.moveToFirst()) {
				do {
					TxnMaster note = new TxnMaster();
					note.setTxnId(cursor.getString(0));
					note.setTxnAmt(cursor.getString(1));
					notes.add(note);
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (DataAccessException e) {
			LOG.debug(Constants.EXCP_GETTXN_AMT 
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);

		} finally {
			db.close();
		}
		return notes;
	}

	
	

}