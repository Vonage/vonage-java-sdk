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
 * Contains utility methods that use HMAC MD-5 hashing. The class uses STANDARD JVM crypto Hmac SHA-512 algorithm.
 */
class HmacMd5Hasher extends AbstractHasher {

    /**
     * Calculates HMAC MD-5 hash for string.
     *
     * @param input string which is going to be encoded into HMAC MD-5 format.
     * @param secretKey The key used for initialization of the algorithm.

     * @return HMAC MD-5 representation of the input string
     * @throws NoSuchAlgorithmException if the HMAC MD-5 algorithm is not available.
     */
    @Override
    String calculate(String input, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha1HMAC = Mac.getInstance("HmacMD5");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(ENCODING), "HmacMD5");
        sha1HMAC.init(keySpec);
        byte[] digest = sha1HMAC.doFinal(input.getBytes(ENCODING));
        return buildHexString(digest);
    }

    /**
     * Calculates HMAC MD-5 hash for string.
     * Secret key that is supplied here is the input itself.
     *
     * @param input string which is going to be encoded into HMAC MD-5 format.
     *
     * @return HMAC MD-5 representation of the input string.
     *
     * @throws NoSuchAlgorithmException if the HMAC MD-5 algorithm is not available.
     * @throws InvalidKeyException if key is invalid.
     */
    @Override
    String calculate(String input) throws NoSuchAlgorithmException, InvalidKeyException {
        return calculate(input, input);
    }
}
