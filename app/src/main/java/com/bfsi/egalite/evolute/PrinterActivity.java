package com.bfsi.egalite.evolute;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bfsi.egalite.entity.AgendaMaster;
import com.bfsi.egalite.entity.CustomerDetail;
import com.bfsi.egalite.entity.HistoryDetails;
import com.bfsi.egalite.entity.Requests;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.DateUtil;
import com.bfsi.egalite.view.LoginView;
import com.impress.api.Printer;

public class PrinterActivity implements EvoluteCallBack {
	static Printer ptr;
	static int k;
	static byte fontstyle;
	int peripheral, logo;
	// static Context mContext;

	String str1 = "Printer mode failure";
	String str2 = "Print success";
	String str3 = "Printer is in good condition";
	String str4 = "Printer platen is open";
	String str5 = "Printer paper is out";
	String str6 = "Printer high headtemp";
	String str7 = "Printer low headtemp";
	String str8 = "Printer improper voltage";
	String str9 = "Printer failed";
	String str10 = "Paperfeed is success";
	String str11 = "Added text is Cleared";
	String str12 = "Library is invalid";
	String str13 = "API not supported for demo version";
	String str14 = "Please enter text";
	String str15 = "Printer platten is open";
	String str16 = "Printer Papper is out";
	String str17 = "Printer improper voltage";
	String str18 = "Print failed";
	String str19 = "Please enter Single character";
	String str20 = "Please enter numeric characters";
	String str21 = "Please enter numeric characters";
	String str22 = "Enter data less than 13 characters";
	String str23 = "Please enter numerics only";
	String str24 = "Bluetooth Connection Disconnected";
	String str25 = "Device not connected";
	String str26 = "Bluetooth is Disconnected";
	String str27 = "Printer param error";

	/* List of Return codes for the respective response */
	public static final int PR_SUCCESS = 0;
	public static final int PR_FAIL = -1;
	public static final int PR_PAPER_OUT = -2;
	public static final int PR_PLATEN_OPEN = -3;
	public static final int PR_HIGH_HEADTEMP = -4;
	public static final int PR_LOW_HEADTEMP = -5;
	public static final int PR_IMPROPER_VOLTAGE = -7;
	public static final int PR_ILLEGAL_LIBRARY = -50;
	public static final int PR_DEMO_VERSION = -51;
	public static final int PR_INACTIVE_PERIPHERAL = -52;
	public static final int PR_INVALID_DEVICE_ID = -53;
	public static final int PR_PARAM_ERROR = -10;
	public static final int PR_NO_DATA = -8;
	public static final int PR_BMP_FILE_NOT_FOUND = -9;
	public static final int PR_NO_RESPONSE = -11;
	public static final int DEVICE_NOTCONNECTED = -100;
	public static final int ENTER_TEXT = -200;
	public static final int ENTER_NUMERIC = -201;
	public static final int ENTER_LESS13 = -202;
	public static PopupWindow pwindo;
	public static View layout;

	private String types;
	private AgendaMaster agendaMasters;
	private Requests reqTxn;
	private HistoryDetails hDtails;
	private CustomerDetail mCustDetails;
	private static Context contexts;
	private int noOfPrints;

	public void printTxns(int noOfPrint,String type, AgendaMaster agendaTxns, Context context) {
		this.types = type;
		this.agendaMasters = agendaTxns;
		this.contexts = context;
		this.noOfPrints = noOfPrint;
		if (CommonContexts.mBT.isEnabled()) {
			if (CommonContexts.CHECK == 0x02) {

				if (type.equalsIgnoreCase("DSB")
						|| type.equalsIgnoreCase("REPAY")
						|| type.equalsIgnoreCase("LNPREPAY"))
					printDsbRepay(noOfPrints,agendaTxns, context);
				else if (type.equalsIgnoreCase("COL")
						|| type.equalsIgnoreCase("PAY"))
					printCollPayment(noOfPrints,agendaTxns, context);

				else if (type.equalsIgnoreCase("CUST")) {
					if (agendaTxns.getTxnCode().equalsIgnoreCase("L01")
							|| agendaTxns.getTxnCode().equalsIgnoreCase("L02")
							|| agendaTxns.getTxnCode().equalsIgnoreCase("L21"))
						printDsbRepay(noOfPrints,agendaTxns, context);
					else if (agendaTxns.getTxnCode().equalsIgnoreCase("D01")
							|| agendaTxns.getTxnCode().equalsIgnoreCase("D02")
							|| agendaTxns.getTxnCode().equalsIgnoreCase("D03"))
						printCollPayment(noOfPrints,agendaTxns, context);
				}

			} else {
				BluetoothPairConnection.pairToDevice(context,
						PrinterActivity.this);
			}

		} else {
			BluetoothPairConnection.blueToothOnOff(context,
					PrinterActivity.this);
		}
	}

