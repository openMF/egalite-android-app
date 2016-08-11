package com.bfsi.egalite.view;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.adapters.AgendaRecordsAdapter;
import com.bfsi.egalite.adapters.EnrolRecordsAdapter;
import com.bfsi.egalite.adapters.TxnRecordsAdapter;
import com.bfsi.egalite.dao.CashPositionDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CcyCodes;
import com.bfsi.egalite.entity.TxnCount;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.util.CommonContexts;

/**
 * 
 * @author Administrator
 * 
 */
@SuppressLint("HandlerLeak")
public class DashboardView extends BaseActivity {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	@SuppressWarnings("unused")
	private ExpandableListView mCashListView;
	private TextView mTxtTopUp, mTxtOpenbal, mTxtTopDown, mTxtDr, mTxtCr,
			mTxtClosingBal,mTxtCashlimit;
	View.OnTouchListener gestureListener;
	private List<TxnCount> mAgnCountList,mTxnCountList,mEnrlCountList;
	private ListView mRecordList,mRecordTxnList,mRecordEnrlList;

	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.INVISIBLE);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBtnLeft.setImageResource(R.drawable.back);
		mBtnLeft.setTag(R.drawable.back);
		mBtnRight.setImageResource(R.drawable.logoutselector);
		mBtnRight.setTag(R.drawable.logoutselector);
		getAgnData();
		getTxnData();
		getEnrollData();
		display();
		CommonContexts.dismissProgressDialog();

	}

	@Override
	protected void onLeftAction() {
		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (id == R.drawable.back) {
			Intent intent = new Intent(DashboardView.this, HomeView.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onRightAction() {

		Object tag = BaseActivity.mBtnRight.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (id == R.drawable.logoutselector) {
			doExit();
		}

	}

	public void doExit() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				DashboardView.this);

		alertDialog.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(DashboardView.this,
								LoginView.class);
						startActivity(intent);
						finish();
					}
				});
		alertDialog.setNegativeButton(R.string.no, null);
		alertDialog.setMessage(R.string.exit);
		alertDialog.setTitle(R.string.exit_title);
		alertDialog.show();
	}

	public void onClick(View v) {

	}

	/**
	 * Displaying all widgets
	 */
	private void display() {
		View mView = getLayoutInflater().inflate(R.layout.home_layout, null);
		TextView welcomeagent = (TextView) mView
				.findViewById(R.id.txv_home_lay_welcome_agent);
		welcomeagent.setText(CommonContexts.mLOGINVALIDATION.getfName());
		
		// BaseActivity.mTitle.setText(CommonContexts.mLOGINVALIDATION
		// .getfName());
		BaseActivity.mTitle.setText(getString(R.string.dashboard));
		

		Spinner mCcySpinner = (Spinner) mView
				.findViewById(R.id.spinner_ccy_val);

		List<String> list = ccyList();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_base_layout, list);
		dataAdapter.setDropDownViewResource(R.layout.dropdown_spinner);
		mCcySpinner
				.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		mCcySpinner.setAdapter(dataAdapter);
		mTxtOpenbal = (TextView) mView.findViewById(R.id.txv_opningbal_val);
		mTxtTopUp = (TextView) mView.findViewById(R.id.txv_topup_val);
		mTxtTopDown = (TextView) mView.findViewById(R.id.txv_topdown_val);
		mTxtDr = (TextView) mView.findViewById(R.id.txv_totaldr_val);
		mTxtCr = (TextView) mView.findViewById(R.id.txv_totalcr_val);
		mTxtClosingBal = (TextView) mView.findViewById(R.id.txv_closing_val);
		mTxtCashlimit = (TextView) mView.findViewById(R.id.txv_cashlimit_val);
		mRecordList = (ListView) mView.findViewById(R.id.list_dashbord_agenda);
		mRecordTxnList= (ListView) mView.findViewById(R.id.list_dashbord_txn);
		mRecordEnrlList= (ListView) mView.findViewById(R.id.list_dashbord_enltxn);
	
		if (mAgnCountList != null) {
			AgendaRecordsAdapter paidAdpters = new AgendaRecordsAdapter(
					DashboardView.this, mAgnCountList);
			mRecordList.setAdapter(paidAdpters);
		}
		if (mTxnCountList != null) {
			TxnRecordsAdapter paidAdpters = new TxnRecordsAdapter(
					DashboardView.this, mTxnCountList);
			mRecordTxnList.setAdapter(paidAdpters);
		}
		if (mEnrlCountList != null) {
			EnrolRecordsAdapter enrlAdpters = new EnrolRecordsAdapter(
					DashboardView.this, mEnrlCountList);
			mRecordEnrlList.setAdapter(enrlAdpters);
		}
		// getCashListView(dummy_cash_view);
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	private class CustomOnItemSelectedListener implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			CashPosition cashDetail = null;
			try {
				LOG.debug(DashboardView.this.getResources().getString(
						R.string.MFB00101));
				cashDetail = CommonContexts.cashData(
						parent.getItemAtPosition(pos).toString(),
						CommonContexts.mLOGINVALIDATION.getAgentId());
			} catch (ServiceException e) {
				LOG.error(
						DashboardView.this.getResources().getString(
								R.string.MFB00101)
								+ e.getMessage(), e);

				Toast.makeText(
						DashboardView.this,
						DashboardView.this.getResources().getString(
								R.string.MFB00101), Toast.LENGTH_SHORT).show();
			}
			mTxtOpenbal.setText(String.valueOf(cashDetail.getOpeningBal()));
			mTxtTopUp.setText(String.valueOf(cashDetail.getTopUp()));
			mTxtTopDown.setText(String.valueOf(cashDetail.getTopDown()));
			mTxtDr.setText(String.valueOf(cashDetail.getDebitAmt()));
			mTxtCr.setText(String.valueOf(cashDetail.getCreditAmt()));
			mTxtClosingBal.setText(String.valueOf(cashDetail.getOpeningBal()
					+ cashDetail.getTopUp() + cashDetail.getCreditAmt()
					- (cashDetail.getDebitAmt() + cashDetail.getTopDown())));
			
			mTxtCashlimit.setText(String.valueOf(CommonContexts.mLOGINVALIDATION.getCashLimit()));
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

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
			if(i.getCcyCode() != null)
			{
				if (!(i.getCcyCode().equalsIgnoreCase("")))
					dateData.add(i.getCcyCode());
			}
		}
		return dateData;
	}

	private void getAgnData() {
		CashPositionDao cashPosDao = DaoFactory.getCashDao();
		try {
			mAgnCountList = cashPosDao.getAgnData();

		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	private void getTxnData() {
		CashPositionDao cashPosDao = DaoFactory.getCashDao();
		try {
			mTxnCountList = cashPosDao.getTxnData();

		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	private void getEnrollData() {
		CashPositionDao cashPosDao = DaoFactory.getCashDao();
		try {
			mEnrlCountList = cashPosDao.getEnrlData();

		} catch (DataAccessException e) {
			System.out.println(e);

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Getting cash position data from running cash position table
	 */
	public List<CashPosition> getCashData() {
		List<CashPosition> cashpositionArrayList = null;
		CashPositionDao cpda = DaoFactory.getCashDao();
		try {
			LOG.debug(this.getResources().getString(R.string.MFB00111));
			// cashpositionArrayList = cpda.getTxnData();
		} catch (DataAccessException e) {
			LOG.error(
					DashboardView.this.getResources().getString(
							R.string.MFB00111)
							+ e.getMessage(), e);
			Toast.makeText(
					DashboardView.this,
					DashboardView.this.getResources().getString(
							R.string.MFB00111), Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					DashboardView.this.getResources().getString(
							R.string.MFB00111)
							+ e.getMessage(), e);
			Toast.makeText(
					DashboardView.this,
					DashboardView.this.getResources().getString(
							R.string.MFB00111), Toast.LENGTH_SHORT).show();
		}

		return cashpositionArrayList;
	}

	private void getCashListView(View dummy_cash_view) {
		// mCashListView = (ExpandableListView) dummy_cash_view
		// .findViewById(R.id.list_home_lay);
	}

	/*
	 * @Override public void onBackPressed() { doExit(); }
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
	 * 
	 * if (keyCode == KeyEvent.KEYCODE_BACK) { doExit(); }
	 * 
	 * return super.onKeyDown(keyCode, event); }
	 * 
	 * public void doExit() {
	 * 
	 * AlertDialog.Builder alertDialog = new AlertDialog.Builder(
	 * DashboardView.this);
	 * 
	 * alertDialog.setPositiveButton(getString(R.string.yes), new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * finish(); } }); alertDialog.setNegativeButton(R.string.no, null);
	 * alertDialog.setMessage(R.string.exit);
	 * alertDialog.setTitle(R.string.exit_title); alertDialog.show(); }
	 */

	@Override
	public void onBackPressed() {
		myOnKeyDown();

	}

	public void myOnKeyDown() {

		Intent intent = new Intent(DashboardView.this, HomeView.class);
		startActivity(intent);
		finish();
	}

}
