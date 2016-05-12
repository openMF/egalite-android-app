package com.bfsi.egalite.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bfsi.egalite.entity.TxnCount;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.R;

public class EnrolRecordsAdapter extends BaseAdapter {
	private List<TxnCount> mEnrlCountList;
	private Context mContext;

	public EnrolRecordsAdapter(Context context, List<TxnCount> mTxnlist) {
		mContext = context;
		mEnrlCountList = mTxnlist;
	}

	public int getCount() {
		return mEnrlCountList.size();
	}

	public Object getItem(int position) {
		return mEnrlCountList.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	private class ViewHolder {
		TextView mName,mPending, mExecuted,mSynced,mReversed;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.agenda_records_listitems,
					parent, false);
			holder = new ViewHolder();
			holder.mName = (TextView) convertView
					.findViewById(R.id.txv_agenda_record_name_val);
			holder.mPending = (TextView) convertView
					.findViewById(R.id.txv_agenda_record_pending_val);
			holder.mExecuted = (TextView) convertView
					.findViewById(R.id.txv_agenda_record_executed_val);

			holder.mSynced = (TextView) convertView
					.findViewById(R.id.txv_agenda_record_synced_val);
			holder.mReversed = (TextView) convertView
					.findViewById(R.id.txv_agenda_record_total_val);
			convertView.setTag(holder);
		}

		else {

			holder = (ViewHolder) convertView.getTag();
		}
		CommonContexts.setThemes(position, convertView);
		holder.mName.setText(mEnrlCountList.get(position).getTxnName());
		holder.mPending.setText(mEnrlCountList.get(position).getPending());
		holder.mExecuted.setText(mEnrlCountList.get(position).getExecuted());
		holder.mSynced.setText(mEnrlCountList.get(position).getSynced());
		holder.mReversed.setText(mEnrlCountList.get(position).getReversed());
		
		return convertView;
	}
}
