/**
 * This source IS part OF the EGALITE - Agent Banking Solution System AND IS copyrighted by © 2014 bfsi software consulting pvt.ltd
 * © 2014 bfsi software consulting pvt.ltd - All rights reserved.
 * No part of this work may be reproduced, stored in a retrieval system, or transmitted in any form or by any means,
 * electronic, mechanical, photocopying, recording or otherwise, 
 * without the prior written permission of bfsi software consulting pvt.ltd 
 */

package com.bfsi.egalite.view.deposit.payments;

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

import com.bfsi.egalite.adapters.PaymentAgndaListAdapter;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.DepositsDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.R;

public class PaymentAgenda extends Fragment implements CommandListener {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private boolean mAscendingOrder[] = { true, true, true };
	private Context mContext;
	private BfsiViewPager mPager;
	private ListView mListView;
	private LinearLayout mCustomerName, mLoanAcNo, mAmountDue;
	private ImageView mNameArrow, mAcNoArrow, mAmountDueArrow;
	private View mView;
	private EditText mSearchBox;
	private List<AgendaMaster> mpayList = new ArrayList<AgendaMaster>();
	private PaymentAgndaListAdapter mlistAdapter;

	public static PaymentAgenda newInstance(Context context,
			BfsiViewPager pager) {
		PaymentAgenda fragment = new PaymentAgenda();
		fragment.mPager = pager;
		fragment.mContext = context;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		((BaseActivity) this.getActivity()).unregisterCommandListener(this);
		((BaseActivity) this.getActivity()).registerCommandListener(this);
		mView = inflater.inflate(R.layout.deposit_agenda_maturity_pay, null);
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		loadWidgetRefs();
		initWidgetStates();
		attachListeners();
	}

	private void loadWidgetRefs() {
		mListView = (ListView) mView.findViewById(R.id.list_all);
		mSearchBox = (EditText) mView.findViewById(R.id.edt_search_all);
		mSearchBox.setTypeface(CommonContexts.getTypeface(mContext));
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
	}

	private void initWidgetStates() {
		mSearchBox.setEnabled(false);
		agendaList();
	}

	private void agendaList() {
		DepositsDao clcDao = DaoFactory.getDepositDao();
		try {
			
			LOG.debug(mContext.getResources().getString(R.string.MFB00105));
			mpayList = clcDao.readAgendaPayments();
			
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

		mlistAdapter = new PaymentAgndaListAdapter(mContext, mpayList,mListView, mPager);
		mListView.setAdapter(mlistAdapter);
		mSearchBox.setEnabled(true);
	}

	private void attachListeners() {
		mSearchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1 && mlistAdapter != null) {
					mlistAdapter.getFilter().filter(s);
				} else {
					if (mpayList != null && mpayList.size() > 0) {
						mlistAdapter = new PaymentAgndaListAdapter(mContext,
								mpayList,mListView, mPager);
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
		mAmountDue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amountDueOnclickAction();
			}
		});
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