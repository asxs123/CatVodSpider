package com.github.catvod.utils;

import android.util.Base64;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryption {


    public static final String CBC_PKCS_7_PADDING = "AES/CBC/PKCS7Padding";
    public static final String ECB_PKCS_7_PADDING = "AES/ECB/PKCS5Padding";

    public static String encrypt(String word, String keyString, String ivString,String trans) {
        try {
            byte[] keyBytes = keyString.getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            byte[] ivBytes = hexStringToByteArray(ivString);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(trans);
            if(StringUtils.isAllBlank(ivString)){
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            }else{
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            }

            byte[] encrypted = cipher.doFinal(word.getBytes("UTF-8"));

            return android.util.Base64.encode(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String word,String keyString,String ivString,String trans) {
        try {
            byte[] keyBytes = keyString.getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            byte[] ivBytes = hexStringToByteArray(ivString);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(trans);
            if(StringUtils.isAllBlank(ivString)){
                cipher.init(Cipher.DECRYPT_MODE, keySpec);

            }else{
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            }
            byte[] decoded = android.util.Base64.decode(word, android.util.Base64.DEFAULT);
            byte[] decrypted = cipher.doFinal(decoded);

            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }

        return data;
    }

    public static String encodeURIComponent(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding URL component", e);
        }
    }
}