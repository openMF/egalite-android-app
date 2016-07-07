package com.bfsi.egalite.view.enrolkyc;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.bfsi.egalite.dao.sqlite.EnrolmentDataAccess;
import com.bfsi.egalite.entity.CustomerDocuments;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.service.impl.BaseTransactionService;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.util.ViewUtil;
import com.bfsi.egalite.view.KycOnlyView;
import com.bfsi.egalite.view.customers.CustomersAll;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CustomerDocument;
import com.bfsi.mfi.rest.model.CustomerEnrolmentDetails;


/**
 * @author Vijay
 * EGA-MN15-000012
 * Captures the Kyc information of an existing customer
 *
 */
public class KycView extends Fragment implements CommandListener {

	private static final String VIEW_KYC_ENTRY = "EntryKyc";
	private static final String VIEW_KYC_VERIFY = "VerifyKyc";
	private static final String VIEW_KYC_CONFIRM = "ConfirmKyc";
	private static final String SIGNATURE_PROOF = "Signature Proof";
	private static final String ADDRESS_PROOF = "Address Proof";
	private static final String ID_PROOF = "Id Proof";
	private Context mContext;
	private static ImageView img_photo, img_photo_select1, img_photo_select2,
			img_photo_select3;
	private static EditText mKycNo1, mKycNo2, mKycNo3;
	private static Spinner spinner_select1, spinner_select2, spinner_select3;
	private String selectedvalue1, selectedvalue2, selectedvalue3;
	private static String selectedRadio1, selectedRadio2, selectedRadio3;
	private View mView;
	private static int imageType = 0;
	private int selectedId1, selectedId2, selectedId3;
	private List<String> documentTags1, documentTags2, documentTags3;
	private BfsiViewPager mPager;
	private static RadioButton mRadidProof1, mRadAddresProof1, mRadSignProof1,
			mRadidProof2, mRadAddresProof2, mRadSignProof2, mRadidProof3,
			mRadAddresProof3, mRadSignProof3;
	private LayoutInflater mLayoutInflater = null;
	private static ViewGroup mViewGroup;
	private Button btn_next;

