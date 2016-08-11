package com.bfsi.egalite.dao.sqlite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.view.R;

public class DbHandlerL extends SQLiteOpenHelper {

	private static DbHandlerL _dbHelper;
	private static String DB_PATH = "";
	private static String DB_NAME = "MFI_DB";
	private SQLiteDatabase database;
	private final Context _context;

	public static String TABLE_MBS_AGENDA_MASTER = "MBS_AGENDA_MASTER";
	public static String TABLE_MBS_AGN_LOAN_PAID_SCH = "MBS_AGN_LOAN_PAID_SCH";
	public static String TABLE_MBS_CUSTOMER_DETAIL = "MBS_CUSTOMER_DETAIL";
	
	public static String TABLE_MBS_AGN_LOAN_DETAIL = "MBS_AGN_LOAN_DETAIL";
	public static String TABLE_MBS_AGN_DEPOSIT_DETAIL = "MBS_AGN_DEPOSIT_DETAIL";
	public static String TABLE_MBS_TXN_MASTER = "MBS_TXN_MASTER";
	
	public static String TABLE_MBS_APP_PARAMETERS = "MBS_APP_PARAMETERS";
	public static String TABLE_MBS_LOV = "MBS_LOV";
	public static String TABLE_MBS_MESSAGE_CODE = "MBS_MESSAGE_CODE";
	
	public static String TABLE_MBS_BRANCH_DETAILS = "MBS_BRANCH_DETAILS";
	public static String TABLE_MBS_AGENT_CASH_RECORD = "MBS_AGENT_CASH_RECORD";
	public static String TABLE_MBS_DEVICE_DETAIL = "MBS_DEVICE_DETAIL";
	
	public static String TABLE_MBS_AGENTS = "MBS_AGENTS";
	public static String TABLE_MBS_CCY_CODES = "MBS_CCY_CODES";
	public static String TABLE_MBS_FX_RATES = "MBS_FX_RATES";
	
	public static String TABLE_MBS_ENROLMENT = "MBS_ENROLMENT";
	public static String TABLE_MBS_ENROLMENT_DOCUMENT = "MBS_ENROLMENT_DOCUMENT";
	public static String VIEW_AGN_LOANS = "MBS_AGN_LOANS_V";
	
	public static String VIEW_AGN_CHILD_LOANS = "MBS_AGN_CHILD_LOANS_V";
	public static String VIEW_AGN_DEPOSITS = "MBS_AGN_DEPOSITS_V";
	public static String VIEW_MBS_HISTORY = "MBS_HISTORY_V";
	
	public static String VIEW_MBS_CUSTOMERS = "MBS_CUSTOMERS_V";
	public static String VIEW_MBS_CUSTOMERS_AGN_ALL = "MBS_CUSTOMER_AGN_ALL_V";
	public static String VIEW_MBS_CUSTOMER_AGN_QUERY_V = "CUSTOMER_AGN_QUERY_V";
	
	public static String TABLE_MBS_MODULE_MASTER = "MBS_MODULE_MASTER";
	public static String TABLE_MBS_TXN_CODES = "MBS_TXN_CODES";
	public static String TABLE_MBS_CUSTACC_DETAIL = "MBS_CUSTACC_DETAILS";
	
	public static String TABLE_MBS_BIOMETRIC_DATA = "MBS_BIOMETRIC_DATA";
	public static String TABLE_MBS_SMS_TEMPLATE = "SMS_TEMPLATE";
	public static String VIEW_MBS_CUSTOMER_AGN_ALL_V = "CUSTOMER_AGN_ALL_V";
	
	public static String VIEW_MBS_AGENDA_DASHBOARD_V = "AGENDA_DASHBOARD_V";
	// SMS_CONFIG COLS

	public static String COL_MBS_SMS_TXN_CODE = "TXN_CODE";
	public static String COL_MBS_SMS_TXN_SMS = "TXN_SMS";
	public static String COL_MBS_SMS_DYN_FIELDS = "DYN_FIELDS";

	// BIOMETRIC

	public static String COL_MBS_BIO_ENROLID = "ENROLLID";
	public static String COL_MBS_BIO_RHLF_TMPLTDATA = "RHLF_TMPLTDATA";
	public static String COL_MBS_BIO_RHRF_TMPLTDATA = "RHRF_TMPLTDATA";
	public static String COL_MBS_BIO_RHMF_TMPLTDATA = "RHMF_TMPLTDATA";
	public static String COL_MBS_BIO_RHIF_TMPLTDATA = "RHIF_TMPLTDATA";
	public static String COL_MBS_BIO_RHTF_TMPLTDATA = "RHTF_TMPLTDATA";
	public static String COL_MBS_BIO_LHLF_TMPLTDATA = "LHLF_TMPLTDATA";
	public static String COL_MBS_BIO_LHRF_TMPLTDATA = "LHRF_TMPLTDATA";
	public static String COL_MBS_BIO_LHMF_TMPLTDATA = "LHMF_TMPLTDATA";
	public static String COL_MBS_BIO_LHIF_TMPLTDATA = "LHIF_TMPLTDATA";
	public static String COL_MBS_BIO_LHTF_TMPLTDATA = "LHTF_TMPLTDATA";
	// for scaneed images

	public static String COL_MBS_BIO_RHLF_SCANDATA = "RHLF_SCANDATA";
	public static String COL_MBS_BIO_RHRF_SCANDATA = "RHRF_SCANDATA";
	public static String COL_MBS_BIO_RHMF_SCANDATA = "RHMF_SCANDATA";
	public static String COL_MBS_BIO_RHIF_SCANDATA = "RHIF_SCANDATA";
	public static String COL_MBS_BIO_RHTF_SCANDATA = "RHTF_SCANDATA";
	public static String COL_MBS_BIO_LHLF_SCANDATA = "LHLF_SCANDATA";
	public static String COL_MBS_BIO_LHRF_SCANDATA = "LHRF_SCANDATA";
	public static String COL_MBS_BIO_LHMF_SCANDATA = "LHMF_SCANDATA";
	public static String COL_MBS_BIO_LHIF_SCANDATA = "LHIF_SCANDATA";
	public static String COL_MBS_BIO_LHTF_SCANDATA = "LHTF_SCANDATA";

	// CUST ACC COLS
	public static String COL_CUSTACC_CUSTOMER_ID = "CUSTOMER_ID";
	public static String COL_CUSTACC_CUSTOMER_FULL_NAME = "CUSTOMER_FULL_NAME";
	public static String COL_CUSTACC_CUST_AC_NO = "CUST_AC_NO";
	public static String COL_CUSTACC_BRANCH_CODE = "BRANCH_CODE";
	public static String COL_CUSTACC_ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public static String COL_CUSTACC_AC_DESC = "AC_DESC";
	public static String COL_CUSTACC_AC_CCY = "AC_CCY";
	public static String COL_CUSTACC_AC_STAT_NO_DR = "AC_STAT_NO_DR";
	public static String COL_CUSTACC_AC_STAT_NO_CR = "AC_STAT_NO_CR";
	public static String COL_CUSTACC_AC_STAT_BLOCK = "AC_STAT_BLOCK";
	public static String COL_CUSTACC_AC_STAT_STOP_PAY = "AC_STAT_STOP_PAY";
	public static String COL_CUSTACC_AC_STAT_DORMANT = "AC_STAT_DORMANT";
	public static String COL_CUSTACC_AGENT_ID = "AGENT_ID";
	public static String COL_CUSTACC_LOCATION_CODE = "LOCATION_CODE";
	public static String COL_CUSTACC_CREDIT_OFFICER_ID = "CREDIT_OFFICER_ID";

	// SYNC CHECKS
	public static String COL_SYNC_BATCH_ID = "BATCH_ID";
	public static String COL_SYNC_SU = "STATICUPLOAD";
	public static String COL_SYNC_DD = "DATADOWNLOAD";

	// MODULE MASTER
	public static String COL_MODULE_MODULE_CODE = "MODULE_CODE";
	public static String COL_MODULE_MODULE_DESC = "MODULE_DESC";
	public static String COL_MODULE_IS_LICENSED = "IS_LICENSED";
	public static String COL_MODULE_IS_PERMANENT = "IS_PERMANENT";

	// TXN_CODES
	public static String COL_TXN_MODULE_CODES = "MODULE_CODES";
	public static String COL_TXN_TXN_CODES = "TXN_CODES";
	public static String COL_TXN_TXN_DESCS = "TXN_DESCS";

	// ENROLMENT COLS

	public static String COL_MBS_ENRL_ENROLMENTID = "ENROLMENTID";
	public static String COL_MBS_ENRL_WORKFLOWQTYPE = "WORKFLOWQTYPE";
	public static String COL_MBS_ENRL_FIRSTNAME = "FIRSTNAME";
	public static String COL_MBS_ENRL_LASTNAME = "LASTNAME";
	public static String COL_MBS_ENRL_MIDDLENAME = "MIDDLENAME";
	public static String COL_MBS_ENRL_DOB = "DOB";
	public static String COL_MBS_ENRL_GENDER = "GENDER";
	public static String COL_MBS_ENRL_RESIDENCETYPE = "RESIDENCETYPE";
	public static String COL_MBS_ENRL_NATIONALID = "NATIONAID";
	public static String COL_MBS_ENRL_ADDRESS1 = "ADDRESS1";
	public static String COL_MBS_ENRL_ADDESS2 = "ADDRESS2";
	public static String COL_MBS_ENRL_ADDESS3 = "ADDRESS3";
	public static String COL_MBS_ENRL_ADDESS4 = "ADDRESS4";
	public static String COL_MBS_ENRL_CITY = "CITY";
	public static String COL_MBS_ENRL_STATE = "STATE";
	public static String COL_MBS_ENRL_ZIPCODE = "ZIPCODE";
	public static String COL_MBS_ENRL_COUNTRY = "COUNTRY";
	public static String COL_MBS_ENRL_EMAILID = "EMAILID";
	public static String COL_MBS_ENRL_CONTACTNO = "CONTACTNO";
	public static String COL_MBS_ENRL_MARTIALSTATUS = "MARTIALSTATUS";
	public static String COL_MBS_ENRL_PROFESSION = "PROFESSION";
	public static String COL_MBS_ENRL_PRFREMARK = "PRFREMARK";
	public static String COL_MBS_ENRL_ACCOUNTCATEGORY = "ACCOUNTCATEGORY";
	public static String COL_MBS_ENRL_ACCOUNTTYPE = "ACCOUNTYPE";
	public static String COL_MBS_ENRL_CURRENCY = "CURRENCY";
	public static String COL_MBS_ENRL_ISACTIVE = "ISACTIVE";
	public static String COL_MBS_ENRL_MODULECODE = "MODULECODE";
	public static String COL_MBS_ENRL_TXNCODE = "TXNCODE";
	public static String COL_MBS_ENRL_TXNINITIME = "TXNINITIME";
	public static String COL_MBS_ENRL_TXNSYNCTIME = "TXNSYNCTIME";
	public static String COL_MBS_ENRL_AGENTID = "AGENTID";
	public static String COL_MBS_ENRL_DEVICEID = "DEVICEID";
	public static String COL_MBS_ENRL_LOCATIONCODE = "LOCATIONCODE";
	public static String COL_MBS_ENRL_TXNSTATUS = "TXNSTATUS";
	public static String COL_MBS_ENRL_ISSYNCED = "ISSYNCED";
	public static String COL_MBS_ENRL_TXNERRORCODE = "TXNERRORCODE";
	public static String COL_MBS_ENRL_TXNERRORMESSAGE = "TXNERRORMESSAGE";
	public static String COL_MBS_ENRL_GROUPINDIVIDUALTYPE = "GROUPINDIVIDUALTYPE";
	public static String COL_MBS_ENRL_TEMPGROUPID = "TEMPGROUPID";
	public static String COL_MBS_ENRL_ISKYCONLY = "ISKYCONLY";
	public static String COL_MBS_ENRL_CUSTOMERID = "CUSTOMERID";
	public static String COL_MBS_ENRL_POC = "POC";
	public static String COL_MBS_ENRL_THUMBIMAGE = "THUMBIMAGE";

