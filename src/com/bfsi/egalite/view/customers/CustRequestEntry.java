package com.bfsi.egalite.view.customers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.CashPositionDao;
import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.TransactionDao;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CcyCodes;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.evolute.PrinterActivity;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.service.ServiceFactory;
import com.bfsi.egalite.service.TransactionService;
import com.bfsi.egalite.service.impl.BaseTransactionService;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.NumberToWords;
import com.bfsi.egalite.util.PopUpActivity;
import com.bfsi.egalite.util.ViewUtil;
import com.bfsi.egalite.view.MfiApplication;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CashRecord;

@SuppressWarnings("unused")
@SuppressLint("HandlerLeak")
public class CustRequestEntry extends BaseActivity implements OnClickListener {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private View mView;
	private static final String VIEW_STATE_PRINT = "NewRequestPrint";
	private static final String VIEW_STATE_VERIFY = "NewRequestVerify";
	private static final String VIEW_STATE_ENTRY = "NewRequestEntry";
	private static TextView txtCustId, txtCustName, txtmaturityDate;
	private static RadioButton mRadioButtonFreqDays, mRadioButtonFreqMonth,
			mRadioButtonFreqWeeks, mRadioButtonFreqYear,
			mRadioButtonTenureYear, mRadioButtonTenuremonth,
			mRadioButtonTenuredays, mRadioButtonTenureweeks,
			mRadioButtonFreqRandom;
	private static EditText edtInstallAmt, edtFrequency, edtTenure,
			edtNarrative, edtInterestRate;
	private Context mContext;
	private Spinner spinnerDepoCurrency;
	private ArrayList<String> currencyData;
	private String selectedDepositCcy, selectedFrequency, selectedTenure,
			selectInstallAmt;
	private long mtDateTime;
	public String RADIOFREQTAG = null;
	public String RADIOTENURETAG = null;
	private List<CustomerDetail> mCustDetailList;
	private String maturityDate;
	private Calendar gc;
	private String TENURECHECK = "DAYS";
	private String FREQCHECK = "DAYS";
	private Requests printRequestObj;

