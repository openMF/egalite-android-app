/**
 * This source IS part OF the EGALITE - Agent Banking Solution System AND IS copyrighted by � 2014 bfsi software consulting pvt.ltd
 * � 2014 bfsi software consulting pvt.ltd - All rights reserved.
 * No part of this work may be reproduced, stored in a retrieval system, or transmitted in any form or by any means,
 * electronic, mechanical, photocopying, recording or otherwise, 
 * without the prior written permission of bfsi software consulting pvt.ltd 
 */

package com.bfsi.egalite.view.deposit.collections;

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
import com.bfsi.egalite.view.DpCollectionView;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CashRecord;

@SuppressWarnings("unused")
public class CollectionEntry extends Fragment implements CommandListener {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private static final String VIEW_STATE_PRINT = "clcprint";
	private static final String VIEW_STATE_VERIFY = "clcVerify";
	private static final String VIEW_STATE_ENTRY = "clcEntry";
	private static TextView acRefNo, txnDateandtime, custName, ccyCode, custId,
			dsbType, agemtId, agentComponentName, agentBalance, agendaAmt,
			fullPartialIndi,minAmt;
	private static EditText edtInputAmt, edtNarrative;
	private static ViewGroup mViewGroup;
	private static String collectionType;
	private String statusFlag;
	private View mView;
	private BfsiViewPager mPager;
	private LayoutInflater mLayoutInflater = null;
	private Context mContext;
	private TabPageIndicator mPageIndicator;
	private OnClickListener listener;
	private static RadioButton mRadioFull, mRadioPartial;
	private Double mAmount;
	private String status;
	private List<CustomerDetail> mCustDetailList;

