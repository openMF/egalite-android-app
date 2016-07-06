package com.bfsi.egalite.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.dao.LoansDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.AgnLoanDetail;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.GroupLoans;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.mfi.rest.model.LoanPaidSch;

@SuppressLint("NewApi")
public class LoansDataAccess implements LoansDao {
	private Logger LOG = LoggerFactory.getLogger(getClass());

	public LoansDataAccess() {

	}

	@Override
	public List<AgendaMaster> repayAgenda() throws SQLiteException,
			DataAccessException {
		List<AgendaMaster> notes = null;
		notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + " *  FROM " + DbHandlerL.VIEW_AGN_LOANS
					+ " AG " + " WHERE " + DbHandlerL.COL_AGENDA_TXN_CODE
					+ "= 'L02' " + " AND "
					+ DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE + " ='X' "
					+ " AND " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH
					+ " = 'N'" + ";");

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
				note.setIsGroupLoan(cursor.getString(19));
				note.setCmpStDate(cursor.getString(17));
				note.setParentCbsRefNo(cursor.getString(23));
				note.setPhno(cursor.getString(24));
				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_REPAY_AGENDA + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_REPAY_AGENDA + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	public List<AgendaMaster> prepayAgenda() throws SQLiteException,
			DataAccessException {
		List<AgendaMaster> notes = null;
		notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + " *  FROM " + DbHandlerL.VIEW_AGN_LOANS
					+ " AG " + " WHERE " + DbHandlerL.COL_AGENDA_TXN_CODE
					+ "= 'L21' " + " AND "
					+ DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH + " = 'N'" + ";");

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
				note.setIsGroupLoan(cursor.getString(19));
				note.setCmpStDate(cursor.getString(17));
				note.setParentCbsRefNo(cursor.getString(23));
				note.setPhno(cursor.getString(24));
				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_REPAY_AGENDA + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_REPAY_AGENDA + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	@Override
	public List<String> readAccountNumbers() throws DataAccessException {
		return null;
	}

	@Override
	public List<String> readCustomerNames() throws DataAccessException {
		return null;
	}

	@Override
	public AgendaMaster readAccountDetails(String accountnumber)
			throws DataAccessException {
		return null;
	}

