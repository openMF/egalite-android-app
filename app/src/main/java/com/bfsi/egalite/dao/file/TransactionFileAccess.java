package com.bfsi.egalite.dao.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bfsi.egalite.entity.TxnMaster;
import com.bfsi.egalite.exceptions.DataAccessException;
import com.bfsi.egalite.util.CommonContexts;
import com.bfsi.egalite.util.Constants;
import com.bfsi.egalite.util.SystemUtil;
import com.google.gson.Gson;

/**
 * Reads / writes transaction record from / to file
 * 
 * @author vijay
 * 
 */
public class TransactionFileAccess {
	private static TransactionFileAccess instance = new TransactionFileAccess();
	private Logger LOG = LoggerFactory.getLogger(getClass());

	private TransactionFileAccess() {
	}

	public static TransactionFileAccess getInstance() {
		return instance;
	}

	/**
	 * @param Txn
	 * @throws Exception
	 */
	public void write(TxnMaster txnMaster) {
		Gson gson = new Gson();
		String json = gson.toJson(txnMaster);

		File dir = CommonContexts
				.getSdFileDirectory(CommonContexts.TRANSACTION_FILE_DIRECTORY);
		try {
			LOG.debug("Writing transaction  : TransactionFileAccess ");
			// String plainDataKey =CryptoDataAccess.getDataKey();
			// String jsonEncrypted =
			// CryptoDataAccess.getEncryptedString(plainDataKey,json);
			SystemUtil.writeToFile(json, dir, txnMaster.getTxnId());
		} catch (FileNotFoundException e) {
			LOG.error(Constants.ERR_WRITING_TRANSACTION
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(Constants.ERR_WRITING_TRANSACTION
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants. ERR_WRITING_TRANSACTION
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	/**
	 * Reads content from external file
	 * 
	 * @param dir
	 * @param filename
	 * @return Txn
	 */
	public TxnMaster read(String txnReference) throws DataAccessException {
		TxnMaster txnMaster = null;
		File dir = CommonContexts
				.getSdFileDirectory(CommonContexts.TRANSACTION_FILE_DIRECTORY);
		try {
			LOG.debug(Constants.READ_TXN_FILE);
			String jString = SystemUtil.readFromFile(dir, txnReference);
			// String plainDataKey =CryptoDataAccess.getDataKey();
			// String decryptedData =
			// CryptoDataAccess.getDecryptedString(plainDataKey, jString);
			Gson gson = new Gson();
			txnMaster = gson.fromJson(jString, TxnMaster.class);
		} catch (FileNotFoundException e) {
			LOG.error(Constants.ERR_READING_TRANSACTION__FILE
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(Constants.ERR_READING_TRANSACTION__FILE
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(Constants.ERR_READING_TRANSACTION__FILE
					+ e.getMessage());
			throw new DataAccessException(e.getMessage(), e);
		}
		return txnMaster;
	}

}