	@SuppressLint("HandlerLeak")
	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};

	public static CollectionEntry newInstance(Context context,
			BfsiViewPager pager, TabPageIndicator mPageIndicat) {
		CollectionEntry fragment = new CollectionEntry();
		fragment.mPager = pager;
		fragment.mPageIndicator = mPageIndicat;
		fragment.mContext = context;
		return fragment;
	}

	private void validation() {
		mAmount = 0.0;
		if (CommonContexts.SELECTED_CLC != null
				&& CommonContexts.SELECTED_CLC.getAgendaStatus() != null) {
			String accountNumber = CommonContexts.SELECTED_CLC.getCbsAcRefNo()
					.toString();
			String amtDue;
			if (CommonContexts.SELECTED_CLC.getAgendaStatus().toString()
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
							.getTxnAmt().toString());
				}
				amtDue = agendaAmt.getText().toString().concat("(Paid:")
						.concat(String.valueOf(mAmount)).concat(")");
				agendaAmt.setText(amtDue);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	private CashRecord cashRecord() {
		AgendaMaster cash = CommonContexts.SELECTED_CLC;
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
		mView = mLayoutInflater
				.inflate(R.layout.deposit_entry_collection, null);
		onSaveLoadWidgets();

		if (CommonContexts.SELECTED_CLC != null) {
			setContentValues(CommonContexts.SELECTED_CLC);
			// validation();
		}
	}

	private void onSaveLoadWidgets() {
		custName = (TextView) mView
				.findViewById(R.id.txv_depo_ent_col_custname);

		acRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_cl_ent_cbs_ac_ref_no);
		ccyCode = (TextView) mView
				.findViewById(R.id.txv_depo_cl_ent_currency_code);
		custId = (TextView) mView.findViewById(R.id.txv_depo_entry_custid);
		agendaAmt = (TextView) mView
				.findViewById(R.id.txv_depo_cl_ent_agenda_amount);
		edtNarrative = (EditText) mView
				.findViewById(R.id.edt_depo_cl_ent_full_narrative);

		edtInputAmt = (EditText) mView
				.findViewById(R.id.edt_depo_cl_ent_input_amount);
		agentComponentName = (TextView) mView
				.findViewById(R.id.txv_depo_cl_ent_component_name_val);

		agentBalance = (TextView) mView
				.findViewById(R.id.txv_depo_cl_ent_agent_balance_val);
		minAmt = (TextView) mView.findViewById(R.id.txv_minamt);
		radiobuttonevent();
		popUpLpg();
		mRadioPartial = (RadioButton) mView
				.findViewById(R.id.radbtn_deposit_entry_collection_partial);

		mRadioFull = (RadioButton) mView
				.findViewById(R.id.radbtn_deposit_entry_collection_full);
		LinearLayout linlay = (LinearLayout) mView
				.findViewById(R.id.linlay_depocoll);
		linlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonContexts.SELECTED_CLC != null) {
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
		edtInputAmt.setTypeface(CommonContexts.getTypeface(mContext));

	}

	private void getCustDetails() {
		CustomerDao clcDao = DaoFactory.getCustomerDao();
		try {
			mCustDetailList = clcDao.readCustInfo(CommonContexts.SELECTED_CLC
					.getCustomerId());
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
				if (CommonContexts.SELECTED_CLC != null) {
					CommonContexts.showDepositDialog(mContext,
							getDepositDetails(), mLayoutInflater);
				}
			}
		});

	}
	private AgnDepositDetail getDepositDetails() {
		AgnDepositDetail mDepositList=null;
		DepositsDao dsbDao = DaoFactory.getDepositDao();
		try {
			 mDepositList = dsbDao
					.getDepositDetails(CommonContexts.SELECTED_CLC
							.getCbsAcRefNo());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}
	
		return mDepositList;
	}
	private void settingFieldValues(AgendaMaster depositCLC) {
		custName.setText(depositCLC.getCustomerName());
		custId.setText(depositCLC.getCustomerId());
		agendaAmt.setText(depositCLC.getAgendaAmt());
		ccyCode.setText(depositCLC.getCcyCode());
		dsbType.setText(depositCLC.getLnDisbursementType());
		acRefNo.setText(depositCLC.getCbsAcRefNo());
	}

	private void setContentValues(AgendaMaster dpClc) {

		CashPosition cashDetail = null;
		try {
			cashDetail = CommonContexts.cashData(dpClc.getCcyCode(), CommonContexts.mLOGINVALIDATION.getAgentId());
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
		String agendaBal = String.valueOf(cashDetail.getOpeningBal()
				+ cashDetail.getTopUp() + cashDetail.getCreditAmt()
				- (cashDetail.getDebitAmt() + cashDetail.getTopDown()));
		CommonContexts.SELECTED_CLC.setAgentBal(agendaBal);

		if (collectionType != null) {
			if (collectionType.equals(mRadioPartial.getText().toString())) {
				mRadioPartial.setChecked(true);
			} else {
				mRadioFull.setChecked(true);
				edtInputAmt.setEnabled(false);
			}
		}
		radiobuttonevent();
		String ZERO = "0";
		custName.setText(dpClc.getCustomerName() != null ? dpClc
				.getCustomerName() : ZERO);

		ccyCode.setText(dpClc.getCcyCode() != null ? dpClc.getCcyCode() : ZERO);
		acRefNo.setText(dpClc.getCbsAcRefNo() != null ? dpClc.getCbsAcRefNo()
				: ZERO);
		custId.setText(dpClc.getCustomerId() != null ? dpClc.getCustomerId()
				: ZERO);
		agentBalance.setText(dpClc.getAgentBal() != null ? dpClc.getAgentBal()
				: ZERO);
		edtInputAmt.setText(dpClc.getAmtSettled() != null ? dpClc
				.getAmtSettled() : "");

		agendaAmt.setText(dpClc.getAgendaAmt() != null ? dpClc.getAgendaAmt()
				: "");
		agentComponentName.setText(dpClc.getComponent() != null ? dpClc
				.getComponent() : "");

	}

	

	private void onVerifyView() {
		BaseActivity.mTitle.setText(R.string.screen_collection_verify);
		enablePagerAndIndicator(false);
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		onVerifyLoadWidgets();
	}

	private void onVerifyLoadWidgets() {
		mView = mLayoutInflater.inflate(R.layout.deposit_verify_collection,
				null);
		TextView ccyCode = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_ccy_code_val);
		TextView acRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_cbs_ac_ref_no_val);
		TextView agentId = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_cbs_agent_id_val);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_customer_id_val);
		TextView custName = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_col_custname);
		TextView componentName = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_component_name_val);
		TextView agendaAmt = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_agenda_amount_val);
		TextView indicatory = (TextView) mView
				.findViewById(R.id.txv_depo_vfy_full_partial_indicator_val);
		TextView settleAmount = (TextView) mView
				.findViewById(R.id.edt_depo_vfy_input_amount_val);
		TextView narrative = (TextView) mView
				.findViewById(R.id.edt_depo_vfy_narrative_val);
		AgendaMaster agendaCollections = CommonContexts.SELECTED_CLC;
		acRefNo.setText(agendaCollections.getCbsAcRefNo());
		agentId.setText(agendaCollections.getAgentId());
		custId.setText(agendaCollections.getCustomerId());
		custName.setText(agendaCollections.getCustomerName());
		componentName.setText(agendaCollections.getComponent());
		ccyCode.setText(agendaCollections.getCcyCode());
		agendaAmt.setText(agendaCollections.getAgendaAmt());
		indicatory.setText(agendaCollections.getFullPartialIndic());
		settleAmount.setText(agendaCollections.getAmtSettled());
		narrative.setText(agendaCollections.getNarrative());
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

	private void onConfirmView() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_PRINT;
		BaseActivity.mTitle.setText(R.string.screen_collection_print);
		BaseActivity.mBtnRight.setImageResource(R.drawable.print);
		BaseActivity.mBtnRight.setTag(R.drawable.print);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.imgsearch);
		BaseActivity.mBtnLeft.setTag(R.drawable.imgsearch);
		onConfirmLoadWidgets();
	}

	private void onConfirmLoadWidgets() {
		mView = mLayoutInflater.inflate(R.layout.deposit_confirm_collection,
				null);
		TextView txnNo = (TextView) mView
				.findViewById(R.id.txv_depo_print_mbs_txn_no);
		TextView acRefNo = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_col_cbs_ac_ref_no);
		TextView agentId = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_agent_id);
		TextView custId = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_customer_id);

		TextView componentName = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_component_name);
		TextView ccyCode = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_currency_code);
		TextView txnDateandtime = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_txndate_time_val);
		/*
		 * TextView agendaAmt = (TextView) mView
		 * .findViewById(R.id.txv_depo_cnf_agend_amount);
		 */
		TextView settleAmount = (TextView) mView
				.findViewById(R.id.txv_depo_cnf_settle_amount);
		AgendaMaster agendaCollections = CommonContexts.SELECTED_CLC;
		custId.setText(agendaCollections.getCustomerId());
		ccyCode.setText(agendaCollections.getCcyCode());
		txnNo.setText(CommonContexts.TXNID);
		acRefNo.setText(agendaCollections.getCbsAcRefNo());
		agentId.setText(agendaCollections.getAgentId());
		txnDateandtime.setText(CommonContexts.simpleDateTimeFormat
				.format(DateUtil.getCurrentDataTime()));
		custName.setText(agendaCollections.getCustomerName());
		componentName.setText(agendaCollections.getComponent());
		agendaAmt.setText(agendaCollections.getAgendaAmt());
		settleAmount.setText(agendaCollections.getAmtSettled());
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
						.equalsIgnoreCase(CommonContexts.SCREEN_CL_ENTRY)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_STATE_VERIFY)) {

					// go back to entry screen
					handleBackToEntryView();
				}
			} else if (ids == R.drawable.cancel) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_CL_ENTRY)) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_ENTRY)) {

						handleBackToAgendaView(false);

					}else {

						handleBackToAgendaView(false);
					}
				}
			}
			else if (ids == R.drawable.imgsearch) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_CL_ENTRY)) {
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
					.equalsIgnoreCase(CommonContexts.SCREEN_CL_ENTRY)) {
				if (id == R.drawable.save) {
					handleSaveForVerification();
				} else if (id == R.drawable.verify) {
					handleConfirmation();
				} else if (id == R.drawable.print) {
					if(CommonContexts.USE_EXTERNAL_DEVICE)
					{
						int noOfPrint = CommonContexts.NOOFPRINTS == 0?1:CommonContexts.NOOFPRINTS;
						PrinterActivity pa = new PrinterActivity();
						pa.printTxns(noOfPrint,"COL", CommonContexts.SELECTED_CLC, mContext);
					}
					handleBackToAgendaView(true);
				}
			}
			break;
		}

	}

	private void handleConfirmation() {
		
		TxnMaster clcTxn = extractObjectFromWidgets();
		TransactionService clcService = ServiceFactory.getTxnService();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00114));

			long txnStatus = clcService.addTxn(clcTxn);
			TransactionDao txnDao = DaoFactory.getTxnDao();
			long status = txnDao.updateAgenda(clcTxn, txnStatus);
			long statusCash = txnDao.insertCashRecord(cashRecord(), clcTxn,
					status);
			if (statusCash != -1 && status != 0 && txnStatus != -1) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.MFB00017),
						Toast.LENGTH_SHORT).show();
				MessageBuilderUtil mbu = new MessageBuilderUtil();
				String smsMessage = mbu.buildMessage(CommonContexts.SELECTED_CLC.getCustomerId(), clcTxn.getTxnCode(), clcTxn);
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
		TxnMaster clc = new TxnMaster();
		AgendaMaster dpclc = CommonContexts.SELECTED_CLC;
		clc.setModuleCode(dpclc.getModuleCode());
		clc.setCustomerName(dpclc.getCustomerName());
		clc.setTxnCode(dpclc.getTxnCode());
		clc.setAgendaId(dpclc.getAgendaId());
		clc.setSeqNo(dpclc.getSeqNo());
		clc.setCbsAcRefNo(dpclc.getCbsAcRefNo());
		clc.setBrnCode(dpclc.getBranchCode());
		clc.setCustomerId(dpclc.getCustomerId());
		clc.setInitTime(DateUtil.getCurrentDataTime());
		clc.setAgentId(dpclc.getAgentId());
		clc.setDeviceId(dpclc.getDeviceId());
		clc.setLocationCode(dpclc.getLocationCode());
		clc.setCcyCode(dpclc.getCcyCode());
		clc.setTxnAmt(dpclc.getAgendaAmt());
		clc.setSettledAmt(dpclc.getAmtSettled());
		clc.setFullPartInd(statusFlag);
		clc.setAgendaStatus("1");
		clc.setAccountType("CL");
		clc.setIsSynched("0");
		clc.setTxnStatus(0);
		clc.setTxnErrorCode("null");
		clc.setTxnErrorMsg("null");
		clc.setGenerateRevr("X");
		clc.setMbsSeqNo("0");
		clc.setGeneratedSms("N");
		clc.setAgendaCmpStartDate(dpclc.getCmpStDate());
		clc.setParentCbsRefAcNo(dpclc.getParentCbsRefNo());
		clc.setSmsMobileNo(dpclc.getPhno());
		return clc;
	}

	private void handleSaveForVerification() {
		String clcAmt = null;
		if (edtInputAmt.getText() != null
				&& !(edtInputAmt.getText().equals(""))) {
			clcAmt = edtInputAmt.getText().toString();

		}
		if (clcAmt != null && !(clcAmt.equals("")) && collectionType != null
				&& !(collectionType.equals(""))) {

			if ((Double.parseDouble(CommonContexts.SELECTED_CLC.getAgendaAmt())) >= Double
					.parseDouble(clcAmt)&& Double.parseDouble(clcAmt) >= CommonContexts
							.getPartialAmount(CommonContexts.SELECTED_CLC
									.getAgendaAmt())) {
				// if ((Double.parseDouble(CommonContexts.SELECTED_CLC
				// .getAgendaAmt()) - Double.parseDouble(mAmount
				// .toString())) == Double.parseDouble(clcAmt)) {
				// status = "1";
				// } else {
				// status = "0";
				// }
				CommonContexts.SELECTED_CLC
						.setAmtSettled(edtInputAmt.getText() != null ? String
								.valueOf(Double.parseDouble(edtInputAmt
										.getText().toString())) : null);
				CommonContexts.SELECTED_CLC
						.setNarrative(edtNarrative.getText() != null ? edtNarrative
								.getText().toString() : null);
				CommonContexts.SELECTED_CLC.setFullPartialIndic(collectionType);

				CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
				//EGA-MN15-000011
				String value = String.valueOf(Double.parseDouble(edtInputAmt.getText().toString()));
				NumberToWords.speakText(value,BaseActivity.ttsp);
				onVerifyView();

			} else
				ViewUtil.showCrutonError(mContext, R.string.install_amt_err,
						handler);
		} else {
			ViewUtil.showCrutonError(mContext, R.string.MFB00014, handler);
		}
	}

	private void radiobuttonevent() {
		listener = new OnClickListener() {

			@Override
			public void onClick(View view) {

				boolean checked = ((RadioButton) view).isChecked();
				switch (view.getId()) {
				case R.id.radbtn_deposit_entry_collection_partial:
					if (checked)
						edtInputAmt.setText("");
					edtInputAmt.setEnabled(true);
					mRadioPartial.setChecked(true);
					collectionType = mRadioPartial.getText().toString();
					calculatePartialAmt();
					statusFlag = "P";
					minAmt.setVisibility(View.VISIBLE);
					break;
				case R.id.radbtn_deposit_entry_collection_full:
					if (checked) {
						
						selectFullRadio();
					}
					break;
				default:
					ViewUtil.showCrutonError(mContext, R.string.clc_type_check,
							handler);
				}
			}
		};
	}

	
	private void calculatePartialAmt() {
		if (CommonContexts.SELECTED_CLC != null) {
			if (CommonContexts.ALLOW_PARTIAL.equalsIgnoreCase("Y")) {
				String amt = "("
						+ "min: "
						+ String.valueOf(CommonContexts
								.getPartialAmount(CommonContexts.SELECTED_CLC
										.getAgendaAmt())) + ")";
				minAmt.setText(amt);
			} else if (CommonContexts.ALLOW_PARTIAL.equalsIgnoreCase("N")) {
				CommonContexts.ShowAlertDialog(mContext);
				selectFullRadio();
			}
		}
	}
	
	private void selectFullRadio() {
		
		if (CommonContexts.SELECTED_CLC != null) {

			edtInputAmt.setEnabled(false);
			mRadioFull.setChecked(true);
			edtInputAmt
					.setText(CommonContexts.SELECTED_CLC
							.getAgendaAmt() != null ? String.valueOf((Double
							.parseDouble(CommonContexts.SELECTED_CLC
									.getAgendaAmt())))
							: "");
			collectionType = mRadioFull.getText().toString();
			statusFlag = "F";
		}
		minAmt.setVisibility(View.INVISIBLE);

	}
	private void handleBackToAgendaView(boolean reload) {
		BaseActivity.mTitle.setText(R.string.screen_collection_agenda);
		enablePagerAndIndicator(true);
		clearFields();
		if (reload) {
			Intent cash = new Intent(mContext, DpCollectionView.class);
			startActivity(cash);
			getActivity().finish();
		} else {
			mPager.setCurrentItem(0);
		}
	}

	private void handleBackToEntryView() {
		BaseActivity.mTitle.setText(R.string.screen_collection_entry);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		CommonContexts.SELECTED_CLC.setAmtSettled(edtInputAmt.getText()
				.toString());

		onSaveView();
		enablePagerAndIndicator(true);
		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);
	}

	public static void clearFields() {
		if (acRefNo != null)
			acRefNo.setText("");
		if (custName != null)
			custName.setText("");
		if (ccyCode != null)
			ccyCode.setText("");
		if (custId != null)
			custId.setText("");
		if (agemtId != null)
			agemtId.setText("");
		if (dsbType != null)
			dsbType.setText("");
		if (agentComponentName != null)
			agentComponentName.setText("");
		if (agendaAmt != null)
			agendaAmt.setText("");
		if (fullPartialIndi != null)
			fullPartialIndi.setText("");

		if (edtInputAmt != null)
			edtInputAmt.setText("");
		collectionType = null;
		CommonContexts.SELECTED_CLC = null;
		mRadioPartial.setChecked(false);
		mRadioFull.setChecked(false);

	}

	private void enablePagerAndIndicator(boolean status) {
		mPager.setEnabled(status);
		mPageIndicator.setEnabled(status);
	}

	public void myOnKeyDown() {

		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_CL_ENTRY)) {
			if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
				// go back to entry screen with the same values entered by
				// user
				handleBackToEntryView();
			} else if (CommonContexts.CURRENT_VIEW
					.equalsIgnoreCase(VIEW_STATE_ENTRY)) {
				// cancel disbursement entry and go back to the agenda view
				// without reloading (false) the agenda screen
				handleBackToAgendaView(false);
			} else {
				CommonContexts.SELECTED_CLC = null;
				handleBackToAgendaView(false);
				// go back to agenda with reloading (true) as the
				// transaction is committed already

			}
		}

	}
}