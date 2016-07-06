package com.bfsi.egalite.view.enrolment;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
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
import com.bfsi.egalite.pageindicators.CustTabPageIndicator;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.view.EnrolView;
import com.bfsi.egalite.view.R;
import com.bfsi.mfi.rest.model.CustomerDocument;
import com.bfsi.mfi.rest.model.CustomerEnrolmentDetails;

/**
 * @author Vijay EGA-MN15-000012 Captures the Biometric of new customers
 */
public class BiometricProof extends Fragment implements CommandListener,
		OnClickListener {
	private static final String VIEW_ENROL_BIOMETRIC = "BIOMETRIC";
	private Context mContext;
	private BfsiViewPager mPager;
	private static ViewGroup mViewGroup;
	private View mView;
	private LayoutInflater mLayoutInflater = null;
	private ImageView mImgBioproofSel1, mImgBioproofSel2, mImgBioproofSel3,
			mImgBioproofSel4, mImgBioproofSel5, mImgBioproofSel6,
			mImgBioproofSel7, mImgBioproofSel8, mImgBioproofSel9,
			mImgBioproofSel10;
	private FingerPrintAsync fp;
	private CheckBox mCheckNationid, mCheckGovtmentissued, mCheckDrivinglicens,
			mCheckPassport, mCheckBirthCert, mCheckAadhar, mCheckResident,
			mCheckFamilybook;
	private CustTabPageIndicator mPageIndicator;
	private static final String ENROL_VIEW_VERIFY = "enrolviewverify";

	public static BiometricProof newInstance(Context context,
			BfsiViewPager pager, CustTabPageIndicator mPageIndicat) {
		BiometricProof fragment = new BiometricProof();
		fragment.mPager = pager;
		fragment.mContext = context;
		fragment.mPageIndicator = mPageIndicat;
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
		} else {
			Toast.makeText(mContext, "Finger print device is not configured",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			Object tag = BaseActivity.mBtnLeft.getTag();
			int id = tag == null ? -1 : (Integer) tag;
			if ((id == R.drawable.back)) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_ENROL_BIOMETRIC)) {
					mPager.setCurrentItem(1);
				} else if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(ENROL_VIEW_VERIFY))
					handleBackToBioView();
				else if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC)
						&& mPager.getCurrentItem() == 2)
					mPager.setCurrentItem(1);
			}
			break;
		case CMD_RIGHT_ACTION:
			Object tags = BaseActivity.mBtnRight.getTag();
			int ids = tags == null ? -1 : (Integer) tags;
			if ((ids == R.drawable.save)) {
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC)
						&& CommonContexts.CURRENT_VIEW
								.equalsIgnoreCase(VIEW_ENROL_BIOMETRIC)) {
					if (CommonContexts.SELECTED_ENROLEMENT != null)
						onVerifyView();

				} else if (mPager.getCurrentItem() == 2
						&& CommonContexts.CURRENT_SCREEN
								.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC)) {
					if (CommonContexts.SELECTED_ENROLEMENT != null)
						onVerifyView();

				}
			} else if (ids == R.drawable.verify) {

				handleSaveEnrolement();
				handleSaveBiomatric();
				startActivity(new Intent(mContext, EnrolView.class));
				getActivity().finish();
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
		if (txnstatus != -1) {
			if (status != -1) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.MFB00033),
						Toast.LENGTH_SHORT).show();

				// MessageBuilderUtil mbu = new MessageBuilderUtil();
				// mbu.buildMessage(CommonContexts.SELECTED_ENROLEMENT.getCustomerId(),
				// loanTxn.getTxnCode(), loanTxn);

				EnrolementEntry.clearFields();
				KycProof.clearFields();
				CommonContexts.SELECTED_ENROLEMENT = null;
				// enablePagerAndIndicator(true);

			} else {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.MFB00018),
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.MFB00018),
					Toast.LENGTH_SHORT).show();
		}

	}

	private void handleSaveBiomatric() {

		EnrolementDao dao = DaoFactory.getEnrolementDao();
		int count = dao.cntBiometric(CommonContexts.SELECTED_BIOMETRIC
				.getEnrolmentId());
		if (count == 1)
			dao.updateBiometric(CommonContexts.SELECTED_BIOMETRIC);
		else if (count == 0)
			dao.insertBiometricData(CommonContexts.SELECTED_BIOMETRIC);
	}

	private CustomerEnrolmentDetails generateCustomerDetails() {
		CustomerEnrolmentDetails ced = new CustomerEnrolmentDetails();
		Enrolement enrol = CommonContexts.SELECTED_ENROLEMENT;
		ced.setEnrolmentId(enrol.getEnrollmentId());
		ced.setModuleCode("CE");
		ced.setTxnCode("111");
		ced.setAgentId(CommonContexts.mLOGINVALIDATION.getAgentId());
		ced.setDeviceId(CommonContexts.mLOGINVALIDATION.getDeviceId());
		ced.setLocCode(CommonContexts.mLOGINVALIDATION.getLocationCode());
		ced.setWorkFlowQueType(enrol.getWorkflowQType());

		CustomerDocuments cd = enrol.getCustomerDocs();
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
		ced.setFirstName(enrol.getFirstName());
		ced.setLastName(enrol.getLastName());
		ced.setMiddleName(enrol.getMiddleName());
		ced.setDob(enrol.getDob());
		ced.setGender(EnrolmentDataAccess.readIntLovValue(enrol.getGender(),
				EnrolmentDataAccess.GDR01));
		ced.setResidenceType(enrol.getResidenceType());

		if (enrol.getResidenceType().equalsIgnoreCase("Resident")) {
			ced.setNationality(EnrolmentDataAccess.readIntLovValue(
					enrol.getCountry(), EnrolmentDataAccess.CN001));
		} else {
			ced.setNationality(EnrolmentDataAccess.readIntLovValue(
					enrol.getNationality(), EnrolmentDataAccess.NAT01));
		}

		ced.setAddress1(enrol.getAddress1());
		ced.setAddress2(enrol.getAddress2());
		ced.setAddress3(enrol.getAddress3());
		ced.setAddress4(enrol.getAddress4());
		ced.setCity(enrol.getCity());
		ced.setState(enrol.getState());
		ced.setZipCode(enrol.getZipCode());
		ced.setCountry(EnrolmentDataAccess.readIntLovValue(enrol.getCountry(),
				EnrolmentDataAccess.CN001));
		ced.setEmail(enrol.getEmailId());
		ced.setContactNo(enrol.getContactNo());
		ced.setMaritalStatus(EnrolmentDataAccess.readIntLovValue(
				enrol.getMartialStatus(), EnrolmentDataAccess.MTS01));
		ced.setProfession(EnrolmentDataAccess.readIntLovValue(
				enrol.getProfession(), EnrolmentDataAccess.PRO01));
		ced.setProfessionRemark(enrol.getProfessionRemark());
		ced.setAccountCategory(enrol.getAccountCategory());
		ced.setAccType(enrol.getAccountType());
		ced.setAccCurrency(EnrolmentDataAccess.readIntLovValue(
				enrol.getCurrency(), EnrolmentDataAccess.CCY01));
		ced.setIsActive(enrol.getIsActive());

		ced.setTxnInitTime(DateUtil.getCurrentDataTime());
		ced.setTxnStatus(0);
		ced.setTxnErrorCode(enrol.getTxnErrorCode());
		ced.setTxnErrorMsg(enrol.getTxnErrorMsg());
		ced.setGroupIndlType(enrol.getGroupIndiviType() == null ? "I" : enrol
				.getGroupIndiviType());
		if (CommonContexts.GROUP.equalsIgnoreCase("GRP"))
			// ced.setTmpGrpId(mGroupSpinner.getSelectedItem().toString());
			ced.setTmpGrpId(enrol.getTempGroupId());

		else
			ced.setTmpGrpId(null);

		ced.setIsKycOnly("N");
		ced.setCustomerId(enrol.getCustomerId());
		ced.setPoc(CommonContexts.poc == null ? "N" : CommonContexts.poc);

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
		BaseActivity.mBtnRight.setVisibility(View.VISIBLE);
		BaseActivity.mBtnRight.setImageResource(R.drawable.verify);
		BaseActivity.mBtnRight.setTag(R.drawable.verify);
		BaseActivity.mBtnLeft.setImageResource(R.drawable.back);
		BaseActivity.mBtnLeft.setTag(R.drawable.back);
		BaseActivity.mTitle.setText(R.string.personal_info_verify);
		CommonContexts.CURRENT_VIEW = ENROL_VIEW_VERIFY;
		mView = mLayoutInflater.inflate(R.layout.enrolement_verify, null);
		TextView mtxv_firstname = (TextView) mView
				.findViewById(R.id.id_verify_firstname_val);
		TextView mtxv_middlename = (TextView) mView
				.findViewById(R.id.id_verify_middlename_val);
		TextView mtxv_lastname = (TextView) mView
				.findViewById(R.id.id_verify_lastname_val);
		TextView mtxv_gender = (TextView) mView
				.findViewById(R.id.id_verify_gender_val);
		TextView mtxv_dob = (TextView) mView
				.findViewById(R.id.id_verify_dob_val);
		TextView mtxv_residentStatus = (TextView) mView
				.findViewById(R.id.id_verify_residental_val);
		TextView mtxv_nationality = (TextView) mView
				.findViewById(R.id.id_verify_nationality_val);
		TextView mtxv_address1 = (TextView) mView
				.findViewById(R.id.id_verify_address1_val);
		TextView mtxv_address2 = (TextView) mView
				.findViewById(R.id.id_verify_address2_val);
		TextView mtxv_address3 = (TextView) mView
				.findViewById(R.id.id_verify_address3_val);
		TextView mtxv_address4 = (TextView) mView
				.findViewById(R.id.id_verify_address4_val);
		TextView mtxv_state = (TextView) mView
				.findViewById(R.id.id_verify_state_val);
		TextView mtxv_country = (TextView) mView
				.findViewById(R.id.id_verify_country_val);
		TextView mtxv_zipcode = (TextView) mView
				.findViewById(R.id.id_verify_zip_val);
		TextView mtxv_email = (TextView) mView
				.findViewById(R.id.id_verify_email_val);
		TextView mtxv_contactno = (TextView) mView
				.findViewById(R.id.id_verify_contactno_val);
		TextView mtxv_maritalstatus = (TextView) mView
				.findViewById(R.id.id_verify_marital_val);
		TextView mtxv_profession = (TextView) mView
				.findViewById(R.id.id_verify_profession_val);
		TextView mtxv_remarks = (TextView) mView
				.findViewById(R.id.id_verify_remarks_val);
		TextView mtxv_acctype = (TextView) mView
				.findViewById(R.id.id_verify_acctype_val);
		TextView mtxv_groupid = (TextView) mView
				.findViewById(R.id.id_verify_groupid_val);
		TextView mtxv_poc = (TextView) mView
				.findViewById(R.id.id_verify_poc_val);
		TableRow mtablNationality = (TableRow) mView
				.findViewById(R.id.tabrow_nationality);
		TableRow mtablGroupid = (TableRow) mView
				.findViewById(R.id.tabrow_groupid);
		TableRow mtablPoc = (TableRow) mView.findViewById(R.id.tabrow_poc);

		TableRow mtablAddress2 = (TableRow) mView
				.findViewById(R.id.tablerow_vfy_address2);
		TableRow mtablAddress3 = (TableRow) mView
				.findViewById(R.id.tablerow_vfy_address3);
		TableRow mtablAddress4 = (TableRow) mView
				.findViewById(R.id.tablerow_vfy_address4);

		// Account Info

		mCheckNationid = (CheckBox) mView.findViewById(R.id.chek_nationalid);
		mCheckGovtmentissued = (CheckBox) mView
				.findViewById(R.id.chek_govtIssuedId);
		mCheckDrivinglicens = (CheckBox) mView
				.findViewById(R.id.chek_drivinglicens);
		mCheckPassport = (CheckBox) mView.findViewById(R.id.chek_passport);
		mCheckBirthCert = (CheckBox) mView
				.findViewById(R.id.chek_birthcertificate);
		mCheckAadhar = (CheckBox) mView.findViewById(R.id.chek_aadhar);
		mCheckResident = (CheckBox) mView.findViewById(R.id.chek_resident);
		mCheckFamilybook = (CheckBox) mView.findViewById(R.id.chek_familybook);

		if (CommonContexts.SELECTED_ENROLEMENT != null) {
			mtxv_firstname.setText(CommonContexts.SELECTED_ENROLEMENT
					.getFirstName());
			mtxv_lastname.setText(CommonContexts.SELECTED_ENROLEMENT
					.getLastName());
			mtxv_middlename.setText(CommonContexts.SELECTED_ENROLEMENT
					.getMiddleName());
			mtxv_gender.setText(CommonContexts.SELECTED_ENROLEMENT.getGender());
			mtxv_contactno.setText(CommonContexts.SELECTED_ENROLEMENT
					.getContactNo());
			mtxv_dob.setText(CommonContexts.dateFormat.format(new Date(Long
					.valueOf(CommonContexts.SELECTED_ENROLEMENT.getDob()))));

			mtxv_residentStatus.setText(CommonContexts.SELECTED_ENROLEMENT
					.getResidenceType().equalsIgnoreCase("R") ? "Resident"
					: "Non Resident");
			mtxv_nationality.setText(CommonContexts.SELECTED_ENROLEMENT
					.getNationality());
			mtablNationality.setVisibility(View.GONE);
			if (!(CommonContexts.SELECTED_ENROLEMENT.getResidenceType()
					.equalsIgnoreCase("R"))) {
				mtablNationality.setVisibility(View.VISIBLE);
			}
			mtxv_address1.setText(CommonContexts.SELECTED_ENROLEMENT
					.getAddress1());
			mtxv_address2.setText(CommonContexts.SELECTED_ENROLEMENT
					.getAddress2());
			mtxv_address3.setText(CommonContexts.SELECTED_ENROLEMENT
					.getAddress3());
			mtxv_address4.setText(CommonContexts.SELECTED_ENROLEMENT
					.getAddress4());
			mtablAddress2.setVisibility(View.GONE);
			if (CommonContexts.SELECTED_ENROLEMENT.getAddress2() != null
					&& !(CommonContexts.SELECTED_ENROLEMENT.getAddress2()
							.equals(""))) {
				mtablAddress2.setVisibility(View.VISIBLE);
			}
			mtablAddress3.setVisibility(View.GONE);
			if (CommonContexts.SELECTED_ENROLEMENT.getAddress3() != null
					&& !(CommonContexts.SELECTED_ENROLEMENT.getAddress3()
							.equals(""))) {
				mtablAddress3.setVisibility(View.VISIBLE);
			}
			mtablAddress4.setVisibility(View.GONE);
			if (CommonContexts.SELECTED_ENROLEMENT.getAddress4() != null
					&& !(CommonContexts.SELECTED_ENROLEMENT.getAddress4()
							.equals(""))) {
				mtablAddress4.setVisibility(View.VISIBLE);
			}
			mtxv_state.setText(CommonContexts.SELECTED_ENROLEMENT.getState());
			mtxv_country.setText(CommonContexts.SELECTED_ENROLEMENT
					.getCountry());
			mtxv_zipcode.setText(CommonContexts.SELECTED_ENROLEMENT
					.getZipCode());
			mtxv_email.setText(CommonContexts.SELECTED_ENROLEMENT.getEmailId());
			mtxv_maritalstatus.setText(CommonContexts.SELECTED_ENROLEMENT
					.getMartialStatus());
			mtxv_profession.setText(CommonContexts.SELECTED_ENROLEMENT
					.getProfession());
			mtxv_remarks.setText(CommonContexts.SELECTED_ENROLEMENT
					.getProfessionRemark());

			mtablGroupid.setVisibility(View.GONE);
			mtablPoc.setVisibility(View.GONE);

			if (CommonContexts.SELECTED_ENROLEMENT.getGroupIndiviType() != null
					&& CommonContexts.SELECTED_ENROLEMENT.getGroupIndiviType()
							.equalsIgnoreCase("G")) {
				mtablGroupid.setVisibility(View.VISIBLE);
				mtablPoc.setVisibility(View.VISIBLE);

			} else {
				CommonContexts.SELECTED_ENROLEMENT.setGroupIndiviType("I");
			}

			if (CommonContexts.SELECTED_ENROLEMENT.getGroupIndiviType() != null)
				mtxv_acctype.setText(CommonContexts.SELECTED_ENROLEMENT
						.getGroupIndiviType().equalsIgnoreCase("G") ? "Group"
						: "Individual");
			if (CommonContexts.SELECTED_ENROLEMENT.getTempGroupId() != null)
				mtxv_groupid.setText(CommonContexts.SELECTED_ENROLEMENT
						.getTempGroupId());
			if (CommonContexts.SELECTED_ENROLEMENT.getPoc() != null)
				mtxv_poc.setText(CommonContexts.SELECTED_ENROLEMENT.getPoc()
						.equalsIgnoreCase("Y") ? "Yes" : "No");
		}

		setChekState();
		// enablePagerAndIndicator(false);
		mViewGroup.removeAllViews();
		mViewGroup.addView(mView);
	}

	private void enablePagerAndIndicator(boolean status) {
		mPager.setEnabled(status);
		mPageIndicator.setEnabled(status);
	}

	private void setChekState() {
		if (CommonContexts.SELECTEDPROOFTYPE
				.equalsIgnoreCase(Constants.National_ID)
				|| CommonContexts.SELECTEDPROOFTYPE2
						.equals(Constants.National_ID)
				|| CommonContexts.SELECTEDPROOFTYPE3
						.equals(Constants.National_ID)) {
			mCheckNationid.setChecked(true);
		}
		if (CommonContexts.SELECTEDPROOFTYPE
				.equalsIgnoreCase(Constants.PASSPORT)
				|| CommonContexts.SELECTEDPROOFTYPE2
						.equalsIgnoreCase(Constants.PASSPORT)
				|| CommonContexts.SELECTEDPROOFTYPE3
						.equalsIgnoreCase(Constants.PASSPORT)) {
			mCheckPassport.setChecked(true);
		}
		if (CommonContexts.SELECTEDPROOFTYPE
				.equalsIgnoreCase(Constants.DRIVING_LIC)
				|| CommonContexts.SELECTEDPROOFTYPE2
						.equalsIgnoreCase(Constants.DRIVING_LIC)
				|| CommonContexts.SELECTEDPROOFTYPE3
						.equalsIgnoreCase(Constants.DRIVING_LIC)) {
			mCheckDrivinglicens.setChecked(true);
		}
		if (CommonContexts.SELECTEDPROOFTYPE
				.equalsIgnoreCase(Constants.Govt_ID)
				|| CommonContexts.SELECTEDPROOFTYPE2
						.equalsIgnoreCase(Constants.Govt_ID)
				|| CommonContexts.SELECTEDPROOFTYPE3
						.equalsIgnoreCase(Constants.Govt_ID)) {
			mCheckGovtmentissued.setChecked(true);
		}
		if (CommonContexts.SELECTEDPROOFTYPE
				.equalsIgnoreCase(Constants.BIRTH_CERT)
				|| CommonContexts.SELECTEDPROOFTYPE2
						.equalsIgnoreCase(Constants.BIRTH_CERT)
				|| CommonContexts.SELECTEDPROOFTYPE3
						.equalsIgnoreCase(Constants.BIRTH_CERT)) {
			mCheckBirthCert.setChecked(true);
		}
		if (CommonContexts.SELECTEDPROOFTYPE
				.equalsIgnoreCase(Constants.ADHAR_CRD)
				|| CommonContexts.SELECTEDPROOFTYPE2
						.equalsIgnoreCase(Constants.ADHAR_CRD)
				|| CommonContexts.SELECTEDPROOFTYPE3
						.equalsIgnoreCase(Constants.ADHAR_CRD)) {
			mCheckAadhar.setChecked(true);
		}

		if (CommonContexts.SELECTEDPROOFTYPE
				.equalsIgnoreCase(Constants.RESIDENT_BOOK)
				|| CommonContexts.SELECTEDPROOFTYPE2
						.equalsIgnoreCase(Constants.RESIDENT_BOOK)
				|| CommonContexts.SELECTEDPROOFTYPE3
						.equalsIgnoreCase(Constants.RESIDENT_BOOK)) {
			mCheckResident.setChecked(true);
		}
		if (CommonContexts.SELECTEDPROOFTYPE
				.equalsIgnoreCase(Constants.FAMILY_BOOK)
				|| CommonContexts.SELECTEDPROOFTYPE2
						.equalsIgnoreCase(Constants.FAMILY_BOOK)
				|| CommonContexts.SELECTEDPROOFTYPE3
						.equalsIgnoreCase(Constants.FAMILY_BOOK)) {
			mCheckFamilybook.setChecked(true);
		}

	}

	public void myOnKeyDown() {

		if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC)
				&& CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(VIEW_ENROL_BIOMETRIC)) {
			mPager.setCurrentItem(1);
		} else if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC)
				&& CommonContexts.CURRENT_VIEW
						.equalsIgnoreCase(ENROL_VIEW_VERIFY))
			handleBackToBioView();
		else if (CommonContexts.CURRENT_SCREEN
				.equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC)
				&& mPager.getCurrentItem() == 2)
			mPager.setCurrentItem(1);

		/*
		 * 
		 * if (CommonContexts.CURRENT_SCREEN
		 * .equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC) &&
		 * CommonContexts.CURRENT_VIEW .equalsIgnoreCase(VIEW_ENROL_BIOMETRIC))
		 * { mPager.setCurrentItem(1); } else if (CommonContexts.CURRENT_SCREEN
		 * .equalsIgnoreCase(CommonContexts.SCREEN_BIOMETRIC) &&
		 * CommonContexts.CURRENT_VIEW .equalsIgnoreCase(ENROL_VIEW_VERIFY)) {
		 * handleBackToBioView(); }
		 */
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
