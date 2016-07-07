package com.bfsi.egalite.support;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bfsi.egalite.util.CommonContexts;

/**
 * Sets the predefined text with typeface.
 * @author Vijay
 *
 */
public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MyTextView(Context context) {
        super(context);
        init();
    }
    private void init() {
    	if(CommonContexts.LANG.equalsIgnoreCase("EN")|| CommonContexts.LANG.equalsIgnoreCase("TAGALOG"))
    	{
	    	Typeface tf= Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
	        setTypeface(tf);
    	}
    	else if(CommonContexts.LANG.equalsIgnoreCase("KHMER"))
    	{
	    	Typeface tf= Typeface.createFromAsset(getContext().getAssets(), "KhmerUI.ttf");
	        setTypeface(tf);
    	}
    	else
    	{
    	    	Typeface tf= Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
    	        setTypeface(tf);
    	}
    }
}
