package com.bfsi.egalite.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.TabPageIndicator;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.deposit.collections.CollectionAgenda;
import com.bfsi.egalite.view.deposit.collections.CollectionEntry;

public class DpCollectionView extends BaseActivity {
	private static String[] CONTENT = null;
	private BfsiViewPager mPager;
	private TabPageIndicator mPageIndicator;
	public static FragmentStatePagerAdapter adapter;
	public SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();
	private View list_layout;

	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		String agenda = getString(R.string.Agenda);
		String entry = getString(R.string.Entry);
	
		CONTENT = new String[] { agenda, entry };
		CommonContexts.dismissProgressDialog();
		CommonContexts.mMfi = ((MfiApplication) getApplicationContext());
				
		list_layout = getLayoutInflater().inflate(R.layout.tabs_layout,null);
		mPager = (BfsiViewPager) list_layout.findViewById(R.id.pager);
		mPageIndicator = (TabPageIndicator) list_layout.findViewById(R.id.indicator);

		adapter = new FragmentsAdapterCollection(getSupportFragmentManager());
		mPager.setAdapter(adapter);
		mPager.setOffscreenPageLimit(0);

		mPageIndicator.setViewPager(mPager);

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(list_layout);

	}
	class FragmentsAdapterCollection extends FragmentStatePagerAdapter {

		public FragmentsAdapterCollection(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			int posi = mPageIndicator.getCurrentItem();
			if (posi == 0) {
				BaseActivity.mBtnLeft.setTag(R.drawable.home);
				BaseActivity.mBtnLeft.setImageResource(R.drawable.home);
				BaseActivity.mTitle.setText( R.string.screen_collection_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_CL_AGENDA;
			}
			switch (position) {
			case 0:
				return CollectionAgenda
						.newInstance(getApplicationContext(), mPager);
			case 1:
				return CollectionEntry.newInstance(DpCollectionView.this,mPager,mPageIndicator);
			
			default:
				return CollectionAgenda
						.newInstance(getApplicationContext(), mPager);

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
		super.onRightAction();
	}
	@Override
	protected void onLeftAction() {
		Object tag = BaseActivity.mBtnLeft.getTag();
		int id = tag == null ? -1 : (Integer) tag;
		if (id == R.drawable.home && mPager.getCurrentItem() == 0) {
			Intent cash = new Intent(DpCollectionView.this, HomeView.class);
			startActivity(cash);
			finish();
		} else if ((id == R.drawable.back || id==R.drawable.cancel)  && (mPager.getCurrentItem() == 1)) {
			super.onLeftAction();
		} else if ((id == R.drawable.imgsearch)  && (mPager.getCurrentItem() == 1)) {
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
		}
		return super.onKeyDown(keyCode, event);
	}

	private void handleBackPressed() {
			if(mPager.getCurrentItem()==0)
			{
				Intent cash = new Intent(DpCollectionView.this, HomeView.class);
				startActivity(cash);
				finish();
			}
			else if(mPager.getCurrentItem()==1)
			{
				CollectionEntry fragment = (CollectionEntry)mRegisteredFragments.get(mPager.getCurrentItem());
				fragment.myOnKeyDown();
			}
	}

	public void doExit() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				DpCollectionView.this);

		alertDialog.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						finish();
					}
				});
		alertDialog.setNegativeButton(getString(R.string.no), null);
		alertDialog.setMessage(getString(R.string.exit));
		alertDialog.setTitle(getString(R.string.exit_title));
		alertDialog.show();
	}
}
