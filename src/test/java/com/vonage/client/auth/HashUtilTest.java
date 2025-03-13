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
package com.vonage.client.auth;

import com.vonage.client.TestUtils;
import static com.vonage.client.auth.hashutils.HashType.*;
import static com.vonage.client.auth.hashutils.HashUtil.calculate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;

public class HashUtilTest {
    private static final String
            ENCODING = "UTF-8",
            INPUT = TestUtils.SIGNATURE_SECRET,
            SECRET = TestUtils.API_SECRET;

    @Test
    public void testHmacSHA512() throws Exception {
        assertEquals(
                "fab480a8dfe36b080040aa2f9d0daab08792191859be0b94ca9ad8694b57e50dc5e3a8a63cdfb011cafd1c1325e98f99b26d79c8f3e7ee08a0eb2468d8b1c52f",
                calculate(INPUT, SECRET, ENCODING, HMAC_SHA512)
        );
        assertEquals(
                "f307da51c08e3c0178c5656fc4c8381d927cf6b824421410f9a0747623e85f8068703eb71b1aae9302bc0427708ce408c57d968b884002bad32ca65bc633701f",
                calculate(INPUT, HMAC_SHA512)
        );
    }

    @Test
    public void testHmacSHA256() throws Exception {
        assertEquals(
                "31b4c132cf06b5facbb7c24dbbcf1e3d1ca1bad99a88da1c4c1f5a6a9e25b0f3",
                calculate(INPUT, SECRET, ENCODING, HMAC_SHA256)
        );
        assertEquals(
                "6e51c1005f105f8a4942930f36c358b2b9ec24e3a81712bc9f4905f09e0df57f",
                calculate(INPUT, HMAC_SHA256)
        );
    }

    @Test
    public void testHmacSHA1() throws Exception {
        assertEquals(
                "e6051a57a2722e32857b97104d1d69df98f60aae",
                calculate(INPUT, SECRET, ENCODING, HMAC_SHA1)
        );
        assertEquals(
                "5dbb7e3fd8f1afd52acd9de452816f78dfc08c0a",
                calculate(INPUT, HMAC_SHA1)
        );
    }

    @Test
    public void testHmacMD5() throws Exception {
        assertEquals(
                "8c565cbc1f5319125f3a27fe3f724b61",
                calculate(INPUT, SECRET, ENCODING, HMAC_MD5)
        );
        assertEquals(
                "4b6729144b978205bac91b99bfb16ef2",
                calculate(INPUT, HMAC_MD5)
        );
    }

    @Test
    public void testMD5() throws Exception {
        var expected = "0d691d68dcb25ec98d312f8902c0020a";
        assertEquals(expected, calculate(INPUT, ENCODING, null, MD5));
        assertEquals(expected, calculate(INPUT, MD5));
    }
}
