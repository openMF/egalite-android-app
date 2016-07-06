package com.bfsi.egalite.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.dao.HistoryDao;
import com.bfsi.egalite.entity.HistoryDetails;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.Constants;

@SuppressLint("NewApi")
public class HistoryDataAccess implements HistoryDao {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	@Override
	public List<HistoryDetails>  readHistoryData() throws SQLiteException ,DataAccessException
	{
		List<HistoryDetails> notes = new ArrayList<HistoryDetails>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {

			String sql = (" SELECT * " + " FROM "
					+ DbHandlerL.VIEW_MBS_HISTORY + " AG ;");

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.move(1)) {
				HistoryDetails hd = new HistoryDetails();
				hd.setCbsRefNo(cursor.getString(0));
				hd.setTxnId(cursor.getString(1));
				hd.setCustomerId(cursor.getString(2));
				hd.setTxnAmt(cursor.getString(3));
				hd.setIsSynced(cursor.getString(4));
				hd.setTxnTime(cursor.getString(5));
				hd.setTxnType(cursor.getString(6));
				
				notes.add(hd);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_HISTORY +e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_HISTORY
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}  finally {
			db.close();
		}
		return notes;
		
	}
	@Override
	public List<HistoryDetails> readHistoryEnrollment() throws SQLiteException,
			DataAccessException {
		List<HistoryDetails> notes = new ArrayList<HistoryDetails>();
		SQLiteDatabase db = DbHandlerL.getInstance().getReadableDatabase();
		try {
			String query = ("SELECT "+DbHandlerL.COL_MBS_ENRL_ENROLMENTID
					+","+DbHandlerL.COL_MBS_ENRL_FIRSTNAME
					+","+DbHandlerL.COL_MBS_ENRL_CONTACTNO
					+","+DbHandlerL.COL_MBS_ENRL_DOB
					+","+DbHandlerL.COL_MBS_ENRL_GENDER
					+","+DbHandlerL.COL_MBS_ENRL_COUNTRY
					+","+DbHandlerL.COL_MBS_ENRL_ISSYNCED
					+","+DbHandlerL.COL_MBS_ENRL_TXNINITIME
					+" FROM "+DbHandlerL.TABLE_MBS_ENROLMENT+" WHERE "+
					DbHandlerL.COL_MBS_ENRL_ISSYNCED+" IN ('0','1','2')");
			Cursor cursor = db.rawQuery(query, null);
			while (cursor.move(1)) {
				HistoryDetails hd = new HistoryDetails();
				hd.setTxnId(cursor.getString(0));
				hd.setCustomerName(cursor.getString(1));
				hd.setContactNumber(cursor.getString(2));
				hd.setDob(cursor.getString(3));
				hd.setGender(cursor.getString(4));
				hd.setCountry(cursor.getString(5));
				hd.setIsSynced(cursor.getString(6));
				hd.setTxnTime(cursor.getString(7));
				notes.add(hd);
			}
			cursor.close();
		} catch (SQLiteException e) {
			LOG.error(Constants.EXCP_READ_HISTORY +e.getMessage());
			throw new SQLiteException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_READ_HISTORY
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}  finally {
			db.close();
		}
		return notes;
	}

}
