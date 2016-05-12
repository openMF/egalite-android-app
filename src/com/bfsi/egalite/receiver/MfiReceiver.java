package com.bfsi.egalite.receiver;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.dao.SyncDao;
import com.bfsi.egalite.dao.sqlite.DataPorting;
import com.bfsi.egalite.dao.sqlite.EnrolmentDataAccess;
import com.bfsi.egalite.dao.sqlite.SyncDataAccess;
import com.bfsi.egalite.encryption.DESedeEncryDecryption;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.CustomerDocuments;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.entity.Parameters;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.servicerequest.RequestManager;
import com.bfsi.egalite.sync.GsonAdapter;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.SystemUtil;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.AgentAgenda;
import com.bfsi.mfi.rest.model.AgentAgendaRequest;
import com.bfsi.mfi.rest.model.AgentAgendaResponse;
import com.bfsi.mfi.rest.model.AgentAgendaUpdate;
import com.bfsi.mfi.rest.model.CashPositionRequest;
import com.bfsi.mfi.rest.model.CashPositionResponse;
import com.bfsi.mfi.rest.model.CashRecord;
import com.bfsi.mfi.rest.model.CustomerBiometricDetails;
import com.bfsi.mfi.rest.model.CustomerBiometricRequest;
import com.bfsi.mfi.rest.model.CustomerBiometricResponse;
import com.bfsi.mfi.rest.model.CustomerDocument;
import com.bfsi.mfi.rest.model.CustomerEnrolmentDetails;
import com.bfsi.mfi.rest.model.CustomerEnrolmentRequest;
import com.bfsi.mfi.rest.model.CustomerInfoResponse;
import com.bfsi.mfi.rest.model.JsonRequestWrapper;
import com.bfsi.mfi.rest.model.JsonResponseWrapper;
import com.bfsi.mfi.rest.model.PostTransactionRequest;
import com.bfsi.mfi.rest.model.PostTransactionResponse;
import com.bfsi.mfi.rest.model.TransactionRequest;
import com.google.gson.Gson;

public class MfiReceiver extends BroadcastReceiver {

	final public static String ONE_TIME = "onetime";
	private static final String CONNECTION_POST = "POST";
	private static final String CONNECTION_GET = "GET";
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {

		// You can do the processing here update the widget/remote views.
		this.mContext = context;
		if (SystemUtil.haveNetworkConnection(context)) {
			new AsyncOperations().execute("");
		}

	}
	
	private class AsyncOperations extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {

