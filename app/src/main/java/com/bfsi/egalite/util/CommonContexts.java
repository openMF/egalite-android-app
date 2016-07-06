package com.bfsi.egalite.util;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bfsi.egalite.adapters.GroupLoanAdapter;
import com.bfsi.egalite.adapters.PaidShedulAdapter;
import com.bfsi.egalite.dao.CashPositionDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.LoansDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.Agent;
import com.bfsi.egalite.entity.AgnDepositDetail;
import com.bfsi.egalite.entity.AgnLoanDetail;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.entity.GroupLoans;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.view.MfiApplication;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CustomerBiometricDetails;
import com.bfsi.mfi.rest.model.LoanPaidSch;

/**
 * Holds all application related context specific data
 * 
 * @author Vijay
 * 
 */
@SuppressLint("SimpleDateFormat")
public class CommonContexts {


	private static int THEMES;
	public final static int THEME_LIGHT = 1;
	public final static int THEME_DARK = 2;
	public static final int SWIPE_MIN_DISTANCE = 120;
	public static final int SWIPE_MAX_OFF_PATH = 250;
	public static final int SWIPE_THRESHOLD_VELOCITY = 200;
	public static  String CIPHER_DATAKEY = "egalitebfsiconsultinglts";
	public static String AGENT_ID = "AGT001";
	public static String REGISTkEY = "";
	public static String SERVERIP = "";
	public static int NOOFPRINTS = 0;
	public static String APP_TODAY = "";
	public static String ALLOW_PARTIAL = "Y";
	public static int PARTIAL_PERCENT = 0;
	public static boolean USE_EXTERNAL_DEVICE = false;
	public static int BATCHSIZE;
	public static String NONSERCURE_SERVERIP = "";
	public static String BASE_URL_TYPE = "https://";
	public static String BASE_URL_TYPE_NON = "http://";
	public static String SERVER_HTTP_PORT = "9090";
	public static String SSL_CERT_PATH = "/resources/sslkeystore.pem";
	public static boolean fromSubViews = false;
	public static final int SUCCESS = 2014;
	public static String CURRENT_VIEW = "";
	public static String DBPATHMAIN = null;
	public static long NETWORKTIMESTAMP = 0;
	public static AgendaMaster SELECTED_DSB;
	public static AgendaMaster SELECTED_CLC;
	public static AgendaMaster SELECTED_MATURITY;
	public static Requests SELECTED_REQUEST;
	public static AgendaMaster SELECTED_REPAY;
	public static AgendaMaster SELECTED_LNPREPAY;
	public static AgendaMaster SELECTED_CUST_AGENDA;
	public static CustomerDetail SELECTED_CUSTOMER;
	public static CustomerDetail SELECTED_CASH_ACCOUNT;
	public static Agent mLOGINVALIDATION;
	public static Enrolement SELECTED_ENROLEMENT;
	public static CustomerBiometricDetails SELECTED_BIOMETRIC;

	public static String THEME = "LIGHT";
	public static String LANG = "EN";
	public static String TAG = "NEWREQUEST";
	public static String MODULE = null;
	public static int WIDTH = 0;
	public static int EQUALWIDTH = 0;
	public static ProgressDialog mProgressDialog;
	public static String DEVICEID;
	public static String CURRENT_SCREEN = "";
	public static final String SCREEN_CUSTOMER = "CUSTOMER";
	public static String TXNID = null;
	public static String BAUDRATE="LOW";
	
	public static final String SCREEN_DSB_AGENDA = "DSBAGENDA";
	public static final String SCREEN_DSB_ENTRY = "DSBENTRY";

	public static final String SCREEN_RP_AGENDA = "RPAGENDA";
	public static final String SCREEN_RP_ENTRY = "RPENTRY";

	public static final String SCREEN_CL_AGENDA = "CLAGENDA";
	public static final String SCREEN_CL_ENTRY = "CLENTRY";

	public static final String SCREEN_PM_AGENDA = "PMAGENDA";
	public static final String SCREEN_PM_ENTRY = "PMENTRY";

	public static final String SCREEN_RDRQ_ENTRY = "RDRQENTRY";
	public static final String SCREEN_NEWRQ_ENTRY = "NEWRQENTRY";

	public static final String SCREEN_PREPAYRQ_ENTRY = "PREPAYRAENTRY";
	public static final String SCREEN_PREPAY_ENTRY = "LOANPREPAYMENTENTRY";

	public static final String SCREEN_lOAN_PREPAYAGENDA = "LOANPREPAYAGENDA";
	public static final String SCREEN_lOAN_PREPAYENTRY = "LOANPREPAYENTRY";

