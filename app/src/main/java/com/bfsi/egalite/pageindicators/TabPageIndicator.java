package com.bfsi.egalite.pageindicators;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.view.R;
import com.bfsi.egalite.view.deposit.payments.PaymentEntry;
import com.bfsi.egalite.view.loan.disbursement.DsbEntry;
import com.bfsi.egalite.view.loan.repayment.RepayEntry;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
/**
 * @author Vijay
 * 
 */
public class TabPageIndicator extends HorizontalScrollView implements
		PageIndicator {

	/** Title text used when no title is provided by the adapter. */
	private static final CharSequence EMPTY_TITLE = "";
	public boolean enabled = true;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Interface for a callback when the selected tab has been reselected.
	 */
	public interface OnTabReselectedListener {
		/**
		 * Callback when the selected tab has been reselected.
		 * 
		 * @param position
		 *            Position of the current center item.
		 */
		void onTabReselected(int position);
	}

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			if (enabled) {
				TabView tabView = (TabView) view;
				final int oldSelected = mViewPager.getCurrentItem();
				final int newSelected = tabView.getIndex();
				mViewPager.setCurrentItem(newSelected);
				if (oldSelected == newSelected
						&& mTabReselectedListener != null) {
					mTabReselectedListener.onTabReselected(newSelected);
				}
			} else {
				return;
			}
		}
	};

	private final IcsLinearLayout mTabLayout;

	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;

	private int mMaxTabWidth;
	private int mSelectedTabIndex;

	private OnTabReselectedListener mTabReselectedListener;
	private Context mContext;

	public TabPageIndicator(Context context) {
		this(context, null);
		mContext = context;
	}

	public TabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setHorizontalScrollBarEnabled(false);

		mTabLayout = new IcsLinearLayout(context,
				R.attr.vpiTabPageIndicatorStyle);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,
				MATCH_PARENT));
	}

	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	private void addTab(int index, CharSequence text, int iconResId) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);
		tabView.setTypeface(CommonContexts.getTypeface(mContext));

		if (CommonContexts.THEME.equals(Constants.LIGHT)) {
			tabView.setTextColor(Color.BLACK);
		} else {
			tabView.setTextColor(Color.WHITE);
		}

		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
		}
		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0,
				MATCH_PARENT, 1));
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (mListener != null) {
			mListener.onPageScrollStateChanged(arg0);
		}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (mListener != null) {
			mListener.onPageScrolled(arg0, arg1, arg2);
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentItem(arg0);

		if (arg0 == 0) {
			CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_DSB_AGENDA;
			BaseActivity.mBtnLeft.setTag(R.drawable.home);
			BaseActivity.mBtnLeft.setImageResource(R.drawable.home);
			BaseActivity.mBtnRight.setVisibility(View.INVISIBLE);
			if (CommonContexts.ISDBVISITED) {
				BaseActivity.mTitle.setText(R.string.screen_dsb_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_DSB_AGENDA;
			} else if (CommonContexts.ISREPAYVISITED) {
				BaseActivity.mTitle.setText(R.string.screen_rpy_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_RP_AGENDA;
			} else if (CommonContexts.ISLOANPREPAYSELECTED) {
				BaseActivity.mTitle.setText(R.string.screen_loanprepay_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_lOAN_PREPAYAGENDA;
			}

			else if (CommonContexts.ISCOLLECTIONVISITED) {
				BaseActivity.mTitle.setText(R.string.screen_collection_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_CL_AGENDA;
			} else if (CommonContexts.ISPAYMENTVISITED) {
				BaseActivity.mTitle.setText(R.string.screen_payment_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_PM_AGENDA;
			} else if (CommonContexts.ISCUSTAGENDA) {
				BaseActivity.mTitle.setText(R.string.screen_cust_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_CUST_AGENDA;
			}
			else if (CommonContexts.ISKYCVIEW) {
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_KYC;
				BaseActivity.mBtnRight.setVisibility(View.VISIBLE);
				BaseActivity.mTitle.setText(R.string.Kyc);
				BaseActivity.mBtnRight.setTag(R.drawable.save);
				BaseActivity.mBtnRight.setImageResource(R.drawable.save);
				BaseActivity.mBtnRight.setVisibility(View.GONE);
				BaseActivity.mBtnLeft.setTag(R.drawable.home);
				BaseActivity.mBtnLeft.setImageResource(R.drawable.home);
				BaseActivity.mTitle.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL);

			}

		} else if (arg0 == 1) {
			BaseActivity.mBtnRight.setVisibility(View.VISIBLE);
			BaseActivity.mBtnRight.setTag(R.drawable.save);
			BaseActivity.mBtnRight.setImageResource(R.drawable.save);
			BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
			BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
			if (CommonContexts.ISDBVISITED) {
				BaseActivity.mTitle.setText(R.string.screen_dsb_entry);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_DSB_ENTRY;
			} else if (CommonContexts.ISREPAYVISITED) {
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_RP_ENTRY;
				BaseActivity.mTitle.setText(R.string.screen_rpy_entry);
			} else if (CommonContexts.ISLOANPREPAYSELECTED) {
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_lOAN_PREPAYENTRY;
				BaseActivity.mTitle.setText(R.string.screen_loanprepay_entry);
			} else if (CommonContexts.ISCOLLECTIONVISITED) {
				BaseActivity.mTitle.setText(R.string.screen_collection_entry);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_CL_ENTRY;
			} else if (CommonContexts.ISPAYMENTVISITED) {
				BaseActivity.mTitle.setText(R.string.screen_payment_entry);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_PM_ENTRY;
			} else if (CommonContexts.ISCUSTAGENDA) {
				BaseActivity.mTitle.setText(R.string.screen_cust_agenda);
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_CUST_ENTRY;
			}

			else if (CommonContexts.ISKYCVIEW) {
				CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_BIOMETRIC_KYC;
				BaseActivity.mBtnRight.setVisibility(View.VISIBLE);
				BaseActivity.mTitle.setText(R.string.Biometric);
				BaseActivity.mBtnRight.setTag(R.drawable.save);
				BaseActivity.mBtnRight.setImageResource(R.drawable.save);
				BaseActivity.mBtnLeft.setTag(R.drawable.back);
				BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
				BaseActivity.mTitle.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL);

			}
		}
		if (mListener != null) {
			mListener.onPageSelected(arg0);
		}
	}

	@SuppressWarnings("unused")
	private void checkVisited() {
		if (CommonContexts.ISDBVISITED)
			DsbEntry.clearFields();
		else if (CommonContexts.ISREPAYVISITED)
			RepayEntry.clearFields();
		else if (CommonContexts.ISPAYMENTVISITED)
			PaymentEntry.clearFields();
	}

	@Override
	public void setViewPager(ViewPager view) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		final PagerAdapter adapter = view.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException(
					Constants.VIEW_PAGER_NOTADAPTER);
		}
		mViewPager = view;
		view.setOnPageChangeListener(this);
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		PagerAdapter adapter = mViewPager.getAdapter();
		IconPagerAdapter iconAdapter = null;
		if (adapter instanceof IconPagerAdapter) {
			iconAdapter = (IconPagerAdapter) adapter;
		}
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			CharSequence title = adapter.getPageTitle(i);
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
			if (iconAdapter != null) {
				iconResId = iconAdapter.getIconResId(i);
			}
			addTab(i, title, iconResId);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	@Override
	public void setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException(Constants.VIEWPAGER_BOUND);
		}
		mSelectedTabIndex = item;
		mViewPager.setCurrentItem(item);

		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}

	public int getCurrentItem() {
		return mSelectedTabIndex;
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mListener = listener;
	}

	private class TabView extends TextView {
		private int mIndex;

		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPageIndicatorStyle);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}
	}
}
