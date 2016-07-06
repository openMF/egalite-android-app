package com.bfsi.egalite.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.view.EnrolView;
import com.bfsi.egalite.view.R;
import com.bfsi.egalite.view.customers.CustomerQuery;

public class HomeRequestListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater layoutInflater;

	public HomeRequestListAdapter(Context c) {
		mContext = c;
		layoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mThumbNames.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.listitem, parent,
					false);
		} else {
			convertView = (View) convertView;
		}
		final TextView name = (TextView) convertView.findViewById(R.id.text);
		name.setText(mThumbNames[position]);
		RelativeLayout relLayout = (RelativeLayout) convertView
				.findViewById(R.id.Rellayout);
		relLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (name.getText().equals(Constants.CUST_REQ)) {
					Intent intent = new Intent(mContext, CustomerQuery.class);
					mContext.startActivity(intent);

				} else if (name.getText().equals(Constants.CUST_ENROLL)) {
					CommonContexts.ISENROLVISITED = true;
					CommonContexts.ISDBVISITED = false;
					CommonContexts.ISREPAYVISITED = false;
					CommonContexts.ISCOLLECTIONVISITED = false;
					CommonContexts.ISPAYMENTVISITED = false;
					Intent intent = new Intent(mContext, EnrolView.class);
					mContext.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	public String[] mThumbNames = { "Customer requests", "Customer Enrolment",
			"Customer Agenda", "New Deposit Request" };

}
