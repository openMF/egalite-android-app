package com.bfsi.egalite.view.enrolkyc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.EnrolementDao;
import com.bfsi.egalite.dao.sqlite.EnrolmentDataAccess;
import com.bfsi.egalite.entity.CustomerDocuments;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.evolute.FingerPrintAsync;
import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.TabPageIndicator;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.view.KycOnlyView;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CustomerDocument;
import com.bfsi.mfi.rest.model.CustomerEnrolmentDetails;

/**
 * @author Vijay EGA-MN15-000012 Captures the Biometric information of an
 *         exsting customer
 * 
 */
public class KycBiometricProof extends Fragment implements CommandListener,
		OnClickListener {
	private static final String VIEW_ENROL_BIOMETRIC = "BIOMETRIC";
	private static final String VIEW_KYC_VERIFY = "VerifyKyc";
	private static final String VIEW_KYC_CONFIRM = "ConfirmKyc";
	private Context mContext;
	private BfsiViewPager mPager;
	private static ViewGroup mViewGroup;
	private View mView;
	private LayoutInflater mLayoutInflater = null;
	private TabPageIndicator mTabPageIndicator;
	private ImageView mImgBioproofSel1, mImgBioproofSel2, mImgBioproofSel3,
			mImgBioproofSel4, mImgBioproofSel5, mImgBioproofSel6,
			mImgBioproofSel7, mImgBioproofSel8, mImgBioproofSel9,
			mImgBioproofSel10;
	private FingerPrintAsync fp;

	public static KycBiometricProof newInstance(Context context,
			BfsiViewPager pager, TabPageIndicator mPageIndicat) {
		KycBiometricProof fragment = new KycBiometricProof();
		fragment.mPager = pager;
		fragment.mContext = context;
		fragment.mTabPageIndicator = mPageIndicat;
		return fragment;
	}

	@SuppressLint("HandlerLeak")
	public final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == CommonContexts.SUCCESS) {
				BaseActivity.mLinearLayout.setVisibility(View.GONE);
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mPager.getCurrentItem() == 2)
			BaseActivity.mBtnRight.setVisibility(View.VISIBLE);
		mContext = getActivity();
		mLayoutInflater = inflater;
		((BaseActivity) this.getActivity()).unregisterCommandListener(this);
		((BaseActivity) this.getActivity()).registerCommandListener(this);
		initLoad();
		mViewGroup = new LinearLayout(getActivity());
		mViewGroup.addView(mView);
		return mViewGroup;
	}

	private void initLoad() {
		CommonContexts.CURRENT_VIEW = VIEW_ENROL_BIOMETRIC;
		mView = mLayoutInflater
				.inflate(R.layout.enrolment_biometricproof, null);

		mImgBioproofSel1 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select1);
		mImgBioproofSel1.setTag(R.drawable.fpsselector);

		mImgBioproofSel2 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select2);
		mImgBioproofSel2.setTag(R.drawable.fpsselector);

		mImgBioproofSel3 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select3);
		mImgBioproofSel3.setTag(R.drawable.fpsselector);

		mImgBioproofSel4 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select4);
		mImgBioproofSel4.setTag(R.drawable.fpsselector);

		mImgBioproofSel5 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select5);
		mImgBioproofSel5.setTag(R.drawable.fpsselector);

		mImgBioproofSel6 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select6);
		mImgBioproofSel6.setTag(R.drawable.fpsselector);

		mImgBioproofSel7 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select7);
		mImgBioproofSel7.setTag(R.drawable.fpsselector);

		mImgBioproofSel8 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select8);
		mImgBioproofSel8.setTag(R.drawable.fpsselector);

		mImgBioproofSel9 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select9);
		mImgBioproofSel9.setTag(R.drawable.fpsselector);

		mImgBioproofSel10 = (ImageView) mView
				.findViewById(R.id.img_bioproof_select10);
		mImgBioproofSel10.setTag(R.drawable.fpsselector);

		mImgBioproofSel1.setOnClickListener(this);
		mImgBioproofSel2.setOnClickListener(this);
		mImgBioproofSel3.setOnClickListener(this);
		mImgBioproofSel4.setOnClickListener(this);
		mImgBioproofSel5.setOnClickListener(this);
		mImgBioproofSel6.setOnClickListener(this);
		mImgBioproofSel7.setOnClickListener(this);
		mImgBioproofSel8.setOnClickListener(this);
		mImgBioproofSel9.setOnClickListener(this);
		mImgBioproofSel10.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		fp = new FingerPrintAsync();
		Object tag = null;
		if (CommonContexts.USE_EXTERNAL_DEVICE) {
			switch (v.getId()) {
			case R.id.img_bioproof_select1:
				tag = mImgBioproofSel1.getTag();
				int id1 = tag == null ? -1 : (Integer) tag;
				if (id1 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "RHTF");
				} else if (id1 == R.drawable.imgfinger) {
					biomatricAlert("RHTF");
				}
				break;
			case R.id.img_bioproof_select2:
				tag = mImgBioproofSel2.getTag();
				int id2 = tag == null ? -1 : (Integer) tag;
				if (id2 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel2, "RHIF");
				} else if (id2 == R.drawable.imgfinger) {
					biomatricAlert("RHIF");
				}

				break;
			case R.id.img_bioproof_select3:
				tag = mImgBioproofSel3.getTag();
				int id3 = tag == null ? -1 : (Integer) tag;
				if (id3 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel3, "RHMF");
				} else if (id3 == R.drawable.imgfinger) {
					biomatricAlert("RHMF");
				}

				break;
			case R.id.img_bioproof_select4:
				tag = mImgBioproofSel4.getTag();
				int id4 = tag == null ? -1 : (Integer) tag;
				if (id4 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel4, "RHRF");
				} else if (id4 == R.drawable.imgfinger) {
					biomatricAlert("RHRF");
				}

				break;
			case R.id.img_bioproof_select5:
				tag = mImgBioproofSel5.getTag();
				int id5 = tag == null ? -1 : (Integer) tag;
				if (id5 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel5, "RHLF");
				} else if (id5 == R.drawable.imgfinger) {
					biomatricAlert("RHLF");
				}

				break;
			case R.id.img_bioproof_select6:
				tag = mImgBioproofSel6.getTag();
				int id6 = tag == null ? -1 : (Integer) tag;
				if (id6 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel6, "LHTF");
				} else if (id6 == R.drawable.imgfinger) {
					biomatricAlert("LHTF");
				}

				break;
			case R.id.img_bioproof_select7:
				tag = mImgBioproofSel7.getTag();
				int id7 = tag == null ? -1 : (Integer) tag;
				if (id7 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel7, "LHIF");
				} else if (id7 == R.drawable.imgfinger) {
					biomatricAlert("LHIF");
				}

				break;
			case R.id.img_bioproof_select8:
				tag = mImgBioproofSel8.getTag();
				int id8 = tag == null ? -1 : (Integer) tag;
				if (id8 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel8, "LHMF");
				} else if (id8 == R.drawable.imgfinger) {
					biomatricAlert("LHMF");
				}

				break;
			case R.id.img_bioproof_select9:

				tag = mImgBioproofSel9.getTag();
				int id9 = tag == null ? -1 : (Integer) tag;
				if (id9 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel9, "LHRF");
				} else if (id9 == R.drawable.imgfinger) {
					biomatricAlert("LHRF");
				}

				break;
			case R.id.img_bioproof_select10:
				tag = mImgBioproofSel10.getTag();
				int id10 = tag == null ? -1 : (Integer) tag;
				if (id10 == R.drawable.fpsselector) {
					fp.fetchBiometric(mContext, mImgBioproofSel10, "LHLF");
				} else if (id10 == R.drawable.imgfinger) {
					biomatricAlert("LHLF");
				}
				break;

			}
		} else
			Toast.makeText(mContext, "Finger print device is not configured",
					Toast.LENGTH_SHORT).show();

	}

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			Object tag = BaseActivity.mBtnLeft.getTag();
			int id = tag == null ? -1 : (Integer) tag;
			if ((id == R.drawable.back)) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC_KYC)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_ENROL_BIOMETRIC)) {
					mPager.setCurrentItem(0);
				} else if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC_KYC)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_KYC_VERIFY))
					handleBackToBioView();
			}
			break;
		case CMD_RIGHT_ACTION:
			Object tags = BaseActivity.mBtnRight.getTag();
			int ids = tags == null ? -1 : (Integer) tags;
			if ((ids == R.drawable.save)) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC_KYC)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_ENROL_BIOMETRIC)) {

					if (CommonContexts.SELECTED_ENROLEMENT != null
							&& CommonContexts.SELECTED_ENROLEMENT
									.getCustomerDocs() != null)
						onVerifyView();

				}
			}
			if ((ids == R.drawable.verify)) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC_KYC)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_KYC_VERIFY)) {

					handleSaveEnrolement();
					handleSaveBiometric();

					startActivity(new Intent(mContext, KycOnlyView.class));
					getActivity().finish();

				}
			}
			break;
		}
	}

	private void handleBackToBioView() {
		BaseActivity.mTitle.setText(R.string.Biometric);
		BaseActivity.mBtnRight.setTag(R.drawable.save);
		BaseActivity.mBtnRight.setImageResource(R.drawable.save);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		mViewGroup.removeAllViews();
		// AgendaMaster dsbData = CommonContexts.SELECTED_DSB;
		// dsbData.setAmtSettled(edtInputAmount.getText().toString());

		initLoad();
		handleBackBiometric();
		enablePagerAndIndicator(true);
		mViewGroup.addView(mView);
	}

	private void enablePagerAndIndicator(boolean status) {
		mPager.setEnabled(status);
		mTabPageIndicator.setEnabled(status);
	}

	private void handleBackBiometric() {
		if (CommonContexts.SELECTED_BIOMETRIC.getRhtfTmpltData() != null) {
			mImgBioproofSel1.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getRhifTmpltData() != null) {
			mImgBioproofSel2.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getRhmfTmpltData() != null) {
			mImgBioproofSel3.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getRhrfTmpltData() != null) {
			mImgBioproofSel4.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getRhlfTmpltData() != null) {
			mImgBioproofSel5.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getLhtfTmpltData() != null) {
			mImgBioproofSel6.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getLhifTmpltData() != null) {
			mImgBioproofSel7.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getLhmfTmpltData() != null) {
			mImgBioproofSel8.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getLhrfTmpltData() != null) {
			mImgBioproofSel9.setImageResource(R.drawable.imgfinger);
		}
		if (CommonContexts.SELECTED_BIOMETRIC.getLhlfTmpltData() != null) {
			mImgBioproofSel10.setImageResource(R.drawable.imgfinger);
		}

	}

	private void handleSaveEnrolement() {

		EnrolementDao enroldao = DaoFactory.getEnrolementDao();
		CustomerEnrolmentDetails ced = generateCustomerDetails();
		long txnstatus = enroldao.insertEnrolement(ced);
		long status = enroldao.insertEnrolDocument(ced, txnstatus);
		if (status != -1) {
			Toast.makeText(mContext,
					getResources().getString(R.string.MFB00017),
					Toast.LENGTH_SHORT).show();
			CommonContexts.SELECTED_ENROLEMENT = null;
			// EnrolementEntry.clearFields();

			// getApplicationContext().finish();
		} else {
			Toast.makeText(mContext,
					getResources().getString(R.string.MFB00018),
					Toast.LENGTH_SHORT).show();
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
		if (kycId1Proof.equals(Constants.ID_PROOF)) {
			return "2";
		} else if (kycId1Proof.equals(Constants.ADD_PROOF)) {
			return "3";
		} else if (kycId1Proof.equals(Constants.SIGN_PROOF)) {
			return "5";
		} else {
			return "";
		}
	}

	private void onVerifyView() {
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		BaseActivity.mTitle.setText(getString(R.string.kyc_verify));
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

		if (CommonContexts.SELECTED_ENROLEMENT != null
				&& CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs() != null) {
			docName1.setText(CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId1Type() != null ? CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId1Type() : "");
			docName2.setText(CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId2Type() != null ? CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId2Type() : "");
			docName3.setText(CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId3Type() != null ? CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId3Type() : "");

			docproof1.setText(CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId1Proof() != null ? "as "
					+ CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
							.getKycId1Proof() : "");
			docproof2.setText(CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId2Proof() != null ? "as "
					+ CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
							.getKycId2Proof() : "");
			docproof3.setText(CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs().getKycId3Proof() != null ? "as "
					+ CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
							.getKycId3Proof() : "");

			if (CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
					.getKycImgCustomer() != null) {
				Bitmap bmp = BitmapFactory.decodeByteArray(
						CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
								.getKycImgCustomer(), 0,
						CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
								.getKycImgCustomer().length);
				imgView1.setImageBitmap(bmp);
			}
			if (CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
					.getKycId1Img() != null) {
				Bitmap bmp1 = BitmapFactory.decodeByteArray(
						CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
								.getKycId1Img(), 0,
						CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
								.getKycId1Img().length);
				imgView2.setImageBitmap(bmp1);
			}
			if (CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
					.getKycId2Img() != null) {
				Bitmap bmp2 = BitmapFactory.decodeByteArray(
						CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
								.getKycId2Img(), 0,
						CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
								.getKycId2Img().length);
				imgView3.setImageBitmap(bmp2);
			}
			if (CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
					.getKycId3Img() != null) {
				Bitmap bmp3 = BitmapFactory.decodeByteArray(
						CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
								.getKycId3Img(), 0,
						CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs()
								.getKycId3Img().length);
				imgView4.setImageBitmap(bmp3);
			}
		}

		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);

	}

	private void handleSaveBiometric() {

		EnrolementDao dao = DaoFactory.getEnrolementDao();
		dao.insertBiometricData(CommonContexts.SELECTED_BIOMETRIC);
	}

	public void myOnKeyDown() {
		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC_KYC)
				&& CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_ENROL_BIOMETRIC)) {
			mPager.setCurrentItem(0);
		} else if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC_KYC)
				&& CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_KYC_VERIFY))
			handleBackToBioView();
		
	}

	public void biomatricAlert(final String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
		adb.setTitle("Biometric Info");
		adb.setMessage("Do you want to capture again?");
		adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (msg.equalsIgnoreCase("RHTF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "RHTF");
				} else if (msg.equalsIgnoreCase("RHIF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "RHIF");
				} else if (msg.equalsIgnoreCase("RHMF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "RHMF");
				} else if (msg.equalsIgnoreCase("RHRF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "RHRF");
				} else if (msg.equalsIgnoreCase("RHLF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "RHLF");
				} else if (msg.equalsIgnoreCase("LHTF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "LHTF");
				} else if (msg.equalsIgnoreCase("LHIF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "LHIF");
				} else if (msg.equalsIgnoreCase("LHMF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "LHMF");
				} else if (msg.equalsIgnoreCase("LHRF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "LHRF");
				} else if (msg.equalsIgnoreCase("LHLF")) {
					fp.fetchBiometric(mContext, mImgBioproofSel1, "LHLF");
				}
			}
		});
		adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		adb.show();
	}

}
