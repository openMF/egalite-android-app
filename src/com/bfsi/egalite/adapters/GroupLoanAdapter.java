package com.bfsi.egalite.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bfsi.egalite.entity.GroupLoans;
import com.bfsi.egalite.view.R;

public class GroupLoanAdapter extends BaseAdapter {
	private List<GroupLoans> mGroupList;
	private Context mContext;

	public GroupLoanAdapter(Context context, List<GroupLoans> mGrouplist) {
		mContext = context;
		mGroupList = mGrouplist;
	}

	public int getCount() {
		return mGroupList.size();
	}

	public Object getItem(int position) {
		return mGroupList.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	private class ViewHolder {
		TextView mName, mAccNo, mAmt;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.groups_listitems,
					parent, false);
			holder = new ViewHolder();
			holder.mName = (TextView) convertView
					.findViewById(R.id.txv_groups_listitem_name);
			holder.mAccNo = (TextView) convertView
					.findViewById(R.id.txv_groups_listitem_accno);

			holder.mAmt = (TextView) convertView
					.findViewById(R.id.txv_groups_listitem_amt);
			convertView.setTag(holder);
		}

		else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.mName.setText(mGroupList.get(position).getCustomerName());
		holder.mAccNo.setText(mGroupList.get(position).getAcNo());
		holder.mAmt.setText(Double.toString(mGroupList.get(position)
				.getAgendaAmt()));
		return convertView;
	}
}