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
package com.vonage.client.auth;


import com.vonage.client.auth.hashutils.HashUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestSigningTest {
    @Test
    public void testConstructSignatureForRequestParameters() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("a", "alphabet"));
        params.add(new BasicNameValuePair("b", "bananas"));

        RequestSigning.constructSignatureForRequestParameters(params, "abcde", 2100);
        Map<String, String> paramMap = constructParamMap(params);
        // md5 -s "&a=alphabet&b=bananas&timestamp=2100abcde"
        assertEquals("7d43241108912b32cc315b48ce681acf", paramMap.get(RequestSigning.PARAM_SIGNATURE));
    }

    @Test
    public void testConstructSignatureForRequestParametersWithSha1Hash() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("a", "alphabet"));
        params.add(new BasicNameValuePair("b", "bananas"));

        RequestSigning.constructSignatureForRequestParameters(params, "abcde", 2100, HashUtil.HashType.HMAC_SHA1);
        Map<String, String> paramMap = constructParamMap(params);
        // md5 -s "&a=alphabet&b=bananas&timestamp=2100"
        assertEquals("b7f749de27b4adcf736cc95c9a7e059a16c85127", paramMap.get(RequestSigning.PARAM_SIGNATURE));
    }

    @Test
    public void testConstructSignatureForRequestParametersWithHmacMd5Hash() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("a", "alphabet"));
        params.add(new BasicNameValuePair("b", "bananas"));

        RequestSigning.constructSignatureForRequestParameters(params, "abcde", 2100, HashUtil.HashType.HMAC_MD5);
        Map<String, String> paramMap = constructParamMap(params);
        // md5 -s "&a=alphabet&b=bananas&timestamp=2100"
        assertEquals("e0afe267aefd6dd18a848c1681517a19", paramMap.get(RequestSigning.PARAM_SIGNATURE));
    }

    @Test
    public void testConstructSignatureForRequestParametersWithHmacSha256Hash() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("a", "alphabet"));
        params.add(new BasicNameValuePair("b", "bananas"));

        RequestSigning.constructSignatureForRequestParameters(params, "abcde", 2100, HashUtil.HashType.HMAC_SHA256);
        Map<String, String> paramMap = constructParamMap(params);
        // md5 -s "&a=alphabet&b=bananas&timestamp=2100"
        assertEquals("8d1b0428276b6a070578225914c3502cc0687a454dfbbbb370c76a14234cb546", paramMap.get(RequestSigning.PARAM_SIGNATURE));
    }

    @Test
    public void testConstructSignatureForRequestParametersWithHmacSha512Hash() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("a", "alphabet"));
        params.add(new BasicNameValuePair("b", "bananas"));

        RequestSigning.constructSignatureForRequestParameters(params, "abcde", 2100, HashUtil.HashType.HMAC_SHA512);
        Map<String, String> paramMap = constructParamMap(params);
        // md5 -s "&a=alphabet&b=bananas&timestamp=2100"
        assertEquals("1c834a1f6a377d4473971387b065cb38e2ad6c4869ba77b7b53e207a344e87ba04b456dfc697b371a2d1ce476d01dafd4394aa97525eff23badad39d2389a710", paramMap.get(RequestSigning.PARAM_SIGNATURE));
    }

    @Test
    public void testConstructSignatureForRequestParametersSkipsSignature() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("a", "alphabet"));
        params.add(new BasicNameValuePair("b", "bananas"));
        params.add(new BasicNameValuePair("sig", "7d43241108912b32cc315b48ce681acf"));


        RequestSigning.constructSignatureForRequestParameters(params, "abcde", 2100);
        Map<String, String> paramMap = constructParamMap(params);
        // md5 -s "&a=alphabet&b=bananas&timestamp=2100abcde"
        assertEquals("7d43241108912b32cc315b48ce681acf", paramMap.get(RequestSigning.PARAM_SIGNATURE));
    }

    @Test
    public void testConstructSignatureForRequestParametersSkipsNullValues() {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("a", "alphabet"));
        params.add(new BasicNameValuePair("b", null));

        RequestSigning.constructSignatureForRequestParameters(params, "abcde", 2100);
        Map<String, String> paramMap = constructParamMap(params);
        // md5 -s "&a=alphabet&timestamp=2100abcde"
        assertEquals("a3368bf718ba104dcb392d8877e8eb2b", paramMap.get(RequestSigning.PARAM_SIGNATURE));
    }

    private static Map<String, String> constructParamMap(List<NameValuePair> params) {
        Map<String, String> paramMap = new HashMap<>();
        for (NameValuePair pair : params) {
            paramMap.put(pair.getName(), pair.getValue());
        }
        return paramMap;
    }

    @Test
    public void testVerifyRequestSignature() {
        HttpServletRequest request = constructDummyRequest();
        assertTrue(RequestSigning.verifyRequestSignature(request, "abcde", 2100000));
    }

    @Test
    public void testVerifyRequestSignatureWithSha1Hash() {
        Map<String, String[]> params = constructDummyParams();
        params.put("sig", new String[]{"b7f749de27b4adcf736cc95c9a7e059a16c85127"});

        assertTrue(RequestSigning.verifyRequestSignature(constructDummyRequest(params), "abcde", 2100000, HashUtil.HashType.HMAC_SHA1));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacSha256Hash() {
        Map<String, String[]> params = constructDummyParams();
        params.put("sig", new String[]{"8d1b0428276b6a070578225914c3502cc0687a454dfbbbb370c76a14234cb546"});

        assertTrue(RequestSigning.verifyRequestSignature(constructDummyRequest(params), "abcde", 2100000, HashUtil.HashType.HMAC_SHA256));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacMd5Hash() {
        Map<String, String[]> params = constructDummyParams();
        params.put("sig", new String[]{"e0afe267aefd6dd18a848c1681517a19"});

        assertTrue(RequestSigning.verifyRequestSignature(constructDummyRequest(params), "abcde", 2100000, HashUtil.HashType.HMAC_MD5));
    }

    @Test
    public void testVerifyRequestSignatureWithHmacSha512Hash() {
        Map<String, String[]> params = constructDummyParams();
        params.put("sig", new String[]{"1c834a1f6a377d4473971387b065cb38e2ad6c4869ba77b7b53e207a344e87ba04b456dfc697b371a2d1ce476d01dafd4394aa97525eff23badad39d2389a710"});

        assertTrue(RequestSigning.verifyRequestSignature(constructDummyRequest(params), "abcde", 2100000, HashUtil.HashType.HMAC_SHA512));
    }

    @Test
    public void testVerifyRequestSignatureNoSig() {
        HttpServletRequest request = constructDummyRequest();
        when(request.getParameter("sig")).thenReturn(null);

        assertFalse(RequestSigning.verifyRequestSignature(request, "abcde", 2100000));
    }

    @Test
    public void testVerifyRequestSignatureBadTimestamp() {
        HttpServletRequest request = constructDummyRequest();
        when(request.getParameter("timestamp")).thenReturn("not a date time string");

        assertFalse(RequestSigning.verifyRequestSignature(request, "abcde", 2100000));
    }

    @Test
    public void testVerifyRequestSignatureMissingTimestamp() {
        HttpServletRequest request = constructDummyRequest();
        when(request.getParameter("timestamp")).thenReturn(null);

        assertFalse(RequestSigning.verifyRequestSignature(request, "abcde", 2100000));
    }

    @Test
    public void testVerifyRequestSignatureHandlesNullParams() {
        Map<String, String[]> params = constructDummyParams();
        params.put("b", new String[]{ null });
        params.put("sig", new String[]{"a3368bf718ba104dcb392d8877e8eb2b"});

        HttpServletRequest request = constructDummyRequest(params);

        assertTrue(RequestSigning.verifyRequestSignature(request, "abcde", 2100000));
    }

    private HttpServletRequest constructDummyRequest() {
        return constructDummyRequest(null);
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
        if (nullableParams == null) {
            params = constructDummyParams();
        } else {
            params = nullableParams;
        }

        HttpServletRequest request = mock(HttpServletRequest.class);
        for (Map.Entry<String, String[]> pair : params.entrySet()) {
            when(request.getParameter(pair.getKey())).thenReturn(pair.getValue() == null ? null : pair.getValue()[0]);
        }

        when(request.getParameterMap()).thenReturn(params);

        return request;
    }
}
