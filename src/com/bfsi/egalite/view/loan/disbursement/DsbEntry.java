/**
This source is part of the MFI and is copyrighted by BFSI Consulting Pvt Ltd.

All rights reserved.  No part of this work may be reproduced, stored in a
retrieval system, adopted or transmitted in any form or by any means,
electronic, mechanical, photographic, graphic, optic recording or otherwise,
translated in any language or computer language, without the prior written
permission of BFSI Consulting Pvt Ltd.

BFSI Consulting Pvt Ltd,
No-309, Venkatesh Complex,
2nd & Ground floor, 100 ft road, 
1st stage, Indiranagar,
(Opp to Lebanese Restaurant)
BANGALORE - 560038,
India

Copyright 2013 BFSI Consulting Pvt Ltd.
 */
package com.bfsi.egalite.view.loan.disbursement;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.LoansDao;
import com.bfsi.egalite.dao.TransactionDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.AgnLoanDetail;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.GroupLoans;
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
import com.bfsi.egalite.view.LoanDsbView;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CashRecord;
import com.bfsi.mfi.rest.model.LoanPaidSch;

@SuppressLint("HandlerLeak")
public class DsbEntry extends Fragment implements CommandListener {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private static final String VIEW_STATE_PRINT = "dsbprint";
	private static final String VIEW_STATE_VERIFY = "dsbVerify";
	private static final String VIEW_STATE_ENTRY = "dsbEntry";
	private static TextView txtaccRefno, custId, txtagendaAmt, disbType,
			minAmt, txtcurrency, txtdisbType, txtAgentBalance, txtCompoName;
	private TextView txtcustName;
	private static EditText edtInputAmount, edtNarrative;
	// private static ViewGroup mViewGroup;
	private String mFlagFullPartial;
	private View mView;
	private BfsiViewPager mPager;
	private String[] content;
	private LayoutInflater mLayoutInflater = null;
	private Context mContext;
	private TabPageIndicator mPageIndicator;
	private ViewGroup mViewGroup;
	private OnClickListener listener;
	private static RadioButton mRadioPartial, mRadioFull;
	private static String mFullPartial;
	private Double mAmount;
	private List<CustomerDetail> mCustDetailList;
	private List<LoanPaidSch> mDsbLoanPaidList;
	private List<GroupLoans> mGroupLoansList;
	private AgnLoanDetail mLoansList;

