/**
 * This source IS part OF the EGALITE - Agent Banking Solution System AND IS copyrighted by © 2014 bfsi software consulting pvt.ltd
 * © 2014 bfsi software consulting pvt.ltd - All rights reserved.
 * No part of this work may be reproduced, stored in a retrieval system, or transmitted in any form or by any means,
 * electronic, mechanical, photocopying, recording or otherwise, 
 * without the prior written permission of bfsi software consulting pvt.ltd 
 */

package com.bfsi.egalite.view.deposit.payments;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.DepositsDao;
import com.bfsi.egalite.dao.TransactionDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.AgnDepositDetail;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.evolute.PrinterActivity;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.TabPageIndicator;
import com.bfsi.egalite.service.ServiceFactory;
import com.bfsi.egalite.service.TransactionService;
import com.bfsi.egalite.service.impl.BaseTransactionService;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.MessageBuilderUtil;
import com.bfsi.egalite.util.NumberToWords;
import com.bfsi.egalite.util.ViewUtil;
import com.bfsi.egalite.view.DpPaymentView;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CashRecord;

@SuppressWarnings("unused")
@SuppressLint("HandlerLeak")
public class PaymentEntry extends Fragment implements CommandListener {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private static final String VIEW_STATE_PRINT = "payprint";
	private static final String VIEW_STATE_VERIFY = "payVerify";
	private static final String VIEW_STATE_ENTRY = "payEntry";
	private String stringRedemType = "Y";
	private String stringMaturityType = "N";
	private Context mContext;
	private BfsiViewPager mPager;
	private static String PaymentType;
	private TabPageIndicator mPageIndicator;
	private View mView;
	private LayoutInflater mLayoutInflater = null;
	private static TextView txtAccRefNo, txtCurrency, txtCustId, txtCustName,
			txtAgendaAmt, txtagentBalance, agentComponentname, minAmt,
			txttxnDateandTime;
	private static RadioButton mRadioPartial, mRadioFull;
	private static String branchCode = null;
	private static ViewGroup mViewGroup;
	private static EditText edtInputAmt, edtNarrative;
	private String paymentType, statusFlag;
	private OnClickListener listener;
	private Double mAmount;
	private String status;
	private List<CustomerDetail> mCustDetailList;

	// private TableRow tablePaymentType, tableInputAmt;

	public static PaymentEntry newInstance(Context context,
			BfsiViewPager pager, TabPageIndicator mPageIndicat) {
		PaymentEntry fragment = new PaymentEntry();
		fragment.mPager = pager;
		fragment.mContext = context;
		fragment.mPageIndicator = mPageIndicat;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mContext = getActivity();
		mLayoutInflater = inflater;
		((BaseActivity) this.getActivity()).unregisterCommandListener(this);
		((BaseActivity) this.getActivity()).registerCommandListener(this);

		onSaveView();
		mViewGroup = new LinearLayout(getActivity());
		mViewGroup.addView(mView);
		return mViewGroup;
	}

