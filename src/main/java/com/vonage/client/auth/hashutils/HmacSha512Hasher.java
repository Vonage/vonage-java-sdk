package com.vonage.client.auth.hashutils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Contains utility methods that use HMAC SHA-512 hashing. The class uses STANDARD JVM crypto Hmac SHA-512 algorithm.
 */
public class HmacSha512Hasher extends AbstractHasher {

    /**
     * Calculates HMAC SHA-512 hash for string.
     * @param input string which is going to be encoded into HMAC SHA-512 format
     * @param secretKey The key used for initialization of the algorithm
     * @param encoding character encoding of the string which is going to be encoded into HMAC SHA-512 format
     * @return  HMAC SHA-512 representation of the input string
     * @throws NoSuchAlgorithmException if the HMAC SHA-512 algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     */
    @Override public String calculate(String input, String secretKey, String encoding) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac sha512HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(encoding), "HmacSHA512");

        sha512HMAC.init(keySpec);

        byte[] digest = sha512HMAC.doFinal(input.getBytes(encoding));

        return buildHexString(digest);
    }

    /**
     * Calculates HMAC SHA-512 hash for string.
     * Secret key that is supplied here is the input itself.
     *
     * @param input string which is going to be encoded into HMAC SHA-512 format
     * @param encoding character encoding of the string which is going to be encoded into HMAC SHA-512 format
     * @return  HMAC SHA-512 representation of the input string
     * @throws NoSuchAlgorithmException if the HMAC SHA-512 algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     * @throws InvalidKeyException if key is invalid
     */
    @Override public String calculate(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        return calculate(input, input, encoding);
    }
}
