package com.bfsi.egalite.view.loanprepay;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.bfsi.egalite.adapters.PrepayAgendaListAdapter;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.LoansDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.R;


public class LoanPrepayAgenda extends Fragment implements CommandListener {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private PrepayAgendaListAdapter mRepayListAdapter;
	private List<AgendaMaster> mRepaymentList;
	private Context mContext;
	private BfsiViewPager mPager;
	private ListView mListView;
	private LinearLayout mCustomerName, mLoanAcNo, mAmountDue;
	private ImageView mNameArrow, mAcNoArrow, mAmountDueArrow;
	private EditText mSearchbox;
	private boolean mAscendingOrder[] = { true, true, true};

	public static LoanPrepayAgenda newInstance(BfsiViewPager pager
			) {
		LoanPrepayAgenda fragment = new LoanPrepayAgenda();
		fragment.mPager = pager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		((BaseActivity) this.getActivity()).unregisterCommandListener(this);
		((BaseActivity) this.getActivity()).registerCommandListener(this);
		View mView = inflater.inflate(R.layout.repay_agenda, null);
		doLoadInit(mView);
		return mView;
	}

	@SuppressLint("HandlerLeak")
	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};

	private void doLoadInit(View mView) {
		mListView = (ListView) mView.findViewById(R.id.list_all);
		mSearchbox = (EditText) mView.findViewById(R.id.edt_search_all);
		mSearchbox.setTypeface(CommonContexts.getTypeface(mContext));
		mSearchbox.setEnabled(false);
		agendaList();

		mSearchbox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1 && mRepayListAdapter != null) {
					mRepayListAdapter.getFilter().filter(s);
				} else {
					if (mRepaymentList != null && mRepaymentList.size() > 0) {
						mRepayListAdapter = new PrepayAgendaListAdapter(
								getActivity(), mRepaymentList, mListView,
								mPager);
						mListView.setAdapter(mRepayListAdapter);
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}
			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		mCustomerName = (LinearLayout) mView.findViewById(R.id.linlay_all_name);
		mLoanAcNo = (LinearLayout) mView.findViewById(R.id.linlay_all_accno);
		mAmountDue = (LinearLayout) mView.findViewById(R.id.linlay_all_amount);
		mNameArrow = (ImageView) mView.findViewById(R.id.img_all_namearrow);
		mAcNoArrow = (ImageView) mView.findViewById(R.id.img_all_accnoarrow);
		mAmountDueArrow = (ImageView) mView
				.findViewById(R.id.img_all_amountarrow);
		CommonContexts.deviceDimension(mContext);
		final LayoutParams lparams = new LayoutParams(CommonContexts.WIDTH,
				LayoutParams.WRAP_CONTENT);
		mCustomerName.setLayoutParams(lparams);
		mLoanAcNo.setLayoutParams(lparams);
		mAmountDue.setLayoutParams(lparams);
		mCustomerName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				customerNameOnclickAction();
			}
		});
		mLoanAcNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				accountNumberOnclickAction();
			}
		});
		mAmountDue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amountDueOnclickAction();
			}
		});
	}

	private void customerNameOnclickAction() {
		// Change the order of items based on price
		if (mAscendingOrder[0]) {
			// Show items descending
			mAscendingOrder[0] = false;
			mRepayListAdapter.sortByNameAsc(false);
			mNameArrow.setImageResource(R.drawable.arrowup);

		} else {
			// Show items ascending
			mAscendingOrder[0] = true;
			mRepayListAdapter.sortByNameAsc(true);
			mNameArrow.setImageResource(R.drawable.arrowdown);
		}
		mNameArrow.setVisibility(View.VISIBLE);
		mAcNoArrow.setVisibility(View.GONE);
		mAmountDueArrow.setVisibility(View.GONE);
		mAscendingOrder[1] = true;
		mAscendingOrder[2] = true;
	}

	private void accountNumberOnclickAction() {
		// Change the order of items based on price
		if (mAscendingOrder[1]) {
			// Show items descending
			mAscendingOrder[1] = false;
			mRepayListAdapter.sortByAcNoAsc(false);
			mAcNoArrow.setImageResource(R.drawable.arrowup);

		} else {
			// Show items ascending
			mAscendingOrder[1] = true;
			mRepayListAdapter.sortByAcNoAsc(true);
			mAcNoArrow.setImageResource(R.drawable.arrowdown);
		}
		// Make visible the arrow next to the price and
		// make the others
		// invisible
		mAcNoArrow.setVisibility(View.VISIBLE);
		mAcNoArrow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mNameArrow.setVisibility(View.GONE);
		mAmountDueArrow.setVisibility(View.GONE);
		mAscendingOrder[0] = false;
		mAscendingOrder[2] = true;
	}

	private void amountDueOnclickAction() {
		// Change the order of items based on price
		if (mAscendingOrder[2]) {
			// Show items descending
			mAscendingOrder[2] = false;
			mRepayListAdapter.sortByAmtAsc(false);
			mAmountDueArrow.setImageResource(R.drawable.arrowup);

		} else {
			// Show items ascending
			mAscendingOrder[2] = true;
			mRepayListAdapter.sortByAmtAsc(true);
			mAmountDueArrow.setImageResource(R.drawable.arrowdown);
		}
		// Make visible the arrow next to the price and
		// make the others
		// invisible
		mAmountDueArrow.setVisibility(View.VISIBLE);
		mAmountDueArrow.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mNameArrow.setVisibility(View.GONE);
		mAcNoArrow.setVisibility(View.GONE);
		mAscendingOrder[0] = false;
		mAscendingOrder[1] = false;
	}

	private void agendaList() {
		LoansDao rpyDao = DaoFactory.getLoanDao();

		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00105));
			mRepaymentList = rpyDao.prepayAgenda();
		} catch (DataAccessException e) {
			LOG.error(mContext.getResources().getString(R.string.MFB00105)
					 + e.getMessage(), e); 

			Toast.makeText(mContext,mContext.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();

		}
		mRepayListAdapter = new PrepayAgendaListAdapter(mContext,
				mRepaymentList, mListView, mPager);
		mRepayListAdapter.notifyDataSetChanged();
		mListView.setAdapter(mRepayListAdapter);
		mSearchbox.setEnabled(true);
	}

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			break;
		case CMD_RIGHT_ACTION:
			break;
		default:
			break;
		}

	}

}
