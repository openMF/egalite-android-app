package com.bfsi.egalite.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.HistoryDao;
import com.bfsi.egalite.dao.sqlite.HistoryDataAccess;
import com.bfsi.egalite.entity.HistoryDetails;
import com.bfsi.egalite.evolute.PrinterActivity;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;

public class HistoryView extends BaseActivity {

	private static final String VIEW_HISTORY = "HISTORY_VIEW";
	private static final String VIEW_HISTORY_DETAILS = "HISTORY_VIEW_DETAILS";
	private Context mContext;
	private LinearLayout mCustomerId, mTxnId, mAmount, mCustName, mEnrollId,
			mContctno;
	private boolean mAscendingOrder[] = { true, true, true };
	public BfsiViewPager mPager;
	private ImageView mCustIdArrow, mTxnIdArrow, mAmountArrow, mCustNameArrow,
			mEnrollIdArrow, mContactNoArrow;
	private HistoryViewAdapter mlistHistoryAdapter;
	private EnrollmentViewAdapter mlistEnrollAdapter;
	private ListView mHistListView, mEnrollListView;
	private EditText mSearchHistoryBox, mSearchEnrollBox;
	private List<HistoryDetails> mListHistory;
	private List<HistoryDetails> mListEnroll;
	private OnClickListener listener;
	private LinearLayout mLinHistory, mLinEnroll;
	private RadioButton mRadAgenda, mRadEnroll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		mBtnLeft.setTag(R.drawable.back);
		mBtnLeft.setImageResource(R.drawable.back);
		CommonContexts.mMfi = ((MfiApplication) getApplicationContext());

