package com.bfsi.egalite.view.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.DepositsDao;
import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.view.CustAgendaView;
import com.bfsi.egalite.view.R;
import com.bfsi.egalite.view.cash.CashView;

@SuppressLint("SimpleDateFormat")
public class CustomerView extends BaseActivity {

	private static final String VIEW_CUSTOMER = "Customer";
	private static final String VIEW_CUSTOMER_DETAILS = "CustomerDetail";
	private static final String VIEW_PAY = "Customerpay";
	private static final String VIEW_SCHEDULES = "CustomerSchedules";
	private boolean mAscendingOrder[] = { true, true, true };
	// public static boolean CommonContexts.fromSubViews = false;
	private List<CustomerDetail> mCustomerDetailList = new ArrayList<CustomerDetail>();
	private CommonCustListAdapter mListAdapter;
	private Context mContext;
	private ListView mListView;
	private LinearLayout mCustomerDetailId, mCustName, mPhNo;
	private ImageView mCustIdArrow, mCustNameArrow, mPhNoArrow;
	private View mView;
	private EditText mSearchBox;
	// private ExpandableListView expListView;
	private List<AgendaMaster> mRepayList;
	private List<AgendaMaster> mDpsList;
	private List<CustomerDetail> mCashList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_CUSTOMER;
		mContext = getApplicationContext();
		mBtnLeft.setTag(R.drawable.back);
		mBtnLeft.setImageResource(R.drawable.back);
		mTitle.setText(getString(R.string.screen_rpy_customer_details));
		CommonContexts.CURRENT_VIEW = VIEW_CUSTOMER;

		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00101));
			mCustomerDetailList = CommonContexts.CustomerDetailList();
		} catch (ServiceException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00101)
							+ e.getMessage(), e);
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00101),
					Toast.LENGTH_SHORT).show();
		}
		if (mCustomerDetailList != null && !mCustomerDetailList.isEmpty()) {
			if (mCustomerDetailList.size() == 1) {
				CommonContexts.fromSubViews = false;
				CommonContexts.CURRENT_VIEW = VIEW_CUSTOMER_DETAILS;
				if (CommonContexts.MODULE.equals(Constants.CASH)) {
					handleCashNavigateToDetailView(mCustomerDetailList.get(0));
				} else if (CommonContexts.MODULE.equals(Constants.CUSTOMERALL)) {
					handleNavigateToCustomerAgenda(mCustomerDetailList.get(0));
				} else {
					detailsView(mCustomerDetailList.get(0));
				}
			}

			else {
				CommonContexts.fromSubViews = true;
				onLoading();
			}
		}

	}

	private void onLoading() {
		CommonContexts.CURRENT_VIEW = VIEW_CUSTOMER;
		mView = getLayoutInflater().inflate(R.layout.customerlist, null);
		mListView = (ListView) mView.findViewById(R.id.list_repay_customer);
		mSearchBox = (EditText) mView
				.findViewById(R.id.edt_repay_customer_search);
		mSearchBox.setTypeface(CommonContexts.getTypeface(mContext));
		mSearchBox.setEnabled(false);
		mSearchBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() >= 1 && mCustomerDetailList != null) {
					mListAdapter.getFilter().filter(s);
				} else {
					if (mCustomerDetailList != null
							&& mCustomerDetailList.size() > 0) {
						mListAdapter = new CommonCustListAdapter(mContext,
								(ArrayList<CustomerDetail>) mCustomerDetailList);
						mListView.setAdapter(mListAdapter);
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
		mCustomerDetailId = (LinearLayout) mView
				.findViewById(R.id.linlay_repay_customer_custid);
		mCustName = (LinearLayout) mView
				.findViewById(R.id.linlay_repay_customer_name);
		mPhNo = (LinearLayout) mView
				.findViewById(R.id.linlay_repay_customer_phoneno);

		mCustIdArrow = (ImageView) mView
				.findViewById(R.id.img_repay_customer_custidarrow);
		mCustNameArrow = (ImageView) mView
				.findViewById(R.id.img_repay_customer_namearrow);
		mPhNoArrow = (ImageView) mView
				.findViewById(R.id.img_repay_customer_phonenoarrow);

		CommonContexts.deviceDimension(mContext);
		final LayoutParams lparams = new LayoutParams(CommonContexts.WIDTH,
				LayoutParams.WRAP_CONTENT);
		mCustName.setLayoutParams(lparams);
		mCustomerDetailId.setLayoutParams(lparams);
		mPhNo.setLayoutParams(lparams);
		mCustomerDetailId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CustomerDetailIdOnclickAction();
			}
		});
		mCustName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CustomerDetailNameOnclickAction();
			}
		});
		mPhNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				phoneOnclickAction();

			}
		});

		mListAdapter = new CommonCustListAdapter(mContext,
				(ArrayList<CustomerDetail>) mCustomerDetailList);
		mListView.setAdapter(mListAdapter);
		mSearchBox.setEnabled(true);

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	private void detailsView(final CustomerDetail detailsdata) {
		BaseActivity.mTitle.setText(R.string.CustomerDetails);
		mView = getLayoutInflater()
				.inflate(R.layout.customer_information, null);
		TextView customername = (TextView) mView
				.findViewById(R.id.txv_repay_customer_info_name_val);
		TextView dob = (TextView) mView
				.findViewById(R.id.txv_repay_customer_info_dob_val);
		TextView gender = (TextView) mView
				.findViewById(R.id.txv_repay_customer_info_gender_val);
		TextView phone = (TextView) mView
				.findViewById(R.id.txv_repay_customer_info_phoneno_val);
		TextView address = (TextView) mView
				.findViewById(R.id.txv_repay_customer_info_address_val);
		TextView city = (TextView) mView
				.findViewById(R.id.txv_repay_customer_info_city_val);
		TextView state = (TextView) mView
				.findViewById(R.id.txv_repay_customer_info_state_val);
		TextView country = (TextView) mView
				.findViewById(R.id.txv_repay_customer_info_country_val);

		LinearLayout linlayDepo = (LinearLayout) mView
				.findViewById(R.id.linlay_deposit);

		LinearLayout linlayNewReq = (LinearLayout) mView
				.findViewById(R.id.linlay_customer_info_newreq);
		linlayNewReq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newreq = new Intent(CustomerView.this,
						CustRequestEntry.class);
				newreq.putExtra("Name", detailsdata.getCustomerFullName());
				newreq.putExtra("CustId", detailsdata.getCustomerId());
				newreq.putExtra("branchcode", detailsdata.getLocalBranchCode());
				startActivity(newreq);
				finish();

			}
		});
		customername.setText(detailsdata.getCustomerFullName());
		phone.setText(detailsdata.getMobileNumber());
		city.setText(detailsdata.getCity());

		// String dateofbirth = null;
		// if (detailsdata.getDob() != null) {
		//
		// DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		// try {
		// LOG.debug(mContext.getResources().getString(R.string.MFB00131));
		// Date date = df.parse(detailsdata.getDob());
		// dateofbirth = CommonContexts.dateFormatMonth.format(date);
		// } catch (Exception e) {
		// LOG.error(mContext.getResources().getString(R.string.MFB00131)
		// + e.getMessage(), e);
		//
		// Toast.makeText(mContext,
		// mContext.getResources().getString(R.string.MFB00131),
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// }
		dob.setText(detailsdata.getDob() != null ? CommonContexts.dateFormat
				.format(new Date(Long.valueOf(detailsdata.getDob()))) : "");
		address.setText(detailsdata.getAddressLine1()
		/*
		 * .concat(detailsdata.getAddressLine2() != null ? detailsdata
		 * .getAddressLine2() : "") .concat(detailsdata.getAddressLine3() !=
		 * null ? detailsdata .getAddressLine3() : "")
		 * .concat(detailsdata.getAddressLine4() != null ? detailsdata
		 * .getAddressLine4() : "")
		 */);
		state.setText(detailsdata.getState());
		country.setText(detailsdata.getCountry().equalsIgnoreCase("IN")? getString(R.string.country_india): "");
		gender.setText(detailsdata.getGender() != null ? (detailsdata
				.getGender().equalsIgnoreCase("M") ? "Male" : "Female") : "");

		DepositsDao clcDao = DaoFactory.getDepositDao();

		getDepositDetail(detailsdata, clcDao);

		if (mDpsList.size() == 0) {
			linlayDepo.setVisibility(View.GONE);
		} else {
			mListView = (ListView) mView
					.findViewById(R.id.list_repay_customer_deposit_details);
			mListView.setAdapter(new DepositListAdapter(mContext, mDpsList));
		}
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	private void getDepositDetail(CustomerDetail detailsdata, DepositsDao clcDao) {
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00110));
			mDpsList = clcDao.CustDepositDetails(detailsdata.getCustomerId());
		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00110)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00110),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00110)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00110),
					Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * private void getAccountDetails(CustomerDetail detailsdata, LoansDao
	 * rpyDao) { try {
	 * LOG.debug(mContext.getResources().getString(R.string.MFB00109));
	 * mRepayList = rpyDao.CustAccountDetails(detailsdata.getCustomerId()); }
	 * catch (DataAccessException e) { LOG.error(
	 * mContext.getResources().getString(R.string.MFB00109) + e.getMessage(),
	 * e);
	 * 
	 * Toast.makeText(mContext,
	 * mContext.getResources().getString(R.string.MFB00109),
	 * Toast.LENGTH_SHORT).show(); } catch (Exception e) { LOG.error(
	 * mContext.getResources().getString(R.string.MFB00109) + e.getMessage(),
	 * e);
	 * 
	 * Toast.makeText(mContext,
	 * mContext.getResources().getString(R.string.MFB00109),
	 * Toast.LENGTH_SHORT).show(); } }
	 */

	private void getCashDetails(CustomerDetail detailsdata, CustomerDao clcDao) {
		try {
			LOG.debug(mContext.getResources().getString(R.string.MFB00133));
			mCashList = clcDao.readCashDetails(detailsdata.getCustomerId());
		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00133)
							+ e.getMessage(), e);
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00133),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00133)
							+ e.getMessage(), e);

			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.MFB00133),
					Toast.LENGTH_SHORT).show();
		}
	}

	private class CommonCustListAdapter extends BaseAdapter implements
			Filterable {

		private List<CustomerDetail> CustomerDetailList;
		private List<CustomerDetail> mDisplayedCustList;
		LayoutInflater l_inflater;
		HolderFilter holderFilter;

		public CommonCustListAdapter(Context applicationContext,
				List<CustomerDetail> loanDbData) {
			CustomerDetailList = loanDbData;
			mContext = applicationContext;
			mDisplayedCustList = CustomerDetailList;

		}

		private void applySort(Comparator<CustomerDetail> comparator) {
			Collections.sort(mDisplayedCustList, comparator);
			mListAdapter = new CommonCustListAdapter(mContext,
					mDisplayedCustList);
			mListView.setAdapter(mListAdapter);
			mListAdapter.notifyDataSetChanged();
		}

		public void sortByCustIdAsc(boolean type) {
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

		protected void sortByNameAsc(boolean type) {

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
						} else {
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

		protected void sortByPhNoAsc(boolean type) {

			final boolean types = type;
			Comparator<CustomerDetail> comparator = new Comparator<CustomerDetail>() {
				@Override
				public int compare(CustomerDetail object1,
						CustomerDetail object2) {
					if (object1.getMobileNumber() != null
							&& object2.getMobileNumber() != null) {
						if (types) {
							return object1.getMobileNumber()
									.compareToIgnoreCase(
											object2.getMobileNumber());
						}

						{
							return object2.getMobileNumber()
									.compareToIgnoreCase(
											object1.getMobileNumber());
						}
					}

					return 0;
				}
			};
			applySort(comparator);
		}

		// View of the application for each list element
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {
				l_inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = l_inflater.inflate(R.layout.customer_listitem,
						parent, false);
				holder = new ViewHolder();
				holder.CustomerDetail_id = (TextView) convertView
						.findViewById(R.id.txv_repay_customer_custid);
				holder.name = (TextView) convertView
						.findViewById(R.id.txv_repay_customer_name);
				holder.Phno = (TextView) convertView
						.findViewById(R.id.txv_repay_customer_phoneno);

				holder.rel_layout = (RelativeLayout) convertView
						.findViewById(R.id.rellay_repay_customer_listitem);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.name.setWidth(CommonContexts.WIDTH);
			holder.CustomerDetail_id.setWidth(CommonContexts.WIDTH);
			holder.Phno.setWidth(CommonContexts.WIDTH);
			holder.CustomerDetail_id.setGravity(Gravity.CENTER);
			holder.Phno.setGravity(Gravity.RIGHT);

			holder.CustomerDetail_id.setText(mDisplayedCustList.get(position)
					.getCustomerId());
			holder.name.setText(mDisplayedCustList.get(position)
					.getCustomerFullName());
			holder.Phno.setText(mDisplayedCustList.get(position)
					.getMobileNumber());
			holder.rel_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (CommonContexts.MODULE.equals(Constants.CASH)) {

						handleCashNavigateToDetailView((CustomerDetail) mDisplayedCustList
								.get(position));

					} else if (CommonContexts.MODULE
							.equals(Constants.CUSTOMERALL)) {
						handleNavigateToCustomerAgenda((CustomerDetail) mDisplayedCustList
								.get(position));

					}

					else {
						handleNavigatesToDetailsView((CustomerDetail) mDisplayedCustList
								.get(position));
					}
				}

			});

			CommonContexts.setThemes(position, convertView);
			return convertView;

		}

		// poup

		private void handleNavigatesToDetailsView(
				final CustomerDetail custDetail) {
			CommonContexts.CURRENT_VIEW = VIEW_CUSTOMER_DETAILS;
			CommonContexts.fromSubViews = false;
			detailsView(custDetail);

		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public int getCount() {

			return mDisplayedCustList.size();

		}

		@Override
		public Object getItem(int position) {
			return mDisplayedCustList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		private class ViewHolder {
			TextView CustomerDetail_id, name, Phno;
			RelativeLayout rel_layout;

		}

		@Override
		public Filter getFilter() {
			if (holderFilter == null) {
				holderFilter = new HolderFilter();
			}
			return holderFilter;
		}

		// To filter the list based on search
		private class HolderFilter extends Filter {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();

				if (constraint == null ) {

					results.values = mDisplayedCustList;
					results.count = mDisplayedCustList.size();
				} else if (constraint != null && constraint.length() > 0) {
					List<CustomerDetail> nHolderList = new ArrayList<CustomerDetail>();
					constraint = constraint.toString().toUpperCase();
					for (CustomerDetail h : CustomerDetailList) {

						if ((h.customerId.toUpperCase().contains(constraint))
								|| (h.customerFullName.toUpperCase()
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

				mDisplayedCustList = (ArrayList<CustomerDetail>) results.values;
				notifyDataSetChanged();

			}
		}
	}

	private void handleCashNavigateToDetailView(final CustomerDetail custDetail) {
		CommonContexts.CURRENT_VIEW = VIEW_CUSTOMER_DETAILS;
		cashDetailsView(custDetail);
	}

	private void handleNavigateToCustomerAgenda(final CustomerDetail custDetail) {
		CommonContexts.CURRENT_VIEW = VIEW_CUSTOMER_DETAILS;
		custAgendaView(custDetail);
	}

	private void custAgendaView(CustomerDetail custDetail) {
		CommonContexts.SELECTED_CUSTOMER = null;
		CommonContexts.SELECTED_CUSTOMER = custDetail;
		Intent intent = new Intent(CustomerView.this, CustAgendaView.class);
		startActivity(intent);
		finish();

	}

	private void CustomerDetailIdOnclickAction() {
		// Change the order of items based on price
		if (mAscendingOrder[0]) {
			// Show items descending
			mAscendingOrder[0] = false;
			mListAdapter.sortByCustIdAsc(false);
			mCustIdArrow.setImageResource(R.drawable.arrowup);

		} else {
			// Show items ascending
			mAscendingOrder[0] = true;
			mListAdapter.sortByCustIdAsc(true);
			mCustIdArrow.setImageResource(R.drawable.arrowdown);
		}

		mCustIdArrow.setVisibility(View.VISIBLE);
		mCustNameArrow.setVisibility(View.GONE);
		mCustNameArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mPhNoArrow.setVisibility(View.GONE);
		mPhNoArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));

		mAscendingOrder[1] = true;
		mAscendingOrder[2] = true;

	}

	public void cashDetailsView(CustomerDetail detailsdata) {
		BaseActivity.mTitle.setText(R.string.cash_details);
		mView = getLayoutInflater().inflate(R.layout.cash_details, null);
		TextView customername = (TextView) mView
				.findViewById(R.id.txv_cash_detail_name);
		TextView dob = (TextView) mView.findViewById(R.id.txv_cash_detail_dob);
		TextView gender = (TextView) mView
				.findViewById(R.id.txv_cash_detail_gendor);
		TextView phone = (TextView) mView
				.findViewById(R.id.txv_cash_detail_telph);
		TextView address1 = (TextView) mView
				.findViewById(R.id.txv_cash_detail_address1);
		TextView address2 = (TextView) mView
				.findViewById(R.id.txv_cash_detail_address2);
		TextView address3 = (TextView) mView
				.findViewById(R.id.txv_cash_detail_address3);
		TextView address4 = (TextView) mView
				.findViewById(R.id.txv_cash_detail_address4);
		TextView city = (TextView) mView
				.findViewById(R.id.txv_cash_detail_city);
		TextView state = (TextView) mView
				.findViewById(R.id.txv_cash_detail_state);
		TextView country = (TextView) mView
				.findViewById(R.id.txv_cash_detail_country);
		LinearLayout linlaySavings = (LinearLayout) mView
				.findViewById(R.id.linlay_savingsacc);

		TableRow tablAdd1 = (TableRow) mView
				.findViewById(R.id.tablerow_address1);
		TableRow tablAdd2 = (TableRow) mView
				.findViewById(R.id.tablerow_address2);
		TableRow tablAdd3 = (TableRow) mView
				.findViewById(R.id.tablerow_address3);
		TableRow tablAdd4 = (TableRow) mView
				.findViewById(R.id.tablerow_address4);
		// String dateofbirth = null;
		// if (detailsdata.getDob() != null) {
		//
		// DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		// try {
		// LOG.debug(mContext.getResources().getString(R.string.MFB00131));
		// Date date = df.parse(detailsdata.getDob());
		// dateofbirth = CommonContexts.dateFormatMonth.format(date);
		// } catch (Exception e) {
		// LOG.error(mContext.getResources().getString(R.string.MFB00131)
		// + e.getMessage(), e);
		//
		// Toast.makeText(mContext,
		// mContext.getResources().getString(R.string.MFB00131),
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// }

		customername.setText(detailsdata.getCustomerFullName());
		phone.setText(detailsdata.getMobileNumber());
		city.setText(detailsdata.getCity());
		dob.setText(CommonContexts.dateFormat.format(new Date(Long
				.valueOf(detailsdata.getDob()))));
		tablAdd1.setVisibility(View.GONE);
		tablAdd2.setVisibility(View.GONE);
		tablAdd3.setVisibility(View.GONE);
		tablAdd4.setVisibility(View.GONE);

		if (detailsdata.getAddressLine1() != null) {
			address1.setText(detailsdata.getAddressLine1());
			tablAdd1.setVisibility(View.VISIBLE);
		}
		if (detailsdata.getAddressLine2() != null) {
			address2.setText(detailsdata.getAddressLine2());
			tablAdd2.setVisibility(View.VISIBLE);
		}
		if (detailsdata.getAddressLine3() != null) {
			address3.setText(detailsdata.getAddressLine3());
			tablAdd3.setVisibility(View.VISIBLE);
		}
		if (detailsdata.getAddressLine4() != null) {
			address4.setText(detailsdata.getAddressLine4());
			tablAdd4.setVisibility(View.VISIBLE);
		}

		state.setText(detailsdata.getState());
		country.setText(detailsdata.getCountry().equalsIgnoreCase("IN")? getString(R.string.country_india): "");
		gender.setText(detailsdata.getGender() != null ? (detailsdata
				.getGender().equalsIgnoreCase("M") ? "Male" : "Female") : "");

		CustomerDao clcDao = DaoFactory.getCustomerDao();
		getCashDetails(detailsdata, clcDao);
		if (mCashList.size() == 0) {
			linlaySavings.setVisibility(View.GONE);

		} else {
			mListView = (ListView) mView.findViewById(R.id.list_savingsacc);
			mListView.setAdapter(new CashListAdapter(mContext, mCashList));
		}

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);

	}

	private void CustomerDetailNameOnclickAction() {
		// Change the order of items based on price
		if (mAscendingOrder[1]) {
			// Show items descending
			mAscendingOrder[1] = false;
			mListAdapter.sortByNameAsc(false);
			mCustNameArrow.setImageResource(R.drawable.arrowup);

		} else {
			// Show items ascending
			mAscendingOrder[1] = true;
			mListAdapter.sortByNameAsc(true);
			mCustNameArrow.setImageResource(R.drawable.arrowdown);
		}
		mCustNameArrow.setVisibility(View.VISIBLE);
		mCustNameArrow.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mCustIdArrow.setVisibility(View.GONE);
		mPhNoArrow.setVisibility(View.GONE);
		mPhNoArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[0] = true;
		mAscendingOrder[2] = true;

	}

	private void phoneOnclickAction() {
		if (mAscendingOrder[2]) {
			// Show items descending
			mAscendingOrder[2] = false;
			mListAdapter.sortByPhNoAsc(false);
			mPhNoArrow.setImageResource(R.drawable.arrowup);

		} else {
			// Show items ascending
			mAscendingOrder[2] = true;
			mListAdapter.sortByPhNoAsc(true);
			mPhNoArrow.setImageResource(R.drawable.arrowdown);
		}
		mPhNoArrow.setVisibility(View.VISIBLE);
		mPhNoArrow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mCustIdArrow.setVisibility(View.GONE);
		mCustNameArrow.setVisibility(View.GONE);
		mCustNameArrow.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT));
		mAscendingOrder[0] = true;
		mAscendingOrder[1] = true;

	}

	@Override
	protected void onLeftAction() {

		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (id == R.drawable.back) {
			if (CommonContexts.CURRENT_SCREEN
					.equalsIgnoreCase(CommonContexts.SCREEN_CUSTOMER)) {
				if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_CUSTOMER)) {
					startActivity(new Intent(mContext, CustomerQuery.class));
					finish();

				} else if (CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_CUSTOMER_DETAILS)) {
					BaseActivity.mTitle
							.setText(R.string.screen_rpy_customer_details);
					if (CommonContexts.fromSubViews) {
						onLoading();
					} else {
						startActivity(new Intent(mContext, CustomerQuery.class));
						finish();
					}

				} else if (CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_PAY)) {
					BaseActivity.mTitle
							.setText(R.string.screen_rpy_customer_details);
					if (CommonContexts.fromSubViews) {
						onLoading();
					} else {
						startActivity(new Intent(mContext, CustomerQuery.class));
						finish();
					}
				}

			}
		}

	}

	@Override
	protected void onRightAction() {

		super.onRightAction();
	}

	public class CustListAdapter extends BaseAdapter {
		private List<AgendaMaster> mDisplayedValues;

		public CustListAdapter(Context c, List<AgendaMaster> hdbc2) {
			mContext = c;
			mDisplayedValues = hdbc2;
		}

		public int getCount() {
			return mDisplayedValues.size();
		}

		public Object getItem(int position) {
			return mDisplayedValues.get(position);
		}

		public long getItemId(int arg0) {
			return 0;
		}

		private class ViewHolder {
			TextView account_number;
			TableLayout table_layout;

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final int _position = position;
			ViewHolder holder = null;
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.customer_account_details_listitems, parent,
						false);
				holder = new ViewHolder();
				holder.account_number = (TextView) convertView
						.findViewById(R.id.txv_repay_acc_details_listitems_accno);
				holder.table_layout = (TableLayout) convertView
						.findViewById(R.id.tablay_repay_acc_details_listitems);
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.account_number.setText(mDisplayedValues.get(_position)
					.getCbsAcRefNo());

			holder.table_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String accountNo = mDisplayedValues.get(_position)
							.getCbsAcRefNo();
					// showLoanDialogReq(accountNo);
				}

			});
			return convertView;

		}

		// protected void showLoanDialogReq(final String acNo) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// CustomerView.this);
		// builder.setTitle("Egalite")
		// .setItems(R.array.laonPopUp,
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int position) {
		// // write onclick listner
		// if (position == 0) {
		// CommonContexts.CURRENT_VIEW = VIEW_SCHEDULES;
		// Toast.makeText(mContext,
		// "Yet to Develop",
		// Toast.LENGTH_SHORT).show();
		// // scheduleView(acNo);
		//
		// } else {
		//
		// CommonContexts.CURRENT_VIEW = VIEW_PAY;
		// LoansDao rpyDao = DaoFactory
		// .getLoanDao();
		// Requests acNoCheckAgendaData = null;
		// try {
		// LOG.debug(mContext.getResources()
		// .getString(
		// R.string.MFB00135));
		// acNoCheckAgendaData = rpyDao
		// .acNoCheckAgenda(acNo);
		// } catch (DataAccessException e) {
		//
		// LOG.error(
		// mContext.getResources()
		// .getString(
		// R.string.MFB00135)
		// + e.getMessage(), e);
		//
		// Toast.makeText(
		// mContext,
		// mContext.getResources()
		// .getString(
		// R.string.MFB00135),
		// Toast.LENGTH_SHORT).show();
		// } catch (Exception e) {
		//
		// LOG.error(
		// mContext.getResources()
		// .getString(
		// R.string.MFB00135)
		// + e.getMessage(), e);
		//
		// Toast.makeText(
		// mContext,
		// mContext.getResources()
		// .getString(
		// R.string.MFB00135),
		// Toast.LENGTH_SHORT).show();
		// }
		// CommonContexts.SELECTED_REQUEST = acNoCheckAgendaData;
		// Intent intent2 = new Intent(mContext,
		// CustLoanPrepayEntry.class);
		//
		// startActivity(intent2);
		// finish();
		//
		// }
		//
		// }
		// })
		// .setPositiveButton("Cancel",
		// new DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog,
		// int id) {
		// }
		// });
		// AlertDialog alert = builder.create();
		// alert.show();
		//
		// }
	}

	public class DepositListAdapter extends BaseAdapter {
		private List<AgendaMaster> mDisplayedValues;
		private Context mContext;

		public DepositListAdapter(Context c, List<AgendaMaster> hdbc2) {
			mContext = c;
			mDisplayedValues = hdbc2;
		}

		public int getCount() {
			return mDisplayedValues.size();
		}

		public Object getItem(int position) {
			return mDisplayedValues.get(position);
		}

		public long getItemId(int arg0) {
			return 0;
		}

		private class ViewHolder {
			TextView account_number;
			TableLayout table_layout;

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final int _position = position;
			ViewHolder holder = null;
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.customer_account_details_listitems, parent,
						false);
				holder = new ViewHolder();
				holder.account_number = (TextView) convertView
						.findViewById(R.id.txv_repay_acc_details_listitems_accno);
				holder.table_layout = (TableLayout) convertView
						.findViewById(R.id.tablay_repay_acc_details_listitems);
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.account_number.setText(mDisplayedValues.get(_position)
					.getCbsAcRefNo());

			holder.table_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// showDialogDeposit(_position);
					prepayEntry(_position);
				}
			});

			return convertView;

		}

		protected void prepayEntry(int pos) {
			DepositsDao clcdao = DaoFactory.getDepositDao();

			Requests acNoCheckAgendaData = null;
			try {
				LOG.debug(mContext.getResources().getString(R.string.MFB00135));
				acNoCheckAgendaData = clcdao
						.readAccountDetails(mDisplayedValues.get(pos)
								.getCbsAcRefNo());
			} catch (DataAccessException e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00135)
						+ e.getMessage(), e);

				Toast.makeText(mContext,
						mContext.getResources().getString(R.string.MFB00135),
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00135)
						+ e.getMessage(), e);

				Toast.makeText(mContext,
						mContext.getResources().getString(R.string.MFB00135),
						Toast.LENGTH_SHORT).show();
			}
			CommonContexts.SELECTED_REQUEST = acNoCheckAgendaData;
			Intent intent1 = new Intent(mContext, CustPrepayEntry.class);
			intent1.putExtra(CommonContexts.CUST_REQUEST_TYPE, "REQ PREPAY");
			startActivity(intent1);
			finish();

		}

		protected void showDialogDeposit(final int pos) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					CustomerView.this);
			builder.setTitle("Egalite")
					.setItems(R.array.deposit_popup,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int position) {
									// write onclick listner
									if (position == 0) {

										DepositsDao clcdao = DaoFactory
												.getDepositDao();

										Requests acNoCheckAgendaData = null;
										try {
											LOG.debug(mContext.getResources()
													.getString(
															R.string.MFB00135));
											acNoCheckAgendaData = clcdao
													.readAccountDetails(mDisplayedValues
															.get(pos)
															.getCbsAcRefNo());
										} catch (DataAccessException e) {
											LOG.error(
													mContext.getResources()
															.getString(
																	R.string.MFB00135)
															+ e.getMessage(), e);

											Toast.makeText(
													mContext,
													mContext.getResources()
															.getString(
																	R.string.MFB00135),
													Toast.LENGTH_SHORT).show();
										} catch (Exception e) {
											LOG.error(
													mContext.getResources()
															.getString(
																	R.string.MFB00135)
															+ e.getMessage(), e);

											Toast.makeText(
													mContext,
													mContext.getResources()
															.getString(
																	R.string.MFB00135),
													Toast.LENGTH_SHORT).show();
										}
										CommonContexts.SELECTED_REQUEST = acNoCheckAgendaData;
										Intent intent1 = new Intent(mContext,
												CustPrepayEntry.class);
										intent1.putExtra(
												CommonContexts.CUST_REQUEST_TYPE,
												"REQ PREPAY");
										startActivity(intent1);
										finish();

									} /*
									 * else {
									 * 
									 * DepositsDao paydao = DaoFactory
									 * .getDepositDao();
									 * 
									 * Requests acNoAgendaData = null; try {
									 * LOG.debug(mContext.getResources()
									 * .getString( R.string.MFB00135));
									 * acNoAgendaData = paydao
									 * .readAccountDetails(mDisplayedValues
									 * .get(pos) .getCbsAcRefNo()); } catch
									 * (DataAccessException e) { // block
									 * LOG.error( mContext.getResources()
									 * .getString( R.string.MFB00135) +
									 * e.getMessage(), e);
									 * 
									 * Toast.makeText( mContext,
									 * mContext.getResources() .getString(
									 * R.string.MFB00135),
									 * Toast.LENGTH_SHORT).show(); } catch
									 * (Exception e) { // block LOG.error(
									 * mContext.getResources() .getString(
									 * R.string.MFB00135) + e.getMessage(), e);
									 * 
									 * Toast.makeText( mContext,
									 * mContext.getResources() .getString(
									 * R.string.MFB00135),
									 * Toast.LENGTH_SHORT).show(); }
									 * 
									 * CommonContexts.SELECTED_REQUEST =
									 * acNoAgendaData; Intent intent2 = new
									 * Intent(mContext, CustRedemEntry.class);
									 * intent2.putExtra(
									 * CommonContexts.CUST_REQUEST_TYPE,
									 * "REQ REDEM"); startActivity(intent2);
									 * 
									 * finish();
									 * 
									 * }
									 */

								}
							})
					.setPositiveButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

		}
	}

	public class CashListAdapter extends BaseAdapter {
		private List<CustomerDetail> mDisplayedValues;

		public CashListAdapter(Context c, List<CustomerDetail> hdbc2) {
			mContext = c;
			mDisplayedValues = hdbc2;
		}

		public int getCount() {
			return mDisplayedValues.size();
		}

		public Object getItem(int position) {
			return mDisplayedValues.get(position);
		}

		public long getItemId(int arg0) {
			return 0;
		}

		private class ViewHolder {
			TextView account_number;
			RadioButton radioBtn;

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.cash_details_listitems,
						parent, false);
				holder = new ViewHolder();
				holder.account_number = (TextView) convertView
						.findViewById(R.id.txv_cashdetails_listitems_accno);
				holder.radioBtn = (RadioButton) convertView
						.findViewById(R.id.radio_cashdetails_listitems);
				convertView.setTag(holder);
			}

			else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.account_number.setText(mDisplayedValues.get(position)
					.getAcNo());
			holder.radioBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					final CustomerDetail data = (CustomerDetail) mDisplayedValues
							.get(position);
					CommonContexts.SELECTED_CASH_ACCOUNT = data;
					Intent intentDeposit = new Intent(mContext, CashView.class);
					startActivity(intentDeposit);
					CommonContexts.CASHCHECK = null;
					finish();

				}
			});

			return convertView;

		}

	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();
	}

	public void myOnKeyDown() {

		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_CUSTOMER)) {
			if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_CUSTOMER)) {
				startActivity(new Intent(mContext, CustomerQuery.class));
				finish();

			} else if (CommonContexts.CURRENT_VIEW
					.equalsIgnoreCase(VIEW_CUSTOMER_DETAILS)) {
				BaseActivity.mTitle
						.setText(R.string.screen_rpy_customer_details);
				if (CommonContexts.fromSubViews) {
					onLoading();
				} else {
					startActivity(new Intent(mContext, CustomerQuery.class));
					finish();
				}

			} else if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_PAY)) {
				BaseActivity.mTitle
						.setText(R.string.screen_rpy_customer_details);
				if (CommonContexts.fromSubViews) {
					onLoading();
				} else {
					startActivity(new Intent(mContext, CustomerQuery.class));
					finish();
				}
			}
		}

	}
}
