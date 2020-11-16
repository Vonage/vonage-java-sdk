package com.vonage.client.auth.hashutils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Contains utility methods that use HMAC MD-5 hashing. The class uses STANDARD JVM crypto Hmac SHA-512 algorithm.
 */
public class HmacMd5Hasher extends AbstractHasher {

    /**
     * Calculates HMAC MD-5 hash for string.
     * @param input string which is going to be encoded into HMAC MD-5 format
     * @param secretKey The key used for initialization of the algorithm
     * @param encoding character encoding of the string which is going to be encoded into HMAC MD-5 format
     * @return  HMAC MD-5 representation of the input string
     * @throws NoSuchAlgorithmException if the HMAC MD-5 algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     */
    @Override public String calculate(String input, String secretKey, String encoding) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac sha1HMAC = Mac.getInstance("HmacMD5");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(encoding), "HmacMD5");

        sha1HMAC.init(keySpec);

        byte[] digest = sha1HMAC.doFinal(input.getBytes(encoding));

        return buildHexString(digest);
    }

    /**
     * Calculates HMAC MD-5 hash for string.
     * Secret key that is supplied here is the input itself.
     *
     * @param input string which is going to be encoded into HMAC MD-5 format
     * @param encoding character encoding of the string which is going to be encoded into HMAC MD-5 format
     * @return  HMAC MD-5 representation of the input string
     * @throws NoSuchAlgorithmException if the HMAC MD-5 algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     * @throws InvalidKeyException if key is invalid
     */
    @Override public String calculate(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        return calculate(input, input, encoding);
    }
}
