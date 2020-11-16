/*
 *   Copyright 2020 Vonage
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Contains utility methods that use MD5 hashing. The class uses STANDARD JVM MD5 algorithm.
 */
class Md5Hasher extends AbstractHasher {

    /**
     * Calculates MD5 hash for string.
     * @param input string which is going to be encoded into MD5 format
     * @param encoding character encoding of the string which is going to be encoded into MD5 format
     * @return  MD5 representation of the input string
     * @throws NoSuchAlgorithmException if the MD5 algorithm is not available.
     * @throws UnsupportedEncodingException if the specified encoding is unavailable.
     */
    @Override public String calculate(String input, String encoding) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes(encoding));
        byte digest[] = md.digest();

        return buildHexString(digest);
    }

}
