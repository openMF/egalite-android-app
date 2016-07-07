package com.bfsi.egalite.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;

@SuppressLint("NewApi")
public class CustomersDataAccess implements CustomerDao {
	private Logger LOG = LoggerFactory.getLogger(getClass());

	@SuppressLint("NewApi")
	@Override
	public List<CustomerDetail> readCustomerDetails() throws SQLiteException,
			DataAccessException {

		List<CustomerDetail> notes = new ArrayList<CustomerDetail>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT * " + " FROM "
					+ DbHandlerL.VIEW_MBS_CUSTOMERS + " AG ;");
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				CustomerDetail hd = new CustomerDetail();
				hd.setCustomerFullName(cursor.getString(0));
				hd.setDob(cursor.getString(1));
				hd.setGender(cursor.getString(2));
				hd.setMobileNumber(cursor.getString(3));
				hd.setAddressLine1(cursor.getString(4));
				hd.setAddressLine2(cursor.getString(5));
				hd.setAddressLine3(cursor.getString(6));
				hd.setAddressLine4(cursor.getString(7));
				hd.setCity(cursor.getString(8));
				hd.setState(cursor.getString(9));
				hd.setCustomerId(cursor.getString(10));
				hd.setCountry(cursor.getString(11));
				hd.setCustomerCategory(cursor.getString(12));
				hd.setZipCode(cursor.getString(13));
				hd.setLocationCode(cursor.getString(14));
				
				notes.add(hd);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_CUST_DETL
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		}

		catch (Exception e) {
			LOG.error(Constants.EXCP_READ_CUST_DETL
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;

	}

	public List<CustomerDetail> readCashDetails(String custId)
			throws SQLiteException, DataAccessException {

		List<CustomerDetail> notes = new ArrayList<CustomerDetail>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT  " + DbHandlerL.COL_CUST_CUSTOMER_ID + " , "
					+ DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME + " , "
					+ DbHandlerL.COL_CUSTACC_CUST_AC_NO + " , "
					+ DbHandlerL.COL_CUSTACC_BRANCH_CODE + " , "
					+ DbHandlerL.COL_CUSTACC_ACCOUNT_TYPE + " , "
					+ DbHandlerL.COL_CUSTACC_AC_CCY + " , "
					+ DbHandlerL.COL_CUSTACC_LOCATION_CODE + "  " + " FROM "
					+ DbHandlerL.TABLE_MBS_CUSTACC_DETAIL + " AG WHERE "
					+ DbHandlerL.COL_CUST_CUSTOMER_ID + " = " + "'" + custId + "' ;"

			);
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				CustomerDetail hd = new CustomerDetail();
				hd.setCustomerId(cursor.getString(0));
				hd.setCustomerFullName(cursor.getString(1));
				hd.setAcNo(cursor.getString(2));
				hd.setLocalBranchCode(cursor.getString(3));
				hd.setAccType(cursor.getString(4));
				hd.setAccCcy(cursor.getString(5));
				hd.setLocationCode(cursor.getString(6));
				notes.add(hd);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_CASH
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		}

		catch (Exception e) {
			LOG.error(Constants.EXCP_READ_CASH
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;

	}

	@Override
	public List<AgendaMaster> customerAgenda() throws SQLiteException,
			DataAccessException {
		List<AgendaMaster> notes = new ArrayList<AgendaMaster>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT  * FROM "
					+ DbHandlerL.VIEW_MBS_CUSTOMERS_AGN_ALL + " AG "
					+ " WHERE (AG." + DbHandlerL.COL_AGENDA_CUSTOMER_ID + " ='"
					+ CommonContexts.SELECTED_CUSTOMER.getCustomerId() + "')" + ";"

			);
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
				note.setIsGroupLoan(cursor.getString(17));
				note.setIsParentLoan(cursor.getString(18));
				note.setIsParentCustomer(cursor.getString(19));
				note.setParentCustomerId(cursor.getString(20));
				note.setParentCbsRefNo(cursor.getString(21));
				note.setPhno(cursor.getString(22));
				notes.add(note);

			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_CUST_AGENDA 
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		}

		catch (Exception e) {
			LOG.error(Constants.EXCP_CUST_AGENDA 
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;
	}

	@Override
	public List<CustomerDetail> readCustInfo(String custId)
			throws SQLiteException, DataAccessException {
		List<CustomerDetail> notes = new ArrayList<CustomerDetail>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT  " + DbHandlerL.COL_CUST_CUSTOMER_ID + " , "
					+ DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME + " , "
					+ DbHandlerL.COL_CUST_DOB + " , "
					+ DbHandlerL.COL_CUST_MOBILE_NUMBER + "  " + " FROM "
					+ DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + "  WHERE "
					+ DbHandlerL.COL_CUST_CUSTOMER_ID + " = " + "'" + custId + "' ;"

			);
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				CustomerDetail hd = new CustomerDetail();
				hd.setCustomerId(cursor.getString(0));
				hd.setCustomerFullName(cursor.getString(1));
				hd.setDob(cursor.getString(2));
				hd.setMobileNumber(cursor.getString(3));
				notes.add(hd);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_CUSTINFO
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		}

		catch (Exception e) {
			LOG.error(Constants.EXCP_READ_CUSTINFO
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return notes;

	}
	public String smsRequired(String custId)throws DataAccessException
	{
		String smsRequired = null;
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String sql = (" SELECT  " + DbHandlerL.COL_CUST_SMS_REQUIRED + " FROM "
					+ DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + "  WHERE "
					+ DbHandlerL.COL_CUST_CUSTOMER_ID + " = " + "'" + custId + "' ;"

			);
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				smsRequired = cursor.getString(0);
				
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_SMS_REQ
					+ e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		}

		catch (Exception e) {
			LOG.error(Constants.EXCP_SMS_REQ
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} finally {
			db.close();
		}
		return smsRequired;
	}

}
