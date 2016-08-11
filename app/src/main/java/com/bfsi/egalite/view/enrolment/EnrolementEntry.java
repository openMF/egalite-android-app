package com.bfsi.egalite.view.enrolment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bfsi.egalite.dao.sqlite.EnrolmentDataAccess;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.CustTabPageIndicator;
import com.bfsi.egalite.service.impl.BaseTransactionService;
import com.bfsi.egalite.util.AppPref;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.ViewUtil;
import com.bfsi.egalite.view.R;

/**
 * @author Vijay
 * EGA-MN15-000012
 * Captures the personal information of new customer
 *
 */
public class EnrolementEntry extends Fragment implements CommandListener {

	private static final String ENROL_VIEW_VERIFY = "enrolviewverify";
	private static final String ENROL_DETAILS = "enroldetails";
	private Logger LOG = LoggerFactory.getLogger(getClass());
	private Context mContext;
	private BfsiViewPager mPager;
	private static EditText firstname, lastname, middlename, dob, address1,
			address3, address4, contactnumber, address2, state, zip, email,
			remarks;
	private RadioGroup gender, resident;
	private CheckBox mCheckNationid, mCheckGovtmentissued, mCheckDrivinglicens,
			mCheckPassport, mCheckBirthCert, mCheckAadhar, mCheckResident,
			mCheckFamilybook;
	private Button btnNext, btnGroup;
	private static String dobDate;
	private ImageView calender;
	private static Spinner nationality, country, maritalstatus, profession,
			mGroupSpinner;
	private Enrolement enrolement;
	private OnClickListener listener;
	private static ViewGroup mViewGroup;
	private LayoutInflater mLayoutInflater = null;
	private View mView;
	private int nationalityPos, countryPos, maritalstatusPos, professionPos;
	private LinearLayout mLinlaySpinner, mLinlayPoc;
	private static String ENROLL = "RESI";

	private static String POC = "N";
	private CustTabPageIndicator mPageIndicator;
	private RadioButton mRadioButtonresident, mRadioButtonNonResident,
			mRadioButtonIndividual, mRadioButtonGroup, mPocNo, mPocYes;

	public static EnrolementEntry newInstance(Context context,
			BfsiViewPager pager, CustTabPageIndicator mPageIndicat) {
		EnrolementEntry fragment = new EnrolementEntry();
		fragment.mPager = pager;
		fragment.mContext = context;
		fragment.mPageIndicator = mPageIndicat;
		return fragment;
	}

	private void enablePagerAndIndicator(boolean status) {
		mPager.setEnabled(status);
		mPageIndicator.setEnabled(status);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mContext = getActivity();
		mLayoutInflater = inflater;
		((BaseActivity) this.getActivity()).unregisterCommandListener(this);
		((BaseActivity) this.getActivity()).registerCommandListener(this);

		doLoadInit();
		enrolement = new Enrolement();
		mViewGroup = new LinearLayout(getActivity());
		mViewGroup.addView(mView);

		return mViewGroup;
	}

