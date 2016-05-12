package com.bfsi.egalite.view.loan.repayment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.bfsi.egalite.view.LoanRepayView;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CashRecord;
import com.bfsi.mfi.rest.model.LoanPaidSch;

@SuppressWarnings("unused")
public class RepayEntry extends Fragment implements CommandListener {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private static final String VIEW_STATE_PRINT = "repayprint";
	private static final String VIEW_STATE_VERIFY = "repayVerify";
	private static final String VIEW_STATE_ENTRY = "repayEntry";
	private static TextView txnNo, txtAcRefNo, txtAgentId, txtCustId,
			txtCustName, txtCcyCode, txtCmpName, txtAgendaAmt,txtAgentBal, txtindicator,minAmt;
	private static EditText edtInputAmt, edtNarrative;
	private static ImageView mViewInfo;
	private static ViewGroup mViewGroup;
	private static TabPageIndicator mPageIndicator;
	private View mViewDetails;
	private BfsiViewPager mPager;
	private LayoutInflater mLayoutInflater = null;
	private Context mContext;
	private OnClickListener listener;
	private static RadioButton mRadioPartial, mRadioFull;
	private String mRepayType;
	private Double mAmount;
	private String mFlagFullPartial;
	private static String mFullPartial;
	private List<CustomerDetail> mCustDetailList;
	private List<LoanPaidSch> mRepayLoanPaidList;
	private List<GroupLoans> mGroupLoansList;
	private AgnLoanDetail mLoansList;
	@SuppressWarnings("static-access")
	public static RepayEntry newInstance(TabPageIndicator indicator,
			BfsiViewPager pager) {
		RepayEntry fragment = new RepayEntry();
		fragment.mPager = pager;
		fragment.mPageIndicator = indicator;
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
		mViewDetails = mLayoutInflater.inflate(R.layout.repay_entry, null);
		onSaveView();
		mViewGroup = new LinearLayout(getActivity());
		mViewGroup.addView(mViewDetails);
		return mViewGroup;
	}

