package com.bfsi.egalite.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bfsi.egalite.entity.LoanPaidSch;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private	List<LoanPaidSch> _listDataHeader; // header titles
	// child data in format of header title, child title
	private Map<LoanPaidSch, List<LoanPaidSch>> _listDataChild;

	public ExpandableListAdapter(Context context,List<LoanPaidSch> listDataHeader,
			Map<LoanPaidSch, List<LoanPaidSch>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@SuppressWarnings("unused")
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final LoanPaidSch childText = (LoanPaidSch) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.repay_customer_schedules_sublist, null);
		}
		TextView component = (TextView) convertView
				.findViewById(R.id.txv_repay_customer_schedule_component);
		TextView amtdue = (TextView) convertView
				.findViewById(R.id.txv_repay_customer_schedule_amtdue);
		TextView amtpaid = (TextView) convertView
				.findViewById(R.id.txv_repay_customer_schedule_amtpaid);
		amtpaid.setText(childText.getAmtSettled());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@SuppressWarnings("unused")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LoanPaidSch headerTitle =(LoanPaidSch) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.repay_customer_schedules_list, null);
		}
		TextView date = (TextView) convertView
				.findViewById(R.id.txv_repay_customer_schedule_date);
		TextView amtdue = (TextView) convertView
				.findViewById(R.id.txv_repay_customer_schedule_amtdue);
		TextView amtpaid = (TextView) convertView
				.findViewById(R.id.txv_repay_customer_schedule_amtpaid);
		
		amtpaid.setText(headerTitle.getAmtSettled());
		CommonContexts.setThemes(groupPosition, convertView);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
