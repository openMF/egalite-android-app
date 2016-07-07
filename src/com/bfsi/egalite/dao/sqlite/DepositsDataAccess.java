package com.bfsi.egalite.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.dao.DepositsDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.AgnDepositDetail;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.Constants;

@SuppressLint("NewApi")
public class DepositsDataAccess implements DepositsDao {
	private Logger LOG = LoggerFactory.getLogger(getClass());

	public DepositsDataAccess() {
	}

	@SuppressLint("NewApi")
	@Override
	public List<AgendaMaster> readAgendaPayments() throws SQLiteException,
			DataAccessException {

		List<AgendaMaster> notes = null;
		notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + "  * FROM "
					+ DbHandlerL.VIEW_AGN_DEPOSITS + " AG " + " WHERE "
					+ DbHandlerL.COL_AGENDA_TXN_CODE + " IN( 'D02','D03') " + ";");
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				AgendaMaster note = new AgendaMaster();
				note.setAgendaId(cursor.getString(0));
				note.setSeqNo(cursor.getString(1));
				note.setCbsAcRefNo(cursor.getString(2));
				note.setModuleCode(cursor.getString(3));
				note.setTxnCode(cursor.getString(4));
				note.setAgentId(cursor.getString(5));
				note.setLocationCode(cursor.getString(6));
				note.setAgendaStatus(cursor.getString(7));
				note.setBranchCode(cursor.getString(8));
				note.setCustomerId(cursor.getString(9));
				note.setCustomerName(cursor.getString(10));
				note.setCcyCode(cursor.getString(11));
				note.setAgendaAmt(String.valueOf(Double.parseDouble(cursor
						.getString(12))));
				note.setLnIsFutureSch(cursor.getString(13));
				note.setComponent(cursor.getString(14));
				note.setDeviceId(cursor.getString(15));
				note.setDpIsRedemption(cursor.getString(16));
				note.setCmpStDate(cursor.getString(17));
				note.setParentCbsRefNo(cursor.getString(19));
				note.setPhno(cursor.getString(20));

