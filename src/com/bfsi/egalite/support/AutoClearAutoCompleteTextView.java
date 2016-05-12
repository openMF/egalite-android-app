/**
 * 
 */
package com.bfsi.egalite.support;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.bfsi.egalite.view.R;

/**
 * @author Vijay
 * 
 */
public class AutoClearAutoCompleteTextView extends AutoCompleteTextView {
	// was the text just cleared?
	public boolean justCleared = false;

	// The image we defined for the clear button
	public Drawable imgCloseButton = getResources().getDrawable(
			R.drawable.clear_search);

	/* Required methods, not used in this implementation */
	public AutoClearAutoCompleteTextView(Context context) {
		super(context);
		init();
	}

	/* Required methods, not used in this implementation */
	public AutoClearAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/* Required methods, not used in this implementation */
	public AutoClearAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	void init() {
		// Set the bounds of the button
		this.setCompoundDrawablesWithIntrinsicBounds(null, null,
				imgCloseButton, null);

		// button should be hidden on first draw
		clrButtonHandler();

		// if the clear button is pressed, clear it. Otherwise do nothing
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				AutoClearAutoCompleteTextView et = AutoClearAutoCompleteTextView.this;

				if (et.getCompoundDrawables()[2] == null)
					return false;

				if (event.getAction() != MotionEvent.ACTION_UP)
					return false;

				if (event.getX() > et.getWidth() - et.getPaddingRight()
						- imgCloseButton.getIntrinsicWidth()) {
					et.setText("");
					AutoClearAutoCompleteTextView.this.clrButtonHandler();
					justCleared = true;
				}
				return false;
			}
		});
	}
	public void clrButtonHandler() {
		if (this == null || this.getText().toString().equals("")
				|| this.getText().toString().length() == 0) {
			// remove clear button
			this.setCompoundDrawables(null, null, null, null);
		} else {
			// add clear button
			this.setCompoundDrawablesWithIntrinsicBounds(null, null,
					imgCloseButton, null);
		}
	}
	public void clearData(AutoClearAutoCompleteTextView completefield)
	{
		completefield.clrButtonHandler();
		justCleared = true;
		completefield.setText("");
	}

}
