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

import com.vonage.client.VonageUnexpectedException;
import static com.vonage.client.auth.RequestSigning.*;
import com.vonage.client.auth.hashutils.HashType;
import static com.vonage.client.auth.hashutils.HashType.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestSigningTest {
    final String secret = "abcde";
    final Map<String, String> inputParams = new LinkedHashMap<>(4);

    @BeforeEach
    public void beforeTest() {
        inputParams.clear();
        inputParams.put("a", "alphabet");
        inputParams.put("b", "bananas");
    }

    void assertEqualsSignature(HashType hashType, String sig) {
        var result = constructSignatureForRequestParameters(inputParams, secret, 2100, hashType);
        assertEquals(sig, result.get(PARAM_SIGNATURE));
    }

    @Test
    public void testConstructSignatureForRequestParameters() {
        String expected = "7d43241108912b32cc315b48ce681acf";
        assertEqualsSignature(MD5, expected);
        var constructed = constructSignatureForRequestParameters(inputParams, secret, 2100, MD5);
        assertNotEquals(expected, inputParams.get(PARAM_SIGNATURE));
        assertNotEquals(constructed, constructSignatureForRequestParameters(inputParams, null, 2100, HMAC_SHA256));
    }

    @Test
    public void testConstructSignatureForRequestParametersWithSha1Hash() {
        assertEqualsSignature(HMAC_SHA1, "b7f749de27b4adcf736cc95c9a7e059a16c85127");
    }

    @Test
    public void testConstructSignatureForRequestParametersWithHmacMd5Hash() {
        assertEqualsSignature(HMAC_MD5, "e0afe267aefd6dd18a848c1681517a19");
    }

    @Test
    public void testConstructSignatureForRequestParametersWithHmacSha256Hash() {
        assertEqualsSignature(HMAC_SHA256, "8d1b0428276b6a070578225914c3502cc0687a454dfbbbb370c76a14234cb546");
    }

    @Test
    public void testConstructSignatureForRequestParametersWithHmacSha512Hash() {
        assertEqualsSignature(HMAC_SHA512, "1c834a1f6a377d4473971387b065cb38e2ad6c4869ba77b7b53e207a344e87ba04b456dfc697b371a2d1ce476d01dafd4394aa97525eff23badad39d2389a710");
    }

    @Test
    public void testConstructSignatureForRequestParametersSkipsSignature() {
        String sig = "7d43241108912b32cc315b48ce681acf";
        inputParams.put("sig", sig);
        assertEqualsSignature(MD5, sig);
    }

    @Test
    public void testConstructSignatureForRequestParametersSkipsNullAndEmptyValues() {
        String sig = "a3368bf718ba104dcb392d8877e8eb2b";
        inputParams.put("b", null);
        assertEqualsSignature(MD5, sig);
        inputParams.put("b", "  ");
        assertEqualsSignature(MD5, sig);
    }

    @Test
    public void testVerifyRequestSignature() {
        assertTrue(verifySignature(constructDummyParams()));
    }

    @Test
    public void testVerifyRequestSignatureTimestampDeltaBounds() {
        var params = constructDummyParamsNoTimestamp();
        int min = 2100 - (MAX_ALLOWABLE_TIME_DELTA / 1000), max = 2100 + (MAX_ALLOWABLE_TIME_DELTA / 1000);
        params.put("timestamp", new String[]{String.valueOf(min)});
        assertFalse(verifyRequestSignature(APPLICATION_JSON, null, params, secret, 2100000, MD5));
        params.put("timestamp", new String[]{String.valueOf(max)});
        assertFalse(verifyRequestSignature(APPLICATION_JSON, null, params, secret, 2100000, MD5));
        params.put("timestamp", new String[]{String.valueOf(min - 1)});
        assertFalse(verifyRequestSignature(APPLICATION_JSON, null, params, secret, 2100000, MD5));
        params.put("timestamp", new String[]{String.valueOf(max + 1)});
        assertFalse(verifyRequestSignature(APPLICATION_JSON, null, params, secret, 2100000, MD5));
    }

    @Test
    public void testVerifyRequestSignatureWithSha1Hash() {
        var params = constructDummyParamsNoSignature();
        params.put("sig", new String[]{"b7f749de27b4adcf736cc95c9a7e059a16c85127"});
        assertTrue(verifyRequestSignature(APPLICATION_JSON, null, params, secret, 2100000, HMAC_SHA1));
    }

    @Test
    public void testVerifyRequestSignatureJson() throws Exception {
        var params = constructDummyParams();
        var request = constructDummyRequestJson();
        assertTrue(verifyRequestSignature(APPLICATION_JSON, request, params, secret, 2100000, HMAC_SHA1));
        params.put("c", new String[]{" "});
        assertThrows(VonageUnexpectedException.class, () ->
                verifyRequestSignature(APPLICATION_JSON, request, params, secret, 2100000, HMAC_SHA1)
        );
    }

    @Test
    public void testVerifyRequestSignatureNonJson() {
        var params = constructDummyParamsAltSignature();
        params.put("c", new String[]{" "});
        assertTrue(verifyRequestSignature("text/plain", null, params, secret, 2100000, HMAC_SHA1));
        assertTrue(verifyRequestSignature("text/plain", null, params, secret, 2100000, HMAC_SHA1));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacSha256Hash() {
        var params = constructDummyParamsNoSignature();
        params.put("sig", new String[]{"8d1b0428276b6a070578225914c3502cc0687a454dfbbbb370c76a14234cb546"});
        assertTrue(verifyRequestSignature(APPLICATION_JSON, null, params, secret, 2100000, HMAC_SHA256));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacSha256NoSecret() {
        var params = constructDummyParamsNoSignature();
        params.put("sig", new String[]{"8d1b0428276b6a070578225914c3502cc0687a454dfbbbb370c76a14234cb546"});
        assertFalse(verifyRequestSignature(APPLICATION_JSON, null, params, null, 2100000, HMAC_SHA256));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacMd5Hash() {
        var params = constructDummyParamsNoSignature();
        params.put("sig", new String[]{"e0afe267aefd6dd18a848c1681517a19"});
        assertTrue(verifyRequestSignature(APPLICATION_JSON, null, params, secret, 2100000, HMAC_MD5));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacSha512Hash() {
        var params = constructDummyParamsNoSignature();
        params.put("sig", new String[]{"1c834a1f6a377d4473971387b065cb38e2ad6c4869ba77b7b53e207a344e87ba04b456dfc697b371a2d1ce476d01dafd4394aa97525eff23badad39d2389a710"});
        assertTrue(verifyRequestSignature(APPLICATION_JSON, null, params, secret, 2100000, HMAC_SHA512));
    }

    @Test
    public void testVerifyRequestSignatureNoSig() {
        assertFalse(verifySignature(constructDummyParamsNoSignature()));
    }

    @Test
    public void testVerifyRequestSignatureBadTimestamp() {
        assertFalse(verifySignature(constructDummyParamsInvalidTimestamp()));
    }

    @Test
    public void testVerifyRequestSignatureMissingTimestamp() {
        assertFalse(verifySignature(constructDummyParamsNoTimestamp()));
    }

    @Test
    public void testVerifyRequestSignatureHandlesNullParams() {
        Map<String, String[]> params = constructDummyParamsNoSignature();
        params.put("b", new String[]{ null });
        params.put("sig", new String[]{"a3368bf718ba104dcb392d8877e8eb2b"});
        assertTrue(verifySignature(params));
    }

    @Test
    public void testVerifyRequestSignatureCurrentTimeMillis() {
        assertFalse(verifyRequestSignature(null, APPLICATION_JSON, constructDummyParams(), "abcde"));
    }

    private InputStream constructDummyRequestJson() {
        String dummyJson = "{\"a\":\"alphabet\",\"b\":\"bananas\",\"timestamp\":\"2100\",\"sig\":\"b7f749de27b4adcf736cc95c9a7e059a16c85127\"}";
        return new ByteArrayInputStream(dummyJson.getBytes(StandardCharsets.UTF_8));
    }

    private Map<String, String[]> constructDummyParamsNoTimestamp() {
        Map<String, String[]> params = new HashMap<>();
        params.put("a", new String[]{"alphabet"});
        params.put("b", new String[]{"bananas"});
        params.put("sig", new String[]{"7d43241108912b32cc315b48ce681acf"});
        return params;
    }

    private Map<String, String[]> constructDummyParamsNoSignature() {
        Map<String, String[]> params = new HashMap<>();
        params.put("a", new String[]{"alphabet"});
        params.put("b", new String[]{"bananas"});
        params.put("timestamp", new String[]{"2100"});
        return params;
    }

    private Map<String, String[]> constructDummyParamsInvalidTimestamp() {
        Map<String, String[]> params = constructDummyParamsNoTimestamp();
        params.put("timestamp", new String[]{"not a date time string"});
        return params;
    }

    private Map<String, String[]> constructDummyParams() {
        Map<String, String[]> params = constructDummyParamsNoSignature();
        params.put("sig", new String[]{"7d43241108912b32cc315b48ce681acf"});
        return params;
    }

    private Map<String, String[]> constructDummyParamsAltSignature() {
        Map<String, String[]> params = constructDummyParamsNoSignature();
        params.put("sig", new String[]{"b7f749de27b4adcf736cc95c9a7e059a16c85127"});
        return params;
    }

    private static boolean verifySignature(Map<String, String[]> params) {
        return verifyRequestSignature(APPLICATION_JSON, null, params, "abcde", 2100000);
    }
}
