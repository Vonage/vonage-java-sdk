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
package com.vonage.client;


import com.vonage.client.auth.AuthCollection;
import com.vonage.client.auth.AuthMethod;
import com.vonage.client.auth.JWTAuthMethod;
import com.vonage.client.logging.LoggingUtils;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.io.IOUtils;
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
import org.junit.*;
import org.mockito.ArgumentCaptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
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

    private static class ConcreteMethodFailingParse extends ConcreteMethod {
        public ConcreteMethodFailingParse(HttpWrapper httpWrapper) {
            super(httpWrapper);
        }

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
        when(LoggingUtils.logResponse(any(HttpResponse.class))).thenReturn("response logged");
        Set<Class> anySet = any(Set.class);
        when(mockAuthMethods.getAcceptableAuthMethod(anySet)).thenReturn(mockAuthMethod);
        when(mockWrapper.getHttpClient()).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenReturn(
            new BasicHttpResponse(
                new BasicStatusLine(
                    new ProtocolVersion(
                    "1.1",
                        1,
                        1
                    ),
         200,
       "OK"
                )
            )
        );
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

        String entityContents = IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
        assertEquals(json, entityContents);
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

        String entityContents = IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);

        assertEquals(json, entityContents);
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