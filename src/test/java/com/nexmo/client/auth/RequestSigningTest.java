/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nexmo.client.auth;


import com.nexmo.client.auth.RequestSigning;
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
