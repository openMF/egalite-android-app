package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.AgnLoanDetail;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.GroupLoans;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.mfi.rest.model.LoanPaidSch;

/**
 * Data access for all loan disbursement
 * 
 * @author swetha
 * 
 */
public interface LoansDao {
	

	List<AgendaMaster> repayAgenda() throws SQLiteException, DataAccessException;

	List<String> readAccountNumbers() throws SQLiteException, DataAccessException;

	List<String> readCustomerNames() throws SQLiteException, DataAccessException;

	AgendaMaster readAccountDetails(String accountnumber)
			throws SQLiteException, DataAccessException;
	List<AgendaMaster> readDsbAgenda() throws SQLiteException, DataAccessException;

	List<CustomerDetail> readCustomerDetails() throws SQLiteException, DataAccessException;

	Requests acNoCheckAgenda(String cbsAcRefNo) throws SQLiteException, DataAccessException;

	List<AgendaMaster> CustAccountDetails(String customerId)
			throws SQLiteException, DataAccessException;

	List<AgendaMaster> readPrepayAgenda() throws SQLiteException, DataAccessException;
	List<String> agentControlAccess() throws SQLiteException, DataAccessException;
	List<LoanPaidSch> getLoanPaidSchedule(String loanNumber)throws SQLiteException,DataAccessException;
	AgnLoanDetail getLoanDetails(String loanNumber)throws SQLiteException,DataAccessException;
	List<GroupLoans> getGroupLoans(String loanNumber,String customerid)throws SQLiteException,DataAccessException;
	List<AgendaMaster> prepayAgenda() throws SQLiteException,DataAccessException;
	int  cntCustomerDetails() throws SQLiteException,DataAccessException;
	int repaymentCheck(String cbsRefNo)throws SQLiteException, DataAccessException ;

}
