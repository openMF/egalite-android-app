package com.bfsi.egalite.util;

import java.util.Date;

import com.bfsi.egalite.dao.CustomerDao;
import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.entity.SmsContent;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.mfi.rest.model.SmsTemplate;

/**
 * Construct to build the SMS text to be sent across to end customer
 * @author Vijay
 *
 */
public class MessageBuilderUtil {
	
	public String buildMessage(String custId,String txnCode,Object obj)
	{
		String finalText = "";
		CustomerDao dao = DaoFactory.getCustomerDao();
		String smsRequired = dao.smsRequired(custId);
		if(smsRequired!= null && smsRequired.equalsIgnoreCase("Y"))
		{
			PreDataDao daos = DaoFactory.getPreDataDao();
			SmsTemplate smsTemple = daos.readSmsTemplate(txnCode);
			if(smsTemple != null && smsTemple.getTxnSms() != null)
			{
				
				SmsContent content  = getSmsContent( obj);
				finalText = buildMsg(smsTemple.getTxnSms(),content);
				System.out.println(finalText);
				return finalText;
			}
		}
		return finalText;
	}
	private String buildMsg(String p_rawMsg, SmsContent mtxn) {

		p_rawMsg = p_rawMsg.replaceAll("AGENTID", mtxn.getAgentId());
		p_rawMsg = p_rawMsg.replaceAll("TXNAMOUNT", mtxn.getTxnAmount());
		p_rawMsg = p_rawMsg.replaceAll("AMOUNT", mtxn.getTxnAmount());
		p_rawMsg = p_rawMsg.replaceAll("BUSINESSDATE", mtxn.getBusinessDate());
		p_rawMsg = p_rawMsg.replaceAll("CBSACREFNO", mtxn.getCbsAcRefNo());
		p_rawMsg = p_rawMsg.replaceAll("CCY ",mtxn.getCcy());
		p_rawMsg = p_rawMsg.replaceAll("CUSTOMERID", mtxn.getCustomerId());
		p_rawMsg = p_rawMsg.replaceAll("TXNID ", mtxn.getTxnId());
		p_rawMsg = p_rawMsg.replaceAll("TXNTIME", mtxn.getTxnTime());
		p_rawMsg = p_rawMsg.replaceAll("TXNCODE", mtxn.getTxnCode());
		p_rawMsg = p_rawMsg.replaceAll("AVAILBAL", mtxn.getAvailable());
		p_rawMsg = p_rawMsg.replaceAll("ACC", mtxn.getCbsAcRefNo());
		p_rawMsg = p_rawMsg.replaceAll("DATE", (DateUtil.getCurrentDataTime()!=0?CommonContexts.dateFormat.format(new Date(DateUtil.getCurrentDataTime())):"0"));
		p_rawMsg = p_rawMsg.replaceAll("CUSTNAME", mtxn.getCustName()!= null?mtxn.getCustName():"Customer");

		return p_rawMsg;
	}
	public SmsContent getSmsContent(Object obj)
	{
		SmsContent smsContent = null;
		if(obj instanceof TxnMaster)
		{
			smsContent  = new SmsContent();
			smsContent.setAgentId(((TxnMaster) obj).getAgentId());
			smsContent.setBusinessDate(CommonContexts.dateFormat.format(new Date(DateUtil.getCurrentDataTime())));
			smsContent.setCbsAcRefNo(((TxnMaster) obj).getCbsAcRefNo());
			smsContent.setCcy(((TxnMaster) obj).getCcyCode());
			smsContent.setCustomerId(((TxnMaster) obj).getCustomerId());
			smsContent.setTxnAmount(((TxnMaster) obj).getTxnAmt());
			smsContent.setTxnCode(((TxnMaster) obj).getTxnCode());
			smsContent.setTxnId(CommonContexts.TXNID);
			smsContent.setCustName(((TxnMaster) obj).getCustomerName());
			smsContent.setTxnTime(CommonContexts.dateFormat.format(new Date(DateUtil.getCurrentDataTime())));
			return smsContent;
		}
		
		return smsContent;
	}

}