	// ENROLMENT DOCUMENTS COLS
	public static String COL_MBS_ENRL_ENROLID = "ENROLLID";
	public static String COL_MBS_ENRL_KYC_IMG_CUSTOMER = "KYC_IMG_CUSTOMER";
	public static String COL_MBS_ENRL_KYC_ID_1_IMG = "KYC_ID_1_IMG";
	public static String COL_MBS_ENRL_KYC_ID_1_TYPE = "KYC_ID_1_TYPE";
	public static String COL_MBS_ENRL_KYC_ID_1_NUMBER = "KYC_ID_1_NUMBER";
	public static String COL_MBS_ENRL_KYC_ID_1_PROOF_TYPE = "KYC_ID_1_PROOF_TYPE";
	public static String COL_MBS_ENRL_KYC_ID_2_IMG = "KYC_ID_2_IMG";
	public static String COL_MBS_ENRL_KYC_ID_2_TYPE = "KYC_ID_2_TYPE";
	public static String COL_MBS_ENRL_KYC_ID_2_NUMBER = "KYC_ID_2_NUMBER";
	public static String COL_MBS_ENRL_KYC_ID_2_PROOF_TYPE = "KYC_ID_2_PROOF_TYPE";
	public static String COL_MBS_ENRL_KYC_ID_3_IMG = "KYC_ID_3_IMG";
	public static String COL_MBS_ENRL_KYC_ID_3_TYPE = "KYC_ID_3_TYPE";
	public static String COL_MBS_ENRL_KYC_ID_3_NUMBER = "KYC_ID_3_NUMBER";
	public static String COL_MBS_ENRL_KYC_ID_3_PROOF_TYPE = "KYC_ID_3_PROOF_TYPE";

	// AGENDA_MASTER
	public static String COL_AGENDA_AGENDA_ID = "AGENDA_ID";
	public static String COL_AGENDA_SEQ_NO = "SEQ_NO";
	public static String COL_AGENDA_CBS_AC_REF_NO = "CBS_AC_REF_NO";
	public static String COL_AGENDA_MODULE_CODE = "MODULE_CODE";
	public static String COL_AGENDA_TXN_CODE = "TXN_CODE";
	public static String COL_AGENDA_AGENT_ID = "AGENT_ID";
	public static String COL_AGENDA_LOCATION_CODE = "LOCATION_CODE";
	public static String COL_AGENDA_AGENDA_STATUS = "AGENDA_STATUS";
	public static String COL_AGENDA_BRANCH_CODE = "BRANCH_CODE";
	public static String COL_AGENDA_CUSTOMER_ID = "CUSTOMER_ID";
	public static String COL_AGENDA_CUSTOMER_NAME = "CUSTOMER_NAME";
	public static String COL_AGENDA_CCY_CODE = "CCY_CODE";
	public static String COL_AGENDA_LCY_CODE = "LCY_CODE";
	public static String COL_AGENDA_CMP_ST_DATE = "CMP_ST_DATE";
	public static String COL_AGENDA_CMP_END_DATE = "CMP_END_DATE";
	public static String COL_AGENDA_AGENDA_AMT = "AGENDA_AMT";
	public static String COL_AGENDA_AGENDA_AMT_LCY = "AGENDA_AMT_LCY";
	public static String COL_AGENDA_LN_DISBURSMENT_TYPE = "LN_DISBURSMENT_TYPE";
	public static String COL_AGENDA_LN_IS_FUTURE_SCH = "LN_IS_FUTURE_SCH";
	public static String COL_AGENDA_DP_IS_REDEMPTION = "DP_IS_REDEMPTION";
	public static String COL_AGENDA_DEVICE_ID = "DEVICE_ID";
	public static String COL_AGENDA_CMP_NAME = "CMP_NAME";
	public static String COL_AGENDA_SMS_M_MOBILE = "SMS_M_MOBILE";
	public static String COL_AGENDA_IS_GROUP_LOAN = "IS_GROUP_LOAN";
	public static String COL_AGENDA_IS_PARENT_LOAN = "IS_PARENT_LOAN";
	public static String COL_AGENDA_IS_PARENT_CUSTOMER = "IS_PARENT_CUSTOMER";
	public static String COL_AGENDA_PARENT_CUSTOMER_ID = "PARENT_CUSTOMER_ID";
	public static String COL_AGENDA_PARENT_CBS_REF_AC_NO = "PARENT_CBS_REF_AC_NO";

	// LOAN_PAID_SCH
	public static String COL_LOANPAID_CBS_AC_REF_NO = "CBS_AC_REF_NO";
	public static String COL_LOANPAID_BRANCH_CODE = "BRANCH_CODE";
	public static String COL_LOANPAID_SCH_DUE_DATE = "SCH_DUE_DATE";
	public static String COL_LOANPAID_SCH_PAID_DATE = "SCH_PAID_DATE";
	public static String COL_LOANPAID_SETTLEMENT_CCY_CODE = "SETTLEMENT_CCY_CODE";
	public static String COL_LOANPAID_AMT_SETTLED = "AMT_SETTLED";
	public static String COL_LOANPAID_FULL_PARTIAL_IND = "FULL_PARTIAL_IND";

	// MBS_CUSTOMER_DETAIL
	public static String COL_CUST_CUSTOMER_ID = "CUSTOMER_ID";
	public static String COL_CUST_CUSTOMER_FULL_NAME = "CUSTOMER_FULL_NAME";
	public static String COL_CUST_CUSTOMER_SHORT_NAME = "CUSTOMER_SHORT_NAME";
	public static String COL_CUST_CUSTOMER_CATEGORY = "CUSTOMER_CATEGORY";
	public static String COL_CUST_GENDER = "GENDER";
	public static String COL_CUST_DOB = "DOB";
	public static String COL_CUST_CUSTOMER_SINCE = "CUSTOMER_SINCE";
	public static String COL_CUST_LOCATION_CODE = "LOCATION_CODE";
	public static String COL_CUST_ADDRESS_LINE1 = "ADDRESS_LINE1";
	public static String COL_CUST_ADDRESS_LINE2 = "ADDRESS_LINE2";
	public static String COL_CUST_ADDRESS_LINE3 = "ADDRESS_LINE3";
	public static String COL_CUST_ADDRESS_LINE4 = "ADDRESS_LINE4";
	public static String COL_CUST_ZIP_CODE = "ZIP_CODE";
	public static String COL_CUST_CITY = "CITY";
	public static String COL_CUST_STATE = "STATE";
	public static String COL_CUST_COUNTRY = "COUNTRY";
	public static String COL_CUST_NATIONALITY = "NATIONALITY";
	public static String COL_CUST_LOC_BRANCH_CODE = "LOCAL_BRANCH_CODE";
	public static String COL_CUST_PREFERRED_LANGUAGE = "PREFERRED_LANGUAGE";
	public static String COL_CUST_EMAIL_ADDRESS = "EMAIL_ADDRESS";
	public static String COL_CUST_SMS_REQUIRED = "SMS_REQUIRED";
	public static String COL_CUST_MOBILE_NUMBER = "MOBILE_NUMBER";
	public static String COL_CUST_AGENT_ID = "AGENT_ID";
	public static String COL_CUST_CREDIT_OFFICER = "CREDIT_OFFICER";
	public static String COL_CUST_COLLECT_KYC = "COLLECT_KYC";
	public static String COL_CUST_PARENT_CUST_ID = "PARENT_CUST_ID";
	public static String COL_CUST_IS_PARENT_CUST = "IS_PARENT_CUST";

	// AGN_LOAN_DETAIL
	public static String COL_LOAN_LOAN_AC_NO = "LOAN_AC_NO";
	public static String COL_LOAN_BRANCH_CODE = "BRANCH_CODE";
	public static String COL_LOAN_CUSTOMER_ID = "CUSTOMER_ID";
	public static String COL_LOAN_CUSTOMER_NAME = "CUSTOMER_NAME";
	public static String COL_LOAN_CREDIT_OFFICER_CODE = "CREDIT_OFFICER_CODE";
	public static String COL_LOAN_LOCATION_CODE = "LOCATION_CODE";
	public static String COL_LOAN_GROUP_ID = "GROUP_ID";
	public static String COL_LOAN_SANCTIONED_DATE = "SANCTIONED_DATE";
	public static String COL_LOAN_LAST_DISBURSED_DATE = "LAST_DISBURSED_DATE";
	public static String COL_LOAN_DISBR_TYPE = "DISBR_TYPE";
	public static String COL_LOAN_LOAN_AC_CCY = "LOAN_AC_CCY";
	public static String COL_LOAN_IS_FULLY_DISBURSED = "IS_FULLY_DISBURSED";
	public static String COL_LOAN_SANCTIONED_PRINCIPAL_AMT = "SANCTIONED_PRINCIPAL_AMT";
	public static String COL_LOAN_DISBURSED_PRINCIPAL_AMT = "DISBURSED_PRINCIPAL_AMT";
	public static String COL_LOAN_INTEREST_RATE = "INTEREST_RATE";
	public static String COL_LOAN_INTEREST_ACCURED = "INTEREST_ACCURED";
	public static String COL_LOAN_PRINCIPAL_AMT_REPAID = "PRINCIPAL_AMT_REPAID";
	public static String COL_LOAN_PRINCIPAL_OUTSTANDING = "PRINCIPAL_OUTSTANDING";
	public static String COL_LOAN_LAST_REPAYMENT_DATE = "LAST_REPAYMENT_DATE";
	public static String COL_LOAN_IS_GROUP_LOAN = "IS_GROUP_LOAN";
	public static String COL_LOAN_IS_PARENT_LOAN = "IS_PARENT_LOAN";
	public static String COL_LOAN_PARENT_CUST_ID = "PARENT_CUST_ID";
	public static String COL_LOAN_PARENT_LOAN_AC_NO = "PARENT_LOAN_AC_NO";
	public static String COL_LOAN_AGENT_ID = "AGENT_ID";

