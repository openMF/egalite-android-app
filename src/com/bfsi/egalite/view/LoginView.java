package com.bfsi.egalite.view;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.dao.sqlite.DataPorting;
import com.bfsi.egalite.dao.sqlite.StaticDataAccessL;
import com.bfsi.egalite.encryption.AESDecrypt;
import com.bfsi.egalite.encryption.AESEncrypt;
import com.bfsi.egalite.encryption.DESedeEncryDecryption;
import com.bfsi.egalite.encryption.DESedeEncryDecryption.EncryptionException;
import com.bfsi.egalite.encryption.GenerateSalt;
import com.bfsi.egalite.encryption.PasswordHash;
import com.bfsi.egalite.entity.Agent;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.Parameters;
import com.bfsi.egalite.entity.PaswordEntity;
import com.bfsi.egalite.entity.ResultMessage;
import com.bfsi.egalite.entity.SyncCheck;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.servicerequest.RequestManager;
import com.bfsi.egalite.sync.GsonAdapter;
import com.bfsi.egalite.util.AppPref;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.SystemUtil;
import com.bfsi.egalite.view.setting.SyncViewL;
import com.bfsi.mfi.rest.constants.RestServiceConstants;
import com.bfsi.mfi.rest.model.AgentDetail;
import com.bfsi.mfi.rest.model.AgentRegistrationRequest;
import com.bfsi.mfi.rest.model.AgentRegistrationResponse;
import com.bfsi.mfi.rest.model.BaseRequest;
import com.bfsi.mfi.rest.model.DeviceDetail;
import com.bfsi.mfi.rest.model.JsonRequestWrapper;
import com.bfsi.mfi.rest.model.JsonResponseWrapper;
import com.bfsi.mfi.rest.model.ParametersResponse;
import com.bfsi.mfi.rest.model.StaticDataRequest;
import com.bfsi.mfi.rest.model.StaticDataResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.impress.api.Setup;

@SuppressLint("HandlerLeak")
public class LoginView extends Activity {
	private static final String INTERNAL_CIPHER_KEY = "egalitebfsiconsultinglts";
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private static final String CONNECTION = "connection";
	private static final String DATAFETCHING = "datafetching";
	private static final String SERVICE_EXCEPTION = "service";
	private static final String CONNECTION_POST = "POST";
	private static final String CONNECTION_GET = "GET";
	
	private final int NETWORK_FAIL = 2;
	private final int DATABASE_FAIL = 0;
	private final int CONNECTION_FAIL = 1;
	@SuppressWarnings("unused")
	private static int retryCount = 1;
	private ProgressDialog mProgressDialog;
	public static final int SUCCESS = 2014;
	private EditText edtUserId, edtPassword;
	// private Agent mAgent;
	private DeviceDetail mDevice;
	private Parameters mParameter;
	private long currtTimeMilsec, lastTimeMilsec, startTimeMilsec,
			endTimeMilsec;
	private String syncvalue, passwordtries;
	private Context mContext;
	private LinearLayout mCrutonLayout;
	private TextView mTxvErrorMsg;
	private static boolean isLibraryActivated = true;
	public static Setup bfsiSetUp;

	public final Handler mhandler = new Handler() {
		public void handleMessage(Message msg) {

			if (msg.what == SUCCESS) {
				mCrutonLayout.setVisibility(View.INVISIBLE);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		mContext = getApplicationContext();

		// evolute
		if(CommonContexts.USE_EXTERNAL_DEVICE)
		{
			if (isLibraryActivated) {
				try {
					bfsiSetUp = new Setup();
					bfsiSetUp.blActivateLibrary(this,R.raw.licence);
	
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				isLibraryActivated = false;
			}
	
			if (null == CommonContexts.mBT) {
				Toast.makeText(this, "Bluetooth module not found",
						Toast.LENGTH_LONG).show();
				this.finish();
			}
		}

		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00132));
			CommonContexts.CIPHER_DATAKEY = AppPref.getPref(LoginView.this, AppPref.CIPHER_KEY);
			if(CommonContexts.CIPHER_DATAKEY==null )
			{
				CommonContexts.CIPHER_DATAKEY = DESedeEncryDecryption.encrypt(RestServiceConstants.DEFAULT_ENC_KEY, INTERNAL_CIPHER_KEY);
			}
			// Fetch agentDetails, Device details and loadWidgets
			handleAgentAndDeviceAnddisplay();
			// Fetching parameters
			getParameterValues();	
			// cheking data in agent and device table 1st time
			dataCheck();
		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00132)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00132),
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00132)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00132),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Displaying all widgets
	 */
	private void handleAgentAndDeviceAnddisplay() {
		PreDataDao lgnDao = DaoFactory.getPreDataDao();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00127));
			CommonContexts.mLOGINVALIDATION = lgnDao.readAgentValues();
			mDevice = lgnDao.readDeviceValues();
			mCrutonLayout = (LinearLayout) findViewById(R.id.linlay_loginlayout);
			mTxvErrorMsg = (TextView) findViewById(R.id.txv_login_layout_error);
		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00127)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00127),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00127)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00127),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Getting UserLogin details from Userlogin table
	 */
	private Agent getUserLoginData() {
		PreDataDao lgnDao = DaoFactory.getPreDataDao();
		Agent login = null;
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00108));
			login = lgnDao.readUserLoginValues();
			if (login != null) {
				CommonContexts.mLOGINVALIDATION.setAgentId(login.getAgentId());
				CommonContexts.mLOGINVALIDATION.setLastLoginTime(login
						.getLastLoginTime());
				CommonContexts.mLOGINVALIDATION.setNoInvalidLogin(login
						.getNoInvalidLogin());
				CommonContexts.mLOGINVALIDATION.setLastSyncTime(login
						.getLastSyncTime());
				if (mDevice != null)
					CommonContexts.mLOGINVALIDATION
							.setDeviceId(mDevice.getId());
				return login;
			}
		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00108)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00108),
					Toast.LENGTH_SHORT).show();
			// StaticDataAccess sda = new StaticDataAccess();
			// String errorDesc = sda.getErrorMessage("");
			// throw new DataAccessException(errorDesc+" " + e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00108)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00108),
					Toast.LENGTH_SHORT).show();

		}
		return null;

	}

	/**
	 * Getting Parameter details from parameter table
	 */
	private void getParameterValues() {
		PreDataDao lgnDao = DaoFactory.getPreDataDao();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00132));
			mParameter = lgnDao.readParameterValues(AppParams.DAYS_SYNC);
			if (mParameter != null)
				syncvalue = mParameter.getValue();
			mParameter = lgnDao
					.readParameterValues(AppParams.MINIMUM_MOBILE_FAILED_LOGINS);
			if (mParameter != null)
				passwordtries = mParameter.getValue();

			mParameter = lgnDao
					.readParameterValues(AppParams.MINIMUM_MOBILE_LOGIN_PASSWORD_LENGTH);
		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00132)
							+ e.getMessage(), e);
			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00132),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00132)
							+ e.getMessage(), e);
			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00132),
					Toast.LENGTH_SHORT).show();
		}
	}

	// This should be commentted while enabling top method while giving the live
	// application

