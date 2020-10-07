package com.vonage.client.auth.hashutils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Contains utility methods that use HMAC SHA-256 hashing. The class uses STANDARD JVM crypto Hmac SHA-256 algorithm.
 */
public class HmacSha256Hasher extends AbstractHasher {

    @Override public String calculate(String input, String secretKey, String encoding) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(encoding), "HmacSHA256");

        sha256HMAC.init(keySpec);

        byte[] digest = sha256HMAC.doFinal(input.getBytes(encoding));

        return this.buildHexString(digest);
    }

    @Override
    public String calculate(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            return this.calculate(input, "", encoding);
        } catch (InvalidKeyException e) {
            return null; // should not occur
        }
    }
}
