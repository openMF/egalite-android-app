package com.bfsi.egalite.view.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import ch.qos.logback.classic.Level;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.dao.sqlite.DbHandlerL;
import com.bfsi.egalite.encryption.PasswordHash;
import com.bfsi.egalite.entity.Agent;
import com.bfsi.egalite.evolute.BluetoothComm;
import com.bfsi.egalite.evolute.BluetoothPairConnection;
import com.bfsi.egalite.evolute.EvoluteCallBack;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.receiver.MfiReceiver;
import com.bfsi.egalite.util.AppPref;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.LogUtil;
import com.bfsi.egalite.view.HomeView;
import com.bfsi.egalite.view.LoginView;
import com.bfsi.egalite.view.MfiApplication;
import com.bfsi.egalite.view.R;
import com.impress.api.BaudChange;

public class SettingView extends BaseActivity implements OnClickListener,
		EvoluteCallBack {
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private LinearLayout mAboutUs;// mSetcashpostion,mChangpassword
	private View mView;
	private RadioGroup mRadioGroupTheme, mRadioGroupLanguage = null;
	private RadioButton mRadioButtonLight, mRadioBtn9600, mRadioBtn1152;
	private Switch mSwitchDebug, mSwitchOnlineSync;
	private Context mContext;
	private LinearLayout mCrutonLayout;
	private TextView mTxvErrorMsg;
	public final static int THEME_LIGHT = 1;
	public static final int SUCCESS = 2014;
	private MfiReceiver alarm;
	public static boolean mResetBtnEnable = false;
	static BaudChange mBdchange;
	int mIBaudRateRetValue;
	@SuppressLint("HandlerLeak")
	public final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SUCCESS) {
				mCrutonLayout.setVisibility(View.INVISIBLE);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getApplication();
		CommonContexts.onActivityCreateSetTheme(this);
		mBtnLeft.setTag(R.drawable.back);
		mBtnLeft.setImageResource(R.drawable.back);
		mTitle.setText(getString(R.string.setting_tiltle));
		alarm = new MfiReceiver();
		CommonContexts.mMfi = ((MfiApplication) getApplicationContext());
		display();
		CommonContexts.dismissProgressDialog();
	}

	@Override
	protected void onLeftAction() {

		startActivity(new Intent(SettingView.this, HomeView.class));
		finish();
	}

	private void display() {
		mView = getLayoutInflater().inflate(R.layout.setting, null);
		mCrutonLayout = (LinearLayout) mView
				.findViewById(R.id.linlay_loginlayout);
		mTxvErrorMsg = (TextView) mView
				.findViewById(R.id.txv_login_layout_error);

		mMiddleFrame.removeAllViews();
		mMiddleFrame.addView(mView);
		declaration();
		// mChangpassword.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// passwordChange();
		// }
		// });
		Button dbDump = (Button) mView.findViewById(R.id.btn_exportdbdump);
		dbDump.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DbHandlerL.getInstance().exportDBFile();

			}
		});

		mRadioBtn9600 = (RadioButton) findViewById(R.id.first_radio);
		mRadioBtn1152 = (RadioButton) findViewById(R.id.second_radio);
		mRadioBtn9600.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * ResetBtnEnable will disable the reset button in Exit dialog
				 * box as the connection is not made in 115200bps
				 */
				if (CommonContexts.mBT.isEnabled()) {
					if (CommonContexts.CHECK == 0x02) {
						mResetBtnEnable = false;
						mRadioBtn9600.setChecked(true);
						mRadioBtn1152.setChecked(false);
						CommonContexts.BAUDRATE = "LOW";
					} else {
						BluetoothPairConnection.pairToDevice(mContext,
								SettingView.this);
					}

				} else {
					BluetoothPairConnection.blueToothOnOff(mContext,
							SettingView.this);
				}
			}
		});

		mRadioBtn1152.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * ResetBtnEnable will enable the reset button in Exit dialog
				 * box as the connection will be made in 115200bps
				 */
				if (CommonContexts.mBT.isEnabled()) {
					if (CommonContexts.CHECK == 0x02) {
						mResetBtnEnable = true;
						try {
							mBdchange = new BaudChange(LoginView.bfsiSetUp,
									BluetoothComm.mosOut, BluetoothComm.misIn);
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}

						IncreaseBaudRateAsyc increaseBaudRate = new IncreaseBaudRateAsyc();
						increaseBaudRate.execute(0);

					} else {
						BluetoothPairConnection.pairToDevice(mContext,
								SettingView.this);
					}

				} else {
					BluetoothPairConnection.blueToothOnOff(mContext,
							SettingView.this);
				}
			}
		});

		if (CommonContexts.BAUDRATE.equals("LOW")) {
			mRadioBtn9600.setChecked(true);
		}

		if (CommonContexts.BAUDRATE.equals("HIGH")) {
			mRadioBtn1152.setChecked(true);
		}

		mAboutUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(SettingView.this, AboutView.class);
				startActivity(intent);
				finish();
			}
		});
		radiobuttonevent();
		mSwitchDebug.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					LogUtil.configureLogger(Level.DEBUG);
					LOG.debug("Log level switched to debug mode");
					AppPref.updatePref(SettingView.this, AppPref.ISDEBUGON,
							true);
				} else {
					LogUtil.configureLogger(Level.ERROR);
					AppPref.updatePref(SettingView.this, AppPref.ISDEBUGON,
							false);
					LOG.debug("This log should not come");
					LOG.info("Log level switched to info mode");
				}
			}
		});
		mSwitchDebug.setChecked(AppPref.getPrefBoolean(SettingView.this,
				AppPref.ISDEBUGON));

		mSwitchOnlineSync
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// Toast.makeText(mContext,
							// "Under Process",Toast.LENGTH_SHORT).show();
							AppPref.updatePref(SettingView.this,
									AppPref.ISONLINESYNC, true);
							startRepeatingTimer();
						} else {
							// Toast.makeText(mContext,
							// "Under Process",Toast.LENGTH_SHORT).show();
							AppPref.updatePref(SettingView.this,
									AppPref.ISONLINESYNC, false);
							cancelRepeatingTimer();
						}
					}
				});
		mSwitchOnlineSync.setChecked(AppPref.getPrefBoolean(SettingView.this,
				AppPref.ISONLINESYNC));

	}

	// increases the device baud rate from 9600bps to 115200bps
	public class IncreaseBaudRateAsyc extends
			AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			// shows a progress dialog until the baud rate process is complete
			CommonContexts.showProgress(SettingView.this,
					getString(R.string.plzwait));
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				// Log.d(TAG, "Change the peripheral Speed");
				mIBaudRateRetValue = mBdchange.iSwitchPeripheral1152();

				Thread.sleep(1000);
				BluetoothComm.mosOut = null;
				BluetoothComm.misIn = null;

				CommonContexts.mMfi.closeConn();

				Thread.sleep(1000);
				if (CommonContexts.mBT != null) {
					CommonContexts.mBT.cancelDiscovery();
				}
				Thread.sleep(1000);
				// if (D) Log.d(TAG, "Reconnect to same device");
				boolean b = CommonContexts.mMfi
						.createConn(CommonContexts.mBDevice.getAddress());
				if (b )
					// new connSocketTask().execute(mBDevice.getAddress());
					CommonContexts.mMfi.mBTcomm.isConnect();

				mBdchange.iSwitchBT1152(BluetoothComm.mosOut,
						BluetoothComm.misIn);
				// if (D) Log.d(TAG, "DONE the BT Speed");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mIBaudRateRetValue;
		}

		/* goes to next activity after setting the new baud rate */
		@Override
		protected void onPostExecute(Integer result) {
			Log.e("115200", "..............>" + mIBaudRateRetValue);
			CommonContexts.dismissProgressDialog();
			mRadioBtn1152.setChecked(true);
			mRadioBtn9600.setChecked(false);
			CommonContexts.BAUDRATE = "HIGH";
		}
	}

	public void startRepeatingTimer() {
		// Context context = this.getApplicationContext();
		if (alarm != null) {
			alarm.SetAlarm(mContext);
		} else {
			Toast.makeText(mContext, "Alarm is null", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void cancelRepeatingTimer() {
		Context context = this.getApplicationContext();
		if (alarm != null) {
			alarm.CancelAlarm(context);
		} else {
			Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Implimentation of change password screen
	 */
	private void passwordChange() {
		final Dialog dialog = new Dialog(SettingView.this);
		dialog.setContentView(R.layout.setting_chngpass_layout);
		dialog.setTitle(R.string.title_changepass);

		final EditText oldpass = (EditText) dialog
				.findViewById(R.id.edt_setting_changpass_lay_oldpass);
		final EditText newpass = (EditText) dialog
				.findViewById(R.id.edt_setting_changpass_lay_newpass);
		final EditText cnfmpass = (EditText) dialog
				.findViewById(R.id.edt_setting_changpass_lay_cnfmpass);
		Button submit = (Button) dialog
				.findViewById(R.id.btn_setting_chngpass_lay_submit);
		Typeface tFont = CommonContexts.getTypeface(mContext);
		oldpass.setTypeface(tFont);
		newpass.setTypeface(tFont);
		cnfmpass.setTypeface(tFont);

		submit.setTypeface(tFont);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// should read password minimum length from system parameters

				// validate with new password/confirm password
				// int minimumLength = 0;
				// PreDataDao lgnDao = DaoFactory.getPreDataDao();
				// Parameter parameter = lgnDao
				// .readParameterValues(AppParams.MINIMUM_PASSWORD_LENGTH);
				// if (parameter != null)
				// minimumLength = Integer.parseInt(parameter
				// .getParamVal());
				//
				// if (oldpass.getText().toString().isEmpty()) {
				// showCrutonError(mContext, R.string.oldpassword, handler);
				// } else if (!(passwordValidation(oldpass.getText()
				// .toString()))) {
				// showCrutonError(mContext, R.string.oldpasswordcurr,
				// handler);
				// } else if (newpass.getText().toString().isEmpty()) {
				// showCrutonError(mContext, R.string.newpassword, handler);
				//
				// } else if (cnfmpass.getText().toString().isEmpty()) {
				// showCrutonError(mContext, R.string.cnfermpassword,
				// handler);
				// } else if (((oldpass.getText().toString())
				// .equalsIgnoreCase(newpass.getText().toString()))) {
				// showCrutonError(mContext, R.string.passwordchek,
				// handler);
				// }else if (!((cnfmpass.getText().toString())
				// .equalsIgnoreCase(newpass.getText().toString()))) {
				// showCrutonError(mContext, R.string.passnotmatch,
				// handler);
				// } else if (newpass.getText().toString().length() != cnfmpass
				// .getText().toString().length()) {
				// showCrutonError(mContext,
				// R.string.messg_set_chekpassword, handler);
				// } else if (newpass.getText().toString().length() <
				// minimumLength) {
				// showCrutonError(mContext,
				// R.string.messg_set_paswordlength, handler);
				// } else if (cnfmpass.getText().toString().length() <
				// minimumLength) {
				// showCrutonError(mContext,
				// R.string.messg_set_paswordlength, handler);
				// }
				// else {
				// // server connection here
				// try {
				// // read agent password, datakey
				// PreDataDao pdao = DaoFactory.getPreDataDao();
				// Agent agent = pdao.readAgentValues();
				// String datakey = agent.getDataKey();
				// String oldPassword = agent.getPassword();
				// String changeDate = CommonContexts.dateFormat
				// .format(CommonContexts.NETWORKTIMESTAMP);
				//
				// // decrypt the datakey
				// AESDecrypt aesDecrypt = new AESDecrypt(oldPassword,
				// datakey);
				// String plainDataKey = aesDecrypt.decrypt();
				//
				// // generate salt and hash the password
				// byte[] salt = GenerateSalt.getSalt();
				// String password = PasswordHash.createHash(newpass
				// .getText().toString(), salt);
				//
				// // encrypt datakey with new hash password
				// AESEncrypt aesEncrypt = new AESEncrypt(password);
				// String encryptDataKey = aesEncrypt
				// .encrypt(plainDataKey);
				//
				// // update the agent table with new values
				// pdao.updateAgentsPassword(salt, password,
				// encryptDataKey, changeDate);
				// Toast.makeText(SettingView.this,
				// "Password changed successfully", Toast.LENGTH_SHORT).show();
				// dialog.cancel();
				// } catch (Exception e) {
				// LOG.debug(
				// "Exception while encryption: Setting"
				// + e.getMessage(), e);
				// throw new DataAccessException(
				// "Error while encryption "
				// + e.getMessage(), e);
				// }
				// }

			}
		});

		Button cancel = (Button) dialog
				.findViewById(R.id.btn_setting_chngpass_lay_cancel);
		cancel.setTypeface(tFont);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
		dialog.setCancelable(false);
	}

	private void radiobuttonevent() {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View view) {

				boolean checked = ((RadioButton) view).isChecked();
				switch (view.getId()) {
				case R.id.radbtn_setting_light:
					if (checked)
						CommonContexts.changeToTheme(SettingView.this,
								CommonContexts.THEME_LIGHT);
					mRadioButtonLight.setChecked(true);
					CommonContexts.THEME = "LIGHT";
					break;
				case R.id.radbtn_setting_english:
					if (checked)
						changeEnglish();
					CommonContexts.LANG = "EN";
					break;

				// case R.id.radbtn_setting_khmer:
				// if (checked)
				// changeKhmer();
				// CommonContexts.LANG = "KHM";
				// break;
				// case R.id.radbtn_setting_tangalog:
				// if (checked)
				// changeTagalog();
				// CommonContexts.LANG = "TAGLAG";
				// break;
				//
				// case R.id.radbtn_setting_swahili:
				// if (checked)
				// changeSwahili();
				// CommonContexts.LANG = "SHL";
				// break;
				}
			}
		};

		Typeface tFace = CommonContexts.getTypeface(SettingView.this);
		mRadioButtonLight = (RadioButton) findViewById(R.id.radbtn_setting_light);
		mRadioButtonLight.setOnClickListener(listener);
		mRadioButtonLight.setTypeface(tFace);

		if (CommonContexts.THEME.equals("LIGHT")) {
			mRadioButtonLight.setChecked(true);
		}
		// mRadioButtonDark = (RadioButton)
		// findViewById(R.id.radbtn_setting_dark);
		// mRadioButtonDark.setOnClickListener(listener);
		// mRadioButtonDark.setTypeface(tFace);
		// if (CommonContexts.THEME.equals("DARK")) {
		// mRadioButtonDark.setChecked(true);
		// }
		RadioButton rb_eng = (RadioButton) findViewById(R.id.radbtn_setting_english);
		rb_eng.setOnClickListener(listener);
		rb_eng.setTypeface(tFace);
		if (CommonContexts.LANG.equals("EN")) {
			rb_eng.setChecked(true);
		}

		// RadioButton rb_khmer = (RadioButton)
		// findViewById(R.id.radbtn_setting_khmer);
		// rb_khmer.setOnClickListener(listener);
		// rb_khmer.setTypeface(tFace);
		// if (CommonContexts.LANG.equals("KHM")) {
		// rb_khmer.setChecked(true);
		// }
		//
		// RadioButton rb_tagalog = (RadioButton)
		// findViewById(R.id.radbtn_setting_tangalog);
		// rb_tagalog.setOnClickListener(listener);
		// rb_tagalog.setTypeface(tFace);
		// if (CommonContexts.LANG.equals("TAGLAG")) {
		// rb_tagalog.setChecked(true);
		// }
		//
		// RadioButton rb_swahili = (RadioButton)
		// findViewById(R.id.radbtn_setting_swahili);
		// rb_swahili.setOnClickListener(listener);
		// rb_swahili.setTypeface(tFace);
		// if (CommonContexts.LANG.equals("SHL")) {
		// rb_swahili.setChecked(true);
		// }

	}

	@SuppressWarnings("unused")
	private boolean passwordValidation(String password) {
		try {
			PreDataDao preDao = DaoFactory.getPreDataDao();
			Agent agent = preDao.readAgentValues();
			try {
				LOG.error(mContext.getResources().getString(R.string.MFB00118));
				boolean validate = PasswordHash.validatePassword(password,
						agent.getPassword());
				return validate;
			} catch (NoSuchAlgorithmException e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00118)
						+ e.getMessage(), e);
				Toast.makeText(SettingView.this,
						this.getResources().getString(R.string.MFB00118),
						Toast.LENGTH_SHORT).show();
			} catch (InvalidKeySpecException e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00118)
						+ e.getMessage(), e);
				Toast.makeText(SettingView.this,
						this.getResources().getString(R.string.MFB00118),
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				LOG.error(mContext.getResources().getString(R.string.MFB00118)
						+ e.getMessage(), e);
				Toast.makeText(SettingView.this,
						this.getResources().getString(R.string.MFB00118),
						Toast.LENGTH_SHORT).show();
			}

		} catch (DataAccessException e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00118)
							+ e.getMessage(), e);
			Toast.makeText(SettingView.this,
					this.getResources().getString(R.string.MFB00118),
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			LOG.error(
					mContext.getResources().getString(R.string.MFB00118)
							+ e.getMessage(), e);
			Toast.makeText(SettingView.this,
					this.getResources().getString(R.string.MFB00118),
					Toast.LENGTH_SHORT).show();
		}

		return false;
	}

	private void declaration() {

		// mChangpassword = (LinearLayout) mView
		// .findViewById(R.id.linlay_setting_changepass);
		// mSetcashpostion = (LinearLayout) mView
		// .findViewById(R.id.linlay_setting_setcashposition);
		mAboutUs = (LinearLayout) mView
				.findViewById(R.id.linlay_setting_aboutus);

		mRadioGroupTheme = (RadioGroup) findViewById(R.id.rad_grp_theme);
		mRadioGroupTheme.clearCheck();
		mRadioGroupLanguage = (RadioGroup) findViewById(R.id.rad_grp_lang);
		mRadioGroupLanguage.clearCheck();
		mSwitchDebug = (Switch) findViewById(R.id.switch_setting_debug);
		mSwitchOnlineSync = (Switch) findViewById(R.id.switch_setting_onlinesync);

	}

	/**
	 * Implimentation of crutonerror
	 */
	public void showCrutonError(final Context context, int messg,
			final Handler handler) {
		Animation animFadein;
		int TIMER_DELAY = 300;

		animFadein = AnimationUtils
				.loadAnimation(context, R.anim.bottom_to_top);
		mCrutonLayout.setVisibility(View.VISIBLE);
		mCrutonLayout.setBackgroundResource(R.color.red);
		mTxvErrorMsg.setText(messg);
		mCrutonLayout.setAnimation(animFadein);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(SUCCESS);
			}
		};
		Timer timer = new Timer();
		timer.schedule(task, TIMER_DELAY);
	}

	public void changeEnglish() {
		Configuration c = new Configuration(getResources().getConfiguration());
		c.locale = Locale.ENGLISH;
		getResources().updateConfiguration(c,
				getResources().getDisplayMetrics());
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);

	}

	public void changeTagalog() {

		Configuration c = new Configuration(getResources().getConfiguration());
		Locale locale = new Locale("tl_PH");
		c.locale = locale;
		getResources().updateConfiguration(c,
				getResources().getDisplayMetrics());
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public void changeKhmer() {

		Configuration c = new Configuration(getResources().getConfiguration());
		Locale locale = new Locale("km_KH");
		c.locale = locale;
		getResources().updateConfiguration(c,
				getResources().getDisplayMetrics());
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	public void changeSwahili() {

		Configuration c = new Configuration(getResources().getConfiguration());
		Locale locale = new Locale("sw_KE");
		c.locale = locale;
		getResources().updateConfiguration(c,
				getResources().getDisplayMetrics());
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(SettingView.this, HomeView.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);

	}

	@SuppressWarnings("unused")
	private void exportDBFile() {
		java.io.File sourceFile = android.os.Environment.getDataDirectory();
		java.io.File exportFile = android.os.Environment
				.getExternalStorageDirectory();

		java.nio.channels.FileChannel sourceChannel = null;
		java.nio.channels.FileChannel exportChannel = null;

		String BFSI_DB_Path = "/data/" + "com.bfsi.egalite.view"
				+ "/databases/" + "MFI_DB";
		String Export_DB_Path = "MFI_DB_" + DateUtil.getCurrentDataTime();

		File sourceDBFile = new File(sourceFile, BFSI_DB_Path);
		File exportDBFile = new File(exportFile, Export_DB_Path);
		try {
			sourceChannel = new FileInputStream(sourceDBFile).getChannel();
			exportChannel = new FileOutputStream(exportDBFile).getChannel();

			exportChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

			sourceChannel.close();
			exportChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doPrinters(int results) {
		if (results == 0x02) {
			mResetBtnEnable = true;
			try {
				mBdchange = new BaudChange(LoginView.bfsiSetUp,
						BluetoothComm.mosOut, BluetoothComm.misIn);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			IncreaseBaudRateAsyc increaseBaudRate = new IncreaseBaudRateAsyc();
			increaseBaudRate.execute(0);
		}
	}
}
