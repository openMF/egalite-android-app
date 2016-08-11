package com.bfsi.egalite.view.cash;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.SyncDao;
import com.bfsi.egalite.dao.TransactionDao;
import com.bfsi.egalite.encryption.DESedeEncryDecryption;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.evolute.PrinterActivity;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.TabPageIndicator;
import com.bfsi.egalite.service.ServerCallback;
import com.bfsi.egalite.service.ServiceFactory;
import com.bfsi.egalite.service.TransactionService;
import com.bfsi.egalite.service.impl.BaseTransactionService;
import com.bfsi.egalite.servicerequest.AsyncOperation;
import com.bfsi.egalite.util.CashPopUp;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.MessageBuilderUtil;
import com.bfsi.egalite.util.NumberToWords;
import com.bfsi.egalite.util.PopUpActivity;
import com.bfsi.egalite.util.SystemUtil;
import com.bfsi.egalite.util.ViewUtil;
import com.bfsi.egalite.view.MfiApplication;
import com.bfsi.egalite.view.R;
import com.bfsi.egalite.view.customers.CustomerView;
import com.bfsi.mfi.rest.model.CashRecord;
import com.bfsi.mfi.rest.model.CashTransactionRequest;
import com.bfsi.mfi.rest.model.CashTransactionResponse;
import com.bfsi.mfi.rest.model.JsonRequestWrapper;
import com.bfsi.mfi.rest.model.JsonResponseWrapper;
import com.google.gson.Gson;

@SuppressLint("HandlerLeak")
public class CashView extends BaseActivity implements ServerCallback {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private View mView;
	private static final String VIEW_STATE_VERIFY = "cashverify";
	private static final String VIEW_STATE_ENTRY = "cashentry";
	private static final String VIEW_STATE_Confirm = "cashconfirm";
	private static TextView txtAccNo, txtCurrency, txtCustName, txtAcType,
			txtCustID, txtAgtBal;
	private static String userValue, Narrative, mbsTxnId, curBal;
	private static EditText edtDepoAmt, edtWithAmt, edtNarrative;
	private Context mContext;
	private RadioButton mRadioDeposit, mRadioWithdrawal;
	private OnClickListener listener;
	private LinearLayout mLinDeposit, mLinWithdrawal, mLinName;
	private BfsiViewPager mPager;
	private TabPageIndicator mPageIndicator;
	private TableLayout mTableLay;
	private RelativeLayout mRelLay;
	private List<CustomerDetail> mCustDetailList;
	private CustomerDetail printCashDetails;

	public CashView() {
	}

	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		CommonContexts.mMfi = ((MfiApplication) getApplicationContext());
		CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_CASHDEPO_ENTRY;
		mTitle.setText(R.string.screen_cashentry);
		onSaveView();
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	private void onSaveView() {
		curBal = "1000";
		CommonContexts.CURRENT_VIEW = VIEW_STATE_ENTRY;
		mView = getLayoutInflater().inflate(R.layout.cash_entry, null);
		onSaveLoadWidgets();
		setContentValues();
	}

