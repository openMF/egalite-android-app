

package com.bfsi.egalite.view.setting;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.util.TextViewEx;
import com.bfsi.egalite.view.R;


public class AboutView extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBtnLeft.setTag(R.drawable.back);
		mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mTitle.setText(R.string.about);
		mTitle.setText(getString(R.string.about));
		display();
	}
	@Override
	protected void onLeftAction() {
		startActivity(new Intent(AboutView.this, SettingView.class));
		finish();
	}

	private void display() {
		View aboutView = getLayoutInflater()
				.inflate(R.layout.about, null);
		TextViewEx txtViewEx = (TextViewEx)aboutView.findViewById(R.id.text_abt);
	    txtViewEx.setText(getString(R.string.txv_about_messg), true); 
		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(aboutView);

	}
	
	@Override
	public void onBackPressed() {
		myOnKeyDown();
	}
	
	public void myOnKeyDown() {
		startActivity(new Intent(AboutView.this, SettingView.class));
		finish();
	}

}