	// AGN_DEPOSIT_DETAIL
	public static String COL_DEPOSIT_DEP_AC_NO = "DEP_AC_NO";
	public static String COL_DEPOSIT_BRANCH_CODE = "BRANCH_CODE";
	public static String COL_DEPOSIT_CUSTOMER_ID = "CUSTOMER_ID";
	public static String COL_DEPOSIT_CUSTOMER_NAME = "CUSTOMER_NAME";
	public static String COL_DEPOSIT_AC_CCY = "AC_CCY";
	public static String COL_DEPOSIT_OPEN_DATE = "OPEN_DATE";
	public static String COL_DEPOSIT_MATURITY_DATE = "MATURITY_DATE";
	public static String COL_DEPOSIT_SCH_INSTALLMENT_AMT = "SCH_INSTALLMENT_AMT";
	public static String COL_DEPOSIT_PAY_FREQ_TYPE = "PAY_FREQ_TYPE";
	public static String COL_DEPOSIT_PAY_FREQ = "PAY_FREQ";
	public static String COL_DEPOSIT_TENURE_TYPE = "TENURE_TYPE";
	public static String COL_DEPOSIT_TENURE = "TENURE";
	public static String COL_DEPOSIT_INT_RATE = "INT_RATE";
	public static String COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT = "PRINCIPAL_MATURITY_AMOUNT";
	public static String COL_DEPOSIT_INTEREST_ACCURED_TILL_DATE = "INTEREST_ACCURED_TILL_DATE";
	public static String COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE = "INSTALLMENT_PAID_TILL_DATE";
	public static String COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE = "TOTAL_INSTALLMENT_AMT_DUE";
	public static String COL_DEPOSIT_REDEMPTION_FLAG = "REDEMPTION_FLAG";
	public static String COL_DEPOSIT_REDEMPTION_PAYOUT_DATE = "REDEMPTION_PAYOUT_DATE";
	public static String COL_DEPOSIT_REDEMPTION_AMOUNT = "REDEMPTION_AMOUNT";
	public static String COL_DEPOSIT_LOCATION_CODE = "LOCATION_CODE";
	public static String COL_DEPOSIT_AGENT_ID = "AGENT_ID";
	public static String COL_DEPOSIT_CREDIT_OFFICER_CODE = "CREDIT_OFFICER_CODE";

	// MBS_FX_RATES
	public static String COL_FX_BRAN_CODE = "BRAN_CODE";
	public static String COL_FX_CCY1 = "CCY1";
	public static String COL_FX_CCY2 = "CCY2";
	public static String COL_FX_RECORD_STAT = "RECORD_STAT";
	public static String COL_FX_MID_RATE = "MID_RATE";
	public static String COL_FX_BUY_RATE = "BUY_RATE";
	public static String COL_FX_SALE_RATE = "SALE_RATE";
	public static String COL_FX_CBS_UPLD_HOB_ID = "CBS_UPLD_HOB_ID";
	public static String COL_FX_SYNC_STATUS = "SYNC_STATUS";
	public static String COL_FX_SYNC_TIME = "SYNC_TIME";

	// CCY_CODES
	public static String COL_CCY_CCY_CODE = "CCY_CODE";
	public static String COL_CCY_CCY_NAME = "CCY_NAME";
	public static String COL_CCY_ISO_CCY_CODE = "ISO_CCY_CODE";
	public static String COL_CCY_ISO_ALT_CODE = "ISO_ALT_CODE";
	public static String COL_CCY_COUNTRY = "COUNTRY";
	public static String COL_CCY_ROUND_RULE = "ROUND_RULE";
	public static String COL_CCY_DECIMAL = "DECIMAL";

	// AGENTS
	public static String COL_AGENT_AGENT_ID = "AGENT_ID";
	public static String COL_AGENT_PASSWORD = "PASSWORD";
	public static String COL_AGENT_PASSWORD_SALT = "PASSWORD_SALT";
	public static String COL_AGENT_AGENT_TYPE = "AGENT_TYPE";
	public static String COL_AGENT_CASH_LIMIT = "CASH_LIMIT";
	public static String COL_AGENT_DEVICE_ID = "DEVICE_ID";
	public static String COL_AGENT_STATUS = "STATUS";
	public static String COL_AGENT_FIRST_NAME = "FIRST_NAME";
	public static String COL_AGENT_LAST_NAME = "LAST_NAME";
	public static String COL_AGENT_LANG = "LANG";
	public static String COL_AGENT_AGENCY_CODE = "AGENCY_CODE";
	public static String COL_AGENT_CHANGEPASSWORD_DATE = "CHANGEPASSWORD_DATE";
	public static String COL_AGENT_CREDIT_OFFICER = "CREDIT_OFFICER";
	public static String COL_AGENT_DOB = "DOB";
	public static String COL_AGENT_GENDER = "GENDER";
	public static String COL_AGENT_AUTH_STATUS = "AUTH_STATUS";
	public static String COL_AGENT_DATA_KEY = "DATA_KEY";
	public static String COL_AGENT_REG_KEY = "REG_KEY";
	public static String COL_AGENT_REGKEY_EXP_DATE = "REGKEY_EXP_DATE";
	public static String COL_AGENT_REG_STATUS = "REG_STATUS";
	public static String COL_AGENT_PROMPT_CHANGEPASSWORD = "PROMPT_CHANGEPASSWORD";
	public static String COL_AGENT_LOCATION_CODE = "LOCATION_CODE";
	public static String COL_AGENT_ROLES_LIST = "ROLES_LIST";
	public static String COL_AGENT_USERNAME = "USERNAME";

	public static String COL_AGENT_LAST_LOGINTIME = "LAST_LOGINTIME";
	public static String COL_AGENT_NO_INVALID_LOGIN = "NO_INVALID_LOGIN";
	public static String COL_AGENT_LAST_SYNC_TIME = "LAST_SYNC_TIME";
	public static String COL_AGENT_EVOLUTE_MAC_ID = "EVOLUTE_MAC_ID";

	// CASH_RECORD
	public static String COL_CASH_ENTRY_SEQ_NO = "ENTRY_SEQ_NO";
	public static String COL_CASH_TXN_ID = "TXN_ID";
	public static String COL_CASH_TXN_SEQ_NO = "TXN_SEQ_NO";
	public static String COL_CASH_TXN_SOURCE = "TXN_SOURCE";
	public static String COL_CASH_TXN_DATETIME = "TXN_DATETIME";
	public static String COL_CASH_TXN_CODE = "TXN_CODE";
	public static String COL_CASH_AGENT_ID = "AGENT_ID";
	public static String COL_CASH_DEVICE_ID = "DEVICE_ID";
	public static String COL_CASH_AGENDA_ID = "AGENDA_ID";
	public static String COL_CASH_AGN_SEQ_NO = "AGN_SEQ_NO";
	public static String COL_CASH_TXN_CCY_CODE = "TXN_CCY_CODE";
	public static String COL_CASH_DR_CR_IND = "DR_CR_IND";
	public static String COL_CASH_CASH_AMT = "CASH_AMT";
	public static String COL_CASH_AUTH_STAT = "AUTH_STAT";
	public static String COL_CASH_IS_REVERSAL = "IS_REVERSAL";
	public static String COL_CASH_IS_DELETED = "IS_DELETED";

	// DEVICE
	public static String COL_DEVICE_DEVICE_ID = "DEVICE_ID";
	public static String COL_DEVICE_DEVICE_TYPE = "DEVICE_TYPE";
	public static String COL_DEVICE_UNIQUE_ID = "UNIQUE_ID";
	public static String COL_DEVICE_ISSUED_DATE = "ISSUED_DATE";
	public static String COL_DEVICE_LAST_SYNC = "LAST_SYNC";
	public static String COL_DEVICE_ISSUED_BY_CO = "ISSUED_BY_CO";
	public static String COL_DEVICE_BRAND_MODEL = "BRAND_MODEL";
	public static String COL_DEVICE_OS = "OS";
	public static String COL_DEVICE_APP_VERSION = "APP_VERSION";
	public static String COL_DEVICE_SIM_NUMBER = "SIM_NUMBER";

	// BRANCH_DETAIL
	public static String COL_BRANCH_CODE = "CODE";
	public static String COL_BRANCH_DATE = "DATE";

	// MESSAGE_CODE
	public static String COL_MESSAGE_CODE = "CODE";
	public static String COL_MESSAGE_LANG = "LANG";
	public static String COL_MESSAGE_DESC = "DESC";
	public static String COL_MESSAGE_ERR_TYPE = "ERR_TYPE";
	public static String COL_MESSAGE_SUB_SYS = "SUB_SYS";
	public static String COL_MESSAGE_MSG_TYPE = "MSG_TYPE";

	// LOV
	public static String COL_LOV_KEYIDENTIFIER = "KEYIDENTIFIER";
	public static String COL_LOV_INTVALUE = "INTVALUE";
	public static String COL_LOV_VALUE = "VALUE";
	public static String COL_LOV_ORDERS = "ORDERS";

	// AGENT_LOGIN

	// LIST OF VALUES
	public static String COL_LIST_NAME = "NAME";
	public static String COL_LIST_VALUE = "VALUE";
	public static String COL_LIST_VALUE_DESC = "VALUE_DESC";
	public static String COL_LIST_VALUE_STATUS = "VALUE_STATUS";

	// PARAMETERS
	public static String COL_PARAM_NAME = "NAME";
	public static String COL_PARAM_VALUE = "VALUE";

