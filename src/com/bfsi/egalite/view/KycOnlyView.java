package com.bfsi.egalite.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.TabPageIndicator;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.customers.CustomersAll;
import com.bfsi.egalite.view.enrolkyc.KycBiometricProof;
import com.bfsi.egalite.view.enrolkyc.KycView;
import com.bfsi.mfi.rest.model.CustomerBiometricDetails;

public class KycOnlyView extends BaseActivity {
	private static String[] CONTENT = null;
	private BfsiViewPager mPager;
	private TabPageIndicator mPageIndicator;
	public static FragmentStatePagerAdapter adapter;
	SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		String kyc = getString(R.string.Kyc);
		String biometric = getString(R.string.Biometric);
		CONTENT = new String[] { kyc, biometric };
		CommonContexts.mMfi = ((MfiApplication) getApplicationContext());

		View list_layout = getLayoutInflater().inflate(R.layout.tabs_layout,
				null);
		CommonContexts.SELECTED_BIOMETRIC = new CustomerBiometricDetails();
		mPager = (BfsiViewPager) list_layout.findViewById(R.id.pager);
		mPageIndicator = (TabPageIndicator) list_layout
				.findViewById(R.id.indicator);
		adapter = new FragmentsAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);
		mPageIndicator.setViewPager(mPager);
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(list_layout);
	}

	class FragmentsAdapter extends FragmentStatePagerAdapter {

		public FragmentsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			int posi = mPageIndicator.getCurrentItem();
			if (posi == 0) {
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_KYC;
				BaseActivity.mBtnRight.setTag(R.drawable.save);
				BaseActivity.mBtnRight.setImageResource(R.drawable.save);
				BaseActivity.mBtnLeft.setTag(R.drawable.home);
				BaseActivity.mBtnLeft.setImageResource(R.drawable.home);
				BaseActivity.mBtnRight.setVisibility(View.GONE);
				BaseActivity.mTitle.setText(R.string.Kyc);
				BaseActivity.mTitle.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL);

			}

			switch (position) {
			case 0:
				return KycView.newInstance(getApplicationContext(), mPager);
			case 1:

				return KycBiometricProof.newInstance(getApplicationContext(),
						mPager, mPageIndicator);
			default:
				return KycView.newInstance(getApplicationContext(), mPager);
			}

		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = (Fragment) super.instantiateItem(container,
					position);
			mRegisteredFragments.put(position, fragment);
			return fragment;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			mRegisteredFragments.remove(position);
			super.destroyItem(container, position, object);
		}

		public Fragment getRegisteredFragment(int position) {
			return mRegisteredFragments.get(position);
		}

	}

	@Override
	protected void onRightAction() {

		Object tag = BaseActivity.mBtnRight.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if ((mPager.getCurrentItem() == 0)) {
			super.onRightAction();
		} else if ((mPager.getCurrentItem() == 1) && id == R.drawable.save||(id == R.drawable.verify) || (id == R.drawable.print)) {
			super.onRightAction();
		}

	}

	@Override
	protected void onLeftAction() {

		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (id == R.drawable.home && mPager.getCurrentItem() == 0) {
			Intent cash = new Intent(KycOnlyView.this, CustomersAll.class);
			startActivity(cash);
			finish();
		} else if ((id == R.drawable.back || id == R.drawable.cancel)
				&& mPager.getCurrentItem() == 0) {
			// Intent cash = new Intent(KycOnlyView.this, CustomersAll.class);
			// startActivity(cash);
			// finish();
			super.onLeftAction();
		} else if ((id == R.drawable.back || id == R.drawable.cancel)
				&& (mPager.getCurrentItem() == 1)) {
			super.onLeftAction();
		}
	}

	@Override
	public void onBackPressed() {
		handleBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			handleBackPressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void handleBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			KycView fragment = (KycView) mRegisteredFragments.get(mPager
					.getCurrentItem());
			fragment.myOnKeyDown();

		} else if (mPager.getCurrentItem() == 1) {
			KycBiometricProof fragment = (KycBiometricProof) mRegisteredFragments.get(mPager
					.getCurrentItem());
			fragment.myOnKeyDown();
		}

	}

}
