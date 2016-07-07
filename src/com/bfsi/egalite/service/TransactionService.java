package com.bfsi.egalite.service;

import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.ServiceException;

/**
 * All use cases related to disbursement
 * 
 * @author Administrator
 * 
 */
public interface TransactionService {
	/**
	 * @param dsbTxn
	 * @return
	 */
	long addTxn(TxnMaster dsbTxn) throws ServiceException;

	String getCashTxnId(String type) throws ServiceException;



}