		loadWidgets();
		CommonContexts.dismissProgressDialog();
	}

	private void loadWidgets() {
		mTitle.setText(getString(R.string.History));
		CommonContexts.CURRENT_VIEW = VIEW_HISTORY;
		View mView = getLayoutInflater().inflate(R.layout.historyview, null);

		mHistListView = (ListView) mView.findViewById(R.id.list_history_view);
		mEnrollListView = (ListView) mView
				.findViewById(R.id.list_historyenroll_view);
		mSearchHistoryBox = (EditText) mView.findViewById(R.id.edt_search_all);
		mSearchEnrollBox = (EditText) mView.findViewById(R.id.edt_search_enroll);
		mCustomerId = (LinearLayout) mView
				.findViewById(R.id.linlay_hist_custid);
		mTxnId = (LinearLayout) mView.findViewById(R.id.linlay_hist_txnid);
		mAmount = (LinearLayout) mView.findViewById(R.id.linlay_hist_amount);

		mCustName = (LinearLayout) mView
				.findViewById(R.id.linlay_hist_custname);
		mEnrollId = (LinearLayout) mView
				.findViewById(R.id.linlay_hist_enrollid);
		mContctno = (LinearLayout) mView
				.findViewById(R.id.linlay_hist_contactno);

		mCustIdArrow = (ImageView) mView.findViewById(R.id.img_custidarrow);
		mTxnIdArrow = (ImageView) mView.findViewById(R.id.img_txnidarrow);
		mAmountArrow = (ImageView) mView.findViewById(R.id.img_amtarrow);

		mCustNameArrow = (ImageView) mView.findViewById(R.id.img_custnamearrow);
		mEnrollIdArrow = (ImageView) mView.findViewById(R.id.img_enrollidarrow);
		mContactNoArrow = (ImageView) mView
				.findViewById(R.id.img_contactnoarrow);

		mRadAgenda = (RadioButton) mView.findViewById(R.id.radio_agenda);
		mRadEnroll = (RadioButton) mView.findViewById(R.id.radio_enrollment);
		mLinHistory = (LinearLayout) mView
				.findViewById(R.id.linlay_history_layout);
		mLinEnroll = (LinearLayout) mView
				.findViewById(R.id.linlay_enrollment_layout);
		implimentaionHistory();
		implimentaionEnroll();

		CommonContexts.deviceDimension(mContext);
		final LayoutParams lparams = new LayoutParams(CommonContexts.WIDTH,
				LayoutParams.WRAP_CONTENT);
		mCustomerId.setLayoutParams(lparams);
		mTxnId.setLayoutParams(lparams);
		mAmount.setLayoutParams(lparams);
		mCustName.setLayoutParams(lparams);
		mEnrollId.setLayoutParams(lparams);
		mContctno.setLayoutParams(lparams);
		radioEvent();
		mRadAgenda.setOnClickListener(listener);
		mRadEnroll.setOnClickListener(listener);

		mSearchHistoryBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1 && mlistHistoryAdapter != null) {
					mlistHistoryAdapter.getFilter().filter(s);
				} else {
					if (mListHistory != null && mListHistory.size() > 0) {
						mlistHistoryAdapter = new HistoryViewAdapter(mContext,
								mListHistory, mHistListView);
						mHistListView.setAdapter(mlistHistoryAdapter);
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

		mSearchEnrollBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1 && mlistEnrollAdapter != null) {
					mlistEnrollAdapter.getFilter().filter(s);
				} else {
					if (mListEnroll != null && mListEnroll.size() > 0) {
						mlistEnrollAdapter = new EnrollmentViewAdapter(
								mContext, mListEnroll, mEnrollListView);
						mEnrollListView.setAdapter(mlistEnrollAdapter);
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

		mCustomerId.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				customerIdOnclickAction();
			}
		});

		mTxnId.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				txnIdOnclickAction();
			}
		});
		mAmount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amountDueOnclickAction();
			}
		});

		mCustName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				customerNameOnclickAction();
			}
		});

		mEnrollId.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				enrollIdOnclickAction();
			}
		});
		mContctno.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				contactNoOnclickAction();
			}
		});

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	private void implimentaionHistory() {
		HistoryDao hd = new HistoryDataAccess();
		mListHistory = hd.readHistoryData();
		mlistHistoryAdapter = new HistoryViewAdapter(mContext, mListHistory,
				mHistListView);
		mHistListView.setAdapter(mlistHistoryAdapter);

	}

	private void implimentaionEnroll() {
		HistoryDao hd = new HistoryDataAccess();
		mListEnroll = hd.readHistoryEnrollment();
		mlistEnrollAdapter = new EnrollmentViewAdapter(mContext, mListEnroll,
				mEnrollListView);
		mEnrollListView.setAdapter(mlistEnrollAdapter);

	}

	private void radioEvent() {
		listener = new OnClickListener() {

			@Override
			public void onClick(View view) {

				boolean checked = ((RadioButton) view).isChecked();
				switch (view.getId()) {
				case R.id.radio_agenda:
					if (checked)
						mLinHistory.setVisibility(View.VISIBLE);
					mLinEnroll.setVisibility(View.GONE);
					mRadAgenda.setChecked(true);
					mRadEnroll.setChecked(false);
					CommonContexts.HISTORYCHECK = "HISTORY";

					break;

				case R.id.radio_enrollment:
					if (checked)
						mLinHistory.setVisibility(View.GONE);
					mLinEnroll.setVisibility(View.VISIBLE);
					mRadAgenda.setChecked(false);
					mRadEnroll.setChecked(true);
					CommonContexts.HISTORYCHECK = "ENROLL";
					break;

				}
			}
		};

		if (CommonContexts.HISTORYCHECK.equalsIgnoreCase(Constants.HISTORY)) {
			mRadAgenda.setChecked(true);
			mLinHistory.setVisibility(View.VISIBLE);
			mLinEnroll.setVisibility(View.GONE);

		}

		if (CommonContexts.HISTORYCHECK.equalsIgnoreCase(Constants.ENROLL)) {
			mRadEnroll.setChecked(true);
			mLinHistory.setVisibility(View.GONE);
			mLinEnroll.setVisibility(View.VISIBLE);

		}

	}

	private void customerIdOnclickAction() {
		if (mAscendingOrder[0]) {
			mAscendingOrder[0] = false;
			mlistHistoryAdapter.sortCustIdAsc(false);
			mCustIdArrow.setImageResource(R.drawable.arrowup);

		} else {
			mAscendingOrder[0] = true;
			mlistHistoryAdapter.sortCustIdAsc(true);
			mCustIdArrow.setImageResource(R.drawable.arrowdown);
		}

		mCustIdArrow.setVisibility(View.VISIBLE);
		mTxnIdArrow.setVisibility(View.GONE);
		mTxnIdArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mAmountArrow.setVisibility(View.GONE);
		mAmountArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[1] = true;
		mAscendingOrder[2] = true;
	}

	private void txnIdOnclickAction() {
		if (mAscendingOrder[1]) {
			mAscendingOrder[1] = false;
			mlistHistoryAdapter.sortByTxnIdAsc(false);
			mTxnIdArrow.setImageResource(R.drawable.arrowup);
		} else {
			mAscendingOrder[1] = true;
			mlistHistoryAdapter.sortByTxnIdAsc(true);
			mTxnIdArrow.setImageResource(R.drawable.arrowdown);
		}

		mTxnIdArrow.setVisibility(View.VISIBLE);
		mTxnIdArrow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mCustIdArrow.setVisibility(View.GONE);
		mAmountArrow.setVisibility(View.GONE);
		mAmountArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[0] = false;
		mAscendingOrder[2] = true;
	}

	private void amountDueOnclickAction() {
		if (mAscendingOrder[2]) {
			mAscendingOrder[2] = false;
			mlistHistoryAdapter.sortByAmtAsc(false);
			mAmountArrow.setImageResource(R.drawable.arrowup);

		} else {
			mAscendingOrder[2] = true;
			mlistHistoryAdapter.sortByAmtAsc(true);
			mAmountArrow.setImageResource(R.drawable.arrowdown);
		}
		mAmountArrow.setVisibility(View.VISIBLE);
		mAmountArrow.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mCustIdArrow.setVisibility(View.GONE);
		mTxnIdArrow.setVisibility(View.GONE);
		mTxnIdArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mAscendingOrder[0] = false;
		mAscendingOrder[1] = false;
	}

	private void customerNameOnclickAction() {
		if (mAscendingOrder[0]) {
			mAscendingOrder[0] = false;
			mlistEnrollAdapter.sortCustNameAsc(false);
			mCustNameArrow.setImageResource(R.drawable.arrowup);

		} else {
			mAscendingOrder[0] = true;
			mlistEnrollAdapter.sortCustNameAsc(true);
			mCustNameArrow.setImageResource(R.drawable.arrowdown);
		}

		mCustNameArrow.setVisibility(View.VISIBLE);
		mEnrollIdArrow.setVisibility(View.GONE);
		mEnrollIdArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mContactNoArrow.setVisibility(View.GONE);
		mContactNoArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[1] = true;
		mAscendingOrder[2] = true;
	}

	private void enrollIdOnclickAction() {
		if (mAscendingOrder[1]) {
			mAscendingOrder[1] = false;
			mlistEnrollAdapter.sortByEnrollIdAsc(false);
			mEnrollIdArrow.setImageResource(R.drawable.arrowup);
		} else {
			mAscendingOrder[1] = true;
			mlistEnrollAdapter.sortByEnrollIdAsc(true);
			mEnrollIdArrow.setImageResource(R.drawable.arrowdown);
		}

		mEnrollIdArrow.setVisibility(View.VISIBLE);
		mEnrollIdArrow.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mCustNameArrow.setVisibility(View.GONE);
		mContactNoArrow.setVisibility(View.GONE);
		mContactNoArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[0] = false;
		mAscendingOrder[2] = true;
	}

	private void contactNoOnclickAction() {
		if (mAscendingOrder[2]) {
			mAscendingOrder[2] = false;
			mlistEnrollAdapter.sortByContactNoAsc(false);
			mContactNoArrow.setImageResource(R.drawable.arrowup);

		} else {
			mAscendingOrder[2] = true;
			mlistEnrollAdapter.sortByContactNoAsc(true);
			mContactNoArrow.setImageResource(R.drawable.arrowdown);
		}
		mContactNoArrow.setVisibility(View.VISIBLE);
		mContactNoArrow.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mCustNameArrow.setVisibility(View.GONE);
		mEnrollIdArrow.setVisibility(View.GONE);
		mEnrollIdArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mAscendingOrder[0] = false;
		mAscendingOrder[1] = false;
	}

	private class HistoryViewAdapter extends BaseAdapter implements Filterable {
		private Context mContext;
		private List<HistoryDetails> mHistoryList;
		private List<HistoryDetails> mDisplayedList;
		private LayoutInflater mLayoutInflater;
		private HistoryViewAdapter mlistAdapter;
		private ListView mListView;
		private HolderFilter mHolderFilter;

		public HistoryViewAdapter(Context context,
				List<HistoryDetails> histDetails, ListView listView) {
			mContext = context;
			mHistoryList = histDetails;
			mDisplayedList = mHistoryList;
			mListView = listView;
			mLayoutInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return mDisplayedList.size();
		}

		@Override
		public Object getItem(int position) {
			return mDisplayedList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		private class ViewHolder {
			TextView mCustId, mTxnId, mAmount;
			RelativeLayout mRelLay;
			View colorView;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = new View(mContext);
				convertView = mLayoutInflater.inflate(
						R.layout.historyview_listitem, null);
				holder = new ViewHolder();
				holder.mCustId = (TextView) convertView
						.findViewById(R.id.txv_hist_custid);
				holder.mTxnId = (TextView) convertView
						.findViewById(R.id.txv_hist_txnid);
				holder.mAmount = (TextView) convertView
						.findViewById(R.id.txv_hist_amount);
				holder.mRelLay = (RelativeLayout) convertView
						.findViewById(R.id.rellay_histview_listitem);
				holder.colorView = (View)convertView.findViewById(R.id.view_color_history);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CommonContexts.setThemes(position, convertView);
			holder.mCustId
					.setText(mDisplayedList.get(position).getCustomerId());
			holder.mTxnId.setText(mDisplayedList.get(position).getTxnId());
			holder.mAmount.setText(mDisplayedList.get(position).getTxnAmt());

			if (mDisplayedList.get(position).getIsSynced() != null) {
				if (mDisplayedList.get(position).getIsSynced()
						.equalsIgnoreCase("1"))
					holder.colorView.setBackgroundResource(R.drawable.red);
				else
					holder.colorView.setBackgroundResource(R.drawable.blue);
			}
			
			
			holder.mRelLay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onDetailsView(position);
				}

				private void onDetailsView(final int position) {
					CommonContexts.CURRENT_VIEW = VIEW_HISTORY_DETAILS;
					View mView = getLayoutInflater().inflate(
							R.layout.historydetails, null);
					mTitle.setText(getString(R.string.HistoryDetails));

					TextView mCustId = (TextView) mView
							.findViewById(R.id.txt_hist_det_custid);
					TextView mTxnId = (TextView) mView
							.findViewById(R.id.txt_hist_det_txnid);
					TextView mTxnAmt = (TextView) mView
							.findViewById(R.id.txt_hist_det_txnamt);
					TextView mTxnTime = (TextView) mView
							.findViewById(R.id.txt_hist_det_txntime);
					TextView mTxnType = (TextView) mView
							.findViewById(R.id.txt_hist_det_txntype);
//					TextView mNarrative = (TextView) mView
//							.findViewById(R.id.txt_hist_det_narrative);

					mCustId.setText(mDisplayedList.get(position)
							.getCustomerId());
					mTxnId.setText(mDisplayedList.get(position).getTxnId());
					mTxnAmt.setText(mDisplayedList.get(position).getTxnAmt());
					mTxnType.setText(mDisplayedList.get(position).getTxnType());
					mTxnTime.setText(CommonContexts.simpleDateTimeFormat.format(new Date(Long.valueOf(mDisplayedList.get(position).getTxnTime()))));
//					mNarrative.setText(mDisplayedList.get(position)
//							.getNarrative());
					final HistoryDetails hd = new HistoryDetails();
					hd.setCustomerId(mCustId.getText().toString());
					hd.setTxnId(mTxnId.getText().toString());
					hd.setTxnAmt(mTxnAmt.getText().toString());
					hd.setTxnType(mTxnType.getText().toString());
					hd.setTxnTime(mTxnTime.getText().toString());
					
					Button print = (Button)mView.findViewById(R.id.id_print);
					print.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							
							if(CommonContexts.USE_EXTERNAL_DEVICE)
							{
								PrinterActivity pa = new PrinterActivity();
								pa.printHistory(1,"TXNS", hd,mContext);
							}else{
								Toast.makeText(HistoryView.this, "Sorry !! You can't access the USB Printer", Toast.LENGTH_SHORT).show();
							}
							
						}
					});
					mMiddleFrame.removeAllViews();
					mMiddleFrame.addView(mView);
				}

			});

			return convertView;
		}

		@Override
		public Filter getFilter() {
			if (mHolderFilter == null) {
				mHolderFilter = new HolderFilter();
			}
			return mHolderFilter;
		}

		private void applySort(Comparator<HistoryDetails> comparator) {
			Collections.sort(mDisplayedList, comparator);
			mlistAdapter = new HistoryViewAdapter(mContext, mDisplayedList,
					mListView);
			mListView.setAdapter(mlistAdapter);
			mlistAdapter.notifyDataSetChanged();
		}

		public void sortCustIdAsc(boolean type) {

			final boolean types = type;
			Comparator<HistoryDetails> comparator = new Comparator<HistoryDetails>() {
				@Override
				public int compare(HistoryDetails object1,
						HistoryDetails object2) {
					if (object1.getCustomerId() != null
							&& object2.getCustomerId() != null) {
						if (types) {
							return object1.getCustomerId().compareToIgnoreCase(
									object2.getCustomerId());
						}

						else if (object1.getCustomerId() != null
								&& object2.getCustomerId() != null) {
							return object2.getCustomerId().compareToIgnoreCase(
									object1.getCustomerId());
						}
					}
					return 0;
				}
			};
			applySort(comparator);
		}

		public void sortByTxnIdAsc(boolean type) {
			final boolean types = type;
			Comparator<HistoryDetails> comparator = new Comparator<HistoryDetails>() {
				@Override
				public int compare(HistoryDetails object1,
						HistoryDetails object2) {
					if (object1.getTxnId() != null
							&& object2.getTxnId() != null) {
						if (types) {
							return object1.getTxnId().compareToIgnoreCase(
									object2.getTxnId());
						} else {
							return object2.getTxnId().compareToIgnoreCase(
									object1.getTxnId());
						}
					}
					return 0;
				}
			};
			applySort(comparator);

		}

		public void sortByAmtAsc(boolean type) {
			final boolean types = type;
			Comparator<HistoryDetails> comparator = new Comparator<HistoryDetails>() {
				@Override
				public int compare(HistoryDetails object1,
						HistoryDetails object2) {
					if (object1.getTxnAmt() != null
							&& object2.getTxnAmt() != null) {
						if (types) {

							return Double
									.valueOf(object1.getTxnAmt())
									.compareTo(
											Double.valueOf(object2.getTxnAmt()));
						} else {
							return Double
									.valueOf(object2.getTxnAmt())
									.compareTo(
											Double.valueOf(object1.getTxnAmt()));

						}
					}
					return 0;
				}
			};
			applySort(comparator);

		}

		// To filter the list based on search
		private class HolderFilter extends Filter {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();

				if (constraint == null ) {

					results.values = mDisplayedList;
					results.count = mDisplayedList.size();
				} else if (constraint != null && constraint.length() > 0) {
					List<HistoryDetails> nHolderList = new ArrayList<HistoryDetails>();
					constraint = constraint.toString().toUpperCase();
					for (HistoryDetails h : mHistoryList) {

						if ((h.customerId.toUpperCase().contains(constraint))
								|| (h.txnId.toUpperCase().contains(constraint))
								|| (h.txnAmt.toUpperCase().contains(constraint))) {
							nHolderList.add(h);
						}
					}
					results.values = nHolderList;
					results.count = nHolderList.size();
				}
				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				mDisplayedList = (ArrayList<HistoryDetails>) results.values;
				notifyDataSetChanged();
			}
		}

	}

	private class EnrollmentViewAdapter extends BaseAdapter implements
			Filterable {
		private Context mContext;
		private List<HistoryDetails> mEnrollList;
		private List<HistoryDetails> mDisplayedList;
		private LayoutInflater mLayoutInflater;
		private EnrollmentViewAdapter mlistEnrollAdapter;
		private ListView mListView;
		private HolderFilter mHolderFilter;

		public EnrollmentViewAdapter(Context context,
				List<HistoryDetails> enrollDetails, ListView listView) {
			mContext = context;
			mEnrollList = enrollDetails;
			mDisplayedList = mEnrollList;
			mListView = listView;
			mLayoutInflater = LayoutInflater.from(mContext);
		}

		@Override
		public int getCount() {
			return mDisplayedList.size();
		}

		@Override
		public Object getItem(int position) {
			return mDisplayedList.get(position);
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		private class ViewHolder {
			TextView mCustName, mEnrollId, mContNo;
			RelativeLayout mRelLay;
			View colorView;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = new View(mContext);
				convertView = mLayoutInflater.inflate(
						R.layout.historyenroll_listitem, null);
				holder = new ViewHolder();
				holder.mCustName = (TextView) convertView
						.findViewById(R.id.txv_hist_custname);
				holder.mEnrollId = (TextView) convertView
						.findViewById(R.id.txv_hist_enrollid);
				holder.mContNo = (TextView) convertView
						.findViewById(R.id.txv_hist_contactno);
				holder.mRelLay = (RelativeLayout) convertView
						.findViewById(R.id.rellay_histview_listitem);
				holder.colorView = (View)convertView.findViewById(R.id.view_color_history);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CommonContexts.setThemes(position, convertView);
			holder.mCustName.setText(mDisplayedList.get(position)
					.getCustomerName());
			holder.mEnrollId.setText(mDisplayedList.get(position).getTxnId());
			holder.mContNo.setText(mDisplayedList.get(position)
					.getContactNumber());

			if (mDisplayedList.get(position).getIsSynced() != null) {
				if (mDisplayedList.get(position).getIsSynced()
						.equalsIgnoreCase("1"))
					holder.colorView.setBackgroundResource(R.drawable.red);
				else
					holder.colorView.setBackgroundResource(R.drawable.blue);
			}
			
			holder.mRelLay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onDetailsEnrollView(position);
				}

				private void onDetailsEnrollView(final int position) {
					CommonContexts.CURRENT_VIEW = VIEW_HISTORY_DETAILS;
					View mView = getLayoutInflater().inflate(
							R.layout.historyenroll_details, null);
					mTitle.setText(getString(R.string.HistoryDetails));

					TextView mCustName = (TextView) mView
							.findViewById(R.id.txt_hist_det_custname);
					TextView mEnrollId = (TextView) mView
							.findViewById(R.id.txt_hist_det_enrollid);
					TextView mContactNo = (TextView) mView
							.findViewById(R.id.txt_hist_det_contno);
					TextView mDob = (TextView) mView
							.findViewById(R.id.txt_hist_det_dob);
					TextView mGendor = (TextView) mView
							.findViewById(R.id.txt_hist_det_gendor);
					TextView mTxnTime = (TextView) mView
							.findViewById(R.id.txt_hist_det_datetime);
					Button print = (Button)mView.findViewById(R.id.id_print);
					
					mCustName.setText(mDisplayedList.get(position)
							.getCustomerName());
					mEnrollId.setText(mDisplayedList.get(position).getTxnId());
					mContactNo.setText(mDisplayedList.get(position)
							.getContactNumber());
					
					
					mDob.setText(CommonContexts.dateFormat.format(new Date(Long.valueOf(mDisplayedList.get(position).getDob()))));
					mGendor.setText(mDisplayedList.get(position).getGender() != null? (mDisplayedList.get(position).getGender()
							.equals("1") ? "Male" : "Female"):"");
					
					mTxnTime.setText(mDisplayedList.get(position).getTxnTime()!= null ?(CommonContexts.simpleDateTimeFormat.format(new Date(Long.valueOf(mDisplayedList.get(position).getTxnTime())))):"");
					
					final HistoryDetails hd = new HistoryDetails();
					hd.setCustomerName(mCustName.getText().toString());
					hd.setTxnId(mEnrollId.getText().toString());
					hd.setContactNumber(mContactNo.getText().toString());
					hd.setDob(mDob.getText().toString());
					hd.setGender(mGendor.getText().toString());
					hd.setTxnTime(mTxnTime.getText().toString());
					
					print.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							if(CommonContexts.USE_EXTERNAL_DEVICE)
							{
								PrinterActivity pa = new PrinterActivity();
								pa.printHistory(1,"ENROLLS", hd,mContext);
							}else{
								Toast.makeText(HistoryView.this, "Sorry !! You can't access the USB Printer", Toast.LENGTH_SHORT).show();
							}
						}
					});
					
					mMiddleFrame.removeAllViews();
					mMiddleFrame.addView(mView);
				}
			});
			return convertView;
		}

		@Override
		public Filter getFilter() {
			if (mHolderFilter == null) {
				mHolderFilter = new HolderFilter();
			}
			return mHolderFilter;
		}

		private void applySort(Comparator<HistoryDetails> comparator) {
			Collections.sort(mDisplayedList, comparator);
			mlistEnrollAdapter = new EnrollmentViewAdapter(mContext,
					mDisplayedList, mListView);
			mListView.setAdapter(mlistEnrollAdapter);
			mlistEnrollAdapter.notifyDataSetChanged();
		}

		public void sortCustNameAsc(boolean type) {

			final boolean types = type;
			Comparator<HistoryDetails> comparator = new Comparator<HistoryDetails>() {
				@Override
				public int compare(HistoryDetails object1,
						HistoryDetails object2) {
					if (object1.getCustomerName() != null
							&& object2.getCustomerName() != null) {
						if (types) {
							return object1.getCustomerName()
									.compareToIgnoreCase(
											object2.getCustomerName());
						}

						else if (object1.getCustomerName() != null
								&& object2.getCustomerName() != null) {
							return object2.getCustomerName()
									.compareToIgnoreCase(
											object1.getCustomerName());
						}
					}
					return 0;
				}
			};
			applySort(comparator);
		}

		public void sortByEnrollIdAsc(boolean type) {
			final boolean types = type;
			Comparator<HistoryDetails> comparator = new Comparator<HistoryDetails>() {
				@Override
				public int compare(HistoryDetails object1,
						HistoryDetails object2) {
					if (object1.getTxnId() != null
							&& object2.getTxnId() != null) {
						if (types) {
							return object1.getTxnId().compareToIgnoreCase(
									object2.getTxnId());
						} else {
							return object2.getTxnId().compareToIgnoreCase(
									object1.getTxnId());
						}
					}
					return 0;
				}
			};
			applySort(comparator);

		}

		public void sortByContactNoAsc(boolean type) {
			final boolean types = type;
			Comparator<HistoryDetails> comparator = new Comparator<HistoryDetails>() {
				@Override
				public int compare(HistoryDetails object1,
						HistoryDetails object2) {
					if (object1.getContactNumber() != null
							&& object2.getContactNumber() != null) {
						if (types) {

							return object1.getContactNumber()
									.compareToIgnoreCase(
											object2.getContactNumber());
						} else {
							return object1.getContactNumber()
									.compareToIgnoreCase(
											object2.getContactNumber());

						}
					}
					return 0;
				}
			};
			applySort(comparator);

		}

		// To filter the list based on search
		private class HolderFilter extends Filter {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();

				if (constraint == null ) {

					results.values = mDisplayedList;
					results.count = mDisplayedList.size();
				} else if (constraint != null && constraint.length() > 0) {
					List<HistoryDetails> nHolderList = new ArrayList<HistoryDetails>();
					constraint = constraint.toString().toUpperCase();
					for (HistoryDetails h : mEnrollList) {

						if ((h.customerName.toUpperCase().contains(constraint))
								|| (h.txnId.toUpperCase().contains(constraint))
								|| (h.contactNumber.toUpperCase().contains(constraint))) {
							nHolderList.add(h);
						}
					}
					results.values = nHolderList;
					results.count = nHolderList.size();
				}
				return results;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				mDisplayedList = (ArrayList<HistoryDetails>) results.values;
				notifyDataSetChanged();
			}
		}

	}

	@Override
	protected void onLeftAction() {
		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (id == R.drawable.back) {
			if (CommonContexts.CURRENT_VIEW.equals(VIEW_HISTORY)) {
				Intent hist = new Intent(HistoryView.this, HomeView.class);
				startActivity(hist);
				finish();
			} else if (CommonContexts.CURRENT_VIEW.equals(VIEW_HISTORY_DETAILS)) {
				loadWidgets();
			}
		}

	}

	@Override
	protected void onRightAction() {

	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();

	}

	public void myOnKeyDown() {

		if (CommonContexts.CURRENT_VIEW.equals(VIEW_HISTORY)) {
			Intent hist = new Intent(HistoryView.this, HomeView.class);
			startActivity(hist);
			finish();
		} else if (CommonContexts.CURRENT_VIEW.equals(VIEW_HISTORY_DETAILS)) {
			loadWidgets();
		}
	}

}
