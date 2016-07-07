package com.bfsi.egalite.evolute;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
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

@SuppressLint("HandlerLeak")
public class FingerPrint implements EvoluteCallBack {
	static byte[] bufvalueTmplt = {};
	static byte[] bufValueImgData = {};
	byte[] image = {};
	byte[] image1 = {};
	byte[] asd = {};
	byte[] rgb = {};
	private Bitmap bmp = null;
	private int returnvalue, verifyvalue, imagecompressed,
			iverifyfingerimagecompressed, ifingerimageuncompressed,
			verifyuncompresd, iRetVal;
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

	public void fetchBiometric(Context context, ImageView imageView,
			String imageType) {
		this.imageType = imageType;
		this.contexts = context;
		this.mImageView = imageView;
		if (CommonContexts.mBT.isEnabled()) {

			if (CommonContexts.CHECK == 0x02) {
				FingerPrint.captureFingure(contexts);

			} else {
				BluetoothPairConnection
						.pairToDevice(contexts, FingerPrint.this);
			}

		} else {

			BluetoothPairConnection.blueToothOnOff(contexts, FingerPrint.this);
		}
	}

	// Context context = this;
	public static void captureFingure(Context context) {

		fps = new FPS(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		FingerPrint fp = new FingerPrint();
		fp.new CaptureFinger().execute(0);
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
				bufValueImgData = new byte[150000];
				
				fpsconfig = new FpsConfig(0, (byte) 0x0F);
				
				bufvalueTmplt = fps.bFpsCaptureTemplate(fpsconfig);
				
				fps.iGetFingerImageUnCompressed(bufvalueTmplt, fpsconfig);
				bufValueImgData = fps.bGetImageData();
				
				
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
				mImageView.setTag(R.drawable.imgfinger);
				mImageView.setImageResource(R.drawable.imgfinger);
				
				addFingerByteData();
				
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

	public static void verifyTempleAsync(Context context) {

		fps = new FPS(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		FingerPrint fp = new FingerPrint();
		fp.new VerifyTempleAsync().execute(0);

	}

	public static void addFingerByteData() {
		if (imageType.equalsIgnoreCase("RHTF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhtfTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setRhtfScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("RHIF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhifTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setRhifScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("RHMF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhmfTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setRhmfScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("RHRF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhrfTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setRhrfTmpltData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("RHLF")) {
			CommonContexts.SELECTED_BIOMETRIC.setRhlfTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setRhlfScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("LHTF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhtfTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setLhtfScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("LHIF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhifTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setLhifScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("LHMF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhmfTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setLhmfScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("LHRF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhrfTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setLhrfScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
		} else if (imageType.equalsIgnoreCase("LHLF")) {
			CommonContexts.SELECTED_BIOMETRIC.setLhlfTmpltData(new String(Base64.encode(bufvalueTmplt,
					Base64.DEFAULT)));
			CommonContexts.SELECTED_BIOMETRIC.setLhlfScanData(new String(Base64.encode(bufValueImgData,
					Base64.DEFAULT)));
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
			}else  if (!VerifyFinger){
				// dialog.dismiss();
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
			}
			super.onPostExecute(result);
		}
	}

	

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
			FingerPrint.captureFingure(contexts);
		}
	}

}