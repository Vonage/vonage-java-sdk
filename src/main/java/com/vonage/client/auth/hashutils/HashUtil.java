/*
 *   Copyright 2024 Vonage
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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for hashing strings.
 */
public class HashUtil {

    private static final Map<HashType, AbstractHasher> HASH_TYPES;

    static {
        HASH_TYPES = new HashMap<>(8);
        HASH_TYPES.put(HashType.MD5, new Md5Hasher());
        HASH_TYPES.put(HashType.HMAC_SHA1, new HmacSha1Hasher());
        HASH_TYPES.put(HashType.HMAC_MD5, new HmacMd5Hasher());
        HASH_TYPES.put(HashType.HMAC_SHA256, new HmacSha256Hasher());
        HASH_TYPES.put(HashType.HMAC_SHA512, new HmacSha512Hasher());
    }

    /**
     * Calculates hash for string. assume string is UTF-8 encoded.
     *
     * @param input string which is going to be encoded into requested format
     * @param hashType The type of hash to be applied to the input string
     * @return representation of the input string with given hash type
     * @throws NoSuchAlgorithmException if the algorithm is not available.
     * @throws InvalidKeyException Only applicable to HMAC encoding types, when a bad key is provided.
     */
    public static String calculate(String input, HashType hashType) throws NoSuchAlgorithmException, InvalidKeyException {
        return HASH_TYPES.get(hashType).calculate(input);
    }

    /**
     * Calculates hash for string.
     *
     * @param input string which is going to be encoded into requested format
     * @param encoding encoding type of input
     * @param hashType The type of hash to be applied to the input string
     * @return representation of the input string with given hash type
     * @throws NoSuchAlgorithmException if the algorithm is not available.
     * @throws InvalidKeyException Only applicable to HMAC encoding types, when a bad key is provided.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     */
    public static String calculate(String input, String encoding, HashType hashType) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        return HASH_TYPES.get(hashType).calculate(input, encoding);
    }

    /**
     * Calculates hash for string.
     * @param input string which is going to be encoded into requested format
     * @param secretKey the key to be used for encoding
     * @param encoding character encoding of the string which is going to be encoded into requested format
     * @param hashType The type of hash to be applied to the input string
     * @return representation of the input string with given hash type
     * @throws NoSuchAlgorithmException if the algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     * @throws InvalidKeyException Only applicable to HMAC encoding types, when a bad key is provided.
     */
    public static String calculate(String input, String secretKey, String encoding, HashType hashType) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        return HASH_TYPES.get(hashType).calculate(input, secretKey, encoding);
    }

    public enum HashType {
        MD5,
        HMAC_SHA1,
        HMAC_MD5,
        HMAC_SHA256,
        HMAC_SHA512
    }
}
