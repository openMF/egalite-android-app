package com.bfsi.egalite.view.customers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.TransactionDao;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.CustTabPageIndicator;
import com.bfsi.egalite.service.ServiceFactory;
import com.bfsi.egalite.service.TransactionService;
import com.bfsi.egalite.service.impl.BaseTransactionService;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.view.DpCollectionView;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CashRecord;

@SuppressWarnings("unused")
@SuppressLint("HandlerLeak")
public class CustRedemEntry extends BaseActivity {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private View mView;
	private static final String VIEW_STATE_PRINT = "rdredPrint";
	private static final String VIEW_STATE_VERIFY = "rdredVerify";
	private static final String VIEW_STATE_ENTRY = "rdredEntry";
	private static TextView accRefNo, custId, custName, redeemReqDate, ccy;
	private Context mContext;

	public static CustRedemEntry newInstance(Context context,
			BfsiViewPager pager, CustTabPageIndicator mPageIndicat) {
		CustRedemEntry fragment = new CustRedemEntry();
		fragment.mContext = context;
		return fragment;
	}

	public CustRedemEntry() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		mTitle.setText(R.string.screen_redem_entry);
		CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_RDRQ_ENTRY;
		onSaveView();
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};

	private void onSaveView() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_ENTRY;
		mView = getLayoutInflater().inflate(R.layout.deposit_entry_redem, null);
		onSaveLoadWidgets();
		if (CommonContexts.SELECTED_REQUEST != null) {
			CommonContexts.SELECTED_REQUEST
					.setRedempReqDate(CommonContexts.endateFormat.format(
							DateUtil.getCurrentDataTime()).toString());
			setContentValues();
		}
	}

	private void setContentValues() {
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
		custName.setText(requestData.getCustomerName());
		custId.setText(requestData.getCustomerId());
		ccy.setText(requestData.getCcyCode());
		accRefNo.setText(requestData.getAcNo());
		redeemReqDate.setText(requestData.getRedempReqDate());
	}
	
	private void handleBackToEntryView() {
		BaseActivity.mTitle.setText(R.string.screen_redem_entry);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		mMiddleFrame.removeAllViews();
		onSaveView();
		mMiddleFrame.addView(mView);
	}

	private void onSaveLoadWidgets() {

		accRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_ent_red_refno_val);
		custId = (TextView) mView
				.findViewById(R.id.txv_depo_ent_red_cust_id_val);
		custName = (TextView) mView
				.findViewById(R.id.txv_depo_ent_red_customername_val);
		ccy = (TextView) mView.findViewById(R.id.txv_depo_ent_red_ccy_val);
		redeemReqDate = (TextView) mView
				.findViewById(R.id.txv_depo_ent_red_reqdt_val);

	}

	public static void clearFields() {
		accRefNo.setText("");
		ccy.setText("");
		custId.setText("");
		custName.setText("");
		redeemReqDate.setText("");
	}

	private void handleSaveForVerification() {

		onVerifyView();
		CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;

	}

	private void onVerifyView() {
		BaseActivity.mTitle.setText(R.string.screen_redem_verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		onVerifyLoadWidgets();

	}

	private void handleConfirmation() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_PRINT;
		TxnMaster depositTxn = extractObjectFromWidgets();
		TransactionService payService = ServiceFactory.getTxnService();
		try {

			LOG.debug(mContext.getResources().getString(R.string.MFB00114));
			
			long txnStatus = payService.addTxn(depositTxn);
			 TransactionDao txnDao = DaoFactory.getTxnDao();
			 long statusCash = txnDao.insertCashRecord(cashRecord(),
			 depositTxn, txnStatus);

			// succesful txn, so switch to print mode
			if (txnStatus != -1  && statusCash!=-1 ) {
				Toast.makeText(CustRedemEntry.this,
						getResources().getString(R.string.MFB00017),
						Toast.LENGTH_SHORT).show();
				onConfirmView();
				if (CommonContexts.SELECTED_REQUEST != null)
					CommonContexts.SELECTED_REQUEST = null;
			} else {
				Toast.makeText(CustRedemEntry.this,
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

		Requests requestData = CommonContexts.SELECTED_REQUEST;
		TxnMaster txn = new TxnMaster();
		txn.setCbsAcRefNo(requestData.getAcNo());
		txn.setBrnCode(requestData.getBranchCode());
		txn.setCustomerId(requestData.getCustomerId());
		txn.setInitTime(DateUtil.getCurrentDataTime());
		txn.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		txn.setLocationCode(requestData.getLocCode());
		txn.setCcyCode(requestData.getCcyCode());
		txn.setTxnAmt(requestData.getPrepaymentAmt());
		txn.setSettledAmt(requestData.getPrepaymentAmt());
		txn.setReqRedReqDt(requestData.getRedempReqDate());
		txn.setAccountType("RR");
		txn.setIsSynched("0");
		txn.setTxnStatus(0);
		txn.setTxnErrorCode("null");
		txn.setTxnErrorMsg("null");
		txn.setGenerateRevr("X");
		txn.setTxnCode("D22");
		txn.setMbsSeqNo("0");
		return txn;

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
		cashRec.setTxnCode("D22");
		cashRec.setAgentId("AGT0001");
		cashRec.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		cashRec.setAgendaId(cash.getAgentId());
		cashRec.setAgendaSeqNo(0);
		cashRec.setTxnCcy(cash.getCcyCode());
		cashRec.setAmount(Double.parseDouble(cash.getPrepaymentAmt()));
		cashRec.setIsReversal("N");
		cashRec.setIsDeleted("N");
		return cashRec;
	}

	private void onConfirmView() {
		BaseActivity.mTitle.setText(R.string.screen_redem_print);
		BaseActivity.mBtnRight.setTag(R.drawable.print);
		BaseActivity.mBtnRight.setImageResource(R.drawable.print);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		onConfirmLoadWidgets();
	}

	private void onVerifyLoadWidgets() {
		mView = getLayoutInflater()
				.inflate(R.layout.deposit_verify_redem, null);
		TextView custName = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_red_customername);
		TextView accRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_red_refno_val);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_red_cust_id_val);
		TextView currency = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_red_ccy_val);
		TextView agentId = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_red_agent_id_val);

		TextView redeemReqDate = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_red_reqdt_val);
		Requests requestData = CommonContexts.SELECTED_REQUEST;
		custName.setText(requestData.getCustomerName());
		accRefNo.setText(requestData.getAcNo());
		custId.setText(requestData.getCustomerId());
		currency.setText(requestData.getCcyCode());
		agentId.setText(CommonContexts.mLOGINVALIDATION.getAgentId());
		redeemReqDate.setText(requestData.getRedempReqDate());
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	private void onConfirmLoadWidgets() {
		mView = getLayoutInflater().inflate(R.layout.deposit_confirm_redem,
				null);
		TextView custName = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_red_customername);
		TextView txnNo = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_red_txn_no_val);
		TextView accRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_red_refno_val);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_red_cust_id_val);
		TextView currency = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_red_ccy_val);
		TextView agentId = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_red_agent_id_val);

		TextView redeemReqDate = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_red_reqdt_val);
		Requests pay = CommonContexts.SELECTED_REQUEST;
		custName.setText(pay.getCustomerName());
		accRefNo.setText(pay.getAcNo());
		custId.setText(pay.getCustomerId());
		currency.setText(pay.getCcyCode());
		agentId.setText(CommonContexts.mLOGINVALIDATION.getAgentId());
		redeemReqDate.setText(String.valueOf(pay.getRedempReqDate()));
		txnNo.setText(CommonContexts.TXNID);
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();
	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_RDRQ_ENTRY)) {

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
		Object tags = BaseActivity.mBtnLeft.getTag();
		int ids = tags == null ? -1 : (Integer) tags;
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_RDRQ_ENTRY)) {
			if (ids == R.drawable.back) {
				if (CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
					handleBackToEntryView();
				}
			} else if (ids == R.drawable.cancel) {
				Intent intent = new Intent(mContext, DpCollectionView.class);
				startActivity(intent);
				this.finish();
			}
		}
	}

	@Override
	protected void onRightAction() {

		Object tag = BaseActivity.mBtnRight.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_RDRQ_ENTRY)) {
			if (id == R.drawable.save) {
				handleSaveForVerification();
			} else if (id == R.drawable.verify) {
				handleConfirmation();
			} else if (id == R.drawable.print) {
				clearFields();
				Intent inthist = new Intent(CustRedemEntry.this,
						CustomerView.class);
				startActivity(inthist);
				finish();
			}
		}

	}
}