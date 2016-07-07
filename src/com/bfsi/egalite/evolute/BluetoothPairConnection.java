package com.bfsi.egalite.evolute;

import java.lang.reflect.Method;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;

/**
 * Private API calls Tools Bluetooth interface
 * */
public class BluetoothPairConnection {
	/** Constants: Bluetooth pairing binding filter listener name */
	static public final String PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
	/**
	 * Pairing Bluetooth devices
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	private static Context mContext;
	private boolean mbBleStatusBefore = false;
	private int SUCCESS;
	// private static String DEVICENAME = "ESYS00013";

//	private static String DEVICEMAC = "00:06:66:61:3C:4C";
//	private static String DEVICEMAC = "00:06:66:61:3A:FE";
	private static String DEVICEMAC = "00:06:66:61:3C:42";
//	private static String DEVICEMAC = "00:06:66:A0:43:07";//Device which is with Vikash
	//Need to asign device mac from mLoginValidations
//	private static String DEVICEMAC = CommonContexts.mLOGINVALIDATION.getMacId();
	

	private static EvoluteCallBack evoluteCallBack;

	static public boolean createBond(BluetoothDevice btDevice) throws Exception {
		Log.e(Constants.BLUETOOTH_CTRL,"<<<<<<<<<CREATE BOND>>>>>>>>>>");
		Class<? extends BluetoothDevice> btClass = btDevice.getClass();
		Method createBondMethod = btClass.getMethod("createBond");
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * Lift the Bluetooth device pairing
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	static public boolean removeBond(BluetoothDevice btDevice) throws Exception {
		Log.e(Constants.BLUETOOTH_CTRL,"<<<<<<<<<CREATE BOND>>>>>>>>>>");
		Class<? extends BluetoothDevice> btClass = btDevice.getClass();
		Method removeBondMethod = btClass.getMethod("removeBond");
		Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * Setting a passcode
	 * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
	 */
	static public boolean setPin(BluetoothDevice btDevice, String str)
			throws Exception {
		Log.e(Constants.BLUETOOTH_CTRL,"<<<<<<<<<CREATE BOND>>>>>>>>>>" + str);
		Boolean returnValue = false;
		try {
			Class<? extends BluetoothDevice> btClass = btDevice.getClass();
			Method removeBondMethod = btClass.getDeclaredMethod("setPin",
					new Class[] { byte[].class });
			// byte[] ar = new byte[]{0x31,0x32,0x33,0x34};
			// Method removeBondMethod =
			// btClass.getMethod("setPin",new
			// Class[]{Array.newInstance(byte.class,4).getClass()});
			// Boolean returnValue = (Boolean) removeBondMethod.invoke(btDevice,
			// ar);
			returnValue = (Boolean) removeBondMethod.invoke(btDevice,
					new Object[] { str.getBytes() });
			Log.d(Constants.RETURNVALUE, ">>setPin:" + returnValue.toString());
		} catch (SecurityException e) {
			// throw new RuntimeException(e.getMessage());
			Log.e(Constants.RETURNVALUE, ">>setPin:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// throw new RuntimeException(e.getMessage());
			Log.e(Constants.RETURNVALUE, ">>setPin:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(Constants.RETURNVALUE, ">>setPin:" + e.getMessage());
			e.printStackTrace();
		}
		return returnValue.booleanValue();
	}

	/**
	 * cancel Pairing User Input
	 * @see android.permission.WRITE_SECURE_SETTINGS<br/>
	 *      Permission is only granted to system apps
	 * */
	static public boolean cancelPairingUserInput(BluetoothDevice btDevice)
			throws Exception {
		Log.e(Constants.BLUETOOTH_CTRL,
				"<<<<<<<<<<<<<<cancelPairingUserInput>>>>>>>>>>>>>>");
		Class<? extends BluetoothDevice> btClass = btDevice.getClass();
		Method createBondMethod = btClass.getMethod(Constants.CANCLE_PAIRINGUSR);
		// cancelBondProcess()
		Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
		return returnValue.booleanValue();
	}

	/**
	 * cancel Bond Process
	 * @see android.permission.WRITE_SETTINGS
	 * */
	static public boolean cancelBondProcess(BluetoothDevice btDevice)
			throws Exception {
		Log.e(Constants.BLUETOOTH_CTRL,
				"<<<<<<<<<<<<<<cancelBondProcess>>>>>>>>>>>>>>");
		Boolean returnValue = false;
		try {
			Class<? extends BluetoothDevice> btClass = btDevice.getClass();
			Method createBondMethod = btClass.getMethod("cancelBondProcess");
			returnValue = (Boolean) createBondMethod.invoke(btDevice);
		} catch (SecurityException e) {
			// throw new RuntimeException(e.getMessage());
			Log.e(Constants.RETURNVALUE,">>cancelBondProcess:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// throw new RuntimeException(e.getMessage());
			Log.e(Constants.RETURNVALUE, ">>cancelBondProcess:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(Constants.RETURNVALUE, ">>cancelBondProcess:"  + e.getMessage());
			e.printStackTrace();
		}
		return returnValue.booleanValue();
	}

	public static void blueToothOnOff(Context context,EvoluteCallBack callback) {
		mContext = context;
		evoluteCallBack = callback;
		BluetoothPairConnection bp = new BluetoothPairConnection();
		bp.new startBluetoothDeviceTask().execute("");
	}

	public class startBluetoothDeviceTask extends
			AsyncTask<String, String, Integer> {

		private static final int RET_BULETOOTH_IS_START = 0x0001;
		private static final int RET_BLUETOOTH_START_FAIL = 0x04;
		private static final int miWATI_TIME = 15;
		private static final int miSLEEP_TIME = 150;

		@Override
		public void onPreExecute() {
			Toast.makeText(mContext, "Starting BLueTooth ON/OFF",
					Toast.LENGTH_LONG).show();
			mbBleStatusBefore = CommonContexts.mBT.isEnabled();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			int iWait = miWATI_TIME * 1000;
			/* BT isEnable */
			if (!CommonContexts.mBT.isEnabled()) {
				CommonContexts.mBT.enable();
				// Wait miSLEEP_TIME seconds, start the Bluetooth device before
				// you start scanning
				while (iWait > 0) {
					if (!CommonContexts.mBT.isEnabled())
						iWait -= miSLEEP_TIME;
					else
						break;
					
				}
				if (iWait < 0)
					return RET_BLUETOOTH_START_FAIL;
			}
			return RET_BULETOOTH_IS_START;
		}

		/**
		 * After blocking cleanup task execution
		 */
		@Override
		public void onPostExecute(Integer result) {
			if (RET_BLUETOOTH_START_FAIL == result) {
				Toast.makeText(mContext, Constants.BLUETOOTH_ON_OFF,
						Toast.LENGTH_LONG).show();
			} else if (RET_BULETOOTH_IS_START == result) {
				BluetoothPairConnection.pairToDevice(mContext,evoluteCallBack);
			}
		}
	}

	public static void pairToDevice(Context context,EvoluteCallBack callback) {
		mContext = context;
		evoluteCallBack  = callback;
		// CommonContexts.mGP = (MfiApplication)mContext;
		CommonContexts.mBDevice = CommonContexts.mBT.getRemoteDevice(DEVICEMAC);
		if (CommonContexts.mBDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
			try {
				System.out.println("Not Bounded...................>");
				BluetoothPairConnection bp = new BluetoothPairConnection();
				bp.new PairTask().execute(DEVICEMAC);
			} catch (Exception e) {
				e.printStackTrace();
			}
			CommonContexts.mbBonded = false;
		} else if (CommonContexts.mBDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
			try {
				// BluetoothPair.createBond(mBDevice);
				checkConnection(mContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			CommonContexts.mbBonded = false;
		}
	}

	public class PairTask extends AsyncTask<String, String, Integer> {
		/** Constants: the pairing is successful */
		static private final int RET_BOND_OK = 0x00;
		/** Constants: Pairing failed */
		static private final int RET_BOND_FAIL = 0x01;
		/** Constants: Pairing waiting time (15 seconds) */
		static private final int iTIMEOUT = 1000 * 15;

		/**
		 * Thread start initialization
		 */
		@Override
		public void onPreExecute() {
			mContext.registerReceiver(CommonContexts._mPairingRequest,
					new IntentFilter(BluetoothPairConnection.PAIRING_REQUEST));
			mContext.registerReceiver(CommonContexts._mPairingRequest,
					new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
		}
		@Override
		protected Integer doInBackground(String... arg0) {
			final int iStepTime = 250;
			int iWait = iTIMEOUT;
			try {
				CommonContexts.mBDevice = CommonContexts.mBT
						.getRemoteDevice(DEVICEMAC);
				BluetoothPairConnection.createBond(CommonContexts.mBDevice);
				CommonContexts.mbBonded = false;
			} catch (Exception e1) {
				// Log.d(R.string.app_name, "create Bond failed!");
				e1.printStackTrace();
				return RET_BOND_FAIL;
			}
			while (!CommonContexts.mbBonded && iWait > 0) {
				// need to chek
				SystemClock.sleep(iStepTime);
				iWait -= iStepTime;
			}
			if (iWait > 0) {
				Log.e(Constants.APPLICATION,Constants.CREATE_FAILED_OK);
			} else {
				Log.e(Constants.APPLICATION, Constants.CREATE_BOND_FAILED);
			}
			return (int) ((iWait > 0) ? RET_BOND_OK : RET_BOND_FAIL);
		}

		@Override
		public void onPostExecute(Integer result) {
			if(!(RET_BOND_FAIL == result))
			{
				mContext.unregisterReceiver(CommonContexts._mPairingRequest);
			}
			if (RET_BOND_OK == result) {
				Toast.makeText(mContext, Constants.PAIRING_SUCCESS,
						Toast.LENGTH_SHORT).show();
				CommonContexts.mhtDeviceInfo.put("BOND", "Bounded");
				checkConnection(mContext);
			} else {
				Toast.makeText(mContext, Constants.PAIRING_FAILED,Toast.LENGTH_SHORT)
						.show();
				try {
					BluetoothPairConnection.removeBond(CommonContexts.mBDevice);
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		}
	}

	public static void checkConnection(Context context) {
		mContext = context;
		System.out.println("Bounded...................>");
		// CommonContexts.mGP = ((MfiApplication) context);
		BluetoothPairConnection bp = new BluetoothPairConnection();
		bp.new connSocketTask().execute(DEVICEMAC);
	}

	public class connSocketTask extends AsyncTask<String, String, Integer> {
		/** Process waits prompt box */
		private ProgressDialog mpd = null;
		/** Constants: connection fails */
		private static final int CONN_FAIL = 0x01;
		/** Constant: the connection is established */
		private static final int CONN_SUCCESS = 0x02;

		/**
		 * Thread start initialization
		 */

		@Override
		public void onPreExecute() {
			Toast.makeText(mContext, Constants.CONNECTING_DEV,Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		protected Integer doInBackground(String... arg0) {
			if (CommonContexts.mMfi.createConn(arg0[0])) {
				SystemClock.sleep(3000);
				return CONN_SUCCESS;
			} else {
				return CONN_FAIL;
			}
		}

		/**
		 * After blocking cleanup task execution
		 */
		@Override
		public void onPostExecute(Integer result) {
			if (CONN_SUCCESS == result) {
				CommonContexts.CHECK = CONN_SUCCESS;
				evoluteCallBack.doPrinters(CommonContexts.CHECK);
				
			} else {
				Toast.makeText(mContext,
						Constants.CONNEC_FAILED,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