	// TXN_MASTER
	public static String COL_TXN_TXN_ID = "TXN_ID";
	public static String COL_TXN_MODULE_CODE = "MODULE_CODE";
	public static String COL_TXN_TXN_CODE = "TXN_CODE";
	public static String COL_TXN_AGENDA_ID = "AGENDA_ID";
	public static String COL_TXN_SEQ_NO = "SEQ_NO";
	public static String COL_TXN_CBS_AC_REF_NO = "CBS_AC_REF_NO";
	public static String COL_TXN_BRN_CODE = "BRN_CODE";
	public static String COL_TXN_CUSTOMER_ID = "CUSTOMER_ID";
	public static String COL_TXN_INIT_TIME = "INIT_TIME";
	public static String COL_TXN_IS_SYNCED = "IS_SYNCED";
	public static String COL_TXN_AGENT_ID = "AGENT_ID";
	public static String COL_TXN_DEVICE_ID = "DEVICE_ID";
	public static String COL_TXN_LOCATION_CODE = "LOCATION_CODE";
	public static String COL_TXN_LN_IS_FUTURE_SCH = "LN_IS_FUTURE_SCH";
	public static String COL_TXN_TXN_AMT = "TXN_AMT";
	public static String COL_TXN_AMT_LCY = "AMT_LCY";
	public static String COL_TXN_SETTLED_AMT = "SETTLED_AMT";
	public static String COL_TXN_SETTLED_AMT_LCY_NUM = "SETTLED_AMT_LCY_NUM";
	public static String COL_TXN_REQ_DEP_NO_INST = "REQ_DEP_NO_INST";
	public static String COL_TXN_REQ_RED_REQ_DT = "REQ_RED_REQ_DT";
	public static String COL_TXN_REQ_RED_FULL_PART_IND = "REQ_RED_FULL_PART_IND";
	public static String COL_TXN_REQ_MATURITY_DATE = "REQ_MATURITY_DATE";
	public static String COL_TXN_REQ_INT_RATE = "REQ_INT_RATE";
	public static String COL_TXN_REQ_DP_TENURE_TYPE = "REQ_DP_TENURE_TYPE";
	public static String COL_TXN_REQ_DP_FREQUENCY_TYPE = "REQ_DP_FREQUENCY_TYPE";
	public static String COL_TXN_REQ_DP_FREQUENCY = "REQ_DP_FREQUENCY";
	public static String COL_TXN_REQ_DP_TENURE = "REQ_DP_TENURE";
	public static String COL_TXN_FULL_PART_IND = "FULL_PART_IND";
	public static String COL_TXN_CCY_CODE = "CCY_CODE";
	public static String COL_TXN_LCY_CODE = "LCY_CODE";
	public static String COL_TXN_AC_TYPE = "ACCOUNT_TYPE";
	public static String COL_TXN_TXN_STATUS = "TXN_STATUS";
	public static String COL_TXN_TXN_ERR_CODE = "TXN_ERR_CODE";
	public static String COL_TXN_TXN_ERR_MSG = "TXN_ERR_MSG";
	public static String COL_TXN_GENERATED_MSG = "GENERATED_MSG";
	public static String COL_TXN_SMS_MOBILE_NO = "SMS_MOBILE_NO";
	public static String COL_TXN_SMS_CONTENT = "SMS_CONTENT";
	public static String COL_TXN_GENERATE_REVR = "GENERATE_REVR";
	public static String COL_TXN_MBS_TXN_SEQ_NO = "MBS_TXN_SEQ_NO";

