package com.vonage.client.auth.hashutils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Contains utility methods that use SHA-256 hashing. The class uses STANDARD JVM SHA-256 algorithm.
 */
class SHA256Util extends Hasher {

    /**
     * Calculates SHA-256 hash for string. assume string is UTF-8 encoded.
     * @param input string which is going to be encoded into SHA-256 format
     * @return  SHA-256 representation of the input string
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     */
    @Override public String calculate(String input) throws NoSuchAlgorithmException {
        try {
            return calculate(input, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null; // -- impossible --
        }
    }

    /**
     * Calculates SHA-256 hash for string.
     * @param input string which is going to be encoded into SHA-256 format
     * @param encoding character encoding of the string which is going to be encoded into SHA-256 format
     * @return  SHA-256 representation of the input string
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     */
    @Override public String calculate(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes(encoding));

        return Base64.getEncoder().encodeToString(md.digest());
    }
}