	private void onSaveView() {

		CommonContexts.CURRENT_VIEW = VIEW_STATE_ENTRY;
		if (CommonContexts.SELECTED_MATURITY != null
				&& CommonContexts.SELECTED_MATURITY.getDpIsRedemption() != null
				&& CommonContexts.SELECTED_MATURITY.getDpIsRedemption()
						.equalsIgnoreCase(stringRedemType)) {
			paymentType = stringRedemType;
		} else {
			paymentType = stringMaturityType;
		}
		mView = mLayoutInflater.inflate(R.layout.deposit_entry_maturity_pay,
				null);
		onSaveLoadWidgets();
		if (CommonContexts.SELECTED_MATURITY != null) {
			setContentValues(CommonContexts.SELECTED_MATURITY);
			// validation();
		}
	}

	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};
	private Object agendaBal;

	private CashRecord cashRecord() {
		AgendaMaster cash = CommonContexts.SELECTED_MATURITY;
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
		cashRec.setTxnCode(cash.getTxnCode() != null ? cash.getTxnCode() : "");
		cashRec.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		cashRec.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		cashRec.setAgendaId(cash.getAgentId());
		cashRec.setAgendaSeqNo(Integer.parseInt(cash.getSeqNo()));
		cashRec.setTxnCcy(cash.getCcyCode());
		cashRec.setAmount(Double.parseDouble(cash.getAmtSettled()));
		cashRec.setIsReversal("N");
		cashRec.setIsDeleted("N");
		return cashRec;
	}

	// private CashRecord cashRecord() {
	// AgendaMaster cash = CommonContexts.SELECTED_MATURITY;
	// CashRecord cashRec = new CashRecord();
	// cashRec.setAuthStatus("A");
	// cashRec.setEntrySeqNo("ABC");
	// cashRec.setCashTxnId(CommonContexts.TXNID);
	// cashRec.setCashTxnSeqNo(1);
	// cashRec.setTxnSource("M");
	// cashRec.setTxnDateTime(DateUtil.getCurrentDataTime());
	// cashRec.setTxnCode(cash.getTxnCode() != null ? cash.getTxnCode() : "");
	// cashRec.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
	// cashRec.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
	// cashRec.setAgendaId(cash.getAgentId());
	// cashRec.setAgendaSeqNo(Integer.parseInt(cash.getSeqNo()));
	// cashRec.setTxnCcy(cash.getCcyCode());
	// cashRec.setAmount(Double.parseDouble(cash.getAmtSettled()));
	// cashRec.setIsReversal("N");
	// cashRec.setIsDeleted("N");
	// cashRec.setDrCrIndicator("D");
	// return cashRec;
	// }

	private void validation() {
		mAmount = 0.0;
		if (CommonContexts.SELECTED_MATURITY != null
				&& CommonContexts.SELECTED_MATURITY.getAgendaStatus() != null) {
			String accountNumber = CommonContexts.SELECTED_MATURITY
					.getCbsAcRefNo().toString();
			String amtDue;
			if (CommonContexts.SELECTED_MATURITY.getAgendaStatus().toString()
					.equalsIgnoreCase("0")) {

				TransactionDao rpyDao = DaoFactory.getTxnDao();
				List<TxnMaster> TxnIdDetails = null;
				try {
					LOG.debug(mContext.getResources().getString(
							R.string.MFB00134));
					TxnIdDetails = rpyDao.getTxnAmt(accountNumber);
				} catch (DataAccessException e) {
					LOG.error(
							mContext.getResources()
									.getString(R.string.MFB00134)
									+ e.getMessage(), e);
					Toast.makeText(
							mContext,
							mContext.getResources()
									.getString(R.string.MFB00134),
							Toast.LENGTH_SHORT).show();
				}
				for (int i = 0; i < TxnIdDetails.size(); i++) {

					mAmount += Double.parseDouble(TxnIdDetails.get(i)
							.getTxnAmt());
				}
				amtDue = txtAgendaAmt.getText().toString().concat("(Paid:")
						.concat(String.valueOf(mAmount)).concat(")");
				txtAgendaAmt.setText(amtDue);
			}

		}
	}

	private void onSaveLoadWidgets() {

		txtCustName = (TextView) mView
				.findViewById(R.id.txv_depo_py_ent_customername_val);
		txtAccRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_py_ent_ac_ref_no_val);
		txtCustId = (TextView) mView
				.findViewById(R.id.txv_depo_py_entry_custid);
		txtCurrency = (TextView) mView
				.findViewById(R.id.txv_depo_py_currency_code_val);
		txtAgendaAmt = (TextView) mView
				.findViewById(R.id.txv_depo_py_agenda_amount_val);

		txtagentBalance = (TextView) mView
				.findViewById(R.id.txv_depo_py_agent_balance_val);
		agentComponentname = (TextView) mView
				.findViewById(R.id.txv_depo_py_component_val);
		minAmt = (TextView) mView.findViewById(R.id.txv_minamt);
		edtInputAmt = (EditText) mView
				.findViewById(R.id.edt_depo_py_input_amount_val);
		edtNarrative = (EditText) mView
				.findViewById(R.id.edt_depo_py_narrative_val);

		// tablePaymentType = (TableRow)
		// mView.findViewById(R.id.tbl_paymenttype);
		// tableInputAmt = (TableRow) mView.findViewById(R.id.inputamt);

		radiobuttonevent();
		popUpLpg();
		mRadioPartial = (RadioButton) mView
				.findViewById(R.id.radbtn_depo_ent_red_partial);

		mRadioFull = (RadioButton) mView
				.findViewById(R.id.radbtn_depo_ent_red_full);
		LinearLayout linlay = (LinearLayout) mView
				.findViewById(R.id.linlay_depoprepay);
		linlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonContexts.SELECTED_MATURITY != null) {
					getCustDetails();
					CommonContexts.showCustDetailsDialog(mContext,
							mCustDetailList, mLayoutInflater);
				}

			}
		});

		LinearLayout mlinNarr = (LinearLayout) mView
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}

		mRadioPartial.setOnClickListener(listener);
		mRadioFull.setOnClickListener(listener);

		// if (paymentType.equalsIgnoreCase(stringRedemType)) {
		// tablePaymentType.setVisibility(View.VISIBLE);
		// } else {
		// tablePaymentType.setVisibility(View.GONE);
		// tableInputAmt.setVisibility(View.GONE);
		//
		// }

	}

	private void getCustDetails() {
		CustomerDao clcDao = DaoFactory.getCustomerDao();
		try {
			mCustDetailList = clcDao
					.readCustInfo(CommonContexts.SELECTED_MATURITY
							.getCustomerId());
			// 130000003
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void popUpLpg() {
		LinearLayout mLinDeposit = (LinearLayout) mView
				.findViewById(R.id.linlay_entry_depo);
		mLinDeposit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonContexts.SELECTED_MATURITY != null) {
					CommonContexts.showDepositDialog(mContext,
							getDepositDetails(), mLayoutInflater);
				}
			}
		});

	}

	private AgnDepositDetail getDepositDetails() {
		AgnDepositDetail mDepositList = null;
		DepositsDao dsbDao = DaoFactory.getDepositDao();
		try {
			mDepositList = dsbDao
					.getDepositDetails(CommonContexts.SELECTED_MATURITY
							.getCbsAcRefNo());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

		return mDepositList;
	}

	private void radiobuttonevent() {
		listener = new OnClickListener() {

			@Override
			public void onClick(View view) {

				boolean checked = ((RadioButton) view).isChecked();
				switch (view.getId()) {
				case R.id.radbtn_depo_ent_red_partial:
					if (checked)
						edtInputAmt.setEnabled(true);
					edtInputAmt.setText("");
					mRadioPartial.setChecked(true);
					statusFlag = "P";
					PaymentType = mRadioPartial.getText().toString();
					calculatePartialAmt();
					minAmt.setVisibility(View.VISIBLE);

					break;
				case R.id.radbtn_depo_ent_red_full:
					if (checked && CommonContexts.SELECTED_MATURITY != null) {

						selectFullRadio();
					}

					break;
				default:
					ViewUtil.showCrutonError(mContext, R.string.rd_type_check,
							handler);
				}
			}
		};
	}

	private void selectFullRadio() {
		edtInputAmt
				.setText(CommonContexts.SELECTED_MATURITY.getAgendaAmt() != null ? String
						.valueOf((Double
								.parseDouble(CommonContexts.SELECTED_MATURITY
										.getAgendaAmt()))) : "");
		edtInputAmt.setEnabled(false);
		statusFlag = "F";
		mRadioFull.setChecked(true);
		PaymentType = mRadioFull.getText().toString();
		minAmt.setVisibility(View.INVISIBLE);

	}

	private void calculatePartialAmt() {
		if (CommonContexts.SELECTED_MATURITY != null) {
			if (CommonContexts.ALLOW_PARTIAL.equalsIgnoreCase("Y")) {
				String amt = "("
						+ "min: "
						+ String.valueOf(CommonContexts
								.getPartialAmount(CommonContexts.SELECTED_MATURITY
										.getAgendaAmt())) + ")";
				minAmt.setText(amt);
			} else if (CommonContexts.ALLOW_PARTIAL.equalsIgnoreCase("N")) {
				CommonContexts.ShowAlertDialog(mContext);
				selectFullRadio();
			}
		}
	}

	private void setContentValues(AgendaMaster depositpayments) {
		CashPosition cashDetail = null;
		try {
			cashDetail = CommonContexts.cashData(depositpayments.getCcyCode(),
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
		CommonContexts.SELECTED_MATURITY.setAgentBal(agendaBal);

		if (PaymentType != null) {
			if (PaymentType.equals(mRadioPartial.getText().toString())) {
				mRadioPartial.setChecked(true);
			}

			else {
				mRadioFull.setChecked(true);
				edtInputAmt.setEnabled(false);
			}

		}
		radiobuttonevent();
		String ZERO = "0";
		txtCustId
				.setText(CommonContexts.SELECTED_MATURITY.getCustomerId() != null ? CommonContexts.SELECTED_MATURITY
						.getCustomerId() : ZERO);
		txtCustName
				.setText(CommonContexts.SELECTED_MATURITY.getCustomerName() != null ? CommonContexts.SELECTED_MATURITY
						.getCustomerName() : ZERO);

		txtAccRefNo
				.setText(CommonContexts.SELECTED_MATURITY.getCbsAcRefNo() != null ? CommonContexts.SELECTED_MATURITY
						.getCbsAcRefNo() : ZERO);
		txtCurrency
				.setText(CommonContexts.SELECTED_MATURITY.getCcyCode() != null ? CommonContexts.SELECTED_MATURITY
						.getCcyCode() : ZERO);
		// txtDisbrType
		// .setText(CommonContexts.SELECTED_MATURITY.getTxnCode() != null ?
		// CommonContexts.SELECTED_MATURITY
		// .getTxnCode() : ZERO);
		txtagentBalance
				.setText(CommonContexts.SELECTED_MATURITY.getAgentBal() != null ? CommonContexts.SELECTED_MATURITY
						.getAgentBal() : ZERO);
		txtAgendaAmt
				.setText(CommonContexts.SELECTED_MATURITY.getAgendaAmt() != null ? CommonContexts.SELECTED_MATURITY
						.getAgendaAmt() : ZERO);
		edtInputAmt
				.setText(CommonContexts.SELECTED_MATURITY.getAmtSettled() != null ? CommonContexts.SELECTED_MATURITY
						.getAmtSettled() : ZERO);
		agentComponentname.setText(CommonContexts.SELECTED_MATURITY
				.getComponent() != null ? CommonContexts.SELECTED_MATURITY
				.getComponent() : ZERO);

	}

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			Object tags = BaseActivity.mBtnLeft.getTag();
			int ids = tags == null ? -1 : (Integer) tags;
			if (ids == R.drawable.back) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_PM_ENTRY)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_STATE_VERIFY)) {

					handleBackToEntryView();
				}
			} else if (ids == R.drawable.cancel) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_PM_ENTRY)) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_ENTRY)) {

						handleBackToAgendaView(false);

					} else {

						handleBackToAgendaView(false);
					}
				}
			}else if (ids == R.drawable.imgsearch) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_PM_ENTRY)) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_PRINT)) {

						handleBackToAgendaView(true);
					} else {

						handleBackToAgendaView(false);
					}
				}
			}
			break;
		case CMD_RIGHT_ACTION:
			Object tag = BaseActivity.mBtnRight.getTag();
			int id = tag == null ? -1 : (Integer) tag;
			if (CommonContexts.CURRENT_SCREEN
					.equalsIgnoreCase(CommonContexts.SCREEN_PM_ENTRY)) {
				if (id == R.drawable.save) {
					handleSaveForVerification();
				} else if (id == R.drawable.verify) {
					handleConfirmation();
				} else if (id == R.drawable.print) {
					if(CommonContexts.USE_EXTERNAL_DEVICE)
					{
						int noOfPrint = CommonContexts.NOOFPRINTS == 0?1:CommonContexts.NOOFPRINTS;
						PrinterActivity pa = new PrinterActivity();
						pa.printTxns(noOfPrint,"PAY", CommonContexts.SELECTED_MATURITY,
								mContext);
					}
					
					handleBackToAgendaView(true);
				}
			}
			break;
		}
	}

	private void handleBackToEntryView() {
		BaseActivity.mTitle.setText(R.string.screen_payment_entry);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		mViewGroup.removeAllViews();

		CommonContexts.SELECTED_MATURITY.setAmtSettled(edtInputAmt.getText()
				.toString());
		onSaveView();
		enablePagerAndIndicator(true);
		mViewGroup.addView(mView);
	}

	private void enablePagerAndIndicator(boolean status) {
		mPager.setEnabled(status);
		mPageIndicator.setEnabled(status);
	}

	private void handleBackToAgendaView(boolean reload) {
		BaseActivity.mTitle.setText(R.string.screen_payment_agenda);
		enablePagerAndIndicator(true);
		clearFields();
		if (reload) {
			Intent cash = new Intent(mContext, DpPaymentView.class);
			startActivity(cash);
			getActivity().finish();
		} else {
			mPager.setCurrentItem(0);
		}
	}

	public static void clearFields() {
		if (txtAccRefNo != null)
			txtAccRefNo.setText("");
		if (txtCurrency != null)
			txtCurrency.setText("");
		if (txtCustId != null)
			txtCustId.setText("");
		if (txtCustName != null)
			txtCustName.setText("");
		if (txtAgendaAmt != null)
			txtAgendaAmt.setText("");
		// if (txtDisbrType != null)
		// txtDisbrType.setText("");
		if (txtAgendaAmt != null)
			txtAgendaAmt.setText("");
		PaymentType = null;
		if (edtInputAmt != null)
			edtInputAmt.setText("");
		CommonContexts.SELECTED_MATURITY = null;
		mRadioPartial.setChecked(false);
		mRadioFull.setChecked(false);

	}

	private void handleSaveForVerification() {
		// String amt = "0";
		// if (paymentType.equalsIgnoreCase(stringMaturityType)) {
		// amt = "0";
		//
		// } else {
		String amt = edtInputAmt.getText().toString();
		// }
		if (amt != null && !(amt.equals(""))) {

			//EGA-MN15-000016
			if(txtagentBalance.getText().toString() != null && Double.parseDouble(txtagentBalance.getText().toString())>=  Double.parseDouble(amt))
			{
				if ((Double.parseDouble(CommonContexts.SELECTED_MATURITY
						.getAgendaAmt())) >= Double.parseDouble(amt)
						&& Double.parseDouble(amt) >= CommonContexts
								.getPartialAmount(CommonContexts.SELECTED_MATURITY
										.getAgendaAmt())) {
	
					CommonContexts.SELECTED_MATURITY.setAmtSettled(edtInputAmt
							.getText() != null ? String.valueOf(Double
							.parseDouble(edtInputAmt.getText().toString())) : null);
					CommonContexts.SELECTED_MATURITY.setNarrative(edtNarrative
							.getText() != null ? edtNarrative.getText().toString()
							: null);
					CommonContexts.SELECTED_MATURITY
							.setFullPartialIndic(PaymentType);
					CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
					//EGA-MN15-000011
					String value = String.valueOf(Double.parseDouble(edtInputAmt.getText().toString()));
					NumberToWords.speakText(value,BaseActivity.ttsp);
					onVerifyView();
				} else
					ViewUtil.showCrutonError(mContext, R.string.install_amt_err,
							handler);
			}else
					ViewUtil.showCrutonError(mContext, R.string.install_amt_err,
						handler);
		} else {
			ViewUtil.showCrutonError(mContext, R.string.MFB00014, handler);
		}
	}

	private void onVerifyView() {
		BaseActivity.mTitle.setText(R.string.screen_payment_verify);
		enablePagerAndIndicator(false);
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		// load views and assignment to fields
		onVerifyLoadWidgets();

	}

	private void onVerifyLoadWidgets() {
		mView = mLayoutInflater.inflate(R.layout.deposit_verify_maturity_pay,
				null);

		TextView txtCustName = (TextView) mView
				.findViewById(R.id.img_depo_py_vfy_customer_name_val);
		TextView txtAccrefNo = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_cbs_ac_ref_no);
		TextView txtAgentId = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_agentid_val);
		TextView txtCurrency = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_currency_code);
		TextView txtCustId = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_customer_id);
		TextView txtComponent = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_component_name);
		TextView txtAgendaAmt = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_agenda_amount);

		TextView totalIndicatory = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_full_partial_indicator);
		TextView txtInputAmount = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_input_amt_val);
		TextView txtNarrative = (TextView) mView
				.findViewById(R.id.txv_depo_py_vfy_narrative_val);

		LinearLayout linlayInd = (LinearLayout) mView
				.findViewById(R.id.linlay_indicator);
		AgendaMaster agendaloans = CommonContexts.SELECTED_MATURITY;

		txtCustName.setText(agendaloans.getCustomerName());
		txtAccrefNo.setText(agendaloans.getCbsAcRefNo());
		txtAgentId.setText(agendaloans.getAgentId());
		txtCustId.setText(agendaloans.getCustomerId());
		txtCurrency.setText(agendaloans.getCcyCode());
		txtComponent.setText(agendaloans.getComponent());
		txtAgendaAmt.setText(agendaloans.getAgendaAmt());
		txtNarrative.setText(agendaloans.getNarrative());
		txtInputAmount.setText(agendaloans.getAmtSettled());

		if (paymentType.equalsIgnoreCase(stringRedemType)) {
			linlayInd.setVisibility(View.VISIBLE);
			totalIndicatory.setText(agendaloans.getFullPartialIndic());
		} else {
			linlayInd.setVisibility(View.GONE);
			// tableInputAmt.setVisibility(View.GONE);
			totalIndicatory.setText(agendaloans.getFullPartialIndic());
		}
		LinearLayout mlinNarr = (LinearLayout) mView
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}
		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);
	}

	private void handleConfirmation() {
		
		TxnMaster payTxn = extractObjectFromWidgets();
		TransactionService payService = ServiceFactory.getTxnService();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00114));
			long txnStatus = payService.addTxn(payTxn);
			TransactionDao txnDao = DaoFactory.getTxnDao();
			long status = txnDao.updateAgenda(payTxn, txnStatus);
			long statusCash = txnDao.insertCashRecord(cashRecord(), payTxn,
					status);
			if (statusCash != -1 && status != 0 && txnStatus != -1) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.MFB00017),
						Toast.LENGTH_SHORT).show();
				MessageBuilderUtil mbu = new MessageBuilderUtil();
				String smsMessage=mbu.buildMessage(
						CommonContexts.SELECTED_MATURITY.getCustomerId(),
						payTxn.getTxnCode(), payTxn);
				//update the txn table with smsContent
				//EGA-MN15-000027
				if(!(smsMessage.equalsIgnoreCase("")))
				{
					TransactionDao daos = DaoFactory.getTxnDao();
					daos.updateTxnSmsText(smsMessage, CommonContexts.TXNID);
				}
				onConfirmView();
				enablePagerAndIndicator(false);

			} else {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.MFB00018),
						Toast.LENGTH_SHORT).show();
				mPager.setEnabled(true);
				mPageIndicator.setEnabled(true);
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

		// Need to set the values to SELECTED_DSB before assigning to
		CommonContexts.SELECTED_MATURITY.setLnIsFutureSch("");
		AgendaMaster agendaLoans = CommonContexts.SELECTED_MATURITY;
		TxnMaster Txn = new TxnMaster();
		Txn.setModuleCode(agendaLoans.getModuleCode());
		Txn.setCustomerName(agendaLoans.getCustomerName());
		Txn.setTxnCode(agendaLoans.getTxnCode());
		Txn.setAgendaId(agendaLoans.getAgendaId());
		Txn.setSeqNo(agendaLoans.getSeqNo());
		Txn.setCbsAcRefNo(agendaLoans.getCbsAcRefNo());
		Txn.setBrnCode(agendaLoans.getBranchCode());
		Txn.setCustomerId(agendaLoans.getCustomerId());
		Txn.setInitTime(DateUtil.getCurrentDataTime());
		Txn.setAgentId(agendaLoans.getAgentId());
		Txn.setDeviceId(agendaLoans.getDeviceId());
		Txn.setLocationCode(agendaLoans.getLocationCode());
		Txn.setLnIsFutureSch(agendaLoans.getLnIsFutureSch());
		Txn.setCcyCode(agendaLoans.getCcyCode());

		Txn.setTxnAmt(agendaLoans.getAmtSettled());
		Txn.setAgendaStatus("1");
		Txn.setTxnAmt(agendaLoans.getAgendaAmt());
		Txn.setSettledAmt(agendaLoans.getAmtSettled());

		Txn.setFullPartInd(statusFlag);
		Txn.setAccountType("PY");
		Txn.setIsSynched("0");
		Txn.setTxnStatus(1);
		Txn.setTxnErrorCode("null");
		Txn.setTxnErrorMsg("null");
		Txn.setGenerateRevr("X");
		Txn.setMbsSeqNo("0");
		Txn.setGeneratedSms("N");
		Txn.setAgendaCmpStartDate(agendaLoans.getCmpStDate());
		Txn.setParentCbsRefAcNo(agendaLoans.getParentCbsRefNo());
		Txn.setSmsMobileNo(agendaLoans.getPhno());
		return Txn;

	}

	private void onConfirmView() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_PRINT;
		BaseActivity.mTitle.setText(R.string.screen_payment_print);
		BaseActivity.mBtnRight.setImageResource(R.drawable.print);
		BaseActivity.mBtnRight.setTag(R.drawable.print);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.imgsearch);
		BaseActivity.mBtnLeft.setTag(R.drawable.imgsearch);
		// load views and assignment to fields
		onConfirmLoadWidgets();
	}

	private void onConfirmLoadWidgets() {

		mView = mLayoutInflater.inflate(R.layout.deposit_confirm_maturity_pay,
				null);

		TextView txnNo = (TextView) mView
				.findViewById(R.id.txv_depo_print_txnNo);
		TextView acRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_print_cbs_ac_ref_no);
		TextView agentId = (TextView) mView
				.findViewById(R.id.txv_depo_print_agent_id);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_depo_print_customer_id);

		TextView componentName = (TextView) mView
				.findViewById(R.id.txv_depo_print_component_name);
		TextView ccyCode = (TextView) mView
				.findViewById(R.id.txv_depo_print_currency_code);
		TextView txnDateandtime = (TextView) mView
				.findViewById(R.id.txv_depo_print_txndateandtime);

		TextView settleAmount = (TextView) mView
				.findViewById(R.id.txv_depo_print_settle_amt);

		AgendaMaster agendaLoans = CommonContexts.SELECTED_MATURITY;
		acRefNo.setText(agendaLoans.getCbsAcRefNo());
		agentId.setText(agendaLoans.getAgentId());
		custId.setText(agendaLoans.getCustomerId());
		ccyCode.setText(agendaLoans.getCcyCode());
		componentName.setText(agendaLoans.getComponent());
		// txtAgendaAmt.setText(agendaLoans.getAgendaAmt());
		txnDateandtime.setText(CommonContexts.simpleDateTimeFormat
				.format(DateUtil.getCurrentDataTime()));
		settleAmount.setText(agendaLoans.getAmtSettled());
		txnNo.setText(CommonContexts.TXNID);
		// if (paymentType.equalsIgnoreCase(stringRedemType)) {
		// linInputAmt.setVisibility(View.VISIBLE);
		// } else {
		//
		// //linInputAmt.setVisibility(View.GONE);
		// linInputAmt.setVisibility(View.VISIBLE);
		// }
		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);
	}

	public void myOnKeyDown() {

		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_PM_ENTRY)) {
			if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
				// go back to entry screen with the same values entered by
				// user
				handleBackToEntryView();
			} else if (CommonContexts.CURRENT_VIEW
					.equalsIgnoreCase(VIEW_STATE_ENTRY)) {
				// cancel disbursement entry and go back to the agenda view
				// without reloading (false) the agenda screen
				handleBackToAgendaView(false);
			} else if (CommonContexts.CURRENT_VIEW
					.equalsIgnoreCase(VIEW_STATE_PRINT)) {
				// go back to agenda with reloading (true) as the
				// transaction is committed already
				handleBackToAgendaView(true);
			} else {
				// firstTime onCancel - for now back to agenda without
				// reloading
				handleBackToAgendaView(false);
			}
		}
	}

}