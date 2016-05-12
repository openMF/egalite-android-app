package com.bfsi.egalite.dao.sqlite;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.entity.Agent;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
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
import com.bfsi.mfi.rest.model.LovResponse;
import com.bfsi.mfi.rest.model.Message;
import com.bfsi.mfi.rest.model.MessageDetailsResponse;
import com.bfsi.mfi.rest.model.ParametersResponse;
import com.bfsi.mfi.rest.model.SmsTemplate;

public class DataPorting {

	static SimpleDateFormat formatter = CommonContexts.dateFormat;
	
	public static void setAgentData(AgentDetail agentDetail, String password,String dataKey,byte[] salt)
    {
		try{
            Agent agent = new Agent();
            agent.setPasswordSalt(salt);
            agent.setDataKey(dataKey);
            agent.setPassword(password);
            agent.setAgencyCode(agentDetail.getAgencyCode());
            agent.setLang(agentDetail.getAgentLang());
            agent.setAgentRoles(agentDetail.getAgentRoles());
            agent.setAgentType(agentDetail.getAgentType());
            agent.setAuthStatus(agentDetail.getAuthStatus());
            agent.setCashLimit(agentDetail.getCashLimit());
            agent.setCity(agentDetail.getCity());
            agent.setAddress(agentDetail.getCommAddr1());
            agent.setContactNumber(agentDetail.getContactNumber());
            agent.setCountry(agentDetail.getCountry());
            agent.setCreditOfficer(agentDetail.getCreditOfficer());
            agent.setDataValue(agentDetail.getDataValue());
            agent.setDeviceId(agentDetail.getDeviceId());
            agent.setDob(agentDetail.getDob());
            agent.setEmailAddress(agentDetail.getEmailAddress());
            agent.setEndDate(agentDetail.getEndDate());
            agent.setfName(agentDetail.getFname());
            agent.setGender(agentDetail.getGender());
            agent.setAgentId(agentDetail.getId());
            agent.setIsActive(agentDetail.getIsActive());
            agent.setlName(agentDetail.getLname());
            agent.setLocationCode(agentDetail.getLocationCode());
            agent.setState(agentDetail.getState());
            agent.setUserName(agentDetail.getUserName());
            agent.setZipCode(agentDetail.getZipCode());
            agent.setStartDate(agentDetail.getStartDate());
            agent.setMacId(agentDetail.getMacId());
            PreDataAccess pd = new PreDataAccess();
            pd.insertAgentsData(agent);
		}catch(DataAccessException e)
		{
			throw new DataAccessException(e.getMessage(),e);
		}catch(Exception e)
		{
			throw new DataAccessException(e.getMessage(),e);
		}
    }
	 public static void setDeviceData(DeviceDetail deviceDetail)
     {
           try{
	             PreDataAccess pd = new PreDataAccess();
	             pd.insertDeviceData(deviceDetail);
           }catch(DataAccessException e)
   			{
   				throw new DataAccessException(e.getMessage(),e);
   			}catch(Exception e)
   			{
   				throw new DataAccessException(e.getMessage(),e);
   			}
     }
	 public static void setMessageCodes(MessageDetailsResponse messagecodes)
     {
		try {
			List<Message> listMessage = messagecodes.getMessages();
			if (listMessage != null && listMessage.size() > 0) {

				PreDataAccess pd = new PreDataAccess();
				pd.insertMessageCodes(listMessage);

			}
		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
     }
	 
	 public static void setAgentData(AgentDetail agentDetail) {
		 try{
				Agent agent = new Agent();
				agent.setAgencyCode(agentDetail.getAgencyCode());
	            agent.setLang(agentDetail.getAgentLang());
	            agent.setAgentRoles(agentDetail.getAgentRoles());
	            agent.setAgentType(agentDetail.getAgentType());
	            agent.setAuthStatus(agentDetail.getAuthStatus());
	            agent.setCashLimit(agentDetail.getCashLimit());
	            agent.setCity(agentDetail.getCity());
	            agent.setAddress(agentDetail.getCommAddr1());
	            agent.setContactNumber(agentDetail.getContactNumber());
	            agent.setCountry(agentDetail.getCountry());
	            agent.setCreditOfficer(agentDetail.getCreditOfficer());
	            agent.setDataValue(agentDetail.getDataValue());
	            agent.setDeviceId(agentDetail.getDeviceId());
	            agent.setDob(agentDetail.getDob());
	            agent.setEmailAddress(agentDetail.getEmailAddress());
	            agent.setEndDate(agentDetail.getEndDate());
	            agent.setfName(agentDetail.getFname());
	            agent.setGender(agentDetail.getGender());
	            agent.setAgentId(agentDetail.getId());
	            agent.setIsActive(agentDetail.getIsActive());
	            agent.setlName(agentDetail.getLname());
	            agent.setLocationCode(agentDetail.getLocationCode());
	            agent.setState(agentDetail.getState());
	            agent.setUserName(agentDetail.getUserName());
	            agent.setZipCode(agentDetail.getZipCode());
	            agent.setStartDate(agentDetail.getStartDate());
	            
	            PreDataAccess pd = new PreDataAccess();
	            pd.insertAgentsData(agent);
		 	}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}

		public static void setCustomerData(List<CustomerDetail> customerDetail) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertCustomersData(customerDetail);
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		public static void setCustomerAcc(List<CustomerAccount> customerAcc) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertCustomersAcc(customerAcc);
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		public static void setCashRecord(List<CashRecord> cashData) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertCashRecordDetails(cashData);
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		public static void setDepositDetailsData(List<Deposit> depositDetail) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertAgnDepositDetails(depositDetail);
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}

		public static void setLoanData(List<Loan> loanDetail) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertLoansData(loanDetail);
				
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}

		public static void setCashData(List<CashRecord> cashData) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertCashRecordDetails(cashData);
			}
			catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		
		public static void setCurrency(List<CurrencyDetail> currerncList)
		{
			try
			{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertCurrency(currerncList);
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}

		@SuppressWarnings("rawtypes")
		public static void setParamData(ParametersResponse map) {

			try{
				if(map != null)
				{
					Map<String, String> parameterMap = map.getParamsValue();
					Iterator iterator = parameterMap.keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						String value = (String) parameterMap.get(key);
						PreDataAccess pd = new PreDataAccess();
						pd.insertParameterData(key.trim(), value.trim());
					}
				}
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		@SuppressWarnings("rawtypes")
		public static void setParamData(Map<String, String> parameterMap) {

			try{
				Iterator iterator = parameterMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = (String) parameterMap.get(key);
					PreDataAccess pd = new PreDataAccess();
					pd.insertParameterData(key.trim(), value.trim());
				}
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		public static void setBranchCodes(List<BranchDetail> branchDetails) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertBranchData(branchDetails);
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		public static void setSmsTemplates(List<SmsTemplate> smsTemplates) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertTemplates(smsTemplates);
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		
		public static void setAgendaData(List<AgentAgenda> agendaDetail) {
			try{
				PreDataDao predao = DaoFactory.getPreDataDao();
				predao.insertAgentAgenda(agendaDetail);
				
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
		public static void setLov(LovResponse lovResponse) {
			try{
				if(lovResponse != null)
				{
					List<Lov> listLov = lovResponse.getLovList();
					PreDataDao predao = DaoFactory.getPreDataDao();
					predao.insertLov(listLov);
				}
				
			}catch(DataAccessException e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}catch(Exception e)
			{
				throw new DataAccessException(e.getMessage(),e);
			}
		}
 }
