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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Contains utility methods that use HMAC SHA-512 hashing. The class uses STANDARD JVM crypto Hmac SHA-512 algorithm.
 */
class HmacSha512Hasher extends AbstractHasher {

    /**
     * Calculates HMAC SHA-512 hash for string.
     *
     * @param input string which is going to be encoded into HMAC SHA-512 format.
     * @param secretKey The key used for initialization of the algorithm.

     * @return  HMAC SHA-512 representation of the input string.
     *
     * @throws NoSuchAlgorithmException if the HMAC SHA-512 algorithm is not available.
     */
    @Override
    String calculate(String input, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha512HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(ENCODING), "HmacSHA512");
        sha512HMAC.init(keySpec);
        byte[] digest = sha512HMAC.doFinal(input.getBytes(ENCODING));
        return buildHexString(digest);
    }

    /**
     * Calculates HMAC SHA-512 hash for string.
     * Secret key that is supplied here is the input itself.
     *
     * @param input string which is going to be encoded into HMAC SHA-512 format.

     * @return HMAC SHA-512 representation of the input string.
     *
     * @throws NoSuchAlgorithmException if the HMAC SHA-512 algorithm is not available.
     * @throws InvalidKeyException if key is invalid
     */
    @Override
    String calculate(String input) throws NoSuchAlgorithmException, InvalidKeyException {
        return calculate(input, input);
    }
}