	@Override
	public List<AgendaMaster> readDsbAgenda() throws SQLiteException,
			DataAccessException {
		List<AgendaMaster> notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + " *  FROM " + DbHandlerL.VIEW_AGN_LOANS
					+ " AG " + " WHERE " + DbHandlerL.COL_AGENDA_TXN_CODE
					+ "= 'L01' " + " AND "
					+ DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE + " <> 'X' "
					+ " AND " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH
					+ " = 'N'" + ";");

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
				note.setLnDisbursementType(cursor.getString(16));
				note.setIsGroupLoan(cursor.getString(19));
				note.setCmpStDate(cursor.getString(17));
				note.setParentCbsRefNo(cursor.getString(23));
				note.setPhno(cursor.getString(24));
				
				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_DSB + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_DSB + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	public List<CustomerDetail> readCustomerDetails() throws SQLiteException,
			DataAccessException {
		List<CustomerDetail> notes = null;
		notes = new ArrayList<CustomerDetail>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + " AG. "
					+ DbHandlerL.COL_CUST_CUSTOMER_ID + " , " + " AG. "
					+ DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME + " , " + " AG. "
					+ DbHandlerL.COL_CUST_GENDER + " , " + " AG. "
					+ DbHandlerL.COL_CUST_DOB + " , " + " AG. "
					+ DbHandlerL.COL_CUST_ADDRESS_LINE1 + " , " + " AG. "
					+ DbHandlerL.COL_CUST_ADDRESS_LINE2 + " , " + " AG. "
					+ DbHandlerL.COL_CUST_ADDRESS_LINE3 + " , " + " AG. "
					+ DbHandlerL.COL_CUST_ADDRESS_LINE4 + " , " + " AG. "
					+ DbHandlerL.COL_CUST_CITY + " , " + " AG. "
					+ DbHandlerL.COL_CUST_STATE + " , " + " AG. "
					+ DbHandlerL.COL_CUST_COUNTRY + "," + " AG. "
					+ DbHandlerL.COL_CUST_LOC_BRANCH_CODE + "," + " AG. "
					+ DbHandlerL.COL_CUST_MOBILE_NUMBER + "  FROM "
					+ DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + " AG WHERE (AG."
					+ DbHandlerL.COL_CUST_CUSTOMER_ID + " LIKE '%"
					+ CommonContexts.SELECTED_CUSTOMER.getCustomerId()
					+ "%' OR AG." + DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME
					+ " LIKE '%"
					+ CommonContexts.SELECTED_CUSTOMER.getCustomerFullName()
					+ "%' OR AG." + DbHandlerL.COL_CUST_MOBILE_NUMBER
					+ " LIKE '%"
					+ CommonContexts.SELECTED_CUSTOMER.getMobileNumber()
					+ "%')" + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				CustomerDetail note = new CustomerDetail();
				note.setCustomerId(cursor.getString(0));
				note.setCustomerFullName(cursor.getString(1));
				note.setGender(cursor.getString(2));
				note.setDob(cursor.getString(3));
				note.setAddressLine1(cursor.getString(4));
				note.setAddressLine2(cursor.getString(5));
				note.setAddressLine3(cursor.getString(6));
				note.setAddressLine4(cursor.getString(7));
				note.setCity(cursor.getString(8));
				note.setState(cursor.getString(9));
				note.setCountry(cursor.getString(10));
				note.setLocalBranchCode(cursor.getString(11));
				note.setMobileNumber(cursor.getString(12));
				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_CUST_LOAN_DELT + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_CUST_LOAN_DELT + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	public int cntCustomerDetails() throws SQLiteException, DataAccessException {
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		int customerCount = 0;
		try {
			String sql = (" SELECT  COUNT(*)  FROM "
					+ DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + " AG WHERE (AG."
					+ DbHandlerL.COL_CUST_CUSTOMER_ID + " LIKE '%"
					+ CommonContexts.SELECTED_CUSTOMER.getCustomerId()
					+ "%' OR AG." + DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME
					+ " LIKE '%"
					+ CommonContexts.SELECTED_CUSTOMER.getCustomerFullName()
					+ "%' OR AG." + DbHandlerL.COL_CUST_MOBILE_NUMBER
					+ " LIKE '%"
					+ CommonContexts.SELECTED_CUSTOMER.getMobileNumber()
					+ "%')" + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				customerCount = (cursor.getString(0) == null ? 0 : Integer
						.parseInt(cursor.getString(0)));
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error("Exception while cntCustomerDetails : LoansDataAccess"
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception while cntCustomerDetails : LoansDataAccess "
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return customerCount;
	}

	@Override
	public Requests acNoCheckAgenda(String cbsAcRefNo) throws SQLiteException,
			DataAccessException {

		Requests note = new Requests();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = "SELECT AC." + DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME
					+ " , AL." + DbHandlerL.COL_LOAN_LOAN_AC_NO + " ,AC."
					+ DbHandlerL.COL_CUST_CUSTOMER_ID + " ,AL."
					+ DbHandlerL.COL_LOAN_LOAN_AC_CCY + " ,AL."
					+ DbHandlerL.COL_LOAN_BRANCH_CODE + " ,AL."
					+ DbHandlerL.COL_LOAN_LOCATION_CODE + " FROM "
					+ DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + " AC, "
					+ DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL + " AL WHERE "
					+ "  AC." + DbHandlerL.COL_CUST_CUSTOMER_ID + " =AL. "
					+ DbHandlerL.COL_LOAN_CUSTOMER_ID + " AND AL."
					+ DbHandlerL.COL_LOAN_LOAN_AC_NO + " ='" + cbsAcRefNo
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
					// note.setStatus(cursor.getString(6));
					// note.setCustomerId(cursor.getString(7));
					// note.setCurrency(cursor.getString(8));
					// note.setBranchCode(cursor.getString(9));
				} while (cursor.moveToNext());
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_ACNO_CHECK + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_ACNO_CHECK + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return note;
	}

	@Override
	public List<AgendaMaster> CustAccountDetails(String customerId)
			throws SQLiteException, DataAccessException {
		ArrayList<AgendaMaster> notes = null;
		notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {

			String sql = (" SELECT " + " LD. " + DbHandlerL.COL_LOAN_LOAN_AC_NO
					+ " FROM " + DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + " CD "
					+ "," + DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL + " LD "
					+ " WHERE " + " CD. " + DbHandlerL.COL_CUST_CUSTOMER_ID
					+ " = " + " LD. " + DbHandlerL.COL_LOAN_CUSTOMER_ID
					+ " AND  CD." + DbHandlerL.COL_CUST_CUSTOMER_ID + " = '"
					+ customerId + "'" + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				AgendaMaster note = new AgendaMaster();
				note.setCbsAcRefNo(cursor.getString(0));
				// note.setCustomerName(cursor.getString(1));
				// note.setCcyCode(cursor.getString(2));
				// note.setComponent(cursor.getString(3));
				// note.setAgendaAmt(cursor.getString(4));
				// note.setModuleCode(cursor.getString(5));
				// note.setTxnCode(cursor.getString(6));
				// note.setBranchCode(cursor.getString(7));
				// note.setCustomerId(cursor.getString(8));
				// note.setAgendaId(cursor.getString(9));
				// note.setDeviceId(cursor.getString(10));
				// note.setLocationCode(cursor.getString(11));
				// note.setLnIsFutureSch(cursor.getString(12));
				// note.setCcyCode(cursor.getString(13));
				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_CUST_ACC_DELT + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_CUST_ACC_DELT + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;

	}

	@Override
	public ArrayList<AgendaMaster> readPrepayAgenda() throws SQLiteException,
			DataAccessException {
		ArrayList<AgendaMaster> notes = null;
		notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {

			String sql = (" SELECT " + " AG. "
					+ DbHandlerL.COL_AGENDA_CBS_AC_REF_NO + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_CUSTOMER_NAME + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_CCY_CODE + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_CMP_NAME + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_AGENDA_AMT + " AG. "
					+ DbHandlerL.COL_AGENDA_MODULE_CODE + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_TXN_CODE + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_BRANCH_CODE + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_CUSTOMER_ID + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_AGENT_ID + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_DEVICE_ID + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_LOCATION_CODE + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH + " , " + " AG. "
					+ DbHandlerL.COL_AGENDA_CCY_CODE + " FROM "
					+ DbHandlerL.VIEW_AGN_LOANS + " AG " + " WHERE "
					+ DbHandlerL.COL_AGENDA_TXN_CODE + "= 'L21' " + " AND "
					+ DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE + " = 'X' "
					+ " AND " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH
					+ " = 'Y'" + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				AgendaMaster note = new AgendaMaster();
				note.setCbsAcRefNo(cursor.getString(0));
				note.setCustomerName(cursor.getString(1));
				note.setCcyCode(cursor.getString(2));
				note.setComponent(cursor.getString(3));
				note.setAgendaAmt(cursor.getString(4));
				note.setModuleCode(cursor.getString(5));
				note.setTxnCode(cursor.getString(6));
				note.setBranchCode(cursor.getString(7));
				note.setCustomerId(cursor.getString(8));
				note.setAgendaId(cursor.getString(9));
				note.setDeviceId(cursor.getString(10));
				note.setLocationCode(cursor.getString(11));
				note.setLnIsFutureSch(cursor.getString(12));
				note.setCcyCode(cursor.getString(13));
				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_PREPAY + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_PREPAY + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	@Override
	public List<String> agentControlAccess() throws SQLiteException,
			DataAccessException {
		List<String> note = new ArrayList<String>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {

			String sql = (" SELECT " + DbHandlerL.COL_MODULE_MODULE_CODE
					+ " FROM " + DbHandlerL.TABLE_MBS_MODULE_MASTER + " WHERE "
					+ DbHandlerL.COL_MODULE_IS_LICENSED + " = " + "'N'" + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				note.add(cursor.getString(0));

			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_PREPAY + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_PREPAY + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return note;
	}

	@Override
	public List<LoanPaidSch> getLoanPaidSchedule(String loanNumber)
			throws SQLiteException, DataAccessException {
		List<LoanPaidSch> notes = null;
		notes = new ArrayList<LoanPaidSch>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + " *  FROM "
					+ DbHandlerL.TABLE_MBS_AGN_LOAN_PAID_SCH + " AG "
					+ " WHERE " + DbHandlerL.COL_LOANPAID_CBS_AC_REF_NO + "= '"
					+ loanNumber + "' " + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {

				LoanPaidSch note = new LoanPaidSch();

				note.setCbsAcRefNo(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOANPAID_CBS_AC_REF_NO)));
				note.setBranchCode(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOANPAID_BRANCH_CODE)));
				note.setSchDueDate(cursor.getLong(cursor
						.getColumnIndex(DbHandlerL.COL_LOANPAID_SCH_DUE_DATE)));
				note.setSchPaidDate(cursor.getLong(cursor
						.getColumnIndex(DbHandlerL.COL_LOANPAID_SCH_PAID_DATE)));
				note.setStlmtCcyCode(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOANPAID_SETTLEMENT_CCY_CODE)));
				note.setAmtSettled(cursor.getDouble(cursor
						.getColumnIndex(DbHandlerL.COL_LOANPAID_AMT_SETTLED)));
				note.setFullPartialInd(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOANPAID_FULL_PARTIAL_IND)));

				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_LOAN_PAIDSCH + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_LOAN_PAIDSCH + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	@Override
	public AgnLoanDetail getLoanDetails(String loanNumber)
			throws SQLiteException, DataAccessException {
		AgnLoanDetail note = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + " *  FROM "
					+ DbHandlerL.TABLE_MBS_AGN_LOAN_DETAIL + " AG " + " WHERE "
					+ DbHandlerL.COL_LOAN_LOAN_AC_NO + "= '" + loanNumber
					+ "' " + ";");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {

				note = new AgnLoanDetail();

				note.setCustomerName(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOAN_CUSTOMER_NAME)));
				note.setCustomerId(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOAN_CUSTOMER_ID)));
				note.setLoanAcNo(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOAN_LOAN_AC_NO)));
				note.setDisbursedPrincipalAmt(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOAN_DISBURSED_PRINCIPAL_AMT)));
				note.setPrincipalOutstanding(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_LOAN_PRINCIPAL_OUTSTANDING)));

				
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_LOAN + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_LOAN + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return note;
	}

	/**
	 * To access the Group Loans : select * from mfi_agenda_master where
	 * parent_cbs_ac_ref_no = 'LBGRPDISBR001' and is_parent_loan = 'N' Which yet
	 * to be implemented based on the data to be shown.
	 */
	public List<GroupLoans> getGroupLoans(String loanNumber, String customerid)
			throws SQLiteException, DataAccessException {
		List<GroupLoans> notes = null;
		notes = new ArrayList<GroupLoans>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT " + DbHandlerL.COL_AGENDA_AGENDA_ID + ","
					+ DbHandlerL.COL_AGENDA_CBS_AC_REF_NO + ","
					+ DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO + ","
					+ DbHandlerL.COL_AGENDA_CUSTOMER_NAME + ","
					+ DbHandlerL.COL_AGENDA_TXN_CODE + ","
					+ DbHandlerL.COL_AGENDA_AGENT_ID + ","
					+ DbHandlerL.COL_AGENDA_CUSTOMER_ID + ","
					+ DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID + ","
					+ DbHandlerL.COL_AGENDA_IS_GROUP_LOAN + ","
					+ DbHandlerL.COL_AGENDA_IS_PARENT_LOAN + ","
					+ DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER + ","
					+ DbHandlerL.COL_AGENDA_AGENDA_AMT + "  FROM "
					+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " AG " + " WHERE "
					+ DbHandlerL.COL_AGENDA_MODULE_CODE + "='LN' AND "
					+ DbHandlerL.COL_AGENDA_TXN_CODE
					+ " IN ('L01','L02','L21') AND "
					+ DbHandlerL.COL_AGENDA_IS_GROUP_LOAN + "='Y' AND "
					+ DbHandlerL.COL_AGENDA_IS_PARENT_LOAN + "='N' AND "
					+ DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER + "='N' AND "
					+ DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO + "= '"
					+ loanNumber + "' AND "
					+ DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID + "='"
					+ customerid + "' ;");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				GroupLoans note = new GroupLoans();
				note.setAcNo(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_AGENDA_CBS_AC_REF_NO)));
				note.setCustomerName(cursor.getString(cursor
						.getColumnIndex(DbHandlerL.COL_AGENDA_CUSTOMER_NAME)));
				note.setAgendaAmt(cursor.getDouble(cursor
						.getColumnIndex(DbHandlerL.COL_AGENDA_AGENDA_AMT)));

				notes.add(note);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_LOAN_GRPLOAN + e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_LOAN_GRPLOAN + e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}
	
	public int repaymentCheck(String cbsRefNo)throws SQLiteException, DataAccessException 
	{
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		int customerCount = 0;
		try {
			String query  = (" SELECT " + " COUNT(*)  FROM "
					+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " AG WHERE AG."
					+ DbHandlerL.COL_AGENDA_CBS_AC_REF_NO + "= '"
					+ cbsRefNo + "' AND AG."+DbHandlerL.COL_AGENDA_TXN_CODE+"='L02' AND AG."+DbHandlerL.COL_AGENDA_AGENDA_STATUS+"='0';");

			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				customerCount = (cursor.getString(0) == null ? 0 : Integer
						.parseInt(cursor.getString(0)));
			}
			
			if(customerCount==0 )
			{
				String querys  = (" SELECT " + " COUNT(*)  FROM "
						+ DbHandlerL.TABLE_MBS_TXN_MASTER + " AG WHERE AG."
						+ DbHandlerL.COL_TXN_CBS_AC_REF_NO + "= '"
						+ cbsRefNo + "' AND AG."+DbHandlerL.COL_TXN_TXN_CODE+"='L02' AND AG."+DbHandlerL.COL_TXN_FULL_PART_IND+"='P' AND AG."+DbHandlerL.COL_TXN_TXN_STATUS+"='1';");
				
				Cursor cursors = db.rawQuery(querys, null);
				while (cursors.move(1)) {
					customerCount = (cursors.getString(0) == null ? 0 : Integer
							.parseInt(cursors.getString(0)));
				}
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error("Exception while cntCustomerDetails : LoansDataAccess"
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception while cntCustomerDetails : LoansDataAccess "
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return customerCount;
	}

}
