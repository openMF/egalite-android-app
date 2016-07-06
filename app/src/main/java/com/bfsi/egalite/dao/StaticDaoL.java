package com.bfsi.egalite.dao;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.exceptions.DataAccessException;

public interface StaticDaoL {
	void insertDsbData() throws SQLiteException, DataAccessException;

	void insertloanPaidSchData() throws SQLiteException, DataAccessException;

	void insertCustDetail() throws SQLiteException, DataAccessException;

	void insertAgnLonDet() throws SQLiteException, DataAccessException;

	void insertDeptDet() throws SQLiteException, DataAccessException;

	void insertAppParm(String name, String value) throws SQLiteException,
			DataAccessException;


	// void insertAgtLogin() throws SQLiteException, DataAccessException;

	void insertLOV() throws SQLiteException, DataAccessException;

	void insertMsgCode() throws SQLiteException, DataAccessException;

	void insertBrchDts() throws SQLiteException, DataAccessException;

	void insertAgtCashRec() throws SQLiteException, DataAccessException;

	void insertDevDetl() throws SQLiteException, DataAccessException;

	void insertAgents() throws SQLiteException, DataAccessException;

	void insertCcyCodes() throws SQLiteException, DataAccessException;

	void insertFixRates() throws SQLiteException, DataAccessException;

	void insertRepayData() throws SQLiteException, DataAccessException;
	

	void insertCollectionData() throws SQLiteException, DataAccessException;

	void insertPaymentsData() throws SQLiteException, DataAccessException;

	void insertErrorCodes() throws SQLiteException;

	void moduleCodes() throws SQLiteException, DataAccessException;

	void insertCashData() throws SQLiteException, DataAccessException;

	void insertAgendaCash() throws SQLiteException, DataAccessException;
	
	void moduleCodesToUpdate() throws SQLiteException, DataAccessException ;
}
