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

import static com.vonage.client.auth.RequestSigning.*;
import com.vonage.client.auth.hashutils.HashUtil;
import static com.vonage.client.auth.hashutils.HashUtil.HashType.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RequestSigningTest {
    final String secret = "abcde";
    final long time = 2100;
    final Map<String, String> inputParams = new LinkedHashMap<>(4);

    @BeforeEach
    public void beforeTest() {
        inputParams.clear();
        inputParams.put("a", "alphabet");
        inputParams.put("b", "bananas");
    }

    void assertEqualsSignature(HashUtil.HashType hashType, String sig) {
        var result = constructSignatureForRequestParameters(inputParams, secret, time, hashType);
        assertEquals(sig, result.get(PARAM_SIGNATURE));
    }

    @Test
    public void testConstructSignatureForRequestParameters() {
        String expected = "7d43241108912b32cc315b48ce681acf";
        assertEqualsSignature(MD5, expected);
        constructSignatureForRequestParameters(inputParams, secret, MD5);
        assertNotEquals(expected, inputParams.get(PARAM_SIGNATURE));
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
    public void testConstructSignatureForRequestParametersSkipsNullValues() {
        inputParams.put("b", null);
        assertEqualsSignature(MD5, "a3368bf718ba104dcb392d8877e8eb2b");
    }

    @Test
    public void testVerifyRequestSignature() {
        assertTrue(verifySignature(constructDummyParams()));
    }

    @Test
    public void testVerifyRequestSignatureWithSha1Hash() {
        Map<String, String[]> params = constructDummyParams();
        params.put("sig", new String[]{"b7f749de27b4adcf736cc95c9a7e059a16c85127"});

        assertTrue(verifyRequestSignature(
                RequestSigning.APPLICATION_JSON, null, params,
                secret, 2100000, HMAC_SHA1
        ));
    }

    @Test
    public void testVerifySignatureRequestJson() throws Exception {
        HttpServletRequest request = constructDummyRequestJson();
        assertTrue(verifyRequestSignature(
                RequestSigning.APPLICATION_JSON, request.getInputStream(), constructDummyParams(),
                secret, 2100000, HMAC_SHA1
        ));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacSha256Hash() {
        Map<String, String[]> params = constructDummyParams();
        params.put("sig", new String[]{"8d1b0428276b6a070578225914c3502cc0687a454dfbbbb370c76a14234cb546"});

        assertTrue(verifyRequestSignature(
                RequestSigning.APPLICATION_JSON, null, params,
                secret, 2100000, HMAC_SHA256
        ));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacMd5Hash() throws Exception {
        Map<String, String[]> params = constructDummyParams();
        params.put("sig", new String[]{"e0afe267aefd6dd18a848c1681517a19"});
        assertTrue(verifyRequestSignature(
                RequestSigning.APPLICATION_JSON, null, params,
                secret, 2100000, HMAC_MD5
        ));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacSha512Hash() {
        Map<String, String[]> params = constructDummyParams();
        params.put("sig", new String[]{"1c834a1f6a377d4473971387b065cb38e2ad6c4869ba77b7b53e207a344e87ba04b456dfc697b371a2d1ce476d01dafd4394aa97525eff23badad39d2389a710"});

        assertTrue(verifyRequestSignature(
                RequestSigning.APPLICATION_JSON, null, params,
                secret, 2100000, HMAC_SHA512
        ));
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
        Map<String, String[]> params = constructDummyParams();
        params.put("b", new String[]{ null });
        params.put("sig", new String[]{"a3368bf718ba104dcb392d8877e8eb2b"});
        assertTrue(verifySignature(params));
    }

    @Test
    public void testVerifyRequestSignatureCurrentTimeMillis() {
        assertFalse(verifyRequestSignature(null, APPLICATION_JSON, constructDummyParams(), "abcde"));
    }

    private HttpServletRequest constructDummyRequest() {
        return constructDummyRequest(null);
    }

    private HttpServletRequest constructDummyRequestJson() throws Exception  {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyJson = "{\"a\":\"alphabet\",\"b\":\"bananas\",\"timestamp\":\"2100\",\"sig\":\"b7f749de27b4adcf736cc95c9a7e059a16c85127\"}";
        when(request.getContentType()).thenReturn("application/json");
        final byte[] contentBytes = dummyJson.getBytes(StandardCharsets.UTF_8);
        ServletInputStream servletInputStream = new ServletInputStream() {
            private int index = 0;

            @Override
            public int read() {
                if (index < contentBytes.length) {
                    return contentBytes[index++] & 0xFF;
                }
                return -1;
            }

            @Override
            public boolean isFinished() {
                return index >= contentBytes.length;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // No need to implement for this example
            }
        };

        when(request.getInputStream()).thenReturn(servletInputStream);

        return request;
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
        Map<String, String[]> params = new HashMap<>();
        params.put("a", new String[]{"alphabet"});
        params.put("b", new String[]{"bananas"});
        params.put("timestamp", new String[]{"not a date time string"});
        params.put("sig", new String[]{"7d43241108912b32cc315b48ce681acf"});
        return params;
    }

    private Map<String, String[]> constructDummyParams() {
        Map<String, String[]> params = new HashMap<>();
        params.put("a", new String[]{"alphabet"});
        params.put("b", new String[]{"bananas"});
        params.put("timestamp", new String[]{"2100"});
        params.put("sig", new String[]{"7d43241108912b32cc315b48ce681acf"});
        return params;
    }

    private HttpServletRequest constructDummyRequest(final Map<String, String[]> nullableParams) {
        Map<String, String[]> params;
        params = Objects.requireNonNullElseGet(nullableParams, this::constructDummyParams);

        HttpServletRequest request = mock(HttpServletRequest.class);
        for (Map.Entry<String, String[]> pair : params.entrySet()) {
            when(request.getParameter(pair.getKey())).thenReturn(pair.getValue() == null ? null : pair.getValue()[0]);
        }

        when(request.getParameterMap()).thenReturn(params);

        return request;
    }

    private static boolean verifySignature(Map<String, String[]> params) {
        return verifyRequestSignature(APPLICATION_JSON, null, params, "abcde", 2100000);
    }
}
