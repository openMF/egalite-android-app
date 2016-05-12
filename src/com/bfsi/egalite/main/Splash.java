package com.bfsi.egalite.main;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.dao.StaticDaoL;
import com.bfsi.egalite.dao.sqlite.DbHandlerL;
import com.bfsi.egalite.dao.sqlite.EnrolmentDataAccess;
import com.bfsi.egalite.entity.Agent;
import com.bfsi.egalite.entity.AppParams;
import com.bfsi.egalite.entity.Parameters;
import com.bfsi.egalite.listeners.NetworkLocationListener;
import com.bfsi.egalite.util.AppPref;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.view.LoginView;
import com.bfsi.egalite.view.R;

public class Splash extends Activity {

	private LocationManager mLocationManager;
	private NetworkLocationListener mNetworkLocationListener = new NetworkLocationListener();
	private Logger LOG = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the version of the CkCrypt2 class
		setContentView(R.layout.splash);
		DbHandlerL dbHelper = DbHandlerL.getInstance(Splash.this);
		
		
//		if (SystemUtil.isSimAvailable(Splash.this)) {
//			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//			if (mLocationManager
//					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//				mLocationManager.requestLocationUpdates(
//						LocationManager.NETWORK_PROVIDER, 5000, 5,
//						mNetworkLocationListener);
//			}
//		} else {
//			CommonContexts.NETWORKTIMESTAMP = Calendar.getInstance()
//					.getTimeInMillis();
//		}

		// if (isRooted() == true) {
		// Toast.makeText(Splash.this,
		// "Sorry, Device is not appropriate to run the app !!",
		// Toast.LENGTH_SHORT).show();
		// } else {
		// To initiate database

		CommonContexts.mLOGINVALIDATION = new Agent();
		CommonContexts.DEVICEID = getImei();
		int invaliddata = AppPref
				.getPrefInt(Splash.this, AppPref.INVALID_LOGIN);
		if (invaliddata == 0)
			AppPref.updatePref(Splash.this, AppPref.INVALID_LOGIN, 0);

		
		PreDataDao lgnDao = DaoFactory.getPreDataDao();
		try {
			LOG.debug(this.getResources().getString(R.string.MFB00130));
			Parameters params = lgnDao.readParameterValues(AppParams.HOST_NAME);
			if (params != null) {
				CommonContexts.SERVERIP = params.getValue();
				Parameters paramss = lgnDao
						.readParameterValues(AppParams.TEMP_HOST_NAME);
				CommonContexts.NONSERCURE_SERVERIP = paramss.getValue();
				Parameters paramsss = lgnDao
						.readParameterValues(AppParams.MBS_NO_OF_PRINT);
				if(paramsss != null)
				CommonContexts.NOOFPRINTS = Integer.parseInt(paramsss.getValue());
				
				Parameters paramToday = lgnDao
						.readParameterValues(AppParams.APP_DATE_TODAY);
				if(paramToday != null)
				CommonContexts.APP_TODAY = paramToday.getValue();
				
				Parameters paramAllowPartial = lgnDao
						.readParameterValues(AppParams.ALLOW_PARTIAL_SETTLEMENT);
				if(paramAllowPartial != null)
				CommonContexts.ALLOW_PARTIAL= paramAllowPartial.getValue();
				
				Parameters paramPartialPercent = lgnDao
						.readParameterValues(AppParams.ALLOWED_PARTIAL_PERCENT);
				if(paramPartialPercent != null)
				CommonContexts.PARTIAL_PERCENT = Integer.parseInt(paramPartialPercent.getValue());
				Parameters paramExternalDevice = lgnDao
						.readParameterValues(AppParams.USE_EXTERNAL_DEVICE);
				if(paramExternalDevice != null)
				CommonContexts.USE_EXTERNAL_DEVICE = paramExternalDevice.getValue()!=null?(paramExternalDevice.getValue().equalsIgnoreCase("Y")?true:false):false;
				
			}
		} catch (Exception e) {
			LOG.error(
					this.getResources().getString(R.string.MFB00130)
							+ e.getMessage(), e);

		}
//		insertDemoData();
		
	deviceId();    
		Thread timer = new Thread() {
			public void run() {
				try {
					LOG.debug(Splash.this.getResources().getString(
							R.string.MFB00130));
					sleep(3000);
				} catch (InterruptedException e) {
					LOG.error(
							Splash.this.getResources().getString(
									R.string.MFB00130)
									+ e.getMessage(), e);

					Toast.makeText(
							Splash.this,
							Splash.this.getResources().getString(
									R.string.MFB00130), Toast.LENGTH_SHORT)
							.show();
				} catch (Exception e) {
					LOG.error(
							Splash.this.getResources().getString(
									R.string.MFB00130)
									+ e.getMessage(), e);

					Toast.makeText(
							Splash.this,
							Splash.this.getResources().getString(
									R.string.MFB00130), Toast.LENGTH_SHORT)
							.show();
				} finally {
					Intent intent = new Intent(Splash.this, LoginView.class);
					startActivity(intent);
					finish();

				}
			}
		};
		timer.start();
		// }

	}

	private void insertDemoData()
	{
		int dataInserted = AppPref.getPrefInt(Splash.this, AppPref.ISDATAINSERT);
		if (dataInserted == 0) {
			StaticDaoL dao = DaoFactory.getStaticDao();
			dao.insertDsbData();
			dao.insertRepayData();
			dao.insertCollectionData();
			dao.insertPaymentsData();
			dao.insertloanPaidSchData();
			dao.insertCustDetail();
			dao.insertAgnLonDet();
			dao.insertDeptDet();
			dao.insertAppParm("sync_days", "0");
			dao.insertAppParm("password", "3");
			dao.insertAppParm("password_days", "15");
			dao.insertAppParm("password_length", "8");
			dao.insertBrchDts();
			dao.insertAgtCashRec();
			dao.insertDevDetl();
			dao.insertAgents();
			dao.insertCcyCodes();
			dao.insertMsgCode();
			dao.moduleCodesToUpdate();
			dao.insertCashData();
			EnrolmentDataAccess.insertLov();
			dao.insertErrorCodes();
			dao.insertAgendaCash();
			AppPref.updatePref(Splash.this, AppPref.ISDATAINSERT, 1);
		}
	}

	private void deviceId() {
		String device_id = null;
		TelephonyManager telemngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		device_id = telemngr.getDeviceId();
		CommonContexts.DEVICEID = device_id;
	}

	public String getImei() {
		String uniqueid = null;
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		uniqueid = telephonyManager.getDeviceId();
		return uniqueid;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLocationManager != null && mNetworkLocationListener != null) {
			if (mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
				mLocationManager.removeUpdates(mNetworkLocationListener);
		}
	}

	public void removeLocationUpdates() {

		if (mLocationManager != null && mNetworkLocationListener != null) {
			if (mLocationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				mLocationManager.removeUpdates(mNetworkLocationListener);
			}
		}
	}

	public static boolean findBinary(String binaryName) {
		boolean found = false;
		if (!found) {
			String[] places = { "/sbin/", "/system/bin/", "/system/xbin/",
					"/data/local/xbin/", "/data/local/bin/",
					"/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/" };
			for (String where : places) {
				if (new File(where + binaryName).exists()) {
					found = true;

					break;
				}
			}
		}
		return found;
	}

	private static boolean isRooted() {
		return findBinary("su");
	}
}
