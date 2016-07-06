package com.bfsi.egalite.view.loan.disbursement;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.Bundle;
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

import com.bfsi.egalite.adapters.DsbAgendaListAdapter;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.LoansDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.TabPageIndicator;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.R;

public class DsbAgenda extends Fragment implements CommandListener {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private boolean mAscendingOrder[] = { true, true, true };
	private static final String VIEW_AGENDA = "agendaview";
	private DsbAgendaListAdapter mlistAdapter;
	private Context mContext;
	private BfsiViewPager mPager;
	private ListView mListView;
	private LinearLayout mCustomerName, mLoanAcNo, mAgendaAmt;
	private ImageView mNameArrow, mAcNoArrow, mAmountDueArrow;
	private View mView;
	private EditText mSearchBox;
	private List<AgendaMaster> mDsbList = new ArrayList<AgendaMaster>();
	

	public static DsbAgenda newInstance(Context context, BfsiViewPager pager,
			TabPageIndicator pageIndicatory) {
		DsbAgenda fragment = new DsbAgenda();
		fragment.mContext = context;
		fragment.mPager = pager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		((BaseActivity) this.getActivity()).unregisterCommandListener(this);
		((BaseActivity) this.getActivity()).registerCommandListener(this);
		mView = inflater.inflate(R.layout.dsb_agenda, null);
		CommonContexts.CURRENT_VIEW = VIEW_AGENDA;
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadWidgetRefs();
		initWidgetStates();
		attachListeners();
	}

	private void initWidgetStates() {
		mSearchBox.setEnabled(false);
		agendaList();
	}

	private void attachListeners() {
		mSearchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1 && mlistAdapter != null) {
					mlistAdapter.getFilter().filter(s);
				} else {
					if (mDsbList != null && mDsbList.size() > 0) {
						mlistAdapter = new DsbAgendaListAdapter(mContext, mDsbList,mListView,
								mPager);
						mListView.setAdapter(mlistAdapter);
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
		mCustomerName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				customerNameOnclickAction();
			}
		});

		mLoanAcNo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loanAccountNumberOnclickAction();
			}
		});
		mAgendaAmt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amountDueOnclickAction();
			}
		});
	}

	private void loadWidgetRefs() {
		mListView = (ListView) mView.findViewById(R.id.list_all);
		mSearchBox = (EditText) mView.findViewById(R.id.edt_search_all);
		mSearchBox.setTypeface(CommonContexts.getTypeface(mContext));
		mCustomerName = (LinearLayout) mView.findViewById(R.id.linlay_all_name);
		mLoanAcNo = (LinearLayout) mView.findViewById(R.id.linlay_all_accno);
		mAgendaAmt = (LinearLayout) mView.findViewById(R.id.linlay_all_amount);
		mNameArrow = (ImageView) mView.findViewById(R.id.img_all_namearrow);
		mAcNoArrow = (ImageView) mView.findViewById(R.id.img_all_accnoarrow);
		mAmountDueArrow = (ImageView) mView
				.findViewById(R.id.img_all_amountarrow);
		CommonContexts.deviceDimension(mContext);
		final LayoutParams lparams = new LayoutParams(CommonContexts.WIDTH,
				LayoutParams.WRAP_CONTENT);
		mCustomerName.setLayoutParams(lparams);
		mLoanAcNo.setLayoutParams(lparams);
		mAgendaAmt.setLayoutParams(lparams);
	}

	private void agendaList() {
		LoansDao dsbDao = DaoFactory.getLoanDao();
		try {

			LOG.debug(mContext.getResources().getString(R.string.MFB00105));
			mDsbList = dsbDao.readDsbAgenda();
			
		} catch (DataAccessException e) {
			LOG.error(mContext.getResources().getString(R.string.MFB00105)
					 + e.getMessage(), e);

			Toast.makeText(mContext,mContext.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();

		} catch (Exception e) {
			LOG.error(mContext.getResources().getString(R.string.MFB00105)
					 + e.getMessage(), e);

			Toast.makeText(mContext,mContext.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();
		}
		mlistAdapter = new DsbAgendaListAdapter(mContext, mDsbList,mListView,
				mPager);
		mListView.setAdapter(mlistAdapter);
		mSearchBox.setEnabled(true);
	}



	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			break;
		case CMD_RIGHT_ACTION:

			break;
		}

	}

	private void customerNameOnclickAction() {
		if (mAscendingOrder[0]) {
			mAscendingOrder[0] = false;
			mlistAdapter.sortByNameAsc(false);
			mNameArrow.setImageResource(R.drawable.arrowup);

		} else {
			mAscendingOrder[0] = true;
			mlistAdapter.sortByNameAsc(true);
			mNameArrow.setImageResource(R.drawable.arrowdown);
		}

		mNameArrow.setVisibility(View.VISIBLE);
		mAcNoArrow.setVisibility(View.GONE);
		mAcNoArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mAmountDueArrow.setVisibility(View.GONE);
		mAmountDueArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[1] = true;
		mAscendingOrder[2] = true;
	}

	private void loanAccountNumberOnclickAction() {
		if (mAscendingOrder[1]) {
			mAscendingOrder[1] = false;
			mlistAdapter.sortByAcNoAsc(false);
			mAcNoArrow.setImageResource(R.drawable.arrowup);
		} else {
			mAscendingOrder[1] = true;
			mlistAdapter.sortByAcNoAsc(true);
			mAcNoArrow.setImageResource(R.drawable.arrowdown);
		}

		mAcNoArrow.setVisibility(View.VISIBLE);
		mAcNoArrow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mNameArrow.setVisibility(View.GONE);
		mAmountDueArrow.setVisibility(View.GONE);
		mAmountDueArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[0] = false;
		mAscendingOrder[2] = true;
	}

	private void amountDueOnclickAction() {
		if (mAscendingOrder[2]) {
			mAscendingOrder[2] = false;
			mlistAdapter.sortByAmtAsc(false);
			mAmountDueArrow.setImageResource(R.drawable.arrowup);

		} else {
			mAscendingOrder[2] = true;
			mlistAdapter.sortByAmtAsc(true);
			mAmountDueArrow.setImageResource(R.drawable.arrowdown);
		}
		mAmountDueArrow.setVisibility(View.VISIBLE);
		mAmountDueArrow.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mNameArrow.setVisibility(View.GONE);
		mAcNoArrow.setVisibility(View.GONE);
		mAcNoArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mAscendingOrder[0] = false;
		mAscendingOrder[1] = false;
	}

}
