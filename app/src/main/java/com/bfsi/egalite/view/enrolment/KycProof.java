package com.bfsi.egalite.view.enrolment;

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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

import com.bfsi.egalite.dao.sqlite.EnrolmentDataAccess;
import com.bfsi.egalite.entity.CustomerDocuments;
import com.bfsi.egalite.entity.Enrolement;
import com.bfsi.egalite.listeners.CommandListener;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.pageindicators.BfsiViewPager;
import com.bfsi.egalite.pageindicators.CustTabPageIndicator;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.ViewUtil;
import com.bfsi.egalite.view.R;

/**
 * @author Vijay EGA-MN15-000012 Captures the KYC information of new customer as
 *         part of customer enrollment.
 * 
 */
public class KycProof extends Fragment implements CommandListener {

	private static final String VIEW_ENROL_KYC = "enrolkyc";
	private static final String SIGNATURE_PROOF = "Signature Proof";
	private static final String ADDRESS_PROOF = "Address Proof";
	private static final String ID_PROOF = "Id Proof";
	private Context mContext;
	private BfsiViewPager mPager;
	private static ImageView img_photo, img_photo_select1, img_photo_select2,
			img_photo_select3;
	private static Spinner spinner_select1, spinner_select2, spinner_select3;
	private String selectedvalue1, selectedvalue2, selectedvalue3;
	private static ViewGroup mViewGroup;
	private static EditText mKycNo1, mKycNo2, mKycNo3;
	private View mView;
	private LayoutInflater mLayoutInflater = null;
	private static int imageType = 0;
	private int selectedId1, selectedId2, selectedId3;
	private String selectedRadio1, selectedRadio2, selectedRadio3;
	private List<String> documentTags1, documentTags2, documentTags3;
	private static RadioButton mRadidProof1, mRadAddresProof1, mRadSignProof1,
			mRadidProof2, mRadAddresProof2, mRadSignProof2, mRadidProof3,
			mRadAddresProof3, mRadSignProof3;
	private Button mBtnNext;

