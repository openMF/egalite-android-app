package com.bfsi.egalite.encryption;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.spongycastle.util.encoders.Base64;

public class AESEncrypt {
    private static final byte[] SALT = {(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03};
    private static final int ITERATION_COUNT = 4096;
    private static final int KEY_LENGTH = 256;
    private Cipher eCipher;
    private Cipher dCipher;
    private byte[] iv;

   public AESEncrypt(String passPhrase) throws Exception {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
        SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
        SecretKey secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");

        eCipher = Cipher.getInstance("AES/GCM/NoPadding");
        eCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        dCipher = Cipher.getInstance("AES/GCM/NoPadding");
        iv = eCipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
        dCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    }

    public String encrypt(String encrypt) throws Exception {
        byte[] bytes = encrypt.getBytes("UTF8");
        byte[] encrypted = encrypt(bytes);
        byte[] cipherText = new byte[encrypted.length + iv.length];
        System.arraycopy(iv, 0, cipherText, 0, iv.length);
        System.arraycopy(encrypted, 0, cipherText, iv.length, encrypted.length);
        return new String(Base64.encode(cipherText));
    }

    public byte[] encrypt(byte[] plain) throws Exception {
        return eCipher.doFinal(plain);
    }

    
}
