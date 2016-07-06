package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.mfi.rest.model.CustomerBiometricDetails;
import com.bfsi.mfi.rest.model.CustomerEnrolmentDetails;

public interface EnrolementDao {
	/**
	 * Inserts enrolement content
	 * 
	 * @param enrolement
	 * @throws DataAccessException
	 */
	long insertEnrolement(CustomerEnrolmentDetails enrolement)
			throws SQLiteException, DataAccessException;

	List<Enrolement> readStatusList() throws SQLiteException,
			DataAccessException;

	// List<String> readUnSyncList() throws SQLiteException,
	// DataAccessException;
	// int updateEnrolStatus(List<String> enrolIdList)throws SQLiteException,
	// DataAccessException;

	long insertEnrolDocument(CustomerEnrolmentDetails enrolement, long txnStatus);
	
	long insertBiometricData(CustomerBiometricDetails biometric) throws SQLiteException, DataAccessException;
	int cntBiometric(String bioEnrollId) throws SQLiteException, DataAccessException;
	long updateBiometric(CustomerBiometricDetails biometric)throws DataAccessException;
}
