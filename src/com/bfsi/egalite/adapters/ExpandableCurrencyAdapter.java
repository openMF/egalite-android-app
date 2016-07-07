package com.bfsi.egalite.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.R;

public class ExpandableCurrencyAdapter  extends BaseExpandableListAdapter {

	private Activity context;
	private List<CashPosition> cashList;
	private ViewGroup vp;

	public ExpandableCurrencyAdapter(Activity context,
			List<CashPosition> repayArrayList) {
		this.context = context;
		this.cashList = repayArrayList;

	}

	public Object getChild(int groupPosition, int childPosition) {
		return cashList.get(groupPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_listitem, null);
		}

		TextView ccy = (TextView) convertView.findViewById(R.id.txv_ccy_val);
		TextView cashin = (TextView) convertView
				.findViewById(R.id.txv_cashin_val);
		TextView cashout = (TextView) convertView
				.findViewById(R.id.txv_cashout_val);
		TextView opencash = (TextView) convertView
				.findViewById(R.id.txv_openingbal_val);
		TextView cashpos = (TextView) convertView
				.findViewById(R.id.txv_currentbal_val);
		ccy.setText("- "+String.valueOf(cashList.get(groupPosition).getCcy()));
//		cashin.setText("- "+String.valueOf(cashList.get(groupPosition).getCashIn()));
//		cashout.setText("- "+String
//				.valueOf(cashList.get(groupPosition).getCashOut()));
//		opencash.setText("- "+String.valueOf(cashList.get(groupPosition)
//				.getOpenCash()));
//		cashpos.setText("- "+String.valueOf(cashList.get(groupPosition)
//				.getCashPosition()));
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public Object getGroup(int groupPosition) {
		return cashList.get(groupPosition);
	}

	public int getGroupCount() {
		return cashList.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public void onGroupExpanded(int groupPosition) {
		final ExpandableListView eLV = (ExpandableListView) vp;
		for (int i = 0; i < cashList.size(); i++) {
			if (i != groupPosition) {
				eLV.collapseGroup(i);
			}
		}
		super.onGroupExpanded(groupPosition);
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		vp = parent;
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.parent_listitem, null);
		}
		TextView ccy = (TextView) convertView
				.findViewById(R.id.txv_home_lay_listitem_ccy);
		TextView cashin = (TextView) convertView
				.findViewById(R.id.txv_home_lay_listitem_cashin);
		TextView cashout = (TextView) convertView
				.findViewById(R.id.txv_home_lay_listitem_cashout);
		TextView opencash = (TextView) convertView
				.findViewById(R.id.txv_home_lay_listitem_opencash);
		TextView cashpos = (TextView) convertView
				.findViewById(R.id.txv_home_lay_listitem_cashpostion);

		ccy.setText(String.valueOf(cashList.get(groupPosition).getCcy()));
//		cashin.setText(String.valueOf(cashList.get(groupPosition).getCashIn()));
//		cashout.setText(String
//				.valueOf(cashList.get(groupPosition).getCashOut()));
//		opencash.setText(String.valueOf(cashList.get(groupPosition)
//				.getOpenCash()));
//		cashpos.setText(String.valueOf(cashList.get(groupPosition)
//				.getCashPosition()));
		CommonContexts.setThemes(groupPosition, convertView);
		return convertView;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
