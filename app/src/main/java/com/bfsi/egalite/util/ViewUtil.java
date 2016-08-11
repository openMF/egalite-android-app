package com.bfsi.egalite.util;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.view.R;

/**
 * Utility class for all common view operations like progress bar, (different
 * type of) prompts
 * 
 * @author Vijay
 * 
 */
public class ViewUtil {

	/**
	 * Shows error dialog
	 * @param context
	 * @param title
	 * @param message
	 */
	public static void showErrorDialog(Context context, int title, int message) {
		Builder alert = new AlertDialog.Builder(context);
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setPositiveButton(R.string.ok, null);
		alert.show();
	}

	/**
	 * Shows error below action bar w.r.t error type
	 * @param context
	 * @param messg
	 * @param handler
	 * @return handler would be called to update the same from UI thread.
	 */
	public static void showCrutonError(final Context context,int messg,final Handler handler) {
		Animation animFadein;
		int TIMER_DELAY = 3000;
		
		animFadein = AnimationUtils.loadAnimation(context,R.anim.bottom_to_top);
		BaseActivity.mLinearLayout.setVisibility(View.VISIBLE);
		BaseActivity.mLinearLayout.setBackgroundResource(R.color.red);
		BaseActivity.mTxvErrorMsg.setText(messg);
		BaseActivity.mLinearLayout.setAnimation(animFadein);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(CommonContexts.SUCCESS);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, TIMER_DELAY);
	}
}
