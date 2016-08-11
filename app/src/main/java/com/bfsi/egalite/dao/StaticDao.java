package com.bfsi.egalite.dao;

import com.bfsi.egalite.exceptions.DataAccessException;

public interface StaticDao {
	
	public void insertAgentsData();
	public void insertDeviceData(String id);
	public void insertCustomersData() ;
	public void insertLoansData();
	public void insertDisbursmentScedule();
	public void insertAgendaDisbursment()throws DataAccessException;
	public void insertUserLogin();
	public void insertTransactions(String transationid, String customername);
	public void insertRunningCash();
	public void insertrepaymentschedules();
	public void insertAgendaRepayments();
	public void insertLiquidation() throws DataAccessException ;
	public void insertAgtbDeposits() throws DataAccessException;
	public void insertDepositCollection() throws DataAccessException;
	public void insertDepositMaturityPayment() throws DataAccessException;
	public void insertDepositRequests() throws DataAccessException;
	
}