	@SuppressLint("HandlerLeak")
	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.INVISIBLE);
			}

		}
	};

	private void onSaveView() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_ENTRY;
		mViewDetails = mLayoutInflater.inflate(R.layout.repay_entry, null);
		onSaveLoadWidgets();

		if (CommonContexts.SELECTED_REPAY != null) {
			if (mFullPartial != null) {
				if (mFullPartial.equals(mRadioPartial.getText().toString())) {
					mRadioPartial.setChecked(true);
				}

				else {
					mRadioFull.setChecked(true);
					edtInputAmt.setEnabled(false);
				}
			}
			radiobuttonevent();
			AgendaMaster rpyData = CommonContexts.SELECTED_REPAY;
			
			CashPosition cashDetail = null;
			try {
				cashDetail = CommonContexts.cashData(rpyData.getCcyCode(), CommonContexts.mLOGINVALIDATION.getAgentId());
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
			CommonContexts.SELECTED_REPAY.setAgentBal(agendaBal);
			
			txtAcRefNo.setText(rpyData.getCbsAcRefNo());
			txtCustId.setText(rpyData.getCustomerId());
			txtCustName.setText(rpyData.getCustomerName());
			txtCcyCode.setText(rpyData.getCcyCode());
			txtAgendaAmt.setText(rpyData.getAgendaAmt());
			// txtDsbType.setText(rpyData.getTxnCode());
			txtCmpName.setText(rpyData.getComponent());
			txtAgentBal.setText(rpyData.getAgentBal());
			edtInputAmt.setText(rpyData.getAmtSettled() != null ? rpyData
					.getAmtSettled() : "");
			radiobuttonevent();
			// validation();
		}

	}

	
	
	private CashRecord cashRecord() {
		AgendaMaster cash = CommonContexts.SELECTED_REPAY;
		CashRecord cashRec = new CashRecord();
		cashRec.setAuthStatus("A");
		cashRec.setEntrySeqNo(BaseTransactionService.generateEntrySequence(mContext));
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
	
	

	private void onSaveLoadWidgets() {
		txtAcRefNo = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_entry_cbs_ac_refno_val);
		txtCustId = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_entry_custid);
		txtCustName = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_entry_customername);

		txtCcyCode = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_entry_ccy_code_val);
		txtAgendaAmt = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_entry_agenda_amt_val);
		// txtDsbType = (TextView) mViewDetails
		// .findViewById(R.id.txv_repay_entry_disb_type_val);
		edtInputAmt = (EditText) mViewDetails
				.findViewById(R.id.edt_repay_entry_input_amount_val);
		edtNarrative = (EditText) mViewDetails
				.findViewById(R.id.edt_repay_entry_narrative_val);
		txtCmpName = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_entry_componet_name_val);
		txtAgentBal = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_entry_agent_bal_val);
		minAmt = (TextView) mViewDetails.findViewById(R.id.txv_minamt);
		radiobuttonevent();
		mRadioPartial = (RadioButton) mViewDetails
				.findViewById(R.id.radbtn_repay_entry_red_partial);

		mRadioFull = (RadioButton) mViewDetails
				.findViewById(R.id.radbtn_repay_entry_red_full);

		mRadioPartial.setOnClickListener(listener);
		mRadioFull.setOnClickListener(listener);
		popUpLpg();
		LinearLayout mLinLay = (LinearLayout) mViewDetails
				.findViewById(R.id.linlay_customer);
		mLinLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (CommonContexts.SELECTED_REPAY != null) {
					getCustDetails();
					CommonContexts.showCustDetailsDialog(mContext,
							mCustDetailList, mLayoutInflater);
				}
			}
		});
		LinearLayout mlinNarr = (LinearLayout) mViewDetails
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}

		edtInputAmt.setTypeface(CommonContexts.getTypeface(mContext));

	}

	private void getCustDetails() {
		CustomerDao clcDao = DaoFactory.getCustomerDao();
		try {
			mCustDetailList = clcDao.readCustInfo(CommonContexts.SELECTED_REPAY
					.getCustomerId());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void popUpLpg() {
		LinearLayout mLinLoan = (LinearLayout) mViewDetails
				.findViewById(R.id.linlay_entry_loan);
		LinearLayout mLinPaidLoan = (LinearLayout) mViewDetails
				.findViewById(R.id.linlay_entry_paidloan);
		LinearLayout mLinGroups = (LinearLayout) mViewDetails
				.findViewById(R.id.linlay_entry_groups);
		mLinLoan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonContexts.SELECTED_REPAY != null) {
					getLoanDetails();
					CommonContexts.showLoanDialog(mContext,
							mLoansList, mLayoutInflater);
				}
			}
		});
		mLinPaidLoan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonContexts.SELECTED_REPAY != null) {
					getPaidSheduls();
					if(!mRepayLoanPaidList.isEmpty())
					CommonContexts.showPaidShedulsDialog(mContext,
							mRepayLoanPaidList, mLayoutInflater);
				}

			}
		});
		mLinGroups.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (CommonContexts.SELECTED_REPAY != null) {
					getGroupLoans();
					if(!mGroupLoansList.isEmpty())
					CommonContexts.showGroupLoanialog(mContext,
							mGroupLoansList, mLayoutInflater);

				}

			}
		});

	}
	private void getLoanDetails() {
		LoansDao dsbDao = DaoFactory.getLoanDao();
		try {
			mLoansList = dsbDao
					.getLoanDetails(CommonContexts.SELECTED_REPAY
							.getCbsAcRefNo());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	private void getGroupLoans() {
		LoansDao dsbDao = DaoFactory.getLoanDao();
		try {
			mGroupLoansList = dsbDao.getGroupLoans(CommonContexts.SELECTED_REPAY
					.getCbsAcRefNo(),CommonContexts.SELECTED_REPAY.getCustomerId());
		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void getPaidSheduls() {
		LoansDao dsbDao = DaoFactory.getLoanDao();
		try {
			mRepayLoanPaidList = dsbDao
					.getLoanPaidSchedule(CommonContexts.SELECTED_REPAY
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
				case R.id.radbtn_repay_entry_red_partial:
					if (checked)
						edtInputAmt.setEnabled(true);
					edtInputAmt.setText("");
					mRadioPartial.setChecked(true);
					mFullPartial = mRadioPartial.getText().toString();
					calculatePartialAmt();
					mFlagFullPartial = "P";
					minAmt.setVisibility(View.VISIBLE);
					break;
				case R.id.radbtn_repay_entry_red_full:
					if (checked) {
						if (CommonContexts.SELECTED_REPAY != null) {
							
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
	
	private void calculatePartialAmt() {
		if (CommonContexts.SELECTED_REPAY != null) {
			if (CommonContexts.ALLOW_PARTIAL.equalsIgnoreCase("Y")) {
				String amt = "("
						+ "min: "
						+ String.valueOf(CommonContexts
								.getPartialAmount(CommonContexts.SELECTED_REPAY
										.getAgendaAmt())) + ")";
				minAmt.setText(amt);
			} else if (CommonContexts.ALLOW_PARTIAL.equalsIgnoreCase("N")){
				CommonContexts.ShowAlertDialog(mContext);
				selectFullRadio();
			}
		}
	}
	
	private void selectFullRadio() {
		
		edtInputAmt.setText(String.valueOf(Double
				.parseDouble(CommonContexts.SELECTED_REPAY
						.getAgendaAmt())));
		edtInputAmt.setEnabled(false);
		mRadioFull.setChecked(true);
		mFullPartial = mRadioFull.getText().toString();
		mFlagFullPartial = "F";
		minAmt.setVisibility(View.INVISIBLE);
	}

	private void validation() {
		mAmount = 0.0;
		if (CommonContexts.SELECTED_REPAY.agendaStatus != null) {
			if (CommonContexts.SELECTED_REPAY.getAgendaStatus().toString() != null) {
				String accountNumber = CommonContexts.SELECTED_REPAY
						.getCbsAcRefNo().toString();
				if (CommonContexts.SELECTED_REPAY.getAgendaStatus().toString()
						.equalsIgnoreCase("0")) {

					TransactionDao rpyDao = DaoFactory.getTxnDao();
					List<TxnMaster> TxnIdDetails = null;
					try {
						LOG.debug(mContext.getResources().getString(
								R.string.MFB00106));
						TxnIdDetails = rpyDao.getTxnAmt(accountNumber);
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
					String amtDue = CommonContexts.SELECTED_REPAY
							.getAgendaAmt().concat("(Paid:")
							.concat(String.valueOf(mAmount)).concat(")");
					txtAgendaAmt.setText(amtDue);
				}

			}
		}
	}

	private void onConfirmView() {
		CommonContexts.CURRENT_VIEW = VIEW_STATE_PRINT;
		BaseActivity.mTitle.setText(R.string.screen_rpy_print);
		BaseActivity.mBtnRight.setImageResource(R.drawable.print);
		BaseActivity.mBtnRight.setTag(R.drawable.print);

		BaseActivity.mBtnLeft.setImageResource(R.drawable.imgsearch);
		BaseActivity.mBtnLeft.setTag(R.drawable.imgsearch);
		onConfirmLoadWidgets();

	}

	private void onConfirmLoadWidgets() {
		mViewDetails = mLayoutInflater.inflate(R.layout.repay_confirm, null);
		TextView txtaccRefno = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_print_ac_ref_no_val);
		TextView txtTxnno = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_print_txn_no_val);
		TextView txtagentId = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_print_agent_id_val);
		TextView custId = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_print_customer_id_val);
		TextView txtcurrency = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_print_ccy_code_val);
		TextView txtcomponentName = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_print_component_val);

		TextView txtInputAmount = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_print_input_amt_val);
		TextView txtTxnDateTime = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_print_txn_datetime_val);

		AgendaMaster agendaLoans = CommonContexts.SELECTED_REPAY;
		txtTxnno.setText(CommonContexts.TXNID);
		txtaccRefno.setText(agendaLoans.getCbsAcRefNo());
		custId.setText(agendaLoans.getCustomerId());
		txtcomponentName.setText(agendaLoans.getComponent());
		txtagentId.setText(agendaLoans.getAgentId());
		txtInputAmount.setText(agendaLoans.getAmtSettled());
		txtcurrency.setText(agendaLoans.getCcyCode());
		txtTxnDateTime.setText(CommonContexts.simpleDateTimeFormat
				.format(DateUtil.getCurrentDataTime()));
		mViewGroup.removeAllViews();
		mViewGroup.addView(mViewDetails);
	}

	@Override
	public View getView() {
		return super.getView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		((BaseActivity) this.getActivity()).registerCommandListener(this);
	}

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			Object tags = BaseActivity.mBtnLeft.getTag();
			int ids = tags == null ? -1 : (Integer) tags;
			if (ids == R.drawable.back) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_RP_ENTRY)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
					// go back to entry screen with the same values
					// entered by
					// user
					handleBackToEntryView();
				}
			} else if (ids == R.drawable.cancel) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_RP_ENTRY)) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_ENTRY)) {
						// cancel disbursement entry and go back to the
						// agenda view
						// without reloading the agenda screen
						handleBackToHomeView();

					}  else {
						CommonContexts.SELECTED_REPAY = null;
						// firstTime onCancel - for now back to agenda
						// without
						// reloading
						// handleBackToHomeView();
						handleBackToAgendaView(true);

					}
				}
			}else if (ids == R.drawable.imgsearch) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_RP_ENTRY)) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_STATE_PRINT)) {
						// cancel disbursement entry and go back to the
						// agenda view
						// with reloading the agenda screen
						handleBackToAgendaView(true);
					} else {
						CommonContexts.SELECTED_REPAY = null;
						// firstTime onCancel - for now back to agenda
						// without
						// reloading
						// handleBackToHomeView();
						handleBackToAgendaView(true);

					}
				}
			}
			break;
		case CMD_RIGHT_ACTION:
			Object tag = BaseActivity.mBtnRight.getTag();
			int id = tag == null ? -1 : (Integer) tag;
			if (CommonContexts.CURRENT_SCREEN
					.equalsIgnoreCase(CommonContexts.SCREEN_RP_ENTRY)) {
				if (id == R.drawable.save) {

					handleSaveForVerification();

				} else if (id == R.drawable.verify) {

					handleRepayConfirmation();

				} else if (id == R.drawable.print) {

					if(CommonContexts.USE_EXTERNAL_DEVICE)
					{
						int noOfPrint = CommonContexts.NOOFPRINTS == 0?1:CommonContexts.NOOFPRINTS;
						PrinterActivity pa = new PrinterActivity();
						pa.printTxns(noOfPrint,"REPAY", CommonContexts.SELECTED_REPAY,
								mContext);
					}
					
					handleBackToAgendaView(true);
				}
			}
			break;
		}
	}

	private void handleRepayConfirmation() {
		
		TxnMaster loanTxn = extractLoanRepayObjectFromWidgets();
		TransactionService repayService = ServiceFactory.getTxnService();
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00114));
		
			long txnStatus = repayService.addTxn(loanTxn);
			TransactionDao txnDao = DaoFactory.getTxnDao();
			long status = txnDao.updateAgenda(loanTxn, txnStatus);
			 long statusCash = txnDao.insertCashRecord(cashRecord(),
			 loanTxn, status);

			if ( statusCash != -1 && status != 0 && txnStatus != -1) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.MFB00017),
						Toast.LENGTH_SHORT).show();
				
				MessageBuilderUtil mbu = new MessageBuilderUtil();
				String smsMessage = mbu.buildMessage(CommonContexts.SELECTED_REPAY.getCustomerId(), loanTxn.getTxnCode(), loanTxn);
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

	private TxnMaster extractLoanRepayObjectFromWidgets() {

		// Need to set the values to SELECTED_REPAY before assigning to
		AgendaMaster agendaLoans = CommonContexts.SELECTED_REPAY;
		TxnMaster loanTxn = new TxnMaster();
		loanTxn.setCustomerName(agendaLoans.getCustomerName());
		loanTxn.setModuleCode(agendaLoans.getModuleCode());
		loanTxn.setTxnCode(agendaLoans.getTxnCode());
		loanTxn.setAgendaId(agendaLoans.getAgendaId());
		loanTxn.setSeqNo(agendaLoans.getSeqNo());
		loanTxn.setCbsAcRefNo(agendaLoans.getCbsAcRefNo());
		loanTxn.setBrnCode(agendaLoans.getBranchCode());
		loanTxn.setCustomerId(agendaLoans.getCustomerId());
		loanTxn.setInitTime(DateUtil.getCurrentDataTime());
		loanTxn.setAgentId(agendaLoans.getAgentId());
		loanTxn.setDeviceId(agendaLoans.getDeviceId());
		loanTxn.setLocationCode(agendaLoans.getLocationCode());
		loanTxn.setLnIsFutureSch(agendaLoans.getLnIsFutureSch());
		loanTxn.setCcyCode(agendaLoans.getCcyCode());
		loanTxn.setTxnAmt(agendaLoans.getAgendaAmt());
		loanTxn.setSettledAmt(agendaLoans.getAmtSettled());
		loanTxn.setFullPartInd(mFlagFullPartial);
		loanTxn.setAgendaStatus("1");
		loanTxn.setAccountType("RP");
		loanTxn.setIsSynched("0");
		loanTxn.setTxnStatus(0);
		loanTxn.setTxnErrorCode("null");
		loanTxn.setTxnErrorMsg("null");
		loanTxn.setGenerateRevr("X");
		loanTxn.setMbsSeqNo("0");
		loanTxn.setGeneratedSms("N");
		loanTxn.setIsGroupLoan(agendaLoans.getIsGroupLoan());
		loanTxn.setLcyCode(agendaLoans.getLcyCode());
		loanTxn.setAgendaCmpStartDate(agendaLoans.getCmpStDate());
		loanTxn.setParentCbsRefAcNo(agendaLoans.getParentCbsRefNo());
		loanTxn.setSmsMobileNo(agendaLoans.getPhno());
		return loanTxn;
	}

	private void handlePager(boolean type) {
		mPager.setEnabled(type);
		mPageIndicator.setEnabled(type);
	}

	private void handleSaveForVerification() {

		String narative = null, disp_amt = null;
		if (edtInputAmt.getText() != null
				&& !(edtInputAmt.getText().equals(""))) {
			disp_amt = edtInputAmt.getText().toString();

		}

		if (disp_amt != null && !(disp_amt.equals("")) && mFullPartial != null
				&& !(mFullPartial.equals(""))) {

			if ((Double.parseDouble(CommonContexts.SELECTED_REPAY
					.getAgendaAmt())) >= Double.parseDouble(disp_amt)&& Double.parseDouble(disp_amt) >= CommonContexts
							.getPartialAmount(CommonContexts.SELECTED_REPAY
									.getAgendaAmt())) {

				CommonContexts.SELECTED_REPAY.setAmtSettled(String
						.valueOf(Double.parseDouble(edtInputAmt.getText()
								.toString())));
				CommonContexts.SELECTED_REPAY.setNarrative(edtNarrative
						.getText().toString());
				CommonContexts.SELECTED_REPAY.setFullPartialIndic(mFullPartial);
				CommonContexts.CURRENT_VIEW = VIEW_STATE_VERIFY;
				//EGA-MN15-000011
				String value = String.valueOf(Double.parseDouble(edtInputAmt.getText().toString()));
				NumberToWords.speakText(value, BaseActivity.ttsp);
				onVerifyView();

			} else
				ViewUtil.showCrutonError(mContext, R.string.install_amt_err,
						handler);
			
		} else {
			ViewUtil.showCrutonError(mContext, R.string.MFB00014, handler);
		}

	}

	private void onVerifyView() {
		BaseActivity.mTitle.setText(R.string.screen_rpy_verify);
		enablePagerAndIndicator(false);
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		onVerifyLoadWidgets();
	}

	private void enablePagerAndIndicator(boolean status) {
		mPager.setEnabled(status);
		mPageIndicator.setEnabled(status);
	}

	private void onVerifyLoadWidgets() {
		mViewDetails = mLayoutInflater.inflate(R.layout.repay_verify, null);

		TextView txtaccRefno = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_cbs_ac_ref_ac_val);
		TextView txtAgentId = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_agent_id_val);
		TextView custId = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_customer_id_val);
		TextView txtcustName = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_customername);
		TextView txtcurrency = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_ccy_code_val);
		TextView txtcomponentName = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_component_val);
		TextView txtagendaAmt = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_agenda_amt_val);
		// TextView disbType = (TextView) mViewDetails
		// .findViewById(R.id.txv_repay_verify_disb_val);
		TextView fullPartialIndic = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_full_partial_indicator_val);
		TextView txtNarrative = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_narrative_val);

		TextView txtInputAmt = (TextView) mViewDetails
				.findViewById(R.id.txv_repay_verify_input_amount_val);

		AgendaMaster agendaloans = CommonContexts.SELECTED_REPAY;
		txtaccRefno.setText(agendaloans.getCbsAcRefNo());
		txtAgentId.setText(agendaloans.getAgentId());
		custId.setText(agendaloans.getCustomerId());
		txtcustName.setText(agendaloans.getCustomerName());
		txtcurrency.setText(agendaloans.getCcyCode());
		txtcomponentName.setText(agendaloans.getComponent());
		txtagendaAmt.setText(agendaloans.getAgendaAmt());
		txtNarrative.setText(agendaloans.getNarrative());
		// disbType.setText(agendaloans.getTxnCode());
		fullPartialIndic.setText(agendaloans.getFullPartialIndic());
		txtInputAmt.setText(agendaloans.getAmtSettled());
		LinearLayout mlinNarr = (LinearLayout) mViewDetails
				.findViewById(R.id.linlay_narrative);
		if (AppParams.CAN_HAV_TXN_NATV.equalsIgnoreCase("Y")) {
			mlinNarr.setVisibility(View.VISIBLE);
		} else {
			mlinNarr.setVisibility(View.GONE);
		}

		mViewGroup.removeAllViews();
		mViewGroup.addView(mViewDetails);
	}

	private void handleBackToHomeView() {
		handlePager(true);
		mPager.setCurrentItem(0);
		clearFields();
	}

	private void handleBackToEntryView() {
		BaseActivity.mTitle.setText(R.string.screen_rpy_entry);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		mViewGroup.removeAllViews();
		AgendaMaster repayData = CommonContexts.SELECTED_REPAY;
		repayData.setAmtSettled(edtInputAmt.getText().toString());

		onSaveView();
		handlePager(true);
		mViewGroup.addView(mViewDetails);
	}

	public static void clearFields() {
		if (txtCmpName != null)
			txtCustName.setText("");
		if (txtCustId != null)
			txtCustId.setText("");
		if (txnNo != null)
			txnNo.setText("");
		if (txtAcRefNo != null)
			txtAcRefNo.setText("");
		if (txtAgentId != null)
			txtAgentId.setText("");
		if (edtInputAmt != null)
			edtInputAmt.setText("");

		if (txtCcyCode != null)
			txtCcyCode.setText(" ");
		if (txtCmpName != null)
			txtCmpName.setText(" ");
		if (txtAgendaAmt != null)
			txtAgendaAmt.setText(" ");
		if (txtCcyCode != null)
			txtCcyCode.setText(" ");
		if (txtindicator != null)
			txtindicator.setText(" ");
		mFullPartial = null;
		if (mViewInfo != null)
			mViewInfo.setVisibility(View.INVISIBLE);
		mRadioPartial.setChecked(false);
		mRadioFull.setChecked(false);
		mFullPartial = null;
		CommonContexts.SELECTED_REPAY = null;

	}

	public Bitmap getBitmapOfView(View v) {
		View rootview = v.getRootView();
		rootview.setDrawingCacheEnabled(true);
		Bitmap bmp = rootview.getDrawingCache();
		return bmp;
	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_RP_ENTRY)) {
			if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_STATE_VERIFY)) {
				// go back to entry screen with the same values entered by
				// user
				handleBackToEntryView();
			} else if (CommonContexts.CURRENT_VIEW
					.equalsIgnoreCase(VIEW_STATE_ENTRY)) {
				// cancel disbursement entry and go back to the agenda view
				// without reloading the agenda screen
				handleBackToAgendaView(false);

			} else if (CommonContexts.CURRENT_VIEW
					.equalsIgnoreCase(VIEW_STATE_PRINT)) {
				// cancel disbursement entry and go back to the agenda view
				// with reloading the agenda screen
				handleBackToAgendaView(true);

			} else {
				CommonContexts.SELECTED_REPAY = null;
				// firstTime onCancel - for now back to agenda without
				// reloading
				handleBackToAgendaView(false);
			}
		}
	}

	private void handleBackToAgendaView(boolean reload) {
		BaseActivity.mTitle.setText(R.string.screen_rpy_agenda);
		enablePagerAndIndicator(true);
		clearFields();
		if (reload) {
			Intent cash = new Intent(mContext, LoanRepayView.class);
			startActivity(cash);
			getActivity().finish();
		} else {
			mPager.setCurrentItem(0);
		}
	}
}