			try {
				if(SystemUtil.haveNetworkConnection(mContext))
				{
					// Pick and send unsynced txns & // update agenda status and transaction status
					sendTransactions(mContext);
					
					//syncing the unsynced +enrollments and biometric
	//				handlePostEnrolmentRequest(mContext);
					//handlePostBiometricRequest(mContext);
					
					//updating the grouploans
					SyncDao dao = DaoFactory.getSyncDao();
					dao.updateGroupLoans();
					
					// pull agenda details
					getAgentAgenda();
	
					// cash top up balance of an agent
					getCashRecordDetails(mContext);
					
					return "success";
				}else 
					return "Failed";
			}  catch (DataAccessException e) {
				return null;
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected void onPostExecute(String result) {
			// dismissProgressDialog();
			
		}
	}
	private void getCashRecordDetails(Context context) {

		List<CashRecord> cashData = null;
		List<String> custmerid = new ArrayList<String>();
		Gson gson = new Gson();
		long synctime  =DateUtil.getCurrentDataTime();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ context.getString(R.string.service_cashRecords);
			CashPositionRequest ldr = new CashPositionRequest();
			ldr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			ldr.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
			ldr.setBatchSize(CommonContexts.BATCHSIZE);
			ldr.setLocCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
			ldr.setRecvdIds(custmerid);
			ldr.setSyncSessionId(String.valueOf(synctime));

			try {
				JsonRequestWrapper jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(
						CommonContexts.CIPHER_DATAKEY, gson.toJson(ldr)));

				JsonResponseWrapper jsoncustResponse = getResponse(url,
						CONNECTION_GET, JsonResponseWrapper.class,
						gson.toJson(jrw));
				if (jsoncustResponse != null
						&& jsoncustResponse.getJsonString() != null) {
					CashPositionResponse custResponse = gson.fromJson(
							DESedeEncryDecryption.decrypt(
									CommonContexts.CIPHER_DATAKEY,
									jsoncustResponse.getJsonString()),
							CashPositionResponse.class);
				if (custResponse != null) {
					cashData = custResponse.getCashRecords();
				}
				if (cashData != null && cashData.size() > 0) {
					DataPorting.setCashRecord(cashData);
					if (custmerid != null) {
						custmerid = null;
						custmerid = new ArrayList<String>();
					}
					for (CashRecord ld : cashData) {
						custmerid.add(ld.getEntrySeqNo());
					}
				}
				}
			} catch (DataAccessException e) {
				throw new DataAccessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage(), e);
			}
		} while (cashData != null && cashData.size() > 0);
	}
	private void sendTransactions(Context context) {
		SyncDao dao = DaoFactory.getSyncDao();
		JsonRequestWrapper jrw = null;
		try {
			List<TxnMaster> txnMasterList = dao.readUnSyncTxns();
			if(txnMasterList != null && txnMasterList.size() >0)
			{
				List<TransactionRequest> transationrequest = new ArrayList<TransactionRequest>();
				for (TxnMaster txnMaster : txnMasterList) {
					transationrequest.add(generateRequestObject(txnMaster));
				}
				String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
						+ context.getString(R.string.service_postsync);
				PostTransactionRequest ptrequest = new PostTransactionRequest();
				ptrequest.setTxnrequest(transationrequest);
				jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, new Gson().toJson(ptrequest)));
				
				String response = RequestManager.transactionRequest(url,
						CONNECTION_POST, new Gson().toJson(jrw));
				Gson gsonBuilder = GsonAdapter.getGson();
				JsonResponseWrapper jresw = gsonBuilder.fromJson(response,JsonResponseWrapper.class);
				if(jresw != null && jresw.getJsonString() != null)
				{
					PostTransactionResponse postTxnResponse = gsonBuilder.fromJson(
							DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jresw.getJsonString()), PostTransactionResponse.class);
					if (postTxnResponse != null) {
						try {
							boolean status = postTxnResponse.getStatus();
							if (status) {
								//update transaction sync status and update agenda status
								dao.updateSyncStatus(transationrequest);
								transationrequest.clear();
							}
						} catch (DataAccessException e) {
							System.out.println(e.getMessage());
						} 
					}
				}
			}

		} catch (Exception e) {
			if (e.getMessage().contains(Constants.HOSTNAME)) {
				sendTransactions(mContext);
			}
			else
			{
				//write throws command.
			}
		}

	}

	private TransactionRequest generateRequestObject(TxnMaster loantxn) {
		TransactionRequest transactionRequest = new TransactionRequest();
		try {
			transactionRequest.setDepNoOfInstalement(loantxn.getReqDepNoInst()!= null ? Integer.parseInt(loantxn.getReqDepNoInst()):0);
			transactionRequest.setDepoRedeemReqDate(loantxn.getReqRedReqDt() != null ? Long.parseLong(loantxn.getReqRedReqDt()):0);
			transactionRequest.setIsFullRepoRedeem(loantxn.getReqRedReqFullPartInd());
			transactionRequest.setMaturityDate(loantxn.getReqMaturityDate());
			transactionRequest.setIntrRate(loantxn.getReqIntRate()!= null?Double.parseDouble(loantxn.getReqIntRate()):0);
			transactionRequest.setDepoTenureType(loantxn.getReqDpTenureType());
			transactionRequest.setDepoFreqType(loantxn.getReqDpFrequencyType());
			transactionRequest.setDepoFreq(loantxn.getReqDpFrequency()!= null?Integer.parseInt(loantxn.getReqDpFrequency()):0);
			transactionRequest.setDepoTenure(loantxn.getReqDpTenure()!= null ? Integer.parseInt(loantxn.getReqDpTenure()):0);
			transactionRequest.setTxnErrCode(loantxn.getTxnErrorCode());
			transactionRequest.setTxnErrMsg(loantxn.getTxnErrorMsg());
			transactionRequest.setGeneratedSms(loantxn.getGeneratedSms());
			transactionRequest.setSmsMobNo(loantxn.getSmsMobileNo());
			transactionRequest.setSmsContent(loantxn.getSmsContent());
			transactionRequest.setGenerateReversal(loantxn.getGenerateRevr());
			transactionRequest.setMbsSeqNo(loantxn.getMbsSeqNo()!= null?Integer.parseInt(loantxn.getMbsSeqNo()):0);
			
			transactionRequest.setTxnId(loantxn.getTxnId());
			transactionRequest.setModuleCode(loantxn.getModuleCode());
			transactionRequest.setTxnCode(loantxn.getTxnCode());
			transactionRequest.setAgendaId(loantxn.getAgendaId());
			transactionRequest.setSeqNo(loantxn.getSeqNo()!= null?Integer.parseInt(loantxn.getSeqNo()):0);
			transactionRequest.setCbsAccRefNo(loantxn.getCbsAcRefNo());
			transactionRequest.setBranchCode(loantxn.getBrnCode());
			transactionRequest.setCustomerId(loantxn.getCustomerId());
			transactionRequest.setTxnInitTime(loantxn.getInitTime());
			transactionRequest.setAgentId(loantxn.getAgentId());
			transactionRequest.setDeviceId(loantxn.getDeviceId());
			transactionRequest.setLocCode(loantxn.getLocationCode());
			transactionRequest.setIsLoanFutureSch(loantxn.getLnIsFutureSch());
			transactionRequest
					.setTxnAmount(Double.valueOf(loantxn.getTxnAmt()));
			transactionRequest.setTxnSettlementAmount(loantxn
					.getSettledAmt() != null ?Double.valueOf(loantxn
					.getSettledAmt()):0);
			transactionRequest.setFullPartIndicator(loantxn
					.getFullPartInd());
			transactionRequest.setTxnCcy(loantxn.getCcyCode());
			transactionRequest.setSyncType("N");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return transactionRequest;
	}

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, MfiReceiver.class);
		intent.putExtra(ONE_TIME, Boolean.FALSE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000*30, pi);
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, MfiReceiver.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	private void getAgentAgenda() {
		List<AgentAgenda> agendaDetails = null;
		JsonRequestWrapper jrw = null;
		List<AgentAgendaUpdate> agendaUpdate = new ArrayList<AgentAgendaUpdate>();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ mContext.getString(R.string.service_agenda_list);
			AgentAgendaRequest aar = new AgentAgendaRequest();
			aar.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			aar.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
			aar.setLocationCode(CommonContexts.mLOGINVALIDATION
					.getLocationCode());
			aar.setBatchSize(CommonContexts.BATCHSIZE);
			aar.setAgentAgendaUpdate(agendaUpdate);
			
			jrw = new JsonRequestWrapper();
			try {
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, new Gson().toJson(aar)));
				
				JsonResponseWrapper jsonagendaResponse = getResponse(url,
						CONNECTION_POST, JsonResponseWrapper.class,
						getAgendaString(jrw));
				if(jsonagendaResponse != null && jsonagendaResponse.getJsonString() != null)
				{
					AgentAgendaResponse agendaResponse = new Gson().fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jsonagendaResponse.getJsonString()), AgentAgendaResponse.class);
					if (agendaResponse != null) {
						agendaDetails = agendaResponse.getAgendaList();
					}
					if (agendaDetails != null && agendaDetails.size() > 0) {
						DataPorting.setAgendaData(agendaDetails);
						if (agendaUpdate != null) {
							agendaUpdate = null;
							agendaUpdate = new ArrayList<AgentAgendaUpdate>();
						}
						for (AgentAgenda ld : agendaDetails) {
							AgentAgendaUpdate aau = new AgentAgendaUpdate();
							aau.setAgendaId(ld.getAgendaId());
							aau.setSeqNo(String.valueOf(ld.getSeqNo()));
							agendaUpdate.add(aau);
						}
					}
				}
			} catch (DataAccessException e) {
				throw new DataAccessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage(), e);
			}
		} while (agendaDetails != null && agendaDetails.size() > 0);
	}

	private <T> T getResponse(String url, String method, Class<T> clazz,
			String payload) {
		T responseObject = null;
		try {
			if(SystemUtil.haveNetworkConnection(mContext))
			{
				String response = RequestManager.transactionRequest(url, method,
						payload);
				Gson gson = GsonAdapter.getGson();
				responseObject = gson.fromJson(response, clazz);
				return responseObject;
			}
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return responseObject;

	}

	private String getAgendaString(JsonRequestWrapper arr) {
		Gson gson = new Gson();
		return gson.toJson(arr);
	}
	
	private String handlePostEnrolmentRequest(Context context)
			throws ServiceException {
		int initialbatchSize = 0;
		PreDataDao daos = DaoFactory.getPreDataDao();
		Parameters params = daos.readParameterValues(AppParams.DEFAULT_DEVICE_SYNCH_BATCH_SIZE);
		if(params != null)
			initialbatchSize = Integer.parseInt(params.getValue());
		
		SyncDataAccess sda = new SyncDataAccess();
		List<String> enrIds = sda.readUnSyncEndId();
		
		List<String> sublist = new ArrayList<String>();
		String statustext = null;
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ context.getString(R.string.service_customerinfo);
		List<CustomerEnrolmentDetails> customerDetailsList = new ArrayList<CustomerEnrolmentDetails>();
		int numTxns = enrIds.size();
		
		for (int k = 0; k < numTxns;) {
			int batchSize = 0;
			if (k == 0) {
				batchSize = (initialbatchSize > numTxns) ? numTxns
						: initialbatchSize;
			} else {
				int remainingTxn = numTxns - k;
				batchSize = (initialbatchSize > remainingTxn) ? remainingTxn
						: initialbatchSize;
			}
			if (batchSize >= 1)
				sublist = enrIds.subList(k, (k + batchSize));
			else
				sublist = enrIds;
			k += batchSize;

			for (String id : sublist) {
				CustomerEnrolmentDetails customerenrolRequest = generateEnrolRequestObject(id);
				customerDetailsList.add(customerenrolRequest);
			}
			CustomerEnrolmentRequest ptrequest = new CustomerEnrolmentRequest();
			ptrequest.setEnrolList(customerDetailsList);

			String response = RequestManager.transactionRequest(url,
					CONNECTION_POST, new Gson().toJson(ptrequest));
			Gson gson = GsonAdapter.getGson();
			CustomerInfoResponse postTxnResponse = gson.fromJson(response,
					CustomerInfoResponse.class);
			if (postTxnResponse != null) {
				try {
					boolean status = postTxnResponse.getStatus();
					if (status) {
						statustext = postTxnResponse.getStatusText();
						EnrolmentDataAccess.updateEnrolStatus(enrIds);
						customerDetailsList.clear();
					}
				}  catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return statustext;
	}
	private CustomerEnrolmentDetails generateEnrolRequestObject(String id)
			throws DataAccessException {

		CustomerEnrolmentDetails cDetialsInfo = new CustomerEnrolmentDetails();
		try {
			Enrolement enrolment = EnrolmentDataAccess
					.readCustomerEnrolmentData(id);

			cDetialsInfo.setEnrolmentId(enrolment.getEnrollmentId());
			cDetialsInfo.setFirstName(enrolment.getFirstName());
			cDetialsInfo.setLastName(enrolment.getLastName());
			cDetialsInfo.setMiddleName(enrolment.getMiddleName());
			cDetialsInfo.setGender(enrolment.getGender());
			cDetialsInfo.setCustomerId(enrolment.getCustomerId());
			cDetialsInfo.setModuleCode(enrolment.getModuleCode());
			cDetialsInfo.setTxnCode(enrolment.getTxnCode());
			cDetialsInfo.setIsKycOnly(enrolment.getIsKycOnly());
			cDetialsInfo.setDeviceId(enrolment.getDeviceId());
			cDetialsInfo.setAgentId(enrolment.getAgentId());
			cDetialsInfo.setLocCode(enrolment.getLocationCode());
			if (enrolment.getDob() != null) {
				cDetialsInfo.setDob(enrolment.getDob());
			} else {

				cDetialsInfo.setDob(null);
			}
			cDetialsInfo.setAddress1(enrolment.getAddress1());
			cDetialsInfo.setAddress2(enrolment.getAddress2());
			cDetialsInfo.setAddress3(enrolment.getAddress3());
			cDetialsInfo.setAddress4(enrolment.getAddress4());
			cDetialsInfo.setEmail(enrolment.getEmailId());
			cDetialsInfo.setContactNo(enrolment.getContactNo());
			cDetialsInfo.setCountry(enrolment.getCountry());
			cDetialsInfo.setState(enrolment.getState());
			cDetialsInfo.setZipCode(enrolment.getZipCode());
			cDetialsInfo.setAccCurrency(enrolment.getCurrency());
			cDetialsInfo.setResidenceType(enrolment.getResidenceType());
			cDetialsInfo.setNationality(enrolment.getNationality());
			cDetialsInfo.setMaritalStatus(enrolment.getMartialStatus());
			cDetialsInfo.setProfession(enrolment.getProfession());
			cDetialsInfo.setProfessionRemark(enrolment.getProfessionRemark());
			cDetialsInfo
					.setTxnInitTime(enrolment.getTxnInitTime()!= 0 ?Long.valueOf(enrolment.getTxnInitTime()):enrolment.getTxnInitTime());
			cDetialsInfo.setAccType(enrolment.getAccountType());
			cDetialsInfo.setGroupIndlType(enrolment.getGroupIndiviType());
			cDetialsInfo.setPoc(enrolment.getPoc());
			cDetialsInfo.setTmpGrpId(enrolment.getTempGroupId());

			CustomerDocuments docs = enrolment.getCustomerDocs();
			CustomerDocument cd = new CustomerDocument();
			cd.setEnrolmentId(docs.getEnrolmentId());
			cd.setKycCustImage(new String(docs.getKycImgCustomer()));
			cd.setKycIdImage1(new String(docs.getKycId1Img()));
			cd.setKycIdNo1(docs.getKycId1Number());
			cd.setKycIdType1(docs.getKycId1Type());
			cd.setKycIdProofType1(docs.getKycId1Proof());
			cd.setKycIdImage2(new String(docs.getKycId2Img()));
			cd.setKycIdNo2(docs.getKycId2Number());
			cd.setKycIdType2(docs.getKycId2Type());
			cd.setKycIdProofType2(docs.getKycId2Proof());
			cd.setKycIdImage3(new String(docs.getKycId3Img()));
			cd.setKycIdNo3(docs.getKycId3Number());
			cd.setKycIdType3(docs.getKycId3Type());
			cd.setKycIdProofType3(docs.getKycId3Proof());
			cDetialsInfo.setDocument(cd);
			
			CustomerBiometricDetails customerenrolRequest = generateBioObject(id);
			cDetialsInfo.setBiometric(customerenrolRequest);
			

		} catch (Exception e) {
			new DataAccessException(e.getMessage(), e);
		}

		return cDetialsInfo;
	}
	private String handlePostBiometricRequest(Context context)
			throws ServiceException {
		
		int initialbatchSize = 0;
		PreDataDao daos = DaoFactory.getPreDataDao();
		Parameters params = daos.readParameterValues(AppParams.DEFAULT_DEVICE_SYNCH_BATCH_SIZE);
		if(params != null)
			initialbatchSize = Integer.parseInt(params.getValue());
		
		SyncDataAccess sda = new SyncDataAccess();
		List<String> enrIds = sda.readUnSyncBios();
		
		List<String> sublist = new ArrayList<String>();
		String statustext = null;
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ context.getString(R.string.service_biometric);
		List<CustomerBiometricDetails> customerDetailsList = new ArrayList<CustomerBiometricDetails>();
		
		//Need to get the count
		int numTxns = enrIds.size();
		
		for (int k = 0; k < numTxns;) {
			int batchSize = 0;
			if (k == 0) {
				batchSize = (initialbatchSize > numTxns) ? numTxns
						: initialbatchSize;
			} else {
				int remainingTxn = numTxns - k;
				batchSize = (initialbatchSize > remainingTxn) ? remainingTxn
						: initialbatchSize;
			}
			if (batchSize >= 1)
				sublist = enrIds.subList(k, (k + batchSize));
			else
				sublist = enrIds;
			k += batchSize;

			for (String id : sublist) {
				CustomerBiometricDetails customerenrolRequest = generateBioObject(id);
				customerDetailsList.add(customerenrolRequest);
			}
			CustomerBiometricRequest ptrequest = new CustomerBiometricRequest();
			ptrequest.setBiometricList(customerDetailsList);

			String response = RequestManager.transactionRequest(url,
					CONNECTION_POST, new Gson().toJson(ptrequest));
			Gson gson = GsonAdapter.getGson();
			CustomerBiometricResponse postTxnResponse = gson.fromJson(response,
					CustomerBiometricResponse.class);
			if (postTxnResponse != null) {
				try {
					boolean status = postTxnResponse.getStatus();
					if (status) {
						statustext = postTxnResponse.getStatusText();
						customerDetailsList.clear();
					}
				} catch (DataAccessException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return statustext;
	}

	private CustomerBiometricDetails generateBioObject(String id)
			throws DataAccessException {
		CustomerBiometricDetails enrolment = null;
		try {
			 enrolment = EnrolmentDataAccess
					.readEnrolmentBioData(id);

		} catch (Exception e) {
			new DataAccessException(e.getMessage(), e);
		}

		return enrolment;
	}

}
