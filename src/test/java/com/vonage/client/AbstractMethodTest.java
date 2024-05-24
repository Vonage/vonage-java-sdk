/*
 *   Copyright 2024 Vonage
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

import com.sun.net.httpserver.HttpServer;
import static com.vonage.client.TestUtils.*;
import com.vonage.client.auth.*;
import com.vonage.client.auth.hashutils.HashUtil;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AbstractMethodTest {

    private static class ConcreteMethod extends AbstractMethod<String, String> {
        public ConcreteMethod(HttpWrapper httpWrapper) {
            super(httpWrapper);
        }

        RequestBuilder makeRequest() throws UnsupportedEncodingException {
            return makeRequest("http://example.org/resource")
                    .addParameter("foo", "bar")
                    .addParameter("BAZINGA", "Yes");
        }

        @Override
        protected Set<Class<? extends AuthMethod>> getAcceptableAuthMethods() {
            return Set.of(AuthMethod.class);
        }

        @Override
        public RequestBuilder makeRequest(String request) throws UnsupportedEncodingException {
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
        public String parseResponse(HttpResponse response) throws IOException {
            throw new IOException("This is a test IOException from parseResponse.");
        }
    }

    static String getEntityContentsAsString(HttpEntity entity) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    private HttpServer httpServer;
    private HttpWrapper mockWrapper;
    private HttpClient mockHttpClient;
    private AuthMethod mockAuthMethod;
    private final HttpResponse basicResponse = new BasicHttpResponse(
            new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
    );

    @BeforeEach
    public void setUp() throws Exception {
        mockWrapper = mock(HttpWrapper.class);
        AuthCollection mockAuthMethods = mock(AuthCollection.class);
        mockAuthMethod = mock(AuthMethod.class);
        mockHttpClient = mock(HttpClient.class);
        when(mockAuthMethods.getAcceptableAuthMethod(any())).thenReturn(mockAuthMethod);
        when(mockWrapper.getHttpClient()).thenReturn(mockHttpClient);
        when(mockHttpClient.execute(any(HttpUriRequest.class))).thenReturn(basicResponse);
        when(mockWrapper.getAuthCollection()).thenReturn(mockAuthMethods);
    }

    ConcreteMethod mockJsonResponse(String json, boolean failing) throws Exception {
        ConcreteMethod method = spy(failing ?
                new ConcreteMethodFailingParse(mockWrapper) : new ConcreteMethod(mockWrapper)
        );
        RequestBuilder builder = RequestBuilder.put("")
                .setHeader("Content-Type", "application/json")
                .setHeader("Accept", "application/json")
                .setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

        when(method.makeRequest(any(String.class))).thenReturn(builder);
        return method;
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
        AuthMethod auth = method.getAuthMethod();
        assertEquals(auth, mockAuthMethod);
    }

    @Test
    public void testApplyAuth() throws Exception {
        var method = new ConcreteMethod(mockWrapper);
        var request = method.makeRequest();
        assertEquals(request, method.applyAuth(request));
        assertEquals(2, request.getParameters().size());

        var jwtAuthCollection = new AuthCollection(new JWTAuthMethod(
                APPLICATION_ID, new TestUtils().loadKey("test/keys/application_key")
        ));
        when(mockWrapper.getAuthCollection()).thenReturn(jwtAuthCollection);
        request = method.makeRequest();
        assertEquals(request, method.applyAuth(request));
        assertEquals(2, request.getParameters().size());
        var expectedHeaderStart = "Bearer eyJ0eXBlIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.";
        var authHeaderValue = request.getFirstHeader("Authorization").getValue();
        assertTrue(authHeaderValue.startsWith(expectedHeaderStart));

        var headerApiKeyAuthCollection = new AuthCollection(new ApiKeyHeaderAuthMethod(API_KEY, API_SECRET));
        when(mockWrapper.getAuthCollection()).thenReturn(headerApiKeyAuthCollection);
        request = method.makeRequest();
        assertEquals(request, method.applyAuth(request));
        assertEquals(2, request.getParameters().size());
        var expectedHeader = "Basic "+Base64.encodeBase64String((API_KEY+":"+API_SECRET).getBytes());
        authHeaderValue = request.getFirstHeader("Authorization").getValue();
        assertEquals(expectedHeader, authHeaderValue);

        var qpApiKeyAuthCollection = new AuthCollection(new ApiKeyQueryParamsAuthMethod(API_KEY, API_SECRET));
        when(mockWrapper.getAuthCollection()).thenReturn(qpApiKeyAuthCollection);
        request = method.makeRequest();
        assertEquals(request, method.applyAuth(request));
        assertEquals(4, request.getParameters().size());
        var paramsMap = AbstractMethod.normalRequestParams(request).toMap();
        assertEquals(4, paramsMap.size());
        assertEquals(API_KEY, paramsMap.get("api_key"));
        assertEquals(API_SECRET, paramsMap.get("api_secret"));

        var sigAuthCollection = new AuthCollection(new SignatureAuthMethod(
                API_KEY, SIGNATURE_SECRET, HashUtil.HashType.HMAC_SHA256
        ));
        when(mockWrapper.getAuthCollection()).thenReturn(sigAuthCollection);

        request = method.makeRequest();
        assertEquals(request, method.applyAuth(request));
        assertEquals(5, request.getParameters().size());
        paramsMap = AbstractMethod.normalRequestParams(request).toMap();
        assertEquals(5, paramsMap.size());
        assertEquals(API_KEY, paramsMap.get("api_key"));
        assertEquals("Yes", paramsMap.get("BAZINGA"));
        assertEquals("bar", paramsMap.get("foo"));
        assertTrue(System.currentTimeMillis() > Long.parseLong(paramsMap.get("timestamp")));
        assertTrue(paramsMap.get("sig").length() > 16);
    }

    @Test
    public void testUsingUtf8Encoding() throws Exception {
        String json = "{\"text\":\"Questo è un test di chiamata\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        ConcreteMethod method = mockJsonResponse(json, false);

        method.execute("");

        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(mockHttpClient).execute(captor.capture());

        HttpEntity entity = ((HttpEntityEnclosingRequest) captor.getValue()).getEntity();
        assertEquals(json, getEntityContentsAsString(entity));
    }

    @Test
    public void testUsingUtf8EncodingChinese() throws Exception {
        String json = "{\"text\":\"您的纳控猫设备异常，请登录查看。\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        ConcreteMethod method = mockJsonResponse(json, false);

        method.execute("");

        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(mockHttpClient).execute(captor.capture());

        HttpEntity entity = ((HttpEntityEnclosingRequest) captor.getValue()).getEntity();
        assertEquals(json, getEntityContentsAsString(entity));
    }

    @Test
    public void rse() throws Exception {
        String json = "{\"text\":\"Hello World\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        ConcreteMethod method = mockJsonResponse(json, true);
        try {
            method.execute("");
            Assert.isTrue(false,"Should have gotten a Parsing exception");
        }
        catch (VonageResponseParseException ex){
            Assert.isTrue(ex.getCause() instanceof IOException, "Unknown Exception Caused Throw");
        }
    }

    @Test
    public void testFailedHttpExecute() throws Exception {
        String json = "{\"text\":\"Hello World\",\"loop\":0,\"voice_name\":\"Kimberly\"}";
        ConcreteMethod method = mockJsonResponse(json, true);
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

    private ConcreteMethod mockServerAndMethod(int clientTimeout, int serverTimeout) throws Exception {
        if (httpServer != null) {
            httpServer.stop(0);
            httpServer = null;
        }
        mockWrapper = new HttpWrapper();
        mockWrapper.setHttpConfig(HttpConfig.builder().timeoutMillis(clientTimeout).build());
        final int port = 8049;
        String endpointPath = "/test";
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext(endpointPath, exchange -> {
            if (serverTimeout > 0) {
                try {
                    Thread.sleep(serverTimeout);
                }
                catch (InterruptedException ex) {
                    fail(ex.getMessage());
                }
            }

            String response = new Scanner(exchange.getRequestBody()).useDelimiter("\\A").next();
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        httpServer.setExecutor(Executors.newSingleThreadExecutor());
        httpServer.start();

        RequestBuilder builder = RequestBuilder.get("http://localhost:" + port + endpointPath);
        class LocalMethod extends ConcreteMethod {
            LocalMethod() {
                super(mockWrapper);
            }

            @Override
            protected AuthMethod getAuthMethod() throws VonageUnexpectedException {
                return mockAuthMethod;
            }

            @Override
            public RequestBuilder makeRequest(String request) throws UnsupportedEncodingException {
                return builder.setEntity(new StringEntity(request));
            }
        }
        return new LocalMethod();
    }

    @Test
    public void testSocketTimeout() throws Exception {
        // For some reason this test is flaky on GitHub's CI runner
        if (
            System.getProperty("os.name").startsWith("Windows") &&
            System.getProperty("java.version").startsWith("1.")
        ) return;

        ConcreteMethod method = mockServerAndMethod(9000, 0);
        String requestBody = "Hello, World!";
        assertEquals(requestBody, method.execute(requestBody));

        method = mockServerAndMethod(970, 1200);
        try {
            method.execute(requestBody);
            fail("Expected VonageClientException");
        }
        catch (VonageClientException ex) {
            assertEquals(SocketTimeoutException.class, ex.getCause().getClass());
        }
        method = mockServerAndMethod(3000, 144);
        requestBody = "Hello again...";
        assertEquals(requestBody, method.execute(requestBody));

        assertThrows(IllegalArgumentException.class, () -> mockServerAndMethod(0, 1));
    }
}