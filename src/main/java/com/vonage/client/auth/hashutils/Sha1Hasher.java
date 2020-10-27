package com.vonage.client.auth.hashutils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Contains utility methods that use SHA-1 hashing. The class uses STANDARD JVM MD5 algorithm.
 */
public class Sha1Hasher extends AbstractHasher {

    /**
     * Calculates SHA-1 hash for string.
     * @param input string which is going to be encoded into SHA-1 format
     * @param encoding character encoding of the string which is going to be encoded into MD5 format
     * @return  SHA-1 representation of the input string
     * @throws NoSuchAlgorithmException if the SHA-1 algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     */
    @Override public String calculate(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(input.getBytes(encoding));
        byte digest[] = md.digest();

        return this.buildHexString(digest);
    }
}
