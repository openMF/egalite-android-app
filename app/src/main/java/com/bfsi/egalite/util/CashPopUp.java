package com.bfsi.egalite.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bfsi.egalite.view.R;

public class CashPopUp extends Activity implements OnClickListener {

	Button ok_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cashpopup);
		ok_btn = (Button) findViewById(R.id.btn_cash_popup_ok);
		ok_btn.setOnClickListener(this);
		TextView mName = (TextView) findViewById(R.id.txv_cashpopup_name);
		TextView mAccNo = (TextView) findViewById(R.id.txv_cashpopup_accno);
		TextView mAccId = (TextView) findViewById(R.id.txv_cashpopup_cashaccid);
		TextView mAccType = (TextView) findViewById(R.id.txv_cashpopup_acctype);
		TextView mLocationCode = (TextView) findViewById(R.id.txv_cashpopup_locationcode);
		mName.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getCustomerFullName());
		mAccNo.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getAcNo());
		mAccId.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getCustomerId());
		mAccType.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getAccType().equalsIgnoreCase("S")? "Savings" : "Current");
		mLocationCode.setText(CommonContexts.SELECTED_CASH_ACCOUNT.getLocationCode());
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_cash_popup_ok:
			this.finish();
			break;
		}

	}

}

