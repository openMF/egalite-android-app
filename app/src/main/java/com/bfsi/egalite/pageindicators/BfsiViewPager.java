package com.bfsi.egalite.pageindicators;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Enable/Disable action events on pager
 * @author Vijay
 *
 */
public class BfsiViewPager extends ViewPager {

	public boolean enabled = true;
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public BfsiViewPager(Context context) {
		super(context);
	}

	public BfsiViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if(this.enabled)
			super.onPageScrolled(arg0, arg1, arg2);
		else
			super.onPageScrolled(0, 0f, 0);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(this.enabled)
		return super.onTouchEvent(arg0);
		else
			return true;
	}
}
