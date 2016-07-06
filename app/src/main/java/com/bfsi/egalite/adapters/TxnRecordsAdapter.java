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

public class TxnRecordsAdapter extends BaseAdapter {
	private List<TxnCount> mTxnCountList;
	private Context mContext;

	public TxnRecordsAdapter(Context context, List<TxnCount> mTxnlist) {
		mContext = context;
		mTxnCountList = mTxnlist;
	}

	public int getCount() {
		return mTxnCountList.size();
	}

	public Object getItem(int position) {
		return mTxnCountList.get(position);
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
		holder.mName.setText(mTxnCountList.get(position).getTxnName());
		holder.mPending.setText(mTxnCountList.get(position).getPending());
		holder.mExecuted.setText(mTxnCountList.get(position).getExecuted());
		holder.mSynced.setText(mTxnCountList.get(position).getSynced());
		holder.mReversed.setText(mTxnCountList.get(position).getReversed());
		
		return convertView;
	}
}

