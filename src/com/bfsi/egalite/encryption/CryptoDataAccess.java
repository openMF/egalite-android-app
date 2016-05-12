package com.bfsi.egalite.encryption;

import com.bfsi.egalite.dao.DaoFactory;
import com.bfsi.egalite.dao.PreDataDao;
import com.bfsi.egalite.entity.Agent;
import com.bfsi.egalite.exceptions.DataAccessException;

public class CryptoDataAccess {

	public static String getDataKey()
	{
		String plainDataKey = null;
		PreDataDao preDataDao = DaoFactory.getPreDataDao();
		Agent agent = preDataDao.readAgentValues();
		String datakey = agent.getDataKey();
		String oldpassword = agent.getPassword();
		try {
			if(datakey != null && oldpassword != null)
			{
				AESDecrypt aesDecrypt = new AESDecrypt(oldpassword, datakey);
				plainDataKey = aesDecrypt.decrypt();
			}
		} catch (Exception e) {
			
			throw new DataAccessException(e.getMessage(),e);
		} 
		return plainDataKey;
	}
	
	public static String getEncryptedString(String datakey,String json)
	{
		String jsonEncrypted  = null;
		try {
			if(datakey != null && json != null)
			{
				AESEncrypt aesEncrypt = new AESEncrypt(datakey);
				jsonEncrypted = aesEncrypt.encrypt(json);
			}
		} catch (Exception e) {
			
			throw new DataAccessException(e.getMessage(),e);
		}
		return jsonEncrypted;
	}
	public static String getDecryptedString(String datakey,String json)
	{
		String jsonDecrypted  = null;
		try {
			AESDecrypt aesEncrypt = new AESDecrypt(datakey,json);
			jsonDecrypted = aesEncrypt.decrypt();  
		} catch (Exception e) {
			
			throw new DataAccessException(e.getMessage(),e);
		}
		return jsonDecrypted;
	}
}