	public static KycView newInstance(Context context, BfsiViewPager pager) {
		KycView fragment = new KycView();
		fragment.mPager = pager;
		fragment.mContext = context;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		mLayoutInflater = inflater;
		((BaseActivity) this.getActivity()).unregisterCommandListener(this);
		((BaseActivity) this.getActivity()).registerCommandListener(this);
		initLoad();
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

	private void initLoad() {
		// CommonContexts.CURRENT_SCREEN = CommonContexts.SCREEN_KYC;
		CommonContexts.CURRENT_VIEW = VIEW_KYC_ENTRY;
		mView = mLayoutInflater.inflate(R.layout.kycentry, null);

		img_photo = (ImageView) mView.findViewById(R.id.img_kyc_entry_photo);
		btn_next = (Button) mView.findViewById(R.id.kyc_next);
		img_photo_select1 = (ImageView) mView
				.findViewById(R.id.img_kyc_entry_selct1);
		img_photo_select2 = (ImageView) mView
				.findViewById(R.id.img_kyc_entry_selct2);
		img_photo_select3 = (ImageView) mView
				.findViewById(R.id.img_kyc_entry_selct3);

		img_photo.setTag(R.drawable.cameralight);
		img_photo_select1.setTag(R.drawable.cameralight);
		img_photo_select2.setTag(R.drawable.cameralight);
		img_photo_select3.setTag(R.drawable.cameralight);

		spinner_select1 = (Spinner) mView
				.findViewById(R.id.spinner_idproof_select1);
		spinner_select2 = (Spinner) mView
				.findViewById(R.id.spinner_idproof_select2);
		spinner_select3 = (Spinner) mView
				.findViewById(R.id.spinner_idproof_select3);
		mKycNo1 = (EditText) mView.findViewById(R.id.edt_kycproof1);
		mKycNo2 = (EditText) mView.findViewById(R.id.edt_kycproof2);
		mKycNo3 = (EditText) mView.findViewById(R.id.edt_kycproof3);

		addSpinners();

		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleSaveForVerification();
			}
		});

		if (CommonContexts.SELECTED_ENROLEMENT != null
				&& CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs() != null) {

			CustomerDocuments docs = CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs();

			Bitmap bmp = BitmapFactory.decodeByteArray(
					docs.getKycImgCustomer(), 0,
					docs.getKycImgCustomer().length);
			img_photo.setImageBitmap(bmp);

			Bitmap bmp1 = BitmapFactory.decodeByteArray(docs.getKycId1Img(), 0,
					docs.getKycId1Img().length);
			img_photo_select1.setImageBitmap(bmp1);
			String documentName1 = docs.getKycId1Number();
			setSpinner(documentName1, 1);

			Bitmap bmp2 = BitmapFactory.decodeByteArray(docs.getKycId2Img(), 0,
					docs.getKycId2Img().length);
			img_photo_select2.setImageBitmap(bmp2);
			String documentName2 = docs.getKycId2Number();
			setSpinner(documentName2, 2);

			Bitmap bmp3 = BitmapFactory.decodeByteArray(docs.getKycId3Img(), 0,
					docs.getKycId3Img().length);
			img_photo_select3.setImageBitmap(bmp3);
			String documentName3 = docs.getKycId3Number();
			setSpinner(documentName3, 3);

		}

		initialiseListener();

	}

	private void setSpinner(String documentName, int position) {
		List<String> idTypelist = EnrolmentDataAccess
				.readlov(EnrolmentDataAccess.KYC01);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_text, idTypelist);
		for (int i = 0; i < dataAdapter.getCount(); i++) {
			if (documentName.equals(dataAdapter.getItem(i).toString())) {
				if (position == 1) {
					spinner_select1.setSelection(i);
				} else if (position == 2) {
					spinner_select2.setSelection(i);
				} else if (position == 3) {
					spinner_select3.setSelection(i);
				}
				break;
			}
		}
	}

	private void addSpinners() {

		List<String> idTypelist = EnrolmentDataAccess
				.readlov(EnrolmentDataAccess.KYC01);

		idTypelist.add("Select *");
		Collections.reverse(idTypelist);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_text, idTypelist);
		dataAdapter.setDropDownViewResource(R.layout.spinner_text_black);

		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(mContext,
				R.layout.spinner_text, idTypelist);
		dataAdapter1.setDropDownViewResource(R.layout.spinner_text_black);

		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(mContext,
				R.layout.spinner_text, idTypelist);
		dataAdapter2.setDropDownViewResource(R.layout.spinner_text_black);
		spinner_select1.setAdapter(dataAdapter);
		spinner_select1
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						selectedvalue1 = spinner_select1.getSelectedItem()
								.toString();
						CommonContexts.SELECTEDPROOFTYPE = selectedvalue1;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinner_select2.setAdapter(dataAdapter1);
		spinner_select2
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						selectedvalue2 = spinner_select2.getSelectedItem()
								.toString();
						CommonContexts.SELECTEDPROOFTYPE2 = selectedvalue2;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		spinner_select3.setAdapter(dataAdapter2);

		spinner_select3
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						selectedvalue3 = spinner_select3.getSelectedItem()
								.toString();
						CommonContexts.SELECTEDPROOFTYPE3 = selectedvalue3;
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
	}

	private void initialiseListener() {
		img_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageType = 1;
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
			}
		});
		img_photo_select1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageType = 2;
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
			}
		});
		img_photo_select2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageType = 3;
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
			}
		});
		img_photo_select3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageType = 4;
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 0);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == getActivity().RESULT_OK) {
			if (data != null) {
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				setImage(bitmap);

			}
		}
	}

	private void setImage(Bitmap bitmap) {
		if (imageType == 1) {
			img_photo.setTag(bitmap);
			img_photo.setImageBitmap(bitmap);
		} else if (imageType == 2) {
			img_photo_select1.setTag(bitmap);
			img_photo_select1.setImageBitmap(bitmap);
		} else if (imageType == 3) {
			img_photo_select2.setTag(bitmap);
			img_photo_select2.setImageBitmap(bitmap);
		} else if (imageType == 4) {
			img_photo_select3.setTag(bitmap);
			img_photo_select3.setImageBitmap(bitmap);
		}
	}

	private void handleBackToEntryView() {
		BaseActivity.mTitle.setText(R.string.kyc_entry);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnRight.setVisibility(View.GONE);
		BaseActivity.mBtnLeft.setTag(R.drawable.home);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.home);

		initLoad();
		radioBtnCheck();
		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);
	}

	private void radioBtnCheck() {

		mRadidProof1 = (RadioButton) mView.findViewById(R.id.radbtn_idproof1);
		mRadAddresProof1 = (RadioButton) mView
				.findViewById(R.id.radbtn_addressproof1);
		mRadSignProof1 = (RadioButton) mView
				.findViewById(R.id.radbtn_signproof1);
		mRadidProof2 = (RadioButton) mView.findViewById(R.id.radbtn_idproof2);
		mRadAddresProof2 = (RadioButton) mView
				.findViewById(R.id.radbtn_addressproof2);
		mRadSignProof2 = (RadioButton) mView
				.findViewById(R.id.radbtn_signproof2);
		mRadidProof3 = (RadioButton) mView.findViewById(R.id.radbtn_idproof3);
		mRadAddresProof3 = (RadioButton) mView
				.findViewById(R.id.radbtn_addressproof3);
		mRadSignProof3 = (RadioButton) mView
				.findViewById(R.id.radbtn_signproof3);
		if (selectedRadio1 != null && selectedRadio1.equalsIgnoreCase(ID_PROOF)) {
			mRadidProof1.setChecked(true);
		} else if (selectedRadio1 != null
				&& selectedRadio1.equalsIgnoreCase(ADDRESS_PROOF)) {
			mRadAddresProof1.setChecked(true);

		} else if (selectedRadio1 != null
				&& selectedRadio1.equalsIgnoreCase(SIGNATURE_PROOF)) {
			mRadSignProof1.setChecked(true);
		}

		if (selectedRadio2 != null && selectedRadio2.equalsIgnoreCase(ID_PROOF)) {
			mRadidProof2.setChecked(true);
		} else if (selectedRadio2 != null
				&& selectedRadio2.equalsIgnoreCase(ADDRESS_PROOF)) {
			mRadAddresProof2.setChecked(true);

		} else if (selectedRadio2 != null
				&& selectedRadio2.equalsIgnoreCase(SIGNATURE_PROOF)) {
			mRadSignProof2.setChecked(true);
		}

		if (selectedRadio3 != null && selectedRadio3.equalsIgnoreCase(ID_PROOF)) {
			mRadidProof3.setChecked(true);
		} else if (selectedRadio3 != null
				&& selectedRadio3.equalsIgnoreCase(ADDRESS_PROOF)) {
			mRadAddresProof3.setChecked(true);

		} else if (selectedRadio3 != null
				&& selectedRadio3.equalsIgnoreCase(SIGNATURE_PROOF)) {
			mRadSignProof3.setChecked(true);
		}

	}

	private CustomerEnrolmentDetails generateCustomerDetails() {
		CustomerEnrolmentDetails ced = new CustomerEnrolmentDetails();
		Enrolement enrol = CommonContexts.SELECTED_ENROLEMENT;
		ced.setEnrolmentId(enrol.getEnrollmentId());
		ced.setModuleCode("CE");
		ced.setTxnCode("111");
		ced.setPoc("Y");
		ced.setLocCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
		ced.setAgentId(enrol.getAgentId());
		ced.setDeviceId(enrol.getDeviceId());
		ced.setTxnInitTime(DateUtil.getCurrentDataTime());
		ced.setWorkFlowQueType(enrol.getWorkflowQType());
		CustomerDocuments cd = enrol.getCustomerDocs();
		ced.setTxnStatus(0);
		ced.setIsKycOnly("Y");
		ced.setCustomerId(enrol.getCustomerId());
		ced.setFirstName(enrol.getFirstName());
		ced.setContactNo(enrol.getContactNo());
		ced.setGender(enrol.getGender());
		CustomerDocument cds = new CustomerDocument();
		cds.setEnrolmentId(enrol.getEnrollmentId());
		cds.setKycCustImage(new String(Base64.encode(cd.getKycImgCustomer(),
				Base64.DEFAULT)));
		cds.setKycIdImage1(new String(Base64.encode(cd.getKycId1Img(),
				Base64.DEFAULT)));
		cds.setKycIdImage2(new String(Base64.encode(cd.getKycId2Img(),
				Base64.DEFAULT)));
		cds.setKycIdImage3(new String(Base64.encode(cd.getKycId3Img(),
				Base64.DEFAULT)));
		cds.setKycIdNo1(cd.getKycId1Number());
		cds.setKycIdNo2(cd.getKycId2Number());
		cds.setKycIdNo3(cd.getKycId3Number());
		cds.setKycIdType1(EnrolmentDataAccess.readIntLovValue(
				cd.getKycId1Type(), EnrolmentDataAccess.KYC01));
		cds.setKycIdType2(EnrolmentDataAccess.readIntLovValue(
				cd.getKycId2Type(), EnrolmentDataAccess.KYC01));
		cds.setKycIdType3(EnrolmentDataAccess.readIntLovValue(
				cd.getKycId3Type(), EnrolmentDataAccess.KYC01));
		cds.setKycIdProofType1(checkIntVal(cd.getKycId1Proof()));
		cds.setKycIdProofType2(checkIntVal(cd.getKycId2Proof()));
		cds.setKycIdProofType3(checkIntVal(cd.getKycId3Proof()));
		ced.setDocument(cds);

		return ced;
	}

	private String checkIntVal(String kycId1Proof) {
		if (kycId1Proof.equals("Id Proof")) {
			return "0";
		} else if (kycId1Proof.equals("Address Proof")) {
			return "1";
		} else if (kycId1Proof.equals("Signature Proof")) {
			return "2";
		} else {
			return "";
		}
	}

	// private void handlePrintKyc() {
	// EnrolementDao enroldao = DaoFactory.getEnrolementDao();
	// if (CommonContexts.SELECTED_ENROLEMENT != null) {
	// CustomerEnrolmentDetails ced = generateCustomerDetails();
	// enroldao.insertEnrolement(ced);
	// CommonContexts.SELECTED_ENROLEMENT = null;
	// }
	// }

	private void handleSaveForVerification() {
		CommonContexts.SELECTED_ENROLEMENT = new Enrolement();
		Bitmap phot_bitmap = ((BitmapDrawable) img_photo.getDrawable())
				.getBitmap();
		Bitmap phot_bitmap1 = ((BitmapDrawable) img_photo_select1.getDrawable())
				.getBitmap();
		Bitmap phot_bitmap2 = ((BitmapDrawable) img_photo_select2.getDrawable())
				.getBitmap();
		Bitmap phot_bitmap3 = ((BitmapDrawable) img_photo_select3.getDrawable())
				.getBitmap();
		RadioGroup mradioGrp1 = (RadioGroup) mView
				.findViewById(R.id.rad_grp_select1);
		RadioGroup mradioGrp2 = (RadioGroup) mView
				.findViewById(R.id.rad_grp_select2);
		RadioGroup mradioGrp3 = (RadioGroup) mView
				.findViewById(R.id.rad_grp_select3);
		selectedId1 = mradioGrp1.getCheckedRadioButtonId();
		selectedId2 = mradioGrp2.getCheckedRadioButtonId();
		selectedId3 = mradioGrp3.getCheckedRadioButtonId();
		documentTags1 = new ArrayList<String>();
		documentTags2 = new ArrayList<String>();
		documentTags3 = new ArrayList<String>();
		String spinnerSelect1 = spinner_select1.getSelectedItem().toString();
		String spinnerSelect2 = spinner_select2.getSelectedItem().toString();
		String spinnerSelect3 = spinner_select3.getSelectedItem().toString();

		if (!(selectedId1 == -1 && selectedId1 == -1 && selectedId1 == -1)) {
			radioSelect();
		}

		CustomerDocuments enrolDocuments0 = new CustomerDocuments();

		enrolDocuments0.setKycImgCustomer(getBitmap(phot_bitmap));
		enrolDocuments0.setKycId1Number(mKycNo1.getText().toString());
		enrolDocuments0.setKycId1Type(selectedvalue1);
		enrolDocuments0.setKycId1Proof(selectedRadio1);
		enrolDocuments0.setKycId1Img(getBitmap(phot_bitmap1));

		enrolDocuments0.setKycId2Number(mKycNo2.getText().toString());
		enrolDocuments0.setKycId2Type(selectedvalue2);
		enrolDocuments0.setKycId2Proof(selectedRadio2);
		enrolDocuments0.setKycId2Img(getBitmap(phot_bitmap2));

		enrolDocuments0.setKycId3Number(mKycNo3.getText().toString());
		enrolDocuments0.setKycId3Type(selectedvalue3);
		enrolDocuments0.setKycId3Proof(selectedRadio3);
		enrolDocuments0.setKycId3Img(getBitmap(phot_bitmap3));

		CommonContexts.SELECTED_ENROLEMENT.setCustomerDocs(enrolDocuments0);
		CommonContexts.SELECTED_ENROLEMENT.setTxnInitTime(DateUtil
				.getCurrentDataTime());
		CommonContexts.SELECTED_ENROLEMENT
				.setEnrollmentId(BaseTransactionService
						.generateEnrolSequence("KY"));
		CommonContexts.SELECTED_ENROLEMENT
				.setCustomerId(CommonContexts.SELECTED_CUSTOMER.getCustomerId());
		CommonContexts.SELECTED_ENROLEMENT
				.setFirstName(CommonContexts.SELECTED_CUSTOMER
						.getCustomerFullName());
		CommonContexts.SELECTED_ENROLEMENT
				.setContactNo(CommonContexts.SELECTED_CUSTOMER
						.getMobileNumber());
		CommonContexts.SELECTED_ENROLEMENT
				.setGender(CommonContexts.SELECTED_CUSTOMER.getGender());
		// CommonContexts.SELECTED_ENROLEMENT
		// .setDob(CommonContexts.SELECTED_CUSTOMER.getDob());
		//

		CommonContexts.SELECTED_ENROLEMENT
				.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		CommonContexts.SELECTED_ENROLEMENT
				.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());

		if (!(documentTags1.size() > 0) || !(documentTags2.size() > 0)
				|| !(documentTags3.size() > 0)) {

			ViewUtil.showCrutonError(mContext, R.string.messg_idproof, handler);
		} else if (spinnerSelect1.equalsIgnoreCase(Constants.SELECT)
				|| spinnerSelect2.equalsIgnoreCase(Constants.SELECT)
				|| spinnerSelect3.equalsIgnoreCase(Constants.SELECT)) {

			ViewUtil.showCrutonError(mContext, R.string.messg_idproof, handler);
		} else if (documentTags1.equals(Constants.SELECT)
				|| documentTags2.equals(Constants.SELECT)
				|| documentTags3.equals(Constants.SELECT)) {
			ViewUtil.showCrutonError(mContext, R.string.messg_idproof, handler);
		}

		else if (numSame(documentTags1, documentTags2)
				|| numSame(documentTags1, documentTags3)
				|| numSame(documentTags2, documentTags3)) {
			ViewUtil.showCrutonError(mContext, R.string.messg_idproofdublicate,
					handler);
		} else

		if (selectedId1 == -1 && selectedId1 == -1 && selectedId1 == -1) {
			ViewUtil.showCrutonError(mContext, R.string.messg_idproof, handler);
		}
		/*
		 * else if (selectedvalue1.equals(selectedvalue2) ||
		 * selectedvalue1.equals(selectedvalue3)) {
		 * ViewUtil.showCrutonError(mContext, R.string.messg_documentname,
		 * handler); } else if (selectedvalue2.equals(selectedvalue1) ||
		 * selectedvalue2.equals(selectedvalue3)) {
		 * ViewUtil.showCrutonError(mContext, R.string.messg_documentname,
		 * handler); } else if (selectedvalue3.equals(selectedvalue1) ||
		 * selectedvalue3.equals(selectedvalue2)) {
		 * ViewUtil.showCrutonError(mContext, R.string.messg_documentname,
		 * handler); }
		 *//*
			 * else if (mKycNo1.getText().toString().isEmpty() ||
			 * mKycNo2.getText().toString().isEmpty() ||
			 * mKycNo3.getText().toString().isEmpty()) {
			 * ViewUtil.showCrutonError(mContext, R.string.messg_cardnumber,
			 * handler); }
			 */
		else if (!photoValidate()) {
			ViewUtil.showCrutonError(mContext, R.string.messg_photo, handler);
		} else {

			// onVerifyView();
			mPager.setCurrentItem(1);
		}

	}

	private boolean photoValidate() {
		if (img_photo
				.getDrawable()
				.getConstantState()
				.equals(getResources().getDrawable(R.drawable.cameralight)
						.getConstantState())
				|| img_photo_select1
						.getDrawable()
						.getConstantState()
						.equals(getResources().getDrawable(
								R.drawable.cameralight).getConstantState())
				|| img_photo_select2
						.getDrawable()
						.getConstantState()
						.equals(getResources().getDrawable(
								R.drawable.cameralight).getConstantState())
				|| img_photo_select3
						.getDrawable()
						.getConstantState()
						.equals(getResources().getDrawable(
								R.drawable.cameralight).getConstantState())) {
			return false;
		} else
			return true;
	}

	private void radioSelect() {
		RadioButton mradioBtn1 = (RadioButton) mView.findViewById(selectedId1);
		selectedRadio1 = (String) mradioBtn1.getText();
		documentTags1.add(selectedRadio1);
		RadioButton mradioBtn2 = (RadioButton) mView.findViewById(selectedId2);
		selectedRadio2 = (String) mradioBtn2.getText();
		documentTags2.add(selectedRadio2);
		RadioButton mradioBtn3 = (RadioButton) mView.findViewById(selectedId3);
		selectedRadio3 = (String) mradioBtn3.getText();
		documentTags3.add(selectedRadio3);
	}

	private void onVerifyView() {
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		// mTitle.setText(CommonContexts.SELECTED_CUSTOMER.getCustomerFullName()
		// + "- " + getString(R.string.kyc_verify));
		CommonContexts.CURRENT_VIEW = VIEW_KYC_VERIFY;
		onVerifyLoadWidgets();

	}

	private void onVerifyLoadWidgets() {
		mView = mLayoutInflater.inflate(R.layout.kycverify, null);

		TextView docName1 = (TextView) mView.findViewById(R.id.txv_docname1);
		TextView docName2 = (TextView) mView.findViewById(R.id.txv_docname2);
		TextView docName3 = (TextView) mView.findViewById(R.id.txv_docname3);

		TextView docproof1 = (TextView) mView.findViewById(R.id.docproof1);
		TextView docproof2 = (TextView) mView.findViewById(R.id.docproof2);
		TextView docproof3 = (TextView) mView.findViewById(R.id.docproof3);

		ImageView imgView1 = (ImageView) mView.findViewById(R.id.imageView1);
		ImageView imgView2 = (ImageView) mView.findViewById(R.id.imageView2);
		ImageView imgView3 = (ImageView) mView.findViewById(R.id.imageView3);
		ImageView imgView4 = (ImageView) mView.findViewById(R.id.imageView4);

		docName1.setText(CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
				.getKycId1Type());
		docName2.setText(CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
				.getKycId2Type());
		docName3.setText(CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
				.getKycId3Type());

		docproof1.setText("as "
				+ CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
						.getKycId1Proof());
		docproof2.setText("as "
				+ CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
						.getKycId2Proof());
		docproof3.setText("as "
				+ CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
						.getKycId3Proof());

		Bitmap bmp = BitmapFactory.decodeByteArray(
				CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
						.getKycImgCustomer(), 0,
				CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
						.getKycImgCustomer().length);
		Bitmap bmp1 = BitmapFactory.decodeByteArray(
				CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
						.getKycId1Img(), 0, CommonContexts.SELECTED_ENROLEMENT
						.getCustomerDocs().getKycId1Img().length);
		Bitmap bmp2 = BitmapFactory.decodeByteArray(
				CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
						.getKycId2Img(), 0, CommonContexts.SELECTED_ENROLEMENT
						.getCustomerDocs().getKycId2Img().length);
		Bitmap bmp3 = BitmapFactory.decodeByteArray(
				CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
						.getKycId3Img(), 0, CommonContexts.SELECTED_ENROLEMENT
						.getCustomerDocs().getKycId3Img().length);
		imgView1.setImageBitmap(bmp);
		imgView2.setImageBitmap(bmp1);
		imgView3.setImageBitmap(bmp2);
		imgView4.setImageBitmap(bmp3);

		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);

	}

	public static boolean numSame(List<String> list1, List<String> list2) {

		for (int i = 0; i <= list1.size() - 1; i++) {
			for (int j = 0; j <= list2.size() - 1; j++) {
				if (list1.get(i).equalsIgnoreCase(list2.get(j))) {
					return true;
				}
			}
		}
		return false;

	}

	private byte[] getBitmap(Bitmap bitmap) {
		byte[] data1 = null;
		Bitmap compress_bitmap = bitmap;
		compress_bitmap = getResizedBitmap(compress_bitmap,
				(compress_bitmap.getHeight()), (compress_bitmap.getWidth()));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		compress_bitmap.compress(CompressFormat.JPEG, 100, bos);
		data1 = bos.toByteArray();
		return data1;
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_KYC)) {

			if (CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_KYC_VERIFY)) {
				handleBackToEntryView();
			} else if (CommonContexts.CURRENT_VIEW
					.equalsIgnoreCase(VIEW_KYC_ENTRY)) {
				startActivity(new Intent(mContext, CustomersAll.class));
				getActivity().finish();

			} else if (CommonContexts.CURRENT_VIEW
					.equalsIgnoreCase(VIEW_KYC_CONFIRM)) {
				clearFields();
				handleBackToEntryView();
			} else {
				CommonContexts.SELECTED_ENROLEMENT = null;
				CommonContexts.SELECTED_CUSTOMER = null;
				handleBackToEntryView();
			}
		}
	}

	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			Object tag = BaseActivity.mBtnLeft.getTag();
			int id = tag == null ? -1 : (Integer) tag;

			if (CommonContexts.CURRENT_SCREEN
					.equalsIgnoreCase(CommonContexts.SCREEN_KYC)) {

				if (id == R.drawable.back) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_KYC_VERIFY)) {
						handleBackToEntryView();
					}
				} else if (id == R.drawable.home) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_KYC_ENTRY)) {
						CommonContexts.SELECTED_ENROLEMENT = null;
						CommonContexts.SELECTED_CUSTOMER = null;
						clearFields();
						startActivity(new Intent(mContext, CustomersAll.class));
						getActivity().finish();
					}
				} else if (id == R.drawable.cancel) {
					if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_KYC_ENTRY)) {
						startActivity(new Intent(mContext, CustomersAll.class));
						getActivity().finish();

					} else if (CommonContexts.CURRENT_VIEW
							.equalsIgnoreCase(VIEW_KYC_CONFIRM)) {
						// go back to agenda with reloading (true) as the
						// transaction is committed already
						clearFields();
						handleBackToEntryView();
					} else {
						CommonContexts.SELECTED_ENROLEMENT = null;
						CommonContexts.SELECTED_CUSTOMER = null;
						// firstTime onCancel - for now back to agenda without
						// reloading
						handleBackToEntryView();
					}
				}
			}
			break;

		case CMD_RIGHT_ACTION:
			Object tags = BaseActivity.mBtnRight.getTag();
			int ids = tags == null ? -1 : (Integer) tags;
			if (CommonContexts.CURRENT_SCREEN
					.equalsIgnoreCase(CommonContexts.SCREEN_KYC)) {
				if (ids == R.drawable.save) {

					// handleSaveForVerification();
				} else if (CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_KYC_VERIFY)
						&& (ids == R.drawable.verify)) {

				} else if (ids == R.drawable.print) {
					// handlePrintKyc();
					mPager.setAdapter(KycOnlyView.adapter);
					mPager.setCurrentItem(1);
				}

			}
			break;

		}
	}

	public static void clearFields() {
		mKycNo1.setText("");
		mKycNo2.setText("");
		mKycNo3.setText("");
		spinner_select1.setSelection(0);
		spinner_select2.setSelection(0);
		spinner_select3.setSelection(0);
		selectedRadio1 = null;
		selectedRadio2 = null;
		selectedRadio3 = null;
		if (mRadidProof1 != null)
			mRadidProof1.setChecked(false);
		if (mRadAddresProof1 != null)
			mRadAddresProof1.setChecked(false);
		if (mRadSignProof1 != null)
			mRadSignProof1.setChecked(false);
		if (mRadidProof2 != null)
			mRadidProof2.setChecked(false);
		if (mRadAddresProof2 != null)
			mRadAddresProof2.setChecked(false);
		if (mRadSignProof2 != null)
			mRadSignProof2.setChecked(false);
		if (mRadidProof3 != null)
			mRadidProof3.setChecked(false);
		if (mRadAddresProof3 != null)
			mRadAddresProof3.setChecked(false);
		if (mRadSignProof3 != null)
			mRadSignProof3.setChecked(false);
		CommonContexts.SELECTED_ENROLEMENT = null;

	}
}
