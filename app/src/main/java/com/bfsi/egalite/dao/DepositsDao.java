package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.AgnDepositDetail;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;

public interface DepositsDao {
	List<AgendaMaster> readAgendaPayments() throws SQLiteException,
			DataAccessException;

	List<AgendaMaster> readAgendaCollections() throws SQLiteException,
			DataAccessException;

	List<TxnMaster> getTxnAmt(String AccountNumber) throws SQLiteException,
			DataAccessException;

	List<AgendaMaster> CustAccountDetails(String customerId)
			throws SQLiteException, DataAccessException;

	Requests readAccountDetails(String cbsAcRefNo) throws SQLiteException,
			DataAccessException;

	List<AgendaMaster> CustDepositDetails(String customerId)
			throws SQLiteException, DataAccessException;

	AgnDepositDetail getDepositDetails(String loanNumber) throws SQLiteException,
			DataAccessException;
}
