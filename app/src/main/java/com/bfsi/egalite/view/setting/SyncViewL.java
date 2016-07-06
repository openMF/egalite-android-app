package com.bfsi.egalite.view.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.dao.SyncDao;
import com.bfsi.egalite.dao.sqlite.DataPorting;
import com.bfsi.egalite.dao.sqlite.DbHandlerL;
import com.bfsi.egalite.dao.sqlite.EnrolmentDataAccess;
import com.bfsi.egalite.dao.sqlite.SyncDataAccess;
import com.bfsi.egalite.encryption.CryptoDataAccess;
import com.bfsi.egalite.encryption.DESedeEncryDecryption;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.CustomerDocuments;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.entity.Parameters;
import com.bfsi.egalite.entity.SyncCheck;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.servicerequest.RequestManager;
import com.bfsi.egalite.support.TextProgressBar;
import com.bfsi.egalite.sync.FileUpload;
import com.bfsi.egalite.sync.GsonAdapter;
import com.bfsi.egalite.sync.SyncUtil;
import com.bfsi.egalite.util.AppPref;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.SyncViewConstants;
import com.bfsi.egalite.util.SystemUtil;
import com.bfsi.egalite.view.HomeView;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.AgentAgenda;
import com.bfsi.mfi.rest.model.AgentAgendaRequest;
import com.bfsi.mfi.rest.model.AgentAgendaResponse;
import com.bfsi.mfi.rest.model.AgentAgendaUpdate;
import com.bfsi.mfi.rest.model.AgentRegistrationRequest;
import com.bfsi.mfi.rest.model.AgentRegistrationResponse;
import com.bfsi.mfi.rest.model.AgentValidationRequest;
import com.bfsi.mfi.rest.model.BaseResponse;
import com.bfsi.mfi.rest.model.BeginTransactionRequest;
import com.bfsi.mfi.rest.model.BeginTransactionResponse;
import com.bfsi.mfi.rest.model.CashPositionRequest;
import com.bfsi.mfi.rest.model.CashPositionResponse;
import com.bfsi.mfi.rest.model.CashRecord;
import com.bfsi.mfi.rest.model.CustomerAccount;
import com.bfsi.mfi.rest.model.CustomerAccountRequest;
import com.bfsi.mfi.rest.model.CustomerAccountResponse;
import com.bfsi.mfi.rest.model.CustomerBiometricDetails;
import com.bfsi.mfi.rest.model.CustomerBiometricRequest;
import com.bfsi.mfi.rest.model.CustomerBiometricResponse;
import com.bfsi.mfi.rest.model.CustomerDetail;
import com.bfsi.mfi.rest.model.CustomerDetailsRequest;
import com.bfsi.mfi.rest.model.CustomerDocument;
import com.bfsi.mfi.rest.model.CustomerEnrolmentDetails;
import com.bfsi.mfi.rest.model.CustomerEnrolmentRequest;
import com.bfsi.mfi.rest.model.CustomerInfoResponse;
import com.bfsi.mfi.rest.model.CustomerResponse;
import com.bfsi.mfi.rest.model.Deposit;
import com.bfsi.mfi.rest.model.DepositDetailsRequest;
import com.bfsi.mfi.rest.model.DepositDetailsResponse;
import com.bfsi.mfi.rest.model.JsonRequestWrapper;
import com.bfsi.mfi.rest.model.JsonResponseWrapper;
import com.bfsi.mfi.rest.model.Loan;
import com.bfsi.mfi.rest.model.LoanDetailsRequest;
import com.bfsi.mfi.rest.model.LoanDetailsResponse;
import com.bfsi.mfi.rest.model.LovRequest;
import com.bfsi.mfi.rest.model.LovResponse;
import com.bfsi.mfi.rest.model.MessageDetailsRequest;
import com.bfsi.mfi.rest.model.MessageDetailsResponse;
import com.bfsi.mfi.rest.model.PostTransactionRequest;
import com.bfsi.mfi.rest.model.PostTransactionResponse;
import com.bfsi.mfi.rest.model.StaticDataRequest;
import com.bfsi.mfi.rest.model.StaticDataResponse;
import com.bfsi.mfi.rest.model.TransactionRequest;
import com.google.gson.Gson;

@SuppressWarnings("unused")
public class SyncViewL extends BaseActivity {