	public void printTxnsReq(int noOfPrint,String type, Requests reqTxns, Context context) {
		this.types = type;
		this.reqTxn = reqTxns;
		this.contexts = context;
		this.noOfPrints = noOfPrint;
		if (CommonContexts.mBT.isEnabled()) {
			if (CommonContexts.CHECK == 0x02) {

				if (type.equalsIgnoreCase("CUSTPREPAY"))
					printCustPreReq(noOfPrints,reqTxn, context);
				else if (type.equalsIgnoreCase("CUSTNEW"))
					printCustNewReq(noOfPrints,reqTxn, contexts);

			} else {
				BluetoothPairConnection.pairToDevice(contexts,
						PrinterActivity.this);
			}

		} else {
			BluetoothPairConnection.blueToothOnOff(contexts,
					PrinterActivity.this);
		}
	}

	public void printHistory(int noOfPrint,String type, HistoryDetails hDetails,
			Context context) {
		this.hDtails = hDetails;
		this.contexts = context;
		this.types = type;
		this.noOfPrints = noOfPrint;
		if (CommonContexts.mBT.isEnabled()) {
			if (CommonContexts.CHECK == 0x02) {
				if (type.equalsIgnoreCase("TXNS"))
					printHTDetails(noOfPrints,hDtails, contexts);
				else if (type.equalsIgnoreCase("ENROLLS"))
					printHEDetails(noOfPrints,hDtails, contexts);
			} else {
				BluetoothPairConnection.pairToDevice(contexts,
						PrinterActivity.this);
			}

		} else {
			BluetoothPairConnection.blueToothOnOff(contexts,
					PrinterActivity.this);
		}
	}

