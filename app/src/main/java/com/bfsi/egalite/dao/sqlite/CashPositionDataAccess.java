package com.bfsi.egalite.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.dao.CashPositionDao;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CcyCodes;
import com.bfsi.egalite.entity.TxnCount;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;

@SuppressLint("NewApi")
public class CashPositionDataAccess implements CashPositionDao {

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public List<CcyCodes> readCurrencyList() throws SQLiteException,
			DataAccessException {
		List<CcyCodes> listCurrency = new ArrayList<CcyCodes>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String query = " SELECT DISTINCT "
					+ DbHandlerL.COL_CCY_CCY_CODE + " FROM "
					+ DbHandlerL.TABLE_MBS_CCY_CODES;
			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				CcyCodes currency = new CcyCodes();
				currency.setCcyCode(cursor.getString(0));
				listCurrency.add(currency);
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_CURR_LIST + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);

		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_CURR_LIST + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return listCurrency;
	}

	@Override
	public List<TxnCount> getAgnData() throws DataAccessException {
		
		//EGA-MN15-000013 : Display name changes
		List<TxnCount> count = new ArrayList<TxnCount>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String query = " SELECT TXN_NAME,PENDING AS P,EXECUTED AS E, SYNCED AS S, (PENDING+EXECUTED+SYNCED) AS TOTAL FROM "
					+

					"( SELECT "
					+ DbHandlerL.COL_AGENDA_TXN_CODE
					+ ","
					+ " (CASE WHEN "
					+ DbHandlerL.COL_AGENDA_TXN_CODE
					+ " = 'L01' THEN 'Loan \n Disbursement' "
					+ " WHEN "
					+ DbHandlerL.COL_AGENDA_TXN_CODE
					+ " =  'L02' THEN 'Loan \n Repayment' "
					+ " WHEN "
					+ DbHandlerL.COL_AGENDA_TXN_CODE
					+ " =  'L21' THEN 'Loan \n Prepayment' "
					+ " WHEN "
					+ DbHandlerL.COL_AGENDA_TXN_CODE
					+ " =  'D01' THEN 'Deposit \n Collection' "
					+ " WHEN "
					+ DbHandlerL.COL_AGENDA_TXN_CODE
					+ " =  'D02' THEN 'Deposit \n Maturity' "
					+ " WHEN "
					+ DbHandlerL.COL_AGENDA_TXN_CODE
					+ " =  'D03' THEN 'Deposit \n Redumption' "
					+ "  ELSE NULL END) TXN_NAME,"
					+ "  SUM(CASE  WHEN "
					+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
					+ "= 0 THEN 1 ELSE 0 END)  AS PENDING, "
					+ " SUM(CASE   WHEN "
					+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
					+ "= 1 THEN 1 ELSE 0 END)  AS EXECUTED, "
					+ " SUM(CASE  WHEN "
					+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
					+ "= 2 THEN 1 ELSE 0 END)  AS SYNCED "
					+ " FROM  "
					+ DbHandlerL.TABLE_MBS_AGENDA_MASTER
					+ " GROUP  BY "
					+ DbHandlerL.COL_AGENDA_TXN_CODE + ");";

			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				TxnCount data = new TxnCount();
				data.setTxnName(cursor.getString(0));
				data.setPending(cursor.getString(1));
				data.setExecuted(cursor.getString(2));
				data.setSynced(cursor.getString(3));
				data.setTotal(cursor.getString(4));
				count.add(data);
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_TXN_LIST + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);

		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_TXN_LIST + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return count;
	}
	
	
	
	public List<TxnCount> getTxnData() throws DataAccessException {
		
		//EGA-MN15-000013 : Display name changes
		List<TxnCount> count = new ArrayList<TxnCount>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String query = " SELECT TXN_NAME,PENDING_SYNCED AS P,EXECUTED AS E, SYNCED AS S,REVERSED AS R FROM "
					+

					"( SELECT "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ ","
					+ " (CASE WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " = 'L01' THEN 'Loan \n Disbursement' "
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'L02' THEN 'Loan \n Repayment' "
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'L21' THEN 'Loan \n Prepayment' "
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'D01' THEN 'Deposit \n Collection' "
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'D02' THEN 'Deposit \n Maturity' "
					
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'D03' THEN 'Deposit \n Redumption' "
					
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'D21' THEN 'Deposit \n New Acc' "
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'D22' THEN 'Deposit \n Redeem Req' "
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'D23' THEN 'Deposit \n Prepay Req' "
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'C21' THEN 'Cash Deposit' "
					+ " WHEN "
					+ DbHandlerL.COL_TXN_TXN_CODE
					+ " =  'C22' THEN 'Cash Withdrawal' "
					+ "  ELSE NULL END) TXN_NAME,"
					
					+ "  SUM(CASE  WHEN "
					+ DbHandlerL.COL_TXN_TXN_STATUS
					+ "= 0 THEN 1 ELSE 1 END)  AS EXECUTED, "
					
					+ " SUM(CASE  WHEN "
					+ DbHandlerL.COL_TXN_GENERATE_REVR
					+ "= 'Y' THEN 1 ELSE 0 END)  AS REVERSED, "
					+ " SUM(CASE  WHEN "
					+ DbHandlerL.COL_TXN_IS_SYNCED
					+ "= 1 THEN 1 ELSE 0 END)  AS SYNCED, "
					
					+ " SUM(CASE  WHEN "
					+ DbHandlerL.COL_TXN_IS_SYNCED
					+ "= 0 THEN 1 ELSE 0 END)  AS PENDING_SYNCED "
					
					+ " FROM  "
					+ DbHandlerL.TABLE_MBS_TXN_MASTER
					+ " GROUP  BY "
					+ DbHandlerL.COL_TXN_TXN_CODE + ");";

			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				TxnCount data = new TxnCount();
				data.setTxnName(cursor.getString(0));
				data.setPending(cursor.getString(1));
				data.setExecuted(cursor.getString(2));
				data.setSynced(cursor.getString(3));
				data.setReversed(cursor.getString(4));
				
				
				count.add(data);
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_TXN_LIST + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);

		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_TXN_LIST + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return count;
	}
	public List<TxnCount> getEnrlData() throws DataAccessException {
		
		//EGA-MN15-000013 : Display name changes
		List<TxnCount> count = new ArrayList<TxnCount>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String query = " SELECT PENDING_SYNCED AS P,EXECUTED AS E, SYNCED AS S FROM "
					+
					"( SELECT "
					+ "  SUM(CASE  WHEN "
					+ DbHandlerL.COL_MBS_ENRL_ISSYNCED
					+ "= 0 THEN 1 ELSE 1 END)  AS EXECUTED, "
					+ " SUM(CASE  WHEN "
					+ DbHandlerL.COL_MBS_ENRL_ISSYNCED
					+ "= 1 THEN 1 ELSE 0 END)  AS SYNCED, "
					+ " SUM(CASE  WHEN "
					+ DbHandlerL.COL_MBS_ENRL_ISSYNCED
					+ "= 0 THEN 1 ELSE 0 END)  AS PENDING_SYNCED "
					+ " FROM  "
					+ DbHandlerL.TABLE_MBS_ENROLMENT
					+");";

			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				TxnCount data = new TxnCount();
				data.setTxnName("Enrollment");
				data.setPending(cursor.getString(0));
				data.setExecuted(cursor.getString(1));
				data.setSynced(cursor.getString(2));
				data.setReversed("0");
				count.add(data);
			}
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_TXN_LIST + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);

		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_TXN_LIST + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return count;
	}
	
	

	@Override
	public CashPosition readCashPosition(String ccyCode, String agentId)
			throws DataAccessException {

		CashPosition currency = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			currency = new CashPosition();
			String openingBal = "SELECT sum(IFNULL("
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ ",0))   FROM "
					+ DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD
					+ " WHERE "
					+ DbHandlerL.COL_CASH_AGENT_ID
					+ "='"
					+ agentId
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CODE
					+ "='T01'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CCY_CODE
					+ "='"
					+ ccyCode
					+ "'"
					
					+ " AND "
					+ DbHandlerL.COL_CASH_DEVICE_ID + "='"
					+ CommonContexts.mLOGINVALIDATION.getDeviceId() + "'" + " AND "
					+ DbHandlerL.COL_CASH_IS_REVERSAL + "='N'" + " AND "
					+ DbHandlerL.COL_CASH_AUTH_STAT + "='A'";
			Cursor c_openingBal = db.rawQuery(openingBal, null);
			while (c_openingBal.move(1)) {

				currency.setOpeningBal(Double.parseDouble(c_openingBal
						.getString(0) == null ? "0" : c_openingBal.getString(0)));
			}
			String topUp = "SELECT sum(IFNULL("
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ ",0)) AS "
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ " FROM "
					+ DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD
					+ " WHERE "
					+ DbHandlerL.COL_CASH_AGENT_ID
					+ "='"
					+ agentId
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CODE
					+ "='T02'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CCY_CODE
					+ "='"
					+ ccyCode
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_DATETIME
					+ "='"
					+ DateUtil.stringToMillisecondsss(CommonContexts.dateMonthYear.format(DateUtil
							.getCurrentDataTime())) + "'" + " AND "
					+ DbHandlerL.COL_CASH_DEVICE_ID + "='"
					+ CommonContexts.mLOGINVALIDATION.getDeviceId() + "'" + " AND "
					+ DbHandlerL.COL_CASH_IS_REVERSAL + "='N'" + " AND "
					+ DbHandlerL.COL_CASH_AUTH_STAT + "='A'";
			Cursor c_topUp = db.rawQuery(topUp, null);
			while (c_topUp.move(1)) {
				currency.setTopUp(Double
						.parseDouble(c_topUp.getString(0) == null ? "0"
								: c_topUp.getString(0)));
			}

			String topDown = "SELECT sum(IFNULL("
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ ",0)) AS "
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ " FROM "
					+ DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD
					+ " WHERE "
					+ DbHandlerL.COL_CASH_AGENT_ID
					+ "='"
					+ agentId
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CODE
					+ "='T05'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CCY_CODE
					+ "='"
					+ ccyCode
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_DATETIME
					+ "='"
					+ DateUtil.stringToMillisecondsss(CommonContexts.dateMonthYear.format(DateUtil
							.getCurrentDataTime())) + "'" + " AND "
					+ DbHandlerL.COL_CASH_DEVICE_ID + "='"
					+ CommonContexts.mLOGINVALIDATION.getDeviceId() + "'" + " AND "
					+ DbHandlerL.COL_CASH_IS_REVERSAL + "='N'" + " AND "
					+ DbHandlerL.COL_CASH_AUTH_STAT + "='A'";
			Cursor c_topDown = db.rawQuery(topDown, null);
			while (c_topDown.move(1)) {
				currency.setTopDown(Double
						.parseDouble(c_topDown.getString(0) == null ? "0"
								: c_topDown.getString(0)));
			}

			String credit = "SELECT sum(IFNULL("
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ ",0)) AS "
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ " FROM "
					+ DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD
					+ " WHERE "
					+ DbHandlerL.COL_CASH_AGENT_ID
					+ "='"
					+ agentId
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CODE
					+ " not in ('T01','T02','T05')"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CCY_CODE
					+ "='"
					+ ccyCode
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_DATETIME
					+ "='"
					+ DateUtil.stringToMillisecondsss(CommonContexts.dateMonthYear.format(DateUtil
							.getCurrentDataTime())) + "'" + " AND "
					+ DbHandlerL.COL_CASH_DEVICE_ID + "='"
					+ CommonContexts.mLOGINVALIDATION.getDeviceId() + "'" + " AND "
					+ DbHandlerL.COL_CASH_IS_REVERSAL + "='N'" + " AND "
					+ DbHandlerL.COL_CASH_AUTH_STAT + "='A'" + "  AND "
					+ DbHandlerL.COL_CASH_DR_CR_IND + "='C'";
			Cursor c_credit = db.rawQuery(credit, null);
			while (c_credit.move(1)) {
				currency.setCreditAmt(Double
						.parseDouble(c_credit.getString(0) == null ? "0"
								: c_credit.getString(0)));
			}
			
//			String Systime = " SELECT  "
//					+ DbHandlerL.COL_CASH_TXN_DATETIME
//					+ " FROM "
//					+ DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD
//					+ " WHERE "
//					+ DbHandlerL.COL_CASH_AGENT_ID
//					+ "='"
//					+ agentId
//					+ "'"
//					+ " AND "
//					+ DbHandlerL.COL_CASH_TXN_CODE
//					+ " not in ('T01','T02','T05')"
//					+ " AND "
//					+ DbHandlerL.COL_CASH_TXN_CCY_CODE
//					+ "='"
//					+ ccyCode
//					+ "'"
////					+ " AND "
////					+ DbHandlerL.COL_CASH_TXN_DATETIME
////					+ "='"
////					+ CommonContexts.dateMonthYear.format(DateUtil
////							.getCurrentDataTime()) + "'" 
//					+ " AND "
//					+ DbHandlerL.COL_CASH_DEVICE_ID + "='"
//					+ CommonContexts.DEVICEID + "'" + " AND "
//					+ DbHandlerL.COL_CASH_IS_REVERSAL + "='N'" + " AND "
//					+ DbHandlerL.COL_CASH_AUTH_STAT + "='A'" + "  AND "
//					+ DbHandlerL.COL_CASH_DR_CR_IND + "='C'";
//			Cursor c_sys = db.rawQuery(Systime, null);
//			while (c_credit.move(1)) {
//				currency.setLastUpdateTime(c_sys.getString(0) == null ? "0"
//								: c_sys.getString(0));
//			}

			String debit = "SELECT sum(IFNULL("
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ ",0)) AS "
					+ DbHandlerL.COL_CASH_CASH_AMT
					+ " FROM "
					+ DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD
					+ " WHERE "
					+ DbHandlerL.COL_CASH_AGENT_ID
					+ "='"
					+ agentId
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CODE
					+ " not in ('T01','T02','T05')"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_CCY_CODE
					+ "='"
					+ ccyCode
					+ "'"
					+ " AND "
					+ DbHandlerL.COL_CASH_TXN_DATETIME
					+ "='"
					+ DateUtil.stringToMillisecondsss(CommonContexts.dateMonthYear.format(DateUtil
							.getCurrentDataTime())) + "'" + " AND "
					+ DbHandlerL.COL_CASH_DEVICE_ID + "='"
					+ CommonContexts.mLOGINVALIDATION.getDeviceId() + "'" + " AND "
					+ DbHandlerL.COL_CASH_IS_REVERSAL + "='N'" + " AND "
					+ DbHandlerL.COL_CASH_AUTH_STAT + "='A'" + "  AND "
					+ DbHandlerL.COL_CASH_DR_CR_IND + "='D'";
			Cursor c_debit = db.rawQuery(debit, null);
			while (c_debit.move(1)) {
				currency.setDebitAmt(Double
						.parseDouble(c_debit.getString(0) == null ? "0"
								: c_debit.getString(0)));
			}

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_CASHPOS + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);

		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_CASHPOS + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return currency;
	}
}