	public static final String SCREEN_CUST_AGENDA = "CUSTOMER AGENDA";
	public static final String SCREEN_CUST_ENTRY = "CUSTOMER ENTRY";

	public static final String SCREEN_BIOMETRIC_KYC = "BIOMETRIC_KYC";
	public static final String SCREEN_BIOMETRIC = "BIOMETRIC";
	public static final String SCREEN_NEW = "NEW";
	public static final String SCREEN_KYC = "KYC";
	public static final String SCREEN_STATUS = "STATUS";
	public static final String SCREEN_ENROLL_DETAILS = "DETAILS";

	public static final String CUST_REQUEST_TYPE = "CUSTOMERTYPE";
	public static final String SCREEN_CASHDEPO_ENTRY = "CASHDEPOSITENTRY";
	public static final String SCREEN_CASHWITH_ENTRY = "CASHWITHDRAWALENTRY";
	public static final String SCREEN_CASH_DEPOSIT_ENTRY = "CASHDEPOSITENTRY";

	public static final String SCREEN_CUSTDETAILS = "CUSTDETAILS";

	public static boolean ISLOANPREPAYSELECTED = false;
	public static boolean ISDPPREPAYSELECTED = false;
	public static boolean ISDPRDSELECTED = false;
	public static boolean ISDPNWSELECTED = false;
	public static boolean ISCASHSELECTED = false;
	public static boolean ISCUSTREQUEST = false;
	public static boolean ISCUSTAGENDA = false;
	public static boolean ISREPAYVISITED = false;
	public static boolean ISENROLVISITED = false;
	public static boolean ISKYCVIEW = false;
	public static boolean ISDBVISITED = false;
	public static boolean ISCOLLECTIONVISITED = false;
	public static boolean ISPAYMENTVISITED = false;
	public static boolean ISPREPAYVISITED = false;
	public static String SELECTEDPROOFTYPE = null;
	public static String SELECTEDPROOFTYPE2 = null;
	public static String SELECTEDPROOFTYPE3 = null;
	public static String CURRENT_SPINNER_VALUE = "";
	public static String CASHCHECK = null;
	public static String HISTORYCHECK = "HISTORY";
	public static String GROUP = "INDVI";
	public static String poc = null;

