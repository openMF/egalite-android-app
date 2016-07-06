package com.bfsi.egalite.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bfsi.egalite.view.R;

/**
 * To access the customer details in general mode
 * @author Ashish
 *
 */
public class PopUpActivity extends Activity implements OnClickListener {

	Button ok_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custdetailsactivitypopup);
		ok_btn = (Button) findViewById(R.id.btn_custdetail_popup_ok);
		ok_btn.setOnClickListener(this);
		TextView mName = (TextView) findViewById(R.id.txv_custall_name);
		TextView mDob = (TextView) findViewById(R.id.txv_custall_dob);
		TextView mPhNo = (TextView) findViewById(R.id.txv_custall_phoneno);
		TextView mCustId = (TextView) findViewById(R.id.txv_custall_custid);
		Bundle bundle = getIntent().getExtras();
		mName.setText(bundle.getString(Constants.NAME));
		mDob.setText(bundle.getString(Constants.DOB));
		mPhNo.setText(bundle.getString(Constants.PHONE));
		mCustId.setText(bundle.getString(Constants.CUSTID));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_custdetail_popup_ok:
			this.finish();
			break;
		}

	}

}
