package com.bfsi.egalite.dao;

import com.bfsi.egalite.dao.sqlite.CashPositionDataAccess;
import com.bfsi.egalite.dao.sqlite.CustomersDataAccess;
import com.bfsi.egalite.dao.sqlite.DepositsDataAccess;
import com.bfsi.egalite.dao.sqlite.EnrolmentDataAccess;
import com.bfsi.egalite.dao.sqlite.HistoryDataAccess;
import com.bfsi.egalite.dao.sqlite.LoansDataAccess;
import com.bfsi.egalite.dao.sqlite.PreDataAccess;
import com.bfsi.egalite.dao.sqlite.RequestsDataAccess;
import com.bfsi.egalite.dao.sqlite.StaticDataAccessL;
import com.bfsi.egalite.dao.sqlite.SyncDataAccess;
import com.bfsi.egalite.dao.sqlite.TxnDataAccess;

/**
 * Basic Data access factory
 * 
 * @author Vijay
 * 
 */
public class DaoFactory {
	public static CashPositionDao getCashDao() {
		return cashDao;
	}

	private static LoansDao loanDao = new LoansDataAccess();
	private static DepositsDao depositDao = new DepositsDataAccess();
	private static PreDataDao preDataDao = new PreDataAccess();
	private static SyncDao syncDao = new SyncDataAccess();
	private static EnrolementDao enrolementDao = new EnrolmentDataAccess();
	private static RequestsDao requestsDao = new RequestsDataAccess();
	private static StaticDaoL staticDao = new StaticDataAccessL();
	private static TransactionDao txnDao = new TxnDataAccess();
	private static CashPositionDao cashDao = new CashPositionDataAccess();
	private static HistoryDao historyDao = new HistoryDataAccess();
	private static CustomerDao customerDao = new CustomersDataAccess();

	public static HistoryDao getHistoryDao() {
		return historyDao;
	}

	public static LoansDao getLoanDao() {
		return loanDao;
	}

	public static DepositsDao getDepositDao() {
		return depositDao;
	}

	public static PreDataDao getPreDataDao() {
		return preDataDao;
	}

	public static SyncDao getSyncDao() {
		return syncDao;
	}

	public static EnrolementDao getEnrolementDao() {
		return enrolementDao;
	}

	public static RequestsDao getRequestsDao() {
		return requestsDao;
	}

	public static StaticDaoL getStaticDao() {
		return staticDao;
	}

	public static TransactionDao getTxnDao() {
		return txnDao;
	}

	public static CustomerDao getCustomerDao() {
		return customerDao;
	}

}
