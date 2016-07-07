package com.bfsi.egalite.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.LoanPaidSch;

public class PaidShedulAdapter extends BaseAdapter {
	private List<LoanPaidSch> mPaidList;
	private Context mContext;

	public PaidShedulAdapter(Context context, List<LoanPaidSch> mPaidlist) {
		mContext = context;
		mPaidList = mPaidlist;
	}

	public int getCount() {
		return mPaidList.size();
	}

	public Object getItem(int position) {
		return mPaidList.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	private class ViewHolder {
		TextView mAccNo, mPaidDt, mAmt;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.paidshedul_listitem,
					parent, false);
			holder = new ViewHolder();
			holder.mAccNo = (TextView) convertView
					.findViewById(R.id.txv_paisdhedul_listitem_accno);
			holder.mPaidDt = (TextView) convertView
					.findViewById(R.id.txv_paisdhedul_listitem_date);
			holder.mAmt = (TextView) convertView
					.findViewById(R.id.txv_paisdhedul_listitem_amt);
			convertView.setTag(holder);
		}

		else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.mAccNo.setText(mPaidList.get(position).getCbsAcRefNo());
		holder.mPaidDt.setText(CommonContexts.dateFormat.format(mPaidList.get(
				position).getSchPaidDate()));
		holder.mAmt.setText(mPaidList.get(position).getStlmtCcyCode()
				+ " "+Double.toString(mPaidList.get(position).getAmtSettled()));
		return convertView;
	}
}
