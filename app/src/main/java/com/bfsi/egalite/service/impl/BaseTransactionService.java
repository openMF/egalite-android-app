package com.bfsi.egalite.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.util.AppPref;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.view.MfiApplication;

/**
 * @author vijay
 * 
 */
public abstract class BaseTransactionService  {

	/**
	 * Generate unique running transaction id with the patter <device
	 * id><transaction type: DB><date:yyyyMMdd><5 digit sequence number padded
	 * with leading zeros>
	 * 
	 * @return
	 */
	protected String generateTransactionId(String type) {
		Context context = MfiApplication.getInstance();
		String sequence = null;
//		if (type.equalsIgnoreCase("RP")) {
			sequence = generateTxnSequence(context, AppPref.TXN_RP_SEQUENCE);
//		} else if (type.equalsIgnoreCase("DB")) {
//			sequence = generateTxnSequence(context, AppPref.TXN_DB_SEQUENCE);
//		} else if (type.equalsIgnoreCase("CL")) {
//			sequence = generateTxnSequence(context, AppPref.TXN_CL_SEQUENCE);
//		} else if (type.equalsIgnoreCase("PY")) {
//			sequence = generateTxnSequence(context, AppPref.TXN_PM_SEQUENCE);
//		} else if (type.equalsIgnoreCase("TY")) {
//			sequence = generateTxnSequence(context, AppPref.TXN_RD_SEQUENCE);
//		} else if (type.equalsIgnoreCase("RR") || type.equalsIgnoreCase("NR")
//				|| type.equalsIgnoreCase("PR") || type.equalsIgnoreCase("DR")) {
//			sequence = generateTxnSequence(context,
//					AppPref.TXN_REQUEST_SEQUENCE);
//		} else if (type.equalsIgnoreCase("CW") || type.equalsIgnoreCase("CD")) {
//			sequence = generateTxnSequence(context, AppPref.TXN_CASH_SEQUENCE);
//		}
//
//		String txnid = getType() + getDeviceId() + sequence;
		
		String txnid = "T" + getDeviceId() + sequence;

		return txnid;
	}

	public static String generateEnrolSequence(String type) {
		Context context = MfiApplication.getInstance();
		String sequence = generateTxnSequence(context, AppPref.ENROL_SEQUENCE);
		String enrlid = "E" + getDeviceId() + sequence;

		return enrlid;
	}

	protected static String getDeviceId() {
		PreDataDao predao = DaoFactory.getPreDataDao();
		String id = predao.getDeviceId();
		if (id != null && id.length() >= 3)
			return id.substring(3);
		else
			return "00001";

	}

	@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
	protected static String generateTxnSequence(Context context,
			String txnSequence) {
		if (AppPref.getPrefInt(context, txnSequence) == 0) {
			AppPref.updatePref(context, txnSequence, 1);
		} else {
			int value = AppPref.getPrefInt(context, txnSequence) + 1;
			AppPref.updatePref(context, txnSequence, value);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
		String requireddate = sdf.format(DateUtil.getCurrentDataTime());

		int sequence = AppPref.getPrefInt(context, txnSequence);
		if (AppPref.getPref(context, AppPref.CURRENTDATE) != null) {
			Date existdate;
			Date currentdate;
			try {
				existdate = sdf.parse(AppPref.getPref(context,
						AppPref.CURRENTDATE));
				currentdate = sdf.parse(requireddate);
				
				if (currentdate.after(existdate)) {
					AppPref.updatePref(context, AppPref.CURRENTDATE,
							requireddate);
					AppPref.updatePref(context, txnSequence, 1);
				}
			} catch (ParseException e) {
				new ParseException(e.getMessage(), 0);
			} catch (Exception e) {
				Log.w(Constants.MFIAPP,Constants.UNABLE_PARSE_CURRENTDATE);

			}
		} else {
			AppPref.updatePref(context, AppPref.CURRENTDATE, requireddate);
		}

		return requireddate + String.format("%06d", sequence);
	}
	
	public static String generateEntrySequence(Context context) {
		
		if (AppPref.getPrefInt(context, AppPref.TXN_ENT_SEQUENCE) == 0) {
			AppPref.updatePref(context, AppPref.TXN_ENT_SEQUENCE, 1);
		} else {
			int value = AppPref.getPrefInt(context, AppPref.TXN_ENT_SEQUENCE) + 1;
			AppPref.updatePref(context, AppPref.TXN_ENT_SEQUENCE, value);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		String requireddate = sdf.format(DateUtil.getCurrentDataTime());

		int sequence = AppPref.getPrefInt(context, AppPref.TXN_ENT_SEQUENCE);
		if (AppPref.getPref(context, AppPref.CURRENTDATE) != null) {
			Date existdate;
			Date currentdate;
			try {
				existdate = sdf.parse(AppPref.getPref(context,
						AppPref.CURRENTDATE));
				currentdate = sdf.parse(requireddate);
				// while rolling over date, set transaction sequence to 1
				if (currentdate.after(existdate)) {
					AppPref.updatePref(context, AppPref.CURRENTDATE,
							requireddate);
					AppPref.updatePref(context, AppPref.TXN_ENT_SEQUENCE, 1);
				}
			} catch (ParseException e) {
				new ParseException(e.getMessage(), 0);
			} catch (Exception e) {
				Log.w(Constants.MFIAPP,Constants.UNABLE_PARSE_CURRENTDATE);

			}
		} else {
			AppPref.updatePref(context, AppPref.CURRENTDATE, requireddate);
		}

		return "CR"+requireddate + String.format("%06d", sequence);
	}
	

	public abstract String getType();
}
