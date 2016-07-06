package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CcyCodes;
import com.bfsi.egalite.entity.TxnCount;
import com.bfsi.egalite.exceptions.DataAccessException;

/**
 * DAO for retieving and maintaining cash positions
 * @author Swetha
 */
public interface CashPositionDao {

	List<CcyCodes> readCurrencyList() throws SQLiteException,
			DataAccessException;

	List<TxnCount> getAgnData() throws SQLiteException,
			DataAccessException;
	List<TxnCount> getTxnData() throws DataAccessException;

	CashPosition readCashPosition(String agentId, String ccyCode)
			throws DataAccessException;
	List<TxnCount> getEnrlData() throws DataAccessException;

}
