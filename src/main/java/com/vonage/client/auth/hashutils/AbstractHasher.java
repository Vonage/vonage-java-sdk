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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Base class for hashers (algorithm-agnostic).
 */
abstract class AbstractHasher {
    static final Charset ENCODING = StandardCharsets.UTF_8;

    /**
     * Calculates hash for string, assuming the string is UTF-8 encoded.
     *
     * @param input string which is going to be encoded into requested format.
     * @return Hashed representation of the input string.
     *
     * @throws NoSuchAlgorithmException if the algorithm is not available.
     * @throws InvalidKeyException Only applicable to HMAC encoding types, when a bad key is provided.
     */
    abstract String calculate(String input) throws NoSuchAlgorithmException, InvalidKeyException;

    /**
     * Calculates hash for string.
     *
     * @param input string which is going to be encoded into requested format.
     * @param secretKey The secret key for the input.
     *
     * @return The hashed representation of the input string.
     * @throws NoSuchAlgorithmException if the algorithm is not available.
     * @throws InvalidKeyException Only applicable to HMAC encoding types, when a bad key is provided.
     */
    String calculate(String input, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        return calculate(input + (secretKey == null ? "" : secretKey));
    }

    String buildHexString(byte[] digest) {
        final StringBuilder hexString = new StringBuilder();
        for (byte element : digest) {
            int z = 0xFF & element;
            if (z < 16)
                hexString.append("0");
            hexString.append(Integer.toHexString(z));
        }
        return hexString.toString();
    }
}
