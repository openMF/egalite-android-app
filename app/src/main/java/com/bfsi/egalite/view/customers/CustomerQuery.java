package com.bfsi.egalite.view.customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.LoansDao;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.view.HomeView;
import com.bfsi.egalite.view.R;

public class CustomerQuery extends BaseActivity {
	EditText custId, custName, phNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBtnLeft.setTag(R.drawable.back);
		mBtnLeft.setImageResource(R.drawable.back);
		mTitle.setText(getString(R.string.customerquery));
		loadWidgets();
		CommonContexts.dismissProgressDialog();
	}

	protected void onLeftAction() {
		Intent cash = new Intent(CustomerQuery.this, HomeView.class);
		startActivity(cash);
		finish();
	};

	private void loadWidgets() {
		View mView = getLayoutInflater().inflate(R.layout.customer_query, null);
		custId = (EditText) mView.findViewById(R.id.edt_cust_query_custid);
		custName = (EditText) mView.findViewById(R.id.edt_cust_query_custname);
		phNo = (EditText) mView.findViewById(R.id.edt_cust_query_phno);
		Button search = (Button) mView.findViewById(R.id.search);

		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!(custId.getText().toString().equals("")
						&& custName.getText().toString().equals("") && phNo
						.getText().toString().equals(""))) {
					CommonContexts.SELECTED_CUSTOMER = new CustomerDetail();
					CustomerDetail CustDetail = CommonContexts.SELECTED_CUSTOMER;
					CustDetail.setCustomerId(custId.getText() != null ? (custId
							.getText().toString().equals("") ? Constants.CUSTOMERID
							: custId.getText().toString()) : Constants.CUSTOMERID);
					CustDetail.setCustomerFullName(custName.getText() != null ? (custName
							.getText().toString().equals("") ? Constants.CUST_NAME
							: custName.getText().toString()) :  Constants.CUST_NAME);
					CustDetail.setMobileNumber(phNo.getText() != null ? (phNo
							.getText().toString().equals("") ? Constants.MBL_NO
							: phNo.getText().toString()) : Constants.MBL_NO);

					if (CustomerDetailList() != 0) {
						Intent intent = new Intent(CustomerQuery.this,
								CustomerView.class);
						startActivity(intent);
						finish();
						clearFields();

					} else {
						Toast.makeText(getApplicationContext(),
								Constants.NO_SEARCH, Toast.LENGTH_LONG)
								.show();
					}

				} else {
					Toast.makeText(getApplicationContext(),
							Constants.PLZ_ENTER_DELT , Toast.LENGTH_LONG)
							.show();
				}
			}

		
		});
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();

	}
	
	private void clearFields() {
		custId.setText("");
		custName.setText("");
		phNo.setText("");
	}

	public void myOnKeyDown() {

		startActivity(new Intent(CustomerQuery.this, HomeView.class));
		finish();
	}

	private int CustomerDetailList() {
		LoansDao clcDao = DaoFactory.getLoanDao();
		int listCount = 0;
		try {
			LOG.debug(this.getResources().getString(R.string.MFB00105));
			listCount = clcDao.cntCustomerDetails();
		} catch (DataAccessException e) {
			LOG.error(
					this.getResources().getString(R.string.MFB00105)
							+ e.getMessage(), e);

			Toast.makeText(this,
					this.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					this.getResources().getString(R.string.MFB00105)
							+ e.getMessage(), e);

			Toast.makeText(this,
					this.getResources().getString(R.string.MFB00105),
					Toast.LENGTH_SHORT).show();
		}
		return listCount;
	}

}
