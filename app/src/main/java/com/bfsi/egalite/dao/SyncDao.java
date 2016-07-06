package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.mfi.rest.model.CashTransactionResponse;
import com.bfsi.mfi.rest.model.TransactionRequest;

/**
 * DAO for retrieving and maintaining cash positions
 * 
 * @author Vijay
 */
public interface SyncDao {
	
	public TxnMaster readUnSyncTxns(String id)throws DataAccessException;
	/**
	 * Reads the customer Numbers
	 * 
	 * @param 
	 * @return ArrayList
	 * @throws DataAccessException
	 *             in case of any error while retrieving  the records
	 */
	public List<String> readCustomerIds() throws DataAccessException;
	/**
	 * Reads the Transaction Ids
	 * 
	 * @param 
	 * @return ArrayList
	 * @throws DataAccessException
	 *             in case of any error while retrieving  the records
	 */
	
	
	public List<String> readUnSyncTxnId() throws DataAccessException;
	/**
	 *updates the sync status
	 * 
	 * @param 
	 * @return 0 if not successful, 1 if successful
	 * @throws DataAccessException
	 *             in case of any error while retrieving  the records
	 */
	
	public List<String> readLoanAcNos() throws DataAccessException;
	/**
	 * @param txnIdlist
	 * @return
	 * @throws DataAccessException
	 */
	public int  updateSyncStatus(List<TransactionRequest> txnIdlist) throws DataAccessException;
	
	/**
	 * To read data from File
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	
//	public TxnMaster readDataFromFile(String id)throws DataAccessException;
	
	/**
	 * @throws SQLiteException
	 */
	void deleteMaintainceData()throws SQLiteException;
	/**
	 * @throws SQLiteException
	 */
	
	 List<TxnMaster> readUnSyncTxns()throws SQLiteException;
	 
	 int updateCashSyncStatus(CashTransactionResponse serverResponse);
	 int updateCashTxnUnSyncStatus(CashTransactionResponse serverResponse);
	 List<String> readUnSyncBios() throws DataAccessException;
	 int updateGroupLoans()throws DataAccessException;
	 void deleteDDMaintainceData()throws SQLiteException;

}