	public DsbEntry() {
	}

	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};

	public static DsbEntry newInstance(TabPageIndicator indicator,
			BfsiViewPager pager,String[] CONTENT) {
		DsbEntry fragment = new DsbEntry();
		fragment.mPager = pager;
		fragment.mPageIndicator = indicator;
		fragment.content = CONTENT;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mLayoutInflater = inflater;
		((BaseActivity) this.getActivity()).unregisterCommandListener(this);
		((BaseActivity) this.getActivity()).registerCommandListener(this);
		CommonContexts.CURRENT_VIEW = VIEW_STATE_ENTRY;
		onSaveView();
		mViewGroup = new LinearLayout(getActivity());
		mViewGroup.addView(mView);
		return mViewGroup;
	}

	private void onSaveView() {

		CommonContexts.CURRENT_VIEW = VIEW_STATE_ENTRY;
		mView = mLayoutInflater.inflate(R.layout.dsb_entry, null);
		onSaveloadWidgets();

		if (CommonContexts.SELECTED_DSB != null) {
			if (mFullPartial != null) {
				if (mFullPartial.equals(mRadioPartial.getText().toString())) {
					mRadioPartial.setChecked(true);
				}

				else {
					mRadioFull.setChecked(true);
					edtInputAmount.setEnabled(false);
				}

			}
			radiobuttonevent();
			AgendaMaster dsbData = CommonContexts.SELECTED_DSB;
			CashPosition cashDetail = null;
			try {
				cashDetail = CommonContexts.cashData(dsbData.getCcyCode(),
						CommonContexts.mLOGINVALIDATION.getAgentId());
			} catch (ServiceException e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00106)
						+ e.getMessage(), e);
				Toast.makeText(mContext,
						mContext.getResources().getString(R.string.MFB00106),
						Toast.LENGTH_SHORT).show();
			}

			String agendaBal = String.valueOf(cashDetail.getOpeningBal()
					+ cashDetail.getTopUp() + cashDetail.getCreditAmt()
					- (cashDetail.getDebitAmt() + cashDetail.getTopDown()));
			CommonContexts.SELECTED_DSB.setAgentBal(agendaBal);

			txtcustName.setText(dsbData.getCustomerName());
			custId.setText(dsbData.getCustomerId());
			txtagendaAmt.setText(dsbData.getAgendaAmt());
			disbType.setText(dsbData.getLnDisbursementType()!=null?(dsbData.getLnDisbursementType().equalsIgnoreCase("A")?"Automatic":"Manual"):"");
			txtaccRefno.setText(dsbData.getCbsAcRefNo());
			txtcurrency.setText(dsbData.getCcyCode());
			txtCompoName.setText(dsbData.getComponent());
			txtAgentBalance.setText(dsbData.getAgentBal());
			edtInputAmount.setText(dsbData.getAmtSettled() != null ? dsbData
					.getAmtSettled() : "");

			radiobuttonevent();
			// validation();
			// txtsettleAmount.setText(dsbData.getAgendaAmt());
			edtNarrative.setText(dsbData.getNarrative() != null ? dsbData
					.getNarrative() : "");

		}

	}

	private void onSaveloadWidgets() {
		txtcustName = (TextView) mView
				.findViewById(R.id.txv_dsb_entry_custname);
		txtaccRefno = (TextView) mView
				.findViewById(R.id.txv_dsb_entryentry_ac_ref_no_val);
		txtAgentBalance = (TextView) mView
				.findViewById(R.id.txv_dsb_entry_agent_bal_val);

		custId = (TextView) mView.findViewById(R.id.txv_dsb_entry_custid);
		txtcurrency = (TextView) mView.findViewById(R.id.txv_dsb_entry_ccy_val);
		txtCompoName = (TextView) mView
				.findViewById(R.id.txv_dsb_entry_componet_name_val);
		txtagendaAmt = (TextView) mView
				.findViewById(R.id.txv_dsb_entry_agenda_val);
		disbType = (TextView) mView.findViewById(R.id.txv_dsb_entry_disb_val);
		minAmt = (TextView) mView.findViewById(R.id.txv_minamt);

		edtInputAmount = (EditText) mView
				.findViewById(R.id.edt_dsb_entry_settle_amt_val);
		radiobuttonevent();
		mRadioPartial = (RadioButton) mView
				.findViewById(R.id.radbtn_dsb_entry_partial);

		mRadioFull = (RadioButton) mView
				.findViewById(R.id.radbtn_dsb_entry_full);

		RelativeLayout rellay = (RelativeLayout) mView
				.findViewById(R.id.rellay_dsb_entry);
		rellay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonContexts.SELECTED_DSB != null) {
					getCustDetails();
					CommonContexts.showCustDetailsDialog(mContext,
							mCustDetailList, mLayoutInflater);
				}
			}
		});
		popUpLpg();

		mRadioPartial.setOnClickListener(listener);
		mRadioFull.setOnClickListener(listener);

		edtNarrative = (EditText) mView
				.findViewById(R.id.edt_dsb_entry_narrative);
		edtNarrative.setTypeface(CommonContexts.getTypeface(mContext));
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
			mCustDetailList = clcDao.readCustInfo(CommonContexts.SELECTED_DSB
					.getCustomerId());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void popUpLpg() {
		LinearLayout mLinLoan = (LinearLayout) mView
				.findViewById(R.id.linlay_entry_loan);
		LinearLayout mLinGroups = (LinearLayout) mView
				.findViewById(R.id.linlay_entry_groups);
		
		mLinLoan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonContexts.SELECTED_DSB != null) {
					getLoanDetails();
					CommonContexts.showLoanDialog(mContext,
							mLoansList, mLayoutInflater);
				}
			}
		});
		
		mLinGroups.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (CommonContexts.SELECTED_DSB != null) {
					getGroupLoans();
					if (!mGroupLoansList.isEmpty())
						CommonContexts.showGroupLoanialog(mContext,
								mGroupLoansList, mLayoutInflater);

				}

			}
		});

	}

	private void getGroupLoans() {
		LoansDao dsbDao = DaoFactory.getLoanDao();
		try {
			mGroupLoansList = dsbDao.getGroupLoans(
					CommonContexts.SELECTED_DSB.getCbsAcRefNo(),
					CommonContexts.SELECTED_DSB.getCustomerId());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void getPaidSheduls() {
		LoansDao dsbDao = DaoFactory.getLoanDao();
		try {
			mDsbLoanPaidList = dsbDao
					.getLoanPaidSchedule(CommonContexts.SELECTED_DSB
							.getCbsAcRefNo());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	
	private void getLoanDetails() {
		LoansDao dsbDao = DaoFactory.getLoanDao();
		try {
			mLoansList = dsbDao
					.getLoanDetails(CommonContexts.SELECTED_DSB
							.getCbsAcRefNo());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}


	private void radiobuttonevent() {
		listener = new OnClickListener() {

			@Override
			public void onClick(View view) {

				boolean checked = ((RadioButton) view).isChecked();
				switch (view.getId()) {
				case R.id.radbtn_dsb_entry_partial:
					if (checked)
						edtInputAmount.setEnabled(true);
					edtInputAmount.setText("");
					mRadioPartial.setChecked(true);
					mFullPartial = mRadioPartial.getText().toString();
					calculatePartialAmt();
					mFlagFullPartial = "P";
					minAmt.setVisibility(View.VISIBLE);
					break;
				case R.id.radbtn_dsb_entry_full:
					if (checked) {
						if (CommonContexts.SELECTED_DSB != null) {

							selectFullRadio();

						}
					}
					break;
				default:
					ViewUtil.showCrutonError(mContext, R.string.clc_type_check,
							handler);
				}
			}
		};
	}

	private void selectFullRadio() {
		edtInputAmount
				.setText(CommonContexts.SELECTED_DSB.getAgendaAmt() != null ? String
						.valueOf((Double
								.parseDouble(CommonContexts.SELECTED_DSB
										.getAgendaAmt()))) : "");

		edtInputAmount.setEnabled(false);
		mRadioFull.setChecked(true);
		mFullPartial = mRadioFull.getText().toString();
		minAmt.setVisibility(View.INVISIBLE);
		mFlagFullPartial = "F";

	}

	private void calculatePartialAmt() {
		if (CommonContexts.SELECTED_DSB != null) {
			if (CommonContexts.ALLOW_PARTIAL.equalsIgnoreCase("Y")) {
				String amt = "("
						+ "min: "
						+ String.valueOf(CommonContexts
								.getPartialAmount(CommonContexts.SELECTED_DSB
										.getAgendaAmt())) + ")";
				minAmt.setText(amt);
			} else if (CommonContexts.ALLOW_PARTIAL.equalsIgnoreCase("N")) {
				CommonContexts.ShowAlertDialog(mContext);
				selectFullRadio();
			}
		}
	}

	private void validation() {
		mAmount = 0.0;
		if (CommonContexts.SELECTED_DSB != null) {
			if (CommonContexts.SELECTED_DSB.getAgendaStatus().toString() != null) {
				String accountNumber = CommonContexts.SELECTED_DSB
						.getCbsAcRefNo().toString();
				if (CommonContexts.SELECTED_DSB.getAgendaStatus().toString()
						.equalsIgnoreCase("0")) {

					TransactionDao dsbDao = DaoFactory.getTxnDao();
					List<TxnMaster> TxnIdDetails = null;
					try {
						LOG.debug(mContext.getResources().getString(
								R.string.MFB00106));
						TxnIdDetails = dsbDao.getTxnAmt(accountNumber);
					} catch (DataAccessException e) {
						LOG.error(
								mContext.getResources().getString(
										R.string.MFB00106)
										+ e.getMessage(), e);
						Toast.makeText(
								mContext,
								mContext.getResources().getString(
										R.string.MFB00106), Toast.LENGTH_SHORT)
								.show();
					} catch (Exception e) {
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
					for (int i = 0; i < TxnIdDetails.size(); i++) {
						mAmount += Double.parseDouble(TxnIdDetails.get(i)
								.getTxnAmt());
					}
					String amtDue = CommonContexts.SELECTED_DSB.getAgendaAmt()
							.concat("(Paid:").concat(String.valueOf(mAmount))
							.concat(")");
					txtagendaAmt.setText(amtDue);
				}

			}
		}
	}

	private void onVerifyView() {
		BaseActivity.mTitle.setText(R.string.screen_dsb_verify);
		enablePagerAndIndicator(false);
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		onVerifyLoadWidgets();
	}

	private void onVerifyLoadWidgets() {
		mView = mLayoutInflater.inflate(R.layout.dsb_verify, null);

		TextView txtaccRefno = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_ac_ref_ac_val);
		TextView txtcustName = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_customername);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_customer_id_val);
		TextView txtcomponentName = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_component_val);
		TextView txtagentId = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_agent_id_val);
		TextView txtsettleAmount = (TextView) mView
				.findViewById(R.id.edt_dsb_verify_input_amt_val);
		TextView txtcurrency = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_ccy_code_val);
		TextView txtagendaAmt = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_agenda_amt_val);
		TextView disbType = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_disb_val);

		TextView payIndicator = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_payindiactor_val);
		TextView narrative = (TextView) mView
				.findViewById(R.id.txv_dsb_verify_narrative_val);
		LinearLayout mlinNarr = (LinearLayout) mView
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}

		AgendaMaster agendaloans = CommonContexts.SELECTED_DSB;
		txtaccRefno.setText(agendaloans.getCbsAcRefNo());
		txtcustName.setText(agendaloans.getCustomerName());
		custId.setText(agendaloans.getCustomerId());
		txtcomponentName.setText(agendaloans.getComponent());
		txtagentId.setText(agendaloans.getAgentId());
		txtsettleAmount.setText(agendaloans.getAmtSettled());
		payIndicator.setText(agendaloans.getFullPartialIndic());
		txtcurrency.setText(agendaloans.getCcyCode());
		txtagendaAmt.setText(agendaloans.getAgendaAmt());
		disbType.setText(agendaloans.getLnDisbursementType());
		narrative.setText(agendaloans.getNarrative());

		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);
	}

	private void onConfirmView() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_PRINT;
		BaseActivity.mTitle.setText(R.string.screen_dsb_print);
		BaseActivity.mBtnRight.setImageResource(R.drawable.print);
		BaseActivity.mBtnRight.setTag(R.drawable.print);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.imgsearch);
		BaseActivity.mBtnLeft.setTag(R.drawable.imgsearch);
		onConfirmLoadWidgets();
	}

	private void onConfirmLoadWidgets() {
		mView = mLayoutInflater.inflate(R.layout.dsb_confirm, null);
		TextView txtaccRefno = (TextView) mView
				.findViewById(R.id.txv_dsb_print_ac_ref_no_val);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_dsb_print_customer_id_val);
		TextView txtcomponentName = (TextView) mView
				.findViewById(R.id.txv_dsb_print_component_val);
		TextView txtagentId = (TextView) mView
				.findViewById(R.id.txv_dsb_print_agent_id_val);
		TextView txtsettleAmount = (TextView) mView
				.findViewById(R.id.txv_dsb_print_inputamount_val);
		TextView txtcurrency = (TextView) mView
				.findViewById(R.id.txv_dsb_print_ccy_code_val);
		// TextView txtagendaAmt = (TextView) mView
		// .findViewById(R.id.txv_dsb_print_agenda_amount_val);
		TextView txnId = (TextView) mView
				.findViewById(R.id.txv_dsb_print_mbs__txn_no_val);
		TextView txnDateTime = (TextView) mView
				.findViewById(R.id.txv_dsb_print_txn_datetime_val);

		AgendaMaster agendaLoans = CommonContexts.SELECTED_DSB;
		txtaccRefno.setText(agendaLoans.getCbsAcRefNo());
		custId.setText(agendaLoans.getCustomerId());
		txtcomponentName.setText(agendaLoans.getComponent());
		txtagentId.setText(agendaLoans.getAgentId());
		txtsettleAmount.setText(agendaLoans.getAmtSettled());
		txtcurrency.setText(agendaLoans.getCcyCode());
		txnDateTime.setText(CommonContexts.simpleDateTimeFormat.format(DateUtil
				.getCurrentDataTime()));
		txnId.setText(CommonContexts.TXNID);
		agendaLoans.setTransactionId(CommonContexts.TXNID);
		
		
		
		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);
	}

	@Override
	public View getView() {
		return super.getView();
	}

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			Object tags = BaseActivity.mBtnLeft.getTag();
			int ids = tags == null ? -1 : (Integer) tags;
			if (ids == R.drawable.back) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_DSB_ENTRY)) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_VERIFY)) {

						handleBackToEntryView();
					} 
				}
			} else if (ids == R.drawable.cancel) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_DSB_ENTRY)) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_ENTRY)) {
						// cancel disbursement entry and go back to the agenda
						// view
						// without reloading (false) the agenda screen
						handleBackToAgendaView(false);
					} else if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_PRINT)) {
						// go back to agenda with reloading (true) as the
						// transaction is committed already
						handleBackToAgendaView(true);
					} else {
						CommonContexts.SELECTED_DSB = null;
						// firstTime onCancel - for now back to agenda without
						// reloading
						handleBackToAgendaView(true);
					}
				}
			}else if (ids == R.drawable.imgsearch) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_DSB_ENTRY)) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_PRINT)) {
						// go back to agenda with reloading (true) as the
						// transaction is committed already
						handleBackToAgendaView(true);
					} else {
						CommonContexts.SELECTED_DSB = null;
						// firstTime onCancel - for now back to agenda without
						// reloading
						handleBackToAgendaView(true);
					}
				}
			}
			break;
		case CMD_RIGHT_ACTION:
			Object tag = BaseActivity.mBtnRight.getTag();
			int id = tag == null ? -1 : (Integer) tag;
			if (CommonContexts.CURRENT_SCREEN
					.equalsIgnoreCase(CommonContexts.SCREEN_DSB_ENTRY)) {
				if (id == R.drawable.save) {
					// save disbursement to verify, change the state of the
					// screen
					// not allowing navigation
					handleSaveForVerification();
				} else if (id == R.drawable.verify) {
					// on confirmation, commit disbursement transaction in
					// database
					handleDsbConfirmation();
				} else if (id == R.drawable.print) {
					// for now, take user to navigation page
					//
					if(CommonContexts.USE_EXTERNAL_DEVICE)
					{
						int noOfPrint = CommonContexts.NOOFPRINTS == 0?1:CommonContexts.NOOFPRINTS;
						PrinterActivity pa = new PrinterActivity();
						pa.printTxns(noOfPrint,"DSB", CommonContexts.SELECTED_DSB,
								mContext);
					}
					

					handleBackToAgendaView(true);
					CommonContexts.SELECTED_DSB = null;

				}
			}
			break;
		}
	}

	/**
	 * Add loan dsb txn to db
	 * 
	 * @throws Exception
	 */
	private void handleDsbConfirmation() {
		
		TxnMaster loanTxn = extractLoanDsbObjectFromWidgets();
		TransactionService dsbService = ServiceFactory.getTxnService();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00114));

			long txnStatus = dsbService.addTxn(loanTxn);
			TransactionDao txnDao = DaoFactory.getTxnDao();
			long status = txnDao.updateAgenda(loanTxn, txnStatus);
			long statusCash = txnDao.insertCashRecord(cashRecord(), loanTxn,
					status);

			if (statusCash != -1 && status != 0 && txnStatus != -1) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.MFB00017),
						Toast.LENGTH_SHORT).show();

				MessageBuilderUtil mbu = new MessageBuilderUtil();
				String smsMessage = mbu.buildMessage(CommonContexts.SELECTED_DSB.getCustomerId(),
						loanTxn.getTxnCode(), loanTxn);
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
				enablePagerAndIndicator(true);
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

	private TxnMaster extractLoanDsbObjectFromWidgets() {
		// Need to set the values to SELECTED_DSB before assigning to
		CommonContexts.SELECTED_DSB.setLnIsFutureSch("");
		AgendaMaster agendaLoans = CommonContexts.SELECTED_DSB;
		TxnMaster loanTxn = new TxnMaster();
		loanTxn.setCustomerName(agendaLoans.getCustomerName());
		loanTxn.setModuleCode(agendaLoans.getModuleCode());
		loanTxn.setTxnCode(agendaLoans.getTxnCode());
		loanTxn.setAgendaId(agendaLoans.getAgendaId());
		loanTxn.setSeqNo(agendaLoans.getSeqNo());
		loanTxn.setCbsAcRefNo(agendaLoans.getCbsAcRefNo());
		loanTxn.setBrnCode(agendaLoans.getBranchCode());
		loanTxn.setCustomerId(agendaLoans.getCustomerId());
		loanTxn.setCustomerName(agendaLoans.getCustomerName());
		loanTxn.setInitTime(DateUtil.getCurrentDataTime());
		loanTxn.setAgentId(agendaLoans.getAgentId());
		loanTxn.setDeviceId(agendaLoans.getDeviceId());
		loanTxn.setLocationCode(agendaLoans.getLocationCode());
		loanTxn.setLnIsFutureSch(agendaLoans.getLnIsFutureSch());
		loanTxn.setCcyCode(agendaLoans.getCcyCode());
		loanTxn.setLcyCode(agendaLoans.getLcyCode());
		loanTxn.setTxnAmt(agendaLoans.getAgendaAmt());
		loanTxn.setSettledAmt(agendaLoans.getAmtSettled());
		loanTxn.setTxnAmt(agendaLoans.getAgendaAmt());
		loanTxn.setComponentName(agendaLoans.getComponent());
		loanTxn.setLoanDisbursementType(agendaLoans.getLnDisbursementType());
		loanTxn.setIsGroupLoan(agendaLoans.getIsGroupLoan());
		loanTxn.setFullPartInd(mFlagFullPartial);
		loanTxn.setAgendaStatus("1");
		loanTxn.setIsSynched("0");
		loanTxn.setTxnStatus(0);
		loanTxn.setAccountType("DB");
		loanTxn.setTxnErrorCode("null");
		loanTxn.setTxnErrorMsg("null");
		loanTxn.setGenerateRevr("X");
		loanTxn.setMbsSeqNo("0");
		loanTxn.setGeneratedSms("N");
		loanTxn.setParentCbsRefAcNo(agendaLoans.getParentCbsRefNo());
		loanTxn.setAgendaCmpStartDate(agendaLoans.getCmpStDate());
		loanTxn.setSmsMobileNo(agendaLoans.getPhno());

		// DOB and Phone are not set to this object as they are not required at
		// server.

		return loanTxn;
	}

	private CashRecord cashRecord() {
		AgendaMaster cash = CommonContexts.SELECTED_DSB;
		CashRecord cashRec = new CashRecord();
		cashRec.setAuthStatus("A");
		cashRec.setEntrySeqNo(BaseTransactionService
				.generateEntrySequence(mContext));
		cashRec.setCashTxnId(CommonContexts.TXNID);
		cashRec.setCashTxnSeqNo(1);
		cashRec.setTxnSource("M");
		cashRec.setTxnDateTime(DateUtil.stringToMillisecondsss(CommonContexts.dateMonthYear.format(DateUtil.getCurrentDataTime())));
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

	private void handleSaveForVerification() {
		@SuppressWarnings("unused")
		String narative = null, disp_amt = null;

		if (edtInputAmount.getText() != null
				&& !(edtInputAmount.getText().equals(""))) {
			disp_amt = edtInputAmount.getText().toString();
			// narative = edtNarrative.getText().toString();

		}
		if (disp_amt != null && !(disp_amt.equals("")) && mFullPartial != null
				&& !(mFullPartial.equals(""))) {

				//EGA-MN15-000016
				if(txtAgentBalance.getText().toString()!=null && Double.parseDouble(txtAgentBalance.getText().toString())>=Double
						.parseDouble(disp_amt))
				{
					if ((Double.parseDouble(CommonContexts.SELECTED_DSB.getAgendaAmt())) >= Double
							.parseDouble(disp_amt)
							&& Double.parseDouble(disp_amt) >= CommonContexts
									.getPartialAmount(CommonContexts.SELECTED_DSB
											.getAgendaAmt())) {
		
						CommonContexts.SELECTED_DSB.setAmtSettled(String.valueOf(Double
								.parseDouble(edtInputAmount.getText().toString())));
						CommonContexts.SELECTED_DSB.setFullPartialIndic(mFullPartial);
						CommonContexts.SELECTED_DSB.setNarrative(edtNarrative.getText()
								.toString());
						CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
						//EGA-MN15-000011
						String value = String.valueOf(Double.parseDouble(edtInputAmount.getText().toString()));
						NumberToWords.speakText(value, BaseActivity.ttsp);
						onVerifyView();
		
					} else {
						ViewUtil.showCrutonError(mContext, R.string.install_amt_err,
								handler);
					}
				}else
					ViewUtil.showCrutonError(mContext, R.string.MFB00036, handler);

		} else {
			ViewUtil.showCrutonError(mContext, R.string.MFB00014, handler);
		}
	}

	private void handleBackToAgendaView(boolean reload) {
		BaseActivity.mTitle.setText(R.string.screen_dsb_agenda);
		enablePagerAndIndicator(true);
		clearFields();
		if (reload) {
			Intent cash = new Intent(mContext, LoanDsbView.class);
			startActivity(cash);
			getActivity().finish();
		} else {
			mPager.setCurrentItem(0);
		}
	}

	private void handleBackToEntryView() {
		BaseActivity.mTitle.setText(R.string.screen_dsb_entry);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		mViewGroup.removeAllViews();
		AgendaMaster dsbData = CommonContexts.SELECTED_DSB;
		dsbData.setAmtSettled(edtInputAmount.getText().toString());

		onSaveView();
		enablePagerAndIndicator(true);
		mViewGroup.addView(mView);
	}

	public static void clearFields() {
		custId.setText("");
		txtaccRefno.setText("");
		txtcurrency.setText("");
		txtagendaAmt.setText("");
		txtCompoName.setText("");
		mFullPartial = null;
		if (txtdisbType != null)
			txtdisbType.setText("");

		if (edtInputAmount != null)
			edtInputAmount.setText("");
		edtNarrative.setText("");
		mRadioPartial.setChecked(false);
		mRadioFull.setChecked(false);
		CommonContexts.SELECTED_DSB = null;
	}

	private void enablePagerAndIndicator(boolean status) {
		mPager.setEnabled(status);
		mPageIndicator.setEnabled(status);
	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_DSB_ENTRY)) {
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
				CommonContexts.SELECTED_DSB = null;
				// firstTime onCancel - for now back to agenda without
				// reloading
				handleBackToAgendaView(false);
			}
		}
	}
}