	public static KycProof newInstance(Context context, BfsiViewPager pager,
			CustTabPageIndicator mPageIndicat) {
		KycProof fragment = new KycProof();
		fragment.mPager = pager;
		fragment.mContext = context;

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
		if (mPager.getCurrentItem() == 0)
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
		CommonContexts.CURRENT_VIEW = VIEW_ENROL_KYC;
		mView = mLayoutInflater.inflate(R.layout.enrolment_kycproof, null);

		img_photo = (ImageView) mView.findViewById(R.id.img_kycproof_photo);
		img_photo_select1 = (ImageView) mView
				.findViewById(R.id.img_kycproof_selct1);
		img_photo_select2 = (ImageView) mView
				.findViewById(R.id.img_kycproof_selct2);
		img_photo_select3 = (ImageView) mView
				.findViewById(R.id.img_kycproof_selct3);

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

		mBtnNext = (Button) mView.findViewById(R.id.enrol_newform_next);
		mBtnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleSave();
			}
		});

		addSpinners();
		radioBtnCheck();
		@SuppressWarnings("unused")
		Typeface tFace = CommonContexts.getTypeface(mContext);

		if (CommonContexts.SELECTED_ENROLEMENT != null
				&& CommonContexts.SELECTED_ENROLEMENT.getCustomerDocs() != null) {

			CustomerDocuments endocs = CommonContexts.SELECTED_ENROLEMENT
					.getCustomerDocs();

			Bitmap bmp = BitmapFactory.decodeByteArray(
					endocs.getKycImgCustomer(), 0,
					endocs.getKycImgCustomer().length);

			Bitmap bmp1 = BitmapFactory.decodeByteArray(endocs.getKycId1Img(),
					0, endocs.getKycId1Img().length);

			Bitmap bmp2 = BitmapFactory.decodeByteArray(endocs.getKycId2Img(),
					0, endocs.getKycId2Img().length);

			Bitmap bmp3 = BitmapFactory.decodeByteArray(endocs.getKycId3Img(),
					0, endocs.getKycId3Img().length);

			img_photo.setTag(bmp);
			img_photo.setImageBitmap(bmp);

			img_photo_select1.setTag(bmp1);
			img_photo_select1.setImageBitmap(bmp1);
			String documentName1 = endocs.getKycId1Number();
			setSpinner(documentName1, 1);

			img_photo_select2.setTag(bmp2);
			img_photo_select2.setImageBitmap(bmp2);
			String documentName2 = endocs.getKycId2Number();
			setSpinner(documentName2, 2);

			img_photo_select3.setTag(bmp3);
			img_photo_select3.setImageBitmap(bmp3);
			String documentName3 = endocs.getKycId3Number();
			setSpinner(documentName3, 3);

		}

		initialiseListener();

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
		if (selectedRadio1 != null) {
			if (selectedRadio1.equalsIgnoreCase(ID_PROOF)) {
				mRadidProof1.setChecked(true);
			} else if (selectedRadio1.equalsIgnoreCase(ADDRESS_PROOF)) {
				mRadAddresProof1.setChecked(true);

			} else if (selectedRadio1.equalsIgnoreCase(SIGNATURE_PROOF)) {
				mRadSignProof1.setChecked(true);
			}
		}

		if (selectedRadio2 != null) {

			if (selectedRadio2.equalsIgnoreCase(ID_PROOF)) {
				mRadidProof2.setChecked(true);
			} else if (selectedRadio2.equalsIgnoreCase(ADDRESS_PROOF)) {
				mRadAddresProof2.setChecked(true);

			} else if (selectedRadio2.equalsIgnoreCase(SIGNATURE_PROOF)) {
				mRadSignProof2.setChecked(true);
			}
		}

		if (selectedRadio3 != null) {

			if (selectedRadio3.equalsIgnoreCase(ID_PROOF)) {
				mRadidProof3.setChecked(true);
			} else if (selectedRadio3.equalsIgnoreCase(ADDRESS_PROOF)) {
				mRadAddresProof3.setChecked(true);

			} else if (selectedRadio3.equalsIgnoreCase(SIGNATURE_PROOF)) {
				mRadSignProof3.setChecked(true);
			}
		}
	}

	private void setSpinner(String documentName, int position) {

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

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == getActivity().RESULT_OK) {
			if (data != null) {
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				setImage(bitmap);

				// Log.v("BITMAPSIZE>>>>>>>",
				// String.valueOf(bitmap.getByteCount()));
				// ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				// byte[] imageInByte = stream.toByteArray();
				// Bitmap bitmap2 = BitmapFactory.decodeByteArray(imageInByte,
				// 0,
				// imageInByte.length);
				// Log.v("BITMAPSIZE>>>>>>>",
				// String.valueOf(bitmap2.getByteCount()));
				// // long lengthbmp = imageInByte.length;
				// Log.v("ASS1>>>>>>>", String.valueOf(stream.size() / 1024));
				// Log.v("ASS2>>>>>>>", String.valueOf(imageInByte.length /
				// 1024));

			}
		}
	}

	private void setImage(Bitmap bitmap) {
		if (imageType == 1) {
			img_photo.setTag(bitmap);
			img_photo.setImageBitmap(compressBitmap(bitmap));
		} else if (imageType == 2) {
			img_photo_select1.setTag(bitmap);
			img_photo_select1.setImageBitmap(compressBitmap(bitmap));
		} else if (imageType == 3) {
			img_photo_select2.setTag(bitmap);
			img_photo_select2.setImageBitmap(compressBitmap(bitmap));
		} else if (imageType == 4) {
			img_photo_select3.setTag(bitmap);
			img_photo_select3.setImageBitmap(compressBitmap(bitmap));
		}
	}

	private Bitmap compressBitmap(Bitmap bitmap) {
		return codec(bitmap, Bitmap.CompressFormat.JPEG, 80);
	}

	private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
			int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);

		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	@Override
	public void update(int command) {
		switch (command) {
		case CMD_LEFT_ACTION:
			/*Object tag = BaseActivity.mBtnLeft.getTag();
			int id = tag == null ? -1 : (Integer) tag;
			if (id == R.drawable.home) {
				
				if (CommonContexts.CURRENT_SCREEN
						.equalsIgnoreCase(CommonContexts.SCREEN_KYC)&& CommonContexts.CURRENT_VIEW.equalsIgnoreCase(VIEW_ENROL_KYC)) {
					clearFields();
						startActivity(new Intent(mContext, HomeView.class));
						getActivity().finish();
				}
			}*/
			break;

		case CMD_RIGHT_ACTION:
			Object tags = BaseActivity.mBtnRight.getTag();
			int ids = tags == null ? -1 : (Integer) tags;
			if (ids == R.drawable.save) {
				if (mPager.getCurrentItem() == 0) {
					// handleSave();
				}
			}
			break;
		}
	}

	public static void clearFields() {
		if(CommonContexts.SELECTED_ENROLEMENT != null)
		{
			mKycNo1.setText("");
			mKycNo2.setText("");
			mKycNo3.setText("");
	
			spinner_select1.setSelection(0);
			spinner_select2.setSelection(0);
			spinner_select3.setSelection(0);
	
			mRadidProof1.setChecked(false);
			mRadAddresProof1.setChecked(false);
			mRadSignProof1.setChecked(false);
			mRadidProof2.setChecked(false);
			mRadAddresProof2.setChecked(false);
			mRadSignProof2.setChecked(false);
			mRadidProof3.setChecked(false);
			mRadAddresProof3.setChecked(false);
			mRadSignProof3.setChecked(false);
			img_photo.setImageResource(R.drawable.cameralight);
			img_photo_select1.setImageResource(R.drawable.cameralight);
			img_photo_select2.setImageResource(R.drawable.cameralight);
			img_photo_select3.setImageResource(R.drawable.cameralight);
	
			CommonContexts.SELECTED_ENROLEMENT = null;
		}
	}

	private void handleSave() {
		String spinnerSelect1 = spinner_select1.getSelectedItem().toString();
		String spinnerSelect2 = spinner_select2.getSelectedItem().toString();
		String spinnerSelect3 = spinner_select3.getSelectedItem().toString();

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

		if (!(selectedId1 == -1 && selectedId1 == -1 && selectedId1 == -1)) {
			radioSelect();
		}
		// photoValidate();
		if (!photoValidate()) {
			ViewUtil.showCrutonError(mContext, R.string.messg_photo, handler);
		} else if (!(documentTags1.size() > 0) || !(documentTags2.size() > 0)
				|| !(documentTags3.size() > 0)) {

			ViewUtil.showCrutonError(mContext, R.string.messg_proof, handler);
		} else if (spinnerSelect1.equalsIgnoreCase(Constants.SELECT)
				|| spinnerSelect2.equalsIgnoreCase(Constants.SELECT)
				|| spinnerSelect3.equalsIgnoreCase(Constants.SELECT)) {

			ViewUtil.showCrutonError(mContext, R.string.messg_idproof, handler);
		} else if (documentTags1.equals(Constants.SELECT)
				|| documentTags2.equals(Constants.SELECT)
				|| documentTags3.equals(Constants.SELECT)) {
			ViewUtil.showCrutonError(mContext, R.string.messg_idproof, handler);
		} else if (numSame(documentTags1, documentTags2)
				|| numSame(documentTags1, documentTags3)
				|| numSame(documentTags2, documentTags3)) {
			ViewUtil.showCrutonError(mContext, R.string.messg_idproofdublicate,
					handler);
		} else if (selectedId1 == -1 && selectedId1 == -1 && selectedId1 == -1) {
			ViewUtil.showCrutonError(mContext, R.string.messg_idproof, handler);
		} /*
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
		 */
		/*
		 * else if (mKycNo1.getText().toString().isEmpty() ||
		 * mKycNo2.getText().toString().isEmpty() ||
		 * mKycNo3.getText().toString().isEmpty()) {
		 * ViewUtil.showCrutonError(mContext, R.string.messg_cardnumber,
		 * handler); }
		 */else {

			CustomerDocuments docs = new CustomerDocuments();
			docs.setKycImgCustomer(getBitmap(phot_bitmap));
			docs.setKycId1Img(getBitmap(phot_bitmap1));
			docs.setKycId1Number(mKycNo1.getText() != null ? mKycNo1.getText()
					.toString() : "");
			docs.setKycId1Proof(selectedRadio1);
			docs.setKycId1Type(selectedvalue1);
			docs.setKycId2Img(getBitmap(phot_bitmap2));
			docs.setKycId2Number(mKycNo2.getText() != null ? mKycNo2.getText()
					.toString() : "");
			docs.setKycId2Proof(selectedRadio2);
			docs.setKycId2Type(selectedvalue2);
			docs.setKycId3Img(getBitmap(phot_bitmap3));
			docs.setKycId3Number(mKycNo3.getText() != null ? mKycNo3.getText()
					.toString() : "");
			docs.setKycId3Proof(selectedRadio3);
			docs.setKycId3Type(selectedvalue3);

			if (CommonContexts.SELECTED_ENROLEMENT != null) {
				CommonContexts.SELECTED_ENROLEMENT.setCustomerDocs(docs);
			} else {
				CommonContexts.SELECTED_ENROLEMENT = new Enrolement();
				CommonContexts.SELECTED_ENROLEMENT.setCustomerDocs(docs);
			}

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

	private void radioSelect() {

		RadioButton mradioBtn1 = (RadioButton) mView.findViewById(selectedId1);
		if (mradioBtn1 != null) {
			selectedRadio1 = (String) mradioBtn1.getText();
			documentTags1.add(selectedRadio1);
		}
		RadioButton mradioBtn2 = (RadioButton) mView.findViewById(selectedId2);
		if (mradioBtn2 != null) {
			selectedRadio2 = (String) mradioBtn2.getText();
			documentTags2.add(selectedRadio2);
		}
		RadioButton mradioBtn3 = (RadioButton) mView.findViewById(selectedId3);
		if (mradioBtn3 != null) {
			selectedRadio3 = (String) mradioBtn3.getText();
			documentTags3.add(selectedRadio3);
		}

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
			/*clearFields();
			startActivity(new Intent(mContext, HomeView.class));
			getActivity().finish();*/

		}
	}
}
