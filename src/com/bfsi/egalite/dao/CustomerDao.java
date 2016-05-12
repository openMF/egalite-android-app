package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.exceptions.DataAccessException;

public interface CustomerDao {
	List<CustomerDetail> readCustomerDetails() throws SQLiteException, DataAccessException;
	List<CustomerDetail>  readCashDetails(String custId) throws SQLiteException,DataAccessException ;

	List<AgendaMaster> customerAgenda() throws SQLiteException, DataAccessException;

	List<CustomerDetail> readCustInfo(String customerId)throws SQLiteException,DataAccessException ;
	String smsRequired(String custId)throws DataAccessException;

}