	private void onSaveLoadWidgets() {

		txtCustName = (TextView) mView.findViewById(R.id.txv_cash_ent_custname);
		txtAccNo = (TextView) mView.findViewById(R.id.txv_cash_ent_accno);
		txtCurrency = (TextView) mView.findViewById(R.id.txv_cash_ent_currency);
		txtCustID = (TextView) mView.findViewById(R.id.txv_cash_entry_custid);
		txtAcType = (TextView) mView.findViewById(R.id.txv_cash_ent_acctype);
		txtAgtBal = (TextView) mView.findViewById(R.id.txv_cash_ent_agtbal);
		// txtAccountBal = (TextView) mView
		// .findViewById(R.id.txv_cash_ent_acc_bal);
		edtDepoAmt = (EditText) mView.findViewById(R.id.edt_cash_ent_depo_amt);
		edtWithAmt = (EditText) mView.findViewById(R.id.edt_cash_ent_with_amt);
		edtNarrative = (EditText) mView
				.findViewById(R.id.edt_cash_ent_narrative);
		mLinDeposit = (LinearLayout) mView.findViewById(R.id.linlay_deposit);
		mLinWithdrawal = (LinearLayout) mView
				.findViewById(R.id.linlay_withdrawal);
		popUpLpg();
		LinearLayout linlay = (LinearLayout) mView
				.findViewById(R.id.linlay_cash);

		linlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCustDetails();

				Intent intcash = new Intent(new Intent(CashView.this,
						PopUpActivity.class));
				Bundle bundle = new Bundle();
				bundle.putString(Constants.NAME, mCustDetailList.get(0)
						.getCustomerFullName());
				bundle.putString(Constants.DOB, CommonContexts.dateFormat
						.format(new Date(Long.valueOf(mCustDetailList.get(0)
								.getDob()))));
				bundle.putString(Constants.PHONE, mCustDetailList.get(0)
						.getMobileNumber());
				bundle.putString(Constants.CUSTID, mCustDetailList.get(0)
						.getCustomerId());
				intcash.putExtras(bundle);
				startActivity(intcash);

			}
		});
		LinearLayout mlinNarr = (LinearLayout) mView
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}
		mTableLay = (TableLayout) mView.findViewById(R.id.tablay_cash);
		mLinName = (LinearLayout) mView.findViewById(R.id.linlay_cash);
		mRelLay = (RelativeLayout) mView
				.findViewById(R.id.rellay_cash_entry_lpg);
		mRadioDeposit = (RadioButton) mView.findViewById(R.id.radio_deposit);
		mRadioWithdrawal = (RadioButton) mView
				.findViewById(R.id.radio_withdrawal);
		mTableLay.setVisibility(View.GONE);
		mLinName.setVisibility(View.GONE);
		mRelLay.setVisibility(View.GONE);
		radioButtonEvent();
		if (CommonContexts.CASHCHECK != null)
			if (CommonContexts.CASHCHECK.equalsIgnoreCase(Constants.DEPOSIT)) {
				mTableLay.setVisibility(View.VISIBLE);
				mLinName.setVisibility(View.VISIBLE);
				mRelLay.setVisibility(View.VISIBLE);
				mRadioDeposit.setChecked(true);

			} else if (CommonContexts.CASHCHECK
					.equalsIgnoreCase(Constants.WITHDRAWAL)) {
				mTableLay.setVisibility(View.VISIBLE);
				mLinName.setVisibility(View.VISIBLE);
				mRelLay.setVisibility(View.VISIBLE);
				mRadioWithdrawal.setChecked(true);

			} else {
				mTableLay.setVisibility(View.GONE);
				mLinName.setVisibility(View.GONE);
				mRelLay.setVisibility(View.GONE);
			}

		mRadioDeposit.setOnClickListener(listener);
		mRadioWithdrawal.setOnClickListener(listener);

	}

	private void getCustDetails() {
		CustomerDao clcDao = DaoFactory.getCustomerDao();
		try {
			mCustDetailList = clcDao
					.readCustInfo(CommonContexts.SELECTED_CASH_ACCOUNT
							.getCustomerId());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void popUpLpg() {
		LinearLayout mLinCash = (LinearLayout) mView
				.findViewById(R.id.linlay_entry_cash);
		mLinCash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(CashView.this, CashPopUp.class));
			}
		});

	}

	private void radioButtonEvent() {
		listener = new OnClickListener() {

			@Override
			public void onClick(View view) {

				boolean checked = ((RadioButton) view).isChecked();
				switch (view.getId()) {
				case R.id.radio_deposit:
					if (checked)
						mLinWithdrawal.setVisibility(View.GONE);
					mLinDeposit.setVisibility(View.VISIBLE);
					mRadioDeposit.setChecked(true);
					mRadioWithdrawal.setChecked(false);
					mTableLay.setVisibility(View.VISIBLE);
					mLinName.setVisibility(View.VISIBLE);
					mRelLay.setVisibility(View.VISIBLE);
					CommonContexts.CASHCHECK = "DEPOSIT";
					break;

				case R.id.radio_withdrawal:
					if (checked)
						mLinDeposit.setVisibility(View.GONE);
					mLinWithdrawal.setVisibility(View.VISIBLE);
					mRadioWithdrawal.setChecked(true);
					mRadioDeposit.setChecked(false);
					mTableLay.setVisibility(View.VISIBLE);
					mLinName.setVisibility(View.VISIBLE);
					mRelLay.setVisibility(View.VISIBLE);
					CommonContexts.CASHCHECK = Constants.WITHDRAWAL;
					break;

				}

				if (CommonContexts.CASHCHECK.equals(Constants.DEPOSIT)) {
					mRadioDeposit.setChecked(true);
					mRadioWithdrawal.setChecked(false);

				}
				if (CommonContexts.CASHCHECK.equals(Constants.WITHDRAWAL)) {
					mRadioWithdrawal.setChecked(true);
					mRadioDeposit.setChecked(false);

				}

			}
		};

	}

	private CashRecord cashRecord() {
		CustomerDetail cash = CommonContexts.SELECTED_CASH_ACCOUNT;

		CashRecord cashRec = new CashRecord();
		cashRec.setAuthStatus("U");
		cashRec.setEntrySeqNo(BaseTransactionService
				.generateEntrySequence(mContext));
		cashRec.setCashTxnId(CommonContexts.TXNID);
		cashRec.setCashTxnSeqNo(1);
		cashRec.setTxnSource("M");
		cashRec.setTxnDateTime(DateUtil
				.stringToMillisecondsss(CommonContexts.dateMonthYear
						.format(DateUtil.getCurrentDataTime())));
		cashRec.setTxnCode(CommonContexts.CASHCHECK.equals("DEPOSIT") ? "C21"
				: "C22");
		cashRec.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		cashRec.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		cashRec.setTxnCcy(cash.getAccCcy());
		cashRec.setAmount(Double.parseDouble(cash.getTxnAmt()));
		cashRec.setIsReversal("N");
		cashRec.setIsDeleted("N");
		return cashRec;
	}

	private void setContentValues() {
		CustomerDetail cd = CommonContexts.SELECTED_CASH_ACCOUNT;
		CashPosition cashDetail = null;
		try {
			cashDetail = CommonContexts.cashData(cd.getAccCcy(),
					CommonContexts.mLOGINVALIDATION.getAgentId());
		} catch (ServiceException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00106)
							+ e.getMessage(), e);
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00106),
					Toast.LENGTH_SHORT).show();
		}
		String agendaBal = String.valueOf(cashDetail.getOpeningBal()
				+ cashDetail.getTopUp() + cashDetail.getCreditAmt()
				- (cashDetail.getDebitAmt() + cashDetail.getTopDown()));
		CommonContexts.SELECTED_CASH_ACCOUNT.setAgentBal(agendaBal);
		txtCustName.setText(cd.getCustomerFullName());
		txtCurrency.setText(cd.getAccCcy());
		txtAccNo.setText(cd.getAcNo());
		txtAcType.setText(cd.getAccType().equalsIgnoreCase("S") ? "Savings"
				: "Current");
		txtCustID.setText(cd.getCustomerId());
		txtAgtBal.setText(cd.getAgentBal());
		// txtAccountBal.setText(accountBal);
		edtDepoAmt.setText("");
		edtWithAmt.setText("");
		edtNarrative.setText("");
	}

	@Override
	protected void onRightAction() {
		Object tags = BaseActivity.mBtnRight.getTag();
		int ids = tags == null ? -1 : (Integer) tags;
		if (ids == R.drawable.save) {
			handleSaveForVerification();
		} else if (ids == R.drawable.verify) {
			handleCashConfirmation();

		} else if (ids == R.drawable.print) {
			int noOfPrint = CommonContexts.NOOFPRINTS == 0?1:CommonContexts.NOOFPRINTS;
			PrinterActivity pa = new PrinterActivity();
			pa.printCash(noOfPrint,Constants.CASH, printCashDetails, mContext);
			
			Intent detailIntent = new Intent(CashView.this, CustomerView.class);
			startActivity(detailIntent);
			finish();
			clearFields();
		}
	}

	@Override
	protected void onLeftAction() {
		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_CASHDEPO_ENTRY)) {
			if (id == R.drawable.back) {
				handleBackToEntryView();
			} else if (id == R.drawable.cancel) {
				Intent intent = new Intent(mContext, CustomerView.class);
				startActivity(intent);
				finish();
				clearFields();
			} else if (id == R.drawable.imgsearch) {
				Intent intent = new Intent(mContext, CustomerView.class);
				startActivity(intent);
				finish();
				clearFields();
			}
		}
	}

	private void handleBackToEntryView() {
		BaseActivity.mTitle.setText(R.string.screen_cashentry);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		// Narrative = edtNarrative.getText().toString();
		onSaveView();
		if (CommonContexts.CASHCHECK.equals(Constants.DEPOSIT)) {
			edtDepoAmt
					.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getTxnAmt());
			userValue = edtDepoAmt.getText().toString();
			mLinDeposit.setVisibility(View.VISIBLE);
			mLinWithdrawal.setVisibility(View.GONE);
		} else {
			edtWithAmt
					.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getTxnAmt());
			userValue = edtWithAmt.getText().toString();
			mLinDeposit.setVisibility(View.GONE);
			mLinWithdrawal.setVisibility(View.VISIBLE);
		}
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	public static void clearFields() {
		txtAccNo.setText("");
		txtCurrency.setText("");
		// txtAccountBal.setText("");
		edtNarrative.setText("");
		edtDepoAmt.setText("");
		edtWithAmt.setText("");

	}

	private void handleSaveForVerification() {
		String narative = null, clc_amt = null;
		if (CommonContexts.CASHCHECK != null) {
			if (CommonContexts.CASHCHECK.equals(Constants.DEPOSIT)) {
				if (edtDepoAmt.getText() != null
						&& !(edtDepoAmt.getText().equals(""))) {
					clc_amt = edtDepoAmt.getText().toString();
				}
				if (clc_amt != null && !(clc_amt.equals(""))) {

					if (Double.parseDouble(clc_amt) > 0) {

						CommonContexts.SELECTED_CASH_ACCOUNT.setTxnAmt(clc_amt);
						CommonContexts.SELECTED_CASH_ACCOUNT
								.setNarrative(edtNarrative.getText() != null ? edtNarrative
										.getText().toString() : "");
						
						//EGA-MN15-000011
						String value = String.valueOf(Double.parseDouble(edtDepoAmt.getText().toString()));
						NumberToWords.speakText(value, BaseActivity.ttsp);
						
						onVerifyView();
						CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
					} else
						ViewUtil.showCrutonError(mContext, R.string.MFB00034,
								handler);

				} else {
					ViewUtil.showCrutonError(mContext, R.string.MFB00014,
							handler);
				}
			} else {
				if (edtWithAmt.getText() != null
						&& !(edtWithAmt.getText().equals(""))) {
					clc_amt = edtWithAmt.getText().toString();
				}
				if (clc_amt != null && !(clc_amt.equals(""))) {
					if(txtAgtBal.getText().toString()!=null && Double.parseDouble(txtAgtBal.getText().toString())>=Double.parseDouble(clc_amt)){
						if (Double.parseDouble(clc_amt) > 0) {
							CommonContexts.SELECTED_CASH_ACCOUNT.setTxnAmt(clc_amt);
							CommonContexts.SELECTED_CASH_ACCOUNT
									.setNarrative(edtNarrative.getText() != null ? edtNarrative
											.getText().toString() : "");
							
							//EGA-MN15-000011
							String value = String.valueOf(Double.parseDouble(edtWithAmt.getText().toString()));
							NumberToWords.speakText(value, BaseActivity.ttsp);
							
							onVerifyView();
							CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
						} else
							ViewUtil.showCrutonError(mContext, R.string.MFB00034,
									handler);
				}else 
					ViewUtil.showCrutonError(mContext, R.string.MFB00036,
						handler);
				} else {
					ViewUtil.showCrutonError(mContext, R.string.MFB00014,
							handler);
				}
			}
		}
	}

	private void onVerifyView() {
		BaseActivity.mTitle.setText(R.string.screen_cashverify);
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		// load views and assignment to fields
		onVerifyLoadWidgets();

	}

	// private void handleConfirm() {
	//
	// mbsTxnId = "WD121281093209371";
	//
	// onConfirmValues();
	// onConfirmView();
	// CommonContexts.CURRENT_VIEW = VIEW_STATE_Confirm;
	// Toast.makeText(mContext, getResources().getString(R.string.MFB00017),
	// Toast.LENGTH_SHORT).show();
	// }

	// private Requests onConfirmValues() {
	// Requests clcTxn = new Requests();
	// clcTxn.setCustomerName(txtCustName.getText() != null ? txtCustName
	// .getText().toString() : null);
	// clcTxn.setCcyCode(txtCurrency.getText() != null ? txtCurrency.getText()
	// .toString() : null);
	// clcTxn.setAcNo(txtAccNo.getText() != null ? txtAccNo.getText()
	// .toString() : null);
	// // clcTxn.setDepositPayfreq(txtAccountBal.getText() != null ?
	// // txtAccountBal
	// // .getText().toString() : null);
	// clcTxn.setNarrative(edtNarrative.getText() != null ? edtNarrative
	// .getText().toString() : null);
	// if (CASHCHECK.equalsIgnoreCase("DEPOSIT")) {
	// clcTxn.setReqAmnt(edtDepoAmt.getText() != null ? edtDepoAmt
	// .getText().toString() : null);
	// } else {
	// clcTxn.setReqAmnt(edtWithAmt.getText() != null ? edtWithAmt
	// .getText().toString() : null);
	// }
	// return clcTxn;
	// }

	private void onConfirmView() {
		BaseActivity.mTitle.setText(R.string.screen_cashprint);
		BaseActivity.mBtnRight.setImageResource(R.drawable.print);
		BaseActivity.mBtnRight.setTag(R.drawable.print);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.imgsearch);
		BaseActivity.mBtnLeft.setTag(R.drawable.imgsearch);
		// load views and assign to fields
		onConfirmLoadWidgets();
	}

	private void onVerifyLoadWidgets() {
		mView = getLayoutInflater().inflate(R.layout.cash_verify, null);

		TextView txtCustName = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_custname);
		TextView txtAccNo = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_accno);
		TextView txtAgtID = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_agtid);
		TextView txtAccType = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_acctype);
		TextView txtCustId = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_custid);
		TextView txtCustCurBal = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_custcurbal);
		TextView txtCurrency = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_currency);
		// TextView txtAccountBal = (TextView) mView
		// .findViewById(R.id.txv_cash_vfy_acc_bal);

		TextView txtNarrative = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_narrative);

		LinearLayout tblDepoAmt = (LinearLayout) mView
				.findViewById(R.id.linlay_deposit);
		LinearLayout tblWithAmt = (LinearLayout) mView
				.findViewById(R.id.linlay_withdrawal);

		TextView txtDepoAmt = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_depo_amt);
		TextView txtWithAmt = (TextView) mView
				.findViewById(R.id.txv_cash_vfy_withdrawal_amt);
		if (CommonContexts.CASHCHECK.equals("DEPOSIT")) {
			tblDepoAmt.setVisibility(View.VISIBLE);
			tblWithAmt.setVisibility(View.GONE);
			txtDepoAmt
					.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getTxnAmt());
		} else {
			tblWithAmt.setVisibility(View.VISIBLE);
			tblDepoAmt.setVisibility(View.GONE);
			txtWithAmt
					.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getTxnAmt());
		}
		LinearLayout mlinNarr = (LinearLayout) mView
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}

		CustomerDetail cc = CommonContexts.SELECTED_CASH_ACCOUNT;
		txtCustName.setText(cc.getCustomerFullName());
		txtCurrency.setText(cc.getAccCcy());
		txtAccNo.setText(cc.getAcNo());
		txtAccType.setText(cc.getAccType().equalsIgnoreCase("S") ? "Savings"
				: "Current");
		// txtAgtID.setText(cc.getAgentId());
		txtAgtID.setText(CommonContexts.mLOGINVALIDATION.getAgentId());
		txtCustId.setText(cc.getCustomerId());
		txtCustCurBal.setText(curBal);
		// txtAccountBal.setText(accountBal);

		txtNarrative.setText(cc.getNarrative());
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	private void onConfirmLoadWidgets() {
		mView = getLayoutInflater().inflate(R.layout.cash_confirm, null);
		if (CommonContexts.CASHCHECK.equalsIgnoreCase("DEPOSIT")) {
			userValue = edtDepoAmt.getText().toString();
			// Narrative = edtNarrative.getText().toString();
		} else {
			userValue = edtWithAmt.getText().toString();
			// Narrative = edtNarrative.getText().toString();
		}
		TextView txtCustName = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_custname);
		// TextView txtAgentName = (TextView) mView
		// .findViewById(R.id.txv_cash_cnf_agentname);
		TextView txtTxnId = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_txnid);
		// TextView txtTxnDt = (TextView) mView
		// .findViewById(R.id.txv_cash_cnf_txndt);
		TextView txtAccNo = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_accno);
		TextView txtAgtID = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_agtid);
		TextView txtAccType = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_acctype);
		TextView txtCustID = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_custid);
		TextView txtCurrency = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_currency);
		// TextView txtAccountBal = (TextView) mView
		// .findViewById(R.id.txv_cash_cnf_acc_bal);
		TextView txtNarrative = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_narrative);

		LinearLayout tblDepoAmt = (LinearLayout) mView
				.findViewById(R.id.linlay_deposit);
		LinearLayout tblWithAmt = (LinearLayout) mView
				.findViewById(R.id.linlay_withdrawal);
		TextView txtDepoAmt = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_depo_amt);
		TextView txtWithAmt = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_with_amt);
		TextView txtdatetime = (TextView) mView
				.findViewById(R.id.txv_cash_cnf_txndatetime);

		if (CommonContexts.CASHCHECK.equals("DEPOSIT")) {
			tblDepoAmt.setVisibility(View.VISIBLE);
			tblWithAmt.setVisibility(View.GONE);
			txtDepoAmt
					.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getTxnAmt());
		} else {
			tblWithAmt.setVisibility(View.VISIBLE);
			tblDepoAmt.setVisibility(View.GONE);
			txtWithAmt
					.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getTxnAmt());
		}
		CustomerDetail cc = CommonContexts.SELECTED_CASH_ACCOUNT;
		txtCustName.setText(cc.getCustomerFullName());
		txtCurrency.setText(cc.getAccCcy());
		txtAccNo.setText(cc.getAcNo());
		txtAgtID.setText(CommonContexts.mLOGINVALIDATION.getAgentId());
		txtAccType.setText(cc.getAccType().equalsIgnoreCase("S") ? "Savings"
				: "Current");
		txtCustID.setText(cc.getCustomerId());
		txtdatetime.setText(CommonContexts.simpleDateTimeFormat.format(DateUtil
				.getCurrentDataTime()));
		// txtAccountBal.setText(accountBal);
		txtTxnId.setText(CommonContexts.TXNID);

		printCashDetails = new CustomerDetail();
		printCashDetails.setCustomerFullName(cc.getCustomerFullName());
		printCashDetails.setAccCcy(cc.getAccCcy());
		printCashDetails.setAcNo(cc.getAcNo());
		printCashDetails.setAgentId(CommonContexts.mLOGINVALIDATION
				.getAgentId());
		printCashDetails
				.setAccType(cc.getAccType().equalsIgnoreCase("S") ? "Savings"
						: "Current");
		printCashDetails.setCustomerId(cc.getCustomerId());
		printCashDetails.setTxnDateTime(CommonContexts.simpleDateTimeFormat
				.format(DateUtil.getCurrentDataTime()));
		printCashDetails.setTxnId(CommonContexts.TXNID);
		printCashDetails.setTxnAmt(cc.getTxnAmt());

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	private CashTransactionRequest getCashReqObject() {
		CashTransactionRequest ctr = new CashTransactionRequest();
		ctr.setAccNo(CommonContexts.SELECTED_CASH_ACCOUNT.getAcNo());
		ctr.setCustomerId(CommonContexts.SELECTED_CASH_ACCOUNT.getCustomerId());
		ctr.setBranchCode(CommonContexts.SELECTED_CASH_ACCOUNT
				.getLocalBranchCode());
		ctr.setTxnCode(CommonContexts.CASHCHECK.equals("DEPOSIT") ? "C21"
				: "C22");
		ctr.setTxnInitTime(DateUtil.getCurrentDataTime());
		ctr.setTxnAmount(Double.valueOf(CommonContexts.SELECTED_CASH_ACCOUNT
				.getTxnAmt()));

		ctr.setTxnCcy(CommonContexts.SELECTED_CASH_ACCOUNT.getAccCcy());
		ctr.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		ctr.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		ctr.setLocCode(CommonContexts.SELECTED_CASH_ACCOUNT.getLocationCode());
		ctr.setNarrative(CommonContexts.SELECTED_CASH_ACCOUNT.getNarrative());
		ctr.setMbsSeqNo(1);
		ctr.setSyncType("R");
		return ctr;
	}

	private void handleCashConfirmation() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_Confirm;

		final CashTransactionRequest ctr = getCashReqObject();
		TransactionService cashService = ServiceFactory.getTxnService();
		TxnMaster cashTxn = extractCashObjectFromWidgets();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00114));
			if (SystemUtil.haveNetworkConnection(CashView.this)) {
					long txnStatus = cashService.addTxn(cashTxn);
					TransactionDao txnDao = DaoFactory.getTxnDao();
					long statusCash = txnDao.insertCashRecord(cashRecord(), cashTxn,
							txnStatus);
					if (txnStatus != -1 && statusCash != -1) {
//						Toast.makeText(mContext,
//								mContext.getResources().getString(R.string.MFB00017),
//								Toast.LENGTH_SHORT).show();
		
						ctr.setTxnId(cashService.getCashTxnId(CommonContexts.CASHCHECK
								.equals(Constants.DEPOSIT) ? "CD" : "CW"));
						
						JsonRequestWrapper jrw = new JsonRequestWrapper();
						jrw.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
						jrw.setJsonString(DESedeEncryDecryption.encrypt(CommonContexts.CIPHER_DATAKEY, new Gson().toJson(ctr)));
							AsyncOperation aso = new AsyncOperation(CashView.this,
									Constants.POST, new Gson().toJson(jrw));
							String url = CommonContexts.BASE_URL_TYPE
									+ CommonContexts.SERVERIP
									+ getString(R.string.service_cashtxns);
							aso.execute(url);
						
		
					} else {
						Toast.makeText(mContext,
								mContext.getResources().getString(R.string.MFB00018),
								Toast.LENGTH_SHORT).show();
					}
			}else
			{
				Toast.makeText(CashView.this, "No Network, Please try again later !",  Toast.LENGTH_SHORT).show();
			}

		} catch (ServiceException e) {
			// show error to user and stay in the current mode / screen
			LOG.error(
					mContext.getResources().getString(R.string.MFB00114)
							+ e.getMessage(), e);
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00114),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// show error to user and stay in the current mode / screen
			LOG.error(
					mContext.getResources().getString(R.string.MFB00114)
							+ e.getMessage(), e);
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00114),
					Toast.LENGTH_SHORT).show();
		}

	}

	private TxnMaster extractCashObjectFromWidgets() {
		TxnMaster loanTxn = new TxnMaster();
		CustomerDetail cashdetails = CommonContexts.SELECTED_CASH_ACCOUNT;
		loanTxn.setCustomerId(cashdetails.getCustomerId());
		loanTxn.setCustomerName(cashdetails.getCustomerFullName());
		loanTxn.setCcyCode(cashdetails.getAccCcy());
		loanTxn.setBrnCode(cashdetails.getLocalBranchCode());
		loanTxn.setSettledAmt(cashdetails.getTxnAmt());
		loanTxn.setTxnAmt("");
		loanTxn.setAccountType(CommonContexts.CASHCHECK
				.equals(Constants.DEPOSIT) ? "CD" : "CW");
		loanTxn.setAccountType(cashdetails.getAccType());
		loanTxn.setCbsAcRefNo(cashdetails.getAcNo());
		loanTxn.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		loanTxn.setIsSynched("0");
		loanTxn.setTxnStatus(0);
		loanTxn.setTxnErrorCode("null");
		loanTxn.setTxnErrorMsg("null");
		loanTxn.setGenerateRevr("N");
		loanTxn.setMbsTxnSeqNo("1");

		return loanTxn;

	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();
	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_CASHDEPO_ENTRY)) {

			if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
				handleBackToEntryView();
			} else {
				startActivity(new Intent(mContext, CustomerView.class));
				finish();
				clearFields();
			}
		}
	}

	@Override
	public void doPostExecute(String serverResponse) throws JSONException {
		CommonContexts.dismissProgressDialog();
		if (serverResponse != null) {
			try{
			JsonResponseWrapper jresw = new Gson().fromJson(serverResponse, JsonResponseWrapper.class);
			
			// this requires verification
			CashTransactionResponse ctr = new Gson().fromJson(DESedeEncryDecryption.decrypt(CommonContexts.CIPHER_DATAKEY, jresw.getJsonString()),
					CashTransactionResponse.class);
			SyncDao dao = DaoFactory.getSyncDao();
			TransactionDao txnDao = DaoFactory.getTxnDao();
			if (ctr.getStatus()) {
				dao.updateCashSyncStatus(ctr);
				txnDao.updateCashRecord("A");

				TxnMaster cashTxn = extractCashObjectFromWidgets();
				MessageBuilderUtil mbu = new MessageBuilderUtil();
				String smsMessage = mbu.buildMessage(
						CommonContexts.SELECTED_CASH_ACCOUNT.getCustomerId(),
						CommonContexts.CASHCHECK.equals("DEPOSIT") ? "C21"
								: "C22", cashTxn);
				if (!(smsMessage.equalsIgnoreCase(""))) {
					TransactionDao daos = DaoFactory.getTxnDao();
					daos.updateTxnSmsText(smsMessage, CommonContexts.TXNID);
				}
				Toast.makeText(mContext, "Transaction is success !!", Toast.LENGTH_SHORT).show();
			} else if (ctr.getMessageCode() != null) {
				try {
					dao.updateCashTxnUnSyncStatus(ctr);
					TransactionService cashService = ServiceFactory
							.getTxnService();
					TxnMaster cashTxn = extractCashObjectFromWidgets();
					cashTxn.setMbsTxnSeqNo("2");
					cashTxn.setGenerateRevr("Y");
					cashTxn.setTxnErrorCode(ctr.getMessageCode());
					cashTxn.setTxnErrorMsg(ctr.getStatusText());
					cashService.addTxn(cashTxn);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
			onConfirmView();
			}catch(Exception e)
			{
				System.out.println(e.getMessage());
			}

		} else {
			// genearate new table with updated values for cash
		}

	}

	@Override
	public void doPreExecute() {
		CommonContexts.showProgress(CashView.this, "Processing operation");
	}
}