	public CustRequestEntry() {
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
		mBtnLeft.setTag(R.drawable.cancel);
		mBtnRight.setTag(R.drawable.save);
		CommonContexts.mMfi = ((MfiApplication) getApplicationContext());
		mBtnLeft.setImageResource(R.drawable.cancel);
		mBtnRight.setImageResource(R.drawable.save);
		mTitle.setText(R.string.screen_newdepoentry);
		CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_NEWRQ_ENTRY;
		CommonContexts.SELECTED_REQUEST = new Requests();
		CommonContexts.SELECTED_REQUEST.setCustomerId(getIntent()
				.getStringExtra("CustId"));
		CommonContexts.SELECTED_REQUEST.setCustomerName(getIntent()
				.getStringExtra("Name"));
		CommonContexts.SELECTED_REQUEST.setBranchCode(getIntent()
				.getStringExtra("branchcode"));
		// getCurrencyList();
		// getCashData();
		onSaveView();
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	public void getCurrencyList() {
		List<CcyCodes> currencyList = null;
		CashPositionDao cpda = DaoFactory.getCashDao();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00102));
			currencyList = cpda.readCurrencyList();
		} catch (DataAccessException e) {

			LOG.error(
					mContext.getResources().getString(R.string.MFB00102)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00102),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00102)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00102),
					Toast.LENGTH_SHORT).show();

		}
		currencyData = new ArrayList<String>();
		for (CcyCodes currency : currencyList)
			currencyData.add(currency.getCcyCode());
	}

	private void onSaveView() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_ENTRY;
		mView = getLayoutInflater().inflate(R.layout.deposit_entry_new, null);
		onSaveLoadWidgets();
	}

	private void onSaveLoadWidgets() {
		spinnerDepoCurrency = (Spinner) mView
				.findViewById(R.id.spinner_depo_currency);
		// spinnerInstallAmt = (Spinner) mView
		// .findViewById(R.id.spinner_depo_no_installment);
		txtCustId = (TextView) mView.findViewById(R.id.txv_depo_ent_new_custid);
		txtCustName = (TextView) mView
				.findViewById(R.id.txv_depo_ent_new_custname);
		edtInterestRate = (EditText) mView
				.findViewById(R.id.edt_depo_ent_new_interestrate);
		edtInstallAmt = (EditText) mView
				.findViewById(R.id.edt_depo_ent_new_install_amt);
		edtFrequency = (EditText) mView
				.findViewById(R.id.edt_depo_ent_new_enter_frequency);
		edtTenure = (EditText) mView
				.findViewById(R.id.edt_depo_ent_new_enter_tenure);
		edtNarrative = (EditText) mView
				.findViewById(R.id.edt_depo_ent_new_narrative);
		/*
		 * txtmaturityDate = (TextView) mView
		 * .findViewById(R.id.txv_depo_ent_new_maturity);
		 */
		mRadioButtonFreqDays = (RadioButton) mView
				.findViewById(R.id.radio_grp_freq_days);
		mRadioButtonFreqMonth = (RadioButton) mView
				.findViewById(R.id.radio_grp_freq_month);
		mRadioButtonFreqYear = (RadioButton) mView
				.findViewById(R.id.radio_grp_freq_year);
		mRadioButtonFreqRandom = (RadioButton) mView
				.findViewById(R.id.radio_grp_freq_randomly);
		mRadioButtonFreqWeeks = (RadioButton) mView
				.findViewById(R.id.radio_grp_freq_weeks);
		mRadioButtonTenureYear = (RadioButton) mView
				.findViewById(R.id.radio_grp_tenure_year);
		mRadioButtonTenuremonth = (RadioButton) mView
				.findViewById(R.id.radio_grp_tenure_month);
		mRadioButtonTenuredays = (RadioButton) mView
				.findViewById(R.id.radio_grp_tenure_days);
		mRadioButtonTenureweeks = (RadioButton) mView
				.findViewById(R.id.radio_grp_tenure_weeks);

		// mRadioButtonFreqDays.setChecked(true);
		// mRadioButtonTenuredays.setChecked(true);
		radiobuttonevent();

		if (TENURECHECK.equalsIgnoreCase("DAYS")) {
			selectedTenure = Constants.DAYS;
			mRadioButtonTenuredays.setChecked(true);
		} else if (TENURECHECK.equalsIgnoreCase("MONTHS")) {
			selectedTenure = Constants.MONTH;
			mRadioButtonTenuremonth.setChecked(true);

		} else if (TENURECHECK.equalsIgnoreCase("YEARS")) {
			selectedTenure = Constants.YEAR;
			mRadioButtonTenureYear.setChecked(true);
		} else if (TENURECHECK.equalsIgnoreCase("WEEKS")) {
			selectedTenure = Constants.WEEKS;
			mRadioButtonTenureweeks.setChecked(true);
		}

		if (FREQCHECK.equalsIgnoreCase("DAYS")) {
			selectedFrequency = Constants.DAYS;
			mRadioButtonFreqDays.setChecked(true);
		} else if (FREQCHECK.equalsIgnoreCase("MONTHS")) {
			selectedFrequency = Constants.MONTH;
			mRadioButtonFreqMonth.setChecked(true);

		} else if (FREQCHECK.equalsIgnoreCase("WEEKS")) {
			selectedFrequency = Constants.WEEKS;
			mRadioButtonFreqWeeks.setChecked(true);

		} else if (FREQCHECK.equalsIgnoreCase("YEARS")) {
			selectedFrequency = Constants.YEAR;
			mRadioButtonFreqYear.setChecked(true);
		} else if (FREQCHECK.equalsIgnoreCase("RANDOM")) {
			selectedFrequency = Constants.RANDOM;
			edtFrequency.setVisibility(View.GONE);
			mRadioButtonFreqRandom.setChecked(true);
		}

		LinearLayout linlay = (LinearLayout) mView
				.findViewById(R.id.linlay_deponew);
		linlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCustDetails();
				Intent intcust = new Intent(new Intent(CustRequestEntry.this,
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
				intcust.putExtras(bundle);
				startActivity(intcust);
			}
		});
		TableRow mtblNarr = (TableRow) mView
				.findViewById(R.id.tabrow_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mtblNarr.setVisibility(View.VISIBLE);
		} else {
			mtblNarr.setVisibility(View.GONE);
		}
		// RadioGroup tenureGroup = (RadioGroup) mView
		// .findViewById(R.id.radio_grp_new_tenure);
		// RadioGroup freqGroup = (RadioGroup) mView
		// .findViewById(R.id.radio_grp_new_frequency);
		// tenureGroup.clearCheck();
		// freqGroup.clearCheck();

		setContentValues();
	}

	private void getCustDetails() {
		CustomerDao clcDao = DaoFactory.getCustomerDao();
		try {
			mCustDetailList = clcDao
					.readCustInfo(CommonContexts.SELECTED_REQUEST
							.getCustomerId());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void setContentValues() {

		edtTenure.setEnabled(true);
		edtFrequency.setEnabled(true);
		Requests depPayments = CommonContexts.SELECTED_REQUEST;
		if (depPayments != null) {

			txtCustName
					.setText(depPayments.getCustomerName() != null ? depPayments
							.getCustomerName() : "");
			txtCustId.setText(depPayments.getCustomerId() != null ? depPayments
					.getCustomerId() : "");
			// txtmaturityDate
			// .setText(depPayments.getMaturityDate() != 0 ?
			// CommonContexts.endateFormat
			// .format(new Date(depPayments.getMaturityDate()))
			// : "");
			edtFrequency
					.setText(depPayments.getFrequency() != null ? depPayments
							.getFrequency() : "");
			edtTenure.setText(depPayments.getTenure() != null ? depPayments
					.getTenure() : "");
			edtInstallAmt
					.setText(depPayments.getDepInstallmentAmt() != null ? depPayments
							.getDepInstallmentAmt() : "");

		}

		List<String> list1 = ccyList();
		spinnerChangeListner();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_text, list1);
		dataAdapter.setDropDownViewResource(R.layout.spinner_text_black);
		spinnerDepoCurrency.setAdapter(dataAdapter);

		spinnerDepoCurrency
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());

	}

	private class CustomOnItemSelectedListener implements
			OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			selectedDepositCcy = parent.getItemAtPosition(pos).toString();
			CashPosition cashDetail = cashData(selectedDepositCcy,
					CommonContexts.mLOGINVALIDATION.getAgentId());
			String AgentBal = (String.valueOf(cashDetail.getOpeningBal()
					+ cashDetail.getTopUp() + cashDetail.getCreditAmt()
					- (cashDetail.getDebitAmt() + cashDetail.getTopDown())));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	private CashPosition cashData(String ccy, String agentId) {
		CashPositionDao dsbDao = DaoFactory.getCashDao();
		CashPosition mCcyData = null;

		try {

			LOG.debug(mContext.getResources().getString(R.string.MFB00105));
			mCcyData = dsbDao.readCashPosition(ccy, agentId);

		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00105)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00105)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();
		}

		return mCcyData;

	}

	private void radiobuttonevent() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				boolean checked = ((RadioButton) view).isChecked();
				gc = Calendar.getInstance();
				Date currentDate = new Date(DateUtil.getCurrentDataTime());
				gc.setTime(currentDate);
				switch (view.getId()) {

				case R.id.radio_grp_freq_month:
					if (checked)
						frequency(Constants.MONTH);
					// txtFreqType.setText(Constants.MONTH);
					mRadioButtonFreqDays.setChecked(false);
					mRadioButtonFreqMonth.setChecked(true);
					mRadioButtonFreqYear.setChecked(false);
					mRadioButtonFreqRandom.setChecked(false);
					mRadioButtonFreqWeeks.setChecked(false);
					edtFrequency.setVisibility(View.VISIBLE);
					FREQCHECK = "MONTHS";
					break;
				case R.id.radio_grp_freq_year:
					if (checked)
						frequency(Constants.YEAR);
					// txtFreqType.setText(Constants.YEAR);
					mRadioButtonFreqDays.setChecked(false);
					mRadioButtonFreqMonth.setChecked(false);
					mRadioButtonFreqYear.setChecked(true);
					mRadioButtonFreqRandom.setChecked(false);
					mRadioButtonFreqWeeks.setChecked(false);
					edtFrequency.setVisibility(View.VISIBLE);
					FREQCHECK = "YEARS";
					break;
				case R.id.radio_grp_freq_days:
					if (checked)
						frequency(Constants.DAYS);
					// txtFreqType.setText(Constants.DAYS);
					mRadioButtonFreqDays.setChecked(true);
					mRadioButtonFreqMonth.setChecked(false);
					mRadioButtonFreqYear.setChecked(false);
					mRadioButtonFreqRandom.setChecked(false);
					mRadioButtonFreqWeeks.setChecked(false);
					edtFrequency.setVisibility(View.VISIBLE);
					FREQCHECK = "DAYS";
					break;
				case R.id.radio_grp_freq_weeks:
					if (checked)
						frequency(Constants.WEEKS);
					// txtFreqType.setText(Constants.DAYS);
					mRadioButtonFreqWeeks.setChecked(true);
					mRadioButtonFreqMonth.setChecked(false);
					mRadioButtonFreqYear.setChecked(false);
					mRadioButtonFreqRandom.setChecked(false);
					mRadioButtonFreqDays.setChecked(false);
					edtFrequency.setVisibility(View.VISIBLE);
					FREQCHECK = "WEEKS";
					break;
				case R.id.radio_grp_freq_randomly:
					if (checked)
						frequency(Constants.RANDOM);
					// txtFreqType.setText(Constants.RANDOM);
					mRadioButtonFreqDays.setChecked(false);
					mRadioButtonFreqMonth.setChecked(false);
					mRadioButtonFreqYear.setChecked(false);
					mRadioButtonFreqRandom.setChecked(true);
					mRadioButtonFreqWeeks.setChecked(false);
					edtFrequency.setVisibility(View.GONE);
					FREQCHECK = "RANDOM";

					break;
				case R.id.radio_grp_tenure_month:
					if (checked)
						TENURECHECK = "MONTHS";
					selectedTenure = Constants.MONTH;
					// tenure(gc, Constants.MONTH);
					// txtTenureType.setText(Constants.MONTH);
					mRadioButtonTenuremonth.setChecked(true);
					mRadioButtonTenureYear.setChecked(false);
					mRadioButtonTenuredays.setChecked(false);
					mRadioButtonTenureweeks.setChecked(false);
					edtTenure.setText("");

					break;
				case R.id.radio_grp_tenure_year:
					if (checked)
						TENURECHECK = "YEARS";
					selectedTenure = Constants.YEAR;
					// tenure(gc, Constants.YEAR);
					// txtTenureType.setText(Constants.YEAR);
					mRadioButtonTenuremonth.setChecked(false);
					mRadioButtonTenureYear.setChecked(true);
					mRadioButtonTenuredays.setChecked(false);
					mRadioButtonTenureweeks.setChecked(false);
					edtTenure.setText("");

					break;
				case R.id.radio_grp_tenure_days:
					if (checked)
						TENURECHECK = "DAYS";
					selectedTenure = Constants.DAYS;
					// tenure(gc, Constants.DAYS);
					// txtTenureType.setText(Constants.DAYS);
					mRadioButtonTenuremonth.setChecked(false);
					mRadioButtonTenureYear.setChecked(false);
					mRadioButtonTenuredays.setChecked(true);
					mRadioButtonTenureweeks.setChecked(false);
					edtTenure.setText("");
					break;
				case R.id.radio_grp_tenure_weeks:
					if (checked)
						TENURECHECK = "WEEKS";
					selectedTenure = Constants.WEEKS;
					// tenure(gc, Constants.DAYS);
					// txtTenureType.setText(Constants.DAYS);
					mRadioButtonTenuremonth.setChecked(false);
					mRadioButtonTenureYear.setChecked(false);
					mRadioButtonTenuredays.setChecked(false);
					mRadioButtonTenureweeks.setChecked(true);
					edtTenure.setText("");

				}
			}

			private void frequency(final String option) {
				edtFrequency.setText("");
				edtFrequency.setEnabled(true);
				selectedFrequency = option;
				RADIOFREQTAG = option;

			}
		};

		mRadioButtonTenuremonth.setOnClickListener(listener);
		mRadioButtonTenureYear.setOnClickListener(listener);
		mRadioButtonTenuredays.setOnClickListener(listener);
		mRadioButtonTenureweeks.setOnClickListener(listener);

		mRadioButtonFreqDays.setOnClickListener(listener);
		mRadioButtonFreqMonth.setOnClickListener(listener);
		mRadioButtonFreqRandom.setOnClickListener(listener);
		mRadioButtonFreqYear.setOnClickListener(listener);
		mRadioButtonFreqWeeks.setOnClickListener(listener);

		if (RADIOFREQTAG != null) {
			if (RADIOFREQTAG.equalsIgnoreCase(Constants.DAYS)) {
				mRadioButtonFreqDays.setChecked(true);
			} else if (RADIOFREQTAG.equalsIgnoreCase(Constants.MONTH)) {
				mRadioButtonFreqMonth.setChecked(true);
			} else if (RADIOFREQTAG.equalsIgnoreCase(Constants.YEAR)) {
				mRadioButtonFreqYear.setChecked(true);
			} else if (RADIOFREQTAG.equalsIgnoreCase(Constants.RANDOM)) {
				mRadioButtonFreqRandom.setChecked(true);
			} else if (RADIOFREQTAG.equalsIgnoreCase(Constants.WEEKS)) {
				mRadioButtonFreqWeeks.setChecked(true);
			}

		}
		if (RADIOTENURETAG != null)
			if (RADIOTENURETAG.equalsIgnoreCase(Constants.YEAR)) {
				mRadioButtonTenureYear.setChecked(true);
			} else if (RADIOTENURETAG.equalsIgnoreCase(Constants.MONTH)) {
				mRadioButtonTenuremonth.setChecked(true);
			} else if (RADIOTENURETAG.equalsIgnoreCase(Constants.DAYS)) {
				mRadioButtonTenuredays.setChecked(true);
			} else if (RADIOTENURETAG.equalsIgnoreCase(Constants.WEEKS)) {
				mRadioButtonTenureweeks.setChecked(true);
			}

	}

	// private void tenure(final Calendar gc, final String option) {
	// edtTenure.setEnabled(true);
	// edtTenure.setText("");
	// edtTenure.addTextChangedListener(new TextWatcher() {
	//
	// @Override
	// public void onTextChanged(CharSequence arg0, int arg1,
	// int arg2, int arg3) {
	// if (!(edtTenure.getText() == null
	// || edtTenure.getText().equals("") || edtTenure
	// .getText().toString().isEmpty())) {
	// int val = (option != null && option
	// .equalsIgnoreCase(Constants.MONTH)) ? Calendar.MONTH
	// : (option.equalsIgnoreCase("years") ? Calendar.YEAR
	// : Calendar.DAY_OF_MONTH);
	// gc.add(val, Integer.parseInt(edtTenure.getText()
	// .toString()));
	// maturityDate = CommonContexts.dateFormat.format(gc
	// .getTime());
	//
	// mtDateTime = gc.getTimeInMillis();
	// selectedTenure = option;
	// RADIOTENURETAG = option;
	// }
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence arg0, int arg1,
	// int arg2, int arg3) {
	// }
	//
	// @Override
	// public void afterTextChanged(Editable arg0) {
	//
	// }
	// });
	// }

	private void spinnerChangeListner() {

		spinnerDepoCurrency
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String value = spinnerDepoCurrency.getSelectedItem()
								.toString();
						selectedDepositCcy = value;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
	}

	@Override
	protected void onLeftAction() {
		Object tag = mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_NEWRQ_ENTRY)) {
			if (id == R.drawable.back) {
				if (CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
					handleBackToEntryView();
				}
			} else if (id == R.drawable.cancel || id == R.drawable.imgsearch) {
				handleBackToView();
			}
		}

	}

	@Override
	protected void onRightAction() {

		Object tags = mBtnRight.getTag();
		int ids = tags == null ? -1 : (Integer) tags;
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_NEWRQ_ENTRY)) {
			if (ids == R.drawable.save) {
				handleSaveForVerification();
			} else if (ids == R.drawable.verify) {
				handleConfirmation();
			} else if (ids == R.drawable.print) {
				if(CommonContexts.USE_EXTERNAL_DEVICE)
				{
					int noOfPrint = CommonContexts.NOOFPRINTS == 0?1:CommonContexts.NOOFPRINTS;
					PrinterActivity pa = new PrinterActivity();
					pa.printTxnsReq(noOfPrint,"CUSTNEW", printRequestObj,
							mContext);
				}
				handleBackToView();
				CommonContexts.SELECTED_REQUEST = null;
			}
		}
	}

	private void handleBackToView() {
		Intent detailIntent = new Intent(CustRequestEntry.this,
				CustomerView.class);
		startActivity(detailIntent);
		finish();
	}

	private void handleBackToEntryView() {
		mTitle.setText(R.string.screen_newdepoentry);
		mBtnRight.setTag(R.drawable.save);
		mBtnRight.setImageResource(R.drawable.save);
		mBtnLeft.setTag(R.drawable.cancel);
		mBtnLeft.setImageResource(R.drawable.cancel);
		Requests req = new Requests();
		req.setCcyCode(selectedDepositCcy);
		// req.setNumOfInst(spinnerInstallAmt.getSelectedItem().toString());
		req.setDepInstallmentAmt(edtInstallAmt.getText().toString());
		req.setTenure(edtTenure.getText().toString());
		req.setFrequency(edtFrequency.getText().toString());
		// req.setNarrative(narrative)
		onSaveView();
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	public static void clearFields() {

		edtFrequency.setText("");
		edtTenure.setText("");
		edtInstallAmt.setText("");
	}

	private void handleSaveForVerification() {
		String clc_amt = null;
		if (!(edtInstallAmt.getText().toString().isEmpty())) {
			clc_amt = edtInstallAmt.getText().toString();
			if ((edtTenure.getText() == null || edtTenure.getText().equals(" ") || edtTenure
					.getText().toString().isEmpty())) {
				ViewUtil.showCrutonError(mContext, R.string.MFB00022, handler);
			} else if ((Integer.parseInt(edtTenure.getText().toString()) >= 367)) {
				ViewUtil.showCrutonError(mContext,
						R.string.errortenurevalidate, handler);

			} else if (!(edtTenure.getText() != null && Integer
					.parseInt(edtTenure.getText().toString()) > 0)) {
				ViewUtil.showCrutonError(mContext, R.string.errorTenure,
						handler);
			} else if (clc_amt != null && Double.parseDouble(clc_amt) > 0) {

				if (!(FREQCHECK.equalsIgnoreCase("RANDOM"))) {
					if ((edtFrequency.getText() == null
							|| edtFrequency.getText().equals(" ") || edtFrequency
							.getText().toString().isEmpty())) {
						ViewUtil.showCrutonError(mContext, R.string.MFB00023,
								handler);

					} else if (!(edtFrequency.getText() != null && Integer
							.parseInt(edtFrequency.getText().toString()) > 0)) {
						ViewUtil.showCrutonError(mContext,
								R.string.errorFrequency, handler);
					}
					if ((edtInterestRate.getText() == null
							|| edtInterestRate.getText().equals(" ") || edtInterestRate
							.getText().toString().isEmpty())) {
						ViewUtil.showCrutonError(mContext, R.string.MFB00034,
								handler);

					} else if (!(edtInterestRate.getText() != null && Integer
							.parseInt(edtInterestRate.getText().toString()) > 0)) {
						ViewUtil.showCrutonError(mContext,
								R.string.errorIntRate, handler);
					} else {
						CommonContexts.SELECTED_REQUEST
								.setDepInstallmentAmt(edtInstallAmt.getText()
										.toString());
						CommonContexts.SELECTED_REQUEST
								.setRateofInst(edtInterestRate.getText()
										.toString());
						CommonContexts.SELECTED_REQUEST
								.setFrequency(edtFrequency.getText() != null ? edtFrequency
										.getText().toString() : "");

						CommonContexts.SELECTED_REQUEST.setTenure(edtTenure
								.getText() != null ? edtTenure.getText()
								.toString() : "");
						CommonContexts.SELECTED_REQUEST
								.setCcyCode(selectedDepositCcy);
						CommonContexts.SELECTED_REQUEST
								.setNumOfInst(selectInstallAmt);

						CommonContexts.SELECTED_REQUEST
								.setAgentId(CommonContexts.mLOGINVALIDATION
										.getAgentId());
						CommonContexts.SELECTED_REQUEST
								.setNarrative(edtNarrative.getText().toString());

						//EGA-MN15-000011
						String value  = String.valueOf(Double.parseDouble(edtInstallAmt.getText().toString()));
						NumberToWords.speakText(value, BaseActivity.ttsp);
						onVerifyView();
						CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
					}
				} else {
					CommonContexts.SELECTED_REQUEST
							.setDepInstallmentAmt(edtInstallAmt.getText()
									.toString());
					CommonContexts.SELECTED_REQUEST
							.setRateofInst(edtInterestRate.getText().toString());
					CommonContexts.SELECTED_REQUEST.setFrequency(edtFrequency
							.getText() != null ? edtFrequency.getText()
							.toString() : "");

					CommonContexts.SELECTED_REQUEST.setTenure(edtTenure
							.getText() != null ? edtTenure.getText().toString()
							: "");
					CommonContexts.SELECTED_REQUEST
							.setCcyCode(selectedDepositCcy);
					CommonContexts.SELECTED_REQUEST
							.setNumOfInst(selectInstallAmt);

					CommonContexts.SELECTED_REQUEST
							.setAgentId(CommonContexts.mLOGINVALIDATION
									.getAgentId());
					CommonContexts.SELECTED_REQUEST.setNarrative(edtNarrative
							.getText().toString());

					onVerifyView();
					CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
				}
			} else {
				ViewUtil.showCrutonError(mContext, R.string.MFB00034, handler);
			}

		} else {
			ViewUtil.showCrutonError(mContext, R.string.MFB00014, handler);
		}

	}

	private void onVerifyView() {
		mTitle.setText(R.string.screen_newdepoverify);
		mBtnRight.setTag(R.drawable.verify);
		mBtnLeft.setTag(R.drawable.back);
		mBtnRight.setImageResource(R.drawable.verify);
		mBtnLeft.setImageResource(R.drawable.back);
		onVerifyLoadWidgets();

	}

	private void onVerifyLoadWidgets() {
		mView = getLayoutInflater().inflate(R.layout.deposit_verify_new, null);
		TextView txtCustName = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_custname);
		TextView txtAgentId = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_agentid);
		TextView txtCustId = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_custid);
		TextView txtDepoCurrency = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_currency);
		TextView txtInstallmentAmt = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_install_amt);
		TextView txtDepositTenure = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_tenure);
		TextView txtPaymentFreq = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_install_freq);
		TextView txtNarrative = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_narrative);
		/*
		 * TextView txtSettledAmt = (TextView) mView
		 * .findViewById(R.id.txv_depo_vfy_settle_amount_val);
		 */
		TextView txtInterestRate = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_interestrate_val);

		/*
		 * TextView txtNoOfInstall = (TextView) mView
		 * .findViewById(R.id.txv_depo_vfy_new_no_of_installments);
		 */

		TextView txtMaturityDate = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_new_mt_date);
		Requests req = CommonContexts.SELECTED_REQUEST;
		txtCustName.setText(req.getCustomerName());
		txtAgentId.setText(req.getAgentId());
		txtCustId.setText(req.getCustomerId());
		txtDepositTenure.setText(req.getTenure().concat(selectedTenure));
		txtPaymentFreq.setText(req.getFrequency().concat(selectedFrequency));
		/* txtNoOfInstall.setText(req.getNumOfInst()); */
		if (TENURECHECK.equalsIgnoreCase("DAYS")) {
			calMaturitDate(gc, Constants.DAYS);
		} else if (TENURECHECK.equalsIgnoreCase("MONTHS")) {
			calMaturitDate(gc, Constants.MONTH);
		} else if (TENURECHECK.equalsIgnoreCase("YEARS")) {
			calMaturitDate(gc, Constants.YEAR);
		} else if (TENURECHECK.equalsIgnoreCase("WEEKS")) {
			calMaturitDate(gc, Constants.WEEKS);
		}
		txtMaturityDate.setText(maturityDate);

		txtInstallmentAmt.setText(req.getDepInstallmentAmt());
		txtDepoCurrency.setText(req.getCcyCode());
		// txtSettledAmt.setText(req.getDepInstallmentAmt());
		txtInterestRate.setText(req.getRateofInst());
		txtNarrative.setText(req.getNarrative());
		LinearLayout mlinNarr = (LinearLayout) mView
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	private void calMaturitDate(Calendar gc, String option) {
		if (option.equalsIgnoreCase("days")) {
			int val = Calendar.DAY_OF_MONTH;
			gc = Calendar.getInstance();
			gc.add(val, Integer.parseInt(edtTenure.getText().toString()));
			maturityDate = CommonContexts.dateFormat.format(gc.getTime());
			mtDateTime = gc.getTimeInMillis();
		} else if (option.equalsIgnoreCase("months")) {
			int val = Calendar.MONTH;
			gc = Calendar.getInstance();
			gc.add(val, Integer.parseInt(edtTenure.getText().toString()));
			maturityDate = CommonContexts.dateFormat.format(gc.getTime());
			mtDateTime = gc.getTimeInMillis();
		} else if (option.equalsIgnoreCase("years")) {
			int val = Calendar.YEAR;
			gc = Calendar.getInstance();
			gc.add(val, Integer.parseInt(edtTenure.getText().toString()));
			maturityDate = CommonContexts.dateFormat.format(gc.getTime());
			mtDateTime = gc.getTimeInMillis();

		} else if (option.equalsIgnoreCase("weeks")) {
			int val = Calendar.WEEK_OF_YEAR;
			gc = Calendar.getInstance();
			gc.add(val, Integer.parseInt(edtTenure.getText().toString()));
			maturityDate = CommonContexts.dateFormat.format(gc.getTime());
			mtDateTime = gc.getTimeInMillis();

		}

	}

	private void handleConfirmation() {

		TxnMaster clcTxn = extractObjectFromWidgets();
		TransactionService payService = ServiceFactory.getTxnService();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00114));

			long txnStatus = payService.addTxn(clcTxn);
			TransactionDao txnDao = DaoFactory.getTxnDao();
			long statusCash = txnDao.insertCashRecord(cashRecord(), clcTxn,
					txnStatus);
			if (txnStatus != -1 && statusCash != -1) {
				Toast.makeText(mContext,
						getResources().getString(R.string.MFB00017),
						Toast.LENGTH_SHORT).show();

				onConfirmView();

			} else {
				Toast.makeText(mContext,
						getResources().getString(R.string.MFB00018),
						Toast.LENGTH_SHORT).show();
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

	private TxnMaster extractObjectFromWidgets() {
		TxnMaster clc = new TxnMaster();
		Requests req = CommonContexts.SELECTED_REQUEST;
		clc.setTxnType("NR");
		clc.setCustomerId(req.getCustomerId());
		clc.setBrnCode(req.getBranchCode());
		clc.setReqDpFrequency(edtFrequency.getText() != null ? edtFrequency
				.getText().toString() : "");
		clc.setReqDpTenure(edtTenure.getText().toString());
		clc.setReqDpFrequency(edtFrequency.getText().toString());

		clc.setReqDepNoInst(selectInstallAmt);
		clc.setReqMaturityDate(mtDateTime);
		clc.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());

		clc.setInitTime(DateUtil.getCurrentDataTime());
		clc.setTxnAmt(req.getDepInstallmentAmt());
		clc.setCcyCode(selectedDepositCcy);
		clc.setSettledAmt(req.getDepInstallmentAmt());

		clc.setAccountType("ND");
		clc.setTxnCode("D21");
		clc.setModuleCode("DP");
		clc.setReqDpTenureType(selectedTenure.toString().equalsIgnoreCase(
				Constants.MONTH) ? "M" : selectedTenure.toString()
				.equalsIgnoreCase(Constants.YEAR) ? "Y" : selectedTenure
				.toString().equalsIgnoreCase(Constants.WEEKS) ? "W" : "D");
		clc.setReqDpFrequencyType(selectedFrequency.toString()
				.equalsIgnoreCase(Constants.MONTH) ? "M" : selectedFrequency
				.toString().equalsIgnoreCase(Constants.YEAR) ? "Y"
				: selectedFrequency.toString()
						.equalsIgnoreCase(Constants.WEEKS) ? "W" : "D");
		clc.setIsSynched("0");
		clc.setTxnStatus(0);
		clc.setTxnErrorCode(Constants.NULL);
		clc.setTxnErrorMsg(Constants.NULL);
		clc.setGenerateRevr("X");
		clc.setMbsSeqNo("0");
		clc.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		clc.setLocationCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
		clc.setReqIntRate(req.getRateofInst());

		CommonContexts.SELECTED_REQUEST.setFrequencyType(clc
				.getReqDpFrequencyType());
		CommonContexts.SELECTED_REQUEST.setTenureType(clc.getReqDpTenureType());
		return clc;
	}

	private CashRecord cashRecord() {
		Requests cash = CommonContexts.SELECTED_REQUEST;
		CashRecord cashRec = new CashRecord();
		cashRec.setAuthStatus("A");
		cashRec.setEntrySeqNo(BaseTransactionService
				.generateEntrySequence(mContext));
		cashRec.setCashTxnId(CommonContexts.TXNID);
		cashRec.setCashTxnSeqNo(1);
		cashRec.setTxnSource("M");
		cashRec.setTxnDateTime(DateUtil
				.stringToMillisecondsss(CommonContexts.dateMonthYear
						.format(DateUtil.getCurrentDataTime())));
		cashRec.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		cashRec.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		cashRec.setAgendaId(cash.getAgentId());
		cashRec.setAgendaSeqNo(0);
		cashRec.setTxnCcy(selectedDepositCcy);
		cashRec.setAmount(Double.parseDouble(cash.getDepInstallmentAmt()));
		cashRec.setIsReversal("N");
		cashRec.setIsDeleted("N");
		cashRec.setTxnCode("D21");
		return cashRec;
	}

	private void onConfirmView() {
		mTitle.setText(R.string.screen_newdepoprint);
		mBtnRight.setTag(R.drawable.print);
		mBtnLeft.setTag(R.drawable.imgsearch);
		mBtnRight.setImageResource(R.drawable.print);
		mBtnLeft.setImageResource(R.drawable.imgsearch);
		// load views and assignment to fields
		CommonContexts.CURRENT_VIEW = VIEW_STATE_PRINT;
		onConfirmLoadWidgets();
	}

	private void onConfirmLoadWidgets() {
		mView = getLayoutInflater().inflate(R.layout.deposit_confirm_new, null);
		// TextView txtCustName = (TextView) mView
		// .findViewById(R.id.txv_depo_cnf_new_custname);
		TextView txtAgentId = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_agentid);
		TextView txtCustId = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_custid);
		TextView txtNo = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_txno);
		TextView txtCurrency = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_currency);
		TextView txtTenure = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_tenure);
		TextView txtInstallFreq = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_install_freq);
		TextView txtMaturitydt = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_mt_date);
		// TextView txtNoOfInstall = (TextView) mView
		// .findViewById(R.id.txv_depo_cnf_new_no_of_installments);
		// TextView txt = (TextView) mView
		// .findViewById(R.id.txv_depo_cnf_new_no_of_installments);
		TextView txtInstallAmt = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_install_amt);
		// TextView txtnarrative = (TextView) mView
		// .findViewById(R.id.txv_depo_cnf_new_narrative);
		TextView txtnNo = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_txno);

		TextView txtTxnDateTime = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_new_txnDateandtime);
		TextView txtInterestRate = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_interestrate_val);

		Requests req = CommonContexts.SELECTED_REQUEST;
		// txtCustName.setText(req.getCustomerName());
		txtAgentId.setText(req.getAgentId());
		txtCustId.setText(req.getCustomerId());
		txtNo.setText(CommonContexts.TXNID);
		txtCurrency.setText(req.getCcyCode());
		txtTenure.setText(req.getTenure().concat(selectedTenure));
		txtInstallAmt.setText(req.getDepInstallmentAmt());
		txtInstallFreq.setText(req.getFrequency().concat(selectedFrequency));
		txtTxnDateTime.setText(CommonContexts.simpleDateTimeFormat
				.format(DateUtil.getCurrentDataTime()));
		txtMaturitydt.setText(maturityDate);
		txtInterestRate.setText(req.getRateofInst());
		// txtNoOfInstall.setText(req.getNumOfInst());
		// txtSettleAmt.setText(req.getDepInstallmentAmt());

		printRequestObj = new Requests();
		printRequestObj.setCustomerId(txtCustId.getText().toString());
		printRequestObj.setAgentId(txtAgentId.getText().toString());
		printRequestObj.setCcyCode(txtCurrency.getText().toString());
		printRequestObj.setTxnId(txtNo.getText().toString());
		printRequestObj.setTxnTime(DateUtil.getCurrentDataTime());
		printRequestObj.setTenure(txtTenure.getText().toString());
		printRequestObj.setFrequency(txtInstallFreq.getText().toString());
		printRequestObj.setRateofInst(txtInterestRate.getText().toString());
		printRequestObj.setMaturityDate(req.getMaturityDate());
		printRequestObj.setDepInstallmentAmt(txtInstallAmt.getText().toString());

		
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();
	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_NEWRQ_ENTRY)) {
			if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
				handleBackToEntryView();
			} else {
				Intent intent = new Intent(mContext, CustomerView.class);
				startActivity(intent);
				finish();
			}
		}
	}

	@Override
	public void onClick(View arg0) {

	}

	private List<String> ccyList() {
		CashPositionDao dsbDao = DaoFactory.getCashDao();
		List<CcyCodes> mCcyList = null;
		List<String> dateData = null;
		try {

			LOG.debug(this.getResources().getString(R.string.MFB00105));
			mCcyList = dsbDao.readCurrencyList();

		} catch (DataAccessException e) {
			LOG.error(
					this.getResources().getString(R.string.MFB00105)
							+ e.getMessage(), e);

			Toast.makeText(this,
					this.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			LOG.error(
					this.getResources().getString(R.string.MFB00105)
							+ e.getMessage(), e);

			Toast.makeText(this,
					this.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();
		}
		dateData = new ArrayList<String>();
		for (CcyCodes i : mCcyList) {
			if (i.getCcyCode() != null) {
				if (!(i.getCcyCode().equalsIgnoreCase("")))
					dateData.add(i.getCcyCode());
			}

		}

		return dateData;

	}

}