package com.bfsi.egalite.view;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.LoansDao;
import com.bfsi.egalite.main.BaseActivity;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.view.customers.CustomerQuery;
import com.bfsi.egalite.view.customers.CustomersAll;
import com.bfsi.egalite.view.setting.SettingView;
import com.bfsi.egalite.view.setting.SyncViewL;

public class HomeView extends BaseActivity {

	List<String> mList;
	private Integer[] mThumbIds;
	private String[] mThumbNames;

	// private GestureDetector gestureDetector;
	// View.OnTouchListener gestureListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		LoadValues(HomeView.this);
		LoansDao dsbDao = DaoFactory.getLoanDao();
		mList = dsbDao.agentControlAccess();
		GridView gridview = (GridView) findViewById(R.id.grid);
		gridview.setAdapter(new HomeImageAdapter(HomeView.this, mThumbIds,
				mThumbNames));


	}

	@Override
	public void onResume() {
		super.onResume();

	}


	private void LoadValues(Context c) {
		mThumbIds = new Integer[] { R.drawable.imgdashbord,
				R.drawable.imgloandsb, R.drawable.imgloanrepay,
				R.drawable.imgdepocollection, R.drawable.imgdepopayment,
				R.drawable.imgcash, R.drawable.imgsync, R.drawable.imgcustomer,
				R.drawable.imghistory, R.drawable.imgsettings,
				R.drawable.imgcustreq, R.drawable.imgcustenroll,
				R.drawable.imgcustagenda, R.drawable.imgkycinfo,
				R.drawable.imgloanprepay };
		mThumbNames = c.getResources().getStringArray(R.array.gridArray);

	}

