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
import com.bfsi.egalite.view.LoanDsbView;
import com.bfsi.egalite.view.R;
import com.bfsi.egalite.view.R.color;

public class DsbAgendaListAdapter extends BaseAdapter implements Filterable {
	private Context mContext;
	private LayoutInflater layoutInflater;
	private List<AgendaMaster> dsbAgendaList;
	private List<AgendaMaster> mDisplayedList;
	private ListView mListView;
	private BfsiViewPager mPager;
	HolderFilter holderFilter;

	public DsbAgendaListAdapter(Context c, List<AgendaMaster> hdbc2,
			ListView mView, BfsiViewPager mPage) {
		mContext = c;
		layoutInflater = LayoutInflater.from(mContext);
		dsbAgendaList = hdbc2;
		mDisplayedList = dsbAgendaList;
		mPager = mPage;
		mListView = mView;
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
	public long getItemId(int arg0) {

		return 0;
	}

	private class ViewHolder {
		TextView name, acno, amountDue;
		RelativeLayout relLayout;
		View colorview;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			convertView = new View(mContext);
			convertView = layoutInflater.inflate(R.layout.dsb_agenda_listitem,
					null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.txv_dsb_agenda_listitem_name);
			holder.acno = (TextView) convertView
					.findViewById(R.id.txv_dsb_agenda_listitem_accno);
			holder.amountDue = (TextView) convertView
					.findViewById(R.id.txv_dsb_agenda_listitem_amount);
			holder.relLayout = (RelativeLayout) convertView
					.findViewById(R.id.rellay_dsb_agenda_listitem);
			holder.colorview = (View) convertView
					.findViewById(R.id.view_color_disbursement);
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
			} else {
				CommonContexts.setThemes(position, convertView);
			}
		} else {
			CommonContexts.setThemes(position, convertView);
		}

		if (mDisplayedList.get(position).getIsGroupLoan() != null) {
			if (mDisplayedList.get(position).getIsGroupLoan()
					.equalsIgnoreCase("Y"))
				holder.colorview.setBackgroundResource(R.drawable.red);
			else
				holder.colorview.setBackgroundResource(R.drawable.voilet);
		}

		/*
		 * holder.rel_layout.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { handleListItem(position); }
		 */
		// if (mDisplayedList.get(position).getAgendaStatus() != null) {
		// if (mDisplayedList.get(position).getAgendaStatus().toString()
		// .equalsIgnoreCase("1")) {
		// convertView.setBackgroundColor(Color.GREEN);
		// holder.name.setTextColor(color.black);
		// holder.acno.setTextColor(color.black);
		// holder.amountDue.setTextColor(color.black);
		// } else {
		// CommonContexts.setThemes(position, convertView);
		// }
		// }

		holder.relLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleListItem(position);
			}

			private void handleListItem(final int position) {
				if (mDisplayedList.get(position).getAgendaStatus() != null) {
					if (mDisplayedList.get(position).getAgendaStatus()
							.toString().equalsIgnoreCase("1")) {
						Toast.makeText(
								mContext,
								mContext.getResources().getString(
										R.string.MFB00029), Toast.LENGTH_SHORT)
								.show();

					} else {

						CommonContexts.SELECTED_DSB = (AgendaMaster) mDisplayedList
								.get(position);
						mPager.setAdapter(LoanDsbView.adapter);
						mPager.setCurrentItem(1);
					}
				} else {
					CommonContexts.SELECTED_DSB = (AgendaMaster) mDisplayedList
							.get(position);
					mPager.setAdapter(LoanDsbView.adapter);
					mPager.setCurrentItem(1);
				}

			}
		});

		return convertView;
	}

	private void applySort(Comparator<AgendaMaster> comparator) {
		Collections.sort(mDisplayedList, comparator);
		DsbAgendaListAdapter mlistAdapter;
		mlistAdapter = new DsbAgendaListAdapter(mContext, mDisplayedList,
				mListView, mPager);
		mListView.setAdapter(mlistAdapter);
		mlistAdapter.notifyDataSetChanged();
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
				if (object1.getCbsAcRefNo() != null
						&& object2.getCbsAcRefNo() != null) {
					if (types) {
						return object1.getCbsAcRefNo().compareToIgnoreCase(
								object2.getCbsAcRefNo());
					} else {
						return object2.getCbsAcRefNo().compareToIgnoreCase(
								object1.getCbsAcRefNo());
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

				results.values = mDisplayedList;
				results.count = mDisplayedList.size();
			} else if (constraint != null && constraint.length() > 0) {
				List<AgendaMaster> nHolderList = new ArrayList<AgendaMaster>();
				constraint = constraint.toString().toUpperCase();
				for (AgendaMaster h : dsbAgendaList) {

					if ((h.customerName.toUpperCase().contains(constraint))
							|| (h.agendaAmt.toUpperCase().contains(constraint))
							|| (h.cbsAcRefNo.toUpperCase().contains(constraint))) {
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
			notifyDataSetChanged();
		}
	}
}
