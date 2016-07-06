package com.bfsi.egalite.sync;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

import com.bfsi.egalite.util.AppPref;
import com.bfsi.egalite.util.DateUtil;

public class SyncUtil {

	public static String getSessionIdRandom(Context context) {
		if (AppPref.getPrefInt(context, AppPref.SESSION_SEQUENCE) == 0) {
			AppPref.updatePref(context, AppPref.SESSION_SEQUENCE, 1);
		} else {
			int value = AppPref.getPrefInt(context, AppPref.SESSION_SEQUENCE) + 1;
			AppPref.updatePref(context, AppPref.SESSION_SEQUENCE, value);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String requireddate = sdf.format(DateUtil.getCurrentDataTime());
	
		if (AppPref.getPref(context, AppPref.CURRENTDATE) != null) {
			try {
				Date existdate = sdf.parse(AppPref.getPref(context,
						AppPref.CURRENTDATE));
				Date currentdate = sdf.parse(requireddate);
				if (currentdate.after(existdate)) {
					AppPref.updatePref(context, AppPref.CURRENTDATE,
							requireddate);
					AppPref.updatePref(context, AppPref.SESSION_SEQUENCE, 1);
				}
			} catch (Exception e) {
				 e.printStackTrace();
			}
		} else {
			AppPref.updatePref(context, AppPref.CURRENTDATE, requireddate);
		}
		int sequence = AppPref.getPrefInt(context, AppPref.SESSION_SEQUENCE);
		return requireddate+String.format("%05d", sequence);
	}
}