	public static SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm:ss");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd-MM-yyyy");
	public static SimpleDateFormat endateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat dateFormatMonth = new SimpleDateFormat(
			"dd-MMM-yyyy");
	public static SimpleDateFormat dateMonthYear = new SimpleDateFormat(
			"dd/MM/yy");

	public static String TRANSACTION_FILE_DIRECTORY = "/TXNS/FILES";
	public static String IMAGE_FILE_DIRECTORY = "/TXNS/IMAGES";
	public static String LOG_FILE_NAME = "log.txt";
	public static boolean DEBUG_STATUS = false;

	public static String LOG_FILE_PATH = "/LOGS";
	public static LinearLayout mLinLay;
	public static View mView;

	// Evolute
	public static int CHECK = 0;
	public static MfiApplication mMfi = null;
	public static BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
	public static BluetoothDevice mBDevice = null;
	public static boolean mbBonded = false;
	public static Hashtable<String, String> mhtDeviceInfo = new Hashtable<String, String>();
	public static byte[] imageData, templeteData = null;

	public static BroadcastReceiver _mPairingRequest = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			BluetoothDevice device = null;
			if (intent.getAction().equals(
					BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
				device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() == BluetoothDevice.BOND_BONDED)
					mbBonded = true;
				else
					mbBonded = false;
			}
		}
	};

	public static void setThemes(final int position, View convertView) {
		if (CommonContexts.THEME.equals("LIGHT")) {
			if (position % 2 == 0) {
				convertView
						.setBackgroundResource(R.drawable.list_row_colorlight);
			} else {
				convertView
						.setBackgroundResource(R.drawable.list_row_otr_colorlight);
			}
		} else {

			if (position % 2 == 0) {
				convertView
						.setBackgroundResource(R.drawable.list_row_colordark);
			} else {
				convertView
						.setBackgroundResource(R.drawable.list_row_otr_colordark);
			}
		}
	}

	/**
	 * get Root directory in SD card
	 * 
	 * @param directory
	 * @return
	 */
	public static File getSdFileDirectory(String directory) {
		File root = android.os.Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + "/MFI" + directory);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * Set the theme of the Activity, and restart it by creating a new Activity
	 * of the same type.
	 */
	public static void changeToTheme(Activity activity, int theme) {
		THEMES = theme;
		activity.finish();

		activity.startActivity(new Intent(activity, activity.getClass()));
	}

	/** Set the theme of the activity, according to the configuration. */
	public static void onActivityCreateSetTheme(Activity activity) {
		switch (THEMES) {
		default:

		case THEME_LIGHT:
			activity.setTheme(R.style.Theme_Light);
			break;
		case THEME_DARK:
			activity.setTheme(R.style.Theme_Dark);
			break;
		}
	}

	/**
	 * shows progress indicator for given context and with message
	 * 
	 * @param context
	 * @param message
	 */
	public static void showProgress(Context context, String message) {
		if (mProgressDialog != null && !mProgressDialog.isShowing()) {
			return;
		}
		mProgressDialog = ProgressDialog.show(context, null, message, true,
				false);
	}

	public static void dismissProgressDialog() {
		if (mProgressDialog != null) {
			if (mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
		}
	}

	public static void deviceDimension(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		int width = metrics.widthPixels;
		@SuppressWarnings("unused")
		int height = metrics.heightPixels;
		CommonContexts.WIDTH = (width - 20) / 3;
		CommonContexts.EQUALWIDTH = (width - 20) / 2;

	}

	public static Typeface getTypeface(Context context) {
		Typeface tf = null;
		if (CommonContexts.LANG.equalsIgnoreCase("EN")
				|| CommonContexts.LANG.equalsIgnoreCase("TAGLAG")) {
			tf = Typeface.createFromAsset(context.getAssets(),
					"Roboto-Regular.ttf");
		} else if (CommonContexts.LANG.equalsIgnoreCase("KHM")) {
			tf = Typeface.createFromAsset(context.getAssets(), "KhmerUI.ttf");
		} else {
			tf = Typeface.createFromAsset(context.getAssets(),
					"Roboto-Regular.ttf");
		}
		return tf;
	}

	public static void showCustDetailsDialog(Context context,
			List<CustomerDetail> mList, LayoutInflater layInf) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.CustomerDetails);
		mView = layInf.inflate(R.layout.custdetailspopup, null);
		builder.setView(mView);
		TextView mName = (TextView) mView.findViewById(R.id.txv_custall_name);
		TextView mDob = (TextView) mView.findViewById(R.id.txv_custall_dob);
		TextView mPhNo = (TextView) mView
				.findViewById(R.id.txv_custall_phoneno);
		TextView mCustId = (TextView) mView
				.findViewById(R.id.txv_custall_custid);
		mName.setText(mList.get(0).getCustomerFullName());
		mDob.setText(CommonContexts.dateFormat.format(new Date(Long
				.valueOf(mList.get(0).getDob()))));
		mPhNo.setText(mList.get(0).getMobileNumber());
		mCustId.setText(mList.get(0).getCustomerId());
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}

				});
		builder.show();

	}

	public static void showLoanDialog(Context context, AgnLoanDetail mLoans,
			LayoutInflater layInf) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.loandetails);
		mView = layInf.inflate(R.layout.loanpopup, null);
		builder.setView(mView);
		TextView mName = (TextView) mView.findViewById(R.id.txv_loanpopup_name);
		TextView mCustId = (TextView) mView
				.findViewById(R.id.txv_loanpopup_custid);
		TextView mLoanAccno = (TextView) mView
				.findViewById(R.id.txv_loanpopup_loanacc);
		TextView mDsbPrinAmt = (TextView) mView
				.findViewById(R.id.txv_loanpopup_disbprinc);
		TextView mOutPrinc = (TextView) mView
				.findViewById(R.id.txv_loanpopup_outprincipal);
		if (mLoans != null) {
			mName.setText(mLoans.getCustomerName() == null ? " " : mLoans
					.getCustomerName());
			mCustId.setText(mLoans.getCustomerId() == null ? " " : mLoans
					.getCustomerId());
			mLoanAccno.setText(mLoans.getLoanAcNo() == null ? " " : mLoans
					.getLoanAcNo());
			mDsbPrinAmt.setText(mLoans.getDisbursedPrincipalAmt() == null ? " "
					: mLoans.getDisbursedPrincipalAmt());
			mOutPrinc.setText(mLoans.getPrincipalOutstanding() == null ? " "
					: mLoans.getPrincipalOutstanding());
		}
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}

				});
		builder.show();

	}

	public static void showDepositDialog(Context context,
			AgnDepositDetail agnDepositDetail, LayoutInflater layInf) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.depositdetails);
		mView = layInf.inflate(R.layout.depositpopup, null);
		builder.setView(mView);
		TextView mName = (TextView) mView.findViewById(R.id.txv_depopopup_name);
		TextView mCustId = (TextView) mView
				.findViewById(R.id.txv_depopopup_custid);
		TextView mDepoAccno = (TextView) mView
				.findViewById(R.id.txv_depopopup_depoacc);
		TextView mPrinMatAmt = (TextView) mView
				.findViewById(R.id.txv_depopopup_pric_mat_amt);
		TextView mInstTillDt = (TextView) mView
				.findViewById(R.id.txv_depopopup_inst_paidtilldt);
		TextView mTotalInstAmtDue = (TextView) mView
				.findViewById(R.id.txv_depopopup_total_inst_amt_due);
		if (agnDepositDetail != null) {
			mName.setText(agnDepositDetail.getCustomerName() == null ? " "
					: agnDepositDetail.getCustomerName());
			mCustId.setText(agnDepositDetail.getCustomerId() == null ? " "
					: agnDepositDetail.getCustomerId());
			mDepoAccno.setText(agnDepositDetail.getDepAcNo() == null ? " "
					: agnDepositDetail.getDepAcNo());
			mPrinMatAmt
					.setText(agnDepositDetail.getPrincipalMaturityAmount() == null ? " "
							: agnDepositDetail.getPrincipalMaturityAmount());
			mInstTillDt
					.setText(agnDepositDetail.getInstallmentPaidTillDate() == null ? " "
							: agnDepositDetail.getInstallmentPaidTillDate());
			mTotalInstAmtDue.setText(agnDepositDetail
					.getTotalInsatllmentAmtDue() == null ? " "
					: agnDepositDetail.getTotalInsatllmentAmtDue());
		}
		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}

				});
		builder.show();

	}

	public static void showPaidShedulsDialog(Context context,
			List<LoanPaidSch> mlist, LayoutInflater layInf) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.paidsheduls);
		mView = layInf.inflate(R.layout.loanpaidsheduls, null);
		builder.setView(mView);
		ListView mListView = (ListView) mView
				.findViewById(R.id.list_paidshedul);
		PaidShedulAdapter paidAdpters = new PaidShedulAdapter(context, mlist);
		mListView.setAdapter(paidAdpters);

		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}

				});
		builder.show();

	}

	public static void showGroupLoanialog(Context context,
			List<GroupLoans> mlist, LayoutInflater layInf) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.grouploan);
		mView = layInf.inflate(R.layout.grouploan, null);
		builder.setView(mView);
		ListView mListView = (ListView) mView.findViewById(R.id.list_grouploan);
		GroupLoanAdapter groupAdpters = new GroupLoanAdapter(context, mlist);
		mListView.setAdapter(groupAdpters);

		builder.setPositiveButton(R.string.close,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {

					}

				});
		builder.show();

	}

	public static CashPosition cashData(String ccy, String agentId)
			throws ServiceException {
		CashPositionDao dsbDao = DaoFactory.getCashDao();
		CashPosition mCcyData = null;

		try {
			mCcyData = dsbDao.readCashPosition(ccy, agentId);

		} catch (Exception e) {
			throw new ServiceException(Constants.ERR_READCURRENTDATA
					+ e.getMessage(), e);

		}
		return mCcyData;
	}

	public static List<CustomerDetail> CustomerDetailList()
			throws ServiceException {
		LoansDao rpyDao = DaoFactory.getLoanDao();
		List<CustomerDetail> listDetails = new ArrayList<CustomerDetail>();
		try {
			listDetails = rpyDao.readCustomerDetails();
		} catch (Exception e) {
			throw new ServiceException(Constants.ERR_READCUST_DELT
					+ e.getMessage(), e);
		}
		return listDetails;

	}

	public static Double getPartialAmount(String txnAmount) {
		Double returnValue = 0.0;
		int percent = CommonContexts.PARTIAL_PERCENT;
		returnValue = (percent * Double.parseDouble(txnAmount)) / 100;
		return returnValue;
	}

	public static void ShowAlertDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.partial_settlement);
		builder.setMessage(R.string.partial_settlementmsg);
		builder.setCancelable(false);
		builder.setNeutralButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert11 = builder.create();
		alert11.show();
	}
	public static String keyGenrate()
	{
		String datakey = null;
		
		KeyGenerator kgen;
		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(256);
			SecretKey skey = kgen.generateKey();
			byte[] raw = skey.getEncoded();
			datakey = Base64.encodeToString(raw, false);


		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return datakey;
		
	}

}
