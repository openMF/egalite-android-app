package com.bfsi.egalite.util;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;

/**
 * Responsible for Application shared preference
 * 
 * @author vijay
 * 
 */
public class AppPref {
	private static final String APP_PREF = "APP_PREF";
	public static final String CURRENTDATE = "CURRENTDATE";
	public static final String TXN_RP_SEQUENCE = "NUMBER_RP_SEQUENCE";
	public static final String TXN_DB_SEQUENCE = "NUMBER_DB_SEQUENCE";
	public static final String TXN_CL_SEQUENCE = "NUMBER_CL_SEQUENCE";
	public static final String TXN_PM_SEQUENCE = "NUMBER_PM_SEQUENCE";
	public static final String TXN_RD_SEQUENCE = "NUMBER_RD_SEQUENCE";
	public static final String TXN_ENT_SEQUENCE = "NUMBER_ENT_SEQUENCE";
	public static final String TXN_REQUEST_SEQUENCE = "NUMBER_REQUEST_SEQUENCE";
	public static final String TXN_CASH_SEQUENCE = "NUMBER_CASH_SEQUENCE";
	public static final String SESSION_SEQUENCE = "SESSION_SEQUENCE";
	public static final String INVALID_LOGIN = "INVALIDLOGIN";
	public static final String ISDEBUGON = "ISDEBUGON ";
	public static final String ENROL_SEQUENCE = "ENROL_SEQUENCE";
	public static final String ISDATAINSERT = "ISDATAINSERT";//To check whether data is already existed and to be skipped if existed.
	public static final String ISONLINESYNC = "ISONLINESYNC"; //Switch button in settings for broad receivers to call sync 
	public static final String ISFIRSTSYNC = "ISFIRSTSYNC"; //To navigate sync screen immediately after registration for first time
	public static final String SYNCCHECK = "SYNCCHECK"; //Sync permutassions 
	public static final String GROUPID = "GROUPID";
	public static final String CIPHER_KEY = "CIPHERKEY";
	
	public static final int CONTEXT_MODE = Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
	
	public static void updatePref(Context context, String pref, String value) {
		context.getSharedPreferences(APP_PREF, CONTEXT_MODE).edit()
				.putString(pref, value).commit();
	}

	public static void updatePref(Context context, String pref, int value) {
		context.getSharedPreferences(APP_PREF, CONTEXT_MODE).edit()
				.putInt(pref, value).commit();
	}
	
	public static void updatePref(Context context, String pref, boolean value) {
		context.getSharedPreferences(APP_PREF, CONTEXT_MODE).edit()
				.putBoolean(pref, value).commit();
	}
	public static void updatePref(Context context, String pref, HashSet<String> value) {
		context.getSharedPreferences(APP_PREF, CONTEXT_MODE).edit()
				.putStringSet(pref, value).commit();
	}
	public static boolean getPrefBoolean(Context context, String pref) {
		return context.getSharedPreferences(APP_PREF, CONTEXT_MODE)
				.getBoolean(pref, false);
	}
	public static int getPrefInt(Context context, String pref) {
		return context.getSharedPreferences(APP_PREF,CONTEXT_MODE)
				.getInt(pref, 0);
	}

	public static String getPref(Context context, String pref) {
		return context.getSharedPreferences(APP_PREF, CONTEXT_MODE)
				.getString(pref, null);
	}
	public static Set<String> getPrefSet(Context context, String pref) {
		return context.getSharedPreferences(APP_PREF, CONTEXT_MODE)
				.getStringSet(pref, null);
	}
}
