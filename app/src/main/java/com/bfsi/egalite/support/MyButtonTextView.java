package com.bfsi.egalite.support;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;

/**
 * @author Vijay
 *
 */
public class MyButtonTextView {

    public static String setTextForButton(Context context, Button button,String text) {
    	String texts = null;
    	Typeface typeface = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    	button.setText(text);
    	button.setTypeface(typeface);
        return texts;
    }
  
}