	public void printCash(int noOfPrint,String type, CustomerDetail hDetails, Context context) {
		this.mCustDetails = hDetails;
		this.contexts = context;
		this.types = type;
		this.noOfPrints = noOfPrint;
		if (CommonContexts.mBT.isEnabled()) {
			if (CommonContexts.CHECK == 0x02) {
				if (type.equalsIgnoreCase(Constants.CASH))
					printCashDetails(noOfPrints,mCustDetails, contexts);

			} else {
				BluetoothPairConnection.pairToDevice(contexts,
						PrinterActivity.this);
			}

		} else {
			BluetoothPairConnection.blueToothOnOff(contexts,
					PrinterActivity.this);
		}
	}

	
	private void printCustNewReq(int noOfprint,Requests reqTxn2, Context context) {

		ptr = new Printer(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		ptr.iFlushBuf();
		printAddString(ptr,"Transaction NO :" + reqTxn2.getTxnId());
		printAddString(ptr,"AgentId :" + reqTxn2.getAgentId());
		printAddString(ptr,"Customer Id :" + reqTxn2.getCustomerId());
		printAddString(ptr,"Currency :" + reqTxn2.getCcyCode());
		printAddString(ptr,"Settle Amt :" + reqTxn2.getDepInstallmentAmt());
		printAddString(ptr,"Deposit Tenure :" + reqTxn2.getTenure());
		printAddString(ptr,"Installment Frequency :" + reqTxn2.getFrequency());
		printAddString(ptr,"Maturity Date :"
				+ CommonContexts.endateFormat.format(new Date(reqTxn2
						.getMaturityDate())));
		printAddString(ptr,"Interest Rate :" + reqTxn2.getRateofInst());
		printAddString(ptr,"TxnDateTime :"
				+ CommonContexts.simpleDateTimeFormat.format(DateUtil
						.getCurrentDataTime()) + "\n" + "\n" + "\n" + "\n"+"\n"+ "\n" + "\n");
		k = ptr.iStartPrinting(noOfprint);
		messAlert();

	}

	private static void printAddString(Printer ptr,String value)
	{
		if(ptr != null)
		ptr.iPrinterAddData((byte) 0x03, value);
	}
	private void printCustPreReq(int noOfPrint,Requests reqTxn2, Context context) {
		
		ptr = new Printer(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		ptr.iFlushBuf();
		printAddString(ptr,"Transaction NO : "+ reqTxn2.getTxnId());
		printAddString(ptr,"Account No :"+ reqTxn2.getAcNo());
		printAddString(ptr,"AgentId :"+ reqTxn2.getAgentId());
		printAddString(ptr,"Cust Id :"+ reqTxn2.getCustomerId());
		printAddString(ptr,"Currency :"	+ reqTxn2.getCcyCode());
		printAddString(ptr, "Deposit OpenDate"+ reqTxn2.getDepOpenDate());
		printAddString(ptr, "Maturity Date :"
				+ CommonContexts.dateFormat.format(new Date(reqTxn2
						.getMaturityDate())));
		printAddString(ptr,"Installment Amt :"
				+ reqTxn2.getPrepaymentAmt());
		printAddString(ptr,"Settle Amt :"
				+ reqTxn2.getPrepaymentAmt());
		printAddString(ptr,"TxnDateTime :"
				+ CommonContexts.simpleDateTimeFormat.format(DateUtil
						.getCurrentDataTime()) + "\n" + "\n" + "\n" + "\n"+ "\n" + "\n");
		k = ptr.iStartPrinting(noOfPrint);
		messAlert();

	}

	private void printHTDetails(int noOfPrint,HistoryDetails hDetails, Context context) {
		
		ptr = new Printer(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		ptr.iFlushBuf();
		
		printAddString(ptr,"Customer Id: " + hDetails.getCustomerId());
		printAddString(ptr,"Transaction Id :" + hDetails.getTxnId());
		printAddString(ptr,"Txn Amount :" + hDetails.getTxnAmt());
		printAddString(ptr,"Txn DateTime :" + hDetails.getTxnTime());
		printAddString(ptr, "Txn Type :" + hDetails.getTxnType() + "\n" + "\n" + "\n"+ "\n" + "\n");
		
		k = ptr.iStartPrinting(noOfPrint);
		messAlert();

	}

	private void printHEDetails(int noOfPrint,HistoryDetails hDetails, Context context) {
		
		ptr = new Printer(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		ptr.iFlushBuf();
		
		printAddString(ptr, "Customer Name: " + hDetails.getCustomerName());
		printAddString(ptr, "Enrollment Id :" + hDetails.getTxnId());
		printAddString(ptr, "Contact Number :" + hDetails.getContactNumber());
		printAddString(ptr, "Date of Birth :" + hDetails.getDob());
		printAddString(ptr,"Gender :"+ hDetails.getGender());
		printAddString(ptr, "Enroll DateTime :"	+ hDetails.getTxnTime()+ "\n" + "\n" + "\n" + "\n"+ "\n" + "\n");
	
		k = ptr.iStartPrinting(noOfPrint);
		messAlert();

	}

	private void printCashDetails(int noOfPrint,CustomerDetail hDetails, Context context) {
		
		ptr = new Printer(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		ptr.iFlushBuf();
		
		printAddString(ptr,  "Customer Name: " + hDetails.getCustomerFullName());
		printAddString(ptr, "Txn Id :" + hDetails.getTxnId());
		printAddString(ptr, "Acc No :"
				+ hDetails.getAcNo());
		printAddString(ptr, "Agent Id :"
				+ hDetails.getAgentId());
		printAddString(ptr, "Account Type :"
				+ hDetails.getAccType());
		printAddString(ptr, "Customer Id :"
				+ hDetails.getCustomerId());
		printAddString(ptr,  "Currency :"
				+ hDetails.getAccCcy());
		printAddString(ptr,"Deposit/Withdrawal Amt :"
				+ hDetails.getTxnAmt());
		printAddString(ptr,  "Txn Time :"
				+ hDetails.getTxnDateTime()+ "\n"+ "\n" + "\n" + "\n" + "\n"+ "\n" + "\n");
		
		k = ptr.iStartPrinting(noOfPrint);
		messAlert();

	}

	public static void printDsbRepay(int noOfPrint,AgendaMaster agendaMaste, Context context) {

		
		ptr = new Printer(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		ptr.iFlushBuf();
		
		printAddString(ptr, "Transaction NO : "
				+ CommonContexts.TXNID);
		
		printAddString(ptr,"Account No :"
				+ agendaMaste.getCbsAcRefNo());
		
		printAddString(ptr,"Cust Id :"
				+ agendaMaste.getCustomerId());
		printAddString(ptr,"AgentId :"
				+ agendaMaste.getAgentId());
		
		printAddString(ptr,"Currency :"
				+ agendaMaste.getCcyCode());
		printAddString(ptr, "ComponentName :"
				+ agendaMaste.getComponent());
		printAddString(ptr,"Settle Amt :"
				+ agendaMaste.getAmtSettled());
		printAddString(ptr,"TxnDateTime :"
				+ CommonContexts.simpleDateTimeFormat.format(DateUtil
						.getCurrentDataTime()) + "\n" + "\n" + "\n" + "\n"+ "\n" + "\n");
		
		k = ptr.iStartPrinting(noOfPrint);
		messAlert();
	}

	public static void printCollPayment(int noOfPrint,AgendaMaster clc, Context context) {
		
		ptr = new Printer(LoginView.bfsiSetUp, BluetoothComm.mosOut,
				BluetoothComm.misIn);
		ptr.iFlushBuf();
		
		printAddString(ptr,"Transaction NO : "
				+ CommonContexts.TXNID);
		printAddString(ptr,"Account No :"
				+ clc.getCbsAcRefNo());
		printAddString(ptr,"AgentId :"
				+ clc.getAgentId());
		printAddString(ptr,"Cust Id :"
				+ clc.getCustomerId());
		printAddString(ptr,"Currency :"
				+ clc.getCcyCode());
		printAddString(ptr,"ComponentName :"
				+ clc.getComponent());
		printAddString(ptr,"Settle Amt :"
				+ clc.getAmtSettled());
		printAddString(ptr,"TxnDateTime :"
				+ CommonContexts.simpleDateTimeFormat.format(DateUtil
						.getCurrentDataTime()) + "\n" + "\n" + "\n" + "\n");
		
		k = ptr.iStartPrinting(noOfPrint);
		messAlert();
	}

	public static void messAlert() {

		if (k == DEVICE_NOTCONNECTED) {
			printerhander.obtainMessage(DEVICE_NOTCONNECTED,
					"Device not connected").sendToTarget();
		} else if (k == Printer.PR_SUCCESS) {
			printerhander.obtainMessage(PR_SUCCESS, "Print Success")
					.sendToTarget();
		} else if (k == Printer.PR_PLATEN_OPEN) {
			printerhander.obtainMessage(PR_PLATEN_OPEN,
					"Printer platen is open").sendToTarget();
		} else if (k == Printer.PR_PAPER_OUT) {
			printerhander.obtainMessage(PR_PAPER_OUT, "Printer paper is out")
					.sendToTarget();
		} else if (k == Printer.PR_IMPROPER_VOLTAGE) {
			printerhander.obtainMessage(PR_IMPROPER_VOLTAGE,
					"Printer improper voltage").sendToTarget();
		} else if (k == Printer.PR_FAIL) {
			printerhander.obtainMessage(PR_FAIL, "Printer failed")
					.sendToTarget();
			CommonContexts.CHECK = -1;
			CommonContexts.mMfi.closeConn();
		} else if (k == Printer.PR_PARAM_ERROR) {
			printerhander.obtainMessage(PR_PARAM_ERROR, "Printer param error")
					.sendToTarget();
		} else if (k == Printer.PR_NO_RESPONSE) {
			printerhander.obtainMessage(PR_NO_RESPONSE,
					"No response from impress device").sendToTarget();
			CommonContexts.CHECK = -1;
			CommonContexts.mMfi.closeConn();
		} else if (k == Printer.PR_DEMO_VERSION) {
			printerhander.obtainMessage(PR_DEMO_VERSION,
					"Library is in demo version").sendToTarget();
		} else if (k == Printer.PR_INVALID_DEVICE_ID) {
			printerhander.obtainMessage(PR_INVALID_DEVICE_ID,
					"Connected  device is not license authenticated.")
					.sendToTarget();
		} else if (k == Printer.PR_ILLEGAL_LIBRARY) {

			printerhander
					.obtainMessage(PR_ILLEGAL_LIBRARY, "Library not valid")
					.sendToTarget();
		}
		// super.onPostExecute(result);
	}

	@SuppressLint("HandlerLeak")
	static Handler printerhander = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PR_SUCCESS:
				String str1 = (String) msg.obj;
				showdialog(str1);
				break;
			case PR_FAIL:
				String str2 = (String) msg.obj;
				showdialog(str2);
				break;
			case PR_PAPER_OUT:
				String str3 = (String) msg.obj;
				showdialog(str3);
				break;
			case PR_PLATEN_OPEN:
				String str4 = (String) msg.obj;
				showdialog(str4);
				break;
			case PR_HIGH_HEADTEMP:
				String str5 = (String) msg.obj;
				showdialog(str5);
				break;
			case PR_LOW_HEADTEMP:
				String str6 = (String) msg.obj;
				showdialog(str6);
				break;
			case PR_IMPROPER_VOLTAGE:
				String str7 = (String) msg.obj;
				showdialog(str7);
				break;
			case PR_ILLEGAL_LIBRARY:
				String str8 = (String) msg.obj;
				showdialog(str8);
				break;
			case PR_DEMO_VERSION:
				String str9 = (String) msg.obj;
				showdialog(str9);
				break;
			case PR_INACTIVE_PERIPHERAL:
				String str10 = (String) msg.obj;
				showdialog(str10);
				break;
			case PR_INVALID_DEVICE_ID:
				String str11 = (String) msg.obj;
				showdialog(str11);
				break;
			case PR_PARAM_ERROR:
				String str12 = (String) msg.obj;
				showdialog(str12);
				break;
			case PR_NO_DATA:
				String str13 = (String) msg.obj;
				showdialog(str13);
				break;
			case PR_BMP_FILE_NOT_FOUND:
				String str14 = (String) msg.obj;
				showdialog(str14);
				break;
			case PR_NO_RESPONSE:
				String str15 = (String) msg.obj;
				showdialog(str15);
				break;
			case DEVICE_NOTCONNECTED:
				String str16 = (String) msg.obj;
				showdialog(str16);
				break;
			case ENTER_TEXT:
				String str17 = (String) msg.obj;
				showdialog(str17);
				break;
			case ENTER_NUMERIC:
				String str18 = (String) msg.obj;
				showdialog(str18);
				break;
			case ENTER_LESS13:
				String str19 = (String) msg.obj;
				showdialog(str19);
				break;
			case 19:
				String str20 = (String) msg.obj;
				showdialog(str20);
				break;
			case 20:
				String str21 = (String) msg.obj;
				showdialog(str21);
				break;
			case 21:
				String str22 = (String) msg.obj;
				showdialog(str22);
				break;
			case 22:
				String str23 = (String) msg.obj;
				showdialog(str23);
				break;
			case 23:
				String str24 = (String) msg.obj;
				showdialog(str24);
				break;
			case 25:
				String str25 = (String) msg.obj;
				showdialog(str25);
				break;
			case 26:
				String str26 = (String) msg.obj;
				showdialog(str26);
				break;
			case 27:
				String str27 = (String) msg.obj;
				showdialog(str27);
				break;
			}
		};
	};

	public static void showdialog(String str) {

		Toast.makeText(contexts, str, Toast.LENGTH_LONG).show();

		
	}

	
	@Override
	public void doPrinters(int results) {
		if (results == 0x02) {
			if (types.equalsIgnoreCase("DSB")
					|| types.equalsIgnoreCase("REPAY")
					|| types.equalsIgnoreCase("LNPREPAY"))
				printDsbRepay(noOfPrints,agendaMasters, contexts);
			else if (types.equalsIgnoreCase("COL")
					|| types.equalsIgnoreCase("PAY"))
				printCollPayment(noOfPrints,agendaMasters, contexts);
			else if (types.equalsIgnoreCase("CUSTPREPAY"))
				printCustPreReq(noOfPrints,reqTxn, contexts);
			else if (types.equalsIgnoreCase("CUSTNEW"))
				printCustNewReq(noOfPrints,reqTxn, contexts);
			else if (types.equalsIgnoreCase("CUST")) {
				if (agendaMasters.getTxnCode().equalsIgnoreCase("L01")
						|| agendaMasters.getTxnCode().equalsIgnoreCase("L02")
						|| agendaMasters.getTxnCode().equalsIgnoreCase("L21"))
					printDsbRepay(noOfPrints,agendaMasters, contexts);
				else if (agendaMasters.getTxnCode().equalsIgnoreCase("D01")
						|| agendaMasters.getTxnCode().equalsIgnoreCase("D02")
						|| agendaMasters.getTxnCode().equalsIgnoreCase("D03"))
					printCollPayment(noOfPrints,agendaMasters, contexts);
			} else if (types.equalsIgnoreCase("TXNS"))
				printHTDetails(noOfPrints,hDtails, contexts);
			else if (types.equalsIgnoreCase("ENROLLS"))
				printHEDetails(noOfPrints,hDtails, contexts);
		}
	}
}
