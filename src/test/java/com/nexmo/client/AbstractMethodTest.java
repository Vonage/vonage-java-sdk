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
package com.nexmo.client;


import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.AuthCollection;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.JWTAuthMethod;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class AbstractMethodTest {
    private static class ConcreteMethod extends AbstractMethod<String, String> {
        public ConcreteMethod(HttpWrapper httpWrapper) {
            super(httpWrapper);
        }

        @Override
        protected Class[] getAcceptableAuthMethods() {
            return new Class[]{JWTAuthMethod.class};
        }

        @Override
        public RequestBuilder makeRequest(String request) throws NexmoClientException, UnsupportedEncodingException {
            return RequestBuilder.get(request);
        }

        @Override
        public String parseResponse(HttpResponse response) throws IOException {
            return "response";
        }
    }

    private HttpWrapper mockWrapper;
    private HttpClient mockHttpClient;
    private AuthCollection mockAuthMethods;
    private AuthMethod mockAuthMethod;

    @Before
    public void setUp() throws Exception {
        mockWrapper = mock(HttpWrapper.class);
        mockAuthMethods = mock(AuthCollection.class);
        mockAuthMethod = mock(AuthMethod.class);
        mockHttpClient = mock(HttpClient.class);
        @SuppressWarnings("unchecked")
        Set<Class> anySet = any(Set.class);
        when(mockAuthMethods.getAcceptableAuthMethod(anySet)).thenReturn(mockAuthMethod);

        when(mockWrapper.getHttpClient()).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenReturn(new BasicHttpResponse(
                new BasicStatusLine(
                        new ProtocolVersion("1.1", 1, 1), 200, "OK")));
        when(mockWrapper.getAuthCollection()).thenReturn(mockAuthMethods);
    }

    @Ignore
    @Test
    public void testExecute() throws Exception {
        ConcreteMethod method = new ConcreteMethod(mockWrapper);

        String result = method.execute("url");
        assertEquals("response", result);
    }

    @Test
    public void testGetAuthMethod() throws Exception {
        ConcreteMethod method = new ConcreteMethod(mockWrapper);

        AuthMethod auth = method.getAuthMethod(method.getAcceptableAuthMethods());
        assertEquals(auth, mockAuthMethod);
    }

    @Test
    public void testApplyAuth() throws Exception {
        ConcreteMethod method = new ConcreteMethod(mockWrapper);

        RequestBuilder request = RequestBuilder.get("url");
        method.applyAuth(request);
        verify(mockAuthMethod).apply(request);
    }
}
