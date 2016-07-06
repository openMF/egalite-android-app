package com.bfsi.egalite.dao;

import java.util.List;

import android.database.sqlite.SQLiteException;

import com.bfsi.egalite.entity.Agent;
import com.bfsi.egalite.entity.Parameters;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.mfi.rest.model.AgentAgenda;
import com.bfsi.mfi.rest.model.AgentDetail;
import com.bfsi.mfi.rest.model.BranchDetail;
import com.bfsi.mfi.rest.model.CashRecord;
import com.bfsi.mfi.rest.model.CurrencyDetail;
import com.bfsi.mfi.rest.model.CustomerAccount;
import com.bfsi.mfi.rest.model.CustomerDetail;
import com.bfsi.mfi.rest.model.Deposit;
import com.bfsi.mfi.rest.model.DeviceDetail;
import com.bfsi.mfi.rest.model.Loan;
import com.bfsi.mfi.rest.model.Lov;
import com.bfsi.mfi.rest.model.Message;
import com.bfsi.mfi.rest.model.SmsTemplate;

/**
 * Data access for all loan Repayments
 * 
 */
/**
 * @author Vijay
 * 
 */
public interface PreDataDao {

	/**
	 * @param agenda
	 * @throws DataAccessException
	 */
	void insertAgentAgenda(List<AgentAgenda> agenda) throws DataAccessException;
	void insertLov(List<Lov> lovs) throws DataAccessException;

	/**
	 * @param depositDetails
	 * @throws SQLiteException
	 * @throws DataAccessException
	 */
	void insertAgnDepositDetails(List<Deposit> depositDetails)
			throws SQLiteException, DataAccessException;

	/**
	 * @param message
	 * @throws DataAccessException
	 */
	void insertMessageCodes(List<Message> message) throws DataAccessException;

	/**
	 * Reads the Agent Details
	 * 
	 * @param
	 * @return Agent
	 * @throws DataAccessException
	 *             in case of any error while retrieving the records
	 */
	Agent readAgentValues() throws DataAccessException;

	/**
	 * Reads the Device Details
	 * 
	 * @param
	 * @return Device
	 * @throws DataAccessException
	 *             in case of any error while retrieving the records
	 */
	DeviceDetail readDeviceValues() throws DataAccessException;

	/**
	 * Reads the Parameter Values w.r.t the param name
	 * 
	 * @param name
	 * @return Parameter
	 * @throws DataAccessException
	 *             in case of any error while retrieving the records
	 */
	Parameters readParameterValues(String name) throws DataAccessException;

	/**
	 * Reads all parameters available
	 * 
	 * @return
	 * @throws DataAccessException
	 */
	List<Parameters> readAllParameterValues() throws DataAccessException;

	/**
	 * Updates the Last Login Time w.r.t to the user
	 * 
	 * @param agt_useriddb
	 *            ,lastlogintime,invalid count
	 * @return void
	 * @throws DataAccessException
	 *             in case of any error while retrieving the records
	 */
	void updateUserLogin(String agt_useriddb, long lastlogintime,
			int invalidcount) throws DataAccessException;

	/**
	 * Reads the user Login Details
	 * 
	 * @param
	 * @return UserLogin
	 * @throws DataAccessException
	 *             in case of any error while retrieving the records
	 */
	Agent readUserLoginValues() throws DataAccessException;

	/**
	 * Inserts Customer details
	 * 
	 * @param customerDetails
	 * @throws DataAccessException
	 */
	void insertCustomersData(List<CustomerDetail> customerDetails)
			throws DataAccessException;
	void insertCustomersAcc(List<CustomerAccount> customerAcc)
			throws DataAccessException;

	/**
	 * Inserts Loans
	 * 
	 * @param loanDetail
	 * @throws DataAccessException
	 */
	void insertLoansData(List<Loan> loanDetail) throws DataAccessException;

	/**
	 * Inserts Running cash
	 * 
	 * @param cashDetail
	 * @throws DataAccessException
	 */
	void insertCashRecordDetails(List<CashRecord> cashDetail)
			throws DataAccessException;

	/**
	 * To update Agent information
	 * 
	 * @param agentDetails
	 * @throws DataAccessException
	 */
	void updateAgentsData(AgentDetail agentDetails) throws DataAccessException;

	/**
	 * To update agent password
	 * 
	 * @param password
	 * @param datakey
	 * @param changePasswordDate
	 * @throws DataAccessException
	 */
	void updateAgentsPassword(byte[] salt, String password, String datakey,
			String changePasswordDate) throws DataAccessException;

	/**
	 * To delete device details before updating
	 * 
	 * @throws DataAccessException
	 */
	void deleteDeviceData() throws DataAccessException;

	/**
	 * To delete parameters before updating while new sync
	 * 
	 * @throws DataAccessException
	 */
	void deleteParamsData() throws DataAccessException;

	/**
	 * @return
	 * @throws DataAccessException
	 */
	String getDeviceId() throws DataAccessException;

	/**
	 * @param currencyList
	 * @throws DataAccessException
	 */
	void insertCurrency(List<CurrencyDetail> currencyList)
			throws DataAccessException;

	/**
	 * @param agentid
	 * @param lastLogin
	 * @param invalidlogin
	 * @param synctime
	 * @throws DataAccessException
	 */
	void insertUserLoginData(String agentid, long lastLogin, int invalidlogin,
			String synctime) throws DataAccessException;

	/**
	 * @param agentId
	 * @param syncTime
	 * @throws DataAccessException
//	 */
	void updateLastSyncTime(String agentId, long syncTime)
			throws DataAccessException;

	/**
	 * @return
	 * @throws DataAccessException
//	 */
	String readLastSyncTime() throws DataAccessException;

	/**
	 * @param deviceId
	 * @param lastSyncTime
	 * @throws DataAccessException
	 */
	void updateDeviceLastSynctime(String deviceId, long lastSyncTime)
			throws DataAccessException;

	/**
	 * @param branchDetails
	 * @throws DataAccessException
	 */
	void insertBranchData(List<BranchDetail> branchDetails)
			throws DataAccessException;

	/**
	 * @param agentData
	 * @throws DataAccessException
	 */
	void insertAgentsData(Agent agentData) throws DataAccessException;

	int agendaCount()throws SQLiteException,DataAccessException;
	void insertTemplates(List<SmsTemplate> listTemplt) throws DataAccessException;
	SmsTemplate readSmsTemplate(String txnCode)throws SQLiteException ,DataAccessException ;
	

}