				notes.add(note);
			}
			cursor.close();

		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_AGENDA_PAYMENT + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.debug(Constants.EXCP_AGENDA_PAYMENT + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {

			db.close();
		}
		return notes;
	}

	@Override
	public List<AgendaMaster> readAgendaCollections() throws SQLiteException,
			DataAccessException {
		List<AgendaMaster> notes = null;
		notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + " *  FROM "
					+ DbHandlerL.VIEW_AGN_DEPOSITS + " AG " + " WHERE "
					+ DbHandlerL.COL_AGENDA_TXN_CODE + "= 'D01' " + " AND "
					+ DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION + " <>'X' " + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				AgendaMaster note = new AgendaMaster();

				note.setAgendaId(cursor.getString(0));
				note.setSeqNo(cursor.getString(1));
				note.setCbsAcRefNo(cursor.getString(2));
				note.setModuleCode(cursor.getString(3));
				note.setTxnCode(cursor.getString(4));
				note.setAgentId(cursor.getString(5));
				note.setLocationCode(cursor.getString(6));
				note.setAgendaStatus(cursor.getString(7));
				note.setBranchCode(cursor.getString(8));
				note.setCustomerId(cursor.getString(9));
				note.setCustomerName(cursor.getString(10));
				note.setCcyCode(cursor.getString(11));
				note.setAgendaAmt(String.valueOf(Double.parseDouble(cursor
						.getString(12))));
				note.setLnIsFutureSch(cursor.getString(13));
				note.setComponent(cursor.getString(14));
				note.setDeviceId(cursor.getString(15));
				note.setCmpStDate(cursor.getString(17));
				note.setParentCbsRefNo(cursor.getString(19));
				note.setPhno(cursor.getString(20));

				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.debug(Constants.EXCP_READ_AGENDA_COLLC + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);

		} catch (Exception e) {
			LOG.debug(Constants.EXCP_READ_AGENDA_COLLC + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);

		} finally {
			db.close();
		}
		return notes;
	}

	@Override
	public List<TxnMaster> getTxnAmt(String AccountNumber)
			throws DataAccessException {
		return null;
	}

	@Override
	public List<AgendaMaster> CustAccountDetails(String customerId)
			throws DataAccessException {
		return null;
	}

	@Override
	public Requests readAccountDetails(String cbsAcRefNo)
			throws SQLiteException, DataAccessException {
		Requests note = new Requests();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT AC." + DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME
					+ " , AD."+ DbHandlerL.COL_DEPOSIT_DEP_AC_NO
					+ " ,AC."+ DbHandlerL.COL_CUST_CUSTOMER_ID 
					+ " ,AD."+ DbHandlerL.COL_DEPOSIT_AC_CCY 
					+ " ,AD."+ DbHandlerL.COL_DEPOSIT_BRANCH_CODE 
					+ " ,AC."+ DbHandlerL.COL_LOAN_LOCATION_CODE
					+ " , AD." + DbHandlerL.COL_DEPOSIT_OPEN_DATE 
					+ " , AD." + DbHandlerL.COL_DEPOSIT_MATURITY_DATE 
					+ " , AD." + DbHandlerL.COL_DEPOSIT_SCH_INSTALLMENT_AMT 
					+ " , AC." + DbHandlerL.COL_CUST_MOBILE_NUMBER
					+ " , AC." + DbHandlerL.COL_CUST_SMS_REQUIRED
					+ " FROM "
					+ DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + " AC, "
					+ DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL + " AD WHERE "
					+ "  AC." + DbHandlerL.COL_CUST_CUSTOMER_ID + " =AD. "
					+ DbHandlerL.COL_DEPOSIT_CUSTOMER_ID + " AND AD."
					+ DbHandlerL.COL_DEPOSIT_DEP_AC_NO + " ='" + cbsAcRefNo
					+ "';";

			Cursor cursor = db.rawQuery(sql, null);

			if (cursor.moveToFirst()) {
				do {
					note.setCustomerName(cursor.getString(0));
					note.setAcNo(cursor.getString(1));
					note.setCustomerId(cursor.getString(2));
					note.setCcyCode(cursor.getString(3));
					note.setBranchCode(cursor.getString(4));
					note.setLocCode(cursor.getString(5));
					note.setDepOpenDate(cursor.getString(6));
					note.setMaturityDate(cursor.getLong(7));
					note.setDepInstallmentAmt(cursor.getString(8));
					note.setMobileNumber(cursor.getString(9));
					note.setSmsRequired(cursor.getString(10));
					
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_ACC_DETAIL + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_ACC_DETAIL + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return note;
	}

	@Override
	public List<AgendaMaster> CustDepositDetails(String customerId)
			throws DataAccessException, SQLiteException {
		ArrayList<AgendaMaster> notes = null;
		notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {

			String sql = (" SELECT " + " LD. "
					+ DbHandlerL.COL_DEPOSIT_DEP_AC_NO + ","
					+ DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME + " FROM "
					+ DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + " CD " + ","
					+ DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL + " LD "
					+ " WHERE " + " CD. " + DbHandlerL.COL_CUST_CUSTOMER_ID
					+ " = " + " LD. " + DbHandlerL.COL_DEPOSIT_CUSTOMER_ID
					+ " AND CD." + DbHandlerL.COL_CUST_CUSTOMER_ID + " = '"
					+ customerId + "'" + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				AgendaMaster note = new AgendaMaster();
				note.setCbsAcRefNo(cursor.getString(0));
				note.setCustomerName(cursor.getString(1));
				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_CUST_DEPOSITE + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_CUST_DEPOSITE + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	@Override
	public AgnDepositDetail getDepositDetails(String loanNumber)
			throws SQLiteException, DataAccessException {
		AgnDepositDetail note = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + " *  FROM "
					+ DbHandlerL.TABLE_MBS_AGN_DEPOSIT_DETAIL + " AG "
					+ " WHERE " + DbHandlerL.COL_DEPOSIT_DEP_AC_NO + "= '"
					+ loanNumber + "' " + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {

				note = new AgnDepositDetail();

				note.setCustomerName(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_DEPOSIT_CUSTOMER_NAME)));
				note.setCustomerId(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_DEPOSIT_CUSTOMER_ID)));
				note.setDepAcNo(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_DEPOSIT_DEP_AC_NO)));
				note.setPrincipalMaturityAmount(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT)));
				note.setInstallmentPaidTillDate(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE)));
				note.setTotalInsatllmentAmtDue(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE)));

			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_DEPOSIT + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_DEPOSIT + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return note;
	}
}
