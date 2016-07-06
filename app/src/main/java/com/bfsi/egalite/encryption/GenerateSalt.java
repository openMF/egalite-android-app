package com.bfsi.egalite.encryption;

import java.security.SecureRandom;

public class GenerateSalt {

	public static final int SALT_BYTE_SIZE = 32;
	public static byte[] getSalt()
	{
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);
        return salt;
	}

}
