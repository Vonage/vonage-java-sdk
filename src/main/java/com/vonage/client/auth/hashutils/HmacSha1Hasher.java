/*
 *   Copyright 2025 Vonage
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.vonage.client.auth.hashutils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Contains utility methods that use HMAC SHA-1 hashing. The class uses STANDARD JVM crypto Hmac SHA-512 algorithm.
 */
public class HmacSha1Hasher extends AbstractHasher {

    /**
     * Calculates HMAC SHA-1 hash for string.
     *
     * @param input string which is going to be encoded into HMAC SHA-1 format
     * @param secretKey The key used for initialization of the algorithm
     * @param encoding character encoding of the string which is going to be encoded into HMAC SHA-1 format
     * @return  HMAC SHA-1 representation of the input string
     * @throws NoSuchAlgorithmException if the HMAC SHA-1 algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     */
    @Override
    public String calculate(String input, String secretKey, String encoding) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac sha1HMAC = Mac.getInstance("HmacSHA1");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(encoding), "HmacSHA1");

        sha1HMAC.init(keySpec);

        byte[] digest = sha1HMAC.doFinal(input.getBytes(encoding));

        return buildHexString(digest);
    }

    /**
     * Calculates HMAC SHA-1 hash for the input string.
     * Secret key that is supplied here is the input itself.
     *
     * @param input string which is going to be encoded into HMAC SHA-1 format.
     * @param encoding character encoding of the string which is going to be encoded into HMAC SHA-1 format.
     *
     * @return HMAC SHA-1 representation of the input string
     * @throws NoSuchAlgorithmException if the HMAC SHA-1 algori.thm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     * @throws InvalidKeyException if key is invalid.
     */
    @Override
    public String calculate(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        return calculate(input, input, encoding);
    }
}
