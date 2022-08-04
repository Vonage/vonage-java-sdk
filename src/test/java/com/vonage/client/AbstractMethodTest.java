/*
 *   Copyright 2022 Vonage
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
package com.vonage.client;

import com.vonage.client.auth.AuthCollection;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import io.jsonwebtoken.lang.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AbstractMethodTest {

    static {
        TestUtils.mockStaticLoggingUtils();
    }

    private static class ConcreteMethod extends AbstractMethod<String, String> {
        public ConcreteMethod(HttpWrapper httpWrapper) {
            super(httpWrapper);
        }

        @Override
        protected Class<?>[] getAcceptableAuthMethods() {
            return new Class<?>[]{JWTAuthMethod.class};
        }

        @Override
        public RequestBuilder makeRequest(String request) {
            return RequestBuilder.get(request);
        }

        @Override
        public String parseResponse(HttpResponse response) throws IOException {
            return basicResponseHandler.handleResponse(response);
        }
    }

    private static class ConcreteMethodFailingParse extends ConcreteMethod {
        public ConcreteMethodFailingParse(HttpWrapper httpWrapper) {
            super(httpWrapper);
        }

        @Override
        public String parseResponse(HttpResponse response) throws IOException{
            throw new IOException("This is a test io exception from parse");
        }
    }

    static String getEntityContentsAsString(HttpEntity entity) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private HttpWrapper mockWrapper;
    private HttpClient mockHttpClient;
    private AuthCollection mockAuthMethods;
    private AuthMethod mockAuthMethod;
    private HttpResponse basicResponse = new BasicHttpResponse(
            new BasicStatusLine(
                    new ProtocolVersion("1.1", 1, 1),
                    200,
                    "OK"
            )
    );

    @Before
    public void setUp() throws Exception {
        mockWrapper = mock(HttpWrapper.class);
        mockAuthMethods = mock(AuthCollection.class);
        mockAuthMethod = mock(AuthMethod.class);
        mockHttpClient = mock(HttpClient.class);
        when(mockAuthMethod.apply(any(RequestBuilder.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, RequestBuilder.class));
        when(LoggingUtils.logResponse(any(HttpResponse.class))).thenReturn("response logged");
        when(mockAuthMethods.getAcceptableAuthMethod(any())).thenReturn(mockAuthMethod);
        when(mockWrapper.getHttpClient()).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenReturn(basicResponse);
        when(mockWrapper.getAuthCollection()).thenReturn(mockAuthMethods);
    }

    @Test
    public void testExecuteHeaders() throws Exception {
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenAnswer(invocation -> {
            HttpUriRequest request = invocation.getArgument(0, HttpUriRequest.class);
            String headers = Arrays.stream(request.getAllHeaders())
                    .map(Object::toString)
                    .collect(Collectors.joining(System.lineSeparator()));

            basicResponse.setEntity(new StringEntity(headers));
            return basicResponse;
        });

        String userAgent = "vonage-java-sdk/X.Y.Z java/"+System.getProperty("java.version");
        when(mockWrapper.getUserAgent()).thenReturn(userAgent);

        ConcreteMethod method = new ConcreteMethod(mockWrapper);
        String result = method.execute("url");
        String expected = "User-Agent: "+userAgent;
        assertEquals(expected, result);
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
                .setHeader("Accept", "application/json")
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        ConcreteMethod method = spy(new ConcreteMethod(mockWrapper));
        when(method.makeRequest(any(String.class))).thenReturn(builder);
        when(mockAuthMethod.apply(any(RequestBuilder.class))).thenReturn(builder);

        method.execute("");

        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(mockHttpClient).execute(captor.capture());

        HttpEntity entity = ((HttpEntityEnclosingRequest) captor.getValue()).getEntity();
        assertEquals(json, getEntityContentsAsString(entity));
    }

    @Test
    public void testUsingUtf8EncodingChinese() throws Exception {
        String json = "{\"text\":\"您的纳控猫设备异常，请登录查看。\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        RequestBuilder builder = RequestBuilder
                .put("")
                .setCharset(StandardCharsets.UTF_8)
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        ConcreteMethod method = spy(new ConcreteMethod(mockWrapper));
        when(method.makeRequest(any(String.class))).thenReturn(builder);
        when(mockAuthMethod.apply(any(RequestBuilder.class))).thenReturn(builder);

        method.execute("");

        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(mockHttpClient).execute(captor.capture());

        HttpEntity entity = ((HttpEntityEnclosingRequest) captor.getValue()).getEntity();
        assertEquals(json, getEntityContentsAsString(entity));
    }

    @Test
    public void rse() throws Exception {
        ConcreteMethodFailingParse method = spy(new ConcreteMethodFailingParse(mockWrapper));
        String json = "{\"text\":\"Hello World\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        RequestBuilder builder = RequestBuilder
                .put("")
                .setCharset(StandardCharsets.UTF_8)
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        when(method.makeRequest(any(String.class))).thenReturn(builder);
        when(mockAuthMethod.apply(any(RequestBuilder.class))).thenReturn(builder);
        try{

            method.execute("");
            Assert.isTrue(false,"Should have gotten a Parsing exception");
        }
        catch (VonageResponseParseException ex){
            Assert.isTrue(ex.getCause() instanceof IOException, "Unknown Exception Caused Throw");
        }
    }

    @Test
    public void testFailedHttpExecute() throws Exception {
        ConcreteMethodFailingParse method = spy(new ConcreteMethodFailingParse(mockWrapper));
        String json = "{\"text\":\"Hello World\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        RequestBuilder builder = RequestBuilder
                .put("")
                .setCharset(StandardCharsets.UTF_8)
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        when(method.makeRequest(any(String.class))).thenReturn(builder);
        when(mockAuthMethod.apply(any(RequestBuilder.class))).thenReturn(builder);
        IOException ex = new IOException("This is a test exception thrown from the HttpClient Execute method");
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenThrow(ex);
        try {
            method.execute("");
            Assert.isTrue(false, "There should have been a Vonage Client exception thrown");
        }
        catch (VonageMethodFailedException e) {
            Assert.isTrue(e.getCause() instanceof IOException, "The cause of the exception was not correct");
        }
    }
}