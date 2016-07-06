package com.bfsi.egalite.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.DpPaymentView;
import com.bfsi.egalite.view.R;
import com.bfsi.egalite.view.R.color;

public class PaymentAgndaListAdapter extends BaseAdapter implements Filterable {
	private Context mContext;
	private LayoutInflater mLayoutInflater;
	private List<AgendaMaster> mPaymentAgendaList;
	private List<AgendaMaster> mDisplayedList;
	private PaymentAgndaListAdapter mlistAdapter;
	private ListView mListView;
	private BfsiViewPager mPager;
	private HolderFilter mHolderFilter;

	public PaymentAgndaListAdapter(Context context,
			List<AgendaMaster> agendaMaster, ListView mView, BfsiViewPager mPage) {
		mContext = context;
		mLayoutInflater = LayoutInflater.from(mContext);
		mPaymentAgendaList = agendaMaster;
		mDisplayedList = mPaymentAgendaList;
		mPager = mPage;
		mListView = mView;
	}

	private void applySort(Comparator<AgendaMaster> comparator) {
		Collections.sort(mDisplayedList, comparator);
		mlistAdapter = new PaymentAgndaListAdapter(mContext, mDisplayedList,
				mListView, mPager);
		mListView.setAdapter(mlistAdapter);
		mlistAdapter.notifyDataSetChanged();
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
		TextView name, acno, amountDue;
		RelativeLayout rel_layout;
		View colorview;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = new View(mContext);
			convertView = mLayoutInflater.inflate(
					R.layout.deposit_agenda_maturity_listitem, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.txv_depo_agen_matp_listitem_name);
			holder.acno = (TextView) convertView
					.findViewById(R.id.txv_depo_agen_matp_listitem_accno);
			holder.amountDue = (TextView) convertView
					.findViewById(R.id.txv_depo_agen_matp_listitem_matamt);
			holder.rel_layout = (RelativeLayout) convertView
					.findViewById(R.id.rellay_maturity_agenda_listitem);
			holder.colorview = (View) convertView
					.findViewById(R.id.view_color_payment);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setWidth(CommonContexts.WIDTH);
		holder.acno.setWidth(CommonContexts.WIDTH);
		holder.amountDue.setWidth(CommonContexts.WIDTH);
		holder.acno.setGravity(Gravity.CENTER);
		holder.amountDue.setGravity(Gravity.RIGHT);
		holder.name.setText(mDisplayedList.get(position).getCustomerName());
		holder.acno.setText(mDisplayedList.get(position).getCbsAcRefNo());
		holder.amountDue.setText("("
				+ mDisplayedList
						.get(position)
						.getCcyCode()
						.concat(") "
								+ mDisplayedList.get(position).getAgendaAmt()));

		if (mDisplayedList.get(position).getAgendaStatus() != null) {
			if (mDisplayedList.get(position).getAgendaStatus()
					.equalsIgnoreCase("1")) {
				convertView.setBackgroundColor(Color.GREEN);
				holder.name.setTextColor(color.black);
				holder.acno.setTextColor(color.black);
				holder.amountDue.setTextColor(color.black);
			}
			else {
				CommonContexts.setThemes(position, convertView);
			}
		} else {
			CommonContexts.setThemes(position, convertView);
		}

		if (mDisplayedList.get(position).getDpIsRedemption() != null) {
			if (mDisplayedList.get(position).getDpIsRedemption()
					.equalsIgnoreCase("Y"))
				holder.colorview.setBackgroundResource(R.drawable.red);
			else
				holder.colorview.setBackgroundResource(R.drawable.voilet);
		}

		holder.rel_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleListItem(position);

			}

			private void handleListItem(final int position) {
				if (mDisplayedList.get(position).getAgendaStatus() != null) {
					if (mDisplayedList.get(position).getAgendaStatus()
							.equalsIgnoreCase("1")) {
						Toast.makeText(
								mContext,
								mContext.getResources().getString(
										R.string.MFB00016), Toast.LENGTH_SHORT)
								.show();

					} else {
						CommonContexts.SELECTED_MATURITY = null;
						CommonContexts.SELECTED_MATURITY = (AgendaMaster) mDisplayedList
								.get(position);
						mPager.setAdapter(DpPaymentView.adapter);
						mPager.setCurrentItem(1);
					}
				} else {

					CommonContexts.SELECTED_MATURITY = null;
					CommonContexts.SELECTED_MATURITY = (AgendaMaster) mDisplayedList
							.get(position);
					mPager.setAdapter(DpPaymentView.adapter);
					mPager.setCurrentItem(1);
				}

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

	public void sortByNameAsc(boolean type) {

		final boolean types = type;
		Comparator<AgendaMaster> comparator = new Comparator<AgendaMaster>() {
			@Override
			public int compare(AgendaMaster object1, AgendaMaster object2) {
				if (object1.getCustomerName() != null
						&& object2.getCustomerName() != null) {
					if (types) {
						return object1.getCustomerName().compareToIgnoreCase(
								object2.getCustomerName());
					}

					else if (object1.getCustomerName() != null
							&& object2.getCustomerName() != null) {
						return object2.getCustomerName().compareToIgnoreCase(
								object1.getCustomerName());
					}
				}
				return 0;
			}
		};
		applySort(comparator);
	}

	public void sortByAcNoAsc(boolean type) {
		final boolean types = type;
		Comparator<AgendaMaster> comparator = new Comparator<AgendaMaster>() {
			@Override
			public int compare(AgendaMaster object1, AgendaMaster object2) {
				if (object1.getCustomerName() != null
						&& object2.getCustomerName() != null) {
					if (types) {
						return object1.getCustomerName().compareToIgnoreCase(
								object2.getCustomerName());
					} else {
						return object2.getCustomerName().compareToIgnoreCase(
								object1.getCustomerName());
					}
				}
				return 0;
			}
		};
		applySort(comparator);

	}

	public void sortByAmtAsc(boolean type) {
		final boolean types = type;
		Comparator<AgendaMaster> comparator = new Comparator<AgendaMaster>() {
			@Override
			public int compare(AgendaMaster object1, AgendaMaster object2) {
				if (object1.getAgendaAmt() != null
						&& object2.getAgendaAmt() != null) {
					if (types) {
						return Double.valueOf(object1.getAgendaAmt())
								.compareTo(
										Double.valueOf(object2.getAgendaAmt()));
					} else {
						return Double.valueOf(object2.getAgendaAmt())
								.compareTo(
										Double.valueOf(object1.getAgendaAmt()));

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

			if (constraint == null || constraint.length() == 0) {

				results.values = mDisplayedList;
				results.count = mDisplayedList.size();
			} else {
				List<AgendaMaster> nHolderList = new ArrayList<AgendaMaster>();
				constraint = constraint.toString().toUpperCase();
				for (AgendaMaster h : mPaymentAgendaList) {

					if ((h.customerName.toUpperCase().contains(constraint))
							|| (h.cbsAcRefNo.toUpperCase().contains(constraint))
							|| (h.agendaAmt.toUpperCase().contains(constraint))) {
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
			mDisplayedList = (ArrayList<AgendaMaster>) results.values;
			PaymentAgndaListAdapter.this.notifyDataSetChanged();
		}
	}

}
