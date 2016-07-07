package com.bfsi.egalite.view.setting;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bfsi.egalite.dao.CashPositionDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.entity.CashPosition;
import com.bfsi.egalite.entity.CcyCodes;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.DashboardView;
import com.bfsi.egalite.view.R;

public class SetCashPositionView extends BaseActivity {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private ArrayList<String> currencyData;
	private EditText edtpostion;
	private String currencyval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBtnRight.setTag(R.drawable.cancel);
		mBtnLeft.setTag(R.drawable.save);
		mBtnRight.setImageResource(R.drawable.save);
		mBtnLeft.setImageResource(R.drawable.cancel);

		getCurrencyList();
		display();
	}

	@Override
	protected void onLeftAction() {
		startActivity(new Intent(SetCashPositionView.this, SettingView.class));
		finish();
	}

	@Override
	protected void onRightAction() {
		updateData();
	}

	protected void updateData() {

		if (edtpostion.getText().toString().isEmpty()) {
			edtpostion.setError(getString(R.string.messg_enterpostion));

		} else {
			Double postionval = null;
			postionval = Double.parseDouble(edtpostion.getText().toString());

			// read the existing cashposition value
			String cashPosition = readCashPosition(currencyval);
			Double exitCash = Double.parseDouble(cashPosition.toString());
			postionval = exitCash > 0 ? (exitCash + postionval) : postionval;

			CashPosition updateCash = new CashPosition();
			//updateCash.setCashPosition(postionval);
			updateCash.setCcy(currencyval);
			// updatePostion(updateCash);

			Intent intent = new Intent(SetCashPositionView.this,
					DashboardView.class);
			startActivity(intent);
			finish();
		}

	}

	private String readCashPosition(String currency) {
		String cashPosition = null;
		@SuppressWarnings("unused")
		CashPositionDao cpaDao = DaoFactory.getCashDao();
		try {
			LOG.debug(SetCashPositionView.this.getResources().getString(
					R.string.MFB00101));
			// cashPosition = cpaDao.readCashPosition(currency);

		} catch (DataAccessException e) {
			LOG.error(
					SetCashPositionView.this.getResources().getString(
							R.string.MFB00101)
							+ e.getMessage(), e);
			Toast.makeText(SetCashPositionView.this,
					this.getResources().getString(R.string.MFB00101),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					SetCashPositionView.this.getResources().getString(
							R.string.MFB00101)
							+ e.getMessage(), e);
			Toast.makeText(SetCashPositionView.this,
					this.getResources().getString(R.string.MFB00101),
					Toast.LENGTH_SHORT).show();
		}
		return cashPosition;
	}

	private void display() {
		View dummy_cash_view = getLayoutInflater().inflate(
				R.layout.setcashpostion, null);
		final Spinner curreny_dropdaown = (Spinner) dummy_cash_view
				.findViewById(R.id.spinner_sel_cashpostion_currency);

		edtpostion = (EditText) dummy_cash_view
				.findViewById(R.id.edt_setcashpostion);
		edtpostion.setTypeface(CommonContexts
				.getTypeface(SetCashPositionView.this));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_text, currencyData);
		dataAdapter.setDropDownViewResource(R.layout.spinner_text_black);
		curreny_dropdaown.setAdapter(dataAdapter);
		curreny_dropdaown
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						currencyval = curreny_dropdaown.getSelectedItem()
								.toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(dummy_cash_view);
	}

	/**
	 * Getting cash position data from running cash position table
	 */
	public void getCurrencyList() {
		List<CcyCodes> currencyList = null;
		CashPositionDao cpda = DaoFactory.getCashDao();
		try {
			LOG.debug(SetCashPositionView.this.getResources().getString(
					R.string.MFB00102));
			currencyList = cpda.readCurrencyList();
		} catch (DataAccessException e) {
			LOG.error(
					SetCashPositionView.this.getResources().getString(
							R.string.MFB00102)
							+ e.getMessage(), e);
			Toast.makeText(SetCashPositionView.this,
					this.getResources().getString(R.string.MFB00102),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					SetCashPositionView.this.getResources().getString(
							R.string.MFB00102)
							+ e.getMessage(), e);
			Toast.makeText(SetCashPositionView.this,
					this.getResources().getString(R.string.MFB00102),
					Toast.LENGTH_SHORT).show();
		}

		currencyData = new ArrayList<String>();
		for (CcyCodes currency : currencyList)
			currencyData.add(currency.getCcyCode());
	}

	@Override
	public void onBackPressed() {
		myOnKeyDown();
	}

	public void myOnKeyDown() {
		startActivity(new Intent(SetCashPositionView.this, SettingView.class));
		finish();
	}
}
