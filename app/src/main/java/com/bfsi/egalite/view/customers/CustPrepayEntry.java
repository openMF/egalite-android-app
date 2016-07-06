package com.bfsi.egalite.view.customers;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.TransactionDao;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.evolute.PrinterActivity;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.CustTabPageIndicator;
import com.bfsi.egalite.service.ServiceFactory;
import com.bfsi.egalite.service.TransactionService;
import com.bfsi.egalite.service.impl.BaseTransactionService;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.MessageBuilderUtil;
import com.bfsi.egalite.util.NumberToWords;
import com.bfsi.egalite.util.PopUpActivity;
import com.bfsi.egalite.util.ViewUtil;
import com.bfsi.egalite.view.MfiApplication;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CashRecord;

@SuppressWarnings("unused")
public class CustPrepayEntry extends BaseActivity {

	private Logger LOG = LoggerFactory.getLogger(getClass());

	private View mView;
	private static final String VIEW_STATE_PRINT = "prepayPrint";
	private static final String VIEW_STATE_VERIFY = "prepayVerify";
	private static final String VIEW_STATE_ENTRY = "prepayEntry";
	private static TextView accRefNo, custName, custId, currency,depositOpendate,maturityDate,installmntAmt;
	private static EditText edtPrepayAmt, edtNarrative;
	private Context mContext;
	private LayoutInflater mLayInflate = null;
	private List<CustomerDetail> mCustDetailList;
	private Requests printRequestObj;

	public static CustPrepayEntry newInstance(Context context,
			BfsiViewPager pager, CustTabPageIndicator mPageIndicat) {
		CustPrepayEntry fragment = new CustPrepayEntry();
		fragment.mContext = context;

		return fragment;
	}

