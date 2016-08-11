package com.bfsi.egalite.view.customers;

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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.dao.sqlite.CustomersDataAccess;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.HomeView;
import com.bfsi.egalite.view.KycOnlyView;
import com.bfsi.egalite.view.R;

public class CustomersAll extends BaseActivity {

	private static final String VIEW_CUST_ALL = "custmerall";
	private static final String VIEW_CUST_DETAILS = "custDetails";
	private Context mContext;
	private LinearLayout mNameLin, mCustIdLin, mTelPhLin;
	private boolean mAscendingOrder[] = { true, true, true };
	public BfsiViewPager mPager;
	private ImageView mNameArrow, mCustIdArrow, mTelePhArrow;
	private CustomerViewListAdapter mlistAdapter;
	private EditText mSearchBox;
	private List<CustomerDetail> mListCust;
	private ListView mCustList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
		mBtnLeft.setTag(R.drawable.back);
		mBtnLeft.setImageResource(R.drawable.back);
		
		loadWidgets();
		CommonContexts.dismissProgressDialog();
	}

	private void loadWidgets() {
		mTitle.setText(getString(R.string.CustomerList));
		CommonContexts.CURRENT_VIEW = VIEW_CUST_ALL;
		View mView = getLayoutInflater().inflate(R.layout.customer_all, null);
		mCustList = (ListView) mView.findViewById(R.id.list_custview);
		mSearchBox = (EditText) mView.findViewById(R.id.edt_search_all);
	
		CustomerDao cd = new CustomersDataAccess();
		mListCust = cd.readCustomerDetails();
		mlistAdapter = new CustomerViewListAdapter(mContext, mListCust,
				mCustList);
		mCustList.setAdapter(mlistAdapter);

		mNameLin = (LinearLayout) mView.findViewById(R.id.linlay_cust_name);
		mCustIdLin = (LinearLayout) mView.findViewById(R.id.linlay_cust_custid);
		mTelPhLin = (LinearLayout) mView.findViewById(R.id.linlay_cust_phone);
		mNameArrow = (ImageView) mView.findViewById(R.id.img_namearrow);
		mCustIdArrow = (ImageView) mView.findViewById(R.id.img_custidarrow);
		mTelePhArrow = (ImageView) mView.findViewById(R.id.img_telpharrow);

		CommonContexts.deviceDimension(mContext);
		final LayoutParams lparams = new LayoutParams(CommonContexts.WIDTH,
				LayoutParams.WRAP_CONTENT);
		mNameLin.setLayoutParams(lparams);
		mCustIdLin.setLayoutParams(lparams);
		mTelPhLin.setLayoutParams(lparams);

		mSearchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1 && mlistAdapter != null) {
					mlistAdapter.getFilter().filter(s);
				} else {
					if (mListCust != null && mListCust.size() > 0) {
						mlistAdapter = new CustomerViewListAdapter(mContext,
								mListCust, mCustList);
						mCustList.setAdapter(mlistAdapter);
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

		mNameLin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				customerNameOnclickAction();
			}
		});

		mCustIdLin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loanAccountNumberOnclickAction();
			}
		});
		mTelPhLin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				amountDueOnclickAction();
			}
		});
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

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
		mCustIdArrow.setVisibility(View.GONE);
		mCustIdArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mTelePhArrow.setVisibility(View.GONE);
		mTelePhArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[1] = true;
		mAscendingOrder[2] = true;

	}

	private void loanAccountNumberOnclickAction() {
		if (mAscendingOrder[1]) {
			mAscendingOrder[1] = false;
			mlistAdapter.sortByAcNoAsc(false);
			mCustIdArrow.setImageResource(R.drawable.arrowup);
		} else {
			mAscendingOrder[1] = true;
			mlistAdapter.sortByAcNoAsc(true);
			mCustIdArrow.setImageResource(R.drawable.arrowdown);
		}

		mCustIdArrow.setVisibility(View.VISIBLE);
		mCustIdArrow.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mNameArrow.setVisibility(View.GONE);
		mTelePhArrow.setVisibility(View.GONE);
		mTelePhArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[0] = false;
		mAscendingOrder[2] = true;
	}

	private void amountDueOnclickAction() {
		if (mAscendingOrder[2]) {
			mAscendingOrder[2] = false;
			mlistAdapter.sortByAmtAsc(false);
			mTelePhArrow.setImageResource(R.drawable.arrowup);

		} else {
			mAscendingOrder[2] = true;
			mlistAdapter.sortByAmtAsc(true);
			mTelePhArrow.setImageResource(R.drawable.arrowdown);
		}
		mTelePhArrow.setVisibility(View.VISIBLE);
		mTelePhArrow.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mNameArrow.setVisibility(View.GONE);
		mCustIdArrow.setVisibility(View.GONE);
		mCustIdArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mAscendingOrder[0] = false;
		mAscendingOrder[1] = false;
	}

	private class CustomerViewListAdapter extends BaseAdapter implements
			Filterable {
		private Context mContext;
		private List<CustomerDetail> mCustDetailList;
		private List<CustomerDetail> mDisplayedList;
		private LayoutInflater mLayoutInflater;
		private CustomerViewListAdapter mlistAdapter;
		private ListView mListView;
		private HolderFilter mHolderFilter;

		public CustomerViewListAdapter(Context context,
				List<CustomerDetail> custDetails, ListView listView) {
			mContext = context;
			mCustDetailList = custDetails;
			mDisplayedList = mCustDetailList;
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
			TextView mCustName, mCustId, mPhone;
			RelativeLayout mRelLay;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = new View(mContext);
				convertView = mLayoutInflater.inflate(
						R.layout.customerview_listitem, null);
				holder = new ViewHolder();
				holder.mCustName = (TextView) convertView
						.findViewById(R.id.txv_cust_name);
				holder.mCustId = (TextView) convertView
						.findViewById(R.id.txv_cust_custid);
				holder.mPhone = (TextView) convertView
						.findViewById(R.id.txv_cust_phone);
				holder.mRelLay = (RelativeLayout) convertView
						.findViewById(R.id.rellay_custview_listitem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.mCustName.setText(mDisplayedList.get(position)
					.getCustomerFullName());
			holder.mCustId
					.setText(mDisplayedList.get(position).getCustomerId());
			holder.mPhone.setText(mDisplayedList.get(position)
					.getMobileNumber());
			CommonContexts.setThemes(position, convertView);
			holder.mRelLay.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (CommonContexts.MODULE.equalsIgnoreCase("KYC")) 
					{
						if(CommonContexts.SELECTED_ENROLEMENT != null)
						{
							CommonContexts.SELECTED_ENROLEMENT = null;
						}
						CommonContexts.SELECTED_ENROLEMENT = new Enrolement();
						CommonContexts.SELECTED_CUSTOMER=mDisplayedList.get(position);
						Intent kycintent = new Intent(CustomersAll.this,
								KycOnlyView.class);
						startActivity(kycintent);
						finish();
					} 
					else {
						View mView = getLayoutInflater().inflate(
								R.layout.customersall_details, null);
						mTitle.setText(getString(R.string.CustomerDetails));
						onDetailsView(position, mView);
					}
				}

				private void onDetailsView(final int position, View mView) {
					CommonContexts.CURRENT_VIEW = VIEW_CUST_DETAILS;
					TextView mName = (TextView) mView
							.findViewById(R.id.txv_custall_name);
					TextView mDob = (TextView) mView
							.findViewById(R.id.txv_custall_dob);
					TextView mGender = (TextView) mView
							.findViewById(R.id.txv_custall_gender);
					TextView mPhoneno = (TextView) mView
							.findViewById(R.id.txv_custall_phoneno);
					TextView mAddress = (TextView) mView
							.findViewById(R.id.txv_custall_address);
					TextView mCity = (TextView) mView
							.findViewById(R.id.txv_custall_city);
					TextView mState = (TextView) mView
							.findViewById(R.id.txv_custall_state);
					TextView mCountry = (TextView) mView
							.findViewById(R.id.txv_custall_country);
					TextView mCategory = (TextView) mView
							.findViewById(R.id.txv_custall_category);
					TextView mZipcode = (TextView) mView
							.findViewById(R.id.txv_custall_zipcode);
					TextView mLocationcode = (TextView) mView
							.findViewById(R.id.txv_custall_locationcode);

					mName.setText(mDisplayedList.get(position)
							.getCustomerFullName());
					mDob.setText(CommonContexts.dateFormat.format(new Date(Long.valueOf(mDisplayedList.get(position).getDob()))));
					mGender.setText(mDisplayedList.get(position).getGender() != null?(mDisplayedList.get(position).getGender().equalsIgnoreCase("M")?"Male":"Female"):"");
					mPhoneno.setText(mDisplayedList.get(position)
							.getMobileNumber());
					mAddress.setText(mDisplayedList.get(position)
							.getAddressLine1() != null?mDisplayedList.get(position)
									.getAddressLine1()+",":""
							
							.concat(mDisplayedList.get(position)
							.getAddressLine2() != null?mDisplayedList.get(position)
									.getAddressLine2()+",":"")
										
									.concat(mDisplayedList.get(position)
										.getAddressLine3() != null?mDisplayedList.get(position)
												.getAddressLine3()+",":"")
												
												.concat(mDisplayedList.get(position)
												.getAddressLine4() != null?mDisplayedList.get(position)
														.getAddressLine4()+",":"")
													
							
							);
					mCity.setText(mDisplayedList.get(position).getCity());
					mState.setText(mDisplayedList.get(position).getState());
					mCountry.setText(mDisplayedList.get(position).getCountry());
					
					mCategory.setText(mDisplayedList.get(position).getCustomerCategory());
					mZipcode.setText(mDisplayedList.get(position).getZipCode());
					mLocationcode.setText(mDisplayedList.get(position).getLocationCode());

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

		private void applySort(Comparator<CustomerDetail> comparator) {
			Collections.sort(mDisplayedList, comparator);
			mlistAdapter = new CustomerViewListAdapter(mContext,
					mDisplayedList, mListView);
			mListView.setAdapter(mlistAdapter);
			mlistAdapter.notifyDataSetChanged();
		}

		public void sortByNameAsc(boolean type) {

			final boolean types = type;
			Comparator<CustomerDetail> comparator = new Comparator<CustomerDetail>() {
				@Override
				public int compare(CustomerDetail object1,
						CustomerDetail object2) {
					if (object1.getCustomerFullName() != null
							&& object2.getCustomerFullName() != null) {
						if (types) {
							return object1.getCustomerFullName()
									.compareToIgnoreCase(
											object2.getCustomerFullName());
						}

						else if (object1.getCustomerFullName() != null
								&& object2.getCustomerFullName() != null) {
							return object2.getCustomerFullName()
									.compareToIgnoreCase(
											object1.getCustomerFullName());
						}
					}
					return 0;
				}
			};
			applySort(comparator);
		}

		public void sortByAcNoAsc(boolean type) {
			final boolean types = type;
			Comparator<CustomerDetail> comparator = new Comparator<CustomerDetail>() {
				@Override
				public int compare(CustomerDetail object1,
						CustomerDetail object2) {
					if (object1.getCustomerId() != null
							&& object2.getCustomerId() != null) {
						if (types) {
							return object1.getCustomerId().compareToIgnoreCase(
									object2.getCustomerId());
						} else {
							return object2.getCustomerId().compareToIgnoreCase(
									object1.getCustomerId());
						}
					}
					return 0;
				}
			};
			applySort(comparator);

		}

		public void sortByAmtAsc(boolean type) {
			final boolean types = type;
			Comparator<CustomerDetail> comparator = new Comparator<CustomerDetail>() {
				@Override
				public int compare(CustomerDetail object1,
						CustomerDetail object2) {
					if (object1.getMobileNumber() != null
							&& object2.getMobileNumber() != null) {
						if (types) {

							return Double.valueOf(object1.getMobileNumber())
									.compareTo(
											Double.valueOf(object2
													.getMobileNumber()));
						} else {
							return Double.valueOf(object2.getMobileNumber())
									.compareTo(
											Double.valueOf(object1
													.getMobileNumber()));

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
					List<CustomerDetail> nHolderList = new ArrayList<CustomerDetail>();
					constraint = constraint.toString().toUpperCase();
					for (CustomerDetail h : mCustDetailList) {

						if ((h.customerFullName.toUpperCase()
								.contains(constraint))
								|| (h.customerId.toUpperCase()
										.contains(constraint))
								|| (h.mobileNumber.toUpperCase()
										.contains(constraint))) {
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

				mDisplayedList = (ArrayList<CustomerDetail>) results.values;
				notifyDataSetChanged();
			}
		}

	}

	@Override
	protected void onLeftAction() {
		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (id == R.drawable.back) {
			if (CommonContexts.CURRENT_VIEW.equals(VIEW_CUST_ALL)) {
				Intent hist = new Intent(CustomersAll.this, HomeView.class);
				startActivity(hist);
				finish();
			} else if (CommonContexts.CURRENT_VIEW.equals(VIEW_CUST_DETAILS)) {
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

		if (CommonContexts.CURRENT_VIEW.equals(VIEW_CUST_ALL)) {
			Intent hist = new Intent(CustomersAll.this, HomeView.class);
			startActivity(hist);
			finish();
		} else if (CommonContexts.CURRENT_VIEW.equals(VIEW_CUST_DETAILS)) {
			loadWidgets();
		}
	}
}