	DbHandlerL(Context context) {
		super(context, DB_NAME, null, 1);
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = context.getString(R.string._data_data_)
					+ context.getPackageName() + "/databases/";
		}
		CommonContexts.DBPATHMAIN = DB_PATH + DB_NAME;
		getWritableDatabase();
		this._context = context;
	}

	// Class singleton
	public static DbHandlerL getInstance(Context context) {
		if (_dbHelper == null) {
			_dbHelper = new DbHandlerL(context);
		}
		return _dbHelper;
	}

	public static DbHandlerL getInstance() {
		return _dbHelper;
	}

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			openDataBase();
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Open the database, so we can query it
	public boolean openDataBase() throws SQLException {
		CommonContexts.DBPATHMAIN = DB_PATH + DB_NAME;
		database = SQLiteDatabase.openDatabase(CommonContexts.DBPATHMAIN, null,
				SQLiteDatabase.OPEN_READWRITE);
		return database != null;
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {

			if (android.os.Build.VERSION.SDK_INT >= 17) {
				DB_PATH = _context.getApplicationInfo().dataDir + "/";
			} else {
				DB_PATH = _context.getString(R.string._data_data_)
						+ _context.getPackageName() + "/databases/";
			}
			CommonContexts.DBPATHMAIN = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(CommonContexts.DBPATHMAIN,
					null, SQLiteDatabase.OPEN_READWRITE
							+ SQLiteDatabase.CREATE_IF_NECESSARY);
		} catch (SQLiteException e) {
			System.out.println("" + e.getMessage());
		} catch (Exception e) {
			System.out.println("" + e.getMessage());
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = _context.getAssets().open(DB_NAME);
		// Path to the just created empty db
		CommonContexts.DBPATHMAIN = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(CommonContexts.DBPATHMAIN);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		CommonContexts.DBPATHMAIN = DB_PATH + DB_NAME;
		database = db;
		ccyCodes();
		fxRates();
		agents();
		cashRecord();
		device();
		branchDetail();
		messageCode();
		lov();
		parameters();
		transactions();
		agenda();
		loansPaid();
		customers();
		loans();
		deposits();
		Enrolement();
		Documents();
		agnLoansV();
		agnChildLoansV();
		agnDepositsV();
		moduleCodes();
		txnCodes();
		historyV();
		customersV();
		cashAccDetails();
		BiometricData();
		customersAgnV();
		smsTemplate();
		agendaDashboardV();

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_AGENDA_MASTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_AGN_LOAN_PAID_SCH);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_CUSTOMER_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_AGN_LOAN_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_AGN_DEPOSIT_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_TXN_MASTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_APP_PARAMETERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_LOV);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_MESSAGE_CODE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_BRANCH_DETAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_AGENT_CASH_RECORD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_DEVICE_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_AGENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_CCY_CODES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_FX_RATES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_MODULE_MASTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_TXN_CODES);
		db.execSQL("DROP TABLE IF EXISTS " + VIEW_AGN_DEPOSITS);
		db.execSQL("DROP TABLE IF EXISTS " + VIEW_AGN_LOANS);
		db.execSQL("DROP TABLE IF EXISTS " + VIEW_AGN_CHILD_LOANS);
		db.execSQL("DROP TABLE IF EXISTS " + VIEW_MBS_HISTORY);
		db.execSQL("DROP TABLE IF EXISTS " + VIEW_MBS_CUSTOMERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_CUSTACC_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_ENROLMENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_ENROLMENT_DOCUMENT);

		db.execSQL("DROP TABLE IF EXISTS " + VIEW_MBS_CUSTOMERS_AGN_ALL);
		db.execSQL("DROP TABLE IF EXISTS " + VIEW_MBS_CUSTOMER_AGN_QUERY_V);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_MODULE_MASTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_TXN_CODES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_CUSTACC_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_BIOMETRIC_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MBS_SMS_TEMPLATE);
		db.execSQL("DROP TABLE IF EXISTS " + VIEW_MBS_CUSTOMER_AGN_ALL_V);
		db.execSQL("DROP TABLE IF EXISTS " + VIEW_MBS_AGENDA_DASHBOARD_V);

		onCreate(db);
	}

	private void fxRates() {
		String query = ("CREATE TABLE " + TABLE_MBS_FX_RATES + " ("
				+ COL_FX_BRAN_CODE + " VARCHAR(5) NOT NULL, " + COL_FX_CCY1
				+ " VARCHAR(3) NOT NULL, " + COL_FX_CCY2
				+ " VARCHAR(3) NOT NULL, " + COL_FX_RECORD_STAT
				+ " VARCHAR(1) NOT NULL, " + COL_FX_MID_RATE
				+ " NUMBER(22,8) , " + COL_FX_BUY_RATE + " NUMBER(22,8) , "
				+ COL_FX_SALE_RATE + " NUMBER(22,8) , "
				+ COL_FX_CBS_UPLD_HOB_ID + " VARCHAR(25) , "
				+ COL_FX_SYNC_STATUS + " VARCHAR(1), " + COL_FX_SYNC_TIME
				+ " VARCHAR(16), PRIMARY KEY(" + COL_FX_BRAN_CODE + ","
				+ COL_FX_CCY1 + "," + COL_FX_CCY2 + "," + COL_FX_RECORD_STAT + "));");
		database.execSQL(query);
	}

	private void ccyCodes() {
		String query = ("CREATE TABLE " + TABLE_MBS_CCY_CODES + " ("
				+ COL_CCY_CCY_CODE + " VARCHAR(3) NOT NULL PRIMARY KEY, "
				+ COL_CCY_CCY_NAME + " VARCHAR(100) , " + COL_CCY_ISO_CCY_CODE
				+ " VARCHAR(3) , " + COL_CCY_ISO_ALT_CODE + " VARCHAR(3) , "
				+ COL_CCY_COUNTRY + " VARCHAR(2) , " + COL_CCY_ROUND_RULE
				+ " VARCHAR(1) , " + COL_CCY_DECIMAL + " NUMBER);");
		database.execSQL(query);
	}

	private void agents() {
		String query = ("CREATE TABLE " + TABLE_MBS_AGENTS + " ("
				+ COL_AGENT_AGENT_ID + " VARCHAR(25) NOT NULL PRIMARY KEY, "
				+ COL_AGENT_PASSWORD + " VARCHAR(300) , "
				+ COL_AGENT_PASSWORD_SALT + " VARCHAR(300) , "
				+ COL_AGENT_AGENT_TYPE + " VARCHAR(1) , "
				+ COL_AGENT_CASH_LIMIT + " NUMBER , " + COL_AGENT_DEVICE_ID
				+ " VARCHAR(25) , " + COL_AGENT_STATUS + " VARCHAR(1) , "
				+ COL_AGENT_FIRST_NAME + " VARCHAR(100) , "
				+ COL_AGENT_LAST_NAME + " VARCHAR(100) , " + COL_AGENT_LANG
				+ " VARCHAR(3) , " + COL_AGENT_AGENCY_CODE + " VARCHAR(25) , "
				+ COL_AGENT_CHANGEPASSWORD_DATE + " TEXT , "
				+ COL_AGENT_CREDIT_OFFICER + " VARCHAR(25) , " + COL_AGENT_DOB
				+ " TEXT ," + COL_AGENT_GENDER + " VARCHAR(6) , "
				+ COL_AGENT_AUTH_STATUS + " VARCHAR(1) , " + COL_AGENT_DATA_KEY
				+ " VARCHAR(300) , " + COL_AGENT_REG_KEY + " VARCHAR(10) , "
				+ COL_AGENT_REGKEY_EXP_DATE + " TEXT ," + COL_AGENT_REG_STATUS
				+ " VARCHAR(1) , " + COL_AGENT_LOCATION_CODE
				+ " VARCHAR(25) , " + COL_AGENT_ROLES_LIST + " VARCHAR(120) , "
				+ COL_AGENT_USERNAME + " VARCHAR(120) , "
				+ COL_AGENT_LAST_LOGINTIME + " TEXT , "
				+ COL_AGENT_NO_INVALID_LOGIN + " VARCHAR(10) , "
				+ COL_AGENT_LAST_SYNC_TIME + " TEXT , "
				+ COL_AGENT_EVOLUTE_MAC_ID + " VARCHAR(35) , "
				+ COL_AGENT_PROMPT_CHANGEPASSWORD + " VARCHAR(1));");
		database.execSQL(query);
	}

	private void agenda() {
		String query = ("CREATE TABLE " + TABLE_MBS_AGENDA_MASTER + " ("
				+ COL_AGENDA_AGENDA_ID + " VARCHAR(25) NOT NULL , "
				+ COL_AGENDA_SEQ_NO + " NUMBER(22,8) NOT NULL , "
				+ COL_AGENDA_CBS_AC_REF_NO + " VARCHAR(25) , "
				+ COL_AGENDA_MODULE_CODE + " VARCHAR(2) , "
				+ COL_AGENDA_TXN_CODE + " VARCHAR(3) , " + COL_AGENDA_AGENT_ID
				+ " VARCHAR(25) , " + COL_AGENDA_LOCATION_CODE
				+ " VARCHAR(25) , " + COL_AGENDA_AGENDA_STATUS
				+ " NUMBER(1,0) , " + COL_AGENDA_BRANCH_CODE + " VARCHAR(5) , "
				+ COL_AGENDA_CUSTOMER_ID + " VARCHAR(25) , "
				+ COL_AGENDA_CUSTOMER_NAME + " VARCHAR(100) , "
				+ COL_AGENDA_CCY_CODE + " VARCHAR(3) , " + COL_AGENDA_LCY_CODE
				+ " VARCHAR(3) , " + COL_AGENDA_CMP_ST_DATE + " TEXT ,"
				+ COL_AGENDA_CMP_END_DATE + " TEXT ," + COL_AGENDA_AGENDA_AMT
				+ " NUMBER(22,8), " + COL_AGENDA_AGENDA_AMT_LCY
				+ " NUMBER(22,8) , " + COL_AGENDA_LN_DISBURSMENT_TYPE
				+ " VARCHAR(1) , " + COL_AGENDA_LN_IS_FUTURE_SCH
				+ " VARCHAR(1) , " + COL_AGENDA_DP_IS_REDEMPTION
				+ " VARCHAR(1) , " + COL_AGENDA_CMP_NAME + " VARCHAR(25) , "
				+ COL_AGENDA_DEVICE_ID + " VARCHAR(25), "

				+ COL_AGENDA_SMS_M_MOBILE + " VARCHAR(25), "
				+ COL_AGENDA_IS_GROUP_LOAN + " VARCHAR(1), "
				+ COL_AGENDA_IS_PARENT_LOAN + " VARCHAR(1), "
				+ COL_AGENDA_IS_PARENT_CUSTOMER + " VARCHAR(1), "
				+ COL_AGENDA_PARENT_CUSTOMER_ID + " VARCHAR(25), "
				+ COL_AGENDA_PARENT_CBS_REF_AC_NO + " VARCHAR(25), "
				+ "PRIMARY KEY(" + COL_AGENDA_AGENDA_ID + ","
				+ COL_AGENDA_SEQ_NO + "));");
		database.execSQL(query);
	}

	private void loansPaid() {
		String query = ("CREATE TABLE " + TABLE_MBS_AGN_LOAN_PAID_SCH + " ("
				+ COL_LOANPAID_CBS_AC_REF_NO + " VARCHAR(25) NOT NULL , "
				+ COL_LOANPAID_BRANCH_CODE + " VARCHAR(5) NOT NULL, "
				+ COL_LOANPAID_SCH_DUE_DATE + " TEXT NOT NULL , "
				+ COL_LOANPAID_SCH_PAID_DATE + " TEXT NOT NULL , "
				+ COL_LOANPAID_SETTLEMENT_CCY_CODE + " VARCHAR(25) , "
				+ COL_LOANPAID_AMT_SETTLED + " NUMBER(1,0) , "
				+ COL_LOANPAID_FULL_PARTIAL_IND + " VARCHAR(5), PRIMARY KEY("
				+ COL_LOANPAID_CBS_AC_REF_NO + "," + COL_LOANPAID_BRANCH_CODE
				+ "," + COL_LOANPAID_SCH_DUE_DATE + ","
				+ COL_LOANPAID_SCH_PAID_DATE + "));");
		database.execSQL(query);
	}

	private void customers() {
		String query = ("CREATE TABLE " + TABLE_MBS_CUSTOMER_DETAIL + " ("
				+ COL_CUST_CUSTOMER_ID + " VARCHAR(25) NOT NULL  PRIMARY KEY, "
				+ COL_CUST_CUSTOMER_FULL_NAME + " VARCHAR(100) , "
				+ COL_CUST_CUSTOMER_SHORT_NAME + " VARCHAR(50) , "
				+ COL_CUST_CUSTOMER_CATEGORY + " VARCHAR(1) , "
				+ COL_CUST_GENDER + " VARCHAR(1) , " + COL_CUST_DOB + " TEXT ,"
				+ COL_CUST_CUSTOMER_SINCE + " TEXT ," + COL_CUST_LOCATION_CODE
				+ " VARCHAR(25) , " + COL_CUST_ADDRESS_LINE1
				+ " VARCHAR(100) , " + COL_CUST_ADDRESS_LINE2
				+ " VARCHAR(100) , " + COL_CUST_ADDRESS_LINE3
				+ " VARCHAR(100) , " + COL_CUST_ADDRESS_LINE4
				+ " VARCHAR(100) , " + COL_CUST_ZIP_CODE + " VARCHAR2(20) , "
				+ COL_CUST_CITY + " VARCHAR(50) , " + COL_CUST_STATE
				+ " VARCHAR(50) , " + COL_CUST_COUNTRY + " VARCHAR(2), "
				+ COL_CUST_NATIONALITY + " VARCHAR(2) , "
				+ COL_CUST_LOC_BRANCH_CODE + " VARCHAR(5) , "
				+ COL_CUST_PREFERRED_LANGUAGE + " VARCHAR(3) , "
				+ COL_CUST_EMAIL_ADDRESS + " VARCHAR(50) , "
				+ COL_CUST_SMS_REQUIRED + " VARCHAR(1) , " + COL_CUST_AGENT_ID
				+ " VARCHAR(25) , " + COL_CUST_CREDIT_OFFICER
				+ " VARCHAR(25) , " + COL_CUST_COLLECT_KYC + " VARCHAR(1) , "
				+ COL_CUST_PARENT_CUST_ID + " VARCHAR(25) , "
				+ COL_CUST_IS_PARENT_CUST + " VARCHAR(1) , "
				+ COL_CUST_MOBILE_NUMBER + " VARCHAR(15)); "

		);
		database.execSQL(query);
	}

	private void loans() {
		String query = ("CREATE TABLE " + TABLE_MBS_AGN_LOAN_DETAIL + " ("
				+ COL_LOAN_LOAN_AC_NO + " VARCHAR(25) NOT NULL  PRIMARY KEY, "
				+ COL_LOAN_BRANCH_CODE + " VARCHAR(5) , "
				+ COL_LOAN_CUSTOMER_ID + " VARCHAR(25) , "
				+ COL_LOAN_CUSTOMER_NAME + " VARCHAR(100) , "
				+ COL_LOAN_CREDIT_OFFICER_CODE + " VARCHAR(25) , "
				+ COL_LOAN_LOCATION_CODE + " VARCHAR(25)  , "
				+ COL_LOAN_GROUP_ID + " VARCHAR(25) , "
				+ COL_LOAN_SANCTIONED_DATE + " TEXT ,"
				+ COL_LOAN_LAST_DISBURSED_DATE + " TEXT ,"
				+ COL_LOAN_DISBR_TYPE + " VARCHAR(1) , " + COL_LOAN_LOAN_AC_CCY
				+ " VARCHAR(3), " + COL_LOAN_IS_FULLY_DISBURSED
				+ " VARCHAR(1) , " + COL_LOAN_SANCTIONED_PRINCIPAL_AMT
				+ " NUMBER(22,8) , " + COL_LOAN_DISBURSED_PRINCIPAL_AMT
				+ " NUMBER(22,8), " + COL_LOAN_INTEREST_RATE
				+ " NUMBER(22,8) , " + COL_LOAN_INTEREST_ACCURED
				+ " NUMBER(22,8) , " + COL_LOAN_PRINCIPAL_AMT_REPAID
				+ " NUMBER(22,8), " + COL_LOAN_PRINCIPAL_OUTSTANDING
				+ " NUMBER(22,8), " + COL_LOAN_IS_GROUP_LOAN + " VARCHAR(1) , "
				+ COL_LOAN_IS_PARENT_LOAN + " VARCHAR(1) , "
				+ COL_LOAN_PARENT_CUST_ID + " VARCHAR(25) , "
				+ COL_LOAN_PARENT_LOAN_AC_NO + " VARCHAR(25) , "
				+ COL_LOAN_AGENT_ID + " VARCHAR(25) , "
				+ COL_LOAN_LAST_REPAYMENT_DATE + " TEXT ); ");
		database.execSQL(query);
	}

	private void deposits() {
		String query = ("CREATE TABLE " + TABLE_MBS_AGN_DEPOSIT_DETAIL + " ("
				+ COL_DEPOSIT_DEP_AC_NO
				+ " VARCHAR(25) NOT NULL  PRIMARY KEY, "
				+ COL_DEPOSIT_BRANCH_CODE + " VARCHAR(5) , "
				+ COL_DEPOSIT_CUSTOMER_ID + " VARCHAR(25) , "
				+ COL_DEPOSIT_CUSTOMER_NAME + " VARCHAR(100) , "
				+ COL_DEPOSIT_AC_CCY + " VARCHAR(3) , " + COL_DEPOSIT_OPEN_DATE
				+ " TEXT ," + COL_DEPOSIT_MATURITY_DATE + " TEXT ,"
				+ COL_DEPOSIT_SCH_INSTALLMENT_AMT + " NUMBER(22,8) , "
				+ COL_DEPOSIT_PAY_FREQ_TYPE + " VARCHAR(1) , "
				+ COL_DEPOSIT_PAY_FREQ + " NUMBER(10,0)  , "
				+ COL_DEPOSIT_TENURE_TYPE + " VARCHAR(1) , "
				+ COL_DEPOSIT_TENURE + " NUMBER(10,0) , "
				+ COL_DEPOSIT_INT_RATE + " NUMBER(22,8) , "
				+ COL_DEPOSIT_PRINCIPAL_MATURITY_AMOUNT + " NUMBER(22,8) , "
				+ COL_DEPOSIT_INTEREST_ACCURED_TILL_DATE + " NUMBER(22,8) , "
				+ COL_DEPOSIT_INSTALLMENT_PAID_TILL_DATE + " NUMBER(22,8), "
				+ COL_DEPOSIT_TOTAL_INSTALLMENT_AMT_DUE + " NUMBER(22,8), "
				+ COL_DEPOSIT_REDEMPTION_FLAG + " VARCHAR(1) , "
				+ COL_DEPOSIT_REDEMPTION_PAYOUT_DATE + " TEXT , "
				+ COL_DEPOSIT_LOCATION_CODE + " VARCHAR(25) , "
				+ COL_DEPOSIT_AGENT_ID + " VARCHAR(25) , "
				+ COL_DEPOSIT_CREDIT_OFFICER_CODE + " VARCHAR(25) , "
				+ COL_DEPOSIT_REDEMPTION_AMOUNT + " NUMBER(22,8)); ");
		database.execSQL(query);
	}

	private void cashRecord() {

		String query = ("CREATE TABLE " + TABLE_MBS_AGENT_CASH_RECORD + " ("
				+ COL_CASH_ENTRY_SEQ_NO + " NUMBER NOT NULL PRIMARY KEY, "
				+ COL_CASH_TXN_ID + " VARCHAR(25) , " + COL_CASH_AGENDA_ID
				+ " VARCHAR(25) , " + COL_CASH_TXN_SOURCE + " VARCHAR(25) , "
				+ COL_CASH_TXN_SEQ_NO + " NUMBER , " + COL_CASH_TXN_DATETIME
				+ " TEXT ," + COL_CASH_TXN_CODE + " VARCHAR(25) , "
				+ COL_CASH_AGENT_ID + " VARCHAR(25) , " + COL_CASH_DEVICE_ID
				+ " VARCHAR(25) , " + COL_CASH_AGN_SEQ_NO + " VARCHAR(25) , "
				+ COL_CASH_TXN_CCY_CODE + " VARCHAR(3) , " + COL_CASH_DR_CR_IND
				+ " VARCHAR(1) , " + COL_CASH_CASH_AMT + " NUMBER(22,8) , "
				+ COL_CASH_IS_REVERSAL + " VARCHAR(1) , " + COL_CASH_IS_DELETED
				+ " VARCHAR(1) , " + COL_CASH_AUTH_STAT + " NUMBER(10));");
		database.execSQL(query);
	}

	private void device() {
		String query = ("CREATE TABLE " + TABLE_MBS_DEVICE_DETAIL + " ("
				+ COL_DEVICE_DEVICE_ID + " VARCHAR(25) NOT NULL PRIMARY KEY, "
				+ COL_DEVICE_DEVICE_TYPE + " VARCHAR(1) , "
				+ COL_DEVICE_UNIQUE_ID + " VARCHAR(25) , "
				+ COL_DEVICE_ISSUED_DATE + " TEXT ," + COL_DEVICE_LAST_SYNC
				+ " TEXT ," + COL_DEVICE_ISSUED_BY_CO + " VARCHAR(25) , "
				+ COL_DEVICE_BRAND_MODEL + " VARCHAR(100) , " + COL_DEVICE_OS
				+ " VARCHAR(100) , " + COL_DEVICE_APP_VERSION
				+ " VARCHAR(100) , " + COL_DEVICE_SIM_NUMBER + " NUMBER(20));");
		database.execSQL(query);
	}

	private void branchDetail() {
		String query = ("CREATE TABLE " + TABLE_MBS_BRANCH_DETAILS + " ("
				+ COL_BRANCH_CODE + " VARCHAR(5) NOT NULL PRIMARY KEY, "
				+ COL_BRANCH_DATE + " TEXT);");
		database.execSQL(query);
	}

	private void messageCode() {
		String query = ("CREATE TABLE " + TABLE_MBS_MESSAGE_CODE + " ("
				+ COL_MESSAGE_CODE + " VARCHAR(10) NOT NULL , "
				+ COL_MESSAGE_LANG + " VARCHAR(2) NOT NULL , "
				+ COL_MESSAGE_DESC + " VARCHAR(200) , " + COL_MESSAGE_ERR_TYPE
				+ " VARCHAR(5) , " + COL_MESSAGE_SUB_SYS + " VARCHAR(1) , "
				+ COL_MESSAGE_MSG_TYPE + " VARCHAR(1), PRIMARY KEY("
				+ COL_MESSAGE_CODE + "," + COL_MESSAGE_LANG + "));");
		database.execSQL(query);
	}

	private void lov() {
		String query = ("CREATE TABLE " + TABLE_MBS_LOV + " ("
				+ COL_LOV_KEYIDENTIFIER + " VARCHAR(5) NOT NULL , "
				+ COL_LOV_INTVALUE + " VARCHAR(3) NOT NULL , " + COL_LOV_VALUE
				+ " VARCHAR(50) , " + COL_LOV_ORDERS
				+ " VARCHAR(2) , PRIMARY KEY(" + COL_LOV_KEYIDENTIFIER + ","
				+ COL_LOV_INTVALUE + "));");
		database.execSQL(query);
	}


	private void parameters() {
		String query = ("CREATE TABLE " + TABLE_MBS_APP_PARAMETERS + " ("
				+ COL_PARAM_NAME + " VARCHAR(100) NOT NULL PRIMARY KEY, "
				+ COL_PARAM_VALUE + " VARCHAR(100));");
		database.execSQL(query);
	}

	private void transactions() {
		String query = ("CREATE TABLE " + TABLE_MBS_TXN_MASTER + " ("
				+ COL_TXN_TXN_ID + " VARCHAR(25) NOT NULL , "
				+ COL_TXN_MODULE_CODE + " VARCHAR(2) , " + COL_TXN_TXN_CODE
				+ " VARCHAR(4) , " + COL_TXN_AGENDA_ID + " VARCHAR(25) , "
				+ COL_TXN_SEQ_NO + "  NUMBER , " + COL_TXN_CBS_AC_REF_NO
				+ " VARCHAR(25) , " + COL_TXN_BRN_CODE + " VARCHAR(5) , "
				+ COL_TXN_CUSTOMER_ID + " VARCHAR(25) , " + COL_TXN_INIT_TIME
				+ " TEXT ," + COL_TXN_IS_SYNCED + " NUMBER(1) , "
				+ COL_TXN_AGENT_ID + " VARCHAR(25) , " + COL_TXN_DEVICE_ID
				+ " VARCHAR(25) , " + COL_TXN_LOCATION_CODE + " VARCHAR(25) , "
				+ COL_TXN_LN_IS_FUTURE_SCH + " VARCHAR(1) , " + COL_TXN_TXN_AMT
				+ " NUMBER(22,8) , " + COL_TXN_AMT_LCY + " NUMBER(22,8) , "
				+ COL_TXN_SETTLED_AMT + " NUMBER(22,8) , "
				+ COL_TXN_SETTLED_AMT_LCY_NUM + " NUMBER(22,8) , "

				+ COL_TXN_REQ_DEP_NO_INST + " VARCHAR(10) , "
				+ COL_TXN_REQ_RED_REQ_DT + " TEXT ,"
				+ COL_TXN_REQ_RED_FULL_PART_IND + " VARCHAR(1) , "
				+ COL_TXN_REQ_MATURITY_DATE + " TEXT ," + COL_TXN_REQ_INT_RATE
				+ " NUMBER , " + COL_TXN_REQ_DP_TENURE_TYPE + " VARCHAR(1) , "
				+ COL_TXN_REQ_DP_FREQUENCY_TYPE + " VARCHAR(1) , "
				+ COL_TXN_REQ_DP_FREQUENCY + " NUMBER , "
				+ COL_TXN_REQ_DP_TENURE + " NUMBER , " + COL_TXN_FULL_PART_IND
				+ " VARCHAR(1) , " + COL_TXN_CCY_CODE + " VARCHAR(5) , "
				+ COL_TXN_TXN_STATUS + " NUMBER(1) , " + COL_TXN_TXN_ERR_CODE
				+ " VARCHAR(10) , " + COL_TXN_TXN_ERR_MSG + " VARCHAR(100) , "
				+ COL_TXN_GENERATED_MSG + " VARCHAR(1) , "
				+ COL_TXN_SMS_MOBILE_NO + " VARCHAR(20) , " + COL_TXN_AC_TYPE
				+ " VARCHAR(2) , " + COL_TXN_SMS_CONTENT + " VARCHAR(150) , "
				+ COL_TXN_GENERATE_REVR + " VARCHAR(1) , "
				+ COL_TXN_MBS_TXN_SEQ_NO + " VARCHAR(1) , " + COL_TXN_LCY_CODE
				+ " VARCHAR(5), PRIMARY KEY(" + COL_TXN_TXN_ID + ","
				+ COL_TXN_MBS_TXN_SEQ_NO + "));");
		database.execSQL(query);
	}

	private void Enrolement() {

		String query = ("CREATE TABLE " + TABLE_MBS_ENROLMENT + " ("
				+ COL_MBS_ENRL_ENROLMENTID
				+ " VARCHAR(25) NOT NULL PRIMARY KEY, "
				+ COL_MBS_ENRL_WORKFLOWQTYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_FIRSTNAME + " VARCHAR(50), "
				+ COL_MBS_ENRL_LASTNAME + " VARCHAR(50), "
				+ COL_MBS_ENRL_MIDDLENAME + " VARCHAR(50), " + COL_MBS_ENRL_DOB
				+ " TEXT, " + COL_MBS_ENRL_GENDER + " VARCHAR(1), "
				+ COL_MBS_ENRL_RESIDENCETYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_NATIONALID + " VARCHAR(3), "
				+ COL_MBS_ENRL_ADDRESS1 + " VARCHAR(100), "
				+ COL_MBS_ENRL_ADDESS2 + " VARCHAR(100), "
				+ COL_MBS_ENRL_ADDESS3 + " VARCHAR(100), "
				+ COL_MBS_ENRL_ADDESS4 + " VARCHAR(100), "+ COL_MBS_ENRL_CITY
				+ " VARCHAR(50), " + COL_MBS_ENRL_STATE + " VARCHAR(50), "
				+ COL_MBS_ENRL_ZIPCODE + " VARCHAR(20), "
				+ COL_MBS_ENRL_COUNTRY + " VARCHAR(3), " + COL_MBS_ENRL_EMAILID
				+ " VARCHAR(100), " + COL_MBS_ENRL_CONTACTNO + " VARCHAR(20), "
				+ COL_MBS_ENRL_MARTIALSTATUS + " VARCHAR(1), "
				+ COL_MBS_ENRL_PROFESSION + " VARCHAR(1), "
				+ COL_MBS_ENRL_PRFREMARK + " VARCHAR(100), "
				+ COL_MBS_ENRL_ACCOUNTCATEGORY + " VARCHAR(1), "
				+ COL_MBS_ENRL_ACCOUNTTYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_CURRENCY + " VARCHAR(3), "
				+ COL_MBS_ENRL_ISACTIVE + " VARCHAR(1), "
				+ COL_MBS_ENRL_MODULECODE + " VARCHAR(2), "
				+ COL_MBS_ENRL_TXNCODE + " VARCHAR(3), "
				+ COL_MBS_ENRL_TXNINITIME + " TEXT, "
				+ COL_MBS_ENRL_TXNSYNCTIME + " TEXT, " + COL_MBS_ENRL_AGENTID
				+ " VARCHAR(25), " + COL_MBS_ENRL_DEVICEID + " VARCHAR(25), "
				+ COL_MBS_ENRL_LOCATIONCODE + " VARCHAR(25), "
				+ COL_MBS_ENRL_TXNSTATUS + " NUMBER(1), "
				+ COL_MBS_ENRL_ISSYNCED + " VARCHAR(1), "
				+ COL_MBS_ENRL_TXNERRORCODE + " VARCHAR(10), "
				+ COL_MBS_ENRL_TXNERRORMESSAGE + " VARCHAR(100), "
				+ COL_MBS_ENRL_GROUPINDIVIDUALTYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_TEMPGROUPID + " VARCHAR(25), "
				+ COL_MBS_ENRL_ISKYCONLY + " VARCHAR(1), " + COL_MBS_ENRL_POC
				+ " VARCHAR(1), " + COL_MBS_ENRL_THUMBIMAGE + " BLOB, "
				+ COL_MBS_ENRL_CUSTOMERID + " VARCHAR(25));");
		database.execSQL(query);
	}

	private void Documents() {

		String query = ("CREATE TABLE " + TABLE_MBS_ENROLMENT_DOCUMENT + " ("
				+ COL_MBS_ENRL_ENROLID + " VARCHAR(25) NOT NULL , "
				+ COL_MBS_ENRL_KYC_IMG_CUSTOMER + " BLOB, "
				+ COL_MBS_ENRL_KYC_ID_1_IMG + " BLOB, "
				+ COL_MBS_ENRL_KYC_ID_1_TYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_KYC_ID_1_NUMBER + " VARCHAR(50), "
				+ COL_MBS_ENRL_KYC_ID_1_PROOF_TYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_KYC_ID_2_IMG + " BLOB, "
				+ COL_MBS_ENRL_KYC_ID_2_TYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_KYC_ID_2_NUMBER + " VARCHAR(50), "
				+ COL_MBS_ENRL_KYC_ID_2_PROOF_TYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_KYC_ID_3_IMG + " BLOB, "
				+ COL_MBS_ENRL_KYC_ID_3_TYPE + " VARCHAR(1), "
				+ COL_MBS_ENRL_KYC_ID_3_NUMBER + " VARCHAR(50), "
				+ COL_MBS_ENRL_KYC_ID_3_PROOF_TYPE + " VARCHAR(1));");
		database.execSQL(query);

	}

	public void moduleCodes() {
		String query = ("CREATE TABLE " + TABLE_MBS_MODULE_MASTER + " ("
				+ COL_MODULE_MODULE_CODE + " VARCHAR(25) NOT NULL , "
				+ COL_MODULE_MODULE_DESC + " VARCHAR(100), "
				+ COL_MODULE_IS_PERMANENT + " VARCHAR(1), "
				+ COL_MODULE_IS_LICENSED + " VARCHAR(1));");
		database.execSQL(query);
	}

	public void txnCodes() {
		String query = ("CREATE TABLE " + TABLE_MBS_TXN_CODES + " ("
				+ COL_TXN_TXN_CODES + " VARCHAR(25) NOT NULL , "
				+ COL_TXN_MODULE_CODES + " VARCHAR(10), " + COL_TXN_TXN_DESCS + " VARCHAR(100));");
		database.execSQL(query);
	}

	public void agnLoansV() {
		String query = ("CREATE  VIEW " + VIEW_AGN_LOANS + " AS SELECT "
				+ " AG. " + DbHandlerL.COL_AGENDA_AGENDA_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_SEQ_NO + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CBS_AC_REF_NO + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_MODULE_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_TXN_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENT_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_LOCATION_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_BRANCH_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CUSTOMER_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CUSTOMER_NAME + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CCY_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENDA_AMT + " , " + " IFNULL( "
				+ " AG. " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH
				+ ",'N')  AS " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH
				+ ", AG." + DbHandlerL.COL_AGENDA_CMP_NAME + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_DEVICE_ID + " , " + " IFNULL ( AG. "
				+ DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE + ", 'X') AS "
				+ DbHandlerL.COL_AGENDA_LN_DISBURSMENT_TYPE + " , AG. "
				+ DbHandlerL.COL_AGENDA_CMP_ST_DATE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CMP_END_DATE + " , " + " AG."
				+ DbHandlerL.COL_AGENDA_IS_GROUP_LOAN + ", AG."
				+ DbHandlerL.COL_AGENDA_IS_PARENT_LOAN + ", AG."
				+ DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER + ", AG."
				+ DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID + ", AG."
				+ DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO + ", AG."
				+ DbHandlerL.COL_AGENDA_SMS_M_MOBILE + " FROM "
				+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " AG " + " WHERE "
				+ " AG." + DbHandlerL.COL_AGENDA_MODULE_CODE + "='LN' "
				+ " AND AG." + DbHandlerL.COL_AGENDA_IS_PARENT_LOAN + "='Y' "
				+ " AND AG." + DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER
				+ "='Y' " + " AND AG." + DbHandlerL.COL_AGENDA_AGENDA_STATUS
				+ " IN ('0','1') " + " AND AG. "
				+ DbHandlerL.COL_AGENDA_TXN_CODE + " IN ('L01','L02','L21') "

		);
		database.execSQL(query);
	}

	public void agnDepositsV() {
		String query = ("CREATE VIEW " + VIEW_AGN_DEPOSITS + " AS SELECT "
				+ " AG. " + DbHandlerL.COL_AGENDA_AGENDA_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_SEQ_NO + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CBS_AC_REF_NO + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_MODULE_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_TXN_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENT_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_LOCATION_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_BRANCH_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CUSTOMER_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CUSTOMER_NAME + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CCY_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENDA_AMT + " , " + " IFNULL( "
				+ " AG. " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH
				+ ",'N') AS " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH + " , "
				+ "  AG. " + DbHandlerL.COL_AGENDA_CMP_NAME + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_DEVICE_ID + " " + ", IFNULL( "
				+ " AG. " + DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION
				+ ",'X')  AS " + DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_CMP_ST_DATE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CMP_END_DATE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_SMS_M_MOBILE + " FROM  "
				+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " AG " + " WHERE "
				+ " AG. " + DbHandlerL.COL_AGENDA_MODULE_CODE
				+ "='DP' AND AG. " + DbHandlerL.COL_AGENDA_AGENDA_STATUS
				+ " IN ('0','1')  AND  AG. " + DbHandlerL.COL_AGENDA_TXN_CODE + " IN ('D01','D02','D03') ");
		database.execSQL(query);
	}

	public void agnChildLoansV() {
		String query = ("CREATE VIEW " + VIEW_AGN_CHILD_LOANS + " AS SELECT "
				+ " AG. " + DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_CBS_AC_REF_NO + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_TXN_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CUSTOMER_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CUSTOMER_NAME + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CCY_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENDA_AMT + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CMP_NAME + " FROM "
				+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " AG " + " WHERE AG."
				+ DbHandlerL.COL_AGENDA_MODULE_CODE + "='LN' " + " AND AG."
				+ DbHandlerL.COL_AGENDA_IS_PARENT_LOAN + "='N' " + " AND AG."
				+ DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER + "='N' "
				+ " AND AG." + DbHandlerL.COL_AGENDA_IS_GROUP_LOAN + " ='Y' "
				+ " AND AG. " + DbHandlerL.COL_AGENDA_TXN_CODE + " IN ('L01','L02','L21') ");
		database.execSQL(query);
	}

	public void historyV() {
		String query = ("CREATE VIEW " + VIEW_MBS_HISTORY + " AS SELECT "
				+ " AG. " + DbHandlerL.COL_TXN_CBS_AC_REF_NO + " , " + " AG. "
				+ DbHandlerL.COL_TXN_TXN_ID + " , " + " AG. "
				+ DbHandlerL.COL_TXN_CUSTOMER_ID + " , " + " AG. "
				+ DbHandlerL.COL_TXN_SETTLED_AMT + " , " + " AG. "
				+ DbHandlerL.COL_TXN_IS_SYNCED + " , " + " AG. "
						+ DbHandlerL.COL_TXN_INIT_TIME + " , " + " AG. "
								+ DbHandlerL.COL_TXN_AC_TYPE + " FROM  "
				+ DbHandlerL.TABLE_MBS_TXN_MASTER + " AG " + " WHERE AG. "
				+ DbHandlerL.COL_TXN_IS_SYNCED + "  IN ('0','1') ");
		database.execSQL(query);
	}

	public void customersQueryV() {
		String query = ("CREATE VIEW " + VIEW_MBS_CUSTOMER_AGN_QUERY_V
				+ " AS SELECT " + " AG. " + DbHandlerL.COL_CUST_CUSTOMER_ID
				+ " , " + " AG. " + DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME
				+ " , " + " AG. " + DbHandlerL.COL_CUST_MOBILE_NUMBER
				+ " FROM  " + DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + " AG  ");
		database.execSQL(query);
	}

	public void customersV() {
		String query = ("CREATE VIEW " + VIEW_MBS_CUSTOMERS + " AS SELECT "
				+ " AG. " + DbHandlerL.COL_CUST_CUSTOMER_FULL_NAME + " , "
				+ " AG. " + DbHandlerL.COL_CUST_DOB + " , " + " AG. "
				+ DbHandlerL.COL_CUST_GENDER + " , " + " AG. "
				+ DbHandlerL.COL_CUST_MOBILE_NUMBER + " , " + " AG. "
				+ DbHandlerL.COL_CUST_ADDRESS_LINE1 + " , " + " AG. "
				+ DbHandlerL.COL_CUST_ADDRESS_LINE2 + " , " + " AG. "
				+ DbHandlerL.COL_CUST_ADDRESS_LINE3 + " , " + " AG. "
				+ DbHandlerL.COL_CUST_ADDRESS_LINE4 + " , " + " AG. "
				+ DbHandlerL.COL_CUST_CITY + " , " + " AG. "
				+ DbHandlerL.COL_CUST_STATE 
				+" ,  AG. "+ DbHandlerL.COL_CUST_CUSTOMER_ID 
				+ " ,  AG. "+ DbHandlerL.COL_CUST_COUNTRY
				+ " ,  AG. "+ DbHandlerL.COL_CUST_CUSTOMER_CATEGORY
				+ " ,  AG. "+ DbHandlerL.COL_CUST_ZIP_CODE
				+ " ,  AG. "+ DbHandlerL.COL_CUST_LOCATION_CODE
				+ " FROM  "
				+ DbHandlerL.TABLE_MBS_CUSTOMER_DETAIL + " AG  ");
		database.execSQL(query);
	}

	public void customersAgnV() {
		String query = ("CREATE VIEW " + VIEW_MBS_CUSTOMERS_AGN_ALL
				+ " AS SELECT " + " AG. " + DbHandlerL.COL_AGENDA_AGENDA_ID
				+ " , " + " AG. " + DbHandlerL.COL_AGENDA_SEQ_NO + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_CBS_AC_REF_NO + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_MODULE_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_TXN_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENT_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_LOCATION_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_BRANCH_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CUSTOMER_ID + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CUSTOMER_NAME + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_CCY_CODE + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_AGENDA_AMT + " , " + " IFNULL( "
				+ " AG. " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH
				+ ",'N')  AS " + DbHandlerL.COL_AGENDA_LN_IS_FUTURE_SCH
				+ " , AG." + DbHandlerL.COL_AGENDA_CMP_NAME + " , " + " AG. "
				+ DbHandlerL.COL_AGENDA_DEVICE_ID + " , " + " IFNULL( "
				+ " AG. " + DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION
				+ ",'N')  AS " + DbHandlerL.COL_AGENDA_DP_IS_REDEMPTION
				+ " , AG." + DbHandlerL.COL_AGENDA_IS_GROUP_LOAN + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_IS_PARENT_LOAN + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_PARENT_CUSTOMER_ID + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_PARENT_CBS_REF_AC_NO + " , "
				+ " AG. " + DbHandlerL.COL_AGENDA_SMS_M_MOBILE + " FROM  "
				+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " AG  " + " WHERE ("
				+ " (AG ." + DbHandlerL.COL_AGENDA_MODULE_CODE + " = "
				+ " 'DP' AND " + DbHandlerL.COL_AGENDA_TXN_CODE
				+ "  IN ('D01','D02','D03'))OR (AG ."
				+ DbHandlerL.COL_AGENDA_MODULE_CODE + " = " + " 'LN' AND "
				+ DbHandlerL.COL_AGENDA_TXN_CODE + "  IN ('L01','L02','L21')) "
				+ " AND " + DbHandlerL.COL_AGENDA_IS_PARENT_LOAN + " ='Y' "
				+ " AND " + DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER + " ='Y'"
				+ " AND " + DbHandlerL.COL_AGENDA_AGENDA_STATUS + " IN (0,1)) ");

		database.execSQL(query);
	}

	public void cashAccDetails() {
		String query = ("CREATE TABLE " + TABLE_MBS_CUSTACC_DETAIL + " ("
				+ COL_CUSTACC_CUSTOMER_ID + " VARCHAR(25) NOT NULL , "
				+ COL_CUSTACC_CUSTOMER_FULL_NAME + " VARCHAR(150), "
				+ COL_CUSTACC_CUST_AC_NO + " VARCHAR(25), "
				+ COL_CUSTACC_BRANCH_CODE + " VARCHAR(5), "
				+ COL_CUSTACC_ACCOUNT_TYPE + " VARCHAR(1), "
				+ COL_CUSTACC_AC_DESC + " VARCHAR(50), " + COL_CUSTACC_AC_CCY
				+ " VARCHAR(3), " + COL_CUSTACC_AC_STAT_NO_DR + " VARCHAR(1), "
				+ COL_CUSTACC_AC_STAT_NO_CR + " VARCHAR(1), "
				+ COL_CUSTACC_AC_STAT_BLOCK + " VARCHAR(1), "
				+ COL_CUSTACC_AC_STAT_STOP_PAY + " VARCHAR(1), "
				+ COL_CUSTACC_AC_STAT_DORMANT + " VARCHAR(1), "
				+ COL_CUSTACC_AGENT_ID + " VARCHAR(25), "
				+ COL_CUSTACC_LOCATION_CODE + " VARCHAR(25), "
				+ COL_CUSTACC_CREDIT_OFFICER_ID + " VARCHAR(25),PRIMARY KEY("
				+ COL_CUSTACC_CUSTOMER_ID + "," + COL_CUSTACC_CUST_AC_NO + "));");
		database.execSQL(query);
	}

	private void BiometricData() {

		String query = ("CREATE TABLE " + TABLE_MBS_BIOMETRIC_DATA + " ("
				+ COL_MBS_BIO_ENROLID + " VARCHAR(25) NOT NULL PRIMARY KEY, "
				+ COL_MBS_BIO_RHLF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_RHRF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_RHMF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_RHIF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_RHTF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_LHLF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_LHRF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_LHMF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_LHIF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_LHTF_TMPLTDATA + " BLOB, "
				+ COL_MBS_BIO_RHLF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_RHRF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_RHMF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_RHIF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_RHTF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_LHLF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_LHRF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_LHMF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_LHIF_SCANDATA + " BLOB, "
				+ COL_MBS_BIO_LHTF_SCANDATA + " BLOB);");
		database.execSQL(query);
	}

	private void smsTemplate() {
		String query = ("CREATE TABLE " + TABLE_MBS_SMS_TEMPLATE + " ("
				+ COL_MBS_SMS_TXN_CODE + " VARCHAR(25) NOT NULL PRIMARY KEY, "
				+ COL_MBS_SMS_TXN_SMS + " VARCHAR(300), "
				+ COL_MBS_SMS_DYN_FIELDS + " VARCHAR(10));");
		database.execSQL(query);
	}

	public void exportDBFile() {
		// Toast.makeText(_context, "Entered exportDBFile",
		// Toast.LENGTH_SHORT).show();
		java.io.File sourceFile = android.os.Environment.getDataDirectory();
		// java.io.File exportFile =
		// android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		java.io.File exportFile = android.os.Environment
				.getExternalStorageDirectory();

		java.nio.channels.FileChannel sourceChannel = null;
		java.nio.channels.FileChannel exportChannel = null;

		String BFSI_DB_Path = "/data/" + "com.bfsi.egalite.view"
				+ "/databases/" + "MFI_DB";
		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
		String format = s.format(new Date());
		String Export_DB_Path = DB_NAME + "_" + format;

		// Toast.makeText(_context,
		// "BFSI_DB_Path : "+BFSI_DB_Path+" : "+" DB_NAME : "+DB_NAME,
		// Toast.LENGTH_SHORT).show();

		try {
			exportFile.setWritable(true);
			File sourceDBFile = new File(sourceFile, BFSI_DB_Path);
			File exportDBFile = new File(exportFile, Export_DB_Path);
			sourceChannel = new FileInputStream(sourceDBFile).getChannel();
			exportChannel = new FileOutputStream(exportDBFile).getChannel();
			exportChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
			sourceChannel.close();
			exportChannel.close();
		} catch (Exception e) {
			Toast.makeText(_context, Constants.EXCEPTION + e.toString(),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		Toast.makeText(_context, Constants.EXIT_EXP_DB, Toast.LENGTH_SHORT)
				.show();
	}

//	public void agendaDashboardV() {
//		String query = ("CREATE VIEW "
//				+ VIEW_MBS_AGENDA_DASHBOARD_V
//				+ " AS SELECT "
//				+ " TXN_NAME,PENDING AS P,EXECUTED AS E, SYNCED AS S, (PENDING+EXECUTED+SYNCED) AS TOTAL FROM "
//				+ "( SELECT TXN_CODE, TXN_NAME,PENDING,EXECUTED, SYNCED FROM ( SELECT "
//				+ DbHandlerL.COL_AGENDA_TXN_CODE + ", (" + " CASE  WHEN "
//				+ DbHandlerL.COL_AGENDA_TXN_CODE + "='D01' THEN 'DPCOLL' "
//				+ " WHEN " + DbHandlerL.COL_AGENDA_TXN_CODE
//				+ "='D02' THEN 'DPMAT' " + " WHEN "
//				+ DbHandlerL.COL_AGENDA_TXN_CODE + "='D03' THEN 'DPRED' "
//				+ " ELSE NULL END) TXN_NAME, SUM( CASE WHEN "
//				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
//				+ "=0 THEN 1 ELSE 0 END)  as PENDING," + " SUM( CASE WHEN "
//				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
//				+ "=1 THEN 1 ELSE 0 END)  as EXECUTED," + " SUM( CASE WHEN "
//				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
//				+ "=2 THEN 1 ELSE 0 END)  as SYNCED" + " FROM "
//				+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " WHERE "
//				+ DbHandlerL.COL_AGENDA_TXN_CODE + " IN ('D01','D02','D03')"
//				+ " UNION " + " SELECT " + DbHandlerL.COL_AGENDA_TXN_CODE
//				+ ", (" + " CASE WHEN " + DbHandlerL.COL_AGENDA_TXN_CODE
//				+ "='L01' THEN 'LNDISBR'" + " WHEN "
//				+ DbHandlerL.COL_AGENDA_TXN_CODE + "='L02' THEN 'LNREPAY' "
//				+ " WHEN " + DbHandlerL.COL_AGENDA_TXN_CODE
//				+ "='L03' THEN 'LNPREPAY' "
//				+ "ELSE NULL END) TXN_NAME, SUM( CASE WHEN "
//				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
//				+ "=0 THEN 1 ELSE 0 END)  as PENDING," + " SUM( CASE WHEN "
//				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
//				+ "=1 THEN 1 ELSE 0 END)  as EXECUTED," + " SUM( CASE WHEN "
//				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
//				+ "=2 THEN 1 ELSE 0 END)  as SYNCED" + " FROM "
//				+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " WHERE "
//				+ DbHandlerL.COL_AGENDA_TXN_CODE + " IN ('L01','L02','L21')"
//				+ " AND " + DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER
//				+ "='Y' ) ALL_TXNS GROUP BY " + DbHandlerL.COL_AGENDA_TXN_CODE + ") ALL_TXNS_GROUPED;");
//
//		database.execSQL(query);
//	}
	
	public void agendaDashboardV() {
		String query = ("CREATE VIEW "
				+ VIEW_MBS_AGENDA_DASHBOARD_V
				+ " AS SELECT "
				+ " TXN_NAME,PENDING AS P,EXECUTED AS E, SYNCED AS S, (PENDING+EXECUTED+SYNCED) AS TOTAL FROM "
				+ "( SELECT TXN_CODE, ( CASE WHEN "
				+DbHandlerL.COL_AGENDA_TXN_CODE+"='D01' THEN 'DPCOLL' WHEN "
				+DbHandlerL.COL_AGENDA_TXN_CODE+"='D02' THEN 'DPMAT' WHEN "
				+DbHandlerL.COL_AGENDA_TXN_CODE+"='D03' THEN 'DPRED' WHEN "
				+DbHandlerL.COL_AGENDA_TXN_CODE+"='L01' THEN 'LNDISBR' WHEN "
				+DbHandlerL.COL_AGENDA_TXN_CODE+"='L02' THEN 'LNREPAY' WHEN " 
				+DbHandlerL.COL_AGENDA_TXN_CODE+"='L21' THEN 'LNPREPAY' ELSE NULL END)TXN_NAME,PENDING,EXECUTED, SYNCED FROM ( SELECT "
				+ DbHandlerL.COL_AGENDA_TXN_CODE + ", SUM( CASE WHEN "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
				+ "=0 THEN 1 ELSE 0 END)  as PENDING," + " SUM( CASE WHEN "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
				+ "=1 THEN 1 ELSE 0 END)  as EXECUTED," + " SUM( CASE WHEN "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
				+ "=2 THEN 1 ELSE 0 END)  as SYNCED" + " FROM "
				+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " WHERE "
				+ DbHandlerL.COL_AGENDA_TXN_CODE + " IN ('D01','D02','D03') GROUP BY "+DbHandlerL.COL_AGENDA_TXN_CODE
				+ " UNION " 
				+ " SELECT " + DbHandlerL.COL_AGENDA_TXN_CODE
				+ ", SUM( CASE WHEN "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
				+ "=0 THEN 1 ELSE 0 END)  as PENDING," + " SUM( CASE WHEN "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
				+ "=1 THEN 1 ELSE 0 END)  as EXECUTED," + " SUM( CASE WHEN "
				+ DbHandlerL.COL_AGENDA_AGENDA_STATUS
				+ "=2 THEN 1 ELSE 0 END)  as SYNCED" + " FROM "
				+ DbHandlerL.TABLE_MBS_AGENDA_MASTER + " WHERE "
				+ DbHandlerL.COL_AGENDA_TXN_CODE + " IN ('L01','L02','L21')"
				+ " AND " + DbHandlerL.COL_AGENDA_IS_PARENT_CUSTOMER
				+ "='Y' GROUP BY " + DbHandlerL.COL_AGENDA_TXN_CODE 
				+ ")ALL_TXNS" 
				+") ALL_TXNS_GROUPED;");

		database.execSQL(query);
	}
}