	public CustPrepayEntry() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		CommonContexts.mMfi = ((MfiApplication) getApplicationContext());
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		mTitle.setText(R.string.screen_prepay_entry);
		mContext = getApplicationContext();
		mLayInflate = LayoutInflater.from(mContext);
		CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_PREPAYRQ_ENTRY;
		onSaveView();
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	@SuppressLint("HandlerLeak")
	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};

	private void onSaveView() {

		CommonContexts.CURRENT_VIEW = VIEW_STATE_ENTRY;
		mView = getLayoutInflater()
				.inflate(R.layout.deposit_entry_prepay, null);
		onSaveLoadWidgets();
		if (CommonContexts.SELECTED_REQUEST != null) {
			setContentValues(CommonContexts.SELECTED_REQUEST);

		}
	}

	private void setContentValues(Requests depositRequests) {
		Requests requestData = CommonContexts.SELECTED_REQUEST;
		CashPosition cashDetail = null;
		try {
			cashDetail = CommonContexts.cashData(requestData.getCcyCode(), CommonContexts.mLOGINVALIDATION.getAgentId());
		} catch (ServiceException e) {
			LOG.error(
					mContext.getResources().getString(
							R.string.MFB00106)
							+ e.getMessage(), e);
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.MFB00106), Toast.LENGTH_SHORT)
					.show();
		}
		String agendaBal=String.valueOf(cashDetail.getOpeningBal()+
				cashDetail.getTopUp()+cashDetail.getCreditAmt()-(cashDetail.getDebitAmt()+cashDetail.getTopDown()));
		CommonContexts.SELECTED_REQUEST.setAgentBal(agendaBal);
		custId.setText(requestData.getCustomerId());
		custName.setText(requestData.getCustomerName());
		accRefNo.setText(requestData.getAcNo());
		currency.setText(requestData.getCcyCode());
		depositOpendate.setText(requestData.getDepOpenDate()!= null?CommonContexts.dateFormat.format(new Date(Long.valueOf(requestData.getDepOpenDate()))):"");
		maturityDate.setText(String.valueOf(requestData.getMaturityDate()!= 0?CommonContexts.dateFormat.format(new Date(requestData.getMaturityDate())):0));
		installmntAmt.setText(requestData.getDepInstallmentAmt());
		
		edtPrepayAmt
				.setText(requestData.getPrepaymentAmt() != null ? requestData
						.getPrepaymentAmt() : "");

	}
	

	private void handleBackToEntryView() {
		BaseActivity.mTitle.setText(R.string.screen_prepay_entry);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		Requests RequestDetail = CommonContexts.SELECTED_REQUEST;
		RequestDetail.setPrepaymentAmt(edtPrepayAmt.getText().toString());

		onSaveView();
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	public static void clearFields() {
		accRefNo.setText("");
		custId.setText("");
		custName.setText("");
		currency.setText("");
		edtPrepayAmt.setText("");

	}

	private void handleSaveForVerification() {
		String clc_amt = null;
		if (edtPrepayAmt.getText() != null
				&& !(edtPrepayAmt.getText().equals(""))) {
			clc_amt = edtPrepayAmt.getText().toString();

		}
		
		if (clc_amt != null && !(clc_amt.toString().isEmpty())) {
			if(Double.parseDouble(clc_amt)>0 && Double.parseDouble(CommonContexts.SELECTED_REQUEST.getDepInstallmentAmt())==Double.parseDouble(clc_amt))
			{
				CommonContexts.SELECTED_REQUEST.setPrepaymentAmt(clc_amt);
				CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
				CommonContexts.SELECTED_REQUEST.setNarrative(edtNarrative.getText()
						.toString());
				//EGA-MN15-000011
				String value = String.valueOf(Double.parseDouble(edtPrepayAmt.getText().toString()));
				NumberToWords.speakText( value, BaseActivity.ttsp);
				onVerifyView();
			}else
				ViewUtil.showCrutonError(mContext, R.string.MFB00035, handler);

		} else {
			ViewUtil.showCrutonError(mContext, R.string.MFB00014, handler);
		}
	}

	private void onVerifyView() {
		BaseActivity.mTitle.setText(R.string.screen_prepay_verify);
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		// load views and assignment to fields
		onVerifyLoadWidgets();

	}

	private void handleConfirmation() throws ServiceException {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_PRINT;
		TxnMaster clcTxn = extractObjectFromWidgets();
		TransactionService txnService = ServiceFactory.getTxnService();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00114));
			
			long txnStatus = txnService.addTxn(clcTxn);
			 TransactionDao txnDao = DaoFactory.getTxnDao();
			 long statusCash = txnDao.insertCashRecord(cashRecord(),
			 clcTxn, txnStatus);

			// succesful txn, so switch to print mode
			if (txnStatus != -1  && statusCash!=-1 ) {
				Toast.makeText(this,
						getResources().getString(R.string.MFB00017),
						Toast.LENGTH_SHORT).show();
				
				MessageBuilderUtil mbu = new MessageBuilderUtil();
				String smsMessage = mbu.buildMessage(CommonContexts.SELECTED_REQUEST.getCustomerId(), clcTxn.getTxnCode(), clcTxn);
				//update the txn table with smsContent
				//EGA-MN15-000027
				if(!(smsMessage.equalsIgnoreCase("")))
				{
					TransactionDao daos = DaoFactory.getTxnDao();
					daos.updateTxnSmsText(smsMessage, CommonContexts.TXNID);
				}
				onConfirmView();
				
			} else {
				Toast.makeText(this,
						getResources().getString(R.string.MFB00018),
						Toast.LENGTH_SHORT).show();
			}
			

		} catch (Exception e) {

			LOG.error(
					mContext.getResources().getString(R.string.MFB00114)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00114),
					Toast.LENGTH_SHORT).show();

		}
	}

	private CashRecord cashRecord() {
		Requests cash = CommonContexts.SELECTED_REQUEST ;
		CashRecord cashRec = new CashRecord();
		cashRec.setAuthStatus("A");
		cashRec.setEntrySeqNo(BaseTransactionService.generateEntrySequence(mContext));
		cashRec.setCashTxnId(CommonContexts.TXNID);
		cashRec.setCashTxnSeqNo(1);
		cashRec.setTxnSource("M");
		cashRec.setTxnDateTime(DateUtil.stringToMillisecondsss(CommonContexts.dateMonthYear.format(DateUtil.getCurrentDataTime())));
		cashRec.setTxnCode("D23");
		cashRec.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		cashRec.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		cashRec.setAgendaId(cash.getAgentId());
		cashRec.setAgendaSeqNo(0);
		cashRec.setTxnCcy(cash.getCcyCode());
		cashRec.setAmount(Double.parseDouble(cash.getPrepaymentAmt()));
		cashRec.setIsReversal("N");
		cashRec.setIsDeleted("N");
		return cashRec;
	}

	private TxnMaster extractObjectFromWidgets() {

		Requests requestData = CommonContexts.SELECTED_REQUEST;
		TxnMaster txn = new TxnMaster();
		txn.setCbsAcRefNo(requestData.getAcNo());
		txn.setCustomerName(requestData.getCustomerName());
		txn.setBrnCode(requestData.getBranchCode());
		txn.setCustomerId(requestData.getCustomerId());
		txn.setInitTime(DateUtil.getCurrentDataTime());
		txn.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		txn.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		txn.setLocationCode(requestData.getLocCode());
		txn.setCcyCode(requestData.getCcyCode());
		txn.setTxnAmt(requestData.getPrepaymentAmt());
		txn.setSettledAmt(requestData.getPrepaymentAmt());
		txn.setTxnType("DR");
		txn.setIsSynched("0");
		txn.setTxnCode("D23");
		txn.setModuleCode("DP");
		txn.setTxnStatus(0);
		txn.setTxnErrorCode("null");
		txn.setTxnErrorMsg("null");
		txn.setGenerateRevr("X");
		txn.setReqMaturityDate(requestData.getMaturityDate());
		txn.setMbsSeqNo("0");
		txn.setGeneratedSms("N");
		txn.setSmsMobileNo(requestData.getMobileNumber());
		return txn;

	}

	private void onConfirmView() {
		BaseActivity.mTitle.setText(R.string.screen_prepay_print);
		BaseActivity.mBtnRight.setImageResource(R.drawable.print);
		BaseActivity.mBtnRight.setTag(R.drawable.print);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.imgsearch);
		BaseActivity.mBtnLeft.setTag(R.drawable.imgsearch);
		// load views and assignment to fields
		onConfirmLoadWidgets();
	}

	private void onSaveLoadWidgets() {

		custName = (TextView) mView
				.findViewById(R.id.txv_depo_ent_pre_customername);
		accRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_ent_pre_refno_val);
		currency = (TextView) mView.findViewById(R.id.txv_depo_ent_pre_ccy_val);
		custId = (TextView) mView.findViewById(R.id.txv_depo_ent_pre_custid);
		edtPrepayAmt = (EditText) mView
				.findViewById(R.id.edt_depo_ent_pre_settle_amt_val);
		edtNarrative = (EditText) mView
				.findViewById(R.id.edt_depo_ent_pre_narrative_val);
		
		depositOpendate = (TextView) mView
				.findViewById(R.id.txv_depo_ent_pre_depo_val);
		maturityDate = (TextView) mView
				.findViewById(R.id.txv_depo_ent_pre_matur_val);
		installmntAmt = (TextView) mView
				.findViewById(R.id.txv_depo_ent_pre_instll_val);
		
		
		
		LinearLayout mLinLay = (LinearLayout) mView
				.findViewById(R.id.linlay_customer);

		mLinLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				getCustDetails();
				Intent intcust = new Intent(new Intent(CustPrepayEntry.this,
						PopUpActivity.class));
				Bundle bundle = new Bundle();
				bundle.putString(Constants.NAME, mCustDetailList.get(0)
						.getCustomerFullName());
				bundle.putString(Constants.DOB,CommonContexts.dateFormat.format(new Date(Long.valueOf(mCustDetailList.get(0).getDob()))));
				bundle.putString(Constants.PHONE, mCustDetailList.get(0)
						.getMobileNumber());
				bundle.putString(Constants.CUSTID, mCustDetailList.get(0)
						.getCustomerId());
				intcust.putExtras(bundle);
				startActivity(intcust);
			}
		});
		LinearLayout mlinNarr = (LinearLayout) mView
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}
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

	private void onVerifyLoadWidgets() {
		mView = getLayoutInflater().inflate(R.layout.deposit_verify_prepay,
				null);

		TextView custName = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_customername);
		TextView accNo = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_refno_val);
		TextView agentId = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_agent_id_val);
		TextView currency = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_ccy_val);
		TextView depositOpendate = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_depo_val);
		TextView maturityDate = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_matur_val);
		TextView installmntAmt = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_instll_val);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_cust_id_val);
		TextView settleAmount = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_settle_amt_val);
		TextView narrative = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_pre_narrative_val);

		Requests requestData = CommonContexts.SELECTED_REQUEST;
		custName.setText(requestData.getCustomerName());
		accNo.setText(requestData.getAcNo());
		agentId.setText(CommonContexts.mLOGINVALIDATION.getAgentId());
		currency.setText(requestData.getCcyCode());
		custId.setText(requestData.getCustomerId());
		settleAmount.setText(requestData.getPrepaymentAmt());
		narrative.setText(requestData.getNarrative());
		depositOpendate.setText(CommonContexts.dateFormat.format(new Date(Long.valueOf(requestData.getDepOpenDate()))));
		maturityDate.setText(CommonContexts.dateFormat.format(new Date(requestData.getMaturityDate())));
		installmntAmt.setText(requestData.getDepInstallmentAmt());
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

	private void onConfirmLoadWidgets() {
		mView = getLayoutInflater().inflate(R.layout.deposit_confirm_prepay,
				null);
		/*
		 * TextView custName = (TextView) mView
		 * .findViewById(R.id.txv_depo_cnf_pre_customername);
		 */
		TextView txnNo = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_txn_no_val);
		TextView acrefNo = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_refno_val);
		TextView agentId = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_agent_id_val);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_cust_id_val);
		TextView currency = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_ccy_val);
		TextView depositOpendate = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_depo_val);
		TextView maturityDate = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_matur_val);
		TextView installmntAmt = (TextView) mView
				.findViewById(R.id.txv_depo_cfy_pre_instll_val);
		
		TextView settleAmount = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_settle_amt_val);
		TextView txndateandtime = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_pre_txndateandtime_val);
		
		// TextView txtnaarrative = (TextView) mView
		// .findViewById(R.id.txv_depo_cnf_pre_narrative_val);

		Requests txnData = CommonContexts.SELECTED_REQUEST;
		custName.setText(txnData.getCustomerName());
		custId.setText(txnData.getCustomerId());
		acrefNo.setText(txnData.getAcNo());
		agentId.setText(CommonContexts.mLOGINVALIDATION.getAgentId());
		currency.setText(txnData.getCcyCode());
		txnNo.setText(CommonContexts.TXNID);
		txndateandtime.setText(CommonContexts.simpleDateTimeFormat
				.format(DateUtil.getCurrentDataTime()));
		settleAmount.setText(txnData.getPrepaymentAmt() != null ? txnData
				.getPrepaymentAmt() : "");
		
		depositOpendate.setText(CommonContexts.dateFormat.format(new Date(Long.valueOf(txnData.getDepOpenDate()))));
		maturityDate.setText(CommonContexts.dateFormat.format(new Date(txnData.getMaturityDate())));
		installmntAmt.setText(txnData.getDepInstallmentAmt());
		
		printRequestObj = new Requests();
		printRequestObj.setCustomerName(txnData.getCustomerName());
		printRequestObj.setCustomerId(custId.getText().toString());
		printRequestObj.setAcNo(acrefNo.getText().toString());
		printRequestObj.setAgentId(agentId.getText().toString());
		printRequestObj.setCcyCode(currency.getText().toString());
		printRequestObj.setTxnId(txnNo.getText().toString());
		printRequestObj.setTxnTime(DateUtil.getCurrentDataTime());
		printRequestObj.setPrepaymentAmt(settleAmount.getText().toString());
		printRequestObj.setDepOpenDate(depositOpendate.getText().toString());
		printRequestObj.setMaturityDate(txnData.getMaturityDate());
		printRequestObj.setDepInstallmentAmt(installmntAmt.getText().toString());

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();
	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_PREPAYRQ_ENTRY)) {

			if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
				handleBackToEntryView();
			} else {
				Intent intent = new Intent(mContext, CustomerView.class);
				startActivity(intent);
				this.finish();
			}
		}
	}

	@Override
	protected void onLeftAction() {
		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_PREPAYRQ_ENTRY)) {
			if (id == R.drawable.back) {
				if (CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
					handleBackToEntryView();
				}
			} else if (id == R.drawable.cancel || id == R.drawable.imgsearch ) {
				Intent intent = new Intent(CustPrepayEntry.this,
						CustomerView.class);
				startActivity(intent);
				finish();
			}
		}

	}

	@Override
	protected void onRightAction() {
		Object tags = BaseActivity.mBtnRight.getTag();
		int ids = tags == null ? -1 : (Integer) tags;
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_PREPAYRQ_ENTRY)) {
			if (ids == R.drawable.save) {
				handleSaveForVerification();
			} else if (ids == R.drawable.verify) {
				try {
					handleConfirmation();
					LOG.debug(mContext.getResources().getString(
							R.string.MFB00114));

				} catch (Exception e) {
					LOG.error(
							mContext.getResources()
									.getString(R.string.MFB00114)
									+ e.getMessage(), e);

					Toast.makeText(
							mContext,
							mContext.getResources()
									.getString(R.string.MFB00114),
							Toast.LENGTH_SHORT).show();

				}
			} else if (ids == R.drawable.print) {
				if(CommonContexts.USE_EXTERNAL_DEVICE)
				{
					int noOfPrint = CommonContexts.NOOFPRINTS == 0?1:CommonContexts.NOOFPRINTS;
					PrinterActivity pa = new PrinterActivity();
					pa.printTxnsReq(noOfPrint,"CUSTPREPAY", printRequestObj,
							mContext);
				}
				Intent detailIntent = new Intent(CustPrepayEntry.this,
						CustomerView.class);
				startActivity(detailIntent);
				finish();
				CommonContexts.SELECTED_REQUEST = null;
			}
		}
	}
}