	private Logger LOG = LoggerFactory.getLogger(getClass());
	private static final String CONNECTION = "connection";
	private static final String DATAFETCHING = "datafetching";
	private static final String SERVICE_EXCEPTION = "service";
	private static final String CONNECTION_POST = "POST";
	private static final String CONNECTION_GET = "GET";
	private final int NETWORK_FAIL = 2;
	private final int DATABASE_FAIL = 0;
	private final int CONNECTION_FAIL = 1;
	private TextProgressBar mPrgBrMain, mPrgBrStaticDataDown,
			mPrgBrCustDetails, mPrgBrAgendaTxnDetails, mPrgBrTxnPosting,
			mPrgBrCustEnrol, mPrgBrAgtLogFile, mPrgBrClrMntence,
			mPrgBrAgendaFetching, mPrgBrTransactions;
	private static String ErrorMessage = null;
	private static RadioButton mRadDataUpload, mRadTransactions, mRadDataDown;
	private OnClickListener listener;
	private static LinearLayout mLayDataUplod, mLayTransations, mLayDataDown;
	private String isSelected;
	private Button btnsync;
	@SuppressLint("HandlerLeak")
	public final Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(SyncViewL.this, ErrorMessage, Toast.LENGTH_SHORT)
					.show();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBtnLeft.setTag(R.drawable.back);
		mBtnLeft.setImageResource(R.drawable.back);
		mTitle.setText(getString(R.string.sync));
		display();
		CommonContexts.dismissProgressDialog();
	}

	@Override
	protected void onLeftAction() {

		startActivity(new Intent(SyncViewL.this, HomeView.class));
		finish();
	}

	private void display() {
		View dummy_sync_view = getLayoutInflater().inflate(
				R.layout.sync_layout, null);
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(dummy_sync_view);

		btnsync = (Button) dummy_sync_view.findViewById(R.id.btn_sync_sync);
		mPrgBrMain = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_sync);
		// Data Download
		mPrgBrStaticDataDown = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_staticdata);
		mPrgBrCustDetails = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_custdetails);
		mPrgBrAgendaTxnDetails = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_agenda_txndetails);
		// Transactions
		mPrgBrTxnPosting = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_txnposting);
		mPrgBrAgendaFetching = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_agendafetching);
		// Data Upload
		mPrgBrCustEnrol = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_custenrollment);
		mPrgBrAgtLogFile = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_agtlogfile);
		mPrgBrClrMntence = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_clrmaintenance);
		mPrgBrTransactions = (TextProgressBar) dummy_sync_view
				.findViewById(R.id.progbar_transactions);

		mLayDataUplod = (LinearLayout) dummy_sync_view
				.findViewById(R.id.lay_dataupload);
		mLayTransations = (LinearLayout) dummy_sync_view
				.findViewById(R.id.lay_transactions);
		mLayDataDown = (LinearLayout) dummy_sync_view
				.findViewById(R.id.lay_datadownload);
		mLayDataUplod.setVisibility(View.GONE);
		mLayTransations.setVisibility(View.GONE);
		mLayDataDown.setVisibility(View.GONE);

		mRadDataUpload = (RadioButton) findViewById(R.id.radbtn_data_upload);
		mRadTransactions = (RadioButton) findViewById(R.id.radbtn_transactions);
		mRadDataDown = (RadioButton) findViewById(R.id.radbtn_data_downlod);

		radiobuttonevent();
		mRadDataUpload.setOnClickListener(listener);
		mRadTransactions.setOnClickListener(listener);
		mRadDataDown.setOnClickListener(listener);

		btnsync.setVisibility(View.GONE);
		mPrgBrMain.setVisibility(View.GONE);

		enableDisableClick(true);

		btnsync.setTypeface(CommonContexts.getTypeface(SyncViewL.this));
		btnsync.setOnClickListener(syncClickListener);
	}

	private void radiobuttonevent() {
		listener = new OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean checked = ((RadioButton) view).isChecked();
				switch (view.getId()) {
				case R.id.radbtn_data_upload:
					if (checked)
						mLayDataUplod.setVisibility(View.VISIBLE);
					mLayTransations.setVisibility(View.GONE);
					mLayDataDown.setVisibility(View.GONE);
					mRadDataUpload.setChecked(true);
					mRadTransactions.setChecked(false);
					mRadDataDown.setChecked(false);
					btnsync.setVisibility(View.VISIBLE);
					mPrgBrMain.setVisibility(View.VISIBLE);
					isSelected = "SU";
					break;

				case R.id.radbtn_transactions:
					if (checked)
						mLayTransations.setVisibility(View.VISIBLE);
					mLayDataUplod.setVisibility(View.GONE);
					mLayDataDown.setVisibility(View.GONE);
					mRadTransactions.setChecked(true);
					mRadDataUpload.setChecked(false);
					mRadDataDown.setChecked(false);
					btnsync.setVisibility(View.VISIBLE);
					mPrgBrMain.setVisibility(View.VISIBLE);
					isSelected = "TXN";
					break;

				case R.id.radbtn_data_downlod:
					if (checked)
						mLayDataDown.setVisibility(View.VISIBLE);
					mLayDataUplod.setVisibility(View.GONE);
					mLayTransations.setVisibility(View.GONE);
					mRadDataDown.setChecked(true);
					mRadTransactions.setChecked(false);
					mRadDataUpload.setChecked(false);
					btnsync.setVisibility(View.VISIBLE);
					mPrgBrMain.setVisibility(View.VISIBLE);
					isSelected = "DD";
					break;

				}
			}
		};
	}

	OnClickListener syncClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			SyncCheck sc = new Gson().fromJson(
					AppPref.getPref(SyncViewL.this, AppPref.SYNCCHECK),
					SyncCheck.class);
			if (isSelected.equalsIgnoreCase("SU")) {
				if ((sc.getBatchId().equalsIgnoreCase("1000"))
						&& sc.getDd().equalsIgnoreCase("N")) {
					new DownloadsOperation().execute("");
				} else if (sc.getBatchId().equalsIgnoreCase("1001")
						&& sc.getSu().equalsIgnoreCase("Y")
						&& sc.getDd().equalsIgnoreCase("Y")) {
					new DownloadsOperation().execute("");
				} else
					Toast.makeText(SyncViewL.this,
							SyncViewConstants.PLZ_DATAUPLD, Toast.LENGTH_SHORT)
							.show();
			} else if (isSelected.equalsIgnoreCase("DD")) {
				if ((sc.getBatchId().equalsIgnoreCase("1001") || sc
						.getBatchId().equalsIgnoreCase("1000"))
						&& sc.getDd().equalsIgnoreCase("N")) {

					PreDataDao dao = DaoFactory.getPreDataDao();
					int count = dao.agendaCount();
					if (count > 0) {
						AlertDialog.Builder adb = new AlertDialog.Builder(
								SyncViewL.this);
						adb.setTitle("Sync Info");
						adb.setMessage(SyncViewConstants.UN_Processed_RECRD);
						adb.setPositiveButton(getString(R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										new UploadOperation().execute("");
									}
								});
						adb.setNegativeButton(getString(R.string.cancel),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						adb.show();
					} else if (count == 0) {
						new UploadOperation().execute("");
					}
				} else
					Toast.makeText(SyncViewL.this,
							SyncViewConstants.DATA_DWNLD, Toast.LENGTH_SHORT)
							.show();

			} else if (isSelected.equalsIgnoreCase("TXN")) {
				if (AppPref
						.getPrefBoolean(SyncViewL.this, AppPref.ISONLINESYNC)) {
					Toast.makeText(SyncViewL.this,
							SyncViewConstants.ONLINE_SYNC_ON,
							Toast.LENGTH_SHORT).show();
				} else if (!AppPref.getPrefBoolean(SyncViewL.this,
						AppPref.ISONLINESYNC)) {
					new TransactionsOperations().execute("");
				}
			}
		}
	};

	private String getLastSyncTime() {
		PreDataDao preDao = DaoFactory.getPreDataDao();
		return preDao.readLastSyncTime();
	}

	private class DownloadsOperation extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {

			try {
				if (SystemUtil.haveNetworkConnection(SyncViewL.this)) {
					PreDataDao lgnDao = DaoFactory.getPreDataDao();
					String response = syncStaticDataDownloadProc(lgnDao);
					return response;
				} else
					return "Failed";

			} catch (ServiceException e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(SERVICE_EXCEPTION, e.getMessage());
				return null;
			} catch (DataAccessException e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(DATAFETCHING, e.getMessage());
				return null;
			} catch (Exception e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(DATAFETCHING, e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			enableDisableClick(false);
			mPrgBrMain.setProgress(0);
			mPrgBrStaticDataDown.setProgress(0);

		}

		@Override
		protected void onPostExecute(String result) {
			enableDisableClick(true);
			// dismissProgressDialog();
			if (result != null && result.equalsIgnoreCase("Success")) {
				AppPref.updatePref(SyncViewL.this, AppPref.ISFIRSTSYNC, false);
				Toast.makeText(SyncViewL.this,
						SyncViewConstants.SYNC_CMPLTD_SUCCESS,
						Toast.LENGTH_SHORT).show();
				mPrgBrMain.setProgress(0);
				Intent intent = new Intent(SyncViewL.this, HomeView.class);
				startActivity(intent);
				finish();
			} else if (result != null && result.equalsIgnoreCase("Failed")) {
				mPrgBrMain.setProgress(0);
				Toast.makeText(SyncViewL.this, SyncViewConstants.NO_NETWORK,
						Toast.LENGTH_SHORT).show();
			} else {
				mPrgBrMain.setProgress(0);
				Toast.makeText(SyncViewL.this,
						SyncViewConstants.STATIC_DWNLD_FAILED,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class UploadOperation extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {

			try {
				if (SystemUtil.haveNetworkConnection(SyncViewL.this)) {
					SyncDao dao = DaoFactory.getSyncDao();
					String response = syncDatauploadProcess(dao);
					return response;
				} else
					return "Failed";

			} catch (ServiceException e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(SERVICE_EXCEPTION, e.getMessage());
				return null;
			} catch (DataAccessException e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(DATAFETCHING, e.getMessage());
				return null;
			} catch (Exception e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(DATAFETCHING, e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			enableDisableClick(false);
		}

		@Override
		protected void onPostExecute(String result) {
			enableDisableClick(true);
			if (result != null && result.equalsIgnoreCase("Success")) {
				Toast.makeText(SyncViewL.this,
						SyncViewConstants.DATA_UPLD_SUCCESS, Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(SyncViewL.this, HomeView.class);
				startActivity(intent);
				finish();
			} else if (result != null && result.equalsIgnoreCase("Failed")) {
				mPrgBrMain.setProgress(0);
				Toast.makeText(SyncViewL.this, SyncViewConstants.NO_NETWORK,
						Toast.LENGTH_SHORT).show();
			} else {
				mPrgBrMain.setProgress(0);
				Toast.makeText(SyncViewL.this,
						SyncViewConstants.DATA_UPLD_FAILED, Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private class TransactionsOperations extends
			AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			try {

				if (SystemUtil.haveNetworkConnection(SyncViewL.this)) {
					SyncDao dao = DaoFactory.getSyncDao();
					String response = syncTransactionsProcess(dao);

					return response;
				} else
					return "Failed";

			} catch (ServiceException e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(SERVICE_EXCEPTION, e.getMessage());
				return null;
			} catch (DataAccessException e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(DATAFETCHING, e.getMessage());
				return null;
			} catch (Exception e) {
				enableDisableClick(true);
				LOG.debug(e.getMessage());
				handleError(DATAFETCHING, e.getMessage());
				return null;
			}
		}

		@Override
		protected void onPreExecute() {
			enableDisableClick(false);
		}

		@Override
		protected void onPostExecute(String result) {
			enableDisableClick(true);

			if (result != null && result.equalsIgnoreCase("Success")) {
				Toast.makeText(SyncViewL.this,
						SyncViewConstants.POSTING_TXN_SUCCESS,
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(SyncViewL.this, HomeView.class);
				startActivity(intent);
				finish();
			} else if (result != null && result.equalsIgnoreCase("Failed")) {
				mPrgBrTxnPosting.setProgress(0);
				mPrgBrMain.setProgress(0);
				Toast.makeText(SyncViewL.this, SyncViewConstants.NO_NETWORK,
						Toast.LENGTH_SHORT).show();
			} else {
				mPrgBrTxnPosting.setProgress(0);
				mPrgBrMain.setProgress(0);
				Toast.makeText(SyncViewL.this,
						SyncViewConstants.POSTING_TXN_FAILED,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean initiateSync() throws Exception {
		boolean agentstatus = false;
		boolean connection = SystemUtil.haveNetworkConnection(SyncViewL.this);
		if (connection) {

			String plainDataKey = CryptoDataAccess.getDataKey();
			AgentRegistrationRequest ag = new AgentRegistrationRequest();
			ag.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			ag.setDeviceId(CommonContexts.mLOGINVALIDATION.getIMEI());
			ag.setDataKey(plainDataKey);

			Gson gson = new Gson();
			String json = gson.toJson(ag);
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_login);
			try {
				String response = RequestManager.initiateRequest(url,
						CONNECTION_POST, json);
				if (response != null) {
					Gson gsons = GsonAdapter.getGson();
					AgentRegistrationResponse arr = gsons.fromJson(response,
							AgentRegistrationResponse.class);
					if (arr != null) {
						agentstatus = arr.getStatus();
						return agentstatus;
					}
				}
			} catch (ServiceException e) {
				if (e.getMessage().contains("Hostname")) {
					String response = RequestManager.initiateRequest(url,
							CONNECTION_POST, json);
					if (response != null) {
						Gson gsons = GsonAdapter.getGson();
						AgentRegistrationResponse arr = gsons.fromJson(
								response, AgentRegistrationResponse.class);
						if (arr != null) {
							agentstatus = arr.getStatus();
							return agentstatus;
						}
					}
				} else
					throw new DataAccessException(e.getMessage(), e);
			} catch (Exception e) {
				if (e.getMessage().contains("Hostname")) {
					String response = RequestManager.initiateRequest(url,
							CONNECTION_POST, json);
					if (response != null) {
						Gson gsons = GsonAdapter.getGson();
						AgentRegistrationResponse arr = gsons.fromJson(
								response, AgentRegistrationResponse.class);
						if (arr != null) {
							agentstatus = arr.getStatus();
							return agentstatus;
						}
					}
				} else
					throw new DataAccessException(e.getMessage(), e);
			}
		} else {

		}
		return agentstatus;

	}

	private StaticDataRequest generatStaticeObject() {
		StaticDataRequest arr = new StaticDataRequest();
		arr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		arr.setDeviceId(CommonContexts.mLOGINVALIDATION.getIMEI());
		return arr;
	}

	private String getJsonString(StaticDataRequest arr) {
		Gson gson = new Gson();
		return gson.toJson(arr);
	}

	private String getJsonString(JsonRequestWrapper arr) {
		Gson gson = new Gson();
		return gson.toJson(arr);
	}

	private StaticDataResponse parseStaticObject(String response) {
		StaticDataResponse arr = null;
		try{
			arr = new Gson().fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, response),
				StaticDataResponse.class);
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return arr;
	}

	private StaticDataResponse handleStatic() throws Exception {
		StaticDataRequest arr = generatStaticeObject();
		JsonRequestWrapper jrw = null;
		StaticDataResponse arrs = null;
		Gson gson = new Gson();
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_static);
		try {
			if (SystemUtil.haveNetworkConnection(SyncViewL.this)) {
				jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY,gson .toJson(arr)));
				
				String response = RequestManager.transactionRequest(url,
						CONNECTION_POST, getJsonString(jrw));
				JsonResponseWrapper jresw = gson.fromJson(response, JsonResponseWrapper.class);
				if(jresw != null && jresw.getJsonString()!= null)
				{
					arrs = parseStaticObject(jresw.getJsonString());
				}
			}
		} catch (ServiceException e) {
			if (e.getMessage().contains("Hostname")) {
								
				String response = RequestManager.transactionRequest(url,
						CONNECTION_POST, getJsonString(jrw));
				JsonResponseWrapper jresw = new Gson().fromJson(response, JsonResponseWrapper.class);
				if(jresw != null && jresw.getJsonString()!= null)
				{
					arrs = parseStaticObject(jresw.getJsonString());
					return arrs;
				}
			} else {
				throw new ServiceException(e.getMessage(), e);
			}

		} catch (Exception e) {
			if (e.getMessage().contains("Hostname")) {
				
				String response = RequestManager.transactionRequest(url,
						CONNECTION_POST, getJsonString(jrw));
				JsonResponseWrapper jresw = new Gson().fromJson(response, JsonResponseWrapper.class);
				if(jresw != null && jresw.getJsonString()!= null)
				{
					arrs = parseStaticObject(jresw.getJsonString());
					return arrs;
				}
			} else {
				throw new ServiceException(e.getMessage(), e);
			}
		}
		return arrs;

	}

	private <T> T getResponse(String url, String method, Class<T> clazz) {
		T responseObject = null;
		try {
			String response = RequestManager.handleReadData(url, method);
			Gson gson = GsonAdapter.getGson();
			responseObject = gson.fromJson(response, clazz);
			return responseObject;
		} catch (ServiceException e) {
			handleError(SERVICE_EXCEPTION, e.getMessage());
		} catch (Exception e) {
			handleError(SERVICE_EXCEPTION, e.getMessage());
		}
		return responseObject;

	}

	private <T> T getResponse(String url, String method, Class<T> clazz,
			String payload) {
		T responseObject = null;
		try {
			if (SystemUtil.haveNetworkConnection(SyncViewL.this)) {
				String response = RequestManager.transactionRequest(url,
						method, payload);
				Gson gson = GsonAdapter.getGson();
				responseObject = gson.fromJson(response, clazz);
				return responseObject;
			}
		} catch (ServiceException e) {
			handleError(SERVICE_EXCEPTION, e.getMessage());
		} catch (Exception e) {
			handleError(SERVICE_EXCEPTION, e.getMessage());
		}
		return responseObject;

	}

	private String syncStaticDataDownloadProc(PreDataDao lgnDao)
			throws ServiceException {

		try {
			SyncDao daos = DaoFactory.getSyncDao();
			daos.deleteMaintainceData();
			daos.deleteDDMaintainceData();

			long synctime = DateUtil.getCurrentDataTime();

			BaseResponse isAgentValid = getAgentValidationCheck();
			if (isAgentValid != null && isAgentValid.getStatus()) {

				Parameters paramss = lgnDao
						.readParameterValues(AppParams.DEFAULT_DEVICE_SYNCH_BATCH_SIZE);
				if (paramss != null)
					CommonContexts.BATCHSIZE = Integer.parseInt(paramss
							.getValue());
				PreDataDao dao = DaoFactory.getPreDataDao();
				// Delete the CashRecord
				SQLiteDatabase db = DbHandlerL.getInstance()
						.getReadableDatabase();
				db.delete(DbHandlerL.TABLE_MBS_AGENT_CASH_RECORD, null, null);

				// read static data details
				mPrgBrStaticDataDown.setText("Fetching Message Codes");
				mPrgBrStaticDataDown.incrementProgressBy(20);
				messageCodes();
				mPrgBrMain.incrementProgressBy(20);

				mPrgBrStaticDataDown
						.setText("Fetching Agents, Devices, branches..");
				mPrgBrStaticDataDown.incrementProgressBy(40);
				StaticDataResponse staticDataDetails = handleStatic();
				// extrat individual objects
				if (staticDataDetails != null) {
					mPrgBrStaticDataDown.incrementProgressBy(60);
					lgnDao.updateAgentsData(staticDataDetails.getAgentDetail());
					DataPorting.setDeviceData(staticDataDetails
							.getDevicedetail());
					DataPorting.setCurrency(staticDataDetails
							.getCurrencyDetail());
					DataPorting.setBranchCodes(staticDataDetails
							.getBranchDetail());
					DataPorting
							.setParamData(staticDataDetails.getParamsValue());
					DataPorting.setSmsTemplates(staticDataDetails
							.getSmsTempList());

					mPrgBrStaticDataDown.setText("Fetching Lov");
					mPrgBrStaticDataDown.incrementProgressBy(80);
					DataPorting.setLov(getLov());
					mPrgBrStaticDataDown.incrementProgressBy(100);
					mPrgBrStaticDataDown.setText("");
					mPrgBrMain.incrementProgressBy(40);

					// Customer Details prg
					mPrgBrCustDetails.setText("Fetching Customer Details");
					mPrgBrCustDetails.incrementProgressBy(30);
					customerDetails(synctime);
					mPrgBrCustDetails.setText("");
					mPrgBrCustDetails.incrementProgressBy(100);
					mPrgBrMain.incrementProgressBy(60);

					// Transactions prg.
					mPrgBrAgendaTxnDetails.setText("Fetching Loan Details");
					mPrgBrAgendaTxnDetails.incrementProgressBy(30);
					loanDetails(synctime);
					mPrgBrMain.incrementProgressBy(70);

					mPrgBrAgendaTxnDetails.setText("Fetching Deposit Details");
					mPrgBrAgendaTxnDetails.incrementProgressBy(50);
					depositdetails(synctime);
					mPrgBrMain.incrementProgressBy(75);

					mPrgBrAgendaTxnDetails.setText("Fetching Agenda Details");
					mPrgBrAgendaTxnDetails.incrementProgressBy(80);
					agentAgenda();
					mPrgBrMain.incrementProgressBy(85);

					mPrgBrAgendaTxnDetails.setText("Fetching Savings Accounts");
					mPrgBrAgendaTxnDetails.incrementProgressBy(90);
					custAccDetails(synctime);
					mPrgBrMain.incrementProgressBy(90);

					mPrgBrAgendaTxnDetails.setText("Fetching Cash records");
					mPrgBrAgendaTxnDetails.incrementProgressBy(95);
					getCashRecordDetails(synctime);
					mPrgBrMain.incrementProgressBy(95);

					mPrgBrAgendaTxnDetails.setText(".....");
					mPrgBrAgendaTxnDetails.incrementProgressBy(100);
					mPrgBrMain.incrementProgressBy(97);

					mPrgBrAgendaTxnDetails.setText("");
					updateSyncTime(dao, staticDataDetails);
					mPrgBrMain.incrementProgressBy(100);

					syncDataDownloadUpdate();

					return "Success";
				}
			} else if (isAgentValid != null) {

				Toast.makeText(SyncViewL.this,
						SyncViewConstants.NOT_VALID_USER, Toast.LENGTH_SHORT)
						.show();
				return "Failed";
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "Failed";
	}

	private void syncDataDownloadUpdate() {
		SyncCheck sc = new Gson().fromJson(
				AppPref.getPref(SyncViewL.this, AppPref.SYNCCHECK),
				SyncCheck.class);
		if ((sc.getBatchId().equalsIgnoreCase("1000"))
				&& sc.getDd().equalsIgnoreCase("N")) {
			SyncCheck scs = new SyncCheck();
			scs.setBatchId("1001");
			scs.setSu("Y");
			scs.setDd("N");
			AppPref.updatePref(SyncViewL.this, AppPref.SYNCCHECK,
					new Gson().toJson(scs));

		} else if (sc.getBatchId().equalsIgnoreCase("1001")
				&& sc.getSu().equalsIgnoreCase("Y")
				&& sc.getDd().equalsIgnoreCase("Y")) {
			SyncCheck scs = new SyncCheck();
			scs.setBatchId("1000");
			scs.setSu("Y");
			scs.setDd("N");
			AppPref.updatePref(SyncViewL.this, AppPref.SYNCCHECK,
					new Gson().toJson(scs));

		}
	}

	private void updateSyncTime(PreDataDao dao,
			StaticDataResponse staticDataDetails) {
		mPrgBrMain.setText("Updating the User LastSyncTime ");
		dao.updateLastSyncTime(staticDataDetails.getAgentDetail().getId(),
				DateUtil.getCurrentDataTime());
		// update last sync time in device table
		mPrgBrMain.setText("Updating the Device LastSyncTime ");
		dao.updateDeviceLastSynctime(staticDataDetails.getAgentDetail()
				.getDeviceId(), DateUtil.getCurrentDataTime());
	}

	private void custAccDetails(long synctime) {

		getCustAccDetails(synctime);
		mPrgBrMain.incrementProgressBy(98);

	}

	private void cashRecordDetails(long synctime) {
		getCustAccDetails(synctime);
		mPrgBrMain.incrementProgressBy(98);
	}

	private void customerDetails(long synctime) {
		getCustomerDetails(synctime);
		mPrgBrMain.incrementProgressBy(70);
	}

	private void loanDetails(long synctime) throws Exception {
		getLoanDetails(synctime);
		mPrgBrMain.incrementProgressBy(80);
	}

	private void agentAgenda() {
		getAgentAgenda();
		mPrgBrMain.incrementProgressBy(95);
	}

	private void depositdetails(long synctime) throws Exception {
		getDepositDetails(synctime);
		mPrgBrMain.incrementProgressBy(90);
	}

	private void messageCodes() throws ServiceException {
		MessageDetailsResponse response = getMessageCodes();
		DataPorting.setMessageCodes(response);
		mPrgBrMain.incrementProgressBy(10);
	}

	private String syncDatauploadProcess(SyncDao dao) throws ServiceException {

		BaseResponse isAgentValid = null;
		int initialbatchSize = 0;
		PreDataDao daos = DaoFactory.getPreDataDao();
		Parameters params = daos
				.readParameterValues(AppParams.DEFAULT_DEVICE_SYNCH_BATCH_SIZE);
		if (params != null)
			initialbatchSize = Integer.parseInt(params.getValue());
		try {
			isAgentValid = getAgentValidationCheck();
			if (isAgentValid != null && isAgentValid.getStatus()) {
				mPrgBrTransactions.setText("Posting Transactions...");
				mPrgBrTransactions.incrementProgressBy(20);
				handlePostTransactionRequest(getUnSyncedTxnIds(),
						initialbatchSize);
				mPrgBrTransactions.setText("");

				mPrgBrCustEnrol.setText("Posting Customer Enrollments...");
				mPrgBrCustEnrol.incrementProgressBy(20);
				handlePostEnrolmentRequest(getUnSyncedEndIds(),
						initialbatchSize);

				uploadFile();
				mPrgBrClrMntence.incrementProgressBy(50);
				dao.deleteMaintainceData();
				mPrgBrClrMntence.incrementProgressBy(100);

				syncDataUploadUpdate();
				return "Success";
			} else if (isAgentValid != null) {

				Toast.makeText(SyncViewL.this,
						SyncViewConstants.NOT_VALID_USER, Toast.LENGTH_SHORT)
						.show();
				return "Failed";
			}

		} catch (Exception e) {
			if (e != null && e.getMessage().contains("Hostname")) {
				handlePostTransactionRequest(getUnSyncedTxnIds(),
						initialbatchSize);
				handlePostEnrolmentRequest(getUnSyncedEndIds(),
						initialbatchSize);
				handlePostBiometricRequest(getUnSyncedBios(), initialbatchSize);
				// uploadFile();
				dao.deleteMaintainceData();
			} else
				throw new ServiceException(e.getMessage(), e);
		}
		return "Failed";
	}

	private void syncDataUploadUpdate() {
		SyncCheck sc = new Gson().fromJson(
				AppPref.getPref(SyncViewL.this, AppPref.SYNCCHECK),
				SyncCheck.class);
		if ((sc.getBatchId().equalsIgnoreCase("1001") || sc.getBatchId()
				.equalsIgnoreCase("1000")) && sc.getDd().equalsIgnoreCase("N")) {

			PreDataDao daoCount = DaoFactory.getPreDataDao();
			int count = daoCount.agendaCount();
			if (count > 0) {
				SyncCheck scs = new SyncCheck();
				scs.setBatchId("1001");
				scs.setSu("Y");
				scs.setDd("Y");
				AppPref.updatePref(SyncViewL.this, AppPref.SYNCCHECK,
						new Gson().toJson(scs));
			} else if (count == 0) {
				SyncCheck scs = new SyncCheck();
				scs.setBatchId("1001");
				scs.setSu("Y");
				scs.setDd("Y");

				AppPref.updatePref(SyncViewL.this, AppPref.SYNCCHECK,
						new Gson().toJson(scs));

			}

		}
	}

	private String syncTransactionsProcess(SyncDao dao) throws ServiceException {
		BaseResponse isAgentValid = null;
		int initialbatchSize = 0;
		PreDataDao daos = DaoFactory.getPreDataDao();
		Parameters params = daos
				.readParameterValues(AppParams.DEFAULT_DEVICE_SYNCH_BATCH_SIZE);
		if (params != null)
			initialbatchSize = Integer.parseInt(params.getValue());

		try {
			isAgentValid = getAgentValidationCheck();
			if (isAgentValid != null && isAgentValid.getStatus()) {
				mPrgBrTxnPosting.setText("Posting Transactions ...");
				mPrgBrTxnPosting.incrementProgressBy(20);
				handlePostTransactionRequest(getUnSyncedTxnIds(),
						initialbatchSize);
				mPrgBrTxnPosting.incrementProgressBy(40);

				mPrgBrTxnPosting.setText("Posting Customer Enrollments..");
				handlePostEnrolmentRequest(getUnSyncedEndIds(),
						initialbatchSize);
				mPrgBrTxnPosting.incrementProgressBy(100);

				mPrgBrAgendaFetching.setText("Agenda fetching...");
				mPrgBrAgendaFetching.incrementProgressBy(20);
				getAgentAgendaafterSync();
				mPrgBrAgendaFetching.incrementProgressBy(60);
				mPrgBrAgendaFetching.setText("Updating Cash...");
				getCashRecordDetails(DateUtil.getCurrentDataTime());
				mPrgBrAgendaFetching.incrementProgressBy(100);
				return "Success";
			} else if (isAgentValid != null) {
				Toast.makeText(SyncViewL.this,
						SyncViewConstants.NOT_VALID_USER, Toast.LENGTH_SHORT)
						.show();
				return "Failed";
			}

		} catch (Exception e) {
			if (e.getMessage().contains("Hostname")) {
				handlePostTransactionRequest(getUnSyncedTxnIds(),
						initialbatchSize);
				handlePostEnrolmentRequest(getUnSyncedEndIds(),
						initialbatchSize);
				getAgentAgendaafterSync();
				getCashRecordDetails(DateUtil.getCurrentDataTime());

			} else
				throw new ServiceException(e.getMessage(), e);
		}
		return "Failed";
	}

	private void getAgentAgendaafterSync() {
		mPrgBrAgendaFetching.incrementProgressBy(30);
		List<AgentAgenda> agendaDetails = null;
		List<AgentAgendaUpdate> agendaUpdate = new ArrayList<AgentAgendaUpdate>();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_agenda_list);
			AgentAgendaRequest aar = new AgentAgendaRequest();
			aar.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			aar.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
			aar.setLocationCode(CommonContexts.mLOGINVALIDATION
					.getLocationCode());
			aar.setBatchSize(CommonContexts.BATCHSIZE);
			aar.setAgentAgendaUpdate(agendaUpdate);
			
			try {
				Gson gson = GsonAdapter.getGson();
				JsonRequestWrapper jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, gson.toJson(aar)));
				
			JsonResponseWrapper jsonagendaResponse = getResponse(url,
					CONNECTION_POST, JsonResponseWrapper.class,
					getAgendaString(jrw));
			if(jsonagendaResponse != null && jsonagendaResponse.getJsonString()!= null)
			{
				AgentAgendaResponse agendaResponse = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jsonagendaResponse.getJsonString()), AgentAgendaResponse.class);
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
				mPrgBrAgendaFetching.incrementProgressBy(60);
			}
			} catch (DataAccessException e) {
				throw new DataAccessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage(), e);
			}
		} while (agendaDetails != null && agendaDetails.size() > 0);
	}

	private MessageDetailsResponse parseMessageObject(String response) {
		MessageDetailsResponse arr = null;
		try{
			arr= 	new Gson().fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, response),
				MessageDetailsResponse.class);
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return arr;
	}

	private LovResponse parseLovObject(String response) {
		Gson gson = new Gson();
		LovResponse arr = null;
		try{
			JsonResponseWrapper jsonresponse = gson.fromJson(response, JsonResponseWrapper.class);
			if(jsonresponse != null && jsonresponse.getJsonString()!= null)
			 arr = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jsonresponse.getJsonString()), LovResponse.class);
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return arr;
	}

	private MessageDetailsResponse getMessageCodes() throws ServiceException {
		MessageDetailsRequest arr = new MessageDetailsRequest();
		JsonRequestWrapper jrw = null;
		Gson gson = new Gson();
		arr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		MessageDetailsResponse arrs = null;
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_messages);
		try {
			jrw = new JsonRequestWrapper();
			jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, gson.toJson(arr)));
			
			LOG.debug(getResources().getString(R.string.MFB00113));
			String response = RequestManager.initiateRequest(url,
					CONNECTION_POST,gson.toJson(jrw));
			if (response != null) {
				JsonResponseWrapper jresw = new Gson().fromJson(response, JsonResponseWrapper.class);
				if(jresw != null && jresw.getJsonString() != null)
				{
					arrs= parseMessageObject(jresw.getJsonString());
				}
			}
		} catch (ServiceException e) {

			if (e.getMessage().contains("Hostname")) {
				String response = RequestManager.initiateRequest(url,
						CONNECTION_POST, new Gson().toJson(jrw));
				if (response != null) {
					JsonResponseWrapper jresw = new Gson().fromJson(response, JsonResponseWrapper.class);
					if(jresw != null && jresw.getJsonString() != null)
					{
						arrs= parseMessageObject(jresw.getJsonString());
					}
				}
			} else {

				LOG.error(
						getResources().getString(R.string.MFB00113)
								+ e.getMessage(), e);
				Toast.makeText(SyncViewL.this,
						getResources().getString(R.string.MFB00113),
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			if (e.getMessage().contains("Hostname")) {
				String response = RequestManager.initiateRequest(url,
						CONNECTION_POST, new Gson().toJson(jrw));
				if (response != null) {
					JsonResponseWrapper jresw = new Gson().fromJson(response, JsonResponseWrapper.class);
					if(jresw != null && jresw.getJsonString() != null)
					{
						arrs= parseMessageObject(jresw.getJsonString());
					}
				}
			} else {

				LOG.error(
						getResources().getString(R.string.MFB00113)
								+ e.getMessage(), e);
				Toast.makeText(SyncViewL.this,
						getResources().getString(R.string.MFB00113),
						Toast.LENGTH_SHORT).show();
			}
		}
		return arrs;
	}

	private LovResponse getLov() throws ServiceException {

		LovRequest arr = new LovRequest();

		JsonRequestWrapper jrw   =null;
		LovResponse arrs = null;
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_lov);
		try {
			LOG.debug(getResources().getString(R.string.MFB00113));
			jrw = new JsonRequestWrapper();
			jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, new Gson().toJson(arr)));
			
			String response = RequestManager.initiateRequest(url,
					CONNECTION_POST, new Gson().toJson(jrw));
			if (response != null) {
				arrs = parseLovObject(response);
			}
		} catch (ServiceException e) {

			if (e.getMessage().contains("Hostname")) {
				String response = RequestManager.initiateRequest(url,
						CONNECTION_POST, new Gson().toJson(jrw));
				if (response != null) {
					arrs = parseLovObject(response);
					return arrs;
				}
			} else {

				LOG.error(
						getResources().getString(R.string.MFB00113)
								+ e.getMessage(), e);
				Toast.makeText(SyncViewL.this,
						getResources().getString(R.string.MFB00113),
						Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			if (e.getMessage().contains("Hostname")) {
				String response = RequestManager.initiateRequest(url,
						CONNECTION_POST, new Gson().toJson(jrw));
				if (response != null) {
					arrs = parseLovObject(response);
					return arrs;
				}
			} else {

				LOG.error(
						getResources().getString(R.string.MFB00113)
								+ e.getMessage(), e);
				Toast.makeText(SyncViewL.this,
						getResources().getString(R.string.MFB00113),
						Toast.LENGTH_SHORT).show();
			}
		}
		return arrs;
	}

	// private void readAgetnValues(PreDataDao lngDao) {
	// Agent agent = lngDao.readAgentValues();
	// Parameters paramss = lngDao
	// .readParameterValues(AppParams.DEFAULT_DEVICE_SYNCH_BATCH_SIZE);
	// if (paramss != null)
	// CommonContexts.BATCHSIZE = Integer.parseInt(paramss.getValue());
	// if (agent != null) {
	// CommonContexts.mLOGINVALIDATION.setAgentId(agent.getAgentId());
	// CommonContexts.mLOGINVALIDATION.setLocationCode(agent
	// .getLocationCode());
	// CommonContexts.mLOGINVALIDATION.setDeviceId(agent.getDeviceId());
	// }
	// }

	private void getLoanDetails(long synctime) throws Exception {
		List<Loan> loandetails = null;
		Gson gson = new Gson();
		List<String> loanAcNo = new ArrayList<String>();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_loan_details);
			LoanDetailsRequest ldr = new LoanDetailsRequest();
			ldr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			ldr.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
			ldr.setBatchSize(CommonContexts.BATCHSIZE);
			ldr.setLocCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
			ldr.setRecvdLoans(loanAcNo);
			ldr.setSyncSessionId(String.valueOf(synctime));
			try {
				JsonRequestWrapper jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, gson.toJson(ldr)));
				
				JsonResponseWrapper jsonloanresponse = getResponse(url, CONNECTION_POST,
					JsonResponseWrapper.class, gson.toJson(jrw));
				if(jsonloanresponse != null && jsonloanresponse.getJsonString()!= null)
				{
					LoanDetailsResponse loanresponse = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jsonloanresponse.getJsonString()), LoanDetailsResponse.class);
					if (loanresponse != null) {
						loandetails = loanresponse.getLoanList();
					}
					if (loandetails != null && loandetails.size() > 0) {
						DataPorting.setLoanData(loandetails);
						if (loanAcNo != null) {
							loanAcNo = null;
							loanAcNo = new ArrayList<String>();
						}
						for (Loan ld : loandetails) {
							loanAcNo.add(ld.getLoanAccNo());
						}
					}
				}
			} catch (DataAccessException e) {
				throw new DataAccessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage(), e);
			}
		} while (loandetails != null && loandetails.size() > 0);
	}

	// EGA-MN15-000017
	private BaseResponse getAgentValidationCheck() throws Exception {
		BaseResponse agentResponse = null;
		JsonRequestWrapper jrw =  null;
		Gson gson = GsonAdapter.getGson();
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_agentvalidationrequest);
		AgentValidationRequest ldr = new AgentValidationRequest();
		ldr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		ldr.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		ldr.setBatchSize(CommonContexts.BATCHSIZE);
		ldr.setLocCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
		ldr.setSimNo(CommonContexts.mLOGINVALIDATION.getSimNo());
		ldr.setImeiNo(CommonContexts.DEVICEID);
		try {
			jrw = new JsonRequestWrapper();
			jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, new Gson().toJson(ldr)));
			
			String response = RequestManager.initiateRequest(url,
					CONNECTION_GET, getJsonString(jrw));
			
			JsonResponseWrapper jresw = gson.fromJson(response, JsonResponseWrapper.class);
			if(jresw != null && jresw.getJsonString() != null)
			{
				agentResponse = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jresw.getJsonString()), BaseResponse.class);
				if (agentResponse != null) {
					return agentResponse;
				}
			}

		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (ServiceException e) {
			if (e.getMessage().contains("Hostname")) {
				String response = RequestManager.initiateRequest(url,
						CONNECTION_GET, getJsonString(jrw));
				
				JsonResponseWrapper jresw = gson.fromJson(response, JsonResponseWrapper.class);
				if(jresw != null && jresw.getJsonString() != null)
				{
					agentResponse = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jresw.getJsonString()), BaseResponse.class);
					if (agentResponse != null) {
						return agentResponse;
					}
				}
			} else {

				LOG.error(
						getResources().getString(R.string.MFB00124)
								+ e.getMessage(), e);
				Toast.makeText(SyncViewL.this,
						getResources().getString(R.string.MFB00124),
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			if (e.getMessage().contains("Hostname")) {
				String response = RequestManager.initiateRequest(url,
						CONNECTION_GET, getJsonString(jrw));
				
				JsonResponseWrapper jresw = gson.fromJson(response, JsonResponseWrapper.class);
				if(jresw != null && jresw.getJsonString() != null)
				{
					agentResponse = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jresw.getJsonString()), BaseResponse.class);
					if (agentResponse != null) {
						return agentResponse;
					}
				}
			} else {

				LOG.error(
						getResources().getString(R.string.MFB00113)
								+ e.getMessage(), e);
				Toast.makeText(SyncViewL.this,
						getResources().getString(R.string.MFB00113),
						Toast.LENGTH_SHORT).show();
			}
		}
		return agentResponse;

	}

	

	

	private String getAgendaString(JsonRequestWrapper arr) {
		Gson gson = new Gson();
		return gson.toJson(arr);
	}

	private void getDepositDetails(long synctime) throws Exception {

		List<Deposit> depositDetails = null;
		Gson gson = new Gson();
		List<String> depositAcNo = new ArrayList<String>();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_deposits_details);

			DepositDetailsRequest ldr = new DepositDetailsRequest();
			ldr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			ldr.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
			ldr.setBatchSize(CommonContexts.BATCHSIZE);
			ldr.setLocCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
			ldr.setRecvdDeposits(depositAcNo);
			ldr.setSyncSessionId(String.valueOf(synctime));
			try {
				JsonRequestWrapper jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(
						CommonContexts.CIPHER_DATAKEY, gson.toJson(ldr)));

				JsonResponseWrapper jsondepositResponse = getResponse(url,
						CONNECTION_POST, JsonResponseWrapper.class,
						gson.toJson(jrw));
				if (jsondepositResponse != null
						&& jsondepositResponse.getJsonString() != null) {
					DepositDetailsResponse depositResponse = gson.fromJson(
							DESedeEncryDecryption.decrypt(
									CommonContexts.CIPHER_DATAKEY,
									jsondepositResponse.getJsonString()),
							DepositDetailsResponse.class);
					if (depositResponse != null) {
						depositDetails = depositResponse.getDepositList();
					}
					if (depositDetails != null && depositDetails.size() > 0) {
						DataPorting.setDepositDetailsData(depositDetails);
						if (depositAcNo != null) {
							depositAcNo = null;
							depositAcNo = new ArrayList<String>();
						}
						for (Deposit ld : depositDetails) {
							depositAcNo.add(ld.getDepositAccNo());
						}
					}
				}
			} catch (DataAccessException e) {
				throw new DataAccessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage(), e);
			}

		} while (depositDetails != null && depositDetails.size() > 0);

	}

	private void getAgentAgenda() {
		List<AgentAgenda> agendaDetails = null;
		List<AgentAgendaUpdate> agendaUpdate = new ArrayList<AgentAgendaUpdate>();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_agenda_list);
			AgentAgendaRequest aar = new AgentAgendaRequest();
			aar.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			aar.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
			aar.setLocationCode(CommonContexts.mLOGINVALIDATION
					.getLocationCode());
			aar.setBatchSize(CommonContexts.BATCHSIZE);
			aar.setAgentAgendaUpdate(agendaUpdate);

			try {
				JsonRequestWrapper jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(
						CommonContexts.CIPHER_DATAKEY, new Gson().toJson(aar)));

				JsonResponseWrapper jsonagendaResponse = getResponse(url,
						CONNECTION_GET, JsonResponseWrapper.class,
						getAgendaString(jrw));
				if (jsonagendaResponse != null
						&& jsonagendaResponse.getJsonString() != null) {
					AgentAgendaResponse agendaResponse = new Gson().fromJson(
							DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jsonagendaResponse.getJsonString()),
							AgentAgendaResponse.class);
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

	private void deleteMaintanceData() {
		SyncDao dao = DaoFactory.getSyncDao();
		dao.deleteMaintainceData();
	}

	private void getCustomerDetails(long synctime) {

		List<CustomerDetail> custdetails = null;
		List<String> custmerid = new ArrayList<String>();
		Gson gson = new Gson();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_customer);
			CustomerDetailsRequest ldr = new CustomerDetailsRequest();

			ldr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			ldr.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
			ldr.setBatchSize(CommonContexts.BATCHSIZE);
			ldr.setLocCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
			ldr.setRecvdIds(custmerid);
			ldr.setSyncSessionId(String.valueOf(synctime));

			try {
				JsonRequestWrapper jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY,gson.toJson(ldr)));
				
				JsonResponseWrapper jsoncustResponse = getResponse(url, CONNECTION_GET,
					JsonResponseWrapper.class, gson.toJson(jrw));
				if(jsoncustResponse != null && jsoncustResponse.getJsonString() != null)
				{
					CustomerResponse custResponse = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jsoncustResponse.getJsonString()), CustomerResponse.class);
					if (custResponse != null) {
						custdetails = custResponse.getCustomerDetail();
					}
					if (custdetails != null && custdetails.size() > 0) {
						DataPorting.setCustomerData(custdetails);
						if (custmerid != null) {
							custmerid = null;
							custmerid = new ArrayList<String>();
						}
						for (CustomerDetail ld : custdetails) {
							custmerid.add(ld.getCustomerId());
						}
					}
				}
			} catch (DataAccessException e) {
				throw new DataAccessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage(), e);
			}
		} while (custdetails != null && custdetails.size() > 0);

	}

	private void getCustAccDetails(long synctime) {

		List<CustomerAccount> custAcc = null;
		Gson gson = new Gson();
		List<String> custmerid = new ArrayList<String>();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_customeracc);
			CustomerAccountRequest ldr = new CustomerAccountRequest();
			ldr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
			ldr.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
			ldr.setBatchSize(CommonContexts.BATCHSIZE);
			ldr.setLocCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
			ldr.setRcvCustAcc(custmerid);
			ldr.setSyncSessionId(String.valueOf(synctime));

			try {
				JsonRequestWrapper jrw = new JsonRequestWrapper();
				jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
				jrw.setJsonString(DESedeEncryDecryption.encrypt(
						CommonContexts.CIPHER_DATAKEY,gson .toJson(ldr)));

				JsonResponseWrapper jsoncustResponse = getResponse(url,
						CONNECTION_GET, JsonResponseWrapper.class,
						gson.toJson(jrw));
				if (jsoncustResponse != null
						&& jsoncustResponse.getJsonString() != null) {
					CustomerAccountResponse custResponse = gson.fromJson(
							DESedeEncryDecryption.decrypt(
									CommonContexts.CIPHER_DATAKEY,
									jsoncustResponse.getJsonString()),
							CustomerAccountResponse.class);
					if (custResponse != null) {
						custAcc = custResponse.getCustAccList();
					}
					if (custAcc != null && custAcc.size() > 0) {
						DataPorting.setCustomerAcc(custAcc);
						if (custmerid != null) {
							custmerid = null;
							custmerid = new ArrayList<String>();
						}
						for (CustomerAccount ld : custAcc) {
							custmerid.add(ld.getCustAcNo());
						}
					}
				}
			} catch (DataAccessException e) {
				throw new DataAccessException(e.getMessage(), e);
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage(), e);
			}
		} while (custAcc != null && custAcc.size() > 0);
	}

	private void getCashRecordDetails(long synctime) {

		List<CashRecord> cashData = null;
		Gson gson = new Gson();
		List<String> custmerid = new ArrayList<String>();
		do {
			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_cashRecords);
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

	private String handlePostEnrolmentRequest(List<String> enrIds,
			int initialSize) throws ServiceException {
		int initialbatchSize = initialSize;
		mPrgBrCustEnrol.setText("Construction of sync enrollment object");
		mPrgBrCustEnrol.incrementProgressBy(60);
		List<String> sublist = new ArrayList<String>();
		String statustext = null;
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_customerinfo);
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

			mPrgBrCustEnrol.setText("Sending transactions");
			mPrgBrCustEnrol.incrementProgressBy(80);

			if (SystemUtil.haveNetworkConnection(SyncViewL.this)) {
				try {
					Gson gson = GsonAdapter.getGson();
					JsonRequestWrapper jrw = new JsonRequestWrapper();
					jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
					jrw.setJsonString(DESedeEncryDecryption.encrypt(
							CommonContexts.CIPHER_DATAKEY,
							gson.toJson(ptrequest)));
					String response = RequestManager.transactionRequest(url,
							CONNECTION_POST, gson.toJson(jrw));
					JsonResponseWrapper jresw = gson.fromJson(response,
							JsonResponseWrapper.class);
					if (jresw != null && jresw.getJsonString() != null) {
						CustomerInfoResponse postTxnResponse = gson.fromJson(
								DESedeEncryDecryption.decrypt(
										CommonContexts.CIPHER_DATAKEY,
										jresw.getJsonString()),
								CustomerInfoResponse.class);
						if (postTxnResponse != null) {
							try {
								boolean status = postTxnResponse.getStatus();
								if (status) {
									statustext = postTxnResponse
											.getStatusText();
									mPrgBrCustEnrol
											.setText("Updating enrollment status");
									mPrgBrCustEnrol.incrementProgressBy(100);
									EnrolmentDataAccess
											.updateEnrolStatus(enrIds);
									customerDetailsList.clear();
								}
							} catch (DataAccessException e) {
								handleError(DATAFETCHING, e.getMessage());
							} catch (Exception e) {
								handleError(DATAFETCHING, e.getMessage());
							}
						}
					}
				} catch (Exception e) {
					handleError(DATAFETCHING, e.getMessage());
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
			cDetialsInfo.setTxnInitTime(enrolment.getTxnInitTime() != 0 ? Long
					.valueOf(enrolment.getTxnInitTime()) : enrolment
					.getTxnInitTime());
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

	private String handlePostBiometricRequest(List<String> enrIds,
			int initialbatchSize) throws ServiceException {

		mPrgBrCustEnrol.setText("Construction of sync enrollment object");
		mPrgBrCustEnrol.incrementProgressBy(60);
		List<String> sublist = new ArrayList<String>();
		String statustext = null;
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_biometric);
		List<CustomerBiometricDetails> customerDetailsList = new ArrayList<CustomerBiometricDetails>();

		// Need to get the count
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
					handleError(DATAFETCHING, e.getMessage());
				} catch (Exception e) {
					handleError(DATAFETCHING, e.getMessage());
				}
			}
		}
		return statustext;
	}

	private CustomerBiometricDetails generateBioObject(String id)
			throws DataAccessException {
		CustomerBiometricDetails enrolment = null;
		try {
			enrolment = EnrolmentDataAccess.readEnrolmentBioData(id);

		} catch (Exception e) {
			new DataAccessException(e.getMessage(), e);
		}

		return enrolment;
	}

	private String handlePostTransactionRequest(List<String> txnIds,
			int initialSize) throws ServiceException {
		mPrgBrTransactions.setText("Construction of sync transaction object");
		mPrgBrTransactions.incrementProgressBy(40);
		int initialbatchSize = initialSize;
		List<String> sublist = new ArrayList<String>();
		SyncDao syndao = DaoFactory.getSyncDao();
		String statustext = null;
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_postsync);
		List<TransactionRequest> transationrequest = new ArrayList<TransactionRequest>();
		int numTxns = txnIds.size();

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
			if (batchSize > 1)
				sublist = txnIds.subList(k, (k + batchSize));
			else
				sublist = txnIds;
			k += batchSize;
			for (String id : sublist) {
				TransactionRequest transactionRequest = generateRequestObject(
						syndao, id);
				transationrequest.add(transactionRequest);
			}
			mPrgBrTransactions.setText("Posting transactions");
			mPrgBrTransactions.incrementProgressBy(60);
			PostTransactionRequest ptrequest = new PostTransactionRequest();
			ptrequest.setTxnrequest(transationrequest);
			if (SystemUtil.haveNetworkConnection(SyncViewL.this)) {
				try {
					Gson gsonBuilder = GsonAdapter.getGson();
					JsonRequestWrapper jrw = new JsonRequestWrapper();
					jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
					jrw.setJsonString(DESedeEncryDecryption.encrypt(
							CommonContexts.CIPHER_DATAKEY,
							gsonBuilder.toJson(ptrequest)));

					String response = RequestManager.transactionRequest(url,
							CONNECTION_POST, gsonBuilder.toJson(jrw));
					JsonResponseWrapper jresw = gsonBuilder.fromJson(response,
							JsonResponseWrapper.class);

					if (jresw != null && jresw.getJsonString() != null) {
						PostTransactionResponse postTxnResponse = gsonBuilder
								.fromJson(DESedeEncryDecryption.decrypt(
										CommonContexts.CIPHER_DATAKEY,
										jresw.getJsonString()),
										PostTransactionResponse.class);
						if (postTxnResponse != null) {
							try {
								boolean status = postTxnResponse.getStatus();
								if (status) {
									statustext = postTxnResponse
											.getStatusText();
									mPrgBrTransactions
											.setText("Updating sync status");
									mPrgBrTransactions.incrementProgressBy(100);
									syndao.updateSyncStatus(transationrequest);
									transationrequest.clear();
								}
							} catch (DataAccessException e) {
								handleError(DATAFETCHING, e.getMessage());
							} catch (Exception e) {
								handleError(DATAFETCHING, e.getMessage());
							}
						}
					}
				} catch (Exception e) {

				}
			}
		}
		return statustext;
	}

	private TransactionRequest generateRequestObject(SyncDao syndao, String id) {
		TransactionRequest transactionRequest = new TransactionRequest();
		try {
			TxnMaster loantxn = syndao.readUnSyncTxns(id);
			transactionRequest.setTxnId(loantxn.getTxnId());
			transactionRequest.setModuleCode(loantxn.getModuleCode());
			transactionRequest.setTxnCode(loantxn.getTxnCode());
			transactionRequest.setAgendaId(loantxn.getAgendaId());
			transactionRequest.setSeqNo(loantxn.getSeqNo() != null ? Integer
					.valueOf(loantxn.getSeqNo()) : 0);
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
			transactionRequest.setTxnSettlementAmount(Double.valueOf(loantxn
					.getSettledAmt()));
			transactionRequest.setFullPartIndicator(loantxn.getFullPartInd());
			transactionRequest.setSyncType("F");

			transactionRequest
					.setDepNoOfInstalement(loantxn.getReqDepNoInst() != null ? Integer
							.parseInt(loantxn.getReqDepNoInst()) : 0);
			transactionRequest
					.setDepoRedeemReqDate(loantxn.getReqRedReqDt() != null ? Long
							.parseLong(loantxn.getReqRedReqDt()) : 0);
			transactionRequest.setIsFullRepoRedeem(loantxn
					.getReqRedReqFullPartInd());
			transactionRequest.setMaturityDate(loantxn.getReqMaturityDate());
			transactionRequest
					.setIntrRate(loantxn.getReqIntRate() != null ? Double
							.parseDouble(loantxn.getReqIntRate()) : 0);
			transactionRequest.setDepoTenureType(loantxn.getReqDpTenureType());
			transactionRequest.setDepoFreqType(loantxn.getReqDpFrequencyType());
			transactionRequest
					.setDepoFreq(loantxn.getReqDpFrequency() != null ? Integer
							.parseInt(loantxn.getReqDpFrequency()) : 0);
			transactionRequest
					.setDepoTenure(loantxn.getReqDpTenure() != null ? Integer
							.parseInt(loantxn.getReqDpTenure()) : 0);
			transactionRequest.setTxnErrCode(loantxn.getTxnErrorCode());
			transactionRequest.setTxnErrMsg(loantxn.getTxnErrorMsg());
			transactionRequest.setGeneratedSms(loantxn.getGeneratedSms());
			transactionRequest.setSmsMobNo(loantxn.getSmsMobileNo());
			transactionRequest.setSmsContent(loantxn.getSmsContent());
			transactionRequest.setGenerateReversal(loantxn.getGenerateRevr());
			transactionRequest
					.setMbsSeqNo(loantxn.getMbsSeqNo() != null ? Integer
							.parseInt(loantxn.getMbsSeqNo()) : 0);

			transactionRequest.setTxnCcy(loantxn.getCcyCode());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return transactionRequest;
	}

	private List<String> initiateTransaction() throws ServiceException {
		List<String> existingTxnId = null;
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_beginsync);
		SyncDataAccess sda = new SyncDataAccess();
		try {
			List<String> idList = sda.readUnSyncTxnId();
			if (idList != null && idList.size() > 0) {
				BeginTransactionRequest btr = new BeginTransactionRequest();
				btr.setSynsessionid(SyncUtil.getSessionIdRandom(SyncViewL.this));
				btr.setTxnid(idList);
				Gson gson = new Gson();
				String payload = gson.toJson(btr);
				String response = RequestManager.transactionRequest(url,
						CONNECTION_POST, payload);
				if (response != null) {
					BeginTransactionResponse css = gson.fromJson(response,
							BeginTransactionResponse.class);
					if (css.getAgentTransaction() != null) {
						existingTxnId = new ArrayList<String>();
						Set<String> keys = css.getAgentTransaction().keySet();
						Iterator<String> iterator = keys.iterator();
						while (iterator.hasNext()) {
							existingTxnId.add(iterator.next());
						}
						return existingTxnId;
					}
				}
			}
		} catch (DataAccessException e) {
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			throw new DataAccessException(e.getMessage(), e);
		}
		return existingTxnId;
	}

	private List<String> getUnSyncedTxnIds() {
		mPrgBrTxnPosting.setText("Fetching un read transactions");
		mPrgBrTxnPosting.incrementProgressBy(30);
		SyncDataAccess sda = new SyncDataAccess();
		List<String> allids = sda.readUnSyncTxnId();
		return allids;
	}

	private List<String> getUnSyncedEndIds() {
		mPrgBrCustEnrol.setText("Fetching unsync transactions ");
		mPrgBrCustEnrol.incrementProgressBy(40);
		SyncDataAccess sda = new SyncDataAccess();
		List<String> allids = sda.readUnSyncEndId();
		return allids;
	}

	private List<String> getUnSyncedBios() {
		mPrgBrCustEnrol.setText("Fetching unsync transactions ");
		mPrgBrCustEnrol.incrementProgressBy(40);
		SyncDataAccess sda = new SyncDataAccess();
		List<String> allids = sda.readUnSyncBios();
		return allids;
	}

	private void uploadFile() {
		mPrgBrAgtLogFile.incrementProgressBy(50);
		File dir = CommonContexts
				.getSdFileDirectory(CommonContexts.LOG_FILE_PATH);
		mPrgBrAgtLogFile.incrementProgressBy(70);
		FileUpload.uploadFile(dir + "/" + CommonContexts.LOG_FILE_NAME,
				CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
						+ getString(R.string.service_fileupload));
		mPrgBrAgtLogFile.incrementProgressBy(100);
	}

	private void handleError(String type, String message) {
		if (type.equalsIgnoreCase(SERVICE_EXCEPTION))
			mHandler.sendEmptyMessage(NETWORK_FAIL);
		else if (type.equalsIgnoreCase(DATAFETCHING))
			mHandler.sendEmptyMessage(DATABASE_FAIL);
		else if (type.equalsIgnoreCase(CONNECTION))
			mHandler.sendEmptyMessage(CONNECTION_FAIL);
		ErrorMessage = message;
	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();
	}

	public void myOnKeyDown() {
		startActivity(new Intent(SyncViewL.this, HomeView.class));
		finish();
	}

	private void enableDisableClick(boolean value) {
		btnsync.setClickable(value);
	}
}
