package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.mfi.rest.model.CashRecord;

public interface TransactionDao {
	
	/**
	 * Adds loan disbursement txn
	 * 
	 * @param loanDsbTxn
	 * @return rowid of the txn record added
	 * @throws DataAccessException
	 *             in case of any error while inserting txn record
	 */
	long insertTrasanction(TxnMaster txn)
			throws SQLiteException, DataAccessException;
	
	int updateTxnStatus(List<String> existingId)throws SQLiteException, DataAccessException;
	List<String> readUnSyncTxnStatus()throws SQLiteException, DataAccessException;
	public List<TxnMaster> getTxnAmt(String AccountNumber) throws SQLiteException, DataAccessException;
	long updateAgenda( TxnMaster VerifyData,long rowId) throws SQLiteException, DataAccessException;
	long insertCashRecord(CashRecord txn, TxnMaster loanTxn, long status) throws DataAccessException,
			SQLiteException;
	long updateCashRecord(String authStatus)throws DataAccessException;
	int updateTxnSmsText(String smsText,String txnId)throws DataAccessException, SQLiteException;
}
