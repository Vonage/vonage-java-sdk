package com.nexmo.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

/**
 * MD5Util.java<br><br>
 *
 * Contains utility methods that use MD5 hashing. The class uses STANDARD JVM MD5 algorithm<br><br>
 *
 * Created on 5 January 2011, 17:34
 *
 * @author  Paul Cook
 * @version 1.0
 */
public class MD5Util {

    /**
     * Calculates MD5 hash for string. assume string is UTF-8 encoded
     * @param input string which is going to be encoded into MD5 format
     * @return  MD5 representation of the input string
     * @throws NoSuchAlgorithmException
     */
    public static String calculateMd5(String input) throws NoSuchAlgorithmException {
        try {
            return calculateMd5(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null; // -- impossible --
        }
    }

    /**
     * Calculates MD5 hash for string.
     * @param input string which is going to be encoded into MD5 format
     * @param encoding character encoding of the string which is going to be encoded into MD5 format
     * @return  MD5 representation of the input string
     * @throws NoSuchAlgorithmException
     */
    public static String calculateMd5(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes(encoding));
        byte digest[] = md.digest();

        final StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            int z = 0xFF & digest[i];
            if (z < 16)
                hexString.append("0");
            hexString.append(Integer.toHexString(z));
        }

        return hexString.toString();
    }

}