//	public void destroyItem(ViewGroup viewPager, int position, Object object) {
//		viewPager.removeViewAt(position);
//	}

	private class HomeImageAdapter extends BaseAdapter {
		private Context mContext;
		private LayoutInflater mLayoutInflater;
		LinearLayout linearlayout;
		private Integer[] mThumbIds;
		private String[] mThumbNames;
		/*
		 * String[] copyVal; Integer[] copyImages; int[] index = { 0, 2, 1 };
		 */

		// Constructor
		public HomeImageAdapter(Context c, Integer[] thumbIds, String[] thumbNames) {
			super();
			mContext = c;
			mLayoutInflater = LayoutInflater.from(mContext);
			mThumbIds=thumbIds;
			mThumbNames=thumbNames;
			// imageData();
		}

		public int getCount() {

			return mThumbNames.length;

			// return copyVal.length;
		}

		public Object getItem(int position) {
			return mThumbNames.length;

		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			View gridView;
			ViewHolder holder = null;

			if (convertView == null) {
				gridView = new View(mContext);
				gridView = mLayoutInflater.inflate(R.layout.row_grid, null);
				gridView.setTag(holder);
			} else {
				gridView = convertView;
			}
			holder = new ViewHolder();
			holder.imageView = (ImageView) gridView.findViewById(R.id.image);
			holder.textView = (TextView) gridView.findViewById(R.id.text);
			linearlayout = (LinearLayout) gridView.findViewById(R.id.grid_view);
			holder.imageView.setImageResource(mThumbIds[position]);
			holder.textView.setText(mThumbNames[position]);
			holder.textView.setOnClickListener(null);
			linearlayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
				
					switch (position) {
					default:
					
					case 0:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						mContext.startActivity(new Intent(mContext,
								DashboardView.class));
						HomeView.this.finish();
						break;
					case 1:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("LD01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, "No Access",
									Toast.LENGTH_SHORT).show();

						} else {
							CommonContexts.ISDBVISITED = true;
							CommonContexts.ISREPAYVISITED = false;
							CommonContexts.ISCOLLECTIONVISITED = false;
							CommonContexts.ISPAYMENTVISITED = false;
							CommonContexts.ISENROLVISITED = false;
							CommonContexts.ISCUSTAGENDA = false;
							CommonContexts.ISLOANPREPAYSELECTED = false;
							CommonContexts.ISKYCVIEW = false;
							mContext.startActivity(new Intent(mContext,
									LoanDsbView.class));
							HomeView.this.finish();
						}

						break;
					case 2:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("LR01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							CommonContexts.ISDBVISITED = false;
							CommonContexts.ISREPAYVISITED = true;
							CommonContexts.ISCOLLECTIONVISITED = false;
							CommonContexts.ISPAYMENTVISITED = false;
							CommonContexts.ISENROLVISITED = false;
							CommonContexts.ISCUSTAGENDA = false;
							CommonContexts.ISLOANPREPAYSELECTED = false;
							CommonContexts.ISKYCVIEW = false;
							mContext.startActivity(new Intent(mContext,
									LoanRepayView.class));
							HomeView.this.finish();
						}
						break;
					case 3:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("DC01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							CommonContexts.ISDBVISITED = false;
							CommonContexts.ISREPAYVISITED = false;
							CommonContexts.ISCOLLECTIONVISITED = true;
							CommonContexts.ISPAYMENTVISITED = false;
							CommonContexts.ISENROLVISITED = false;
							CommonContexts.ISCUSTAGENDA = false;
							CommonContexts.ISLOANPREPAYSELECTED = false;
							CommonContexts.ISKYCVIEW = false;
							mContext.startActivity(new Intent(mContext,
									DpCollectionView.class));
							HomeView.this.finish();
						}
						break;
					case 4:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("DP01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							CommonContexts.ISDBVISITED = false;
							CommonContexts.ISREPAYVISITED = false;
							CommonContexts.ISCOLLECTIONVISITED = false;
							CommonContexts.ISPAYMENTVISITED = true;
							CommonContexts.ISENROLVISITED = false;
							CommonContexts.ISCUSTAGENDA = false;
							CommonContexts.ISLOANPREPAYSELECTED = false;
							CommonContexts.ISKYCVIEW = false;
							mContext.startActivity(new Intent(mContext,
									DpPaymentView.class));
							HomeView.this.finish();
						}
						break;
					case 5:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("CA01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							mContext.startActivity(new Intent(mContext,
									CustomerQuery.class));

							CommonContexts.MODULE = "CASH";
							HomeView.this.finish();
						}
						break;
					case 6:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						mContext.startActivity(new Intent(mContext,
								SyncViewL.class));
						HomeView.this.finish();
						break;
					case 7:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						mContext.startActivity(new Intent(mContext,
								CustomersAll.class));

						CommonContexts.MODULE = "CUSTOMERALL";
						HomeView.this.finish();
						break;
					case 8:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						mContext.startActivity(new Intent(mContext,
								HistoryView.class));
						HomeView.this.finish();

						break;
					case 9:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("LN")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							mContext.startActivity(new Intent(mContext,
									SettingView.class));
							HomeView.this.finish();
						}
						break;
					case 10:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("CR01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							mContext.startActivity(new Intent(mContext,
									CustomerQuery.class));

							CommonContexts.MODULE = "CUSTOMER";
							HomeView.this.finish();
						}
						break;
					case 11:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("CE01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							CommonContexts.ISDBVISITED = false;
							CommonContexts.ISREPAYVISITED = false;
							CommonContexts.ISCOLLECTIONVISITED = false;
							CommonContexts.ISPAYMENTVISITED = false;
							CommonContexts.ISENROLVISITED = true;
							CommonContexts.ISCUSTAGENDA = false;
							CommonContexts.ISLOANPREPAYSELECTED = false;
							CommonContexts.ISKYCVIEW = false;
							mContext.startActivity(new Intent(mContext,
									EnrolView.class));
							HomeView.this.finish();
						}
						break;
					case 12:
						// CommonContexts.showProgress(mContext,getString(R.string.loading));
						if (mList.contains("AG01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							CommonContexts.ISDBVISITED = false;
							CommonContexts.ISREPAYVISITED = false;
							CommonContexts.ISCOLLECTIONVISITED = false;
							CommonContexts.ISPAYMENTVISITED = false;
							CommonContexts.ISENROLVISITED = false;
							CommonContexts.ISCUSTAGENDA = true;
							CommonContexts.ISKYCVIEW = false;
							CommonContexts.MODULE = "CUSTOMERALL";

							CommonContexts.ISLOANPREPAYSELECTED = false;

							mContext.startActivity(new Intent(mContext,
									CustomerQuery.class));

							HomeView.this.finish();
						}
						break;

					case 13:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("KC01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							CommonContexts.ISDBVISITED = false;
							CommonContexts.ISREPAYVISITED = false;
							CommonContexts.ISCOLLECTIONVISITED = false;
							CommonContexts.ISPAYMENTVISITED = false;
							CommonContexts.ISENROLVISITED = false;
							CommonContexts.ISCUSTAGENDA = false;
							CommonContexts.ISLOANPREPAYSELECTED = false;
							CommonContexts.ISKYCVIEW = true;
							mContext.startActivity(new Intent(mContext,
									CustomersAll.class));
							CommonContexts.MODULE = "KYC";
							HomeView.this.finish();
						}
						break;

					case 14:
						CommonContexts.showProgress(mContext,
								getString(R.string.loading));
						if (mList.contains("LP01")) {
							linearlayout.setEnabled(false);
							CommonContexts.dismissProgressDialog();
							Toast.makeText(mContext, Constants.NO_ACCESS,
									Toast.LENGTH_SHORT).show();

						} else {
							CommonContexts.ISDBVISITED = false;
							CommonContexts.ISREPAYVISITED = false;
							CommonContexts.ISCOLLECTIONVISITED = false;
							CommonContexts.ISPAYMENTVISITED = false;
							CommonContexts.ISENROLVISITED = false;
							CommonContexts.ISCUSTAGENDA = false;
							CommonContexts.ISLOANPREPAYSELECTED = true;
							CommonContexts.ISKYCVIEW = false;
							mContext.startActivity(new Intent(mContext,
									LoanPrepayView.class));
							HomeView.this.finish();
						}
						break;

					}

				}
			});
			/*
			 * if (position >= index.length) { imageView.setEnabled(false);
			 * textView.setEnabled(false); } else { imageView.setEnabled(true);
			 * textView.setEnabled(true); }
			 */
			return gridView;
		}

		private class ViewHolder {
			ImageView imageView;
			TextView textView;

		}

		public void imageData() {
			// Integer[] mThumbIds = { R.drawable.home, R.drawable.deposits,
			// R.drawable.loans, R.drawable.ic_launcher, R.drawable.home,
			// R.drawable.deposits, R.drawable.loans, R.drawable.ic_launcher,
			// R.drawable.loans };
			// String[] mThumbNames = { "Loan Disbursment", "Loan Repayment",
			// "Deposit Collection", "Deposit Payment",
			// "Customer Based Agenda", "Sync", "Customer", "History", "IBS" };
			//
			// copyVal = new String[mThumbNames.length];
			// copyImages = new Integer[mThumbIds.length];
			// int moveStartIndex = mThumbNames.length - index.length;
			// for (int j = 0, b = 0; j < mThumbNames.length; j++) {
			//
			// boolean shouldMove = false;
			// for (int q = 0; q < index.length; q++) {
			// if (j == index[q]) {
			// shouldMove = true;
			// break;
			// }
			// }
			// if (shouldMove) {
			// copyVal[moveStartIndex] = mThumbNames[j];
			// copyImages[moveStartIndex] = mThumbIds[j];
			// moveStartIndex++;
			// } else {
			// copyVal[b] = mThumbNames[j];
			// copyImages[b] = mThumbIds[j];
			// b++;
			// }
			// }
		}
	}
}