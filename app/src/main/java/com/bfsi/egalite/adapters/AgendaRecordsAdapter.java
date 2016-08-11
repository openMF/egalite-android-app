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

public class AgendaRecordsAdapter extends BaseAdapter {
	private List<TxnCount> mAgnCountList;
	private Context mContext;

	public AgendaRecordsAdapter(Context context, List<TxnCount> mTxnlist) {
		mContext = context;
		mAgnCountList = mTxnlist;
	}

	public int getCount() {
		return mAgnCountList.size();
	}

	public Object getItem(int position) {
		return mAgnCountList.get(position);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	private class ViewHolder {
		TextView mName,mPending, mExecuted,mSynced,mTotal;
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
			holder.mTotal = (TextView) convertView
					.findViewById(R.id.txv_agenda_record_total_val);
			convertView.setTag(holder);
		}

		else {

			holder = (ViewHolder) convertView.getTag();
		}
		CommonContexts.setThemes(position, convertView);
		holder.mName.setText(mAgnCountList.get(position).getTxnName());
		holder.mPending.setText(mAgnCountList.get(position).getPending());
		holder.mExecuted.setText(mAgnCountList.get(position).getExecuted());
		holder.mSynced.setText(mAgnCountList.get(position).getSynced());
		holder.mTotal.setText(mAgnCountList.get(position).getTotal());
		
		return convertView;
	}
}