//	private void getParameterValues() {
//		PreDataDao lgnDao = DaoFactory.getPreDataDao();
//		try {
//			LOG.debug(mContext.getResources().getString(R.string.MFB00132));
//			LOG.debug("getParameterValues of LoginView ");
//			mParameter = lgnDao.readParameterValues("sync_days");
//			syncvalue = mParameter.getValue();
//			mParameter = lgnDao.readParameterValues("password");
//			passwordtries = mParameter.getValue();
//			mParameter = lgnDao.readParameterValues("password_days");
//			mParameter = lgnDao.readParameterValues("password_length");
//		} catch (DataAccessException e) {
//			LOG.error(
//					mContext.getResources().getString(R.string.MFB00132)
//							+ e.getMessage(), e);
//			Toast.makeText(mContext,
//					this.getResources().getString(R.string.MFB00132),
//					Toast.LENGTH_SHORT).show();
//		}
//	}

	/**
	 * cheking data in agent and device table 1st time
	 */
	private void dataCheck() {

		if (CommonContexts.mLOGINVALIDATION != null) {
			if (CommonContexts.mLOGINVALIDATION.getAgentId() != null
					&& CommonContexts.mLOGINVALIDATION.getPassword() != null) {
				setContentView(R.layout.login_layout);
				addListenerOnSignin();
			}
		} else if (CommonContexts.mLOGINVALIDATION == null)  {
			setContentView(R.layout.registration_layout);
			addListenerOnRegister();
		}
	}
	/**
	 * 
	 * Handles registration process on success registration,setPassword would be
	 * called. SMS through received values like of agentid, registration key,
	 * serverip will be set to commonContent values
	 */
	public void addListenerOnRegister() {

		SyncCheck sc = new SyncCheck();
		sc.setBatchId("1000");
		sc.setSu("N");
		sc.setDd("N");
		AppPref.updatePref(LoginView.this, AppPref.SYNCCHECK,
				new Gson().toJson(sc));

		Button signin = (Button) findViewById(R.id.btn_signup);
		Button login = (Button) findViewById(R.id.btn_login);
		signin.setTypeface(CommonContexts.getTypeface(LoginView.this));
		login.setTypeface(CommonContexts.getTypeface(LoginView.this));

		Button btnRegister = (Button) findViewById(R.id.btn_loginlay_register);
		mCrutonLayout = (LinearLayout) findViewById(R.id.linlay_loginlayout);
		mTxvErrorMsg = (TextView) findViewById(R.id.txv_login_layout_error);
		btnRegister.setTypeface(CommonContexts.getTypeface(LoginView.this));

		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				EditText edtAgentIdReg = (EditText) findViewById(R.id.edt_loginlay_agentid_reg);
				EditText edtRegistrationkeyReg = (EditText) findViewById(R.id.edt_loginlay_redistrationkey_reg);
				EditText edtIpaddressReg = (EditText) findViewById(R.id.edt_loginlay_ipaddress_reg);
				edtAgentIdReg.setTypeface(CommonContexts
						.getTypeface(LoginView.this));
				edtRegistrationkeyReg.setTypeface(CommonContexts
						.getTypeface(LoginView.this));
				edtRegistrationkeyReg.setTypeface(CommonContexts
						.getTypeface(LoginView.this));

				String enterusername = edtAgentIdReg.getText().toString();
				String enterpassword = edtRegistrationkeyReg.getText()
						.toString();
				String ipaddress = edtIpaddressReg.getText().toString();

				boolean connection = SystemUtil
						.haveNetworkConnection(LoginView.this);
				if (connection ) {
					if (enterusername.isEmpty() || enterpassword.isEmpty()
							|| CommonContexts.DEVICEID.isEmpty()
							|| ipaddress.isEmpty()) {
						showCrutonError(mContext, R.string.MFB00006, mhandler);
					} else {
						CommonContexts.AGENT_ID = enterusername;
						CommonContexts.REGISTkEY = enterpassword;
						CommonContexts.NONSERCURE_SERVERIP = ipaddress;
						new AsyncOperation().execute("");
					}
				} else {
					showCrutonError(mContext, R.string.MFB00003, mhandler);
				}
			}
		});
	}

	/**
	 * Signin button click listener
	 */
	public void addListenerOnSignin() {
		Button signin = (Button) findViewById(R.id.btn_signup);
		Button login = (Button) findViewById(R.id.btn_login);
		signin.setTypeface(CommonContexts.getTypeface(LoginView.this));
		login.setTypeface(CommonContexts.getTypeface(LoginView.this));
		Button btnLogin = (Button) findViewById(R.id.btn_loginlay_login);
		edtUserId = (EditText) findViewById(R.id.edt_loginlay_userid);
		edtPassword = (EditText) findViewById(R.id.edt_loginlay_password);
		mCrutonLayout = (LinearLayout) findViewById(R.id.linlay_loginlayout);
		mTxvErrorMsg = (TextView) findViewById(R.id.txv_login_layout_error);
		edtUserId.setTypeface(CommonContexts.getTypeface(LoginView.this));
		edtPassword.setTypeface(CommonContexts.getTypeface(LoginView.this));
		btnLogin.setTypeface(CommonContexts.getTypeface(LoginView.this));

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// startEndDate(); // calculation of days with Agent Start and
				
					agentValidation(); // Agent validation
//					CommonContexts.mLOGINVALIDATION.setAgentId("AGT0001");
//					startActivity(new Intent(LoginView.this,HomeView.class));
				

			}
		});
	}

	/**
	 * Agent validation against agent credentials and device table values
	 * 
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws NumberFormatException
	 */
	protected void agentValidation() {
		if (edtUserId.getText().toString().isEmpty()
				&& edtPassword.getText().toString().isEmpty()) {
			showCrutonError(mContext, R.string.MFB00006, mhandler);

		} else if (edtUserId.getText().toString().isEmpty()) {
			showCrutonError(mContext, R.string.MFB00007, mhandler);

		} else if (edtPassword.getText().toString().isEmpty()) {
			showCrutonError(mContext, R.string.MFB00008, mhandler);

		} else if ((!(edtUserId.getText().toString()
				.equalsIgnoreCase(CommonContexts.mLOGINVALIDATION.getUserName())))) {
			int invalidcount = AppPref.getPrefInt(LoginView.this,
					AppPref.INVALID_LOGIN) + 1;
			if (invalidcount == Integer.parseInt(passwordtries)) {
				AppPref.updatePref(LoginView.this, AppPref.INVALID_LOGIN, 0);
				showCrutonError(mContext, R.string.MFB000012, mhandler);
			} else
				showCrutonError(mContext, R.string.MFB00010, mhandler);
			AppPref.updatePref(LoginView.this, AppPref.INVALID_LOGIN,
					invalidcount);
		} else if (!(passwordValidation(edtPassword.getText().toString()))) {
			int invalidcount = AppPref.getPrefInt(LoginView.this,
					AppPref.INVALID_LOGIN) + 1;
			if (invalidcount == Integer.parseInt(passwordtries)) {
				AppPref.updatePref(LoginView.this, AppPref.INVALID_LOGIN, 0);
				showCrutonError(mContext, R.string.MFB000012, mhandler);
			} else
				showCrutonError(mContext, R.string.MFB00010, mhandler);
			AppPref.updatePref(LoginView.this, AppPref.INVALID_LOGIN,
					invalidcount);
		} else {
			if (startTimeMilsec <= currtTimeMilsec
					&& currtTimeMilsec <= endTimeMilsec) {
				CommonContexts.mLOGINVALIDATION.setAllowTransactions(true);
			} else {
				CommonContexts.mLOGINVALIDATION.setAllowTransactions(false);
			}
			deviceValidation();
		}
	}

	private boolean passwordValidation(String password) {
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00118));

			PreDataDao preDao = DaoFactory.getPreDataDao();
			Agent agent = preDao.readAgentValues();
			try {
				LOG.debug(mContext.getResources().getString(R.string.MFB00118));
				boolean validate = PasswordHash.validatePassword(password,
						agent.getPassword());
				return validate;
			} catch (NoSuchAlgorithmException e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00118)
						+ e.getMessage(), e);

				Toast.makeText(mContext,
						this.getResources().getString(R.string.MFB00118),
						Toast.LENGTH_SHORT).show();
			} catch (InvalidKeySpecException e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00118)
						+ e.getMessage(), e);

				Toast.makeText(mContext,
						this.getResources().getString(R.string.MFB00118),
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00118)
						+ e.getMessage(), e);

				Toast.makeText(mContext,
						this.getResources().getString(R.string.MFB00118),
						Toast.LENGTH_SHORT).show();
			}

		} catch (DataAccessException e) {

			LOG.error(
					mContext.getResources().getString(R.string.MFB00118)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00118),
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {

			LOG.error(
					mContext.getResources().getString(R.string.MFB00118)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					this.getResources().getString(R.string.MFB00118),
					Toast.LENGTH_SHORT).show();

		}

		return false;
	}

	/**
	 * Device validation against device table values
	 */
	protected void deviceValidation() {
		if (mDevice != null) {
			if (!(CommonContexts.DEVICEID.equalsIgnoreCase(mDevice
					.getUniqueId()))) {
				showCrutonError(mContext, R.string.MFB00004, mhandler);
			} else if (!(CommonContexts.mLOGINVALIDATION.getDeviceId()
					.equalsIgnoreCase(mDevice.getId()))) {
				showCrutonError(mContext, R.string.MFB00004, mhandler);
			} else {
				CommonContexts.mLOGINVALIDATION
						.setIMEI(CommonContexts.DEVICEID);
				CommonContexts.mLOGINVALIDATION.setSimNo(mDevice.getSimNumber());
				parameterValidation();
			}
		} else {
			AppPref.updatePref(LoginView.this, AppPref.INVALID_LOGIN, 0);
			Toast.makeText(LoginView.this, "Device are null. Reregister again",
					Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * Parameters validation against parameters table values
	 */
	protected void parameterValidation() {
		// Agent login details
		if (CommonContexts.mLOGINVALIDATION != null) {
			updateLoginData();
		} else {
			long lastLoginTime = DateUtil.getCurrentDataTime();
			int invalidLogin = AppPref.getPrefInt(LoginView.this,
					AppPref.INVALID_LOGIN);
			String lastSyncTime = null;
			PreDataDao predao = DaoFactory.getPreDataDao();
			predao.insertUserLoginData(CommonContexts.mLOGINVALIDATION.getAgentId(), lastLoginTime, invalidLogin,
					lastSyncTime);
		}
		if (AppPref.getPrefBoolean(LoginView.this, AppPref.ISDEBUGON))
			deBugDialog();
		if (AppPref.getPrefBoolean(LoginView.this, AppPref.ISFIRSTSYNC)) {
			edtUserId.setText("");
			edtPassword.setText("");
			startActivity(new Intent(LoginView.this, SyncViewL.class));
		} else {
			edtUserId.setText("");
			edtPassword.setText("");
			startActivity(new Intent(LoginView.this, HomeView.class));
		}
	}
	/**
	 * forget password
	 */
	protected void forgetPassword() {
		final Dialog dialog = new Dialog(LoginView.this);
		dialog.setContentView(R.layout.forgetpassword);
		dialog.setTitle(R.string.title_forget);
		final EditText edtagentid = (EditText) dialog
				.findViewById(R.id.edt_forgetpassword_agentid);
		final EditText edtregkey = (EditText) dialog
				.findViewById(R.id.edt_forgetpassword_reg_key);
		Button submit = (Button) dialog
				.findViewById(R.id.btn_forgetpassword_submit);
		final String agentid = CommonContexts.mLOGINVALIDATION.getAgentId();
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean connection = SystemUtil
						.haveNetworkConnection(LoginView.this);
				if (connection) {
					String edtanget = edtagentid.getText().toString();
					if (edtagentid.getText().toString().isEmpty()) {
						showCrutonError(mContext, R.string.messg_agentid,
								mhandler);
					} else if (edtregkey.getText().toString().isEmpty()) {
						showCrutonError(mContext, R.string.messg_registerkey,
								mhandler);
					} else if (!(edtanget.equalsIgnoreCase(agentid))) {
						showCrutonError(mContext, R.string.MFB00005, mhandler);
					} else {
						new forgotPasswordAsync().execute(edtagentid.getText()
								.toString(), edtregkey.getText().toString());
					}

				} else {
					showCrutonError(mContext, R.string.MFB00003, mhandler);
				}
			}
		});

		Button cancel = (Button) dialog
				.findViewById(R.id.btn_forgetpassword_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
		dialog.setCancelable(false);
	}

	private class forgotPasswordAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... details) {
			String statusText = null;
			try {
				LOG.debug(mContext.getResources().getString(R.string.MFB00119));

				String response = handleRequest(details[0], details[1]);
				AgentRegistrationResponse arr = GsonAdapter.getGson().fromJson(
						response, AgentRegistrationResponse.class);
				statusText = arr.getStatusText();

			} catch (Exception e) {

				LOG.error(mContext.getResources().getString(R.string.MFB00119)
						+ e.getMessage(), e);

				Toast.makeText(mContext,
						mContext.getResources().getString(R.string.MFB00119),
						Toast.LENGTH_SHORT).show();
			}

			return statusText;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null && result.equalsIgnoreCase("success")) {
				PaswordEntity passowrd = new PaswordEntity();
				passowrd.setAgentdetails(null);
				setPassword(passowrd);
			} else {
				Toast.makeText(LoginView.this, result, Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	private String handleRequest(String agentid, String regkey)
			throws ServiceException {
		String response = null;
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00120));

			String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
					+ getString(R.string.service_forgetkey);
			AgentRegistrationRequest arr = new AgentRegistrationRequest();
			arr.setAgentId(agentid);
			arr.setRegistKey(regkey);
			arr.setDeviceId(CommonContexts.DEVICEID);
			response = RequestManager.initiateRequest(url, CONNECTION_POST,
					getJsonString(arr));
		} catch (ServiceException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00120)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00120),
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00120)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00120),
					Toast.LENGTH_SHORT).show();
		}
		return response;
	}

	/**
	 * calculation of days with Agent Start and end date to validate current
	 * date
	 */
	/*
	 * protected void startEndDate() {
	 * 
	 * try { LOG.debug("start end date"); currtTimeMilsec =
	 * CommonContexts.NETWORKTIMESTAMP; String startDateString = mAgent.get;
	 * Date mDates = CommonContexts.dateFormat.parse(startDateString);
	 * startTimeMilsec = mDates.getTime(); String endDateString =
	 * mAgent.getAgentEndDate(); Date mDatess =
	 * CommonContexts.dateFormat.parse(endDateString); endTimeMilsec =
	 * mDatess.getTime(); } catch (ParseException e) {
	 * handleError(SERVICE_EXCEPTION);
	 * LOG.error("Error-in start end date  "+e.getMessage()); throw new
	 * DataAccessException("Error-in start end date  "+" " + e.getMessage(), e);
	 * 
	 * // StaticDataAccess sda = new StaticDataAccess(); // String errorDesc =
	 * sda.getErrorMessage(""); // throw new DataAccessException(errorDesc+" " +
	 * e.getMessage(), e); }catch (Exception e) {
	 * handleError(SERVICE_EXCEPTION);
	 * LOG.error("Error-in start end date  "+e.getMessage()); throw new
	 * DataAccessException("Error-in start end date  "+" " + e.getMessage(), e);
	 * 
	 * // StaticDataAccess sda = new StaticDataAccess(); // String errorDesc =
	 * sda.getErrorMessage(""); // throw new DataAccessException(errorDesc+" " +
	 * e.getMessage(), e); } }
	 */

	/**
	 * calculation number of sync days
	 */
	// private int syncDaysCalculation() {
	// int days = 0;
	// try {
	// LOG.debug(mContext.getResources().getString(R.string.MFB00121));
	// LOG.debug("syncDaysCalculation");
	// currtTimeMilsec = CommonContexts.NETWORKTIMESTAMP;
	// long lastDateString = mDevice.getLastSync();
	// lastTimeMilsec = lastDateString;
	// } catch (Exception e) {
	// LOG.error(
	// mContext.getResources().getString(R.string.MFB00121)
	// + e.getMessage(), e);
	//
	// Toast.makeText(mContext,
	// mContext.getResources().getString(R.string.MFB00121),
	// Toast.LENGTH_SHORT).show();
	//
	// }
	// long final_time_milsec = currtTimeMilsec - lastTimeMilsec;
	// days = (int) (final_time_milsec / (1000 * 60 * 60 * 24));
	// return days;
	// }

	/**
	 * Update userlogin values
	 */
	private void updateLoginData() {

		PreDataDao lgnDao = DaoFactory.getPreDataDao();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00122));
			LOG.debug("updateLoginData");
			lgnDao.updateUserLogin(
					CommonContexts.mLOGINVALIDATION.getAgentId(),
					DateUtil.getCurrentDataTime(),
					AppPref.getPrefInt(LoginView.this, AppPref.INVALID_LOGIN));
		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00122)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00122),
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00122)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00122),
					Toast.LENGTH_SHORT).show();
		}
	}

	// private void syncAlert() {
	// AlertDialog.Builder builder = new AlertDialog.Builder(this);
	// builder.setTitle(R.string.MFB00002);
	// builder.setMessage(R.string.MFB000011);
	// builder.setPositiveButton(getString(R.string.ok),
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// clearFields();
	// if (AppPref.getPrefBoolean(LoginView.this, AppPref.ISDEBUGON))
	// deBugDialog();
	//
	// if (AppPref.getPrefBoolean(LoginView.this, AppPref.ISFIRSTSYNC))
	// startActivity(new Intent(LoginView.this, SyncViewL.class));
	// else
	// startActivity(new Intent(LoginView.this, HomeView.class));
	// }
	// });
	// AlertDialog alert = builder.create();
	// alert.show();
	// }

	private void deBugDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.MFB00001);
		builder.setMessage(R.string.debug_alert);
		builder.setPositiveButton(getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// clearing usename and passward
	public void clearFields() {
		edtUserId.setText("");
		edtPassword.setText("");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			doExit();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void doExit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				LoginView.this);

		alertDialog.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		alertDialog.setNegativeButton(R.string.no, null);
		alertDialog.setMessage(R.string.exit);
		alertDialog.setTitle(R.string.exit_title);
		alertDialog.show();
	}

	/**
	 * Implimentation of set password screen
	 */
	private void setPassword(final PaswordEntity passwordentity) {
		ContextThemeWrapper ctw = new ContextThemeWrapper(LoginView.this,
				R.style.AlertDialog);
		final Dialog dialog = new Dialog(ctw);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setTitle(R.string.set_title);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.alertdialog);

		final LinearLayout lv = (LinearLayout) dialog
				.findViewById(R.id.linearLayout1);
		lv.setPadding(10, 10, 10, 10);
		final EditText pass = (EditText) dialog
				.findViewById(R.id.edt_setpassword_password);
		final EditText cnfpass = (EditText) dialog
				.findViewById(R.id.edt_setpassword_cnfpassword);
		pass.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		cnfpass.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		pass.setHint(R.string.set_password);
		pass.setPadding(10, 10, 10, 10);
		cnfpass.setHint(R.string.set_cnf_password);
		cnfpass.setPadding(10, 10, 10, 10);
		int minimumLength = 0, maximumLength = 0;
		PreDataDao lgnDao = DaoFactory.getPreDataDao();
		Parameters parameter = lgnDao
				.readParameterValues(AppParams.MINIMUM_PASSWORD_LENGTH);

		if (parameter != null)
			minimumLength = Integer.parseInt(parameter.getValue());

		Parameters parameters = lgnDao
				.readParameterValues(AppParams.MAXIMUM_MOBILE_LOGIN_PASSWORD_LENGTH);

		if (parameters != null)
			maximumLength = Integer.parseInt(parameters.getValue());

		final TextView text1 = (TextView) dialog
				.findViewById(R.id.txv_maximum_password);
		text1.setText(getResources().getString(R.string.maxpassword) + " "
				+ maximumLength);
		text1.setPadding(10, 10, 10, 10);
		final TextView text2 = (TextView) dialog
				.findViewById(R.id.txv_minimum_password);
		text2.setText(getResources().getString(R.string.minpassword) + " "
				+ minimumLength);
		text2.setPadding(10, 5, 10, 10);

		final TextView text3 = (TextView) dialog
				.findViewById(R.id.txv_alphanumeric);
		text3.setText(getResources().getString(R.string.alphanumeric));
		text3.setPadding(10, 5, 10, 10);

		Button ok = (Button) dialog.findViewById(R.id.btn_setpassword_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				boolean connection = SystemUtil
						.haveNetworkConnection(LoginView.this);
				if (connection) {
					int minimumLength = 0, maximumLength = 0;
					PreDataDao lgnDao = DaoFactory.getPreDataDao();
					Parameters parameter = lgnDao
							.readParameterValues(AppParams.MINIMUM_PASSWORD_LENGTH);
					if (parameter != null)
						minimumLength = Integer.parseInt(parameter.getValue());
					Parameters parameters = lgnDao
							.readParameterValues(AppParams.MAXIMUM_MOBILE_LOGIN_PASSWORD_LENGTH);
					if (parameters != null)
						maximumLength = Integer.parseInt(parameters.getValue());

					if (pass.getText().toString().isEmpty()) {
						showCrutonError(mContext, R.string.messg_set_password,
								mhandler);
					} else if (cnfpass.getText().toString().isEmpty()) {
						showCrutonError(mContext,
								R.string.messg_set_cnfmpassword, mhandler);
					} else if (pass.getText().toString().length() != cnfpass
							.getText().toString().length()) {
						showCrutonError(mContext,
								R.string.messg_set_chekpassword, mhandler);
					} else if (pass.getText().toString().length() < minimumLength) {
						showCrutonError(mContext,
								R.string.messg_set_paswordlength, mhandler);
					} else if (pass.getText().toString().length() > maximumLength) {
						showCrutonError(mContext,
								R.string.messg_set_paswordlength, mhandler);
					} else if (!(pass.getText().toString().equals(cnfpass
							.getText().toString()))) {
						showCrutonError(mContext,
								R.string.messg_set_chekpassword, mhandler);
					} else {
						// populate Agent, Device and parameters tables
						if (passwordentity.getAgentdetails() != null) {
							try {
								LOG.debug(mContext.getResources().getString(
										R.string.MFB00107));
								byte[] salt = GenerateSalt.getSalt();
								String password = PasswordHash.createHash(pass
										.getText().toString(), salt);
//								AESEncrypt aesEncrypt = new AESEncrypt(password);
//								String dataKey = aesEncrypt
//										.encrypt(passwordentity.getDatakey());

								DataPorting.setAgentData(
										passwordentity.getAgentdetails(),
										password, passwordentity.getDatakey(), salt);
								DataPorting.setDeviceData(passwordentity
										.getDevicedetails());

								AppPref.updatePref(LoginView.this,
										AppPref.ISFIRSTSYNC, true);
								AppPref.updatePref(LoginView.this,
										AppPref.INVALID_LOGIN, 0);
								AppPref.updatePref(LoginView.this,
										AppPref.CIPHER_KEY,passwordentity.getDatakey());
							} catch (Exception e) {
								LOG.error(
										mContext.getResources().getString(
												R.string.MFB00107)
												+ e.getMessage(), e);

								Toast.makeText(
										mContext,
										mContext.getResources().getString(
												R.string.MFB00107),
										Toast.LENGTH_SHORT).show();

							}
						} else {
							try {
								LOG.debug(mContext.getResources().getString(
										R.string.MFB00107));

								// read password, datakey from agent table
								PreDataDao preDataDao = DaoFactory
										.getPreDataDao();
								Agent agent = preDataDao.readAgentValues();
								String datakey = agent.getDataKey();
								String oldpassword = agent.getPassword();

								// decrypt the datakey with old password
								AESDecrypt aesDecrypt = new AESDecrypt(
										oldpassword, datakey);
								String plainDataKey = aesDecrypt.decrypt();

								// genrate salt and hash the new password
								byte[] salt = GenerateSalt.getSalt();
								String password = PasswordHash.createHash(pass
										.getText().toString(), salt);

								// encrypt the datakey with new passwordhash
								AESEncrypt aesEncrypt = new AESEncrypt(password);
								String dataKey = aesEncrypt
										.encrypt(plainDataKey);

								// update the agent table with new values
								preDataDao.updateAgentsPassword(
										salt,
										password,
										dataKey,
										CommonContexts.dateFormat
												.format(CommonContexts.NETWORKTIMESTAMP));
								AppPref.updatePref(LoginView.this,
										AppPref.INVALID_LOGIN, 0);
							} catch (Exception e) {
								LOG.error(
										mContext.getResources().getString(
												R.string.MFB00107)
												+ e.getMessage(), e);

								Toast.makeText(
										mContext,
										mContext.getResources().getString(
												R.string.MFB00107),
										Toast.LENGTH_SHORT).show();

							}
						}

						restrat();
					}
				} else {

					showCrutonError(mContext, R.string.MFB00003, mhandler);
				}
			}
		});
		dialog.show();
		dialog.setCancelable(false);
	}

	/**
	 * Implimentation of cruton error
	 */
	public void showCrutonError(final Context context, int messg,
			final Handler handler) {
		Animation animFadein;
		int TIMER_DELAY = 3000;

		animFadein = AnimationUtils
				.loadAnimation(context, R.anim.bottom_to_top);
		mCrutonLayout.setVisibility(View.VISIBLE);
		mCrutonLayout.setBackgroundResource(R.color.red);
		mTxvErrorMsg.setText(messg);
		mCrutonLayout.setAnimation(animFadein);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(SUCCESS);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, TIMER_DELAY);
	}

	public boolean registrationConfirmation() throws ServiceException {
		boolean status = false;
		AgentRegistrationResponse arr = null;
		BaseRequest br = new BaseRequest();
		br.setAgentId(CommonContexts.AGENT_ID);
		
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_registrationconfirmation);
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00123));
			LOG.debug("getting registration confirmation  ");
			JsonRequestWrapper jrw = new JsonRequestWrapper();
			jrw.setAgentId(CommonContexts.AGENT_ID);
			jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, new Gson().toJson(br)));
			String response = RequestManager
					.initiateRequest(url, CONNECTION_POST, new Gson().toJson(jrw));
			if (response != null) {
				Gson gson = new Gson();
				JsonResponseWrapper jresw = gson.fromJson(response, JsonResponseWrapper.class);
				if(jresw != null && jresw.getJsonString()!= null)
					try{
						arr = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jresw.getJsonString()),AgentRegistrationResponse.class);
						if (arr.getStatus())
						status = arr.getStatus();
					}catch(Exception e)
					{
						System.out.println(e.getMessage());
					}

			}

		} catch (ServiceException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00123)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00123),
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00123)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00123),
					Toast.LENGTH_SHORT).show();

		}
		return status;
	}

	private AgentRegistrationResponse parseRegistrationObject(String response) {
		Gson gson = new Gson();
		AgentRegistrationResponse arr = null;

		JsonResponseWrapper jrw = gson.fromJson(response,
				JsonResponseWrapper.class);
		try {
			if (jrw != null) {
				arr = gson.fromJson(
						DESedeEncryDecryption.decrypt(INTERNAL_CIPHER_KEY,
								jrw.getJsonString()),
						AgentRegistrationResponse.class);
			}
		} catch (Exception e) {

		}
		return arr;
	}

	private StaticDataResponse parseStaticObject(String response) {
		Gson gson = new Gson();
		StaticDataResponse arr = null;
		JsonResponseWrapper jrw = gson.fromJson(response, JsonResponseWrapper.class);
		try{
			if(jrw != null && jrw.getJsonString() != null)
			{
				arr = gson.fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY,jrw.getJsonString()),StaticDataResponse.class);
			}
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return arr;
	}

	private AgentRegistrationResponse handleRegistration() throws Exception {
		AgentRegistrationRequest arr = generateObject();
		AgentRegistrationResponse arrs = null;
		// Need to take ip address provided by server and append to all
		// services.
		JsonRequestWrapper jrw = new JsonRequestWrapper();
		jrw.setDataKey(CommonContexts.CIPHER_DATAKEY);
		jrw.setJsonString(DESedeEncryDecryption.encrypt(INTERNAL_CIPHER_KEY, getJsonString(arr)));
		
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_registration);

		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00124));
			String response = RequestManager.initiateRequest(url,
					CONNECTION_POST, getJsonString(jrw));
			if (response != null) {
				arrs = parseRegistrationObject(response);

			}
		} catch (ServiceException e) {
			System.out.println("Exception null object --------------:" + e);
			if (e != null) {
				if (e.getMessage() != null) {
					if (e.getMessage().contains("Hostname")) {
						String response = RequestManager.initiateRequest(url,
								CONNECTION_POST, getJsonString(jrw));
						if (response != null) {
							arrs = parseRegistrationObject(response);

						}
					} else {
						LOG.error(
								mContext.getResources().getString(
										R.string.MFB00124)
										+ e.getMessage(), e);

						Toast.makeText(
								mContext,
								mContext.getResources().getString(
										R.string.MFB00124), Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					LOG.error(
							mContext.getResources()
									.getString(R.string.MFB00124)
									+ e.getMessage(), e);

					Toast.makeText(
							mContext,
							mContext.getResources()
									.getString(R.string.MFB00124),
							Toast.LENGTH_SHORT).show();
				}
			}

		}
		return arrs;

	}

	private StaticDataResponse handleStatic() throws Exception {
		StaticDataRequest arr = generatStaticeObject();
		StaticDataResponse arrs = null;
		
		JsonRequestWrapper jrw = new JsonRequestWrapper();
		jrw.setAgentId(CommonContexts.AGENT_ID);
		jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, getJsonString(arr)));
		
		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_static);

		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00112));
			String response = RequestManager.transactionRequest(url,
					CONNECTION_POST, getJsonString(jrw));
			if (response != null) {
				arrs = parseStaticObject(response);

			}
		} catch (ServiceException e) {

			LOG.error(
					mContext.getResources().getString(R.string.MFB00112)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00112),
					Toast.LENGTH_SHORT).show();

		}
		return arrs;

	}

	private String getJsonString(AgentRegistrationRequest arr) {
		Gson gson = new Gson();
		return gson.toJson(arr);
	}
	private String getJsonString(JsonRequestWrapper jrw) {
		Gson gson = new Gson();
		return gson.toJson(jrw);
	}

	private String getJsonString(StaticDataRequest arr) {
		Gson gson = new Gson();
		return gson.toJson(arr);
	}

	private AgentRegistrationRequest generateObject() {
		AgentRegistrationRequest arr = new AgentRegistrationRequest();
		arr.setAgentId(CommonContexts.AGENT_ID);
		arr.setDeviceId(CommonContexts.DEVICEID);
		arr.setRegistKey(CommonContexts.REGISTkEY);
		return arr;
	}

	private StaticDataRequest generatStaticeObject() {
		StaticDataRequest arr = new StaticDataRequest();
		arr.setAgentId(CommonContexts.AGENT_ID);
		arr.setDeviceId(CommonContexts.DEVICEID);
		return arr;
	}

	private class AsyncOperation extends AsyncTask<String, Void, PaswordEntity> {

		@Override
		protected void onPostExecute(PaswordEntity passwordEntity) {

			dismissProgressDialog();
			// launching password screen to enter values;
			if (passwordEntity != null) {
				if (passwordEntity.getAgentdetails() != null)
					setPassword(passwordEntity);
				else {
					StaticDataAccessL sda = new StaticDataAccessL();
					String errorDesc = sda.getErrorMessage(passwordEntity
							.getResultMessage().getMessageCode());
					Toast.makeText(mContext, errorDesc, Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				if (mProgressDialog != null && mProgressDialog.isShowing())
					mProgressDialog.dismiss();
				Toast.makeText(LoginView.this,
						"Registration failed, please try again later!!",
						Toast.LENGTH_SHORT).show();
			}

		}

		protected PaswordEntity doInBackground(String... arg0) {

			try {
				LOG.debug(mContext.getResources().getString(R.string.MFB00125));

				// initiate registration
				PaswordEntity passwordEntity = new PaswordEntity();

				ParametersResponse paramet = getSysParameters();
				DataPorting.setParamData(paramet);

				PreDataDao lgnDao = DaoFactory.getPreDataDao();
				Parameters hostname = lgnDao
						.readParameterValues(AppParams.HOST_NAME);
				if (hostname != null)
					CommonContexts.SERVERIP = hostname.getValue();

				AgentRegistrationResponse arrs = handleRegistration();
				if (arrs.getDataKey() != null) {

					// registration confirmation call
					CommonContexts.CIPHER_DATAKEY = arrs.getDataKey();

					boolean status = registrationConfirmation();

					// call for agent, device, other static content
					StaticDataResponse staticDataDetails = handleStatic();
					AgentDetail agentDetails = staticDataDetails
							.getAgentDetail();
					DeviceDetail deviceDetails = staticDataDetails
							.getDevicedetail();

					// form password entity
					passwordEntity.setAgentdetails(agentDetails);
					passwordEntity.setDevicedetails(deviceDetails);
					passwordEntity.setDatakey(arrs.getDataKey());

					// confirming registration progress
					

					if (status)
						
						return passwordEntity;
				} else if (!arrs.getStatus()) {
					ResultMessage resultMessage = new ResultMessage();
					resultMessage.setMessageCode(arrs.getMessageCode());
					resultMessage.setStatusCode(arrs.getCode());
					passwordEntity.setResultMessage(resultMessage);
					return passwordEntity;
				}

			} catch (ServiceException e) {
				dismissProgressDialog();
				LOG.error(mContext.getResources().getString(R.string.MFB00125)
						+ e.getMessage(), e);

				Toast.makeText(mContext,
						mContext.getResources().getString(R.string.MFB00125),
						Toast.LENGTH_SHORT).show();
				retryCount++;

			} catch (DataAccessException e) {
				dismissProgressDialog();
				LOG.error(mContext.getResources().getString(R.string.MFB00125)
						+ e.getMessage(), e);

				retryCount++;
			} catch (Exception e) {
				dismissProgressDialog();
				LOG.error(mContext.getResources().getString(R.string.MFB00125)
						+ e.getMessage(), e);

				retryCount++;
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			showProgress(LoginView.this, "Registering");
		}
	}

	@SuppressWarnings("unused")
	private StaticDataResponse getAgentDetails() throws ServiceException {
		StaticDataResponse staticdataResponse = null;

		String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
				+ getString(R.string.service_static);

		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00107));
			StaticDataResponse agentResponse = getResponse(url, CONNECTION_GET,
					StaticDataResponse.class);
			if (agentResponse != null) {
				boolean status = agentResponse.getStatus();
				if (status) {

					return staticdataResponse;
				}
			}
		} catch (DataAccessException e) {
			// throw new DataAccessException(e.getMessage(), e);
			LOG.error(
					mContext.getResources().getString(R.string.MFB00107)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00107),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// throw new DataAccessException(e.getMessage(), e);
			LOG.error(
					mContext.getResources().getString(R.string.MFB00107)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00107),
					Toast.LENGTH_SHORT).show();
		}
		return staticdataResponse;
	}

	// private DeviceDetail getDeviceDetails() throws ServiceException {
	// DeviceDetail devicedetails = null;
	// String url = CommonContexts.BASE_URL_TYPE + CommonContexts.SERVERIP
	// + getString(R.string.service_devicedetails);
	// DeviceResponse deviceresponse = getResponse(url, CONNECTION_GET,
	// DeviceResponse.class);
	// if (deviceresponse != null) {
	// if (deviceresponse.getStatus()) {
	// devicedetails = deviceresponse.getDevicedetail();
	// return devicedetails;
	// } else {
	// throw new ServiceException(deviceresponse.getStatusText());
	// }
	// }
	// return devicedetails;
	// }

	private <T> T getResponse(String url, String method, Class<T> clazz) {
		T responseObject = null;
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00128));
			String response = RequestManager.handleReadData(url, method);
			Gson gson = GsonAdapter.getGson();
			responseObject = gson.fromJson(response, clazz);
			return responseObject;
		} catch (ServiceException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00128)
							+ e.getMessage(), e);
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00128),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00128)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00128),
					Toast.LENGTH_SHORT).show();
		}
		return responseObject;
	}