	@SuppressLint("HandlerLeak")
	public final Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};

	private void spinnerVal() {
		List<String> list;
		if (AppPref.getPref(mContext, AppPref.GROUPID) != null) {
			list = Arrays.asList(TextUtils.split(
					AppPref.getPref(mContext, AppPref.GROUPID), ","));
		} else {
			list = new ArrayList<String>();
		}
		Collections.reverse(list);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_text, list);
		dataAdapter.setDropDownViewResource(R.layout.spinner_text_black);
		mGroupSpinner.setAdapter(dataAdapter);
		dataAdapter.notifyDataSetChanged();
	}

	private void doLoadInit() {
		if (mPager.getCurrentItem() == 1)
			BaseActivity.mBtnRight.setVisibility(View.INVISIBLE);
		CommonContexts.CURRENT_VIEW = ENROL_DETAILS;
		BaseActivity.mTitle.setText(R.string.personal_info);

		mView = mLayoutInflater.inflate(R.layout.enrolment_newform, null);
		firstname = (EditText) mView.findViewById(R.id.edt_enrol_firstname);
		middlename = (EditText) mView.findViewById(R.id.edt_enrol_middlename);
		lastname = (EditText) mView.findViewById(R.id.edt_enrol_lastname);
		state = (EditText) mView.findViewById(R.id.edt_enrol_state);
		zip = (EditText) mView.findViewById(R.id.edt_enrol_zip);
		email = (EditText) mView.findViewById(R.id.edt_enrol_email);
		remarks = (EditText) mView.findViewById(R.id.edt_enrol_remarks);
		dob = (EditText) mView.findViewById(R.id.edt_enrol_dob);
		address1 = (EditText) mView.findViewById(R.id.edt_enrol_address1);
		address2 = (EditText) mView.findViewById(R.id.edt_enrol_address2);
		address3 = (EditText) mView.findViewById(R.id.edt_enrol_address3);
		address4 = (EditText) mView.findViewById(R.id.edt_enrol_address4);
		contactnumber = (EditText) mView.findViewById(R.id.edt_enrol_contactno);
		gender = (RadioGroup) mView.findViewById(R.id.enrol_radioSex);
		calender = (ImageView) mView.findViewById(R.id.img_enrol_calender);
		btnNext = (Button) mView.findViewById(R.id.id_enrol_nextbutton);
		btnGroup = (Button) mView.findViewById(R.id.btn_enroll_groupID);
		btnGroup.setOnClickListener(addListener);
		Typeface tFace = CommonContexts.getTypeface(mContext);
		firstname.setTypeface(tFace);
		lastname.setTypeface(tFace);
		middlename.setTypeface(tFace);
		dob.setTypeface(tFace);
		address1.setTypeface(tFace);
		contactnumber.setTypeface(tFace);
		address2.setTypeface(tFace);
		address3.setTypeface(tFace);
		address4.setTypeface(tFace);
		state.setTypeface(tFace);
		zip.setTypeface(tFace);
		email.setTypeface(tFace);
		remarks.setTypeface(tFace);
		nationality = (Spinner) mView.findViewById(R.id.spinner_nationalty);
		country = (Spinner) mView.findViewById(R.id.spinner_country);
		maritalstatus = (Spinner) mView.findViewById(R.id.spinner_marital);
		profession = (Spinner) mView.findViewById(R.id.spinner_profession);

		resident = (RadioGroup) mView.findViewById(R.id.enrol_resident);
		mLinlaySpinner = (LinearLayout) mView
				.findViewById(R.id.linlay_groupspinner);
		mGroupSpinner = (Spinner) mView.findViewById(R.id.spin_enroll_groupID);
		spinnerVal();
		mLinlayPoc = (LinearLayout) mView.findViewById(R.id.linlay_poc);
		mLinlaySpinner.setVisibility(View.GONE);
		mLinlayPoc.setVisibility(View.GONE);

		RadioButton mRadioButtonmale = (RadioButton) mView
				.findViewById(R.id.radioMale);
		RadioButton mRadioButtonfemale = (RadioButton) mView
				.findViewById(R.id.radioFemale);
		mRadioButtonmale.setTypeface(tFace);
		mRadioButtonfemale.setTypeface(tFace);

		radiobuttonevent();
		mRadioButtonresident = (RadioButton) mView
				.findViewById(R.id.radioresidance);

		mRadioButtonresident.setChecked(true);
		nationality.setVisibility(View.INVISIBLE);
		mRadioButtonresident.setTypeface(tFace);
		mRadioButtonNonResident = (RadioButton) mView
				.findViewById(R.id.radiononresidance);
		mRadioButtonIndividual = (RadioButton) mView
				.findViewById(R.id.radioindividual);
		mRadioButtonGroup = (RadioButton) mView.findViewById(R.id.radiongroup);
		mPocYes = (RadioButton) mView.findViewById(R.id.radiopocyes);
		mPocNo = (RadioButton) mView.findViewById(R.id.radiopocno);
		mPocNo.setChecked(true);
		mRadioButtonIndividual.setChecked(true);
		mRadioButtonIndividual.setOnClickListener(listener);
		mRadioButtonGroup.setOnClickListener(listener);
		mRadioButtonresident.setOnClickListener(listener);
		mRadioButtonNonResident.setOnClickListener(listener);
		mRadioButtonIndividual.setOnClickListener(listener);
		mPocYes.setOnClickListener(listener);
		mPocNo.setOnClickListener(listener);

		mRadioButtonNonResident.setTypeface(CommonContexts
				.getTypeface(mContext));

		List<String> nationalitylist = EnrolmentDataAccess
				.readlov(EnrolmentDataAccess.NAT01);
		nationalitylist.add("Nationality *");

		Collections.reverse(nationalitylist);
		List<String> martialStatuslist = EnrolmentDataAccess
				.readlov(EnrolmentDataAccess.MTS01);
		martialStatuslist.add("Martial Status *");
		Collections.reverse(martialStatuslist);

		List<String> countrylist = EnrolmentDataAccess
				.readlov(EnrolmentDataAccess.CN001);
		countrylist.add("Country *");
		Collections.reverse(countrylist);

		List<String> professionlist = EnrolmentDataAccess
				.readlov(EnrolmentDataAccess.PRO01);
		professionlist.add("Profession *");
		Collections.reverse(professionlist);

		List<String> currencyList = EnrolmentDataAccess
				.readlov(EnrolmentDataAccess.CCY01);
		currencyList.add("Currency *");
		Collections.reverse(currencyList);

		ArrayAdapter<String> ccyAdapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_text, currencyList);
		ccyAdapter.setDropDownViewResource(R.layout.spinner_text_black);

		ArrayAdapter<String> nationalityAdapter = new ArrayAdapter<String>(
				mContext, R.layout.spinner_text, nationalitylist);
		nationalityAdapter.setDropDownViewResource(R.layout.spinner_text_black);
		nationality.setAdapter(nationalityAdapter);

		nationality
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						String value = nationality.getSelectedItem().toString();
						if (arg2 == 0)
							enrolement.setNationality("");
						else
							enrolement.setNationality(value);
						nationalityPos = arg2;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		ArrayAdapter<String> maritalstatusAdapter = new ArrayAdapter<String>(
				mContext, R.layout.spinner_text, martialStatuslist);
		maritalstatusAdapter
				.setDropDownViewResource(R.layout.spinner_text_black);
		maritalstatus.setAdapter(maritalstatusAdapter);

		maritalstatus
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						String value = maritalstatus.getSelectedItem()
								.toString();
						enrolement.setMartialStatus(value);
						maritalstatusPos = arg2;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(
				mContext, R.layout.spinner_text, countrylist);
		countryAdapter.setDropDownViewResource(R.layout.spinner_text_black);
		country.setAdapter(countryAdapter);

		country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String value = country.getSelectedItem().toString();
				enrolement.setCountry(value);
				countryPos = arg2;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		ArrayAdapter<String> professionalAdapter = new ArrayAdapter<String>(
				mContext, R.layout.spinner_text, professionlist);
		professionalAdapter
				.setDropDownViewResource(R.layout.spinner_text_black);
		profession.setAdapter(professionalAdapter);
		profession
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						String value = profession.getSelectedItem().toString();
						enrolement.setProfession(value);
						professionPos = arg2;

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		calender.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment picker = new DatePickerFragment();
				picker.show(getFragmentManager(), Constants.DATEPICKER);

			}
		});

		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleSave();

			}
		});
	}

	private void radiobuttonevent() {
		listener = new OnClickListener() {

			@Override
			public void onClick(View view) {

				boolean checked = ((RadioButton) view).isChecked();
				switch (view.getId()) {
				case R.id.radioresidance:
					if (checked)
						nationality.setSelection(0);
					ENROLL = "RESI";
					enrolement.setNationality("");
					nationalityPos = 0;
					nationality.setVisibility(View.INVISIBLE);
					break;
				case R.id.radiononresidance:
					nationality.setVisibility(View.VISIBLE);
					ENROLL = "NONRESI";
					break;

				case R.id.radioindividual:

					if (CommonContexts.SELECTED_ENROLEMENT != null)
						CommonContexts.SELECTED_ENROLEMENT
								.setGroupIndiviType("I");
					mLinlaySpinner.setVisibility(View.GONE);
					mLinlayPoc.setVisibility(View.GONE);
					CommonContexts.GROUP = "INDVI";

					break;
				case R.id.radiongroup:
					if (CommonContexts.SELECTED_ENROLEMENT != null)
						CommonContexts.SELECTED_ENROLEMENT
								.setGroupIndiviType("G");
					mLinlaySpinner.setVisibility(View.VISIBLE);
					mLinlayPoc.setVisibility(View.VISIBLE);
					CommonContexts.GROUP = "GRP";

					break;
				case R.id.radiopocyes:
					CommonContexts.poc = "Y";
					POC = "Y";
					break;
				case R.id.radiopocno:
					CommonContexts.poc = "N";
					POC = "N";
					break;

				}
			}
		};
	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -18);
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), this,
					year, month, day);
			dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
			return dialog;

		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Calendar c = Calendar.getInstance();
			c.set(year, month, day);
			dob.setText(year + "-"
					+ ((month + 1) < 10 ? ("0" + (month + 1)) : (month + 1))

					+ "-" + (day < 10 ? ("0" + day) : day));
			dobDate = dob.getText().toString();
		}

	}

	private boolean validation() {

		String proffession = profession.getSelectedItem().toString();
		String selcountry = country.getSelectedItem().toString();
		String maritials = maritalstatus.getSelectedItem().toString();
		String national = nationality.getSelectedItem().toString();
		boolean residenttype = residentValidation();
		if (firstname.getText().toString().isEmpty()) {
			ViewUtil.showCrutonError(mContext, R.string.messg_firstname,
					handler);
			return false;
		} else if (lastname.getText().toString().isEmpty()) {
			ViewUtil.showCrutonError(mContext, R.string.messg_lastname, handler);
			return false;
		} else if (dob.getText().toString().isEmpty()) {
			ViewUtil.showCrutonError(mContext, R.string.messg_dateofbirth,
					handler);
			return false;
		} else if (address1.getText().toString().isEmpty()) {
			ViewUtil.showCrutonError(mContext, R.string.messg_address, handler);
			return false;
		} else if (zip.getText().toString().isEmpty()) {
			ViewUtil.showCrutonError(mContext, R.string.messg_zip, handler);
			return false;
		} else if (contactnumber.getText().toString().isEmpty()) {
			ViewUtil.showCrutonError(mContext, R.string.messg_contact, handler);
			return false;
		} else if (residenttype
				&& national.equalsIgnoreCase(Constants.NATIONALITY)) {
			ViewUtil.showCrutonError(mContext, R.string.messg_nationality,
					handler);
			return false;
		} else if (proffession.equalsIgnoreCase(Constants.PROFESSION)) {
			ViewUtil.showCrutonError(mContext, R.string.messg_profession,
					handler);
			return false;
		} else if (selcountry.equalsIgnoreCase(Constants.COUNTRY)) {
			ViewUtil.showCrutonError(mContext, R.string.messg_country, handler);
			return false;
		}

		else if (maritials.equalsIgnoreCase(Constants.MARITAL_STAT)) {
			ViewUtil.showCrutonError(mContext, R.string.messg_maritialstatus,
					handler);
			return false;
		} else if (!(email.getText().toString().isEmpty())) {
			if (!(emailValidation(email.getText().toString()))) {
				ViewUtil.showCrutonError(mContext, R.string.messg_email,
						handler);
				return false;
			} else {
				return true;
			}

		} else {
			return true;
		}

	}

	private boolean residentValidation() {
		int radioButtonResidance = resident.getCheckedRadioButtonId();
		View radioButtonResidant = resident.findViewById(radioButtonResidance);
		int idResidant = resident.indexOfChild(radioButtonResidant);

		enrolement.setResidenceType(idResidant == 0 ? "Resident"
				: "Non Resident");
		if (enrolement.getResidenceType().equals("Non Resident"))
			return true;
		else
			return false;
	}

	private void handleSave() {
		if (validation()) {

			try {
				Enrolement enroll = CommonContexts.SELECTED_ENROLEMENT;
				if (enroll != null) {
					int radioButtonID = gender.getCheckedRadioButtonId();
					View radioButton = gender.findViewById(radioButtonID);
					int genderId = gender.indexOfChild(radioButton);

					int radioButtonResidance = resident
							.getCheckedRadioButtonId();
					View radioButtonResidant = resident
							.findViewById(radioButtonResidance);
					int idResidant = resident.indexOfChild(radioButtonResidant);

					enroll.setEnrollmentId(BaseTransactionService
							.generateEnrolSequence("EN"));
					if (enroll.getEnrollmentId() != null)
						CommonContexts.SELECTED_BIOMETRIC.setEnrolmentId(enroll
								.getEnrollmentId());

					enroll.setResidenceType(idResidant == 0 ? "R" : "N");
					enroll.setFirstName(firstname.getText().toString());
					enroll.setLastName(lastname.getText().toString());
					enroll.setMiddleName(middlename.getText() != null ? middlename
							.getText().toString() : "");
					enroll.setGender(genderId == 0 ? "Male" : "Female");
					enroll.setDob(DateUtil.stringToMillisecondss(dob.getText()
							.toString()));
					enroll.setContactNo(contactnumber.getText().toString());
					enroll.setAddress1(address1.getText().toString());
					enroll.setAddress2(address2.getText() != null ? address2
							.getText().toString() : "");
					enroll.setAddress3(address3.getText() != null ? address3
							.getText().toString() : "");
					enroll.setAddress4(address4.getText() != null ? address4
							.getText().toString() : "");

					enroll.setZipCode(zip.getText().toString());
					enroll.setEmailId(email.getText() != null ? email.getText()
							.toString() : "");
					enroll.setState(state.getText() != null ? state.getText()
							.toString() : "");
					enroll.setProfessionRemark(remarks.getText().toString());
					enroll.setNationality(enrolement.getNationality() != null ? enrolement
							.getNationality() : "");
					enroll.setCountry(enrolement.getCountry());
					enroll.setMartialStatus(enrolement.getMartialStatus());
					enroll.setProfession(enrolement.getProfession());
					enroll.setAgentId(CommonContexts.mLOGINVALIDATION
							.getAgentId());

					if (enroll.getGroupIndiviType() != null
							&& enroll.getGroupIndiviType()
									.equalsIgnoreCase("G"))
						enroll.setTempGroupId(mGroupSpinner.getSelectedItem()
								.toString());

					enroll.setPoc(POC);
					mPager.setCurrentItem(2);

				} else {
					ViewUtil.showCrutonError(mContext, R.string.messg_fill_kyc,
							handler);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
	}

	public static void clearFields() {
		firstname.setText("");
		lastname.setText("");
		middlename.setText("");
		dob.setText("");
		address1.setText("");
		contactnumber.setText("");
		address2.setText("");
		address3.setText("");
		address4.setText("");
		state.setText("");
		zip.setText("");
		email.setText("");
		remarks.setText("");
		nationality.setSelection(0);
		country.setSelection(0);
		maritalstatus.setSelection(0);
		profession.setSelection(0);

	}

	public boolean emailValidation(String emailstring) {
		Pattern emailPattern = Pattern
				.compile("^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			Object tag = BaseActivity.mBtnLeft.getTag();
			int id = tag == null ? -1 : (Integer) tag;
			if (id == R.drawable.back) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_ENROLL_DETAILS)) {
					mPager.setCurrentItem(0);

				}
			}
		
			break;

		case CMD_RIGHT_ACTION:
			

			break;
		}
	}
	
	

	@SuppressWarnings("unused")
	private void getBitmapFromString(String encodedImage) {
		byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
				decodedString.length);
		storeImage(decodedByte);

	}

	private void storeImage(Bitmap image) {
		File pictureFile = getOutputMediaFile();
		if (pictureFile == null) {
			Log.d("", Constants.ERR_CREATING_MEDIA);// e.getMessage());
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			image.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d("", Constants.FILE_NT_FOUND + e.getMessage());
		} catch (IOException e) {
			Log.d("", Constants.ERR_FILE_ACCESS + e.getMessage());
		}
	}

	private File getOutputMediaFile() {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.
		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory() + "/Android/data/"
						+ mContext.getPackageName() + "/Files");

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm")
				.format(new Date());
		File mediaFile;
		String mImageName = "MI_" + timeStamp + ".jpg";
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ mImageName);
		return mediaFile;
	}

	private void handleBackToEntryView() {
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.cancel);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.cancel);
		mViewGroup.removeAllViews();
		doLoadInit();
		handleBackEnrollEntry();
		mViewGroup.addView(mView);
	}

	private void handleBackEnrollEntry() {
		Enrolement enroll = CommonContexts.SELECTED_ENROLEMENT;
		firstname.setText(enroll.getFirstName());
		lastname.setText(enroll.getLastName());
		middlename.setText(enroll.getMiddleName());
		dob.setText(dobDate);
		address1.setText(enroll.getAddress1());
		contactnumber.setText(enroll.getContactNo());
		address2.setText(enroll.getAddress2());
		address3.setText(enroll.getAddress3());
		address4.setText(enroll.getAddress4());
		state.setText(enroll.getState());
		zip.setText(enroll.getZipCode());
		email.setText(enroll.getEmailId());
		nationality.setSelection(nationalityPos);
		country.setSelection(countryPos);
		maritalstatus.setSelection(maritalstatusPos);
		profession.setSelection(professionPos);
		remarks.setText(enroll.getProfessionRemark());
		if (ENROLL.equalsIgnoreCase("NONRESI")) {
			mRadioButtonNonResident.setChecked(true);
			nationality.setVisibility(View.VISIBLE);

		}
		if (ENROLL.equalsIgnoreCase("RESI")) {
			mRadioButtonresident.setChecked(true);
			nationality.setVisibility(View.INVISIBLE);
		}
		if (CommonContexts.GROUP.equalsIgnoreCase("GRP")) {
			mRadioButtonGroup.setChecked(true);
			mLinlaySpinner.setVisibility(View.VISIBLE);
			mLinlayPoc.setVisibility(View.VISIBLE);

		}
		if (CommonContexts.GROUP.equalsIgnoreCase("INDVI")) {
			mRadioButtonIndividual.setChecked(true);
			mLinlaySpinner.setVisibility(View.GONE);
			mLinlayPoc.setVisibility(View.GONE);
		}

		if (POC.equalsIgnoreCase("Y")) {
			mPocYes.setChecked(true);
		}
		if (POC.equalsIgnoreCase("N")) {
			mPocNo.setChecked(true);
		}

	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_ENROLL_DETAILS)) {
				mPager.setCurrentItem(0);

			/*if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(ENROL_VIEW_VERIFY))
				handleBackToEntryView();
			else if (mPager.getCurrentItem() == 1)
				mPager.setCurrentItem(0);*/

		}
	}

	View.OnClickListener addListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			AlertGroupIdDiologue();

		}
	};

	protected void GenerateGroupId(String type) {
		List<String> listStrings = null;
		String firstName = "";
		if (!(firstname.getText().toString().isEmpty())
				&& firstname.getText().toString().length() >= 4)
			firstName = firstname.getText().toString().substring(0, 3);

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
		Calendar cal = Calendar.getInstance();
		String grpId = type + firstName + sdf.format(cal.getTime());

		if (AppPref.getPref(mContext, AppPref.GROUPID) != null) {
			listStrings = new ArrayList<String>(Arrays.asList(TextUtils.split(
					AppPref.getPref(mContext, AppPref.GROUPID), ",")));
		} else {
			listStrings = new ArrayList<String>();
		}
		if (listStrings != null && !(listStrings.contains(grpId))) {
			listStrings.add(grpId);
			AppPref.updatePref(mContext, AppPref.GROUPID,
					TextUtils.join(",", listStrings));

		} else {
			Toast.makeText(mContext, Constants.ID_ALREADY_AVL,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void AlertGroupIdDiologue() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);

		// set title
		alertDialogBuilder.setTitle(Constants.GRP_ID);

		// set dialog message
		alertDialogBuilder
				.setMessage(Constants.GEN_GRP_ID)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								GenerateGroupId("GR");

								spinnerVal();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just
						// close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

}
