package com.bfsi.egalite.evolute;

import java.math.BigInteger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.view.LoginView;
import com.bfsi.egalite.view.R;
import com.impress.api.FPS;
import com.impress.api.FpsConfig;

public class FingerPrintAsync implements EvoluteCallBack {
	static byte[] bufvalueTmplt = {};
//	static byte[] bufValueImgData = {};
	byte[] image = {};
	byte[] image1 = {};
	byte[] asd = {};
	byte[] rgb = {};
	private int returnvalue, verifyvalue;
	boolean conStatus = false;
	FpsConfig fpsconfig = new FpsConfig();
	public static final int FAILURE = -1;
	public static final int TIME_OUT = -2;
	public static final int PARAMETER_ERROR = -3;
	public static final int SUCCESS = -5;
	public static final int FPSMANY_FAIL = -120;
	public static final int FPSMANY_SUCESS = -124;
	public static final int FPSMANY_Livefingernotmatch = -121;
	public static final int FPSMANY_Livefingernotmatchanyfinger = -122;
	public static final int FPSMANY_Livefingernotmatchindex = -123;
	public static final int VERIFYMATCH_INVALID_RESPONSE = -7;
	public static final int FPS_ILLEGAL_LIBRARY = -50;

	public static final int FPS_DEMO_VERSION = -51;
	public static final int FPS_INACTIVE_PERIPHERAL = -52;
	public static final int FPS_INVALID_DEVICE_ID = -53;
	public static final int NO_FINGER = 0;
	public static final int MOVE_FINGER_UP = 1;
	public static final int MOVE_FINGER_DOWN = 2;
	public static final int MOVE_FINGER_LEFT = 3;
	public static final int MOVE_FINGER_RIGHT = 4;
	public static final int LATENT = 6;
	public static final int DEVICE_NOTCONNECTED = -100;
	public static final int FINGER_OK = 8;
	public static final int FIRST_CAPTURE = -110;

	public boolean VerifyFinger = false;
	public boolean verifycompressed = false;
	public boolean verifyuncompressed = false;
	ImageView img;
	EditText input;
	int ret7;
	byte[][] byteTemplates;
	ImageView img1;
	public static FPS fps;
	byte[] value;
	private static Context contexts;
	private static ImageView mImageView;
	private static String imageType;
	
	private static int fingerQuality,noOfMinutiae;

	public void fetchBiometric(Context context, ImageView imageView,
			String imageType) {
		this.imageType = imageType;
		this.contexts = context;
		this.mImageView = imageView;
		if (CommonContexts.mBT.isEnabled()) {

			if (CommonContexts.CHECK == 0x02) {
				FingerPrintAsync.captureFingure(contexts);

			} else {
				BluetoothPairConnection
						.pairToDevice(contexts, FingerPrintAsync.this);
			}

		} else {

			BluetoothPairConnection.blueToothOnOff(contexts, FingerPrintAsync.this);
		}
	}

