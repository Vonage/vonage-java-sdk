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


import com.nexmo.client.auth.AuthCollection;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.JWTAuthMethod;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.nio.charset.Charset;
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
        public RequestBuilder makeRequest(String request) {
            return RequestBuilder.get(request);
        }

        @Override
        public String parseResponse(HttpResponse response) throws IOException {
            return "response";
        }
    }

    private static class ConcreteMethodFailingParse extends ConcreteMethod{
        public ConcreteMethodFailingParse(HttpWrapper httpWrapper){super(httpWrapper);}

        @Override
        public String parseResponse(HttpResponse response) throws IOException{
            throw new IOException("This is a test io exception from parse");
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
        @SuppressWarnings("unchecked") Set<Class> anySet = any(Set.class);
        when(mockAuthMethods.getAcceptableAuthMethod(anySet)).thenReturn(mockAuthMethod);

        when(mockWrapper.getHttpClient()).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenReturn(new BasicHttpResponse(new BasicStatusLine(new ProtocolVersion("1.1",
                1,
                1
        ), 200, "OK")));
        when(mockWrapper.getAuthCollection()).thenReturn(mockAuthMethods);
    }

    @Ignore
    @Test
    public void testExecute() {
        ConcreteMethod method = new ConcreteMethod(mockWrapper);

        String result = method.execute("url");
        assertEquals("response", result);
    }

    @Test
    public void testGetAuthMethod() {
        ConcreteMethod method = new ConcreteMethod(mockWrapper);

        AuthMethod auth = method.getAuthMethod(method.getAcceptableAuthMethods());
        assertEquals(auth, mockAuthMethod);
    }

    @Test
    public void testApplyAuth() {
        ConcreteMethod method = new ConcreteMethod(mockWrapper);

        RequestBuilder request = RequestBuilder.get("url");
        method.applyAuth(request);
        verify(mockAuthMethod).apply(request);
    }

    @Test
    public void testUsingUtf8Encoding() throws Exception {
        String json = "{\"text\":\"Questo è un test di chiamata\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        RequestBuilder builder = RequestBuilder
                .put("")
                .setHeader("Content-Type", "application/json")
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        ConcreteMethod method = spy(new ConcreteMethod(mockWrapper));
        when(method.makeRequest(any(String.class))).thenReturn(builder);
        when(mockAuthMethod.apply(any(RequestBuilder.class))).thenReturn(builder);

        method.execute("");

        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(mockHttpClient).execute(captor.capture());

        HttpEntity entity = ((HttpEntityEnclosingRequest) captor.getValue()).getEntity();

        String entityContents = IOUtils.toString(entity.getContent(), Charset.forName("UTF-8"));
        assertEquals(json, entityContents);
    }

    @Test
    public void testUsingUtf8EncodingChinese() throws Exception {
        String json = "{\"text\":\"您的纳控猫设备异常，请登录查看。\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        RequestBuilder builder = RequestBuilder
                .put("")
                .setCharset(Charset.forName("UTF-8"))
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        ConcreteMethod method = spy(new ConcreteMethod(mockWrapper));
        when(method.makeRequest(any(String.class))).thenReturn(builder);
        when(mockAuthMethod.apply(any(RequestBuilder.class))).thenReturn(builder);

        method.execute("");

        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(mockHttpClient).execute(captor.capture());

        HttpEntity entity = ((HttpEntityEnclosingRequest) captor.getValue()).getEntity();

        String entityContents = IOUtils.toString(entity.getContent(), Charset.forName("UTF-8"));

        assertEquals(json, entityContents);
    }

    @Test
    public void testFailedParse() throws Exception{
        ConcreteMethodFailingParse method = spy(new ConcreteMethodFailingParse(mockWrapper));
        String json = "{\"text\":\"Hello World\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        RequestBuilder builder = RequestBuilder
                .put("")
                .setCharset(Charset.forName("UTF-8"))
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        when(method.makeRequest(any(String.class))).thenReturn(builder);
        when(mockAuthMethod.apply(any(RequestBuilder.class))).thenReturn(builder);
        try{

            method.execute("");
            Assert.isTrue(false,"Should have gotten a Parsing exception");
        }
        catch (NexmoResponseParseException ex){
            Assert.isTrue(ex.getCause() instanceof IOException, "Unknown Exception Caused Throw");
        }
    }

    @Test
    public void testFailedHttpExecute() throws Exception{
        ConcreteMethodFailingParse method = spy(new ConcreteMethodFailingParse(mockWrapper));
        String json = "{\"text\":\"Hello World\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        RequestBuilder builder = RequestBuilder
                .put("")
                .setCharset(Charset.forName("UTF-8"))
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        when(method.makeRequest(any(String.class))).thenReturn(builder);
        when(mockAuthMethod.apply(any(RequestBuilder.class))).thenReturn(builder);
        IOException ex = new IOException("This is a test exception thrown from the HttpClient Execute method");
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenThrow(ex);
        try{
            method.execute("");
            Assert.isTrue(false, "There should have been a Nexmo Client exception thrown");
        }
        catch (NexmoMethodFailedException e){
            Assert.isTrue(e.getCause() instanceof IOException, "The cause of the exception was not correct");
        }
    }
}