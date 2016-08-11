package com.bfsi.egalite.service;

import com.bfsi.egalite.service.impl.TransactionServiceImpl;


/**
 * Service Factory to get the instances
 * @author Vijay
 *
 */
public class ServiceFactory {
	private static TransactionService txnService = new TransactionServiceImpl();

	public static TransactionService getTxnService() {
		return txnService;
	}

	
}