	// Context context = this;
	public static void captureFingure(Context context) {

		fps = new FPS(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
	
//		fingerPositionThread();
		FingerPrintAsync fp = new FingerPrintAsync();
		fp.new CaptureFinger().execute(0);
		
		
	}
	
	private static void fingerPositionThread()
	{
		new Thread()
 {
			public void run() {
				// Set the availability for asynchronous input.
				fps.vSetAsyncInstance();
				do {
					try {
						// returns the current captured finger quality.
						int qual = fps.iGetCurrentFingerQuality();

						// returns the current Finger Position.
						int pos = fps.iGetCurrentFingerPosition();
						String stPos = "";
						switch (pos) {
						case FPS.NO_FINGER:
							stPos = "NO_FINGER";
							break;
						case FPS.MOVE_FINGER_UP:
							stPos = "MOVE_FINGER_UP";
							break;
						case FPS.MOVE_FINGER_DOWN:
							stPos = "MOVE_FINGER_DOWN";
							break;
						case FPS.MOVE_FINGER_LEFT:
							stPos = "MOVE_FINGER_LEFT";
							break;
						case FPS.MOVE_FINGER_RIGHT:
							stPos = "MOVE_FINGER_RIGHT";
							break;
						case FPS.PRESS_FINGER_HARDER:
							stPos = "PRESS_FINGER_HARDER";
							break;
						case FPS.LATENT:
							stPos = "LATENT";
							break;
						case FPS.REMOVE_FINGER:
							stPos = "REMOVE_FINGER";
							break;
						case FPS.FINGER_OK:
							stPos = "FINGER_OK";
							break;
						}
						if (!stPos.equals(""))

							Message.obtain(fpshandlerPosition, 1, qual, 0,
									stPos).sendToTarget();
						// The process is repeated every second to know
						// the current update of quality and position
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// The thead is run in loop until the asynchronous data is
					// available.
				} while (fps.blGetAsyncInstance());
			}
		}.start();
				
	}

	/* This method shows the CaptureFinger AsynTask operation */
	public class CaptureFinger extends AsyncTask<Integer, Integer, Integer> {
		/* displays the progress dialog until background task is completed */
		@Override
		protected void onPreExecute() {
			if (imageType.equalsIgnoreCase("RHTF")) {
				CommonContexts.showProgress(contexts,
						Constants.PLACE_RIGHT_THUMB);
			} else if (imageType.equalsIgnoreCase("RHIF")) {
				CommonContexts.showProgress(contexts,
						Constants.PLACE_RIGHT_INDX);
			} else if (imageType.equalsIgnoreCase("RHMF")) {
				CommonContexts.showProgress(contexts,
						Constants.PLACE_RIGHT_MIDDLE);
			} else if (imageType.equalsIgnoreCase("RHRF")) {
				CommonContexts.showProgress(contexts,
						Constants.PLACE_RIGHT_FINGR);
			} else if (imageType.equalsIgnoreCase("RHLF")) {
				CommonContexts.showProgress(contexts,
					Constants.PLACE_RIGHT_LITTLE);
			} else if (imageType.equalsIgnoreCase("LHTF")) {
				CommonContexts.showProgress(contexts,
						"Please Place your Left Hand Thumb Finger");
			} else if (imageType.equalsIgnoreCase("LHIF")) {
				CommonContexts.showProgress(contexts,
						Constants.PLACE_LEFT_INDEX);
			} else if (imageType.equalsIgnoreCase("LHMF")) {
				CommonContexts.showProgress(contexts,
						Constants.PLACE_LEFT_MIDDLE);
			} else if (imageType.equalsIgnoreCase("LHRF")) {
				CommonContexts.showProgress(contexts,
						Constants.PLACE_LEFT_RING);
			} else if (imageType.equalsIgnoreCase("LHLF")) {
				CommonContexts.showProgress(contexts,
						Constants.PLACE_LEFT_LITTLE);
			}

			super.onPreExecute();
		}

		/* Task of CaptureFinger performing in the background */
		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				bufvalueTmplt = new byte[3500];
//				bufValueImgData = new byte[150000];
				
				fpsconfig = new FpsConfig(0, (byte) 0x0F);
				bufvalueTmplt = fps.bFpsCaptureTemplate(fpsconfig);
//				fps.iGetFingerImageUnCompressed(bufvalueTmplt, fpsconfig);
//				bufValueImgData = fps.bGetImageData();
				if((bufvalueTmplt != null /*&& bufvalueTmplt.length>0*/))
				{
					String templateS = convertToHex(bufvalueTmplt);
//					fingerQuality =  new BigInteger(templateS.substring(76, 78), 16).intValue();
					noOfMinutiae =  new BigInteger(templateS.substring(78, 80), 16).intValue();
				}
				returnvalue = fps.iGetReturnCode();

			} catch (NullPointerException e) {
				returnvalue = DEVICE_NOTCONNECTED;
				return returnvalue;
			}
			return returnvalue;
		}

		/*
		 * This function sends message to handler to display the status messages
		 * of Diagnose in the dialog box
		 */
		@Override
		protected void onPostExecute(Integer result) {
			// dialog.dismiss();
			CommonContexts.dismissProgressDialog();
			if (result == DEVICE_NOTCONNECTED) {
				fpshandler.obtainMessage(DEVICE_NOTCONNECTED,
						Constants.DEV_NOT_CONNC).sendToTarget();
			} else if (result == FPS.SUCCESS) {
				fpshandler.obtainMessage(SUCCESS, Constants.CAPTURE_SUCCESS)
						.sendToTarget();
				
				if(noOfMinutiae>30 /*&& fingerQuality>35*/)
				{
					mImageView.setTag(R.drawable.imgfinger);
					mImageView.setImageResource(R.drawable.imgfinger);
					addFingerByteData();
				}
				else
				{
					fpshandler.obtainMessage(SUCCESS, Constants.NOOF_MINUTIAE_NOT_ENOUGH).sendToTarget();
				}
				
			} else if (result == FPS.FPS_INACTIVE_PERIPHERAL) {
				fpshandler.obtainMessage(FPS_INACTIVE_PERIPHERAL,
						"Peripheral is inactive").sendToTarget();
			} else if (result == FPS.TIME_OUT) {
				fpshandler.obtainMessage(TIME_OUT, Constants.CAPTURE_TIMEOUT)
						.sendToTarget();
			} else if (result == FPS.FPS_ILLEGAL_LIBRARY) {
				fpshandler
						.obtainMessage(FPS_ILLEGAL_LIBRARY, Constants.ILLEGAL_LIB)
						.sendToTarget();
			} else if (result == FPS.FAILURE) {
				fpshandler.obtainMessage(FAILURE,Constants.CAPTURE_FAILED )
						.sendToTarget();
			} else if (result == FPS.PARAMETER_ERROR) {
				fpshandler.obtainMessage(PARAMETER_ERROR, Constants.PAR_ERR)
						.sendToTarget();
			} else if (result == FPS.FPS_INVALID_DEVICE_ID) {
				fpshandler.obtainMessage(FPS_INVALID_DEVICE_ID,
						Constants.dEV_NOT_LICN_AUTH)
						.sendToTarget();
			} else if (result == FPS.FPS_ILLEGAL_LIBRARY) {

				fpshandler.obtainMessage(FPS_ILLEGAL_LIBRARY,
						Constants.LIB_NOT_VALID).sendToTarget();
			}

			super.onPostExecute(result);
		}

	}
	private static String convertToHex(byte[] data) {
	    StringBuffer buf = new StringBuffer();
	    for (int i = 0; i < data.length; i++) {
	        int halfbyte = (data[i] >>> 4) & 0x0F;
	        int two_halfs = 0;
	        do {
	            if ((0 <= halfbyte) && (halfbyte <= 9))
	                buf.append((char) ('0' + halfbyte));
	            else
	                buf.append((char) ('a' + (halfbyte - 10)));
	            halfbyte = data[i] & 0x0F;
	        } while(two_halfs++ < 1);
	    }
	    return buf.toString();
	}
	public static void verifyTempleAsync(Context context) {

		fps = new FPS(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		FingerPrintAsync fp = new FingerPrintAsync();
		fp.new VerifyTempleAsync().execute(0);

	}

	public static void addFingerByteData() {
		if (imageType.equalsIgnoreCase("RHTF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhtfTmpltData(bufvalueTmplt != null ?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setRhtfScanData(bufValueImgData != null ?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("RHIF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhifTmpltData(bufvalueTmplt != null ?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setRhifScanData(bufValueImgData != null ?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("RHMF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhmfTmpltData(bufvalueTmplt != null ?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setRhmfScanData(bufValueImgData != null ?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("RHRF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhrfTmpltData(bufvalueTmplt != null ?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setRhrfScanData(bufValueImgData != null ?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("RHLF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhlfTmpltData(bufvalueTmplt != null ?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setRhlfScanData(bufValueImgData != null?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("LHTF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhtfTmpltData(bufvalueTmplt != null?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setLhtfScanData(bufValueImgData != null?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("LHIF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhifTmpltData(bufvalueTmplt != null?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setLhifScanData(bufValueImgData != null?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("LHMF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhmfTmpltData(bufvalueTmplt != null?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setLhmfScanData(bufValueImgData != null?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("LHRF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhrfTmpltData(bufvalueTmplt != null?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setLhrfScanData(bufValueImgData !=null?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		} else if (imageType.equalsIgnoreCase("LHLF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhlfTmpltData(bufvalueTmplt !=null?new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)):"0");
//			CommonContexts.SELECTED_BIOMETRIC.setLhlfScanData(bufValueImgData !=null?new String(Base64.encode(bufValueImgData,
//					Base64.DEFAULT)):"0");
		}
	}

	/* This method shows the VerifyTempleAsync AsynTask operation */
	public class VerifyTempleAsync extends AsyncTask<Integer, Integer, Integer> {

		/* displays the progress dialog until background task is completed */
		@Override
		protected void onPreExecute() {

			Toast.makeText(contexts,Constants.PLACE_FING_FPS,
					Toast.LENGTH_LONG).show();
			super.onPreExecute();
		}

		/* Task of VerifyTempleAsync performing in the background */
		@Override
		protected Integer doInBackground(Integer... params) {

			if (VerifyFinger) {
				try {
					verifyvalue = fps.iFpsVerifyTemplate(
							CommonContexts.imageData, new FpsConfig(1,
									(byte) 0x0f));
				} catch (NullPointerException e) {
					verifyvalue = DEVICE_NOTCONNECTED;
					return verifyvalue;
				}
			}else if (!VerifyFinger) {
				fpshandler.obtainMessage(FIRST_CAPTURE,
						Constants.CAPTURE_FING_VERIFY).sendToTarget();
			}  
			return verifyvalue;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// dialog.dismiss();
			System.out.println("VerifyTempleAsync....>" + verifyvalue);
			if (verifyvalue == DEVICE_NOTCONNECTED) {
				fpshandler.obtainMessage(DEVICE_NOTCONNECTED,
						Constants.DEV_NOT_CONNC).sendToTarget();
			} else if (verifyvalue == FPS.SUCCESS) {
				fpshandler.obtainMessage(SUCCESS,
						Constants.tEMP_VERF_SUCCESS)
						.sendToTarget();

			} else if (verifyvalue == FPS.FPS_INACTIVE_PERIPHERAL) {
				fpshandler.obtainMessage(FPS_INACTIVE_PERIPHERAL,
						Constants.PERIPHERAL_INACTIVE ).sendToTarget();
			} else if (verifyvalue == FPS.TIME_OUT) {
				fpshandler.obtainMessage(TIME_OUT, Constants.FINGER_TIMEOUT)
						.sendToTarget();
			} else if (verifyvalue == FPS.FPS_ILLEGAL_LIBRARY) {
				fpshandler
						.obtainMessage(FPS_ILLEGAL_LIBRARY, Constants.ILLEGAL_LIB)
						.sendToTarget();
			} else if (verifyvalue == FPS.FAILURE) {
				fpshandler
						.obtainMessage(FAILURE,
								Constants.TEMP_VERF_FAILED)
						.sendToTarget();
			} else if (verifyvalue == FPS.PARAMETER_ERROR) {
				fpshandler.obtainMessage(PARAMETER_ERROR, Constants.PAR_ERR)
						.sendToTarget();
			} else if (verifyvalue == FPS.FPS_INVALID_DEVICE_ID) {
				fpshandler.obtainMessage(FPS_INVALID_DEVICE_ID,
						Constants.LIB_DEMO).sendToTarget();
			} else if (verifyvalue == FPS.FPS_INVALID_DEVICE_ID) {
				fpshandler.obtainMessage(FPS_INVALID_DEVICE_ID,
						Constants.dEV_NOT_LICN_AUTH)
						.sendToTarget();
			} else if (verifyvalue == FPS.FPS_ILLEGAL_LIBRARY) {

				fpshandler.obtainMessage(FPS_ILLEGAL_LIBRARY,
					Constants.LIB_NOT_VALID).sendToTarget();
				fpshandler.obtainMessage(FPS_ILLEGAL_LIBRARY,
						Constants.LIB_NOT_VALID).sendToTarget();
			}
			super.onPostExecute(result);
		}
	}

	

	static Handler fpshandlerPosition = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(contexts, (String)msg.obj, Toast.LENGTH_SHORT).show(); break;
			}
		};
	};
	
	Handler fpshandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS:
				String str1 = (String) msg.obj;
				showdialog(str1);
				break;
			case FAILURE:
				String str2 = (String) msg.obj;
				showdialog(str2);
				break;
			case TIME_OUT:
				String str3 = (String) msg.obj;
				showdialog(str3);
				break;
			case PARAMETER_ERROR:
				String str4 = (String) msg.obj;
				showdialog(str4);
				break;
			case VERIFYMATCH_INVALID_RESPONSE:
				String str5 = (String) msg.obj;
				showdialog(str5);
				break;
			case FPS_ILLEGAL_LIBRARY:
				String str6 = (String) msg.obj;
				showdialog(str6);
				break;
			case FPS_DEMO_VERSION:
				String str7 = (String) msg.obj;
				showdialog(str7);
				break;
			case FPS_INACTIVE_PERIPHERAL:
				String str8 = (String) msg.obj;
				showdialog(str8);
				break;
			case FPS_INVALID_DEVICE_ID:
				String str9 = (String) msg.obj;
				showdialog(str9);
				break;
			case FIRST_CAPTURE:
				String str11 = (String) msg.obj;
				showdialog(str11);
				break;
			case DEVICE_NOTCONNECTED:
				String str10 = (String) msg.obj;
				showdialog(str10);
				break;
			case FPSMANY_FAIL:
				String str12 = (String) msg.obj;
				showdialog(str12);
				break;
			case FPSMANY_Livefingernotmatch:
				String str13 = (String) msg.obj;
				showdialog(str13);
				break;
			case FPSMANY_Livefingernotmatchanyfinger:
				String str14 = (String) msg.obj;
				showdialog(str14);
				break;
			case FPSMANY_Livefingernotmatchindex:
				String str15 = (String) msg.obj;
				showdialog(str15);

				break;
			case FPSMANY_SUCESS:
				String str16 = (String) msg.obj;
				showdialog(str16);
				break;

			}
		};
	};

	/* To show response messages */
	public void showdialog(String str) {

		Toast.makeText(contexts, str, Toast.LENGTH_SHORT).show();

	}

	@Override
	public void doPrinters(int results) {
		if (results == 0x02) {
			FingerPrintAsync.captureFingure(contexts);
		}
	}
	

}
