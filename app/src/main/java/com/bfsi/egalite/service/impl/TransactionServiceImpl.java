package com.bfsi.egalite.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.TransactionDao;
import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.ServiceException;
import com.bfsi.egalite.service.TransactionService;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;

/**
 * Disbursement Transaction insertions and on success update CashView
 * 
 * @author Vijay
 * 
 */
public class TransactionServiceImpl extends BaseTransactionService implements
		TransactionService {

	private Logger LOG = LoggerFactory.getLogger(getClass());

	@SuppressLint("NewApi")
	@Override
	public long addTxn(TxnMaster txnMaster) throws ServiceException {
 		txnMaster.setTxnId(generateTransactionId(txnMaster.getTxnType()));
		CommonContexts.TXNID = txnMaster.getTxnId();
		TransactionDao txnDao = DaoFactory.getTxnDao();
		long status = -1;
		try {
			LOG.debug(Constants.EXCP_ADDDSBTXN);

			status = txnDao.insertTrasanction(txnMaster);
		} catch (Exception e) {
			LOG.error(Constants.EXCP_ADDDSBTXN
					+ e.getMessage());
			throw new ServiceException(
					Constants.ERR_INSR_DSB 
							+ e.getMessage(), e);
		}
		return status;
	}

	@Override
	public String getType() {
		return "DB";
	}

	@Override
	public String getCashTxnId(String type) throws ServiceException {
		String txnId = null;
		txnId = generateTransactionId(type);
		return txnId;
	}

}