//	private <T> T getSysResponse(String url, String method, Class<T> clazz) {
//		T responseObject = null;
//		try {
//			LOG.debug(mContext.getResources().getString(R.string.MFB00129));
//
//			String response = RequestManager.getSysRequest(url, method, clazz);
//			Gson gson = GsonAdapter.getGson();
//			responseObject = gson.fromJson(response, clazz);
//			return responseObject;
//
//		} catch (ServiceException e) {
//			LOG.error(
//					mContext.getResources().getString(R.string.MFB00129)
//							+ e.getMessage(), e);
//
//			Toast.makeText(mContext,
//					this.getResources().getString(R.string.MFB00129),
//					Toast.LENGTH_SHORT).show();
//		} catch (Exception e) {
//			LOG.error(
//					mContext.getResources().getString(R.string.MFB00129)
//							+ e.getMessage(), e);
//
//			Toast.makeText(mContext,
//					this.getResources().getString(R.string.MFB00129),
//					Toast.LENGTH_SHORT).show();
//		}
//		return responseObject;
//	}

	private ParametersResponse getSysParameters() throws ServiceException {
		ParametersResponse parameters = null;
		JsonResponseWrapper jrq = null;
		String url = CommonContexts.BASE_URL_TYPE_NON
				+ CommonContexts.NONSERCURE_SERVERIP
				+ getString(R.string.service_parameters);
		JsonRequestWrapper jrw = new JsonRequestWrapper();
		jrw.setDataKey(CommonContexts.CIPHER_DATAKEY); // cipherKey = 123456789123456789123456 
		jrw.setAgentId(CommonContexts.AGENT_ID);
		try {
			String response = RequestManager.getSysRequest(url,
					CONNECTION_POST, new Gson().toJson(jrw));
			if (response != null) {
				jrq = parseResponseWraper(response);
			}
			if (jrq != null) {
				parameters = new Gson().fromJson(DESedeEncryDecryption.decrypt(
						INTERNAL_CIPHER_KEY, jrq.getJsonString()),
						ParametersResponse.class);
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (EncryptionException e) {

			e.printStackTrace();
		}catch(ServiceException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return parameters;
	}
	private JsonResponseWrapper parseResponseWraper(String response) {
		Gson gson = new Gson();
		JsonResponseWrapper arr = gson.fromJson(response,
				JsonResponseWrapper.class);
		return arr;
	}
	private void restrat() {
		Intent intent = this.getIntent();
		startActivity(intent);
		finish();
	}

	@SuppressWarnings("unused")
	private void handleError(String type) {
		if (type.equalsIgnoreCase(SERVICE_EXCEPTION))
			mhandler.sendEmptyMessage(NETWORK_FAIL);
		else if (type.equalsIgnoreCase(DATAFETCHING))
			mhandler.sendEmptyMessage(DATABASE_FAIL);
		else if (type.equalsIgnoreCase(CONNECTION))
			mhandler.sendEmptyMessage(CONNECTION_FAIL);
	}

	public void showProgress(Context context, String message) {
		if (mProgressDialog != null && !mProgressDialog.isShowing()) {
			return;
		}
		mProgressDialog = ProgressDialog.show(context, null, message, true,
				false);
	}

	private void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
	}

	@Override
	protected void onDestroy() {
		if (CommonContexts.mMfi != null) {
			CommonContexts.mMfi.closeConn();
			CommonContexts.BAUDRATE = "LOW";
			CommonContexts.CHECK = 0;
		}
		super.onDestroy();
	}
}
